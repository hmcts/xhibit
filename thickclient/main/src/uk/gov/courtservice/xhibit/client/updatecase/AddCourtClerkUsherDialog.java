package uk.gov.courtservice.xhibit.client.updatecase;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAddCourtClerkAction;
import uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAddUsherAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
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
 * @editor Frederik Vandendriessche
 * @version 1.0
 */
public class AddCourtClerkUsherDialog extends XDialog {
    private UpdateCourtStaffModel model;

    private JTextField nameText;

    private JLabel nameLabel;

    private XAction action;

    public AddCourtClerkUsherDialog(Frame frame, UpdateCourtStaffModel model, XAction a) {
        super(frame, "", true);
        this.action = a;
        if (action instanceof OpenAddCourtClerkAction) {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "courtClerkTitle"));
        } else if (action instanceof OpenAddUsherAction) {
            super.setTitle(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "usherTitle"));
        }
        this.model = model;
        this.getContentPane().setLayout(new GridBagLayout());
        nameLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "lblName"));
        Insets insets = new Insets(4, 4, 4, 4);
        this.getContentPane().add(
                nameLabel,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets,
                        0, 0));
        this.getContentPane().add(
                getNameText(),
                new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets,
                        0, 0));
        this.getContentPane().add(
                this.getButtonPanel(),
                new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, insets,
                        0, 0));
        super.pack();
        this.setVisible(true);
    }

    public void stepActivate() {
        stepUpdateViewState();
    }

    public JTextField getNameText() {
        if (nameText == null) {
            nameText = new JTextField();
            nameText.setColumns(20);
            nameText.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
        }
        return nameText;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public UpdateCourtStaffModel getModel() {
        return model;
    }

    public void okClicked(ActionEvent e) throws CSValidationException {
        XHIBITConstant.debug("Ok clicked in AddCourtClerkDialog");

        if (action instanceof OpenAddCourtClerkAction) {
            model.addCourtClerk(getNameText().getText());
        } else if (action instanceof OpenAddUsherAction) {
            model.addUsher(getNameText().getText());
        }

        this.dispose();
    }

    public void cancelClicked(ActionEvent e) {
        XHIBITConstant.debug("Cancel clicked in AddCourtClerkDialog");
        this.dispose();
    }

    public void stepUpdateViewState() {
        this.buttonPanel.okButton.setEnabled(isMandatoryFieldsComplete());
    }

    private boolean isMandatoryFieldsComplete() {
        return (getNameText().getText().trim().length() > 0);
    }
}
