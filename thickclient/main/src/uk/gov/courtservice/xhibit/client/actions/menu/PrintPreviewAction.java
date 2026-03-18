package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.event.ActionEvent;

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

public class PrintPreviewAction extends PrintAction {

    public PrintPreviewAction() {
        populateFromBundle("PrintPreview");
    }

    // public void xActionPerformed(ActionEvent e) throws
    // java.lang.Exception
    // {
    // print(true, true);
    // }
    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        print(true, true);
    }
}