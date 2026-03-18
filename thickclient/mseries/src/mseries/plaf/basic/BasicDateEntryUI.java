/*
 *   Copyright (c) 2001 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
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
package mseries.plaf.basic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.ComponentUI;

import mseries.Calendar.MDateSelectorPanel;
import mseries.Calendar.MMonthEvent;
import mseries.Calendar.MMonthListener;
import mseries.ui.ArrowButton;
import mseries.ui.MChangeEvent;
import mseries.ui.MDateEntryField;
import mseries.ui.MDateField;
import mseries.ui.MPopup;
import mseries.ui.MPopupPanel;
import mseries.ui.MPopupWindow;
import mseries.ui.ScreenUtilities;
import mseries.utils.MComboBoxLayout;

public class BasicDateEntryUI extends ComponentUI implements PropertyChangeListener {
    // The arrow button that invokes the popup.
    protected JButton arrowButton;

    boolean isShowing = false;

    Action activator;
    
    Action deactivator;

    MDateField display;

    MDateSelectorPanel panel;

    MPopup popup;

    Border border = null;

    MMonthListener mMonthListener;

    AncestorListener ancListener;
    
    protected MDateEntryField dateEntry;

    private final Object classLock = new Object();

    private static final int MAX_CACHE_SIZE = 1;

    private final Vector<MPopup> lightPopupCache = new Vector<MPopup>(MAX_CACHE_SIZE);

    private static final Vector<MPopup> heavyPopupCache = new Vector<MPopup>(MAX_CACHE_SIZE);

    Dimension d;

    /**
     * This method is called by the UIManager to get an instance of this class
     * and must be overridden in subclasses.
     */
    public static ComponentUI createUI(JComponent x) {
        return new BasicDateEntryUI();
    }

    /*
     * Called by the UIManager to install the UI of the component
     */
    public void installUI(JComponent c) {
        dateEntry = (MDateEntryField) c;

        installComponents();

        configureDisplay(dateEntry.getDisplay());
        configureBorder(dateEntry);

        dateEntry.setLayout(createLayoutManager());

        installListeners();
    }

    public void uninstallUI(JComponent c) {
        dateEntry.setLayout(null);
        uninstallListeners();
        uninstallComponents();
    }

    public void update(Graphics g, JComponent c) {
        paint(g, c);
    }

    public void paint(Graphics g, JComponent c) {
        // empty
    }

    protected void installListeners() {
        if ((activator = createOpenActionListener()) != null) {
            arrowButton.addActionListener(activator);
        }
        deactivator = createCloseActionListener();
        if ((ancListener = createAncestorListener()) != null) {
            dateEntry.addAncestorListener(ancListener);
        }
        registerKeyboardActions();
        mMonthListener = createMMonthListener();

        dateEntry.addPropertyChangeListener(this);

    }

    protected void uninstallListeners() {
        arrowButton.removeActionListener(activator);
        dateEntry.addPropertyChangeListener(this);
        unRegisterKeyboardActions();
        dateEntry.removeAncestorListener(ancListener);
    }

    /**
     * Creates the standard combo box layout manager that has the arrow button
     * to the right and the editor to the left. Returns an instance of
     * BasicComboBoxUI$ComboBoxLayoutManager.
     */
    protected LayoutManager createLayoutManager() {
        return new MComboBoxLayout();
    }

    /**
     * The editor and arrow button are added to the JComboBox here.
     */
    protected void installComponents() {
        display = dateEntry.getDisplay();
        dateEntry.add(display);

        arrowButton = createArrowButton();
        dateEntry.add(arrowButton);
    }

    protected void uninstallComponents() {
        arrowButton = null;
        dateEntry.removeAll();
    }

    /**
     * Creates the arrow button. Subclasses can create any button they like. The
     * default behavior of this class is to attach various listeners to the
     * button returned by this method. Returns an instance of BasicArrowButton.
     */
    protected JButton createArrowButton() {
        JButton x = new ArrowButton(ArrowButton.SOUTH);
        x.setBackground(dateEntry.getBackground());
        x.setForeground(dateEntry.getBackground());
        return x;
    }

    /**
     * Gets the insets from the JComboBox.
     */
    protected Insets getInsets() {
        return dateEntry.getInsets();
    }

    /**
     * This is where we add a border the component, (the display field and the
     * button)
     * 
     * @param c
     *            the entire component
     */
    protected void configureBorder(@SuppressWarnings("unused")JComponent c) {
        // empty
    }

    /**
     * This is where we would configure the display field part of the component,
     * such as remove the dfault border and change preferred size.
     * 
     * @param display
     *            the display part of the component
     */
    public void configureDisplay(@SuppressWarnings("unused")JComponent display) {
        // emtpy
    }

    /**
     * Class to encapsulate the open and close action, activated by the button
     * and keyboard keys
     */
    protected class OpenAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
            if (isShowing) {
                // The arrow button acts like a toggle
                // opening and closing the dialog.
                isShowing = false;
                panel.close("");
                display.requestFocus();
            } else {
                isShowing = true;
                showPopup();
            }
        }
    }
    
    protected class CloseAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
            isShowing = false;
            panel.close("");
            display.requestFocus();
        }
    }

    protected Action createOpenActionListener() {
        return new OpenAction();
    }

    protected Action createCloseActionListener() {
        return new CloseAction();
    }
   
    protected void registerKeyboardActions() {

        display.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.ALT_MASK), "OPEN");
        display.getActionMap().put("OPEN", activator);
    }

    protected void unRegisterKeyboardActions() {
        // display.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
        // InputEvent.ALT_MASK));
    }

    private void showPopup() {
        if (panel == null) {
            panel = createMDateSelectorPanel();
        }
        if (popup == null) {
            dateEntry.notifyListeners(new FocusEvent(dateEntry, FocusEvent.FOCUS_GAINED));
            /*
             * Remove the focus listener so that when the pull down receives the
             * focus, the focusLost event is not caught
             */
            dateEntry.opened();
            Point p = dateEntry.getLocationOnScreen();

            panel.setShowTodayButton(dateEntry.getShowTodayButton());
            panel.setCloseOnToday(dateEntry.getCloseOnToday());
            panel.setPullDownConstraints(dateEntry.getConstraints());
            panel.setBorder(createBorder());
            d = panel.getPreferredSize();

            if (checkLightPosition(dateEntry, p)) {
                popup = createLightWeightPopup();
            } else {
                checkHeavyPosition(dateEntry, p);
                popup = createHeavyWeightPopup();
            }

            /* This is where the MCalendarPanel is configured */

            if (display.getMinimum() != null) {
                panel.setMinimum(display.getMinimum());
            }
            if (display.getMaximum() != null) {
                panel.setMaximum(display.getMaximum());
            }

            panel.setDate(display.getValue(new Date()));
            panel.addMMonthListener(mMonthListener);

            popup.setShadow(dateEntry.getConstraints().hasShadow());
            popup.addComponent(panel, BorderLayout.CENTER);
            popup.pack();
            popup.setLocationOnScreen(p.x, p.y);
            popup.setParent(display);
            popup.setVisible(true);

            popup.requestFocus();
            dateEntry.notifyListeners(MChangeEvent.PULLDOWN_OPENED);

        }
    }

    private void destroyPopup() {
        if (popup != null) {
            panel.removeMMonthListener(mMonthListener);
            popup.setVisible(false);
            dateEntry.notifyListeners(MChangeEvent.PULLDOWN_CLOSED);
            isShowing = false;
            popup.removeComponent(panel);
            switch (popup.getWeight()) {
            case MPopup.LIGHT:
                recycleLightPopup(popup);
                break;
            case MPopup.HEAVY:
                // Don't recycle heavy weights until we sort out the focus
                // problems with JWindow
                // recycleHeavyPopup(popup);
                break;
            default:
            }
        }
        popup = null;
    }

    protected MDateSelectorPanel createMDateSelectorPanel() {
        MDateSelectorPanel panel;

        panel = new MDateSelectorPanel();
        panel.setFocusCycleRoot(true);

        panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.ALT_MASK), "CLOSE");
        panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "CLOSE");
        panel.getActionMap().put("CLOSE", deactivator);

        panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CANCEL");
        panel.getActionMap().put("CANCEL", deactivator);

        return panel;
    }

    private boolean checkLightPosition(Component field, Point p) {
        Container parent;
        if (SwingUtilities.getAncestorOfClass(JFrame.class, field) == null
                && SwingUtilities.getAncestorOfClass(JDialog.class, field) == null) {
            return false;
        }
        parent = SwingUtilities.getAncestorOfClass(Window.class, field);
        Point pr = parent.getLocationOnScreen();
        // pr represents the field position relative to its parent frame
        pr.x = p.x - pr.x;
        pr.y = p.y - pr.y;

        return getPositionRelative(d, field.getSize(), parent.getSize(), pr, p);
    }

    private boolean getPositionRelative(Dimension popupSize, Dimension fieldSize, Dimension parent, Point pr, Point p) {
        Point pos = new Point(p.x, p.y);
        // Vertical
        if (pr.y + fieldSize.height + popupSize.height <= parent.height) {
            // Draw popup below the field
            pos.y = p.y + fieldSize.height;
        } else if (pr.y - d.height >= 0) {
            // Draw popup above the field
            pos.y = p.y - d.height;
        } else {
            return false;
        }

        // Horizontal
        if (pr.x + popupSize.width <= parent.width) {
            // Fits OK
        } else if (pr.x + fieldSize.width - popupSize.width >= 0) {
            pos.x = pos.x - (popupSize.width - fieldSize.width);
        } else {
            return false;
        }

        p.x = pos.x;
        p.y = pos.y;
        return true;
    }

    private boolean checkHeavyPosition(Component field, Point p) {
        Point pr = field.getLocation();
        SwingUtilities.convertPointToScreen(pr, field);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return getPositionRelative(d, field.getSize(), screenSize, p, p);
    }

    /*
     * @returns a light weight Popup , either a brand new one or a recycled one
     */
    private MPopup createLightWeightPopup() {
        MPopup p;
        p = getRecycledLightPopup();
        if (p == null) {
            p = new MPopupPanel();
        }
        return p;

    }

    private MPopup getRecycledLightPopup() {
        synchronized (classLock) {
            if ((lightPopupCache.size()) > 0) {
                MPopup r = lightPopupCache.elementAt(0);
                lightPopupCache.removeElementAt(0);
                return r;
            }
            return null;
        }
    }

    private void recycleLightPopup(MPopup aPopup) {
        synchronized (classLock) {
            if (lightPopupCache.size() < MAX_CACHE_SIZE) {
                lightPopupCache.addElement(aPopup);
            }
        }
    }

    private MPopup createHeavyWeightPopup() {
        MPopup p;
        p = getRecycledHeavyPopup();

        if (p == null) {
            Window parent = null;
            if ((parent = ScreenUtilities.getParentFrameOrWindow(display)) != null) {
                p = new MPopupWindow(parent);
            } else {
                p = new MPopupWindow();
            }
        }
        return p;

    }

    private MPopup getRecycledHeavyPopup() {
        synchronized (classLock) {
            if ((heavyPopupCache.size()) > 0) {
                MPopup r = heavyPopupCache.elementAt(0);
                heavyPopupCache.removeElementAt(0);
                return r;
            }
            return null;
        }
    }

    /*
     * Manufactures a border for the popup calendar. Subclasses should return a
     * zero width empty border if no border is required. @return a border for
     * the popup calendar
     */
    private Border createBorder() {
        if (border == null) {
            Border innerBorder = BorderFactory.createEmptyBorder(2, 3, 0, 3);
            Border outerBorder = BorderFactory.createLineBorder(Color.black);
            return BorderFactory.createCompoundBorder(outerBorder, innerBorder);
        }
        return border;

    }

    protected MMonthListener createMMonthListener() {
        return new MMonthListener() {
            boolean echoSelection = true;

            public void dataChanged(MMonthEvent e) {
                int type = e.getType();
                if (type == MMonthEvent.EXITED) {
                    destroyPopup();
                    dateEntry.closed();
                    SwingUtilities.invokeLater(testFocus);
                }
                if (type == MMonthEvent.SELECTED) {
                    display.setValue(e.getNewDate().getTime());
                    destroyPopup();
                    display.requestFocus();
                    dateEntry.closed();
                }
                echoSelection = dateEntry.getConstraints().isSelectionEventsEnabled();

                if (((type == MMonthEvent.NEW_DATE) || (type == MMonthEvent.NEW_MONTH) || (type == MMonthEvent.SELECTED))
                        && echoSelection) {
                    display.setValue(e.getNewDate().getTime());
                    dateEntry.notifyListeners(MChangeEvent.CHANGE);
                }
            }
        };
    }

    /*
     * This variable is invoked by SwingUtiities.invokeLater after the
     * focusEvents have been process so that we can determine where the focus
     * end up. If it is outside of the component then we need to fire the event
     * otherwise the component still has focus so no event is need yet
     */
    Runnable testFocus = new Runnable() {
        public void run() {
            if (!display.hasFocus()) {
                dateEntry.notifyListeners(new FocusEvent(dateEntry, FocusEvent.FOCUS_LOST));
            }
        }
    };

    /**
     * Deals with the components ancestor being moved & removed, especially for
     * when the component is used in an Applet & the browser is closed.
     */
    protected AncestorListener createAncestorListener() {
        return new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                destroyPopup();
                if (isShowing) {
                    display.requestFocus();
                }
            }

            public void ancestorRemoved(AncestorEvent event) {
                destroyPopup();
                if (isShowing) {
                    display.requestFocus();
                }
            }

            public void ancestorMoved(AncestorEvent event) {
                destroyPopup();
                if (isShowing) {
                    display.requestFocus();
                }
            }
        };
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String event = evt.getPropertyName();
        if (event.equals("enabled")) {
            Object o = evt.getNewValue();
            boolean enabled = ((Boolean) o).booleanValue();
            display.setEnabled(enabled);
            if (arrowButton != null) {
                arrowButton.setEnabled(enabled);
            } else {
                System.out.println("arrowbutton is null");
            }
            if (enabled) {
                registerKeyboardActions();
            } else {
                unRegisterKeyboardActions();
            }
        }
    }
}

