package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.GridBagConstraints;
import java.util.ResourceBundle;

import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
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
public class FreeTextEventPanel extends CourtLogEventPanel {
    private final ResourceBundle ftResources = XHIBITConstant.getResourceBundle(XhibitBundles.SimpleEvent);

    private final FreeTextModel model;

    private JLabel panelTitleLabel;

    public FreeTextEventPanel(XDialog parent, FreeTextModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        this.add(getPanelTitleLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));

        this.add(getLogAuditPanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    private JLabel getPanelTitleLabel() {
        if (panelTitleLabel == null) {
            final String labelTitle = XHIBITConstant.getResource(ftResources, "freeTextPanelTitle");

            panelTitleLabel = new PanelTitleLabel(labelTitle);
            model.setPanelText(labelTitle);
        }
        return panelTitleLabel;
    }
}
