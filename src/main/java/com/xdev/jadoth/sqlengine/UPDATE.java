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

package com.xdev.jadoth.sqlengine;

import java.util.List;

import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineMissingFromClauseException;
import com.xdev.jadoth.sqlengine.interfaces.SqlExecutor;
import com.xdev.jadoth.sqlengine.internal.AssignmentList;
import com.xdev.jadoth.sqlengine.internal.ColumnValueAssignment;
import com.xdev.jadoth.sqlengine.internal.ColumnValueTuple;
import com.xdev.jadoth.sqlengine.internal.ConditionClause;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.FROM;
import com.xdev.jadoth.sqlengine.internal.JoinClause;
import com.xdev.jadoth.sqlengine.internal.QueryListeners;
import com.xdev.jadoth.sqlengine.internal.SET;
import com.xdev.jadoth.sqlengine.internal.SqlClause;
import com.xdev.jadoth.sqlengine.internal.SqlColumn;
import com.xdev.jadoth.sqlengine.internal.WHERE;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.types.ConditionalTableQuery;
import com.xdev.jadoth.sqlengine.types.ConditionalWritingTableQuery;
import com.xdev.jadoth.sqlengine.types.Query;
import com.xdev.jadoth.sqlengine.types.TableQuery;
import com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery;



/**
 * The Class UPDATE.
 *
 * @author Thomas Muenz
 */
