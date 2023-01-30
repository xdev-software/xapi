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
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.Icon;

import xdev.io.XdevFile;
import xdev.lang.LibraryMember;
import xdev.ui.GraphicUtils;
import xdev.ui.XdevImage;


/**
 * 
 * <p>
 * The <code>ParseUtils</code> class provides parsing methods for various types.
 * </p>
 * 
 * @since 2.0
 * @author XDEV Software
 * 
 */
@LibraryMember
public class ParseUtils
{
	private ParseUtils()
	{
	}
	

	/**
	 * Parse the variable <code>value</code> to a <code>byte</code> .
	 * 
	 * <pre>
	 * ParseUtils.parseByte(7895);     returns -44
	 * ParseUtils.parseByte(-64568);   returns -56
	 * </pre>
	 * 
	 * @param value
	 *            the value which will be parsed to <code>byte</code>
	 * @return the <code>byte</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @throws NumberFormatException
	 *             if value could not be parsed to <code>byte</code>
	 * 
	 * @see ParseUtils#parseByte(Object, String)
	 * @see ParseUtils#parseDouble(Object)
	 * 
	 * @deprecated use {@link ConversionUtils#tobyte(Object)}
	 */
	@Deprecated
	public static byte parseByte(Object value) throws NumberFormatException
	{
		return (byte)parseDouble(value);
	}
	

	/**
	 * Parse the variable <code>value</code> to a <code>byte</code> with the
	 * help of of the <code>format</code>. The format of the current language
	 * setting will be considered.
	 * 
	 * <pre>
	 * ParseUtils.parseByte(589, "XDEV");        returns 77
	 * ParseUtils.parseByte(-3695, "XDEV");      returns -111
	 * </pre>
	 * 
	 * @param value
	 *            the value which will be parsed to <code>byte</code>
	 * @param pattern
	 *            the format with which the <code>value</code> should be parsed
	 * @return the <code>byte</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @see ParseUtils#parse(Object, String)
	 * 
	 * @throws ParseException
	 *             if value could not be parsed to <code>byte</code>
	 */
	public static byte parseByte(Object value, String pattern) throws ParseException
	{
		if(value instanceof Number)
		{
			return ((Number)value).byteValue();
		}
		
		try
		{
			return parse(value,pattern).byteValue();
		}
		catch(ParseException e)
		{
			throw new NumberFormatException(e.getMessage());
		}
	}
	

	/**
	 * Parse the variable <code>value</code> to a <code>short</code>.The format
	 * of the current language setting will be considered.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.parseShort(2561);      returns 2561
	 * ParseUtils.parseShort(-25615);    returns -25615
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param value
	 *            the value which will be parsed to <code>short</code>
	 * @return the <code>short</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @throws NumberFormatException
	 *             if value could not be parsed to <code>short</code>
	 * 
	 * @see ParseUtils#parseShort(Object, String)
	 * @see ParseUtils#parseDouble(Object)
	 * 
	 * @deprecated use {@link ConversionUtils#toshort(Object)}
	 */
	@Deprecated
	public static short parseShort(Object value) throws NumberFormatException
	{
		return (short)parseDouble(value);
	}
	

	/**
	 * Parse the variable <code>value</code> to a <code>short</code> with the
	 * help of of the <code>format</code>. The format of the current language
	 * setting will be considered.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.parseShort("0032767", "##0000");		returns 32767
	 * ParseUtils.parseShort("235.123", "###");			returns 235
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * 
	 * @param value
	 *            the value which will be parsed to <code>short</code>
	 * @param pattern
	 *            the format with which the <code>value</code> should be parsed
	 * @return the <code>short</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @see ParseUtils#parse(Object, String)
	 * 
	 * @throws ParseException
	 *             if value could not be parsed to <code>short</code>
	 */
	public static short parseShort(Object value, String pattern) throws ParseException
	{
		if(value instanceof Number)
		{
			return ((Number)value).shortValue();
		}
		
		try
		{
			return parse(value,pattern).shortValue();
		}
		catch(ParseException e)
		{
			throw new NumberFormatException(e.getMessage());
		}
	}
	

	/**
	 * Parse the variable <code>value</code> to an <code>int</code>. The format
	 * of the current language setting will be considered.
	 * 
	 * <blockquote><b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.parseInt(56);     returns 56
	 * ParseUtils.parseInt(-268);   returns -268
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param value
	 *            the value which will be parsed to <code>int</code>
	 * @return the <code>int</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 *         <p>
	 *         <strong>Warning:</strong> The value has to be between
	 *         -2.147.483.648 and 2.147.483.647. Beyond that use
	 *         {@link ParseUtils#parseDouble(Object)}.
	 *         </p>
	 * 
	 * @see ParseUtils#parseInt(Object, String)
	 * @see ParseUtils#parseDouble(Object)
	 * @see ParseUtils#parseDouble(Object, String)
	 * @see ParseUtils#isInteger(Object)
	 * @see ParseUtils#parseLong(Object)
	 * @see ParseUtils#parseLong(Object, String)
	 * 
	 * @throws NumberFormatException
	 *             if value could not be parsed to <code>int</code>
	 * 
	 * @deprecated use {@link ConversionUtils#toint(Object)}
	 */
	@Deprecated
	public static int parseInt(Object value) throws NumberFormatException
	{
		return (int)parseDouble(value);
	}
	

	/**
	 * Parse the variable <code>value</code> to a <code>int</code> with the help
	 * of of the <code>format</code>. The format of the current language setting
	 * will be considered.
	 * 
	 * <blockquote> <b> Examples </b></blockquote>
	 * 
	 * <pre>
	 * ParseUtils.parseInt("12.356", "##");		returns 12
	 * ParseUtils.parseInt("12", "##");			returns 12
	 * </pre>
	 * 
	 * 
	 * 
	 * @param value
	 *            the value which will be parsed to <code>int</code>
	 * @param pattern
	 *            the format with which the <code>value</code> should be parsed
	 * @return the <code>int</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 *         <p>
	 *         <strong>Warning:</strong> The value has to be between
	 *         -2.147.483.648 and 2.147.483.647. Beyond that use
	 *         {@link ParseUtils#parseDouble(Object, String)}.
	 *         </p>
	 * 
	 * @see ParseUtils#parse(Object, String)
	 * 
	 * @throws ParseException
	 *             if value could not be parsed to <code>int</code>
	 */
	public static int parseInt(Object value, String pattern) throws ParseException
	{
		if(value instanceof Number)
		{
			return ((Number)value).intValue();
		}
		
		try
		{
			return parse(value,pattern).intValue();
		}
		catch(ParseException e)
		{
			throw new NumberFormatException(e.getMessage());
		}
	}
	

	/**
	 * Parse the variable <code>value</code> to an <code>long</code>. The format
	 * of the current language setting will be considered.
	 * 
	 * <blockquote> <b> Examples </b> </blockquote>
	 * 
	 * <pre>
	 * ParseUtils.parseLong(223545854);    returns 223545854
	 * ParseUtils.parseLong(-545845854);   returns -545845854
	 * </pre>
	 * 
	 * 
	 * 
	 * @param value
	 *            the value which will be parsed to <code>long</code>
	 * @return the <code>long</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @see ParseUtils#parseInt(Object)
	 * @see ParseUtils#parseDouble(Object)
	 * @see ParseUtils#parseDouble(Object)
	 * @see ParseUtils#isLong(Object)
	 * @see ParseUtils#parseInt(Object, String)
	 * @see ParseUtils#parseLong(Object, String)
	 * @see ParseUtils#parseDouble(Object, String)
	 * 
	 * @throws NumberFormatException
	 *             if value could not be parsed to <code>long</code>
	 * 
	 * @deprecated use {@link ConversionUtils#tolong(Object)}
	 */
	@Deprecated
	public static long parseLong(Object value) throws NumberFormatException
	{
		return (long)parseDouble(value);
	}
	

