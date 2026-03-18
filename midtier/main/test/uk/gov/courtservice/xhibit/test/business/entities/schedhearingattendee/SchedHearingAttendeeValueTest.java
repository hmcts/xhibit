//package uk.gov.courtservice.xhibit.test.business.entities.schedhearingattendee;
//
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingAttendeeBasicValue;
//import java.util.Date;
//
//public class SchedHearingAttendeeValueTest extends TestCase
//{
//    private Logger log =  CSServices.getLogger(SchedHearingAttendeeValueTest.class);
//
//    public SchedHearingAttendeeValueTest(String s)
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
//            Integer shAttendeeID = new Integer(1);
//            Integer attendeeID = new Integer(1);
//            Integer sheduledHearingID = new Integer(1);
//            Integer shStaffID = new Integer(1);
//            Integer shLegRepID = new Integer(1);
//            Integer shJusticeID = new Integer(1);
//            Integer refJudgeID = new Integer(1);
//            Integer refCourtReporterID = new Integer(1);
//            Integer version = new Integer(1);
//            Date datetimeEnd = new Date();
//            Date datetimeStart = new Date();
//            String attendeeType = new String("attendeeType");
//
//            SchedHearingAttendeeBasicValue basic = new SchedHearingAttendeeBasicValue(
//                    shAttendeeID, version);
//            basic.setSheduledHearingID(sheduledHearingID);
//            basic.setShStaffID(shStaffID);
//            //basic.setShLegRepID(shLegRepID);
//            basic.setShJusticeID(shJusticeID);
//            basic.setRefJudgeID(refJudgeID);
//            basic.setRefCourtReporterID(refCourtReporterID);
//            basic.setAttendeeType(attendeeType);
//
//
//            log.debug("shAttendeeID");
//            assertEquals(shAttendeeID, basic.getId());
//            log.debug("sheduledHearingID");
//            assertEquals(sheduledHearingID, basic.getSheduledHearingID());
//            log.debug("shStaffID");
//            assertEquals(shStaffID, basic.getShStaffID());
//            //log.debug("shLegRepID");
//            //assertEquals(shLegRepID, basic.getShLegRepID());
//            log.debug("shJusticeID");
//            assertEquals(shJusticeID, basic.getShJusticeID());
//            log.debug("refJudgeID");
//            assertEquals(refJudgeID, basic.getRefJudgeID());
//            log.debug("refCourtReporterID");
//            assertEquals(refCourtReporterID, basic.getRefCourtReporterID());
//            log.debug("attendeeType");
//            assertEquals(attendeeType, basic.getAttendeeType());
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