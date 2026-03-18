package uk.gov.courtservice.xhibit.business.database.query.counsel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.CourtClerkValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseHeaderValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.UsherValue;

/**
 * <p>
 * Title: CounselRowProcessor
 * </p>
 * <p>
 * Description: Class to process the counsel data that will be used for the
 * counsel sign in.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford / Marie Holmberg
 * @version 1.0
 */

class CounselRowProcessor extends AbstractRowProcessor {
    // The logger for this class.
    private static final Logger log = CSServices.getLogger(CounselRowProcessor.class);

    // Instance cache
    private final Map scheduledHearingCache = new HashMap();

    private final Collection scheduledHearingOrderedCache = new ArrayList();

    // Child Processor for defendant details
    private final DefendantRowProcessor defendantRowProcessor = new DefendantRowProcessor();

    // Child Processor for Legal representative details
    private final LegalRepRowProcessor legalRepRowProcess = new LegalRepRowProcessor();

    /**
     * Default constructor that adds the child processors
     */
    CounselRowProcessor() {
        addChildProcessor(defendantRowProcessor);
        addChildProcessor(legalRepRowProcess);
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     *            from the resultset
     */
    public void processRow(Row row) {
        log.debug("CounselRowProcessor.processRow() called");

        // The CounselDataHolder that will hold all information per scheduled
        // hearingid
        final CounselDataHolder counselDataHolder;

        // get all the necessary info from the row (not childProcess info)
        final Integer scheduledHearingId = getScheduledHearingId(row);
        final Integer courtRoomId = getCourtRoomId(row);
        final Integer caseNumber = getCaseNumber(row);
        final String caseType = getCaseType(row);
        final String caseSubType = getCaseSubType(row);
        final String caseTitle = getCaseTitle(row);
        final Date timeListed = getTimeListed(row);
        final String courtRoomDesc = getCourtRoomDesc(row);
        final String courtRoomName = getCourtRoomName(row);
        final String courtRoomDisplayName = getCourtRoomDisplayName(row);
        final Integer schedHearingDefId = getSchedHearingDefId(row);
        final String legalRole = getLegalRole(row);
        final String isFloating = getIsFloating(row);
        final String staffName = getStaffName(row);
        final String staffRole = getStaffRole(row);
        final String hearingTypeDesc = getHearingTypeDesc(row);
        final String hearingTypeCode = getHearingTypeCode(row);
        final Integer crestCourtRoomNo = getCrestCourtroomNumber(row);
        // PRE00180
        final String courtSiteCode = getCourtSiteCode(row);
        final Integer sittingSequenceNo = getSittingSequenceNo(row);
        final Integer shSequenceNo = getShSequenceNo(row);
        final String courtSiteShortName = getCourtSiteShortName(row);

        if (scheduledHearingCache.containsKey(scheduledHearingId)) {
            // Get the object from the cache
            log.debug("The scheduledHearingCache contains the key : " + scheduledHearingId);
            counselDataHolder = (CounselDataHolder) scheduledHearingCache.get(scheduledHearingId);
        } else {
            log.debug("The scheduledHearingCache does not contain the key : " + scheduledHearingId);

            // Populate the PartyOnCaseHeaderValue
            PartyOnCaseHeaderValue pocHeaderValue = new PartyOnCaseHeaderValue(scheduledHearingId, courtRoomId,
                    caseNumber, caseType, caseSubType, caseTitle, timeListed, courtRoomDesc, courtRoomName,
                    courtRoomDisplayName, isFloating, hearingTypeDesc, hearingTypeCode, crestCourtRoomNo,
                    courtSiteCode, sittingSequenceNo, shSequenceNo, courtSiteShortName);

            // Create a new holder for the scheduled hearing ID
            counselDataHolder = new CounselDataHolder(pocHeaderValue);

            // and add it to the map and collection

            // add the scheduledhearingId to the map as the key and
            // the CounselDataHolder as the value. This will replace the old
            // one
            // in case the map already contained the key.
            this.scheduledHearingCache.put(scheduledHearingId, counselDataHolder);
            this.scheduledHearingOrderedCache.add(counselDataHolder);
        }

        // If no scheduled hearing defendant id found, add a new defendant else
        // populate PartyOnCaseValue with the existing one. This will be used to
        // add the representative.
        PartyOnCaseValue defPartyOnCaseValue = (PartyOnCaseValue) counselDataHolder.getDefendantData().get(
                schedHearingDefId);

        if (defPartyOnCaseValue == null) {
            log.debug("Will create a new PartyOnCaseValue for the defendat with shdefid: " + schedHearingDefId);
            // Set the header information in the new PartyOnCaseValue for
            // this defendant.
            defPartyOnCaseValue = counselDataHolder.setHeaderInformation();

            // set the schedule_hear_def_id
            defPartyOnCaseValue.setScheduledHearingDefendantId(schedHearingDefId);

            // set the party role for the defendant.
            defPartyOnCaseValue.setPartyRole(getDefendacePartyRole(defPartyOnCaseValue.getCaseType()));

            // use data from child row processor to populate a
            // DefendantValue
            // add DefendantValue List to the partyOnCaseValue collection
            defPartyOnCaseValue.getDefendants().add(defendantRowProcessor.getDefendant());

            // add partyOnCaseValue to hashmap keyed on schedDefId
            counselDataHolder.getDefendantData().put(schedHearingDefId, defPartyOnCaseValue);
        }

        if ((legalRole != null) && (legalRole.length() > 0)) {
            log.debug("Will set the legal rep with role : " + legalRole);

            // Set the Objector if not already set.
            if (legalRole.equalsIgnoreCase(CounselConstants.PARTY_ROLE_OBJECTOR)) {
                if (!counselDataHolder.getObjectorSet().contains(legalRepRowProcess.getLegalRep().getRefLegalRepId())) {
                    counselDataHolder.getObjector().getRepresentatives().add(legalRepRowProcess.getLegalRep());
                    counselDataHolder.getObjectorSet().add(legalRepRowProcess.getLegalRep().getRefLegalRepId());
                }
            }
            // Set the Respondent if not already set
            else if (legalRole.equalsIgnoreCase(CounselConstants.PARTY_ROLE_RESPONDENT)) {
                if (!counselDataHolder.getRespondentSet().contains(legalRepRowProcess.getLegalRep().getRefLegalRepId())) {
                    counselDataHolder.getRespondent().getRepresentatives().add(legalRepRowProcess.getLegalRep());
                    counselDataHolder.getRespondentSet().add(legalRepRowProcess.getLegalRep().getRefLegalRepId());
                }
            }
            // Set the Prosecutor if not already set
            else if (legalRole.equalsIgnoreCase(CounselConstants.PARTY_ROLE_PROSECUTION)) {
                if (!counselDataHolder.getProsecutorSet().contains(legalRepRowProcess.getLegalRep().getRefLegalRepId())) {
                    counselDataHolder.getProsecutor().getRepresentatives().add(legalRepRowProcess.getLegalRep());
                    counselDataHolder.getProsecutorSet().add(legalRepRowProcess.getLegalRep().getRefLegalRepId());
                }
            }
            // Set the Defence/Appellant counsel
            else if (legalRole.equalsIgnoreCase(CounselConstants.PARTY_ROLE_DEFENDANT)
                    || legalRole.equalsIgnoreCase(CounselConstants.PARTY_ROLE_APPELLENT)) {
                PartyOnCaseValue pocValue = (PartyOnCaseValue) counselDataHolder.getDefendantData().get(
                        schedHearingDefId);
                pocValue.getRepresentatives().add(legalRepRowProcess.getLegalRep());
                counselDataHolder.getDefenceSet().add(legalRepRowProcess.getLegalRep().getRefLegalRepId());
                counselDataHolder.getDefendantData().put(schedHearingDefId, defPartyOnCaseValue);
            }
        }

        // Set the court staff
        setCourtStaffDetails(staffName, staffRole, counselDataHolder);
        log.debug("CounselRowProcessor.processRow() finished");
    }

    /**
     * This will set the court staff details depending on the role. We only want
     * to set court clerks and ushers.
     * 
     * @param staffName
     *            String
     * @param staffRole
     *            String
     * @param counselDataHolder
     *            CounselDataHolder
     * @return CounselDataHolder the same as the one passed in.
     */
    private void setCourtStaffDetails(String staffName, String staffRole, CounselDataHolder counselDataHolder) {
        // if there is a court clerk/usher then add to the List of court clerks
        // and ushers.
        if ((staffName != null) && (staffRole != null) && (staffName.length() > 0)) {
            // check if court clerk
            if (staffRole.equalsIgnoreCase(CounselConstants.COURT_CLERK_ROLE)) {
                if (!counselDataHolder.getCourtClerkMap().containsKey(staffName)) {
                    CourtClerkValue ccValue = new CourtClerkValue(staffName, CounselConstants.COURT_CLERK_ROLE);
                    counselDataHolder.getCourtClerkMap().put(staffName, ccValue);
                }
            }
            // check if usher
            else if (staffRole.equalsIgnoreCase(CounselConstants.USHER_ROLE)) {
                if (!counselDataHolder.getUsherMap().containsKey(staffName)) {
                    UsherValue usherValue = new UsherValue(staffName, CounselConstants.USHER_ROLE);
                    counselDataHolder.getUsherMap().put(staffName, usherValue);
                }
            }
        }
    }

    /**
     * Returns the list of counsel sign in data. To get all PartyOnCaseValues we
     * need to take out all values from the Hashmap where the scheduledhearingid
     * is the key. All the values will be added to the PartyOnCaseArrayList,
     * which will be returned.
     * 
     * @param removeRecordsWithNoLegReps
     *            boolean determine whether to remove records with no legal
     *            representatives.
     * @return PartyOnCaseValue - Collection
     */
    Collection getAssignedRepresentatives(boolean removeRecordsWithNoLegReps) {
        log.debug("CounselRowProcessor.getAssignedRepresentatives() called");
        final Collection partyOnCaseArray = new ArrayList();

        // loop through the hashmap and get each CounselHolder and its parties
        // and add that to the list
        // Set set = scheduledHearingCache.keySet();
        // Iterator it = set.iterator();
        final Iterator it = scheduledHearingOrderedCache.iterator();

        while (it.hasNext()) {
            // Integer key = (Integer) it.next();
            // CounselDataHolder holder = (CounselDataHolder)
            // scheduledHearingCache.get(key);
            CounselDataHolder holder = (CounselDataHolder) it.next();

            partyOnCaseArray.addAll(holder.getPartyOnCaseList(removeRecordsWithNoLegReps));
        }

        log.debug("CounselRowProcessor.getAssignedRepresentatives() finished. " + "Will return a list of size : "
                + partyOnCaseArray.size());
        return partyOnCaseArray;
    }

    /**
     * This method returns either CounselConstants.PARTY_ROLE_APPELLENT or
     * CounselConstants.PARTY_ROLE_DEFENDANT dependnig on the case type passed
     * in.
     * 
     * @param caseType
     *            case type
     * @return String
     */
    private String getDefendacePartyRole(String caseType) {
        if ((caseType != null) && caseType.equalsIgnoreCase(CounselConstants.APPEAL_CASE_TYPE)) {
            return CounselConstants.PARTY_ROLE_APPELLENT;
        } else {
            return CounselConstants.PARTY_ROLE_DEFENDANT;
        }
    }

    // get the time listed
    private Date getTimeListed(Row row) {
        return row.getTimestamp(CounselConstants.TIME_LISTED);
    }

    // get the scheduled hearing id
    private Integer getScheduledHearingId(Row row) {
        return new Integer(row.getInt(CounselConstants.SCHEDULED_HEARING_ID));
    }

    // get the court room id
    private Integer getCourtRoomId(Row row) {
        return new Integer(row.getInt(CounselConstants.COURT_ROOM_ID));
    }

    // get the case number
    private Integer getCaseNumber(Row row) {
        return new Integer(row.getInt(CounselConstants.CASE_NUMBER));
    }

    // get the case type
    private String getCaseType(Row row) {
        return row.getString(CounselConstants.CASE_TYPE);
    }

    // get the case sub type
    private String getCaseSubType(Row row) {
        return row.getString(CounselConstants.CASE_SUB_TYPE);
    }

    // get the case title
    private String getCaseTitle(Row row) {
        return row.getString(CounselConstants.CASE_TITLE);
    }

    // get the court room description
    private String getCourtRoomDesc(Row row) {
        return row.getString(CounselConstants.COURT_ROOM_DESCRIPTION);
    }

    // get the court room name
    private String getCourtRoomName(Row row) {
        return row.getString(CounselConstants.COURT_ROOM_NAME);
    }

    // get the court room display name
    private String getCourtRoomDisplayName(Row row) {
        return row.getString(CounselConstants.COURT_ROOM_DISPLAY_NAME);
    }

    // get the legal role
    private String getLegalRole(Row row) {
        return row.getString(CounselConstants.LEGAL_ROLE);
    }

    // get the scheduled hearing defendant id
    private Integer getSchedHearingDefId(Row row) {
        return new Integer(row.getInt(CounselConstants.SCHED_HEAR_DEF_ID));
    }

    // get the floating value for the case, 1=Unassigned
    private String getIsFloating(Row row) {
        return row.getString(CounselConstants.IS_FLOATING);
    }

    // get the court clerk
    private String getStaffName(Row row) {
        return row.getString(CounselConstants.STAFF_NAME);
    }

    // get the usher
    private String getStaffRole(Row row) {
        return row.getString(CounselConstants.STAFF_ROLE);
    }

    // get the hearing type description
    private String getHearingTypeDesc(Row row) {
        return row.getString(CounselConstants.HEARING_TYPE_DESC);
    }

    // get the hearing type code
    private String getHearingTypeCode(Row row) {
        return row.getString(CounselConstants.HEARING_TYPE_CODE);
    }

    // get the crest courtroom number
    private Integer getCrestCourtroomNumber(Row row) {
        return new Integer(row.getInt(CounselConstants.CREST_COURT_ROOM_NO));
    }

    // get the court site code
    private String getCourtSiteCode(Row row) {
        return row.getString(CounselConstants.COURT_SITE_CODE);
    }

    // get the sitting sequence number
    private Integer getSittingSequenceNo(Row row) {
        return new Integer(row.getInt(CounselConstants.SITTING_SEQUENCE_NO));
    }

    // get the scheduled hearing sequence number
    private Integer getShSequenceNo(Row row) {
        return new Integer(row.getInt(CounselConstants.SH_SEQUENCE_NO));
    }

    // get the court site short name
    private String getCourtSiteShortName(Row row) {
        return row.getString(CounselConstants.COURT_SITE_SHORT_NAME);
    }
}
