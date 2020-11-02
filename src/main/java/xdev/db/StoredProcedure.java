package xdev.db;

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


import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import xdev.util.MathUtils;


/**
 * The client side counterpart of a stored procedure in a database.
 * <p>
 * It holds information about the structure containing name, return value and
 * parameters of a stored procedure.
 * <p>
 * To actually invoke a stored procedure in the database with user defined
 * arguments see {@link DBConnection#call(StoredProcedure, Object...)}.
 * <p>
 * Example:
 * 
 * <pre>
 * Param customerGroup = new Param(ParamType.IN,&quot;customerGroup&quot;,DataType.VARCHAR);
 * Param revenue = new Param(ParamType.IN,&quot;revenue&quot;,DataType.DECIMAL);
 * StoredProcedure getCustomerCount = new StoredProcedure(ReturnTypeFlavor.TYPE,DataType.INTEGER,
 * 		&quot;getCustomerCount&quot;,null,customerGroup,revenue);
 * DBConnection con = DBUtils.getCurrentDataSource().openConnection();
 * con.call(getCustomerCount,&quot;whales&quot;,1000000);
 * Integer count = (Integer)getCustomerCount.getReturnValue();
 * con.close();
 * </pre>
 * 
 * @author XDEV Software
 */
public class StoredProcedure implements Comparable<StoredProcedure>, Serializable, AutoCloseable
{
	private static final long	serialVersionUID	= 5887159909079048104L;
	


	/**
	 * Describes the return type of a {@link StoredProcedure}.
	 * 
	 * @author XDEV Software
	 * 
	 */
	public static enum ReturnTypeFlavor
	{
		VOID, UNKNOWN, RESULT_SET, TYPE
	}
	
	private final ReturnTypeFlavor		returnTypeFlavor;
	/**
	 * only used if {@link #returnTypeFlavor} = {@link ReturnTypeFlavor#TYPE}.
	 */
	private final DataType				returnType;
	private final String				name;
	private final String				description;
	private final Param[]				params;
	private final Map<String, Param>	paramMap;
	private transient Object			returnValue;
	private final int					hash;
	private transient String			toString	= null;
	

	/**
	 * Constructs a new <code>StoredProcedure</code> with the given parameters.
	 * 
	 * @param returnTypeFlavor
	 *            the return flavor of this stored procedure
	 * 
	 * @param returnType
	 *            only used if {@link #returnTypeFlavor} =
	 *            {@link ReturnTypeFlavor#TYPE}
	 * 
	 * @param name
	 *            the name of this {@link StoredProcedure}
	 * 
	 * @param description
	 *            the description of this {@link StoredProcedure}
	 * 
	 * @param params
	 *            a array of {@link Param}
	 */
	public StoredProcedure(ReturnTypeFlavor returnTypeFlavor, DataType returnType, String name,
			String description, Param... params)
	{
		this.returnTypeFlavor = returnTypeFlavor;
		this.returnType = returnType;
		this.name = name;
		this.description = description;
		this.params = params;
		
		Map<String, Param> paramMap = new Hashtable();
		for(Param param : params)
		{
			paramMap.put(param.name,param);
		}
		this.paramMap = Collections.unmodifiableMap(paramMap);
		
		this.hash = MathUtils.computeHashDeep(returnTypeFlavor,returnType,name,params);
	}
	

	/**
	 * Returns the {@link ReturnTypeFlavor} of this {@link StoredProcedure}.
	 * 
	 * @return the {@link ReturnTypeFlavor}
	 */
	public ReturnTypeFlavor getReturnTypeFlavor()
	{
		return returnTypeFlavor;
	}
	

	/**
	 * Returns the return type of this {@link StoredProcedure}.
	 * 
	 * @return the return type if {@link #returnTypeFlavor} =
	 *         {@link ReturnTypeFlavor#TYPE}, <code>null</code> otherwise
	 */
	public DataType getReturnType()
	{
		return returnType;
	}
	