public class UPDATE extends TableQuery.Implementation implements ConditionalWritingTableQuery, ValueAssigningTableQuery
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = 763162177748106385L;



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////	

	private final SuperValueAssigningQuery superValueAssigningQuery;
	private final SuperConditionalQuery superConditionalQuery;
	private final SET setClause; 

	

	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new UPDATE.
	 *
	 * @param table the table
	 */
	public UPDATE(final SqlTableIdentity table){
		super();
		this.superValueAssigningQuery = new SuperValueAssigningQuery(table);
		this.superConditionalQuery = new SuperConditionalQuery();
		this.setClause = new SET(this.superValueAssigningQuery.iterateAssignments());
	}
	/**
	 * 
	 * @param copySource
	 */
	private UPDATE(final UPDATE copySource)
	{
		super(copySource);
		this.superValueAssigningQuery = copySource.superValueAssigningQuery.copy();
		this.superConditionalQuery = copySource.superConditionalQuery.copy();
		this.setClause = copySource.setClause.copy();
	}
	
	

	/**
	 * Columns.
	 *
	 * @param columns the colum list
	 * @return the uPDATE
	 */
	public UPDATE columns(final Object... columns)
	{
		this.clearAssignments();
		return this.addColumns(columns);
	}
	/**
	 * Sets the.
	 *
	 * @param column the column
	 * @param value the value
	 * @return the uPDATE
	 */
	public UPDATE SET(final ColumnValueTuple... keyValueTuples)
	{
		this.superValueAssigningQuery.assign(keyValueTuples);
		return this;
	}

	/**
	 * Sets the.
	 *
	 * @param column the column
	 * @param value the value
	 * @return the uPDATE
	 */
	public UPDATE SET(final Object column, final Object value)
	{
		return this.assign(column, value);
	}
	
	
	@Override
	public UPDATE assign(final Object column, final Object value)
	{
		this.superValueAssigningQuery.assign(column, value);
		return this;
	}

	@Override
	public UPDATE assign(final ColumnValueTuple... keyValueTuples)
	{
		this.superValueAssigningQuery.assign(keyValueTuples);
		return this;
	}



	/**
	 * @return
	 * @see net.net.jadoth.sqlengine.interfaces.TableQuery#getTable()
	 */
	@Override
	public SqlTableIdentity getTable() {
		return this.superValueAssigningQuery.getTable();
	}


	public SET getSetClause()
	{
		return this.setClause;
	}


	/**
	 * @return
	 * @see net.net.jadoth.sqlengine.interfaces.TableQuery#keyword()
	 */
	@Override
	public String keyword() {
		return SQL.LANG.UPDATE;
	}


	
	private transient INSERT linkedINSERT = null;
	

	/**
	 * Creates a new <code>INSERT</code> object that uses the identical column-value assignment list as this 
	 * <code>UPDATE</code> object does. Changes to assignments of one of the <code>UPDATE</code> / <code>INSERT</code>
	 * objects will change those of the other one as well.
	 * 
	 * @return a assignment-list-linked <code>INSERT</code> object.
	 */
	public INSERT getLinkedINSERT()
	{
		if(this.linkedINSERT == null){
			this.linkedINSERT = new INSERT(this.superValueAssigningQuery.getAssignmentList()).INTO(this.getTable());
		}
		return this.linkedINSERT;
	}



	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#iterateAssignments()
	 */
	@Override
	public Iterable<ColumnValueAssignment> iterateAssignments()
	{
		return this.superValueAssigningQuery.iterateAssignments();
	}



	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#iterateColumns()
	 */
	@Override
	public Iterable<SqlColumn> iterateColumns()
	{
		return this.superValueAssigningQuery.iterateColumns();
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#getAssignments()
	 */
	@Override
	public ColumnValueAssignment[] getAssignments()
	{
		return this.superValueAssigningQuery.getAssignments();
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#getColumns()
	 */
	@Override
	public SqlColumn[] getColumns()
	{
		return this.superValueAssigningQuery.getColumns();
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#getValues()
	 */
	@Override
	public Object[] getValues()
	{
		return this.superValueAssigningQuery.getValues();
	}

	
	@Override
	public UPDATE setName(final String name)
	{
		super.setName(name);
		return this;
	}

	@Override
	public UPDATE setComment(final String comment)
	{
		super.setComment(comment);
		return this;
	}
	
	
	/**
	 * @param databaseGateway
	 * @return
	 */
	@Override
	public UPDATE setDatabaseGateway(final DatabaseGateway<?> databaseGateway)
	{
		super.setDatabaseGateway(databaseGateway);
		return this;
	}
	
	
	/**
	 * @param values
	 * @return
	 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#setValues(java.lang.Object[])
	 */
	@Override
	public UPDATE setValues(final Object... values)
	{
		this.superValueAssigningQuery.setValues(values);
		return this;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#iterateValues()
	 */
	@Override
	public Iterable<Object> iterateValues()
	{
		return this.superValueAssigningQuery.iterateValues();
	}

	/**
	 * @return
	 * @see net.jadoth.abstraction.interfaces.Copyable#copy()
	 */
	@Override
	public UPDATE copy()
	{
		return new UPDATE(this);
	}
	/**
	 * @return
	 * @throws CloneNotSupportedException is never thrown at the moment
	 */
	@Override
	public UPDATE clone() throws CloneNotSupportedException
	{
		return this.copy();
	}

	/**
	 * @param column
	 * @return
	 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#addColumn(java.lang.Object)
	 */
	@Override
	public UPDATE addColumn(final Object column)
	{
		return this.assign(column, null);
	}

	/**
	 * @param columns
	 * @return
	 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#addColumns(java.lang.Object[])
	 */
	@Override
	public UPDATE addColumns(final Object... columns)
	{
		final SuperValueAssigningQuery v = this.superValueAssigningQuery;
		for(final Object c : columns) {
			v.addColumn(c);
		}
		return this;
	}

	/**
	 * @param index
	 * @param value
	 * @return
	 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#setValue(int, java.lang.Object)
	 */
	@Override
	public UPDATE setValue(final int index, final Object value)
	{
		this.superValueAssigningQuery.setValue(index, value);
		return this;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#clearAssignments()
	 */
	@Override
	public UPDATE clearAssignments()
	{
		this.superValueAssigningQuery.clearAssignments();
		return this;
	}

	/**
	 * @param parameters
	 * @return
	 * @throws SQLEngineException
	 * @see net.net.jadoth.sqlengine.interfaces.TableQuery#execute(java.lang.Object[])
	 */
	@Override
	public Integer execute(final Object... parameters) throws SQLEngineException 
	{
		return this.execute(SqlExecutor.update, parameters);
	}
	/**
	 * @param condition
	 * @return
	 */
	@Override
	public UPDATE AND(final Object condition)
	{
		this.superConditionalQuery.AND(condition);
		return this;
	}
	/**
	 * @param table
	 * @return
	 */
	@Override
	public UPDATE FROM(final TableExpression table)
	{
		this.superConditionalQuery.FROM(table);
		return this;
	}
	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 */
	@Override
	public UPDATE INNER_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.INNER_JOIN(table, joinCondition);
		return this;
	}
	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 */
	@Override
	public UPDATE LEFT_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.LEFT_JOIN(table, joinCondition);
		return this;
	}
	/**
	 * @param condition
	 * @return
	 */
	@Override
	public UPDATE OR(final Object condition)
	{
		this.superConditionalQuery.OR(condition);
		return this;
	}
	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 */
	@Override
	public UPDATE OUTER_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.OUTER_JOIN(table, joinCondition);
		return this;
	}
	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 */
	@Override
	public UPDATE RIGHT_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.RIGHT_JOIN(table, joinCondition);
		return this;
	}
	/**
	 * @param condition
	 * @return
	 */
	@Override
	public UPDATE WHERE(final Object condition)
	{
		this.superConditionalQuery.WHERE(condition);
		return this;
	}
	/**
	 * @param joinClause
	 * @return
	 */
	@Override
	public UPDATE addJoinClause(final JoinClause joinClause)
	{
		this.superConditionalQuery.addJoinClause(joinClause);
		return this;
	}
	/**
	 * @return
	 */
	@Override
	public List<SqlTableIdentity> getClauseTables()
	{
		return this.superConditionalQuery.getClauseTables();
	}
	/**
	 * @return
	 */
	@Override
	public FROM getFromClause()
	{
		return this.superConditionalQuery.getFromClause();
	}
	/**
	 * @return
	 */
	@Override
	public int getJoinCount()
	{
		return this.superConditionalQuery.getJoinCount();
	}
	/**
	 * @return
	 */
	@Override
	public List<JoinClause> getJoins()
	{
		return this.superConditionalQuery.getJoins();
	}
	/**
	 * @return
	 */
	@Override
	public ConditionClause<?> getLastConditionClause()
	{
		return this.superConditionalQuery.getLastConditionClause();
	}
	/**
	 * @return
	 */
	@Override
	public SqlClause<?>[] getSqlClauses()
	{
		return new SqlClause<?>[]{this.getFromClause(), this.getWhereClause()};
	}
	/**
	 * @return
	 */
	@Override
	public WHERE getWhereClause()
	{
		return this.superConditionalQuery.getWhereClause();
	}
	/**
	 * @param <C>
	 * @param join
	 * @return
	 * @throws SQLEngineMissingFromClauseException
	 */
	@Override
	public <C extends JoinClause> C registerJoinClause(final C join) throws SQLEngineMissingFromClauseException
	{
		return this.superConditionalQuery.registerJoinClause(join);
	}
	/**
	 * @param fromClause
	 * @return
	 */
	@Override
	public UPDATE setFromClause(final FROM fromClause)
	{
		this.superConditionalQuery.setFromClause(fromClause);
		return this;
	}
	/**
	 * @param whereClause
	 * @return
	 */
	@Override
	public UPDATE setWhereClause(final WHERE whereClause)
	{
		this.superConditionalQuery.setWhereClause(whereClause);
		return this;
	}	

	@Override
	public int getColumnCount()
	{
		return this.superValueAssigningQuery.getColumnCount();
	}
		
	protected class SuperValueAssigningQuery extends ValueAssigningTableQuery.Implementation
	{	
		protected SuperValueAssigningQuery()
		{
			super();
		}
		protected SuperValueAssigningQuery(final SqlTableIdentity table)
		{
			super(table);
		}
		protected SuperValueAssigningQuery(final SuperValueAssigningQuery copySource)
		{
			super(copySource);
		}		
		
		@Override
		public String assemble(final Object... parameters){
			return null;
		}
		@Override public Integer execute(final Object... parameters) throws SQLEngineException {
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
		@Override public boolean isPacked(){
			return false;
		}
		@Override public boolean isSingleLineMode(){
			return false;
		}
		@Override public int prepare(){
			return 0;
		}
		@Override public SuperValueAssigningQuery copy(){
			return new SuperValueAssigningQuery(this);
		}
		@Override public TableQuery pack(){
			return null;
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
		@Override
		public TableQuery setComment(final String comment){
			return null;
		}
		@Override public TableQuery setName(final String name){
			return null;
		}		
		@Override public SuperValueAssigningQuery setDatabaseGateway(final DatabaseGateway<?> databaseGateway){
			return null;
		}
		@Override public boolean isDatabaseGatewaySet(){
			return false;
		}
		
		
		@Override
		protected AssignmentList getAssignmentList()
		{
			return super.getAssignmentList();
		}
		@Override public QueryListeners getListeners(){
			return null;
		}
		@Override public QueryListeners listeners(){
			return null;
		}
		@Override public Query setListeners(QueryListeners queryListeners){
			return null;
		}
		
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
		
		@Override public String assemble(final Object... parameters){
			return null;
		}
		@Override public Object execute(final Object... parameters) throws SQLEngineException{
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
		@Override public int prepare(){
			return 0;
		}
		@Override public TableQuery pack(){
			return null;
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
		@Override public Query setListeners(QueryListeners queryListeners){
			return null;
		}
		
	}
	
}
