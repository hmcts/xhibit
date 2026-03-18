package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.SystemColor;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.services.joinder.JoinderControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caze.CaseHelper;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderIndictmentValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLogDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.IndictmentLogHelper;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2 - Change Request PAnel
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
 * @author Joseph Antoniou
 * @version 1.0
 */
public class ChangeRequestPanel extends JoinderIndictmentPanel {

    /** The text area which contains the content to be logged. */
    private JTextArea logTextArea = null;

    /**
     * Prevents activate reentrancy issues due to setVisible changes in the
     * underlying JDK 1.5
     */
    private boolean subActivateReentrancy = false;

    public ChangeRequestPanel(XWizardDialog wizardDialog) {

        super(wizardDialog);
        this.stepInitialise();
    }

    /**
     * Initialise the gui components.
     */
    public void stepInitialise() {

        String logIntro = ("The following text will be logged for each case when the joinder indictment will be created.\nPlease edit if necessary.");
        JTextArea logIntroTxtArea = new JTextArea();
        logIntroTxtArea.setText(logIntro);
        logIntroTxtArea.setEnabled(false);
        logIntroTxtArea.setEditable(false);
        logIntroTxtArea.setBackground(this.getBackground());
        logIntroTxtArea.setDisabledTextColor(SystemColor.controlText);
        logIntroTxtArea.setFont(XHIBITConstant.getCurrentFont());
        logIntroTxtArea.setWrapStyleWord(true);
        logIntroTxtArea.setLineWrap(true);
        logIntroTxtArea.setBorder(null);
        JScrollPane logIntroScroller = new JScrollPane(logIntroTxtArea);
        logIntroScroller.setBorder(null);
        logTextArea = new JTextArea();
        logTextArea.setFont(XHIBITConstant.getCurrentFont());
        logTextArea.setWrapStyleWord(true);
        logTextArea.setLineWrap(true);
        Dimension dialogDim = wizardDialog.getSize();
        logIntroTxtArea.setPreferredSize(new Dimension(dialogDim.width * (int) 0.35, dialogDim.height * (int) 0.15));
        logTextArea.setPreferredSize(new Dimension(dialogDim.width * (int) 0.35, dialogDim.height * (int) 0.3));
        // Add the components.
        this.setLayout(gbLayout);
        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        this.add(new JScrollPane(logIntroTxtArea), gbConstraints);
        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        this.add(new JScrollPane(logTextArea), gbConstraints);
    }

    private void populateText() {

        Iterator<XhbCaseBasicValue> caseBasicValuesIterator = model.getJoinderCases().iterator();
        XhbCaseBasicValue caseBasicValue = caseBasicValuesIterator.next();
        StringBuffer defaultJoinderLogTxt = new StringBuffer("Leave to join indictments for ");
        defaultJoinderLogTxt.append(caseBasicValue.getCaseType());
        defaultJoinderLogTxt.append(caseBasicValue.getCaseNumber());
        while (caseBasicValuesIterator.hasNext()) {
            caseBasicValue = caseBasicValuesIterator.next();
            defaultJoinderLogTxt.append(", ");
            defaultJoinderLogTxt.append(caseBasicValue.getCaseType());
            defaultJoinderLogTxt.append(caseBasicValue.getCaseNumber());
        }
        String joinderDate = XDateFormat.format(Calendar.getInstance(), XDateFormat.DATEFORMAT);
        defaultJoinderLogTxt.append(" on ");
        defaultJoinderLogTxt.append(joinderDate);
        if (model.getJudgeName() != null) {
            defaultJoinderLogTxt.append(" granted by ");
            defaultJoinderLogTxt.append(model.getJudgeName());
            defaultJoinderLogTxt.append(".");
        }
        logTextArea.setText(defaultJoinderLogTxt.toString());
    }

    public void stepActivate() throws CSRecoverableException {

        super.stepActivate();
        // Refer to JoinderIndictmentPanel superclass for reentrancy details
        if (subActivateReentrancy) {
            log.debug("ChargeRequestPanel - stepActivate reentrancy");
            return;
        } else {
            subActivateReentrancy = true;
        }
        wizardDialog.getButtonPanel().getNext().setEnabled(false);
        wizardDialog.getButtonPanel().getFinish().setEnabled(true);
        populateText();
    }

    public void stepUpdateViewState() {
    }

    public void stepValidate() {
    }

    public void stepDeactivateOnNext() {
    }

