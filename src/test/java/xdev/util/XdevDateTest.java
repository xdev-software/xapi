
package xdev.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

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


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import xdev.lang.AssertCopyable;


/**
 * Tests for the Class {@link XdevDate}.
 * 
 * @author XDEV Software (FHAE)
 */
// TODO Konstruktoren per Reflection testen
public class XdevDateTest
{
	/**
	 * The XdevDate to test this class. The XdevDate is 2008-12-28
	 */
	public static XdevDate					testDatePast;
	/**
	 * The XdevDate to test this class. The XdevDate is 2130-12-15
	 */
	public static XdevDate					testDateFuture;
	
	public final static int					DAY_OF_WEEK_VALUE			= XdevDate.SUNDAY;
	public final static int					DAY_OF_WEEK_IN_MONTH_VALUE	= 4;
	public final static int					DAY_OF_YEAR_VALUE			= 363;
	
	public final static int					MONTH_PAST_DATE				= 11;
	public final static int					YEAR_PAST_DATE				= 2008;
	public final static int					DAY_PAST_DATE				= 28;
	
	public final static int					MONTH_FUTURE_DATE			= 11;
	public final static int					YEAR_FUTURE_DATE			= 2130;
	public final static int					DAY_FUTURE_DATE				= 15;
	public final static int					HOUR_FUTURE_DATE			= 14;
	public final static int					SECOND_FUTURE_DATE			= 45;
	public final static int					MINUTE_FUTURE_DATE			= 21;
	
