package xdev.util;

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


import java.text.*;
import java.util.*;

import xdev.lang.Copyable;


/**
 * The class <code>XdevDate</code> represents a specific instant in time, with
 * millisecond precision.
 * 
 * <p>
 * The <code>XdevDate</code> class provides common methods for date / time
 * manipulation, comparison and formatting.
 * </p>
 * 
 * 
 * @author XDEV Software
 * @since 2.0
 */
public class XdevDate extends GregorianCalendar implements Copyable<XdevDate>
{
	/**
	 * Returns a new {@link XdevDate} using the current time in the default time
	 * zone with the default locale.
	 * 
	 * @return a new {@link XdevDate}
	 * 
	 * @see #XdevDate()
	 */
	public static XdevDate now()
	{
		return new XdevDate();
	}
	
	/**
	 * JavaDoc omitted for private field.
	 */
	private DateFormat	format		= null;
	
	/**
	 * JavaDoc omitted for private field.
	 */
	private boolean		adjustMonth	= true;
	

	/**
	 * Constructs a new {@link XdevDate} using the current time in the default
	 * time zone with the default locale.
	 */
	public XdevDate()
	{
		super();
	}
	

	/**
	 * Constructs a new {@link XdevDate} using the time from the given long
	 * value.
	 * 
	 * @param currentTimeMillis
	 *            the time in UTC milliseconds from the epoch.
	 * @see #setTime(Date)
	 * @see #getTimeInMillis()
	 */
	public XdevDate(long currentTimeMillis)
	{
		super();
		setTimeInMillis(currentTimeMillis);
	}
	

	/**
	 * Constructs a new {@link XdevDate} with the given date set in the default
	 * time zone with the default locale.
	 * 
	 * @param year
	 *            the value used to set the year of this date.
	 * @param month
	 *            the value used to set the month of this date. Month value is
	 *            0-based. e.g., 0 for January.
	 * @param day
	 *            the value used to set the day of this date.
	 */
	public XdevDate(int year, int month, int day)
	{
		super(year,month,day);
	}
	

	/**
	 * Constructs a new {@link XdevDate} with the given date set in the default
	 * time zone with the default locale.
	 * 
	 * @param year
	 *            the value used to set the year of this date.
	 * @param month
	 *            the value used to set the month of this date. Month value is
	 *            0-based. e.g., 0 for January.
	 * @param day
	 *            the value used to set the day of this date.
	 * @param hour
	 *            the value used to set the hour of this date.
	 * @param minute
	 *            the value used to set the minute of this date.
	 */
	public XdevDate(int year, int month, int day, int hour, int minute)
	{
		super(year,month,day,hour,minute);
	}
	

	/**
	 * Constructs a new {@link XdevDate} with the given date set in the default
	 * time zone with the default locale.
	 * 
	 * @param year
	 *            the value used to set the year of this date.
	 * @param month
	 *            the value used to set the month of this date. Month value is
	 *            0-based. e.g., 0 for January.
	 * @param day
	 *            the value used to set the day of this date.
	 * @param hour
	 *            the value used to set the hour of this date.
	 * @param minute
	 *            the value used to set the minute of this date.
	 * @param second
	 *            the value used to set the second of this date.
	 * 
	 */
	public XdevDate(int year, int month, int day, int hour, int minute, int second)
	{
		super(year,month,day,hour,minute,second);
	}
	

	/**
	 * Constructs a {@link XdevDate} based on the current time in the default
	 * time zone with the given locale.
	 * 
	 * @param locale
	 *            the given locale.
	 */
	public XdevDate(Locale locale)
	{
		super(locale);
	}
	

	/**
	 * Constructs a {@link XdevDate} based on the current time in the given time
	 * zone with the given locale.
	 * 
	 * @param zone
	 *            the given time zone.
	 * @param locale
	 *            the given locale.
	 */
	public XdevDate(TimeZone zone, Locale locale)
	{
		super(zone,locale);
	}
	

	/**
	 * Constructs a {@link XdevDate} based on the current time in the given time
	 * zone with the default locale.
	 * 
	 * @param zone
	 *            the given time zone.
	 */
	public XdevDate(TimeZone zone)
	{
		super(zone);
	}
	

