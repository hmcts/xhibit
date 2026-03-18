package uk.gov.courtservice.xhibit.rolemapping.services;

import java.util.Hashtable;

import javax.jms.ObjectMessage;

import uk.gov.courtservice.framework.business.services.CSMessageBeanNotifier;

/**
 * Custom notifier for the role mapping refresh message driven beans. This class
 * encapsulates all of the code necessary to send the message.
 * 
 * @author tz0d5m
 * @version $Id: RoleMappingNotifier.java,v 1.7 2007/01/10 13:54:45 rzvddy Exp $
 */
public class RoleMappingNotifier extends CSMessageBeanNotifier {
    /** Default connection factory */
    protected static final String CONNECTION_FACTORY = "PublicDisplayConnectionFactory";

    /** Default destination */
    protected static final String DESTINATION = "RoleMapper";

    /**
     * Default constructor
     */
    public RoleMappingNotifier() {
        this(null);
    }

    /**
     * Constructor that initializes the context with the environment passed in.
     * 
     * @param environment
     *            The environment for the <code>InitialContext</code>.
     */
    public RoleMappingNotifier(Hashtable environment) {
        super(environment, CONNECTION_FACTORY, DESTINATION);
    }

    /**
     * Send a message to the role mapping queue.
     * 
     * @see uk.gov.courtservice.framework.business.services.CSMessageBeanNotifier
     *      #sendMessage(java.util.Serializable)
     */
    public final void sendMessage(String val) {
        // we only care that an event happened, not any details...
        log.info("sendMessage - BEGIN");
        super.sendMessage(val);
        log.info("sendMessage - END");
    }

    /**
     * Implementation of the required setMessageHeader method. This
     * implemenation does not require any headers to be set.
     * 
     * @param message
     *            The message on which to set headers.
     * 
     * @see uk.gov.courtservice.framework.business.services.CSMessageBeanNotifier
     *      #setMessageHeader(javax.jms.ObjectMessage)
     */
    protected void setMessageHeader(ObjectMessage message) {
        // no implementation required...
    }
}
