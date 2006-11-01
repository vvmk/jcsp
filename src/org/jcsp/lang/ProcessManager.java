    //////////////////////////////////////////////////////////////////////
    //                                                                  //
    //  JCSP ("CSP for Java") Libraries                                 //
    //  Copyright (C) 1996-2006 Peter Welch and Paul Austin.            //
    //                2001-2004 Quickstone Technologies Limited.        //
    //                                                                  //
    //  This library is free software; you can redistribute it and/or   //
    //  modify it under the terms of the GNU Lesser General Public      //
    //  License as published by the Free Software Foundation; either    //
    //  version 2.1 of the License, or (at your option) any later       //
    //  version.                                                        //
    //                                                                  //
    //  This library is distributed in the hope that it will be         //
    //  useful, but WITHOUT ANY WARRANTY; without even the implied      //
    //  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR         //
    //  PURPOSE. See the GNU Lesser General Public License for more     //
    //  details.                                                        //
    //                                                                  //
    //  You should have received a copy of the GNU Lesser General       //
    //  Public License along with this library; if not, write to the    //
    //  Free Software Foundation, Inc., 59 Temple Place, Suite 330,     //
    //  Boston, MA 02111-1307, USA.                                     //
    //                                                                  //
    //  Author contact: P.H.Welch@ukc.ac.uk                             //
    //                                                                  //
    //                                                                  //
    //////////////////////////////////////////////////////////////////////

package org.jcsp.lang;

/**
 * This enables a {@link CSProcess} to be spawned
 * <I>concurrently</I> with the process doing the spawning.
 * <P>
 * <A HREF="#constructor_summary">Shortcut to the Constructor and Method Summaries.</A>
 *
 * <H2>Description</H2>
 * The <TT>ProcessManager</TT> class enables a {@link CSProcess} to be spawned
 * <I>concurrently</I> with the process doing the spawning.  The class provides
 * methods to manage the spawned process: {@link #start start},
 * {@link #join join} and {@link #stop stop}.  The spawned process may, of course,
 * be a {@link Parallel} network of processes to any depth of nesting, in which
 * case the <I>whole</I> network comes under this management.
 * <P>
 * Spawning processes is not the normal way of creating a network in JCSP - the
 * normal method is to use the {@link Parallel} class.  However, when we need
 * to add processes in response to some run-time event, this capability is very
 * useful.
 * <P>
 * For completeness, <TT>ProcessManager</TT> is itself a <TT>CSProcess</TT>
 * - {@link #run run}ning a <TT>ProcessManager</TT> simply runs the process
 * it is managing.
 * </P>
 *
 * <H2>Examples</H2>
 *
 * <H3>Spawning a CSProcess</H3>
 *
 * This first example demonstrates that the managed <TT>CSProcess</TT> is
 * executed concurrently with the spawning process and that it dies when
 * its manager terminates.  The managed process is `infinite' and
 * just counts and chatters.
 * <PRE>
 * import org.jcsp.lang.*;
 * <I></I>
 * public class ProcessManagerExample1 {
 * <I></I>
 *   public static void main (String[] argv) {
 * <I></I>
 *     final ProcessManager manager = new ProcessManager (
 *       new CSProcess () {
 *         public void run () {
 *           final CSTimer tim = new CSTimer ();
 *           long timeout = tim.read ();
 *           int count = 0;
 *           while (true) {
 *             System.out.println (count + " :-) managed process running ...");
 *             count++;
 *             timeout += 100;
 *             tim.after (timeout);   // every 1/10th of a second ...
 *           }
 *         }
 *       }
 *     );
 * <I></I>
 *     final CSTimer tim = new CSTimer ();
 *     long timeout = tim.read ();
 * <I></I>
 *     System.out.println ("\n\n\t\t\t\t\t
 *                         *** start the managed process");
 *     manager.start ();
 * <I></I>
 *     for (int i = 0; i < 10; i++) {
 *       System.out.println ("\n\n\t\t\t\t\t
 *                           *** I'm still executing as well");
 *       timeout += 1000;
 *       tim.after (timeout);         // every second ...
 *     }
 * <I></I>
 *     System.out.println ("\n\n\t\t\t\t\t
 *                         *** I'm finishing now!");
 *   }
 * }
 * </PRE>
 *
 * * <H3>Stopping Race-Hazards</H3>
 *
 * Stopping a process releases any locks it (or any sub-process) may be
 * holding, so there is little danger of deadlock.  Of course, we
 * are assuming that nobody attempts a committed interaction afterwards.
 * However, if the stopped process were in the middle of some synchronised
 * transaction, the data update may be incomplete depending on the precise
 * moment of the stopping.  This is a race-hazard.
 * <P>
 * To avoid this, a managed process should only be stopped if we know it
 * is in a state where it is not interacting with its environment - <I>or</I>,
 * as in the above example, we do not care about such spoilt data.  To know
 * that it is in a stoppable state, the manager ought to listen for a message
 * (e.g. channel communication) from the process that it is ready to be stopped.
 * [An example of such cooperation between managed and manager processes is
 * given in the <TT>Hamming</TT> demonstration (see <TT>jcsp-demos.hamming</TT>).]
 * <P>
 * If the managed process is purely serial, there is not much point in the above trick,
 * since it could simply terminate.  However, having a manager stop a complex process
 * network means that the network does not have to make its own provision for termination.
 * The latter can add considerable algorithmic complexity, often to the detriment
 * of the clarity of individual process logic.
 * <P>
 * Stopping a network by setting a global <TT>volatile</TT> flag that each process polls
 * from time to time (the first suggestion in the documentation of the <I>deprecated</I>
 * <TT>stop</TT> method in <TT>java.lang.Thread</TT>) is not, in general, safe.
 * For example, a thread blocked on a monitor <TT>wait</TT> will remain blocked
 * if the thread that was going to <TT>notify</TT> it spots the shut-down flag
 * and terminates.  The JDK1.2 documentation describes some work-arounds, but
 * they are not simple and depend in part on the application logic.
 * <P>
 * For JCSP processes, <I>there is</I> a general solution to this [<I>`Graceful Termination
 * and Graceful Resetting'</I>, P.H.Welch, Proceedings of OUG-10, pp. 310-317,
 * Ed. A.W.P.Bakkers, IOS Press (Amsterdam), ISBN 90 5199 011 1, April, 1989],
 * based on the careful distribution of <I>poison</I> over the network's normal
 * communication channels.  Future versions of JCSP may take account of this.
 *
 * @see org.jcsp.lang.CSProcess
 * @see org.jcsp.lang.Parallel
 * @see org.jcsp.awt.ActiveApplet
 *
 * @author P.H.Welch
 * @author P.D.Austin
 */

