package de.atennert.com.communication;

import de.atennert.com.util.DataContainer;

/**
 * Interface that is used to start data evaluation after a data
 * packet was received and pre-analysed by the communication
 * system.
 *
 * @startuml
 * participant Receiver
 * participant Interpreter
 * participant IDataAcceptance
 * Receiver -> Interpreter : interpret(host, message)
 * Interpreter -> IDataAcceptance : evaluateData(host, data)
 * IDataAcceptance -> Interpreter : <responseData>
 * Interpreter -> Receiver : <responseMessage>
 * @enduml
 *
 * @author Andreas Tennert
 */
public interface IDataAcceptance
{
    /**
     * Evaluate received data as part of the interpretation process from
     * the COM framework.
     *
     * @param senderAddress address from the host, that has sent the data
     * @param data the received data
     */
    void accept(String senderAddress, DataContainer data);
}
