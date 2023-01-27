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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import xdev.db.DBConnection;
import xdev.db.DBDataSource;
import xdev.db.DBException;
import xdev.db.Result;
import xdev.db.sql.Condition;
import xdev.db.sql.SELECT;
import xdev.ui.FormularComponent.ValueChangeListener;
import xdev.ui.MasterDetailComponent.DetailHandler;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.EntityRelationship;
import xdev.vt.EntityRelationship.Entity;
import xdev.vt.EntityRelationships;
import xdev.vt.KeyValues;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;


/**
 * Default implementation of {@link MasterDetailHandler}.
 * <p>
 * If you plan to implement your own {@link MasterDetailHandler} this is the
 * best point to start.
 * </p>
 * 
 * @see MasterDetail#setHandler(MasterDetailHandler)
 * @see MasterDetailComponent
 * @author XDEV Software
 */
public class DefaultMasterDetailHandler implements MasterDetailHandler
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log					= LoggerFactory
																.getLogger(DefaultMasterDetailHandler.class);
	
	protected final static String	MASTER_COMPONENT	= "MASTER_COMPONENT";
	protected final static String	DETAIL_COMPONENT	= "DETAIL_COMPONENT";
	protected final static String	WORKING				= MasterDetailHandler.class.getName()
																.concat(".working");
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect(MasterDetailComponent master, Formular detail) throws MasterDetailException
	{
		if(master.isMultiSelect())
		{
			throw new MasterDetailException(master,detail,
					"The master component does support multiple selection");
		}
		
		connectImpl(master,detail);
	}
	
	
	/**
	 * Called from {@link #connect(MasterDetailComponent, Formular)} after
	 * preconditions are checked successfully.
	 * 
	 * @param master
	 *            the component which operates as master
	 * @param detail
	 *            the {@link Formular} which operates as detail view
	 */
	protected void connectImpl(final MasterDetailComponent master, final Formular detail)
	{
		master.addValueChangeListener(new ValueChangeListener()
		{
			@Override
			public void valueChanged(Object eventObject)
			{
				VirtualTableRow row = master.getSelectedVirtualTableRow();
				if(row == null)
				{
					return;
				}
				
				VirtualTable formularVT = detail.getVirtualTable();
				if(formularVT == null)
				{
					return;
				}
				
				KeyValues pkValues = new KeyValues(row);
				row = formularVT.getRow(pkValues);
				try
				{
					if(!Boolean.TRUE.equals(detail.getClientProperty(OFFLINE)))
					{
						if(row != null)
						{
							row.reload();
						}
						else
						{
							if(formularVT.queryAndAppend(pkValues))
							{
								row = formularVT.getLastRow();
							}
						}
					}
					
					if(row != null)
					{
						detail.setModel(row);
					}
				}
				catch(Exception e)
				{
					log.error(e);
				}
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect(MasterDetailComponent master, MasterDetailComponent detail)
			throws MasterDetailException
	{
		if(master.isMultiSelect())
		{
			throw new MasterDetailException(master,detail,
					"The master component does support multiple selection");
		}
		
		connectImpl(master,detail);
	}
	
	
	/**
	 * Called from
	 * {@link #connect(MasterDetailComponent, MasterDetailComponent)} after
	 * preconditions are checked successfully.
	 * 
	 * @param master
	 *            the component which operates as master
	 * @param detail
	 *            the component which operates as detail view
	 */
	protected void connectImpl(MasterDetailComponent master, MasterDetailComponent detail)
	{
		new MasterDetailComponentConnection(master,detail);
	}
	
	
	
	/**
	 * Default implementation for the connection of two
	 * {@link MasterDetailComponent}s.
	 */
	protected static class MasterDetailComponentConnection
	{
		/**
		 * The master component of this connection.
		 */
		protected final MasterDetailComponent	master;
		/**
		 * The detail component of this connection.
		 */
		protected final MasterDetailComponent	detail;
		
		
		protected MasterDetailComponentConnection(MasterDetailComponent master,
				MasterDetailComponent detail)
		{
			this.master = master;
			this.detail = detail;
			
			((JComponent)detail).putClientProperty(MASTER_COMPONENT,master);
			((JComponent)master).putClientProperty(DETAIL_COMPONENT,detail);
			
			master.addValueChangeListener(createMasterHandler());
			detail.setDetailHandler(createDetailHandler());
		}
		
		
		/**
		 * Factory method for the handler of the master component.
		 * 
		 * @return the handler for the master component
		 */
		protected ValueChangeListener createMasterHandler()
		{
			return new DefaultMasterHandler();
		}
		
		
		
		protected class DefaultMasterHandler implements ValueChangeListener
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void valueChanged(Object eventObject)
			{
				if(!isWorking(master) && master.getSelectedVirtualTableRow() != null)
				{
					try
					{
						setWorking(master,true);
						setWorking(detail,true);
						updateDetailComponent();
					}
					catch(MasterDetailException mde)
					{
						log.error(mde);
					}
					finally
					{
						setWorking(master,false);
						setWorking(detail,false);
					}
				}
			}
		}
		
		
		protected void updateDetailComponent() throws MasterDetailException
		{
			MasterDetailInfo info = new MasterDetailInfo(master,detail);
			Condition condition = getAndCondition(info.detailKeyColumns);
			updateDetailComponent(condition,info.getMasterKeyValues(),info);
			clearFollowingDetailComponents();
		}
		
		
		protected void updateDetailComponent(Condition condition, Object[] values,
				MasterDetailInfo info)
		{
			detail.updateModel(condition,values);
		}
		
		
		protected void clearFollowingDetailComponents()
		{
			MasterDetailComponent detail = MasterDetailComponentConnection.this.detail;
			while((detail = getDetail(detail)) != null)
			{
				detail.clearModel();
			}
		}
		
		
		/**
		 * Factory method for the handler of the detail component.
		 * 
		 * @return the handler for the detail component
		 */
		protected DetailHandler createDetailHandler()
		{
			return new DefaultDetailHandler();
		}
		
		
		
		protected class DefaultDetailHandler implements DetailHandler
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void checkDetailView(Map<String, Object> value)
			{
				if(!isWorking(detail))
				{
					try
					{
						updateMasterComponents(value.values().toArray());
					}
					catch(Exception e)
					{
						log.error(e);
					}
				}
			}
		}
		
		
		protected void updateMasterComponents(Object[] detailPrimaryKeyValue)
				throws MasterDetailException, DBException
		{
			List<MasterDetailComponent> stack = new ArrayList();
			MasterDetailComponent c = detail;
			while(c != null)
			{
				stack.add(0,c);
				c = getMaster(c);
			}
			
			DBDataSource<?> dataSource = null;
			
			SELECT select = new SELECT();
			int max = stack.size() - 2;
			MasterDetailInfo[] infos = new MasterDetailInfo[max + 1];
			for(int i = 0; i <= max; i++)
			{
				MasterDetailComponent master = stack.get(i);
				MasterDetailComponent detail = stack.get(i + 1);
				
				MasterDetailInfo info = new MasterDetailInfo(master,detail);
				infos[i] = info;
				select.columns((Object[])info.masterKeyColumns);
				if(i == 0)
				{
					dataSource = info.masterVT.getDataSource();
					select.FROM(info.masterVT);
				}
				select.INNER_JOIN(info.detailVT,
						getAndCondition(info.detailKeyColumns,info.masterKeyColumns));
				if(i == max)
				{
					select.WHERE(getAndCondition(info.getDetailPrimaryKeyColumns()));
				}
			}
			
			DBConnection<?> con = dataSource.openConnection();
			try
			{
				Result keys = con.query(select,detailPrimaryKeyValue);
				if(keys.next())
				{
					int col = 0;
					for(MasterDetailInfo info : infos)
					{
						try
						{
							setWorking(master,true);
							setWorking(detail,true);
							
							int kc = info.masterKeyColumns.length;
							Object[] key = new Object[kc];
							Map<String, Object> record = new HashMap();
							for(int i = 0; i < kc; i++)
							{
								key[i] = keys.getObject(col++);
								record.put(info.masterKeyColumns[i].getName(),key[i]);
							}
							
							info.master.setMasterValue(info.masterVT,record);
							info.detail.updateModel(getAndCondition(info.detailKeyColumns),key);
						}
						finally
						{
							setWorking(master,false);
							setWorking(detail,false);
						}
					}
				}
			}
			finally
			{
				if(con != null)
				{
					con.close();
				}
			}
		}
		
		
		protected Condition getAndCondition(VirtualTableColumn[] leftSideColumns,
				VirtualTableColumn[] rightSideColumns)
		{
			Condition condition = null;
			for(int i = 0; i < leftSideColumns.length; i++)
			{
				Condition part = leftSideColumns[i].eq(rightSideColumns[i]);
				if(condition == null)
				{
					condition = part;
				}
				else
				{
					condition = condition.AND(part);
				}
			}
			return condition;
		}
		
		
		protected Condition getAndCondition(VirtualTableColumn[] columns)
		{
			Condition condition = null;
			for(int i = 0; i < columns.length; i++)
			{
				Condition part = columns[i].eq("?");
				if(condition == null)
				{
					condition = part;
				}
				else
				{
					condition = condition.AND(part);
				}
			}
			return condition;
		}
		
		
		protected boolean isWorking(MasterDetailComponent cpn)
		{
			return Boolean.TRUE.equals(((JComponent)cpn).getClientProperty(WORKING));
		}
		
		
		protected void setWorking(MasterDetailComponent cpn, boolean working)
		{
			((JComponent)cpn).putClientProperty(WORKING,working ? Boolean.TRUE : null);
		}
		
		
		protected MasterDetailComponent getDetail(MasterDetailComponent master)
		{
			Object o = ((JComponent)master).getClientProperty(DETAIL_COMPONENT);
			if(o != null && o instanceof MasterDetailComponent)
			{
				return (MasterDetailComponent)o;
			}
			return null;
		}
		
		
		protected MasterDetailComponent getMaster(MasterDetailComponent detail)
		{
			Object o = ((JComponent)detail).getClientProperty(MASTER_COMPONENT);
			if(o != null && o instanceof MasterDetailComponent)
			{
				return (MasterDetailComponent)o;
			}
			return null;
		}
	}
	
	
	
	/**
	 * Holds information of a distinct master detail connection.
	 * 
	 * @see #getMasterKeyValue()
	 * @see #getDetailPrimaryKeyColumn()
	 */
	protected static class MasterDetailInfo
	{
		public final MasterDetailComponent	master;
		public final VirtualTable			masterVT;
		public final VirtualTableColumn[]	masterKeyColumns;
		
		public final MasterDetailComponent	detail;
		public final VirtualTable			detailVT;
		public final VirtualTableColumn[]	detailKeyColumns;
		
		public final EntityRelationship		relation;
		public final Entity					detailEntity;
		
		
		protected MasterDetailInfo(MasterDetailComponent master, MasterDetailComponent detail)
				throws MasterDetailException
		{
			this.master = master;
			this.detail = detail;
			
			masterVT = master.getVirtualTable();
			if(masterVT == null)
			{
				throw new MasterDetailException(master,detail,
						"Master component has no VirtualTable");
			}
			
			VirtualTableColumn[] masterKeys = masterVT.getPrimaryKeyColumns();
			if(masterKeys == null || masterKeys.length == 0)
			{
				throw new MasterDetailException(master,detail,"Master has no primary key");
			}
			
			detailVT = detail.getVirtualTable();
			if(detailVT == null)
			{
				throw new MasterDetailException(master,detail,
						"Detail component has no VirtualTable");
			}
			
			relation = EntityRelationships.getModel().getRelationship(masterVT.getName(),
					VirtualTableColumn.getNamesOf(masterKeys),detailVT.getName());
			if(relation == null)
			{
				throw new MasterDetailException(master,detail,"No relation found");
			}
			
			Entity masterEntity = relation.getReferrer(masterVT.getName());
			// now get the master columns, to get the right column order (13188)
			masterKeyColumns = masterVT.getColumns(masterEntity.getColumnNames());
			
			detailEntity = relation.getReferrer(detailVT.getName());
			detailKeyColumns = detailVT.getColumns(detailEntity.getColumnNames());
			for(int i = 0; i < detailKeyColumns.length; i++)
			{
				if(detailKeyColumns[i] == null)
				{
					throw new MasterDetailException(master,detail,"Detail column '"
							+ detailEntity.getColumnName(i) + "' not found in relation");
				}
			}
		}
		
		
		/**
		 * Determines the current selected key of the master component.
		 * 
		 * @return the current master key value or <code>null</code> if nothing
		 *         is selected
		 * @throws MasterDetailException
		 *             if the column count of the master's primary key != 1
		 */
		protected Object[] getMasterKeyValues() throws MasterDetailException
		{
			VirtualTableRow selectedRow = master.getSelectedVirtualTableRow();
			if(selectedRow == null)
			{
				return null;
			}
			KeyValues masterKeyValues = new KeyValues(selectedRow);
			return masterKeyValues.getValues(VirtualTableColumn.getNamesOf(masterKeyColumns));
		}
		
		
		/**
		 * Determines the primary key columns of the detail table.
		 * 
		 * @return the detail's primary key columns
		 * @throws MasterDetailException
		 *             if the detail table has no primary key
		 */
		protected VirtualTableColumn[] getDetailPrimaryKeyColumns() throws MasterDetailException
		{
			VirtualTableColumn[] detailKeys = detailVT.getPrimaryKeyColumns();
			if(detailKeys == null || detailKeys.length == 0)
			{
				throw new MasterDetailException(master,detail,"Detail has no primary key");
			}
			return detailKeys;
		}
	}
}
