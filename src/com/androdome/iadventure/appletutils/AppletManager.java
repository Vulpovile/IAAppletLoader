package com.androdome.iadventure.appletutils;

import java.applet.Applet;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class AppletManager {

	public static Applet createApplet(String className, ClassLoader classLoader) throws UnsupportedClassVersionError {
		try
		{
			Class<?> appletClass = classLoader.loadClass(className);

			return (Applet) appletClass.newInstance();
		}
		catch (Throwable ex)
		{
			ex.printStackTrace();
			return null;

		}

	}
	
	public static Applet getApplet(final String name, final URL[] archives, final String className, HashMap<String, String> params, final String codeBase, final ExtendedAppletContext context, final boolean isJar) {

		final Wrapplet wrapplet = new Wrapplet();
		wrapplet.setAppletContext(context);
		wrapplet.setMessage("Getting codebase");
		wrapplet.codebase = codeBase;
		wrapplet.setMessage("Getting parameters");
		wrapplet.setParams(params);
		wrapplet.setMessage("Waiting for permission");
		wrapplet.startThread();
		Thread th = new Thread() {
			public void run() {
				
				URL[] arJ = null;
				if(isJar)
					arJ = archives;
				AppletAcceptDialog dialog = new AppletAcceptDialog(name, arJ, className, codeBase);
				dialog.setVisible(true);
				while (dialog.dialogResult == 0)
					try
					{
						if (dialog.isSiteTrusted(codeBase))
						{
							dialog.dialogResult = AppletAcceptDialog.DIALOG_RUN;
							dialog.dispose();
						}
						Thread.sleep(100L);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				if (dialog.dialogResult == AppletAcceptDialog.DIALOG_CANCEL)
				{
					wrapplet.setCancel();
					return;
				}
				wrapplet.setMessage("Updating classloader");
				wrapplet.setProgress(20);
				URLClassLoader loader = new URLClassLoader(archives, null);

				wrapplet.setProgress(40);

				wrapplet.setMessage("Swapping context");
				Thread.currentThread().setContextClassLoader(loader);
				wrapplet.setProgress(50);
				try
				{

					wrapplet.setMessage("Fetching applet");
					wrapplet.setProgress(60);
					Applet applet = createApplet(className, loader);
					wrapplet.setMessage("Swapping applet");
					wrapplet.setProgress(70);
					context.putApplet(name, applet);
					wrapplet.setApplet(applet);
				}
				catch (UnsupportedClassVersionError er)
				{
					String errorString = er.getMessage();
					wrapplet.setCancel(errorString);

					er.printStackTrace();
				}
				
			}
		};
		th.start();

		return wrapplet;
	}
}
