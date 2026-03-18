package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: This will open up a dialog where the defendants can be removed on
 * the selected count
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:Logica
 * </p>
 * 
 * @author Krishna Pokala
 * @version 1.0
 */

public class RemoveDefendantsFromCountDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private ResourceBundle myResources = XHIBITConstant.getResourceBundle(XhibitBundles.RemoveFromCountResources);

    private Vector defendantIDs = null;

    private HashMap defendantOnOffenceComplexValues;

    private RemoveDefendantsFromCountPanel removeDefendantsFromCountPanel;

    public XhibitApplicationController xac;

    public RemoveDefendantsFromCountDialog(XhibitApplicationController xac, Collection defandants)
            throws CSRecoverableException {
        super(xac, "", true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

        if (xac == null || defandants == null) {
            throw new IllegalArgumentException(
                    "RemoveDefendantsFromCountDialog - xac,  defandants and mode parameters must contain values.");
        }

        this.xac = xac;
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel model = chargesController.getModel();

        super.setTitle(XHIBITConstant.getResource(myResources, "removeDefendantsFromCountDialog.title"));

        removeDefendantsFromCountPanel = new RemoveDefendantsFromCountPanel(this, model, buttonPanel, defandants, xac
                .getApplicationCaseModel().getScheduledHearingId());

        addBodyPanel(removeDefendantsFromCountPanel);
        pack();
    }

    public void setDefendantID(Vector defendantIDs) {
        this.defendantIDs = defendantIDs;
    }

    public Vector getDefendantIDs() {
        return defendantIDs;
    }

    public void setDefendantsOnOffenceComplexValues(HashMap defendantOnOffenceComplexValues) {
        this.defendantOnOffenceComplexValues = defendantOnOffenceComplexValues;
    }

    public HashMap getDefendantOnOffenceComplexValues() {
        return this.defendantOnOffenceComplexValues;
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    public void setXac(XhibitApplicationController xac) {
        this.xac = xac;
    }

}
