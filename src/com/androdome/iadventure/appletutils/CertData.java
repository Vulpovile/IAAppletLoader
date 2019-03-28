package com.androdome.iadventure.appletutils;

import java.security.cert.X509Certificate;

import com.androdome.iadventure.appletutils.AppletVerifier.Signage;

public class CertData {
	public final Signage signage;
	public final X509Certificate cert;
	public CertData(Signage sg, X509Certificate cert) {
		signage = sg;
		this.cert = cert;
	}

}
