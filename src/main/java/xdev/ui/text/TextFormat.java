package xdev.ui.text;

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


import java.awt.Color;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import xdev.db.DataType;
import xdev.lang.Copyable;
import xdev.util.DateFormatException;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTableColumn;


public class TextFormat implements Serializable, Copyable<TextFormat>
{
	private static final long	serialVersionUID	= -4792811575951355754L;

	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log			= LoggerFactory.getLogger(TextFormat.class);
	
	
	public static final int		PLAIN				= 0;
	public static final int		DATETIME			= 1;
	public static final int		NUMBER				= 2;
	public static final int		CURRENCY			= 3;
	public static final int		PERCENT				= 4;
	
	public static final int		USE_DATE_ONLY		= 0;
	public static final int		USE_TIME_ONLY		= 1;
	public static final int		USE_DATE_AND_TIME	= 2;
	
	private int					type;
	private int					dateUse;
	private int					dateStyle;
	private int					timeStyle;
	private int					minFractionDigits;
	private int					maxFractionDigits;
	private String				pattern;
	private Locale				locale;
	private boolean				groupingUsed;
	private boolean				suppressZero		= false;
	private Color				signedColor			= Color.red;
	private transient Format	format				= null;
	

	public TextFormat(VirtualTableColumn col)
	{
		dateUse = USE_DATE_AND_TIME;
		dateStyle = DateFormat.MEDIUM;
		timeStyle = DateFormat.MEDIUM;
		minFractionDigits = 0;
		maxFractionDigits = 0;
		groupingUsed = false;
		locale = Locale.getDefault();
		
		DataType dataType = col.getType();
		if(dataType.isInt())
		{
			type = NUMBER;
		}
		else if(dataType.isDecimal())
		{
			type = NUMBER;
			minFractionDigits = maxFractionDigits = 2;
		}
		else if(dataType.isDate())
		{
			type = DATETIME;
		}
	}
	

	public TextFormat()
	{
		this.type = PLAIN;
		this.locale = Locale.getDefault();
	}
	

	public static TextFormat getPlainInstance()
	{
		return new TextFormat();
	}
	

	public static TextFormat getDateInstance()
	{
		return getDateInstance(DateFormat.DEFAULT);
	}
	

	public static TextFormat getDateInstance(int dateStyle)
	{
		return getDateInstance(Locale.getDefault(),DateFormat.DEFAULT);
	}
	

	public static TextFormat getDateInstance(Locale locale, int dateStyle)
	{
		return getDateInstance(Locale.getDefault(),null,USE_DATE_ONLY,dateStyle,DateFormat.DEFAULT);
	}
	

	public static TextFormat getTimeInstance()
	{
		return getTimeInstance(DateFormat.DEFAULT);
	}
	

	public static TextFormat getTimeInstance(int timeStyle)
	{
		return getTimeInstance(Locale.getDefault(),DateFormat.DEFAULT);
	}
	

	public static TextFormat getTimeInstance(Locale locale, int timeStyle)
	{
		return getDateInstance(Locale.getDefault(),null,USE_TIME_ONLY,DateFormat.DEFAULT,timeStyle);
	}
	

	public static TextFormat getDateTimeInstance()
	{
		return getDateTimeInstance(DateFormat.DEFAULT,DateFormat.DEFAULT);
	}
	

	public static TextFormat getDateTimeInstance(int dateStyle, int timeStyle)
	{
		return getDateTimeInstance(Locale.getDefault(),dateStyle,timeStyle);
	}
	

	public static TextFormat getDateTimeInstance(Locale locale, int dateStyle, int timeStyle)
	{
		return getDateInstance(Locale.getDefault(),null,USE_DATE_AND_TIME,dateStyle,timeStyle);
	}
	

	public static TextFormat getDateInstance(Locale locale, String pattern, int dateUse,
			int dateStyle, int timeStyle)
	{
		return new TextFormat(locale,pattern,dateUse,dateStyle,timeStyle);
	}
	

	public TextFormat(Locale locale, String pattern, int dateUse, int dateStyle, int timeStyle)
	{
		this.type = DATETIME;
		this.locale = locale;
		this.pattern = pattern;
		this.dateUse = dateUse;
		this.dateStyle = dateStyle;
		this.timeStyle = timeStyle;
	}
	

	public static TextFormat getNumberInstance(Locale locale, String pattern,
			int minFractionDigits, int maxFractionDigits, boolean groupingUsed, boolean suppressZero)
	{
		return getNumberInstance(locale,pattern,minFractionDigits,maxFractionDigits,groupingUsed,
				suppressZero,null);
	}
	

	public static TextFormat getNumberInstance(Locale locale, String pattern,
			int minFractionDigits, int maxFractionDigits, boolean groupingUsed,
			boolean suppressZero, Color signedColor)
	{
		return new TextFormat(NUMBER,locale,pattern,minFractionDigits,maxFractionDigits,
				groupingUsed,suppressZero,signedColor);
	}
	

