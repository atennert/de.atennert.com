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

package org.atennert.com.interpretation;

import org.atennert.com.communication.IDataAcceptance;
import org.atennert.com.registration.INodeRegistration;
import org.atennert.com.util.DataContainer;
import org.atennert.com.util.MessageContainer;
import org.junit.Before;
import org.junit.Test;
import rx.SingleSubscriber;
import rx.functions.Func1;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test cases for the {@link InterpreterManager} class.
 */
public class InterpreterManagerTest {

    private InterpreterManager manager;
    private Map<String, IInterpreter> map;
    private DataContainer data;

    @Before
    public void setUp() {
        manager = new InterpreterManager();

        data = new DataContainer("id", "data", mock(SingleSubscriber.class));

        map = new HashMap<>();
        manager.setInterpreters(map);

        IInterpreter interpreter = mock(IInterpreter.class);
        doAnswer(inv -> ((DataContainer) inv.getArguments()[0]).data).when(interpreter).encode(any(DataContainer.class));
        doAnswer(inv -> data).when(interpreter).decode(any(MessageContainer.class));
        doAnswer(inv -> ((MessageContainer) inv.getArguments()[0]).message).when(interpreter).interpret(
                any(MessageContainer.class), eq("sender"), any(IDataAcceptance.class));
        map.put("interpreter", interpreter);
    }

    @Test(expected = NullPointerException.class)
    public void encodeNoInterpreter() {
        manager.setInterpreters(null);
        DataContainer container = new DataContainer(null, null, null);
        Func1<DataContainer, MessageContainer> func = manager.encode("interpreter");
        func.call(container);
    }

    @Test(expected = NullPointerException.class)
    public void decodeNoInterpreter() {
        manager.setInterpreters(null);
        MessageContainer container = mock(MessageContainer.class);
        Func1<MessageContainer, DataContainer> func = manager.decode();
        func.call(container);
    }

    @Test
    public void encode() {
        Func1<DataContainer, MessageContainer> func = manager.encode("interpreter");
        MessageContainer mc = func.call(data);
        assertEquals(data.data, mc.message);
        assertEquals("interpreter", mc.interpreter);
    }

    @Test
    public void decode() {
        MessageContainer container = new MessageContainer("interpreter", "message");

        Func1<MessageContainer, DataContainer> func = manager.decode();
        DataContainer s = func.call(container);
        assertEquals(data, s);
    }

    @Test
    public void interpret() {
        MessageContainer container = new MessageContainer("interpreter", "message");

        Func1<MessageContainer, String> func = manager.interpret("sender");
        String s = func.call(container);
        assertEquals(container.message, s);
    }
}
