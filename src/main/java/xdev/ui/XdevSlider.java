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
import java.awt.Font;
import java.util.Map;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;

import xdev.db.Operator;
import xdev.util.ObjectUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;


@BeanSettings(useXdevCustomizer = true)
public class XdevSlider extends JSlider implements NumberFormularComponent<XdevSlider>,
		XdevFocusCycleComponent
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger						log			= LoggerFactory
																			.getLogger(XdevSlider.class);
	
	private int											savedValue	= 0;
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int											tabIndex	= -1;
	
	private final FormularComponentSupport<XdevSlider>	support		= new FormularComponentSupport(
																			this);
	
	
	/**
	 * Creates a horizontal <code>XdevSlider</code> with the range
	 * <code>0</code> to <code>100</code> and an initial value of 50.
	 */
	public XdevSlider()
	{
		super();
	}
	
	
	/**
	 * Creates a horizontal <code>XdevSlider</code> using the specified
	 * BoundedRangeModel.
	 * 
	 * @param brm
	 *            the new, {@code non-null} <code>BoundedRangeModel</code> to
	 *            use
	 * 
	 * @see BoundedRangeModel
	 */
	public XdevSlider(BoundedRangeModel brm)
	{
		super(brm);
	}
	
	
	/**
	 * Creates a <code>XdevSlider</code> using the specified orientation with
	 * the range <code>0</code> to <code>100</code> and an initial value of
	 * <code>50</code>. The orientation can be either
	 * <code>SwingConstants.VERTICAL</code> or
	 * <code>SwingConstants.HORIZONTAL</code>.
	 * 
	 * @param orientation
	 *            the orientation of the slider
	 * 
	 * @throws IllegalArgumentException
	 *             if orientation is not one of <code>VERTICAL</code>,
	 *             <code>HORIZONTAL</code>
	 * 
	 * @see #setOrientation
	 */
	public XdevSlider(int orientation) throws IllegalArgumentException
	{
		super(orientation);
	}
	
	
	/**
	 * Creates a horizontal <code>XdevSlider</code> using the specified min and
	 * max with an initial value equal to the average of the min plus max.
	 * <p>
	 * The <code>BoundedRangeModel</code> that holds the slider's data handles
	 * any issues that may arise from improperly setting the minimum and maximum
	 * values on the slider. See the <code>BoundedRangeModel</code>
	 * documentation for details.
	 * 
	 * @param min
	 *            the minimum value of the slider
	 * @param max
	 *            the maximum value of the slider
	 * 
	 * @see BoundedRangeModel
	 * @see #setMinimum
	 * @see #setMaximum
	 */
	public XdevSlider(int min, int max)
	{
		super(min,max);
	}
	
	
	/**
	 * Creates a horizontal <code>XdevSlider</code> using the specified min, max
	 * and value.
	 * <p>
	 * The <code>BoundedRangeModel</code> that holds the slider's data handles
	 * any issues that may arise from improperly setting the minimum, initial,
	 * and maximum values on the slider. See the {@code BoundedRangeModel}
	 * documentation for details.
	 * 
	 * @param min
	 *            the minimum value of the XdevSlider
	 * @param max
	 *            the maximum value of the XdevSlider
	 * @param value
	 *            the initial value of the XdevSlider
	 * 
	 * @see BoundedRangeModel
	 * @see #setMinimum
	 * @see #setMaximum
	 * @see #setValue
	 */
	public XdevSlider(int min, int max, int value)
	{
		super(min,max,value);
	}
	
	
	/**
	 * Creates a <code>XdevSlider</code> with the specified orientation and the
	 * specified minimum, maximum, and initial values. The orientation can be
	 * either <code>SwingConstants.VERTICAL</code> or
	 * <code>SwingConstants.HORIZONTAL</code>.
	 * <p>
	 * The <code>BoundedRangeModel</code> that holds the slider's data handles
	 * any issues that may arise from improperly setting the minimum, initial,
	 * and maximum values on the slider. See the {@code BoundedRangeModel}
	 * documentation for details.
	 * 
	 * @param orientation
	 *            the orientation of the XdevSlider
	 * @param min
	 *            the minimum value of the XdevSlider
	 * @param max
	 *            the maximum value of the XdevSlider
	 * @param value
	 *            the initial value of the XdevSlider
	 * 
	 * @throws IllegalArgumentException
	 *             if orientation is not one of {@code VERTICAL},
	 *             {@code HORIZONTAL}
	 * 
	 * @see BoundedRangeModel
	 * @see #setOrientation
	 * @see #setMinimum
	 * @see #setMaximum
	 * @see #setValue
	 */
	public XdevSlider(int orientation, int min, int max, int value) throws IllegalArgumentException
	{
		super(orientation,min,max,value);
	}
	
	/*
	 * alter default values
	 * 
	 * @since 3.2
	 */
	{
		setMajorTickSpacing(50);
		setMinorTickSpacing(10);
	}
	
	
	/**
	 * Sets the values of the slider.
	 * <p>
	 * This is a convenience method for
	 * <code>setModel(new DefaultBoundedRangeModel(value,0,min,max))</code>
	 * 
	 * @param min
	 *            the new minimum
	 * @param max
	 *            the new maximum
	 * @param value
	 *            the new value
	 */
	public void setValues(int min, int max, int value)
	{
		setModel(new DefaultBoundedRangeModel(value,0,min,max));
	}
	
	
	/**
	 * Creates a label used for the caption in
	 * {@link #setLabelTable(java.util.Dictionary)}
	 * 
	 * @param text
	 *            the caption
	 * @return a new {@link JLabel}
	 * @since 3.2
	 */
	public JLabel createLabel(String text)
	{
		return new LabelUIResource(text);
	}
	
	
	
	/**
	 * Label used for caption, which uses font and foreground of the slider
	 * 
	 * @since 3.2
	 */
	protected class LabelUIResource extends JLabel implements UIResource
	{
		public LabelUIResource(String text)
		{
			super(text,CENTER);
			setName("Slider.label");
		}
		
		
		public Font getFont()
		{
			Font font = super.getFont();
			if(font != null && !(font instanceof UIResource))
			{
				return font;
			}
			return XdevSlider.this.getFont();
		}
		
		
		public Color getForeground()
		{
			Color fg = super.getForeground();
			if(fg != null && !(fg instanceof UIResource))
			{
				return fg;
			}
			if(!(XdevSlider.this.getForeground() instanceof UIResource))
			{
				return XdevSlider.this.getForeground();
			}
			return fg;
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
