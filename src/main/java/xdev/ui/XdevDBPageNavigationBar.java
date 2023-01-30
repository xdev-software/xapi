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


import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xdev.ui.paging.PageControl;
import xdev.ui.paging.Pageable;


/**
 * With a page navigation bar a {@link Pageable} component can be controlled.<br>
 * To connect this bar with a {@link Pageable} use
 * {@link #setPageable(Pageable)}.
 *
 *
 * @author XDEV Software (HL)
 * @since 4.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevDBPageNavigationBar extends JPanel
{
	public final static int			FIRST					= 0;
	public final static int			PREVIOUS				= 1;
	public final static int			NEXT					= 2;
	public final static int			LAST					= 3;
	
	private final AbstractButton[]	buttons;
	private final JLabel			lblUnit;
	private final JComponent		cmdOptions;
	private final JSpinner			spinPage;
	private final JLabel			lblPageCount;
	
	private int						rowsPerPage				= PageControl.DYNAMIC_ROW_COUNT;
	private boolean					showDynamicOption		= true;
	private int[]					fixedRowOptionChoices	= {5,10,20,50,100};
	
	private Pageable				pageable;
	private PageControl				pageControl;
	
	
	public XdevDBPageNavigationBar()
	{
		super(new BorderLayout());
		
		// JPanel pnlCmd = new JPanel(new GridLayout(1,4,0,0));
		final JToolBar tb = new JToolBar();
		tb.setRollover(true);
		tb.setFloatable(false);
		this.buttons = new AbstractButton[4];
		for(int action = FIRST; action <= LAST; action++)
		{
			this.buttons[action] = this.createNavigationButton(action);
			tb.add(this.buttons[action]);
		}
		this.add(tb,BorderLayout.LINE_START);
		
		this.cmdOptions = this.createOptionsButton();
		
		this.lblUnit = new JLabel(UIResourceBundle.getString("dbpagenavigationbar.page"));
		
		this.spinPage = this.createCurrentPageSpinner();
		
		this.lblPageCount = new JLabel();
		
		final JPanel pnlCenter = new JPanel(new GridBagLayout());
		pnlCenter.setBorder(UIManager.getBorder("TextField.border"));
		pnlCenter.setBackground(UIManager.getColor("TextField.background"));
		pnlCenter.add(this.cmdOptions,new GBC(1,1,1,1,0.0,0.0,GBC.LINE_START,GBC.NONE,new Insets(0,
				3,0,2)));
		pnlCenter.add(this.lblUnit,new GBC(2,1,1,1,0.0,0.0,GBC.LINE_START,GBC.NONE,new Insets(0,3,
				0,3)));
		pnlCenter.add(this.spinPage,new GBC(3,1,1,1,0.0,0.0,GBC.LINE_START,GBC.NONE,new Insets(0,0,
				0,3)));
		pnlCenter.add(new JLabel(UIResourceBundle.getString("dbpagenavigationbar.pageof")),new GBC(
				4,1,1,1,0.0,0.0,GBC.LINE_START,GBC.NONE,new Insets(0,0,0,3)));
		pnlCenter.add(this.lblPageCount,new GBC(5,1,1,1,0.0,0.0,GBC.LINE_START,GBC.NONE,new Insets(
				0,0,0,3)));
		pnlCenter.add(new JLabel(),new GBC(6,1,1,1,1.0,0.0,GBC.LINE_START,GBC.HORIZONTAL,
				new Insets(0,0,0,0)));
		this.add(pnlCenter,BorderLayout.CENTER);
		
		this.setPageable(null);
	}
	
	
	/**
	 * @return the {@link Pageable} which is control by this bar
	 * @see #setPageable(Pageable)
	 */
	public Pageable getPageable()
	{
		return this.pageable;
	}
	
	
	/**
	 * Registered the {@link Pageable} which should be controlled by this bar.
	 *
	 * @param pageable
	 *            the component to be controlled
	 * @see #getPageable()
	 */
	@BeanProperty(category = DefaultBeanCategories.OBJECT)
	public void setPageable(final Pageable pageable)
	{
		this.pageable = pageable;
		
		boolean isSingleRow = false;
		
		if(pageable != null)
		{
			this.pageControl = pageable.getPageControl();
			this.pageControl.setRowsPerPage(this.rowsPerPage);
			this.cmdOptions.setEnabled(true);
			
			isSingleRow = this.pageControl.isSingleRowPager();
			
			this.pageChanged();
			this.pageControl.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(final ChangeEvent e)
				{
					XdevDBPageNavigationBar.this.pageChanged();
				}
			});
		}
		else
		{
			this.pageControl = null;
			for(final JComponent button : this.buttons)
			{
				button.setEnabled(false);
			}
			this.spinPage.setModel(new SpinnerNumberModel(0,0,0,1));
			this.cmdOptions.setEnabled(false);
			this.lblPageCount.setText("0");
		}
		
		this.cmdOptions.setVisible(!isSingleRow);
		this.lblUnit.setText(UIResourceBundle.getString(isSingleRow ? "dbpagenavigationbar.row"
				: "dbpagenavigationbar.page"));
	}
	
	
	protected void pageChanged()
	{
		final boolean hasPrevious = this.pageControl.hasPreviousPage();
		final boolean hasNext = this.pageControl.hasNextPage();
		this.buttons[FIRST].setEnabled(hasPrevious);
		this.buttons[PREVIOUS].setEnabled(hasPrevious);
		this.buttons[NEXT].setEnabled(hasNext);
		this.buttons[LAST].setEnabled(hasNext);
		
		final int max = this.pageControl.getMaxPageIndex() + 1;
		int index = this.pageControl.getCurrentPageIndex() + 1;
		
		/*
		 * workaround for XdevVirtualTable (XDEVAPI-214) Oracle Database 11g
		 * returns -1 index must not be smaller than 1
		 */
		index = index < 1 ? 1 : index;
		
		this.spinPage.setModel(new SpinnerNumberModel(index,1,max,1));
		this.lblPageCount.setText(Integer.toString(max));
	}
	
	
	public boolean isActionEnabled(final int action)
	{
		return this.buttons[action].isEnabled();
	}
	
	
	/**
	 * Scrolls to the first page
	 */
	public void firstPage()
	{
		if(this.pageControl != null)
		{
			this.pageControl.firstPage();
		}
	}
	
	
	/**
	 * Scrolls to the previous page
	 */
	public void previousPage()
	{
		if(this.pageControl != null)
		{
			this.pageControl.previousPage();
		}
	}
	
	
	/**
	 * Scrolls to the next page
	 */
	public void nextPage()
	{
		if(this.pageControl != null)
		{
			this.pageControl.nextPage();
		}
	}
	
	
	/**
	 * Scrolls to the last page
	 */
	public void lastPage()
	{
		if(this.pageControl != null)
		{
			this.pageControl.lastPage();
		}
	}
	
	
	public void gotoPage(final int page)
	{
		if(this.pageControl != null)
		{
			this.pageControl.gotoPage(page);
		}
	}
	
	
	/**
	 * @param action
	 *            <ul>
	 *            <li>{@link #FIRST}</li>
	 *            <li>{@link #PREVIOUS}</li>
	 *            <li>{@link #NEXT}</li>
	 *            <li>{@link #LAST}</li>
	 *            </ul>
	 * @return the button component which performs the action
	 */
	protected AbstractButton createNavigationButton(final int action)
	{
		final JButton cmd = new JButton();
		cmd.setFocusable(false);
		
		switch(action)
		{
			case FIRST:
				cmd.setIcon(GraphicUtils.loadResIcon("first.png"));
				cmd.setToolTipText(UIResourceBundle.getString("dbpagenavigationbar.first"));
				cmd.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent e)
					{
						XdevDBPageNavigationBar.this.firstPage();
					}
				});
			break;
			
			case PREVIOUS:
				cmd.setIcon(GraphicUtils.loadResIcon("previous.png"));
				cmd.setToolTipText(UIResourceBundle.getString("dbpagenavigationbar.previous"));
				cmd.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent e)
					{
						XdevDBPageNavigationBar.this.previousPage();
					}
				});
			break;
			
			case NEXT:
				cmd.setIcon(GraphicUtils.loadResIcon("next.png"));
				cmd.setToolTipText(UIResourceBundle.getString("dbpagenavigationbar.next"));
				cmd.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent e)
					{
						XdevDBPageNavigationBar.this.nextPage();
					}
				});
			break;
			
			case LAST:
				cmd.setIcon(GraphicUtils.loadResIcon("last.png"));
				cmd.setToolTipText(UIResourceBundle.getString("dbpagenavigationbar.last"));
				cmd.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent e)
					{
						XdevDBPageNavigationBar.this.lastPage();
					}
				});
			break;
		}
		
		return cmd;
	}
	
	
	/**
	 * Sets the icon of the "first" button.
	 *
	 * @param icon
	 *            the new icon
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setFirstIcon(final Icon icon)
	{
		this.buttons[FIRST].setIcon(icon);
	}
	
	
	/**
	 * @return the icon of the "first" button
	 */
	public Icon getFirstIcon()
	{
		return this.buttons[FIRST].getIcon();
	}
	
	
	/**
	 * Sets the icon of the "previous" button.
	 *
	 * @param icon
	 *            the new icon
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setPreviousIcon(final Icon icon)
	{
		this.buttons[PREVIOUS].setIcon(icon);
	}
	
	
	/**
	 * @return the icon of the "previous" button
	 */
	public Icon getPreviousIcon()
	{
		return this.buttons[PREVIOUS].getIcon();
	}
	
	
	/**
	 * Sets the icon of the "next" button.
	 *
	 * @param icon
	 *            the new icon
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setNextIcon(final Icon icon)
	{
		this.buttons[NEXT].setIcon(icon);
	}
	
	
	/**
	 * @return the icon of the "next" button
	 */
	public Icon getNextIcon()
	{
		return this.buttons[NEXT].getIcon();
	}
	
	
	/**
	 * Sets the icon of the "last" button.
	 *
	 * @param icon
	 *            the new icon
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setLastIcon(final Icon icon)
	{
		this.buttons[LAST].setIcon(icon);
	}
	
	
	/**
	 * @return the icon of the "last" button
	 */
	public Icon getLastIcon()
	{
		return this.buttons[LAST].getIcon();
	}
	
	
	/**
	 * Sets how much rows per page should be displayed.
	 *
	 * @param rowsPerPage
	 *            a positive int or {@link PageControl#DYNAMIC_ROW_COUNT}
	 * @see PageControl#DYNAMIC_ROW_COUNT
	 */
	@BeanProperty(owner = "pageable", intMin = 0)
	public void setRowsPerPage(final int rowsPerPage)
	{
		if(this.rowsPerPage != rowsPerPage)
		{
			this.rowsPerPage = rowsPerPage;
			
			if(this.pageControl != null)
			{
				this.pageControl.setRowsPerPage(rowsPerPage);
			}
		}
	}
	
	
	/**
	 * @return the currenly displayed number of rows per page
	 * @see PageControl#DYNAMIC_ROW_COUNT
	 */
	public int getRowsPerPage()
	{
		return this.rowsPerPage;
	}
	
	
	@BeanProperty(owner = "pageable")
	public void setShowDynamicOption(final boolean showDynamicOption)
	{
		this.showDynamicOption = showDynamicOption;
	}
	
	
	public boolean getShowDynamicOption()
	{
		return this.showDynamicOption;
	}
	
	
	@BeanProperty(owner = "pageable")
	public void setFixedRowOptionChoices(final int[] fixedRowOptionChoices)
	{
		this.fixedRowOptionChoices = fixedRowOptionChoices;
	}
	
	
	public int[] getFixedRowOptionChoices()
	{
		return this.fixedRowOptionChoices;
	}
	
	
	protected JComponent createOptionsButton()
	{
		final JLabel lbl = new JLabel();
		lbl.setIcon(GraphicUtils.loadResIcon("visible.png"));
		lbl.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(final MouseEvent e)
			{
				if(lbl.isEnabled())
				{
					XdevDBPageNavigationBar.this.showOptionsPopup();
				}
			}
		});
		return lbl;
	}
	
	
	protected void showOptionsPopup()
	{
		this.createOptionsPopup().show(this.cmdOptions,0,this.cmdOptions.getHeight());
	}
	
	
	protected JPopupMenu createOptionsPopup()
	{
		final JPopupMenu popup = new JPopupMenu();
		final ButtonGroup bgOptions = new ButtonGroup();
		
		final int rowCount = this.pageControl != null ? this.pageControl.getRowsPerPage()
				: PageControl.DYNAMIC_ROW_COUNT;
		
		if(this.showDynamicOption)
		{
			final JRadioButtonMenuItem cmdDynamic = new JRadioButtonMenuItem(
					UIResourceBundle.getString("dbpagenavigationbar.dynamic"),
					rowCount == PageControl.DYNAMIC_ROW_COUNT);
			cmdDynamic.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(final ActionEvent e)
				{
					XdevDBPageNavigationBar.this.pageControl
							.setRowsPerPage(PageControl.DYNAMIC_ROW_COUNT);
				}
			});
			
			bgOptions.add(cmdDynamic);
			popup.add(cmdDynamic);
			popup.addSeparator();
		}
		
		for(int i = 0; i < this.fixedRowOptionChoices.length; i++)
		{
			final int rows = this.fixedRowOptionChoices[i];
			final JRadioButtonMenuItem cmdFixed = new JRadioButtonMenuItem(
					UIResourceBundle.getString("dbpagenavigationbar.rows",rows),rows == rowCount);
			cmdFixed.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(final ActionEvent e)
				{
					XdevDBPageNavigationBar.this.pageControl.setRowsPerPage(rows);
				}
			});
			bgOptions.add(cmdFixed);
			popup.add(cmdFixed);
		}
		
		return popup;
	}
	
	
	protected JSpinner createCurrentPageSpinner()
	{
		final JSpinner spinner = new JSpinner();
		spinner.setBorder(BorderFactory.createEmptyBorder());
		spinner.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(final ChangeEvent e)
			{
				XdevDBPageNavigationBar.this.gotoPage((Integer)spinner.getValue() - 1);
			}
		});
		return spinner;
	}
}
