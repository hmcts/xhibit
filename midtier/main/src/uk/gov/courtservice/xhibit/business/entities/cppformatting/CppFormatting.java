package uk.gov.courtservice.xhibit.business.entities.cppformatting;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.court.Court;

/**
 * Represents data in XHB_CPP_FORMATTING.
 * @author waltersn
 *
 */
public interface CppFormatting extends CSEntityLocal {
	public Integer getCppFormattingId();

	public void setCppFormattingId(Integer cppFormattingId);

	public Integer getStagingTableId();

	public void setStagingTableId(Integer stagingTableId);

	public Date getDateIn();

	public void setDateIn(Date dateIn);

	public String getFormatStatus();

	public void setFormatStatus(String formatStatus);

	public String getDocumentType();

	public void setDocumentType(String documentType);

	public Integer getCourtId();

	public void setCourtId(Integer courtId);

	public Long getXmlDocumentClobId();

	public void setXmlDocumentClobId(Long xmlDocumentClobId);

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
	
	public Court getCourt();
	
	public void setCourt(Court court);
	
	public String getErrorMessage();

	public void setErrorMessage(String errorMessage);
}