
package com.xdev.jadoth.sqlengine;

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

import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;

import java.sql.ResultSet;
import java.util.List;

import com.xdev.jadoth.collections.GrowList;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineMissingFromClauseException;
import com.xdev.jadoth.sqlengine.interfaces.SqlExecutor;
import com.xdev.jadoth.sqlengine.internal.ConditionClause;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.FROM;
import com.xdev.jadoth.sqlengine.internal.GROUP_BY;
import com.xdev.jadoth.sqlengine.internal.HAVING;
import com.xdev.jadoth.sqlengine.internal.JoinClause;
import com.xdev.jadoth.sqlengine.internal.ORDER_BY;
import com.xdev.jadoth.sqlengine.internal.QueryListeners;
import com.xdev.jadoth.sqlengine.internal.SqlClause;
import com.xdev.jadoth.sqlengine.internal.SqlExpression;
import com.xdev.jadoth.sqlengine.internal.WHERE;
import com.xdev.jadoth.sqlengine.internal.interfaces.Conditional;
import com.xdev.jadoth.sqlengine.internal.interfaces.SelectItem;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.types.ConditionalTableQuery;
import com.xdev.jadoth.sqlengine.types.ConditionalTableReadingTableQuery;
import com.xdev.jadoth.sqlengine.types.Query;
import com.xdev.jadoth.sqlengine.types.TableQuery;



/**
 * The Class SELECT.
 *
 * @author Thomas Muenz
 */
