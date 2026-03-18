package uk.gov.courtservice.xhibit.client.util.security;

import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;


/*
 * Default implementation of the TerminalIDFactory that reads the
 * terminal ID from the operating system (the hostname is returned).
 */
public class DefaultTerminalIDFactory extends TerminalIDFactory {

    private static final Logger log = CSServices.getLogger(DefaultTerminalIDFactory.class);
    
    public String getTerminalID() {
        String hostname = "";
        try {
            log.debug("looking up terminal name");
            hostname = InetAddress.getLocalHost().getHostName();
            log.debug("terminal name=" + hostname);
            return hostname.toLowerCase();
        } catch (UnknownHostException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException(e);
        }
    }
}
