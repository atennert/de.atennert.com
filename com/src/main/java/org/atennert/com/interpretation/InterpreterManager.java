/*******************************************************************************
 * Copyright 2015 Andreas Tennert
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/

package org.atennert.com.interpretation;

import org.atennert.com.communication.IDataAcceptance;
import org.atennert.com.util.DataContainer;
import org.atennert.com.util.MessageContainer;
import org.springframework.beans.factory.annotation.Required;
import rx.Scheduler;
import rx.functions.Func1;

import java.util.Map;


/**
 * This is the controller for all implemented interpreters.
 */
public class InterpreterManager {

    private Map<String, IInterpreter> interpreters;

    /**
     * Objects accessible for interpretation process
     */
    private IDataAcceptance acceptance;
    private Scheduler scheduler;


    @Required
    public void setInterpreters(final Map<String, IInterpreter> interpreters) {
        this.interpreters = interpreters;
    }

    @Required
    public void setDataAcceptance(final IDataAcceptance acceptance) {
        this.acceptance = acceptance;
    }

    /**
     * Sets the scheduler, that is forwarded to interpreters as parameter of the interpret method.
     * <em>This is supposed to be used by the Communicator instance only!!!</em>
     * @param scheduler The scheduler that will be forwarded to the interpreter
     */
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


    public void dispose() {
        this.scheduler = null;
        this.interpreters = null;
        this.acceptance = null;
    }

    /**
     * Encodes a specific data type to a String for transmission purposes. Used
     * by Communicator.
     *
     * @param type type of the data / the required interpreters
     * @return A RxJava function that encodes data to a string
     */
    public Func1<DataContainer, MessageContainer> encode(final String type) {
        return data -> new MessageContainer(type, interpreters.get(type).encode(data));
    }

    /**
     * Decodes a message into into usable data.
     *
     * @return A RxJava function that decodes a message to data
     */
    public Func1<MessageContainer, DataContainer> decode() {
        return message -> interpreters.get(message.interpreter).decode(message);
    }

    /**
     * Interprets a message. Used by specific receivers.
     *
     * @param senderAddress The address of the sender of the received message
     * @return A RxJava function that interprets a message
     */
    public Func1<MessageContainer, String> interpret(final String senderAddress) {
        return message -> interpreters.get(message.interpreter).interpret(message, senderAddress, acceptance, scheduler);
    }
}
