package uk.gov.courtservice.xhibit.client.util.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.Icon;

/**
 * Icon Implementation: Draw an aliased tick in the color of the components
 * foreground
 * 
 * @author Will Fardell, Xdevelopment 2004
 * @version $Revision: 1.3 $
 */
public class TickIcon implements Icon {

    /**
     * The icon width
     */
    public static final int WIDTH = 20;

    /**
     * The icon height
     */
    public static final int HEIGHT = 20;

    /**
     * The ticks xpoints (ratios from original tick)
     */
    private static final int[] XPOINTS = new int[] { 0, (int) (5.0 * (WIDTH / 14.0)), WIDTH,
            (int) (5.0 * (WIDTH / 14.0)), };

    /**
     * The ticks ypoints (ratios from original tick)
     */
    private static final int[] YPOINTS = new int[] { (int) (8.0 * (HEIGHT / 14.0)), HEIGHT, 0,
            (int) (10.0 * (HEIGHT / 14.0)) };

    /**
     * The ticks npoints (number of points)
     */
    private static final int NPOINTS = 4;

    /**
     * Icon Implementation: Draw the icon at the specified location. Icon
     * implementations may use the Component argument to get properties useful
     * for painting, e.g. the foreground or background color.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;

        // Store original
        Color oldColor = g.getColor();
        Object oldAntiAliasingHint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);

        // Draw Tick
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(c.getForeground());
        g.fillPolygon(getTick(x, y));

        // Reset to original
        g.setColor(oldColor);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntiAliasingHint);
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

    /**
     * Return a polygon outline of a tick translated by x and y
     */
    private static Polygon getTick(int x, int y) {
        Polygon tick = getTick();
        tick.translate(x, y);
        return tick;
    }

    /**
     * Return a polygon outline of a tick
     */
    private static Polygon getTick() {
        return new Polygon(XPOINTS, YPOINTS, NPOINTS);
    }
}