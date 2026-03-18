package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Screen for entering Indictment details
 * </p>
 * <p>
 * Description: This is the first screen of the Add Indictment Wizard process,
 * where general data that is required for the creation of an Indictment is
 * entered.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class IndictmentDetailsPanel extends XPanel {
    private IndictmentWizardModel model = null;

    private ResourceBundle resources = null;

    private BreachController controller = null;

    private JLabel indReceivedLabel = null;

    private JLabel prosPapersServedLabel = null;

    private XDatePanel indReceivedDate = null;

    private XDatePanel prosPapersServedDate = null;

    public IndictmentDetailsPanel(BreachController controller, IndictmentWizardModel model)
            throws CSRecoverableException {
        super();
        this.controller = controller;
        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    /**
     * Paints the controls on the screen.
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());
        add(getIndReceivedLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(getIndReceivedDate(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(getProsPapersServedLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(getProsPapersServedDate(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    }

    /**
     * Lazy instantiates the Indictment Received Date label.
     * 
     * @return the Indictment Received date label.
     */
    private JLabel getIndReceivedLabel() {
        if (indReceivedLabel == null) {
            indReceivedLabel = new JLabel();
            indReceivedLabel.setText(XHIBITConstant.getResource(resources, "indictmentDetails.label.indReceived"));
        }
        return indReceivedLabel;
    }

    /**
     * Lazy instantiates the Indictment Received Date field.
     * 
     * @return the Indictment Received Date.
     */
    private XDatePanel getIndReceivedDate() {
        if (indReceivedDate == null) {
            Calendar noDate = null;
            indReceivedDate = new XDatePanel(this, noDate);
            indReceivedDate.setRequired(true);
            indReceivedDate.getDateComponent().getDisplay().addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    try {
                        stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITConstant.handleError(csre);
                    }
                }
            });
        }
        return indReceivedDate;
    }

    /**
     * Lazy instantiates the Prosecution Papers Served date label.
     * 
     * @return the Prosecution Papers Served date label.
     */
    private JLabel getProsPapersServedLabel() {
        if (prosPapersServedLabel == null) {
            prosPapersServedLabel = new JLabel();
            prosPapersServedLabel.setText(XHIBITConstant.getResource(resources,
                    "indictmentDetails.label.prosPapersServed"));
        }
        return prosPapersServedLabel;
    }

    /**
     * Lazy instantiates the Prosecution Papers Served date field.
     * 
     * @return the Prosecution Papers Served date field.
     */
    private XDatePanel getProsPapersServedDate() {
        if (prosPapersServedDate == null) {
            Calendar noDate = null;
            prosPapersServedDate = new XDatePanel(this, noDate);
            prosPapersServedDate.setRequired(false);
            prosPapersServedDate.setDateEditable(false);
            prosPapersServedDate.setDateEnabled(false);
            prosPapersServedDate.getDateComponent().getDisplay().addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    try {
                        stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITConstant.handleError(csre);
                    }
                }
            });
        }
        return prosPapersServedDate;
    }

    /**
     * Checks if all required fields have been populated.
     * 
     * @return true if all mandatory fields have been completed.
     */
    protected boolean isMandatoryFieldsCompleted() {
        return getIndReceivedDate().isMandatoryFieldsCompleted();
    }

    /**
     * Saves the data on the screen to the IndictmentWizardModel.
     * 
     * @throws CSRecoverableException
     */
    private void moveScreenToModel() throws CSRecoverableException {
        XHIBITConstant.debug("moveScreenToModel");
        model.setIndReceivedDate(getIndReceivedDate().getDate());
        model.setProsPapersServedDate(getProsPapersServedDate().getDate());
    }

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * first loaded.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        XHIBITConstant.debug("[IndictmentDetailsPanel] stepInitialise");
        resources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);
    }

    /**
     * XPanel implementation of life cycle method, called when (or each time)
     * the screen is left.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        XHIBITConstant.debug("[IndictmentDetailsPanel] stepDeactivate");
    }

    /**
     * XPanel implementation of life cycle method, called when leaving this
     * screen to validate the data.
     * 
     * @throws CSRecoverableException
     * @throws CSValidationException
     *             if an invalid date is entered.
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        XHIBITConstant.debug("[IndictmentDetailsPanel] stepValidate");

        try {
            getIndReceivedDate().stepValidate();
            getProsPapersServedDate().stepValidate();
        } catch (CSValidationException csve) {
            throw csve;
        }

        moveScreenToModel();
    }

    /**
     * XPanel implementation of life cycle method, called when the state of a
     * widget changes.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        XHIBITConstant.debug("[IndictmentDetailsPanel] stepUpdateViewState");

        if (getIndReceivedDate().isMandatoryFieldsCompleted()) {
            getProsPapersServedDate().setDateEditable(true);
            getProsPapersServedDate().setDateEnabled(true);
        } else {
            getProsPapersServedDate().setDateEditable(false);
            getProsPapersServedDate().setDateEnabled(false);
        }

        model.setIndictmentDetailsPopulated(isMandatoryFieldsCompleted());

        controller.stepUpdateViewState();
    }

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * closed. Note - This is only called if the user has been to the other
     * screens in the wizard, populated the necessary data and then come back to
     * this screen.
     * 
     * @param update
     *            true if the data on the screen is being saved.
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        XHIBITConstant.debug("[IndictmentDetailsPanel] stepDeinitialise");

        if (update) {
            controller.stepDeinitialise();
        }
    }

    /**
     * XPanel implementation of life cycle method, called each time the screen
     * is displayed.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        XHIBITConstant.debug("[IndictmentDetailsPanel] stepActivate");
        stepUpdateViewState();
    }
}