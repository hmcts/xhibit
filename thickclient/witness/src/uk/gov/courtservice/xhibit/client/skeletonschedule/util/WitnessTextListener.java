/**
 * Created by IntelliJ IDEA.
 * <p>Title: IMDocumentListener</p>
 * <p>Description: Listens for text events on a JTextField Document and enables/disables a button
 * if the length of the text is > 0</p>
 * User: qzd3k3
 * Date: May 19, 2003
 * Time: 5:07:20 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.skeletonschedule.util;

import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class WitnessTextListener implements DocumentListener {

    private JButton button;

    public WitnessTextListener(JButton btn) {
        button = btn;
    }

    public void changedUpdate(DocumentEvent e) {
        // Enable the button if the text field has text entered
        button.setEnabled(e.getDocument().getLength() > 0);
    }

    public void removeUpdate(DocumentEvent e) {
        // Enable the button if the text field has text entered
        button.setEnabled(e.getDocument().getLength() > 0);
    }

    public void insertUpdate(DocumentEvent e) {
        // Enable the button if the text field has text entered
        button.setEnabled(e.getDocument().getLength() > 0);
    }
}