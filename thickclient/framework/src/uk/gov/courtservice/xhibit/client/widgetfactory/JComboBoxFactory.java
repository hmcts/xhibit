package uk.gov.courtservice.xhibit.client.widgetfactory;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

/**
 * @author Meeraj
 * @title JComboBoxFactory A factory for combo boxes
 */
public class JComboBoxFactory {
    // -----------------------------------------------------------------------------
    /**
     * Don't instantiate me
     */
    private JComboBoxFactory() {
    }

    // -----------------------------------------------------------------------------
   
    
    public static JComboBox getComboBox(ComboBoxModel model, String tooltipText, ActionListener actionListener, ItemListener itemListener,
            ListCellRenderer renderer, Dimension minimumSize, Dimension maximumSize, Dimension preferredSize) {

        // Create the combo field with model
        if (model != null){
            JComboBox field = new JComboBox(model);
        
            // Set the tooltip
            if (tooltipText != null)
                field.setToolTipText(tooltipText);
            // Add the action listener
            if (actionListener != null)
                field.addActionListener(actionListener);
            // Add the item listener
            if (itemListener != null)
                field.addItemListener(itemListener);
            // Set the renderer
            if (renderer != null)
                field.setRenderer(renderer);
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
        return null;
    }
    
    
    /**
     * Creates a combobox with the following properties
     * 
     * @param Tooltip
     *            text
     * @param Action
     *            listener
     * @param Item
     *            listener
     * @param Renderer
     * @param Minimum
     *            size
     * @param Maximum
     *            size
     * @param Preferred
     *            size
     * @return Combobox
     */
    public static JComboBox getComboBox(String tooltipText, ActionListener actionListener, ItemListener itemListener,
            ListCellRenderer renderer, Dimension minimumSize, Dimension maximumSize, Dimension preferredSize) {

        // Create the text field
        JComboBox field = new JComboBox();
        // Set the tooltip
        if (tooltipText != null)
            field.setToolTipText(tooltipText);
        // Add the action listener
        if (actionListener != null)
            field.addActionListener(actionListener);
        // Add the item listener
        if (itemListener != null)
            field.addItemListener(itemListener);
        // Set the renderer
        if (renderer != null)
            field.setRenderer(renderer);
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
    public static JComboBox getComboBox() {

        return getComboBox(null, // Tooltip
                null, // Action listener
                null, // Item listener
                null, // Renderer
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
     * @param Action
     *            listener
     * @param Minimum
     *            size
     * @param Maximum
     *            size
     * @param Preferred
     *            size
     * @return Combobox
     */
    public static JComboBox getComboBox(String tooltipText, ActionListener actionListener, Dimension minimumSize,
            Dimension maximumSize, Dimension preferredSize) {

        return getComboBox(tooltipText, // Tooltip
                actionListener, // Action listener
                null, // Item listener
                null, // Renderer
                minimumSize, // Minimum size
                maximumSize, // Maximum size
                preferredSize); // Preferred size

    }
    
    /**
     * Creates a combobox with the following properties
     * @param tooltipText
     * @param actionListener
     * @param renderer
     * @param minimumSize
     * @param maximumSize
     * @param preferredSize
     * @return
     */
    public static JComboBox getComboBox(String tooltipText, ActionListener actionListener, ListCellRenderer renderer, Dimension minimumSize,
            Dimension maximumSize, Dimension preferredSize) {
        return getComboBox(tooltipText, // Tooltip
                actionListener, // Action listener
                null, // Item listener
                renderer, // Renderer
                minimumSize, // Minimum size
                maximumSize, // Maximum size
                preferredSize); // Preferred size
    }
    // -----------------------------------------------------------------------------
}