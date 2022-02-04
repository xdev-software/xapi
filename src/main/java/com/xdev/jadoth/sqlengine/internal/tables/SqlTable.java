
package com.xdev.jadoth.sqlengine.internal.tables;

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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.xdev.jadoth.lang.functional.controlflow.TPredicate;
import com.xdev.jadoth.lang.reflection.JaReflect;
import com.xdev.jadoth.lang.reflection.annotations.Label;
import com.xdev.jadoth.sqlengine.INSERT;
import com.xdev.jadoth.sqlengine.SELECT;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.UPDATE;
import com.xdev.jadoth.sqlengine.SQL.DATATYPE;
import com.xdev.jadoth.sqlengine.SQL.INDEXTYPE;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineInvalidIdentifier;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineTableNotFoundException;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.SqlField;




/**
 * The Class SqlTable.
 *
 * @author Thomas Muenz
 */
public abstract class SqlTable extends SqlTableIdentity
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	/**
	 *
	 */
	private static final long serialVersionUID = 1145485752676926837L;

	/** The Constant LABEL_METHOD_PrimaryKey. */
	public static final String LABEL_METHOD_PrimaryKey 	= "PrimaryKey";

	/** The Constant LABEL_METHOD_ForeignKey. */
	public static final String LABEL_METHOD_ForeignKey 	= "ForeignKey";

	/** The Constant LABEL_METHOD_Index. */
	public static final String LABEL_METHOD_Index 		= "Index";

	/** The Constant LABEL_METHOD_BitmapIndex. */
	public static final String LABEL_METHOD_BitmapIndex = "BitmapIndex";

	/** The Constant LABEL_METHOD_UniqueIndex. */
	public static final String LABEL_METHOD_UniqueIndex = "UniqueIndex";

	/** The Constant NULL. */
	protected static final String NULL 		= SQL.LANG.NULL;

	/** The Constant NOT_NULL. */
	protected static final String NOT_NULL 	= SQL.LANG.NOT_NULL;

	/** The Constant UNIQUE. */
	protected static final String UNIQUE 	= SQL.LANG.UNIQUE;



