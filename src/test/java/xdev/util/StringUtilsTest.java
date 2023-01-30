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


import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import xdev.util.StringUtils.ParameterProvider;


/**
 * Tests for the Class {@link StringUtils}.
 * 
 * @author XDEV Software (FHAE)
 */
@SuppressWarnings("deprecation")
public class StringUtilsTest
{
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#charAt(java.lang.String, int)}.
	 */
	@Test
	public void testCharAt_defaultBehavior()
	{
		final String value = "Test the default behavior";
		assertEquals("d",StringUtils.charAt(value,9));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#charAt(java.lang.String, int)}.
	 */
	@Test
	public void testCharAt_WithSpaces()
	{
		final String value = "This test Method";
		assertEquals(" ",StringUtils.charAt(value,4));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#charAt(java.lang.String, int)}.
	 */
	@Test(expected = NullPointerException.class)
	public void testCharAt_ParamStrNullPointerException()
	{
		StringUtils.charAt(null,1);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#charAt(java.lang.String, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testCharAt_ParamIndexNegativeIndexOutOfBoundsException()
	{
		StringUtils.charAt("Test",-20);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#charAt(java.lang.String, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testCharAt_ParamIndexGreaterIndexOutOfBoundsException()
	{
		StringUtils.charAt("Test",100);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#contains(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testContains_defaultBehavior()
	{
		assertEquals(Boolean.FALSE,StringUtils.contains("Hello XDEV3","beta"));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#contains(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testContains_CaseSensitive()
	{
		assertEquals(Boolean.FALSE,StringUtils.contains("Hello XDEV3","xdev3"));
		assertEquals(Boolean.TRUE,StringUtils.contains("Hello XDEV3","XDEV3"));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#contains(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testContains_ParamStrNullPointerException()
	{
		assertEquals(Boolean.FALSE,StringUtils.contains(null,"xdev3"));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#contains(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testContains_ParamSearchNullPointerException()
	{
		assertEquals(Boolean.FALSE,StringUtils.contains(null,"xdev3"));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#substr(java.lang.String, int, int)}.
	 */
	@Test
	public void testSubstr_defaultBehavior()
	{
		assertEquals("Pointer",StringUtils.substr("NullPointerException",4,7));
		assertEquals("Null",StringUtils.substr("NullPointerException",0,4));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#substr(java.lang.String, int, int)}.
	 */
	@Test
	public void testSubstr_WithSpaces()
	{
		assertEquals("This i",StringUtils.substr("This is a Test",0,6));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#substr(java.lang.String, int, int)}.
	 */
	@Test(expected = NullPointerException.class)
	public void testSubstr_ParamStrNullPointerException()
	{
		StringUtils.substr(null,0,0);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#substr(java.lang.String, int, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSubstr_ParamStartGreaterIndexOutOfBoundsException()
	{
		StringUtils.substr("test",100,0);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#substr(java.lang.String, int, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSubstr_ParamStartNegativeIndexOutOfBoundsException()
	{
		StringUtils.substr("test",-1,0);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#substr(java.lang.String, int, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSubstr_ParamLengthGreaterIndexOutOfBoundsException()
	{
		StringUtils.substr("test",0,10);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#substr(java.lang.String, int, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSubstr_ParamLengthNegativeIndexOutOfBoundsException()
	{
		StringUtils.substr("test",0,-1);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#replaceAll(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testReplaceAll_defaultBehavior()
	{
		final String text = "Replaces each substring of the given source that matches the given search with the given replacement.";
		
		assertEquals(
				"Replaces each substring of the  source that matches the  search with the  replacement.",
				StringUtils.replaceAll(text,"given",""));
		assertEquals(
				"Replaces each substring of the String source that matches the String search with the String replacement.",
				StringUtils.replaceAll(text,"given","String"));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#replaceAll(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testReplaceAll_WithSpaces()
	{
		final String text = "Replaces each substring";
		assertEquals("Replaces substring",StringUtils.replaceAll(text," each",""));
		assertEquals("Replaceseachsubstring",StringUtils.replaceAll(text," ",""));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#replaceAll(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testReplaceAll_ParamSourceNullPointerException()
	{
		StringUtils.replaceAll(null,"test","a");
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#replaceAll(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testReplaceAll_ParamSearchNullPointerException()
	{
		StringUtils.replaceAll("test method",null,"a");
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#replaceAll(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testReplaceAll_ParamReplacementNullPointerException()
	{
		StringUtils.replaceAll("test this method","test",null);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#explode(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testExplodeStringString_defaultBehavior()
	{
		assertEquals(new XdevList<String>("German","English","Spanish"),
				StringUtils.explode("German,English,Spanish",","));
		assertEquals(new XdevList<String>("German,English,Spanish"),
				StringUtils.explode("German,English,Spanish"," "));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#explode(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testExplodeStringString_ParamStrNullPointerException()
	{
		StringUtils.explode(null,",");
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#explode(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testExplodeStringString_ParamSeparatorNullPointerException()
	{
		StringUtils.explode("German,English,Spanish",null);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#explode(java.lang.String, java.lang.String, boolean)}
	 * .
	 */
	@Test
	public void testExplodeStringStringBoolean_WithSeparator()
	{
		final String seperator = ",";
		assertEquals(new XdevList<String>("German",seperator,"English",seperator,"Spanish"),
				StringUtils.explode("German,English,Spanish",seperator,true));
		assertEquals(new XdevList<String>("German,English,Spanish"),
				StringUtils.explode("German,English,Spanish"," ",true));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#explode(java.lang.String, java.lang.String, boolean)}
	 * .
	 */
	@Test
	public void testExplodeStringStringBoolean_WithoutSeparator()
	{
		assertEquals(new XdevList<String>("German","English","Spanish"),
				StringUtils.explode("German,English,Spanish",",",false));
		assertEquals(new XdevList<String>("German,English,Spanish"),
				StringUtils.explode("German,English,Spanish"," ",false));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#explode(java.lang.String, java.lang.String, boolean)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testExplodeStringStringBoolean_ParamStrNullPointerException()
	{
		StringUtils.explode(null,",",false);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#explode(java.lang.String, java.lang.String, boolean)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testExplodeStringStringBoolean_ParamSeparatorNullPointerException()
	{
		StringUtils.explode("German,English,Spanish",null,false);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#implode(java.util.Collection, java.lang.String)}
	 * .
	 */
	@Test
	public void testImplode_defaultBehavior()
	{
		final String separator = ",";
		final Collection<String> col = new XdevList<String>("German","English","Spanish");
		
		assertEquals("German,English,Spanish",StringUtils.implode(col,separator));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#implode(java.util.Collection, java.lang.String)}
	 * .
	 */
	@Test
	public void testImplode_WithSpaces()
	{
		final String separator = " , ";
		final Collection<String> col = new XdevList<String>("German","English","Spanish");
		
		assertEquals("German , English , Spanish",StringUtils.implode(col,separator));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#implode(java.util.Collection, java.lang.String)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testImplode_ParamListNullPointerException()
	{
		StringUtils.implode(null,"");
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#implode(java.util.Collection, java.lang.String)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testImplode_ParamSeparatorNullPointerException()
	{
		StringUtils.implode(new XdevList<String>(""),null);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#create(java.lang.String, int)}.
	 */
	@Test
	public void testCreate_defaultBehavior()
	{
		assertEquals("Testmethod!Testmethod!",StringUtils.create("Testmethod!",2));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#create(java.lang.String, int)}.
	 */
	@Test
	public void testCreate_WithSpaces()
	{
		assertEquals(" Testmethod! Testmethod!",StringUtils.create(" Testmethod!",2));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#create(java.lang.String, int)}.
	 */
	@Test
	public void testCreate_ParamCountZero()
	{
		assertEquals("",StringUtils.create(" Testmethod!",0));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#create(java.lang.String, int)}.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreate_ParamSourceNullPointerException()
	{
		StringUtils.create(null,0);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#create(java.lang.String, int)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_ParamLengthIllegalArgumentException()
	{
		StringUtils.create("IllegalArgumentException",-1);
	}
	
	
	/**
	 * Test method for {@link xdev.util.StringUtils#getUnicodeString(int)}.
	 */
	@Test
	public void testGetUnicodeString_defaultBehavior()
	{
		
		String expected = "A";
		String actual = StringUtils.getUnicodeString(65);
		assertEquals(expected,actual);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#getUnicodeNr(java.lang.String)}.
	 */
	@Test
	public void testGetUnicodeNrString()
	{
		assertEquals(65,StringUtils.getUnicodeNr("A"));
		assertEquals(72,StringUtils.getUnicodeNr("H"));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#getUnicodeNr(java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetUnicodeNrString_ParamStrGreaterIllegalArgumentException()
	{
		StringUtils.getUnicodeNr("abc");
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#getUnicodeNr(java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetUnicodeNrString_ParamStrEmptyIllegalArgumentException()
	{
		StringUtils.getUnicodeNr("");
	}
	
	
	/**
	 * Test method for {@link xdev.util.StringUtils#getUnicodeNr(char)}.
	 */
	@Test
	public void testGetUnicodeNrChar_defaultBehavior()
	{
		assertEquals(65,StringUtils.getUnicodeNr('A'));
		assertEquals(70,StringUtils.getUnicodeNr('F'));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#concat(java.lang.String, java.util.Collection)}
	 * .
	 */
	@Test
	public void testConcatStringCollectionOfQ_defaultBehavior()
	{
		final String separator = ",";
		final Collection<String> col = new XdevList<String>("German","English","Spanish");
		
		assertEquals("German,English,Spanish",StringUtils.concat(separator,col));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#concat(java.lang.String, java.util.Collection)}
	 * .
	 */
	@Test
	public void testConcatStringCollectionOfQ_WithSpaces()
	{
		final String separator = " , ";
		final Collection<String> col = new XdevList<String>("German","English","Spanish");
		
		assertEquals("German , English , Spanish",StringUtils.concat(separator,col));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#concat(java.lang.String, java.util.Collection)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testConcatStringCollectionOfQ_ParamSeparatorNullPointerException()
	{
		StringUtils.concat(null,new XdevList<String>(""));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#concat(java.lang.String, java.util.Collection)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testConcatStringCollectionOfQ_ParamCollectionNullPointerException()
	{
		StringUtils.concat("",(Collection<?>)null);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#concat(java.lang.String, java.lang.Object[])}
	 * .
	 */
	@Test
	public void testConcatStringObjectArray_defaultBehavior()
	{
		final String separator = ",";
		assertEquals("German,English,Spanish",
				StringUtils.concat(separator,"German","English","Spanish"));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#concat(java.lang.String, java.lang.Object[])}
	 * .
	 */
	@Test
	public void testConcatStringObjectArray_WithSpaces()
	{
		final String separator = " , ";
		assertEquals("German , English , Spanish",
				StringUtils.concat(separator,"German","English","Spanish"));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#concat(java.lang.String, java.lang.Object[])}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testConcatStringObjectArray_ParamSeparatorNullPointerException()
	{
		StringUtils.concat(null,"German","English");
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#concat(java.lang.String, java.lang.Object[])}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testConcatStringObjectArray_ParamArrayNullPointerException()
	{
		StringUtils.concat(",",(Object[])null);
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#format(java.lang.String, java.util.Map)}.
	 */
	@Test
	public void testFormatStringMapOfStringString_defaultBehavior()
	{
		final Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put("product","XDEV");
		replaceMap.put("RAD","Rapid Application Development");
		
		final String expected = "XDEV is a Java development environment for Rapid Application Development (RAD) that is available as a free download.";
		assertEquals(
				expected,
				StringUtils
						.format("{$product} is a Java development environment for {$RAD} (RAD) that is available as a free download.",
								replaceMap));
	}
	
	
	/**
	 * Test method for
	 * {@link xdev.util.StringUtils#format(java.lang.String, xdev.util.StringUtils.ParameterProvider)}
	 * .
	 */
	@Test
	public void testFormatStringParameterProvider_defaultBehavior()
	{
		final ParameterProvider provider = new ParameterProvider()
		{
			final Map<String, String>	replaceMap	= new HashMap<String, String>()
													{
														/**
				 * 
				 */
														private static final long	serialVersionUID	= -1803692248116708366L;
														
														{
															put("product","XDEV");
															put("RAD",
																	"Rapid Application Development");
														}
													};
			
			
			@Override
			public String getValue(String key)
			{
				return replaceMap.get(key);
			}
		};
		
		final String expected = "XDEV is a Java development environment for Rapid Application Development (RAD) that is available as a free download.";
		assertEquals(
				expected,
				StringUtils
						.format("{$product} is a Java development environment for {$RAD} (RAD) that is available as a free download.",
								provider));
	}
	
}
