package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Vector;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

//main report object
public class OBWPrintValue extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	private Vector<OBWDefendantValue> obwDefendantValues;
	private String dateOfRequest;
	private String priorToDate;
	private Integer numRows;
    
	public OBWPrintValue(){
		obwDefendantValues = new Vector<OBWDefendantValue>();
    }
	
    /**
	 * @return the obwPrintValues
	 */
	public Vector<OBWDefendantValue> getOBWDefendantValues() {
		return obwDefendantValues;
	}

	/**
	 * @param obwPrintValues the obwPrintValues to set
	 */
	public void setOBWDefendantValues(Vector<OBWDefendantValue> obwDefendantValues) {
		this.obwDefendantValues = obwDefendantValues;
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

	public Integer getNumRows() {
		return numRows;
	}

	public void setNumRows(Integer numRows) {
		this.numRows = numRows;
	}

	@Override
	public String getShortReportCode() {
		return "OBW";
	}

	
}
