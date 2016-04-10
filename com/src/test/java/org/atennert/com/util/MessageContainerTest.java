/*******************************************************************************
 * Copyright 2016 Andreas Tennert
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/

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
