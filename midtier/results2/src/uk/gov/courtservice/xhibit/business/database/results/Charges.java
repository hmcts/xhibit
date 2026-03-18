package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;
import java.util.List;

public class Charges implements Serializable {
	
	private static final long serialVersionUID = 1L;	
	private  String chargesInfo;
	
	public String getChargesInfo() {
		return chargesInfo;
	}

	public void setChargesInfo(String chargesInfo) {
		this.chargesInfo = chargesInfo;
	}

}
