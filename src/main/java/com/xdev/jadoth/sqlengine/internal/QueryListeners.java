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

import com.xdev.jadoth.sqlengine.interfaces.QueryListenerPostExecution;
import com.xdev.jadoth.sqlengine.interfaces.QueryListenerPreAssembly;
import com.xdev.jadoth.sqlengine.interfaces.QueryListenerPreExecution;
import com.xdev.jadoth.sqlengine.interfaces.QueryListenerSQLException;

/**
 * @author Thomas Muenz
 *
 */
public class QueryListeners
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	private QueryListenerPreAssembly.List preAssemblyListeners = null;
	private QueryListenerPreExecution.List preExecutionListeners = null;
	private QueryListenerPostExecution.List postExecutionListeners = null;
	private QueryListenerSQLException.List sqlExceptionListeners = null;
	
	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	
	/**
	 * @return the queryPreAssemblyListeners
	 */
	public QueryListenerPreAssembly.List getPreAssemblyListeners()
	{
		return preAssemblyListeners;
	}
	/**
	 * @return the queryPreExecutionListeners
	 */
	public QueryListenerPreExecution.List getPreExecutionListeners()
	{
		return preExecutionListeners;
	}
	/**
	 * @return the queryPostExecutionListeners
	 */
	public QueryListenerPostExecution.List getPostExecutionListeners()
	{
		return postExecutionListeners;
	}
	/**
	 * @return the queryExecutionExceptionListeners
	 */
	public QueryListenerSQLException.List getExecutionExceptionListeners()
	{
		return sqlExceptionListeners;
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	
	/**
	 * @param queryPreAssemblyListeners the queryPreAssemblyListeners to set
	 */
	public void setPreAssemblyListeners(QueryListenerPreAssembly.List queryPreAssemblyListeners)
	{
		this.preAssemblyListeners = queryPreAssemblyListeners;
	}
	/**
	 * @param queryPreExecutionListeners the queryPreExecutionListeners to set
	 */
	public void setPreExecutionListeners(QueryListenerPreExecution.List queryPreExecutionListeners)
	{
		this.preExecutionListeners = queryPreExecutionListeners;
	}
	/**
	 * @param queryPostExecutionListeners the queryPostExecutionListeners to set
	 */
	public void setPostExecutionListeners(QueryListenerPostExecution.List queryPostExecutionListeners)
	{
		this.postExecutionListeners = queryPostExecutionListeners;
	}
	/**
	 * @param querySqlExceptionListeners the queryExecutionExceptionListeners to set
	 */
	public void setSqlExceptionListeners(QueryListenerSQLException.List querySqlExceptionListeners)
	{
		this.sqlExceptionListeners = querySqlExceptionListeners;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	
	public QueryListenerPreAssembly.List preAssemblyListeners(){
		if(this.preAssemblyListeners == null){
			this.preAssemblyListeners = new QueryListenerPreAssembly.List();
		}
		return this.preAssemblyListeners;
	}
	public QueryListenerPreExecution.List preExecutionListeners(){
		if(this.preExecutionListeners == null){
			this.preExecutionListeners = new QueryListenerPreExecution.List();
		}
		return this.preExecutionListeners;
	}
	public QueryListenerPostExecution.List postExecutionListeners(){
		if(this.postExecutionListeners == null){
			this.postExecutionListeners = new QueryListenerPostExecution.List();
		}
		return this.postExecutionListeners;
	}
	public QueryListenerSQLException.List sqlExceptionListeners(){
		if(this.sqlExceptionListeners == null){
			this.sqlExceptionListeners = new QueryListenerSQLException.List();
		}
		return this.sqlExceptionListeners;
	}
	
}
