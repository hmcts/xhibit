package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.WizardButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: CounselSignInPanel
 * </p>
 * <p>
 * Description: The panel for Counsel sign in.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully( EDS )
 * @version 1.0
 */

public class CounselSignInPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private String resources = XhibitBundles.CounselFacilities;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel nameLbl = null;

    private JTextField nameText = null;

    private JButton findBtn = null;

    private JLabel chambersLbl = null;

    private JTextField chambersText = null;

    private JLabel addressLine01Lbl = null;

    private JTextField addressLine01Text = null;

    private JTextField addressLine02Text = null;

    private JLabel townLbl = null;

    private JTextField townText = null;

    private JTextField countyText = null;

    private JTextField postCodeText = null;

    private JLabel countyLbl = null;

    private JLabel postCodeLbl = null;

    private XWizardDialog parent;

    private CounselSignInModel model;

    private WizardButtonPanel buttonPanel;

    public CounselSignInPanel(XWizardDialog parent, CounselSignInModel model) throws CSRecoverableException {
        super();

        this.parent = parent;
        this.model = model;
        this.buttonPanel = parent.getButtonPanel();

        stepInitialise();
        jbInit();
        stepActivate();
    }

    /**
     * Obtain reference data and/or data that is required to be available before
     * the screen is built
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        // empty
    }

    /**
     * Add components to the screen
     */
    void jbInit() {
        this.setLayout(gridBagLayout1);

        this.add(getNameLbl(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getFindBtn(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getNameText(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getChambersLbl(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getChambersText(), new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getAddressLine01Lbl(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getAddressLine01Text(), new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getAddressLine02Text(), new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getTownLbl(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getTownText(), new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getCountyLbl(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getCountyText(), new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPostCodeLbl(), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPostCodeText(), new GridBagConstraints(1, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    }

    private JLabel getNameLbl() {
        if (nameLbl == null) {
            nameLbl = new JLabel();
            nameLbl.setText(XHIBITConstant.getResource(resources, "lblName"));
        }

        return nameLbl;
    }

    private JTextField getNameText() {
        if (nameText == null) {
            nameText = new JTextField();
            enableTextField(nameText, false);
            nameText.setToolTipText(XHIBITConstant.getResource(resources, "ttName"));
            nameText.setColumns(20);
        }

        return nameText;
    }

    /**
     * Obtain the text for the the find/search button if the button has not
     * already been created. The text will be obtained from resource bundle,
     * with the appropriate tooltip text.
     * 
     * @return
     */
    private JButton getFindBtn() {
        if (findBtn == null) {
            findBtn = new JButton();
            findBtn.setBorder(BorderFactory.createRaisedBevelBorder());
            findBtn.setPreferredSize(buttonPanel.getCancel().getPreferredSize());
            findBtn.setToolTipText(XHIBITConstant.getResource(resources, "ttFindLegalRep"));
            findBtn.setMnemonic(XHIBITConstant.getResource(resources, "mnmFind").charAt(0));
            // findBtn.setEnabled( false );
            findBtn.setText(XHIBITConstant.getResource(resources, "lblFindLegalRep"));
            findBtn.addActionListener(new XAction() {
   
                private static final long serialVersionUID = 1L;

                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    // Call the FindLegalRepresentativeDialog utility screen
                    FindLegalRepresentativeModel flrModel = new FindLegalRepresentativeModel();
                    flrModel.setXac(model.getXac());
                    FindLegalRepresentativeDialog flrDialog = new FindLegalRepresentativeDialog(
                            parent.getParentFrame(), flrModel);

                    flrDialog.setVisible(true);

                    // If the OK button was clicked, save the details in the
                    // model
                    if (flrDialog.isOkClicked()) {
                        FindLegalRepresentativeTableRowModel flrtrModel = flrModel
                                .getFindLegalRepresentativeTableRowModel();

                        model.setLegalRep(flrtrModel);

                        moveModelToScreen();
                    }

                    stepUpdateViewState();
                }
            });
        }

        return findBtn;
    }

    private JLabel getChambersLbl() {
        if (chambersLbl == null) {
            chambersLbl = new JLabel();
            chambersLbl.setText(XHIBITConstant.getResource(resources, "lblChambers"));
        }

        return chambersLbl;
    }

    private JTextField getChambersText() {
        if (chambersText == null) {
            chambersText = new JTextField();
            chambersText.setToolTipText(XHIBITConstant.getResource(resources, "ttChambers"));
            enableTextField(chambersText, false);
        }

        return chambersText;
    }

    private JLabel getAddressLine01Lbl() {
        if (addressLine01Lbl == null) {
            addressLine01Lbl = new JLabel();
            addressLine01Lbl.setText(XHIBITConstant.getResource(resources, "lblAddressLine01"));
        }

        return addressLine01Lbl;
    }

    private JTextField getAddressLine01Text() {
        if (addressLine01Text == null) {
            addressLine01Text = new JTextField();
            addressLine01Text.setToolTipText(XHIBITConstant.getResource(resources, "ttAddressLine01"));
            enableTextField(addressLine01Text, false);
        }

        return addressLine01Text;
    }

    private JTextField getAddressLine02Text() {
        if (addressLine02Text == null) {
            addressLine02Text = new JTextField();
            addressLine02Text.setToolTipText(XHIBITConstant.getResource(resources, "ttAddressLine02"));
            enableTextField(addressLine02Text, false);
        }

        return addressLine02Text;
    }

    private JLabel getTownLbl() {
        if (townLbl == null) {
            townLbl = new JLabel();
            townLbl.setText(XHIBITConstant.getResource(resources, "lblTown"));
        }

        return townLbl;
    }

    private JTextField getTownText() {
        if (townText == null) {
            townText = new JTextField();
            townText.setToolTipText(XHIBITConstant.getResource(resources, "ttTown"));
            enableTextField(townText, false);
        }

        return townText;
    }

    private JLabel getCountyLbl() {
        if (countyLbl == null) {
            countyLbl = new JLabel();
            countyLbl.setText(XHIBITConstant.getResource(resources, "lblCounty"));
        }

        return countyLbl;
    }

    private JTextField getCountyText() {
        if (countyText == null) {
            countyText = new JTextField();
            countyText.setToolTipText(XHIBITConstant.getResource(resources, "ttCounty"));
            enableTextField(countyText, false);
        }

        return countyText;
    }

    private JLabel getPostCodeLbl() {
        if (postCodeLbl == null) {
            postCodeLbl = new JLabel();
            postCodeLbl.setText(XHIBITConstant.getResource(resources, "lblPostCode"));
        }

        return postCodeLbl;
    }

    private JTextField getPostCodeText() {
        if (postCodeText == null) {
            postCodeText = new JTextField();
            postCodeText.setToolTipText(XHIBITConstant.getResource(resources, "ttPostCode"));
            enableTextField(postCodeText, false);
        }

        return postCodeText;
    }

    /**
     * Obtain non-reference data from the mid-tier. Save the data in the model.
     * Update the view state.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();

        stepUpdateViewState();
    }

    /**
     * Move data from the model to the screen.
     */
    private void moveModelToScreen() {
        if (model.getLegalRep() == null) {
            // NoAction
        } else {
            getNameText().setText(model.getLegalRep().getFullName());
            getChambersText().setText(model.getLegalRep().getChambersName());
            getAddressLine01Text().setText(model.getLegalRep().getAddressLine01());
            getAddressLine02Text().setText(model.getLegalRep().getAddressLine02());
            getTownText().setText(model.getLegalRep().getTown());
            getCountyText().setText(model.getLegalRep().getCounty());
            getPostCodeText().setText(model.getLegalRep().getPostCode());
        }
    }

    /**
     * Change the state of the screen components depending upon available data
     */
    public void stepUpdateViewState() {
        getFindBtn().setEnabled(true);
        buttonPanel.getBack().setEnabled(false);
        buttonPanel.getNext().setEnabled(true);
        buttonPanel.getFinish().setEnabled(isMandatoryFieldsCompleted());
    }

    /**
     * Check if the mandatory field has been completed. Mandatory field is the
     * Legal rep
     * 
     * @return boolean
     */
    private boolean isMandatoryFieldsCompleted() {
        return (model.getLegalRep() == null ? false : true);
    }

    /**
     * Perform logical validation for all the data on the screen
     * 
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // empty
    }

    /**
     * Populate the model with data from the screen ready to be written back to
     * the mid-tier
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }

    /**
     * Move data from the screen to the model
     */
    private void moveScreenToModel() {
        // empty
    }

    /**
     * If the Apply/Finish/OK button was clicked the value of the parameter will
     * be set to true.
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // empty
    }

    public JComponent getFirstEnterableComponent() {
        return getNameText();
    }

    /**
     * Method to make textfield enabled and editable.
     * 
     * @param textField
     * @param state
     */
    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setEditable(state);
        textField.setBackground((state ? SystemColor.white : SystemColor.text));
    }
}
