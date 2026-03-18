package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface SittingOnListHome extends EJBLocalHome {
    public SittingOnList create(Integer sittingOnListId, Integer sittingNumber,Integer listId,
    		Integer timeMarkingId, Date timeListed, Integer judgeRefId,
    		String jp1, String jp2, String jp3, String jp4,
    		String listNoteText, Integer freeTextNoteClassId,
    		String obsInd, String userDisplayName, Integer courtRoomId, Integer courtSiteId) throws CreateException;

    public SittingOnList findByPrimaryKey(Integer sittingOnListId) throws FinderException;

    public Collection findByListId(Integer listId) throws FinderException;

    public Collection findByJudgeIdAndDate(Integer listId, Date currentDate) throws FinderException;

}