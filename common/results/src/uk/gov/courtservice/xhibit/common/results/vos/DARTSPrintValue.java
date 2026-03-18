package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Vector;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class DARTSPrintValue extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	private Vector<DARTSRetentionPolicyValue> dartsRetentionPolicyValues;
	private String dateOfRequest;
	private String startDate;
	private String endDate;
	
	public DARTSPrintValue(){
		dartsRetentionPolicyValues = new Vector<DARTSRetentionPolicyValue>();
    }
	
	/**
	 * @return the dartsPrintValues
	 */
	public Vector<DARTSRetentionPolicyValue> getDARTSRetentionPolicyValues() {
		return dartsRetentionPolicyValues;
	}

	/**
	 * @param dartsPrintValues the dartsPrintValues to set
	 */
	public void setDARTSRetentionPolicyValues(Vector<DARTSRetentionPolicyValue> dartsRetentionPolicyValues) {
		this.dartsRetentionPolicyValues = dartsRetentionPolicyValues;
	}
	
	public String getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String getShortReportCode() {
		return "XSDAR";
	}
}
