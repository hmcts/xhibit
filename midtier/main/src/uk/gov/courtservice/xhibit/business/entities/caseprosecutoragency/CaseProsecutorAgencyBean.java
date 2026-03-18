package uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class CaseProsecutorAgencyBean extends CSEntityBean implements EntityBean {
	private static final long serialVersionUID = 1L;

	/**
	 * Used in case creation process
	 * 
	 * @param prosecutorType
	 *            String
	 * @param caseId
	 *            Integer
	 * @param refProsecutorAgencyId
	 *            Integer
	 * @return Integer
	 * @throws CreateException
	 */
	public Integer ejbCreate(String prosecutorType, Integer caseId, Integer refProsecutorAgencyId, String userDisplayName)
			throws CreateException {
		setProsecutorType(prosecutorType);
		setCaseId(caseId);
		setRefProsecutorAgencyId(refProsecutorAgencyId);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(String prosecutorType, Integer caseId, Integer refProsecutorAgencyId, String userDisplayName)
			throws CreateException {
	}

	/**
	 * Used in case creation process for respondent
	 * 
	 * @param prosecutorType
	 *            String
	 * @param caseId
	 *            Integer
	 * @param refProsecutorAgencyId
	 *            Integer
	 * @return Integer
	 * @throws CreateException
	 */
	public Integer ejbCreate(String prosecutorType, Integer caseId, Integer refProsecutorAgencyId,
			String respondentStatus, String userDisplayName) throws CreateException {
		setProsecutorType(prosecutorType);
		setCaseId(caseId);
		setRefProsecutorAgencyId(refProsecutorAgencyId);
		setRespondentStatus(respondentStatus);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(String prosecutorType, Integer caseId, Integer refProsecutorAgencyId,
			String respondentStatus, String userDisplayName) throws CreateException {
	}

	public abstract java.lang.Integer getCaseProsAgencyId();

	public abstract void setCaseProsAgencyId(java.lang.Integer caseProsAgencyId);

	public abstract java.lang.String getProsecutorType();

	public abstract void setProsecutorType(java.lang.String prosecutorType);

	public abstract java.lang.Integer getCaseId();

	public abstract void setCaseId(java.lang.Integer caseId);

	public abstract java.lang.Integer getRefProsecutorAgencyId();

	public abstract void setRefProsecutorAgencyId(java.lang.Integer refProsecutorAgencyId);

	public abstract java.lang.String getCreatedBy();

	public abstract void setCreatedBy(java.lang.String createdBy);

	public abstract java.lang.String getLastUpdatedBy();

	public abstract void setLastUpdatedBy(java.lang.String lastUpdatedBy);

	public abstract java.lang.Integer getVersion();

	public abstract void setVersion(java.lang.Integer version);

	public abstract java.lang.String getRespondentStatus();

	public abstract void setRespondentStatus(java.lang.String respondentStatus);

	public abstract java.util.Collection getProsecutorRefSolFirms();

	public abstract void setProsecutorRefSolFirms(java.util.Collection prosecutorRefSolFirms);

	public abstract java.lang.String getObsInd();
	
	public abstract void setObsInd(java.lang.String obsInd);
}
