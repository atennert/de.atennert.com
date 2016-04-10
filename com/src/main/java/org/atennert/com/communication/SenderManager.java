/*******************************************************************************
 * Copyright 2016 Andreas Tennert
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
import rx.functions.Func1;

/**
 * This is the controller class for all implemented senders.
 */
public class SenderManager {
    private Map<String, ISender> sender;

    @Required
    public void setSender(Map<String, ISender> sender) {
        this.sender = sender;
    }

    Func1<MessageContainer, MessageContainer> send(final String address, final String protocol) {
        return message -> sender.get(protocol).send(address, message);
    }
}
