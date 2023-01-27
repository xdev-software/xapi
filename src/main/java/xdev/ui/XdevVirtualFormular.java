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


import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import xdev.db.DBException;
import xdev.db.QueryInfo;
import xdev.db.sql.Condition;
import xdev.db.sql.SELECT;
import xdev.lang.cmd.Query;
import xdev.ui.FormularComponent.ValueChangeListener;
import xdev.ui.event.FormularListener;
import xdev.ui.paging.VirtualFormularPageControl;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.KeyValues;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableException;


/**
 * The {@link XdevVirtualFormular} is a collection containing
 * {@link FormularComponent}s and {@link ManyToManyComponent}s provided as a
 * alternative for the {@link XdevFormular} but without an UI representation.
 * 
 * <p>
 * The {@link XdevVirtualFormular} can be used to display a row of a
 * {@link VirtualTable}. Therefore the {@link XdevVirtualFormular} must contain
 * {@link FormularComponent}s that are mapped to the columns of the
 * {@link VirtualTable} you want to display. This can be done by the XDEV IDE or
 * manually.
 * </p>
 * 
 * <p>
 * The {@link XdevVirtualFormular} also manages n:m-relations. Therefore
 * {@link ManyToManyComponent}s can be added into this container.
 * </p>
 * 
 * <p>
 * The {@link XdevVirtualFormular} provides methods to:
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
 * @see FormularGroupListener
 * @see FormularComponent
 * @see MasterDetailComponent
 * @see ManyToManyComponent
 * @see XdevVirtualFormular
 * 
 * @since 4.0
 */
