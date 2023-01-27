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
package com.xdev.jadoth.sqlengine.types;

import static com.xdev.jadoth.sqlengine.internal.SqlClause.copySqlClause;

import java.util.HashMap;
import java.util.List;

import com.xdev.jadoth.sqlengine.exceptions.SQLEngineMissingFromClauseException;
import com.xdev.jadoth.sqlengine.internal.ConditionClause;
import com.xdev.jadoth.sqlengine.internal.FROM;
import com.xdev.jadoth.sqlengine.internal.INNER_JOIN;
import com.xdev.jadoth.sqlengine.internal.JoinClause;
import com.xdev.jadoth.sqlengine.internal.LEFT_JOIN;
import com.xdev.jadoth.sqlengine.internal.OUTER_JOIN;
import com.xdev.jadoth.sqlengine.internal.RIGHT_JOIN;
import com.xdev.jadoth.sqlengine.internal.SqlClause;
import com.xdev.jadoth.sqlengine.internal.WHERE;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;


public interface ConditionalTableQuery extends TableQuery
{
	public FROM getFromClause();
	public WHERE getWhereClause();
	public ConditionClause<?> getLastConditionClause();
	public ConditionalTableQuery setFromClause(FROM fromClause);
	public ConditionalTableQuery setWhereClause(WHERE whereClause);
	public SqlClause<?>[] getSqlClauses();
	public ConditionalTableQuery AND(Object condition);
	public ConditionalTableQuery OR(Object condition);
	public ConditionalTableQuery WHERE(Object condition);
	public ConditionalTableQuery FROM(TableExpression table);
	public ConditionalTableQuery addJoinClause(JoinClause joinClause);
	public ConditionalTableQuery INNER_JOIN(final TableExpression table, final Object joinCondition);
	public ConditionalTableQuery LEFT_JOIN(final TableExpression table, final Object joinCondition);
	public ConditionalTableQuery OUTER_JOIN(final TableExpression table, final Object joinCondition);
	public ConditionalTableQuery RIGHT_JOIN(final TableExpression table, final Object joinCondition);
	public int getJoinCount();
	public List<JoinClause> getJoins();
	public List<SqlTableIdentity> getClauseTables();
	public <C extends JoinClause> C registerJoinClause(final C join) throws SQLEngineMissingFromClauseException;
//	public ConditionalQuery setJoins(final List<JoinClause> joins) throws SQLEngineMissingFromClauseException;
	
	
	
	abstract class Implementation implements ConditionalTableQuery
	{
		/** The from clause. */
		private FROM fromClause;

		/** The where clause. */
		private WHERE whereClause;

		/** The last condition clause. */
		private ConditionClause<?> lastConditionClause;
		
		

		/**
		 * Instantiates a new abstract conditional query.
		 *
		 * @param clauses the clauses
		 */
		protected Implementation()
		{
			super();
			this.fromClause = null;
			this.whereClause = null;
			this.lastConditionClause = null;
		}
		
		protected Implementation(final ConditionalTableQuery.Implementation copySource)
		{
			super();
			final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied = new HashMap<SqlClause<?>, SqlClause<?>>();
			this.fromClause = copySqlClause(copySource.fromClause, alreadyCopied);
			this.whereClause = copySqlClause(copySource.whereClause, alreadyCopied);
			this.lastConditionClause = copySqlClause(copySource.lastConditionClause, alreadyCopied);
		}
		
			
		
		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////
		/**
		 * Gets the from clause.
		 *
		 * @return the fromClause
		 */
		public FROM getFromClause() 
		{
			return fromClause;
		}

		/**
		 * Gets the where clause.
		 *
		 * @return the whereClause
		 */
		public WHERE getWhereClause() 
		{
			return whereClause;
		}

		/**
		 * Gets the last condition clause.
		 *
		 * @return the lastConditionClause
		 */
		public ConditionClause<?> getLastConditionClause() 
		{
			return lastConditionClause;
		}



		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
		/**
		 * Sets the from clause.
		 *
		 * @param fromClause the fromClause to set
		 * @return the q
		 */
		public Implementation setFromClause(final FROM fromClause) 
		{
//			this.registerSqlClause(fromClause, this.)
			this.fromClause = fromClause;
			return this;
		}

		/**
		 * Sets the where clause.
		 *
		 * @param whereClause the whereClause to set
		 * @return the q
		 */
		public Implementation setWhereClause(final WHERE whereClause) 
		{
			this.setWhereClauseIntern(whereClause);
			this.lastConditionClause = whereClause;
			return this;
		}
		
		protected void setWhereClauseIntern(final WHERE whereClause)
		{
			this.whereClause = whereClause;
		}
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
		
		protected ConditionalTableQuery setLastConditionClause(final ConditionClause<?> lastConditionClause)
		{
			this.lastConditionClause = lastConditionClause;
			return this;
		}


