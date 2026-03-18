package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;
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
 * @author unascribed
 * @version 1.0
 */

public class PrintChargesPanel extends XPanel {
    private ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);

    private PrintChargesModel printChargesModel;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private ButtonGroup printOptionsRadioButtonGroup = null;

    private JRadioButton printChargesRb = null;

    private JRadioButton printChargesAndLogRb = null;

    private JLabel promptLabel = null;

    private JLabel printChargesLabel = null;

    private JLabel printChargesAndLogLabel = null;

    public PrintChargesPanel(PrintChargesModel printChargesModel) throws CSRecoverableException {
        this.printChargesModel = printChargesModel;
        stepInitialise();

        jbInit();

        stepActivate();
    }

    private void jbInit() {
        this.setLayout(gridBagLayout1);
        this.setMinimumSize(new Dimension(250, 150));
        this.setPreferredSize(new Dimension(250, 150));

        getPrintOptionsRadioButtonGroup().add(getPrintChargesRb());
        getPrintOptionsRadioButtonGroup().add(getPrintChargesAndLogRb());

        this.add(getPromptLabel(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPrintChargesLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPrintChargesRb(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPrintChargesAndLogLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPrintChargesAndLogRb(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    }

    private ButtonGroup getPrintOptionsRadioButtonGroup() {
        if (printOptionsRadioButtonGroup == null) {
            printOptionsRadioButtonGroup = new ButtonGroup();
        }
        return printOptionsRadioButtonGroup;
    }

    private PrintChargesModel getPrintChargesModel() {
        return printChargesModel;
    }

    private JRadioButton getPrintChargesRb() {
        if (printChargesRb == null) {
            printChargesRb = new JRadioButton();
            printChargesRb.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) {
                    // printChargesModel.setPrintSelection(PrintChargesModel.PRINT_CHARGES);
                    getPrintChargesModel().setPrintSelection(PrintChargesModel.PRINT_CHARGES);
                }
            });
        }
        return printChargesRb;
    }

    private JRadioButton getPrintChargesAndLogRb() {
        if (printChargesAndLogRb == null) {
            printChargesAndLogRb = new JRadioButton();
            printChargesAndLogRb.setSelected(true);
            printChargesAndLogRb.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) {
                    // printChargesModel.setPrintSelection(PrintChargesModel.PRINT_CHARGES_AND_LOG);
                    getPrintChargesModel().setPrintSelection(PrintChargesModel.PRINT_CHARGES_AND_LOG);
                }
            });
        }
        return printChargesAndLogRb;
    }

    private JLabel getPromptLabel() {
        if (promptLabel == null) {
            promptLabel = new JLabel();
            promptLabel.setText(XHIBITConstant.getResource(resources, "PrintCharges.Prompt"));
        }
        return promptLabel;
    }

    private JLabel getPrintChargesLabel() {
        if (printChargesLabel == null) {
            printChargesLabel = new JLabel();
            printChargesLabel.setText(XHIBITConstant.getResource(resources, "PrintCharges.PrintCharges"));
        }
        return printChargesLabel;
    }

    private JLabel getPrintChargesAndLogLabel() {
        if (printChargesAndLogLabel == null) {
            printChargesAndLogLabel = new JLabel();
            printChargesAndLogLabel.setText(XHIBITConstant.getResource(resources, "PrintCharges.PrintChargesAndLog"));
        }
        return printChargesAndLogLabel;
    }

    public void stepInitialise() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        getPrintChargesModel().setPrintSelection(PrintChargesModel.PRINT_CHARGES_AND_LOG);
    }

    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
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
        stepUpdateViewState();
    }
}