package uk.gov.courtservice.xhibit.business.database.query.counsel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.CourtClerkValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.LegalRepSignInValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseHeaderValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.UsherValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: CounselDataHolder
 * </p>
 * 
 * <p>
 * Description: Class to temporarily hold all PartyOnCase values per scheduled
 * hearing.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford, Marie Holmberg
 * @version 1.0
 */
public class CounselDataHolder {

    // The header value that is common for all PartyOnCaseValues
    private PartyOnCaseHeaderValue pocHeaderValue;

    // The PartyOnCaseValue that only holds Objector data
    private PartyOnCaseValue objector = new PartyOnCaseValue();

    // The PartyOnCaseValue that only holds Respondant data
    private PartyOnCaseValue respondent = new PartyOnCaseValue();

    // The PartyOnCaseValue that only holds Prosecutor data
    private PartyOnCaseValue prosecutor = new PartyOnCaseValue();

    // Name of the court clerk signed in for the scheduled hearing stored as
    // key with the value
    private HashMap courtClerkMap = new HashMap();

    // Name of the usher signed in for the scheduled hearing stored as key
    // with the value
    private HashMap usherMap = new HashMap();

    // Instant cache to hold the schedHearingDefId and related
    // PartyOnCaseValue
    private HashMap defendantData = new HashMap();

    // HashSet that holds the objectors for a scheduledhearing. Will only be
    // used
    // to make sure the same objector is not added to the same case more
    // than ones.
    // The objector information will still be held in the PartyOnCaseValue
    // for Objectors.
    private HashSet objectorSet = new HashSet();

    // Same as the objectorMap but for respondent
    private HashSet respondentSet = new HashSet();

    // Same as the objectorMap but for proseutor
    private HashSet prosecutorSet = new HashSet();

    // Same as the objectorMap but for defence/appellants
    private HashSet defenceSet = new HashSet();

    // The logger for this class.
    private static final Logger log = CSServices.getLogger(CounselDataHolder.class);

    /**
     * Constructor that will set the HeaderInformation even for the objector,
     * respondent and the prosecutor.
     * 
     * @param pocHeaderValue
     *            value object holding common data for all PartyOnCase Values
     *            per scheduled hearing.
     */
    public CounselDataHolder(PartyOnCaseHeaderValue pocHeaderValue) {
        this.pocHeaderValue = pocHeaderValue;
        this.setUp();
    }

    /**
     * All PartyOnCaseValues objector, respondent and prosecutor will be set up
     * with the appropriate header values depending on case type
     */
    private void setUp() {
        log.debug("CounselDataHolder.setUp() called");
        if (this.pocHeaderValue.getCaseType().equalsIgnoreCase(CounselConstants.APPEAL_CASE_TYPE)) {
            this.respondent = setHeaderInformation();
            this.respondent.setPartyRole(CounselConstants.PARTY_ROLE_RESPONDENT);

            // only set the Objector for the misc appeals.
            if (this.pocHeaderValue.getCaseSubType().equalsIgnoreCase(CounselConstants.CASE_SUB_TYPE_MISC_APPEAL)) {
                this.objector = setHeaderInformation();
                this.objector.setPartyRole(CounselConstants.PARTY_ROLE_OBJECTOR);
            }
        } else {
            this.prosecutor = setHeaderInformation();
            this.prosecutor.setPartyRole(CounselConstants.PARTY_ROLE_PROSECUTION);
        }
        log.debug("CounselDataHolder.setUp() finished");
    }

    /**
     * This method will create a new PartyOnCaseValue (that will be returned)
     * and set the common header information for the PartyOnCaseValue.
     * 
     * @return PartyOnCaseValue
     */
    protected PartyOnCaseValue setHeaderInformation() {
        log.debug("CounselDataHolder.setHeaderInformation() called");

        PartyOnCaseValue value = new PartyOnCaseValue();
        value.setScheduledHearingId(pocHeaderValue.getScheduledHearingId());
        value.setCaseNumber(pocHeaderValue.getCaseNumber());
        value.setCaseType(pocHeaderValue.getCaseType());
        value.setCaseTitle(pocHeaderValue.getCaseTitle());
        value.setCourtRoomId(pocHeaderValue.getCourtRoomId());
        value.setTimeListed(pocHeaderValue.getTimeListed());
        value.setCourtRoomDescription(pocHeaderValue.getCourtRoomDescription());
        value.setCourtRoomName(pocHeaderValue.getCourtRoomName());
        value.setCourtRoomDisplayName(pocHeaderValue.getCourtRoomDisplayName());
        value.setIsFloating(pocHeaderValue.getIsFloating());
        value.setHearingTypeCode(pocHeaderValue.getHearingTypeCode());
        value.setHearingTypeDesc(pocHeaderValue.getHearingTypeDesc());
        value.setCrestCourtRoomNumber(pocHeaderValue.getCrestCourtRoomNumber());
        value.setCaseSubType(pocHeaderValue.getCaseSubType());

        // PRE00180 - need to set the new values also
        value.setCourtSiteCode(pocHeaderValue.getCourtSiteCode());
        value.setSittingSequenceNo(pocHeaderValue.getSittingSequenceNo());
        value.setShSequenceNo(pocHeaderValue.getShSequenceNo());

        value.setCourtSiteShortName(pocHeaderValue.getCourtSiteShortName());

        log.debug("CounselDataHolder.setHeaderInformation() finished. " + "Return PartyOnCase :" + value);
        return value;
    }

