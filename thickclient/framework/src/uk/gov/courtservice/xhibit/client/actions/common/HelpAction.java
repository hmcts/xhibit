package uk.gov.courtservice.xhibit.client.actions.common;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.HTMLHelper;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: Display Help
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

public class HelpAction extends XAction {

    private static HelpAction ca = null;

    private HelpAction() {
        populateFromBundle("Help");
        setAccelaratorKey(KeyStroke.getKeyStroke("F1"));
    }

    public static HelpAction getInstance() {
        if (ca == null)
            ca = new HelpAction();
        return ca;
    }

    public void xActionPerformed(ActionEvent e) {

        // String pubDispServer =
        // CSServices.getConfigServices().getProperty("helpweb.PROVIDER_URL");
        // String helpWebAddress =
        // CSServices.getConfigServices().getProperty("helpWebsite");

        // --------------------------------------
        // CO - 53165 : Override helpWebAddress from command line in order to
        // enable easy change of properties
        String helpWebAddress;
        if ((System.getProperty("helpWebsite") != null) && System.getProperty("helpWebsite").length() > 0) {
            helpWebAddress = System.getProperty("helpWebsite");
        } else {
            helpWebAddress = CSServices.getConfigServices().getProperty("helpWebsite");
        }
        // --------------------------------------

        if (helpWebAddress == null) {
            XHIBITConstant.error("Building help web address did not find property helpWebsite from CSServices.");
            throw new CSConfigurationException("Exception during ....");
        }
        String link = helpWebAddress;
        // String path = new java.io.File(".").getAbsolutePath();
        // String link = "file://" + path + "/Xhibit_User_Guide/index.htm";
        String command = HTMLHelper.getWindowsURLLauncher(link);
        Process helpProcess = HTMLHelper.runCommand(command);
    }
}