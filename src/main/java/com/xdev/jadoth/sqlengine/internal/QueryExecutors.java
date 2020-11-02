/**
 * 
 */
package com.xdev.jadoth.sqlengine.internal;

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

import java.util.LinkedList;
import java.util.List;

import com.xdev.jadoth.sqlengine.aspects.QueryExecutionAspect;
import com.xdev.jadoth.sqlengine.aspects.QueryExecutionContext;


/**
 * @author Thomas Muenz
 *
 */
public class QueryExecutors<R>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	private List<QueryExecutionAspect<QueryExecutionContext<R>, R>> preAssemblyExecutors = null;
	private List<QueryExecutionAspect<QueryExecutionContext<R>, R>> preExecutionExecutors = null;
	private List<QueryExecutionAspect<QueryExecutionContext<R>, R>> executionExecutors = null;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	
	/**
	 * @return the preAssemblyExecutors
	 */
	public List<QueryExecutionAspect<QueryExecutionContext<R>, R>> getPreAssemblyExecutors()
	{
		return preAssemblyExecutors;
	}
	/**
	 * @return the preExecutionExecutors
	 */
	public List<QueryExecutionAspect<QueryExecutionContext<R>, R>> getPreExecutionExecutors()
	{
		return preExecutionExecutors;
	}
	/**
	 * @return the postExecutionExecutors
	 */
	public List<QueryExecutionAspect<QueryExecutionContext<R>, R>> getPostExecutionExecutors()
	{
		return executionExecutors;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////	
	
	/**
	 * @param preAssemblyExecutors the preAssemblyExecutors to set
	 */
	public QueryExecutors<R> setPreAssemblyExecutors(List<QueryExecutionAspect<QueryExecutionContext<R>, R>> preAssemblyExecutors)
	{
		this.preAssemblyExecutors = preAssemblyExecutors;
		return this;
	}
	/**
	 * @param preExecutionExecutors the preExecutionExecutors to set
	 */
	public QueryExecutors<R> setPreExecutionExecutors(List<QueryExecutionAspect<QueryExecutionContext<R>, R>> preExecutionExecutors)
	{
		this.preExecutionExecutors = preExecutionExecutors;
		return this;
	}
	/**
	 * @param postExecutionExecutors the postExecutionExecutors to set
	 */
	public QueryExecutors<R> setPostExecutionExecutors(List<QueryExecutionAspect<QueryExecutionContext<R>, R>> postExecutionExecutors)
	{
		this.executionExecutors = postExecutionExecutors;
		return this;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	
	public List<QueryExecutionAspect<QueryExecutionContext<R>, R>> preAssemblyExecutors()
	{
		if(this.preAssemblyExecutors == null){
			this.preAssemblyExecutors = new LinkedList<QueryExecutionAspect<QueryExecutionContext<R>,R>>();
		}
		return this.preAssemblyExecutors;
	}
	
	public List<QueryExecutionAspect<QueryExecutionContext<R>, R>> preExecutionExecutors()
	{
		if(this.preExecutionExecutors == null){
			this.preExecutionExecutors = new LinkedList<QueryExecutionAspect<QueryExecutionContext<R>,R>>();
		}
		return this.preExecutionExecutors;
	}
	
	public List<QueryExecutionAspect<QueryExecutionContext<R>, R>> postExecutionExecutors()
	{
		if(this.executionExecutors == null){
			this.executionExecutors = new LinkedList<QueryExecutionAspect<QueryExecutionContext<R>,R>>();
		}
		return this.executionExecutors;
	}
	
	// (22.07.2010 TM)FIXME: invokeAssembly
//	public Object[] invokeAssembly(QueryExecutionContext<R> context)
//	{
//		if(this.preAssemblyExecutors == null) return null;
//		
//		for(QueryExecutionAspect<QueryExecutionContext<R>, R> executor : this.preAssemblyExecutors)
//		{
//			
//		}
//	}
	
	
}

