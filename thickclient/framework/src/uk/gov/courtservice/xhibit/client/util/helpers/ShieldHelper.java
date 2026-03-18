package uk.gov.courtservice.xhibit.client.util.helpers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.ShieldInterface;

/**
 * <p>
 * Title: Helper class that tracks the shield status
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Rakesh Lakhani
 * @version $Id: ShieldHelper.java,v 1.13 2007/02/23 12:53:53 szfnvt Exp $
 */
public class ShieldHelper {
    /**
     * A logger
     */
    private static final Logger log = CSServices.getLogger(ShieldHelper.class);

    /**
     * Used to cache the glasspane
     */
    private Component glassPaneCache = null;

    /**
     * Used to cache the shieldpane
     */
    private ShieldPane shieldPaneCache = new ShieldPane();

    /**
     * The component that had focus before shield
     */
    private Component focusedBeforeShield = null;

    /**
     * Int the current level of the shield.
     */
    private int level = 0;

    private final Window window;

    /**
     *
     * @param window
     *            Window must implement ShieldInterface
     */
    public ShieldHelper(Window window) {
        if (!(window instanceof ShieldInterface)) {
            throw new IllegalArgumentException("window: " + window);
        }
        this.window = window;
    }

    /**
     * Shield the window from user input
     */
    public void shield() {
        synchronized (window.getTreeLock()) {
            if (level++ == 0) {
                log.debug("Dialog " + ((ShieldInterface) window).getTitle() + " raising shield.");

                glassPaneCache = ((ShieldInterface) window).getGlassPane();
                ((ShieldInterface) window).setGlassPane(shieldPaneCache);

                // shieldPaneCache.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                focusedBeforeShield = getFocusedComponent(window);
                shieldPaneCache.requestFocus();

                window.repaint();
                // This makes the glass pane appear imediatly
                window.validate();
            } else {
                log.debug("Dialog " + ((ShieldInterface) window).getTitle() + " increasing shield power to level "
                        + level + ".");
            }
        }
    }

    /**
     * Unshield the window from user input
     */
    public void unshield() {
        synchronized (window.getTreeLock()) {
            if (level == 0)
                return;
            if (--level == 0) {
                log.debug("Dialog " + ((ShieldInterface) window).getTitle() + " lowering shield.");
                if (glassPaneCache != null) {
                    // Setting glass pane back to original.
                    // Note: To ensure that the original pane visibility is
                    // maintained
                    // we need to reset it after calling setGlassPane as
                    // this method
                    // will set the visibility to that of the shield pane
                    // which is true.
                    boolean visible = glassPaneCache.isVisible();
                    ((ShieldInterface) window).setGlassPane(glassPaneCache);
                    glassPaneCache.setVisible(visible);
                    glassPaneCache = null;
                }

                window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                if (window.isActive()){
                    if (focusedBeforeShield != null) {
                        focusedBeforeShield.requestFocus(); 
                        focusedBeforeShield = null;
                    } else {
                        Component comp = getFirstSelectableComponent(window);
                        if (comp != null) {
                            log.debug("unshield() focusedBeforeShield is null");
                            comp.requestFocus();
                        }
                    }
                }
                window.repaint();

                // This works arround the MouseEntered bug
                nudgeMouse(shieldPaneCache.getLastMousePoint(), window);
                shieldPaneCache.clearMousePoint();
            } else {
                log.debug("Dialog " + ((ShieldInterface) window).getTitle() + " reducing shield power to level "
                        + level + ".");
            }
        }
    }

    /**
     * Method to work arround MouseEntered not generated when component appears
     * under mouse Move the mouse (issue an underlying operating system event
     */
    private void nudgeMouse(Point p, Window w) {
        log.debug("Nudging of mouse has been disabled.");
        /*
         * if (p != null) { try { Robot r = new
         * Robot(w.getGraphicsConfiguration().getDevice());
         * r.mouseMove((int)p.getX() + 1, (int)p.getY()); log.debug("Nudged
         * mouse."); } catch (AWTException awte) { log.warn("Could not nudge
         * mouse.", awte); } } else { log.warn("Could not nudge mouse, no point
         * recorded."); }
         */
    }

