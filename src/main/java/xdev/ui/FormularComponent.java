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


import java.util.EventListener;
import java.util.Map;

import javax.swing.JComponent;

import xdev.db.Operator;
import xdev.vt.VirtualTable;


/**
 * Enables a {@link JComponent} to be used as a {@link FormularComponent}. A
 * {@link FormularComponent} provides methods that allows the {@link Formular} to
 * set and read values from the component automatically.
 * 
 * @param <C>
 *            type of the implementing {@link JComponent}
 * 
 * @see BooleanFormularComponent
 * @see NumberFormularComponent
 * @see DateFormularComponent
 * @see ImageFormularComponent
 * @see MasterDetailComponent
 */
public interface FormularComponent<C extends JComponent>
{
	/**
	 * Returns the name of the component in the formular context.
	 * 
	 * @return the name of the component in the formular context.
	 */
	public String getFormularName();
	
	/**
	 * Separator which is used if a data field contains more entries, e.g. a
	 * multi column foreign key
	 * 
	 * @since 3.2
	 */
	public final static String	DATA_FIELD_SEPARATOR	= ",";
	
	
	/**
	 * Sets the name of the bound data field of this formular component.
	 * 
	 * @param dataField
	 *            the new bound data field
	 * @since 3.1
	 */
	public void setDataField(String dataField);
	
	
	/**
	 * Returns the name of the bound data field of this formular component.
	 * 
	 * @return the name of the bound data field
	 * @since 3.1
	 */
	public String getDataField();
	
	
	/**
	 * Sets the value of the component according to the provided value
	 * <code>value</code>. The value is formatted using the column format of the
	 * provided {@link VirtualTable} and <code>columnIndex</code>.
	 * 
	 * @param vt
	 *            {@link VirtualTable} to use the format from
	 * @param columnIndex
	 *            of the column to use the format from
	 * @param value
	 *            value to set
	 * @deprecated replaced by {@link #setFormularValue(VirtualTable, Map)

	 */
	@Deprecated
	public void setFormularValue(VirtualTable vt, int columnIndex, Object value);
	
	
	/**
	 * Sets the value of the component, taking the value(s) of the record
	 * according to {@link #getDataField()}.
	 * 
	 * @param vt
	 *            the underlying virtual table
	 * @param record
	 *            the data &lt;column,value&gt;
	 * @since 3.2
	 */
	public void setFormularValue(VirtualTable vt, Map<String, Object> record);
	
	
	/**
	 * Returns the value of the component.
	 * 
	 * @return the value of the component.
	 */
	public Object getFormularValue();
	
	
	/**
	 * Saves the state of the component internally.
	 * <p>
	 * A saved state can be restored using {@link #restoreState()}.
	 * </p>
	 */
	public void saveState();
	
	
	/**
	 * Restores the internally saved state of the component.
	 * <p>
	 * The state of the component can be saved using {@link #saveState()}.
	 * </p>
	 */
	public void restoreState();
	
	
	/**
	 * Checks if the component's state since the last call of
	 * {@link #saveState()} has changed.
	 * 
	 * @return <code>true</code> if the component's state has changed,
	 *         <code>false</code> otherwise
	 * 
	 * @since 3.1
	 */
	public boolean hasStateChanged();
	
	
	/**
	 * Registers a {@link ValueChangeListener}.
	 * 
	 * @param l
	 *            the listener to register
	 */
	public void addValueChangeListener(ValueChangeListener l);
	
	
	
	/**
	 * Listener interface which is informed when the value changes.
	 * 
	 * @see FormularComponent#addValueChangeListener(ValueChangeListener)
	 */
	public static interface ValueChangeListener extends EventListener
	{
		/**
		 * Invoked when the value of a {@link FormularComponent} has changed.
		 */
		public void valueChanged(Object eventObject);
	}
	
	
	/**
	 * Returns whether the component supports <i>multi selection</i> or not.
	 * 
	 * <p>
	 * A component that supports <i>multi selection</i> can have more than one
	 * selected item / value.
	 * </p>
	 * 
	 * @return <code>true</code> if the component supports <i>multi
	 *         selection</i>, <code>false</code> otherwise.
	 */
	public boolean isMultiSelect();
	
	
	/**
	 * Returns whether the component's value adheres all set constraints.
	 * <p>
	 * This is a alternative method for {@link #validateState()}, but this
	 * method returns a boolean depending on the validation's result and doesn't
	 * throw an {@link ValidationException}.
	 * 
	 * @return <code>true</code> if the value of the component adheres all set
	 *         constraints; otherwise <code>false</code>.
	 */
	public boolean verify();
	
	
	/**
	 * Adds a validator to this component
	 * 
	 * @param validator
	 *            the validator to add
	 * @see #validateState()
	 * @since 3.1
	 */
	public void addValidator(Validator validator);
	
	
	/**
	 * Removes a validator from this component
	 * 
	 * @param validator
	 *            the validator to remove
	 * @see #validateState()
	 * @since 3.1
	 */
	public void removeValidator(Validator validator);
	
	
	/**
	 * Returns all validators of this component.
	 * <p>
	 * If no validator is present an empty array is returned.
	 * 
	 * @return all validators of this component
	 * @since 3.1
	 */
	public Validator[] getValidators();
	
	
	/**
	 * Calls {@link Validator#validate(Object)} of all registered
	 * {@link Validator}s.
	 * 
	 * @throws ValidationException
	 * @see #verify
	 * @since 3.1
	 */
	public void validateState() throws ValidationException;
	
	
	/**
	 * Calls {@link Validator#validate(Object)} of all registered
	 * {@link Validator}s.
	 * <p>
	 * Every {@link ValidationException} is recorded in the
	 * <code>validation</code> object, and if
	 * {@link Validation#continueValidation(ValidationException)} returns
	 * <code>false</code> this exception is re-thrown by this method.
	 * 
	 * @param validation
	 *            the validation process object
	 * @throws ValidationException
	 * @see #verify
	 * @since 3.1
	 */
	public void validateState(Validation validation) throws ValidationException;
	
	
	/**
	 * Returns the operator used in {@link Formular#createCondition(String)}
	 * 
	 * @return the component's filter operator
	 * @since 3.1
	 */
	public Operator getFilterOperator();
	
	
	/**
	 * Sets the filter operator used in {@link Formular#createCondition(String)}
	 * 
	 * @param filterOperator
	 *            the new filter operator
	 * @since 3.1
	 */
	public void setFilterOperator(Operator filterOperator);
	
	
	/**
	 * Determines whether this component is only used to display values.
	 * 
	 * @return <code>true</code> if this component is only used to display
	 *         values.
	 * @since 3.2
	 */
	public boolean isReadOnly();
	
	
	/**
	 * Sets if this form component is only used to display values.
	 * 
	 * @param readOnly
	 *            <code>true</code> to only display values
	 * @since 3.2
	 */
	public void setReadOnly(boolean readOnly);
	
	
	/**
	 * Determines whether this component is visible or not.
	 * 
	 * @return <code>true</code> if the component is visible, <code>false</code>
	 *         otherwise
	 */
	public boolean isVisible();
	
	
	/**
	 * Determines whether this component is enabled or not.
	 * 
	 * @return <code>true</code> if the component is enabled, <code>false</code>
	 *         otherwise
	 */
	public boolean isEnabled();
	
	
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
