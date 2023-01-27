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
/**
 * 
 */

package xdev.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import xdev.io.IOUtils;
import xdev.test.UITest;


/**
 * @author XDEV Software (FHAE)
 * 
 */
public class GraphicUtilsTest
{
	// Root
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	/**
	 * Root path relative of the TestPicture2.JPG
	 */
	public static final String ROOT_PATH_RELATIVE = "TestPicture2.JPG";
	public static ImageIcon ORIGINAL_IMAGEICON;
	
	@BeforeClass
	public static void setup() throws IOException
	{
		ORIGINAL_IMAGEICON = new ImageIcon(IOUtils.readData(GraphicUtilsTest.class.getResourceAsStream("/TestImage.PNG")));
	}
	
	
	/**
	 * Test method for {@link xdev.ui.GraphicUtils#create2D(java.awt.Graphics)}.
	 */
	@Test
	public void testCreate2D()
	{
		// can not be tested
	}
	
	/**
	 * Test method for {@link xdev.ui.GraphicUtils#getImageScaleMode()}.
	 */
	@Test
	public void testGetImageScaleMode()
	{
		// Set the property to check the parameter
		System.setProperty("xdev.renderMode", "speed");
		
		final int actualScaleModeFAST = GraphicUtils.getImageScaleMode();
		assertEquals(Image.SCALE_FAST, actualScaleModeFAST);
		
		// Set the property to check the parameter
		System.setProperty("xdev.renderMode", "quality");
		
		final int actualScaleModeSMOOTH = GraphicUtils.getImageScaleMode();
		assertEquals(Image.SCALE_SMOOTH, actualScaleModeSMOOTH);
		
	}
	
	/**
	 * Test method for {@link xdev.ui.GraphicUtils#getInternalFrameDragMode()}.
	 */
	@Test
	public void testGetInternalFrameDragMode()
	{
		
		// Set the property to check the parameter
		System.setProperty("xdev.renderMode", "quality");
		
		final int actualInternalFrameDragModeLIVE = GraphicUtils.getInternalFrameDragMode();
		assertEquals(JDesktopPane.LIVE_DRAG_MODE, actualInternalFrameDragModeLIVE);
		
		// Set the property to check the parameter
		System.setProperty("xdev.renderMode", "speed");
		
		final int actualInternalFrameDragModeOUTLINE = GraphicUtils.getInternalFrameDragMode();
		assertEquals(JDesktopPane.OUTLINE_DRAG_MODE, actualInternalFrameDragModeOUTLINE);
		
	}
	
	/**
	 * Test method for
	 * {@link xdev.ui.GraphicUtils#loadImagePlain(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test
	@Category(UITest.class)
	public void testLoadImagePlainString() throws IOException
	{
		final Image img = GraphicUtils.loadImagePlain(this.ROOT_PATH_RELATIVE);
		assertNotNull(img);
		
	}
	
	/**
	 * Test method for
	 * {@link xdev.ui.GraphicUtils#loadImagePlain(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	@Category(UITest.class)
	public void testLoadImagePlainStringIOException() throws IOException
	{
		GraphicUtils.loadImagePlain(this.ROOT_PATH_RELATIVE + "1");
		
	}
	
	/**
	 * Test method for
	 * {@link xdev.ui.GraphicUtils#loadImagePlain(java.lang.String, java.awt.Component)}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void testLoadImagePlainStringComponent() throws IOException
	{
		final Component c = new XdevButton();
		
		final Image img = GraphicUtils.loadImagePlain(this.ROOT_PATH_RELATIVE, c);
		assertNotNull(img);
	}
	
	/**
	 * Test method for {@link xdev.ui.GraphicUtils#loadImage(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test
	@Category(UITest.class)
	public void testLoadImageString() throws IOException
	{
		final BufferedImage bimg = GraphicUtils.loadImage(this.ROOT_PATH_RELATIVE);
		
		assertNotNull(bimg);
	}
	
	/**
	 * Test method for {@link xdev.ui.GraphicUtils#loadImage(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	@Category(UITest.class)
	public void testLoadImageStringIOException() throws IOException
	{
		GraphicUtils.loadImage(this.ROOT_PATH_RELATIVE + "1");
	}
	
	/**
	 * Test method for
	 * {@link xdev.ui.GraphicUtils#loadImage(java.lang.String, java.awt.Component)}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	@Category(UITest.class)
	public void testLoadImageStringComponent() throws IOException
	{
		final XdevButton b = new XdevButton();
		final BufferedImage bimg = GraphicUtils.loadImage(this.ROOT_PATH_RELATIVE, b);
		
		assertNotNull(bimg);
	}
	
	/**
	 * Test method for {@link xdev.ui.GraphicUtils#loadIcon(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test
	@Category(UITest.class)
	public void testLoadIcon() throws IOException
	{
		final Icon i = GraphicUtils.loadIcon(this.ROOT_PATH_RELATIVE);
		assertNotNull(i);
	}
	
	/**
	 * Test method for {@link xdev.ui.GraphicUtils#loadIcon(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	@Category(UITest.class)
	public void testLoadIconIOException() throws IOException
	{
		GraphicUtils.loadIcon(this.ROOT_PATH_RELATIVE + "1");
	}
	
	/**
	 * Test method for
	 * {@link xdev.ui.GraphicUtils#createBufferedImage(java.awt.Image)}.
	 */
	@Test
	@Category(UITest.class)
	public void testCreateBufferedImageImage()
	{
		final BufferedImage biTranslucent = GraphicUtils.createBufferedImage(this.ORIGINAL_IMAGEICON.getImage());
		assertNotNull(biTranslucent);
	}
	
