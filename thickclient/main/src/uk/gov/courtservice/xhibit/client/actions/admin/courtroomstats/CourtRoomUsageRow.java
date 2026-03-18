package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import java.util.Date;

/**
 * POJO to represent a row.
 * @author westalll
 *
 */
public class CourtRoomUsageRow {
	
	private Integer courtRoomUsageId;
	private Integer courtRoomId;
	private String courtRoomName;
	
	private Integer amHours;
	private Integer amMins;
	private Integer pmHours;
	private Integer pmMins;
	private Date sittingDate;
	
	private boolean dirty = false;
	
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
	public final String getCourtRoomName() {
		return courtRoomName;
	}
	public final void setCourtRoomName(String courtRoomName) {
		this.courtRoomName = courtRoomName;
	}
	public final Integer getAmHours() {
		return amHours;
	}
	public final void setAmHours(Integer amHours) {
		this.amHours = amHours;
	}
	public final Integer getAmMins() {
		return amMins;
	}
	public final void setAmMins(Integer amMins) {
		this.amMins = amMins;
	}
	public final Integer getPmHours() {
		return pmHours;
	}
	public final void setPmHours(Integer pmHours) {
		this.pmHours = pmHours;
	}
	public final Integer getPmMins() {
		return pmMins;
	}
	public final void setPmMins(Integer pmMins) {
		this.pmMins = pmMins;
	}
	public final Date getSittingDate() {
		return sittingDate;
	}
	public final void setSittingDate(Date sittingDate) {
		this.sittingDate = sittingDate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courtRoomUsageId == null) ? 0 : courtRoomUsageId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourtRoomUsageRow other = (CourtRoomUsageRow) obj;
		if (courtRoomUsageId == null) {
			if (other.courtRoomUsageId != null)
				return false;
		} else if (!courtRoomUsageId.equals(other.courtRoomUsageId))
			return false;
		return true;
	}
	public boolean isDirty() {
		return dirty;
	}
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
}
