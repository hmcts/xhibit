package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import java.util.Date;

import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class RecordCourtroomStatisticsModel {
	
	public RecordCourtroomStatisticsModel(XhibitApplicationController xac) {
		setXac(xac);
	}
	
	private Date sittingDate = new Date();
	private XhibitApplicationController xac;
	
	public final Date getSittingDate() {
		return sittingDate;
	}
	public final void setSittingDate(Date sittingDate) {
		this.sittingDate = sittingDate;
	}
	public XhibitApplicationController getXac() {
		return xac;
	}
	public void setXac(XhibitApplicationController xac) {
		this.xac = xac;
	}
}
