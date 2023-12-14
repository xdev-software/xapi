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


import java.awt.Container;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import xdev.db.Operator;
import xdev.db.locking.LockTimeMonitor;
import xdev.ui.locking.LockCountMonitorDownUtils;
import xdev.util.Countdown;
import xdev.util.ObjectUtils;
import xdev.vt.VirtualTable;


/**
 * The standard label in XDEV. Based on {@link JLabel}.
 * 
 * @see JLabel
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevLabel extends JLabel implements TextComponent, FormularComponent<XdevLabel>, LockTimeMonitor
{
	/**
	 * 
	 */
	private static final long							serialVersionUID	= 7702329628022922856L;
	
	private String										savedValue			= "";
	
	private final FormularComponentSupport<XdevLabel>	support				= new FormularComponentSupport(
																					this);
	
	
	/**
	 * Creates a {@link XdevLabel} instance with no image and with an empty
	 * string for the title. The {@link XdevLabel} is centered vertically in its
	 * display area. The label's contents, once set, will be displayed on the
	 * leading edge of the label's display area.
	 */
	public XdevLabel()
	{
		super();
	}
	
	
	/**
	 * Creates a {@link XdevLabel} instance with the specified text. The
	 * {@link XdevLabel} is aligned against the leading edge of its display
	 * area, and centered vertically.
	 * 
	 * @param text
	 *            The text to be displayed by the label.
	 */
	public XdevLabel(String text)
	{
		super(text);
	}
	
	
	/**
	 * Creates a {@link XdevLabel} instance with the specified text and
	 * horizontal alignment. The {@link XdevLabel} is centered vertically in its
	 * display area.
	 * 
	 * @param text
	 *            The text to be displayed by the label.
	 * @param horizontalAlignment
	 *            One of the following constants defined in
	 *            <code>SwingConstants</code>: <code>LEFT</code>,
	 *            <code>CENTER</code>, <code>RIGHT</code>, <code>LEADING</code>
	 *            or <code>TRAILING</code>.
	 */
	public XdevLabel(String text, int horizontalAlignment)
	{
		super(text,horizontalAlignment);
	}
	
	
	/**
	 * Creates a {@link XdevLabel} instance with the specified text and icon.
	 * The {@link XdevLabel} is aligned against the leading edge of its display
	 * area, and centered vertically.
	 * 
	 * @param text
	 *            The text to be displayed by the label.
	 * 
	 * @param icon
	 *            The {@link Icon} to be displayed by the label.
	 */
	public XdevLabel(String text, Icon icon)
	{
		super(text);
		setIcon(icon);
	}
	
	
	/**
	 * Creates a {@link XdevLabel} instance with the specified image. The
	 * {@link XdevLabel} is centered vertically and horizontally in its display
	 * area.
	 * 
	 * @param image
	 *            The image to be displayed by the label.
	 */
	public XdevLabel(Icon image)
	{
		super(image);
	}
	
	
	/**
	 * Creates a {@link XdevLabel} instance with the specified image and
	 * horizontal alignment. The {@link XdevLabel} is centered vertically in its
	 * display area.
	 * 
	 * @param image
	 *            The {@link Icon} to be displayed by the label.
	 * 
	 * @param horizontalAlignment
	 *            One of the following constants defined in
	 *            <code>SwingConstants</code>: <code>LEFT</code>,
	 *            <code>CENTER</code>, <code>RIGHT</code>, <code>LEADING</code>
	 *            or <code>TRAILING</code>.
	 */
	public XdevLabel(Icon image, int horizontalAlignment)
	{
		super(image,horizontalAlignment);
	}
	
	
	/**
	 * Creates a {@link XdevLabel} instance with the specified text, image, and
	 * horizontal alignment. The {@link XdevLabel} is centered vertically in its
	 * display area. The text is on the trailing edge of the image.
	 * 
	 * @param text
	 *            The text to be displayed by the label.
	 * 
	 * @param icon
	 *            The {@link Icon} to be displayed by the label.
	 * 
	 * @param horizontalAlignment
	 *            One of the following constants defined in
	 *            <code>SwingConstants</code>: <code>LEFT</code>,
	 *            <code>CENTER</code>, <code>RIGHT</code>, <code>LEADING</code>
	 *            or <code>TRAILING</code>.
	 */
	public XdevLabel(String text, Icon icon, int horizontalAlignment)
	{
		super(text,icon,horizontalAlignment);
	}
	
	{
		if(!Beans.isDesignTime())
		{
			addHierarchyListener(new HierarchyListener()
			{
				@Override
				public void hierarchyChanged(HierarchyEvent e)
				{
					Container parent = getParent();
					if(parent != null)
					{
						removeHierarchyListener(this);
						if(parent.getLayout() == null)
						{
							setSize(getPreferredSize());
						}
					}
				}
			});
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setText(String text)
	{
		super.setText(text);
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				Container parent = getParent();
				if(parent != null && parent.getLayout() == null)
				{
					setSize(getPreferredSize());
				}
			}
		});
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
		addPropertyChangeListener("text",new PropertyChangeListener()
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return getText();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateContext(Countdown countdown, long remainingTime)
	{
		this.setText(LockCountMonitorDownUtils.getUpdatedText(countdown,remainingTime,
				this.getText()));
	}
}