//	protected static HashMap<Class<? extends SqlBaseTable<?>>, SqlBaseTable<?>> subTables = new HashMap<Class<? extends SqlBaseTable<?>>, SqlBaseTable<?>>();

	  ////////////////////
	 // Static Methods //
	////////////////////

	/**
	 * Creates the column.
	 *
	 * @param type the type
	 * @param length the length
	 * @param precision the precision
	 * @param scale the scale
	 * @param params the params
	 * @return the sql field
	 */
	protected static SqlField createColumn(
		final DATATYPE type, final int length, final Integer precision, final Integer scale, final Object... params
	)
	{
		boolean notNull = false;
		boolean unique = false;
		Object defaultValue = null;
		if(params.length > 0) {
			for(final Object p : params) {
				if(p instanceof String) {
					if(p.equals(NOT_NULL)) {
						notNull = true;
					}
					else if(p.equals(UNIQUE)) {
						unique = true;
					}
					else {
						defaultValue = p;
					}
				}
				else {
					defaultValue = p;
				}
			}
		}

		try {
			return new SqlField(null, type, length, precision, scale, notNull, unique, defaultValue);
		}
		catch(final SQLEngineInvalidIdentifier e) {
			//can never happen because valid java identifiers are always valid SQL identifiers
			return null;
		}
	}

	/**
	 * Creates the column.
	 *
	 * @param name the name
	 * @param type the type
	 * @param length the length
	 * @return the sql field
	 */
	protected static SqlField createColumn(
		final String name,
		final DATATYPE type,
		final int length
	)
	{
		SqlField c = null;
		try {
			c = new SqlField(name, type, length, null, null, false, false, null);
		} catch (final SQLEngineInvalidIdentifier e) {
			//can never happen because valid java identifiers are always valid SQL identifiers
		}
		return c;
	}

	/**
	 * Creates the column.
	 *
	 * @param name the name
	 * @param type the type
	 * @param length the length
	 * @param precision the precision
	 * @param scale the scale
	 * @param notNull the not null
	 * @param unique the unique
	 * @param defaultValue the default value
	 * @return the sql field
	 */
	protected static SqlField createColumn(
		final String name,
		final DATATYPE type,
		final int length,
		final Integer precision,
		final Integer scale,
		final boolean notNull,
		final boolean unique,
		final String defaultValue
	)
	{
		SqlField c = null;
		try {
			c = new SqlField(name, type, length, precision, scale, notNull, unique, defaultValue);
		} catch (final SQLEngineInvalidIdentifier e) {
			//can never happen because valid java identifiers are always valid SQL identifiers
		}
		return c;
	}

	/**
	 * TINYINT.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField TINYINT(final String... params) {
		return createColumn(SQL.DATATYPE.TINYINT, 0, null, null, (Object[])params);
	}

	/**
	 * SMALLINT.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField SMALLINT(final String... params) {
		return createColumn(SQL.DATATYPE.SMALLINT, 0, null, null, (Object[])params);
	}

	/**
	 * INT.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField INT(final String... params) {
		return createColumn(SQL.DATATYPE.INT, 0, null, null, (Object[])params);
	}

	/**
	 * BIGINT.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField BIGINT(final String... params) {
		return createColumn(SQL.DATATYPE.BIGINT, 0, null, null, (Object[])params);
	}

	/**
	 * BOOLEAN.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField BOOLEAN(final String... params) {
		return createColumn(SQL.DATATYPE.BOOLEAN, 0, null, null, (Object[])params);
	}

	/**
	 * CHAR.
	 *
	 * @param length the length
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField CHAR(final int length, final String... params) {
		return createColumn(SQL.DATATYPE.CHAR, length, null, null, (Object[])params);
	}

	/**
	 * VARCHAR.
	 *
	 * @param length the length
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField VARCHAR(final int length, final String... params) {
		return createColumn(SQL.DATATYPE.VARCHAR, length, null, null, (Object[])params);
	}

	/**
	 * NCHAR.
	 *
	 * @param length the length
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField NCHAR(final int length, final String... params) {
		return createColumn(SQL.DATATYPE.NCHAR, length, null, null, (Object[])params);
	}

	/**
	 * NVARCHAR.
	 *
	 * @param length the length
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField NVARCHAR(final int length, final String... params) {
		return createColumn(SQL.DATATYPE.NVARCHAR, length, null, null, (Object[])params);
	}

	/**
	 * BINARY.
	 *
	 * @param length the length
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField BINARY(final int length, final String... params) {
		return createColumn(SQL.DATATYPE.BINARY, length, null, null, (Object[])params);
	}

	/**
	 * VARBINARY.
	 *
	 * @param length the length
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField VARBINARY(final int length, final String... params) {
		return createColumn(SQL.DATATYPE.VARBINARY, length, null, null, (Object[])params);
	}

	/**
	 * REAL.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField REAL(final String... params) {
		return createColumn(SQL.DATATYPE.REAL, 0, null, null, (Object[])params);
	}

	/**
	 * FLOAT.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField FLOAT(final String... params) {
		return createColumn(SQL.DATATYPE.FLOAT, 0, null, null, (Object[])params);
	}

	/**
	 * DOUBLE.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField DOUBLE(final String... params) {
		return createColumn(SQL.DATATYPE.DOUBLE, 0, null, null, (Object[])params);
	}

	/**
	 * NUMERIC.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField NUMERIC(final String... params) {
		return NUMERIC(null, null, params);
	}

	/**
	 * NUMERIC.
	 *
	 * @param precision the precision
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField NUMERIC(final Integer precision, final String... params) {
		return NUMERIC(precision, null, params);
	}

	/**
	 * NUMERIC.
	 *
	 * @param precision the precision
	 * @param scale the scale
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField NUMERIC(final Integer precision, final Integer scale, final String... params) {
		return createColumn(SQL.DATATYPE.NUMERIC, 0, precision, scale, (Object[])params);
	}

	/**
	 * DECIMAL.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField DECIMAL(final String... params) {
		return DECIMAL(null, null, params);
	}

	/**
	 * DECIMAL.
	 *
	 * @param precision the precision
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField DECIMAL(final Integer precision, final String... params) {
		return DECIMAL(precision, null, params);
	}

	/**
	 * DECIMAL.
	 *
	 * @param precision the precision
	 * @param scale the scale
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField DECIMAL(final Integer precision, final Integer scale, final String... params) {
		return createColumn(SQL.DATATYPE.DECIMAL, 0, precision, scale, (Object[])params);
	}


	/**
	 * TIME.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField TIME(final String... params) {
		return createColumn(SQL.DATATYPE.TIME, 0, null, null, (Object[])params);
	}

	/**
	 * DATE.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField DATE(final String... params) {
		return createColumn(SQL.DATATYPE.DATE, 0, null, null, (Object[])params);
	}

	/**
	 * TIMESTAMP.
	 *
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField TIMESTAMP(final String... params) {
		return createColumn(SQL.DATATYPE.TIMESTAMP, 0, null, null, (Object[])params);
	}

	/**
	 * LONGVARCHAR.
	 *
	 * @param length the length
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField LONGVARCHAR(final int length, final String... params) {
		return createColumn(SQL.DATATYPE.LONGVARCHAR, length, null, null, (Object[])params);
	}

	/**
	 * LONGVARBINARY.
	 *
	 * @param length the length
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField LONGVARBINARY(final int length, final String... params) {
		return createColumn(SQL.DATATYPE.LONGVARBINARY, length, null, null, (Object[])params);
	}

	/**
	 * BLOB.
	 *
	 * @param length the length
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField BLOB(final int length, final String... params) {
		return createColumn(SQL.DATATYPE.BLOB, length, null, null, (Object[])params);
	}

	/**
	 * CLOB.
	 *
	 * @param length the length
	 * @param params the params
	 * @return the sql field
	 */
	protected static final SqlField CLOB(final int length, final String... params) {
		return createColumn(SQL.DATATYPE.CLOB, length, null, null, (Object[])params);
	}


	/**
	 * Primary key.
	 *
	 * @param columnList the column list
	 * @return the sql primary key
	 */
	@Label(LABEL_METHOD_PrimaryKey)
	protected static final SqlPrimaryKey PrimaryKey(final Object... columnList) {
		return new SqlPrimaryKey(columnList);
	}

	/**
	 * Foreign key.
	 *
	 * @param columnList the column list
	 * @return the sql index
	 */
	@Label(LABEL_METHOD_ForeignKey)
	protected static final SqlIndex ForeignKey(final Object... columnList) {
		return null;
	}

	/**
	 * Index.
	 *
	 * @param columnList the column list
	 * @return the sql index
	 */
	@Label(LABEL_METHOD_Index)
	protected static final SqlIndex Index(final Object... columnList) {
		return new SqlIndex(null, columnList);
	}

	/**
	 * Bitmap index.
	 *
	 * @param columnList the column list
	 * @return the sql index
	 */
	@Label(LABEL_METHOD_BitmapIndex)
	protected static final SqlIndex BitmapIndex(final Object... columnList) {
		return new SqlIndex(INDEXTYPE.BITMAP, columnList);
	}

	/**
	 * Unique index.
	 *
	 * @param columnList the column list
	 * @return the sql index
	 */
	@Label(LABEL_METHOD_UniqueIndex)
	protected static final SqlIndex UniqueIndex(final Object... columnList) {
		return new SqlIndex(INDEXTYPE.UNIQUE, columnList);
	}






	  ////////////////////////
	 // Instance Variables //
	////////////////////////

	/** The db. */
	public final Database db = new Database();

	/** The util. */
	public final Util util = new Util();

	/** The database gateway. */
	protected DatabaseGateway<?> databaseGateway = null;

	/** The cached fields. */
	protected SqlField[] cachedFields = null;

	/** The column names. */
	protected String[] columnNames = null;

	/** The primary key. */
	protected SqlPrimaryKey primaryKey = null;