	/**
	 * Parse the variable <code>value</code> to a <code>long</code> with the
	 * help of of the <code>format</code>. The format of the current language
	 * setting will be considered.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.parseLong("546545,64121.385", ",######");  	returns 546545
	 * ParseUtils.parseLong("546872313", "########");			returns 546872313
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param value
	 *            the value which will be parsed to <code>long</code>
	 * @param pattern
	 *            the format with which the <code>value</code> should be parsed
	 * @return the <code>long</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @see ParseUtils#parse(Object, String)
	 * 
	 * @throws ParseException
	 *             if value could not be parsed to <code>long</code>
	 */
	public static long parseLong(Object value, String pattern) throws ParseException
	{
		if(value instanceof Number)
		{
			return ((Number)value).longValue();
		}
		
		try
		{
			return parse(value,pattern).longValue();
		}
		catch(ParseException e)
		{
			throw new NumberFormatException(e.getMessage());
		}
	}
	

	/**
	 * Parse the variable <code>value</code> to an <code>float</code>. The
	 * format of the current language setting will be considered.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.parseFloat(545687453);   returns 545687453
	 * ParseUtils.parseFloat(-564868745);  returns -564868745
	 * 
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param value
	 *            the value which will be parsed to <code>long</code>
	 * @return the <code>float</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @see ParseUtils#parseInt(Object)
	 * @see ParseUtils#parseDouble(Object)
	 * @see ParseUtils#parseDouble(Object)
	 * @see ParseUtils#parseInt(Object, String)
	 * @see ParseUtils#parseLong(Object, String)
	 * @see ParseUtils#parseDouble(Object, String)
	 * 
	 * @throws NumberFormatException
	 *             if value could not be parsed to <code>float</code>
	 * 
	 * @deprecated use {@link ConversionUtils#tofloat(Object)}
	 */
	@Deprecated
	public static float parseFloat(Object value) throws NumberFormatException
	{
		return (float)parseDouble(value);
	}
	

	/**
	 * Parse the variable <code>value</code> to a <code>float</code> with the
	 * help of of the <code>format</code>. The format of the current language
	 * setting will be considered.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.parseFloat("00003456", "000000");		returns 3456
	 * ParseUtils.parseFloat("0000014", "000000");		returns 14
	 * 
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * 
	 * @param value
	 *            the value which will be parsed to <code>float</code>
	 * @param pattern
	 *            the format with which the <code>value</code> should be parsed
	 * @return the <code>float</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @see ParseUtils#parse(Object, String)
	 * 
	 * @throws ParseException
	 *             if value could not be parsed to <code>float</code>
	 */
	public static float parseFloat(Object value, String pattern) throws ParseException
	{
		if(value instanceof Number)
		{
			return ((Number)value).floatValue();
		}
		
		try
		{
			return parse(value,pattern).floatValue();
		}
		catch(ParseException e)
		{
			throw new NumberFormatException(e.getMessage());
		}
	}
	

	/**
	 * Parse the variable <code>value</code> to an <code>double</code>. The
	 * format of the current language setting will be considered.<blockquote>
	 * 
	 * <pre>
	 * ParseUtils.parseDouble(5.20);    returns 5.20
	 * ParseUtils.parseDouble(-175.28); returns -175.28
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param value
	 *            the value which will be parsed to <code>double</code>
	 * @return the <code>double</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @see ParseUtils#parseInt(Object, String)
	 * @see ParseUtils#parseDouble(Object)
	 * @see ParseUtils#parseDouble(Object, String)
	 * @see ParseUtils#isDecimal(Object)
	 * @see ParseUtils#parseLong(Object)
	 * @see ParseUtils#parseLong(Object, String)
	 * 
	 * @throws NumberFormatException
	 *             if value could not be parsed to <code>double</code>
	 * 
	 * @deprecated use {@link ConversionUtils#todouble(Object)}
	 */
	@Deprecated
	public static double parseDouble(Object value) throws NumberFormatException
	{
		if(value instanceof Number)
		{
			return ((Number)value).doubleValue();
		}
		
		String s = String.valueOf(value);
		
		try
		{
			return Double.parseDouble(s);
		}
		catch(Exception e)
		{
			try
			{
				return getNumberFormat().parse(s).doubleValue();
			}
			catch(ParseException pe)
			{
				throw new NumberFormatException(e.getMessage());
			}
		}
	}
	

	/**
	 * Parse the variable <code>value</code> to a <code>double</code> with the
	 * help of of the <code>format</code>. The format of the current language
	 * setting will be considered.
	 * 
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.parseDouble(",35", ".00");       returns 0.35
	 * ParseUtils.parseDouble("12,340000","#.00"); returns 12.34
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * 
	 * @param value
	 *            the value which will be parsed to <code>double</code>
	 * @param pattern
	 *            the format with which the <code>value</code> should be parsed
	 * @return the <code>double</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @see ParseUtils#parse(Object, String)
	 * 
	 * @throws ParseException
	 *             if value could not be parsed to <code>double</code>
	 */
	public static double parseDouble(Object value, String pattern) throws ParseException
	{
		if(value instanceof Number)
		{
			return ((Number)value).doubleValue();
		}
		
		try
		{
			return parse(value,pattern).doubleValue();
		}
		catch(ParseException e)
		{
			throw new NumberFormatException(e.getMessage());
		}
	}
	

	/**
	 * Parse the variable <code>value</code> to an <code>double</code>.
	 * Additional characters behind the <code>value</code> will be
	 * considered.<blockquote>
	 * 
	 * 
	 * <b> Example </b>
	 * 
	 * <pre>
	 * ParseUtils.parseDouble(2587.6998);    returns 2587.6998
	 * ParseUtils.parseDouble(-5697.6237);   returns -5697.6237
	 * </pre>
	 * 
	 * <b>Hint</b>
	 * 
	 * <pre>
	 * Can be used as inverse function to {@link ParseUtils#formatAsCurrency(double)}
	 * </pre>
	 * 
	 * <b>Hint</b>
	 * 
	 * <pre>
	 * If you don't need the extra function to accepted additional characters 
	 * behind the number you should use {@link ParseUtils#parseDouble(Object)} instead.
	 * Because fewer mistakes could be slipped in. 
	 * And {@link ParseUtils#parseDouble(Object)} has more performance because fewer cases has to be checked.
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param value
	 *            the value which will be parsed to <code>double</code>
	 * @return the <code>double</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @see ParseUtils#formatAsCurrency(double)
	 * @see ParseUtils#parseDoubleAsCurrency(Object, String)
	 * @see ParseUtils#parseDouble(Object)
	 * @see ParseUtils#parseDouble(Object, String)
	 * 
	 */
	public static double parseDoubleAsCurrency(Object value)
	{
		if(value instanceof Number)
		{
			return ((Number)value).doubleValue();
		}
		
		String s = String.valueOf(value);
		
		try
		{
			return NumberFormat.getCurrencyInstance().parse(s).doubleValue();
		}
		catch(ParseException pe)
		{
			throw new NumberFormatException(pe.getMessage());
		}
	}
	

