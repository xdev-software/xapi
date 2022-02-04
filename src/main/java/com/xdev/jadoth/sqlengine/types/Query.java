package com.xdev.jadoth.sqlengine.types;

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

import static com.xdev.jadoth.lang.reflection.JaReflect.getMemberByLabel;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.xdev.jadoth.Jadoth;
import com.xdev.jadoth.lang.Copyable;
import com.xdev.jadoth.lang.reflection.annotations.Label;
import com.xdev.jadoth.lang.types.ToString;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.dbms.standard.StandardDbmsAdaptor;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineCouldNotConnectToDBException;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.interfaces.SqlExecutor;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.QueryListeners;
import com.xdev.jadoth.sqlengine.internal.QueryPart;
import com.xdev.jadoth.util.logging.jul.LoggingAspect;
import com.xdev.jadoth.util.logging.jul.LoggingContext;


public interface Query extends ToString, Copyable, Cloneable, AssembableParamterizedQuery<Object>
{	
	public Query singleLineMode();
	public Query pack();	
	public Query setSingleLineMode(boolean singleLineMode);
	public Query setPacked(boolean packed);	
	public boolean isSingleLineMode();	
	public boolean isPacked();
	
	public String getName();
	public String getComment();
	public String[] getCommentLines();
	
	public Query setName(String name);	
	public Query setComment(String comment);
		
	public String keyword();
	
	public DatabaseGateway<?> getDatabaseGatewayForExecution();
	public Query setDatabaseGateway(DatabaseGateway<?> databaseGateway);
	public boolean isDatabaseGatewaySet();
		
	public int prepare();	
	@Override
	public String assemble(final Object... parameters);
	public Object execute(final Object... parameters) throws SQLEngineException;
	
	/**
	 * @return the proper SQL query String for this <code>Query</code> object.
	 */
	@Override
	public String toString();
	
	
	
	public QueryListeners listeners();
	public QueryListeners getListeners();
	public Query setListeners(QueryListeners queryListeners);
	
	
	
	
	abstract class Implementation extends QueryPart implements Query
	{
		private static final long serialVersionUID = -2958062534103405684L;
			

		/** The Constant LABEL_assemble. */
		static final String LABEL_assemble = "assemble";

		/** The Constant LABEL_assembleQuery. */
		static final String LABEL_assembleQuery = "assembleQuery";

		/** The Constant LABEL_execute. */
		static final String LABEL_execute = "execute";
		
		//Refactoring-safe determination of method names
		/** The Constant METHOD_compose. */
		static final String METHOD_assemble = getMemberByLabel(
			LABEL_assemble, Query.Implementation.class.getMethods()
		).getName();

		/** The Constant METHOD_execute. */
		static final String METHOD_execute = getMemberByLabel(
			LABEL_execute, Query.Implementation.class.getMethods()
		).getName();
				
		
		
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		private DatabaseGateway<?> databaseGateway = null;


		//Settings
		/** The seperate clauses. */
		private boolean seperateClauses = true;

		/** The single line mode. */
		private boolean singleLineMode = false;

		/** The prepare token. */
		private String prepareToken = SQL.config.prepareParamRegExp;

		//Helper variables
		/** The prepared parts. */
		private String[] preparedParts = null;

		//Logging, disabled by default
		/** The log. */
		private LoggingAspect log = null;

		/** The level compose. */
//		private Level levelAssemble = null;

		/** The level execute. */
		private Level levelExecute = null;

		/** The name. */
		private String name = null;

		/** The comment lines. */
		private String[] commentLines = null;

		/** The comment. */
		private String comment = null;
		
		
		private QueryListeners queryListeners = null;
				

		/** The logging context execute. */
		protected final LoggingContext loggingContextExecute = new LoggingContext(){
			@Override
			public Level getLevel() {
				return levelExecute!=null ?levelExecute :SQL.config.getLoggingLevelExecute();
			}
			@Override
			public String getSourceMethodName() {
				return METHOD_execute;
			}
			@Override
			public LoggingAspect getLoggingAspect() {
				return log!=null ?log :SQL.config.getGlobalLoggingAspect();
			}
			@Override
			public Class<?> getSourceClass() {
				return null;
			}
		};


		/** The jdbc. */
		public final Jdbc jdbc = new Jdbc();
		
		
		protected Implementation()
		{
			super();
		}
		
		protected Implementation(final Implementation copySource)
		{
			super();
			//FIXME: Query.Implementation copy constructor
		}		
		

		@Override
		@Label(LABEL_execute)
		public abstract Object execute(Object... parameters) throws SQLEngineException;
		
			

		@Override
		public String getComment()
		{
			return this.comment;
		}

		@Override
		public String[] getCommentLines()
		{
			return this.commentLines;
		}

		@Override
		public DatabaseGateway<?> getDatabaseGatewayForExecution()
		{
			if(this.databaseGateway != null) return this.databaseGateway;
			return SQL.getDefaultDatabaseGateway();
		}
		
		public DatabaseGateway<?> getDatabaseGateway()
		{
			return this.databaseGateway;
		}
		
