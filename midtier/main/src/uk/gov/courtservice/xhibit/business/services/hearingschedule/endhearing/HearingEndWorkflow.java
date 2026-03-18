package uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecord;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecordMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocal;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.DefHearingRecordValueHelper;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.HearingProgressValue;

/**
 * <p>
 * Title: HearingEndWorkflow
 * </p>
 * <p>
 * Description: The workflow for End Hearing functionality.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version $Revision: 1.34 $
 */
public class HearingEndWorkflow {
    private static final Logger log = CSServices.getLogger(HearingEndWorkflow.class);

    // Maintainers - all other required maintainers have a getInstance()
    // method
    private final ScheduledHearingMaintainer schedHearingMaintainer = new ScheduledHearingMaintainer();

    private final DefHearingRecordValueHelper defHearingRecordHelper = new DefHearingRecordValueHelper();
    
    public HearingEndWorkflow() {
        log.debug("HearingEndWorkflow default constructor");
    }

    /**
     * Reverse the effects of an endHearing call.
     * 
     * @param hearingId
     * @param scheduledHearingId
     * @param defendantOnCaseId
     */
    public void deleteEndHearing(
            Integer hearingId, 
            Integer scheduledHearingId, 
            Integer defendantOnCaseId, String userDisplayName)
    throws HearingEndedValidationException {
     
        log.debug("deleteEndHearing(..) START::hearingId = " 
                + hearingId + "; scheduledHearingId = " + scheduledHearingId
                + "; defendantOnCaseId = " + defendantOnCaseId);

        final Hearing hearing;

        try {
            hearing = HearingMaintainer.getInstance().findByPK(hearingId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new EJBException(e);
        }
        
        // The following call can actually create records
        final DefHearingRecord[] defHearingRecords = 
            HearingEndHelper.getDefHearingRecords(hearing, userDisplayName);
        
        if (defendantOnCaseId != null) {
            for (DefHearingRecord defHearingRecord : defHearingRecords) {
                if (defHearingRecord.getDefendantOnCaseId().equals(defendantOnCaseId)) {
                    log.debug("Delete the defendant-level hearing-end");
                    defHearingRecord.setHearingStartDate(null);
                    defHearingRecord.setHearingEndDate(null);
                } 
            }
        }

        log.debug("Delete the case-level hearing-end");
        hearing.setHearingEndDate(null);
        hearing.setHearingStartDate(null);
        deleteScheduledHearingFinished(scheduledHearingId, userDisplayName);

        log.debug("deleteEndHearing(..) FINISHED");
    }
    
    /**
     * Method used to set the specified scheduled hearing to not finished.
     * If the hearing is FINISHED then it is changed to IN_PROGRESS.  If it
     * is TO_BE_HEARD then it is left alone, because the hearing has not started.
     * 
     * @param scheduledHearingId
     */
    private void deleteScheduledHearingFinished(Integer scheduledHearingId, String userDisplayName) {
        try {
            ScheduledHearing sh = schedHearingMaintainer.findByPK(scheduledHearingId);
            if (sh.getHearingProgress() != null 
                    && sh.getHearingProgress().equals(HearingProgressValue.FINISHED)) {
                setScheduledHearingState(scheduledHearingId, HearingProgressValue.IN_PROGRESS, userDisplayName);
            }
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new EJBException(e);
        }
    }
    
    /**
     * Method used to attempt to end the passed in hearing. If a defendant on
     * case id is passed in, the hearing will be ended for the defendant. If no
     * defendant on case id is passed in, or the defendant is the last un-ended
     * defendant on the hearing (or only), then the hearing will be ended.
     * <p>
     * If the hearing is set to ended, the hearing progress will also be set to
     * finished.
     * </p>
     * 
     * @param hearing
     *            The hearing id that we wish to end
     * @param scheduledHearingId
     *            The scheduled hearing id to set to finished
     * @param defendantOnCaseId
     *            The defendant on case id for the defendant we want to end the
     *            hearing for
     * @throws HearingEndedAlreadyException
     *             If the hearing has already ended, at either the hearing
     *             level, or at the defendant level
     * @throws HearingEndedValidationException
     *             (The parent of <code>HearingEndedAlreadyException</code>)
     *             if any errors occur
     */
    public void endHearing(Integer hearingId, Integer scheduledHearingId, Integer defendantOnCaseId, String userDisplayName)
            throws HearingEndedAlreadyException, HearingEndedValidationException {
        log.debug("endHearing(..) START::hearingId = " + hearingId + "; scheduledHearingId = " + scheduledHearingId
                + "; defendantOnCaseId = " + defendantOnCaseId);

        final Hearing hearing;

        try {
            hearing = HearingMaintainer.getInstance().findByPK(hearingId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new EJBException(e);
        }

        if (isHearingEnded(hearing)) {
            throw new HearingEndedAlreadyException(EndHearingConstants.HEARING_ALREADY_ENDED,
                    "Cannot modify a hearing if it has already been ended");
        }

        // used to indicate if we should end the hearing, or not, we may
        // wish not to if there are more defendants on the case who have
        // do not have an associated end hearing event
        boolean endHearing = true;
        
        // The following call can actually create records
        final DefHearingRecord[] defHearingRecords = 
            HearingEndHelper.getDefHearingRecords(hearing, userDisplayName);

        // then it is a defendant level event...
        if (defendantOnCaseId != null) {
            for (DefHearingRecord defHearingRecord : defHearingRecords) {
                if (defHearingRecord.getDefendantOnCaseId().equals(defendantOnCaseId)) {
                    log.debug("End the defendant-level hearing");
                    endDefHearingRecord(defHearingRecord);
                } else if (!isDefHearingRecordEnded(defHearingRecord)) {
                    endHearing = false;
                }
            }
        }

        // If we should also (or only) end the hearing - this is true if
        // for a case level event (for B and U cases - where there are no
        // defendants) or if this was the last defendant on the case to
        // have and end hearing event generated
        if (endHearing) {
            log.debug("End the case-level hearing");
            endHearing(hearing, userDisplayName);
            setScheduledHearingFinished(scheduledHearingId, userDisplayName);
        }

        log.debug("endHearing(..) FINISHED");
    }

    /**
     * Method used to extract the functionality to actually end a hearing. And
     * calculate and populate the hearing start and end dates.
     * 
     * @param hearing
     *            The hearing to end
     * @param defHearingRecords
     *            An array of all of the <code>DefHearingRecord</code>s for
     *            the hearing
     * @throws HearingEndedDateCalculationException
     * @throws ObjectNotFoundException
     */
    private void endHearing(Hearing hearing, String userDisplayName) throws HearingEndedDateCalculationException {
        // always calculate the dates from the scheduled hearings...
        final StartAndEndDates hearingStartAndEndDates = 
            getHearingDatesFromScheduledHearing(hearing, null);

        // validate the hearing start and hearing end dates...
        HearingEndValidationHelper.validateDates(
                hearingStartAndEndDates.getEarliestStartTimestamp(),
                hearingStartAndEndDates.getLatestEndTimestamp());

        setHearingState(
                hearing, 
                hearingStartAndEndDates.getEarliestStartTimestamp(),
                hearingStartAndEndDates.getLatestEndTimestamp(), userDisplayName);
    }
    
    private void setHearingState(Hearing hearing, Date startDate, Date endDate, String userDisplayName) {
        final HearingBasicValue hbv = 
            HearingMaintainer.getInstance().getHearingBasicValue(hearing);

        hbv.setHearingStartDate(startDate);
        hbv.setHearingEndDate(endDate);

        try {
            HearingMaintainer.getInstance().update(hbv, userDisplayName);
        } catch (ObjectNotFoundException e) {
            // could not find the entity
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException("Unable to update for the hearing (id): " + hbv.getId(), e);
        }
    }

    /**
     * Method used to set the specified scheduled hearing to finished
     * 
     * @param scheduledHearingId
     */
    private void setScheduledHearingFinished(Integer scheduledHearingId, String userDisplayName) {
        setScheduledHearingState(scheduledHearingId, HearingProgressValue.FINISHED, userDisplayName);
    }
    
    private void setScheduledHearingState(Integer scheduledHearingId, Integer state, String userDisplayName) {
        if (scheduledHearingId != null) {
            try {
                final HearingScheduleControllerLocal tmp = (HearingScheduleControllerLocal) CSServices.getEJBServices()
                        .createLocalSession(HearingScheduleControllerLocalHome.class);
                tmp.updateHearingProgress(scheduledHearingId, state, userDisplayName);
                
                
            } catch (HearingScheduleException e) {
                // problem setting scheduled hearing to finished...
                CSServices.getDefaultErrorHandler().handleError(e, HearingEndHelper.class);
                throw new EJBException("Unable to set scheduledHearing " + scheduledHearingId + " state", e);
            }
        }
    }

    /**
     * End the hearing for only the specified defendant
     * 
     * @param defendantOnCaseId
     * @param hearingId
     * @param userDisplayName
     * @throws HearingEndedValidationException
     */
    public void endUnendedHearing(Integer defendantOnCaseId, Integer hearingId, String userDisplayName) 
    		throws HearingEndedValidationException {
    	DefHearingRecord defHearingRecord;
    	DefHearingRecordMaintainer defHearingRecordMaintainer = DefHearingRecordMaintainer.getInstance();
		try {
			defHearingRecord = defHearingRecordMaintainer
					.findByDefendantOnCaseIDAndHearingID(defendantOnCaseId, hearingId);
			// End the selected hearing
			endDefHearingRecord(defHearingRecord);
			// Was that the last hearing to be ended
			Hearing hearing = getHearing(hearingId);
			Boolean hasUnendedHearing = isUnendedHearing(hearing, defHearingRecord, userDisplayName);
		    // If not then stamp the scheduled hearing as ended
			if (!hasUnendedHearing) {
				endScheduledHearing(hearing, userDisplayName);
			}
		} catch (ObjectNotFoundException ex) {
			// No record, so no update required
		} catch (HearingEndedAlreadyException ex) {
			// Record already ended so no update required
		} catch (HearingEndedValidationException ex) {
			throw ex;
		}
    }
    
    /**
     * End the hearing for only the specified defendant
     * 
     * @param defHearingRecord
     * @return
     * @throws HearingEndedAlreadyException
     *             If the defendant hearing has already been ended
     */
    private void endDefHearingRecord(final DefHearingRecord defHearingRecord) throws HearingEndedAlreadyException,
            HearingEndedValidationException {
        if (isDefHearingRecordEnded(defHearingRecord)) {
            String defendantName = getDefendantName(defHearingRecord.getDefendantOnCaseId());
            throw new HearingEndedAlreadyException(EndHearingConstants.DEFENDANT_HEARING_ALREADY_ENDED,
                    new Object[] { defendantName }, "Can't end a hearing for a defendant if it has already been ended",
                    null);
        }

        // get earliest start and latest end dates for all scheduled
        // hearings on the hearing that the defendant is also on
        final StartAndEndDates dates = getHearingDatesFromScheduledHearing(defHearingRecord.getHearing(),
                defHearingRecord.getDefendantOnCaseId());

        defHearingRecord.setHearingStartDate(dates.getEarliestStartTimestamp());
        defHearingRecord.setHearingEndDate(dates.getLatestEndTimestamp());
    }

    /**
     * Method used to attempt to end the Hearing passed in, and all linked
     * hearings. Only those hearings that are not already ended will be ended.
     * <p>
     * If we end a hearing, we will also be setting the hearing progress of the
     * last scheduled hearing for that hearing to finished.
     * </p>
     * 
     * @param hearingId
     *            The id of hearing entity that we wish to end
     * @throws HearingEndedValidationException
     *             if any errors occur
     */
    public void endAllUnendedHearings(Integer hearingId, String userDisplayName) throws HearingEndedValidationException {
        log.debug("endAllUnendedHearings(..) START::hearingId = " + hearingId);

        final Hearing hearing = getHearing(hearingId);

        // first get an array containing all of the Hearings associated
        final Hearing[] hearings = HearingEndHelper.getAllLinkedHearings(hearing);

        for (int i = 0; i < hearings.length; i++) {
            // only attempt to end those not already ended...
            if (!isHearingEnded(hearings[i])) {
                final DefHearingRecord[] defHearingRecords = HearingEndHelper.getDefHearingRecords(hearings[i], userDisplayName);

                for (int j = 0; j < defHearingRecords.length; j++) {
                    // only attempt to end those not already ended...
                    if (!isDefHearingRecordEnded(defHearingRecords[j])) {
                        endDefHearingRecord(defHearingRecords[j]);
                    }
                }

                // now actually end the hearing...
                endScheduledHearing(hearings[i], userDisplayName);
            }
        }

        log.debug("endAllUnendedHearings(..) FINISHED");
    }

    /*
     * End the scheduled hearing
     */
    private void endScheduledHearing(Hearing hearing, String userDisplayName) throws HearingEndedDateCalculationException {
    	// end the hearing...
        endHearing(hearing, userDisplayName);

        // and set the last scheduled hearing to finished...
        final Integer scheduledHearingId = HearingEndHelper.getMostRecentScheduledHearingId(hearing);
        setScheduledHearingFinished(scheduledHearingId, userDisplayName);
    }
    
    /**
     * Get the DefHearingRecordValue for the given hearing and defendant ids.
     * 
     * @param hearingID
     *            Integer the id of the hearing
     * @param defID
     *            The id of the defendant
     * @return the DefHearingRecordValue
     * @throws HearingRecordException
     */
    public DefHearingRecordValue getDefHearingRecord(Integer hearingID, Integer defID) throws HearingRecordException {
        log.debug("getDefHearingRecord() called");
        try {
            // find the hearing
            Hearing hearing = HearingMaintainer.getInstance().findByPK(hearingID);
            // get the case id to find the doc id
            Integer caseID = hearing.getCaseId();
            Integer docID = XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(defID, caseID).getDefendantOnCaseId();

            final DefHearingRecordValue defHearingRecordValue = defHearingRecordHelper.getDefHearingRecordValue(docID,
                    hearingID);

            log.debug("getDefHearingRecord finished ok about to return");

            return defHearingRecordValue;
        } catch (ObjectNotFoundException ex) {
            // could not find the object
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * This will re-calculate the duration time and return the updated
     * DefHearingRecordValue. If the user is not happy with the time - the time
     * for the court log events can be changed and the user can thereafter
     * re-calculate the time.
     * 
     * @param defHearingRecordValue
     * @return the DefHearingRecordValue update with the recalculated hearing
     *         duration.
     * @throws HearingEndedValidationException
     */
    public void reCalculateHearingDuration(DefHearingRecordValue defHearingRecordValue, String userDisplayName)
            throws HearingEndedValidationException {
        log.debug("reCalculateHearingDuration called");

        try {
            // find the hearing
            Hearing hearing = HearingMaintainer.getInstance().findByPK(defHearingRecordValue.getHearingID());

            Integer docID = defHearingRecordValue.getDefendantOnCaseID();

            // Get all scheduled hearings for that particular hearing using
            // CMR so that the duration can be calculated.
            Collection scheduledHearings = hearing.getScheduledHearings();
            Iterator scheduledHearingsIterator = scheduledHearings.iterator();

            // iterate through all the values and add them to a vector of
            // Basic values.
            Vector<ScheduledHearingBasicValue> scheduledHearingBasicValues = 
                new Vector<ScheduledHearingBasicValue>();

            while (scheduledHearingsIterator.hasNext()) {
                ScheduledHearing local = (ScheduledHearing) scheduledHearingsIterator.next();
                // convert to basic value
                ScheduledHearingBasicValue value = schedHearingMaintainer.createBasicVO(local);
                scheduledHearingBasicValues.addElement(value);
            }// end while loop

            // Calculate the new duration and set it to the hearingValue.
            Long newDurationTime = HearingEndHelper.calculateDuration(scheduledHearingBasicValues, docID);

            final DefHearingRecord defHearingRecord = DefHearingRecordMaintainer.getInstance()
                    .findByDefendantOnCaseIDAndHearingID(docID, hearing.getHearingId());

            if (!defHearingRecord.getVersion().equals(defHearingRecordValue.getVersion())) {
                log.error("reCalculateHearingDuration OptimisticLock exception - Entity: "
                        + defHearingRecord.getVersion() + "VO: " + defHearingRecordValue.getVersion());
                throw new OptimisticLockException("Optimistic Lock Error");
            }

            this.amendHearingDuration(defHearingRecord, newDurationTime, userDisplayName);
        } catch (HearingEndedDurationCalculationException ex) {
            // problems calculating the hearing duration
            HearingEndedValidationException hevex = new HearingEndedValidationException(ex.getUserMessageAsMessage()
                    .getKey(), ex.getMessage(), ex);

            CSServices.getDefaultErrorHandler().handleError(hevex, getClass(), hevex.toString());

            throw hevex;
        } catch (ObjectNotFoundException ex) {
            // could not find the object
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
        log.debug("reCalculateHearingDuration finished ok");
    }

    /**
     * Manually amending the hearing duration, now defendant level so changing
     * the defHearingRecord
     * 
     * @param defHearingRecordId
     *            Integer
     * @param newDuration
     *            Long in milliseconds
     */
    public void amendHearingDuration(Integer defHearingRecordId, Long newDuration, String userDisplayName) {
        log.debug("amendHearingDuration(Integer, Long) called");

        try {
            final DefHearingRecord defHearingRecord = DefHearingRecordMaintainer.getInstance().findByPrimaryKey(
                    defHearingRecordId);

            amendHearingDuration(defHearingRecord, newDuration, userDisplayName);
        } catch (ObjectNotFoundException ex) {
            // could not find the entity
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException("Unable to find the defHearingRecord for (id): " + defHearingRecordId, ex);
        }
    }

    private void amendHearingDuration(DefHearingRecord defHearingRecord, Long newDuration, String userDisplayName) {
        log.debug("amendHearingDuration(DefHearingRecord, Long) called");

        if (log.isDebugEnabled()) {
            log.debug("defHearingRecord entity id = " + defHearingRecord.getHearingId() + ", new duration : "
                    + newDuration.toString());
        }

        DefHearingRecordBasicValue bv = DefHearingRecordMaintainer.getInstance().getDefHearingRecordBasicValue(
                defHearingRecord);
        bv.setLastCalculatedDuration(newDuration);

        if (log.isDebugEnabled()) {
            log.debug("try to update DefHearingRecordBasicValue : " + bv.toString());
        }

        try {
            DefHearingRecordMaintainer.getInstance().update(bv, userDisplayName);
        } catch (ObjectNotFoundException ex) {
            // could not find the entity
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException("Unable to find the defHearingRecord for (id): " + bv.getId(), ex);
        }

        log.debug("amendHearingDuration() finished ok");
    }

    /**
     * Acquire the hearing earliest start and latest end date from all of the
     * scheduled hearing original/notBefore times. If the passed in defendant on
     * case id is not null, then only those scheduled hearings with the
     * defendant on them will be checked.
     * 
     * @param hearing
     * @param docId
     *            the defendant on case id, null if for all scheduled hearings
     * @return
     */
    private StartAndEndDates getHearingDatesFromScheduledHearing(Hearing hearing, Integer docId) {
        // we will be returning this value...
        final StartAndEndDates startAndEndDates = new StartAndEndDates();
        final Iterator it = hearing.getScheduledHearings().iterator();

        while (it.hasNext()) {
            final ScheduledHearing scheduledHearing = (ScheduledHearing) it.next();
            final Date originalDate = HearingEndHelper.getScheduledHearingDate(scheduledHearing);

            if (startAndEndDates.isUpdateRequired(originalDate)
                    && ((docId == null) || HearingEndHelper.isDefendantOnScheduledHearing(scheduledHearing, docId))) {
                startAndEndDates.setEarliestStartDate(originalDate);
                startAndEndDates.setLatestEndDate(originalDate);
            }
        }

        return startAndEndDates;
    }

    /**
     * Private class used to maintain a copy of the earliest start date, and the
     * latest end date. In this implementation, they are used to represent the
     * earliest/latest hearing start/end dates at either the case level or the
     * defendant level. The accessor methods for the fields return the
     * earliest/latest dates with all time fields set to 0 (effectively making
     * it midnight) as a <code>java.sql.Timestamp</code> so as to allow direct
     * insertion into the required fields.
     */
    private class StartAndEndDates {
        private Date earliestStartDate = null;

        private Date latestEndDate = null;

        /**
         * Only available mutator method for the internally stored earliest
         * start date variable. This method will only update the variable if the
         * passed in parameter is not <i>null</i> and that either the instance
         * variable is not set, or the input parameter is older than that of the
         * instance variable.
         * 
         * @param inputDate
         *            The <code>java.util.Date</code> Object to check
         */
        public void setEarliestStartDate(Date inputDate) {
            if ((inputDate != null) && ((this.earliestStartDate == null) || inputDate.before(this.earliestStartDate))) {
                this.earliestStartDate = inputDate;
            }
        }

        /**
         * Only available mutator method for the internally stored latest end
         * date variable. This method will only update the variable if the
         * passed in parameter is not <i>null</i> and that either the instance
         * variable is not set, or the input parameter is later than that of the
         * instance variable.
         * 
         * @param inputDate
         *            The <code>java.util.Date</code> Object to check
         */
        public void setLatestEndDate(Date inputDate) {
            if ((inputDate != null) && ((this.latestEndDate == null) || inputDate.after(this.latestEndDate))) {
                this.latestEndDate = inputDate;
            }
        }

        /**
         * Method used to determine if the passed in <code>java.sql.Date</code>
         * value would actually alter this Objects internal earliest start or
         * latest end dates.
         * 
         * @param inputDate
         *            The <code>java.util.Date</code> to check
         * @return <i>true</i> if any alteration would be made, or <i>false
         *         </i> otherwise, note that this does not actually modify this
         *         Object.
         */
        public boolean isUpdateRequired(Date inputDate) {
            return ((inputDate != null) && ((this.earliestStartDate == null) || this.latestEndDate == null)
                    || ((inputDate != null) && inputDate.before(this.earliestStartDate)) 
                    || ((inputDate != null) && inputDate.after(this.latestEndDate)));
        }

        /**
         * Only available accessor method to the earliest start date variable.
         * Converts the internal <code>java.util.Date</code> Object into a
         * <code>java.sql.Timestamp</code> without time fields set
         * 
         * @return The <code>java.sql.Timestamp</code>
         */
        public Timestamp getEarliestStartTimestamp() {
            return HearingEndHelper.convertDateToTimestamp(this.earliestStartDate);
        }

        /**
         * Only available accessor method to the latest end date variable.
         * Converts the internal <code>java.util.Date</code> Object into a
         * <code>java.sql.Timestamp</code> without time fields set
         * 
         * @return The <code>java.sql.Timestamp</code>
         */
        public Timestamp getLatestEndTimestamp() {
            return HearingEndHelper.convertDateToTimestamp(this.latestEndDate);
        }
    }

    /**
     * This method will check if a hearing has been ended. If the hearing has
     * been ended the returning Boolean will be true else false.
     * 
     * @param schedHearingID
     *            Integer
     * @return Boolean
     */
    public Boolean isHearingEnded(Integer schedHearingID) {
        log.debug("HearingEndWorkflow.isHearingEnded(Integer schedHearingID) called " + " with schedHearingID : "
                + schedHearingID.toString());

        XhbScheduledHearing schedHearingEntity = XhbScheduledHearingBeanHelper2.findByPrimaryKey(schedHearingID);
        return new Boolean(isHearingEnded(schedHearingEntity.getXhbHearing()));
    }

    /**
     * Method used to determine if a hearing has ended. A hearing is classified
     * as to have ended if it has a hearing end date, AND it has a hearing start
     * date.
     * 
     * @param hearing
     *            the hearing to check
     * @return <i>true</i> if ended, <i>false</i> otherwise
     */
    public boolean isHearingEnded(Hearing hearing) {
        final boolean isHearingEnded = (hearing.getHearingStartDate() != null) && (hearing.getHearingEndDate() != null);

        log.debug("isHearingEnded() - Hearing id = " + hearing.getHearingId() + "::" + isHearingEnded);

        return isHearingEnded;
    }

    /**
     * Method used to determine if a hearing has ended. A hearing is classified
     * as to have ended if it has a hearing end date, AND it has a hearing start
     * date. Overloaded for the new entity layer...
     * 
     * @param hearing
     *            the hearing to check
     * @return <i>true</i> if ended, <i>false</i> otherwise
     */
    public boolean isHearingEnded(XhbHearing hearing) {
        final boolean isHearingEnded = hearing.isHearingEnded();

        log.debug("isHearingEnded() - Hearing id = " + hearing.getHearingId() + "::" + isHearingEnded);

        return isHearingEnded;
    }

    /**
     * Method used to determine if a hearing has ended for a particular
     * defendant. A hearing is classified as to have ended if the passed in
     * <code>DefHearingRecord</code> has a hearing end date set.
     * 
     * @param defHearingRecord
     *            the defendants hearing to check
     * @return <i>true</i> if ended, <i>false</i> otherwise
     */
    private boolean isDefHearingRecordEnded(DefHearingRecord defHearingRecord) {
        return (defHearingRecord.getHearingEndDate() != null);
    }

    /**
     * Utility method to acquire the defendants name using the passed in
     * defendat on case id
     * 
     * @param defendantOnCaseId
     * @return The formatted, defendants name
     * @throws HearingEndedValidationException
     */
    private String getDefendantName(Integer defendantOnCaseId) {
        final XhbDefendant defendant = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defendantOnCaseId)
                .getXhbDefendant();

        final StringBuffer name = new StringBuffer();

        // add firstname and any formatting
        if (defendant.getFirstName() != null) {
            name.append(defendant.getFirstName());
        }

        if (defendant.getMiddleName() != null) {
            if (name.length() > 0)
                name.append(' ');
            name.append(defendant.getMiddleName());
        }

        // add the surname
        if (defendant.getSurname() != null) {
            if (name.length() > 0)
                name.append(' ');
            name.append(defendant.getSurname());
        }

        return name.toString();
    }
    
    private Hearing getHearing(Integer hearingId) {
    	Hearing hearing = null;
    	try {
            hearing = HearingMaintainer.getInstance().findByPK(hearingId);
        } catch (ObjectNotFoundException e) {
            // could not find the entity
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException("Unable to find the hearing (id): " + hearingId, e);
        }
    	return hearing;
    }
    
    /*
     * Test if there is another unended defendant hearing record on this hearing
     */
    private Boolean isUnendedHearing(Hearing hearing, DefHearingRecord currentDefHearingRecord, String userDisplayName) throws HearingEndedValidationException {
    	Boolean unendedExists = false;
    
    	if (!isHearingEnded(hearing)) {
            final DefHearingRecord[] defHearingRecords = HearingEndHelper.getDefHearingRecords(hearing, userDisplayName);

            for (int j = 0; j < defHearingRecords.length; j++) {
            	if (currentDefHearingRecord == null || 
            			defHearingRecords[j].equals(currentDefHearingRecord)) {
	                // only attempt to end those not already ended...
	                if (!isDefHearingRecordEnded(defHearingRecords[j])) {
	                	unendedExists = true;
	                    break;
	                }
            	}
            }
    	}
    	return unendedExists;
    }
    
}
