package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
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

public class AdditionalDefendantOnCountInfoAction extends XAction {

    public AdditionalDefendantOnCountInfoAction() {
        populateFromBundle("AdditionalDefendantOnCountInfoAction");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();

        /*
         * CCN0257 This field is now optional //Check if additional Offence End
         * date present before continuing if
         * (ccm.getOffenceValue().getOffenceEndDateTime() == null){
         * JOptionPane.showMessageDialog(xac,
         * getString("addDefendantOnOffence.noEndDate.msg"),
         * getString("addDefendantToCount.title"),
         * JOptionPane.INFORMATION_MESSAGE); throw new UserCancelException(); }
         */

        // Collection including existing defOnOffence.
        Collection onOffenceCol = new ArrayList();
        onOffenceCol.add(ccm.getDefendantValue());

        AddDefendantsToOffenceDialog addDefendantsToOffenceDialog = new AddDefendantsToOffenceDialog(xac, true,
                AddDefendantsToOffenceDialog.TITLE.DEFENDANT_TO_OFFENCE, onOffenceCol,
                ChargesControllerHelper.MODE.EDIT);

        addDefendantsToOffenceDialog.setVisible(true);

        if (addDefendantsToOffenceDialog.isCancelClicked())
            throw new UserCancelException();

        // Refresh Charges tab
        chargesController.loadCharges();
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
