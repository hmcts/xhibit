package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class SittingOnListBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer sittingOnListId, Integer sittingNumber,Integer listId,
    		Integer timeMarkingId, Date timeListed, Integer judgeRefId,
    		String jp1, String jp2, String jp3, String jp4,
    		String listNoteText, Integer freeTextNoteClassId,
    		String obsInd, String userDisplayName, Integer courtRoomId, Integer courtSiteId) throws CreateException {
		setSittingOnListId(sittingOnListId);
		setSittingNumber(sittingNumber);
    	setListId(listId);
    	setTimeMarkingId(timeMarkingId);
    	setTimeListed(timeListed);
    	setJudgeRefId(judgeRefId);
    	setJp1(jp1);
    	setJp2(jp2);
    	setJp3(jp3);
    	setJp4(jp4);
    	setListNoteText(listNoteText);
    	setFreeTextNoteClassId(freeTextNoteClassId);
    	setObsInd(obsInd);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setCourtRoomId(courtRoomId);
        setCourtSiteId(courtSiteId);
        return null;
    }

    public void ejbPostCreate(Integer sittingOnListId, Integer sittingNumber,Integer listId,
    		Integer timeMarkingId, Date timeListed, Integer judgeRefId,
    		String jp1, String jp2, String jp3, String jp4,
    		String listNoteText, Integer freeTextNoteClassId,
    		String obsInd, String userDisplayName, Integer courtRoomId, Integer courtSiteId) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------
	public abstract Integer getSittingOnListId();
	public abstract Integer getSittingNumber();
	public abstract Integer getListId();
	public abstract Integer getTimeMarkingId();
	public abstract Date getTimeListed();
	public abstract Integer getJudgeRefId();
	public abstract String getJp1();
	public abstract String getJp2();
	public abstract String getJp3();
	public abstract String getJp4();
	public abstract String getListNoteText();
	public abstract Integer getFreeTextNoteClassId();
	public abstract String getObsInd();
	public abstract Integer getCourtRoomId();
	public abstract Integer getCourtSiteId();
	public abstract String getCreatedBy();
	public abstract String getLastUpdatedBy();
	public abstract Date getCreationDate();
	public abstract Date getLastUpdateDate();
	
    public abstract void setSittingOnListId(Integer sittingOnListId);
	public abstract void setSittingNumber(Integer sittingNumber);
	public abstract void setListId(Integer listId);
	public abstract void setTimeMarkingId(Integer timeMarkingId);
	public abstract void setTimeListed(Date timeListed);
	public abstract void setJudgeRefId(Integer judgeRefId);
	public abstract void setJp1(String jp1);
	public abstract void setJp2(String jp2);
	public abstract void setJp3(String jp3);
	public abstract void setJp4(String jp4);
	public abstract void setListNoteText(String listNoteText);
	public abstract void setFreeTextNoteClassId(Integer freeTextNoteClassId);
	public abstract void setObsInd(String obsInd);
	public abstract void setCourtRoomId(Integer courtRoomId);
	public abstract void setCourtSiteId(Integer courtSiteId);
	public abstract void setCreatedBy(String createdBy);
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract void setCreationDate(Date creationDate);
	public abstract void setLastUpdateDate(Date lastUpdateDate);	
	    
}