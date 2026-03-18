package uk.gov.courtservice.xhibit.business.entities.prosecutorrefsolfirm;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm;
import uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency.CaseProsecutorAgency;

public interface ProsecutorRefSolFirm extends CSEntityLocal {

	/**
	 * Returns the prosecutorRefSolFirmId
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the prosecutorRefSolFirmId
	 */
	public java.lang.Integer getProsecutorRefSolFirmId();

	/**
	 * Sets the prosecutorRefSolFirmId
	 * 
	 * @param java.lang.Integer
	 *            the new prosecutorRefSolFirmId value
	 */
	public void setProsecutorRefSolFirmId(java.lang.Integer prosecutorRefSolFirmId);

	/**
	 * Returns the crestCpfId
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the crestCpfId
	 */
	public java.lang.Integer getCrestCpfId();

	/**
	 * Sets the crestCpfId
	 * 
	 * @param java.lang.Integer
	 *            the new crestCpfId value
	 */
	public void setCrestCpfId(java.lang.Integer crestCpfId);

	/**
	 * Returns the repType
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the repType
	 */
	public java.lang.String getRepType();

	/**
	 * Sets the repType
	 * 
	 * @param java.lang.String
	 *            the new repType value
	 */
	public void setRepType(java.lang.String repType);

	/**
	 * Returns the repStDate
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the repStDate
	 */
	public java.util.Date getRepStDate();

	/**
	 * Sets the repStDate
	 * 
	 * @param java.util.Date
	 *            the new repStDate value
	 */
	public void setRepStDate(java.util.Date repStDate);

	/**
	 * Returns the repEndDate
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the repEndDate
	 */
	public java.util.Date getRepEndDate();

	/**
	 * Sets the repEndDate
	 * 
	 * @param java.util.Date
	 *            the new repEndDate value
	 */
	public void setRepEndDate(java.util.Date repEndDate);

	/**
	 * Returns the refSolicitorFirmId
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the refSolicitorFirmId
	 */
	public java.lang.Integer getRefSolicitorFirmId();

	/**
	 * Sets the RefSolicitorFirmId
	 * 
	 * @param java.util.Integer
	 *            the new RefSolicitorFirmId value
	 */
	public void setRefSolicitorFirmId(java.lang.Integer refSolicitorFirmId);
	
	/**
	 * Returns the caseProsAgencyId
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the caseProsAgencyId
	 */
	public java.lang.Integer getCaseProsAgencyId();
	
	/**
	 * Sets the caseProsAgencyId Id
	 * 
	 * @param java.util.Integer
	 *            the new caseProsAgencyId value
	 */
	public void setCaseProsAgencyId(java.lang.Integer caseProsAgencyId);


	/**
	 * Returns the lastUpdateDate
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the lastUpdateDate
	 */
	public java.util.Date getLastUpdateDate();

	/**
	 * Sets the lastUpdateDate
	 * 
	 * @param java.util.Date
	 *            the new lastUpdateDate value
	 */
	public void setLastUpdateDate(java.util.Date lastUpdateDate);

	/**
	 * Returns the creationDate
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the creationDate
	 */
	public java.util.Date getCreationDate();

	/**
	 * Sets the creationDate
	 * 
	 * @param java.util.Date
	 *            the new creationDate value
	 */
	public void setCreationDate(java.util.Date creationDate);

	/**
	 * Returns the createdBy
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the createdBy
	 */
	public java.lang.String getCreatedBy();

	/**
	 * Sets the createdBy
	 * 
	 * @param java.lang.String
	 *            the new createdBy value
	 */
	public void setCreatedBy(java.lang.String createdBy);

	/**
	 * Returns the lastUpdatedBy
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the lastUpdatedBy
	 */
	public java.lang.String getLastUpdatedBy();

	/**
	 * Sets the lastUpdatedBy
	 * 
	 * @param java.lang.String
	 *            the new lastUpdatedBy value
	 */
	public void setLastUpdatedBy(java.lang.String lastUpdatedBy);

	/**
	 * Returns the version
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the version
	 */
	public java.lang.Integer getVersion();

	/**
	 * Sets the version
	 * 
	 * @param java.lang.Integer
	 *            the new version value
	 */
	public void setVersion(java.lang.Integer version);

	/**
	 * Returns the solicitorRef
	 * 
	 * @todo support OracleClob,OracleBlob on WLS
	 * @return the solicitorRef
	 */
	public java.lang.String getSolicitorRef();

	/**
	 * Sets the solicitorRef
	 * 
	 * @param java.lang.String
	 *            the new solicitorRef value
	 */
	public void setSolicitorRef(java.lang.String solicitorRef);

	public RefSolicitorFirm getRefSolicitorFirm();

	public void setRefSolicitorFirm(RefSolicitorFirm refSolicitorFirm);

	public CaseProsecutorAgency getCaseProsecutorAgency();

	public void setCaseProsecutorAgency(CaseProsecutorAgency caseProsecutorAgency);
	
	public String getObsInd();
	
	public void setObsInd(String obsInd);
	
	public Integer getLegalAidOrderId();
	
	public void setLegalAidOrderId(Integer legalAidOrderId);

}
