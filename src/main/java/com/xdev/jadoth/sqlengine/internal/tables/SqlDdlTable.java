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

package com.xdev.jadoth.sqlengine.internal.tables;


import static com.xdev.jadoth.sqlengine.SQL.LANG.CREATE_TABLE;
import static com.xdev.jadoth.sqlengine.SQL.LANG.DEFAULT;
import static com.xdev.jadoth.sqlengine.SQL.LANG.DROP_TABLE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.TAB;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.comma_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.xdev.jadoth.lang.reflection.JaReflect;
import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineTableStructureChangedException;
import com.xdev.jadoth.sqlengine.interfaces.SqlExecutor;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.SqlField;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTrigger.Event;




/**
 * The Class SqlDdlTable.
 *
 * @author Thomas Muenz
 */
public abstract class SqlDdlTable extends SqlTable
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	/**
	 *
	 */
	private static final long serialVersionUID = 3516721864501741331L;



	///////////////////////////////////////////////////////////////////////////
	// static fields    //
	/////////////////////
	/** The Constant INSERT. */
	protected static final Event INSERT = SqlTrigger.eINSERT;

	/** The Constant DELETE. */
	protected static final Event DELETE = SqlTrigger.eDELETE;

	/** The Constant UPDATE. */
	protected static final Event UPDATE = SqlTrigger.eUPDATE;

	/** The Constant BEFORE. */
	protected static final SqlTrigger.Time BEFORE = SqlTrigger.Time.BEFORE;

	/** The Constant AFTER. */
	protected static final SqlTrigger.Time AFTER = SqlTrigger.Time.BEFORE;

	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////
	/**
	 * UPDAT e_ of.
	 *
	 * @param columnNames the column names
	 * @return the event
	 */
	protected static final Event UPDATE_OF(final String[] columnNames){
		return SqlTrigger.UPDATE_OF(columnNames);
	}

	/**
	 * Creates the trigger.
	 *
	 * @param time the time
	 * @param event the event
	 * @param action the action
	 * @return the sql trigger
	 */
	protected static SqlTrigger createTrigger(
		final SqlTrigger.Time time,
		final SqlTrigger.Event event,
		final Object action
	)
	{
		return new SqlTrigger(null, time, event, null, action);
	}




	  ////////////////////////
	 // Instance Variables //
	////////////////////////

	/** The db. */
  	public final Database db = new Database();

	/** The ddl. */
	public final Ddl ddl = new Ddl();

	/** The util. */
	public final Util util = new Util();

	/** The initialize in db. */
	private final boolean initializeInDB = true;



	/** The initialized triggers. */
	private boolean initializedTriggers = false;

	  //////////////////
	 // Constructors //
	//////////////////

	/**
  	 * Instantiates a new sql ddl table.
  	 *
  	 * @param schema the schema
  	 * @param name the name
  	 * @param alias the alias
  	 */
  	public SqlDdlTable(final String schema, final String name, final String alias){
		super(schema, name, alias);
	}





	  ///////////////////////
	 // Getters & Setters //
	///////////////////////



	  ////////////////////
	 // Public Methods //
	////////////////////

	/**
  	 * @param newAlias
  	 * @return
  	 * @throws SQLEngineException
  	 * @see com.xdev.jadoth.sqlengine.internal.tables.SqlTable#AS(java.lang.String)
  	 */
  	@Override
	public synchronized SqlDdlTable AS(final String newAlias) throws SQLEngineException {
		return (SqlDdlTable)super.AS(newAlias);
	}




	  ///////////////////////
	 // Protected Methods //
	///////////////////////



	/**
  	 * Check index.
  	 *
  	 * @param definedIndex the defined index
  	 * @param loadedIndices the loaded indices
  	 * @return the int
  	 */
  	protected int checkIndex(final SqlIndex definedIndex, final SqlIndex[] loadedIndices) {
		for (int i = 0; i < loadedIndices.length; i++) {
			if(loadedIndices[i].equals(definedIndex)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Check column.
	 *
	 * @param definedColumn the defined column
	 * @param loadedColumns the loaded columns
	 * @return the int
	 */
	protected int checkColumn(final SqlField definedColumn, final SqlField[] loadedColumns) {
		for (int i = 0; i < loadedColumns.length; i++) {
			if(loadedColumns[i].equals(definedColumn)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Validate indices.
	 *
	 * @throws SQLEngineException the sQL engine exception
	 */
	protected void validateIndices() throws SQLEngineException {

		final SqlIndex[] loadedIndices = this.getDatabaseGateway().getDbmsAdaptor().getRetrospectionAccessor().loadIndices(this);
		final boolean[] found = new boolean[loadedIndices.length];
		for (int i = 0; i < found.length; i++) {
			found[i] = false;
		}


		final SqlDdlTable.Indices indices = this.util.getIndices();
		if(indices != null) {
			int loopFoundIndex = -1;
			for (final Field f : JaReflect.getAllFields(indices.getClass())) {
				final Object o = JaReflect.getFieldValue(f, indices);
				if(o != null && o instanceof SqlIndex) {
					final SqlIndex index = (SqlIndex)o;
					loopFoundIndex = this.checkIndex(index, loadedIndices);
					if(loopFoundIndex > -1) {
						found[loopFoundIndex] = true;
//						System.out.println("Found Index: "+loopSqlIndex.getCompareString());
					}
					else {
						System.out.println("Missing Index: "+index.getCompareString());
						if(this.getDatabaseGateway().indexValidation_createMissingIndices) {
							if(!(index instanceof SqlPrimaryKey)) {
//								System.out.println("  creating missing Index " + loopSqlIndex.getName());
								this.getDatabaseGateway().getDbmsAdaptor().getDdlMapper().createIndex(index, false);
							}
						}
						else {
							throw new SQLEngineTableStructureChangedException("Missing Index: "+index.getCompareString());
						}
					}
				}
			}
		}
		for (int i = 0; i < loadedIndices.length; i++) {
			if(!found[i]) {
//				System.out.println("Undefined Index: "+loadedIndices[i].getCompareString());
				if(this.getDatabaseGateway().indexValidation_dropUndefinedIndices) {
					System.out.println("  dropping undefined Index " + loadedIndices[i].getName());
					this.getDatabaseGateway().dropIndex(loadedIndices[i]);
				}
				else {
					throw new SQLEngineTableStructureChangedException("Undefined Index: "+loadedIndices[i].getCompareString());
				}
			}
		}
	}






	/**
	 * Initialize defined triggers.
	 *
	 * @param forceInitialize the force initialize
	 */
	protected void initializeDefinedTriggers(final boolean forceInitialize) {
		if(this.initializedTriggers && !forceInitialize) {
			return;
		}
		final SqlDdlTable.Triggers triggers = this.getTriggersIntern();
		if(triggers != null) {
			for (final Field f : JaReflect.getAllFields(triggers.getClass())) {
				final Object o = JaReflect.getFieldValue(f, triggers);
				if(o != null && o instanceof SqlTrigger) {
					final SqlTrigger trigger = (SqlTrigger)o;
					trigger.setName(f.getName());
					trigger.setTable(this);
				}
			}
		}
		this.initializedTriggers = true;
	}



	/**
	 * Creates the column definition.
	 *
	 * @param column the column
	 * @return the string
	 */
	protected String createColumnDefinition(final SqlField column) {
		if(column == null) {
			return null;
		}
		final String name = column.getName();

		if(name == null) {
			return null;
		}

		final StringBuilder sb = new StringBuilder(20);
		sb.append(name);
		sb.append(_);
		column.assembleType(sb, this.getDatabaseGateway().getDbmsAdaptor());

		if(column.isNotNull()) {
			sb.append(_);
			sb.append(NOT_NULL);
		}
		if(column.isUnique()) {
			sb.append(_);
			sb.append(UNIQUE);
		}

		final Object defaultValue = column.getDefaultValue();
		if(defaultValue != null) {
			sb.append(_);
			sb.append(DEFAULT);
			sb.append(_);
			sb.append(defaultValue.toString());
		}
		return sb.toString();
	}

	/**
	 * Validate columns.
	 *
	 * @throws SQLEngineException the sQL engine exception
	 */
	protected void validateColumns() throws SQLEngineException {
		final SqlField[] loadedColumns = this.getDatabaseGateway().getDbmsAdaptor().getRetrospectionAccessor().loadColumns(this);
		final boolean[] found = new boolean[loadedColumns.length];
		for (int i = 0; i < found.length; i++) {
			found[i] = false;
		}

		int loopFoundIndex = -1;
		SqlField loopSqlColumn = null;
		for (final Field f : JaReflect.getAllFields(this.getClass())) {
			try {
				loopSqlColumn = (SqlField)f.get(this);
			}
			catch (final ClassCastException e) {
//				System.err.println(e);
				continue;
			}
			catch (final NullPointerException e) {
//				System.err.println(e);
				continue;
			}
			catch (final IllegalAccessException e) {
//				System.err.println(e);
				continue;
			}

			if(loopSqlColumn == null) {
//				System.err.println("null: "+f.getName());
				continue;
			}

			loopFoundIndex = this.checkColumn(loopSqlColumn, loadedColumns);
			if(loopFoundIndex > -1) {
				found[loopFoundIndex] = true;
//				System.out.println("Found Column: "+loopSqlColumn.getCompareString());
			}
			else {
//				System.out.println("Missing Column: "+loopSqlColumn.getCompareString());
				throw new SQLEngineTableStructureChangedException("Missing Column: "+loopSqlColumn.getCompareString());
			}
		}

		for (int i = 0; i < loadedColumns.length; i++) {
			if(!found[i]) {
//				System.out.println("Undefined Column: "+loadedColumns[i].getCompareString());
				throw new SQLEngineTableStructureChangedException("Undefined Column: "+loadedColumns[i].getCompareString());
			}
		}
	}



	  /////////////////////
	 // Private Methods //
	/////////////////////

	//this is a little buggy in jave imho. Something like "parent" instead of "this" for inner classes is needed...
	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.tables.SqlTable#getTopLevelInstance()
	 */
	@Override
	protected SqlDdlTable getTopLevelInstance() {
		return this;
	}

	/**
	 * Append column definitions.
	 *
	 * @param sb the sb
	 */
	private void appendColumnDefinitions(final StringBuilder sb)
	{
		final ArrayList<Field> fields =  JaReflect.getAllFields(this.getClass(),Modifier.STATIC);
		boolean notEmpty = false;
		for (int i = 0, size = fields.size(); i < size; i++) {
			final Object value = JaReflect.getFieldValue(fields.get(i), this);
			if(! (value instanceof SqlField[])) continue;

			for (final SqlField sf : (SqlField[])value) {
				if(sf == null) continue;
				if(notEmpty) {
					sb.append(comma_).append(NEW_LINE);
				}
				else {
					notEmpty = true;
				}
				sb.append(TAB).append(this.createColumnDefinition(sf));
			}
		}
	}




	  ///////////////////
	 // Inner Classes //
	///////////////////

	/**
  	 * @return
  	 * @see com.xdev.jadoth.sqlengine.internal.tables.SqlTable#getDbIntern()
  	 */
  	@Override
	protected Database getDbIntern() {
		return this.db;
	}

	/**
	 * Gets the ddl intern.
	 *
	 * @return the ddl intern
	 */
	protected Ddl getDdlIntern() {
		return this.ddl;
	}

	/**
	 * Gets the triggers intern.
	 *
	 * @return the triggers intern
	 */
	protected Triggers getTriggersIntern() {
		return null;
	}

	/**
	 * The Class Util.
	 */
	public class Util extends SqlTable.Util {

		/**
		 * Gets the ddl.
		 *
		 * @return the ddl
		 */
		public Ddl getDdl() {
			return SqlDdlTable.this.getDdlIntern();
		}

//		/**
//		 * @param reinitialize
//		 * @see net.jadoth.sqlengine.internal.tables.SqlTable.Util#initialize(boolean)
//		 */
//		@Override
//		public void initialize(final boolean reinitialize) {
//			super.initialize(reinitialize);
//			SqlDdlTable.this.getTopLevelInstance().initializeDefinedTriggers(reinitialize);
//		}

		/**
		 * Gets the triggers.
		 *
		 * @return the triggers
		 */
		public Triggers getTriggers() {
			return SqlDdlTable.this.getTriggersIntern();
		}
	}


	/**
	 * The Class Database.
	 */
	public class Database extends SqlTable.Database {

		/**
		 * Instantiates a new database.
		 */
		protected Database() {
			super();
		}

		/**
		 * Initialize for.
		 *
		 * @param databaseGateway the database gateway
		 * @return the sql ddl table
		 */
		public SqlDdlTable initializeFor(final DatabaseGateway<?> databaseGateway) {
			this.setDatabaseGateway(databaseGateway);
			this.initialize();
			return SqlDdlTable.this.getTopLevelInstance();
		}

		/**
		 * Initialize.
		 */
		public void initialize() {
			try {
				SqlDdlTable.this.getDdlIntern().initialize(/*false*/);
			} catch (final Exception e) {
				//TODO RunTimeException weitergeben
			}
		}

		/**
		 * Kill table.
		 *
		 * @throws SQLEngineException the sQL engine exception
		 */
		public void killTable() throws SQLEngineException 
		{
			this.truncate();
			this.getDatabaseGateway().execute(SqlExecutor.update, SqlDdlTable.this.ddl.DROP_TABLE());
		}

	}


	/**
	 * The Class Triggers.
	 */
	public abstract class Triggers {

		/**
		 * Instantiates a new triggers.
		 */
		protected Triggers() {
			super();
		}

		/**
		 * Define triggers.
		 *
		 * @param <A> the generic type
		 * @param dbms the dbms
		 */
		protected void defineTriggers(final DbmsAdaptor<?> dbms) {
			//nothing in basic implementation, override only if needed
		}

		/**
		 * List triggers.
		 *
		 * @return the list
		 */
		public List<SqlTrigger> listTriggers()
		{
			final ArrayList<Field> allFields = JaReflect.getAllFields(this.getClass());
			final ArrayList<SqlTrigger> definedTriggers = new ArrayList<SqlTrigger>(allFields.size());
			for (final Field f : allFields) {
				final Object o = JaReflect.getFieldValue(f, this);
				if(o != null && o instanceof SqlTrigger) {
					definedTriggers.add((SqlTrigger)o);
				}
			}
			return definedTriggers;
		}
	}


	/**
	 * The Class Ddl.
	 */
	public class Ddl
	{

		/**
		 * Instantiates a new ddl.
		 */
		protected Ddl() {
			super();
		}

		/**
		 * Initialize.
		 *
		 * @param forceInitialize the force initialize
		 * @return the sql ddl table
		 * @throws SQLEngineException the sQL engine exception
		 */
		public SqlDdlTable initialize(/*final boolean forceInitialize*/) throws SQLEngineException 
		{
			SqlDdlTable.this.getTopLevelInstance().util.initialize(/*forceInitialize*/);

			if(!SqlDdlTable.this.initializeInDB) {
				return SqlDdlTable.this.getTopLevelInstance();
			}

			final DatabaseGateway<?> dbc = SqlDdlTable.this.getDatabaseGateway();
			if(dbc == null || !dbc.isConnectionEnabled()) return SqlDdlTable.this.getTopLevelInstance();

			final Triggers trg = SqlDdlTable.this.getTriggersIntern();
			if(trg != null) {
				trg.defineTriggers(dbc.getDbmsAdaptor());
			}

			if(SqlDdlTable.this.db.existsInDB()) {
				if(SqlDdlTable.this.getDatabaseGateway().validateTableColumns) {
					try {
						SqlDdlTable.this.validateColumns();
					} catch (final SQLEngineTableStructureChangedException e) {
//						System.err.println(e);
					}
				}
				if(SqlDdlTable.this.getDatabaseGateway().indexValidationEnabled) {
					try {
						SqlDdlTable.this.validateIndices();
					} catch (final SQLEngineTableStructureChangedException e) {
//						System.err.println(e);
					}
				}
			}
			else {
				if(SqlDdlTable.this.columnNames != null && SqlDdlTable.this.columnNames.length > 0) {
					this.createEverything();
				}
				else {
//					System.err.println(this.toString() + " has no columns defined. No table created.");
				}
			}

			return SqlDdlTable.this.getTopLevelInstance();
		}


		/**
		 * Gets the primary key.
		 *
		 * @return the primary key
		 */
		public SqlPrimaryKey getPrimaryKey() {
			return SqlDdlTable.this.primaryKey;
		}

		/**
		 * Creates the everything.
		 */
		protected void createEverything(){
			SqlDdlTable.this.getDdlIntern().preCreateActions();
			SqlDdlTable.this.getDdlIntern().createTable();
			SqlDdlTable.this.getDdlIntern().createIndices();
			SqlDdlTable.this.getDdlIntern().createTriggers();
			SqlDdlTable.this.getDdlIntern().postCreateActions();
		}


		/**
		 * Pre create actions.
		 *
		 * @return the object
		 */
		protected Object preCreateActions() {
			return SqlDdlTable.this.getDatabaseGateway().getDbmsAdaptor().getDdlMapper().preCreateTableActions(SqlDdlTable.this.getTopLevelInstance());
		}

		/**
		 * Creates the table.
		 *
		 * @throws SQLEngineException the sQL engine exception
		 */
		public void createTable() throws SQLEngineException {
			SqlDdlTable.this.getDatabaseGateway().getDbmsAdaptor().getDdlMapper().createTable(SqlDdlTable.this.getTopLevelInstance());
		}

		/**
		 * Post create actions.
		 *
		 * @return the object
		 */
		protected Object postCreateActions() {
			return SqlDdlTable.this.getDatabaseGateway().getDbmsAdaptor().getDdlMapper().postCreateTableActions(SqlDdlTable.this.getTopLevelInstance());
		}

		/**
		 * Recreate table.
		 *
		 * @throws SQLEngineException the sQL engine exception
		 */
		public void recreateTable() throws SQLEngineException {
			SqlDdlTable.this.db.killTable();
			this.createEverything();
		}

		/**
		 * Creates the indices.
		 *
		 * @throws SQLEngineException the sQL engine exception
		 */
		public void createIndices() throws SQLEngineException {
			SqlDdlTable.this.getDatabaseGateway().getDbmsAdaptor().getDdlMapper().createIndices(SqlDdlTable.this.getTopLevelInstance());
		}

		/**
		 * Creates the triggers.
		 *
		 * @throws SQLEngineException the sQL engine exception
		 */
		public void createTriggers() throws SQLEngineException {
			SqlDdlTable.this.getDatabaseGateway().getDbmsAdaptor().getDdlMapper().createTriggers(SqlDdlTable.this.getTopLevelInstance());
		}


		/**
		 * DRO p_ table.
		 *
		 * @return the string
		 */
		public String DROP_TABLE() {
			return DROP_TABLE+_+SqlDdlTable.this.getTopLevelInstance().toString();
		}

		/**
		 * CREAT e_ table.
		 *
		 * @return the string
		 */
		public String CREATE_TABLE()
		{
			final StringBuilder sb = new StringBuilder(1024)
			.append(CREATE_TABLE).append(_).append(SqlDdlTable.this.getTopLevelInstance().toString())
			.append(_).append(par).append(NEW_LINE);
			SqlDdlTable.this.appendColumnDefinitions(sb);
			if(SqlDdlTable.this.primaryKey != null) {
				sb.append(comma_).append(NEW_LINE)
				.append(NEW_LINE)
				.append(TAB)
				.append(SqlDdlTable.this.primaryKey.CREATE_INDEX());
			}
			sb.append(NEW_LINE);
			sb.append(rap);
			return sb.toString();
		}
	}

}
