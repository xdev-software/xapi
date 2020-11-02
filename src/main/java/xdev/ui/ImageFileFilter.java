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


import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.imageio.ImageIO;


/**
 * A XdevFileFilter which filters all supported images.
 * 
 * @see #getInstance()
 * @see #getSupportedImageExtensions()
 * 
 * @author XDEV Software
 * 
 */
public class ImageFileFilter extends XdevFileFilter
{
	private static ImageFileFilter	instance;


	/**
	 * Returns the single instance of the image file filter.
	 * 
	 * @return the image file filter singleton
	 */
	public static ImageFileFilter getInstance()
	{
		if(instance == null)
		{
			instance = new ImageFileFilter();
		}

		return instance;
	}


	/**
	 * Creates a new image file filter.
	 */
	protected ImageFileFilter()
	{
		super(UIResourceBundle.getString("filefilter.supportedimages"),
				getSupportedImageExtensions());
	}


	/**
	 * Returns all supported image format extensions, as an lowercase string
	 * array.
	 * <p>
	 * Default supported images, may vary depending on the platform this
	 * application is running on:
	 * <ul>
	 * <li>jpg</li>
	 * <li>gif</li>
	 * <li>png</li>
	 * <li>bmp</li>
	 * <li>wbmp</li>
	 * </ul>
	 * 
	 * @return All supported image format extensions
	 */
	public static String[] getSupportedImageExtensions()
	{
		Set<String> set = new LinkedHashSet();
		for(String str : ImageIO.getWriterFormatNames())
		{
			set.add(str.toLowerCase());
		}
		String[] extensions = set.toArray(new String[set.size()]);
		Arrays.sort(extensions);
		return extensions;
	}


	/**
	 * Checks if <code>format</code> is a supported image format and returns an
	 * optional case-corrected format string.
	 * 
	 * @param format
	 *            the image format to check, e.g. "gif", "png", "jpg"
	 * @return optional case-corrected format string or <code>format</code>
	 * @throws IllegalArgumentException
	 *             if <code>format</code> is not supported
	 */
	public static String checkImageFormat(String format) throws IllegalArgumentException
	{
		format = format.toLowerCase();

		String[] supportedFormats = ImageFileFilter.getSupportedImageExtensions();
		for(String supportedFormat : supportedFormats)
		{
			if(supportedFormat.equals(format))
			{
				return format;
			}
		}

		throw new IllegalArgumentException("Unsupported format: " + format
				+ ", supported formats: " + Arrays.toString(supportedFormats));
	}
}
