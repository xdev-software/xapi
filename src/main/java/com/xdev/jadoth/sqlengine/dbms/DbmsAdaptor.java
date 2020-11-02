
package com.xdev.jadoth.sqlengine.dbms;

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


import static com.xdev.jadoth.sqlengine.SQL.LANG.LIKE;
import static com.xdev.jadoth.sqlengine.SQL.LANG.NOT;
import static com.xdev.jadoth.sqlengine.SQL.LANG._OR_;
import static com.xdev.jadoth.sqlengine.SQL.LANG.__AND;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;

import java.sql.Connection;
import java.sql.SQLException;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.interfaces.SqlExecutor;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTable;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;



/**
 * The Interface DbmsAdaptor.
 * 
 * @param <A> the generic type
 * @author Thomas Muenz
 */
public interface DbmsAdaptor<A extends DbmsAdaptor<A>>
{
	// connection logics //

	/**
	 * Creates the connection information.
	 * 
	 * @param host the host
	 * @param port the port
	 * @param user the user
	 * @param password the password
	 * @param catalog the catalog
	 * @param properties the extended url properties
	 * @return the dbms connection information
	 */
	public DbmsConnectionInformation<A> createConnectionInformation(
		String host, int port, String user,	String password, String catalog, String properties
	);

	// (20.02.2010)TODO move to ConnectionInformation (?)
	/**
	 * Gets the jdbc connection.
	 * 
	 * @return the jdbc connection
	 */
	public Connection getJdbcConnection();

	/**
	 * Initialize.
	 * 
	 * @param dbc the dbc
	 */
	public void initialize(DatabaseGateway<A> dbc);



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * Gets the database gateway.
	 * 
	 * @return the database gateway
	 */
	public DatabaseGateway<A> getDatabaseGateway();
	
	/**
	 * Gets the retrospection accessor.
	 * 
	 * @return the retrospection accessor
	 */
	public DbmsRetrospectionAccessor<A> getRetrospectionAccessor();
	
	/**
	 * Gets the dML assembler.
	 * 
	 * @return the dML assembler
	 */
	public DbmsDMLAssembler<A> getDMLAssembler();
	
	/**
	 * Gets the ddl mapper.
	 * 
	 * @return the ddl mapper
	 */
	public DbmsDDLMapper<A> getDdlMapper();

	public DbmsConfiguration<A> getConfiguration();

	public DbmsSyntax<A> getSyntax();
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	
	/**
	 * Sets the database gateway.
	 * 
	 * @param dbgw the dbgw
	 * @return the a
	 */
	public A setDatabaseGateway(DatabaseGateway<A> dbgw);

	public A setConfiguration(DbmsConfiguration<A> configuration);


	

	// db util logic //

	/**
	 * Calls an appropriate database function to calculate and update the selectivity for all columns of
	 * <code>table</code>, like MS SQL Server's "UPDATE STATISTICS" or InterSystems Cache's "%TuneTable()".<br>
	 * The return value is whatever the DBMSAdaptor gets from the DB as an answer/result
	 * or whatever the DBMSAdaptor deems best.
	 * <p>
	 * If the DBMS does not support or need such a function, the method has no effect and returns <tt>null</tt>.
	 * 
	 * @param table the table for which the selecticity shall be updated.
	 * @return an apropriate return value or  if the method is not supported.
	 */
	public Object updateSelectivity(SqlTableIdentity table);

	/**
	 * Calls "TRUNCATE TABLE" for <code>table</code> or an equivalent depending on the database.
	 * 
	 * @param table the table
	 * @return the object
	 */
	public Object truncate(SqlTable table);

	/**
	 * Rebuild all indices.
	 * 
	 * @param fullQualifiedTableName the full qualified table name
	 * @return the object
	 */
	public Object rebuildAllIndices(String fullQualifiedTableName);



	// DBMS capabilities information //

