
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

import java.util.List;

import com.xdev.jadoth.collections.GrowList;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.SQL.DATATYPE;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.interfaces.SqlExecutor;
import com.xdev.jadoth.sqlengine.internal.SqlField;
import com.xdev.jadoth.sqlengine.internal.tables.SqlIndex;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.retrospection.RetrospectionIdentifierMapper;
import com.xdev.jadoth.sqlengine.util.ResultTable;




/**
 * The Interface DbmsRetrospectionAccessor.
 * 
 * @param <A> the generic type
 * @author Thomas Muenz
 */
public interface DbmsRetrospectionAccessor<A extends DbmsAdaptor<A>> extends DbmsAdaptor.Member<A>
{
	
	/** The Constant Column_TABLE_SCHEMA. */
	public static final String Column_TABLE_SCHEMA = "TABLE_SCHEMA";
	
	/** The Constant Column_TABLE_NAME. */
	public static final String Column_TABLE_NAME = "TABLE_NAME";
	
	/** The Constant Column_TABLE_TYPE. */
	public static final String Column_TABLE_TYPE = "TABLE_TYPE";

	/** The Constant Column_COLUMN_NAME. */
	public static final String Column_COLUMN_NAME = "COLUMN_NAME";
	
	/** The Constant Column_COLUMN_DEFAULT. */
	public static final String Column_COLUMN_DEFAULT = "COLUMN_DEFAULT";
	
	/** The Constant Column_IS_NULLABLE. */
	public static final String Column_IS_NULLABLE = "IS_NULLABLE";
	
	/** The Constant Column_DATA_TYPE. */
	public static final String Column_DATA_TYPE = "DATA_TYPE";
	
	/** The Constant Column_CHARACTER_MAXIMUM_LENGTH. */
	public static final String Column_CHARACTER_MAXIMUM_LENGTH = "CHARACTER_MAXIMUM_LENGTH";



	/**
 * Gets the general identifier mappers.
 * 
 * @return the general identifier mappers
 */
public List<RetrospectionIdentifierMapper> getGeneralIdentifierMappers();
	
	/**
	 * Gets the schema identifier mappers.
	 * 
	 * @return the schema identifier mappers
	 */
	public List<RetrospectionIdentifierMapper> getSchemaIdentifierMappers();
	
	/**
	 * Gets the table identifier mappers.
	 * 
	 * @return the table identifier mappers
	 */
	public List<RetrospectionIdentifierMapper> getTableIdentifierMappers();
	
	/**
	 * Gets the column identifier mappers.
	 * 
	 * @return the column identifier mappers
	 */
	public List<RetrospectionIdentifierMapper> getColumnIdentifierMappers();
	
	/**
	 * Gets the index identifier mappers.
	 * 
	 * @return the index identifier mappers
	 */
	public List<RetrospectionIdentifierMapper> getIndexIdentifierMappers();

	/**
	 * Sets the general identifier mappers.
	 * 
	 * @param schemaIdentifierMappers the new general identifier mappers
	 */
	public void setGeneralIdentifierMappers(List<RetrospectionIdentifierMapper> schemaIdentifierMappers);
	
	/**
	 * Sets the schema identifier mappers.
	 * 
	 * @param schemaIdentifierMappers the new schema identifier mappers
	 */
	public void setSchemaIdentifierMappers(List<RetrospectionIdentifierMapper> schemaIdentifierMappers);
	
	/**
	 * Sets the table identifier mappers.
	 * 
	 * @param tableIdentifierMappers the new table identifier mappers
	 */
	public void setTableIdentifierMappers(List<RetrospectionIdentifierMapper> tableIdentifierMappers);
	
	/**
	 * Sets the column identifier mappers.
	 * 
	 * @param columnIdentifierMappers the new column identifier mappers
	 */
	public void setColumnIdentifierMappers(List<RetrospectionIdentifierMapper> columnIdentifierMappers);
	
	/**
	 * Sets the index identifier mappers.
	 * 
	 * @param indexIdentifierMappers the new index identifier mappers
	 */
	public void setIndexIdentifierMappers(List<RetrospectionIdentifierMapper> indexIdentifierMappers);



	/**
	 * Gets the system table_ tables.
	 * 
	 * @return the system table_ tables
	 */
	public SqlTableIdentity getSystemTable_TABLES();
	
