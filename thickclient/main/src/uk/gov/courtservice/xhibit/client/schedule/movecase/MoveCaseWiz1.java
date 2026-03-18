package uk.gov.courtservice.xhibit.client.schedule.movecase;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.InvalidHearingTimeException;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.HearingHeaderValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.MoveCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.HearingHeaderValueHelper;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT Prototype
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.1 This is the first window of the Move Case wizard. This allows
 *          the user to move the case to another time that day or court, or
 *          adjourn the case. The wizard is accessed from the Case Progress
 *          window. <p/> <Change History/>
 *          <P>
 *          17/04/2003 CO - Bug ID: 52467, sorting of courtrooms changed to be
 *          done via crestCourtRoomNo as done in the public displays
 *          </P>
 *          <p/>
 *          <P>
 *          17/04/2003 DC - Bug ID: 52410, background colour of time component
 *          set to SystemColor.text for disabled (and SystemColor.white for
 *          enabled)
 *          </P>
 */
/*
 * Ref Date Author Description
 * 
 * 72,52569 24-04-2003 AW Daley currentCaseType retreived from scheduled hearing
 * value and set on the MoveCaseModel
 */
public class MoveCaseWiz1 extends XPanel {
    private CaseDetailsPanel caseDetailsPanel;

    private MoveThisCasePanel moveThisCasePanel;

    private MoveCourtStaffPanel moveCourtStaffPanel;

    private JLabel adviceLabel;

    private JLabel advice2Label;

    private ResourceBundle myResource;

    private MoveCaseModel model;

    private JPanel buttonPanel;

    private XhbCourtRoomBasicValue courtRoomValue;

    private ScheduledHearingValue scheduledHearingValue;

    private DefaultListModel prosAdvmodel;

    private DefaultListModel defendantmodel;

    private DefaultListModel defAdvmodel;

    private Integer scheduleHearingID;

    private HearingHeaderValue hearingHeaderValue;

    private Collection colCourtRooms;

    private boolean isAppeal;

    private Frame parentFrame;

    // TEMP CODE CONSTRUCTOR
    public MoveCaseWiz1() throws CSRecoverableException {
        try {
            this.model = new MoveCaseModel();
            myResource = XHIBITConstant.getResourceBundle("XhibitTodaysScheduleResources");
            stepInitialise();
        } catch (java.util.MissingResourceException ex) {
            XHIBITConstant.error("ResourceBundle could not be found for " + Locale.getDefault());
            XHIBITConstant.error(ex);
        }

        jbInit();
    }

    // ACTUAL Constructor
    public MoveCaseWiz1(MoveCaseModel moveCaseModel, XDialog parent) throws CSRecoverableException {
        this.model = moveCaseModel;
        this.parentFrame = parent.getParentFrame();
        this.buttonPanel = parent.getButtonPanel();
        myResource = moveCaseModel.getMyResource();
        stepInitialise();

        jbInit();
    }

