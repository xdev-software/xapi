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


import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;


public class XdevScrollBar extends JScrollBar implements AdjustmentListener
{
	public final static int			AS_NEEDED	= 0;
	public final static int			ALWAYS		= 1;
	
	private XdevScrollBarListener	listener;
	private double					pvis;
	private int						max;
	

	public XdevScrollBar(int orientation, int min, int max, double pvis, int scrollAmount,
			XdevScrollBarListener listener)
	{
		super(orientation);
		setValues(min,max,pvis,min);
		setScrollAmount(scrollAmount);
		this.listener = listener;
		addAdjustmentListener(this);
	}
	

	public void refresh()
	{
		setValues(getMinimum(),max,pvis,getValue());
	}
	

	public void setValues(int min, int max, double pvis, int val)
	{
		this.pvis = pvis;
		this.max = max;
		int extend = Math.max(1,(int)Math.round(((getOrientation() == HORIZONTAL ? getWidth()
				: getHeight()))
				* pvis));
		super.setValues(val,extend,min,max + extend);
	}
	

	public void setScrollAmount(int sa)
	{
		setUnitIncrement(sa);
		setBlockIncrement((int)Math.round((max - getMinimum()) * pvis));
	}
	

	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		listener.scrollBarValueChanged();
	}
	

	public int getMax()
	{
		return max;
	}
	

	public int getVal()
	{
		return getValue();
	}
	

	public void setVal(int i)
	{
		setValue(i);
	}
}
