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
package xdev.ui.locking;


import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

import javax.swing.SwingUtilities;

import xdev.db.locking.LockTimeoutListener;
import xdev.db.locking.PessimisticLock;
import xdev.db.locking.RowAlreadyLockedException;
import xdev.lang.EventHandlerDelegate;
import xdev.ui.GBC;
import xdev.ui.UIUtils;
import xdev.ui.XdevButton;
import xdev.ui.XdevContainer;
import xdev.ui.XdevLabel;
import xdev.ui.XdevPicture;
import xdev.ui.XdevSlider;
import xdev.util.XdevHashtable;


/**
 * Concrete implementation of {@link AbstractLockRenewWindow}.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class RenewLockWindow extends AbstractLockRenewWindow implements LockRenovator
{
	
	final SimpleDateFormat		df							= new SimpleDateFormat("HH:mm:ss");
	
	private PessimisticLock		lock;
	
	private int					lockDeterminedSliderMinValue;
	private int					lockDeterminedSliderMaxValue;
	private final int			maxMultiplier				= 4;
	
	private static final int	TIME_MODIFIER				= 1000;
	private static final int	DEFAULT_TICK_SPACER			= 4;
	private static final int	THRESHOLD_AVOIDANCE_TIME	= 10;
	
	
	/**
	 * Initiates this renew lock window for the given lock to renovate.
	 * 
	 * @param lock
	 *            the lock to renovate.
	 */
	public RenewLockWindow(final PessimisticLock lock)
	{
		this.lock = lock;
		this.lockDeterminedSliderMinValue = this.getLockDeterminedMinimum(lock)
				+ THRESHOLD_AVOIDANCE_TIME;
		this.lockDeterminedSliderMaxValue = this.getLockDeterminedMaximum(lock);
		
		this.initUI();
	}
	
	
	public RenewLockWindow()
	{
		this.initUI();
	}
	
	
	private int getLockDeterminedMinimum(PessimisticLock lock)
	{
		// consider and return notification threshold based value to avoid value
		// range exceptions regarding timer/taks.
		long min = 0;
		for(LockTimeoutListener timeoutListener : lock.getLockTimeoutListener())
		{
			if(timeoutListener.getNotificationThreshold() > min)
			{
				min = timeoutListener.getNotificationThreshold();
			}
		}
		
		return safeLongToInt(min) / TIME_MODIFIER;
	}
	
	
	private int getLockDeterminedMaximum(PessimisticLock lock)
	{
		if(this.lockDeterminedSliderMinValue > 0)
		{
			return this.lockDeterminedSliderMinValue * this.maxMultiplier;
		}
		else
		{
			return (safeLongToInt(lock.getTimeout()) / 2) / TIME_MODIFIER;
		}
	}
	
	
	// XXX utility method
	private int safeLongToInt(long l)
	{
		if(l < Integer.MIN_VALUE || l > Integer.MAX_VALUE)
		{
			throw new IllegalArgumentException(l
					+ " cannot be cast to int without changing its value.");
		}
		return (int)l;
	}
	
	
	@EventHandlerDelegate
	void this_windowClosing(WindowEvent event)
	{
		close();
	}
	
	
	@EventHandlerDelegate
	void cmdCancel_actionPerformed(ActionEvent event)
	{
		this.close();
	}
	
	
	@EventHandlerDelegate
	void cmdExtend_actionPerformed(ActionEvent event)
	{
		this.renovateLock(this.lock);
		
	}
	
	XdevSlider		sldExtend;
	XdevPicture		picture;
	XdevLabel		lblEditableUntilDesc, lblEditableUntil, lblRenewLock;
	XdevContainer	container;
	XdevButton		cmdExtend, cmdCancel;
	
	
	protected void initUI()
	{
		picture = new XdevPicture();
		lblEditableUntilDesc = new XdevLabel();
		lblEditableUntil = new XdevLabel();
		lblRenewLock = new XdevLabel();
		container = new XdevContainer();
		sldExtend = new XdevSlider();
		cmdExtend = new XdevButton();
		cmdCancel = new XdevButton();
		
		this.setPreferredSize(new Dimension(467,136));
		picture.setImagePath("xdev/ui/locking/icons/info_icon.png");
		lblEditableUntilDesc.setText(UIResourceBundle.getString("RenewLockWindow.LockIsValid"));
		lblEditableUntil.setText("Label");
		lblRenewLock.setText(UIResourceBundle.getString("RenewLockWindow.RenewLockText"));
		sldExtend.setTabIndex(1);
		sldExtend.setValue(this.lockDeterminedSliderMinValue);
		sldExtend.setMajorTickSpacing((this.lockDeterminedSliderMinValue * this.maxMultiplier)
				/ DEFAULT_TICK_SPACER);
		sldExtend.setLabelTable(new XdevHashtable(this.lockDeterminedSliderMinValue,sldExtend
				.createLabel(Integer.toString(this.lockDeterminedSliderMinValue)),
				this.lockDeterminedSliderMaxValue,sldExtend.createLabel(Integer
						.toString(this.lockDeterminedSliderMaxValue))));
		sldExtend.setMaximum(this.lockDeterminedSliderMaxValue);
		sldExtend.setMinimum(this.lockDeterminedSliderMinValue);
		sldExtend.setPaintLabels(true);
		sldExtend.setPaintTicks(true);
		sldExtend.setSnapToTicks(true);
		cmdExtend.setTabIndex(3);
		cmdExtend.setText(UIResourceBundle.getString("RenewLockWindow.Renew"));
		cmdCancel.setTabIndex(4);
		cmdCancel.setText(UIResourceBundle.getString("RenewLockWindow.Cancel"));
		
		picture.saveState();
		lblEditableUntilDesc.saveState();
		lblEditableUntil.saveState();
		lblRenewLock.saveState();
		sldExtend.saveState();
		
		this.setTitle(UIResourceBundle.getString("RenewLockWindow.Title"));
		container.setLayout(new GridBagLayout());
		container.add(lblEditableUntilDesc,new GBC(1,1,1,1,0.0,0.0,GBC.BASELINE_LEADING,GBC.NONE,
				new Insets(3,3,3,3),0,0));
		container.add(lblEditableUntil,new GBC(2,1,1,1,0.0,0.0,GBC.BASELINE_LEADING,GBC.NONE,
				new Insets(3,3,3,3),0,0));
		container.add(lblRenewLock,new GBC(1,2,1,1,0.0,0.0,GBC.BASELINE_LEADING,GBC.NONE,
				new Insets(3,3,3,3),0,0));
		container.add(sldExtend,new GBC(2,2,1,1,0.1,0.0,GBC.WEST,GBC.HORIZONTAL,
				new Insets(3,3,3,3),0,0));
		GBC.addSpacer(container,true,true);
		picture.setBounds(6,12,48,48);
		this.add(picture);
		cmdCancel.setBounds(236,100,109,23);
		this.add(cmdCancel);
		cmdExtend.setBounds(123,100,109,23);
		this.add(cmdExtend);
		container.setBounds(63,16,387,85);
		this.add(container);
		this.setResizable(false);
		
		SwingUtilities.invokeLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				if(lock != null)
				{
					lblEditableUntil.setText(df.format(lock.getLockInfo().getValidUntil()));
				}
			}
		});
		
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent event)
			{
				this_windowClosing(event);
			}
		});
		cmdExtend.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				cmdExtend_actionPerformed(event);
			}
		});
		cmdCancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				cmdCancel_actionPerformed(event);
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renovateLock(PessimisticLock lock)
	{
		if(this.lock != null)
		{
			try
			{
				this.lock.renewLock(sldExtend.getValue() * TIME_MODIFIER);
				this.close();
			}
			catch(RowAlreadyLockedException e)
			{
				
				UIUtils.showMessage("Datensatz gesperrt von "
						+ e.getBlockingLockInfo().getUserString(),
						"Der Datensatz wird mittlerweile von einem anderen Benutzer ("
								+ e.getBlockingLockInfo().getUserString() + ") bearbeitet bis "
								+ df.format(e.getBlockingLockInfo().getValidUntil())
								+ " (Clientzeit).",UIUtils.INFORMATION_MESSAGE);
				
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLock(final PessimisticLock lock)
	{
		this.lock = lock;
		
		// re init
		this.lockDeterminedSliderMinValue = this.getLockDeterminedMinimum(lock)
				+ THRESHOLD_AVOIDANCE_TIME;
		this.lockDeterminedSliderMaxValue = this.getLockDeterminedMaximum(lock);
		
		this.removeAll();
		this.initUI();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PessimisticLock getLock()
	{
		return this.lock;
	}
}
