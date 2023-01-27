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
package com.xdev.jadoth.lang.functional.controlflow;

import com.xdev.jadoth.lang.functional.Operation;
import com.xdev.jadoth.lang.signalthrows.ThrowBreak;
import com.xdev.jadoth.lang.signalthrows.ThrowContinue;
import com.xdev.jadoth.lang.signalthrows.ThrowReturn;


/**
 * {@link Operation} that utilizes branching throws like {@link ThrowBreak}, {@link ThrowContinue}, {@link ThrowReturn}.
 *
 * @see Operation
 * @author Thomas Muenz
 *
 */
public interface TOperation<T>
{
	public void execute(T e) throws ThrowBreak, ThrowContinue, ThrowReturn;
}
