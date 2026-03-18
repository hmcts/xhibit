package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.RemoveDefendantsFromCountDialog;
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
 * Description: Remove Defendants from the count.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Krishna Pokala
 * @version 1.0
 */

public class RemoveDefendantsFromCountAction extends XAction {
    private static final long serialVersionUID = 1L;

    public RemoveDefendantsFromCountAction() {
        populateFromBundle("RemoveDefendantsOnCount");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac;
        ChargesController chargesController;
        ChargesControllerModel ccm;

        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        ccm = chargesController.getModel();

        Collection onOffence = ChargesControllerHelper.getDefendantsOnOffence(ccm);

        if (onOffence != null && onOffence.size() > 0) {
            RemoveDefendantsFromCountDialog removeDefendantsFromCountDialog = new RemoveDefendantsFromCountDialog(xac,
                    onOffence);

            removeDefendantsFromCountDialog.setVisible(true);
            if (removeDefendantsFromCountDialog.isCancelClicked())
                throw new UserCancelException();

            chargesController.loadCharges();

        } else {
            JOptionPane.showMessageDialog(xac, getString("removeDefendantsFromCount.nodefendants.msg"),
                    getString("removeDefendantsFromCount.title"), JOptionPane.INFORMATION_MESSAGE);
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
        return ResourceBundleHelper.getResource(XhibitBundles.RemoveFromCountResources, key);
    }

}