	/**
	 * Parse the variable <code>value</code> to a <code>double</code> with the
	 * help of of the <code>format</code>. The format of the current language
	 * setting will be considered.
	 * 
	 * <b> Example </b>
	 * 
	 * <pre>
	 * ParseUtils.parseDoubleAsCurrency("12,350000", "#.000000");		returns 12.35
	 * ParseUtils.parseDoubleAsCurrency("12.345,679", ",###");          returns 12345.679
	 * </pre>
	 * 
	 * 
	 * @param value
	 *            the value which will be parsed to <code>double</code>
	 * @param pattern
	 *            the format with which the <code>value</code> should be parsed
	 * @return the <code>double</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @see ParseUtils#parse(Object, String)
	 * @see ParseUtils#parseDouble(Object, String)
	 * 
	 * @throws ParseException
	 *             if value could not be parsed to <code>double</code>
	 */
	public static double parseDoubleAsCurrency(Object value, String pattern) throws ParseException
	{
		return parseDouble(value,pattern);
	}
	

	/**
	 * Parse the variable <code>value</code> to an <code>Number</code> .
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.parse(78);    return 78
	 * ParseUtils.parse(-256);  return -256
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param value
	 *            the value which will be parsed to <code>Number</code>
	 * @return the <code>Number</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * 
	 * 
	 * @throws ParseException
	 *             if value could not be parsed to <code>Number</code>
	 * 
	 */
	public static Number parse(Object value) throws ParseException
	{
		if(value instanceof Number)
		{
			return (Number)value;
		}
		
		return getNumberFormat().parse(String.valueOf(value));
	}
	

	/**
	 * Parse the variable <code>value</code> to an <code>Number</code> with the
	 * help of of the <code>format</code>. The format of the current language
	 * setting will be considered.
	 * 
	 * <blockquote>
	 * 
	 * Examples
	 * 
	 * <pre>
	 * 
	 * ParseUtils.parse("00000014", "00000000");		returns 14
	 * ParseUtils.parse("0014000", "0000000");			returns 14000
	 * </pre>
	 * 
	 * The following Symbols will be supported:<br>
	 * <table width="100%" cellspacing="1" cellpadding="3">
	 * <br>
	 * <tr>
	 * <th bgcolor="#BABAFF" align="left" valign="top"><b>Symbol</b><br>
	 * </th>
	 * <th bgcolor="#BABAFF" align="left" valign="top"><b>Meaning</b><br>
	 * </th>
	 * </tr>
	 * <tr>
	 * <td valign="top"><span class="listing">0</span><br>
	 * </td>
	 * <td valign="top">Represent a digit - is the position empty, will be shown
	 * a zero.<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><span class="listing">#</span><br>
	 * </td>
	 * <td valign="top">Represent a digit - is the position empty it stays
	 * empty, with that leading zero and unnecessary zeros after the decimal
	 * point will be avoided.<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><span class="listing">.</span><br>
	 * </td>
	 * <td valign="top">Decimal separator. Separates external and internal
	 * decimal places.<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><span class="listing">,</span><br>
	 * </td>
	 * <td valign="top">Groups the digits (a group is as big as the distance
	 * from "," to ".").<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><span class="listing">;</span><br>
	 * </td>
	 * <td valign="top">Delimiter. On the left of it is the pattern for the
	 * positive, on the right the format for the negative numbers<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><span class="listing">-</span><br>
	 * </td>
	 * <td valign="top">The standardsymbol for the negativeprefix<br>
	 * </td>
	 * </tr>
	 * <br>
	 * <tr>
	 * <td valign="top"><span class="listing">%</span><br>
	 * </td>
	 * <td valign="top">The number will be multiply with 100 and declared as a
	 * percent value<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><span class="listing">%%</span><br>
	 * </td>
	 * <td valign="top">The same as %, only with per mill.<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><span class="listing">\u00A4 (¤)</span><br>
	 * <br>
	 * </td>
	 * <td valign="top">National currency sign (&eur; for Germany)<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><span class="listing">\u00A4\u00A4 (¤¤)</span><br>
	 * </td>
	 * <td valign="top">Internationale currency sign (EUR for Germany)<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><span class="listing">X</span><br>
	 * </td>
	 * <td valign="top">All other symbols <span class="listing">X</span> can be
	 * used as normal.<br>
	 * <br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><span class="listing">'</span><br>
	 * </td>
	 * <td valign="top">to mask special symbols in prefix or suffix<br>
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * <table width="100%" cellspacing="1" cellpadding="3">
	 * <br>
	 * <tr>
	 * <th bgcolor="#BABAFF" align="left" valign="top"><b>Format</b><br>
	 * </th>
	 * <th bgcolor="#BABAFF" align="left" valign="top"><b>Intake Number</b><br>
	 * </th>
	 * <th bgcolor="#BABAFF" align="left" valign="top"><b>Result</b><br>
	 * </th>
	 * </tr>
	 * <br>
	 * <tr>
	 * <td valign="top">0000<br>
	 * </td>
	 * <td valign="top">12<br>
	 * </td>
	 * <td valign="top">0012<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">0000<br>
	 * </td>
	 * <td valign="top">12,5<br>
	 * </td>
	 * <td valign="top">0012<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">0000<br>
	 * </td>
	 * <td valign="top">1234567<br>
	 * </td>
	 * <td valign="top">1234567<br>
	 * <br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">##<br>
	 * </td>
	 * <td valign="top">12<br>
	 * </td>
	 * <td valign="top">12<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">##<br>
	 * </td>
	 * <td valign="top">12.3456<br>
	 * </td>
	 * <td valign="top">12<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">##<br>
	 * </td>
	 * <td valign="top">123456<br>
	 * </td>
	 * <td valign="top">123456<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">.00<br>
	 * </td>
	 * <td valign="top">12.3456<br>
	 * </td>
	 * <td valign="top">12,35<br>
	 * </td>
	 * </tr>
	 * <br>
	 * <tr>
	 * <td valign="top">.00<br>
	 * </td>
	 * <td valign="top">.3456<br>
	 * </td>
	 * <td valign="top">,35<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">0.00<br>
	 * </td>
	 * <td valign="top">.789<br>
	 * </td>
	 * <td valign="top">0,79<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">#.000000<br>
	 * </td>
	 * <td valign="top">12.34<br>
	 * </td>
	 * <td valign="top">12,340000<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">,###<br>
	 * </td>
	 * <td valign="top">12345678.901<br>
	 * </td>
	 * <td valign="top">12.345.679<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">#.#;(#.#)<br>
	 * <br>
	 * </td>
	 * <td valign="top">12345678.901<br>
	 * </td>
	 * <td valign="top">12345678,9<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">#.#;(#.#)<br>
	 * </td>
	 * <td valign="top">-12345678.901<br>
	 * </td>
	 * <td valign="top">(12345678,9)<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">,###.## \u00A4 <br>
	 * </td>
	 * <td valign="top">12345.6789<br>
	 * </td>
	 * <td valign="top">12.345,68 i<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">,#00.00 \u00A4\u00A4<br>
	 * </td>
	 * <td valign="top">-12345678.9<br>
	 * </td>
	 * <td valign="top">-12.345.678,90 EUR<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top">,#00.00 \u00A4\u00A4<br>
	 * </td>
	 * <td valign="top">0.1<br>
	 * <br>
	 * </td>
	 * <td valign="top">00,10 EUR<br>
	 * </td>
	 * </tr>
	 * </table>
	 * </blockquote>
	 * 
	 * @param value
	 *            the value which will be parsed to <code>Number</code>
	 * @param pattern
	 *            the format with which the <code>value</code> should be parsed
	 * @return the <code>Number</code> parsed from the <code>value</code>, if
	 *         possible.
	 * 
	 * @see ParseUtils#parse(Object)
	 * @see ParseUtils#parseInt(Object)
	 * @see ParseUtils#parseInt(Object, String)
	 * @see ParseUtils#isDecimal(Object)
	 * 
	 * @throws ParseException
	 *             if <code>value</code> could not be parsed to
	 *             <code>Number</code>
	 */
	public static Number parse(Object value, String pattern) throws ParseException
	{

		// Bug 13349. Numbers was never parsed.
//		if(value instanceof Number)
//		{
//			return (Number)value;
//		}
		
		return new DecimalFormat(pattern).parse(String.valueOf(value));
	}
	

