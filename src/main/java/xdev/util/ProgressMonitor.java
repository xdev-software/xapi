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


/**
 * The <code>ProgressMonitor</code> interface is implemented by objects that
 * monitor the progress of an activity; the methods in this interface are
 * invoked by code that performs the activity.
 * <p>
 * All activity is broken down into a linear sequence of tasks against which
 * progress is reported. When a task begins, a {@link #beginTask(String, int)}
 * notification is reported, followed by any number and mixture of progress
 * reports ({@link #worked(int)}). When the task is eventually completed, a
 * {@link #done()} notification is reported.
 * </p>
 * <p>
 * A request to cancel an operation can be signaled using the {@link #cancel()}
 * method. Operations taking a progress monitor are expected to poll the monitor
 * (using {@link #isCanceled()}) periodically and abort at their earliest
 * convenience. Operation can however choose to ignore cancelation requests.
 * </p>
 * <p>
 * Since notification is synchronous with the activity itself, the listener
 * should provide a fast and robust implementation. If the handling of
 * notifications would involve blocking operations, or operations which might
 * throw uncaught exceptions, the notifications should be queued, and the actual
 * processing deferred (or perhaps delegated to a separate thread).
 * </p>
 * 
 * @since 3.0
 */
public interface ProgressMonitor
{
	/**
	 * A default no-op implementation of {@link ProgressMonitor}.
	 */
	public final static ProgressMonitor	DUMMY	= new NullProgressMonitor();
	
	/**
	 * Constant indicating an unknown amount of work.
	 */
	public final static int				UNKNOWN	= -1;
	

	/**
	 * Notifies that a task is beginning.
	 * 
	 * @param name
	 *            the name (or description) of the main task
	 * @param totalWork
	 *            the total number of work units into which the main task is
	 *            been subdivided. If the value is {@link #UNKNOWN} the
	 *            implementation is free to indicate progress in a way which
	 *            doesn't require the total number of work units in advance.
	 */
	public void beginTask(String name, int totalWork);
	

	/**
	 * Notifies that a given number of work unit of the task has been completed.
	 * Note that this amount represents an installment, as opposed to a
	 * cumulative amount of work done to date.
	 * 
	 * @param progress
	 *            the number of work units just completed
	 */
	public void worked(int progress);
	

	/**
	 * Sets the task name to the given value. This method is used to restore the
	 * task label after a nested operation was executed.
	 * 
	 * @param name
	 *            the name (or description) of the main task
	 * @see #beginTask(java.lang.String, int)
	 */
	public void setTaskName(String name);
	

	/**
	 * Notifies that the work is done; that is, either the task is completed or
	 * the user canceled it.
	 */
	public void done();
	

	/**
	 * Returns whether cancelation of current operation has been requested.
	 * Long-running operations should poll to see if cancelation has been
	 * requested.
	 * 
	 * @return <code>true</code> if cancellation has been requested, and
	 *         <code>false</code> otherwise
	 * 
	 * @see #cancel()
	 */
	public boolean isCanceled();
	

	/**
	 * Indicates that cancelation has been requested (but not necessarily
	 * acknowledged).
	 * 
	 * @see #isCanceled()
	 */
	public void cancel();
	
}



class NullProgressMonitor implements ProgressMonitor
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginTask(String name, int max)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void worked(int progress)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTaskName(String name)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void done()
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCanceled()
	{
		return false;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancel()
	{
	}
}
