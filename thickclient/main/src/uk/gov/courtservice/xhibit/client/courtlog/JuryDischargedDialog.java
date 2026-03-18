package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Frame;

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
 * @author David Crossland
 * @version 1.0
 */

public class JuryDischargedDialog extends XDialog {

    private JuryDischargedModel model;

    private JuryDischargedPanel bodyPanel;

    public JuryDischargedDialog(Frame frame, JuryDischargedModel model) throws CSRecoverableException {
        super(frame, "", true);
        if (model.isInEditMode()) {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelEdit"));
        } else {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelAdd"));
        }
        this.model = model;
        this.model.setPanelText(XHIBITConstant.getResource(XHIBITConstant
                .getResourceBundle(XhibitBundles.JuryDischarged), "panelTitle"));
        this.bodyPanel = new JuryDischargedPanel(this, model);
        super.addBodyPanel(bodyPanel);
        super.pack();
        super.setResizable(false);
    }
}
