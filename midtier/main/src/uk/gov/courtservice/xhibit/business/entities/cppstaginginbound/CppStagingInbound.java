package uk.gov.courtservice.xhibit.business.entities.cppstaginginbound;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

/**
 * Represents data in XHB_CPP_STAGING_INBOUND.
 * @author atwells
 *
 */
public interface CppStagingInbound extends CSEntityLocal {
	public Integer getCppStagingInboundId();

	public void setCppStagingInboundId(Integer cppStagingInboundId);
	
	public String getDocumentName();

	public void setDocumentName(String documentName);
	
	public Integer getCourtCode();

	public void setCourtCode(Integer courtCode);
	
	public String getDocumentType();

	public void setDocumentType(String documentType);

	public Date getTimeLoaded();

	public void setTimeLoaded(Date TimeLoaded);
	
	public Long getClobId();

	public void setClobId(Long clobId);

	public String getValidationStatus();

	public void setValidationStatus(String validationStatus);

	public String getAcknowledgmentStatus();

	public void setAcknowledgmentStatus(String acknowledgmentStatus);

	public String getProcessingStatus();

	public void setProcessingStatus(String processingStatus);

	public String getValidationErrorMessage();

	public void setValidationErrorMessage(String validationErrorMessage);

	public String getObsInd();

	public void setObsInd(String obsInd);

	public String getLastUpdatedBy();

	public void setLastUpdatedBy(String lastUpdatedBy);

	public Date getLastUpdateDate();

	public void setLastUpdateDate(Date lastUpdateDate);

	public Date getCreationDate();

	public void setCreationDate(Date creationDate);

	public String getCreatedBy();

	public void setCreatedBy(String createdBy) ;
	
}