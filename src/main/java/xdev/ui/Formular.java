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
package xdev.ui;


import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import xdev.db.DBException;
import xdev.db.QueryInfo;
import xdev.db.sql.Condition;
import xdev.db.sql.WHERE;
import xdev.lang.cmd.Query;
import xdev.ui.event.FormularListener;
import xdev.vt.KeyValues;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableException;


/**
 * The {@link Formular} is a form container for {@link FormularComponent}s and
 * {@link ManyToManyComponent}s.
 * 
 * <p>
 * The {@link Formular} can be used to display a row of a {@link VirtualTable}.
 * Therefore the {@link Formular} must contain {@link FormularComponent}s that
 * are mapped to the columns of the {@link VirtualTable} you want to display.
 * This can be done by the XDEV IDE or manually.
 * </p>
 * 
 * <p>
 * The {@link Formular} also manages n:m-relations. Therefore
 * {@link ManyToManyComponent}s can be added into this container.
 * </p>
 * 
 * <p>
 * The {@link Formular} provides methods to:
 * <ul>
 * <li>display {@link VirtualTableRow}s like
 * {@link #setModel(xdev.vt.VirtualTable.VirtualTableRow)}</li>
 * <li>insert {@link VirtualTableRow}s like {@link #insert(boolean)}</li>
 * <li>update (before loaded) {@link VirtualTableRow}s like
 * {@link #update(boolean)}</li>
 * <li>delete (before loaded) {@link VirtualTableRow}s like
 * {@link #delete(boolean)}</li>
 * </ul>
 * </p>
 * 
 * @author XDEV Software
 * 
 * @see XdevFormular
 * @see XdevVirtualFormular
 * @see FormularListener
 * @see FormularComponent
 * @see MasterDetailComponent
 * @see ManyToManyComponent
 * 
 * @since 4.0
 */
public interface Formular
{
	/**
	 * Key used to store form references in client properties
	 */
	public final static String	CLIENT_PROPERTY_KEY	= "form";
	
	
	
	/**
	 * Current working state of a {@link Formular}.
	 * 
	 * @see Formular#getWorkingState()
	 */
	public static enum WorkingState
	{
		/**
		 * No special action is in progress.
		 */
		IDLE,
		
		/**
		 * The formular's model is currently beeing updated.
		 */
		ADJUSTING_MODEL,
		
		/**
		 * The formular's values are currently beeing restored.
		 */
		RESTORING
	}
	
	
	/**
	 * Returns the working state of the form.
	 * 
	 * @return the formular's current state
	 */
	public WorkingState getWorkingState();
	
	
	/**
	 * @return all form components of this form
	 */
	public Iterable<FormularComponent> formComponents();
	
	
	/**
	 * @return all many to many components of this form
	 */
	public Iterable<ManyToManyComponent> manyToManyComponents();
	
	
	/**
	 * Registers a {@link FormularListener} so that it will receive form events.
	 * 
	 * @param l
	 *            the listener to register
	 */
	public void addFormularListener(FormularListener l);
	
	
	/**
	 * Removes the listener from the listener list of this form.
	 * 
	 * @param l
	 *            the listener to remove
	 */
	public void removeFormularListener(FormularListener l);
	
	
	/**
	 * @return all registered form listeners of this form
	 */
	public FormularListener[] getFormularListeners();
	
