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
import java.io.PrintStream;


/**
 * The Class FilenamePrinter.
 * 
 * @author Thomas Muenz
 */
public class FilenamePrinter implements FileProcessor
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	/** The print stream. */
	private final PrintStream printStream;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public FilenamePrinter()
	{
		this(System.out);
	}
	
	/**
	 * Instantiates a new filename printer.
	 * 
	 * @param printStream the print stream
	 * @throws NullPointerException the null pointer exception
	 */
	public FilenamePrinter(final PrintStream printStream) throws NullPointerException
	{
		super();
		if(printStream == null) throw new NullPointerException("printStream may not be null");
		this.printStream = printStream;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * Process file.
	 * 
	 * @param file the file
	 * @see com.xdev.jadoth.util.file.FileProcessor#processFile(java.io.File)
	 */
	@Override
	public void processFile(final File file)
	{
		this.printStream.println(file);
	}

}
