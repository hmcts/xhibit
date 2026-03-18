package uk.gov.courtservice.xhibit.client.schedule.addhearing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.CaseInfoValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefHearingTypeCriteria;
import uk.gov.courtservice.xhibit.client.schedule.CourtRoomListCellRenderer;
import uk.gov.courtservice.xhibit.client.util.WizardButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTimePanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0 This is the first (of three) windows in the Add Hearing Wizard.
 *          It allows the user to select the courtroom and choose the time for
 *          the Hearing and select a hearing type.
 */
public class AddHearingEnterCaseNumber extends XPanel {
    private static final Logger LOG = CSServices.getLogger(AddHearingEnterCaseNumber.class);

    private static final int ROWS_TO_SHOW = 7;

    private static final String CASE_TYPE_NUMBER_REG_EXP = "[A-Z]{1}[0-9]{8}";

    private static final String CASE_TYPE_REG_EXP = "[ABSTU]{1}";

    public static final String CASE_TYPE_U = "U";

    private final Dimension lblDim = new Dimension(75, 17);

    private final Dimension lbl2Dim = new Dimension(100, 17);

    private final Dimension listDim = new Dimension(350, XHIBITConstant.getLineHeight() * ROWS_TO_SHOW);

    // only to be set in the constructor...
    private final AddHearingModel model;

    private final WizardButtonPanel buttonPanel;

    private final Vector allRefHearingTypes = new Vector();

    private final Vector top5RefHearingTypes = new Vector();

    private final Vector distinctRefHearingTypes = new Vector();

    private DefaultComboBoxModel courtRoomsModel;

    private String selectedCourtRoomItem;

    private Integer selectedCourtRoomId;

    private HashMap refHearingTypesHashMap;

    private XhbCourtRoomBasicValue selectedCourtRoomBasicValue;

    // the swing components...
    private JTextField txtFldCaseNumber;

    private JTextField hearingTypeDescTxt;

    private JTextField hearingTypeCodeTxt;

    private JComboBox courtList;

    private XTimePanel timePanel;

    private JPanel hearingTypesPanel;

    private JPanel hearingTypeCodePanel;

    private JPanel hearingTypeFilterPanel;

    private JScrollPane hearingTypesScrollPane;

    private JList hearingTypesList;

    private JButton verifyCodeBtn;

    private JToggleButton allTypesBtn;

    private JToggleButton byCaseTypeBtn;

    private JToggleButton top5TypesBtn;

    private ButtonGroup hearingTypesBtnGrp;

    private JPanel caseLifeCyclePanel;

    private JRadioButton existingCaseRadio;

    private JRadioButton newUCaseRadio;

    private ButtonGroup caseLifeCycleBtnGrp;

    /**
     * Constructor that takes in the model for this dialog and a reference to
     * the button panel
     * 
     * @param model
     * @param buttonPanel
     * @throws CSRecoverableException
     */
    public AddHearingEnterCaseNumber(AddHearingModel model, WizardButtonPanel buttonPanel)
            throws CSRecoverableException {
        this.model = model;
        this.buttonPanel = buttonPanel;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    /**
     * Add widgets to the screen
     */
    private void jbInit() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - jbInit ");
        }

        this.setLayout(new GridBagLayout());

        // Initialise the time panel...
        timePanel = new XTimePanel(this);
        timePanel.setRequired(true);

        hearingTypesPanel = new JPanel(new GridBagLayout());
        hearingTypeCodePanel = new JPanel(new GridBagLayout());
        hearingTypeFilterPanel = new JPanel(new GridBagLayout());
        caseLifeCyclePanel = new JPanel(new GridBagLayout());

