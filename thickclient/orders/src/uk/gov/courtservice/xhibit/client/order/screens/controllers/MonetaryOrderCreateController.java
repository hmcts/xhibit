package uk.gov.courtservice.xhibit.client.order.screens.controllers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateValue;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderAlreadyCreatedException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderNotSupportedException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.ReplaceableOrderAlreadyCreatedException;
import uk.gov.courtservice.xhibit.client.order.exceptions.CancelledByUserException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderCreateException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderInitialDataException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderNotSelectedException;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderExistsDialog;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrdersWizard;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: Xhibit2 MonetaryOrderCreateController
 * </p>
 * <p>
 * Description: This must be registered with the main application. The action
 * controls the flow between the wizard and the other screens.
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

public class MonetaryOrderCreateController extends OrderController {

    private static final Logger log = CSServices.getLogger(MonetaryOrderCreateController.class);
    
    private static final String NO_MON_DISP_ERROR = "orders.monetary.disposals.empty";

    /**
     * Creates a new instance of OrderInitialDataVO in Create mode
     * 
     * @param xac
     *            the controller
     * @throws CSRecoverableException
     */
    public MonetaryOrderCreateController(XhibitApplicationController xac) throws CSRecoverableException {
        super();
        setController(xac);
        setHelper(new OrderInitialDataHelper(getController()));
        setUpModel();
        getHelper().setModel(getModel());
        loadData();
    }

    /**
     * Constructor
     * 
     * @throws CSRecoverableException
     */
    public MonetaryOrderCreateController() throws CSRecoverableException {
        super();
        log.debug("MonetaryOrderCreateController: entering default contructor");
        log.debug("MonetaryOrderCreateController: leaving default contructor");
    }

    /**
     * Displays the Orders Wizard screens. This method also controls the flow
     * between the wizard and the various orders dilaogs. The navigataion
     * between the wizard screens is controlled by the wizard itself.
     * 
     * @throws CSRecoverableException
     */
    public void displayWizard() throws CSRecoverableException {
        setWizardBounds();

        /**
         * Holder for the Orders Wizard
         */
        OrdersWizard wizard = null;
        wizard = new OrdersWizard(getController(), getModel());
        wizard.setBounds(getBounds());
        wizard.repaint();
        wizard.setVisible(true);

        // Once the wizard has been closed with 'Finish' the only way to tell
        // what has happened is via a flag on the OrderInitialDataVO.
        if (getModel().getFinished() == true) {
            try {
                if (isCreateAllowed()) {
                    showOrderGUI(generateOrderTemplates(false));
                } else {
                    throw new CSRecoverableException(NO_MON_DISP_ERROR, "No monetary disposals exist for this defendant.");
                }
            } catch (ReplaceableOrderAlreadyCreatedException ece1) {
                log.debug("ORDER ALREADY EXISTS ");

                displayOrderExists(wizard, true);
            } catch (OrderAlreadyCreatedException ece1) {
                log.debug("ORDER ALREADY EXISTS ");

                displayOrderExists(wizard, false);
            }
        }
    }

    private void displayOrderExists(OrdersWizard wizard, boolean replace) throws CSRecoverableException,
            CSUnrecoverableException {
        // If an exception is thrown here and order of this type already exists.
        // The Order Exists dialog is then displayed giving the user the option
        // to
        // create an order or view existing orders.
        try {
            displayOrderExistsDialog(wizard, replace);
        } catch (CancelledByUserException ex) {
            log.debug("$$$>>>>> Dialog Cancelled ");
            // Reset
            resetModel();

            displayWizard();// Do nothing as Dialog has been cancelled
        } catch (CSRecoverableException ex) {
            ex.printStackTrace();
            log.error("$$$>>>>> Could not create Order " + ex.getMessage());
            String logMessage = "Could not create Order - " + ex.getMessage();
            CSRecoverableException CSre = new CSRecoverableException(logMessage, ex.getUserMessageAsMessage().getKey(),
                    ex);
            throw CSre;
        }
    }

    /**
     * Reset the selections on the model
     */
    private void resetModel() {
        getModel().setDefendantName("");
        getModel().setDefendantID(null);
        getModel().setDefendantID(0);
        getModel().setDefendantOnCaseID(null);
        getModel().setDefendantOnCaseID(0);
        getModel().setOrderType("Monetary Order");
        getModel().setOrderID("Monetary Order");
    }

