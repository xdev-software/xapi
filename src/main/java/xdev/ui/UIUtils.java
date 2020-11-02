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


import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.Collection;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.FocusManager;
import javax.swing.Icon;
import javax.swing.JApplet;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import javafx.application.Platform;
import xdev.Application;
import xdev.io.XdevFile;
import xdev.lang.LibraryMember;
import xdev.lang.NotNull;
import xdev.lang.Nullable;
import xdev.ui.tree.TreeUtils;
import xdev.util.XdevList;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


/**
 * <p>
 * The <code>UIUtils</code> class provides utility methods for dialog, window
 * and general UI (user interface) handling.
 * </p>
 *
 * @since 2.0
 *
 * @author XDEV Software
 */
@LibraryMember
public final class UIUtils implements ClientProperties
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger log = LoggerFactory.getLogger(UIUtils.class);


	/**
	 * <p>
	 * <code>UIUtils</code> class can not be instantiated. The class should be
	 * used as utility class: <code>UIUtils.createFrame(myContainer);</code>.
	 * </p>
	 */
	private UIUtils()
	{
	}

	/**
	 * JavaDoc omitted for private field.
	 */
	private static JFileChooser								sharedFileChooser	= null;
	/**
	 * JavaDoc omitted for private field.
	 */
	private static JFileChooser								sharedFolderChooser	= null;
	/**
	 * JavaDoc omitted for private field.
	 */
	private static JFileChooser								fileChooser;
	/**
	 * JavaDoc omitted for private field.
	 */
	private static XdevFileFilter							fileFilter;
	/**
	 * JavaDoc omitted for private field.
	 */
	private final static Map<RenderingHints.Key, Object>	hintsQuality		= new Hashtable<RenderingHints.Key, Object>();
	/**
	 * JavaDoc omitted for private field.
	 */
	private final static Map<RenderingHints.Key, Object>	hintsSpeed			= new Hashtable<RenderingHints.Key, Object>();

	static
	{
		hintsQuality.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		hintsQuality.put(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		hintsQuality.put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		hintsQuality.put(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		hintsQuality.put(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_OFF);

		hintsSpeed.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
		hintsSpeed.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
		hintsSpeed.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
		hintsSpeed.put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		hintsSpeed.put(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		hintsSpeed.put(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
	}


	/**
	 * Set rendering hints to <strong>speed</strong> on the provided
	 * {@link Graphics2D} <code>g</code>.
	 *
	 * @param g
	 *            {@link Graphics2D} to set the rendering hints on.
	 *
	 * @throws NullPointerException
	 *             if provided {@link Graphics2D} <code>g</code> is
	 *             <code>null</code>.
	 */
	public static void setSpeedHints(@NotNull final Graphics2D g) throws NullPointerException
	{
		g.setRenderingHints(hintsSpeed);
	}


	/**
	 * Set rendering hints to <strong>quality</strong> on the provided
	 * {@link Graphics2D} <code>g</code>.
	 *
	 * @param g
	 *            {@link Graphics2D} to set the rendering hints on.
	 *
	 *
	 * @throws NullPointerException
	 *             if provided {@link Graphics2D} <code>g</code> is
	 *             <code>null</code>.
	 */
	public static void setQualityHints(@NotNull final Graphics2D g) throws NullPointerException
	{
		g.setRenderingHints(hintsQuality);
	}


	/**
	 * Calls {@link GraphicUtils#loadIcon(String)} and catches the
	 * {@link IOException}.
	 *
	 * @param path
	 *            the relative path of the icon, e.g. 'res/image/splash.png'
	 * @return The loaded icon or <code>null</code> if an {@link IOException}
	 *         occurs
	 */
	public static Icon loadIcon(@Nullable final String path)
	{
		try
		{
			return GraphicUtils.loadIcon(path);
		}
		catch(final IOException e)
		{
			log.error(e);
			return null;
		}
	}


	/**
	 * package private util method
	 */
	static Dimension getPrefSizeFieldValue(@NotNull final Component c) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		final Field prefSize = Component.class.getDeclaredField("prefSize");
		final boolean accessible = prefSize.isAccessible();
		prefSize.setAccessible(true);
		final Dimension d = (Dimension)prefSize.get(c);
		prefSize.setAccessible(accessible);
		return new Dimension(d);
	}


	/**
	 * Return the {@link XdevWindow} on which the provided {@link Component}
	 * <code>component</code> is located on. If the provided {@link Component}
	 * <code>component</code> is not placed on a {@link XdevWindow}
	 * <code>null</code> will be returned.
	 *
	 * @param component
	 *            {@link Component} to get the {@link XdevWindow} for.
	 * @return the {@link XdevWindow} on which the provided {@link Component}
	 *         <code>component</code> is located on. If the provided
	 *         {@link Component} <code>component</code> is not placed on a
	 *         {@link XdevWindow} <code>null</code> will be returned.
	 */
	public static XdevWindow getXdevWindowOf(@NotNull final Component component)
	{
		return getParentOfClass(XdevWindow.class,component);
	}


	/**
	 * Convenience method for searching above <code>cpn</code> in the component
	 * hierarchy and returns the first object of class <code>c</code> it finds.
	 * Can return {@code null}, if a class <code>c</code> cannot be found.
	 *
	 * @param c
	 *            The type to search for
	 * @param cpn
	 *            The starting point in the component hierarchy
	 * @return The owner of <code>cpn</code> or <code>null</code> if no owner of
	 *         type <code>c</code> could be found
	 */

	public static <T> T getParentOfClass(final Class<T> c, Component cpn)
	{
		if(c == null)
		{
			return null;
		}

		while(cpn != null)
		{
			if(c.isInstance(cpn))
			{
				return (T)cpn;
			}

			if(cpn instanceof JPopupMenu)
			{
				cpn = ((JPopupMenu)cpn).getInvoker();
			}
			else
			{
				cpn = cpn.getParent();
			}
		}

		return null;
	}


	/**
	 * Returns the shared {@link JFileChooser}.
	 *
	 * <p>
	 * <strong>Hint:</strong> Does not show the {@link JFileChooser}. To show
	 * the {@link JFileChooser} use {@link #showFileChooser(String, Collection)}
	 * .
	 * </p>
	 *
	 * @return the shared {@link JFileChooser}.
	 */
	public static JFileChooser getSharedFileChooser()
	{
		if(UIUtils.sharedFileChooser == null)
		{
			UIUtils.sharedFileChooser = new JFileChooser();
		}

		return UIUtils.sharedFileChooser;
	}


	/**
	 * Returns a {@link JFileChooser} configured to chose folders.
	 * <p>
	 * <strong>Hint:</strong> Does not show the {@link JFileChooser}. To show
	 * the {@link JFileChooser} use {@link #showFolderChooser(String)}.
	 * </p>
	 *
	 * @return a {@link JFileChooser} configured to chose folders.
	 */
	public static JFileChooser createFolderChooser()
	{
		if(sharedFolderChooser == null)
		{
			sharedFolderChooser = new JFileChooser();
		}

		sharedFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		return sharedFolderChooser;
	}


	/**
	 * Shows the <i>system file chooser dialog</i> to select a folder.
	 *
	 * <p>
	 * The chooser starts the last use folder.
	 * </p>
	 *
	 * @return the chosen folder; <code>null</code> if the user closes the
	 *         dialog or does not select a folder.
	 *
	 * @since 4.0
	 */
	public static XdevFile showFolderChooser()
	{
		return showFolderChooser(null);
	}


	/**
	 * Shows the <i>system file chooser dialog</i> to select a folder.
	 *
	 * <p>
	 * The chooser starts the last use folder.
	 * </p>
	 *
	 * @param okButtonText
	 *            {@link String} to use as text for the <i>ok button</i> of the
	 *            folder chooser. If <code>null</code> the default text is used.
	 * @return the chosen folder; <code>null</code> if the user closes the
	 *         dialog or does not select a folder.
	 */
	public static XdevFile showFolderChooser(@Nullable final String okButtonText)
	{
		return showFolderChooser(okButtonText,null);
	}


	/**
	 * Shows the <i>system file chooser dialog</i> to select a folder.
	 *
	 * @param okButtonText
	 *            {@link String} to use as text for the <i>ok button</i> of the
	 *            folder chooser. If <code>null</code> the default text is used.
	 * @param initialPath
	 *            {@link String} path to open the file chooser for. If
	 *            <code>null</code> the <i>folder chooser</i> starts in the last
	 *            used folder.
	 * @return the chosen folder; <code>null</code> if the user closes the
	 *         dialog or does not select a folder.
	 */
	public static XdevFile showFolderChooser(@Nullable String okButtonText,
			@Nullable final String initialPath)
	{
		final JFileChooser fc = createFolderChooser();

		if(okButtonText == null || okButtonText.length() == 0)
		{
			okButtonText = UIManager.getString("OptionPane.okButtonText");
		}

		File dir = null;
		if(initialPath != null && initialPath.length() > 0)
		{
			dir = new File(initialPath);
		}

		fc.setCurrentDirectory(dir);

		if(fc.showDialog(getActiveWindow(),okButtonText) == JFileChooser.APPROVE_OPTION)
		{
			final File file = fc.getSelectedFile();
			if(file != null)
			{
				return new XdevFile(file);
			}
		}

		return null;
	}


	/**
	 * Shows the <i>system file chooser dialog</i> to select a file.
	 *
	 * <p>
	 * The chooser starts in the last used folder.
	 * </p>
	 *
	 * <p>
	 * <b> Hint:<br>
	 * For a more sophisticated way to use file choosers see
	 * {@link XdevFileChooserFactory}. </b>
	 * </p>
	 *
	 * @return the chosen folder; <code>null</code> if the user closes the
	 *         dialog or does not select a folder.
	 *
	 * @since 4.0
	 */
	public static XdevFile showFileChooser()
	{
		return showFileChooser(null);
	}


	/**
	 * Shows the <i>system file chooser dialog</i> to select a file.
	 *
	 * <p>
	 * The chooser starts in the last used folder.
	 * </p>
	 *
	 * <p>
	 * <b> Hint:<br>
	 * For a more sophisticated way to use file choosers see
	 * {@link XdevFileChooserFactory}. </b>
	 * </p>
	 *
	 * @param allowedSuffixes
	 *            a {@link Collection} containing the allowed suffixes for files
	 *            this file chooser should display. If <code>null</code> or
	 *            empty (= list with not entries) all files will be displayed.
	 * @return the chosen folder; <code>null</code> if the user closes the
	 *         dialog or does not select a folder.
	 *
	 * @since 4.0
	 */
	public static XdevFile showFileChooser(@Nullable final Collection<?> allowedSuffixes)
	{
		return showFileChooser(null,allowedSuffixes);
	}


	/**
	 * Shows the <i>system file chooser dialog</i> to select a file.
	 *
	 * <p>
	 * The chooser starts in the last used folder.
	 * </p>
	 *
	 * <p>
	 * <b> Hint:<br>
	 * For a more sophisticated way to use file choosers see
	 * {@link XdevFileChooserFactory}. </b>
	 * </p>
	 *
	 * @param okButtonText
	 *            {@link String} to use as text for the <i>ok button</i> of the
	 *            file chooser. If <code>null</code> the default text is used.
	 * @param allowedSuffixes
	 *            a {@link Collection} containing the allowed suffixes for files
	 *            this file chooser should display. If <code>null</code> or
	 *            empty (= list with not entries) all files will be displayed.
	 * @return the chosen folder; <code>null</code> if the user closes the
	 *         dialog or does not select a folder.
	 */
	public static XdevFile showFileChooser(@Nullable final String okButtonText,
			@Nullable final Collection<?> allowedSuffixes)
	{
		return showFileChooser(okButtonText,allowedSuffixes,null);
	}


	/**
	 * Shows the <i>system file chooser dialog</i> to select a file.
	 *
	 * <p>
	 * <b> Hint:<br>
	 * For a more sophisticated way to use file choosers see
	 * {@link XdevFileChooserFactory}. </b>
	 * </p>
	 *
	 * @param okButtonText
	 *            {@link String} to use as text for the <i>ok button</i> of the
	 *            file chooser. If <code>null</code> the default text is used.
	 * @param allowedSuffixes
	 *            a {@link Collection} containing the allowed suffixes for files
	 *            this file chooser should display. If <code>null</code> or
	 *            empty (= list with not entries) all files will be displayed.
	 * @param initialPath
	 *            {@link String} path to open the file chooser for. If
	 *            <code>null</code> the <i>folder chooser</i> starts in the last
	 *            used folder.
	 * @return the chosen folder; <code>null</code> if the user closes the
	 *         dialog or does not select a folder.
	 */
	public static XdevFile showFileChooser(@Nullable String okButtonText,
			@Nullable final Collection<?> allowedSuffixes, @Nullable final String initialPath)
	{
		final JFileChooser fc = createFileChooser(allowedSuffixes);

		if(okButtonText == null || okButtonText.length() == 0)
		{
			okButtonText = UIManager.getString("OptionPane.okButtonText");
		}

		File dir = null;
		if(initialPath != null && initialPath.length() > 0)
		{
			dir = new File(initialPath);
		}

		fc.setCurrentDirectory(dir);

		if(fc.showDialog(getActiveWindow(),okButtonText) == JFileChooser.APPROVE_OPTION)
		{
			final File file = fc.getSelectedFile();
			if(file != null)
			{
				return new XdevFile(file);
			}
		}

		return null;
	}


	/**
	 * Shows the <i>system file chooser dialog</i> to select a file.
	 * <p>
	 * The standard ok button text is used. The chooser starts in the last used
	 * folder.
	 * </p>
	 * <p>
	 * <strong>Hint:</strong> Does not show the {@link JFileChooser}. To show
	 * the {@link JFileChooser} use {@link #showFileChooser(String, Collection)}
	 * .
	 * </p>
	 *
	 * <p>
	 * <b> Hint:<br>
	 * For a more sophisticated way to use file choosers see
	 * {@link XdevFileChooserFactory}. </b>
	 * </p>
	 *
	 * @param allowedSuffixes
	 *            a {@link Collection} containing the allowed suffixes for files
	 *            this file chooser should display. If <code>null</code> or
	 *            empty (= list with not entries) all files will be displayed.
	 *
	 * @return the chosen folder; <code>null</code> if the user closes the
	 *         dialog or does not select a folder.
	 */
	public static JFileChooser createFileChooser(@Nullable final Collection<?> allowedSuffixes)
	{
		if(fileChooser == null)
		{
			fileFilter = new XdevFileFilter();
			fileChooser = new JFileChooser();
		}

		if(allowedSuffixes != null && allowedSuffixes.size() > 0)
		{
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileFilter.setFilter(allowedSuffixes);
			fileChooser.setFileFilter(fileFilter);
		}
		else
		{
			fileFilter.clear();
			fileChooser.setFileFilter(null);
			fileChooser.setAcceptAllFileFilterUsed(true);
		}

		return fileChooser;
	}



	/**
	 * JavaDoc omitted for private class.
	 */
	private static class XdevFileFilter extends javax.swing.filechooser.FileFilter
	{
		private String[]	allowedSuffixes;
		private String		description;


		public XdevFileFilter()
		{
		}


		public void clear()
		{
			this.allowedSuffixes = new String[0];
			this.description = "";
		}


		public void setFilter(final Collection<?> allowedSuffixes)
		{
			final StringBuffer sb = new StringBuffer();
			final int c = allowedSuffixes.size();
			int i = 0;
			this.allowedSuffixes = new String[c];
			for(final Object suffix : allowedSuffixes)
			{
				this.allowedSuffixes[i] = suffix.toString().toUpperCase();

				if(i > 0)
				{
					sb.append(", ");
				}
				sb.append(this.allowedSuffixes[i]);

				i++;
			}
			this.description = sb.toString();
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getDescription()
		{
			return this.description;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean accept(final File f)
		{
			if(f.isDirectory())
			{
				return true;
			}

			final String name = f.getName().toUpperCase();
			for(int i = 0; i < this.allowedSuffixes.length; i++)
			{
				if(name.endsWith(this.allowedSuffixes[i]))
				{
					return true;
				}
			}

			return false;
		}
	}


	/**
	 * Shows a {@link JColorChooser} dialog so the user can choose a
	 * {@link Color}.
	 *
	 * @param title
	 *            {@link String} title of the dialog. If <code>null</code> the
	 *            chooser title will be empty.
	 * @param initialColor
	 *            {@link Color} to start with. If <code>null</code> the chooser
	 *            is started with {@link Color#white}.
	 * @return the selected {@link Color}; or <code>null</code> if the user
	 *         closes the dialog or does not select a {@link Color}.
	 */
	public static Color showColorChooser(@Nullable final String title, @Nullable Color initialColor)
	{
		if(initialColor == null)
		{
			initialColor = Color.white;
		}

		return JColorChooser.showDialog(getActiveWindow(),title,initialColor);
	}


	/**
	 * Shortcut for <code>lookupComponentTree(parent,visitor,null)</code>.
	 *
	 * @param <T>
	 *            The return type
	 * @param <C>
	 *            The component type to visit
	 * @param parent
	 *            The root of the component tree to visit
	 * @param visitor
	 *            the {@link ComponentTreeVisitor}
	 *
	 * @see ComponentTreeVisitor
	 */
	public static <T, C extends Component> T lookupComponentTree(final Container parent,
			final ComponentTreeVisitor<T, C> visitor)
	{
		return lookupComponentTree(parent,visitor,null);
	}


	/**
	 * Walks through the <code>parent</code>'s component tree hierarchy.
	 * <p>
	 * For every child in the tree hierarchy, which fits the <code>type</code>
	 * parameter (or type is <code>null</code>), the <code>visitor</code>'s
	 * {@link ComponentTreeVisitor#visit(Component)} is invoked.
	 * <p>
	 * If the <code>visit</code> method returns a value != <code>null</code>,
	 * the visitation ends and that value is returned.
	 *
	 *
	 * @param <T>
	 *            The return type
	 * @param <C>
	 *            The component type to visit
	 * @param parent
	 *            The root of the component tree to visit
	 * @param visitor
	 *            the {@link ComponentTreeVisitor}
	 * @param type
	 *            The component type class to visit
	 *
	 * @see ComponentTreeVisitor
	 */
	public static <T, C extends Component> T lookupComponentTree(Container parent,
			final ComponentTreeVisitor<T, C> visitor, final Class<C> type)
	{
		T value = null;

		if(type == null || type.isInstance(parent))
		{
			value = visitor.visit((C)parent);
		}

		if(value == null)
		{
			if(parent instanceof JMenu)
			{
				parent = ((JMenu)parent).getPopupMenu();
			}

			final int c = parent.getComponentCount();
			for(int i = 0; i < c && value == null; i++)
			{
				final Component cpn = parent.getComponent(i);
				if(cpn instanceof Container)
				{
					value = lookupComponentTree((Container)cpn,visitor,type);
				}
				else if(type == null || type.isInstance(cpn))
				{
					value = visitor.visit((C)cpn);
				}
			}
		}

		return value;
	}


	/**
	 * Returns the current active {@link Window}.
	 *
	 * @return the current active {@link Window}.
	 */
	public static Window getActiveWindow()
	{
		final FocusManager fm = FocusManager.getCurrentManager();
		Window w = fm.getActiveWindow();
		if(w != null)
		{
			return w;
		}

		final Component c = fm.getFocusOwner();
		if(c != null)
		{
			w = SwingUtilities.getWindowAncestor(c);
			if(w != null)
			{
				return w;
			}
		}

		return JOptionPane.getRootFrame();
	}

	//
	// Option types
	//
	/**
	 * Type meaning Look and Feel should not supply any options -- only use the
	 * options from the <code>JOptionPane</code>.
	 */
	public static final int	DEFAULT_OPTION			= JOptionPane.DEFAULT_OPTION;
	/** Type used for <code>showConfirmDialog</code>. */
	public static final int	YES_NO_OPTION			= JOptionPane.YES_NO_OPTION;
	/** Type used for <code>showConfirmDialog</code>. */
	public static final int	YES_NO_CANCEL_OPTION	= JOptionPane.YES_NO_CANCEL_OPTION;
	/** Type used for <code>showConfirmDialog</code>. */
	public static final int	OK_CANCEL_OPTION		= JOptionPane.OK_CANCEL_OPTION;

	//
	// Return values.
	//
	/** Return value from class method if YES is chosen. */
	public static final int	YES_OPTION				= JOptionPane.YES_OPTION;
	/** Return value from class method if NO is chosen. */
	public static final int	NO_OPTION				= JOptionPane.NO_OPTION;
	/** Return value from class method if CANCEL is chosen. */
	public static final int	CANCEL_OPTION			= JOptionPane.CANCEL_OPTION;
	/** Return value form class method if OK is chosen. */
	public static final int	OK_OPTION				= JOptionPane.OK_OPTION;
	/**
	 * Return value from class method if user closes window without selecting
	 * anything, more than likely this should be treated as either a
	 * <code>CANCEL_OPTION</code> or <code>NO_OPTION</code>.
	 */
	public static final int	CLOSED_OPTION			= JOptionPane.CLOSED_OPTION;

	/**
	 * Used for error messages.
	 */
	public static final int	ERROR_MESSAGE			= JOptionPane.ERROR_MESSAGE;
	/**
	 * Used for information messages.
	 */
	public static final int	INFORMATION_MESSAGE		= JOptionPane.INFORMATION_MESSAGE;
	/**
	 * Used for warning messages.
	 */
	public static final int	WARNING_MESSAGE			= JOptionPane.WARNING_MESSAGE;
	/**
	 * Used for questions.
	 */
	public static final int	QUESTION_MESSAGE		= JOptionPane.QUESTION_MESSAGE;
	/**
	 * No icon is used.
	 */
	public static final int	PLAIN_MESSAGE			= JOptionPane.PLAIN_MESSAGE;



	/**
	 * Message types. Used by the UI to determine what icon to display, and
	 * possibly what behavior to give based on the type.
	 *
	 *
	 * @since 3.0
	 *
	 * @author XDEV Software
	 *
	 */
	public static enum MessageDialogType
	{
		
		/**
		 * Used for error messages.
		 */
		ERROR_MESSAGE(UIUtils.ERROR_MESSAGE),
		/**
		 * Used for information messages.
		 */
		INFORMATION_MESSAGE(UIUtils.INFORMATION_MESSAGE),
		/**
		 * Used for warning messages.
		 */
		WARNING_MESSAGE(UIUtils.WARNING_MESSAGE),
		/**
		 * Used for questions.
		 */
		QUESTION_MESSAGE(UIUtils.QUESTION_MESSAGE),
		/**
		 * No icon is used.
		 */
		PLAIN_MESSAGE(UIUtils.PLAIN_MESSAGE);

		private final int typeValue;


		private MessageDialogType(final int typeValue)
		{
			this.typeValue = typeValue;
		}


		/**
		 * Returns the <code>int</code> representation of the message type.
		 *
		 * @return the <code>int</code> representation of the message type.
		 */
		public int getTypeValue()
		{
			return this.typeValue;
		}

	}


	/**
	 * Brings up a dialog that displays a message using a no icon.
	 *
	 *
	 * @param message
	 *            the message to display
	 * @param title
	 *            the title string for the dialog
	 *
	 * @see #showMessage(String, String, int)
	 */
	public static void showMessage(final String title, final String message)
	{
		showMessage(title,message,PLAIN_MESSAGE);
	}


	/**
	 * Brings up a dialog that displays a message using a default icon
	 * determined by the <code>messageType</code> parameter.
	 *
	 *
	 * @param message
	 *            the message to display
	 * @param title
	 *            the title string for the dialog
	 * @param messageType
	 *            the type of message to be displayed:
	 *            <code>ERROR_MESSAGE</code>, <code>INFORMATION_MESSAGE</code>,
	 *            <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
	 *            or <code>PLAIN_MESSAGE</code>
	 *
	 * @see #ERROR_MESSAGE
	 * @see #WARNING_MESSAGE
	 * @see #INFORMATION_MESSAGE
	 * @see #QUESTION_MESSAGE
	 * @see #PLAIN_MESSAGE
	 *
	 */
	public static void showMessage(final String title, final String message, final int messageType)
	{
		JOptionPane.showMessageDialog(getActiveWindow(),message,title,messageType);
	}


	/**
	 * Brings up a dialog that displays a message using a default icon
	 * determined by the <code>messageType</code> parameter.
	 *
	 *
	 * @param message
	 *            the message to display
	 * @param title
	 *            the title string for the dialog
	 * @param messageDialogType
	 *            the type of message to be displayed:
	 *            {@link MessageDialogType#ERROR_MESSAGE},
	 *            {@link MessageDialogType#INFORMATION_MESSAGE}
	 *            {@link MessageDialogType#WARNING_MESSAGE},
	 *            {@link MessageDialogType#QUESTION_MESSAGE}, or
	 *            {@link MessageDialogType#PLAIN_MESSAGE}
	 *
	 * @see MessageDialogType
	 *
	 * @since 3.0
	 */
	public static void showMessage(final String title, final String message,
			final MessageDialogType messageDialogType)
	{
		JOptionPane.showMessageDialog(getActiveWindow(),message,title,
				messageDialogType.getTypeValue());
	}


	/**
	 * Brings up a simple confirm dialog.
	 *
	 * @param message
	 *            the message to display
	 * @param title
	 *            the title string for the dialog
	 *
	 * @param ok
	 *            {@link String} to use as text for the <i>ok button</i>.
	 * @param cancel
	 *            {@link String} to use as text for the <i>cancel button</i>.
	 *
	 *
	 * @return <code>true</code> if the user selected the <i>ok button</i>;
	 *         otherwise <code>false</code>.
	 *
	 */
	public static boolean showConfirmMessage(final String title, final String message,
			final String ok, final String cancel)
	{
		return showConfirmMessage(title,message,ok,cancel,PLAIN_MESSAGE);
	}


	/**
	 * Brings up a simple confirm dialog with the specified icon.
	 *
	 * @param message
	 *            the message to display
	 * @param title
	 *            the title string for the dialog
	 *
	 * @param ok
	 *            {@link String} to use as text for the <i>ok button</i>.
	 * @param cancel
	 *            {@link String} to use as text for the <i>cancel button</i>.
	 * @param messageType
	 *            the type of message to be displayed:
	 *            <code>ERROR_MESSAGE</code>, <code>INFORMATION_MESSAGE</code>,
	 *            <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
	 *            or <code>PLAIN_MESSAGE</code>
	 *
	 * @return <code>true</code> if the user selected the <i>ok button</i>;
	 *         otherwise <code>false</code>.
	 *
	 * @see #ERROR_MESSAGE
	 * @see #WARNING_MESSAGE
	 * @see #INFORMATION_MESSAGE
	 * @see #QUESTION_MESSAGE
	 * @see #PLAIN_MESSAGE
	 */
	public static boolean showConfirmMessage(final String title, final String message,
			final String ok, final String cancel, final int messageType)
	{
		return JOptionPane.showOptionDialog(getActiveWindow(),message,title,OK_CANCEL_OPTION,
				messageType,null,new String[]{ok,cancel},ok) == OK_OPTION;
	}


	/**
	 * Brings up a simple confirm dialog with the specified icon.
	 *
	 * @param message
	 *            the message to display
	 * @param title
	 *            the title string for the dialog
	 *
	 * @param ok
	 *            {@link String} to use as text for the <i>ok button</i>.
	 * @param cancel
	 *            {@link String} to use as text for the <i>cancel button</i>.
	 * @param messageDialogType
	 *            the type of message to be displayed:
	 *            {@link MessageDialogType#ERROR_MESSAGE},
	 *            {@link MessageDialogType#INFORMATION_MESSAGE}
	 *            {@link MessageDialogType#WARNING_MESSAGE},
	 *            {@link MessageDialogType#QUESTION_MESSAGE}, or
	 *            {@link MessageDialogType#PLAIN_MESSAGE}
	 *
	 * @return <code>true</code> if the user selected the <i>ok button</i>;
	 *         otherwise <code>false</code>.
	 *
	 * @throws NullPointerException
	 *             if <code>messageDialogType</code> is <code>null</code>
	 *
	 * @see MessageDialogType
	 *
	 * @since 3.0
	 */
	public static boolean showConfirmMessage(final String title, final String message,
			final String ok, final String cancel,
			@NotNull final MessageDialogType messageDialogType) throws NullPointerException
	{
		return JOptionPane.showOptionDialog(getActiveWindow(),message,title,OK_CANCEL_OPTION,
				messageDialogType.getTypeValue(),null,new String[]{ok,cancel},ok) == OK_OPTION;
	}


	/**
	 * Brings up a simple confirm dialog with the specified icon.
	 *
	 * @param message
	 *            the message to display
	 * @param title
	 *            the title string for the dialog
	 *
	 * @param options
	 *            an {@link Collection} of objects indicating the possible
	 *            choices the user can make; if the objects are components, they
	 *            are rendered properly; non-<code>String</code> objects are
	 *            rendered using their <code>toString</code> methods; if this
	 *            parameter is <code>null</code>, the options are determined by
	 *            the Look and Feel
	 * @param selectedIndex
	 *            the index of the option that should be the default selection
	 *            for the dialog. The first element of options has the
	 *            <code>index 0</code>.
	 *
	 * @param messageType
	 *            the type of message to be displayed:
	 *            <code>ERROR_MESSAGE</code>, <code>INFORMATION_MESSAGE</code>,
	 *            <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
	 *            or <code>PLAIN_MESSAGE</code>
	 *
	 * @return the <code>option</code> object indicating the option chosen by
	 *         the user, or <code>null</code> if the user closed the dialog.
	 *
	 * @see #ERROR_MESSAGE
	 * @see #WARNING_MESSAGE
	 * @see #INFORMATION_MESSAGE
	 * @see #QUESTION_MESSAGE
	 * @see #PLAIN_MESSAGE
	 */
	public static Object showConfirmMessage(final String title, final String message,
			final Collection<?> options, final int selectedIndex, final int messageType)
	{
		return showConfirmMessage(title,message,options.toArray(),selectedIndex,messageType);
	}


	/**
	 * Brings up a simple confirm dialog with the specified icon.
	 *
	 * @param message
	 *            the message to display
	 * @param title
	 *            the title string for the dialog
	 *
	 * @param options
	 *            an {@link Collection} of objects indicating the possible
	 *            choices the user can make; if the objects are components, they
	 *            are rendered properly; non-<code>String</code> objects are
	 *            rendered using their <code>toString</code> methods; if this
	 *            parameter is <code>null</code>, the options are determined by
	 *            the Look and Feel
	 * @param selectedIndex
	 *            the index of the option that should be the default selection
	 *            for the dialog. The first element of options has the
	 *            <code>index 0</code>.
	 *
	 * @param messageDialogType
	 *            the type of message to be displayed:
	 *            {@link MessageDialogType#ERROR_MESSAGE},
	 *            {@link MessageDialogType#INFORMATION_MESSAGE}
	 *            {@link MessageDialogType#WARNING_MESSAGE},
	 *            {@link MessageDialogType#QUESTION_MESSAGE}, or
	 *            {@link MessageDialogType#PLAIN_MESSAGE}
	 *
	 * @return the <code>option</code> object indicating the option chosen by
	 *         the user, or <code>null</code> if the user closed the dialog.
	 *
	 * @throws NullPointerException
	 *             if <code>messageDialogType</code> is <code>null</code>
	 *
	 * @see MessageDialogType
	 *
	 * @since 3.0
	 */
	public static Object showConfirmMessage(final String title, final String message,
			final Collection<?> options, final int selectedIndex,
			@NotNull final MessageDialogType messageDialogType) throws NullPointerException
	{
		return showConfirmMessage(title,message,options.toArray(),selectedIndex,
				messageDialogType.getTypeValue());
	}


	/**
	 * Brings up a simple confirm dialog with the specified icon.
	 *
	 * @param message
	 *            the message to display
	 * @param title
	 *            the title string for the dialog
	 *
	 * @param options
	 *            an array of objects indicating the possible choices the user
	 *            can make; if the objects are components, they are rendered
	 *            properly; non-<code>String</code> objects are rendered using
	 *            their <code>toString</code> methods; if this parameter is
	 *            <code>null</code>, the options are determined by the Look and
	 *            Feel
	 * @param selectedIndex
	 *            the index of the option that should be the default selection
	 *            for the dialog. The first element of options has the
	 *            <code>index 0</code>.
	 *
	 * @param messageType
	 *            the type of message to be displayed:
	 *            <code>ERROR_MESSAGE</code>, <code>INFORMATION_MESSAGE</code>,
	 *            <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
	 *            or <code>PLAIN_MESSAGE</code>
	 *
	 * @return the <code>option</code> object indicating the option chosen by
	 *         the user, or <code>null</code> if the user closed the dialog.
	 *
	 * @see #ERROR_MESSAGE
	 * @see #WARNING_MESSAGE
	 * @see #INFORMATION_MESSAGE
	 * @see #QUESTION_MESSAGE
	 * @see #PLAIN_MESSAGE
	 */
	public static Object showConfirmMessage(@Nullable final String title, final String message,
			final Object[] options, final int selectedIndex, final int messageType)
	{
		final int value = JOptionPane.showOptionDialog(getActiveWindow(),message,title,
				DEFAULT_OPTION,messageType,null,options,options[selectedIndex]);

		return value == CLOSED_OPTION ? null : options[value];
	}


	/**
	 * Brings up a simple confirm dialog with the specified icon.
	 *
	 * @param message
	 *            the message to display
	 * @param title
	 *            the title string for the dialog
	 *
	 * @param options
	 *            an array of objects indicating the possible choices the user
	 *            can make; if the objects are components, they are rendered
	 *            properly; non-<code>String</code> objects are rendered using
	 *            their <code>toString</code> methods; if this parameter is
	 *            <code>null</code>, the options are determined by the Look and
	 *            Feel
	 * @param selectedIndex
	 *            the index of the option that should be the default selection
	 *            for the dialog. The first element of options has the
	 *            <code>index 0</code>.
	 *
	 * @param messageDialogType
	 *            the type of message to be displayed:
	 *            {@link MessageDialogType#ERROR_MESSAGE},
	 *            {@link MessageDialogType#INFORMATION_MESSAGE}
	 *            {@link MessageDialogType#WARNING_MESSAGE},
	 *            {@link MessageDialogType#QUESTION_MESSAGE}, or
	 *            {@link MessageDialogType#PLAIN_MESSAGE}
	 *
	 * @return the <code>option</code> object indicating the option chosen by
	 *         the user, or <code>null</code> if the user closed the dialog.
	 *
	 * @throws NullPointerException
	 *             if <code>messageDialogType</code> is <code>null</code>
	 *
	 * @see MessageDialogType
	 *
	 * @since 3.0
	 */
	public static Object showConfirmMessage(@Nullable final String title, final String message,
			final Object[] options, final int selectedIndex,
			@NotNull final MessageDialogType messageDialogType) throws NullPointerException
	{
		final int value = JOptionPane.showOptionDialog(getActiveWindow(),message,title,
				DEFAULT_OPTION,messageDialogType.getTypeValue(),null,options,
				options[selectedIndex]);

		return value == CLOSED_OPTION ? null : options[value];
	}


	public static String getIP()
	{
		String ip;

		try
		{
			final InetAddress ia = Application.isApplet()
					? InetAddress.getByName(Application.getContainer().getCodeBase().getHost())
					: InetAddress.getLocalHost();
			ip = ia.getHostAddress();
		}
		catch(final Exception e)
		{
			ip = "127.0.0.1";
		}

		return ip;
	}


	/**
	 * Causes the currently executing thread to sleep (temporarily cease
	 * execution) for the specified number of milliseconds, subject to the
	 * precision and accuracy of system timers and schedulers. The thread does
	 * not lose ownership of any monitors.
	 *
	 * @param millis
	 *            the length of time to sleep in milliseconds.
	 */
	public static void pause(final long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch(final InterruptedException e)
		{
		}
	}


	/**
	 * Returns the component for the provided <code>name</code>.Searches for the
	 * component in the current active {@link Window}.
	 *
	 * @param name
	 *            to get component for.
	 * @return the requested component if the component could be found in the
	 *         provided <code>window</code>; otherwise <code>null</code>.
	 */
	public static Component getComponentByName(final String name)
	{
		return getComponentByName(name,getActiveWindow());
	}


	/**
	 * Returns the component for the provided <code>name</code>. Searches for
	 * the component in the given {@link Window} <code>window</code>.
	 *
	 * @param name
	 *            to get component for.
	 * @param window
	 *            {@link Window} to search for the component in.
	 * @return the requested component if the component could be found in the
	 *         provided <code>window</code>; otherwise <code>null</code>.
	 */
	public static Component getComponentByName(@Nullable final String name,
			@Nullable final Window window)
	{
		Component cpn = null;

		if(window != null && window instanceof XdevRootPaneContainer)
		{
			final XdevWindow xdevWindow = ((XdevRootPaneContainer)window).getXdevWindow();

			cpn = lookupComponentTree(xdevWindow,new ComponentTreeVisitor<Component, Component>()
			{
				@Override
				public Component visit(final Component cpn)
				{
					final String cpnName = cpn.getName();
					if(cpnName != null && cpnName.equals(name))
					{
						return cpn;
					}
					
					return null;
				}
			});
		}

		return cpn;
	}


	/**
	 * Returns the JApplet of the running application, if the running
	 * application runs as applet.
	 *
	 * @return the JApplet of the running application, if the running
	 *         application runs as applet; otherwise <code>null</code>.
	 */
	public static JApplet getApplet()
	{
		if(Application.isApplet())
		{
			return (JApplet)Application.getContainer();
		}

		return null;
	}


	/**
	 * Creates and shows a {@link XdevFrame} hosting the provided
	 * {@link XdevWindow} <code>content</code>, using content's title as title
	 * for the frame.
	 *
	 * @param content
	 *            {@link XdevWindow} to display in the frame
	 * @return the created {@link XdevFrame} with the provided
	 *         <code>title</code>, hosting the provided {@link XdevWindow}
	 *         <code>content</code> .
	 */
	public static XdevFrame createFrame(@NotNull final XdevWindow content)
	{
		return createFrame(content,content.getTitle());
	}


	/**
	 * Creates and shows a {@link XdevFrame} with the provided
	 * <code>title</code>, hosting the provided {@link XdevWindow}
	 * <code>content</code>.
	 *
	 * @param content
	 *            {@link XdevWindow} to display in the frame
	 * @param title
	 *            title of the frame
	 * @return the created {@link XdevFrame} with the provided
	 *         <code>title</code>, hosting the provided {@link XdevWindow}
	 *         <code>content</code> .
	 */
	public static XdevFrame createFrame(@NotNull final XdevWindow content, final String title)
	{
		final Dimension d = content.getSize();
		if(d.width > 0 || d.height > 0)
		{
			content.setPreferredSize(d);
		}

		final XdevFrame frame = new XdevFrame(title,(Image)null);
		frame.setXdevWindow(content);
		frame.setTitle(title);
		frame.pack();

		frame.setVisible(true);

		return frame;
	}


	/**
	 * Creates and shows a {@link XdevDialog} hosting the provided
	 * {@link XdevWindow} <code>content</code>, using content's title as title
	 * for the dialog.
	 *
	 * @param content
	 *            {@link XdevWindow} to display in the dialog
	 * @param owner
	 *            the owner of the dialog
	 *
	 * @param modal
	 *            if <code>true</code> the dialog is modal; otherwise it is not
	 *
	 * @return the created {@link XdevDialog} with the provided
	 *         <code>title</code>, hosting the provided {@link XdevWindow}
	 *         <code>content</code> .
	 */
	public static XdevDialog createDialog(final XdevWindow content, final Component owner,
			final boolean modal)
	{
		return createDialog(content,owner,content.getTitle(),modal);
	}


	/**
	 * Creates and shows a {@link XdevDialog} with the provided
	 * <code>title</code>, hosting the provided {@link XdevWindow}
	 * <code>content</code>.
	 *
	 * @param content
	 *            {@link XdevWindow} to display in the dialog
	 * @param owner
	 *            the owner of the dialog
	 * @param title
	 *            title of the dialog
	 *
	 * @param modal
	 *            if <code>true</code> the dialog is modal; otherwise it is not
	 *
	 * @return the created {@link XdevDialog} with the provided
	 *         <code>title</code>, hosting the provided {@link XdevWindow}
	 *         <code>content</code> .
	 */
	public static XdevDialog createDialog(final XdevWindow content, final Component owner,
			final String title, final boolean modal)
	{
		final Dimension d = content.getSize();
		if(d.width > 0 || d.height > 0)
		{
			content.setPreferredSize(d);
		}

		final XdevDialog dialog = new XdevDialog(owner,modal,content);
		dialog.setTitle(title);
		dialog.pack();

		dialog.setVisible(true);

		return dialog;
	}


	/**
	 * Creates and shows a {@link XdevScreen} hosting the provided
	 * {@link XdevWindow} <code>content</code>.
	 *
	 * @param content
	 *            {@link XdevWindow} to display in the window
	 * @param owner
	 *            the owner of the window
	 *
	 *
	 * @return the created {@link XdevScreen} with the provided
	 *         <code>title</code>, hosting the provided {@link XdevWindow}
	 *         <code>content</code> .
	 */
	public static XdevScreen createScreen(final XdevWindow content, final Component owner)
	{
		final Dimension d = content.getSize();
		if(d.width > 0 || d.height > 0)
		{
			content.setPreferredSize(d);
		}

		final XdevScreen screen = new XdevScreen(owner,false);
		screen.setXdevWindow(content);
		screen.pack();

		screen.setVisible(true);

		return screen;
	}


	/**
	 * Moves the {@link Component} <code>cpn</code> to the specified position.
	 *
	 * @param cpn
	 *            {@link Component} to move
	 * @param x
	 *            new x coordinate of the {@link Component}
	 * @param y
	 *            new y coordinate of the {@link Component}
	 * @param relative
	 *            if true the Component will be moved relative to is current
	 *            position; other wise the Component will be moved absolute.
	 */
	public static void move(@NotNull Component cpn, int x, int y, final boolean relative)
	{
		if(cpn instanceof XdevWindow)
		{
			final XdevWindow xdevWindow = (XdevWindow)cpn;
			cpn = (Component)xdevWindow.getOwner();
		}

		if(relative)
		{
			x = cpn.getX() + x;
			y = cpn.getY() + y;
		}

		cpn.setLocation(x,y);
	}


	/**
	 *
	 * Moves the {@link Component} <code>cpn</code> to the specified position.
	 *
	 * @param cpn
	 *            {@link Component} to move
	 * @param toX
	 *            new x coordinate of the {@link Component}
	 * @param toY
	 *            new y coordinate of the {@link Component}
	 * @param duration
	 *            duration of the move operation in milliseconds
	 *
	 *
	 * @param relative
	 *            if true the Component will be moved relative to is current
	 *            position; other wise the Component will be moved absolute.
	 *
	 * @param wait
	 *            if <code>true</code> the moving operation forces all following
	 *            operations to wait for it to finish; otherwise the following
	 *            operation will start immediately.
	 */
	public static void move(@NotNull final Component cpn, final int toX, final int toY,
			final long duration, final boolean relative, final boolean wait)
	{
		final Mover mover = new Mover(cpn,toX,toY,duration,relative);

		if(wait)
		{
			mover.run();
		}
		else
		{
			mover.start();
		}
	}



	/**
	 * JavaDoc omitted for private class.
	 */
	private static class Mover extends Thread
	{
		private final Component	cpn;
		private int				x, y;
		private final long		duration;
		private final boolean	relative;


		public Mover(final Component cpn, final int x, final int y, final long duration,
				final boolean relative)
		{
			super();
			this.cpn = cpn;
			this.x = x;
			this.y = y;
			this.duration = duration;
			this.relative = relative;
		}


		@Override
		public void run()
		{
			final int startx = this.cpn.getX();
			final int starty = this.cpn.getY();

			if(this.relative)
			{
				this.x = startx + this.x;
				this.y = starty + this.y;
			}

			final int diffx = this.x - startx;
			final int diffy = this.y - starty;
			final int steps = (int)Math.round(25d * (this.duration / 1000d));
			final double stepx = (double)diffx / (double)steps;
			final double stepy = (double)diffy / (double)steps;

			for(int i = 0; i < steps; i++)
			{
				final long start = System.currentTimeMillis();

				final int newx = (int)Math.round(startx + stepx * i);
				final int newy = (int)Math.round(starty + stepy * i);
				this.cpn.setLocation(newx,newy);

				try
				{
					final long stepDuration = System.currentTimeMillis() - start;
					Thread.sleep(Math.max(0,40 - stepDuration));
				}
				catch(final InterruptedException e)
				{
				}
			}

			this.cpn.setLocation(this.x,this.y);
		}
	}


	/**
	 * Moves the {@link Component} <code>cpn</code> to the specified position
	 * and scales the {@link Component} to fit the new <code>width</code> and
	 * <code>height</code>.
	 *
	 * @param cpn
	 *            {@link Component} to move
	 * @param x
	 *            new x coordinate of the {@link Component}
	 * @param y
	 *            new y coordinate of the {@link Component}
	 *
	 * @param width
	 *            new width of the {@link Component}
	 * @param height
	 *            new height of the {@link Component}
	 * @param relative
	 *            if true the Component will be moved relative to is current
	 *            position; other wise the Component will be moved absolute.
	 */
	public static void scale(@NotNull Component cpn, int x, int y, int width, int height,
			final boolean relative)
	{
		if(cpn instanceof XdevWindow)
		{
			final XdevWindow xdevWindow = (XdevWindow)cpn;
			cpn = (Component)xdevWindow.getOwner();
		}

		if(relative)
		{
			x = cpn.getX() + x;
			y = cpn.getY() + y;
			width = cpn.getWidth() + width;
			height = cpn.getHeight() + height;
		}

		cpn.setBounds(x,y,width,height);
	}


	/**
	 *
	 * Moves the {@link Component} <code>cpn</code> to the specified position
	 * and scales the {@link Component} to fit the new <code>width</code> and
	 * <code>height</code>.
	 *
	 * @param cpn
	 *            {@link Component} to move
	 * @param toX
	 *            new x coordinate of the {@link Component}
	 * @param toY
	 *            new y coordinate of the {@link Component}
	 * @param duration
	 *            duration of the move operation in milliseconds
	 * @param toWidth
	 *            new width of the {@link Component}
	 * @param toHeight
	 *            new height of the {@link Component}
	 *
	 * @param wait
	 *            if <code>true</code> the moving operation forces all following
	 *            operations to wait for it to finish; otherwise the following
	 *            operation will start immediately.
	 */
	public static void scale(final Component cpn, final int toX, final int toY, final int toWidth,
			final int toHeight, final long duration, final boolean wait)
	{
		final Scaler scaler = new Scaler(cpn,toX,toY,toWidth,toHeight,duration);

		if(wait)
		{
			scaler.run();
		}
		else
		{
			scaler.start();
		}
	}



	/**
	 * JavaDoc omitted for private class.
	 */
	private static class Scaler extends Thread
	{
		private final Component	cpn;
		private final int		x, y, w, h;
		private final long		duration;


		public Scaler(final Component cpn, final int x, final int y, final int w, final int h,
				final long duration)
		{
			super();
			this.cpn = cpn;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.duration = duration;
		}


		@Override
		public void run()
		{
			final int startx = this.cpn.getX();
			final int starty = this.cpn.getY();
			final int startw = this.cpn.getWidth();
			final int starth = this.cpn.getHeight();
			final int diffx = this.x - startx;
			final int diffy = this.y - starty;
			final int diffw = this.w - startw;
			final int diffh = this.h - starth;
			final int steps = (int)Math.round(25d * (this.duration / 1000d));
			final double stepx = (double)diffx / (double)steps;
			final double stepy = (double)diffy / (double)steps;
			final double stepw = (double)diffw / (double)steps;
			final double steph = (double)diffh / (double)steps;

			for(int i = 0; i < steps; i++)
			{
				final long start = System.currentTimeMillis();

				final int newx = (int)Math.round(startx + stepx * i);
				final int newy = (int)Math.round(starty + stepy * i);
				final int neww = (int)Math.round(startw + stepw * i);
				final int newh = (int)Math.round(starth + steph * i);
				this.cpn.setBounds(newx,newy,neww,newh);

				try
				{
					final int stepDuration = (int)(System.currentTimeMillis() - start);
					Thread.sleep(Math.max(0,40 - stepDuration));
				}
				catch(final InterruptedException e)
				{
				}
			}

			this.cpn.setBounds(this.x,this.y,this.w,this.h);
		}
	}


	/**
	 * Sets the provided {@link Component} <code>cpn</code> to front.
	 *
	 * @param cpn
	 *            Component to set to front
	 */
	public static void toFront(@NotNull final Component cpn)
	{
		moveZ(cpn,0);
	}


	/**
	 * Sets the provided {@link Component} <code>cpn</code> to back.
	 *
	 * @param cpn
	 *            Component to set to back
	 */
	public static void toBack(@NotNull final Component cpn)
	{
		moveZ(cpn,-1);
	}


	/**
	 * JavaDoc omitted for private method.
	 *
	 * @param cpn
	 *            x
	 * @param z
	 *            x
	 */
	private static void moveZ(final Component cpn, final int z)
	{
		final Container parent = cpn.getParent();
		if(parent != null)
		{
			parent.add(cpn,z);
			parent.validate();
			parent.repaint();
		}
	}


	/**
	 * Verifies the value of the given {@link JComponent} <code>cpn</code> using
	 * the set constrains of the component.
	 *
	 * @param cpn
	 *            {@link JComponent} to check value on
	 * @return <code>true</code> if the value of the component is valid or the
	 *         component has no constaints set; otherwise <code>false</code>
	 * @deprecated use {@link #validateState(FormularComponent)} instead
	 */
	@Deprecated
	public static boolean verifyFormularComponent(final JComponent cpn)
	{
		final Object o = cpn.getClientProperty(FORMULAR_VERIFIER);
		if(o != null && o instanceof FormularVerifier)
		{
			final FormularVerifier verifier = (FormularVerifier)o;
			final boolean ok = verifier.verify();
			if(!ok)
			{
				final String message = verifier.getMessage();
				if(message != null && message.length() > 0)
				{
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(cpn),message,
							verifier.getTitle(),JOptionPane.INFORMATION_MESSAGE);
				}
			}

			return ok;
		}

		return true;
	}


	/**
	 * Validates the state of <code>formCpn</code> using the {@link Validator}s
	 * of the {@link FormularComponent} and displays an error if one of the
	 * validators throws an error.
	 *
	 * @param formCpn
	 *            the component to validate
	 * @return <code>false</code> if at least one of the validators returns with
	 *         an error, <code>true</code> otherwise
	 */
	public static boolean validateState(final FormularComponent formCpn)
	{
		try
		{
			formCpn.validateState();
		}
		catch(final ValidationException e)
		{
			if(e.getSeverity() == Severity.ERROR)
			{
				showMessage(e.getTitle(),e.getMessage(),getMessageType(e.getSeverity()));
				return false;
			}
		}

		return true;
	}


	/**
	 * Returns the appropriate message type for the specified severity.
	 * <p>
	 * {@link Severity#ERROR} -&gt; {@link #ERROR_MESSAGE}<br>
	 * {@link Severity#WARNING} -&gt; {@link #WARNING_MESSAGE}<br>
	 * {@link Severity#INFORMATION} -&gt; {@link #INFORMATION_MESSAGE}<br>
	 *
	 * @param severity
	 * @return the appropriate message type for the specified severity
	 * @since 3.1
	 */
	public static int getMessageType(final Severity severity)
	{
		switch(severity)
		{
			case ERROR:
				return ERROR_MESSAGE;
			case WARNING:
				return WARNING_MESSAGE;
			case INFORMATION:
				return INFORMATION_MESSAGE;
		}
		return PLAIN_MESSAGE;
	}


	/**
	 * Creates a {@link XdevImage} from the provided {@link Component}
	 * <code>cpn</code>.
	 *
	 * @param cpn
	 *            Component to create a image from
	 * @return a {@link XdevImage} from the provided {@link Component}
	 *         <code>cpn</code>.
	 */
	public static XdevImage createImageFrom(@NotNull final Component cpn)
	{
		final Image img = cpn.createImage(cpn.getWidth(),cpn.getHeight());
		final Graphics g = img.getGraphics();
		cpn.paintAll(g);
		g.dispose();

		return new XdevImage(img);
	}


	/**
	 * Sets the border title for provided {@link JComponent} <code>cpn</code>.
	 * <p>
	 * <strong>Hint:</strong> Works only if the component has a
	 * {@link TitledBorder}. You can set a {@link TitledBorder} for a component
	 * via XDEV IDE or manually with {@link JComponent#setBorder(Border)}.
	 * </p>
	 *
	 * @param cpn
	 *            {@link JComponent} to set the title for
	 *
	 * @param title
	 *            {@link String} to be displayed in title of the border.
	 *
	 * @see JComponent#setBorder(Border)
	 */
	public static void setBorderTitle(final JComponent cpn, final String title)
	{
		final javax.swing.border.Border border = cpn.getBorder();
		if(border != null && border instanceof TitledBorder)
		{
			final TitledBorder tb = (TitledBorder)border;
			tb.setTitle(title);
			cpn.revalidate();
			cpn.repaint();
		}
	}


	/**
	 * Returns a {@link String} representation of the provided
	 * {@link JComponent} <code>cpn</code>.
	 *
	 * @param cpn
	 *            {@link JComponent} to get the {@link String} representation
	 *            for.
	 * @return a {@link String} representation of the provided
	 *         {@link JComponent} <code>cpn</code> or <code>null</code> if no
	 *         suitable {@link String} could be created.
	 */
	public static String toString(@NotNull final JComponent cpn)
	{
		final String tagData = getTagData(cpn);
		if(tagData != null && tagData.length() > 0)
		{
			return tagData;
		}

		final String name = cpn.getName();
		if(name != null && name.length() > 0)
		{
			return name;
		}

		return null;
	}


	/**
	 * Return a {@link String} representation of the <i>tag data</i> for the
	 * provided {@link Object} <code>obj</code>.
	 *
	 * @param obj
	 *            {@link Object} to get the tag data for.
	 * @return a {@link String} representation of the <i>tag data</i> for the
	 *         provided {@link Object} <code>obj</code> or <code>null</code> if
	 *         the object has no <i>tag data</i> set.
	 * @deprecated
	 */
	@Deprecated
	public static String getTagData(@NotNull final Object obj)
	{
		final Object tagData = getClientProperty(obj,TAG_DATA);
		return tagData == null ? "" : tagData.toString();
	}


	/**
	 * JavaDoc omitted for private method.
	 */
	@SuppressWarnings("deprecation")
	private static Object getClientProperty(final Object obj, final Object key)
	{
		if(obj instanceof JComponent)
		{
			return ((JComponent)obj).getClientProperty(key);
		}

		if(obj instanceof ClientPropertyAdapter)
		{
			return ((ClientPropertyAdapter)obj).getClientProperty(key);
		}

		return null;
	}


	/**
	 * Sets the <code>tagData</code> for the provided {@link Object}
	 * <code>obj</code>.
	 *
	 * @param obj
	 *            Object to set the <code>tagData</code> for.
	 *
	 * @param tagData
	 *            to set for the provided object
	 * @deprecated
	 */
	@Deprecated
	public static void setTagData(@NotNull final Object obj, final String tagData)
	{
		if(obj instanceof JComponent)
		{
			((JComponent)obj).putClientProperty(TAG_DATA,tagData);
		}
		else if(obj instanceof ClientPropertyAdapter)
		{
			((ClientPropertyAdapter)obj).putClientProperty(TAG_DATA,tagData);
		}
	}


	/**
	 * Performs the {@link Component#setEnabled(boolean)} method on all
	 * {@link Component}s in the provided {@link Container} <code>root</code>.
	 * For the {@link Container} itself the method is also called.
	 * <p>
	 * Before this a {@link EnabledState} is created to store the original
	 * state. This state is returned.
	 * </p>
	 * <p>
	 * <strong>Hint:</strong> This is a deep operation that also affects all
	 * children of children of <code>root</code> and so on.
	 * </p>
	 *
	 * @param root
	 *            {@link Container} to perform the operation on
	 * @param enabled
	 *            <code>true</code> to enable all {@link Component}s;
	 *            <code>false</code> to disable all {@link Component}s.
	 * @return the previous state of root's hierarchy
	 * @see EnabledState
	 * @since 3.1 now returns the previous {@link EnabledState}
	 */
	public static EnabledState setEnabled(@NotNull final Container root, final boolean enabled)
	{
		final EnabledState state = new EnabledState(root);

		lookupComponentTree(root,new ComponentTreeVisitor<Object, Component>()
		{
			@Override
			public Object visit(final Component cpn)
			{
				cpn.setEnabled(enabled);

				return null;
			}
		});

		return state;
	}


	/**
	 * Returns a {@link XdevList} containing all children of the provided
	 * {@link Container} <code>parent</code>.
	 *
	 *
	 * <p>
	 * <strong>Hint:</strong> Depending on <code>deep</code>, this can be a deep
	 * operation that also returns all children of children of
	 * <code>parent</code> and so on.
	 * </p>
	 *
	 * @param parent
	 *            {@link Container} to perform the operation on
	 * @param deep
	 *            <code>true</code> to make this a deep operation; otherwise
	 *            <code>false</code>
	 * @return a {@link XdevList} containing all children of the provided
	 *         {@link Container} parent. The {@link XdevList} does not contain
	 *         the provided <code>parent</code> itself.
	 */
	public static XdevList<Component> getChildrenOf(final Container parent, final boolean deep)
	{
		final XdevList<Component> children = new XdevList<Component>();
		addChildren(parent,children,deep);
		return children;
	}


	/**
	 * Adds children of the provided {@link Container} <code>parent</code> to
	 * the provided {@link XdevList}.
	 *
	 *
	 * <p>
	 * <strong>Hint:</strong> Depending on <code>deep</code>, this can be a deep
	 * operation that also returns all children of children of
	 * <code>parent</code> and so on.
	 * </p>
	 *
	 * @param parent
	 *            {@link Container} to perform the operation on
	 * @param children
	 *            {@link XdevList} to add the children to
	 * @param deep
	 *            <code>true</code> to make this a deep operation; otherwise
	 *            <code>false</code>
	 */
	public static void addChildren(@NotNull final Container parent,
			@NotNull final XdevList<Component> children, final boolean deep)
	{
		final int c = parent.getComponentCount();
		for(int i = 0; i < c; i++)
		{
			final Component child = parent.getComponent(i);
			children.add(child);

			if(deep && child instanceof Container)
			{
				addChildren((Container)child,children,deep);
			}
		}
	}


	/**
	 * Registers the provided {@link MouseListener} <code>mouseListener</code>
	 * at the desired <code>index</code> for the {@link Component}
	 * <code>cpn</code>.
	 *
	 * @param cpn
	 *            {@link Component} to register the {@link MouseListener} for
	 * @param mouseListener
	 *            {@link MouseListener} to register
	 * @param index
	 *            desired position of the {@link MouseListener}
	 */
	public static void insertMouseListener(final Component cpn, final MouseListener mouseListener,
			final int index)
	{
		final MouseListener ml[] = cpn.getMouseListeners();
		for(int i = 0; i < ml.length; i++)
		{
			cpn.removeMouseListener(ml[i]);
		}

		for(int i = 0; i < ml.length; i++)
		{
			if(index == i)
			{
				cpn.addMouseListener(mouseListener);
			}
			cpn.addMouseListener(ml[i]);
		}

		if(index > ml.length - 1)
		{
			cpn.addMouseListener(mouseListener);
		}
	}


	/**
	 * Unregisters all instances of the provided {@link MouseListener} class
	 * from the {@link Component} <code>cpn</code>.
	 *
	 * @param cpn
	 *            {@link Component} to unregister the {@link MouseListener} for
	 * @param type
	 *            class of the {@link MouseListener}s to unregister
	 */
	public static void removeMouseListener(final Component cpn,
			final Class<? extends MouseListener> type)
	{
		final MouseListener ml[] = cpn.getMouseListeners();
		for(int i = 0; i < ml.length; i++)
		{
			if(type.isInstance(ml[i]))
			{
				cpn.removeMouseListener(ml[i]);
			}
		}
	}


	/**
	 * Registers the provided {@link MouseMotionListener}
	 * <code>mouseMotionListener</code> at the desired <code>index</code> for
	 * the {@link Component} <code>cpn</code> .
	 *
	 * @param cpn
	 *            {@link Component} to register the {@link MouseMotionListener}
	 *            for
	 * @param mouseMotionListener
	 *            {@link MouseMotionListener} to register
	 * @param index
	 *            desired position of the {@link MouseMotionListener}
	 */
	public static void insertMouseMotionListener(final Component cpn,
			final MouseMotionListener mouseMotionListener, final int index)
	{
		final MouseMotionListener mml[] = cpn.getMouseMotionListeners();
		for(int i = 0; i < mml.length; i++)
		{
			cpn.removeMouseMotionListener(mml[i]);
		}

		for(int i = 0; i < mml.length; i++)
		{
			if(index == i)
			{
				cpn.addMouseMotionListener(mouseMotionListener);
			}
			cpn.addMouseMotionListener(mml[i]);
		}

		if(index > mml.length - 1)
		{
			cpn.addMouseMotionListener(mouseMotionListener);
		}
	}


	/**
	 * Unregisters all instances of the provided {@link MouseMotionListener}
	 * class from the {@link Component} <code>cpn</code>.
	 *
	 * @param cpn
	 *            {@link Component} to unregister the
	 *            {@link MouseMotionListener} for
	 * @param type
	 *            class of the {@link MouseMotionListener}s to unregister
	 */
	public static void removeMouseMotionListener(final Component cpn,
			final Class<? extends MouseMotionListener> type)
	{
		final MouseMotionListener mml[] = cpn.getMouseMotionListeners();
		for(int i = 0; i < mml.length; i++)
		{
			if(type.isInstance(mml[i]))
			{
				cpn.removeMouseMotionListener(mml[i]);
			}
		}
	}


	/**
	 * Creates and returns a new {@link ButtonGroup} containing the provided
	 * {@link AbstractButton} <code>buttons</code>.
	 *
	 * @param buttons
	 *            members of the new {@link ButtonGroup}
	 * @return a new {@link ButtonGroup} containing the provided
	 *         {@link AbstractButton} <code>buttons</code>.
	 */
	public static ButtonGroup createButtonGroup(@NotNull final AbstractButton... buttons)
	{
		final ButtonGroup group = new ButtonGroup();
		for(final AbstractButton button : buttons)
		{
			group.add(button);
		}

		return group;
	}


	/**
	 * Registers a keyboard shortcut for the specified <code>button</code>.
	 *
	 * @param button
	 *            to register the shortcut for
	 * @param keyStroke
	 *            {@link KeyStroke} to use a shortcut for the specified
	 *            <code>button</code>.
	 *
	 * @see KeyStroke
	 */
	public static void registerShortcut(final AbstractButton button, final KeyStroke keyStroke)
	{
		button.getActionMap().put("SHORTCUT",new AbstractAction()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				final ActionListener[] listener = button.getActionListeners();
				if(listener != null)
				{
					for(final ActionListener l : listener)
					{
						l.actionPerformed(e);
					}
				}
			}
		});
		button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke,"SHORTCUT");
	}


	/**
	 * Returns the {@link XdevWindow} of the specified {@link Window}
	 * <code>window</code>.
	 *
	 * @param window
	 *            to get the {@link XdevWindow} for
	 * @return the {@link XdevWindow} of the specified {@link Window}
	 *         <code>window</code> or <code>null</code> if the {@link Window}
	 *         does not contain a {@link XdevWindow}.
	 */
	public static XdevWindow getXdevWindowOf(final Window window)
	{
		if(window instanceof XdevRootPaneContainer)
		{
			return ((XdevRootPaneContainer)window).getXdevWindow();
		}

		return lookupComponentTree(window,new ComponentTreeVisitor<XdevWindow, XdevWindow>()
		{
			@Override
			public XdevWindow visit(final XdevWindow cpn)
			{
				return cpn;
			}
		},XdevWindow.class);
	}


	/**
	 * Returns the {@link GraphicsConfiguration} for the provided
	 * {@link Component} <code>cpn</code>.
	 *
	 * @param cpn
	 *            {@link Component} to get the {@link GraphicsConfiguration} for
	 * @return the {@link GraphicsConfiguration} for the provided
	 *         {@link Component} <code>cpn</code>.
	 */
	public static GraphicsConfiguration getGraphicsConfigurationOf(@NotNull final Component cpn)
	{
		GraphicsConfiguration g = cpn.getGraphicsConfiguration();

		if(g == null)
		{
			try
			{
				g = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
						.getDefaultConfiguration();
			}
			catch(final Exception e)
			{
			}
		}

		return g;
	}


	/**
	 * Returns <code>true</code> if <code>e</code> is a left double click;
	 * otherwise <code>false</code>.
	 *
	 * @param e
	 *            {@link MouseEvent} to be checked
	 * @return <code>true</code> if <code>e</code> is a left double click;
	 *         otherwise <code>false</code>.
	 */
	public static boolean isDoubleClick(final MouseEvent e)
	{
		return e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e);
	}


	/**
	 * Returns <code>true</code> if the mouse event specifies the left mouse
	 * button.
	 *
	 * @param e
	 *            a MouseEvent object
	 * @return <code>true</code> if the left mouse button was active
	 * @since 3.1
	 */
	public static boolean isLeftMouseButton(final MouseEvent e)
	{
		return SwingUtilities.isLeftMouseButton(e);
	}


	/**
	 * Returns <code>true</code> if the mouse event specifies the middle mouse
	 * button.
	 *
	 * @param e
	 *            a MouseEvent object
	 * @return <code>true</code> if the middle mouse button was active
	 * @since 3.1
	 */
	public static boolean isMiddleMouseButton(final MouseEvent e)
	{
		return SwingUtilities.isMiddleMouseButton(e);
	}


	/**
	 * Returns <code>true</code> if the mouse event specifies the right mouse
	 * button.
	 *
	 * @param e
	 *            a MouseEvent object
	 * @return <code>true</code> if the right mouse button was active
	 * @since 3.1
	 */
	public static boolean isRightMouseButton(final MouseEvent e)
	{
		return SwingUtilities.isRightMouseButton(e);
	}


	/**
	 * Registers listeners to the component to show the popup in the platform's
	 * specific manner.<br>
	 * <br>
	 * To register a popup to a JTree
	 * {@link TreeUtils#registerPopup(JTree, XdevMenu)} is the preferred way.
	 *
	 * @param cpn
	 * @param popup
	 */

	public static void registerPopup(final Component cpn, final XdevMenu popup)
	{
		if(cpn instanceof JTree)
		{
			TreeUtils.registerPopup((JTree)cpn,popup);
		}
		else
		{
			registerPopupHandler(cpn,new PopupHandler()
			{
				@Override
				public void showPopup(final EventObject e, final int x, final int y)
				{
					UIUtils.showPopup(popup,cpn,x,y);
				}
			});
		}
	}


	/**
	 * Registers listeners to the component to show the popup in the platform's
	 * specific manner.<br>
	 * <br>
	 * To register a popup to a JTree
	 * {@link TreeUtils#registerPopupHandler(JTree, xdev.ui.tree.TreePopupHandler)}
	 * is the preferred way.
	 *
	 * @param cpn
	 * @param handler
	 */

	public static void registerPopupHandler(final Component cpn, final PopupHandler handler)
	{
		cpn.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(final MouseEvent e)
			{
				mouseAction(e);
			}


			@Override
			public void mouseReleased(final MouseEvent e)
			{
				mouseAction(e);
			}


			private void mouseAction(final MouseEvent e)
			{
				if(e.isPopupTrigger())
				{
					handler.showPopup(e,e.getX(),e.getY());
				}
			}
		});

		cpn.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(final KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_CONTEXT_MENU)
				{
					final Point p = MouseInfo.getPointerInfo().getLocation();
					SwingUtilities.convertPointFromScreen(p,cpn);
					handler.showPopup(e,p.x,p.y);
				}
			}
		});
	}


	/**
	 * Invokes the popup at position <code>X</code> and <code>Y</code>.
	 * <p>
	 * Also selects the <code>Components</code> row or data related to the popup
	 * invocation location.
	 * </p>
	 *
	 * @param popup
	 *            the {@link XdevMenu} to show as popup.
	 * @param invoker
	 *            the <code>Component</code> which is related to the popup.
	 * @param x
	 *            popup <code>X</code> coordinate.
	 * @param y
	 *            popup <code>Y</code> coordinate.
	 */
	private static void showPopup(final XdevMenu popup, final Component invoker, final int x,
			final int y)
	{
		invoker.requestFocus();

		// selectes the row related to the popup invocation location
		if(invoker instanceof PopupRowSelectionHandler)
		{
			final PopupRowSelectionHandler selectionManager = (PopupRowSelectionHandler)invoker;
			selectionManager.handlePopupRowSelection(x,y);
		}

		popup.show(invoker,x,y);
	}


	/**
	 * Scrolls to the visible start of the component, e.g. the first line of a
	 * multiline text component.
	 *
	 * @param cpn
	 * @since 4.0
	 */
	public static void scrollToStart(final JComponent cpn)
	{
		final Rectangle r;
		if(cpn.getComponentOrientation().isLeftToRight())
		{
			r = new Rectangle(0,0,1,1);
		}
		else
		{
			r = new Rectangle(cpn.getWidth() - 1,0,1,1);
		}

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				cpn.scrollRectToVisible(r);
			}
		});
	}


	/**
	 * Ensures that the runnable is executed in the Java FX thread.
	 *
	 * @since 5.0
	 */
	public static void runInJFXThread(final Runnable runnable)
	{
		if(Platform.isFxApplicationThread())
		{
			runnable.run();
		}
		else
		{
			Platform.runLater(runnable);
		}
	}


	/**
	 * Ensures that the runnable is executed in the Java Swing thread.
	 *
	 * @since 5.0
	 */
	public static void runInSwingThread(final Runnable runnable)
	{
		if(SwingUtilities.isEventDispatchThread())
		{
			runnable.run();
		}
		else
		{
			SwingUtilities.invokeLater(runnable);
		}
	}
}
