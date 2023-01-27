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

package com.xdev.jadoth.sqlengine.internal;

import static com.xdev.jadoth.sqlengine.SQL.LANG._AS_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.t;

import java.io.Serializable;

import com.xdev.jadoth.lang.Copyable;
import com.xdev.jadoth.sqlengine.SQL.Punctuation;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.types.TableQuery;



/**
 * The Class QueryPart.
 */
public abstract class QueryPart implements Serializable, Copyable
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = -6899616612478028117L;

	/** The Constant defaultQueryStringBuilderLength. */
	public static final int defaultQueryStringBuilderLength = 1024;

	/** The Constant defaultClauseStringBuilderLength. */
	public static final int defaultClauseStringBuilderLength = 512;

	/** The Constant defaultExpressionStringBuilderLength. */
	public static final int defaultExpressionStringBuilderLength = 256;


	/** The Constant SINGLELINE. */
	public static final int SINGLELINE        = getBitFlag(); //   1

	/** The Constant PACKED. */
	public static final int PACKED            = getBitFlag(); //   2

	/** The Constant UNQUALIFIED. */
	public static final int UNQUALIFIED       = getBitFlag(); //   4

	/** The Constant OMITALIAS. */
	public static final int OMITALIAS         = getBitFlag(); //   8

	/** The Constant MINIINDENT. */
	public static final int MINIINDENT        = getBitFlag(); //  16

	/** The Constant ASEXPRESSION. */
	public static final int ASEXPRESSION      = getBitFlag(); //  32

	/** The Constant QUALIFY_BY_TABLE. */
	public static final int QUALIFY_BY_TABLE  = getBitFlag(); //  64

	/** The Constant ESCAPE_QUOTES. */
	public static final int ESCAPE_QUOTES     = getBitFlag(); // 128

	/** The Constant ESCAPE_DBL_QUOTES. */
	public static final int ESCAPE_DBL_QUOTES = getBitFlag(); // 256
	
	public static final int DELIMIT_TABLE_IDENTIFIERS  = getBitFlag(); // 512
	
	public static final int DELIMIT_COLUMN_IDENTIFIERS = getBitFlag(); // 1024
	
	public static final int DELIMIT_ALIASES   = getBitFlag(); // 2048

	/** The Constant FLAGS_TAKEN. */
	public static final int FLAGS_TAKEN = initFlagsTaken();

	/**
	 * Inits the flags taken.
	 *
	 * @return the int
	 */
	private static int initFlagsTaken()
	{
		return SINGLELINE | PACKED | UNQUALIFIED | OMITALIAS | MINIINDENT | ASEXPRESSION | QUALIFY_BY_TABLE
		| ESCAPE_QUOTES | ESCAPE_DBL_QUOTES | DELIMIT_TABLE_IDENTIFIERS | DELIMIT_COLUMN_IDENTIFIERS | DELIMIT_ALIASES
		;
	}



	///////////////////////////////////////////////////////////////////////////
	// static fields    //
	/////////////////////

	/** The current shift index. */
	private static int currentShiftIndex = 0;

	/**
	 * Gets the bit flag.
	 *
	 * @return the bit flag
	 */
	private static int getBitFlag() {
		return 1 << currentShiftIndex++;
	}




	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////

	/**
	 * Checks if is single line.
	 *
	 * @param flags the flags
	 * @return true, if is single line
	 */
	public static final boolean isSingleLine(final int flags) {
		return (flags & SINGLELINE) != 0;
	}

	/**
	 * Checks if is packed.
	 *
	 * @param flags the flags
	 * @return true, if is packed
	 */
	public static final boolean isPacked(final int flags) {
		return (flags & PACKED) != 0;
	}

	/**
	 * Checks if is unqualified.
	 *
	 * @param flags the flags
	 * @return true, if is unqualified
	 */
	public static final boolean isUnqualified(final int flags) {
		return (flags & UNQUALIFIED) != 0;
	}

	/**
	 * Checks if is omit alias.
	 *
	 * @param flags the flags
	 * @return true, if is omit alias
	 */
	public static final boolean isOmitAlias(final int flags) {
		return (flags & OMITALIAS) != 0;
	}

	/**
	 * Checks if is mini indent.
	 *
	 * @param flags the flags
	 * @return true, if is mini indent
	 */
	public static final boolean isMiniIndent(final int flags) {
		return (flags & MINIINDENT) != 0;
	}

	/**
	 * Checks if is as expression.
	 *
	 * @param flags the flags
	 * @return true, if is as expression
	 */
	public static final boolean isAsExpression(final int flags) {
		return (flags & ASEXPRESSION) != 0;
	}

	/**
	 * Checks if is qualify by table.
	 *
	 * @param flags the flags
	 * @return true, if is qualify by table
	 */
	public static final boolean isQualifyByTable(final int flags) {
		return (flags & QUALIFY_BY_TABLE) != 0;
	}

	/**
	 * Checks if is escape quotes.
	 *
	 * @param flags the flags
	 * @return true, if is escape quotes
	 */
	public static final boolean isEscapeQuotes(final int flags) {
		return (flags & ESCAPE_QUOTES) != 0;
	}

	/**
	 * Checks if is escape double quotes.
	 *
	 * @param flags the flags
	 * @return true, if is escape double quotes
	 */
	public static final boolean isEscapeDoubleQuotes(final int flags) {
		return (flags & ESCAPE_DBL_QUOTES) != 0;
	}
	
	public static final boolean isDelimitTableIdentifiers(final int flags) {
		return (flags & DELIMIT_TABLE_IDENTIFIERS) != 0;
	}
	
	public static final boolean isDelimitColumnIdentifiers(final int flags) {
		return (flags & DELIMIT_COLUMN_IDENTIFIERS) != 0;
	}
	
	public static final boolean isDelimitAliases(final int flags) {
		return (flags & DELIMIT_ALIASES) != 0;
	}

	/**
	 * Prints the flags.
	 *
	 * @param flags the flags
	 */
	public static final void printFlags(final int flags) {
		System.out.println(flagsToString(flags));
	}

	/**
	 * Flags to string.
	 *
	 * @param flags the flags
	 * @return the string
	 */
	public static final String flagsToString(final int flags)
	{
		/* (27.02.2010)NOTE:
		 * If anyone knows a working Java lib function for this, like String.format(), let me know ^^.
		 * Couldn't get it to work nor found anything on google. Unbelievable...
		 */
		final StringBuilder sb = new StringBuilder(32);
		final String bits = Integer.toBinaryString(flags);
		final int bitsLength = bits.length();

		int zeroCount = sb.capacity();
		while(zeroCount --> bitsLength) {
			sb.append('0');
		}
		sb.append(bits);

		return sb.toString();
	}


	/**
	 * Sets the single line.
	 *
	 * @param flags the flags
	 * @param singleLine the single line
	 * @return the int
	 */
	public static final int setSingleLine(final int flags, final boolean singleLine) {
		return singleLine ?flags | SINGLELINE :flags &~SINGLELINE;
	}

	/**
	 * Sets the packed.
	 *
	 * @param flags the flags
	 * @param packed the packed
	 * @return the int
	 */
	public static final int setPacked(final int flags, final boolean packed) {
		return packed ?flags | PACKED :flags &~PACKED;
	}

	/**
	 * Sets the qualified.
	 *
	 * @param flags the flags
	 * @param qualified the qualified
	 * @return the int
	 */
	public static final int setQualified(final int flags, final boolean qualified) {
		return qualified ?flags | UNQUALIFIED :flags &~UNQUALIFIED;
	}

	/**
	 * Sets the use alias.
	 *
	 * @param flags the flags
	 * @param useAlias the use alias
	 * @return the int
	 */
	public static final int setUseAlias(final int flags, final boolean useAlias) {
		return useAlias ?flags | OMITALIAS :flags &~OMITALIAS;
	}

	/**
	 * Sets the mini indent.
	 *
	 * @param flags the flags
	 * @param miniIndent the mini indent
	 * @return the int
	 */
	public static final int setMiniIndent(final int flags, final boolean miniIndent) {
		return miniIndent ?flags | MINIINDENT :flags &~MINIINDENT;
	}

	/**
	 * Sets the as expression.
	 *
	 * @param flags the flags
	 * @param asExpression the as expression
	 * @return the int
	 */
	public static final int setAsExpression(final int flags, final boolean asExpression) {
		return asExpression ?flags | ASEXPRESSION :flags &~ASEXPRESSION;
	}

	/**
	 * Sets the qualify by table.
	 *
	 * @param flags the flags
	 * @param qualifyByTable the qualify by table
	 * @return the int
	 */
	public static final int setQualifyByTable(final int flags, final boolean qualifyByTable) {
		return qualifyByTable ?flags | QUALIFY_BY_TABLE :flags &~QUALIFY_BY_TABLE;
	}

	/**
	 * Sets the escape quotes.
	 *
	 * @param flags the flags
	 * @param escapeQuotes the escape quotes
	 * @return the int
	 */
	public static final int setEscapeQuotes(final int flags, final boolean escapeQuotes) {
		return escapeQuotes ?flags | ESCAPE_QUOTES :flags &~ESCAPE_QUOTES;
	}

	/**
	 * Sets the escape double quotes.
	 *
	 * @param flags the flags
	 * @param escapeDoubleQuotes the escape double quotes
	 * @return the int
	 */
	public static final int setEscapeDoubleQuotes(final int flags, final boolean escapeDoubleQuotes) {
		return escapeDoubleQuotes ?flags | ESCAPE_DBL_QUOTES :flags &~ESCAPE_DBL_QUOTES;
	}
	
	public static final int setDelimitTableIdentifiers(final int flags, final boolean delimitTableIdentifiers) {
		return delimitTableIdentifiers ?flags | DELIMIT_TABLE_IDENTIFIERS :flags &~DELIMIT_TABLE_IDENTIFIERS;
	}
	
	public static final int setDelimitColumnIdentifiers(final int flags, final boolean delimitColumnIdentifiers) {
		return delimitColumnIdentifiers ?flags | DELIMIT_COLUMN_IDENTIFIERS :flags &~DELIMIT_COLUMN_IDENTIFIERS;
	}
	
	public static final int setDelimitAliases(final int flags, final boolean delimitAliases) {
		return delimitAliases ?flags | DELIMIT_ALIASES :flags &~DELIMIT_ALIASES;
	}


	/**
	 * Bit single line.
	 *
	 * @param singleLine the single line
	 * @return the int
	 */
	public static final int bitSingleLine(final boolean singleLine) {
		return singleLine ?SINGLELINE :0;
	}

	/**
	 * Bit packed.
	 *
	 * @param packed the packed
	 * @return the int
	 */
	public static final int bitPacked(final boolean packed) {
		return packed ?PACKED :0;
	}

	/**
	 * Bit qualified.
	 *
	 * @param qualified the qualified
	 * @return the int
	 */
	public static final int bitQualified(final boolean qualified) {
		return qualified ?UNQUALIFIED :0;
	}

	/**
	 * Bit omit alias.
	 *
	 * @param omitAlias the omit alias
	 * @return the int
	 */
	public static final int bitOmitAlias(final boolean omitAlias) {
		return omitAlias ?OMITALIAS :0;
	}

	/**
	 * Bit mini indent.
	 *
	 * @param miniIndent the mini indent
	 * @return the int
	 */
	public static final int bitMiniIndent(final boolean miniIndent) {
		return miniIndent ?MINIINDENT :0;
	}

	/**
	 * Bit as expression.
	 *
	 * @param asExpression the as expression
	 * @return the int
	 */
	public static final int bitAsExpression(final boolean asExpression) {
		return asExpression ?ASEXPRESSION :0;
	}

	/**
	 * Bit qualify by table.
	 *
	 * @param qualifyByTable the qualify by table
	 * @return the int
	 */
	public static final int bitQualifyByTable(final boolean qualifyByTable) {
		return qualifyByTable ?QUALIFY_BY_TABLE :0;
	}

	/**
	 * Escape quotes.
	 *
	 * @param escapeQuotes the escape quotes
	 * @return the int
	 */
	public static final int EscapeQuotes(final boolean escapeQuotes) {
		return escapeQuotes ?ESCAPE_QUOTES :0;
	}

	/**
	 * Bit escape double quotes.
	 *
	 * @param escapeDoubleQuotes the escape double quotes
	 * @return the int
	 */
	public static final int bitEscapeDoubleQuotes(final boolean escapeDoubleQuotes) {
		return escapeDoubleQuotes ?ESCAPE_DBL_QUOTES :0;
	}
	
	public static final int bitDelimitTableIdentifiers(final boolean delimitTableIdentifiers) {
		return delimitTableIdentifiers ?DELIMIT_TABLE_IDENTIFIERS :0;
	}
	
	public static final int bitDelimitColumnIdentifiers(final boolean delimitColumnIdentifiers) {
		return delimitColumnIdentifiers ?DELIMIT_COLUMN_IDENTIFIERS :0;
	}
	
	public static final int bitDelimitAliases(final boolean delimitAliases) {
		return delimitAliases ?DELIMIT_ALIASES :0;
	}





	/**
	 * Indent.
	 *
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param singleLine the single line
	 * @return the string builder
	 */
	public static final StringBuilder indent(final StringBuilder sb, int indentLevel, final boolean singleLine)
	{
		if(singleLine) return sb;

		while(indentLevel-->0){
			sb.append(t);
		}
		return sb;
	}

	/**
	 * Assemble query.
	 *
	 * @param query the query
	 * @param dmlAssembler the dml assembler
	 * @param indentLevel the indent level
	 * @param packed the packed
	 * @param singleLine the single line
	 * @param asExpression the as expression
	 * @return the string
	 */
	public static String assembleQuery(
		final TableQuery query,
		final DbmsDMLAssembler<?> dmlAssembler,
		final int indentLevel,
		final boolean packed,
		final boolean singleLine,
		final boolean asExpression
	)
	{
		return assembleObject(
			query,
			dmlAssembler,
			new StringBuilder(defaultQueryStringBuilderLength),
			indentLevel,
			bitPacked(packed) | bitSingleLine(singleLine) | bitAsExpression(asExpression)
		).toString();
	}

	/**
	 * Assemble object.
	 *
	 * @param object the object
	 * @param dmlAssembler the dml assembler
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	public static StringBuilder assembleObject(
		final Object object,
		final DbmsDMLAssembler<?> dmlAssembler,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		//Note: if a new sb should be created inside, that one must be returned
		
		//apply protected class-version assemble() prior to calling dmlAssembler
		if(object instanceof QueryPart){
			return ((QueryPart)object).assemble(dmlAssembler, sb, indentLevel, flags);
		}
		//standard case: let dmlAssembler decide completely alone how to assemble
		return dmlAssembler.assembleObject(object, sb, indentLevel, flags);
	}

	/**
	 * Assemble alias.
	 *
	 * @param sb the sb
	 * @param alias the alias
	 * @param useAlias the use alias
	 * @return the string builder
	 */
	protected static StringBuilder assembleAlias(final StringBuilder sb, final String alias, final boolean useAlias)
	{
		if(useAlias && alias != null){
			sb.append(_AS_).append(alias);
		}
		return sb;
	}



	/**
	 * Concat sql expressions.
	 *
	 * @param dmlAssembler the dml assembler
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @param concatenator the concatenator
	 * @param parts the parts
	 * @return the string
	 */
	public static final String concatSqlExpressions(
		final DbmsDMLAssembler<?> dmlAssembler,
		StringBuilder sb,
		final int indentLevel,
		final int flags,
		final String concatenator,
		final SqlExpression... parts
	)
	{
		if(sb == null){
			sb = new StringBuilder(QueryPart.defaultExpressionStringBuilderLength);
		}

		boolean notEmtpy = false;
		for (int i = 0; i < parts.length ; i++) {
			if(parts[i] == null) {
				continue;
			}
			if(notEmtpy) {
				sb.append(concatenator);
			}
			else {
				notEmtpy = true;
			}
			assembleObject(parts[i], dmlAssembler, sb, indentLevel, flags);
		}
		return sb.toString();
	}

	/**
	 * Function.
	 *
	 * @param dmlAssembler the dml assembler
	 * @param name the name
	 * @param flags the flags
	 * @param params the params
	 * @return the string
	 */
	public static final String function(
		final DbmsDMLAssembler<?> dmlAssembler, final String name, final int flags, final Object... params
	)
	{
		final StringBuilder sb = new StringBuilder(50);
		sb.append(name);
		sb.append(Punctuation.par);
		sb.append(list(dmlAssembler, flags, params));
		sb.append(Punctuation.rap);
		return sb.toString();
	}

	/**
	 * List.
	 *
	 * @param dmlAssembler the dml assembler
	 * @param flags the flags
	 * @param parts the parts
	 * @return the string
	 */
	public static final String list(final DbmsDMLAssembler<?> dmlAssembler, final int flags, final Object... parts) {
		return concat(dmlAssembler, Punctuation.comma_, flags, parts);
	}

	/**
	 * Concat.
	 *
	 * @param dmlAssembler the dml assembler
	 * @param concatenator the concatenator
	 * @param flags the flags
	 * @param parts the parts
	 * @return the string
	 */
	public static final String concat(
		final DbmsDMLAssembler<?> dmlAssembler, final String concatenator, final int flags, final Object... parts
	)
	{
		final StringBuilder sb = new StringBuilder(128);
		final int lastIndex = parts.length-1;
		for (int i = 0; i <= lastIndex ; i++) {
			if(parts[i] == null) {
				continue;
			}
			if(sb.length() > 0) {
				sb.append(concatenator);
			}
			assembleObject(parts[i], dmlAssembler, sb, 0, flags);
		}
		return sb.toString();
	}



	///////////////////////////////////////////////////////////////////////////
	// abstract methods //
	/////////////////////

	/**
	 * Assemble.
	 *
	 * @param dmlAssembler the dml assembler
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected abstract StringBuilder assemble(
		DbmsDMLAssembler<?> dmlAssembler, StringBuilder sb, int indentLevel, int flags
	);

	public String keyword()
	{
		return null;
	}
}
