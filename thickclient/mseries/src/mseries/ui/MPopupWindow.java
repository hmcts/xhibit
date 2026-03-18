/*
 *   Author Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
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
 *
 *   Reaped from javax.swing.JPopupMenu with thanks
 */
package mseries.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JWindow;
import javax.swing.border.Border;

/**
 * A class used to popup a window.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. A future release of Swing will provide support for
 * long term persistence.
 */
public class MPopupWindow extends JWindow implements MPopup, FocusListener, Serializable {
    int saveX, saveY;

    boolean firstShow = true;

    boolean isVisible = false;

    boolean hasShadow = false;

    Component firstComp = null;

    JButton a;

    public MPopupWindow(Window owner) {
        super(owner);
        addFocusListener(this);
    }

    public MPopupWindow() {
        addFocusListener(this);
    }

    public void focusGained(FocusEvent e) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_ACTIVATED));
        firstComp.requestFocus();
    }

    public void focusLost(FocusEvent e) {
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void setParent(Component invoker) {
    }

    public void setLocationOnScreen(int x, int y) {
        this.setLocation(x, y);
    }

    public void addComponent(Component aComponent, Object constraints) {
        this.getContentPane().add(aComponent, constraints);
        if (firstComp == null) {
            firstComp = aComponent;
        }
    }

    public void removeComponent(Component c) {
        this.getContentPane().remove(c);
    }

    public void setBorder(Border border) {
    }

    public int getWeight() {
        return MPopup.HEAVY;
    }

    public void setShadow(boolean hasShadow) {
    }

    /**
     * Heavy weight panels can not be opaque and therefore a shadow can not be
     * transparant.
     */
    public boolean isShadow() {
        return hasShadow;
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            toFront();
        }
    }
}
/*
 * $Log: MPopupWindow.java,v $
 * Revision 1.3  2006/06/05 12:31:54  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.2 2006/05/31 14:26:09 bzjrnl Change:
 * TI901 Comment: Weblogic Upgrade - Standadise code formatting /* Revision 1.1
 * 2004/04/02 15:44:07 sz0t7n /* Adding mseries to the build so we can bug fix
 * it from now on /* /* Revision 1.5 2002/01/30 21:45:05 martin /* Fixed Focus
 * problem /* /* Revision 1.4 2002/01/22 21:35:21 martin /* Provided new
 * constructor to pass owner /* /* Revision 1.3 2002/01/15 20:01:27 martin /*
 * Over rode setVisible(boolean) from Window /*
 */

