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

import java.util.Map;

/**
 * Extension of the DataContainer whose data is a Map. Instead of just one
 * key value pair it can contain multiple key value pairs. The dataId field
 * of this classes instances is always <code>null</code>.
 */
public class MapDataContainer extends DataContainer
{
    public MapDataContainer(Map<String, Object> data)
    {
        super(null, data);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getData()
    {
        return (Map<String, Object>)data;
    }
}
