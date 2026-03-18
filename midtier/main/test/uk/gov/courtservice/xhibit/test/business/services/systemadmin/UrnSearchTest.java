//package uk.gov.courtservice.xhibit.test.business.services.systemadmin;
//
//import java.util.Collection;
//import java.util.Iterator;
//import junit.framework.TestCase;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.systemadmin.*;
//import uk.gov.courtservice.xhibit.business.vos.entities.*;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.*;
//import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefController;
///**
// * This tests the URN setter method on the Court criteria objects.
// *
// * @author Jem Marsh
// */
//
//public class UrnSearchTest extends TestCase {
//
//  private static Logger logger = null;
//  private BisRefController bisRefController = null;
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public UrnSearchTest(String aString) {
//    super(aString);
//  }
//
//  protected void setUp() {
//      logger = CSServices.getLogger(UrnSearchTest.class);
//  }
//
//  protected void tearDown() {
//  }
//
//  private void debug(String message) {
//
//      if( logger.isDebugEnabled() ) {
//          logger.debug( message );
//     }
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  private BisRefController getBisRefController() {
//
//    if (this.bisRefController == null ) {
//      this.bisRefController = (BisRefController) CSServices.getEJBServices().createRemoteSession(BisRefControllerHome.class);
//    }
//    return this.bisRefController;
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindCourts() {
//
//    this.debug("START: testFindCourts() by URN == //OLDBA ");
//    try {
//        String urn = "//OLDBA";
//        CourtCriteria criteria = new CourtCriteria();
//        criteria.setUrn(urn);
//        Collection data = this.getBisRefController().findCourts(criteria);
//
//        if (data.isEmpty()) {
//            this.debug("No Courts found");
//        } else {
//            int numFound = data.size();
//            this.debug("*** " + numFound + " Courts found...");
//            Iterator allData = data.iterator();
//            CourtBasicValue eachValue = null;
//            while (allData.hasNext()) {
//                eachValue = (CourtBasicValue) allData.next();
//                this.debug("name = " + eachValue.getCourtName() + ".");
//            }
//            if (numFound != 1) {
//                this.debug("Failure: URNs should UNIQUELY identify the Court we are searching for.");
//                fail();
//            }
//
//        }
//
//     } catch (Exception anException) {
//         this.debug("An Exception was thrown..." + anException.getMessage());
//         anException.printStackTrace();
//         fail();
//     }
//     this.debug("END: Test complete.");
//     this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindCourtSites() {
//
//    this.debug("START: testFindCourtSites() by URN == //OLDBA/I/2 ");
//    try {
//        String urn = "//OLDBA/I";
//        CourtSiteCriteria criteria = new CourtSiteCriteria();
//        criteria.setUrn(urn);
//        Collection data = this.getBisRefController().findCourtSites(criteria);
//
//        if (data.isEmpty()) {
//            this.debug("No Court Sites found");
//        } else {
//            int numFound = data.size();
//            this.debug("*** " + numFound + " Court Sites found...");
//            Iterator allData = data.iterator();
//            CourtSiteBasicValue eachValue = null;
//            while (allData.hasNext()) {
//                eachValue = (CourtSiteBasicValue) allData.next();
//                this.debug("name = " + eachValue.getCourtSiteName() + ".");
//            }
//            if (numFound != 1) {
//                this.debug("Failure: URNs should UNIQUELY identify the Court Site we are searching for.");
//                fail();
//            }
//
//        }
//
//     } catch (Exception anException) {
//         this.debug("An Exception was thrown..." + anException.getMessage());
//         anException.printStackTrace();
//         fail();
//     }
//     this.debug("END: Test complete.");
//     this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindCourtRooms() {
//
//    this.debug("START: testFindCourtRooms() by URN == //OLDBA/I/2 ");
//    try {
//        String urn = "//OLDBA/I/2";
//        CourtRoomCriteria criteria = new CourtRoomCriteria();
//        criteria.setUrn(urn);
//        Collection data = this.getBisRefController().findCourtRooms(criteria);
//
//        if (data.isEmpty()) {
//            this.debug("No Court Rooms found");
//        } else {
//            int numFound = data.size();
//            this.debug("*** " + numFound + " Court Rooms found...");
//            Iterator allData = data.iterator();
//            CourtRoomBasicValue eachValue = null;
//            while (allData.hasNext()) {
//                eachValue = (CourtRoomBasicValue) allData.next();
//                this.debug("name = " + eachValue.getCourtRoomName() + ".");
//            }
//            if (numFound != 1) {
//                this.debug("Failure: URNs should UNIQUELY identify the Court Room we are searching for.");
//                fail();
//            }
//
//        }
//
//     } catch (Exception anException) {
//         this.debug("An Exception was thrown..." + anException.getMessage());
//         anException.printStackTrace();
//         fail();
//     }
//     this.debug("END: Test complete.");
//     this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//}
//