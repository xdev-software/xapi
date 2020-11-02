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


import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


/**
 * To get an counter.
 * 
 * @author XDEV Software (RHHF)
 */
public abstract class Countdown
{
	/**
	 *
	 */
	public static final String			REGEX_DATE	= "\\d{2}:\\d{2}:\\d{2}";
	
	protected Timer						timer;
	protected TimerTask					task;
	
	protected long						durationMillis;
	
	private long						endTime;
	
	private static final DecimalFormat	df			= new DecimalFormat("00");
	
	private boolean						isCanceled	= false;
	
	
	public Countdown(final long durationMillis)
	{
		this.durationMillis = durationMillis;
		this.timer = new Timer("countdown",true);
		createTask();
	}
	
	
	protected void createTask()
	{
		this.task = new TimerTask()
		{
			@Override
			public void run()
			{
				final long now = System.currentTimeMillis();
				
				if((endTime - now) >= 0)
				{
					onCountDown(endTime - now);
					isCanceled = false;
				}
				else
				{
					timer.cancel();
					isCanceled = true;
				}
			}
		};
	}
	
	
	public long getDurationMillis()
	{
		return durationMillis;
	}
	
	
	/**
	 * Start countdown.
	 */
	public void start()
	{
		this.endTime = System.currentTimeMillis() + durationMillis;
		
		if(isCanceled)
		{
			this.timer = new Timer("countdown",true); // refresh timer - also
														// removes stops running
														// tasks???
			createTask();
			this.timer.scheduleAtFixedRate(task,0,TimeUnit.SECONDS.toMillis(1L));
		}
		else
			
			this.timer.scheduleAtFixedRate(task,0,TimeUnit.SECONDS.toMillis(1L));
	}
	
	
	/**
	 * Cancel countdown.
	 */
	public void cancel()
	{
		this.timer.cancel();
		isCanceled = true;
	}
	
	
	/**
	 * 
	 * @param remainingTime
	 *            in milliseconds
	 */
	public abstract void onCountDown(final long remainingTime);
	
	
	/**
	 * Get countdown time as string.
	 * 
	 * @param durationMillis
	 *            duration time in ms
	 * @return countdown (String)
	 */
	public static String getCountdownString(final long durationMillis)
	{
		
		Duration d = new Duration(durationMillis);
		
		return df.format(d.getRemainingHours()) + ":" + df.format(d.getRemainingMinutes()) + ":"
				+ df.format(d.getRemainingSeconds());
	}
	
	
	public void updateTime(long newTime)
	{
		final long now = System.currentTimeMillis();
		if(this.endTime <= now)
		{
			this.timer.purge();
			start();
		}
		this.endTime = System.currentTimeMillis() + newTime;
	}
	
	
	public long getEndTime()
	{
		return endTime;
	}
	
	
	public boolean isCanceled()
	{
		return isCanceled;
	}
}
