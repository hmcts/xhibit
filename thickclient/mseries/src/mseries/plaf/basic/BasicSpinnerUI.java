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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;

import mseries.ui.ArrowButton;
import mseries.ui.MSpinner;
import mseries.utils.MSpinnerLayout;

public class BasicSpinnerUI extends ComponentUI implements PropertyChangeListener {
    private static final int UP = 1;

    private static final int DOWN = -1;

    private AbstractAction upAction = new UpDownAction(UP, "up");

    private AbstractAction downAction = new UpDownAction(DOWN, "down");

    protected JButton up;

    protected JButton down;

    boolean isShowing = false;

    boolean mustClose = false;

    ActionListener activator;

    JTextField display;

    Border border = null;

    protected MSpinner field;

    Dimension d;

    /**
     * This method is called by the UIManager to get an instance of this class
     * and must be overridden in subclasses.
     */
    public static ComponentUI createUI(JComponent x) {
        return new BasicSpinnerUI();
    }

    /*
     * Called by the UIManager to install the UI of the component
     */
    public void installUI(JComponent c) {
        field = (MSpinner) c;

        installComponents();
        configureDisplay(field, display, up, down);
        field.setLayout(createLayoutManager());

        installListeners();
    }

    public void uninstallUI(JComponent c) {
        field.setLayout(null);
        uninstallListeners();
        uninstallComponents();
    }

    public void update(Graphics g, JComponent c) {
        paint(g, c);
    }

    protected void installListeners() {
        field.addPropertyChangeListener(this);
        up.addActionListener(upAction);
        down.addActionListener(downAction);
        registerKeyboardActions();

    }

    protected void uninstallListeners() {
        field.removePropertyChangeListener(this);
        up.removeActionListener(upAction);
        down.removeActionListener(downAction);
        unRegisterKeyboardActions();
    }

    /**
     * Creates the standard combo box layout manager that has the arrow button
     * to the right and the editor to the left. Returns an instance of
     * BasicComboBoxUI$ComboBoxLayoutManager.
     */
    protected LayoutManager createLayoutManager() {
        return new MSpinnerLayout();
    }

    /**
     * The editor and arrow buttone are added to the spinner here.
     */
    protected void installComponents() {
        display = field.getTextField();
        field.add(display);

        up = createArrowButton(BasicArrowButton.NORTH);
        field.add(up);

        down = createArrowButton(BasicArrowButton.SOUTH);
        field.add(down);
    }

    protected void uninstallComponents() {
        up = null;
        down = null;
        field.removeAll();
    }

    /**
     * Creates the arrow button. Subclasses can create any button they like. The
     * default behavior of this class is to attach various listeners to the
     * button returned by this method. Returns an instance of BasicArrowButton.
     * 
     * @param dir
     *            the dirction, one of the BasicArrowButton constants
     */
    protected JButton createArrowButton(int dir) {
        JButton x = new ArrowButton(dir);
        x.setBackground(field.getBackground());
        x.setForeground(field.getForeground());
        return x;
    }

    /**
     * This is where we would configure the display field part of the component,
     * such as remove the dfault border and change preferred size.
     * 
     * @param display
     *            the display part of the component
     */
    public void configureDisplay(MSpinner field, JComponent display, JButton up, JButton down) {
        field.getTextField().setBorder(null);
    }

    /**
     * Class to do the increment/decrement when the buttons are pressed
     */
    protected class UpDownAction extends AbstractAction {
        int direction; // +1 = up; -1 = down

        public UpDownAction(int direction, String name) {
            super(name);

            this.direction = direction;
        }

        public void actionPerformed(ActionEvent evt) {

            if (direction == UP) {
                // setValue(getNextValue());
                field.getNextValue();
                // field.setValue(val);
            } else {
                // setValue(getPreviousValue());
                field.getPreviousValue();
                // field.setValue(val);

            }

            display.requestFocus();
        }
    }

    /**
     * Associates the Up Arrow and Down Arrow keys with incrment and decrement
     * of the value
     */
    protected void registerKeyboardActions() {
        display.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UP");
        display.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DOWN");
        display.getActionMap().put("UP", upAction);
        display.getActionMap().put("DOWN", downAction);
    }

    protected void unRegisterKeyboardActions() {
        /*
         * display.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
         * 0));
         * display.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
         * 0));
         */
    }

    // private Border createBorder()
    // {
    // if (border == null)
    // {
    // Border innerBorder = BorderFactory.createEmptyBorder(2,3,0,3);
    // Border outerBorder = BorderFactory.createLineBorder(Color.black);
    // return BorderFactory.createCompoundBorder(outerBorder, innerBorder);
    // }
    // return border;
    //
    // }

    public void propertyChange(PropertyChangeEvent evt) {
        String event = evt.getPropertyName();
        if (event.equals("enabled")) {
            Object o = evt.getNewValue();
            boolean enabled = ((Boolean) o).booleanValue();
            display.setEnabled(enabled);
            up.setEnabled(enabled);
            down.setEnabled(enabled);
        }
        if (event.equals("background")) {
            Object o = evt.getNewValue();
            Color c = (Color) o;
            up.setBackground(c);
            down.setBackground(c);
        }
        if (event.equals("foreground")) {
            Object o = evt.getNewValue();
            Color c = (Color) o;
            up.setForeground(c);
            down.setForeground(c);
        }
    }
}
// $Log: BasicSpinnerUI.java,v $
// Revision 1.3  2006/06/05 12:31:52  bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting tab fix
//
// Revision 1.2 2006/05/31 14:26:07 bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting
//
// Revision 1.1 2004/04/02 15:44:04 sz0t7n
// Adding mseries to the build so we can bug fix it from now on
//
// Revision 1.4 2002/12/21 22:53:16 martin
// *** empty log message ***
//
// Revision 1.3 2002/02/27 22:03:33 martin
// Replaced obsolete method JComponent.registerKeyboardAction with JDK1.3.1
// versions
//
