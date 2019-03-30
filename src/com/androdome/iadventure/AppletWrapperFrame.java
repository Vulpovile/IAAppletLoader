package com.androdome.iadventure;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.androdome.iadventure.appletutils.AppletManager;
import com.androdome.iadventure.appletutils.ExtendedAppletContext;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class AppletWrapperFrame extends JDialog {
	/**
	 * 
	 */

	ExtendedAppletContext context = new ExtendedAppletContext();
	private static final long serialVersionUID = 1L;
	HashMap<String, String> props = new HashMap<String, String>();
	ArrayList<String> archives = new ArrayList<String>();
	String className = "";
	String name = "";
	String codebase = "";
	boolean isJar = false;
	private Applet applet;
	
	public static final int RESX = 0;
	public static final int RESY = 1;
	public static final int POSX = 2;
	public static final int POSY = 3;
	public static final int SD = 4;
	public static final int SHOW = 5;
	public static final int HIDE = 6;
	public static final int VPRECT = 7;
	public static final int FULLRECT = 8;
	JPanel appletContainer = new JPanel();
	Dimension origSize;
	public AppletWrapperFrame(String[] args) {
		this.setUndecorated(true);
		this.setBounds(50, 50, 500, 500);
		getContentPane().setLayout(null);
		appletContainer.setLayout(new BorderLayout());
		add(appletContainer);
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].startsWith("param:"))
			{
				String param = args[i].replaceFirst("param:", "");
				i++;
				String value = args[i].replaceFirst("value:", "");
				props.put(param, value);
			}
			else if (args[i].startsWith("archive:"))
				archives.add(args[i].replaceFirst("archive:", ""));
			else if (args[i].startsWith("classname:"))
				className = args[i].replaceFirst("classname:", "");
			else if (args[i].startsWith("name:"))
				name = args[i].replaceFirst("name:", "");
			else if (args[i].startsWith("codebase:"))
				codebase = args[i].replaceFirst("codebase:", "");
			else if (args[i].startsWith("isjar:"))
				isJar = Boolean.parseBoolean(args[i].replaceFirst("codebase:", ""));
			else if (args[i].startsWith("width:"))
				setSize(Integer.parseInt(args[i].replaceFirst("width:", "")), this.getSize().height);
			else if (args[i].startsWith("height:"))
				setSize(this.getSize().width, Integer.parseInt(args[i].replaceFirst("height:", "")));
			else if (args[i].startsWith("x:"))
				setLocation(Integer.parseInt(args[i].replaceFirst("x:", "")), this.getLocation().y);
			else if (args[i].startsWith("y:"))
				setLocation(this.getLocation().x, Integer.parseInt(args[i].replaceFirst("y:", "")));

		}
		appletContainer.setSize(this.getSize());
		origSize = appletContainer.getSize();

		Thread stdinReader = new Thread() {

			@Override
			public void run() {
				AppletWrapperFrame frame = AppletWrapperFrame.this;
				DataInputStream in = new DataInputStream(System.in);
				try
				{
					while (true)
					{
						int operation = in.readInt();
						switch(operation)
						{
							case RESX:
								appletContainer.setSize(in.readInt(), frame.getSize().height);
								break;
							case RESY:
								appletContainer.setSize(frame.getSize().width, in.readInt());
								break;
							case POSX:
								frame.setLocation(in.readInt(), frame.getLocation().y);
								break;
							case POSY:
								frame.setLocation(frame.getLocation().x, in.readInt());
								break;
							case SHOW:
								toFront();
								frame.setAlwaysOnTop(true);
								repaint();
								break;
							case HIDE:
								frame.setAlwaysOnTop(false);
								break;
							case SD:
								frame.applet.stop();
								frame.applet.destroy();
								frame.dispose();
								System.exit(0);
								break;
							case VPRECT:
							{
								int px = in.readInt();
								int py = in.readInt();
								int sx = in.readInt();
								int sy = in.readInt();
								calculateSize(px,py,sx,sy);
							}
								break;
							case FULLRECT:
							{
								int posx = in.readInt();
								int posy = in.readInt();
								int width = in.readInt();
								int height = in.readInt();
								int px = in.readInt();
								int py = in.readInt();
								int sx = in.readInt();
								int sy = in.readInt();
								calculateSize(px,py,sx,sy);
							}
								break;
							default:
								break;
						}
						frame.repaint();
						frame.validate();
					}
				}
				catch (IOException e)
				{
					frame.applet.stop();
					frame.applet.destroy();
					frame.dispose();
					System.exit(0);
				}
			}

		};
		stdinReader.start();

	}
	
	private void calculateSize(int px, int py, int sx, int sy) {
		setSize(origSize);
		appletContainer.setSize(origSize);
		appletContainer.setLocation(0,0);
		int xstart = Math.max(px,getLocationOnScreen().x);
		int ystart = Math.max(py,getLocationOnScreen().y);
		int xend = Math.min(sx,getLocationOnScreen().x+getWidth());
		int yend = Math.min(sy,getLocationOnScreen().y+getHeight());
		appletContainer.setLocation(getLocationOnScreen().x-xstart,getLocationOnScreen().y-ystart);
		setLocation(xstart,ystart);
		setSize(xend-xstart, yend-ystart);
		repaint();
		validate();
		appletContainer.repaint();
		appletContainer.revalidate();
	}

	public static void main(String args[]) {
		AppletWrapperFrame frame = new AppletWrapperFrame(args);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		try
		{
			frame.initApplet();
		}
		catch (MalformedURLException e)
		{
			System.exit(0);
		}
	}

	private void initApplet() throws MalformedURLException {
		URL[] arArr = new URL[archives.size()];
		for (int i = 0; i < archives.size(); i++)
		{
			System.out.println(archives.get(i));
			arArr[i] = new URL(archives.get(i));
		}
		applet = AppletManager.getApplet(name, arArr, className, props, codebase, context, isJar);
		appletContainer.add(applet);
		validate();

	}
}