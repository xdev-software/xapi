
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

import java.util.List;

import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineMissingFromClauseException;
import com.xdev.jadoth.sqlengine.interfaces.SqlExecutor;
import com.xdev.jadoth.sqlengine.internal.ConditionClause;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.FROM;
import com.xdev.jadoth.sqlengine.internal.JoinClause;
import com.xdev.jadoth.sqlengine.internal.QueryListeners;
import com.xdev.jadoth.sqlengine.internal.SqlClause;
import com.xdev.jadoth.sqlengine.internal.WHERE;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.types.ConditionalTableQuery;
import com.xdev.jadoth.sqlengine.types.ConditionalWritingTableQuery;
import com.xdev.jadoth.sqlengine.types.Query;
import com.xdev.jadoth.sqlengine.types.TableQuery;
import com.xdev.jadoth.sqlengine.types.WritingTableQuery;



/**
 * The Class DELETE.
 *
 * @author Thomas Muenz
 */
public class DELETE extends TableQuery.Implementation implements ConditionalWritingTableQuery
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final long serialVersionUID = -4742313533552431L;



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	private final SuperWritingQuery superWritingQuery;
	private final SuperConditionalQuery superConditionalQuery;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new dELETE.
	 */
	public DELETE() 
	{
		super();
		this.superConditionalQuery = new SuperConditionalQuery();
		this.superWritingQuery = new SuperWritingQuery();
	}
	
	protected DELETE(final DELETE copySource)
	{
		super(copySource);
		this.superConditionalQuery = copySource.superConditionalQuery.copy();
		this.superWritingQuery = copySource.superWritingQuery.copy();
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * @return
	 * @see net.net.jadoth.sqlengine.interfaces.TableQuery#keyword()
	 */
	@Override
	public String keyword() {
		return SQL.LANG.DELETE;
	}


	@Override
	public DELETE setName(final String name)
	{
		super.setName(name);
		return this;
	}

	@Override
	public DELETE setComment(final String comment)
	{
		super.setComment(comment);
		return this;
	}
	
	/**
	 * @param databaseGateway
	 * @return
	 */
	@Override
	public DELETE setDatabaseGateway(final DatabaseGateway<?> databaseGateway)
	{
		super.setDatabaseGateway(databaseGateway);
		return this;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// SQL Syntax Methods //
	///////////////////////
	// (22.02.2010 TM)TODO: refactor to seperate FROM-Table and FROM-Clause.
	/**
	 * FROM.
	 *
	 * @param table the table
	 * @return the dELETE
	 */
	public DELETE FROM(final SqlTableIdentity table)
	{
		//if no fromTable (target table) is set, set it.
		if(this.superWritingQuery.getTable() == null) {
			this.superWritingQuery.setTable(table);
		}
		else if(this.superConditionalQuery.getFromClause() == null) {
			//if there is already a fromTable but no FROM clause, set this.
			this.superConditionalQuery.setFromClause(new FROM(table));
		}
		else {
			//if there is already a FROM clause and yet another FROM is called, set the fromTable
			this.superWritingQuery.setTable(table);
			this.superConditionalQuery.setFromClause(null);
		}
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
	
	@Override
	public SqlTableIdentity getTable()
	{
		return this.superWritingQuery.getTable();
	}

	@Override
	public DELETE copy()
	{
		return new DELETE(this);
	}
	/**
	 * @return
	 * @throws CloneNotSupportedException is never thrown at the moment
	 */
	@Override
	public DELETE clone() throws CloneNotSupportedException
	{
		return this.copy();
	}
	
	@Override
	public DELETE AND(final Object condition)
	{
		this.superConditionalQuery.AND(condition);
		return this;
	}

	@Override
	public DELETE FROM(final TableExpression table)
	{
		this.superConditionalQuery.FROM(table);
		return this;
	}

	@Override
	public DELETE INNER_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.INNER_JOIN(table, joinCondition);
		return this;
	}

	@Override
	public DELETE LEFT_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.LEFT_JOIN(table, joinCondition);
		return this;
	}

	@Override
	public DELETE OR(final Object condition)
	{
		this.superConditionalQuery.OR(condition);
		return this;
	}

	@Override
	public DELETE OUTER_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.OUTER_JOIN(table, joinCondition);
		return this;
	}

	@Override
	public DELETE RIGHT_JOIN(final TableExpression table, final Object joinCondition)
	{
		this.superConditionalQuery.RIGHT_JOIN(table, joinCondition);
		return this;
	}

	@Override
	public DELETE WHERE(final Object condition)
	{
		this.superConditionalQuery.WHERE(condition);
		return this;
	}

	@Override
	public DELETE addJoinClause(final JoinClause joinClause)
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
		return new SqlClause<?>[]{this.getFromClause(), this.getWhereClause()};
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
	public DELETE setFromClause(final FROM fromClause)
	{
		this.superConditionalQuery.setFromClause(fromClause);
		return this;
	}

	@Override
	public DELETE setWhereClause(final WHERE whereClause)
	{
		this.superConditionalQuery.setWhereClause(whereClause);
		return this;
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
		public SuperConditionalQuery copy()
		{
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
		@Override public SuperConditionalQuery setDatabaseGateway(DatabaseGateway<?> databaseGateway){
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
	
	protected class SuperWritingQuery extends WritingTableQuery.Implementation
	{
		protected SuperWritingQuery()
		{
			super();
		}
		
		protected SuperWritingQuery(final SuperWritingQuery copySource)
		{
			super(copySource);
		}
		
		@Override
		public SuperWritingQuery copy()
		{
			return new SuperWritingQuery(this);
		}

		@Override public String assemble(final Object... parameters){
			return null;
		}
		@Override public Integer execute(final Object... parameters){
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
		@Override protected void setTable(final SqlTableIdentity table){
			super.setTable(table);
		}
		@Override public TableQuery setComment(final String comment){
			return null;
		}
		@Override public TableQuery setName(final String name){
			return null;
		}		
		@Override public SuperWritingQuery setDatabaseGateway(DatabaseGateway<?> databaseGateway){
			return null;
		}
		@Override public boolean isDatabaseGatewaySet(){
			return false;
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
