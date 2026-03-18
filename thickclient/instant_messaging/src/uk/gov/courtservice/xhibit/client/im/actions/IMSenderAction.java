package uk.gov.courtservice.xhibit.client.im.actions;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.screens.IMSenderDialog;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessagingDialogFactory;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessagingReceiverFactory;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: IMSenderAction
 * </p>
 * <p>
 * Description: Creates an instance of the Messaging sender dialog
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

public class IMSenderAction extends XAction {

    public static final String IM_CONTEXT_ROOT = "im.context.root";

    private Logger log = CSServices.getLogger(IMSenderAction.class);

    private IMSenderDialog imSender;

    /**
     * Constructor
     */
    public IMSenderAction() {
        populateFromBundle("IMSender");
    }

    /**
     * Create an instance of the sender dialog and also reset the receiver -
     * just in case there have been any undetected errors earlier on. This is a
     * bit over the top, but testing revealed the InstantMessageServices did not
     * detect all exceptions
     * 
     * @param parm1
     * @throws java.lang.Exception
     * @throws CSRecoverableException
     */
    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception, CSRecoverableException {
        log.debug("IMSenderAction***: xActionPerformed");
        XhibitApplicationController xac = (XhibitApplicationController) getController();

        try {
            // Display the IM Send dialog
            InstantMessagingDialogFactory.getInstance().getIMSenderDialog(xac);

            // Reset the receiver
            InstantMessagingReceiverFactory.getInstance().getIMReceiver().reset();
        } catch (Exception ex) {
            log.debug("<<<<>>>>> IMSENDERHELPER exception " + ex.getMessage() + "<<<<>>>>");
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
        }
    }
}