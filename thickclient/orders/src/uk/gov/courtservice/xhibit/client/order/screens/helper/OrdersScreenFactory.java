package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.awt.GridBagConstraints;

import javax.swing.ButtonModel;
import javax.swing.JButton;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.PanelNotInitialisedException;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrdersWizardDialog;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.panel.D20TypeListPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderExistsPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderSavedPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderSentConfirmedPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderSignedPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderTypeListPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrdersPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrdersSummaryPanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Xhibit2: OrdersScreenFactory
 * </p>
 * <p>
 * Description: Returns the generated (cached) order screens
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

public class OrdersScreenFactory {
    private static final String NO_DEFS_ERROR = "orders.defendant.empty";

    private static final Logger log = CSServices.getLogger(OrdersScreenFactory.class);

    private static OrdersScreenFactory orderScreenFactory;

    private static OrdersPanelFactory ordersPanelFactory;

    private OrdersWizardDialog wizardDefendantScreen = null;

    private OrdersWizardDialog wizardOrderTypeScreen = null;

    private OrdersWizardDialog wizardDisposalsScreen = null;

    private OrdersPanel dialogOrderExistsScreen = null;

    private OrdersPanel dialogOrderSignedScreen = null;
    
    private OrdersPanel dialogOrderSentScreen = null;

    private OrdersPanel dialogOrderSavedScreen = null;

    private OrdersPanel dialogOrderListScreen = null;

    private OrderComboBoxListener defsListener = null;

    private OrderComboBoxListener ordListener = null;

    private OrderComboBoxListener dispListener = null;

    /**
     * Obtain instances of the factories
     */
    static {
        orderScreenFactory = new OrdersScreenFactory();
        ordersPanelFactory = OrdersPanelFactory.getinstance();
    }

    /**
     * Returns a static instance of the orderScreenFactory
     * 
     * @return orderScreenFactory
     */
    public static OrdersScreenFactory getinstance() {
        return orderScreenFactory;
    }

    /**
     * Returns the Defendant Screen to the Orders Wizard
     * 
     * @return XPanel Defendant screen
     */
    private XPanel getOrdersWizardDefendantScreen(int x) {
        return wizardDefendantScreen;
    }

    /**
     * Returns the Defendant Screen to the Orders Wizard
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @return XPanel Defendant screen
     * @throws CSRecoverableException
     */
    public XPanel getOrdersWizardDefendantScreen(OrderInitialDataVO model) throws CSRecoverableException {

        if (null == wizardDefendantScreen) {
            loadDefendantScreen(model);
        } else {
            try {
                wizardDefendantScreen.getSummaryPanel().setOrderDataModel(model);
                wizardDefendantScreen.getSummaryPanel().setPanelFromModel(model);
                wizardDefendantScreen.getDefendantPanel().setModel(model);
                wizardDefendantScreen.getDefendantPanel().setPanelFromModel(model);

            } catch (PanelNotInitialisedException pnie) {
                log.error("PanelNotInitialisedException: ", pnie);
                loadDefendantScreen(model);
            }
        }
        return wizardDefendantScreen;
    }

    /**
     * Returns the Defendant Screen to the Orders Wizard. the listener listens
     * to the text fields on the screen and enables/disables the button.
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @param button
     *            The button to enable/disable
     * @return XPanel Defendant screen
     * @throws CSRecoverableException
     */
    public XPanel getOrdersWizardDefendantScreen(OrderInitialDataVO model, JButton button)
            throws CSRecoverableException {
        getOrdersWizardDefendantScreen(model);
        OrderComboBoxListener listener = wizardDefendantScreen.getDefendantPanel().getListener();
        if (listener == null) {
            listener = new OrderDefendantComboBoxListener(wizardDefendantScreen.getSummaryPanel()
                    .getDefendantNameData(), model);
        } else {
            listener.setModel(model);
        }
        log.debug("$$$ OrderScreenFactory.getOrdersWizardDefendantScreen " + "odm.getMode() " + model.getMode());
        listener.addButton(button);

        return wizardDefendantScreen;
    }

