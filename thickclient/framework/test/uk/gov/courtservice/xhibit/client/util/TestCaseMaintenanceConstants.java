package uk.gov.courtservice.xhibit.client.util;

import junit.framework.TestCase;

/**
 * Test the values that are in case Maintenance
 * @author waltersn
 *
 */
public class TestCaseMaintenanceConstants extends TestCase{
	
	private static final String ERROR_IN = "Error in ";
	private static final String DATE_RECEIVED = "Date Received";
	private static final String BEFORE = "before";
	private static final String AFTER = "after";
	private static final String ON_BEFORE_DATE_RECEIVED = "Must be on/before Date Received";
	private static final String ON_AFTER_DATE_APPEAL = "Must be on/after Date Appeal Lodged";
	private static final String OBSOLETE_COURT = "Obsolete court";
	private static final String PACKAGE_NAME = "uk.gov.courtservice.xhibit.client.casemanagement";	
	private static final String MANDATORY_FIELD = "Mandatory Field";
	private static final String ON_BEFORE_SFT = "Must be on/before SFT date";
	private static final String ON_BEFORE_COMMITTAL = "Must be on/before Date of Committal";
	private static final String ON_AFTER_SFT = "Must be on/after SFT date";
	private static final String ON_AFTER_DATE_OF_COMMITTAL = "Must be on/after Date Of Committal";
	private static final String JPS_VAL = "^.{1,35}$";
	private static final String DATE_APPEAL_LODGED = "Date Appeal Lodged";
	private static final String IN_CARE = "In Care";
	private static final String INVALID_ENTRY = "Invalid Entry";

	/**
	 * Test values are correct from the constants file.
	 */
	public void testValues() {
		
		assertTrue(ERROR_IN.equals(CaseMaintenanceConstants.ERROR_IN));
		assertTrue(DATE_RECEIVED.equals(CaseMaintenanceConstants.DATE_RECEIVED));
		assertTrue(BEFORE.equals(CaseMaintenanceConstants.BEFORE));
		assertTrue(AFTER.equals(CaseMaintenanceConstants.AFTER));
		assertTrue(ON_BEFORE_DATE_RECEIVED.equals(CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED));
		assertTrue(ON_AFTER_DATE_APPEAL.equals(CaseMaintenanceConstants.ON_AFTER_DATE_APPEAL));
		assertTrue(OBSOLETE_COURT.equals(CaseMaintenanceConstants.OBSOLETE_COURT));
		assertTrue(PACKAGE_NAME.equals(CaseMaintenanceConstants.PACKAGE_NAME));
		assertTrue(MANDATORY_FIELD.equals(CaseMaintenanceConstants.MANDATORY_FIELD));
		assertTrue(ON_BEFORE_SFT.equals(CaseMaintenanceConstants.ON_BEFORE_SFT));
		assertTrue(ON_BEFORE_COMMITTAL.equals(CaseMaintenanceConstants.ON_BEFORE_COMMITTAL));
		assertTrue(ON_BEFORE_COMMITTAL.equals(CaseMaintenanceConstants.ON_BEFORE_COMMITTAL));
		assertTrue(ON_AFTER_SFT.equals(CaseMaintenanceConstants.ON_AFTER_SFT));
		assertTrue(ON_AFTER_DATE_OF_COMMITTAL.equals(CaseMaintenanceConstants.ON_AFTER_DATE_OF_COMMITTAL));
		assertTrue(JPS_VAL.equals(CaseMaintenanceConstants.JPS_VAL));
		assertTrue(DATE_APPEAL_LODGED.equals(CaseMaintenanceConstants.DATE_APPEAL_LODGED));
		assertTrue(IN_CARE.equals(CaseMaintenanceConstants.IN_CARE));
		assertTrue(INVALID_ENTRY.equals(CaseMaintenanceConstants.INVALID_ENTRY));

	}

}
