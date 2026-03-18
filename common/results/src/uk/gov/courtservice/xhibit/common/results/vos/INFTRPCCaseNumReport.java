package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class INFTRPCCaseNumReport extends ReportAbsttractValue {

	private static final long serialVersionUID = 1L;
	private List<INFTRPCCaseNumValue>inftrpcCaseNumValues = new ArrayList<INFTRPCCaseNumValue>();
	private ArrayList caseDescriptionValues;
	private String monthOfReport ;
	private String yearOfReport;
	
	@Override
	public String getShortReportCode() {
	
		return"INFTRC";
	}
	public List<INFTRPCCaseNumValue> getInftrpcCaseNumValues() {
		return inftrpcCaseNumValues;
	}
	public void setInftrpcCaseNumValues(List<INFTRPCCaseNumValue> inftrpcCaseNumValues) {
		this.inftrpcCaseNumValues = inftrpcCaseNumValues;
	}
	
	public ArrayList getCaseDescriptionValues() {
		return caseDescriptionValues;
	}
	public void setCaseDescriptionValues(ArrayList caseDescriptionValues) {
		this.caseDescriptionValues = caseDescriptionValues;
	}
	public String getMonthOfReport() {
		return monthOfReport;
	}
	public void setMonthOfReport(String monthOfReport) {
		this.monthOfReport = monthOfReport;
	}
	public String getYearOfReport() {
		return yearOfReport;
	}
	public void setYearOfReport(String yearOfReport) {
		this.yearOfReport = yearOfReport;
	}

}