		/**
		 * @return
		 */
		@Override
		public boolean isDatabaseGatewaySet()
		{
			return this.databaseGateway != null;
		}

		@Override
		public String getName()
		{
			return this.name;
		}

		@Override
		public boolean isPacked()
		{
			return !this.seperateClauses;
		}

		@Override
		public boolean isSingleLineMode()
		{
			return singleLineMode;
		}
		
		
		
		
		@Override
		public Query setName(final String name)
		{
			this.name = name;
			return this;
		}

		@Override
		public Query setComment(final String comment)
		{
			this.comment = comment;
			this.commentLines = comment.split("\r*\n");
			return this;
		}

		@Override
		public Query pack()
		{
			this.seperateClauses = false;
			return this;
		}

		@Override
		public Query setPacked(final boolean packed)
		{
			this.seperateClauses = !packed;
			return this;
		}

		@Override
		public Query setSingleLineMode(final boolean singleLineMode)
		{
			this.singleLineMode = singleLineMode;
			return this;
		}

		@Override
		public Query singleLineMode()
		{
			this.singleLineMode = true;
			return this;
		}

		
		
		
		
		
		
		
		
		protected boolean isPrepared()
		{
			return this.preparedParts != null;
		}
		
		/**
		 * Prepare.
		 *
		 * @return the int
		 * @return
		 */
		public int prepare(){
			//in case a prepareToken is located at the end of the query, make sure there will be one last query part.
			final String queryString = this.toString() + _;
			this.preparedParts = queryString.split(this.prepareToken);
			return this.preparedParts.length-1;
		}
		
		@Label(LABEL_assemble)
		public String assemble(Object... parameters)
		{
			this.prepare();
			final String[] preparedParts = this.preparedParts;
			//preparedParts can never be length 0, only 1 or higher
			if(preparedParts.length == 1){
				//trivial case: no query assembly parameters present, so no parameter assembly required
				return preparedParts[0];
			}
			
			//parameter assembly: preappend first prepared part, then weave in parameters
			final StringBuilder sb = new StringBuilder(2048).append(preparedParts[0]);
			Object loopParameter;
			for(int i = 1; i < preparedParts.length; i++){
				
				//nullpointer, bounds etc. exceptions are provoked intentionally
				loopParameter = parameters[i-1];
				if(loopParameter.getClass().isArray()){
					Jadoth.appendArraySeperated(sb, ',', (Object[])loopParameter);
				}
				else if(loopParameter instanceof Iterable<?>){
					Jadoth.appendIterableSeperated(sb, ',', (Iterable<?>)loopParameter);
				}
				else {
					sb.append(loopParameter.toString());
				}
				sb.append(preparedParts[i]);
			}
			//superfluous parameters are unharmful and discarded
			
			return sb.toString();
		}
		
		protected <R> R execute(final SqlExecutor<R> queryExecutor, final Object... parameters)
		{
			final DatabaseGateway<?> dbc = this.getDatabaseGatewayForExecution();
			if(!dbc.isConnectionEnabled()) {
				throw new SQLEngineCouldNotConnectToDBException("DB not connected: "+dbc);
			}			
			return DatabaseGateway.execute(dbc, this, this.loggingContextExecute, queryExecutor, parameters);		
		}
				
		protected DbmsDMLAssembler<?> getDMLAssembler()
		{
			final DatabaseGateway<?> dbg = this.getDatabaseGatewayForExecution();
			if(dbg == null){
				return SQL.getDefaultDMLAssembler();
			}
			
			return dbg.getDbmsAdaptor().getDMLAssembler();
		}
	
		@Override
		public String toString()
		{
			return this.assemble(
				this.getDMLAssembler(),
				new StringBuilder(defaultQueryStringBuilderLength),
				0,
				bitSingleLine(this.singleLineMode) | bitPacked(!this.seperateClauses)
			).toString();
		}
		
		@Override
		@Label(LABEL_assembleQuery)
		protected StringBuilder assemble(
			DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
		)
		{			
			if(dmlAssembler == null){
				dmlAssembler = StandardDbmsAdaptor.getSingletonStandardDbmsAdaptor().getDMLAssembler();
				final DbmsAdaptor<?> staticDefaultDbms = SQL.getDefaultDBMS();
				if(staticDefaultDbms != null){
					dmlAssembler = staticDefaultDbms.getDMLAssembler();
				}
			}			
			
			final boolean reallySingleLine = this.isSingleLineMode() || isSingleLine(flags);
			final boolean reallyPacked = this.isPacked() || isPacked(flags);

			dmlAssembler.assembleQuery(this, sb, indentLevel, bitSingleLine(reallySingleLine)|bitPacked(reallyPacked));
			return sb;
		}
		
		/**
		 * Encapsulates JDBC method calls like <code>prepareStatement</code> for the instance of the enclosing Query class.
		 *
		 * @author Thomas Muenz
		 *
		 */
		public class Jdbc
		{
			/**
			 * Prepare statement.
			 *
			 * @return the prepared statement
			 * @throws SQLEngineException the sQL engine exception
			 */
			public PreparedStatement prepareStatement() throws SQLEngineException
			{
				try {
					return getDatabaseGatewayForExecution().getConnection().prepareStatement(Query.Implementation.this.toString());
				}
				catch(final SQLException e) {
					throw new SQLEngineException(e);
				}
			}

