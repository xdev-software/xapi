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
package xdev.io;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import xdev.lang.LibraryMember;


/**
 * Utility class with static methods for XML handling.
 * <p>
 * To read XML data from a source use the parse(...) methods.<br>
 * To write XML data to a target use the transform(...) methods.
 * 
 * @author XDEV Software
 * 
 * @since 3.1
 */
@LibraryMember
public final class XMLUtils
{
	private XMLUtils()
	{
	}
	

	/**
	 * Parse the content of the given file as an XML document and return a new
	 * DOM {@link Document} object. An <code>IllegalArgumentException</code> is
	 * thrown if the <code>File</code> is <code>null</code> null.
	 * 
	 * @param file
	 *            The file containing the XML to parse.
	 * 
	 * @return A new DOM Document object.
	 * 
	 * @throws IOException
	 *             If any IO errors occur.
	 * @throws SAXException
	 *             If any parse errors occur.
	 * @throws IllegalArgumentException
	 *             When <code>f</code> is <code>null</code>
	 */
	public static Document parse(File file) throws SAXException, IOException
	{
		return createDocumentBuilder().parse(file);
	}
	

	/**
	 * Parse the content of the given input source as an XML document and return
	 * a new DOM {@link Document} object. An
	 * <code>IllegalArgumentException</code> is thrown if the
	 * <code>InputSource</code> is <code>null</code> null.
	 * 
	 * @param is
	 *            InputSource containing the content to be parsed.
	 * 
	 * @return A new DOM Document object.
	 * 
	 * @throws IOException
	 *             If any IO errors occur.
	 * @throws SAXException
	 *             If any parse errors occur.
	 * @throws IllegalArgumentException
	 *             When <code>is</code> is <code>null</code>
	 */
	public static Document parse(InputSource is) throws SAXException, IOException
	{
		return createDocumentBuilder().parse(is);
	}
	

	/**
	 * Parse the content of the given URI as an XML document and return a new
	 * DOM {@link Document} object. An <code>IllegalArgumentException</code> is
	 * thrown if the URI is <code>null</code> null.
	 * 
	 * @param uri
	 *            The location of the content to be parsed.
	 * 
	 * @return A new DOM Document object.
	 * 
	 * @throws IOException
	 *             If any IO errors occur.
	 * @throws SAXException
	 *             If any parse errors occur.
	 * @throws IllegalArgumentException
	 *             When <code>uri</code> is <code>null</code>
	 */
	public static Document parse(String uri) throws SAXException, IOException
	{
		return createDocumentBuilder().parse(uri);
	}
	

	/**
	 * Parse the content of the given <code>InputStream</code> as an XML
	 * document and return a new DOM {@link Document} object. An
	 * <code>IllegalArgumentException</code> is thrown if the
	 * <code>InputStream</code> is null.
	 * 
	 * @param is
	 *            InputStream containing the content to be parsed.
	 * 
	 * @return <code>Document</code> result of parsing the
	 *         <code>InputStream</code>
	 * 
	 * @throws IOException
	 *             If any IO errors occur.
	 * @throws SAXException
	 *             If any parse errors occur.
	 * @throws IllegalArgumentException
	 *             When <code>is</code> is <code>null</code>
	 */
	public static Document parse(InputStream is) throws SAXException, IOException
	{
		return createDocumentBuilder().parse(is);
	}
	

	/**
	 * Parse the content of the given <code>InputStream</code> as an XML
	 * document and return a new DOM {@link Document} object. An
	 * <code>IllegalArgumentException</code> is thrown if the
	 * <code>InputStream</code> is null.
	 * 
	 * @param is
	 *            InputStream containing the content to be parsed.
	 * @param systemId
	 *            Provide a base for resolving relative URIs.
	 * 
	 * @return A new DOM Document object.
	 * 
	 * @throws IOException
	 *             If any IO errors occur.
	 * @throws SAXException
	 *             If any parse errors occur.
	 * @throws IllegalArgumentException
	 *             When <code>is</code> is <code>null</code>
	 */
	public static Document parse(InputStream is, String systemId) throws SAXException, IOException
	{
		return createDocumentBuilder().parse(is,systemId);
	}
	

