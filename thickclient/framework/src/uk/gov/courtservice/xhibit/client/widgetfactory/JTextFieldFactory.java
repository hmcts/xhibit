package uk.gov.courtservice.xhibit.client.widgetfactory;

import java.awt.Dimension;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * @author Meeraj
 * @title JTextFactory This is a factory class for JTextField objects
 */
public class JTextFieldFactory {
    // -----------------------------------------------------------------------------
    /**
     * Don't instantiate me
     */
    private JTextFieldFactory() {
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
    public static JTextField getTextField(String tooltipText, KeyListener keyListener, int cols, Dimension minimumSize,
            Dimension maximumSize, Dimension preferredSize, boolean editable, String text, Document doc) {

        // Create the text field
        JTextField field = new JTextField();
        // Set the tooltip
        if (tooltipText != null)
            field.setToolTipText(tooltipText);
        // Add the key listener
        if (keyListener != null)
            field.addKeyListener(keyListener);
        // Set the number of columns
        if (cols > 0)
            field.setColumns(cols);
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

        return field;
    }

    // -----------------------------------------------------------------------------
    /**
     * Creates an editable text field with the default properties
     */
    public static JTextField getTextField() {

        return getTextField(null, // Tooltip
                null, // Key listener
                -1, // No of columns
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
    public static JTextField getTextField(Document doc) {

        return getTextField(null, // Tooltip
                null, // Key listener
                -1, // No of columns
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
    public static JTextField getTextField(KeyListener keyListener, int cols, Dimension minimumSize,
            Dimension maximumSize, Dimension preferredSize, boolean editable) {

        return getTextField(null, // Tooltip
                keyListener, // Key listener
                cols, // No of columns
                minimumSize, // Minimum size
                maximumSize, // Maximum size
                preferredSize, // Preferred size
                editable, // Editable
                null, // Text
                null); // Document

    }
    // -----------------------------------------------------------------------------
}