//package uk.gov.courtservice.xhibit.test.business.entities.shstaff;
//
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHStaffBasicValue;
//
//
//
//public class SHStaffValueTest extends TestCase
//{
//    private Logger log =  CSServices.getLogger(SHStaffValueTest.class);
//
//    public SHStaffValueTest(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp()
//    {
//    }
//
//    protected void tearDown()
//    {
//    }
//
//    public void testBasic()
//    {
//        try
//        {
//            Integer shStaffID = new Integer(1);
//            Integer verson = new Integer(1);
//            String staffRole = "staffRole";
//            String staffName = "staffName";
//
//            SHStaffBasicValue basic = new SHStaffBasicValue(shStaffID, verson);
//            basic.setStaffName(staffName);
//            basic.setStaffRole(staffRole);
//
//            log.debug("shStaffID");
//            assertEquals(shStaffID, basic.getId());
//            log.debug("staffName");
//            assertEquals(staffName, basic.getStaffName());
//            log.debug("staffRole");
//            assertEquals(staffRole, basic.getStaffRole());
//            log.debug("verson");
//            assertEquals(verson, basic.getVersion());
//        }
//        catch(Exception e)
//        {
//            log.debug("testBasic() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//}