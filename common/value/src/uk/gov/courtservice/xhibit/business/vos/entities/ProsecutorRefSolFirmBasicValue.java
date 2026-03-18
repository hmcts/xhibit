package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: XhbProsecutorRefSolFirmBasicValue
 * </p>
 * <p>
 * Description: This value object composes 3 value objects that contain
 * data from XHIBIT. The internal value objects is
 * ProsecutorRefSolFirmBasicValue.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Chris Kudzin
 * @version 1.0
 */

public class ProsecutorRefSolFirmBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private Integer prosecutorRefSolFirmId;
	private Integer refSolicitorFirmId;
	private Integer crestCpfId;
	private String repType;
	private Date repStDate;
	private Date repEndDate;
	private Integer caseProsAgencyId;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;
	private String lastUpdatedBy;
	private Integer version;
	private String solicitorRef;
	private String obsInd;
	private Integer legalAidOrderId;

	public ProsecutorRefSolFirmBasicValue() {
	}

	public ProsecutorRefSolFirmBasicValue(Integer prosecutorRefSolFirmId, Integer version) {
		super(prosecutorRefSolFirmId, version);
	}

	public ProsecutorRefSolFirmBasicValue(Integer prosecutorRefSolFirmId, Integer refSolicitorFirmId,
			Integer crestCpfId, String repType, Date repStDate, Date repEndDate, Integer caseProsAgencyId,
			Date lastUpdateDate, Date creationDate, String createdBy, String lastUpdatedBy, Integer version,
			String solicitorRef, String obsInd, Integer legalAidOrderId) {

		this(prosecutorRefSolFirmId, version);
		setProsecutorRefSolFirmId(prosecutorRefSolFirmId);
		setRefSolicitorFirmId(refSolicitorFirmId);
		setCrestCpfId(crestCpfId);
		setRepType(repType);
		setRepStDate(repStDate);
		setRepEndDate(repEndDate);
		setCaseProsAgencyId(caseProsAgencyId);
		setLastUpdateDate(lastUpdateDate);
		setCreationDate(creationDate);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setSolicitorRef(solicitorRef);
		setObsInd(obsInd);
		setLegalAidOrderId(legalAidOrderId);
	}

	public Integer getPrimaryKey() {
		return getProsecutorRefSolFirmId();
	}

	public Integer getProsecutorRefSolFirmId() {
		return this.prosecutorRefSolFirmId;
	}

	public void setProsecutorRefSolFirmId(Integer prosecutorRefSolFirmId) {
		this.prosecutorRefSolFirmId = prosecutorRefSolFirmId;
	}

	public Integer getRefSolicitorFirmId() {
		return this.refSolicitorFirmId;
	}

	public void setRefSolicitorFirmId(Integer refSolicitorFirmId) {
		this.refSolicitorFirmId = refSolicitorFirmId;
	}

	public Integer getCrestCpfId() {
		return this.crestCpfId;
	}

	public void setCrestCpfId(Integer crestCpfId) {
		this.crestCpfId = crestCpfId;
	}

	public String getRepType() {
		return this.repType;
	}

	public void setRepType(String repType) {
		this.repType = repType;
	}

	public Date getRepStDate() {
		return this.repStDate;
	}

	public void setRepStDate(Date repStDate) {
		this.repStDate = repStDate;
	}

	public Date getRepEndDate() {
		return this.repEndDate;
	}

	public void setRepEndDate(Date repEndDate) {
		this.repEndDate = repEndDate;
	}

	public Integer getCaseProsAgencyId() {
		return this.caseProsAgencyId;
	}

	public void setCaseProsAgencyId(Integer caseProsAgencyId) {
		this.caseProsAgencyId = caseProsAgencyId;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getSolicitorRef() {
		return this.solicitorRef;
	}

	public void setSolicitorRef(String solicitorRef) {
		this.solicitorRef = solicitorRef;
	}

	/**
	 * @return the obsInd
	 */
	public String getObsInd() {
		return obsInd;
	}

	/**
	 * @param obsInd the obsInd to set
	 */
	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
	
	/**
	 * @return the legalAidOrderId
	 */
	public Integer getLegalAidOrderId() {
		return legalAidOrderId;
	}

	/**
	 * @param legalAidOrderId the legalAidOrderId to set
	 */
	public void setLegalAidOrderId(Integer legalAidOrderId) {
		this.legalAidOrderId = legalAidOrderId;
	}

}