    /**
     * sets the bounds of the wizard screen based on the main xhibit screen
     */
    private void setWizardBounds() {
        if (getController() == null) {
            setBounds(new Rectangle(0, 0, 800, 600));
            log.debug("MonetaryOrderCreateController***: xac is null");
        } else {
            setBounds(getController().getBounds());
            log.debug("MonetaryOrderCreateController***: case id: " + getController().getApplicationCaseModel().getCaseNumber());
        }
    }

    /**
     * Display the Order Exists Dialog
     * 
     * @param wizard
     *            the wizard
     * @throws CSUnrecoverableException
     * @throws CSRecoverableException
     */
    private void displayOrderExistsDialog(OrdersWizard wizard, boolean replace) throws CSUnrecoverableException,
            CSRecoverableException {

        switch (showOrderExistsDialog(replace).intValue()) {
        case OrderInitialDataVO.CREATE_MODE: {
            forceCreateOrder(wizard);
        }
            ;
            break;
        case OrderInitialDataVO.VIEW_MODE: {
            viewOrders();
        }
            ;
            break;
        case OrderInitialDataVO.REPLACE_MODE: {
            replaceOrder();
        }
            ;
            break;
        default: {
            // Do nothing
        }
            ;
        }
    }

    /**
     * Attempts to create a set of order template files via the middle tier. If
     * the order (for the case, defendant, order type combination) already
     * exists an OrderCreateExceptionn is thrown
     * 
     * @param force
     *            Forces the creation of the order, even if one already exists
     * @throws OrderCreateException
     */
    private XhbOrderValue generateOrderTemplates(boolean force) throws ReplaceableOrderAlreadyCreatedException,
            OrderAlreadyCreatedException, CSUnrecoverableException, OrderNotSupportedException, OrderXMLException {
        log.debug("<<<<<<>>>>>> generateOrderTemplates <<<<<<<>>>>>>>");
        Integer defOnCaseID = getModel().getDefendantOnCaseID();
        Integer orderTypeID = getModel().getOrderTypeID();

        Integer caseId = Integer.parseInt(getModel().getCaseID().substring(1));
        Integer xhibitCaseId = getModel().getXhibitCaseId();
        String caseTitle = getModel().getCaseTitle();
        Integer courtId = new Integer(getModel().getXhibitCourtId());
        
        boolean isBCase = getModel().isBCase();

        log.debug("\nORDERS***: CaseID = " + getModel().getCaseID());
        log.debug("\nORDERS***: DefendantOnCaseID = " + defOnCaseID);
        log.debug("\nORDERS***: Defendant = " + getModel().getDefendantName());
        log.debug("\nORDERS***: OrderTypeID = " + orderTypeID);
        log.debug("\nORDERS***: OrderType = " + getModel().getOrderType());
        log.debug("\nORDERS***: IsBCase = " + getModel().isBCase());
        log.debug("\nORDERS***: CaseTitle = " + getModel().getCaseTitle());
        log.debug("\nORDERS***: CourtId = " + getModel().getXhibitCourtId());

        try {
            // Get the Order from the middle tier
            // MonetaryOrder data for display needs to be passed over too
            String monetaryDisposalInfoForDisplayOnOrder = "";
            String monetaryTotalsDisplayOnOrder = "";
            if (getModel().getHelper().getOrderDisposalData() != null) {
                if (getModel().getHelper().getOrderDisposalData().get(getModel().getDefendantName()) != null) {
                    HashMap thisDefendantDisposalDetails = (HashMap) getModel().getHelper().getOrderDisposalData().get(getModel().getDefendantName());
                    
                    if (thisDefendantDisposalDetails.get("monetaryDisposalsDisplayOnOrder") != null) {
                        monetaryDisposalInfoForDisplayOnOrder = thisDefendantDisposalDetails.get("monetaryDisposalsDisplayOnOrder").toString();
                        log.debug("\nORDERS***: MonetaryDisposalInfoForDisplayOnOrder = " + monetaryDisposalInfoForDisplayOnOrder);
                    } else {
                        log.debug("No disposal details for display");
                    }
                    
                    if (thisDefendantDisposalDetails.get("monetaryTotalsDisplayOnOrder") != null) {
                    	monetaryTotalsDisplayOnOrder = thisDefendantDisposalDetails.get("monetaryTotalsDisplayOnOrder").toString();
                        log.debug("\nORDERS***: MonetaryTotalsForDisplayOnOrder = " + monetaryTotalsDisplayOnOrder);
                    } else {
                        log.debug("No total details for display");
                    }
                    
                } else {
                    log.debug("no matching defendant name on orderdisposal data for " + getModel().getDefendantName());
                }
            } else {
                log.debug("order disposal data is empty");
            }
            XhbOrderValue order = createOrder(defOnCaseID, caseId, xhibitCaseId, caseTitle, courtId, orderTypeID, force, isBCase, monetaryDisposalInfoForDisplayOnOrder, monetaryTotalsDisplayOnOrder);

            logTemplateDetails(order);

            return order;
        } catch (ReplaceableOrderAlreadyCreatedException ex) {
            log.debug("$$$ ReplaceableOrderAlreadyCreatedException ");
            throw ex;
        } catch (OrderAlreadyCreatedException ex) {
            log.debug("$$$ OrderAlreadyCreatedException ");
            throw ex;
        } catch (OrderException ex) {
            log.debug("$$$ OrderException " + ex.getMessage());
            throw new CSUnrecoverableException("ORDER ***", ex);
        }

    }

