package uk.gov.courtservice.xhibit.business.services.migration;

import java.io.Serializable;

/**
 * <p>
 * Title: MigrationDetail
 * </p>
 * <p>
 * Description: Object to hold migration specific fields
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
public class MigrationDetail implements Serializable {
		
	private static final long serialVersionUID = 1L;
	
	public String migrationTo;
	public boolean isMigrated;
	
	public MigrationDetail(String migrationTo, boolean isMigrated) {
		super();
		this.migrationTo = migrationTo;
		this.isMigrated = isMigrated;
	}

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
