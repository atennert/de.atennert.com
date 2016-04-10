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

package org.atennert.com.registration;

import java.util.Set;

/**
 * Interface for classes which implement a node registration.
 */
public interface INodeRegistration
{
    /**
     * Returns a nodes addresses.
     * @param node
     * @return
     */
    Set<String> getNodeReceiveAddresses(String node);

    /**
     * Returns the protocol associated with an address.
     * @param address
     * @return
     */
    String getNodeReceiveProtocol(String address);

    /**
     * Returns the interpreters from a node.
     * @param node
     * @return
     */
    Set<String> getNodeInterpreters(String node);

    /**
     * @param protocol
     * @return the interpreters that can be used with the given protocol
     */
    Set<String> getInterpretersForProtocol(String protocol);
}
