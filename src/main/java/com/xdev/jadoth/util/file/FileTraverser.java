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
package com.xdev.jadoth.util.file;

import java.io.File;
import java.io.FileFilter;


/**
 * The Class FileTraverser.
 * 
 * @author Thomas Muenz
 */
public class FileTraverser
{
	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////

	/**
	 * Traverse directory.
	 * 
	 * @param directory the directory
	 * @param filter the filter
	 * @param directoryProcessor the directory processor
	 * @param fileProcessor the file processor
	 * @param deep the deep
	 */
	public static void traverseDirectory(
		final File directory,
		final FileFilter filter,
		FileProcessor directoryProcessor,
		final FileProcessor fileProcessor,
		final boolean deep
	)
	{
		if(directory == null || !directory.isDirectory()) return;
		if(directoryProcessor == null){
			directoryProcessor = new RecursiveDirectoryProcessor(filter, fileProcessor, deep);
		}
		traverseDirectoryIntern(directory, filter, directoryProcessor, fileProcessor, deep);
	}

	/**
	 * Traverse directory intern.
	 * 
	 * @param directory the directory
	 * @param filter the filter
	 * @param directoryProcessor the directory processor
	 * @param fileProcessor the file processor
	 * @param deep the deep
	 */
	protected static void traverseDirectoryIntern(
		final File directory,
		final FileFilter filter,
		final FileProcessor directoryProcessor,
		final FileProcessor fileProcessor,
		final boolean deep
	)
	{
		for(File f : directory.listFiles(filter)) {
			if(f.isDirectory()){
				if(deep){
					directoryProcessor.processFile(f);
				}
			}
			else {
				fileProcessor.processFile(f);
			}
		}
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	/** The directory. */
	private File directory = null;
	
	/** The filter. */
	private FileFilter filter = null;
	
	/** The directory processor. */
	private FileProcessor directoryProcessor = null;
	
	/** The file processor. */
	private FileProcessor fileProcessor = null;
	
	/** The deep. */
	private boolean deep = true;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * Instantiates a new file traverser.
	 * 
	 * @param directory the directory
	 * @param fileProcessor the file processor
	 */
	public FileTraverser(final File directory, final FileProcessor fileProcessor) {
		this(directory,
			new FileFilter(){
				@Override
				public boolean accept(final File pathname) {
					return true;
				}
			},
			null,
			fileProcessor,
			true
		);
	}
	
	/**
	 * Instantiates a new file traverser.
	 * 
	 * @param directory the directory
	 * @param filter the filter
	 * @param directoryProcessor the directory processor
	 * @param fileProcessor the file processor
	 * @param deep the deep
	 */
	public FileTraverser(
		final File directory,
		final FileFilter filter,
		final FileProcessor directoryProcessor,
		final FileProcessor fileProcessor,
		final boolean deep
	)
	{
		super();
		this.directory = directory;
		this.filter = filter;
		this.directoryProcessor = directoryProcessor;
		this.fileProcessor = fileProcessor;
		this.deep = deep;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * Gets the directory.
	 * 
	 * @return the directory
	 */
	public File getDirectory() {
		return directory;
	}
	
	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	public FileFilter getFilter() {
		return filter;
	}
	
	/**
	 * Checks if is deep.
	 * 
	 * @return the deep
	 */
	public boolean isDeep() {
		return deep;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////

	/**
	 * Sets the directory.
	 * 
	 * @param directory the directory to set
	 */
	public void setDirectory(final File directory) {
		this.directory = directory;
	}
	
	/**
	 * Sets the filter.
	 * 
	 * @param filter the filter to set
	 */
	public void setFilter(final FileFilter filter) {
		this.filter = filter;
	}
	
	/**
	 * Sets the deep.
	 * 
	 * @param deep the deep to set
	 */
	public void setDeep(final boolean deep) {
		this.deep = deep;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/**
	 * Traverse.
	 */
	public void traverse(){
		traverseDirectory(getDirectory(), getFilter(), getDirectoryProcessor(), getFileProcessor(), isDeep());
	}
	
	/**
	 * Traverse.
	 * 
	 * @param directory the directory
	 */
	public void traverse(final File directory){
		traverseDirectory(directory, getFilter(), getDirectoryProcessor(), getFileProcessor(), isDeep());
	}
	
	/**
	 * Traverse.
	 * 
	 * @param directory the directory
	 * @param deep the deep
	 */
	public void traverse(final File directory, final boolean deep){
		traverseDirectory(directory, getFilter(), getDirectoryProcessor(), getFileProcessor(), deep);
	}

	/**
	 * Gets the directory processor.
	 * 
	 * @return the directoryProcessor
	 */
	public FileProcessor getDirectoryProcessor() {
		return directoryProcessor;
	}

	/**
	 * Gets the file processor.
	 * 
	 * @return the fileProcessor
	 */
	public FileProcessor getFileProcessor() {
		return fileProcessor;
	}

	/**
	 * Sets the directory processor.
	 * 
	 * @param directoryProcessor the directoryProcessor to set
	 */
	public void setDirectoryProcessor(final FileProcessor directoryProcessor) {
		this.directoryProcessor = directoryProcessor;
	}

	/**
	 * Sets the file processor.
	 * 
	 * @param fileProcessor the fileProcessor to set
	 */
	public void setFileProcessor(final FileProcessor fileProcessor) {
		this.fileProcessor = fileProcessor;
	}

	/**
	 * The Class RecursiveDirectoryProcessor.
	 */
	public static class RecursiveDirectoryProcessor implements FileProcessor
	{
		
		/** The filter. */
		private FileFilter filter = null;
		
		/** The file processor. */
		private FileProcessor fileProcessor = null;
		
		/** The deep. */
		private boolean deep = true;

		/**
		 * Instantiates a new recursive directory processor.
		 * 
		 * @param filter the filter
		 * @param fileProcessor the file processor
		 * @param deep the deep
		 */
		public RecursiveDirectoryProcessor(final FileFilter filter, final FileProcessor fileProcessor, final boolean deep) {
			super();
			this.filter = filter;
			this.fileProcessor = fileProcessor;
			this.deep = deep;
		}

		/**
		 * @param file
		 * @see com.xdev.jadoth.util.file.FileProcessor#processFile(java.io.File)
		 */
		@Override
		public void processFile(final File file) {
			traverseDirectoryIntern(file, this.filter, this, this.fileProcessor, this.deep);
		}
	}

}
