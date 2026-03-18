//package uk.gov.courtservice.xhibit.test.business.entities.refcourtreporter;
//
////jdk
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.RandomValues;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.refcourtreporter.RefCourtReporter;
//import uk.gov.courtservice.xhibit.business.entities.refcourtreporter.RefCourtReporterHome;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterValue;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.TreeSet;
//
///**
// * <p>Title: TestRefCourtReporter</p>
// * <p>Description: This class only tests finder methods of RefCourtReporter</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class TestRefCourtReporter extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(TestRefCourtReporter.class);
//    Collection reporterCol = new ArrayList();
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//    public  static String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//    public  static String firmSQL ="INSERT INTO XHB_REF_COURT_REPORTER_FIRM ( REF_COURT_REPORTER_FIRM_ID, OBS_IND, DISPLAY_FIRST, DX_REF, VAT_NO, FIRM_NAME, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, VERSION, ADDRESS_ID, COURT_ID ) VALUES ( 1, 'O', 'D', 'dx_ref', 'vatNo', 'Firm', 'bush', 'bushSnr', '1-dec-02', '1-dec-02',  1, 1, 1)";
//
//
//    public TestRefCourtReporter(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp() throws Exception
//    {
//        log.debug("setUp()");
//        log.debug("deleting data");
//        TestUtils.execSql("Delete from XHB_REF_COURT_REPORTER");
//        TestUtils.execSql("Delete from XHB_REF_COURT_REPORTER_FIRM");
//        TestUtils.execSql("delete from xhb_court");
//        TestUtils.execSql("Delete from XHB_ADDRESS");
//        log.debug("creating test data");
//        TestUtils.execSql(addrSQL);
//        TestUtils.execSql(courtSQL);
//        TestUtils.execSql(firmSQL);
//
//
//        log.debug("creating CourtReporter value");
//        RandomValues random = new RandomValues();
//        RefCourtReporterValue values;
//        for(int i = 0; i<10; i++)
//        {
//            RefCourtReporterValue refCourtReporterValue = new RefCourtReporterValue(new Integer(i), new Integer(1),random.randomAlphaNumericString(5),
//            random.randomAlphaNumericString(3), random.randomAlphaNumericString(3),
//            random.randomAlphaNumericString(3), random.randomAlphaNumericString(1),
//            random.randomAlphaNumericString(1), new Integer(1), new Integer(1));
//            reporterCol.add(refCourtReporterValue);
//        }
//
//
//        log.debug("creating sql statemnet to insert into court reporter");
//        Iterator itr = reporterCol.iterator();
//        for(int i = 0; i< 10; i++)
//        {
//            values =(RefCourtReporterValue)itr.next();
//            String courtReporterSQL = "INSERT INTO XHB_REF_COURT_REPORTER ( REF_COURT_REPORTER_ID, FIRST_NAME, MIDDLE_NAME, SURNAME, INITIALS, REPORT_METHOD, OBS_IND, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, REF_COURT_REPORTER_FIRM_ID, COURT_ID,CREST_COURT_REPORTER_ID) VALUES ( \n"
//                              +i +", '" + values.getFirstName()+ "' , '"+ values.getMidleName()+"', '"+ values.getSurname() + "', '" + values.getInitials()+"','"+values.getReportMethod()+"','"+values.getObsInd()+"','1-dec-02', '1-dec-02', 'Bush','Bush Senior', 1, 1, 1, 1)";
//
//            log.debug("sql=" + courtReporterSQL);
//            TestUtils.execSql(courtReporterSQL);
//        }
//
//        log.debug("inserted sqlHearing into COURT reporter table");
//
//
//
//    }
//
//    protected void tearDown() throws Exception
//    {
//        log.debug("tearDown()");
//        log.debug("deleting data");
//        TestUtils.execSql("Delete from XHB_REF_COURT_REPORTER");
//        TestUtils.execSql("Delete from XHB_REF_COURT_REPORTER_FIRM");
//        TestUtils.execSql("delete from xhb_court");
//        TestUtils.execSql("Delete from XHB_ADDRESS");
//    }
//
//    /*public void testCreate()
//    {
//        try
//        {
//            RefCourtReporterHome home = lookupHome();
//            log.debug("testCreate() - Got RefCourtReporterHome");
//            RefCourtReporter local = home.create(new Integer(4), new Integer(4), new Integer(4));
//            log.debug("refCourtReporterId : " + local.getRefCourtReporterId());
//        }
//        catch(Exception e)
//        {
//            log.debug("create() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }*/
//
//
//    public void testFindRefCourtReportersByCourtId()
//    {
//    log.debug("***testFindRefCourtReportersByCourtId() start");
//        Collection courtRepCollection = null;
//    Collection courtRepValues = new ArrayList();
//    RefCourtReporter courtRep = null;
//        try
//        {
//            RefCourtReporterHome home = lookupHome();
//            log.debug("testFindAllRefCourtReportersByCourtId() - Got RefCourtReporterHome");
//
//            courtRepCollection= home.findByCourtId(new Integer("1"));
//            log.debug("No. of rows returned : " + courtRepCollection.size());
//            assertEquals(reporterCol.size(), courtRepCollection.size());
//
//            String obsolete;
//            log.debug("update one of the entries to be obsolete");
//            obsolete = "update XHB_REF_COURT_REPORTER set obs_ind = 'Y' where REF_COURT_REPORTER_ID = 1";
//            log.debug(obsolete);
//            TestUtils.execSql(obsolete);
//            log.debug("Changed one record to be obsolete");
//
//            courtRepCollection = home.findByCourtId(new Integer("1"));
//            log.debug("No. of rows returned : " + courtRepCollection.size());
//            assertEquals(reporterCol.size()-1, courtRepCollection.size());
//
//            log.debug("update obsolete entry to active again");
//            obsolete = "update XHB_REF_COURT_REPORTER set obs_ind = 'N' where REF_COURT_REPORTER_ID = 1";
//            log.debug(obsolete);
//            TestUtils.execSql(obsolete);
//
//            courtRepCollection = home.findByCourtId(new Integer("1"));
//            log.debug("No. of rows returned : " + courtRepCollection.size());
//            assertEquals(reporterCol.size(), courtRepCollection.size());
//
//            Iterator itr = courtRepCollection.iterator();
//            //while(itr.hasNext());
//            for(int i = 0; i<10; i++)
//            {
//                RefCourtReporterValue courtRepValue = new RefCourtReporterValue();
//                courtRep =(RefCourtReporter)itr.next();
//
//                courtRepValue.setFirstName(courtRep.getFirstName());
//                courtRepValue.setInitials(courtRep.getInitials());
//                courtRepValue.setMidleName(courtRep.getMiddleName());
//                courtRepValue.setObsInd(courtRep.getObsInd());
//                courtRepValue.setRefCourtReporterId(courtRep.getRefCourtReporterId());
//                courtRepValue.setReportMethod(courtRep.getReportMethod());
//                courtRepValue.setSurname(courtRep.getSurname());
//                courtRepValue.setCourtId(courtRep.getCourtId());
//                courtRepValue.setCrestCourtReporterId(courtRep.getCrestCourtReporterId());
//                courtRepValue.setRefCourtReporterFirmId(courtRep.getRefCourtReporterFirmId());
//
//        System.out.println("court first name = " + courtRepValue.getFirstName());
//        courtRepValues.add(courtRepValue);
//        log.debug("sixe = " + courtRepValues.size());
//        }
//
//
//        log.debug("here");
//        TreeSet actual = new TreeSet();
//      TreeSet expected = new TreeSet();
//      log.debug("created tree set");
//      Iterator itrE = courtRepValues.iterator();
//      log.debug("got iterator");
//      while(itrE.hasNext())
//      {
//          RefCourtReporterValue firmVal = new RefCourtReporterValue();
//          firmVal = (RefCourtReporterValue)itrE.next();
//          String firm = firmVal.toString();
//          log.debug("firmactual = " + firm);
//          expected.add(firm);
//
//      }
//
//      Iterator itrA = reporterCol.iterator();
//      while(itrA.hasNext())
//      {
//          RefCourtReporterValue firmVal = new RefCourtReporterValue();
//          firmVal = (RefCourtReporterValue)itrA.next();
//          String firm = firmVal.toString();
//          log.debug("firmexpected = " + firm);
//          actual.add(firm);
//
//      }
//
//      log.debug("expected = " + expected.toString());
//      log.debug("actual = " + actual.toString());
//
//      Iterator itrT = actual.iterator();
//      Iterator itrT1 = expected.iterator();
//      while(itrT.hasNext())
//      {
//          assertEquals(itrT.next(), itrT1.next());
//          log.debug("passed");
//      }
//        }
//        catch(Exception e)
//        {
//            log.debug("findAllRefCourtReporters() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//
//    public void testFindByPrimaryKey()
//    {
//        try
//        {
//            RefCourtReporterHome home = lookupHome();
//            log.debug("testFindByPrimaryKey() - Got RefCourtReporterHome");
//            RefCourtReporter local = home.findByPrimaryKey(new Integer(1));
//            log.debug("refCourtReporterId : " + local.getRefCourtReporterId());
//            this.assertEquals(1, local.getRefCourtReporterId().intValue());
//        }
//        catch(Exception e)
//        {
//            log.debug("findByPrimaryKey() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    private RefCourtReporterHome lookupHome() throws Exception
//    {
//        Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (RefCourtReporterHome) ctx.lookup("RefCourtReporterHome");
//        return (RefCourtReporterHome) PortableRemoteObject.narrow(home, RefCourtReporterHome.class);
//    }
//}