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

import java.util.List;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsConnectionInformation;
import com.xdev.jadoth.sqlengine.interfaces.ConnectionProvider;



/**
 * The Interface TableRetrospectionTask.
 */
public interface TableRetrospectionTask
{
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost();

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername();

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword();

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public String getPort();

	/**
	 * Gets the namespace.
	 *
	 * @return the namespace
	 */
	public String getNamespace();
	
	/**
	 * Gets the extended url properties
	 * 
	 * @return the properties
	 */
	public String getProperties();

	/**
	 * Gets the src working directory.
	 *
	 * @return the src working directory
	 */
	public String getSrcWorkingDirectory();

	/**
	 * Gets the bin working directory.
	 *
	 * @return the bin working directory
	 */
	public String getBinWorkingDirectory();

	/**
	 * Gets the src sub dir.
	 *
	 * @return the src sub dir
	 */
	public String getSrcSubDir();

	/**
	 * Gets the bin sub dir.
	 *
	 * @return the bin sub dir
	 */
	public String getBinSubDir();

	/**
	 * Gets the mapping class sub dir.
	 *
	 * @return the mapping class sub dir
	 */
	public String getMappingClassSubDir();

	/**
	 * Gets the table class suffix.
	 *
	 * @return the table class suffix
	 */
	public String getTableClassSuffix();

	/**
	 * Gets the table class prefix.
	 *
	 * @return the table class prefix
	 */
	public String getTableClassPrefix();

	/**
	 * Gets the constructor visibility nullary.
	 *
	 * @return the constructor visibility nullary
	 */
	public String getConstructorVisibilityNullary();

	/**
	 * Gets the constructor visibility alias.
	 *
	 * @return the constructor visibility alias
	 */
	public String getConstructorVisibilityAlias();

	/**
	 * Gets the constructor visibility name.
	 *
	 * @return the constructor visibility name
	 */
	public String getConstructorVisibilityName();

	/**
	 * Gets the constructor visibility schema.
	 *
	 * @return the constructor visibility schema
	 */
	public String getConstructorVisibilitySchema();

	/**
	 * Gets the constructor visibility schema alias.
	 *
	 * @return the constructor visibility schema alias
	 */
	public String getConstructorVisibilitySchemaAlias();

	/**
	 * Gets the constructor visibility name alias.
	 *
	 * @return the constructor visibility name alias
	 */
	public String getConstructorVisibilityNameAlias();

	/**
	 * Gets the constructor visibility schema name.
	 *
	 * @return the constructor visibility schema name
	 */
	public String getConstructorVisibilitySchemaName();

	/**
	 * Gets the constructor visibility schema name alias.
	 *
	 * @return the constructor visibility schema name alias
	 */
	public String getConstructorVisibilitySchemaNameAlias();

	/**
	 * Gets the schema inclusion patterns.
	 *
	 * @return the schema inclusion patterns
	 */
	public String getSchemaInclusionPatterns();

	/**
	 * Gets the schema exclusion patterns.
	 *
	 * @return the schema exclusion patterns
	 */
	public String getSchemaExclusionPatterns();

	/**
	 * Gets the table inclusion patterns.
	 *
	 * @return the table inclusion patterns
	 */
	public String getTableInclusionPatterns();

	/**
	 * Gets the table exclusion patterns.
	 *
	 * @return the table exclusion patterns
	 */
	public String getTableExclusionPatterns();

	/**
	 * Checks if is creates the singleton index class.
	 *
	 * @return true, if is creates the singleton index class
	 */
	public boolean isCreateSingletonIndexClass();

	/**
	 * Gets the singleton index class name.
	 *
	 * @return the singleton index class name
	 */
	public String getSingletonIndexClassName();

