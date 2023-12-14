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
package com.xdev.jadoth.collections;

import java.util.RandomAccess;

/**
 * Extended List interface with a ton of badly needed list operations like distinction between identity and equality
 * element comparison, operation range specification, higher order (functional) operations, proper toArray() methods,
 * etc.<br>
 * <br>
 * All {@link XList} implementations have to have {@link RandomAccess} behaviour.<br>
 * Intelligent implementations make non-random-access implementations like simple linked lists obsolete.
 *
 * @author Thomas Muenz
 *
 */
public interface XList<E>
extends
XCollection<E>,
XSettingList<E>,
XAddingList<E>,
XInsertingList<E>,
XRemovingList<E>
{
	public interface Factory<E>
	extends
	XCollection.Factory<E>,
	XSettingList.Factory<E>,
	XAddingList.Factory<E>,
	XInsertingList.Factory<E>,
	XRemovingList.Factory<E>
	{
		@Override
		public XList<E> createInstance();
	}

	
	public SubList<E> subList(int fromIndex, int toIndex);

}


/*

Element Access Definitions

Different situations require different behaviour/possibilities/characteristics of List implementations as to 
element access types.

The following access types can be reasonable distinguished:
1.) get:     getting one or more elements from the list
2.) set:     setting one or more elements to already existing buckets in the list
3.) add:     adding new elements with new buckets to the end of the list
4.) insert:  adding new elements with new buckets to any point before the end of the list, thus shifting all elements 
             from that point until the end of the list to the right
5.) remove:  removing one or more elements including their buckets from any point in the list, thus shifting all 
             lements from that point until the end of the list to the left
6.) growing: the number of element buckets can be increased beyond the initial value if necessary


the following table shows which List implementation class has what combination of these element access types.
Note that "GrowList" is just a more fancier and shorter name for "GeneralPuposeList".

implementation class |   get  |   set  |   add  | remove | insert | growing
GrowList                  v        v        v        v        v        v
LimitedList               v        v        v        v        v
FixedList                 v        v
ConstList                 v					



Element Access Wrapper Classes

Element access wrapper classes are List wrapper classes around other List classes (or Arrays) that retroactively 
restrict certain element access types of their wrapped instance. This is usefull for example to being able to 
internally add, remove, etc. element to a list but allow external access to this list only in a limited way. 
Also, array wrapper classes allow using arrays is if they were List implementations with all the functionality 
a List provides.
						
						
wrapper class        |   get  |   set  |   add  | remove | insert | growing					
ListAccessor              v        v	
ListView                  v					

ArrayAccessor             v        v				
ArrayView                 v		




Other Wrapper Classes

Apart from element access wrapper classes, there are other wrapper classes that fulfill usefull roles.


SubList
List implementations that provide access only to a defined window of another list instance.

Weaklist
List implementation that wraps every element in a WeapReference that in turn is stored in a wrapped List<WeakReference<E>> implementation

*/
