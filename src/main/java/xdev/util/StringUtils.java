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


import java.util.Collection;
import java.util.Map;
import java.util.StringTokenizer;

import xdev.lang.LibraryMember;
import xdev.lang.NotNull;


/**
 * <p>
 * The <code>StringUtils</code> class provides utility methods for
 * {@link String} handling.
 * </p>
 * 
 * @since 2.0
 * 
 * @author XDEV Software
 */
@LibraryMember
public final class StringUtils
{
	
	/**
	 * newline constant used in StringUtils.
	 */
	public final static String	NEWLINE		= "\n";
	
	/**
	 * line feed constant used in StringUtils.
	 */
	public final static String	LINEFEED	= "\r";
	
	/**
	 * tab constant used in StringUtils.
	 */
	public final static String	TAB			= "\t";
	
	
	/**
	 * <p>
	 * <code>StringUtils</code> instances can not be instantiated. The class
	 * should be used as utility class:
	 * <code>StringUtils.getUnicodeNr("bar");</code>.
	 * </p>
	 */
	private StringUtils()
	{
	}
	
	
	/**
	 * Gets the character at <code>index</code> as new String.
	 * 
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * StringUtils.charAt("XDEV", 0) returns "X"
	 * StringUtils.charAt("Bar", 2) returns "r"
	 * StringUtils.charAt("Hello XDEV3", 6) returns "X"
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param str
	 *            the String to get the character from.
	 * @param index
	 *            the position of the character. 0 &lt;= <code>index</code> &lt;
	 *            length of <code>str</code>. The first character has
	 *            <code>index</code> 0.
	 * @return the character at the specified index of <code>str</code>. The
	 *         first <code>char</code> value is at index <code>0</code>.
	 * @throws NullPointerException
	 *             if <code>str</code> is <code>null</code>
	 * @throws IndexOutOfBoundsException
	 *             if the <code>index</code> argument is negative or not less
	 *             than the length of <code>str</code>.
	 * 
	 * 
	 */
	@NotNull
	public static String charAt(@NotNull String str, int index) throws NullPointerException,
			IndexOutOfBoundsException
	{
		return String.valueOf(str.charAt(index));
	}
	
	
	/**
	 * Returns <code>true</code> if and only if {@link String} <code>str</code>
	 * contains the specified {@link String} <code>search</code>. This method is
	 * case sensitive.
	 * 
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * StringUtils.contains("Hello XDEV3", "beta")  returns false
	 * StringUtils.contains("Hello XDEV3", "Xdev3") returns false
	 * StringUtils.contains("Hello XDEV3", "XDEV3") returns true
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * 
	 * @param str
	 *            the {@link String} to search in
	 * @param search
	 *            the {@link String} to search for
	 * @return <code>true</code> if {@link String} <code>str</code> contains
	 *         <code>search</code>, false otherwise
	 * @throws NullPointerException
	 *             if <code>str</code> is <code>null</code>
	 * @throws NullPointerException
	 *             if <code>search</code> is <code>null</code>
	 * 
	 */
	public static boolean contains(@NotNull String str, @NotNull String search)
			throws NullPointerException
	{
		return str.indexOf(search) >= 0;
	}
	
	
	/**
	 * Returns a new string that is a substring of <code>str</code>. The
	 * substring begins with the character at the specified index
	 * <code>start</code> and contains the following number of characters
	 * specified by <code>length</code>.
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * StringUtils.substr("New York",4,4) returns "York"
	 * StringUtils.substr("New York",0,3) returns "New"
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param str
	 *            the {@link String} to operate on
	 * @param start
	 *            the beginning index, inclusive.
	 * @param length
	 *            the length of the substring
	 * @return the specified substring.
	 * 
	 * @throws NullPointerException
	 *             if <code>str</code> is <code>null</code>
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if <code>start</code> is negative or greater than the length
	 *             of <code>str</code>. If <code>length</code> is negative or
	 *             greater than the length of <code>str</code> -
	 *             <code>start</code>.
	 */
	@NotNull
	public static String substr(@NotNull String str, int start, int length)
			throws NullPointerException, IndexOutOfBoundsException
	{
		return str.substring(start,start + length);
		
	}
	
	
	/**
	 * Replaces each substring of the given {@link String} <code>source</code>
	 * that matches the given {@link String} <code>search</code> with the given
	 * {@link String} <code>replacement</code>.
	 * 
	 * <p>
	 * <strong>Hint:</strong> The {@link String} <code>source</code> remains
	 * unchanged. All operations take place on a newly created String that is
	 * returned by this method.
	 * </p>
	 * 
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 *  StringUtils.replaceAll("This is a String","is","at") returns "That at a String"
	 *  StringUtils.replaceAll("In Betatest","In","For")     returns "For Betatest"
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * 
	 * @param source
	 *            {@link String} to search in
	 * @param search
	 *            {@link String} to search for
	 * @param replacement
	 *            {@link String} to replace the {@link String}
	 *            <code>search</code> with
	 * @return the result of the replace operation
	 * 
	 * @throws NullPointerException
	 *             if <code>source</code>, <code>search</code> or
	 *             <code>replacement</code> are <code>null</code>.
	 * 
	 */
	@NotNull
	public static String replaceAll(@NotNull String source, @NotNull String search,
			@NotNull String replacement) throws NullPointerException
	{
		return source.replace(search,replacement);
		
		// String _source = new String(source);
		//
		// if(search.length() > 0)
		// {
		// int searchLen = search.length();
		// int replacementLen = replacement.length();
		// int si = _source.indexOf(search);
		//
		// while(si >= 0)
		// {
		// int sourceLen = _source.length();
		//
		// char[] newChars = new char[sourceLen - searchLen + replacementLen];
		//
		// if(si > 0)
		// {
		// _source.getChars(0,si,newChars,0);
		// }
		//
		// if(replacementLen > 0)
		// {
		// replacement.getChars(0,replacementLen,newChars,si);
		// }
		//
		// int si2 = si + searchLen;
		// if(si2 < sourceLen)
		// {
		// _source.getChars(si2,sourceLen,newChars,si + replacementLen);
		// }
		//
		// _source = new String(newChars);
		// si = _source.indexOf(search,si + replacementLen);
		// }
		// }
		//
		// return _source;
	}
	
	
	/**
	 * Splits a the {@link String} <code>str</code> at every separator
	 * {@link String} <code>separator</code>.
	 * 
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * XdevList&lt;String&gt; expected = new XdevList&lt;String&gt;();
	 * expected.add(&quot;Hello&quot;);
	 * expected.add(&quot;XDEV3&quot;);
	 * XdevList&lt;String&gt; actual = StringUtils.explode(&quot;Hello,XDEV3&quot;,&quot;,&quot;);
	 * Assert.assertEquals(expected,actual);
	 * 
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * <p>
	 * This method is a alias for
	 * <code>StringUtils.explode(str, separator, false)</code>
	 * </p>
	 * 
	 * @param str
	 *            the {@link String} to be split
	 * @param separator
	 *            the separator
	 * 
	 * @return {@link XdevList} containing all parts of the given {@link String}
	 *         <code>str</code> split at every separator {@link String}
	 *         <code>separator</code>.
	 * @see #explode(String, String, boolean)
	 */
	@NotNull
	public static XdevList<String> explode(@NotNull String str, @NotNull String separator)
	{
		return explode(str,separator,false);
	}
	
	
	/**
	 * Splits a the {@link String} <code>str</code> at every separator
	 * {@link String} <code>separator</code>.
	 * 
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * StringUtils.explode("XDEV IDE based on java", " ", true)  returns a list with 9 elements 
	 * StringUtils.explode("XDEV IDE based on java", " ", false) returns a list with 5 elements
	 *  XdevList<String> expected = new XdevList<String>();
	 * expected.add("Hello");
	 * expected.add("world");
	 * expected.add("and");
	 * expected.add("space");
	 * XdevList<String> actual = StringUtils.explode("Hello,world,and,space", ",",false);
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * 
	 * 
	 * @param str
	 *            the {@link String} to be split
	 * @param separator
	 *            the separator
	 * @param returnDelimiters
	 *            if <code>true</code> the separator is returned as independent
	 *            element in the list; otherwise it is not.
	 * @return {@link XdevList} containing all parts of the given {@link String}
	 *         <code>str</code> split at every separator {@link String}
	 *         <code>separator</code>.
	 * 
	 * @throws NullPointerException
	 *             if eighter <code>str</code> or <code>separator</code> is
	 *             <code>null</code>
	 * @see #explode(String, String)
	 * @see #concat(String, Collection)
	 * @see #concat(String, Object...)
	 */
	@NotNull
	public static XdevList<String> explode(@NotNull String str, @NotNull String separator,
			boolean returnDelimiters) throws NullPointerException
	{
		XdevList<String> list = new XdevList<String>();
		StringTokenizer st = new StringTokenizer(str,separator,returnDelimiters);
		while(st.hasMoreTokens())
		{
			list.addElement(st.nextToken());
		}
		return list;
	}
	
	
	/**
	 * @deprecated replaced by {@link #concat(String, Collection)}
	 */
	@Deprecated
	public static String implode(@NotNull Collection<?> list, @NotNull String separator)
			throws NullPointerException
	{
		return concat(separator,list);
	}
	
	
	/**
	 * 
	 * Creates a new {@link String} with the value of n (provided
	 * <code>count</code>) times the provided {@link String} <code>str</code>.
	 * 
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * StringUtils.create("Hello!",3);           returns "Hello!Hello!Hello!"
	 * StringUtils.create("XDEV!",2);            returns "XDEV!XDEV!"
	 * StringUtils.create("XDEV!XDEV2!XDEV3",1); returns "XDEV!XDEV2!XDEV3"
	 * 	 *
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param source
	 *            {@link String} to use as source for the new {@link String}
	 * @param count
	 *            how many times should the provided {@link String} be repeated.
	 *            <code>Count</code> is valid for &gt;= 0.
	 * @return a new {@link String} with the value of n (provided
	 *         <code>count</code>) times the provided {@link String}
	 *         <code>str</code>.
	 * 
	 * @throws NullPointerException
	 *             if <code>source</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>count</code> is not valid (&lt;0)
	 * 
	 */
	@NotNull
	public static String create(@NotNull String source, int count) throws NullPointerException,
			IllegalArgumentException
	{
		if(count < 0)
		{
			throw new IllegalArgumentException("count must at least be 0");
		}
		
		StringBuffer sb = new StringBuffer(source.length() * count);
		for(int i = 0; i < count; i++)
		{
			sb.append(source);
		}
		
		return sb.toString();
	}
	
	
	/**
	 * Returns the unicode character for the provided number.
	 * 
	 * 
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * StringUtils.getUnicodeString(65) returns "A"
	 * StringUtils.getUnicodeString(75) returns "K"
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * 
	 * @param number
	 *            of the unicode character
	 * @return the unicode character for the provided number.
	 */
	@NotNull
	public static String getUnicodeString(int number)
	{
		return "" + ((char)number);
	}
	
	
	/**
	 * Returns the unicode number for the prodvied {@link String}
	 * <code>str</code>.
	 * 
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * StringUtils.getUnicodeNr("A") returns 65
	 * StringUtils.getUnicodeNr("H") returns 72
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param str
	 *            {@link String} to get the unicode number for. <code>Str</code>
	 *            must have length = 1.
	 * @return the unicode number for the prodvied {@link String}
	 *         <code>str</code>.
	 * 
	 * @throws IllegalArgumentException
	 *             if {@link String} has length != 1
	 */
	public static int getUnicodeNr(String str) throws IllegalArgumentException
	{
		if(str.length() != 1)
		{
			throw new IllegalArgumentException("str.length() must be 1");
		}
		
		return getUnicodeNr(str.charAt(0));
	}
	
	
	/**
	 * Returns the unicode number for the prodvied <code>ch</code>.
	 * 
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * StringUtils.getUnicodeNr('A') returns 65
	 * StringUtils.getUnicodeNr('F') returns 70
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param ch
	 *            to get the unicode number for.
	 * @return the unicode number for the prodvied <code>ch</code>.
	 * 
	 */
	public static int getUnicodeNr(char ch)
	{
		return (int)ch;
	}
	
	
	/**
	 * Returns a {@link String} representation of all elements of the provided
	 * {@link Collection} <code>values</code> separated by the specified
	 * <code>separator</code>.
	 * 
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 *  StringUtils.concat(&quot;, &quot;, new XdevList&lt;String&gt;(&quot;Miami&quot;,&quot;Atalanta&quot;,&quot;Vegas&quot;,&quot;LA&quot;));
	 * returns "Miami, Atalanta, Vegas, LA"
	 * StringUtils.concat("-", new XdevList<String>("Rom","Munich","Berlin","Madrid"))
	 * returns "Rom-Munich-Berlin-Madrid"
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param separator
	 *            String to separated the list elements.
	 * @param values
	 *            the {@link Collection} to concat
	 * 
	 * @return a {@link String} representation of all elements of the provided
	 *         {@link Collection} <code>values</code> separated by the specified
	 *         <code>separator</code>.
	 * @throws NullPointerException
	 *             if <code>values</code> or <code>separator</code> is
	 *             <code>null</code>
	 * 
	 * @see #concat(String, Object...)
	 * @see #explode(String, String)
	 * @see #explode(String, String, boolean)
	 */
	@NotNull
	public static String concat(@NotNull String separator, @NotNull Collection<?> values)
			throws NullPointerException
	{
		if(separator == null)
		{
			throw new NullPointerException("separator must not be null");
		}
		
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(Object elem : values)
		{
			if(i > 0)
			{
				sb.append(separator);
			}
			sb.append(String.valueOf(elem));
			i++;
		}
		return sb.toString();
	}
	
	
	/**
	 * Returns a {@link String} representation of all elements of the provided
	 * <code>values</code> separated by the specified <code>separator</code>.
	 * 
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * StringUtils.concat(&quot;, &quot;, new XdevList&lt;String&gt;(&quot;Miami&quot;,&quot;Atalanta&quot;,&quot;Vegas&quot;,&quot;LA&quot;));
	 * returns "Miami, Atalanta, Vegas, LA"
	 * StringUtils.concat(".", new XdevList<String>("Wien","Graz","Frankfurt","Dublin"));
	 * returns "Wien.Graz.Frankfurt.Dublin"
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param separator
	 *            String to separated the list elements.
	 * @param values
	 *            the values to concat
	 * 
	 * @return a {@link String} representation of all elements of the provided
	 *         <code>values</code> separated by the specified
	 *         <code>separator</code>.
	 * @throws NullPointerException
	 *             if <code>values</code> or <code>separator</code> is
	 *             <code>null</code>
	 * 
	 * @see #concat(String, Collection)
	 * @see #explode(String, String)
	 * @see #explode(String, String, boolean)
	 */
	@NotNull
	public static String concat(@NotNull String separator, @NotNull Object... values)
			throws NullPointerException
	{
		if(separator == null)
		{
			throw new NullPointerException("separator must not be null");
		}
		
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(Object elem : values)
		{
			if(i > 0)
			{
				sb.append(separator);
			}
			sb.append(String.valueOf(elem));
			i++;
		}
		return sb.toString();
	}
	
	
	
