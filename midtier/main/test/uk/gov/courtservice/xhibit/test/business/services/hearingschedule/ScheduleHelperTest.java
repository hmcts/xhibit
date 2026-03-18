//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.Vector;
//
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendant;
//import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendantMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;
//import uk.gov.courtservice.xhibit.business.entities.sitting.SittingMaintainer;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.schedule.ScheduleHelper;
//import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.SittingBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
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
//public class ScheduleHelperTest extends TestCase
//{
//    private Logger log =  CSServices.getLogger(ScheduleHelperTest.class);
//    private ScheduleHelper helper = new ScheduleHelper();
//    private UserTransaction ut;
//
//    public ScheduleHelperTest(String s)
//    {
//        super(s);
//    }
//
//
//    protected void setUp() throws Exception
//    {
//        InitialContext initContext = new InitialContext();
//        ut = (UserTransaction)initContext.lookup("java:comp/UserTransaction");
//    }
//
//
//    protected void tearDown()
//    {
//    }
//
//
//    public void testAdjustSHSequenceNumbers() throws Exception
//    {
//        log.debug("testAdjustSHSequenceNumbers() - start");
//
//        try
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_scheduled_hearing");
//            TestUtils.execSql("delete from xhb_sitting");
//
//            // Insert data...
//            TestUtils.execSql("insert into xhb_sitting(sitting_id, court_room_id) values(1, 1)");
//            TestUtils.execSql("insert into xhb_scheduled_hearing(scheduled_hearing_id, sitting_id, sequence_no) values(1, 1, 5)");
//            TestUtils.execSql("insert into xhb_scheduled_hearing(scheduled_hearing_id, sitting_id, sequence_no) values(2, 1, 3)");
//            TestUtils.execSql("insert into xhb_scheduled_hearing(scheduled_hearing_id, sitting_id, sequence_no, not_before_time) values(3, 1, 4, to_date('11/11/2002 13:00:00', 'mm/dd/yyyy hh24:mi:ss'))");
//            TestUtils.execSql("insert into xhb_scheduled_hearing(scheduled_hearing_id, sitting_id, sequence_no, not_before_time) values(4, 1, 2, to_date('11/11/2002 12:00:00', 'mm/dd/yyyy hh24:mi:ss'))");
//            TestUtils.execSql("insert into xhb_scheduled_hearing(scheduled_hearing_id, sitting_id, sequence_no) values(5, 1, 1)");
//
//            SittingMaintainer sM = new SittingMaintainer();
//
//            ScheduledHearingBasicValue bv = new ScheduledHearingBasicValue();
//            bv.setNotBeforeTime(Timestamp.valueOf("2002-11-11 12:30:00"));
//
//            ut.begin();
//            Sitting sitting = sM.findByPK(new Integer(1));
//            Integer seqNo = helper.adjustSHSequenceNumbers(sitting, bv);
//            ut.commit();
//
//            log.debug("Sequence No: " + seqNo);
//            assertEquals(new Integer(4), seqNo);
//        }
//        catch(Exception e)
//        {
//            log.debug("testAdjustSHSequenceNumbers() is failed");
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_scheduled_hearing");
//            TestUtils.execSql("delete from xhb_sitting");
//        }
//    }
//
//
//    public void testCaseExists() throws Exception
//    {
//        log.debug("testCaseExists() - start");
//
//        try
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_case");
//
//            // Insert data...
//            TestUtils.execSql("insert into xhb_case(case_id, case_number, case_type, court_id) values(1, 999, 'c', 1)");
//
//            ut.begin();
//            boolean expected = helper.caseExists(new Integer(999), "c", new Integer(1));
//            ut.commit();
//
//            assertEquals(true, expected);
//        }
//        catch(Exception e)
//        {
//            log.debug("testCaseExists() is failed");
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_case");
//        }
//    }
//
//
//    public void testCreateSitting1() throws Exception
//    {
//        log.debug("testCreateSitting1() - start");
//
//        try
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_hearing_list");
//            TestUtils.execSql("delete from xhb_sitting");
//
//            // Insert data...
//            TestUtils.execSql("insert into xhb_hearing_list(list_id, court_id, start_date) values(1, 1, to_date('02/25/2003 00:00:00', 'mm/dd/yyyy hh24:mi:ss'))");
//            TestUtils.execSql("insert into xhb_hearing_list(list_id, court_id, start_date) values(2, 1, to_date('02/26/2003 00:00:00', 'mm/dd/yyyy hh24:mi:ss'))");
//
//            ut.begin();
//            Sitting expected = helper.createSitting(new Integer(1),
//                    new Integer(1), new Integer(1), Timestamp.valueOf("2003-02-25 13:00:00"));
//
//            log.debug("courtRoomId");
//            assertEquals(new Integer(1), expected.getCourtRoomId());
//            log.debug("courtSiteId");
//            assertEquals(new Integer(1), expected.getCourtSiteId());
//            log.debug("sittingTime");
//            assertEquals(Timestamp.valueOf("2003-02-25 13:00:00").getTime(),
//                         expected.getSittingTime().getTime());
//            ut.commit();
//        }
//        catch(Exception e)
//        {
//            log.debug("testCreateSitting1() is failed");
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_hearing_list");
//            TestUtils.execSql("delete from xhb_sitting");
//        }
//    }
//
//    // would not work
//    /*public void testCreateSitting2() throws Exception
//    {
//        log.debug("testCreateSitting2() - start");
//
//        try
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_court_room");
//            TestUtils.execSql("delete from xhb_court_site");
//            TestUtils.execSql("delete from xhb_court");
//            TestUtils.execSql("delete from xhb_hearing_list");
//            TestUtils.execSql("delete from xhb_sitting");
//
//            // Insert data...
//            TestUtils.execSql("insert into xhb_court(court_id, court_type) values(1, 'court_type')");
//            TestUtils.execSql("insert into xhb_court_site(court_site_id, court_id) values(1, 1)");
//            TestUtils.execSql("insert into xhb_court_room(court_room_id, court_site_id) values(1, 1)");
//
//            TestUtils.execSql("insert into xhb_hearing_list(list_id, court_id, start_date) values(1, 1, to_date('02/25/2003 00:00:00', 'mm/dd/yyyy hh24:mi:ss'))");
//            TestUtils.execSql("insert into xhb_hearing_list(list_id, court_id, start_date) values(2, 1, to_date('02/26/2003 00:00:00', 'mm/dd/yyyy hh24:mi:ss'))");
//
//            ut.begin();
//            Sitting expected = helper.createSitting(new Integer(1), Timestamp.valueOf("2003-02-25 13:00:00"));
//            ut.commit();
//
//            log.debug("courtRoomId");
//            assertEquals(new Integer(1), expected.getCourtRoomId());
//            log.debug("courtSiteId");
//            assertEquals(new Integer(1), expected.getCourtSiteId());
//            log.debug("sittingTime");
//            assertEquals(Timestamp.valueOf("2003-02-25 13:00:00").getTime(),
//                         expected.getSittingTime().getTime());
//
//        }
//        catch(Exception e)
//        {
//            log.debug("testCreateSitting2() is failed");
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_court_room");
//            TestUtils.execSql("delete from xhb_court_site");
//            TestUtils.execSql("delete from xhb_court");
//            TestUtils.execSql("delete from xhb_hearing_list");
//            TestUtils.execSql("delete from xhb_sitting");
//        }
//    }*/
//
//
//    /*public void testGetCourtSiteId() throws Exception
//    {
//        log.debug("testGetCourtSiteId() - start");
//
//        try
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_court_room");
//
//            // Insert data...
//            TestUtils.execSql("insert into xhb_court_room(court_room_id, court_site_id) values(1, 1)");
//
//            ut.begin();
//            Integer expected = helper.getCourtSiteId(new Integer(1));
//            ut.commit();
//
//            log.debug("courtSiteId");
//            assertEquals(new Integer(1), expected);
//        }
//        catch(Exception e)
//        {
//            log.debug("testGetCourtSiteId() is failed");
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_court_room");
//        }
//    }*/
//
//    // would not work
//    /*public void testGetCourtRoom() throws Exception
//    {
//        log.debug("testGetCourtSiteId() - start");
//
//        try
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_court_room");
//
//            // Insert data...
//            TestUtils.execSql("insert into xhb_court_room(court_room_id, court_site_id) values(1, 1)");
//
//            ut.begin();
//            CourtRoomBasicValue expected = helper.getCourtRoom(new Integer(1));
//            ut.commit();
//
//            log.debug("courtRoomId");
//            assertEquals(new Integer(1), expected);
//            log.debug("courtSiteId");
//            assertEquals(new Integer(1), expected);
//        }
//        catch(Exception e)
//        {
//            log.debug("testGetCourtSiteId() is failed");
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_court_room");
//        }
//    }*/
//
//
//    public void testCreateSittingVo() throws Exception
//    {
//        log.debug("testCreateSitting() - start");
//
//        try
//        {
//            Integer courtRoomId = new Integer(1);
//            Integer courtSiteId = new Integer(1);
//            Date sittingTime = Timestamp.valueOf("2003-09-20 12:00:30");
//            Integer listId =new Integer(1);
//
//            SittingBasicValue bv = helper.createSittingVO(courtRoomId, courtSiteId,
//                   sittingTime, listId);
//
//            log.debug("courtRoomId");
//            assertEquals(courtRoomId, bv.getCourtRoomID());
//            log.debug("courtSiteId");
//            assertEquals(courtSiteId, bv.getCourtSiteID());
//            log.debug("sittingTime");
//            assertEquals(sittingTime.getTime(), bv.getSittingTime().getTime());
//            log.debug("listId");
//            assertEquals(listId, bv.getListID());
//        }
//        catch(Exception e)
//        {
//
//        }
//    }
//
//
//    public void testStripTime()
//    {
//        log.debug("testStripTime() - start");
//
//        try
//        {
//            Timestamp actual = Timestamp.valueOf("2003-02-27 00:00:00");
//
//            //ut.begin();
//            Date expected = helper.stripTime(actual);
//            //helper.stripTime(null);
//            //ut.commit();
//
//            log.debug("striped time");
//            assertEquals(actual.getTime(), expected.getTime());
//        }
//        catch(Exception e)
//        {
//            log.debug("testStripTime() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    /*public void testSittingExists() throws Exception
//    {
//        log.debug("testSittingExists() - start");
//
//        try
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_sitting");
//
//            // Insert data...
//            TestUtils.execSql("insert into xhb_sitting(sitting_id, court_room_id, court_site_id, sitting_time) values(1, 1, 1, to_date('02/26/2003 12:00:00', 'mm/dd/yyyy hh24:mi:ss'))");
//
//            ut.begin();
//            Sitting expected = helper.sittingExists(Timestamp.valueOf("2003-02-26 13:00:00"), new Integer(1), new Integer(1));
//
//            log.debug("sittingtime");
//            assertEquals(Timestamp.valueOf("2003-02-26 12:00:00").getTime(), expected.getSittingTime().getTime());
//            log.debug("sittingId");
//            assertEquals(new Integer(1), expected.getSittingId());
//            log.debug("courtRoomId");
//            assertEquals(new Integer(1), expected.getCourtRoomId());
//            log.debug("courtSiteId");
//            assertEquals(new Integer(1), expected.getCourtSiteId());
//            ut.commit();
//        }
//        catch(Exception e)
//        {
//            log.debug("testSittingExists() is failed");
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_sitting");
//        }
//    }*/
//
//
//    public void testCreateSHDefendants() throws Exception
//    {
//        log.debug("testCreateSHDefendants() - start");
//
//        try
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_scheduled_hearing");
//            TestUtils.execSql("delete from xhb_sched_hearing_defendant");
//
//            // Insert data...
//            TestUtils.execSql("insert into xhb_scheduled_hearing(scheduled_hearing_id, sitting_id, hearing_id) values(1, 1, 1)");
//
//            DefendantOnCaseBasicValue doc = new DefendantOnCaseBasicValue();
//            doc.setDefendantID(new Integer(1));
//            DefendantValue d = new DefendantValue();
//            d.setDefOnCaseBasicValue(doc);
//
//            Collection defendants = new ArrayList();
//            defendants.add(d);
//
//            ScheduledHearingMaintainer sh = new ScheduledHearingMaintainer();
//
//            ut.begin();
//            ScheduledHearing schedHear = sh.findByPK(new Integer(1));
//            ut.commit();
//            ut.begin();
//            helper.createSHDefendants(new Integer(1), schedHear, defendants);
//            ut.commit();
//            ut.begin();
//            SchedHearingDefendantMaintainer shd = new SchedHearingDefendantMaintainer();
//            Collection schedHearDefs = shd.findByDefOnCaseId(new Integer(1));
//
//            log.debug("collection size");
//            assertEquals(1, schedHearDefs.size());
//
//            Iterator it = schedHearDefs.iterator();
//            while(it.hasNext())
//            {
//                SchedHearingDefendant end = (SchedHearingDefendant)it.next();
//                ScheduledHearing en = end.getScheduledHearing();
//
//                log.debug("SchedHearingDefendant - defOnCaseId");
//                assertEquals(new Integer(1), end.getDefOnCaseID());
//
//                log.debug("ScheduledHearing - scheduledHearingId");
//                assertEquals(new Integer(1), en.getScheduledHearingId());
//                log.debug("ScheduledHearing - sittingId");
//                assertEquals(new Integer(1), en.getSittingId());
//                log.debug("ScheduledHearing - hearingId");
//                assertEquals(new Integer(1), en.getHearingId());
//            }
//
//            ut.commit();
//        }
//        catch(Exception e)
//        {
//            log.debug("testCreateSHDefendants() is failed");
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_scheduled_hearing");
//            TestUtils.execSql("delete from xhb_sched_hearing_defendant");
//        }
//    }
//
//
//    public void testGetOrderedScheduledHearings() throws Exception
//    {
//        log.debug("testGetOrderedScheduledHearings() - start");
//
//        try
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_scheduled_hearing");
//            TestUtils.execSql("delete from xhb_sitting");
//
//            // Insert data...
//            TestUtils.execSql("insert into xhb_sitting(sitting_id, court_room_id) values(1, 1)");
//            TestUtils.execSql("insert into xhb_scheduled_hearing(scheduled_hearing_id, sitting_id, sequence_no) values(1, 1, 5)");
//            TestUtils.execSql("insert into xhb_scheduled_hearing(scheduled_hearing_id, sitting_id, sequence_no) values(2, 1, 3)");
//            TestUtils.execSql("insert into xhb_scheduled_hearing(scheduled_hearing_id, sitting_id, sequence_no, not_before_time) values(3, 1, 4, to_date('11/11/2002 13:00:00', 'mm/dd/yyyy hh24:mi:ss'))");
//            TestUtils.execSql("insert into xhb_scheduled_hearing(scheduled_hearing_id, sitting_id, sequence_no, not_before_time) values(4, 1, 2, to_date('11/11/2002 12:00:00', 'mm/dd/yyyy hh24:mi:ss'))");
//            TestUtils.execSql("insert into xhb_scheduled_hearing(scheduled_hearing_id, sitting_id, sequence_no) values(5, 1, 1)");
//
//            SittingMaintainer sM = new SittingMaintainer();
//            String [] orderCriteria = {"sequenceNo"};
//
//            ut.begin();
//            Sitting sitting = sM.findByPK(new Integer(1));
//            Vector vec = (Vector)helper.getOrderedScheduledHearings(sitting, orderCriteria);
//            ut.commit();
//
//            log.debug("Collection size: " + vec.size());
//            assertEquals(5, vec.size());
//
//            for(int i = 0; i < vec.size(); i++)
//            {
//                ScheduledHearingBasicValue bv = (ScheduledHearingBasicValue)vec.get(i);
//                assertEquals(new Integer(i + 1), bv.getSequenceNo());
//            }
//        }
//        catch(Exception e)
//        {
//            log.debug("testGetOrderedScheduledHearings() is failed");
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            // Delete data...
//            TestUtils.execSql("delete from xhb_scheduled_hearing");
//            TestUtils.execSql("delete from xhb_sitting");
//        }
//    }
//}