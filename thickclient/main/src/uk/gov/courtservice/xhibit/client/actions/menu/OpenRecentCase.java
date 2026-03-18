package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.Component;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.models.RecentCase;
import uk.gov.courtservice.xhibit.client.schedule.othercase.OpenCaseHelper;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Open a recent case into the XAC
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
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class OpenRecentCase extends XAction {
    public OpenRecentCase(RecentCase model) {
        setModel(model);
        setName(model.getCaseNumber());
        setShortDescription(model.getCaseNumber());
        setLongDescription("Open case " + model.getCaseNumber());
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = null;
        if (e.getSource() instanceof Component) {
            java.awt.Frame frame = XSwingUtilities.getUltimateFrameAncestor((Component) e.getSource());
            if (frame instanceof XhibitApplicationController) {
                xac = (XhibitApplicationController) frame;
                RecentCase _model = (RecentCase) getModel();
                OpenCaseHelper.OpenCase(xac, _model.getCaseNumber(), _model.isReadOnly());
            } else {
                throw new CSRecoverableException("gui.recentcase.xacfailure", new Object[] {},
                        "Could not get handle on XAC to open case");
            }
        } else {
            throw new CSRecoverableException("gui.recentcase.xacfailure", new Object[] {},
                    "event source not a component");
        }
    }
}