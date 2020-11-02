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
import java.util.logging.Level;
import java.util.logging.Logger;

import xdev.lang.NotNull;


/**
 * {@link XdevLoggerImpl} is the reference implementation of {@link XdevLogger}.
 * 
 * For information regarding logger configuration take a look at {@link LoggingConfiguration}.
 * 
 * <p>
 * Instances of this class are immutable.
 * </p>
 * 
 * @author XDEV Software (FH)
 * @author XDEV Software (RHHF)
 * 
 */
public class XdevLoggerImpl implements XdevLogger
{
	
	/**
	 * {@link Logger} instance to back this {@link XdevLoggerImpl}.
	 */
	private final Logger	logger;
	

	/**
	 * Creates a new {@link XdevLoggerImpl} instance utilizing the passed
	 * {@link Logger} instance.
	 * 
	 * @param logger
	 *            {@link Logger} instance to back this {@link XdevLoggerImpl}.
	 */
	public XdevLoggerImpl(final @NotNull Logger logger)
	{
		if(logger == null)
		{
			throw new IllegalArgumentException("logger must not be null");
		}
		this.logger = logger;
	}
	

	@Override
	public void error(final Throwable thrown)
	{
		error(thrown,null);
	}
	

	@Override
	public void error(final Throwable thrown, final String msg)
	{
		log(Level.SEVERE,msg,thrown);
	}
	

	@Override
	public void error(final String msg, final Object... args)
	{
		log(Level.SEVERE,msg,args);
	}
	

	@Override
	public void warning(final Throwable thrown)
	{
		warning(thrown,null);
	}
	

	@Override
	public void warning(final Throwable thrown, final String msg)
	{
		log(Level.WARNING,msg,thrown);
	}
	

	@Override
	public void warning(final String msg, final Object... args)
	{
		log(Level.WARNING,msg,args);
	}
	

	@Override
	public void info(final Throwable thrown)
	{
		info(thrown,null);
	}
	

	@Override
	public void info(final Throwable thrown, final String msg)
	{
		log(Level.INFO,msg,thrown);
	}
	

	@Override
	public void info(final String msg, final Object... args)
	{
		log(Level.INFO,msg,args);
	}
	

	@Override
	public void info(final String msg)
	{
		log(Level.INFO,msg);
	}
	

	@Override
	public boolean isDebugEnabled()
	{
		return this.logger.isLoggable(Level.FINE);
	}
	

	@Override
	public boolean isErrorEnabled()
	{
		return this.logger.isLoggable(Level.SEVERE);
	}
	

	@Override
	public boolean isWarningEnabled()
	{
		return this.logger.isLoggable(Level.WARNING);
	}
	

	@Override
	public boolean isInfoEnabled()
	{
		return this.logger.isLoggable(Level.INFO);
	}
	

	@Override
	public void debug(final Throwable thrown)
	{
		debug(thrown,null);
	}
	

	@Override
	public void debug(final Throwable thrown, final String msg)
	{
		log(Level.FINE,msg,thrown);
	}
	

	@Override
	public void debug(final String msg, final Object... args)
	{
		log(Level.FINE,msg,args);
	}
	

	@Override
	public void debug(final String msg)
	{
		log(Level.FINE,msg);
	}
	

	@Override
	public void warning(final String msg)
	{
		log(Level.WARNING,msg);
	}
	

	@Override
	public void error(final String msg)
	{
		log(Level.SEVERE,msg);
	}
	

	/**
	 * Log a message at the provided level according to the specified message
	 * and arguments.
	 * 
	 * <p>
	 * The arguments will be integrated using the {@link MessageFormat} syntax.
	 * </p>
	 * 
	 * @param level
	 *            {@link Level} to log on
	 * 
	 * @param msg
	 *            the format string
	 * @param args
	 *            an array of arguments
	 * 
	 * @see MessageFormat
	 */
	protected void log(final Level level, final String msg, final Object... args)
	{
		log0(level,msg,null,args);
	}
	

	/**
	 * Log an exception (throwable) at the provided level with an accompanying
	 * message.
	 * 
	 * @param level
	 *            {@link Level} to log on
	 * 
	 * @param msg
	 *            the message accompanying the exception
	 * @param ex
	 *            the exception (throwable) to log
	 * 
	 */
	protected void log(final Level level, final String msg, final Throwable ex)
	{
		log0(level,msg,ex,(Object[])null);
	}
	

	/**
	 * Log an exception (throwable) at the provided level with an accompanying
	 * message and arguments.
	 * 
	 * <p>
	 * The arguments will be integrated using the {@link MessageFormat} syntax.
	 * </p>
	 * 
	 * @param level
	 *            {@link Level} to log on
	 * 
	 * @param msg
	 *            the message accompanying the exception
	 * @param ex
	 *            the exception (throwable) to log
	 * 
	 * @param args
	 *            an array of arguments
	 * 
	 */
	protected void log0(final Level level, String msg, final Throwable ex, final Object... args)
	{
		Logger logger = getLogger();
		if(logger.isLoggable(level) && !isBlacklisted(ex))
		{
			if(msg == null)
			{
				msg = "";
			}
			
			StackTraceElement locations[] = new Throwable().getStackTrace();
			
			String sourceClass = "[unknown]";
			String sourceMethod = "[unknown]";
			if(locations != null && locations.length > 3)
			{
				StackTraceElement caller = locations[3];
				sourceClass = caller.getClassName();
				sourceMethod = caller.getMethodName();
			}
			
			if(ex != null)
			{
				logger.logp(level,sourceClass,sourceMethod,msg,ex);
			}
			else if(args != null && args.length > 0)
			{
				logger.logp(level,sourceClass,sourceMethod,msg,args);
				
			}
			else
			{
				logger.logp(level,sourceClass,sourceMethod,msg);
			}
		}
	}
	

	/**
	 * Checks if the provided {@link Throwable} t is blacklisted (= should not
	 * be logged).
	 * 
	 * <p>
	 * This method is used to filter JRE bugs/messages with no effect.
	 * </p>
	 * 
	 * @param t
	 *            {@link Throwable} to check
	 * @return <code>true</code> if the provided {@link Throwable} is
	 *         blacklisted (= should not be logged); otherwise false.
	 */
	protected boolean isBlacklisted(final Throwable t)
	{
		
		/*
		 * (07.09.2012 RHHF): Disabled, because loggers/levels can be configured
		 * now
		 */
		return false;
		// final String KEYBOARD_FOCUS_MANAGER =
		// "this KeyboardFocusManager is not installed in the current thread's context";
		// return(t instanceof SecurityException &&
		// t.getMessage().equals(KEYBOARD_FOCUS_MANAGER));
		
	}
	

	// /**
	// * Sets {@link Logger} instance that backs this {@link XdevLogger}.
	// *
	// * @param logger
	// * {@link Logger} instance that backs this {@link XdevLogger}.
	// */
	// public void setLogger(final @NotNull Logger logger)
	// {
	// if(logger == null)
	// {
	// throw new IllegalArgumentException("logger must not be null");
	// }
	// this.logger = logger;
	// }
	
	@Override
	public Logger getLogger()
	{
		return logger;
	}
	
}
