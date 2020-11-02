package xdev.lang;

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


import java.lang.annotation.*;


/**
 * A marker annotation for delegate methods of event handlers.
 * <p>
 * This class is used for example by visual user interface designers.
 * </p>
 * Sample:
 * 
 * <pre>
 * ...
 * 
 * JButton cmdOK;
 * 
 * ...
 * 
 * cmdOK.addActionListener(new ActionListener(){
 * 	public void actionPerformed(ActionEvent e){
 * 		cmdOK_actionPerformed(e);
 * 	}
 * });
 * 
 * ...
 * 
 * &#64;EventHandlerDelegate void cmdOK_actionPerformed(ActionEvent e){
 * 	// do stuff
 * }
 * 
 * ...
 * </pre>
 * 
 * 
 * @author XDEV Software Corp.
 */

@Target(ElementType.METHOD)
public @interface EventHandlerDelegate
{
}
