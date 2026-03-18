package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.awt.event.ActionEvent;

import javax.swing.JRadioButton;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: Xhibit2 OrderExistsRButtonListener
 * </p>
 * <p>
 * Description: Listens on a radio button on the OrderExistsPanel and sets the
 * CVOption on the OrderInitialDataVO to the selection (as specified in the
 * ActionCommand)
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

public class OrderExistsRButtonListener extends XAction {
    private static final Logger log = CSServices.getLogger(OrderExistsRButtonListener.class);

    /**
     * 
     */
    private OrderInitialDataVO model = null;

    /**
     * 
     */
    private JRadioButton btnChoice = null;

    /**
     * 
     */
    private OrderExistsRButtonListener() {
    }

    /**
     * Constructor to hold local references to the button and model. The
     * listener will update the model when the button is selected.
     * 
     * @param button
     *            JRadioButton to which the listener is attached
     * @param oidvo
     *            OrderInitialDataVO used to hold the initial wizard data
     */
    public OrderExistsRButtonListener(JRadioButton button, OrderInitialDataVO model) {
        this();
        this.model = model;
        btnChoice = button;
    }

    /**
     * 
     * @param parm1
     *            ActionEvent
     * @throws java.lang.Exception
     */
    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XAction
         *       abstract method
         */
        log.debug("Button choice : " + btnChoice.getActionCommand());
        this.model.setCVOption(btnChoice.getActionCommand());
    }
}