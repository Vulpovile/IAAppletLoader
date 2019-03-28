package com.androdome.iadventure.dialogutils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JSeparator;

public class InformationFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final JPanel panel_1 = new JPanel();

	private JPanel titlePanel = new JPanel();
	private final JSeparator separator = new JSeparator();
	private final JSeparator separator_1 = new JSeparator();
	private final JPanel content = new JPanel();
	private final JPanel footer = new JPanel();
	private JLabel lblIcon = new JLabel("");
	private JLabel lblTitle = new JLabel();
	@Override
	public JPanel getContentPane()
	{
		return content;
	}
	
	@Override
	public Component add(Component comp)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void remove(Component comp)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void removeAll()
	{
		throw new UnsupportedOperationException();
	}
	
	public JPanel getFooterPane()
	{
		return footer;
	}
	
	public void setIcon(Image icon)
	{
		lblIcon.setIcon(new ImageIcon(icon.getScaledInstance(-1, 54, Image.SCALE_SMOOTH)));
	}
	
	public void setTitleContent(String titleContent)
	{
		lblTitle.setText("<html>"+titleContent+"</html>");
	}
	
	public InformationFrame(String title, String titleContent, Image icon) {

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setAlwaysOnTop(true);
		setTitle(title);
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(null);
		lblTitle.setText("<html>"+titleContent+"</html>");
		contentPanel.setLayout(new BorderLayout(0, 0));

		titlePanel.setPreferredSize(new Dimension(-1, 64));
		titlePanel.setBackground(Color.WHITE);
		titlePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new BorderLayout(0, 0));

		
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
		titlePanel.add(lblTitle);

		lblIcon.setHorizontalAlignment(SwingConstants.TRAILING);
		if(icon != null)
			lblIcon.setIcon(new ImageIcon(icon.getScaledInstance(-1, 54, Image.SCALE_SMOOTH)));
		titlePanel.add(lblIcon, BorderLayout.EAST);

		panel_1.setBorder(null);
		contentPanel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		panel_1.add(separator, BorderLayout.NORTH);
		
		panel_1.add(separator_1, BorderLayout.SOUTH);
		
		panel_1.add(content, BorderLayout.CENTER);
		

		footer.setPreferredSize(new Dimension(-1, 48));
		footer.setBorder(null);
		contentPanel.add(footer, BorderLayout.SOUTH);
		setContentPane(contentPanel);
		pack();
		this.setLocationRelativeTo(null);
	}


}
