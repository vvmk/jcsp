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
 * This implements {@link ChannelOutput} with <I>black hole</I> semantics.
 * <H2>Description</H2>
 * <TT>BlackHoleChannel</TT> is an implementation of {@link ChannelOutput} that yields
 * <I>black hole</I> semantics for the channel.  Writers may always write but there can be
 * no readers.  Any number of writers may share the same <I>black hole</I>.
 * <P>
 * <I>Note:</I> <TT>BlackHoleChannel</TT>s are used for masking off unwanted outputs
 * from processes.  They are useful when we want to reuse an existing process component
 * intact, but don't need some of its output channels (i.e. we don't want to redesign
 * and reimplement the component to remove the redundant channels).  Normal channels cannot
 * be plugged in and left dangling as this may deadlock (parts of) the component being
 * reused.
 * <P>
 *
 * @see org.jcsp.lang.ChannelOutput
 * @see org.jcsp.lang.One2OneChannel
 * @see org.jcsp.lang.Any2OneChannel
 * @see org.jcsp.lang.One2AnyChannel
 * @see org.jcsp.lang.Any2AnyChannel
 *
 * @author P.H.Welch
 */

public class BlackHoleChannel implements ChannelOutput
{
    /**
     * Write an Object to the channel and loose it.
     *
     * @param value the object to write to the channel.
     */
    public void write(Object object)
    {
    }
}