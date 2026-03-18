package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JTextField;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
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
public class AddEditWitnessPanel extends XPanel {
    private AddEditWitnessModel model;

    private OkCancelPanel buttonPanel;

    private JLabel eventNameLabel;

    private JTextField nameText;

    private JLabel nameLabel;

    private ResourceBundle taskResources = XHIBITConstant.getResourceBundle(XhibitBundles.WitnessSworn);

    public AddEditWitnessPanel(OkCancelPanel buttonPanel, AddEditWitnessModel model) {
        super();
        this.model = model;
        this.buttonPanel = buttonPanel;

        stepInitialise();

        jbInit();

        stepActivate();
    }

    public AddEditWitnessPanel() {
        // no implementation required...
    }

    private void jbInit() {
        this.setLayout(new GridBagLayout());
        this.add(getEventNameLabel(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(5, 2, 25, 2), 0, 0));
        this.add(getNameLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getNameText(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    }

    public void stepInitialise() {
        // No initialisation necessary
    }

    public void stepUpdateViewState() {
        buttonPanel.okButton.setEnabled(isMandatoryFieldsCompleted());
    }

    private JTextField getNameText() {
        if (nameText == null) {
            nameText = new JTextField();
            nameText.setColumns(15);
            nameText.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
            nameText.setToolTipText(XHIBITConstant.getResource(taskResources, "ttWitnessName"));
        }

        return nameText;
    }

    private JLabel getNameLabel() {
        if (nameLabel == null) {
            nameLabel = new JLabel();
            nameLabel.setText(XHIBITConstant.getResource(taskResources, "witnessName"));
        }

        return nameLabel;
    }

    private JLabel getEventNameLabel() {
        if (eventNameLabel == null) {
            eventNameLabel = new PanelTitleLabel(XHIBITConstant.getResource(taskResources, "eventNameLabel"));
        }

        return eventNameLabel;
    }

    private boolean isMandatoryFieldsCompleted() {
        return (getNameText().getText().trim().length() > 0);
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            model.setSaveClicked(true);
            model.setWitnessName(getNameText().getText());
        }
    }

    public void stepDeactivate() throws CSRecoverableException {
        // no implementation required...
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // no implementation required...
    }

    public void stepActivate() {
        stepUpdateViewState();
    }
}
