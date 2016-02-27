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
    public enum Error {
        NONE,
        UNKOWN_HOST,
        IO;
    }

    public final String interpreter;

    public final String message;

    public final Error error;

    public MessageContainer(String interpreter, String message, Error error)
    {
        if (Error.NONE.equals(error) && (interpreter == null || message == null)){
            throw new IllegalArgumentException("Expected an error other then N or useful data!");
        }
        this.interpreter = interpreter;
        this.message = message;
        this.error = error;
    }

    public MessageContainer(String interpreter, String message)
    {
        this(interpreter, message, Error.NONE);
    }

    public MessageContainer(Error error)
    {
        this(null, null, error);
    }

    public boolean hasError(){
        return error != Error.NONE;
    }

    @Override
    public String toString()
    {
        return "MessageContainer: " + interpreter + " :: " + message;
    }
}
