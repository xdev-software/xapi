/*
 * XDEV Application Framework - XDEV Application Framework
 * Copyright © 2003 XDEV Software (https://xdev.software)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package xdev;


import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Permission;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xdev.event.ApplicationExitEvent;
import xdev.event.ApplicationExitListener;
import xdev.io.IOUtils;
import xdev.ui.GraphicUtils;
import xdev.ui.XdevApplicationContainer;
import xdev.ui.XdevFocusTraversalPolicy;
import xdev.ui.XdevFullScreen;
import xdev.ui.XdevMainFrame;
import xdev.ui.XdevWindow;
import xdev.ui.laf.LookAndFeel;
import xdev.ui.laf.LookAndFeelException;
import xdev.ui.laf.LookAndFeelManager;
import xdev.ui.laf.SystemLookAndFeel;
import xdev.util.Settings;
import xdev.util.XdevDate;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.LoggingConfiguration;
import xdev.util.logging.XdevLogger;


/**
 * 
 * This is the entry point of the XDEV Application Framework.
 * <p>
 * Desktop and webstart applications are started via the {@link #main(String[])}
 * method, applets per default via {@link #init()} and {@link #start()}.
 * <p>
 * The user application on top of the framework is started afterwards. This is
 * because runtime environment specific init routines of the framwork are
 * performed in this class.
 * <p>
 * The system or program parameter 'main' is used to define the user
 * application's main class. Ensure that this main class is in a package, not in
 * the default package.
 * <hr>
 * <p>
 * Desktop application:<br>
 * <br>
 * <code>java [vm args] -Dmain=myapp.Start xdev.Application [application args]</code>
 * <hr>
 * <p>
 * Applet:<br>
 * <br>
 * <code>
 * &lt;applet code="xdev.Application.class"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param name="main" value="myapp.Start" /&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param...<br>
 * &lt;/applet&gt;
 * </code>
 * <hr>
 * <p>
 * Webstart:<br>
 * <br>
 * <code>
 * &lt;jnlp...<br>
 * ...<br>
 * &lt;resources&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="application.type" value="webstart" /&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="main" value="start.Main" /&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property...<br>
 * &lt;/resources&gt;<br>
 * ...<br>
 * &lt;application-desc main-class="xdev.Application"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;argument...<br>
 * &lt;/application-desc&gt;<br>
 * ...<br>
 * &lt;/jnlp&gt;
 * </code>
 * <hr>
 * 
 * @see #main(String[])
 * @see #initApplication(String[])
 * @see #startApplication()
 * @see #isApplet()
 * @see #isApplication()
 * @see #isWebstart()
 * 
 * @author XDEV Software
 * @since 3.0
 */

public final class Application extends JApplet implements AppletStub, XdevApplicationContainer
{
	/**
	 * @since 4.0
	 */
	public static enum State
	{
		INITIALIZING,
		
		RUNNING,
		
		REQUESTING_EXIT,
		
		EXITING
	}
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log			= LoggerFactory.getLogger(Application.class);
	
	/**
	 * Application type constant.
	 */
	public final static int			APPLICATION	= 0;
	
	/**
	 * Application type constant.
	 */
	public final static int			APPLET		= 1;
	
	/**
	 * Application type constant.
	 */
	public final static int			WEBSTART	= 2;
	
	private static int				type		= APPLICATION;
	
	
	/**
	 * @return The application's type. Either {@link #APPLICATION},
	 *         {@link #APPLET} or {@link #WEBSTART}
	 */
	
	public static int getType()
	{
		return type;
	}
	
	
	/**
	 * @return <code>true</code> if this is a desktop application started via
	 *         {@link #main(String[])}, <code>false</code> otherwise
	 */
	
	public static boolean isApplication()
	{
		return type == APPLICATION;
	}
	
	
	/**
	 * @return <code>true</code> if this is a applet running in a browser or
	 *         another applet container, <code>false</code> otherwise
	 */
	
	public static boolean isApplet()
	{
		return type == APPLET;
	}
	
	
	/**
	 * @return <code>true</code> if this is a webstart application started via a
	 *         webstart descriptor, <code>false</code> otherwise
	 */
	
	public static boolean isWebstart()
	{
		return type == WEBSTART;
	}
	