	/**
	 * Gets the system table_ columns.
	 * 
	 * @return the system table_ columns
	 */
	public SqlTableIdentity getSystemTable_COLUMNS();

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor.Member#getDbmsAdaptor()
	 */
	public A getDbmsAdaptor();

	/**
	 * Creates the select_ informatio n_ schem a_ tables.
	 * 
	 * @param schemaInclusionPatterns the schema inclusion patterns
	 * @param schemaExclusionPatterns the schema exclusion patterns
	 * @param tableInclusionPatterns the table inclusion patterns
	 * @param tableExclusionPatterns the table exclusion patterns
	 * @param additionalWHERECondition the additional where condition
	 * @return the string
	 */
	public String createSelect_INFORMATION_SCHEMA_TABLES(
		String[] schemaInclusionPatterns,
		String[] schemaExclusionPatterns,
		String[] tableInclusionPatterns,
		String[] tableExclusionPatterns,
		String additionalWHERECondition
	);

	/**
	 * Load tables.
	 * 
	 * @param schemaInclusionPatterns the schema inclusion patterns
	 * @param schemaExclusionPatterns the schema exclusion patterns
	 * @param tableInclusionPatterns the table inclusion patterns
	 * @param tableExclusionPatterns the table exclusion patterns
	 * @param additionalWHERECondition the additional where condition
	 * @return the sql table identity[]
	 * @throws SQLEngineException the sQL engine exception
	 */
	public SqlTableIdentity[] loadTables(
		String[] schemaInclusionPatterns,
		String[] schemaExclusionPatterns,
		String[] tableInclusionPatterns,
		String[] tableExclusionPatterns,
		String additionalWHERECondition
	) throws SQLEngineException;

	/**
	 * Load tables as strings.
	 * 
	 * @param schemaInclusionPatterns the schema inclusion patterns
	 * @param schemaExclusionPatterns the schema exclusion patterns
	 * @param tableInclusionPatterns the table inclusion patterns
	 * @param tableExclusionPatterns the table exclusion patterns
	 * @param additionalWHERECondition the additional where condition
	 * @return the list
	 * @throws SQLEngineException the sQL engine exception
	 */
	public List<String> loadTablesAsStrings(
		String[] schemaInclusionPatterns,
		String[] schemaExclusionPatterns,
		String[] tableInclusionPatterns,
		String[] tableExclusionPatterns,
		String additionalWHERECondition
	) throws SQLEngineException;

	/**
	 * Creates the select_ informatio n_ schem a_ columns.
	 * 
	 * @param table the table
	 * @return the string
	 */
	public String createSelect_INFORMATION_SCHEMA_COLUMNS(SqlTableIdentity table);
	
	/**
	 * Creates the select_ informatio n_ schem a_ indices.
	 * 
	 * @param table the table
	 * @return the string
	 */
	public String createSelect_INFORMATION_SCHEMA_INDICES(SqlTableIdentity table);

	/**
	 * Load columns.
	 * 
	 * @param table the table
	 * @return the sql field[]
	 * @throws SQLEngineException the sQL engine exception
	 */
	public SqlField[] loadColumns(SqlTableIdentity table) throws SQLEngineException;
	
	/**
	 * Load indices.
	 * 
	 * @param table the table
	 * @return the sql index[]
	 * @throws SQLEngineException the sQL engine exception
	 */
	public SqlIndex[] loadIndices(SqlTableIdentity table) throws SQLEngineException;

	/**
	 * Parses the column default value.
	 * 
	 * @param rawDefaultValue the raw default value
	 * @return the object
	 */
	public Object parseColumnDefaultValue(Object rawDefaultValue);

	/**
	 * Gets the retrospection code generation note.
	 * 
	 * @return the retrospection code generation note
	 */
	public String getRetrospectionCodeGenerationNote();




