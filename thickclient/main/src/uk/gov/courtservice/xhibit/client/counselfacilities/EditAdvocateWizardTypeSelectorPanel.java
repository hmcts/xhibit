package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.counselfacilities.InstructedAdvocateHelper;

/**
 * <p>
 * Title: Screen for choosing the type of legal representative
 * </p>
 * <p>
 * Description: Presents a radio button list of legal representatives.
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

public class EditAdvocateWizardTypeSelectorPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private EditAdvocateWizardModel model = null;

    private ResourceBundle resources = null;

    private EditAdvocateWizardController controller = null;

    private ButtonGroup buttonGroup = new ButtonGroup();

    private static final int INSTRUCTED_BARRISTER = 0;

    private static final int SUBSTITUTE_BARRISTER = 1;

    private JRadioButton[] buttons = { null, null };

    private String[] resourceSuffix = { 
            "InstructedBarrister", 
            "SubstituteBarrister" };

    private String[] actionCommand = { 
            Integer.toString(INSTRUCTED_BARRISTER), 
            Integer.toString(SUBSTITUTE_BARRISTER) };

    private String[] barristerType = { 
            InstructedAdvocateHelper.INSTRUCTED_ADVOCATE_FLAG,
            InstructedAdvocateHelper.SUBSTITUE_ADVOCATE_FLAG };
    
    
    public EditAdvocateWizardTypeSelectorPanel(
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
 
        add(getButton(INSTRUCTED_BARRISTER), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(getButton(SUBSTITUTE_BARRISTER), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    }

    
    private JRadioButton getButton(int index) {
        if (buttons[index] == null) {
            String buttonText = XHIBITConstant.getResource(resources, "rb" + resourceSuffix[index]);
            JRadioButton button = new JRadioButton();
            button.setToolTipText(buttonText);
            button.setSelected(false);
            button.setText(buttonText);
            button.setActionCommand(actionCommand[index]);
            String mnemonicText = XHIBITConstant.getResource(resources, "mnm" + resourceSuffix[index]);
            if (mnemonicText != null && mnemonicText.length() > 0) {
                button.setMnemonic(mnemonicText.charAt(0));
            }
            button.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        moveScreenToModel();
                        stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITConstant.handleError(csre);
                    }
                }
            });
            buttons[index] = button;
            buttonGroup.add(button);
        }

        return buttons[index];
    }

    /**
     * Checks if all required fields have been populated.
     * 
     * @return true if all mandatory fields have been completed.
     */
    protected boolean isMandatoryFieldsCompleted() {
        return (buttonGroup.getSelection() != null);
    }

    /**
     * Saves the data on the screen to the FindRepresentationWizardModel.
     * 
     * @throws CSRecoverableException
     */
    private void moveScreenToModel() {
        XHIBITConstant.debug("moveScreenToModel");

        if (buttonGroup.getSelection() != null) {
            final int index = Integer.parseInt(buttonGroup.getSelection().getActionCommand());
            model.setBarristerType(barristerType[index]);
        } else {
            model.setBarristerType(null);
        }
    }

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * first loaded.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        XHIBITConstant.debug("[EditAdvocateWizardTypeSelectorPanel] stepInitialise");
        resources = XHIBITConstant.getResourceBundle(XhibitBundles.CounselFacilities);
    }

    /**
     * XPanel implementation of life cycle method, called when (or each time)
     * the screen is left.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        XHIBITConstant.debug("[EditAdvocateWizardTypeSelectorPanel] stepDeactivate");
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
        XHIBITConstant.debug("[EditAdvocateWizardTypeSelectorPanel] stepValidate");
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
        XHIBITConstant.debug("[EditAdvocateWizardTypeSelectorPanel] stepUpdateViewState");
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
        XHIBITConstant.debug("[EditAdvocateWizardTypeSelectorPanel] stepDeinitialise");

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
        XHIBITConstant.debug("[EditAdvocateWizardTypeSelectorPanel] stepActivate");
        stepUpdateViewState();
    }
}
