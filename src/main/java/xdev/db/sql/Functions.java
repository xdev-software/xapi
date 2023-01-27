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
package xdev.db.sql;


import com.xdev.jadoth.sqlengine.internal.SqlAggregateAVG;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateCOUNT;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateMAX;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateMin;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateSUM;
import com.xdev.jadoth.sqlengine.internal.SqlFunctionABS;
import com.xdev.jadoth.sqlengine.internal.SqlFunctionCAST;
import com.xdev.jadoth.sqlengine.internal.SqlFunctionCOALESCE;
import com.xdev.jadoth.sqlengine.internal.SqlFunctionMOD;
import com.xdev.jadoth.sqlengine.internal.SqlFunctionROW_NUMBER;
import com.xdev.jadoth.sqlengine.internal.SqlFunctionSUBSTRING;
import com.xdev.jadoth.sqlengine.internal.SqlxFunctionCONCAT;
import com.xdev.jadoth.sqlengine.internal.SqlxFunctionROUND;
import com.xdev.jadoth.sqlengine.internal.SqlxFunctionTO_CHAR;
import com.xdev.jadoth.sqlengine.internal.SqlxFunctionTO_NUMBER;


public final class Functions
{
	private Functions()
	{
	}
	
	
	public static Expression AVG(Object value)
	{
		return new Expression(new SqlAggregateAVG(SqlObject.expose(value)));
	}
	
	
	public static Expression COUNT()
	{
		return new Expression(new SqlAggregateCOUNT());
	}
	
	
	public static Expression COUNT(Object value)
	{
		return new Expression(new SqlAggregateCOUNT(SqlObject.expose(value)));
	}
	
	
	public static Expression MAX(Object value)
	{
		return new Expression(new SqlAggregateMAX(SqlObject.expose(value)));
	}
	
	
	public static Expression MIN(Object value)
	{
		return new Expression(new SqlAggregateMin(SqlObject.expose(value)));
	}
	
	
	public static Expression SUM(Object value)
	{
		return new Expression(new SqlAggregateSUM(SqlObject.expose(value)));
	}
	
	
	public static Expression CAST(Object rhsExpression, Object lhsExpression)
	{
		return new Expression(new SqlFunctionCAST(lhsExpression,rhsExpression));
	}
	
	
	public static Expression ABS(Object value)
	{
		return new Expression(new SqlFunctionABS(SqlObject.expose(value)));
	}
	
	
	public static Expression COALESCE(Object... values)
	{
		return new Expression(new SqlFunctionCOALESCE(SqlObject.expose(values)));
	}
	
	
	public static Expression MOD(Object dividend, Object divisor)
	{
		return new Expression(new SqlFunctionMOD(SqlObject.expose(dividend),
				SqlObject.expose(divisor)));
	}
	
	
	public static Expression ROW_NUMBER()
	{
		return new Expression(new SqlFunctionROW_NUMBER());
	}
	
	
	public static Expression SUBSTRING(Object string, int start)
	{
		return new Expression(new SqlFunctionSUBSTRING(SqlObject.expose(string),start));
	}
	
	
	public static Expression SUBSTRING(Object string, int start, int length)
	{
		return new Expression(new SqlFunctionSUBSTRING(SqlObject.expose(string),start,length));
	}
	
	
	public static Expression CONCAT(Object left, Object right)
	{
		return new Expression(
				new SqlxFunctionCONCAT(SqlObject.expose(left),SqlObject.expose(right)));
	}
	
	
	public static Expression ROUND(Object value, int decimals)
	{
		return new Expression(new SqlxFunctionROUND(SqlObject.expose(value),decimals));
	}
	
	
	public static Expression TO_CHAR(Object expression, Object format)
	{
		return new Expression(new SqlxFunctionTO_CHAR(SqlObject.expose(expression),
				SqlObject.expose(format)));
	}
	
	
	public static Expression TO_NUMBER(Object expresion)
	{
		return new Expression(new SqlxFunctionTO_NUMBER(SqlObject.expose(expresion)));
	}
}
