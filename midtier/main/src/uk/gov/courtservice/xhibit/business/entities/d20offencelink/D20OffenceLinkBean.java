package uk.gov.courtservice.xhibit.business.entities.d20offencelink;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class D20OffenceLinkBean extends CSEntityBean implements EntityBean {

	/**
	 * default serialversionuid.
	 */
	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Integer defendantOnCaseId, Integer seqNo, String dvlaOffenceCode, Integer refOffenceId, Date convictionDate, String intD20, Date intD20Date, String finalD20, Date finalD20Date, String userDisplayName) throws CreateException {
		setDefendantOnCaseId(defendantOnCaseId);
		setSeqNo(seqNo);
		setDvlaOffenceCode(dvlaOffenceCode);
		setRefOffenceId(refOffenceId);
		setConvictionDate(convictionDate);
		setIntD20(intD20);
		setIntD20Date(intD20Date);
		setFinalD20(finalD20);
		setFinalD20Date(finalD20Date);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(Integer defendantOnCaseId, Integer seqNo, String dvlaOffenceCode, Integer refOffenceId, Date convictionDate, String intD20, Date intD20Date, String finalD20, Date finalD20Date, String userDisplayName) throws CreateException {
		// Empty
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract Integer getD20OffenceLinkId();

	public abstract void setD20OffenceLinkId(Integer d20OffenceLinkId);
	
	public abstract Integer getDefendantOnCaseId();

	public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);
	
	public abstract Integer getSeqNo();

	public abstract void setSeqNo(Integer seqNo);
	
	public abstract String getDvlaOffenceCode();

	public abstract void setDvlaOffenceCode(String dvlaOffenceCode);
	
	public abstract Date getConvictionDate();
	
	public abstract void setConvictionDate(Date convictionDate);

	public abstract Integer getRefOffenceId();

	public abstract void setRefOffenceId(Integer refOffenceId);
	
	public abstract String getIntD20();

	public abstract void setIntD20(String intD20);

	public abstract Date getIntD20Date();

	public abstract void setIntD20Date(Date intD20Date);
	
	public abstract String getFinalD20();

	public abstract void setFinalD20(String finalD20);

	public abstract Date getFinalD20Date();

	public abstract void setFinalD20Date(Date finalD20Date);
	
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