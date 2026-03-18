package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JPopupMenu;

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

public class XSwingUtilities {

    /**
     * This is an extension of SwingUtilities getWindowAncestor It has been
     * written because SwingUtilities does not handle JPopup's
     * 
     * @param comp
     * @return The parent window - Either the Dialog (should be XDialog) or the
     *         Frame (should be XhibitApplicationController)
     */
    public static Window getWindowAncestor(Component comp) {
        // First check if the component is a window
        if (comp instanceof Window)
            return (Window) comp;

        for (Container p = comp.getParent(); p != null; p = p.getParent()) {
            if (p instanceof Window) {
                return (Window) p;
            }
            if (p instanceof JPopupMenu) {
                return getWindowAncestor(((JPopupMenu) p).getInvoker());
            }
        }
        return null;
    }

    /**
     * This method will return the XhibitApplicationController is available. If
     * the component originates from a Dialog, the parent of the dialog will be
     * used to get to the XAC. If the dialog was not created with the XAC passed
     * into the Frame, this will return null.
     * 
     * @param comp
     * @return A Frame which should be an XhibitApplicationController. Null
     *         returned if Frame could not be found.
     */
    public static Frame getUltimateFrameAncestor(Component comp) {
        // Find the window for the supplied component (either Dialog or Frame
        // should be returned)
        Window w = getWindowAncestor(comp);
        if (w == null)
            return null;
        if (w instanceof Dialog) {
            // For dialogs look up the parent (passed in in the constructor
            // of XDialog)
            Component c = ((Dialog) w).getParent();
            if (c == null) {
                return null;
            } else if (c instanceof Frame) {
                return (Frame) c;
            } else {
                // If the parent is not null and not an XAC, but is another
                // component
                // recursively calls itself.
                if (c.getParent() == null) {
                    return null;
                } else if (c instanceof Component) {
                    return getUltimateFrameAncestor(c);
                } else {
                    return null;
                }
            }
        } else if (w instanceof Frame) {
            return (Frame) w;
        } else {
            // if the window is not a dialog or the XAC, and it has a parent
            // that is a component
            // recursively call itself,
            // otherwise return null.
            if (w.getParent() instanceof Component) {
                return getUltimateFrameAncestor(w.getParent());
            } else {
                return null;
            }
        }
    }
}