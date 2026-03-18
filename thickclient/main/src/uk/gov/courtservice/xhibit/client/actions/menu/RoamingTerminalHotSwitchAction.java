package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.Component;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:
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
 * @author unascribed
 * @version 1.0
 */

public class RoamingTerminalHotSwitchAction extends XAction {

    private static RoamingTerminalHotSwitchAction ca = null;

    private RoamingTerminalHotSwitchAction() {
        populateFromBundle("RoamingHotSwitch");
        setMnemonicKeyFromBundle("RoamingHotSwitch");
    }

    public static RoamingTerminalHotSwitchAction getInstance() {
        if (ca == null)
            ca = new RoamingTerminalHotSwitchAction();
        return ca;
    }

    public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
        java.awt.Frame callingFrame = null;
        if (ae.getSource() instanceof Component) {
            callingFrame = XSwingUtilities.getUltimateFrameAncestor((Component) ae.getSource());
        }
        ((XhibitInterface) getController()).roamingTerminalHotSwitch(callingFrame);
    }

    /**
     * Override this uk.gov.courtservice.xhibit.client.util.XAction
     * 
     * @return
     */
    public boolean hasReadAccess() {
        return XhibitSingleton.getInstance().isTerminalRoaming();
    }

    /**
     * Override this uk.gov.courtservice.xhibit.client.util.XAction
     * 
     * @return
     */
    public boolean hasEditAccess() {
        return (FunctionList.hasAccess(FunctionList.UserCanRoamAllCourts) || FunctionList
                .hasAccess(FunctionList.UserCanRoamWithinCourt));
    }
}