    /**
     * Returns the Order Type selection screen to the Order Wizard. The listener
     * checks the values on the screen and enables/disables the button.
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @param button
     *            The button to enable disable
     * @return XPanel The OrderTypeScreen
     * @throws CSRecoverableException
     */
    public XPanel getOrdersWizardTypeScreen(OrderInitialDataVO model, JButton button) throws CSRecoverableException {
        getOrdersWizardTypeScreen(model);
        OrderComboBoxListener listener = wizardOrderTypeScreen.getOrderTypePanel().getListener();
        listener.setModel(model);
        listener.addButton(button);
        return wizardOrderTypeScreen;
    }
    
    /**
     * Returns the D20 Type selection screen to the Order Wizard. The listener
     * checks the values on the screen and enables/disables the button.
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @param button
     *            The button to enable disable
     * @return XPanel The OrderTypeScreen
     * @throws CSRecoverableException
     */
    public XPanel getD20TypeScreen(OrderInitialDataVO model, JButton button) throws CSRecoverableException {
        this.getD20TypePanel(model);
        OrderComboBoxListener listener = wizardOrderTypeScreen.getD20TypePanel().getListener();
        listener.setModel(model);
        listener.addButton(button);
        return wizardOrderTypeScreen;
    }

    /**
     * Returns the Order Type selection screen to the Order Wizard
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @return XPanel The OrderTypeScreen
     * @throws CSRecoverableException
     */
    public XPanel getOrdersWizardTypeScreen(OrderInitialDataVO model) throws CSRecoverableException {
        if ((null == wizardOrderTypeScreen) || (wizardOrderTypeScreen != null && wizardOrderTypeScreen.isD20TypePanel())) {
            wizardOrderTypeScreen = new OrdersWizardDialog(model);

            wizardOrderTypeScreen.getConstraints().weightx = 1.0;
            wizardOrderTypeScreen.getConstraints().weighty = 0.2;
            wizardOrderTypeScreen.getConstraints().fill = GridBagConstraints.BOTH;

            wizardOrderTypeScreen.add(getOrdersSummaryPanel(model));

            wizardOrderTypeScreen.getConstraints().gridy++;
            wizardOrderTypeScreen.getConstraints().weighty = 1.0;

        	wizardOrderTypeScreen.add((OrderTypeListPanel) ordersPanelFactory.getOrderTypeListPanel(model, ordListener));
        } else {
            try {
                wizardOrderTypeScreen.getOrderTypePanel().stepInitialise();
                
                wizardOrderTypeScreen.getSummaryPanel().setOrderDataModel(model);
                wizardOrderTypeScreen.getSummaryPanel().setPanelFromModel(model);
                wizardOrderTypeScreen.getOrderTypePanel().setPanelFromModel(model);
                log.debug("$$$ DEF OrderScreenFactory." + "getOrdersWizardDefendantScreen odm.getMode() " + model.getMode());
                wizardOrderTypeScreen.getOrderTypePanel().setModel(model);
            } catch (PanelNotInitialisedException pnie) {
                // Do nothing as panel will be reset
                log.debug("PanelNotInitialisedException............");
            }
        }
        return wizardOrderTypeScreen;
    }
    
    private XPanel getD20TypePanel(OrderInitialDataVO model) throws CSRecoverableException {
    	if ((null == wizardOrderTypeScreen) || (wizardOrderTypeScreen != null && wizardOrderTypeScreen.isOrderTypePanel())) {
            wizardOrderTypeScreen = new OrdersWizardDialog(model);

            wizardOrderTypeScreen.getConstraints().weightx = 1.0;
            wizardOrderTypeScreen.getConstraints().weighty = 0.2;
            wizardOrderTypeScreen.getConstraints().fill = GridBagConstraints.BOTH;

            wizardOrderTypeScreen.add(getOrdersSummaryPanel(model));

            wizardOrderTypeScreen.getConstraints().gridy++;
            wizardOrderTypeScreen.getConstraints().weighty = 1.0;
            
            wizardOrderTypeScreen.add((D20TypeListPanel) ordersPanelFactory.getD20TypeListPanel(model, ordListener));
        } else {
            try {
            	wizardOrderTypeScreen.getD20TypePanel().stepInitialise();
                
                wizardOrderTypeScreen.getSummaryPanel().setOrderDataModel(model);
                wizardOrderTypeScreen.getSummaryPanel().setPanelFromModel(model);
                log.debug("$$$ DEF OrderScreenFactory." + "getOrdersWizardDefendantScreen odm.getMode() " + model.getMode());
                
                wizardOrderTypeScreen.getOrderTypePanel().setModel(model);
            } catch (PanelNotInitialisedException pnie) {
                // Do nothing as panel will be reset
                log.debug("PanelNotInitialisedException............");
            }
        }
        return wizardOrderTypeScreen;
    }

