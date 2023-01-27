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
package xdev.net;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import xdev.Application;
import xdev.io.IOUtils;
import xdev.lang.LibraryMember;
import xdev.ui.UIUtils;
import xdev.util.Settings;
import xdev.util.XdevHashtable;


/**
 * Utility class with static methods for net operations.
 * 
 * @author XDEV Software
 * 
 * @since 3.1
 */
@LibraryMember
public final class NetUtils
{
	private NetUtils()
	{
	}
	

	/**
	 * Sends a HTTP-POST request to the <code>targetURL</code> and returns the
	 * resulting connection.
	 * 
	 * @param targetURL
	 *            the target of the request
	 * @param urlParameters
	 *            the post data, e.g: ?name=John&surname=Doe
	 * @param timeout
	 *            timeout in millis
	 * @return the opened connection
	 * @throws IOException
	 *             if the targetURL is invalid or an I/O error occurs
	 * 
	 */
	public static HttpURLConnection sendPost(String targetURL, String urlParameters, int timeout)
			throws IOException
	{
		return sendPost(new URL(targetURL),urlParameters,timeout);
	}
	

	/**
	 * Creates an url encoded representation of the specified string. For
	 * example the String
	 * 
	 * <pre>
	 * &quot;Cheap stuff, only $1.50&quot;
	 * </pre>
	 * 
	 * gets encoded to
	 * 
	 * <pre>
	 * &quot;Cheap+stuff%2C+only+%241.50&quot;
	 * </pre>
	 * 
	 * @param s
	 *            the string to encode
	 * @return the encoded string
	 * 
	 * @see #decodeURLString(String)
	 */
	public static String encodeURLString(String s)
	{
		try
		{
			return URLEncoder.encode(s,"UTF-8");
		}
		catch(Exception e)
		{
			return s;
		}
	}
	

	/**
	 * Decodes an url encoded string.
	 * 
	 * <pre>
	 * &quot;Cheap+stuff%2C+only+%241.50&quot;
	 * </pre>
	 * 
	 * gets decoded to
	 * 
	 * <pre>
	 * &quot;Cheap stuff, only $1.50&quot;
	 * </pre>
	 * 
	 * @param s
	 *            the string to decode
	 * @return the decoded string
	 * 
	 * @see #encodeURLString(String)
	 */
	public static String decodeURLString(String s)
	{
		try
		{
			return URLDecoder.decode(s,"UTF-8");
		}
		catch(Exception e)
		{
			return s;
		}
	}
	
	/**
	 * 
	 */
	private static String	sessionID;
	

	/**
	 * Returns a session id for this application.<br>
	 * If no session id exists, a new session id is created.
	 * 
	 * @return the session id as a {@link String}
	 */
	public static synchronized String getSessionID()
	{
		if(sessionID == null)
		{
			renewSessionID();
		}
		
		return sessionID;
	}
	

	/**
	 * Renews the session id. <br>
	 * If the application is an applet, the sessid id will be retrieved as an
	 * applet parameter. Otherwise a new session id will be created.
	 */
	public static synchronized void renewSessionID()
	{
		if(Application.isApplet())
		{
			sessionID = UIUtils.getApplet().getParameter("sessionID");
		}
		else
		{
			sessionID = Long.toHexString(Math.round(Math.random() * Long.MAX_VALUE));
		}
	}
	

	/**
	 * Sets a cookie value.
	 * <p>
	 * <strong> This method works only if {@link Application#getType()} is
	 * {@link Application#APPLET}. </strong>
	 * 
	 * @param name
	 *            the name of the cookie
	 * @param value
	 *            the value of the cookie
	 * @return <code>true</code> if the operation was successful,
	 *         <code>false</code> otherwise
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws UnsupportedOperationException
	 *             if {@link Application#getType()} is not
	 *             {@link Application#APPLET}
	 */
	public static boolean setCookie(String name, String value) throws IOException,
			UnsupportedOperationException
	{
		ensureApplet();
		
		String answer = sendSXAPIRequest(new XdevHashtable<String, String>("action","setCookie",
				"name",name,"value",value));
		return "1".equals(answer);
	}
	

	/**
	 * Gets a cookie value.
	 * <p>
	 * <strong> This method works only if {@link Application#getType()} is
	 * {@link Application#APPLET}. </strong>
	 * 
	 * @param name
	 *            the name of the cookie
	 * @return the value of the cookie
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws UnsupportedOperationException
	 *             if {@link Application#getType()} is not
	 *             {@link Application#APPLET}
	 */
	public static String getCookie(String name) throws IOException, UnsupportedOperationException
	{
		ensureApplet();
		
		return sendSXAPIRequest(new XdevHashtable<String, String>("action","getCookie","name",name));
	}
	

