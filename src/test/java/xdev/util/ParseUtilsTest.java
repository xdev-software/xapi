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

package xdev.util;

import java.awt.Image;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.ImageIcon;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import xdev.io.XdevFile;
import xdev.ui.XdevImage;


/**
 * Test for Class {@link ParseUtils}.
 * 
 * @author mklinger
 * @deprecated {@link ParseUtils} are deprecated
 */
@Deprecated
@Ignore
@SuppressWarnings("deprecation")
public class ParseUtilsTest
{
	
	@BeforeClass
	public static void specify()
	{
		System.out.println("\n-> ParseUtils Test");
	}
	
	/**
	 * The delta value to compare floating-point numbers.
	 */
	public static final double DOUBLE_DELTA = 0.00;
	
	/**
	 * Test for Method {@link ParseUtils#parse(double)}.
	 */
	@Test
	public void parse()
	{
		final Number expected = 12;
		Number actual = null;
		final double input = 12.5;
		try
		{
			actual = ParseUtils.parse(input, "0000");
		}
		catch(final ParseException e)
		{
			System.err.println(e);
		}
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#format(double)}.
	 */
	@Test
	public void format_Double()
	{
		final String expected = "2.000,35";
		final String actual = ParseUtils.format(2000.3505);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.format(2000.3505).equals("2.000,35"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#format(double)}.
	 */
	@Test
	public void format_Double_two()
	{
		final String expected = "-23.205,359";
		final String actual = ParseUtils.format(-23205.3589705);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.format(2000.3505).equals("2.000,35"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#format(double, String)}.
	 */
	@Test
	public void format_DoubleString()
	{
		final String expected = "hello25898";
		final String actual = ParseUtils.format(25897.6321, "hello");
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.format(2000.3505).equals("2.000,35"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#format(double, String)}.
	 */
	@Test
	public void format_DoubleString_two()
	{
		final String expected = "-Euro7892";
		final String actual = ParseUtils.format(-7892.0365478, "Euro");
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.format(2000.3505).equals("2.000,35"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#format(long)}.
	 */
	@Test
	public void format_Long()
	{
		final String expected = "894.302.256";
		final String actual = ParseUtils.format(894302256);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.format(20003505).equals("20.003.505"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#format(long)}.
	 */
	@Test
	public void format_Long_two()
	{
		final String expected = "-789.602.314";
		final String actual = ParseUtils.format(-789602314);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.format(20003505).equals("20.003.505"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#format(long, String)}.
	 */
	@Test
	public void format_LongString()
	{
		final String expected = "1.000,12";
		final String actual = ParseUtils.format(1000.123, ",###.##");
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.format(20003505).equals("20.003.505"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#format(long, String)}.
	 */
	@Test
	public void format_LongString_two()
	{
		final String expected = "-Negative Zahl 569780212";
		final String actual = ParseUtils.format(-569780212, "Negative Zahl ");
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.format(20003505).equals("20.003.505"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isAlphaNumeric(String, int, int)}.
	 */
	@Test
	public void isAlphaNumeric()
	{
		final boolean expected = true;
		final boolean actual = ParseUtils.isAlphaNumeric("Hello10", 2, 30);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isAlphaNumeric("Hallo10",2,25));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isAlphaNumeric(String, int, int)}.
	 */
	@Test
	public void isAlphaNumeric_two()
	{
		final boolean expected = true;
		final boolean actual = ParseUtils.isAlphaNumeric("Hello-15", -40, 30);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isAlphaNumeric("Hallo10",2,25));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isAlphaNumeric(String, int, int)}
	 */
	@Test
	public void isAlphaNumeric_three()
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.isAlphaNumeric("Hello XDEV3", -40, -15);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isAlphaNumeric("Hallo10",2,25));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isAlphaNumeric(String, int, int)}
	 */
	@Test
	public void isAlphaNumeric_four()
	{
		Assert.assertFalse(ParseUtils.isAlphaNumeric("123", 0, 5));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isInteger(Object)}
	 */
	@Test
	public void isInteger()
	{
		final boolean expected = true;
		final boolean actual = ParseUtils.isInteger(20);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isInteger(Object)}
	 */
	@Test
	public void isInteger_two()
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.isInteger(null);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object)}.
	 */
	@Test
	public void parseXdevDate()
	{
		final XdevDate datum = new XdevDate(new Date(2009, 3, 12));
		final Object dt = datum;
		Assert.assertTrue(ParseUtils.parseDate(dt) instanceof XdevDate);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDouble(Object)}.
	 */
	@Test
	public void parseDouble()
	{
		final double expected = 5.20;
		final double actual = ParseUtils.parseDouble(5.20);
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
		// Assert.assertEquals(ParseUtils.parseDouble("1.9"),1.9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDouble(Object)}.
	 */
	@Test
	public void parseDouble_two()
	{
		final double expected = -175.28;
		final double actual = ParseUtils.parseDouble(-175.28);
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
		// Assert.assertEquals(ParseUtils.parseDouble("1.9"),1.9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDouble(Object)}.
	 */
	@Test
	public void parseDouble_three()
	{
		final double expected = 147.258;
		final double actual = ParseUtils.parseDouble("147,258");
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
		// Assert.assertEquals(ParseUtils.parseDouble("1.9"),1.9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDouble(Object)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseDouble_NumberFormatException()
	{
		ParseUtils.parseDouble("XDEV Buchungssystem");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object, String)}.
	 */
	@Test
	public void parseDoubleString() throws ParseException
	{
		final double expected = 0.35;
		final double actual = ParseUtils.parseDouble(",35", ".00");
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
		// Assert.assertEquals(ParseUtils.parseDouble("1.9"),1.9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object, String)}.
	 */
	@Test
	public void parseDoubleString_two() throws ParseException
	{
		final double expected = 12.341;
		final double actual = ParseUtils.parseDouble(12.341000, "#.00");
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
		// Assert.assertEquals(ParseUtils.parseDouble("1.9"),1.9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object, String)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseDouble_ParseException()
	{
		try
		{
			ParseUtils.parseDouble("Hello", "56.256");
		}
		catch(final ParseException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseImage(Object)}.
	 */
	@Test
	public void parseInt()
	{
		final int expected = 56;
		final int actual = ParseUtils.parseInt(56);
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseDouble("9"),9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseImage(Object)}.
	 */
	@Test
	public void parseInt_two()
	{
		final int expected = -268;
		final int actual = ParseUtils.parseInt(-268);
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseDouble("9"),9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseImage(Object)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseInt_NumberFormatException()
	{
		ParseUtils.parseInt("Hello");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseInt(Object, String)}.
	 */
	@Test
	public void parseIntString() throws ParseException
	{
		final int expected = 12;
		final int actual = ParseUtils.parseInt(12.356, "##");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseDouble("9"),9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseInt(Object, String)}.
	 */
	@Test
	public void parseIntString_two() throws ParseException
	{
		final int expected = 12;
		final int actual = ParseUtils.parseInt("12", "##");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseDouble("9"),9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseInt(Object, String)}.
	 */
	@Test
	public void parseIntString_three() throws ParseException
	{
		final int expected = 17;
		final int actual = ParseUtils.parseInt("17.06", "##");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseDouble("9"),9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseInt(Object, String)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseInt_ParseException() throws ParseException
	{
		ParseUtils.parseInt("hello", "54587456486");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDoubleAsCurrency(Object)}.
	 */
	@Test
	public void parseDoubleAsCurrency()
	{
		final double expected = 2587.69;
		final double actual = ParseUtils.parseDoubleAsCurrency(2587.69);
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDoubleAsCurrency(Object)}.
	 */
	@Test
	public void parseDoubleAsCurrency_two()
	{
		final double expected = -5697.62;
		final double actual = ParseUtils.parseDoubleAsCurrency(-5697.62);
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDoubleAsCurrency(Object)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseDoubleAsCurrency_three()
	{
		final double expected = -5697.62;
		final double actual = ParseUtils.parseDoubleAsCurrency("110");
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDoubleAsCurrency(Object, String)}.
	 */
	@Test
	public void parseDoubleAsCurrencyString() throws ParseException
	{
		final double expected = 12.35;
		final double actual = ParseUtils.parseDoubleAsCurrency("12,350000", "#.000000");
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDoubleAsCurrency(Object, String)}.
	 */
	@Test
	public void parseDoubleAsCurrencyString_two() throws ParseException
	{
		final double expected = 12345.679;
		final double actual = ParseUtils.parseDoubleAsCurrency("12.345,679", ",###");
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseFloat(Object)}.
	 */
	@Test
	public void parseFloat()
	{
		final float expected = 545687453;
		final float actual = ParseUtils.parseFloat(545687453);
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseFloat(Object)}.
	 */
	@Test
	public void parseFloat_two()
	{
		final float expected = -564868745;
		final float actual = ParseUtils.parseFloat(-564868745);
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseFloat(Object)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseFloat_NumberFormatException()
	{
		ParseUtils.parseFloat("hallo xdev");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseFloat(Object, String)}.
	 */
	@Test
	public void parseFloatString() throws ParseException
	{
		final float expected = 14;
		final float actual = ParseUtils.parseFloat("0000014", "000000");
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
		// Assert.assertEquals((float)ParseUtils.parseFloat("1289340128"),new
		// Float("1289340128"),0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseFloat(Object, String)}.
	 */
	@Test
	public void parseFloatString_two() throws ParseException
	{
		final float expected = 3456;
		final float actual = ParseUtils.parseFloat("00003456", "000000");
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
		// Assert.assertEquals((float)ParseUtils.parseFloat("1289340128"),new
		// Float("1289340128"),0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseFloat(Object, String)}.
	 */
	@Test
	public void parseFloatString_three() throws ParseException
	{
		final float expected = 1234;
		final float actual = ParseUtils.parseFloat(1234, "000000");
		Assert.assertEquals(expected, actual, DOUBLE_DELTA);
		// Assert.assertEquals((float)ParseUtils.parseFloat("1289340128"),new
		// Float("1289340128"),0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseFloat(Object, String)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseFloat_ParseException() throws ParseException
	{
		// java.lang.Exception: Unexpected exception,
		// expected<java.text.ParseException> but
		// was<java.lang.NumberFormatException>
		ParseUtils.parseFloat("one", "#####");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseLong(Object)}.
	 */
	@Test
	public void parseLong()
	{
		final long expected = 223545854;
		final long actual = ParseUtils.parseLong(223545854);
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseLong("128934012856"),128934012856l);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseLong(Object)}.
	 */
	@Test
	public void parseLong_two()
	{
		final long expected = -545845854;
		final long actual = ParseUtils.parseLong(-545845854);
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseLong("128934012856"),128934012856l);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseLong(Object)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseLong_NumberFormatException()
	{
		ParseUtils.parseLong("Hello XDEV");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseLong(Object, String)}.
	 */
	@Test
	public void parseLongString() throws ParseException
	{
		final long expected = 546545;
		final long actual = ParseUtils.parseLong("546545,64121.385", ",######");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseLong("128934012856"),128934012856l);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseLong(Object, String)}.
	 */
	@Test
	public void parseLongString_two() throws ParseException
	{
		final long expected = 546872313;
		final long actual = ParseUtils.parseLong("546872313", "########");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseLong("128934012856"),128934012856l);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseLong(Object, String)}.
	 */
	@Test
	public void parseLongString_three() throws ParseException
	{
		final long expected = 1234;
		final long actual = ParseUtils.parseLong(1234, "#");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseLong("128934012856"),128934012856l);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseLong(Object, String)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseLong_ParseException() throws ParseException
	{
		// java.lang.Exception: Unexpected exception,
		// expected<java.text.ParseException> but
		// was<java.lang.NumberFormatException>
		ParseUtils.parseLong("465413256", "5457.89545");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseShort(Object)}.
	 */
	@Test
	public void parseShort()
	{
		final short expected = 2561;
		final short actual = ParseUtils.parseShort("2561");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals((short)ParseUtils.parseShort("212"),(short)212,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseShort(Object, String)}.
	 */
	@Test
	public void parseShortString() throws ParseException
	{
		final short expected = 32767;
		final short actual = ParseUtils.parseShort("0032767", "##0000");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals((short)ParseUtils.parseShort("212"),(short)212,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseShort(Object, String)}.
	 */
	@Test
	public void parseShortString_two() throws ParseException
	{
		
		final short expected = 235;
		final short actual = ParseUtils.parseShort("235.123", "####");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals((short)ParseUtils.parseShort("212"),(short)212,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseShort(Object, String)}.
	 */
	@Test
	public void parseShortString_three() throws ParseException
	{
		final short expected = 012345;
		final short actual = ParseUtils.parseShort(012345, "#");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals((short)ParseUtils.parseShort("212"),(short)212,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseShort(Object)}.
	 */
	@Test
	public void parseShort_two()
	{
		final short expected = -25615;
		final short actual = ParseUtils.parseShort(-25615);
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals((short)ParseUtils.parseShort("212"),(short)212,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseShort(Object)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseShort_NumberFormatException()
	{
		ParseUtils.parseShort("HelloXDEV");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseShort(Object, String)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseShort_ParseException() throws ParseException
	{
		// java.lang.Exception: Unexpected exception,
		// expected<java.text.ParseException> but
		// was<java.lang.NumberFormatException>
		ParseUtils.parseShort("545238", "215.8792");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseHashtable(Object)}.
	 */
	@Test
	public void parseHashtable()
	{
		// Hashtable wird erzeugt
		final XdevHashtable<String, String> hauptstaedte = new XdevHashtable<>();
		
		hauptstaedte.put("Deutschland", "Berlin");
		hauptstaedte.put("Schweiz", "Bern");
		hauptstaedte.put("Oesterreich", "Wien");
		hauptstaedte.put("Liechtenstein", "Vaduz");
		
		final Object o = hauptstaedte;
		
		Assert.assertTrue(ParseUtils.parseHashtable(o) instanceof XdevHashtable<?, ?>);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseUtilDate(Object)}.
	 */
	@Test
	public void parseUtilDate()
	{
		final java.util.Date datum = new java.util.Date(2009, 3, 12);
		final Object o = datum;
		
		Assert.assertTrue(ParseUtils.parseUtilDate(o) instanceof java.util.Date);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseUtilDate(Object)}.
	 */
	@Test
	public void parseUtilDate_2()
	{
		final java.util.Date actual = ParseUtils.parseUtilDate(null);
		
		Assert.assertEquals(null, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseUtilDate(Object)}.
	 */
	@Test
	public void parseUtilDate_3()
	{
		final java.util.Date expected = new Date(91, 05, 17);
		
		final Calendar c = new GregorianCalendar(1991, 05, 17);
		final java.util.Date actual = ParseUtils.parseUtilDate(c);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseImage(Object)}.
	 */
	@Test
	public void parseImage()
	{
		final Image image = new ImageIcon("image.gif").getImage();
		final Object i = image;
		
		Assert.assertTrue(ParseUtils.parseImage(i) instanceof XdevImage);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseFile(Object)}.
	 */
	@Test
	public void parseFile()
	{
		final XdevFile file = new XdevFile("c:\\test.txt");
		
		Assert.assertTrue(ParseUtils.parseFile(file) instanceof XdevFile);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseList(Object)}.
	 */
	@Test
	public void parseList()
	{
		final XdevList<Object> expected = new XdevList<>();
		// XdevList<Object> actual = ParseUtils.parseList(expected);
		// Assert.assertEquals(expected, actual);
		
		Assert.assertTrue(ParseUtils.parseList(expected) instanceof XdevList<?>);
	}
	
	/**
	 * Test for Method {@link ParseUtils#isBoolean(Object)}.
	 */
	@Test
	public void isBoolean()
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.isBoolean(null);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isBoolean(bt));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isBoolean(Object)}.
	 */
	@Test
	public void isBoolean_two()
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.isBoolean(150);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isBoolean(bt));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isBoolean(Object)}.
	 */
	@Test
	public void istBoolean_three()
	{
		Assert.assertFalse(ParseUtils.isBoolean("true"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isDecimal(Object)}.
	 */
	@Test
	public void isDecimal()
	{
		final boolean expected = true;
		final boolean actual = ParseUtils.isDecimal(200.05);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isDecimal(dt));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isDecimal(Object)}.
	 */
	@Test
	public void isDecimal_two()
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.isDecimal(null);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isDecimal(dt));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isEmail(String)}.
	 */
	@Test
	public void isEmail()
	{
		final boolean expected = true;
		final boolean actual = ParseUtils.isEmail("a.bauernfeind@xdev-software.de");
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#isEmail(String)}.
	 */
	@Test
	public void isEmail_two()
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.isEmail(" ");
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#isLong(Object)}.
	 */
	@Test
	public void isLong()
	{
		final boolean expected = true;
		final boolean actual = ParseUtils.isLong(20875884855699l);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isLong(expected));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isLong(Object)}.
	 */
	@Test
	public void isLong_two()
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.isLong(null);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isLong(expected));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isLong(Object)}.
	 */
	@Test
	public void isLong_three()
	{
		Assert.assertFalse(ParseUtils.isLong(11.11));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isNumeric(Object, long, long)}.
	 */
	@Test
	public void isNumericLong()
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.isNumeric(489999995, 12000, 698777);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isNumeric(100000000l,5000000l,9000000000l));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isNumeric(Object, long, long)}.
	 */
	@Test
	public void isNumericLong_two()
	{
		final boolean expected = true;
		final boolean actual = ParseUtils.isNumeric(5000, 5000, 5000);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isNumeric(100000000l,5000000l,9000000000l));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isNumeric(Object, double, double)}.
	 */
	@Test
	public void isNumericDouble()
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.isNumeric(5600.80, 500.20, 4000.90);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isNumeric(100000000l,5000000l,9000000000l));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isNumeric(Object, double, double)}.
	 */
	@Test
	public void isNumericDouble_two()
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.isNumeric(-5000.90, -489.25, -800.65);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isNumeric(100000000l,5000000l,9000000000l));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isNumeric(Object, double, double)}.
	 */
	@Test
	public void isNumericDouble_three()
	{
		final boolean expected = true;
		final boolean actual = ParseUtils.isNumeric(-5000.90, -5000.90, -5000.90);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isNumeric(100000000l,5000000l,9000000000l));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toHexString(int)}.
	 */
	@Test
	public void toHexStringInt()
	{
		final String expected = "19";
		final String actual = ParseUtils.toHexString(25);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toHexString(int)}.
	 */
	@Test
	public void toHexStringInt_two()
	{
		final String expected = "ffffffc2";
		final String actual = ParseUtils.toHexString(-62);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toHexString(long)}.
	 */
	@Test
	public void toHexStringLong()
	{
		final String expected = "ffff9ad7";
		final String actual = ParseUtils.toHexString(-25897);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toHexString(long)}.
	 */
	@Test
	public void toHexStringLong_two()
	{
		final String expected = "a1d";
		final String actual = ParseUtils.toHexString(2589);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toOctalString(int)}.
	 */
	@Test
	public void toOctalStringInt()
	{
		final String expected = "37774054227";
		final String actual = ParseUtils.toOctalString(-1025897);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toOctalString(int)}.
	 */
	@Test
	public void toOctalStringInt_two()
	{
		final String expected = "2217200";
		final String actual = ParseUtils.toOctalString(597632);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toOctalString(long)}.
	 */
	@Test
	public void toOctalStringLong()
	{
		final String expected = "7511252571";
		final String actual = ParseUtils.toOctalString(1025856889);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toOctalString(long)}.
	 */
	@Test
	public void toOctalStringLong_two()
	{
		final String expected = "33466103201";
		final String actual = ParseUtils.toOctalString(-589789567);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toBinaryString(int)}.
	 */
	@Test
	public void toBinaryStringInt()
	{
		final String expected = "11011100110110001000011010000001";
		final String actual = ParseUtils.toBinaryString(-589789567);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toBinaryString(int)}.
	 */
	@Test
	public void toBinaryStringInt_two()
	{
		final String expected = "110101100001001101100111010111";
		final String actual = ParseUtils.toBinaryString(897898967);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toBinaryString(long)}.
	 */
	@Test
	public void toBinaryStringLong()
	{
		final String expected = "110101011110111010011001011111";
		final String actual = ParseUtils.toBinaryString(897295967);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toBinaryString(long)}.
	 */
	@Test
	public void toBinaryStringLong_two()
	{
		final String expected = "11001110011010101000110001001101";
		final String actual = ParseUtils.toBinaryString(-831878067);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#formatAsCurrency(long)}.
	 */
	@Test
	public void formatAsCurrencyLong()
	{
		final String expected = "897.565.258,00 €";
		final String actual = ParseUtils.formatAsCurrency(897565258);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#formatAsCurrency(long)}.
	 */
	@Test
	public void formatAsCurrencyLong_two()
	{
		final String expected = "-45.752.378,00 €";
		final String actual = ParseUtils.formatAsCurrency(-45752378);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#formatAsCurrency(double)}.
	 */
	@Test
	public void formatAsCurrencyDouble()
	{
		final String expected = "78.963,26 €";
		final String actual = ParseUtils.formatAsCurrency(78963.256);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#formatAsCurrency(double)}.
	 */
	@Test
	public void formatAsCurrencyDouble_two()
	{
		final String expected = "-458,26 €";
		final String actual = ParseUtils.formatAsCurrency(-458.256);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parse(Object)}.
	 */
	@Test
	public void parseNumber() throws ParseException
	{
		final Number expected = 78;
		final Number actual = ParseUtils.parse(78);
		
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parse(Object)}.
	 */
	@Test
	public void parseNumber_two() throws ParseException
	{
		final Number expected = -256;
		final Number actual = ParseUtils.parse(-256);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parse(Object)}.
	 */
	@Test(expected = ParseException.class)
	public void parseNumber_ParseException() throws ParseException
	{
		ParseUtils.parse("hello");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseBoolean(Object)}.
	 */
	@Test
	public void parseBoolean()
	{
		final boolean expected = true;
		final boolean actual = ParseUtils.parseBoolean(232);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseBoolean(Object)}.
	 */
	public void parseBoolean_two()
	{
		final boolean expected = true;
		final boolean actual = ParseUtils.parseBoolean(-232);
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseBoolean(Object)}.
	 */
	@Test
	public void parseBoolean_three()
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.parseBoolean("hello");
		Assert.assertEquals(expected, actual);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseBoolean(Object)}.
	 */
	@Test
	public void parseBoolean_four()
	{
		Assert.assertFalse(ParseUtils.parseBoolean(0));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseBoolean(Object)}.
	 */
	@Test
	public void parseBoolean_5()
	{
		Assert.assertFalse(ParseUtils.parseBoolean(false));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parse(Object, String)}.
	 */
	@Test
	public void parseNumberString() throws ParseException
	{
		final Number expected = 14;
		final Number actual = ParseUtils.parse("00000014", "00000000");
		Assert.assertEquals(expected.doubleValue(), actual.doubleValue(), DOUBLE_DELTA);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parse(Object, String)}.
	 */
	@Test
	public void parseNumberString_two() throws ParseException
	{
		final Number expected = 14000;
		final Number actual = ParseUtils.parse("0014000", "0000000");
		Assert.assertEquals(expected.doubleValue(), actual.doubleValue(), DOUBLE_DELTA);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parse(Object, String)}.
	 */
	@Test
	public void parseNumberString_three() throws ParseException
	{
		final Number expected = 0617;
		final Number actual = ParseUtils.parse(0617, "#");
		Assert.assertEquals(expected.doubleValue(), actual.doubleValue(), DOUBLE_DELTA);
		// Assert.assertTrue(ParseUtils.isInteger(10));
	}
	
	/**
	 * Test for Method {@link ParseUtils#parse(Object, String)}.
	 */
	@Test(expected = ParseException.class)
	public void parseNumberString_ParseException() throws ParseException
	{
		ParseUtils.parse("Hello", "564678645");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseByte(Object)}.
	 */
	@Test
	public void parseByte()
	{
		final byte expected = -41;
		final byte actual = ParseUtils.parseByte(7895);
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseDouble("9"),9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseByte(Object)}.
	 */
	@Test
	public void parseByte_two()
	{
		final byte expected = -56;
		final byte actual = ParseUtils.parseByte(-64568);
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseDouble("9"),9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseByte(Object)}.
	 */
	@Test
	public void parseByte_three()
	{
		final byte expected = 100;
		final byte actual = ParseUtils.parseByte(100.10);
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseDouble("9"),9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseByte(Object)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseByte_NumberFormatException()
	{
		ParseUtils.parseByte("hello and goodbye");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseByte(Object, String)}.
	 */
	@Test
	public void parseByteString() throws ParseException
	{
		final byte expected = 20;
		final byte actual = ParseUtils.parseByte("58900", "00000");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseDouble("9"),9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseByte(Object, String)}.
	 */
	@Test
	public void parseByteString_two() throws ParseException
	{
		final byte expected = 111;
		final byte actual = ParseUtils.parseByte("-3695 EUR", "-,#00.00 \u00A4\u00A4");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseDouble("9"),9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseByte(Object, String)}.
	 */
	@Test
	public void parseByteString_three() throws ParseException
	{
		final byte expected = 20;
		final byte actual = ParseUtils.parseByte(58900, "00000");
		Assert.assertEquals(expected, actual);
		// Assert.assertEquals(ParseUtils.parseDouble("9"),9,0);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseByte(Object, String)}.
	 */
	@Test(expected = NumberFormatException.class)
	public void parseByte_ParseException() throws ParseException
	{
		// java.lang.Exception: Unexpected exception,
		// expected<java.text.ParseException> but
		// was<java.lang.NumberFormatException>
		ParseUtils.parseByte("Hello", "5698");
	}
	
	/**
	 * Test for Method {@link ParseUtils#isType(Object, String)}.
	 */
	@Test
	public void isType_String() throws ParseException
	{
		final boolean expected = false;
		final boolean actual = ParseUtils.isType(65, "double");
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object)}.
	 */
	@Test
	public void parseDate() throws ParseException
	{
		final XdevDate expected = new XdevDate(2010, 00, 10);
		
		final XdevDate actual = ParseUtils.parseDate("10.01.2010", "dd.MM.yyyy");
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object)}.
	 */
	@Test
	public void parseDate_1()
	{
		final XdevDate expected = null;
		final XdevDate actual = ParseUtils.parseDate("01.01.2010");
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object)}.
	 */
	@Test
	public void parseDate_2()
	{
		final XdevDate expected = new XdevDate(2010, 10, 03);
		
		final Calendar c = new GregorianCalendar(2010, 10, 03);
		final XdevDate actual = ParseUtils.parseDate(c);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object)}.
	 */
	@Test
	public void parseDate_3()
	{
		final XdevDate expected = new XdevDate(2011, 01, 01);
		
		final XdevDate actual = ParseUtils.parseDate("2011-02-01", "yyyy-MM-dd");
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object)}.
	 */
	@Test
	public void parseDate_4()
	{
		final XdevDate expected = null;
		final XdevDate actual = ParseUtils.parseDate(null);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object)}.
	 */
	@Test
	public void parseDate_5()
	{
		final XdevDate expected = null;
		final XdevDate actual = ParseUtils.parseDate(null, "yyyy-MM-dd");
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object)}.
	 */
	@Test(expected = DateFormatException.class)
	public void parseDate_6()
	{
		ParseUtils.parseDate("datum", "yyyy-MM-dd");
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseDate(Object)}.
	 */
	@Test
	public void parseDate_7()
	{
		final XdevDate expected = new XdevDate(1991, 05, 17);
		final XdevDate actual = ParseUtils.parseDate(new Date(91, 05, 17), "yyyy-MM-dd");
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseList(Object)}.
	 */
	@Test
	public void parseList_1()
	{
		final int[] array = {1, 2, 3, 4, 5};
		
		final XdevList expected = new XdevList();
		expected.add(1);
		expected.add(2);
		expected.add(3);
		expected.add(4);
		expected.add(5);
		
		final XdevList actual = ParseUtils.parseList(array);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseList(Object)}.
	 */
	@Test
	public void parseList_2()
	{
		final ArrayList<String> liste = new ArrayList<>();
		liste.add("XDEV");
		liste.add("Software");
		liste.add("Corp.");
		
		final XdevList<String> expected = new XdevList<>();
		expected.add("XDEV");
		expected.add("Software");
		expected.add("Corp.");
		
		final XdevList<?> actual = ParseUtils.parseList(liste);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseList(Object)}.
	 */
	@Test
	public void parseList_3()
	{
		final XdevList<?> actual = ParseUtils.parseList("XDEV Software");
		
		Assert.assertEquals(null, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseList(Object)}.
	 */
	@Test
	public void parseList_4()
	{
		final XdevList<?> actual = ParseUtils.parseList(null);
		
		Assert.assertEquals(null, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseHashtable(Object)}.
	 */
	@Test
	public void parseHashtable_1()
	{
		final HashMap<String, Boolean> answer = new HashMap<>();
		answer.put("A", true);
		answer.put("B", false);
		answer.put("C", false);
		
		final XdevHashtable<String, Boolean> expected = new XdevHashtable<>();
		expected.put("A", true);
		expected.put("B", false);
		expected.put("C", false);
		
		final XdevHashtable<?, ?> actual = ParseUtils.parseHashtable(answer);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseHashtable(Object)}.
	 */
	@Test
	public void parseHashtable_2()
	{
		final XdevHashtable actual = ParseUtils.parseHashtable("XDEV Software");
		
		Assert.assertEquals(null, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseHashtable(Object)}.
	 */
	@Test
	public void parseHashtable_3()
	{
		final XdevHashtable actual = ParseUtils.parseHashtable(null);
		
		Assert.assertEquals(null, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseHashtable(Object)}.
	 */
	@Test
	public void parseHashtable_4()
	{
		final XdevHashtable<String, Boolean> expected = new XdevHashtable<>();
		expected.put("A", true);
		expected.put("B", false);
		expected.put("C", false);
		
		final Object[] array = {"A", true, "B", false, "C", false};
		
		final XdevHashtable<?, ?> actual = ParseUtils.parseHashtable(array);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseFile(Object)}.
	 */
	@Test
	public void parseFile_1()
	{
		final XdevFile actual = ParseUtils.parseFile("C:\\test.txt");
		
		Assert.assertEquals(null, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseFile(Object)}.
	 */
	@Test
	public void parseFile_2()
	{
		final XdevFile expected = new XdevFile("C:\\test.txt");
		final File f = new File("C:\\test.txt");
		
		final XdevFile actual = ParseUtils.parseFile(f);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseFile(Object)}.
	 */
	@Test
	public void parseFile_3()
	{
		final XdevFile expected = null;
		
		final XdevFile actual = ParseUtils.parseFile(null);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseImage(Object)}.
	 */
	@Test
	public void parseImage_1()
	{
		final XdevImage actual = ParseUtils.parseImage("C:\\logo.jpg");
		
		Assert.assertEquals(null, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parseImage(Object)}.
	 */
	@Test
	public void parseImage_2()
	{
		final XdevImage actual = ParseUtils.parseImage(null);
		
		Assert.assertEquals(null, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#castTo(Object, Class)}.
	 */
	@Test
	public void castTo_1()
	{
		final double expected = 1.2;
		final double num = 1.2;
		final Object actual = ParseUtils.castTo(num, Number.class);
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#castTo(Object, Class)}.
	 */
	@Test
	public void castTo_2()
	{
		final String expected = "XDEV";
		final Object actual = ParseUtils.castTo("XDEV", String.class);
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#castTo(Object, Class)}.
	 */
	@Test(expected = ClassCastException.class)
	public void castTo_3()
	{
		final double expected = 1.2;
		final double num = 1.2;
		final Object actual = ParseUtils.castTo(num, String.class);
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#isType(Object, Class)}.
	 */
	@Test
	public void isType_1()
	{
		Assert.assertTrue(ParseUtils.isType("XDEV-Software", String.class));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isType(Object, Class)}.
	 */
	@Test
	public void isType_2()
	{
		Assert.assertTrue(ParseUtils.isType(true, Boolean.class));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isType(Object, Class)}.
	 */
	@Test
	public void isType_3()
	{
		Assert.assertFalse(ParseUtils.isType(123, String.class));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isType(Object, Class)}.
	 */
	@Test
	public void isType_4()
	{
		Assert.assertFalse(ParseUtils.isType("XDEV-Software", Double.class));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isType(Object, String)}.
	 */
	@Test
	public void isType_5()
	{
		Assert.assertTrue(ParseUtils.isType(123.45, "java.lang.Double"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isType(Object, String)}.
	 */
	@Test
	public void isType_7()
	{
		Assert.assertFalse(ParseUtils.isType("XDEV-Software", "String"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isType(Object, String)}.
	 */
	@Test
	public void isType_8()
	{
		Assert.assertFalse(ParseUtils.isType("XDEV-Software", "double"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isType(Object, String)}.
	 */
	@Test
	public void isType_6()
	{
		Assert.assertFalse(ParseUtils.isType("XDEV-Software", "java.lang.Double"));
	}
	
	/**
	 * Test for Method {@link ParseUtils#isInteger(Object)}.
	 */
	@Test
	public void isInteger_1()
	{
		Assert.assertFalse(ParseUtils.isInteger(123.4));
	}
	
	/**
	 * Test for Method {@link ParseUtils#toBinaryString(long)}.
	 */
	@Test
	public void toBinaryString()
	{
		final long l = 192;
		final String expected = "11000000";
		final String actual = ParseUtils.toBinaryString(l);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#toHexString(long)}.
	 */
	@Test
	public void toHexString()
	{
		final long l = 170691;
		final String expected = "29ac3";
		final String actual = ParseUtils.toHexString(l);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#toOctalString(long)}.
	 */
	@Test
	public void toOctalString()
	{
		final long l = 170691;
		final String expected = "515303";
		final String actual = ParseUtils.toOctalString(l);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parse(Object)}.
	 */
	@Test
	public void parse_1() throws ParseException
	{
		final Number expected = 100;
		final Number actual = ParseUtils.parse("100");
		
		Assert.assertEquals(expected.doubleValue(), actual.doubleValue(), DOUBLE_DELTA);
	}
	
	/**
	 * Test for Method {@link ParseUtils#parse(Object)}.
	 */
	@Test
	public void parse_2() throws ParseException
	{
		final Number expected = 991.1;
		
		final Number actual = ParseUtils.parse(991.1);
		
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test for Method {@link ParseUtils##isNumeric(Object, long, long)}.
	 */
	@Test
	public void isNumeric()
	{
		Assert.assertFalse(ParseUtils.isNumeric("TEST", 1, 2));
	}
	
	/**
	 * Test for Method {@link ParseUtils##isNumeric(Object, double, double)}.
	 */
	@Test
	public void isNumeric_2()
	{
		Assert.assertFalse(ParseUtils.isNumeric("TEST", 1.0, 2.0));
	}
	
}
