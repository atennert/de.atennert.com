
package org.atennert.com.communication;

import java.util.Map;

public class MapDataContainer extends DataContainer
{
    public MapDataContainer(Map<String, Object> data)
    {
        super(null, data);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getData()
    {
        return (Map<String, Object>)data;
    }
}
