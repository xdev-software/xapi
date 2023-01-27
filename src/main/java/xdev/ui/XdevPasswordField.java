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

import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import xdev.db.Operator;
import xdev.lang.NotNull;
import xdev.util.ObjectUtils;
import xdev.util.StringUtils;
import xdev.util.auth.Password;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;


/**
 * The standard password field in XDEV. Based on {@link JPasswordField}.
 * 
 * @see Password
 * @see JPasswordField
 * @see ClientProperties
 * @see XdevFocusCycleComponent
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevPasswordField extends JPasswordField implements ClientProperties, TextComponent,
		FormularComponent<XdevPasswordField>, XdevFocusCycleComponent
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log	= LoggerFactory.getLogger(XdevPasswordField.class);
	
	
	
	public static enum PasswordFormularReturnValueType
	{
		PLAIN_TEXT,
		
		HASH_STRING
	}
	
	/**
	 * 
	 */
	private static final long										serialVersionUID		= 1755265304653124836L;
	
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
	
	private PasswordFormularReturnValueType							formularReturnValueType	= PasswordFormularReturnValueType.PLAIN_TEXT;
	private String													hashAlgorithm			= Password.DEFAULT_ALGORITHM;
	
	private final TextFormularComponentSupport<XdevPasswordField>	support					= new TextFormularComponentSupport(
																									this);
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevPasswordField}.
	 * 
	 */
	public XdevPasswordField()
	{
		this("");
	}
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevPasswordField}.
	 * 
	 * @param text
	 *            the text to be displayed, <code>null</code> if none
	 */
	public XdevPasswordField(String text)
	{
		this(text,1000);
	}
	
	
	/**
	 * Constructor for creating a new instance of a {@link XdevPasswordField}.
	 * 
	 * @param maxSigns
	 *            a <code>int</code> to determine the max signs of the
	 *            {@link MaxSignDocument}
	 */
	public XdevPasswordField(int maxSigns)
	{
		this("",maxSigns);
	}
	
	
	/**
	 * Constructs a new {@link XdevPasswordField} that uses the given text
	 * storage model and the given number to create a {@link MaxSignDocument}.<br>
	 * This is the constructor through which the other constructors feed. The
	 * echo character is set to '*', but may be changed by the current Look and
	 * Feel.
	 * 
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
	public XdevPasswordField(String text, int maxSigns) throws IllegalArgumentException
	{
		super();
		
		setDocument(new MaxSignDocument(maxSigns));
		setText(text);
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
						focusGainedBehavior.focusGained(XdevPasswordField.this);
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
	 * Sets the return value type which is used by {@link #getFormularValue()}.
	 * 
	 * @param formularReturnValueType
	 *            the return type
	 * @see PasswordFormularReturnValueType
	 * @see #setHashAlgorithm(String)
	 */
	@BeanProperty(category = DefaultBeanCategories.DATA)
	public void setFormularReturnValueType(PasswordFormularReturnValueType formularReturnValueType)
	{
		this.formularReturnValueType = formularReturnValueType;
	}
	
	
	/**
	 * Returns the return value type which is used by
	 * {@link #getFormularValue()}.
	 * 
	 * @return the formular return value type for {@link #getFormularValue()}
	 */
	public PasswordFormularReturnValueType getFormularReturnValueType()
	{
		return formularReturnValueType;
	}
	
	
	/**
	 * Sets the used hash algorithm, per default
	 * {@link Password#DEFAULT_ALGORITHM} is used.
	 * 
	 * @param hashAlgorithm
	 *            the new hash algorithm
	 * @see #setFormularReturnValueType(PasswordFormularReturnValueType)
	 * @see PasswordFormularReturnValueType#HASH_STRING
	 * @see #getHash()
	 * @see #getHashString()
	 */
	@BeanProperty(category = DefaultBeanCategories.DATA)
	public void setHashAlgorithm(String hashAlgorithm)
	{
		this.hashAlgorithm = hashAlgorithm;
	}
	
	
	/**
	 * Returns the used hash algorithm.
	 * 
	 * @return the used hash algorithm.
	 * @see PasswordFormularReturnValueType#HASH_STRING
	 * @see #getHash()
	 * @see #getHashString()
	 */
	public String getHashAlgorithm()
	{
		return hashAlgorithm;
	}
	
	
	/**
	 * Returns the entered text as a {@link Password} object which uses the
	 * default algorithm: SHA.
	 * 
	 * @return The entered text as {@link Password}
	 * @see Password
	 * @see #setHashAlgorithm(String)
	 */
	public Password getPasswordObject()
	{
		return getPasswordObject(hashAlgorithm);
	}
	
	
	/**
	 * Returns the entered text as a {@link Password} object which uses the
	 * specified <code>algorithm</code>.
	 * 
	 * @param algorithm
	 *            e.g. SHA, MD5, ...
	 * @return The entered text as {@link Password}
	 * @see Password
	 */
	public Password getPasswordObject(String algorithm)
	{
		return new Password(new String(getPassword()),algorithm);
	}
	
	
	/**
	 * Returns the password's fingerprint.
	 * <p>
	 * The set hash alogrithm is used.
	 * 
	 * @return The password's SHA fingerprint.
	 * @see #getHash(String)
	 * @see Password
	 * @see Password#getHash()
	 * @see #setHashAlgorithm(String)
	 */
	public byte[] getHash()
	{
		return getHash(hashAlgorithm);
	}
	
	
	/**
	 * Returns the password's fingerprint.
	 * 
	 * @param algorithm
	 *            e.g. SHA, MD5, ...
	 * @return the password's fingerprint.
	 * 
	 * @see Password
	 * @see Password#getHash()
	 */
	public byte[] getHash(String algorithm)
	{
		return getPasswordObject(algorithm).getHash();
	}
	
	
	/**
	 * Returns the password's fingerprint as a proper hash string.<br>
	 * The set hash alogrithm is used.
	 * 
	 * @return The password's SHA fingerprint as string.
	 * @see #getHash(String)
	 * @see Password
	 * @see Password#getHashString()
	 * @see #setHashAlgorithm(String)
	 */
	public String getHashString()
	{
		return getHashString(hashAlgorithm);
	}
	
	
	/**
	 * Returns the password's fingerprint as a proper hash string.
	 * 
	 * @param algorithm
	 *            e.g. SHA, MD5, ...
	 * @return the password's fingerprint as string.
	 * 
	 * @see Password
	 * @see Password#getHashString()
	 */
	public String getHashString(String algorithm)
	{
		return getPasswordObject(algorithm).getHashString();
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
	 *            the observer to register
	 * 
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
	 * Returns the max sign count of this {@link XdevPasswordField}.
	 * 
	 * 
	 * @return the max sign count of this {@link XdevPasswordField}, -1 if the
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
	 * Sets the max sign count of this {@link XdevPasswordField}.
	 * 
	 * @param maxSignCount
	 *            the max sign count of this {@link XdevPasswordField}
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
		
		setText(support.getFormattedFormularValue(vt,record));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFormularValue()
	{
		switch(formularReturnValueType)
		{
			case PLAIN_TEXT:
				return new String(getPassword());
				
			case HASH_STRING:
				return getHashString();
		}
		
		return new String(getPassword());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveState()
	{
		savedValue = (String)getFormularValue();
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
	 * Returns the selected text contained in this {@link XdevPasswordField}. If
	 * the selection is <code>null</code> or the document empty, returns a empty
	 * {@link String}.
	 * 
	 * @return the selected text
	 * 
	 * @exception IllegalArgumentException
	 *                if the selection doesn't have a valid mapping into the
	 *                document for some reason
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
	 * Inserts a {@link String} into this {@link XdevPasswordField}.
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
	@Override
	public String toString()
	{
		return StringUtils.create(String.valueOf(getEchoChar()),getDocument().getLength());
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
