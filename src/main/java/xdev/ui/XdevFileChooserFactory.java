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


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.filechooser.FileFilter;


/**
 * Factory class for {@link XdevFileChooser}.
 * <p>
 * Example:<br>
 * <br>
 * <code>
 * XdevFileChooser chooser = XdevFileChooserFactory.getFileChooser(<br>
 * "Media", // key used for caching and persistance <br>
 * new XdevFileFilter("Audio Files","mp3","flac","ogg"),<br>
 * new XdevFileFilter("Video Files","mp4","mpg","avi"));<br>
 * XdevFile file = chooser.showOpenDialog();
 * </code>
 * </p>
 * 
 * 
 * @see XdevFileChooser
 * @see XdevFileFilter
 * 
 * @author XDEV Software Corp.
 * 
 */

public final class XdevFileChooserFactory
{
	private XdevFileChooserFactory()
	{
	}
	
	private static Map<String, FileChooserState>	cache;
	private static XdevFileChooser					fileChooser;
	
	
	/**
	 * Shortcut for getFileChooser(key,false,false,filters).
	 * 
	 * @see #getFileChooser(String, boolean, boolean, FileFilter...)
	 */
	public static XdevFileChooser getFileChooser(String key, FileFilter... filters)
	{
		return getFileChooser(key,false,false,filters);
	}
	
	
	/**
	 * Shortcut for getFileChooser(key,false,acceptAllFileFilter,filters).
	 * 
	 * @see #getFileChooser(String, boolean, boolean, FileFilter...)
	 */
	
	public static XdevFileChooser getFileChooser(String key, boolean acceptAllFileFilter,
			FileFilter... filters)
	{
		return getFileChooser(key,false,acceptAllFileFilter,filters);
	}
	
	
	/**
	 * Returns a {@link XdevFileChooser} associated with <code>key</code>.
	 * <p>
	 * If the XdevFileChooser should check for the right extension when saving a
	 * file use a {@link XdevFileFilter}.
	 * </p>
	 * 
	 * @param key
	 *            the id of the file chooser, e.g. "XML files" or "images"
	 * @param multiSelectionEnabled
	 *            true if multiple files may be selected
	 * @param acceptAllFileFilter
	 *            true if the all file filter should be used
	 * @param filters
	 *            the filters for the filechooser
	 * @return the file chooser associated with <code>key</code>
	 */
	
	public static XdevFileChooser getFileChooser(String key, boolean multiSelectionEnabled,
			boolean acceptAllFileFilter, FileFilter... filters)
	{
		if(cache == null)
		{
			cache = new Hashtable();
			fileChooser = new XdevFileChooser();
		}
		
		FileChooserState state = cache.get(key);
		if(state != null)
		{
			fileChooser.setCurrentDirectory(state.currentDirectory);
		}
		else
		{
			state = new FileChooserState();
			cache.put(key,state);
		}
		
		fileChooser.addWindowListener(state);
		fileChooser.setMultiSelectionEnabled(multiSelectionEnabled);
		fileChooser.clearChooseableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed(acceptAllFileFilter);
		for(FileFilter filter : filters)
		{
			fileChooser.addChoosableFileFilter(filter);
		}
		
		return fileChooser;
	}
	
	
	
	private static class FileChooserState extends WindowAdapter
	{
		File	currentDirectory;
		
		
		public void windowClosed(WindowEvent e)
		{
			currentDirectory = fileChooser.getCurrentDirectory();
			fileChooser.removeWindowListener(this);
		}
	}
}
