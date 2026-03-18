package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class CaseProsecutorAgencyBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;

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

	public CaseProsecutorAgencyBasicValue() {
	}

	public CaseProsecutorAgencyBasicValue(Integer id, Integer version) {
		super(id, version);
	}

	public CaseProsecutorAgencyBasicValue(Integer caseProsAgencyId, String prosecutorType, Integer caseId,
			Integer refProsecutorAgencyId, Date lastUpdateDate, Date creationDate, String createdBy,
			String lastUpdatedBy, Integer version, String respondentStatus) {

		this(caseProsAgencyId, version);
		setCaseProsAgencyID(caseProsAgencyId);
		setProsecutorType(prosecutorType);
		setCaseID(caseId);
		setRefProsecutorAgencyID(refProsecutorAgencyId);
		setLastUpdateDate(lastUpdateDate);
		setCreationDate(creationDate);
		setLastUpdatedBy(lastUpdatedBy);
		setRespondentStatus(respondentStatus);
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
}