package uk.gov.courtservice.xhibit.client.im.util;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.messaging.MessagingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: InstantMessagingServicesHelper
 * </p>
 * <p>
 * Description: Handles calls to delegate. Currently only retrieves the
 * destination tree, but can be extendede
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

public class InstantMessagingServicesHelper {

    private static final Logger log = CSServices.getLogger(InstantMessagingServicesHelper.class);

    /**
     * Private constructor to prevent instantiation
     */
    private InstantMessagingServicesHelper() {
        // private constructor
    }

    /**
     * Get the JMS Destination tree model for the terminal id
     * 
     * @param startName
     *            the JMS start context
     * @param info
     *            the sceurit access info
     * @param terminalId
     *            the terminal id of the user
     * @param ims
     *            the InsatntMessagingServices instance
     * @return the populated tree model
     * @throws CSRecoverableException
     *             if any of the tree elements are not found
     */
    public static DefaultTreeModel getTreeModel(String startName, Integer courtId, InstantMessageServices ims)
            throws CSRecoverableException {
        MessagingControllerBeanBusinessDelegate delegate = XhibitDelegateHelper.getMessagingDelegate();

        try {
            // Get the tree, as retrieved from the database, convert it to a
            // more strongly typed tree and then sort it
            return ims.getSortedTreeModel(startName, ims.convertToTopicTree(delegate.getTreeModelByCourtId(courtId)));
        } catch (NamingException ex) {
            throw new CSRecoverableException("IMMessageReceiver.reset", "IMMessageReceiver.reset", ex);
        } catch (JMSException ex) {
            throw new CSRecoverableException("IMMessageReceiver.reset", "IMMessageReceiver.reset", ex);
        }
    }

}