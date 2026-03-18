package uk.gov.courtservice.xhibit.common.results.vos;

import uk.gov.courtservice.xhibit.business.database.results.OWRDABody;
import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class OWRDAPrintInformation extends ReportAbsttractValue {

	private static final long serialVersionUID = 1L;
	
	private OWRDABody owrdaBodyObject;

	@Override
	public String getShortReportCode() {
		return "OWRDA";
	}

	public OWRDABody getOwrdaBodyObject() {
		return owrdaBodyObject;
	}


	public void setOwrdaBodyObject(OWRDABody owrdaBodyObject) {
		this.owrdaBodyObject = owrdaBodyObject;
	}

}
