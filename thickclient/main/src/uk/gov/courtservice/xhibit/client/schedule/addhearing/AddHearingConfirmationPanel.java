package uk.gov.courtservice.xhibit.client.schedule.addhearing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.text.MessageFormat;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.AddHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.im.helper.IMLocationHelper;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessageServices;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessagingServicesFactory;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.WizardButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: AddHearingConfirmationPanel
 * </p>
 * <p>
 * Description: Displays a confirmation message stating the users selections in
 * narrative form
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class AddHearingConfirmationPanel extends XPanel {
    private static final Logger LOG = CSServices.getLogger(AddHearingConfirmationPanel.class);

    private final AddHearingModel model;

    private final WizardButtonPanel buttonPanel;

    private JLabel confirmationMessageLbl;

    private JScrollPane scrollPane;

    private JTextArea confirmationMessageTxtArea;

    private Dimension msgDim = new Dimension(350, XHIBITConstant.getLineHeight() * 10);

    public AddHearingConfirmationPanel(AddHearingModel model, WizardButtonPanel buttonPanel)
            throws CSRecoverableException {
        this.model = model;
        this.buttonPanel = buttonPanel;

        LOG.debug(" AddHearingConfirmationPanel - model: ");

        stepInitialise();
        jbInit();
    }

    /**
     * Empty implementation of a life-cycle method
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        // Nothing to implement
    }

    /**
     * Life-cycle method execute when the user navigates to the previous screen
     * or selects the 'Finish' button.
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }

    /**
     * Empty implementation of a life-cycle method
     * 
     * @throws
     * uk.gov.courtservice.framework.services.validation.CSValidationException
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // Nothing to implement
    }

    /**
     * Life-cycle method to set the enabled state of screen components
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        buttonPanel.getBack().setEnabled(true);
        buttonPanel.getNext().setEnabled(false);
        buttonPanel.getFinish().setEnabled(true);
    }

    /**
     * Life-cycle method executed when the user selects the 'Finish' button and
     * all data is valid. In this instance, a new hearing VO is instantiated and
     * populated using the data entered by the user. If defendants are available
     * for selection but the user has not selected any, they are provided with
     * the opportunity to abandon the update otherwise a method is then called
     * on the Hearing Schedule business delegate to add the hearing to the
     * schedule and a JMS message is sent to the court room receiving the
     * hearing informing them of the new schedule.
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" AddHearingConfirmationPanel - stepDeInitialise ");
        }

        if (update) {
            if (LOG.isDebugEnabled()){
            LOG.debug(" Court Id        : " + model.getCourtId());
            LOG.debug(" Court Room Id   : " + model.getSelectedCourtRoomId());
            LOG.debug(" Case Number     : " + model.getCaseNumber());
            LOG.debug(" Case Type       : " + model.getCaseType());
            if (model.getDefendants() != null){
                LOG.debug(" Defendants      : " + model.getDefendants().toArray().toString());
            }
            LOG.debug(" Not Before Time : " + model.getTime().toString());
            LOG.debug(" Hearing Type    : " + model.getRefHearingTypeBasicValue().getId());
            LOG.debug(" New 'U' Case?   : "
                    + model.getCaseLifeCycleCommand().equalsIgnoreCase(getBundleEntry("AddHearingNewUCaseAction")));
            }
            String regex = "[AST]";
            
            // Check if defendants have been selected when there are
            // defendants in the list to select or case type is 'A,S,T'. 
            //If no defendant is select then prevent the user continuing and inform the user.

            if ((model.getDefendants() != null && model.getDefendants().size() > 0)
            ||  (model.getCaseType()   != null && model.getCaseType().matches(regex)))
            {
                if (!model.getCaseType().equals("B")) { // For B cases this is not an error
                    if (model.getSelectedBWHDefendants() != null && model.getSelectedBWHDefendants().size() == 0) {
                        JOptionPane.showConfirmDialog(
                            this,
                            getBundleEntry("AddHearingDefendantsErrorMsg"),
                            getBundleEntry("AddHearingDefendantsErrorTitle"),
                            JOptionPane.CLOSED_OPTION,
                            JOptionPane.ERROR_MESSAGE
                        );
                            
                        buttonPanel.getFinish().setEnabled(false);
                        throw new UserCancelException();
                    }
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("This is a B Case so no defendants need to be selected, even if they have been created as part of creating a Bail Order");
                    }
                }
            }

            try {
                if (model.isNewUCase()) {
                    // Create the new U case and save the details in the
                    // model for future use
                    CaseBasicValue acv = createNewUCase();
                    model.setCaseType(acv.getCaseType());
                    model.setCaseNumber(acv.getCaseNumber());

                    // Inform the user of the new case number
                    JOptionPane.showMessageDialog(this, buildNewUCaseMessage(),
                            getBundleEntry("AddHearingConfirm.newUCaseTitle"), JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                throw new CSRecoverableException("gui.user.businessdelegateinstantiation",
                        new Object[] { "HearingScheduleControllerBusinessDelegate" },
                        "Exception whilst instantiating the HearingScheduleController BusinessDelegate", e);
            }

            // Populate the hearing VO with the details of the new hearing
            AddHearingValue addHearingValue = new AddHearingValue();
            addHearingValue.setCourtId(model.getCourtId());
            addHearingValue.setCourtRoomId(model.getSelectedCourtRoomId());
            addHearingValue.setCaseNumber(model.getCaseNumber());
            addHearingValue.setCaseType(model.getCaseType());
            addHearingValue.setDefendants(model.getSelectedBWHDefendants());
            addHearingValue.setNotBeforeTime(model.getTime());
            addHearingValue.setRefJudgeID(model.getRefJudgeId());
            addHearingValue.setRefHearingTypeId(model.getRefHearingTypeBasicValue().getId());

            // Create the hearing
            XhibitDelegateHelper.getHearingDelegate().addHearing(addHearingValue,
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

            // If a JMS queue name is available, inform the receiving court
            // room
            try {
                String message = buildJMSMessage();
                String location = buildLocation();

                if (location != null && location.length() > 0) {
                    String topic = IMLocationHelper.getTopicIDForLocation(location);
                    InstantMessageServices ims = InstantMessagingServicesFactory.getInstance()
                            .getSubscriptionMessagingServices();
                    ims.publishTextMessage(message, topic);
                }
            } catch (Exception e) {
                LOG.debug("JMS Messaging Error with Add Hearing: " + e.getMessage());
                JOptionPane.showMessageDialog(this, getBundleEntry(XhibitBundles.ErrorText,
                        "gui.addHearing.jmserror.message"), getBundleEntry(XhibitBundles.ErrorText,
                        "gui.addHearing.jmserror.title"), JOptionPane.WARNING_MESSAGE);
            }

            // Notify the user to not open the case in a few minutes
            JOptionPane.showMessageDialog(this, getBundleEntry("AddHearingDefendantsInfoMsg"));
        }
    }

    /**
     * Builds a location that represents the court, court site and court room of
     * the PC that will be notified of the added hearing. The returned value
     * will be of the form /isleworth_crown_court/court_site_i/court_room_1
     * 
     * @return String representing the location of PCs to receive the message
     * @throws CSRecoverableException
     */
    private String buildLocation() throws CSRecoverableException {
        StringBuffer buf = new StringBuffer();

        // Obtain details of the court
        CourtBasicValue cbv = XhibitSingleton.getInstance().getCourtBasicValue(model.getCourtId());

        // Obtain details of the court site
        XhbCourtSiteBasicValue[] courtSites = XhibitSingleton.getInstance().getCourtStructureValue().getCourtSites();
        XhbCourtSiteBasicValue csbv = null;
        for (int i = 0; i < courtSites.length; i++) {
            if (courtSites[i].getCourtSiteId().intValue() == model.getCourtSiteId().intValue()) {
                csbv = courtSites[i];
                break;
            }
        }

        // Obtain details of the court room
        XhbCourtRoomBasicValue crbv = model.getCourtRoomBasicValue();

        // If all details are available, then construct the location value
        if (cbv != null && csbv != null && crbv != null) {
            buf.append("/");
            buf.append(cbv.getDisplayName().replace(' ', '_'));
            buf.append("/");
            buf.append(csbv.getDisplayName().replace(' ', '_'));
            buf.append("/");
            buf.append(crbv.getDisplayNameNoSite().replace(' ', '_'));
        }

        // Convert the value to lower case string and return it
        return buf.toString().toLowerCase();
    }

    /**
     * Build an AddCaseValue VO from the model and call the delegate to create a
     * new 'U' case via the Mercator integration facade
     * 
     * @return AddCaseValue - the details of the new 'U' case that has been
     *         created
     * @throws HearingScheduleException
     * @throws Exception
     */
    private CaseBasicValue createNewUCase() throws HearingScheduleException, Exception {
        AddCaseValue acv = new AddCaseValue();
        acv.setCaseNumber(null);
        acv.setCaseType("U");
        acv.setCourtID(model.getCourtId());
        acv.setCaseTitle(model.getCaseTitle());
        acv.setCreateCaseOnCrest(true);
     
        return XhibitDelegateHelper.getHearingDelegate().createNewUCase(acv, XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
    }

    /**
     * Formats the message to be seen by the JMS queue subscriber
     * 
     * @param String
     *            caseType
     * @param Integer
     *            caseNumber
     * @return String - the JMS message to be sent
     * @throws CSRecoverableException
     */
    private String buildJMSMessage() throws CSRecoverableException {
        return MessageFormat.format(getBundleEntry("AddHearingConfirm.hearingAddedMessage"), new Object[] {
                XhibitSingleton.getInstance().getCourtRoomBasicValue().getDisplayName(),
                model.getRefHearingTypeBasicValue().getHearingTypeDesc(), model.getCaseTypeAndNumber(),
                XDateFormat.format(model.getTime(), XDateFormat.TIMEFORMAT) });
    }

    /**
     * Formats the message to inform the user of the new 'U' case number
     * 
     * @return String
     * @throws CSRecoverableException
     */
    private String buildNewUCaseMessage() throws CSRecoverableException {
        return MessageFormat.format(getBundleEntry("AddHearingConfirm.newUCaseMessage"), new Object[] { model
                .getCaseTypeAndNumber() });
    }

    /**
     * Life-cycle method called by the framework when the screen is made
     * visible.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();
        stepUpdateViewState();
    }

    /**
     * Add components to the screen
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());

        final JPanel confirmHearingPanel = new JPanel();

        confirmHearingPanel.setLayout(new GridBagLayout());
        confirmHearingPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148,
                145, 140)), getBundleEntry("AddHearingConfirmHearing")));

        final GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0);

        confirmHearingPanel.add(getScrollPane(), constraints);

        // Add the confirm hearing panel to this main panel
        this.add(confirmHearingPanel);
    }

    /**
     * Pseudo life-cycle method called a a consequence of making the screen
     * visible
     */
    private void moveModelToScreen() {
        getConfirmationMessageTxtArea().setText(buildScreenMessage());
    }

    /**
     * Empty implementation of a pseudo life-cycle event called when the user
     * navigates off the screen of selects 'Finish'
     */
    private void moveScreenToModel() {
        // Nothing to implement
    }

    /**
     * Formats the message to be seen by the user detailing their selections in
     * narrative form taking data from the model. The message consists of three
     * parts:
     * <ul>
     * <li>the generic message
     * <li>judge's details
     * <li>selected defendant's details
     * </ol>
     * 
     * @return String - representing the message to be displayed
     */
    private String buildScreenMessage() {
        StringBuffer buf = new StringBuffer();
        String defendantsMessage = null;
        String genericMessage = null;
        String judgeMessage = null;
        String temp;

        String caseSummary = (model.isExistingCase() ? model.getCaseTypeAndNumber() : model.getCaseTitle());
        genericMessage = MessageFormat.format(getBundleEntry("AddHearingConfirm.genericMessage"), new Object[] {
                model.getRefHearingTypeBasicValue().getHearingTypeDesc(), caseSummary,
                model.getCourtRoomBasicValue().getDisplayName(),
                XDateFormat.format(model.getTime(), XDateFormat.TIMEFORMAT) });

        temp = null;
        if (model.getJudgeName().length() == 0) {
            temp = getBundleEntry("AddHearingConfirm.judgeMessage.noJudge");
        } else {
            temp = MessageFormat.format(getBundleEntry("AddHearingConfirm.judgeMessage.judgeName"),
                    new Object[] { model.getJudgeName() });
        }
        judgeMessage = MessageFormat.format(getBundleEntry("AddHearingConfirm.judgeMessage"), new Object[] { temp });

        // The defendants message is only required if the case number is known
        if (model.isExistingCase()) {
            temp = null;
            
            if (model.getDefendants() == null || model.getDefendants().size() == 0 || model.getCaseType().equals("B")) {
                temp = getBundleEntry("AddHearingConfirm.defendantsMessage.noDefendantsAvailable");
            } else {
                if (model.getSelectedBWHDefendants() == null || model.getSelectedBWHDefendants().size() == 0) {
                    temp = getBundleEntry("AddHearingConfirm.defendantsMessage.noDefendantsSelected");
                } else {
                    temp = MessageFormat.format(getBundleEntry("AddHearingConfirm.defendantsMessage.defendantNames"),
                            new Object[] { buildDefendantNames() });
                }
            }
            defendantsMessage = MessageFormat.format(getBundleEntry("AddHearingConfirm.defendantsMessage"),
                    new Object[] { temp });
        }

        // Piece all the parts of the message together
        buf.append(genericMessage);
        buf.append("  ");
        buf.append(judgeMessage);
        if (defendantsMessage != null) {
            buf.append("  ");
            buf.append(defendantsMessage);
        }

        return buf.toString();
    }

    /**
     * Utility method to build defendant names.
     * 
     * @return
     */
    private String buildDefendantNames() {
        StringBuffer buf = new StringBuffer();
        boolean firstName = true;
        int x = 0;

        Iterator it = model.getSelectedBWHDefendants().iterator();
        while (it.hasNext()) {
            DefendantValue temp = (DefendantValue) it.next();

            x++;

            if (firstName)
                firstName = false;
            else {
                buf.append(x == model.getSelectedBWHDefendants().size() ? " and " : ", ");
            }

            buf.append(checkNull(temp.getFirstName()));
            buf.append(temp.getFirstName() == null ? "" : " ");
            buf.append(checkNull(temp.getMiddleName()));
            buf.append(temp.getMiddleName() == null ? "" : " ");
            buf.append(checkNull(temp.getSurName()));
        }

        return buf.toString();
    }

    private String checkNull(String checkString) {
        return (checkString == null ? "" : checkString);
    }

    private JLabel getConfirmationMessageLbl() {
        if (confirmationMessageLbl == null) {
            confirmationMessageLbl = new JLabel();
        }
        return confirmationMessageLbl;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane(getConfirmationMessageTxtArea());
            scrollPane.setBorder(null);
            scrollPane.setMinimumSize(msgDim);
            scrollPane.setPreferredSize(msgDim);
        }
        return scrollPane;
    }

    private JTextArea getConfirmationMessageTxtArea() {
        if (confirmationMessageTxtArea == null) {
            confirmationMessageTxtArea = new JTextArea();
            confirmationMessageTxtArea.setEditable(false);
            confirmationMessageTxtArea.setBackground(SystemColor.text);
            confirmationMessageTxtArea.setWrapStyleWord(true);
            confirmationMessageTxtArea.setLineWrap(true);
            confirmationMessageTxtArea.setFont(XHIBITConstant.getCurrentFont());
        }
        return confirmationMessageTxtArea;
    }

    /**
     * Obtains a bundle entry assuming TodaysSchedule as the source
     * 
     * @param param -
     *            the name of the entry required
     * @return String - the value coresponding to the name
     */
    private String getBundleEntry(String param) {
        // Assume bundle is TodaysSchedule
        return getBundleEntry(XhibitBundles.TodaysSchedule, param);
    }

    /**
     * Return the entry from the specified bundle
     * 
     * @param bundle -
     *            the bundle to search
     * @param param -
     *            the name of the entry to retrieve
     * @return String - the value coresponding to the name
     */
    private String getBundleEntry(String bundle, String param) {
        return ResourceBundleHelper.getResource(bundle, param);
    }
}