public class XdevVirtualFormular implements Formular, xdev.ui.paging.Pageable
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger								LOG							= LoggerFactory
																								.getLogger(XdevVirtualFormular.class);
	
	private final FormularSupport								support						= new FormularSupport(
																									this);
	
	private boolean												saveStateAfterModelUpdate	= true;
	
	private FormularComponent[]									formComponents				= new FormularComponent[0];
	private ManyToManyComponent[]								manyToManyComponents		= new ManyToManyComponent[0];
	
	protected final EventListenerList							listenerList				= new EventListenerList();
	protected final PropertyChangeSupport						propertChangeSupport		= new PropertyChangeSupport(
																									this);
	
	private final Map<FormularComponent, ValueChangeListener>	listenerMap					= new HashMap();
	
	private Map<Object, Object>									clientProperties;
	
	
	/**
	 * Creates a new {@link XdevVirtualFormular}.
	 */
	public XdevVirtualFormular()
	{
		super();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<FormularComponent> formComponents()
	{
		return Arrays.asList(formComponents);
	}
	
	
	/**
	 * 
	 * @return a list of FormularComponents
	 */
	public FormularComponent[] getFormComponents()
	{
		return formComponents;
	}
	
	
	/**
	 * Sets the {@link FormularComponent}s which should be used in this
	 * FormularGroup.
	 * 
	 * @param formularComponents
	 */
	@BeanProperty(category = DefaultBeanCategories.OBJECT)
	public void setFormComponents(FormularComponent[] formularComponents)
	{
		if(formularComponents == null)
		{
			formularComponents = new FormularComponent[0];
		}
		this.formComponents = formularComponents;
		
		for(final FormularComponent component : formularComponents)
		{
			component.putClientProperty(CLIENT_PROPERTY_KEY,XdevVirtualFormular.this);
			
			if(!listenerMap.containsKey(component))
			{
				ValueChangeListener valueChangeListener = new ValueChangeListener()
				{
					@Override
					public void valueChanged(Object eventObject)
					{
						fireFormularComponentValueChanged(component,eventObject);
					}
				};
				component.addValueChangeListener(valueChangeListener);
				listenerMap.put(component,valueChangeListener);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<ManyToManyComponent> manyToManyComponents()
	{
		return Arrays.asList(manyToManyComponents);
	}
	
	
	/**
	 * 
	 * @return a list of ManyToManyComponents
	 */
	public ManyToManyComponent[] getManyToManyComponents()
	{
		return manyToManyComponents;
	}
	
	
	/**
	 * Sets the {@link ManyToManyComponent}s which should be used in this
	 * FormularGroup
	 * 
	 * @param manyToManyComponents
	 */
	@BeanProperty(category = DefaultBeanCategories.OBJECT)
	public void setManyToManyComponents(ManyToManyComponent[] manyToManyComponents)
	{
		if(manyToManyComponents == null)
		{
			manyToManyComponents = new ManyToManyComponent[0];
		}
		
		this.manyToManyComponents = manyToManyComponents;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WorkingState getWorkingState()
	{
		return support.getWorkingState();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFormularListener(FormularListener l)
	{
		listenerList.add(FormularListener.class,l);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFormularListener(FormularListener l)
	{
		listenerList.remove(FormularListener.class,l);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FormularListener[] getFormularListeners()
	{
		return listenerList.getListeners(FormularListener.class);
	}
	
	
	protected void fireModelChanged()
	{
		support.fireModelChanged();
	}
	
	
	protected void fireFormularComponentValueChanged(FormularComponent formularComponent,
			Object formularComponentEventObject)
	{
		support.fireFormularComponentValueChanged(formularComponent,formularComponentEventObject);
	}
	
	
	protected void fireSavePerformed()
	{
		support.fireSavePerformed();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@BeanProperty(category = DefaultBeanCategories.DATA)
	public void setSaveStateAfterModelUpdate(boolean saveStateAfterModelUpdate)
	{
		if(this.saveStateAfterModelUpdate != saveStateAfterModelUpdate)
		{
			boolean oldValue = this.saveStateAfterModelUpdate;
			this.saveStateAfterModelUpdate = saveStateAfterModelUpdate;
			propertChangeSupport.firePropertyChange(SAVE_STATE_AFTER_MODEL_UPDATE_PROPERTY,
					oldValue,saveStateAfterModelUpdate);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getSaveStateAfterModelUpdate()
	{
		return saveStateAfterModelUpdate;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveState()
	{
		support.saveState();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState()
	{
		support.restoreState();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset()
	{
		restoreState();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasStateChanged()
	{
		return support.hasStateChanged();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTable vt)
	{
		support.setModel(vt);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset(VirtualTable vt)
	{
		support.reset(vt);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(int row, VirtualTable vt)
	{
		support.setModel(row,vt);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(final VirtualTableRow virtualTableRow)
	{
		support.setModel(virtualTableRow);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putHiddenField(String name, Object value)
	{
		support.putHiddenField(name,value);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getHiddenField(String name)
	{
		return support.getHiddenField(name);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Iterable<String> getHiddenFieldNames()
	{
		return support.getHiddenFieldNames();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getData(boolean withNulls)
	{
		return support.getData(withNulls);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableRow getVirtualTableRow()
	{
		return support.getVirtualTableRow();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTable getVirtualTable()
	{
		return support.getVirtualTable();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() throws VirtualTableException, DBException
	{
		save(true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(boolean synchronizeDB) throws VirtualTableException, DBException
	{
		support.save(synchronizeDB);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update() throws VirtualTableException, DBException
	{
		update(true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(boolean synchronizeDB) throws VirtualTableException, DBException
	{
		support.update(synchronizeDB);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRowsInVT(VirtualTable vt, KeyValues keyValues, boolean synchronizeDB)
			throws VirtualTableException, DBException
	{
		support.updateRowsInVT(vt,keyValues,synchronizeDB);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertRowInVT(VirtualTable vt, boolean synchronizeDB) throws VirtualTableException,
			DBException
	{
		support.insertRowInVT(vt,synchronizeDB);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert() throws VirtualTableException, DBException
	{
		insert(true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(final boolean synchronizeDB) throws VirtualTableException, DBException
	{
		support.insert(synchronizeDB);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() throws VirtualTableException, DBException
	{
		delete(true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(boolean synchronizeDB) throws VirtualTableException, DBException
	{
		support.delete(synchronizeDB);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean verifyFormularComponents()
	{
		return support.verifyFormularComponents();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Validation validateFormularComponents()
	{
		return support.validateFormularComponents();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Validation validateFormularComponents(final Validation validation)
	{
		return support.validateFormularComponents(validation);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void submit(String url, String target) throws IOException
	{
		support.submit(url,target);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getURLAdd()
	{
		return support.getURLAdd();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public QueryInfo createQuery(String connector) throws IllegalStateException,
			IllegalArgumentException
	{
		return support.createQuery(connector);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Condition createCondition(String connector) throws IllegalArgumentException
	{
		return createCondition(connector,(Query)null);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Condition createCondition(String connector, final Query query)
			throws IllegalArgumentException
	{
		return support.createCondition(connector,query);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Condition createCondition(String connector, final Collection paramCollection)
			throws IllegalArgumentException
	{
		return support.createCondition(connector,paramCollection);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void search(String connector, VirtualTableOwner target) throws IllegalArgumentException
	{
		support.search(connector,target);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putClientProperty(Object key, Object value)
	{
		if(value == null && clientProperties == null)
		{
			return;
		}
		
		if(clientProperties == null)
		{
			clientProperties = new HashMap();
		}
		
		if(value == null)
		{
			clientProperties.remove(key);
		}
		else
		{
			clientProperties.put(key,value);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getClientProperty(Object key)
	{
		if(clientProperties == null)
		{
			return null;
		}
		else
		{
			return clientProperties.get(key);
		}
	}
	
	// ***************************************************
	// Paging
	// ***************************************************
	
	private boolean					pagingEnabled	= false;
	private boolean					pagingAutoQuery	= true;
	private VirtualFormularPageControl	pageControl;
	
	
	@Override
	public boolean isPagingEnabled()
	{
		return pagingEnabled;
	}
	
	
	@Override
	public void setPagingEnabled(boolean pagingEnabled)
	{
		this.pagingEnabled = pagingEnabled;
	}
	
	
	@BeanProperty(owner = "pagingEnabled")
	public void setPagingAutoQuery(boolean pagingAutoQuery)
	{
		this.pagingAutoQuery = pagingAutoQuery;
	}
	
	
	public boolean getPagingAutoQuery()
	{
		return pagingAutoQuery;
	}
	
	
	public void setModelForPaging(VirtualTable vt) throws DBException
	{
		setModelForPaging(vt,vt.getSelect());
	}
	
	
	public void setModelForPaging(VirtualTable vt, SELECT select, Object... params)
			throws DBException
	{
		getPageControl().changeModel(vt,select,params,0);
	}
	
	
	@Override
	public VirtualFormularPageControl getPageControl()
	{
		if(pageControl == null)
		{
			pageControl = new VirtualFormularPageControl(this);
			
			if(pagingAutoQuery)
			{
				try
				{
					setModelForPaging(support.lookupVT().clone());
				}
				catch(DBException e)
				{
					// direct catch here.. no throws declared within interface
					LOG.error(e);
				}
			}
		}
		
		return pageControl;
	}
	
}
