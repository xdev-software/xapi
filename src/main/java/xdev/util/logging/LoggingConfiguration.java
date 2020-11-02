package xdev.util.logging;

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


import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

import xdev.io.IOUtils;
import xdev.util.systemproperty.StringSystemProperty;


/**
 * SystemProperty: xdev.util.logging.config.file
 * 
 * <p>
 * This class is necessary due to a JDK bug / spec flaw, that requires that a
 * absolute path for <code>java.util.logging.config.file</code> system property.
 * A relative path is required in most environments. This class provides tools
 * to read a log configuration from a relative path.
 * </p>
 * 
 * <p>
 * The log-Levels you can use in the properties file are:<br>
 * <ul>
 * <li>SEVERE (highest)</li>
 * <li>WARNING</li>
 * <li>INFO</li>
 * <li>CONFIG</li>
 * <li>FINE</li>
 * <li>FINER</li>
 * <li>FINEST (lowest)</li>
 * </ul>
 * </p>
 * 
 * <P>
 * The {@link LoggingConfiguration} supports following properties, which must be
 * set in the <code>System</code> properties. To change the properties in
 * Runtime in a type-safe manner you may use the {@link StringSystemProperty}
 * reference hold by {@link #SYSTEM_PROPERTY_CONFIG_FILE}.
 * 
 * <P>
 * <TABLE BORDER>
 * <TR>
 * <TH>Name</TH>
 * <TH>Type</TH>
 * <TH>Description</TH>
 * </TR>
 * 
 * 
 * <TR>
 * <TD>xdev.util.logging.config.file</TD>
 * <TD>String</TD>
 * <TD>
 * The <code>xdev.util.logging.config.file</code> System property can be used to
 * specify a properties file (in java.util.Properties format). The initial
 * logging configuration will be read from this file.
 * 
 * If the property is not defined then, the LogManager will read its initial
 * configuration from a properties file "lib/logging.properties" in the JRE
 * directory.</TD>
 * </TR>
 * 
 * 
 * </TABLE>
 * 
 * 
 * 
 * @author XDEV Software (RHHF)
 * @since 3.2
 * 
 * @see LogManager
 * 
 */
public class LoggingConfiguration
{
	/**
	 * The <code>xdev.util.logging.config.file</code> System property can be
	 * used to specify a properties file (in java.util.Properties format). The
	 * initial logging configuration will be read from this file.
	 * 
	 * If the property is not defined then, the LogManager will read its initial
	 * configuration from a properties file "lib/logging.properties" in the JRE
	 * directory.
	 */
	public final static StringSystemProperty	SYSTEM_PROPERTY_CONFIG_FILE	= new StringSystemProperty(
																					"xdev.util.logging.config.file");
	

	/**
	 * Reinitialize the logging properties and reread the logging configuration
	 * from the given path, which should be in java.util.Properties format. A
	 * PropertyChangeEvent will be fired after the properties are read.
	 * <p>
	 * Any log level definitions in the new configuration file will be applied
	 * using Logger.setLevel(), if the target Logger exists.
	 * 
	 * @param path
	 *            relative path to a properties file (in java.util.Properties
	 *            format) to read properties from
	 * @exception SecurityException
	 *                if a security manager exists and if the caller does not
	 *                have LoggingPermission("control").
	 */
	public static void readConfiguration(final String path) throws SecurityException
	{
		
		InputStream inputStream = null;
		try
		{
			inputStream = IOUtils.findResource(path);
			LogManager.getLogManager().readConfiguration(inputStream);
		}
		catch(final IOException e)
		{
			/*
			 * Explicit use of System.err because there is no logger to log to!
			 */
			System.err.println("Could not read logger configuration from " + String.valueOf(path));
			e.printStackTrace();
			throw new LoggingConfigurationException("Could not read logger configuration from "
					+ String.valueOf(path),e);
		}
		finally
		{
			if(inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch(final IOException e)
				{
					/*
					 * Explicit use of System.err because there is no logger to
					 * log to!
					 */
					e.printStackTrace();
				}
			}
		}
	}
	

	/**
	 * Reinitialize the logging properties and reread the logging configuration.
	 * <p>
	 * The same rules are used for locating the configuration properties as are
	 * used at startup ({@link #SYSTEM_PROPERTY_CONFIG_FILE}). So normally the
	 * logging properties will be re-read from the same file that was used at
	 * startup.
	 * <P>
	 * Any log level definitions in the new configuration file will be applied
	 * using Logger.setLevel(), if the target Logger exists.
	 * <p>
	 * This method may do nothing if {@link #SYSTEM_PROPERTY_CONFIG_FILE} is
	 * <code>null</code> or empty.
	 * <p>
	 * A PropertyChangeEvent will be fired after the properties are read.
	 * 
	 * @see #SYSTEM_PROPERTY_CONFIG_FILE
	 */
	public static void readConfiguration()
	{
		
		final String path = SYSTEM_PROPERTY_CONFIG_FILE.getValue();
		if(path != null && !path.isEmpty())
		{
			readConfiguration(path);
		}
	}
	
}
