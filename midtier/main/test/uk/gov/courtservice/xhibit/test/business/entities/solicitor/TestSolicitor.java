//package uk.gov.courtservice.xhibit.test.business.entities.solicitor;
//
////jdk
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.solicitor.Solicitor;
//import uk.gov.courtservice.xhibit.business.entities.solicitor.SolicitorHome;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//
///**
// * <p>Title: TestSolicitor</p>
// * <p>Description: This class only tests finder methods of Solicitor</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class TestSolicitor extends TestCase
//{
//    private Logger log =  CSServices.getLogger(TestSolicitor.class);
//    private String[][] values = new String[3][3];
//
//    public TestSolicitor(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp()
//    {
//        try
//        {
//            log.debug("setUp() --- Delete from XHB_REF_SOLICITOR");
//            TestUtils.execSql("Delete from XHB_REF_SOLICITOR");
//        TestUtils.execSql("Delete from XHB_REF_LEGAL_REPRESENTATIVE");
//        TestUtils.execSql("Delete from XHB_COURT");
//        TestUtils.execSql("Delete from XHB_ADDRESS");
//
//
//        String sqladdr ="INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES (\n"
//       +1 +", 'Addr1', 'Addr2', 'Addr3', 'addr4','town', 'county', 'postcode', 'country','1-dec-02','1-dec-02','Bush','Bush Senior'," +1+")";
//        TestUtils.execSql(sqladdr);
//
//        String sqlCourt ="INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES (\n"
//        +1 +", 'courtType', 'circuit', 'courtName', 'cci', 'prefix', 'name', '1-dec-02', '1-dec-02', 'bush', 'bush senior', "+ 1 +","+ 1+")";
//        TestUtils.execSql(sqlCourt);
//
//        String legalRepSql="INSERT INTO XHB_REF_LEGAL_REPRESENTATIVE ( REF_LEGAL_REP_ID, FIRST_NAME, MIDDLE_NAME, SURNAME, TITLE, INITALS, LEGAL_REP_TYPE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID ) VALUES (\n"
//        +1+", 'first', 'middle', 'surname', 'title', 'FS', 'L', '1-dec-02', '1-dec-02', 'Bush', 'Bush Senior', "+1+","+1+")";
//        TestUtils.execSql(legalRepSql);
//
//
//            for(int i = 0; i < 3; i++)
//            {
//                System.out.println(i);
//                values[i][0] = String.valueOf(i + 1);
//                values[i][1] = "Crest_Solicitor_Name " + (i + 1);
//                values[i][2] = "Y";
//            }
//
//            for(int j = 0; j < values.length; j++)
//            {
//
//                StringBuffer sql = new StringBuffer();
//                sql .append("INSERT INTO XHB_REF_SOLICITOR ( SOLICITOR_ID, CREST_SOLICITOR_NAME, IS_IN_CREST, REF_LEGAL_REP_ID, ");
//                sql.append("LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID, OBS_IND ) VALUES ( ");
//                sql.append(Integer.valueOf(values[j][0]) + ", '" + values[j][1] + "', '" + values[j][2] + "', ");
//                sql.append("1, '1-dec-02', '1-dec-02', 'C', 'L', 1, 1, 'N' )");
//
//                TestUtils.execSql(sql.toString());
//            }
//        }
//        catch(Exception e)
//        {
//            log.debug("Caught exception in setUp()");
//            log.debug("Exception = " + e);
//        }
//    }
//
//    protected void tearDown()
//    {
//
//    }
//
//    /*public void testCreate()
//    {
//        try
//        {
//            SolicitorHome home = lookupHome();
//            log.debug("testCreate() - Got SolicitorHome");
//            Solicitor local = home.create(new Integer(4), new Integer(4), new Integer(4));
//            log.debug("SolicitorId : " + local.getSolicitorId());
//        }
//        catch(Exception e)
//        {
//            log.debug("create() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }*/
//    /*
//    public void testFindSolicitorsByCourtId()
//    {
//        try
//        {
//            SolicitorHome home = lookupHome();
//            log.debug("testFindSolicitorsByCourtId() - Got SolicitorHome");
//
//            Collection solicitors = home.findByCourtId(new Integer("1"));
//            log.debug("No. of rows returned : " + solicitors.size());
//            assertEquals(3, solicitors.size());
//
//            Iterator it = solicitors.iterator();
//            while (it.hasNext()) {
//                Solicitor solicitor = (Solicitor)it.next();
//                int solicitorId = solicitor.getSolicitorId().intValue();
//                log.debug("solicitorId : " + solicitorId);
//
//                if(solicitorId >= 1 && solicitorId <= 3)
//                {
//                    assertEquals(vos[solicitorId - 1][1], solicitor.getCrestSolicitorName());
//                    assertEquals(vos[solicitorId - 1][2], solicitor.getIsInCrest());
//                }
//                else throw new Exception();
//            }
//        }
//        catch(Exception e)
//        {
//            log.debug("findAllSolicitors() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    finally
//    {
//        try
//        {
//        log.debug("deleting data");
//        TestUtils.execSql("Delete from XHB_REF_SOLICITOR");
//        TestUtils.execSql("Delete from XHB_REF_LEGAL_REPRESENTATIVE");
//        TestUtils.execSql("Delete from XHB_COURT");
//        TestUtils.execSql("Delete from XHB_ADDRESS");
//        }
//        catch(Exception e)
//        {
//        log.debug("findAllRefSolicitor() finally()  is failed");
//        e.printStackTrace();
//        fail();
//        }
//
//
//    }
//    }
//
//    */
//
//    public void testFindByPrimaryKey()
//    {
//        try
//        {
//            SolicitorHome home = lookupHome();
//            log.debug("testFindByPrimaryKey() - Got SolicitorHome");
//            Solicitor solicitor = home.findByPrimaryKey(new Integer(1));
//
//            int solicitorId = solicitor.getSolicitorId().intValue();
//            log.debug("solicitorId : " + solicitorId);
//            if(solicitorId == 1)
//            {
//                assertEquals(values[solicitorId - 1][1], solicitor.getCrestSolicitorName());
//                assertEquals(values[solicitorId - 1][2], solicitor.getIsInCrest());
//            }
//            else throw new Exception();
//        }
//        catch(Exception e)
//        {
//            log.debug("findByPrimaryKey() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    finally
//    {
//        try
//        {
//        log.debug("deleting data");
//        TestUtils.execSql("Delete from XHB_REF_SOLICITOR");
//        TestUtils.execSql("Delete from XHB_REF_LEGAL_REPRESENTATIVE");
//        TestUtils.execSql("Delete from XHB_COURT");
//        TestUtils.execSql("Delete from XHB_ADDRESS");
//        }
//        catch(Exception e)
//        {
//        log.debug("findAllRefSolicitor() finally()  is failed");
//        e.printStackTrace();
//        fail();
//        }
//
//
//    }
//    }
//
//
//    private SolicitorHome lookupHome() throws Exception
//    {
//        Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (SolicitorHome) ctx.lookup("SolicitorHome");
//        return (SolicitorHome) PortableRemoteObject.narrow(home, SolicitorHome.class);
//    }
//}