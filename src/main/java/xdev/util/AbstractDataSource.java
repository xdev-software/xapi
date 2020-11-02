package xdev.util;

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


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

import xdev.db.ConnectionConfiguration;
import xdev.util.auth.EncryptedPassword;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


public abstract class AbstractDataSource<T extends DataSource> implements DataSource<T>
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger			LOGGER	= LoggerFactory
															.getLogger(AbstractDataSource.class);
	
	private String							qualifiedName;
	private String							simpleName;
	protected final Map<String, Parameter>	params	= new Hashtable();
	
	private String							defaultPath;
	
	protected ConnectionConfiguration		connConfig;
	
	
	public AbstractDataSource()
	{
		for(final Parameter param : this.getDefaultParameters())
		{
			this.putParameter(param);
		}
	}
	
	
	/**
	 * Initialize DataSource. qualifiedName/SimpleName and default path is
	 * generated by the class instance. Default location will set to source
	 * folder of the given class.
	 * 
	 * @param dbClass
	 *            of the specific JDBCDataSource implementation
	 */
	public void init(Class dbClass)
	{
		this.qualifiedName = dbClass.getName();
		this.simpleName = dbClass.getSimpleName();
		
		this.defaultPath = dbClass.getResource("").getPath();
		this.defaultPath = this.defaultPath.replace("/bin/","/src/");
		
		this.connConfig = new ConnectionConfiguration(this.simpleName,this.defaultPath);
	}
	
	
	/**
	 * Initialize DataSource.
	 * 
	 * @param qualifiedName
	 *            of the specific JDBCDataSource implementation
	 * @param simpleName
	 *            simple name of the specific JDBCDataSource implementation
	 * @param path
	 *            default path to db config property file
	 */
	public void init(String qualifiedName, String simpleName, String path)
	{
		this.qualifiedName = qualifiedName;
		this.simpleName = simpleName;
		
		this.defaultPath = path;
		
		this.connConfig = new ConnectionConfiguration(this.simpleName,this.defaultPath);
	}
	
	
	@Deprecated
	public void setName(String name)
	{
		this.qualifiedName = name;
	}
	
	
	@Override
	public String getName()
	{
		return this.qualifiedName;
	}
	
	
	/**
	 * Use default or get new parameters and set them for connecting to
	 * databases.
	 * 
	 * @param name
	 *            of the parameter
	 * @param defaultValue
	 *            value of the parameter
	 * @param overrideWithSystemProperty
	 *            if true it will be checked whether there is the system
	 *            argument "XDEV_DB_DBPROPERTYFILE" set or there is a property
	 *            file in the default location (same folder like the class who
	 *            is calling this methode)
	 */
	public void putParameterValue(String name, Object defaultValue,
			boolean overrideWithSystemProperty)
	{
		if(overrideWithSystemProperty && this.connConfig != null)
		{
			try
			{
				if(defaultValue instanceof Integer)
				{
					this.putParameterValue(name,
							Integer.valueOf((String)this.connConfig.getProperties().get(name)));
				}
				else if(defaultValue instanceof Boolean)
				{
					this.putParameterValue(name,
							Boolean.getBoolean((String)this.connConfig.getProperties().get(name)));
					
				}
				else if(defaultValue instanceof EncryptedPassword)
				{
					final EncryptedPassword password = new EncryptedPassword(
							(String)this.connConfig.getProperties().get(name));
					this.putParameterValue(name,password);
					
				}
				else
				{
					this.putParameterValue(name,this.connConfig.getProperties().get(name));
				}
			}
			catch(final FileNotFoundException e)
			{
				LOGGER.error("The value for " + name + " could not be found",e);
			}
			catch(final IOException e)
			{
				LOGGER.error("The value for " + name + " could not be found",e);
			}
		}
		else
		{
			this.putParameterValue(name,defaultValue);
		}
	}
	
	
	public <P> void putParameter(Parameter<P> param, P value)
	{
		this.putParameter(param.clone().setValue(value));
	}
	
	
	@Override
	public void putParameter(Parameter param)
	{
		this.params.put(param.getName(),param);
	}
	
	
	public void putParameterValue(String name, Object value)
	{
		this.getParameter(name).setValue(value);
	}
	
	
	@Override
	public Parameter getParameter(String name)
	{
		return this.params.get(name);
	}
	
	
	@Override
	public Parameter[] getParameters()
	{
		final Parameter[] paramArray = this.params.values().toArray(
				new Parameter[this.params.size()]);
		Arrays.sort(paramArray);
		return paramArray;
	}
	
	
	public <P> Parameter<P> getParameter(Parameter<P> template)
	{
		return this.params.get(template.getName());
	}
	
	
	@Override
	public <P> P getParameterValue(Parameter<P> template)
	{
		return this.getParameterValue(template,template.getDefaultValue());
	}
	
	
	@Override
	public <P> P getParameterValue(Parameter<P> template, P defaultValue)
	{
		final Parameter<P> param = this.getParameter(template);
		return param != null ? param.getValue() : defaultValue;
	}
	
	
	@Override
	public boolean isParameterRequired(Parameter p)
	{
		return true;
	}
	
	
	@Override
	public Parameter getDefaultParameter(String name)
	{
		for(final Parameter parameter : this.getDefaultParameters())
		{
			if(parameter.getName().equals(name))
			{
				return parameter;
			}
		}
		
		return null;
	}
	
	
	/**
	 * ranks the <code>params</code> in descending order
	 * 
	 * @param params
	 */
	
	protected static void rankParams(Parameter... params)
	{
		for(int i = 0; i < params.length; i++)
		{
			params[i].setRank(i + 1);
		}
	}
}