	/**
	 * Returns the name of this {@link StoredProcedure}.
	 * 
	 * @return the name of this {@link StoredProcedure}
	 */
	public String getName()
	{
		return name;
	}
	

	/**
	 * Returns the description of this {@link StoredProcedure}.
	 * 
	 * @return the description of this {@link StoredProcedure}
	 */
	public String getDescription()
	{
		return description;
	}
	

	/**
	 * Returns the number of parameter.
	 * 
	 * @return the number of parameter as <code>int</code> value.
	 */
	public int getParamCount()
	{
		return params.length;
	}
	

	/**
	 * Returns the {@link Param} with the specified <code>index</code>.
	 * 
	 * @param index
	 *            of the {@link Param}
	 * 
	 * @return the {@link Param} for the specified <code>index</code>
	 */
	public Param getParam(int index)
	{
		return params[index];
	}
	

	/**
	 * Returns all params of this stored procedure.
	 * 
	 * @return all params of this stored procedure
	 * 
	 * @since 3.1
	 */
	public Param[] getParams()
	{
		return Arrays.copyOf(params,params.length);
	}
	

	/**
	 * Returns the {@link Param} with the specified <code>name</code>.
	 * 
	 * @param name
	 *            of the {@link Param}
	 * 
	 * @return the {@link Param} for the specified <code>name</code>
	 */
	public Param getParam(String name)
	{
		return paramMap.get(name);
	}
	

	/**
	 * Prepares this procedure's params by setting the arguments.
	 * <p>
	 * This method is used by {@link DBConnection}s and is not intended to be
	 * called by the user.
	 * 
	 * @param args
	 *            the procedures arguments.
	 */
	public void prepareCall(Object... args)
	{
		returnValue = null;
		for(Param param : params)
		{
			param.value = null;
		}
		
		if(args != null && args.length > 0)
		{
			for(int pi = 0, ai = 0; pi < params.length && ai < args.length; pi++)
			{
				if(params[pi].getParamType() == ParamType.OUT)
				{
					continue;
				}
				
				params[pi].setValue(args[ai++]);
			}
		}
	}
	

	/**
	 * Sets the <code>returnValue</code> of this {@link StoredProcedure}.
	 * 
	 * @param returnValue
	 *            the new {@link #returnValue}
	 */
	public void setReturnValue(Object returnValue)
	{
		this.returnValue = returnValue;
	}
	

	/**
	 * Returns the return value of this {@link StoredProcedure}.
	 * 
	 * @return the return value as <code>Object</code>
	 */
	public Object getReturnValue()
	{
		return returnValue;
	}
	

