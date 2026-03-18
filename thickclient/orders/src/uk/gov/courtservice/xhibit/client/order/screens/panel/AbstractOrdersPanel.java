package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.listeners.XhibitListeners;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Xhibit2 AbstractOrdersPanel
 * </p>
 * <p>
 * Description: Abstract super class of all the orders panels. Subclass this to
 * create a new panel to add to a screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public abstract class AbstractOrdersPanel extends XPanel implements OrderPanelData {
    private static final Logger log = CSServices.getLogger(AbstractOrdersPanel.class);

    private static final Color ORDER_DISABLE_COLOR = Color.lightGray;

    /**
     * width of the text field
     */
    protected static final int TEXT_FIELD_WIDTH = 250;

    /**
     * heoght of the text field
     */
    protected static final int TEXT_FIELD_HEIGHT = 21;

    /**
     * width of the combo box
     */
    protected static final int TEXT_COMBO_FIELD_WIDTH = 500; // SCR 53190

    /**
     * height of the combo box
     */
    protected static final int COMBO_BOX_HEIGHT = 25;

    /**
     * width of the text area
     */
    protected static final int TEXT_AREA_WIDTH = 250;

    /**
     * height of the text area
     */
    protected static final int TEXT_AREA_HEIGHT = 50;

    /**
     * width of the scroll pane
     */
    protected static final int SCROLL_PANE_WIDTH = 450;

    public static final String CANCEL_CONFIRM = "Order.CancelConfirm";

    public static final String CANCEL_TITLE = "Order.CancelTitle";

    /**
     * The model
     */
    protected static OrderInitialDataVO model = null;

    /**
     * The layout
     */
    private GridBagLayout gridBag;

    /**
     * The GridBagConstraints
     */
    protected GridBagConstraints constraints;

    /**
     * The dimension of the JTextField
     */
    protected Dimension textFieldDim;

    /**
     * The dimension of the JComboBox
     */
    protected Dimension textComboField; // SCR 53190

    /**
     * The dimension of the JTextArea
     */
    protected Dimension textAreaDim;

    /**
     * The dimension of the JScrollPane
     */
    protected Dimension scrollPaneDim;

    /**
     * Constructor
     */
    public AbstractOrdersPanel() {
        super();
        XhibitListeners.setDefaultListeners(this);
    }

    /**
     * Constructor
     * 
     * @param oDM
     *            The model
     */
    public AbstractOrdersPanel(OrderInitialDataVO model) {
        this();
        this.model = model;
    }

    /**
     * Initialise the panel with the standard components
     * 
     * @throws CSRecoverableException
     */
    protected void initialisePanel() throws CSRecoverableException {
        gridBag = new GridBagLayout();
        setLayout(gridBag);
        constraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4), 0, 0);

        textFieldDim = new Dimension(TEXT_FIELD_WIDTH, XHIBITConstant.getLineHeight());
        textAreaDim = new Dimension(TEXT_AREA_WIDTH, TEXT_AREA_HEIGHT);
        textComboField = new Dimension(TEXT_COMBO_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
    }

    /**
     * Sets up the local GridBagConstraints
     * 
     * @param gridx
     *            gridx
     * @param gridy
     *            gridy
     * @param weightx
     *            weightx
     * @param weighty
     *            weighty
     */
    protected void setLocalConstraints(int gridx, int gridy, double weightx, double weighty) {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
    }

    /**
     * Add a JTextField top the panel
     * 
     * @param txtField
     *            The text field
     */
    public void add(JTextField txtField) {
        this.add(txtField, TEXT_FIELD_WIDTH);
    }

    /**
     * Add a JTextField top the panel
     * 
     * @param txtField
     *            The text field
     * @param width
     *            The width of the text field
     */
    public void add(JTextField txtField, int width) {
        Dimension dim = new Dimension(width, XHIBITConstant.getLineHeight());
        super.add(txtField, constraints);
        txtField.setEditable(false);
        txtField.setMaximumSize(dim);
        txtField.setMinimumSize(dim);
        txtField.setPreferredSize(dim);
        txtField.setBackground(ORDER_DISABLE_COLOR);
    }

    /**
     * Add a JTextField and also set the editable property.
     * 
     * @param txtField
     *            The text field
     */
    public void add(JTextField txtField, boolean mode) {
        Color tempColor = txtField.getBackground();
        add(txtField);
        txtField.setEditable(mode);
        txtField.setBackground(tempColor);
    }

    /**
     * Add a JTextArea to the panel
     * 
     * @param txtArea
     *            The text area
     */
    public void add(JTextArea txtArea) {
        super.add(txtArea, constraints);
        txtArea.setEditable(true);
        txtArea.setMaximumSize(textAreaDim);
        txtArea.setMinimumSize(textAreaDim);
        txtArea.setPreferredSize(textAreaDim);
    }

    /**
     * Add a JComboBox to the panel
     * 
     * @param JComboBox
     *            The combo box
     * @param constraints
     *            The GridBagConstraints to use
     */
    public void add(JComboBox combo, GridBagConstraints constraints) {
        super.add(combo, constraints);
        combo.setMaximumSize(textFieldDim);
        combo.setMinimumSize(textFieldDim);
        combo.setPreferredSize(textComboField); // SCR 53190

    }

    /**
     * Add a JScrollPane to the panel
     * 
     * @param scrollPane
     *            The scroll pane
     */
    public void add(JScrollPane scrollPane) {
        super.add(scrollPane, constraints);
    }

    /**
     * Add an XPanel to the panel
     * 
     * @param panel
     *            The XPanel
     */
    public void add(XPanel panel) {
        add(panel, constraints);
    }

    /**
     * Return the current GridBagConstraints
     * 
     * @return The constraints
     */
    public GridBagConstraints getConstraints() {
        return constraints;
    }

    /**
     * Return the current model
     * 
     * @return The model
     */
    public OrderInitialDataVO getModel() {
        return this.model;
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        // No implementation
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        // No implementation
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     * @throws CSValidationException
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        // No implementation
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        // No implementation
    }

    /**
     * Framework method
     * 
     * @param parm1
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean parm1) throws CSRecoverableException {
        // No implementation
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        // No implementation
    }

    /**
     * Update the model
     */
    public void updateModel() {
        // No implementation
    }

}