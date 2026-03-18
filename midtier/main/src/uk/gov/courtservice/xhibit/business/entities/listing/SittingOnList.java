package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface SittingOnList extends CSEntityLocal {
 
	public Integer getSittingOnListId();
	public Integer getSittingNumber();
	public Integer getListId();
	public Integer getTimeMarkingId();
	public Date getTimeListed();
	public Integer getJudgeRefId();
	public String getJp1();
	public String getJp2();
	public String getJp3();
	public String getJp4();
	public String getListNoteText();
	public Integer getFreeTextNoteClassId();
	public String getObsInd();
	public Integer getCourtRoomId();
	public Integer getCourtSiteId();
	public String getCreatedBy();
	public String getLastUpdatedBy();
	public Date getCreationDate();
	public Date getLastUpdateDate();	
	
	public void setSittingOnListId(Integer sittingOnListId);
	public void setSittingNumber(Integer sittingNumber);
	public void setListId(Integer listId);
	public void setTimeMarkingId(Integer timeMarkingId);
	public void setTimeListed(Date timeListed);
	public void setJudgeRefId(Integer judgeRefId);
	public void setJp1(String jp1);
	public void setJp2(String jp2);
	public void setJp3(String jp3);
	public void setJp4(String jp4);
	public void setListNoteText(String listNoteText);
	public void setFreeTextNoteClassId(Integer freeTextNoteClassId);
	public void setObsInd(String obsInd);
	public void setCourtRoomId(Integer courtRoomId);
	public void setCourtSiteId(Integer courtSiteId);
	public void setCreatedBy(String createdBy);
	public void setLastUpdatedBy(String lastUpdatedBy);
	public void setCreationDate(Date creationDate);
	public void setLastUpdateDate(Date lastUpdateDate);
	public void setVersion(Integer version);

}
	