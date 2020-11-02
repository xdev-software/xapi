package xdev.util;

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


import xdev.lang.Copyable;


/**
 * A Duration holds an specific amount of time in milliseconds.
 * <p>
 * This class is immutable.
 * 
 * @see XdevDate#getDurationSince(Object)
 * @see XdevDate#getDurationUntil(Object)
 * @see XdevDate#rollBack(Duration)
 * @see XdevDate#rollForward(Duration)
 * 
 * @author XDEV Software
 */

public class Duration implements Copyable<Duration>, Comparable<Duration>
{
	/**
	 * Creates a default duration with the specified amount of seconds.
	 * 
	 * @param seconds
	 *            The past seconds
	 * @return A new duration
	 */
	public static Duration seconds(int seconds)
	{
		return new Duration(seconds * MILLIS_PER_SECOND);
	}
	

	/**
	 * Creates a default duration with the specified amount of minutes.
	 * 
	 * @param minutes
	 *            The past minutes
	 * @return A new duration
	 */
	public static Duration minutes(int minutes)
	{
		return new Duration(minutes * MILLIS_PER_MINUTE);
	}
	

	/**
	 * Creates a default duration with the specified amount of hours.
	 * 
	 * @param hours
	 *            The past hours
	 * @return A new duration
	 */
	public static Duration hours(int hours)
	{
		return new Duration(hours * MILLIS_PER_HOUR);
	}
	

	/**
	 * Creates a default duration with the specified amount of days.
	 * 
	 * @param days
	 *            The past days
	 * @return A new duration
	 */
	public static Duration days(int days)
	{
		return new Duration(days * MILLIS_PER_DAY);
	}
	
	/**
	 * Milliseconds per second = 1000.
	 */
	public final static long	MILLIS_PER_SECOND	= 1000L;
	
	/**
	 * Milliseconds per minute = 1000 * 60.
	 */
	public final static long	MILLIS_PER_MINUTE	= MILLIS_PER_SECOND * 60L;
	
	/**
	 * Milliseconds per hour = 1000 * 60 * 60.
	 */
	public final static long	MILLIS_PER_HOUR		= MILLIS_PER_MINUTE * 60L;
	
	/**
	 * Milliseconds per hour = 1000 * 60 * 60 * 24.
	 */
	public final static long	MILLIS_PER_DAY		= MILLIS_PER_HOUR * 24L;
	
	private final long			millis;
	

	/**
	 * Creates a duraction with the specified time in milliseconds.
	 * 
	 * @param millis
	 *            The duration in milliseconds
	 */
	public Duration(long millis)
	{
		if(millis < 0)
		{
			throw new IllegalArgumentException("No negative time span possible");
		}
		
		this.millis = millis;
	}
	

	/**
	 * Creates a duraction with the specified values.
	 * 
	 * @param days
	 * @param hours
	 * @param minutes
	 * @param seconds
	 */
	
	public Duration(int days, int hours, int minutes, int seconds)
	{
		this(days,hours,minutes,seconds,0);
	}
	

	/**
	 * Creates a duraction with the specified values.
	 * 
	 * @param days
	 * @param hours
	 * @param minutes
	 * @param seconds
	 */
	
	public Duration(int days, int hours, int minutes, int seconds, int milliseconds)
	{
		if(days < 0 || hours < 0 || minutes < 0 || seconds < 0 || milliseconds < 0)
		{
			throw new IllegalArgumentException("No negative value possible");
		}
		
		this.millis = days * MILLIS_PER_DAY + hours * MILLIS_PER_HOUR + minutes * MILLIS_PER_MINUTE
				+ seconds * MILLIS_PER_SECOND + milliseconds;
	}
	

	/**
	 * Creates a duration with the ellapsed time between <code>date1</code> and
	 * <code>date2</code>.
	 * <p>
	 * It doesn't matter if <code>date1</code> is before <code>date2</code> or
	 * vice versa.
	 * 
	 * @param date1
	 *            a date
	 * @param date2
	 *            a date
	 */
	public Duration(XdevDate date1, XdevDate date2)
	{
		this(Math.abs(date1.getTimeInMillis() - date2.getTimeInMillis()));
	}
	

	/**
	 * Return the exact amount of time in milliseconds of this duraction.
	 * 
	 * @return The duraction in milliseconds
	 */
	public long getMilliseconds()
	{
		return millis;
	}
	

	/**
	 * Returns the ramaining milliseconds of this duration after all days,
	 * hours, minutes and seconds are past.
	 * 
	 * @return The remaining milliseconds
	 */
	public int getRemainingMilliseconds()
	{
		return (int)(millis % MILLIS_PER_SECOND);
	}
	

