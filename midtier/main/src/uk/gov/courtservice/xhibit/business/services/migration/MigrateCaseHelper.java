package uk.gov.courtservice.xhibit.business.services.migration;

import java.util.Collection;

import uk.gov.courtservice.xhibit.business.entities.migration.MigrateCase;
import uk.gov.courtservice.xhibit.business.entities.migration.MigrateCaseMaintainer;

/**
 * <p>
 * Title: MigrateCaseHelper
 * </p>
 * <p>
 * Description: Set of methods interacting with migrated cases in the DB
 * </p>
 * <p>
 * Copyright: Copyright (c) 2026
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Owain Greener, Ben Morris
 * @version 1.0
 */
public class MigrateCaseHelper {
	
	private static final String ARM = "ARM";
	private static final String CP = "ARM & CP";
	private static final String CP_AREA = "CP";
	private static final String COMMON_PLATFORM_AREA = "Common Platform";
	private static final String MIGRATED_MESSAGE_EDIT = "This case has migrated to %s and can no longer be amended";
	private static final String MIGRATED_MESSAGE_DELETE = "This case has migrated to %s and can no longer be deleted";
	private static final String MIGRATED_MESSAGE_READONLY = "This case has migrated to %s. It will open as Read Only";
	private static final String MIGRATED_MESSAGE_LISTED = "This case has migrated to %s and can no longer be listed in XHIBIT";
	private static final String MIGRATED_MESSAGE_LINKED = "This case has migrated to %s and can no longer be linked/unlinked";
	private static final String VIEW_COURT_LOG_MESSAGE = " (Migrated to %s)";
	private static final String VIEW_COURT_LOG_MESSAGE_WITH_URN = " (Migrated to %s - URN %s)";
	
	private MigrateCaseMaintainer migrateCaseMaintainer;
	
	public MigrateCaseHelper() { 
		migrateCaseMaintainer = new MigrateCaseMaintainer();
	}
	
	public MigrationDetail getMigrationDetails(int caseId, MigrationMessageType messageType) {
		try {
			MigrateCase caze = getMigratedCase(caseId);
			
			if (caze != null) {
				String area = getAreaName(caze.getMigrationTo());
				String message;

				if (area != null) {
					switch (messageType) {
						case EDIT:
							message = String.format(MIGRATED_MESSAGE_EDIT, area);
							break;
						case DELETE:
							message = String.format(MIGRATED_MESSAGE_DELETE, area);
							break;
						case READONLY:
							message = String.format(MIGRATED_MESSAGE_READONLY, area);
							break;
						case LISTED:
							message = String.format(MIGRATED_MESSAGE_LISTED, area);
							break;
						case LINKED:
							message = String.format(MIGRATED_MESSAGE_LINKED, area);
							break;
						case COURT_LOG:
							if (area == COMMON_PLATFORM_AREA) {
								area = CP_AREA;
							}
							if (caze.getMigrationToUrn() != null) {
								message = String.format(VIEW_COURT_LOG_MESSAGE_WITH_URN, area, caze.getMigrationToUrn());
							} else {
								message = String.format(VIEW_COURT_LOG_MESSAGE, area);
							}
							break;
						default:
							message = null;
					}
					return new MigrationDetail(message, true);	
				}		
			}
			return null;
		} catch(Exception e) {
			return null;
		}
	}
	
	public boolean isCaseMigratedAndInReadOnly(boolean isInEditMode, int caseId) {
		// Check if viewing in read only
		if(!isInEditMode) {
			MigrateCase migratedCase = getMigratedCase(caseId);
			if (migratedCase != null) {
				return true; // Case migrated and in read only
			}
			return false; // Case is not migrated
		}
		return false; // Viewing in edit mode
	}
	
	public boolean isCaseMigrated(int caseId) {
		return getMigratedCase(caseId) != null;
	}
	
	private MigrateCase getMigratedCase(int caseId) {
		try {
			Collection<MigrateCase> caze = migrateCaseMaintainer.findByCaseId(caseId);
			
			if (caze != null && !caze.isEmpty()) {
				return caze.iterator().next();
			}
			
			return null;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	private String getAreaName(String migrationTo) {
		if (ARM.equals(migrationTo)) {
			return ARM;
		}
		if (CP.equals(migrationTo)) {
			return COMMON_PLATFORM_AREA;
		}
		return null;
	}

}
