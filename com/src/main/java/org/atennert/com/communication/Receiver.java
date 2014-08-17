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

import org.atennert.com.interpretation.InterpreterManager;

/**
 * This is the interface for all implemented communicators except the
 * Communicator which is the adapter for those.
 * 
 * The run method, which is to be implemented is supposed to wait for incoming
 * requests and forward it to the interpret method of the InterpreterManager.
 */
public abstract class Receiver extends Thread {
	
	protected InterpreterManager interpreter;

	/**
	 * Set the address as a String.
	 * @param address
	 */
	public abstract void setAddress(String address);
	
	/**
	 * Returns the address. Necessary for the ReceiverManager to get all addresses
	 * and safe them in the database.
	 * @return
	 */
	public abstract String getAddress();
	
	/**
	 * Set the InterpreterManager.
	 * @param interpreter
	 */
	public void setInterpreterManager(InterpreterManager interpreter){
		this.interpreter = interpreter;
	}
	
	/**
	 * This is called when the bean is stopped.
	 */
	public void dispose(){
		this.interrupt();
		interpreter = null;
	}
	
	/**
	 * This is called when the bean is created.
	 */
	public void init(){
	}
}
