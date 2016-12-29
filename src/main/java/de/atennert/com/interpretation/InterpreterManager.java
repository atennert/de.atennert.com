package de.atennert.com.interpretation;

import de.atennert.com.communication.IDataAcceptance;
import de.atennert.com.util.DataContainer;
import de.atennert.com.util.MessageContainer;
import de.atennert.com.util.Session;
import org.springframework.beans.factory.annotation.Required;
import rx.Scheduler;
import rx.functions.Action1;
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
     * @param session The session for receiving the message and sending the response
     * @return A RxJava function that interprets a message
     */
    public Action1<MessageContainer> interpret(final Session session) {
        return message -> interpreters.get(message.interpreter).interpret(message, session, acceptance, scheduler);
    }
}