//	private Class<?> initializedClass = null;
//	private boolean initializedIndices = false;





	  //////////////////
	 // Constructors //
	//////////////////

	/**
  	 * Instantiates a new sql table.
  	 *
  	 * @param schema the schema
  	 * @param name the name
  	 * @param alias the alias
  	 */
  	public SqlTable(final String schema, final String name, final String alias)
  	{
		super(new Sql(schema, name, alias));
		this.sql().this$(this);
	}

	/**
	 * Instantiates a new sql table.
	 *
	 * @param table the table
	 * @param alias the alias
	 */
	public SqlTable(final SqlTable table, final String alias){
		this(table.sql().schema, table.sql().name, alias);
	}

	/**
	 * Instantiates a new sql table.
	 *
	 * @param schema the schema
	 * @param name the name
	 * @param alias the alias
	 * @param columnNames the column names
	 */
	public SqlTable(final String schema, final String name, final String alias, final String[] columnNames){
		this(schema, name, alias);
		this.columnNames = columnNames;
	}



	  ////////////////////
	 // Public Methods //
	////////////////////

	@Override
	public Sql sql()
	{
		return (Sql)super.sql();
	}
	
	/**
  	 * @param newAlias
  	 * @return
  	 * @throws SQLEngineException
  	 * @see com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity#AS(java.lang.String)
  	 */
  	@Override
	public synchronized SqlTable AS(final String newAlias) throws SQLEngineException
	{
		Constructor<?> c = null;
		try {
			c = this.getClass().getDeclaredConstructor(String.class);
		}
		catch (final Exception e) {
			// Is handled below
		}

		if(c == null) {
			throw new NullPointerException("Constructor(String) not found.");
		}

		SqlTable newInstance = null;
		final boolean oldAccessible = c.isAccessible();
		try {
			c.setAccessible(true);
			newInstance = (SqlTable)c.newInstance(newAlias);
		}
		catch (final InvocationTargetException | IllegalAccessException | InstantiationException e) {
			// returns null
		}
		finally {
			c.setAccessible(oldAccessible);
		}
		return newInstance; //or null in case of exception
	}

	//this is a little missing in jave imho. Something like "parent" instead of "this" for inner classes is needed...
	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity#getTopLevelInstance()
	 */
	@Override
	protected SqlTable getTopLevelInstance() {
		return this;
	}

	
	
	private static final TPredicate<Field> FILTER_FOR_SQLFIELDS = new TPredicate<Field>() {
		@Override public boolean apply(final Field t) {
			return !Modifier.isStatic(t.getModifiers()) && JaReflect.isOfClassType(t.getType(), SqlField.class);
		}		
	};
	

	/**
	 * Initialize columns.
	 *
	 * @param forceInitialize the force initialize
	 */
	protected void initializeColumns(/*final boolean forceInitialize*/) 
	{
		final ArrayList<String> columnNames = new ArrayList<String>(30);
		final ArrayList<SqlField> fields = new ArrayList<SqlField>(30);
		
		SqlField sf;
		for (final Field f : JaReflect.getAllFields(this.getClass(), FILTER_FOR_SQLFIELDS)) {
			if((sf = ((SqlField)JaReflect.getFieldValue(f, this))) == null) continue;
			sf.setOwner(this);
			if(sf.getName() == null) {
				sf.setName(f.getName());
			}
			columnNames.add(sf.getName());
			fields.add(sf);
		}
		this.columnNames = columnNames.toArray(new String[columnNames.size()]);
		this.cachedFields = fields.toArray(new SqlField[fields.size()]);
	}

	/**
	 * Initialize indices.
	 *
	 * @param forceInitialize the force initialize
	 */
	protected void initializeIndices(/*final boolean forceInitialize*/) 
	{
		final SqlDdlTable.Indices indices = this.util.getIndices();
		if(indices != null) {
			for (final Field f : JaReflect.getAllFields(indices.getClass())) {
				final Object o = JaReflect.getFieldValue(f, indices);
				if(o != null && o instanceof SqlIndex) {
					final SqlIndex index = (SqlIndex)o;
					index.setName(f.getName());
					index.setOwner(this);
					if(index instanceof SqlPrimaryKey) {
						this.primaryKey = (SqlPrimaryKey)index;
					}
				}
			}
		}
	}


	/**
	 * Gets the database gateway.
	 *
	 * @return the database gateway
	 */
	protected DatabaseGateway<?> getDatabaseGateway() {
		return this.db.getDatabaseGateway();
	}

	/**
	 * Gets the db intern.
	 *
	 * @return the db intern
	 */
	protected Database getDbIntern() {
		return this.db;
	}

	/**
	 * Gets the indices intern.
	 *
	 * @return the indices intern
	 */
	protected Indices getIndicesIntern() {
		return null;
	}

	  ///////////////////
	 // Inner Classes //
	///////////////////

	/**
  	 * The Class Database.
  	 */
  	public class Database {

		/**
		 * Instantiates a new database.
		 */
		protected Database() {
			super();
		}

		/**
		 * Exists in db.
		 *
		 * @return true, if successful
		 */
		public boolean existsInDB() 
		{
			final SELECT y = new SELECT().FETCH_FIRST(0).FROM(SqlTable.this.getTopLevelInstance());			
			try {
				y.execute();
				return true;
			}
			catch (final SQLEngineTableNotFoundException e) {
				return false;
			}
			catch (final SQLEngineException e) {
				throw e;
			}
		}

		/**
		 * Gets the database gateway.
		 *
		 * @return the database gateway
		 */
		public DatabaseGateway<?> getDatabaseGateway() {
			return SqlTable.this.databaseGateway==null
				?SQL.getDefaultDatabaseGateway()
				:SqlTable.this.databaseGateway
			;
		}

		/**
		 * Truncate.
		 *
		 * @return the object
		 */
		public Object truncate() {
			return this.getDatabaseGateway().getDbmsAdaptor().truncate(SqlTable.this.getTopLevelInstance());
		}

		/**
		 * Update selectivity.
		 *
		 * @return the object
		 */
		public Object updateSelectivity() {
			return this.getDatabaseGateway().getDbmsAdaptor().updateSelectivity(SqlTable.this.getTopLevelInstance());
		}

		/**
		 * Rebuild indices.
		 *
		 * @return the object
		 */
		public Object rebuildIndices() {
			return this.getDatabaseGateway().getDbmsAdaptor().rebuildAllIndices(SqlTable.this.getTopLevelInstance().toString());
		}

		/**
		 * Sets the database gateway.
		 *
		 * @param databaseGateway the new database gateway
		 */
		public void setDatabaseGateway(final DatabaseGateway<?> databaseGateway) {
			SqlTable.this.getTopLevelInstance().databaseGateway = databaseGateway;
		}

		


		/**
		 * Execute count.
		 *
		 * @return the int
		 * @throws SQLEngineException the sQL engine exception
		 */
		public int executeCOUNT() throws SQLEngineException {
			final Object countResult = SqlTable.this.sql().COUNT().executeSingle();
			return (Integer)countResult;
		}

	}


	/**
	 * The Class Util.
	 */
	public class Util extends SqlTableIdentity.Util
	{

		/**
		 * Gets the db.
		 *
		 * @return the db
		 */
		public Database getDb() {
			return SqlTable.this.getDbIntern();
		}


		/**
		 * Initialize.
		 */
		public void initialize() {
			SqlTable.this.getTopLevelInstance().initializeColumns();
			SqlTable.this.getTopLevelInstance().initializeIndices();
//			this.initialize(false);
		}

//		/**
//		 * Initialize.
//		 *
//		 * @param forceInitialize the force initialize
//		 */
//		public void initialize(final boolean forceInitialize) {
//			SqlTable.this.getTopLevelInstance().initializeColumns(forceInitialize);
//			SqlTable.this.getTopLevelInstance().initializeIndices(forceInitialize);
//		}

		/**
		 * Gets the column names.
		 *
		 * @return the column names
		 */
		public String[] getColumnNames() {
			return SqlTable.this.columnNames;
		}

		/**
		 * Gets the sql fields.
		 *
		 * @return the sql fields
		 */
		public SqlField[] getSqlFields() {
			return SqlTable.this.getTopLevelInstance().cachedFields;
		}

		/**
		 * Gets the sql field.
		 *
		 * @param columnIndex the column index
		 * @return the sql field
		 */
		public SqlField getSqlField(final int columnIndex){
			return SqlTable.this.getTopLevelInstance().cachedFields[columnIndex];
		}

		/**
		 * Sets the column names.
		 *
		 * @param columnNames the new column names
		 */
		public void setColumnNames(final String[] columnNames) {
			SqlTable.this.getTopLevelInstance().columnNames = columnNames;
		}

		/**
		 * Gets the indices.
		 *
		 * @return the indices
		 */
		public Indices getIndices() {
			Indices foundIndices = SqlTable.this.getIndicesIntern();
			if(foundIndices == null){
				foundIndices = this.getIndicesByReflection();
			}
			return foundIndices;
		}

		/**
		 * Gets the indices by reflection.
		 *
		 * @return the indices by reflection
		 */
		protected Indices getIndicesByReflection() {
			final ArrayList<Field> allFields = JaReflect.getAllFields(
				SqlTable.this.getTopLevelInstance().getClass(), Modifier.STATIC
			);

			/* (25.08.2009 TM)NOTE:
			 * iterate through fields from end to start to get indices class of most extended class first
			 */
			int i = allFields.size();
			while(i-->0){
				final Field f = allFields.get(i);
				if(JaReflect.isOfClassType(f.getType(), Indices.class)) {
					return (Indices)JaReflect.getFieldValue(f, SqlTable.this.getTopLevelInstance());
				}
			}
			return null;
		}
	}

	/**
	 * The Class Sql.
	 */
	public static class Sql extends SqlTableIdentity.Sql
	{
		
		@Override
		protected SqlTable this$()
		{
			return (SqlTable)super.this$();
		}

		/**
		 * Instantiates a new sql.
		 *
		 * @param schema the schema
		 * @param name the name
		 * @param alias the alias
		 */
		protected Sql(final String schema, final String name, final String alias) {
			super(schema, name, alias);
		}

		/**
		 * Gets the sub tables.
		 *
		 * @return the sub tables
		 */
		public SqlTable[] getSubTables() {
			return null;
		}

		/**
		 * Select from hirarchie.
		 *
		 * @param flat the flat
		 * @return the sELECT
		 */
		public SELECT selectFromHirarchie(final boolean flat)
		{
			if(flat){
				return this.this$().selectFromHirarchieFlat(this.this$().util.getColumnNames())[0];
			}
			return this.this$().selectFromHirarchieHirarchical(this.this$().util.getColumnNames());
		}

		/**
		 * @param columns
		 * @param values
		 * @return
		 * @see com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity.Sql#INSERT(java.lang.Object[], java.lang.Object[])
		 */
		@Override
		public INSERT INSERT(final Object[] columns, final Object... values) 
		{
			return new INSERT().INTO(this.this$()).columns(
				(columns!=null)?columns:this.this$().columnNames
			)
			.VALUES(values);
		}

		/**
		 * @param columns
		 * @param values
		 * @return
		 * @see com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity.Sql#UPDATE(java.lang.Object[], java.lang.Object[])
		 */
		@Override
		public UPDATE UPDATE(final Object[] columns, final Object... values) 
		{
			return new UPDATE(this.this$().getTopLevelInstance()).columns(
				(columns!=null)?columns:this.this$().columnNames
			)
			.setValues(values);
		}
	}

	/**
	 * The Class Indices.
	 */
	public class Indices{

		/**
		 * Instantiates a new indices.
		 */
		protected Indices() {
			super();
		}

		/**
		 * List indices.
		 *
		 * @return the list
		 */
		public List<SqlIndex> listIndices()
		{
			final ArrayList<Field> allFields = JaReflect.getAllFields(this.getClass());
			final ArrayList<SqlIndex> definedIndices = new ArrayList<SqlIndex>(allFields.size());
			for (final Field f : allFields) {
				final Object o = JaReflect.getFieldValue(f, this);
				if(o != null && o instanceof SqlIndex) {
					definedIndices.add((SqlIndex)o);
				}
			}
			return definedIndices;
		}
	}



	/**
	 * Select from hirarchie hirarchical.
	 *
	 * @param columns the columns
	 * @return the sELECT
	 */
	protected SELECT selectFromHirarchieHirarchical(final Object[] columns)
	{
		final SELECT s = new SELECT().items(columns).FROM(this).setPacked(true);
		final SqlTable[] subTableArray = this.sql().getSubTables();

		if(subTableArray == null){
			return this.sql().SELECT();
		}

		SELECT lastSelect = s;
		for (final SqlTable t : subTableArray) {
			final SELECT childSelects = t.selectFromHirarchieHirarchical(columns);
			lastSelect.UNION(childSelects);
			lastSelect = childSelects;
		}
		if(lastSelect != s){
			return new SELECT().items(columns).FROM(s).setPacked(true);
		}
		return s;
	}



	/**
	 * Select from hirarchie flat.
	 *
	 * @param columns the columns
	 * @return the sELEC t[]
	 */
	protected SELECT[] selectFromHirarchieFlat(final Object[] columns) {
		final SELECT s = new SELECT().items(columns).FROM(this).setPacked(true);
		final SqlTable[] subTableArray = this.sql().getSubTables();
		if(subTableArray == null){
			return new SELECT[]{this.sql().SELECT()};
		}

		SELECT lastSelect = s;
		for (final SqlTable t : subTableArray) {
			final SELECT[] childSelects = t.selectFromHirarchieFlat(columns);
			lastSelect.UNION(childSelects[0]);
			lastSelect = childSelects[1];
		}
		return new SELECT[]{s,lastSelect};
	}

}
