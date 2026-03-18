package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.event.ActionEvent;

/**
 * <p>
 * Title: Print Toolbar action that prints without showing dialog
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
 * @version $Id: PrintToolbarAction.java,v 1.5 2006/06/05 12:31:01 bzjrnl Exp $
 */

public class PrintToolbarAction extends PrintAction {
    public PrintToolbarAction() {
        populateFromBundle("Print");
    }

    // public void xActionPerformed(ActionEvent e) throws
    // java.lang.Exception
    // {
    // print(false, true);
    // // print(false, false);
    // }
    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        print(false, true);
        // print(false, false);
    }
}