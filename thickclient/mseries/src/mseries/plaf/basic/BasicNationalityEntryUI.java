
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

import mseries.nationality.MNationalitySelectorPanel;
import mseries.nationality.MNationalityEvent;
import mseries.nationality.MNationalityListener;
import mseries.ui.ArrowButton;
import mseries.ui.MChangeEvent;
import mseries.ui.MNationalityEntryField;
import mseries.ui.MNationalityField;
import mseries.ui.MPopup;
import mseries.ui.MPopupPanel;
import mseries.ui.MPopupWindow;
import mseries.ui.ScreenUtilities;
import mseries.utils.MComboBoxLayout;


/*
 * User interface for the nationality selector.
 * Modelled on BasicDateNationalityUI.
 */
public class BasicNationalityEntryUI extends ComponentUI implements PropertyChangeListener {
    // The arrow button that invokes the popup.
    protected JButton arrowButton;

    boolean isShowing = false;

    Action activator;
    Action deactivator;

    MNationalityField display;

    MNationalitySelectorPanel panel;

    MPopup popup;

    Border border = null;

    MNationalityListener mNationalityListener;

    AncestorListener ancListener;

    protected MNationalityEntryField nationalityEntry;

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
        return new BasicNationalityEntryUI();
    }

    /*
     * Called by the UIManager to install the UI of the component
     */
    public void installUI(JComponent c) {
        nationalityEntry = (MNationalityEntryField) c;

        installComponents();

        configureDisplay(nationalityEntry.getDisplay());
        configureBorder(nationalityEntry);

        nationalityEntry.setLayout(createLayoutManager());

        installListeners();
    }

    public void uninstallUI(JComponent c) {
        nationalityEntry.setLayout(null);
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
            nationalityEntry.addAncestorListener(ancListener);
        }
        registerKeyboardActions();
        mNationalityListener = createMNationalityListener();

        nationalityEntry.addPropertyChangeListener(this);
    }

    protected void uninstallListeners() {
        arrowButton.removeActionListener(activator);
        nationalityEntry.addPropertyChangeListener(this);
        unRegisterKeyboardActions();
        nationalityEntry.removeAncestorListener(ancListener);
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
        display = nationalityEntry.getDisplay();
        nationalityEntry.add(display);

        arrowButton = createArrowButton();
        nationalityEntry.add(arrowButton);
    }

    protected void uninstallComponents() {
        arrowButton = null;
        nationalityEntry.removeAll();
    }

    /**
     * Creates the arrow button. Subclasses can create any button they like. The
     * default behavior of this class is to attach various listeners to the
     * button returned by this method. Returns an instance of BasicArrowButton.
     */
    protected JButton createArrowButton() {
        JButton x = new ArrowButton(ArrowButton.SOUTH);
        x.setBackground(nationalityEntry.getBackground());
        x.setForeground(nationalityEntry.getBackground());
        return x;
    }

    /**
     * Gets the insets from the JComboBox.
     */
    protected Insets getInsets() {
        return nationalityEntry.getInsets();
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
        // empty
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
        
        // Unlike the BasicDateEntryUI.java we create a fresh panel for each popup.
        // Otherwise a return key press while browsing the nationalities would leave
        // the list with a different item selected to the choosen item.  Could not
        // find a way around that.
        String nationality = display.getValue();
        panel = createMNationalitySelectorPanel(nationality);
        
        if (popup == null) {
            nationalityEntry.notifyListeners(new FocusEvent(nationalityEntry, FocusEvent.FOCUS_GAINED));
            /*
             * Remove the focus listener so that when the pull down receives the
             * focus, the focusLost event is not caught
             */
            nationalityEntry.opened();
            Point p = nationalityEntry.getLocationOnScreen();

            panel.setPullDownConstraints(nationalityEntry.getConstraints());
            panel.setBorder(createBorder());
            d = panel.getPreferredSize();

            if (checkLightPosition(nationalityEntry, p)) {
                popup = createLightWeightPopup();
            } else {
                checkHeavyPosition(nationalityEntry, p);
                popup = createHeavyWeightPopup();
            }

            /* This is where the MNationalityPanel is configured */
            panel.addMNationalityListener(mNationalityListener);

            popup.setShadow(nationalityEntry.getConstraints().hasShadow());
            popup.addComponent(panel, BorderLayout.CENTER);
            popup.pack();
            popup.setLocationOnScreen(p.x, p.y);
            popup.setParent(display);
            popup.setVisible(true);

            popup.requestFocus();
            nationalityEntry.notifyListeners(MChangeEvent.PULLDOWN_OPENED);

        }
    }

    private void destroyPopup() {
        if (popup != null) {
            panel.removeMNationalityListener(mNationalityListener);
            popup.setVisible(false);
            nationalityEntry.notifyListeners(MChangeEvent.PULLDOWN_CLOSED);
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

    protected MNationalitySelectorPanel createMNationalitySelectorPanel(String nationality) {
        MNationalitySelectorPanel panel;

        panel = new MNationalitySelectorPanel();
        panel.setFocusCycleRoot(true);

        panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.ALT_MASK), "CLOSE");
        panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "CLOSE");
        panel.getActionMap().put("CLOSE", deactivator);

        panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CANCEL");
        panel.getActionMap().put("CANCEL", deactivator);

        panel.setNationality(nationality);
        
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
     * Manufactures a border for the popup nationality selector. Subclasses should return a
     * zero width empty border if no border is required. 
     * @return a border for the popup natinality selector
     */
    private Border createBorder() {
        if (border == null) {
            Border innerBorder = BorderFactory.createEmptyBorder(2, 3, 0, 3);
            Border outerBorder = BorderFactory.createLineBorder(Color.black);
            return BorderFactory.createCompoundBorder(outerBorder, innerBorder);
        }
        return border;

    }

    protected MNationalityListener createMNationalityListener() {
        return new MNationalityListener() {

            public void dataChanged(MNationalityEvent e) {
                int type = e.getType();
                if (type == MNationalityEvent.EXITED) {
                    destroyPopup();
                    nationalityEntry.closed();
                    SwingUtilities.invokeLater(testFocus);
                }
                if (type == MNationalityEvent.NEW_NATIONALITY) {
                    String nationality = e.getNewNationality();
                    display.setValue(nationality);
                    destroyPopup();
                    display.requestFocus();
                    nationalityEntry.closed();
                    nationalityEntry.notifyListeners(MChangeEvent.CHANGE);
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
                nationalityEntry.notifyListeners(new FocusEvent(nationalityEntry, FocusEvent.FOCUS_LOST));
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
            arrowButton.setEnabled(enabled);
            if (enabled) {
                registerKeyboardActions();
            } else {
                unRegisterKeyboardActions();
            }
        }
    }
}
