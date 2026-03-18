//package uk.gov.courtservice.xhibit.test.business.entities.refjustice;
//
////jdk
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.refjustice.RefJustice;
//import uk.gov.courtservice.xhibit.business.entities.refjustice.RefJusticeHome;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//import java.util.Collection;
//import java.util.Iterator;
//
///**
// * <p>Title: TestRefJustice</p>
// * <p>Description: This class only tests finder methods of RefJustice</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class TestRefJustice extends TestCase
//{
//    private Logger log =  CSServices.getLogger(TestRefJustice.class);
//    private String[][] values = new String[3][4];
//
//    public TestRefJustice(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp()
//    {
//        try
//        {
//	    TestUtils.execSql("Delete from XHB_REF_JUSTICE");
//	    TestUtils.execSql("Delete from XHB_SITTING");
//	    TestUtils.execSql("Delete from XHB_COURT_ROOM");
//	    TestUtils.execSql("Delete from XHB_COURT_SITE");
//	    TestUtils.execSql("Delete from XHB_HEARING_LIST");
//	    TestUtils.execSql("Delete from XHB_DAILY_LIST_XML");
//	    TestUtils.execSql("Delete from XHB_REF_JUDGE");
//	    TestUtils.execSql("Delete from XHB_COURT");
//	    TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	    String sqlAddr ="INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES (\n"
//	   +1 +", 'Addr1', 'Addr2', 'Addr3', 'addr4','town', 'county', 'postcode', 'country','1-dec-02','1-dec-02','Bush','Bush Senior'," +1+")";
//	    TestUtils.execSql(sqlAddr);
//
//	    String sqlCourt ="INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES (\n"
//	    +1 +", 'courtType', 'circuit', 'courtName', 'cci', 'prefix', 'name', '1-dec-02', '1-dec-02', 'bush', 'bush senior', "+ 1 +","+ 1+")";
//	    TestUtils.execSql(sqlCourt);
//
//	    String sqlJudge = "INSERT INTO XHB_REF_JUDGE ( REF_JUDGE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID ) VALUES (\n"
//	    +1+", '1-dec-02', '1-dec-02', 'bush sne', 'bush', 1, 1 )";
//	    TestUtils.execSql(sqlJudge);
//
//	    String sqlDaily = "INSERT INTO XHB_DAILY_LIST_XML ( DAILY_LIST_XML_ID, COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES (\n"
//	    +1+", 1, '1-dec-02', '1-dec-02', 'bush snr', 'bush', 1)";
//	    TestUtils.execSql(sqlDaily);
//
//	    String sqlHear ="INSERT INTO XHB_HEARING_LIST ( LIST_ID, CREST_LIST_ID, DAILY_LIST_XML_ID, COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (\n"
//	    +1+", 1, 1, 1, '1-dec-02', '1-dec-02', 'bush snr', 'bush', 1)";
//	    TestUtils.execSql(sqlHear);
//
//	    String sqlCourtSite = "INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_ID, ADDRESS_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES (\n"
//	    +1+", 1, 1, '1-dec-02', '1-dec-02', 'bush snr', 'bush', 1)";
//	    TestUtils.execSql(sqlCourtSite);
//
//	    String sqlCourtRoom ="INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_SITE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES (\n"
//	    +1+", 1, '1-dec-02', '1-dec-02', 'bush snr', 'bush', 1)";
//	    TestUtils.execSql(sqlCourtRoom);
//
//	    String sqlSitting = "INSERT INTO XHB_SITTING ( SITTING_ID, SITTING_TIME, IS_FLOATING, LIST_ID, REF_JUDGE_ID, COURT_SITE_ID, COURT_ROOM_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES (\n"
//	    +1+", '1-dec-02', 'F', 1, 1, 1, 1, '1-dec-02', '1-dec-02', 'bush snr', 'bush', 1)";
//	    TestUtils.execSql(sqlSitting);
//
//
//
//
//
//
//
//
//            /*log.debug("setUp() --- Populate XHB_COURT");
//            StringBuffer sb = new StringBuffer();
//            sb.append("INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, ");
//            sb.append("COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, ");
//            sb.append("VERSION ) VALUES ( 1, 'C', 'C', 'C', 1, 'C', 'S', TO_Date( '11/11/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), ");
//            sb.append("TO_Date( '10/10/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'C', 'L', 1 )");
//            TestUtils.execSql(sb.toString());*/
//
//            for(int i = 0; i < 3; i++)
//            {
//                System.out.println(i);
//                values[i][0] = String.valueOf(i + 1);
//                values[i][1] = "Justice_Name " + (i + 1);
//                values[i][2] = String.valueOf(1);
//                values[i][3] = String.valueOf(1);
//            }
//
//            for(int j = 0; j < values.length; j++)
//            {
//                StringBuffer sql = new StringBuffer();
//                sql .append("INSERT INTO XHB_REF_JUSTICE ( REF_JUSTICE_ID, JUSTICE_NAME, CREST_JUSTICE_ID, COURT_ID, ");
//                sql.append("LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, OBS_IND, INITIALS, TITLE, PSD_COURT_CODE ) VALUES ( ");
//                sql.append(Integer.valueOf(values[j][0]) + ", '" + values[j][1] + "', " + Integer.valueOf(values[j][2]) + ", ");
//                sql.append(new Integer("1")+", '1-dec-02', '1-dec-02', 'created_by', 'last_updated_by', " + new Integer(1) + ", 'N', 'Mr', 'Right Dishonourable', 'PSD' )");
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
//      /*  try
//        {
//            log.debug("setUp() --- Delete from XHB_REF_JUSTICE");
//            TestUtils.execSql("Delete from XHB_REF_JUSTICE");
//
//            log.debug("setUp() --- Delete from XHB_COURT");
//            TestUtils.execSql("Delete from XHB_COURT");
//        }
//        catch(Exception e)
//        {
//            log.debug("Caught exception in tearDown()");
//            log.debug("Exception = " + e);
//        }*/
//    }
//
//    /*public void testCreate()
//    {
//        try
//        {
//            RefJusticeHome home = lookupHome();
//            log.debug("testCreate() - Got RefJusticeHome");
//            RefJustice local = home.create(new Integer(4), new Integer(4), new Integer(4));
//            log.debug("refJusticeId : " + local.getRefJusticeId());
//        }
//        catch(Exception e)
//        {
//            log.debug("create() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }*/
//
//    public void testFindRefJusticesByCourtId()
//    {
//        try
//        {
//            RefJusticeHome home = lookupHome();
//            log.debug("testFindAllRefJustices() - Got RefJusticeHome");
//
//            Collection refJustices = home.findByCourtId(new Integer("1"));
//            log.debug("No. of rows returned : " + refJustices.size());
//
//            assertEquals(3, refJustices.size());
//
//            Iterator it = refJustices.iterator();
//            while (it.hasNext()) {
//                RefJustice refJustice = (RefJustice)it.next();
//                int refJusticeId = refJustice.getRefJusticeId().intValue();
//                log.debug("refJusticeId : " + refJusticeId);
//
//                if(refJusticeId >= 1 && refJusticeId <= 3)
//                {
//                    assertEquals(values[refJusticeId - 1][1], refJustice.getJusticeName());
//                    assertEquals(Integer.valueOf(values[refJusticeId - 1][2]), refJustice.getCrestJusticeId() );
//                    assertEquals(new Integer("1"), refJustice.getCourtId());
//                    assertEquals("Mr",refJustice.getInitials());
//                    assertEquals("Right Dishonourable", refJustice.getTitle());
//                    assertEquals("PSD",refJustice.getPsdCourtCode());
//
//                }
//                else throw new Exception();
//            }
//        }
//        catch(Exception e)
//        {
//            log.debug("findAllRefJustices() is failed");
//            e.printStackTrace();
//            fail();
//        }
//	finally
//	{
//	    try
//	    {
//		log.debug("deleting data");
//		TestUtils.execSql("Delete from XHB_REF_JUSTICE");
//	    TestUtils.execSql("Delete from XHB_SITTING");
//	    TestUtils.execSql("Delete from XHB_COURT_ROOM");
//	    TestUtils.execSql("Delete from XHB_COURT_SITE");
//	    TestUtils.execSql("Delete from XHB_HEARING_LIST");
//	    TestUtils.execSql("Delete from XHB_DAILY_LIST_XML");
//	    TestUtils.execSql("Delete from XHB_REF_JUDGE");
//	    TestUtils.execSql("Delete from XHB_COURT");
//	    TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	    }
//	    catch(Exception e)
//	    {
//		log.debug("findAllRefOffences() finally()  is failed");
//		e.printStackTrace();
//		fail();
//	    }
//
//
//	}
//    }
//
//
//
//    public void testFindByPrimaryKey()
//    {
//        try
//        {
//            RefJusticeHome home = lookupHome();
//            log.debug("testFindByPrimaryKey() - Got RefJusticeHome");
//            RefJustice refJustice = home.findByPrimaryKey(new Integer(1));
//            int refJusticeId = refJustice.getRefJusticeId().intValue();
//            log.debug("refJusticeId : " + refJusticeId);
//            if(refJusticeId == 1)
//            {
//                assertEquals(values[0][1], refJustice.getJusticeName());
//                assertEquals(Integer.valueOf(values[0][2]), refJustice.getCrestJusticeId() );
//                assertEquals(Integer.valueOf(values[0][3]), refJustice.getCourtId());
//            }
//            else throw new Exception();
//        }
//        catch(Exception e)
//        {
//            log.debug("findByPrimaryKey() is failed");
//            e.printStackTrace();
//            fail();
//        }
//	finally
//	{
//	    try
//	    {
//		log.debug("deleting data");
//		TestUtils.execSql("Delete from XHB_REF_JUSTICE");
//	    TestUtils.execSql("Delete from XHB_SITTING");
//	    TestUtils.execSql("Delete from XHB_COURT_ROOM");
//	    TestUtils.execSql("Delete from XHB_COURT_SITE");
//	    TestUtils.execSql("Delete from XHB_HEARING_LIST");
//	    TestUtils.execSql("Delete from XHB_DAILY_LIST_XML");
//	    TestUtils.execSql("Delete from XHB_REF_JUDGE");
//	    TestUtils.execSql("Delete from XHB_COURT");
//	    TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	    }
//	    catch(Exception e)
//	    {
//		log.debug("findAllRefOffences() finally()  is failed");
//		e.printStackTrace();
//		fail();
//	    }
//
//
//	}
//    }
//
//
//    private void populateCourt()
//    {
//        try
//        {
//            log.debug("Populate XHB_COURT");
//            StringBuffer sb = new StringBuffer();
//            sb.append("INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, ");
//            sb.append("COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, ");
//            sb.append("VERSION ) VALUES ( 1, 'C', 'C', 'C', 1, 'C', 'S', TO_Date( '11/11/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), ");
//            sb.append("TO_Date( '10/10/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'C', 'L', 1 )");
//            TestUtils.execSql(sb.toString());
//        }
//        catch(Exception e)
//       {
//           log.debug("Caught exception in populateCourt()");
//           log.debug("Exception = " + e);
//        }
//    }
//
//
//    private RefJusticeHome lookupHome() throws Exception
//    {
//        Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (RefJusticeHome) ctx.lookup("RefJusticeHome");
//        return (RefJusticeHome) PortableRemoteObject.narrow(home, RefJusticeHome.class);
//    }
//}