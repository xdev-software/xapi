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
package xdev.util;


import java.awt.*;

import xdev.db.ColumnMetaData;
import xdev.ui.XdevTextField;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


/**
 * Helper class to manage global settings of the XAPI.
 * 
 * 
 * @author XDEV Software
 * 
 */
public class Settings
{
	/**
	 * Setting name constant for the painting quality
	 * 
	 * @see #setUseQualityPaint(boolean)
	 * @see #useQualityPaint()
	 */
	public final static String	QUALITY_PAINT					= "xdev.qualityPaint";
	
	/**
	 * Setting name constant for {@link String} data handling of the
	 * {@link VirtualTable}
	 * 
	 * @see #setTrimData(boolean)
	 * @see #trimData()
	 */
	public final static String	TRIM_DATA						= "xdev.trimData";
	
	/**
	 * Setting name constant for a focus behavior of {@link TextComponent}s
	 * 
	 * @see #setSelectAllOnFocus(boolean)
	 * @see #selectAllOnFocus()
	 * @deprecated replaced by
	 *             {@link XdevTextField#setFocusGainedBehavior(xdev.ui.FocusGainedBehavior)}
	 */
	@Deprecated
	public final static String	SELECT_ALL_ON_FOCUS				= "xdev.selectAllOnFocus";
	
	/**
	 * 
	 */
	public final static String	SERVER_SIDE_SUFFIX				= "xdev.serverSideSuffix";
	
	/**
	 * Setting name constant for the SQL AS keyword usage in
	 * {@link VirtualTableColumn}
	 * 
	 * @see #setSwapColumnNameAndCaption(boolean)
	 * @see #swapColumnNameAndCaption()
	 */
	public final static String	SWAP_COLUMN_NAME_AND_CAPTION	= "xdev.swapColumnNameAndCaption";
	
	/**
	 * Setting name constant for the servlet session renewal behavior.
	 * 
	 * @see #setAutoRenewServletSessions(boolean)
	 * @see #autoRenewServletSessions()
	 */
	public final static String	AUTO_RENEW_SERVLET_SESSIONS		= "xdev.autoRenewServletSessions";
	
	
	private Settings()
	{
	}
	
	
	/**
	 * @return <code>true</code> if high quality painting should be used,
	 *         <code>false</code> otherwise
	 * @see #setUseQualityPaint(boolean)
	 * @see #QUALITY_PAINT
	 */
	public static boolean useQualityPaint()
	{
		String value = System.getProperty(QUALITY_PAINT);
		return value != null && checkUserSetting(value);
	}
	
	
	/**
	 * Sets if high quality painting should be used.
	 * 
	 * @see #useQualityPaint()
	 * @see #QUALITY_PAINT
	 * @since 3.2
	 */
	public static void setUseQualityPaint(boolean b)
	{
		System.setProperty(QUALITY_PAINT,Boolean.toString(b));
	}
	
	
	/**
	 * @return The image scale mode which should be used for
	 *         {@link Image#getScaledInstance(int, int, int)}. This depends on
	 *         the {@link #QUALITY_PAINT} setting.
	 * @see #setUseQualityPaint(boolean)
	 * @see #QUALITY_PAINT
	 */
	public static int getImageScaleMode()
	{
		return useQualityPaint() ? Image.SCALE_SMOOTH : Image.SCALE_FAST;
	}
	
	
	/**
	 * Returns <code>true</code> if the {@link VirtualTable} should trim the
	 * {@link String}s.
	 * <p>
	 * This is useful for some databases which use fixed length {@link String}s
	 * filled up with spaces.
	 * 
	 * @return <code>true</code> if the {@link VirtualTable} should trim the
	 *         {@link String}s
	 * @see #setTrimData(boolean)
	 * @see #TRIM_DATA
	 */
	public static boolean trimData()
	{
		String value = System.getProperty(TRIM_DATA);
		return value != null && checkUserSetting(value);
	}
	
	
	/**
	 * Sets if the {@link VirtualTable} should trim the {@link String}s.
	 * 
	 * @see #trimData()
	 * @see #TRIM_DATA
	 * @since 3.2
	 */
	public static void setTrimData(boolean b)
	{
		System.setProperty(TRIM_DATA,Boolean.toString(b));
	}
	
	
	/**
	 * @deprecated replaced by
	 *             {@link XdevTextField#setFocusGainedBehavior(xdev.ui.FocusGainedBehavior)}
	 */
	@Deprecated
	public static boolean selectAllOnFocus()
	{
		String value = System.getProperty(SELECT_ALL_ON_FOCUS);
		return value != null && checkUserSetting(value);
	}
	
	
	/**
	 * @return if the {@link VirtualTableColumn} should use the
	 *         {@link ColumnMetaData}s name as caption or vise versa
	 * @see #setSwapColumnNameAndCaption(boolean)
	 * @see #SWAP_COLUMN_NAME_AND_CAPTION
	 * @since 3.2
	 */
	public static boolean swapColumnNameAndCaption()
	{
		String value = System.getProperty(SWAP_COLUMN_NAME_AND_CAPTION);
		return value != null && checkUserSetting(value);
	}
	
	
	/**
	 * Sets if the {@link VirtualTableColumn} should use the
	 * {@link ColumnMetaData}s name as caption or vise versa.
	 * 
	 * @see #swapColumnNameAndCaption()
	 * @see #SWAP_COLUMN_NAME_AND_CAPTION
	 * @since 3.2
	 */
	public static void setSwapColumnNameAndCaption(boolean b)
	{
		System.setProperty(SWAP_COLUMN_NAME_AND_CAPTION,Boolean.toString(b));
	}
	
	
	/**
	 * @return if expired servlet sessions are automatically renewed
	 * @see #setAutoRenewServletSessions(boolean)
	 * @see #AUTO_RENEW_SERVLET_SESSIONS
	 * @since 3.2
	 */
	public static boolean autoRenewServletSessions()
	{
		String value = System.getProperty(AUTO_RENEW_SERVLET_SESSIONS);
		return value == null || checkUserSetting(value);
	}
	
	
	/**
	 * Sets if expired servlet sessions are automatically renewed.
	 * 
	 * @see #autoRenewServletSessions()
	 * @see #AUTO_RENEW_SERVLET_SESSIONS
	 * @since 3.2
	 */
	public static void setAutoRenewServletSessions(boolean b)
	{
		System.setProperty(AUTO_RENEW_SERVLET_SESSIONS,Boolean.toString(b));
	}
	
	
	public static String getServerSideSuffix()
	{
		String suffix = System.getProperty(SERVER_SIDE_SUFFIX);
		if(suffix != null)
		{
			return suffix;
		}
		
		return "jsp";
	}
	
	
	public static boolean checkUserSetting(Object value)
	{
		return value != null
				&& (value.toString().equalsIgnoreCase("true") || value.toString().equals("1"));
	}
}