    void jbInit() // throws Exception
    {
        this.setLayout(new GridBagLayout());
        this.add(getCaseDetailsPanel(), new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getAdviceOneLabel(), new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(4, 10, 4, 4), 0, 0));
        this.add(getAdviceTwoLabel(), new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(4, 10, 4, 4), 0, 0));
        this.add(getMoveThisCasePanel(), new GridBagConstraints(0, 3, 1, 1, 0.7, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getMoveCourtStaffPanel(), new GridBagConstraints(1, 3, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
    }

    public CaseDetailsPanel getCaseDetailsPanel() {
        if (caseDetailsPanel == null) {
            caseDetailsPanel = new CaseDetailsPanel(myResource, isAppeal);
            caseDetailsPanel.setToolTipText(XHIBITConstant.getResource(myResource, "ttCaseDetailsPanel"));
            caseDetailsPanel.enableFields(false); // disable fields
        }
        return caseDetailsPanel;
    }

    public MoveThisCasePanel getMoveThisCasePanel() {
        if (moveThisCasePanel == null) {
            moveThisCasePanel = new MoveThisCasePanel(this, myResource);
            moveThisCasePanel.setToolTipText(XHIBITConstant.getResource(myResource, "ttMoveThisCasePanel"));
        }
        return moveThisCasePanel;
    }

    private JLabel getAdviceOneLabel() {
        if (adviceLabel == null) {
            adviceLabel = new JLabel(XHIBITConstant.getResource(myResource, "moveCaseAdvice1"));
        }
        return adviceLabel;
    }

    private JLabel getAdviceTwoLabel() {
        if (advice2Label == null) {
            advice2Label = new JLabel(XHIBITConstant.getResource(myResource, "moveCaseAdvice2"));
        }
        return advice2Label;
    }

    public MoveCourtStaffPanel getMoveCourtStaffPanel() {
        if (moveCourtStaffPanel == null) {
            moveCourtStaffPanel = new MoveCourtStaffPanel(myResource);
            moveCourtStaffPanel.setToolTipText(XHIBITConstant.getResource(myResource, "ttMoveCourtStaffPanel"));
        }
        return moveCourtStaffPanel;
    }

    /**
     * Given a collection of advocate names returns the names of the advocates
     * in a DefaultListModel.
     * 
     * @param advocates
     *            collection of (String) advocate names
     * @return DefaultListModel populated with names of advocates
     */
    private DefaultListModel getAdvocates(Collection advocates) {
        DefaultListModel listModel = new DefaultListModel();
        if (advocates != null) {
            String advocateName;
            Iterator i = advocates.iterator();
            while (i.hasNext()) {
                advocateName = (String) i.next();
                listModel.addElement(advocateName);
            }
        }
        return listModel;
    }

    private HearingScheduleControllerBeanBusinessDelegate getHSCDelegate() {
        return XhibitDelegateHelper.getHearingDelegate();
    }

    public void stepInitialise() throws CSRecoverableException {
        // Get SHV from Dialog
        scheduledHearingValue = model.getScheduledHearingValue();
        if (scheduledHearingValue == null) {
            throw new CSRecoverableException("gui.user.MoveCase.scheduleHearingValue",
                    "gui.log.MoveCase.scheduleHearingValue");
        }

        // Retrieve all data from shv
        model.setCurrentCase(scheduledHearingValue.getCaseNumber());
        model.setCurrentCaseType(scheduledHearingValue.getCaseType());
        model.setHearingType(scheduledHearingValue.getHearingTypeDesc());
        model.setJudge(scheduledHearingValue.getJudge());
        model.setTimeListed(XDateFormat.format(scheduledHearingValue.getNotBeforeTime(), XDateFormat.TIMEFORMAT));

        isAppeal = XHIBITConstant.isCriminalAppeal_CaseType(scheduledHearingValue)
                || XHIBITConstant.isMiscelleanousAppeal_CaseType(scheduledHearingValue);

        defendantmodel = new DefaultListModel();
        // Loop through Strings[] and add to Listmodel.
        String[] defendants = scheduledHearingValue.getDefendants();
        for (int i = 0; i < defendants.length; i++) {
            defendantmodel.addElement(defendants[i]);
        }
        model.setDefendantListModel(defendantmodel);

        scheduleHearingID = scheduledHearingValue.getScheduledHearingId();
        XHIBITConstant.debug("the id of the case/scheduledHearing to fetch is: " + scheduleHearingID);

        /*
         * Get the hearing header but pass in false for updating the sitting
         * information. We are not really open the case here and should
         * therefore not update the sitting information now since it will all be
         * blank if we do.
         */
        hearingHeaderValue = getHSCDelegate().getHearingHeader(scheduleHearingID, false,
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

        HearingHeaderValueHelper hearingHeaderHelper = new HearingHeaderValueHelper(hearingHeaderValue);

        if (hearingHeaderValue == null) {
            throw new CSRecoverableException("gui.user.TodaysScheduleController.courtLogHeader",
                    "gui.log.TodaysScheduleController.courtLogHeader");
        }

        Collection defence = hearingHeaderHelper.getLegalRepresentativesNamesByType(PersonValue.DEFENCE);
        if (isAppeal) {
            Collection respondent = hearingHeaderHelper.getLegalRepresentativesNamesByType(PersonValue.RESPONDENT);
            prosAdvmodel = getAdvocates(respondent);
            defAdvmodel = getAdvocates(defence);
            // prosAdvmodel = getAdvocates(defence);
            // defAdvmodel = getAdvocates(respondent);
        } else {
            Collection prosecution = hearingHeaderHelper.getLegalRepresentativesNamesByType(PersonValue.PROSECUTION);
            prosAdvmodel = getAdvocates(prosecution);
            defAdvmodel = getAdvocates(defence);
        }
        model.setDefAdvocateListModel(defAdvmodel);
        model.setProsAdvocateListModel(prosAdvmodel);

        model.setShortHandWriter("");
        Collection courtReporter = hearingHeaderHelper.getStaffNamesByType(PersonValue.COURT_REPORTER);
        if (courtReporter != null) {
            Iterator i = courtReporter.iterator();
            if (i.hasNext()) {
                String shorthandWriter = (String) i.next();
                model.setShortHandWriter(shorthandWriter);
            }
        }

        // XhbCourtRoomBasicValue[] courtRooms = ScheduleHelper.getDelegate().
        // getCourtStructure(XhibitSingleton.getInstance().getCourtId()).
        // getAllCourtRooms();
        XhbCourtRoomBasicValue[] courtRooms = XhibitSingleton.getInstance().getCourtStructureValue().getAllCourtRooms();

        XhbCourtRoomBasicValue[] availableCourtRooms = new XhbCourtRoomBasicValue[courtRooms.length + 1];
        availableCourtRooms[0] = new XhbCourtRoomBasicValue();
        availableCourtRooms[0].setDisplayName("");
        System.arraycopy(courtRooms, 0, availableCourtRooms, 1, courtRooms.length);

        model.setCourtComboModel(new DefaultComboBoxModel(availableCourtRooms));

        // Initialising setting for time panel
        getMoveThisCasePanel().getXTimePanel().setRequired(false);
        moveModelToScreen();
        // stepUpdateViewState(); //TEMP call to cover for StepActivate todo -
        // by RL
    }

    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();
        stepUpdateViewState();
    }

    public void stepUpdateViewState() {
        if (getMoveThisCasePanel().getAdjournedRb().isSelected()) {
            // no further data required
            ((OkCancelPanel) buttonPanel).okButton.setEnabled(true);
            getMoveThisCasePanel().getXTimePanel().setEnabled(false);
            getMoveThisCasePanel().getXTimePanel().setBackground(SystemColor.text);
            getMoveThisCasePanel().getCourtCb().setEnabled(false);
            getMoveCourtStaffPanel().getJudgeCbx().setEnabled(false);
            getMoveCourtStaffPanel().getCourtClerkCbx().setEnabled(false);
            getMoveCourtStaffPanel().getShortWriterCbx().setEnabled(false);
        } else {
            // set for time entry
            getMoveThisCasePanel().getXTimePanel().setEnabled(true);
            getMoveThisCasePanel().getXTimePanel().setBackground(Color.white);
            getMoveThisCasePanel().getCourtCb().setEnabled(true);

            // Court staff can only be moved if court room is changed
            if (getMoveThisCasePanel().getCourtCb().getSelectedIndex() > 0) {
                getMoveCourtStaffPanel().getJudgeCbx().setEnabled(true);
                getMoveCourtStaffPanel().getCourtClerkCbx().setEnabled(true);
                getMoveCourtStaffPanel().getShortWriterCbx().setEnabled(true);
            } else {
                getMoveCourtStaffPanel().getJudgeCbx().setEnabled(false);
                getMoveCourtStaffPanel().getCourtClerkCbx().setEnabled(false);
                getMoveCourtStaffPanel().getShortWriterCbx().setEnabled(false);
            }

            // Enabled OK if mandatory fields have been filled in
            getMoveThisCasePanel().getXTimePanel().setRequired(true);
            if (getMoveThisCasePanel().getXTimePanel().isMandatoryFieldsCompleted() == true
            // Part of PRE00180 and related fixes, time is always required.
            // || getMoveThisCasePanel().getCourtCb().getSelectedIndex() > 0
            ) {
                ((OkCancelPanel) buttonPanel).okButton.setEnabled(true);
            } else {
                ((OkCancelPanel) buttonPanel).okButton.setEnabled(false);
            }
            getMoveThisCasePanel().getXTimePanel().setRequired(false);
        }
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
    }

    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            MoveCaseValue moveCaseValue = new MoveCaseValue();
            moveCaseValue.setScheduledHearingId(scheduleHearingID);
            moveCaseValue.setCourtId(XhibitSingleton.getInstance().getCourtId());
            if (model.isAdjourned()) {
                moveCaseValue.setAdjourned(true);
                // may not need to do flags below...
                // No court staff will be moved.
                moveCaseValue.setUseExistingJudge(false);
                moveCaseValue.setUseExistingCourtClerk(false);
                moveCaseValue.setUseExistingSHWriter(false);
            } else // New Time Today
            {
                if (model.getCourtRoomId() == null) {
                    // No court staff will be moved.
                    moveCaseValue.setUseExistingJudge(false);
                    moveCaseValue.setUseExistingCourtClerk(false);
                    moveCaseValue.setUseExistingSHWriter(false);
                } else {
                    // Court room has been selected so include staff details
                    moveCaseValue.setNewCourtRoomId(model.getCourtRoomId());
                    moveCaseValue.setUseExistingJudge(model.isMoveJudge());
                    moveCaseValue.setUseExistingCourtClerk(model.isMoveCourtClerk());
                    moveCaseValue.setUseExistingSHWriter(model.isMoveShorthandWriter());
                    moveCaseValue.setOldCourtRoomId(scheduledHearingValue.getCourtRoomBasicValue().getId());
                }
                // Change to Date from Calendar*/
                if (model.getTime() != null) {
                    Calendar cal = model.getTime();
                    cal.set(Calendar.SECOND, 0);
                    Date newtime = cal.getTime();
                    moveCaseValue.setNewHearingTime(newtime);
                }
            }

            // write back VO to BG
            try {
                getHSCDelegate().moveCase(moveCaseValue,
                        XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            } catch (InvalidHearingTimeException e) {

                JOptionPane.showConfirmDialog(parentFrame, XHIBITConstant.getResource(myResource,
                        "moveCaseTimeErrorMessage"), XHIBITConstant.getResource(myResource, "moveCaseTimeErrorTitle"),
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

                throw new UserCancelException();
            }
        }
    }

    private void moveModelToScreen() {
        // CaseDetails
        getCaseDetailsPanel().getCurrCaseText().setText(model.getCurrentCaseType() + model.getCurrentCase());
        getCaseDetailsPanel().getHearingTypeText().setText("" + model.getHearingType());
        getCaseDetailsPanel().getTimeListedText().setText("" + model.getTimeListed());
        getCaseDetailsPanel().getShortWriterText().setText("" + model.getShortHandWriter());
        getCaseDetailsPanel().getJudgeText().setText("" + model.getJudge());
        if (model.getProsAdvocateListModel() != null) {
            getCaseDetailsPanel().getProsAdvocList().setModel(model.getProsAdvocateListModel());
        }
        if (model.getDefendantListModel() != null) {
            getCaseDetailsPanel().getDefendantList().setModel(model.getDefendantListModel());
        }
        if (model.getDefAdvocateListModel() != null) {
            getCaseDetailsPanel().getDefAdvocList().setModel(model.getDefAdvocateListModel());
        }
        // Move this case
        getMoveThisCasePanel().getNewTimeTodayRb().setSelected(!model.isAdjourned());

        if (model.getCourtComboModel() != null) {
            getMoveThisCasePanel().getCourtCb().setModel(model.getCourtComboModel());
        }

        getMoveThisCasePanel().getXTimePanel().setTime(model.getTime());
        // Move Staff
        getMoveCourtStaffPanel().getJudgeCbx().setSelected(model.isMoveJudge());
        getMoveCourtStaffPanel().getShortWriterCbx().setSelected(model.isMoveShorthandWriter());
        getMoveCourtStaffPanel().getCourtClerkCbx().setSelected(model.isMoveCourtClerk());
    }

    private void moveScreenToModel() throws CSValidationException {
        model.setAdjourned(getMoveThisCasePanel().getAdjournedRb().isSelected());

        if (getMoveThisCasePanel().getCourtCb().getSelectedItem() != null
                && getMoveThisCasePanel().getCourtCb().getSelectedItem() != "") {
            courtRoomValue = (XhbCourtRoomBasicValue) getMoveThisCasePanel().getCourtCb().getSelectedItem();
            model.setCourtRoomId(courtRoomValue.getCourtRoomId()); // only
            // when
            // courtroomid
            // is an
            // integer!!
        }
        model.setTime(getMoveThisCasePanel().getXTimePanel().getDate());

        model.setMoveJudge(getMoveCourtStaffPanel().getJudgeCbx().isSelected());
        model.setMoveShorthandWriter(getMoveCourtStaffPanel().getShortWriterCbx().isSelected());
        model.setMoveCourtClerk(getMoveCourtStaffPanel().getCourtClerkCbx().isSelected());

    }
}