    /**
     * Returns the OrdersSummaryPanel, as used in the Wizard Screens
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @return XPanel The OrderSummaryScreen
     * @throws CSRecoverableException
     */
    private XPanel getOrdersSummaryPanel(OrderInitialDataVO model) throws CSRecoverableException {
        OrdersSummaryPanel oSP = (OrdersSummaryPanel) ordersPanelFactory.getOrderSummaryPanel(model);
        defsListener = new OrderDefendantComboBoxListener(oSP.getDefendantNameData(), model);
        ordListener = new OrderTypeComboBoxListener(oSP.getOrderTypeData(), model);
        return oSP;
    }

    /**
     * Returns the screen if the requested OrderType/Defendant/Case combination
     * already exists
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @return XPanel The OrderExistsOptionScreen
     * @throws CSRecoverableException
     */
    public XPanel getOrderExistsOptionScreen(OrderInitialDataVO model, boolean replace) throws CSRecoverableException {
        log.debug("entering OrderScreenFactory.getOrderExistsOptionScreen");
        if (null == dialogOrderExistsScreen) {
            log.debug("creating OrderScreenFactory.getOrderExistsOptionScreen");
            dialogOrderExistsScreen = new OrdersPanel(model);

            dialogOrderExistsScreen.getConstraints().weightx = 1.0;
            dialogOrderExistsScreen.getConstraints().weighty = 0.2;
            dialogOrderExistsScreen.getConstraints().fill = GridBagConstraints.BOTH;

            dialogOrderExistsScreen.add(ordersPanelFactory.getOrderSummaryPanel(model), dialogOrderExistsScreen
                    .getConstraints());

            dialogOrderExistsScreen.getConstraints().gridx = 0;
            dialogOrderExistsScreen.getConstraints().gridy++;
            dialogOrderExistsScreen.getConstraints().weighty = 1.0;
            dialogOrderExistsScreen.add(ordersPanelFactory.getOrderExistsPanel(model), dialogOrderExistsScreen
                    .getConstraints());
        } else {
            log.debug("updating OrderScreenFactory.getOrderExistsOptionScreen");
            dialogOrderExistsScreen.getSummaryPanel().setOrderDataModel(model);
            dialogOrderExistsScreen.getSummaryPanel().setPanelFromModel(model);
            ButtonModel button = ((OrderExistsPanel) dialogOrderExistsScreen.getDetailPanel()).getCreateRButton()
                    .getModel();
            ((OrderExistsPanel) dialogOrderExistsScreen.getDetailPanel()).getOptionGroup().setSelected(button, true);
            ((OrderExistsPanel) dialogOrderExistsScreen.getDetailPanel()).resetOrderType();
        }
        // If Order can't be replace and existing order, hide the replace button
        ((OrderExistsPanel) dialogOrderExistsScreen.getDetailPanel()).getReplaceRButton().setVisible(replace);
        ((OrderExistsPanel) dialogOrderExistsScreen.getDetailPanel()).setMessage(replace);
        return dialogOrderExistsScreen;
    }

