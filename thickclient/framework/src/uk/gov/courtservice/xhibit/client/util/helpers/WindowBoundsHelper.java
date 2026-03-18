package uk.gov.courtservice.xhibit.client.util.helpers;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XhibitProperties;

/**
 * <p>
 * Title: WindowBoundsHelper
 * </p>
 * <p>
 * Description: Print WLL Action
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell
 * @version $Id: WindowBoundsHelper.java,v 1.3 2006/06/05 12:30:43 bzjrnl Exp $
 */
public class WindowBoundsHelper extends WindowAdapter {
    /**
     * The name to use for the printpreview frame
     */
    public static final String PRINT_PREVIEW_FRAME_NAME = "printpreview";

    // The log to write debug to!
    private static final Logger log = CSServices.getLogger(WindowBoundsHelper.class);

    // The property file name. Note used as lock to stop reads and writes
    // occuring concurently.
    private static final String PROPERTY_FILE_NAME = XhibitProperties.XhibitWindowBounds + ".properties";

    // The name to store the bounds against
    private final String name;

    /**
     * Store the bounds of the window using the specified name! Names should be
     * declared as constants above to ensure there are no conflicts!
     */
    private WindowBoundsHelper(String p_name) {
        name = p_name;
    }

    /**
     * WindowListener Implementation. Store the component bounds.
     * 
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent e) {
        Component c = e.getComponent();
        if (c != null) {
            storeBounds(name, getBounds(c));
        }
    }

    /**
     * Set the bounds for the window, care should be taken to ensure the name is
     * unique.
     * 
     * @param p_window
     *            the window to set the bounds for
     * @param p_name
     *            the name to store the bounds for
     * @param p_defaultBounds
     *            the bounds to use if cant load bounds or bounds pub window off
     *            screen!
     * @param p_storeOnClose
     *            if true store the bounds when the window is closed
     */
    public static void setBounds(Window p_window, String p_name, Rectangle p_defaultBounds, boolean p_storeOnClose) {
        // Check Arguments
        if (p_window == null) {
            throw new IllegalArgumentException("p_window: null");
        }
        if (p_name == null) {
            throw new IllegalArgumentException("p_name: null");
        }
        if (p_defaultBounds == null) {
            throw new IllegalArgumentException("p_defaultBounds: null");
        }
        // Set the bounds from the stored values if valid else use default
        Rectangle bounds = loadBounds(p_name);
        if (bounds != null && checkBounds(bounds)) {
            setBounds(p_window, bounds);
        } else {
            setBounds(p_window, p_defaultBounds);
        }
        // If required add a listener to store the bounds on close
        if (p_storeOnClose) {
            p_window.addWindowListener(new WindowBoundsHelper(p_name));
        }
    }

    /**
     * Get the bounds centered in the default graphics configuration (screen)
     * 
     * @param window
     * @return a rectangle containing the bounds
     */
    public static Rectangle getDefaultBounds(Window window) {
        Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().getBounds();
        int windowWidth = window.getWidth();
        int windowHeight = window.getWidth();

        // if width to big fit to screen
        int x, width;
        if (windowWidth > screen.width) {
            x = screen.x;
            width = screen.width;
        } else {
            x = screen.x + ((screen.width - windowWidth) / 2);
            width = windowWidth;
        }

        // if height to big fit to screen
        int y, height;
        if (windowHeight > screen.height) {
            y = screen.y;
            height = screen.height;
        } else {
            y = screen.y + ((screen.height - windowHeight) / 2);
            height = windowHeight;
        }

        return new Rectangle(x, y, width, height);
    }

    // *** Access Bounds ***

    private static void setBounds(Component component, Rectangle bounds) {
        if (log.isDebugEnabled()) {
            _setBounds(component, bounds);
            log.debug("Bounds " + bounds + " set.");
        } else {
            _setBounds(component, bounds);
        }
    }

    private static void _setBounds(Component component, Rectangle bounds) {
        component.setBounds(bounds);
    }

