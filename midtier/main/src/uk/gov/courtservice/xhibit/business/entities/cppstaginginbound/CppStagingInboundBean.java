package uk.gov.courtservice.xhibit.business.entities.cppstaginginbound;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class CppStagingInboundBean extends CSEntityBean implements EntityBean {

	/**
	 * default serialversionuid.
	 */
	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(String documentName, Integer courtCode, String documentType, Date timeLoaded, Long clobId,
			String validationStatus, String acknowledgmentStatus, String processingStatus, String validationErrorMessage, String userDisplayName) throws CreateException {
		setDocumentName(documentName);
		setCourtCode(courtCode);
		setDocumentType(documentType);
		setTimeLoaded(timeLoaded);
		setClobId(clobId);
		setValidationStatus(validationStatus);
		setAcknowledgmentStatus(acknowledgmentStatus);
		setProcessingStatus(processingStatus);
		setValidationErrorMessage(validationErrorMessage);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(String documentName, Integer courtCode, String documentType, Date timeLoaded, Long clobId,
			String validationStatus, String acknowledgmentStatus, String processingStatus, String validationErrorMessage, String userDisplayName) throws CreateException {
		// Empty
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract Integer getCppStagingInboundId();

	public abstract void setCppStagingInboundId(Integer cppStagingInboundId);
	
	public abstract String getDocumentName();

	public abstract void setDocumentName(String documentName);

	public abstract Integer getCourtCode();

	public abstract void setCourtCode(Integer courtCode);
	
	public abstract String getDocumentType();

	public abstract void setDocumentType(String documentType);

	public abstract Date getTimeLoaded();

	public abstract void setTimeLoaded(Date timeLoaded);
	
	public abstract Long getClobId();

	public abstract void setClobId(Long clobId);

	public abstract String getValidationStatus();

	public abstract void setValidationStatus(String validationStatus);

	public abstract String getAcknowledgmentStatus();

	public abstract void setAcknowledgmentStatus(String acknowledgmentStatus);

	public abstract String getProcessingStatus();

	public abstract void setProcessingStatus(String processingStatus);
	
	public abstract String getValidationErrorMessage();

	public abstract void setValidationErrorMessage(String validationErrorMessage);

	public abstract String getObsInd();

	public abstract void setObsInd(String obsInd);

	public abstract String getLastUpdatedBy();

	public abstract void setLastUpdatedBy(String lastUpdatedBy);

	public abstract Date getLastUpdateDate();

	public abstract void setLastUpdateDate(Date lastUpdateDate);

	public abstract Date getCreationDate();

	public abstract void setCreationDate(Date creationDate);

	public abstract String getCreatedBy();

	public abstract void setCreatedBy(String createdBy) ;
	

}