package uk.gov.courtservice.xhibit.business.entities.migration;

import java.util.Collection;

public class MigrateUtils {
		
	public MigrationDetail getMigrationDetails(int caseId) {
		
		MigrateCaseMaintainer migrateCaseMaintainer = new MigrateCaseMaintainer();
		try {
			Collection<MigrateCase> caze = migrateCaseMaintainer.findByCaseId(caseId);
			
			if(caze == null) {
				return null;
			}
			
			if (!caze.isEmpty()) {
				MigrateCase entry = caze.iterator().next();
				
				MigrationDetail migrationDetail = new MigrationDetail(entry.getMigrationTo(), true);
					
				return migrationDetail;
			} 
			
		} catch(Exception e) {
			return null;
		}
		return null;
	}
	
	
	public class MigrationDetail {
				
		public MigrationDetail(String migrationTo, boolean isMigrated) {
			super();
			this.migrationTo = migrationTo;
			this.isMigrated = isMigrated;
		}

		public String migrationTo;
		public boolean isMigrated;
		
		public String getMigrationTo() {
			return migrationTo;
		}
		
		public void setMigrationTo(String migrationTo) {
			this.migrationTo = migrationTo;
		}
		
		public boolean isMigrated() {
			return isMigrated;
		}
		
		public void setMigrated(boolean isMigrated) {
			this.isMigrated = isMigrated;
		}
	}

}
