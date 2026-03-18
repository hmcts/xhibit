//package uk.gov.courtservice.xhibit.test.business.entities.refsystemcode;
//
////jdk
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCode;
//import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCodeHome;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//import java.util.Collection;
//import java.util.Iterator;
//
///**
// * <p>Title: TestRefSystemCode</p>
// * <p>Description: This class only tests finder methods of RefSystemCode</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class TestRefSystemCode extends TestCase
//{
//    private Logger log =  CSServices.getLogger(TestRefSystemCode.class);
//    private String[][] values = new String[3][6];
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//    public  static String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//
//
//    public TestRefSystemCode(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp() throws Exception
//    {
//
//        log.debug("setUp()");
//        log.debug("deleting data");
//        TestUtils.execSql("Delete from XHB_REF_SYSTEM_CODE");
//        TestUtils.execSql("delete from xhb_court");
//        TestUtils.execSql("Delete from XHB_ADDRESS");
//
//        log.debug("Insert Address and Court Record");
//        TestUtils.execSql(addrSQL);
//        TestUtils.execSql(courtSQL);
//
//
//        for(int i = 0; i < 3; i++)
//        {
//            System.out.println(i);
//            values[i][0] = String.valueOf(i + 1);
//            values[i][1] = "Code " + (i + 1);
//            values[i][2] = "Code_Type" + (i + 1);
//            values[i][3] = "Code_Title" + (i + 1);
//            values[i][4] = "Decode " + (i + 1);
//            values[i][5] = String.valueOf(1);
//        }
//
//        for(int j = 0; j < values.length; j++)
//        {
//
//            StringBuffer sql = new StringBuffer();
//            sql .append("INSERT INTO XHB_REF_SYSTEM_CODE ( REF_SYSTEM_CODE_ID, CODE, CODE_TYPE, CODE_TITLE, DE_CODE, REF_CODE_ORDER, ");
//            sql.append("LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, XHB_VERSION, COURT_ID, OBS_IND ) VALUES ( ");
//            sql.append(Integer.valueOf(values[j][0]) + ", '" + values[j][1] + "', '" + values[j][2] + "', '");
//            sql.append(values[j][3] + "', '" + values[j][4] + "', " + Integer.valueOf(values[j][5]) + ", ");
//            sql.append("TO_Date( '11/11/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_Date( '10/10/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'C', 'L', 1, 'xhb_version', 1, 'N' )");
//
//            TestUtils.execSql(sql.toString());
//        }
//    }
//
//    protected void tearDown() throws Exception
//    {
//        log.debug("tearDown()");
//        log.debug("deleting data");
//        TestUtils.execSql("Delete from XHB_REF_SYSTEM_CODE");
//        TestUtils.execSql("delete from xhb_court");
//        TestUtils.execSql("Delete from XHB_ADDRESS");
//    }
//
//    /*public void testCreate()
//    {
//        try
//        {
//            RefSystemCodeHome home = lookupHome();
//            log.debug("testCreate() - Got RefSystemCodeHome");
//            RefSystemCode local = home.create(new Integer(4), new Integer(4), new Integer(4));
//            log.debug("refSystemCodeId : " + local.getRefSystemCodeId());
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
//    public void testFindRefSystemCodesByCourtId()
//    {
//        try
//        {
//            RefSystemCodeHome home = lookupHome();
//            log.debug("testFindRefSystemCodesByCourtId() - Got RefSystemCodeHome");
//
//            Collection refSystemCodes = home.findByCourtId(new Integer("1"));
//            log.debug("No. of rows returned : " + refSystemCodes.size());
//            assertEquals(3, refSystemCodes.size());
//
//            Iterator it = refSystemCodes.iterator();
//            while (it.hasNext()) {
//                RefSystemCode refSystemCode = (RefSystemCode)it.next();
//                int refSystemCodeId = refSystemCode.getRefSystemCodeId().intValue();
//                log.debug("refSystemCodeId : " + refSystemCodeId);
//
//                if(refSystemCodeId >= 1 && refSystemCodeId <= 3)
//                {
//                    assertEquals(values[refSystemCodeId - 1][1], refSystemCode.getCode());
//                    assertEquals(values[refSystemCodeId - 1][2], refSystemCode.getCodeType());
//                    assertEquals(values[refSystemCodeId - 1][3], refSystemCode.getCodeTitle());
//                    assertEquals(values[refSystemCodeId - 1][4], refSystemCode.getDecode());
//                    assertEquals(Integer.valueOf(values[refSystemCodeId - 1][5]), refSystemCode.getRefCodeOrder());
//
//                }
//                else throw new Exception();
//            }
//        }
//        catch(Exception e)
//        {
//            log.debug("findAllRefSystemCodes() is failed");
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
//            RefSystemCodeHome home = lookupHome();
//            log.debug("testFindByPrimaryKey() - Got RefSystemCodeHome");
//            RefSystemCode refSystemCode = home.findByPrimaryKey(new Integer(1));
//
//            int refSystemCodeId = refSystemCode.getRefSystemCodeId().intValue();
//            log.debug("refSystemCodeId : " + refSystemCodeId);
//            if(refSystemCodeId == 1)
//            {
//                assertEquals(values[refSystemCodeId - 1][1], refSystemCode.getCode());
//                assertEquals(values[refSystemCodeId - 1][2], refSystemCode.getCodeType());
//                assertEquals(values[refSystemCodeId - 1][3], refSystemCode.getCodeTitle());
//                assertEquals(values[refSystemCodeId - 1][4], refSystemCode.getDecode());
//                assertEquals(Integer.valueOf(values[refSystemCodeId - 1][5]), refSystemCode.getRefCodeOrder());
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
//    private RefSystemCodeHome lookupHome() throws Exception
//    {
//        Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (RefSystemCodeHome) ctx.lookup("RefSystemCodeHome");
//        return (RefSystemCodeHome) PortableRemoteObject.narrow(home, RefSystemCodeHome.class);
//    }
//}