    /**
     * Attempts to create a set of order template files via the middle tier. If
     * the order (for the case, defendant, order type combination) already
     * exists an OrderCreateExceptionn is thrown
     * 
     * @param force
     *            Forces the creation of the order, even if one already exists
     * @throws OrderCreateException
     */
    private XhbOrderValue generateOrderTemplates(Integer originalOrderId) throws // ReplaceableOrderAlreadyCreatedException,
            OrderAlreadyCreatedException, CSUnrecoverableException, OrderNotSupportedException, OrderXMLException {
        log.debug("<<<<<<>>>>>> generateOrderTemplates REPLACE<<<<<<<>>>>>>>");
        Integer defOnCaseID = getModel().getDefendantID();
        Integer orderTypeID = getModel().getOrderTypeID();

        log.debug("\nMonetaryOrderCreateController***: CaseID = " + getModel().getCaseID());
        log.debug("\nMonetaryOrderCreateController***: DefendantID = " + defOnCaseID);
        log.debug("\nMonetaryOrderCreateController***: Defendant = " + getModel().getDefendantName());
        log.debug("\nMonetaryOrderCreateController***: OrderTypeID = " + orderTypeID);
        log.debug("\nMonetaryOrderCreateController***: OrderType = " + getModel().getOrderType());

        try {
            // Get the Order from the middle tier
            XhbOrderValue order = XhibitDelegateHelper.getOrdersDelegate().replaceOrder(defOnCaseID, orderTypeID, null,
                    originalOrderId);

            logTemplateDetails(order);

            return order;
        } catch (OrderAlreadyCreatedException ex) {
            log.debug("$$$ OrderAlreadyCreatedException ");
            throw ex;
        } catch (OrderException ex) {
            log.debug("$$$ OrderException " + ex.getMessage());
            throw new CSUnrecoverableException("ORDER ***", ex);
        }

    }

    private void logTemplateDetails(XhbOrderValue order) {
        // Logging
        log.debug("ORDER XML \n order:" + order.toString());
        XhbOrderTemplateValue xhbOrderTemplate = order.getXhbOrderTemplate();
        String displayTransformName = xhbOrderTemplate.getDisplayTransformName();
        log.debug("Display Transform Name: " + displayTransformName);
        String narrativeTemplateName = xhbOrderTemplate.getNarrativeTemplateName();
        log.debug("Narrative Template Name: " + narrativeTemplateName);
        String editorTemplateName = xhbOrderTemplate.getEditorTemplateName();
        log.debug("Editor Template Name: " + editorTemplateName);
    }

