package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPopupMenu;

/**
 * <p>
 * Title: Popup menu that will be displayed on the screen
 * </p>
 * <p>
 * Description: Extends JPopupMenu overriding the show method to ensure that the
 * popup is displayed on the screen.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Simon Gilmore
 * @version $Revision: 1.5 $
 */
public class XPopupMenu extends JPopupMenu {
    /**
     * Constructs a default XPopupMenu.
     */
    public XPopupMenu() {
        super();
    }

    /**
     * Overrides the show method in JPopupMenu
     * 
     * @param invoker
     *            the component in whose space the popup menu is to appear.
     * @param x
     *            the x coordinate in invoker's coordinate space at which the
     *            popup menu is to be displayed
     * @param y
     *            the x coordinate in invoker's coordinate space at which the
     *            popup menu is to be displayed
     */
    public void show(Component invoker, int x, int y) {
        if (invoker != null) {
            Point pt = invoker.getLocationOnScreen();
            Dimension sizePopup = getPreferredSize();
            Dimension sizeScreen = Toolkit.getDefaultToolkit().getScreenSize();

            if (sizePopup != null && sizeScreen != null) {
                if (pt.x + x + sizePopup.width > sizeScreen.width) {
                    // move it to left of cursor
                    x -= sizePopup.width;
                }

                // off bottom of screen?
                if (pt.y + y + sizePopup.height > sizeScreen.height) {
                    // move it above cursor
                    y -= sizePopup.height;
                }

                // no matter what, make sure it doesn't go off top of screen
                if (pt.y + y < 0) {
                    y = -pt.y + 5;
                }
            }
        }

        super.show(invoker, x, y);
    }
}