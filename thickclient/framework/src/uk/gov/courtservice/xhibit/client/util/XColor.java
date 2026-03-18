package uk.gov.courtservice.xhibit.client.util;

import java.awt.Color;

/**
 * <p>
 * Title: XColor
 * </p>
 * <p>
 * Description: Colors with names
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell
 * @version $Revision: 1.3 $
 */
public class XColor extends Color {
    /**
     * Red
     */
    public static final XColor RED = new XColor("xcolor.red", Color.red);

    /**
     * Green
     */
    public static final XColor GREEN = new XColor("xcolor.green", Color.green);

    /**
     * Blue
     */
    public static final XColor BLUE = new XColor("xcolor.blue", Color.blue);

    /**
     * Black
     */
    public static final XColor BLACK = new XColor("xcolor.black", Color.black);

    /**
     * White
     */
    public static final XColor WHITE = new XColor("xcolor.white", Color.white);

    /**
     * Orange
     */
    public static final XColor ORANGE = new XColor("xcolor.orange", Color.orange);

    /**
     * Magenta
     */
    public static final XColor MAGENTA = new XColor("xcolor.magenta", Color.magenta);

    /**
     * Pink
     */
    public static final XColor PINK = new XColor("xcolor.pink", Color.pink);

    /**
     * Cyan
     */
    public static final XColor CYAN = new XColor("xcolor.cyan", Color.cyan);

    /**
     * The name of the color
     */
    private final String name;

    /**
     * Construct a new instance of this named color
     */
    private XColor(String key, Color color) {
        super(color.getRGB());
        if (key == null) {
            throw new IllegalArgumentException("key: null");
        }
        this.name = XHIBITConstant.getResourceBundle(XhibitBundles.UtilResources).getString(key);
    }

    /**
     * Construct a new instance of this named color
     */
    public XColor(String name, int r, int g, int b) {
        super(r, g, b);
        if (name == null) {
            throw new IllegalArgumentException("name: null");
        }
        this.name = name;
    }

    /**
     * Construct a new instance of this named color
     */
    public XColor(String name, int r, int g, int b, int t) {
        super(r, g, b, t);
        if (name == null) {
            throw new IllegalArgumentException("name: null");
        }
        this.name = name;
    }

    /**
     * Return the name of this color
     */
    public String getName() {
        return name;
    }

    /**
     * Construct a new instance of this named color
     */
    public String toString() {
        return name;
    }
}
