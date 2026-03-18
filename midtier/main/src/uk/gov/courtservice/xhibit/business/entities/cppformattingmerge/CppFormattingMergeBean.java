package uk.gov.courtservice.xhibit.business.entities.cppformattingmerge;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.cppformatting.CppFormatting;

abstract public class CppFormattingMergeBean extends CSEntityBean implements EntityBean {

	/**
	 * default serialversionuid.
	 */
	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Integer formattingId, Long xhibitClobId, String language, Court court, CppFormatting cppFormatting,
			String userDisplayName) throws CreateException {
		setFormattingId(formattingId);
		setXhibitClobId(xhibitClobId);
		setLanguage(language);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(Integer formattingId, Long xhibitClobId, String language, Court court, CppFormatting cppFormatting,
			String userDisplayName) throws CreateException {
		setCourt(court);
		setCppFormatting(cppFormatting);
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract Integer getCppFormattingMergeId();

	public abstract void setCppFormattingMergeId(Integer cppFormattingMergeId);

	public abstract Integer getCppFormattingId();

	public abstract void setCppFormattingId(Integer cppFormattingId);

	public abstract Integer getFormattingId();

	public abstract void setFormattingId(Integer formattingId);

	public abstract Long getXhibitClobId();
	
	public abstract void setXhibitClobId(Long xhibitClobId);
	
	public abstract Integer getCourtId();

	public abstract void setCourtId(Integer courtId);

	public abstract String getLanguage();

	public abstract void setLanguage(String language);

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
	
	// ------------------------------CMR
	// Fields------------------------------------
	public abstract Court getCourt();
	public abstract void setCourt(Court court);
	public abstract CppFormatting getCppFormatting();
	
	public abstract void setCppFormatting(CppFormatting cppFormatting);

}