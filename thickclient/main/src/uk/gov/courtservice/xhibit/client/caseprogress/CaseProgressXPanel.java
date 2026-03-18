package uk.gov.courtservice.xhibit.client.caseprogress;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.TableModel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.appealresults.AppealResultsHelper;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;

/**
 * <p>
 * Title: XHIBIT 2 - Case Progress
 * </p>
 * <p>
 * Description: The XPanel of this module that is responsible for displaying the
 * correct business panel. i.e whether to display charge information, criminal,
 * or miscellaneous appeal
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

public class CaseProgressXPanel extends XPanel {
    protected static final int TRIAL = 0;

    protected static final int CRIMINAL_APPEAL = 1;

    protected static final int MISCELLANEOUS_APPEAL = 2;

    private XhibitApplicationController xhibitController;

    private ResultsHelper resultsHelper;

    private CaseBasicValue caseBasicValue = null;

    private ScheduledHearingValue hearingValue = null;

    private ResultsCompositeValue resultsCompositeValue;

    private ResultsRowValue caseLevelResultRow;

    private TableModel criminalTableModel = null;

    private ArrayList enableActionList = null;

    /**
     * Create a case progress xpanel.
     * 
     * @param xhibitController
     *            the main application.
     * @throws CSRecoverableException
     */
    public CaseProgressXPanel(XhibitApplicationController xhibitController) throws CSRecoverableException {
        super();
        this.xhibitController = xhibitController;
        this.stepInitialise();
    }

    /**
     * XPanel method
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        this.setLayout(new GridBagLayout());

        ApplicationCaseModel caseModel = xhibitController.getApplicationCaseModel();

        caseModel.refresh();
        hearingValue = caseModel.getScheduledHearingValue();
        caseBasicValue = hearingValue.getCaseBasicValue();

        resultsHelper = new ResultsHelper(caseModel);
        resultsCompositeValue = resultsHelper.getResultsCompositeValue();

        if (CaseTypeHelper.isCriminalAppeal_CaseType(hearingValue)) {
            displayCriminalAppealPanel();
            return;
        }
        if (CaseTypeHelper.isMiscelleanousAppeal_CaseType(hearingValue)) {
            displayMiscellaneousAppealPanel();
            return;
        }
        if (CaseTypeHelper.isNormal_CaseType(hearingValue)) {
            CaseProgressHelper.clearPleaText();

            displayTrialPanel();
            return;
        }
    }

    private ArrayList getActions() {
        if (enableActionList == null) {
            enableActionList = new ArrayList();
        }
        return enableActionList;
    }

    /**
     * XPanel method
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        Iterator iter = getActions().iterator();
        while (iter.hasNext()) {
            XAction item = (XAction) iter.next();
            item.setEnabled(true);
        }
    }

    /**
     * XPanel method
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
    }

    /**
     * XPanel method - no implementation as validation not necessary for
     * READ-ONLY information.
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
    }

    /**
     * XPanel method
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        Iterator iter = getActions().iterator();
        while (iter.hasNext()) {
            XAction item = (XAction) iter.next();
            item.setEnabled(false);
        }
    }

    /**
     * XPanel method
     * 
     * @param update
     *            true if screen is being saved.
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    /**
     * Display the criminal appeal panel.
     */
    private void displayCriminalAppealPanel() throws CSRecoverableException {
        // Case Level Appeal Results
        caseLevelResultRow = resultsHelper.getCaseResultsRowValue();

        String caseLevelResultText = "";
        String caseLevelResultDate = "";
        if (caseLevelResultRow != null) {
            RefSystemCodeBasicValue rscbv = AppealResultsHelper.getRefSystemCodeBasicValue(caseLevelResultRow,
                    AppealResultsHelper.getCaseCrimRefData());
            caseLevelResultText = (rscbv != null) ? rscbv.getDecode() : "";

            if (caseLevelResultRow.getVerdictValue() != null) {
                caseLevelResultDate = XDateFormat.format(caseLevelResultRow.getVerdictValue().getVerdictDate(),
                        XDateFormat.DATEFORMAT);
            }
        }

        // Offence Level Appeal Results
        ArrayList crimAppeal = resultsHelper.getResultsForCharge(ChargeTypes.CRIMINAL_APPEAL_DISPOSAL.getChargeType());

        ArrayList defDisposals = resultsHelper.getResultsForCharge(ResultsHelper.CHARGETYPE_UNRELATED_PROGRESS);

        if (crimAppeal == null)
            crimAppeal = new ArrayList();
        if (defDisposals == null)
            defDisposals = new ArrayList();

        criminalTableModel = new CriminalTableModel(crimAppeal.toArray());

        CriminalDefendantDisposalTableModel defDisposalTableModel = new CriminalDefendantDisposalTableModel(
                defDisposals.toArray());

        // Create Criminal Appeal Panel passing in both case and offence level
        // information
        CriminalAppealPanel cap = new CriminalAppealPanel(xhibitController, caseLevelResultText, caseLevelResultDate,
                criminalTableModel, defDisposalTableModel, buildJudgesComments());

        this.removeAll();
        this.add(cap, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                XHIBITConstant.containerInsets, 0, 0));
    }

    /**
     * Display the miscellaneous appeal panel.
     */
    private void displayMiscellaneousAppealPanel() {
        ResultsRowValue rrv = resultsHelper.getCaseResultsRowValue();

        String title = caseBasicValue.getCaseTitle();

        StringBuffer sb = new StringBuffer();
        String[] defendants = hearingValue.getDefendants();
        for (int i = 0; i < defendants.length; i++) {
            sb.append(defendants[i]);
            if (i < (defendants.length - 1))
                sb.append(", ");
        }

        String appType = "";
        String appResult = "";
        String appResultDate = "";
        String transferredTo = "";
        String hearingStartDate = "";
        Long hearingDuration = null;

        if (rrv != null) {
            appType = rrv.getCaseSubType();
            if (rrv.getVerdictValue() != null) {
                appResult = rrv.getVerdictValue().getRefVerdictDesc();
                appResultDate = XDateFormat.format(rrv.getVerdictValue().getVerdictDate(), XDateFormat.DATEFORMAT);
                hearingStartDate = XDateFormat.format(rrv.getVerdictValue().getHearingDate(), XDateFormat.DATEFORMAT);
                hearingDuration = rrv.getVerdictValue().getLastCalculatedDuration();

                if (rrv.getVerdictValue().getRefVerdictCode().equals("TO")) {
                    transferredTo = rrv.getVerdictValue().getCccTransToRefCourtCode() + " - "
                            + rrv.getVerdictValue().getCccTransToRefCourtDesc();
                }
            }
        }

        MiscellaneousAppealPanel map = new MiscellaneousAppealPanel(title, appType, sb.toString(), appResult,
                appResultDate, hearingStartDate, hearingDuration, transferredTo);

        this.removeAll();
        this.add(map, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.CENTER, XHIBITConstant.containerInsets, 0, 0));
    }

    /**
     * Builds StringBuffer of Judges comments retrieved from
     * ResultsCompositeValue
     */
    private String buildJudgesComments() {
        int caseAppReasonCount = resultsCompositeValue.getCaseAppReasonValueCount();
        if (0 < caseAppReasonCount) {
            StringBuffer judgesComments = new StringBuffer();
            String appReason = resultsCompositeValue.getCaseAppReasonValue(0).getAppReason();
            judgesComments.append(appReason != null ? appReason : "");
            for (int i = 1; i < caseAppReasonCount; i++) {
                appReason = resultsCompositeValue.getCaseAppReasonValue(i).getAppReason();
                judgesComments.append('\n');
                judgesComments.append(appReason != null ? appReason : "");
            }
            return judgesComments.toString();
        } else {
            return "";
        }
    }

    /**
     * Display the trial panel.
     */
    private void displayTrialPanel() {
        ArrayList indictments = this.resultsHelper.getResultsForCharge(ChargeTypes.INDICTMENT_DISPOSAL.getChargeType());
        ArrayList breaches = this.resultsHelper.getResultsForCharge(ChargeTypes.BREACH_DISPOSAL.getChargeType());
        ArrayList commitals = this.resultsHelper.getResultsForCharge(ChargeTypes.COMMITAL_FOR_SENTENCE_DISPOSAL
                .getChargeType());
        ArrayList section41s = this.resultsHelper.getResultsForCharge(ChargeTypes.SECTION_41_DISPOSAL.getChargeType());
        ArrayList fail2Appears = this.resultsHelper.getResultsForCharge(ChargeTypes.FAIL2APPEAR_DISPOSAL.getChargeType());
        ArrayList defDisposals = this.resultsHelper.getResultsForCharge(ResultsHelper.CHARGETYPE_UNRELATED);
        ArrayList emptyArray = new ArrayList();

        TableModel indictmentTableModel = null;
        TableModel Section41TableModel = null;
        TableModel CommitalsTableModel = null;
        TableModel BreachTableModel = null;
        TableModel DefDisposalTableModel = null;
        TableModel fail2AppearTableModel = null;

        if (indictments != null) {
            indictmentTableModel = new IndictmentTableModel(indictments.toArray());
        } else {
            indictmentTableModel = new IndictmentTableModel(emptyArray.toArray());
        }
        if (section41s != null) {
            Section41TableModel = new Section41TableModel(section41s.toArray());
        } else {
            Section41TableModel = new Section41TableModel(emptyArray.toArray());
        }
        if (commitals != null) {
            CommitalsTableModel = new CommitalsTableModel(commitals.toArray());
        } else {
            CommitalsTableModel = new CommitalsTableModel(emptyArray.toArray());
        }
        if (breaches != null) {
            BreachTableModel = new BreachTableModel(breaches.toArray());
        } else {
            BreachTableModel = new BreachTableModel(emptyArray.toArray());
        }
        if (fail2Appears != null ){
            fail2AppearTableModel = new Fail2AppearTableModel(fail2Appears.toArray());
        } else {
            fail2AppearTableModel = new Fail2AppearTableModel(emptyArray.toArray());
        }
        if (defDisposals != null) {
            DefDisposalTableModel = new DefendantDisposalTableModel(defDisposals.toArray());
        } else {
            DefDisposalTableModel = new DefendantDisposalTableModel(emptyArray.toArray());
        }

        CaseProgressChargesPanel chargesPanel = new CaseProgressChargesPanel(xhibitController, indictmentTableModel,
                Section41TableModel, CommitalsTableModel, BreachTableModel, fail2AppearTableModel, DefDisposalTableModel);

        this.removeAll();
        this.add(chargesPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
    }
}