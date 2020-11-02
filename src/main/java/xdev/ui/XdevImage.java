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
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.swing.ImageIcon;

import xdev.io.IOUtils;
import xdev.util.Settings;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;


/**
 * 
 * The standard image in XDEV. Based on {@link ImageIcon}.
 * 
 * <p>
 * Provides methods to load, change and write images.
 * </p>
 * 
 * <p>
 * Supported image formats are JPG, GIF, PNG and BMP.
 * </p>
 * 
 * @see ImageIcon
 * @see IOUtils
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 * 
 */
public class XdevImage extends ImageIcon
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log	= LoggerFactory.getLogger(XdevImage.class);
	
	
	/**
	 * Creates an {@link XdevImage} from an image object. If the image has a
	 * "comment" property that is a string, then the string is used as the
	 * description of this image.
	 * 
	 * @param image
	 *            the image
	 * @see Image#getProperty(String, java.awt.image.ImageObserver)
	 * @see #getDescription()
	 */
	public XdevImage(Image image)
	{
		super(image);
	}


	/**
	 * Loads an {@link XdevImage} from an {@link InputStream}.
	 * 
	 * @param in
	 *            an {@link InputStream} to read from
	 * @throws IOException
	 *             if an error occurs during reading
	 */
	public XdevImage(InputStream in) throws IOException
	{
		super(ImageIO.read(in));

		if(getImage() == null)
		{
			throw new IOException("Error loading image or unsupported image format");
		}
	}


	/**
	 * Loads an {@link XdevImage} from a {@link File}.
	 * 
	 * @param file
	 *            a {@link File} to read from
	 * @throws IOException
	 *             if an error occurs during reading
	 */
	public XdevImage(File file) throws IOException
	{
		super(ImageIO.read(file));

		if(getImage() == null)
		{
			throw new IOException("Error loading image or unsupported image format: "
					+ file.getAbsolutePath());
		}
	}


	/**
	 * Loads an {@link XdevImage} from an {@link URL}.
	 * 
	 * @param url
	 *            a {@link URL} to read from
	 * @throws IOException
	 *             if an error occurs during reading
	 */
	public XdevImage(URL url) throws IOException
	{
		super(ImageIO.read(url));

		if(getImage() == null)
		{
			throw new IOException("Error loading image or unsupported image format: "
					+ url.toExternalForm());
		}
	}


	/**
	 * Creates an {@link XdevImage} from an array of bytes containing the image
	 * data.
	 * 
	 * @param data
	 *            the image data as an array of bytes
	 * @throws IOException
	 *             if an error occurs while creating the image
	 */
	public XdevImage(byte[] data) throws IOException
	{
		super(ImageIO.read(new ByteArrayInputStream(data)));

		if(getImage() == null)
		{
			throw new IOException("Unable to create image or unsupported image format");
		}
	}


	/**
	 * Creates an {@link XdevImage} from an path containing the image data.
	 * 
	 * @param path
	 *            the relative path of the image, e.g. 'res/image/splash.png'
	 * 
	 * @throws IOException
	 *             if an error occurs while creating the image
	 */
	public XdevImage(String path) throws IOException
	{
		super(GraphicUtils.loadImagePlain(path));
	}


	/**
	 * Gets the width of the image.
	 * 
	 * @return the width in pixels of this image.
	 */
	public int getImageWidth()
	{
		return getIconWidth();
	}


	/**
	 * Gets the height of the image.
	 * 
	 * @return the height in pixels of this image.
	 */
	public int getImageHeight()
	{
		return getIconHeight();
	}


	/**
	 * Scales this image to the given <code>width</code> and <code>height</code>
	 * .
	 * 
	 * 
	 * <p>
	 * If either <code>width</code> or <code>height</code> is a negative number
	 * then a value is substituted to maintain the aspect ratio of the original
	 * image dimensions. If both <code>width</code> and <code>height</code> are
	 * negative, then the original image dimensions are used.
	 * </p>
	 * 
	 * @param width
	 *            the width to which to scale the image.
	 * 
	 * @param height
	 *            the height to which to scale the image.
	 */
	public void scale(int width, int height)
	{
		try
		{
			Image image = getImage();
			Image scaledImage = image.getScaledInstance(width,height,Settings.getImageScaleMode());
			new ImageIcon(scaledImage); // loadImage
			image.flush();
			setImage(scaledImage);
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}


	/**
	 * Returns this {@link XdevImage}'s <code>Image</code>.
	 * 
	 * @return the <code>Image</code> object for this <code>XdevImage</code>
	 */
	public Image getAWTImage()
	{
		return getImage();
	}


	/**
	 * Writes an image using an arbitrary {@link ImageWriter} that supports the
	 * given format to an {@link OutputStream}.
	 * <p>
	 * This method does not close the provided {@link OutputStream} after the
	 * write operation has completed; it is the responsibility of the caller to
	 * close the stream, if desired.
	 * 
	 * @param out
	 *            the {@link OutputStream} to be written to
	 * @param format
	 *            the informal name of the format
	 * @throws IOException
	 *             if an error occurs during writing
	 * @throws IllegalArgumentException
	 *             if format is not supported
	 * 
	 * @see ImageFileFilter#getSupportedImageExtensions()
	 * @see ImageFileFilter#checkImageFormat(String)
	 */
	public void write(OutputStream out, String format) throws IOException, IllegalArgumentException
	{
		write(out,format,0.75);
	}


	/**
	 * Writes an image using an arbitrary {@link ImageWriter} that supports the
	 * given format to an {@link OutputStream}.
	 * <p>
	 * This method does not close the provided {@link OutputStream} after the
	 * write operation has completed; it is the responsibility of the caller to
	 * close the stream, if desired.
	 * <p>
	 * If the format doesn't support compression the parameter
	 * <code>quality</code> has no effect.
	 * <p>
	 * Quality guidelines:
	 * <ul>
	 * <li>0.25 low quality</li>
	 * <li>0.5 medium quality</li>
	 * <li>0.75 visually lossless</li>
	 * </ul>
	 * 
	 * @param out
	 *            the {@link OutputStream} to be written to
	 * @param format
	 *            the informal name of the format
	 * @param quality
	 *            compression quality from 0.0 (low) to 1.0 (high)
	 * @throws IOException
	 *             if an error occurs during writing
	 * @throws IllegalArgumentException
	 *             if format is not supported or quality is not between 0.0 and
	 *             1.0, inclusive
	 * 
	 * @see ImageFileFilter#getSupportedImageExtensions()
	 * @see ImageFileFilter#checkImageFormat(String)
	 */
	public void write(OutputStream out, String format, double quality) throws IOException,
			IllegalArgumentException
	{
		format = ImageFileFilter.checkImageFormat(format);
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(format);
		if(!iter.hasNext())
		{
			// Shouldn't happen since we check the format
			throw new IllegalArgumentException("Unsupported image format: " + format);
		}
		RenderedImage image = GraphicUtils.createBufferedImage(getImage());
		ImageWriter writer = iter.next();
		if(writer instanceof JPEGImageWriter)
		{
			writer.setOutput(ImageIO.createImageOutputStream(out));
			ImageWriteParam param = writer.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionType("JPEG");
			param.setCompressionQuality((float)quality);
			IIOImage iioImage = new IIOImage(image,null,null);
			writer.write(null,iioImage,param);
			writer.dispose();
		}
		else
		{
			ImageIO.write(image,format,out);
		}
	}


	/**
	 * Writes an image using an arbitrary {@link ImageWriter} that supports the
	 * given format to a {@link File}. If there is already a {@link File}
	 * present, its contents are discarded.
	 * 
	 * @param file
	 *            the {@link File} to be written to
	 * @param format
	 *            the informal name of the format
	 * @throws IOException
	 *             if an error occurs during writing
	 * @throws IllegalArgumentException
	 *             if format is not supported
	 * 
	 * @see ImageFileFilter#getSupportedImageExtensions()
	 * @see ImageFileFilter#checkImageFormat(String)
	 */
	public void write(File file, String format) throws IOException, IllegalArgumentException
	{
		write(file,format,0.75);
	}


	/**
	 * Writes an image using an arbitrary {@link ImageWriter} that supports the
	 * given format to a {@link File}.
	 * <p>
	 * If the format doesn't support compression the parameter
	 * <code>quality</code> has no effect.
	 * <p>
	 * Quality guidelines:
	 * <ul>
	 * <li>0.25 low quality</li>
	 * <li>0.5 medium quality</li>
	 * <li>0.75 visually lossless</li>
	 * </ul>
	 * 
	 * @param file
	 *            the {@link File} to be written to
	 * @param format
	 *            the informal name of the format
	 * @param quality
	 *            compression quality from 0.0 (low) to 1.0 (high)
	 * @throws IOException
	 *             if an error occurs during writing
	 * @throws IllegalArgumentException
	 *             if format is not supported or quality is not between 0.0 and
	 *             1.0, inclusive
	 * 
	 * @see ImageFileFilter#getSupportedImageExtensions()
	 * @see ImageFileFilter#checkImageFormat(String)
	 */
	public void write(File file, String format, double quality) throws IOException,
			IllegalArgumentException
	{
		FileOutputStream out = new FileOutputStream(file);
		try
		{
			write(out,format,quality);
		}
		finally
		{
			IOUtils.closeSilent(out);
		}
	}


	/**
	 * Writes a JPG image of this {@link XdevImage} to the given
	 * <code>handle</code>.
	 * 
	 * <pre>
	 * Some guidelines: 0.75 high quality
	 *                  0.50 medium quality
	 *                  0.25 low quality
	 * </pre>
	 * 
	 * @param handle
	 *            handle to write on
	 * @param quality
	 *            0.0-1.0 setting of desired quality level.
	 * 
	 * @throws IOException
	 *             if the image could not be written to handle
	 * @deprecated use {@link #write(OutputStream, String, double)} or
	 *             {@link #write(File, String, double)} instead
	 */
//	@Deprecated
//	public void writeJPG(int handle, double quality) throws IOException
//	{
//		BinaryIO bio = IOUtils.getBinaryIO(handle);
//		OutputStream out = bio.getDataOutput();
//		BufferedImage bi = GraphicUtils.createBufferedImage(this.getImage(),false);
//		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
//		param.setQuality((float)quality,true);
//		encoder.setJPEGEncodeParam(param);
//		encoder.encode(bi);
//		bio.close();
//	}


	/**
	 * Writes a PNG image of this {@link XdevImage} to the given
	 * <code>handle</code>.
	 * 
	 * @param handle
	 *            handle to write on
	 * 
	 * @throws IOException
	 *             if the image could not be written to handle
	 * 
	 * @deprecated use {@link #write(OutputStream, String)} or
	 *             {@link #write(File, String)} instead
	 */
//	@Deprecated
//	public void writePNG(int handle) throws IOException
//	{
//		BinaryIO bio = IOUtils.getBinaryIO(handle);
//		OutputStream out = bio.getDataOutput();
//		BufferedImage bi = GraphicUtils.createBufferedImage(this.getImage());
//		ImageIO.write(bi,"PNG",out);
//		bio.close();
//	}
}
