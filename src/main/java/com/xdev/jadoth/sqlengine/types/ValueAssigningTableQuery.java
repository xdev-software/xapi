
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

import com.xdev.jadoth.sqlengine.internal.AssignmentList;
import com.xdev.jadoth.sqlengine.internal.ColumnValueAssignment;
import com.xdev.jadoth.sqlengine.internal.ColumnValueTuple;
import com.xdev.jadoth.sqlengine.internal.SqlColumn;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;


/**
 * The Interface ValueAssigningQuery.
 */
public interface ValueAssigningTableQuery extends WritingTableQuery
{
	public ValueAssigningTableQuery assign(Object column, Object value);
	
	public ValueAssigningTableQuery assign(ColumnValueTuple... columnValueAssignments);

	public ValueAssigningTableQuery columns(Object... columns);

	public ValueAssigningTableQuery addColumns(Object... columns);

	public ValueAssigningTableQuery addColumn(Object column);

	public ValueAssigningTableQuery setValues(Object... values);

	public ValueAssigningTableQuery setValue(int index, Object value);

	public ValueAssigningTableQuery clearAssignments();

	public int getColumnCount();

	public SqlColumn[] getColumns();

	public Object[] getValues();

	public ColumnValueAssignment[] getAssignments();



	public Iterable<SqlColumn> iterateColumns();

	public Iterable<Object> iterateValues();

	public Iterable<ColumnValueAssignment> iterateAssignments();
	
	
	
	abstract class Implementation extends WritingTableQuery.Implementation implements ValueAssigningTableQuery
	{
		private final AssignmentList assignments;
		
		protected Implementation()
		{
			this((SqlTableIdentity)null);
		}
		
		protected Implementation(final AssignmentList assignments)
		{
			this(null, assignments);
		}	
		
		protected Implementation(final SqlTableIdentity table)
		{
			this(table, null);
		}
		
		protected Implementation(final SqlTableIdentity table, final AssignmentList assignments)
		{
			super(table);
			this.assignments = assignments != null ?assignments :new AssignmentList();
		}

		protected Implementation(final ValueAssigningTableQuery.Implementation copySource)
		{
			super(copySource);
			this.assignments = copySource.assignments;
		}
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////

		/**
		 * Columns.
		 *
		 * @param columns the colum list
		 * @return the uPDATE
		 */
		public ValueAssigningTableQuery columns(final Object... columns)
		{
			this.clearAssignments();
			return this.addColumns(columns);
		}


		protected AssignmentList getAssignmentList()
		{
			return this.assignments;
		}



		@Override
		public ValueAssigningTableQuery assign(final Object column, final Object value)
		{
			this.assignments.assign(SqlColumn.wrap(column), value);
			return this;
		}
		
		/**
		 * @param keyValueTuples
		 * @return
		 */
		@Override
		public ValueAssigningTableQuery assign(final ColumnValueTuple... columnValueAssignments)
		{
			if(columnValueAssignments == null) return this;

			final AssignmentList assignments = this.assignments;
			for(final ColumnValueTuple cva : columnValueAssignments) {
				assignments.assign(cva);
			}			
			return this;
		}
		
		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#iterateAssignments()
		 */
		@Override
		public Iterable<ColumnValueAssignment> iterateAssignments()
		{
			return this.assignments;
		}



		@Override
		public int getColumnCount()
		{
			return this.assignments.size();
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#iterateColumns()
		 */
		@Override
		public Iterable<SqlColumn> iterateColumns()
		{
			return this.assignments.columns();
		}



		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#getAssignments()
		 */
		@Override
		public ColumnValueAssignment[] getAssignments()
		{
			return this.assignments.getAssignments();
		}



		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#getColumns()
		 */
		@Override
		public SqlColumn[] getColumns()
		{
			return this.assignments.getColumns();
		}



		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#getValues()
		 */
		@Override
		public Object[] getValues()
		{
			return this.assignments.getValues();
		}



		/**
		 * @param values
		 * @return
		 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#setValues(java.lang.Object[])
		 */
		@Override
		public ValueAssigningTableQuery setValues(final Object... values)
		{
			if(values == null) return this;

			final int columnCount = this.assignments.size();
			final int valueCount = values.length;
			if(valueCount > columnCount) {
				throw new IndexOutOfBoundsException("More values than columns ("+valueCount+" > "+columnCount);
			}

			int i = 0;
			for(final ColumnValueAssignment cva : this.assignments){
				cva.setValue(values[i++]);
				if(i == valueCount) break;
			}
			return this;
		}



		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#iterateValues()
		 */
		@Override
		public Iterable<Object> iterateValues()
		{
			return this.assignments.values();
		}


		/**
		 * @param column
		 * @return
		 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#addColumn(java.lang.Object)
		 */
		@Override
		public ValueAssigningTableQuery addColumn(final Object column)
		{
			return this.assign(SqlColumn.wrap(column), null);
		}


		/**
		 * @param columns
		 * @return
		 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#addColumns(java.lang.Object[])
		 */
		@Override
		public ValueAssigningTableQuery addColumns(final Object... columns)
		{
			final AssignmentList assignments = this.assignments;
			for(final Object c : columns) {
				assignments.addColumn(SqlColumn.wrap(c));
			}
			return this;
		}


		/**
		 * @param index
		 * @param value
		 * @return
		 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#setValue(int, java.lang.Object)
		 */
		@Override
		public ValueAssigningTableQuery setValue(final int index, final Object value)
		{
			this.assignments.get(index).setValue(value);
			return this;
		}


		/**
		 * @return
		 * @see com.xdev.jadoth.sqlengine.types.ValueAssigningTableQuery#clearAssignments()
		 */
		@Override
		public ValueAssigningTableQuery clearAssignments()
		{
			this.assignments.clear();
			return this;
		}


		

	}

}
