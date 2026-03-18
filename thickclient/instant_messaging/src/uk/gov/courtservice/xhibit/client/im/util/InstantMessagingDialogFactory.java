package uk.gov.courtservice.xhibit.client.im.util;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.screens.IMSenderDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: InstantMessagingDialogFactory
 * </p>
 * <p>
 * Description: Returns an instance of InstantMessagingDialogFactory that is
 * used to return an instance of IMSenderDialog.
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

public class InstantMessagingDialogFactory {

    private static final Logger log = CSServices.getLogger(InstantMessagingDialogFactory.class);

    private static IMSenderDialog imDialog;

    private static InstantMessagingDialogFactory imDlgFactory;

    static {
        imDlgFactory = new InstantMessagingDialogFactory();
    }

    /**
     * Returns an instance of InstantMessagingDialogFactory.
     * 
     * @return InstantMessagingDialogFactory
     */
    public static InstantMessagingDialogFactory getInstance() {
        log.debug("MESSAGING***: Returning InstantMessagingDialogFactory");
        return imDlgFactory;
    }

    /**
     * Returns an instance of IMSenderDialog.
     * 
     * @param XhibitApplicationController
     * @return IMSenderDialog
     * @throws CSRecoverableException
     */
    public IMSenderDialog getIMSenderDialog(XhibitApplicationController xac) throws CSRecoverableException {
        log.debug("MESSAGING***: Returning IMSenderDialog");
        try {
            if (imDialog == null) {
                imDialog = new IMSenderDialog(xac);
            }
            imDialog.reset();
            imDialog.setVisible(true);
            imDialog.requestFocus();
        } catch (CSRecoverableException ex) {
            log.debug("<<<<>>>>> InstantMessagingDialogFactory exception " + ex.getMessage() + "<<<<>>>>");
            imDialog.dispose();
            imDialog = null;
            throw ex;
        }
        return imDialog;
    }

}