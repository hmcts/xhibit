/*
 *   Copyright (c) 2000 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
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
package mseries.Calendar;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;

public class TestIt {
    public static void main(String[] argv) {
        // Locale.setDefault(new Locale("fr", "FR"));
        JFrame frame = new JFrame("MDateSelectorPanel Test");

        frame.getContentPane().setLayout(new FlowLayout());

        MDateSelectorPanel panel = new MDateSelectorPanel();

        MDateDisplay display = new MDateDisplay(20);
        panel.addMMonthListener(display);

        // panel.setBackground(Color.green);
        panel.setImageFile("d:\\temp\\fond.jpg");
        panel.setFirstDay(java.util.Calendar.SATURDAY);
        // panel.setDayBackground(Color.blue, java.util.Calendar.SATURDAY);
        // panel.setDayBackground(Color.blue, java.util.Calendar.SUNDAY);
        panel.setForeground(java.util.Calendar.SUNDAY, Color.blue);
        panel.setForeground(java.util.Calendar.SATURDAY, Color.blue);

        // panel.setEnabled(false);

        frame.getContentPane().add(panel);
        frame.getContentPane().add(display);

        frame.pack();
        frame.show();
    }

}
