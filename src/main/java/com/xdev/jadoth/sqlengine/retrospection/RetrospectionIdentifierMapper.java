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
package com.xdev.jadoth.sqlengine.retrospection;

import java.util.List;




/**
 * The Interface RetrospectionIdentifierMapper.
 */
public interface RetrospectionIdentifierMapper
{
	/**
	 * Map identifier.
	 * 
	 * @param identifier the identifier
	 * @return the string
	 */
	public String mapIdentifier(String identifier);

	/** The TOLOWERCASE. */
	public final RetrospectionIdentifierMapper TOLOWERCASE = new RetrospectionIdentifierMapper() {
		@Override
		public String mapIdentifier(final String identifier) {
			return identifier.toLowerCase();
		}
	};

	/** The TOUPPERCASE. */
	public final RetrospectionIdentifierMapper TOUPPERCASE = new RetrospectionIdentifierMapper() {
		@Override
		public String mapIdentifier(final String identifier) {
			return identifier.toUpperCase();
		}
	};

	/** The TOCAPITALIZED. */
	public final RetrospectionIdentifierMapper TOCAPITALIZED = new RetrospectionIdentifierMapper() {
		@Override
		public String mapIdentifier(final String identifier) {
			return Character.toUpperCase(identifier.charAt(0))+identifier.substring(1);
		}
	};


	/**
	 * The Class Util.
	 */
	public class Util
	{
		
		/**
		 * Apply identifier mappers.
		 * 
		 * @param identifier the identifier
		 * @param mapperCollections the mapper collections
		 * @return the string
		 */
		public static String applyIdentifierMappers(
			final String identifier,
			final List<RetrospectionIdentifierMapper>... mapperCollections
		)
		{
			if(identifier == null || mapperCollections == null) return null;

			String modifiedIdentifier = identifier;
			for (List<RetrospectionIdentifierMapper> mapperCollection : mapperCollections) {
				for (RetrospectionIdentifierMapper mapper : mapperCollection) {
					modifiedIdentifier = mapper.mapIdentifier(modifiedIdentifier);
				}
			}
			return modifiedIdentifier;
		}
	}
}