    /**
     * This method will return all PartyOnCaseValues that are being held in this
     * class. For an appeal objectors, respondants and appeallants. For all
     * other cases prosecution and defendants.
     * 
     * The PartyOnCaseValue also holds the legal representatives.
     * 
     * The PartyOnCaseValues for objectors, respondents and prosecutors holds a
     * Collection of all defendants for a particular scheduledHearingId. A case
     * can have more than one defendant.
     * 
     * @param removeRecordsWithNoLegReps
     *            boolean determine whether to remove records with no legal
     *            representatives.
     * @return ArrayList - the entire list of the PartyOnCases for this
     *         scheduled hearing id.
     */
    public ArrayList getPartyOnCaseList(boolean removeRecordsWithNoLegReps) {
        log.debug("CounselDataHolder.getPartyOnCaseList() called");

        ArrayList partyList = new ArrayList();
        ArrayList defendantList = new ArrayList();

        // get the court clerks
        ArrayList courtClerkList = this.setCourtClerkList();

        // get the ushers
        ArrayList usherList = this.setUsherList();

        // Get the defendant data and the PartyOnCaseValue from the Map
        Set set = defendantData.keySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Integer key = (Integer) it.next();
            PartyOnCaseValue pocDef = (PartyOnCaseValue) defendantData.get(key);

            // set the court clerks and ushers.
            pocDef.setCourtClerk(courtClerkList);
            pocDef.setUsher(usherList);

            /*
             * We dont want to add the defendant POC if they do not have legal
             * reps and the the removeRecordsWithNoLegReps is true. i.e : - When
             * searhing for defendants, we want to include all defs, even if
             * they have no legal reps. - When searhing for counsel, we do not
             * want to add records if they do not have legal reps
             */
            if (!removeRecordsWithNoLegReps || pocDef.getRepresentatives().size() > 0) {
                // remove any duplicates
                if (pocDef.getRepresentatives() != null && pocDef.getRepresentatives().size() > 0) {
                    pocDef.setRepresentatives(removeDuplicates(pocDef.getRepresentatives()));
                }
                // add the defendantPOCvalue to the returning array list
                partyList.add(pocDef);
            }

            // populate the defendantList with all the defendants
            if (pocDef.getDefendants() != null && pocDef.getDefendants().size() > 0) {
                Iterator it2 = pocDef.getDefendants().iterator();
                while (it2.hasNext()) {
                    DefendantValue value = (DefendantValue) it2.next();
                    defendantList.add(value);
                }
            }
        }

