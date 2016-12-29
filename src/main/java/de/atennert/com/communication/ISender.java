package de.atennert.com.communication;

import de.atennert.com.util.MessageContainer;

/**
 * This is the interface for all implemented senders. A sender
 * is supposed to send a message to a given address.
 */
public interface ISender
{

    /**
     * Sends a message to a certain address. Returns
     * the response if there is one, null otherwise.
     *
     * @param address Device address
     * @param message The message
     * @return The response if there is one, null otherwise.
     */
    MessageContainer send(String address, MessageContainer message);
}
