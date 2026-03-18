package uk.gov.courtservice.xhibit.business.entities.cppformatting;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.court.Court;

abstract public class CppFormattingBean extends CSEntityBean implements EntityBean {

	/**
	 * default serialversionuid.
	 */
	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Integer stagingTableId, Date dateIn,
			String formatStatus, String documentType, Court court, Long xmlDocumentClobId, String userDisplayName) throws CreateException {
		setStagingTableId(stagingTableId);
		setDateIn(dateIn);
		setFormatStatus(formatStatus);
		setDocumentType(documentType);
		setXmlDocumentClobId(xmlDocumentClobId);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(Integer stagingTableId, Date dateIn,
			String formatStatus, String documentType, Court court, Long xmlDocumentClobId, String userDisplayName) throws CreateException {
		setCourt(court);
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract Integer getCppFormattingId();

	public abstract void setCppFormattingId(Integer cppFormattingId);

	public abstract Integer getStagingTableId();

	public abstract void setStagingTableId(Integer stagingTableId);

	public abstract Date getDateIn();

	public abstract void setDateIn(Date dateIn);

	public abstract String getFormatStatus();

	public abstract void setFormatStatus(String formatStatus);

	public abstract String getDocumentType();

	public abstract void setDocumentType(String documentType);

	public abstract Integer getCourtId();

	public abstract void setCourtId(Integer courtId);

	public abstract Long getXmlDocumentClobId();

	public abstract void setXmlDocumentClobId(Long xmlDocumentClobId);

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
	
	public abstract String getErrorMessage();

	public abstract void setErrorMessage(String errorMessage);
	
	// ------------------------------CMR
	// Fields------------------------------------
	public abstract Court getCourt();
	public abstract void setCourt(Court court);

}