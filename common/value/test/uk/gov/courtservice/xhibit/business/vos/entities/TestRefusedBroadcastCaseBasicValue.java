package uk.gov.courtservice.xhibit.business.vos.entities;


import junit.framework.TestCase;

/**
 * <p>
 * Title: RefusedBroadcastCaseBasicValue test
 * </p>
 * <p>
 * Description: RefusedBroadcastCaseBasicValue is intended to represent 
 * refused broadcast case basic values in the refusedbroadcastcase table.
 * </p>
 * 
 */

public class TestRefusedBroadcastCaseBasicValue  extends TestCase {
	
	 private static Integer id = 1;
	 private static Integer version = 2;
	 private static Integer teleApp= 50;
	 private static Integer caseId = 300;
	 
	 public TestRefusedBroadcastCaseBasicValue(String s)	 {
	   super(s);
     }
	 
	 public void testConstructor() {
		 RefusedBroadcastCaseBasicValue bv = new RefusedBroadcastCaseBasicValue(id, version);
		 assertEquals(id, bv.getId());
		 assertEquals(version, bv.getVersion());
	 }
	
	 public void testGettersAndSetters() {
		 RefusedBroadcastCaseBasicValue bv = new RefusedBroadcastCaseBasicValue(id, version);
		 bv.setCaseId(caseId);
		 bv.setTeleAppRefusedReasonId(teleApp);
		 assertEquals(caseId, bv.getCaseId());
		 assertEquals(teleApp, bv.getTeleAppRefusedReasonId());
		 assertEquals(id, bv.getId());
		 assertEquals(version, bv.getVersion());
	 }
		
}
