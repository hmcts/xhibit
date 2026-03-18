package uk.gov.courtservice.xhibit.business.entities.hearinglist;

import java.sql.Timestamp;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface HearingListHome extends javax.ejb.EJBLocalHome {
    public HearingList create(String listType, Timestamp startDate, Timestamp endDate, String status,
            Integer editionNo, Timestamp publishedTime, String printReference, Integer crestListId, Integer courtId,
            java.lang.String listCourtType, String userDisplayName) throws CreateException;

    public HearingList findByPrimaryKey(Integer listId) throws FinderException;

    public HearingList findByCourtIdAndDate(java.lang.Integer courtId, java.sql.Timestamp date) throws FinderException;

    public HearingList findByCourtIdDateAndListType(java.lang.Integer courtId, java.sql.Timestamp date, String listType)
            throws FinderException;
}