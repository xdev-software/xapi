package xdev.ui.locking;

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


import xdev.util.Countdown;


/**
 * 
 * @since 4.0
 */
public class LockCountMonitorDownUtils
{
	public static String getUpdatedText(Countdown countdown, long remainingTime, String text)
	{
		String countdownString = Countdown.getCountdownString(remainingTime);
		if(text.matches(".*" + Countdown.REGEX_DATE + ".*"))
		{
			text = text.replaceAll(Countdown.REGEX_DATE,countdownString);
		}
		else
		{
			text = text + countdownString;
		}
		return text;
	}
}
