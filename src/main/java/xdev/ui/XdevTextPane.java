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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;

import xdev.db.Operator;
import xdev.ui.event.TextChangeAdapter;
import xdev.ui.text.Link;
import xdev.ui.text.XdevDocument;
import xdev.util.ObjectUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;


//TODO javadoc (class description)
@BeanSettings(useXdevCustomizer = true)
public class XdevTextPane extends XdevComponent implements XdevTextComponent,
		FormularComponent<XdevTextPane>
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger							log				= LoggerFactory
																					.getLogger(XdevTextPane.class);
	
	private Image											image;
	private String											textImagePath	= null;
	
	private int												horizontalAlign	= XdevDocument.LEFT;
	private boolean											underline		= false;
	private int												textColumnCount	= 1;
	private int												textColumnGap	= 10;
	private Insets											textInsets		= new Insets(0,0,0,0);
	private Dimension										originalSize	= null;
	private final XdevDocument								document;
	
	private String											savedValue		= "";
	
	private final FormularComponentSupport<XdevTextPane>	support			= new FormularComponentSupport(
																					this);
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevTextPane}.
	 * 
	 * @see XdevDocument
	 */
	public XdevTextPane()
	{
		super();
		
		LinkListener ll = new LinkListener();
		addMouseListener(ll);
		addMouseMotionListener(ll);
		
		document = new XdevDocument(this);
		
		setOpaque(false);
		
		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				document.relayout(XdevDocument.FIT_NONE);
			}
		});
	}
	
	
	/*
	 * Opaque is always false to avoid artifacts
	 */
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpaque()
	{
		return false;
	}
	
	
	@Override
	protected boolean paintComponent()
	{
		return (style != null && style.isOpaque()) || document.length() > 0;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getCpn()
	{
		return this;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void reshape(int x, int y, int width, int height)
	{
		if(originalSize == null)
		{
			originalSize = new Dimension(width,height);
		}
		
		super.reshape(x,y,width,height);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getOriginalSize()
	{
		return originalSize;
	}
	
	
	/**
	 * Sets the new text of this {@link XdevTextPane}.
	 * 
	 * <p>
	 * This is an alias for {@link #setText(String, String)} .
	 * </p>
	 * 
	 * @param str
	 *            the text to be displayed
	 * 
	 * @see #setText(String, String)
	 */
	public void setText(String str)
	{
		setText(str,XdevDocument.CONTENT_TYPE_TEXT_PLAIN);
	}
	
	
	/**
	 * Sets the new text of this {@link XdevTextPane}.
	 * 
	 * @param str
	 *            the text to be displayed
	 * 
	 * @param contentType
	 *            the constants defined in <code>XdevDocument</code>:
	 *            {@link XdevDocument#CONTENT_TYPE_TEXT_PLAIN}
	 *            {@link XdevDocument#CONTENT_TYPE_TEXT_RTF}
	 * 
	 */
	public void setText(String str, String contentType)
	{
		document.setText(str,contentType);
		repaint();
	}
	
	
	/**
	 * <p>
	 * This is an alias for {@link #setTextAndPack(String, String)}.
	 * </p>
	 * 
	 * @param str
	 */
	// TODO javaDoc
	public void setTextAndPack(String str)
	{
		setTextAndPack(str,XdevDocument.CONTENT_TYPE_TEXT_PLAIN);
	}
	
	
	// TODO javaDoc
	public void setTextAndPack(String str, String contentType)
	{
		document.setText(str,contentType,XdevDocument.FIT_STRETCH);
		repaint();
	}
	
	
	/**
	 * Returns the text of this {@link XdevTextPane}.
	 * 
	 * @return the text of this {@link XdevTextPane}.
	 */
	public String getText()
	{
		return document.toString();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevDocument getDocument()
	{
		return document;
	}
	
	
	// TODO javaDoc
	public void setTextImagePath(String textImagePath)
	{
		this.textImagePath = textImagePath;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHorizontalAlign()
	{
		return horizontalAlign;
	}
	
	
	// TODO javaDoc
	public void setHorizontalAlign(int align)
	{
		this.horizontalAlign = align;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public int getTextColumnCount()
	{
		return textColumnCount;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public void setTextColumnCount(int textColumnCount)
	{
		this.textColumnCount = textColumnCount;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public int getTextColumnGap()
	{
		return textColumnGap;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public void setTextColumnGap(int textColumnGap)
	{
		this.textColumnGap = textColumnGap;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getUnderline()
	{
		return underline;
	}
	
	
	public void setUnderline(boolean underline)
	{
		this.underline = underline;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPaintText()
	{
		textImagePath = null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVertical()
	{
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVerticalAlign()
	{
		return -1;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean paintAsImage()
	{
		return textImagePath != null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Insets getTextInsets()
	{
		return textInsets;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTextInsets(Insets i)
	{
		textInsets = i;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Insets getBorderInsets(boolean withTextBorder)
	{
		Insets insets = getBorderInsets();
		if(withTextBorder && textInsets != null)
		{
			insets.top += textInsets.top;
			insets.left += textInsets.left;
			insets.bottom += textInsets.bottom;
			insets.right += textInsets.right;
		}
		
		return insets;
	}
	
	
	@Override
	protected void paintImage(Graphics2D g)
	{
		Dimension d = getSize();
		paintBackground(g,0,0,d.width,d.height);
		drawTexture(g,0,0,d.width,d.height);
		drawText(g);
	}
	
	
	// TODO javaDoc
	public void drawText(Graphics2D g)
	{
		if(textImagePath != null)
		{
			if(image == null)
			{
				try
				{
					image = GraphicUtils.loadImage(textImagePath);
				}
				catch(IOException e)
				{
					log.error(e);
				}
			}
			if(image != null)
			{
				g.drawImage(image,0,0,this);
			}
		}
		else if(document != null)
		{
			document.paint(g);
		}
	}
	
	
	
	class LinkListener extends MouseAdapter
	{
		private Cursor	defaultCursor	= null;
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mouseEntered(MouseEvent e)
		{
			if(defaultCursor == null)
			{
				defaultCursor = getCursor();
			}
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mouseMoved(MouseEvent e)
		{
			if(document != null)
			{
				Link l = document.getSignLinkForPoint(e.getPoint());
				if(l != null && l.type != Link.NONE)
				{
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				else
				{
					setCursor(defaultCursor);
				}
			}
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mousePressed(MouseEvent e)
		{
			if(document != null && SwingUtilities.isLeftMouseButton(e))
			{
				Link link = document.getSignLinkForPoint(e.getPoint());
				if(link != null && link.type != Link.NONE)
				{
					try
					{
						link.execute(XdevTextPane.this);
					}
					catch(IOException ex)
					{
						log.error(ex);
					}
				}
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public String getFormularName()
	{
		return support.getFormularName();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setDataField(String dataField)
	{
		support.setDataField(dataField);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public String getDataField()
	{
		return support.getDataField();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public final void setFormularValue(VirtualTable vt, int col, Object value)
	{
		support.setFormularValue(vt,col,value);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setFormularValue(VirtualTable vt, Map<String, Object> record)
	{
		if(!support.hasDataField())
		{
			return;
		}
		
		setText(support.getFormattedFormularValue(vt,record));
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Object getFormularValue()
	{
		return getText();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void saveState()
	{
		savedValue = getText();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void restoreState()
	{
		setText(savedValue);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public boolean hasStateChanged()
	{
		return !ObjectUtils.equals(savedValue,getText());
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void addValueChangeListener(final ValueChangeListener l)
	{
		document.addChangeListener(new TextChangeAdapter()
		{
			@Override
			public void textChanged(DocumentEvent e)
			{
				l.valueChanged(e);
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public boolean isMultiSelect()
	{
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public boolean verify()
	{
		return support.verify();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void addValidator(Validator validator)
	{
		support.addValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void removeValidator(Validator validator)
	{
		support.removeValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Validator[] getValidators()
	{
		return support.getValidators();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void validateState() throws ValidationException
	{
		support.validateState();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void validateState(Validation validation) throws ValidationException
	{
		support.validateState(validation);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setFilterOperator(Operator filterOperator)
	{
		support.setFilterOperator(filterOperator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Operator getFilterOperator()
	{
		return support.getFilterOperator();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setReadOnly(boolean readOnly)
	{
		support.setReadOnly(readOnly);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public boolean isReadOnly()
	{
		return support.isReadOnly();
	}
}
