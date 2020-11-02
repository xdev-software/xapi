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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.NumberFormatter;

import xdev.io.IOUtils;
import xdev.ui.text.TextFormat;


public class DatePopup
{
	private static Map<JComponent, DatePopup> instances = new Hashtable<>();
	
	
	public static DatePopup getInstance(final JComponent owner)
	{
		DatePopup instance = instances.get(owner);
		if(instance == null)
		{
			instance = new DatePopup(owner);
			instances.put(owner,instance);
		}
		
		return instance;
	}
	
	private static DatePopupCustomizer defaultCustomizer;
	static
	{
		defaultCustomizer = new DatePopupCustomizer()
		{
			@Override
			public int getMinYear()
			{
				return 1900;
			}
			
			
			@Override
			public int getMaxYear()
			{
				return 3000;
			}
		};
	}
	
	
	public static void setDefaultCustomizer(final DatePopupCustomizer defaultCustomizer)
	{
		if(defaultCustomizer == null)
		{
			throw new IllegalArgumentException("defaultCustomizer can't be null");
		}
		
		DatePopup.defaultCustomizer = defaultCustomizer;
	}
	
	
	public static DatePopupCustomizer getDefaultCustomizer()
	{
		return defaultCustomizer;
	}
	
	private final static Dimension	dateLabelSize	= new Dimension(25,18);
	private final Calendar			selectedDateTime;
	private final JPanel			contentPane, pnlDate, pnlTime;
	private final MonthChooser		monthChooser;
	private final ArrowButton		cmdPrevious, cmdNext;
	private final XdevSpinner		spinYear, spinHour, spinMinute, spinSecond;
	private final DayHeader[]		dayHeader;
	private final DayButton[]		dayButton;
	private final JButton			cmdNow, cmdOK;
	private final PopupWindow		window;
	private DatePopupOwner			owner;
	private TextFormat				textFormat;
	private Locale					locale;
	private DateFormat				dayFormat;
	private boolean					fireChanged		= true;
	
	
	private DatePopup(final JComponent owner)
	{
		this.selectedDateTime = Calendar.getInstance();
		
		this.monthChooser = new MonthChooser();
		
		final FocusListener selectAllListener = new FocusAdapter()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				SwingUtilities.invokeLater(() -> ((JTextField)e.getComponent()).selectAll());
			}
		};
		
		final int minYear = defaultCustomizer.getMinYear();
		final int maxYear = defaultCustomizer.getMaxYear();
		this.spinYear = new XdevSpinner(new SpinnerNumberModel(minYear,minYear,maxYear,1));
		
		// XDEVAPI-218
		this.spinYear.setEditor(new JSpinner.NumberEditor(this.spinYear,"#"));
		
		this.spinYear.getTextField().addFocusListener(selectAllListener);
		
		this.spinYear.addChangeListener(e -> {
			if(this.fireChanged)
			{
				updateMonthView();
			}
		});
		
		this.cmdPrevious = new ArrowButton(SwingConstants.LEFT)
		{
			@Override
			public void setCoords(final int x, final int y)
			{
				this.xp[0] = x;
				this.xp[1] = x + 5;
				this.xp[2] = x + 5;
				this.yp[0] = y + 5;
				this.yp[1] = y;
				this.yp[2] = y + 10;
			}
		};
		this.cmdPrevious.setBorder(BorderFactory.createEmptyBorder(0,1,0,5));
		this.cmdPrevious.addActionListener(e -> {
			try
			{
				this.fireChanged = false;
				
				final int month = this.monthChooser.getSelectedMonth();
				if(month == 0)
				{
					this.monthChooser.setSelectedMonth(this.monthChooser.getMonthCount() - 1);
					final int year = (Integer)this.spinYear.getValue() - 1;
					this.spinYear.setValue(year);
				}
				else
				{
					this.monthChooser.setSelectedMonth(month - 1);
				}
			}
			finally
			{
				this.fireChanged = true;
				updateMonthView();
			}
		});
		
		this.cmdNext = new ArrowButton(SwingConstants.RIGHT)
		{
			@Override
			public void setCoords(final int x, final int y)
			{
				this.xp[0] = x + 5;
				this.xp[1] = x;
				this.xp[2] = x;
				this.yp[0] = y + 5;
				this.yp[1] = y;
				this.yp[2] = y + 10;
			}
		};
		this.cmdNext.setBorder(BorderFactory.createEmptyBorder(0,3,0,10));
		this.cmdNext.addActionListener(e -> {
			try
			{
				this.fireChanged = false;
				
				final int month = this.monthChooser.getSelectedMonth();
				if(month == this.monthChooser.getMonthCount() - 1)
				{
					this.monthChooser.setSelectedMonth(0);
					final int year = (Integer)this.spinYear.getValue() + 1;
					this.spinYear.setValue(year);
				}
				else
				{
					this.monthChooser.setSelectedMonth(month + 1);
				}
			}
			finally
			{
				this.fireChanged = true;
				updateMonthView();
			}
		});
		
		final JPanel pnlDay = new JPanel(null);
		pnlDay.setOpaque(false);
		final Dimension size = new Dimension(dateLabelSize.width * 7 + 1,
				dateLabelSize.height * 7 + 1);
		pnlDay.setPreferredSize(size);
		pnlDay.setSize(size);
		
		this.dayHeader = new DayHeader[7];
		for(int x = 0; x < 7; x++)
		{
			this.dayHeader[x] = new DayHeader("00");
			this.dayHeader[x].setLocation(x * dateLabelSize.width,0);
			pnlDay.add(this.dayHeader[x]);
		}
		
		int i = 0;
		this.dayButton = new DayButton[42];
		
		for(int y = 1; y <= 6; y++)
		{
			for(int x = 0; x < 7; x++, i++)
			{
				this.dayButton[i] = new DayButton();
				this.dayButton[i].setLocation(x * dateLabelSize.width,y * dateLabelSize.height);
				pnlDay.add(this.dayButton[i]);
			}
		}
		
		this.spinHour = new XdevSpinner(new SpinnerNumberModel(0,0,23,1));
		this.spinHour.getTextField().addFocusListener(selectAllListener);
		this.spinHour.addChangeListener(e -> {
			if(this.fireChanged)
			{
				this.selectedDateTime.set(Calendar.HOUR_OF_DAY,
						(Integer)DatePopup.this.spinHour.getValue());
			}
		});
		timizeSpinnerEditor(this.spinHour);
		
		this.spinMinute = new XdevSpinner(new SpinnerNumberModel(0,0,59,1));
		this.spinMinute.getTextField().addFocusListener(selectAllListener);
		this.spinMinute.addChangeListener(e -> {
			if(this.fireChanged)
			{
				this.selectedDateTime.set(Calendar.MINUTE,
						(Integer)DatePopup.this.spinMinute.getValue());
			}
		});
		timizeSpinnerEditor(this.spinMinute);
		
		this.spinSecond = new XdevSpinner(new SpinnerNumberModel(0,0,59,1));
		this.spinSecond.getTextField().addFocusListener(selectAllListener);
		this.spinSecond.addChangeListener(e -> {
			if(this.fireChanged)
			{
				this.selectedDateTime.set(Calendar.SECOND,
						(Integer)DatePopup.this.spinSecond.getValue());
			}
		});
		timizeSpinnerEditor(this.spinSecond);
		
		this.cmdNow = new JButton(UIResourceBundle.getString("datechooser.now"));
		this.cmdNow.addActionListener(e -> setSelectedDateTime(new Date()));
		
		this.cmdOK = new JButton(UIManager.getString("OptionPane.okButtonText"));
		this.cmdOK.addActionListener(e -> hidePopup(true));
		
		final JPanel pnlMonth = new JPanel(new BorderLayout());
		pnlMonth.setOpaque(false);
		pnlMonth.add(this.cmdPrevious,BorderLayout.WEST);
		pnlMonth.add(this.monthChooser,BorderLayout.CENTER);
		pnlMonth.add(this.cmdNext,BorderLayout.EAST);
		
		final JPanel pnlNorth = new JPanel(new BorderLayout());
		pnlNorth.setOpaque(false);
		pnlNorth.add(pnlMonth,BorderLayout.CENTER);
		pnlNorth.add(this.spinYear,BorderLayout.EAST);
		
		this.pnlDate = new JPanel(new BorderLayout(0,10));
		this.pnlDate.setOpaque(false);
		this.pnlDate.add(pnlNorth,BorderLayout.NORTH);
		this.pnlDate.add(pnlDay,BorderLayout.CENTER);
		
		final JPanel pnlHour = new JPanel(new BorderLayout());
		pnlHour.setOpaque(false);
		pnlHour.add(this.spinHour,BorderLayout.CENTER);
		pnlHour.add(new JLabel(" : "),BorderLayout.EAST);
		
		final JPanel pnlMinute = new JPanel(new BorderLayout());
		pnlMinute.setOpaque(false);
		pnlMinute.add(this.spinMinute,BorderLayout.CENTER);
		pnlMinute.add(new JLabel(" : "),BorderLayout.EAST);
		
		final JPanel pnlTimeSetter = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		pnlTimeSetter.setOpaque(false);
		pnlTimeSetter.add(pnlHour);
		pnlTimeSetter.add(pnlMinute);
		pnlTimeSetter.add(this.spinSecond);
		
		this.pnlTime = new JPanel(new BorderLayout());
		this.pnlTime.setOpaque(false);
		this.pnlTime.add(new JLabel(UIResourceBundle.getString("datechooser.time")),
				BorderLayout.CENTER);
		this.pnlTime.add(pnlTimeSetter,BorderLayout.EAST);
		
		final JPanel pnlButtons = new JPanel(new GridLayout(1,2,5,0));
		pnlButtons.setOpaque(false);
		pnlButtons.add(this.cmdNow);
		pnlButtons.add(this.cmdOK);
		
		final JPanel pnlDummy = new JPanel();
		pnlDummy.setOpaque(false);
		
		final JPanel pnlCmd = new JPanel(new BorderLayout());
		pnlCmd.setOpaque(false);
		pnlCmd.add(pnlDummy,BorderLayout.CENTER);
		pnlCmd.add(pnlButtons,BorderLayout.EAST);
		
		final JPanel pnlSouth = new JPanel(new BorderLayout(0,10));
		pnlSouth.setOpaque(false);
		pnlSouth.add(this.pnlTime,BorderLayout.NORTH);
		pnlSouth.add(pnlCmd,BorderLayout.CENTER);
		
		this.contentPane = new JPanel(new BorderLayout(0,10));
		this.contentPane.setBackground(Color.white);
		this.contentPane.setBorder(BorderFactory.createCompoundBorder(
				UIManager.getBorder("PopupMenu.border"),BorderFactory.createEmptyBorder(5,5,5,5)));
		this.contentPane.add(this.pnlDate,BorderLayout.CENTER);
		this.contentPane.add(pnlSouth,BorderLayout.SOUTH);
		
		this.window = new PopupWindow(owner);
		this.window.add(this.contentPane);
		
		this.locale = Locale.getDefault();
		this.dayFormat = new SimpleDateFormat("d",this.locale);
	}
	
	
	private void timizeSpinnerEditor(final JSpinner spinner)
	{
		final JSpinner.NumberEditor editor = (JSpinner.NumberEditor)spinner.getEditor();
		((NumberFormatter)editor.getTextField().getFormatter()).setFormat(new DecimalFormat("00"));
	}
	
	
	public DatePopupOwner getOwner()
	{
		return this.owner;
	}
	
	
	public void showPopup(final DatePopupOwner owner)
	{
		setOwner(owner);
		
		Date d = null;
		try
		{
			d = this.textFormat.parseDate(owner.getText());
		}
		catch(final Exception e)
		{
			d = new Date();
		}
		setSelectedDateTime(d);
		
		final Component cpn = owner.getComponentForDatePopup();
		this.window.addAsExcludedComponents(cpn);
		this.window.show(cpn,cpn.getWidth() - this.contentPane.getPreferredSize().width,
				cpn.getHeight());
	}
	
	
	private void setOwner(final DatePopupOwner owner)
	{
		this.owner = owner;
		
		this.textFormat = owner.getTextFormat();
		this.locale = this.textFormat.getLocale();
		this.dayFormat = new SimpleDateFormat("d",this.locale);
		
		final Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",this.locale);
		final int monthCount = calendar.getMaximum(Calendar.MONTH) + 1;
		final String[] monthString = new String[monthCount];
		for(int i = 0; i < monthCount; i++)
		{
			calendar.set(1999,i,1);
			monthString[i] = monthFormat.format(calendar.getTime());
		}
		this.monthChooser.reset(monthString);
		
		this.pnlDate.setVisible(this.textFormat.hasDate());
		this.pnlTime.setVisible(this.textFormat.hasTime());
		
		calendar.set(1999,0,1);
		while(calendar.get(Calendar.DAY_OF_WEEK) != calendar.getFirstDayOfWeek())
		{
			calendar.add(Calendar.DATE,1);
		}
		
		final SimpleDateFormat weekdayFormat = new SimpleDateFormat("E",this.locale);
		for(int x = 0; x < 7; x++)
		{
			this.dayHeader[x].setText(weekdayFormat.format(calendar.getTime()));
			calendar.roll(Calendar.DATE,true);
		}
		
		setCustomizer(owner.getDatePopupCustomizer());
	}
	
	
	private void setCustomizer(DatePopupCustomizer customizer)
	{
		if(customizer == null)
		{
			customizer = defaultCustomizer;
		}
		
		final int minYear = customizer.getMinYear();
		final int maxYear = customizer.getMaxYear();
		this.spinYear.setModel(new SpinnerNumberModel(maxYear,minYear,maxYear,1));
	}
	
	
	private void setSelectedDateTime(final Date d)
	{
		try
		{
			this.fireChanged = false;
			
			this.selectedDateTime.setTime(d);
			
			this.monthChooser.setSelectedMonth(this.selectedDateTime.get(Calendar.MONTH));
			this.spinYear.setValue(this.selectedDateTime.get(Calendar.YEAR));
			
			updateMonthView();
			
			this.spinHour.setValue(new Integer(this.selectedDateTime.get(Calendar.HOUR_OF_DAY)));
			this.spinMinute.setValue(new Integer(this.selectedDateTime.get(Calendar.MINUTE)));
			this.spinSecond.setValue(new Integer(this.selectedDateTime.get(Calendar.SECOND)));
		}
		finally
		{
			this.fireChanged = true;
		}
	}
	
	
	private void updateMonthView()
	{
		final int year = (Integer)this.spinYear.getValue();
		final int month = this.monthChooser.getSelectedMonth();
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,year);
		calendar.set(Calendar.MONTH,month);
		calendar.set(Calendar.DATE,1);
		int start = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
		if(start < 0)
		{
			start += 7;
		}
		
		final int end = start + calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		for(int i = 0; i < start; i++)
		{
			this.dayButton[i].setValue(-1,-1,-1);
		}
		
		for(int i = start, day = 1; i < end; i++, day++)
		{
			this.dayButton[i].setValue(year,month,day);
		}
		
		for(int i = end; i < this.dayButton.length; i++)
		{
			this.dayButton[i].setValue(-1,-1,-1);
		}
	}
	
	
	public boolean isVisible()
	{
		return this.window.isVisible();
	}
	
	
	public void hidePopup()
	{
		this.window.hide();
	}
	
	
	private void hidePopup(final boolean fire)
	{
		this.owner.hideDatePopup();
		
		if(fire)
		{
			try
			{
				this.owner.setText(this.textFormat.format(this.selectedDateTime.getTime()));
			}
			catch(final Exception e)
			{
			}
		}
	}
	
	
	private void checkDaySelection()
	{
		for(int i = 0; i < this.dayButton.length; i++)
		{
			this.dayButton[i].checkSelection();
		}
	}
	
	
	
	private class DayHeader extends JLabel
	{
		DayHeader(final String caption)
		{
			super(caption);
			
			setHorizontalAlignment(CENTER);
			setSize(dateLabelSize.width + 1,dateLabelSize.height + 1);
			setOpaque(true);
			setBackground(UIManager.getColor("TableHeader.background"));
			setForeground(UIManager.getColor("TableHeader.foreground"));
			setBorder(BorderFactory.createLineBorder(UIManager.getColor("Table.gridColor"),1));
		}
	}
	
	
	
	private class DayButton extends JLabel implements MouseListener
	{
		int		year, month, day;
		boolean	selected	= false;
		
		
		DayButton()
		{
			super("");
			
			setHorizontalAlignment(CENTER);
			setSize(dateLabelSize.width + 1,dateLabelSize.height + 1);
			setOpaque(true);
			setBackground(Color.white);
			setForeground(Color.black);
			setBorder(BorderFactory.createLineBorder(UIManager.getColor("Table.gridColor"),1));
			
			addMouseListener(this);
		}
		
		
		void setValue(final int year, final int month, final int day)
		{
			this.year = year;
			this.month = month;
			this.day = day;
			
			checkSelection();
			
			if(day == -1)
			{
				setText("");
			}
			else
			{
				final Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR,year);
				calendar.set(Calendar.MONTH,month);
				calendar.set(Calendar.DAY_OF_MONTH,day);
				setText(DatePopup.this.dayFormat.format(calendar.getTime()));
			}
		}
		
		
		void checkSelection()
		{
			this.selected = DatePopup.this.selectedDateTime.get(Calendar.YEAR) == this.year
					&& DatePopup.this.selectedDateTime.get(Calendar.MONTH) == this.month
					&& DatePopup.this.selectedDateTime.get(Calendar.DAY_OF_MONTH) == this.day;
			setBackground(this.selected ? UIManager.getColor("Table.selectionBackground")
					: UIManager.getColor("Table.background"));
			setForeground(this.selected ? UIManager.getColor("Table.selectionForeground")
					: UIManager.getColor("Table.foreground"));
			final Font f = UIManager.getFont("Label.font");
			setFont(this.selected ? f.deriveFont(Font.BOLD) : f);
		}
		
		
		@Override
		public void mousePressed(final MouseEvent e)
		{
			if(this.day != -1)
			{
				DatePopup.this.selectedDateTime.set(Calendar.YEAR,this.year);
				DatePopup.this.selectedDateTime.set(Calendar.MONTH,this.month);
				DatePopup.this.selectedDateTime.set(Calendar.DAY_OF_MONTH,this.day);
				checkDaySelection();
				if(e.getClickCount() > 1)
				{
					hidePopup(true);
				}
			}
		}
		
		
		@Override
		public void mouseExited(final MouseEvent e)
		{
			if(this.day != -1 && !this.selected)
			{
				setBackground(Color.white);
				setForeground(Color.black);
			}
		}
		
		
		@Override
		public void mouseEntered(final MouseEvent e)
		{
			if(this.day != -1 && !this.selected)
			{
				setBackground(Color.black);
				setForeground(Color.white);
			}
		}
		
		
		@Override
		public void mouseClicked(final MouseEvent e)
		{
		}
		
		
		@Override
		public void mouseReleased(final MouseEvent e)
		{
		}
	}
	
	
	
	private abstract class ArrowButton extends JLabel implements Icon, MouseListener
	{
		int		direction;
		boolean	mouseOver	= false, mouseDown = false;
		int[]	xp			= new int[3];
		int[]	yp			= new int[3];
		
		
		ArrowButton(final int direction)
		{
			super();
			this.direction = direction;
			setIcon(this);
			addMouseListener(this);
		}
		
		
		void addActionListener(final ActionListener al)
		{
			this.listenerList.add(ActionListener.class,al);
		}
		
		
		@Override
		public int getIconWidth()
		{
			return 5;
		}
		
		
		@Override
		public int getIconHeight()
		{
			return 9;
		}
		
		
		@Override
		public void paintIcon(final Component c, final Graphics g0, int x, final int y)
		{
			if(this.mouseDown)
			{
				x += this.direction == SwingConstants.LEFT ? -1 : 1;
			}
			
			setCoords(x,y);
			final Polygon poly = new Polygon(this.xp,this.yp,3);
			
			final Graphics2D g = (Graphics2D)g0;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			
			g.setPaint(SystemColor.controlDkShadow);
			g.draw(poly);
			if(this.mouseOver)
			{
				g.fill(poly);
			}
		}
		
		
		abstract void setCoords(int x, int y);
		
		
		@Override
		public void mouseEntered(final MouseEvent e)
		{
			this.mouseOver = true;
			repaint();
		}
		
		
		@Override
		public void mouseExited(final MouseEvent e)
		{
			this.mouseOver = false;
			repaint();
		}
		
		
		@Override
		public void mousePressed(final MouseEvent e)
		{
			this.mouseDown = true;
			repaint();
		}
		
		
		@Override
		public void mouseReleased(final MouseEvent e)
		{
			this.mouseDown = false;
			repaint();
		}
		
		
		@Override
		public void mouseClicked(final MouseEvent e)
		{
			final EventListener[] el = this.listenerList.getListeners(ActionListener.class);
			for(int i = 0; i < el.length; i++)
			{
				((ActionListener)el[i]).actionPerformed(new ActionEvent(this,0,""));
			}
		}
	}
	
	
	
	private class MonthChooser extends JLabel
	{
		private String[]	monthString		= new String[12];
		JPopupMenu			popup			= null;
		int					selectedMonth	= 0;
		
		
		public MonthChooser()
		{
			setBorder(UIManager.getBorder("Spinner.border"));
			if(!IOUtils.isMac())
			{
				// Mac cannot handle popups in windows
				// https://bugs.openjdk.java.net/browse/JDK-8032872
				
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				addMouseListener(new MouseAdapter()
				{
					@Override
					public void mousePressed(final MouseEvent e)
					{
						if(MonthChooser.this.popup == null)
						{
							MonthChooser.this.popup = new JPopupMenu();
							for(int i = 0; i < MonthChooser.this.monthString.length; i++)
							{
								final int month = i;
								final JMenuItem mi = new JMenuItem(
										MonthChooser.this.monthString[i]);
								mi.addActionListener(actionEvent -> {
									setSelectedMonth(month);
									
									if(DatePopup.this.fireChanged)
									{
										updateMonthView();
									}
								});
								MonthChooser.this.popup.add(mi);
							}
						}
						
						MonthChooser.this.popup.show(MonthChooser.this,0,getHeight());
					}
				});
			}
		}
		
		
		void reset(final String[] monthString)
		{
			this.monthString = monthString;
			this.popup = null;
		}
		
		
		void setSelectedMonth(final int month)
		{
			this.selectedMonth = month;
			setText(this.monthString[month]);
		}
		
		
		int getSelectedMonth()
		{
			return this.selectedMonth;
		}
		
		
		int getMonthCount()
		{
			return this.monthString.length;
		}
	}
}
