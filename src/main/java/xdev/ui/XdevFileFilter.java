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


import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Clone of {@link FileNameExtensionFilter}, but this filter works with
 * extensions which includes a dot '.' .
 * 
 * 
 * @author XDEV Software Corp.
 * 
 */

public class XdevFileFilter extends FileFilter
{
	// Description of this filter.
	private final String	description;
	// Known extensions.
	private final String[]	extensions;
	// Cached ext
	private final String[]	lowerCaseExtensions;
	

	/**
	 * Creates a {@code XdevFileFilter} with the specified description and file
	 * name extensions. The returned {@code XdevFileFilter} will accept all
	 * directories and any file with a file name extension contained in
	 * {@code extensions}.
	 * 
	 * @param description
	 *            textual description for the filter, may be {@code null}
	 * @param extensions
	 *            the accepted file name extensions without a leading dot, e.g.
	 *            "zip" not ".zip"
	 * @throws IllegalArgumentException
	 *             if extensions is {@code null}, empty, contains {@code null},
	 *             or contains an empty string
	 * @see #accept
	 */
	public XdevFileFilter(String description, String... extensions) throws IllegalArgumentException
	{
		if(extensions == null || extensions.length == 0)
		{
			throw new IllegalArgumentException("Extensions must be non-null and not empty");
		}
		this.description = description;
		this.extensions = new String[extensions.length];
		this.lowerCaseExtensions = new String[extensions.length];
		for(int i = 0; i < extensions.length; i++)
		{
			if(extensions[i] == null || extensions[i].length() == 0)
			{
				throw new IllegalArgumentException("Each extension must be non-null and not empty");
			}
			this.extensions[i] = extensions[i];
			lowerCaseExtensions[i] = "." + extensions[i].toLowerCase(Locale.ENGLISH);
		}
	}
	

	/**
	 * Tests the specified file, returning true if the file is accepted, false
	 * otherwise. True is returned if the extension matches one of the file name
	 * extensions of this {@code FileFilter}, or the file is a directory.
	 * 
	 * @param f
	 *            the {@code File} to test
	 * @return true if the file is to be accepted, false otherwise
	 */
	public boolean accept(File f)
	{
		if(f != null)
		{
			if(f.isDirectory())
			{
				return true;
			}
			
			String fileName = f.getName().toLowerCase(Locale.ENGLISH);
			for(String extension : lowerCaseExtensions)
			{
				if(fileName.endsWith(extension))
				{
					return true;
				}
			}
		}
		return false;
	}
	

	/**
	 * The description of this filter. For example: "JPG and GIF Images."
	 * 
	 * @return the description of this filter
	 */
	public String getDescription()
	{
		return description;
	}
	

	/**
	 * Returns the set of file name extensions files are tested against.
	 * 
	 * @return the set of file name extensions files are tested against
	 */
	public String[] getExtensions()
	{
		String[] result = new String[extensions.length];
		System.arraycopy(extensions,0,result,0,extensions.length);
		return result;
	}
	

	/**
	 * Ensures that the <code>file</code>'s name ends with a extension of this
	 * filter.
	 * 
	 * @param file
	 * @return the file with the optional corrected path
	 */
	
	public File ensureFileEndsWithExtension(File file)
	{
		String name = file.getName();
		
		for(String extension : lowerCaseExtensions)
		{
			if(name.toLowerCase(Locale.ENGLISH).endsWith(extension))
			{
				return file;
			}
		}
		
		return new File(file.getParentFile(),name + lowerCaseExtensions[0]);
	}
}
