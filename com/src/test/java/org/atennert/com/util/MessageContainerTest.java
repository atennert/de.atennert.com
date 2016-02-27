package org.atennert.com.util;

import org.junit.Assert;
import org.junit.Test;

public class MessageContainerTest
{
    @Test
    public void testErrorContainer(){
        MessageContainer mc = new MessageContainer(MessageContainer.Exception.UNKOWN_HOST);
        Assert.assertTrue(mc.hasException());

        mc = new MessageContainer(MessageContainer.Exception.IO);
        Assert.assertTrue(mc.hasException());

        mc = new MessageContainer(MessageContainer.Exception.EMPTY);
        Assert.assertTrue(mc.hasException());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testContainerException(){
        new MessageContainer(MessageContainer.Exception.NONE);
    }

    @Test
    public void checkSimpleCorrectCase(){
        MessageContainer mc = new MessageContainer("Interpreter", "message");
        Assert.assertFalse(mc.hasException());
    }
}
