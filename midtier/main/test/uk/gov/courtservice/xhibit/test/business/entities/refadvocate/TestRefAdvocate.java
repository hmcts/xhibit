//package uk.gov.courtservice.xhibit.test.business.entities.refadvocate;
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
//import uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocate;
//import uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocateHome;
//import uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamber;
//
///**
// * <p>Title: TestRefAdvocate</p>
// * <p>Description: This class only tests finder methods of RefAdvocate</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class TestRefAdvocate extends TestCase
//{
//    private Logger log =  CSServices.getLogger(TestRefAdvocate.class);
//    Collection AdvoCol = new ArrayList();
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//    public  static String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//    public  static String legalRepSql ="INSERT INTO XHB_REF_LEGAL_REPRESENTATIVE ( REF_LEGAL_REP_ID, FIRST_NAME, MIDDLE_NAME, SURNAME, TITLE, INITALS, LEGAL_REP_TYPE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID ) VALUES (1, 'first', 'middle', 'surname', 'title', 'FS', 'L', '1-dec-02', '1-dec-02', 'Bush', 'Bush Senior', 1, 1)";
//    public  static String chamberSql ="INSERT INTO XHB_REF_CHAMBER ( REF_CHAMBER_ID, XHB_VERSION, OBS_IND, IS_GLOBAL, DX_REF, LOCATION_CODE, CREST_CHAMBER_ID, FIRM_NAME, ADDRESS_ID, LAST_UPDATE_DATE,CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID) VALUES ( 1, 'xhbVersion 3', '3', '3', 'DX_REF 3', 'C3', 3, 'FIRM_NAME3', 1,'1-dec-02', '1-dec-02', 'created_by', 'last_updated_by', 1, 1)";
//
//    public TestRefAdvocate(String s)
//    {
//        super(s);
//    }
//
//    public void testFindByPrimaryKey()
//    {
//        try
//        {
//            log.debug("testGetAddress() start");
//            RefAdvocateHome home = lookupHome();
//            RefAdvocate local = home.findByPrimaryKey(new Integer(1));
//            log.debug("refAdvocateId : " + local.getRefAdvocateId());
//            assertEquals(1, local.getRefAdvocateId().intValue());
//        }
//        catch(Exception e)
//        {
//            log.debug("testGetAddress() has failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    public void testGetChamber()
//    {
//        try
//        {
//            RefAdvocateHome home = lookupHome();
//            log.debug("testFindByPrimaryKey() - Got RefAdvocateHome");
//            RefAdvocate local = home.findByPrimaryKey(new Integer(1));
//            log.debug("refAdvocateId : " + local.getRefAdvocateId());
//            assertEquals(1, local.getRefAdvocateId().intValue());
//            RefChamber chamber = local.getRefChamber();
//            assertEquals(new Integer(1), chamber.getRefChamberId());
//            assertEquals("FIRM_NAME3", chamber.getFirmName());
//
//        }
//        catch(Exception e)
//        {
//            log.debug("findByPrimaryKey() has failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    private RefAdvocateHome lookupHome() throws Exception
//    {
//        Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (RefAdvocateHome) ctx.lookup("RefAdvocateHome");
//        return (RefAdvocateHome) PortableRemoteObject.narrow(home, RefAdvocateHome.class);
//    }
//}