	public static TextFormat getCurrencyInstance(Locale locale, String pattern,
			int minFractionDigits, int maxFractionDigits, boolean groupingUsed, boolean suppressZero)
	{
		return getCurrencyInstance(locale,pattern,minFractionDigits,maxFractionDigits,groupingUsed,
				suppressZero,null);
	}
	

	public static TextFormat getCurrencyInstance(Locale locale, String pattern,
			int minFractionDigits, int maxFractionDigits, boolean groupingUsed,
			boolean suppressZero, Color signedColor)
	{
		return new TextFormat(CURRENCY,locale,pattern,minFractionDigits,maxFractionDigits,
				groupingUsed,suppressZero,signedColor);
	}
	

	public static TextFormat getPercentInstance(Locale locale, String pattern,
			int minFractionDigits, int maxFractionDigits, boolean groupingUsed, boolean suppressZero)
	{
		return getPercentInstance(locale,pattern,minFractionDigits,maxFractionDigits,groupingUsed,
				suppressZero,null);
	}
	

	public static TextFormat getPercentInstance(Locale locale, String pattern,
			int minFractionDigits, int maxFractionDigits, boolean groupingUsed,
			boolean suppressZero, Color signedColor)
	{
		return new TextFormat(PERCENT,locale,pattern,minFractionDigits,maxFractionDigits,
				groupingUsed,suppressZero,signedColor);
	}
	

	public TextFormat(int type, Locale locale, String pattern, int minFractionDigits,
			int maxFractionDigits, boolean groupingUsed, boolean suppressZero)
	{
		this(type,locale,pattern,minFractionDigits,maxFractionDigits,groupingUsed,suppressZero,null);
	}
	

	public TextFormat(int type, Locale locale, String pattern, int minFractionDigits,
			int maxFractionDigits, boolean groupingUsed, boolean suppressZero, Color signedColor)
	{
		this.type = type;
		this.locale = locale;
		this.pattern = pattern;
		this.minFractionDigits = minFractionDigits;
		this.maxFractionDigits = maxFractionDigits;
		this.groupingUsed = groupingUsed;
		this.signedColor = signedColor;
		this.suppressZero = suppressZero;
	}
	

	public Format createFormat(int type)
	{
		Format format = null;
		
		try
		{
			if(type == DATETIME)
			{
				if(pattern != null)
				{
					format = new SimpleDateFormat(pattern);
				}
				else
				{
					switch(dateUse)
					{
						case USE_DATE_AND_TIME:

							format = DateFormat.getDateTimeInstance(dateStyle,timeStyle,locale);
							
						break;
						
						case USE_DATE_ONLY:

							format = DateFormat.getDateInstance(dateStyle,locale);
							
						break;
						
						case USE_TIME_ONLY:

							format = DateFormat.getTimeInstance(timeStyle,locale);
							
						break;
					}
				}
			}
			else if(type != PLAIN)
			{
				if(pattern != null)
				{
					format = new DecimalFormat(pattern);
				}
				else
				{
					switch(type)
					{
						case NUMBER:
						{
							format = NumberFormat.getNumberInstance(locale);
						}
						break;
						
						case CURRENCY:
						{
							Locale locale = this.locale;
							String country = locale.getCountry();
							// Currency locale should specify a country
							// Workaround for issue 10986
							if(country.length() == 0)
							{
								String language = locale.getLanguage();
								if(language.length() > 0)
								{
									if(language.length() > 2)
									{
										language = language.substring(0,2);
									}
									locale = new Locale(language,language.toUpperCase());
								}
							}
							
							format = NumberFormat.getCurrencyInstance(locale);
						}
						break;
						
						case PERCENT:
						{
							format = NumberFormat.getPercentInstance(locale);
						}
						break;
					}
					
					NumberFormat nf = (NumberFormat)format;
					nf.setMinimumFractionDigits(minFractionDigits);
					nf.setMaximumFractionDigits(maxFractionDigits);
					nf.setGroupingUsed(groupingUsed);
				}
			}
		}
		catch(Exception e)
		{
			format = null;
			log.error(e);
		}
		
		return format;
	}
	

	public void setSignedColor(Color signedColor)
	{
		this.signedColor = signedColor;
	}
	

	public boolean getUseSignedColor()
	{
		return signedColor != null;
	}
	

	public Color getSignedColor()
	{
		return signedColor;
	}
	

	public int getType()
	{
		return type;
	}
	

	public void setType(int type)
	{
		this.type = type;
		this.format = null;
	}
	

	public Locale getLocale()
	{
		return locale;
	}
	

	public void setLocale(Locale locale)
	{
		this.locale = locale;
		this.format = null;
	}
	

	public String getPattern()
	{
		return pattern;
	}
	

	public void setPattern(String pattern)
	{
		this.pattern = pattern;
		this.format = null;
	}
	

	public Format getFormat()
	{
		if(format == null)
		{
			format = createFormat(type);
		}
		
		return format;
	}
	

