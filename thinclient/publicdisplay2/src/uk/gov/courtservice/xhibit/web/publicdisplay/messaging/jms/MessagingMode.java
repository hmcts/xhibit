/*
 * Created on 09-Jan-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms;

/**
 * @author pznwc5
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class MessagingMode {

    /** P2P messaging mode */
    public static final MessagingMode P2P = new MessagingMode();

    /** Publish/subscribe messaging mode */
    public static final MessagingMode PUB_SUB = new MessagingMode();

    /**
     * Private constructor
     * 
     */
    private MessagingMode() {
    }

}
