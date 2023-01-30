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
package xdev.ui;


import java.awt.Component;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import xdev.Application;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


public class XdevFullScreen extends XdevScreen implements XdevApplicationContainer
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log			= LoggerFactory.getLogger(XdevFullScreen.class);
	
	public XdevFullScreen()
	{
		super(JOptionPane.getRootFrame(),false);
		
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				Application.exit(XdevFullScreen.this);
			}
		});
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setXdevWindow(XdevWindow window)
	{
		getGraphicsConfiguration().getDevice().setFullScreenWindow(null);
		setVisible(false);
		
		cleanup();
		
		this.window = window;
		window.setOwner(this);
		
		setTitle(window.getTitle());
		
		setContentPane(window);
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				getGraphicsConfiguration().getDevice().setFullScreenWindow(XdevFullScreen.this);
				setVisible(true);
			}
		});
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIconImage(Image img)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pack()
	{
		invalidate();
		validate();
		repaint();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocationRelativeTo(Component arg0)
	{
		// do nothing
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getCodeBase()
	{
		URL url = null;
		try
		{
			url = new File("./").getAbsoluteFile().toURI().toURL();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		
		return url;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getImage(URL url)
	{
		return getToolkit().getImage(url);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showDocument(URL url)
	{
		try
		{
			DesktopUtils.browse(url.toURI());
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showDocument(URL url, String target)
	{
		try
		{
			DesktopUtils.browse(url.toURI());
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
}
