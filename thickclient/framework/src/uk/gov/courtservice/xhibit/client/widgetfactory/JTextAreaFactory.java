package uk.gov.courtservice.xhibit.client.widgetfactory;

import java.awt.Dimension;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;
import javax.swing.text.Document;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * @author Meeraj
 * @title JTextFactory This is a factory class for JTextField objects
 */
public class JTextAreaFactory {
    // -----------------------------------------------------------------------------
    /**
     * Don't instantiate me
     */
    private JTextAreaFactory() {
    }

    // -----------------------------------------------------------------------------
    /**
     * Creates a text field with the following properties
     * 
     * @param Tooltip
     *            text
     * @param Key
     *            listener
     * @param Number
     *            of columns
     * @param Minimum
     *            size
     * @param Maximum
     *            size
     * @param Preferred
     *            size
     * @param Is
     *            editable
     * @param Text
     * @param Backing
     *            document
     * @return
     */
    public static JTextArea getTextArea(String tooltipText, KeyListener keyListener, Dimension minimumSize,
            Dimension maximumSize, Dimension preferredSize, boolean editable, String text, Document doc) {

        // Create the text field
        JTextArea field = new JTextArea();
        // Set the tooltip
        if (tooltipText != null)
            field.setToolTipText(tooltipText);
        // Add the key listener
        if (keyListener != null)
            field.addKeyListener(keyListener);
        // Set the minimum size
        if (minimumSize != null)
            field.setMinimumSize(minimumSize);
        // Set the maximum size
        if (maximumSize != null)
            field.setMaximumSize(maximumSize);
        // Set the preferred size
        if (preferredSize != null)
            field.setPreferredSize(preferredSize);
        // Set editable
        field.setEditable(editable);
        // Set the text
        if (text != null)
            field.setText(text);
        // Set the document
        if (doc != null)
            field.setDocument(doc);

        field.setWrapStyleWord(true);
        field.setLineWrap(true);
        field.setFont(XHIBITConstant.getCurrentFont());

        return field;
    }

    // -----------------------------------------------------------------------------
    /**
     * Creates an editable text field with the default properties
     */
    public static JTextArea getTextArea() {

        return getTextArea(null, // Tooltip
                null, // Key listener
                null, // Minimum size
                null, // Maximum size
                null, // Preferred size
                true, // Editable
                null, // Text
                null); // Document

    }

    // -----------------------------------------------------------------------------
    /**
     * Creates an editable text field with the backing document
     * 
     * @param Document
     */
    public static JTextArea getTextArea(Document doc) {

        return getTextArea(null, // Tooltip
                null, // Key listener
                null, // Minimum size
                null, // Maximum size
                null, // Preferred size
                true, // Editable
                null, // Text
                doc); // Document

    }

    // -----------------------------------------------------------------------------
    /**
     * Creates a text field with the following properties
     * 
     * @param Key
     *            listener
     * @param Number
     *            of columns
     * @param Minimum
     *            size
     * @param Maximum
     *            size
     * @param Preferred
     *            size
     * @param Is
     *            editable
     * @return
     */
    public static JTextArea getTextArea(KeyListener keyListener, Dimension minimumSize, Dimension maximumSize,
            Dimension preferredSize, boolean editable) {

        return getTextArea(null, // Tooltip
                keyListener, // Key listener
                minimumSize, // Minimum size
                maximumSize, // Maximum size
                preferredSize, // Preferred size
                editable, // Editable
                null, // Text
                null); // Document

    }
    // -----------------------------------------------------------------------------
}