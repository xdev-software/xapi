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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.UIManager;
import javax.swing.border.Border;

import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


//TODO javadoc (class description)
public class XdevStyle
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log					= LoggerFactory.getLogger(XdevStyle.class);
	
	public final static int			NONE				= 0;
	public final static int			COLOR				= 1;
	public final static int			GRADIENT			= 2;
	public final static int			SYSTEM				= 3;
	
	public final static int			LEFF_RIGHT			= 0;
	public final static int			TOP_BOTTOM			= 1;
	public final static int			TOPLEFT_BOTTOMRIGHT	= 2;
	public final static int			TOPRIGHT_BOTTOMLEFT	= 3;
	public final static int			RECTANGULAR			= 4;
	public final static int			RADIAL				= 5;
	
	public final static int			CENTER				= 0;
	public final static int			STRECHED			= 1;
	public final static int			TILED				= 2;
	
	private Component				component;
	
	private int						backgroundType		= SYSTEM;
	private Color					gradientColor1		= Color.white;
	private Color					gradientColor2		= Color.black;
	private int						gradientAlign		= LEFF_RIGHT;
	
	private String					texturePath			= "";
	private int						textureOption		= CENTER;
	
	private Image					textureSource;
	private Image					textureImage;
	
	private Border					border;
	
	
	public XdevStyle(Component component)
	{
		this.component = component;
	}
	
	
	/**
	 * @return <code>true</code> if this style has something to paint, either a
	 *         background or a texture, <code>false</code> otherwise
	 * @since 3.2.4
	 */
	public boolean isOpaque()
	{
		return backgroundType != NONE || (texturePath != null && texturePath.length() > 0);
	}
	
	
	/**
	 * Returns the background type as <code>int</code> for this
	 * {@link XdevStyle}.
	 * 
	 * @return the background type as <code>int</code> for this
	 *         <code>XdevStyle</code>.
	 * 
	 * @see XdevStyle
	 */
	public int getBackgroundType()
	{
		return backgroundType;
	}
	
	
	/**
	 * Sets the background type as <code>int</code> for this {@link XdevStyle}.
	 * 
	 * @param backgroundType
	 *            the background type as <code>int</code> for this
	 *            <code>XdevStyle</code>.
	 * 
	 * @see XdevStyle#NONE
	 * @see XdevStyle#COLOR
	 * @see XdevStyle#GRADIENT
	 * @see XdevStyle#SYSTEM
	 */
	public void setBackgroundType(int backgroundType)
	{
		this.backgroundType = backgroundType;
	}
	
	
	/**
	 * Returns the gradient {@link Color} one for this {@link XdevStyle}.
	 * 
	 * @return the gradient {@link Color} one for this <code>XdevStyle</code>
	 * 
	 * @see XdevStyle#GRADIENT
	 */
	public Color getGradientColor1()
	{
		return gradientColor1;
	}
	
	
	/**
	 * Sets the gradient {@link Color} one for this {@link XdevStyle}.
	 * 
	 * 
	 * @param gradientColor1
	 *            the gradient {@link Color} one for this <code>XdevStyle</code>
	 *            .
	 * 
	 * @see XdevStyle#GRADIENT
	 */
	public void setGradientColor1(Color gradientColor1)
	{
		this.gradientColor1 = gradientColor1;
	}
	
	
	/**
	 * Returns the gradient {@link Color} two for this {@link XdevStyle}.
	 * 
	 * @return the gradient {@link Color} two for this <code>XdevStyle</code>
	 * 
	 * @see XdevStyle#GRADIENT
	 */
	public Color getGradientColor2()
	{
		return gradientColor2;
	}
	
	
	/**
	 * Sets the gradient {@link Color} two for this {@link XdevStyle}.
	 * 
	 * 
	 * @param gradientColor2
	 *            the gradient {@link Color} two for this <code>XdevStyle</code>
	 *            .
	 * 
	 * @see XdevStyle#GRADIENT
	 */
	public void setGradientColor2(Color gradientColor2)
	{
		this.gradientColor2 = gradientColor2;
	}
	
	
	/**
	 * Returns the gradient align for this {@link XdevStyle}.
	 * 
	 * 
	 * @return the gradient align for this <code>XdevStyle</code>.
	 * 
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
		return gradientAlign;
	}
	
	
	/**
	 * Sets the gradient align for this {@link XdevStyle}.
	 * 
	 * 
	 * @param gradientAlign
	 *            the gradient align for this <code>XdevStyle</code>.
	 * 
	 * 
	 * @see XdevStyle#LEFF_RIGHT
	 * @see XdevStyle#TOP_BOTTOM
	 * @see XdevStyle#TOPLEFT_BOTTOMRIGHT
	 * @see XdevStyle#TOPRIGHT_BOTTOMLEFT
	 * @see XdevStyle#RECTANGULAR
	 * @see XdevStyle#RADIAL
	 * 
	 */
	public void setGradientAlign(int gradientAlign)
	{
		this.gradientAlign = gradientAlign;
	}
	
	
	/**
	 * Returns the texture path for this {@link XdevStyle}.
	 * 
	 * @return the texture path for this <code>XdevStyle</code>
	 */
	public String getTexturePath()
	{
		return texturePath;
	}
	
	
	/**
	 * Sets the texture path for this {@link XdevStyle}.
	 * 
	 * @param texturePath
	 *            the texture path for this <code>XdevStyle</code>
	 */
	public void setTexturePath(String texturePath)
	{
		this.texturePath = texturePath == null ? "" : texturePath;
		flushTextureImage();
		flushTextureSource();
	}
	
	
	/**
	 * Returns the texture for this {@link XdevStyle}.
	 * 
	 * @return the texture for this <code>XdevStyle</code> or <code>null</code>
	 * 
	 * @see XdevImage
	 */
	public XdevImage getTexture()
	{
		if(textureSource == null)
		{
			return null;
		}
		
		return new XdevImage(textureSource);
	}
	
	
	/**
	 * Sets the texture for this {@link XdevStyle}.
	 * 
	 * @param image
	 *            the texture for this <code>XdevStyle</code> or
	 *            <code>null</code>
	 * 
	 * @see XdevImage
	 */
	public void setTexture(XdevImage image)
	{
		flushTextureImage();
		flushTextureSource();
		
		if(image == null)
		{
			texturePath = "";
			textureSource = null;
		}
		else
		{
			texturePath = image.toString();
			textureSource = image.getAWTImage();
		}
	}
	
	
	/**
	 * Returns the texture option for this {@link XdevStyle}.
	 * 
	 * @return the texture option for this <code>XdevStyle</code> as
	 *         <code>int</code>.
	 * 
	 * @see XdevStyle#CENTER
	 * @see XdevStyle#STRECHED
	 * @see XdevStyle#TILED
	 */
	public int getTextureOption()
	{
		return textureOption;
	}
	
	
	/**
	 * Sets the texture option for this {@link XdevStyle}.
	 * 
	 * @param textureOption
	 *            the texture option for this <code>XdevStyle</code>.
	 * 
	 * @see XdevStyle#CENTER
	 * @see XdevStyle#STRECHED
	 * @see XdevStyle#TILED
	 */
	public void setTextureOption(int textureOption)
	{
		this.textureOption = textureOption;
	}
	
	private Rectangle	paintRect	= new Rectangle();
	
	
	public synchronized void paintBackground(Graphics2D g, Component c, int _x, int _y, int _w,
			int _h)
	{
		switch(backgroundType)
		{
			case COLOR:
				g.setPaint(component.getBackground());
				paintRect.setBounds(_x,_y,_w,_h);
				g.fill(paintRect);
			break;
			
			case GRADIENT:
				float rd,
				gd,
				bd,
				len,
				pd;
				int rs = gradientColor1.getRed(),
				gs = gradientColor1.getGreen(),
				bs = gradientColor1.getBlue();
				int re = gradientColor2.getRed(),
				ge = gradientColor2.getGreen(),
				be = gradientColor2.getBlue();
				int x,
				y,
				max;
				
				switch(gradientAlign)
				{
					case LEFF_RIGHT:
						g.setPaint(new GradientPaint(_x,_y,gradientColor1,_x + _w,_y,gradientColor2));
						paintRect.setBounds(_x,_y,_w,_h);
						g.fill(paintRect);
					break;
					case TOP_BOTTOM:
						g.setPaint(new GradientPaint(_x,_y,gradientColor1,_x,_y + _h,gradientColor2));
						paintRect.setBounds(_x,_y,_w,_h);
						g.fill(paintRect);
					break;
					case TOPLEFT_BOTTOMRIGHT:
						len = Math.max(_w,_h);
						rd = ((gradientColor2.getRed() - rs)) / len * 0.5f;
						gd = ((gradientColor2.getGreen() - gs)) / len * 0.5f;
						bd = ((gradientColor2.getBlue() - bs)) / len * 0.5f;
						if(_h >= _w)
						{
							pd = ((float)_w / (float)_h);
							for(int i = 0; i < _h; i++)
							{
								x = Math.round(i * pd);
								g.setColor(new Color(Math.round(rs + i * rd),Math
										.round(gs + i * gd),Math.round(bs + i * bd)));
								drawLine(g,_x,i + _y,_x + x,_y);
								g.setColor(new Color(Math.round(re - i * rd),Math
										.round(ge - i * gd),Math.round(be - i * bd)));
								drawLine(g,_x + _w - x,_y + _h,_x + _w,_y + _h - i);
							}
						}
						else
						{
							pd = ((float)_h / (float)_w);
							for(int i = 0; i < _w; i++)
							{
								y = Math.round(i * pd);
								g.setColor(new Color(Math.round(rs + i * rd),Math
										.round(gs + i * gd),Math.round(bs + i * bd)));
								drawLine(g,i + _x,_y,_x,_y + y);
								g.setColor(new Color(Math.round(re - i * rd),Math
										.round(ge - i * gd),Math.round(be - i * bd)));
								drawLine(g,_x + _w,_y + _h - y,_x + _w - i,_y + _h);
							}
						}
						g.setColor(new Color(
								Math.min(rs,gradientColor2.getRed())
										+ (int)Math
												.round(Math.abs(rs - gradientColor2.getRed()) * 0.5),
								Math.min(gs,gradientColor2.getGreen())
										+ (int)Math.round(Math.abs(gs - gradientColor2.getGreen()) * 0.5),
								Math.min(bs,gradientColor2.getBlue())
										+ (int)Math.round(Math.abs(bs - gradientColor2.getBlue()) * 0.5)));
						g.drawLine(_x,_y + _h,_x + _w,_y);
					break;
					case TOPRIGHT_BOTTOMLEFT:
						len = Math.max(_w,_h);
						rd = ((gradientColor2.getRed() - rs)) / len * 0.5f;
						gd = ((gradientColor2.getGreen() - gs)) / len * 0.5f;
						bd = ((gradientColor2.getBlue() - bs)) / len * 0.5f;
						if(_h >= _w)
						{
							pd = ((float)_w / (float)_h);
							for(int i = 0; i < _h; i++)
							{
								x = Math.round(i * pd);
								g.setColor(new Color(Math.round(rs + i * rd),Math
										.round(gs + i * gd),Math.round(bs + i * bd)));
								drawLine(g,_x + _w - x,_y,_x + _w,i + _y);
								g.setColor(new Color(Math.round(re - i * rd),Math
										.round(ge - i * gd),Math.round(be - i * bd)));
								drawLine(g,_x,_y + _h - i,x + _x,_y + _h);
							}
						}
						else
						{
							pd = ((float)_h / (float)_w);
							for(int i = 0; i < _w; i++)
							{
								y = Math.round(i * pd);
								g.setColor(new Color(Math.round(rs + i * rd),Math
										.round(gs + i * gd),Math.round(bs + i * bd)));
								drawLine(g,_x + _w - i,_y,_x + _w,y + _y);
								g.setColor(new Color(Math.round(re - i * rd),Math
										.round(ge - i * gd),Math.round(be - i * bd)));
								drawLine(g,_x,_y + _h - y,_x + i,_y + _h);
							}
						}
						g.setColor(new Color(
								Math.min(rs,gradientColor2.getRed())
										+ (int)Math
												.round(Math.abs(rs - gradientColor2.getRed()) * 0.5),
								Math.min(gs,gradientColor2.getGreen())
										+ (int)Math.round(Math.abs(gs - gradientColor2.getGreen()) * 0.5),
								Math.min(bs,gradientColor2.getBlue())
										+ (int)Math.round(Math.abs(bs - gradientColor2.getBlue()) * 0.5)));
						g.drawLine(_x,_y,_x + _w,_y + _h);
					break;
					case RECTANGULAR:
						len = Math.max(_w,_h);
						rd = ((gradientColor2.getRed() - rs)) / len * 2f;
						gd = ((gradientColor2.getGreen() - gs)) / len * 2f;
						bd = ((gradientColor2.getBlue() - bs)) / len * 2f;
						if(_h >= _w)
						{
							max = _h / 2;
							pd = ((float)_w / (float)_h);
							for(int i = 0; i < max; i++)
							{
								x = Math.round(i * pd);
								g.setPaint(new Color(Math.round(rs + i * rd),Math
										.round(gs + i * gd),Math.round(bs + i * bd)));
								paintRect.setBounds(_x + x,_y + i,_w - 2 * x,_h - 2 * i);
								g.fill(paintRect);
							}
						}
						else
						{
							max = _w / 2;
							pd = ((float)_h / (float)_w);
							for(int i = 0; i < max; i++)
							{
								y = Math.round(i * pd);
								g.setPaint(new Color(Math.round(rs + i * rd),Math
										.round(gs + i * gd),Math.round(bs + i * bd)));
								paintRect.setBounds(_x + i,_y + y,_w - 2 * i,_h - 2 * y);
								g.fill(paintRect);
							}
						}
					break;
					case RADIAL:
						len = Math.max(_w,_h);
						rd = ((gradientColor2.getRed() - rs)) / len * 2f;
						gd = ((gradientColor2.getGreen() - gs)) / len * 2f;
						bd = ((gradientColor2.getBlue() - bs)) / len * 2f;
						g.setColor(gradientColor1);
						g.fillRect(_x,_y,_w,_h);
						if(_h >= _w)
						{
							max = _h / 2;
							pd = ((float)_w / (float)_h);
							for(int i = 0; i < max; i++)
							{
								x = Math.round(i * pd);
								g.setColor(new Color(Math.round(rs + i * rd),Math
										.round(gs + i * gd),Math.round(bs + i * bd)));
								g.fillOval(_x + x,_y + i,_w - 2 * x,_h - 2 * i);
							}
						}
						else
						{
							max = _w / 2;
							pd = ((float)_h / (float)_w);
							for(int i = 0; i < max; i++)
							{
								y = Math.round(i * pd);
								g.setColor(new Color(Math.round(rs + i * rd),Math
										.round(gs + i * gd),Math.round(bs + i * bd)));
								g.fillOval(_x + i,_y + y,_w - 2 * i,_h - 2 * y);
							}
						}
					break;
				}
			
			break;
			
			case SYSTEM:
				g.setPaint(UIManager.getColor(c instanceof JDesktopPane ? "Desktop.background"
						: "control"));
				paintRect.setBounds(_x,_y,_w,_h);
				g.fill(paintRect);
			break;
		}
	}
	
	private int[]	xPoints	= new int[2], yPoints = new int[2];
	
	
	private synchronized void drawLine(Graphics g, int x1, int y1, int x2, int y2)
	{
		xPoints[0] = x1;
		xPoints[1] = x2;
		yPoints[0] = y1;
		yPoints[1] = y2;
		g.drawPolygon(xPoints,yPoints,2);
	}
	
	
	public void drawTexture(Graphics g, Component c, int _x, int _y, int _w, int _h)
	{
		try
		{
			if(!texturePath.equals(""))
			{
				if(textureSource == null)
				{
					textureSource = GraphicUtils.loadImage(texturePath,c);
				}
				
				if(textureOption == STRECHED)
				{
					if(textureImage == null || textureImage.getWidth(c) != _w
							|| textureImage.getHeight(c) != _h)
					{
						textureImage = textureSource.getScaledInstance(_w,_h,
								GraphicUtils.getImageScaleMode());
						try
						{
							MediaTracker mt = new MediaTracker(c);
							mt.addImage(textureImage,0);
							mt.waitForID(0);
							mt.removeImage(textureImage,0);
						}
						catch(Exception e)
						{
						}
					}
				}
				else
				{
					textureImage = textureSource;
				}
				
				if(textureImage != null)
				{
					switch(textureOption)
					{
						case CENTER:
							int x = (_w - textureImage.getWidth(c)) / 2;
							int y = (_h - textureImage.getHeight(c)) / 2;
							g.drawImage(textureImage,_x + x,_y + y,c);
						break;
						case STRECHED:
							g.drawImage(textureImage,_x,_y,c);
						break;
						case TILED:
							int iw = textureImage.getWidth(c),
							dx = 0;
							int ih = textureImage.getHeight(c),
							dy = 0;
							int xmax = (_w / iw) + (_w % iw > 0 ? 1 : 0),
							dx2 = 0;
							int ymax = (_h / ih) + (_h % ih > 0 ? 1 : 0),
							dy2 = 0;
							for(int xpos = 0; xpos < xmax; xpos++)
							{
								for(int ypos = 0; ypos < ymax; ypos++)
								{
									dx = _x + xpos * iw;
									dy = _y + ypos * ih;
									dx2 = Math.min(iw,_w - (iw * xpos) - _x);
									dy2 = Math.min(ih,_h - (ih * ypos) - _y);
									g.drawImage(textureImage,dx,dy,dx + dx2,dy + dy2,0,0,dx2,dy2,c);
								}
							}
						break;
					}
				}
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable
	{
		flushTextureImage();
		flushTextureSource();
	}
	
	
	public void flushTextureImage()
	{
		if(textureImage != null)
		{
			GraphicUtils.flushImage(textureImage);
			textureImage = null;
		}
	}
	
	
	private void flushTextureSource()
	{
		if(textureSource != null)
		{
			GraphicUtils.flushImage(textureSource);
			textureSource = null;
		}
	}
	
	
	/**
	 * Returns the border of this {@link XdevStyle}.
	 * 
	 * @return the border object for this <code>XdevStyle</code>
	 * 
	 * @see #setBorder(Border)
	 */
	public Border getBorder()
	{
		return border;
	}
	
	
	/**
	 * Sets the border of this {@link XdevStyle}.
	 * 
	 * @param border
	 *            the border to be rendered for this <code>XdevStyle</code>
	 * 
	 * @see #getBorder()
	 */
	public void setBorder(Border border)
	{
		this.border = border;
	}
	
	
	public XdevStyle copy(Component cpn)
	{
		XdevStyle style = new XdevStyle(cpn);
		cpn.setBackground(this.component.getBackground());
		style.backgroundType = backgroundType;
		style.gradientColor1 = gradientColor1;
		style.gradientColor2 = gradientColor2;
		style.gradientAlign = gradientAlign;
		style.texturePath = texturePath;
		style.textureOption = textureOption;
		style.textureSource = textureSource;
		style.textureImage = textureImage;
		style.border = border;
		return style;
	}
}
