package uk.gov.courtservice.xhibit.business.entities.sitting;

import java.sql.Timestamp;
import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.hearinglist.HearingList;

public interface Sitting extends CSEntityLocal {
    public Integer getSittingId();

    public void setSittingSequenceNo(Integer sittingSequenceNo);

    public Integer getSittingSequenceNo();

    public void setIsSittingJudge(String isSittingJudge);

    public String getIsSittingJudge();

    public void setSittingTime(Timestamp sittingTime);

    public Timestamp getSittingTime();

    public void setSittingNote(String sittingNote);

    public String getSittingNote();

    public void setRefJustice1Id(Integer refJustice1Id);

    public Integer getRefJustice1Id();

    public void setRefJustice2Id(Integer refJustice2Id);

    public Integer getRefJustice2Id();

    public void setRefJustice3Id(Integer refJustice3Id);

    public Integer getRefJustice3Id();

    public void setRefJustice4Id(Integer refJustice4Id);

    public Integer getRefJustice4Id();

    public void setIsFloating(String isFloating);

    public String getIsFloating();

    public void setRefJudgeId(Integer refJudgeId);

    public Integer getRefJudgeId();

    public void setCourtSiteId(Integer courtSiteId);

    public Integer getCourtSiteId();

    public void setCourtRoomId(Integer courtRoomId);

    public Integer getCourtRoomId();

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

    public void setScheduledHearings(Collection scheduledHearings);

    public Collection getScheduledHearings();

    public void setHearingList(HearingList hearingList);

    public HearingList getHearingList();
}