        // set the defendant collection and if Appeal case populate objectors
        // and respondants
        // else set the prosecutor list.
        if (this.pocHeaderValue.getCaseType().equalsIgnoreCase(CounselConstants.APPEAL_CASE_TYPE)) {
            if (!removeRecordsWithNoLegReps
                    || (objector != null && objector.getRepresentatives() != null && objector.getRepresentatives()
                            .size() > 0)) {
                // only add objectors for misc appeals.
                if (this.pocHeaderValue.getCaseSubType().equalsIgnoreCase(CounselConstants.CASE_SUB_TYPE_MISC_APPEAL)) {
                    this.objector.setDefendants(defendantList);
                    // set the court clerk and usher information.
                    this.objector.setCourtClerk(courtClerkList);
                    this.objector.setUsher(usherList);
                    partyList.add(this.objector);
                }
            }

            if (!removeRecordsWithNoLegReps || respondent.getRepresentatives().size() > 0) {
                this.respondent.setDefendants(defendantList);
                // set the court clerk and usher information.
                this.respondent.setCourtClerk(courtClerkList);
                this.respondent.setUsher(usherList);
                partyList.add(this.respondent);
            }
        } else {
            if (!removeRecordsWithNoLegReps || prosecutor.getRepresentatives().size() > 0) {
                this.prosecutor.setDefendants(defendantList);
                this.prosecutor.setCourtClerk(courtClerkList);
                this.prosecutor.setUsher(usherList);
                partyList.add(this.prosecutor);
            }
        }
        log.debug("CounselDataHolder.getPartyOnCaseList() finished");
        return partyList;
    }

    /**
     * Method to remove any duplicates in the representation collection. It will
     * check for the shLegRepId and if a representation with that id has been
     * added then the next one will not be included in the returning collection.
     * 
     * @param inCol
     *            Collection of representations that includes duplicates
     * @return Collection excluding duplicated representation
     */
    private Collection removeDuplicates(Collection inCol) {
        log.debug("CounselDataHolder.removeDuplicates() called");
        // if we have a collection loop through and check/remove if the
        // representation is
        // already there.
        if (inCol == null || inCol.size() == 0) {
            return inCol;
        }

        HashSet set = new HashSet();
        ArrayList newList = new ArrayList();

        Iterator it = inCol.iterator();
        while (it.hasNext()) {
            LegalRepSignInValue value = (LegalRepSignInValue) it.next();
            if (!set.contains(value.getShLegRepId())) {
                set.add(value.getShLegRepId());
                newList.add(value);
            }
        }
        log.debug("CounselDataHolder.removeDuplicates() finished");
        return newList;
    }

    /**
     * Method to collect all court clerks for this hearing scheduled.
     * 
     * @return ArrayList of CourtClerkValues.
     */
    private ArrayList setCourtClerkList() {
        ArrayList courtClerkList = new ArrayList();
        // Populate an array list with all the court clerks
        Set ccSet = courtClerkMap.keySet();
        Iterator ccIt = ccSet.iterator();
        while (ccIt.hasNext()) {
            String ccKey = (String) ccIt.next();
            CourtClerkValue ccValue = (CourtClerkValue) courtClerkMap.get(ccKey);
            courtClerkList.add(ccValue);
        }
        return courtClerkList;
    }

    /**
     * Method to collect all ushers for this hearing scheduled.
     * 
     * @return ArrayList
     */
    private ArrayList setUsherList() {
        ArrayList usherList = new ArrayList();
        // Populate an array list with all the ushers.
        Set usherSet = usherMap.keySet();
        Iterator usherIt = usherSet.iterator();
        while (usherIt.hasNext()) {
            String usherKey = (String) usherIt.next();
            UsherValue usherValue = (UsherValue) usherMap.get(usherKey);
            usherList.add(usherValue);
        }
        return usherList;
    }

    // Getters and setters
    public HashMap getDefendantData() {
        return defendantData;
    }

    public PartyOnCaseValue getObjector() {
        return objector;
    }

    public PartyOnCaseValue getProsecutor() {
        return prosecutor;
    }

    public PartyOnCaseValue getRespondent() {
        return respondent;
    }

    public void setDefendantData(HashMap defendantData) {
        this.defendantData = defendantData;
    }

    public void setObjector(PartyOnCaseValue objector) {
        this.objector = objector;
    }

    public void setProsecutor(PartyOnCaseValue prosecutor) {
        this.prosecutor = prosecutor;
    }

    public void setRespondent(PartyOnCaseValue respondent) {
        this.respondent = respondent;
    }

    public void setCourtClerkMap(HashMap courtClerkMap) {
        this.courtClerkMap = courtClerkMap;
    }

    public HashMap getCourtClerkMap() {
        return courtClerkMap;
    }

    public void setUsherMap(HashMap usherMap) {
        this.usherMap = usherMap;
    }

    public HashMap getUsherMap() {
        return usherMap;
    }

    public void setObjectorSet(HashSet objectorSet) {
        this.objectorSet = objectorSet;
    }

    public void setRespondentSet(HashSet respondentSet) {
        this.respondentSet = respondentSet;
    }

    public void setProsecutorSet(HashSet prosecutorSet) {
        this.prosecutorSet = prosecutorSet;
    }

    public void setDefenceSet(HashSet defenceSet) {
        this.defenceSet = defenceSet;
    }

    public HashSet getObjectorSet() {
        return objectorSet;
    }

    public HashSet getRespondentSet() {
        return respondentSet;
    }

    public HashSet getProsecutorSet() {
        return prosecutorSet;
    }

    public HashSet getDefenceSet() {
        return defenceSet;
    }
}
