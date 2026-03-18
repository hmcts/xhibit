package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDatePickerPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class OpenOtherDaysLogPanel extends XPanel {
    private String resources = XhibitBundles.OpenOtherDaysLog;

    private OpenOtherDaysLogModel model;

    private OkCancelPanel buttonPanel;

    private JLabel caseLabel;

    private JLabel printLogLabel;

    private JLabel toLabel;

    private JTextField caseText = null;

    private ButtonGroup selectedDateGroup = null;

    private JRadioButton selectDayRadio = null;
    
    private JRadioButton selectRangeRadio = null;

    private JRadioButton selectAllRadio = null;

    private XDatePickerPanel date1Panel;
    
    private XDatePickerPanel date2Panel;
    
    private XDatePickerPanel date3Panel;

    /**
     * Default constructor
     */
    public OpenOtherDaysLogPanel() {
        super();
    }

    /**
     * Public constructor
     * 
     * @param parent -
     *            the dialog that invoked this panel
     * @param model -
     *            a reference to the data used in this screen
     * @throws CSRecoverableException
     */
    public OpenOtherDaysLogPanel(XDialog parent, OpenOtherDaysLogModel model) throws CSRecoverableException {
        super();

        this.buttonPanel = (OkCancelPanel) parent.getButtonPanel();
        this.model = model;

        stepInitialise();

        jbInit();

        stepActivate();
    }

    /**
     * Life-cycle method used to save non-volatile data in the model
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        model.setScheduledHearingDates((ArrayList) model.getXhibitApplicationController().getApplicationCaseModel().getAllScheduledHearingDatesForCase(true));
    }

    /**
     * Manages the screen widgets and places them on the screen
     */
    private void jbInit() {
        date1Panel = new XDatePickerPanel(this, model.getScheduledHearingDates());
        date2Panel = new XDatePickerPanel(this, model.getScheduledHearingDates());
        date3Panel = new XDatePickerPanel(this, model.getScheduledHearingDates());

        JPanel dateOptionsPanel = new JPanel(new GridBagLayout());
        // Per day radio button row
        dateOptionsPanel.add(getSelectDayRadio(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        dateOptionsPanel.add(date1Panel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        
        // Range radio button row
        dateOptionsPanel.add(getSelectRangeRadio(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        dateOptionsPanel.add(date2Panel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        dateOptionsPanel.add(getToLabel(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        dateOptionsPanel.add(date3Panel, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        
        // Entire log radio button row
        dateOptionsPanel.add(getSelectAllRadio(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));

        this.setLayout(new GridBagLayout());
        this.add(getCaseLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getCaseText(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPrintLogLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(dateOptionsPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        getSelectedDateGroup().add(getSelectDayRadio());
        getSelectedDateGroup().add(getSelectRangeRadio());
        getSelectedDateGroup().add(getSelectAllRadio());
    }

    private ButtonGroup getSelectedDateGroup() {
        if (selectedDateGroup == null) {
            selectedDateGroup = new ButtonGroup();
        }

        return selectedDateGroup;
    }

    private JLabel getCaseLabel() {
        if (caseLabel == null) {
            caseLabel = new JLabel();
            caseLabel.setText(XHIBITConstant.getResource(resources, "lblCaseNumber"));
        }

        return caseLabel;
    }

    private JTextField getCaseText() {
        if (caseText == null) {
            caseText = new JTextField();
            caseText.setMinimumSize(new Dimension(100, XHIBITConstant.getLineHeight()));
            caseText.setPreferredSize(new Dimension(100, XHIBITConstant.getLineHeight()));
            enableTextField(caseText, false);
        }

        return caseText;
    }

    private JLabel getPrintLogLabel() {
        if (printLogLabel == null) {
        	printLogLabel = new JLabel();
        	printLogLabel.setText(XHIBITConstant.getResource(resources, "lblDateOptions"));
        }

        return printLogLabel;
    }
        
    private JLabel getToLabel() {
        if (toLabel == null) {
            toLabel = new JLabel();
            toLabel.setText(XHIBITConstant.getResource(resources, "lblRange2"));
        }

        return toLabel;
    }

    private JRadioButton getSelectDayRadio() {
        if (selectDayRadio == null) {
            selectDayRadio = new JRadioButton();
            selectDayRadio.setSelected(true);
            selectDayRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblDay"));
            selectDayRadio.setActionCommand("ALL");
            selectDayRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmDay").charAt(0));
            selectDayRadio.setText(XHIBITConstant.getResource(resources, "lblDay"));
            selectDayRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedDateGroup_actionPerformed();
                }
            });
        }

        return selectDayRadio;
    }
    
    private JRadioButton getSelectRangeRadio() {
        if (selectRangeRadio == null) {
            selectRangeRadio = new JRadioButton();
            selectRangeRadio.setSelected(true);
            selectRangeRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblRange1"));
            selectRangeRadio.setActionCommand("ALL");
            selectRangeRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmRange").charAt(0));
            selectRangeRadio.setText(XHIBITConstant.getResource(resources, "lblRange1"));
            selectRangeRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedDateGroup_actionPerformed();
                }
            });
        }

        return selectRangeRadio;
    }

    private JRadioButton getSelectAllRadio() {
        if (selectAllRadio == null) {
            selectAllRadio = new JRadioButton();
            selectAllRadio.setSelected(false);
            selectAllRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblAll"));
            selectAllRadio.setActionCommand("DAY");
            selectAllRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmAll").charAt(0));
            selectAllRadio.setText(XHIBITConstant.getResource(resources, "lblAll"));
            selectAllRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedDateGroup_actionPerformed();
                }
            });
        }

        return selectAllRadio;
    }

    void selectedDateGroup_actionPerformed() {
        stepUpdateViewState();
    }

    /**
     * Life-cycle method to manage the movement of data from the model to the
     * screen
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();

        stepUpdateViewState();
    }

    /**
     * Moves data from the model to the screen
     */
    private void moveModelToScreen() {
        getCaseText().setText(
                model.getXhibitApplicationController().getApplicationCaseModel().getCaseType()
                        + model.getXhibitApplicationController().getApplicationCaseModel().getCaseNumber());
    }

    /**
     * Life-cycle method to enable/disable screen components
     */
    public void stepUpdateViewState() {
        date1Panel.setDateEnabled(getSelectDayRadio().isSelected());
        date2Panel.setDateEnabled(getSelectRangeRadio().isSelected());
        date3Panel.setDateEnabled(getSelectRangeRadio().isSelected());
        buttonPanel.okButton.setEnabled(isMandatoryFieldsCompleted());
    }

    /**
     * Determines if all mandatory fields have been completed
     * 
     * @return true if either the "All" radio button is selected OR the "Day"
     *         radio button is selected and a date has been picked
     */
    private boolean isMandatoryFieldsCompleted() {
        return ((getSelectAllRadio().isSelected())
        			|| (getSelectDayRadio().isSelected() && date1Panel.getDropDownDate().getSelectedIndex() >= 0)
        			|| (getSelectRangeRadio().isSelected() && date2Panel.getDropDownDate().getSelectedIndex() >= 0
        					&& date3Panel.getDropDownDate().getSelectedIndex() >= 0
        					&& date3Panel.getDropDownDate().getSelectedIndex() >= date2Panel.getDropDownDate().getSelectedIndex()));
    }

    /**
     * Life-cycle method to validate any data on the screen
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // no implementation required...
    }

    /**
     * Lify-cycle method to manage the movement of data from the screen to the
     * model
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }

    /**
     * Moves data from the screen to the model
     */
    private void moveScreenToModel() {
        model.setShowAllDaysLogs(getSelectAllRadio().isSelected());
        model.setShowRangeLogs(getSelectRangeRadio().isSelected());

        if (model.isShowAllDaysLogs()) {
            int index = model.getScheduledHearingDates().size() - 1;
            model.setSelectedDate((Date) model.getScheduledHearingDates().get(index));
        } else if (model.isShowRangeLogs()) {
            model.setSelectedDateFrom(date2Panel.getDate());
            model.setSelectedDateTo(date3Panel.getDate());
        } else {
        	model.setSelectedDate(date1Panel.getDate());
        }
    }

    /**
     * Life-cycle method to process the users request based upon whether or not
     * the Apply/OK or Cancel button was clicked
     * 
     * @param update -
     *            true if Apply/OK clicked
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
        	if (model.isShowRangeLogs()) {
        		model.getXhibitApplicationController().openCase(
                        model.getXhibitApplicationController().getApplicationCaseModel().getShvForDate(model.getSelectedDateFrom()),
                        model.getXhibitApplicationController().getApplicationCaseModel().isInEditMode(FunctionList.ECourtLog),
                        model.isShowAllDaysLogs(), model.getSelectedDateFrom(), model.getSelectedDateTo());
        	} else {
        		model.getXhibitApplicationController().openCase(
                    model.getXhibitApplicationController().getApplicationCaseModel().getShvForDate(model.getSelectedDate()),
                    model.getXhibitApplicationController().getApplicationCaseModel().isInEditMode(FunctionList.ECourtLog),
                    model.isShowAllDaysLogs());
        	}
        }
    }

    /**
     * Utility method to enable/disable text field screen components
     * 
     * @param textField -
     *            the screen component to act upon
     * @param state -
     *            true to enable the text field
     */
    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setBackground((state ? Color.white : SystemColor.text));
        if (state == false)
            textField.setText("");
    }
}