    private static Rectangle getBounds(Component component) {
        if (log.isDebugEnabled()) {
            Rectangle bounds = _getBounds(component);
            log.debug("Bounds " + bounds + " got.");
            return bounds;
        } else {
            return _getBounds(component);
        }
    }

    private static Rectangle _getBounds(Component component) {
        return component.getBounds();
    }

    // *** Check Bounds ***

    // Check the bounds, return true if window (taskbar) will be at least
    // partialy visible
    private static boolean checkBounds(Rectangle bounds) {
        if (log.isDebugEnabled()) {
            boolean check = _checkBounds(bounds);
            log.debug("Bounds " + bounds + (check ? " are valid." : " are invalid."));
            return check;
        } else {
            return _checkBounds(bounds);
        }
    }

    private static boolean _checkBounds(Rectangle bounds) {
        return true;
    }

    // *** Store Bounds ***

    private static boolean storeBounds(String p_name, Rectangle bounds) {
        if (log.isDebugEnabled()) {
            boolean stored = _storeBounds(p_name, bounds);
            if (stored) {
                log.debug("Bounds " + bounds + " for \"" + p_name + "\" stored.");
            }
            return stored;
        } else {
            return _storeBounds(p_name, bounds);
        }
    }

    private static boolean _storeBounds(String p_name, Rectangle bounds) {
        try {
            Properties properties = loadPropertyFile();
            setIntProperty(properties, p_name + ".bounds.x", bounds.x);
            setIntProperty(properties, p_name + ".bounds.y", bounds.y);
            setIntProperty(properties, p_name + ".bounds.width", bounds.width);
            setIntProperty(properties, p_name + ".bounds.height", bounds.height);
            storePropertyFile(properties);
            return true;
        } catch (Exception ex) {
            log.warn("Bounds " + bounds + " for \"" + p_name + "\" not stored.", ex);
            return false;
        }
    }

    private static void setIntProperty(Properties properties, String key, int value) {
        properties.setProperty(key, String.valueOf(value));
    }

    private static synchronized void storePropertyFile(Properties properties) throws CSRecoverableException {
        synchronized (PROPERTY_FILE_NAME) {
            PropertyHelper.storeUserHomeProperties(properties, PROPERTY_FILE_NAME);
        }
    }

    // *** Load Bounds

    // Load the bounds with the given name, returns null if an error occures
    private static Rectangle loadBounds(String p_name) {
        if (log.isDebugEnabled()) {
            Rectangle bounds = _loadBounds(p_name);
            if (bounds != null) {
                log.debug("Bounds " + bounds + " for \"" + p_name + "\" loaded.");
            }
            return bounds;
        } else {
            return _loadBounds(p_name);
        }
    }

    private static Rectangle _loadBounds(String p_name) {
        try {
            Properties properties = loadPropertyFile();
            return new Rectangle(getIntProperty(properties, p_name + ".bounds.x"), getIntProperty(properties, p_name
                    + ".bounds.y"), getIntProperty(properties, p_name + ".bounds.width"), getIntProperty(properties,
                    p_name + ".bounds.height"));
        } catch (Exception ex) {
            log.warn("Bounds for \"" + p_name + "\" not loaded.", ex);
            return null;
        }
    }

    private static int getIntProperty(Properties properties, String key) throws NumberFormatException {
        return Integer.parseInt(properties.getProperty(key));
    }

    private static Properties loadPropertyFile() throws CSRecoverableException {
        // Load the property file, create if it doesnt exist.
        synchronized (PROPERTY_FILE_NAME) {
            Properties properties = PropertyHelper.getUserHomeProperties(PROPERTY_FILE_NAME);
            if (properties == null) {
                PropertyHelper.createUserHomeProperties(PROPERTY_FILE_NAME, "Generic User Properties");
                properties = PropertyHelper.getUserHomeProperties(PROPERTY_FILE_NAME);
            }
            return properties;
        }
    }

}