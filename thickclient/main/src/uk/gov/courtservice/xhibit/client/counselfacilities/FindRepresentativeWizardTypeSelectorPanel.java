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

public class FindRepresentativeWizardTypeSelectorPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private FindLegalRepresentativeModel model = null;
    
    private boolean legalAidOrderGranted;

    private ResourceBundle resources = null;

    private FindRepresentativeWizardController controller = null;

    private ButtonGroup buttonGroup = new ButtonGroup();

    private static final int INSTRUCTED_BARRISTER = 0;

    private static final int SUBSTITUTE_BARRISTER = 1;

    private static final int SOLICITOR = 2;

    private static final int IN_PERSON = 3;
    
    private static final int BARRISTER = 4;
    
    private static final int NON_ATTENDANCE = 5;

    private JRadioButton[] buttons = { null, null, null, null, null, null };

    private String[] resourceSuffix = { 
            "InstructedBarrister", 
            "SubstituteBarrister", 
            "Solicitor", 
            "InPerson",
            "Barrister",
            "NonAttendance"};

    private String[] actionCommand = { 
            Integer.toString(INSTRUCTED_BARRISTER), 
            Integer.toString(SUBSTITUTE_BARRISTER),
            Integer.toString(SOLICITOR),
            Integer.toString(IN_PERSON),
            Integer.toString(BARRISTER),
            Integer.toString(NON_ATTENDANCE)};

    private String[] radioType = { 
            CounselFacilitiesHelper.BARRADIO, 
            CounselFacilitiesHelper.BARRADIO,
            CounselFacilitiesHelper.SOLRADIO, 
            CounselFacilitiesHelper.INPRADIO,
            CounselFacilitiesHelper.BARRADIO,
            CounselFacilitiesHelper.NONATTRADIO};

    private String[] barristerType = { 
            InstructedAdvocateHelper.INSTRUCTED_ADVOCATE_FLAG,
            InstructedAdvocateHelper.SUBSTITUE_ADVOCATE_FLAG, 
            null, 
            null, 
            null,
            null};
    
    
    public FindRepresentativeWizardTypeSelectorPanel(
            FindRepresentativeWizardController controller,
            FindLegalRepresentativeModel model,
            boolean legalAidOrderGranted) 
    throws CSRecoverableException {
        super();
        this.controller = controller;
        this.model = model;
        this.legalAidOrderGranted = legalAidOrderGranted;

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
        if (legalAidOrderGranted) {
            add(getButton(INSTRUCTED_BARRISTER), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
            add(getButton(SUBSTITUTE_BARRISTER), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        } else {
            add(getButton(BARRISTER), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        }
        
        add(getButton(SOLICITOR), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(getButton(IN_PERSON), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(getButton(NON_ATTENDANCE), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
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
    private void moveScreenToModel() throws CSRecoverableException {
        XHIBITConstant.debug("moveScreenToModel");

        if (buttonGroup.getSelection() != null) {
            final int index = Integer.parseInt(buttonGroup.getSelection().getActionCommand());
            model.setRepTypeRadio(radioType[index]);
            model.setBarristerType(barristerType[index]);
        } else {
            model.setRepTypeRadio(null);
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
        XHIBITConstant.debug("[FindRepresentativeWizardTypeSelectorPanel] stepInitialise");
        resources = XHIBITConstant.getResourceBundle(XhibitBundles.CounselFacilities);
    }

    /**
     * XPanel implementation of life cycle method, called when (or each time)
     * the screen is left.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        XHIBITConstant.debug("[FindRepresentativeWizardTypeSelectorPanel] stepDeactivate");
        
        if (model.isInPersonSelected()) {
            FindLegalRepresentativeTableRowModel item = new FindLegalRepresentativeTableRowModel();
            item.setFullName(XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "lblInPerson"));
            final int index = Integer.parseInt(buttonGroup.getSelection().getActionCommand());
            item.setLegalRepType(radioType[index]);
            model.setFindLegalRepresentativeTableRowModel(item);
            model.setDisableInPerson(false);
        } else if (model.isNonAttendanceSelected()) {
            FindLegalRepresentativeTableRowModel item = new FindLegalRepresentativeTableRowModel();
            item.setFullName(XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "lblNonAttendance"));
            final int index = Integer.parseInt(buttonGroup.getSelection().getActionCommand());
            item.setLegalRepType(radioType[index]);
            model.setFindLegalRepresentativeTableRowModel(item);
            model.setDisableNonAttendance(false);
        }
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
        XHIBITConstant.debug("[FindRepresentativeWizardTypeSelectorPanel] stepValidate");
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
        XHIBITConstant.debug("[FindRepresentativeWizardTypeSelectorPanel] stepUpdateViewState");
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
        XHIBITConstant.debug("[FindRepresentativeWizardTypeSelectorPanel] stepDeinitialise");

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
        XHIBITConstant.debug("[FindRepresentativeWizardTypeSelectorPanel] stepActivate");
        stepUpdateViewState();
    }
}