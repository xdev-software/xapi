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
package com.xdev.jadoth.codegen;


/**
 * The Interface Punctuation.
 * 
 * @author Thomas Muenz
 */
public interface Punctuation {
	
	/** The Constant n. */
	public static final char n = '\n';
	
	/** The Constant t. */
	public static final char t = '\t';
	
	/** The Constant qt. */
	public static final char qt = '"';
	
	/** The Constant apo. */
	public static final char apo = '\'';
	
	/** The Constant at. */
	public static final char at = '@';
	
	/** The Constant cma. */
	public static final char cma = ',';
	
	/** The Constant str. */
	public static final char str = '*';
	
	/** The Constant d. */
	public static final char d = '.';
	
	/** The Constant qM. */
	public static final char qM = '?';

	/** The Constant _. */
	public static final char _ = ' ';
	
	/** The Constant NEW_LINE. */
	public static final String NEW_LINE = ""+n;
	
	/** The Constant TAB. */
	public static final String TAB = ""+t;
	
	/** The Constant quote. */
	public static final String quote = ""+qt;
	
	/** The Constant scol. */
	public static final String scol = ";";

	
	/** The Constant par. */
	public static final String par = "(";
	
	/** The Constant rap. */
	public static final String rap = ")";
	
	/** The Constant comma. */
	public static final String comma = ""+cma;
	
	/** The Constant dot. */
	public static final String dot = ""+d;
	
	/** The Constant star. */
	public static final String star = ""+str;
	
	/** The Constant qMark. */
	public static final String qMark = ""+qM;


	//comparators
	/** The Constant is. */
	public static final String is = "=";
	
	/** The Constant ne1. */
	public static final String ne1 = "!=";
	
	/** The Constant ne2. */
	public static final String ne2 = "<>";
	
	/** The Constant gt. */
	public static final String gt = ">";
	
	/** The Constant lt. */
	public static final String lt = "<";
	
	/** The Constant gte. */
	public static final String gte = ">=";
	
	/** The Constant lte. */
	public static final String lte = "<=";
	
	/** The Constant comma_. */
	public static final String comma_ = cma+""+_;
}
