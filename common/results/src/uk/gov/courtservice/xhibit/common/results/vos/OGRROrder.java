package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Vector;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class OGRROrder extends ReportAbsttractValue {

	private static final long serialVersionUID = 1L;
	private String courtCode;
	private Vector<OGRRRespondentValue> ogrrRespondentValues;

	public OGRROrder() {
		ogrrRespondentValues = new Vector<OGRRRespondentValue>();
	}

	/**
	 * @return the ogrrRespondentValues
	 */
	public Vector<OGRRRespondentValue> getOGRRRespondentValues() {
		return ogrrRespondentValues;
	}

	/**
	 * @param ogrrRespondentValues
	 *            the ogrrRespondentValues to set
	 */
	public void setOGRRRespondentValues(Vector<OGRRRespondentValue> ogrrRespondentValues) {
		this.ogrrRespondentValues = ogrrRespondentValues;
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
