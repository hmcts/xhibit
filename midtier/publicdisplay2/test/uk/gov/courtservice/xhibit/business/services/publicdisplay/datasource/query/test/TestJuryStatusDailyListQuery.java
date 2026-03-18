//package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.test;
//
//import java.util.Collection;
//import java.util.GregorianCalendar;
//import java.util.Iterator;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.test.MidTierTestConstants;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.JuryStatusDailyListQuery;
//import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.JuryStatusDailyListValue;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.JuryStatusDailyListUnassignedCasesQuery;
//
//
//
///**
// * DOCUMENT ME!
// *
// * @author pznwc5 To change the template for this generated type comment go to
// *         Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestJuryStatusDailyListQuery extends TransactionTestCase implements MidTierTestConstants
//{
//    /** DOCUMENT ME! */
//    private JuryStatusDailyListQuery query;
//    private JuryStatusDailyListUnassignedCasesQuery queryWithUnassigned;
//
//    /**
//     * Creates a new TestSummaryByNameQuery object.
//     *
//     * @param name DOCUMENT ME!
//     *
//     * @throws NamingException DOCUMENT ME!
//     */
//    public TestJuryStatusDailyListQuery(String name) throws NamingException
//    {
//        super(name, false);
//    }
//
//    /**
//     * DOCUMENT ME!
//     *
//     * @throws Exception DOCUMENT ME!
//     */
//    public void setUp() throws Exception
//    {
//        super.setUp();
//        query = new JuryStatusDailyListQuery();
//        queryWithUnassigned = new JuryStatusDailyListUnassignedCasesQuery();
//    }
//
//    /**
//     * DOCUMENT ME!
//     */
//    public void testGetJuryStatusDailyListData_manyRows() throws Exception
//    {
//        Collection juryStatusDailyList =
//            query.getData(START_DATE, COURT_ID, COURT_ROOM_IDS);
//        assertNotNull(juryStatusDailyList);
//        assertEquals(74, juryStatusDailyList.size());
//        for(Iterator iterator = juryStatusDailyList.iterator(); iterator.hasNext();)
//        {
//            JuryStatusDailyListValue value = (JuryStatusDailyListValue) iterator.next();
//            System.out.println("Value: " + value);
//
//        }
//
//    }
//
//    public void testGetJuryStatusDailyListWithUnassignedData_manyRows() throws Exception
//    {
//        Collection juryStatusDailyList =
//            queryWithUnassigned.getData(START_DATE, COURT_ID, COURT_ROOM_IDS);
//        assertNotNull(juryStatusDailyList);
//        assertEquals(77, juryStatusDailyList.size());
//        for(Iterator iterator = juryStatusDailyList.iterator(); iterator.hasNext();)
//        {
//            JuryStatusDailyListValue value = (JuryStatusDailyListValue) iterator.next();
//            System.out.println("Value: " + value);
//
//        }
//
//    }
//
//    public void testGetJuryStatusDailyList_singleRow() throws Exception
//    {
//        Collection juryStatusDailyList = query.getData(
//                new GregorianCalendar(2003, 04, 13).getTime(), 3, new int[]{31});
//        assertEquals(1, juryStatusDailyList.size());
//
//        JuryStatusDailyListValue[] value = (JuryStatusDailyListValue[])
//            juryStatusDailyList.toArray(
//                new JuryStatusDailyListValue[juryStatusDailyList.size()]);
//        System.out.println("Value: " + value[0]);
//        assertEquals(value[0].toString(),
//                     "notBeforeTime=2003-05-13 10:00:00.0;courtRoomName=Court Room 1;hearingDescription=For Trial;caseNumber=T20028828;class=class common.renderdata.JuryStatusDailyListValue;judgeName=PHIL ADER1;reportingRestricted=false;defendantNames=[20028828];hearingProgress=5;movedFromCourtRoomName=null;notBeforeTimeAsString=10:00;");
//
//    }
//
//    public void testGetJuryStatusDailyListData_negative() throws Exception
//    {
//        Collection juryStatusDailyList = null;
//        juryStatusDailyList = query.getData(
//                new GregorianCalendar(2199, 04, 13).getTime(), 3, new int[]{31});
//        assertEquals(0, juryStatusDailyList.size());
//        try
//        {
//            juryStatusDailyList = query.getData(
//                    null, 3, new int[]{31});
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            juryStatusDailyList = query.getData(
//                    null, -1, null);
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            juryStatusDailyList = query.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), 3, new int[]{});
//
//            fail("Should have thrown an exception no courts passed in the array.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        juryStatusDailyList = query.getData(
//                new GregorianCalendar(2003, 04, 13).getTime(), Integer.MAX_VALUE, new int[]{31});
//        try
//        {
//            juryStatusDailyList = query.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), Integer.MIN_VALUE, new int[]{31});
//
//            fail("Should have thrown an exception, the court id was negative.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            juryStatusDailyList = query.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), 3, null);
//
//            fail("Should have thrown an exception, the courtRoomIds were null.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//
//    }
//
//    public void testGetJuryStatusDailyListWithUnassignedData_negative() throws Exception
//    {
//        Collection juryStatusDailyList = null;
//        juryStatusDailyList = queryWithUnassigned.getData(
//                new GregorianCalendar(2199, 04, 13).getTime(), 3, new int[]{31});
//        assertEquals(0, juryStatusDailyList.size());
//        try
//        {
//            juryStatusDailyList = queryWithUnassigned.getData(
//                    null, 3, new int[]{31});
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            juryStatusDailyList = queryWithUnassigned.getData(
//                    null, -1, null);
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            juryStatusDailyList = queryWithUnassigned.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), 3, new int[]{});
//
//            fail("Should have thrown an exception no courts passed in the array.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        juryStatusDailyList = queryWithUnassigned.getData(
//                new GregorianCalendar(2003, 04, 13).getTime(), Integer.MAX_VALUE, new int[]{31});
//        try
//        {
//            juryStatusDailyList = queryWithUnassigned.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), Integer.MIN_VALUE, new int[]{31});
//
//            fail("Should have thrown an exception, the court id was negative.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            juryStatusDailyList = queryWithUnassigned.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), 3, null);
//
//            fail("Should have thrown an exception, the courtRoomIds were null.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//
//    }
//
//}
//