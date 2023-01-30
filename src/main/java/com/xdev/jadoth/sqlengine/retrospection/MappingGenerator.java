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

import static com.xdev.jadoth.codegen.java.Java.Lang.$final;
import static com.xdev.jadoth.codegen.java.Java.Lang.$new;
import static com.xdev.jadoth.codegen.java.Java.Lang.$protected;
import static com.xdev.jadoth.codegen.java.Java.Lang.$public;
import static com.xdev.jadoth.codegen.java.Java.Lang.$static;
import static com.xdev.jadoth.codegen.java.Java.Lang.$super;
import static com.xdev.jadoth.codegen.java.JavaSyntax.callMethod;
import static com.xdev.jadoth.lang.reflection.JaReflect.getFullClassName;
import static com.xdev.jadoth.lang.reflection.JaReflect.getMemberByLabel;
import static com.xdev.jadoth.lang.reflection.JaReflect.getMembersByLabel;
import static com.xdev.jadoth.lang.reflection.JaReflect.isOfClassType;
import static com.xdev.jadoth.sqlengine.SQL.LANG.AS;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.comma_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.dot;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.is;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.xdev.jadoth.codegen.java.Java;
import com.xdev.jadoth.codegen.java.JavaRuntimeCompiler;
import com.xdev.jadoth.codegen.java.JavaSourceCode;
import com.xdev.jadoth.codegen.java.JavaSyntax;
import com.xdev.jadoth.codegen.java.JavaRuntimeCompiler.CompilerStatusCode;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.internal.SqlField;
import com.xdev.jadoth.sqlengine.internal.tables.SqlIndex;
import com.xdev.jadoth.sqlengine.internal.tables.SqlPrimaryKey;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTable;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.retrospection.definition.ColumnDefinition;
import com.xdev.jadoth.sqlengine.retrospection.definition.IndexDefinition;
import com.xdev.jadoth.sqlengine.retrospection.definition.TableDefinition;
import com.xdev.jadoth.sqlengine.util.SqlEngineLabels;





/**
 * The Class MappingGenerator.
 */
public class MappingGenerator extends SqlEngineLabels
{
	
	/** The Constant CONSTRUCTOR_PARAMETERNAME_SCHEMA. */
	protected static final String CONSTRUCTOR_PARAMETERNAME_SCHEMA = "schema";
	
	/** The Constant CONSTRUCTOR_PARAMETERNAME_NAME. */
	protected static final String CONSTRUCTOR_PARAMETERNAME_NAME = "name";
	
	/** The Constant CONSTRUCTOR_PARAMETERNAME_ALIAS. */
	protected static final String CONSTRUCTOR_PARAMETERNAME_ALIAS = "alias";

	/** The Constant STATIC_SQLSCHEMA. */
	protected static final String STATIC_SQLSCHEMA 		= "defaultSchema";
	
	/** The Constant STATIC_SQLNAME. */
	protected static final String STATIC_SQLNAME 		= "defaultName";
	
	/** The Constant STATIC_DEFAULTALIAS. */
	protected static final String STATIC_DEFAULTALIAS 	= "defaultAlias";
	
	/** The Constant STATIC_DECLAREDSQLCOLS. */
	protected static final String STATIC_DECLAREDSQLCOLS = "declaredSqlColumns";

	/** The Constant GENERATION_NOTE. */
	protected static final String GENERATION_NOTE = "Generated by jadoth SQL Engine";
	
	/** The Constant FIELDNAME_PRIMARY_KEY. */
	protected static final String FIELDNAME_PRIMARY_KEY = "PrimaryKey";
	
	/** The Constant FIELDNAME_INDEX. */
	protected static final String FIELDNAME_INDEX = "index";



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	/** The table def. */
	private TableDefinition tableDef;
	
	/** The config. */
	private Configuration config;
	
	/** The class name. */
	private String className;
	
	/** The package name. */
	private String packageName;
	
	/** The package dir. */
	private String packageDir;

	/** The created classes canoncical names. */
	private List<String> createdClassesCanoncicalNames = new ArrayList<String>();
	