	/**
	 * Gets the singleton index class dir.
	 *
	 * @return the singleton index class dir
	 */
	public String getSingletonIndexClassDir();







	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the host.
	 *
	 * @param host the new host
	 */
	public void setHost(String host);

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username);

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password);

	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(String port);

	/**
	 * Sets the namespace.
	 *
	 * @param namespace the new namespace
	 */
	public void setNamespace(String namespace);

	/**
	 * Sets the src working directory.
	 *
	 * @param srcWorkingDirectory the new src working directory
	 */
	public void setSrcWorkingDirectory(String srcWorkingDirectory);

	/**
	 * Sets the bin working directory.
	 *
	 * @param binWorkingDirectory the new bin working directory
	 */
	public void setBinWorkingDirectory(String binWorkingDirectory);

	/**
	 * Sets the src sub dir.
	 *
	 * @param srcSubDir the new src sub dir
	 */
	public void setSrcSubDir(String srcSubDir);

	/**
	 * Sets the bin sub dir.
	 *
	 * @param binSubDir the new bin sub dir
	 */
	public void setBinSubDir(String binSubDir);

	/**
	 * Sets the mapping class sub dir.
	 *
	 * @param mappingClassSubDir the new mapping class sub dir
	 */
	public void setMappingClassSubDir(String mappingClassSubDir);

	/**
	 * Sets the table class suffix.
	 *
	 * @param tableClassSuffix the new table class suffix
	 */
	public void setTableClassSuffix(String tableClassSuffix);

	/**
	 * Sets the table class prefix.
	 *
	 * @param tableClassprefix the new table class prefix
	 */
	public void setTableClassPrefix(String tableClassprefix);

	/**
	 * Sets the constructor visibility nullary.
	 *
	 * @param constructorVisibilityNullary the new constructor visibility nullary
	 */
	public void setConstructorVisibilityNullary(String constructorVisibilityNullary);

	/**
	 * Sets the constructor visibility alias.
	 *
	 * @param constructorVisibilityAlias the new constructor visibility alias
	 */
	public void setConstructorVisibilityAlias(String constructorVisibilityAlias);

	/**
	 * Sets the constructor visibility name.
	 *
	 * @param constructorVisibilityName the new constructor visibility name
	 */
	public void setConstructorVisibilityName(String constructorVisibilityName);

	/**
	 * Sets the constructor visibility schema.
	 *
	 * @param constructorVisibilitySchema the new constructor visibility schema
	 */
	public void setConstructorVisibilitySchema(String constructorVisibilitySchema);

	/**
	 * Sets the constructor visibility schema alias.
	 *
	 * @param constructorVisibilitySchemaAlias the new constructor visibility schema alias
	 */
	public void setConstructorVisibilitySchemaAlias(String constructorVisibilitySchemaAlias);

	/**
	 * Sets the constructor visibility name alias.
	 *
	 * @param constructorVisibilityNameAlias the new constructor visibility name alias
	 */
	public void setConstructorVisibilityNameAlias(String constructorVisibilityNameAlias);

	/**
	 * Sets the constructor visibility schema name.
	 *
	 * @param constructorVisibilitySchemaName the new constructor visibility schema name
	 */
	public void setConstructorVisibilitySchemaName(String constructorVisibilitySchemaName);

	/**
	 * Sets the constructor visibility schema name alias.
	 *
	 * @param constructorVisibilitySchemaNameAlias the new constructor visibility schema name alias
	 */
	public void setConstructorVisibilitySchemaNameAlias(String constructorVisibilitySchemaNameAlias);

	/**
	 * Sets the schema inclusion patterns.
	 *
	 * @param schemaInclusionPatterns the new schema inclusion patterns
	 */
	public void setSchemaInclusionPatterns(String schemaInclusionPatterns);

	/**
	 * Sets the schema exclusion patterns.
	 *
	 * @param schemaExclusionPatterns the new schema exclusion patterns
	 */
	public void setSchemaExclusionPatterns(String schemaExclusionPatterns);

	/**
	 * Sets the table inclusion patterns.
	 *
	 * @param schemaInclusionPatterns the new table inclusion patterns
	 */
	public void setTableInclusionPatterns(String schemaInclusionPatterns);

	/**
	 * Sets the table exclusion patterns.
	 *
	 * @param schemaExclusionPatterns the new table exclusion patterns
	 */
	public void setTableExclusionPatterns(String schemaExclusionPatterns);

	/**
	 * Sets the creates the singleton index class.
	 *
	 * @param createSingletonIndexClass the new creates the singleton index class
	 */
	public void setCreateSingletonIndexClass(String createSingletonIndexClass);

	/**
	 * Sets the singleton index class name.
	 *
	 * @param singletonIndexClassName the new singleton index class name
	 */
	public void setSingletonIndexClassName(String singletonIndexClassName);

	/**
	 * Sets the singleton index class dir.
	 *
	 * @param singletonIndexClassDir the new singleton index class dir
	 */
	public void setSingletonIndexClassDir(String singletonIndexClassDir);


	/**
	 * The Class Util.
	 */
	public static abstract class Util
	{

		/** The Constant scol. */
		protected static final String scol = ";";

		/** The Constant commma. */
		protected static final String commma = ",";

		/**
		 * Parses the schema patterns string.
		 *
		 * @param patternsString the patterns string
		 * @return the string[]
		 */
		protected static String[] parseSchemaPatternsString(final String patternsString)
		{
			if(patternsString == null || patternsString.equals("")) {
				return new String[]{};
			}
			return patternsString.replaceAll("\\s", "").replaceAll(scol, commma).split(commma);
		}

		/**
		 * Parses the schema inclusion patterns string.
		 *
		 * @param rTask the r task
		 * @return the string[]
		 */
		public static String[] parseSchemaInclusionPatternsString(final TableRetrospectionTask rTask){
			return parseSchemaPatternsString(rTask.getSchemaInclusionPatterns());
		}

		/**
		 * Parses the schema exclusion patterns string.
		 *
		 * @param rTask the r task
		 * @return the string[]
		 */
		public static String[] parseSchemaExclusionPatternsString(final TableRetrospectionTask rTask){
			return parseSchemaPatternsString(rTask.getSchemaExclusionPatterns());
		}

		/**
		 * Creates the mapping generator configuration.
		 *
		 * @param rTask the r task
		 * @return the mapping generator. configuration
		 */
		public static MappingGenerator.Configuration createMappingGeneratorConfiguration(final TableRetrospectionTask rTask){
			final MappingGenerator.Configuration config = new MappingGenerator.Configuration();
			if(rTask.getSrcWorkingDirectory() != null)	config.setSrcWorkingDirectory(rTask.getSrcWorkingDirectory());
			if(rTask.getBinWorkingDirectory() != null)	config.setBinWorkingDirectory(rTask.getBinWorkingDirectory());
			if(rTask.getSrcSubDir() != null) 			config.setSrcSubDir(rTask.getSrcSubDir());
			if(rTask.getBinSubDir() != null) 			config.setBinSubDir(rTask.getBinSubDir());
			if(rTask.getMappingClassSubDir() != null)	config.setMappingClassSubDir(rTask.getMappingClassSubDir());
			if(rTask.getTableClassSuffix() != null) 	config.setTableClassSuffix(rTask.getTableClassSuffix());
			if(rTask.getTableClassPrefix() != null) 	config.setTableClassPrefix(rTask.getTableClassPrefix());

			if(rTask.getSingletonIndexClassName() != null) config.setSingletonIndexClassName(rTask.getSingletonIndexClassName());
			if(rTask.getSingletonIndexClassDir() != null)  config.setSingletonIndexClassDir(rTask.getSingletonIndexClassDir());
			config.setCreateSingletonIndexClass(rTask.isCreateSingletonIndexClass());

			return config;
		}

		/**
		 * Creates the table retrospection.
		 *
		 * @param <A> the generic type
		 * @param t the t
		 * @param dbmsAdaptor the dbms adaptor
		 * @return the table retrospection
		 */
		public static <A extends DbmsAdaptor<A>> TableRetrospection<A> createTableRetrospection(
			final TableRetrospectionTask t, final A dbmsAdaptor
		)
		{
			final DbmsConnectionInformation<A> conInfo = dbmsAdaptor.createConnectionInformation(
				t.getHost(), Integer.parseInt(t.getPort()), t.getUsername(), t.getPassword(), t.getNamespace(), t.getProperties()
			);
			SQL.connect(new ConnectionProvider.Body<A>(conInfo));
			final MappingGenerator.Configuration config = createMappingGeneratorConfiguration(t);
			final TableRetrospection<A> tr = new TableRetrospection<A>(new MappingGenerator(config));
			return tr;
		}

		/**
		 * Creates the mapping classes for schema pattern.
		 *
		 * @param <A> the generic type
		 * @param t the t
		 * @param dbmsAdaptor the dbms adaptor
		 * @param compile the compile
		 * @return the list
		 */
		public static <A extends DbmsAdaptor<A>> List<String> createMappingClassesForSchemaPattern(
			final TableRetrospectionTask t, final A dbmsAdaptor, final boolean compile
		)
		{
			final TableRetrospection<A> tr = createTableRetrospection(t, dbmsAdaptor);
			final String[] includePatterns = parseSchemaInclusionPatternsString(t);
			final String[] excludePatterns = parseSchemaExclusionPatternsString(t);
			final List<String> createdClasses = tr.createMappingClassesForSchemaPattern(includePatterns, excludePatterns, compile);

			if(t.isCreateSingletonIndexClass()){
				String indexClassName = tr.createSingletonIndexClass(compile);
				createdClasses.add(indexClassName);
			}
			return createdClasses;
		}
	}

}
