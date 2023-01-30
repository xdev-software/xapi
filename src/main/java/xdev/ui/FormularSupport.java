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


import java.awt.Component;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;

import xdev.Application;
import xdev.db.DBConnection;
import xdev.db.DBException;
import xdev.db.Operator;
import xdev.db.QueryInfo;
import xdev.db.Transaction;
import xdev.db.sql.Column;
import xdev.db.sql.Condition;
import xdev.db.sql.SELECT;
import xdev.lang.cmd.Query;
import xdev.net.NetUtils;
import xdev.ui.Formular.WorkingState;
import xdev.ui.UIUtils.MessageDialogType;
import xdev.ui.event.FormularEvent;
import xdev.ui.event.FormularListener;
import xdev.ui.table.ExtendedTable;
import xdev.util.CollectionUtils;
import xdev.util.StringUtils;
import xdev.vt.KeyValues;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;
import xdev.vt.VirtualTables;


/**
 * 
 * @author XDEV Software
 * 
 * @since 4.0
 * 
 */
public class FormularSupport
{
	private final Formular		form;
	private VirtualTableRow		virtualTableRow	= null;
	private WorkingState		workingState	= WorkingState.IDLE;
	private Map<String, Object>	hiddenFields;
	
	
	public FormularSupport(Formular form)
	{
		this.form = form;
	}
	
	
	public Formular getFormular()
	{
		return form;
	}
	
	
	public WorkingState getWorkingState()
	{
		return workingState;
	}
	
	
	public void fireModelChanged()
	{
		FormularListener[] listeners = form.getFormularListeners();
		if(listeners != null && listeners.length > 0)
		{
			FormularEvent event = new FormularEvent(form);
			for(FormularListener listener : listeners)
			{
				listener.formularModelChanged(event);
			}
		}
	}
	
	
	public void fireFormularComponentValueChanged(FormularComponent formularComponent,
			Object formularComponentEventObject)
	{
		FormularListener[] listeners = form.getFormularListeners();
		if(listeners != null && listeners.length > 0)
		{
			FormularEvent event = new FormularEvent(form,formularComponent,
					formularComponentEventObject,workingState);
			for(FormularListener listener : listeners)
			{
				listener.formularComponentValueChanged(event);
			}
		}
	}
	
	
	public void fireSavePerformed()
	{
		FormularListener[] listeners = form.getFormularListeners();
		if(listeners != null && listeners.length > 0)
		{
			FormularEvent event = new FormularEvent(form);
			for(FormularListener listener : listeners)
			{
				listener.formularSavePerformed(event);
			}
		}
		
		for(ManyToManyComponent component : form.manyToManyComponents())
		{
			component.refresh(virtualTableRow);
		}
	}
	
	
	public void saveState()
	{
		for(ManyToManyComponent component : form.manyToManyComponents())
		{
			component.saveState();
		}
		for(FormularComponent component : form.formComponents())
		{
			component.saveState();
		}
	}
	
	
	public void restoreState()
	{
		workingState = WorkingState.RESTORING;
		
		try
		{
			for(ManyToManyComponent component : form.manyToManyComponents())
			{
				component.restoreState();
			}
			for(FormularComponent component : form.formComponents())
			{
				component.restoreState();
			}
		}
		finally
		{
			workingState = WorkingState.IDLE;
		}
	}
	
	
	public boolean hasStateChanged()
	{
		for(ManyToManyComponent manyToManyComponent : form.manyToManyComponents())
		{
			if(manyToManyComponent.hasStateChanged())
			{
				return true;
			}
		}
		
		for(FormularComponent<JComponent> formularComponent : form.formComponents())
		{
			if(formularComponent.hasStateChanged())
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	public void putHiddenField(String name, Object value)
	{
		if(name == null || name.length() == 0)
		{
			throw new IllegalArgumentException("name cannot be null or empty");
		}
		
		if(value != null)
		{
			if(hiddenFields == null)
			{
				hiddenFields = new LinkedHashMap();
			}
			hiddenFields.put(name,value);
		}
		else if(hiddenFields != null)
		{
			hiddenFields.remove(name);
		}
	}
	
	
	public Object getHiddenField(String name)
	{
		if(hiddenFields != null)
		{
			return hiddenFields.get(name);
		}
		return null;
	}
	
	
	public final Iterable<String> getHiddenFieldNames()
	{
		if(hiddenFields != null)
		{
			return hiddenFields.keySet();
		}
		
		return Collections.EMPTY_LIST;
	}
	
	
	public void setModel(VirtualTable vt)
	{
		reset(vt);
	}
	
	
	public void reset(VirtualTable vt)
	{
		setModel(vt.createRow());
	}
	
	
	public void setModel(int row, VirtualTable vt)
	{
		setModel(vt.getRow(row));
	}
	
	
	public void setModel(final VirtualTableRow virtualTableRow)
	{
		this.virtualTableRow = virtualTableRow;
		
		workingState = WorkingState.ADJUSTING_MODEL;
		
		try
		{
			final Map<String, Object> map = virtualTableRow.toMap();
			
			for(ManyToManyComponent component : form.manyToManyComponents())
			{
				component.refresh(virtualTableRow);
			}
			for(FormularComponent component : form.formComponents())
			{
				String dataField = component.getDataField();
				if(dataField != null && dataField.length() > 0)
				{
					component.setFormularValue(virtualTableRow.getVirtualTable(),map);
				}
			}
		}
		finally
		{
			if(form.getSaveStateAfterModelUpdate())
			{
				saveState();
			}
			
			workingState = WorkingState.IDLE;
			
			fireModelChanged();
		}
	}
	
	
	public VirtualTableRow getVirtualTableRow()
	{
		return virtualTableRow;
	}
	
	
	private void checkVirtualTableRow() throws VirtualTableException
	{
		if(virtualTableRow == null)
		{
			throw new VirtualTableException("No data from a VirtualTable assigned");
		}
	}
	
	
	public VirtualTable getVirtualTable()
	{
		if(virtualTableRow != null)
		{
			return virtualTableRow.getVirtualTable();
		}
		
		return lookupVT();
	}
	
	
	protected VirtualTable lookupVT()
	{
		for(FormularComponent c : form.formComponents())
		{
			VirtualTable vt = getConnectedVT(c);
			if(vt != null)
			{
				return vt;
			}
		}
		
		return null;
	}
	
	
	public static VirtualTable getConnectedVT(FormularComponent component)
	{
		String dataField = component.getDataField();
		if(dataField != null && dataField.length() > 0)
		{
			int si = dataField.lastIndexOf('.');
			if(si > 0)
			{
				dataField = dataField.substring(0,si);
			}
			
			return VirtualTables.getVirtualTable(dataField);
		}
		
		return null;
	}
	
	
	public static VirtualTableColumn getVirtualTableColumn(String dataField)
	{
		if(dataField != null && dataField.length() > 0)
		{
			int si = dataField.lastIndexOf('.');
			if(si > 0)
			{
				String table = dataField.substring(0,si);
				VirtualTable vt = VirtualTables.getVirtualTable(table);
				if(vt != null)
				{
					String column = dataField.substring(si + 1);
					return vt.getColumn(column);
				}
			}
		}
		
		return null;
	}
	
	
	public void save(final boolean synchronizeDB) throws VirtualTableException, DBException
	{
		if(virtualTableRow != null)
		{
			if(virtualTableRow.isNew())
			{
				insert(synchronizeDB);
			}
			else
			{
				update(synchronizeDB);
			}
		}
		else
		{
			VirtualTable vt = lookupVT();
			if(vt == null)
			{
				throw new NullPointerException("No VirtualTable found for Form");
			}
			
			virtualTableRow = vt.addRow(getData(true),synchronizeDB);
			
			fireSavePerformed();
		}
	}
	
	
	public void insert(final boolean synchronizeDB) throws VirtualTableException, DBException
	{
		final VirtualTableRow virtualTableRow = this.virtualTableRow;
		if(virtualTableRow != null)
		{
			final Map<String, Object> data = getData(true);
			final List<ManyToManyComponent> nmCpns = CollectionUtils.asList(form
					.manyToManyComponents().iterator());
			
			if(nmCpns.size() > 0)
			{
				if(synchronizeDB)
				{
					final DBConnection connection = virtualTableRow.getVirtualTable()
							.getDataSource().openConnection();
					
					try
					{
						new Transaction()
						{
							@Override
							protected DBConnection<?> getConnection() throws DBException
							{
								return connection;
							}
							
							
							@Override
							protected void write(DBConnection<?> connection) throws DBException
							{
								// insert master record first to get the
								// generated ids
								virtualTableRow.insert(data,synchronizeDB,connection);
								
								for(ManyToManyComponent nmCpn : nmCpns)
								{
									nmCpn.save(synchronizeDB,connection);
								}
							}
						}.execute();
					}
					finally
					{
						connection.close();
					}
				}
				else
				{
					virtualTableRow.insert(data,synchronizeDB);
					for(ManyToManyComponent nmCpn : nmCpns)
					{
						nmCpn.save(synchronizeDB,null);
					}
				}
			}
			else
			{
				virtualTableRow.insert(data,synchronizeDB);
			}
		}
		else
		{
			VirtualTable vt = lookupVT();
			if(vt == null)
			{
				throw new NullPointerException("No VirtualTable found for Form");
			}
			
			vt.addRow(getData(true),synchronizeDB);
		}
		
		fireSavePerformed();
	}
	
	
	public void insertRowInVT(VirtualTable vt, boolean synchronizeDB) throws VirtualTableException,
			DBException
	{
		vt.addRow(getData(true),synchronizeDB);
	}
	
	
	public void update(final boolean synchronizeDB) throws VirtualTableException, DBException
	{
		checkVirtualTableRow();
		
		final VirtualTableRow virtualTableRow = this.virtualTableRow;
		final VirtualTable vt = virtualTableRow.getVirtualTable();
		final Map<String, Object> data = getData(true);
		final List<ManyToManyComponent> nmCpns = CollectionUtils.asList(form.manyToManyComponents()
				.iterator());
		
		if(nmCpns.size() > 0)
		{
			if(synchronizeDB)
			{
				new Transaction(vt.getDataSource())
				{
					@Override
					protected void write(DBConnection<?> connection) throws DBException
					{
						virtualTableRow.update(data,synchronizeDB,connection);
						for(ManyToManyComponent nmCpn : nmCpns)
						{
							nmCpn.save(synchronizeDB,connection);
						}
					}
				}.execute();
			}
			else
			{
				virtualTableRow.update(data,synchronizeDB);
				for(ManyToManyComponent nmCpn : nmCpns)
				{
					nmCpn.save(synchronizeDB,null);
				}
			}
		}
		else
		{
			virtualTableRow.update(data,synchronizeDB);
		}
		
		if(synchronizeDB && vt.hasLinkedColumns())
		{
			virtualTableRow.reload();
		}
		
		fireSavePerformed();
	}
	
	
	public void updateRowsInVT(VirtualTable vt, KeyValues keyValues, boolean synchronizeDB)
			throws VirtualTableException, DBException
	{
		vt.updateRows(getData(true),keyValues,synchronizeDB);
	}
	
	
	public void delete(boolean synchronizeDB) throws VirtualTableException, DBException
	{
		checkVirtualTableRow();
		
		virtualTableRow.delete(synchronizeDB);
	}
	
	
	public Map<String, Object> getData(boolean withNulls)
	{
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		if(hiddenFields != null)
		{
			map.putAll(hiddenFields);
		}
		
		for(FormularComponent component : form.formComponents())
		{
			if(component.isReadOnly())
			{
				continue;
			}
			
			String dataField = component.getDataField();
			if(dataField != null && dataField.length() > 0)
			{
				Object value = component.getFormularValue();
				
				boolean isCompoundValue = false;
				
				if(value != null && value.getClass().isArray()
						&& dataField.indexOf(FormularComponent.DATA_FIELD_SEPARATOR) != -1)
				{
					List<String> names = StringUtils.explode(dataField,
							FormularComponent.DATA_FIELD_SEPARATOR);
					int c = Array.getLength(value);
					if(c == names.size())
					{
						isCompoundValue = true;
						
						for(int i = 0; i < c; i++)
						{
							Object element = Array.get(value,i);
							if((withNulls || !isNull(element)))
							{
								map.put(names.get(i),element);
							}
						}
					}
				}
				
				if(!isCompoundValue)
				{
					if((withNulls || !isNull(value)))
					{
						map.put(dataField,value);
					}
				}
			}
		}
		
		return map;
	}
	
	
	public boolean isNull(Object value)
	{
		if(value == null)
		{
			return true;
		}
		
		if(value instanceof String && ((String)value).length() == 0)
		{
			return true;
		}
		
		return false;
	}
	
	
	public boolean verifyFormularComponents()
	{
		Validation validation = new Validation();
		validateFormularComponents(validation);
		if(validation.hasError())
		{
			ValidationException ve = validation.getExceptionsOf(Severity.ERROR)[0];
			UIUtils.showMessage(ve.getTitle(),ve.getMessage(),MessageDialogType.ERROR_MESSAGE);
			
			return false;
		}
		
		return true;
	}
	
	
	public Validation validateFormularComponents()
	{
		return validateFormularComponents(new Validation());
	}
	
	
	public Validation validateFormularComponents(final Validation validation)
	{
		for(FormularComponent component : form.formComponents())
		{
			try
			{
				component.validateState(validation);
			}
			catch(ValidationException e)
			{
				// cancel
				break;
			}
		}
		
		return validation;
	}
	
	
	public void submit(String url, String target) throws IOException
	{
		Application.getContainer().showDocument(new URL(url + getURLAdd()),target);
	}
	
	
	public String getURLAdd()
	{
		StringBuffer urlAdd = new StringBuffer();
		Vector<XdevRadioButton> usedRadioButtons = new Vector<XdevRadioButton>();
		
		for(FormularComponent component : form.formComponents())
		{
			if(component instanceof XdevRadioButton && usedRadioButtons.contains(component))
			{
				continue;
			}
			
			Object value = component.getFormularValue();
			String dataField = component.getDataField();
			if(value != null && dataField != null && dataField.length() > 0)
			{
				urlAdd.append(urlAdd.length() == 0 ? '?' : '&');
				urlAdd.append(dataField);
				
				if(component instanceof XdevRadioButton)
				{
					XdevRadioButton radio = (XdevRadioButton)component;
					ButtonGroup buttonGroup = radio.getButtonGroup();
					if(buttonGroup != null)
					{
						Enumeration<AbstractButton> e = buttonGroup.getElements();
						while(e.hasMoreElements())
						{
							Object o = e.nextElement();
							if(o instanceof XdevRadioButton)
							{
								radio = (XdevRadioButton)o;
								if(radio.isSelected())
								{
									value = radio.getFormularValue();
								}
								usedRadioButtons.add(radio);
							}
						}
					}
				}
				
				urlAdd.append('=');
				urlAdd.append(NetUtils.encodeURLString(value.toString()));
			}
		}
		
		return urlAdd.toString();
	}
	
	
	public QueryInfo createQuery(String connector) throws IllegalStateException,
			IllegalArgumentException
	{
		VirtualTable vt = getVirtualTable();
		if(vt == null)
		{
			throw new IllegalStateException("No data binding available");
		}
		
		SELECT select = vt.getSelect();
		Collection paramCollection = new ArrayList();
		Condition where = createCondition(connector,paramCollection);
		if(where != null)
		{
			select.WHERE(where);
		}
		return new QueryInfo(select,paramCollection.toArray());
	}
	
	
	public Condition createCondition(String connector, final Query query)
			throws IllegalArgumentException
	{
		Map<String, List<FormularValue>> map = new LinkedHashMap<String, List<FormularValue>>();
		collectFormularValues(map,false);
		
		ParameterContainer paramContainer = new ParameterContainer()
		{
			@Override
			public void addParameters(Object... params)
			{
				if(query != null)
				{
					query.addParameters(params);
				}
			}
		};
		
		return createWhereCondition(connector,paramContainer,map);
	}
	
	
	public Condition createCondition(String connector, final Collection paramCollection)
			throws IllegalArgumentException
	{
		Map<String, List<FormularValue>> map = new LinkedHashMap<String, List<FormularValue>>();
		collectFormularValues(map,false);
		
		ParameterContainer paramContainer = new ParameterContainer()
		{
			@Override
			public void addParameters(Object... params)
			{
				if(paramCollection != null)
				{
					CollectionUtils.addAll(paramCollection,params);
				}
			}
		};
		
		return createWhereCondition(connector,paramContainer,map);
	}
	
	
	public void search(String connector, VirtualTableOwner target) throws IllegalArgumentException
	{
		Map<String, List<FormularValue>> map = new LinkedHashMap<String, List<FormularValue>>();
		collectFormularValues(map,false);
		
		final List paramList = new ArrayList();
		ParameterContainer paramContainer = new ParameterContainer()
		{
			@Override
			public void addParameters(Object... params)
			{
				Collections.addAll(paramList,params);
			}
		};
		
		VirtualTable targetVT = target.getVirtualTable();
		VirtualTable formVT = getVirtualTable();
		if(formVT != null && (targetVT == null || !targetVT.equals(formVT)))
		{
			target.setVirtualTable(formVT);
		}
		
		Condition condition = createWhereCondition(connector,paramContainer,map);
		target.updateModel(condition,paramList.toArray());
	}
	
	
	/**
	 * @param target
	 * @param vt
	 * @since 4.0
	 */
	public static void initModel(VirtualTableOwner target, VirtualTable vt)
	{
		if(target instanceof ExtendedTable)
		{
			((ExtendedTable)target).setModel(vt);
		}
		else if(target instanceof ExtendedList)
		{
			
		}
	}
	
	
	protected void collectFormularValues(final Map<String, List<FormularValue>> formularValues,
			final boolean withNulls)
	{
		for(FormularComponent component : form.formComponents())
		{
			if(component.isReadOnly())
			{
				continue;
			}
			
			String dataField = component.getDataField();
			if(dataField != null && dataField.length() > 0)
			{
				Object value = component.getFormularValue();
				
				boolean isCompoundValue = false;
				
				if(value != null && value.getClass().isArray()
						&& dataField.indexOf(FormularComponent.DATA_FIELD_SEPARATOR) != -1)
				{
					List<String> names = StringUtils.explode(dataField,
							FormularComponent.DATA_FIELD_SEPARATOR);
					int c = Array.getLength(value);
					if(c == names.size())
					{
						isCompoundValue = true;
						
						for(int i = 0; i < c; i++)
						{
							Object element = Array.get(value,i);
							if((withNulls || !isNull(element)))
							{
								CollectionUtils.accumulate(formularValues,names.get(i),
										new FormularValue(component,element));
							}
						}
					}
				}
				
				if(!isCompoundValue)
				{
					if((withNulls || !isNull(value)))
					{
						CollectionUtils.accumulate(formularValues,dataField,new FormularValue(
								component,value));
					}
				}
			}
		}
	}
	
	
	
	protected static interface ParameterContainer
	{
		void addParameters(Object... params);
	}
	
	
	
	protected static class FormularValue
	{
		final FormularComponent	component;
		final Object			value;
		
		
		FormularValue(FormularComponent component, Object value)
		{
			this.component = component;
			this.value = value;
		}
	}
	
	
	protected Condition createWhereCondition(String connector, ParameterContainer paramContainer,
			Map<String, List<FormularValue>> formularValues)
	{
		if(!("AND".equalsIgnoreCase(connector) || "OR".equalsIgnoreCase(connector)))
		{
			throw new IllegalArgumentException("illegal connector: " + connector);
		}
		
		Condition where = null;
		
		for(String key : formularValues.keySet())
		{
			for(FormularValue formularValue : formularValues.get(key))
			{
				Column column = getVirtualTableColumn(key);
				if(column == null)
				{
					column = new Column(key);
				}
				
				Condition condition = null;
				Operator operator = formularValue.component.getFilterOperator();
				
				boolean useDefault = true;
				if(formularValue.component.isMultiSelect()
						&& (operator == Operator.IN || operator == Operator.NOT_IN))
				{
					useDefault = false;
					if(formularValue.value != null && formularValue.value.getClass().isArray())
					{
						Object[] values = (Object[])formularValue.value;
						paramContainer.addParameters(values);
						
						Object[] params = new String[values.length];
						for(int i = 0; i < params.length; i++)
						{
							params[i] = "?";
						}
						
						if(operator == Operator.IN)
						{
							condition = column.IN(params);
						}
						else
						{
							condition = column.NOT_IN(params);
						}
					}
				}
				
				if(useDefault)
				{
					Object value = formularValue.value;
					
					switch(operator)
					{
						case NOT_LIKE_$:
						case LIKE_$:
						{
							value = value + "%";
						}
						break;
						
						case NOT_LIKE$_:
						case LIKE$_:
						{
							value = "%" + value;
						}
						break;
						
						case NOT_LIKE$_$:
						case LIKE$_$:
						{
							value = "%" + value + "%";
						}
						break;
					}
					
					if(paramContainer != null)
					{
						paramContainer.addParameters(value);
						value = "?";
					}
					
					switch(operator)
					{
						case EQUAL:
						{
							condition = column.eq(value);
						}
						break;
						
						case NOT_EQUAL:
						{
							condition = column.ne(value);
						}
						break;
						
						case SMALLER:
						{
							condition = column.lt(value);
						}
						break;
						
						case SMALLER_EQUAL:
						{
							condition = column.lte(value);
						}
						break;
						
						case GREATER:
						{
							condition = column.gt(value);
						}
						break;
						
						case GREATER_EQUAL:
						{
							condition = column.gte(value);
						}
						break;
						
						case LIKE:
						case LIKE_$:
						case LIKE$_:
						case LIKE$_$:
						{
							condition = column.LIKE(value);
						}
						break;
						
						case NOT_LIKE:
						case NOT_LIKE_$:
						case NOT_LIKE$_:
						case NOT_LIKE$_$:
						{
							condition = column.LIKE(value);
						}
						break;
						
						case IN:
						{
							condition = column.IN(value);
						}
						break;
						
						case NOT_IN:
						{
							condition = column.NOT_IN(value);
						}
						break;
					}
				}
				
				if(condition != null)
				{
					if(where == null)
					{
						where = condition;
					}
					else if("AND".equalsIgnoreCase(connector))
					{
						where = where.AND(condition);
					}
					else if("OR".equalsIgnoreCase(connector))
					{
						where = where.OR(condition);
					}
				}
			}
		}
		
		return where;
	}
	
	
	public static Formular getFormularOf(Object component)
	{
		if(component instanceof FormularComponent)
		{
			Formular form = (Formular)((FormularComponent)component)
					.getClientProperty(Formular.CLIENT_PROPERTY_KEY);
			if(form != null)
			{
				return form;
			}
		}
		
		if(component instanceof Component)
		{
			return UIUtils.getParentOfClass(Formular.class,(Component)component);
		}
		
		return null;
	}
}
