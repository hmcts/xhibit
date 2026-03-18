package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class OARDAPrintInformation extends ReportAbsttractValue {

	private static final long serialVersionUID = 1L;
	
	private OARDABody oardaBodyObject;

	@Override
	public String getShortReportCode() {
		return "OARDA";
	}


	public OARDABody getOardaBodyObject() {
		return oardaBodyObject;
	}


	public void setOardaBodyObject(OARDABody oardaBodyObject) {
		this.oardaBodyObject = oardaBodyObject;
	}
}
