//package uk.gov.courtservice.xhibit.test.business.entities.refcourt;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourt;
//import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourtHome;
///**
// * Tests finder methods of RefCourt.
// *
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @author Jem Marsh
// * @version 1.2 - Updated for DB v0.13
// */
//public class TestRefCourt extends TestCase {
//
//    private Logger log =  CSServices.getLogger(TestRefCourt.class);
//    Collection refCourtCol = new ArrayList();
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//    public  static String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//
//
//    public TestRefCourt(String s) {
//        super(s);
//    }
//
//    public void testFindByPrimaryKey() {
//
//	  try {
//	    RefCourtHome home = lookupHome();
//	    log.debug("testFindByPrimaryKey() - Got RefCourtHome");
//	    RefCourt local = home.findByPrimaryKey(new Integer(1));
//	    log.debug("refCourtId : " + local.getRefCourtId());
//	    assertEquals(1, local.getRefCourtId().intValue());
//	  } catch(Exception e) {
//	    log.debug("findByPrimaryKey() is failed");
//	    e.printStackTrace();
//	    fail();
//	  }
//    }
//
//    private RefCourtHome lookupHome() throws Exception
//    {
//	Context ctx = CSServices.getServiceLocator().getInitialContext();
//	Object home = (RefCourtHome) ctx.lookup("RefCourtHome");
//	return (RefCourtHome) PortableRemoteObject.narrow(home, RefCourtHome.class);
//    }
//}