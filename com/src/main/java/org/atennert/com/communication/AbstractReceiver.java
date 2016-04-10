/*******************************************************************************
 * Copyright 2015 Andreas Tennert
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

import org.atennert.com.interpretation.InterpreterManager;
import org.springframework.beans.factory.annotation.Required;
import rx.Scheduler;

/**
 * This is the interface for all implemented communicators except the
 * Communicator which is the adapter for those.
 *
 * The run method, which is to be implemented is supposed to wait for incoming
 * requests and forward it to the interpret method of the InterpreterManager.
 *
 * TODO let receivers use the thread container of the communicator.
 */
public abstract class AbstractReceiver extends Thread
{
    /** Interpreter manager for interpreting received data. */
    protected InterpreterManager interpreter;
    /**
     * Use this scheduler to handle message receiving, data interpretation and sending
     * of responses.
     */
    protected Scheduler scheduler;

    /**
     * Set a scheduler that can be used to execute receiving and interpretation as well
     * as sending the response.
     * @param scheduler
     */
    void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Set the InterpreterManager.
     * @param interpreter
     */
    @Required
    public void setInterpreterManager(InterpreterManager interpreter)
    {
        this.interpreter = interpreter;
    }

    /**
     * This is called when the bean is stopped.
     */
    public void dispose()
    {
        this.interrupt();
        scheduler = null;
        interpreter = null;
    }
}
