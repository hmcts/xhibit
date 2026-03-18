package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Logica
 * </p>
 */

public class SpecialMeasuresApplicationDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private final SpecialMeasuresApplicationPanel bodyPanel;


    public SpecialMeasuresApplicationDialog(
            final Frame frame, 
            final SpecialMeasuresApplicationModel model) 
    throws CSRecoverableException {
        super(frame, "", true);
        if (model.isInEditMode()) {
            setTitle(ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, "titleBarLabelEdit"));
        } else {
            setTitle(ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, "titleBarLabelAdd"));
        }
        model.setPanelText(ResourceBundleHelper.getResource(XhibitBundles.SpecialMeasures, "panelTitleLabel"));
        bodyPanel = new SpecialMeasuresApplicationPanel(this, model);
        addBodyPanel(bodyPanel);
        pack();
        setResizable(false);
    }
}
