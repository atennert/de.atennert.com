package de.atennert.com.interpretation;

import de.atennert.com.communication.IDataAcceptance;
import de.atennert.com.util.DataContainer;
import de.atennert.com.util.MessageContainer;
import de.atennert.com.util.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import rx.Scheduler;
import rx.SingleSubscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test cases for the {@link InterpreterManager} class.
 */
public class InterpreterManagerTest {

    private InterpreterManager manager;
    private Map<String, IInterpreter> map;
    private DataContainer data;
    @Mock
    private Session session;
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
                any(MessageContainer.class), eq(session), eq(dataAcceptance), eq(scheduler));
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

        Action1<MessageContainer> func = manager.interpret(session);
        func.call(container);
        verify(interpreter).interpret(eq(container), eq(session), eq(dataAcceptance), eq(scheduler));
    }
}