        // add the items to the panel
        final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0);

        // Case life-cycle panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(createLabel("AddHearingCaseLifeCycleLabel", lblDim), gbc);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        caseLifeCyclePanel.add(getExistingCaseRadio(), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        caseLifeCyclePanel.add(getNewUCaseRadio(), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(caseLifeCyclePanel, gbc);

        // Case No
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(createLabel("AddHearingCrestCaseNumberLabel", lblDim), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(getTxtFldCaseNumber(), gbc);

        // Court
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(createLabel("AddHearingCourtNoLabel", lblDim), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        this.add(getCourtList(), gbc);

        // Time
        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(createLabel("AddHearingCourtTimeLabel", lblDim), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        this.add(timePanel, gbc);

        // Hearing type code
        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(createLabel("AddHearingHearingTypeCodeLabel", lbl2Dim), gbc);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        hearingTypeCodePanel.add(getHearingTypeCodeTxt(), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        hearingTypeCodePanel.add(getVerifyCodeBtn(), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        this.add(hearingTypeCodePanel, gbc);

        // Hearing types description and list
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(4, 4, 4, 4);
        this.add(createLabel("AddHearingHearingTypeDescLabel", lbl2Dim), gbc);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        hearingTypesPanel.add(getHearingTypeDescTxt(), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        hearingTypesPanel.add(getHearingTypesScrollPane(), gbc);

        // Hearing type toggle button controls
        gbc.gridx = 0;
        gbc.gridy = 0;
        hearingTypeFilterPanel.add(getTop5TypesBtn(), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        hearingTypeFilterPanel.add(getByCaseTypeBtn(), gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        hearingTypeFilterPanel.add(getAllTypesBtn(), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        hearingTypesPanel.add(hearingTypeFilterPanel, gbc);

        // Add the hearing types panel to the main panel
        gbc.gridx = 1;
        gbc.gridy = 6;
        this.add(hearingTypesPanel, gbc);

        getCaseLifeCycleBtnGrp().add(getExistingCaseRadio());
        getCaseLifeCycleBtnGrp().add(getNewUCaseRadio());

        getHearingTypesBtnGrp().add(getAllTypesBtn());
        getHearingTypesBtnGrp().add(getByCaseTypeBtn());
        getHearingTypesBtnGrp().add(getTop5TypesBtn());
    }

    /**
     * Life-cycle method to retrieve non-volatile data, e.g. entries for pull
     * down lists. In this case, the list of court rooms and hearing types for
     * the current court.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepInitialise:START");
        }

        model.setCourtId(XhibitSingleton.getInstance().getCourtId());
        model.setCourtSiteId(XhibitSingleton.getInstance().getCourtSiteId());

        // XhbCourtRoomBasicValue[] courtRooms = ScheduleHelper.getDelegate().
        // getCourtStructure(XhibitSingleton.getInstance().getCourtId()).
        // getAllCourtRooms();
        XhbCourtRoomBasicValue[] courtRooms = XhibitSingleton.getInstance().getCourtStructureValue().getAllCourtRooms();

        XhbCourtRoomBasicValue[] availableCourtRooms = new XhbCourtRoomBasicValue[courtRooms.length + 1];
        availableCourtRooms[0] = new XhbCourtRoomBasicValue();
        availableCourtRooms[0].setDisplayName("");
        System.arraycopy(courtRooms, 0, availableCourtRooms, 1, courtRooms.length);
        courtRoomsModel = new DefaultComboBoxModel(availableCourtRooms);

        // Build a HashSet of the top 5 codes
        HashSet top5Codes = new HashSet();
        top5Codes.add(getBundleEntry("AddHearingTop5HearingTypes.code01"));
        top5Codes.add(getBundleEntry("AddHearingTop5HearingTypes.code02"));
        top5Codes.add(getBundleEntry("AddHearingTop5HearingTypes.code03"));
        top5Codes.add(getBundleEntry("AddHearingTop5HearingTypes.code04"));
        top5Codes.add(getBundleEntry("AddHearingTop5HearingTypes.code05"));

        // Obtain the hearing types and save them all in a HashMap to be used
        // for validation
        refHearingTypesHashMap = new HashMap();

        RefHearingTypeCriteria rhtCriteria = new RefHearingTypeCriteria();
        rhtCriteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
        Collection refHearingTypesCollection = XhibitDelegateHelper.getBizRefDelegate().findHearingTypes(rhtCriteria);

        // Sort all the hearing types by the description
        Sorter.sort((List) refHearingTypesCollection, new String[] { "hearingTypeDesc" });

        // Iterate through the hearing types saving them in various Vector
        // objects for use when the filtering command buttons are clicked
        Iterator iter = refHearingTypesCollection.iterator();
        while (iter.hasNext()) {
            RefHearingTypeBasicValue rhtBV = (RefHearingTypeBasicValue) iter.next();
            PullDownListObject pdlo = buildListObject(rhtBV);

            // Save the item in a full list of hearing types
            allRefHearingTypes.add(pdlo);

            if (!refHearingTypesHashMap.containsKey(rhtBV.getHearingTypeCode())) {
                // Save the item in a unique collection of codes
                refHearingTypesHashMap.put(rhtBV.getHearingTypeCode(), rhtBV);
                distinctRefHearingTypes.add(pdlo);

                if (top5Codes.contains(rhtBV.getHearingTypeCode())) {
                    // Save the item in the collection of top 5 codes
                    top5RefHearingTypes.add(pdlo);
                }
            }
        }

        stepUpdateViewState();

        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepInitialise:END");
        }
    }

    /**
     * Builds a PullDownListObject using the object passed to it
     * 
     * @param param -
     *            the RefHearingTypeBasicValue object to be used a the donor
     * @return PullDownListObject - built from the donor
     */
    private PullDownListObject buildListObject(RefHearingTypeBasicValue param) {
        return new PullDownListObject(param.getId().intValue(), param.getHearingTypeCode(), param.getHearingTypeDesc(),
                param.getId(), param);
    }

    /**
     * Life-cycle method execute when the user navigates to the next screen. In
     * this instance, the case number entered by the user is used to retrieve
     * the remaining case details. This method is called by the framework after
     * stepValidate but only if all the data is valid.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepDeActivate:START");
        }
        
        // move screen to model. This will only be from Enter Case Number panel
        // this is where the case details are retrieved
        try {
            final String oldCaseTypeAndNumber = model.getCaseTypeAndNumber();

            moveScreenToModel();

            if (model.isExistingCase()) {
                // doesn't matter about the court id as not from screen...
                if (LOG.isDebugEnabled()){
                    LOG.debug("oldCaseTypeAndNumber: " +oldCaseTypeAndNumber);
                    LOG.debug("model.getDefendants(): " +model.getDefendants());
                    LOG.debug("model.getCaseTitle(): " +model.getCaseTitle());
                }
                       
                if (oldCaseTypeAndNumber == null
                        || oldCaseTypeAndNumber.equalsIgnoreCase("")
                        || !oldCaseTypeAndNumber.equalsIgnoreCase(model.getCaseTypeAndNumber())){
                
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(" cinv values : " + model.getCaseNumber() + ", " + model.getCaseType() + ", "
                            + model.getCourtId());
                        LOG.debug("BEFORE CALL to getCaseDetails");
                    }
                    
                    CaseInfoValue cinv = getCaseDetails(model.getCourtId(), model.getCaseType(), model.getCaseNumber(),
                            model.getRefHearingTypeBasicValue().getHearingTypeCode());
                    
                    if (LOG.isDebugEnabled()){
                        LOG.debug("AFTER CALL to getCaseDetails");
                    }
                    
                    model.setDefendants(cinv.getDefendants());
                    model.setCaseTitle(cinv.getCaseBasicValue().getCaseTitle());

                    if (model.getJudgeName() == null) {
                        model.setJudgeName(cinv.getJudgeName());
                    }
                } else {
                    if (LOG.isDebugEnabled()){
                        LOG.debug(" cinv values : not refreshing as case not changed");
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("AddHearingEnterCaseNumber - stepDeActivate: END");
                }
            } else {
                if (LOG.isDebugEnabled()){
                    LOG.debug(" cinv values : not obtained as new 'U' case");
                }
                model.setDefendants(new Vector());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("AddHearingEnterCaseNumber - stepDeActivate: END");
                }
            }
        } catch (HearingScheduleException ex) {
            // reset the case number and type to force a midtier read next
            // time
            LOG.error("HearingScheduleException has occured: ", ex);

            model.setCaseNumber(null);
            model.setCaseType(null);
            LOG.debug("HearingScheduleException: model information has been reset");
            // model.setCaseTypeAndNumber(null);
            // If there is a HearingScheduleException we want to re-throw it
            // to ensure we display the right error information for the user.
            throw ex;
        } catch (Exception e) {
            LOG.error("Exception has occured: ", e);
            // reset case number and type to force a midtier read next time
            model.setCaseNumber(null);
            model.setCaseType(null);
            if (LOG.isDebugEnabled()){
                LOG.debug("Exception: model information has been reset");
            }
            // model.setCaseTypeAndNumber(null);
            throw new CSRecoverableException("gui.user.businessdelegateinstantiation",
                    new Object[] { "HearingScheduleControllerBusinessDelegate" },
                    "Exception whilst instantiating the HearingScheduleController BusinessDelegate", e);
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepDeActivate:END");
        }
    }

    private CaseInfoValue getCaseDetails(Integer courtId, String caseType, Integer caseNumber, String hearingType)
            throws HearingScheduleException, Exception {
        return (XhibitDelegateHelper.getHearingDelegate().getCaseDetails(caseNumber, caseType, courtId, hearingType));
    }

    /**
     * Life-cycle method to vaidate the data on the screen. This method is
     * called by the framework when the user navigates off this screen.
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepValidate:START");
        }

        // validate the fields before moving them to the model
        if (isExistingCaseSelected()) {
            validateCaseNumber(getTxtFldCaseNumber().getText().trim());
        }

        if (getHearingTypesList().getSelectedIndex() < 0) {
            validateHearingTypeCode();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepValidate:END");
        }
    }

    /**
     * Ensure the case type and number conform to an acceptable format
     * 
     * @param caseNumber
     * @throws CSValidationException
     */
    private void validateCaseNumber(String caseNumber) throws CSValidationException {
        // caseNo must be in A99999999 format
        if (!isDataMatchesRegularExpression(CASE_TYPE_NUMBER_REG_EXP, caseNumber)) {
            throw new CSValidationException("gui.addHearing.invalidCaseNumber",
                    "Incorrect format for Case Number: must be a letter followed by 8 digits");
        }

        // the first character or caseNo must be A, B, S, T or U
        if (!isDataMatchesRegularExpression(CASE_TYPE_REG_EXP, caseNumber.substring(0, 1))) {
            throw new CSValidationException("gui.addHearing.invalidCaseType",
                    "The first character must br A, B, S, T or U");
        }

        // ensure the number part is valid
        try {
            Integer tempCaseNumber = new Integer(caseNumber.substring(1));
            String tempCaseType = String.valueOf(caseNumber.charAt(0));
        } catch (NumberFormatException nfe) {
            throw new CSValidationException("gui.benchwarrant.invalidcasenumber", "The case number is not valid");
        }
    }

    /**
     * Empty implementation of the life-cycle method.
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepDeInitialise ");
        }
    }

    /**
     * Life-cycle method that is executed as a consequence of making the screen
     * visible. In 'Wizard' dialogs, the method is called explicitly for the
     * first screen in the sequence but is called by the framework for all
     * subsequent screens.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepActivate:START");
        }

        stepUpdateViewState();

        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepActivate:END");
        }
    }

    private JTextField getTxtFldCaseNumber() {
        if (txtFldCaseNumber == null) {
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability.upperCase(),
                    Capability.limitedText(9) });
            txtFldCaseNumber = JTextFieldFactory.getTextField(doc);
            txtFldCaseNumber.setPreferredSize(new Dimension(100, XHIBITConstant.getLineHeight()));
            txtFldCaseNumber.setMinimumSize(new Dimension(100, XHIBITConstant.getLineHeight()));
            txtFldCaseNumber.setToolTipText(getBundleEntry("AddHearingCrestCaseNumberToolTip"));
            txtFldCaseNumber.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
        }
        return txtFldCaseNumber;
    }

    private JComboBox getCourtList() {
        if (courtList == null) {
            courtList = new JComboBox(courtRoomsModel);
            courtList.setToolTipText(getBundleEntry("AddHearingCourtListDropDownToolTip"));
            courtList.setRenderer(new CourtRoomListCellRenderer());
            courtList.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    courtList_ActionPerformed(e);
                }
            });
        }
        return courtList;
    }

    /**
     * Pseudo life-cycle method to copy data from the screen to the model
     * 
     * @throws CSRecoverableException
     */
    private void moveScreenToModel() throws CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - moveScreenToModel ");
            LOG.debug("AddHearingEnterCaseNumber - datePanel.getHour()    :" + timePanel.getHour());
            LOG.debug("AddHearingEnterCaseNumber - datePanel.getMinute()  :" + timePanel.getMinute());
            LOG.debug("AddHearingEnterCaseNumber - selectedCourtRoomItem  :" + selectedCourtRoomItem);
        }

        model.setCaseLifeCycleCommand(getCaseLifeCycleBtnGrp().getSelection().getActionCommand());

        // The case number in its various forms
        if (model.isExistingCase()) {
            // model.setCaseTypeAndNumber( txtFldCaseNumber.getText( ) );
            model.setCaseType(txtFldCaseNumber.getText().substring(0, 1));
            model.setCaseNumber(new Integer(txtFldCaseNumber.getText().substring(1)));
        } else {
            // model.setCaseTypeAndNumber(null);
            model.setCaseType(null);
            model.setCaseNumber(null);
        }

        // set the rest of the fields...
        model.setTime(timePanel.getDate().getTime());
        model.setSelectedCourtRoomItem(selectedCourtRoomItem);
        model.setSelectedCourtRoomId(selectedCourtRoomId);
        model.setCourtRoomBasicValue(selectedCourtRoomBasicValue);

        model.setRefHearingTypeBasicValue(getBasicValueFromHashMap(getHearingTypeCodeTxt().getText()));

        // some debug...
        model.printModel();
    }

    /**
     * Life-cycle methos to manage the enabled state of screen components.
     */
    public void stepUpdateViewState() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepUpdateViewState:START");
        }

        // Only enable the case number when the existing case radio is selected
        getTxtFldCaseNumber().setEnabled(isExistingCaseSelected());
        enableTextField(getTxtFldCaseNumber(), isExistingCaseSelected());
        getVerifyCodeBtn().setEnabled(getHearingTypeCodeTxt().getText().trim().length() > 0);
        getAllTypesBtn().setEnabled(true);
        getTop5TypesBtn().setEnabled(true);
        getByCaseTypeBtn().setEnabled(
                isNewUCaseSelected()
                        || (isExistingCaseSelected() && getTxtFldCaseNumber().getText().trim().length() > 0));
        buttonPanel.getFinish().setEnabled(false);
        buttonPanel.getNext().setEnabled(isMandatoryCompleted());
        buttonPanel.getBack().setEnabled(false);

        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepUpdateViewState:END");
        }
    }

    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setEditable(state);
        textField.setBackground((state ? Color.white : SystemColor.text));
    }

    /**
     * Determines whether or not all mandatory fields have been completed. The
     * result of this is used to determine the enabled state of the 'Next'
     * button.
     * 
     * @return true if all mandatory fields are completed
     */
    private boolean isMandatoryCompleted() {
        boolean returnCode = true;

        if (LOG.isDebugEnabled()) {
            LOG.debug(" getTxtFldCaseNumber().getText()   : " + getTxtFldCaseNumber().getText());
            LOG.debug(" getCourtList().getSelectedIndex() : " + getCourtList().getSelectedIndex());
        }

        boolean caseNumberIsEmpty = getTxtFldCaseNumber().getText().trim().length() == 0;

        if (isExistingCaseSelected() && caseNumberIsEmpty) {
            returnCode = false;
        }

        if (getCourtList().getSelectedIndex() < 1) {
            returnCode = false;
        }

        if (getHearingTypesList().getSelectedIndex() < 0 && getHearingTypeCodeTxt().getText().trim().length() == 0) {
            returnCode = false;
        }

        return returnCode;
    }

    /**
     * Executed whenever a user selects a court room from the drop-down list
     * 
     * @param e
     */
    private void courtList_ActionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        XhbCourtRoomBasicValue courtRoomValue = (XhbCourtRoomBasicValue) getCourtList().getSelectedItem();

        selectedCourtRoomItem = courtRoomValue.getDisplayName();
        selectedCourtRoomId = courtRoomValue.getCourtRoomId();
        selectedCourtRoomBasicValue = courtRoomValue;

        if (LOG.isDebugEnabled()) {
            LOG.debug(" selectedCourtRoomItem is: " + selectedCourtRoomItem);
            LOG.debug(" selectedCourtRoomId is: " + selectedCourtRoomId);
        }

        stepUpdateViewState();
    }

    /**
     * Create a JLabel using the passed in parameters
     * 
     * @param resourceKey
     *            A <code>String</code> value of the resource key used to get
     *            the text to be display
     * @param preferredSize
     *            Preferred size of the created <code>JLabel</code>
     * @return
     */
    private JLabel createLabel(String resourceKey, Dimension preferredSize) {
        final JLabel label = new JLabel();

        if (resourceKey != null) {
            label.setText(getBundleEntry(resourceKey));
        }

        return label;
    }

    /**
     * Immutable object used to represent the value of a row in a JList
     */
    private class PullDownListObject {
        final private int id;

        final private String code;

        final private String desc;

        final private Integer crId;

        final private Object basicValue;

        public PullDownListObject(int id, String code, String desc, Integer crId, Object basicValue) {
            this.id = id;
            this.code = code;
            this.desc = desc;
            this.crId = crId;
            this.basicValue = basicValue;
        }

        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public Integer getCourtRoomId() {
            return crId;
        }

        public Object getBasicValue() {
            return basicValue;
        }

        public String toString() {
            return desc;
        }
    }

    private JTextField getHearingTypeCodeTxt() {
        if (hearingTypeCodeTxt == null) {
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability.upperCase(),
                    Capability.limitedText(3) });
            hearingTypeCodeTxt = JTextFieldFactory.getTextField(doc);

            hearingTypeCodeTxt.setPreferredSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            hearingTypeCodeTxt.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            hearingTypeCodeTxt.setEnabled(true);
            hearingTypeCodeTxt.setToolTipText(getBundleEntry("AddHearingHearingTypeCodeToolTip"));
            hearingTypeCodeTxt.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    getHearingTypesList().clearSelection();

                    stepUpdateViewState();
                }
            });
        }
        return hearingTypeCodeTxt;
    }

    private JButton getVerifyCodeBtn() {
        if (verifyCodeBtn == null) {
            verifyCodeBtn = new JButton(getBundleEntry("AddHearingHearingTypeVerifyBtnLabel"));

            verifyCodeBtn.setToolTipText(getBundleEntry("AddHearingHearingTypeVerifyBtnToolTip"));
            verifyCodeBtn.setEnabled(false);
            verifyCodeBtn.setMnemonic(getBundleEntry("AddHearingHearingTypeVerifyBtnMnemonic").charAt(0));
            verifyCodeBtn.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    validateHearingTypeCode();

                    RefHearingTypeBasicValue temp = (RefHearingTypeBasicValue) refHearingTypesHashMap
                            .get(getHearingTypeCodeTxt().getText());

                    getHearingTypeDescTxt().setText(temp.getHearingTypeDesc());

                    stepUpdateViewState();
                }
            });
        }
        return verifyCodeBtn;
    }

    private JTextField getHearingTypeDescTxt() {
        if (hearingTypeDescTxt == null) {
            hearingTypeDescTxt = JTextFieldFactory.getTextField();
            hearingTypeDescTxt.setPreferredSize(new Dimension(350, XHIBITConstant.getLineHeight()));
            hearingTypeDescTxt.setMinimumSize(new Dimension(350, XHIBITConstant.getLineHeight()));
            hearingTypeDescTxt.setEnabled(true);
            hearingTypeDescTxt.setToolTipText(getBundleEntry("AddHearingHearingTypeDescToolTip"));
            hearingTypeDescTxt.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    getHearingTypeCodeTxt().setText("");
                    getHearingTypesList().clearSelection();
                    filterHearingTypeList();
                    stepUpdateViewState();
                }
            });
        }
        return hearingTypeDescTxt;
    }

    /**
     * Filters the list of hearing types based on the data entered into the
     * hearing description field.
     */
    private void filterHearingTypeList() {
        String searchText = getHearingTypeDescTxt().getText().trim();
        Vector filteredList = new Vector();
        for (int x = 0; x < distinctRefHearingTypes.size(); x++) {
            PullDownListObject item = (PullDownListObject) distinctRefHearingTypes.elementAt(x);
            if (item.getDesc().regionMatches(true, 0, searchText, 0, searchText.length())) {
                filteredList.add(item);
            }
        }
        getHearingTypesList().setListData(filteredList);
    }

    private JScrollPane getHearingTypesScrollPane() {
        if (hearingTypesScrollPane == null) {
            hearingTypesScrollPane = new JScrollPane(getHearingTypesList());
            hearingTypesScrollPane.setMinimumSize(listDim);
            hearingTypesScrollPane.setPreferredSize(listDim);
        }
        return hearingTypesScrollPane;
    }

    private JList getHearingTypesList() {
        if (hearingTypesList == null) {
            hearingTypesList = new JList(top5RefHearingTypes);
            hearingTypesList.setVisibleRowCount(ROWS_TO_SHOW);
            hearingTypesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            hearingTypesList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (getHearingTypesList().getSelectedIndex() >= 0) {
                        PullDownListObject temp = (PullDownListObject) getHearingTypesList().getSelectedValue();
                        getHearingTypeDescTxt().setText(temp.getDesc());
                        getHearingTypeCodeTxt().setText(temp.getCode());
                    }

                    stepUpdateViewState();
                }
            });
        }
        return hearingTypesList;
    }

    /**
     * Determines whether or not the user entered hearing type code is valid.
     * 
     * @throws CSValidationException
     */
    private void validateHearingTypeCode() throws CSValidationException {
        if (!refHearingTypesHashMap.containsKey(getHearingTypeCodeTxt().getText())) {
            getHearingTypeCodeTxt().requestFocus();
            throw new CSValidationException("gui.addHearing.refHearingTypeCodeNotKnown",
                    new String[] { getHearingTypeCodeTxt().getText() }, "Hearing type code not known");
        }
    }

    /**
     * Returns the requested value from the resource bundle for this task.
     * 
     * @param param
     * @return
     */
    private String getBundleEntry(String param) {
        return ResourceBundleHelper.getResource(XhibitBundles.TodaysSchedule, param);
    }

    /**
     * Returns a RefHearingTypeBasicValue from the hash map off all hearing
     * types that coresponds to the passed in parameter.
     * 
     * @param code
     * @return
     */
    private RefHearingTypeBasicValue getBasicValueFromHashMap(String code) {
        return (RefHearingTypeBasicValue) refHearingTypesHashMap.get(code);
    }

    private JToggleButton getAllTypesBtn() {
        if (allTypesBtn == null) {
            allTypesBtn = new JToggleButton();
            allTypesBtn.setToolTipText(getBundleEntry("AddHearingAllTypesBtnToolTip"));
            allTypesBtn.setActionCommand(getBundleEntry("AddHearingAllTypesAction"));
            allTypesBtn.setText(getBundleEntry("AddHearingAllTypesBtn"));
            allTypesBtn.setMnemonic(getBundleEntry("AddHearingAllTypesBtnMnemonic").charAt(0));
            allTypesBtn.setPreferredSize(getByCaseTypeBtn().getPreferredSize());
            allTypesBtn.setSelected(false);
            allTypesBtn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rebuildHearingTypes_actionPerformed(e);
                }
            });
        }
        return allTypesBtn;
    }

    private JToggleButton getTop5TypesBtn() {
        if (top5TypesBtn == null) {
            top5TypesBtn = new JToggleButton();
            top5TypesBtn.setToolTipText(getBundleEntry("AddHearingTop5TypesBtnToolTip"));
            top5TypesBtn.setActionCommand(getBundleEntry("AddHearingTop5TypesAction"));
            top5TypesBtn.setText(getBundleEntry("AddHearingTop5TypesBtn"));
            top5TypesBtn.setMnemonic(getBundleEntry("AddHearingTop5TypesBtnMnemonic").charAt(0));
            top5TypesBtn.setPreferredSize(getByCaseTypeBtn().getPreferredSize());
            top5TypesBtn.setSelected(true);
            top5TypesBtn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rebuildHearingTypes_actionPerformed(e);
                }
            });
        }
        return top5TypesBtn;
    }

    private JToggleButton getByCaseTypeBtn() {
        if (byCaseTypeBtn == null) {
            byCaseTypeBtn = new JToggleButton();
            byCaseTypeBtn.setToolTipText(getBundleEntry("AddHearingByCaseTypeBtnToolTip"));
            byCaseTypeBtn.setActionCommand(getBundleEntry("AddHearingByCaseTypeAction"));
            byCaseTypeBtn.setText(getBundleEntry("AddHearingByCaseTypeBtn"));
            byCaseTypeBtn.setMnemonic(getBundleEntry("AddHearingByCaseTypeBtnMnemonic").charAt(0));
            byCaseTypeBtn.setSelected(false);
            byCaseTypeBtn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rebuildHearingTypes_actionPerformed(e);
                }
            });
        }
        return byCaseTypeBtn;
    }

    private ButtonGroup getHearingTypesBtnGrp() {
        if (hearingTypesBtnGrp == null) {
            hearingTypesBtnGrp = new ButtonGroup();
        }

        return hearingTypesBtnGrp;
    }

    private JRadioButton getExistingCaseRadio() {
        if (existingCaseRadio == null) {
            existingCaseRadio = new JRadioButton(getBundleEntry("AddHearingExistingCaseLabel"));
            existingCaseRadio.setMnemonic(getBundleEntry("AddHearingExistingCaseMnemonic").charAt(0));
            existingCaseRadio.setSelected(true);
            existingCaseRadio.setToolTipText(getBundleEntry("AddHearingExistingCaseToolTip"));
            existingCaseRadio.setActionCommand(getBundleEntry("AddHearingExistingCaseAction"));
            existingCaseRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    caseLifeCycleRadioSelection_actionPerformed(e);
                }
            });
        }

        return existingCaseRadio;
    }

    private JRadioButton getNewUCaseRadio() {
        if (newUCaseRadio == null) {
            newUCaseRadio = new JRadioButton(getBundleEntry("AddHearingNewUCaseLabel"));
            newUCaseRadio.setMnemonic(getBundleEntry("AddHearingNewUCaseMnemonic").charAt(0));
            newUCaseRadio.setSelected(false);
            newUCaseRadio.setToolTipText(getBundleEntry("AddHearingNewUCaseToolTip"));
            newUCaseRadio.setActionCommand(getBundleEntry("AddHearingNewUCaseAction"));
            newUCaseRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clearScreen();
                    caseLifeCycleRadioSelection_actionPerformed(e);
                }
            });
        }

        return newUCaseRadio;
    }

    private ButtonGroup getCaseLifeCycleBtnGrp() {
        if (caseLifeCycleBtnGrp == null) {
            caseLifeCycleBtnGrp = new ButtonGroup();
        }

        return caseLifeCycleBtnGrp;
    }

    /**
     * Executed whenever a case life-cycle radio button is selected
     * 
     * @param e
     */
    private void caseLifeCycleRadioSelection_actionPerformed(ActionEvent e) {
        stepUpdateViewState();
    }

    /**
     * Clears the text in various screen widgets
     */
    private void clearScreen() {
        getTxtFldCaseNumber().setText("");
    }

    /**
     * Manages the rebuildiong of the hearing type list.
     * 
     * @param e
     */
    private void rebuildHearingTypes_actionPerformed(ActionEvent e) {
        rebuildHearingTypeList(e.getActionCommand());
    }

    /**
     * Rebuilds the hearing type list depending on the 'filter' button that was
     * pressed
     * 
     * @param actionCommand
     */
    private void rebuildHearingTypeList(String actionCommand) {
        if (actionCommand.equalsIgnoreCase(getBundleEntry("AddHearingTop5TypesAction"))) {
            getHearingTypesList().setListData(top5RefHearingTypes);
        } else if (actionCommand.equalsIgnoreCase(getBundleEntry("AddHearingByCaseTypeAction"))) {
            Vector temp = new Vector();
            Iterator it = allRefHearingTypes.iterator();
            while (it.hasNext()) {
                PullDownListObject pdlo = (PullDownListObject) it.next();
                RefHearingTypeBasicValue rhtBV = (RefHearingTypeBasicValue) pdlo.getBasicValue();
                if ((getTxtFldCaseNumber().getText().startsWith(rhtBV.getCategory()))
                        || (isNewUCaseSelected() && rhtBV.getCategory().equalsIgnoreCase(CASE_TYPE_U))) {
                    temp.add(pdlo);
                }
            }
            getHearingTypesList().setListData(temp);
        } else {
            getHearingTypesList().setListData(distinctRefHearingTypes);
        }
    }

    /**
     * Determines whether or not the data matches the expression
     * 
     * @param expression -
     *            the regular expression to check the data against
     * @param data -
     *            the data to be checked
     * @return true - if the data is valid for the expression
     */
    private boolean isDataMatchesRegularExpression(String expression, String data) {
        boolean matched = false;

        try {
            RE regexp = new RE(expression);
            matched = regexp.match(data);
        } catch (RESyntaxException e) {
            matched = false;
        }

        return matched;
    }

    /**
     * Helper method to determine if the existing case radio button is selected
     * 
     * @return true - if the existing case radio button is selected
     */
    private boolean isExistingCaseSelected() {
        return getCaseLifeCycleBtnGrp().isSelected(getExistingCaseRadio().getModel());
    }

    /**
     * Helper method to determine if the new case radio button is selected
     * 
     * @return true - if the new case radio button is selected
     */
    private boolean isNewUCaseSelected() {
        return getCaseLifeCycleBtnGrp().isSelected(getNewUCaseRadio().getModel());
    }
}
