package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDatePickerPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

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

public class PrintCourtLogPanel extends XPanel {
    private PrintCourtLogModel model;

    private OkCancelPanel buttonPanel;

    private JLabel caseLabel;

    private JLabel printOptionsLabel;

    private JLabel caseText;
    
    private JLabel fromLabel;
    
    private JLabel toLabel;

    private ButtonGroup printOptionsRadioButtonGroup = null;

    private JRadioButton printAllRadio = null;

    private JRadioButton printDayRadio = null;
    
    private JRadioButton printRangeRadio = null;

    private XDatePickerPanel datePanel;
    
    private XDatePickerPanel date1Panel;
    
    private XDatePickerPanel date2Panel;

    private ApplicationCaseModel acm;

    public PrintCourtLogPanel() {
        super();
    }

    public PrintCourtLogPanel(XDialog parent, PrintCourtLogModel model) throws CSRecoverableException {
        super();

        this.buttonPanel = (OkCancelPanel) parent.getButtonPanel();
        this.model = model;
        this.acm = model.getXhibitApplicationController().getApplicationCaseModel();

        stepInitialise();

        jbInit();

        stepActivate();
    }

    public void stepInitialise() throws CSRecoverableException {
        // set the model scheduled hearing value
        ArrayList currentHearing = new ArrayList();
        if (acm.getScheduledHearingDateFrom() != null) {
            currentHearing.add(new Date(acm.getScheduledHearingDateFrom().getTime()));
        }
        model.setScheduledHearingDates(currentHearing);
    }

