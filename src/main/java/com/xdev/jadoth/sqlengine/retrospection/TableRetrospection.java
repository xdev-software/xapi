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
package com.xdev.jadoth.sqlengine.retrospection;

import static com.xdev.jadoth.sqlengine.retrospection.RetrospectionIdentifierMapper.Util.applyIdentifierMappers;

import java.util.ArrayList;
import java.util.List;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsRetrospectionAccessor;
import com.xdev.jadoth.sqlengine.internal.SqlField;
import com.xdev.jadoth.sqlengine.internal.tables.SqlIndex;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.retrospection.definition.ColumnDefinition;
import com.xdev.jadoth.sqlengine.retrospection.definition.IndexDefinition;
import com.xdev.jadoth.sqlengine.retrospection.definition.TableDefinition;



/**
 * The Class TableRetrospection.
 * 
 * @param <A> the generic type
 */
public class TableRetrospection<A extends DbmsAdaptor<A>>
{
	
	/** The dbms. */
	protected A dbms;
	
	/** The mapping generator. */
	protected MappingGenerator mappingGenerator;
	
	/** The file suffix. */
	private String fileSuffix = "java";

	/** The general identifier mappers. */
	private List<RetrospectionIdentifierMapper> generalIdentifierMappers =
		new ArrayList<RetrospectionIdentifierMapper>();
	
	/** The schema identifier mappers. */
	private List<RetrospectionIdentifierMapper> schemaIdentifierMappers =
		new ArrayList<RetrospectionIdentifierMapper>();
	
	/** The table identifier mappers. */
	private List<RetrospectionIdentifierMapper> tableIdentifierMappers =
		new ArrayList<RetrospectionIdentifierMapper>();
	
	/** The column identifier mappers. */
	private List<RetrospectionIdentifierMapper> columnIdentifierMappers =
		new ArrayList<RetrospectionIdentifierMapper>();
	
	/** The index identifier mappers. */
	private List<RetrospectionIdentifierMapper> indexIdentifierMappers =
		new ArrayList<RetrospectionIdentifierMapper>();


	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new table retrospection.
	 */
	@SuppressWarnings("unchecked")
	public TableRetrospection() {
		this((A)SQL.getDefaultDBMS());
	}
	
	/**
	 * Instantiates a new table retrospection.
	 * 
	 * @param dbms the dbms
	 */
	public TableRetrospection(final A dbms) {
		this(dbms, new MappingGenerator());
	}
	
	/**
	 * Instantiates a new table retrospection.
	 * 
	 * @param dbms the dbms
	 * @param mappingGenerator the mapping generator
	 */
	public TableRetrospection(final A dbms, final MappingGenerator mappingGenerator) {
		super();
		this.dbms = dbms;
		this.mappingGenerator = mappingGenerator;
	}
	
