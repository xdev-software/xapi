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

import static com.xdev.jadoth.Jadoth.appendArray;
import static com.xdev.jadoth.Jadoth.appendArraySeperated;
import static com.xdev.jadoth.sqlengine.SQL.LANG.BEGIN_ATOMIC;
import static com.xdev.jadoth.sqlengine.SQL.LANG.CREATE_TRIGGER;
import static com.xdev.jadoth.sqlengine.SQL.LANG.END;
import static com.xdev.jadoth.sqlengine.SQL.LANG.NEW;
import static com.xdev.jadoth.sqlengine.SQL.LANG.OLD;
import static com.xdev.jadoth.sqlengine.SQL.LANG.ON;
import static com.xdev.jadoth.sqlengine.SQL.LANG.REFERENCING;
import static com.xdev.jadoth.sqlengine.SQL.LANG.WHEN;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.scol;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.assembleObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.xdev.jadoth.Jadoth;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.standard.StandardDMLAssembler;
import com.xdev.jadoth.sqlengine.internal.SqlCondition;
import com.xdev.jadoth.util.strings.StringBuilderAssemblable;



/**
 * The Class SqlTrigger.
 */
public class SqlTrigger implements StringBuilderAssemblable
{
	///////////////////////////////////////////////////////////////////////////
	// static fields    //
	/////////////////////
	/** The Constant eINSERT. */
	public static final Event eINSERT = new Event(SQL.LANG.INSERT);
	
	/** The Constant eDELETE. */
	public static final Event eDELETE = new Event(SQL.LANG.DELETE);
	
	/** The Constant eUPDATE. */
	public static final Event eUPDATE = new Event(SQL.LANG.UPDATE);

	/** The Constant actionStatmentSeperator. */
	protected static final String actionStatmentSeperator = scol+NEW_LINE;



	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////
	/**
	 * UPDAT e_ of.
	 * 
	 * @param columnNames the column names
	 * @return the event
	 */
	public static final Event UPDATE_OF(final String[] columnNames){
		return new Event("UPDATE OF", columnNames);
	}

