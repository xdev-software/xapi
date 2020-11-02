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


import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;


/**
 * A component to embed JavaFX content into Swing applications. The content to
 * be displayed is specified with the {@link #setScene} method that accepts an
 * instance of JavaFX {@code Scene}. After the scene is assigned, it gets
 * repainted automatically. All the input and focus events are forwarded to the
 * scene transparently to the developer.
 * <p>
 * There are some restrictions related to this component. As a Swing component,
 * it should only be accessed from the event dispatch thread, except the
 * {@link #setScene} method, which can be called either on the event dispatch
 * thread or on the JavaFX application thread.
 *
 * @author XDEV Software
 * @since 5.0
 *
 */
public class XdevJFXPanel extends JFXPanel
{
	/**
	 * Creates a new {@code XdevJFXPanel} object.
	 * <p>
	 * <b>Implementation note</b>: when the first {@code XdevJFXPanel} object is
	 * created, it implicitly initializes the JavaFX runtime. This is the
	 * preferred way to initialize JavaFX in Swing.
	 */
	public XdevJFXPanel()
	{
	}


	/**
	 * Attaches a {@code Scene} object to display in this {@code
	 * XdevJFXPanel}. This method can be called either on the event dispatch
	 * thread or the JavaFX application thread.
	 *
	 * @param newScene
	 *            a scene to display in this {@code XdevJFXpanel}
	 */
	@Override
	public void setScene(final Scene newScene)
	{
		super.setScene(newScene);
	}
}
