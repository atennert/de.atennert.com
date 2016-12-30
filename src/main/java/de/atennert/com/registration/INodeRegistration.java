package de.atennert.com.registration;

import java.util.Set;

/**
 * Interface for classes which implement a node registration.
 */
public interface INodeRegistration
{
    /**
     * Returns a nodes addresses.
     * @param node
     * @return
     */
    Set<String> getNodeReceiveAddresses(String node);

    /**
     * Returns the protocol associated with an address.
     * @param address
     * @return
     */
    String getNodeReceiveProtocol(String address);

    /**
     * Returns the interpreters from a node.
     * @param node
     * @return
     */
    Set<String> getNodeInterpreters(String node);

    /**
     * @param protocol
     * @return the interpreters that can be used with the given protocol
     */
    Set<String> getInterpretersForProtocol(String protocol);
}