	/**
	 * Returns the maximum length for the DDL type VARCHAR measured in unicode compatible 16 bit characters.
	 *
	 * @return the maximal VARCHAR length in 16 bit characters.
	 */
	public int getMaxVARCHARlength();

	public char getIdentifierDelimiter();
	
	/**
	 * Supports merge.
	 * 
	 * @return true, if successful
	 */
	public boolean supportsMERGE();

	public boolean supportsOFFSET_ROWS();
	



	// error handling //

	/**
	 * Parses the sql exception.
	 * 
	 * @param e the e
	 * @return the sQL engine exception
	 */
	public SQLEngineException parseSqlException(SQLException e);








	// SQL Query escaping logics //

	/**
	 * Escape.
	 * 
	 * @param s the s
	 * @return the string
	 */
	public String escape(String s);
	
	/**
	 * Assemble escape.
	 * 
	 * @param sb the sb
	 * @param s the s
	 * @return the string builder
	 */
	public StringBuilder assembleEscape(StringBuilder sb, String s);

	/**
	 * Transform bytes.
	 * 
	 * @param bytes the bytes
	 * @return the string
	 */
	public String transformBytes(byte[] bytes);
	
	/**
	 * Assemble transform bytes.
	 * 
	 * @param bytes the bytes
	 * @param sb the sb
	 * @return the string builder
	 */
	public StringBuilder assembleTransformBytes(byte[] bytes, StringBuilder sb);

	

	
	

