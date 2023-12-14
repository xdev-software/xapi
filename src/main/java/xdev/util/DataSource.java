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
package xdev.util;


import xdev.db.DBDataSource;
import xdev.lang.Copyable;


/**
 * 
 * Base type of all data sources like DBMs, plain files or any other arbitrary
 * data source.
 * 
 * @author XDEV Software Corp.
 * 
 * @see DBDataSource
 * 
 */
public interface DataSource<T extends DataSource>
{
	public String getName();
	
	
	public void putParameter(Parameter param);
	
	
	public Parameter getParameter(String name);
	
	
	public Parameter[] getParameters();
	
	
	public Parameter[] getDefaultParameters();
	
	
	public Parameter getDefaultParameter(String name);
	
	
	public <P> P getParameterValue(Parameter<P> template);
	
	
	public <P> P getParameterValue(Parameter<P> template, P defaultValue);
	
	
	/**
	 * 
	 * @param p
	 * @return <code>true</code> if the parameter <code>p</code> is required,
	 *         depending on the other parameters' values, <code>false</code>
	 *         otherwise
	 */
	
	public boolean isParameterRequired(Parameter p);
	
	
	public DataSourceMetaData getMetaData() throws DataSourceException;
	
	
	/**
	 * Clears all caches of this data source.
	 * <p>
	 * This method should be called after a parameter's value is changed if the
	 * data source has been used before.
	 * </p>
	 */
	public void clearCaches();
	
	
	
	public static class Parameter<T> implements Copyable<Parameter<T>>, Comparable<Parameter>
	{
		private int			rank	= -1;
		private String		name;
		private T			value;
		private T			defaultValue;
		private Class<T>	type;
		
		
		public Parameter(String name, T defaultValue)
		{
			this(name,defaultValue,(Class<T>)defaultValue.getClass());
		}
		
		
		public Parameter(String name, T defaultValue, Class<T> type)
		{
			this(name,defaultValue,defaultValue,type);
		}
		
		
		public Parameter(String name, T value, T defaultValue, Class<T> type)
		{
			this.name = name;
			this.value = value;
			this.defaultValue = defaultValue;
			this.type = type;
		}
		
		
		public String getName()
		{
			return name;
		}
		
		
		public T getValue()
		{
			return value;
		}
		
		
		public Parameter setValue(T value)
		{
			this.value = value;
			return this;
		}
		
		
		public T getDefaultValue()
		{
			return defaultValue;
		}
		
		
		public Class<T> getType()
		{
			return type;
		}
		
		
		@Override
		public Parameter<T> clone()
		{
			Parameter p = new Parameter(name,value,defaultValue,type);
			p.rank = rank;
			return p;
		}
		
		
		/**
		 * Returns a clone of this Parameter.
		 * 
		 * @param value
		 *            The value and default value
		 * 
		 * @return a clone of this Parameter
		 */
		public Parameter<T> clone(T value)
		{
			Parameter p = new Parameter(name,value,value,type);
			p.rank = rank;
			return p;
		}
		
		
		void setRank(int rank)
		{
			this.rank = rank;
		}
		
		
		@Override
		public int compareTo(Parameter param)
		{
			int diff = rank - param.rank;
			if(diff != 0)
			{
				return diff;
			}
			
			return name.compareTo(param.name);
		}
		
		
		/**
		 * Checks if <code>obj</code> is a parameter with the same name.
		 * 
		 * @return <code>true</code> if <code>obj</code> is a parameter with the
		 *         same name, <code>false</code> otherwise
		 */
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj == this)
			{
				return true;
			}
			
			if(obj instanceof Parameter)
			{
				return ((Parameter)obj).name.equals(name);
			}
			
			return false;
		}
		
		
		@Override
		public int hashCode()
		{
			return name.hashCode();
		}
		
		
		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			sb.append(name);
			sb.append(" = ");
			sb.append(value);
			
			return sb.toString();
		}
	}
}
