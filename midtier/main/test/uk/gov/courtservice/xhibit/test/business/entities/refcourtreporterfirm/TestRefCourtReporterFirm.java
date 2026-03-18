//package uk.gov.courtservice.xhibit.test.business.entities.refcourtreporterfirm;
//
////jdk
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
//import uk.gov.courtservice.xhibit.business.entities.refcourtreporterfirm.RefCourtReporterFirm;
//import uk.gov.courtservice.xhibit.business.entities.refcourtreporterfirm.RefCourtReporterFirmHome;
//
///**
// * <p>Title: TestRefCourtReporterFirm</p>
// * <p>Description: This class only tests finder methods of RefCourtReporterFirm</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class TestRefCourtReporterFirm extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(TestRefCourtReporterFirm.class);
//    Collection reporterFirmCol = new ArrayList();
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//    public  static String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//
//
//    public TestRefCourtReporterFirm(String s)
//    {
//        super(s);
//    }
//
//    public void testFindByPrimaryKey()
//    {
//        try
//        {
//            RefCourtReporterFirmHome home = lookupHome();
//            log.debug("testFindByPrimaryKey() - Got RefCourtReporterFirmHome");
//            RefCourtReporterFirm local = home.findByPrimaryKey(new Integer(1));
//            log.debug("refCourtReporterFirmId : " + local.getRefCourtReporterFirmId());
//            assertEquals(1, local.getRefCourtReporterFirmId().intValue());
//        }
//        catch(Exception e)
//        {
//            log.debug("findByPrimaryKey() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    private RefCourtReporterFirmHome lookupHome() throws Exception
//    {
//        Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (RefCourtReporterFirmHome) ctx.lookup("RefCourtReporterFirmHome");
//        return (RefCourtReporterFirmHome) PortableRemoteObject.narrow(home, RefCourtReporterFirmHome.class);
//    }
//}