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

import xdev.db.Operator;


/**
 * This class is deprecated. Use {@link Operator} instead.
 */

@Deprecated
public class FormularCondition
{
	public final static Integer	INCLUSIVE		= new Integer(0);
	public final static Integer	EXCLUSIVE		= new Integer(1);
	
	public final static Integer	EXACT			= new Integer(0);
	public final static Integer	LIKE_LEFT_RIGHT	= new Integer(1);
	public final static Integer	LIKE_RIGHT		= new Integer(2);
	public final static Integer	LIKE_LEFT		= new Integer(3);
}