	/**
	 * Instantiates a new table retrospection.
	 * 
	 * @param mappingGenerator the mapping generator
	 */
	@SuppressWarnings("unchecked")
	public TableRetrospection(final MappingGenerator mappingGenerator) {
		this((A)SQL.getDefaultDBMS(), mappingGenerator);
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the dbms.
	 * 
	 * @return the dbms
	 */
	public A getDbms() {
		return this.dbms;
	}
	
	/**
	 * Gets the mapping generator.
	 * 
	 * @return the mappingGenerator
	 */
	public MappingGenerator getMappingGenerator() {
		return this.mappingGenerator;
	}
	
	/**
	 * Gets the file suffix.
	 * 
	 * @return the fileSuffix
	 */
	protected String getFileSuffix() {
		return this.fileSuffix;
	}
	
	/**
	 * Gets the schema identifier mappers.
	 * 
	 * @return the schemaIdentifierMappers
	 */
	public List<RetrospectionIdentifierMapper> getSchemaIdentifierMappers() {
		return this.schemaIdentifierMappers;
	}
	
	/**
	 * Gets the table identifier mappers.
	 * 
	 * @return the tableIdentifierMappers
	 */
	public List<RetrospectionIdentifierMapper> getTableIdentifierMappers() {
		return this.tableIdentifierMappers;
	}
	
	/**
	 * Gets the column identifier mappers.
	 * 
	 * @return the columnIdentifierMappers
	 */
	public List<RetrospectionIdentifierMapper> getColumnIdentifierMappers() {
		return this.columnIdentifierMappers;
	}
	
	/**
	 * Gets the index identifier mappers.
	 * 
	 * @return the indexIdentifierMappers
	 */
	public List<RetrospectionIdentifierMapper> getIndexIdentifierMappers() {
		return this.indexIdentifierMappers;
	}




	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the dbms.
	 * 
	 * @param dbms the dbms to set
	 */
	public void setDbms(final A dbms) {
		this.dbms = dbms;
	}
	
	/**
	 * Sets the mapping generator.
	 * 
	 * @param mappingGenerator the mappingGenerator to set
	 */
	public void setMappingGenerator(final MappingGenerator mappingGenerator) {
		this.mappingGenerator = mappingGenerator;
	}
	
	/**
	 * Sets the file suffix.
	 * 
	 * @param fileSuffix the fileSuffix to set
	 */
	protected void setFileSuffix(final String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
	
	/**
	 * Sets the schema identifier mappers.
	 * 
	 * @param schemaIdentifierMappers the schemaIdentifierMappers to set
	 */
	public void setSchemaIdentifierMappers(final List<RetrospectionIdentifierMapper> schemaIdentifierMappers) {
		this.schemaIdentifierMappers = schemaIdentifierMappers;
	}
	
	/**
	 * Sets the table identifier mappers.
	 * 
	 * @param tableIdentifierMappers the tableIdentifierMappers to set
	 */
	public void setTableIdentifierMappers(final List<RetrospectionIdentifierMapper> tableIdentifierMappers) {
		this.tableIdentifierMappers = tableIdentifierMappers;
	}
	
	/**
	 * Sets the column identifier mappers.
	 * 
	 * @param columnIdentifierMappers the columnIdentifierMappers to set
	 */
	public void setColumnIdentifierMappers(final List<RetrospectionIdentifierMapper> columnIdentifierMappers) {
		this.columnIdentifierMappers = columnIdentifierMappers;
	}
	
	/**
	 * Sets the index identifier mappers.
	 * 
	 * @param indexIdentifierMappers the indexIdentifierMappers to set
	 */
	public void setIndexIdentifierMappers(final List<RetrospectionIdentifierMapper> indexIdentifierMappers) {
		this.indexIdentifierMappers = indexIdentifierMappers;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	/**
	 * Adds the identifier mapper.
	 * 
	 * @param mapperList the mapper list
	 * @param mappers the mappers
	 */
	protected static void addIdentifierMapper(
		final List<RetrospectionIdentifierMapper> mapperList,
		final RetrospectionIdentifierMapper[] mappers
	)
	{
		if(mapperList == null || mappers == null) return;
		for(final RetrospectionIdentifierMapper mapper : mappers) {
			mapperList.add(mapper);
		}
	}


	/**
	 * Adds the general identifier mapper.
	 * 
	 * @param mappers the mappers
	 * @return the table retrospection
	 */
	public TableRetrospection<A> addGeneralIdentifierMapper(final RetrospectionIdentifierMapper... mappers)
	{
		addIdentifierMapper(this.generalIdentifierMappers, mappers);
		return this;
	}
	
	/**
	 * Adds the schema identifier mapper.
	 * 
	 * @param mappers the mappers
	 * @return the table retrospection
	 */
	public TableRetrospection<A> addSchemaIdentifierMapper(final RetrospectionIdentifierMapper... mappers)
	{
		addIdentifierMapper(this.schemaIdentifierMappers, mappers);
		return this;
	}
	
	/**
	 * Adds the table identifier mapper.
	 * 
	 * @param mappers the mappers
	 * @return the table retrospection
	 */
	public TableRetrospection<A> addTableIdentifierMapper(final RetrospectionIdentifierMapper... mappers)
	{
		addIdentifierMapper(this.tableIdentifierMappers, mappers);
		return this;
	}
	
	/**
	 * Adds the column identifier mapper.
	 * 
	 * @param mappers the mappers
	 * @return the table retrospection
	 */
	public TableRetrospection<A> addColumnIdentifierMapper(final RetrospectionIdentifierMapper... mappers)
	{
		addIdentifierMapper(this.columnIdentifierMappers, mappers);
		return this;
	}
	
	/**
	 * Adds the index identifier mapper.
	 * 
	 * @param mappers the mappers
	 * @return the table retrospection
	 */
	public TableRetrospection<A> addIndexIdentifierMapper(final RetrospectionIdentifierMapper... mappers)
	{
		addIdentifierMapper(this.indexIdentifierMappers, mappers);
		return this;
	}

	/**
	 * Creates the mapping class source code.
	 * 
	 * @param tableDef the table def
	 * @return the string
	 */
	public String createMappingClassSourceCode(final TableDefinition tableDef){
		return this.mappingGenerator.setTableDef(tableDef).createSqlTableDefinitionCode();
	}

	/**
	 * Creates the mapping classes for schema pattern.
	 * 
	 * @param compile the compile
	 * @return the int
	 */
	public int createMappingClassesForSchemaPattern(final boolean compile)
	{
		final TableDefinition[] tableDefs = this.createTableDefinitionsForSchemaPattern(null, null, null, null, null);
		for(int i = 0; i < tableDefs.length; i++) {
			this.mappingGenerator.setTableDef(tableDefs[i]);
			this.mappingGenerator.writeClassFile(compile);
		}
		return tableDefs.length;
	}

	/**
	 * Creates the mapping classes for schema pattern.
	 * 
	 * @param includeSchemaPatterns the include schema patterns
	 * @param excludeSchemaPatterns the exclude schema patterns
	 * @param compile the compile
	 * @return the list
	 */
	public List<String> createMappingClassesForSchemaPattern(
		final String[] includeSchemaPatterns,
		final String[] excludeSchemaPatterns,
		final boolean compile
	)
	{
		this.mappingGenerator.clearCreatedClassesList();
		final TableDefinition[] tableDefs = this.createTableDefinitionsForSchemaPattern(
			includeSchemaPatterns,
			excludeSchemaPatterns,
			null,
			null,
			null
		);
		for(int i = 0; i < tableDefs.length; i++) {
			this.mappingGenerator.setTableDef(tableDefs[i]);
			this.mappingGenerator.writeClassFile(compile);
		}
		return this.mappingGenerator.getCreatedClassesCanoncicalNames();
	}



	/**
	 * Creates the table definitions for schema pattern.
	 * 
	 * @param schemaInclusionPatterns the schema inclusion patterns
	 * @param schemaExclusionPatterns the schema exclusion patterns
	 * @param tableInclusionPatterns the table inclusion patterns
	 * @param tableExclusionPatterns the table exclusion patterns
	 * @param additionalWHERECondition the additional where condition
	 * @return the table definition[]
	 * @return
	 */
	public TableDefinition[] createTableDefinitionsForSchemaPattern(
		String[] schemaInclusionPatterns,
		String[] schemaExclusionPatterns,
		String[] tableInclusionPatterns,
		String[] tableExclusionPatterns,
		final String additionalWHERECondition
	)
	{
		if(schemaInclusionPatterns == null) schemaInclusionPatterns = new String[0];
		if(schemaExclusionPatterns == null) schemaExclusionPatterns = new String[0];
		if(tableInclusionPatterns == null) tableInclusionPatterns = new String[0];
		if(tableExclusionPatterns == null) tableExclusionPatterns = new String[0];


		for(int i = 0; i < schemaInclusionPatterns.length; i++) {
			schemaInclusionPatterns[i] = schemaInclusionPatterns[i].trim();
		}
		for(int i = 0; i < schemaExclusionPatterns.length; i++) {
			schemaExclusionPatterns[i] = schemaExclusionPatterns[i].trim();
		}
		for(int i = 0; i < tableInclusionPatterns.length; i++) {
			tableInclusionPatterns[i] = tableInclusionPatterns[i].trim();
		}
		for(int i = 0; i < tableExclusionPatterns.length; i++) {
			tableExclusionPatterns[i] = tableExclusionPatterns[i].trim();
		}


		System.out.println("Loading tables...");


		final DbmsRetrospectionAccessor<A> retrospectionAccessor = this.dbms.getRetrospectionAccessor();
		retrospectionAccessor.setSchemaIdentifierMappers(this.schemaIdentifierMappers);
		retrospectionAccessor.setSchemaIdentifierMappers(this.schemaIdentifierMappers);
		retrospectionAccessor.setTableIdentifierMappers(this.tableIdentifierMappers);

		final SqlTableIdentity[] tables = retrospectionAccessor.loadTables(
			schemaInclusionPatterns,
			schemaExclusionPatterns,
			tableInclusionPatterns,
			tableExclusionPatterns,
			additionalWHERECondition
		);
		System.out.println("Loaded tables: "+tables.length);

		final TableDefinition[] tableDefs = new TableDefinition[tables.length];
		for(int i = 0; i < tables.length; i++) {
			System.out.println("Creating table definition for: "+tables[i]);
			tableDefs[i] = this.createTableDefinition(tables[i]);
		}
		System.out.println("Created table definitions: "+tableDefs.length);
		return tableDefs;
	}

	/**
	 * Creates the table definition.
	 * 
	 * @param table the table
	 * @return the table definition
	 */
	@SuppressWarnings("unchecked")
	public TableDefinition createTableDefinition(final SqlTableIdentity table)
	{
		final DbmsRetrospectionAccessor<A> retrospectionAccessor = this.dbms.getRetrospectionAccessor();
		retrospectionAccessor.setColumnIdentifierMappers(this.columnIdentifierMappers);
		retrospectionAccessor.setIndexIdentifierMappers(this.indexIdentifierMappers);
		final SqlField[] fields = retrospectionAccessor.loadColumns(table);
		final SqlIndex[] indices = retrospectionAccessor.loadIndices(table);

		final ArrayList<ColumnDefinition> colDefList = new ArrayList<ColumnDefinition>(fields.length);
		for(final SqlField f : fields) {
			colDefList.add(f.toColumnDefinition().setName(
				applyIdentifierMappers(f.getName(), this.generalIdentifierMappers, this.columnIdentifierMappers)
			));
		}

		final ArrayList<IndexDefinition> idxDefList = new ArrayList<IndexDefinition>(indices.length);

		for(final SqlIndex i : indices) {

			//correct the column names
			final Object[] columnArray = i.getColumnList().toArray();
			for(int j = 0; j < columnArray.length; j++) {
				columnArray[j] = applyIdentifierMappers(
					columnArray[j].toString(), this.generalIdentifierMappers, this.columnIdentifierMappers
				);
			}

			final IndexDefinition iDef = i.toIndexDefinition().setName(
				applyIdentifierMappers(i.getName(), this.generalIdentifierMappers, this.indexIdentifierMappers)
			);
			iDef.setColumns(columnArray);
			idxDefList.add(iDef);
		}

		final TableDefinition tableDef = new TableDefinition(
			table.sql().schema, table.sql().name, colDefList, idxDefList
		)
		.setSchema(applyIdentifierMappers(table.sql().schema, this.generalIdentifierMappers, this.schemaIdentifierMappers))
		.setName(applyIdentifierMappers(table.sql().name, this.generalIdentifierMappers, this.tableIdentifierMappers))
		;

		return tableDef;
	}


	/**
	 * Creates the singleton index class.
	 * 
	 * @param compile the compile
	 * @return the string
	 */
	public String createSingletonIndexClass(final boolean compile){
		return this.mappingGenerator.createSingletonIndexClass(compile);
	}


	/**
	 * Gets the general identifier mappers.
	 * 
	 * @return the generalIdentifierMappers
	 */
	public List<RetrospectionIdentifierMapper> getGeneralIdentifierMappers() {
		return this.generalIdentifierMappers;
	}
	
	/**
	 * Sets the general identifier mappers.
	 * 
	 * @param generalIdentifierMappers the generalIdentifierMappers to set
	 */
	public void setGeneralIdentifierMappers(final List<RetrospectionIdentifierMapper> generalIdentifierMappers) {
		this.generalIdentifierMappers = generalIdentifierMappers;
	}

}
