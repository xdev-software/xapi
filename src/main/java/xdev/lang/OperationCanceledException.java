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


/**
 * A control flow exception to end processes like a visit of a complex
 * structure.
 * 
 * @author XDEV Software
 * @since 3.0
 */
public class OperationCanceledException extends RuntimeException
{
	private static final long	serialVersionUID	= 1L;

	private boolean cancelMultipleOperations = false;
	
	/**
	 * Constructs a new operation canceled exception.
	 */
	public OperationCanceledException()
	{
		super();
	}


	/**
	 * Constructs a new operation canceled exception with a specific detail
	 * message.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public OperationCanceledException(String message)
	{
		super(message);
	}


	/**
	 * Constructs a new operation canceled exception with the specified cause.
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public OperationCanceledException(Throwable cause)
	{
		super(cause);
	}


	/**
	 * Constructs a new operation canceled exception with the specified detail
	 * message and cause.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public OperationCanceledException(String message, Throwable cause)
	{
		super(message,cause);
	}


	/**
	 * By setting this flag to true, the exception is configured to cancel multiple operations in a work flow.
	 * <p>
	 * This can be useful, when a operation is cancelled within a iterative process.
	 * The executing code catching this exception can then determine, if only one iteration or the whole iterative process should be cancelled.
	 * </p>
	 * <p>
	 * this value defaults to false
	 * </p>
	 * @param cancelMultipleOperations if set to {@code true} the whole process will be cancelled.
	 */
	public void setCancelMultipleOperations(boolean cancelMultipleOperations)
	{
		this.cancelMultipleOperations = cancelMultipleOperations;
	}

	/**
	 * Returns, if only one iteration or a whole iterative process should be cancelled.
	 * @return {@code true}, if the whole process should be cancelled.
	 */
	public boolean isCancelMultipleOperations()
	{
		return cancelMultipleOperations;
	}
	
}
