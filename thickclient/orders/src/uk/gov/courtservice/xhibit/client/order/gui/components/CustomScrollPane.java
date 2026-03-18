package uk.gov.courtservice.xhibit.client.order.gui.components;

import java.awt.Component;

import javax.swing.JScrollPane;

/**
 * <p>
 * Title: CustomScrollPane
 * </p>
 * <p>
 * Description: A custom scroll pane used to provide a scrollable view of any
 * component to be displayed by the Xhibit2 GUI. 4 constructors are available
 * allowing optional specification of horizontal and/or vertical scroll bars.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David
 * @version 1.0
 */

public class CustomScrollPane extends JScrollPane {
    // Component to which a scrollable view should be implemented.
    private Component scrollingComponent;

    /**
     * Constructs an empty (no viewport view) CustomScrollPane where both
     * horizontal and vertical scrollbars appear when needed.
     */
    public CustomScrollPane() {
    }

    /**
     * Creates a CustomScrollPane that displays the view component in a viewport
     * whose view position can be controlled with a pair of scrollbars.
     * 
     * @param view
     *            the component to display in the scrollpane's viewport
     * @param vsbPolicy
     *            an integer that specifies the vertical scrollbar policy
     * @param hsbPolicy
     *            an integer that specifies the horizontal scrollbar policy
     */
    public CustomScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        scrollingComponent = view;
    }

    /**
     * Creates a CustomScrollPane that displays the contents of the specified
     * component, where both horizontal and vertical scrollbars appear whenever
     * the component's contents are larger than the view.
     * 
     * @param view
     *            the component to display in the scrollpane's viewport.
     */
    public CustomScrollPane(Component view) {
        super(view);
        scrollingComponent = view;
    }

    /**
     * Creates an empty (no viewport view) CustomScrollPane with specified
     * scrollbar policies.
     * 
     * @param vsbPolicy
     *            an integer that specifies the vertical scrollbar policy
     * @param hsbPolicy
     *            an integer that specifies the horizontal scrollbar policy
     */
    public CustomScrollPane(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
    }

    /**
     * Sets whether or not this component is enabled. Also iterates through
     * child components and sets their enabled property.
     * 
     * @param enabled
     *            true to enable component.
     */
    public void setEnabled(boolean enabled) {
        scrollingComponent.setEnabled(enabled);
        super.setEnabled(enabled);
        Component[] components = getComponents();
        int max = components.length;
        for (int i = 0; i < max; i++) {
            components[i].setEnabled(enabled);
        }

    }
}
