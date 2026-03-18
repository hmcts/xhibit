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
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.AllCourtStatusQuery;
//import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCourtStatusValue;
//
//
//
///**
// * DOCUMENT ME!
// *
// * @author pznwc5 To change the template for this generated type comment go to
// *         Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestAllCourtStatusQuery extends TransactionTestCase implements MidTierTestConstants
//{
//    /** DOCUMENT ME! */
//    private AllCourtStatusQuery query;
//
//    /**
//     * Creates a new TestSummaryByNameQuery object.
//     *
//     * @param name DOCUMENT ME!
//     *
//     * @throws NamingException DOCUMENT ME!
//     */
//    public TestAllCourtStatusQuery(String name) throws NamingException
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
//        query = new AllCourtStatusQuery();
//    }
//
//    /**
//     * DOCUMENT ME!
//     */
//    public void testGetAllCourtStatusData_manyRows() throws Exception
//    {
//        Collection courtList =
//            query.getData(START_DATE, COURT_ID, COURT_ROOM_IDS);
//        assertNotNull(courtList);
//        assertEquals(5, courtList.size());
//        for(Iterator iterator = courtList.iterator(); iterator.hasNext();)
//        {
//            AllCourtStatusValue value = (AllCourtStatusValue) iterator.next();
//            System.out.println("Value: " + value);
//
//        }
//
//    }
//
//    public void testGetAllCourtStatusData_singleRow() throws Exception
//    {
//        Collection allCourtStatus = query.getData(
//                new GregorianCalendar(2003, 04, 13).getTime(), 3, new int[]{31});
//        assertEquals(1, allCourtStatus.size());
//
//        AllCourtStatusValue[] value = (AllCourtStatusValue[])
//        allCourtStatus.toArray(
//                new AllCourtStatusValue[allCourtStatus.size()]);
//        System.out.println("Value: " + value[0]);
//        assertEquals(value[0].toString(),
//                     "defendantNames=[];courtRoomName=Court Room 1;liveStatus=null;liveStatusTime=null;class=class common.renderdata.AllCourtStatusValue;");
//
//    }
//
//    public void testGetAllCourtStatusData_negative() throws Exception
//    {
//        Collection allCourtStatus = null;
//
//        try
//        {
//            allCourtStatus = query.getData(
//                    null, 3, new int[]{31});
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            allCourtStatus = query.getData(
//                    null, -1, null);
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            allCourtStatus = query.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), 3, new int[]{});
//
//            fail("Should have thrown an exception no courts passed in the array.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        allCourtStatus = query.getData(
//                new GregorianCalendar(2003, 04, 13).getTime(), Integer.MAX_VALUE, new int[]{31});
//        try
//        {
//            allCourtStatus = query.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), Integer.MIN_VALUE, new int[]{31});
//
//            fail("Should have thrown an exception, the court id was negative.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            allCourtStatus = query.getData(
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