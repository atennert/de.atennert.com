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
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.atennert.com.communication.DataContainer;
import org.atennert.com.communication.IDataAcceptance;
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
            return iif.interpret(message, sender, acceptance, nr);
        }

    }

    private Map<String, IInterpreter> interpreter;

    /** Objects accessible for interpretation process */
    private IDataAcceptance acceptance;
    private INodeRegistration nr;
    private ExecutorService interpreterPool;
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
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Future<String> encode(DataContainer data, String type) throws InstantiationException, IllegalAccessException
    {
        IInterpreter ic = interpreter.get(type).getClass().newInstance();

        return ic == null ? null : interpreterPool.submit(new Encoder(data, ic));
    }

    /**
     * Decodes a
     *
     * @param message
     * @param type
     *            the type of data
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Future<DataContainer> decode(String message, String type) throws InstantiationException, IllegalAccessException
    {
        IInterpreter ic = interpreter.get(type).getClass().newInstance();

        return ic == null ? null : interpreterPool.submit(new Decoder(message, ic));
    }

    /**
     * Interprets a message. Used by specific receivers.
     *
     * @param message
     * @param type
     *            The type of the message.
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Future<String> interpret(MessageContainer msgContainer, String sender) throws InstantiationException, IllegalAccessException
    {
        IInterpreter ic = interpreter.get(msgContainer.interpreter).getClass().newInstance();

        return ic == null ? null : interpreterPool.submit(new Interpreter(msgContainer.message, sender, ic));
    }

    public void dispose()
    {
        interpreterPool.shutdown();
        this.interpreter = null;
        this.acceptance = null;
        this.nr = null;
    }

    public void setDataAcceptance(IDataAcceptance acceptance)
    {
        this.acceptance = acceptance;
    }

    public void setNodeRegistration(INodeRegistration nr)
    {
        this.nr = nr;
    }

    public void init()
    {
        interpreterPool = Executors.newFixedThreadPool(threadCount);
    }

    public Set<String> getInterpreterIds()
    {
        return interpreter.keySet();
    }
}
