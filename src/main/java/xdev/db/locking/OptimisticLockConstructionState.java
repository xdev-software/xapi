package xdev.db.locking;

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


/**
 * Optimistic lock approach implementation of {@link LockConstructionState}.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class OptimisticLockConstructionState implements LockConstructionState
{
	private boolean	initial	= false, regenerated = false;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInitial()
	{
		return this.initial;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInitial(boolean constructionState)
	{
		this.initial = constructionState;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRegenerated()
	{
		return this.regenerated;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRegenerated(boolean constructionState)
	{
		this.regenerated = constructionState;
	}
}
