package uk.gov.courtservice.xhibit.client.widgetfactory;

import java.awt.Dimension;
import java.awt.event.ItemListener;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionListener;

/**
 * @author Meeraj
 * @title JListFactory A factory for combo boxes
 */
public class JListFactory {
    // -----------------------------------------------------------------------------
    /**
     * Don't instantiate me
     */
    private JListFactory() {
    }

    // -----------------------------------------------------------------------------
    /**
     * Creates a combobox with the following properties
     * 
     * @param Tooltip
     *            text
     * @param List
     *            Selecttion listener
     * @param Minimum
     *            size
     * @param Maximum
     *            size
     * @param Preferred
     *            size
     * @return Combobox
     */
    public static JList getList(String tooltipText, ListSelectionListener selectionListener, ItemListener itemListener,
            ListCellRenderer renderer, Dimension minimumSize, Dimension maximumSize, Dimension preferredSize) {

        // Create the text field
        JList field = new JList();
        // Set the tooltip
        if (tooltipText != null)
            field.setToolTipText(tooltipText);
        // Add the action listener
        if (selectionListener != null)
            field.addListSelectionListener(selectionListener);
        // Set the minimum size
        if (minimumSize != null)
            field.setMinimumSize(minimumSize);
        // Set the maximum size
        if (maximumSize != null)
            field.setMaximumSize(maximumSize);
        // Set the preferred size
        if (preferredSize != null)
            field.setPreferredSize(preferredSize);

        return field;
    }

    // -----------------------------------------------------------------------------
    /**
     * Creates an editable text field with the default properties
     */
    public static JList getList() {

        return getList(null, // Tooltip
                null, // Action listener
                null, // Minimum size
                null, // Maximum size
                null); // Preferred size

    }

    // -----------------------------------------------------------------------------
    /**
     * Creates a combobox with the following properties
     * 
     * @param Tooltip
     *            text
     * @param List
     *            Selection listener
     * @param Minimum
     *            size
     * @param Maximum
     *            size
     * @param Preferred
     *            size
     * @return Combobox
     */
    public static JList getList(String tooltipText, ListSelectionListener selectionListener, Dimension minimumSize,
            Dimension maximumSize, Dimension preferredSize) {

        return getList(tooltipText, // Tooltip
                selectionListener, // Action listener
                minimumSize, // Minimum size
                maximumSize, // Maximum size
                preferredSize); // Preferred size

    }
    // -----------------------------------------------------------------------------
}