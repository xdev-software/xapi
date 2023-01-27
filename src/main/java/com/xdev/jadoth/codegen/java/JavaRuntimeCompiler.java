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

package com.xdev.jadoth.codegen.java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.xdev.jadoth.Jadoth;


/**
 * The Class JavaRuntimeCompiler.
 */
public class JavaRuntimeCompiler {

	/** The javac. */
	private final JavaCompiler javac;

	/** The src folder. */
	private File srcFolder;

	/** The bin folder. */
	private File binFolder;

	/** The file suffix. */
	private String fileSuffix = "java";


	/**
	 * Gets the file suffix.
	 *
	 * @return the fileSuffix
	 */
	public String getFileSuffix() {
		return this.fileSuffix;
	}


	/**
	 * Sets the file suffix.
	 *
	 * @param fileSuffix the fileSuffix to set
	 */
	public void setFileSuffix(final String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}


	/**
	 * Instantiates a new java runtime compiler.
	 */
	public JavaRuntimeCompiler(){
		this.javac = ToolProvider.getSystemJavaCompiler();
		if(this.javac == null) {
			throw new NullPointerException("System Java Compiler not found. Check for JDK and build path.");
		}
		this.srcFolder = null;
		this.binFolder = null;
	}


	/**
	 * Gets the src folder.
	 *
	 * @return the src folder
	 */
	public File getSrcFolder() {
		return this.srcFolder;
	}


	/**
	 * Sets the src folder.
	 *
	 * @param srcFolder the new src folder
	 */
	public void setSrcFolder(final String srcFolder) {
		File newFile;
		try {
			newFile = new File(srcFolder);
		} catch (final Exception e) {
			this.srcFolder = null;
			return;
		}

		if(newFile.isAbsolute()) {
			this.srcFolder = newFile;
		}
	}


	/**
	 * Gets the bin folder.
	 *
	 * @return the bin folder
	 */
	public File getBinFolder() {
		return this.binFolder;
	}


	/**
	 * Sets the bin folder.
	 *
	 * @param binFolder the new bin folder
	 */
	public void setBinFolder(final String binFolder) {
		File newFile;
		try {
			newFile = new File(binFolder);
		} catch (final Exception e) {
			this.binFolder = null;
			return;
		}

		if(newFile.isAbsolute()) {
			this.binFolder = newFile;
		}
	}



	/**
	 * Write class file.
	 *
	 * @param code the code
	 * @param filename the filename
	 * @param compile the compile
	 * @return the compiler status code
	 */
	public final CompilerStatusCode writeClassFile(final String code, final String filename, final boolean compile)
	{
		if(this.srcFolder == null) {
			throw new RuntimeException("Error: no valid src folder given.");
		}
		if(compile && this.binFolder == null) {
			throw new RuntimeException("Error: no valid bin folder given.");
		}

		Jadoth.ensureFolder(this.srcFolder);
		if(compile){
			Jadoth.ensureFolder(this.binFolder);
		}
		final File sourceFile = Jadoth.ensureWritableFile(this.srcFolder, filename + '.'+this.fileSuffix);
		final String sourceFileString = sourceFile.getAbsolutePath();

		if(!this.writeSourceFile(code, sourceFile)) {
			throw new RuntimeException("Error: Could not write file "+sourceFile);
		}

		if(!compile || !this.fileSuffix.toLowerCase().equals("java")){
			return new CompilerStatusCode("File "+sourceFileString+" written successfully (uncompiled).");
		}

		final File classFolder = Jadoth.ensureFolder(this.binFolder);
		final String classFolderString = classFolder.getAbsolutePath();

		if(classFolder == null) {
			throw new RuntimeException("Error: Could not reach folder "+classFolderString);
		}

		final ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();


		final int returnCode = this.javac.run(null, null, errorOutput, "-d", this.binFolder.getAbsolutePath(), sourceFileString);
		boolean success = false;
		if(returnCode == 0){
			success = true;
		}

		if(success){
			return new CompilerStatusCode(returnCode, "File "+sourceFileString+" compiled successfully.");
		}
		sourceFile.delete();
		classFolder.delete();
		return new CompilerStatusCode(returnCode, "Error: \n"+errorOutput.toString());
	}


	/**
	 * Compile.
	 *
	 * @param code the code
	 * @param filename the filename
	 * @return the compiler status code
	 */
	public final CompilerStatusCode compile(final String code, final String filename){
		return this.writeClassFile(code, filename, true);
	}




	/**
	 * Write source file.
	 *
	 * @param code the code
	 * @param sourceFile the source file
	 * @return true, if successful
	 */
	protected boolean writeSourceFile(final String code, final File sourceFile){
		try {
			sourceFile.createNewFile();
		} catch (final Exception e) {
			return false;
		}

		if(sourceFile.canWrite()){
			try(FileWriter sourceWriter = new FileWriter(sourceFile)) {
				sourceWriter.write(code);
			} catch (final IOException e) {
				sourceFile.delete();
				return false;
			}
		}
		else {
			return false;
		}
		return true;
	}


	/**
	 * The Class CompilerStatusCode.
	 */
	public static class CompilerStatusCode
	{

		/** The return code. */
		final private int returnCode;

		/** The message. */
		final private String message;

		/** The success. */
		final private boolean success;

		/**
		 * Instantiates a new compiler status code.
		 *
		 * @param message the message
		 */
		private CompilerStatusCode(final String message) {
			this(0, message);
		}

		/**
		 * Instantiates a new compiler status code.
		 *
		 * @param returnCode the return code
		 * @param message the message
		 */
		private CompilerStatusCode(final int returnCode, final String message) {
			super();
			this.returnCode = returnCode;
			this.message = message;
			this.success = returnCode == 0; //javac returns 0 for success
		}

		/**
		 * Gets the return code.
		 *
		 * @return the returnCode
		 */
		public Integer getReturnCode() {
			return this.returnCode;
		}

		/**
		 * Gets the message.
		 *
		 * @return the message
		 */
		public String getMessage() {
			return this.message;
		}

		/**
		 * Checks if is success.
		 *
		 * @return the success
		 */
		public boolean isSuccess() {
			return this.success;
		}

		/**
		 * To string.
		 *
		 * @return the string
		 * @return
		 */
		@Override
		public String toString() {
			return "Code("+this.returnCode+"): "+this.message;
		}


	}


}
