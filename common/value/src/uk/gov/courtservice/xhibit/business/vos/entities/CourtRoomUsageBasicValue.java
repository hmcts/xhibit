package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class CourtRoomUsageBasicValue extends CSAbstractValue {
	

	private static final long serialVersionUID = 1L;
	private Integer courtRoomUsageId;
	private Integer courtRoomId;
	private Integer amTimeHours;
	private Integer amTimeMins;
	private Integer pmTimeHours;
	private Integer pmTimeMins;
	private Date sittingDate;
	public final Integer getCourtRoomUsageId() {
		return courtRoomUsageId;
	}
	public final void setCourtRoomUsageId(Integer courtRoomUsageId) {
		this.courtRoomUsageId = courtRoomUsageId;
	}
	public final Integer getCourtRoomId() {
		return courtRoomId;
	}
	public final void setCourtRoomId(Integer courtRoomId) {
		this.courtRoomId = courtRoomId;
	}
	public final Integer getAmTimeHours() {
		return amTimeHours;
	}
	public final void setAmTimeHours(Integer amTimeHours) {
		this.amTimeHours = amTimeHours;
	}
	public final Integer getAmTimeMins() {
		return amTimeMins;
	}
	public final void setAmTimeMins(Integer amTimeMins) {
		this.amTimeMins = amTimeMins;
	}
	public final Integer getPmTimeHours() {
		return pmTimeHours;
	}
	public final void setPmTimeHours(Integer pmTimeHours) {
		this.pmTimeHours = pmTimeHours;
	}
	public final Integer getPmTimeMins() {
		return pmTimeMins;
	}
	public final void setPmTimeMins(Integer pmTimeMins) {
		this.pmTimeMins = pmTimeMins;
	}
	public final Date getSittingDate() {
		return sittingDate;
	}
	public final void setSittingDate(Date sittingDate) {
		this.sittingDate = sittingDate;
	}
	
	

}
