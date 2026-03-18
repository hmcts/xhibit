package uk.gov.courtservice.xhibit.business.entities.cppformattingmerge;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.cppformatting.CppFormatting;

/**
 * Represents data in XHB_CPP_FORMATTING_MERGE.
 * @author waltersn
 *
 */
public interface CppFormattingMerge extends CSEntityLocal {

	public Integer getCppFormattingMergeId();

	public void setCppFormattingMergeId(Integer cppFormattingMergeId);

	public Integer getCppFormattingId();

	public void setCppFormattingId(Integer cppFormattingId);

	public Integer getFormattingId();

	public void setFormattingId(Integer formattingId);

	public Long getXhibitClobId();
	
	public void setXhibitClobId(Long xhibitClobId);
	
	public Integer getCourtId();

	public void setCourtId(Integer courtId);

	public String getLanguage();

	public void setLanguage(String language);

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
	
	public CppFormatting getCppFormatting();
	
	public void setCppFormatting(CppFormatting cppFormatting);
}