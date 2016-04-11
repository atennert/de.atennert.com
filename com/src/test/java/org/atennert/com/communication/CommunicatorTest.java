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

import org.atennert.com.interpretation.InterpreterManager;
import org.atennert.com.registration.INodeRegistration;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test cases for the {@link Communicator} class.
 */
public class CommunicatorTest {

    private final static String HOST_NAME = "host";
    private final static String HOST_ADDRESS = "address";
    private final static String HOST_PROTOCOL = "protocol";
    private final static String HOST_INTERPRETER = "interpreter";

    private Communicator communicator;

    @Mock
    private INodeRegistration registration;

    @Mock
    private SenderManager sender;

    @Mock
    private InterpreterManager interpreter;

    @Mock
    private ExecutorService executor;

    @Before
    public void setUp() {
        initMocks(this);

        communicator = new Communicator();
        communicator.setNodeRegistration(registration);
        communicator.setSenderManager(sender);
        communicator.setInterpreterManager(interpreter);
        communicator.setExecutor(executor);

        Set<String> addresses = new HashSet<>();
        addresses.add(HOST_ADDRESS);
        Set<String> interpreters = new HashSet<>();
        interpreters.add(HOST_INTERPRETER);
        doReturn(addresses).when(registration).getNodeReceiveAddresses(HOST_NAME);
        doReturn(HOST_PROTOCOL).when(registration).getNodeReceiveProtocol(HOST_ADDRESS);
        doReturn(interpreters).when(registration).getNodeInterpreters(HOST_NAME);
        doReturn(interpreters).when(registration).getInterpretersForProtocol(HOST_PROTOCOL);

        doAnswer(invocation -> {
            ((Runnable)invocation.getArguments()[0]).run();
            return null;
        }).when(executor).execute(any(Runnable.class));
    }

    @After
    public void tearDown(){
        communicator.dispose();
    }

    @Test
    public void init(){
        AbstractReceiver receiver = mock(AbstractReceiver.class);
        Set<AbstractReceiver> receivers = new HashSet<>();
        receivers.add(receiver);
        communicator.setReceivers(receivers);

        communicator.init();
        verify(receiver).setScheduler(any(Scheduler.class));
        verify(interpreter).setScheduler(any(Scheduler.class));
        verify(receiver).start();
    }

    @Test
    public void sendData() {
        String id = "id";
        String data = "data";
        SingleSubscriber<DataContainer> subscriber = spy(new SingleSubscriber<DataContainer>() {
            @Override
            public void onSuccess(DataContainer value) {
            }

            @Override
            public void onError(Throwable error) {
            }
        });
        DataContainer container = new DataContainer(id, data, subscriber);

        doReturn((Func1) dc -> new MessageContainer(HOST_INTERPRETER, "message")).when(interpreter).encode(HOST_INTERPRETER);
        doReturn((Func1) mc -> mc).when(sender).send(HOST_ADDRESS, HOST_PROTOCOL);
        doReturn((Func1) mc -> container).when(interpreter).decode();

        communicator.send(HOST_NAME, container);

        verify(interpreter).encode(eq(HOST_INTERPRETER));
        verify(sender).send(eq(HOST_ADDRESS), eq(HOST_PROTOCOL));
        verify(interpreter).decode();
        verify(subscriber).onSuccess(eq(container));
    }

    // TODO test error cases
}
