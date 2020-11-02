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


import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import xdev.Application;


/**
 * A {@link XdevRootPaneContainer} which is used as the {@link Application}'s
 * top level container, which is either a Frame or a Applet.
 * 
 * @see XdevMainFrame
 * @see XdevFullScreen
 * @see Application
 * 
 * @author XDEV Software
 * 
 */
public interface XdevApplicationContainer extends XdevRootPaneContainer
{
	/**
	 * Sets the icon image of this {@link XdevApplicationContainer}.
	 * 
	 * @param img
	 *            the image to be displayed
	 */
	public void setIconImage(Image img);
	

	/**
	 * Gets the base URL. This is the URL of the directory which contains this
	 * XdevApplicationContainer.
	 * 
	 * @return the base {@link java.net.URL} of the directory which contains
	 *         this XdevApplicationContainer.
	 * 
	 */
	public URL getCodeBase();
	

	/**
	 * Returns an <code>Image</code> object that can then be painted on the
	 * screen. The <code>url</code> that is passed as an argument must specify
	 * an absolute URL.
	 * <p>
	 * This method always returns immediately, whether or not the image exists.
	 * When this XdevApplicationContainer attempts to draw the image on the
	 * screen, the data will be loaded. The graphics primitives that draw the
	 * image will incrementally paint on the screen.
	 * 
	 * @param url
	 *            an absolute URL giving the location of the image.
	 * 
	 * @return the image at the specified URL.
	 * 
	 * @see java.awt.Image
	 */
	public Image getImage(URL url);
	

	/**
	 * Gets the toolkit of this XdevApplicationContainer. Note that the frame
	 * that contains a component controls which toolkit is used by that
	 * component. Therefore if the component is moved from one frame to another,
	 * the toolkit it uses may change.
	 * 
	 * @return the toolkit of this <code>XdevApplicationContainer</code>
	 * 
	 */
	public Toolkit getToolkit();
	

	/**
	 * Gets the font metrics for the specified font. Warning: Since Font metrics
	 * are affected by the {@link java.awt.font.FontRenderContext
	 * FontRenderContext} and this method does not provide one, it can return
	 * only metrics for the default render context which may not match that used
	 * when rendering on the Component if {@link Graphics2D} functionality is
	 * being used. Instead metrics can be obtained at rendering time by calling
	 * {@link Graphics#getFontMetrics()} or text measurement APIs on the
	 * {@link Font Font} class.
	 * 
	 * @param f
	 *            the font for which font metrics is to be obtained
	 * 
	 * @return the font metrics for <code>font</code>
	 */
	public FontMetrics getFontMetrics(Font f);
	

	/**
	 * Creates a graphics context for this <code>XdevApplicationContainer</code>
	 * . This method will return <code>null</code> if this
	 * <code>XdevApplicationContainer</code> is currently not displayable.
	 * 
	 * @return a graphics context for this XdevApplicationContainer, or
	 *         <code>null</code> if it has none
	 * 
	 * @see #getGraphicsConfiguration()
	 */
	public Graphics getGraphics();
	

	/**
	 * Gets the <code>GraphicsConfiguration</code> associated with this
	 * <code>XdevApplicationContainer</code>. If the
	 * <code>XdevApplicationContainer</code> has not been assigned a specific
	 * <code>GraphicsConfiguration</code>, the
	 * <code>GraphicsConfiguration</code> of the
	 * <code>XdevApplicationContainer</code> object's top-level container is
	 * returned. If the <code>XdevApplicationContainer</code> has been created,
	 * but not yet added to a <code>Container</code>, this method returns
	 * <code>null</code>.
	 * 
	 * @return the <code>GraphicsConfiguration</code> used by this
	 *         <code>XdevApplicationContainer</code> or <code>null</code>
	 * 
	 * @see #getGraphics()
	 */
	public GraphicsConfiguration getGraphicsConfiguration();
	

	/**
	 * Requests that the application container shows the Web page indicated by
	 * the <code>url</code> argument. The browser or applet viewer determines
	 * which window or frame to display the Web page.
	 * 
	 * @param url
	 *            an absolute URL giving the location of the document.
	 */
	public void showDocument(URL url);
	

	/**
	 * Requests that the application container shows the Web page indicated by
	 * the <code>url</code> argument. The <code>target</code> argument indicates
	 * in which HTML frame the document is to be displayed. The target argument
	 * is interpreted as follows:
	 * <p>
	 * 
	 * <table border="3" summary="Target arguments and their descriptions">
	 * <tr>
	 * <th>Target Argument</th>
	 * <th>Description</th>
	 * </tr>
	 * <tr>
	 * <td><code>"_self"</code>
	 * <td>Show in the window and frame that contain the applet.
	 * </tr>
	 * <tr>
	 * <td><code>"_parent"</code>
	 * <td>Show in the applet's parent frame. If the applet's frame has no
	 * parent frame, acts the same as "_self".
	 * </tr>
	 * <tr>
	 * <td><code>"_top"</code>
	 * <td>Show in the top-level frame of the applet's window. If the applet's
	 * frame is the top-level frame, acts the same as "_self".
	 * </tr>
	 * <tr>
	 * <td><code>"_blank"</code>
	 * <td>Show in a new, unnamed top-level window.
	 * </tr>
	 * <tr>
	 * <td><i>name</i>
	 * <td>Show in the frame or window named <i>name</i>. If a target named
	 * <i>name</i> does not already exist, a new top-level window with the
	 * specified name is created, and the document is shown there.
	 * </tr>
	 * </table>
	 * 
	 * 
	 * <p>
	 * An applet viewer or browser is free to ignore this method.
	 * 
	 * @param url
	 *            an absolute URL giving the location of the document.
	 * @param target
	 *            a <code>String</code> indicating where to display the page.
	 */
	public void showDocument(URL url, String target);
}
