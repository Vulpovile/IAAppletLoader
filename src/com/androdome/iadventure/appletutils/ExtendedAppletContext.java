package com.androdome.iadventure.appletutils;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;

public class ExtendedAppletContext implements AppletContext {

	HashMap<String, Applet> appletMap = new HashMap<String, Applet>();
	HashMap<String, InputStream> streamMap = new HashMap<String, InputStream>();

	public void addApplet(String name, Applet applet) {
		appletMap.put(name, applet);
	}

	@Override
	public Applet getApplet(String arg0) {
		return appletMap.get(arg0);
	}

	@Override
	public Enumeration<Applet> getApplets() {
		return Collections.enumeration(appletMap.values());
	}

	@Override
	public AudioClip getAudioClip(URL url) {
		return Applet.newAudioClip(url);
	}

	@Override
	public Image getImage(URL url) {
		try
		{
			return ImageIO.read(url);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public InputStream getStream(String streamName) {
		return streamMap.get(streamName);
	}

	@Override
	public Iterator<String> getStreamKeys() {
		return streamMap.keySet().iterator();
	}

	@Override
	public void setStream(String name, InputStream stream) throws IOException {
		streamMap.put(name, stream);
	}

	@Override
	public void showDocument(URL url) {

	}

	@Override
	public void showDocument(URL url, String name) {

	}

	@Override
	public void showStatus(String name) {

	}
	
	public void dispose()
	{
		Object[] arr = this.streamMap.values().toArray();
		for(int i = 0; i < arr.length; i++)
		{
			try
			{
				((InputStream)arr[i]).close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void putApplet(String name, Applet applet) {
		this.appletMap.put(name, applet);
	}

}
