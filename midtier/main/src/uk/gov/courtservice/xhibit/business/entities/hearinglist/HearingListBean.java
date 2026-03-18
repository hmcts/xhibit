package uk.gov.courtservice.xhibit.business.entities.hearinglist;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class HearingListBean extends CSEntityBean implements EntityBean {

    public Integer ejbCreate(String listType, java.sql.Timestamp startDate, java.sql.Timestamp endDate, String status,
            Integer editionNo, java.sql.Timestamp publishedTime, String printReference, Integer crestListId,
            Integer courtId, java.lang.String listCourtType, String userDisplayName) throws CreateException {
        setListType(listType);
        setStartDate(startDate);
        setEndDate(endDate);
        setStatus(status);
        setEditionNo(editionNo);
        setPublishedTime(publishedTime);
        setPrintReference(printReference);
        setCrestListId(crestListId);
        setCourtId(courtId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setListCourtType(listCourtType);
        return null;
    }

    public void ejbPostCreate(String listType, java.sql.Timestamp startDate, java.sql.Timestamp endDate, String status,
            Integer editionNo, java.sql.Timestamp publishedTime, String printReference, Integer crestListId,
            Integer courtId, java.lang.String listCourtType, String userDisplayName) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract void setListId(Integer listId);

    public abstract void setListType(String listType);

    public abstract void setStartDate(java.sql.Timestamp startDate);

    public abstract void setEndDate(java.sql.Timestamp endDate);

    public abstract void setStatus(String status);

    public abstract void setEditionNo(Integer editionNo);

    public abstract void setPublishedTime(java.sql.Timestamp publishedTime);

    public abstract void setPrintReference(String printReference);

    public abstract void setCrestListId(Integer crestListId);

    public abstract void setCourtId(Integer courtId);

    public abstract void setListCourtType(java.lang.String listCourtType);

    public abstract Integer getListId();

    public abstract String getListType();

    public abstract java.sql.Timestamp getStartDate();

    public abstract java.sql.Timestamp getEndDate();

    public abstract String getStatus();

    public abstract Integer getEditionNo();

    public abstract java.sql.Timestamp getPublishedTime();

    public abstract String getPrintReference();

    public abstract Integer getCrestListId();

    public abstract Integer getCourtId();

    public abstract java.lang.String getListCourtType();

    // ------------------------------CMR
    // Fields------------------------------------
    public abstract void setSittings(java.util.Collection sittings);

    public abstract java.util.Collection getSittings();

}