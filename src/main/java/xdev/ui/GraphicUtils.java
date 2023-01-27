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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import xdev.Application;
import xdev.io.IOUtils;
import xdev.lang.LibraryMember;
import xdev.lang.NotNull;
import xdev.lang.Nullable;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


/**
 * <p>
 * The <code>GraphicUtils</code> class provides utility methods for graphics
 * handling.
 * </p>
 * 
 * @since 2.0
 * 
 * @author XDEV Software
 */
@LibraryMember
public final class GraphicUtils
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log	= LoggerFactory.getLogger(GraphicUtils.class);
	

	/**
	 * <p>
	 * <code>GraphicUtils</code> instances can not be instantiated. The class
	 * should be used as utility class:
	 * <code>GraphicUtils.loadImagePlain("myImg.png");</code>.
	 * </p>
	 */
	private GraphicUtils()
	{
	}
	

	/**
	 * Determines if quality rendering should be used.
	 * <p>
	 * <strong>Hint:</strong> Set system property <code>xdev.renderMode</code>
	 * to "<code>speed</code>" to tell the XDEV API to use speed rendering.
	 * </p>
	 * 
	 * @return <code>true</code> if quality rendering should be used,
	 *         <code>false</code> otherwise
	 */
	private static boolean useQualityRendering()
	{
		return System.getProperty("xdev.renderMode","quality").equals("quality");
	}
	

	/**
	 * TODO ...
	 * 
	 * @param g
	 *            x
	 * @return x
	 */
	public static Graphics2D create2D(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		if(useQualityRendering())
		{
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		}
		else
		{
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
			g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_SPEED);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		}
		
		return g2;
	}
	

	/**
	 * Returns the <code>int</code> value of the image scale mode that should be
	 * used.
	 * 
	 * @return the <code>int</code> value of the image scale mode that should be
	 *         used.
	 */
	public static int getImageScaleMode()
	{
		return useQualityRendering() ? Image.SCALE_SMOOTH : Image.SCALE_FAST;
	}
	

	/**
	 * Returns the <code>int</code> value of the {@link XdevInternalFrame} drag
	 * mode that should be used.
	 * 
	 * @return the <code>int</code> value of the {@link XdevInternalFrame} drag
	 *         mode that should be used.
	 */
	public static int getInternalFrameDragMode()
	{
		return useQualityRendering() ? JDesktopPane.LIVE_DRAG_MODE : JDesktopPane.OUTLINE_DRAG_MODE;
	}
	

	/**
	 * Alias for {@link #loadImagePlain(String, Component)}.
	 * 
	 * 
	 * 
	 * @param path
	 *            the relative path of the image, e.g. 'res/image/splash.png'
	 * 
	 * @return the image loaded from the given <code>path</code>.
	 * 
	 * @throws IOException
	 *             if an IO-error occurs
	 * 
	 * @throws FileNotFoundException
	 *             if the image can't be found
	 */
	public static Image loadImagePlain(String path) throws IOException, FileNotFoundException
	{
		return loadImagePlain(path,JOptionPane.getRootFrame());
	}
	
	/**
	 * JavaDoc omitted for private field.
	 */
	private static Hashtable<String, Image>	imageCache	= new Hashtable<String, Image>();
	

	/**
	 * Returns the image loaded from the given <code>path</code>.
	 * 
	 * <p>
	 * Searches for the image in the application's classpath and the filesystem.
	 * </p>
	 * 
	 * <p>
	 * If the image is found the image is returned .<br>
	 * If the image can't be found an {@link FileNotFoundException} is thrown.
	 * 
	 * @param path
	 *            the relative path of the image, e.g. 'res/image/splash.png'
	 * @param c
	 *            component to load the image for
	 * 
	 * @return the image loaded from the given <code>path</code>.
	 * 
	 * @throws IOException
	 *             if the image can't be found or any IO-error occurs
	 * 
	 */
	public static Image loadImagePlain(String path, Component c) throws IOException
	{
		Image image = imageCache.get(path);
		if(image != null)
		{
			return image;
		}
		
		Exception cause = null;
		
		try
		{
			InputStream in = null;
			try
			{
				in = IOUtils.findResource(path);
			}
			catch(FileNotFoundException e)
			{
			}
			
			if(in != null)
			{
				try
				{
					byte[] b = IOUtils.readData(in);
					image = c.getToolkit().createImage(b);
					waitFor(image);
				}
				finally
				{
					IOUtils.closeSilent(in);
				}
			}
			else if(Application.isApplet())
			{
				String name = getFileName(path);
				URL url = createImageURL(name);
				image = loadImage(url,c);
			}
		}
		catch(Exception e)
		{
			cause = e;
			
			try
			{
				File f = new File(path);
				if(f.exists())
				{
					image = c.getToolkit().createImage(f.getAbsolutePath());
					waitFor(image);
				}
			}
			catch(Exception e2)
			{
				cause = e2;
			}
		}
		
		String projectHome = null;
		if(image == null
				&& c instanceof JComponent
				&& (projectHome = (String)((JComponent)c).getClientProperty("project.home")) != null)
		{
			try
			{
				File f = new File(new File(projectHome),path);
				if(f.exists())
				{
					image = c.getToolkit().createImage(f.getAbsolutePath());
					waitFor(image);
				}
			}
			catch(Exception e)
			{
				cause = e;
			}
		}
		
		if(image == null)
		{
			throw new IOException("Image not found: " + path,cause);
		}
		
		imageCache.put(path,image);
		
		return image;
	}
	

	/**
	 * Alias for {@link #loadImage(String, Component)}.
	 * 
	 * @param path
	 *            the relative path of the image, e.g. 'res/image/splash.png'
	 * @return a {@link BufferedImage} loaded from the given <code>path</code>.
	 * @throws IOException
	 *             if an IO-error occurs
	 */
	public static BufferedImage loadImage(String path) throws IOException
	{
		return loadImage(path,JOptionPane.getRootFrame());
	}
	

	/**
	 * Returns a {@link BufferedImage} loaded from the given <code>path</code>.
	 * 
	 * <p>
	 * Searches for the image in the application's classpath and the filesystem.
	 * </p>
	 * 
	 * <p>
	 * If the image is found the image is returned .<br>
	 * 
	 * 
	 * @param path
	 *            the relative path of the image, e.g. 'res/image/splash.png'
	 * @param c
	 *            component to load the image for
	 * 
	 * @return a {@link BufferedImage} loaded from the given <code>path</code>.
	 * 
	 * @throws IOException
	 *             if an IO-error occurs
	 */
	public static BufferedImage loadImage(String path, Component c) throws IOException
	{
		try
		{
			return createBufferedImage(loadImagePlain(path,c));
		}
		catch(Exception e)
		{
			throw new IOException("Image not found: " + path,e);
		}
	}
	

	/**
	 * 
	 * Returns a {@link Icon} loaded from the given <code>path</code>.
	 * 
	 * <p>
	 * Searches for the image in the application's classpath and the filesystem.
	 * </p>
	 * 
	 * <p>
	 * If the icon is found the icon is returned .<br>
	 * 
	 * 
	 * @param path
	 *            the relative path of the icon, e.g. 'res/image/splash.png'
	 * 
	 * @return a {@link Icon} loaded from the given <code>path</code>.
	 * @throws IOException
	 *             if an IO-error occurs
	 */
	public static Icon loadIcon(String path) throws IOException
	{
		Image image = loadImage(path);
		if(image == null)
		{
			return null;
		}
		
		return new ImageIcon(image);
	}
	

	/**
	 * 
	 * 
	 * @param path
	 *            x
	 * @return x
	 * @deprecated Use {@link IOUtils#getFileName(String)}
	 */
	@Deprecated
	public static String getFileName(String path)
	{
		int i = path.lastIndexOf('/');
		if(i < 0)
		{
			i = path.lastIndexOf('\\');
		}
		
		if(i >= 0)
		{
			return path.substring(i + 1);
		}
		
		return path;
	}
	

	/**
	 * Alias for <code>GraphicUtils.createBufferedImage(data, true)</code>.
	 * 
	 * @param data
	 *            {@link Image} to create a {@link BufferedImage} from.
	 * 
	 * @return a {@link BufferedImage} from the given {@link Image}
	 *         <code>data</code>.
	 * 
	 * @see #createBufferedImage(Image, boolean)
	 */
	public static BufferedImage createBufferedImage(@Nullable Image data)
	{
		return createBufferedImage(data,true);
	}
	

	/**
	 * Creates a {@link BufferedImage} from the given {@link Image}
	 * <code>data</code>.
	 * 
	 * @param data
	 *            {@link Image} to create a {@link BufferedImage} from.
	 * 
	 * @param translucent
	 *            if <code>true</code> the {@link BufferedImage} will be
	 *            translucent; otherwise not
	 * 
	 * @return a {@link BufferedImage} from the given {@link Image}
	 *         <code>data</code>.
	 * 
	 * @see Transparency
	 */
	public static BufferedImage createBufferedImage(@Nullable Image data, boolean translucent)
	{
		BufferedImage image = null;
		
		if(data != null)
		{
			if(data instanceof BufferedImage)
			{
				image = (BufferedImage)data;
			}
			else
			{
				int w = data.getWidth(null), h = data.getHeight(null);
				if(w > 0 && h > 0)
				{
					if(translucent)
					{
						image = Application.getLocalGraphicsConfiguration().createCompatibleImage(
								w,h,Transparency.TRANSLUCENT);
					}
					else
					{
						image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
					}
					Graphics g = image.createGraphics();
					g.drawImage(data,0,0,null);
					g.dispose();
				}
			}
		}
		
		return image;
	}
	

	/**
	 * Creates a {@link Image} of the given Icon <code>i</code>.
	 * 
	 * @param i
	 *            {@link Icon} to create a {@link Image} from.
	 * @return a {@link Image} of the given Icon <code>i</code>.
	 */
	public static Image createImageFromIcon(Icon i)
	{
		if(i instanceof ImageIcon)
		{
			return ((ImageIcon)i).getImage();
		}
		
		BufferedImage bi = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration()
				.createCompatibleImage(i.getIconWidth(),i.getIconHeight(),Transparency.TRANSLUCENT);
		Graphics g = bi.createGraphics();
		i.paintIcon(null,g,0,0);
		g.dispose();
		return bi;
	}
	

	/**
	 * TODO Works only with applets?
	 * 
	 * @param path
	 *            x
	 * @return x
	 * @throws MalformedURLException
	 *             x
	 */
	public static URL createImageURL(String path) throws MalformedURLException
	{
		if(path.startsWith("http://"))
		{
			return new URL(path);
		}
		
		String url = Application.getContainer().getCodeBase().toExternalForm();
		if(!url.endsWith("/"))
		{
			url = url.concat("/");
		}
		
		if(path.startsWith("/"))
		{
			url = url.concat(path.substring(1));
		}
		else
		{
			url = url.concat(path);
		}
		
		return new URL(url);
	}
	

	/**
	 * Loads a {@link Image} from a given {@link URL}.
	 * 
	 * @param url
	 *            {@link URL} to load the {@link Image} from.
	 * @return a {@link Image} from a given {@link URL}.
	 */
	public static Image loadImage(URL url)
	{
		return loadImage(url,JOptionPane.getRootFrame());
	}
	

	/**
	 * Loads a {@link Image} from a given {@link URL}.
	 * 
	 * @param url
	 *            {@link URL} to load the {@link Image} from.
	 * @param c
	 *            TODO ?
	 * @return a {@link Image} from a given {@link URL}.
	 */
	public static Image loadImage(URL url, Component c)
	{
		String path = url.toExternalForm();
		if(imageCache.containsKey(path))
		{
			return imageCache.get(path);
		}
		
		Image image = null;
		
		image = Application.getContainer().getImage(url);
		
		if(image != null)
		{
			waitFor(image);
			imageCache.put(path,image);
		}
		
		return image;
	}
	

	/**
	 * Flushes (frees) the provided {@link Image} from the image cache. The
	 * {@link Image} it self is also flushed via {@link Image#flush()}.
	 * 
	 * @param image
	 *            {@link Image} to flush.
	 */
	public static void flushImage(Image image)
	{
		if(imageCache.contains(image))
		{
			Enumeration<String> e = imageCache.keys();
			while(e.hasMoreElements())
			{
				Object key = e.nextElement();
				if(imageCache.get(key).equals(image))
				{
					imageCache.remove(key);
					break;
				}
			}
		}
		
		image.flush();
	}
	

	/**
	 * TODO is the path still correct?
	 * 
	 * @param name
	 *            x
	 * @param c
	 *            x
	 * @return x
	 * @deprecated use {@link #loadResIcon(String)} instead
	 */
	// @Deprecated
	// public static ImageIcon loadResIcon(String name, Component c)
	// {
	// URL url = GraphicUtils.class.getResource("/xdev/ui/icons/" + name);
	// Image image = c.getToolkit().createImage(url);
	// return new ImageIcon(image);
	// }
	
	/**
	 * 
	 */
	public static ImageIcon loadResIcon(String name)
	{
		return loadResIcon(name,GraphicUtils.class);
	}
	

	public static ImageIcon loadResIcon(String name, Class requestor)
	{
		try
		{
			URL url = requestor.getResource("/xdev/ui/icons/" + name);
			return new ImageIcon(ImageIO.read(url));
		}
		catch(Exception e)
		{
			log.error(e);
			return null;
		}
	}
	

	/**
	 * Resizes the provided {@link Image} <code>image</code> to the given
	 * <code>width</code> and <code>height</code>.
	 * 
	 * <p>
	 * <strong>Hint:</strong> The provided {@link Image} will be resized
	 * </p>
	 * 
	 * @param image
	 *            to be resized
	 * @param width
	 *            new width of the image
	 * @param height
	 *            new height of the image
	 * @return the resized image.
	 */
	public static Image resize(@NotNull Image image, int width, int height)
	{
		image = image.getScaledInstance(width,height,getImageScaleMode());
		waitFor(image);
		return image;
	}
	

	/**
	 * Resizes the provided {@link Image} <code>image</code> either to the given
	 * <code>maxWidth</code> or <code>maxHeight</code>.
	 * 
	 * <p>
	 * <strong>Hint:</strong> The provided {@link Image} will be resized
	 * </p>
	 * 
	 * @param image
	 *            to be resized
	 * @param maxWidth
	 *            new maximum width of the image
	 * @param maxHeight
	 *            new maximum height of the image
	 * @return the resized image.
	 */
	public static Image resizeProportional(@NotNull Image image, int maxWidth, int maxHeight)
			throws IllegalArgumentException
	{
		if(maxWidth <= 0 || maxHeight <= 0)
		{
			throw new IllegalArgumentException("maxWidth and maxHeight must have a value > 0");
		}
		
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		
		double wf = (double)maxWidth / (double)imageWidth;
		double hf = (double)maxHeight / (double)imageHeight;
		if(Math.abs(wf) < Math.abs(hf))
		{
			maxHeight = -1;
		}
		else
		{
			maxWidth = -1;
		}
		
		return resize(image,maxWidth,maxHeight);
	}
	

	/**
	 * Waits for the provided {@link Image} to be loaded completely.
	 * 
	 * @param image
	 *            {@link Image} to wait for.
	 */
	public static void waitFor(Image image)
	{
		new ImageIcon(image);
	}
}
