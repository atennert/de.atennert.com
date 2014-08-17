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
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This is the controller class for all implemented senders.
 */
public class SenderManager {
	
	private class Sender implements Callable<String>{

		private String message;
		private String address;
		
		private ISender sender;
		
		public Sender(String message, String address, ISender sender){
			this.message = message;
			this.address = address;
			this.sender = sender;
		}
		
		public String call() throws Exception {
			
			return sender.send(address, message);
		}
		
	}

	private ExecutorService senderPool;
	private int threadCount = 5;
	private Map<String, ISender> sender;

	public void setSender(Map<String,ISender> sender){
		this.sender = sender;
	}
	
	public Set<String> getSendProtocols(){
		// TODO (questionable since protocols can be fetched from registration)
		return null;
	}

	public void init(){
		senderPool = Executors.newFixedThreadPool(threadCount);
	}
	
	protected Future<String> send(String address, String message, String protocol){
		ISender targetSender = sender.get(protocol);
		ISender sndr = null;
		try {
			sndr = targetSender.getClass().newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (sndr == null)
			return null;
		
		return senderPool.submit(new Sender(message, address, sndr));
	}
	
	public void dispose(){
		senderPool.shutdown();
	}
}