	public boolean getSuppressZero()
	{
		return suppressZero;
	}
	

	public void setSuppressZero(boolean suppressZero)
	{
		this.suppressZero = suppressZero;
		this.format = null;
	}
	

	public int getDateStyle()
	{
		return dateStyle;
	}
	

	public void setDateStyle(int dateStyle)
	{
		this.dateStyle = dateStyle;
		this.format = null;
	}
	

	public int getTimeStyle()
	{
		return timeStyle;
	}
	

	public void setTimeStyle(int timeStyle)
	{
		this.timeStyle = timeStyle;
		this.format = null;
	}
	

	public int getDateUse()
	{
		return dateUse;
	}
	

	public void setDateUse(int dateUse)
	{
		this.dateUse = dateUse;
		this.format = null;
	}
	

	public boolean hasDate()
	{
		return dateUse == USE_DATE_AND_TIME || dateUse == USE_DATE_ONLY;
	}
	

	public boolean hasTime()
	{
		return dateUse == USE_DATE_AND_TIME || dateUse == USE_TIME_ONLY;
	}
	

	public boolean isNumeric()
	{
		return type == NUMBER || type == CURRENCY || type == PERCENT;
	}
	

	public int getMinFractionDigits()
	{
		return minFractionDigits;
	}
	

	public void setMinFractionDigits(int minFractionDigits)
	{
		this.minFractionDigits = minFractionDigits;
		this.format = null;
	}
	

	public int getMaxFractionDigits()
	{
		return maxFractionDigits;
	}
	

	public void setMaxFractionDigits(int maxFractionDigits)
	{
		this.maxFractionDigits = maxFractionDigits;
		this.format = null;
	}
	

	public boolean isGroupingUsed()
	{
		return groupingUsed;
	}
	

	public void setGroupingUsed(boolean groupingUsed)
	{
		this.groupingUsed = groupingUsed;
		this.format = null;
	}
	

	public Date parseDate(String str) throws ParseException, DateFormatException
	{
		Object o = getFormat().parseObject(str);
		if(o instanceof Date)
		{
			return (Date)o;
		}
		
		throw new DateFormatException(str);
	}
	

	public Object parse(String str)
	{
		if(type == USE_DATE_ONLY)
		{
			try
			{
				return parseDate(str);
			}
			catch(Exception e)
			{
			}
		}
		else if(type != PLAIN)
		{
			// try
			// {
			// long l = Long.valueOf(str);
			// if(type == PERCENT)
			// {
			// return (double)l * 0.01;
			// }
			// return l;
			// }
			// catch(Exception e)
			// {
			// try
			// {
			// double d = Double.valueOf(str);
			// if(type == PERCENT)
			// {
			// d *= 0.01;
			// }
			// return d;
			// }
			// catch(Exception e2)
			// {
			try
			{
				return getFormat().parseObject(str);
			}
			catch(Exception e3)
			{
				try
				{
					Object o = NumberFormat.getNumberInstance().parseObject(str);
					if(type == PERCENT && o instanceof Number)
					{
						o = ((Number)o).doubleValue() * 0.01;
					}
					return o;
				}
				catch(ParseException e4)
				{
				}
			}
			// }
			// }
		}
		
		return str;
	}
	

	public String format(Object o)
	{
		if(o == null)
		{
			return "";
		}
		
		Format format = getFormat();
		
		if(format == null)
		{
			return String.valueOf(o);
		}
		
		if(suppressZero && o != null && o instanceof Number && ((Number)o).doubleValue() == 0.0)
		{
			return "";
		}
		
		if(format instanceof NumberFormat)
		{
			try
			{
				if(!(o instanceof Number))
				{
					o = Double.valueOf(o.toString());
				}
				
				if(suppressZero && ((Number)o).doubleValue() == 0.0)
				{
					return "";
				}
			}
			catch(Exception e)
			{
			}
		}
		
		if(format instanceof DateFormat)
		{
			if(o instanceof Calendar)
			{
				o = ((Calendar)o).getTime();
			}
		}
		
		try
		{
			String value = format.format(o);
			
			if(suppressZero && format instanceof NumberFormat)
			{
				try
				{
					if(((Number)format.parseObject(value)).doubleValue() == 0.0)
					{
						return "";
					}
				}
				catch(Exception e)
				{
				}
			}
			
			return value;
		}
		catch(Exception e)
		{
			return String.valueOf(o);
		}
	}
	

	@Override
	public TextFormat clone()
	{
		return new TextFormat(this);
	}
	

	private TextFormat(TextFormat other)
	{
		type = other.type;
		dateUse = other.dateUse;
		dateStyle = other.dateStyle;
		timeStyle = other.timeStyle;
		minFractionDigits = other.minFractionDigits;
		maxFractionDigits = other.maxFractionDigits;
		pattern = other.pattern;
		locale = other.locale;
		groupingUsed = other.groupingUsed;
		suppressZero = other.suppressZero;
		signedColor = other.signedColor;
	}
}
