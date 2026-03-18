package uk.gov.courtservice.xhibit.business.vos.services.todaysschedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;

/**
 * <p>
 * Title: ScheduledHearingValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Babad
 * 
 * <Change History/>
 * 
 * <P>
 * 10/02/03 - JB - Re-worked for new today's schedule
 * </P>
 * 64,52033 20-02-2003 AW Daley getDefendants modified to make a call to a new
 * method buildPersonsFullNames. This method does not add first, middle or
 * surnames to the full name if null.
 * <P>
 * 12/03/03 - JB - Added collection of DefendantOnCaseBasicValue VOs
 * </P>
 * <P>
 * 13/03/03 - JB - Added facade method... getCourtSiteId
 * </P>
 */

public class ScheduledHearingValue extends CSAbstractValue {
	private static final long serialVersionUID = 5762505259397001030L;
    private ScheduledHearingBasicValue scheduledHearingBasicValue = new ScheduledHearingBasicValue();

    private CaseBasicValue caseBasicValue;

    private RefHearingTypeBasicValue refHearingTypeBasicValue;

    private CourtRoomBasicValue courtRoomBasicValue;

    private TreeMap<Integer, DefendantOnCaseBasicValue> defendantOnCaseBasicValues = 
        new TreeMap<Integer, DefendantOnCaseBasicValue>();

    private TreeMap<Integer, DefendantBasicValue> defendantsOnCase = 
        new TreeMap<Integer,DefendantBasicValue>();

    private String judge;

    private Integer sittingSequenceNo;

    private Boolean floating;

    private Integer refJudgeId;

    private Calendar hearingListStartDate;

    private String courtSiteShortName;

    private String crestCourtId;

    /*
     * These two fields are used by Public Displays only. They will not be
     * populated for Today's Schedule and will only be set when passing through
     * the Public PublicDisplayController to the Public Display screens
     */
    private String currentStatus;

    private Calendar currentStatusTime;

    /**
     * <p>
     * Title: SchedHearingCompositeValue
     * </p>
     * <p>
     * Description:
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: Electronic Data Systems
     * </p>
     * 
     * @author Joseph Babad
     * 
     * <Change History/>
     * 
     * <P>
     * 19/02/03 - JB - Created.
     * </P>
     */
    public ScheduledHearingValue() {
        // empty
    }

    // Start - Vector accessors
    // -------------------------------------------------

    public ScheduledHearingBasicValue getScheduledHearingBasicValue() {
        return scheduledHearingBasicValue;
    }

    public Collection getDefendantsOnCase() {
        return defendantsOnCase.values();
    }

    public CaseBasicValue getCaseBasicValue() {
        return caseBasicValue;
    }

    public CourtRoomBasicValue getCourtRoomBasicValue() {
        return courtRoomBasicValue;
    }

    public RefHearingTypeBasicValue getRefHearingTypeBasicValue() {
        return refHearingTypeBasicValue;
    }

    public Collection getDefendantOnCaseBasicValues() {
        return defendantOnCaseBasicValues.values();
    }

    // End - Vector accessors
    // ---------------------------------------------------

    // Start - Vector mutators
    // --------------------------------------------------

    public void setScheduledHearingBasicValue(ScheduledHearingBasicValue val) {
        scheduledHearingBasicValue = val;
    }

    public void setDefendantsOnCase(Collection val) {
        for (Iterator it = val.iterator(); it.hasNext();) {
            addDefendantBasicValue((DefendantBasicValue) it.next());
        }
    }

    public void addDefendantBasicValue(DefendantBasicValue val) {
        defendantsOnCase.put(val.getId(), val);
    }

    public void setCaseBasicValue(CaseBasicValue val) {
        caseBasicValue = val;
    }

    public void setCourtRoomValue(CourtRoomBasicValue val) {
        courtRoomBasicValue = val;
    }

    public void setDefendantOnCaseBasicValues(Collection val) {
        for (Iterator it = val.iterator(); it.hasNext();) {
            addDefendantOnCaseBasicValue((DefendantOnCaseBasicValue) it.next());
        }
    }

    public void addDefendantOnCaseBasicValue(DefendantOnCaseBasicValue val) {
        defendantOnCaseBasicValues.put(val.getDefendantID(), val);
    }

    public void setRefHearingTypeBasicValue(RefHearingTypeBasicValue val) {
        refHearingTypeBasicValue = val;
    }

    // End - Vector mutators
    // ----------------------------------------------------

    // Start - Scalar accessors
    // -------------------------------------------------

    public Integer getScheduledHearingId() {
        return scheduledHearingBasicValue.getId();
    }

    public Date getNotBeforeTime() {
        return scheduledHearingBasicValue.getNotBeforeTime();
    }

    public Integer getHearingProgress() {
        return scheduledHearingBasicValue.getHearingProgress();
    }

    public String getMovedFromCourtRoomName() {
        return scheduledHearingBasicValue.getMovedFrom();
    }

    public Integer getSequenceNo() {
        return scheduledHearingBasicValue.getSequenceNo();
    }

    public Integer getLinkedSHID() {
        return scheduledHearingBasicValue.getLinkedSHID();
    }

    public String getCourtRoomDisplayName() {
        return courtRoomBasicValue.getDisplayName();
    }

    public String getCourtRoomName() {
        return courtRoomBasicValue.getCourtRoomName();
    }

    public Integer getCrestCourtRoomNo() {
        return courtRoomBasicValue.getCrestCourtRoomNo();
    }

    public Integer getCourtRoomId() {
        return courtRoomBasicValue.getId();
    }

    public Integer getCourtSiteId() {
        if (this.courtRoomBasicValue != null)
            return this.courtRoomBasicValue.getCourtSiteId();
        else
            return new Integer(-1);
    }

