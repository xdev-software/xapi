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

package com.xdev.jadoth.sqlengine.dbms.standard;

import static com.xdev.jadoth.sqlengine.SQL.LANG.DEFAULT_VALUES;
import static com.xdev.jadoth.sqlengine.SQL.LANG.DISTINCT;
import static com.xdev.jadoth.sqlengine.SQL.LANG.EXCEPT;
import static com.xdev.jadoth.sqlengine.SQL.LANG.FETCH;
import static com.xdev.jadoth.sqlengine.SQL.LANG.FIRST;
import static com.xdev.jadoth.sqlengine.SQL.LANG.FROM;
import static com.xdev.jadoth.sqlengine.SQL.LANG.INTERSECT;
import static com.xdev.jadoth.sqlengine.SQL.LANG.INTO;
import static com.xdev.jadoth.sqlengine.SQL.LANG.OFFSET;
import static com.xdev.jadoth.sqlengine.SQL.LANG.ONLY;
import static com.xdev.jadoth.sqlengine.SQL.LANG.ROW;
import static com.xdev.jadoth.sqlengine.SQL.LANG.ROWS;
import static com.xdev.jadoth.sqlengine.SQL.LANG.UNION;
import static com.xdev.jadoth.sqlengine.SQL.LANG.UNION_ALL;
import static com.xdev.jadoth.sqlengine.SQL.LANG._ON_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.apo;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.blockCommentEnd;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.blockCommentStart;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.comma_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.dot;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.n;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.qt;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.selectItem_Comma_NEW_LINE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.selectItem_NEW_LINE_Comma;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.singleLineComment;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.star;
import static com.xdev.jadoth.sqlengine.dbms.standard.StandardDbmsAdaptor.IDENTIFIER_DELIMITER;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.ASEXPRESSION;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.ESCAPE_DBL_QUOTES;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.ESCAPE_QUOTES;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.MINIINDENT;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.UNQUALIFIED;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.bitDelimitAliases;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.bitDelimitColumnIdentifiers;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.bitDelimitTableIdentifiers;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.indent;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.isOmitAlias;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.isPacked;
import static com.xdev.jadoth.sqlengine.internal.QueryPart.isSingleLine;
import static com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression.Utils.getAlias;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.xdev.jadoth.sqlengine.DELETE;
import com.xdev.jadoth.sqlengine.INSERT;
import com.xdev.jadoth.sqlengine.SELECT;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.UPDATE;
import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsConfiguration;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.dbms.DbmsSyntax;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineAssembleException;
import com.xdev.jadoth.sqlengine.interfaces.AssembableSqlExpression;
import com.xdev.jadoth.sqlengine.internal.AssignmentColumnsClause;
import com.xdev.jadoth.sqlengine.internal.AssignmentValuesClause;
import com.xdev.jadoth.sqlengine.internal.DoubleQuotedExpression;
import com.xdev.jadoth.sqlengine.internal.FROM;
import com.xdev.jadoth.sqlengine.internal.GROUP_BY;
import com.xdev.jadoth.sqlengine.internal.HAVING;
import com.xdev.jadoth.sqlengine.internal.JoinClause;
import com.xdev.jadoth.sqlengine.internal.ORDER_BY;
import com.xdev.jadoth.sqlengine.internal.QueryPart;
import com.xdev.jadoth.sqlengine.internal.QuotedExpression;
import com.xdev.jadoth.sqlengine.internal.SET;
import com.xdev.jadoth.sqlengine.internal.SqlClause;
import com.xdev.jadoth.sqlengine.internal.SqlColumn;
import com.xdev.jadoth.sqlengine.internal.SqlCondition;
import com.xdev.jadoth.sqlengine.internal.SqlDate;
import com.xdev.jadoth.sqlengine.internal.SqlExpression;
import com.xdev.jadoth.sqlengine.internal.SqlTime;
import com.xdev.jadoth.sqlengine.internal.SqlTimestamp;
import com.xdev.jadoth.sqlengine.internal.WHERE;
import com.xdev.jadoth.sqlengine.internal.interfaces.SelectItem;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.types.Query;
import com.xdev.jadoth.sqlengine.types.TableQuery;


/**
 * The Class StandardDMLAssembler.
 *
 * @param <A> the generic type
 */
