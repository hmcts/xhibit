package uk.gov.courtservice.xhibit.client.im.util;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: InstantMessagingReceiverFactory
 * </p>
 * <p>
 * Description: Returns an instance of InstantMessagingReceiverFactory that is
 * used to return an instance of IMMessageReceiver.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class InstantMessagingReceiverFactory {

    private static final Logger log = CSServices.getLogger(InstantMessagingReceiverFactory.class);

    private static IMMessageReceiver receiver;

    private static InstantMessagingReceiverFactory imRecFactory;

    static {
        imRecFactory = new InstantMessagingReceiverFactory();
    }

    private void InstantMessageReceiverFactory() {
        // private constructor
    }

    /**
     * Returns an instance of InstantMessagingReceiverFactory.
     * 
     * @return InstantMessagingReceiverFactory
     */
    public static InstantMessagingReceiverFactory getInstance() {
        log.debug("MESSAGING***: Returning InstantMessagingReceiverFactory");
        return imRecFactory;
    }

    /**
     * Returns an instance of IMMessageReceiver
     * 
     * @return IMMessageReceiver
     * @throws CSRecoverableException
     */
    public IMMessageReceiver getIMReceiver() {
        log.debug("MESSAGING***: Returning IMMessageReceiver");
        try {
            if (receiver == null) {
                receiver = new IMMessageReceiver();
            }
        } catch (CSRecoverableException ex) {
            log.debug("<<<<>>>>> InstantMessagingReceiverFactory exception " + ex.getMessage() + "<<<<>>>>");
            CSServices.getDefaultErrorHandler().handleError(ex, InstantMessagingReceiverFactory.class);
        }
        return receiver;
    }

}