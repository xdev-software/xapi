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
package xdev.db;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import xdev.util.AbstractDataSource;
import xdev.util.auth.EncryptedPassword;
import xdev.util.auth.Password;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.util.systemproperty.XdevSystemPropertyKeys;
import xdev.util.systemproperty.XdevSystemPropertyUtils;


/**
 * 
 * Loads database configuration settings from properties file.
 * 
 * @author XDEV Software (MP)
 * @since 4.0
 * 
 */
public class ConnectionConfiguration
{
	
	private static final String		IS_SERVER_DATA_SOURCE	= "isServerDataSource";
	
	private static final String		URL_EXTENSION			= "urlExtension";
	
	private static final String		CATALOG					= "catalog";
	
	private static final String		PASSWORD				= "password";
	
	private static final String		USERNAME				= "username";
	
	private static final String		PORT					= "port";
	
	private static final String		HOST					= "host";
	
	private static final String     MAX_STATEMENTS_PER_CONNECTION        = "maxStatementsPerConnection";
	
	private static final String		DEFAULT_XML_ENCODING	= "UTF-8";
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	LOGGER					= LoggerFactory
																	.getLogger(ConnectionConfiguration.class);
	
	private Properties				properties;
	private String					propertyFile;
	
	private final String			dbClassname;
	
