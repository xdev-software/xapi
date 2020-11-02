package xdev.util;

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


import java.awt.Font;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import xdev.io.IOUtils;


/**
 * <p>
 * The <code>HTMLUtils</code> class provides utility methods for HTML handling.
 * </p>
 * 
 * @since 2.0
 * 
 * @author XDEV Software
 */
public final class HTMLUtils
{
	private HTMLUtils()
	{
	}
	
	private final static Map<Integer, String>	htmlSigns;
	static
	{
		htmlSigns = new HashMap();
		htmlSigns.put(0x22,"&quot;"); // "
		htmlSigns.put(0x26,"&amp;"); // &
		htmlSigns.put(0x3c,"&lt;"); // <
		htmlSigns.put(0x3e,"&gt;"); // >
		htmlSigns.put(0xa0,"&nbsp;"); //  
		htmlSigns.put(0xa1,"&iexcl;"); // ¡
		htmlSigns.put(0xa2,"&cent;"); // ¢
		htmlSigns.put(0xa3,"&pound;"); // £
		htmlSigns.put(0xa4,"&curren;"); // ¤
		htmlSigns.put(0xa5,"&yen;"); // ¥
		htmlSigns.put(0xa6,"&brvbar;"); // ¦
		htmlSigns.put(0xa7,"&sect;"); // §
		htmlSigns.put(0xa8,"&uml;"); // ¨
		htmlSigns.put(0xa9,"&copy;"); // ©
		htmlSigns.put(0xaa,"&ordf;"); // ª
		htmlSigns.put(0xab,"&laquo;"); // «
		htmlSigns.put(0xac,"&not;"); // ¬
		htmlSigns.put(0xad,"&shy;"); // ­
		htmlSigns.put(0xae,"&reg;"); // ®
		htmlSigns.put(0xaf,"&macr;"); // ¯
		htmlSigns.put(0xb0,"&deg;"); // °
		htmlSigns.put(0xb1,"&plusmn;"); // ±
		htmlSigns.put(0xb2,"&sup2;"); // ²
		htmlSigns.put(0xb3,"&sup3;"); // ³
		htmlSigns.put(0xb4,"&acute;"); // ´
		htmlSigns.put(0xb5,"&micro;"); // µ
		htmlSigns.put(0xb6,"&para;"); // ¶
		htmlSigns.put(0xb7,"&middot;"); // ·
		htmlSigns.put(0xb8,"&cedil;"); // ¸
		htmlSigns.put(0xb9,"&sup1;"); // ¹
		htmlSigns.put(0xba,"&ordm;"); // º
		htmlSigns.put(0xbb,"&raquo;"); // »
		htmlSigns.put(0xbc,"&frac14;"); // ¼
		htmlSigns.put(0xbd,"&frac12;"); // ½
		htmlSigns.put(0xbe,"&frac34;"); // ¾
		htmlSigns.put(0xbf,"&iquest;"); // ¿
		htmlSigns.put(0xc0,"&Agrave;"); // À
		htmlSigns.put(0xc1,"&Aacute;"); // Á
		htmlSigns.put(0xc2,"&Acirc;"); // Â
		htmlSigns.put(0xc3,"&Atilde;"); // Ã
		htmlSigns.put(0xc4,"&Auml;"); // Ä
		htmlSigns.put(0xc5,"&Aring;"); // Å
		htmlSigns.put(0xc6,"&AElig;"); // Æ
		htmlSigns.put(0xc7,"&Ccedil;"); // Ç
		htmlSigns.put(0xc8,"&Egrave;"); // È
		htmlSigns.put(0xc9,"&Eacute;"); // É
		htmlSigns.put(0xca,"&Ecirc;"); // Ê
		htmlSigns.put(0xcb,"&Euml;"); // Ë
		htmlSigns.put(0xcc,"&Igrave;"); // Ì
		htmlSigns.put(0xcd,"&Iacute;"); // Í
		htmlSigns.put(0xce,"&Icirc;"); // Î
		htmlSigns.put(0xcf,"&Iuml;"); // Ï
		htmlSigns.put(0xd0,"&ETH;"); // Ð
		htmlSigns.put(0xd1,"&Ntilde;"); // Ñ
		htmlSigns.put(0xd2,"&Ograve;"); // Ò
		htmlSigns.put(0xd3,"&Oacute;"); // Ó
		htmlSigns.put(0xd4,"&Ocirc;"); // Ô
		htmlSigns.put(0xd5,"&Otilde;"); // Õ
		htmlSigns.put(0xd6,"&Ouml;"); // Ö
		htmlSigns.put(0xd7,"&times;"); // ×
		htmlSigns.put(0xd8,"&Oslash;"); // Ø
		htmlSigns.put(0xd9,"&Ugrave;"); // Ù
		htmlSigns.put(0xda,"&Uacute;"); // Ú
		htmlSigns.put(0xdb,"&Ucirc;"); // Û
		htmlSigns.put(0xdc,"&Uuml;"); // Ü
		htmlSigns.put(0xdd,"&Yacute;"); // Ý
		htmlSigns.put(0xde,"&THORN;"); // Þ
		htmlSigns.put(0xdf,"&szlig;"); // ß
		htmlSigns.put(0xe0,"&agrave;"); // à
		htmlSigns.put(0xe1,"&aacute;"); // á
		htmlSigns.put(0xe2,"&acirc;"); // â
		htmlSigns.put(0xe3,"&atilde;"); // ã
		htmlSigns.put(0xe4,"&auml;"); // ä
		htmlSigns.put(0xe5,"&aring;"); // å
		htmlSigns.put(0xe6,"&aelig;"); // æ
		htmlSigns.put(0xe7,"&ccedil;"); // ç
		htmlSigns.put(0xe8,"&egrave;"); // è
		htmlSigns.put(0xe9,"&eacute;"); // é
		htmlSigns.put(0xea,"&ecirc;"); // ê
		htmlSigns.put(0xeb,"&euml;"); // ë
		htmlSigns.put(0xec,"&igrave;"); // ì
		htmlSigns.put(0xed,"&iacute;"); // í
		htmlSigns.put(0xee,"&icirc;"); // î
		htmlSigns.put(0xef,"&iuml;"); // ï
		htmlSigns.put(0xf0,"&eth;"); // ð
		htmlSigns.put(0xf1,"&ntilde;"); // ñ
		htmlSigns.put(0xf2,"&ograve;"); // ò
		htmlSigns.put(0xf3,"&oacute;"); // ó
		htmlSigns.put(0xf4,"&ocirc;"); // ô
		htmlSigns.put(0xf5,"&otilde;"); // õ
		htmlSigns.put(0xf6,"&ouml;"); // ö
		htmlSigns.put(0xf7,"&divide;"); // ÷
		htmlSigns.put(0xf8,"&oslash;"); // ø
		htmlSigns.put(0xf9,"&ugrave;"); // ù
		htmlSigns.put(0xfa,"&uacute;"); // ú
		htmlSigns.put(0xfb,"&ucirc;"); // û
		htmlSigns.put(0xfc,"&uuml;"); // ü
		htmlSigns.put(0xfd,"&yacute;"); // ý
		htmlSigns.put(0xfe,"&thorn;"); // þ
		htmlSigns.put(0xff,"&yuml;"); // ÿ
		htmlSigns.put(0x152,"&OElig;"); // Œ
		htmlSigns.put(0x153,"&oelig;"); // œ
		htmlSigns.put(0x160,"&Scaron;"); // Š
		htmlSigns.put(0x161,"&scaron;"); // š
		htmlSigns.put(0x178,"&Yuml;"); // Ÿ
		htmlSigns.put(0x192,"&fnof;"); // ƒ
		htmlSigns.put(0x2c6,"&circ;"); // ˆ
		htmlSigns.put(0x2dc,"&tilde;"); // ˜
		htmlSigns.put(0x2002,"&ensp;"); // ?
		htmlSigns.put(0x2003,"&emsp;"); // ?
		htmlSigns.put(0x2009,"&thinsp;"); // ?
		htmlSigns.put(0x200c,"&zwnj;"); // ?
		htmlSigns.put(0x200d,"&zwj;"); // ?
		htmlSigns.put(0x200e,"&lrm;"); // ?
		htmlSigns.put(0x200f,"&rlm;"); // ?
		htmlSigns.put(0x2013,"&ndash;"); // –
		htmlSigns.put(0x2014,"&mdash;"); // —
		htmlSigns.put(0x2018,"&lsquo;"); // ‘
		htmlSigns.put(0x2019,"&rsquo;"); // ’
		htmlSigns.put(0x201a,"&sbquo;"); // ‚
		htmlSigns.put(0x201c,"&ldquo;"); // “
		htmlSigns.put(0x201d,"&rdquo;"); // ”
		htmlSigns.put(0x201e,"&bdquo;"); // „
		htmlSigns.put(0x2020,"&dagger;"); // †
		htmlSigns.put(0x2021,"&Dagger;"); // ‡
		htmlSigns.put(0x2030,"&permil;"); // ‰
		htmlSigns.put(0x2039,"&lsaquo;"); // ‹
		htmlSigns.put(0x203a,"&rsaquo;"); // ›
		htmlSigns.put(0x20ac,"&euro;"); // €
		htmlSigns.put(0x391,"&Alpha;"); // ?
		htmlSigns.put(0x392,"&Beta;"); // ?
		htmlSigns.put(0x393,"&Gamma;"); // ?
		htmlSigns.put(0x394,"&Delta;"); // ?
		htmlSigns.put(0x395,"&Epsilon;"); // ?
		htmlSigns.put(0x396,"&Zeta;"); // ?
		htmlSigns.put(0x397,"&Eta;"); // ?
		htmlSigns.put(0x398,"&Theta;"); // ?
		htmlSigns.put(0x399,"&Iota;"); // ?
		htmlSigns.put(0x39a,"&Kappa;"); // ?
		htmlSigns.put(0x39b,"&Lambda;"); // ?
		htmlSigns.put(0x39c,"&Mu;"); // ?
		htmlSigns.put(0x39d,"&Nu;"); // ?
		htmlSigns.put(0x39e,"&Xi;"); // ?
		htmlSigns.put(0x39f,"&Omicron;"); // ?
		htmlSigns.put(0x3a0,"&Pi;"); // ?
		htmlSigns.put(0x3a1,"&Rho;"); // ?
		htmlSigns.put(0x3a3,"&Sigma;"); // ?
		htmlSigns.put(0x3a4,"&Tau;"); // ?
		htmlSigns.put(0x3a5,"&Upsilon;"); // ?
		htmlSigns.put(0x3a6,"&Phi;"); // ?
		htmlSigns.put(0x3a7,"&Chi;"); // ?
		htmlSigns.put(0x3a8,"&Psi;"); // ?
		htmlSigns.put(0x3a9,"&Omega;"); // ?
		htmlSigns.put(0x3b1,"&alpha;"); // ?
		htmlSigns.put(0x3b2,"&beta;"); // ?
		htmlSigns.put(0x3b3,"&gamma;"); // ?
		htmlSigns.put(0x3b4,"&delta;"); // ?
		htmlSigns.put(0x3b5,"&epsilon;"); // ?
		htmlSigns.put(0x3b6,"&zeta;"); // ?
		htmlSigns.put(0x3b7,"&eta;"); // ?
		htmlSigns.put(0x3b8,"&theta;"); // ?
		htmlSigns.put(0x3b9,"&iota;"); // ?
		htmlSigns.put(0x3ba,"&kappa;"); // ?
		htmlSigns.put(0x3bb,"&lambda;"); // ?
		htmlSigns.put(0x3bc,"&mu;"); // ?
		htmlSigns.put(0x3bd,"&nu;"); // ?
		htmlSigns.put(0x3be,"&xi;"); // ?
		htmlSigns.put(0x3bf,"&omicron;"); // ?
		htmlSigns.put(0x3c0,"&pi;"); // ?
		htmlSigns.put(0x3c1,"&rho;"); // ?
		htmlSigns.put(0x3c2,"&sigmaf;"); // ?
		htmlSigns.put(0x3c3,"&sigma;"); // ?
		htmlSigns.put(0x3c4,"&tau;"); // ?
		htmlSigns.put(0x3c5,"&upsilon;"); // ?
		htmlSigns.put(0x3c6,"&phi;"); // ?
		htmlSigns.put(0x3c7,"&chi;"); // ?
		htmlSigns.put(0x3c8,"&psi;"); // ?
		htmlSigns.put(0x3c9,"&omega;"); // ?
		htmlSigns.put(0x3d1,"&thetasym;"); // ?
		htmlSigns.put(0x3d2,"&upsih;"); // ?
		htmlSigns.put(0x3d6,"&piv;"); // ?
		htmlSigns.put(0x2022,"&bull;"); // •
		htmlSigns.put(0x2026,"&hellip;"); // …
		htmlSigns.put(0x2032,"&prime;"); // ?
		htmlSigns.put(0x2033,"&Prime;"); // ?
		htmlSigns.put(0x203e,"&oline;"); // ?
		htmlSigns.put(0x2044,"&frasl;"); // ?
		htmlSigns.put(0x2118,"&weierp;"); // ?
		htmlSigns.put(0x2111,"&image;"); // ?
		htmlSigns.put(0x211c,"&real;"); // ?
		htmlSigns.put(0x2122,"&trade;"); // ™
		htmlSigns.put(0x2135,"&alefsym;"); // ?
		htmlSigns.put(0x2190,"&larr;"); // ?
		htmlSigns.put(0x2191,"&uarr;"); // ?
		htmlSigns.put(0x2192,"&rarr;"); // ?
		htmlSigns.put(0x2193,"&darr;"); // ?
		htmlSigns.put(0x2194,"&harr;"); // ?
		htmlSigns.put(0x21b5,"&crarr;"); // ?
		htmlSigns.put(0x21d0,"&lArr;"); // ?
		htmlSigns.put(0x21d1,"&uArr;"); // ?
		htmlSigns.put(0x21d2,"&rArr;"); // ?
		htmlSigns.put(0x21d3,"&dArr;"); // ?
		htmlSigns.put(0x21d4,"&hArr;"); // ?
		htmlSigns.put(0x2200,"&forall;"); // ?
		htmlSigns.put(0x2202,"&part;"); // ?
		htmlSigns.put(0x2203,"&exist;"); // ?
		htmlSigns.put(0x2205,"&empty;"); // ?
		htmlSigns.put(0x2207,"&nabla;"); // ?
		htmlSigns.put(0x2208,"&isin;"); // ?
		htmlSigns.put(0x2209,"&notin;"); // ?
		htmlSigns.put(0x220b,"&ni;"); // ?
		htmlSigns.put(0x220f,"&prod;"); // ?
		htmlSigns.put(0x2211,"&sum;"); // ?
		htmlSigns.put(0x2212,"&minus;"); // ?
		htmlSigns.put(0x2217,"&lowast;"); // ?
		htmlSigns.put(0x221a,"&radic;"); // ?
		htmlSigns.put(0x221d,"&prop;"); // ?
		htmlSigns.put(0x221e,"&infin;"); // ?
		htmlSigns.put(0x2220,"&ang;"); // ?
		htmlSigns.put(0x2227,"&and;"); // ?
		htmlSigns.put(0x2228,"&or;"); // ?
		htmlSigns.put(0x2229,"&cap;"); // ?
		htmlSigns.put(0x222a,"&cup;"); // ?
		htmlSigns.put(0x222b,"&int;"); // ?
		htmlSigns.put(0x2234,"&there4;"); // ?
		htmlSigns.put(0x223c,"&sim;"); // ?
		htmlSigns.put(0x2245,"&cong;"); // ?
		htmlSigns.put(0x2248,"&asymp;"); // ?
		htmlSigns.put(0x2260,"&ne;"); // ?
		htmlSigns.put(0x2261,"&equiv;"); // ?
		htmlSigns.put(0x2264,"&le;"); // ?
		htmlSigns.put(0x2265,"&ge;"); // ?
		htmlSigns.put(0x2282,"&sub;"); // ?
		htmlSigns.put(0x2283,"&sup;"); // ?
		htmlSigns.put(0x2284,"&nsub;"); // ?
		htmlSigns.put(0x2286,"&sube;"); // ?
		htmlSigns.put(0x2287,"&supe;"); // ?
		htmlSigns.put(0x2295,"&oplus;"); // ?
		htmlSigns.put(0x2297,"&otimes;"); // ?
		htmlSigns.put(0x22a5,"&perp;"); // ?
		htmlSigns.put(0x22c5,"&sdot;"); // ?
		htmlSigns.put(0x2308,"&lceil;"); // ?
		htmlSigns.put(0x2309,"&rceil;"); // ?
		htmlSigns.put(0x230a,"&lfloor;"); // ?
		htmlSigns.put(0x230b,"&rfloor;"); // ?
		htmlSigns.put(0x2329,"&lang;"); // ?
		htmlSigns.put(0x232a,"&rang;"); // ?
		htmlSigns.put(0x25ca,"&loz;"); // ?
		htmlSigns.put(0x2660,"&spades;"); // ?
		htmlSigns.put(0x2663,"&clubs;"); // ?
		htmlSigns.put(0x2665,"&hearts;"); // ?
		htmlSigns.put(0x2666,"&diams;"); // ?
	}
	

