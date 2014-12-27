
package org.atennert.com.communication;

import java.util.concurrent.Future;

/**
 * Interface that is used to start data evaluation after a data
 * packet was received and pre-analysed by the communication
 * system.
 *
 * @author Andreas Tennert
 */
public interface IDataAcceptance
{
    Future<DataContainer> evaluateData(String hostAddress, DataContainer data);
}
