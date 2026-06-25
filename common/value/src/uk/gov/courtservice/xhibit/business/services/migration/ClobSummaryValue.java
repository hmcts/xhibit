package uk.gov.courtservice.xhibit.business.services.migration;

import java.io.Serializable;
import java.util.Date;

public class ClobSummaryValue implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long clobId;
	private String clobData;
	
	
	public ClobSummaryValue(Long clobId, String clobData) {
		super();
		this.clobId = clobId;
		this.clobData = clobData;
	}

	public Long getClobId() {
		return clobId;
	}

	public void setClobId(Long clobId) {
		this.clobId = clobId;
	}

	public String getClobData() {
		return clobData;
	}

	public void setClobData(String clobData) {
		this.clobData = clobData;
	}
}