package uk.gov.courtservice.xhibit.client.actions.admin.crestimport;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.admin.crestimport.CrestImportController;
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
 * @version 1.0
 */
public class CrestImportAction extends XAction {

    private static final String ACTION_NAME = "CrestImport";

    private static CrestImportAction ca = null;

    public CrestImportAction() {
        populateFromBundle(ACTION_NAME);
        setMnemonicKeyFromBundle(ACTION_NAME);
        // setEnabled(true);
    }

    public static CrestImportAction getInstance() {
        if (ca == null)
            ca = new CrestImportAction();
        return ca;
    }

    public static CrestImportAction getInstance(Object controller) {
        CrestImportAction aa = getInstance();
        aa.setController(controller);
        return aa;
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        CrestImportController con = new CrestImportController(xac);
        xac.open(con);
    }

}