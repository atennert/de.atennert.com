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

import rx.SingleSubscriber;

/**
 * Container class for exchanging data between the com framework
 * and the application.
 */
public class DataContainer
{
    /** Data identifier (key or name) */
    public final String dataId;

    /** The data */
    public final Object data;

    /** Subscriber that will get a response after the data has been requested */
    public final SingleSubscriber<DataContainer> subscriber;

    public DataContainer(String dataId, Object data, SingleSubscriber<DataContainer> subscriber)
    {
        this.dataId = dataId;
        this.data = data;
        this.subscriber = subscriber;
    }
}
