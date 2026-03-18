/*
 *   Copyright (c) 2000 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
 * 
 *   The author makes no representations or warranties about the suitability of the
 *   software, either express or implied, including but not limited to the
 *   implied warranties of merchantability, fitness for a particular
 *   purpose, or non-infringement. The author shall not be liable for any damages
 *   suffered by licensee as a result of using, modifying or distributing
 *   this software or its derivatives.
 *
 *   The author requests that he be notified of any application, applet, or other binary that 
 *   makes use of this code and that some acknowedgement is given. Comments, questions and 
 *   requests for change will be welcomed.
 */
package mseries.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

public class ScreenUtilities {

    /**
     * Finds the parent container (Window, JRootPane) of the component. This is
     * useful for drawing components in the container since all co-ords are
     * reltive to the component and not its parent
     * 
     * @param c
     *            the component
     * @return its parent
     */
    public static Container getParentWindow(Component c) {
        Container parent = null;
        if (c != null) {
            parent = c.getParent();
        }

        for (Container p = parent; p != null; p = p.getParent()) {
            if (p instanceof JRootPane) {
                if (p.getParent() instanceof JInternalFrame) {
                    continue;
                }

                parent = ((JRootPane) p).getLayeredPane();
                for (p = parent.getParent(); p != null && (!(p instanceof java.awt.Window)); p = p.getParent())
                    ;
                break;
            } else if (p instanceof Window) {
                parent = p;
                break;
            }
        }
        return parent;
    }

    /**
     * Given a point ona screen this method calculates the absolute point in the
     * parent container (Frame)
     */
    public static Point convertScreenLocationToParent(Container parent, int x, int y) {
        Window parentWindow = null;
        Rectangle r;
        for (Container p = parent; p != null; p = p.getParent()) {
            if (p instanceof Window) {
                parentWindow = (Window) p;
                break;
            }
        }
        if (parentWindow != null) {
            Point p = new Point(x, y);
            SwingUtilities.convertPointFromScreen(p, parent);
            return p;
        } else {
            throw new Error("convertScreenLocationToParent: no window ancestor found");
        }
    }

    /**
     * @return a Frame or Window that is the first one in the hierarchy
     *         containing the component passed
     */
    public static Window getParentFrameOrWindow(Component child) {
        Window parentWindow = null;
        Rectangle r;
        for (Container p = child.getParent(); p != null; p = p.getParent()) {
            if (p instanceof Frame) {
                parentWindow = (Frame) p;
                break;
            }
            if (p instanceof Window) {
                parentWindow = (Window) p;
                break;
            }
        }
        return parentWindow;
    }
}
