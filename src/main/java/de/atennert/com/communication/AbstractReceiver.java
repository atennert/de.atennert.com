package de.atennert.com.communication;

import de.atennert.com.interpretation.InterpreterManager;
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
