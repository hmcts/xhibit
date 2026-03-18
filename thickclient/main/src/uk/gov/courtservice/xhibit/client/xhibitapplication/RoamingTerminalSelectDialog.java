package uk.gov.courtservice.xhibit.client.xhibitapplication;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;

public class RoamingTerminalSelectDialog extends XDialog {
    public RoamingTerminalSelectDialog() throws CSRecoverableException {
        super(new uk.gov.courtservice.xhibit.client.util.XFrame(), ResourceBundleHelper.getResource(
                XhibitBundles.XhibitClientDefaultResources, "pleaseSelectCourtRoom"), true);

        // Check if roaming is for all courts
        if (XhibitSingleton.getInstance().getCourtId() == null) {
            /*
             * This section is in place for when we plan to allow Judges to roam
             * the country with XHIBIT To activate the functionality three
             * things need to be done 1. In
             * WlsProviders\resource\role.properties uncomment the line for
             * XHBUserCanRoamAllCourts 2. UPDATE XHB_SECURITY_ROLE SET
             * SYSTEM_ROLE='N' WHERE ROLE_NAME='XHBUserCanRoamAllCourts'; 3.
             * Assign the functionality to roam all courts to the appropriate
             * users. (can be set up in the
             * weblogic-security-role-assignment.xml in midtier userterminal
             * project)
             */
            // Can user can roam all courts
            if (!FunctionList.hasAccess(FunctionList.UserCanRoamAllCourts)) {
                Message userMessage = new Message("security.insufficientaccess", new Object[] { ResourceBundleHelper
                        .getResource(XhibitBundles.XhibitClientDefaultResources, "xhibit.roamingTerminal.allCourts") });
                String logMessage = "User \""
                        + XhibitSingleton.getInstance().getUserSession().getUserName()
                        + "\" attempted to log into teminal set up to roam all court houses \""
                        + XhibitSingleton.getInstance().getUserSession().getSessionProperty(
                                UserTerminalProperties.TERMINAL_NAME)
                        + "\" but does not have permission to use XHIBIT on a roaming terminal for all court houses (XHBUserCanRoamAllCourts).";

                throw new CSUnrecoverableException(userMessage, logMessage);
            }
        } else {
            // Can user roam within a court
            if (!FunctionList.hasAccess(FunctionList.UserCanRoamWithinCourt)) {
                Message userMessage = new Message("security.insufficientaccess", new Object[] { ResourceBundleHelper
                        .getResource(XhibitBundles.XhibitClientDefaultResources, "xhibit.roamingTerminal") });
                String logMessage = "User \""
                        + XhibitSingleton.getInstance().getUserSession().getUserName()
                        + "\" attempted to log into roaming terminal \""
                        + XhibitSingleton.getInstance().getUserSession().getSessionProperty(
                                UserTerminalProperties.TERMINAL_NAME)
                        + "\" but does not have permission to use XHIBIT on a roaming terminal (XHBUserCanRoamWithinCourt).";

                throw new CSUnrecoverableException(userMessage, logMessage);
            }
        }
        addBodyPanel(new RoamingTerminalSelectPanel((OkCancelPanel) buttonPanel));
        pack();
    }
}
