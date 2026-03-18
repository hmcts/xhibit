package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.CancelledByUserException;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.util.LimitedTextDocument;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: Xhibit2 OrderSavedPanel
 * </p>
 * <p>
 * Description: Displays order saved information
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

public class OrderSavedPanel extends AbstractOrdersPanel {
    private static final Logger log = CSServices.getLogger(OrderSavedPanel.class);

    private boolean cancelledByUser = false;

    private static final String ORDER_SAVED_PANEL_TITLE = "OrderSavedTitle";

    private static final String ORDER_SAVED_DESC = "OrderSavedDescription";

    private static final int ORDER_SAVED_TEXT_LEN = 40;

    private JTextArea saveDetails;

    /**
     * 
     */
    public OrderSavedPanel() throws CSRecoverableException {
        super();
        initialisePanel();
    }

    /**
     * 
     */
    protected void initialisePanel() throws CSRecoverableException {
        super.initialisePanel();

        getConstraints().anchor = GridBagConstraints.NORTHWEST;
        add(new JLabel(ResourceHelper.getResourceString(ORDER_SAVED_DESC)), getConstraints());

        getConstraints().gridx++;
        JScrollPane pane = new JScrollPane(getSaveDetails(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(pane);

        setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), ResourceHelper
                .getResourceString(ORDER_SAVED_PANEL_TITLE)));

    }

    /**
     * 
     * @param parm1
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepDeinitialise(boolean parm1) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        XHIBITConstant.debug("OrderSavedPanel: stepDeinitialise(" + parm1 + ")");
        cancelledByUser = !parm1;
    }

    /**
     * Returns a JTextArea, which is added to the panel to hold the description.
     * 
     * @return JTextArea, of limited input length, to enter the save descrition
     */
    public JTextArea getSaveDetails() {

        if (saveDetails == null) {
            saveDetails = new JTextArea();
            saveDetails.setDocument(new LimitedTextDocument(ORDER_SAVED_TEXT_LEN));
            saveDetails.setLineWrap(true);
        }
        return saveDetails;
    }

    /**
     * Returns the saved details entered into the dilaog
     * 
     * @return String
     * @throws CancelledByUserException
     */
    public String getOrderSaveDetails() throws CancelledByUserException {
        if (cancelledByUser == true) {
            throw new CancelledByUserException("OrderSavedDialog cancelled by user");
        }
        return getSaveDetails().getText();
    }

    /**
     * Add a JScrollPane containing a JTextArea to the panel
     * 
     * @param scrollPane
     *            The scroll pane
     */
    public void add(JScrollPane scrollPane) {
        super.add(scrollPane, constraints);
        // set the scroll pane to the size of the text area
        scrollPane.setMaximumSize(textAreaDim);
        scrollPane.setMinimumSize(textAreaDim);
        scrollPane.setPreferredSize(textAreaDim);
    }

}