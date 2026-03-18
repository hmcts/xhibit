package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.ResourceBundle;

import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

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
 * @author Stephen Tully
 * @version 1.0
 */
public class AddEditWitnessDialog extends XDialog {

    private ResourceBundle taskResources = XHIBITConstant.getResourceBundle(XhibitBundles.WitnessSworn);

    public AddEditWitnessDialog(java.awt.Frame frame, AddEditWitnessModel model) {
        super(frame, "", true);

        setTitle(XHIBITConstant.getResource(taskResources, "titleBarLabelAdd"));

        addBodyPanel(new AddEditWitnessPanel(buttonPanel, model));

        pack();

        setResizable(false);
    }
}