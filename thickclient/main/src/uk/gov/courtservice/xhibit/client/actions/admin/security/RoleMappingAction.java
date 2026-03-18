package uk.gov.courtservice.xhibit.client.actions.admin.security;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.admin.security.rolemapping.RoleMappingController;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * @version $Revision: 1.9 $
 */
public class RoleMappingAction extends XAction {
    private static final String ACTION_NAME = "RoleMapping";

    private static RoleMappingAction ca = null;

    public RoleMappingAction() {
        populateFromBundle(ACTION_NAME);

        // A known bug in jdk 1.3.1 means that not all 3rd level menu item
        // mnemonics are recognised.
        // The decision was made to remove all of these mnemonics. Neil
        // Entwistle 06/06/2003
        // setMnemonicKeyFromBundle(ACTION_NAME);
        // setEnabled(true);
    }

    public static RoleMappingAction getInstance() {
        if (ca == null) {
            ca = new RoleMappingAction();
        }

        return ca;
    }

    public static RoleMappingAction getInstance(Object controller) {
        RoleMappingAction aa = getInstance();
        aa.setController(controller);
        return aa;
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        xac.open(new RoleMappingController());
    }
}
