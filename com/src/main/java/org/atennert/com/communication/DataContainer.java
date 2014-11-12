
package org.atennert.com.communication;

public class DataContainer
{
    public final String valueId;

    public final Object data;

    public DataContainer(String valueId, Object data)
    {
        this.valueId = valueId;
        this.data = data;
    }
}
