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
import org.atennert.com.util.DataContainer;
import org.atennert.com.util.MessageContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import rx.Scheduler;
import rx.SingleSubscriber;
import rx.functions.Func1;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test cases for the {@link InterpreterManager} class.
 */
public class InterpreterManagerTest {

    private InterpreterManager manager;
    private Map<String, IInterpreter> map;
    private DataContainer data;
    @Mock
    private IDataAcceptance dataAcceptance;
    @Mock
    private IInterpreter interpreter;
    @Mock
    private Scheduler scheduler;

    @Before
    public void setUp() {
        initMocks(this);

        manager = new InterpreterManager();

        data = new DataContainer("id", "data", mock(SingleSubscriber.class));

        map = new HashMap<>();

        doAnswer(inv -> ((DataContainer) inv.getArguments()[0]).data).when(interpreter).encode(any(DataContainer.class));
        doAnswer(inv -> data).when(interpreter).decode(any(MessageContainer.class));
        doAnswer(inv -> ((MessageContainer) inv.getArguments()[0]).message).when(interpreter).interpret(
                any(MessageContainer.class), eq("sender"), eq(dataAcceptance), eq(scheduler));
        map.put("interpreter", interpreter);

        manager.setInterpreters(map);
        manager.setDataAcceptance(dataAcceptance);
        manager.setScheduler(scheduler);
    }

    @After
    public void tearDown(){
        manager.dispose();
    }

    @Test(expected = NullPointerException.class)
    public void encodeNoInterpreter() {
        manager.setInterpreters(null);
        Func1<DataContainer, MessageContainer> func = manager.encode("interpreter");
        func.call(data);
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
        verify(interpreter).encode(data);
    }

    @Test
    public void decode() {
        MessageContainer container = new MessageContainer("interpreter", "message");

        Func1<MessageContainer, DataContainer> func = manager.decode();
        DataContainer s = func.call(container);
        assertEquals(data, s);
        verify(interpreter).decode(eq(container));
    }

    @Test
    public void interpret() {
        MessageContainer container = new MessageContainer("interpreter", "message");

        Func1<MessageContainer, String> func = manager.interpret("sender");
        String s = func.call(container);
        assertEquals(container.message, s);
        verify(interpreter).interpret(eq(container), eq("sender"), eq(dataAcceptance), eq(scheduler));
    }
}
