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
package xdev.vt;


import xdev.vt.EntityRelationship.Entity;


/**
 * An enum specifying the possible cardinalities for entities within a
 * {@link EntityRelationship}.
 * 
 * @author TF
 * @see EntityRelationship
 * @see Entity
 */
public enum Cardinality
{
	/**
	 * The entities on the other side of the relation can reference at most one
	 * entity with this Cardinality.
	 */
	ONE("1"),

	/**
	 * The entities on the other side of the relation may reference 0 to many
	 * entites with this Cardinality.
	 */
	MANY("n");
	
	/**
	 * Returns the enum value for a specified shortcut.
	 * 
	 * @param shortcut
	 *            a valid shortcurt for a {@link Cardinality} value.
	 * @return a {@link Cardinality}
	 * @throws IllegalArgumentException
	 *             if an invalid <code>shortcut</code> was used.
	 */
	public static Cardinality get(String shortcut)
	{
		for(Cardinality c : values())
		{
			if(c.shortcut.equals(shortcut))
			{
				return c;
			}
		}
		
		throw new IllegalArgumentException("Cardinality '" + shortcut + "' not found");
	}
	
	private String	shortcut;
	

	private Cardinality(String shortcut)
	{
		this.shortcut = shortcut;
	}
	

	/**
	 * Returns the shortcut for this enum value.
	 * 
	 * @return the shortcut
	 */
	public String getShortcut()
	{
		return shortcut;
	}
	

	/**
	 * Returns a string representation of this enum value, consisting of the
	 * shortcut.
	 * 
	 * @return the shortcut
	 */
	@Override
	public String toString()
	{
		return shortcut;
	}
}
