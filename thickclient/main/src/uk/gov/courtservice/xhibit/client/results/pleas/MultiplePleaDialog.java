package uk.gov.courtservice.xhibit.client.results.pleas;

import java.util.ResourceBundle;

import uk.gov.courtservice.xhibit.client.results.PleasAndDirectionsController;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class MultiplePleaDialog extends XDialog {
    private ResourceBundle resources;

    public MultiplePleaDialog(XhibitApplicationController xac) {
        super(xac, "", true);
        resources = XHIBITConstant.getResourceBundle(XhibitBundles.Pleas);
        super.setTitle(XHIBITConstant.getResource(resources, "multiple.title"));

        // PleaController pleaController = (PleaController) xac.getBodyPanel();
        PleasAndDirectionsController pleaAndDirController;
        PleaController pleaController;
        Object screenController = xac.getBodyPanel();
        if (screenController instanceof PleaController) {
            pleaController = (PleaController) screenController;
        } else if (screenController instanceof PleasAndDirectionsController) {
            pleaAndDirController = (PleasAndDirectionsController) screenController;
            pleaController = pleaAndDirController.getPleaController();
        } else {
            throw new UnsupportedOperationException("Unknown class " + screenController);
        }

        PleaControllerModel model = pleaController.getPleaControllerModel();

        MultiplePleasPanel multiplePleasPanel = new MultiplePleasPanel(model);
        addBodyPanel(multiplePleasPanel);
        this.setResizable(false);
        pack();
    }
}