	/**
	 * Constructs a new {@link XdevDate} using the date and time from the given
	 * {@link Date} <code>d</code>.
	 * 
	 * 
	 * @param d
	 *            the {@link Date} to take time and date from.
	 * 
	 */
	public XdevDate(Date d)
	{
		this(d.getTime());
	}
	

	/**
	 * Constructs a new {@link XdevDate} using the date and time from the given
	 * {@link Calendar} <code>d</code>.
	 * 
	 * @param c
	 *            the {@link Calendar} to take time and date from.
	 * 
	 */
	public XdevDate(Calendar c)
	{
		this(c.getTimeInMillis());
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(int field, int amount)
	{
		/*
		 * Overwritten since 3.1, fix for 12169
		 */

		try
		{
			adjustMonth = false;
			super.add(field,amount);
		}
		finally
		{
			adjustMonth = true;
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void roll(int field, int amount)
	{
		try
		{
			adjustMonth = false;
			super.roll(field,amount);
		}
		finally
		{
			adjustMonth = true;
		}
	}
	

	/**
	 * Returns the year of this date.
	 * <p>
	 * This is a convenience method for <code>get(XdevDate.YEAR)</code>.
	 * 
	 * @return The year of this date
	 */
	
	public int getYear()
	{
		return get(YEAR);
	}
	

	/**
	 * Returns the month of this date.
	 * <p>
	 * This is a convenience method for <code>get(XdevDate.MONTH)</code>.
	 * 
	 * @return The month of this date
	 */
	
	public int getMonth()
	{
		return get(MONTH);
	}
	

	/**
	 * Returns the day of the month of this date.
	 * <p>
	 * This is a convenience method for <code>get(XdevDate.DAY_OF_MONTH)</code>.
	 * 
	 * @return The day of the month of this date
	 */
	
	public int getDay()
	{
		return get(DAY_OF_MONTH);
	}
	

	/**
	 * Returns the hour of this time using the 24-hour clock.
	 * <p>
	 * This is a convenience method for <code>get(XdevDate.HOUR_OF_DAY)</code>.
	 * 
	 * @return The hour of this time
	 */
	
	public int getHour()
	{
		return get(HOUR_OF_DAY);
	}
	

	/**
	 * Returns the minute of this time.
	 * <p>
	 * This is a convenience method for <code>get(XdevDate.MINUTE)</code>.
	 * 
	 * @return The minute of this time
	 */
	
	public int getMinute()
	{
		return get(MINUTE);
	}
	

	/**
	 * Returns the second of this time.
	 * <p>
	 * This is a convenience method for <code>get(XdevDate.SECOND)</code>.
	 * 
	 * @return The second of this time
	 */
	
	public int getSecond()
	{
		return get(SECOND);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int get(int field)
	{
		int value = super.get(field);
		
		if(adjustMonth)
		{
			if(field == MONTH)
			{
				value++;
			}
		}
		
		return value;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(int field, int value)
	{
		if(adjustMonth)
		{
			if(field == MONTH)
			{
				value--;
			}
		}
		
		super.set(field,value);
	}
	

	/**
	 * Indicates whether some other {@link XdevDate} object's date and time is
	 * "equal to" this one.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * XdevDate	christmas	= new XdevDate(2000,12,25,12,00);
	 * XdevDate	newYearsEve	= new XdevDate(2000,12,31,12,00);
	 * 
	 * christmas.equals(newYearsEve) returns false
	 * christmas.equals(christmas)   returns true
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * 
	 * @param other
	 *            the reference object with which to compare.
	 * @return <code>true</code> if this object's date and time is the same as
	 *         the other argument's date; <code>false</code> otherwise.
	 * 
	 * @see #equalsTime(Object)
	 * @see #equalsDate(Object)
	 */
	@Override
	public boolean equals(Object other)
	{
		return super.equals(other);
	}
	

	/**
	 * Indicates whether some other {@link Date} object's date and time is
	 * "equal to" this one.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * XdevDate	christmas	= new XdevDate(2000,12,25,12,00);
	 * XdevDate	newYearsEve	= new XdevDate(2000,12,31,12,00);
	 * 
	 * christmas.equals(newYearsEve) returns false
	 * christmas.equals(christmas)   returns true
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * 
	 * @param other
	 *            the reference object with which to compare.
	 * @return <code>true</code> if this object's date and time is the same as
	 *         the other argument's date; <code>false</code> otherwise.
	 * 
	 * @see #equalsTime(Object)
	 * @see #equalsDate(Object)
	 */
	public boolean equals(Date other)
	{
		if(other == null)
		{
			return false;
		}
		
		return getTimeInMillis() == other.getTime();
	}
	

	/**
	 * Indicates whether some other {@link Calendar} object's date and time is
	 * "equal to" this one.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * XdevDate	christmas	= new XdevDate(2000,12,25,12,00);
	 * XdevDate	newYearsEve	= new XdevDate(2000,12,31,12,00);
	 * 
	 * christmas.equals(newYearsEve) returns false
	 * christmas.equals(christmas)   returns true
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * 
	 * @param other
	 *            the reference object with which to compare.
	 * @return <code>true</code> if this object's date and time is the same as
	 *         the other argument's date; <code>false</code> otherwise.
	 * 
	 * @see #equalsTime(Object)
	 * @see #equalsDate(Object)
	 */
	public boolean equals(Calendar other)
	{
		if(other == this)
		{
			return true;
		}
		
		if(other == null)
		{
			return false;
		}
		
		return getTimeInMillis() == other.getTimeInMillis();
	}
	

	/**
	 * Indicates whether some other {@link Date} or {@link Calendar} object's
	 * date is "equal to" this one.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * XdevDate	christmas	 = new XdevDate(2000,12,25,12,00);
	 * XdevDate	christmasEve = new XdevDate(2000,12,24,18,00);
	 * 
	 * christmas.equalsDate(christmasEve) returns false
	 * christmas.equalsDate(christmas)    returns true
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * 
	 * @param other
	 *            the reference object with which to compare.
	 * @return <code>true</code> if this object's date is the same as the other
	 *         argument's date; <code>false</code> otherwise.
	 * @throws IllegalArgumentException
	 *             if <code>other</code> is not a instance of {@link Calendar}
	 *             or {@link Date}.
	 * 
	 * @see #equalsTime(Object)
	 * @see #equals(Object)
	 */
	@SuppressWarnings("deprecation")
	public boolean equalsDate(Object other) throws IllegalArgumentException
	{
		if(other instanceof Calendar)
		{
			Calendar otherCalendar = (Calendar)other;
			return get(DAY_OF_MONTH) == otherCalendar.get(DAY_OF_MONTH)
					&& get(MONTH) == otherCalendar.get(MONTH)
					&& get(YEAR) == otherCalendar.get(YEAR);
		}
		
		if(other instanceof Date)
		{
			Date otherDate = (Date)other;
			return get(DAY_OF_MONTH) == otherDate.getDate() && get(MONTH) == otherDate.getMonth()
					&& get(YEAR) == otherDate.getYear();
		}
		
		throw new IllegalArgumentException();
	}
	

	/**
	 * Indicates whether some other {@link Date} or {@link Calendar} object's
	 * time is "equal to" this one.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * XdevDate	christmas	 = new XdevDate(2000,12,25,12,00);
	 * XdevDate	christmasEve = new XdevDate(2000,12,24,18,00);
	 * XdevDate	newYearsEve	 = new XdevDate(2000,12,31,12,00);
	 * 
	 * christmas.equalsDate(newYearsEve)  returns true
	 * christmas.equalsDate(christmas)    returns true
	 * christmas.equalsDate(christmasEve) returns false
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param other
	 *            the reference object with which to compare.
	 * @return <code>true</code> if this object's time is the same as the other
	 *         argument's time; <code>false</code> otherwise.
	 * @throws IllegalArgumentException
	 *             if <code>other</code> is not a instance of {@link Calendar}
	 *             or {@link Date}.
	 * @see #equalsDate(Object)
	 * @see #equalsTime(Object)
	 * 
	 */
	@SuppressWarnings("deprecation")
	public boolean equalsTime(Object other) throws IllegalArgumentException
	{
		if(other instanceof Calendar)
		{
			Calendar otherCalendar = (Calendar)other;
			return get(HOUR_OF_DAY) == otherCalendar.get(HOUR_OF_DAY)
					&& get(MINUTE) == otherCalendar.get(MINUTE)
					&& get(SECOND) == otherCalendar.get(SECOND);
		}
		
		if(other instanceof Date)
		{
			Date otherDate = (Date)other;
			return get(HOUR_OF_DAY) == otherDate.getHours()
					&& get(MINUTE) == otherDate.getMinutes()
					&& get(SECOND) == otherDate.getSeconds();
		}
		
		throw new IllegalArgumentException();
	}
	

	/**
	 * This method is a alias for {@link #after(Object)}.
	 * 
	 * 
	 * @param date
	 *            the Object to be compared.
	 * @return <code>true</code> if the time of this {@link XdevDate} is after
	 *         the time represented by when; <code>false</code> otherwise.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>other</code> is not a instance of {@link Calendar}
	 *             or {@link Date}.
	 * 
	 * 
	 * @see #after(Object)
	 * @see #before(Object)
	 * @see #isBefore(Object)
	 * @see #between(Object, Object)
	 * @see #isBetween(Object, Object)
	 */
	public boolean isAfter(Object date) throws IllegalArgumentException
	{
		return after(date);
	}
	

	/**
	 * Returns whether this {@link XdevDate} represents a date/time after the
	 * date/time represented by the specified {@link Object} <code>date</code>.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * XdevDate	christmas	= new XdevDate(2000,12,25,12,00);
	 * XdevDate	newYearsEve	= new XdevDate(2000,12,31,12,00);
	 * 
	 * christmas.after(newYearsEve) returns false
	 * christmas.after(christmas)   returns false
	 * newYearsEve.after(christmas) returns true
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * 
	 * @param date
	 *            the Object to be compared.
	 * @return <code>true</code> if the time of this {@link XdevDate} is after
	 *         the time represented by when; <code>false</code> otherwise.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>other</code> is not a instance of {@link Calendar}
	 *             or {@link Date}.
	 * 
	 * 
	 * @see #before(Object)
	 * @see #isBefore(Object)
	 * @see #between(Object, Object)
	 * @see #isBetween(Object, Object)
	 */
	@Override
	public boolean after(Object date) throws IllegalArgumentException
	{
		return getTimeInMillis() > getTimeInMillis(date);
	}
	

	/**
	 * This method is a alias for {@link #before(Object)}.
	 * 
	 * 
	 * @param date
	 *            the Object to be compared.
	 * @return <code>true</code> if the time of this {@link XdevDate} is before
	 *         the time represented by when; <code>false</code> otherwise.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>other</code> is not a instance of {@link Calendar}
	 *             or {@link Date}.
	 * 
	 * @see #before(Object)
	 * @see #after(Object)
	 * @see #isAfter(Object)
	 * @see #between(Object, Object)
	 * @see #isBetween(Object, Object)
	 */
	public boolean isBefore(Object date) throws IllegalArgumentException
	{
		return before(date);
	}
	

	/**
	 * Returns whether this {@link XdevDate} represents a date/time before the
	 * date/time represented by the specified {@link Object} <code>date</code>.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * XdevDate	christmas	= new XdevDate(2000,12,25,12,00);
	 * XdevDate	newYearsEve	= new XdevDate(2000,12,31,12,00);
	 * 
	 * christmas.before(newYearsEve) returns ture
	 * christmas.before(christmas)   returns false
	 * newYearsEve.before(christmas) returns false
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * 
	 * @param date
	 *            the Object to be compared.
	 * @return <code>true</code> if the time of this {@link XdevDate} is before
	 *         the time represented by when; <code>false</code> otherwise.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>other</code> is not a instance of {@link Calendar}
	 *             or {@link Date}.
	 * 
	 * 
	 * @see #after(Object)
	 * @see #isAfter(Object)
	 * @see #between(Object, Object)
	 * @see #isBetween(Object, Object)
	 */
	@Override
	public boolean before(Object date) throws IllegalArgumentException
	{
		return getTimeInMillis() < getTimeInMillis(date);
	}
	

	/**
	 * 
	 * This method is a alias for {@link #isBetween(Object, Object)}.
	 * 
	 * 
	 * @param startDate
	 *            the Object to be compared.
	 * @param endDate
	 *            the Object to be compared.
	 * @return <code>true</code> if the time of this {@link XdevDate} is before
	 *         the time represented by when; <code>false</code> otherwise.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>startDate</code> or <code>endDate</code> is not a
	 *             instance of {@link Calendar} or {@link Date}.
	 * 
	 * 
	 * @see #after(Object)
	 * @see #isAfter(Object)
	 * @see #between(Object, Object)
	 */
	public boolean between(Object startDate, Object endDate) throws IllegalArgumentException
	{
		return isBetween(startDate,endDate);
	}
	

	/**
	 * 
	 * Returns whether this {@link XdevDate} represents a date/time between the
	 * date/time represented by the specified {@link Object}
	 * <code>startDate</code> and <code>endDate</code>.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * XdevDate	christmasEve = new XdevDate(2000,12,24,18,00);
	 * XdevDate	christmas	= new XdevDate(2000,12,25,12,00);
	 * XdevDate	newYearsEve	= new XdevDate(2000,12,31,12,00);
	 * 
	 * christmas.isBetween(christmasEve, newYearsEve) returns ture
	 * christmasEve.isBetween(christmas, newYearsEve) returns false
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * 
	 * @param startDate
	 *            the Object to be compared.
	 * @param endDate
	 *            the Object to be compared.
	 * @return <code>true</code> if the time of this {@link XdevDate} is before
	 *         the time represented by when; <code>false</code> otherwise.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>startDate</code> or <code>endDate</code> is not a
	 *             instance of {@link Calendar} or {@link Date}.
	 * 
	 * 
	 * @see #after(Object)
	 * @see #isAfter(Object)
	 * @see #between(Object, Object)
	 */
	public boolean isBetween(Object startDate, Object endDate) throws IllegalArgumentException
	{
		long start = getTimeInMillis(startDate);
		long end = getTimeInMillis(endDate);
		long time = getTimeInMillis();
		return time >= start && time <= end;
	}
	

	/**
	 * Returns the time value in milliseconds for the given object
	 * <code>date</code>.
	 * 
	 * @param date
	 *            object to get the milliseconds from
	 * 
	 * @return the time value in milliseconds for the given object
	 *         <code>date</code>.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>other</code> is not a instance of {@link Calendar}
	 *             or {@link Date}.
	 * 
	 * @see #getTime()
	 * @see #setTimeInMillis(long)
	 */
	private long getTimeInMillis(Object date) throws IllegalArgumentException
	{
		if(date instanceof Calendar)
		{
			return ((Calendar)date).getTimeInMillis();
		}
		
		if(date instanceof Date)
		{
			return ((Date)date).getTime();
		}
		
		throw new IllegalArgumentException();
	}
	

	/**
	 * Determines if the year of this {@link XdevDate} is a leap year. Returns
	 * <code>true</code> if this year is a leap year.
	 * 
	 * @return <code>true</code> if the year this {@link XdevDate} is a leap
	 *         year; <code>false</code> otherwise.
	 */
	public boolean isLeapYear()
	{
		return isLeapYear(get(YEAR));
	}
	

	/**
	 * Sets the {@link DateFormat} for this {@link XdevDate}.
	 * 
	 * @param format
	 *            {@link DateFormat} to be set.
	 */
	public void setFormat(DateFormat format)
	{
		this.format = format;
	}
	

	/**
	 * 
	 * Returns the {@link DateFormat} for this {@link XdevDate}.
	 * 
	 * @return {@link DateFormat} of this {@link XdevDate}.
	 * 
	 * 
	 */
	public DateFormat getFormat()
	{
		return format;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		DateFormat df = format != null ? format : DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.MEDIUM);
		return df.format(getTime());
	}
	

	/**
	 * Returns a <code>Date</code> object representing this
	 * <code>XdevDate</code>'s time value (millisecond offset from the <a
	 * href="#Epoch">Epoch</a>").
	 * 
	 * @return a <code>Date</code> representing the time value.
	 * @see #setTime(Date)
	 * @see #getTimeInMillis()
	 */
	public Date toDate()
	{
		return super.getTime();
	}
	

	/**
	 * Returns a String representation of this {@link XdevDate}. Formatted
	 * according to the given <code>pattern</code>.
	 * 
	 * @param pattern
	 *            {@link SimpleDateFormat} pattern to format the String.
	 * @return a a String representation of this {@link XdevDate}.
	 * 
	 * @throws NullPointerException
	 *             if the given pattern is null
	 * @throws IllegalArgumentException
	 *             if the given pattern is invalid
	 * 
	 * @see SimpleDateFormat
	 * 
	 */
	public String format(String pattern) throws NullPointerException, IllegalArgumentException
	{
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(getTime());
	}
	

	/**
	 * Returns this XdevDate's time value in milliseconds.
	 * 
	 * @return the current time as UTC milliseconds from the epoch.
	 * @see #getTime()
	 * @see #setTimeInMillis(long)
	 */
	public long timeStamp()
	{
		return getTimeInMillis();
	}
	

	/**
	 * Computes the duraction between this {@link XdevDate} and
	 * <code>later</code>.
	 * 
	 * @param later
	 *            A future time
	 * @return The duration between this and <code>later</code>
	 */
	public Duration getDurationUntil(Object later)
	{
		long laterMillis = getTimeInMillis(later);
		long thisMillis = getTimeInMillis();
		
		if(laterMillis < thisMillis)
		{
			throw new IllegalArgumentException("'later' is before this XdevDate");
		}
		
		return new Duration(laterMillis - thisMillis);
	}
	

	/**
	 * Computes the duraction between <code>sooner</code> and this
	 * {@link XdevDate}.
	 * 
	 * @param sooner
	 *            A past time
	 * @return The duration between <code>sooner</code> and this
	 */
	public Duration getDurationSince(Object sooner)
	{
		long soonerMillis = getTimeInMillis(sooner);
		long thisMillis = getTimeInMillis();
		
		if(soonerMillis > thisMillis)
		{
			throw new IllegalArgumentException("'sooner' is after this XdevDate");
		}
		
		return new Duration(thisMillis - soonerMillis);
	}
	

	/**
	 * Computes the duraction between <code>date</code> and this
	 * {@link XdevDate}.
	 * <p>
	 * It doesn't matter if <code>date</code> is before <code>this</code> or
	 * vice versa.
	 * 
	 * @param date
	 *            A date
	 * @return The duration between <code>date</code> and this
	 */
	public Duration getDurationBetween(Object date)
	{
		long otherTime = getTimeInMillis(date);
		long thisTime = getTimeInMillis();
		return new Duration(Math.abs(otherTime - thisTime));
	}
	

	/**
	 * Rolls this time to the future.
	 * 
	 * @param duration
	 *            The duration to be added
	 * @return this
	 */
	public XdevDate rollForward(Duration duration)
	{
		setTimeInMillis(getTimeInMillis() + duration.getMilliseconds());
		return this;
	}
	

	/**
	 * Rolls this time to the past.
	 * 
	 * @param duration
	 *            The duration to be removed
	 * @return this
	 */
	public XdevDate rollBack(Duration duration)
	{
		setTimeInMillis(getTimeInMillis() - duration.getMilliseconds());
		return this;
	}
	

	/**
	 * Creates a new {@link XdevDate} whith this moment of time plus the
	 * <code>duration</code>'s time.
	 * 
	 * @param duration
	 *            The duration to be added
	 * @return A new future {@link XdevDate}
	 */
	public XdevDate plus(Duration duration)
	{
		return new XdevDate(getTimeInMillis() + duration.getMilliseconds());
	}
	

	/**
	 * Creates a new {@link XdevDate} whith this moment of time minus the
	 * <code>duration</code>'s time.
	 * 
	 * @param duration
	 *            The duration to be removed
	 * @return A new past {@link XdevDate}
	 */
	public XdevDate minus(Duration duration)
	{
		return new XdevDate(getTimeInMillis() - duration.getMilliseconds());
	}
	

	/**
	 * Creates a (defensive) copy of this {@link XdevDate}.
	 * 
	 * @return a (defensive) copy of this {@link XdevDate}.
	 */
	public XdevDate copy()
	{
		XdevDate d = new XdevDate(getTimeInMillis());
		d.format = format;
		return d;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevDate clone()
	{
		return copy();
	}
}
