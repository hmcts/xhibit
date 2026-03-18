package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;
import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Screen when advocate is an instructed advocate
 * </p>
 * <p>
 * Description: Sets the I/S flag to I.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */

public class EditAdvocateWizardExistingInstructedAdvocate extends XPanel {

    private static final long serialVersionUID = 1L;

    private EditAdvocateWizardModel model;

    private ResourceBundle resources;

    private EditAdvocateWizardController controller;

    private JLabel message1;
    private JLabel message2;

    
    
    public EditAdvocateWizardExistingInstructedAdvocate(
            EditAdvocateWizardController controller,
            EditAdvocateWizardModel model)
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
        
        int row = 0;
 
        add(getMessage1(), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(getMessage2(), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    }

    private JLabel getMessage1() {
        if (message1 == null) {
            message1 = new JLabel(
                    XHIBITConstant.getResource(resources, "lblAdvocateIsAnIAForDefendant")); 
        }
        return message1;
    }
    
    private JLabel getMessage2() {
        if (message2 == null) {
            message2 = new JLabel(
                    XHIBITConstant.getResource(resources, "lblPressFinishSetISFlag")); 
        }
        return message2;
    }
    
    /**
     * Checks if all required fields have been populated.
     * 
     * @return true if all mandatory fields have been completed.
     */
    protected boolean isMandatoryFieldsCompleted() {
        return true;
    }

    /**
     * Saves the data on the screen to the FindRepresentationWizardModel.
     * 
     * @throws CSRecoverableException
     */
    private void moveScreenToModel() {
        XHIBITConstant.debug("moveScreenToModel");
        model.setNewInstructedAdvocateCrestPostNumber(null);
        model.setNewInstructedAdvocateDefenceCategory(null);
        model.setSubstitutedInstructedAdvocateTableRowModel(null);
    }

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * first loaded.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        XHIBITConstant.debug("[EditAdvocateWizardExistingInstructedAdvocate] stepInitialise");
        resources = XHIBITConstant.getResourceBundle(XhibitBundles.CounselFacilities);
    }

    /**
     * XPanel implementation of life cycle method, called when (or each time)
     * the screen is left.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        XHIBITConstant.debug("[EditAdvocateWizardExistingInstructedAdvocate] stepDeactivate");
        moveScreenToModel();
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
        XHIBITConstant.debug("[EditAdvocateWizardExistingInstructedAdvocate] stepValidate");
        // Nothing to validate
        moveScreenToModel();
    }

    /**
     * XPanel implementation of life cycle method, called when the state of a
     * widget changes.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        XHIBITConstant.debug("[EditAdvocateWizardExistingInstructedAdvocate] stepUpdateViewState");
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
        XHIBITConstant.debug("[EditAdvocateWizardExistingInstructedAdvocate] stepDeinitialise");

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
        XHIBITConstant.debug("[EditAdvocateWizardExistingInstructedAdvocate] stepActivate");
        moveScreenToModel();
        stepUpdateViewState();
    }
}

