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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Format;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.swing.JFormattedTextField;
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
 * The standard formatted textfield in XDEV. Based on
 * {@link JFormattedTextField}.
 * 
 * @see JFormattedTextField
 * @see ClientProperties
 * @see FormattedFormularComponent
 * @see XdevFocusCycleComponent
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevFormattedTextField extends JFormattedTextField implements ClientProperties, TextComponent,
		FormattedFormularComponent<XdevFormattedTextField>,
		NumberFormularComponent<XdevFormattedTextField>, XdevFocusCycleComponent
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger									log					= LoggerFactory
																								.getLogger(XdevFormattedTextField.class);
	
	/**
	 * 
	 */
	private static final long										serialVersionUID	= 2643519059601388480L;
	
	private TextFormat												textFormat;
	
	private Object													savedValue			= null;
	
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int														tabIndex			= -1;
	
	/**
	 * @since 3.1
	 */
	private FocusGainedBehavior										focusGainedBehavior	= FocusGainedBehavior.SYSTEM_DEFAULT;
	
	/**
	 * @since 4.0
	 */
	private TextChangedBehavior										textChangedBehavior	= TextChangedBehavior.SYSTEM_DEFAULT;
	
	private final FormularComponentSupport<XdevFormattedTextField>	support				= new FormularComponentSupport(
																								this);
	
	
	/**
	 * Constructor for creating a new instance of a
	 * {@link XdevFormattedTextField}.
	 * 
	 */
	public XdevFormattedTextField()
	{
		this(TextFormat.getNumberInstance(Locale.getDefault(),null,2,2,true,false));
	}
	
	
	/**
	 * Constructor for creating a new instance of a
	 * {@link XdevFormattedTextField}.
	 * 
	 * @param textFormat
	 *            the {@link TextFormat} to format the display text,
	 *            <code>null</code> if none
	 */
	public XdevFormattedTextField(TextFormat textFormat)
	{
		this("",textFormat);
	}
	
	
	/**
	 * Constructor for creating a new instance of a
	 * {@link XdevFormattedTextField}.
	 * 
	 * @param text
	 *            the text to be displayed, <code>null</code> if none
	 * 
	 * @param textFormat
	 *            the {@link TextFormat} to format the display text,
	 *            <code>null</code> if none
	 */
	public XdevFormattedTextField(String text, TextFormat textFormat)
	{
		this(text,1000,textFormat);
	}
	
	
	/**
	 * Constructor for creating a new instance of a
	 * {@link XdevFormattedTextField}.
	 * 
	 * @param maxSigns
	 *            a <code>int</code> to determine the max signs of the
	 *            {@link MaxSignDocument}
	 * 
	 * @param textFormat
	 *            the {@link TextFormat} to format the display text,
	 *            <code>null</code> if none
	 * 
	 * @throws IllegalArgumentException
	 *             if the <code>maxSigns</code> is <= 0
	 */
	public XdevFormattedTextField(int maxSigns, TextFormat textFormat)
			throws IllegalArgumentException
	{
		this("",maxSigns,textFormat);
	}
	
	
	/**
	 * Constructor for creating a new instance of a
	 * {@link XdevFormattedTextField}.
	 * 
	 * @param text
	 *            the text to be displayed, <code>null</code> if none
	 * 
	 * @param maxSigns
	 *            a <code>int</code> to determine the max signs of the
	 *            {@link MaxSignDocument}
	 * 
	 * @param textFormat
	 *            the {@link TextFormat} to format the display text,
	 *            <code>null</code> if none
	 * 
	 * @throws IllegalArgumentException
	 *             if the <code>maxSigns</code> is <= 0
	 */
	public XdevFormattedTextField(String text, int maxSigns, final TextFormat textFormat)
			throws IllegalArgumentException
	{
		super();
		
		setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		setDocument(new MaxSignDocument(maxSigns));
		setTextFormat(textFormat);
		
		if(text != null && text.length() > 0)
		{
			if(textFormat != null)
			{
				setText(textFormat.format(text));
			}
			else
			{
				setText(text);
			}
		}
		
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
						focusGainedBehavior.focusGained(XdevFormattedTextField.this);
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
	public void setText(String str)
	{
		try
		{
			if(textFormat != null)
			{
				Object o = textFormat.parse(str);
				if(o != null && textFormat.getSuppressZero() && o instanceof Number
						&& ((Number)o).doubleValue() == 0.0)
				{
					super.setText("");
				}
				else
				{
					super.setText(str);
				}
			}
			else
			{
				super.setText(str);
			}
		}
		catch(Exception e)
		{
			super.setText(str);
		}
		
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
	 * Returns the max sign count of this {@link XdevFormattedTextField}.
	 * 
	 * 
	 * @return the max sign count of this {@link XdevFormattedTextField}, -1 if
	 *         the {@link Document} is not an instance of
	 *         {@link MaxSignDocument}.
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
	 * Sets the max sign count of this {@link XdevFormattedTextField}.
	 * 
	 * @param maxSignCount
	 *            the max sign count of this {@link XdevFormattedTextField}
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
		
		String dataField = getDataField();
		dataField = support.getFirstDataField(dataField);
		String columnName = support.getColumnName(dataField);
		Object value = record.get(columnName);
		
		try
		{
			setValue(value);
		}
		catch(Exception e)
		{
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
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFormularValue()
	{
		Object value = getValue();
		
		if("".equals(value) && textFormat != null && textFormat.getType() != TextFormat.PLAIN)
		{
			value = null;
		}
		
		return value;
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
	public void setTextFormat(TextFormat format)
	{
		this.textFormat = format;
		
		if(textFormat != null)
		{
			setFormatterFactory(new AbstractFormatterFactory()
			{
				@Override
				public AbstractFormatter getFormatter(JFormattedTextField tf)
				{
					return new AbstractFormatter()
					{
						@Override
						public Object stringToValue(String str) throws ParseException
						{
							return textFormat.parse(str);
						}
						
						
						@Override
						public String valueToString(Object value) throws ParseException
						{
							return textFormat.format(value);
						}
					};
				}
			});
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commitEdit() throws ParseException
	{
		try
		{
			super.commitEdit();
		}
		catch(ParseException e)
		{
			if(textFormat == null)
			{
				throw e;
			}
			
			switch(textFormat.getType())
			{
				case TextFormat.CURRENCY:
				{
					Format numberFormat = textFormat.createFormat(TextFormat.NUMBER);
					setValue(numberFormat.parseObject(getText()));
				}
				break;
				
				case TextFormat.PERCENT:
				{
					Format numberFormat = textFormat.createFormat(TextFormat.NUMBER);
					Object value = numberFormat.parseObject(getText());
					if(value instanceof Number)
					{
						double d = ((Number)value).doubleValue();
						value = new Double(d / 100d);
					}
					setValue(value);
				}
				break;
				
				default:
				{
					throw e;
				}
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveState()
	{
		savedValue = getValue();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState()
	{
		setValue(savedValue);
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
		addPropertyChangeListener("value",new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				l.valueChanged(evt);
			}
		});
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
	 * Sets the text back to an empty String.<br>
	 * Has the same effect as <code>setValue(null)</code>.
	 */
	public void reset()
	{
		setValue(null);
	}
	
	
	/**
	 * Returns the selected text contained in this
	 * {@link XdevFormattedTextField}. If the selection is <code>null</code> or
	 * the document empty, returns a empty {@link String}.
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
	 * Inserts a {@link String} into this {@link XdevFormattedTextField}.
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
	 * 
	 * Returns the value of this {@link XdevFormattedTextField} as
	 * {@link XdevDate}.
	 * 
	 * @return the value of this {@link XdevFormattedTextField} as
	 *         {@link XdevDate}.
	 * 
	 * @throws DateFormatException
	 *             if no {@link TextFormat} is existing or the value of this
	 *             {@link XdevFormattedTextField} can not be converted.
	 * 
	 * @see #getDate(XdevDate)
	 * @see #getInt()
	 * @see #getDouble()
	 */
	public XdevDate getDate() throws DateFormatException
	{
		Object val = getValue();
		if(val instanceof Date)
		{
			return new XdevDate((Date)val);
		}
		else if(val instanceof Calendar)
		{
			return new XdevDate((Calendar)val);
		}
		
		if(textFormat != null && textFormat.getType() == TextFormat.DATETIME)
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
	 * Returns the value of this {@link XdevFormattedTextField} as
	 * {@link XdevDate}. If the internal value can not be converted into a
	 * {@link XdevDate}, the <code>defaultValue</code> is returned.
	 * 
	 * @param defaultValue
	 *            the default {@link XdevDate}
	 * 
	 * @return the value of this {@link XdevFormattedTextField}. If the internal
	 *         value can not be converted into a {@link XdevDate}, the
	 *         <code>defaultValue</code> is returned.
	 * 
	 * @see #getDate()
	 */
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
	 * Returns the value of this {@link XdevFormattedTextField} as
	 * <code>int</code>.
	 * 
	 * @return the value of this {@link XdevFormattedTextField} as
	 *         <code>int</code>.
	 * 
	 * @throws NumberFormatException
	 *             if no {@link TextFormat} is existing or the value of this
	 *             {@link XdevFormattedTextField} can not be converted.
	 * 
	 * @see #getInt(int)
	 * @see #getNumber()
	 * @see #getDate()
	 * @see #getDouble()
	 */
	public int getInt() throws NumberFormatException
	{
		return getNumber().intValue();
	}
	
	
	/**
	 * Returns the value of this {@link XdevFormattedTextField} as
	 * <code>int</code>. If the internal value can not be converted into a
	 * <code>int</code>, the <code>defaultValue</code> is returned.
	 * 
	 * @param defaultValue
	 *            the default <code>int</code>
	 * 
	 * @return the value of this {@link XdevFormattedTextField}. If the internal
	 *         value can not be converted into a <code>int</code>, the
	 *         <code>defaultValue</code> is returned.
	 * 
	 * @see #getInt()
	 */
	public int getInt(int defaultValue)
	{
		try
		{
			return getInt();
		}
		catch(NumberFormatException e)
		{
			return defaultValue;
		}
	}
	
	
	/**
	 * Returns the value of this {@link XdevFormattedTextField} as
	 * <code>double</code>.
	 * 
	 * @return the value of this {@link XdevFormattedTextField} as
	 *         <code>double</code>
	 * 
	 * @throws NumberFormatException
	 *             if no {@link TextFormat} is existing or the value of this
	 *             {@link XdevFormattedTextField} can not be converted.
	 * 
	 * @see #getDouble(double)
	 * @see #getNumber()
	 * @see #getDate()
	 * @see #getInt()
	 */
	public double getDouble() throws NumberFormatException
	{
		return getNumber().doubleValue();
	}
	
	
	/**
	 * Returns the value of this {@link XdevFormattedTextField} as
	 * <code>double</code>. If the internal value can not be converted into a
	 * <code>double</code>, the <code>defaultValue</code> is returned.
	 * 
	 * @param defaultValue
	 *            the default <code>double</code>
	 * 
	 * @return the value of this {@link XdevFormattedTextField}. If the internal
	 *         value can not be converted into a <code>double</code>, the
	 *         <code>defaultValue</code> is returned.
	 * 
	 * @see #getDouble()
	 */
	public double getDouble(double defaultValue)
	{
		try
		{
			return getDouble();
		}
		catch(NumberFormatException e)
		{
			return defaultValue;
		}
	}
	
	
	/**
	 * Returns the value of this {@link XdevFormattedTextField} as
	 * {@link Number}.
	 * 
	 * @return the value of this {@link XdevFormattedTextField} as
	 *         {@link Number}
	 * 
	 * @throws NumberFormatException
	 *             if no {@link TextFormat} is existing or the value of this
	 *             {@link XdevFormattedTextField} can not be converted.
	 * 
	 * @see #getNumber(Number)
	 * @see #getDouble()
	 * @see #getDate()
	 * @see #getInt()
	 */
	@NoBeanProperty
	@Override
	public Number getNumber() throws NumberFormatException
	{
		Object val = getValue();
		if(val instanceof Number)
		{
			return (Number)val;
		}
		
		if(textFormat != null && textFormat.isNumeric())
		{
			try
			{
				val = textFormat.parse(getText());
				if(val instanceof Number)
				{
					return (Number)val;
				}
			}
			catch(Exception e)
			{
			}
		}
		
		throw new NumberFormatException(getText());
	}
	
	
	/**
	 * Returns the value of this {@link XdevFormattedTextField} as
	 * {@link Number}. If the internal value can not be converted into a
	 * {@link Number}, the <code>defaultValue</code> is returned.
	 * 
	 * @param defaultValue
	 *            the default {@link Number}
	 * 
	 * @return the value of this {@link XdevFormattedTextField}. If the internal
	 *         value can not be converted into a {@link Number}, the
	 *         <code>defaultValue</code> is returned.
	 * 
	 * @see #getNumber()
	 * @see #getDouble()
	 * @see #getDate()
	 * @see #getInt()
	 */
	@Override
	public Number getNumber(Number defaultValue)
	{
		try
		{
			return getNumber();
		}
		catch(NumberFormatException e)
		{
			return defaultValue;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@NoBeanProperty
	@Override
	public void setNumber(Number number)
	{
		setValue(number);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		if(textFormat != null)
		{
			return textFormat.format(getValue());
		}
		else
		{
			return getText();
		}
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
