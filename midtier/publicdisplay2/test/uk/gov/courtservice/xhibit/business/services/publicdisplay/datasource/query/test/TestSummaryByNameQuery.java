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
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.SummaryByNameQuery;
//import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.SummaryByNameValue;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.SummaryByNameUnassignedCasesQuery;
//
//
//
///**
// * DOCUMENT ME!
// *
// * @author pznwc5 To change the template for this generated type comment go to
// *         Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestSummaryByNameQuery extends TransactionTestCase implements MidTierTestConstants
//{
//    /** DOCUMENT ME! */
//    private SummaryByNameQuery query;
//    private SummaryByNameUnassignedCasesQuery queryWithUnassigned;
//
//    /**
//     * Creates a new TestSummaryByNameQuery object.
//     *
//     * @param name DOCUMENT ME!
//     *
//     * @throws NamingException DOCUMENT ME!
//     */
//    public TestSummaryByNameQuery(String name) throws NamingException
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
//        query = new SummaryByNameQuery();
//        queryWithUnassigned = new SummaryByNameUnassignedCasesQuery();
//    }
//
//    /**
//     * DOCUMENT ME!
//     */
//    public void testGetSummaryByNameData_manyRows() throws Exception
//    {
//        Collection summaryByNameData = query.getData(START_DATE, COURT_ID, COURT_ROOM_IDS);
//        assertNotNull(summaryByNameData);
//        /** @todo This expected value needs updating */
//        assertEquals(86, summaryByNameData.size());
//        for (Iterator iterator = summaryByNameData.iterator(); iterator.hasNext();)
//        {
//            SummaryByNameValue value = (SummaryByNameValue) iterator.next();
//            System.out.println("Value: " + value);
//
//        }
//
//    }
//
//    /**
//     * DOCUMENT ME!
//     */
//    public void testGetSummaryByNameWithUnassignedData_manyRows() throws Exception
//    {
//        Collection summaryByNameData = queryWithUnassigned.getData(START_DATE, COURT_ID, COURT_ROOM_IDS);
//        assertNotNull(summaryByNameData);
//        assertEquals(89, summaryByNameData.size());
//        for (Iterator iterator = summaryByNameData.iterator(); iterator.hasNext();)
//        {
//            SummaryByNameValue value = (SummaryByNameValue) iterator.next();
//            System.out.println("Value: " + value);
//
//        }
//
//    }
//
//    public void testGetSummaryByNameData_singleRow() throws Exception
//    {
//        Collection summaryByNameData = query.getData(
//                new GregorianCalendar(2003, 04, 13).getTime(), 3, new int[]{31});
//        assertEquals(1, summaryByNameData.size());
//
//        SummaryByNameValue[] value = (SummaryByNameValue[])
//                summaryByNameData.toArray(new SummaryByNameValue[summaryByNameData.size()]);
//        System.out.println("Value: " + value[0]);
//        assertEquals(value[0].toString(),
//                     "courtRoomName=Court Room 1;movedFromCourtRoomName=null;defendantName=20028828;class=class common.renderdata.SummaryByNameValue;notBeforeTimeAsString=10:00;notBeforeTime=2003-05-13 10:00:00.0;");
//
//    }
//
//    public void testGetSummaryByNameData_negative() throws Exception
//    {
//        Collection summaryByNameData = null;
//        summaryByNameData = query.getData(
//                new GregorianCalendar(2199, 04, 13).getTime(), 3, new int[]{31});
//        assertEquals(0, summaryByNameData.size());
//        try
//        {
//            summaryByNameData = query.getData(
//                    null, 3, new int[]{31});
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            summaryByNameData = query.getData(
//                    null, -1, null);
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            summaryByNameData = query.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), 3, new int[]{});
//
//            fail("Should have thrown an exception no courts passed in the array.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        summaryByNameData = query.getData(
//                new GregorianCalendar(2003, 04, 13).getTime(), Integer.MAX_VALUE, new int[]{31});
//        try
//        {
//            summaryByNameData = query.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), Integer.MIN_VALUE, new int[]{31});
//
//            fail("Should have thrown an exception, the court id was negative.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            summaryByNameData = query.getData(
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
//    public void testGetSummaryByNameWithUnassignedData_negative() throws Exception
//    {
//        Collection summaryByNameData = null;
//        summaryByNameData = queryWithUnassigned.getData(
//                new GregorianCalendar(2199, 04, 13).getTime(), 3, new int[]{31});
//        assertEquals(0, summaryByNameData.size());
//        try
//        {
//            summaryByNameData = queryWithUnassigned.getData(
//                    null, 3, new int[]{31});
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            summaryByNameData = queryWithUnassigned.getData(
//                    null, -1, null);
//            fail("Passed a null as date, should have thrown an exception.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            summaryByNameData = queryWithUnassigned.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), 3, new int[]{});
//
//            fail("Should have thrown an exception no courts passed in the array.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        summaryByNameData = queryWithUnassigned.getData(
//                new GregorianCalendar(2003, 04, 13).getTime(), Integer.MAX_VALUE, new int[]{31});
//        try
//        {
//            summaryByNameData = queryWithUnassigned.getData(
//                    new GregorianCalendar(2003, 04, 13).getTime(), Integer.MIN_VALUE, new int[]{31});
//
//            fail("Should have thrown an exception, the court id was negative.");
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        try
//        {
//            summaryByNameData = queryWithUnassigned.getData(
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