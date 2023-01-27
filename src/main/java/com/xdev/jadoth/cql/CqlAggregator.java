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
package com.xdev.jadoth.cql;

import com.xdev.jadoth.collections.Collecting;
import com.xdev.jadoth.lang.Equalator;

/**
 * @author Thomas Muenz
 *
 */
public interface CqlAggregator<E, P, C extends Collecting<E>> extends CqlProjector<E, P, C>
{		
	public boolean aggregate(E element, Equalator<E> equalator);
	
	public CqlAggregator<E, P, C> reset();
	
}
