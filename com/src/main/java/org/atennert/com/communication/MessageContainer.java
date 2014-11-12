
package org.atennert.com.communication;

public class MessageContainer
{
    public final String interpreter;

    public final String message;

    public MessageContainer(String interpreter, String message)
    {
        this.interpreter = interpreter;
        this.message = message;
    }
}
