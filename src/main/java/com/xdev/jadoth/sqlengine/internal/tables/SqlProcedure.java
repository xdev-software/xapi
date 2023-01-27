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

package com.xdev.jadoth.sqlengine.internal.tables;

import static com.xdev.jadoth.sqlengine.SQL.LANG.CREATE_PROCEDURE;
import static com.xdev.jadoth.sqlengine.SQL.LANG.IN;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.TAB;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.cma;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;

import java.util.ArrayList;

import com.xdev.jadoth.sqlengine.SQL.DATATYPE;
import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper;



/**
 * The Class SqlProcedure.
 * 
 * @param <A> the generic type
 */

@Deprecated
public class SqlProcedure<A extends DbmsAdaptor<A>>
{
	
	/** The name. */
	protected String name;
	
	/** The return type. */
	protected DATATYPE returnType;
	
	/** The type length. */
	protected int typeLength = 0;
	
	/** The code. */
	protected String code = null;
	
	/** The parameters. */
	protected ArrayList<Parameter<A>> parameters;
	
	/** The dbms. */
	protected A dbms;




	/**
	 * Instantiates a new sql procedure.
	 * 
	 * @param dbms the dbms
	 * @param name the name
	 * @param returnType the return type
	 * @param typeLength the type length
	 * @param code the code
	 * @param parameters the parameters
	 */
	public SqlProcedure(
		final A dbms,
		final String name,
		final DATATYPE returnType,
		final int typeLength,
		final String code,
		final Parameter<A>... parameters
	)
	{
		super();
		this.dbms = dbms;
		this.name = name;
		this.returnType = returnType;
		this.typeLength = typeLength;
		this.code = code;
		if(parameters != null) {
			this.parameters = new ArrayList<Parameter<A>>(parameters.length);
			for (final Parameter<A> p : parameters) {
				this.parameters.add(p);
			}
		}
		else {
			this.parameters = new ArrayList<Parameter<A>>();
		}
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the return type.
	 * 
	 * @return the return type
	 */
	public DATATYPE getReturnType() {
		return this.returnType;
	}

	/**
	 * Gets the type length.
	 * 
	 * @return the type length
	 */
	public int getTypeLength() {
		return this.typeLength;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public ArrayList<Parameter<A>> getParameters() {
		return this.parameters;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Sets the return type.
	 * 
	 * @param returnType the new return type
	 */
	public void setReturnType(final DATATYPE returnType) {
		this.returnType = returnType;
	}

	/**
	 * Sets the type length.
	 * 
	 * @param typeLength the new type length
	 */
	public void setTypeLength(final int typeLength) {
		this.typeLength = typeLength;
	}

	/**
	 * Sets the code.
	 * 
	 * @param code the new code
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * Sets the parameters.
	 * 
	 * @param parameters the new parameters
	 */
	public void setParameters(final ArrayList<Parameter<A>> parameters) {
		this.parameters = parameters;
	}


	/**
	 * The Class Parameter.
	 * 
	 * @param <A> the generic type
	 */
	public static class Parameter<A extends DbmsAdaptor<A>>
	{
		
		/** The name. */
		private final String name;
		
		/** The type. */
		private final DATATYPE type;
		
		/** The length. */
		private final int length;


		/**
		 * Instantiates a new parameter.
		 * 
		 * @param name the name
		 * @param type the type
		 */
		public Parameter(final String name, final DATATYPE type) {
			this(name, type, 0);
		}

		/**
		 * Instantiates a new parameter.
		 * 
		 * @param name the name
		 * @param type the type
		 * @param length the length
		 */
		public Parameter(final String name, final DATATYPE type, final int length) {
			super();
			this.name = name;
			this.type = type;
			this.length = length;
		}
		
		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
		
		/**
		 * Gets the type.
		 * 
		 * @return the type
		 */
		public DATATYPE getType() {
			return this.type;
		}
		
		/**
		 * Gets the length.
		 * 
		 * @return the length
		 */
		public int getLength() {
			return this.length;
		}

		/**
		 * To ddl string.
		 * 
		 * @param ddlMapper the ddl mapper
		 * @return the string
		 */
		public String toDdlString(final DbmsDDLMapper<A> ddlMapper) {
			final StringBuilder sb = new StringBuilder(256);
			sb.append(this.name).append(':').append(ddlMapper.getDataTypeDDLString(this.type, null));
			if(this.type.isLengthed()) {
				sb.append(par).append(this.length).append(rap);
			}
			return sb.toString();
		}


	}

	/**
	 * Assemble head.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assembleHead(StringBuilder sb)
	{
		if(sb == null) {
			sb = new StringBuilder(2048);
		}

		final DbmsDDLMapper<?> ddlMapper = this.dbms.getDdlMapper();

		sb.append(CREATE_PROCEDURE).append(_).append(this.name).append(par);
		if(this.parameters.size() > 0) {
			for (int i = 0, len = this.parameters.size(); i < len; i++) {
				if(i == 0) {
					sb.append(NEW_LINE);
				}
				else {
					sb.append(cma).append(NEW_LINE);
				}
				final Parameter<A> p = this.parameters.get(i);
				sb.append(TAB).append(IN).append(_).append(p.getName()).append(_)
				.append(ddlMapper.getDataTypeDDLString(p.type, null));
				if(p.type.isLengthed()) {
					sb.append(par).append(p.length).append(rap);
				}
			}
			sb.append(NEW_LINE).append(rap).append(NEW_LINE);
			if(this.returnType != null) {
				sb.append("RETURNS").append(_).append(ddlMapper.getDataTypeDDLString(this.returnType, null));
				if(this.returnType.isLengthed()) {
					sb.append(par).append(this.typeLength).append(rap);
				}
				sb.append(NEW_LINE);
			}
		}
		return sb;
	}

	/**
	 * Assemble.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assemble(StringBuilder sb) {
		if(sb == null) {
			sb = new StringBuilder(2048);
		}
		this.assembleHead(sb);

		this.assembleCode(sb);
		return sb;
	}

	/**
	 * Assemble code.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assembleCode(StringBuilder sb) {
		if(sb == null) {
			sb = new StringBuilder(2048);
		}
		sb.append(this.code);
		return sb;
	}


	/**
	 * CREAT e_ procedure.
	 * 
	 * @return the string
	 */
	public String CREATE_PROCEDURE() {
		return this.assemble(null).toString();
	}

}