    /**
     * Returns the OrderSignedDialog This has to be recreated each time due to
     * problems resetting the date.
     * 
     * @param listener
     *            The listener to listen to the text fields
     * @return XPanel The OrderSignedDialog
     * @throws CSRecoverableException
     */
    public XPanel getOrderSignedScreen(OrderSignedListener listener) throws CSRecoverableException {
        dialogOrderSignedScreen = new OrdersPanel();

        GridBagConstraints signConstraints = dialogOrderSignedScreen.getConstraints();

        signConstraints.weightx = 1.0;
        signConstraints.weighty = 1.0;
        signConstraints.fill = GridBagConstraints.BOTH;
        OrderSignedPanel sPanel = null;
        if (listener.getSignVO() != null) {
        	sPanel = (OrderSignedPanel) ordersPanelFactory.getOrderSignedPanel(listener.getSignVO());
        } else {
        	sPanel = (OrderSignedPanel) ordersPanelFactory.getOrderSignedPanel(listener.getSentVO());
        }
        listener.setDateField(sPanel.getDatePanel().getDateComponent());
        sPanel.getDatePanel().getDateComponent().addMFieldListener(listener);
        sPanel.getSignSurname().addFocusListener(listener);
        // Initialise the signed date to the value on the panel (today)
        if (listener.getSignVO() != null) {
        	listener.getSignVO().setSignedDate(sPanel.getDatePanel().getDateComponent().getText());
        } else {
        	listener.getSentVO().setSentDate(sPanel.getDatePanel().getDateComponent().getText());
        }
        dialogOrderSignedScreen.add(sPanel, signConstraints);

        return dialogOrderSignedScreen;
    }
    

    /**
     * Returns the OrderSavedDialog
     * 
     * @return XPanel The OrderSavedDialog
     * @throws CSRecoverableException
     */
    public XPanel getOrderSavedScreen() throws CSRecoverableException {
        if (null == dialogOrderSavedScreen) {
            dialogOrderSavedScreen = new OrdersPanel();

            GridBagConstraints saveConstraints = dialogOrderSavedScreen.getConstraints();

            saveConstraints.weighty = 1.0;
            saveConstraints.weightx = 1.0;
            saveConstraints.fill = GridBagConstraints.BOTH;

            dialogOrderSavedScreen.add(ordersPanelFactory.getOrderSavedPanel(), saveConstraints);
        }
        ((OrderSavedPanel) dialogOrderSavedScreen.getDetailPanel()).getSaveDetails().setText("");
        return dialogOrderSavedScreen;
    }

    /**
     */
    /**
     * Returns the screen displaying the list of orders for the Case/Defendant
     * combination. The listener listens to the text fields and enables/disables
     * the button.
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @param button
     *            The button to enable/disable
     * @return XPanel The OrderListScreen
     * @throws CSRecoverableException
     */
    public XPanel getOrderListScreen(OrderInitialDataVO model, JButton button, boolean replace, boolean copy)
            throws CSRecoverableException {
        dialogOrderListScreen = new OrdersPanel(model);

        GridBagConstraints listConstraints = dialogOrderListScreen.getConstraints();

        listConstraints.weightx = 1.0;
        listConstraints.weighty = 0.2;
        listConstraints.fill = GridBagConstraints.BOTH;

        dialogOrderListScreen.add(ordersPanelFactory.getOrderSummaryPanel(model), listConstraints);

        addListPanel(model, button, listConstraints, replace, copy);

        return dialogOrderListScreen;
    }

    /**
     * Add the order list panel to the screen
     * 
     * @param odm
     *            The model
     * @param button
     *            The button to enable/disable
     * @param constraints
     *            The GridBagConstraints
     * @throws CSRecoverableException
     */
    private void addListPanel(OrderInitialDataVO model, JButton button, GridBagConstraints constraints, boolean replace, boolean copy)
            throws CSRecoverableException {
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weighty = 1.0;
        dialogOrderListScreen.add(ordersPanelFactory.getOrderListPanel(model, button, replace, copy), constraints);
    }

    /**
     * Populate the defendant panel with the defendant dropdown
     * 
     * @param model
     *            The model
     * @throws CSRecoverableException
     */
    private void loadDefendantScreen(OrderInitialDataVO model) throws CSRecoverableException {
        wizardDefendantScreen = new OrdersWizardDialog(model);

        wizardDefendantScreen.getConstraints().weightx = 1.0;
        wizardDefendantScreen.getConstraints().weighty = 0.2;
        wizardDefendantScreen.getConstraints().fill = GridBagConstraints.BOTH;

        wizardDefendantScreen.add(getOrdersSummaryPanel(model));

        wizardDefendantScreen.getConstraints().gridy++;
        wizardDefendantScreen.getConstraints().weighty = 1.0;

        wizardDefendantScreen.add(ordersPanelFactory.getDefendantListPanel(model, defsListener));

    }
}