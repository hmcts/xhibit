//package uk.gov.courtservice.xhibit.test.business.entities.shjudge;
//
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHJudgeBasicValue;
//
//public class SHJudgeValueTest extends TestCase
//{
//    private Logger log =  CSServices.getLogger(SHJudgeValueTest.class);
//
//    public SHJudgeValueTest(String s)
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
//            Integer shJudgeID = new Integer(1);
//            Integer hearingID = new Integer(1);
//            Integer refJudgeID = new Integer(1);
//            Integer shAttendeeID = new Integer(1);
//            Integer version = new Integer(1);
//            String deputyHCJ = "deputyHCJ";
//
//            SHJudgeBasicValue basic = new SHJudgeBasicValue(shJudgeID, version);
//            basic.setRefJudgeID(refJudgeID);
//            basic.setShAttendeeID(shAttendeeID);
//            basic.setDeputyHCJ(deputyHCJ);
//
//            log.debug("shJudgeID");
//            assertEquals(shJudgeID, basic.getId());
//            log.debug("refJudgeID");
//            assertEquals(refJudgeID, basic.getRefJudgeID());
//            log.debug("shAttendeeID");
//            assertEquals(shAttendeeID, basic.getShAttendeeID());
//            log.debug("deputyHCJ");
//            assertEquals(deputyHCJ, basic.getDeputyHCJ());
//            log.debug("version");
//            assertEquals(version, basic.getVersion());
//        }
//        catch(Exception e)
//        {
//            log.debug("testBasic() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//}