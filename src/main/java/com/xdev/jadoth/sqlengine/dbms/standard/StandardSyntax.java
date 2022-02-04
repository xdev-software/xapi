/**
 * 
 */
package com.xdev.jadoth.sqlengine.dbms.standard;

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

import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsSyntax;

/**
 * @author Thomas Muenz
 *
 */
public class StandardSyntax<A extends DbmsAdaptor<A>> extends DbmsSyntax.Implementation<A>
{	
	protected StandardSyntax()
	{
		super(
			wordSet(
				"ABS","ALL","ALLOCATE","ALTER","AND","ANY","ARE","ARRAY","AS","ASENSITIVE","ASYMMETRIC","AT","ATOMIC","AUTHORIZATION","AVG","BEGIN","BETWEEN","BIGINT","BINARY","BLOB","BOOLEAN","BOTH","BY","CALL","CALLED","CARDINALITY","CASCADED","CASE","CAST","CEIL","CEILING","CHAR","CHAR_LENGTH","CHARACTER","CHARACTER_LENGTH","CHECK","CLOB","CLOSE","COALESCE","COLLATE","COLLECT","COLUMN","COMMIT","CONDITION","CONNECT","CONSTRAINT","CONVERT","CORR","CORRESPONDING","COUNT","COVAR_POP","COVAR_SAMP","CREATE","CROSS","CUBE","CUME_DIST","CURRENT","CURRENT_CATALOG","CURRENT_DATE","CURRENT_DEFAULT_TRANSFORM_GROUP","CURRENT_PATH","CURRENT_ROLE","CURRENT_SCHEMA","CURRENT_TIME","CURRENT_TIMESTAMP","CURRENT_TRANSFORM_GROUP_FOR_TYPE","CURRENT_USER","CURSOR","CYCLE","DATE","DAY","DEALLOCATE","DEC","DECIMAL","DECLARE","DEFAULT","DELETE","DENSE_RANK","DEREF","DESCRIBE","DETERMINISTIC","DISCONNECT","DISTINCT","DOUBLE","DROP","DYNAMIC","EACH","ELEMENT","ELSE","END","END-EXEC","ESCAPE","EVERY","EXCEPT","EXEC","EXECUTE","EXISTS","EXP","EXTERNAL","EXTRACT","FALSE","FETCH","FILTER","FLOAT","FLOOR","FOR","FOREIGN","FREE","FROM","FULL","FUNCTION","FUSION","GET","GLOBAL","GRANT","GROUP","GROUPING","HAVING","HOLD","HOUR","IDENTITY","IN","INDICATOR","INNER","INOUT","INSENSITIVE","INSERT","INT","INTEGER","INTERSECT","INTERSECTION","INTERVAL","INTO","IS","JOIN","LANGUAGE","LARGE","LATERAL","LEADING","LEFT","LIKE","LIKE_REGEX","LN","LOCAL","LOCALTIME","LOCALTIMESTAMP","LOWER","MATCH","MAX","MEMBER","MERGE","METHOD","MIN","MINUTE","MOD","MODIFIES","MODULE","MONTH","MULTISET","NATIONAL","NATURAL","NCHAR","NCLOB","NEW","NO","NONE","NORMALIZE","NOT","NULL","NULLIF","NUMERIC","OCTET_LENGTH","OCCURRENCES_REGEX","OF","OLD","ON","ONLY","OPEN","OR","ORDER","OUT","OUTER","OVER","OVERLAPS","OVERLAY","PARAMETER","PARTITION","PERCENT_RANK","PERCENTILE_CONT","PERCENTILE_DISC","POSITION","POSITION_REGEX","POWER","PRECISION","PREPARE","PRIMARY","PROCEDURE","RANGE","RANK","READS","REAL","RECURSIVE","REF","REFERENCES","REFERENCING","REGR_AVGX","REGR_AVGY","REGR_COUNT","REGR_INTERCEPT","REGR_R2","REGR_SLOPE","REGR_SXX","REGR_SXY","REGR_SYY","RELEASE","RESULT","RETURN","RETURNS","REVOKE","RIGHT","ROLLBACK","ROLLUP","ROW","ROW_NUMBER","ROWS","SAVEPOINT","SCOPE","SCROLL","SEARCH","SECOND","SELECT","SENSITIVE","SESSION_USER","SET","SIMILAR","SMALLINT","SOME","SPECIFIC","SPECIFICTYPE","SQL","SQLEXCEPTION","SQLSTATE","SQLWARNING","SQRT","START","STATIC","STDDEV_POP","STDDEV_SAMP","SUBMULTISET","SUBSTRING","SUBSTRING_REGEX","SUM","SYMMETRIC","SYSTEM","SYSTEM_USER","TABLE","TABLESAMPLE","THEN","TIME","TIMESTAMP","TIMEZONE_HOUR","TIMEZONE_MINUTE","TO","TRAILING","TRANSLATE","TRANSLATE_REGEX","TRANSLATION","TREAT","TRIGGER","TRIM","TRUE","UESCAPE","UNION","UNIQUE","UNKNOWN","UNNEST","UPDATE","UPPER","USER","USING","VALUE","VALUES","VAR_POP","VAR_SAMP","VARBINARY","VARCHAR","VARYING","WHEN","WHENEVER","WHERE","WIDTH_BUCKET","WINDOW","WITH","WITHIN","WITHOUT","YEAR"
			),
			wordSet(
				"A","ABSOLUTE","ACTION","ADA","ADD","ADMIN","AFTER","ALWAYS","ASC","ASSERTION","ASSIGNMENT","ATTRIBUTE","ATTRIBUTES","BEFORE","BERNOULLI","BREADTH","C","CASCADE","CATALOG","CATALOG_NAME","CHAIN","CHARACTER_SET_CATALOG","CHARACTER_SET_NAME","CHARACTER_SET_SCHEMA","CHARACTERISTICS","CHARACTERS","CLASS_ORIGIN","COBOL","COLLATION","COLLATION_CATALOG","COLLATION_NAME","COLLATION_SCHEMA","COLUMN_NAME","COMMAND_FUNCTION","COMMAND_FUNCTION_CODE","COMMITTED","CONDITION_NUMBER","CONNECTION","CONNECTION_NAME","CONSTRAINT_CATALOG","CONSTRAINT_NAME","CONSTRAINT_SCHEMA","CONSTRAINTS","CONSTRUCTOR","CONTAINS","CONTINUE","CURSOR_NAME","DATA","DATETIME_INTERVAL_CODE","DATETIME_INTERVAL_PRECISION","DEFAULTS","DEFERRABLE","DEFERRED","DEFINED","DEFINER","DEGREE","DEPTH","DERIVED","DESC","DESCRIPTOR","DIAGNOSTICS","DISPATCH","DOMAIN","DYNAMIC_FUNCTION","DYNAMIC_FUNCTION_CODE","EQUALS","EXCLUDE","EXCLUDING","FINAL","FIRST","FLAG","FOLLOWING","FORTRAN","FOUND","G","GENERAL","GENERATED","GO","GOTO","GRANTED","HIERARCHY","IMMEDIATE","IMPLEMENTATION","INCLUDING","INCREMENT","INITIALLY","INPUT","INSTANCE","INSTANTIABLE","INVOKER","ISOLATION","K","KEY","KEY_MEMBER","KEY_TYPE","LAST","LENGTH","LEVEL","LOCATOR","M","MAP","MATCHED","MAXVALUE","MESSAGE_LENGTH","MESSAGE_OCTET_LENGTH","MESSAGE_TEXT","MINVALUE","MORE","MUMPS","NAME","NAMES","NESTING","NEXT","NFC","NFD","NFKC","NFKD","NORMALIZED","NULLABLE","NULLS","NUMBER","OBJECT","OCTETS","OPTION","OPTIONS","ORDERING","ORDINALITY","OTHERS","OUTPUT","OVERRIDING","P","PAD","PARAMETER_MODE","PARAMETER_NAME","PARAMETER_ORDINAL_POSITION","PARAMETER_SPECIFIC_CATALOG","PARAMETER_SPECIFIC_NAME","PARAMETER_SPECIFIC_SCHEMA","PARTIAL","PASCAL","PATH","PLACING","PLI","PRECEDING","PRESERVE","PRIOR","PRIVILEGES","PUBLIC","READ","RELATIVE","REPEATABLE","RESTART","RESTRICT","RETURNED_CARDINALITY","RETURNED_LENGTH","RETURNED_OCTET_LENGTH","RETURNED_SQLSTATE","ROLE","ROUTINE","ROUTINE_CATALOG","ROUTINE_NAME","ROUTINE_SCHEMA","ROW_COUNT","SCALE","SCHEMA","SCHEMA_NAME","SCOPE_CATALOG","SCOPE_NAME","SCOPE_SCHEMA"
			)
		);
	}
	
}
