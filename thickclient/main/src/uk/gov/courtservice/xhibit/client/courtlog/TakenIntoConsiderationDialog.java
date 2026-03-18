package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Frame;
import java.awt.event.WindowEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
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
public class TakenIntoConsiderationDialog extends XDialog {
    private final TakenIntoConsiderationPanel bodyPanel;

    public TakenIntoConsiderationDialog(Frame frame, TakenIntoConsiderationModel model) throws CSRecoverableException {
        super(frame, "", true);
        if (model.isInEditMode()) {
            setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelEdit"));
        } else {
            setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelAdd"));
        }

        model.setPanelText(XHIBITConstant.getResource(XHIBITConstant
                .getResourceBundle(XhibitBundles.TakenIntoConsideration), "panelTitle"));
        this.bodyPanel = new TakenIntoConsiderationPanel(this, model);
        addBodyPanel(bodyPanel);
        pack();
        setResizable(false);
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_OPENED) {
            this.bodyPanel.getFirstEnterableComponent().requestFocus();
        }
    }
}
