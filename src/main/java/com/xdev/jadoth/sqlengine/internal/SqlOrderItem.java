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

package com.xdev.jadoth.sqlengine.internal;

import static com.xdev.jadoth.sqlengine.SQL.LANG.ASC;
import static com.xdev.jadoth.sqlengine.SQL.LANG.DESC;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;

import com.xdev.jadoth.lang.Copyable;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;


/**
 * The Class SqlOrderItem.
 * 
 * @author Thomas Muenz
 */
/**
 * @author Thomas Muenz
 *
 */
public class SqlOrderItem extends QueryPart
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = -1521811559370126972L;

	/** The Constant _ASC. */
	protected static final String _ASC = _+ASC;
	
	/** The Constant _DESC. */
	protected static final String _DESC = _+DESC;
	
	/** The Constant _NULLS_FIRST. */
	protected static final String _NULLS_FIRST = _+"NULLS FIRST";
	
	/** The Constant _NULLS_LAST. */
	protected static final String _NULLS_LAST = _+"NULLS LAST";


	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	/** The item. */
	final private Object item;
	
	/** The desc. */
	private Boolean desc;
	
	/** The nulls first. */
	private Boolean nullsFirst;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new sql order item.
	 * 
	 * @param item the item
	 */
	public SqlOrderItem(final Object item) 
	{
		this(item, null, null);
	}

	/**
	 * Instantiates a new sql order item.
	 * 
	 * @param item the item
	 * @param desc the desc
	 * @param nullsFirst the nulls first
	 */
	public SqlOrderItem(final Object item, final Boolean desc, final Boolean nullsFirst)
	{
		super();
		this.item = item;
		this.desc = desc;
		this.nullsFirst = nullsFirst;
	}
	/**
	 * 
	 * @param copySource
	 */
	protected SqlOrderItem(final SqlOrderItem copySource)
	{
		super();
		this.item = copySource.item;
		this.desc = copySource.desc;
		this.nullsFirst = copySource.nullsFirst;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the item.
	 * 
	 * @return the item
	 * @return
	 */
	public Object getItem() {
		return this.item;
	}

	/**
	 * Checks if is desc.
	 * 
	 * @return true, if is desc
	 * @return
	 */
	public boolean isDesc() {
		return this.desc;
	}
	
	/**
	 * Gets the nulls first.
	 * 
	 * @return the nullsFirst
	 */
	protected Boolean getNullsFirst() {
		return this.nullsFirst;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the desc.
	 * 
	 * @param desc the new desc
	 */
	public void setDesc(final Boolean desc) {
		this.desc = desc;
	}

	/**
	 * Sets the nulls first.
	 * 
	 * @param nullsFirst the nullsFirst to set
	 */
	public void setNullsFirst(final Boolean nullsFirst) {
		this.nullsFirst = nullsFirst;
	}


	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * Assemble.
	 * 
	 * @param dmlAssembler the dml assembler
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.QueryPart#assemble(DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(
		final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		assembleObject(this.item, dmlAssembler, sb, indentLevel, flags);
		if(this.desc != null) {
			sb.append(this.desc?_DESC:_ASC);
		}
		if(this.nullsFirst != null) {
			sb.append(this.nullsFirst?_NULLS_FIRST:_NULLS_LAST);
		}
		return sb;
	}

	/**
	 * @return
	 */
	@Override
	public Copyable copy()
	{
		return new SqlOrderItem(this);
	}

}
