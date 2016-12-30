package de.atennert.com.util;

import rx.SingleSubscriber;

import java.util.Map;

/**
 * Extension of the DataContainer whose data is a Map. Instead of just one
 * key value pair it can contain multiple key value pairs. The dataId field
 * of this classes instances is always <code>null</code>.
 */
public class MapDataContainer extends DataContainer
{
    public MapDataContainer(Map<String, Object> data, SingleSubscriber<DataContainer> subscriber)
    {
        super(null, data, subscriber);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getData()
    {
        return (Map<String, Object>)data;
    }
}
