package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Migrate Case Basic Value.
 * 
 * <p>
 * Copyright: Copyright (c) 2026
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Luke Gittins
 * @version 1.0
 */
public class MigrateCaseBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	private Integer migrateCaseId;
	private Integer caseId;
	private String migrated;
	private String migrationTo; 
	private String migrationToUrn; 
	private Date migrationDate;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;
	private String lastUpdatedBy;


    public MigrateCaseBasicValue() {
    }

    public MigrateCaseBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
	public Integer getMigrateCaseId() {
		return migrateCaseId;
	}

	public void setMigrateCaseId(Integer migrateCaseId) {
		this.migrateCaseId = migrateCaseId;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public String getMigrated() {
		return migrated;
	}

	public void setMigrated(String migrated) {
		this.migrated = migrated;
	}

	public String getMigrationTo() {
		return migrationTo;
	}

	public void setMigrationTo(String migrationTo) {
		this.migrationTo = migrationTo;
	}

	public String getMigrationToUrn() {
		return migrationToUrn;
	}

	public void setMigrationToUrn(String migrationToUrn) {
		this.migrationToUrn = migrationToUrn;
	}

	public Date getMigrationDate() {
		return migrationDate;
	}

	public void setMigrationDate(Date migrationDate) {
		this.migrationDate = migrationDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
}