	/** The created classes table name. */
	private List<String> createdClassesTableName = new ArrayList<String>();


	/** The rc. */
	private JavaRuntimeCompiler rc = new JavaRuntimeCompiler();



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new mapping generator.
	 * 
	 */
	public MappingGenerator() {
		super();
		this.config = new Configuration();
	}
	
	/**
	 * Instantiates a new mapping generator.
	 * 
	 * @param config the config
	 */
	public MappingGenerator(final Configuration config) {
		super();
		this.config = config;
		this.setTableDef(null);
	}
	
	/**
	 * Instantiates a new mapping generator.
	 * 
	 * @param tableDef the table def
	 */
	public MappingGenerator(final TableDefinition tableDef) {
		this();
		this.setTableDef(tableDef);
	}
	
	/**
	 * Instantiates a new mapping generator.
	 * 
	 * @param tableDef the table def
	 * @param config the config
	 */
	public MappingGenerator(final TableDefinition tableDef, final Configuration config) {
		this(config);
		this.setTableDef(tableDef);
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the table def.
	 * 
	 * @return the tableDef
	 */
	public TableDefinition getTableDef() {
		return this.tableDef;
	}
	
	/**
	 * Gets the config.
	 * 
	 * @return the config
	 */
	public Configuration getConfig() {
		return this.config;
	}
	
	/**
	 * Gets the created classes canoncical names.
	 * 
	 * @return the createdClassesCanoncicalNames
	 */
	public List<String> getCreatedClassesCanoncicalNames() {
		return this.createdClassesCanoncicalNames;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the config.
	 * 
	 * @param config the config to set
	 * @return the mapping generator
	 */
	public MappingGenerator setConfig(final Configuration config) {
		this.config = config;
		return this;
	}
	
	/**
	 * Sets the table def.
	 * 
	 * @param tableDef the tableDef to set
	 * @return the mapping generator
	 */
	public MappingGenerator setTableDef(final TableDefinition tableDef) {
		this.tableDef = tableDef;
		if(tableDef != null){
			this.className = this.createMappingClassName(tableDef.getName());
			this.packageName = this.createPackageName(tableDef.getSchema());
			this.packageDir = transformPackageToDirectory(this.packageName);
		}
		else {
			this.className = null;
			this.packageName = null;
			this.packageDir = null;
		}
		return this;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	/**
	 * Clear created classes list.
	 * 
	 * @return the int
	 */
	public int clearCreatedClassesList() {
		final int size = this.createdClassesCanoncicalNames.size();
		this.createdClassesCanoncicalNames = new ArrayList<String>();
		this.createdClassesTableName = new ArrayList<String>();
		return size;
	}



	/**
	 * Gets the canonical name last element.
	 * 
	 * @param canonicalClassName the canonical class name
	 * @return the canonical name last element
	 */
	private static final String getCanonicalNameLastElement(final String canonicalClassName){
		if(canonicalClassName == null) return null;

		final int lastDotIndex = canonicalClassName.lastIndexOf('.');
		if(lastDotIndex < 0){
			return canonicalClassName;
		}
		return canonicalClassName.substring(lastDotIndex+1);
	}


	/**
	 * Creates the singleton index class.
	 * 
	 * @param compile the compile
	 * @return the string
	 */
	public String createSingletonIndexClass(final boolean compile)
	{
		if(this.createdClassesCanoncicalNames == null || this.createdClassesCanoncicalNames.size() == 0){
			return null;
		}

		final String subDir = this.config.getSingletonIndexClassDir();
		final String packagePath = transformDirectoryToPackage(subDir);
		final String className = this.config.getSingletonIndexClassName();

		final String canonicalClassName = packagePath==null?className:packagePath+"."+className;

		final File srcFile = new File(this.config.getSourceBaseDirectory(), subDir==null?"":subDir);
		final File binFile = compile?this.config.getBinaryBaseDirectory():null;

		final String source = this.createSingletonIndexClassCode(packagePath, className);

		final CompilerStatusCode message = this.writeClassFile(compile, srcFile, binFile, className, source);
		System.out.println(message);

		return canonicalClassName;
	}

	/**
	 * Creates the singleton index class code.
	 * 
	 * @param packagePath the package path
	 * @param className the class name
	 * @return the string
	 */
	public final String createSingletonIndexClassCode(final String packagePath, final String className)
	{
		final JavaSourceCode code = new JavaSourceCode();

		final String[] tableClassNames = this.createdClassesCanoncicalNames.toArray(new String[0]);
		final String[] tableNames = this.createdClassesTableName.toArray(new String[0]);



		  /////////////////////////////////////////////////////////////////////
		 // code generation starts here //
		/////////////////////////////////
		if(packagePath != null)code.package_(packagePath);
		code.blankLine();
		code.import_(Generated.class);
		for(int i = 0; i < tableClassNames.length; i++) {
			code.import_(tableClassNames[i]);
		}
		code.blankLine();
		code.addGenerationAnnotation(0, new Class<?>[] {this.getClass()}, GENERATION_NOTE);
		code.modifier($public).class_(className, null);

		String tableClassName = null;
		for(int i = 0; i < tableClassNames.length; i++) {
			tableClassName = getCanonicalNameLastElement(tableClassNames[i]);
			code.modifier($public, $static, $final).append(
				tableClassName, tableNames[i], is, $new, tableClassName+"()"
			).endLine();
		}

		code.blockEnd();
		return code.toString();
	}


	/**
	 * Write class file.
	 * 
	 * @param compile the compile
	 * @param srcFile the src file
	 * @param binFile the bin file
	 * @param filename the filename
	 * @param source the source
	 * @return the compiler status code
	 */
	protected CompilerStatusCode writeClassFile(final boolean compile, final File srcFile, final File binFile, final String filename, final String source){
		this.rc.setSrcFolder(srcFile.getAbsolutePath());
		this.rc.setBinFolder(compile?binFile.getAbsolutePath():null);
		if(this.config.getTableClassFileType() != null){
			this.rc.setFileSuffix(this.config.getTableClassFileType());
		}
		return this.rc.writeClassFile(source, filename, compile);
	}

	/**
	 * Write class file.
	 * 
	 * @param compile the compile
	 * @return the mapping generator
	 */
	public MappingGenerator writeClassFile(final boolean compile)
	{
		final File srcFile = new File(this.config.getSourceBaseDirectory(), this.packageDir);
		final File binFile = compile?this.config.getBinaryBaseDirectory():null;
		final String source = this.createSqlTableDefinitionCode();

		final CompilerStatusCode message = this.writeClassFile(compile, srcFile, binFile, this.className, source);
		System.out.println(message);

		if(message.isSuccess()){
			//add class only on success
			this.createdClassesCanoncicalNames.add(this.packageName+"."+this.className);
			this.createdClassesTableName.add(this.tableDef.getName());
		}
		return this;
	}

	/**
	 * Transform package to directory.
	 * 
	 * @param packageString the package string
	 * @return the string
	 */
	protected static String transformPackageToDirectory(final String packageString){
		if(packageString == null) return null;
		return packageString.replaceAll("\\.", "/");
	}
	
	/**
	 * Transform directory to package.
	 * 
	 * @param directoryString the directory string
	 * @return the string
	 */
	protected static String transformDirectoryToPackage(final String directoryString){
		if(directoryString == null) return null;
		return directoryString.replaceAll("/", ".");
	}

	/**
	 * Creates the package name.
	 * 
	 * @param schema the schema
	 * @return the string
	 */
	public String createPackageName(final String schema)
	{
		final String packageName = this.config.getMappingClassSubDir().replaceAll("/", dot);
		final StringBuilder sb = new StringBuilder(64);
		if(packageName != null){
			sb.append(packageName);
			if(schema != null){
				sb.append(dot);
			}
		}
		if(schema != null){
			sb.append(schema.replaceAll("_", dot));
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * Creates the mapping class name.
	 * 
	 * @param tableName the table name
	 * @return the string
	 */
	public String createMappingClassName(final String tableName){
		final String prefix = this.config.getTableClassPrefix();
		final String suffix = this.config.getTableClassSuffix();
		final StringBuilder sb = new StringBuilder(64);
		if(prefix != null){
			sb.append(prefix);
		}
		sb.append(tableName);
		if(suffix != null){
			sb.append(suffix);
		}
		return sb.toString();
	}

	/**
	 * Generate constructor.
	 * 
	 * @param code the code
	 * @param visibility the visibility
	 * @param schema the schema
	 * @param name the name
	 * @param alias the alias
	 */
	@SuppressWarnings("unchecked")
	protected void generateConstructor(
		final JavaSourceCode code,
		final Java.Visibility visibility,
		final boolean schema,
		final boolean name,
		final boolean alias
	)
	{
		if(visibility == null) return;

		code.modifier(visibility.toString());
		final List<String> parameters = new ArrayList<String>(3);
		final String String = String.class.getSimpleName();

		if(schema){
			parameters.add($final+_+String+_+CONSTRUCTOR_PARAMETERNAME_SCHEMA);
		}
		if(name){
			parameters.add($final+_+String+_+CONSTRUCTOR_PARAMETERNAME_NAME);
		}
		if(alias){
			parameters.add($final+_+String+_+CONSTRUCTOR_PARAMETERNAME_ALIAS);
		}

		code.constructor_(this.className, parameters.toArray(new String[parameters.size()]));
		code.space().throws_(SQLEngineException.class).space().blockStart();
		code.indent();

		final String params = new StringBuilder(64)
		.append(schema?CONSTRUCTOR_PARAMETERNAME_SCHEMA:STATIC_SQLSCHEMA).append(comma_)
		.append(name?CONSTRUCTOR_PARAMETERNAME_NAME:STATIC_SQLNAME).append(comma_)
		.append(alias?CONSTRUCTOR_PARAMETERNAME_ALIAS:STATIC_DEFAULTALIAS).toString();

		code.super_(params).endLine();
		if(visibility.equals(Java.Visibility.public_)){
			code.indent().append(callMethod("util", "initialize")).endLine();
		}
		code.blockEnd();
	}

	/**
	 * Generate as.
	 * 
	 * @param code the code
	 */
	protected void generateAS(final JavaSourceCode code)
	{
		code.modifier(Java.Visibility.public_.toString()).append(this.className).append(_)
		.method(AS, "final String newAlias")
		.blockNewLineStart()
		.addLine(JavaSourceCode.return_(par+this.className+rap+$super+dot+AS+par+"newAlias"+rap))
		.blockEnd()
		;
	}

	/**
	 * Generate constructors.
	 * 
	 * @param code the code
	 */
	protected void generateConstructors(final JavaSourceCode code){
		this.generateConstructor(code, this.config.visibilityConstructorNullary(), 		false, false, false);
		this.generateConstructor(code, this.config.visibilityConstructorAlias(), 			false, false, true );
		this.generateConstructor(code, this.config.visibilityConstructorName(), 			false, true , false);
		this.generateConstructor(code, this.config.visibilityConstructorSchema(), 		true , false, false);
		this.generateConstructor(code, this.config.visibilityConstructorSchemaAlias(), 	true , false, true );
		this.generateConstructor(code, this.config.visibilityConstructorSchemaName(), 	true , true , false);
		this.generateConstructor(code, this.config.visibilityConstructorNameAlias(), 		false, true , true );
		this.generateConstructor(code, this.config.visibilityConstructorSchemaNameAlias(), true, true , true );
	}

	/**
	 * Gets the super class index inner class.
	 * 
	 * @return the super class index inner class
	 */
	@SuppressWarnings("unchecked")
	protected Class<? extends SqlTable.Indices> getSuperClassIndexInnerClass(){
		final Class<?>[] allInnerClasses = this.config.superClass.getDeclaredClasses();
		for(final Class<?> c : allInnerClasses) {
			if(isOfClassType(c, SqlTable.Indices.class)){
				return (Class<? extends SqlTable.Indices>)c;
			}
		}
		return null;
	}

	/**
	 * Creates the column string array elements.
	 * 
	 * @param java the java
	 * @param columns the columns
	 * @return the string
	 */
	protected static String createColumnStringArrayElements(final JavaSyntax java, final ColumnDefinition[] columns)
	{
		final StringBuilder columnList = new StringBuilder(1024);
		for(int c = 0; c < columns.length; c++) {
			columnList.append(c>0?comma_:"").append(java.$quote(columns[c].getName()));
		}
		return columnList.toString();
	}

	/**
	 * Creates the default alias value.
	 * 
	 * @return the string
	 */
	protected static String createDefaultAliasValue(){
		final Method[] guessAliasMethods = getMembersByLabel(LABEL_SQL_util_guessAlias, SQL.util.class.getMethods());
		final Method guessAlias = getMemberByLabel(LABEL_1Param, guessAliasMethods);

		return callMethod(getFullClassName(SQL.util.class), guessAlias.getName(), STATIC_SQLNAME);
	}

	/**
	 * Creates the sql table definition code.
	 * 
	 * @return the string
	 */
	public final String createSqlTableDefinitionCode()
	{
		final JavaSourceCode code = new JavaSourceCode();
		final JavaSyntax java = code.getSyntax();

		String schema = this.tableDef.getSchemaInDb();
		final String tableName = this.tableDef.getNameInDb();
		final String defaultAlias = createDefaultAliasValue();
		if(schema.equals("")){
			schema = null;
		}

		final ColumnDefinition[] columns = this.tableDef.getColumns().toArray(new ColumnDefinition[0]);
		final IndexDefinition[] indices = this.tableDef.getIndices().toArray(new IndexDefinition[0]);
		final int columnPaddingSpaces = this.tableDef.determinePaddingSpacesColumns();
		final int indexPaddingSpaces = this.tableDef.determinePaddingSpacesIndices();

		final String colList = createColumnStringArrayElements(java, columns);

		final boolean hasNonPkey = this.tableDef.hasNonPkeyIndices();
		final boolean hasPrimKey = this.tableDef.hasPrimaryKey();

		final Class<? extends SqlTable> superClass = this.config.superClass;
		final String superClassGenericsParam = superClass.getTypeParameters().length>0?this.className:null;
		final Java.Array d1String = new Java.Array(String.class, 1);
		final Class<?> cString = String.class;


		  /////////////////////////////////////////////////////////////////////
		 // code generation starts here //
		/////////////////////////////////
		code.package_(this.packageName);
		code.blankLine();
		code.import_(Generated.class);
		code.import_(superClass);
		code.import_(SQLEngineException.class);
		code.import_(SQL.class);
		code.import_(SqlField.class);
		if(hasNonPkey) code.import_(SqlIndex.class);
		if(hasPrimKey) code.import_(SqlPrimaryKey.class);
		code.blankLine();
		code.addGenerationAnnotation(0, new Class<?>[] {this.getClass()}, GENERATION_NOTE);
		code.modifier($public).class_(this.className, superClass, superClassGenericsParam);
		code.addLine("private static final long serialVersionUID = 1L;").blankLine();
		code.modifier($public, $static, $final).varDecl_init(cString, STATIC_SQLSCHEMA, schema).endLine();
		code.modifier($public, $static, $final).varDecl_init(cString, STATIC_SQLNAME, tableName).endLine();
		code.modifier($public, $static, $final).varDecl_assign(cString, STATIC_DEFAULTALIAS, defaultAlias).endLine();
		code.modifier($public, $static, $final).varDecl_init(d1String, STATIC_DECLAREDSQLCOLS, colList).endLine();
		code.blankLine();
		code.blankLine();
		for(final ColumnDefinition c : columns) {
			code.indent();
			c.assembleJavaSourceDefinition(code.getCode(), columnPaddingSpaces);
			code.endLine();
		}
		code.blankLine();
		if(hasNonPkey || hasPrimKey) {
			final Class<?> cIndices = this.getSuperClassIndexInnerClass();
			code.modifier($public, $final).varDecl_init(cIndices, FIELDNAME_INDEX, "").endLine();
			code.modifier($public).class_(cIndices.getSimpleName(), null, cIndices, null, this.className, (String[])null);
			code.modifier($protected).constructor_(cIndices.getSimpleName(), (String[])null).blockStart();
			code.indent().super_().endLine();
			code.blockEnd();
			code.blankLine();
			for (final IndexDefinition i : indices) {
				code.indent();
				i.assembleJavaSourceDefinition(code.getCode(), indexPaddingSpaces);
				code.endLine();
			}
			code.blockEnd();
		}
		code.blankLine();
		code.blankLine();
		this.generateConstructors(code);
		code.blankLine();
		this.generateAS(code);
		code.blankLine();
		code.blockEnd();
		return code.toString();
	}



	///////////////////////////////////////////////////////////////////////////
	// inner classes //
	//////////////////
	/**
	 * The Class Configuration.
	 */
	public static class Configuration
	{
		
		/** The Constant defaultWorkingDirectory. */
		public static final String defaultWorkingDirectory = System.getProperty("user.dir");
		
		/** The Constant defaultSrcDir. */
		public static final String defaultSrcDir = "src";
		
		/** The Constant defaultBinDir. */
		public static final String defaultBinDir = "bin";
		
		/** The Constant defaultMappingClassSubDir. */
		public static final String defaultMappingClassSubDir = "sqltables";
		
		/** The Constant defaultSingletonIndexClassName. */
		public static final String defaultSingletonIndexClassName = "SqlTables"; //if no actual name or prefix is given!

		/** The super class. */
		private Class<? extends SqlTable> superClass = SqlTable.class;

		/** The src working directory. */
		private String srcWorkingDirectory = defaultWorkingDirectory;
		
		/** The bin working directory. */
		private String binWorkingDirectory = defaultWorkingDirectory;
		
		/** The src sub dir. */
		private String srcSubDir = defaultSrcDir;
		
		/** The bin sub dir. */
		private String binSubDir = defaultBinDir;
		
		/** The mapping class sub dir. */
		private String mappingClassSubDir = defaultMappingClassSubDir;

		/** The table class suffix. */
		private String tableClassSuffix = "";
		
		/** The table class prefix. */
		private String tableClassPrefix = "Tbl";
		
		/** The table class file type. */
		private String tableClassFileType = null; //null means default ("java")


		/** The create singleton index class. */
		private boolean createSingletonIndexClass = true;
		
		/** The singleton index class name. */
		private String singletonIndexClassName = null;
		
		/** The singleton index class dir. */
		private String singletonIndexClassDir = null;

		/** The constructor nullary. */
		private Java.Visibility constructorNullary 			= Java.Visibility.public_;
		
		/** The constructor schema. */
		private Java.Visibility constructorSchema 			= null;
		
		/** The constructor name. */
		private Java.Visibility constructorName 			= null;
		
		/** The constructor alias. */
		private Java.Visibility constructorAlias 			= Java.Visibility.public_;
		
		/** The constructor schema name. */
		private Java.Visibility constructorSchemaName 		= null;
		
		/** The constructor schema alias. */
		private Java.Visibility constructorSchemaAlias 		= null;
		
		/** The constructor name alias. */
		private Java.Visibility constructorNameAlias 		= null;
		
		/** The constructor schema name alias. */
		private Java.Visibility constructorSchemaNameAlias 	= Java.Visibility.protected_;

		/**
		 * Sets the constructor configuration.
		 * 
		 * @param nullary the nullary
		 * @param alias the alias
		 * @param name the name
		 * @param schema the schema
		 * @param schemaAlias the schema alias
		 * @param nameAlias the name alias
		 * @param schemaName the schema name
		 * @param schemaNameAlias the schema name alias
		 * @return the configuration
		 */
		public Configuration setConstructorConfiguration(
			final Java.Visibility nullary,
			final Java.Visibility alias,
			final Java.Visibility name,
			final Java.Visibility schema,
			final Java.Visibility schemaAlias,
			final Java.Visibility nameAlias,
			final Java.Visibility schemaName,
			final Java.Visibility schemaNameAlias
		)
		{
			this.constructorNullary 		= nullary;
			this.constructorAlias 			= alias;
			this.constructorName 			= name;
			this.constructorSchema 			= schema;
			this.constructorSchemaAlias 	= schemaAlias;
			this.constructorNameAlias 		= nameAlias;
			this.constructorSchemaName 		= schemaName;
			this.constructorSchemaNameAlias = schemaNameAlias;
			return this;
		}








		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////
		/**
		 * Gets the super class.
		 * 
		 * @return the superClass
		 */
		public Class<? extends SqlTableIdentity> getSuperClass() {
			return this.superClass;
		}
		
		/**
		 * Gets the src working directory.
		 * 
		 * @return the workingDirectory
		 */
		public String getSrcWorkingDirectory() {
			return this.srcWorkingDirectory;
		}
		
		/**
		 * Gets the src sub dir.
		 * 
		 * @return the sourceSubDir
		 */
		public String getSrcSubDir() {
			return this.srcSubDir;
		}
		
		/**
		 * Gets the mapping class sub dir.
		 * 
		 * @return the mappingClassSubDir
		 */
		public String getMappingClassSubDir() {
			return this.mappingClassSubDir;
		}
		
		/**
		 * Gets the source base directory.
		 * 
		 * @return the source base directory
		 */
		public File getSourceBaseDirectory(){
			return new File(this.srcWorkingDirectory, this.srcSubDir);
		}
		
		/**
		 * Gets the binary base directory.
		 * 
		 * @return the binary base directory
		 */
		public File getBinaryBaseDirectory(){
			return new File(this.binWorkingDirectory, this.binSubDir);
		}
		
		/**
		 * Gets the source target directory.
		 * 
		 * @return the source target directory
		 */
		public File getSourceTargetDirectory(){
			return new File(this.getSourceBaseDirectory(), this.mappingClassSubDir);
		}
		
		/**
		 * Gets the table class suffix.
		 * 
		 * @return the tableClassSuffix
		 */
		public String getTableClassSuffix() {
			return this.tableClassSuffix;
		}
		
		/**
		 * Gets the table class prefix.
		 * 
		 * @return the tableClassprefix
		 */
		public String getTableClassPrefix() {
			return this.tableClassPrefix;
		}
		
		/**
		 * Visibility constructor nullary.
		 * 
		 * @return the constructorNullary
		 */
		public Java.Visibility visibilityConstructorNullary() {
			return this.constructorNullary;
		}
		
		/**
		 * Visibility constructor alias.
		 * 
		 * @return the constructorAlias
		 */
		public Java.Visibility visibilityConstructorAlias() {
			return this.constructorAlias;
		}
		
		/**
		 * Visibility constructor name.
		 * 
		 * @return the constructorName
		 */
		public Java.Visibility visibilityConstructorName() {
			return this.constructorName;
		}
		
		/**
		 * Visibility constructor schema.
		 * 
		 * @return the constructorSchema
		 */
		public Java.Visibility visibilityConstructorSchema() {
			return this.constructorSchema;
		}
		
		/**
		 * Visibility constructor schema alias.
		 * 
		 * @return the constructorSchemaAlias
		 */
		public Java.Visibility visibilityConstructorSchemaAlias() {
			return this.constructorSchemaAlias;
		}
		
		/**
		 * Visibility constructor name alias.
		 * 
		 * @return the constructorNameAlias
		 */
		public Java.Visibility visibilityConstructorNameAlias() {
			return this.constructorNameAlias;
		}
		
		/**
		 * Visibility constructor schema name.
		 * 
		 * @return the constructorSchemaName
		 */
		public Java.Visibility visibilityConstructorSchemaName() {
			return this.constructorSchemaName;
		}
		
		/**
		 * Visibility constructor schema name alias.
		 * 
		 * @return the constructorSchemaNameAlias
		 */
		public Java.Visibility visibilityConstructorSchemaNameAlias() {
			return this.constructorSchemaNameAlias;
		}
		
		/**
		 * Checks if is triple constructor present.
		 * 
		 * @return true, if is triple constructor present
		 */
		public boolean isTripleConstructorPresent(){
			return this.constructorSchemaNameAlias != null;
		}		
		/**
		 * Checks if is creates the singleton index class.
		 * 
		 * @return the createSingletonIndexClass
		 */
		public boolean isCreateSingletonIndexClass() {
			return this.createSingletonIndexClass;
		}
		
		/**
		 * Gets the singleton index class name.
		 * 
		 * @return the singletonIndexClassName
		 */
		public String getSingletonIndexClassName() {
			if(this.singletonIndexClassName != null){
				return this.singletonIndexClassName;
			}
			else if(this.tableClassPrefix != null){
				return this.tableClassPrefix;
			}
			else {
				return defaultSingletonIndexClassName;
			}
		}
		
		/**
		 * Gets the singleton index class dir.
		 * 
		 * @return the singletonIndexClassDir
		 */
		public String getSingletonIndexClassDir() {
			return this.singletonIndexClassDir;
		}
		
		/**
		 * Gets the table class file type.
		 * 
		 * @return the tableClassFileType
		 */
		public String getTableClassFileType() {
			return this.tableClassFileType;
		}


		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
		/**
		 * Sets the super class.
		 * 
		 * @param superClass the superClass to set
		 * @return the configuration
		 */
		public Configuration setSuperClass(final Class<? extends SqlTable> superClass) {
			this.superClass = superClass;
			return this;
		}
		
		/**
		 * Sets the src working directory.
		 * 
		 * @param srcWorkingDirectory the new src working directory
		 */
		public void setSrcWorkingDirectory(final String srcWorkingDirectory) {
			this.srcWorkingDirectory = srcWorkingDirectory;
		}
		
		/**
		 * Sets the src sub dir.
		 * 
		 * @param srcSubDir the new src sub dir
		 */
		public void setSrcSubDir(final String srcSubDir) {
			this.srcSubDir = srcSubDir;
		}
		
		/**
		 * Sets the bin working directory.
		 * 
		 * @param binWorkingDirectory the new bin working directory
		 */
		public void setBinWorkingDirectory(final String binWorkingDirectory) {
			this.binWorkingDirectory = binWorkingDirectory;
		}
		
		/**
		 * Sets the bin sub dir.
		 * 
		 * @param binSubDir the new bin sub dir
		 */
		public void setBinSubDir(final String binSubDir) {
			this.binSubDir = binSubDir;
		}
		
		/**
		 * Sets the mapping class sub dir.
		 * 
		 * @param mappingClassSubDir the mappingClassSubDir to set
		 */
		public void setMappingClassSubDir(final String mappingClassSubDir) {
			this.mappingClassSubDir = mappingClassSubDir;
		}
		
		/**
		 * Sets the table class suffix.
		 * 
		 * @param tableClassSuffix the tableClassSuffix to set
		 */
		public void setTableClassSuffix(final String tableClassSuffix) {
			this.tableClassSuffix = tableClassSuffix;
		}
		
		/**
		 * Sets the table class prefix.
		 * 
		 * @param tableClassprefix the tableClassprefix to set
		 */
		public void setTableClassPrefix(final String tableClassprefix) {
			this.tableClassPrefix = tableClassprefix;
		}
		
		/**
		 * Sets the creates the singleton index class.
		 * 
		 * @param createSingletonIndexClass the createSingletonIndexClass to set
		 */
		public void setCreateSingletonIndexClass(final boolean createSingletonIndexClass) {
			this.createSingletonIndexClass = createSingletonIndexClass;
		}
		
		/**
		 * Sets the singleton index class name.
		 * 
		 * @param singletonIndexClassName the singletonIndexClassName to set
		 */
		public void setSingletonIndexClassName(final String singletonIndexClassName) {
			this.singletonIndexClassName = singletonIndexClassName;
		}
		
		/**
		 * Sets the singleton index class dir.
		 * 
		 * @param singletonIndexClassDir the singletonIndexClassDir to set
		 */
		public void setSingletonIndexClassDir(final String singletonIndexClassDir) {
			this.singletonIndexClassDir = singletonIndexClassDir;
		}

		/**
		 * Sets the table class file type.
		 * 
		 * @param tableClassFileType the tableClassFileType to set
		 */
		public void setTableClassFileType(final String tableClassFileType) {
			this.tableClassFileType = tableClassFileType;
		}

	}

}
