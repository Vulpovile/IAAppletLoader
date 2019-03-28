package com.androdome.iadventure;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.androdome.iadventure.dialogutils.InformationDialog;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CrashDialog extends InformationDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the dialog.
	 */
	public CrashDialog(String stacktrace) {
		super("404 - Adventure Not Found!", "Internet Adventure has crashed!", null);
		
		setModal(true);
		try
		{
			setIcon(ImageIO.read(this.getClass().getResourceAsStream("/stopscale.png")));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setBounds(100, 100, 593, 427);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel panel = new JPanel();
			panel.setBorder(new EmptyBorder(5, 5, 0, 5));
			getContentPane().add(panel, BorderLayout.NORTH);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblInternetAdventureHas = new JLabel("<html>Internet Adventure has experienced an unexpected exception. Please send the following to developers:</html>");
				panel.add(lblInternetAdventureHas);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane);
				{
					JTextArea textArea = new JTextArea(stacktrace);
					textArea.setEditable(false);
					scrollPane.setViewportView(textArea);
				}
			}
		}
		getFooterPane().setLayout(new BorderLayout(0, 0));
		{
			JPanel buttonPane = new JPanel();
			getFooterPane().add(buttonPane, BorderLayout.EAST);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		{
			JPanel panel = new JPanel();
			getFooterPane().add(panel, BorderLayout.NORTH);
		}
	}

}
