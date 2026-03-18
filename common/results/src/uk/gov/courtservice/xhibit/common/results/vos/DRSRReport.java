package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Vector;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class DRSRReport extends ReportAbsttractValue{
	
	
	private static final long serialVersionUID = 1L;
	private String timeofReport;
	private String dateofReport;
	//private String courtName;
	private Vector<DRSRReportValue>drsrReportValues ;
	
	public DRSRReport(){
		drsrReportValues = new Vector<DRSRReportValue>();
    } 
	
	public String getTimeofReport() {
		return timeofReport;
	}
	public void setTimeofReport(String timeofReport) {
		this.timeofReport = timeofReport;
	}
	public String getDateofReport() {
		return dateofReport;
	}
	public void setDateofReport(String dateofReport) {
		this.dateofReport = dateofReport;
	}
	/*public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}*/
	public Vector<DRSRReportValue> getDrsrReportValues() {
		return drsrReportValues;
	}
	public void setDrsrReportValues(Vector<DRSRReportValue> drsrReportValues) {
		this.drsrReportValues = drsrReportValues;
	}

	@Override
	public String getShortReportCode() {
		return "DRSR";
	}
}
