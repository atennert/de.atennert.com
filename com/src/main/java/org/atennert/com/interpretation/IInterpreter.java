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

package org.atennert.com.interpretation;

import org.atennert.com.communication.IDataAcceptance;
import org.atennert.com.registration.INodeRegistration;
import org.atennert.com.util.DataContainer;
import org.atennert.com.util.MessageContainer;

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
    public DataContainer decode(MessageContainer message);

    /**
     * Encode data to a message that is to be send.
     *
     * @param data
     * @return
     */
    public String encode(DataContainer data);

    /**
     * Interpret a received request from another node.
     *
     * @param message
     * @param sender
     * @param acceptance
     * @param nr
     * @return
     */
    public String interpret(MessageContainer message, String sender, IDataAcceptance acceptance, INodeRegistration nr);
}
