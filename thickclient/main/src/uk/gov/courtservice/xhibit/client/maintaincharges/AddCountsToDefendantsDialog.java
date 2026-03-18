package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ResourceBundle;

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
 * Description:
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
/*
 * Ref Date Author Description
 * 
 * 06-08-2003 AW Daley CountsSelectorPanel replaced with
 * DefendantsCountsTablePanel. Now handles input of CRN when adding a defendant
 * to a count.
 */

public class AddCountsToDefendantsDialog extends XDialog {
    private ResourceBundle myResources = XHIBITConstant.getResourceBundle(XhibitBundles.AddCountsDefendantsResources);

    private AddCountsToDefendantsPanel addCountsToDefendantsPanel;

    // public AddCountsToDefendantsDialog() { //Temp method for testing
    // super("", true);
    // super.setTitle(XHIBITConstant.getResource(myResources,
    // "addCountsToDefendant"));
    // AddCountsToDefendantsPanel addCountsToDefendantsPanel= new
    // AddCountsToDefendantsPanel(buttonPanel);
    // addBodyPanel(addCountsToDefendantsPanel);
    // pack();
    // }

    public AddCountsToDefendantsDialog(XhibitApplicationController xac) throws CSRecoverableException {
        super(xac, "", true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel model = chargesController.getModel();
        super.setTitle(XHIBITConstant.getResource(myResources, "addCountsToDefendant"));
        addCountsToDefendantsPanel = new AddCountsToDefendantsPanel(model, buttonPanel);
        addBodyPanel(addCountsToDefendantsPanel);
        pack();
    }

}