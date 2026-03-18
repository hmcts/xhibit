package uk.gov.courtservice.xhibit.client.order.gui.general;

import java.awt.BorderLayout;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.misc.OrderStatus;
import uk.gov.courtservice.xhibit.client.order.gui.entry.DataEntryPanel;

/**
 * Top level container utilising a JSplitPane to divide the order data into two
 * sections. Left hand section allows user to edit order data which is reflected
 * on the right hand pane. The two sections can then be interactively resized.
 * 
 * @author Neil Ellis & Neil Entwistle
 */

public class OrderMainPane extends JSplitPane implements PropertyChangeListener {
    private static final Logger log = CSServices.getLogger(OrderMainPane.class);

    // Starting x position for viewport in JScrollPane
    private static final int X_START_POS = 0;

    // Starting y position for viewport in JScrollPane
    private static final int Y_START_POS = 0;

    private JScrollPane rhsScrollPane;

    private JScrollPane lhsScrollPane;

    private OrderStatus status;

    private DataEntryPanel test;

    /**
     * Constructs the OrderMainPane with the left hand section populated by the
     * DataEntryPanel and the right hand section by the OrderPreviewPane.
     * 
     * @param dataEntryPanel
     *            panel containing tree structure of all order components.
     * @param orderPreviewPane
     *            pane capable of displaying text in various formats.
     * @param status
     *            the order status
     */
    public OrderMainPane(DataEntryPanel dataEntryPanel, JComponent orderPreviewPane, OrderStatus status) {
        this.setStatus(status);
        this.status.addPropertyChangeListener(this);
        setLookAndFeel();
        createSplitPane(dataEntryPanel, orderPreviewPane);
    }

    /**
     * Sets the order status
     * 
     * @param status
     *            the status
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * Returns the order status
     * 
     * @return the status
     */
    public OrderStatus getStatus() {
        return this.status;
    }

    /**
     * Creates the JSplitPane with vertical scrollbars and a resizable divide
     * between the data panels.
     * 
     * @param dataEntryPanel
     *            panel containing tree structure of all order components.
     * @param orderPreviewPane
     *            pane capable of displaying text in various formats.
     */
    private void createSplitPane(DataEntryPanel dataEntryPanel, final JComponent orderPreviewPane) {
        test = dataEntryPanel;

        JPanel base;
        switch (status.getStatus()) {
        case OrderStatus.SIGNED:
            createComponents(orderPreviewPane);
            setDividerProperties(false, 0, 0);
            break;
        case OrderStatus.SENT:
            createComponents(orderPreviewPane);
            setDividerProperties(false, 0, 0);
            break;
        default:
            base = setUpDataEntryPanel(dataEntryPanel);
            createComponents(base, orderPreviewPane);
            setDividerProperties(true, 375, 10);
            break;
        }
    }

    /**
     * Sets the split pane divider properties
     * 
     * @param expandable
     *            true if expandable
     * @param location
     *            where on the screen the divider is positioned
     * @param size
     *            the size of the divider
     */
    private void setDividerProperties(boolean expandable, int location, int size) {
        this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.setOneTouchExpandable(expandable);
        this.setLeftComponent(lhsScrollPane);
        this.setRightComponent(rhsScrollPane);
        this.setDividerLocation(location);
        this.setDividerSize(size);
    }

    /**
     * Sets the data entry panel
     * 
     * @param dataEntryPanel
     *            the data entry panel
     * @return the man panel
     */
    private JPanel setUpDataEntryPanel(DataEntryPanel dataEntryPanel) {
        JPanel pPanel = new JPanel();
        pPanel.setLayout(new BorderLayout());
        JPanel basePanel = new JPanel();
        basePanel.setLayout(new BorderLayout());
        pPanel.add(dataEntryPanel, BorderLayout.NORTH);
        basePanel.add(pPanel, BorderLayout.WEST);
        return basePanel;
    }

    /**
     * Creates the components to add to the main panel
     * 
     * @param base
     *            the base panel
     * @param orderPreviewPane
     *            the preview pane
     */
    private void createComponents(JPanel base, final JComponent orderPreviewPane) {
        lhsScrollPane = new JScrollPane(base, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // Neil Entwistle - enforce scrolling movement
        lhsScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        rhsScrollPane = new JScrollPane(orderPreviewPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // Neil Entwistle - enforce scrolling movement
        rhsScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        // Set/reset the viewport when the order is opened
        rhsScrollPane.getViewport().setViewPosition(new Point(X_START_POS, Y_START_POS));
    }

    /**
     * Creates the components to add to the main panel
     * 
     * @param orderPreviewPane
     *            the preview pane
     */
    private void createComponents(final JComponent orderPreviewPane) {
        rhsScrollPane = new JScrollPane(orderPreviewPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * Positions he divider depending on the status of the order
     * 
     * @param event
     */
    public void propertyChange(PropertyChangeEvent event) {
        switch (status.getStatus()) {
        case OrderStatus.SIGNED:
            setDividerProperties(false, 0, 0);
            break;
        case OrderStatus.SENT:
            setDividerProperties(false, 0, 0);
            break;
        default:
            setDividerProperties(true, 450, 10); // Des Johnston SCR52735
            break;
        }
    }

    /**
     * Sets the look and feel to match that of the main application
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logLookAndFeelError(ex);
        }
    }

    private void logLookAndFeelError(Exception ex) {
        log
                .error("OrderMainPane: setLookAndFeel: " + UIManager.getSystemLookAndFeelClassName() + " "
                        + ex.getMessage());

    }
}