    /**
     * @return the currently focused component or null
     * @param the
     *            component to start looking at
     */
    private Component getFocusedComponent(Component component) {
        if (component != null) {
            if (component.hasFocus()) {
                return component;
            } else if (component instanceof Container) {
                Container container = (Container) component;
                for (int i = 0, c = container.getComponentCount(); i < c; i++) {
                    Component focused = getFocusedComponent(container.getComponent(i));
                    if (focused != null) {
                        return focused;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Get the first sub-component of the component passed in that allows focus
     * to be passed to it.
     *
     * @param component
     * @return
     * @see java.awt.Component#isFocusTraversable()
     */
    private Component getFirstSelectableComponent(Component component) {
        if (component != null) {
            if (component.isFocusable()) {
                return component;
            } else if (component instanceof Container) {
                Container container = (Container) component;
                for (int i = 0, c = container.getComponentCount(); i < c; i++) {
                    Component focused = getFirstSelectableComponent(container.getComponent(i));
                    if (focused != null) {
                        return focused;
                    }
                }
            }
        }

        return null;
    }

    /**
     * AbstractPane
     */
    private static abstract class AbstractPane extends JPanel {
        /**
         * Construct a new abstract pane
         */
        public AbstractPane() {
            super();
        }

        /**
         * Construct a new abstract pane with the specified layout manager
         */
        public AbstractPane(LayoutManager l) {
            super(l);
        }

        /**
         * Get the component name
         */
        public String getName() {
            String name = super.getName();
            if (name != null) {
                return name;
            } else {
                String parentName = getParentName();
                return parentName == null ? getUnqualifiedClassName() : parentName + getUnqualifiedClassName();
            }
        }

        /**
         * Get the parent name
         */
        private String getParentName() {
            Component parent = getParent();
            return parent == null ? null : parent.getName();
        }

        /**
         * Get the unqualifed name of the class
         */
        private String getUnqualifiedClassName() {
            String name = getClass().getName();
            int index = name.lastIndexOf(".");
            if (index == -1) {
                return name;
            } else {
                return name.substring(index + 1);
            }
        }
    }

    /**
     * The shield pane intercepts all input events
     */
    private static final class ShieldPane extends AbstractPane implements MouseListener, MouseMotionListener,
            KeyListener, FocusListener {
        /**
         * Name of system property, set to true to paint shield pane
         */
        private static final String PAINT_KEY = "shield.pane.paint";

        /**
         * The text to paint
         */
        private static final String PAINT_TEXT = "SHIELD!";

        /**
         * If true paint shield overlay
         */
        private static final boolean PAINT = getPaint();

        /**
         * The last mouse point recorded
         */
        private Point lastMousePoint = null;

        /**
         * Construct a new shield pane
         */
        public ShieldPane() {
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.addKeyListener(this);
            this.addFocusListener(this);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.setOpaque(false);

            if (PAINT) {
                setFont(new Font("Arial", Font.BOLD, 36));
            }
        }

        /**
         * Allow the shield to accept focus
         */
        public boolean isFocusTraversable() {
            return true;
        }

        /**
         * Stop the pane being disabled
         *
         * @param enabled
         *            enable the frame;
         */
        public void setEnabled(boolean enabled) {
        }

        /**
         * Stop the pane being hidden
         *
         * @param enabled
         *            enable the frame;
         */
        public void setVisible(boolean visible) {
        }

        /**
         * Get the last mouse point in screen courdinates
         */
        public Point getLastMousePoint() {
            return lastMousePoint;
        }

        /**
         * Removes the record of last mouse point.
         */
        public void clearMousePoint() {
            lastMousePoint = null;
        }

        /**
         * MouseListener Implementation
         *
         * @see java.awt.event.MouseListener#mouseClicked(MouseEvent);
         */
        public void mouseClicked(MouseEvent e) {
            recordPoint(e);
        }

        /**
         * MouseListener Implementation
         *
         * @see java.awt.event.MouseListener#mouseExited(MouseEvent);
         */
        public void mouseExited(MouseEvent e) {
            clearMousePoint();
            e.consume();
        }

        /**
         * MouseListener Implementation
         *
         * @see java.awt.event.MouseListener#mousePressed(MouseEvent);
         */
        public void mousePressed(MouseEvent e) {
            recordPoint(e);
        }

        /**
         * MouseListener Implementation
         *
         * @see java.awt.event.MouseListener#mouseReleased(MouseEvent);
         */
        public void mouseReleased(MouseEvent e) {
            recordPoint(e);
        }

        /**
         * MouseListener Implementation
         *
         * @see java.awt.event.MouseListener#mouseEntered(MouseEvent);
         */
        public void mouseEntered(MouseEvent e) {
            recordPoint(e);
        }

        /**
         * MouseMotionListener Implementation
         *
         * @see java.awt.event.MouseListener#mouseDragged(MouseEvent);
         */
        public void mouseDragged(MouseEvent e) {
            recordPoint(e);
        }

        /**
         * MouseMotionListener Implementation
         *
         * @see java.awt.event.MouseListener#mouseMoved(MouseEvent);
         */
        public void mouseMoved(MouseEvent e) {
            recordPoint(e);
        }

        /**
         * Used to record the point from e
         */
        public void recordPoint(MouseEvent e) {
            Point mousePoint = new Point(e.getX(), e.getY());
            SwingUtilities.convertPointToScreen(mousePoint, e.getComponent());
            lastMousePoint = mousePoint;
            e.consume();
        }

        /**
         * KeyListener Implementation
         *
         * @see java.awt.event.KeyListener#keyTyped(KeyEvent);
         */
        public void keyTyped(KeyEvent e) {
            e.consume();
        }

        /**
         * KeyListener Implementation
         *
         * @see java.awt.event.KeyListener#keyPressed(KeyEvent);
         */
        public void keyPressed(KeyEvent e) {
            e.consume();
        }

        /**
         * KeyListener Implementation
         *
         * @see java.awt.event.KeyListener#keyReleased(KeyEvent);
         */
        public void keyReleased(KeyEvent e) {
            e.consume();
        }

        /**
         * FocusListener Implementation
         *
         * @see java.awt.event.FocusListener#focusGained(FocusEvent);
         */
        public void focusGained(FocusEvent e) {
        }

        /**
         * FocusListener Implementation
         *
         * @see java.awt.event.FocusListener#focusLost(FocusEvent);
         */
        public void focusLost(FocusEvent e) {
            if (!e.isTemporary()){
                requestFocus();
            }
        }
        /**
         * Make component visable
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (PAINT) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Shade background
                g2.setColor(new Color(255, 0, 0, 64));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Draw text
                Rectangle2D r = g2.getFontMetrics().getStringBounds(PAINT_TEXT, g2);
                g2.setColor(new Color(200, 0, 0));
                g2.drawString(PAINT_TEXT, (getWidth() - (int) r.getWidth()) / 2,
                        ((getHeight() - (int) r.getHeight()) / 2) + (int) r.getHeight());
            }
        }

        /**
         * Return true if shield pane should paint
         */
        private static final boolean getPaint() {
            String paintProperty = System.getProperty(PAINT_KEY);
            return paintProperty != null
                    && (paintProperty.equalsIgnoreCase("TRUE") || paintProperty.equalsIgnoreCase("YES"));
        }
    }
}
