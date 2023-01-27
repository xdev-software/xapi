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
package xdev.ui;


import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import xdev.db.locking.LockTimeMonitor;
import xdev.db.locking.LockTimeOutChangeEvent;
import xdev.db.locking.LockTimeoutChangeListener;
import xdev.db.locking.PessimisticLock;
import xdev.util.Countdown;


/**
 * Implementation of {@link Countdown} to display countdown time in
 * {@link XdevWindow} or in a other {@link JComponent}.
 * 
 * @author XDEV Software (RHHF)
 */
public class WindowCountDown extends Countdown
{
	
	private XdevWindow			xdevWindow;
	
	static long					calls						= 0;
	
	static final String			extendedTitle				= " -"	+ UIResourceBundle.getString("WindowCountDown.LockIsValidForText") + " : "; //$NON-NLS-1$ //$NON-NLS-2$
																																				
	public static final String	COUNTDOWN_TEXT_PLACEHOLDER	= "{%XDEVCOUNTDOWN}";
	
	private LockTimeMonitor[]	countdownMonitors			= new LockTimeMonitor[0];
	
	
	public WindowCountDown(PessimisticLock concernedLock, final XdevWindow xdevWindow)
	{
		super(concernedLock.getRemainingTime());
		this.xdevWindow = xdevWindow;
		this.initLockTimeOutDependency(concernedLock);
		this.start();
		
	}
	
	
	/**
	 * @param renewLockTime
	 * @param xdevWindow
	 * @param getjComponentList
	 */
	public WindowCountDown(PessimisticLock concernedLock, XdevWindow xdevWindow,
			LockTimeMonitor[] jComponentList)
	{
		super(concernedLock.getRemainingTime());
		this.xdevWindow = xdevWindow;
		this.countdownMonitors = jComponentList;
		this.initLockTimeOutDependency(concernedLock);
		this.start();
	}
	
	
	/**
	 * @param renewLockTime
	 * @param getjComponentList
	 */
	public WindowCountDown(PessimisticLock concernedLock, LockTimeMonitor[] countdownMonitors)
	{
		super(concernedLock.getRemainingTime());
		this.countdownMonitors = countdownMonitors;
		this.initLockTimeOutDependency(concernedLock);
		this.start();
	}
	
	
	/**
	 * @param remainingTime
	 * @param countdownString
	 */
	private void setCountdownShowing(final long remainingTime, String countdownString)
	{
		for(int i = 0; i < WindowCountDown.this.countdownMonitors.length; i++)
		{
			WindowCountDown.this.countdownMonitors[i].updateContext(WindowCountDown.this,
					remainingTime);
		}
	}
	
	
	private void countdownMonitorReset()
	{
		for(int i = 0; i < WindowCountDown.this.countdownMonitors.length; i++)
		{
			WindowCountDown.this.countdownMonitors[i].updateContext(WindowCountDown.this,0);
		}
	}
	
	
	@Override
	public void onCountDown(final long remainingTime)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				String countdownString = Countdown.getCountdownString(remainingTime);
				if(xdevWindow != null)
				{
					setWindowTitle(countdownString);
					
				}
				setCountdownShowing(remainingTime,countdownString);
			}
			
			
			/**
			 * Set the window title.
			 * 
			 * @param countdownString
			 */
			private void setWindowTitle(String countdownString)
			{
				String title = xdevWindow.getTitle();
				if(title.contains(extendedTitle))
				{
					String[] split = title.split(extendedTitle);
					title = split[0] + extendedTitle + countdownString;
				}
				else
				{
					title = title + extendedTitle + countdownString;
				}
				xdevWindow.setTitle(title);
			}
		});
		
	}
	
	
	public void showCountdownIsOut()
	{
		setCountdownShowing(0L,"00:00:00");
	}
	
	
	/**
	 * Reset window title, deletes the countdown specific text from the title.
	 */
	public void resetWindowTitle()
	{
		if(this.xdevWindow != null)
		{
			String title = xdevWindow.getTitle();
			if(title.contains(extendedTitle))
			{
				String[] split = title.split(extendedTitle);
				title = split[0];
			}
			xdevWindow.setTitle(title);
		}
	}
	
	
	/**
	 * Return {@link XdevWindow}.
	 * 
	 * @return the xdevWindow
	 */
	public XdevWindow getXdevWindow()
	{
		return xdevWindow;
	}
	
	
	private void initLockTimeOutDependency(PessimisticLock concernedLock)
	{
		concernedLock.addLockTimeoutPropertyChangeListener(new LockTimeoutChangeListener()
		{
			@Override
			public void lockTimeOutChange(LockTimeOutChangeEvent lockTimeOutChangedEvent)
			{
				if(lockTimeOutChangedEvent.isCanceled())
				{
					WindowCountDown.this.cancel();
					WindowCountDown.this.resetWindowTitle();
					WindowCountDown.this.countdownMonitorReset();
				}
				else
				{
					WindowCountDown.this.updateTime(lockTimeOutChangedEvent.getLock()
							.getRemainingTime());
				}
			}
		});
	}
}
