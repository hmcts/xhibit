package uk.gov.courtservice.xhibit.test.business.services.systemadmin;

import java.util.Collection;
import java.util.Iterator;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.*;
/**
 * This tests the URN setter method on the Court criteria objects.
 *
 * @author Jem Marsh
 */

public class UrnCriteriaTest extends TestCase {

  private static Logger logger = null;

/* ----------------------------------------------------------------------------------------------------------------------------------------------------- */

  public UrnCriteriaTest(String aString) {
	super(aString);
  }

  protected void setUp() {
	  logger = CSServices.getLogger(UrnCriteriaTest.class);
  }

  protected void tearDown() {
  }

  private void debug(String message) {

	  if( logger.isDebugEnabled() ) {
		  logger.debug( message );
	 }
  }

/* ----------------------------------------------------------------------------------------------------------------------------------------------------- */

  /**
   * Setting the URN to '//CHELMS/A/01' should yield a Short Name of 'CHELMS'.
   */
  public void testCourtCriteria() {

	final String EXPECTED_NAME = "CHELMS";
	final String INPUT = "//CHELMS/A/01";
	this.debug("START: testCourtCriteria()");
	try {
		CourtCriteria criteria = new CourtCriteria();
		criteria.setUrn(INPUT);
		this.debug("Criteria: " + criteria.toString());
		String actualName = criteria.getShortName();
		this.debug("Does the actual Name (" + actualName + ") equal the expected (" + EXPECTED_NAME + ") result? ");
		if (actualName.equals(EXPECTED_NAME)) {
			this.debug("Yes!");
		} else {
			this.debug("NOT as expected!! Failure.");
			this.fail();
		}
	 } catch (Exception anException) {
		 this.debug("An Exception was thrown..." + anException.getMessage());
		 this.fail();
	 }
	 this.debug("END: Test complete.");
	 this.debug("");
  }

/* ----------------------------------------------------------------------------------------------------------------------------------------------------- */

  /**
   * Setting the URN to '//CHELMS' should yield a Short Name of 'CHELMS'.
   */
  public void testCourtCriteria2() {

	final String EXPECTED_NAME = "CHELMS";
	final String INPUT = "//CHELMS";
	this.debug("START: testCourtCriteria()");
	try {
		CourtCriteria criteria = new CourtCriteria();
		criteria.setUrn(INPUT);
		this.debug("Criteria: " + criteria.toString());
		String actualName = criteria.getShortName();
		this.debug("Does the actual Name (" + actualName + ") equal the expected (" + EXPECTED_NAME + ") result? ");
		if (actualName.equals(EXPECTED_NAME)) {
			this.debug("Yes!");
		} else {
			this.debug("NOT as expected!! Failure.");
			this.fail();
		}
	 } catch (Exception anException) {
		 this.debug("An Exception was thrown..." + anException.getMessage());
		 this.fail();
	 }
	 this.debug("END: Test complete.");
	 this.debug("");
  }

/* ----------------------------------------------------------------------------------------------------------------------------------------------------- */

  /**
   * Setting the URN to "//CHELMS/A/01" should yield a ShortName of 'CHELMS', a Site Code of 'A' and a Crest Room No of '01'.
   */
  public void testCourtRoomCriteria() {

	final String EXPECTED_ROOM_NO = "01";
	final String EXPECTED_SITE_CODE = "A";
	final String EXPECTED_NAME = "CHELMS";
	final String INPUT = "//CHELMS/A/01";
	this.debug("START: testCourtRoomCriteria()");
	try {
		CourtRoomCriteria criteria = new CourtRoomCriteria();
		criteria.setUrn(INPUT);
		this.debug("Criteria: " + criteria.toString());
		String actualRoomNo = criteria.getCrestCourtRoomNo();
		String actualSiteCode = criteria.getCourtSiteCode();
		String actualName = criteria.getShortName();
		this.debug("Does the actual (" + actualName + " & " + actualSiteCode + " & " + actualRoomNo + ") equal the expected (" + EXPECTED_NAME + " & " + EXPECTED_SITE_CODE + " & " + EXPECTED_ROOM_NO + ") result? ");
		if (actualName.equals(EXPECTED_NAME) && actualRoomNo.equals(EXPECTED_ROOM_NO) && actualSiteCode.equals(EXPECTED_SITE_CODE)) {
			this.debug("Yes!");
		} else {
			this.debug("NOT as expected!! Failure.");
			this.fail();
		}
	 } catch (Exception anException) {
		 this.debug("An Exception was thrown..." + anException.getMessage());
		 this.fail();
	 }
	 this.debug("END: Test complete.");
	 this.debug("");
  }

/* ----------------------------------------------------------------------------------------------------------------------------------------------------- */

  /**
   * Setting the URN to "//CHELMS/A/01" should yield a ShortName of 'CHELMS' & a Court Code of 'A'.
   */
  public void testCourtSiteCriteria() {

	final String EXPECTED_SITE_CODE = "A";
	final String EXPECTED_NAME = "CHELMS";
	final String INPUT = "//CHELMS/A/01";
	this.debug("START: testCourtRoomCriteria()");
	try {
		CourtRoomCriteria criteria = new CourtRoomCriteria();
		criteria.setUrn(INPUT);
		this.debug("Criteria: " + criteria.toString());
		String actualSiteCode = criteria.getCourtSiteCode();
		String actualName = criteria.getShortName();
		this.debug("Does the actual (" + actualName + " & " + actualSiteCode + ") equal the expected (" + EXPECTED_NAME + " & " + EXPECTED_SITE_CODE + ") result? ");
		if (actualName.equals(EXPECTED_NAME) && actualSiteCode.equals(EXPECTED_SITE_CODE)) {
			this.debug("Yes!");
		} else {
			this.debug("NOT as expected!! Failure.");
			this.fail();
		}
	 } catch (Exception anException) {
		 this.debug("An Exception was thrown..." + anException.getMessage());
		 this.fail();
	 }
	 this.debug("END: Test complete.");
	 this.debug("");
  }

/* ----------------------------------------------------------------------------------------------------------------------------------------------------- */

  /**
   * Setting the URN to "//CHELMS/A" (different to that intest above) should yield a Court Code of 'A'.
   */
  public void testCourtSiteCriteria2() {

	final String EXPECTED_RESULT = "A";
	final String INPUT = "//CHELMS/A";
	this.debug("START: testCourtSiteCriteria2()");
	try {
		CourtSiteCriteria criteria = new CourtSiteCriteria();
		criteria.setUrn(INPUT);
		this.debug("Criteria: " + criteria.toString());
		String actual = criteria.getCourtSiteCode();
		this.debug("Does the actual (" + actual + ") equal the expected (" + EXPECTED_RESULT + ") result? ");
		if (actual.equals(EXPECTED_RESULT)) {
			this.debug("Yes!");
		} else {
			this.debug("NOT as expected!! Failure.");
			this.fail();
		}
	 } catch (Exception anException) {
		 this.debug("An Exception was thrown..." + anException.getMessage());
		 this.fail();
	 }
	 this.debug("END: Test complete.");
	 this.debug("");
  }

/* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
}
