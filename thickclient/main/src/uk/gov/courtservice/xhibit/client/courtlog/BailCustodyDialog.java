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
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class BailCustodyDialog extends XDialog {
    private final BailCustodyModel model;

    private final BailCustodyPanel bodyPanel;

    public BailCustodyDialog(final Frame frame, final BailCustodyModel model) throws CSRecoverableException {
        super(frame, "", true);
        if (model.isInEditMode()) {
            setTitle(ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, "titleBarLabelEdit"));
        } else {
            setTitle(ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, "titleBarLabelAdd"));
        }
        this.model = model;
        this.model.setDefendantDisplayType(CourtLogEventLevelPanel.DEFENDANT_LIST);
        this.model.setPanelText(ResourceBundleHelper.getResource(XhibitBundles.BailCustody, "panelTitleLabel"));
        bodyPanel = new BailCustodyPanel(this, model);
        addBodyPanel(bodyPanel);
        pack();
        setResizable(false);
    }
}
