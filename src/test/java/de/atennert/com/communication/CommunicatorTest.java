package de.atennert.com.communication;

import de.atennert.com.interpretation.InterpreterManager;
import de.atennert.com.registration.INodeRegistration;
import de.atennert.com.util.DataContainer;
import de.atennert.com.util.MessageContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import rx.Scheduler;
import rx.SingleSubscriber;
import rx.functions.Action0;
import rx.functions.Func1;

import java.util.HashSet;
import java.util.Set;

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
    private Scheduler executor;

    @Before
    public void setUp() {
        initMocks(this);

        communicator = new Communicator();
        communicator.setNodeRegistration(registration);
        communicator.setSenderManager(sender);
        communicator.setInterpreterManager(interpreter);
        communicator.setScheduler(executor);

        Set<String> addresses = new HashSet<>();
        addresses.add(HOST_ADDRESS);
        Set<String> interpreters = new HashSet<>();
        interpreters.add(HOST_INTERPRETER);
        doReturn(addresses).when(registration).getNodeReceiveAddresses(HOST_NAME);
        doReturn(HOST_PROTOCOL).when(registration).getNodeReceiveProtocol(HOST_ADDRESS);
        doReturn(interpreters).when(registration).getNodeInterpreters(HOST_NAME);
        doReturn(interpreters).when(registration).getInterpretersForProtocol(HOST_PROTOCOL);

        Scheduler.Worker worker = mock(Scheduler.Worker.class);
        doAnswer(invocation -> {
            ((Action0)invocation.getArguments()[0]).call();
            return null;
        }).when(worker).schedule(any());
        doReturn(worker).when(executor).createWorker();
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
