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

import org.jcsp.util.ints.*;

/**
 * Defines an interface for a factory that can create arrays of integer carrying channels with
 * user-definable buffering semantics.
 *
 * @author Quickstone Technologies Limited
 */
public interface BufferedChannelIntArrayFactory
{
    /**
     * Creates a populated array of <code>n</code> <code>One2One</code> channels with the
     * specified buffering behaviour.
     *
     * @param buffer the buffer implementation to use.
     * @param n the size of the array.
     * @return the created array of channels.
     */
    public One2OneChannelInt[] createOne2One(ChannelDataStoreInt buffer, int n);

    /**
     * Creates a populated array of <code>n</code> <code>Any2One</code> channels with the
     * specified buffering behaviour.
     *
     * @param buffer the buffer implementation to use.
     * @param n the size of the array.
     * @return the created array of channels.
     */
    public Any2OneChannelInt[] createAny2One(ChannelDataStoreInt buffer, int n);

    /**
     * Creates a populated array of <code>n</code> <code>One2Any</code> channels with the
     * specified buffering behaviour.
     *
     * @param buffer the buffer implementation to use.
     * @param n the size of the array.
     * @return the created array of channels.
     */
    public One2AnyChannelInt[] createOne2Any(ChannelDataStoreInt buffer, int n);

    /**
     * Creates a populated array of <code>n</code> <code>Any2Any</code> channels with the
     * specified buffering behaviour.
     *
     * @param buffer the buffer implementation to use.
     * @param n the size of the array.
     * @return the created array of channels.
     */
    public Any2AnyChannelInt[] createAny2Any(ChannelDataStoreInt buffer, int n);
}