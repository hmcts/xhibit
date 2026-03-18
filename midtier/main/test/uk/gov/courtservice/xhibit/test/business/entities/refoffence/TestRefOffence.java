//package uk.gov.courtservice.xhibit.test.business.entities.refoffence;
//
////jdk
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.refoffence.RefOffenceHome;
//
//
///**
// * <p>Title: TestRefOffence</p>
// * <p>Description: This class only tests finder methods of RefOffence</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class TestRefOffence extends TestCase
//{
//    private Logger log =  CSServices.getLogger(TestRefOffence.class);
//    private String[][] values = new String[3][14];
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//    public  static String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//
//
//    public TestRefOffence(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp() throws Exception
//    {
//      /*  log.debug("setUp()");
//        log.debug("deleting data");
//        TestUtils.execSql("Delete from XHB_REF_OFFENCE");
//        TestUtils.execSql("delete from xhb_court");
//        TestUtils.execSql("Delete from XHB_ADDRESS");
//
//        log.debug("Insert Address and Court Record");
//        TestUtils.execSql(addrSQL);
//        TestUtils.execSql(courtSQL);
//
//
//            for(int i = 0; i < 3; i++)
//            {
//                System.out.println(i);
//                values[i][0] = String.valueOf(i + 1);
//                values[i][1] = "O_C " + (i + 1);
//                values[i][2] = "Offence_Desc " + (i + 1);
//                values[i][3] = "HP";
//                values[i][4] = "HC";
//		values[i][5] = "UB";
//		values[i][6] = "dvlc";
//		values[i][7] = "R";
//		values[i][8] = "statue";
//		values[i][9] = "H";
//		values[i][10] = "act";
//		values[i][11] = "O";
//		values[i][12] = "offenc desc";
//		values[i][13] = "H";
//            }
//
//            for(int j = 0; j < values.length; j++)
//            {
//                StringBuffer sql = new StringBuffer();
//                sql .append("INSERT INTO XHB_REF_OFFENCE ( REF_OFFENCE_ID, OFFENCE_CODE, OFFENCE_DESC, HO_PROC_TYPE, HO_CLASS, HO_SUB_CLASS, DVLC_CODE, OFFENCE_TYPE, STATUTE, OFFENCE_CLASS, ACT_SECTION, OBS_IND, OFFENCE_DESC2, OFFENCE_GROUP, ");
//                sql.append("LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, XHB_VERSION, COURT_ID ) VALUES ( ");
//                sql.append(Integer.valueOf(values[j][0]) + ", '" + values[j][1] + "', '" + values[j][2] + "', '" + values[j][3] + "', '" + values[j][4] + "', '"+ values[j][5] + "', '"+ values[j][6] + "', '"+ values[j][7] + "', '"+ values[j][8] + "', '"+ values[j][9] + "', '"+ values[j][10] + "', '"+ values[j][11] + "', '"+ values[j][12] + "', '"+ values[j][13] + "', '");
//                sql.append("1-dec-02','1-dec-02','C', 'L', 1, 'xhb_version', 1 )");
//
//                TestUtils.execSql(sql.toString());
//            }
//   */ }
//
//    protected void tearDown() throws Exception
//    {
//     /*   log.debug("tearDown()");
//        log.debug("deleting data");
//        TestUtils.execSql("Delete from XHB_REF_OFFENCE");
//        TestUtils.execSql("delete from xhb_court");
//        TestUtils.execSql("Delete from XHB_ADDRESS");
//*/    }
//
//    /*public void testCreate()
//    {
//        try
//        {
//            RefOffenceHome home = lookupHome();
//            log.debug("testCreate() - Got RefOffenceHome");
//            RefOffence local = home.create("a", "b", "c", "c", "d", "e", "f", "g",
//                    "h", "i", "j", "k", "l");
//            log.debug("refOffenceId : " + local.getRefOffenceId());
//        }
//        catch(Exception e)
//        {
//            log.debug("create() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }*/
//
//    public void testFindRefOffencesByCourtId()
//    {
//  /* Does not build KB
//      log.debug("***testFindRefOffencesByCourtId() start");
//        try
//        {
//            RefOffenceHome home = lookupHome();
//            log.debug("testFindRefOffencesByCourtId() - Got RefOffenceHome");
//
//            Collection refOffences = home.findByCourtId(new Integer("1"));
//            log.debug("No. of rows returned : " + refOffences.size());
//            assertEquals(3, refOffences.size());
//
//            Iterator it = refOffences.iterator();
//            while (it.hasNext()) {
//                RefOffence refOffence = (RefOffence)it.next();
//                int refOffenceId = refOffence.getRefOffenceId().intValue();
//                log.debug("refOffenceId : " + refOffenceId);
//
//                if(refOffenceId >= 1 && refOffenceId <= 3)
//                {
//                    assertEquals(values[refOffenceId - 1][1], refOffence.getOffenceCode());
//                    assertEquals(values[refOffenceId - 1][2], refOffence.getOffenceDesc());
//                    assertEquals(values[refOffenceId - 1][3], refOffence.getHoProcType());
//                    assertEquals(values[refOffenceId - 1][4], refOffence.getHoClass());
//		    assertEquals(values[refOffenceId - 1][5], refOffence.getHoSubclass());
//		    assertEquals(values[refOffenceId - 1][6], refOffence.getDvlcCode());
//		    assertEquals(values[refOffenceId - 1][7], refOffence.getOffenceType());
//                    assertEquals(values[refOffenceId - 1][8], refOffence.getStatute());
//		    assertEquals(values[refOffenceId - 1][9], refOffence.getOffenceClass());
//		    assertEquals(values[refOffenceId - 1][10], refOffence.getActSection());
//		    assertEquals(values[refOffenceId - 1][11], refOffence.getObsInd());
//                    assertEquals(values[refOffenceId - 1][12], refOffence.getOffenceDesc2());
//		    assertEquals(values[refOffenceId - 1][13], refOffence.getOffenceGroup());                }
//                else throw new Exception();
//            }
//        }
//        catch(Exception e)
//        {
//            log.debug("findAllRefOffences() is failed");
//            e.printStackTrace();
//            fail();
//        }
//*/
//    }
//
//
//
//    public void testFindByPrimaryKey()
//    {
//    /*    try
//        {
//            RefOffenceHome home = lookupHome();
//            log.debug("testFindByPrimaryKey() - Got RefOffenceHome");
//            RefOffence refOffence = home.findByPrimaryKey(new Integer(1));
//
//            int refOffenceId = refOffence.getRefOffenceId().intValue();
//            log.debug("refOffenceId : " + refOffenceId);
//            if(refOffenceId == 1)
//            {
//                assertEquals(values[0][1], refOffence.getOffenceCode());
//                assertEquals(values[0][2], refOffence.getOffenceDesc());
//                assertEquals(values[0][3], refOffence.getHoProcType());
//                assertEquals(values[0][4], refOffence.getHoClass());
//            }
//            else throw new Exception();
//        }
//        catch(Exception e)
//        {
//            log.debug("findByPrimaryKey() is failed");
//            e.printStackTrace();
//            fail();
//        }
//*/
//    }
//
//
//    private RefOffenceHome lookupHome() throws Exception
//    { return null;
//  /*      Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (RefOffenceHome) ctx.lookup("RefOffenceHome");
//        return (RefOffenceHome) PortableRemoteObject.narrow(home, RefOffenceHome.class);
//   */ }
//}