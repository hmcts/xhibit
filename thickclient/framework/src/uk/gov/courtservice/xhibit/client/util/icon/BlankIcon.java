package uk.gov.courtservice.xhibit.client.util.icon;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * Icon Implementation: This blank icon can be used as a spacer
 * 
 * @author Will Fardell, Xdevelopment 2004
 * @version $Revision: 1.4 $
 */
public class BlankIcon implements Icon {

    /**
     * The icon width
     */
    public static final int WIDTH = 20;

    /**
     * The icon height
     */
    public static final int HEIGHT = 20;

    /**
     * Icon Implementation: Draw the icon at the specified location. Icon
     * implementations may use the Component argument to get properties useful
     * for painting, e.g. the foreground or background color.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        // Blank so do nothing
    }

    /**
     * Icon Implementation: Returns the icon's width.
     * 
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth() {
        return WIDTH;
    }

    /**
     * Icon Implementation: Returns the icon's height.
     * 
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight() {
        return HEIGHT;
    }
}