public class StandardDMLAssembler<A extends DbmsAdaptor<A>>
extends DbmsAdaptor.Member.Implementation<A>
implements DbmsDMLAssembler<A>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	/** The Constant DATE. */
	private static final String DATE = "yyyy-MM-dd";

	/** The Constant TIME. */
	private static final String TIME = "HH:mm:ss.SSSSSS";

	/** The Constant TIMESTAMP. */
	private static final String TIMESTAMP = DATE+" "+TIME;

	/** The Constant date2TIMESTAMP. */
	private static final SimpleDateFormat dateToTIMESTAMP = new SimpleDateFormat(TIMESTAMP);

	/** The Constant date2DATE. */
	private static final SimpleDateFormat dateToDATE = new SimpleDateFormat(DATE);

	/** The Constant date2TIME. */
	private static final SimpleDateFormat dateToTIME = new SimpleDateFormat(TIME);



	/** The Constant __. */
	protected static final String __ = "  ";

	/** The Constant _star. */
	protected static final String _star = _+star;

	/** The Constant OFFSET_. */
	protected static final String OFFSET_ = OFFSET+_;

	/** The Constant FETCH_FIRST_. */
	protected static final String FETCH_FIRST_ = FETCH+_+FIRST+_;

	/** The Constant _ROW. */
	protected static final String _ROW = _+ROW;

	/** The Constant _ROWS. */
	protected static final String _ROWS = _+ROWS;

	/** The Constant _ROW_ONLY. */
	protected static final String _ROW_ONLY = _+ROW+_+ONLY;

	/** The Constant _ROWS_ONLY. */
	protected static final String _ROWS_ONLY = _+ROWS+_+ONLY;

	/** The Constant _FROM_. */
	protected static final String _FROM_ = _+FROM+_;

	/** The Constant _INTO_. */
	protected static final String _INTO_ = _+INTO+_;



	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////

	/**
	 * Gets the singleton standard dml assembler.
	 *
	 * @return the singleton standard dml assembler
	 */
	public static StandardDMLAssembler<StandardDbmsAdaptor> getSingletonStandardDMLAssembler(){
		return StandardDbmsAdaptor.getSingletonStandardDbmsAdaptor().getDMLAssembler();
	}



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * Instantiates a new standard dml assembler.
	 *
	 * @param dbmsAdaptor the dbms adaptor
	 */
	public StandardDMLAssembler(final A dbmsAdaptor) {
		super(dbmsAdaptor);
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////

	/**
	 * @param query
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler#assembleQuery(Query, java.lang.StringBuilder, int, int)
	 */
	@Override
	public void assembleQuery(
		final Query query,
		final StringBuilder sb,
		final int indentLevel,
		int flags
	)
	{
		final boolean singleLineMode = isSingleLine(flags);
		final String clauseSeperator = singleLineMode || isPacked(flags)?"":NEW_LINE;
		final String newLine = singleLineMode?" ":NEW_LINE;

		final DbmsConfiguration<?> config = this.getDbmsAdaptor().getConfiguration();
		flags = flags
			| bitDelimitTableIdentifiers(config.isDelimitTableIdentifiers())
			| bitDelimitColumnIdentifiers(config.isDelimitColumnIdentifiers())
			| bitDelimitAliases(config.isDelimitAliases())
		;
		this.assembleQueryName(query, sb, indentLevel, singleLineMode);
		this.assembleQueryComment(query, sb, indentLevel, singleLineMode);
		this.assembleQuerySubclassContext(query, sb, indentLevel, flags, clauseSeperator, newLine);
	}

	/**
	 * Assemble query name.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param singleLine the single line
	 * @return the string builder
	 */
	protected StringBuilder assembleQueryName(
		final Query query, final StringBuilder sb, final int indentLevel, final boolean singleLine
	)
	{
		final String name = query.getName();
		if(name == null) return sb;

		indent(sb, indentLevel, singleLine);
		sb.append(singleLine?blockCommentStart:singleLineComment);
		sb.append(name);
		sb.append(singleLine?blockCommentEnd:NEW_LINE);
		return sb;
	}

	/**
	 * Assemble query comment.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param singleLine the single line
	 * @return the string builder
	 */
	protected StringBuilder assembleQueryComment(
		final Query query, final StringBuilder sb, final int indentLevel, final boolean singleLine
	)
	{
		if(query.getComment() == null) return sb;

		indent(sb, indentLevel, singleLine);
		sb.append(blockCommentStart).append(singleLine?_:NEW_LINE);
		this.assembleCommentLines(query, sb, indentLevel, singleLine);
		sb.append(singleLine?_:NEW_LINE);
		indent(sb, indentLevel, singleLine).append(blockCommentEnd);
		sb.append(singleLine?_:NEW_LINE);
		return sb;
	}

	/**
	 * Assemble comment lines.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param singleLine the single line
	 * @return the string builder
	 */
	protected StringBuilder assembleCommentLines(
		final Query query, StringBuilder sb, final int indentLevel, final boolean singleLine
	)
	{
		final String[] comments = query.getCommentLines();
		if(comments == null){
			return sb;
		}
		if(sb == null){
			final String comment = query.getComment();
			int commentLength = 0;
			if(comment != null){
				commentLength = comment.length();
			}
			else {
				for(final String s : comments) {
					if(s == null) break;
					commentLength += s.length();
				}
			}
			sb = new StringBuilder(commentLength);
		}

		final char seperator = singleLine?' ':n;
		for(int i = 0; i < comments.length; i++) {
			if(i > 0){
				sb.append(seperator);
			}
			indent(sb, indentLevel, singleLine);
			sb.append(comments[i]);
		}
		return sb;
	}

	/**
	 * Assemble query subclass context.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @param clauseSeperator the clause seperator
	 * @param newLine the new line
	 * @return the string builder
	 * @return
	 */
	protected StringBuilder assembleQuerySubclassContext(
		final Query query,
		final StringBuilder sb,
		final int indentLevel,
		final int flags,
		final String clauseSeperator,
		final String newLine
	){
		if(query instanceof SELECT){
			return this.assembleSELECT(
				(SELECT)query, sb, indentLevel, flags, clauseSeperator, newLine
			);
		}
		else if(query instanceof INSERT){
			return this.assembleINSERT(
				(INSERT)query, sb, flags, clauseSeperator, newLine, indentLevel
			);
		}
		else if(query instanceof UPDATE){
			return this.assembleUPDATE(
				(UPDATE)query, sb, indentLevel, flags, clauseSeperator, newLine
			);
		}
		else if(query instanceof DELETE){
			return this.assembleDELETE(
				(DELETE)query, sb, flags, clauseSeperator, newLine, indentLevel
			);
		}
		else {
			throw new RuntimeException("Unhandled Query Type: "+query.getClass().getName());
		}
	}

	/**
	 * Assemble select distinct.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleSelectDISTINCT(
		final SELECT query,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		if(query.isDistinct()){
			sb.append(_).append(DISTINCT);
		}
		return sb;
	}

	/**
	 * Assemble select items.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param flags the flags
	 * @param indentLevel the indent level
	 * @param newLine the new line
	 * @return the string builder
	 */
	protected StringBuilder assembleSelectItems(
		final SELECT query,
		final StringBuilder sb,
		final int flags,
		final int indentLevel,
		final String newLine
	)
	{
		final boolean singleLine = isSingleLine(flags);
		final String indentItem = singleLine
			?""
			:indent(new StringBuilder(indentLevel), indentLevel, singleLine).append(__).toString()
		;

		final String itemSeperator = singleLine?comma_:SQL.config.list_CommaNewLine ?selectItem_Comma_NEW_LINE
									 :selectItem_NEW_LINE_Comma;

		final List<SelectItem> selectItems = query.getSelectItems();
		if(selectItems == null || selectItems.isEmpty()){
			if(query.getFromClause() != null){
				sb.append(_star);
			}

			//otherwise, add no item at all
		}
		else{
			sb.append(newLine).append(indentItem);
			for(int i = 0, size = selectItems.size(); i < size; i++) {
				if(i > 0){
					sb.append(itemSeperator).append(indentItem);
				}

				this.assembleSelectItem(selectItems.get(i), sb, indentLevel, flags);
			}
		}
		return sb;
	}

	/**
	 * @param selectItem
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler#assembleSelectItem(com.xdev.jadoth.sqlengine.internal.interfaces.SelectItem, java.lang.StringBuilder, int, int)
	 */
	@Override
	public void assembleSelectItem(
		final SelectItem selectItem, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		QueryPart.assembleObject(selectItem, this, sb, indentLevel, flags | MINIINDENT | ASEXPRESSION);
	}

	/**
	 * @param expression
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler#assembleExpression(com.xdev.jadoth.sqlengine.internal.SqlExpression, java.lang.StringBuilder, int, int)
	 */
	@Override
	public void assembleExpression(final SqlExpression expression, final StringBuilder sb, final int indentLevel, final int flags)
	{
		QueryPart.assembleObject(expression, this, sb, indentLevel, flags | MINIINDENT | ASEXPRESSION);
	}

	/**
	 * Assemble sql clause head.
	 *
	 * @param sqlClause the sql clause
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleSqlClauseHead(
		final SqlClause<?> sqlClause,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		assembleKeyword: {
			final String keyword = sqlClause.keyword();
			if(keyword == null) break assembleKeyword;

			indent(sb, indentLevel, isSingleLine(flags));
			sb.append(keyword);
			sb.append(isSingleLine(flags) ?_ :sqlClause.getKeyWordSeperator());
		}

		return sb;
	}

	/**
	 * Assemble sql clause body.
	 *
	 * @param sqlClause the sql clause
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleSqlClauseBody(
		final SqlClause<?> sqlClause,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		final boolean singleLine = isSingleLine(flags);
		final String bodySep = sqlClause.getBodyElementSeperator(singleLine);
		final String indent = !singleLine && sqlClause.isIndentationAllowed()
			?QueryPart.indent(new StringBuilder(indentLevel), indentLevel, singleLine).toString()
			:""
		;
		if(sqlClause.isIndentFirstBodyElement()){
			sb.append(indent);
		}
		boolean notEmpty = false;
		for (final Object o : sqlClause) {
			if(notEmpty){
				sb.append(bodySep).append(indent);
			}
			else {
				notEmpty = true;
			}
			if(o instanceof SqlExpression) {
				this.assembleExpression((SqlExpression)o, sb, indentLevel, flags);
			}
			else {
				QueryPart.assembleObject(o, this, sb, indentLevel, flags | MINIINDENT | ASEXPRESSION);
			}
		}
		return sb;
	}

	/**
	 * Assemble parenthesized list clause body.
	 *
	 * @param sqlClause the sql clause
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleParenthesizedListClauseBody(
		final SqlClause<?> sqlClause,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		indent(sb, indentLevel, isSingleLine(flags));
		sb.append(par);
		this.assembleSqlClauseBody(sqlClause, sb, indentLevel, flags);
		sb.append(rap);
		return sb;
	}

	/**
	 * Assemble sql clause.
	 *
	 * @param parentQuery the parent query
	 * @param sqlClause the sql clause
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	public StringBuilder assembleSqlClause(
//		final TableQuery parentQuery,
		final SqlClause<?> sqlClause,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		if(sqlClause == null) return sb;

		/* (12.11.2009 TM)NOTE:
		 * Bad instanceof switch: assembleSqlClause() is supposed to avoid boiler plate code in calling context.
		 * But assemble() can't be moved back do SqlClause class itself because it's QueryAssembler-dependant.
		 */
		if(sqlClause instanceof FROM){
			return this.assembleFROM(null, (FROM)sqlClause, sb, indentLevel, flags);
		}
		else if(sqlClause instanceof WHERE){
			return this.assembleWHERE(null,  (WHERE)sqlClause, sb, indentLevel, flags);
		}
		else if(sqlClause instanceof ORDER_BY){
			return this.assembleORDERBY(null,  (ORDER_BY)sqlClause, sb, indentLevel, flags);
		}
		else if(sqlClause instanceof GROUP_BY){
			return this.assembleGROUPBY(null,  (GROUP_BY)sqlClause, sb, indentLevel, flags);
		}
		else if(sqlClause instanceof HAVING){
			return this.assembleHAVING(null,  (HAVING)sqlClause, sb, indentLevel, flags);
		}
		else if(sqlClause instanceof JoinClause){
			return this.assembleJOIN(null,  (JoinClause)sqlClause, sb, indentLevel, flags);
		}
		else {
			throw new SQLEngineAssembleException("Unhandled SqlClause: "+sqlClause);
		}
	}

	/**
	 * Assemble from.
	 *
	 * @param parentQuery the parent query
	 * @param from the from
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleFROM(
		final TableQuery parentQuery,
		final FROM from,
		StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		sb = this.assembleSqlClauseHead(from, sb, indentLevel, flags);

		final String clauseSeperator = isSingleLine(flags) || isPacked(flags)?"":NEW_LINE;
		final String lineSeperator = isSingleLine(flags)?" ":NEW_LINE;

		final int passFlags = flags | ASEXPRESSION;
		this.assembleTableExpression(from.getTable(), sb, indentLevel, passFlags);

		final List<JoinClause> joins = from.getJoins();
		if(joins != null){
			//joins is known to be an ArrayList
			for(int i = 0, size = joins.size(); i < size; i++){
				sb.append(lineSeperator).append(clauseSeperator);
				this.assembleJOIN(parentQuery, joins.get(i), sb, indentLevel, passFlags);
			}
		}

		return sb;
	}

	protected StringBuilder assembleJOIN(
		final TableQuery parentQuery,
		final JoinClause join,
		StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		sb = this.assembleSqlClauseHead(join, sb, indentLevel, flags);

		final String lineSeperator = isSingleLine(flags)?" ":NEW_LINE;

		final int passFlags = flags | ASEXPRESSION;
		this.assembleTableExpression(join.getTable(), sb, indentLevel, passFlags);
		sb.append(_ON_);
		QueryPart.assembleObject(join.getJoinCondition(), this, sb, indentLevel, passFlags);

		for(final SqlCondition sqlCondition : join) {
			sb.append(lineSeperator);
			QueryPart.assembleObject(sqlCondition, this, sb, indentLevel, passFlags);
		}

		return sb;
	}

	/**
	 * Assemble where.
	 *
	 * @param parentQuery the parent query
	 * @param where the where
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleWHERE(
		final TableQuery parentQuery,
		final WHERE where,
		StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		sb = this.assembleSqlClauseHead(where, sb, indentLevel, flags);
		sb = this.assembleSqlClauseBody(where, sb, indentLevel, flags);
		return sb;
	}

	/**
	 * Assemble groupby.
	 *
	 * @param parentQuery the parent query
	 * @param groupBy the group by
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleGROUPBY(
		final TableQuery parentQuery,
		final GROUP_BY groupBy,
		StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		sb = this.assembleSqlClauseHead(groupBy, sb, indentLevel, flags);
		sb = this.assembleSqlClauseBody(groupBy, sb, indentLevel, flags);
		return sb;
	}

	/**
	 * Assemble having.
	 *
	 * @param parentQuery the parent query
	 * @param having the having
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleHAVING(
		final TableQuery parentQuery,
		final HAVING having,
		StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		sb = this.assembleSqlClauseHead(having, sb, indentLevel, flags);
		sb = this.assembleSqlClauseBody(having, sb, indentLevel, flags|ASEXPRESSION);
		return sb;
	}

	/**
	 * Assemble orderby.
	 *
	 * @param parentQuery the parent query
	 * @param orderby the orderby
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleORDERBY(
		final TableQuery parentQuery,
		final ORDER_BY orderby,
		StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		sb = this.assembleSqlClauseHead(orderby, sb, indentLevel, flags);
		sb = this.assembleSqlClauseBody(orderby, sb, indentLevel, flags);
		return sb;
	}

	/**
	 * Assemble select row limit.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param flags the flags
	 * @param clauseSeperator the clause seperator
	 * @param newLine the new line
	 * @param indentLevel the indent level
	 * @return the string builder
	 */
	protected StringBuilder assembleSelectRowLimit(
		final SELECT query,
		final StringBuilder sb,
		final int flags,
		final String clauseSeperator,
		final String newLine,
		final int indentLevel
	)
	{

		final Integer skip = query.getOffsetSkipCount();
		final Integer range = query.getFetchFirstRowCount();
		if(skip != null){
			sb.append(newLine).append(clauseSeperator).append(OFFSET_).append(skip).append(skip==1?_ROW:_ROWS);
		}
		if(range != null){
			sb.append(newLine);
			if(skip != null){
				sb.append(clauseSeperator);
			}
			sb.append(FETCH_FIRST_).append(range).append(range==1?_ROW_ONLY:_ROWS_ONLY);
		}
		return sb;
	}

	/**
	 * Assemble set.
	 *
	 * @param parentQuery the parent query
	 * @param set the set
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleSET(
		final TableQuery parentQuery,
		final SET set,
		StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		sb = this.assembleSqlClauseHead(set, sb, indentLevel, flags);
		sb = this.assembleSqlClauseBody(set, sb, indentLevel, flags);
		return sb;
	}

	/**
	 * Assemble assignment columns clause.
	 *
	 * @param parentQuery the parent query
	 * @param columns the columns
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleAssignmentColumnsClause(
		final TableQuery parentQuery,
		final AssignmentColumnsClause columns,
		StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		sb = this.assembleSqlClauseHead(columns, sb, indentLevel, flags);
		sb = this.assembleParenthesizedListClauseBody(columns, sb, indentLevel, flags);
		return sb;
	}

	/**
	 * Assemble assignment values clause.
	 *
	 * @param parentQuery the parent query
	 * @param values the values
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleAssignmentValuesClause(
		final TableQuery parentQuery,
		final AssignmentValuesClause values,
		StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		sb = this.assembleSqlClauseHead(values, sb, indentLevel, flags);
		sb = this.assembleParenthesizedListClauseBody(values, sb, indentLevel, flags|ASEXPRESSION);
		return sb;
	}



	/**
	 * Assemble select.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @param clauseSeperator the clause seperator
	 * @param newLine the new line
	 * @return the string builder
	 * @return
	 */
	protected StringBuilder assembleSELECT(
		final SELECT query,
		final StringBuilder sb,
		final int indentLevel,
		final int flags,
		final String clauseSeperator,
		final String newLine
	)
	{
		indent(sb, indentLevel, isSingleLine(flags));
		sb.append(query.keyword());

		this.assembleSelectDISTINCT(query, sb, indentLevel, flags);
		this.assembleSelectItems(query, sb, flags, indentLevel, newLine);
		this.assembleSelectSqlClauses(query, sb, indentLevel, flags|ASEXPRESSION, clauseSeperator, newLine);
		this.assembleSelectRowLimit(query, sb, flags, clauseSeperator, newLine, indentLevel);
		this.assembleAppendSELECTs(query, sb, indentLevel, flags, clauseSeperator, newLine);
		return sb;
	}

	/**
	 * Assemble append selec ts.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @param clauseSeperator the clause seperator
	 * @param newLine the new line
	 * @return the string builder
	 */
	protected StringBuilder assembleAppendSELECTs(
		final SELECT query,
		final StringBuilder sb,
		final int indentLevel,
		final int flags,
		final String clauseSeperator,
		final String newLine
	)
	{
		SELECT appendSelect = query.getUnionSelect();
		if(appendSelect != null) {
			this.assembleAppendSelect(appendSelect, sb, indentLevel, flags, clauseSeperator, newLine, UNION);
			return sb;
		}

		appendSelect = query.getUnionAllSelect();
		if(appendSelect != null) {
			this.assembleAppendSelect(appendSelect, sb, indentLevel, flags, clauseSeperator, newLine, UNION_ALL);
			return sb;
		}

		appendSelect = query.getIntersectSelect();
		if(appendSelect != null) {
			this.assembleAppendSelect(appendSelect, sb, indentLevel, flags, clauseSeperator, newLine, INTERSECT);
			return sb;
		}

		appendSelect = query.getExceptSelect();
		if(appendSelect != null) {
			this.assembleAppendSelect(appendSelect, sb, indentLevel, flags, clauseSeperator, newLine, EXCEPT);
			return sb;
		}
		return sb;
	}



	/**
	 * Assemble append select.
	 *
	 * @param appendSelect the append select
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @param clauseSeperator the clause seperator
	 * @param newLine the new line
	 * @param appendKeyword the append keyword
	 * @return the string builder
	 */
	protected StringBuilder assembleAppendSelect(
		final SELECT appendSelect,
		final StringBuilder sb,
		final int indentLevel,
		final int flags,
		final String clauseSeperator,
		final String newLine,
		final String appendKeyword
	)
	{
		sb.append(clauseSeperator).append(newLine).append(appendKeyword).append(newLine).append(clauseSeperator);
		QueryPart.assembleObject(appendSelect, this, sb, indentLevel, flags &~MINIINDENT &~ASEXPRESSION);
		return sb;
	}

	public StringBuilder assembleObject(final Object object, final StringBuilder sb, final int indentLevel, final int flags)
	{
		if(object == null) {
			sb.append((String)null);
		}
		else if(object instanceof AssembableSqlExpression){
			((AssembableSqlExpression)object).assemble(this, sb, indentLevel, flags);
		}
		else {
			this.assembleStringValue(object.toString(), sb, indentLevel, flags);
		}
		return sb;
	}


	/**
	 * Assemble select sql clauses.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @param clauseSeperator the clause seperator
	 * @param newLine the new line
	 * @return the string builder
	 */
	protected StringBuilder assembleSelectSqlClauses(
		final SELECT query,
		final StringBuilder sb,
		final int indentLevel,
		final int flags,
		final String clauseSeperator,
		final String newLine
	)
	{
		for(final SqlClause<?> sqlClause : query.getSqlClauses()) {
			if(sqlClause == null) continue;
			sb.append(newLine).append(clauseSeperator);
			this.assembleSqlClause(sqlClause, sb, indentLevel, flags);
		}
		return sb;
	}


	/**
	 * Assemble insert.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param flags the flags
	 * @param clauseSeperator the clause seperator
	 * @param newLine the new line
	 * @param indentLevel the indent level
	 * @return the string builder
	 * @return
	 */
	protected StringBuilder assembleINSERT(
		final INSERT query,
		final StringBuilder sb,
		final int flags,
		final String clauseSeperator,
		final String newLine,
		final int indentLevel
	)
	{
		indent(sb, indentLevel, isSingleLine(flags)).append(query.keyword()).append(_INTO_);

		this.assembleTableIdentifier(query.getTable(), sb, indentLevel, flags);
		sb.append(newLine);

		this.assembleAssignmentColumnsClause(query, query.getColumnsClause(), sb, indentLevel, flags|UNQUALIFIED);
		sb.append(newLine);


		final SELECT valueSelect = query.filterSelect();
		if(valueSelect != null){
			sb.append(clauseSeperator);
			QueryPart.assembleObject(valueSelect, this, sb, indentLevel, flags);
		}
		else{
			final AssignmentValuesClause values = query.getValuesClause();
			if(values != null) {
				this.assembleAssignmentValuesClause(query, values, sb, indentLevel, flags);
			}
			else{
				indent(sb, indentLevel, isSingleLine(flags)).append(DEFAULT_VALUES);
			}
		}


		return sb;
	}

	/**
	 * Assemble update.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @param clauseSeperator the clause seperator
	 * @param newLine the new line
	 * @return the string builder
	 * @return
	 */
	protected StringBuilder assembleUPDATE(
		final UPDATE query,
		final StringBuilder sb,
		final int indentLevel,
		final int flags,
		final String clauseSeperator,
		final String newLine
	)
	{
		indent(sb, indentLevel, isSingleLine(flags));
		sb.append(query.keyword());

		sb.append(_);
		this.assembleTableIdentifier(query.getTable(), sb, indentLevel, flags);


		final SET set = query.getSetClause();
		sb.append(newLine);
		this.assembleSET(query, set, sb, indentLevel, flags);

		final FROM from = query.getFromClause();
		if(from != null) {
			sb.append(newLine).append(clauseSeperator);
			this.assembleFROM(query, from, sb, indentLevel, flags);
		}

		final WHERE where = query.getWhereClause();
		if(where != null){
			sb.append(newLine).append(clauseSeperator);
			this.assembleWHERE(query, where, sb, indentLevel, flags);
		}

		return sb;
	}

	/**
	 * Assemble delete.
	 *
	 * @param query the query
	 * @param sb the sb
	 * @param flags the flags
	 * @param clauseSeperator the clause seperator
	 * @param newLine the new line
	 * @param indentLevel the indent level
	 * @return the string builder
	 * @return
	 */
	protected StringBuilder assembleDELETE(
		final DELETE query,
		final StringBuilder sb,
		final int flags,
		final String clauseSeperator,
		final String newLine,
		final int indentLevel
	)
	{
		//the first FROM is mandatory
		indent(sb, indentLevel, isSingleLine(flags)).append(query.keyword()).append(_FROM_);
		this.assembleTableIdentifier(query.getTable(), sb, indentLevel, flags);

		final FROM from = query.getFromClause();
		if(from != null) {
			sb.append(newLine).append(clauseSeperator);
			this.assembleFROM(query, from, sb, indentLevel, flags);
		}

		final WHERE where = query.getWhereClause();
		if(where != null){
			sb.append(newLine).append(clauseSeperator);
			this.assembleWHERE(query, where, sb, indentLevel, flags);
		}
		return sb;
	}



	/**
	 * Assemble double quoted expression.
	 *
	 * @param expression the expression
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler#assembleDoubleQuotedExpression(com.xdev.jadoth.sqlengine.internal.DoubleQuotedExpression, java.lang.StringBuilder, int, int)
	 */
	@Override
	public StringBuilder assembleDoubleQuotedExpression(
		final DoubleQuotedExpression expression, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		final Object expressionObject = expression.getExpressionObject();
		if(expressionObject == null){
			return sb.append((String)null);
		}

		sb.append(qt);
		QueryPart.assembleObject(expressionObject, this, sb, indentLevel, flags | ESCAPE_DBL_QUOTES);
		sb.append(qt);
		return sb;
	}


	/**
	 * Assemble quoted expression.
	 *
	 * @param expression the expression
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler#assembleQuotedExpression(com.xdev.jadoth.sqlengine.internal.QuotedExpression, java.lang.StringBuilder, int, int)
	 */
	@Override
	public StringBuilder assembleQuotedExpression(
		final QuotedExpression expression, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		final Object expressionObject = expression.getExpressionObject();
		if(expressionObject == null){
			return sb.append((String)null);
		}

		sb.append(apo);
		QueryPart.assembleObject(expressionObject, this, sb, indentLevel, flags | ESCAPE_QUOTES);
		sb.append(apo);
		return sb;
	}


	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * @return
	 */
	@Override
	public DateFormat getDateFormatDATE()
	{
		return dateToDATE;
	}
	/**
	 * @return
	 */
	@Override
	public DateFormat getDateFormatTIME()
	{
		return dateToTIME;
	}
	/**
	 * @return
	 */
	@Override
	public DateFormat getDateFormatTIMESTAMP()
	{
		return dateToTIMESTAMP;
	}

	@Override
	public StringBuilder assembleDateTimeExpression(final SqlTimestamp dateTimeExpression, final StringBuilder sb)
	{
		sb.append(
			 dateTimeExpression instanceof SqlDate ?SQL.LANG.DATE
			:dateTimeExpression instanceof SqlTime ?SQL.LANG.TIME
			:SQL.LANG.TIMESTAMP
		)
		.append(_).append(apo)
		.append(dateTimeExpression.getDateFormat(this).format(dateTimeExpression.getDate()))
		.append(apo)
		;
		return sb;
	}



	/**
	 * @param column
	 * @param sb
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler#assembleColumnQualifier(com.xdev.jadoth.sqlengine.internal.SqlColumn, java.lang.StringBuilder, int)
	 */
	@Override
	public StringBuilder assembleColumnQualifier(final SqlColumn column, final StringBuilder sb, final int flags)
	{
		final TableExpression owner = column.getOwner();
		String qualifier = getAlias(owner);
		if(qualifier == null || QueryPart.isQualifyByTable(flags)){
			qualifier = owner.toString();
		}
		return sb.append(qualifier).append(dot);
	}

	/**
	 * @param column
	 * @param sb
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler#assembleColumn(com.xdev.jadoth.sqlengine.internal.SqlColumn, java.lang.StringBuilder, int)
	 */
	@Override
	public StringBuilder assembleColumn(
		final SqlColumn column, final StringBuilder sb, final int indentLevel, int flags
	)
	{
		final TableExpression owner = column.getOwner();

		final DbmsAdaptor<?> dbms = this.getDbmsAdaptor();
		final boolean delimColumn = dbms.getConfiguration().isDelimitColumnIdentifiers()
			|| QueryPart.isDelimitColumnIdentifiers(flags)
		;
		final char delim = dbms.getIdentifierDelimiter();

		flags |= QueryPart.bitDelimitColumnIdentifiers(
			this.getDbmsAdaptor().getConfiguration().isDelimitColumnIdentifiers()
		);

		if(delimColumn){
			sb.append(delim);
		}
		if(owner != null && !QueryPart.isUnqualified(flags)) {
			this.assembleColumnQualifier(column, sb, flags);
		}
		QueryPart.assembleObject(column.getExpressionObject(), this, sb, indentLevel, flags);
		if(delimColumn){
			sb.append(delim);
		}
		return sb;
	}


	/**
	 * @param identifier
	 * @param sb
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler#assembleDelimitedIdentifier(java.lang.String, java.lang.StringBuilder, int)
	 */
	@Override
	public StringBuilder assembleDelimitedIdentifier(final String identifier, final StringBuilder sb, final int flags)
	{
		return sb.append(IDENTIFIER_DELIMITER).append(identifier).append(IDENTIFIER_DELIMITER);
	}


	/**
	 * @param object
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler#assembleObject(java.lang.Object, java.lang.StringBuilder, int, int)
	 */
	@Override
	public StringBuilder assembleStringValue(final String s, final StringBuilder sb, final int indentLevel, final int flags)
	{
		final DbmsAdaptor<?> dbms = this.getDbmsAdaptor();
		if(dbms.getConfiguration().isAutoEscapeReservedWords() && dbms.getSyntax().isReservedWord(s)){
			final char d = dbms.getIdentifierDelimiter();
			sb.append(d).append(s).append(d);
			return sb;
		}

		final int length = s.length();
		final char[] chars = new char[length];
		s.getChars(0, length, chars, 0);
		sb.ensureCapacity(sb.length() + length + 10);

		if (QueryPart.isEscapeQuotes(flags)) {
			for(final char c : chars){
				if(c == apo) {
					sb.append(apo);
				}
				sb.append(c);
			}
		}
		else if (QueryPart.isEscapeDoubleQuotes(flags)) {
			for(final char c : chars){
				if(c == qt) {
					sb.append(qt);
				}
				sb.append(c);
			}
		}
		else {
			sb.append(s);
		}
		return sb;
	}




	/**
	 * @param tableExpression
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler#assembleTableExpression(com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression, java.lang.StringBuilder, int, int)
	 */
	@Override
	public StringBuilder assembleTableExpression(
		final TableExpression tableExpression, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		if(tableExpression instanceof SqlTableIdentity){
			return this.assembleTableIdentifier((SqlTableIdentity)tableExpression, sb, indentLevel, flags);
		}
		QueryPart.assembleObject(tableExpression, this, sb, indentLevel, flags);
		if(!isOmitAlias(flags)){
			final String alias = TableExpression.Utils.getAlias(tableExpression);
			if(alias != null) {
				sb.append(_);
				sb.append(alias);
			}
		}
		return sb;
	}

	/**
	 * @param table
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler#assembleTableIdentifier(com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity, java.lang.StringBuilder, int, int)
	 */
	@Override
	public StringBuilder assembleTableIdentifier(
		final SqlTableIdentity table, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		final DbmsAdaptor<?> dbms = this.getDbmsAdaptor();
		final DbmsSyntax<?> syntax = dbms.getSyntax();
		final DbmsConfiguration<?> config = dbms.getConfiguration();
		final char delim = dbms.getIdentifierDelimiter();

		final SqlTableIdentity.Sql sql = table.sql();
		final String schema = sql.schema;
		final String name = sql.name;

		final boolean delimTable = QueryPart.isDelimitTableIdentifiers(flags)
			|| config.isDelimitTableIdentifiers()
			|| config.isAutoEscapeReservedWords()
			&&
			(syntax.isReservedWord(name) || schema != null && syntax.isReservedWord(schema))
		;

		if(delimTable){
			sb.append(delim);
		}
		if(schema != null){
			sb.append(schema).append(dot);
		}
		sb.append(name);
		if(delimTable){
			sb.append(delim);
		}

		if(!isOmitAlias(flags)){
			final String alias = sql.alias;
			if(alias != null && alias.length() > 0){
				sb.append(_);
				if(config.isDelimitAliases() || config.isAutoEscapeReservedWords() && syntax.isReservedWord(alias)){
					sb.append(delim).append(alias).append(delim);
				}
				else {
					sb.append(alias);
				}
			}
		}
		return sb;
	}

}
