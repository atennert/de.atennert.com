package de.atennert.com.util;

import rx.Scheduler;
import rx.functions.Action1;

/**
 * Base implementation for a session object.
 */
public abstract class Session implements Action1<String> {

    /**
     * Use this scheduler to decouple the sending of the response from the application thread.
     */
    public final Scheduler scheduler;

    /**
     * Initialize the Session
     * @param scheduler Set here the scheduler used in the Com-Framework
     */
    public Session(Scheduler scheduler)
    {
        this.scheduler = scheduler;
    }

    /**
     * @return The sender of the received message
     */
    public abstract String getSender();
}
