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

import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;


/**
 * The Class SqlBooleanArithmeticCondition.
 * 
 * @author Thomas Muenz
 */
public abstract class SqlBooleanArithmeticCondition extends SqlCondition 
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = 4538793162841087450L;

	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new sql boolean arithmetic condition.
	 * 
	 * @param con the con
	 */
	protected SqlBooleanArithmeticCondition(final Object con) {
		super(SQL.util.parseCondition(con));
	}
	
	protected SqlBooleanArithmeticCondition(
		final SqlBooleanArithmeticCondition copySource, final SqlBooleanArithmeticCondition nextCondition
	)
	{
		super(copySource, nextCondition);
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the condition.
	 * 
	 * @return the condition
	 */
	public SqlCondition getCondition(){
		return (SqlCondition)this.content;
	}


	/**
	 * Gets the newline prefix space.
	 * 
	 * @return the newline prefix space
	 */
	protected String getNewlinePrefixSpace() {
		return "  ";
	}
	
	/**
	 * Gets the new line suffix space.
	 * 
	 * @return the new line suffix space
	 */
	protected String getNewLineSuffixSpace() {
		return " ";
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////	
	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @see com.xdev.jadoth.sqlengine.internal.SqlCondition#assembleIntern(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected void assembleIntern(
		final DbmsDMLAssembler<?> dmlAssembler,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		this.assembleArithmetic(
			dmlAssembler, sb, indentLevel, flags, true
		);
	}

	/**
	 * Assemble arithmetic.
	 * 
	 * @param dmlAssembler the dml assembler
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @param isFirstInLine the is first in line
	 * @return the string builder
	 */
	protected StringBuilder assembleArithmetic(
		final DbmsDMLAssembler<?> dmlAssembler,
		final StringBuilder sb,
		final int indentLevel,
		final int flags,
		final boolean isFirstInLine
	)
	{
		sb.append(isFirstInLine&&!isSingleLine(flags)?this.getNewlinePrefixSpace():_);
		sb.append(this.keyword());
		sb.append(isFirstInLine&&!isSingleLine(flags)?this.getNewLineSuffixSpace():_);
		if(this.content != null) {
			if(this.content instanceof SqlBooleanArithmeticCondition){
				((SqlBooleanArithmeticCondition)this.content).assembleArithmetic(
					dmlAssembler, sb, indentLevel, flags, false
				);
			}
			else {
				((SqlCondition)this.content).assemble(
					dmlAssembler, sb, indentLevel, flags
				);
			}
		}
		return sb;
	}


	/**
	 * @return
	 */
	@Override
	public abstract SqlBooleanArithmeticCondition copy();
	/**
	 * @param copyDeepNextCondition
	 * @return
	 */
	@Override
	public abstract SqlBooleanArithmeticCondition copy(final Boolean copyDeepNextCondition);
	
}
