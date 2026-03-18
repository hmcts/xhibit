package uk.gov.courtservice.xhibit.client.schedule.othercase;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.Document;

import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Open Case by Case number
 * </p>
 * <p>
 * Description: Panel to allow user to enter a case number
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */
public class OpenOtherCasePanel extends XPanel {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel lblHelpText = new JLabel();

    private JLabel lblPrompt = new JLabel();

    private JTextField txtCaseNumber;

    private JCheckBox chkReadOnly = new JCheckBox();

    private XDialog myParent;

    // Does the user have case update rights
    private boolean userCanUpdate = false;

    public OpenOtherCasePanel(XDialog parent) {
        myParent = parent;
        stepInitialise();
        jbInit();
    }

    public void stepInitialise() {
        // Initialise access rights
        XhibitApplicationController xac = (XhibitApplicationController) myParent.getParentFrame();
        setUserCanUpdate(XhibitActions.getAction(xac, XhibitActions.UpdateCase).hasEditAccess());
    }

    public void stepActivate() {
        stepUpdateViewState();
    }

    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
    }

    public void stepUpdateViewState() {
        ((OkCancelPanel) (myParent.getButtonPanel())).okButton.setEnabled(txtCaseNumber.getText().length() > 0);
    }

    public void stepValidate() throws uk.gov.courtservice.framework.services.validation.CSValidationException {
        String secondPart = txtCaseNumber.getText().substring(1);
        try {
            Integer.parseInt(secondPart);
        } catch (NumberFormatException ex) {
            txtCaseNumber.requestFocus();
            throw new CSValidationException("gui.addHearing.invalidCaseNumber", "Letters entered in the case number");
        }
    }

    public void stepDeinitialise(boolean update) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        if (update) {
            OpenCaseHelper.OpenCase((XhibitApplicationController) myParent.getParentFrame(), txtCaseNumber.getText(),
                    chkReadOnly.isSelected());
        }
    }

    private void jbInit() {
        this.setLayout(gridBagLayout1);
        lblHelpText.setText(XHIBITConstant.getResource(XhibitBundles.TodaysSchedule, "OtherCaseHelp"));
        lblPrompt.setText(XHIBITConstant.getResource(XhibitBundles.TodaysSchedule, "OtherCaseLabel"));
        chkReadOnly.setText(XHIBITConstant.getResource(XhibitBundles.TodaysSchedule, "Read_Only"));
        chkReadOnly.setSelected(true);

        if (!userCanUpdate) {
            // if the user can't update then don't let them uncheck the
            // read only button
            chkReadOnly.setEnabled(false);
        }

        this.add(lblHelpText, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 5));
        this.add(lblPrompt, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getCaseNumber(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(chkReadOnly, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    private JTextField getCaseNumber() {
        if (txtCaseNumber == null) {
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability.upperCase(),
                    Capability.limitedText(9) });

            JTextField tf = JTextFieldFactory.getTextField(doc);
            tf.setMinimumSize(new Dimension(100, XHIBITConstant.getLineHeight()));
            tf.setPreferredSize(new Dimension(100, XHIBITConstant.getLineHeight()));
            tf.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
            txtCaseNumber = tf;
        }
        return txtCaseNumber;
    }

    protected void setUserCanUpdate(boolean hasAccess) {
        userCanUpdate = hasAccess;
    }
}
