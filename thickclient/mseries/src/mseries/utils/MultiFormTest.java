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
package mseries.utils;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MultiFormTest {

    public static void main(String[] argv) {

        JFrame f = new JFrame("MultiForm Test");

        f.getContentPane().setLayout(new MultiFormLayout(f.getContentPane()));

        MultiFormTest gbt = new MultiFormTest();

        f.getContentPane().add(gbt.getPanel1());
        f.getContentPane().add(gbt.getPanel2());

        f.pack();
        f.show();
    }

    public MultiFormTest() {

    }

    public JPanel getPanel1() {

        JPanel p = new JPanel();
        p.setLayout(new FormLayout());

        FormConstraints c = new FormConstraints();

        // Row 1
        JLabel l1 = new JLabel("Username");

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = FormConstraints.EAST;
        p.add(l1, c);

        JTextField t1 = new JTextField(20);

        c.gridx = FormConstraints.RELATIVE;
        c.anchor = FormConstraints.WEST;
        p.add(t1, c);

        // Row 2
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = FormConstraints.EAST;

        JLabel l2 = new JLabel("Password");

        p.add(l2, c);

        // JTextField t2= new JTextField(20);
        JPanel ip = makeUOMPanel();

        c.gridx = FormConstraints.RELATIVE;
        c.anchor = FormConstraints.WEST;
        c.spansColumns = true;
        p.add(ip, c);

        p.setBorder(BorderFactory.createTitledBorder("Top Panel"));
        return p;
    }

    public JPanel makeUOMPanel() {

        JPanel p = new JPanel();
        p.setLayout(new FormCellLayout());

        FormConstraints c = new FormConstraints();

        JTextField t1 = new JTextField(5);

        c.anchor = FormConstraints.WEST;
        p.add(t1, c);

        c.anchor = FormConstraints.EAST;

        JLabel l2 = new JLabel("Printer");

        p.add(l2, c);

        JTextField t2 = new JTextField(5);

        c.anchor = FormConstraints.WEST;
        p.add(t2, c);

        return p;
    }

    public JPanel getPanel2() {

        JPanel p = new JPanel();
        p.setLayout(new FormLayout());

        FormConstraints c = new FormConstraints();

        // Row 1
        JLabel l1 = new JLabel("Locale");

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = FormConstraints.EAST;
        p.add(l1, c);

        JTextField t1 = new JTextField(20);

        c.gridx = FormConstraints.RELATIVE;
        c.anchor = FormConstraints.WEST;
        p.add(t1, c);

        // Row 2
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = FormConstraints.EAST;

        JLabel l2 = new JLabel("Printer");

        p.add(l2, c);

        c.gridx = FormConstraints.RELATIVE;
        JTextField t2 = new JTextField(10);

        c.anchor = FormConstraints.WEST;
        c.fill = true;
        p.add(t2, c);
        c.fill = false;

        c.anchor = FormConstraints.EAST;

        JLabel l3 = new JLabel("Location");

        p.add(l3, c);

        JTextField t3 = new JTextField(10);

        c.anchor = FormConstraints.WEST;
        p.add(t3, c);
        p.setBorder(BorderFactory.createTitledBorder("Bottom Panel"));
        return p;
    }

}