	private static String[]					args;
	private static EventListenerList		listenerList	= new EventListenerList();
	private static XdevApplicationContainer	container		= null;
	private static Image					appIcon			= null;
	private static boolean					fullscreen		= false;
	private static long						serverTimeDiff	= 0;
	private static boolean					initialized		= false;
	private static Map<String, Extension>	extensions		= new HashMap();
	// @since 4.0
	private static State					state			= State.INITIALIZING;
	
	
	// private static XdevJobQueue jobQueue = new XdevJobQueue();
	
	/**
	 * Returns the extension named <code>name</code> or <code>null</code> if not
	 * found.
	 * <p>
	 * Note: The extensions are only available after {@link #init()} has been
	 * called.
	 * 
	 * @param name
	 *            the extension's name
	 * @return the appropriate extension or <code>null</code>
	 */
	public static Extension getExtension(String name)
	{
		return extensions.get(name);
	}
	
	
	/**
	 * Returns all available extensions.
	 * <p>
	 * Note: The extensions are only available after {@link #init()} has been
	 * called.
	 * 
	 * @return All available extensions.
	 */
	public static Extension[] getExtensions()
	{
		return extensions.values().toArray(new Extension[extensions.size()]);
	}
	
	
	/**
	 * This is the entry point of the XDEV Application Framwork for desktop and
	 * webstart appications.
	 * <p>
	 * It initializes the framework and starts the application based on the
	 * framework.
	 * <p>
	 * First {@link #initApplication(String[])} is called, then
	 * {@link #startApplication()}.
	 * 
	 * <p>
	 * <strong>Hint:</strong> Can be used to start {@link XdevWindow}s.
	 * Therefore set the VM Argument
	 * <code>-Dmain=FULL_QUALIFIED_CLASSNAME</code>. Where
	 * <code>FULL_QUALIFIED_CLASSNAME</code> is the full qualified classname
	 * like <i>xdev.ui.MyClass</i>.
	 * </p>
	 * 
	 * @param args
	 *            The application's arguments
	 */
	
