//package uk.gov.courtservice.xhibit.test.business.database.schedule;
//
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.Date;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.business.database.query.schedule.ScheduleQuery;
//import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.TodaysScheduleValue;
//
///**
// * <p>Title:TestSchedule</p>
// * <p>Description: This will test the query to get the todays scheudule for a day
// * or for a courtroom per court and date. </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class TestSchedule extends TestCase {
//
//    public TestSchedule(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//    }
//
//    protected void tearDown() {
//    }
//
//    /**
//     * This will test the getTodaysSchedule for a given court and date.
//     */
//    public void testGetTodaysSchedule()
//    {
//        ScheduleQuery schedulequery = new ScheduleQuery();
//
//        //Set up the input parameters to the query
//        Calendar cal = Calendar.getInstance();
//        cal.set(2003, 07, 07);
//        Integer courtId = new Integer(3);
//        Date date = cal.getTime();
//
//        TodaysScheduleValue todaysschedulevalueRet = schedulequery.getSchedule(courtId, date);
//
//        if(todaysschedulevalueRet != null && todaysschedulevalueRet.getScheduledHearings() != null)
//        {
//            assertEquals(129, todaysschedulevalueRet.getScheduledHearings().size());
//        }
//        else
//        {
//            fail("There are no records returned - TEST FAILED!");
//        }
//
//    }
//
//    /**
//     * Will get the schedules for a given court, date and court room.
//     */
//    public void testGetTodaysScheduleForCourtRoom()
//    {
//        ScheduleQuery schedulequery = new ScheduleQuery();
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(2003, 07, 07);
//        Integer courtId = new Integer(3);
//        Integer courtRoomId = new Integer(31);
//        Date date = cal.getTime();
//
//        TodaysScheduleValue todaysschedulevalueRet = schedulequery.getSchedule(courtId, date, courtRoomId);
//
//        //check that the number of hearings that was retrieved was expected
//        Collection scheduledHearings = todaysschedulevalueRet.getScheduledHearings();
//        if(todaysschedulevalueRet != null && scheduledHearings != null)
//        {
//            assertEquals(5, scheduledHearings.size());
//        }
//        else
//        {
//            fail("There are no records returned - TEST FAILED!");
//        }
//    }
//}
//