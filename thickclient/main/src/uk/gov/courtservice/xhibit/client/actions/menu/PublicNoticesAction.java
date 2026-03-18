package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.publicnotice.PublicNoticesDialog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;

/**
 * <p>
 * Title: Action launches Public Notices Screen
 * </p>
 * <p>
 * Description: An instance of the dialog is called which allows access to the
 * Public notices
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version $Revision: 1.3 $
 */
public class PublicNoticesAction extends XAction {
    /**
     * PublicNoticesAction pna
     */
    // private static PublicNoticesAction pna = null;
    /**
     * <init>
     */
    public PublicNoticesAction() {
        populateFromBundle("PublicNotices");
        setIcon(XHIBITConstant.imageRoot + "tactivepublicnotice.gif");
    }

    /**
     * getInstance
     * 
     * @return the returned PublicNoticesAction
     */
    // public static PublicNoticesAction getInstance()
    // {
    // if (pna == null)
    // {
    // pna = new PublicNoticesAction();
    // }
    // return pna;
    // }
    /**
     * xActionPerformed
     * 
     * @param e
     *            parameter for xActionPerformed
     * @throws CSRecoverableException -
     */
    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("PublicNoticesAction - displayScreen(e);");

        Frame f = null;
        if (getController() != null) {
            f = (Frame) getController();
        } else {
            if (e.getSource() instanceof Component) {
                f = XSwingUtilities.getUltimateFrameAncestor((Component) e.getSource());
            }
        }
        PublicNoticesDialog pnd = new PublicNoticesDialog(f);
        pnd.setVisible(true);
    }
}