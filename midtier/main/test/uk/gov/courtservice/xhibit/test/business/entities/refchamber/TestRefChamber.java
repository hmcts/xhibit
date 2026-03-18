//package uk.gov.courtservice.xhibit.test.business.entities.refchamber;
//
////jdk
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.address.Address;
//import uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamber;
//import uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamberHome;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//import java.util.Collection;
//import java.util.Iterator;
//
//
///**
// * <p>Title: TestRefChamber</p>
// * <p>Description: This class only tests finder methods of RefChamber</p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class TestRefChamber extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(TestRefChamber.class);
//    String[][] values = new String[3][9];
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//    public  static String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//    public  static String court2SQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 2, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//    public  static String chamberDifCourtSQL = ("INSERT INTO XHB_REF_CHAMBER ( REF_CHAMBER_ID, XHB_VERSION, OBS_IND, IS_GLOBAL, DX_REF, LOCATION_CODE, CREST_CHAMBER_ID, FIRM_NAME, ADDRESS_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID) VALUES ( 4, 'xhbVersion 3', '3', '3', 'DX_REF 3', 'C3', 3, 'FIRM_NAME3', 1,'1-dec-02', '1-dec-02', 'created_by', 'last_updated_by', 1, 2)");
//
//    public TestRefChamber(String s)
//    {
//	super(s);
//    }
//
//    protected void setUp() throws Exception
//    {
//
//	log("setUp()");
//	log("deleting data");
//	TestUtils.execSql("Delete from XHB_REF_CHAMBER");
//	TestUtils.execSql("delete from xhb_court");
//	TestUtils.execSql("Delete From XHB_ADDRESS");
//	log("Insert Address and Court Record");
//	TestUtils.execSql(addrSQL);
//	TestUtils.execSql(courtSQL);
//
//	for(int i = 0; i < 3; i++)
//	{
//	    System.out.println(i);
//	    values[i][0] = String.valueOf(i + 1);
//	    values[i][1] = "xhbVersion " + (i + 1);
//	    values[i][2] = String.valueOf(i + 1);
//	    values[i][3] = String.valueOf(i + 1);
//	    values[i][4] = "DX_REF " + (i + 1);
//	    values[i][5] = "C" + (i + 1);
//	    values[i][6] = String.valueOf(i + 1);
//	    values[i][7] = "FIRM_NAME" + (i + 1);
//	    //Address ID
//	    values[i][8] = "1";
//	}
//
//	for(int j = 0; j < values.length; j++)
//	{
//	    StringBuffer sql = new StringBuffer();
//	    sql.append("INSERT INTO XHB_REF_CHAMBER ( REF_CHAMBER_ID, XHB_VERSION, ");
//	    sql.append("OBS_IND, IS_GLOBAL, DX_REF, LOCATION_CODE, ");
//	    sql.append("CREST_CHAMBER_ID, FIRM_NAME, ");
//	    sql.append("ADDRESS_ID, LAST_UPDATE_DATE,CREATION_DATE, ");
//	    sql.append("CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID) VALUES ( ");
//	    sql.append(Integer.valueOf(values[j][0]) + ", '" + values[j][1] + "', '" + values[j][2] + "', '");
//	    sql.append(values[j][3] + "', '" + values[j][4] + "', '" + values[j][5] + "', " + Integer.valueOf(values[j][6]) + ", '" +values[j][7] + "', 1");
//	    sql.append(",'1-dec-02', '1-dec-02', 'created_by', 'last_updated_by', "+ new Integer(1) +", " + new Integer(1) +")");
//
//	    log("SQL - " + sql.toString());
//	    TestUtils.execSql(sql.toString());
//	}
//    }
//
//    protected void tearDown()throws Exception
//    {
//	log("tearDown()");
//	log("deleting data");
//	TestUtils.execSql("Delete from XHB_REF_CHAMBER");
//	TestUtils.execSql("delete from xhb_court");
//	TestUtils.execSql("Delete From XHB_ADDRESS");
//    }
//
//    /*public void testCreate()
//    {
//	try
//	{
//	    RefChamberHome home = lookupHome();
//	    log.debug("testCreate() - Got RefChamberHome");
//	    RefChamber local = home.create(new Integer(4), new Integer(4), new Integer(4));
//	    log.debug("refChamberId : " + local.getRefChamberId());
//	}
//	catch(Exception e)
//	{
//	    log.debug("create() is failed");
//	    e.printStackTrace();
//	    fail();
//	}
//    }*/
//
//    public void testFindRefChambersByCourtId()
//    {
//
//	try
//	{
//	    TestUtils.execSql(court2SQL);
//	    TestUtils.execSql(chamberDifCourtSQL);
//	    Collection refchambers;
//	    log("testFindAllRefChambersByCourtId() start");
//
//	    RefChamberHome home = lookupHome();
//	    log("testFindAllRefChambersByCourtId() - Got RefChamberHome");
//
//	    refchambers = home.findByCourtId(new Integer("1"));
//	    log("No. of rows returned : " + refchambers.size());
//	    assertEquals(3, refchambers.size());
//	    String obsolete;
//
//	    log("update one of the chamber entries to be obsolete");
//	    obsolete = "update XHB_REF_CHAMBER set obs_ind = 'Y' where REF_CHAMBER_ID = 1";
//	    log(obsolete);
//	    TestUtils.execSql(obsolete);
//	    log("Changed one record to be obsolete");
//
//	    refchambers = home.findByCourtId(new Integer("1"));
//	    log("No. of rows returned : " + refchambers.size());
//	    assertEquals(2, refchambers.size());
//
//	    log("update obsolete chamber entry to active again");
//	    obsolete = "update XHB_REF_CHAMBER set obs_ind = 'N' where REF_CHAMBER_ID = 1";
//	    log(obsolete);
//	    TestUtils.execSql(obsolete);
//	    ;
//
//	    refchambers = home.findByCourtId(new Integer("1"));
//	    log("No. of rows returned : " + refchambers.size());
//	    assertEquals(3, refchambers.size());
//
//	    Iterator it = refchambers.iterator();
//	    while (it.hasNext()) {
//		RefChamber refChamber = (RefChamber)it.next();
//		int refChamberId = refChamber.getRefChamberId().intValue() - 1;
//		log.debug("refChamberId : " + refChamberId);
//
//		if(refChamberId >= 0 && refChamberId <= 2) {
///** @todo The trouble with this 'fix' is that it pays scant regard to the order of the left-over columns - they need to be re-numbered.
//    Unfortunately, just as the person before me, I have no time to do this right now.
//		    assertEquals(values[refChamberId][1], refChamber.getXhbVersion());
//		    assertEquals(Integer.valueOf(vos[refChamberId][2]), refChamber.getCrestJudgeId());
//		    assertEquals(vos[refChamberId][3], refChamber.getFirstName()); */
//		    assertEquals(values[refChamberId][4], refChamber.getDxRef());
//		    assertEquals(values[refChamberId][7], refChamber.getFirmName());
///*                  assertEquals(new Integer(1), refChamber.getCourtId()); */
//		}
//		else throw new Exception();
//	    }
//	}
//	catch(Exception e)
//	{
//	    log.debug("findAllRefChambers() is failed");
//	    e.printStackTrace();
//	    fail();
//	}
//    }
//
//    public void testFindByPrimaryKey()
//    {
//	try
//	{
//	    RefChamberHome home = lookupHome();
//	    log("testFindByPrimaryKey() - Got RefChamberHome");
//	    RefChamber local = home.findByPrimaryKey(new Integer(1));
//	    log("refChamberId : " + local.getRefChamberId());
//	    assertEquals(1, local.getRefChamberId().intValue());
//	}
//	catch(Exception e)
//	{
//	    log("findByPrimaryKey() is failed");
//	    e.printStackTrace();
//	    fail();
//	}
//    }
//
//    public void testGetAddress()
//   {
//       try
//       {
//	   log("testGetAddress() start");
//	   RefChamberHome home = lookupHome();
//	   RefChamber local = home.findByPrimaryKey(new Integer(1));
//	   log("refChamberId : " + local.getRefChamberId());
//	   assertEquals(new Integer(1), local.getRefChamberId());
//	   Address address = local.getAddress();
//	   assertEquals("12 Napier Rd", address.getAddress1());
//	   log("****addressID : " + address.getAddressId());
//	   log("****address1 : " + address.getAddress1());
//       }
//       catch(Exception e)
//       {
//	   log("testGetAddress() is failed");
//	   e.printStackTrace();
//	   fail();
//       }
//    }
//
//
//    private void log (String msg)
//    {
//	log.debug(msg);
//    }
//
//    private RefChamberHome lookupHome() throws Exception
//    {
//	Context ctx = CSServices.getServiceLocator().getInitialContext();
//	Object home = (RefChamberHome) ctx.lookup("RefChamberHome");
//	return (RefChamberHome) PortableRemoteObject.narrow(home, RefChamberHome.class);
//    }
//}