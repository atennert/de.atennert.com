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

package org.atennert.com.interpretation;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.atennert.com.communication.Communicator;
import org.atennert.com.communication.DataContainer;
import org.atennert.com.communication.MessageContainer;
import org.atennert.com.registration.INodeRegistration;

/**
 * This is the controller for all implemented interpreters.
 */
public class InterpreterManager
{

    private class Encoder implements Callable<String>
    {
        private final DataContainer data;

        private final IInterpreter iif;

        public Encoder(DataContainer data, IInterpreter iif)
        {
            this.data = data;
            this.iif = iif;
        }

        @Override
        public String call() throws Exception
        {
            return iif.encode(data);
        }
    }

    private class Decoder implements Callable<DataContainer>
    {

        private final String message;

        private final IInterpreter iif;

        public Decoder(String message, IInterpreter iif)
        {
            this.message = message;
            this.iif = iif;
        }

        @Override
        public DataContainer call() throws Exception
        {
            return iif.decode(message);
        }

    }

    private class Interpreter implements Callable<String>
    {
        private final String message;
        private final String sender;

        private final IInterpreter iif;

        public Interpreter(String message, String sender, IInterpreter iif)
        {
            this.message = message;
            this.sender = sender;
            this.iif = iif;
        }

        @Override
        public String call() throws Exception
        {
            return iif.interpret(message, sender, objectMap, nr);
        }

    }

    private Map<String, IInterpreter> interpreter;

    /** Objects accessible for interpretation process */
    private Map<String, Object> objectMap;
    private INodeRegistration nr;
    private ExecutorService interpreterPool;
    private Communicator com;
    private static final int threadCount = 5;

    public void setInterpreter(Map<String, IInterpreter> cis)
    {
        this.interpreter = cis;
    }

    /**
     * Decodes a specific data type to a String for transmission purposes. Used
     * by Communicator.
     *
     * @param data
     * @param type
     *            type of the data
     * @return
     */
    public Future<String> encode(DataContainer data, String type)
    {
        final IInterpreter targetInterpreter = interpreter.get(type);
        IInterpreter ic = null;
        try
        {
            ic = targetInterpreter.getClass().newInstance();
        }
        catch ( final InstantiationException e )
        {
            // TODO Autoated catch block
            e.printStackTrace();
        }
        catch ( final IllegalAccessException e )
        {
            // TODO Auto-generatch block
            e.printStackTrace();
        }

        if ( ic == null )
        {
            return null;
        }

        return interpreterPool.submit(new Encoder(data, ic));
    }

    /**
     * Decodes a
     *
     * @param message
     * @param type
     *            the type of data
     * @return
     */
    public Future<DataContainer> decode(String message, String type)
    {
        final IInterpreter targetInterpreter = interpreter.get(type);
        IInterpreter ic = null;
        try
        {
            ic = targetInterpreter.getClass().newInstance();
        }
        catch ( final InstantiationException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( final IllegalAccessException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if ( ic == null )
        {
            return null;
        }

        return interpreterPool.submit(new Decoder(message, ic));
    }

    /**
     * Interprets a message. Used by specific receivers.
     *
     * @param message
     * @param type
     *            The type of the message.
     * @return
     */
    public Future<String> interpret(MessageContainer msgContainer, String sender)
    {
        final IInterpreter targetInterpreter = interpreter.get(msgContainer.interpreter);
        IInterpreter ic = null;
        try
        {
            ic = targetInterpreter.getClass().newInstance();
        }
        catch ( final InstantiationException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( final IllegalAccessException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if ( ic == null )
        {
            return null;
        }

        return interpreterPool.submit(new Interpreter(msgContainer.message, sender, ic));
    }

    public void dispose()
    {
        interpreterPool.shutdown();
        this.interpreter = null;
        this.objectMap = null;
        this.nr = null;
    }

    public void setObjectHandler(Map<String, Object> oh)
    {
        this.objectMap = oh;
    }

    public void setNodeRegistration(INodeRegistration nr)
    {
        this.nr = nr;
    }

    public void setCommunicator(Communicator com)
    {
        this.com = com;
    }

    public void init()
    {
        interpreterPool = Executors.newFixedThreadPool(threadCount);
        for ( final String interpreterName : interpreter.keySet() )
        {
            nr.addNodeInterpreter(com.getIdent(), interpreterName);
        }
    }
}
