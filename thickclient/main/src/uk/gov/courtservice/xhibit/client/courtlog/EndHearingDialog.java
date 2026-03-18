package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class EndHearingDialog extends XDialog {
    private EndHearingModel model;

    private EndHearingPanel bodyPanel;

    /**
     * Public constructor. Sets an indicator to request that the defendants be
     * displayed in a list and then instantiates an EndHearingPanel to display
     * the end hearing court log event.
     * 
     * @param frame -
     *            the parent frame: in this case the XhibitApplicationController
     * @param model -
     *            the model for the EndHearingEvent: this is a type of
     *            FreeTextModel
     * @throws CSRecoverableException
     */
    public EndHearingDialog(Frame frame, EndHearingModel model) throws CSRecoverableException {
        super(frame, "", true);

        if (model.isInEditMode()) {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelEdit"));
        } else {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelAdd"));
        }

        this.model = model;
        this.model.setDefendantDisplayType(CourtLogEventLevelPanel.DEFENDANT_LIST);
        this.bodyPanel = new EndHearingPanel(this, model);
        super.addBodyPanel(bodyPanel);
        super.pack();
        super.setResizable(false);
    }
}
