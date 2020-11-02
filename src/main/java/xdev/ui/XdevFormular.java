package xdev.ui;

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


import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ContainerEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JMenu;

import xdev.Application;
import xdev.db.DBException;
import xdev.db.QueryInfo;
import xdev.db.locking.LockNotificationStrategy;
import xdev.db.locking.LockTimeMonitor;
import xdev.db.locking.PessimisticLockStrategy;
import xdev.db.locking.RowAlreadyLockedException;
import xdev.db.sql.Condition;
import xdev.db.sql.SELECT;
import xdev.event.ApplicationExitListener;
import xdev.lang.cmd.Query;
import xdev.ui.FormularComponent.ValueChangeListener;
import xdev.ui.event.ContainerHierarchyListener;
import xdev.ui.event.FirstShowAdapter;
import xdev.ui.event.FormularListener;
import xdev.ui.locking.AbstractLockRenewWindow;
import xdev.ui.locking.HybridLockInUseNotificationStrategy;
import xdev.ui.locking.LockAlreadyInUseIndicator;
import xdev.ui.locking.LockPropertyInfo;
import xdev.ui.locking.Lockable;
import xdev.ui.locking.LockableFormSupport;
import xdev.ui.locking.LockingApplicationExitAdapter;
import xdev.ui.locking.OnComponentFocusPessimisticLockStrategy;
import xdev.ui.paging.FormularPageControl;
import xdev.ui.paging.Pageable;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.KeyValues;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableException;


/**
 * The {@link XdevFormular} is a GUI form container containing
 * {@link FormularComponent}s and {@link ManyToManyComponent}s.
 * 
 * <p>
 * The {@link XdevFormular} can be used to display a row of a
 * {@link VirtualTable}. Therefore the {@link XdevFormular} must contain
 * {@link FormularComponent}s that are mapped to the columns of the
 * {@link VirtualTable} you want to display. This can be done by the XDEV IDE or
 * manually.
 * </p>
 * 
 * <p>
 * The {@link XdevFormular} also manages n:m-relations. Therefore
 * {@link ManyToManyComponent}s can be added into this container.
 * </p>
 * 
 * <p>
 * The {@link XdevFormular} provides methods to:
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
 * @see FormularListener
 * @see FormularComponent
 * @see MasterDetailComponent
 * @see ManyToManyComponent
 * 
 * @since 2.0
 */
public class XdevFormular extends XdevContainer implements Formular, Pageable, Lockable
{
	
	private FormularSupport			support						= new FormularSupport(this);
	
	private boolean					saveStateAfterModelUpdate	= true;
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	LOGGER						= LoggerFactory
																		.getLogger(XdevFormular.class);
	private LockPropertyInfo		lockingPropertyInfo			= new LockPropertyInfo();
	
