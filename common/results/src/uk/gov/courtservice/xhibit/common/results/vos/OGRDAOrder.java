package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Vector;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class OGRDAOrder extends ReportAbsttractValue {

	private static final long serialVersionUID = 1L;
	private String courtCode;
	private Vector<OGRDADefendantValue> ogrdaDefendantValues;

	public OGRDAOrder() {
		ogrdaDefendantValues = new Vector<OGRDADefendantValue>();
	}

	/**
	 * @return the ogrdaDefendantValues
	 */
	public Vector<OGRDADefendantValue> getOGRDADefendantValues() {
		return ogrdaDefendantValues;
	}

	/**
	 * @param ogrdaDefendantValues
	 *            the ogrdaDefendantValues to set
	 */
	public void setOGRDADefendantValues(Vector<OGRDADefendantValue> ogrdaDefendantValues) {
		this.ogrdaDefendantValues = ogrdaDefendantValues;
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
}
