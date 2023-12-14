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
package com.xdev.jadoth.sqlengine;



import com.xdev.jadoth.sqlengine.DELETE.SuperWritingQuery;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.interfaces.SqlExecutor;
import com.xdev.jadoth.sqlengine.internal.AssignmentColumnsClause;
import com.xdev.jadoth.sqlengine.internal.AssignmentList;
import com.xdev.jadoth.sqlengine.internal.AssignmentValuesClause;
import com.xdev.jadoth.sqlengine.internal.ColumnValueAssignment;
import com.xdev.jadoth.sqlengine.internal.ColumnValueTuple;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.QueryListeners;
import com.xdev.jadoth.sqlengine.internal.SqlColumn;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.types.Query;
import com.xdev.jadoth.sqlengine.types.TableQuery;
import com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery;

/**
 * The Class INSERT.
 *
 * @author Thomas Muenz
 */
public class INSERT extends TableQuery.Implementation implements ValueAssigningTableQuery
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	private static final long serialVersionUID = 8510908565626401519L;


	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	/** The value select. */
	private SELECT valueSelect;

	/** The select filter. */
	private String[] selectFilter;

	/** The modified select filter. */
	private boolean modifiedSelectFilter;

	/** The filtered value select. */
	private transient SELECT cachedFilteredValueSelect;
	
	private final SuperValueAssigningQuery superValueAssigningQuery;

	private final AssignmentColumnsClause columnsClause;
	
	private final AssignmentValuesClause valuesClause;


	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Instantiates a new iNSERT.
	 */
	public INSERT()
	{
		this((AssignmentList)null);
	}		
	INSERT(final AssignmentList assignments)
	{
		this.superValueAssigningQuery = new SuperValueAssigningQuery(assignments);
		this.valueSelect = null;
		this.selectFilter = null;
		this.cachedFilteredValueSelect = null;
		this.modifiedSelectFilter = false;
		this.columnsClause = new AssignmentColumnsClause(this.superValueAssigningQuery.iterateColumns());
		this.valuesClause = new AssignmentValuesClause(this.superValueAssigningQuery.iterateValues());
	}
	protected INSERT(final INSERT copySource)
	{
		super(copySource);
		this.superValueAssigningQuery = copySource.superValueAssigningQuery.copy();
		this.valueSelect = copySource.valueSelect; //do not copy other query
		this.selectFilter = copySource.selectFilter.clone();
		this.cachedFilteredValueSelect = null; //do not copy caching fields
		this.modifiedSelectFilter = copySource.modifiedSelectFilter;
		this.columnsClause = new AssignmentColumnsClause(this.superValueAssigningQuery.iterateColumns());
		this.valuesClause = new AssignmentValuesClause(this.superValueAssigningQuery.iterateValues());
	}
	

	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * Gets the value select.
	 *
	 * @return the valueSelect
	 */
	public SELECT getValueSelect()
	{
		return this.valueSelect;
	}

	/**
	 * INTO.
	 *
	 * @param table the table
	 * @return the iNSERT
	 */
	public INSERT INTO(final SqlTableIdentity table)
	{
		this.superValueAssigningQuery.setTable(table);
		return this;
	}




	/**
	 * Columns.
	 *
	 * @param columList the colum list
	 * @return the iNSERT
	 */
	public INSERT columns(final Object... columList)
	{
		this.superValueAssigningQuery.columns(columList);
		return this;
	}



	public AssignmentColumnsClause getColumnsClause()
	{
		return this.columnsClause;
	}
	
	public AssignmentValuesClause getValuesClause()
	{
		return this.valuesClause;
	}



	/**
	 * VALUES.
	 *
	 * @param values the values
	 * @return the iNSERT
	 */
	public INSERT VALUES(final Object... values)
	{
		this.superValueAssigningQuery.setValues(values);
		return this;
	}


	/**
	 * Select.
	 *
	 * @param selectQuery the select query
	 * @return the iNSERT
	 */
	public INSERT select(final SELECT selectQuery)
	{
		this.valueSelect = selectQuery;
		return this;
	}

	/**
	 * Filter.
	 *
	 * @param fields the fields
	 * @return the iNSERT
	 */
	public INSERT filter(final Object... fields)
	{
		//TODO SqlColumn-Erkennung
		if(fields[0] instanceof String[]) {
			this.selectFilter = (String[])fields[0];
		}
		else {
			this.selectFilter = new String[fields.length];
			for(int i = 0; i < fields.length; i++) {
				this.selectFilter[i] = ((fields[i]==null)?null:fields[i].toString());
			}
		}
		this.modifiedSelectFilter = true;
		return this;
	}


	/**
	 * Filter select.
	 *
	 * @return the sELECT
	 */
	public SELECT filterSelect()
	{
		//if no source SELECT in the first place, there's nothing to filter.
		if(this.valueSelect == null){
			this.cachedFilteredValueSelect = null; //ensure consistency
			return null;
		}

		//if no filter is present, return the original source SELECT
		if(this.selectFilter == null){
			this.cachedFilteredValueSelect = null;
			return this.valueSelect;
		}

		//if filter is present and hasn't changed, don't create anew but return the existing one
		if(this.cachedFilteredValueSelect != null
			&& (SELECT)this.cachedFilteredValueSelect.getFromClause().getTable() == this.valueSelect
			&& !this.modifiedSelectFilter
		)
		{
			return this.cachedFilteredValueSelect;
		}

		//now finally, it's ensured that creating the filter is needed.

		
		//the fields of the SELECT to be used. selectFilter being not null is already ensured above.
		final String[] usedFields = this.selectFilter;
		
		//estimated size in the worst case: alias + " AS " + alias for each column => around >2 times the length.
		final StringBuilder sb = new StringBuilder(this.superValueAssigningQuery.getColumnCount() * 3);
		
		boolean isNotEmpty = false;
		int i = 0;
		for(final ColumnValueAssignment cva : this.superValueAssigningQuery.iterateAssignments()) {			
			if(i == usedFields.length) break;
			
			if(isNotEmpty){
				sb.append(',').append('\n');
			}
			else {
				isNotEmpty = true;
			}
			sb.append(usedFields[i++]).append(" AS ").append(cva.getColumn().getColumnName());			
		}
		
		//encapsulate the acutal selectQuery with the newly created filter-select.
		this.cachedFilteredValueSelect = new SELECT().items(sb.toString()).FROM(this.valueSelect).setPacked(true);
		this.modifiedSelectFilter = false;
		return this.cachedFilteredValueSelect;
	}

	/**
	 * @return
	 * @see net.net.jadoth.sqlengine.interfaces.TableQuery#getTable()
	 */
	@Override
	public SqlTableIdentity getTable()
	{
		return this.superValueAssigningQuery.getTable();
	}



	@Override
	public INSERT setName(final String name)
	{
		super.setName(name);
		return this;
	}

	@Override
	public INSERT setComment(final String comment)
	{
		super.setComment(comment);
		return this;
	}
	
	
	/**
	 * @param databaseGateway
	 * @return
	 */
	@Override
	public INSERT setDatabaseGateway(final DatabaseGateway<?> databaseGateway)
	{
		super.setDatabaseGateway(databaseGateway);
		return this;
	}
	
	@Override
	public INSERT pack()
	{
		super.pack();
		return this;
	}

	@Override
	public INSERT setPacked(final boolean packed)
	{
		super.setPacked(packed);
		return this;
	}

	@Override
	public INSERT setSingleLineMode(final boolean singleLineMode)
	{
		super.setSingleLineMode(singleLineMode);
		return this;
	}

	@Override
	public INSERT singleLineMode()
	{
		super.singleLineMode();
		return this;
	}

	



	/**
	 * @return
	 * @see net.net.jadoth.sqlengine.interfaces.TableQuery#keyword()
	 */
	@Override
	public String keyword()
	{
		return SQL.LANG.INSERT;
	}

	
	@Override
	public INSERT addColumn(final Object column)
	{
		this.superValueAssigningQuery.addColumn(column);
		return this;
	}

	@Override
	public INSERT addColumns(final Object... columns)
	{
		this.superValueAssigningQuery.addColumns(columns);
		return this;
	}

	@Override
	public INSERT assign(final Object column, final Object value)
	{
		this.superValueAssigningQuery.assign(column, value);
		return this;
	}
	
	/**
	 * @param keyValueTuples
	 * @return
	 */
	@Override
	public INSERT assign(final ColumnValueTuple... columnValueTuples)
	{
		this.superValueAssigningQuery.assign(columnValueTuples);
		return this;
	}

	@Override
	public INSERT clearAssignments()
	{
		this.superValueAssigningQuery.clearAssignments();
		return this;
	}

	@Override
	public ColumnValueAssignment[] getAssignments()
	{
		return this.superValueAssigningQuery.getAssignments();
	}

	@Override
	public int getColumnCount()
	{
		return this.superValueAssigningQuery.getColumnCount();
	}

	@Override
	public SqlColumn[] getColumns()
	{
		return this.superValueAssigningQuery.getColumns();
	}

	@Override
	public Object[] getValues()
	{
		return this.superValueAssigningQuery.getValues();
	}

	@Override
	public Iterable<ColumnValueAssignment> iterateAssignments()
	{
		return this.superValueAssigningQuery.iterateAssignments();
	}

	@Override
	public Iterable<SqlColumn> iterateColumns()
	{
		return this.superValueAssigningQuery.iterateColumns();
	}

	@Override
	public Iterable<Object> iterateValues()
	{
		return this.superValueAssigningQuery.iterateValues();
	}

	@Override
	public INSERT setValue(final int index, final Object value)
	{
		this.superValueAssigningQuery.setValue(index, value);
		return this;
	}

	@Override
	public INSERT setValues(final Object... values)
	{
		this.superValueAssigningQuery.setValues(values);
		return this;
	}

	/**
	 * @param parameters
	 * @return
	 * @throws SQLEngineException
	 * @see net.net.jadoth.sqlengine.interfaces.TableQuery#execute(java.lang.Object[])
	 */
	@Override
	public Integer execute(final Object... parameters) throws SQLEngineException 
	{
		return this.execute(SqlExecutor.update, parameters);
	}
	
	@Override
	public INSERT copy()
	{
		return new INSERT(this);
	}
	/**
	 * @return
	 * @throws CloneNotSupportedException is never thrown at the moment
	 */
	@Override
	public INSERT clone() throws CloneNotSupportedException
	{
		return this.copy();
	}



	protected class SuperValueAssigningQuery extends ValueAssigningTableQuery.Implementation
	{	
		protected SuperValueAssigningQuery()
		{
			super();
		}
		protected SuperValueAssigningQuery(final SqlTableIdentity table)
		{
			super(table);
		}
		protected SuperValueAssigningQuery(final AssignmentList assignments)
		{
			super(assignments);			
		}
		protected SuperValueAssigningQuery(final SuperValueAssigningQuery copySource)
		{
			super(copySource);
		}
		
		@Override 
		protected void setTable(final SqlTableIdentity table)
		{
			super.setTable(table);
		}		
		
		@Override public String assemble(final Object... parameters){
			return null;
		}
		@Override public Integer execute(final Object... parameters){
			return null;
		}
		@Override public String getComment(){
			return null;
		}
		@Override public String[] getCommentLines(){
			return null;
		}
		@Override public DatabaseGateway<?> getDatabaseGatewayForExecution(){
			return null;
		}
		@Override
		public String keyword(){
			return null;
		}
		@Override public String getName(){
			return null;
		} public boolean isPacked(){
			return false;
		}
		@Override public boolean isSingleLineMode(){
			return false;
		}
		@Override public int prepare(){
			return 0;
		}
		@Override public SuperValueAssigningQuery copy(){
			return new SuperValueAssigningQuery(this);
		}
		@Override public TableQuery pack(){
			return null;
		}
		@Override public TableQuery setPacked(final boolean packed){
			return null;
		}
		@Override public TableQuery setSingleLineMode(final boolean singleLineMode){
			return null;
		}
		@Override public TableQuery singleLineMode(){
			return null;
		}
		@Override public TableQuery setComment(final String comment){
			return null;
		}
		@Override public TableQuery setName(final String name){
			return null;
		}		
		@Override public SuperWritingQuery setDatabaseGateway(final DatabaseGateway<?> databaseGateway){
			return null;
		}
		@Override public boolean isDatabaseGatewaySet(){
			return false;
		}
		@Override public QueryListeners getListeners(){
			return null;
		}
		@Override public QueryListeners listeners(){
			return null;
		}
		@Override public Query setListeners(QueryListeners queryListeners){
			return null;
		}
				
	}

}
