package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class InternetHtmlBasicValue extends CSAbstractValue {
	
	private static final long serialVersionUID = 8772760362423655022L;
	
    private String status;
    private Integer courtId;
    private Long htmlBlobId;
    
    public InternetHtmlBasicValue() {
        super();
    }

    public InternetHtmlBasicValue(Integer version) {
        super(version);
    }

    public InternetHtmlBasicValue(Integer internetHtmlId, Integer version) {
        super(internetHtmlId, version);
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public Long getHtmlBlobId() {
		return htmlBlobId;
	}

	public void setHtmlBlobId(Long htmlBlobId) {
		this.htmlBlobId = htmlBlobId;
	}
    
    
}
