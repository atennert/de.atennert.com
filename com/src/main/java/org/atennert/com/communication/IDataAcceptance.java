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

import org.atennert.com.util.DataContainer;

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