	/**
	 * Default implementation of DbmsAdaptor.
	 * 
	 * @param <A> the generic type
	 * @param <DML> the generic type
	 * @param <DDL> the generic type
	 * @param <RA> the generic type
	 * @author Thomas Muenz
	 */
	public abstract class Implementation<
		A extends DbmsAdaptor<A>,
		DML extends DbmsDMLAssembler<A>,
		DDL extends DbmsDDLMapper<A>,
		RA extends DbmsRetrospectionAccessor<A>,
		S extends DbmsSyntax<A>
	>
	implements DbmsAdaptor<A>
	{	
		/**
		 * Append include exclude conditions.
		 * 
		 * @param sb the sb
		 * @param schemaInclusionPatterns the schema inclusion patterns
		 * @param schemaExcluionPatterns the schema excluion patterns
		 * @param tableIncluionPatterns the table incluion patterns
		 * @param tableExcluionPatterns the table excluion patterns
		 * @param schemaColumn the schema column
		 * @param tablenameColumn the tablename column
		 */
		protected static void appendIncludeExcludeConditions(
			final StringBuilder sb,
			String[] schemaInclusionPatterns,
			String[] schemaExcluionPatterns,
			String[] tableIncluionPatterns,
			String[] tableExcluionPatterns,
			final String schemaColumn,
			final String tablenameColumn
		)
		{
			if(schemaInclusionPatterns == null) schemaInclusionPatterns = new String[0];
			if(schemaExcluionPatterns == null) schemaExcluionPatterns = new String[0];
			if(tableIncluionPatterns == null) tableIncluionPatterns = new String[0];
			if(tableExcluionPatterns == null) tableExcluionPatterns = new String[0];


			//schemaInclusionPatterns
			if(schemaInclusionPatterns.length > 0){
				sb.append(NEW_LINE).append(__AND).append(_).append(par);
				for(int i = 0; i < schemaInclusionPatterns.length; i++){
					sb.append(i>0?_OR_:"");
					sb.append(schemaColumn).append(_).append(LIKE).append(_)
					.append(SQL.util.escapeIfNecessary(schemaInclusionPatterns[i]));
				}
				sb.append(rap);
			}

			//schemaExcluionPatterns
			if(schemaExcluionPatterns.length > 0){
				sb.append(NEW_LINE).append(__AND).append(_).append(NOT).append(_).append(par);
				for(int i = 0; i < schemaExcluionPatterns.length; i++){
					sb.append(i>0?_OR_:"");
					sb.append(schemaColumn).append(_).append(LIKE).append(_)
					.append(SQL.util.escapeIfNecessary(schemaExcluionPatterns[i]));
				}
				sb.append(rap);
			}

			//tableIncluionPatterns
			if(tableIncluionPatterns.length > 0){
				sb.append(NEW_LINE).append(__AND).append(_).append(par);
				for(int i = 0; i < tableIncluionPatterns.length; i++){
					sb.append(i>0?_OR_:"");
					sb.append(schemaColumn).append(_).append(LIKE).append(_)
					.append(SQL.util.escapeIfNecessary(tableIncluionPatterns[i]));
				}
				sb.append(rap);
			}

			//tableExcluionPatterns
			if(tableExcluionPatterns.length > 0){
				sb.append(NEW_LINE).append(__AND).append(_).append(NOT).append(_).append(par);
				for(int i = 0; i < tableExcluionPatterns.length; i++){
					sb.append(i>0?_OR_:"");
					sb.append(schemaColumn).append(_).append(LIKE).append(_)
					.append(SQL.util.escapeIfNecessary(tableExcluionPatterns[i]));
				}
				sb.append(rap);
			}

		}


		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		/** The dbg. */
		private DatabaseGateway<A> dbg = null;
		
		/** The sql exception parser. */
		private final SQLExceptionParser sqlExceptionParser;
		
		/** The dml assembler. */
		private DML dmlAssembler;
		
		/** The ddl mapper. */
		private DDL ddlMapper;
		
		/** The retrospection accessor. */
		private RA retrospectionAccessor;
		
		private S syntax;
		
		private DbmsConfiguration<A> configuration = new DbmsConfiguration<A>();


		/** The supports_ merge. */
		private final boolean supports_MERGE;




		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////
		/**
		 * Instantiates a new body.
		 * 
		 * @param sqlExceptionParser the sql exception parser
		 * @param supports_MERGE the supports_ merge
		 */
		public Implementation(final SQLExceptionParser sqlExceptionParser, final boolean supports_MERGE){
			super();
			this.sqlExceptionParser = sqlExceptionParser;
			this.supports_MERGE = supports_MERGE;
		}



		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////
		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#getDatabaseGateway()
		 */
		@Override
		public DatabaseGateway<A> getDatabaseGateway() {
			return this.dbg;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#getJdbcConnection()
		 */
		@Override
		public Connection getJdbcConnection() {
			if(this.dbg == null) return null;
			return this.dbg.getConnection();
		}

		/**
		 * Gets the dML assembler.
		 * 
		 * @return the queryAssembler
		 */
		@Override
		public DML getDMLAssembler() {
			return this.dmlAssembler;
		}

		/**
		 * Gets the ddl mapper.
		 * 
		 * @return the ddl mapper
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#getDdlMapper()
		 */
		@Override
		public DDL getDdlMapper() {
			return ddlMapper;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#getRetrospectionAccessor()
		 */
		@Override
		public RA getRetrospectionAccessor() {
			return this.retrospectionAccessor;
		}

		/**
		 * @return
		 */
		@Override
		public DbmsSyntax<A> getSyntax()
		{
			return this.syntax;
		}		
		
		/**
		 * @return
		 */
		@Override
		public DbmsConfiguration<A> getConfiguration()
		{
			return this.configuration;
		}


		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////

		/**
		 * Sets the dml assembler.
		 * 
		 * @param queryAssembler the queryAssembler to set
		 * @return the a
		 */
		@SuppressWarnings("unchecked")
		protected A setDMLAssembler(final DML queryAssembler) {
			this.dmlAssembler = queryAssembler;
			return (A)this;
		}

		/**
		 * Sets the ddl mapper.
		 * 
		 * @param ddlMapper the ddlMapper to set
		 * @return the a
		 */
		@SuppressWarnings("unchecked")
		protected A setDdlMapper(final DDL ddlMapper) {
			this.ddlMapper = ddlMapper;
			return (A)this;
		}

		/**
		 * Sets the retrospection accessor.
		 * 
		 * @param retrospectionAccessor the retrospectionAccessor to set
		 * @return the a
		 */
		@SuppressWarnings("unchecked")
		protected A setRetrospectionAccessor(final RA retrospectionAccessor) {
			this.retrospectionAccessor = retrospectionAccessor;
			return (A)this;
		}

		/**
		 * @param dbc
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#setDatabaseGateway(com.xdev.jadoth.sqlengine.internal.DatabaseGateway)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public A setDatabaseGateway(final DatabaseGateway<A> dbc) {
			this.dbg = dbc;
			return (A)this;
		}

		
		/**
		 * @param configuration
		 * @return
		 */
		@SuppressWarnings("unchecked")
		@Override
		public A setConfiguration(final DbmsConfiguration<A> configuration)
		{
			this.configuration = configuration;
			return (A)this;
		}
		
		@SuppressWarnings("unchecked")
		protected A setSyntax(final S syntax) {
			this.syntax = syntax;
			return (A)this;
		}


		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * Executes a standard SQL "TRUNCATE" Query via JDBC Statement.executeUpdate().
		 * 
		 * @param table the table
		 * @return the int value returned by Statement.executeUpdate(String sql).
		 * @throws an SQLEngineException wrapping any occuring SQLException
		 * @throws SQLEngineException the sQL engine exception
		 */
		@Override
		public Object truncate(final SqlTable table) throws SQLEngineException 
		{
			return this.getDatabaseGateway().execute(SqlExecutor.update, table.sql().TRUNCATE());			
		}

		/**
		 * @param e
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#parseSqlException(java.sql.SQLException)
		 */
		@Override
		public SQLEngineException parseSqlException(final SQLException e) {
			return this.sqlExceptionParser.parseSQLException(e);
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#supportsMERGE()
		 */
		@Override
		public boolean supportsMERGE() {
			return supports_MERGE;
		}

		//default implementation in case DBMS needs no special escaping
		/**
		 * @param s
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#escape(java.lang.String)
		 */
		@Override
		public String escape(final String s) {
			return SQL.util.escape(s);
		}

		/**
		 * @param sb
		 * @param s
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#assembleEscape(java.lang.StringBuilder, java.lang.String)
		 */
		@Override
		public StringBuilder assembleEscape(final StringBuilder sb, final String s) {
			return SQL.util.assembleEscape(sb, s);
		}

		/**
		 * @param bytes
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#transformBytes(byte[])
		 */
		@Override
		public String transformBytes(final byte[] bytes) {
			return assembleTransformBytes(bytes, null).toString();
		}

		
	}



	
	/**
	 * The Interface Member.
	 * 
	 * @param <A> the generic type
	 */
	interface Member<A extends DbmsAdaptor<A>>
	{		
		/**
		 * Gets the dbms adaptor.
		 * 
		 * @return the dbms adaptor
		 */
		public A getDbmsAdaptor();

		/**
		 * The Class Body.
		 * 
		 * @param <A> the generic type
		 */
		public abstract class Implementation<A extends DbmsAdaptor<A>> implements Member<A>
		{			
			/** The dbms adaptor. */
			private A dbmsAdaptor;

			/**
			 * Instantiates a new body.
			 * 
			 * @param dbmsAdaptor the dbms adaptor
			 */
			protected Implementation(final A dbmsAdaptor) {
				super();
				this.dbmsAdaptor = dbmsAdaptor;
			}

			/**
			 * @return
			 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor.Member#getDbmsAdaptor()
			 */
			@Override
			public A getDbmsAdaptor() {
				return dbmsAdaptor;
			}

			/**
			 * Sets the dbms adaptor.
			 * 
			 * @param dbmsAdaptor the new dbms adaptor
			 */
			protected void setDbmsAdaptor(final A dbmsAdaptor) {
				this.dbmsAdaptor = dbmsAdaptor;
			}
				
		}
		
	}

}