    /**
     * Sets reentrancy variable
     */
    public void stepDeactivateOnAll() {
        subActivateReentrancy = false;

        //If the user hits Previous we need to ensure Finish is disabled
		//as you can only save from the last Joinder panel
        wizardDialog.getButtonPanel().getFinish().setEnabled(false);
    }

    /**
     * Called when finish is pressed on the wizard dialog.
     *
     * @param update
     *            true if the panel is being saved.
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {

        if (wizardDialog.getLatestEvent() == XWizardDialog.FINISH_EVENT) {
            JoinderIndictmentValue joinderValue = model.getJoinderIndictmentValue();
            HashMap map = model.getDefendantMap().getDefOnCaseIdsMap();
            Set set = map.keySet();
            log.debug("====> DefendantOnCase map contains: " + map.size() + " entries.");
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Integer key = (Integer) i.next();
                log.debug("====> alias id = " + key.toString() + " original id = " + map.get(key));
            }
            joinderValue.setDefendantOnCaseAliases(model.getDefendantMap().getDefOnCaseIdsMap());
            joinderValue.setOriginalJoiningCaseIds(model.getJoinderCaseIds());
            joinderValue.setOriginalJoiningChargeIds(model.getJoinderChargeIds());
            joinderValue.getNewChargeValue().setCourtLogDate(Calendar.getInstance());
            // printCharge(joinderValue.getNewChargeValue());
            JoinderControllerBeanBusinessDelegate.DelegateFactory.getInstance().createJoinderIndictment(joinderValue,
            		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            // Now that the joinder indictment is created, update the CREST
            // Indictment log for each case that has had an Indicmtent
            // joined.
            updateCrestIndictmentLogs(model.getJoinderCases());
        }
    }

    /**
     * debug method to print offence details for the given charge.
     *
     * @param cv
     *            charge value
     */
    private void printCharge(ChargeValue cv) {

        log.debug("\n\nJoinder - Charge id = " + cv.getChargeID());
        Iterator i = cv.getOffenceValues().iterator();
        while (i.hasNext()) {
            OffenceValue ov = (OffenceValue) i.next();
            log.debug("\nOffence id       = " + ov.getOffenceID());
            log.debug("Ref Offence id   = " + ov.getRefOffenceID());
            log.debug("Ref Sys code id  = " + ov.getRefSystemCodeID());
            log.debug("Offence code     = " + ov.getOffenceCode());
            log.debug("Offence desc     = " + ov.getOffenceDescription());
            log.debug("Crest Off seq#   = " + ov.getCrestOffenceSeqNo());
            log.debug("Crest Off id     = " + ov.getCrestOffenceID());
        }
        log.debug("\nDone Charge id = " + cv.getChargeID() + "\n");
    }

    /**
     * Updates the Crest Indictment Log with the joinder details for each case
     * that has an Indictment joined.
     *
     * @param cases
     *            collection of CaseBasicValues for cases with Indictments that
     *            have been joined.
     * @throws CSRecoverableException
     */
    private void updateCrestIndictmentLogs(Collection<XhbCaseBasicValue> cases) throws CSRecoverableException {

        CrestIndictmentLog.getInstance().joinderIndictmentLog(cases, logTextArea.getText());
        CaseBasicValue caseBasicValue = null;
        
        for (XhbCaseBasicValue xhbCaseBasicValue : cases)  {
            
            // This is a temporary method that will be removed when the old entity 
            // layer is removed and XhbCaseBasicValue totally replaces CaseBasicValue
            caseBasicValue = CaseHelper.convertToCaseBasicValue(xhbCaseBasicValue);
            
            if (caseBasicValue.getId().equals(model.getJoinderCaseId())) {
                // Log for the current case will be updated when the user leaves
                // the charges screen
            } else {
                String originalLog = CrestIndictmentLog.getInstance().getCaseIndictmentLog(caseBasicValue);
                String logText = originalLog + logTextArea.getText().trim();
                // If the log text is too big for the crest indictment log
                // fields, display the Crest Indictment Log Dialog to let the
                // user make the decision about what text is saved.
                // Otherwise just write the text back to the midtier.
                if (logText.length() > CrestIndictmentLog.getInstance().logLengthLimit) {
                    CrestIndictmentLogDialog dialog = new CrestIndictmentLogDialog(wizardDialog, caseBasicValue,
                            logText, 400, 400);
                    dialog.setVisible(true);
                } else {
                    IndictmentLogHelper.updateIndictmentInfo(caseBasicValue, logText);
                    CrestIndictmentLog.getInstance().clearChanges(caseBasicValue);
                }
            }
        }
    }
}