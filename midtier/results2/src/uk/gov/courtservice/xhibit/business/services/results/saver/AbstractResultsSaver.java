package uk.gov.courtservice.xhibit.business.services.results.saver;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_formb_result.XhbFormbResultBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_formb_result.XhbFormbResultBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.services.results.ResultsSaver;
import uk.gov.courtservice.xhibit.common.results.vos.PleaSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;

/**
 * <p>
 * Title: AbstractResultsSaver
 * </p>
 * <p>
 * Description: Implement common ResultsSaver technology.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Revision: 1.15 $
 */
public abstract class AbstractResultsSaver implements ResultsSaver {
    public void preprocess(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
    }

    public abstract void save(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException;

    public void saveCrestKeys(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
    }

    public void log(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
    }

    protected static void setJoinderAttributes(ResultSaveValue result, Integer defendantOnOffenceId) {
        Collection cases = XhbCaseBeanHelper2.findByJoinderDefendantOnOffenceId(defendantOnOffenceId);

        if (cases != null) {
            Iterator caseIt = cases.iterator();
            if (caseIt.hasNext()) {
                XhbCase caze = (XhbCase) caseIt.next();
                result.setEntityCaseId(caze.getCaseId());
                result.setEntityCaseNumber(caze.getCaseNumber());
                result.setEntityCaseType(caze.getCaseType());
            }
        }
    }

    protected void updateVco(Integer defendantOnOffenceId, Date vcoDate, String vcoFlag) {
        if (defendantOnOffenceId != null) {
            XhbDefendantOnOffence defendantOnOffence = XhbDefendantOnOffenceBeanHelper2
                    .findByPrimaryKey(defendantOnOffenceId);
            defendantOnOffence.setVcoDate(vcoDate);
            defendantOnOffence.setVcoFlag(vcoFlag);
        }
    }

    protected static boolean equals(Object obj1, Object obj2) {
        return obj1 == null ? obj2 == null : obj2 != null && obj1.equals(obj2);
    }

    /**
     * This class calculates the VCO flag and date. This is complex because it
     * needs to take into account all the other operations that have proceded
     * it. The logic is as follows
     * <ul>
     * <li>The plea or verdict object has the base line of original codes and
     * dates set upon creation.</li>
     * <li>For each proceding operation we determine how it will effect these
     * codes and dates.</li>
     * <li>The current operation then determines how we need to proceed.
     * <li>
     * <ul>
     * 
     * @todo This can be optimised, implemented thus for clarity
     */
    protected static class VCOCalculator {
        private static final Logger log = CSServices.getLogger(VCOCalculator.class);

        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        public static void calculate(ResultsSaveValue resultsSaveValue, int index, PleaSaveValue pleaSaveValue)
                throws ResultsControllerException {
            VCOCalculator calculator = new VCOCalculator(pleaSaveValue);
            calculator.process(resultsSaveValue, index);
            calculator.set(pleaSaveValue);
        }

        public static void calculate(ResultsSaveValue resultsSaveValue, int index, VerdictSaveValue verdictSaveValue)
                throws ResultsControllerException {
            VCOCalculator calculator = new VCOCalculator(verdictSaveValue);
            calculator.process(resultsSaveValue, index);
            calculator.set(verdictSaveValue);
        }

        private Date pleaDate;

        private Integer refPleaId;

        private Date verdictDate;

        private Integer verdictRefId;

        private int defendantOnOffenceId;

        private VCOCalculator(PleaSaveValue pleaSaveValue) {
            if (pleaSaveValue == null || !pleaSaveValue.isOnOffence()) {
                throw new IllegalArgumentException("pleaSaveValue: " + pleaSaveValue);
            }

            pleaDate = pleaSaveValue.getOriginalPleaDate();
            refPleaId = pleaSaveValue.getOriginalRefPleaId();
            verdictDate = pleaSaveValue.getOriginalVerdictDate();
            verdictRefId = pleaSaveValue.getOriginalRefVerdictId();
            defendantOnOffenceId = pleaSaveValue.getDefendantOnOffenceId().intValue();
        }

        private VCOCalculator(VerdictSaveValue verdictSaveValue) {
            if (verdictSaveValue == null || !verdictSaveValue.isOnOffence()) {
                throw new IllegalArgumentException("verdictSaveValue: " + verdictSaveValue);
            }

            pleaDate = verdictSaveValue.getOriginalPleaDate();
            refPleaId = verdictSaveValue.getOriginalRefPleaId();
            verdictDate = verdictSaveValue.getOriginalVerdictDate();
            verdictRefId = verdictSaveValue.getOriginalRefVerdictId();
            defendantOnOffenceId = verdictSaveValue.getDefendantOnOffenceId().intValue();
        }

        private void process(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
            log.debug("Process Start: " + this);
            for (int i = 0; i < index; i++) {
                ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(i);
                if (resultSaveValue instanceof PleaSaveValue) {
                    process((PleaSaveValue) resultSaveValue);
                } else if (resultSaveValue instanceof VerdictSaveValue) {
                    process((VerdictSaveValue) resultSaveValue);
                }
            }
            log.debug("Process End: " + this);
        }

        private void process(PleaSaveValue pleaSaveValue) throws ResultsControllerException {
            Integer currentDefendantOnOffenceId = pleaSaveValue.getDefendantOnOffenceId();
            if (currentDefendantOnOffenceId != null && currentDefendantOnOffenceId.intValue() == defendantOnOffenceId) {
                log.debug("Processing: " + valueOf(pleaSaveValue));
                String operation = pleaSaveValue.getOperation();
                if (operation.equals(ResultSaveValue.ADD) || operation.equals(ResultSaveValue.UPDATE)) {
                    pleaDate = pleaSaveValue.getPleaDate();
                    refPleaId = pleaSaveValue.getRefPleaId();
                } else if (operation.equals(ResultSaveValue.DELETE)) {
                    pleaDate = null;
                    refPleaId = null;
                } else {
                    throw new ResultsControllerException("AbstractResultsSaver.UnrecognisedOperation",
                            new Object[] { operation }, "Unrecognised operation " + operation + ".");
                }
                log.debug("Updated: " + this);
            } else {
                log.debug("Ignoring: " + valueOf(pleaSaveValue));
            }
        }

        private void process(VerdictSaveValue verdictSaveValue) throws ResultsControllerException {
            Integer currentDefendantOnOffenceId = verdictSaveValue.getDefendantOnOffenceId();
            if (currentDefendantOnOffenceId != null && currentDefendantOnOffenceId.intValue() == defendantOnOffenceId) {
                log.debug("Processing: " + valueOf(verdictSaveValue));
                String operation = verdictSaveValue.getOperation();
                if (operation.equals(ResultSaveValue.ADD) || operation.equals(ResultSaveValue.UPDATE)) {
                    verdictDate = verdictSaveValue.getVerdictDate();
                    verdictRefId = verdictSaveValue.getRefVerdictId();
                } else if (operation.equals(ResultSaveValue.DELETE)) {
                    verdictDate = null;
                    verdictRefId = null;
                } else {
                    throw new ResultsControllerException("AbstractResultsSaver.UnrecognisedOperation",
                            new Object[] { operation }, "Unrecognised operation " + operation + ".");
                }
                log.debug("Updated: " + this);
            } else {
                log.debug("Ignoring: " + valueOf(verdictSaveValue));
            }
        }

        private void set(PleaSaveValue pleaSaveValue) {
            Date vcoDate = null;
            String vcoFlag = null;

            if (verdictRefId != null && verdictDate != null) {
                vcoDate = verdictDate;
                vcoFlag = getVerdictVcoFlag(verdictRefId);
            } else {
                String operation = pleaSaveValue.getOperation();
                if (operation.equals(ResultSaveValue.ADD) || operation.equals(ResultSaveValue.UPDATE)) {
                    vcoDate = pleaSaveValue.getPleaDate();
                    vcoFlag = getPleaVcoFlag(pleaSaveValue.getRefPleaId());
                }
            }

            log.debug("Set: " + valueOf(pleaSaveValue) + " vcoDate: " + vcoDate + " vcoFlag: " + vcoFlag);

            if (vcoDate != null && vcoFlag != null) {
                pleaSaveValue.setVcoDate(vcoDate);
                pleaSaveValue.setVcoFlag(vcoFlag);
            } else {
                pleaSaveValue.setVcoDate(null);
                pleaSaveValue.setVcoFlag(null);
            }
        }

        private void set(VerdictSaveValue verdictSaveValue) {
            Date vcoDate = null;
            String vcoFlag = null;

            String operation = verdictSaveValue.getOperation();
            if (operation.equals(ResultSaveValue.ADD) || operation.equals(ResultSaveValue.UPDATE)) {
                vcoDate = verdictSaveValue.getVerdictDate();
                vcoFlag = getVerdictVcoFlag(verdictSaveValue.getRefVerdictId());
            } else // if (operation.equals(ResultSaveValue.DELETE))
            {
                if (refPleaId != null && pleaDate != null) {
                    vcoDate = pleaDate;
                    vcoFlag = getPleaVcoFlag(refPleaId);
                }
            }

            log.debug("Set: " + valueOf(verdictSaveValue) + " vcoDate: " + vcoDate + " vcoFlag: " + vcoFlag);

            if (vcoDate != null && vcoFlag != null) {
                verdictSaveValue.setVcoDate(vcoDate);
                verdictSaveValue.setVcoFlag(vcoFlag);
            } else {
                verdictSaveValue.setVcoDate(null);
                verdictSaveValue.setVcoFlag(null);
            }
        }

        private static String getPleaVcoFlag(Integer refPleaId) {
            return getVcoFlag(XhbFormbResultBeanHelper2.findCurrentByRefPleaIdValue(refPleaId));
        }

        private static String getVerdictVcoFlag(Integer verdictRefId) {
            return getVcoFlag(XhbFormbResultBeanHelper2.findCurrentByRefVerdictIdValue(verdictRefId));
        }

        // extracted common code...
        private static String getVcoFlag(XhbFormbResultBasicValue[] formbResults) {

            if ((formbResults.length > 0) && (formbResults[0] != null)) {
                String description = formbResults[0].getResultDescription();
                if ((description != null) && (description.length() > 0)) {
                    return String.valueOf(description.charAt(0));
                }
            }
            return null;
        }

        public String toString() {
            return "VCOCalculator[defendantOnOffenceId=" + defendantOnOffenceId + ", pleaDate=" + valueOf(pleaDate)
                    + ", refPleaId=" + refPleaId + ", verdictDate=" + valueOf(verdictDate) + ", verdictRefId="
                    + verdictRefId + "]";
        }

        private static String valueOf(PleaSaveValue plea) {
            return plea == null ? "null" : ("PleaSaveValue[operation=" + plea.getOperation()
                    + ", defendantOnOffenceId=" + plea.getDefendantOnOffenceId() + ", pleaDate="
                    + valueOf(plea.getPleaDate()) + ", refPleaId=" + plea.getRefPleaId() + "]");
        }

        private static String valueOf(VerdictSaveValue verdict) {
            return verdict == null ? "null" : ("VerdictSaveValue[operation=" + verdict.getOperation()
                    + ", defendantOnOffenceId=" + verdict.getDefendantOnOffenceId() + ", verdictDate="
                    + valueOf(verdict.getVerdictDate()) + ", verdictRefId=" + verdict.getRefVerdictId() + "]");
        }

        /*
         * private static String valueOf(XhbFormbResultBasicValue formbResults) {
         * return formbResults == null ? "null" :
         * ("XhbFormbResultBasicValue[refVerdictId=" +
         * formbResults.getRefVerdictId() + ", refPleaId=" +
         * formbResults.getRefPleaId() + ", result=" +
         * formbResults.getResultDescription() + "]"); }
         */

        private static String valueOf(Date date) {
            return date == null ? "null" : dateFormat.format(date);
        }
    }
}
