package xdev.lang.cmd;

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


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xdev.db.DBConnection;
import xdev.db.DBDataSource;
import xdev.db.DBException;
import xdev.db.DBUtils;
import xdev.db.Result;
import xdev.db.sql.Column;
import xdev.db.sql.Condition;
import xdev.db.sql.Expression;
import xdev.db.sql.Functions;
import xdev.db.sql.SELECT;
import xdev.db.sql.Table;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;
import xdev.vt.VirtualTableFillMethod;


public abstract class Query extends XdevCommandObject
{
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface SqlFunction
	{
	}
	


	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ColumnNamePossible
	{
	}
	
	private DBDataSource				dataSource;
	protected final SELECT				select;
	private List						parameters;
	private VirtualTable				virtualTable;
	private List<VirtualTableColumn>	targetColumns;
	private VirtualTableFillMethod		fillMethod;
	private String						primaryTable;
	private Map<String, Table>			tableCache		= new HashMap();
	private Map<String, Integer>		aliasCounter	= new HashMap();
	

	public Query(Object... args)
	{
		super(args);
		
		select = new SELECT();
		parameters = new ArrayList();
		targetColumns = new ArrayList();
		fillMethod = VirtualTableFillMethod.OVERWRITE;
		aliasCounter = new HashMap();
	}
	

