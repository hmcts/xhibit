package uk.gov.courtservice.xhibit.client.util;

import java.awt.Point;

import javax.swing.JMenu;
import javax.swing.SwingUtilities;

/**
 * Custom implementation of <code>JMenu</code> that is used to ensure that
 * when the menu is drawn on the screen, it does not go off at the top of the
 * screen.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 */
public class XMenu extends JMenu {
    /**
     * Only available constructor to instantiate this class.
     * 
     * @param menuTextLabel
     *            The text for the menu label
     */
    public XMenu(String menuTextLabel) {
        super(menuTextLabel);
    }

    /**
     * Computes the origin for this popup menu. If the y co-ordinate origin on
     * the screen is &lt; 0, then it will be reset to 0, so that the menu does
     * not appear off-screen.
     * 
     * @return A <code>Point</code> in the coordinate space of the menu which
     *         should be used as the origin of this popup menu.
     * 
     * @see javax.swing.JMenu#getPopupMenuOrigin()
     */
    protected Point getPopupMenuOrigin() {
        final Point p = super.getPopupMenuOrigin();

        // p is the point in the co-ordinate space of the component, we need
        // it according to the screen, so convert to screen co-ordinates...
        SwingUtilities.convertPointToScreen(p, this);

        // ensure that the y co-ordinate of the origin is on the screen...
        if (p.getY() < 0) {
            p.setLocation(p.getX(), 0);
        }

        // convert back to the co-ordinate space of the component...
        SwingUtilities.convertPointFromScreen(p, this);

        return p;
    }
}
