
package xdev.util.systemproperty;

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


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * 
 * @author XDEV Software (RHHF)
 * @since 3.2
 */
public class SystemPropertyTest
{
	
	public final static String	PROPERTY_NAME	= "myNiceTestProperty.Borat";
	

	@After
	@Before
	public void cleanUp()
	{
		System.getProperties().remove(PROPERTY_NAME);
	}
	

	@Test()
	public void testIntegerSystemPropertySet100()
	{
		this.testIntegerSystemPropertySet(100);
	}
	

	@Test(expected = NullPointerException.class)
	public void testIntegerSystemPropertySetNull()
	{
		this.testIntegerSystemPropertySet(null);
	}
	

	@Test()
	public void testIntegerSystemPropertySetMinus100()
	{
		this.testIntegerSystemPropertySet(-100);
	}
	

	protected void testIntegerSystemPropertySet(final Integer NUMBER_VALUE)
	{
		assertNull(System.getProperty(PROPERTY_NAME));
		
		IntegerSystemProperty number = new IntegerSystemProperty(PROPERTY_NAME);
		
		number.setValue(NUMBER_VALUE);
		
		assertNotNull(System.getProperty(PROPERTY_NAME));
		
		final int numberValueFromProperties = Integer.getInteger(PROPERTY_NAME);
		
		assertSame(NUMBER_VALUE,numberValueFromProperties);
		
	}
	

	@Test
	public void testIntegerSystemPropertyGet100()
	{
		this.testIntegerSystemPropertyGet(100,100);
	}
	

	@Test
	public void testIntegerSystemPropertyGetMinus100()
	{
		this.testIntegerSystemPropertyGet(-100,-100);
	}
	

	@Test
	public void testIntegerSystemPropertyGetNull()
	{
		this.testIntegerSystemPropertyGet(null,null);
	}
	

	@Test
	public void testIntegerSystemPropertyGetString()
	{
		this.testIntegerSystemPropertyGet("abc",null);
	}
	

	protected void testIntegerSystemPropertyGet(final Object NUMBER_VALUE,
			final Integer EXPECTED_VALUE)
	{
		
		assertNull(System.getProperty(PROPERTY_NAME));
		
		System.setProperty(PROPERTY_NAME,String.valueOf(NUMBER_VALUE));
		
		assertNotNull(System.getProperty(PROPERTY_NAME));
		
		IntegerSystemProperty number = new IntegerSystemProperty(PROPERTY_NAME);
		
		assertSame(EXPECTED_VALUE,number.getValue());
		
	}
	

	@Test
	public void testIntegerSystemPropertyGetDefaultString()
	{
		this.testIntegerSystemPropertyGetDefault("abc",5,5);
	}
	

	@Test
	public void testIntegerSystemPropertyGetDefault100()
	{
		this.testIntegerSystemPropertyGetDefault(100,5,100);
	}
	

	@Test
	public void testIntegerSystemPropertyGetDefaultNull()
	{
		this.testIntegerSystemPropertyGetDefault(null,5,5);
	}
	

	protected void testIntegerSystemPropertyGetDefault(final Object NUMBER_VALUE,
			final Integer DEAULT_VALUE, final Integer EXPECTED_VALUE)
	{
		
		assertNull(System.getProperty(PROPERTY_NAME));
		
		System.setProperty(PROPERTY_NAME,String.valueOf(NUMBER_VALUE));
		
		assertNotNull(System.getProperty(PROPERTY_NAME));
		
		IntegerSystemProperty number = new IntegerSystemProperty(PROPERTY_NAME);
		
		assertSame(EXPECTED_VALUE,number.getValue(DEAULT_VALUE));
		
	}
	

	@Test
	public void testBooleanSystemPropertySetTrue()
	{
		this.testBooleanSystemPropertySet(true);
	}
	

	@Test
	public void testBooleanSystemPropertySetFalse()
	{
		this.testBooleanSystemPropertySet(false);
	}
	

	@Test
	public void testBooleanSystemPropertySetNull()
	{
		this.testBooleanSystemPropertySet(null);
	}
	

	protected void testBooleanSystemPropertySet(final Boolean BOOLEAN_VALUE)
	{
		assertNull(System.getProperty(PROPERTY_NAME));
		
		BooleanSystemProperty bool = new BooleanSystemProperty(PROPERTY_NAME);
		
		bool.setValue(BOOLEAN_VALUE);
		
		assertNotNull(System.getProperty(PROPERTY_NAME));
		
		final Object objectValueFromProperties = System.getProperty(PROPERTY_NAME);
		
		final Boolean booleanValueFromProperties;
		
		if(objectValueFromProperties.equals("null"))
		{
			booleanValueFromProperties = null;
		}
		else
		{
			booleanValueFromProperties = Boolean.getBoolean(PROPERTY_NAME);
		}
		
		assertSame(BOOLEAN_VALUE,booleanValueFromProperties);
		
	}
	

	@Test()
	public void testBooleanSystemPropertyGetTrue()
	{
		this.testBooleanSystemPropertyGet(true,true);
	}
	

	@Test()
	public void testBooleanSystemPropertyGetFalse()
	{
		this.testBooleanSystemPropertyGet(false,false);
	}
	

	@Test()
	public void testBooleanSystemPropertyGetNull()
	{
		this.testBooleanSystemPropertyGet(null,false);
	}
	

	@Test()
	public void testBooleanSystemPropertyGetString()
	{
		this.testBooleanSystemPropertyGet("hans",false);
	}
	

