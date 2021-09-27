
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

import static com.xdev.jadoth.Jadoth.glue;
import static com.xdev.jadoth.lang.reflection.JaReflect.getMemberByLabel;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.TAB;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.apo;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.dot;
import static com.xdev.jadoth.sqlengine.internal.tables.SqlTable.LABEL_METHOD_BitmapIndex;
import static com.xdev.jadoth.sqlengine.internal.tables.SqlTable.LABEL_METHOD_ForeignKey;
import static com.xdev.jadoth.sqlengine.internal.tables.SqlTable.LABEL_METHOD_Index;
import static com.xdev.jadoth.sqlengine.internal.tables.SqlTable.LABEL_METHOD_PrimaryKey;
import static com.xdev.jadoth.sqlengine.internal.tables.SqlTable.LABEL_METHOD_UniqueIndex;

import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.xdev.jadoth.Jadoth;
import com.xdev.jadoth.lang.reflection.annotations.Label;
import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.dbms.standard.StandardDbmsAdaptor;
import com.xdev.jadoth.sqlengine.exceptions.NoDatabaseConnectionFoundException;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineCouldNotConnectToDBException;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineRuntimeException;
import com.xdev.jadoth.sqlengine.interfaces.ConnectionProvider;
import com.xdev.jadoth.sqlengine.interfaces.SqlExecutor;
import com.xdev.jadoth.sqlengine.internal.ColumnValueAssignment;
import com.xdev.jadoth.sqlengine.internal.ColumnValueTuple;
import com.xdev.jadoth.sqlengine.internal.DISTINCT;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.DoubleQuotedExpression;
import com.xdev.jadoth.sqlengine.internal.QueryListeners;
import com.xdev.jadoth.sqlengine.internal.QuotedExpression;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateAVG;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateCOLLECT;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateCOUNT;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateFUSION;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateINTERSECTION;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateMAX;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateMin;
import com.xdev.jadoth.sqlengine.internal.SqlAggregateSUM;
import com.xdev.jadoth.sqlengine.internal.SqlBoolean;
import com.xdev.jadoth.sqlengine.internal.SqlBooleanTerm;
import com.xdev.jadoth.sqlengine.internal.SqlColumn;
import com.xdev.jadoth.sqlengine.internal.SqlConcatenation;
import com.xdev.jadoth.sqlengine.internal.SqlCondition;
import com.xdev.jadoth.sqlengine.internal.SqlConditionEXISTS;
import com.xdev.jadoth.sqlengine.internal.SqlConditionNOT;
import com.xdev.jadoth.sqlengine.internal.SqlExpression;
import com.xdev.jadoth.sqlengine.internal.SqlField;
import com.xdev.jadoth.sqlengine.internal.SqlFunctionABS;
import com.xdev.jadoth.sqlengine.internal.SqlFunctionCASE;
import com.xdev.jadoth.sqlengine.internal.SqlFunctionSUBSTRING;
import com.xdev.jadoth.sqlengine.internal.SqlStar;
import com.xdev.jadoth.sqlengine.internal.SqlTimestamp;
import com.xdev.jadoth.sqlengine.internal.SqlxAggregateCOLLECT_asString;
import com.xdev.jadoth.sqlengine.internal.SqlxFunctionROUND;
import com.xdev.jadoth.sqlengine.internal.SqlxFunctionTO_CHAR;
import com.xdev.jadoth.sqlengine.internal.SqlxFunctionTO_NUMBER;
import com.xdev.jadoth.sqlengine.internal.procedures.PROCEDURE;
import com.xdev.jadoth.sqlengine.internal.tables.SqlDdlTable;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTable;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.util.SqlEngineLabels;
import com.xdev.jadoth.util.logging.jul.LoggingAspect;



/**
 * The Class SQL.
 *
 * @author Thomas Muenz
 */