	/**
	 * Converts chars to html character instructions if necessary.<br>
	 * <br>
	 * e.g: &lt; to &amp;lt;
	 * 
	 * @param s
	 *            a String
	 * 
	 * @return a html displayable string
	 */
	public static String toHTML(String s)
	{
		StringBuffer sb = new StringBuffer(s.length());
		
		for(int i = 0; i < s.length(); i++)
		{
			char ch = s.charAt(i);
			String htmlSpecialSign = htmlSigns.get((int)ch);
			if(htmlSpecialSign != null)
			{
				sb.append(htmlSpecialSign);
			}
			else
			{
				int ascii = (int)ch;
				if(ascii > 126)
				{
					sb.append('&');
					sb.append(ascii);
					sb.append(';');
				}
				else
				{
					sb.append(ch);
				}
			}
		}
		
		return sb.toString();
	}
	

	/**
	 * Converts {@link Font} to html stylesheet.<br>
	 * 
	 * <p>
	 * Examples:
	 * 
	 * <br>
	 * Font font = new Font("Times New Roman", Font.ITALIC, 12); <br>
	 * returns
	 * "font-family:'Times New Roman'; font-style:italic; font-size:12pt;"
	 * </p>
	 * 
	 * <p>
	 * This method is a alias for
	 * <code>HTMLUtils.toStyle(f.getFamily(),f.getStyle(),f.getSize());</code>
	 * </p>
	 * 
	 * 
	 * @param f
	 *            the {@link Font} of this style
	 * 
	 * @return a html displayable string
	 * 
	 * @see #toStyle(String, int, int)
	 */
	public static String toStyle(Font f)
	{
		return toStyle(f.getFamily(),f.getStyle(),f.getSize());
	}
	

