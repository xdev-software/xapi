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


import java.util.Map;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xdev.db.Operator;
import xdev.db.locking.LockTimeMonitor;
import xdev.util.Countdown;
import xdev.util.ObjectUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;


/**
 * The standard progress bar in XDEV. Based on {@link JProgressBar}.
 * 
 * @see FormularComponent
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevProgressBar extends JProgressBar implements
		NumberFormularComponent<XdevProgressBar>, LockTimeMonitor
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger							log			= LoggerFactory
																				.getLogger(XdevProgressBar.class);
	
	private int												savedValue	= 0;
	
	private final FormularComponentSupport<XdevProgressBar>	support		= new FormularComponentSupport(
																				this);
	
	
	/**
	 * Creates a new horizontal {@link XdevProgressBar} that displays a border
	 * but no progress string. The initial and minimum values are 0, and the
	 * maximum is 100.
	 * 
	 * @see #setOrientation
	 * @see #setBorderPainted
	 * @see #setStringPainted
	 * @see #setString
	 * @see #setIndeterminate
	 */
	public XdevProgressBar()
	{
		super(new DefaultBoundedRangeModel(50,0,0,100));
	}
	
	
	/**
	 * Creates a new {@link XdevProgressBar} with the specified orientation,
	 * which can be either {@code SwingConstants.VERTICAL} or
	 * {@code SwingConstants.HORIZONTAL}. By default, a border is painted but a
	 * progress string is not. The initial and minimum values are 0, and the
	 * maximum is 100.
	 * 
	 * @param orientation
	 *            the desired orientation of this {@link XdevProgressBar}
	 * 
	 * @throws IllegalArgumentException
	 *             if {@code orient} is an illegal value
	 * 
	 * @see #setOrientation
	 * @see #setBorderPainted
	 * @see #setStringPainted
	 * @see #setString
	 * @see #setIndeterminate
	 */
	public XdevProgressBar(int orientation) throws IllegalArgumentException
	{
		super(orientation);
	}
	
	
	/**
	 * Creates a new horizontal {@link XdevProgressBar} with the specified
	 * minimum and maximum. Sets the initial value of the
	 * {@link XdevProgressBar} to the specified minimum. By default, a border is
	 * painted but a progress string is not.
	 * <p>
	 * The <code>BoundedRangeModel</code> that holds the progress bar's data
	 * handles any issues that may arise from improperly setting the minimum,
	 * initial, and maximum values on the progress bar. See the
	 * {@code BoundedRangeModel} documentation for details.
	 * 
	 * @param min
	 *            the minimum value of this {@link XdevProgressBar}
	 * 
	 * @param max
	 *            the maximum value of this {@link XdevProgressBar}
	 * 
	 * @see BoundedRangeModel
	 * @see #setOrientation
	 * @see #setBorderPainted
	 * @see #setStringPainted
	 * @see #setString
	 * @see #setIndeterminate
	 */
	public XdevProgressBar(int min, int max)
	{
		super(min,max);
	}
	
	
	/**
	 * Creates a new {@link XdevProgressBar} using the specified orientation,
	 * minimum, and maximum. By default, a border is painted but a progress
	 * string is not. Sets the initial value of the {@link XdevProgressBar} to
	 * the specified minimum.
	 * <p>
	 * The <code>BoundedRangeModel</code> that holds the progress bar's data
	 * handles any issues that may arise from improperly setting the minimum,
	 * initial, and maximum values on the progress bar. See the
	 * {@code BoundedRangeModel} documentation for details.
	 * 
	 * @param orientation
	 *            the desired orientation of this {@link XdevProgressBar}
	 * 
	 * @param min
	 *            the minimum value of this {@link XdevProgressBar}
	 * 
	 * @param max
	 *            the maximum value of this {@link XdevProgressBar}
	 * 
	 * @throws IllegalArgumentException
	 *             if {@code orient} is an illegal value
	 * 
	 * @see BoundedRangeModel
	 * @see #setOrientation
	 * @see #setBorderPainted
	 * @see #setStringPainted
	 * @see #setString
	 * @see #setIndeterminate
	 */
	public XdevProgressBar(int orientation, int min, int max) throws IllegalArgumentException
	{
		super(orientation,min,max);
	}
	
	
	/**
	 * Creates a new horizontal {@link XdevProgressBar} that uses the specified
	 * model to hold the progress bar's data. By default, a border is painted
	 * but a progress string is not.
	 * 
	 * @param newModel
	 *            the data model for this {@link XdevProgressBar}
	 * 
	 * @see #setOrientation
	 * @see #setBorderPainted
	 * @see #setStringPainted
	 * @see #setString
	 * @see #setIndeterminate
	 */
	public XdevProgressBar(BoundedRangeModel newModel)
	{
		super(newModel);
	}
	
	
	/**
	 * Sets a new data model ({@link DefaultBoundedRangeModel}) used by the
	 * {@link XdevProgressBar}.
	 * 
	 * @param min
	 *            the {@link XdevProgressBar} minimum
	 * 
	 * @param max
	 *            the {@link XdevProgressBar} maximum
	 * 
	 * @param value
	 *            the {@link XdevProgressBar} current value
	 * 
	 * @see #setModel(BoundedRangeModel)
	 * @see DefaultBoundedRangeModel
	 */
	public void setValues(int min, int max, int value)
	{
		setModel(new DefaultBoundedRangeModel(value,0,min,max));
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
		
		Object value = support.getSingleValue(vt,record);
		if(value != null)
		{
			try
			{
				setValue(Integer.parseInt(value.toString()));
			}
			catch(NumberFormatException e)
			{
				log.error(e);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFormularValue()
	{
		return new Integer(getValue());
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
		addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				l.valueChanged(e);
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
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@NoBeanProperty
	@Override
	public Number getNumber() throws NumberFormatException
	{
		return getValue();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
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
	 * 
	 * @since 3.1
	 */
	@NoBeanProperty
	@Override
	public void setNumber(Number number)
	{
		setValue(number.intValue());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return String.valueOf(getValue());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateContext(Countdown countdown, long remainingTime)
	{
		// TODO check MP impl
		this.setMaximum((int)countdown.getDurationMillis());
		this.setValue((int)remainingTime);
		
		// int intState = (int)(remainingTime / (getDurationMillis() / 100));
		// xdevProgressBar.setMaximum(100);
		// xdevProgressBar.setValue(intState);
	}
}
