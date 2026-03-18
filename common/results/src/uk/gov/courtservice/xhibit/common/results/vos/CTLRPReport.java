package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class CTLRPReport extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	
	private String dateOfRequest;
	//private String courtName;
	private String courttelephoneno;
	private String courtaddress;
	private String inputDate;
	private String time;
	private String casesList;
	
	private ArrayList ctlrpValues;

	public String getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	/*public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}*/

	public ArrayList getCtlrpValues() {
		return ctlrpValues;
	}

	public void setCtlrpValues(ArrayList ctlrpValues) {
		this.ctlrpValues = ctlrpValues;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCasesList() {
		return casesList;
	}

	public void setCasesList(String casesList) {
		this.casesList = casesList;
	}

	public String getCourttelephoneno() {
		return courttelephoneno;
	}

	public void setCourttelephoneno(String courttelephoneno) {
		this.courttelephoneno = courttelephoneno;
	}

	public String getCourtaddress() {
		return courtaddress;
	}

	public void setCourtaddress(String courtaddress) {
		this.courtaddress = courtaddress;
	}

	@Override
	public String getShortReportCode() {
		return "CTLRP";
	}	
}