	/**
	 * Returns all completely passed seconds of this duraction (millis /
	 * {@link #MILLIS_PER_SECOND}).
	 * 
	 * @return The completely passed seconds of this duraction
	 */
	public long getSeconds()
	{
		return millis / MILLIS_PER_SECOND;
	}
	

	/**
	 * Returns the ramaining seconds of this duration after all days, hours and
	 * minutes are past.
	 * 
	 * @return The remaining seconds
	 */
	public int getRemainingSeconds()
	{
		return (int)((millis % MILLIS_PER_MINUTE) / MILLIS_PER_SECOND);
	}
	

	/**
	 * Returns all completely passed minutes of this duraction (millis /
	 * {@link #MILLIS_PER_MINUTE}).
	 * 
	 * @return The completely passed minutes of this duraction
	 */
	public long getMinutes()
	{
		return millis / MILLIS_PER_MINUTE;
	}
	

	/**
	 * Returns the ramaining minutes of this duration after all days and hours
	 * are past.
	 * 
	 * @return The remaining minutes
	 */
	public int getRemainingMinutes()
	{
		return (int)((millis % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE);
	}
	

	/**
	 * Returns all completely passed hours of this duraction (millis /
	 * {@link #MILLIS_PER_HOUR}).
	 * 
	 * @return The completely passed hours of this duraction
	 */
	public long getHours()
	{
		return millis / MILLIS_PER_HOUR;
	}
	

	/**
	 * Returns the ramaining hours of this duration after all days are past.
	 * 
	 * @return The remaining hours
	 */
	public int getRemainingHours()
	{
		return (int)((millis % MILLIS_PER_DAY) / MILLIS_PER_HOUR);
	}
	

	/**
	 * Returns all completely passed days of this duraction (millis /
	 * {@link #MILLIS_PER_DAY}).
	 * 
	 * @return The completely passed days of this duraction
	 */
	public long getDays()
	{
		return millis / MILLIS_PER_DAY;
	}
	

	/**
	 * Returns the ramaining days of this duration.
	 * <p>
	 * This method return the same result as {@link #getDays()}
	 * 
	 * @return The remaining hours
	 */
	public int getRemainingDays()
	{
		return (int)(millis / MILLIS_PER_DAY);
	}
	

	/**
	 * Creates a new duration object whith this amount of time plus
	 * <code>duration</code>'s amount of time.
	 * 
	 * @param duration
	 *            The duration to be added
	 * @return A new duration object with the accumulated time.
	 */
	public Duration plus(Duration duration)
	{
		return plus(duration.millis);
	}
	

	/**
	 * Creates a new duration object whith this amount of time plus
	 * <code>millis</code>.
	 * 
	 * @param millis
	 *            The duration to be added
	 * @return A new duration object with the accumulated time.
	 */
	public Duration plus(long millis)
	{
		return new Duration(this.millis + millis);
	}
	

	/**
	 * Creates a new duration object whith this amount of time minus
	 * <code>duration</code>'s amount of time.
	 * 
	 * @param duration
	 *            The duration to be removed
	 * @return A new duration object with the residuary time.
	 */
	public Duration minus(Duration duration)
	{
		return minus(duration.millis);
	}
	

	/**
	 * Creates a new duration object whith this amount of time minus
	 * <code>millis</code>.
	 * 
	 * @param millis
	 *            The duration to be removed
	 * @return A new duration object with the residuary time.
	 */
	public Duration minus(long millis)
	{
		return new Duration(this.millis - millis);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
		{
			return true;
		}
		
		if(obj instanceof Duration)
		{
			return ((Duration)obj).millis == millis;
		}
		
		return false;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return (int)(millis ^ (millis >>> 32));
	}
	

	/**
	 * {@inheritDoc}
	 */
	public Duration clone()
	{
		return new Duration(millis);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Duration other)
	{
		long thisMillis = this.millis;
		long anotherMillis = other.millis;
		return(thisMillis < anotherMillis ? -1 : (thisMillis == anotherMillis ? 0 : 1));
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		append(sb,getRemainingDays(),"day(s)");
		append(sb,getRemainingHours(),"hour(s)");
		append(sb,getRemainingMinutes(),"minute(s)");
		append(sb,getRemainingSeconds(),"second(s)");
		append(sb,getRemainingMilliseconds(),"millisecond(s)");
		
		return sb.toString();
	}
	

	private void append(StringBuilder sb, int value, String unit)
	{
		if(value > 0)
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(value);
			sb.append(" ");
			sb.append(unit);
		}
	}
}
