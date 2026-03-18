//
//package uk.gov.courtservice.xhibit.test.business.entities.scheduledhearing;
//
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.test.business.entities.sitting.SittingValueTest;
//import java.util.Date;
//import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.SittingBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingComplexValue;
//
//
//
//public class ScheduledHearingValueTest extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(SittingValueTest.class);
//
//    public ScheduledHearingValueTest(String s)
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
//            Integer scheduledHearingID = new Integer(1);
//            Integer sequenceNo = new Integer(1);
//            Integer hearingProgress = new Integer(1);
//            Integer sittingID = new Integer(1);
//            Integer hearingID = new Integer(1);
//            Integer refCourtReporterID = new Integer(1);
//            Integer linkedSHID = new Integer(1);
//            Integer version = new Integer(1);
//            Date notBeforeTime = new Date();
//            Date originalTime = new Date();
//            Date endTime = new Date();
//            Date startTime = new Date();
//            Date dateOfHearing = new Date();
//            String listingNote = "listingNote";
//            String currentStatus = "currentStatus";
//            String movedFrom = "movedFrom";
//            Boolean isCaseActive = new Boolean(true);
//
//            ScheduledHearingBasicValue basic = new ScheduledHearingBasicValue(
//                    scheduledHearingID, version);
//            basic.setSequenceNo(sequenceNo);
//            basic.setHearingProgress(hearingProgress);
//            basic.setSittingID(sittingID);
//            basic.setHearingID(hearingID);
//            //basic.setRefCourtReporterID(refCourtReporterID);
//            basic.setLinkedSHID(linkedSHID);
//            basic.setNotBeforeTime(notBeforeTime);
//            basic.setOriginalTime(originalTime);
//            basic.setEndTime(endTime);
//            basic.setStartTime(startTime);
//            basic.setDateOfHearing(dateOfHearing);
//            basic.setListingNote(listingNote);
//            //basic.setCurrentStatus(currentStatus);
//            basic.setMovedFrom(movedFrom);
//            basic.setIsCaseActive(isCaseActive);
//
//
//            log.debug("scheduledHearingID");
//            assertEquals(scheduledHearingID, basic.getId());
//            log.debug("sequenceNo");
//            assertEquals(sequenceNo, basic.getSequenceNo());
//            log.debug("hearingProgress");
//            assertEquals(hearingProgress, basic.getHearingProgress());
//            log.debug("sittingID");
//            assertEquals(sittingID, basic.getSittingID());
//            log.debug("hearingID");
//            assertEquals(hearingID, basic.getHearingID());
//            //log.debug("refCourtReporterID");
//            //assertEquals(refCourtReporterID, basic.getRefCourtReporterID());
//            log.debug("linkedSHID");
//            assertEquals(linkedSHID, basic.getLinkedSHID());
//            log.debug("notBeforeTime");
//            assertEquals(notBeforeTime, basic.getNotBeforeTime());
//            log.debug("originalTime");
//            assertEquals(originalTime, basic.getOriginalTime());
//            log.debug("endTime");
//            assertEquals(endTime, basic.getEndTime());
//            log.debug("startTime");
//            assertEquals(startTime, basic.getStartTime());
//            log.debug("dateOfHearing");
//            assertEquals(dateOfHearing, basic.getDateOfHearing());
//            log.debug("listingNote");
//            assertEquals(listingNote, basic.getListingNote());
//            log.debug("movedFrom");
//            assertEquals(movedFrom, basic.getMovedFrom());
//            log.debug("isCaseActive");
//            assertEquals(isCaseActive, basic.getIsCaseActive());
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
//            HearingBasicValue hearing = new HearingBasicValue(new Integer(1), new Integer(1));
//            SittingBasicValue sitting = new SittingBasicValue(new Integer(1), new Integer(1));
//
//            ScheduledHearingComplexValue complex = new ScheduledHearingComplexValue();
//            complex.setHearing(hearing);
//            complex.setSitting(sitting);
//
//            log.debug("Hearing - ID");
//            assertEquals(new Integer(1), complex.getHearing().getId());
//            log.debug("Hearing - version");
//            assertEquals(new Integer(1), complex.getHearing().getVersion());
//
//            log.debug("Sitting - ID");
//            assertEquals(new Integer(1), complex.getSitting().getId());
//            log.debug("Sitting - version");
//            assertEquals(new Integer(1), complex.getSitting().getVersion());
//        }
//        catch(Exception e)
//        {
//            log.debug("testComplex() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//}
//