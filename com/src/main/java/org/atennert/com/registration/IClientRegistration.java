/*******************************************************************************
 * Copyright 2012 Andreas Tennert
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

package org.atennert.com.registration;

import java.util.Map;
import java.util.Set;

/**
 * Interface for a class that creates a message for (un-)registering the client
 * at the server.
 */
public interface IClientRegistration<T>
{

    /**
     * @param name
     *            The name of the client
     * @param interpreters
     *            The nodes own interpreters
     * @param addressesProtocols
     *            The nodes own addresses and protocols
     * @return The formated data for registering at the server via the
     *         interpreter named by the ID.
     */
    public T formatRegistrationData (String name, Set<String> interpreters,
            Map<String, String> addressesProtocols);

    /**
     * @param name
     *            The name of the client
     * @return The formated data for unregistering at the server via the
     *         interpreter named by the ID.
     */
    public T formatUnregistrationData (String name);

    /**
     * @return The ID of the interpreter to use for encoding the data.
     */
    public String getInterpreterID ();
}
