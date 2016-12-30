package de.atennert.com.util;

public class MessageContainer
{
    public enum Exception {
        NONE,
        UNKOWN_HOST,
        IO,
        EMPTY
    }

    public final String interpreter;

    public final String message;

    public final Exception error;

    public MessageContainer(String interpreter, String message, Exception error)
    {
        if (Exception.NONE.equals(error) && (interpreter == null || message == null)){
            throw new IllegalArgumentException("Expected an error other then N or useful data!");
        }
        this.interpreter = interpreter;
        this.message = message;
        this.error = error;
    }

    public MessageContainer(String interpreter, String message)
    {
        this(interpreter, message, Exception.NONE);
    }

    public MessageContainer(Exception error)
    {
        this(null, null, error);
    }

    public boolean hasException(){
        return error != Exception.NONE;
    }

    @Override
    public String toString()
    {
        return "MessageContainer: " + interpreter + " :: " + message;
    }
}
