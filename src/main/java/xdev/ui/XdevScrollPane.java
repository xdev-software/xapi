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


import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import xdev.ui.persistence.Persistable;


/**
 * The standard scroll pane in XDEV. Based on {@link JScrollPane}.
 * 
 * @see JScrollPane
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
public class XdevScrollPane extends JScrollPane implements Persistable
{
	public final static int	VIEWPORT				= 0;
	public final static int	CONTENT_PANE			= 1;
	
	private int				designTarget			= VIEWPORT;
	private SP_ContentPane	sp_contentPane;
	private SP_Viewport		sp_viewport;
	private XdevStyle		xdevBackground;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean			persistenceEnabled		= true;
	
	protected boolean		rootPaneCheckingEnabled	= false;
	
	
	/**
	 * Creates an new <code>XdevScrollPane</code>.
	 * 
	 * 
	 * <p>
	 * Alias for <code>XdevScrollPane(LayoutManager)</code>
	 * </p>
	 * 
	 */
	public XdevScrollPane()
	{
		this(null);
	}
	
	
	/**
	 * Creates an new <code>XdevScrollPane</code> with specified
	 * <code>LayoutManager</code>.
	 * 
	 * 
	 * <p>
	 * Alias for <code>XdevScrollPane(LayoutManager, int, int)</code>
	 * </p>
	 * 
	 * @param layout
	 *            the LayoutManager to use
	 */
	public XdevScrollPane(LayoutManager layout)
	{
		this(layout,VERTICAL_SCROLLBAR_ALWAYS,HORIZONTAL_SCROLLBAR_ALWAYS);
	}
	
	
	/**
	 * Creates an new <code>XdevScrollPane</code> with specified scrollbar
	 * policies.
	 * 
	 * <p>
	 * The available policy settings are listed at
	 * {@link #setVerticalScrollBarPolicy} and
	 * {@link #setHorizontalScrollBarPolicy}.
	 * <p>
	 * 
	 * <p>
	 * Alias for <code>XdevScrollPane(LayoutManager, int, int)</code>
	 * </p>
	 * 
	 * @param vsbPolicy
	 *            an integer that specifies the vertical scrollbar policy
	 * 
	 * @param hsbPolicy
	 *            an integer that specifies the horizontal scrollbar policy
	 */
	public XdevScrollPane(int vsbPolicy, int hsbPolicy)
	{
		this(null,vsbPolicy,hsbPolicy);
	}
	
	
	/**
	 * Creates an new <code>XdevScrollPane</code> with specified
	 * <code>LayoutManager</code> and scrollbar policies.
	 * 
	 * <p>
	 * The available policy settings are listed at
	 * {@link #setVerticalScrollBarPolicy} and
	 * {@link #setHorizontalScrollBarPolicy}.
	 * <p>
	 * 
	 * 
	 * @param layout
	 *            the LayoutManager to use
	 * 
	 * @param vsbPolicy
	 *            an integer that specifies the vertical scrollbar policy
	 * 
	 * @param hsbPolicy
	 *            an integer that specifies the horizontal scrollbar policy
	 */
	public XdevScrollPane(LayoutManager layout, int vsbPolicy, int hsbPolicy)
	{
		super(vsbPolicy,hsbPolicy);
		
		setOpaque(false);
		
		xdevBackground = new XdevStyle(this);
		
		sp_viewport = new SP_Viewport();
		setViewport(sp_viewport);
		
		sp_contentPane = new SP_ContentPane(layout);
		setViewportView(sp_contentPane);
		
		setRootPaneCheckingEnabled(true);
	}
	
	
	/**
	 * Returns the <code>contentPane</code> for this {@link XdevScrollPane}.
	 * 
	 * @return the <code>sp_contentPane</code> property as {@link Container}
	 * 
	 */
	public Container getContentPane()
	{
		return sp_contentPane;
	}
	
	
	/**
	 * Used to control the method of scrolling the viewport contents. You may
	 * want to change this mode to get maximum performance for your use case.
	 * 
	 * @param mode
	 *            one of the following values:
	 *            <ul>
	 *            <li>JViewport.BLIT_SCROLL_MODE
	 *            <li>JViewport.BACKINGSTORE_SCROLL_MODE
	 *            <li>JViewport.SIMPLE_SCROLL_MODE
	 *            </ul>
	 * 
	 * @see #BLIT_SCROLL_MODE
	 * @see #BACKINGSTORE_SCROLL_MODE
	 * @see #SIMPLE_SCROLL_MODE
	 * 
	 * @beaninfo bound: false description: Method of moving contents for
	 *           incremental scrolls. enum: BLIT_SCROLL_MODE
	 *           JViewport.BLIT_SCROLL_MODE BACKINGSTORE_SCROLL_MODE
	 *           JViewport.BACKINGSTORE_SCROLL_MODE SIMPLE_SCROLL_MODE
	 *           JViewport.SIMPLE_SCROLL_MODE
	 * 
	 * @since 4.0
	 */
	public void setScrollMode(int mode)
	{
		getViewport().setScrollMode(mode);
	}
	
	
	/**
	 * Returns the current scrolling mode.
	 * 
	 * @return the <code>scrollMode</code> property
	 * @see #setScrollMode
	 * @since 4.0
	 */
	public int getScrollMode()
	{
		return getViewport().getScrollMode();
	}
	
	
	// TODO javaDoc
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setDesignTarget(int designTarget)
	{
		this.designTarget = designTarget;
		sp_viewport.setPaint(designTarget == VIEWPORT);
		sp_contentPane.setPaint(designTarget == CONTENT_PANE);
		repaint();
	}
	
	
	// TODO javaDoc
	public int getDesignTarget()
	{
		return designTarget;
	}
	
	
	/**
	 * Scroll to the position of <code>x</code> in the horizontal scrollbar.
	 * 
	 * @param x
	 *            the current value, between minimum and maximum - visibleAmount
	 * 
	 * @see #setInnerX(int)
	 * @see JScrollBar#setValue(int)
	 */
	public void scrollToX(int x)
	{
		setInnerX(x);
	}
	
	
	/**
	 * Sets the horizontal scrollbar's value.
	 * 
	 * @param x
	 *            the current value, between minimum and maximum - visibleAmount
	 * 
	 * @see JScrollBar#setValue(int)
	 */
	public void setInnerX(int x)
	{
		if(horizontalScrollBar != null && horizontalScrollBar.getMaximum() >= x)
		{
			horizontalScrollBar.setValue(x);
		}
	}
	
	
	/**
	 * Scroll to the position of <code>y</code> in the vertical scrollbar.
	 * 
	 * @param y
	 *            the current value, between minimum and maximum - visibleAmount
	 * 
	 * @see #setInnerY(int)
	 * @see JScrollBar#setValue(int)
	 */
	public void scrollToY(int y)
	{
		setInnerY(y);
	}
	
	
	/**
	 * Sets the vertical scrollbar's value.
	 * 
	 * @param y
	 *            the current value, between minimum and maximum - visibleAmount
	 * 
	 * @see JScrollBar#setValue(int)
	 */
	public void setInnerY(int y)
	{
		if(verticalScrollBar != null && verticalScrollBar.getMaximum() >= y)
		{
			verticalScrollBar.setValue(y);
		}
	}
	
	
	/**
	 * Sets the inner size of this {@link XdevScrollPane}.
	 * 
	 * @param width
	 *            the new width of the inner size
	 * @param height
	 *            the new width of the inner size
	 */
	public void setInnerSize(int width, int height)
	{
		setInnerSize(new Dimension(width,height));
	}
	
	
	/**
	 * Sets the inner size of this {@link XdevScrollPane}. If <code>d</code> is
	 * <code>null</code>, the UI will be asked for the preferred size.
	 * 
	 * @param d
	 *            The new inner size, or <code>null</code>
	 */
	public void setInnerSize(Dimension d)
	{
		sp_contentPane.setPreferredSize(d);
		revalidate();
	}
	
	
	// TODO javadoc
	public int getScrollRange()
	{
		if(verticalScrollBar != null)
		{
			return verticalScrollBar.getUnitIncrement();
		}
		
		if(horizontalScrollBar != null)
		{
			return horizontalScrollBar.getUnitIncrement();
		}
		
		return 0;
	}
	
	
	// TODO javadoc
	public void setScrollRange(int range)
	{
		if(verticalScrollBar != null)
		{
			setScrollAmount(verticalScrollBar,range);
		}
		
		if(horizontalScrollBar != null)
		{
			setScrollAmount(horizontalScrollBar,range);
		}
	}
	
	
	// TODO javadoc
	private void setScrollAmount(JScrollBar sb, int range)
	{
		sb.setUnitIncrement(range);
		sb.setBlockIncrement(range * 2);
	}
	
	
	/**
	 * Returns the horizontal scrollbar's value.
	 * 
	 * @return the value of the horizontal scrollbar or <code>0</code> if the
	 *         <code>scrollbar</code> is <code>null</code>
	 */
	public int getInnerX()
	{
		if(horizontalScrollBar == null)
		{
			return 0;
		}
		
		return horizontalScrollBar.getValue();
	}
	
	
	/**
	 * Returns the vertical scrollbar's value.
	 * 
	 * @return the value of the vertical scrollbar or <code>0</code> if the
	 *         <code>scrollbar</code> is <code>null</code>
	 */
	public int getInnerY()
	{
		if(verticalScrollBar == null)
		{
			return 0;
		}
		
		return verticalScrollBar.getValue();
	}
	
	
	// TODO javaDoc
	public int getInnerWidth()
	{
		Dimension d = sp_contentPane.getPreferredSize();
		if(d != null)
		{
			return d.width;
		}
		
		return sp_contentPane.getWidth();
	}
	
	
	// TODO javaDoc
	public void setInnerWidth(int width)
	{
		setInnerSize(width,getInnerHeight());
	}
	
	
	// TODO javaDoc
	public int getInnerHeight()
	{
		Dimension d = sp_contentPane.getPreferredSize();
		if(d != null)
		{
			return d.height;
		}
		
		return sp_contentPane.getHeight();
	}
	
	
	// TODO javaDoc
	public void setInnerHeight(int height)
	{
		setInnerSize(getInnerWidth(),height);
	}
	
	
	// TODO javaDoc
	public void pack()
	{
		Rectangle r = new Rectangle(getSize());
		
		LayoutManager lm = sp_contentPane.getLayout();
		if(lm != null)
		{
			r.setSize(lm.preferredLayoutSize(sp_contentPane));
		}
		else
		{
			int c = sp_contentPane.getComponentCount();
			for(int i = 0; i < c; i++)
			{
				r.add(sp_contentPane.getComponent(i).getBounds());
			}
		}
		
		r.setLocation(0,0);
		setInnerSize(r.getSize());
		
		revalidate();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOpaque(boolean isOpaque)
	{
		super.setOpaque(isOpaque);
		
		if(sp_viewport != null)
		{
			sp_viewport.setOpaque(isOpaque);
		}
		
		if(sp_contentPane != null)
		{
			sp_contentPane.setOpaque(isOpaque);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		
		if(sp_viewport != null)
		{
			sp_viewport.setBackground(bg);
		}
		
		if(sp_contentPane != null)
		{
			sp_contentPane.setBackground(bg);
		}
	}
	
	
	/**
	 * Returns the background type as <code>int</code> for this
	 * {@link XdevScrollPane}.
	 * 
	 * @return the background type as <code>int</code> for this
	 *         {@link XdevScrollPane}.
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle
	 */
	public int getBackgroundType()
	{
		return xdevBackground.getBackgroundType();
	}
	
	
	/**
	 * Sets the background type as <code>int</code> for this
	 * {@link XdevScrollPane}.
	 * 
	 * @param backgroundType
	 *            the background type as <code>int</code> for this
	 *            {@link XdevScrollPane}.
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle#NONE
	 * @see XdevStyle#COLOR
	 * @see XdevStyle#GRADIENT
	 * @see XdevStyle#SYSTEM
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setBackgroundType(int backgroundType)
	{
		xdevBackground.setBackgroundType(backgroundType);
		repaint();
	}
	
	
	/**
	 * Returns the gradient {@link Color} one for this {@link XdevScrollPane}.
	 * 
	 * @return the gradient {@link Color} one for this {@link XdevScrollPane}.
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle#GRADIENT
	 */
	public Color getGradientColor1()
	{
		return xdevBackground.getGradientColor1();
	}
	
	
	/**
	 * Sets the gradient {@link Color} one for this {@link XdevScrollPane}.
	 * 
	 * <p>
	 * Gradient colors are only applied if the background type of this
	 * {@link XdevScrollPane} is set to {@link XdevStyle#GRADIENT}. You can set
	 * the background type via XDEV IDE or manually with
	 * {@link #setBackgroundType(int)}.
	 * </p>
	 * 
	 * @param gradientColor1
	 *            the gradient {@link Color} one for this {@link XdevScrollPane}
	 *            .
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle#GRADIENT
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setGradientColor1(Color gradientColor1)
	{
		xdevBackground.setGradientColor1(gradientColor1);
		repaint();
	}
	
	
	/**
	 * Returns the gradient {@link Color} two for this {@link XdevScrollPane}.
	 * 
	 * @return the gradient {@link Color} two for this {@link XdevScrollPane}.
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle#GRADIENT
	 */
	public Color getGradientColor2()
	{
		return xdevBackground.getGradientColor2();
	}
	
	
	/**
	 * Sets the gradient {@link Color} two for this {@link XdevScrollPane}.
	 * 
	 * <p>
	 * Gradient colors are only applied if the background type of this
	 * {@link XdevScrollPane} is set to {@link XdevStyle#GRADIENT}. You can set
	 * the background type via XDEV IDE or manually with
	 * {@link #setBackgroundType(int)}.
	 * </p>
	 * 
	 * @param gradientColor2
	 *            the gradient {@link Color} two for this {@link XdevScrollPane}
	 *            .
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle#GRADIENT
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setGradientColor2(Color gradientColor2)
	{
		xdevBackground.setGradientColor2(gradientColor2);
		repaint();
	}
	
	
	/**
	 * Returns the gradient align for this {@link XdevScrollPane}.
	 * 
	 * 
	 * 
	 * @return the gradient align for this {@link XdevScrollPane}.
	 * 
	 * @see #setBackgroundType(int)
	 * 
	 * @see XdevStyle#LEFF_RIGHT
	 * @see XdevStyle#TOP_BOTTOM
	 * @see XdevStyle#TOPLEFT_BOTTOMRIGHT
	 * @see XdevStyle#TOPRIGHT_BOTTOMLEFT
	 * @see XdevStyle#RECTANGULAR
	 * @see XdevStyle#RADIAL
	 */
	public int getGradientAlign()
	{
		return xdevBackground.getGradientAlign();
	}
	
	
	/**
	 * Sets the gradient align for this {@link XdevScrollPane}.
	 * <p>
	 * Gradient align is only applied if the background type of this
	 * {@link XdevScrollPane} is set to {@link XdevStyle#GRADIENT}. You can set
	 * the background type via XDEV IDE or manually with
	 * {@link #setBackgroundType(int)}.
	 * </p>
	 * 
	 * 
	 * @param gradientAlign
	 *            the gradient align for this {@link XdevScrollPane}.
	 * 
	 * @see #setBackgroundType(int)
	 * 
	 * @see XdevStyle#LEFF_RIGHT
	 * @see XdevStyle#TOP_BOTTOM
	 * @see XdevStyle#TOPLEFT_BOTTOMRIGHT
	 * @see XdevStyle#TOPRIGHT_BOTTOMLEFT
	 * @see XdevStyle#RECTANGULAR
	 * @see XdevStyle#RADIAL
	 * 
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setGradientAlign(int gradientAlign)
	{
		xdevBackground.setGradientAlign(gradientAlign);
		repaint();
	}
	
	
	/**
	 * Returns the texture path for this {@link XdevScrollPane}.
	 * 
	 * @return the texture path for this {@link XdevScrollPane}.
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle#setTexturePath(String)
	 */
	public String getTexturePath()
	{
		return xdevBackground.getTexturePath();
	}
	
	
	/**
	 * Sets the texture path for this {@link XdevScrollPane}.
	 * 
	 * @param texturePath
	 *            the texture path for this {@link XdevScrollPane}.
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle#getTexturePath()
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setTexturePath(String texturePath)
	{
		xdevBackground.setTexturePath(texturePath);
		repaint();
	}
	
	
	/**
	 * Returns the texture for this {@link XdevScrollPane}.
	 * 
	 * @return the texture for this {@link XdevScrollPane}.
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle#getTexture()
	 * @see XdevImage
	 */
	public XdevImage getTexture()
	{
		return xdevBackground.getTexture();
	}
	
	
	/**
	 * Sets the texture for this {@link XdevScrollPane}.
	 * 
	 * @param image
	 *            the texture for this {@link XdevScrollPane}.
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle#setTexture(XdevImage)
	 * @see XdevImage
	 */
	public void setTexture(XdevImage image)
	{
		xdevBackground.setTexture(image);
	}
	
	
	/**
	 * Returns the texture option for this {@link XdevScrollPane}.
	 * 
	 * @return the texture option for this {@link XdevScrollPane} as
	 *         <code>int</code>.
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle#getTextureOption()
	 * @see XdevStyle#CENTER
	 * @see XdevStyle#STRECHED
	 * @see XdevStyle#TILED
	 */
	public int getTextureOption()
	{
		return xdevBackground.getTextureOption();
	}
	
	
	/**
	 * Sets the texture option for this {@link XdevScrollPane}.
	 * 
	 * @param textureOption
	 *            the texture option for this {@link XdevScrollPane}.
	 * 
	 * @see #xdevBackground
	 * @see XdevStyle#setTextureOption(int)
	 * @see XdevStyle#CENTER
	 * @see XdevStyle#STRECHED
	 * @see XdevStyle#TILED
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setTextureOption(int textureOption)
	{
		xdevBackground.setTextureOption(textureOption);
		repaint();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		String toString = UIUtils.toString(this);
		if(toString != null)
		{
			return toString;
		}
		
		return super.toString();
	}
	
	
	
	class SP_Viewport extends JViewport
	{
		private boolean	paint	= false;
		
		
		SP_Viewport()
		{
			super();
		}
		
		
		public void setPaint(boolean b)
		{
			paint = b;
		}
		
		
		@Override
		protected void paintComponent(Graphics g)
		{
			if(paint)
			{
				xdevBackground.paintBackground(GraphicUtils.create2D(g),this,0,0,getWidth(),
						getHeight());
				xdevBackground.drawTexture(g,this,0,0,getWidth(),getHeight());
			}
		}
	}
	
	
	
	class SP_ContentPane extends XComponent
	{
		private boolean	paint	= true;
		
		
		public SP_ContentPane(LayoutManager layout)
		{
			super(layout);
		}
		
		
		public void setPaint(boolean b)
		{
			paint = b;
		}
		
		
		@Override
		protected void paintComponent(Graphics g)
		{
			if(paint)
			{
				xdevBackground.paintBackground(GraphicUtils.create2D(g),this,0,0,getWidth(),
						getHeight());
				xdevBackground.drawTexture(g,this,0,0,getWidth(),getHeight());
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see #savePersistentState()
	 */
	@Override
	public void loadPersistentState(String persistentState)
	{
		String[] persistentValues = persistentState.split(Persistable.VALUE_SEPARATOR);
		int index = 0;
		try
		{
			this.getVerticalScrollBar().setValue(Integer.parseInt(persistentValues[index++]));
			this.getHorizontalScrollBar().setValue(Integer.parseInt(persistentValues[index++]));
		}
		catch(Exception e)
		{
			// do nothing her, if persistent value can't be retrieved...
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Persisted properties:
	 * <ul>
	 * <li>vertical position</li>
	 * <li>horizontal position</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public String savePersistentState()
	{
		String verticalValue = Integer.toString(this.getVerticalScrollBar().getValue());
		String horizontalValue = Integer.toString(this.getHorizontalScrollBar().getValue());
		StringBuilder persistentState = new StringBuilder();
		persistentState.append(verticalValue).append(Persistable.VALUE_SEPARATOR);
		persistentState.append(horizontalValue);
		return persistentState.toString();
	}
	
	
	/**
	 * Uses the name of the component as a persistent id.
	 * <p>
	 * If no name is specified the name of the class will be used. This will
	 * work only for one persistent instance of the class!
	 * </p>
	 * {@inheritDoc}
	 */
	@Override
	public String getPersistentId()
	{
		return (this.getName() != null) ? this.getName() : this.getClass().getSimpleName();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPersistenceEnabled()
	{
		return persistenceEnabled;
	}
	
	
	/**
	 * Sets the persistenceEnabled flag.
	 * 
	 * @param persistenceEnabled
	 *            the state for this instance
	 */
	public void setPersistenceEnabled(boolean persistenceEnabled)
	{
		this.persistenceEnabled = persistenceEnabled;
	}
	
	
	protected boolean isRootPaneCheckingEnabled()
	{
		return rootPaneCheckingEnabled;
	}
	
	
	protected void setRootPaneCheckingEnabled(boolean enabled)
	{
		rootPaneCheckingEnabled = enabled;
	}
	
	
	protected void addImpl(Component comp, Object constraints, int index)
	{
		if(isRootPaneCheckingEnabled())
		{
			getContentPane().add(comp,constraints,index);
		}
		else
		{
			super.addImpl(comp,constraints,index);
		}
	}
	
	
	public void setLayout(LayoutManager manager)
	{
		if(isRootPaneCheckingEnabled())
		{
			getContentPane().setLayout(manager);
		}
		else
		{
			super.setLayout(manager);
		}
	}
	
	
	@Override
	public LayoutManager getLayout()
	{
		if(isRootPaneCheckingEnabled())
		{
			return getContentPane().getLayout();
		}
		else
		{
			return super.getLayout();
		}
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
