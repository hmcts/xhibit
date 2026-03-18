/*
 *   Copyright (c) 2002 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
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
package mseries.ui;

import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * An arrow button with no border, until the mouse rolls over when the L&F
 * border is drawn
 */
public class RollOverButton extends ArrowButton {
    int direction;

    public RollOverButton(int dir) {
        super(dir);
        setRolloverEnabled(true);
    }

    protected void paintBorder(Graphics g) {
        setBorderPainted(getModel().isRollover());
        super.paintBorder(g);
    }

    public static void main(String[] argv) {
        JFrame f = new JFrame("Test");
        final RollOverButton c = new RollOverButton(SwingConstants.WEST);

        /*
         * c.setBground(Color.red); c.setFground(Color.green);
         * c.addMChangeListener(new MChangeListener(){ public void
         * valueChanged(MChangeEvent e) { System.out.println(c.getValue()); }
         * });
         */

        f.getContentPane().setLayout(new FlowLayout());
        f.getContentPane().add(new JLabel("Hello"));
        f.getContentPane().add(c);

        f.pack();
        f.show();
    }
}

// $Log: RollOverButton.java,v $
// Revision 1.3  2006/06/05 12:31:54  bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting tab fix
//
// Revision 1.2 2006/05/31 14:26:09 bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting
//
// Revision 1.1 2004/04/02 15:44:08 sz0t7n
// Adding mseries to the build so we can bug fix it from now on
//
// Revision 1.4 2002/12/21 22:53:16 martin
// *** empty log message ***
//
// Revision 1.3 2002/07/21 16:25:20 martin
// no message
//
// Revision 1.2 2002/06/18 21:32:55 martin
// no message
//
// Revision 1.1 2002/06/16 21:46:43 martin
// new file
//
