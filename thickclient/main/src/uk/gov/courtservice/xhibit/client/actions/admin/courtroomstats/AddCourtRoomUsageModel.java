package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import java.util.Date;

public class AddCourtRoomUsageModel {

	private Date sittingDate = new Date();
	private Integer courtSiteId;
	
	public final Date getSittingDate() {
		return sittingDate;
	}
	public final void setSittingDate(Date sittingDate) {
		this.sittingDate = sittingDate;
	}
	public Integer getCourtSiteId() {
		return courtSiteId;
	}
	public void setCourtSiteId(Integer courtSiteId) {
		this.courtSiteId = courtSiteId;
	}
}