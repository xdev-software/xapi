package xdev.ui.text;

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


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.beans.Beans;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.rtf.RTFEditorKit;

import xdev.Application;
import xdev.ui.UIUtils;
import xdev.ui.XdevApplicationContainer;
import xdev.ui.XdevTextComponent;
import xdev.ui.event.TextChangeAdapter;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


public class XdevDocument
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log						= LoggerFactory
			.getLogger(XdevDocument.class);

	public final static int			LEFT					= 0;
	public final static int			RIGHT					= 1;
	public final static int			CENTER					= 2;
	public final static int			TRAILING				= 3;
	public final static int			JUSTIFIED				= TRAILING;

	private final static int		TAB_WIDTH				= 30;

	public final static Object		LINK_ATTRIBUTE			= "LINK";

	public final static int			FIT_NONE				= 0;
	public final static int			FIT_STRETCH				= 1;
	public final static int			FIT_FIT					= 2;

	public final static String		CONTENT_TYPE_TEXT_PLAIN	= "text/plain";
	public final static String		CONTENT_TYPE_TEXT_RTF	= "text/rtf";
	// public final static String CONTENT_TYPE_TEXT_HTML = "text/html";

	private XdevTextComponent		txtCpn;

	private String					text;
	private String					contentType				= CONTENT_TYPE_TEXT_PLAIN;

	private Sign[]					signs;
	private Word[]					words;
	private PaintSet[]				paintSet;
	private Rectangle				place;

	private List<TextChangeAdapter>	listeners				= new Vector();


	public XdevDocument(XdevTextComponent txtCpn)
	{
		this.txtCpn = txtCpn;

		Font font = txtCpn.getFont();
		Color color = txtCpn.getForeground();
		boolean underline = txtCpn.getUnderline();
		char ch = '\n';
		FontRenderContext frc = getFontRenderContext();
		FontMetrics fm = getComponent().getFontMetrics(font);
		LineMetrics lm = font.getLineMetrics("" + ch,frc);
		Char s = new Char(ch,font,fm.getAscent(),fm.getDescent(),fm.getLeading(),color,underline,
				(int)lm.getUnderlineOffset(),(int)lm.getUnderlineThickness());
		this.signs = new Sign[]{s};
	}


	private Component getComponent()
	{
		Component cpn = this.txtCpn.getCpn();
		if(cpn != null && cpn.isShowing())
		{
			return cpn;
		}

		if(!Beans.isDesignTime())
		{
			XdevApplicationContainer container = Application.getContainer();
			if(container != null)
			{
				cpn = (Component)container;
				if(cpn != null && cpn.isShowing())
				{
					return cpn;
				}
			}
		}

		return JOptionPane.getRootFrame();
	}


	private XdevDocument(Sign[] signs, XdevTextComponent txtCpn)
	{
		this.txtCpn = txtCpn;
		this.signs = signs;
	}


	public XdevDocument copy(XdevTextComponent txtCpn)
	{
		return new XdevDocument(this.signs,txtCpn);
	}


	public void addChangeListener(TextChangeAdapter listener)
	{
		this.listeners.add(listener);
	}


	public void removeChangeListener(TextChangeAdapter listener)
	{
		this.listeners.remove(listener);
	}


	protected void fireTextChanged()
	{
		int c = this.listeners.size();
		if(c > 0)
		{
			final int length = length();
			DocumentEvent event = new DocumentEvent()
			{
				@Override
				public int getOffset()
				{
					return 0;
				}


				@Override
				public int getLength()
				{
					return length;
				}


				@Override
				public Document getDocument()
				{
					return null;
				}


				@Override
				public EventType getType()
				{
					return EventType.CHANGE;
				}


				@Override
				public ElementChange getChange(Element elem)
				{
					return null;
				}

			};
			for(TextChangeAdapter listener : this.listeners)
			{
				listener.textChanged(event);
			}
		}
	}


	public int length()
	{
		return this.signs.length;
	}


	public Sign getSign(int index)
	{
		return this.signs[index];
	}


	public void paint(Graphics g0)
	{
		if(this.signs == null)
		{
			return;
		}

		if(this.paintSet == null)
		{
			if(this.text != null)
			{
				setText(this.text,this.contentType);
			}

			createPaintSets();
		}

		Graphics2D g = (Graphics2D)g0;

		RenderingHints hints = (RenderingHints)g.getRenderingHints().clone();
		UIUtils.setQualityHints(g);

		Shape clip = g.getClip();

		for(int i = 0; this.paintSet != null && i < this.paintSet.length; i++)
		{
			this.paintSet[i].paint(g,clip);
		}

		g.setRenderingHints(hints);
	}


	private void createPaintSets()
	{
		List<PaintSet> list = new ArrayList();
		int index = 0;
		Sign s = this.signs[0];
		for(int i = 0; i < this.signs.length - 1; i++)
		{
			if(this.signs[i] instanceof TextImage)
			{
				if(index < i)
				{
					list.add(new PaintSet(index,i,(Char)s));
					s = null;
				}
				list.add(new PaintSet((TextImage)this.signs[i]));
				index = i + 1;
			}
			else
			{
				Char c = (Char)this.signs[i];
				if(s == null)
				{
					s = c;
				}
				if(c.isTab() || c.isSpace())
				{
					if(index < i)
					{
						list.add(new PaintSet(index,i,(Char)s));
					}
					int ti = i;
					while(ti < this.signs.length - 1
							&& (this.signs[ti].isTab() || this.signs[ti].isSpace()))
					{
						list.add(new PaintSet((Char)this.signs[i]));
						ti++;
					}
					i = index = ti;
					if(i < this.signs.length - 1 && this.signs[i] instanceof Char)
					{
						s = this.signs[i];
					}
					else
					{
						i--;
					}
				}
				else if(!this.signs[i].equalsStyleAndRow(s))
				{
					if(s instanceof TextImage)
					{
						list.add(new PaintSet((TextImage)s));
						index = i + 1;
					}
					else
					{
						list.add(new PaintSet(index,i,(Char)s));
						index = i;
					}
					s = this.signs[i];
				}
			}
		}
		if(index < this.signs.length - 1)
		{
			if(s instanceof TextImage)
			{
				list.add(new PaintSet((TextImage)s));
			}
			else
			{
				list.add(new PaintSet(index,this.signs.length - 1,(Char)s));
			}
		}

		this.paintSet = list.toArray(new PaintSet[list.size()]);
	}



	private class PaintSet
	{
		private SignBounds	outline;
		private boolean		underline;
		private int			x, y, uX, uY, uX2;
		private Char		s;
		private char[]		chars;
		private TextImage	image;


		public PaintSet(TextImage s)
		{
			this.image = s;
			this.outline = new SignBounds(s.rect.x,s.rect.y,s.rect.width,s.rect.height);
		}


		public PaintSet(Char s)
		{
			this.s = s;

			this.chars = new char[0];

			this.x = s.rect.x;
			this.y = s.drawY;

			this.underline = s.underline;
			if(this.underline)
			{
				this.uX = s.rect.x;
				this.uY = s.drawY + s.underlineY + 1;
				this.uX2 = Math.round(s.rect.x + s.rect.width);
			}

			this.outline = new SignBounds(s.rect.x,s.drawY,s.rect.width,s.rect.height);
		}


		public PaintSet(int start, int end, Char s)
		{
			this.s = s;

			this.outline = s.getBounds();

			int ci = 0;
			this.chars = new char[end - start];
			for(int i = start; i < end; i++, ci++)
			{
				this.chars[ci] = getSign(i).c;
				getSign(i).addBounds(this.outline);
			}

			this.x = s.rect.x;
			this.y = s.drawY;

			this.underline = s.underline;
			if(this.underline)
			{
				this.uX = s.rect.x;
				this.uY = s.drawY + s.underlineY + 1;
				Char us = getSign(end - 1);
				this.uX2 = Math.round(us.rect.x + us.rect.width);
			}
		}


		private Char getSign(int i)
		{
			return (Char)XdevDocument.this.signs[i];
		}

		private GlyphVector	glyphVector;


		public void paint(Graphics2D g, Shape clip)
		{
			if(clip == null
					|| clip.intersects(this.outline.x,this.outline.y,this.outline.width,
							this.outline.height))
			{
				if(this.image != null)
				{
					this.image.paint(g);
				}
				else
				{
					if(this.glyphVector == null && this.chars.length > 0)
					{
						this.glyphVector = this.s.font.createGlyphVector(g.getFontRenderContext(),
								this.chars);
					}

					g.setColor(this.s.color);

					if(this.glyphVector != null)
					{
						g.drawGlyphVector(this.glyphVector,this.x,this.y);
					}

					if(this.underline)
					{
						g.drawLine(this.uX,this.uY,this.uX2,this.uY);
					}
				}
			}
		}
	}


	public Link getSignLinkForPoint(Point p)
	{
		SignBounds r;
		for(int i = 0; i < this.signs.length; i++)
		{
			r = this.signs[i].getBounds();
			if(this.signs[i] != null && r != null && r.getBounds().contains(p))
			{
				return this.signs[i].getLink();
			}
		}
		return null;
	}
	private boolean	fire	= true;


	public void recalc()
	{
		if(this.text == null)
		{
			this.text = toString();
		}

		this.fire = false;
		int h = Math.max(setText(this.text,this.contentType,FIT_STRETCH),
				this.txtCpn.getOriginalSize().height);
		this.fire = true;

		Dimension dim = new Dimension(this.txtCpn.getPreferredSize().width,h);
		this.txtCpn.setPreferredSize(dim);
	}


	public int setText(String text)
	{
		return setText(text,CONTENT_TYPE_TEXT_PLAIN);
	}


	public int setText(String text, String contentType)
	{
		return setText(text,contentType,FIT_NONE);
	}


	public int setText(String text, int fit)
	{
		return setText(text,this.contentType,fit);
	}


	public int setText(String text, String contentType, int fit)
	{
		this.text = text;
		this.contentType = contentType;

		this.txtCpn.setPaintText();
		// txtCpn.setTextColumnCount(1);

		Color color = this.txtCpn.getForeground();
		Font font = this.txtCpn.getFont();
		boolean underline = this.txtCpn.getUnderline();

		int len = text.length();

		Link link = null;
		FontRenderContext frc = getFontRenderContext();

		if(CONTENT_TYPE_TEXT_PLAIN.equals(contentType))
		{
			text = text.concat("\n");
			len = text.length();

			FontMetrics fm = getComponent().getFontMetrics(font);
			int height = fm.getHeight();
			int ascent = fm.getAscent();
			int descent = fm.getDescent();
			int leading = fm.getLeading();

			Sign[] signs = new Sign[len];
			for(int i = 0; i < len; i++)
			{
				char ch = text.charAt(i);

				LineMetrics lm = font.getLineMetrics("" + ch,frc);
				signs[i] = new Char(ch,font,ascent,descent,leading,color,underline,
						(int)lm.getUnderlineOffset(),(int)lm.getUnderlineThickness());
				signs[i].getBounds().setBounds(0,0,
						ch == '\n' ? 2 : ch == '\t' ? 0 : fm.charWidth(ch),height);
				signs[i].setLink(link);
			}

			this.signs = signs;
		}
		else if(CONTENT_TYPE_TEXT_RTF.equals(contentType))
		{
			DefaultStyledDocument dsd = new DefaultStyledDocument();
			RTFEditorKit rtf = new RTFEditorKit();
			try
			{
				rtf.read(new StringReader(text),dsd,0);

				text = dsd.getText(0,dsd.getLength());
				text = text.concat("\n");
				len = text.length();
				Sign[] signs = new Sign[len];

				for(int i = 0; i < len; i++)
				{
					AttributeSet as = dsd.getCharacterElement(i).getAttributes();
					char ch = text.charAt(i);
					String fontName = StyleConstants.getFontFamily(as);
					int fontStyle = Font.PLAIN;
					if(StyleConstants.isBold(as))
					{
						fontStyle += Font.BOLD;
					}
					if(StyleConstants.isItalic(as))
					{
						fontStyle += Font.ITALIC;
					}
					int fontSize = StyleConstants.getFontSize(as);

					color = StyleConstants.getForeground(as);
					underline = StyleConstants.isUnderline(as);

					font = new Font(fontName,fontStyle,fontSize);

					FontMetrics fm = getComponent().getFontMetrics(font);
					int height = fm.getHeight();
					int ascent = fm.getAscent();
					int descent = fm.getDescent();
					int leading = fm.getLeading();

					LineMetrics lm = font.getLineMetrics("" + ch,frc);
					signs[i] = new Char(ch,font,ascent,descent,leading,color,underline,
							(int)lm.getUnderlineOffset(),(int)lm.getUnderlineThickness());
					signs[i].getBounds().setBounds(0,0,
							ch == '\n' ? 2 : ch == '\t' ? 0 : fm.charWidth(ch),height);
				}

				this.signs = signs;
			}
			catch(Exception e)
			{
				// BadLocation or I/O - shouldn't happen
				log.error(e);
			}
		}
		else
		{
			throw new IllegalArgumentException("content type not supported: " + contentType);
		}

		int retVal = relayout(fit);

		fireTextChanged();

		return retVal;
	}


	public synchronized int relayout(int fit)
	{
		this.place = new Rectangle(this.txtCpn.getSize());

		Insets bi = this.txtCpn.getBorderInsets(true);

		this.place.x += bi.left;
		this.place.y += bi.top;
		this.place.width -= (bi.left + bi.right + 1);
		this.place.height -= (bi.top + bi.bottom);

		if(this.txtCpn.isVertical())
		{
			int w = this.place.width;
			this.place.width = this.place.height;
			this.place.height = w;
		}

		List<Word> list = new ArrayList();
		Word word = new Word();
		boolean addLast = false;

		for(int i = 0; i < this.signs.length; i++)
		{
			if(this.signs[i].canBreak() || this.signs[i].isTab() || this.signs[i].isBreak())
			{
				if(word.signs.size() > 0)
				{
					list.add(word);
				}
				word = new Word();
				word.signs.addElement(this.signs[i]);
				list.add(word);
				word = new Word();
				addLast = false;
			}
			else
			{
				word.signs.addElement(this.signs[i]);
				addLast = true;
			}
		}
		if(addLast)
		{
			list.add(word);
		}

		int wordCount = list.size();
		this.words = new Word[wordCount];
		for(int i = 0; i < wordCount; i++)
		{
			this.words[i] = list.get(i);
			this.words[i].computeSize();
		}

		boolean morePlace = true;
		int y = this.place.y, curRow = 0;

		if(fit != FIT_NONE)
		{
			this.place.height = 10000;
		}

		int align = this.txtCpn.getHorizontalAlign();

		int curWord = 0;
		Sign lastSign = null;

		while(curWord < this.words.length && morePlace)
		{
			int rowWidth = 0, rowHeight = 0;

			for(int wi = curWord; wi < this.words.length; wi++)
			{
				if(rowWidth + this.words[wi].width <= this.place.width || this.words[wi].isSpace())
				{
					rowWidth += this.words[wi].width;
					rowHeight = Math.max(rowHeight,this.words[wi].height);
				}
				else
				{
					break;
				}
			}

			Rectangle rowRect = new Rectangle(this.place.x,y,this.place.width,rowHeight);
			rowHeight = 0;

			int usedWidth = 0;
			RowBox rowBox = null;
			boolean next = true, doBreak = false;

			while(next && curWord < this.words.length)
			{
				if(this.words[curWord].isBreak())
				{
					doBreak = true;
					next = false;
					rowHeight = Math.max(this.words[curWord++].height,rowHeight);
				}
				else
				{
					float ww = this.words[curWord].width;
					if(this.words[curWord].isTab())
					{
						ww = TAB_WIDTH - ((rowRect.x + usedWidth) % TAB_WIDTH);
						usedWidth += ww;
						this.words[curWord].width = this.words[curWord].sign[0].getBounds().width = ww;
					}
					if(usedWidth + ww < rowRect.width || this.words[curWord].isSpace())
					{
						usedWidth += ww;
						rowHeight = Math.max(rowHeight,this.words[curWord].height);
						if(rowBox == null)
						{
							rowBox = new RowBox(rowRect);
						}
						rowBox.addWord(this.words[curWord]);
						curWord++;
					}
					else
					{
						next = false;
					}
				}
			}

			if(rowHeight > 0 && rowBox != null)
			{
				rowRect.height = rowHeight;

				lastSign = rowBox.layout(align,rowHeight,curRow);

				curRow++;
				y += rowHeight;
			}
			else if(doBreak)
			{
				curRow++;
				y += rowHeight;
			}
			else
			{
				if(rowRect != null && this.words[curWord].width >= rowRect.width
						&& this.words[curWord].sign[0].getBounds().width < rowRect.width)
				{
					float tw = this.words[curWord].width;
					Word newWord = new Word();

					while(this.words[curWord].signs.size() > 0)
					{
						if(tw >= rowRect.width)
						{
							Sign ts = (Sign)this.words[curWord].signs
									.elementAt(this.words[curWord].signs.size() - 1);
							this.words[curWord].signs.removeElement(ts);
							newWord.signs.insertElementAt(ts,0);
							tw -= ts.getBounds().width;
						}
						else
						{
							break;
						}
					}

					this.words[curWord].computeSize();
					newWord.computeSize();

					int wc = this.words.length;
					Word[] oldWords = this.words;
					this.words = new Word[wc + 1];
					System.arraycopy(oldWords,0,this.words,0,curWord + 1);
					System.arraycopy(oldWords,curWord + 1,this.words,curWord + 2,wc - curWord - 1);
					this.words[curWord + 1] = newWord;
				}
				else
				{
					y++;
				}
			}

			if(y >= this.place.y + this.place.height)
			{
				morePlace = false;
			}
		}

		this.paintSet = null;

		if(lastSign == null)
		{
			if(this.fire)
			{
				this.txtCpn.repaint();
			}

			return 1;
		}

		Dimension d = this.txtCpn.getSize();
		int h = lastSign.getBounds().y + lastSign.getBounds().height + bi.bottom;
		if(fit == FIT_STRETCH)
		{
			h = Math.max(h,d.height);
		}

		if(this.fire)
		{
			if(fit != FIT_NONE && lastSign != null)
			{
				Dimension dim = new Dimension(d.width,h);
				this.txtCpn.setSize(dim);
				this.txtCpn.setPreferredSize(dim);

				this.txtCpn.repaint();
			}
		}

		return h;
	}


	private FontRenderContext getFontRenderContext()
	{
		Component cpn = getComponent();
		Graphics graphics = cpn != null ? cpn.getGraphics() : null;
		return graphics != null ? ((Graphics2D)graphics).getFontRenderContext()
				: new FontRenderContext(null,false,false);
	}


	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < this.signs.length - 1; i++)
		{
			sb.append(this.signs[i].toString());
		}
		return sb.toString();
	}


	public int getPlaceWidth()
	{
		Insets bi = this.txtCpn.getBorderInsets(true);
		return this.txtCpn.getSize().width - (bi.left + bi.right + 1);
	}


	public Font getFont()
	{
		return this.txtCpn.getFont();
	}


	public Color getForeground()
	{
		return this.txtCpn.getForeground();
	}


	public Insets getBorderInsets()
	{
		return this.txtCpn.getBorderInsets(true);
	}


	public int getHorizontalAlign()
	{
		return this.txtCpn.getHorizontalAlign();
	}


	public boolean isVertical()
	{
		return this.txtCpn.isVertical();
	}


	public int getVerticalAlign()
	{
		return this.txtCpn.getVerticalAlign();
	}


	public synchronized String[] getLines()
	{
		if(this.signs.length == 0)
		{
			return new String[0];
		}

		Map<Integer, List<String>> map = new Hashtable();
		int maxRow = 0;
		for(int i = 0; i < this.words.length; i++)
		{
			maxRow = Math.max(maxRow,this.words[i].row);
			Integer key = new Integer(this.words[i].row);
			List list;
			if(map.containsKey(key))
			{
				list = map.get(key);
			}
			else
			{
				list = new ArrayList();
				map.put(key,list);
			}
			list.add(this.words[i]);
		}

		List<String> list = new ArrayList(maxRow);
		for(int i = 0; i <= maxRow; i++)
		{
			List<String> row = map.get(i);
			if(row == null)
			{
				list.add("");
			}
			else
			{
				StringBuffer sb = new StringBuffer();
				for(int c = 0; c < row.size(); c++)
				{
					sb.append(row.get(c));
				}
				list.add(sb.toString());
			}
		}

		return list.toArray(new String[list.size()]);
	}
}