	public static void main(String[] args)
	{
		LoggingConfiguration.readConfiguration();
		
		checkManifests();
		
		initApplication(args);
		startApplication();
	}
	
	
	/**
	 * Reads non-standard attributes from the jar-manifests.
	 */
	private static void checkManifests()
	{
		try
		{
			ClassLoader loader = Application.class.getClassLoader();
			for(Enumeration<URL> e = loader.getResources("META-INF/MANIFEST.MF"); e
					.hasMoreElements();)
			{
				URL url = e.nextElement();
				InputStream in = url.openStream();
				try
				{
					Manifest manifest = new Manifest(in);
					Attributes atts = manifest.getMainAttributes();
					String systemProperties = atts.getValue("System-Properties");
					if(systemProperties != null)
					{
						StringTokenizer pairs = new StringTokenizer(systemProperties," ");
						while(pairs.hasMoreTokens())
						{
							String pair = pairs.nextToken();
							StringTokenizer st = new StringTokenizer(pair,"=");
							String key = st.nextToken().trim();
							String value = st.hasMoreTokens() ? st.nextToken().trim() : "true";
							System.setProperty(key,value);
						}
					}
				}
				finally
				{
					in.close();
				}
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	/**
	 * Initializes the XDEV Application Framework.
	 * <ul>
	 * <li>Determines whether this is a desktop or a webstart application</li>
	 * <li>Sets the look and feel to the system's default</li>
	 * <li>Evaluates the application's arguments <code>args</code></li>
	 * <li>Initializes the applications top level container: a frame, a
	 * fullscreen window if the system property <code>fullscreen</code> is
	 * <code>true</code> or an applet</li>
	 * </ul>
	 * <p>
	 * This method is called by {@link #main(String[])}.
	 * <p>
	 * If you want to start your application without using
	 * {@link #main(String[])} of this class, call
	 * {@link #initApplication(String[])} at the beginning of your application
	 * to initialize the XDEV Application Framwork properly.
	 * 
	 * @param args
	 *            The application's arguments
	 */
	
	public static void initApplication(String[] args)
	{
		initApplicationImpl(args,"webstart".equalsIgnoreCase(getSystemPropertyOrArgument(
				"application.type","")) ? WEBSTART : APPLICATION);
	}
	
	
	private static void initApplicationImpl(String[] args, int type)
	{
		Application.type = type;
		
		// see
		// https://xdevcollaboration.com/pages/viewpage.action?pageId=13730460
		if(IOUtils.isMac())
		{
			System.setProperty("swing.volatileImageBufferEnabled","false");
		}
		
		try
		{
			Application.args = args != null ? args : new String[0];
			
			String icon = System.getProperty("icon");
			if(icon != null && icon.length() > 0)
			{
				try
				{
					appIcon = GraphicUtils.loadImagePlain(icon);
				}
				catch(IOException e)
				{
				}
			}
			
			loadExtensions();
			
			initLookAndFeel();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			initialized = true;
			state = State.RUNNING;
		}
		
		if(container == null)
		{
			fullscreen = Settings.checkUserSetting(System.getProperty("fullscreen"));
		}
	}
	
	
	private static void loadExtensions() throws IOException
	{
		ClassLoader loader = Application.class.getClassLoader();
		for(Enumeration<URL> urls = loader.getResources("xdev/Extensions"); urls.hasMoreElements();)
		{
			URL url = urls.nextElement();
			
			try
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(url.openStream());
				doc.getDocumentElement().normalize();
				NodeList children = doc.getElementsByTagName("extension");
				for(int i = 0, c = children.getLength(); i < c; i++)
				{
					Element child = (Element)children.item(i);
					String extensionClassName = child.getAttribute("class");
					Extension extension = (Extension)Class.forName(extensionClassName)
							.newInstance();
					Map<String, String> params = new HashMap();
					NodeList paramElements = child.getElementsByTagName("param");
					for(int p = 0, pc = paramElements.getLength(); p < pc; p++)
					{
						Element paramElement = (Element)paramElements.item(p);
						params.put(paramElement.getAttribute("name"),
								paramElement.getAttribute("value"));
					}
					try
					{
						extension.init(params);
					}
					catch(ExtensionInitializationException eie)
					{
						log.error(eie);
					}
				}
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}
	}
	
	
	private static void initLookAndFeel() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, LookAndFeelException
	{
		LookAndFeel laf = null;
		String lafName = getSystemPropertyOrArgument("laf",null);
		if(lafName != null && lafName.length() > 0)
		{
			laf = (LookAndFeel)Class.forName(lafName).newInstance();
		}
		if(laf == null)
		{
			laf = new SystemLookAndFeel();
		}
		LookAndFeelManager.setLookAndFeel(laf);
	}
	
	
	private static String getSystemPropertyOrArgument(String name, String defaultValue)
	{
		String value = System.getProperty(name);
		if(value != null)
		{
			return value;
		}
		
		if(args != null && args.length > 0)
		{
			for(int i = 0; i < args.length; i++)
			{
				String arg = args[i];
				if(arg.startsWith("-" + name) || arg.startsWith("--" + name))
				{
					int eq = arg.indexOf('=');
					if(eq >= 0)
					{
						value = arg.substring(eq + 1).trim();
					}
					else if(i < args.length - 1)
					{
						value = args[i + 1].trim();
					}
					
					return value;
				}
			}
		}
		
		return defaultValue;
	}
	
	
	/**
	 * Starts the application after the XDEV application framwork has been
	 * initialized.
	 * <p>
	 * This method is called by {@link #main(String[])} after
	 * {@link #initApplication(String[])}.
	 * <p>
	 * The system property main is used to determine the main class.
	 * <ul>
	 * <li>If the class has a valid main method, it is called with the arguments
	 * handed over in {@link #initApplication(String[])}.</li>
	 * <li>If the class is an instance of {@link XdevWindow} it is initialized
	 * via the default constructor and shown in the application's top level
	 * container.</li>
	 * <li>If the class is an instance of {@link JComponent} it is initialized
	 * via the default constructor, placed in a {@link XdevWindow} and shown in
	 * the application's top level container.</li>
	 * </ul>
	 * 
	 * @throws IllegalStateException
	 *             if the framework has not been initialized via
	 *             {@link #initApplication(String[])}
	 * 
	 * @see #main(String[])
	 * @see #initApplication(String[])
	 */
	
	public static void startApplication() throws IllegalStateException
	{
		startApplicationImpl(getSystemPropertyOrArgument("main",null));
	}
	
	
	private static void startApplicationImpl(String main) throws IllegalStateException
	{
		if(!initialized)
		{
			throw new IllegalStateException("XDEV Application Framework has not been initialized "
					+ "(Application.initApplication(String[]");
		}
		
		if(main == null || main.length() == 0)
		{
			throw new IllegalArgumentException("No main class specified");
		}
		
		try
		{
			Class mainClass = Class.forName(main);
			try
			{
				mainClass.getMethod("main",String[].class).invoke(null,(Object)args);
			}
			catch(NoSuchMethodException nsme)
			{
				try
				{
					mainClass.getDeclaredConstructor(new Class[0]);
				}
				catch(NoSuchMethodException e)
				{
					throw new IllegalArgumentException(main + " has no default constructor");
				}
				
				XdevApplicationContainer container = getContainer();
				if(XdevWindow.class.isAssignableFrom(mainClass))
				{
					XdevWindow window = (XdevWindow)mainClass.newInstance();
					container.setXdevWindow(window);
					container.pack();
					container.setLocationRelativeTo(null);
					container.setVisible(true);
				}
				else if(JComponent.class.isAssignableFrom(mainClass))
				{
					JComponent cpn = (JComponent)mainClass.newInstance();
					XdevWindow window = new XdevWindow();
					window.setLayout(new BorderLayout());
					window.add(cpn,BorderLayout.CENTER);
					container.setXdevWindow(window);
					container.pack();
					container.setLocationRelativeTo(null);
					container.setVisible(true);
				}
				else
				{
					throw new IllegalArgumentException(main + " has no main method nor is a bean");
				}
			}
		}
		catch(Exception e)
		{
			Throwable t = e;
			if(e instanceof InvocationTargetException)
			{
				Throwable cause = e.getCause();
				if(cause != null)
				{
					t = cause;
				}
			}
			
			log.error(t);
		}
	}
	
	
	/**
	 * Return the arguments handed over to the application in
	 * {@link #main(String[])}.
	 * 
	 * @return The application's arguments
	 * @throws IllegalStateException
	 *             if this {@link Application} is an {@link Applet}
	 * @see #getType()
	 */
	
	public static String[] getArguments() throws IllegalStateException
	{
		if(type == APPLET)
		{
			throw new IllegalStateException("application is an applet");
		}
		
		return args;
	}
	
	
	/**
	 * Returns the value of the named parameter in the HTML tag. For example, if
	 * this applet is specified as
	 * 
	 * <pre>
	 * <applet code="Clock" width=50 height=50>
	 * <param name="Color" value="blue"/>
	 * 
	 * </pre>
	 * 
	 * then a call to getParameter("Color") returns the value "blue".
	 * <p>
	 * The name argument is case insensitive.
	 * </p>
	 * 
	 * @param name
	 *            a parameter name.
	 * @return the value of the named parameter, or null if not set.
	 * @throws IllegalStateException
	 *             if this {@link Application} is not an {@link Applet}
	 * @see #getType()
	 */
	public static String getParam(String name) throws IllegalStateException
	{
		if(type == APPLET && container instanceof Applet)
		{
			return ((Applet)container).getParameter(name);
		}
		
		throw new IllegalStateException("not an applet");
	}
	
	
	public static Image getIconImage()
	{
		return appIcon;
	}
	
	
	/**
	 * Returns the applications top level container.
	 * <p>
	 * It is either a frame, a fullscreen window or an applet.
	 * 
	 * @return The application's top level container
	 */
	
	public static XdevApplicationContainer getContainer()
	{
		if(container == null)
		{
			if(fullscreen)
			{
				container = new XdevFullScreen();
			}
			else
			{
				container = new XdevMainFrame();
			}
		}
		
		return container;
	}
	
	
	/**
	 * @return the application's current state
	 * 
	 * @since 4.0
	 */
	public static State getState()
	{
		return state;
	}
	
	
	// /**
	// * @return the job queue of this application
	// *
	// * @since 4.0
	// */
	// public static JobQueue getJobQueue()
	// {
	// return jobQueue;
	// }
	//
	//
	// /**
	// * Adds the <code>job</code> to the job queue of this application.
	// *
	// * @param job
	// * the job to enqueue
	// *
	// * @since 4.0
	// */
	// public static void enqueue(Job job)
	// {
	// jobQueue.enqueue(job);
	// }
	
	/**
	 * Registers an {@link ApplicationExitListener} to obsorve the exit of this
	 * {@link Application}.
	 * 
	 * @param l
	 *            the listener to add
	 */
	public static void addExitListener(ApplicationExitListener l)
	{
		listenerList.add(ApplicationExitListener.class,l);
	}
	
	
	/**
	 * Removes the {@link ApplicationExitListener} <code>l</code> from this
	 * {@link Application}.
	 * 
	 * @param l
	 *            the listener to remove
	 */
	public static void removeExitListener(ApplicationExitListener l)
	{
		listenerList.remove(ApplicationExitListener.class,l);
	}
	
	
	/**
	 * Exits the current running XDEV Application Framwork and Java Virtual
	 * Machine.
	 * <p>
	 * If a registered {@link ApplicationExitListener} vetos the process, the
	 * exit is terminated and the XDEV Application Framework remains running.
	 * 
	 * @param source
	 *            The initiator of the exit process
	 * @see ApplicationExitListener
	 * @see ApplicationExitEvent#vetoExit()
	 */
	public static void exit(final Object source)
	{
		ApplicationExitListener[] listeners = listenerList
				.getListeners(ApplicationExitListener.class);
		if(listeners != null && listeners.length > 0)
		{
			ApplicationExitEvent event = new ApplicationExitEvent(source);
			
			for(ApplicationExitListener l : listeners)
			{
				l.applicationWillExit(event);
			}
			
			if(event.hasVeto())
			{
				return;
			}
			
			for(ApplicationExitListener l : listeners)
			{
				l.applicationExits(event);
			}
		}
		
		System.exit(0);
	}
	
	/**
	 * Returns the time of the hosting server of this applet / webstart
	 * application.
	 * 
	 * @return The server time of this application.
	 */
	public static XdevDate getServerTime()
	{
		return new XdevDate(System.currentTimeMillis() + serverTimeDiff);
	}
	
	private static Runnable	gc;
	static
	{
		gc = new Runnable()
		{
			public void run()
			{
				System.gc();
			}
		};
	}
	
	
	/**
	 * Runs the garbage collector.
	 * <p>
	 * Calling the gc method suggests that the Java Virtual Machine expend
	 * effort toward recycling unused objects in order to make the memory they
	 * currently occupy available for quick reuse. When control returns from the
	 * method call, the Java Virtual Machine has made a best effort to reclaim
	 * space from all discarded objects.
	 * </p>
	 */
	public static void gc()
	{
		SwingUtilities.invokeLater(gc);
	}
	
	
	/**
	 * Returns the default {@link GraphicsConfiguration} associated with the
	 * {@link GraphicsDevice} this application is showing on.
	 * 
	 * @return the default {@link GraphicsConfiguration} of this
	 *         {@link GraphicsDevice}.
	 */
	public static GraphicsConfiguration getLocalGraphicsConfiguration()
	{
		if(container != null)
		{
			return container.getGraphicsConfiguration();
		}
		
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
	}
	
	
	/*
	 * ---------------------------- Applet---------------------------
	 */
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init()
	{
		try
		{
			System.setSecurityManager(new SecurityManager()
			{
				@Override
				public void checkPermission(Permission perm)
				{
				}
				
				
				@Override
				public void checkPermission(Permission perm, Object context)
				{
				}
			});
		}
		catch(Throwable t)
		{
		}
		
		getContentPane().setLayout(new BorderLayout());
		
		container = this;
		
		try
		{
			serverTimeDiff = Long.parseLong(getParameter("serverTimeMillis")) * 1000
					- System.currentTimeMillis();
		}
		catch(Exception e)
		{
			serverTimeDiff = 0;
		}
		
		try
		{
			fullscreen = "true".equals(getParameter("fullscreen"));
		}
		catch(Exception e)
		{
			fullscreen = false;
		}
		
		try
		{
			String value = getParameter("suffix");
			if(value != null && value.length() > 0)
			{
				System.setProperty(Settings.SERVER_SIDE_SUFFIX,value);
			}
			
		}
		catch(Exception e)
		{
			log.error(e);
		}
		
		setFocusTraversalPolicy(new XdevFocusTraversalPolicy(this));
		
		try
		{
			String vmArgs = getParameter("java_args");
			if(vmArgs != null && vmArgs.length() > 0)
			{
				StringTokenizer st = new StringTokenizer(vmArgs," ");
				while(st.hasMoreTokens())
				{
					String token = st.nextToken().trim();
					if(token.startsWith("-D"))
					{
						int index = token.indexOf('=');
						if(index == -1)
						{
							System.setProperty(token.substring(2),"true");
						}
						else
						{
							System.setProperty(token.substring(2,index),token.substring(index + 1));
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		
		initApplicationImpl(null,APPLET);
		
		System.out.println("XDEV Application Framework " + API.VERSION.toScreen());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start()
	{
		startApplicationImpl(getParameter("main"));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy()
	{
		System.gc();
		exit(this);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Window getWindow()
	{
		return JOptionPane.getRootFrame();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getImage(URL url)
	{
		return getAppletContext().getImage(url);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showDocument(URL url)
	{
		getAppletContext().showDocument(url);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showDocument(URL url, String target)
	{
		getAppletContext().showDocument(url,target);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void appletResize(int width, int height)
	{
		resize(width,height);
	}
	
	private XdevWindow	window;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setXdevWindow(XdevWindow window)
	{
		cleanup();
		
		this.window = window;
		
		window.setOwner(this);
		
		String width;
		String height;
		if(fullscreen)
		{
			width = height = "\"100%\"";
		}
		else
		{
			Dimension ps = window.getPreferredSize();
			width = "\"" + ps.width + "px\"";
			height = "\"" + ps.height + "px\"";
		}
		
		setJMenuBar(window.getJMenuBar());
		getRootPane().setDefaultButton(window.getDefaultButton());
		getContentPane().add(Util.createContainer(this,window));
		
		jsc("resize","width",width,"height",height);
		
		validate();
		repaint();
		
		window.fireWindowOpened(new WindowEvent(SwingUtilities.getWindowAncestor(this),0));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevWindow getXdevWindow()
	{
		return window;
	}
	
	
	private void cleanup()
	{
		try
		{
			getRootPane().setJMenuBar(null);
			getContentPane().removeAll();
			
			if(window != null)
			{
				window.setOwner(null);
				window = null;
			}
		}
		catch(Exception e)
		{
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTitle(String title)
	{
		jsc("setTitle","title",jsc_escape(title));
	}
	
	
	private void jsc(String action, String... params)
	{
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append("jsc.");
			sb.append(Settings.getServerSideSuffix());
			sb.append("?action=");
			sb.append(action);
			for(int i = 0; i < params.length; i += 2)
			{
				sb.append("&");
				sb.append(params[i]);
				sb.append("=");
				sb.append(URLEncoder.encode(params[i + 1],"UTF8"));
			}
			
			showDocument(new URL(Application.getContainer().getCodeBase(),sb.toString()),"jsc");
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	private String jsc_escape(String str)
	{
		int len = str.length();
		StringBuilder sb = new StringBuilder(len + 2);
		sb.append("'");
		for(int i = 0; i < len; i++)
		{
			char ch = str.charAt(i);
			if(ch == '\'')
			{
				sb.append('\\');
			}
			sb.append(ch);
		}
		sb.append("'");
		return sb.toString();
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public void setIconImage(Image img)
	{
		// no op
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public void pack()
	{
		// no op
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public void setLocationRelativeTo(Component c)
	{
		// no op
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public void setResizable(boolean b)
	{
		// no op
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public void setExtendedState(int extendedState)
	{
		// no op
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public int getExtendedState()
	{
		return NORMAL;
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public void toFront()
	{
		// no op
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public void toBack()
	{
		// no op
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public void addWindowListener(WindowListener listener)
	{
		// no op
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public void removeWindowListener(WindowListener listener)
	{
		// no op
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close()
	{
		exit(this);
	}
	
	
	/**
	 * Returns the {@link XdevLogger} of this Application which is used to log
	 * messages for the XDEV Application Framework.
	 * 
	 * 
	 * <p>
	 * <strong>Hint:</strong> Consider using local loggers instead of using one
	 * application global logger. With local loggers you benefit from many
	 * advantages i.e. name based filtering and message routing.
	 * 
	 * </p>
	 * 
	 * @return the {@link XdevLogger} of this Application.
	 * 
	 * 
	 */
	public static XdevLogger getLogger()
	{
		return log;
	}
	
}
