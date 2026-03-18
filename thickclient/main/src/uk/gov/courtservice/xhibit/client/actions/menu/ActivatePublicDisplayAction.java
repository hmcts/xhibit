package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.AbstractButton;

import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Action used to notify server when public display are turned on or off
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
public class ActivatePublicDisplayAction extends XAction {
    public ActivatePublicDisplayAction() {
        populateFromBundle("ActivatePublicDisplay");
        // default to inactive
        setIcon(XHIBITConstant.imageRoot + "tdeactivatepublicdisplay.gif");
        setName(getBundle().getString("ActivatePublicDisplayInactive"));
    }

    public void xActionPerformed(ActionEvent e) throws Exception {
        if (e.getSource() instanceof AbstractButton) {
            boolean state = ((AbstractButton) (e.getSource())).isSelected();
            /**
             * @todo Implement this
             *       uk.gov.courtservice.xhibit.client.util.XAction abstract
             *       method
             */
            // BD call
            XhibitApplicationController xac = (XhibitApplicationController) getController();
            Integer shId = xac.getApplicationCaseModel().getScheduledHearingId();

            if (state) {
                // pass the client system date/time...
                XhibitDelegateHelper.getDisplayDelegate().activatePublicDisplay(shId, new Date(),
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            } else {
                // pass the client system date/time...
                XhibitDelegateHelper.getDisplayDelegate().deActivatePublicDisplay(shId, new Date(),
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            }

            // Update XAC screen active
            xac.setScreenActive(state);

            if (state) {
                XhibitSingleton.getInstance().setUserDeactivedScreen(false);
                setIcon(XHIBITConstant.imageRoot + "tactivatepublicdisplay.gif");
                setName(getBundle().getString("ActivatePublicDisplayActive"));
            } else {
                XhibitSingleton.getInstance().setUserDeactivedScreen(true);
                setIcon(XHIBITConstant.imageRoot + "tdeactivatepublicdisplay.gif");
                setName(getBundle().getString("ActivatePublicDisplayInactive"));
            }
        }
    }
}