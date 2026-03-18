package uk.gov.courtservice.xhibit.client.courtlog;

/**
 * <p>Title: Xhibit2</p>
 * <p>Description: Court Services Application</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author David Crossland
 * @version 1.0
 */

import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class LegalArgumentOptionsDialog extends XDialog {

    private LegalArgumentOptionsModel model;

    private LegalArgumentOptionsPanel bodyPanel;

    private ResourceBundle taskResources = XHIBITConstant.getResourceBundle(XhibitBundles.LegalArgumentOptions);

    public LegalArgumentOptionsDialog(java.awt.Frame frame, LegalArgumentOptionsModel model)
            throws CSRecoverableException {
        super(frame, "", true);
        if (model.isInEditMode()) {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelEdit"));
        } else {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelAdd"));
        }
        this.model = model;
        this.model.setPanelText(XHIBITConstant.getResource(taskResources, "legal_argument_options"));
        this.bodyPanel = new LegalArgumentOptionsPanel(this, model);
        super.addBodyPanel(bodyPanel);
        pack();
        super.setResizable(false);
    }

}
