package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Vector;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class PRLISReport extends ReportAbsttractValue {

	private static final long serialVersionUID = 1L;
	private String courtAddress;
	private String courtPhoneNumber;
	private String courtFaxNumber;
	private String dateofReport;
	private Vector<PRLISCaseValue> prlisCaseValues;
	
	public PRLISReport(){
		prlisCaseValues = new Vector<PRLISCaseValue>();
    }
	
	/**
	 * @return the courtAddress
	 */
	public String getCourtAddress() {
		return courtAddress;
	}
	/**
	 * @param courtAddress the courtAddress to set
	 */
	public void setCourtAddress(String courtAddress) {
		this.courtAddress = courtAddress;
	}
	/**
	 * @return the courtPhoneNumber
	 */
	public String getCourtPhoneNumber() {
		return courtPhoneNumber;
	}
	/**
	 * @param courtPhoneNumber the courtPhoneNumber to set
	 */
	public void setCourtPhoneNumber(String courtPhoneNumber) {
		this.courtPhoneNumber = courtPhoneNumber;
	}
	/**
	 * @return the courtFaxNumber
	 */
	public String getCourtFaxNumber() {
		return courtFaxNumber;
	}
	/**
	 * @param courtFaxNumber the courtFaxNumber to set
	 */
	public void setCourtFaxNumber(String courtFaxNumber) {
		this.courtFaxNumber = courtFaxNumber;
	}
	/**
	 * @return the dateofReport
	 */
	public String getDateofReport() {
		return dateofReport;
	}
	/**
	 * @param dateofReport the dateofReport to set
	 */
	public void setDateofReport(String dateofReport) {
		this.dateofReport = dateofReport;
	}
	/**
	 * @return the prlisCaseValues
	 */
	public Vector<PRLISCaseValue> getPrlisCaseValues() {
		return prlisCaseValues;
	}
	/**
	 * @param prlisCaseValues the prlisCaseValues to set
	 */
	public void setPrlisCaseValues(Vector<PRLISCaseValue> prlisCaseValues) {
		this.prlisCaseValues = prlisCaseValues;
	}

	@Override
	public String getShortReportCode() {
		return "PRLIS";
	}
}
