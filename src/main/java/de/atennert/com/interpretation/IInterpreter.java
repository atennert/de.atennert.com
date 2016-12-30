package de.atennert.com.interpretation;

import de.atennert.com.communication.IDataAcceptance;
import de.atennert.com.util.DataContainer;
import de.atennert.com.util.MessageContainer;
import de.atennert.com.util.Session;
import rx.Scheduler;

/**
 * This class serves as Interface for all implemented Interpreters. The type
 * name of the interpreter must not contain @@.
 */
public interface IInterpreter
{

    /**
     * Decode a received response for a sent request into a target data format.
     *
     * @param message
     * @return
     */
    DataContainer decode(MessageContainer message);

    /**
     * Encode data to a message that is to be send.
     *
     * @param data
     * @return
     */
    String encode(DataContainer data);

    /**
     * Interpret a received request from another node.
     *
     * @param message
     * @param session
     * @param acceptance
     * @param scheduler
     */
    void interpret(MessageContainer message, Session session, IDataAcceptance acceptance, Scheduler scheduler);
}
