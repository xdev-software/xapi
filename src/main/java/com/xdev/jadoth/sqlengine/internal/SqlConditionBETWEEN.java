
package com.xdev.jadoth.sqlengine.internal;

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


import static com.xdev.jadoth.sqlengine.SQL.LANG.BETWEEN;
import static com.xdev.jadoth.sqlengine.SQL.LANG._AND_;

import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;


/**
 * The Class SqlConditionBETWEEN.
 * 
 * @author Thomas Muenz
 */
public class SqlConditionBETWEEN extends SqlComparison
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5603662699575389425L;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	/**
	 * Instantiates a new sql condition between.
	 * 
	 * @param valueExpression the value expression
	 * @param lowerBoundExpression the lower bound expression
	 * @param upperBoundExpression the upper bound expression
	 */
	public SqlConditionBETWEEN(
		final Object valueExpression, final Object lowerBoundExpression, final Object upperBoundExpression
	) {
		super(valueExpression, BETWEEN, lowerBoundExpression, upperBoundExpression);
	}


	/**
	 * Gets the value expression.
	 * 
	 * @return the value expression
	 */
	public SqlExpression getValueExpression() {
		return this.lefthandExpression;
	}

	/**
	 * Gets the lower bound expression.
	 * 
	 * @return the lower bound expression
	 */
	public SqlExpression getLowerBoundExpression() {
		return this.righthandExpressions[0];
	}

	/**
	 * Gets the upper bound expression.
	 * 
	 * @return the upper bound expression
	 */
	public SqlExpression getUpperBoundExpression() {
		return this.righthandExpressions[1];
	}

	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 */
	@Override
	protected StringBuilder assembleRighthandExpression(
		final DbmsDMLAssembler<?> dmlAssembler, 
		final StringBuilder sb, 
		final int indentLevel, 
		final int flags
	)
	{
		QueryPart.concatSqlExpressions(dmlAssembler, sb, indentLevel, flags, _AND_, this.righthandExpressions);
		return sb;
	}	
	
}
