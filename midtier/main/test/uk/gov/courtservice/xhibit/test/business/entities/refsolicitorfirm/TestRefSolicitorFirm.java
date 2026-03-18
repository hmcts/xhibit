//package uk.gov.courtservice.xhibit.test.business.entities.refsolicitorfirm;
//
////jdk
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm;
//import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirmHome;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//import java.util.Collection;
//import java.util.Iterator;
//
///**
// * <p>Title: TestRefSolicitorFirm</p>
// * <p>Description: This class only tests finder methods of RefSolicitorFirm</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class TestRefSolicitorFirm extends TestCase
//{
//    private Logger log =  CSServices.getLogger(TestRefSolicitorFirm.class);
//    private String[][] values = new String[3][8];
//
//    public TestRefSolicitorFirm(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp()
//    {
//        try
//       {
//           log.debug("setUp() --- Delete from XHB_REF_SOLICITOR_FIRM");
//	      TestUtils.execSql("Delete from XHB_REF_SOLICITOR_FIRM");
//	      TestUtils.execSql("Delete from XHB_COURT");
//	      TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	      String sqlAddr ="INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES (\n"
//	     +1 +", 'Addr1', 'Addr2', 'Addr3', 'addr4','town', 'county', 'postcode', 'country','1-dec-02','1-dec-02','Bush','Bush Senior'," +1+")";
//	      TestUtils.execSql(sqlAddr);
//
//	      String sqlCourt ="INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES (\n"
//	      +1 +", 'courtType', 'circuit', 'courtName', 'cci', 'prefix', 'name', '1-dec-02', '1-dec-02', 'bush', 'bush senior', "+ 1 +","+ 1+")";
//	      TestUtils.execSql(sqlCourt);
//
//
//           for(int i = 0; i < 3; i++)
//           {
//               System.out.println(i);
//               values[i][0] = String.valueOf(i + 1);
//               values[i][1] = "Solicitor_First_Name " + (i + 1);
//               values[i][2] = String.valueOf(1);
//	       values[i][3] = String.valueOf(1);
//	       values[i][4] = "O";
//	       values[i][5] = "short";
//	       values[i][6] = "dxref";
//	       values[i][7] = "vat";
//           }
//
//           for(int j = 0; j < values.length; j++)
//           {
//
//               StringBuffer sql = new StringBuffer();
//               sql .append("INSERT INTO XHB_REF_SOLICITOR_FIRM ( REF_SOLICITOR_FIRM_ID, SOLICITOR_FIRM_NAME, CREST_SOF_ID, COURT_ID, OBS_IND, SHORT_NAME, ");
//               sql.append("DX_REF, VAT_NO, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID) VALUES ( ");
//               sql.append(Integer.valueOf(values[j][0]) + ", '" + values[j][1] + "', " + Integer.valueOf(values[j][2]) + ", "+ Integer.valueOf(values[j][3]) + ",");
//               sql.append("'" + values[j][4] + "','" + values[j][5] + "','" + values[j][6] + "','" + values[j][7] + "',");
//	       sql.append("'1-dec-02', '1-dec-02', 'C', 'L', 1, 1 )");
//
//               TestUtils.execSql(sql.toString());
//           }
//       }
//       catch(Exception e)
//       {
//           log.debug("Caught exception in setUp()");
//           log.debug("Exception = " + e);
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
//            RefSolicitorFirmHome home = lookupHome();
//            log.debug("testCreate() - Got RefSolicitorFirmHome");
//            RefSolicitorFirm local = home.create(new Integer(4), new Integer(4), new Integer(4));
//            log.debug("refSolicitorFirmId : " + local.getRefSolicitorFirmId());
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
//    public void testFindRefSolicitorFirmsByCourtId()
//    {
//        try
//        {
//            RefSolicitorFirmHome home = lookupHome();
//            log.debug("testFindRefSolicitorFirmsByCourtId() - Got RefSolicitorFirmHome");
//
//            Collection refSolicitorFirms = home.findByCourtId(new Integer("1"));
//            log.debug("No. of rows returned : " + refSolicitorFirms.size());
//            assertEquals(3, refSolicitorFirms.size());
//
//            Iterator it = refSolicitorFirms.iterator();
//            while (it.hasNext()) {
//                RefSolicitorFirm refSolicitorFirm = (RefSolicitorFirm)it.next();
//                int refSolicitorFirmId = refSolicitorFirm.getRefSolicitorFirmId().intValue();
//                log.debug("refSolicitorFirmId : " + refSolicitorFirmId);
//
//                if(refSolicitorFirmId >= 1 && refSolicitorFirmId <= 3)
//                {
//                    assertEquals(values[refSolicitorFirmId - 1][1], refSolicitorFirm.getSolicitorFirmName());
//                    assertEquals(Integer.valueOf(values[refSolicitorFirmId - 1][2]), refSolicitorFirm.getCrestSofId());
//		    assertEquals(Integer.valueOf(values[refSolicitorFirmId - 1][3]), refSolicitorFirm.getCourtId());
//		    assertEquals(values[refSolicitorFirmId - 1][4], refSolicitorFirm.getObsInd());
//		    assertEquals(values[refSolicitorFirmId - 1][5], refSolicitorFirm.getShortName());
//		    assertEquals(values[refSolicitorFirmId - 1][6], refSolicitorFirm.getDxRef());
//		    assertEquals(values[refSolicitorFirmId - 1][7], refSolicitorFirm.getVatNo());
//		    //assertEquals(Integer.valueOf(vos[refSolicitorFirmId - 1][1]), refSolicitorFirm.getCrestSofId());
//
//                }
//                else throw new Exception();
//            }
//        }
//        catch(Exception e)
//        {
//            log.debug("findAllRefSolicitorFirms() is failed");
//            e.printStackTrace();
//            fail();
//        }
//	finally
//	{
//	    try
//	    {
//		log.debug("deleting data");
//		TestUtils.execSql("Delete from XHB_REF_SOLICITOR_FIRM");
//		TestUtils.execSql("Delete from XHB_COURT");
//		TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	    }
//	    catch(Exception e)
//	    {
//		log.debug("findAllRefsolfirms() finally()  is failed");
//		e.printStackTrace();
//		fail();
//	    }
//
//
//	}
//    }
//
//
//    public void testFindByPrimaryKey()
//    {
//        try
//        {
//            RefSolicitorFirmHome home = lookupHome();
//            log.debug("testFindByPrimaryKey() - Got RefSolicitorFirmHome");
//            RefSolicitorFirm refSolicitorFirm = home.findByPrimaryKey(new Integer(1));
//
//            int refSolicitorFirmId = refSolicitorFirm.getRefSolicitorFirmId().intValue();
//            log.debug("refSolicitorFirmId : " + refSolicitorFirmId);
//            if(refSolicitorFirmId == 1)
//            {
//                assertEquals(values[0][1], refSolicitorFirm.getSolicitorFirmName());
//                assertEquals(Integer.valueOf(values[0][2]), refSolicitorFirm.getCourtId());
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
//		TestUtils.execSql("Delete from XHB_REF_SOLICITOR_FIRM");
//		TestUtils.execSql("Delete from XHB_COURT");
//		TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	    }
//	    catch(Exception e)
//	    {
//		log.debug("findAllRefsolfirms() finally()  is failed");
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
//    private RefSolicitorFirmHome lookupHome() throws Exception
//    {
//        Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (RefSolicitorFirmHome) ctx.lookup("RefSolicitorFirmHome");
//        return (RefSolicitorFirmHome) PortableRemoteObject.narrow(home, RefSolicitorFirmHome.class);
//    }
//}