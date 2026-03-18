package uk.gov.courtservice.xhibit.business.entities.migration;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface MigrateCase extends CSEntityLocal {
    
		public java.lang.Integer getMigrateCaseId();
		public void setMigrateCaseId(java.lang.Integer migrateCaseId);
		public java.lang.Integer getCaseId();
		public void setCaseId(java.lang.Integer caseId);
		public java.lang.String getMigrated();
		public void setMigrated(java.lang.String migrated);
		public java.lang.String getMigrationTo();
		public void setMigrationTo(java.lang.String migrationTo);
		public java.lang.String getMigrationToUrn();
		public void setMigrationToUrn(java.lang.String migrationToUrn);
		public java.util.Date getMigrationDate();
		public void setMigrationDate(java.util.Date migrationDate);
		public java.util.Date getLastUpdateDate();
		public void setLastUpdateDate(java.util.Date lastUpdateDate);
		public java.util.Date getCreationDate();
		public void setCreationDate(java.util.Date creationDate);
		public java.lang.String getCreatedBy();
		public void setCreatedBy(java.lang.String createdBy);
		public java.lang.String getLastUpdatedBy();
		public void setLastUpdatedBy(java.lang.String lastUpdatedBy);
}