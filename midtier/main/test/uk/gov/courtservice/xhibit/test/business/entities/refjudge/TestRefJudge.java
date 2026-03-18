//package uk.gov.courtservice.xhibit.test.business.entities.refjudge;
//
////jdk
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudge;
//import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudgeHome;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//import java.util.Collection;
//import java.util.Iterator;
//
///**
// * <p>Title: TestRefJudge</p>
// * <p>Description: This class only tests finder methods of RefJudge</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class TestRefJudge extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(TestRefJudge.class);
//    private String[][] values = new String[3][16];
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//    public  static String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//
//
//    public TestRefJudge(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp()
//    {
//        try
//        {
//            log.debug("setUp()");
//            log.debug("deleting data");
//            TestUtils.execSql("Delete from XHB_REF_JUDGE");
//            TestUtils.execSql("Delete from XHB_COURT");
//            TestUtils.execSql("Delete from XHB_ADDRESS");
//
//            log.debug("Insert Address and Court Record");
//            TestUtils.execSql(addrSQL);
//            TestUtils.execSql(courtSQL);
//
//
//
//
//            for(int i = 0; i < 3; i++)
//            {
//                System.out.println(i);
//                values[i][0] = String.valueOf(i + 1);
//                values[i][1] = "Judge_Type " + (i + 1);
//                values[i][2] = String.valueOf(i + 1);
//                values[i][3] = "First_Name " + (i + 1);
//                values[i][4] = "Middle_Name " + (i + 1);
//                values[i][5] = "Surname " + (i + 1);
//                values[i][6] = "Full_List_Title1 " + (i + 1);
//                values[i][7] = "Full_List_Title2 " + (i + 1);
//                values[i][8] = "Full_List_Title3 " + (i + 1);
//                values[i][9] = "S_C " + (i + 1);
//                values[i][10] = "I " + (i + 1);
//                values[i][11] = "Honours " + (i + 1);
//                values[i][12] = "J" + (i + 1);
//                values[i][13] = "N";
//                values[i][14] = "Source_Table " + (i + 1);
//                values[i][15] = "Title " + (i + 1);
//            }
//
//            for(int j = 0; j < values.length; j++)
//            {
//                StringBuffer sql = new StringBuffer();
//                sql .append("INSERT INTO XHB_REF_JUDGE ( REF_JUDGE_ID, JUDGE_TYPE, ");
//                sql.append("CREST_JUDGE_ID, FIRST_NAME, MIDDLE_NAME, SURNAME, ");
//                sql.append("FULL_LIST_TITLE1, FULL_LIST_TITLE2, FULL_LIST_TITLE3, ");
//                sql.append("STATS_CODE, INITIALS, HONOURS, JUD_VERS, OBS_IND, ");
//                sql.append("SOURCE_TABLE, TITLE, LAST_UPDATE_DATE, CREATION_DATE, ");
//                sql.append("CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID) VALUES ( ");
//                sql.append(Integer.valueOf(values[j][0]) + ", '" + values[j][1] + "', " + Integer.valueOf(values[j][2]) + ", '");
//                sql.append(values[j][3] + "', '" + values[j][4] + "', '" + values[j][5] + "', '" + values[j][6] + "', '" + values[j][7] + "', '");
//                sql.append(values[j][8] + "', '" + values[j][9] + "', '" + values[j][10] + "', '" + values[j][11] + "', '");
//                sql.append(values[j][12] + "', '" + values[j][13] + "', '" + values[j][14] + "', '" + values[j][15] + "', '1-dec-02', '1-dec-02', '");
//                sql.append("created_by', 'last_updated_by', " + new Integer(1) + ", "+new Integer(1)+")");
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
//    protected void tearDown() throws Exception
//    {
//        log.debug("tearDown()");
//        log.debug("deleting data");
//        TestUtils.execSql("Delete from XHB_REF_JUDGE");
//        TestUtils.execSql("Delete from XHB_COURT");
//        TestUtils.execSql("Delete from XHB_ADDRESS");
//    }
//
//    /*public void testCreate()
//    {
//        try
//        {
//            RefJudgeHome home = lookupHome();
//            log.debug("testCreate() - Got RefJudgeHome");
//            RefJudge local = home.create(new Integer(4), new Integer(4), new Integer(4));
//            log.debug("refJudgeId : " + local.getRefJudgeId());
//        }
//        catch(Exception e)
//        {
//            log.debug("create() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }*/
//
//    public void testFindRefJudgesByCourtId()
//    {
//        log.debug("***testFindRefJudgesByCourtId() start");
//        try
//        {
//            RefJudgeHome home = lookupHome();
//            log.debug("testFindRefJudgesByCourtId() - Got RefJudgeHome");
//
//            Collection refJudges = home.findByCourtId(new Integer("1"));
//            log.debug("No. of rows returned : " + refJudges.size());
//
//            assertEquals(3, refJudges.size());
//
//            Iterator it = refJudges.iterator();
//            while (it.hasNext()) {
//                RefJudge refJudge = (RefJudge)it.next();
//                int refJudgeId = refJudge.getRefJudgeId().intValue();
//                log.debug("refJudgeId : " + refJudgeId);
//
//                if(refJudgeId >= 1 && refJudgeId <= 3)
//                {
//                    assertEquals(values[refJudgeId - 1][1], refJudge.getJudgeType());
//                    assertEquals(Integer.valueOf(values[refJudgeId - 1][2]), refJudge.getCrestJudgeId());
//                    assertEquals(values[refJudgeId - 1][3], refJudge.getFirstName());
//                    assertEquals(values[refJudgeId - 1][4], refJudge.getMiddleName());
//                    assertEquals(values[refJudgeId - 1][5], refJudge.getSurname());
//                    assertEquals(values[refJudgeId - 1][6], refJudge.getFullListTitle1());
//                    assertEquals(values[refJudgeId - 1][7], refJudge.getFullListTitle2());
//                    assertEquals(values[refJudgeId - 1][8], refJudge.getFullListTitle3());
//                    assertEquals(values[refJudgeId - 1][9], refJudge.getStatsCode());
//                    assertEquals(values[refJudgeId - 1][10], refJudge.getInitials());
//                    assertEquals(values[refJudgeId - 1][11], refJudge.getHonours());
//                    assertEquals(values[refJudgeId - 1][12], refJudge.getJudVers());
//                    assertEquals(values[refJudgeId - 1][13], refJudge.getObsInd());
//                    assertEquals(values[refJudgeId - 1][14], refJudge.getSourceTable());
//                    assertEquals(values[refJudgeId - 1][15], refJudge.getTitle());
//                }
//                else throw new Exception();
//            }
//        }
//        catch(Exception e)
//        {
//            log.debug("findAllRefJudges() is failed");
//            e.printStackTrace();
//            fail();
//        }
//
//    }
//
//
//
//    public void testFindByPrimaryKey()
//    {
//        try
//        {
//            RefJudgeHome home = lookupHome();
//            log.debug("testFindByPrimaryKey() - Got RefJudgeHome");
//            RefJudge refJudge = home.findByPrimaryKey(new Integer(1));
//            int refJudgeId = refJudge.getRefJudgeId().intValue();
//            log.debug("refJudgeId : " + refJudgeId);
//
//            if(refJudgeId == 1)
//            {
//                assertEquals(values[0][1], refJudge.getJudgeType());
//                assertEquals(Integer.valueOf(values[0][2]), refJudge.getCrestJudgeId());
//                assertEquals(values[0][3], refJudge.getFirstName());
//                assertEquals(values[0][4], refJudge.getMiddleName());
//                assertEquals(values[0][5], refJudge.getSurname());
//                assertEquals(values[0][6], refJudge.getFullListTitle1());
//                assertEquals(values[0][7], refJudge.getFullListTitle2());
//                assertEquals(values[0][8], refJudge.getFullListTitle3());
//                assertEquals(values[0][9], refJudge.getStatsCode());
//                assertEquals(values[0][10], refJudge.getInitials());
//                assertEquals(values[0][11], refJudge.getHonours());
//                assertEquals(values[0][12], refJudge.getJudVers());
//                assertEquals(values[0][13], refJudge.getObsInd());
//                assertEquals(values[0][14], refJudge.getSourceTable());
//                assertEquals(values[0][15], refJudge.getTitle());
//            }
//            else throw new Exception();
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
//    private RefJudgeHome lookupHome() throws Exception
//    {
//        Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (RefJudgeHome) ctx.lookup("RefJudgeHome");
//        return (RefJudgeHome) PortableRemoteObject.narrow(home, RefJudgeHome.class);
//    }
//}