	public void setDataSource(DBDataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	

	public Query select(String column)
	{
		select.columns(getColumn(column));
		return this;
	}
	

	public Query select(String table, String column)
	{
		if(primaryTable == null || !table.equals(primaryTable))
		{
			select.columns(new Column(getTableWithAlias(table,false),column));
		}
		else
		{
			select.columns(getColumn(table,column));
		}
		
		return this;
	}
	

	public Query select2(Object expression)
	{
		select.columns(expression);
		return this;
	}
	

	public void into(VirtualTableColumn column)
	{
		if(column != null)
		{
			select.ensureNameOfLastColumn(column.getName());
		}
		
		targetColumns.add(column);
	}
	

	public void from(String table)
	{
		primaryTable = table;
		select.FROM(new Table(table));
	}
	

	public void innerJoin(String table, String column, String table2, String column2)
	{
		Table sqlTable = getTableWithAlias(table,true);
		Table sqlTable2 = new Table(table2);
		select.INNER_JOIN(sqlTable,new Column(sqlTable,column).eq(new Column(sqlTable2,column2)));
	}
	

	public void outerJoin(String table, String column, String table2, String column2)
	{
		Table sqlTable = getTableWithAlias(table,true);
		Table sqlTable2 = new Table(table2);
		select.OUTER_JOIN(sqlTable,new Column(sqlTable,column).eq(new Column(sqlTable2,column2)));
	}
	

	public void leftJoin(String table, String column, String table2, String column2)
	{
		Table sqlTable = getTableWithAlias(table,true);
		Table sqlTable2 = new Table(table2);
		select.LEFT_JOIN(sqlTable,new Column(sqlTable,column).eq(new Column(sqlTable2,column2)));
	}
	

	public void rightJoin(String table, String column, String table2, String column2)
	{
		Table sqlTable = getTableWithAlias(table,true);
		Table sqlTable2 = new Table(table2);
		select.RIGHT_JOIN(sqlTable,new Column(sqlTable,column).eq(new Column(sqlTable2,column2)));
	}
	

	public void innerJoin(String table, Object condition)
	{
		select.INNER_JOIN(new Table(table),condition);
	}
	

	public void outerJoin(String table, Object condition)
	{
		select.OUTER_JOIN(new Table(table),condition);
	}
	

	public void leftJoin(String table, Object condition)
	{
		select.LEFT_JOIN(new Table(table),condition);
	}
	

	public void rightJoin(String table, Object condition)
	{
		select.RIGHT_JOIN(new Table(table),condition);
	}
	

	public void where(Object condition, Object... params)
	{
		select.WHERE(condition);
		addParameters(params);
	}
	

	public void addParameters(Object... params)
	{
		if(params != null && params.length > 0)
		{
			Collections.addAll(this.parameters,params);
		}
	}
	

	public Condition condition(Object condition)
	{
		return Condition.valueOf(condition);
	}
	

	public void groupBy(Object... fields)
	{
		select.GROUP_BY(fields);
	}
	

	public void having(Object condition, Object... params)
	{
		select.HAVING(condition);
		
		if(params != null && params.length > 0)
		{
			parameters.addAll(Arrays.asList(params));
		}
	}
	
	public final static int	ORDER_DEFAULT	= 0;
	public final static int	ORDER_ASC		= 1;
	public final static int	ORDER_DESC		= 2;
	
	public final static int	NULLS_DEFAULT	= 0;
	public final static int	NULLS_FIRST		= 1;
	public final static int	NULLS_LAST		= 2;
	

	public void orderBy(String column, int order, int nulls)
	{
		orderBy(getColumn(column),order,nulls);
	}
	

	public void orderBy(String table, String column, int order, int nulls)
	{
		orderBy(getColumn(table,column),order,nulls);
	}
	

	private void orderBy(Column column, int order, int nulls)
	{
		Boolean desc = null;
		switch(order)
		{
			case ORDER_ASC:
				desc = Boolean.FALSE;
			break;
			case ORDER_DESC:
				desc = Boolean.TRUE;
			break;
		}
		
		Boolean nullsFirst = null;
		switch(nulls)
		{
			case NULLS_FIRST:
				nullsFirst = Boolean.TRUE;
			break;
			case NULLS_LAST:
				nullsFirst = Boolean.FALSE;
			break;
		}
		
		select.ORDER_BY(column,desc,nullsFirst);
	}
	

	public void orderBy(Object item)
	{
		select.ORDER_BY(item);
	}
	

	public void offset(int offset)
	{
		select.OFFSET(offset);
	}
	

	public void limit(int limit)
	{
		select.FETCH_FIRST(limit);
	}
	

	public Condition parenthesis(Object condition)
	{
		return Condition.par(condition);
	}
	

	public Column getColumn(String column)
	{
		return new Column(column);
	}
	

	public Column getColumn(String table, String column)
	{
		return new Column(new Table(table),column);
	}
	

	public Table getTable(String name)
	{
		return new Table(name);
	}
	

	private Table getTableWithAlias(String table, boolean newAliasIfExists)
	{
		Table sqlTable = tableCache.get(table);
		if(sqlTable == null)
		{
			sqlTable = new Table(table);
			if(newAliasIfExists)
			{
				tableCache.put(table,sqlTable);
			}
		}
		else if(newAliasIfExists)
		{
			Integer nr = aliasCounter.get(table);
			if(nr == null)
			{
				nr = 2;
			}
			else
			{
				nr++;
			}
			sqlTable = sqlTable.AS(sqlTable.getTableName() + nr);
			aliasCounter.put(table,nr);
			tableCache.put(table,sqlTable);
		}
		return sqlTable;
	}
	

	public Object LIKE_(Object expression)
	{
		return String.valueOf(expression).concat("%");
	}
	

	public Object _LIKE(Object expression)
	{
		return "%".concat(String.valueOf(expression));
	}
	

	public Object _LIKE_(Object expression)
	{
		return "%".concat(String.valueOf(expression)).concat("%");
	}
	

	/**
	 * Returns the average value of a numeric column.
	 * 
	 * @param expression
	 *            the name of the column
	 */
	@SqlFunction
	public Expression AVG(@ColumnNamePossible Object expression)
	{
		return Functions.AVG(expression);
	}
	

	/**
	 * Returns the number of rows in a query.
	 * 
	 * @param expression
	 *            the expression to count the rows of, e.g. a column name
	 */
	@SqlFunction
	public Expression COUNT(@ColumnNamePossible Object expression)
	{
		return Functions.COUNT(expression);
	}
	

	/**
	 * Returns the number of rows in a query - COUNT(*).
	 */
	@SqlFunction
	public Expression COUNT_ALL()
	{
		return Functions.COUNT();
	}
	

	/**
	 * Returns the largest value of the selected column.
	 * 
	 * @param expression
	 *            the name of the column
	 */
	@SqlFunction
	public Expression MAX(@ColumnNamePossible Object expression)
	{
		return Functions.MAX(expression);
	}
	

	/**
	 * Returns the smallest value of the selected column.
	 * 
	 * @param expression
	 *            the name of the column
	 */
	@SqlFunction
	public Expression MIN(@ColumnNamePossible Object expression)
	{
		return Functions.MIN(expression);
	}
	

	/**
	 * Returns the total sum of a numeric column.
	 * 
	 * @param expression
	 *            the name of the column
	 */
	@SqlFunction
	public Expression SUM(@ColumnNamePossible Object expression)
	{
		return Functions.SUM(expression);
	}
	

	/**
	 * Returns the absolute value of a numeric column or expression.
	 * 
	 * @param expression
	 *            the name of the column or a numeric expression
	 */
	@SqlFunction
	public Expression ABS(@ColumnNamePossible Object expression)
	{
		return Functions.ABS(expression);
	}
	

	/**
	 * Converts an expression of one data type to another.
	 * 
	 * @param type
	 *            the type to convert to
	 * @param expression
	 *            the value to convert
	 */
	// @SqlFunction
	// public Expression CAST(Object type, Object expression)
	// {
	// return new SqlFunctionCAST(type,expression);
	// }
	
	/**
	 * Returns the first non-null expression among the arguments.
	 * 
	 * @param values
	 *            the table column names or value value expressions
	 */
	@SqlFunction
	public Expression COALESCE(@ColumnNamePossible Object... values)
	{
		return Functions.COALESCE(values);
	}
	

	/**
	 * Returns the remainder of the division from 2 integer values.
	 * 
	 * @param dividend
	 *            the dividend
	 * @param divisor
	 *            the divisor
	 */
	@SqlFunction
	public Expression MOD(@ColumnNamePossible Object dividend, @ColumnNamePossible Object divisor)
	{
		return Functions.MOD(dividend,divisor);
	}
	
	/**
	 * Rounds a numeric field to the number of decimals specified.
	 * <p>
	 * <strong> This method is not supported by some databases.</strong>
	 * 
	 * @param value
	 *            column name or numeric expression
	 * @param decimals
	 *            the number of decimals to round to
	 */
	@SqlFunction
	public Expression ROUND(@ColumnNamePossible Object value, int decimals)
	{
		return Functions.ROUND(value,decimals);
	}
	
	public void setVirtualTable(VirtualTable virtualTable)
	{
		this.virtualTable = virtualTable;
	}
	

	public void setFillMethod(VirtualTableFillMethod fillMethod)
	{
		this.fillMethod = fillMethod;
	}
	

	@Override
	public void execute() throws DBException, VirtualTableException
	{
		if(dataSource == null)
		{
			dataSource = DBUtils.getCurrentDataSource();
		}
		
		if(dataSource == null)
		{
			CommandException.throwMissingParameter("dataSource");
		}
		
		if(select == null)
		{
			CommandException.throwMissingParameter("select");
		}
		
		if(virtualTable == null)
		{
			CommandException.throwMissingParameter("virtualTable");
		}
		
		DBConnection connection = dataSource.openConnection();
		try
		{
			Result result = connection.query(select,this.parameters.toArray());
			try
			{
				int columnCount;
				if(targetColumns != null && (columnCount = targetColumns.size()) > 0)
				{
					// get actual columns if vt has been cloned
					VirtualTableColumn[] columns = targetColumns
							.toArray(new VirtualTableColumn[columnCount]);
					for(int i = 0; i < columnCount; i++)
					{
						if(columns[i] != null)
						{
							columns[i] = virtualTable.getColumn(columns[i].getName());
						}
					}
					
					virtualTable.addData(result,columns,fillMethod);
				}
				else
				{
					virtualTable.addData(result,fillMethod);
				}
			}
			finally
			{
				result.close();
			}
		}
		finally
		{
			connection.close();
		}
	}
	

	public VirtualTable getVirtualTable()
	{
		return virtualTable;
	}
	

	@Override
	public String toString()
	{
		return select.toString();
	}
}
