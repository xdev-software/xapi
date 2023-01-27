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
package com.xdev.jadoth.math;
/**
 * 
 */


/**
 * @author Thomas Muenz
 *
 */
public enum DirectionCardinal
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	
	NORTH_WEST(1<<4), NORTH(1<<0), NORTH_EAST(1<<5),
	
	WEST      (1<<3), CENTER(0),   EAST      (1<<1),
	   
	SOUTH_WEST(1<<7), SOUTH(1<<2), SOUTH_EAST(1<<6)
	;

	
	
	private static final DirectionCardinal[][] MATRIX = {		
		{NORTH_WEST, NORTH,  NORTH_EAST},
		{WEST,       CENTER, EAST      },
		{SOUTH_WEST, SOUTH,  SOUTH_EAST}
	};
	
	
	
	/**
	 * 
	 * @param difX a value in {-1, 0, +1}
	 * @param difY a value in {-1, 0, +1}
	 * @return the direction <code>difX</code> or <code>difY</code> represent or <tt>null</tt> otherweise.
	 * @throws IllegalArgumentException if <code>(difX, difY)</code> does not point to a neighbor square
	 */
	public static final DirectionCardinal translateNeighborVector(final int difX, final int difY)
		throws IllegalArgumentException
	{
		if(difX == 0 && difY == 0 || Math.abs(difX) != 1 && Math.abs(difY) != 1){
			throw new IllegalArgumentException("Vector does not point to a neighbor: ("+difX+','+difY+')');
		}
		return MATRIX[difY+1][difX+1];
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	
	private final int directionBit; 
	private transient DirectionCardinal cachedOpposite = null;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	private DirectionCardinal(final int bit)
	{
		this.directionBit = bit;
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	
	public int getBit()
	{
		return this.directionBit;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	
	private final DirectionCardinal determineOpposite()
	{
		switch(this) {
			case NORTH_WEST: return SOUTH_EAST;
			case NORTH:      return SOUTH;
			case NORTH_EAST: return SOUTH_WEST;
			case WEST:       return EAST;
			case EAST:       return WEST;
			case SOUTH_WEST: return NORTH_EAST;
			case SOUTH:      return NORTH;
			case SOUTH_EAST: return NORTH_WEST;
			case CENTER:     return CENTER;
			default:
				//can never occur unless someone adds a direction (?) without modifying this switch
				throw new RuntimeException("Unknown "+DirectionCardinal.class.getSimpleName()+": "+this);
		}
	}
	
	public DirectionCardinal opposite()
	{
		if(this.cachedOpposite == null){
			this.cachedOpposite = this.determineOpposite();
		}
		return this.cachedOpposite;
	}
	
	public final boolean isSet(final int bits)
	{
		return (bits & this.directionBit) > 0;
	}	
	
	public final boolean isMainDirection()
	{
		final int bit = this.directionBit;
		return bit != 0 && bit <= WEST.directionBit;
	}
	public final boolean isIntermediateDirection()
	{
		final int bit = this.directionBit;
		return bit != 0 && bit > WEST.directionBit;
	}
	
}
