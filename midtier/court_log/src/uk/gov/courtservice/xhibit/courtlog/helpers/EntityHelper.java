package uk.gov.courtservice.xhibit.courtlog.helpers;

import java.util.Date;
import java.util.Iterator;

import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_category_desc.XhbCourtLogCategoryDesc;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_category_desc.XhbCourtLogCategoryDescBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDesc;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDescBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecord;
import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecordBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_witness.XhbWitness;
import uk.gov.courtservice.xhibit.business.entities.xhb_witness.XhbWitnessBeanHelper2;

/**
 * Utility class used to lookup the entity beans required by the court log
 * components, and to handle, and rethrow <code>ObjectNotFoundException</code>s
 * as the runtime exceptions, as when looking up via a primary key, the entity
 * should always exist.
 * 
 * @author tz0d5m
 */
public class EntityHelper {
    /**
     * Private constructor to prevent instantiation, as all methods are static.
     */
    private EntityHelper() {
        // Do not allow external instantiation...
    }

    /**
     * Utility method used to acquire the <code>XhbCourtLogEventDesc</code>
     * using a primary key look up using the passed in event description id
     * <code>Integer</code> value.
     * 
     * @param eventDescId
     *            The primary key of the log event desc to look up
     * @return The looked up log event desc
     * @see uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc
     *      .XhbCourtLogEventDescBeanHelper#findByPrimaryKey(java.lang.Integer).
     */
    public static XhbCourtLogEventDesc getXhbCourtLogEventDesc(Integer eventDescId) {
        return XhbCourtLogEventDescBeanHelper2.findByPrimaryKey(eventDescId);
    }

    /**
     * Utility method used to acquire the <code>XhbCourtLogEntry</code> using
     * a primary key look up using the passed in log entry id <code>Long</code>
     * value.
     * 
     * @param logEntryId
     *            The primary key of the log entry to look up
     * @return The looked up log entry
     * @see uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry
     *      .XhbCourtLogEntryBeanHelper#findByPrimaryKey(java.lang.Long).
     */
    public static XhbCourtLogEntry getXhbCourtLogEntry(Long logEntryId) {
        return XhbCourtLogEntryBeanHelper2.findByPrimaryKey(logEntryId);
    }

    /**
     * Utility method used to acquire the <code>XhbCourtLogEntry</code> using
     * a primary key look up using the passed in log entry id <code>Long</code>
     * value.
     * 
     * @param logEntryId
     *            The primary key of the log entry to look up
     * @return The looked up log entry
     * @see uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry
     *      .XhbCourtLogEntryBeanHelper#findByPrimaryKey(java.lang.Long).
     */
    public static XhbCourtLogEntryBasicValue getXhbCourtLogEntryBasicValue(Long logEntryId) {
        return XhbCourtLogEntryBeanHelper2.findByPrimaryKeyValue(logEntryId);
    }

    /**
     * Utility method used to acquire the <code>XhbWitness</code> using a
     * primary key look up using the passed in witness id <code>Integer</code>
     * value.
     * 
     * @param witnessId
     *            The primary key of the witness to look up
     * @return The looked up witness
     * @see uk.gov.courtservice.xhibit.business.entities.xhb_witness
     *      .XhbWitnessBeanHelper#findByPrimaryKey(java.lang.Integer).
     */
    public static XhbWitness getXhbWitness(Integer witnessId) {
        return XhbWitnessBeanHelper2.findByPrimaryKey(witnessId);
    }

    /**
     * Utility method used to acquire the <code>XhbCase</code> using a primary
     * key look up using the passed in case id <code>Integer</code> value.
     * 
     * @param caseId
     *            The primary key of the case to look up
     * @return The looked up case
     * @see uk.gov.courtservice.xhibit.business.entities.xhb_case
     *      .XhbCaseBeanHelper#findByPrimaryKey(java.lang.Integer).
     */
    public static XhbCase getXhbCase(Integer caseId) {
        return XhbCaseBeanHelper2.findByPrimaryKey(caseId);
    }