    private void jbInit() throws CSRecoverableException {
        datePanel = new XDatePickerPanel(this, getDistinctHearingDates(), acm.getScheduledHearingDateFrom());
        date1Panel = new XDatePickerPanel(this, getDistinctHearingDates(), acm.getScheduledHearingDateFrom());
        date2Panel = new XDatePickerPanel(this, getDistinctHearingDates(), acm.getScheduledHearingDateFrom());
        datePanel.setDateEditable(false);
        datePanel.setDateEnabled(false);
        date1Panel.setDateEditable(false);
        date2Panel.setDateEditable(false);

        //this.add(getPrintDayRadio(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        //this.add(datePanel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        
        JPanel dateOptionsPanel = new JPanel(new GridBagLayout());
        // Per day radio button row
        dateOptionsPanel.add(getPrintDayRadio(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        dateOptionsPanel.add(datePanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        
        // Range radio button row
        dateOptionsPanel.add(getPrintRangeRadio(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        dateOptionsPanel.add(date1Panel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        dateOptionsPanel.add(getToLabel(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        dateOptionsPanel.add(date2Panel, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        
        // Entire log radio button row
        dateOptionsPanel.add(getPrintAllRadio(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        //this.add(getPrintAllRadio(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        
        this.setLayout(new GridBagLayout());
        this.add(getCaseLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getCaseText(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPrintOptionsLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(dateOptionsPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        getPrintOptionsRadioButtonGroup().add(getPrintDayRadio());
        getPrintOptionsRadioButtonGroup().add(getPrintRangeRadio());
        getPrintOptionsRadioButtonGroup().add(getPrintAllRadio());
    }

    private ButtonGroup getPrintOptionsRadioButtonGroup() {
        if (printOptionsRadioButtonGroup == null) {
            printOptionsRadioButtonGroup = new ButtonGroup();
        }

        return printOptionsRadioButtonGroup;
    }

    private JLabel getCaseText() {
        if (caseText == null) {
            caseText = new JLabel();
        }
        return caseText;
    }

    /*
     * void subEventCb_itemStateChanged() { stepUpdateViewState(); }
     */

    private JLabel getCaseLabel() {
        if (caseLabel == null) {
            caseLabel = new JLabel();
            caseLabel.setText(XHIBITConstant.getResource(XHIBITConstant.getResourceBundle(XhibitBundles.PrintCourtLog), "lblCaseNumber"));
        }

        return caseLabel;
    }

    private JLabel getPrintOptionsLabel() {
        if (printOptionsLabel == null) {
            printOptionsLabel = new JLabel();
            printOptionsLabel.setText(XHIBITConstant.getResource(XHIBITConstant.getResourceBundle(XhibitBundles.PrintCourtLog), "lblPrintOptions"));
        }

        return printOptionsLabel;
    }
    
    private JLabel getToLabel() {
        if (toLabel == null) {
            toLabel = new JLabel();
            toLabel.setText(XHIBITConstant.getResource(XHIBITConstant.getResourceBundle(XhibitBundles.PrintCourtLog), "lblRange2"));
        }

        return toLabel;
    }

    private JRadioButton getPrintAllRadio() {
        if (printAllRadio == null) {
            printAllRadio = new JRadioButton();
            printAllRadio.setSelected(false);
            printAllRadio.setToolTipText(XHIBITConstant.getResource(XHIBITConstant.getResourceBundle(XhibitBundles.PrintCourtLog), "ttAllDays"));
            printAllRadio.setActionCommand(PrintCourtLogModel.ALL);
            printAllRadio.setMnemonic('A');
            printAllRadio.setText(XHIBITConstant.getResource(XHIBITConstant.getResourceBundle(XhibitBundles.PrintCourtLog), "lblAllDays"));
            printAllRadio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    printOptionsRadio_actionPerformed();
                }
            });
        }

        return printAllRadio;
    }

    private JRadioButton getPrintDayRadio() {
        if (printDayRadio == null) {
            printDayRadio = new JRadioButton();
            printDayRadio.setSelected(true);
            printDayRadio.setToolTipText(XHIBITConstant.getResource(XHIBITConstant.getResourceBundle(XhibitBundles.PrintCourtLog), "ttSelectedDay"));
            printDayRadio.setActionCommand(PrintCourtLogModel.SELECTEDDAY);
            printDayRadio.setMnemonic('S');
            printDayRadio.setText(XHIBITConstant.getResource(XHIBITConstant.getResourceBundle(XhibitBundles.PrintCourtLog), "lblSelectedDay"));
            printDayRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    printOptionsRadio_actionPerformed();
                }
            });
        }

        return printDayRadio;
    }
    
    private JRadioButton getPrintRangeRadio() {
        if (printRangeRadio == null) {
            printRangeRadio = new JRadioButton();
            printRangeRadio.setSelected(true);
            printRangeRadio.setToolTipText(XHIBITConstant.getResource(XHIBITConstant.getResourceBundle(XhibitBundles.PrintCourtLog), "ttSelectedRange"));
            printRangeRadio.setActionCommand(PrintCourtLogModel.SELECTEDFROM);
            printRangeRadio.setMnemonic('S');
            printRangeRadio.setText(XHIBITConstant.getResource(XHIBITConstant.getResourceBundle(XhibitBundles.PrintCourtLog), "lblRange1"));
            printRangeRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    printOptionsRadio_actionPerformed();
                }
            });
        }

        return printRangeRadio;
    }

    void printOptionsRadio_actionPerformed() {
        stepUpdateViewState();
    }

    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();

        stepUpdateViewState();
    }

    private void moveModelToScreen() {
        getCaseText().setText(acm.getCaseType() + acm.getCaseNumber());
        if (acm.getScheduledHearingDateFrom() != null) {
            datePanel.setDate(new Date(acm.getScheduledHearingDateFrom().getTime()));
        }
    }

    public void stepUpdateViewState() {
        // set up date panel panel according to option button selected
        datePanel.setDateEnabled(PrintCourtLogModel.SELECTEDDAY.equals(getPrintOptionsRadioButtonGroup().getSelection().getActionCommand()));
        datePanel.setDateEditable(PrintCourtLogModel.SELECTEDDAY.equals(getPrintOptionsRadioButtonGroup().getSelection().getActionCommand()));
        
        date1Panel.setDateEnabled(PrintCourtLogModel.SELECTEDFROM.equals(getPrintOptionsRadioButtonGroup().getSelection().getActionCommand()));
        date1Panel.setDateEditable(PrintCourtLogModel.SELECTEDFROM.equals(getPrintOptionsRadioButtonGroup().getSelection().getActionCommand()));
        date2Panel.setDateEnabled(PrintCourtLogModel.SELECTEDFROM.equals(getPrintOptionsRadioButtonGroup().getSelection().getActionCommand()));
        date2Panel.setDateEditable(PrintCourtLogModel.SELECTEDFROM.equals(getPrintOptionsRadioButtonGroup().getSelection().getActionCommand()));

        buttonPanel.okButton.setEnabled(isMandatoryFieldsCompleted());
    }

    private boolean isMandatoryFieldsCompleted() {
        String option = getPrintOptionsRadioButtonGroup().getSelection().getActionCommand();
        return PrintCourtLogModel.ALL.equals(option)
                || (PrintCourtLogModel.SELECTEDDAY.equals(option) && datePanel.getDate() != null
                || (getPrintRangeRadio().isSelected() && date1Panel.getDropDownDate().getSelectedIndex() >= 0
					&& date1Panel.getDropDownDate().getSelectedIndex() >= 0
					&& date2Panel.getDropDownDate().getSelectedIndex() >= date1Panel.getDropDownDate().getSelectedIndex()));
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // no implementation required...
    }

    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
        /*
         * if( ALL == model.getSelectedOption( ) ) { // set the date to null if
         * the all option is selected model.setlogRequestedForDates(null); }
         * else { ArrayList logDate = new ArrayList( ); logDate.add(
         * model.getSelectedDate( ) ); model.setlogRequestedForDates( logDate ); }
         */
        model.printModel();
    }

    private void moveScreenToModel() {
    	if (getPrintOptionsRadioButtonGroup().getSelection().getActionCommand().equals(PrintCourtLogModel.SELECTEDFROM)) {
    		model.setSelectedDateFrom(date1Panel.getDate());
    		model.setSelectedDateTo(date2Panel.getDate());
    	} else {
    		model.setSelectedDate(datePanel.getDate());
    	}
        model.setSelectedOption(getPrintOptionsRadioButtonGroup().getSelection().getActionCommand());
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // no implementation required...
    }

    public JComponent getFirstEnterableComponent() {
        // set the Day Option as the first item for focus
        return getPrintDayRadio();
    }

    /**
     * Function to return a list of distinct hearing dates (sorted) to populate
     * the Hearing Dates drop down list
     * 
     * @return
     * @throws CSRecoverableException
     */
    private ArrayList getDistinctHearingDates() throws CSRecoverableException {
        ArrayList hearingDates = (ArrayList) model.getXhibitApplicationController().getApplicationCaseModel().getAllScheduledHearingDatesForCase(false);
        return hearingDates;
    }
}
