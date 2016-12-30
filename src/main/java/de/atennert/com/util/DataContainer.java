package de.atennert.com.util;

import rx.SingleSubscriber;

/**
 * Container class for exchanging data between the com framework
 * and the application.
 */
public class DataContainer
{
    /** Data identifier (key or name) */
    public final String dataId;

    /** The data */
    public final Object data;

    /** Subscriber that will get a response after the data has been requested */
    public final SingleSubscriber<DataContainer> subscriber;

    public DataContainer(String dataId, Object data, SingleSubscriber<DataContainer> subscriber)
    {
        this.dataId = dataId;
        this.data = data;
        this.subscriber = subscriber;
    }
}
