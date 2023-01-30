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
package com.xdev.jadoth.sqlengine.dbms;


import java.text.DateFormat;

import com.xdev.jadoth.sqlengine.internal.DoubleQuotedExpression;
import com.xdev.jadoth.sqlengine.internal.QuotedExpression;
import com.xdev.jadoth.sqlengine.internal.SqlClause;
import com.xdev.jadoth.sqlengine.internal.SqlColumn;
import com.xdev.jadoth.sqlengine.internal.SqlExpression;
import com.xdev.jadoth.sqlengine.internal.SqlTimestamp;
import com.xdev.jadoth.sqlengine.internal.interfaces.SelectItem;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.types.Query;



/**
 * The Interface DbmsDMLAssembler.
 * 
 * @param <A> the generic type
 */
public interface DbmsDMLAssembler<A extends DbmsAdaptor<A>>
{
	/**
	 * Assemble query.
	 * 
	 * @param query the query
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 */
	public void assembleQuery(Query query, StringBuilder sb, int indentLevel, int flags);

	
	public StringBuilder assembleSqlClause(
		SqlClause<?> sqlClause, StringBuilder sb, int indentLevel, int flags
	);
	
	/**
	 * Assemble select item.
	 * 
	 * @param selectItem the select item
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 */
	public void assembleSelectItem(SelectItem selectItem, StringBuilder sb, int indentLevel, int flags);

	/**
	 * Assemble expression.
	 * 
	 * @param expression the expression
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 */
	public void assembleExpression(SqlExpression expression, StringBuilder sb, int indentLevel, int flags);

	
	/**
	 * Assemble quoted expression.
	 * 
	 * @param expression the expression
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	public StringBuilder assembleQuotedExpression(
		QuotedExpression expression, StringBuilder sb, int indentLevel, int flags
	);

	/**
	 * Assemble double quoted expression.
	 * 
	 * @param expression the expression
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	public StringBuilder assembleDoubleQuotedExpression(
		DoubleQuotedExpression expression, StringBuilder sb, int indentLevel, int flags
	);
	
	public StringBuilder assembleColumnQualifier(SqlColumn column, StringBuilder sb, int flags);
	
	public StringBuilder assembleColumn(SqlColumn column, StringBuilder sb, int indentLevel, int flags);
	
	public StringBuilder assembleDelimitedIdentifier(String identifier, StringBuilder sb, int flags);
	
	public StringBuilder assembleStringValue(String s, StringBuilder sb, int indentLevel, int flags);
	
	public StringBuilder assembleObject(Object object, StringBuilder sb, int indentLevel, int flags);
	
	public StringBuilder assembleTableExpression(
		TableExpression tableExpression, StringBuilder sb, int indentLevel, int flags
	);
	
	public StringBuilder assembleTableIdentifier(
		SqlTableIdentity table, StringBuilder sb, int indentLevel, int flags
	);
	
	
	public DateFormat getDateFormatDATE();
	public DateFormat getDateFormatTIME();
	public DateFormat getDateFormatTIMESTAMP();
	
	public StringBuilder assembleDateTimeExpression(SqlTimestamp dateTimeExpression, StringBuilder sb);

}
