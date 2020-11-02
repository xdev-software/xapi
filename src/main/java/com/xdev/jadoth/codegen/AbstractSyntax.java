
package com.xdev.jadoth.codegen;

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

import static com.xdev.jadoth.Jadoth.appendArraySeperated;
import static com.xdev.jadoth.codegen.Punctuation.is;
import static com.xdev.jadoth.codegen.Punctuation.par;
import static com.xdev.jadoth.codegen.Punctuation.quote;
import static com.xdev.jadoth.codegen.Punctuation.rap;
import static com.xdev.jadoth.codegen.java.Java.Lang.$else;
import static com.xdev.jadoth.codegen.java.Java.Lang.$if;
import static com.xdev.jadoth.codegen.java.Java.Lang.blockEnd;
import static com.xdev.jadoth.codegen.java.Java.Lang.blockStart;


/**
 * The Class AbstractSyntax.
 * 
 * @author Thomas Muenz
 */
public abstract class AbstractSyntax 
{
	
	
	/**
	 * Gets the quote.
	 * 
	 * @return the quote
	 */
	public String getQuote(){
		return quote;
	}
	
	/**
	 * Gets the assign operator.
	 * 
	 * @return the assign operator
	 */
	public String getAssignOperator(){
		return is;
	}
	
	
	/**
	 * _if.
	 * 
	 * @param sb the sb
	 * @param condition the condition
	 * @return the string builder
	 */
	public StringBuilder _if(StringBuilder sb, final String condition){
		if(sb == null){
			sb = new StringBuilder(128);
		}		
		return sb.append($if).append(par).append(condition).append(rap);
	}
	
	/**
	 * $if.
	 * 
	 * @param condition the condition
	 * @return the string
	 */
	public String $if(final String condition){
		return this._if(null, condition).toString();
	}
	
	
	
	/**
	 * _else.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	public StringBuilder _else(StringBuilder sb){
		if(sb == null){
			sb = new StringBuilder(128);
		}		
		return sb.append($else);
	}
	
	/**
	 * $else.
	 * 
	 * @return the string
	 */
	public String $else(){
		return this._else(null).toString();
	}
	
	
	
	/**
	 * _block start.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	public StringBuilder _blockStart(StringBuilder sb){
		if(sb == null){
			sb = new StringBuilder(128);
		}		
		return sb.append(blockStart);
	}
	
	/**
	 * $block start.
	 * 
	 * @return the string
	 */
	public String $blockStart(){
		return this._blockStart(null).toString();
	}
	
	
	/**
	 * _block end.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	public StringBuilder _blockEnd(StringBuilder sb){
		if(sb == null){
			sb = new StringBuilder(128);
		}		
		return sb.append(blockEnd);
	}
	
	/**
	 * $block end.
	 * 
	 * @return the string
	 */
	public String $blockEnd(){
		return this._blockEnd(null).toString();
	}
	

	
	/**
	 * _quote.
	 * 
	 * @param sb the sb
	 * @param s the s
	 * @return the string builder
	 */
	public StringBuilder _quote(StringBuilder sb, final String s){
		if(sb == null){
			sb = new StringBuilder(128);
		}		
		return sb.append(this.getQuote()).append(s).append(this.getQuote());
	}
	
	/**
	 * $quote.
	 * 
	 * @param s the s
	 * @return the string
	 */
	public String $quote(final String s){
		return this._quote(null, s).toString();
	}
	
	
	/**
	 * _modifier.
	 * 
	 * @param sb the sb
	 * @param modifiers the modifiers
	 * @return the string builder
	 */
	public StringBuilder _modifier(StringBuilder sb, final String... modifiers){
		if(sb == null){
			sb = new StringBuilder(128);
		}		
		return appendArraySeperated(sb, ' ', (Object[])modifiers);
	}
	
	/**
	 * $modifier.
	 * 
	 * @param modifiers the modifiers
	 * @return the string
	 */
	public String $modifier(final String... modifiers){
		return this._modifier(null, modifiers).toString();
	}
	
	

}
