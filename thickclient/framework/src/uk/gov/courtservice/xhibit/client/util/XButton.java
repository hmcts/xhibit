package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Button which adds additional functionality to basic button
 * 
 * @author William Fardell
 * @version $Revision: 1.3 $
 */
public class XButton extends JButton {

    /**
     * The size can be fixed at a given point to stop button resizing if you
     * change text etc.
     */
    private Dimension fixedSize;

    /**
     * Construct a new XButton
     */
    public XButton() {
        super();
    }

    /**
     * Construct a new XButton for the specified action
     */
    public XButton(Action action) {
        super(action);
    }

    /**
     * Construct a new XButton with the specified text
     */
    public XButton(String text) {
        super(text);
    }

    /**
     * Construct a new XButton with the specified text and icon
     */
    public XButton(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * Free the button to automatically resize when its text or icon changes
     */
    public void unfixSize() {
        this.fixedSize = null;
    }

    /**
     * Fix the button at its current size, it wont resize when its text or icon
     * changes
     */
    public void fixSize() {
        fixSize(super.getPreferredSize());
    }

    /**
     * Fix the button to the specified size it wont resize when its text or icon
     * changes
     */
    public void fixSize(Dimension fixedSize) {
        this.fixedSize = fixedSize;
    }

    /**
     * Overriden to fix the size
     */
    public Dimension getPreferredSize() {
        return fixedSize == null ? super.getPreferredSize() : fixedSize;
    }

    /**
     * Overriden to fix the size
     */
    public Dimension getMinimumSize() {
        return fixedSize == null ? super.getMinimumSize() : fixedSize;
    }

    /**
     * Overriden to fix the size
     */
    public Dimension getMaximumSize() {
        return fixedSize == null ? super.getMaximumSize() : fixedSize;
    }

}
