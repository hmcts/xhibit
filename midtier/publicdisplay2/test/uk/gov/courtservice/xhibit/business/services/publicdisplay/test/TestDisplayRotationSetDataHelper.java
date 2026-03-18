//package uk.gov.courtservice.xhibit.business.services.publicdisplay.test;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.List;
//
//import uk.gov.courtservice.framework.testutils.DatabaseUtil;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper;
//import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplay;
//import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplayBeanHelper;
//import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSet;
//import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBeanHelper;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.DisplayRotationSetData;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.RotationSetDisplayDocument;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.DisplayRotationSetDataHelper;
//
//
///**
// * <p>Title: Unit test class for the <code>DisplayRotationSetDataHelper</code>.</p>
// * <p>Description: </p>
// * <p>
// * This unit test must be run on the application server carrying the entity beans
// * as the class that it is testing uses local interfaces. It is expected within the
// * scope of this project that the test will run within JUnitEE.
// * </p>
// * <p>
// * This unit test needs to be run in a Context with a DataSource called
// * 'XhibitOracleTXDataSource' and an user transaction.
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author Bob Boothby
// * @version 1.0
// */
//public class TestDisplayRotationSetDataHelper extends TransactionTestCase
//{
//
//    /**
//     * The name of the script relative to the classloader(s) that will be run
//     * to populate the database with test data.
//     * <p>
//     * <code>    /sql/configuration_create.sql</code>
//     * <p>
//     * Note the preceding slash is a requirement of the Weblogic classloader
//     * which does not behave like others...
//     */
//    public static final String TEST_CONFIGURATION_SCRIPT = "/sql/configuration_create.sql";
//
//    /**
//     * Create an instance of the test case, all work done will be within scope
//     * of a transaction that will be rolled back.
//     * @param name The name of the test.
//     * @throws javax.naming.NamingException If there is no available DataSource
//     * or UserTransaction.
//     */
//    public TestDisplayRotationSetDataHelper(String name) throws javax.naming.NamingException
//    {
//        super(name, true);
//    }
//
//    /**
//     * For each test start a transaction and execute the test configuration script.
//     * @throws Exception
//     */
//    protected void setUp() throws Exception
//    {
//        //Insert the configuration data into the database.
//        super.setUp();
//        URL scriptURL = this.getClass().getClassLoader().getResource(TEST_CONFIGURATION_SCRIPT);
//        DatabaseUtil.executeScript(scriptURL,';', this.connection);
//        //Shouldn't need to remove it as the super class tearDown() should
//        //roll back the transaction that encompasses the insert.
//    }
//
//    /**
//     * Tests the expected behaviour of the utility method for adding arrays
//     * to <code>List</code>s.
//     */
//    public void testAddArrayToList()
//    {
//        Object[] array =  new String[] {"a","b","c","d"};
//        List list=  new ArrayList();
//        DisplayRotationSetDataHelper.addArrayToList(array, list);
//
//        //First check that the array is of the correct size.
//        assertEquals(
//                "There are the wrong number of entries in the list.",
//                4, list.size());
//
//        //Then check that the elements in the list are correct.
//        for(int i = 0; i < array.length; i++)
//        {
//            assertEquals(
//            "The element at position " + i +
//            " of the list is not what was expected.",
//            array[i], list.get(i));
//        }
//    }
//
//    /**
//     * Checks that the display configuration data for a court that is returned
//     * is correct and properly composed.
//     */
//    public void testGetDataForCourt() throws Exception
//    {
//        int courtId = 1;
//        XhbCourt court=  XhbCourtBeanHelper.findByPrimaryKey(
//                new Integer(courtId));
//        DisplayRotationSetData[] displayRotationSetData =
//                DisplayRotationSetDataHelper.getDataForCourt(courtId, court);
//
//        //First check that we have the number of entries that we expected.
//        assertEquals(
//                "The wrong number of DisplayRotationSetData instances were returned.",
//                4, displayRotationSetData.length);
//
//        //Sort the results.
//        Arrays.sort(displayRotationSetData,
//                    DisplayRotationSetDataByDisplayComparator.getInstance());
//
//        validateNegFourthDisplay(displayRotationSetData[0]);
//
//        validateNegThirdDisplay(displayRotationSetData[1]);
//
//        validateNegSecondDisplay(displayRotationSetData[2]);
//
//        validateNegFirstDisplay(displayRotationSetData[3]);
//
//    }
//
//    /**
//     * Checks that the display configuration data for a rotation set that is returned
//     * is correct and properly composed.
//     */
//    public void testGetDisplayRotationSetData() throws Exception
//    {
//        int courtId = 1;
//        XhbDisplay display =  XhbDisplayBeanHelper.findByPrimaryKey(new Integer(-4));
//        XhbRotationSet rotationSet = display.getXhbRotationSet();
//        DisplayRotationSetData displayRotationSetData =
//                DisplayRotationSetDataHelper.getDisplayRotationSetData(courtId, display, rotationSet);
//
//        validateNegFourthDisplay(displayRotationSetData);
//    }
//
//    /**
//      * Checks that the configuration data for a display that is returned
//      * is correct and properly composed.
//      */
//     public void testGetDataForDisplayRotationSets() throws Exception
//     {
//         int courtId = 1;
//         XhbRotationSet rotationSet =
//                 XhbRotationSetBeanHelper.findByPrimaryKey(new Integer(-1));
//         DisplayRotationSetData[] displayRotationSetData =
//                 DisplayRotationSetDataHelper.getDataForDisplayRotationSets(courtId, rotationSet);
//
//         //First check that we have the number of entries that we expected.
//         assertEquals(
//                 "The wrong number of DisplayRotationSetData instances were returned.",
//                 2, displayRotationSetData.length);
//
//         //Sort the results.
//         Arrays.sort(displayRotationSetData,
//                     DisplayRotationSetDataByDisplayComparator.getInstance());
//
//         validateNegSecondDisplay(displayRotationSetData[0]);
//
//         validateNegFirstDisplay(displayRotationSetData[1]);
//     }
//
//    private void validateNegFourthDisplay(DisplayRotationSetData displayRotationSetData)
//    {
//        assertEquals("The Display was not the expected one.", -4l,
//                     displayRotationSetData.getDisplayId());
//        assertEquals("The Rotation Set was not the expected one.", -2l,
//                     displayRotationSetData.getRotationSetId());
//        assertEquals("The expected display URI was not returned: " ,
//                     "publicdisplay://display/snare/a/OUTSIDE_COURT_ROOMS_1_AND_2/Yet another something else.",
//                      displayRotationSetData.getDisplayURI().toString());
//
//        RotationSetDisplayDocument[] rsDDs =
//                displayRotationSetData.getRotationSetDisplayDocuments();
//        assertEquals("The number of display documents was incorrect.",3,rsDDs.length);
//
//        assertEquals("The page delay was wrong.", 14l ,rsDDs[0].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/AllCourtStatus:" +
//                     "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20",
//                     rsDDs[0].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 15l ,rsDDs[1].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/SummaryByName:" +
//                     "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20",
//                     rsDDs[1].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 16l ,rsDDs[2].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/JuryCurrentStatus:" +
//                     "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20",
//                     rsDDs[2].getDisplayDocumentURI().toString());
//    }
//
//    private void validateNegThirdDisplay(DisplayRotationSetData displayRotationSetData)
//    {
//        assertEquals("The Display was not the expected one.", -3l,
//                     displayRotationSetData.getDisplayId());
//        assertEquals("The Rotation Set was not the expected one.", -2l,
//                     displayRotationSetData.getRotationSetId());
//        assertEquals("The expected display URI was not returned: " ,
//                     "publicdisplay://display/snare/a/ENTRANCE_VESTIBULE/Yet another something.",
//                      displayRotationSetData.getDisplayURI().toString());
//
//        RotationSetDisplayDocument[] rsDDs =
//                displayRotationSetData.getRotationSetDisplayDocuments();
//        assertEquals("The number of display documents was incorrect.",3,rsDDs.length);
//
//        assertEquals("The page delay was wrong.", 14l ,rsDDs[0].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/AllCourtStatus:" +
//                     "4",
//                     rsDDs[0].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 15l ,rsDDs[1].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/SummaryByName:" +
//                     "4",
//                     rsDDs[1].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 16l ,rsDDs[2].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/JuryCurrentStatus:" +
//                     "4",
//                     rsDDs[2].getDisplayDocumentURI().toString());
//    }
//
//    private void validateNegSecondDisplay(DisplayRotationSetData displayRotationSetData)
//    {
//        assertEquals("The Display was not the expected one.", -2l,
//                     displayRotationSetData.getDisplayId());
//        assertEquals("The Rotation Set was not the expected one.", -1l,
//                     displayRotationSetData.getRotationSetId());
//        assertEquals("The expected display URI was not returned: " ,
//                     "publicdisplay://display/snare/a/OUTSIDE_COURT_ROOMS_1_AND_2/Something else.",
//                      displayRotationSetData.getDisplayURI().toString());
//
//        RotationSetDisplayDocument[] rsDDs =
//                displayRotationSetData.getRotationSetDisplayDocuments();
//        assertEquals("The number of display documents was incorrect.",7,rsDDs.length);
//
//        assertEquals("The page delay was wrong.", 11l ,rsDDs[0].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtDetail:" +
//                     "1",
//                     rsDDs[0].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 11l ,rsDDs[1].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtDetail:" +
//                     "2",
//                     rsDDs[1].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 11l ,rsDDs[2].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtDetail:" +
//                     "3",
//                     rsDDs[2].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 12l ,rsDDs[3].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtList:" +
//                     "1",
//                     rsDDs[3].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 12l ,rsDDs[4].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtList:" +
//                     "2",
//                     rsDDs[4].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 12l ,rsDDs[5].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtList:" +
//                     "3",
//                     rsDDs[5].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 13l ,rsDDs[6].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/DailyList:" +
//                     "1,2,3",
//                     rsDDs[6].getDisplayDocumentURI().toString());
//    }
//
//    private void validateNegFirstDisplay(DisplayRotationSetData displayRotationSetData)
//    {
//        assertEquals("The Display was not the expected one.", -1l,
//                     displayRotationSetData.getDisplayId());
//        assertEquals("The Rotation Set was not the expected one.", -1l,
//                     displayRotationSetData.getRotationSetId());
//        assertEquals("The expected display URI was not returned: " ,
//                     "publicdisplay://display/snare/a/ENTRANCE_VESTIBULE/Something.",
//                      displayRotationSetData.getDisplayURI().toString());
//
//        RotationSetDisplayDocument[] rsDDs =
//                displayRotationSetData.getRotationSetDisplayDocuments();
//        assertEquals("The number of display documents was incorrect.",7,rsDDs.length);
//
//        assertEquals("The page delay was wrong.", 11l ,rsDDs[0].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtDetail:" +
//                     "1",
//                     rsDDs[0].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 11l ,rsDDs[1].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtDetail:" +
//                     "2",
//                     rsDDs[1].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 11l ,rsDDs[2].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtDetail:" +
//                     "3",
//                     rsDDs[2].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 12l ,rsDDs[3].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtList:" +
//                     "1",
//                     rsDDs[3].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 12l ,rsDDs[4].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtList:" +
//                     "2",
//                     rsDDs[4].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 12l ,rsDDs[5].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/CourtList:" +
//                     "3",
//                     rsDDs[5].getDisplayDocumentURI().toString());
//        assertEquals("The page delay was wrong.", 13l ,rsDDs[6].getPageDelay());
//        assertEquals("The Display Document was wrong.",
//                     "publicdisplay://document/1/DailyList:" +
//                     "1,2,3",
//                     rsDDs[6].getDisplayDocumentURI().toString());
//    }
//
//    /**
//     * <p>Title: Comparator for sorting <code>DisplayRotationSetData</code> by
//     * display ID</p>
//     * <p>Description: </p>
//     * <p>Copyright: Copyright (c) 2003</p>
//     * <p>Company: EDS</p>
//     * @author Bob Boothby
//     * @version 1.0
//     */
//    private static class DisplayRotationSetDataByDisplayComparator implements Comparator
//    {
//        private static final DisplayRotationSetDataByDisplayComparator _instance =
//                new DisplayRotationSetDataByDisplayComparator();
//
//        /**
//         * Get an instance of the comparator.
//         * @return An instance of the comparator.
//         */
//        public static DisplayRotationSetDataByDisplayComparator getInstance()
//        {
//            return _instance;
//        }
//
//        private DisplayRotationSetDataByDisplayComparator(){}
//
//        /**
//         * Returns an ordering for two instances of <code>DisplayRotationSetData</code>
//         * based on the Display's ID.
//         * @param o1 The first instance of <code>DisplayRotationSetData</code>.
//         * @param o2 The second instance of <code>DisplayRotationSetData</code>.
//         * @return -1 if the o1's display ID is less than o2's, 0 if they are the same
//         * and +1 if o1's display ID is greater.
//         * @throws ClassCastException
//         */
//        public int compare(Object o1, Object o2) throws ClassCastException
//        {
//            long firstId = ((DisplayRotationSetData)o1).getDisplayId();
//            long secondId = ((DisplayRotationSetData)o2).getDisplayId();
//            long result = firstId - secondId;
//            //Doing the division means that result/result is always going to
//            //be -1, 0 or 1 and so will always be storeable in an int.
//            //If we don't do this, then we could end up with an interesting
//            //situation when the difference between the two longs is greater
//            //than Integer.MAV_VALUE.
//            return (int) (result/Math.abs(result));
//        }
//
//        /**
//         * Checks whether the passed in object is equivalent to this one.
//         * @param obj The object to check for equality.
//         * @return true if obj is equivalent to this one.
//         */
//        public boolean equals(Object obj)
//        {
//            return this==obj;
//        }
//    }
//}