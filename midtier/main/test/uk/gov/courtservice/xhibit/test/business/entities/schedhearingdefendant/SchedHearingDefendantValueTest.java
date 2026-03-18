//package uk.gov.courtservice.xhibit.test.business.entities.schedhearingdefendant;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingDefendantBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingDefendantComplexValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
//
//public class SchedHearingDefendantValueTest extends TestCase
//{
//    private Logger log =  CSServices.getLogger(SchedHearingDefendantValueTest.class);
//
//    public SchedHearingDefendantValueTest(String s)
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
//            Integer schedHearDefID = new Integer(1);
//            Integer scheduledHearingID = new Integer(1);
//            Integer defendantOnCaseID = new Integer(1);
//            Integer version = new Integer(1);
//
//            SchedHearingDefendantBasicValue basic = new SchedHearingDefendantBasicValue(
//                    schedHearDefID, version);
//            basic.setScheduledHearingID(scheduledHearingID);
//            basic.setDefendantOnCaseID(defendantOnCaseID);
//
//            log.debug("schedHearDefID");
//            assertEquals(schedHearDefID, basic.getId());
//            log.debug("scheduledHearingID");
//            assertEquals(scheduledHearingID, basic.getScheduledHearingID());
//            log.debug("defendantOnCaseID");
//            assertEquals(defendantOnCaseID, basic.getDefendantOnCaseID());
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
//
//    public void testComplex()
//    {
//        try
//        {
//            ScheduledHearingBasicValue shb = new ScheduledHearingBasicValue(new Integer(1), new Integer(1));
//
//            SchedHearingDefendantComplexValue complex = new SchedHearingDefendantComplexValue();
//            complex.setScheduledHearing(shb);
//
//            log.debug("ScheduledHearing - ID");
//            assertEquals(new Integer(1), complex.getScheduledHearing().getId());
//            log.debug("ScheduledHearing - version");
//            assertEquals(new Integer(1), complex.getScheduledHearing().getVersion());
//
//        }
//        catch(Exception e)
//        {
//            log.debug("testComplex() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//}