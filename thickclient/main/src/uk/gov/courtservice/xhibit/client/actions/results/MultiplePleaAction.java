package uk.gov.courtservice.xhibit.client.actions.results;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.PleasAndDirectionsController;
import uk.gov.courtservice.xhibit.client.results.pleas.MultiplePleaDialog;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaController;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaControllerModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: MultiplePleaAction
 * </p>
 * <p>
 * Description: action for multiple pleas
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Paul Morris
 * @version 1.0
 */

public class MultiplePleaAction extends XAction {
    private XhibitApplicationController xac = null;

    private PleaController pleaController = null;

    private PleasAndDirectionsController pleaAndDirController = null;

    private PleaControllerModel model = null;

    public MultiplePleaAction() {
        populateFromBundle("MultiplePlea");
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        xac = (XhibitApplicationController) getController();
        /* @todo code in multiple pleas call */
        // MultiplePleasController mpc = new MultiplePleasController(
        // xac.getApplicationCaseModel() );
        // xac.open(mpc);
        // pleaController = (PleaController) xac.getBodyPanel();
        Object screenController = xac.getBodyPanel();
        if (screenController instanceof PleaController) {
            pleaController = (PleaController) screenController;
        } else if (screenController instanceof PleasAndDirectionsController) {
            pleaAndDirController = (PleasAndDirectionsController) screenController;
            pleaController = pleaAndDirController.getPleaController();
        } else {
            throw new UnsupportedOperationException("Unknown class " + screenController);
        }

        model = pleaController.getPleaControllerModel();

        MultiplePleaDialog dialog = new MultiplePleaDialog(xac);
        dialog.setVisible(true);

        if (dialog.isOkClicked()) {
            uk.gov.courtservice.xhibit.client.util.XHIBITConstant
                    .debug("[MultiplePleaAction] MultiplePleaDialog - OK clicked");
            pleaController.updateIndictmentTable();
        }

    }
}