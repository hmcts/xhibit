package uk.gov.courtservice.xhibit.client.actions.common;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitAboutDialog;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Show the about window
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AboutAction.java,v 1.10 2006/06/05 12:30:52 bzjrnl Exp $
 */

public class AboutAction extends XAction {

    private static AboutAction ca = null;

    private AboutAction() {
        populateFromBundle("About");
        setMnemonicKeyFromBundle("About");
    }

    public static AboutAction getInstance() {
        if (ca == null)
            ca = new AboutAction();
        return ca;
    }

    public static AboutAction getInstance(Object controller) {
        AboutAction aa = getInstance();
        aa.setController(controller);
        return aa;
    }

    public void xActionPerformed(ActionEvent e) {
        XhibitAboutDialog xd = new XhibitAboutDialog(getParentFrame(e), getShortDescription(), true);
    }

    /**
     * Attempt to get the parent frame from the action event, if can't be found,
     * then find the frame from the getController. If still can't be found, then
     * null is acceptable.
     * 
     * @param e
     *            The action event
     * @return The parent frame
     */
    private Frame getParentFrame(ActionEvent e) {
        Frame w = null;
        if (e.getSource() instanceof Component) {
            final Window windowAncestor = XSwingUtilities.getWindowAncestor((Component) e.getSource());
            if (windowAncestor instanceof Frame) {
                w = (Frame) windowAncestor;
            }
        }
        // If we have not been able to establish an ancestor
        // then use the frame from getController.
        if (w == null) {
            if (getController() instanceof Frame) {
                w = (Frame) getController();
            }
        }
        return w;
    }
}
