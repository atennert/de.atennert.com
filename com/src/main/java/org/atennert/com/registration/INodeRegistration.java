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

package org.atennert.com.registration;

import java.util.Set;

/**
 * Interface for classes which implement a node registration.
 */
public interface INodeRegistration {
	
	/**
	 * Register a new node.
	 * @param name
	 * @param address
	 */
	public void registerNode(String name);
	
	/**
	 * Unregister a node.
	 * @param name
	 * @return
	 */
	public void unregisterNode(String name);
	
	/**
	 * Returns the number of registered nodes.
	 * @return
	 */
	public int getNodeCount();
	
	/**
	 * Returns the names of all registered nodes.
	 * @return
	 */
	public Set<String> getNodeNames();
	
	/**
	 * Add a nodes address and the associated protocol.
	 * @param node
	 * @param address
	 * @param protocol
	 */
	public void addNodeAddressProtocol(String node, String address, String protocol);
	
	/**
	 * Returns a nodes addresses.
	 * @param node
	 * @return
	 */
	public Set<String> getNodeAddresses(String node);
	
	/**
	 * Returns the node belonging to an address.
	 * @param address
	 * @return
	 */
	public String getNodeName(String address);
	
	/**
	 * Returns the address associated with a node protocol combination.
	 * @param node
	 * @param protocol
	 * @return
	 */
	public String getNodeAddress(String node, String protocol);
	
	/**
	 * Returns the protocols of a node.
	 * @param node
	 * @return
	 */
	public Set<String> getNodeProtocols(String node);
	
	/**
	 * Returns the protocol associated with an address.
	 * @param address
	 * @return
	 */
	public String getNodeProtocol(String address);
	
	/**
	 * Deletes an entry identified by the address.
	 * @param address
	 */
	public void deleteNodeAddressProtocol(String address);
	
	/**
	 * Deletes an entry identified by a node protocol combination.
	 * @param node
	 * @param protocol
	 */
	public void deleteNodeAddressProtocol(String node, String protocol);
	
	/**
	 * Adds a node interpreter combination.
	 * @param node
	 * @param interpreter
	 */
	public void addNodeInterpreter(String node, String interpreter);
	
	/**
	 * Returns the interpreters from a node.
	 * @param node
	 * @return
	 */
	public Set<String> getNodeInterpreters(String node);
	
	/**
	 * Returns the nodes using the specified interpreter.
	 * @param node
	 * @return
	 */
	public Set<String> getInterpreterNodes(String interpreter);
	
	/**
	 * Deletes a node interpreter combination.
	 * @param node
	 * @param interpreter
	 */
	public void deleteNodeInterpreter(String node, String interpreter);
}