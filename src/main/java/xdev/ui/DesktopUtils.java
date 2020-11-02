package xdev.ui;

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


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

import xdev.io.IOUtils;
import xdev.lang.LibraryMember;


/**
 * The {@code DesktopUtils} class allows to launch associated applications
 * registered on the native desktop to handle a {@link java.net.URI}, a path or
 * a file.
 * 
 * <p>
 * Supported operations include:
 * <ul>
 * <li>launching the user-default browser to show a specified URI;</li>
 * <li>launching the user-default mail client with an optional {@code mailto}
 * URI;</li>
 * <li>launching a registered application to open, edit or print a specified
 * file.</li>
 * </ul>
 * 
 * @see Desktop
 * 
 * @author XDEV Software
 * @since 3.1
 */
@LibraryMember
public final class DesktopUtils
{
	/**
	 * <p>
	 * <code>DesktopUtils</code> class can not be instantiated.
	 * </p>
	 */
	private DesktopUtils()
	{
	}
	

	/**
	 * Launches the default browser to display a URI.
	 * 
	 * @param uri
	 *            the URI to be displayed in the user default browser
	 * @throws URISyntaxException
	 *             If the given string isn't a valid URI (RFC 2396)
	 * @throws IOException
	 *             if an error occurs
	 * @see Desktop#browse(URI)
	 */
	public static void browse(String uri) throws URISyntaxException, IOException
	{
		browse(new URI(uri));
	}
	

