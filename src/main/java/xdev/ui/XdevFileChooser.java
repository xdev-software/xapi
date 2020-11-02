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


import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

import xdev.io.XdevFile;


/**
 * {@link JFileChooser} which works with {@link XdevFileFilter}, meaning when
 * showing a save dialog, the filter ensures that the correct extension is added
 * to the selected file.
 * <p>
 * Additional methods:
 * <ul>
 * <li>{@link #showOpenDialog()}</li>
 * <li>{@link #showSaveDialog()}</li>
 * <li>{@link #askOverwriteIfExists(File)}</li>
 * </ul>
 * </p>
 * 
 * 
 * @see XdevFileChooserFactory
 * @see XdevFileFilter
 * @see XdevFileFilter#ensureFileEndsWithExtension(File)
 * 
 * @author XDEV Software Corp.
 * 
 */

public class XdevFileChooser extends JFileChooser
{
	/**
	 * Checks if the <code>file</code> exists and asks the user if he wants to
	 * overwrite it if necessary.
	 * 
	 * @param file
	 * @return <code>true</code> if the file doesn't exist or the used decides
	 *         to overwrite, <code>false</code> otherwise
	 */
	
	public static boolean askOverwriteIfExists(File file)
	{
		if(!file.exists())
		{
			return true;
		}
		
		String message = UIResourceBundle.getString("filechooser.overwrite.message",
				file.getAbsolutePath());
		return JOptionPane.showConfirmDialog(UIUtils.getActiveWindow(),message,
				UIResourceBundle.getString("filechooser.overwrite.title"),
				JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
	}
	
	
	public XdevFileChooser()
	{
		super();
	}
	
	
	public XdevFileChooser(File currentDirectory, FileSystemView fsv)
	{
		super(currentDirectory,fsv);
	}
	
	
	public XdevFileChooser(File currentDirectory)
	{
		super(currentDirectory);
	}
	
	
	public XdevFileChooser(FileSystemView fsv)
	{
		super(fsv);
	}
	
	
	public XdevFileChooser(String currentDirectoryPath, FileSystemView fsv)
	{
		super(currentDirectoryPath,fsv);
	}
	
	
	public XdevFileChooser(String currentDirectoryPath)
	{
		super(currentDirectoryPath);
	}
	
	
	public void clearChooseableFileFilters()
	{
		for(FileFilter fileFilter : getChoosableFileFilters())
		{
			removeChoosableFileFilter(fileFilter);
		}
	}
	
	
	/**
	 * Pops up an "Open File" file chooser dialog. Note that the text that
	 * appears in the approve button is determined by the L&F.
	 * 
	 * @return The selected file if the dialog has been approved,
	 *         <code>null</code> otherwise
	 * @throws HeadlessException
	 */
	
	public XdevFile showOpenDialog() throws HeadlessException
	{
		if(showOpenDialog(UIUtils.getActiveWindow()) == APPROVE_OPTION)
		{
			return toXdevFile(getSelectedFile());
		}
		
		return null;
	}
	
	
	/**
	 * Pops up an "Open File" file chooser dialog. Note that the text that
	 * appears in the approve button is determined by the L&F.
	 * 
	 * @return The selected files if the dialog has been approved,
	 *         <code>null</code> otherwise
	 * @throws HeadlessException
	 */
	
	public XdevFile[] showOpenDialogMultiSelection() throws HeadlessException
	{
		if(showOpenDialog(UIUtils.getActiveWindow()) == APPROVE_OPTION)
		{
			return XdevFile.toXdevFile(getSelectedFiles());
		}
		
		return null;
	}
	
	
	/**
	 * Pops up an "Save File" file chooser dialog. Note that the text that
	 * appears in the approve button is determined by the L&F.
	 * 
	 * @return The selected file if the dialog has been approved,
	 *         <code>null</code> otherwise
	 * @throws HeadlessException
	 * 
	 * @see #askOverwriteIfExists(File)
	 */
	public XdevFile showSaveDialog() throws HeadlessException
	{
		if(showSaveDialog(UIUtils.getActiveWindow()) == APPROVE_OPTION)
		{
			File file = getSelectedFile();
			if(askOverwriteIfExists(file))
			{
				return toXdevFile(file);
			}
			
		}
		
		return null;
	}
	
	
	@Override
	public XdevFile getSelectedFile()
	{
		File file = super.getSelectedFile();
		
		if(file != null && getDialogType() == SAVE_DIALOG)
		{
			FileFilter filter = getFileFilter();
			if(filter instanceof XdevFileFilter)
			{
				file = ((XdevFileFilter)filter).ensureFileEndsWithExtension(file);
			}
		}
		
		return toXdevFile(file);
	}
	
	
	public void addWindowListener(WindowListener l)
	{
		listenerList.add(WindowListener.class,l);
	}
	
	
	public void removeWindowListener(WindowListener l)
	{
		listenerList.remove(WindowListener.class,l);
	}
	
	
	@Override
	protected JDialog createDialog(Component parent) throws HeadlessException
	{
		JDialog dialog = super.createDialog(parent);
		
		for(WindowListener l : listenerList.getListeners(WindowListener.class))
		{
			dialog.addWindowListener(l);
		}
		
		return dialog;
	}
	
	
	/**
	 * Conversion Helper
	 * 
	 * @since 4.0
	 */
	private XdevFile toXdevFile(File file)
	{
		return file != null ? new XdevFile(file) : null;
	}
}