	/**
	 * Provides {@link String} parameter values for the format method.
	 * 
	 * @see StringUtils#format(String, ParameterProvider)
	 */
	public static interface ParameterProvider
	{
		/**
		 * Returns the value for the parameter <code>key</code>.
		 * 
		 * @param key
		 *            the parameter's key
		 * @return the value of the parameter
		 */
		public String getValue(String key);
	}
	
	
	/**
	 * Formats the {@link String} <code>str</code> by replacing the variables
	 * with values provided by <code>params</code>.
	 * <p>
	 * For more information see {@link #format(String, ParameterProvider)}
	 * 
	 * @param str
	 *            the {@link String} to format
	 * @param params
	 *            the parameter {@link Map}
	 * @return a formatted {@link String}
	 */
	public static String format(String str, final Map<String, String> params)
	{
		return format(str,new ParameterProvider()
		{
			@Override
			public String getValue(String key)
			{
				return params.get(key);
			}
		});
	}
	
	
	/**
	 * Formats the {@link String} <code>str</code> by replacing the variables
	 * with values provided by <code>parameterProvider</code>.
	 * <p>
	 * Variables have the format <code>{$variableName}</code>.
	 * <p>
	 * 
	 * <pre>
	 * Given: parameter provider (name -> Peter, color -> blue)
	 * Result of format("{$name} loves the color {$color}.",provider) is "Peter loves the color blue".
	 * </pre>
	 * 
	 * @param str
	 *            the {@link String} to format
	 * @param parameterProvider
	 *            the {@link ParameterProvider}
	 * @return a formatted {@link String}
	 */
	public static String format(String str, ParameterProvider parameterProvider)
	{
		boolean isRichText = str.startsWith("{\\rtf");
		
		int start;
		int searchStart = 0;
		while((start = str.indexOf("{$",searchStart)) >= 0)
		{
			int end = str.indexOf("}",start + 2);
			if(end > start)
			{
				String key = str.substring(start + 2,end);
				if(isRichText)
				{
					// remove optional rich text escapes
					
					if(start > 0 && str.charAt(start - 1) == '\\')
					{
						start--;
					}
					
					int len = key.length();
					if(len > 0 && key.charAt(len - 1) == '\\')
					{
						key = key.substring(0,len - 1);
					}
				}
				
				String value = parameterProvider.getValue(key);
				
				StringBuilder sb = new StringBuilder();
				sb.append(str.substring(0,start));
				sb.append(value);
				sb.append(str.substring(end + 1));
				str = sb.toString();
				
				searchStart = start + value.length();
			}
			else
			{
				break;
			}
		}
		
		return str;
	}
}