	/**
	 * Identifies a change in the saveStateAfterModelUpdate property.
	 */
	public final static String	SAVE_STATE_AFTER_MODEL_UPDATE_PROPERTY	= "saveStateAfterModelUpdate";
	
	
	/**
	 * Sets if the formular should save its state after an update of the model.
	 * 
	 * @param saveStateAfterModelUpdate
	 *            <code>true</code> if the state should be saved,
	 *            <code>false</code> otherwise
	 * 
	 * @see #setModel(VirtualTable.VirtualTableRow)
	 * @see #saveState()
	 */
	public void setSaveStateAfterModelUpdate(boolean saveStateAfterModelUpdate);
	
	
	/**
	 * Determines if the formular saves its state after an update of the model.
	 * 
	 * @return <code>true</code> if the state is saved, <code>false</code>
	 *         otherwise
	 * 
	 * @see #setModel(VirtualTable.VirtualTableRow)
	 * @see #saveState()
	 */
	public boolean getSaveStateAfterModelUpdate();
	
	
	/**
	 * Saves the state of all {@link FormularComponent}s in this formular.
	 * 
	 * @see #reset()
	 * @see FormularComponent#saveState()
	 */
	public void saveState();
	
	
	/**
	 * Resets this {@link Formular} / the mapped components in the
	 * {@link Formular} to the default values of the components.
	 * <p>
	 * This method is a synonym for {@link #reset()}
	 * 
	 * @see #saveState()
	 * @see FormularComponent#restoreState()
	 */
	public void restoreState();
	
	
	/**
	 * Resets this {@link Formular} / the mapped components in the
	 * {@link Formular} to the default values of the components.
	 * <p>
	 * This method is a synonym for {@link #restoreState()}
	 * 
	 * @see #saveState()
	 * @see FormularComponent#restoreState()
	 */
	public void reset();
	
	
	/**
	 * Checks if one of the {@link FormularComponent}'s or
	 * {@link ManyToManyComponent}'s state since the last call of
	 * {@link #saveState()} has changed.
	 * 
	 * @return <code>true</code> if one component's state has changed,
	 *         <code>false</code> otherwise
	 * 
	 * @see FormularComponent#hasStateChanged()
	 * @see ManyToManyComponent#hasStateChanged()
	 */
	public boolean hasStateChanged();
	
	
	/**
	 * Resets this {@link Formular} / the mapped components in the
	 * {@link Formular} to the default values of the specified
	 * {@link VirtualTable} <code>vt</code>.
	 * 
	 * @param vt
	 *            {@link VirtualTable} to reset on.
	 * 
	 */
	public void setModel(VirtualTable vt);
	
	
	/**
	 * Resets this {@link Formular} / the mapped components in the
	 * {@link Formular} to the default values of the specified
	 * {@link VirtualTable} <code>vt</code>.
	 * 
	 * <p>
	 * Alias for <code>setModel(vt.createRow())</code>
	 * </p>
	 * 
	 * @param vt
	 *            {@link VirtualTable} to reset on.
	 * 
	 */
	public void reset(VirtualTable vt);
	
	
	/**
	 * Fills this {@link Formular} / the mapped components in the
	 * {@link Formular} with the values provided by the specified
	 * <code>row</code> in the {@link VirtualTable} <code>vt</code>.
	 * 
	 * @param row
	 *            the row index of the {@link VirtualTable}
	 * @param vt
	 *            {@link VirtualTable} to take the data from.
	 * 
	 * @see VirtualTableRow
	 * @see #setModel(VirtualTable.VirtualTableRow)
	 */
	public void setModel(int row, VirtualTable vt);
	
	
	/**
	 * Fills this {@link Formular} / the mapped components in the
	 * {@link Formular} with the values provided by the specified
	 * <code>row</code>.
	 * 
	 * @param virtualTableRow
	 *            {@link VirtualTableRow} to take the data from.
	 * 
	 * @see VirtualTableRow
	 * @see #setModel(int, VirtualTable)
	 */
	public void setModel(final VirtualTableRow virtualTableRow);
	
	
	/**
	 * Adds an arbitrary name/value "hidden field" to this form.
	 * <p>
	 * If value is <code>null</code> this method will remove the field.
	 * 
	 * @param name
	 *            the new hidden field name
	 * @param value
	 *            the new hidden field value; if <code>null</code> this method
	 *            will remove the field
	 * @see #getHiddenField(String)
	 * @see #getHiddenFieldNames()
	 */
	public void putHiddenField(String name, Object value);
	
	
	/**
	 * Returns the value of the hidden field with the specified name. Only
	 * fields added with <code>putHiddenField</code> will return a non-
	 * <code>null</code> value.
	 * 
	 * @param name
	 *            the being queried
	 * @return the value of this hidden field or <code>null</code>
	 * @see #putHiddenField(String, Object)
	 * @see #getHiddenFieldNames()
	 */
	public Object getHiddenField(String name);
	
	
	/**
	 * Returns all hidden field names of this form.
	 * 
	 * @return all hidden field names of this form
	 * @see #putHiddenField(String, Object)
	 * @see #getHiddenField(String)
	 */
	public Iterable<String> getHiddenFieldNames();
	
	
	/**
	 * Returns a {@link Map} containing all values of this {@link Formular} /
	 * the mapped components in the {@link Formular}. The key of the {@link Map}
	 * is the component name; the value is the value of that component.
	 * 
	 * @param withNulls
	 *            <code>true</code> if null values should be returned; otherwise
	 *            <code>false</code>
	 * @return a {@link Map} containing all values of this {@link Formular} /
	 *         the mapped components in the {@link Formular}.
	 */
	public Map<String, Object> getData(boolean withNulls);
	
	
	/**
	 * Returns the current assigned {@link VirtualTableRow}.
	 * 
	 * @return The current assigned {@link VirtualTableRow}
	 * 
	 * @see #setModel(VirtualTable.VirtualTableRow)
	 */
	public VirtualTableRow getVirtualTableRow();
	
	
	/**
	 * Returns the assigned {@link VirtualTable} of this {@link Formular}.
	 * 
	 * @return The current assigned {@link VirtualTable}
	 */
	public VirtualTable getVirtualTable();
	
	
	/**
	 * <p>
	 * Alias for <code>save(true).</code>
	 * </p>
	 * 
	 * @throws VirtualTableException
	 *             if the new row can't be set to the {@link VirtualTable}.
	 * @throws DBException
	 *             if the new row can't be propagated to the underling database.
	 * 
	 * @see #save(boolean)
	 */
	public void save() throws VirtualTableException, DBException;
	
	
	/**
	 * <p>
	 * Propagates the values of this {@link Formular} / the mapped components in
	 * the {@link Formular} to the set {@link VirtualTable} as new row if the
	 * row is new to the {@link VirtualTable}, otherwise an update is performed.
	 * </p>
	 * 
	 * 
	 * @param synchronizeDB
	 *            <code>true</code> if the new row should be propagated to the
	 *            underling database as well; otherwise <code>false</code>.
	 * @throws VirtualTableException
	 *             if the new row can't be set to the {@link VirtualTable}.
	 * @throws DBException
	 *             if the new row can't be propagated to the underling database.
	 */
	public void save(boolean synchronizeDB) throws VirtualTableException, DBException;
	
	
	/**
	 * Alias for <code>update(true)</code>.
	 * 
	 * @throws VirtualTableException
	 *             if the values can't be set to the {@link VirtualTable}.
	 * @throws DBException
	 *             if the values can't be propagated to the underling database.
	 * 
	 * @see #update(boolean)
	 */
	public void update() throws VirtualTableException, DBException;
	
	
	/**
	 * <p>
	 * Propagates the values of this {@link Formular} / the mapped components in
	 * the {@link Formular} to the set {@link VirtualTable} / the set
	 * {@link VirtualTableRow}.
	 * </p>
	 * 
	 * <p>
	 * <strong>Warning:</strong> In order to be able to update a row, you need
	 * to set a {@link VirtualTableRow} for this {@link Formular}. You can do
	 * this by calling {@link #setModel(int, VirtualTable)} or
	 * {@link #setModel(VirtualTable.VirtualTableRow)}.
	 * </p>
	 * 
	 * @param synchronizeDB
	 *            <code>true</code> if the changes should be propagated to the
	 *            underling database as well; otherwise <code>false</code>.
	 * @throws VirtualTableException
	 *             if the values can't be set to the {@link VirtualTable}.
	 * @throws DBException
	 *             if the values can't be propagated to the underling database.
	 * 
	 * @see #setModel(VirtualTable.VirtualTableRow)
	 */
	public void update(boolean synchronizeDB) throws VirtualTableException, DBException;
	
	
	/**
	 * <p>
	 * Propagates the values of this {@link Formular} / the mapped components in
	 * the {@link Formular} to the specified {@link VirtualTable}
	 * <code>vt</code>, to the rows which matches the given {@link KeyValues}
	 * <code>pkv</code>.
	 * </p>
	 * 
	 * @param vt
	 *            {@link VirtualTable} to propagate the changes to
	 * @param keyValues
	 *            The values which identify the rows to update
	 * @param synchronizeDB
	 *            <code>true</code> if the changes should be propagated to the
	 *            underling database as well; otherwise <code>false</code>.
	 * @throws VirtualTableException
	 *             if the values can't be set to the {@link VirtualTable}.
	 * @throws DBException
	 *             if the values can't be propagated to the underling database.
	 */
	public void updateRowsInVT(VirtualTable vt, KeyValues keyValues, boolean synchronizeDB)
			throws VirtualTableException, DBException;
	
	
	/**
	 * <p>
	 * Propagates the values of this {@link Formular} / the mapped components in
	 * the {@link Formular} to the set {@link VirtualTable} as new row.
	 * </p>
	 * 
	 * @param vt
	 *            {@link VirtualTable} to propagate the new row to
	 * 
	 * @param synchronizeDB
	 *            <code>true</code> if the changes should be propagated to the
	 *            underling database as well; otherwise <code>false</code>.
	 * @throws VirtualTableException
	 *             if the values can't be set to the {@link VirtualTable}.
	 * @throws DBException
	 *             if the values can't be propagated to the underling database.
	 */
	public void insertRowInVT(VirtualTable vt, boolean synchronizeDB) throws VirtualTableException,
			DBException;
	
	
	/**
	 * <p>
	 * Alias for <code>insert(true).</code>
	 * </p>
	 * 
	 * @throws VirtualTableException
	 *             if the new row can't be set to {@link VirtualTable}.
	 * @throws DBException
	 *             if the new row can't be propagated to underling database.
	 * 
	 * @see #insert(boolean)
	 */
	public void insert() throws VirtualTableException, DBException;
	
	
	/**
	 * <p>
	 * Propagates the values of this {@link Formular} / the mapped components in
	 * the {@link Formular} to the set {@link VirtualTable} as new row.
	 * </p>
	 * 
	 * @param synchronizeDB
	 *            <code>true</code> if the new row should be propagated to the
	 *            underling database as well; otherwise <code>false</code>.
	 * @throws VirtualTableException
	 *             if the new row can't be set to {@link VirtualTable}.
	 * @throws DBException
	 *             if the new row can't be propagated to underling database.
	 */
	public void insert(final boolean synchronizeDB) throws VirtualTableException, DBException;
	
	
	/**
	 * <p>
	 * Alias for <code>delete(true).</code>
	 * </p>
	 * 
	 * @throws VirtualTableException
	 *             if row can't be deleted from {@link VirtualTable}.
	 * @throws DBException
	 *             if the row can't be deleted from the underling database.
	 * 
	 * @see #delete(boolean)
	 */
	public void delete() throws VirtualTableException, DBException;
	
	
	/**
	 * <p>
	 * Deletes the {@link VirtualTableRow} represented by this {@link Formular}
	 * from the set {@link VirtualTable}.
	 * </p>
	 * 
	 * <p>
	 * <strong>Warning:</strong> In order to be able to delete a row, you need
	 * to set a {@link VirtualTableRow} for this {@link Formular}. You can do
	 * this by calling {@link #setModel(VirtualTable.VirtualTableRow)} or
	 * {@link #setModel(int, VirtualTable)}.
	 * </p>
	 * 
	 * @param synchronizeDB
	 *            <code>true</code> if the row should also be deleted from the
	 *            underling database as well; otherwise <code>false</code>.
	 * @throws VirtualTableException
	 *             if row can't be deleted from {@link VirtualTable}.
	 * @throws DBException
	 *             if the row can't be deleted from the underling database.
	 * 
	 */
	public void delete(boolean synchronizeDB) throws VirtualTableException, DBException;
	
	
	/**
	 * Returns <code>ture</code> if all values of all {@link FormularComponent}s
	 * of this {@link Formular} could be verified, <code>false</code> otherwise.
	 * <p>
	 * This method shows a message box, if a validation fails.
	 * </p>
	 * This is a alternative method for {@link #validateFormularComponents()},
	 * but this method returns a boolean depending on the validation's result,
	 * not the result itself.
	 * 
	 * @return <code>true</code> if all values of all {@link FormularComponent}s
	 *         of this {@link Formular} could be verified, <code>false</code>
	 *         otherwise.
	 */
	public boolean verifyFormularComponents();
	
	
	/**
	 * Validates all {@link FormularComponent}s of this formular.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * if(formular.validateFormularComponents().hasError())
	 * {
	 * 	// show message
	 * }
	 * else
	 * {
	 * 	formular.save();
	 * }
	 * </pre>
	 * 
	 * @return the validation object containing all occured exceptions
	 * 
	 * @see Validation#hasError()
	 */
	public Validation validateFormularComponents();
	
	
	/**
	 * Validates all {@link FormularComponent}s of this formular depending on an
	 * given validation object.
	 * 
	 * @param validation
	 * @return the given validation object
	 * 
	 * @see Validation#hasError()
	 */
	public Validation validateFormularComponents(final Validation validation);
	
	
	/**
	 * Submits the contents of this {@link Formular} to the given
	 * <code>url</code>.
	 * 
	 * @param url
	 *            url to send the contents to
	 * @param target
	 *            HTML target (<code>_blank</code>, <code>_parent</code> etc.)
	 * @throws IOException
	 *             if the submit fails
	 */
	public void submit(String url, String target) throws IOException;
	
	
	/**
	 * Returns a parameter list consisting of key value pairs containing all
	 * components of this {@link Formular} and their values.
	 * 
	 * e.g. ?txtFirstname=John&txtLastname=Doe
	 * 
	 * @return a parameter list consisting of key value pairs containing all
	 *         components of this {@link Formular} and their values.
	 */
	public String getURLAdd();
	
	
	/**
	 * Creates a query (SELECT * FROM ..) of the connected {@link VirtualTable}
	 * with a {@link WHERE} condition created by the values of the
	 * {@link FormularComponent}s of this formular.
	 * 
	 * @param connector
	 *            "AND" or "OR"
	 * @return The default query info of the connected {@link VirtualTable} with
	 *         a {@link WHERE} condition created by the values of the
	 *         {@link FormularComponent}s of this formular.
	 * @throws IllegalStateException
	 *             if no data binding is available
	 * @throws IllegalArgumentException
	 *             if connector != "AND" resp. "OR"
	 * @see #createCondition(String, Collection)
	 * @see #search(String, VirtualTableOwner)
	 */
	public QueryInfo createQuery(String connector) throws IllegalStateException,
			IllegalArgumentException;
	
	
	/**
	 * <p>
	 * Alias for <code>createCondition(connector,null)</code>.
	 * </p>
	 * 
	 * @param connector
	 *            "AND" or "OR"
	 * 
	 * @return The {@link WHERE} condition depending on the input component's
	 *         values, or <code>null</code> if no values are available
	 * 
	 * @throws IllegalArgumentException
	 *             if connector != "AND" resp. "OR"
	 * 
	 * @see #createCondition(String, Query)
	 * @see #createCondition(String, Collection)
	 * @see #createQuery(String)
	 */
	public Condition createCondition(String connector) throws IllegalArgumentException;
	
	
	/**
	 * Creates a {@link Condition} depending on the input component's values and
	 * their conditional settings.
	 * 
	 * @param connector
	 *            "AND" or "OR"
	 * @param query
	 *            The {@link Query} to create this {@link WHERE} condition for
	 *            (optional)
	 * @return The {@link WHERE} condition depending on the input component's
	 *         values, or <code>null</code> if no values are available
	 * @throws IllegalArgumentException
	 *             if connector != "AND" resp. "OR"
	 * @see #createCondition(String, Collection)
	 * @see #createQuery(String)
	 */
	public Condition createCondition(String connector, final Query query)
			throws IllegalArgumentException;
	
	
	/**
	 * Creates a {@link Condition} depending on the input component's values and
	 * their conditional settings.
	 * 
	 * @param connector
	 *            "AND" or "OR"
	 * @param paramCollection
	 *            The {@link Collection} to add the {@link WHERE} condition's
	 *            parameter to (optional)
	 * @return The {@link WHERE} condition depending on the input component's
	 *         values, or <code>null</code> if no values are available
	 * @throws IllegalArgumentException
	 *             if connector != "AND" resp. "OR"
	 * @see #createCondition(String, Query)
	 * @see #createQuery(String)
	 */
	public Condition createCondition(String connector, final Collection paramCollection)
			throws IllegalArgumentException;
	
	
	/**
	 * Updates the model of the {@link VirtualTableOwner} target.
	 * <p>
	 * First a {@link Condition} depending on the input component's values and
	 * their conditional settings is created and then handed over to
	 * {@link VirtualTableOwner#updateModel(Condition, Object...)} of
	 * <code>target</code>.
	 * 
	 * @param connector
	 *            "AND" or "OR"
	 * @param target
	 *            the component to update
	 * @throws IllegalArgumentException
	 *             if connector != "AND" resp. "OR"
	 * 
	 * @see #createCondition(String)
	 * @see #createCondition(String, Collection)
	 * @see #createCondition(String, Query)
	 * @see #createQuery(String)
	 */
	public void search(String connector, VirtualTableOwner target) throws IllegalArgumentException;
	
	
	/**
	 * Adds an arbitrary key/value "client property" to this component.
	 * <p>
	 * The <code>get/putClientProperty</code> methods provide access to a small
	 * per-instance hashtable. Callers can use get/putClientProperty to annotate
	 * components that were created by another module. For example, a layout
	 * manager might store per child constraints this way. For example:
	 * 
	 * <pre>
	 * componentA.putClientProperty(&quot;to the left of&quot;,componentB);
	 * </pre>
	 * 
	 * If value is <code>null</code> this method will remove the property.
	 * Changes to client properties are reported with
	 * <code>PropertyChange</code> events. The name of the property (for the
	 * sake of PropertyChange events) is <code>key.toString()</code>.
	 * <p>
	 * The <code>clientProperty</code> dictionary is not intended to support
	 * large scale extensions to form nor should be it considered an alternative
	 * to subclassing when designing a new component.
	 * 
	 * @param key
	 *            the new client property key
	 * @param value
	 *            the new client property value; if <code>null</code> this
	 *            method will remove the property
	 * @see #getClientProperty
	 */
	public void putClientProperty(Object key, Object value);
	
	
	/**
	 * Returns the value of the property with the specified key. Only properties
	 * added with <code>putClientProperty</code> will return a non-
	 * <code>null</code> value.
	 * 
	 * @param key
	 *            the being queried
	 * @return the value of this property or <code>null</code>
	 * @see #putClientProperty
	 */
	public Object getClientProperty(Object key);
	
}
