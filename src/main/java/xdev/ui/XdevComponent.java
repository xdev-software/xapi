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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.FilteredImageSource;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import xdev.util.MathUtils;


/**
 * This class serves as abstract base for many XDEV GUI components. It provides
 * many common methods for drawing (including gradient and transparency), moving
 * and sizing of XDEV GUI components.
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 * 
 */
public abstract class XdevComponent extends XComponent implements ClientProperties
{
	/**
	 * Alpha value of this {@link XdevComponent}. Range <code>0 - 255</code>.
	 */
	protected int			alpha			= 255;
	
	/**
	 * Alpha condition of this {@link XdevComponent}.
	 * 
	 * @see AlphaFilter
	 */
	protected int			alphaCondition	= AlphaFilter.COMPLETE;
	
	/**
	 * Alpha tolerance of this {@link XdevComponent}.
	 */
	protected int			alphaTolerance	= 0;
	
	/**
	 * Alpha colors of this {@link XdevComponent}. A array of {@link Color}s
	 * that will be affected by alpha effects on this {@link XdevComponent}.
	 */
	protected Color[]		alphaColors		= null;
	
	/**
	 * Style of this {@link XdevComponent}.
	 * 
	 * @see XdevStyle
	 */
	protected XdevStyle		style;
	
	/**
	 * JavaDoc omitted for private field.
	 */
	private boolean			paintAnything	= true;
	/**
	 * TODO
	 */
	protected boolean		useAlphaFilter	= false;
	/**
	 * TODO
	 */
	protected boolean		remakeImages	= false;
	/**
	 * Alpha filter for this {@link XdevComponent}.
	 * 
	 * @see AlphaFilter
	 */
	protected AlphaFilter	alphaFilter;
	/**
	 * TODO
	 */
	protected Image			filteredImage;
	/**
	 * TODO
	 */
	protected Rectangle		imageBounds;
	/**
	 * TODO
	 */
	protected Rectangle		clip;
	
	
	/**
	 * Create a new {@link XdevComponent} with no layout manager.
	 * 
	 * <p>
	 * Alias for <code>XdevComponent(null)</code>
	 * </p>
	 * 
	 * @see #XdevComponent(LayoutManager)
	 * 
	 */
	public XdevComponent()
	{
		this(null);
	}
	
	
	/**
	 * Create a new {@link XdevComponent} with the specified layout manager.
	 * 
	 * @param layout
	 *            the LayoutManager to use
	 */
	public XdevComponent(LayoutManager layout)
	{
		super(layout);
		
		style = new XdevStyle(this);
		
		setOpaque(true);
		
		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				boolean paintAnything = XdevComponent.this.paintAnything;
				int alpha = XdevComponent.this.alpha;
				
				checkAlphaUse();
				
				if(paintAnything != XdevComponent.this.paintAnything
						|| alpha != XdevComponent.this.alpha)
				{
					repaint();
				}
			}
		});
	}
	
	
	/**
	 * Forces this {@link XdevComponent} to repaint.
	 * 
	 * @see #repaint()
	 */
	public void forcePaint()
	{
		checkAlphaUse();
		
		if(isShowing())
		{
			repaint();
		}
	}
	
	
	/**
	 * TODO
	 */
	protected void checkAlphaUse()
	{
		style.flushTextureImage();
		
		useAlphaFilter = false;
		paintAnything = true;
		remakeImages = false;
		
		if(alpha < 255)
		{
			if(alpha == 0 && alphaCondition == AlphaFilter.COMPLETE)
			{
				paintAnything = false;
			}
			else
			{
				useAlphaFilter = true;
				remakeImages = true;
			}
		}
	}
	
	
	/**
	 * Returns the size for the alpha filter.
	 * 
	 * @return the size for the alpha filter.
	 * 
	 * @see Dimension
	 */
	public Dimension getSizeForFilter()
	{
		return getSize();
	}
	
	
	/**
	 * Returns the insets of the border.
	 * 
	 * @return the insets of the border.
	 * @see Insets
	 */
	public Insets getBorderInsets()
	{
		Border border = getBorder();
		if(border != null)
		{
			return border.getBorderInsets(this);
		}
		
		return new Insets(0,0,0,0);
	}
	
	
	/**
	 * Sets the border title for this {@link XdevComponent}.
	 * <p>
	 * <strong>Hint:</strong> Works only if the component has a
	 * {@link TitledBorder}. You can set a {@link TitledBorder} for this
	 * component via XDEV IDE or manually with {@link #setBorder(Border)}.
	 * </p>
	 * 
	 * @param title
	 *            {@link String} to be displayed in title of the border.
	 * 
	 * @see #setBorder(Border)
	 */
	public void setBorderTitle(String title)
	{
		Border border = getBorder();
		if(border != null && border instanceof TitledBorder)
		{
			((TitledBorder)border).setTitle(title);
		}
		else
		{
			setBorder(BorderFactory.createTitledBorder(title));
		}
		
		revalidate();
		repaint();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Graphics g)
	{
		paint(g);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpaque()
	{
		return super.isOpaque() && (style != null && style.isOpaque());
	}
	
	
	/**
	 * @since 3.2
	 */
	protected boolean paintComponent()
	{
		return isOpaque();
	}
	
	
	@Override
	protected void paintComponent(Graphics g)
	{
		if(paintAnything && paintComponent())
		{
			if(useAlphaFilter)
			{
				Dimension d = getSize();
				d.width = Math.max(1,d.width);
				d.height = Math.max(1,d.height);
				
				if(remakeImages)
				{
					remakeImages = false;
					
					GraphicsConfiguration config = UIUtils.getGraphicsConfigurationOf(this);
					java.awt.Image image = config.createCompatibleImage(d.width,d.height,
							Transparency.TRANSLUCENT);
					Graphics ig = image.getGraphics();
					paintImage(createG2D(ig));
					ig.dispose();
					
					if(alphaFilter == null)
					{
						alphaFilter = new AlphaFilter(this);
					}
					
					alphaFilter.update();
					filteredImage = createImage(new FilteredImageSource(image.getSource(),
							alphaFilter));
				}
				imageBounds = g.getClipBounds();
				if(imageBounds == null)
				{
					imageBounds = new Rectangle(0,0,d.width,d.height);
				}
				g.drawImage(filteredImage,imageBounds.x,imageBounds.y,imageBounds.x
						+ imageBounds.width,imageBounds.y + imageBounds.height,imageBounds.x,
						imageBounds.y,imageBounds.x + imageBounds.width,imageBounds.y
								+ imageBounds.height,null);
			}
			else
			{
				paintImage(createG2D(g));
			}
		}
	}
	
	
	/**
	 * TODO
	 * 
	 * @param g
	 */
	protected abstract void paintImage(Graphics2D g);
	
	
	/**
	 * TODO
	 * 
	 * @param g
	 * @return
	 */
	protected Graphics2D createG2D(Graphics g)
	{
		return GraphicUtils.create2D(g);
	}
	
	
	/**
	 * Paints the background of this {@link XdevComponent}.
	 * 
	 * @param g
	 *            {@link Graphics} to paint on
	 * @param x
	 *            the X coordinate for the upper-left corner of the background.
	 * @param y
	 *            the Y coordinate for the upper-left corner of the background.
	 * @param w
	 *            the width for the background.
	 * @param h
	 *            the height for the background.
	 * 
	 * @see #setBackground(Color)
	 * @see #setBackgroundType(int)
	 */
	protected void paintBackground(Graphics g, int x, int y, int w, int h)
	{
		style.paintBackground(createG2D(g),this,x,y,w,h);
	}
	
	
	/**
	 * Draws the texture of this {@link XdevComponent}.
	 * 
	 * @param g
	 *            {@link Graphics} to paint on
	 * @param x
	 *            the X coordinate for the upper-left corner of the texture.
	 * @param y
	 *            the Y coordinate for the upper-left corner of the texture.
	 * @param w
	 *            the width for the texture.
	 * @param h
	 *            the height for the texture.
	 * 
	 * @see #setTexture(XdevImage)
	 * 
	 */
	protected void drawTexture(Graphics g, int x, int y, int w, int h)
	{
		style.drawTexture(g,this,x,y,w,h);
	}
	
	
	/**
	 * Returns the alpha value for this {@link XdevComponent}.
	 * 
	 * @return the alpha value for this {@link XdevComponent}.
	 */
	public int getAlpha()
	{
		return alpha;
	}
	
	
	/**
	 * Sets the alpha value for this {@link XdevComponent}.
	 * 
	 * @param alpha
	 *            the alpha value for this {@link XdevComponent}, 0
	 *            (translucent) to 255 (opaque).
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN, intMin = 0, intMax = 255)
	public void setAlpha(int alpha)
	{
		if(!MathUtils.isInRange(alpha,0,255))
		{
			throw new IllegalArgumentException("alpha value out of range");
		}
		
		this.alpha = alpha;
		forcePaint();
	}
	
	
	/**
	 * Returns the alpha condition for this {@link XdevComponent}.
	 * 
	 * @return the alpha condition for this {@link XdevComponent}.
	 * @see AlphaFilter
	 */
	public int getAlphaCondition()
	{
		return alphaCondition;
	}
	
	
	/**
	 * Sets the alpha condition for this {@link XdevComponent}.
	 * 
	 * @param alphaCondition
	 *            the alpha condition for this {@link XdevComponent}.
	 * 
	 * @see AlphaFilter
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN, owner = "alpha")
	public void setAlphaCondition(int alphaCondition)
	{
		this.alphaCondition = alphaCondition;
		forcePaint();
	}
	
	
	/**
	 * Returns the alpha tolerance for this {@link XdevComponent}.
	 * 
	 * @return the alpha tolerance for this {@link XdevComponent}.
	 */
	public int getAlphaTolerance()
	{
		return alphaTolerance;
	}
	
	
	/**
	 * Sets the alpha tolerance for this {@link XdevComponent}.
	 * 
	 * @param alphaTolerance
	 *            the alpha tolerance for this {@link XdevComponent}, 0 - 255
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN, owner = "alpha", intMin = 0, intMax = 255)
	public void setAlphaTolerance(int alphaTolerance)
	{
		if(!MathUtils.isInRange(alpha,0,255))
		{
			throw new IllegalArgumentException("alpha value out of range");
		}
		
		this.alphaTolerance = alphaTolerance;
		forcePaint();
	}
	
	
	/**
	 * Returns the alpha {@link Color}s for this {@link XdevComponent}.
	 * 
	 * @return the alpha {@link Color}s for this {@link XdevComponent}.
	 * @see AlphaFilter
	 */
	public Color[] getAlphaColors()
	{
		return alphaColors;
	}
	
	
	/**
	 * Sets the alpha {@link Color}s for this {@link XdevComponent}.
	 * 
	 * @param alphaColors
	 *            the alpha {@link Color}s for this {@link XdevComponent}.
	 * @see AlphaFilter
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN, owner = "alpha")
	public void setAlphaColors(Color... alphaColors)
	{
		this.alphaColors = alphaColors;
	}
	
	
	/**
	 * Returns the background type as <code>int</code> for this
	 * {@link XdevComponent}.
	 * 
	 * @return the background type as <code>int</code> for this
	 *         {@link XdevComponent}.
	 * @see XdevStyle
	 */
	public int getBackgroundType()
	{
		return style.getBackgroundType();
	}
	
	
	/**
	 * Sets the background type as <code>int</code> for this
	 * {@link XdevComponent}.
	 * 
	 * @param backgroundType
	 *            the background type as <code>int</code> for this
	 *            {@link XdevComponent}.
	 * @see XdevStyle#NONE
	 * @see XdevStyle#COLOR
	 * @see XdevStyle#GRADIENT
	 * @see XdevStyle#SYSTEM
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setBackgroundType(int backgroundType)
	{
		style.setBackgroundType(backgroundType);
		forcePaint();
	}
	
	
	/**
	 * Returns the gradient {@link Color} one for this {@link XdevComponent}.
	 * 
	 * @return the gradient {@link Color} one for this {@link XdevComponent}.
	 * @see XdevStyle#GRADIENT
	 */
	public Color getGradientColor1()
	{
		return style.getGradientColor1();
	}
	
	
	/**
	 * Sets the gradient {@link Color} one for this {@link XdevComponent}.
	 * 
	 * <p>
	 * Gradient colors are only applied if the background type of this
	 * {@link XdevComponent} is set to {@link XdevStyle#GRADIENT}. You can set
	 * the background type via XDEV IDE or manually with
	 * {@link #setBackgroundType(int)}.
	 * </p>
	 * 
	 * @param gradientColor1
	 *            the gradient {@link Color} one for this {@link XdevComponent}.
	 * 
	 * @see XdevStyle#GRADIENT
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setGradientColor1(Color gradientColor1)
	{
		style.setGradientColor1(gradientColor1);
		forcePaint();
	}
	
	
	/**
	 * Returns the gradient {@link Color} two for this {@link XdevComponent}.
	 * 
	 * @return the gradient {@link Color} two for this {@link XdevComponent}.
	 * @see XdevStyle#GRADIENT
	 */
	public Color getGradientColor2()
	{
		return style.getGradientColor2();
	}
	
	
	/**
	 * Sets the gradient {@link Color} two for this {@link XdevComponent}.
	 * 
	 * <p>
	 * Gradient colors are only applied if the background type of this
	 * {@link XdevComponent} is set to {@link XdevStyle#GRADIENT}. You can set
	 * the background type via XDEV IDE or manually with
	 * {@link #setBackgroundType(int)}.
	 * </p>
	 * 
	 * @param gradientColor2
	 *            the gradient {@link Color} two for this {@link XdevComponent}.
	 * @see XdevStyle#GRADIENT
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setGradientColor2(Color gradientColor2)
	{
		style.setGradientColor2(gradientColor2);
		forcePaint();
	}
	
	
	/**
	 * Returns the gradient align for this {@link XdevComponent}.
	 * 
	 * 
	 * 
	 * @return the gradient align for this {@link XdevComponent}.
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
		return style.getGradientAlign();
	}
	
	
	/**
	 * Sets the gradient align for this {@link XdevComponent}.
	 * <p>
	 * Gradient align is only applied if the background type of this
	 * {@link XdevComponent} is set to {@link XdevStyle#GRADIENT}. You can set
	 * the background type via XDEV IDE or manually with
	 * {@link #setBackgroundType(int)}.
	 * </p>
	 * 
	 * 
	 * @param gradientAlign
	 *            the gradient align for this {@link XdevComponent}.
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
		style.setGradientAlign(gradientAlign);
		forcePaint();
	}
	
	
	/**
	 * Returns the texture path for this {@link XdevComponent}.
	 * 
	 * @return the texture path for this {@link XdevComponent}.
	 */
	public String getTexturePath()
	{
		return style.getTexturePath();
	}
	
	
	/**
	 * Sets the texture path for this {@link XdevComponent}.
	 * 
	 * @param texturePath
	 *            the texture path for this {@link XdevComponent}.
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setTexturePath(String texturePath)
	{
		style.setTexturePath(texturePath);
		forcePaint();
	}
	
	
	/**
	 * Returns the texture for this {@link XdevComponent}.
	 * 
	 * @return the texture for this {@link XdevComponent}.
	 * @see XdevImage
	 */
	public XdevImage getTexture()
	{
		return style.getTexture();
	}
	
	
	/**
	 * Sets the texture for this {@link XdevComponent}.
	 * 
	 * @param image
	 *            the texture for this {@link XdevComponent}.
	 * @see XdevImage
	 */
	public void setTexture(XdevImage image)
	{
		style.setTexture(image);
		forcePaint();
	}
	
	
	/**
	 * Returns the texture option for this {@link XdevComponent}.
	 * 
	 * @return the texture option for this {@link XdevComponent} as
	 *         <code>int</code>.
	 * 
	 * @see XdevStyle#CENTER
	 * @see XdevStyle#STRECHED
	 * @see XdevStyle#TILED
	 */
	public int getTextureOption()
	{
		return style.getTextureOption();
	}
	
	
	/**
	 * Sets the texture option for this {@link XdevComponent}.
	 * 
	 * @param textureOption
	 *            the texture option for this {@link XdevComponent}.
	 * 
	 * @see XdevStyle#CENTER
	 * @see XdevStyle#STRECHED
	 * @see XdevStyle#TILED
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setTextureOption(int textureOption)
	{
		style.setTextureOption(textureOption);
		forcePaint();
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
}
