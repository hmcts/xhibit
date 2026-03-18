package uk.gov.courtservice.xhibit.client.listdistribution;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllRecipientComplexValue;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: SetMaintainListLetterRecipientsDialog
 * </p>
 * <p>
 * Description: Screen for setting the distribution options for list letter
 * recipients.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * <p>
 * Author: Will Fardell, Xdevelopment (2004)
 * </p>
 * 
 * @version $Id: MaintainListLetterRecipientsSetDialog.java,v 1.2 2005/02/22
 *          09:25:34 bzjrnl Exp $
 */
public class MaintainListLetterRecipientsSetDialog extends XDialog {
    private final MaintainListLetterRecipientsEditPanel bodyPanel;

    public MaintainListLetterRecipientsSetDialog(XhibitApplicationController xac, WllRecipientComplexValue value)
            throws CSRecoverableException {
        super(xac, getResource("maintainlistletterrecipientssetdialog.title"), true, OKCANCEL, DEFAULTOK);

        if (value == null) {
            throw new IllegalArgumentException("value: " + null);
        }

        bodyPanel = new MaintainListLetterRecipientsEditPanel(xac, value);

        addBodyPanel(bodyPanel);
        setSize(400, 300);
        centreDialog();
    }

    // Query

    public WllRecipientComplexValue getValue() {
        return bodyPanel.getValue();
    }

    // Environment Utilities

    /**
     * Get the specifed resource from the list distribution resources
     */
    private static String getResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.ListDistribution, key);
    }
}
