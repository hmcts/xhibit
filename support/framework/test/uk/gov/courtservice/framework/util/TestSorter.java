//package uk.gov.courtservice.framework.util;
//
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Vector;
//
//import junit.framework.TestCase;
//
///**
// * <p>
// * Title: TestSorter
// * </p>
// * <p>
// * Description: Test harness for the Sorter Utility Class
// * </p>
// * <p>
// * Copyright: Copyright (c) 2002
// * </p>
// * <p>
// * Company: EDS
// * </p>
// * 
// * @author AW Daley
// * @version 1.0
// */
///*
// * Ref Date Author Description
// * 
// * 315, 03-04-2003 AW Daley Initial version.
// */
//public class TestSorter extends TestCase {
//    private TestData[] unsortedArray = new TestData[8];
//
//    private TestData[] expectedAscArray = new TestData[8];
//
//    private TestData[] expectedDscArray = new TestData[8];
//
//    private Vector unsortedVector = new Vector();
//
//    private String[] keys = { "date" };
//
//    public TestSorter(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//        try {
//            buildTestData();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void tearDown() {
//    }
//
//    private void buildTestData() throws ParseException {
//        SimpleDateFormat format = new SimpleDateFormat();
//        TestData element1 = new TestData(new Timestamp(format.parse("03/01/03 12:00:00").getTime()));
//        unsortedArray[0] = element1;
//        expectedAscArray[2] = element1;
//        unsortedVector.add(element1);
//        expectedDscArray[5] = element1;
//
//        TestData element2 = new TestData(new Timestamp(format.parse("06/01/03 12:00:00").getTime()));
//        unsortedArray[1] = element2;
//        expectedAscArray[5] = element2;
//        unsortedVector.add(element2);
//        expectedDscArray[2] = element2;
//
//        TestData element3 = new TestData(null);
//        unsortedArray[2] = element3;
//        unsortedVector.add(element3);
//        expectedAscArray[0] = element3;
//        expectedDscArray[7] = element3;
//
//        TestData element4 = new TestData(new Timestamp(format.parse("08/01/03 12:00:00").getTime()));
//        unsortedArray[3] = element4;
//        unsortedVector.add(element4);
//        expectedAscArray[7] = element4;
//        expectedDscArray[0] = element4;
//
//        TestData element5 = new TestData(new Timestamp(format.parse("07/01/03 12:00:00").getTime()));
//        unsortedArray[4] = element5;
//        unsortedVector.add(element5);
//        expectedAscArray[6] = element5;
//        expectedDscArray[1] = element5;
//
//        TestData element6 = new TestData(new Timestamp(format.parse("05/01/03 12:00:00").getTime()));
//        unsortedArray[5] = element6;
//        unsortedVector.add(element6);
//        expectedAscArray[4] = element6;
//        expectedDscArray[3] = element6;
//
//        TestData element7 = new TestData(new Timestamp(format.parse("04/01/03 12:00:00").getTime()));
//        unsortedArray[6] = element7;
//        unsortedVector.add(element7);
//        expectedAscArray[3] = element7;
//        expectedDscArray[4] = element7;
//
//        TestData element8 = new TestData(new Timestamp(format.parse("02/01/03 12:00:00").getTime()));
//        unsortedArray[7] = element8;
//        unsortedVector.add(element8);
//        expectedAscArray[1] = element8;
//        expectedDscArray[6] = element8;
//    }
//
//    public void testAscendingSort() {
//        Sorter.sort(unsortedArray, keys, Sorter.ASCENDING);
//
//        for (int i = 0; i < unsortedArray.length; i++)
//            assertEquals(unsortedArray[i], expectedAscArray[i]);
//    }
//
//    public void testDescendingSort() {
//        Sorter.sort(unsortedArray, keys, Sorter.DESCENDING);
//
//        for (int i = 0; i < unsortedArray.length; i++)
//            assertEquals(unsortedArray[i], expectedDscArray[i]);
//    }
//
//    public void testSort() {
//        Sorter.sort(unsortedVector, keys);
//
//        TestData[] sortedArray = new TestData[8];
//
//        unsortedVector.toArray(sortedArray);
//
//        for (int i = 0; i < sortedArray.length; i++)
//            assertEquals(sortedArray[i], expectedAscArray[i]);
//    }
//
//    /**
//     * <p>
//     * Title: TestData
//     * </p>
//     * <p>
//     * Description: Test data to sort
//     * </p>
//     * <p>
//     * Copyright: Copyright (c) 2002
//     * </p>
//     * <p>
//     * Company: EDS
//     * </p>
//     * 
//     * @author AW Daley
//     * @version 1.0
//     */
//    public class TestData {
//        private Timestamp date;
//
//        public TestData(Timestamp date) {
//            this.date = date;
//        }
//
//        public Timestamp getDate() {
//            return date;
//        }
//
//        public void setDate(Timestamp date) {
//            this.date = date;
//        }
//    }
//}
//