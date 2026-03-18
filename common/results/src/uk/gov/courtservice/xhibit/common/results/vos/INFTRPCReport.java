package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Vector;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class INFTRPCReport extends ReportAbsttractValue{
	
	private static final long serialVersionUID = 1L;
	
	private Vector<INFTRPCMainValue>inftrpcMainValues ;
	private String monthOfReport ;
	private String yearOfReport;
	
	public INFTRPCReport(){
		inftrpcMainValues = new Vector<INFTRPCMainValue>();
	}
	

	@Override
	public String getShortReportCode() {
		return "INFTRL";
		
	}


	public Vector<INFTRPCMainValue> getInftrpcMainValues() {
		return inftrpcMainValues;
	}


	public void setInftrpcMainValues(Vector<INFTRPCMainValue> inftrpcMainValues) {
		this.inftrpcMainValues = inftrpcMainValues;
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