	/**
	 * Creates the.
	 * 
	 * @param name the name
	 * @param time the time
	 * @param event the event
	 * @param table the table
	 * @param action the action
	 * @param oldRowAlias the old row alias
	 * @param newRowAlias the new row alias
	 * @param when the when
	 * @return the sql trigger
	 */
	public static final SqlTrigger create(
		final String name,
		final Time time,
		final Event event,
		final SqlTableIdentity table,
		final Object action,
		final String oldRowAlias,
		final String newRowAlias,
		final SqlCondition when
	)
	{
		return new SqlTrigger(name, time, event, table, action)
			.OLD_ROW_AS(oldRowAlias)
			.NEW_ROW_AS(newRowAlias)
			.WHEN(when)
		;
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	/** The name. */
	protected String name;
	
	/** The time. */
	protected final Time time;
	
	/** The event. */
	protected final Event event;
	
	/** The table. */
	protected SqlTableIdentity table;
	
	/** The actions. */
	protected final ArrayList<Object> actions = new ArrayList<Object>();

	/** The old row alias. */
	protected String oldRowAlias = null;
	
	/** The new row alias. */
	protected String newRowAlias = null;
	
	/** The when. */
	protected SqlCondition when = null;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new sql trigger.
	 * 
	 * @param name the name
	 * @param time the time
	 * @param event the event
	 * @param table the table
	 * @param action the action
	 */
	public SqlTrigger(
		final String name,
		final Time time,
		final Event event,
		final SqlTableIdentity table,
		final Object action
	)
	{
		super();
		this.name = name;
		this.time = time;
		this.event = event;
		this.table = table;
		if(action != null) {
			this.actions.add(action);
		}

	}


	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public Time getTime() {
		return this.time;
	}
	
	/**
	 * Gets the event.
	 * 
	 * @return the event
	 */
	public Event getEvent() {
		return this.event;
	}
	
	/**
	 * Gets the table.
	 * 
	 * @return the table
	 */
	public SqlTableIdentity getTable() {
		return this.table;
	}
	
	/**
	 * Gets the actions.
	 * 
	 * @return the actions
	 */
	public ArrayList<Object> getActions() {
		return this.actions;
	}

	/**
	 * Gets the old row alias.
	 * 
	 * @return the old row alias
	 */
	public String getOldRowAlias() {
		return this.oldRowAlias;
	}
	
	/**
	 * Gets the new row alias.
	 * 
	 * @return the new row alias
	 */
	public String getNewRowAlias() {
		return this.newRowAlias;
	}
	
	/**
	 * Gets the when.
	 * 
	 * @return the when
	 */
	public SqlCondition getWhen() {
		return this.when;
	}
	
	/**
	 * Gets the action statement seperator.
	 * 
	 * @return the action statement seperator
	 */
	protected String getActionStatementSeperator() {
		return actionStatmentSeperator;
	}


	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the old row alias.
	 * 
	 * @param oldRowAlias the new old row alias
	 */
	public void setOldRowAlias(final String oldRowAlias) {
		this.oldRowAlias = oldRowAlias;
	}
	
	/**
	 * Sets the new row alias.
	 * 
	 * @param newRowAlias the new new row alias
	 */
	public void setNewRowAlias(final String newRowAlias) {
		this.newRowAlias = newRowAlias;
	}
	
	/**
	 * Sets the when.
	 * 
	 * @param when the new when
	 */
	public void setWhen(final SqlCondition when) {
		this.when = when;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Sets the table.
	 * 
	 * @param table the new table
	 */
	public void setTable(final SqlDdlTable table) {
		this.table = table;
	}



	/**
	 * WHEN.
	 * 
	 * @param when the when
	 * @return the sql trigger
	 */
	public SqlTrigger WHEN(final SqlCondition when) {
		this.setWhen(when);
		return this;
	}
	
	/**
	 * OL d_ ro w_ as.
	 * 
	 * @param oldRowAlias the old row alias
	 * @return the sql trigger
	 */
	public SqlTrigger OLD_ROW_AS(final String oldRowAlias) {
		this.setOldRowAlias(oldRowAlias);
		return this;
	}
	
	/**
	 * NE w_ ro w_ as.
	 * 
	 * @param newRowAlias the new row alias
	 * @return the sql trigger
	 */
	public SqlTrigger NEW_ROW_AS(final String newRowAlias) {
		this.setNewRowAlias(newRowAlias);
		return this;
	}


	/**
	 * Adds the action.
	 * 
	 * @param action the action
	 * @return the sql trigger
	 */
	public SqlTrigger addAction(final Object action) {
		this.actions.add(action);
		return this;
	}
	
	/**
	 * Removes the action.
	 * 
	 * @param action the action
	 * @return the sql trigger
	 */
	public SqlTrigger removeAction(final Object action) {
		this.actions.remove(action);
		return this;
	}

	/**
	 * Assemble part create.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assemblePartCREATE(final StringBuilder sb) {
		sb.append(CREATE_TRIGGER);
		sb.append(_).append(this.name);
		sb.append(_).append(this.time);
		sb.append(_).append(this.event);
		return sb.append(NEW_LINE);
	}
	
	/**
	 * Assemble part on.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assemblePartON(final StringBuilder sb) {
		this.assembleONtable(sb);
		this.assembleREFERENCING(sb);
		return sb;
	}

	/**
	 * Assemble o ntable.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assembleONtable(final StringBuilder sb) {
		sb.append(ON).append(_).append(this.table);
		return sb;
	}


	/**
	 * Assemble referencing.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assembleREFERENCING(final StringBuilder sb) {
		if(this.oldRowAlias != null || this.newRowAlias != null) {
			sb.append(_);
			sb.append(REFERENCING);
			if(this.oldRowAlias != null) {
				sb.append(_).append(OLD)
				.append(_).append(this.oldRowAlias);
			}
			if(this.newRowAlias != null) {
				sb.append(_).append(NEW)
				.append(_).append(this.newRowAlias);
			}
		}
		return sb;
	}

	/**
	 * Assemble when.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assembleWhen(final StringBuilder sb)
	{
		if(this.when != null) {
			sb.append(WHEN).append(_).append(par);
			assembleObject(this.when, StandardDMLAssembler.getSingletonStandardDMLAssembler(), sb, 0, 0);
			sb.append(rap).append(NEW_LINE);
		}
		return sb;
	}


	/**
	 * Assemble fo r_ each.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assembleFOR_EACH(final StringBuilder sb) {
		return sb;
	}

	/**
	 * Assemble part actions.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assemblePartActions(final StringBuilder sb) {
		this.assembleFOR_EACH(sb);
		this.assembleWhen(sb);
		this.assembleTriggerCode(sb);
		return sb;
	}

	/**
	 * Assemble action statements.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	public StringBuilder assembleActionStatements(StringBuilder sb)
	{
		if(sb == null) {
			sb = new StringBuilder(1024);
		}
		final int actionCount = this.actions.size();
		final String sep = this.getActionStatementSeperator();
		for(int i = 0; i < actionCount; i++) {
			final Object action = this.actions.get(i);
			if(action == null) continue;

			if(i > 0) {
				sb.append(sep);
			}
			if(action.getClass().isArray()) {
				appendArray(sb, (Object[])action);
			}
			else {
				sb.append(action);
			}
		}
		return sb;
	}

	/**
	 * Assemble trigger code.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assembleTriggerCode(final StringBuilder sb) {
		final int actionCount = this.actions.size();
		if(actionCount > 1) {
			sb.append(BEGIN_ATOMIC).append(NEW_LINE);
		}
		this.assembleActionStatements(sb);
		if(actionCount > 1) {
			sb.append(NEW_LINE).append(END);
		}
		return sb;
	}

	/**
	 * @param sb
	 * @return
	 * @see net.jadoth.util.StringBuilderAssemblable#assembleString(java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder assembleString(StringBuilder sb) {
		if(sb == null) {
			sb = new StringBuilder(2048);
		}
		this.assemblePartCREATE(sb).append(NEW_LINE);
		this.assemblePartON(sb).append(NEW_LINE);
		this.assemblePartActions(sb);
		return sb;
	}

	/**
	 * CREAT e_ trigger.
	 * 
	 * @return the string
	 */
	public String CREATE_TRIGGER() {
		return this.toString();
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.assembleString(null).toString();
	}














	/**
	 * The Enum Time.
	 */
	public static enum Time {
		
		/** The BEFORE. */
		BEFORE, 
 /** The AFTER. */
 AFTER
	}

	/**
	 * The Class Event.
	 */
	public static final class Event implements StringBuilderAssemblable {
		
		/** The name. */
		private final String name;
		
		/** The column names. */
		private final Set<String> columnNames;


		/**
		 * Instantiates a new event.
		 * 
		 * @param name the name
		 */
		private Event(final String name) {
			this(name, (Set<String>)null);
		}

		/**
		 * Instantiates a new event.
		 * 
		 * @param name the name
		 * @param columnNames the column names
		 */
		private Event(final String name, final String[] columnNames) {
			this(name, Jadoth.addArray(new HashSet<String>(), columnNames));
		}
		
		/**
		 * Instantiates a new event.
		 * 
		 * @param name the name
		 * @param columnNames the column names
		 */
		private Event(final String name, final Set<String> columnNames) {
			super();
			this.name = name;
			this.columnNames = columnNames;
		}

		/**
		 * @return
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.assembleString(null).toString();
		}

		/**
		 * @param sb
		 * @return
		 * @see net.jadoth.util.StringBuilderAssemblable#assembleString(java.lang.StringBuilder)
		 */
		@Override
		public StringBuilder assembleString(StringBuilder sb) 
		{
			if(sb == null) {
				sb = new StringBuilder(1024);
			}
			sb.append(this.name);
			if(this.columnNames != null) {
				sb.append(_);
				appendArraySeperated(sb, ',', this.columnNames.toArray());
			}
			return sb;
		}

	}
	
}
