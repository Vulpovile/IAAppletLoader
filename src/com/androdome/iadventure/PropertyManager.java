package com.androdome.iadventure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
	private static Properties props = new Properties();
	private static File propFile = new File("browser.properties");
	static
	{
		if(!propFile.exists())
			try
			{
				propFile.createNewFile();
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		try
		{
			props.load(new FileInputStream(propFile));
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getProperty(String key, String def)
	{
		String prop = props.getProperty(key, def);
		props.setProperty(key, prop);
		save();
		return prop;
	}
	public static void setProperty(String key, String prop)
	{
		props.setProperty(key, prop);
		save();
	}
	private static void save() {
		if(!propFile.exists())
			try
			{
				propFile.createNewFile();
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
		try
		{
			props.store(new FileOutputStream(propFile), "Properties");
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
