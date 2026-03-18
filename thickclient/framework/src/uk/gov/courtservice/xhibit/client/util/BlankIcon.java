package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * <p>
 * Title: BlankIcon
 * </p>
 * <p>
 * Description: An implementation of the Icon interface that paints blank
 * (empty) icons
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class BlankIcon implements Icon {
    /** width in pixels of the icon. */
    private int width;

    /** height in pixels of the icon. */
    private int height;

    /**
     * Creates a blank icon of the specified width and height.
     * 
     * @param width
     *            of the icon
     * @param height
     *            of the icon
     */
    public BlankIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a blank icon with the default width and height.
     */
    public BlankIcon() {
        this(18, 18);
    }

    /**
     * Paints the icon. The top-left corner of the icon is drawn at the point
     * (x, y) in the coordinate space of the graphics context g. If this icon
     * has no image observer, this method uses the c component as the observer.
     * 
     * @param c
     *            the component to be used as the observer if this icon has no
     *            image observer.
     * @param g
     *            the graphics context.
     * @param x
     *            the X coordinate of the icon's top-left corner.
     * @param y
     *            the Y coordinate of the icon's top-left corner.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        // Don't want to paint anything as this is a blank icon.
    }

    /**
     * Get the width of the icon.
     * 
     * @return the width in pixels of this icon.
     */
    public int getIconWidth() {
        return width;
    }

    /**
     * Get the height of the icon.
     * 
     * @return the height in pixels of this icon.
     */
    public int getIconHeight() {
        return height;
    }
}