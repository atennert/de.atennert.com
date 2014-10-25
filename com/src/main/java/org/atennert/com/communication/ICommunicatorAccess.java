/*******************************************************************************
 * Copyright 2014 Andreas Tennert
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

import java.util.concurrent.Future;

/**
 * Interface for accessing communicator functionality.
 *
 * @author Andreas Tennert
 */
public interface ICommunicatorAccess
{
    /**
     * Sends a message to a target host and, if provided, returns the raw
     * content of the answer.
     *
     * @param hostname
     *            Target, the data has to be send to
     * @param data
     *            The data to send
     * @param type
     *            The interpreter type
     */
    Future<Object> send(String hostname, Object data, String type);

    /**
     * This method forwards a message of a specified interpreter type to another
     * node. <br>
     * <br>
     * <i>Only Interpreters should use this method.</i>
     *
     * @param hostname
     * @param message
     * @param type
     * @return
     */
    String forward(String hostname, String message, String type);
}
