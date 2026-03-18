package uk.gov.courtservice.xhibit.client.util.security;

import uk.gov.courtservice.framework.services.CSServices;


/*
 * Adapted version of TranslationBundlesFactory
 */
public abstract class TerminalIDFactory {
    private static TerminalIDFactory instance;

    /**
     * Get the singleton using the discovery pattern.
     * 
     * @return the singleton instance.
     */
    public static synchronized TerminalIDFactory getInstance() {
        if (instance == null) {
            instance = createTerminalIDFactory();
        }
        return instance;
    }
    
    /*
     * getTerminalID returns the name of the workstation the user 
     * is logged in on.  This is usually the hostname.  However for
     * testing purposes it can be useful to set this to another value.
     * The value returned must match an entry in the XHB_TERMINAL table.
     */
    public abstract String getTerminalID();

    
    private static TerminalIDFactory createTerminalIDFactory() {
        TerminalIDFactory terminalIDFactory = (TerminalIDFactory) CSServices
                .getDiscoveryServices().createInstance(TerminalIDFactory.class);
        if (terminalIDFactory != null) {
            return terminalIDFactory;
        }
        throw new TerminalIDException(
                "Could not discover a concrete implementation of TerminalIDFactory.");
    }
}
