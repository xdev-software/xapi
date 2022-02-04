/**
 *
 */
package com.xdev.jadoth.sqlengine.internal;

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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.xdev.jadoth.collections.GrowList;
import com.xdev.jadoth.lang.Copyable;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;


/**
 * @author Thomas Muenz
 *
 */
public final class AssignmentList implements List<ColumnValueAssignment>, Copyable
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private final GrowList<ColumnValueAssignment> assignments;
	private final Values valueIterable;
	private final Columns columnIterable;

	//caching fields, not relevant for anything special (serialisation, copiing)
	private transient SqlColumn[] cachedColumnArray = null;
	private transient Object[] cachedValueArray = null;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	public AssignmentList()
	{
		super();
		this.assignments = new GrowList<ColumnValueAssignment>(100);
		this.valueIterable = new Values();
		this.columnIterable = new Columns();
	}

	protected AssignmentList(final AssignmentList copySource)
	{
		super();
		this.assignments = new GrowList<ColumnValueAssignment>(copySource.assignments);
		//these two don't have to be copied as they are just stateless iterator providers
		this.valueIterable = new Values();
		this.columnIterable = new Columns();
	}


	@Override
	public int size()
	{
		return this.assignments.size();
	}


	public void addColumn(final SqlColumn column)
	{
		if(column == null) {
			throw new NullPointerException("column is null");
		}
		this.assign(column, null);
	}

	public void assign(final ColumnValueTuple columnValueAssignment)
	{
		this.clearCacheArray();
		this.assignments.add(new Assignment(columnValueAssignment.getColumn(), columnValueAssignment.getValue()));
	}

	public void assign(final SqlColumn column, final Object value)
	{
		this.assign(new Assignment(column, value));
	}


	private final class ColumnIterator implements Iterator<SqlColumn>
	{
		private int currentIndex = 0;

		/**
		 * @return
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			return this.currentIndex < AssignmentList.this.assignments.size();
		}

		/**
		 * @return
		 * @see java.util.Iterator#next()
		 */
		@Override
		public SqlColumn next()
		{
			return AssignmentList.this.assignments.get(this.currentIndex++).getColumn();
		}

		/**
		 * Not supported.
		 *
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove()
		{
			//empty
		}

	}


	private final class Columns implements Iterable<SqlColumn>
	{
		/**
		 * @return
		 * @see java.lang.Iterable#iterator()
		 */
		@Override
		public Iterator<SqlColumn> iterator()
		{
			return new ColumnIterator();
		}

	}
	private final class Values implements Iterable<Object>
	{
		/**
		 * @return
		 * @see java.lang.Iterable#iterator()
		 */
		@Override
		public Iterator<Object> iterator()
		{
			return new ValueIterator();
		}

	}

	private final class ValueIterator implements Iterator<Object>
	{
		private int currentIndex = 0;

		/**
		 * @return
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			return this.currentIndex < AssignmentList.this.assignments.size();
		}

		/**
		 * @return
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Object next()
		{
			return AssignmentList.this.assignments.get(this.currentIndex++).getValue();
		}

		/**
		 * Not supported.
		 *
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove()
		{
			//empty
		}

	}


	public SqlColumn[] getColumns()
	{
		SqlColumn[] cachedColumnArray = this.cachedColumnArray;
		final GrowList<ColumnValueAssignment> assignments = this.assignments;
		if(cachedColumnArray == null) {
			cachedColumnArray = new SqlColumn[assignments.size()];
			for(int i = 0; i < cachedColumnArray.length; i++){
				cachedColumnArray[i] = assignments.get(i).getColumn();
			}
			this.cachedColumnArray = cachedColumnArray;
		}
		return cachedColumnArray;
	}
	public Object[] getValues()
	{
		Object[] cachedValueArray = this.cachedValueArray;
		final GrowList<ColumnValueAssignment> assignments = this.assignments;
		if(cachedValueArray == null) {
			cachedValueArray = new SqlColumn[assignments.size()];
			for(int i = 0; i < cachedValueArray.length; i++){
				cachedValueArray[i] = assignments.get(i).getValue();
			}
			this.cachedValueArray = cachedValueArray;
		}
		return cachedValueArray;
	}
	public ColumnValueAssignment[] getAssignments()
	{
		return this.assignments.toArray(new ColumnValueAssignment[this.assignments.size()]);
	}

	private void clearCacheArray()
	{
		this.cachedColumnArray = null;
		this.cachedValueArray = null;
	}

	public Iterable<SqlColumn> columns()
	{
		return this.columnIterable;
	}
	public Iterable<Object> values()
	{
		return this.valueIterable;
	}

	private final class Assignment extends QueryPart implements ColumnValueAssignment
	{
		///////////////////////////////////////////////////////////////////////////
		// constants        //
		/////////////////////
		/**
		 *
		 */
		private static final long serialVersionUID = -4936746366420582686L;



		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		private final SqlColumn column;
		private Object value;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		private Assignment(final SqlColumn column, final Object value)
		{
			super();
			this.column = column;
			this.value = value;
		}

		protected Assignment(final Assignment copySource)
		{
			this.column = copySource.column;
			this.value = copySource.value;
		}



		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////

		@Override
		public SqlColumn getColumn()
		{
			return this.column;
		}

		@Override
		public Object getValue()
		{
			return this.value;
		}



		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
		/**
		 * @param value
		 * @see com.xdev.jadoth.sqlengine.internal.ColumnValueAssignment#setValue(java.lang.Object)
		 */
		@Override
		public void setValue(final Object value)
		{
			if(value != this.value){
				AssignmentList.this.cachedValueArray = null;
			}
			this.value = value;
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////
		/**
		 * @param dmlAssembler
		 * @param sb
		 * @param indentLevel
		 * @param flags
		 * @return
		 */
		@Override
		protected StringBuilder assemble(
			final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
		)
		{
			dmlAssembler.assembleExpression(this.column, sb, indentLevel, flags);
			sb.append(" = ");
			assembleObject(this.value, dmlAssembler, sb, indentLevel, flags);
			return sb;
		}

		/**
		 * @return
		 */
		@Override
		public Assignment copy()
		{
			return new Assignment(this);
		}
	}

	/**
	 * @return
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ColumnValueAssignment> iterator()
	{
		return new AssignmentIterator();
	}

	private final class AssignmentIterator implements Iterator<ColumnValueAssignment>
	{
		private int currentIndex = 0;
		/**
		 * @return
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			return this.currentIndex < AssignmentList.this.assignments.size();
		}

		/**
		 * @return
		 * @see java.util.Iterator#next()
		 */
		@Override
		public ColumnValueAssignment next()
		{
			return AssignmentList.this.assignments.get(this.currentIndex++);
		}


		/**
		 * Not supported.
		 *
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove()
		{
			//empty
		}

	}


	/**
	 * @return
	 * @see net.jadoth.abstraction.interfaces.Copyable#copy()
	 */
	@Override
	public AssignmentList copy()
	{
		return new AssignmentList(this);
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	@Override
	public boolean add(final ColumnValueAssignment e)
	{
		return this.assignments.add(e);
	}

	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	@Override
	public void add(final int index, final ColumnValueAssignment element)
	{
		this.assignments.add(index, element);

	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(final Collection<? extends ColumnValueAssignment> c)
	{
		return this.assignments.addAll(c);
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(final int index, final Collection<? extends ColumnValueAssignment> c)
	{
		return this.assignments.addAll(index, c);
	}

	/**
	 *
	 * @see java.util.List#clear()
	 */
	@Override
	public void clear()
	{
		this.assignments.clear();

	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(final Object o)
	{
		return this.assignments.contains(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(final Collection<?> c)
	{
		return this.assignments.containsAll(c);
	}


	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	@Override
	public ColumnValueAssignment get(final int index)
	{
		return this.assignments.get(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(final Object o)
	{
		return this.assignments.indexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return this.assignments.isEmpty();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	@Override
	public int lastIndexOf(final Object o)
	{
		return this.assignments.lastIndexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	@Override
	public ListIterator<ColumnValueAssignment> listIterator()
	{
		return this.assignments.listIterator();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public ListIterator<ColumnValueAssignment> listIterator(final int index)
	{
		return this.assignments.listIterator(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(final Object o)
	{
		return this.assignments.remove(o);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	@Override
	public ColumnValueAssignment remove(final int index)
	{
		return this.assignments.remove(index);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(final Collection<?> c)
	{
		return this.assignments.removeAll(c);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(final Collection<?> c)
	{
		return this.assignments.retainAll(c);
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	@Override
	public ColumnValueAssignment set(final int index, final ColumnValueAssignment element)
	{
		return this.assignments.set(index, element);
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public List<ColumnValueAssignment> subList(final int fromIndex, final int toIndex)
	{
		return this.assignments.subList(fromIndex, toIndex);
	}

	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	@Override
	public Object[] toArray()
	{
		return this.assignments.toArray();
	}

	/**
	 * @param <T>
	 * @param a
	 * @return
	 * @see java.util.List#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(final T[] a)
	{
		return this.assignments.toArray(a);
	}

}
