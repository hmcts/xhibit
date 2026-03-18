package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ResultsMVO;

/**
 * <p>
 * Title: ResultsSaveValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Revision: 1.22 $
 */
public class ResultsSaveValue extends ResultValue {
	
	static final long serialVersionUID = -1702214206215794195L;

    // Stores dumps of the state at various stages
    private final ResultsDebugValue resultsDebugValue = new ResultsDebugValue();

    // The value objects to be saved
    private final List resultSaveValueList = new ArrayList();

    // Needed to determine which CREST DB to update
    private final Integer courtId;

    // Constructor
    public ResultsSaveValue(Integer courtId) {
        if (courtId == null) {
            throw new IllegalArgumentException("courtId: null");
        }
        this.courtId = courtId;
    }

    // Accessors
    public Integer getCourtId() {
        return courtId;
    }

    // Result Save
    public void addResultSaveValues(List resultSaveValues) {
        for (int i = 0, s = resultSaveValues.size(); i < s; i++) {
            addResultSaveValue((ResultSaveValue) resultSaveValues.get(i));
        }
    }

    public void addResultSaveValue(ResultSaveValue resultSaveValue) {
        resultSaveValueList.add(resultSaveValue);
    }

    public void addCaseAppReasonValues(String[] newAppReasons, Integer caseId, String caseType, Integer caseNumber) {
        addCaseAppReasonValues(newAppReasons, 0, newAppReasons.length, caseId, caseType, caseNumber);
    }

    public void removeCaseAppReasonValues(List appReasonValues, Integer caseId, String caseType, Integer caseNumber) {
        removeCaseAppReasonValues(appReasonValues, 0, appReasonValues.size(), caseId, caseType, caseNumber);
    }

    public void updateCaseAppReasonValues(String[] newAppReasons, Integer caseId, String caseType, Integer caseNumber,
            List appReasonValues) {
        int oldLength = appReasonValues.size();
        int newLength = newAppReasons.length;
        if (oldLength == newLength) {
            updateCaseAppReasonValues(newAppReasons, appReasonValues, 0, oldLength, caseId, caseType, caseNumber);
        } else if (oldLength < newLength) {
            updateCaseAppReasonValues(newAppReasons, appReasonValues, 0, oldLength, caseId, caseType, caseNumber);
            addCaseAppReasonValues(newAppReasons, oldLength, newLength, caseId, caseType, caseNumber);
        } else {
            updateCaseAppReasonValues(newAppReasons, appReasonValues, 0, newLength, caseId, caseType, caseNumber);
            removeCaseAppReasonValues(appReasonValues, newLength, oldLength, caseId, caseType, caseNumber);
        }
    }

    private void addCaseAppReasonValues(String[] newAppReasons, int startIndex, int endIndex, Integer caseId,
            String caseType, Integer caseNumber) {
        for (int i = startIndex; i < endIndex; i++) {
            resultSaveValueList.add(new CaseAppReasonSaveValue(newAppReasons[i], ResultSaveValue.ADD, caseId,
                    caseNumber, caseType));
        }
    }

    private void removeCaseAppReasonValues(List appReasonValues, int startIndex, int endIndex, Integer caseId,
            String caseType, Integer caseNumber) {
        for (int i = startIndex; i < endIndex; i++) {
            resultSaveValueList.add(new CaseAppReasonSaveValue((CaseAppReasonValue) appReasonValues.get(i),
                    ResultSaveValue.DELETE, caseId, caseNumber, caseType));
        }
    }

    private void updateCaseAppReasonValues(String[] newAppReasons, List appReasonValues, int startIndex, int endIndex,
            Integer caseId, String caseType, Integer caseNumber) {
        for (int i = startIndex; i < endIndex; i++) {
            CaseAppReasonValue appReasonVal = (CaseAppReasonValue) appReasonValues.get(i);
            if (!newAppReasons[i].equals(appReasonVal.getAppReason())) {
                appReasonVal.setAppReason(newAppReasons[i]);
                resultSaveValueList.add(new CaseAppReasonSaveValue(appReasonVal, ResultSaveValue.UPDATE, caseId,
                        caseNumber, caseType));
            }
        }
    }

    public int getResultSaveValueCount() {
        return resultSaveValueList.size();
    }

    public ResultSaveValue getResultSaveValue(int index) {
        return (ResultSaveValue) resultSaveValueList.get(index);
    }

    // Business
    public Integer[] getCaseIds() {
        Set caseIdSet = new HashSet();
        for (int i = 0, c = getResultSaveValueCount(); i < c; i++) {
            caseIdSet.add(getResultSaveValue(i).getCourtLogCaseId());
        }
        return (Integer[]) caseIdSet.toArray(new Integer[caseIdSet.size()]);
    }

    public Integer[] getDefendantOnCaseIds() {
        Set defendantOnCaseIdIdSet = new HashSet();
        for (int i = 0, c = getResultSaveValueCount(); i < c; i++) {
            defendantOnCaseIdIdSet.add(getResultSaveValue(i).getDefendantOnCaseId());
        }
        return (Integer[]) defendantOnCaseIdIdSet.toArray(new Integer[defendantOnCaseIdIdSet.size()]);
    }

