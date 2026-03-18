package uk.gov.courtservice.xhibit.client.actions.common;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XToolBarHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: View/Hide Toolbars
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class ViewToolbarAction extends XAction {
    private static ViewToolbarAction ca = null;

    private ViewToolbarAction() {
    }

    public synchronized static ViewToolbarAction getInstance() {
        if (ca == null) {
            ca = new ViewToolbarAction();
        }
        return ca;
    }

    public void xActionPerformed(ActionEvent e) {
        JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) e.getSource();
        XhibitApplicationController xac = (XhibitApplicationController) checkBox.getClientProperty("xhibitController");
        XToolBarHelper toolBarHelper = xac.getToolBarHelper();
        if (toolBarHelper == null) {
            // Handle exception here.
            return;
        }
        String toolbarId = (String) checkBox.getClientProperty("toolBarId");
        toolBarHelper.activateToolbar(toolbarId, checkBox.isSelected());
        toolBarHelper.refreshToolBarState(toolbarId);
        xac.addGenericToolBars();
    }
}