	/**
	 * Parse the variable <code>value</code> to <code>boolean</code>
	 * .<blockquote>
	 * 
	 * <pre>
	 * Returns <code>true</code> for the following cases:<br>
	 * <ul><li><b>Boolean</b> <i>true</i></li><br>
	 * <li><b>Number</b> != 0</li><br>
	 * <li><b><code>String</code></b> "true" (not case sensitive)</li></ul><br>
	 * All others cases != <code>null</code> return <code>false</code>
	 * </pre>
	 * 
	 * </blockquote> <b> Example </b>
	 * 
	 * <pre>
	 * ParseUtils.parseBoolean(232);      return true
	 * ParseUtils.parseBoolean(-232);     return true
	 * ParseUtils.parseBoolean("hello");  return false
	 * </pre>
	 * <p>
	 * <strong>Warning:</strong> Instead of {@link ParseUtils#parseInt(Object)}
	 * and {@link ParseUtils#parseDouble(Object)} this methode will never throw
	 * a <code>NumberFormatException</code>
	 * 
	 * @param value
	 *            the value which will be parsed to <code>boolean</code>.
	 * @return <code>true</code> if the <code>value</code> is a
	 *         <code>boolean</code>, else <code>false</code>
	 * 
	 * @see ParseUtils#isBoolean(Object)
	 * @see ParseUtils#parseInt(Object)
	 * @see ParseUtils#parseDouble(Object)
	 * 
	 * @deprecated use {@link ConversionUtils#toboolean(Object)}
	 */
	@Deprecated
	public static boolean parseBoolean(Object value)
	{
		boolean result = false;
		
		if(value instanceof Boolean)
		{
			result = ((Boolean)value).booleanValue();
		}
		else
		{
			try
			{
				result = parseDouble(value) != 0;
			}
			catch(Exception e)
			{
				result = Boolean.valueOf(String.valueOf(value)).booleanValue();
			}
		}
		
		return result;
	}
	

	/**
	 * Ckecks whether <code>Object</code> <code>value</code> is a
	 * <code>XdevDate</code>. If yes it returns a <code>XdevDate</code> else
	 * <code>null</code>.
	 * 
	 * @param value
	 *            <code>Object</code> which will be parsed to
	 *            <code>XdevDate</code>.
	 * @return <code>XdevDate</code> if <code>value</code> is a
	 *         <code>XdevDate</code>. Else <code>null</code>.
	 * 
	 * @see ParseUtils#parseDate(Object, String)
	 * @see ParseUtils#parseUtilDate(Object)
	 * 
	 * @deprecated use {@link ConversionUtils#toXdevDate(Object)}
	 */
	@Deprecated
	public static XdevDate parseDate(Object value)
	{
		if(value == null)
		{
			return null;
		}
		
		XdevDate date = null;
		
		if(value instanceof XdevDate)
		{
			date = (XdevDate)value;
		}
		else if(value instanceof Date)
		{
			date = new XdevDate((Date)value);
		}
		else if(value instanceof Calendar)
		{
			date = new XdevDate((Calendar)value);
		}
		
		if(date != null)
		{
			date = date.copy();
		}
		
		return date;
	}
	

	/**
	 * Ckecks whether <code>Object</code> <code>value</code> is a
	 * <code>XdevDate</code>. If yes it returns a <code>XdevDate</code> else
	 * <code>null</code>.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.parseDate("10.01.2010", "dd.MM.yyyy");  returns 10.01.2010
	 * </pre>
	 * 
	 * <b>Hint</b>
	 * 
	 * <pre>
	 * 	For valid patterns see {@link SimpleDateFormat}
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param value
	 *            <code>Object</code> which will be parsed to
	 *            <code>XdevDate</code>.
	 * @param pattern
	 *            the format with which the <code>value</code> should be parsed.
	 * @return <code>XdevDate</code> if <code>value</code> is a
	 *         <code>XdevDate</code>. Else <code>null</code>.
	 * 
	 * @see ParseUtils#parseDate(Object)
	 * @see ParseUtils#parseUtilDate(Object)
	 */
	public static XdevDate parseDate(Object value, String pattern)
	{
		if(value == null)
		{
			return null;
		}
		
		DateFormat dateFormat = null;
		
		XdevDate date = parseDate(value);
		
		if(date == null)
		{
			String dateStr = String.valueOf(value);
			
			try
			{
				dateFormat = new SimpleDateFormat(pattern);
				Date d = dateFormat.parse(dateStr);
				date = new XdevDate(d);
			}
			catch(ParseException e)
			{
				throw new DateFormatException(e.getMessage());
			}
		}
		
		date.setFormat(dateFormat);
		
		return date;
	}
	

	/**
	 * Ckecks whether <code>Object</code> <code>value</code> is a
	 * <code>Date</code>. If yes it returns a <code>Date</code> else
	 * <code>null</code>.
	 * 
	 * @param value
	 *            <code>Object</code> which will be parsed to <code>Date</code>.
	 * @return <code>XdevDate</code> if <code>value</code> is a
	 *         <code>Date</code>. Else <code>null</code>.
	 * 
	 * @see ParseUtils#parseDate(Object, String)
	 * @see ParseUtils#parseDate(Object)
	 * 
	 * @deprecated use {@link ConversionUtils#toDate(Object)}
	 */
	@Deprecated
	public static Date parseUtilDate(Object value)
	{
		if(value == null)
		{
			return null;//
		}
		
		Date date = null;
		
		if(value instanceof Date)
		{
			date = (Date)value;
		}
		else if(value instanceof Calendar)
		{
			date = ((Calendar)value).getTime();
		}
		
		return date;
	}
	

