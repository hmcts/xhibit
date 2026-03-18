package uk.gov.courtservice.xhibit.client.order.screens.dialog;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationProblem;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Panel to display Error and Warning messages generated from
 * Orders validation
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 * 
 */

public class OrderValidationPanel extends XPanel {
    private static final Logger log = CSServices.getLogger(OrderValidationPanel.class);

    private static final String ERROR_TITLE = "order.error.title";

    private static final String WARNING_TITLE = "order.warning.title";

    private static final String DEFAULT_MESSAGE = "order.default.message";

    private static final int DEFAULT_PANEL_HEIGHT_MULTIPLE = 5;

    private OrderValidationProblem[] errors;

    private OrderValidationProblem[] warnings;

    private OrderValidationModel model;

    private GridBagConstraints defaultConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 2, 2), 0, 20);

    private JScrollPane errorsScrollPane = new JScrollPane();

    private JScrollPane warningsScrollPane = new JScrollPane();

    private JLabel errorLbl;

    private JLabel warningsLbl;

    private ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.OrdersValidation);

    private OkCancelPanel buttonPanel;

    private JLabel panelTitleLabel;

    private XDialog parent;

    /**
     * Public constructor
     * 
     * @param parent
     *            The parent of the dialog
     * @param model
     *            The model to display
     * @throws CSRecoverableException
     */
    public OrderValidationPanel(XDialog parent, OrderValidationModel model) throws CSRecoverableException {
        super();
        this.parent = parent;
        this.buttonPanel = (OkCancelPanel) parent.getButtonPanel();
        this.model = model;

        stepInitialise();
        init();
        stepActivate();
    }

    private void init() throws CSRecoverableException {
        log.debug("$$$Entering init()");
        this.setLayout(new GridBagLayout());

        if (model.getErrors() != null && model.getErrors().length > 0) {
            addErrors(model.getErrors());
        }

        if (model.getWarnings() != null && model.getWarnings().length > 0) {
            addWarnings(model.getWarnings());
        }
        log.debug("$$$leaving jbInit()");
    }

    /**
     * Print the model
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        log.debug("$$$Entering stepInitialise()");
        model.printModel();
        log.debug("$$$Leaving stepInitialise()");
    }

    /**
     * Framework method
     */
    public void stepActivate() {
    }

    /**
     * Framework method
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
    }

    /**
     * Clears the panel by removing any populated components - ready for reuse.
     * 
     * @param update
     *            true if okay was pressed
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        model.clearmodel();
        if (errorsScrollPane != null) {
            this.remove(errorsScrollPane);
            errorsScrollPane = null;
        }
        if (errorLbl != null) {
            this.remove(errorLbl);
            errorLbl = null;
        }
        if (warningsScrollPane != null) {
            this.remove(warningsScrollPane);
            warningsScrollPane = null;
        }
        if (warningsLbl != null) {
            this.remove(warningsLbl);
            warningsLbl = null;
        }
        resetConstraints();
    }

    /**
     * Framework method
     */
    public void stepUpdateViewState() {
    }

    /**
     * Add any errors, from the OrderValidationExceprion, to the panel. If no
     * errors are present, do not add any error info to the panel.
     * 
     * @param errors
     *            Array of OrderValidationProblem as retrieved from the
     *            OrderValidationException
     * @throws CSRecoverableException
     */
    private void addErrors(OrderValidationProblem[] errors) throws CSRecoverableException {
        log.debug("$$$Entering addErrors()");
        java.awt.Dimension errorPanelSize = new java.awt.Dimension(450, Math.min(
                ((XHIBITConstant.getLineHeight() + 1) * errors.length),
                ((XHIBITConstant.getLineHeight() + 1) * DEFAULT_PANEL_HEIGHT_MULTIPLE)));
        errorsScrollPane.setMinimumSize(errorPanelSize);
        errorsScrollPane.setPreferredSize(errorPanelSize);
        XPanel errorPanel = getMessagePanel(errors);
        errorPanel.setBorder(BorderFactory.createEtchedBorder(Color.yellow, Color.red));
        errorsScrollPane.getViewport().add(errorPanel, null);

        errorLbl = getPanelTitleLabel(ERROR_TITLE);
        errorLbl.setForeground(Color.red);
        this.add(errorLbl, defaultConstraints);
        log.debug("$$$Entering addErrors() errorLbl gridy = " + defaultConstraints.gridy);
        defaultConstraints.gridy++;
        this.add(errorsScrollPane, defaultConstraints);
        log.debug("$$$Entering addErrors() errorsScrollPane gridy = " + defaultConstraints.gridy);
        defaultConstraints.gridy++;
        log.debug("$$$Leaving addErrors()");
    }

    /**
     * Add any warnings, from the OrderValidationExceprion, to the panel. If no
     * warnings are present, do not add any error info to the panel.
     * 
     * @param warnings
     *            Array of OrderValidationProblem as retrieved from the
     *            OrderValidationException
     * @throws CSRecoverableException
     */
    private void addWarnings(OrderValidationProblem[] warnings) throws CSRecoverableException {
        log.debug("$$$Entering addWarnings()");
        java.awt.Dimension warningPanelSize = new java.awt.Dimension(550, Math.min(
                ((XHIBITConstant.getLineHeight() + 1) * warnings.length),
                ((XHIBITConstant.getLineHeight() + 1) * DEFAULT_PANEL_HEIGHT_MULTIPLE)));
        warningsScrollPane.setMinimumSize(warningPanelSize);
        warningsScrollPane.setPreferredSize(warningPanelSize);
        XPanel warningsPanel = getMessagePanel(warnings);
        warningsPanel.setBorder(BorderFactory.createEtchedBorder(Color.yellow, Color.green));
        warningsScrollPane.getViewport().add(warningsPanel, null);

        warningsLbl = getPanelTitleLabel(WARNING_TITLE);
        this.add(warningsLbl, defaultConstraints);
        log.debug("$$$Entering addWarnings() warningsLbl gridy = " + defaultConstraints.gridy);
        defaultConstraints.gridy++;
        this.add(warningsScrollPane, defaultConstraints);
        log.debug("$$$Entering addWarnings() warningsScrollPane gridy = " + defaultConstraints.gridy);
        defaultConstraints.gridy++;
        log.debug("$$$Leaving addWarnings()");
    }

    /**
     * Get the populated panel to display
     * 
     * @param problems
     *            Array of OrderVlidationProblem as retrieved from the
     *            OrderValidationException
     * @return The panel containing the messages
     * @throws CSRecoverableException
     */
    private XPanel getMessagePanel(OrderValidationProblem[] problems) throws CSRecoverableException {
        log.debug("$$$Entering getMessagePanel()");
        XPanel panel = new MessagePanel(problems);
        log.debug("$$$Leaving getMessagePanel()");
        return panel;
    }

    private JLabel getPanelTitleLabel(String title) {
        return new PanelTitleLabel(XHIBITConstant.getResource(resources, title));
    }

    private void resetConstraints() {
        defaultConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 2, 2), 0, 20);
    }

    /**
     * 
     * <p>
     * Title: MessagePanel
     * </p>
     * <p>
     * Description: Class that contains a list of all messagesa retrieved from
     * an array of OrderValidationProblem
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
    class MessagePanel extends XPanel {
        private static final int MESSAGE_COLS = 1;

        private final Logger log = CSServices.getLogger(MessagePanel.class);

        private OrderValidationProblem[] problems;

        private Color colour;

        private MessagePanel(OrderValidationProblem[] problems) throws CSRecoverableException {
            this.problems = problems;
            this.stepInitialise();
        }

        /**
         * Polulates the panel with the appropriate codes and messages
         * 
         * @throws CSRecoverableException
         */
        public void stepInitialise() throws CSRecoverableException {
            log.debug("$$$ MessagePanel.stepInitialise");
            this.setLayout(new GridLayout(problems.length, MESSAGE_COLS));

            log.debug("$$$ MessagePanel.stepInitialise.problems.length = |" + problems.length + "|");
            for (int x = 0; x < problems.length; x++) {
                log.debug("$$$ MessagePanel.....getMessageCode() = |" + this.problems[x].getMessageCode() + "|");
                String messageCode = XHIBITConstant.getResource(resources, this.problems[x].getMessageCode());
                Message message = null;
                try {
                    message = new Message(messageCode, this.problems[x].getMessageFields());
                } catch (Exception ex) {
                    log.error("$$$ Message Exception " + ex);
                    throw new CSRecoverableException("ORDERS_XXX", "Error retrieving Resource", ex);
                }
                log.debug("$$$ MessagePanel.stepInitialise.message.getMessage() = |" + message.getMessage() + "|");
                addMessage(message);
            }
        }

        private void addMessage(Message message) {
            try {
                this.add(new JLabel(message.getMessage()));
            } catch (MissingResourceException ex) {
                this.add(new JLabel(XHIBITConstant.getResource(resources, DEFAULT_MESSAGE)));
            }
        }

        /**
         * Framework method
         */
        public void stepActivate() {
        }

        /**
         * Framework method
         * 
         * @throws CSValidationException
         * @throws CSRecoverableException
         */
        public void stepValidate() throws CSValidationException, CSRecoverableException {
        }

        /**
         * Framework method
         * 
         * @throws CSRecoverableException
         */
        public void stepDeactivate() throws CSRecoverableException {
        }

        /**
         * Framework method
         * 
         * @param update
         * @throws CSRecoverableException
         */
        public void stepDeinitialise(boolean update) throws CSRecoverableException {
            problems = null;
        }

        /**
         * Framework method
         */
        public void stepUpdateViewState() {
        }

    }
    // end MessagePanel class
}