	private ApplicationExitListener	exitListener;
	
	
	/**
	 * @return the support
	 */
	public FormularSupport getSupport()
	{
		return support;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	// @BeanProperty(category = DefaultBeanCategories.DATA, owner =
	// "showDialog")
	public void setRenewLockWindow(AbstractLockRenewWindow renewDialog)
	{
		this.getLockingPropertyInfo().setRenewLockWindow(renewDialog);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractLockRenewWindow getRenewLockWindow()
	{
		return this.getLockingPropertyInfo().getRenewLockWindow();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DATA, longMin = 0, longMax = Long.MAX_VALUE)
	@Override
	public void setNotificationThreshold(long notificationThreshold)
	{
		this.getLockingPropertyInfo().setNotificationThreshold(notificationThreshold);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getNotificationThreshold()
	{
		return this.getLockingPropertyInfo().getNotificationThreshold();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	// @BeanProperty(category = DefaultBeanCategories.DATA, owner =
	// "showDialog")
	public void setLockInUseNotifier(LockAlreadyInUseIndicator lockInUseDialog)
	{
		this.getLockingPropertyInfo().setLockInUseNotifier(lockInUseDialog);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockAlreadyInUseIndicator getLockInUseNotifier()
	{
		return this.getLockingPropertyInfo().getLockInUseNotifier();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DATA, owner = Lockable.AUTO_LOCK, longMin = 1, longMax = Long.MAX_VALUE)
	@Override
	public void setLockingTime(long lockingTime)
	{
		this.getLockingPropertyInfo().setLockingTime(lockingTime);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLockingTime()
	{
		return this.getLockingPropertyInfo().getLockingTime();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DATA, owner = Lockable.AUTO_LOCK)
	@Override
	public void setCountdownMonitors(LockTimeMonitor[] countdownMonitors)
	{
		this.getLockingPropertyInfo().setCountdownMonitors(countdownMonitors);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockTimeMonitor[] getCountdownMonitors()
	{
		return this.getLockingPropertyInfo().getCountdownMonitors();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DATA)
	@Override
	public void setAutoLock(boolean autoLock)
	{
		// lazy init locking
		this.lockingPropertyInfo.setAutoLock(autoLock);
		
		// default strategies
		this.setLockNotificationStrategy(new HybridLockInUseNotificationStrategy(this));
		this.setLockStrategy(new OnComponentFocusPessimisticLockStrategy(this));
		
		if(autoLock)
		{
			LockableFormSupport support = new LockableFormSupport(this);
			this.support = support;
			this.exitListener = new LockingApplicationExitAdapter();
			Application.addExitListener(this.exitListener);
		}
		else
		{
			this.support = new FormularSupport(this);
			if(this.exitListener != null)
			{
				Application.removeExitListener(this.exitListener);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAutoLock()
	{
		return this.getLockingPropertyInfo().isAutoLock();
	}
	
	
	/**
	 * Create a new {@link XdevFormular} with no layout manager.
	 * 
	 * <p>
	 * Alias for <code>XdevFormular(null)</code>
	 * </p>
	 * 
	 * @see #XdevFormular(LayoutManager)
	 * 
	 */
	public XdevFormular()
	{
		super();
	}
	
	
	/**
	 * Create a new {@link XdevFormular} with the specified layout manager.
	 * 
	 * @param layout
	 *            the LayoutManager to use
	 */
	public XdevFormular(LayoutManager layout)
	{
		super(layout);
	}
	
	/*
	 * Handler for FormularListener#formularComponentValueChanged(FormularEvent)
	 * 
	 * @since 3.1
	 */
	{
		
		addContainerListener(new ContainerHierarchyListener()
		{
			Map<FormularComponent, ValueChangeListener>	listenerMap			= new HashMap();
			Map<Component, FocusListener>				focusListenerMap	= new HashMap();
			
			
			@Override
			public void componentAddedInHierarchy(ContainerEvent e)
			{
				Component child = e.getChild();
				if(child instanceof Container)
				{
					UIUtils.lookupComponentTree((Container)child,new ComponentTreeVisitor()
					{
						@Override
						public Object visit(Component cpn)
						{
							if(cpn instanceof FormularComponent)
							{
								addFocusListener(cpn);
								add((FormularComponent)cpn);
							}
							return null;
						}
					});
				}
				else if(child instanceof FormularComponent)
				{
					add((FormularComponent)child);
				}
			}
			
			
			void add(final FormularComponent formularComponent)
			{
				if(!listenerMap.containsKey(formularComponent))
				{
					ValueChangeListener valueChangeListener = new ValueChangeListener()
					{
						@Override
						public void valueChanged(Object eventObject)
						{
							fireFormularComponentValueChanged(formularComponent,eventObject);
						}
					};
					formularComponent.addValueChangeListener(valueChangeListener);
					listenerMap.put(formularComponent,valueChangeListener);
				}
			}
			
			
			void addFocusListener(final Component component)
			{
				if(!focusListenerMap.containsKey(component))
				{
					FocusListener focusListener = new FocusListener()
					{
						@Override
						public void focusGained(FocusEvent e)
						{
							fireFormularComponentFocusGained(component,e);
						}
						
						
						@Override
						public void focusLost(FocusEvent e)
						{
							fireFormularComponentFocusLost(component,e);
						}
					};
					component.addFocusListener(focusListener);
					focusListenerMap.put(component,focusListener);
				}
			}
			
			
			@Override
			public void componentRemovedInHierarchy(ContainerEvent e)
			{
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	@Override
	public Iterable<FormularComponent> formComponents()
	{
		final List<FormularComponent> list = new ArrayList();
		lookupComponentTree(new ComponentTreeVisitor<Object, Component>()
		{
			@Override
			public Object visit(Component cpn)
			{
				if(cpn instanceof FormularComponent && !(cpn instanceof ManyToManyComponent))
				{
					list.add((FormularComponent)cpn);
				}
				return null;
			}
		});
		return list;
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	@Override
	public Iterable<ManyToManyComponent> manyToManyComponents()
	{
		final List<ManyToManyComponent> list = new ArrayList();
		lookupComponentTree(new ComponentTreeVisitor<Object, Component>()
		{
			@Override
			public Object visit(Component cpn)
			{
				if(cpn instanceof ManyToManyComponent)
				{
					list.add((ManyToManyComponent)cpn);
				}
				return null;
			}
		});
		return list;
	}
	
	
	/**
	 * Determines of the {@link FormularComponent} <code>formCpn</code> should
	 * be used in this formular's actions.
	 * 
	 * @param formCpn
	 *            the {@link FormularComponent} to check
	 * @return <code>true</code> if <code>formCpn</code> should be used,
	 *         <code>false</code> otherwise
	 */
	protected boolean use(FormularComponent formCpn)
	{
		// since 3.1, see FORMULAR_SKIP
		if(formCpn instanceof JComponent
				&& Boolean.TRUE.equals(((JComponent)formCpn).getClientProperty(FORMULAR_SKIP)))
		{
			return false;
		}
		
		return true;
	}
	
	
	private <T> T lookupComponentTree(final ComponentTreeVisitor<T, Component> _visitor)
	{
		ComponentTreeVisitor<T, Component> visitor = new ComponentTreeVisitor<T, Component>()
		{
			@Override
			public T visit(Component cpn)
			{
				T ret = null;
				
				if(!(cpn instanceof FormularComponent && !use((FormularComponent)cpn)))
				{
					ret = _visitor.visit(cpn);
				}
				
				return ret;
			}
		};
		return lookupComponentTreeImpl(this,visitor);
	}
	
	
	private <T> T lookupComponentTreeImpl(Container parent,
			ComponentTreeVisitor<T, Component> visitor)
	{
		T value = null;
		
		value = visitor.visit(parent);
		
		if(value == null)
		{
			if(parent instanceof JMenu)
			{
				parent = ((JMenu)parent).getPopupMenu();
			}
			
			int c = parent.getComponentCount();
			for(int i = 0; i < c && value == null; i++)
			{
				Component cpn = parent.getComponent(i);
				if(cpn instanceof Container)
				{
					// don't traverse through sub-forms
					if(!(cpn instanceof XdevFormular))
					{
						value = lookupComponentTreeImpl((Container)cpn,visitor);
					}
				}
				else
				{
					value = visitor.visit(cpn);
				}
			}
		}
		
		return value;
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
	
	
	protected void fireModelChanged()
	{
		support.fireModelChanged();
	}
	
	
	protected void fireFormularComponentValueChanged(FormularComponent formularComponent,
			Object formularComponentEventObject)
	{
		support.fireFormularComponentValueChanged(formularComponent,formularComponentEventObject);
	}
	
	
	protected void fireFormularComponentFocusGained(Component formularComponent,
			FocusEvent formularComponentEventObject)
	{
		// cannot delegate to support which would resolve in a public API change
		// (Formular Interface had to be updated)
		// support.fireFormularComponentFocusGained(formularComponent,formularComponentEventObject);
		FocusListener[] listeners = this.getFocusListeners();
		if(listeners != null && listeners.length > 0)
		{
			FocusEvent event = new FocusEvent(formularComponent,FocusEvent.FOCUS_GAINED);
			for(FocusListener listener : listeners)
			{
				listener.focusGained(event);
			}
		}
	}
	
	
	protected void fireFormularComponentFocusLost(Component formularComponent,
			FocusEvent formularComponentEventObject)
	{
		// cannot delegate to support which would resolve in a public API change
		// (Formular Interface had to be updated)
		// support.fireFormularComponentFocusGained(formularComponent,formularComponentEventObject);
		FocusListener[] listeners = this.getFocusListeners();
		if(listeners != null && listeners.length > 0)
		{
			FocusEvent event = new FocusEvent(formularComponent,FocusEvent.FOCUS_LOST);
			for(FocusListener listener : listeners)
			{
				listener.focusLost(event);
			}
		}
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
			firePropertyChange(SAVE_STATE_AFTER_MODEL_UPDATE_PROPERTY,oldValue,
					saveStateAfterModelUpdate);
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
		VirtualTableRow virtualTableRow = vt.createRow();
		support.setModel(virtualTableRow);
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
		VirtualTableRow virtualTableRow = vt.getRow(row);
		support.setModel(virtualTableRow);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws RowAlreadyLockedException
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
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setForeground(Color fg)
	{
		super.setForeground(fg);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setFont(Font font)
	{
		super.setFont(font);
	}
	
	// ***************************************************
	// Paging
	// ***************************************************
	
	private boolean				pagingEnabled	= false;
	private boolean				pagingAutoQuery	= true;
	private FormularPageControl	pageControl;
	
	
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
	
	
	public void setModelForPaging(VirtualTable vt)
	{
		setModelForPaging(vt,vt.getSelect());
	}
	
	
	public void setModelForPaging(VirtualTable vt, SELECT select, Object... params)
	{
		try
		{
			getPageControl().changeModel(vt,select,params,0);
		}
		catch(Exception e)
		{
			LOGGER.error(e);
		}
	}
	
	
	@Override
	public FormularPageControl getPageControl()
	{
		if(pageControl == null)
		{
			pageControl = new FormularPageControl(this);
			
			if(pagingAutoQuery)
			{
				if(isShowing())
				{
					setModelForPaging(support.lookupVT().clone());
				}
				else
				{
					FirstShowAdapter.invokeLater(this,new Runnable()
					{
						@Override
						public void run()
						{
							setModelForPaging(support.lookupVT().clone());
						}
					});
				}
			}
		}
		
		return pageControl;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FormularListener[] getFormularListeners()
	{
		return listenerList.getListeners(FormularListener.class);
	}
	
	
	private LockPropertyInfo getLockingPropertyInfo()
	{
		if(this.lockingPropertyInfo != null)
		{
			return this.lockingPropertyInfo;
		}
		else
		{
			throw new IllegalStateException("Locking has not been activated");
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DATA, owner = Lockable.AUTO_LOCK)
	@Override
	public void setLockStrategy(PessimisticLockStrategy lockStrategy)
	{
		this.lockingPropertyInfo.setLockStrategy(lockStrategy);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PessimisticLockStrategy getLockStrategy()
	{
		return this.lockingPropertyInfo.getLockStrategy();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.DATA, owner = Lockable.AUTO_LOCK)
	@Override
	public void setLockNotificationStrategy(LockNotificationStrategy notificationStrategy)
	{
		this.lockingPropertyInfo.setLockNotificationStrategy(notificationStrategy);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockNotificationStrategy getLockNotificationStrategy()
	{
		return this.lockingPropertyInfo.getLockNotificationStrategy();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addFocusListener(FocusListener l)
	{
		this.listenerList.add(FocusListener.class,l);
	}
	
	
	@Override
	public synchronized void removeFocusListener(FocusListener l)
	{
		this.listenerList.remove(FocusListener.class,l);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized FocusListener[] getFocusListeners()
	{
		return this.listenerList.getListeners(FocusListener.class);
	}
}
