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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;


@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
public class XdevWindowContainer extends XdevComponent implements XdevRootPaneContainer,
		XdevFocusCycleRoot
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6548647014026742500L;
	
	private JRootPane			rootPane;
	private JPanel				contentPane;
	private JScrollPane			scrl;
	private XdevWindow			window;
	private int					tabIndexOffset		= 0;
	
	
	/**
	 * Constructs a new {@link XdevWindowContainer} with default
	 * {@link BorderLayout}.
	 */
	public XdevWindowContainer()
	{
		super(new BorderLayout());
		
		rootPane = createRootPane();
		
		add(rootPane,BorderLayout.CENTER);
		
		contentPane = new JPanel(new BorderLayout());
		contentPane.setOpaque(false);
		
		scrl = new JScrollPane();
		scrl.setBorder(BorderFactory.createEmptyBorder());
		scrl.setOpaque(false);
		scrl.getViewport().setOpaque(false);
		
		rootPane.setContentPane(contentPane);
	}
	
	
	protected JRootPane createRootPane()
	{
		JRootPane rootPane = new JRootPane();
		rootPane.setOpaque(false);
		return rootPane;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addImpl(Component comp, Object constraints, int index)
	{
		if(comp instanceof XdevWindow)
		{
			throw new IllegalArgumentException(
					"Don't use add(...) to insert a XdevWindow into a XdevWindowContainer"
							+ " - use setXdevWindow(XdevWindow) instead.");
		}
		
		super.addImpl(comp,constraints,index);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addMouseListener(MouseListener l)
	{
		if(contentPane != null)
		{
			contentPane.addMouseListener(l);
		}
		else
		{
			super.addMouseListener(l);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addMouseMotionListener(MouseMotionListener l)
	{
		if(contentPane != null)
		{
			contentPane.addMouseMotionListener(l);
		}
		else
		{
			super.addMouseMotionListener(l);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addMouseWheelListener(MouseWheelListener l)
	{
		if(contentPane != null)
		{
			contentPane.addMouseWheelListener(l);
		}
		else
		{
			super.addMouseWheelListener(l);
		}
	}
	
	
	/**
	 * Determines when the vertical scrollbar appears in the scrollpane. Legal
	 * values are:
	 * <ul>
	 * <li><code>ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED</code>
	 * <li><code>ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER</code>
	 * <li><code>ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS</code>
	 * </ul>
	 * 
	 * @param policy
	 *            one of the three values listed above
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>policy</code> is not one of the legal values shown
	 *             above
	 * 
	 * @see #getVerticalScrollBarPolicy
	 */
	public void setVerticalScrollBarPolicy(int policy) throws IllegalArgumentException
	{
		scrl.setVerticalScrollBarPolicy(policy);
	}
	
	
	/**
	 * Returns the vertical scroll bar policy value.
	 * 
	 * @return the vertical scroll bar policy value
	 * 
	 * @see #setVerticalScrollBarPolicy
	 */
	public int getVerticalScrollBarPolicy()
	{
		return scrl.getVerticalScrollBarPolicy();
	}
	
	
	/**
	 * Determines when the horizontal scrollbar appears in the scrollpane. The
	 * options are:
	 * <ul>
	 * <li><code>ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED</code>
	 * <li><code>ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER</code>
	 * <li><code>ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS</code>
	 * </ul>
	 * 
	 * @param policy
	 *            one of the three values listed above
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>policy</code> is not one of the legal values shown
	 *             above
	 * 
	 * @see #getHorizontalScrollBarPolicy
	 */
	public void setHorizontalScrollBarPolicy(int policy) throws IllegalArgumentException
	{
		scrl.setHorizontalScrollBarPolicy(policy);
	}
	
	
	/**
	 * Returns the horizontal scroll bar policy value.
	 * 
	 * @return the horizontal scroll bar policy value
	 * 
	 * @see #setHorizontalScrollBarPolicy
	 */
	public int getHorizontalScrollBarPolicy()
	{
		return scrl.getHorizontalScrollBarPolicy();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setXdevWindow(XdevWindow window)
	{
		Window w = getWindow();
		if(w == null)
		{
			w = JOptionPane.getRootFrame();
		}
		
		if(this.window != null)
		{
			WindowEvent event = new WindowEvent(w,WindowEvent.WINDOW_CLOSED);
			for(WindowListener listener : listenerList.getListeners(WindowListener.class))
			{
				listener.windowClosed(event);
			}
			
			this.window.setOwner(null);
		}
		
		this.window = window;
		
		scrl.setViewportView(null);
		contentPane.removeAll();
		
		if(window != null)
		{
			window.setOwner(this);
			window.setPreferredSize(null);
			
			contentPane.add(window,BorderLayout.CENTER);
			
			JMenuBar menuBar = window.getJMenuBar();
			if(menuBar != null)
			{
				contentPane.add(menuBar,BorderLayout.NORTH);
			}
			
			if(scrl.getVerticalScrollBarPolicy() == JScrollPane.VERTICAL_SCROLLBAR_NEVER
					&& scrl.getHorizontalScrollBarPolicy() == JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
			{
				rootPane.setContentPane(contentPane);
			}
			else
			{
				scrl.setViewportView(contentPane);
				rootPane.setContentPane(scrl);
			}
		}
		else
		{
			rootPane.setContentPane(contentPane);
		}
		
		revalidate();
		repaint();
		
		if(window != null)
		{
			WindowEvent event = new WindowEvent(w,WindowEvent.WINDOW_OPENED);
			for(WindowListener listener : listenerList.getListeners(WindowListener.class))
			{
				listener.windowOpened(event);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevWindow getXdevWindow()
	{
		return this.window;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void paintImage(Graphics2D g)
	{
		int w = getWidth();
		int h = getHeight();
		paintBackground(g,0,0,w,h);
		drawTexture(g,0,0,w,h);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addWindowListener(WindowListener listener)
	{
		listenerList.add(WindowListener.class,listener);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeWindowListener(WindowListener listener)
	{
		listenerList.remove(WindowListener.class,listener);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Window getWindow()
	{
		return SwingUtilities.getWindowAncestor(this);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JRootPane getRootPane()
	{
		return rootPane;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Container getContentPane()
	{
		return contentPane;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getGlassPane()
	{
		return rootPane.getGlassPane();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JLayeredPane getLayeredPane()
	{
		return rootPane.getLayeredPane();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContentPane(Container contentPane)
	{
		rootPane.setContentPane(contentPane);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGlassPane(Component glassPane)
	{
		rootPane.setGlassPane(glassPane);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLayeredPane(JLayeredPane layeredPane)
	{
		rootPane.setLayeredPane(layeredPane);
	}
	
	
	/**
	 * Implements {@link XdevRootPaneContainer#setResizable(boolean)} with no
	 * effect.
	 */
	@Override
	public void setResizable(boolean b)
	{
	}
	
	
	/**
	 * Implements {@link XdevRootPaneContainer#pack()} with no effect.
	 */
	@Override
	public void pack()
	{
	}
	
	
	/**
	 * Implements {@link XdevRootPaneContainer#setLocationRelativeTo(Component)}
	 * with no effect.
	 */
	@Override
	public void setLocationRelativeTo(Component c)
	{
	}
	
	
	/**
	 * Implements {@link XdevRootPaneContainer#setTitle(String)} with no effect.
	 */
	@Override
	public void setTitle(String s)
	{
	}
	
	
	/**
	 * Implements {@link XdevRootPaneContainer#toFront()} with no effect.
	 */
	@Override
	public void toFront()
	{
	}
	
	
	/**
	 * Implements {@link XdevRootPaneContainer#toBack()} with no effect.
	 */
	@Override
	public void toBack()
	{
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public void setExtendedState(int extendedState)
	{
		// no op
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public int getExtendedState()
	{
		return NORMAL;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close()
	{
		setXdevWindow(null);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabIndexOffset()
	{
		return tabIndexOffset;
	}
	
	
	/**
	 * Sets the tabIndexOffset of this component. The
	 * <code>tabIndexOffset</code> affects the normal tab index.
	 * 
	 * @param tabIndexOffset
	 *            to be set
	 */
	public void setTabIndexOffset(int tabIndexOffset)
	{
		this.tabIndexOffset = tabIndexOffset;
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
}