    public String getHearingType() {
        return refHearingTypeBasicValue.getHearingTypeCode();
    }

    public String getHearingTypeDesc() {
        return refHearingTypeBasicValue.getHearingTypeDesc();
    }

    public Integer getCaseId() {
        return caseBasicValue.getId();
    }

    public Integer getCaseNumber() {
        return caseBasicValue.getCaseNumber();
    }

    public String getCaseSubType() {
        return caseBasicValue.getCaseSubType();
    }

    public String getCaseTitle() {
        return caseBasicValue.getCaseTitle();
    }

    public String getCaseType() {
        return caseBasicValue.getCaseType();
    }

    public String getJudge() {
        return judge;
    }

    public Integer getRefJudgeId() {
        return refJudgeId;
    }

    public Integer getSittingSequenceNo() {
        return sittingSequenceNo;
    }

    public Boolean getIsFloating() {
        return floating;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public Calendar getCurrentStatusTime() {
        return currentStatusTime;
    }

    public String getCourtSiteShortName() {
        return courtSiteShortName;
    }

    public void setCourtSiteShortName(String courtSiteShortName) {
        this.courtSiteShortName = courtSiteShortName;
    }

    public String getCrestCourtId() {
        return crestCourtId;
    }

    public void setCrestCourtId(String crestCourtId) {
        this.crestCourtId = crestCourtId;
    }

    /**
     * Return a list of defendants with names in the following order [firstname,
     * middlename, surname]
     * 
     * @return String array of defendants
     */
    public String[] getDefendants() {

        try {
            CaseBasicValue caseBV = getCaseBasicValue();
            String caseType = caseBV.getCaseType();
            if ("U".equalsIgnoreCase(caseType) || "B".equalsIgnoreCase(caseType)) {
                return new String[] { caseBV.getCaseTitle() };
            } else {
                DefendantBasicValue dbv = null;
                Iterator<DefendantBasicValue> it = this.defendantsOnCase.values().iterator();
                List<DefendantBasicValue> defList = new ArrayList<DefendantBasicValue>();
                while (it.hasNext()) {
                    dbv = it.next();
                    defList.add(dbv);
                }

                // sort defendants by surname and first name
                Sorter.sort(defList, new String[] { "surname", "firstName" });

                // building string array of sorted defendants.
                Iterator<DefendantBasicValue> defIt = defList.iterator();
                String[] defendants = new String[this.defendantsOnCase.size()];
                int i = 0;
                while (defIt.hasNext()) {
                    dbv = defIt.next();
                    String name = buildPersonsFullName(dbv.getFirstName(), dbv.getMiddleName(), dbv.getSurname());
                    defendants[i] = name;
                    i++;
                }
                return defendants;
            }
        } catch (NullPointerException e) {
            return new String[] {};
        }

    }

    /**
     * Return a list of defendants with names in the following order [surname,
     * middlename, firstname]
     * 
     * @return String array of defendants
     */
    public String[] getDefendantsBySurname() {

        try {
            CaseBasicValue caseBV = getCaseBasicValue();
            String caseType = caseBV.getCaseType();
            if ("U".equalsIgnoreCase(caseType) || "B".equalsIgnoreCase(caseType)) {
                return new String[] { caseBV.getCaseTitle() };
            } else {

                Iterator<DefendantBasicValue> it = this.defendantsOnCase.values().iterator();
                String[] defendants = new String[this.defendantsOnCase.size()];
                int i = 0;
                while (it.hasNext()) {
                    DefendantBasicValue dbv = it.next();
                    String name = buildPersonsFullName(dbv.getSurname(), dbv.getFirstName(), dbv.getMiddleName());
                    defendants[i] = name;
                    i++;
                }
                return defendants;
            }
        } catch (NullPointerException e) {
            return new String[] {};
        }

    }

    public DefendantOnCaseBasicValue getDefendantOnCaseBasicValue(Integer defId) {
        return defendantOnCaseBasicValues.get(defId);
    }

    public Calendar getHearingListStartDate() {
        return hearingListStartDate;
    }

    // End - Scalar accessors
    // ------------------------------------------------------

    // Start - Scalar mutators
    // ----------------------------------------------------

    public void setJudge(String val) {
        judge = val;
    }

    public void setRefJudgeId(Integer val) {
        refJudgeId = val;
    }

    public void setSittingSequenceNo(Integer val) {
        sittingSequenceNo = val;
    }

    public void setIsFloating(Boolean val) {
        floating = val;
    }

    public void setCurrentStatus(String val) {
        currentStatus = val;
    }

    public void setCurrentStatusTime(Calendar val) {
        currentStatusTime = val;
    }

    public void setHearingListStartDate(Calendar hearingListStartDate) {
        this.hearingListStartDate = hearingListStartDate;
    }

    // End - Scalar mutators
    // ------------------------------------------------------

    // Start - utility methods
    // ----------------------------------------------------

    /**
     * Method to return a string containing the defendants name if called from
     * getDefendants(firstname, middlename,surname) if called from
     * getDefendantsBySurname(surname,firstname,middlename)
     * 
     * @param name1
     * @param name2
     * @param name3
     * @return
     */

    private String buildPersonsFullName(String name1, String name2, String name3) {

        StringBuffer fullName = new StringBuffer();

        // First name
        if (name1 != null) {
            fullName.append(name1);
            fullName.append(" ");
        }

        // Middle name
        if (name2 != null) {
            fullName.append(name2);
            fullName.append(" ");
        }

        // Surname
        if (name3 != null)
            fullName.append(name3);

        return fullName.toString();

    }

    // End - Utility methods
    // ------------------------------------------------------


}