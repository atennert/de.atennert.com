
package org.atennert.com.communication;

import java.util.concurrent.Future;

public interface IDataAcceptance
{
    Future<DataContainer> evaluateData(String hostAddress, DataContainer data);
}
