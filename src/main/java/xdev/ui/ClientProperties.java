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


import javax.swing.JComponent;


/**
 * Constant interface for component's client properties.
 * 
 * @author XDEV Software
 */
public interface ClientProperties
{
	/**
	 * Additional data of a component
	 * 
	 * @deprecated use {@link JComponent#putClientProperty(Object, Object)},
	 *             {@link JComponent#getClientProperty(Object)}
	 */
	@Deprecated
	String	TAG_DATA						= "TAG_DATA";
	
	/**
	 * @deprecated use {@link FormularComponent#getDataField()}
	 */
	String	DATA_FIELD						= "DATA_FIELD";
	
	/**
	 * @see FormularVerifier
	 * @deprecated use {@link Validator}s instead
	 */
	@Deprecated
	String	FORMULAR_VERIFIER				= "FORMULAR_VERIFIER";
	
	/**
	 * @deprecated use {@link #FORMULAR_FILTER_OPERATOR}
	 */
	@Deprecated
	String	FORMULAR_CONDITION_FILTER		= "FORMULAR_CONDITION_FILTER";
	
	/**
	 * @deprecated use {@link #FORMULAR_FILTER_OPERATOR}
	 */
	@Deprecated
	String	FORMULAR_CONDITION_PRECISION	= "FORMULAR_CONDITION_PRECISION";
	
	/**
	 * @deprecated use {@link FormularComponent#getFilterOperator()} and
	 *             {@link FormularComponent#setFilterOperator(xdev.db.Operator)}
	 */
	@Deprecated
	String	FORMULAR_FILTER_OPERATOR		= "FILTER_CONDITION";
	
	/**
	 * Used for {@link FormularComponent}s which are part of another
	 * {@link FormularComponent} and shouldn't be traversed
	 * 
	 * @since 3.1
	 */
	String	FORMULAR_SKIP					= "FORMULAR_SKIP";
}
