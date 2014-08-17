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

import java.util.Map;

import org.atennert.com.interpretation.InterpreterManager;
import org.atennert.com.registration.INodeRegistration;

/**
 * Controller class for all concrete receivers.
 */
public class ReceiverManager {
	private Map<String,Receiver> receiver;
	private InterpreterManager interpreter;
	private INodeRegistration nr;
	private Communicator com;
	
	public void setReceiver(Map<String,Receiver> cis){
		this.receiver = cis;
	}
	
	public void setNodeRegistration(INodeRegistration nr){
		this.nr = nr;
	}
	
	public void dispose(){
		this.receiver = null;
		interpreter = null;
		nr = null;
	}
	
	public void setInterpreterManager(InterpreterManager interpreter){
		this.interpreter = interpreter;
	}
	
	public void setCommunicator(Communicator com){
		this.com = com;
	}
	
	public void init(){
		Receiver rif;
		for(String key : receiver.keySet()){
			rif = receiver.get(key);
			rif.setInterpreterManager(interpreter);
			rif.start();
			nr.addNodeAddressProtocol(com.getIdent(), rif.getAddress(), key);
		}
		
		com.setReceiverReady();
	}
}
