package uk.gov.courtservice.xhibit.business.entities.d20offencelink;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

/**
 * Represents data in XHB_D20_OFFENCE_LINK.
 * @author atwells
 *
 */
public interface D20OffenceLink extends CSEntityLocal {
	public Integer getD20OffenceLinkId();

	public void setD20OffenceLinkId(Integer d20OffenceLinkId);
	
	public Integer getDefendantOnCaseId();

	public void setDefendantOnCaseId(Integer defendantOnCaseId);
	
	public Integer getSeqNo();

	public void setSeqNo(Integer seqNo);
	
	public String getDvlaOffenceCode();

	public void setDvlaOffenceCode(String dvlaOffenceCode);

	public Integer getRefOffenceId();

	public void setRefOffenceId(Integer refOffenceId);
	
	public Date getConvictionDate();

	public void setConvictionDate(Date convictionDate);

	public String getIntD20();

	public void setIntD20(String intD20);

	public Date getIntD20Date();

	public void setIntD20Date(Date intD20Date);
	
	public String getFinalD20();

	public void setFinalD20(String finalD20);

	public Date getFinalD20Date();

	public void setFinalD20Date(Date finalD20Date);

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