package uk.gov.courtservice.xhibit.client.actions.common;

import java.awt.Window;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.util.XAction;

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

public class DisposeWindowAction extends XAction {

    public DisposeWindowAction(Object controller) {
        setController(controller);
        populateFromBundle("btnOk");
        setMnemonicKeyFromBundle("btnOk");
    }

    public void xActionPerformed(ActionEvent e) {
        ((Window) getController()).dispose();
    }
}