package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Vector;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class ADJSSReportList extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	private String dateOfRequest;
	private String shortReportCode;
	
	private Vector<ADJSSValue> adjssDefendantValues;

	public ADJSSReportList(){
		adjssDefendantValues = new Vector<ADJSSValue>();
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
	 * @return the docarPrintValues
	 */
	public Vector<ADJSSValue> getADJSSDefendantValues() {
		return adjssDefendantValues;
	}

	/**
	 * @param docarPrintValues the docarPrintValues to set
	 */
	public void setADJSSDefendantValues(Vector<ADJSSValue> adjssValues) {
		this.adjssDefendantValues = adjssValues;
	}

	@Override
	public String getShortReportCode() {
		return shortReportCode;
	}

	public void setShortReportCode(String shortReportCode) {
		this.shortReportCode = shortReportCode;
	}
}
