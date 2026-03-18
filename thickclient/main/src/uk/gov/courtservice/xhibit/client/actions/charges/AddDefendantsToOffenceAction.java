package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddDefendantsToOffenceDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Add defendant to an existing offence.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class AddDefendantsToOffenceAction extends XAction {

    public AddDefendantsToOffenceAction() {
        populateFromBundle("AddDefendantsToOffence");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {

        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();

        /*
         * CCN0257 This field is now optional. //Check if additional Offence End
         * date present before continuing if
         * (ccm.getOffenceValue().getOffenceEndDateTime() == null) {
         * JOptionPane.showMessageDialog(xac,
         * getString("addDefendantOnOffence.noEndDate.msg"),
         * getString("addDefendantsToOffence.title"),
         * JOptionPane.INFORMATION_MESSAGE); throw new UserCancelException(); }
         */

        Collection notOnOffence = ChargesControllerHelper.getDefendantsNotOnOffence(ccm);
        if (notOnOffence.size() > 0) {
            AddDefendantsToOffenceDialog addDefendantsToOffenceDialog = new AddDefendantsToOffenceDialog(xac, true,
                    AddDefendantsToOffenceDialog.TITLE.DEFENDANT_TO_OFFENCE, notOnOffence,
                    ChargesControllerHelper.MODE.ADD);
            addDefendantsToOffenceDialog.setVisible(true);

            if (addDefendantsToOffenceDialog.isCancelClicked())
                throw new UserCancelException();

            chargesController.loadCharges();
        } else {
            JOptionPane.showMessageDialog(xac, getString("addDefendantsToOffence.noDefendants.msg"),
                    getString("addDefendantsToOffence.title"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Get a resource string from the Additional resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource from the given key.
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources, key);
    }
}