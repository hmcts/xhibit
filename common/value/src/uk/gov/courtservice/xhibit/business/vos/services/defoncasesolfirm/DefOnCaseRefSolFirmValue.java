package uk.gov.courtservice.xhibit.business.vos.services.defoncasesolfirm;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseRefSolFirmBasicValue;

/**
 * <p>
 * Title: XhbDefOnCaseRefSolFirmBasicValue
 * </p>
 * <p>
 * Description: This value object composes 3 value objects that contain
 * updatable data from XHIBIT. The internal value obejcts is
 * DefOnCaseRefSolFirmBasicValue.
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

public class DefOnCaseRefSolFirmValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private DefOnCaseRefSolFirmBasicValue defOnCaseRSFBV;

	private Integer defOnCaseRefSolFirmId;
	private Integer defendantOnCaseId;
	private Integer refSolicitorFirmId;
	private Integer crestCpfId;
	private String repType;
	private Date repStDate;
	private Date repEndDate;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;
	private String lastUpdatedBy;
	private Integer version;
	private String solicitorRef;
	//used for the defendantAppellantTab
	private String obsInd;
	private Integer legalAidOrderId;

	public DefOnCaseRefSolFirmValue() {
	}

	public DefOnCaseRefSolFirmValue(Integer defOnCaseRefSolFirmId, Integer defendantOnCaseId,
			Integer refSolicitorFirmId, Integer crestCpfId, String repType, Date repStDate, Date repEndDate,
			Date lastUpdateDate, Date creationDate, String createdBy, String lastUpdatedBy, Integer version,
			String solicitorRef, String obsInd, Integer legalAidOrderId) {
		setDefOnCaseRefSolFirmId(defOnCaseRefSolFirmId);
		setDefendantOnCaseId(defendantOnCaseId);
		setRefSolicitorFirmId(refSolicitorFirmId);
		setCrestCpfId(crestCpfId);
		setRepType(repType);
		setRepStDate(repStDate);
		setRepEndDate(repEndDate);
		setLastUpdateDate(lastUpdateDate);
		setCreationDate(creationDate);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setVersion(version);
		setSolicitorRef(solicitorRef);
		setObsInd(obsInd);
		setLegalAidOrderId(legalAidOrderId);
	}

	public Integer getPrimaryKey() {
		return getDefOnCaseRefSolFirmId();
	}

	public Integer getDefOnCaseRefSolFirmId() {
		return this.defOnCaseRefSolFirmId;
	}

	public void setDefOnCaseRefSolFirmId(Integer defOnCaseRefSolFirmId) {
		this.defOnCaseRefSolFirmId = defOnCaseRefSolFirmId;
	}

	public Integer getDefendantOnCaseId() {
		return this.defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
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

	public DefOnCaseRefSolFirmBasicValue getDefOnCaseRSFBV() {
		return defOnCaseRSFBV;
	}

	public void setDefOnCaseRSFBV(DefOnCaseRefSolFirmBasicValue defOnCaseRSFBV) {
		this.defOnCaseRSFBV = defOnCaseRSFBV;
	}

	public String getObsInd() {
		return obsInd;
	}

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
