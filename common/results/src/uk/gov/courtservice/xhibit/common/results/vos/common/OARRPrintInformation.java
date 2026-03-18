package uk.gov.courtservice.xhibit.common.results.vos.common;

import uk.gov.courtservice.xhibit.business.database.results.OARDABody;
import uk.gov.courtservice.xhibit.business.database.results.OARRBody;

public class OARRPrintInformation extends ReportAbsttractValue {

private static final long serialVersionUID = 1L;
	
	private OARRBody oarrBodyObject;

	@Override
	public String getShortReportCode() {
		return "OARR";
	}


	public OARRBody getOarrBodyObject() {
		return oarrBodyObject;
	}


	public void setOarrBodyObject(OARRBody oarrBodyObject) {
		this.oarrBodyObject = oarrBodyObject;
	}
}
