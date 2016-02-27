/*******************************************************************************
 * Copyright 2014 Andreas Tennert
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.atennert.com.interpretation.InterpreterManager;
import org.atennert.com.registration.IClientRegistration;
import org.atennert.com.registration.INodeRegistration;
import org.atennert.com.util.CommunicationException;
import org.atennert.com.util.DataContainer;
import org.atennert.com.util.MessageContainer;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

/**
 * Adapter to access package functions from program.
 */
public class Communicator implements ICommunicatorAccess
{
    private SenderManager sm;
    private String myHostName;
    private INodeRegistration nr;
    private IClientRegistration cr = null;

    private Map<String, AbstractReceiver> receiver;

    private InterpreterManager im;
    private boolean isServer = true;

    private String serverName = null;



    @Required
    public void setHostName(String hostName)
    {
        this.myHostName = hostName;
    }

    @Required
    public void setSenderManager(SenderManager sm)
    {
        this.sm = sm;
    }

    @Required
    public void setNodeRegistration(INodeRegistration nr)
    {
        this.nr = nr;
    }

    @Required
    public void setReceiver(Map<String, AbstractReceiver> cis)
    {
        this.receiver = cis;
    }

    @Required
    public void setInterpreterManager(InterpreterManager im)
    {
        this.im = im;
    }

    public void setIsServer(boolean isServer)
    {
        this.isServer = isServer;
    }

    public void setClientRegistration(IClientRegistration cr)
    {
        this.cr = cr;
    }

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }



    /**
     * Initializes the communicator. (Spring function)
     */
    public void init()
    {
        nr.registerNode(myHostName);

        registerInterpreters();

        initializeReceivers();

        if ( !isServer )
        {
            Assert.notNull(cr);
            Assert.notNull(serverName);

            registerAsClient();
        }
    }

    /**
     * Stop the Communicator. (Spring function)
     */
    public synchronized void dispose()
    {
        if ( !isServer )
        {
            // unregister at server
            final DataContainer data = cr.formatUnregistrationData(myHostName);
            send(serverName, data, cr.getInterpreterID());
        }
        sm = null;
        im = null;
        nr = null;
        receiver = null;
    }

    /**
     * Used for server communication (registration and deregistration)
     * @param hostName
     * @param data
     * @param interpreter
     */
    private void send(String hostName, DataContainer data, String interpreter)
    {
        String hostAddress = null;
        String protocol = null;
        for ( String address : nr.getNodeReceiveAddresses(hostName) )
        {
            protocol = nr.getNodeReceiveProtocol(address);
            Set<String> interpreters = nr.getInterpretersForProtocol(protocol);
            if ( interpreters.contains(interpreter) )
            {
                hostAddress = address;
                break;
            }
        }

        if ( hostAddress == null )
        {
            throw new CommunicationException("[Communicator.send] unable to find address for " + hostName);
        }

        try
        {
            sm.send(hostAddress, new MessageContainer(interpreter, im.encode(data, interpreter).get()), protocol);
        }
        catch ( final Exception e )
        {
            throw new CommunicationException(e);
        }
    }

    private void initializeReceivers()
    {
        AbstractReceiver rif;
        for ( String key : receiver.keySet() )
        {
            rif = receiver.get(key);
            rif.start();
            nr.addNodeReceiveAddressProtocol(myHostName, rif.getAddress(), key);
        }
    }

    private void registerInterpreters()
    {
        for ( final String interpreterName : im.getInterpreterIds() )
        {
            nr.addNodeInterpreter(myHostName, interpreterName);
        }
    }



    @Override
    public Future<DataContainer> send(String hostName, DataContainer data) throws CommunicationException
    {
        final String address = getHostAddress(hostName);
        final String protocol = nr.getNodeReceiveProtocol(address);
        final String interpreter = getInterpreterForProtocol(hostName, protocol);

        // FIXME let threads wait for response
        try
        {
            MessageContainer response= sm.send(address, new MessageContainer(interpreter, im.encode(data, interpreter).get()), protocol).get();

            return im.decode(response, response.interpreter);
        }
        catch ( final Exception e )
        {
            throw new CommunicationException(e);
        }
    }

    @Override
    public String forward(String hostName, String message) throws CommunicationException
    {
        final String address = getHostAddress(hostName);
        final String protocol = nr.getNodeReceiveProtocol(address);
        final String interpreter = getInterpreterForProtocol(hostName, protocol);
        MessageContainer response = null;

        // FIXME let threads wait for response
        try
        {
            response = sm.send(address, new MessageContainer(interpreter, message), protocol).get();
        }
        catch ( final Exception e )
        {
            throw new CommunicationException(e);
        }

        if ( response != null && response.interpreter.equals(interpreter) )
        {
            return response.message;
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
    private String getHostAddress(String hostname)
    {
        final Iterator<String> iter = nr.getNodeReceiveAddresses(hostname).iterator();
        if ( iter.hasNext() )
        {
            return iter.next();
        }
        return null;
    }

    /**
     * Returns an interpreter that can be used with the given protocol.
     *
     * @param protocol communication protocol
     * @return interpreter ID
     */
    private String getInterpreterForProtocol(String hostName, String protocol)
    {
        Set<String> nodeInterpreters = nr.getNodeInterpreters(hostName);
        final Iterator<String> iter = nr.getInterpretersForProtocol(protocol).iterator();
        while ( iter.hasNext() )
        {
            String interpreter = iter.next();
            if ( nodeInterpreters.contains(interpreter) )
            {
                return interpreter;
            }
        }
        return null;
    }

    private synchronized void registerAsClient()
    {
        final Set<String> addresses = nr.getNodeReceiveAddresses(myHostName);
        final Map<String, String> addressesProtocols = new HashMap<String, String>();
        for ( final String address : addresses )
        {
            addressesProtocols.put(address, nr.getNodeReceiveProtocol(address));
        }

        final Set<String> interpreters = nr.getNodeInterpreters(myHostName);

        final DataContainer data = cr.formatRegistrationData(myHostName, interpreters, addressesProtocols);
        send(serverName, data, cr.getInterpreterID());
    }
}