public class SELECT extends TableQuery.Implementation
	implements ConditionalTableReadingTableQuery, TableExpression, SelectItem, Conditional<SELECT>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final long serialVersionUID = 3276555717823103305L;



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	/** The distinct. */
	private boolean distinct;

	/** The limit row skip. */
	private Integer limitRowSkip;

	/** The limit row range. */
	private Integer limitRowRange;

	/** The select items. */
	private final GrowList<SelectItem> selectItems;

	/** The group by clause. */
	private GROUP_BY groupByClause;

	/** The having clause. */
	private HAVING havingClause;

	/** The order by clause. */
	private ORDER_BY orderByClause;

	/** The union select. */
	private SELECT unionSelect;

	/** The union all select. */
	private SELECT unionAllSelect;

	/** The intersect select. */
	private SELECT intersectSelect;

	/** The except select. */
	private SELECT exceptSelect;

	/** The alias. */
	private String alias; //Needed in case of use as a table expression



	private final SuperConditionalQuery superConditionalQuery;

	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Instantiates a new sELECT.
	 */
	public SELECT()
	{
		super();
		this.superConditionalQuery = new SuperConditionalQuery();
		this.limitRowSkip = null;
		this.limitRowRange = null;
		this.distinct = false;
		this.selectItems = new GrowList<SelectItem>();
		this.groupByClause = null;
		this.havingClause = null;
		this.orderByClause = null;
		this.unionSelect = null;
		this.unionAllSelect = null;
		this.intersectSelect = null;
		this.exceptSelect = null;
		this.alias = null;
	}

	public SELECT(final SELECT copySource)
	{
		super(copySource);
		this.superConditionalQuery = copySource.superConditionalQuery.copy();
		this.limitRowSkip = copySource.limitRowSkip;
		this.limitRowRange = copySource.limitRowRange;
		this.distinct = copySource.distinct;
		this.selectItems = copySource.selectItems != null ? 
			new GrowList<SelectItem>(copySource.selectItems) : 
			new GrowList<SelectItem>();
		if(copySource.groupByClause != null)
			this.groupByClause = copySource.groupByClause.copy();
		if(copySource.havingClause != null)
			this.havingClause = copySource.havingClause.copy();
		if(copySource.orderByClause != null)
			this.orderByClause = copySource.orderByClause.copy();
		this.unionSelect = copySource.unionSelect;         //do not copy other query objects
		this.unionAllSelect = copySource.unionAllSelect;   //do not copy other query objects
		this.intersectSelect = copySource.intersectSelect; //do not copy other query objects
		this.exceptSelect = copySource.exceptSelect;       //do not copy other query objects
		this.alias = copySource.alias; //Strings are immutable
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * Checks if is distinct.
	 *
	 * @return the distinct
	 */
	public boolean isDistinct()
	{
		return this.distinct;
	}

	/**
	 * Gets the union select.
	 *
	 * @return the unionSelect
	 */
	public SELECT getUnionSelect()
	{
		return this.unionSelect;
	}

	/**
	 * Gets the union all select.
	 *
	 * @return the unionAllSelect
	 */
	public SELECT getUnionAllSelect()
	{
		return this.unionAllSelect;
	}

	/**
	 * Gets the intersect select.
	 *
	 * @return the intersectSelect
	 */
	public SELECT getIntersectSelect()
	{
		return this.intersectSelect;
	}

	/**
	 * Gets the except select.
	 *
	 * @return the exceptSelect
	 */
	public SELECT getExceptSelect()
	{
		return this.exceptSelect;
	}

	/**
	 * Gets the select items.
	 *
	 * @return the selectItems
	 */
	public List<SelectItem> getSelectItems()
	{
		return this.selectItems;
	}

	/**
	 * Gets the group by clause.
	 *
	 * @return the groupByClause
	 */
	public GROUP_BY getGroupByClause()
	{
		return this.groupByClause;
	}

	/**
	 * Gets the having clause.
	 *
	 * @return the havingClause
	 */
	public HAVING getHavingClause()
	{
		return this.havingClause;
	}

	/**
	 * Gets the order by clause.
	 *
	 * @return the orderByClause
	 */
	public ORDER_BY getOrderByClause()
	{
		return this.orderByClause;
	}

	/**
	 * Gets the offset skip count.
	 *
	 * @return the offset skip count
	 */
	public Integer getOffsetSkipCount()
	{
		return this.limitRowSkip;
	}

	/**
	 * Gets the fetch first row count.
	 *
	 * @return the fetch first row count
	 */
	public Integer getFetchFirstRowCount()
	{
		return this.limitRowRange;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////

	/**
	 * Sets the distinct.
	 *
	 * @param distinct the distinct to set
	 * @return the sELECT
	 */
	public SELECT setDistinct(final boolean distinct)
	{
		this.distinct = distinct;
		return this;
	}


	/**
	 * Sets the from clause.
	 *
	 * @param fromClause the from clause
	 * @return the sELECT
	 */
	@Override
	public SELECT setFromClause(final FROM fromClause)
	{
		this.superConditionalQuery.setFromClause(fromClause);
		return this;
	}


	/**
	 * @param databaseGateway
	 * @return
	 */
	@Override
	public SELECT setDatabaseGateway(final DatabaseGateway<?> databaseGateway)
	{
		super.setDatabaseGateway(databaseGateway);
		return this;
	}

	/**
	 * @param whereClause
	 * @return
	 */
	@Override
	public SELECT setWhereClause(final WHERE whereClause)
	{
		this.superConditionalQuery.setWhereClause(whereClause);
		return this;
	}

	/**
	 * Sets the group by clause.
	 *
	 * @param groupByClause the groupByClause to set
	 * @return the sELECT
	 */
	public SELECT setGroupByClause(final GROUP_BY groupByClause)
	{
		this.groupByClause = groupByClause;
		return this;
	}

	/**
	 * Sets the having clause.
	 *
	 * @param havingClause the havingClause to set
	 * @return the sELECT
	 */
	public SELECT setHavingClause(final HAVING havingClause)
	{
		this.havingClause = havingClause;
		final SuperConditionalQuery superConditionalQuery = this.superConditionalQuery;
		final ConditionClause<?> lastConditionClause = superConditionalQuery.getLastConditionClause();
		if(lastConditionClause == null || lastConditionClause instanceof HAVING){
			superConditionalQuery.setLastConditionClause(lastConditionClause);
		}
		return this;
	}

	/**
	 * Sets the order by clause.
	 *
	 * @param orderByClause the orderByClause to set
	 * @return the sELECT
	 */
	public SELECT setOrderByClause(final ORDER_BY orderByClause)
	{
		this.orderByClause = orderByClause;
		return this;
	}

	/**
	 * Sets the selects.
	 *
	 * @param union the union
	 * @param unionAll the union all
	 * @param intersect the intersect
	 * @param except the except
	 */
	private void setSelects(final SELECT union, final SELECT unionAll, final SELECT intersect, final SELECT except)
	{
		this.unionSelect = union;
		this.unionAllSelect = unionAll;
		this.intersectSelect = intersect;
		this.exceptSelect = except;
	}

	/**
	 * Sets the union select.
	 *
	 * @param unionSelect the unionSelect to set
	 * @return the sELECT
	 */
	public SELECT setUnionSelect(final SELECT unionSelect)
	{
		this.setSelects(unionSelect, null, null, null);
		return this;
	}

	/**
	 * Sets the union all select.
	 *
	 * @param unionAllSelect the unionAllSelect to set
	 * @return the sELECT
	 */
	public SELECT setUnionAllSelect(final SELECT unionAllSelect)
	{
		this.setSelects(null, unionAllSelect, null, null);
		return this;
	}

	/**
	 * Sets the intersect select.
	 *
	 * @param intersectSelect the intersectSelect to set
	 * @return the sELECT
	 */
	public SELECT setIntersectSelect(final SELECT intersectSelect)
	{
		this.setSelects(null, null, intersectSelect, null);
		return this;
	}

	/**
	 * Sets the except select.
	 *
	 * @param exceptSelect the exceptSelect to set
	 * @return the sELECT
	 */
	public SELECT setExceptSelect(final SELECT exceptSelect)
	{
		this.setSelects(null, null, null, exceptSelect);
		return this;
	}

	/**
	 * Sets the alias.
	 *
	 * @param alias the alias to set
	 * @return the sELECT
	 */
	public SELECT setAlias(final String alias) {
		this.alias = alias;
		return this;
	}

	@Override
	public SELECT setName(final String name)
	{
		super.setName(name);
		return this;
	}

	@Override
	public SELECT setComment(final String comment)
	{
		super.setComment(comment);
		return this;
	}


	///////////////////////////////////////////////////////////////////////////
	// SQL Syntax Methods //
	///////////////////////

	/**
	 * DISTINCT.
	 *
	 * @return the sELECT
	 */
	public SELECT DISTINCT()
	{
		this.distinct = true;
		return this;
	}

	/**
	 * Columns.
	 *
	 * @param columns the columns
	 * @return the sELECT
	 */
	public SELECT columns(final Object... columns)
	{
		return this.items(columns);
	}

	/**
	 * Items.
	 *
	 * @param items the items
	 * @return the sELECT
	 */
	public SELECT items(final Object... items)
	{
		for(final Object o : items){
			if(o instanceof SelectItem){
				this.selectItems.add((SelectItem)o);
			}
			else {
				this.selectItems.add(new SqlExpression(o));
			}
		}
		return this;
	}

	/**
	 * Clear select items.
	 */
	public void clearSelectItems()
	{
		this.selectItems.clear();
	}

	/**
	 * Creates a new GROUP BY clause for this SELECT object using the field list
	 * provided by <code>orderByFields</code>.<br>
	 * See {@link SELECT#setGroupByClause(GROUP_BY)} for setting an GROUP_BY clause object directly.	 *
	 *
	 * @param groupByFields the fields to use in the GROUP BY clause of this SELECT object.
	 * @return this SELECT object
	 * @see SELECT#setGroupByClause(GROUP_BY)
	 */
	public SELECT GROUP_BY(final Object... groupByFields)
	{
		if(groupByFields.length == 1){
			final Object firstElement = groupByFields[0];
			if(firstElement instanceof GROUP_BY){
				return this.setGroupByClause((GROUP_BY)firstElement);
			}
		}
		return this.setGroupByClause(new GROUP_BY(groupByFields));
	}

	/**
	 * HAVING.
	 *
	 * @param havingCondition the having condition
	 * @return the sELECT
	 */
	public SELECT HAVING(final Object havingCondition)
	{
		final HAVING newHavingClause = havingCondition instanceof HAVING
			?(HAVING)havingCondition
			:new HAVING(havingCondition)
		;
		this.setHavingClause(newHavingClause);
		//set lastConditionClause here anyway
		this.superConditionalQuery.setLastConditionClause(newHavingClause);
		return this;
	}

	/**
	 * Creates a new ORDER BY clause for this SELECT object using the field list
	 * provided by <code>orderByFields</code>.<br>
	 * See {@link SELECT#setOrderByClause(ORDER_BY)} for setting an ORDER_BY clause object directly.	 *
	 *
	 * @param orderByFields the fields to use in the ORDER BY clause of this SELECT object.
	 * @return this SELECT object
	 * @see SELECT#setOrderByClause(ORDER_BY)
	 */
	public SELECT ORDER_BY(final Object... orderByFields)
	{
		return this.setOrderByClause(new ORDER_BY(orderByFields));
	}

	/**
	 * See SQL:2008 non-core feature IDs F856, F857, F858, and F859.
	 *
	 * @param n the n
	 * @return the sELECT
	 * @return
	 */
	//http://troels.arvin.dk/db/rdbms/#select
	public SELECT FETCH_FIRST(final Integer n)
	{
		if(n == null || n < 0){
			this.limitRowRange = null;
		}
		else {
			this.limitRowRange = n;
		}
		return this;
	}

	/**
	 * OFFSET.
	 *
	 * @param offsetRowCount the skip
	 * @return the sELECT
	 */
	public SELECT OFFSET(final Integer offsetRowCount)
	{
		if(offsetRowCount == null || offsetRowCount < 0) {
			this.limitRowSkip = null;
		}
		else {
			this.limitRowSkip = offsetRowCount;
		}
		return this;
	}

	/**
	 * UNION.
	 *
	 * @param unionSelect the union select
	 * @return the sELECT
	 */
	public SELECT UNION(final SELECT unionSelect)
	{
		return this.setUnionSelect(unionSelect);
	}

	/**
	 * UNIO n_ all.
	 *
	 * @param unionAllSelect the union all select
	 * @return the sELECT
	 */
	public SELECT UNION_All(final SELECT unionAllSelect)
	{
		return this.setUnionAllSelect(unionAllSelect);
	}

	/**
	 * INTERSECT.
	 *
	 * @param intersectSelect the intersect select
	 * @return the sELECT
	 */
	public SELECT INTERSECT(final SELECT intersectSelect)
	{
		return this.setIntersectSelect(intersectSelect);
	}

	/**
	 * EXCEPT.
	 *
	 * @param exceptSelect the except select
	 * @return the sELECT
	 */
	public SELECT EXCEPT(final SELECT exceptSelect)
	{
		return this.setExceptSelect(exceptSelect);
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public SELECT pack()
	{
		super.pack();
		return this;
	}

	@Override
	public SELECT setPacked(final boolean packed)
	{
		super.setPacked(packed);
		return this;
	}

	@Override
	public SELECT setSingleLineMode(final boolean singleLineMode)
	{
		super.setSingleLineMode(singleLineMode);
		return this;
	}

	@Override
	public SELECT singleLineMode()
	{
		super.singleLineMode();
		return this;
	}

	/**
	 * @return
	 * @see net.net.jadoth.sqlengine.interfaces.TableQuery#keyword()
	 */
	@Override
	public String keyword()
	{
		return SQL.LANG.SELECT;
	}

	// (11.12.2009 TM)TODO: Alias Wrapperclass, ggf. SELECT dadurch zu interface machen ^^
	/**
	 * @param newAlias
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression#AS(java.lang.String)
	 */
	@Override
	public SELECT AS(final String newAlias)
	{
		this.alias = newAlias;
		return this;
	}


	/**
	 * @param parameters
	 * @return
	 * @throws SQLEngineException
	 * @see net.net.jadoth.sqlengine.interfaces.TableQuery#execute(java.lang.Object[])
	 */
	@Override
	public ResultSet execute(final Object... parameters) throws SQLEngineException
	{
		return super.execute(SqlExecutor.query, parameters);
	}


	/**
	 * @return
	 * @see net.net.jadoth.sqlengine.interfaces.TableQuery#getTable()
	 */
	@Override
	public TableExpression getTable()
	{
		final FROM fromClause = this.superConditionalQuery.getFromClause();
		return fromClause == null ?null :fromClause.getTable();
	}

	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see net.net.jadoth.sqlengine.interfaces.TableQuery#assemble(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(
		final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		final boolean asExpression = isAsExpression(flags);
		final boolean reallySingleLine = isSingleLine(flags) || this.isSingleLineMode();
		final boolean reallyPacked = isPacked(flags) || this.isPacked();
		final String subSelectNewLine = reallySingleLine?"":NEW_LINE;
		final int actualIndentLevel = asExpression ?indentLevel+1 :indentLevel;

		//reset flags for subSelect
		final int passFlags = flags
			| bitSingleLine(reallySingleLine)
			| bitPacked(reallyPacked)
			&~UNQUALIFIED
			&~OMITALIAS
			&~MINIINDENT
			&~ASEXPRESSION
		;

		if(asExpression){
			sb.append('(').append(subSelectNewLine);
		}
		super.assemble(dmlAssembler, sb, actualIndentLevel, passFlags);

		if(asExpression){
			sb.append(subSelectNewLine);
			indent(sb, indentLevel, reallySingleLine);
			if(!reallySingleLine && isMiniIndent(flags)){
				sb.append(' ').append(' '); //2x append(char) is faster than 1x append(String)
			}
			sb.append(')');
			if(this.alias != null){
				sb.append(" AS ").append(this.alias);
			}
		}

		return sb;
	}



	@Override
	public SELECT AND(final Object condition)
	{
		this.superConditionalQuery.AND(condition);
		return this;
	}

	@Override
	public SELECT FROM(final TableExpression table)
	{
		this.superConditionalQuery.FROM(table);
		return this;
	}

	@Override
	public SELECT INNER_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.INNER_JOIN(table, joinCondition);
		return this;
	}

	@Override
	public SELECT LEFT_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.LEFT_JOIN(table, joinCondition);
		return this;
	}

	@Override
	public SELECT OR(final Object condition)
	{
		this.superConditionalQuery.OR(condition);
		return this;
	}

	@Override
	public SELECT OUTER_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.OUTER_JOIN(table, joinCondition);
		return this;
	}

	@Override
	public SELECT RIGHT_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.RIGHT_JOIN(table, joinCondition);
		return this;
	}

	@Override
	public SELECT WHERE(final Object condition)
	{
		this.superConditionalQuery.WHERE(condition);
		return this;
	}

	@Override
	public SELECT addJoinClause(final JoinClause joinClause)
	{
		this.superConditionalQuery.addJoinClause(joinClause);
		return this;
	}

	@Override
	public List<SqlTableIdentity> getClauseTables()
	{
		return this.superConditionalQuery.getClauseTables();
	}

	@Override
	public FROM getFromClause()
	{
		return this.superConditionalQuery.getFromClause();
	}

	@Override
	public int getJoinCount()
	{
		return this.superConditionalQuery.getJoinCount();
	}

	@Override
	public List<JoinClause> getJoins()
	{
		return this.superConditionalQuery.getJoins();
	}

	@Override
	public ConditionClause<?> getLastConditionClause()
	{
		return this.superConditionalQuery.getLastConditionClause();
	}

	@Override
	public SqlClause<?>[] getSqlClauses()
	{
		return new SqlClause<?>[]{
			this.getFromClause(),
			this.getWhereClause(),
			this.getGroupByClause(),
			this.getHavingClause(),
			this.getOrderByClause()
		};
	}

	@Override
	public WHERE getWhereClause()
	{
		return this.superConditionalQuery.getWhereClause();
	}

	@Override
	public <C extends JoinClause> C registerJoinClause(final C join) throws SQLEngineMissingFromClauseException
	{
		return this.superConditionalQuery.registerJoinClause(join);
	}

	@Override
	public SELECT copy()
	{
		return new SELECT(this);
	}
	/**
	 * @return
	 * @throws CloneNotSupportedException is never thrown at the moment
	 */
	@Override
	public SELECT clone() throws CloneNotSupportedException
	{
		return this.copy();
	}


	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/**
	 * Gets the alias.
	 *
	 * @return the alias
	 */
	public String getAlias()
	{
		if(this.alias != null) {
			return this.alias;
		}
		return this.superConditionalQuery.getFromClause().getAlias();
	}


	/**
	 * Execute single.
	 *
	 * @param parameters the parameters
	 * @return the object
	 * @throws SQLEngineException the sQL engine exception
	 */
	public Object executeSingle(final Object... parameters) throws SQLEngineException
	{
		return super.execute(SqlExecutor.singleResultQuery, parameters);
	}

	/**
	 * To sql expression.
	 *
	 * @return the sql expression
	 */
	public SqlExpression toSqlExpression()
	{
		// (07.09.2010 TM)FIXME: subSelect transformation / flagging / whatever
		/*
		 * Or is this done in dmlAssembler.assembleExpression() ?
		 * Also see TO DO note in class SelectItem
		 */
		return new SqlExpression(this);
	}


	protected class SuperConditionalQuery extends ConditionalTableQuery.Implementation
	{
		protected SuperConditionalQuery()
		{
			super();
		}
		protected SuperConditionalQuery(final SuperConditionalQuery copySource)
		{
			super(copySource);
		}


		@Override
		public SuperConditionalQuery copy(){
			return new SuperConditionalQuery(this);
		}

		@Override
		public Implementation setFromClause(final FROM fromClause)
		{
			super.setFromClause(fromClause);
			return this;
		}


		@Override
		public Implementation setWhereClause(final WHERE whereClause)
		{
			final ConditionClause<?> lastConditionClause = this.getLastConditionClause();
			if(lastConditionClause == null || lastConditionClause instanceof WHERE){
				super.setWhereClause(whereClause);
			}
			else {
				this.setWhereClauseIntern(whereClause);
			}
			return this;
		}

		@Override
		public String assemble(final Object... parameters){
			return null;
		}
		@Override
		public Object execute(final Object... parameters) throws SQLEngineException{
			return null;
		}
		@Override public String getComment(){
			return null;
		}
		@Override public String[] getCommentLines(){
			return null;
		}
		@Override public DatabaseGateway<?> getDatabaseGatewayForExecution(){
			return null;
		}
		@Override
		public String keyword(){
			return null;
		}
		@Override public String getName(){
			return null;
		}
		@Override public TableExpression getTable(){
			return null;
		}
		@Override public boolean isPacked(){
			return false;
		}
		@Override public boolean isSingleLineMode(){
			return false;
		}
		@Override public TableQuery pack(){
			return null;
		}
		@Override public int prepare(){
			return 0;
		}
		@Override public TableQuery setPacked(final boolean packed){
			return null;
		}
		@Override public TableQuery setSingleLineMode(final boolean singleLineMode){
			return null;
		}
		@Override public TableQuery singleLineMode(){
			return null;
		}

		@Override public TableQuery setComment(final String comment){
			return null;
		}
		@Override public TableQuery setName(final String name){
			return null;
		}

		@Override
		protected ConditionalTableQuery setLastConditionClause(final ConditionClause<?> lastConditionClause)
		{
			return super.setLastConditionClause(lastConditionClause);
		}

		@Override public SuperConditionalQuery setDatabaseGateway(final DatabaseGateway<?> databaseGateway){
			return null;
		}
		@Override public boolean isDatabaseGatewaySet(){
			return false;
		}
		@Override public SqlClause<?>[] getSqlClauses(){
			return null;
		}
		@Override public QueryListeners getListeners(){
			return null;
		}
		@Override public QueryListeners listeners(){
			return null;
		}
		@Override public Query setListeners(final QueryListeners queryListeners){
			return null;
		}
	}

	/**
	 * Sets this SELECT's new selectItems list to the elements of <code>selectItems</code>
	 *
	 * @param selectItems the selectItems to set
	 */
	public void setSelectItems(final List<SelectItem> selectItems)
	{
		if(selectItems == null){
			throw new NullPointerException("selectItems may not be null");
		}
		this.selectItems.clear();
		this.selectItems.addAll(selectItems);
	}

	/**
	 * Adds <code>selectItems</code> via the internal selectitems' implementation of
	 * <code>java.util.List.addAll(Collection)</code>
	 *
	 * @param selectItems the selectItems to set
	 */
	public void addSelectItems(final List<SelectItem> selectItems)
	{
		this.selectItems.addAll(selectItems);
	}

	/**
	 * @param selectItems the selectItems to set
	 */
	public void addSelectItem(final SelectItem selectItem)
	{
		this.selectItems.add(selectItem);
	}

}
