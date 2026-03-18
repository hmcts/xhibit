package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchSolicitorFirmAction;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: AddLegalRepresentativePanel
 * </p>
 * <p>
 * Description: The panel for Add Legal Representative
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class AddLegalRepresentativePanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private static final int CREST_SOLICITOR_NAME_LENGTH = 40;

    private String resources = XhibitBundles.CounselFacilities;

    private JLabel firstNameLbl = null;

    private JTextField firstNameText = null;

    private JLabel surnameLbl = null;

    private JTextField surnameText = null;

    private JLabel chambersLbl = null;

    private JTextField chambersText = null;

    private JButton findBtn = null;

    private JLabel addressLine01Lbl = null;

    private JTextField addressLine01Text = null;

    private JTextField addressLine02Text = null;

    private JLabel townLbl = null;

    private JTextField townText = null;

    private JTextField countyText = null;

    private JTextField postCodeText = null;

    private JLabel countyLbl = null;

    private JLabel postCodeLbl = null;

    private AddLegalRepresentativeModel model;

    private OkCancelPanel buttonPanel;

    /**
     * Public constructor
     * 
     * @param parent
     * @param model
     * @throws CSRecoverableException
     */
    public AddLegalRepresentativePanel(XDialog parent, AddLegalRepresentativeModel model) throws CSRecoverableException {
        super();

        this.model = model;
        this.buttonPanel = (OkCancelPanel) parent.getButtonPanel();

        stepInitialise();
        jbInit();
        stepActivate();
    }

    /**
     * This is a life-cycle event under the control of the class. Obtain
     * reference data and/or data that is required to be available before the
     * screen is built
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
        this.setLayout(new GridBagLayout());

        this.add(getFirstNameLbl(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getFirstNameText(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSurnameLbl(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSurnameText(), new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getChambersLbl(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getChambersText(), new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getFindBtn(), new GridBagConstraints(3, 2, 1, 1, 1.0, 1.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getAddressLine01Lbl(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getAddressLine01Text(), new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getAddressLine02Text(), new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getTownLbl(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getTownText(), new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getCountyLbl(), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getCountyText(), new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPostCodeLbl(), new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPostCodeText(), new GridBagConstraints(1, 7, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    }

    /**
     * Create a new label for first name if it doesn't exist.
     * 
     * @return JLabel
     */
    private JLabel getFirstNameLbl() {
        if (firstNameLbl == null) {
            firstNameLbl = new JLabel();
            firstNameLbl.setText(XHIBITConstant.getResource(resources, "lblFirstName"));
        }

        return firstNameLbl;
    }

    /**
     * Get the text field for the firstname. If it doesn't exist it will be
     * created and a keylistener will be added to it.
     * 
     * @return JTextField
     */
    private JTextField getFirstNameText() {
        if (firstNameText == null) {
            firstNameText = new JTextField();
            firstNameText.setDocument(new LimitedTextValidatingDocumentDecorator(35));
            firstNameText.setColumns(15);
            firstNameText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    firstNameText_keyReleased(e);
                }
            });
            enableTextField(firstNameText, true);
            firstNameText.setToolTipText(XHIBITConstant.getResource(resources, "ttFirstName"));
        }
        return firstNameText;
    }

    /**
     * Consumes key released events on the firstNameText field.
     * 
     * @param KeyEvent
     */
    private void firstNameText_keyReleased(@SuppressWarnings("unused") KeyEvent e) {
        stepUpdateViewState();
    }

    /**
     * Create a new label for surname if it doesn't exist.
     * 
     * @return JLabel
     */
    private JLabel getSurnameLbl() {
        if (surnameLbl == null) {
            surnameLbl = new JLabel();
            surnameLbl.setText(XHIBITConstant.getResource(resources, "lblSurname"));
        }

        return surnameLbl;
    }

    /**
     * Get the static text field for the surname. If it doesn't exist it will be
     * created and a keylistener will be added to it.
     * 
     * @return JTextField
     */
    private JTextField getSurnameText() {
        if (surnameText == null) {
            surnameText = new JTextField();
            surnameText.setDocument(new LimitedTextValidatingDocumentDecorator(35));
            surnameText.setColumns(15);
            surnameText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    surnameText_keyReleased(e);
                }
            });
            enableTextField(surnameText, true);
            surnameText.setToolTipText(XHIBITConstant.getResource(resources, "ttSurname"));
        }

        return surnameText;
    }

    /**
     * Consumes key released events on the surnameText field.
     * 
     * @param KeyEvent
     */
    private void surnameText_keyReleased(@SuppressWarnings("unused") KeyEvent e) {
        stepUpdateViewState();
    }

    /**
     * Create the search/find button if it doesn't exist.
     * 
     * @return JButton
     */
    private JButton getFindBtn() {
        if (findBtn == null) {
            findBtn = new JButton();
            XAction a = XhibitActions.getAction(model.getXac(), XhibitActions.OpenSearchSolicitorFirm);
            a.setCaller(this);
            findBtn.setAction(a);
            findBtn.setBorder(BorderFactory.createRaisedBevelBorder());
            findBtn.setPreferredSize(buttonPanel.cancelButton.getPreferredSize());
            findBtn.setToolTipText(XHIBITConstant.getResource(resources, "ttFindChambers"));
            findBtn.setMnemonic('F');
            findBtn.setText(XHIBITConstant.getResource(resources, "lblFindChambers"));
        }

        return findBtn;
    }

    /**
     * Method to process the search result. All results will be added to the
     * model.
     * 
     * @param OpenSearchSolicitorFirmAction
     */
    public void processSearchSolicitorFirm(OpenSearchSolicitorFirmAction ossfa) {
        Collection col = ossfa.getResults();
        Iterator it = col.iterator();

        if (it.hasNext()) {
            RefSolicitorFirmComplexValue item = (RefSolicitorFirmComplexValue) it.next();

            model.setFirstName(getFirstNameText().getText());
            model.setSurname(getSurnameText().getText());

            model.setAddressLine01(CounselFacilitiesHelper.tidyUp(item.getAddress1()));
            model.setAddressLine02(CounselFacilitiesHelper.tidyUp(item.getAddress2()));
            model.setChambers(CounselFacilitiesHelper.tidyUp(item.getSolicitorFirmName()));
            model.setChambersId(item.getId());
            model.setCounty(CounselFacilitiesHelper.tidyUp(item.getCounty()));
            model.setPostCode(CounselFacilitiesHelper.tidyUp(item.getPostcode()));
            model.setTown(CounselFacilitiesHelper.tidyUp(item.getTown()));

            moveModelToScreen();

            stepUpdateViewState();
        }
    }

    /**
     * Create a new label for chamber if it doesn't exist.
     * 
     * @return JLabel
     */
    private JLabel getChambersLbl() {
        if (chambersLbl == null) {
            chambersLbl = new JLabel();
            chambersLbl.setText(XHIBITConstant.getResource(resources, "lblChambers"));
        }

        return chambersLbl;
    }

    /**
     * Get the static text field for the chamber name. If it doesn't exist it
     * will be created and a keylistener will be added to it.
     * 
     * @return JTextField
     */
    private JTextField getChambersText() {
        if (chambersText == null) {
            chambersText = new JTextField();
            chambersText.setToolTipText(XHIBITConstant.getResource(resources, "ttChambers"));
            enableTextField(chambersText, false);
        }

        return chambersText;
    }

    /**
     * Create a new label for address line 1 if it doesn't exist.
     * 
     * @return JLabel
     */
    private JLabel getAddressLine01Lbl() {
        if (addressLine01Lbl == null) {
            addressLine01Lbl = new JLabel();
            addressLine01Lbl.setText(XHIBITConstant.getResource(resources, "lblAddressLine01"));
        }

        return addressLine01Lbl;
    }

    /**
     * Get the static text field for the address line 1. If it doesn't exist it
     * will be created and a keylistener will be added to it.
     * 
     * @return JTextField
     */
    private JTextField getAddressLine01Text() {
        if (addressLine01Text == null) {
            addressLine01Text = new JTextField();
            addressLine01Text.setToolTipText(XHIBITConstant.getResource(resources, "ttAddressLine01"));
            enableTextField(addressLine01Text, false);
        }

        return addressLine01Text;
    }

    /**
     * Get the static text field for the address line 2. If it doesn't exist it
     * will be created and a keylistener will be added to it.
     * 
     * @return JTextField
     */
    private JTextField getAddressLine02Text() {
        if (addressLine02Text == null) {
            addressLine02Text = new JTextField();
            addressLine02Text.setToolTipText(XHIBITConstant.getResource(resources, "ttAddressLine02"));
            enableTextField(addressLine02Text, false);
        }

        return addressLine02Text;
    }

    /**
     * Create a new label for town if it doesn't exist.
     * 
     * @return JLabel
     */
    private JLabel getTownLbl() {
        if (townLbl == null) {
            townLbl = new JLabel();
            townLbl.setText(XHIBITConstant.getResource(resources, "lblTown"));
        }

        return townLbl;
    }

    /**
     * Get the static text field for the Town. If it doesn't exist it will be
     * created and a keylistener will be added to it.
     * 
     * @return JTextField
     */
    private JTextField getTownText() {
        if (townText == null) {
            townText = new JTextField();
            townText.setToolTipText(XHIBITConstant.getResource(resources, "ttTown"));
            enableTextField(townText, false);
        }

        return townText;
    }

    /**
     * Create a new label for county if it doesn't exist.
     * 
     * @return JLabel
     */
    private JLabel getCountyLbl() {
        if (countyLbl == null) {
            countyLbl = new JLabel();
            countyLbl.setText(XHIBITConstant.getResource(resources, "lblCounty"));
        }
        return countyLbl;
    }

    /**
     * Get the static text field for the county. If it doesn't exist it will be
     * created and a keylistener will be added to it.
     * 
     * @return JTextField
     */
    private JTextField getCountyText() {
        if (countyText == null) {
            countyText = new JTextField();
            countyText.setToolTipText(XHIBITConstant.getResource(resources, "ttCounty"));
            enableTextField(countyText, false);
        }

        return countyText;
    }

    /**
     * Create a new label for postcode if it doesn't exist.
     * 
     * @return JLabel
     */
    private JLabel getPostCodeLbl() {
        if (postCodeLbl == null) {
            postCodeLbl = new JLabel();
            postCodeLbl.setText(XHIBITConstant.getResource(resources, "lblPostCode"));
        }
        return postCodeLbl;
    }

    /**
     * Get the static text field for the postcode. If it doesn't exist it will
     * be created and a keylistener will be added to it.
     * 
     * @return JTextField
     */
    private JTextField getPostCodeText() {
        if (postCodeText == null) {
            postCodeText = new JTextField();
            postCodeText.setToolTipText(XHIBITConstant.getResource(resources, "ttPostCode"));
            enableTextField(postCodeText, false);
        }

        return postCodeText;
    }

    /**
     * This is a life-cycle event called by the framework when the screen is
     * made visible. Move data from the model to the screen. Update the view
     * state.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();

        stepUpdateViewState();
    }

    /**
     * Set the data on screen to what is in the model.
     */
    private void moveModelToScreen() {
        getFirstNameText().setText(model.getFirstName());
        getSurnameText().setText(model.getSurname());
        getChambersText().setText(model.getChambers());
        getAddressLine01Text().setText(model.getAddressLine01());
        getAddressLine02Text().setText(model.getAddressLine02());
        getTownText().setText(model.getTown());
        getCountyText().setText(model.getCounty());
        getPostCodeText().setText(model.getPostCode());
    }

    /**
     * This is a life-cycle event under the control of the class. Enable/disable
     * screen widgets depending on the state of the data.
     */
    public void stepUpdateViewState() {
        buttonPanel.okButton.setEnabled(isMandatoryFieldsCompleted());
    }

    /**
     * Validation to check if all mandatory data has been entered. The fields
     * checked are: chamber, first name and surname.
     * 
     * @return boolean
     */
    private boolean isMandatoryFieldsCompleted() {
        return (getChambersText().getText().trim().length() > 0 && (getFirstNameText().getText().trim().length() > 0 || getSurnameText()
                .getText().trim().length() > 0));
    }

    /**
     * This is a life-cycle event called by the framework when the screen is
     * made invisible or the Apply/Finish/OK button is clicked. Move details
     * from the screen to the model.
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }

    /**
     * Set the data entered on screen into the model.
     */
    private void moveScreenToModel() {
        model.setFirstName(getFirstNameText().getText().trim());
        model.setSurname(getSurnameText().getText().trim());
        model.setChambers(getChambersText().getText());
        model.setAddressLine01(getAddressLine01Text().getText());
        model.setAddressLine02(getAddressLine02Text().getText());
        model.setTown(getTownText().getText());
        model.setCounty(getCountyText().getText());
        model.setPostCode(getPostCodeText().getText());

        model.printModel();
    }

    /**
     * This is a life-cycle event called by the framework whene any of Apply,
     * Finish, OK or Cancel is clicked. If the Apply/Finish/OK button was
     * clicked the value of the 'update' parameter is set to true: a
     * SolicitorBasicValue object is built the createSolicitor( ) method on the
     * BisRefController is called passing the solicitor as a parameter the new
     * solicitor details are saved in the model
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            /**
             * It's a new solicitor so create the solicitor record with their
             * name truncated to a maximum of the size of the CREST solicitor
             * name column and save the legalRepId in the model ready for
             * assignment
             */
            SolicitorBasicValue solicitor = new SolicitorBasicValue();

            String crestName = CounselFacilitiesHelper.getFullName("", new String[] { model.getFirstName(),
                    model.getSurname() });
            int crestNameLength = crestName.length();
            solicitor.setCrestSolicitorName(crestName.substring(0, Math.min(CREST_SOLICITOR_NAME_LENGTH,
                    crestNameLength)));

            solicitor.setFirmId(model.getChambersId());
            solicitor.setFirstName(model.getFirstName());
            solicitor.setSurname(model.getSurname());
            solicitor.setCourtId(XhibitSingleton.getInstance().getCourtId());
            solicitor.setInCrest("N");
            solicitor.setLegalRepType(PersonValue.SOLICITOR);

            solicitor = XhibitDelegateHelper.getBizRefDelegate().createSolicitor(solicitor,
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

            model.setLegaRepId(solicitor.getLegalRepId());
        }
    }

    /**
     * This is a life-cycle event called by the framework when the
     * Apply/Finish/OK button is clicked.
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() {
        // empty
    }

    /**
     * Returns a reference to the first enterable component on the screen.
     * 
     * @return JCompenent
     */
    public JComponent getFirstEnterableComponent() {
        return getFirstNameText();
    }

    /**
     * Method to set a textfield to be enabled and editable.
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
