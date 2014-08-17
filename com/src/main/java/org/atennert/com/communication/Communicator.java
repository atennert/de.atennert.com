/*******************************************************************************
 * Copyright 2012 Andreas Tennert
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/

package org.atennert.com.communication;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.atennert.com.interpretation.InterpreterManager;
import org.atennert.com.registration.IClientRegistration;
import org.atennert.com.registration.INodeRegistration;

/**
 * Adapter to access package functions from program.
 */
public class Communicator
{

    private static Communicator INSTANCE = null;

    public static Communicator getInstance()
    {
        if( INSTANCE == null )
        {
            INSTANCE = new Communicator();
        }
        return INSTANCE;
    }

    private SenderManager sm;
    private String name;
    private INodeRegistration nr;
    private IClientRegistration cr;

    private InterpreterManager im;
    private boolean isServer;

    private String serverName;
    private Map<String, String> serverAddresses;
    private List<String> serverInterpreter;

    public void setName( String name )
    {
        this.name = name;
    }

    public void setSenderManager( SenderManager cm )
    {
        this.sm = cm;
    }

    public void setNodeRegistration( INodeRegistration nr )
    {
        this.nr = nr;
    }

    public void setInterpreterManager( InterpreterManager im )
    {
        this.im = im;
    }

    public void setIsServer( boolean isServer )
    {
        this.isServer = isServer;
    }

    public void setClientRegistration( IClientRegistration cr )
    {
        this.cr = cr;
    }

    public void setServerName( String serverName )
    {
        this.serverName = serverName;
    }

    public void setServerAddresses( Map<String, String> serverAddresses )
    {
        this.serverAddresses = serverAddresses;
    }

    public void setServerInterpreter( List<String> serverInterpreter )
    {
        this.serverInterpreter = serverInterpreter;
    }

    /**
     * Initializes the communicator. (Spring function)
     */
    public void init()
    {
        if( isServer )
        {

        }
        else
        {
            // register server connections
            nr.registerNode( serverName );
            for( final String iptr : serverInterpreter )
            {
                nr.addNodeInterpreter( serverName, iptr );
            }
            for( final String key : serverAddresses.keySet() )
            {
                nr.addNodeAddressProtocol( serverName, serverAddresses.get( key ), key );
            }
        }
        nr.registerNode( name );
    }

    /**
     * Sends a message to a target host and, if provided, returns the raw
     * content of the answer.
     *
     * @param hostname
     *            Target, the data has to be send to
     * @param data
     *            The data to send
     * @param type
     *            The interpreter type
     */
    public Future<Object> send( String hostname, Object data, String type )
    {
        final String address = getTargetAddress( hostname );
        final String protocol = nr.getNodeProtocol( address );
        String response = null;
        // FIXME let threads wait for response
        try
        {
            response = sm.send( address, type + "@@" + im.encode( data, type ).get(), protocol )
                    .get();
        }
        catch( final InterruptedException e )
        {
            e.printStackTrace();
        }
        catch( final ExecutionException e )
        {
            e.printStackTrace();
        }
        if( response == null || !response.contains( "@@" ) )
        {
            return null;
        }
        final String rspParts[] = response.split( "@@", 2 );
        return im.decode( rspParts[1], rspParts[0] );
    }

    /**
     * This method forwards a message of a specified interpreter type to another
     * node. <br>
     * <br>
     * <i>Only Interpreters should use this method.</i>
     *
     * @param hostname
     * @param message
     * @param type
     * @return
     */
    public String forward( String hostname, String message, String type )
    {
        // TODO no return value
        final String address = getTargetAddress( hostname );
        final String protocol = nr.getNodeProtocol( address );
        String response = null;
        // FIXME let threads wait for response
        try
        {
            response = sm.send( address, type + "@@" + message, protocol ).get();
        }
        catch( final InterruptedException e )
        {
            e.printStackTrace();
        }
        catch( final ExecutionException e )
        {
            e.printStackTrace();
        }
        if( response == null || !response.contains( "@@" ) )
        {
            return null;
        }

        final String rspParts[] = response.split( "@@", 2 );
        if( rspParts[0].equals( type ) )
        {
            return rspParts[1];
        }

        return null;
    }

    /**
     * Returns the address of a given host name (node name).
     *
     * @param hostname
     *            Node name
     * @return Node address
     */
    public String getTargetAddress( String hostname )
    {
        final Set<String> addresses = nr.getNodeAddresses( hostname );
        String address = null;
        final Iterator<String> iter = addresses.iterator();
        if( iter.hasNext() )
        {
            address = iter.next();
        }
        return address;
    }

    /**
     * Stop the Communicator. (Spring function)
     */
    public synchronized void dispose()
    {
        if( !isServer )
        {
            // unregister at server
            final Object data = cr.formatUnregistrationData( name );
            send( serverName, data, cr.getInterpreterID() );
        }
        sm = null;
        im = null;
        nr = null;
        INSTANCE = null;
    }

    /**
     * @return The nodes name
     */
    public String getIdent()
    {
        return name;
    }

    /**
     * Should only be used by ReceiverManager.
     */
    protected synchronized void setReceiverReady()
    {
        // register at server
        if( !isServer )
        {
            final Set<String> addresses = nr.getNodeAddresses( name );
            final Map<String, String> addressesProtocols = new HashMap<String, String>();
            for( final String address : addresses )
            {
                addressesProtocols.put( address, nr.getNodeProtocol( address ) );
            }

            final Set<String> interpreters = nr.getNodeInterpreters( name );

            final Object data = cr.formatRegistrationData( name, interpreters, addressesProtocols );
            send( serverName, data, cr.getInterpreterID() );
        }
    }
}
