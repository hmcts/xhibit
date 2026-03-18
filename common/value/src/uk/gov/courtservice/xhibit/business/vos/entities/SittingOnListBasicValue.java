package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.util.DateTimeUtilities;

/**
 * <p>
 * Title: SittingOnListBasicValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class SittingOnListBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	// 1st January 1970 is used for date of time listed when only time is required
	private static final Calendar ZERO_DATE = new GregorianCalendar(1970, 0, 1);

	private Integer sittingOnListId;
	private Integer sittingNumber;
	private Integer listId;
	private Integer timeMarkingId;
	private Calendar timeListed;
	private Integer judgeRefId;
	private String jp1;
	private String jp2;
	private String jp3;
	private String jp4;
	private String listNoteText;
	private Integer freeTextNoteClassId;
	private String obsInd;
	private Integer courtRoomId;
	private Integer courtSiteId;
	private String createdBy;
	private String lastUpdatedBy; 
	private Date creationDate;
	private Date lastUpdateDate;
	

	public SittingOnListBasicValue() {
		super();
	}

	public SittingOnListBasicValue(Integer id, Integer version) {
		super(id, version);
	}
	
	public Integer getSittingOnListId() {
		return sittingOnListId;
	}

	public void setSittingOnListId(Integer sittingOnListId) {
		this.sittingOnListId = sittingOnListId;
	}

	public Integer getSittingNumber() {
		return sittingNumber;
	}

	public void setSittingNumber(Integer sittingNumber) {
		this.sittingNumber = sittingNumber;
	}

	public Integer getListId() {
		return listId;
	}

	public void setListId(Integer listId) {
		this.listId = listId;
	}

	public Integer getTimeMarkingId() {
		return timeMarkingId;
	}

	public void setTimeMarkingId(Integer timeMarkingId) {
		this.timeMarkingId = timeMarkingId;
	}

	public Calendar getTimeListed() {
		return timeListed;
	}	

	public void setTimeListed(Calendar timeListed) {
		this.timeListed = timeListed;
	}

	public Date getTimeListedDate() {
		Date dateListed = null;
		if (timeListed != null) {
			dateListed = DateTimeUtilities.stripTimeToUtilDate(timeListed.getTime());
		}
		return dateListed;
	}	

	public void setTimeListedDate(Date dateListed) {
		if (timeListed == null) {
			timeListed = DateTimeUtilities.stripTimeToCalendar(dateListed);			
		} else {
			Calendar cal = DateTimeUtilities.convertToCalendar(dateListed);
			timeListed.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			timeListed.set(Calendar.MONTH, cal.get(Calendar.MONTH));
			timeListed.set(Calendar.DATE, cal.get(Calendar.DATE));
		}
	}

	public boolean hasTimeListedDate() {
		boolean hasDate = false;
		if (timeListed != null &&
			!(timeListed.get(Calendar.YEAR) == ZERO_DATE.get(Calendar.YEAR) &&
			  timeListed.get(Calendar.MONTH) == ZERO_DATE.get(Calendar.MONTH) &&
			  timeListed.get(Calendar.DATE) == ZERO_DATE.get(Calendar.DATE))) {
			hasDate = true;
		}
		return hasDate;
	}
	
	public int getTimeListedHour() {
		int hourListed = 0;
		if (timeListed != null) {
			hourListed = timeListed.get(Calendar.HOUR_OF_DAY);
		}
		return hourListed;
	}	
	
	public void setTimeListedHour(int hourListed) {
		if (timeListed == null) {
			timeListed = DateTimeUtilities.stripTimeToCalendar(new Date());
			clearTimeListedDate();
		}
		timeListed.set(Calendar.HOUR_OF_DAY, hourListed);
	}
	
	public int getTimeListedMinute() {
		int minuteListed = 0;
		if (timeListed != null) {
			minuteListed = timeListed.get(Calendar.MINUTE);
		}
		return minuteListed;
	}	
	
	public void setTimeListedMinute(int minuteListed) {
		if (timeListed == null) {
			timeListed = DateTimeUtilities.stripTimeToCalendar(new Date());
			clearTimeListedDate();
		}
		timeListed.set(Calendar.MINUTE, minuteListed);
	}
	
	public void clearTimeListedDate() {
		if (timeListed != null) {
			timeListed.set(Calendar.YEAR, ZERO_DATE.get(Calendar.YEAR));
			timeListed.set(Calendar.MONTH, ZERO_DATE.get(Calendar.MONTH));
			timeListed.set(Calendar.DATE, ZERO_DATE.get(Calendar.DATE));
		}
	}

	public void clearTimeListedTime() {
		if (timeListed != null) {
			timeListed = DateTimeUtilities.stripTimeToCalendar(timeListed.getTime());
		}
	}
	
	public Integer getJudgeRefId() {
		return judgeRefId;
	}

	public void setJudgeRefId(Integer judgeRefId) {
		this.judgeRefId = judgeRefId;
	}

	public String getJp1() {
		return jp1;
	}

	public void setJp1(String jp1) {
		this.jp1 = jp1;
	}

	public String getJp2() {
		return jp2;
	}

	public void setJp2(String jp2) {
		this.jp2 = jp2;
	}

	public String getJp3() {
		return jp3;
	}

	public void setJp3(String jp3) {
		this.jp3 = jp3;
	}

	public String getJp4() {
		return jp4;
	}

	public void setJp4(String jp4) {
		this.jp4 = jp4;
	}


	public String getListNoteText() {
		return listNoteText;
	}

	public void setListNoteText(String listNoteText) {
		this.listNoteText = listNoteText;
	}

	public Integer getFreeTextNoteClassId() {
		return freeTextNoteClassId;
	}

	public void setFreeTextNoteClassId(Integer freeTextNoteClassId) {
		this.freeTextNoteClassId = freeTextNoteClassId;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
	
	public Integer getCourtRoomId() {
		return courtRoomId;
	}

	public void setCourtRoomId(Integer courtRoomId) {
		this.courtRoomId = courtRoomId;
	}

	public Integer getCourtSiteId() {
		return courtSiteId;
	}

	public void setCourtSiteId(Integer courtSiteId) {
		this.courtSiteId = courtSiteId;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


	@Override
	public String toString() {
		return "SittingOnListBasicValue [sittingOnListId=" + sittingOnListId + ", sittingNumber=" + sittingNumber
				+ ", listId=" + listId + ", timeMarkingId=" + timeMarkingId + ", timeListed=" + timeListed
				+ ", judgeRefId=" + judgeRefId + ", jp1=" + jp1 + ", jp2=" + jp2 + ", jp3=" + jp3 + ", jp4=" + jp4
				+ ", listNoteText=" + listNoteText
				+  ", freeTextNoteClassId="
				+ freeTextNoteClassId + ", obsInd=" + obsInd + ", courtRoomId=" + courtRoomId + ", courtSiteId="
				+ courtSiteId + "]";
	}
	
	
}