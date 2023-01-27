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

import static com.xdev.jadoth.sqlengine.SQL.LANG.IS;
import static com.xdev.jadoth.sqlengine.SQL.LANG.IS_NOT;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.gt;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.gte;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.is;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.lt;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.lte;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.ne;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.dbms.standard.StandardDMLAssembler;
import com.xdev.jadoth.sqlengine.internal.interfaces.SelectItem;

/**
 * The Class SqlExpression.
 *
 * @author Thomas Muenz
 */
public class SqlExpression extends QueryPart implements SelectItem
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = -6665335334642316340L;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////	
	/** The expression. */
	protected Object expression;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new sql expression.
	 *
	 * @param expression the expression
	 */
	public SqlExpression(final Object expression) 
	{
		super();
		this.expression = expression;
	}
	
	protected SqlExpression(final SqlExpression copySource)
	{
		super();
		this.expression = copySource.expression; //do NOT copy expression because SqlField etc shall not be copied.
	}


	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	/**
	 * AS.
	 *
	 * @param alias the alias
	 * @return the sql expression
	 */
	public final SqlExpression AS(final String alias) 
	{
		return new AliasedExpression(this, alias);
	}

	
	

	/**
	 * O.
	 *
	 * @param operator the operator
	 * @param value the value
	 * @return the sql operation
	 */
	public final SqlOperation o(final String operator, final Object value) 
	{
		return new SqlOperation(this, operator, value);
	}

	/**
	 * C.
	 *
	 * @param operator the operator
	 * @param value the value
	 * @return the sql comparison
	 */
	public final SqlComparison c(final String operator, final Object value) 
	{
		return new SqlComparison(this, operator, value);
	}

	//convenience methods, maybe cause more hassle than c("...", value), but who knows who wants them...
	/**
	 * Eq.
	 *
	 * @param value the value
	 * @return the sql comparison
	 */
	public final SqlComparison eq(final Object value) 
	{
		return new SqlComparison(this, is, value);
	}

	/**
	 * Ne.
	 *
	 * @param value the value
	 * @return the sql comparison
	 */
	public final SqlComparison ne(final Object value) 
	{
		return new SqlComparison(this, ne, value);
	}

	/**
	 * Gt.
	 *
	 * @param value the value
	 * @return the sql comparison
	 */
	public final SqlComparison gt(final Object value) 
	{
		return new SqlComparison(this, gt, value);
	}

	/**
	 * Lt.
	 *
	 * @param value the value
	 * @return the sql comparison
	 */
	public final SqlComparison lt(final Object value) 
	{
		return new SqlComparison(this, lt, value);
	}

	/**
	 * Gte.
	 *
	 * @param value the value
	 * @return the sql comparison
	 */
	public final SqlComparison gte(final Object value) 
	{
		return new SqlComparison(this, gte, value);
	}

	/**
	 * Lte.
	 *
	 * @param value the value
	 * @return the sql comparison
	 */
	public final SqlComparison lte(final Object value) 
	{
		return new SqlComparison(this, lte, value);
	}



	/**
	 * I s_ null.
	 *
	 * @return the sql comparison
	 */
	public final SqlComparison IS_NULL() 
	{
		return new SqlComparison(this, IS, SQL.NULL);
	}

	/**
	 * I s_ no t_ null.
	 *
	 * @return the sql comparison
	 */
	public final SqlComparison IS_NOT_NULL() 
	{
		return new SqlComparison(this, IS_NOT, SQL.NULL);
	}

	/**
	 * IN.
	 *
	 * @param list the list
	 * @return the sql comparison
	 */
	public final SqlComparison IN(final Object... list) 
	{
		return new SqlConditionIN(this, list);
	}

	/**
	 * NO t_ in.
	 *
	 * @param list the list
	 * @return the sql comparison
	 */
	public final SqlComparison NOT_IN(final Object... list) 
	{
		return new SqlConditionNOT(this, new SqlConditionIN(null, list));
	}

	/**
	 * LIKE.
	 *
	 * @param value the value
	 * @return the sql condition like
	 */
	public final SqlConditionLIKE LIKE(final Object value) 
	{
		return new SqlConditionLIKE(this, value);
	}

	/**
	 * NO t_ like.
	 *
	 * @param value the value
	 * @return the sql condition not
	 */
	public final SqlConditionNOT NOT_LIKE(final Object value) 
	{
		return new SqlConditionNOT(this, new SqlConditionLIKE(null, value));
		
	}


	/**
	 * BETWEEN.
	 *
	 * @param lowerBoundExpression the lower bound expression
	 * @param upperBoundExpression the upper bound expression
	 * @return the sql condition between
	 */
	public final SqlConditionBETWEEN BETWEEN(final Object lowerBoundExpression, final Object upperBoundExpression)
	{
		return new SqlConditionBETWEEN(this, lowerBoundExpression, upperBoundExpression);
	}

	/**
	 * NO t_ between.
	 *
	 * @param lowerBoundExpression the lower bound expression
	 * @param upperBoundExpression the upper bound expression
	 * @return the sql condition not
	 */
	public final SqlConditionNOT NOT_BETWEEN(final Object lowerBoundExpression, final Object upperBoundExpression) 
	{
		return new SqlConditionNOT(this, new SqlConditionBETWEEN(null, lowerBoundExpression, upperBoundExpression));
	}

	/**
	 * CONCAT.
	 *
	 * @param value the value
	 * @return the sqlx function concat
	 */
	public final SqlxFunctionCONCAT CONCAT(final Object value) 
	{
		return new SqlxFunctionCONCAT(this, value);
	}

	/**
	 * CONTAINS.
	 *
	 * @param value the value
	 * @return the sql comparison
	 */
	public final SqlComparison CONTAINS(final Object value) 
	{
		return new SqlxConditionCONTAINS(this, value);
	}

	/**
	 * NO t_ contains.
	 *
	 * @param value the value
	 * @return the sql comparison
	 */
	public final SqlComparison NOT_CONTAINS(final Object value) 
	{
		return new SqlConditionNOT(this, new SqlxConditionCONTAINS(null, value));
	}



	/**
	 * _ asc.
	 *
	 * @return the sql order item null sortable
	 */
	public final SqlOrderItemNullSortable _ASC() 
	{
		return new SqlOrderItemNullSortable(this, false);
	}

	/**
	 * _ desc.
	 *
	 * @return the sql order item null sortable
	 */
	public final SqlOrderItemNullSortable _DESC() 
	{
		return new SqlOrderItemNullSortable(this, true);
	}

	/**
	 * _ null s_ first.
	 *
	 * @return the sql order item
	 */
	public final SqlOrderItem _NULLS_FIRST() 
	{
		return new SqlOrderItem(this, null, Boolean.TRUE);
	}

	/**
	 * _ null s_ last.
	 *
	 * @return the sql order item
	 */
	public final SqlOrderItem _NULLS_LAST() 
	{
		return new SqlOrderItem(this, null, Boolean.FALSE);
	}


	/**
	 * Gets the expression.
	 *
	 * @return the expression
	 */
	public String getExpression() 
	{
		return this.expression==null?null:this.expression.toString();
	}

	/**
	 * Gets the expression object.
	 *
	 * @return the expression object
	 */
	public Object getExpressionObject() 
	{
		return this.expression;
	}


	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		return this.assemble(
			StandardDMLAssembler.getSingletonStandardDMLAssembler(),
			new StringBuilder(),
			defaultExpressionStringBuilderLength,
			0
		).toString();
	}




	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.QueryPart#assemble(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(
		final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		if(this.expression instanceof SqlExpression) {
			dmlAssembler.assembleExpression((SqlExpression)this.expression, sb, indentLevel, flags);
			return sb;
		}
		return assembleObject(this.expression, dmlAssembler, sb, indentLevel, flags);
	}
	
	/**
	 * Copy as.
	 *
	 * @param alias the alias
	 * @return the sql expression
	 */
	public final SqlExpression copyAS(final String alias) 
	{
		return this.copy().AS(alias);
	}



	/**
	 * @return
	 */
	@Override
	public SqlExpression copy()
	{
		return new SqlExpression(this);
	}

}
