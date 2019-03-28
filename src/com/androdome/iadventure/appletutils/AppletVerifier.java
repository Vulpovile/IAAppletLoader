package com.androdome.iadventure.appletutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class AppletVerifier {
	public static enum Signage {
		CORRUPT, UNSIGNED, EXPIRED, SELFSIGNED, SIGNED
	}

	public static CertData verifySignage(URL archive) {
		URL jarUrl;
		try
		{
			jarUrl = new URL("jar:" + archive.toString() + "!/");
		}
		catch (MalformedURLException e1)
		{
			e1.printStackTrace();
			return new CertData(Signage.CORRUPT, null);
		}
		JarFile jf = null;
		try
		{
			Vector<JarEntry> entriesVec = new Vector<JarEntry>();
			jf = ((JarURLConnection) jarUrl.openConnection()).getJarFile();
			Manifest man = jf.getManifest();
			if (man == null)
				return new CertData(Signage.UNSIGNED, null);
			else
			{
				try
				{
					byte[] buffer = new byte[8192];
					Enumeration<JarEntry> entries = jf.entries();

					while (entries.hasMoreElements())
					{
						JarEntry je = (JarEntry) entries.nextElement();

						// Skip directories.
						if (je.isDirectory())
							continue;

						entriesVec.addElement(je);
						InputStream is = jf.getInputStream(je);

						// Read in each jar entry. A security exception will
						// be thrown if a signature/digest check fails.
						int n;
						while ((n = is.read(buffer, 0, buffer.length)) != -1)
						{
							// Don't care
						}
						is.close();
					}

					Enumeration<JarEntry> e = entriesVec.elements();
					while (e.hasMoreElements())
					{
						JarEntry je = (JarEntry) e.nextElement();

						// Every file must be signed except files in META-INF.
						Certificate[] certs = je.getCertificates();
						if ((certs == null) || (certs.length == 0))
						{
							if (!je.getName().startsWith("META-INF"))
								return new CertData(Signage.UNSIGNED, null);
						}
						else
						{
							// Check whether the file is signed by the expected
							// signer. The jar may be signed by multiple
							// signers.
							// See if one of the signers is 'targetCert'.
							TrustManagerFactory factory = TrustManagerFactory.getInstance("PKIX");
							factory.init((KeyStore)null);
							TrustManager[] trustManagers = factory.getTrustManagers();
							CertData currTrusted = null;
							int startIndex = 0;
							X509Certificate[] certChain;
							while ((certChain = getAChain(certs, startIndex)) != null)
							{
								try
								{
									certChain[0].checkValidity();
									if(true)
									{
										for(TrustManager trustedManager : trustManagers)
										{
											if(trustedManager instanceof X509TrustManager)
											{
												X509TrustManager tm = (X509TrustManager) trustedManager;
												try
												{
													tm.checkClientTrusted(certChain, certChain[0].getSigAlgOID());
												
													return new CertData(Signage.SIGNED, certChain[0]);
												}
												catch (CertificateException e1)
												{
													try
													{
														tm.checkServerTrusted(certChain, certChain[0].getSigAlgOID());
														
														return new CertData(Signage.SIGNED, certChain[0]);
													}
													catch (CertificateException e2)
													{
														// TODO Auto-generated catch block
														e2.printStackTrace();
													}
												}
												
											}
										}
										
									}
										if(currTrusted == null || currTrusted.signage.compareTo(Signage.SELFSIGNED) > 0)
											currTrusted =  new CertData(Signage.SELFSIGNED, certChain[0]);
									
								}
								catch (CertificateExpiredException e1)
								{
									// TODO Auto-generated catch block
									e1.printStackTrace();
									if(currTrusted == null || currTrusted.signage.compareTo(Signage.EXPIRED) > 0)
										currTrusted = new CertData(Signage.EXPIRED, certChain[0]);
								}
								catch (CertificateNotYetValidException e1)
								{
									// TODO Auto-generated catch block
									e1.printStackTrace();
									if(currTrusted == null || currTrusted.signage.compareTo(Signage.CORRUPT) > 0)
										currTrusted = new CertData(Signage.CORRUPT, certChain[0]);
								}
								startIndex += certChain.length;
							}
							if(currTrusted != null)
								return currTrusted;
						}
					}
					return new CertData(Signage.UNSIGNED, null);
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
					return new CertData(Signage.CORRUPT, null);
				}
				catch (NoSuchAlgorithmException e2)
				{
					e2.printStackTrace();
					return new CertData(Signage.CORRUPT, null);
				}
				catch (KeyStoreException e2)
				{
					e2.printStackTrace();
					return new CertData(Signage.CORRUPT, null);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return new CertData(Signage.CORRUPT, null);
	}

	private static X509Certificate[] getAChain(Certificate[] certs, int startIndex) {
		if (startIndex > certs.length - 1)
			return null;

		int i;
		// Keep going until the next certificate is not the
		// issuer of this certificate.
		for (i = startIndex; i < certs.length - 1; i++)
		{
			if (!((X509Certificate) certs[i + 1]).getSubjectDN().equals(((X509Certificate) certs[i]).getIssuerDN()))
			{
				break;
			}
		}
		// Construct and return the found certificate chain.
		int certChainSize = (i - startIndex) + 1;
		X509Certificate[] ret = new X509Certificate[certChainSize];
		for (int j = 0; j < certChainSize; j++)
		{
			ret[j] = (X509Certificate) certs[startIndex + j];
		}
		return ret;
	}

}
