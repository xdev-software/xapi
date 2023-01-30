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
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;


/**
 * The standard desktop pane in XDEV. Based on {@link JDesktopPane}.
 * 
 * @see JDesktopPane
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
public class XdevDesktopPane extends JDesktopPane
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5559962272028958379L;
	
	private XdevStyle			style;
	

	/**
	 * Constructor for creating a new instance of a {@link XdevDesktopPane}.
	 * 
	 */
	public XdevDesktopPane()
	{
		super();
		
		style = new XdevStyle(this);
		
		setDragMode(GraphicUtils.getInternalFrameDragMode());
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component add(Component c, int z)
	{
		super.add(c,JLayeredPane.DRAG_LAYER,z);
		return c;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		style.paintBackground(GraphicUtils.create2D(g),this,0,0,getWidth(),getHeight());
		style.drawTexture(g,this,0,0,getWidth(),getHeight());
	}
	

	/**
	 * Returns the background type as <code>int</code> for this
	 * {@link XdevDesktopPane}.
	 * 
	 * @return the background type as <code>int</code> for this
	 *         {@link XdevDesktopPane}.
	 * 
	 * @see #style
	 * @see XdevStyle
	 */
	public int getBackgroundType()
	{
		return style.getBackgroundType();
	}
	

	/**
	 * Sets the background type as <code>int</code> for this
	 * {@link XdevDesktopPane}.
	 * 
	 * @param backgroundType
	 *            the background type as <code>int</code> for this
	 *            {@link XdevDesktopPane}.
	 * 
	 * @see #style
	 * @see XdevStyle#NONE
	 * @see XdevStyle#COLOR
	 * @see XdevStyle#GRADIENT
	 * @see XdevStyle#SYSTEM
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setBackgroundType(int backgroundType)
	{
		style.setBackgroundType(backgroundType);
		setOpaque(backgroundType != XdevStyle.NONE);
		repaint();
	}
	

	/**
	 * Returns the gradient {@link Color} one for this {@link XdevDesktopPane}.
	 * 
	 * @return the gradient {@link Color} one for this {@link XdevDesktopPane}.
	 * 
	 * @see #style
	 * @see XdevStyle#GRADIENT
	 */
	public Color getGradientColor1()
	{
		return style.getGradientColor1();
	}
	

	/**
	 * Sets the gradient {@link Color} one for this {@link XdevDesktopPane}.
	 * 
	 * <p>
	 * Gradient colors are only applied if the background type of this
	 * {@link XdevScrollPane} is set to {@link XdevStyle#GRADIENT}. You can set
	 * the background type via XDEV IDE or manually with
	 * {@link #setBackgroundType(int)}.
	 * </p>
	 * 
	 * @param gradientColor1
	 *            the gradient {@link Color} one for this
	 *            {@link XdevDesktopPane} .
	 * 
	 * @see #style
	 * @see XdevStyle#GRADIENT
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setGradientColor1(Color gradientColor1)
	{
		style.setGradientColor1(gradientColor1);
		repaint();
	}
	

	/**
	 * Returns the gradient {@link Color} two for this {@link XdevDesktopPane}.
	 * 
	 * @return the gradient {@link Color} two for this {@link XdevDesktopPane}.
	 * 
	 * @see #style
	 * @see XdevStyle#GRADIENT
	 */
	public Color getGradientColor2()
	{
		return style.getGradientColor2();
	}
	

	/**
	 * Sets the gradient {@link Color} two for this {@link XdevDesktopPane}.
	 * 
	 * <p>
	 * Gradient colors are only applied if the background type of this
	 * {@link XdevScrollPane} is set to {@link XdevStyle#GRADIENT}. You can set
	 * the background type via XDEV IDE or manually with
	 * {@link #setBackgroundType(int)}.
	 * </p>
	 * 
	 * @param gradientColor2
	 *            the gradient {@link Color} two for this
	 *            {@link XdevDesktopPane} .
	 * 
	 * @see #style
	 * @see XdevStyle#GRADIENT
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setGradientColor2(Color gradientColor2)
	{
		style.setGradientColor2(gradientColor2);
		repaint();
	}
	

	/**
	 * Returns the gradient align for this {@link XdevDesktopPane}.
	 * 
	 * @return the gradient align for this {@link XdevDesktopPane}.
	 * 
	 * 
	 * @see #style
	 * @see #setBackgroundType(int)
	 * @see XdevStyle#LEFF_RIGHT
	 * @see XdevStyle#TOP_BOTTOM
	 * @see XdevStyle#TOPLEFT_BOTTOMRIGHT
	 * @see XdevStyle#TOPRIGHT_BOTTOMLEFT
	 * @see XdevStyle#RECTANGULAR
	 * @see XdevStyle#RADIAL
	 */
	public int getGradientAlign()
	{
		return style.getGradientAlign();
	}
	

	/**
	 * Sets the gradient align for this {@link XdevDesktopPane}.
	 * <p>
	 * Gradient align is only applied if the background type of this
	 * {@link XdevScrollPane} is set to {@link XdevStyle#GRADIENT}. You can set
	 * the background type via XDEV IDE or manually with
	 * {@link #setBackgroundType(int)}.
	 * </p>
	 * 
	 * 
	 * @param gradientAlign
	 *            the gradient align for this {@link XdevDesktopPane}.
	 * 
	 * @see #style
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
		style.setGradientAlign(gradientAlign);
		repaint();
	}
	

	/**
	 * Returns the texture path for this {@link XdevDesktopPane}.
	 * 
	 * @return the texture path for this {@link XdevDesktopPane}.
	 * 
	 * @see #style
	 * @see XdevStyle#setTexturePath(String)
	 */
	public String getTexturePath()
	{
		return style.getTexturePath();
	}
	

	/**
	 * Sets the texture path for this {@link XdevDesktopPane}.
	 * 
	 * @param texturePath
	 *            the texture path for this {@link XdevDesktopPane}.
	 * 
	 * @see #style
	 * @see XdevStyle#getTexturePath()
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setTexturePath(String texturePath)
	{
		style.setTexturePath(texturePath);
		repaint();
	}
	

	/**
	 * Returns the texture for this {@link XdevDesktopPane}.
	 * 
	 * @return the texture for this {@link XdevDesktopPane}.
	 * 
	 * @see #style
	 * @see XdevStyle#getTexture()
	 * @see XdevImage
	 */
	public XdevImage getTexture()
	{
		return style.getTexture();
	}
	

	/**
	 * Sets the texture for this {@link XdevDesktopPane}.
	 * 
	 * @param image
	 *            the texture for this {@link XdevDesktopPane}.
	 * 
	 * @see #style
	 * @see XdevStyle#setTexture(XdevImage)
	 * @see XdevImage
	 */
	public void setTexture(XdevImage image)
	{
		style.setTexture(image);
	}
	

	/**
	 * Returns the texture option for this {@link XdevDesktopPane}.
	 * 
	 * @return the texture option for this {@link XdevDesktopPane} as
	 *         <code>int</code>.
	 * 
	 * @see #style
	 * @see XdevStyle#getTextureOption()
	 * @see XdevStyle#CENTER
	 * @see XdevStyle#STRECHED
	 * @see XdevStyle#TILED
	 */
	public int getTextureOption()
	{
		return style.getTextureOption();
	}
	

	/**
	 * Sets the texture option for this {@link XdevDesktopPane}.
	 * 
	 * @param textureOption
	 *            the texture option for this {@link XdevDesktopPane}.
	 * 
	 * @see #style
	 * @see XdevStyle#setTextureOption(int)
	 * @see XdevStyle#CENTER
	 * @see XdevStyle#STRECHED
	 * @see XdevStyle#TILED
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setTextureOption(int textureOption)
	{
		style.setTextureOption(textureOption);
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
