package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Vector;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class DOCARPrintValue extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	private Vector<DOCARDefendantValue> docarDefendantValues;
	private String dateOfRequest;
	private String priorToDate;
    
	public DOCARPrintValue(){
		docarDefendantValues = new Vector<DOCARDefendantValue>();
    }
	
    /**
	 * @return the docarPrintValues
	 */
	public Vector<DOCARDefendantValue> getDOCARDefendantValues() {
		return docarDefendantValues;
	}

	/**
	 * @param docarPrintValues the docarPrintValues to set
	 */
	public void setDOCARDefendantValues(Vector<DOCARDefendantValue> docarDefendantValues) {
		this.docarDefendantValues = docarDefendantValues;
	}

	/**
	 * @return the dateOfRequest
	 */
	public String getDateOfRequest() {
		return dateOfRequest;
	}

	/**
	 * @param dateOfRequest the dateOfRequest to set
	 */
	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	/**
	 * @return the priorToDate
	 */
	public String getPriorToDate() {
		return priorToDate;
	}

	/**
	 * @param priorToDate the priorToDate to set
	 */
	public void setPriorToDate(String priorToDate) {
		this.priorToDate = priorToDate;
	}

	@Override
	public String getShortReportCode() {
		return "DOCAR";
	}
}
