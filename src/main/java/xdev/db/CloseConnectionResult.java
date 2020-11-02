package xdev.db;

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


/**
 * A delegating result which closes the used connection when it is closed
 * itself.
 * 
 * @author XDEV Software
 * @since 3.1
 */
public class CloseConnectionResult extends DelegatingResult
{
	private final DBConnection	connection;
	

	/**
	 * Creates a new close connection result
	 * 
	 * @param delegate
	 *            the original result
	 * @param connection
	 *            the connection to close
	 */
	public CloseConnectionResult(Result delegate, DBConnection connection)
	{
		super(delegate);
		this.connection = connection;
	}
	

	@Override
	public void close() throws DBException
	{
		try
		{
			super.close();
		}
		finally
		{
			connection.close();
		}
	}
}