	/**
	 * Creates a new instance of a javax.xml.parsers.DocumentBuilder using the
	 * currently configured parameters.
	 */
	private static DocumentBuilder createDocumentBuilder() throws IOException
	{
		try
		{
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch(ParserConfigurationException e)
		{
			throw new IOException(e);
		}
	}
	

	/**
	 * Parse the content of the file specified as XML using the specified
	 * {@link DefaultHandler}.
	 * 
	 * @param f
	 *            The file containing the XML to parse
	 * @param handler
	 *            The SAX DefaultHandler to use.
	 * 
	 * @throws IllegalArgumentException
	 *             If the File object is null.
	 * @throws IOException
	 *             If any IO errors occur.
	 * @throws SAXException
	 *             If any SAX errors occur during processing.
	 */
	public static void parse(File f, DefaultHandler handler) throws SAXException, IOException
	{
		createSAXParser().parse(f,handler);
	}
	

	/**
	 * Parse the content given {@link org.xml.sax.InputSource} as XML using the
	 * specified {@link DefaultHandler}.
	 * 
	 * @param is
	 *            The InputSource containing the content to be parsed.
	 * @param handler
	 *            The SAX DefaultHandler to use.
	 * 
	 * @throws IllegalArgumentException
	 *             If the <code>InputSource</code> object is <code>null</code>.
	 * @throws IOException
	 *             If any IO errors occur.
	 * @throws SAXException
	 *             If any SAX errors occur during processing.
	 */
	public static void parse(InputSource is, DefaultHandler handler) throws SAXException,
			IOException
	{
		createSAXParser().parse(is,handler);
	}
	

	/**
	 * Parse the content described by the giving Uniform Resource Identifier
	 * (URI) as XML using the specified {@link DefaultHandler}.
	 * 
	 * @param uri
	 *            The location of the content to be parsed.
	 * @param handler
	 *            The SAX DefaultHandler to use.
	 * 
	 * @throws IllegalArgumentException
	 *             If the uri is null.
	 * @throws IOException
	 *             If any IO errors occur.
	 * @throws SAXException
	 *             If any SAX errors occur during processing.
	 */
	public static void parse(String uri, DefaultHandler handler) throws SAXException, IOException
	{
		createSAXParser().parse(uri,handler);
	}
	

	/**
	 * Parse the content of the given {@link java.io.InputStream} instance as
	 * XML using the specified {@link DefaultHandler}.
	 * 
	 * @param is
	 *            InputStream containing the content to be parsed.
	 * @param handler
	 *            The SAX DefaultHandler to use.
	 * 
	 * @throws IllegalArgumentException
	 *             If the given InputStream is null.
	 * @throws IOException
	 *             If any IO errors occur.
	 * @throws SAXException
	 *             If any SAX errors occur during processing.
	 */
	public static void parse(InputStream is, DefaultHandler handler) throws SAXException,
			IOException
	{
		createSAXParser().parse(is,handler);
	}
	

	/**
	 * Parse the content of the given {@link java.io.InputStream} instance as
	 * XML using the specified {@link DefaultHandler}.
	 * 
	 * @param is
	 *            InputStream containing the content to be parsed.
	 * @param handler
	 *            The SAX DefaultHandler to use.
	 * @param systemId
	 *            The systemId which is needed for resolving relative URIs.
	 * 
	 * @throws IllegalArgumentException
	 *             If the given InputStream is null.
	 * @throws IOException
	 *             If any IO errors occur.
	 * @throws SAXException
	 *             If any SAX errors occur during processing.
	 */
	public static void parse(InputStream is, String systemId, DefaultHandler handler)
			throws SAXException, IOException
	{
		createSAXParser().parse(is,handler,systemId);
	}
	

	/**
	 * Creates a new instance of a SAXParser using the currently configured
	 * factory parameters.
	 */
	private static SAXParser createSAXParser() throws IOException, SAXException
	{
		try
		{
			return SAXParserFactory.newInstance().newSAXParser();
		}
		catch(ParserConfigurationException e)
		{
			throw new IOException(e);
		}
	}
	

	/**
	 * Transform the document to a file.
	 * 
	 * @param doc
	 *            The document transform.
	 * @param file
	 *            The target to transform the document to
	 * 
	 * @throws TransformerException
	 *             If an unrecoverable error occurs during the course of the
	 *             transformation.
	 */
	public static void transform(Document doc, File file) throws TransformerException
	{
		Source source = new DOMSource(doc);
		Result result = new StreamResult(file);
		createTransformer().transform(source,result);
	}
	

	/**
	 * Transform the document to a writer.
	 * 
	 * @param doc
	 *            The document transform.
	 * @param writer
	 *            The target to transform the document to
	 * 
	 * @throws TransformerException
	 *             If an unrecoverable error occurs during the course of the
	 *             transformation.
	 */
	public static void transform(Document doc, Writer writer) throws TransformerException
	{
		Source source = new DOMSource(doc);
		Result result = new StreamResult(writer);
		createTransformer().transform(source,result);
	}
	

	/**
	 * Transform the document to an output stream.
	 * 
	 * @param doc
	 *            The document transform.
	 * @param os
	 *            The target to transform the document to
	 * 
	 * @throws TransformerException
	 *             If an unrecoverable error occurs during the course of the
	 *             transformation.
	 */
	public static void transform(Document doc, OutputStream os) throws TransformerException
	{
		Source source = new DOMSource(doc);
		Result result = new StreamResult(os);
		createTransformer().transform(source,result);
	}
	

	/**
	 * Transform the document to an {@link URI}.
	 * 
	 * @param doc
	 *            The document transform.
	 * @param uri
	 *            Must be a String that conforms to the URI syntax
	 * 
	 * @throws TransformerException
	 *             If an unrecoverable error occurs during the course of the
	 *             transformation.
	 */
	public static void transform(Document doc, String uri) throws TransformerException
	{
		Source source = new DOMSource(doc);
		Result result = new StreamResult(uri);
		createTransformer().transform(source,result);
	}
	

	/**
	 * Create a new <code>Transformer</code> that performs a copy of the
	 * <code>Source</code> to the <code>Result</code>. i.e. the "
	 * <em>identity transform</em>".
	 */
	private static Transformer createTransformer() throws TransformerException
	{
		try
		{
			return TransformerFactory.newInstance().newTransformer();
		}
		catch(TransformerConfigurationException e)
		{
			throw new TransformerException(e);
		}
		catch(TransformerFactoryConfigurationError e)
		{
			throw new TransformerException(e);
		}
	}
}