	/**
	 * Converts {@link Font} to html stylesheet.<br>
	 * 
	 * <p>
	 * Examples:
	 * 
	 * <br>
	 * Font font = new Font("Times New Roman", Font.ITALIC, 12); <br>
	 * returns
	 * "font-family:'Times New Roman'; font-style:italic; font-size:12pt;"
	 * </p>
	 * 
	 * 
	 * @param fontFamily
	 *            the font family name as <code>String</code>
	 * 
	 * @param style
	 *            the font style as <code>int</code>
	 * @param size
	 *            the font size as <code>int</code>
	 * 
	 * @return a html displayable string
	 * 
	 * @see #toStyle(Font)
	 */
	public static String toStyle(String fontFamily, int style, int size)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("font-family:'");
		sb.append(fontFamily);
		sb.append("';");
		
		if((style & Font.ITALIC) != 0)
		{
			sb.append(" font-style:italic;");
		}
		
		if((style & Font.BOLD) != 0)
		{
			sb.append(" font-weight:bold;");
		}
		
		sb.append(" font-size:");
		sb.append(size);
		sb.append("pt;");
		
		return sb.toString();
	}
	
	private static HTML2Text	html2text;
	

	// TODO java doc
	public static synchronized String htmlToText(String html)
	{
		if(html2text == null)
		{
			html2text = new HTML2Text();
		}
		else
		{
			html2text.reset();
		}
		
		Reader in = new StringReader(html);
		try
		{
			html2text.parse(in);
		}
		catch(Exception e)
		{
		}
		finally
		{
			IOUtils.closeSilent(in);
		}
		
		return html2text.getText();
	}
	


	private static class HTML2Text extends HTMLEditorKit.ParserCallback
	{
		StringBuffer		stringBuffer;
		Stack<IndexType>	indentStack;
		


		static class IndexType
		{
			String	type;
			int		counter;
			

			IndexType(String type)
			{
				this.type = type;
				counter = 0;
			}
		}
		

		HTML2Text()
		{
			stringBuffer = new StringBuffer();
			indentStack = new Stack<IndexType>();
		}
		

		void reset()
		{
			stringBuffer.setLength(0);
			indentStack.clear();
		}
		

		void parse(Reader in) throws IOException
		{
			ParserDelegator delegator = new ParserDelegator();
			// the third parameter is TRUE to ignore charset directive
			delegator.parse(in,this,Boolean.TRUE);
		}
		

		public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos)
		{
			if(t.toString().equals("p"))
			{
				if(stringBuffer.length() > 0
						&& !stringBuffer.substring(stringBuffer.length() - 1).equals("\n"))
				{
					newLine();
				}
				newLine();
			}
			else if(t.toString().equals("ol"))
			{
				indentStack.push(new IndexType("ol"));
				newLine();
			}
			else if(t.toString().equals("ul"))
			{
				indentStack.push(new IndexType("ul"));
				newLine();
			}
			else if(t.toString().equals("li"))
			{
				IndexType parent = indentStack.peek();
				if(parent.type.equals("ol"))
				{
					String numberString = "" + (++parent.counter) + ".";
					stringBuffer.append(numberString);
					for(int i = 0; i < (4 - numberString.length()); i++)
					{
						stringBuffer.append(" ");
					}
				}
				else
				{
					stringBuffer.append("*   ");
				}
				indentStack.push(new IndexType("li"));
			}
			else if(t.toString().equals("dl"))
			{
				newLine();
			}
			else if(t.toString().equals("dt"))
			{
				newLine();
			}
			else if(t.toString().equals("dd"))
			{
				indentStack.push(new IndexType("dd"));
				newLine();
			}
		}
		

		void newLine()
		{
			stringBuffer.append("\n");
			for(int i = 0; i < indentStack.size(); i++)
			{
				stringBuffer.append("    ");
			}
		}
		

		public void handleEndTag(HTML.Tag t, int pos)
		{
			if(t.toString().equals("p"))
			{
				newLine();
			}
			else if(t.toString().equals("ol"))
			{
				indentStack.pop();
				;
				newLine();
			}
			else if(t.toString().equals("ul"))
			{
				indentStack.pop();
				;
				newLine();
			}
			else if(t.toString().equals("li"))
			{
				indentStack.pop();
				;
				newLine();
			}
			else if(t.toString().equals("dd"))
			{
				indentStack.pop();
				;
			}
		}
		

		public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos)
		{
			if(t.toString().equals("br"))
			{
				newLine();
			}
		}
		

		public void handleText(char[] text, int pos)
		{
			stringBuffer.append(text);
		}
		

		String getText()
		{
			return stringBuffer.toString();
		}
	}
}
