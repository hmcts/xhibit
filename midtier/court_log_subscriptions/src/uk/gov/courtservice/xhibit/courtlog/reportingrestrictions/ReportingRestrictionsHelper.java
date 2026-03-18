package uk.gov.courtservice.xhibit.courtlog.reportingrestrictions;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case_reference.XhbCaseReferenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case_reference.XhbCaseReferenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_case_reference.XhbCaseReferenceBeanNotFoundException;
import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;

/**
 * @author pznwc5
 * 
 * Helper class for setting and lifting reporting restrictions
 */
public class ReportingRestrictionsHelper {
    /** Logger */
    protected final static Logger LOG = Logger.getLogger(ReportingRestrictionsHelper.class);

    /**
     * Sets restrictions
     * 
     * @param caseId
     *            Case id
     */
    public static void setRestrictions(Integer caseId) {
        handleReportingRestrictions(caseId, XhbCaseReferenceBasicValue.REPORTING_RESTRICTIONS);
    }

    /**
     * Lifts restrictions
     * 
     * @param caseId
     *            Case id
     */
    public static void liftRestrictions(Integer caseId) {
        handleReportingRestrictions(caseId, XhbCaseReferenceBasicValue.LIFTING_RESTRICTIONS);
    }

    /**
     * Handles reporting restrictions
     * 
     * @param caseId
     *            Case Id
     * @param restriction
     *            Whether setting or lifting restrictions
     */
    private static void handleReportingRestrictions(Integer caseId, Integer restriction) {
        LOG.debug("Start - setReportingRestrictions, caseId = " + caseId);
        try {
            // Update the case reference if it is already there
            XhbCaseReferenceBasicValue val = XhbCaseReferenceBeanHelper2.findByCaseIdValue(caseId);
            val.setReportingRestrictions(restriction);
            XhbCaseReferenceBeanHelper2.update(val);
        } catch (XhbCaseReferenceBeanNotFoundException ex) {
            createCaseReference(caseId, restriction);
        }
        LOG.debug("End - setReportingRestrictions, caseId = " + caseId);
    }

    /**
     * Create a new case reference record
     * 
     * @param caseId
     *            Case Id
     */
    private static void createCaseReference(Integer caseId, Integer restriction) {
        LOG.debug("Start - createCaseReference, caseId = " + caseId);

        // Create a new case reference if it is not there
        XhbCaseReferenceBasicValue val = new XhbCaseReferenceBasicValue();
        val.setReportingRestrictions(restriction);

        XhbCase xhbCase = EntityHelper.getXhbCase(caseId);
        XhbCaseReferenceBeanHelper2.create(val, xhbCase);

        LOG.debug("End - createCaseReference, caseId = " + caseId);
    }
}
