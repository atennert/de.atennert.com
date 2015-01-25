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

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.atennert.com.util.MessageContainer;
import org.springframework.beans.factory.annotation.Required;

/**
 * This is the controller class for all implemented senders.
 */
public class SenderManager
{

    private class Sender implements Callable<MessageContainer>
    {

        private final MessageContainer message;
        private final String address;

        private final ISender sender;

        public Sender(MessageContainer message, String address, ISender sender)
        {
            this.message = message;
            this.address = address;
            this.sender = sender;
        }

        @Override
        public MessageContainer call() throws Exception
        {

            return sender.send(address, message);
        }

    }

    private ExecutorService senderPool;
    private static final int THREAD_COUNT = 3;
    private Map<String, ISender> sender;

    @Required
    public void setSender(Map<String, ISender> sender)
    {
        this.sender = sender;
    }


    public void init()
    {
        senderPool = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public void dispose()
    {
        senderPool.shutdown();
    }


    Future<MessageContainer> send(String address, MessageContainer message, String protocol) throws InstantiationException, IllegalAccessException
    {
        // TODO check for unknown protocol and throw exception

        ISender targetSender = sender.get(protocol);
        ISender sndr = null;

        sndr = targetSender.getClass().newInstance();

        if ( sndr == null )
        {
            return null;
        }

        return senderPool.submit(new Sender(message, address, sndr));
    }
}