	protected void testBooleanSystemPropertyGet(final Object BOOLEAN_VALUE,
			final Boolean EXPECTED_VALUE)
	{
		
		assertNull(System.getProperty(PROPERTY_NAME));
		
		System.setProperty(PROPERTY_NAME,String.valueOf(BOOLEAN_VALUE));
		
		assertNotNull(System.getProperty(PROPERTY_NAME));
		
		BooleanSystemProperty bool = new BooleanSystemProperty(PROPERTY_NAME);
		
		assertSame(EXPECTED_VALUE,bool.getValue());
		
	}
	

	@Test
	public void testBooleanSystemPropertyGetDefaultString()
	{
		this.testBooleanSystemPropertyGetDefault("abc",null,false);
	}
	

	@Test
	public void testBooleanSystemPropertyGetDefaultNull()
	{
		this.testBooleanSystemPropertyGetDefault(null,true,false);
	}
	

	@Test
	public void testBooleanSystemPropertyGetDefaultTrue()
	{
		this.testBooleanSystemPropertyGetDefault(true,null,true);
	}
	

	@Test
	public void testBooleanSystemPropertyGetDefaultFalse()
	{
		this.testBooleanSystemPropertyGetDefault(false,true,false);
	}
	

	protected void testBooleanSystemPropertyGetDefault(final Object BOOLEAN_VALUE,
			final Boolean DEAULT_VALUE, final Boolean EXPECTED_VALUE)
	{
		
		assertNull(System.getProperty(PROPERTY_NAME));
		
		System.setProperty(PROPERTY_NAME,String.valueOf(BOOLEAN_VALUE));
		
		assertNotNull(System.getProperty(PROPERTY_NAME));
		
		BooleanSystemProperty bool = new BooleanSystemProperty(PROPERTY_NAME);
		
		assertSame(EXPECTED_VALUE,bool.getValue(DEAULT_VALUE));
		
	}
	

	@Test()
	public void testStringSystemPropertySetString()
	{
		this.testStringSystemPropertySet("hans","hans");
	}
	

	@Test()
	public void testStringSystemPropertySetNull()
	{
		/*
		 * There is no parsing in String, so a "null" String can not be parsed
		 * to NULLPOINTER.
		 */
		this.testStringSystemPropertySet(null,"null");
	}
	

	@Test()
	public void testStringSystemPropertySetNullString()
	{
		this.testStringSystemPropertySet("null","null");
	}
	

	protected void testStringSystemPropertySet(final String STRING_VALUE,
			final String EXPECTED_VALUE)
	{
		assertNull(System.getProperty(PROPERTY_NAME));
		
		StringSystemProperty number = new StringSystemProperty(PROPERTY_NAME);
		
		number.setValue(STRING_VALUE);
		
		assertNotNull(System.getProperty(PROPERTY_NAME));
		
		final String stringValueFromProperties = System.getProperty(PROPERTY_NAME);
		
		assertSame(EXPECTED_VALUE,stringValueFromProperties);
		
	}
	

	@Test
	public void testStringSystemPropertyGetNull()
	{
		this.testStringSystemPropertyGet(null,"null");
	}
	

	@Test
	public void testStringSystemPropertyGetNumber()
	{
		this.testStringSystemPropertyGet("-100","-100");
	}
	

	@Test
	public void testStringSystemPropertyGetString()
	{
		this.testStringSystemPropertyGet("borat","borat");
	}
	

	protected void testStringSystemPropertyGet(final Object STRING_VALUE,
			final String EXPECTED_VALUE)
	{
		
		assertNull(System.getProperty(PROPERTY_NAME));
		
		System.setProperty(PROPERTY_NAME,String.valueOf(STRING_VALUE));
		
		assertNotNull(System.getProperty(PROPERTY_NAME));
		
		StringSystemProperty str = new StringSystemProperty(PROPERTY_NAME);
		
		assertSame(EXPECTED_VALUE,str.getValue());
		
	}
	

	@Test
	public void testStringSystemPropertyNotThere()
	{
		StringSystemProperty str = new StringSystemProperty(PROPERTY_NAME);
		assertNull(str.getValue());
	}
	

	@Test
	public void testStringSystemPropertyNotThereDefault()
	{
		
		final String DEFAULT_VALUE = "nice";
		
		StringSystemProperty str = new StringSystemProperty(PROPERTY_NAME);
		assertSame(DEFAULT_VALUE,str.getValue(DEFAULT_VALUE));
	}
	

	@Test
	public void testIntegerSystemPropertyNotThere()
	{
		IntegerSystemProperty str = new IntegerSystemProperty(PROPERTY_NAME);
		assertNull(str.getValue());
	}
	

	@Test
	public void testIntegerSystemPropertyNotThereDefault()
	{
		
		final Integer DEFAULT_VALUE = 100;
		
		IntegerSystemProperty str = new IntegerSystemProperty(PROPERTY_NAME);
		assertSame(DEFAULT_VALUE,str.getValue(DEFAULT_VALUE));
	}
	
	
	
	
	@Test
	public void testBooleanSystemPropertyNotThere()
	{
		BooleanSystemProperty str = new BooleanSystemProperty(PROPERTY_NAME);
		assertNull(str.getValue());
	}
	

	@Test
	public void testBooleanSystemPropertyNotThereDefault()
	{
		
		final Boolean DEFAULT_VALUE = null;
		
		BooleanSystemProperty str = new BooleanSystemProperty(PROPERTY_NAME);
		assertSame(DEFAULT_VALUE,str.getValue(DEFAULT_VALUE));
	}
	
}
