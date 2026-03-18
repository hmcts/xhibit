package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class OWRRPrintInformation extends ReportAbsttractValue {

	private static final long serialVersionUID = 1L;

	private OWRRBody owrrBody;
	
	@Override
	public String getShortReportCode() {
		return "OWRR";
	}

	public OWRRBody getOwrrBody() {
		return owrrBody;
	}

	public void setOwrrBody(OWRRBody owrrBody) {
		this.owrrBody = owrrBody;
	}
	
	

}