	private boolean					isXML					= true;
	
	
	/**
	 * Set propertyFile either with the location get from system property or the
	 * default location next to the db instance. The file name is always build
	 * with {@code (dbClassname + "_config.properties")} or
	 * {@code (dbClassname + "_config.xml")}.
	 * 
	 * @param dbClassname
	 *            name of the database instance
	 */
	public ConnectionConfiguration(String dbClassname)
	{
		this.dbClassname = dbClassname;
		
		String propertyFileLocation = XdevSystemPropertyUtils
				.getStringValue(XdevSystemPropertyKeys.DATABASE_CONNECTION_PROPERTYFILE_LOCATION);
		
		if(propertyFileLocation != null && !propertyFileLocation.endsWith("/"))
		{
			propertyFileLocation = propertyFileLocation + "/";
		}
		
		if(propertyFileLocation == null)
		{
			propertyFileLocation = this.getClass().getResource("").getPath();
		}
		
		this.createPropertyFileString(dbClassname,propertyFileLocation);
	}
	
	
	/**
	 * @param dbClassname
	 * @param propertyFileLocation
	 */
	private void createPropertyFileString(String dbClassname, String propertyFileLocation)
	{
		this.propertyFile = propertyFileLocation + dbClassname + "_config.xml";
		if(!new File(this.propertyFile).exists())
		{
			this.propertyFile = propertyFileLocation + dbClassname + "_config.properties";
			this.isXML = false;
		}
		this.propertyFile = propertyFileLocation + dbClassname + "_config.properties";
	}
	
	
	/**
	 * Set propertyFile either with the location get from system property or the
	 * default location next to the db instance. The file name is always build
	 * with {@code (dbClassname + "_config.properties")}.
	 * 
	 * @param dbClassname
	 *            classname of the database instance
	 * @param defaultFilePath
	 *            path to defaultFile without filename
	 */
	public ConnectionConfiguration(String dbClassname, String defaultFilePath)
	{
		
		this.dbClassname = dbClassname;
		
		String propertyFileLocation = XdevSystemPropertyUtils
				.getStringValue(XdevSystemPropertyKeys.DATABASE_CONNECTION_PROPERTYFILE_LOCATION);
		
		if(propertyFileLocation != null && !propertyFileLocation.endsWith("/"))
		{
			propertyFileLocation = propertyFileLocation + "/";
		}
		
		if(propertyFileLocation == null)
		{
			if(defaultFilePath == null)
			{
				LOGGER.error("propertyFileLocatin and defaultFilepath is NULL.\nCheck if "
						+ AbstractDataSource.class.getSimpleName()
						+ " is initalized by calling one of its init() methods.");
				throw new NullPointerException();
			}
			propertyFileLocation = defaultFilePath;
		}
		
		this.createPropertyFileString(dbClassname,propertyFileLocation);
	}
	
	
	/**
	 * Set propertyFile either with the location get from system property or the
	 * default location next to the db instance. The file name is always build
	 * with {@code (dbClassname + "_config.properties")}.
	 * 
	 * @param targetClass
	 *            class where the default properties file have to be located
	 */
	public ConnectionConfiguration(Class<?> targetClass)
	{
		
		if(targetClass == null)
		{
			throw new NullPointerException();
		}
		
		this.dbClassname = targetClass.getSimpleName();
		
		String propertyFileLocation = XdevSystemPropertyUtils
				.getStringValue(XdevSystemPropertyKeys.DATABASE_CONNECTION_PROPERTYFILE_LOCATION);
		
		if(propertyFileLocation != null && !propertyFileLocation.endsWith("/"))
		{
			propertyFileLocation = propertyFileLocation + "/";
		}
		
		if(propertyFileLocation == null)
		{
			
			final String className = targetClass.getSimpleName().concat(".class");
			
			propertyFileLocation = targetClass.getResource(className).getPath();
			
			propertyFileLocation = propertyFileLocation.replace(className,"");
		}
		
		this.propertyFile = propertyFileLocation + this.dbClassname + "_config.xml";
		if(!new File(this.propertyFile).exists())
		{
			this.propertyFile = propertyFileLocation + this.dbClassname + "_config.properties";
			this.isXML = false;
		}
		this.propertyFile = propertyFileLocation + this.dbClassname + "_config.properties";
	}
	
	
	/**
	 * Set and store property.
	 * 
	 * @param key
	 *            , name of value
	 * @param value
	 *            , property value
	 * @param xmlEncoding
	 *            , encoding for the xml property file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public void storeProperty(String key, String value, String xmlEncoding) throws IOException
	{
		
		if(this.properties == null)
		{
			this.properties = new Properties();
		}
		
		this.properties.setProperty(key,value);
		this.writeProperties();
	}
	
	
	/**
	 * Set and store properties.
	 * 
	 * @param propertiesMap
	 *            with properties
	 * @param xmlEncoding
	 *            , encoding for the xml property file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public void storeProperties(Map<String, String> propertiesMap, String xmlEncoding)
			throws IOException
	{
		
		for(final String key : propertiesMap.keySet())
		{
			this.properties.setProperty(key,propertiesMap.get(key));
		}
		
		this.writeProperties();
	}
	
	
	/**
	 * Set and store property.
	 * 
	 * @param key
	 *            , name of value
	 * @param value
	 *            , property value
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public void storeProperty(String key, String value) throws IOException
	{
		this.storeProperty(key,value,DEFAULT_XML_ENCODING);
	}
	
	
	/**
	 * Set and store properties.
	 * 
	 * @param propertiesMap
	 *            with properties
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public void storeProperties(Map<String, String> propertiesMap) throws IOException
	{
		this.storeProperties(propertiesMap,DEFAULT_XML_ENCODING);
	}
	
	
	/**
	 * Delete the propertyFile.
	 */
	public void deletePropertyFile()
	{
		final File file = new File(this.propertyFile);
		file.delete();
	}
	
	
	private void writeProperties() throws IOException
	{
		FileWriter fileWriter = null;
		
		try
		{
			if(this.isXML)
			{
				this.properties.storeToXML(new FileOutputStream(this.propertyFile),
						"Configuration Settings for class" + this.dbClassname);
			}
			else
			{
				fileWriter = new FileWriter(this.propertyFile);
				this.properties.store(fileWriter,"Configuration Settings for class" + this.dbClassname);
			}
			
		}
		catch(final IOException e)
		{
			final String err = "Properties for " + this.dbClassname + " can not stored at " + this.propertyFile;
			LOGGER.error(e,err);
			throw new IOException(err,e);
		}
		finally
		{
			try
			{
				fileWriter.close();
			}
			catch(final Exception e)
			{
				LOGGER.warning("FileWriter can not closed. " + e.getStackTrace());
			}
			
		}
		
	}
	
	
	/**
	 * Load property file or xml property file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void loadProperties() throws FileNotFoundException, IOException
	{
		
		if(this.properties == null)
		{
			
			this.properties = new Properties();
			
			try
			{
				if(this.isXML)
				{
					this.properties.loadFromXML(new FileInputStream(this.propertyFile));
				}
				else
				{
					this.properties.load(new FileReader(this.propertyFile));
				}
			}
			catch(final FileNotFoundException e)
			{
				final String err = "Can not found file: " + this.propertyFile;
				LOGGER.error(e,err);
				throw new FileNotFoundException(err);
			}
			catch(final IOException e)
			{
				final String err = "Can not load properties file at " + this.propertyFile;
				LOGGER.error(e,err);
				throw new IOException(err,e);
			}
		}
	}
	
	
	/**
	 * 
	 * @param key
	 *            to String value
	 * @return value (String)
	 * @throws FileNotFoundException
	 *             , Can not found file, Can not found file
	 * @throws IOException
	 *             , Can not load properties file, Can not load properties file
	 *             Can not load properties file
	 */
	public String getString(String key) throws FileNotFoundException, IOException
	{
		this.loadProperties();
		final String value = (String)this.properties.get(key);
		return value;
	}
	
	
	/**
	 * 
	 * @param key
	 *            to Integer value
	 * @return value (Integer)
	 * @throws FileNotFoundException
	 *             , Can not found file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public Integer getInteger(String key) throws FileNotFoundException, IOException
	{
		this.loadProperties();
		final String strValue = (String)this.properties.get(key);
		final int value = Integer.parseInt(strValue);
		return value;
	}
	
	
	/**
	 * 
	 * @param key
	 *            to boolean value
	 * @return value (boolean)
	 * @throws FileNotFoundException
	 *             , Can not found file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public Boolean getBoolean(String key) throws FileNotFoundException, IOException
	{
		this.loadProperties();
		final String strValue = (String)this.properties.get(key);
		final boolean value = Boolean.parseBoolean(strValue);
		return value;
	}
	
	
	/**
	 * 
	 * @param key
	 *            to password value
	 * @return password (EncryptedPassword)
	 * @throws FileNotFoundException
	 *             , Can not found file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public EncryptedPassword getEncryptedPassword(String key) throws FileNotFoundException,
			IOException
	{
		this.loadProperties();
		final String strValue = (String)this.properties.get(key);
		final EncryptedPassword value = new EncryptedPassword(strValue);
		return value;
	}
	
	
	/**
	 * 
	 * @return host
	 * @throws FileNotFoundException
	 *             , Can not found file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public String getHost() throws FileNotFoundException, IOException
	{
		this.loadProperties();
		final String host = this.properties.getProperty(HOST);
		this.nullCheck(host,HOST);
		return host;
	}
	
	
	/**
	 * Return port number. Throws NullpointerException if no port number can be
	 * load.
	 * 
	 * @return port number
	 * @throws FileNotFoundException
	 *             , Can not found file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public int getPort() throws FileNotFoundException, IOException
	{
		this.loadProperties();
		final String property = this.properties.getProperty(PORT);
		final int port = Integer.valueOf(property);
		this.nullCheck(port,PORT);
		return port;
	}
	
	
	/**
	 * Return username. Throws NullpointerException if no username can be load.
	 * 
	 * @return username
	 * @throws FileNotFoundException
	 *             , Can not found file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public String getUsername() throws FileNotFoundException, IOException
	{
		this.loadProperties();
		final String username = this.properties.getProperty(USERNAME);
		this.nullCheck(username,USERNAME);
		return username;
	}
	
	
	/**
	 * 
	 * @return password
	 * @throws FileNotFoundException
	 *             , Can not found file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public Password getPassword() throws FileNotFoundException, IOException
	{
		this.loadProperties();
		final String property = this.properties.getProperty(PASSWORD);
		final EncryptedPassword password = new EncryptedPassword(property);
		return password;
	}
	
	
	/**
	 * 
	 * @return catalog
	 * @throws FileNotFoundException
	 *             , Can not found file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public String getCatalog() throws FileNotFoundException, IOException
	{
		this.loadProperties();
		final String catalog = this.properties.getProperty(CATALOG);
		return catalog;
	}
	
	
	/**
	 * 
	 * @return urlExtension
	 * @throws FileNotFoundException
	 *             , Can not found file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public String getUrlExtension() throws FileNotFoundException, IOException
	{
		this.loadProperties();
		final String urlExtension = this.properties.getProperty(URL_EXTENSION);
		return urlExtension;
	}
	
	
	/**
	 * Return isServerDataSource.
	 * 
	 * @return isServerDataSource true or false
	 * @throws FileNotFoundException
	 *             , Can not found file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public boolean isServerDataSource() throws FileNotFoundException, IOException
	{
		this.loadProperties();
		final String property = this.properties.getProperty(IS_SERVER_DATA_SOURCE);
		final boolean isServerDataSource = Boolean.parseBoolean(property);
		return isServerDataSource;
	}
	
	
	public int getMaxStatementsPerConnection()
	{
		try {
			this.loadProperties();
		} catch (final IOException e) {
			return -1;
		}
		final String property = this.properties.getProperty(MAX_STATEMENTS_PER_CONNECTION);
		final int maxStmtPerCon = Integer.parseInt(property);
		return maxStmtPerCon;
	}
	
	/**
	 * Returns all properties.
	 * 
	 * @return properties a Hashtable, with key and value pairs
	 * @throws FileNotFoundException
	 *             , Can not found file
	 * @throws IOException
	 *             , Can not load properties file
	 */
	public Hashtable<Object, Object> getProperties() throws FileNotFoundException, IOException
	{
		this.loadProperties();
		return this.properties;
	}
	
	
	private void nullCheck(Object propertyValue, String propertyKey)
	{
		
		if(propertyValue == null)
		{
			final String err = "Property " + propertyKey.toString() + " is not set in property file at "
					+ this.propertyFile;
			LOGGER.error(err);
			throw new NullPointerException(err);
		}
		
	}
}
