package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
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

public class JurySwornInDialog extends XDialog {

    private JurySwornInModel model;

    private JurySwornInPanel bodyPanel;

    private ResourceBundle taskResources = XHIBITConstant.getResourceBundle(XhibitBundles.JurySwornIn);

    public JurySwornInDialog(java.awt.Frame frame, JurySwornInModel model) throws CSRecoverableException {
        super(frame, "", true);
        if (model.isInEditMode()) {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelEdit"));
        } else {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelAdd"));
        }
        this.model = model;
        this.model.setPanelText(XHIBITConstant.getResource(taskResources, "panelTitle"));
        this.bodyPanel = new JurySwornInPanel(this, model);
        super.addBodyPanel(bodyPanel);
        super.pack();
        super.setResizable(false);
    }
}