    /**
     * Request the middle tier to create an order of the given type for the
     * given defendant on case.
     * 
     * @param defOnCaseID
     *            The defendant on case id
     * @param orderID
     *            The id of the required order type
     * @param force
     *            true if the order is to be created, whether one already exists
     *            or not, otherwise false
     * @return The generated XhbOrderValue
     * @throws CSUnrecoverableException
     * @throws OrderException
     * @throws OrderNotSupportedException
     * @throws OrderXMLException
     */
    private XhbOrderValue createOrder(Integer defOnCaseID, Integer caseId, Integer xhibitCaseId, String caseTitle, Integer courtId, Integer orderID, boolean force, boolean isBCase, String monetaryDisposalInfoForDisplayOnOrder, String monetaryTotalsForDisplayOrder)
            throws CSUnrecoverableException, OrderException, OrderNotSupportedException, OrderXMLException {

        log.debug("---------------------------------------------Def: " + defOnCaseID + " OrderId: " + orderID);
        XhbOrderValue order = XhibitDelegateHelper.getOrdersDelegate().createOrder(defOnCaseID, caseId, xhibitCaseId, caseTitle, courtId, orderID, null, force, isBCase, monetaryDisposalInfoForDisplayOnOrder, monetaryTotalsForDisplayOrder);
        return order;
    }

    /**
     * Initialises the model
     * 
     * @throws CSRecoverableException
     */
    private void setUpModel() throws CSRecoverableException {
        setModel(new OrderInitialDataVO());
        getModel().setMode(OrderInitialDataVO.CREATE_MODE);
        getModel().setHelper(getHelper());
    }

    /**
     * Populates the model
     * 
     * @throws CSRecoverableException
     */
    public void loadData() throws CSRecoverableException {
        try {
            getHelper().loadModel(true, false);
        } catch (OrderInitialDataException oide) {
            log.error("OrderInitialDataException");
            throw new CSRecoverableException("OrderInitialDataException", "OrderCreateAction", oide);
        }
    }

    /**
     * Display the Order Exists Dialog. The user is given the option to create
     * an order of the given type, or view a list of existing orders.
     * 
     * @return true if a new order can be created, otherwise false
     * @throws CSRecoverableException
     */
    private Integer showOrderExistsDialog(boolean replace) throws CSRecoverableException {
        log.debug("Displaying OrderExistsDialog............................");
        getModel().setCVOption(null);
        OrderExistsDialog ordExists = new OrderExistsDialog(getController(), "Orders - Order Exists", getModel(),
                replace);
        ordExists.setBounds(getBounds());
        ordExists.setVisible(true);
        if (ordExists.isCancelled()) {
            ordExists = null;
            throw new CancelledByUserException("OrderExistsDialog:Cancelled - Dialog Cancelled");
        }
        return new Integer(ordExists.getScreenOption());
    }

    private void forceCreateOrder(OrdersWizard wizard) throws CSUnrecoverableException, CSRecoverableException {
        wizard.setVisible(false);
        wizard = null;
        showOrderGUI(generateOrderTemplates(true));
    }

    private void viewOrders() throws CSUnrecoverableException, CSRecoverableException {
        log.debug("Start - viewOrders");
        // The user has chosen to view the list of existing orders
        try {
            int result = isShowListDialog(getModel(), getBounds(), false, false);
            processSelection(result);
        } catch (UserCancelException ex) {
            log.info("User cancelled dialog");
        }

    }

    private void replaceOrder(Integer originalOrderId) throws CSUnrecoverableException, CSRecoverableException {
        showOrderGUI(generateOrderTemplates(originalOrderId));
    }

    private void processSelection(int result) throws CSUnrecoverableException, CSRecoverableException {
        switch (result) {
        case OrderInitialDataVO.CREATE_MODE: {
            displayWizard();
            break;
        }
        case OrderInitialDataVO.VIEW_MODE: {
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
        case OrderInitialDataVO.REPLACE_MODE: {
            // Display the selected order
            Integer orderID = null;
            try {
                orderID = getModel().getSelectedOrder();
                replaceOrder(orderID);
            } catch (OrderNotSelectedException ex) {
                log.info(ex.getMessage());
            }
            break;
        }
        }
    }

    private void replaceOrder() throws CSUnrecoverableException, CSRecoverableException {

        log.debug("Start - replaceOrder");
        // The user has chosen to view the list of existing orders
        try {
            int result = isShowListDialog(getModel(), getBounds(), true, false);
            processSelection(result);
        } catch (UserCancelException ex) {
            log.info("User cancelled dialog");
        }

    }
}