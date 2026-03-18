package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;
import java.util.Vector;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class NHAReport extends ReportAbsttractValue implements ISingleRunLetterReport {

	private static final long serialVersionUID = 1L;
	private String courtCode;
	private String courtAddressFormatted;
	private Vector<NHACaseValue> nhaCaseValues;

	public NHAReport() {
		nhaCaseValues = new Vector<NHACaseValue>();
	}

	/**
	 * @return the docarPrintValues
	 */
	public Vector<NHACaseValue> getNHACaseValues() {
		return nhaCaseValues;
	}

	/**
	 * @param docarPrintValues
	 *            the docarPrintValues to set
	 */
	public void NHACaseValues(Vector<NHACaseValue> nhaCaseValues) {
		this.nhaCaseValues = nhaCaseValues;
	}

	/**
	 * @return the courtCode
	 */
	public String getCourtCode() {
		return courtCode;
	}

	/**
	 * @param courtCode
	 *            the courtCode to set
	 */
	public void setCourtCode(String courtCode) {
		this.courtCode = courtCode;
	}

	@Override
	public String getShortReportCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCourtAddressFormatted() {
		return courtAddressFormatted;
	}

	public void setCourtAddressFormatted(String courtAddressFormatted) {
		this.courtAddressFormatted = courtAddressFormatted;
	}

	@Override
	public Integer[] getReportedIDs() {

		ArrayList<Integer> printedCases = new ArrayList<Integer>();
		for (int i = 0; i < getNHACaseValues().size(); i++) {
			NHACaseValue cv = getNHACaseValues().get(i);
			printedCases.add(cv.getCaseId());
		}
		Integer[] reportedCasesArray = new Integer[printedCases.size()];
		printedCases.toArray(reportedCasesArray);
		return reportedCasesArray;
	}

	@Override
	public String getDatabaseUpdateProcedureName() {
		return "XHB_REPORT_PKG.MARK_CASES_AS_NHA_REPORTED";
	}
}
