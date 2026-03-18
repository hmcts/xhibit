package uk.gov.courtservice.xhibit.client.widgetfactory;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.border.Border;

import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * @author Meeraj
 * @title JButtonFactory This class provides factory methods for creating
 *        JButtons
 */
public class JButtonFactory {
    // -----------------------------------------------------------------------------
    /**
     * Don't instantiate me
     */
    private JButtonFactory() {
    }

    // -----------------------------------------------------------------------------
    /**
     * Creates a button that will inherit the properties from the action
     * 
     * @param Action
     *            for the button
     * @param Border
     * @param Minimum
     *            size
     * @param Maximum
     *            size
     * @param Preferred
     *            size
     * @return New JButton instance
     */
    public static JButton getButton(XAction action, Border border, Dimension minimumSize, Dimension maximumSize,
            Dimension preferredSize) {

        if (action == null)
            throw new IllegalArgumentException("action");

        // Button will inherit the properties from the action
        JButton button = new JButton();
        button.setAction(action);
        if (action.getMnemonicKey() != null) {
            button.setMnemonic(action.getMnemonicKey().intValue());
        }

        // Request the minimum size
        if (minimumSize != null)
            button.setMinimumSize(minimumSize);
        // Request the maximum size
        if (maximumSize != null)
            button.setMaximumSize(maximumSize);
        // Request the maximum size
        if (preferredSize != null)
            button.setPreferredSize(preferredSize);

        return button;
    }

    // -----------------------------------------------------------------------------
    /**
     * Creates a button that will inherit the properties from the action
     * 
     * @param Action
     *            for the button
     * @return New JButton instance
     */
    public static JButton getButton(XAction action) {

        return getButton(action, // Action
                null, // Border
                null, // Minimum size
                null, // Maximum size
                null); // Preferred size

    }
    // -----------------------------------------------------------------------------
}
