package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;
import java.util.List;

public class HearingTypes implements Serializable {
	
	private static final long serialVersionUID = 1L;	
	private String hearingType;
	public String getHearingType() {
		return hearingType;
	}

	public void setHearingType(String hearingType) {
		this.hearingType = hearingType;
	}


	
}
