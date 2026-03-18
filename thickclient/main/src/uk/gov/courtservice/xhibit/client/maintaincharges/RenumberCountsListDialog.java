package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RenumberCountsListDialog
 * </p>
 * <p>
 * Description: This dialog will allow the counts to be renumbered
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

public class RenumberCountsListDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private ResourceBundle myResources = 
        XHIBITConstant.getResourceBundle(XhibitBundles.RenumberCountsResources);

    private RenumberCountsListPanel renumberCountsListPanel;

    public XhibitApplicationController xac;
    

    public RenumberCountsListDialog(XhibitApplicationController xac) throws CSRecoverableException {
        super(xac, "Renumber Counts List", true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

        if (xac == null) {
            throw new IllegalArgumentException("RenumberCountsListDialog - xac must contain values.");
        }

        this.xac = xac;
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel model = chargesController.getModel();

        super.setTitle(XHIBITConstant.getResource(myResources, "renumberCountsDialog.title"));

        renumberCountsListPanel = new RenumberCountsListPanel(model, this);
        addBodyPanel(renumberCountsListPanel);
        pack();
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    public void setXac(XhibitApplicationController xac) {
        this.xac = xac;
    }
}
