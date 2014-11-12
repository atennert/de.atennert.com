/*******************************************************************************
 * Copyright 2012 Andreas Tennert
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package org.atennert.com.communication;

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
    public MessageContainer send(String address, MessageContainer message);
}
