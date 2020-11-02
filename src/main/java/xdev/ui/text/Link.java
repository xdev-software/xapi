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


import java.awt.Component;
import java.io.IOException;

import xdev.io.IOUtils;
import xdev.ui.Formular;
import xdev.ui.FormularSupport;


public class Link
{
	public final static int	NONE		= 0;
	public final static int	PAGELINK	= 1;
	public final static int	HYPERLILNK	= 2;
	public final static int	EMAIL		= 3;
	public final static int	FORM_SUBMIT	= 4;
	public final static int	FORM_RESET	= 5;
	
	public int				type;
	public String			page, hyperURL, hyperTarget, formURL, formTarget, mailReceiver,
			mailSubject, mailCC, mailBCC, mailText;
	
	
	public Link(String page)
	{
		type = PAGELINK;
		this.page = page;
	}
	
	
	public Link(String url, String target)
	{
		type = HYPERLILNK;
		hyperURL = url;
		hyperTarget = target;
	}
	
	
	public Link(String receiver, String subject, String cc, String bcc, String text)
	{
		type = EMAIL;
		mailReceiver = receiver;
		mailSubject = subject;
		mailCC = cc;
		mailBCC = bcc;
		mailText = text;
	}
	
	
	public Link(int type)
	{
		if(!(type == NONE || type == FORM_SUBMIT || type == FORM_RESET))
		{
			throw new IllegalArgumentException(
					"Type has to be FORM_SUBMIT, FORM_RESET or NONE, for other types use other consturctors");
		}
		
		this.type = type;
	}
	
	
	@SuppressWarnings("deprecation")
	public void execute(Component source) throws IOException
	{
		switch(type)
		{
			case PAGELINK:
			{
				// TODO
				// UIUtils.showWindow(page);
			}
			break;
			
			case HYPERLILNK:
			{
				IOUtils.showDocument(hyperURL,hyperTarget);
			}
			break;
			
			case EMAIL:
			{
				StringBuffer mailOpt = new StringBuffer("?");
				if(mailCC.trim().length() > 0)
				{
					mailOpt.append("cc=" + mailCC + "&");
				}
				if(mailBCC.trim().length() > 0)
				{
					mailOpt.append("bcc=" + mailBCC + "&");
				}
				if(mailSubject.trim().length() > 0)
				{
					mailOpt.append("subject=" + mailSubject + "&");
				}
				if(mailText.trim().length() > 0)
				{
					mailOpt.append("body=" + mailText + "&");
				}
				mailOpt.setLength(mailOpt.length() - 1);
				StringBuffer mailStr = new StringBuffer("mailto:" + mailReceiver);
				if(mailOpt.length() > 0)
				{
					mailStr.append(mailOpt.toString());
				}
				
				IOUtils.showDocument(mailStr.toString(),null);
			}
			break;
			
			case FORM_SUBMIT:
			{
				Formular form = FormularSupport.getFormularOf(source);
				if(form != null)
				{
					form.submit(formURL,formTarget);
				}
			}
			break;
			
			case FORM_RESET:
			{
				Formular form = FormularSupport.getFormularOf(source);
				if(form != null)
				{
					form.reset();
				}
			}
			break;
		}
	}
}
