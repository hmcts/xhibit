package uk.gov.courtservice.xhibit.business.vos.services.caseprosecutoragency;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseProsecutorAgencyBasicValue;

/**
 * <p>
 * Title: XhbCaseProsecutorAgency
 * </p>
 * <p>
 * Description: This value object composes 3 value objects that contain
 * updatable data from XHIBIT. The internal value obejcts is
 * CaseProsecutorAgencyBasicValue.
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

public class CaseProsecutorAgencyValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private CaseProsecutorAgencyBasicValue caseProsAgencyBV;

	private Integer caseProsAgencyId;
	private String prosecutorType;
	private Integer caseId;
	private Integer refProsecutorAgencyId;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;
	private String lastUpdatedBy;
	private Integer version;
	private String respondentStatus;
	private String obsInd;

	public CaseProsecutorAgencyValue() {
	}

	public CaseProsecutorAgencyValue(Integer caseProsAgencyId, String prosecutorType, Integer caseId,
			Integer refProsecutorAgencyId, Date lastUpdateDate, Date creationDate, String createdBy,
			String lastUpdatedBy, Integer version, String respondentStatus, String obsInd) {

		setCaseProsAgencyID(caseProsAgencyId);
		setProsecutorType(prosecutorType);
		setCaseID(caseId);
		setRefProsecutorAgencyID(refProsecutorAgencyId);
		setLastUpdateDate(lastUpdateDate);
		setCreationDate(creationDate);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setVersion(version);
		setRespondentStatus(respondentStatus);
		setObsInd(obsInd);
		
	}

	public Integer getPrimaryKey() {
		return getCaseProsAgencyID();
	}

	public Integer getCaseProsAgencyID() {
		return this.caseProsAgencyId;
	}

	public void setCaseProsAgencyID(Integer caseProsAgencyId) {
		this.caseProsAgencyId = caseProsAgencyId;
	}

	public Integer getCaseID() {
		return this.caseId;
	}

	public void setCaseID(Integer caseId) {
		this.caseId = caseId;
	}

	public void setProsecutorType(String prosecutorType) {
		this.prosecutorType = prosecutorType;
	}

	public String getProsecutorType() {
		return prosecutorType;
	}

	public Integer getRefProsecutorAgencyID() {
		return this.refProsecutorAgencyId;
	}

	public void setRefProsecutorAgencyID(Integer refProsecutorAgencyId) {
		this.refProsecutorAgencyId = refProsecutorAgencyId;
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

	public String getRespondentStatus() {
		return this.respondentStatus;
	}

	public void setRespondentStatus(String respondentStatus) {
		this.respondentStatus = respondentStatus;
	}

	public CaseProsecutorAgencyBasicValue getCaseProsAgencyBV() {
		return caseProsAgencyBV;
	}

	public void setCaseProsAgencyBV(CaseProsecutorAgencyBasicValue caseProsAgencyBV) {
		this.caseProsAgencyBV = caseProsAgencyBV;
	}
	
	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}

}