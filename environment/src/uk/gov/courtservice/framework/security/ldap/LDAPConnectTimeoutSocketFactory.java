/**
 * 
 */
package uk.gov.courtservice.framework.security.ldap;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSocketFactory;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryServiceImpl;

/**
 * This factory is used to take advantage of the socket timeout functionality
 * included in jdk 5.
 * 
 * @author bzjrnl
 */
public class LDAPConnectTimeoutSocketFactory implements LDAPSocketFactory {

    private static final Logger log = Logger.getLogger(ActiveDirectoryServiceImpl.class);

    private final int timeout;

    /**
     * Construct a new socket factory with the specified timeout
     * 
     * @param timeout
     */
    public LDAPConnectTimeoutSocketFactory(int timeout) {
        this.timeout = timeout;
    }

    /**
     * LDAPSocketFactory implementation, construct a new socket factory with the
     * specified timeout.
     * 
     * @param timeout,
     *            the timeout to use
     */
    public Socket makeSocket(String host, int port) throws LDAPException {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            try {
                return _makeSocket(host, port);
            } finally {
                log.debug("Connecting to server " + host + ":" + port + " took "
                        + (System.currentTimeMillis() - starttime) + "ms.");
            }
        } else {
            return _makeSocket(host, port);
        }

    }

    private Socket _makeSocket(String host, int port) throws LDAPException {
        try {
            Socket socket = new Socket();
            if(timeout > 0) {
                socket.setSoTimeout(timeout);
                socket.connect(new InetSocketAddress(host, port), timeout);
            } else {
                socket.connect(new InetSocketAddress(host, port));
            }
            return socket;
        } catch (UnknownHostException e) {
            String msg = "Could not connect to server " + host + ":" + port + ".";
            log.warn(msg, e);
            throw new LDAPException(msg, LDAPException.CONNECT_ERROR);
        } catch (IOException e) {
            String msg = "Could not connect to server " + host + ":" + port + ".";
            log.warn(msg, e);
            throw new LDAPException(msg, LDAPException.CONNECT_ERROR);
        }
    }
}