	/**
	 * Ckecks whether <code>Object</code> <code>value</code> is a
	 * <code>XdevList</code>. If yes it returns a <code>XdevList</code> else
	 * <code>null</code>.
	 * 
	 * @param value
	 *            <code>Object</code> which will be parsed to
	 *            <code>XdevList</code>.
	 * @return <code>XdevDate</code> if <code>value</code> is a
	 *         <code>XdevList</code>. Else <code>null</code>.
	 * 
	 * @deprecated use {@link ConversionUtils#toXdevList(Object)}
	 */
	@Deprecated
	public static XdevList parseList(Object value)
	{
		if(value == null)
		{
			return null;
		}
		
		XdevList list = null;
		
		if(value instanceof XdevList)
		{
			list = (XdevList)value;
		}
		else if(value instanceof Collection)
		{
			list = new XdevList((Collection)value);
		}
		else if(value.getClass().isArray())
		{
			int c = Array.getLength(value);
			list = new XdevList(c);
			for(int i = 0; i < c; i++)
			{
				list.add(Array.get(value,i));
			}
		}
		
		return list;
	}
	

	/**
	 * Ckecks whether <code>Object</code> <code>value</code> is a
	 * <code>XdevHashtable</code>. If yes it returns a
	 * <code>XdevHashtable</code> else <code>null</code>.
	 * 
	 * @param value
	 *            <code>Object</code> which will be parsed to
	 *            <code>XdevHashtable</code>.
	 * @return <code>XdevHashtable</code> if <code>value</code> is a
	 *         <code>XdevHashtable</code>. Else <code>null</code>.
	 * 
	 * @deprecated use {@link ConversionUtils#toXdevHashtable(Object)}
	 */
	@Deprecated
	public static XdevHashtable parseHashtable(Object value)
	{
		if(value == null)
		{
			return null;
		}
		
		XdevHashtable hashtable = null;
		
		if(value instanceof XdevHashtable)
		{
			hashtable = (XdevHashtable)value;
		}
		else if(value instanceof Map)
		{
			hashtable = new XdevHashtable((Map)value);
		}
		else if(value.getClass().isArray() && Array.getLength(value) % 2 == 0)
		{
			hashtable = new XdevHashtable((Object[])value);
		}
		
		return hashtable;
	}
	

