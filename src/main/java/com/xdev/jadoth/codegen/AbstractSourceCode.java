
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
import static com.xdev.jadoth.codegen.Punctuation.TAB;
import static com.xdev.jadoth.codegen.Punctuation._;
import static com.xdev.jadoth.codegen.Punctuation.comma_;
import static com.xdev.jadoth.codegen.Punctuation.dot;
import static com.xdev.jadoth.codegen.Punctuation.n;
import static com.xdev.jadoth.codegen.Punctuation.par;
import static com.xdev.jadoth.codegen.Punctuation.rap;
import static com.xdev.jadoth.codegen.Punctuation.scol;





/**
 * The Class AbstractSourceCode.
 * 
 * @param <S> the generic type
 */
public abstract class AbstractSourceCode<S extends AbstractSourceCode<S>>
{
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	/** The code. */
	protected final StringBuilder code = new StringBuilder(10*1024);
	
	/** The tab level. */
	protected int tabLevel = 0;
	
	/** The syntax. */
	protected AbstractSyntax syntax = null;


	/**
	 * Gets the syntax.
	 * 
	 * @return the syntax
	 */
	public AbstractSyntax getSyntax() {
		return this.syntax;
	}



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new abstract source code.
	 * 
	 * @param syntax the syntax
	 */
	protected AbstractSourceCode(final AbstractSyntax syntax) {
		super();
		this.syntax = syntax;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public StringBuilder getCode() {
		return this.code;
	}



	/**
	 * Tab.
	 * 
	 * @param tabLevel the tab level
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S tab(int tabLevel) {
		while(tabLevel-->0) this.code.append(TAB);
		return (S)this;
	}


	/**
	 * Break line.
	 * 
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S breakLine() {
		this.code.append(n);
		return (S)this;
	}


	/**
	 * Ln.
	 * 
	 * @return the s
	 */
	public S ln() {
		return this.breakLine();
	}
	
	/**
	 * End line.
	 * 
	 * @return the s
	 */
	public S endLine() {
		this.code.append(scol);
		return this.breakLine();
	}
	
	
	/**
	 * _is_.
	 * 
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S _is_() {
		this.code.append(_).append(this.syntax.getAssignOperator()).append(_);
		return (S)this;
	}

	/**
	 * Blank line.
	 * 
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S blankLine(){
		if(this.code.length() > 0 && this.code.charAt(this.code.length()-1) != n) {
			this.code.append(n);
		}
		this.code.append(n);
		return (S)this;
	}

	/**
	 * Append.
	 * 
	 * @param elements the elements
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S append(final Object... elements) {
		appendArraySeperated(this.code, ' ', elements);
		return (S)this;
	}

	/**
	 * Adds the line.
	 * 
	 * @param elements the elements
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S addLine(final Object... elements) {
		this.tab(this.tabLevel);
		appendArraySeperated(this.code, ' ', elements);
		this.breakLine();
		return (S)this;
	}

	/**
	 * Adds the tabbed line.
	 * 
	 * @param tabLevel the tab level
	 * @param line the line
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S addTabbedLine(final int tabLevel, final Object... line) {
		this.tab(tabLevel);
		this.append(line);
		this.breakLine();
		return (S)this;
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.code.toString();
	}

	/**
	 * Block new line start.
	 * 
	 * @return the s
	 */
	public S blockNewLineStart() {
		this.breakLine();
		this.tab(this.tabLevel);
		return this.blockStart();
	}
	
	/**
	 * Block start.
	 * 
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S blockStart() {
		this.code.append("{");
		this.breakLine();
		this.tabLevel++;
		return (S)this;
	}
	
	/**
	 * Block end.
	 * 
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S blockEnd() {
		if(this.tabLevel > 0) {
			this.tabLevel--;
		}
		this.tab(this.tabLevel);
		this.code.append("}");
		this.breakLine();
		return (S)this;
	}
	
	
	
	
	/**
	 * If_.
	 * 
	 * @param condition the condition
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S if_(final String condition) {
		this.tab(this.tabLevel);
		this.syntax._if(this.code, condition).append(_);
		return (S)this;
	}
	
	/**
	 * Else_.
	 * 
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S else_() {
		this.tab(this.tabLevel);
		this.syntax._else(this.code).append(_);
		return (S)this;
	}

	
	/**
	 * Quote.
	 * 
	 * @param s the s
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S quote(final String s) {
		this.syntax._quote(this.code, s);
		return (S)this;
	}
	
	/**
	 * Dot.
	 * 
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S dot() {
		this.code.append(dot);
		return (S)this;
	}
	
	/**
	 * Space.
	 * 
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S space() {
		this.code.append(_);
		return (S)this;
	}
	
	/**
	 * Modifier.
	 * 
	 * @param modifiers the modifiers
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S modifier(final String... modifiers) {
		this.tab(this.tabLevel);
		this.syntax._modifier(this.code, modifiers).append(_);
		return (S)this;
	}
	
	/**
	 * Indent.
	 * 
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S indent() {
		this.tab(this.tabLevel);
		return (S)this;
	}
	
	/**
	 * Method.
	 * 
	 * @param methodname the methodname
	 * @param args the args
	 * @return the s
	 */
	@SuppressWarnings("unchecked")
	public S method(final String methodname, final String... args) 
	{
		final StringBuilder code = this.code;
		code.append(methodname).append(par);
		if(args != null) {
			for (int i = 0; i < args.length; i++) {
				if(i > 0) {
					code.append(comma_);
				}
				code.append(args[i]);
			}
		}
		code.append(rap);
		return (S)this;
	}
	
	

}
