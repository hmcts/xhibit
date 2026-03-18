//package uk.gov.courtservice.xhibit.test.business.entities.reflegalrepresentative;
//
////jdk
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentative;
//import uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentativeHome;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//import java.util.Collection;
//import java.util.Iterator;
//
///**
// * <p>Title: TestRefLegalRepresentative</p>
// * <p>Description: This class only tests finder methods of RefLegalRepresentative</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class TestRefLegalRepresentative extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(TestRefLegalRepresentative.class);
//    private String[][] values = new String[3][7];
//
//    public TestRefLegalRepresentative(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp()
//    {
//        try
//        {
//	    log.debug("setUp() --- Delete from XHB_REF_JUDGE");
//	    TestUtils.execSql("Delete from XHB_REF_LEGAL_REPRESENTATIVE");
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
//            for(int i = 0; i < 3; i++)
//            {
//                System.out.println(i);
//                values[i][0] = String.valueOf(i + 1);
//                values[i][1] = "First_Name " + (i + 1);
//                values[i][2] = "Middle_Name " + (i + 1);
//                values[i][3] = "Surame " + (i + 1);
//                values[i][4] = "Mr " + (i + 1);
//                values[i][5] = "I " + (i + 1);
//                values[i][6] = "1";
//            }
//
//            for(int j = 0; j < values.length; j++)
//            {
//                StringBuffer sql = new StringBuffer();
//                sql .append("INSERT INTO XHB_REF_LEGAL_REPRESENTATIVE ( REF_LEGAL_REP_ID, FIRST_NAME, MIDDLE_NAME, SURNAME, TITLE, INITALS, LEGAL_REP_TYPE, ");
//                sql.append("LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID, OBS_IND ) VALUES ( ");
//                sql.append(Integer.valueOf(values[j][0]) + ", '" + values[j][1] + "', '" + values[j][2] + "', '" + values[j][3] + "', '");
//                sql.append(values[j][4] + "', '" + values[j][5] + "', '" + values[j][6] + "', ");
//                sql.append("TO_Date( '11/11/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_Date( '10/10/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'C', 'L', 1, 1, 'N')");
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
//       /* try
//        {
//            log.debug("setUp() --- Delete from XHB_REF_LEGAL_REPRESENTATIVE");
//            TestUtils.execSql("Delete from XHB_REF_LEGAL_REPRESENTATIVE");
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
//            RefLegalRepresentativeHome home = lookupHome();
//            log.debug("testCreate() - Got RefLegalRepresentativeHome");
//            RefLegalRepresentative local = home.create(new Integer(4), new Integer(4), new Integer(4));
//            log.debug("refLegalRepresentativeId : " + local.getRefLegalRepresentativeId());
//        }
//        catch(Exception e)
//        {
//            log.debug("create() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }*/
//
//    public void testFindRefLegalRepresentativesByCourtId()
//    {
//        try
//        {
//            RefLegalRepresentativeHome home = lookupHome();
//            log.debug("testFindRefLegalRepresentativesByCourtId() - Got RefLegalRepresentativeHome");
//
//            Collection refLegalRepresentatives = home.findByCourtId(new Integer("1"));
//            log.debug("No. of rows returned : " + refLegalRepresentatives.size());
//            assertEquals(3, refLegalRepresentatives.size());
//
//            Iterator it = refLegalRepresentatives.iterator();
//            while (it.hasNext()) {
//                RefLegalRepresentative refLegalRepresentative = (RefLegalRepresentative)it.next();
//                int refLegalRepId = refLegalRepresentative.getRefLegalRepId().intValue();
//                log.debug("refLegalRepId : " + refLegalRepId);
//
//                if(refLegalRepId >= 1 && refLegalRepId <= 3)
//                {
//                    assertEquals(values[refLegalRepId - 1][1], refLegalRepresentative.getFirstName());
//                    assertEquals(values[refLegalRepId - 1][2], refLegalRepresentative.getMiddleName());
//                    assertEquals(values[refLegalRepId - 1][3], refLegalRepresentative.getSurname());
//                    assertEquals(values[refLegalRepId - 1][4], refLegalRepresentative.getTitle());
//                    assertEquals(values[refLegalRepId - 1][5], refLegalRepresentative.getInitials());
//                    assertEquals(values[refLegalRepId - 1][6], refLegalRepresentative.getLegalRepType());
//                }
//                else throw new Exception();
//            }
//        }
//        catch(Exception e)
//        {
//            log.debug("findAllRefLegalRepresentatives() is failed");
//            e.printStackTrace();
//            fail();
//        }
//	finally
//	{
//	    try
//	    {
//		log.debug("deleting data");
//		TestUtils.execSql("Delete from XHB_REF_LEGAL_REPRESENTATIVE");
//		TestUtils.execSql("Delete from XHB_COURT");
//		TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	    }
//	    catch(Exception e)
//	    {
//		log.debug("findAllXHB_REF_LEGAL_REPRESENTATIVE() finally()  is failed");
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
//            RefLegalRepresentativeHome home = lookupHome();
//            log.debug("testFindByPrimaryKey() - Got RefLegalRepresentativeHome");
//            RefLegalRepresentative refLegalRepresentative = home.findByPrimaryKey(new Integer(1));
//            int refLegalRepId = refLegalRepresentative.getRefLegalRepId().intValue();
//            log.debug("refLegalRepId : " + refLegalRepId);
//            if(refLegalRepId == 1)
//            {
//                assertEquals(values[0][1], refLegalRepresentative.getFirstName());
//                assertEquals(values[0][2], refLegalRepresentative.getMiddleName());
//                assertEquals(values[0][3], refLegalRepresentative.getSurname());
//                assertEquals(values[0][4], refLegalRepresentative.getTitle());
//                assertEquals(values[0][5], refLegalRepresentative.getInitials());
//                assertEquals(values[0][6], refLegalRepresentative.getLegalRepType());
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
//		TestUtils.execSql("Delete from XHB_REF_LEGAL_REPRESENTATIVE");
//		TestUtils.execSql("Delete from XHB_COURT");
//		TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	    }
//	    catch(Exception e)
//	    {
//		log.debug("findAllXHB_REF_LEGAL_REPRESENTATIVE() finally()  is failed");
//		e.printStackTrace();
//		fail();
//	    }
//
//
//	}
//    }
//
//
//    private RefLegalRepresentativeHome lookupHome() throws Exception
//    {
//        Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (RefLegalRepresentativeHome) ctx.lookup("RefLegalRepresentativeHome");
//        return (RefLegalRepresentativeHome) PortableRemoteObject.narrow(home, RefLegalRepresentativeHome.class);
//    }
//}