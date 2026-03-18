package uk.gov.courtservice.xhibit.client.results.appealresults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_case_app_reason.XhbCaseAppReasonBasicValue;
import uk.gov.courtservice.xhibit.business.services.caze.CaseTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAppResultCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.CaseAppReasonValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

/**
 * <p>
 * Title: Paul Morris
 * </p>
 * <p>
 * Description: Helper methods for appeal results
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Paul Morris
 */
public class AppealResultsHelper {
    private static final Logger log = CSServices.getLogger(AppealResultsHelper.class);

    public static final String NEW_LINE = "\n";

    private static final String APPEAL_RESULT_CRIMINAL = "APP_RESULT";

    private static final String LESSER_OFENCE_APPEAL_CODE = "ACALO";

    // initialised in static block...
    public static final RefSystemCodeBasicValue NO_RESULT_TYPE_SELECTED = createNoResultType();

    private static final RefAppResultBasicValue NO_RESULT_TYPE_SELECTED_CRIM = createNoResultTypeCrimAppeal();

    private static final Collection criminalOffenceRefData = createCrimOffenceRefData();

    /**
     * Non instantiable constructor
     */
    private AppealResultsHelper() {
    }

    public static Collection getRefSystemCodes(String codeType) throws CSRecoverableException {
        RefSystemCodeCriteria rfsc = new RefSystemCodeCriteria();
        rfsc.setCodeType(codeType);
        rfsc.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
        Collection refData = ResultsHelper.getBisRefDelegate().findSystemCodes(rfsc);
        Sorter.sort((List) refData, new String[] { "decode" });

        Collection returnedList = new ArrayList(refData.size() + 1);
        returnedList.add(NO_RESULT_TYPE_SELECTED);
        returnedList.addAll(refData);

        return returnedList;
    }

    private static RefSystemCodeBasicValue createNoResultType() {
        RefSystemCodeBasicValue refSystemCodeBasicValue = new RefSystemCodeBasicValue(new Integer(0), new Integer(0),
                null, "", "", "", "", "", null);
        return refSystemCodeBasicValue;
    }

    private static RefAppResultBasicValue createNoResultTypeCrimAppeal() {
        RefAppResultBasicValue refAppResultBasicValue = new RefAppResultBasicValue();
        refAppResultBasicValue.setCode(null);
        refAppResultBasicValue.setDescription1(""); // ????
        refAppResultBasicValue.setDescription2("");
        refAppResultBasicValue.setLesserOffInd("");
        refAppResultBasicValue.setObsInd("");
        refAppResultBasicValue.setVarySentence("");

        return refAppResultBasicValue;
    }

    private static Collection createCrimOffenceRefData() {
        Collection returnedList = null;

        try {
            RefAppResultCriteria rfsc = new RefAppResultCriteria();
            rfsc.setAppResultCode("%");
            rfsc.setCourtId(XhibitSingleton.getInstance().getCourtId().toString()); // DR
                                                                                    // 55928
                                                                                    // restrict
                                                                                    // to
                                                                                    // court

            Collection refData = ResultsHelper.getBisRefDelegate().findAppResults(rfsc);
            Sorter.sort((List) refData, new String[] { "description1" });

            returnedList = new ArrayList(refData.size() + 1);
            returnedList.add(NO_RESULT_TYPE_SELECTED_CRIM);
            returnedList.addAll(refData);
        } catch (CSRecoverableException csre) {
            XHIBITErrorHandler.handleError(csre);
        }

        return returnedList;
    }

    public static Collection getCrimOffenceRefData() {
        return criminalOffenceRefData;
    }

    public static Collection getAppealGeneralMagsRefData() {
        Collection appealGeneralMagsRefData = new ArrayList();
        for (Iterator i = criminalOffenceRefData.iterator(); i.hasNext();) {
            RefAppResultBasicValue rarbv = (RefAppResultBasicValue) i.next();
            if (rarbv.getCode() == null || !rarbv.getCode().equalsIgnoreCase("ACALO")) {
                appealGeneralMagsRefData.add(rarbv);
            }
        }
        return appealGeneralMagsRefData;
    }