	/**
	 * Ckecks whether <code>Object</code> <code>value</code> is a
	 * <code>XdevFile</code>. If yes it returns a <code>XdevFile</code> else
	 * <code>null</code>.
	 * 
	 * @param value
	 *            <code>Object</code> which will be parsed to
	 *            <code>XdevFile</code>.
	 * @return <code>XdevFile</code> if <code>value</code> is a
	 *         <code>XdevFile</code>. Else <code>null</code>.
	 * 
	 * @deprecated use {@link ConversionUtils#toXdevFile(Object)}
	 */
	@Deprecated
	public static XdevFile parseFile(Object value)
	{
		if(value == null)
		{
			return null;
		}
		
		XdevFile file = null;
		
		if(value instanceof XdevFile)
		{
			file = (XdevFile)value;
		}
		else if(value instanceof File)
		{
			file = new XdevFile((File)value);
		}
		else if(value instanceof URI)
		{
			file = new XdevFile((URI)value);
		}
		else if(value instanceof URL)
		{
			try
			{
				file = new XdevFile(((URL)value).toURI());
			}
			catch(URISyntaxException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return file;
	}
	

	/**
	 * Ckecks whether <code>Object</code> <code>value</code> is a
	 * <code>XdevImage</code>. If yes it returns a <code>XdevImage</code> else
	 * <code>null</code>.
	 * 
	 * @param value
	 *            <code>Object</code> which will be parsed to
	 *            <code>XdevImage</code>.
	 * @return <code>XdevImage</code> if <code>value</code> is a
	 *         <code>XdevImage</code>. Else <code>null</code>.
	 * 
	 * @deprecated use {@link ConversionUtils#toXdevImage(Object)}
	 */
	@Deprecated
	public static XdevImage parseImage(Object value)
	{
		if(value == null)
		{
			return null;
		}
		
		XdevImage image = null;
		
		if(value instanceof XdevImage)
		{
			image = (XdevImage)value;
		}
		else if(value instanceof Image)
		{
			image = new XdevImage((Image)value);
		}
		else if(value instanceof Icon)
		{
			image = new XdevImage(GraphicUtils.createImageFromIcon((Icon)value));
		}
		
		return image;
	}
	

	/**
	 * Parse the variable <code>i</code> to <code>HexString</code>. <blockquote>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.toHexString(25);     returns 19
	 * ParseUtils.toHexString(-62);    returns ffffffc2
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param i
	 *            <code>int</code> which will be parsed to
	 *            <code>HexString</code>
	 * @return String HexSring parsed from <code>i</code>
	 * 
	 * @see Integer#toHexString(int)
	 */
	public static String toHexString(int i)
	{
		return Integer.toHexString(i);
	}
	

	/**
	 * Parse the variable <code>l</code> to <code>HexString</code>.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.toHexString(-25897);      returns ffff9ad7
	 * ParseUtils.toHexString(2589);        returns a1d
	 * 
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param l
	 *            <code>long</code> which will be parsed to
	 *            <code>HexString</code>
	 * @return String HexSring parsed from <code>l</code>
	 * 
	 * @see Long#toHexString(long)
	 */
	public static String toHexString(long l)
	{
		return Long.toHexString(l);
	}
	

	/**
	 * Parse the variable <code>i</code> to <code>OctalString</code>.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.toOctalString(597632);       returns 2217200
	 * ParseUtils.toOctalString(-1025897);     returns 37774054227
	 * 
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param i
	 *            <code>int</code> which will be parsed to
	 *            <code>OctalString</code>
	 * @return String OctalString parsed from <code>i</code>
	 * 
	 * @see Integer#toOctalString(int)
	 */
	public static String toOctalString(int i)
	{
		return Integer.toOctalString(i);
	}
	

	/**
	 * Parse the variable <code>l</code> to <code>OctalString</code>.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.toOctalString(1025856889);       returns 7511252571
	 * ParseUtils.toOctalString(-589789567);       returns 33466103201
	 * 
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param l
	 *            <code>long</code> which will be parsed to
	 *            <code>OctalString</code>
	 * @return String OctalString parsed from <code>l</code>
	 * 
	 * @see Long#toOctalString(long)
	 */
	public static String toOctalString(long l)
	{
		return Long.toOctalString(l);
	}
	

	/**
	 * Parse the variable <code>i</code> to <code>BinaryString</code>.
	 * 
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.toBinaryString(-589789567);      returns 11011100110110001000011010000001
	 * ParseUtils.toBinaryString(897898967);       returns 110101100001001101100111010111
	 * 
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param i
	 *            <code>int</code> which will be parsed to
	 *            <code>BinaryString</code>
	 * @return String BinaryString parsed from <code>i</code>
	 * 
	 * @see Integer#toBinaryString(int)
	 */
	public static String toBinaryString(int i)
	{
		return Integer.toBinaryString(i);
	}
	

	/**
	 * Parse the variable <code>l</code> to <code>BinaryString</code>.
	 * 
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.toBinaryString(897295967);       returns 110101011110111010011001011111
	 * ParseUtils.toBinaryString(-831878067);      returns 11001110011010101000110001001101
	 * 
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param l
	 *            <code>long</code> which will be parsed to
	 *            <code>BinaryString</code>
	 * @return String BinaryString parsed from <code>l</code>
	 * 
	 * @see Long#toBinaryString(long)
	 */
	public static String toBinaryString(long l)
	{
		return Long.toBinaryString(l);
	}
	

	/**
	 * Checks if the variable <code>value</code> is a value between
	 * <code>min</code> and <code>max</code>. With the condition:
	 * <code>min</code> <= <code>value</code> <= <code>max</code>.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.isNumeric(489999995, 12000, 698777);     returns false
	 * ParseUtils.isNumeric(5000, 5000, 5000);             returns true
	 * 
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param value
	 *            the value which will be checked
	 * @param min
	 *            the(inclusive)lower border for <code>value</code>
	 * @param max
	 *            the(inclusive)upper border for <code>value</code>
	 * @return true if <code>min</code> <= <code>value</code> <=
	 *         <code>max</code>
	 * 
	 * @see ParseUtils#isAlphaNumeric(String, int, int)
	 * @see ParseUtils#isNumeric(Object, double, double)
	 */
	public static boolean isNumeric(Object value, long min, long max)
	{
		try
		{
			long l = parseLong(value);
			return l >= min && l <= max;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * * Checks if the variable <code>value</code> is a value between
	 * <code>min</code> and <code>max</code>. With the condition:
	 * <code>min</code> <= <code>value</code> <= <code>max</code>.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * ParseUtils.isNumeric(5600.80, 500.20, 4000.90);     returns false
	 * ParseUtils.isNumeric(-5000.90, -489.25, -800.65);   returns false
	 * ParseUtils.isNumeric(-5000.90, -5000.90, -5000.90); returns true
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param value
	 *            the value which will be checked
	 * @param min
	 *            the(inclusive)lower border for <code>value</code>
	 * @param max
	 *            the(inclusive)upper border for <code>value</code>
	 * @return true if <code>min</code> <= <code>value</code> <=
	 *         <code>max</code>
	 * 
	 * @see ParseUtils#isAlphaNumeric(String, int, int)
	 * @see ParseUtils#isNumeric(Object, long, long)
	 */
	public static boolean isNumeric(Object value, double min, double max)
	{
		try
		{
			double d = parseDouble(value);
			return d >= min && d <= max;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Checks if the variable <code>String</code> is not a number and is it has
	 * a length between <code>minLen</code> and <code>maxLen</code>.
	 * 
	 * If this applies it returns <code>true</code>, else <code>false</code>.
	 * 
	 * <blockquote> <b> Example </b>
	 * 
	 * <pre>
	 * ParseUtils.isAlphaNumeric("Hello10", 2, 30);         returns true
	 * ParseUtils.isAlphaNumeric("Hello-15", -40, 30);      returns true
	 * ParseUtils.isAlphaNumeric("Hello XDEV3", -40, -15);  returns false
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param value
	 *            the <code>String</code> which length will be checked
	 * @param min
	 *            the(inclusive)minimum length for <code>value</code>
	 * @param max
	 *            the(inclusive)maximum length for <code>value</code>
	 * @return true if <code>minLen</code> <= <code>value.length</code> <=
	 *         <code>maxLen</code>
	 * 
	 * @see ParseUtils#isNumeric(Object, double, double)
	 * @see ParseUtils#isNumeric(Object, long, long)
	 */
	public static boolean isAlphaNumeric(String value, int min, int max)
	{
		try
		{
			Double.valueOf(value);
		}
		catch(NumberFormatException e)
		{
			int anl = value.length();
			if(anl >= min && anl <= max)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private final static String	EMAIL_REGEX	= "^[a-zA-Z0-9._%-]+@[a-zA-ZZÄÖÜäöü0-9._%-]+\\.[a-zA-Z]{2,4}$";
	

	/**
	 * Checks if the variable <code>String</code> is a valid Email address.
	 * 
	 * <blockquote> <b> Example </b>
	 * 
	 * <pre>
	 * ParseUtils.isEmail("a.bau@xabc-software.de"); returns true
	 * ParseUtils.isEmail(" ");                              returns false
	 * </pre>
	 * 
	 * </blockquote> <b>Hint</b>
	 * 
	 * <pre>
	 * It can only be checked whether the String has the right format for an email address, not whether this email address truely exists.
	 * </pre>
	 * 
	 * 
	 * @param value
	 *            the <code>String</code> which format will be checked
	 * @return true if the String has a valid email address format.
	 * 
	 * @see ParseUtils#isNumeric(Object, double, double)
	 * @see ParseUtils#isNumeric(Object, long, long)
	 */
	public static boolean isEmail(String value)
	{
		return Pattern.compile(EMAIL_REGEX).matcher(value).matches();
	}
	

	/**
	 * Formats the <code>long</code> <code>l</code> with the actual language
	 * setting and returns it as <code>String</code>.
	 * 
	 * <blockquote> <b> Example </b>
	 * 
	 * <pre>
	 * ParseUtils.format(894302256);   returns 894.302.256
	 * ParseUtils.format(-789602314);  returns -789.602.314
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * <b></b>
	 * 
	 * <pre>
	 * The german standard setting for decimals is:<br>
	 * <ul><li>Thousand delimiter "."</li><br>
	 * <li>decimal point ","</li><br>
	 * <li>round to 3 decimal places </li></ul>
	 * </pre>
	 * 
	 * 
	 * @param l
	 *            the <code>long</code> which will be formated.
	 * @return <code>long</code> in format of the actual language setting.
	 * 
	 * @see ParseUtils#format(double)
	 * @see ParseUtils#format(long, String)
	 */
	public static String format(long l)
	{
		return getNumberFormat().format(l);
	}
	

	/**
	 * Formates the number <code>l</code> to a <code>String</code> with the help
	 * of the <code>format</code>.
	 * 
	 * <blockquote> <b> Example </b>
	 * 
	 * <pre>
	 * ParseUtils.format(789302125, "Lange Zahl ");     returns Lange Zahl 789302125
	 * ParseUtils.format(-569780212, "Negative Zahl "); returns -Negative Zahl 569780212
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * <b>Hint</b>
	 * 
	 * <pre>
	 * for valid patterns see {@link ParseUtils#parse(Object, String)}
	 * </pre>
	 * 
	 * 
	 * @param l
	 *            the <code>long</code> which will be formated.
	 * @param pattern
	 *            the <code>String</code> with which <code>l</code> will be
	 *            formated.
	 * @return <code>l</code> formated with pattern <code>pattern</code>.
	 * 
	 * @see ParseUtils#format(long)
	 * @see ParseUtils#format(double, String)
	 */
	public static String format(long l, String pattern)
	{
		pattern = pattern.replace('\\','\u2030');
		pattern = pattern.replace('_','\u00A4');
		
		return new DecimalFormat(pattern).format(l);
	}
	

	/**
	 * Formats the <code>double</code> <code>d</code> with the actual language
	 * setting and returns it as <code>String</code>.
	 * 
	 * <blockquote> <b> Example </b>
	 * 
	 * <pre>
	 * 
	 * ParseUtils.format(2000.3505);       returns 2.000,35
	 * ParseUtils.format(-23205.3589705);  returns -23.205,359
	 * 
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * <b></b>
	 * 
	 * <pre>
	 * The german standard setting for decimals is:<br>
	 * <ul><li>Thousand delimiter "."</li><br>
	 * <li>decimal point ","</li><br>
	 * <li>round to 3 decimal places </li></ul>
	 * </pre>
	 * 
	 * 
	 * @param d
	 *            the <code>double</code> which will be formated.
	 * @return <code>double</code> in format of the actual language setting.
	 * 
	 * @see ParseUtils#format(long)
	 * @see ParseUtils#format(double, String)
	 */
	public static String format(double d)
	{
		return getNumberFormat().format(d);
	}
	

	/**
	 * Formates the number <code>l</code> to a <code>String</code> with the help
	 * of the <code>format</code>.
	 * 
	 * <b>Example</b>
	 * 
	 * <pre>
	 * ParseUtils.format(-7892.0365478, "Euro");    returns -Euro7892
	 * ParseUtils.format(25897.6321, "hello");      returns hello25898
	 * 
	 * </pre>
	 * 
	 * 
	 * @param d
	 *            the <code>long</code> which will be formated.
	 * @param pattern
	 *            the <code>String</code> with which <code>l</code> will be
	 *            formated.
	 * @return <code>l</code> formated with pattern <code>pattern</code>.
	 * 
	 * @see ParseUtils#format(long, String)
	 * @see ParseUtils#format(double)
	 */
	public static String format(double d, String pattern)
	{
		pattern = pattern.replace('\\','\u2030');
		pattern = pattern.replace('_','\u00A4');
		
		return new DecimalFormat(pattern).format(d);
	}
	

	/**
	 * Formates the <code>long</code> <code>l</code> as currency with the actual
	 * language setting and returns it as <code>String</code>.
	 * 
	 * <blockquote> <b> Example </b>
	 * 
	 * <pre>
	 * 
	 * ParseUtils.formatAsCurrency(897565258);     returns 897.565.258,00 €
	 * ParseUtils.formatAsCurrency(-45752378);     returns -45.752.378,00 €
	 * 
	 * </pre>
	 * 
	 * </blockquote> <b>Hint</b>
	 * 
	 * <pre>
	 * The german default setting for currency is:<br>
	 * <ul><li>Thousand delimiter "."</li><br>
	 * <li>decimal point ","</li><br>
	 * <li>round to 2 decimal places </li><br>
	 * <li>closing "€"-sign with preceding space</li></ul>
	 * </pre>
	 * 
	 * 
	 * @param l
	 *            the <code>long</code> which will be formated as currency
	 * @return the <code>long</code> formated as currency with the actual
	 *         language setting.
	 * 
	 * @see ParseUtils#format(double)
	 * @see ParseUtils#format(long, String)
	 * @see ParseUtils#parseDouble(Object)
	 * @see ParseUtils#parseDouble(Object, String)
	 */
	public static String formatAsCurrency(long l)
	{
		return NumberFormat.getCurrencyInstance().format(l);
	}
	

	/**
	 * Formates the <code>double</code> <code>d</code> as currency with the
	 * actual language setting and returns it as <code>String</code>.
	 * 
	 * <blockquote> <b> Example </b>
	 * 
	 * <pre>
	 * ParseUtils.formatAsCurrency(78963.256);     returns 78.963,26 €
	 * ParseUtils.formatAsCurrency(-458.256);      returns -458,26 €
	 * 
	 * </pre>
	 * 
	 * </blockquote> <b>Hint</b>
	 * 
	 * <pre>
	 * The german default setting for currency is:<br>
	 * <ul><li>Thousand delimiter "."</li><br>
	 * <li>decimal point ","</li><br>
	 * <li>round to 2 decimal places </li><br>
	 * <li>closing "€"-sign with preceding space</li></ul>
	 * </pre>
	 * 
	 * 
	 * @param d
	 *            the <code>double</code> which will be formated as currency
	 * @return the <code>double</code> formated as currency with the actual
	 *         language setting.
	 * 
	 * @see ParseUtils#format(double)
	 * @see ParseUtils#format(long, String)
	 * @see ParseUtils#parseDouble(Object)
	 * @see ParseUtils#parseDouble(Object, String)
	 */
	public static String formatAsCurrency(double d)
	{
		return NumberFormat.getCurrencyInstance().format(d);
	}
	

	/**
	 * Checks whether <code>Object</code> <code>value</code> is a type of
	 * <code>Class</code> <code>type</code>. If yes it returns
	 * <code>value</code> else <code>ClassCastException</code>.
	 * 
	 * @param value
	 *            <code>Object</code> which type will be checked
	 * @param type
	 *            <code>Class</code> of which <code>Object</code> will be
	 *            checked.
	 * @return <code>value</code> if <code>value</code> is type of
	 *         <code>type</code>. Else <code>ClassCastException</code>.
	 * 
	 * @see ParseUtils#isType(Object, String)
	 * @see ParseUtils#isType(Object, Class)
	 * 
	 */
	public static Object castTo(Object value, Class type)
	{
		if(isType(value,type))
		{
			return value;
		}
		
		throw new ClassCastException(value + " is not a " + type);
	}
	

	/**
	 * Checks whether <code>Object</code> <code>value</code> is a type of
	 * <code>Class</code> <code>type</code>. If yes it returns <code>true</code>
	 * else <code>false</code>.
	 * 
	 * @param value
	 *            <code>Object</code> which type will be checked
	 * @param type
	 *            <code>Class</code> of which <code>Object</code> will be
	 *            checked.
	 * @return true if <code>Object</code> is type of <code>type</code>. Else
	 *         <code>false</code>.
	 * 
	 * @see ParseUtils#isType(Object, String)
	 */
	public static boolean isType(Object value, Class type)
	{
		return type.isAssignableFrom(value.getClass());
	}
	

	/**
	 * Checks whether <code>Object</code> <code>value</code> is a type of
	 * <code>Class</code> <code>className</code>. If yes it returns
	 * <code>true</code> else <code>false</code>.
	 * 
	 * <blockquote> <b> Example </b></blockquote>
	 * 
	 * <pre>
	 * ParseUtils.isType(65, "double");		returns false
	 * 
	 * </pre>
	 * 
	 * @param value
	 *            <code>Object</code> which type will be checked
	 * @param className
	 *            <code>className</code> of which <code>Object</code> will be
	 *            checked.
	 * @return true if <code>Object</code> is type of <code>className</code>.
	 *         Else <code>false</code>.
	 * 
	 * @see ParseUtils#isType(Object, Class)
	 */
	public static boolean isType(Object value, String className)
	{
		try
		{
			return Class.forName(className).isAssignableFrom(value.getClass());
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Checks if the <code>Object</code> <code>value</code> is an integer. That
	 * means whether <code>value</code> can be parsed to an integer without
	 * losing information.
	 * 
	 * <blockquote> <b> Example </b>
	 * 
	 * <pre>
	 * 
	 * ParseUtils.isInteger(20);     returns true
	 * ParseUtils.isInteger(null);   returns false
	 * 
	 * </pre>
	 * 
	 * </blockquote> <b></b>
	 * 
	 * <pre>
	 * The number range of <code>Integer</code> is between -2.147.483.648 and 2.147.483.647 .
	 * </pre>
	 * 
	 * @param value
	 *            the <code>Object</code> <code>value</code> which will be
	 *            checked
	 * @return true if <code>value</code> is an integer or can be parsed to
	 *         without losing information
	 * 
	 * @see ParseUtils#isInteger(Object)
	 * @see ParseUtils#isDecimal(Object)
	 * @see ParseUtils#isBoolean(Object)
	 * @see ParseUtils#parseLong(Object)
	 * @see ParseUtils#parseLong(Object, String)
	 */
	public static boolean isInteger(Object value)
	{
		try
		{
			double d = parseDouble(value);
			int i = (int)d;
			return i == d && i >= Integer.MIN_VALUE && i <= Integer.MAX_VALUE;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Checks if the <code>Object</code> <code>value</code> is an integer. That
	 * means whether <code>value</code> can be parsed to an integer without
	 * losing information.
	 * 
	 * <blockquote> <b> Example </b>
	 * 
	 * ParseUtils.isLong(20875884855699l); returns true ParseUtils.isLong(null);
	 * returns false
	 * 
	 * </blockquote> <b></b>
	 * 
	 * <pre>
	 * The number range of <code>long</code> is between -9.223.372.036.854.775.808 and 9.223.372.036.854.775.807 .
	 * </pre>
	 * 
	 * @param value
	 *            the <code>Object</code> <code>value</code> which will be
	 *            checked
	 * @return true if <code>value</code> is an integer or can be parsed to
	 *         without losing information
	 * 
	 * @see ParseUtils#isLong(Object)
	 * @see ParseUtils#isDecimal(Object)
	 * @see ParseUtils#isBoolean(Object)
	 * @see ParseUtils#parseLong(Object)
	 * @see ParseUtils#parseLong(Object, String)
	 */
	public static boolean isLong(Object value)
	{
		try
		{
			double d = parseDouble(value);
			long l = (long)d;
			return l == d;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Checks if the <code>Object</code> <code>value</code> is a decimal or can
	 * parsed to.
	 * 
	 * <blockquote> <b> 1. General </b>
	 * 
	 * <pre>
	 * isDecimal("3.14")
	 * 
	 * returns <code>true</code>
	 * </pre>
	 * 
	 * <b>2. Integer</b>
	 * 
	 * <pre>
	 * isDecimal("5")
	 * 
	 * returns <code>true</code>.
	 * </pre>
	 * 
	 * <b>3. Zero</b>
	 * 
	 * <pre>
	 * isDecimal("0")
	 * 
	 * returns <code>true</code>.
	 * </pre>
	 * 
	 * <b>4. Exponent spelling 1</b>
	 * 
	 * <pre>
	 * isDecimal("1E3")
	 * 
	 * 
	 * returns <code>true</code>.
	 * (<i>1E3</i> = 1 * 10<sup>3</sup> = 1000)
	 * </pre>
	 * 
	 * <b>5. Exponent spelling 2</b>
	 * 
	 * <pre>
	 * isDecimal(1E-3)
	 * 
	 * returns <code>true</code>.
	 * (<i>1E3</i> = 1 * 10<sup>-3</sup> = 0,001)
	 * </pre>
	 * 
	 * <b>6. Not a number</b>
	 * 
	 * <pre>
	 * isDecimal("NaN")
	 * 
	 * returns <code>true</code>.
	 * </pre>
	 * 
	 * <b>7. Minus infinity</b>
	 * 
	 * <pre>
	 * isDecimal("Infinity") 
	 * 
	 * returns <code>true</code>.
	 * </pre>
	 * 
	 * <b>8. Minus infinity</b>
	 * 
	 * <pre>
	 * isDecimal("-Infinity")
	 * 
	 * returns <code>true</code>.
	 * </pre>
	 * 
	 * </blockquote> <b>Hint</b>
	 * 
	 * <pre>
	 * The check wil be made with the regular expression:
	 * ^[a-zA-Z0-9._%-]+@[a-zA-ZÄÖÜäöü0-9._%-]+\\.[a-zA-Z]{2,4}$
	 * </pre>
	 * 
	 * <b>Hint</b>
	 * 
	 * <pre>
	 * "NaN", "Infinity" and "-Infinity" are case sensitive.
	 * That means "NAN", "nan", "infinity", "INFINITY", etc can't be recognized.
	 * Also "Negative Infinity" or something like that can't be recognized.
	 * 
	 * For the exponent notation can instead of "E" also "e" be used. 
	 * Likewise, "5E09", "0E4" or "6.5E12" can be used too. Is not possible to use  e.g. "E3" or "5E9.1"
	 * 
	 * </pre>
	 * 
	 * <b>Hint</b>
	 * 
	 * <pre>
	 * This methode has approximately the same performance as {@link ParseUtils#parseDouble(Object)}.
	 * That means if <code>Object</code> <code>value</code> will be used as decimal, after this methode, it is convenient 
	 * to write:
	 * <br>
	 * <code>  <br>
	 * <br>
	 * ParseUtils.isDecimal(200.05); returns true
	 * ParseUtils.isDecimal(null);   returns false
	 * </code><br>
	 * <br>
	 *  
	 * With this the check will be made only once.
	 * 
	 * </pre>
	 * 
	 * 
	 * @param value
	 *            the <code>Object</code> <code>value</code> which will be
	 *            checked
	 * @return true if <code>value</code> is a decimal or can be parsed to.
	 * 
	 * @see ParseUtils#isInteger(Object)
	 * @see ParseUtils#isNumeric(Object, double, double)
	 * @see ParseUtils#parseDouble(Object)
	 */
	public static boolean isDecimal(Object value)
	{
		try
		{
			parseDouble(value);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Checks if the variable <code>value</code> is type of <code>boolean</code>
	 * .<blockquote>
	 * 
	 * <pre>
	 * simple
	 * ParseUtils.isBoolean(<code>true</code>) 
	 * ParseUtils.isBoolean(<code>false</code>) 
	 * 
	 * Returns <code>true</code>.
	 * 
	 * other Value
	 * isBoolean("true") 
	 * isBoolean(5) 
	 * isBoolean("alles andere")
	 * 
	 * Returns <code>false</code>. 
	 * (also "true" isnt a Boolean, because the type is a {@link String})
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * *
	 * <p>
	 * <strong>Warning:</strong> While {@link ParseUtils#parseBoolean(Object)}
	 * trys to parse a given value to <code>boolean</code>, checks this methode
	 * not if a given value can be parsed to <code>boolean</code>, but if a
	 * given value is type of <code>boolean</code>.
	 * 
	 * To check if a value (like the <code>String</code> "true") can be
	 * interprete as <code>boolean</code> can be used the following Code: <code>
	 * 
	 * 	ParseUtils.isBoolean(null); returns false
	 * 	ParseUtils.isBoolean(150);  returns false
		</code>
	 * </p>
	 * 
	 * @param value
	 *            the value which will be checked if its is a
	 *            <code>boolean</code>.
	 * @return <code>true</code> if the <code>value</code> is a
	 *         <code>boolean</code>, else <code>false</code>
	 * 
	 * @see ParseUtils#parseBoolean(Object)
	 * @see ParseUtils#isInteger(Object)
	 * @see ParseUtils#isDecimal(Object)
	 * @see ParseUtils#isLong(Object)
	 * 
	 */
	public static boolean isBoolean(Object value)
	{
		return value != null && value instanceof Boolean;
	}
	
	private static Hashtable	numberFormatCache	= new Hashtable();
	

	private static NumberFormat getNumberFormat()
	{
		Locale locale = Locale.getDefault();
		if(numberFormatCache.containsKey(locale))
		{
			return (NumberFormat)numberFormatCache.get(locale);
		}
		
		NumberFormat format = NumberFormat.getNumberInstance(locale);
		numberFormatCache.put(locale,format);
		return format;
	}
}