public class ProcessManager implements CSProcess
{
    /**
     * The maximum priority value for running a process.
     */
    public static final int PRIORITY_MAX = Thread.MAX_PRIORITY;

    /**
     * The normal priority value for running a process.
     */
    public static final int PRIORITY_NORM = Thread.NORM_PRIORITY;

    /**
     * The minimum priority value for running a process.
     */
    public static final int PRIORITY_MIN = Thread.MIN_PRIORITY;


    /** The CSProcess to be executed by this ProcessManager */
    private final CSProcess process;

    /** The thread supporting the CSProcess being executed by this ProcessManager */
    private Thread thread;

    /**
     * @param <TT>proc</TT> the {@link CSProcess} to be executed by this ProcessManager
     */
    public ProcessManager(CSProcess proc)
    {
        this.process = proc;
        thread = new Thread()
        {
            public void run()
            {
                try
                {
                    Parallel.addToAllParThreads(this);
                    process.run();
                }
                catch (Throwable e)
                {
                    Parallel.uncaughtException("org.jcsp.lang.ProcessManager", e);
                }
                finally
                {
                    Parallel.removeFromAllParThreads(this);
                }
            }
        };
        thread.setDaemon(true);
    }

    //}}}

    //{{{ public void start ()
    /**
     * Start the managed process (but keep running ourselves).
     */
    public void start()
    {
        thread.start();
    }

    /**
     * Start the managed process at a specified priority
     * (but keep running ourselves). The priority of the
     * <code>ProcessManager</code> that this is called upon
     * will remain at the specified priority once the process
     * has terminated.
     *
     * The priority should be specified as an <code>int</code> between
     * <code>PRIORITY_MIN<code> and <code>PRIORITY_MAX<code>.
     *
     * @param priority   the priority at which to start the process.
     */
    public void start(int priority)
    {
        thread.setPriority(priority);
        start();
    }

    /**
     * Stop (permanently) the managed process.
     * 
     * This method now calls interrupt(), which will not always stop the process.
     * 
     * @deprecated
     */
    public void stop()
    {
        interrupt();
    }
    
    /**
     * Interrupt the managed process.  This will usually cause the process to throw a 
     * {@link ProcessInterruptedException}, which will likely halt the process.
     */
    public void interrupt()
    {
        thread.interrupt();
    }

    /**
     * Join the managed process (that is wait for it to terminate).
     */
    public void join()
    {
        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            throw new ProcessInterruptedException("Joining process " + process);
        }
    }

    /**
     * <p>
     * Run the managed process (that is start it and wait for it to terminate).
     * This will adjust the priority of the calling process to the priority of
     * this <code>ProcessManager</code> and then return the priority to the
     * previous value once the managed process has terminated.
     * </p>
     *
     * <p>
     * The managed process can be run at the caller's priority simply by directly
     * calling the <code>CSProcess</code> object's <code>run()</code> method.
     * </p>
     */
    public void run()
    {
        int oldPriority = Thread.currentThread().getPriority();
        Thread.currentThread().setPriority(thread.getPriority());
        process.run();
        Thread.currentThread().setPriority(oldPriority);
    }

    /**
     * <p>
     * Public mutator for setting the <code>ProcessManager</code> object's
     * process' priority.
     * </p>
     * <p>
     * The priority should be specified as an <code>int</code> between
     * <code>PRIORITY_MIN<code> and <code>PRIORITY_MAX<code>.
     * </p>
     *
     * @param  the priority to use.
     */
    public void setPriority(int priority)
    {
        thread.setPriority(priority);
    }

    /**
     * <p>
     * Public accessor for obtaining the <code>ProcessManager</code> object's
     * process' priority.
     * </p>
     *
     * @return the priority at which the <code>ProcessManager</code> object's
     *          process will be run.
     */
    public int getPriority()
    {
        return thread.getPriority();
    }
}
