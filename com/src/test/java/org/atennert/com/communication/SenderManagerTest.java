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

import org.atennert.com.util.MessageContainer;
import org.junit.Before;
import org.junit.Test;
import rx.functions.Func1;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Test cases for the {@link SenderManager} class
 */
public class SenderManagerTest {

    private SenderManager manager;
    private Map<String, ISender> map;

    @Before
    public void setup(){
        manager = new SenderManager();

        map = new HashMap<>();
        manager.setSender(map);
    }


    @Test(expected = NullPointerException.class)
    public void sendDataNoSender(){
        MessageContainer data = mock(MessageContainer.class);
        Func1<MessageContainer, MessageContainer> sender = manager.send("address", "protocol");
        sender.call(data);
    }

    @Test
    public void sendData(){
        MessageContainer data = new MessageContainer("interpreter", "message");
        ISender sender = mock(ISender.class);
        doReturn(data).when(sender).send(eq("address"), eq(data));
        map.put("protocol", sender);

        Func1<MessageContainer, MessageContainer> senderFunc = manager.send("address", "protocol");
        MessageContainer response = senderFunc.call(data);
        assertEquals(data, response);
    }
}