		/**
		 * @param condition
		 * @return
		 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Conditional#AND(java.lang.Object)
		 */
		@Override
		public Implementation AND(final Object condition) 
		{
			if(this.lastConditionClause == null) {
				this.WHERE(condition);
			}
			else {
				this.lastConditionClause.AND(condition);
			}
			return this;
		}

		/**
		 * @param condition
		 * @return
		 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Conditional#OR(java.lang.Object)
		 */
		@Override
		public Implementation OR(final Object condition) 
		{
			if(this.lastConditionClause == null) {
				this.WHERE(condition);
			}
			else {
				this.lastConditionClause.OR(condition);
			}
			return this;
		}


		/**
		 * WHERE.
		 *
		 * @param condition the condition
		 * @return the q
		 */
		public Implementation WHERE(final Object condition) 
		{
			final WHERE newWhereClause = condition instanceof WHERE
				?(WHERE)condition
				:new WHERE(condition)
			;
			this.setWhereClause(newWhereClause);
			//set lastConditionClause here anyway
			this.lastConditionClause = newWhereClause;
			return this;
		}

		/**
		 * FROM.
		 *
		 * @param table the table
		 * @return the q
		 */
		public Implementation FROM(final TableExpression table) 
		{
			return this.setFromClause(new FROM(table));
		}


		/**
		 * Adds a JOIN Clause but does NOT update the last condition.<br>
		 * Behaves like a simple setter (oder "adder")
		 *
		 * @param join the join
		 * @return the q
		 */
		public Implementation addJoinClause(final JoinClause join) 
		{
			this.registerJoinClause(join);
			return this;
		}

		/**
		 * Adds a JOIN Clause and sets the last condition to the newly added join.<br>
		 *
		 * @param join the join
		 * @return the q
		 */
		protected Implementation addJoinClauseForSQL(final JoinClause join) 
		{
			this.lastConditionClause = this.registerJoinClause(join);
			return this;
		}

		/**
		 * Adds an INNER JOIN and sets the last condition to the newly added join.<br>
		 *
		 * @param table the table
		 * @param joinCondition the join condition
		 * @return the q
		 */
		@Override
		public Implementation INNER_JOIN(final TableExpression table, final Object joinCondition) 
		{
			return addJoinClauseForSQL(new INNER_JOIN(table, joinCondition));
		}

		/**
		 * Adds a LEFT JOIN and sets the last condition to the newly added join.<br>
		 *
		 * @param table the table
		 * @param joinCondition the join condition
		 * @return the q
		 */
		@Override
		public Implementation LEFT_JOIN(final TableExpression table, final Object joinCondition) 
		{
			return addJoinClauseForSQL(new LEFT_JOIN(table, joinCondition));
		}

		/**
		 * Adds an OUTER JOIN and sets the last condition to the newly added join.<br>
		 *
		 * @param table the table
		 * @param joinCondition the join condition
		 * @return the q
		 */
		@Override
		public Implementation OUTER_JOIN(final TableExpression table, final Object joinCondition) 
		{
			return addJoinClauseForSQL(new OUTER_JOIN(table, joinCondition));
		}

		/**
		 * Adds a RIGHT JOIN and sets the last condition to the newly added join.<br>
		 *
		 * @param table the table
		 * @param joinCondition the join condition
		 * @return the q
		 */
		@Override
		public Implementation RIGHT_JOIN(final TableExpression table, final Object joinCondition) 
		{
			return addJoinClauseForSQL(new RIGHT_JOIN(table, joinCondition));
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.internal.interfaces.JoinContainer#getJoinCount()
		 */
		@Override
		public int getJoinCount() 
		{
			return this.fromClause==null?0:this.fromClause.getJoinCount();
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.internal.interfaces.JoinContainer#getJoins()
		 */
		@Override
		public List<JoinClause> getJoins() 
		{
			return this.fromClause==null?null:this.fromClause.getJoins();
		}


		/**
		 * Returns all proper tables (no sub-SELECTs) used in FROM and JOIN clauses.<br>
		 * If no FROM clause has been set yet, <tt>null</tt> is returned.
		 *
		 * @return the clause tables
		 */
		public List<SqlTableIdentity> getClauseTables()
		{
			if(this.fromClause == null) return null;

			return this.fromClause.getClauseTables();
		}

		/**
		 * @param <C>
		 * @param join
		 * @return
		 * @throws SQLEngineMissingFromClauseException
		 * @see com.xdev.jadoth.sqlengine.internal.interfaces.JoinContainer#registerJoinClause(com.xdev.jadoth.sqlengine.internal.JoinClause)
		 */
		@Override
		public <C extends JoinClause> C registerJoinClause(final C join) throws SQLEngineMissingFromClauseException
		{
			if(this.fromClause == null){
				throw new SQLEngineMissingFromClauseException();
			}
			return this.fromClause.registerJoinClause(join);
		}

	}
	
}