	/**
	 * Body implementation of DbmsRetrospection.
	 * 
	 * @param <A> the generic type
	 * @author Thomas Muenz
	 */
	public static abstract class Implementation<A extends DbmsAdaptor<A>>
	extends DbmsAdaptor.Member.Implementation<A>
	implements DbmsRetrospectionAccessor<A>
	{

		///////////////////////////////////////////////////////////////////////////
		// static methods //
		///////////////////
		
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

		/** The general identifier mappers. */
		private List<RetrospectionIdentifierMapper> generalIdentifierMappers =
			new GrowList<RetrospectionIdentifierMapper>();
		
		/** The schema identifier mappers. */
		private List<RetrospectionIdentifierMapper> schemaIdentifierMappers =
			new GrowList<RetrospectionIdentifierMapper>();
		
		/** The table identifier mappers. */
		private List<RetrospectionIdentifierMapper> tableIdentifierMappers =
			new GrowList<RetrospectionIdentifierMapper>();
		
		/** The column identifier mappers. */
		private List<RetrospectionIdentifierMapper> columnIdentifierMappers =
			new GrowList<RetrospectionIdentifierMapper>();
		
		/** The index identifier mappers. */
		private List<RetrospectionIdentifierMapper> indexIdentifierMappers =
			new GrowList<RetrospectionIdentifierMapper>();



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////
		/**
		 * Instantiates a new abstract body.
		 * 
		 * @param dbmsadaptor the dbmsadaptor
		 */
		public Implementation(final A dbmsadaptor) {
			super(dbmsadaptor);
		}




		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////

		/**
		 * Gets the general identifier mappers.
		 * 
		 * @return the generalIdentifierMappers
		 */
		@Override
		public List<RetrospectionIdentifierMapper> getGeneralIdentifierMappers() {
			return generalIdentifierMappers;
		}
		
		/**
		 * Gets the schema identifier mappers.
		 * 
		 * @return the schemaIdentifierMappers
		 */
		@Override
		public List<RetrospectionIdentifierMapper> getSchemaIdentifierMappers() {
			return schemaIdentifierMappers;
		}
		
		/**
		 * Gets the table identifier mappers.
		 * 
		 * @return the tableIdentifierMappers
		 */
		@Override
		public List<RetrospectionIdentifierMapper> getTableIdentifierMappers() {
			return tableIdentifierMappers;
		}
		
		/**
		 * Gets the column identifier mappers.
		 * 
		 * @return the columnIdentifierMappers
		 */
		@Override
		public List<RetrospectionIdentifierMapper> getColumnIdentifierMappers() {
			return columnIdentifierMappers;
		}
		
		/**
		 * Gets the index identifier mappers.
		 * 
		 * @return the indexIdentifierMappers
		 */
		@Override
		public List<RetrospectionIdentifierMapper> getIndexIdentifierMappers() {
			return indexIdentifierMappers;
		}



		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
		/**
		 * Sets the general identifier mappers.
		 * 
		 * @param generalIdentifierMappers the generalIdentifierMappers to set
		 */
		@Override
		public void setGeneralIdentifierMappers(final List<RetrospectionIdentifierMapper> generalIdentifierMappers) {
			this.generalIdentifierMappers = generalIdentifierMappers;
		}
		
		/**
		 * Sets the schema identifier mappers.
		 * 
		 * @param schemaIdentifierMappers the schemaIdentifierMappers to set
		 */
		@Override
		public void setSchemaIdentifierMappers(final List<RetrospectionIdentifierMapper> schemaIdentifierMappers) {
			this.schemaIdentifierMappers = schemaIdentifierMappers;
		}
		
		/**
		 * Sets the table identifier mappers.
		 * 
		 * @param tableIdentifierMappers the tableIdentifierMappers to set
		 */
		@Override
		public void setTableIdentifierMappers(final List<RetrospectionIdentifierMapper> tableIdentifierMappers) {
			this.tableIdentifierMappers = tableIdentifierMappers;
		}
		
		/**
		 * Sets the column identifier mappers.
		 * 
		 * @param columnIdentifierMappers the columnIdentifierMappers to set
		 */
		@Override
		public void setColumnIdentifierMappers(final List<RetrospectionIdentifierMapper> columnIdentifierMappers) {
			this.columnIdentifierMappers = columnIdentifierMappers;
		}
		
		/**
		 * Sets the index identifier mappers.
		 * 
		 * @param indexIdentifierMappers the indexIdentifierMappers to set
		 */
		@Override
		public void setIndexIdentifierMappers(final List<RetrospectionIdentifierMapper> indexIdentifierMappers) {
			this.indexIdentifierMappers = indexIdentifierMappers;
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * @param rawDefaultValue
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsRetrospectionAccessor#parseColumnDefaultValue(java.lang.Object)
		 */
		@Override
		public Object parseColumnDefaultValue(final Object rawDefaultValue) {
			return rawDefaultValue;
		}

		/**
		 * @param schemaInclusionPatterns
		 * @param schemaExcluionPatterns
		 * @param tableIncluionPatterns
		 * @param tableExcluionPatterns
		 * @param additionalWHERECondition
		 * @return
		 * @throws SQLEngineException
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsRetrospectionAccessor#loadTables(java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String)
		 */
		@Override
		public SqlTableIdentity[] loadTables(
			final String[] schemaInclusionPatterns,
			final String[] schemaExcluionPatterns,
			final String[] tableIncluionPatterns,
			final String[] tableExcluionPatterns,
			final String additionalWHERECondition
		)
			throws SQLEngineException
		{
			final String select_Tables = createSelect_INFORMATION_SCHEMA_TABLES(
				schemaInclusionPatterns,
				schemaExcluionPatterns,
				tableIncluionPatterns,
				tableExcluionPatterns,
				additionalWHERECondition
			);

			final ResultTable rt = new ResultTable(this.getDbmsAdaptor().getDatabaseGateway().execute(SqlExecutor.query, select_Tables));
			final int indexSchema = rt.getColumnIndex(Column_TABLE_SCHEMA);
			final int indexTable = rt.getColumnIndex(Column_TABLE_NAME);
			final SqlTableIdentity[] tables = new SqlTableIdentity[rt.getRowCount()];
			for (int i = 0; i < tables.length; i++) {
				tables[i] = new SqlTableIdentity(
					rt.getValue(i, indexSchema).toString(),
					rt.getValue(i, indexTable).toString(),
					null
				);
			}
			return tables;
		}

		/**
		 * @param schemaInclusionPatterns
		 * @param schemaExcluionPatterns
		 * @param tableIncluionPatterns
		 * @param tableExcluionPatterns
		 * @param additionalWHERECondition
		 * @return
		 * @throws SQLEngineException
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsRetrospectionAccessor#loadTablesAsStrings(java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String)
		 */
		@Override
		public List<String> loadTablesAsStrings(
			final String[] schemaInclusionPatterns,
			final String[] schemaExcluionPatterns,
			final String[] tableIncluionPatterns,
			final String[] tableExcluionPatterns,
			final String additionalWHERECondition
		)
			throws SQLEngineException
		{
			final SqlTableIdentity[] tables = loadTables(
				schemaInclusionPatterns,
				schemaExcluionPatterns,
				tableIncluionPatterns,
				tableExcluionPatterns,
				additionalWHERECondition
			);

			final GrowList<String> tableStrings = new GrowList<String>(tables.length);
			for(final SqlTableIdentity table : tables) {
				tableStrings.add(table.toString());
			}
			return tableStrings;
		}

		/**
		 * @param table
		 * @return
		 * @throws SQLEngineException
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsRetrospectionAccessor#loadColumns(com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity)
		 */
		@Override
		public SqlField[] loadColumns(final SqlTableIdentity table) throws SQLEngineException
		{
			final String selectInformationSchemaColumns = createSelect_INFORMATION_SCHEMA_COLUMNS(table);
			final ResultTable rt = new ResultTable(getDbmsAdaptor().getDatabaseGateway().execute(SqlExecutor.query, selectInformationSchemaColumns));
			final int rowCount = rt.getRowCount();
			final SqlField[] columns = new SqlField[rowCount];

			final int colIndexName = rt.getColumnIndex(Column_COLUMN_NAME);
			final int colIndexType = rt.getColumnIndex(Column_DATA_TYPE);
			final int colIndexNullable = rt.getColumnIndex(Column_IS_NULLABLE);
			final int colIndexDefault = rt.getColumnIndex(Column_COLUMN_DEFAULT);
			final int colIndexMaxLen = rt.getColumnIndex(Column_CHARACTER_MAXIMUM_LENGTH);

			String name = null;
			DATATYPE type = null;
			boolean nullable = true;
			Object defaultValue = null;
			Integer length = 0;
			for (int i = 0; i < rowCount; i++) {
				name = rt.getValue(i, colIndexName).toString();
				type = this.getDbmsAdaptor().getDdlMapper().mapDataType(rt.getValue(i, colIndexType).toString());
				nullable = SQL.util.recognizeBooleanPrimitive(rt.getValue(i, colIndexNullable));
				defaultValue = this.parseColumnDefaultValue(rt.getValue(i, colIndexDefault));
				length = (Integer)rt.getValue(i, colIndexMaxLen);
				try {
					columns[i] = new SqlField(name, type, length==null?0:length, null, null, !nullable, false, defaultValue);
				}
				catch(final Exception e) {
					e.printStackTrace();
				}
				columns[i].setOwner(table);
			}
			return columns;
		}







	}

}
