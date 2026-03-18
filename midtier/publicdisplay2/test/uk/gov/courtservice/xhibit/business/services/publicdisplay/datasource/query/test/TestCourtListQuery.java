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
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.CourtListQuery;
//import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtListValue;
//
//
//
///**
// * DOCUMENT ME!
// *
// * @author pznwc5 To change the template for this generated type comment go to
// *         Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestCourtListQuery extends TransactionTestCase implements MidTierTestConstants
//{
//    /** DOCUMENT ME! */
//    private CourtListQuery query;
//
//    /**
//     * Creates a new TestSummaryByNameQuery object.
//     *
//     * @param name DOCUMENT ME!
//     *
//     * @throws NamingException DOCUMENT ME!
//     */
//    public TestCourtListQuery(String name) throws NamingException
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
//        query = new CourtListQuery();
//    }
//
//    /**
//     * DOCUMENT ME!
//     */
//    public void testGetCourtListData_manyRows() throws Exception
//    {
//        Collection courtList =
//            query.getData(START_DATE, COURT_ID, COURT_ROOM_IDS);
//        assertNotNull(courtList);
//        assertEquals(78, courtList.size());
//        for(Iterator iterator = courtList.iterator(); iterator.hasNext();)
//        {
//            CourtListValue value = (CourtListValue) iterator.next();
//            System.out.println("Value: " + value);
//
//        }
//
//    }
//
//    public void testGetCourtListData_singleRow() throws Exception
//    {
//        Collection courtList = query.getData(
//                new GregorianCalendar(2003, 04, 13).getTime(), 3, new int[]{31});
//        assertEquals(1, courtList.size());
//
//        CourtListValue[] value = (CourtListValue[])
//        courtList.toArray(
//                new CourtListValue[courtList.size()]);
//        System.out.println("Value: " + value[0]);
//        assertEquals(value[0].toString(),
//                     "notBeforeTime=2003-05-13 10:00:00.0;courtRoomName=Court Room 1;hearingDescription=For Trial;caseNumber=T20028828;class=class common.renderdata.CourtListValue;reportingRestricted=false;defendantNames=[20028828];hearingProgress=5;movedFromCourtRoomName=null;notBeforeTimeAsString=10:00;");
//
//    }
//
//    public void testGetCourtListData_negative() throws Exception
//    {
//        Collection courtList = null;
//        courtList = query.getData(
//                new GregorianCalendar(2199, 04, 13).getTime(), 3, new int[]{31});
//        assertEquals(0, courtList.size());
//        try
//        {
//            courtList = query.getData(
//                    null, 3, new int[]{31});
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            courtList = query.getData(
//                    null, -1, null);
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            courtList = query.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), 3, new int[]{});
//
//            fail("Should have thrown an exception no courts passed in the array.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        courtList = query.getData(
//                new GregorianCalendar(2003, 04, 13).getTime(), Integer.MAX_VALUE, new int[]{31});
//        try
//        {
//            courtList = query.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), Integer.MIN_VALUE, new int[]{31});
//
//            fail("Should have thrown an exception, the court id was negative.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            courtList = query.getData(
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