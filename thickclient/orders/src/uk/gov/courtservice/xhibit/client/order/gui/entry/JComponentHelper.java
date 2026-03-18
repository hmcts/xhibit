package uk.gov.courtservice.xhibit.client.order.gui.entry;

import java.awt.Component;

import javax.swing.JTextArea;
import javax.swing.JTextField;

//!!!!!!!!!!Helper class no longer needed...Can be deleted...

public class JComponentHelper {

    /**
     * 
     * @param comp
     * @return
     */
    private static int calculateAreaSize(Component comp) {
        return (((JTextArea) comp).getText().length());
    }

    /**
     * 
     * @param comp
     * @return
     */
    private static int calculateFieldSize(Component comp) {
        return (((JTextField) comp).getText().length());
    }

    /**
     * 
     * @param comp
     * @param size
     */
    public static void resizeComponents(Object comp, int size) {
        if (comp instanceof JTextArea) {
            ((JTextArea) comp).setColumns(size);
        } else if (comp instanceof JTextField) {
            ((JTextField) comp).setColumns(size);
        }
    }

    /**
     * 
     * @param comp
     * @return
     */
    public int getPreferredSize(Component comp) {
        int size = 0;
        if (comp instanceof JTextArea) {
            size = calculateAreaSize(comp);
            return size;
        } else if (comp instanceof JTextField) {
            size = calculateFieldSize(comp);
            return size;
        } else {
            return 10;
        }
    }

}