	/**
	 * Sends an email with the specified parameters.
	 * <p>
	 * Alias for method
	 * <code>mail(String receiver, String subject, String message, String additionalHeaders)</code>
	 * <p>
	 * <strong> This method works only if {@link Application#getType()} is
	 * {@link Application#APPLET}. </strong>
	 * 
	 * @param receiver
	 *            the mail receiver
	 * @param subject
	 *            the mail subject
	 * @param message
	 *            the message body of the mail
	 * @return true, if successful
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws UnsupportedOperationException
	 *             if {@link Application#getType()} is not
	 *             {@link Application#APPLET}
	 */
	public static boolean sendMail(String receiver, String subject, String message)
			throws IOException, UnsupportedOperationException
	{
		return sendMail(receiver,subject,message,null);
	}
	

	/**
	 * Sends a email with the specified parameters.
	 * <p>
	 * <strong> This method works only if {@link Application#getType()} is
	 * {@link Application#APPLET}. </strong>
	 * 
	 * @param receiver
	 *            the mail receiver
	 * @param subject
	 *            the mail subject
	 * @param message
	 *            the message body of the mail
	 * @param additionalHeaders
	 *            a {@link String} containing additional mail headers
	 * @return true, if successful
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws UnsupportedOperationException
	 *             if {@link Application#getType()} is not
	 *             {@link Application#APPLET}
	 */
	public static boolean sendMail(String receiver, String subject, String message,
			String additionalHeaders) throws IOException, UnsupportedOperationException
	{
		ensureApplet();
		
		XdevHashtable<String, String> params = new XdevHashtable<String, String>("action",
				"sendMail","receiver",receiver,"subject",subject,"message",message);
		if(additionalHeaders != null)
		{
			params.put("additionalHeaders",additionalHeaders);
		}
		
		String answer = sendSXAPIRequest(params);
		return "1".equals(answer);
	}
	

	/**
	 * Uploads a file via a HTML form.
	 * <p>
	 * <strong> This method works only if {@link Application#getType()} is
	 * {@link Application#APPLET}. </strong>
	 * 
	 * @param destFolder
	 *            the destination folder on the server
	 * @param destFileName
	 *            the destination file name on the server
	 * @param maxSize
	 *            the file's maximum size, <=0 for no maximum
	 * @param dialogTitle
	 *            the upload dialog's title
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws UnsupportedOperationException
	 *             if {@link Application#getType()} is not
	 *             {@link Application#APPLET}
	 */
	public static void uploadFile(String destFolder, String destFileName, long maxSize,
			String dialogTitle) throws IOException, UnsupportedOperationException
	{
		ensureApplet();
		
		if(destFileName == null)
		{
			destFileName = "";
		}
		
		String maxSizeStr = "";
		if(maxSize > 0)
		{
			maxSizeStr = String.valueOf(maxSize);
		}
		
		XdevHashtable<String, String> params = new XdevHashtable<String, String>("action",
				"uploadPrepare","destFolder",destFolder,"destFile",destFileName,"maxsize",
				maxSizeStr,"title",dialogTitle);
		
		Application.getContainer()
				.showDocument(getSXAPI_URL(createURLParameters(params)),"control");
	}
	

	private static void ensureApplet() throws UnsupportedOperationException
	{
		if(!Application.isApplet())
		{
			throw new UnsupportedOperationException("The application must be running as an applet");
		}
	}
	

	private static String sendSXAPIRequest(Map<String, String> params) throws IOException
	{
		try
		{
			URLConnection con = sendPost(getSXAPI_URL(null),createURLParameters(params),0);
			InputStream in = con.getInputStream();
			return IOUtils.readString(in,"ISO-8859-1");
		}
		catch(IOException e)
		{
			throw new IOException(e);
		}
	}
	

	private static URL getSXAPI_URL(String parameters) throws IOException
	{
		String spec = "xdev." + Settings.getServerSideSuffix();
		if(parameters != null)
		{
			spec = spec + parameters;
		}
		return new URL(Application.getContainer().getCodeBase(),spec);
	}
	

	private static String createURLParameters(Map<String, String> params)
	{
		StringBuilder sb = new StringBuilder();
		for(String key : params.keySet())
		{
			sb.append(sb.length() == 0 ? '?' : '&');
			sb.append(key);
			sb.append('=');
			sb.append(encodeURLString(params.get(key)));
		}
		return sb.toString();
	}
	

	private static HttpURLConnection sendPost(URL url, String urlParameters, int timeout)
			throws IOException
	{
		HttpURLConnection con = null;
		
		byte[] postData = urlParameters.getBytes("UTF-8");
		
		con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		con.setRequestProperty("Content-Length",Integer.toString(postData.length));
		con.setConnectTimeout(timeout);
		con.setReadTimeout(timeout);
		con.setUseCaches(false);
		con.setDoInput(true);
		con.setDoOutput(true);
		
		OutputStream out = con.getOutputStream();
		out.write(postData);
		out.flush();
		out.close();
		
		return con;
	}
}
