package uk.gov.courtservice.xhibit.business.entities.migration;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class MigrateCaseBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer migrateCaseId, Integer caseId,
			String migrated, String migrationTo, String migrationToUrn, Date migrationDate,
			String userDisplayName) throws CreateException {
		setMigrateCaseId(migrateCaseId);
		setCaseId(caseId);
		setMigrated(migrated);
		setMigrationTo(migrationTo);
		setMigrationToUrn(migrationToUrn);
		setMigrationDate(migrationDate);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer migrateCaseId, Integer caseId,
			String migrated, String migrationTo, String migrationToUrn, Date migrationDate,
			String userDisplayName) throws CreateException {
	}

	public abstract Integer getMigrateCaseId();
	public abstract void setMigrateCaseId(Integer migrateCaseId);
	public abstract Integer getCaseId();
	public abstract void setCaseId(Integer caseId);
	public abstract String getMigrated();
	public abstract void setMigrated(String migrated);
	public abstract String getMigrationTo();
	public abstract void setMigrationTo(String migrationTo);
	public abstract String getMigrationToUrn();
	public abstract void setMigrationToUrn(String migrationToUrn);
	public abstract Date getMigrationDate();
	public abstract void setMigrationDate(Date migrationDate);
	public abstract Date getLastUpdateDate();
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	public abstract Date getCreationDate();
	public abstract void setCreationDate(Date creationDate);
	public abstract String getCreatedBy();
	public abstract void setCreatedBy(String createdBy);
	public abstract String getLastUpdatedBy();
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
}