	/**
	 * Launches the default browser to display a URI.
	 * 
	 * @param uri
	 *            the URI to be displayed in the user default browser
	 * @return <code>true</code> if the browse command returned with no error,
	 *         <code>false</code> otherwise
	 * @see Desktop#browse(URI)
	 */
	public static boolean browse_noError(String uri)
	{
		try
		{
			browse(uri);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Launches the default browser to display a URI.
	 * 
	 * @param uri
	 *            the URI to be displayed in the user default browser
	 * @throws IOException
	 *             if an error occurs
	 * @see Desktop#browse(URI)
	 */
	public static void browse(URI uri) throws IOException
	{
		Desktop.getDesktop().browse(uri);
	}
	

	/**
	 * Launches the default browser to display a URI.
	 * 
	 * @param uri
	 *            the URI to be displayed in the user default browser
	 * @return <code>true</code> if the browse command returned with no error,
	 *         <code>false</code> otherwise
	 * @see Desktop#browse(URI)
	 */
	public static boolean browse_noError(URI uri)
	{
		try
		{
			browse(uri);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Launches the associated editor application and opens a file for editing.
	 * 
	 * @param file
	 *            the file to be opened for editing
	 * @throws IOException
	 *             if an error occurs
	 * @see Desktop#edit(File)
	 */
	public static void edit(File file) throws IOException
	{
		Desktop.getDesktop().edit(file);
	}
	

	/**
	 * Launches the associated editor application and opens a file for editing.
	 * 
	 * @param file
	 *            the file to be opened for editing
	 * @return <code>true</code> if the edit command returned with no error,
	 *         <code>false</code> otherwise
	 * @see Desktop#edit(File)
	 */
	public static boolean edit_noError(File file)
	{
		try
		{
			edit(file);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Launches the associated application to open the file.
	 * <p>
	 * If the specified file is a directory, the file manager of the current
	 * platform is launched to open it.
	 * </p>
	 * 
	 * @param file
	 *            the file to be opened with the associated application
	 * @throws IOException
	 *             if an error occurs
	 * @see Desktop#open(File)
	 */
	public static void open(File file) throws IOException
	{
		if(IOUtils.isWindows())
		{
			String fileOpenMethod = System.getProperty("desktop.fileOpenMethod");
			if("RUNDLL".equalsIgnoreCase(fileOpenMethod))
			{
				Runtime.getRuntime().exec(
						"rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
				return;
			}
			else if("CMD".equalsIgnoreCase(fileOpenMethod))
			{
				new ProcessBuilder("cmd","/c",file.getAbsolutePath()).start();
				return;
			}
		}
		
		Desktop.getDesktop().open(file);
	}
	

	/**
	 * Launches the associated application to open the file.
	 * <p>
	 * If the specified file is a directory, the file manager of the current
	 * platform is launched to open it.
	 * </p>
	 * 
	 * @param file
	 *            the file to be opened with the associated application
	 * @return <code>true</code> if the open command returned with no error,
	 *         <code>false</code> otherwise
	 * @see Desktop#open(File)
	 */
	public static boolean open_noError(File file)
	{
		try
		{
			open(file);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Prints a file with the native desktop printing facility, using the
	 * associated application's print command.
	 * 
	 * @param file
	 *            the file to be printed
	 * 
	 * @throws IOException
	 *             if an error occurs
	 * @see Desktop#print(File)
	 */
	public static void print(File file) throws IOException
	{
		Desktop.getDesktop().print(file);
	}
	

	/**
	 * Prints a file with the native desktop printing facility, using the
	 * associated application's print command.
	 * 
	 * @param file
	 *            the file to be printed
	 * @return <code>true</code> if the print command returned with no error,
	 *         <code>false</code> otherwise
	 * @see Desktop#print(File)
	 */
	public static boolean print_noError(File file)
	{
		try
		{
			print(file);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Launches the mail composing window of the user default mail client.
	 * 
	 * @throws IOException
	 *             if an error occurs
	 * @see Desktop#mail()
	 */
	public static void mail() throws IOException
	{
		Desktop.getDesktop().mail();
	}
	

	/**
	 * Launches the mail composing window of the user default mail client.
	 * 
	 * @return <code>true</code> if the mail command returned with no error,
	 *         <code>false</code> otherwise
	 * @see Desktop#mail()
	 */
	public static boolean mail_noError()
	{
		try
		{
			mail();
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Launches the mail composing window of the user default mail client,
	 * filling the specified message fields.
	 * 
	 * @param receiver
	 *            the receiver of the email
	 * @throws IOException
	 *             if an error occurs
	 * @see Desktop#mail(URI)
	 */
	public static void mail(String receiver) throws IOException
	{
		mail(receiver,null);
	}
	

	/**
	 * Launches the mail composing window of the user default mail client,
	 * filling the specified message fields.
	 * 
	 * @param receiver
	 *            the receiver of the email
	 * @return <code>true</code> if the mail command returned with no error,
	 *         <code>false</code> otherwise
	 * @see Desktop#mail(URI)
	 */
	public static boolean mail_noError(String receiver)
	{
		try
		{
			mail(receiver);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Launches the mail composing window of the user default mail client,
	 * filling the specified message fields.
	 * 
	 * @param receiver
	 *            the receiver of the email
	 * @param subject
	 *            the subject of the email, maybe <code>null</code>
	 * @throws IOException
	 *             if an error occurs
	 * @see Desktop#mail(URI)
	 */
	public static void mail(String receiver, String subject) throws IOException
	{
		mail(receiver,subject,null);
	}
	

	/**
	 * Launches the mail composing window of the user default mail client,
	 * filling the specified message fields.
	 * 
	 * @param receiver
	 *            the receiver of the email
	 * @param subject
	 *            the subject of the email, maybe <code>null</code>
	 * @return <code>true</code> if the mail command returned with no error,
	 *         <code>false</code> otherwise
	 * @see Desktop#mail(URI)
	 */
	public static boolean mail_noError(String receiver, String subject)
	{
		try
		{
			mail(receiver,subject);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Launches the mail composing window of the user default mail client,
	 * filling the specified message fields.
	 * 
	 * @param receiver
	 *            the receiver of the email
	 * @param subject
	 *            the subject of the email, maybe <code>null</code>
	 * @param body
	 *            the predefined body of the email, maybe <code>null</code>
	 * @throws IOException
	 *             if an error occurs
	 * @see Desktop#mail(URI)
	 */
	public static void mail(String receiver, String subject, String body) throws IOException
	{
		mail(receiver,subject,body,null,null);
	}
	

	/**
	 * Launches the mail composing window of the user default mail client,
	 * filling the specified message fields.
	 * 
	 * @param receiver
	 *            the receiver of the email
	 * @param subject
	 *            the subject of the email, maybe <code>null</code>
	 * @param body
	 *            the predefined body of the email, maybe <code>null</code>
	 * @return <code>true</code> if the mail command returned with no error,
	 *         <code>false</code> otherwise
	 * @see Desktop#mail(URI)
	 */
	public static boolean mail_noError(String receiver, String subject, String body)
	{
		try
		{
			mail(receiver,subject,body);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Launches the mail composing window of the user default mail client,
	 * filling the specified message fields.
	 * 
	 * @param receiver
	 *            the receiver of the email
	 * @param subject
	 *            the subject of the email, maybe <code>null</code>
	 * @param body
	 *            the predefined body of the email, maybe <code>null</code>
	 * @param cc
	 *            the copy receiver of the email, maybe <code>null</code>
	 * @param bcc
	 *            the blind copy receiver of the email, maybe <code>null</code>
	 * @throws IOException
	 *             if an error occurs
	 * @see Desktop#mail(URI)
	 */
	public static void mail(String receiver, String subject, String body, String cc, String bcc)
			throws IOException
	{
		if(receiver == null)
		{
			throw new IllegalArgumentException("receiver cannot be null");
		}
		
		Map<String, String> params = new LinkedHashMap();
		
		if(subject != null && ((subject = subject.trim()).length() > 0))
		{
			params.put("subject",encodeURI(subject));
		}
		if(body != null && ((body = body.trim()).length() > 0))
		{
			params.put("body",encodeURI(body));
		}
		if(cc != null && ((cc = cc.trim()).length() > 0))
		{
			params.put("cc",cc);
		}
		if(bcc != null && ((bcc = bcc.trim()).length() > 0))
		{
			params.put("bcc",bcc);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("mailto:");
		sb.append(receiver);
		
		if(params.size() > 0)
		{
			boolean first = true;
			for(String key : params.keySet())
			{
				if(first)
				{
					sb.append("?");
					first = false;
				}
				else
				{
					sb.append("&");
				}
				
				sb.append(key);
				sb.append("=");
				sb.append(params.get(key));
			}
		}
		
		try
		{
			mail(new URI(sb.toString()));
		}
		catch(URISyntaxException e)
		{
			// shouldn't happen
			throw new IOException(e);
		}
	}
	

	/**
	 * Launches the mail composing window of the user default mail client,
	 * filling the specified message fields.
	 * 
	 * @param receiver
	 *            the receiver of the email
	 * @param subject
	 *            the subject of the email, maybe <code>null</code>
	 * @param body
	 *            the predefined body of the email, maybe <code>null</code>
	 * @param cc
	 *            the copy receiver of the email, maybe <code>null</code>
	 * @param bcc
	 *            the blind copy receiver of the email, maybe <code>null</code>
	 * @return <code>true</code> if the mail command returned with no error,
	 *         <code>false</code> otherwise
	 * @see Desktop#mail(URI)
	 */
	public static boolean mail_noError(String receiver, String subject, String body, String cc,
			String bcc)
	{
		try
		{
			mail(receiver,subject,body,cc,bcc);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * Launches the mail composing window of the user default mail client,
	 * filling the message fields specified by a mailto: URI.
	 * 
	 * @param mailtoURI
	 *            the specified mailto: URI
	 * @throws IOException
	 *             if an error occurs
	 * @see Desktop#mail(URI)
	 */
	public static void mail(URI mailtoURI) throws IOException
	{
		Desktop.getDesktop().mail(mailtoURI);
	}
	

	/**
	 * Launches the mail composing window of the user default mail client,
	 * filling the message fields specified by a mailto: URI.
	 * 
	 * @param mailtoURI
	 *            the specified mailto: URI
	 * @return <code>true</code> if the mail command returned with no error,
	 *         <code>false</code> otherwise
	 * @see Desktop#mail(URI)
	 */
	public static boolean mail_noError(URI mailtoURI)
	{
		try
		{
			mail(mailtoURI);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	

	/**
	 * encodes a string to a valid uri parameter
	 */
	private static String encodeURI(String str)
	{
		StringBuilder uri = new StringBuilder();
		
		for(char ch : str.toCharArray())
		{
			if((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
					|| "-_.!~*'()\"".indexOf(ch) != -1)
			{
				uri.append(ch);
			}
			else
			{
				uri.append('%');
				String hex = Integer.toHexString((int)ch);
				if(hex.length() == 1)
				{
					uri.append('0');
				}
				uri.append(hex);
			}
		}
		
		return uri.toString();
	}
}
