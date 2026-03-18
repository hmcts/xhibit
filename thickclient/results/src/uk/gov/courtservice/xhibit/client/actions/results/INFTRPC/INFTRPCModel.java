package uk.gov.courtservice.xhibit.client.actions.results.INFTRPC;

import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class INFTRPCModel extends MonthYearDatePeriodReportModel {

	public  final String COUNTS_PERCENTAGE_REPORT = "Counts and Percentage Summary";
	public  final String CASENUMBER_DETAILS_REPORT = "Case Number Details";
	
	private String caseNumbersReport;
	private String countsPercentageReport;
	
	
	public INFTRPCModel(XhibitApplicationController xac) {
		super(xac);
		
	}


	public String getCaseNumbersReport() {
		return caseNumbersReport;
	}


	public void setCaseNumbersReport(String caseNumbersReport) {
		this.caseNumbersReport = caseNumbersReport;
	}


	public String getCountsPercentageReport() {
		return countsPercentageReport;
	}


	public void setCountsPercentageReport(String countsPercentageReport) {
		this.countsPercentageReport = countsPercentageReport;
	}


	public String getCOUNTS_PERCENTAGE_REPORT() {
		return COUNTS_PERCENTAGE_REPORT;
	}


	public String getCASENUMBER_DETAILS_REPORT() {
		return CASENUMBER_DETAILS_REPORT;
	}

	

}
