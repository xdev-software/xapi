/**
 * 
 */
package com.xdev.jadoth.sqlengine.dbms;

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

import static com.xdev.jadoth.Jadoth.mergeInto;

import java.util.HashMap;
import java.util.Iterator;



/**
 * @author Thomas Muenz
 *
 */
public interface DbmsSyntax<A extends DbmsAdaptor<A>> extends Iterable<String>
{
	/**
	 * Note that <code>reserved word</code> and <code>keyword</code> are different types of words.<br>
	 * According to SQL Standard, a <code>keyword</code> is either a <code>reserved word</code> 
	 * or a <code>non-reserved word</code>.
	 * <p>
	 * Note: <code>non-reserved word</code> does not mean "not a reserved word" but describes another list of words
	 * in the SQL Standard. Not sure yet where the difference is. 
	 * 
	 * @param s
	 * @return
	 */
	public boolean isKeyword(String s);
	public boolean isNormalizedKeyword(String s);
	public boolean isKeyword(String s, boolean normalize);
	public Iterable<String> iterateKeywords();

	public boolean isReservedWord(String s);
	public boolean isNormalizedReservedWord(String s);
	public boolean isReservedWord(String s, boolean normalize);
	public Iterable<String> iterateReservedWords();
	
	public boolean isNonReservedWord(String s);
	public boolean isNormalizedNonReservedWord(String s);
	public boolean isNonReservedWord(String s, boolean normalize);
	public Iterable<String> iterateNonReservedWords();
	
	
	public abstract class Implementation<A extends DbmsAdaptor<A>> implements DbmsSyntax<A>
	{
		///////////////////////////////////////////////////////////////////////////
		// static methods //
		///////////////////
		
		/**
		 * 
		 * @param words
		 * @return
		 */
		public static final HashMap<String, Object> wordSet(final String... words)
		{
			if(words == null) return new HashMap<String, Object>(0);
			
			final HashMap<String, Object> map = new HashMap<String, Object>(words.length*20);
			for(final String word : words) {
				map.put(word, null);
			}
			return map;
		}
		
		
		/**
		 * 
		 * @param reservedWords
		 * @param nonReservedWords
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static final HashMap<String, Object> createKeywordSet(
			final HashMap<String, Object> reservedWords, final HashMap<String, Object> nonReservedWords
		)
		{	
			if(reservedWords == null){
				if(nonReservedWords == null){
					return new HashMap<String, Object>(0);
				}
				return nonReservedWords;
			}
			else if(nonReservedWords == null){
				return reservedWords;
			}
			
			return mergeInto(
				new HashMap<String, Object>((reservedWords.size()+nonReservedWords.size())*2),
				reservedWords, nonReservedWords
			);
		}
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////
		
		private final HashMap<String, Object> reservedWords;
		private final HashMap<String, Object> nonReservedWords;
		private final HashMap<String, Object> keywords;
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////
		
		/**
		 * @param reservedWords
		 * @param nonReservedWords
		 * @param keywords
		 */
		protected Implementation(
			final HashMap<String, Object> reservedWords, 
			final HashMap<String, Object> nonReservedWords
		)
		{
			super();
			this.reservedWords    = reservedWords    != null ?reservedWords    :new HashMap<String, Object>(0);
			this.nonReservedWords = nonReservedWords != null ?nonReservedWords :new HashMap<String, Object>(0);
			this.keywords = createKeywordSet(reservedWords, nonReservedWords);
		}
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////
		
		/**
		 * Checks case sensitive for performance reasons.
		 * See <code>isKeyword(String, boolean)</code> for checking case insensitive.
		 * 
		 * @param s
		 * @return
		 */
		@Override
		public boolean isKeyword(final String s)
		{
			return keywords.containsKey(s.toUpperCase());
		}
		/**
		 * @param s
		 * @param normalize
		 * @return
		 */
		@Override
		public boolean isKeyword(final String s, final boolean normalize)
		{
			return keywords.containsKey(normalize ?s.toUpperCase() :s);
		}
		/**
		 * @param s
		 * @return
		 */
		@Override
		public boolean isNonReservedWord(final String s)
		{
			return nonReservedWords.containsKey(s.toUpperCase());
		}
		/**
		 * @param s
		 * @param normalize
		 * @return
		 */
		@Override
		public boolean isNonReservedWord(final String s, final boolean normalize)
		{
			return nonReservedWords.containsKey(normalize ?s.toUpperCase() :s);
		}
		/**
		 * @param s
		 * @return
		 */
		@Override
		public boolean isReservedWord(final String s)
		{
			return reservedWords.containsKey(s.toUpperCase());
		}
		/**
		 * @param s
		 * @param normalize
		 * @return
		 */
		@Override
		public boolean isReservedWord(final String s, final boolean normalize)
		{
			return reservedWords.containsKey(normalize ?s.toUpperCase() :s);
		}
		/**
		 * @param s
		 * @return
		 */
		@Override
		public boolean isNormalizedKeyword(String s)
		{
			return this.keywords.containsKey(s);
		}
		/**
		 * @param s
		 * @return
		 */
		@Override
		public boolean isNormalizedNonReservedWord(final String s)
		{
			return this.nonReservedWords.containsKey(s);
		}
		/**
		 * @param s
		 * @return
		 */
		@Override
		public boolean isNormalizedReservedWord(String s)
		{
			return this.reservedWords.containsKey(s);
		}
		
		
		
		/**
		 * @return
		 */
		@Override
		public Iterable<String> iterateKeywords()
		{
			return keywords.keySet();
		}
		/**
		 * @return
		 */
		@Override
		public Iterable<String> iterateNonReservedWords()
		{
			return reservedWords.keySet();
		}
		/**
		 * @return
		 */
		@Override
		public Iterable<String> iterateReservedWords()
		{
			return reservedWords.keySet();
		}
		
		
		/**
		 * @return
		 */
		@Override
		public Iterator<String> iterator()
		{
			return this.keywords.keySet().iterator();
		}



		
		
	}
}
