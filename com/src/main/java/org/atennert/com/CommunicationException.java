
package org.atennert.com;

public class CommunicationException extends RuntimeException
{
    private static final long serialVersionUID = -898415758047014162L;

    public CommunicationException(Throwable cause)
    {
        super(cause);
    }

    public CommunicationException(String message)
    {
        super(message);
    }
}
