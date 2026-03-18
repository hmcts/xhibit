package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecord;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecordMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingType;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingTypeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendant;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_legal_aid_order.XhbLegalAidOrderBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_legal_aid_order.XhbLegalAidOrderBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ExportAValue;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRDefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingListSummaryValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingSummaryValue;

/**
 * Accesses relevant maintainers to construct single instances of the hearing
 * record summary value. called by the retrieve helper class as part of the
 * process which constructs the HearingSummaryValue. Each
 * HearingListSummaryValue contains case, hearing and defendant information.
 * 
 * @author Anthony Martin
 * @author Marie Holmberg - Re-factored
 * @version 1.1
 */
public class HearingListSummaryValueHelper // implements HRBasicValueHelper
// {
{
    private static final Logger log = CSServices.getLogger(HearingListSummaryValueHelper.class);

    // set the maintainers and helpers required for this class.
    private HearingMaintainer hearingMaintainer = getHearingMaintainer();

    private RefHearingTypeMaintainer refHearingTypeMaintainer = getRefHearingTypeMaintainer();

    private ScheduledHearingMaintainer scheduledHearingMaintainer = getScheduledHearingMaintainer();

    private SchedHearingDefendantMaintainer schedHearingDefendantMaintainer = getSchedHearingDefendantMaintainer();

    private DefendantOnCaseMaintainer defendantOnCaseMaintainer = getDefendantOnCaseMaintainer();

    private DefendantMaintainer defendantMaintainer = getDefendantMaintainer();

    private CaseMaintainer caseMaintainer = getCaseMaintainer();

    private DefHearingRecordMaintainer defHearingRecordMaintainer = getDefHearingRecordMaintainer();

    private HearingRecordStatusHelper statusHelper = getHearingRecordStatusHelper();

    private HearingRecordValidationHelper validationHelper = getHearingRecordValidationHelper();

    /**
     * Provides client with a list of summary data representing linked hearing
     * records; the HearingSummaryValue contains an exportA to indicate the
     * export status and a collection of linked summary data. (of course, there
     * may be only one member if it has no links).
     * 
     * @param hearingID
     *            Integer
     * @return HearingSummaryValue
     * @throws HearingRecordException
     */
    public HearingSummaryValue retrieveHearingSummaryValue(Integer hearingID, String userDisplayName) throws HearingRecordException {
        log.debug("entered HearingRecordWorkflow.retrieveHearingRecordList()");

        if (hearingID == null) {
            throw new IllegalArgumentException("Hearing id must be passed in.");
        }
        // create new summaryValue ready to populate with information
        HearingSummaryValue hearingSummaryValue = new HearingSummaryValue();
        Collection hearingListSummaryValueList = new ArrayList();
        HearingBasicValue hearingBasicValue = null;
        Collection linked_HearingBasicValueList = new ArrayList();
        Integer linkedHearingID = null;

        // find the main hearing represented by the hearingID
        hearingBasicValue = findHearingBasicValue(hearingID);

        // extract the linkedHearingID from the main hearing, to use when
        // finding all linked hearings
        linkedHearingID = hearingBasicValue.getLinkedHearingID();

        // find all the hearings linked to the main one if linkedHearingID !=
        // null
        if (linkedHearingID != null) {
            linked_HearingBasicValueList = findAllLinkedHearings(linkedHearingID);
        } else {
            linked_HearingBasicValueList.add(hearingBasicValue);
        }
        // run through all the linked hearings producing a
        // HearingListSummaryValue for each
        Iterator hearingsIt = linked_HearingBasicValueList.iterator();
        while (hearingsIt.hasNext()) {
            HearingBasicValue hbv = (HearingBasicValue) hearingsIt.next();
            HearingListSummaryValue hlsv = findHearingListSummaryValue(hbv, userDisplayName);
            hearingListSummaryValueList.add(hlsv);
        }
        // find out export status for these hearings populate main summary value
        ExportAValue exportValue = statusHelper.getExportValue(hearingID);
        hearingSummaryValue.setHearingListSummaryValues(hearingListSummaryValueList);
        hearingSummaryValue.setExportAValue(exportValue);
        return hearingSummaryValue;
    }

    /**
     * This will return a HearingListSummaryValue that will be populated with
     * the relevant case, hearing, defendant, hearing record information.
     * 
     * @param hbv
     *            HearingBasicValue
     * @return HearingListSummaryValue
     * @throws HearingRecordException
     */
    private HearingListSummaryValue findHearingListSummaryValue(HearingBasicValue hbv, String userDisplayName) throws HearingRecordException {
        HearingListSummaryValue list = new HearingListSummaryValue();
        // set hearing info
        list = populateHearingInfo(list, hbv);
        // set case info
        list = populateCaseInfo(list, hbv);
        // set hearing record defendants
        Collection hrDefendants = getHRDefendants(hbv, userDisplayName);
        list.setHrDefendantValues(hrDefendants);
        // set the linked hearing id
        list.setLinkedHearingID(hbv.getLinkedHearingID());
        return list;
    }

    /**
     * Method to find all the defendants for the hearing. The defendants will be
     * returned as HRDefendants.
     * 
     * @param hbv
     *            HearingBasicValue
     * @return Collection of HRDefendants
     * @throws HearingRecordException
     */
    private Collection getHRDefendants(HearingBasicValue hbv, String userDisplayName) throws HearingRecordException {
        Collection hrDefendantValues = new ArrayList();
        Collection scheduledHearings = null;
        Collection scheduledHearingBasicValues = null;
        try {
            // get scheduled hearing basic values
            scheduledHearings = scheduledHearingMaintainer.findByHearingId(hbv.getId());
            scheduledHearingBasicValues = scheduledHearingMaintainer.getScheduledHearings(scheduledHearings);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException(HearingRecordConstants.SCHED_HEARING_NOT_FOUND, ex
                    .getMessage(), ex);
            throw hex;
        }
        // use scheduled hearing id to get scheduled hearing defendants basic
        // values
        Collection schedHearingDefendantBasicValues = findSchedHearingDefendantBasicValues(scheduledHearingBasicValues);
        if (schedHearingDefendantBasicValues == null || schedHearingDefendantBasicValues.isEmpty()) {
            // this means that there are no defendants on case so there will
            // be
            // nothing to display just return an empty collection
            log.debug("getHRDefendants returning an empty collection");
            return hrDefendantValues;
        }
        // use the defendantOnCaseIds to get the defendantOnCaseBasicValueList
        Collection defendantOnCaseIDList = findDefendantOnCaseIDs(schedHearingDefendantBasicValues);
        Collection defendantOnCaseBasicValueList = findDefendantOnCaseBasicValues(defendantOnCaseIDList);

        // use defendantOnCaseBasicValueList to get a list of defendantIds and
        // basic values.
        Collection defendantIDList = findDefendantIDs(defendantOnCaseBasicValueList);
        Collection defendantBasicValueList = findDefendantBasicValues(defendantIDList);

        // get DefendantHearingRecords for all the defendants
        Collection defHearingRecords = getDefHearingRecords(defendantOnCaseBasicValueList, hbv, userDisplayName);

        // create HRDefendantValues from the composite parts in defendants and
        // defhearingrecords
        hrDefendantValues = composeHrDefendantValues(defendantBasicValueList, defHearingRecords,
                defendantOnCaseBasicValueList);
        log.debug("getHRDefendants returning hrDefendantValues.size()" + hrDefendantValues.size());
        return hrDefendantValues;
    }

    /**
     * Compose the HrDefendantValues from the defendants and defHearingRecords.
     * These two collections are 'linked' by their DefendantOnCaseIds For each
     * DefendantOnCaseId, get the corresponding DefHearingRecord id and the
     * Defendant details and compose a HRDefendantValue from these elements.
     * 
     * @param defendantBasicValueList
     *            Collection
     * @param defHearingRecordBasicValueList
     *            Collection
     * @param defendantOnCaseBasicValueList
     *            Collection
     * @return Collection of HRDefendantValues
     */
    private Collection composeHrDefendantValues(Collection defendantBasicValueList,
            Collection defHearingRecordBasicValueList, Collection defendantOnCaseBasicValueList) {
        Collection hrDefendantValueList = new ArrayList();
        DefendantOnCaseBasicValue docBasicValue = null;
        DefHearingRecordBasicValue defHearingRecordBasicValue = null;
        DefendantBasicValue defendantBasicValue = null;
        Iterator iterator = defendantOnCaseBasicValueList.iterator();
        while (iterator.hasNext()) {
            // for each defendant on case
            // a) get the DefHearingRecord id
            // b) get the defendant details
            // c) compose the HrDefendantValue from these elements
            log.debug("composeHRDefendantValues : setting defRecordID and defendant details and naming data");
            docBasicValue = (DefendantOnCaseBasicValue) iterator.next();
            // get defHearingRecord corresponding to the defendant on case
            log.debug(" getting the defHearingRecord corresponding to the defendant on case");
            defHearingRecordBasicValue = findDefHearingRecord(defHearingRecordBasicValueList, docBasicValue.getId());

            if (defHearingRecordBasicValue != null)
                log.debug("defHearingRecordBasicValue " + defHearingRecordBasicValue.getId());
            else
                log.debug("defHearingRecordBasicValue is null");

            // get the defendant basic value corresponding to the defendant
            // on the case
            defendantBasicValue = findDefendantBasicValue(defendantBasicValueList, docBasicValue.getDefendantID());
            // compose a HRDefendantValue from the above
            hrDefendantValueList.add(constructHRDefendantValue(defHearingRecordBasicValue, defendantBasicValue));

            // debug info
            if (log.isDebugEnabled()) {
                Iterator internal_iterator = hrDefendantValueList.iterator();
                HRDefendantValue hrDefendantValue = null;
                log.debug(" end of composeHRDefendantValues - checking final results");
                while (internal_iterator.hasNext()) {
                    hrDefendantValue = (HRDefendantValue) internal_iterator.next();
                    log.debug("defHearingRecordID : " + hrDefendantValue.getDefHearingRecordID());
                    log.debug("defendantID : " + hrDefendantValue.getDefendantID());
                }
            }
        }
        return hrDefendantValueList;
    }

    /**
     * Constructs a HRDefendantValue from the composite parts -
     * DefHearingRecordBasicValue and the DefendantBasicValue. The
     * defHearingRecord will not be set when the defendant is a juvenile.
     * 
     * @param defHearingRecordBasicValue
     *            DefHearingRecordBasicValue
     * @param defBasicValue
     *            DefendantBasicValue
     * @return HRDefendantValue the Hearing record defendant value
     */
    private HRDefendantValue constructHRDefendantValue(DefHearingRecordBasicValue defHearingRecordBasicValue,
            DefendantBasicValue defBasicValue) {

        log.debug("constructHRDefendantValue() called");
        HRDefendantValue hrDefendantValue = new HRDefendantValue(defBasicValue.getId());
        hrDefendantValue.setFirstName(defBasicValue.getFirstName());
        hrDefendantValue.setMiddleName(defBasicValue.getMiddleName());
        hrDefendantValue.setSurname(defBasicValue.getSurname());
        hrDefendantValue.setInCustody(defBasicValue.getCurrentPrisonStatus());

        // sometimes defHearingRecord will not exist for a defendant
        // e.g. for juveniles in this case the id is just set to null
        if (defHearingRecordBasicValue == null) {
            log.debug("defHearingRecord will not exist for this defendant");
            hrDefendantValue.setDefHearingRecordID(null);
        } else {
            log.debug("setDefHearingRecordID " + defHearingRecordBasicValue.getId());
            hrDefendantValue.setDefHearingRecordID(defHearingRecordBasicValue.getId());
        }
        try {
            log.debug("Lookup legallyAided value using findByDefendantOnCaseIdValue(" 
                    + defHearingRecordBasicValue.getDefendantOnCaseID() + ")");
            XhbLegalAidOrderBeanHelper2.findByDefendantOnCaseIdValue(defHearingRecordBasicValue.getDefendantOnCaseID());
            log.debug("LegalAidOrder found");
            hrDefendantValue.setLegallyAided(true);
        } catch (XhbLegalAidOrderBeanNotFoundException notFound) {
            hrDefendantValue.setLegallyAided(false);
        }
        return hrDefendantValue;
    }

    /**
     * Iteratore over collection to find the DefendantBasicValue that has a
     * matching defendantID
     * 
     * @param defendantBasicValueList
     *            Collection
     * @param defendantId
     *            Integer
     * @return DefendantBasicValue
     */
    private DefendantBasicValue findDefendantBasicValue(Collection defendantBasicValueList, Integer defendantId) {
        log.debug("findDefendantBasicValue called");
        DefendantBasicValue basicValue = null;

        Iterator iterator = defendantBasicValueList.iterator();
        while (iterator.hasNext()) {
            basicValue = (DefendantBasicValue) iterator.next();
            if (basicValue.getId().equals(defendantId)) {
                break;
            }
        }
        log.debug("findDefendantBasicValue finished");
        return basicValue;
    }

    /**
     * Iterate over collection to find the DefHearingRecord that has a matching
     * defendantOnCaseId.
     * 
     * @param dhrBasicValueList
     *            Collection
     * @param docId
     *            Integer
     * @return DefHearingRecordBasicValue
     */
    private DefHearingRecordBasicValue findDefHearingRecord(Collection dhrBasicValueList, Integer docId) {
        DefHearingRecordBasicValue basicValue = null;
        // iterate through the list and do the work
        Iterator iterator = dhrBasicValueList.iterator();
        while (iterator.hasNext()) {
            basicValue = (DefHearingRecordBasicValue) iterator.next();
            if (basicValue != null && basicValue.getDefendantOnCaseID() != null) {
                if (basicValue.getDefendantOnCaseID().equals(docId)) {
                    break;
                }
            }
        }
        log.debug("findDefHearingRecord finished");
        return basicValue;
    }

    /**
     * Method to return a collection of defHearingRecords for each defendant on
     * the case for the passed in hearing. If the defHearingRecord doesn't exist
     * one will be created.
     * 
     * @param defendantOnCaseBasicValueList
     *            Collection of defendantOnCaseBasicValues.
     * @param hearingBasicValue
     *            HearingBasicValue
     * @return Collection of defHearingRecordBasicValues
     * @throws HearingRecordException
     */
    private Collection getDefHearingRecords(Collection defendantOnCaseBasicValueList,
            HearingBasicValue hearingBasicValue, String userDisplayName) throws HearingRecordException {
        log.debug(" getDefHearingRecords called");
        ArrayList dhrBasicValueList = new ArrayList(); // will be returned
        DefHearingRecordBasicValue dhrBasicValue = null;
        DefendantOnCaseBasicValue docBasicValue = null;
        Iterator iterator = defendantOnCaseBasicValueList.iterator();
        // for each defendant determine if defHearingRecord exists, create if
        // necessary
        while (iterator.hasNext()) {
            docBasicValue = (DefendantOnCaseBasicValue) iterator.next();
            Integer defOnCaseID = docBasicValue.getId();
            try {
                // try to find the defhearing record. If it doesn't yet exist
                // create one.
                DefHearingRecord localDefHR = defHearingRecordMaintainer.findByDefendantOnCaseIDAndHearingID(
                        defOnCaseID, hearingBasicValue.getId());

                dhrBasicValue = defHearingRecordMaintainer.getDefHearingRecordBasicValue(localDefHR);
                log.debug("DefHearingRecord already exists : adding it to the collection" + dhrBasicValue);
                log.debug("DefHearingRecord.getId() " + dhrBasicValue.getId());
            } catch (ObjectNotFoundException e) {
                // this means the record doesn't exist
                // check with ValidationHelper to see if case type allows
                // creation
                if (validationHelper.validateCaseTypes(hearingBasicValue.getCaseID()).booleanValue()) {
                    dhrBasicValue = createNewDefHearingRecord(defOnCaseID, hearingBasicValue.getId(), userDisplayName);
                    log.debug("created new DefHearingRecordBasicValue : " + dhrBasicValue);
                }
            }
            // add the defHearingRecordBasicValue to the returning
            // collection
            dhrBasicValueList.add(dhrBasicValue);
        }
        return dhrBasicValueList;
    }

    /**
     * Accesses maintainer to provide a Hearing Value Object.
     * 
     * @param hearingID
     *            Integer
     * @return HearingBasicValue
     * @throws HearingRecordException
     */
    public HearingBasicValue findHearingBasicValue(Integer hearingID) throws HearingRecordException {
        log.debug("hearingListSummaryValueHelper.findHearingBasicValue(" + hearingID.intValue() + "): exec");
        HearingBasicValue hbv = null;
        try {
            Hearing hearing = hearingMaintainer.findByPK(hearingID);
            hbv = hearingMaintainer.getHearingBasicValue(hearing);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException(HearingRecordConstants.HEARING_NOT_FOUND, ex
                    .getMessage(), ex);
            throw hex;
        }
        log.debug("hearingListSummaryValueHelper.findHearingBasicValue(" + hearingID.intValue() + "): finished.");
        return hbv;
    }

    /**
     * Generates the collection of linked hearings, that share the
     * linkedHearingID argument.
     * 
     * @param linkedHearingID
     *            Integer
     * @return Collection of Hearing Value Objects
     * @throws HearingRecordException
     */
    public Collection findAllLinkedHearings(Integer linkedHearingID) {
        log.debug("hearingListSummaryValueHelper.findAllLinkedHearings(" + linkedHearingID.intValue() + "): exec");
        Collection linked_Hearings = new ArrayList();
        Collection locals = hearingMaintainer.findByLinkedHearingId(linkedHearingID);
        Iterator localsIt = locals.iterator();
        while (localsIt.hasNext()) {
            Hearing hearing = (Hearing) localsIt.next();
            HearingBasicValue hbv = hearingMaintainer.getHearingBasicValue(hearing);
            log.debug("\n\n HearingListSummaryValueHelper.findAllLinkedHearings() - adding hearing with hearingID = "
                    + hbv.getId());
            linked_Hearings.add(hbv);
        }
        log.debug("hearingListSummaryValueHelper.findAllLinkedHearings(" + linkedHearingID.intValue() + "): finished");
        return linked_Hearings;
    }

    /**
     * Get the scheduled hearing defendants from a list of
     * ScheduledHearingBasicValues iterate over the basic value list get a
     * collection of scheduleHearingDefendant (locals) from the maintainer for
     * each collection of scheduleHearingDefendant locals returned iterate over
     * and add each element to the returned list of locals
     * 
     * @param scheduledHearingBasicValueList
     *            as a Collection
     * @return Collection of SchedHearingDefendantBasicValue
     * @throws HearingRecordException
     */
    private Collection findSchedHearingDefendantBasicValues(Collection scheduledHearingBasicValueList)
            throws HearingRecordException {
        log.debug(".......number of schedHearingBasicValues = " + scheduledHearingBasicValueList.size());
        ArrayList schedHearingDefendantBasicValues = new ArrayList(); // will
        // be
        // returned
        Iterator iterator = scheduledHearingBasicValueList.iterator();
        while (iterator.hasNext()) {
            ScheduledHearingBasicValue basicValue = (ScheduledHearingBasicValue) iterator.next();
            try {
                // find all DefendantScheduledHearingValues for each
                // scheduledhearing
                Collection schedHearingDefendants = schedHearingDefendantMaintainer.findByScheduledHearingId(basicValue
                        .getId());
                log.debug(".......number of schedHearingDefendants = " + schedHearingDefendants.size());
                Iterator shdIt = schedHearingDefendants.iterator();
                while (shdIt.hasNext()) {
                    SchedHearingDefendant shdef = (SchedHearingDefendant) shdIt.next();
                    
                    //PR5673
                    try {
                        XhbDefendantOnCase defOnCase =
                            XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(shdef.getDefOnCaseID());
                        if (defOnCase.getObsInd() != null
                                && defOnCase.getObsInd().equals("Y")) {
                            log.debug("Defendant on case marked obsolete");
                            continue;
                        }
                    } catch (XhbDefendantOnCaseBeanNotFoundException notFound) {
                        log.debug("Failed to find defendant on case");
                        continue;
                    }
                    
                    SchedHearingDefendantBasicValue shdefBasic = schedHearingDefendantMaintainer
                            .getSchedHearingDefendantBasicValue(shdef);
                    schedHearingDefendantBasicValues.add(shdefBasic);
                    log.debug(".......added schedHearingdDefendantBasicValue to collection");
                }
            } catch (ObjectNotFoundException ex) {
                CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
                HearingRecordException hex = new HearingRecordException(
                        HearingRecordConstants.SCHED_HEAR_DEF_NOT_FOUND, ex.getMessage(), ex);
                throw hex;
            }
        }
        return schedHearingDefendantBasicValues;
    }

    /**
     * Method to take out the defendantOnCaseIds from a collection of
     * ScheduledHearingDefendantBasicValues. They will be returned in a
     * collection that will only contain the unique ids.
     * 
     * @param defBasics
     *            collection of SchedHearingDefendantBasicValue's.
     * @return Collection of defendantOnCaseIds
     */
    private Collection findDefendantOnCaseIDs(Collection defBasics) {
        log.debug("findDefendantOnCaseIDs  called");
        ArrayList defendantOnCaseIDs = new ArrayList();// will be returned

        Iterator defBasicsIt = defBasics.iterator();
        while (defBasicsIt.hasNext()) {
            SchedHearingDefendantBasicValue schedDef = (SchedHearingDefendantBasicValue) defBasicsIt.next();
            Integer defOnCaseID = schedDef.getDefendantOnCaseID();
            // check if the defendantOnCase already exist in the list, if
            // not then
            // add it to the returning Arraylist.
            if (!defendantOnCaseIDs.contains(defOnCaseID)) {
                defendantOnCaseIDs.add(defOnCaseID);
                log.debug("added defendantOnCaseID of " + defOnCaseID);
            }
        }
        log.debug("findDefendantOnCaseIDs finished");
        return defendantOnCaseIDs;
    }

    /**
     * Get a list of DefendantOnCaseBasicValues from a list of
     * defendantOnCaseIds.
     * 
     * @param defendantOnCaseIDs
     *            in a Collection
     * @return Collection of DefendantOnCaseBasicValues
     * @throws HearingRecordException
     */
    private Collection findDefendantOnCaseBasicValues(Collection defendantOnCaseIDs) throws HearingRecordException {
        log.debug("findDefendantOnCaseBasicValues called");
        Collection defendantOnCases = new ArrayList();

        Iterator defOnCaseIDIt = defendantOnCaseIDs.iterator();
        while (defOnCaseIDIt.hasNext()) {
            Integer id = (Integer) defOnCaseIDIt.next();
            log.debug("finding defendantOnCase for primary key = " + id);
            try {
                DefendantOnCase def = defendantOnCaseMaintainer.findByPrimaryKey(id);
                
                //PR5673
                try {
                    // findByDefendantAndCase only returns values that are not obsolete.
                    // Use this to check that the defendant has not had obs_ind set to "Y".
                    XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(
                            def.getDefendantId(), def.getCaseId());
                } catch (XhbDefendantOnCaseBeanNotFoundException notFound) {
                    log.debug("discarding obsolete defendant for " + id);
                    continue;
                }
                
                defendantOnCases.add(def);
                log.debug("successfully added defendant for " + id);
            } catch (ObjectNotFoundException ex) {
                CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
                HearingRecordException hex = new HearingRecordException(HearingRecordConstants.DEF_ON_CASE_NOT_FOUND,
                        ex.getMessage(), ex);
                throw hex;
            }
        }
        log.debug("calling defendantOnCaseMaintainer.getDefendantOnCaseBasicValues(..)");
        Collection defendantOnCaseBasicValues = defendantOnCaseMaintainer
                .getDefendantOnCaseBasicValues(defendantOnCases);
        log.debug("findDefendantOnCaseBasicValues returning  collection.size() = " + defendantOnCaseBasicValues.size());
        return defendantOnCaseBasicValues;
    }

    /**
     * Method to bring out all the defendant ids from a collection of
     * DefendantOnCaseBasicValues
     * 
     * @param defendantOnCaseBasicValues
     *            a Collection
     * @return Collection of defendantIds
     */
    private Collection findDefendantIDs(Collection defendantOnCaseBasicValues) {
        log.debug("findDefendantIDs called");
        ArrayList defendantIDs = new ArrayList(); // will be returned.
        Iterator defOnCaseBasicIt = defendantOnCaseBasicValues.iterator();
        while (defOnCaseBasicIt.hasNext()) {
            DefendantOnCaseBasicValue def = (DefendantOnCaseBasicValue) defOnCaseBasicIt.next();
            log.debug("added defendantID of " + def.getDefendantID());
            defendantIDs.add(def.getDefendantID());
        }
        log.debug("findDefendantIDs() returned");
        return defendantIDs;
    }

    /**
     * Method to find all the defendants for the defendantIds passed in.
     * 
     * @param defendantIDs
     *            as a Collection
     * @return Collection of DefendantBasicValues
     * @throws HearingRecordException
     */
    private Collection findDefendantBasicValues(Collection defendantIDs) throws HearingRecordException {
        log.debug("findDefendantBasicValues()");
        ArrayList defendantBasicValues = new ArrayList(); // will be
        // returned
        Collection defendants = new ArrayList();

        Iterator defIDIt = defendantIDs.iterator();
        while (defIDIt.hasNext()) {
            Integer id = (Integer) defIDIt.next();
            try {
                Defendant def = defendantMaintainer.findByPrimaryKey(id);
                defendants.add(def);
                log.debug("added defendant for id " + id);
            } catch (ObjectNotFoundException ex) {
                CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
                HearingRecordException hex = new HearingRecordException(HearingRecordConstants.DEFENDANT_NOT_FOUND, ex
                        .getMessage(), ex);
                throw hex;
            }
        }
        log.debug("getting defendantBasicValues from the defendantMaintainer ...");
        defendantBasicValues = (ArrayList) defendantMaintainer.getDefendantBasicValues(defendants);
        log.debug("findDefendantBasicValues returning collection.size() = " + defendantBasicValues.size());
        return defendantBasicValues;
    }

    /**
     * This will populate the passed in HearingListSummaryValue with case
     * information
     * 
     * @param summaryValue
     *            HearingListSummaryValue
     * @param hbv
     *            HearingBasicValue
     * @return HearingListSummaryValue populated with case information
     * @throws HearingRecordException
     */
    private HearingListSummaryValue populateCaseInfo(HearingListSummaryValue summaryValue, HearingBasicValue hbv)
            throws HearingRecordException {
        // get a case value object for case info.
        try {
            Case caze = caseMaintainer.findByPrimaryKey(hbv.getCaseID());
            CaseBasicValue caseBV = caseMaintainer.getCaseBasicValue(caze);
            summaryValue.setCaseID(caseBV.getId());
            summaryValue.setCaseType(caseBV.getCaseType());
            summaryValue.setCaseSubType(caseBV.getCaseSubType());
            summaryValue.setCaseNumber(caseBV.getCaseNumber());
            return summaryValue;
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException(HearingRecordConstants.CASE_NOT_FOUND, ex
                    .getMessage(), ex);
            throw hex;
        }
    }

    /**
     * This will populate the passed in HearingListSummaryValue with hearing
     * information
     * 
     * @param summaryValue
     *            HearingListSummaryValue
     * @param hearingBasicValue
     *            HearingBasicValue
     * @return HearingListSummaryValue populated with hearing information
     * @throws HearingRecordException
     */
    private HearingListSummaryValue populateHearingInfo(HearingListSummaryValue summaryValue,
            HearingBasicValue hearingBasicValue) throws HearingRecordException {
        // get a refhearingtype value object for necessary info
        try {
            RefHearingType refHearingType = refHearingTypeMaintainer.findByPrimaryKey(hearingBasicValue
                    .getRefHearingTypeID());
            RefHearingTypeBasicValue refHearingTypeBasicValue = refHearingTypeMaintainer.getBasicValue(refHearingType);
            summaryValue.setHearingID(hearingBasicValue.getId());
            summaryValue.setRefHearingTypeID(refHearingTypeBasicValue.getId());
            summaryValue.setRefHearingTypeCode(refHearingTypeBasicValue.getHearingTypeCode());
            summaryValue.setRefHearingTypeDesc(refHearingTypeBasicValue.getHearingTypeDesc());
            return summaryValue;
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException(HearingRecordConstants.HEARING_NOT_FOUND, ex
                    .getMessage(), ex);
            throw hex;
        }
    }

    /**
     * Triggers creation of a new defhearingrecord entity with the two values
     * supplied. Most of the values will not be set but will be left to the
     * user.
     * 
     * @param defendantOnCaseID
     *            Integer
     * @param hearingID
     *            Integer
     * @return DefHearingRecordBasicValue the newly created value.
     * @throws HearingRecordException
     */
    private DefHearingRecordBasicValue createNewDefHearingRecord(Integer defendantOnCaseID, Integer hearingID, String userDisplayName)
            throws HearingRecordException {
        log.debug("createNewDefHearingRecord with : " + defendantOnCaseID + " : " + hearingID);
        DefHearingRecordBasicValue dhrv = new DefHearingRecordBasicValue();
        dhrv.setDefendantOnCaseID(defendantOnCaseID);
        dhrv.setHearingID(hearingID);
        dhrv.setIsHraApplication("N");// HRA application is not required, will
        // always be set to 'N'
        try {
            // create the defHearingRecord with the CMR to hearing.
            DefHearingRecord local = (DefHearingRecord) defHearingRecordMaintainer.create(dhrv, userDisplayName);
            // HearingMaintainer hearingMaintainer = new
            // HearingMaintainer();
            Hearing hearing = hearingMaintainer.findByPK(hearingID);
            local.setHearing(hearing);
            dhrv = defHearingRecordMaintainer.getDefHearingRecordBasicValue(local);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException(
                    HearingRecordConstants.DEF_HEAR_REC_COULD_NOT_BE_CREATED, ex.getMessage(), ex);
            throw hex;
        }
        return dhrv;
    }

    /*
     * ---------------------------------private maintainer
     * getters-------------------
     */
    private HearingMaintainer getHearingMaintainer() {
        return new HearingMaintainer();
    }

    private RefHearingTypeMaintainer getRefHearingTypeMaintainer() {
        return new RefHearingTypeMaintainer();
    }

    private ScheduledHearingMaintainer getScheduledHearingMaintainer() {
        return new ScheduledHearingMaintainer();
    }

    private SchedHearingDefendantMaintainer getSchedHearingDefendantMaintainer() {
        return new SchedHearingDefendantMaintainer();
    }

    private DefendantOnCaseMaintainer getDefendantOnCaseMaintainer() {
        return new DefendantOnCaseMaintainer();
    }

    private DefendantMaintainer getDefendantMaintainer() {
        return new DefendantMaintainer();
    }

    private CaseMaintainer getCaseMaintainer() {
        return new CaseMaintainer();
    }

    private DefHearingRecordMaintainer getDefHearingRecordMaintainer() {
        return new DefHearingRecordMaintainer();
    }

    private HearingRecordStatusHelper getHearingRecordStatusHelper() {
        return new HearingRecordStatusHelper();
    }

    private HearingRecordValidationHelper getHearingRecordValidationHelper() {
        return new HearingRecordValidationHelper();
    }
}