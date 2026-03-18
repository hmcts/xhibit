package uk.gov.courtservice.xhibit.client.actions.results.common;

import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class MonthYearDatePeriodReportModel {
	
	private String monthPeriod;
	private String yearPeriod;
	
	
	private XhibitApplicationController  xac;
	
	public MonthYearDatePeriodReportModel(XhibitApplicationController xac) {
		this.xac = xac;
	}

	public XhibitApplicationController getXac() {
		return xac;
	}

	public void setXac(XhibitApplicationController xac) {
		this.xac = xac;
	}

	public String getMonthPeriod() {
		return monthPeriod;
	}

	public void setMonthPeriod(String monthPeriod) {
		this.monthPeriod = monthPeriod;
	}

	public String getYearPeriod() {
		return yearPeriod;
	}

	public void setYearPeriod(String yearPeriod) {
		this.yearPeriod = yearPeriod;
	}
}
