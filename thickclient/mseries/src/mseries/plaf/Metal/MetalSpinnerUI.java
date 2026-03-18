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
package mseries.plaf.Metal;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

import mseries.ui.MSpinner;

public class MetalSpinnerUI extends mseries.plaf.basic.BasicSpinnerUI {

    /**
     * This method is called by the UIManager to get an instance of this class
     * and must be overridden in subclasses.
     */
    public static ComponentUI createUI(JComponent x) {
        return new MetalSpinnerUI();
    }

    public void configureDisplay(MSpinner field, JComponent display, JButton up, JButton down) {
        field.getTextField().setBorder(null);
        field.getTextField().setPreferredSize(new Dimension(75, 22));
        field.setBorder(BorderFactory.createEtchedBorder(MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel
                .getControlDarkShadow()));
    }

    protected JButton createArrowButton(int dir) {
        MetalArrowButton b = new MetalArrowButton(dir);
        if (dir == SwingConstants.NORTH) {
            b.drawBorder(false, true, false, false);
        }
        if (dir == SwingConstants.SOUTH) {
            b.drawBorder(false, true, false, false);
        }
        return b;
    }
}
