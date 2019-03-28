package com.androdome.iadventure.appletutils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import com.androdome.iadventure.appletutils.AppletVerifier.Signage;
import com.androdome.iadventure.dialogutils.InformationFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.BoxLayout;

import java.awt.Component;

import javax.swing.Box;

public class AppletAcceptDialog extends InformationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int dialogResult = 0;
	static final int DIALOG_RUN = 1;
	static final int DIALOG_CANCEL = 2;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 * 
	 * @param codeBase
	 * @param className
	 * @param archives
	 * @param name
	 */

	static File trustedSites = new File("TrustedSites.txt");
	static ArrayList<String> trusted = new ArrayList<String>();

	static
	{
		if (trustedSites.exists())
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(trustedSites));
				String line;
				while ((line = reader.readLine()) != null)
					trusted.add(line);
				reader.close();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				trustedSites.createNewFile();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean isSiteTrusted(String codebase) {
		return trusted.contains(codebase.toLowerCase());
		// return true;
	}

	public void setSiteTrusted(String codebase) {
		codebase = codebase.toLowerCase();
		trusted.add(codebase);
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(trustedSites));
			for (int i = 0; i < trusted.size(); i++)
			{
				writer.write(trusted.get(i));
				writer.newLine();
			}
			writer.close();
		}
		catch (Exception ex)
		{
		}
	}

	String codebase;
	CertData signage = null;

	public AppletAcceptDialog(String name, URL[] archives, String className, final String codeBase) {
		
		super("Warning - Security", "An applet wants to run", null);
		this.setPreferredSize(new Dimension(580, 360));
		setResizable(false);
		getContentPane().setBorder(new EmptyBorder(5, 5, 0, 5));
		try
		{
			if (archives != null)
			{
				for (int i = 0; i < archives.length; i++)
				{
					CertData newSignage = AppletVerifier.verifySignage(archives[0]);
					if (signage == null || newSignage.signage.compareTo(signage.signage) < 0)
						signage = newSignage;
				}
				signage = AppletVerifier.verifySignage(archives[0]);
				System.out.println(signage.signage);
				if (signage.signage == Signage.UNSIGNED)
					setIcon(ImageIO.read(this.getClass().getResourceAsStream("/warnscale.png")));
				else if (signage.signage == Signage.SELFSIGNED || signage.signage == Signage.EXPIRED)
					setIcon(ImageIO.read(this.getClass().getResourceAsStream("/questionscale.png")));
				else if (signage.signage == Signage.SIGNED)
					setIcon(ImageIO.read(this.getClass().getResourceAsStream("/okayscale.png")));
				else setIcon(ImageIO.read(this.getClass().getResourceAsStream("/stopscale.png")));

			}
			else{
				signage = new CertData(Signage.UNSIGNED, null);
				setIcon(ImageIO.read(this.getClass().getResourceAsStream("/warnscale.png")));
			}

		}
		catch (IOException e1)
		{
			signage = new CertData(Signage.UNSIGNED, null);
			e1.printStackTrace();
		}
		

			switch(signage.signage)
			{
				case UNSIGNED:
					setTitleContent("This Applet does not have a valid signature.<br>Do you want to excecute?");
					break;
				case CORRUPT:
					setTitleContent("This Applet has a corrupt signature, It may be malicious. Do you want to excecute?");
					break;
				case EXPIRED:
					setTitleContent("This Applets signature has expired and can't be verified. Do you want to excecute?");
					break;
				case SELFSIGNED:
					setTitleContent("This Applet is self-signed and can't be verified.<br>Do you want to excecute?");
					break;
				case SIGNED:
					setTitleContent("This Applet is verified and secure.<br>Do you want to excecute?");
					break;
				default:
					setTitleContent("What");
					break;
					
			}

		codebase = codeBase;
		if (isSiteTrusted(codeBase))
		{
			this.dialogResult = DIALOG_RUN;
			return;
		}
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setAlwaysOnTop(true);
		setTitle("Warning - Security");

		String ar = "";
		if(archives != null)
		{
			for (int i = 0; i < archives.length; i++)
			{
				if (i != 0)
					ar += ", ";
				ar += archives[i].toString().replace(codeBase, "");
			}
		}
		else ar = className;
		getContentPane().setLayout(new BorderLayout(0, 0));

		final JCheckBox chckbxDoNotAsk = new JCheckBox("Always trust content from this codebase");
		getContentPane().add(chckbxDoNotAsk, BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		scrollPane.setViewportView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		//Name
		panel.add(new JLabel("<html><b>Name:</b> "+name+"</html>"));
		panel.add(Box.createVerticalStrut(10));
		//Archives
		panel.add(new JLabel("<html><b>Archives:</b> "+ar+"</html>"));
		panel.add(Box.createVerticalStrut(10));
		
		panel.add(new JLabel("<html><b>Main Class:</b> "+className+"</html>"));
		panel.add(Box.createVerticalStrut(10));
		
		if(this.signage.cert != null)
		{
		
			String sName = signage.cert.getSubjectDN().getName();
			String iName = signage.cert.getIssuerDN().getName();
			System.out.println(sName);
			System.out.println(iName);
			//Publisher
			panel.add(new JLabel("<html><b>Publisher:</b> "+getValByAttributeTypeFromIssuerDN(sName, "O")+"</html>"));
			panel.add(Box.createVerticalStrut(10));
			panel.add(new JLabel("<html><b>Issuer:</b> "+getValByAttributeTypeFromIssuerDN(iName, "O")+"</html>"));
			panel.add(Box.createVerticalStrut(10));
		}
		else
		{
			panel.add(new JLabel("<html><b>Publisher:</b> UNKNOWN</html>"));
			panel.add(Box.createVerticalStrut(10));
		}
		panel.add(new JLabel("<html><b>Codebase:</b> "+codeBase+"</html>"));
		pack();
		this.setLocationRelativeTo(null);
		getFooterPane().setLayout(null);
		
		JButton btnOkay = new JButton("Cancel");
		btnOkay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AppletAcceptDialog.this.dialogResult = AppletAcceptDialog.DIALOG_CANCEL;
				dispose();
			}
		});
		btnOkay.setBounds(484, 11, 80, 23);
		getFooterPane().add(btnOkay);
		
		JButton button = new JButton("Run");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AppletAcceptDialog.this.dialogResult = AppletAcceptDialog.DIALOG_RUN;
				dispose();
			}
		});
		button.setBounds(394, 11, 80, 23);
		getFooterPane().add(button);
		
		JLabel lblRunningUntrustedApplets = new JLabel("Running untrusted applets could be a security risk");
		lblRunningUntrustedApplets.setBounds(10, 15, 310, 14);
		getFooterPane().add(lblRunningUntrustedApplets);
	}

	@Override()
	public void setVisible(boolean vis) {
		if (isSiteTrusted(codebase))
		{
			this.dialogResult = DIALOG_RUN;
			this.dispose();
		}
		else super.setVisible(vis);
	}

	private String getValByAttributeTypeFromIssuerDN(String dn, String attributeType)
	{
		LdapName ln;
		try
		{
			ln = new LdapName(dn);
		}
		catch (InvalidNameException e)
		{
			e.printStackTrace();
			return null;
		}

		for(Rdn rdn : ln.getRdns()) {
		    if(rdn.getType().equalsIgnoreCase(attributeType)) {
		        return rdn.getValue().toString();
		    }
		}
		return null;
	}
}
