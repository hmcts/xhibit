package uk.gov.courtservice.xhibit.client.order.screens.controllers;

import java.awt.Rectangle;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderInitialDataException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderNotSelectedException;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrdersWizard;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Xhibit2 D20ViewController
 * </p>
 * <p>
 * Description: This must be registered with the main application. The action
 * controls the flow between the wizard in view mode and the other screens.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */

public class D20ViewController extends OrderController {

    private static final Logger log = CSServices.getLogger(D20ViewController.class);

	private static final String D20_MISCELLANEOUS_WARNING = "d20.view.miscellaneous.warning";

    /**
     * Creates a new instance of OrderInitialDataVO in View mode
     * 
     * @param xac
     *            the controller
     * @throws CSRecoverableException
     */
    public D20ViewController(XhibitApplicationController xac) throws CSRecoverableException {
        super();
        setController(xac);
        log.debug("D20ViewController***: constructor with XhibitApplicationController");
    }

    /**
     * Displays the Orders Wizard
     * 
     * @throws CSRecoverableException
     */
    public void displayWizard() throws CSRecoverableException {
        setHelper(new OrderInitialDataHelper(getController()));
        setUpModel();
        getHelper().setModel(getModel());

        try {
            getHelper().loadModel(false, true);
        } catch (OrderInitialDataException oide) {
            log.error("OrderInitialDataException");
            throw new CSUnrecoverableException(oide);
        }

        if ( getModel().isACase() && getModel().getCaseSubType().equals("O") ){
			//	It's a miscellaneous case
			log.debug("Miscellaneous case");
			
            JOptionPane.showMessageDialog(getController(), ResourceHelper.getResourceString(D20_MISCELLANEOUS_WARNING),
                    ResourceHelper.getResourceString(OrdersWizard.WIZ_WARN_TITLE), JOptionPane.WARNING_MESSAGE);
		}else {			
			if (getController() == null) {
	            setBounds(new Rectangle(0, 0, 800, 600));
	            log.debug("D20ViewController***: xac is null");
	        } else {
	            setBounds(getController().getBounds());
	            log.debug("D20ViewController***: case id: " + getController().getApplicationCaseModel().getCaseNumber());
	        }
	        /**
	         * Holder for the Orders Wizard
	         */
	        OrdersWizard wizard = null;
	        wizard = new OrdersWizard(getController(), getModel());
	        wizard.setBounds(getBounds());
	        wizard.setVisible(true);
	
	        // wizard is modal so set reference to null once the dialog has been
	        // closed
	        wizard = null;
	
	        // The only way to tell how the wizard was closed is to set a flag on
	        // the
	        // model and query that
	        if (getModel().getFinished() == true) {
	            try {
	
	                switch (isShowListDialog(getModel(), getBounds(), false, false)) {
	                case OrderInitialDataVO.CREATE_MODE: {
	                    wizard = null;
	                    createController();
	                    break;
	                }
	                case OrderInitialDataVO.VIEW_MODE:
	                    // {
	                    // wizard = null;
	                    // try
	                    // {
	                    // Integer orderID = getModel().getSelectedOrder();
	                    // showExistingOrder(orderID);
	                    // }
	                    // catch (OrderNotSelectedException ex) {
	                    // log.info(ex.getMessage());
	                    // }
	                    // break;
	                    // }
	                case OrderInitialDataVO.REPLACE_MODE: {
	                    // Display the selected order
	                    Integer orderID = null;
	                    try {
	                        orderID = getModel().getSelectedOrder();
	                        showExistingOrder(orderID);
	                    } catch (OrderNotSelectedException ex) {
	                        log.info(ex.getMessage());
	                    }
	                    break;
	                }
	                }
	            } catch (CSRecoverableException ex) {
	                displayWizard();
	            }
	        }
		}
    }

    private void setUpModel() throws CSRecoverableException {
        setModel(new OrderInitialDataVO());
        log.debug("$$$ D20ViewController:this.ordInitData.getMode " + getModel().getMode());
        getModel().setMode(OrderInitialDataVO.VIEW_MODE);
        log.debug("$$$ D20ViewController:this.ordInitData.getMode " + getModel().getMode());
        getModel().setHelper(getHelper());
    }

    private void createController() throws CSRecoverableException {

        getModel().setMode(OrderInitialDataVO.CREATE_MODE);
        D20CreateController createController = new D20CreateController(getController());
        createController.displayWizard();

    }

}