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


import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Map;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import xdev.db.Operator;
import xdev.lang.NotNull;
import xdev.util.ObjectUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;


/**
 * The standard formatted textfield in XDEV. Based on {@link JTextArea}.
 * 
 * @see ClientProperties
 * @see FormularComponent
 * @see XdevFocusCycleComponent
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevTextArea extends JTextArea implements ClientProperties, TextComponent,
		FormularComponent<XdevTextArea>, XdevFocusCycleComponent
{
	/**
	 * 
	 */
	private static final long									serialVersionUID	= 2915603711086126974L;
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger								log					= LoggerFactory
																							.getLogger(XdevTextArea.class);
	
	private String												savedValue			= "";
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int													tabIndex			= -1;
	
	/**
	 * @since 3.1
	 */
	private FocusGainedBehavior									focusGainedBehavior	= FocusGainedBehavior.SYSTEM_DEFAULT;
	
	/**
	 * @since 4.0
	 */
	private TextChangedBehavior									textChangedBehavior	= TextChangedBehavior.SYSTEM_DEFAULT;
	
	private final TextFormularComponentSupport<XdevTextArea>	support				= new TextFormularComponentSupport(
																							this);
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevTextArea}.
	 * 
	 */
	public XdevTextArea()
	{
		this("");
	}
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevTextArea}.
	 * 
	 * @param text
	 *            the text to be displayed, <code>null</code> if none
	 * 
	 */
	public XdevTextArea(String text)
	{
		this(text,10000);
	}
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevTextArea}.
	 * 
	 * 
	 * @param maxSigns
	 *            a <code>int</code> to determine the max signs of the
	 *            {@link MaxSignDocument}
	 * 
	 * @throws IllegalArgumentException
	 *             if the <code>maxSigns</code> is <= 0
	 */
	public XdevTextArea(int maxSigns) throws IllegalArgumentException
	{
		this("",maxSigns);
	}
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevTextArea}.
	 * 
	 * @param text
	 *            the text to be displayed, <code>null</code> if none
	 * 
	 * @param maxSigns
	 *            a <code>int</code> to determine the max signs of the
	 *            {@link MaxSignDocument}
	 * 
	 * @throws IllegalArgumentException
	 *             if the <code>maxSigns</code> is <= 0
	 */
	public XdevTextArea(String text, int maxSigns) throws IllegalArgumentException
	{
		super();
		
		setWrapStyleWord(true);
		setLineWrap(true);
		
		setDocument(new MaxSignDocument(maxSigns));
		setText(text);
		
		setColumns(20);
		setRows(4);
		
		addFocusListener(new FocusListener()
		{
			public void focusGained(FocusEvent e)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						focusGainedBehavior.focusGained(XdevTextArea.this);
					}
				});
			}
			
			
			public void focusLost(FocusEvent e)
			{
				select(0,0);
			}
		});
	}
	
	
	/**
	 * Returns the {@link FocusGainedBehavior} of this TextComponent.
	 * 
	 * @return the {@link FocusGainedBehavior} of this TextComponent
	 * 
	 * @since 3.1
	 */
	public FocusGainedBehavior getFocusGainedBehavior()
	{
		return focusGainedBehavior;
	}
	
	
	/**
	 * Sets the new {@link FocusGainedBehavior} for this TextComponent.
	 * 
	 * @param focusGainedBehavior
	 *            the new {@link FocusGainedBehavior}
	 * @throws {@link IllegalArgumentException} if
	 *         <code>focusGainedBehavior</code> is <code>null</code>
	 * 
	 * @since 3.1
	 */
	public void setFocusGainedBehavior(@NotNull FocusGainedBehavior focusGainedBehavior)
	{
		if(focusGainedBehavior == null)
		{
			throw new IllegalArgumentException("focusGainedBehavior cannot be null");
		}
		else if(focusGainedBehavior != this.focusGainedBehavior)
		{
			FocusGainedBehavior old = this.focusGainedBehavior;
			this.focusGainedBehavior = focusGainedBehavior;
			firePropertyChange(FocusGainedBehavior.PROPERTY_NAME,old,focusGainedBehavior);
		}
	}
	
	
	/**
	 * Return the {@link TextChangedBehavior} of this TextComponent
	 * 
	 * @return the {@link TextChangedBehavior} of this TextComponent
	 * 
	 * @since 4.0
	 */
	public TextChangedBehavior getTextChangedBehavior()
	{
		return textChangedBehavior;
	}
	
	
	/**
	 * Sets the new {@link TextChangedBehavior} for this TextComponent.
	 * 
	 * @param textChangedBehavior
	 *            the new {@link TextChangedBehavior}
	 * @throws {@link IllegalArgumentException} if
	 *         <code>textChangedBehavior</code> is <code>null</code>
	 * 
	 * @since 4.0
	 */
	public void setTextChangedBehavior(@NotNull TextChangedBehavior textChangedBehavior)
	{
		if(textChangedBehavior == null)
		{
			throw new IllegalArgumentException("textChangedBehavior cannot be null");
		}
		else if(textChangedBehavior != this.textChangedBehavior)
		{
			TextChangedBehavior old = this.textChangedBehavior;
			this.textChangedBehavior = textChangedBehavior;
			firePropertyChange(TextChangedBehavior.PROPERTY_NAME,old,textChangedBehavior);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see UIUtils#scrollToStart(javax.swing.JComponent)
	 */
	@Override
	public void setText(String t)
	{
		super.setText(t);
		
		textChangedBehavior.textChanged(this);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize()
	{
		// Hack because JTextArea doesn't honor the set preferred size
		if(isPreferredSizeSet())
		{
			try
			{
				return UIUtils.getPrefSizeFieldValue(this);
			}
			catch(Exception e)
			{
				// Shouldn't happen
				log.error(e);
			}
		}
		
		return super.getPreferredSize();
	}
	
	
	/**
	 * Registers the given {@link DocumentListener} to begin receiving
	 * notifications when changes are made to the document.
	 * 
	 * @param listener
	 *            the {@link DocumentListener} to register
	 * @see #removeDocumentListener(DocumentListener)
	 */
	public void addDocumentListener(DocumentListener listener)
	{
		getDocument().addDocumentListener(listener);
	}
	
	
	/**
	 * Unregisters the given {@link DocumentListener} from the notification list
	 * so it will no longer receive change updates.
	 * 
	 * @param listener
	 *            the observer to register
	 * @see #addDocumentListener(DocumentListener)
	 */
	public void removeDocumentListener(DocumentListener listener)
	{
		getDocument().removeDocumentListener(listener);
	}
	
	
	/**
	 * {@inheritDoc}
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
	 * Returns the max sign count of this {@link XdevTextArea}.
	 * 
	 * 
	 * @return the max sign count of this {@link XdevTextArea}, -1 if the
	 *         {@link Document} is not an instance of {@link MaxSignDocument}.
	 * 
	 * @see MaxSignDocument
	 */
	public int getMaxSignCount()
	{
		Document doc = getDocument();
		if(doc instanceof MaxSignDocument)
		{
			return ((MaxSignDocument)doc).getMaxSignCount();
		}
		
		return -1;
	}
	
	
	/**
	 * Sets the max sign count of this {@link XdevTextArea}.
	 * 
	 * @param maxSignCount
	 *            the max sign count of this {@link XdevTextArea}
	 */
	public void setMaxSignCount(int maxSignCount)
	{
		Document doc = getDocument();
		if(doc instanceof MaxSignDocument)
		{
			MaxSignDocument msDoc = (MaxSignDocument)doc;
			int oldValue = msDoc.getMaxSignCount();
			if(oldValue != maxSignCount)
			{
				msDoc.setMaxSignCount(maxSignCount);
				firePropertyChange(MaxSignDocument.MAX_SIGNS_PROPERTY,oldValue,maxSignCount);
			}
		}
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
	 */
	@Override
	public Object getFormularValue()
	{
		return getText();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveState()
	{
		savedValue = getText();
	}
	
	
	/**
	 * {@inheritDoc}
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
		return !ObjectUtils.equals(savedValue,getFormularValue());
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void addValueChangeListener(final ValueChangeListener l)
	{
		support.addValueChangeListener(l,this);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMultiSelect()
	{
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
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
	
	
	/**
	 * Returns the selected text contained in this {@link XdevTextArea}. If the
	 * selection is <code>null</code> or the document empty, returns a empty
	 * {@link String}.
	 * 
	 * @return the selected text, or a empty {@link String}
	 * 
	 * @throws IllegalArgumentException
	 *             if the selection doesn't have a valid mapping into the
	 *             document for some reason
	 * 
	 * @see #setText
	 * @see JTextComponent#getSelectedText()
	 */
	@Override
	public String getSelectedText() throws IllegalArgumentException
	{
		String st = super.getSelectedText();
		if(st == null)
		{
			st = "";
		}
		
		return st;
	}
	
	
	/**
	 * Inserts a {@link String} into this {@link XdevTextArea}.
	 * 
	 * @param text
	 *            the {@link String} to insert
	 * 
	 * @throws IllegalArgumentException
	 *             if the caret position is not an valid position in the model
	 * 
	 * @see #getCaretPosition()
	 * @see Document#insertString(int, String, javax.swing.text.AttributeSet)
	 */
	public void insertText(String text) throws IllegalArgumentException
	{
		insert(text,getCaretPosition());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabIndex()
	{
		return tabIndex;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(int tabIndex)
	{
		if(this.tabIndex != tabIndex)
		{
			int oldValue = this.tabIndex;
			this.tabIndex = tabIndex;
			firePropertyChange(TAB_INDEX_PROPERTY,oldValue,tabIndex);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return getText();
	}
}