	/**
	 * Test method for
	 * {@link xdev.ui.GraphicUtils#createBufferedImage(java.awt.Image, boolean)}
	 * .
	 */
	@Test
	@Category(UITest.class)
	public void testCreateBufferedImageImageBoolean()
	{
		final BufferedImage biTranslucent = GraphicUtils.createBufferedImage(
			this.ORIGINAL_IMAGEICON.getImage(),
			true);
		
		final BufferedImage biNOTTranslucent = GraphicUtils.createBufferedImage(
			this.ORIGINAL_IMAGEICON.getImage(),
			false);
		
		assertNotNull(biTranslucent);
		assertNotSame(biTranslucent.getColorModel(), biNOTTranslucent.getColorModel());
		
	}
	
	/**
	 * Test method for
	 * {@link xdev.ui.GraphicUtils#createImageFromIcon(javax.swing.Icon)}.
	 */
	@Test
	public void testCreateImageFromIcon()
	{
		final Image actual = GraphicUtils.createImageFromIcon(this.ORIGINAL_IMAGEICON);
		assertNotNull(actual);
		assertEquals(this.ORIGINAL_IMAGEICON.getImage(), actual);
	}
	
	/**
	 * Test method for
	 * {@link xdev.ui.GraphicUtils#resize(java.awt.Image, int, int)}.
	 */
	@Test
	public void testResize()
	{
		final Image orgImg = this.ORIGINAL_IMAGEICON.getImage();
		
		final int originalWidth = this.ORIGINAL_IMAGEICON.getIconWidth();
		final int originalHeight = this.ORIGINAL_IMAGEICON.getIconHeight();
		final int plus = 100;
		
		final Image img = GraphicUtils.resize(orgImg, originalWidth + plus, originalHeight + plus);
		
		assertNotNull(img);
		assertEquals(orgImg.getHeight(null) + plus, img.getHeight(null));
		assertEquals(orgImg.getWidth(null) + plus, img.getWidth(null));
		
	}
	
	/**
	 * Test method for
	 * {@link xdev.ui.GraphicUtils#resizeProportional(java.awt.Image, int, int)}
	 * .
	 */
	@Test
	public void testResizeProportional()
	{
		final Image orgImg = this.ORIGINAL_IMAGEICON.getImage();
		
		final int originalWidth = this.ORIGINAL_IMAGEICON.getIconWidth();
		final int originalHeight = this.ORIGINAL_IMAGEICON.getIconHeight();
		final int plus = 100;
		
		final Image img = GraphicUtils.resizeProportional(orgImg, originalWidth + plus, originalHeight + plus);
		
		assertNotNull(img);
		assertEquals(112, orgImg.getHeight(null));
		assertEquals(484, img.getWidth(null) + 100);
	}
	
}