/*
 * $Log: BasicDateEntryUI.java,v $
 * Revision 1.5  2014/06/22 17:56:47  atwells
 * 8.6.2 WebLogic Upgrade - JDK1.6 amendments as setEnabled method had changed
 *
 * Revision 1.4  2009/10/09 11:16:11  dunnepi
 * Merger of HK 8.3.11 branch to the trunk
 *
 * Revision 1.3.14.2  2009/09/11 08:15:52  hewittm
 * PR6019: Fix keyboard traversal issues
 *
 * Revision 1.3.14.1  2009/09/08 10:01:34  hewittm
 * PR6019: Fix a null pointer error when ALT-<down arrow> is pressed
 *
 * Revision 1.3  2006/06/05 12:31:52  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.2 2006/05/31 14:26:07 bzjrnl
 * Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting /*
 * Revision 1.1 2004/04/02 15:44:04 sz0t7n /* Adding mseries to the build so we
 * can bug fix it from now on /* /* Revision 1.16 2003/01/18 16:40:09 martin /*
 * *** empty log message *** /* /* Revision 1.15 2003/01/15 21:47:24 martin /*
 * *** empty log message *** /* /* Revision 1.14 2002/12/21 22:53:16 martin /*
 * *** empty log message *** /* /* Revision 1.13 2002/11/20 20:17:42 martin /*
 * Fixed bug for InputVerifier and AncesterListener /* /* Revision 1.12
 * 2002/08/29 20:56:44 martin /* *** empty log message *** /* /* Revision 1.11
 * 2002/06/13 19:25:14 martin /* Added closeOnToday button support /* /*
 * Revision 1.10 2002/02/27 22:03:57 martin /* Removed unregisterKeyboardAction /* /*
 * Revision 1.9 2002/02/27 21:53:14 martin /* Replaced obsolete method
 * JComponent.registerKeyboardAction with JDK1.3.1 versions /* /* Revision 1.8
 * 2002/02/24 12:33:47 martin /* Fixed a placement bug for heavy weight popup /* /*
 * Revision 1.7 2002/02/16 18:13:09 martin /* The events to update the text
 * field are switchable and can be disabled. This makes the escape key more
 * effective /* /* Revision 1.6 2002/02/04 20:11:48 martin /* Removed re-cycle
 * of heavy weights /* /* Revision 1.5 2002/02/03 13:06:27 martin /* Recycle
 * Heavyweight popup /* /* Revision 1.4 2002/01/22 21:36:07 martin /* Find and
 * pass owner to heavyweight popup calendar /* /* Revision 1.3 2002/01/01
 * 14:38:02 martin /* Better detectio for lightweight/heavyweight popup which
 * makes improves use in BeanBox /*
 */
