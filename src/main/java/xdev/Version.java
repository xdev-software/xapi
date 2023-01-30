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
package xdev;


import java.util.*;


public final class Version implements Comparable<Version>
{
	// version info: always 4 ints
	private final int[]	v;
	private String		appendix;

	private int			hash	= 0;


	public Version(String s)
	{
		s = s.trim();
		int si = s.indexOf(' ');
		if(si > 0)
		{
			appendix = s.substring(si);
			s = s.substring(0,si);
		}

		StringTokenizer st = new StringTokenizer(s,".");
		v = new int[4];
		for(int i = 0; i < 4; i++)
		{
			if(st.hasMoreElements())
			{
				v[i] = Integer.parseInt(st.nextToken());
			}
			else
			{
				v[i] = 0;
			}
		}
	}


	public Version(int major, int minor1, int minor2, int minor3)
	{
		v = new int[]{major,minor1,minor2,minor3};
	}


	@Override
	public String toString()
	{
		return str(v.length);
	}


	public int getSmallOffset()
	{
		return ("" + v[0]).length() + 1;
	}


	public String toScreen()
	{
		int end = v.length;
		for(int i = v.length - 1; i >= 0; i--)
		{
			if(v[i] == 0)
			{
				end--;
			}
			else
			{
				break;
			}
		}

		return str(end);
	}


	public String getMain()
	{
		return String.valueOf(v[0]);
	}


	private String str(int end)
	{
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < end; i++)
		{
			sb.append(v[i]);
			if(i < end - 1)
			{
				sb.append(".");
			}
		}

		if(appendix != null)
		{
			sb.append(appendix);
		}

		return sb.toString();
	}


	public boolean isOlderThan(Version version)
	{
		for(int i = 0; i < 4; i++)
		{
			if(v[i] < version.v[i])
			{
				return true;
			}
			else if(v[i] > version.v[i])
			{
				return false;
			}
		}

		return false;
	}


	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Version)
		{
			Version version = (Version)o;
			if(version.v.length == v.length)
			{
				for(int i = 0; i < v.length; i++)
				{
					if(version.v[i] != v[i])
					{
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}


	public int compareTo(Version v)
	{
		return isOlderThan(v) ? -1 : (equals(v) ? 0 : 1);
	}


	@Override
	public int hashCode()
	{
		int h = hash;
		if(h == 0)
		{
			for(int i = 0; i < v.length; i++)
			{
				h = 31 * h + v[i];
			}
			hash = h;
		}
		return h;
	}
}