	public final static SimpleDateFormat	DATE_FORMAT_FUTURE			= new SimpleDateFormat(
																				"yyyy-MM-DD HH:mm:ss");
	
	
	@Before
	public void init()
	{
		testDatePast = new XdevDate(YEAR_PAST_DATE,MONTH_PAST_DATE,DAY_PAST_DATE);
		testDateFuture = new XdevDate(YEAR_FUTURE_DATE,MONTH_FUTURE_DATE,DAY_FUTURE_DATE,
				HOUR_FUTURE_DATE,MINUTE_FUTURE_DATE,SECOND_FUTURE_DATE);
		testDateFuture.setFormat(DATE_FORMAT_FUTURE);
	}
	
	
	/**
	 * Test for method {@link XdevDate#now()}.
	 */
	@Test
	public void testNow_defaultBehavior()
	{
		final XdevDate date_first = XdevDate.now();
		final XdevDate date_second = XdevDate.now();
		
		// The date must be the same
		assertTrue(date_first.equalsDate(date_second));
	}
	
	
	/**
	 * Test for method {@link XdevDate#after(Object)}.
	 */
	@Test
	public void testAfter_ParamDateIsAfter()
	{
		assertTrue(testDateFuture.after(testDatePast));
	}
	
	
	/**
	 * Test for method {@link XdevDate#after(Object)}.
	 */
	@Test
	public void testAfter_ParamDateNotAfter()
	{
		assertFalse(testDatePast.after(testDateFuture));
	}
	
	
	/**
	 * Test for method {@link XdevDate#after(Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAfter_ParamDateIllegalArgumentException()
	{
		testDatePast.after("no Date");
	}
	
	
	/**
	 * Test for method {@link XdevDate#before(Object)}.
	 */
	@Test
	public void testBefore_ParamDateIsBefore()
	{
		assertTrue(testDatePast.before(testDateFuture));
	}
	
	
	/**
	 * Test for method {@link XdevDate#before(Object)}.
	 */
	@Test
	public void testBefore_ParamDateIsNotBefore()
	{
		assertFalse(testDateFuture.before(testDatePast));
	}
	
	
	/**
	 * Test for method {@link XdevDate#before(Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBefore_ParamDateIllegalArgumentException()
	{
		testDatePast.before("no Date");
	}
	
	
	/**
	 * Test for method {@link XdevDate#between(Object, Object)}.
	 */
	@Test
	public void testBetween_IsBetween()
	{
		final XdevDate now = XdevDate.now();
		assertTrue(now.between(testDatePast,testDateFuture));
	}
	
	
	/**
	 * Test for method {@link XdevDate#between(Object, Object)}.
	 */
	@Test
	public void testBetween_IsNotBetween()
	{
		final XdevDate now = XdevDate.now();
		assertFalse(testDatePast.between(now,testDateFuture));
	}
	
	
	/**
	 * Test for method {@link XdevDate#between(Object, Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBetween_ParamStartDateIllegalArgumentException()
	{
		testDatePast.between(null,testDateFuture);
	}
	
	
	/**
	 * Test for method {@link XdevDate#between(Object, Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBetween_ParamEndDateIllegalArgumentException()
	{
		testDatePast.between(testDateFuture,null);
	}
	
	
	/**
	 * Test for method {@link XdevDate#clone()}.
	 */
	@Test
	public void testClone_defaultBehavior()
	{
		AssertCopyable.assertClone(testDateFuture);
	}
	
	
	/**
	 * Test for method {@link XdevDate#copy()}.
	 */
	@Test
	public void testCopy_defaultBehavior()
	{
		final XdevDate date = testDatePast.copy();
		assertEquals(date,testDatePast);
		assertEquals(date.getFormat(),testDatePast.getFormat());
	}
	
	
	/**
	 * Test for method {@link XdevDate#equals(Object)}.
	 */
	@Test
	public void testEquals_IsEqual()
	{
		final XdevDate dateReference = testDatePast;
		assertTrue(dateReference.equals(testDatePast));
		
		final XdevDate date = testDatePast.copy();
		assertTrue(date.equals(testDatePast));
	}
	
	
	/**
	 * Test for method {@link XdevDate#equals(Object)}.
	 */
	@Test
	public void testEquals_IsNotEqual()
	{
		assertFalse(testDatePast.equals(testDateFuture));
	}
	
	
	/**
	 * Test for method {@link XdevDate#equalsDate(Object)}.
	 */
	@Test
	public void testEqualsDate_IsEqual()
	{
		final XdevDate dateClone = testDatePast.clone();
		
		assertFalse(testDatePast.equalsDate(testDateFuture));
		assertTrue(dateClone.equalsDate(testDatePast));
		assertFalse(testDatePast.equalsDate(XdevDate.now()));
	}
	
	
	/**
	 * Test for method {@link XdevDate#equalsDate(Object)}.
	 */
	@Test
	public void testEqualsDate_IsNotEqual()
	{
		final XdevDate dateClone = testDatePast.clone();
		
		assertFalse(testDatePast.equalsDate(testDateFuture));
		assertTrue(dateClone.equalsDate(testDatePast));
		assertFalse(testDatePast.equalsDate(XdevDate.now()));
	}
	
	
	/**
	 * Test for method {@link XdevDate#equalsDate(Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEqualsDate_ParamOtherIllegalArgumentException()
	{
		testDatePast.equalsDate(new String());
	}
	
	
	/**
	 * Test for method {@link XdevDate#equalsTime(Object)}.
	 */
	@Test
	public void testEqualsTime_SameTime()
	{
		assertTrue(XdevDate.now().equalsTime(XdevDate.now().getTime()));
	}
	
	
	/**
	 * Test for method {@link XdevDate#equalsTime(Object)}.
	 */
	@Test
	public void testEqualsTime_NotSameTime()
	{
		assertFalse(testDatePast.equalsTime(XdevDate.now().getTime()));
	}
	
	
	/**
	 * Test for method {@link XdevDate#equalsTime(Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEqualsTime_ParamOtherIllegalArgumentException()
	{
		testDateFuture.equalsTime(new String());
	}
	
	
	/**
	 * Test for method {@link XdevDate#format(String)}.
	 */
	@Test
	public void testFormat_defaultBehavior()
	{
		final String datePattern = "yyyy.MM.dd";
		final String timePattern = "HH:mm:ss";
		final String point = ".";
		
		final StringBuilder datePatternResult = new StringBuilder();
		datePatternResult.append(YEAR_FUTURE_DATE);
		datePatternResult.append(point);
		datePatternResult.append(MONTH_FUTURE_DATE + 1);
		datePatternResult.append(point);
		datePatternResult.append(DAY_FUTURE_DATE);
		
		final StringBuilder timePatternResult = new StringBuilder();
		timePatternResult.append(HOUR_FUTURE_DATE);
		timePatternResult.append(":");
		timePatternResult.append(MINUTE_FUTURE_DATE);
		timePatternResult.append(":");
		timePatternResult.append(SECOND_FUTURE_DATE);
		
		assertEquals(datePatternResult.toString(),testDateFuture.format(datePattern));
		
		assertEquals(timePatternResult.toString(),testDateFuture.format(timePattern));
	}
	
	
	/**
	 * Test for method {@link XdevDate#format(String)}.
	 */
	@Test(expected = NullPointerException.class)
	public void testFormat_ParamPatternNullPointerException()
	{
		testDateFuture.format(null);
	}
	
	
	/**
	 * Test for method {@link XdevDate#format(String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFormat_ParamPatternIllegalArgumentException()
	{
		testDateFuture.format("123dfeswe");
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)} with
	 * {@link XdevDate#DAY_OF_MONTH}.
	 */
	@Test
	public void testGet_ParamFieldIsDayOfMonth()
	{
		assertEquals(testDatePast.get(XdevDate.DAY_OF_MONTH),DAY_PAST_DATE);
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)} with
	 * {@link XdevDate#DAY_OF_WEEK}.
	 */
	@Test
	public void testGet_ParamFieldIsDayOfWeek()
	{
		assertEquals(XdevDate.SUNDAY,testDatePast.get(XdevDate.DAY_OF_WEEK));
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)} with
	 * {@link XdevDate#DAY_OF_WEEK_IN_MONTH}.
	 */
	@Test
	public void testGet_ParamFieldIsDayOfWeekInMonth()
	{
		assertEquals(DAY_OF_WEEK_IN_MONTH_VALUE,testDatePast.get(XdevDate.DAY_OF_WEEK_IN_MONTH));
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)} with
	 * {@link XdevDate#DAY_OF_YEAR}.
	 */
	@Test
	public void testGet_ParamFieldIsDayOfYear()
	{
		assertEquals(DAY_OF_YEAR_VALUE,testDatePast.get(XdevDate.DAY_OF_YEAR));
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)} with {@link XdevDate#MONTH}.
	 */
	@Test
	public void testGet_ParamFieldIsMonth()
	{
		assertEquals(MONTH_PAST_DATE + 1,testDatePast.getMonth());
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)} with {@link XdevDate#YEAR}.
	 */
	@Test
	public void testGet_ParamFieldIsYear()
	{
		assertEquals(testDatePast.get(XdevDate.YEAR),YEAR_PAST_DATE);
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)} with {@link XdevDate#HOUR}.
	 */
	@Test
	public void testGet_ParamFieldIsHour()
	{
		assertEquals(testDateFuture.get(XdevDate.HOUR),2);
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)} with
	 * {@link XdevDate#HOUR_OF_DAY}.
	 */
	@Test
	public void testGet_ParamFieldIsHourOfDay()
	{
		assertEquals(testDateFuture.get(XdevDate.HOUR_OF_DAY),HOUR_FUTURE_DATE);
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)} with {@link XdevDate#MINUTE}.
	 */
	@Test
	public void testGet_ParamFieldIsMinute()
	{
		assertEquals(testDateFuture.get(XdevDate.MINUTE),MINUTE_FUTURE_DATE);
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)} with {@link XdevDate#SECOND}.
	 */
	@Test
	public void testGet_ParamFieldIsSecond()
	{
		assertEquals(testDateFuture.get(XdevDate.SECOND),SECOND_FUTURE_DATE);
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)} with {@link MILLISECOND#HOUR}.
	 */
	@Test
	public void testGet_ParamFieldIsMilliSecond()
	{
		testDatePast.set(XdevDate.MILLISECOND,223);
		assertEquals(testDatePast.get(XdevDate.MILLISECOND),223);
	}
	
	
	/**
	 * Test for method {@link XdevDate#get(int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetWithIndexOutOfBoundsException()
	{
		testDatePast = new XdevDate(2008,12,28,8,18,28);
		
		assertEquals(testDatePast.get(100),0);
	}
	
	
	/**
	 * Test for method {@link XdevDate#getDay()}.
	 */
	@Test
	public void testGetDay_defaultBehavior()
	{
		assertEquals(testDatePast.getDay(),DAY_PAST_DATE);
		assertNotSame(testDatePast.getDay(),DAY_PAST_DATE + 1);
	}
	
	
	/**
	 * Test for method {@link XdevDate#getHour()}.
	 */
	@Test
	public void testGetHour_defaultBehavior()
	{
		assertEquals(testDateFuture.getHour(),HOUR_FUTURE_DATE);
		assertNotSame(testDateFuture.getHour(),HOUR_FUTURE_DATE + 1);
	}
	
	
	/**
	 * Test for method {@link XdevDate#getMinute()}.
	 */
	@Test
	public void testGetMinute_defaultBehavior()
	{
		assertEquals(testDateFuture.getMinute(),MINUTE_FUTURE_DATE);
		assertNotSame(testDateFuture.getMinute(),MINUTE_FUTURE_DATE + 1);
	}
	
	
	/**
	 * Test for method {@link XdevDate#getMonth()}.
	 */
	@Test
	public void testGetMonth_defaultBehavior()
	{
		assertEquals(MONTH_FUTURE_DATE,testDateFuture.getMonth() - 1);
		assertNotSame(MONTH_FUTURE_DATE,testDateFuture.getMonth());
	}
	
	
	/**
	 * Test for method {@link XdevDate#getSecond()}.
	 */
	@Test
	public void testGetSecond_defaultBehavior()
	{
		assertEquals(testDateFuture.getSecond(),SECOND_FUTURE_DATE);
		assertNotSame(testDateFuture.getSecond(),SECOND_FUTURE_DATE + 1);
	}
	
	
	/**
	 * Test for method {@link XdevDate#getYear()}.
	 */
	@Test
	public void testGetYear_defaultBehavior()
	{
		assertEquals(testDateFuture.getYear(),YEAR_FUTURE_DATE);
		assertNotSame(testDateFuture.getYear(),YEAR_FUTURE_DATE + 1);
	}
	
	
	/**
	 * Test for method {@link XdevDate#getDurationBetween(Object)}.
	 */
	@Test
	public void testGetDurationBetween_defaultBehavior()
	{
		final long otherTime = testDateFuture.getTimeInMillis();
		final long thisTime = testDatePast.getTimeInMillis();
		
		final long expectedDuration = Math.abs(otherTime - thisTime);
		
		final Duration returnDuration = testDatePast.getDurationBetween(testDateFuture);
		
		assertEquals(expectedDuration,returnDuration.getMilliseconds());
	}
	
	
	/**
	 * Test for method {@link XdevDate#getDurationSince(Object)}.
	 */
	@Test
	public void testGetDurationSince_defaultBehavior()
	{
		
		final long soonerMillis = testDatePast.getTimeInMillis();
		final long thisMillis = testDateFuture.getTimeInMillis();
		final long expectedDuration = Math.abs(thisMillis - soonerMillis);
		
		final Duration returnDuration = testDateFuture.getDurationSince(testDatePast);
		
		assertEquals(expectedDuration,returnDuration.getMilliseconds());
	}
	
	
	/**
	 * Test for method {@link XdevDate#getDurationSince(Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetDurationSince_ParamSoonerIllegalArgumentException()
	{
		testDatePast.getDurationSince(testDateFuture);
	}
	
	
	/**
	 * Test for method {@link XdevDate#getDurationUntil(Object)}.
	 */
	@Test
	public void testGetDurationUntil_defaultBehavior()
	{
		
		final long laterMillis = testDateFuture.getTimeInMillis();
		final long thisMillis = testDatePast.getTimeInMillis();
		final long expectedDuration = Math.abs(laterMillis - thisMillis);
		
		final Duration returnDuration = testDatePast.getDurationUntil(testDateFuture);
		
		assertEquals(expectedDuration,returnDuration.getMilliseconds());
	}
	
	
	/**
	 * Test for method {@link XdevDate#getDurationUntil(Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetDurationUntil_ParamLaterIllegalArgumentException()
	{
		testDateFuture.getDurationUntil(testDatePast);
	}
	
	
	/**
	 * Test for method {@link XdevDate#getFormat())}.
	 */
	@Test
	public void testGetFormat_defaultBehavior()
	{
		assertEquals(DATE_FORMAT_FUTURE,testDateFuture.getFormat());
	}
	
	
	/**
	 * Test for method {@link XdevDate#setFormat(java.text.DateFormat))}.
	 */
	@Test
	public void testSetFormat_defaultBehavior()
	{
		testDatePast.setFormat(DATE_FORMAT_FUTURE);
		
		assertEquals(DATE_FORMAT_FUTURE,testDatePast.getFormat());
	}
	
	
	/**
	 * Test for method {@link XdevDate#isLeapYear()}.
	 */
	@Test
	public void testIsLeapYear_IsLeapYear()
	{
		assertTrue(testDatePast.isLeapYear());
	}
	
	
	/**
	 * Test for method {@link XdevDate#isLeapYear()}.
	 */
	@Test
	public void testIsLeapYear_IsNotLeapYear()
	{
		assertFalse(testDateFuture.isLeapYear());
	}
	
	
	/**
	 * Test for method {@link XdevDate#roll(int, int)} with
	 * XdevDate#DAY_OF_WEEK.
	 */
	@Test
	public void testRoll_ParamFieldIsDayOfWeek()
	{
		testDatePast.roll(XdevDate.DAY_OF_WEEK,1);
		
		assertEquals(testDatePast.get(XdevDate.DAY_OF_WEEK),XdevDate.MONDAY);
	}
	
	
	/**
	 * Test for method {@link XdevDate#roll(int, int)} with XdevDate#YEAR.
	 */
	@Test
	public void testRoll_ParamFieldIsYear()
	{
		testDatePast.roll(XdevDate.YEAR,1);
		
		assertEquals(testDatePast.get(XdevDate.YEAR),YEAR_PAST_DATE + 1);
	}
	
	
	/**
	 * Test for method {@link XdevDate#roll(int, int)} with XdevDate#MONTH.
	 */
	@Test
	public void testRoll_ParamFieldIsMonth()
	{
		testDatePast.roll(XdevDate.MONTH,3);
		
		assertEquals(testDatePast.get(XdevDate.MONTH),3);
	}
	
	
	/**
	 * Test for method {@link XdevDate#roll(int, int)} with XdevDate#MINUTE.
	 */
	@Test
	public void testRoll_ParamFieldIsMinute()
	{
		testDateFuture.roll(XdevDate.MINUTE,1);
		
		assertEquals(testDateFuture.get(XdevDate.MINUTE),MINUTE_FUTURE_DATE + 1);
	}
	
	
	/**
	 * Test for method {@link XdevDate#roll(int, int)} with
	 * XdevDate#MILLISECOND.
	 */
	@Test
	public void testRoll_ParamFieldIsMilliSecond()
	{
		testDatePast.roll(XdevDate.MILLISECOND,65);
		
		assertEquals(testDatePast.get(XdevDate.MILLISECOND),65);
	}
	
	
	/**
	 * Test for method {@link XdevDate#set(int, int)}.
	 */
	@Test
	public void testSet_ParamFieldIsDayOfMonth()
	{
		final XdevDate currentDate = new XdevDate();
		currentDate.set(XdevDate.DAY_OF_MONTH,28);
		assertEquals(currentDate.get(XdevDate.DAY_OF_MONTH),28);
	}
	
	
	/**
	 * Test for method {@link XdevDate#set(int, int)}.
	 */
	@Test
	public void testSet_ParamFieldIsMonth()
	{
		final XdevDate currentDate = new XdevDate();
		currentDate.set(XdevDate.MONTH,12);
		assertEquals(currentDate.get(XdevDate.MONTH),12);
	}
	
	
	/**
	 * Test for method {@link XdevDate#set(int, int)}.
	 */
	@Test
	public void testSet_ParamFieldIsYear()
	{
		final XdevDate currentDate = new XdevDate();
		currentDate.set(XdevDate.YEAR,2008);
		assertEquals(currentDate.get(XdevDate.YEAR),2008);
	}
	
	
	/**
	 * Test for method {@link XdevDate#set(int, int)}.
	 */
	@Test
	public void testSet_ParamFieldIsHourOfDay()
	{
		final XdevDate currentDate = new XdevDate();
		currentDate.set(XdevDate.HOUR_OF_DAY,8);
		assertEquals(currentDate.get(XdevDate.HOUR),8);
	}
	
	
	/**
	 * Test for method {@link XdevDate#set(int, int)}.
	 */
	@Test
	public void testSet_ParamFieldIsMinute()
	{
		final XdevDate currentDate = new XdevDate();
		currentDate.set(XdevDate.MINUTE,18);
		assertEquals(currentDate.get(XdevDate.MINUTE),18);
	}
	
	
	/**
	 * Test for method {@link XdevDate#set(int, int)}.
	 */
	@Test
	public void testSet_ParamFieldIsSecond()
	{
		final XdevDate currentDate = new XdevDate();
		currentDate.set(XdevDate.SECOND,28);
		assertEquals(currentDate.get(XdevDate.SECOND),28);
	}
	
	
	/**
	 * Test for method {@link XdevDate#minus(Duration)}.
	 */
	@Test
	public void testMinus_defaultBehavior()
	{
		final Duration duration = new Duration(2,2,0,0,0);
		
		final XdevDate expected = testDatePast.copy();
		
		assertEquals(expected.rollBack(duration),testDatePast.minus(duration));
	}
	
	
	/**
	 * Test for method {@link XdevDate#plus(Duration)}.
	 */
	@Test
	public void testPlus_defaultBehavior()
	{
		final Duration duration = new Duration(2,2,0,0,0);
		
		final XdevDate expected = testDatePast.copy();
		
		assertEquals(expected.rollForward(duration),testDatePast.plus(duration));
	}
	
	
	/**
	 * Test for method {@link XdevDate#rollForward(Duration)}.
	 */
	@Test
	public void testRollForward_defaultBehavior()
	{
		final Duration duration = new Duration(2,2,0,0,0);
		
		final XdevDate expected = testDatePast.copy();
		
		testDatePast.roll(XdevDate.DAY_OF_MONTH,2);
		testDatePast.roll(XdevDate.HOUR_OF_DAY,2);
		
		assertEquals(testDatePast,expected.rollForward(duration));
	}
	
	
	/**
	 * Test for method {@link XdevDate#rollBack(Duration)}.
	 */
	@Test
	public void testRollBack_defaultBehavior()
	{
		final Duration duration = new Duration(2,2,0,0,0);
		
		final XdevDate expected = testDatePast.copy();
		expected.rollBack(duration);
		
		assertEquals(expected,testDatePast.minus(duration));
	}
	
	@Test
	public void testTimeStamp_defaultBehavior()
	{
		final long currentTimeMillis = testDateFuture.getTimeInMillis();
		final XdevDate date = new XdevDate(currentTimeMillis);
		assertEquals(date.timeStamp(),currentTimeMillis);
	}
	
	
	@Test
	@Ignore("Works only in UTC+1")
	public void testGetTime_defaultBehavior()
	{
		final Date javdate = testDatePast.getTime();
		assertEquals(1230418800000L, javdate.getTime());
	}
	
	
	/**
	 * tests the implementation of {@link Object#equals(Object)} and
	 * {@link Object#hashCode()}.
	 * 
	 * @author RF
	 */
	@Test
	public void testHashCode()
	{
		final XdevDate testDate1 = new XdevDate(YEAR_PAST_DATE,MONTH_PAST_DATE,DAY_PAST_DATE);
		final XdevDate testDate2 = new XdevDate(YEAR_PAST_DATE,MONTH_PAST_DATE,DAY_PAST_DATE);
		
		final Set<XdevDate> dates = new HashSet<>();
		dates.add(testDate1);
		dates.add(testDate2);
		/*
		 * There should be only one XdevDate instance in the Set, because both
		 * instances added represent the same point in time and should therefore
		 * be equal
		 */
		assertEquals(1,dates.size());
	}
	
	
	/**
	 * Tests {@link XdevDate#equals(Object)} <a
	 * href="http://issues.xdev-software.de/view.php?id=11417">IssueTracker
	 * #11417</a>
	 * 
	 * @author RF
	 */
	@Test
	public void testEquals_Issue11417()
	{
		final XdevDate testDate1 = new XdevDate(YEAR_PAST_DATE,MONTH_PAST_DATE,DAY_PAST_DATE);
		final XdevDate testDate2 = new XdevDate(YEAR_PAST_DATE,MONTH_PAST_DATE,DAY_PAST_DATE);
		final GregorianCalendar cal = testDate2;
		assertTrue(testDate1.equals(cal));
		assertTrue(cal.equals(testDate1));
		
	}
}