	/**
	 * Returns the result of this {@link StoredProcedure}.
	 * <p>
	 * Note that {@link #getReturnTypeFlavor()} must be
	 * {@link ReturnTypeFlavor#RESULT_SET}, otherwise an
	 * {@link IllegalStateException} is thrown.
	 * 
	 * @return the procedure's result
	 * @throws IllegalStateException
	 *             if {@link #getReturnTypeFlavor()} is not
	 *             {@link ReturnTypeFlavor#RESULT_SET}
	 * 
	 * @since 3.1
	 */
	public Result getResult()
	{
		if(returnTypeFlavor != ReturnTypeFlavor.RESULT_SET)
		{
			throw new IllegalStateException("Procedure doesn't return a result");
		}
		
		return (Result)returnValue;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws DBException
	{
		if(returnValue instanceof Result)
		{
			((Result)returnValue).close();
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(StoredProcedure other)
	{
		return toString().compareTo(other.toString());
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
		{
			return true;
		}
		
		if(obj instanceof StoredProcedure)
		{
			StoredProcedure sp = (StoredProcedure)obj;
			if(returnTypeFlavor != sp.returnTypeFlavor || returnType != sp.returnType
					|| !name.equals(sp.name) || params.length != sp.params.length)
			{
				return false;
			}
			
			for(int i = 0; i < params.length; i++)
			{
				if(!params[i].equals(sp.params[i]))
				{
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return hash;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		if(toString == null)
		{
			StringBuilder sb = new StringBuilder();
			
			switch(returnTypeFlavor)
			{
				case TYPE:
					sb.append(returnType);
				break;
				
				default:
					sb.append(returnTypeFlavor);
			}
			
			sb.append(" ");
			sb.append(name);
			sb.append("(");
			
			for(int i = 0; i < params.length; i++)
			{
				if(i > 0)
				{
					sb.append(", ");
				}
				sb.append(params[i].toString());
			}
			
			sb.append(")");
			
			toString = sb.toString();
		}
		
		return toString;
	}
	


	/**
	 * Describes the type of a {@link Param}.
	 * 
	 * <ul>
	 * <li>IN</li>
	 * <li>OUT</li>
	 * <li>IN_OUT</li>
	 * </ul>
	 * 
	 * 
	 * @author XDEV Software
	 * 
	 */
	public static enum ParamType
	{
		/**
		 * The param is used as an argument only.
		 */
		IN,

		/**
		 * The param returns a value.
		 */
		OUT,

		/**
		 * The param is used as an argument and returns a value.
		 */
		IN_OUT
	}
	


	/**
	 * This class includes information about the {@link StoredProcedure}
	 * parameter.
	 * <p>
	 * The class has properties like paramType, name, dataType, value, hash.
	 * </p>
	 * 
	 * @author XDEV Software
	 * 
	 */
	public static class Param implements Serializable
	{
		private static final long	serialVersionUID	= 6869273219350312631L;
		
		private final ParamType		paramType;
		private final String		name;
		private final DataType		dataType;
		private transient Object	value;
		private final int			hash;
		private transient String	toString			= null;
		

		/**
		 * Constructs a new {@link Param} with the given <code>paramType</code>,
		 * <code>name</code> and the <code>dataType</code>.
		 * 
		 * @param paramType
		 *            the type of this {@link Param}
		 * 
		 * @param name
		 *            the name of this {@link Param}
		 * 
		 * @param dataType
		 *            the data type of this {@link Param}
		 */
		public Param(ParamType paramType, String name, DataType dataType)
		{
			this.paramType = paramType;
			this.name = name;
			this.dataType = dataType;
			
			this.hash = MathUtils.computeHash(paramType,name,dataType);
		}
		

		/**
		 * Returns the {@link ParamType} of this {@link Param}.
		 * 
		 * 
		 * @return the {@link ParamType} of this {@link Param}
		 */
		public ParamType getParamType()
		{
			return paramType;
		}
		

		/**
		 * Returns the name of this {@link Param}.
		 * 
		 * 
		 * @return the name of this {@link Param}
		 */
		public String getName()
		{
			return name;
		}
		

		/**
		 * Returns the {@link DataType} of this {@link Param}.
		 * 
		 * 
		 * @return the {@link DataType} of this {@link Param}
		 */
		public DataType getDataType()
		{
			return dataType;
		}
		

		/**
		 * Sets the value of this {@link Param}.
		 * 
		 * @param value
		 *            the new value
		 */
		public void setValue(Object value)
		{
			this.value = value;
		}
		

		/**
		 * Returns the value of this {@link Param}.
		 * 
		 * 
		 * @return the value as <code>Object</code>
		 */
		public Object getValue()
		{
			return value;
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object o)
		{
			if(o == this)
			{
				return true;
			}
			
			if(o instanceof Param)
			{
				Param p = (Param)o;
				return p.paramType == paramType && p.name.equals(name) && p.dataType == dataType;
			}
			
			return false;
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode()
		{
			return hash;
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			if(toString == null)
			{
				StringBuilder sb = new StringBuilder();
				sb.append(paramType);
				sb.append(" ");
				sb.append(dataType);
				sb.append(" ");
				sb.append(name);
				
				toString = sb.toString();
			}
			
			return toString;
		}
	}
}
