/*******************************************************************************
 * Copyright 2015 Andreas Tennert
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/

package org.atennert.com.util;

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
