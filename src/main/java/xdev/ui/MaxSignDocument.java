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


import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


public class MaxSignDocument extends PlainDocument
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log					= LoggerFactory
																.getLogger(MaxSignDocument.class);
	
	public final static String	MAX_SIGNS_PROPERTY	= "maxSignCount";

	private int					maxSignCount;


	public MaxSignDocument(int maxSignCount)
	{
		if(maxSignCount <= 0)
		{
			throw new IllegalArgumentException("maxSignCount<=0");
		}

		this.maxSignCount = maxSignCount;
	}


	public int getMaxSignCount()
	{
		return maxSignCount;
	}


	public void setMaxSignCount(int maxSignsCount)
	{
		this.maxSignCount = maxSignsCount;
		int len = getLength();
		if(len > maxSignsCount)
		{
			try
			{
				remove(maxSignsCount,len - maxSignsCount);
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}
	}


	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
		int max = maxSignCount - getLength();
		super.insertString(offs,str.substring(0,Math.min(max,str.length())),a);
	}
}
