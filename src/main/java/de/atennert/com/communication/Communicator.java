package de.atennert.com.communication;

import de.atennert.com.interpretation.InterpreterManager;
import de.atennert.com.registration.INodeRegistration;
import de.atennert.com.util.DataContainer;
import org.springframework.beans.factory.annotation.Required;
import rx.Scheduler;
import rx.Single;

import java.util.Iterator;
import java.util.Set;

/**
 * Adapter to access package functions from program.
 */
public class Communicator implements ICommunicatorAccess {
    private SenderManager senderManager;
    private INodeRegistration nodeRegistration;

    private Set<AbstractReceiver> receivers;

    private InterpreterManager interpreterManager;

    private Scheduler scheduler;

    @Required
    public void setSenderManager(final SenderManager senderManager) {
        this.senderManager = senderManager;
    }

    @Required
    public void setNodeRegistration(final INodeRegistration nodeRegistration) {
        this.nodeRegistration = nodeRegistration;
    }

    @Required
    public void setReceivers(final Set<AbstractReceiver> receivers) {
        this.receivers = receivers;
    }

    @Required
    public void setInterpreterManager(final InterpreterManager interpreterManager) {
        this.interpreterManager = interpreterManager;
    }

    /**
     * Set an executor. This is needed for an internal scheduler.
     *
     * @param scheduler The thread scheduler<br>
     *                 Instantiate in Spring for example with:<br>
     *                 &lt;bean id="scheduler" class="rx.schedulers.Schedulers" factory-method="from"&gt;<br>
     *                 &lt;constructor-arg index="0"&gt;<br>
     *                 &lt;bean id="executor" class="java.util.concurrent.Executors" factory-method="newFixedThreadPool"
     *                 destroy-method="shutdown"&gt;<br>
     *                 &lt;constructor-arg index="0" value="4" /&gt;&lt;!-- Thread count --&gt;<br>
     *                 &lt;/bean&gt;<br>
     *                 &lt;/constructor-arg&gt;<br>
     *                 &lt;/bean&gt;
     */
    @Required
    public void setScheduler(final Scheduler scheduler) {
        this.scheduler = scheduler;
    }


    /**
     * Initializes the communicator. (Spring function)
     */
    public void init() {
        interpreterManager.setScheduler(scheduler);
        initializeReceivers();
    }

    /**
     * Stop the Communicator. (Spring function)
     */
    public synchronized void dispose() {
        scheduler = null;
        senderManager = null;
        interpreterManager = null;
        nodeRegistration = null;
        receivers = null;
    }

    private void initializeReceivers() {
        for (AbstractReceiver r : receivers) {
            r.setScheduler(scheduler);
            r.start();
        }
    }

    @Override
    public void send(final String hostName, final DataContainer data) {
        final String address = getHostAddress(hostName);
        final String protocol = nodeRegistration.getNodeReceiveProtocol(address);
        final String interpreter = getInterpreterForProtocol(hostName, protocol);

        Single.just(data)
                .subscribeOn(scheduler)
                .map(interpreterManager.encode(interpreter))
                .map(senderManager.send(address, protocol))
                .map(interpreterManager.decode())
                .subscribe(data.subscriber);
    }

    /**
     * Returns the address of a given host name (node name).
     *
     * @param hostname Node name
     * @return Node address
     */
    private String getHostAddress(final String hostname) {
        final Iterator<String> iter = nodeRegistration.getNodeReceiveAddresses(hostname).iterator();
        if (iter.hasNext()) {
            return iter.next();
        }
        return null;
    }

    /**
     * Returns an interpreter that can be used with the given protocol.
     *
     * @param protocol communication protocol
     * @return interpreter ID
     */
    private String getInterpreterForProtocol(final String hostName, final String protocol) {
        Set<String> nodeInterpreters = nodeRegistration.getNodeInterpreters(hostName);
        for (String interpreter : nodeRegistration.getInterpretersForProtocol(protocol)) {
            if (nodeInterpreters.contains(interpreter)) {
                return interpreter;
            }
        }
        return null;
    }
}
