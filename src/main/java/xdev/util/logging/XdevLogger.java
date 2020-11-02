package xdev.util.logging;

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


import java.text.MessageFormat;
import java.util.logging.Logger;


/**
 * {@link XdevLogger} is a thin wrapper around the java.util.logging
 * package. It provides many shortcuts for everyday logging situations.
 * 
 * For information regarding logger configuration take a look at {@link LoggingConfiguration}.
 *
 * @author XDEV Software (RHHF)
 * 
 */
public interface XdevLogger
{
	
	/**
	 * Log an exception (throwable) at the ERROR (SEVERE) level.
	 * 
	 * 
	 * @param thrown
	 *            the exception (throwable) to log
	 * 
	 * 
	 */
	public void error(final Throwable thrown);
	

	/**
	 * Log an exception (throwable) at the ERROR (SEVERE) level with an accompanying
	 * message.
	 * 
	 * @param msg
	 *            the message accompanying the exception
	 * @param thrown
	 *            the exception (throwable) to log
	 * 
	 */
	public void error(final Throwable thrown, final String msg);
	

	/**
	 * Log a message at the ERROR (SEVERE) level according to the specified message and
	 * arguments.
	 * 
	 * <p>
	 * The arguments will be integrated using the {@link MessageFormat} syntax.
	 * </p>
	 * 
	 * 
	 * @param msg
	 *            the format string
	 * @param args
	 *            an array of arguments
	 * 
	 * @see MessageFormat
	 */
	public void error(final String msg, final Object... args);
	

	/**
	 * Log an exception (throwable) at the WARNING level.
	 * 
	 * 
	 * @param thrown
	 *            the exception (throwable) to log
	 * 
	 * 
	 */
	public void warning(final Throwable thrown);
	

	/**
	 * Log an exception (throwable) at the WARNING level with an accompanying
	 * message.
	 * 
	 * @param msg
	 *            the message accompanying the exception
	 * @param thrown
	 *            the exception (throwable) to log
	 * 
	 */
	public void warning(final Throwable thrown, final String msg);
	

	/**
	 * Log a message at the WARNING level according to the specified message and
	 * arguments.
	 * 
	 * <p>
	 * The arguments will be integrated using the {@link MessageFormat} syntax.
	 * </p>
	 * 
	 * 
	 * @param msg
	 *            the format string
	 * @param args
	 *            an array of arguments
	 * 
	 * @see MessageFormat
	 */
	public void warning(final String msg, final Object... args);
	

	/**
	 * Log an exception (throwable) at the INFO level.
	 * 
	 * 
	 * @param thrown
	 *            the exception (throwable) to log
	 * 
	 * 
	 */
	public void info(final Throwable thrown);
	

	/**
	 * Log an exception (throwable) at the WARNING level with an accompanying
	 * message.
	 * 
	 * @param msg
	 *            the message accompanying the exception
	 * @param thrown
	 *            the exception (throwable) to log
	 * 
	 */
	public void info(final Throwable thrown, final String msg);
	

	/**
	 * Log a message at the INFO level according to the specified message and
	 * arguments.
	 * 
	 * <p>
	 * The arguments will be integrated using the {@link MessageFormat} syntax.
	 * </p>
	 * 
	 * 
	 * @param msg
	 *            the format string
	 * @param args
	 *            an array of arguments
	 * 
	 * @see MessageFormat
	 */
	public void info(final String msg, final Object... args);
	

	/**
	 * Log a message at the INFO level.
	 * 
	 * @param msg
	 *            the message to log
	 * 
	 */
	public void info(final String msg);
	

	/**
	 * Is the logger instance enabled for the FINE level?
	 * 
	 * @return <code>true</code> if this Logger is enabled for the FINE level,
	 *         <code>false</code> otherwise.
	 * 
	 */
	public abstract boolean isDebugEnabled();
	

	/**
	 * Is the logger instance enabled for the ERROR level?
	 * 
	 * @return <code>true</code> if this Logger is enabled for the ERROR level,
	 *         <code>false</code> otherwise.
	 * 
	 */
	public abstract boolean isErrorEnabled();
	

	/**
	 * Is the logger instance enabled for the WARNING level?
	 * 
	 * @return <code>true</code> if this Logger is enabled for the WARNING
	 *         level, <code>false</code> otherwise.
	 * 
	 */
	public abstract boolean isWarningEnabled();
	

	/**
	 * Is the logger instance enabled for the INFO level?
	 * 
	 * @return <code>true</code> if this Logger is enabled for the INFO level,
	 *         <code>false</code> otherwise.
	 * 
	 */
	public abstract boolean isInfoEnabled();
	

	/**
	 * Log an exception (throwable) at the FINE level.
	 * 
	 * 
	 * @param thrown
	 *            the exception (throwable) to log
	 * 
	 * 
	 */
	public void debug(final Throwable thrown);
	

	/**
	 * Log an exception (throwable) at the FINE level with an accompanying
	 * message.
	 * 
	 * @param msg
	 *            the message accompanying the exception
	 * @param thrown
	 *            the exception (throwable) to log
	 * 
	 */
	public void debug(final Throwable thrown, final String msg);
	

	/**
	 * Log a message at the FINE level according to the specified message and
	 * arguments.
	 * 
	 * <p>
	 * The arguments will be integrated using the {@link MessageFormat} syntax.
	 * </p>
	 * 
	 * 
	 * @param msg
	 *            the format string
	 * @param args
	 *            an array of arguments
	 * 
	 * @see MessageFormat
	 */
	public void debug(final String msg, final Object... args);
	

	/**
	 * Log a message at the FINE level.
	 * 
	 * @param msg
	 *            the message to log
	 * 
	 */
	public void debug(final String msg);
	

	/**
	 * Log a message at the WARNING level.
	 * 
	 * @param msg
	 *            the message to log
	 * 
	 */
	public void warning(final String msg);
	

	/**
	 * Log a message at the ERROR (SEVERE) level.
	 * 
	 * @param msg
	 *            the message to log
	 * 
	 */
	public void error(final String msg);
	

	/**
	 * Returns {@link Logger} instance that backs this {@link XdevLoggerImpl}.
	 * 
	 * @return {@link Logger} instance that backs this {@link XdevLoggerImpl}.
	 */
	public abstract Logger getLogger();
	
}