    // Debug
    public void appendDebug(StringBuffer buffer, int indent) {
        // Add Header
        buffer.append(ResultsSaveValue.class.getName());
        buffer.append("{courtId=");
        buffer.append(courtId);
        buffer.append(",");

        // Add Values
        indent += 1;
        appendLine(buffer, indent);
        buffer.append("resultSaveValueList={");
        indent += 1;
        int c = getResultSaveValueCount();
        if (0 < c) {
            appendLine(buffer, indent);
            getResultSaveValue(0).appendDebug(buffer, indent);
            for (int i = 1; i < c; i++) {
                buffer.append(",");
                appendLine(buffer, indent);
                getResultSaveValue(i).appendDebug(buffer, indent);
            }
        }
        indent -= 1;
        appendLine(buffer, indent);
        buffer.append("}");
        indent -= 1;

        // Add Footer
        appendLine(buffer, indent);
        buffer.append("}");
    }

    /**
     * Record the dump
     */
    public void recordVoEditedDump() {
        resultsDebugValue.recordVoEditedDump(this);
    }

    /**
     * Get the dump
     */
    public String getVoEditedDump() {
        return resultsDebugValue.getVoEditedDump();
    }

    /**
     * Record the dump
     */
    public void recordVoPreprocessedDump() {
        resultsDebugValue.recordVoPreprocessedDump(this);
    }

    /**
     * Get the dump
     */
    public String getVoPreprocessedDump() {
        return resultsDebugValue.getVoPreprocessedDump();
    }

    /**
     * Record the dump
     */
    public void recordVoSavedDump() {
        resultsDebugValue.recordVoSavedDump(this);
    }

    /**
     * Get the dump
     */
    public String getVoSavedDump() {
        return resultsDebugValue.getVoSavedDump();
    }

    /**
     * Record the dump
     * 
     * @deprecated Replaced by inputLocalLogging of
     *             uk.gov.courtservice.xhibit.integration.mercator.HTTPMercatorWrapperImpl
     */
    public void recordMvoBeforeExportDump(ResultsMVO value) {
        resultsDebugValue.recordMvoBeforeExportDump(value);
    }

    /**
     * Get the dump
     * 
     * @deprecated Replaced by inputLocalLogging of
     *             uk.gov.courtservice.xhibit.integration.mercator.HTTPMercatorWrapperImpl
     *             Returns String: "Not Recorded!"
     */
    public String getMvoBeforeExportDump() {
        return resultsDebugValue.getMvoBeforeExportDump();
    }

    /**
     * Record the dump
     * 
     * @deprecated Replaced by outputLocalLogging of
     *             uk.gov.courtservice.xhibit.integration.mercator.HTTPMercatorWrapperImpl
     */
    public void recordMvoAfterExportDump(ResultsMVO value) {
        resultsDebugValue.recordMvoAfterExportDump(value);
    }

    /**
     * Get the dump
     * 
     * @deprecated Replaced by outputLocalLogging of
     *             uk.gov.courtservice.xhibit.integration.mercator.HTTPMercatorWrapperImpl
     *             Returns String: "Not Recorded!"
     */
    public String getMvoAfterExportDump() {
        return resultsDebugValue.getMvoAfterExportDump();
    }

    /**
     * Record the dump
     */
    public void recordVoExportedDump() {
        resultsDebugValue.recordVoExportedDump(this);
    }

    /**
     * Get the dump
     */
    public String getVoExportedDump() {
        return resultsDebugValue.getVoExportedDump();
    }

    /**
     * Get the debug value
     */
    public ResultsDebugValue getResultsDebugValue() {
        return resultsDebugValue;
    }

    /**
     * Currently this method ensures that deletes occur before inserts.
     * Insertion order: 1. Disposal 2.CaseAppeal 3. Verdict 4. Plea, Deletion
     * order: 1. Plea, 2. Verdict 3. CaseAppeal 4. Disposal It does not try to
     * reorder updates since it is assumed that updates do not affect
     * referential integrity in this scenario.
     */
    public void sortResultSaveValueForMove() {
        Collections.sort(this.resultSaveValueList, JOINDER_COMPARATOR);
    }

    /**
     * Sort the result save values into the correct order for joinders, this was
     * added as a localised fix for a problem in the implementation.
     */
    private static final Comparator JOINDER_COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
            int sequenceNumber1 = getSequenceNumber((ResultSaveValue) o1);
            int sequenceNumber2 = getSequenceNumber((ResultSaveValue) o2);
            return (sequenceNumber1 < sequenceNumber2 ? -1 : (sequenceNumber1 == sequenceNumber2 ? 0 : 1));
        }

        /**
         * Get the sequence order to ensure values are added and deleted in the
         * correct order, the first operation performed is deleteing disposals,
         * the last operation is adding disposals.
         */
        public int getSequenceNumber(ResultSaveValue rsv) {
            if (rsv.isDeleteOperation()) {
                if (rsv instanceof DisposalSaveValue) {
                    return 1;
                } else if (rsv instanceof VerdictSaveValue) {
                    return 2;
                } else if (rsv instanceof PleaSaveValue) {
                    return 3;
                } else // order defined for disposal, verdicts and pleas
                {
                    return 0;
                }
            } else // Add & Update in same order
            {
                if (rsv instanceof DisposalSaveValue) {
                    return 6;
                } else if (rsv instanceof VerdictSaveValue) {
                    return 5;
                } else if (rsv instanceof PleaSaveValue) {
                    return 4;
                } else // order defined for disposal, verdicts and pleas
                {
                    return 7;
                }
            }
        }
    };
}
