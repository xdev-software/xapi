package xdev.ui;

/*-
 * #%L
 * XDEV Application Framework
 * %%
 * Copyright (C) 2003 - 2020 XDEV Software
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */


import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import xdev.Application;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


/**
 * The {@link XdevMainFrame} is a top-level Frame. The XdevMainFrame is always
 * called as first frame. Based on {@link XdevFrame}.
 * 
 * 
 * @see JFrame
 * @see XdevRootPaneContainer
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
public class XdevMainFrame extends XdevFrame implements XdevApplicationContainer
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log			= LoggerFactory.getLogger(XdevMainFrame.class);
	
	/**
	 * Creates a new, initially invisible XdevMainFrame.
	 * 
	 * @see XdevFrame
	 */
	public XdevMainFrame()
	{
		super("",(Image)null);
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				Application.exit(XdevMainFrame.this);
			}
		});
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