    /**
     * Utility method used to acquire the <code>XhbDefHearingRecord</code>
     * using a finder method using the passed in hearing id <code>Integer</code>
     * value, and the defendantOnCaseId <code>Integer</code> value.
     * 
     * @param hearingId
     *            The primary key of the hearing
     * @param defendantOnCaseId
     *            The primary key of the defendant on case
     * @return The looked up def hearing record
     * @see uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record
     *      .XhbDefHearingRecordBeanHelper#findByDefOnCaseAndHearing(
     *      java.lang.Integer, java.lang.Integer).
     */
    public static XhbDefHearingRecord getXhbDefHearingRecord(Integer hearingId, Integer defendantOnCaseId) {
        return XhbDefHearingRecordBeanHelper2.findByDefOnCaseAndHearing(defendantOnCaseId, hearingId);
    }

    /**
     * Utility method used to acquire the <code>XhbDefendantOnCase</code>
     * using a finder method using the passed in defendant on case id
     * <code>Integer</code> value.
     * 
     * @param defendantOnCaseId
     *            The primary key of the defendant on case
     * @return The looked up defendant on case
     * @see uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case
     *      .XhbDefendantOnCaseBeanHelper#findByPrimaryKey(java.lang.Integer).
     */
    public static XhbDefendantOnCase getXhbDefendantOnCase(Integer defendantOnCaseId) {
        return XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defendantOnCaseId);
    }

    /**
     * Utility method used to acquire the <code>XhbScheduledHearing</code>
     * using a finder method using the passed in scheduled hearing id
     * <code>Integer</code> value.
     * 
     * @param scheduledHearingId
     *            The primary key of the scheduled hearing
     * @return The looked up defendant on offence
     * @see uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing
     *      .XhbScheduledHearingBeanHelper#findByPrimaryKey(java.lang.Integer).
     */
    public static XhbScheduledHearing getXhbScheduledHearing(Integer scheduledHearingId) {
        return XhbScheduledHearingBeanHelper2.findByPrimaryKey(scheduledHearingId);
    }

    /**
     * Utility method used to acquire the <code>XhbScheduledHearing</code>
     * using a finder method using the passed case id and entry date.
     * 
     * @param caseId
     *            The primary key of the case we want to find a scheduled
     *            hearing for
     * @param entryDate
     *            The date on which the scheduled hearing should occur
     * @return The looked up scheduled heraing
     * @see uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing
     *      .XhbScheduledHearingBeanHelper#findByCaseIdAndHearingDate
     *      (java.lang.Integer, java.util.Date).
     */
    public static XhbScheduledHearing getXhbScheduledHearing(Integer caseId, Date entryDate) {
        final Date hearingDate = DateTimeUtilities.stripTimeToUtilDate(entryDate);

        Iterator it = XhbScheduledHearingBeanHelper2.findByCaseIdAndHearingDate(caseId, hearingDate).iterator();

        if (!it.hasNext()) {
            throw new XhbScheduledHearingBeanNotFoundException();
        }

        // Not sure whether that this is right
        return (XhbScheduledHearing) it.next();
    }

    /**
     * Utility method used to acquire the <code>XhbCourtLogCategoryDesc</code>
     * using a finder method using the passed in category description.
     * 
     * @param categoryDescription
     *            The description of the <code>XhbCourtLogCategoryDesc</code>
     *            that we want looked up.
     * @return The looked up court log category description
     * @see uk.gov.courtservice.xhibit.business.entities
     *      .xhb_court_log_category_desc.XhbCourtLogCategoryDescBeanHelper
     *      #findByCategoryDescription(java.lang.String).
     * 
     * @deprecated Use XhbCourtLogCategoryDescBeanHelper2
     *             .findByCategoryDescription(categoryDescription) directly
     */
    public static XhbCourtLogCategoryDesc getXhbCourtLogCategoryDesc(String categoryDescription) {
        return XhbCourtLogCategoryDescBeanHelper2.findByCategoryDescription(categoryDescription);
    }
}
