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
package xdev.ui.persistence;


/**
 * Components that implement this interface are capable of persisting state over
 * multiple invocations of a application.
 * <p>
 * The implementing component is responsible for generating a {@code String}
 * representation of the persistent information for saving the state and to
 * interpret the same information when restoring.
 * </p>
 * <p>
 * The storage mechanism itself is transparent to the {@code Component}.
 * </p>
 * <p>
 * The component has to return a persistent id for uniquely identifying its
 * state across multiple application runs.
 * </p>
 * 
 * @author XDEV Software Corp.
 * @since 3.0
 */
public interface Persistable
{
	/**
	 * This String should be used by {@link Persistable} components to separate
	 * multiple persistent values from each other.
	 */
	public static final String	VALUE_SEPARATOR	= "~";
	

	/**
	 * Restores this {@code Components} persistent state.
	 * <p>
	 * By calling this method, the passed state information get interpreted and
	 * applied on the affected attributes.
	 * </p>
	 * 
	 * @param persistentState
	 *            {@code String} representation for the persistent state of the
	 *            {@code Component}.
	 */
	public void loadPersistentState(String persistentState);
	

	/**
	 * Generates a {@code String} representation of this {@code Components}
	 * persistent state and returns it for being stored.
	 * 
	 * @return a {@code String} containing persistent state
	 */
	public String savePersistentState();
	

	/**
	 * Return a persistent id for this {@code Component}.
	 * <p>
	 * The following requirements have to be met for the persistent id:
	 * <ul>
	 * <li>it has to be unique within the current application</li>
	 * <li>it has to be indentical across multiple invocations of the
	 * application</li>
	 * </ul>
	 * 
	 * @return a persistent id
	 */
	public String getPersistentId();
	

	/**
	 * Flag, indicates, if the gui state for this component should be persisted.
	 * 
	 * @return {@code true} if a instance of type {@link Persistable} is to be
	 *         persisted.
	 */
	public boolean isPersistenceEnabled();
	
}
