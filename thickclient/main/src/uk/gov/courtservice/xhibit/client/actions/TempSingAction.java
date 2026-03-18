package uk.gov.courtservice.xhibit.client.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Used to copy text onto
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

public class TempSingAction extends XAction {

    private static TempSingAction ca = null;

    private TempSingAction() {
        setName("TempAction");
        setShortDescription("Temp");
        setLongDescription("This is an action that can be copied for temporary actions");
        setIcon(XHIBITConstant.imageRoot + "x.gif");
        setAccelaratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        setMnemonicKey(KeyEvent.VK_C);
    }

    public static TempSingAction getInstance() {
        if (ca == null)
            ca = new TempSingAction();
        return ca;
    }

    public static TempSingAction getInstance(Object controller) {
        TempSingAction aa = getInstance();
        aa.setController(controller);
        return aa;
    }

    public void xActionPerformed(ActionEvent e) {
        uk.gov.courtservice.xhibit.client.util.XMessageBox.alert("This action has not been implemented yet");
    }
}