public class SQL extends SqlEngineLabels
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	//SQL expression shortcuts
	/** The Constant NULL. */
	public static final SqlExpression NULL  = new SqlExpression(LANG.NULL);

	/** The Constant STAR. */
	public static final SqlExpression STAR  = new SqlStar();

	/** The Constant TRUE. */
	public static final SqlExpression TRUE  = new SqlBoolean(true);

	/** The Constant FALSE. */
	public static final SqlExpression FALSE = new SqlBoolean(false);

	/** The Constant CURRENT_CATALOG. */
	public static final SqlExpression CURRENT_CATALOG   = new SqlExpression(LANG.CURRENT_USER);

	/** The Constant CURRENT_SCHEMA. */
	public static final SqlExpression CURRENT_SCHEMA    = new SqlExpression(LANG.CURRENT_SCHEMA);

	/** The Constant CURRENT_TIME. */
	public static final SqlExpression CURRENT_TIME      = new SqlExpression(LANG.CURRENT_TIME);

	/** The Constant CURRENT_TIMESTAMP. */
	public static final SqlExpression CURRENT_TIMESTAMP = new SqlExpression(LANG.CURRENT_TIMESTAMP);

	/** The Constant CURRENT_USER. */
	public static final SqlExpression CURRENT_USER      = new SqlExpression(LANG.CURRENT_USER);



	///////////////////////////////////////////////////////////////////////////
	// static fields //
	//////////////////

	/** The default dbms. */
	private static DbmsAdaptor<?> defaultDBMS = null;

	/** The default database gateway. */
	private static DatabaseGateway<?> defaultDatabaseGateway = null;

	private static QueryListeners globalQueryListeners = new QueryListeners();


	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	/**
	 * @return
	 */
	public static QueryListeners getGlobalQueryListeners()
	{
		return globalQueryListeners;
	}

	public static QueryListeners globalQueryListeners()
	{
		return globalQueryListeners;
	}


	/**
	 * @param queryListeners
	 * @return
	 */
	public static void setGlobalQueryListeners(final QueryListeners queryListeners) throws NullPointerException
	{
		if(queryListeners == null){
			throw new NullPointerException("queryListeners may not be null");
		}
		globalQueryListeners = queryListeners;
	}




	/**
	 * Gets the default dbms.
	 *
	 * @return the defaultDBMS
	 */
	public static DbmsAdaptor<?> getDefaultDBMS()
	{
		if(defaultDBMS == null){
			defaultDBMS = StandardDbmsAdaptor.getSingletonStandardDbmsAdaptor();
		}
		return defaultDBMS;
	}

	public static DbmsDMLAssembler<?> getDefaultDMLAssembler() {
		return getDefaultDBMS().getDMLAssembler();
	}

	/**
	 * Gets the default database gateway.
	 *
	 * @return the defaultDatabaseGateway
	 */
	public static DatabaseGateway<?> getDefaultDatabaseGateway() {
		return defaultDatabaseGateway;
	}

	/**
	 * Sets the default dbms.
	 *
	 * @param defaultDBMS the defaultDBMS to set
	 */
	public static void setDefaultDBMS(final DbmsAdaptor<?> defaultDBMS) {
		SQL.defaultDBMS = defaultDBMS;
	}

	/**
	 * Sets the default database gateway.
	 *
	 * @param defaultDatabaseGateway the defaultDatabaseGateway to set
	 */
	public static void setDefaultDatabaseGateway(final DatabaseGateway<?> defaultDatabaseGateway) {
		SQL.defaultDatabaseGateway = defaultDatabaseGateway;
		SQL.defaultDBMS = defaultDatabaseGateway.getDbmsAdaptor();
	}



	/**
	 * Execute query.
	 *
	 * @param query the query
	 * @return the result set
	 * @throws SQLEngineException the sQL engine exception
	 */
	public static ResultSet executeQuery(final String query) throws SQLEngineException
	{
		if(defaultDatabaseGateway == null) {
			throw new NoDatabaseConnectionFoundException();
		}
		return defaultDatabaseGateway.execute(SqlExecutor.query, query);
	}


	/**
	 * Execute query single.
	 *
	 * @param query the query
	 * @return the object
	 * @throws SQLEngineException the sQL engine exception
	 */
	public static Object executeQuerySingle(final String query) throws SQLEngineException
	{
		if(defaultDatabaseGateway == null) {
			throw new NoDatabaseConnectionFoundException();
		}
		return defaultDatabaseGateway.execute(SqlExecutor.singleResultQuery, query);
	}

	/**
	 * Execute update.
	 *
	 * @param query the query
	 * @return the int
	 * @throws SQLEngineException the sQL engine exception
	 */
	public static int executeUpdate(final String query) throws SQLEngineException
	{
		if(defaultDatabaseGateway == null) {
			throw new NoDatabaseConnectionFoundException();
		}
		return defaultDatabaseGateway.execute(SqlExecutor.update, query);
	}


	/**
	 * TABLE.
	 *
	 * @param tablename the tablename
	 * @return the sql table identity
	 */
	public static final SqlTableIdentity TABLE(final String tablename){
		final String[] parts = util.parseFullQualifiedTablename(tablename.trim());
		return new SqlTableIdentity(parts[0], parts[1], parts[2] != null ?parts[2] :SQL.util.guessAlias(parts[1]));
	}

	/**
	 * TABLE.
	 *
	 * @param tablename the tablename
	 * @param alias the alias
	 * @return the sql table identity
	 */
	public static final SqlTableIdentity TABLE(final String tablename, final String alias){
		final String[] parts = util.parseFullQualifiedTablename(tablename.trim());
		return new SqlTableIdentity(parts[0], parts[1], alias);
	}

	/**
	 * TABLE.
	 *
	 * @param schema the schema
	 * @param tablename the tablename
	 * @param alias the alias
	 * @return the sql table identity
	 */
	public static final SqlTableIdentity TABLE(final String schema, final String tablename, final String alias){
		return new SqlTableIdentity(schema, tablename, alias != null ?alias :SQL.util.guessAlias(tablename));
	}

	/**
	 * SELECT.
	 *
	 * @param items the items
	 * @return the sELECT
	 */
	public static final SELECT SELECT(final Object... items) {
		return new SELECT().items(items);
	}

	public static final <R> CALL<R> CALL(final Class<R> returnType, final PROCEDURE<R> storedProcedure, final Object... paramters)
	{
		return new CALL<R>(returnType, storedProcedure).parameters(paramters);
	}

	public static final <R> CALL<R> CALL(final PROCEDURE<R> storedProcedure, final Object... paramters)
	{
		return new CALL<R>(storedProcedure).parameters(paramters);
	}


	public static ColumnValueTuple cv(final SqlColumn column, final Object value)
	{
		return new ColumnValueTuple(){
			@Override public SqlColumn getColumn(){ return column; }
			@Override public Object getValue(){ return value; }
		};
	}

	public static ColumnValueAssignment assign(final SqlColumn column, final Object value)
	{
		return new ColumnValueAssignment(){
			private Object val = value;
			@Override public SqlColumn getColumn(){ return column; }
			@Override public Object getValue(){ return this.val; }
			@Override public void setValue(final Object value) { this.val = value; }
		};
	}

	/**
	 * ABS.
	 *
	 * @param value the value
	 * @return the sql function abs
	 */
	public static final SqlFunctionABS ABS(final Object value) {
		return new SqlFunctionABS(value);
	}

	/**
	 * SUM.
	 *
	 * @param value the value
	 * @return the sql aggregate sum
	 */
	public static final SqlAggregateSUM SUM(final Object value) {
		return new SqlAggregateSUM(value);
	}

	/**
	 * MAX.
	 *
	 * @param value the value
	 * @return the sql aggregate max
	 */
	public static final SqlAggregateMAX MAX(final Object value) {
		return new SqlAggregateMAX(value);
	}

	/**
	 * MIN.
	 *
	 * @param value the value
	 * @return the sql aggregate min
	 */
	public static final SqlAggregateMin MIN(final Object value) {
		return new SqlAggregateMin(value);
	}

	/**
	 * AVG.
	 *
	 * @param value the value
	 * @return the sql aggregate avg
	 */
	public static final SqlAggregateAVG AVG(final Object value) {
		return new SqlAggregateAVG(value);
	}

	/**
	 * COUNT.
	 *
	 * @return the sql aggregate count
	 */
	public static final SqlAggregateCOUNT COUNT() {
		return COUNT(null);
	}

	/**
	 * COUNT.
	 *
	 * @param value the value
	 * @return the sql aggregate count
	 */
	public static final SqlAggregateCOUNT COUNT(final Object value) {
		if(value == null) {
			return new SqlAggregateCOUNT();
		}
		return new SqlAggregateCOUNT(value);
	}

	/**
	 * COLLECT.
	 *
	 * @param expression the expression
	 * @return the sql aggregate collect
	 */
	public static final SqlAggregateCOLLECT COLLECT(final Object expression)
	{
		return new SqlAggregateCOLLECT(expression);
	}

	/**
	 * xCollect_asString.
	 *
	 * @param expression the expression
	 * @return the sqlx aggregate collect as string
	 */
	public static final SqlxAggregateCOLLECT_asString xCOLLECT_asString(final Object expression)
	{
		return new SqlxAggregateCOLLECT_asString(expression);
	}

	/**
	 * xCollect_asString.
	 *
	 * @param expression the expression
	 * @param seperator the seperator
	 * @return the sqlx aggregate collect as string
	 */
	public static final SqlxAggregateCOLLECT_asString xCOLLECT_asString(final Object expression, final String seperator)
	{
		return new SqlxAggregateCOLLECT_asString(expression, seperator);
	}

	/**
	 * FUSION.
	 *
	 * @param expression the expression
	 * @return the sql aggregate fusion
	 */
	public static final SqlAggregateFUSION FUSION(final Object expression)
	{
		return new SqlAggregateFUSION(expression);
	}

	/**
	 * INTERSECTION.
	 *
	 * @param expression the expression
	 * @return the sql aggregate intersection
	 */
	public static final SqlAggregateINTERSECTION INTERSECTION(final Object expression)
	{
		return new SqlAggregateINTERSECTION(expression);
	}

	public static final DISTINCT DISTINCT(final Object expression)
	{
		return new DISTINCT(expression);
	}

	/**
	 * Concatenate.
	 *
	 * @param characterValueExpressions the character value expressions
	 * @return the sql concatenation
	 */
	public static final SqlConcatenation concatenate(final Object... characterValueExpressions)
	{
		return new SqlConcatenation(characterValueExpressions);
	}

	/**
	 * Resembles an "if"/then/else in SQL.
	 *
	 * @param whenExpression the when expression
	 * @param thenExpression the then expression
	 * @param elseExpression the else expression
	 * @return the sql function case
	 * @see {@link SQL.CASE} for the "switch" equivalent
	 */
	public static final SqlFunctionCASE CASE_WHEN(final Object whenExpression, final Object thenExpression, final Object elseExpression)
	{
		return new SqlFunctionCASE().WHEN(whenExpression, thenExpression).ELSE(elseExpression);
	}

	/**
	 * Resembles a "switch" in SQL.
	 *
	 * @param expression the expression
	 * @return the sql function case
	 * @see {@link SQL.CASE_WHEN} for the "if"/then/else equivalent
	 */
	public static final SqlFunctionCASE CASE(final Object expression)
	{
		return new SqlFunctionCASE(expression);
	}

	/**
	 * SUBSTRING.
	 *
	 * @param characterValueExpression the character value expression
	 * @param startPosition the start position
	 * @param stringLength the string length
	 * @param charLengthUnits the char length units
	 * @return the sql function substring
	 */
	public static final SqlFunctionSUBSTRING SUBSTRING(
		final Object characterValueExpression,
		final Integer startPosition,
		final Integer stringLength,
		final Integer charLengthUnits
	)
	{
		return new SqlFunctionSUBSTRING(characterValueExpression, startPosition, stringLength, charLengthUnits);
	}

	/**
	 * SUBSTRING.
	 *
	 * @param characterValueExpression the character value expression
	 * @param startPosition the start position
	 * @param stringLength the string length
	 * @return the sql function substring
	 */
	public static final SqlFunctionSUBSTRING SUBSTRING(
		final Object characterValueExpression,
		final Integer startPosition,
		final Integer stringLength
	)
	{
		return new SqlFunctionSUBSTRING(characterValueExpression, startPosition, stringLength, null);
	}

	/**
	 * SUBSTRING.
	 *
	 * @param characterValueExpression the character value expression
	 * @param startPosition the start position
	 * @return the sql function substring
	 */
	public static final SqlFunctionSUBSTRING SUBSTRING(
		final Object characterValueExpression,
		final Integer startPosition
	)
	{
		return new SqlFunctionSUBSTRING(characterValueExpression, startPosition, null, null);
	}

	/**
	 * NOT.
	 *
	 * @param condition the condition
	 * @return the sql condition not
	 */
	public static final SqlConditionNOT NOT(final SqlCondition condition) {
		return new SqlConditionNOT(null, condition);
	}

	/**
	 * EXISTS.
	 *
	 * @param subSelect the sub select
	 * @return the sql condition exists
	 */
	public static final SqlConditionEXISTS EXISTS(final SELECT subSelect) {
		return new SqlConditionEXISTS(subSelect);
	}

	/**
	 * T o_ number.
	 *
	 * @param value the value
	 * @return the sqlx function t o_ number
	 */
	public static final SqlxFunctionTO_NUMBER TO_NUMBER(final Object value) {
		return new SqlxFunctionTO_NUMBER(value);
	}

	/**
	 * T o_ char.
	 *
	 * @param value the value
	 * @param format the format
	 * @return the sqlx function t o_ char
	 */
	public static final SqlxFunctionTO_CHAR TO_CHAR(final Object value, final Object format) {
		return new SqlxFunctionTO_CHAR(value, format);
	}

	/**
	 * ROUND.
	 *
	 * @param value the value
	 * @param decimals the decimals
	 * @return the sqlx function round
	 */
	public static final SqlxFunctionROUND ROUND(final Object value, final int decimals) {
		return new SqlxFunctionROUND(value, decimals);
	}


	/**
	 * Quote.
	 *
	 * @param characterValueExpression the character value expression
	 * @return the quoted expression
	 */
	public static QuotedExpression quote(final Object characterValueExpression) {
		return new QuotedExpression(characterValueExpression);
	}

	/**
	 * Double quote.
	 *
	 * @param characterValueExpression the character value expression
	 * @return the double quoted expression
	 */
	public static DoubleQuotedExpression doubleQuote(final Object characterValueExpression)
	{
		return new DoubleQuotedExpression(characterValueExpression);
	}


	/**
	 * Wraps the given expression object as an {@link SqlBooleanTerm} instance that encloses it in <b>par</b>enthesis
	 * (round brackets).<br>
	 * Note that in contradiction to {@link #condition(Object)}, a new wrapper instance is <b>always</b> created to
	 * ensure that each call actually adds a pair of parenthesis (for whatever reason that might be needed).
	 *
	 * @param expression the expression to be wrapped as {@link SqlBooleanTerm}
	 * @return a {@link SqlBooleanTerm} instance wrapping the passed expression object.
	 */
	public static final SqlBooleanTerm par(final Object expression)
	{
		return new SqlBooleanTerm(expression, null, true);
	}

	/**
	 * Wraps the given expression object as an {@link SqlCondition} instance.<br>
	 * If the object itself is already a {@link SqlCondition}, it is returned right away.
	 *
	 * @param expression the expression to be wrapped as {@link SqlCondition}
	 * @return a {@link SqlCondition} instance representing the passed expression object's content as a condition.
	 */
	public static final SqlCondition condition(final Object expression)
	{
		if(expression instanceof SqlCondition){
			return (SqlCondition)expression;
		}
		return new SqlCondition(expression);
	}

	/**
	 * Exp.
	 *
	 * @param o the o
	 * @return the sql expression
	 */
	public static final SqlExpression exp(final Object o)
	{
		return o==null ?null :new SqlExpression(o);
	}

	/**
	 * Col.
	 *
	 * @param s the s
	 * @return the sql column
	 */
	public static final SqlColumn col(final String s) {
		return s==null?null:new SqlColumn(s);
	}

	/**
	 * Timestamp.
	 *
	 * @param parts the parts
	 * @return the sql timestamp
	 */
	public static final SqlTimestamp timestamp(final int... parts) {
		return new SqlTimestamp(SQL.util.assembleTimestamp(parts));
	}


	/**
	 * Connect.
	 *
	 * @param connectionProvider the connection provider
	 * @return true, if successful
	 * @throws SQLEngineCouldNotConnectToDBException the sQL engine could not connect to db exception
	 */
	@SuppressWarnings("unchecked")
	public static boolean connect(final ConnectionProvider<?> connectionProvider)
		throws SQLEngineCouldNotConnectToDBException
	{
		if(connectionProvider == null) return false;

		setDefaultDBMS(connectionProvider.getDbmsAdaptor());
		setDefaultDatabaseGateway(new DatabaseGateway(connectionProvider));
		defaultDatabaseGateway.connect();
		return true;
	}




	/**
	 * Creates the.
	 *
	 * @param <T> the generic type
	 * @param tableClass the table class
	 * @param alias the alias
	 * @return the t
	 * @throws SQLEngineRuntimeException the sQL engine runtime exception
	 */
	public static <T extends SqlTable> T create(final Class<T> tableClass, final String alias) throws SQLEngineRuntimeException {
		try {
			final T t =	tableClass.getConstructor(String.class).newInstance(alias);
			t.util.initialize(/*false*/);
			return t;
		} catch (final Exception e) {
			throw new SQLEngineRuntimeException(e);
		}
	}

	/**
	 * Initialize in db.
	 *
	 * @param <T> the generic type
	 * @param tableClass the table class
	 * @param alias the alias
	 * @return the t
	 * @throws SQLEngineRuntimeException the sQL engine runtime exception
	 */
	public static <T extends SqlDdlTable> T initializeInDb(final Class<T> tableClass, final String alias) throws SQLEngineRuntimeException {
		return initializeFor(defaultDatabaseGateway, tableClass, alias);
	}

	/**
	 * Initialize for.
	 *
	 * @param <T> the generic type
	 * @param dbc the dbc
	 * @param tableClass the table class
	 * @param alias the alias
	 * @return the t
	 * @throws SQLEngineRuntimeException the sQL engine runtime exception
	 */
	public static <T extends SqlDdlTable> T initializeFor(
		final DatabaseGateway<?> dbc, final Class<T> tableClass, final String alias
	)
		throws SQLEngineRuntimeException
	{
		try {
			final T t = tableClass.getConstructor(String.class).newInstance(alias);
			t.db.initializeFor(dbc);
			return t;
		}
		catch (final Exception e) {
			throw new SQLEngineRuntimeException(e);
		}
	}


	/**
	 * Executes <code>update</code> with <code>parameters</code>.
	 * If 0 rows have been affected by the UPDATE, then an INSERT with the
	 * same column-value assignment and target table is derived from the UPDATE and executed.
	 * <p>
	 * A save is meant to update or insert values for a single row of data automatically as needed.
	 * Still it is possible, to update multiple rows via call of this method, if the conditions in <code>update</code>
	 * are set in the right way (or "wrong" way, if you intended to only update one row.)
	 * <p>
	 * If the UPDATE already affects rows, the performance of this method is the same as calling
	 * <code>UPDATE.execute()</code> directly. Only if an INSERT is really needed additional work is done.
	 *
	 * @param update
	 * @param parameters
	 * @return the number of affected rows
	 */
	public static int save(final UPDATE update, final Object... parameters)
	{
		int updateRowCount = update.execute(parameters);
		if(updateRowCount == 0){
			updateRowCount = update.getLinkedINSERT().execute(parameters);
		}
		return updateRowCount;
	}


	/**
	 * The Class config.
	 */
	public static class config
	{
//		private static Level loggingLevelAssemble = Level.OFF;
//		/** The logging level compose. */
//		private static Level loggingLevelCompose = Level.OFF;

		/** The logging level execute. */
		private static Level loggingLevelExecute = Level.OFF;

		/** The global logging aspect. */
		private static LoggingAspect globalLoggingAspect = new LoggingAspect(null, Level.OFF);



		/** The Constant param. */
		public static String param = "?";

		//Defaults
		//this must escape param
		/** The Constant prepareParamRegExp. */
		public static  String prepareParamRegExp = "\\?";

		/** The Constant default_subSelectLevelSpace. */
		public static String default_subSelectLevelSpace = TAB;

		//Settings
		/** The Constant list_CommaNewLine. */
		public static boolean list_CommaNewLine = true;

		/** The Constant TablePrefix. */
		public static String TablePrefix = "Tbl";

		/** The Constant TableSuffix. */
		public static String TableSuffix = "";

		/** The Constant IndexPrefix. */
		public static String IndexPrefix = "Idx";

		/** The Constant IndexSuffix. */
		public static String IndexSuffix = "";

		/** The Constant DefaultSchema. */
		public static String DefaultSchema = null;

		/** The Constant SQLTRUE. */
		public static final String SQLTRUE = "SQLTRUE()";

		/** The Constant SQLFALSE. */
		public static final String SQLFALSE = "SQLFALSE()";



		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////
//		/**
//		 * @return the levelAssemble
//		 */
//		public static Level getLoggingLevelAssemble() {
//			return loggingLevelAssemble;
//		}
//		/**
//		 * Gets the logging level compose.
//		 *
//		 * @return the levelCompose
//		 */
//		public static Level getLoggingLevelCompose() {
//			return loggingLevelCompose;
//		}

		/**
		 * Gets the logging level execute.
		 *
		 * @return the levelExecute
		 */
		public static Level getLoggingLevelExecute() {
			return loggingLevelExecute;
		}

		/**
		 * Gets the global logging aspect.
		 *
		 * @return the globalLoggingAspect
		 */
		public static LoggingAspect getGlobalLoggingAspect() {
			return globalLoggingAspect;
		}


		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
//		/**
//		 * @param levelAssemble the levelAssemble to set
//		 */
//		public static void setLoggingLevelAssemble(Level levelAssemble) {
//			loggingLevelAssemble = levelAssemble;
//		}
//		/**
//		 * Sets the logging level compose.
//		 *
//		 * @param levelCompose the levelCompose to set
//		 */
//		public static void setLoggingLevelCompose(final Level levelCompose) {
//			loggingLevelCompose = levelCompose;
//		}

		/**
		 * Sets the logging level execute.
		 *
		 * @param levelExecute the levelExecute to set
		 */
		public static void setGlobalLoggingLevelExecute(final Level levelExecute) {
			loggingLevelExecute = levelExecute;
		}

		/**
		 * Sets the logging default.
		 *
		 * @param globalLoggingAspect the globalLoggingAspect to set as default
		 */
		public static void setGlobalLogger(final LoggingAspect globalLoggingAspect) {
			config.globalLoggingAspect = globalLoggingAspect;
		}

		/**
		 * Sets the logging default.
		 *
		 * @param logger the new logging default
		 */
		public static void setGlobalLogger(final Logger logger) {
			config.globalLoggingAspect = new LoggingAspect(logger, Level.CONFIG);
		}

		/**
		 * Sets the logging default.
		 *
		 * @param logger the logger
		 * @param defaultLevel the default level
		 */
		public static void setGlobalLogger(final Logger logger, final Level defaultLevel) {
			config.globalLoggingAspect = new LoggingAspect(logger, defaultLevel);
		}

	}

	/**
	 * The Class Punctuation.
	 */
	public static abstract class Punctuation
	{
		/** The Constant n. */
		public static final char n = '\n';

		/** The Constant t. */
		public static final char t = '\t';

		/** The Constant qt. */
		public static final char qt = '"';

		/** The Constant apo. */
		public static final char apo = '\'';

		/** The Constant at. */
		public static final char at = '@';

		/** The Constant cma. */
		public static final char cma = ',';

		/** The Constant str. */
		public static final char str = '*';

		/** The Constant d. */
		public static final char d = '.';

		/** The Constant qM. */
		public static final char qM = '?';

		/** The Constant _. */
		public static final char _ = ' ';

		/** The Constant NEW_LINE. */
		public static final String NEW_LINE = ""+n;

		public static final String SPACE = ""+_;

		/** The Constant TAB. */
		public static final String TAB = ""+t;

		/** The Constant quote. */
		public static final String quote = ""+qt;

		/** The Constant scol. */
		public static final String scol = ";";

		/** The Constant par. */
		public static final String par = "(";

		/** The Constant rap. */
		public static final String rap = ")";

		/** The Constant comma. */
		public static final String comma = ""+cma;

		/** The Constant dot. */
		public static final String dot = ""+d;

		/** The Constant star. */
		public static final String star = ""+str;

		/** The Constant qMark. */
		public static final String qMark = ""+qM;


		//comparators
		/** The Constant is. */
		public static final String is = "=";

		/** The Constant ne1. */
		public static final String ne1 = "!=";

		/** The Constant ne2. */
		public static final String ne2 = "<>";

		/** The Constant gt. */
		public static final String gt = ">";

		/** The Constant lt. */
		public static final String lt = "<";

		/** The Constant gte. */
		public static final String gte = ">=";

		/** The Constant lte. */
		public static final String lte = "<=";

		/** The Constant comma_. */
		public static final String comma_ = cma+""+_;



		/** The Constant eq. */
		public static final String eq = is;

		/** The Constant _eq_. */
		public static final String _eq_ = _+eq+_;

		/** The Constant ne. */
		public static final String ne = ne1;

		/** The Constant selectItem_Comma_NEW_LINE. */
		public static final String selectItem_Comma_NEW_LINE = comma+NEW_LINE;

		/** The Constant selectItem_NEW_LINE_Comma. */
		public static final String selectItem_NEW_LINE_Comma = NEW_LINE+comma;

		/** The Constant singleLineComment. */
		public static final String singleLineComment = "--";

		/** The Constant blockCommentStart. */
		public static final String blockCommentStart = "/*";

		/** The Constant blockCommentEnd. */
		public static final String blockCommentEnd = "*/";
	}

	/**
	 * The Class LANG.
	 */
	public static abstract class LANG
	{

		/** The Constant ABS. */
		public static final String ABS    		= "ABS";

		/** The Constant ALL. */
		public static final String ALL 			= "ALL";

		/** The Constant ALTER. */
		public static final String ALTER 		= "ALTER";

		/** The Constant AND. */
		public static final String AND 			= "AND";

		/** The Constant AS. */
		public static final String AS 			= "AS";

		/** The Constant ASC. */
		public static final String ASC 			= "ASC";

		/** The Constant AVG. */
		public static final String AVG    		= "AVG";

		/** The Constant BEGIN_ATOMIC. */
		public static final String BEGIN_ATOMIC	= "BEGIN ATOMIC";

		/** The Constant BETWEEN. */
		public static final String BETWEEN 		= "BETWEEN";

		/** The Constant BITMAP. */
		public static final String BITMAP 		= "BITMAP";

		/** The Constant BY. */
		public static final String BY 			= "BY";

		/** The Constant CALL. */
		public static final String CALL  		= "CALL";

		/** The Constant CASE. */
		public static final String CASE 		= "CASE";

		/** The Constant CAST. */
		public static final String CAST 		= "CAST";

		/** The Constant COLLECT. */
		public static final String COLLECT  	= "COLLECT";

		/** The Constant THEN. */
		public static final String THEN 		= "THEN";

		/** The Constant ELSE. */
		public static final String ELSE 		= "ELSE";

		/** The Constant CHAR. */
		public static final String CHAR  		= "CHAR";

		/** The Constant COALESCE. */
		public static final String COALESCE 	= "COALESCE";

		/** The Constant CONSTRAINT. */
		public static final String CONSTRAINT 	= "CONSTRAINT";

		/** The Constant CONTAINS. */
		public static final String CONTAINS 	= "CONTAINS";

		/** The Constant COUNT. */
		public static final String COUNT  		= "COUNT";

		/** The Constant CREATE. */
		public static final String CREATE 		= "CREATE";

		/** The Constant TABLE. */
		public static final String DATE 		= "DATE";

		/** The Constant DEFAULT. */
		public static final String DEFAULT 		= "DEFAULT";

		/** The Constant DELETE. */
		public static final String DELETE 		= "DELETE";

		/** The Constant DESC. */
		public static final String DESC 		= "DESC";

		/** The Constant DISTINCT. */
		public static final String DISTINCT 	= "DISTINCT";

		/** The Constant DROP. */
		public static final String DROP 		= "DROP";

		/** The Constant END. */
		public static final String END 			= "END";

		/** The Constant EXISTS. */
		public static final String EXISTS 		= "EXISTS";

		/** The Constant FETCH. */
		public static final String FETCH 		= "FETCH";

		/** The Constant FIRST. */
		public static final String FIRST 		= "FIRST";

		/** The Constant FOREIGN. */
		public static final String FOREIGN 		= "FOREIGN";

		/** The Constant FUSION. */
		public static final String FUSION  		= "FUSION";

		/** The Constant FROM. */
		public static final String FROM 		= "FROM";

		/** The Constant FULL. */
		public static final String FULL    		= "FULL";

		/** The Constant GLOBAL. */
		public static final String GLOBAL 		= "GLOBAL";

		/** The Constant GROUP. */
		public static final String GROUP 		= "GROUP";

		/** The Constant HAVING. */
		public static final String HAVING 		= "HAVING";

		/** The Constant IN. */
		public static final String IN 			= "IN";

		/** The Constant INDEX. */
		public static final String INDEX 		= "INDEX";

		/** The Constant INNER. */
		public static final String INNER   		= "INNER";

		/** The Constant INSERT. */
		public static final String INSERT 		= "INSERT";

		/** The Constant INTO. */
		public static final String INTO 		= "INTO";

		/** The Constant IS. */
		public static final String IS 			= "IS";

		/** The Constant JOIN. */
		public static final String JOIN    		= "JOIN";

		/** The Constant KEY. */
		public static final String KEY 			= "KEY";

		/** The Constant LEFT. */
		public static final String LEFT    		= "LEFT";

		/** The Constant LIKE. */
		public static final String LIKE 		= "LIKE";

		/** The Constant MOD. */
		public static final String MOD    		= "MOD";

		/** The Constant MAX. */
		public static final String MAX    		= "MAX";

		/** The Constant MIN. */
		public static final String MIN    		= "MIN";

		/** The Constant NATURAL. */
		public static final String NATURAL 		= "NATURAL";

		/** The Constant NEW. */
		public static final String NEW 			= "NEW";

		/** The Constant NOT. */
		public static final String NOT 			= "NOT";

		/** The Constant NULL. */
		public static final String NULL 		= "NULL";

		/** The Constant NUMBER. */
		public static final String NUMBER 		= "NUMBER";

		/** The Constant OFFSET. */
		public static final String OFFSET 		= "OFFSET";

		/** The Constant OLD. */
		public static final String OLD 			= "OLD";

		/** The Constant ON. */
		public static final String ON 			= "ON";

		/** The Constant ONLY. */
		public static final String ONLY 		= "ONLY";

		/** The Constant OR. */
		public static final String OR 			= "OR";

		/** The Constant ORDER. */
		public static final String ORDER 		= "ORDER";

		/** The Constant OUTER. */
		public static final String OUTER   		= "OUTER";

		/** The Constant PRIMARY. */
		public static final String PRIMARY 		= "PRIMARY";

		/** The Constant PROCEDURE. */
		public static final String PROCEDURE 	= "PROCEDURE";

		/** The Constant REFERENCING. */
		public static final String REFERENCING 	= "REFERENCING";

		/** The Constant RIGHT. */
		public static final String RIGHT   		= "RIGHT";

		/** The Constant ROUND. */
		public static final String ROUND  		= "ROUND";

		/** The Constant ROW. */
		public static final String ROW 			= "ROW";

		/** The Constant ROWS. */
		public static final String ROWS 		= "ROWS";

		/** The Constant SELECT. */
		public static final String SELECT 		= "SELECT";

		/** The Constant SET. */
		public static final String SET 			= "SET";

		/** The Constant SUBSTRING. */
		public static final String SUBSTRING 	= "SUBSTRING";

		/** The Constant SUBSTRING_REGEX. */
		public static final String SUBSTRING_REGEX 	= "SUBSTRING_REGEX";

		/** The Constant SUM. */
		public static final String SUM    		= "SUM";

		/** The Constant TABLE. */
		public static final String TABLE 		= "TABLE";

		/** The Constant TEMPORARY. */
		public static final String TEMPORARY 	= "TEMPORARY";

		/** The Constant TABLE. */
		public static final String TIME 		= "TIME";

		/** The Constant TABLE. */
		public static final String TIMESTAMP 	= "TIMESTAMP";

		/** The Constant TO. */
		public static final String TO 			= "TO";

		/** The Constant TOP. */
		public static final String TOP 			= "TOP";

		/** The Constant TRIGGER. */
		public static final String TRIGGER 		= "TRIGGER";

		/** The Constant TRUNCATE. */
		public static final String TRUNCATE 	= "TRUNCATE";

		/** The Constant UNION. */
		public static final String UNION 		= "UNION";

		/** The Constant INTERSECT. */
		public static final String INTERSECT	= "INTERSECT";

		/** The Constant INTERSECTION. */
		public static final String INTERSECTION	= "INTERSECTION";

		/** The Constant EXCEPT. */
		public static final String EXCEPT 		= "EXCEPT";

		/** The Constant UNIQUE. */
		@Label(SQL.LABEL_UNIQUE)
		public static final String UNIQUE 		= "UNIQUE";

		/** The Constant UPDATE. */
		public static final String UPDATE 		= "UPDATE";

		/** The Constant VALUES. */
		public static final String VALUES 		= "VALUES";

		/** The Constant WHEN. */
		public static final String WHEN 		= "WHEN";

		/** The Constant WHERE. */
		public static final String WHERE 		= "WHERE";

		/** The Constant CURRENT_CATALOG. */
		public static final String CURRENT_CATALOG   = "CURRENT_USER";

		/** The Constant CURRENT_SCHEMA. */
		public static final String CURRENT_SCHEMA    = "CURRENT_SCHEMA";

		/** The Constant CURRENT_TIME. */
		public static final String CURRENT_TIME      = "CURRENT_TIME";

		/** The Constant CURRENT_TIMESTAMP. */
		public static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";

		/** The Constant CURRENT_USER. */
		public static final String CURRENT_USER      = "CURRENT_USER";


		/** The Constant DEFAULT_VALUES. */
		public static final String DEFAULT_VALUES = glue(DEFAULT,VALUES);

		//DDL
		/** The Constant CREATE_TABLE. */
		public static final String CREATE_TABLE = glue(CREATE,TABLE);

		/** The Constant CREATE_GLOBAL_TEMPORARY_TABLE. */
		public static final String CREATE_GLOBAL_TEMPORARY_TABLE = glue(CREATE,GLOBAL,TEMPORARY,TABLE);

		/** The Constant CREATE_TRIGGER. */
		public static final String CREATE_TRIGGER = glue(CREATE,TRIGGER);

		/** The Constant CREATE_PROCEDURE. */
		public static final String CREATE_PROCEDURE = glue(CREATE,PROCEDURE);

		/** The Constant DROP_TABLE. */
		public static final String DROP_TABLE = glue(DROP,TABLE);

		/** The Constant CREATE_INDEX. */
		public static final String CREATE_INDEX = glue(CREATE,INDEX);

		/** The Constant DROP_INDEX. */
		public static final String DROP_INDEX = glue(DROP,INDEX);

		/** The Constant NOT_NULL. */
		@Label(SQL.LABEL_NOTNULL)
		public static final String NOT_NULL = glue(NOT,NULL);

		/** The Constant PRIMARY_KEY. */
		public static final String PRIMARY_KEY = glue(PRIMARY,KEY);

		/** The Constant FOREIGN_KEY. */
		public static final String FOREIGN_KEY = glue(FOREIGN,KEY);

		/** The Constant TRUNCATE_TABLE. */
		public static final String TRUNCATE_TABLE 	= glue(TRUNCATE,TABLE);


		/** The Constant ROW_NUMBER. */
		public static final String ROW_NUMBER = glue(ROW,NUMBER);

		//DML
		/** The Constant INSERT_INTO. */
		public static final String INSERT_INTO = glue(INSERT,INTO);

		//Bys
		/** The Constant DISTINCT_BY. */
		public static final String DISTINCT_BY = glue(DISTINCT,BY);

		/** The Constant GROUP_BY. */
		public static final String GROUP_BY = glue(GROUP,BY);

		/** The Constant ORDER_BY. */
		public static final String ORDER_BY = glue(ORDER,BY);

		//Joins
		/** The Constant INNER_JOIN. */
		public static final String INNER_JOIN = glue(INNER,JOIN);

		/** The Constant LEFT_JOIN. */
		public static final String LEFT_JOIN = glue(LEFT,JOIN);

		/** The Constant RIGHT_JOIN. */
		public static final String RIGHT_JOIN = glue(RIGHT,JOIN);

		/** The Constant OUTER_JOIN. */
		public static final String OUTER_JOIN = glue(FULL,OUTER,JOIN);

		/** The Constant NATURAL_JOIN. */
		public static final String NATURAL_JOIN = glue(NATURAL,JOIN);

		/** The Constant IS_NOT. */
		public static final String IS_NOT = glue(IS,NOT);

		/** The Constant NOT_IN. */
		public static final String NOT_IN = glue(NOT,IN);

		/** The Constant IS_NULL. */
		public static final String IS_NULL = glue(IS,NULL);

		/** The Constant IS_NOT_NULL. */
		public static final String IS_NOT_NULL = glue(IS,NOT,NULL);

		/** The Constant TO_NUMBER. */
		public static final String TO_NUMBER = glue(TO,NUMBER);

		/** The Constant TO_CHAR. */
		public static final String TO_CHAR = glue(CHAR);

		/** The Constant UNION_ALL. */
		public static final String UNION_ALL = glue(UNION,ALL);

		/** The Constant _AS_. */
		public static final String _AS_ 	= _+AS+_;

		/** The Constant _ON_. */
		public static final String _ON_ 	= _+ON+_;

		/** The Constant __AND. */
		public static final String __AND 	= "  "+AND;

		/** The Constant __OR_. */
		public static final String __OR_ 	= "  "+OR+_;

		/** The Constant _OR_. */
		public static final String _OR_ 	= _+OR+_;

		/** The Constant _AND_. */
		public static final String _AND_ 	= _+AND+_;


	}


	/**
	 * Lookup sql table definition method name.
	 *
	 * @param label the label
	 * @return the string
	 */
	private static final String lookupSqlTableDefinitionMethodName(final String label){
		try {
			return getMemberByLabel(label, SqlTable.class.getDeclaredMethods()).getName();
		}
		catch(final RuntimeException e) {
			throw e;
		}
	}

	/**
	 * The Enum INDEXTYPE.
	 */
	public static enum INDEXTYPE
	{

		/** The NORMAL. */
		NORMAL(null, lookupSqlTableDefinitionMethodName(LABEL_METHOD_Index)),

		/** The UNIQUE. */
		UNIQUE(SQL.LANG.UNIQUE, lookupSqlTableDefinitionMethodName(LABEL_METHOD_UniqueIndex)),

		/** The BITMAP. */
		BITMAP(SQL.LANG.BITMAP, lookupSqlTableDefinitionMethodName(LABEL_METHOD_BitmapIndex)),

		/** The PRIMARYKEY. */
		PRIMARYKEY(SQL.LANG.PRIMARY_KEY, lookupSqlTableDefinitionMethodName(LABEL_METHOD_PrimaryKey)),

		/** The FOREIGNKEY. */
		FOREIGNKEY(SQL.LANG.FOREIGN_KEY, lookupSqlTableDefinitionMethodName(LABEL_METHOD_ForeignKey)),
		;



		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////

		/** The ddl string. */
		private final String ddlString;

		/** The sqltable definition method name. */
		private final String sqltableDefinitionMethodName;



		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////

		/**
		 * Instantiates a new iNDEXTYPE.
		 *
		 * @param ddlString the ddl string
		 * @param sqltableDefinitionMethodName the sqltable definition method name
		 */
		private INDEXTYPE(final String ddlString, final String sqltableDefinitionMethodName) {
			this.ddlString = ddlString;
			this.sqltableDefinitionMethodName = sqltableDefinitionMethodName;
		}



		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////

		/**
		 * To ddl string.
		 *
		 * @return the ddlString
		 */
		public String toDdlString() {
			return this.ddlString;
		}

		/**
		 * Gets the sqltable definition method name.
		 *
		 * @return the sqltableDefinitionMethodName
		 */
		public String getSqltableDefinitionMethodName() {
			return this.sqltableDefinitionMethodName;
		}

	}

	/**
	 * The Enum DATATYPE.
	 */
	public static enum DATATYPE
	{
		//6.1 <data type> Syntax Rules

		/** The BOOLEAN. */
		BOOLEAN(java.sql.Types.BOOLEAN),

		/** The TINYINT. */
		TINYINT(java.sql.Types.TINYINT),

		/** The SMALLINT. */
		SMALLINT(java.sql.Types.SMALLINT),

		/** The INT. */
		INT(java.sql.Types.INTEGER),

		/** The BIGINT. */
		BIGINT(java.sql.Types.BIGINT),

		/** The REAL. */
		REAL(java.sql.Types.REAL),

		/** The FLOAT. */
		FLOAT(java.sql.Types.FLOAT),

		/** The DOUBLE. */
		DOUBLE(java.sql.Types.DOUBLE),

		/** The NUMERIC. */
		NUMERIC(java.sql.Types.NUMERIC, false, false, true, true),

		/** The DECIMAL. */
		DECIMAL(java.sql.Types.DECIMAL, false, false, true, true),

		/** The DATE. */
		DATE(java.sql.Types.DATE),

		/** The TIME. */
		TIME(java.sql.Types.TIME),

		/** The TIMESTAMP. */
		TIMESTAMP(java.sql.Types.TIMESTAMP),

		/** The CHAR. */
		CHAR(java.sql.Types.CHAR, true, true),

		/** The VARCHAR. */
		VARCHAR(java.sql.Types.VARCHAR, true, true),

		/** The CLOB. */
		CLOB(java.sql.Types.CLOB, true, true),

		/** The NCHAR. */
		NCHAR(java.sql.Types.NCHAR, true, true),

		/** The NVARCHAR. */
		NVARCHAR(java.sql.Types.NVARCHAR, true, true),

		/** The NCLOB. */
		NCLOB(java.sql.Types.NCLOB, true, true),

		/** The BINARY. */
		BINARY(java.sql.Types.BINARY, true, false),

		/** The VARBINARY. */
		VARBINARY(java.sql.Types.VARBINARY, true, false),

		/** The BLOB. */
		BLOB(java.sql.Types.BLOB, true, false),

		/** The LONGVARCHAR. */
		LONGVARCHAR(java.sql.Types.LONGVARCHAR, CLOB.toDdlString(), true, true),

		/** The LONGVARBINARY. */
		LONGVARBINARY(java.sql.Types.LONGVARBINARY, BLOB.toDdlString(), true, false),

		/** The OBJECT. */
		OBJECT(java.sql.Types.JAVA_OBJECT),

		/** The ARRAY. */
		ARRAY(java.sql.Types.ARRAY)
		;



		/** The jdbc type. */
		private int jdbcType = 0;

		/** The lengthed. */
		private boolean lengthed = false;

		/** The literal. */
		private boolean literal = false;

		/** The precisioned. */
		private boolean precisioned = false;

		/** The scaled. */
		private boolean scaled = false;

		/** The ddl string. */
		private String ddlString = null;

		/*
		 * The scale of a numeric is the count of decimal digits in the fractional part,
		 * to the right of the decimal point.
		 * The precision of a numeric is the total count of significant digits in the whole number,
		 * that is, the number of digits to both sides of the decimal point.
		 * So the number 23.5141 has a precision of 6 and a scale of 4.
		 * Integers can be considered to have a scale of zero.
		 */



		/**
		 * Instantiates a new dATATYPE.
		 *
		 * @param jdbcType the jdbc type
		 */
		private DATATYPE(final int jdbcType) {
			this(jdbcType, null);
		}

		/**
		 * Instantiates a new dATATYPE.
		 *
		 * @param jdbcType the jdbc type
		 * @param ddlString the ddl string
		 */
		private DATATYPE(final int jdbcType, final String ddlString) {
			this(jdbcType, ddlString, false, false);
		}

		/**
		 * Instantiates a new dATATYPE.
		 *
		 * @param jdbcType the jdbc type
		 * @param lengthed the lengthed
		 * @param literal the literal
		 */
		private DATATYPE(final int jdbcType, final boolean lengthed, final boolean literal) {
			this(jdbcType, null, lengthed, literal, false, false);
		}

		/**
		 * Instantiates a new dATATYPE.
		 *
		 * @param jdbcType the jdbc type
		 * @param ddlString the ddl string
		 * @param lengthed the lengthed
		 * @param literal the literal
		 */
		private DATATYPE(final int jdbcType, final String ddlString, final boolean lengthed, final boolean literal) {
			this(jdbcType, ddlString, lengthed, literal, false, false);
		}

		/**
		 * Instantiates a new dATATYPE.
		 *
		 * @param jdbcType the jdbc type
		 * @param lengthed the lengthed
		 * @param literal the literal
		 * @param precisioned the precisioned
		 * @param scaled the scaled
		 */
		private DATATYPE(
			final int jdbcType,
			final boolean lengthed,
			final boolean literal,
			final boolean precisioned,
			final boolean scaled
		)
		{
			this(jdbcType, null, lengthed, literal, precisioned, scaled);
		}

		/**
		 * Instantiates a new dATATYPE.
		 *
		 * @param jdbcType the jdbc type
		 * @param ddlString the ddl string
		 * @param lengthed the lengthed
		 * @param literal the literal
		 * @param precisioned the precisioned
		 * @param scaled the scaled
		 */
		private DATATYPE(
			final int jdbcType,
			final String ddlString,
			final boolean lengthed,
			final boolean literal,
			final boolean precisioned,
			final boolean scaled
		)
		{
			this.jdbcType = jdbcType;
			this.lengthed = lengthed;
			this.literal = literal;
			this.precisioned = precisioned;
			this.scaled = scaled;
			this.ddlString = ddlString;
		}



		/**
		 * Checks if is lengthed.
		 *
		 * @return true, if is lengthed
		 */
		public boolean isLengthed() {
			return this.lengthed;
		}

		/**
		 * Checks if is literal.
		 *
		 * @return true, if is literal
		 */
		public boolean isLiteral() {
			return this.literal;
		}

		/**
		 * Gets the jdbc type.
		 *
		 * @return the jdbc type
		 */
		public int getJdbcType() {
			return this.jdbcType;
		}

		/**
		 * To ddl string.
		 *
		 * @return the string
		 */
		public String toDdlString() {
			return this.ddlString!=null?this.ddlString:this.name();
		}

		/**
		 * Checks if is precisioned.
		 *
		 * @return the precisioned
		 */
		public boolean isPrecisioned() {
			return this.precisioned;
		}

		/**
		 * Checks if is scaled.
		 *
		 * @return the scaled
		 */
		public boolean isScaled() {
			return this.scaled;
		}


		/**
		 * Lengthed type.
		 *
		 * @param type the type
		 * @param length the length
		 * @return the string
		 */
		private static final String lengthedType(final DATATYPE type, final int length) {
			return new StringBuilder(64).append(type).append(Punctuation.par).append(length).append(Punctuation.rap).toString();
		}

		/**
		 * CHAR.
		 *
		 * @param length the length
		 * @return the string
		 */
		public static final String CHAR(final int length) {
			return lengthedType(CHAR, length);
		}

		/**
		 * VARCHAR.
		 *
		 * @param length the length
		 * @return the string
		 */
		public static final String VARCHAR(final int length) {
			return lengthedType(VARCHAR, length);
		}

		/**
		 * BINARY.
		 *
		 * @param length the length
		 * @return the string
		 */
		public static final String BINARY(final int length) {
			return lengthedType(BINARY, length);
		}

		/**
		 * VARBINARY.
		 *
		 * @param length the length
		 * @return the string
		 */
		public static final String VARBINARY(final int length) {
			return lengthedType(VARBINARY, length);
		}

		/**
		 * LONGVARCHAR.
		 *
		 * @param length the length
		 * @return the string
		 */
		public static final String LONGVARCHAR(final int length) {
			return lengthedType(LONGVARCHAR, length);
		}

		/**
		 * LONGVARBINARY.
		 *
		 * @param length the length
		 * @return the string
		 */
		public static final String LONGVARBINARY(final int length) {
			return lengthedType(LONGVARBINARY, length);
		}

	}

	/**
	 * The Class FORMAT.
	 */
	public static abstract class FORMAT {

		/** The Constant TIMESTAMP. */
		public static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss";

		/** The Constant DATE. */
		public static final String DATE = "yyyy-MM-dd";

		/** The Constant TIME. */
		public static final String TIME = "HH:mm:ss";
	}

	/**
	 * The Class util.
	 */
	public static abstract class util {

		/**
		 * Needs escaping.
		 *
		 * @param columnType the column type
		 * @return true, if successful
		 */
		public static final boolean needsEscaping(final DATATYPE columnType){
			return columnType.ordinal() <= 6;
		}

		/** The Constant c_apo. */
		protected static final char c_apo = com.xdev.jadoth.sqlengine.SQL.Punctuation.apo;

		/**
		 * Assemble escape.
		 *
		 * @param sb the sb
		 * @param s the s
		 * @return the string builder
		 */
		public static StringBuilder assembleEscape(StringBuilder sb, final String s) {
			if(s == null) return null;
			final int len = s.length();
			if(sb == null){
				sb = new StringBuilder((int)(len*1.1 + 4));
			}
			final char[] cs = new char[len];
			s.getChars(0, len, cs, 0);
			char c = 0;
			sb.append(c_apo);
			for (int i = 0; i < len; i++) {
				c = cs[i];
				sb.append(c);
				if(c == c_apo) sb.append(c);
			}
			sb.append(c_apo);
			return sb;
		}

		/**
		 * Escape.
		 *
		 * @param s the s
		 * @return the string
		 */
		public static String escape(final String s) {
			return assembleEscape(null, s).toString();
		}

		/**
		 * Guess alias.
		 *
		 * @param tablename the tablename
		 * @return the string
		 */
		@Label({LABEL_SQL_util_guessAlias, LABEL_1Param})
		public static final String guessAlias(final String tablename) {
			return guessAlias(tablename, 2, 4);
		}

		/**
		 * Guess alias.
		 *
		 * @param tablename the tablename
		 * @param charCount the char count
		 * @return the string
		 */
		@Label({LABEL_SQL_util_guessAlias, LABEL_2Param})
		public static final String guessAlias(final String tablename, final int charCount) {
			return guessAlias(tablename, charCount, charCount);
		}

		public static final String escapeReservedWord(final String s)
		{
			if(getDefaultDBMS().getSyntax().isReservedWord(s.toUpperCase())){
				return s+'x'; //there is no reserved word that yields another r.w. when adding an 'x';
			}
			return s;
		}

		/**
		 * Guess alias.
		 *
		 * @param tablename the tablename
		 * @param minChars the min chars
		 * @param maxChars the max chars
		 * @return the string
		 */
		@Label({LABEL_SQL_util_guessAlias, LABEL_3Param})
		public static final String guessAlias(final String tablename, final int minChars, final int maxChars) {
			final int length = tablename.length();
			if(length <= minChars) {
				return tablename.toUpperCase();
			}
			final char[] chars = tablename.toCharArray();
			final char first = tablename.charAt(0);
			boolean currentUpper = Character.isUpperCase(first);
			final Vector<Character> significantChars = new Vector<Character>(maxChars);
			significantChars.add(first);
			char loopChar = first;
			boolean loopUpper = !currentUpper;

			for (int i = 0; i < chars.length; i++) {
				loopChar = tablename.charAt(i);
				loopUpper = Character.isUpperCase(loopChar);
				if(loopUpper != currentUpper) {
					significantChars.add(loopChar);
					currentUpper = ! currentUpper;
				}
			}
			final StringBuilder sb = new StringBuilder(significantChars.size());
			for (final Character c : significantChars) {
				sb.append(c);
			}
			final String reducedName = sb.toString();
			String returnCandidate = reducedName;
			if(returnCandidate.length() < minChars) {
				returnCandidate = tablename.substring(0, Math.min(Math.min((minChars + maxChars)/2+1, maxChars), length));
				return returnCandidate.toUpperCase();
			}
			if(returnCandidate.length() > maxChars) {
				returnCandidate = reducedName.replaceAll("[^a-zA-Z]", "");
			}
			if(returnCandidate.length() > maxChars) {
				returnCandidate = reducedName.replaceAll("[a-z]", "");
			}
			if(returnCandidate.length() > maxChars) {
				returnCandidate = reducedName.replaceAll("[^A-Z]", "");
			}
			if(returnCandidate.length() > maxChars) {
				returnCandidate = returnCandidate.substring(0, Math.min(maxChars, length));
			}
			return returnCandidate.toUpperCase();
		}


		/**
		 * Utility method than recognizes Boolean values.<br>
		 * <code>object</code> can have several values that are recognized as boolean values:
		 * - null value: returns null<br>
		 * - Boolean value: returns object<br>
		 * - Number value: return false for value 0, true otherwise<br>
		 * - String value (caseinsensitive): returns false for "N" or "NO" , true for "Y" or "YES", otherwise value is not recognized<br>
		 * <br>
		 * Any unrecognizable value will cause a <code>ClassCastException</code>.<br>
		 * <br>
		 * Notes:<br>
		 * - this method is inefficient if the type of <code>object</code> is already known.<br>
		 * - use this method only if you are sure that the provided value is meant to be a boolean value of some form
		 * - the main purpose of this method is to handle boolean and pseudo-boolean values returned from different
		 * DBMS generically, no matter if it's a true boolean or 0/1 or stupid 0/-1 or "YES/NO", etc.
		 *
		 * @param object the object
		 * @return the boolean
		 * @throws ClassCastException the class cast exception
		 */
		public static final Boolean recognizeBoolean(final Object object) throws ClassCastException
		{
			if(object == null) return null;

			if(object instanceof Boolean) {
				return (Boolean)object;
			}
			else if(object instanceof Number) {
				return ((Number)object).intValue() == 0;
			}
			else if(object instanceof String){
				final String upperCaseValue = object.toString().toUpperCase();
				if(upperCaseValue.equals("Y") || upperCaseValue.equals("YES")) return true;
				if(upperCaseValue.equals("N") || upperCaseValue.equals("NO")) return false;
				//else continue to throw exception
			}
			throw new ClassCastException(
				"value "+object+" of "+object.getClass()+" cannot be recognized as boolean value"
			);
		}

		/**
		 * Like <code>recognizeBoolean</code>, but maps <tt>null</tt> value to <code>false</code>.
		 *
		 * @param object the object
		 * @return true or false :-)
		 * @throws ClassCastException the class cast exception
		 */
		public static final boolean recognizeBooleanPrimitive(final Object object) throws ClassCastException
		{
			final Boolean b = recognizeBoolean(object);
			return b==null?false:b;
		}



		/**
		 * Escape if necessary.
		 *
		 * @param s the s
		 * @param sb the sb
		 * @return the string builder
		 */
		public static final StringBuilder escapeIfNecessary(final String s, StringBuilder sb)
		{
			if(sb == null){
				sb = new StringBuilder(s==null?4:(int)(s.length()*1.1 + 2));
			}
			if(s == null){
				return sb.append(SQL.LANG.NULL);
			}
			if(s.length() == 0) {
				return sb.append(apo).append(apo);
			}

			try {
				//Double can parse any number.
				Double.parseDouble(s);
				//Note that booleans must already be in the 1/0 form to avoid accidently parsing the real String "true"
				sb.append(s);
			}
			catch (final NumberFormatException e){
				assembleEscape(sb, s);
			}
			return sb;
		}

		/**
		 * Escape if necessary.
		 *
		 * @param s the s
		 * @return the string
		 */
		public static final String escapeIfNecessary(final String s) {
			return escapeIfNecessary(s, null).toString();
		}

		/**
		 * Parses the expression.
		 *
		 * @param obj the obj
		 * @return the sql expression
		 */
		public static final SqlExpression parseExpression(final Object obj) {
			if(obj == null) {
				return null;
			}
			else if(obj instanceof SqlExpression) {
				return (SqlExpression)obj;
			}
			else if(obj instanceof Boolean) {
//				return ((Boolean)obj)?SQL.TRUE:SQL.FALSE;
				return new SqlExpression((Boolean)obj?"1":"0");
			}
			else {
				return new SqlExpression(obj);
			}
		}

		/**
		 * Parses the expression array.
		 *
		 * @param objects the objects
		 * @return the sql expression[]
		 */
		public static final SqlExpression[] parseExpressionArray(final Object... objects) {
			if(objects == null) {
				return null;
			}
			else if(objects instanceof SqlExpression[]) {
				return (SqlExpression[])objects;
			}
			else {
				final SqlExpression[] exps = new SqlExpression[objects.length];
				for (int i = 0; i < exps.length; i++) {
					exps[i] = parseExpression(objects[i]);
				}
				return exps;
			}
		}

		/**
		 * Parses the condition.
		 *
		 * @param object the object
		 * @return the sql condition
		 */
		public static final SqlCondition parseCondition(final Object object) {
			if(object == null) {
				return null;
			}
			else if(object instanceof SqlCondition) {
				return (SqlCondition)object;
			}
			else {
				return new SqlCondition(object);
			}
		}


		/**
		 * Parses the full qualified tablename.
		 *
		 * @param fullQualifiedTablename the full qualified tablename
		 * @return the string[]
		 */
		public static final String[] parseFullQualifiedTablename(final String fullQualifiedTablename)
		{
			if(fullQualifiedTablename == null) return null;

			final String[] parts = new String[3];
			final int dotIndex = fullQualifiedTablename.indexOf(dot);

			String unQualTablename;
			if(dotIndex > 0){
				parts[0] = fullQualifiedTablename.substring(0, dotIndex);
				unQualTablename = fullQualifiedTablename.substring(dotIndex+1);
			}
			else {
				unQualTablename = fullQualifiedTablename;
			}

			final int firstSpaceIndex = unQualTablename.indexOf(_);
			if(firstSpaceIndex > 0){
				parts[1] = unQualTablename.substring(0, firstSpaceIndex);
				//use lastIndexOf to automatically cut out " AS ". More spaces afterwars are an error anyway.
				parts[2] = unQualTablename.substring(unQualTablename.lastIndexOf(_)+1, unQualTablename.length());
			}
			else {
				parts[1] = unQualTablename;
				parts[2] = null;
			}

			return parts;
		}


		/**
		 * Assemble timestamp.
		 *
		 * @param parts the parts
		 * @return the string
		 */
		public static final String assembleTimestamp(final int... parts) {
			final StringBuilder sb = new StringBuilder(20);
			final int length = parts.length;

			sb.append(parts[0]);

			if(length > 1) {
				sb.append("-");
				sb.append(parts[1]);
			}
			if(length > 2) {
				sb.append("-");
				sb.append(parts[2]);
			}
			if(length > 3) {
				sb.append(" ");
				sb.append(parts[3]);
			}
			if(length > 4) {
				sb.append(":");
				sb.append(parts[4]);
			}
			if(length > 5) {
				sb.append(":");
				sb.append(parts[5]);
			}
			return sb.toString();
		}


		/**
		 * Qualify.
		 *
		 * @param parts the parts
		 * @return the string
		 */
		public static final String qualify(final Object... parts) {
			return Jadoth.concat(Punctuation.dot, parts);
		}



		/**
		 * Gets the column name.
		 *
		 * @param o the o
		 * @return the column name
		 */
		public static final String getColumnName(final Object o) {
			if(o == null) {
				return null;
			}
			if(o instanceof SqlField) {
				return ((SqlField)o).getName();
			}
			else if (o instanceof String) {
				return (String)o;
			}
			else {
				return o.toString();
			}
		}


	}

}
