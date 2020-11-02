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
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import xdev.db.Operator;
import xdev.lang.NotNull;
import xdev.ui.text.TextFormat;
import xdev.util.DateFormatException;
import xdev.util.ObjectUtils;
import xdev.util.XdevDate;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;


/**
 * The standard date textfield in XDEV. Based on {@link JTextField}.
 * 
 * @see JTextField
 * @see ClientProperties
 * @see FormattedFormularComponent
 * @see XdevFocusCycleComponent
 * @see DatePopupOwner
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevDateTextField extends JTextField implements ClientProperties,
		FormattedFormularComponent<XdevDateTextField>, DateFormularComponent<XdevDateTextField>,
		DatePopupOwner, XdevFocusCycleComponent
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger									log						= LoggerFactory
																									.getLogger(XdevDateTextField.class);
	
	private DatePopup												datePopup;
	private DatePopupCustomizer										datePopupCustomizer;
	private TextFormat												textFormat;
	private XdevDateDropDownButton									dropDownButton;
	private boolean													dropDownButtonVisible	= true;
	
	private String													savedValue				= "";
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int														tabIndex				= -1;
	
	/**
	 * @since 3.1
	 */
	private FocusGainedBehavior										focusGainedBehavior		= FocusGainedBehavior.SYSTEM_DEFAULT;
	
	/**
	 * @since 4.0
	 */
	private TextChangedBehavior										textChangedBehavior		= TextChangedBehavior.SYSTEM_DEFAULT;
	
	private final TextFormularComponentSupport<XdevDateTextField>	support					= new TextFormularComponentSupport(
																									this);
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevDateTextField}.
	 * Initialized with default {@link TextFormat}.
	 */
	public XdevDateTextField()
	{
		this(TextFormat.getDateInstance());
	}
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevDateTextField}.
	 * Initialized with the specified <code>textFormat</code>.
	 * 
	 * @param textFormat
	 *            the {@link TextFormat} to format the display value,
	 *            <code>null</code> if none
	 */
	public XdevDateTextField(TextFormat textFormat)
	{
		this("",textFormat);
	}
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevDateTextField}.
	 * Initialized with the specified <code>text</code>, <code>textFormat</code>
	 * and the default <code>maxSigns</code> of <code>100</code>.
	 * 
	 * @param text
	 *            the text to be displayed, <code>null</code> if none
	 * 
	 * @param textFormat
	 *            the {@link TextFormat} to format the display value,
	 *            <code>null</code> if none
	 * 
	 */
	public XdevDateTextField(String text, TextFormat textFormat)
	{
		this(text,100,textFormat);
	}
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevDateTextField}.
	 * Initialized with the specified <code>maxSigns</code> and
	 * <code>textFormat</code>.
	 * 
	 * 
	 * @param maxSigns
	 *            a <code>int</code> to determine the max signs of the
	 *            {@link MaxSignDocument}
	 * 
	 * @param textFormat
	 *            the {@link TextFormat} to format the display value,
	 *            <code>null</code> if none
	 * 
	 * @throws IllegalArgumentException
	 *             if the <code>maxSigns</code> is <= 0
	 */
	public XdevDateTextField(int maxSigns, TextFormat textFormat) throws IllegalArgumentException
	{
		this("",maxSigns,textFormat);
	}
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevDateTextField}.
	 * Initialized with the specified <code>text</code>, <code>maxSigns</code>
	 * and <code>textFormat</code>.
	 * 
	 * @param text
	 *            the text to be displayed, <code>null</code> if none
	 * 
	 * @param maxSigns
	 *            a <code>int</code> to determine the max signs of the
	 *            {@link MaxSignDocument}
	 * 
	 * @param textFormat
	 *            the {@link TextFormat} to format the display value,
	 *            <code>null</code> if none
	 * 
	 * @throws IllegalArgumentException
	 *             if the <code>maxSigns</code> is <= 0
	 */
	public XdevDateTextField(String text, int maxSigns, TextFormat textFormat)
			throws IllegalArgumentException
	{
		super();
		
		setTextFormat(textFormat);
		
		setDocument(new MaxSignDocument(maxSigns));
		setText(text);
		
		init();
	}
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevDateTextField}.
	 * Initialized with the specified <code>value</code> and
	 * <code>textFormat</code>.
	 * 
	 * @param value
	 *            the initial value
	 * 
	 * @param textFormat
	 *            the {@link TextFormat} to format the display value,
	 *            <code>null</code> if none
	 */
	public XdevDateTextField(XdevDate value, TextFormat textFormat)
	{
		this(value,100,textFormat);
	}
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevDateTextField}.
	 * Initialized with the specified <code>value</code>, <code>maxSigns</code>
	 * and <code>textFormat</code>.
	 * 
	 * @param value
	 *            the initial value
	 * 
	 * @param maxSigns
	 *            a <code>int</code> to determine the max signs of the
	 *            {@link MaxSignDocument}
	 * 
	 * @param textFormat
	 *            the {@link TextFormat} to format the display value,
	 *            <code>null</code> if none
	 * 
	 * @throws IllegalArgumentException
	 *             if the <code>maxSigns</code> is <= 0
	 */
	public XdevDateTextField(XdevDate value, int maxSigns, TextFormat textFormat)
			throws IllegalArgumentException
	{
		super();
		
		setTextFormat(textFormat);
		
		setDocument(new MaxSignDocument(maxSigns));
		if(value != null)
		{
			setText(textFormat.format(value));
		}
		
		init();
	}
	
	
	private void init()
	{
		setColumns(20);
		
		addFocusListener(new FocusListener()
		{
			public void focusGained(FocusEvent e)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						focusGainedBehavior.focusGained(XdevDateTextField.this);
					}
				});
			}
			
			
			public void focusLost(FocusEvent e)
			{
				select(0,0);
			}
		});
		
		dropDownButton = createDropDownButton();
		
		addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE && datePopup != null)
				{
					hideDatePopup();
				}
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
		// Hack because JTextField doesn't honor the set preferred size
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
	 * Creates the drop down button for this text field.
	 * <p>
	 * Overwrite this method if you want to use your own drop down button.
	 * 
	 * @return the drop down button
	 */
	protected XdevDateDropDownButton createDropDownButton()
	{
		XdevDateDropDownButton dropDownButton = new XdevDateDropDownButton(this);
		dropDownButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		dropDownButton.setVisible(isVisible() && dropDownButtonVisible);
		return dropDownButton;
	}
	
	
	/**
	 * Gets the {@link XdevDateDropDownButton} of this {@link XdevDateTextField}
	 * .
	 * 
	 * @return the {@link XdevDateDropDownButton} of this
	 *         {@link XdevDateTextField}
	 * 
	 */
	public XdevDateDropDownButton getDropDownButton()
	{
		return dropDownButton;
	}
	
	
	/**
	 * Shows or hides the drop down button.
	 * 
	 * @param b
	 *            <code>true</code> if the button should be shown,
	 *            <code>false</code> if the button should be hidden
	 */
	public void setDropDownButtonVisible(boolean b)
	{
		dropDownButtonVisible = b;
		if(dropDownButton != null)
		{
			dropDownButton.setVisible(b);
		}
	}
	
	
	/**
	 * Returns <code>true</code> if the drop down button is visible,
	 * <code>false</code> otherwise
	 * 
	 * @return the visibility of the drop down button
	 */
	public boolean isDropDownButtonVisible()
	{
		return dropDownButtonVisible;
	}
	
	
	/**
	 * Creates the panel which contains this {@link XdevDateTextField} and the
	 * drop down button.<br>
	 * This is a convenience methode to build layout constructs.
	 * 
	 * @return a panel containing this {@link XdevDateTextField} and the drop
	 *         down button
	 */
	public XComponent createPanel()
	{
		XComponent pnl = new XComponent(new BorderLayout());
		pnl.add(this,BorderLayout.CENTER);
		pnl.add(dropDownButton,BorderLayout.EAST);
		return pnl;
	}
	
	
	/**
	 * Sets the {@link DatePopupCustomizer} of this {@link XdevDateTextField}.
	 * 
	 * @param datePopupCustomizer
	 *            a {@link DatePopupCustomizer} to be set.
	 * 
	 */
	public void setDatePopupCustomizer(DatePopupCustomizer datePopupCustomizer)
	{
		this.datePopupCustomizer = datePopupCustomizer;
	}
	
	
	/**
	 * Gets the {@link DatePopupCustomizer} of this {@link XdevDateTextField}.
	 * 
	 * @return the {@link DatePopupCustomizer} of this {@link XdevDateTextField}
	 * 
	 */
	@Override
	public DatePopupCustomizer getDatePopupCustomizer()
	{
		return datePopupCustomizer;
	}
	
	
	/**
	 * Registers the given {@link DocumentListener} to begin receiving
	 * notifications when changes are made to the document.
	 * 
	 * @param listener
	 *            the {@link DocumentListener} to register
	 * 
	 * @see Document#addDocumentListener(DocumentListener)
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
	 * Returns the max sign count of this {@link XdevDateTextField}.
	 * 
	 * 
	 * @return the max sign count of this {@link XdevDateTextField}, -1 if the
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
	 * Sets the max sign count of this {@link XdevDateTextField}.
	 * 
	 * @param maxSignCount
	 *            the max sign count of this {@link XdevDateTextField}
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
		
		Object value = null;
		String dataField = getDataField();
		String columnName = null;
		if(dataField != null && dataField.length() > 0)
		{
			dataField = support.getFirstDataField(dataField);
			columnName = support.getColumnName(dataField);
			value = record.get(columnName);
		}
		
		String str = value != null ? value.toString() : "";
		
		if(textFormat != null && textFormat.getType() != TextFormat.PLAIN)
		{
			str = textFormat.format(value);
		}
		else if(vt != null && columnName != null)
		{
			str = vt.formatValue(value,columnName);
		}
		
		setText(str);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFormularValue()
	{
		try
		{
			return getDate();
		}
		catch(DateFormatException e)
		{
			return null;
		}
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
	 * {@inheritDoc}
	 */
	@Override
	public void setCursor(Cursor cursor)
	{
		super.setCursor(cursor);
		
		if(dropDownButton != null)
		{
			dropDownButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible)
	{
		if(!visible && datePopup != null)
		{
			hideDatePopup();
		}
		
		if(dropDownButton != null)
		{
			dropDownButton.setVisible(visible);
		}
		
		super.setVisible(visible);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		if(!enabled && datePopup != null)
		{
			hideDatePopup();
		}
		
		if(dropDownButton != null)
		{
			dropDownButton.setEnabled(enabled);
		}
		
		super.setEnabled(enabled);
	}
	
	
	/**
	 * Shows or hides the date popup.
	 * 
	 * @param visible
	 *            <code>true</code> if the popup should be shown,
	 *            <code>false</code> if the popup should be hidden
	 */
	@NoBeanProperty
	public void setDatePopupVisible(boolean visible)
	{
		if(visible)
		{
			requestFocus();
			
			datePopup = DatePopup.getInstance(this);
			datePopup.showPopup(this);
		}
		else
		{
			hideDatePopup();
		}
	}
	
	
	/**
	 * @return if the date popup is currently showing
	 * 
	 * @since 4.0
	 */
	public boolean isDatePopupVisible()
	{
		return datePopup != null && datePopup.isVisible();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextFormat getTextFormat()
	{
		return textFormat;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTextFormat(TextFormat textFormat)
	{
		this.textFormat = textFormat;
	}
	
	
	/**
	 * Get this {@link XdevDateTextField}.
	 * 
	 * @return this {@link XdevDateTextField} as {@link Component}
	 * 
	 */
	@Override
	public Component getComponentForDatePopup()
	{
		return dropDownButton;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void hideDatePopup()
	{
		datePopup.hidePopup();
		datePopup = null;
	}
	
	
	/**
	 * Returns the selected text contained in this {@link XdevDateTextField}. If
	 * the selection is <code>null</code> or the document empty, returns a empty
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
	 * Inserts a {@link String} into this {@link XdevDateTextField}.
	 * 
	 * @param text
	 *            the {@link String} to insert
	 * 
	 * @see Document#insertString(int, String, javax.swing.text.AttributeSet)
	 */
	public void insertText(String text)
	{
		try
		{
			getDocument().insertString(getCaretPosition(),text,null);
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@NoBeanProperty
	@Override
	public XdevDate getDate() throws DateFormatException
	{
		if(textFormat != null)
		{
			try
			{
				return new XdevDate(textFormat.parseDate(getText()));
			}
			catch(Exception e)
			{
			}
		}
		
		throw new DateFormatException(getText());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevDate getDate(XdevDate defaultValue)
	{
		try
		{
			return getDate();
		}
		catch(DateFormatException e)
		{
			return defaultValue;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@NoBeanProperty
	@Override
	public void setDate(XdevDate date)
	{
		setValue(date);
	}
	
	
	/**
	 * Set the <code>value</code> in this {@link XdevDateTextField} as text. If
	 * a {@link TextFormat} exist, the <code>value</code> is formatted.
	 * 
	 * @param value
	 *            the display value
	 * 
	 * @see #setText(String)
	 */
	public void setValue(Object value)
	{
		if(textFormat != null)
		{
			setText(textFormat.format(value));
		}
		else
		{
			setText(String.valueOf(value));
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() throws NullPointerException
	{
		return getText();
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
}
