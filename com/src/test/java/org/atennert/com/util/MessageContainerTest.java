package org.atennert.com.util;

import org.junit.Assert;
import org.junit.Test;

public class MessageContainerTest
{
    @Test
    public void testErrorContainer(){
        MessageContainer mc = new MessageContainer(MessageContainer.Error.UNKOWN_HOST);
        Assert.assertTrue(mc.hasError());

        mc = new MessageContainer(MessageContainer.Error.IO);
        Assert.assertTrue(mc.hasError());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testContainerException(){
        new MessageContainer(MessageContainer.Error.NONE);
    }

    @Test
    public void checkSimpleCorrectCase(){
        MessageContainer mc = new MessageContainer("Interpreter", "message");
        Assert.assertFalse(mc.hasError());
    }
}
