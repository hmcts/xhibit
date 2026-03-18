package uk.gov.courtservice.xhibit.business.entities.sitting;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class SittingBean extends CSEntityBean implements EntityBean {

    public Integer ejbCreate(Integer sittingSequenceNo, String isSittingJudge, java.sql.Timestamp sittingTime,
            String sittingNote, Integer refJustice1Id, Integer refJustice2Id, Integer refJustice3Id,
            Integer refJustice4Id, String isFloating, Integer refJudgeId, Integer courtSiteId, Integer courtRoomId,
            String justiceName1, String justiceName2, String justiceName3, String justiceName4, String userDisplayName) throws CreateException {
        setSittingSequenceNo(sittingSequenceNo);
        setIsSittingJudge(isSittingJudge);
        setSittingTime(sittingTime);
        setSittingNote(sittingNote);
        setRefJustice1Id(refJustice1Id);
        setRefJustice2Id(refJustice2Id);
        setRefJustice3Id(refJustice3Id);
        setRefJustice4Id(refJustice4Id);
        setIsFloating(isFloating);
        setRefJudgeId(refJudgeId);
        setCourtSiteId(courtSiteId);
        setCourtRoomId(courtRoomId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        // setListID(listID);
        setJusticeName1(justiceName1);
        setJusticeName2(justiceName2);
        setJusticeName3(justiceName3);
        setJusticeName4(justiceName4);
        return null;
    }

    public void ejbPostCreate(Integer sittingSequenceNo, String isSittingJudge, java.sql.Timestamp sittingTime,
            String sittingNote, Integer refJustice1Id, Integer refJustice2Id, Integer refJustice3Id,
            Integer refJustice4Id, String isFloating, Integer refJudgeId, Integer courtSiteId, Integer courtRoomId,
            String justiceName1, String justiceName2, String justiceName3, String justiceName4, String userDisplayName) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract void setSittingId(Integer sittingId);

    public abstract void setSittingSequenceNo(Integer sittingSequenceNo);

    public abstract void setIsSittingJudge(String isSittingJudge);

    public abstract void setSittingTime(java.sql.Timestamp sittingTime);

    public abstract void setSittingNote(String sittingNote);

    public abstract void setRefJustice1Id(Integer refJustice1Id);

    public abstract void setRefJustice2Id(Integer refJustice2Id);

    public abstract void setRefJustice3Id(Integer refJustice3Id);

    public abstract void setRefJustice4Id(Integer refJustice4Id);

    public abstract void setIsFloating(String isFloating);

    public abstract void setRefJudgeId(Integer refJudgeId);

    public abstract void setCourtSiteId(Integer courtSiteId);

    public abstract void setCourtRoomId(Integer courtRoomId);

    public abstract Integer getSittingId();

    public abstract Integer getSittingSequenceNo();

    public abstract String getIsSittingJudge();

    public abstract java.sql.Timestamp getSittingTime();

    public abstract String getSittingNote();

    public abstract Integer getRefJustice1Id();

    public abstract Integer getRefJustice2Id();

    public abstract Integer getRefJustice3Id();

    public abstract Integer getRefJustice4Id();

    public abstract String getIsFloating();

    public abstract Integer getRefJudgeId();

    public abstract Integer getCourtSiteId();

    public abstract Integer getCourtRoomId();

    public abstract void setListID(Integer listID);

    public abstract void setJusticeName1(java.lang.String justiceName1);

    public abstract void setJusticeName2(java.lang.String justiceName2);

    public abstract void setJusticeName3(java.lang.String justiceName3);

    public abstract void setJusticeName4(java.lang.String justiceName4);

    public abstract Integer getListID();

    public abstract java.lang.String getJusticeName1();

    public abstract java.lang.String getJusticeName2();

    public abstract java.lang.String getJusticeName3();

    public abstract java.lang.String getJusticeName4();

    // ------------------------------CMR
    // Fields------------------------------------
    public abstract void setScheduledHearings(java.util.Collection scheduledHearings);

    public abstract void setHearingList(uk.gov.courtservice.xhibit.business.entities.hearinglist.HearingList hearingList);

    public abstract java.util.Collection getScheduledHearings();

    public abstract uk.gov.courtservice.xhibit.business.entities.hearinglist.HearingList getHearingList();

}