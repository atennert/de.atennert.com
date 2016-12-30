package de.atennert.com.communication;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.atennert.com.util.MessageContainer;
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
