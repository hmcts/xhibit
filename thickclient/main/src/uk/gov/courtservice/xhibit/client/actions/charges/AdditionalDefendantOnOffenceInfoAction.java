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

/**
 * Action to Add additional defendant on offence related infor mation to an
 * existing defendant on offence. Can also used to editing existing additional
 * defendant on offence information.
 * 
 * @author szfnvt
 * 
 */
public class AdditionalDefendantOnOffenceInfoAction extends XAction {

    /**
     * No arg constructor
     */
    public AdditionalDefendantOnOffenceInfoAction() {
        populateFromBundle("AdditionalDefendantOnOffenceInfoAction");
    }

    /**
     * xActionPerformed - body of action processing
     */
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
         * getString("addDefendantToOffence.title"),
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