			/**
			 * Prepare statement.
			 *
			 * @param autoGeneratedKeys the auto generated keys
			 * @return the prepared statement
			 * @throws SQLEngineException the sQL engine exception
			 */
			public PreparedStatement prepareStatement(final int autoGeneratedKeys) throws SQLEngineException
			{
				try {
					return getDatabaseGatewayForExecution().getConnection().prepareStatement(Query.Implementation.this.toString(), autoGeneratedKeys);
				}
				catch(final SQLException e) {
					throw new SQLEngineException(e);
				}
			}

			/**
			 * Prepare statement.
			 *
			 * @param columnIndexes the column indexes
			 * @return the prepared statement
			 * @throws SQLEngineException the sQL engine exception
			 */
			public PreparedStatement prepareStatement(final int[] columnIndexes) throws SQLEngineException
			{
				try {
					return getDatabaseGatewayForExecution().getConnection().prepareStatement(Query.Implementation.this.toString(), columnIndexes);
				}
				catch(final SQLException e) {
					throw new SQLEngineException(e);
				}
			}

			/**
			 * Prepare statement.
			 *
			 * @param columnNames the column names
			 * @return the prepared statement
			 * @throws SQLEngineException the sQL engine exception
			 */
			public PreparedStatement prepareStatement(final String[] columnNames) throws SQLEngineException
			{
				try {
					return getDatabaseGatewayForExecution().getConnection().prepareStatement(Query.Implementation.this.toString(), columnNames);
				}
				catch(final SQLException e) {
					throw new SQLEngineException(e);
				}
			}

			/**
			 * Prepare statement.
			 *
			 * @param resultSetType the result set type
			 * @param resultSetConcurrency the result set concurrency
			 * @return the prepared statement
			 * @throws SQLEngineException the sQL engine exception
			 */
			public PreparedStatement prepareStatement(final int resultSetType, final int resultSetConcurrency) throws SQLEngineException
			{
				try {
					return getDatabaseGatewayForExecution().getConnection().prepareStatement(
						Query.Implementation.this.toString(), resultSetType, resultSetConcurrency
					);
				}
				catch(final SQLException e) {
					throw new SQLEngineException(e);
				}
			}

			/**
			 * Prepare statement.
			 *
			 * @param resultSetType the result set type
			 * @param resultSetConcurrency the result set concurrency
			 * @param resultSetHoldability the result set holdability
			 * @return the prepared statement
			 * @throws SQLEngineException the sQL engine exception
			 */
			public PreparedStatement prepareStatement(
				final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability
			)
				throws SQLEngineException
			{
				try {
					return getDatabaseGatewayForExecution().getConnection().prepareStatement(
						Query.Implementation.this.toString(), resultSetType, resultSetConcurrency, resultSetHoldability
					);
				}
				catch(final SQLException e) {
					throw new SQLEngineException(e);
				}
			}

		}

		public final Logging logging = new Logging();
		public class Logging
		{
//			public Logging setLevelAssemble(final Level levelAssemble)
//			{
//				Implementation.this.levelAssemble = levelAssemble;
//				return this;
//			}
			public Logging setLevelExecute(final Level levelExecute)
			{
				Implementation.this.levelExecute = levelExecute;
				return this;
			}
			public Logging setLogger(final Logger logger)
			{
				return setLogger(
					logger, 
					Implementation.this.levelExecute != null? Implementation.this.levelExecute :Level.CONFIG
				);
			}
			public Logging setLogger(final Logger logger, final Level level)
			{				
				Implementation.this.log = new LoggingAspect(logger, level);
				//override LoggingAspect defaultLevel feature when only providing plain jul logger 
				Implementation.this.levelExecute = level;
				return this;
			}
			public Logging setLogger(final LoggingAspect loggingAspect)
			{
				Implementation.this.log = loggingAspect;
				return this;
			}
			
			public Level getLevelExecute()
			{
				return Implementation.this.levelExecute;
			}
			
			public LoggingAspect getLoggingAspect()
			{
				return Implementation.this.log;
			}
			public Logger getLogger()
			{
				final LoggingAspect la = Implementation.this.log;
				return la == null ?null :la.getLogger();
			}
		}

		/**
		 * @param databaseGateway
		 * @return
		 */
		@Override
		public Query setDatabaseGateway(final DatabaseGateway<?> databaseGateway)
		{
			this.databaseGateway = databaseGateway;
			return this;
		}

		/**
		 * @return
		 */
		@Override
		public QueryListeners getListeners()
		{
			return this.queryListeners;
		}

		/**
		 * @return
		 */
		@Override
		public QueryListeners listeners()
		{
			if(this.queryListeners == null){
				this.queryListeners = new QueryListeners();
			}
			return this.queryListeners;
		}

		/**
		 * @param queryListeners
		 * @return
		 */
		@Override
		public Query setListeners(final QueryListeners queryListeners)
		{
			this.queryListeners = queryListeners;
			return this;
		}

	

		
		
	}
	
	
}
