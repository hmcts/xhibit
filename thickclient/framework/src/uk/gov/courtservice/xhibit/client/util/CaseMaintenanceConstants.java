package uk.gov.courtservice.xhibit.client.util;


/**
 * <p>
 * Title: Case Maintenance Constants
 * </p>
 * <p>
 * Description: Case Maintenance Constants contains the constants used in the Case Maintenance area 
 * (as a lot were defined twice in different files).
 * </p>
 * 
 */

public class CaseMaintenanceConstants {
	
	private CaseMaintenanceConstants() {
		//private constructor so that it can't be
		//initialised.
	}
	public static final String ERROR_IN = "Error in ";
	public static final String DATE_RECEIVED = "Date Received";
	public static final String BEFORE = "before";
	public static final String AFTER = "after";
	public static final String ON_BEFORE_DATE_RECEIVED = "Must be on/before Date Received";
	public static final String ON_AFTER_DATE_APPEAL = "Must be on/after Date Appeal Lodged";
	public static final String OBSOLETE_COURT = "Obsolete court";
	public static final String PACKAGE_NAME = "uk.gov.courtservice.xhibit.client.casemanagement";	
	public static final String MANDATORY_FIELD = "Mandatory Field";
	public static final String ON_BEFORE_SFT = "Must be on/before SFT date";
	public static final String ON_BEFORE_COMMITTAL = "Must be on/before Date of Committal";
	public static final String ON_AFTER_SFT = "Must be on/after SFT date";
	public static final String ON_AFTER_DATE_OF_COMMITTAL = "Must be on/after Date Of Committal";
	public static final String JPS_VAL = "^.{1,35}$";
	public static final String DATE_APPEAL_LODGED = "Date Appeal Lodged";
	public static final String IN_CARE = "In Care";
	public static final String INVALID_ENTRY = "Invalid Entry";



 }