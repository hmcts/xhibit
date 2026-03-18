package uk.gov.courtservice.xhibit.business.entities.hearinglist;

import java.sql.Timestamp;
import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface HearingList extends CSEntityLocal {
    public Integer getListId();

    public void setListType(String listType);

    public String getListType();

    public void setStartDate(Timestamp startDate);

    public Timestamp getStartDate();

    public void setEndDate(Timestamp endDate);

    public Timestamp getEndDate();

    public void setStatus(String status);

    public String getStatus();

    public void setEditionNo(Integer editionNo);

    public Integer getEditionNo();

    public void setPublishedTime(Timestamp publishedTime);

    public Timestamp getPublishedTime();

    public void setPrintReference(String printReference);

    public String getPrintReference();

    public void setCrestListId(Integer crestListId);

    public Integer getCrestListId();

    public void setCourtId(Integer courtId);

    public Integer getCourtId();

    public abstract void setListCourtType(java.lang.String listCourtType);

    public abstract java.lang.String getListCourtType();

    public void setSittings(Collection sittings);

    public Collection getSittings();
}