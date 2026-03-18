package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva
 * @version 1.0
 */

public class PrintHearingRecordPanel extends XPanel {
    HearingRecordModel model;

    private JTextField caseText;

    private JRadioButton selectedHearingRb;

    private JRadioButton allHearingsRb;

    public PrintHearingRecordPanel(HearingRecordModel model) {
        this.model = model;
        this.setMaximumSize(new Dimension(340, 100));
        this.setMinimumSize(new Dimension(340, 100));
        this.setPreferredSize(new Dimension(340, 100));
        try {
            stepInitialise();
            jbInit();
            stepActivate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jbInit() {
        this.setLayout(new GridBagLayout());

        this.caseText = new JTextField(10);
        this.caseText.setEditable(false);
        this.selectedHearingRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord,
                "selectedHearing"));
        this.allHearingsRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "linkedHearings"));

        ButtonGroup bg = new ButtonGroup();
        bg.add(selectedHearingRb);
        bg.add(allHearingsRb);

        this.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "case")), new GridBagConstraints(0,
                0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "printOptions")),
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4), 0, 0));

        this.add(caseText, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(selectedHearingRb, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(allHearingsRb, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    }

    public void stepInitialise() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }

    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        if (this.selectedHearingRb.isSelected()) {
            model.setHearingsToPrint(HearingRecordModel.PRINT_SELECTED);
        } else if (this.allHearingsRb.isSelected()) {
            model.setHearingsToPrint(HearingRecordModel.PRINT_ALL);
        }
    }

    public void stepValidate() throws uk.gov.courtservice.framework.exception.CSRecoverableException,
            uk.gov.courtservice.framework.services.validation.CSValidationException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }

    public void stepUpdateViewState() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }

    public void stepDeinitialise(boolean update) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }

    public void stepActivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        this.caseText.setText(model.getCaseNumber());
        this.selectedHearingRb.setSelected(true);
    }
}