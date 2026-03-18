package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.GridBagConstraints;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: EndHearingPanel
 * </p>
 * <p>
 * Description: The main panel for End Hearing court log events. The screen
 * takes two forms; one for case level end hearing events where no defendants
 * are known, and one for defendant level end hearing events where a defendant
 * must be associated with the event.
 * 
 * For defendant level events, the user may select one, many or all of the
 * defendants when creating the event however, when editing an existing
 * defendant level event, no defendant selection is allowed.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class EndHearingPanel extends CourtLogEventPanel {
    public static final String U_CASE = "U";

    public static final String B_CASE = "B";

    public static final String CASE_LEVEL_EVENT_CODE = "CASE_LEVEL_EVENT_CODE";

    public static final String DEFENDANT_LEVEL_EVENT_CODE = "DEFENDANT_LEVEL_EVENT_CODE";

    private final ApplicationCaseModel applicationCaseModel;

    private final EndHearingModel model;

    private JLabel eventNameLabel;

    /**
     * Public constructor. Saves the passed parameters, calls life-cycle method
     * to initialise a CourtLogCRUDValue and then drops components onto the
     * screen
     * 
     * @param parent -
     *            the dialog that invoked this panel
     * @param model -
     *            the model containing any passed in values
     * @throws CSRecoverableException
     */
    public EndHearingPanel(XDialog parent, EndHearingModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;
        this.applicationCaseModel = model.getXac().getApplicationCaseModel();

        stepInitialise();
        jbInit();
    }

    /**
     * Life-cycle method to initialise a CourtLogCRUDValue using the data for
     * the record being edited and saves the data in the model.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        if (isDefendantLevelEvent()) {
            if (model.isInEditMode()) {
                // Defendant selection is prohibited as the event may only be
                // related
                // to the defendant it was created for
                getCourtLogEventLevelPanel().setDefendantDisplayType(CourtLogEventLevelPanel.DEFENDANT_COMBO);
            } else {
                // Defendant selection is permitted since this is a new event
                getCourtLogEventLevelPanel().setDefendantDisplayType(CourtLogEventLevelPanel.DEFENDANT_LIST);
            }
        } else {
            getCourtLogEventLevelPanel().setProcessStepMethods(false);
        }

        super.stepInitialise();
    }

    /**
     * Add the components to the screen.
     * 
     * @throws CSRecoverableException
     */
    private void jbInit() {
        this.add(getEventNameLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        this.add(getCourtLogEventLevelPanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    private JLabel getEventNameLabel() {
        if (eventNameLabel == null) {
            eventNameLabel = new PanelTitleLabel(ResourceBundleHelper
                    .getResource(XhibitBundles.EndHearing, "eventName"));
        }

        return eventNameLabel;
    }

    /**
     * Life-cycle method to control the enabled state of the screen components.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        super.stepUpdateViewState();

        getCourtLogEventLevelPanel().getDefendantCbLabel().setEnabled(!model.isInEditMode());
        getCourtLogEventLevelPanel().getDisplayedWidget().setEnabled(!model.isInEditMode());
    }

    /**
     * Determines whether or not all mandatory fields have been completed
     * depending on event type. The result is used to enable/disable the OK
     * button.
     * 
     * For event type 30500, this is un-necessary as the user is not obliged to
     * enter any further information.
     * 
     * For event type 30600, the user is obliged to select at least one
     * defendant.
     * 
     * @return true if all mandatory fields are completed
     */
    protected boolean isMandatoryFieldsCompleted() {
        boolean result = true;

        if (isDefendantLevelEvent()) {
            result = getCourtLogEventLevelPanel().isItemSelected();
        }

        return result;
    }

    /**
     * Life-cycle method that is executed when the screen is destroyed. This is
     * generally as a result of the user clicking the OK/Cancel buttons. It
     * constructs a CourtLogCRUDValue for each record to be added/updated and
     * calls the appropriate method on the business delegate.
     * 
     * @param update -
     *            true if the user clicked the OK button
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            /**
             * If the event is in "add" mode, check if the end hearing event
             * should be applied to linked cases too
             */
            if (model.isInEditMode()) {
                model.setEndLinkedCases(false);
            } else if (applicationCaseModel.isLinked()) {
                model.setEndLinkedCases(endLinkedCases());
            }

            /**
             * There may be many records to add depending on event type: - case
             * level always has 1 - defendant level has one for each selected
             * defendant, so store all CRUD values in an array and call the
             * delegate method that expects multiple records
             */
            if (model.isInEditMode() || isCaseLevelEvent()) {
                super.stepDeinitialise(update);
            } else {
                super.getCourtLogEventLevelPanel().stepDeinitialise(update);

                int arrayLength = model.getSelectedDefendants().length;
                CourtLogCRUDValue[] crudArray = new CourtLogCRUDValue[arrayLength];

                for (int x = 0; x < model.getSelectedDefendants().length; x++) {
                    DefendantBasicValue dbv = (DefendantBasicValue) model.getSelectedDefendants()[x];

                    model.setDefendantId(dbv.getId());
                    model.setDefendantOnCaseId(getCourtLogEventLevelPanel().findDefOnCase(dbv.getId()).getId());
                    model.setDefendantName(PDHConstants.buildDefendantName(dbv));

                    crudArray[x] = createCRUDFromModel();
                }

                getCLCDelegate().newEntries(crudArray);
            }
        }
    }

    /**
     * Populates those elements of a court log CRUD value that are not dependant
     * on the level - defendant or case - of the event being processed.
     * 
     * @return - the popualted CourtLogCRUDValue
     */
    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        propertyMap.put(ResourceBundleHelper.getResource(XhibitBundles.EndHearing, "HEARING_ID"), applicationCaseModel
                .getScheduledHearingValue().getScheduledHearingBasicValue().getHearingID());

        propertyMap.put(ResourceBundleHelper.getResource(XhibitBundles.EndHearing, "SCHEDULED_HEARING_ID"),
                applicationCaseModel.getScheduledHearingId());

        propertyMap.put(ResourceBundleHelper.getResource(XhibitBundles.EndHearing, "PROCESS_LINKED_CASES"), model
                .isEndLinkedCases() ? "true" : "false");
    }

    /**
     * Asks the user whether they want the end hearing event propogated to
     * linked cases too
     * 
     * @return true if the end hearing event should be propogated to linked
     *         cases
     */
    private boolean endLinkedCases() throws CSRecoverableException {
        boolean result = true;

        String messageText = null;
        if (isCaseLevelCaseType(applicationCaseModel.getCaseType())) {
            messageText = ResourceBundleHelper.getResource(XhibitBundles.EndHearing, "applyTolinkedCasesForUC_Cases");
        } else {
            messageText = ResourceBundleHelper.getResource(XhibitBundles.EndHearing, "applyTolinkedCases");
        }

        int option = JOptionPane.showConfirmDialog(this, messageText, ResourceBundleHelper.getResource(
                XhibitBundles.EndHearing, "eventName"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        switch (option) {
        case JOptionPane.YES_OPTION:
            result = true;
            break;
        case JOptionPane.NO_OPTION:
            result = false;
            break;
        case JOptionPane.CLOSED_OPTION:
        case JOptionPane.CANCEL_OPTION:
        default:
            throw new UserCancelException();
        }

        /**
         * If the user answerd yes, i.e. the result of the confirmDialog is
         * true, then, if applicable, warn them that any linked case of a
         * different level must be ended seperately.
         */
        if (result) {
            if (isWarningRequired()) {
                showWarning(getWarningMessage());
            }
        }

        return result;
    }

    /**
     * Determines whether or not a warning message should be displayed.<br>
     * This will be because:
     * <ul>
     * <li>the case type of the the case that the event is for is of type B or
     * U and is linked to one or more cases of case type A, S or T
     * <li>the case type of the the case that the event is for is of type A, S
     * or T and is linked to one or more cases of case type B or U
     * </ul>
     * 
     * To check this, loop through the linked scheduled hearings and ensure that
     * all case types are of a type similar to the case for which the event is
     * to be created.
     * 
     * @return true if there is at least one mis-matched case type, i.e. a
     *         warning is required; false if all case types are of a similar
     *         type
     * @throws CSRecoverableException
     */
    private boolean isWarningRequired() throws CSRecoverableException {
        boolean result = false;
        int currentCaseId = applicationCaseModel.getCaseId().intValue();
        String currentCaseType = applicationCaseModel.getCaseType();

        CaseSchedHearingValue[] caseSchedHearingValueArray = XhibitDelegateHelper.getHearingDelegate()
                .getLinkedSchedHearingsByShId(applicationCaseModel.getScheduledHearingId());

        for (int x = 0; x < caseSchedHearingValueArray.length; x++) {
            CaseSchedHearingValue linkedCase = caseSchedHearingValueArray[x];

            if (currentCaseId == linkedCase.getCaseId().intValue())
                continue;

            if (isDefendantLevelCaseType(currentCaseType) && isCaseLevelCaseType(linkedCase.getCaseType())) {
                result = true;
                break;
            }

            if (isCaseLevelCaseType(currentCaseType) && isDefendantLevelCaseType(linkedCase.getCaseType())) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * Returns a string coresponding to the message to be displayed.
     * 
     * @return the message
     */
    private String getWarningMessage() {
        if (isDefendantLevelCaseType(applicationCaseModel.getCaseType())) {
            return ResourceBundleHelper.getResource(XhibitBundles.EndHearing, "caseLevelMismatch");
        } else {
            return ResourceBundleHelper.getResource(XhibitBundles.EndHearing, "defendantLevelMismatch");
        }

    }

    /**
     * Shows a message informing users that they need to seperately end cases of
     * a level different to the current case.
     * 
     * @param message -
     *            the message to be displayed
     */
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, ResourceBundleHelper.getResource(XhibitBundles.EndHearing,
                "eventName"), JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Determines whether or not the case type parameter passed is at case level
     * 
     * @param caseType
     * @return true if the case type is of type B or U
     */
    private boolean isCaseLevelCaseType(String caseType) {
        return (U_CASE.equalsIgnoreCase(caseType) || B_CASE.equalsIgnoreCase(caseType));
    }

    /**
     * Determines whether or not the case type parameter passed is at defendant
     * level
     * 
     * @param caseType
     * @return true if the case type is not of type B or U
     */
    private boolean isDefendantLevelCaseType(String caseType) {
        return (!(U_CASE.equalsIgnoreCase(caseType)) && !(B_CASE.equalsIgnoreCase(caseType)));
    }

    private boolean isCaseLevelEvent() {
        return isCaseLevelEvent(model.getEventType());
    }

    /**
     * Determines whether or not the event code is for a case level end hearing
     * event - 30500
     * 
     * @param eventType
     * @return
     */
    private boolean isCaseLevelEvent(String eventType) {
        return ResourceBundleHelper.getResource(XhibitBundles.EndHearing, CASE_LEVEL_EVENT_CODE).equalsIgnoreCase(
                eventType);
    }

    private boolean isDefendantLevelEvent() {
        return isDefendantLevelEvent(model.getEventType());
    }

    /**
     * Determines whether or not the event code is for a defendant level end
     * hearing event - 30600
     * 
     * @param eventType
     * @return
     */
    private boolean isDefendantLevelEvent(String eventType) {
        return ResourceBundleHelper.getResource(XhibitBundles.EndHearing, DEFENDANT_LEVEL_EVENT_CODE).equalsIgnoreCase(
                eventType);
    }
}