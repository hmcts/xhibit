//package uk.gov.courtservice.xhibit.test.business.entities.sitting;
//
//import junit.framework.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.xhibit.business.vos.entities.SittingBasicValue;
//import java.util.Date;
//import uk.gov.courtservice.xhibit.business.vos.entities.SittingComplexValue;
//import java.util.Collection;
//import uk.gov.courtservice.xhibit.business.vos.entities.HearingListBasicValue;
//import java.util.ArrayList;
//import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
//import java.util.Iterator;
//
///**
// *
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class SittingValueTest extends TestCase
//{
//    private Logger log =  CSServices.getLogger(SittingValueTest.class);
//
//    public SittingValueTest(String s)
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
//            Integer sittingID = new Integer(1);
//            Integer sittingSequenceNo = new Integer(1);
//            Integer refJustice1ID = new Integer(1);
//            Integer refJustice2ID = new Integer(1);
//            Integer refJustice4ID = new Integer(1);
//            Integer refJustice3ID = new Integer(1);
//            Integer listID = new Integer(1);
//            Integer refJudgeID = new Integer(1);
//            Integer courtRoomID = new Integer(1);
//            Integer courtSiteID = new Integer(1);
//            Integer version = new Integer(1);
//            String sittingNote = "sittingNote";
//            String justiceName1 = "justiceName1";
//            String justiceName2 = "justiceName2";
//            String justiceName3 = "justiceName3";
//            String justiceName4 = "justiceName4";
//            String isFloating = "1";
//            String isSittingJudge = "0";
//            Date sittingTime = new Date();
//
//            SittingBasicValue basic = new SittingBasicValue(sittingID, version);
//            basic.setSittingSequenceNo(sittingSequenceNo);
//            basic.setRefJustice1ID(refJustice1ID);
//            basic.setRefJustice2ID(refJustice2ID);
//            basic.setRefJustice4ID(refJustice4ID);
//            basic.setRefJustice3ID(refJustice3ID);
//            basic.setListID(listID);
//            basic.setRefJudgeID(refJudgeID);
//            basic.setCourtRoomID(courtRoomID);
//            basic.setCourtSiteID(courtSiteID);
//            basic.setSittingNote(sittingNote);
//            basic.setJusticeName1(justiceName1);
//            basic.setJusticeName2(justiceName2);
//            basic.setJusticeName3(justiceName3);
//            basic.setJusticeName4(justiceName4);
//            basic.setIsFloating(isFloating);
//            basic.setIsSittingJudge(isSittingJudge);
//            basic.setSittingTime(sittingTime);
//
//            log.debug("sittingID");
//            assertEquals(sittingID, basic.getId());
//            log.debug("sittingSequenceNo");
//            assertEquals(sittingSequenceNo, basic.getSittingSequenceNo());
//            log.debug("refJustice1ID");
//            assertEquals(refJustice1ID, basic.getRefJustice1ID());
//            log.debug("refJustice2ID");
//            assertEquals(refJustice2ID, basic.getRefJustice2ID());
//            log.debug("refJustice4ID");
//            assertEquals(refJustice4ID, basic.getRefJustice4ID());
//            log.debug("refJustice3ID");
//            assertEquals(refJustice3ID, basic.getRefJustice3ID());
//            log.debug("listID");
//            assertEquals(listID, basic.getListID());
//            log.debug("refJudgeID");
//            assertEquals(refJudgeID, basic.getRefJudgeID());
//            log.debug("courtRoomID");
//            assertEquals(courtRoomID, basic.getCourtRoomID());
//            log.debug("courtSiteID");
//            assertEquals(courtSiteID, basic.getCourtSiteID());
//            log.debug("sittingNote");
//            assertEquals(sittingNote, basic.getSittingNote());
//            log.debug("justiceName1");
//            assertEquals(justiceName1, basic.getJusticeName1());
//            log.debug("justiceName2");
//            assertEquals(justiceName2, basic.getJusticeName2());
//            log.debug("justiceName3");
//            assertEquals(justiceName3, basic.getJusticeName3());
//            log.debug("justiceName4");
//            assertEquals(justiceName4, basic.getJusticeName4());
//            log.debug("isFloating");
//            assertEquals(isFloating, basic.getIsFloating());
//            log.debug("isSittingJudge");
//            assertEquals(isSittingJudge, basic.getIsSittingJudge());
//            log.debug("sittingTime");
//            assertEquals(sittingTime, basic.getSittingTime());
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
//            ScheduledHearingBasicValue shValue1 = new ScheduledHearingBasicValue(new Integer(1), new Integer(1));
//            ScheduledHearingBasicValue shValue2 = new ScheduledHearingBasicValue(new Integer(2), new Integer(2));
//
//
//            Collection scheduledHearings = new ArrayList();
//            scheduledHearings.add(shValue1);
//            scheduledHearings.add(shValue2);
//
//            HearingListBasicValue hlValue = new HearingListBasicValue(new Integer(1), new Integer(1));
//
//
//            SittingComplexValue complex = new SittingComplexValue();
//            complex.setHearingList(hlValue);
//            complex.setScheduledHearings(scheduledHearings);
//
//            log.debug("scheduledHearings");
//            assertEquals(2, complex.getScheduledHearings().size());
//            Iterator it = complex.getScheduledHearings().iterator();
//            while(it.hasNext())
//            {
//                ScheduledHearingBasicValue value = (ScheduledHearingBasicValue)it.next();
//                assertEquals(value.getId(), value.getVersion());
//            }
//
//            log.debug("HearingList - ID");
//            assertEquals(hlValue.getId(), complex.getHearingList().getId());
//            log.debug("HearingList - version");
//            assertEquals(hlValue.getVersion(), complex.getHearingList().getVersion());
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