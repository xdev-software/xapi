
package com.xdev.jadoth.sqlengine.util;

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




/**
 * The Class SqlHTML.
 * 
 * @author Thomas Muenz
 */
public class SqlHTML
{
	
	/**
	 * Generate style.
	 * 
	 * @return the string
	 */
	private static String generateStyle() {
		return
		"\t.SELECT { color:#FF0000; font-weight:bold; }\n"+
		"\t.TOP { color:#804040; font-weight:bold; }\n"+
		"\t.DISTINCT { color:#804040; font-weight:bold; }\n"+
		"\t.SUM { color:#808080; font-weight:bold; }\n"+
		"\t.COUNT { color:#808080; font-weight:bold; }\n"+
		"\t.FROM { color:#A0A000; font-weight:bold; }\n"+
		"\t.WHERE { color:#0000FF; font-weight:bold; }\n"+
		"\t.INNER_JOIN { color:#00A000; font-weight:bold; }\n"+
		"\t.LEFT_JOIN { color:#60A000; font-weight:bold; }\n"+
		"\t.RIGHT_JOIN { color:#00A060; font-weight:bold; }\n"+
		"\t.GROUP_BY { color:#E000E0; font-weight:bold; }\n"+
		"\t.HAVING { color:#D07000; font-weight:bold; }\n"+
		"\t.ORDER_BY { color:#8000FF; font-weight:bold; }\n"+
		"\t.AND { color:#000000; font-weight:bold; }\n"+
		"\t.OR { color:#000000; font-weight:bold; }\n"+
		"\t.ON { color:#00A000; font-weight:bold; }\n"+
		"\t.structure { color:#000000; font-weight:bold; }\n"+
		"\t.area { font-family:'Courier New'; font-size:16px; \n"+
		         "\t\tborder-color:#000000; border-width:1px; border-style:solid; \n"+
		         "\t\tbackground-color:#D0D8FF;\n"+
		         "\t\tpadding:5px;\n"+
		"\t}\n";
	}

	/**
	 * Generate query html begin.
	 * 
	 * @param title the title
	 * @return the string
	 */
	private static String generateQueryHTMLBegin(final String title){
		return
			"<html>\n"+
			"<head>\n"+
			"<title>"+title+"</title>\n"+
			"<style type=\"text/css\">\n"+
			generateStyle()+
			"</style>\n"+
			"</head>\n"+
			"<body>\n"+
			"<pre class=\"area\"class=\"area\">\n";
	}

	/**
	 * Generate query html end.
	 * 
	 * @return the string
	 */
	private static String generateQueryHTMLEnd(){
		return "</pre>\n</body>\n</html>\n";
	}


	/**
	 * Adds the html tags.
	 * 
	 * @param s the s
	 * @return the string
	 */
	private static String addHTMLTags(final String s){
		return "<span class=\""+s.replaceAll(" ", "_")+"\">"+s+"</span>";
	}

	/**
	 * Adds the html tags.
	 * 
	 * @param s the s
	 * @param cssClassName the css class name
	 * @return the string
	 */
	private static String addHTMLTags(final String s, final String cssClassName){
		return "<span class=\""+cssClassName+"\">"+s+"</span>";
	}

	/**
	 * Query to html.
	 * 
	 * @param q the q
	 * @return the string
	 */
	private static String queryToHTML(final String q){
		return q
		.replaceAll("SELECT "	 ,	   addHTMLTags("SELECT")+" ")
		.replaceAll(" TOP "		 ," " +addHTMLTags("TOP")+" ")
		.replaceAll(" DISTINCT"	 ," " +addHTMLTags("DISTINCT"))
		.replaceAll("SUM\\("	 ,	   addHTMLTags("SUM")+"(")
		.replaceAll("COUNT\\("	 ,	   addHTMLTags("COUNT")+"(")
		.replaceAll("FROM "		 ,	   addHTMLTags("FROM")+" ")
		.replaceAll("WHERE "	 ,	   addHTMLTags("WHERE")+" ")
		.replaceAll("INNER JOIN ",	   addHTMLTags("INNER JOIN")+" ")
		.replaceAll("LEFT JOIN " ,	   addHTMLTags("LEFT JOIN")+" ")
		.replaceAll("RIGHT JOIN ",	   addHTMLTags("RIGHT JOIN")+" ")
		.replaceAll("GROUP BY "	 ,	   addHTMLTags("GROUP BY")+" ")
		.replaceAll("HAVING "	 ,	   addHTMLTags("HAVING")+" ")
		.replaceAll("ORDER BY "  ,	   addHTMLTags("ORDER BY")+" ")
		.replaceAll("  AND "	 ,"  "+addHTMLTags("AND")+" ")
		.replaceAll("  OR "		 ,"  "+addHTMLTags("OR")+" ")
		.replaceAll(" ON "		 ," " +addHTMLTags("ON")+" ")
		.replaceAll(" ASC "		 ," " +addHTMLTags("ASC", "ORDER_BY")+" ")
		.replaceAll(" DESC "	 ," " +addHTMLTags("DESC","ORDER_BY")+" ")
		.replaceAll("\\("		 ,	   addHTMLTags("(", "structure"))
		.replaceAll("\\)"		 ,	   addHTMLTags(")", "structure"))
		.replaceAll("\\,"		 ,	   addHTMLTags(",", "structure"))
		;
	}


	/**
	 * Generate query html document.
	 * 
	 * @param queryString the query string
	 * @param title the title
	 * @return the string
	 */
	public static String generateQueryHTMLDocument(final String queryString, final String title){
		return generateQueryHTMLBegin(title) + queryToHTML(queryString) + generateQueryHTMLEnd();
	}
}
