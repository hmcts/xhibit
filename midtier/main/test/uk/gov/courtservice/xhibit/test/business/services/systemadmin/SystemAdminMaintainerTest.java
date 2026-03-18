//package uk.gov.courtservice.xhibit.test.business.services.systemadmin;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtCriteria;
//
///**
// * This tests ALL of the System Admin (Read-Only) Maintainers.
// * <p>Not finished... it *will*, in time, test ALL of the SysAdmin maintainer objects.</p>
// *
// * @author Jem Marsh
// */
//public class SystemAdminMaintainerTest extends TestCase {
//
//  private static Logger logger = CSServices.getLogger(SystemAdminMaintainerTest.class);
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public SystemAdminMaintainerTest(String aString) {
//	super(aString);
//  }
//
//  private void debug(String message) {
//
//	  if( logger.isDebugEnabled() ) {
//		  logger.debug( message );
//	 }
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  /**
//   * This tests that the code replaces a trailing splat ('*') with an asterix.
//   */
//  public void testWildcard() {
//
//	final String EXPECTED_RESULT = "ValueEndsWithPercent%";
//	final String USER_INPUT = "ValueEndsWithPercent*";
//	this.debug("START: testWildcard()");
//	try {
//		CourtCriteria criteria = new CourtCriteria();
//		criteria.setShortName(USER_INPUT); // Short name is allowed to be wild
//		this.debug("Criteria: " + criteria.toString());
//		String actual = criteria.getShortName();
//		this.debug("Does the actual (" + actual + ") equal the expected (" + EXPECTED_RESULT + ") result? ");
//		if (actual.equals(EXPECTED_RESULT)) {
//			this.debug("Yes!");
//		} else {
//			this.debug("NOT as expected!! Failure.");
//			fail();
//		}
//	 } catch (Exception anException) {
//		 this.debug("An Exception was thrown..." + anException.getMessage());
//		 fail();
//	 }
//	 this.debug("END: Test complete.");
//	 this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  /**
//   * We should expect a well-formed query out the end of the following test...
//   * <p>Something like: select object(o) from Court o  WHERE circuit = 'anything' AND crestCourtId = '123' and courtName LIKE 'Dave%'</p>
//   * <p>I have added an empty string and also a wildcard to test all possibilities.</p>
//   */
//  public void testQueryUtils() {
//	final String EXPECTED_RESULT = "select object(o) from Court o  WHERE o.courtName LIKE 'Dave%' AND o.circuit = 'anything' AND o.crestCourtId = '123'";
//	this.debug("START: testQueryUtils()");
//	try {
//		CourtCriteria criteria = new CourtCriteria();
//		criteria.setCircuit("anything"); // This tests the setAttribute/getAttribute code
//		criteria.setCourtSiteId("123");
//		criteria.setCrestCourtId(""); // This is not expected top appear in the query
//		criteria.setCourtName("Dave*");
//		this.debug("Criteria: " + criteria.toString());
//		String qlString = criteria.queryString();
//		this.debug("The query expected is: ");
//		this.debug("'" + EXPECTED_RESULT + "'");
//		this.debug("The query created was: ");
//		this.debug("'" + qlString + "'");
//		if (qlString.equals(EXPECTED_RESULT)) {
//			this.debug("which is nice.");
//		} else {
//			this.debug("which is NOT as expected!! Failure.");
//			fail();
//		}
//	 } catch (Exception anException) {
//		 this.debug("An Exception was thrown..." + anException.getMessage());
//		 fail();
//	 }
//	 this.debug("END: Test complete.");
//	 this.debug("");
//  }
//
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//
//   public void testCreateAddress()
//   {
//     this.debug("START: testCreateRefLegalRep()");
//     try{
//       AddressMaintainer maintainer = new AddressMaintainer();
//       AddressBasicValue basicValue = new AddressBasicValue();
//       basicValue.setAddress1("something new");
//       maintainer.create(basicValue);
//     }
//     catch(Exception e)
//     {
//       e.printStackTrace();
//     }
//
//
//   }
//
//
//
//}
//