    /**
     * gets the ref system code basic value for a given row
     * 
     * @param rrv
     *            the row
     * @param referenceData
     *            Collection of ref data
     * @return the ref system code basic value
     */
    public static RefSystemCodeBasicValue getRefSystemCodeBasicValue(ResultsRowValue rrv, Collection referenceData) {
        RefSystemCodeBasicValue result = null;
        VerdictValue vdv = rrv.getVerdictValue();

        if (vdv != null) {
            Integer vdvId = vdv.getRefVerdictId();

            if (vdvId != null) {
                boolean found = false;

                for (Iterator i = referenceData.iterator(); i.hasNext() && !found;) {
                    RefSystemCodeBasicValue next = (RefSystemCodeBasicValue) i.next();

                    if (vdvId.equals(next.getId())) {
                        result = next;
                        found = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * gets the (Criminal) appeal ref system code basic value for a given code
     * 
     * @param code
     *            the ref appeal result code
     * @param referenceAppealOffenceData
     *            The reference data set to examine
     * @return the ref system code basic value
     */
    public static RefAppResultBasicValue getRefAppResultBasicValueForCode(String code,
            Collection referenceAppealOffenceData) {
        RefAppResultBasicValue result = null;

        if (code != null) {
            boolean found = false;

            for (Iterator i = referenceAppealOffenceData.iterator(); i.hasNext() && !found;) {
                RefAppResultBasicValue next = (RefAppResultBasicValue) i.next();
                if (code.equals(next.getCode())) {
                    result = next;
                    found = true;
                }
            }
        }
        return result;
    }
    
 

    /**
     * gets the (Criminal) appeal ref system code basic value for a given row
     * 
     * @param rrv
     *            the row
     * @param referenceAppealOffenceData
     *            The reference data set to examin
     * @return the ref system code basic value
     */
    public static RefAppResultBasicValue getRefAppResultBasicValue(final ResultsRowValue rrv,
            final Collection referenceAppealOffenceData) {
        RefAppResultBasicValue result = null;
        VerdictValue vdv = rrv.getVerdictValue();

        if (vdv != null) {
            Integer vdvId = vdv.getRefAppResultId();
            if (vdvId != null) {
                boolean found = false;

                for (Iterator i = referenceAppealOffenceData.iterator(); i.hasNext() && !found;) {
                    RefAppResultBasicValue next = (RefAppResultBasicValue) i.next();
                    if (vdvId.equals(next.getRefAppResId())) {
                        result = next;
                        found = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Method shared between Case Progress and Criminal Appeal Panel
     * 
     * @return Collection of Case level appeal results
     * @throws CSRecoverableException
     */
    public static Collection getCaseCrimRefData() throws CSRecoverableException {
        return getRefSystemCodes(APPEAL_RESULT_CRIMINAL);
    }

    /**
     * Creates Appeal Results Result description from RefAppResultBasicValue.
     * 
     * @param rarbv
     *            a RefAppResultBasicValue.
     * @return the appeal result description.
     */
    public static String getAppealResultDescription(RefAppResultBasicValue rarbv) {
        if (rarbv.getDescription2() == null || rarbv.getDescription2().equals("")) {
            return rarbv.getDescription1();
        } else {
            return rarbv.getDescription1() + " " + rarbv.getDescription2();
        }
    }

    /**
     * Returns true if for the given appeal result code there should be a lesser
     * offence.
     * 
     * @param appealResultCode
     *            the code to check
     * @return true if the given appeal result code should have a lesser
     *         offence.
     */
    public static boolean hasAlternateOffence(String appealResultCode) {
        return (LESSER_OFENCE_APPEAL_CODE.equalsIgnoreCase(appealResultCode));
    }

    /**
     * 
     * @param tableModel
     *            XHIBITTableModelInterface
     * @param resultsSaveValue
     *            ResultsSaveValue
     * @param arm
     *            AppealResultsModel
     * @param magistrateGeneralDisposal
     *            set to true to process appeal results for Magistrates general
     *            disposals .
     * @throws CSRecoverableException
     */
    protected static void setAlteredFlags(XHIBITTableModelInterface tableModel, ResultsSaveValue resultsSaveValue,
            AppealResultsModel arm, boolean magistrateGeneralDisposal) throws CSRecoverableException {
        ResultsRowValue rrv;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            rrv = (ResultsRowValue) tableModel.getDataAt(i);
            setAlteredFlag(rrv, resultsSaveValue, arm, magistrateGeneralDisposal);
        }
    }

    protected static void setAlteredFlag(ResultsRowValue rrv, ResultsSaveValue resultsSaveValue,
            AppealResultsModel arm, boolean magistrateGeneralDisposal) throws CSRecoverableException {
        setAlteredFlag(rrv, resultsSaveValue, null, null, arm, magistrateGeneralDisposal);
    }

    protected static void setAlteredFlag(ResultsRowValue rrv, ResultsSaveValue resultsSaveValue,
            String[] judgesComments, List oldJudgesComments, AppealResultsModel arm, boolean magistrateGeneralDisposal)
            throws CSRecoverableException {
        VerdictSaveValue verdictSaveValue = null;

        log.debug("setAlteredFlag rrv.getAction() = " + rrv.getAction());

        switch (rrv.getAction()) {
        case ResultsRowValue.RESULT_ADD:
            verdictSaveValue = rrv.getVerdictSaveValue(ResultSaveValue.ADD);
            if (magistrateGeneralDisposal) {
                verdictSaveValue.setDisposalSaveValue(rrv.getDisposalSaveValue(ResultSaveValue.ADD));
            }
            resultsSaveValue.addResultSaveValue(verdictSaveValue);

            // Check if result is case level before working with comments.
            if (verdictSaveValue.isOnCase()) {
                if (judgesComments != null) {
                    resultsSaveValue.addCaseAppReasonValues(judgesComments, rrv.getCaseId(), rrv.getCaseType(), rrv
                            .getCaseNumber());
                }
            }
            break;
        case ResultsRowValue.RESULT_UPDATE:
            if (rrv.getVariationDisposalCount() > 0 && !rrv.getVerdictValue().isAppealResultVariable()) {
                // The updated Appeal Result does not allow for variation
                // disposals - so delete them.
                log.debug("setAlteredFlag - UPDATE about to processDisposalDelete");
                if (rrv.getVerdictValue().isOnOffence()) {
                    deleteVariationsForOffenceAppealResult(arm, resultsSaveValue, rrv);
                }
                if (magistrateGeneralDisposal) {
                    deleteVariationsForMagistrateGeneralDisposal(arm, resultsSaveValue, rrv);
                }
            }
            verdictSaveValue = rrv.getVerdictSaveValue(ResultSaveValue.UPDATE);
            if (magistrateGeneralDisposal) {
                verdictSaveValue.setDisposalSaveValue(rrv.getDisposalSaveValue(ResultSaveValue.UPDATE));
            }
            resultsSaveValue.addResultSaveValue(verdictSaveValue);
            if (verdictSaveValue.isOnCase()) {
                if (judgesComments != null) {
                    resultsSaveValue.updateCaseAppReasonValues(judgesComments, rrv.getCaseId(), rrv.getCaseType(), rrv
                            .getCaseNumber(), oldJudgesComments);
                }
            }
            break;
        case ResultsRowValue.RESULT_DELETE:
            verdictSaveValue = rrv.getVerdictSaveValue(ResultSaveValue.DELETE);
            if (verdictSaveValue.isOnOffence()) {
                deleteVariationsForOffenceAppealResult(arm, resultsSaveValue, rrv);
            }
            if (magistrateGeneralDisposal) {
                deleteVariationsForMagistrateGeneralDisposal(arm, resultsSaveValue, rrv);

                // We are deleting the Appeal Result for a Magistrate
                // General Disposal which means updating the Disposal on Crest.
                verdictSaveValue.setDisposalSaveValue(rrv.getDisposalSaveValue(ResultSaveValue.UPDATE));
            }
            resultsSaveValue.addResultSaveValue(verdictSaveValue);
            if (verdictSaveValue.isOnCase()) {
                if (oldJudgesComments != null) {
                    resultsSaveValue.removeCaseAppReasonValues(oldJudgesComments, rrv.getCaseId(), rrv.getCaseType(),
                            rrv.getCaseNumber());
                }
            }
            break;
        case ResultsRowValue.RESULT_UNCHANGED:
        default:
            if (rrv != null && !rrv.getCaseSubType().equals(CaseTypes.MISC_APPEAL.getCaseType())
                    && rrv.getVerdictValue() != null) {
                /**
                 * @todo This should be moved to separate method and also called
                 *       by criminal appeals
                 */
                if (oldJudgesComments != null && oldJudgesComments.size() != 0) {
                    if (judgesComments == null || judgesComments.length == 0) {
                        resultsSaveValue.removeCaseAppReasonValues(oldJudgesComments, rrv.getCaseId(), rrv
                                .getCaseType(), rrv.getCaseNumber());
                    } else {
                        resultsSaveValue.updateCaseAppReasonValues(judgesComments, rrv.getCaseId(), rrv.getCaseType(),
                                rrv.getCaseNumber(), oldJudgesComments);
                    }
                } else {
                    if (judgesComments == null || judgesComments.length == 0) {
                        // do nothing as old and new are null
                    } else {
                        resultsSaveValue.addCaseAppReasonValues(judgesComments, rrv.getCaseId(), rrv.getCaseType(), rrv
                                .getCaseNumber());
                    }
                }
            }
            break;
        }
    }

    /**
     * setDeleteFlags is called when deleting a Criminal Appeal Case level
     * appeal result to delete all of the offence level and Magistrates General
     * Disposal appeal results and their respective variation disposals.
     * 
     * @param arm
     *            AppealResultsModel
     * @param tableModel
     *            XHIBITTableModelInterface
     * @param resultsSaveValue
     *            ResultsSaveValue
     * @param isMagistrateGeneralDisposals
     *            set to true if processing Magsistrates general disposals
     * @throws CSRecoverableException
     */
    protected static void setDeleteFlags(AppealResultsModel arm, XHIBITTableModelInterface tableModel,
            ResultsSaveValue resultsSaveValue, boolean isMagistrateGeneralDisposals) throws CSRecoverableException {
        log.debug("setDeleteFlags - BEGIN");
        ResultsRowValue rrv;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            rrv = (ResultsRowValue) tableModel.getDataAt(i);
            if ((rrv.getVerdictValue() != null || rrv.getAction() == ResultsRowValue.RESULT_DELETE)
                    && rrv.getAction() != ResultsRowValue.RESULT_ADD) {
                // Delete disposal(s) first
                if (isMagistrateGeneralDisposals) {
                    deleteVariationsForMagistrateGeneralDisposal(arm, resultsSaveValue, rrv);
                } else {
                    deleteVariationsForOffenceAppealResult(arm, resultsSaveValue, rrv);
                }

                // then delete verdict
                VerdictSaveValue verdictSaveValue = rrv.getVerdictSaveValue(ResultSaveValue.DELETE);
                if (isMagistrateGeneralDisposals) {
                    verdictSaveValue.setDisposalSaveValue(rrv.getDisposalSaveValue(ResultSaveValue.DELETE));
                }
                resultsSaveValue.addResultSaveValue(verdictSaveValue);
            }
            rrv.setAction(ResultsRowValue.RESULT_UNCHANGED);
        }
    }

    /**
     * Finds and deletes all offence level variation disposals related to the
     * offence level appeal result.
     * 
     * @param arm
     *            AppealResultsModel
     * @param resultsSaveValue
     *            ResultsSaveValue
     * @param rrv
     *            ResultsRowValue
     * @throws CSRecoverableException
     */
    private static void deleteVariationsForOffenceAppealResult(AppealResultsModel arm,
            ResultsSaveValue resultsSaveValue, ResultsRowValue rrv) throws CSRecoverableException {
        log.debug("deleteVariationsForOffenceAppealResult");

        // get list of disposal results using chargeType of deleteRRV
        List disposalList = arm.getResultsHelper().getResultsForCharge(rrv.getChargeType() + "D");

        // search for rrvs matching the defendantOnOffenceId of the
        // passed in RRV and add to resultSaveValue as a delete.
        if (disposalList != null && disposalList.size() > 0) {
            ResultsRowValue listRRV = null;
            ResultSaveValue resultSaveValue = null;
            for (int i = 0; i < disposalList.size(); i++) {
                listRRV = (ResultsRowValue) disposalList.get(i);

                if (rrv.getDefendantOnOffenceId().equals(listRRV.getDefendantOnOffenceId())
                        && listRRV.getDisposalValue() != null && listRRV.getDisposalValue().isVariationDisposal()) {
                    log.debug("deleteVariationsForOffenceAppealResult adding resultSaveValue");
                    resultSaveValue = listRRV.getDisposalSaveValue(ResultSaveValue.DELETE);
                    resultSaveValue.setCourtLogged(false);
                    resultsSaveValue.addResultSaveValue(resultSaveValue);
                }
            }
        }
    }

    /**
     * Finds and deletes all variation disposals related to the appeal result
     * for a Magistrate general disposal.
     * 
     * @param arm
     *            AppealResultsModel
     * @param resultsSaveValue
     *            ResultsSaveValue
     * @param rrv
     *            ResultsRowValue
     * @throws CSRecoverableException
     */
    private static void deleteVariationsForMagistrateGeneralDisposal(AppealResultsModel arm,
            ResultsSaveValue resultsSaveValue, ResultsRowValue rrv) throws CSRecoverableException {
        log.debug("deleteVariationsForMagistrateGeneralDisposal");

        Integer disposalId;
        if (rrv.getVerdictValue() == null) {
            disposalId = rrv.getDeleteVerdictValue().getDisposal2Id();
        } else {
            disposalId = rrv.getVerdictValue().getDisposal2Id();
        }

        List disposalList = arm.getResultsHelper().getResultsForCharge(ResultsHelper.CHARGETYPE_UNRELATED);

        // search for rrvs with disposals with ids matching the disposalId of
        // the passed in RRV and add to the resultSaveValue as a delete.
        if (disposalList != null && disposalList.size() > 0) {
            ResultsRowValue listRRV = null;
            ResultSaveValue resultSaveValue = null;
            for (int i = 0; i < disposalList.size(); i++) {
                listRRV = (ResultsRowValue) disposalList.get(i);

                if (listRRV.getDisposalValue() != null && listRRV.getDisposalValue().isVariationDisposal()
                        && listRRV.getDisposalValue().getPsdDisposal2Id() != null
                        && listRRV.getDisposalValue().getPsdDisposal2Id().equals(disposalId)) {
                    log.debug("deleteVariationsForMagistrateGeneralDisposal adding resultSaveValue");
                    resultSaveValue = listRRV.getDisposalSaveValue(ResultSaveValue.DELETE);
                    resultSaveValue.setCourtLogged(rrv.getAction() == ResultsRowValue.RESULT_UPDATE);
                    resultsSaveValue.addResultSaveValue(resultSaveValue);
                }
            }
        }
    }

    /**
     * Determines by checking the number of verdicts/disposals, whether to warn
     * the user about the consequence of deleting the plea on the passed in row.
     * 
     * @param dataRow
     *            ResultsRowValue
     * @param acm
     *            ApplicationCaseModel
     * @throws UserCancelException
     */
    public static void processRowDelete(ResultsRowValue dataRow, ApplicationCaseModel acm) throws UserCancelException {
        int rc = JOptionPane.DEFAULT_OPTION;
        int variationDisposalCount = dataRow.getVariationDisposalCount();
        if (variationDisposalCount > 0) {
            String space = " ";
            rc = JOptionPane.showConfirmDialog(acm.getXhibitApplicationController(),
                    getResource("criminal.DeleteResult.IntroMessage") + // Are
                            // there
                            // disposals?
                            getResource("criminal.DeleteResult.DisposalText", new Object[] { space
                                    + variationDisposalCount }) + // Question
                            getResource("criminal.DeleteResult.Question"), getResource("criminal.DeleteResult.Title"),

                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (rc == JOptionPane.YES_OPTION) {
                // carry on
            } else {
                throw new UserCancelException();
            }
        } else {
            rc = JOptionPane
                    .showConfirmDialog(acm.getXhibitApplicationController(),
                            // "Are you sure you would like to delete?",
                            getResource("criminal.DeleteResult.NoResults"),
                            // "Delete Appeal Result",
                            getResource("criminal.DeleteResult.Title"), JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
            if (rc == JOptionPane.YES_OPTION) {
                // carry on
            } else {
                throw new UserCancelException();
            }
        }
    }

    /**
     * If there are variation disposals for the current row - prompt the user.
     * 
     * @param dataRow
     *            ResultsRowValue
     * @param acm
     *            ApplicationCaseModel
     * @throws UserCancelException
     */
    public static void processDeleteAppealResultVariationDisposals(ResultsRowValue dataRow, ApplicationCaseModel acm)
            throws UserCancelException {
        // if results found
        int variationDisposalCount = dataRow.getVariationDisposalCount();
        log.debug("processDeleteAppealResultVariationDisposals - count = " + variationDisposalCount);
        if (variationDisposalCount > 0) {
            String space = " ";
            int reply = JOptionPane.showConfirmDialog(acm.getXhibitApplicationController(),
                    getResource("criminal.ChangeResult.IntroMessage") + // Are
                            // there
                            // disposals?
                            getResource("criminal.ChangeResult.DisposalText", new Object[] { space
                                    + variationDisposalCount }) + // Question
                            getResource("criminal.ChangeResult.Question"), getResource("criminal.ChangeResult.Title"),

                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (reply == JOptionPane.YES_OPTION) {
                // carry on
            } else {
                throw new UserCancelException();
            }
        }
    }

    public static boolean isAppealResultVariable(String code) {
        return (Arrays.binarySearch(VerdictValue.variableAppealResultCodes, code) >= 0);
    }

    /**
     * Creates a copy of the List caseAppReasonValues by deep cloning each
     * caseAppReasonValue in the list. This is to prevent the original list
     * reflecting any changes as a result of the creation of judges comments on
     * the ResultsSaveValue.
     * 
     * @param caseAppReasonValues
     *            List of case appeal reasons.
     * @return List caseAppReasonValues
     */
    public static List copyCaseAppReasonValues(List caseAppReasonValues) {
        List caseAppReasonsCopy = null;

        if (caseAppReasonValues != null) {
            caseAppReasonsCopy = new ArrayList(caseAppReasonValues.size());
            if (caseAppReasonValues.size() > 0) {
                CaseAppReasonValue caseAppReasonValue;
                XhbCaseAppReasonBasicValue caseAppReasonBasicValue;
                CaseAppReasonValue caseAppReasonValueCopy;
                XhbCaseAppReasonBasicValue caseAppReasonBasicValueCopy;

                for (int i = 0; i < caseAppReasonValues.size(); i++) {
                    caseAppReasonValue = (CaseAppReasonValue) caseAppReasonValues.get(i);
                    caseAppReasonBasicValue = caseAppReasonValue.getXhbCaseAppReasonBasicValue();
                    caseAppReasonValueCopy = new CaseAppReasonValue();
                    caseAppReasonBasicValueCopy = new XhbCaseAppReasonBasicValue();

                    // set XhbCaseAppReasonBasicValue
                    caseAppReasonBasicValueCopy.setAppReason(caseAppReasonBasicValue.getAppReason());
                    caseAppReasonBasicValueCopy.setCarId(caseAppReasonBasicValue.getCarId());
                    caseAppReasonBasicValueCopy.setCaseAppReasonId(caseAppReasonBasicValue.getCaseAppReasonId());
                    caseAppReasonBasicValueCopy.setCaseId(caseAppReasonBasicValue.getCaseId());
                    caseAppReasonBasicValueCopy.setCreatedBy(caseAppReasonBasicValue.getCreatedBy());
                    caseAppReasonBasicValueCopy.setCreationDate(caseAppReasonBasicValue.getCreationDate());
                    caseAppReasonBasicValueCopy.setLastUpdateDate(caseAppReasonBasicValue.getLastUpdateDate());
                    caseAppReasonBasicValueCopy.setVersion(caseAppReasonBasicValue.getVersion());
                    caseAppReasonBasicValueCopy.setObsInd("N");

                    caseAppReasonValueCopy.setCaseAppReasonBasicValue(caseAppReasonBasicValueCopy);

                    caseAppReasonsCopy.add(i, caseAppReasonValueCopy);
                }
            }
        }
        return caseAppReasonsCopy;
    }

    /**
     * Creates a concatenated String from a collection of Strings
     * 
     * @param list
     *            Collection of Strings
     * @return the concatenated String
     */
    public static String getConcatenatedString(final Collection list) {
        final StringBuffer sb = new StringBuffer();
        final Iterator iter = list.iterator();
        while (iter.hasNext()) {
            String item = (String) iter.next();
            sb.append(item);
            sb.append(NEW_LINE);
        }
        return sb.toString();
    }

    private static String getResource(final String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AppealResults, key);
    }

    private static String getResource(final String key, final Object[] params) {
        return ResourceBundleHelper.getResource(XhibitBundles.AppealResults, key, params);
    }
}