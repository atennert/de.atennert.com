package de.atennert.com.communication;

import de.atennert.com.util.DataContainer;

/**
 * Interface for accessing communicator functionality.
 */
public interface ICommunicatorAccess
{
    /**
     * Sends a message to a target host and, if provided, returns the raw
     * content of the answer.
     *
     * @param hostname Target, the data has to be send to
     * @param data The data to send
     */
    void send(String hostname, DataContainer data);
}
