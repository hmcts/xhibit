//package uk.gov.courtservice.xhibit.web.publicdisplay.configuration;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.DisplayRotationSetData;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.RotationSetDisplayDocument;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayURI;
//import uk.gov.courtservice.xhibit.web.publicdisplay.types.RenderChanges;
//import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;
//
//import java.util.Arrays;
//import java.util.Locale;
//
//
///**
// * <p>Title: Test class for <code>DisplayDocumentReferenceManager</code>.</p>
// * <p>Description: </p>
// * <p/>
// * This test class is designed to check the overall management of Display
// * Document References by the <code>DisplayDocumentReferenceManager</code>.
// * In it's present naive form it just does numerical checking. It should
// * in future be tightened up to do more sophisticated type checking.
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// *
// * @author Bob Boothby
// * @version 1.0
// */
//public class TestDisplayDocumentReferenceManager extends TestCase
//{
//    public DisplayDocumentURI testDisplayURI1_1 = new DisplayDocumentURI(Locale.ENGLISH, 1, DisplayDocumentType.ALL_COURT_STATUS, new int[]{1, 2, 3});
//
//    public DisplayDocumentURI testDisplayURI2_1 = new DisplayDocumentURI(Locale.ENGLISH, 1, DisplayDocumentType.ALL_COURT_STATUS, new int[]{3, 4, 5});
//    public DisplayDocumentURI testDisplayURI2_2 = new DisplayDocumentURI(Locale.ENGLISH, 1, DisplayDocumentType.COURT_DETAIL, new int[]{3});
//    public DisplayDocumentURI testDisplayURI2_3 = new DisplayDocumentURI(Locale.ENGLISH, 1, DisplayDocumentType.COURT_DETAIL, new int[]{4});
//    public DisplayDocumentURI testDisplayURI2_4 = new DisplayDocumentURI(Locale.ENGLISH, 1, DisplayDocumentType.COURT_DETAIL, new int[]{5});
//
//    public DisplayDocumentURI testDisplayURI3_1 = new DisplayDocumentURI(Locale.ENGLISH, 1, DisplayDocumentType.COURT_LIST, new int[]{5, 6, 7});
//    public DisplayDocumentURI testDisplayURI3_2 = new DisplayDocumentURI(Locale.ENGLISH, 1, DisplayDocumentType.COURT_DETAIL, new int[]{5});
//    public DisplayDocumentURI testDisplayURI3_3 = new DisplayDocumentURI(Locale.ENGLISH, 1, DisplayDocumentType.COURT_DETAIL, new int[]{6});
//    public DisplayDocumentURI testDisplayURI3_4 = new DisplayDocumentURI(Locale.ENGLISH, 1, DisplayDocumentType.COURT_DETAIL, new int[]{7});
//
//    public RotationSetDisplayDocument rsDD1_1 =
//            new RotationSetDisplayDocument(testDisplayURI1_1, 11L);
//    public RotationSetDisplayDocument rsDD2_1 =
//            new RotationSetDisplayDocument(testDisplayURI2_1, 21L);
//    public RotationSetDisplayDocument rsDD2_2 =
//            new RotationSetDisplayDocument(testDisplayURI2_2, 22L);
//    public RotationSetDisplayDocument rsDD2_3 =
//            new RotationSetDisplayDocument(testDisplayURI2_3, 23L);
//    public RotationSetDisplayDocument rsDD2_4 =
//            new RotationSetDisplayDocument(testDisplayURI2_4, 24L);
//    public RotationSetDisplayDocument rsDD3_1 =
//            new RotationSetDisplayDocument(testDisplayURI3_1, 31L);
//    public RotationSetDisplayDocument rsDD3_2 =
//            new RotationSetDisplayDocument(testDisplayURI3_2, 32L);
//    public RotationSetDisplayDocument rsDD3_3 =
//            new RotationSetDisplayDocument(testDisplayURI3_3, 33L);
//    public RotationSetDisplayDocument rsDD3_4 =
//            new RotationSetDisplayDocument(testDisplayURI3_4, 34L);
//
//    DisplayURI display1 = new DisplayURI("aCourtHouse", "aCourtSite", "aLocation", "aDisplay");
//    DisplayURI display2 = new DisplayURI("aSecondCourtHouse", "aSecondCourtSite", "aSecondLocation", "aSecondDisplay");
//    DisplayURI display3 = new DisplayURI("aThirdCourtHouse", "aThirdCourtSite", "aThirdLocation", "aThirdDisplay");
//
//    DisplayRotationSetData display1RotationSetData = new DisplayRotationSetData(display1, new RotationSetDisplayDocument[]{rsDD1_1}, 1, 1, "42in");
//
//    DisplayRotationSetData display2RotationSetData = new DisplayRotationSetData(display2,
//                                                                                new RotationSetDisplayDocument[]{rsDD2_1, rsDD2_2, rsDD2_3, rsDD2_4},
//                                                                                1, 1, "42in");
//
//    DisplayRotationSetData display3RotationSetData = new DisplayRotationSetData(display3,
//                                                                                new RotationSetDisplayDocument[]{rsDD3_1, rsDD3_2, rsDD3_3, rsDD3_4},
//                                                                                1, 1, "42in");
//
//    protected DisplayDocumentReferenceManager testable = null;
//
//    /**
//     * Creates a new TestDisplayDocumentReferenceManager object.
//     *
//     * @param s TODO:
//     */
//    public TestDisplayDocumentReferenceManager(String s)
//    {
//        super(s);
//    }
//
//    /**
//     * This method tests the basic behaviour of the DisplayDocumentReferenceManager
//     * when a single DisplayRotationSetData is first added and then removed.
//     */
//    public void testBasicBehaviour()
//    {
//        //First add a brand new DisplayRotationSetData.
//        testable.addDisplayDocumentReferences(display1RotationSetData);
//
//        //Check for first rendering.
//        RenderChanges firstRendering = new RenderChanges();
//        testable.fillInRenderChanges(firstRendering);
//        assertRenderChangesSizes(firstRendering, 1, 0, 0, 0);
//
//        checkDisplayDocumentArrays(firstRendering.getDocumentsToStartRendering(),
//                                   new DisplayDocumentURI[]{testDisplayURI1_1});
//
//
//        //Check that the document is now available for normal data changes.
//        RenderChanges changes = testable.getRenderChanges(new DisplayDocumentType[]{DisplayDocumentType.ALL_COURT_STATUS},
//                                                          new CourtRoomIdentifier(new Integer(1), new Integer(1)));
//        assertRenderChangesSizes(changes, 1, 0, 0, 0);
//
//        checkDisplayDocumentArrays(firstRendering.getDocumentsToStartRendering(),
//                                   new DisplayDocumentURI[]{testDisplayURI1_1});
//
//        //Now remove the DisplayRotationSetData.
//        testable.removeDisplayDocumentReferences(display1RotationSetData);
//
//        //Check that we get the stop rendering.
//        RenderChanges stopRendering = new RenderChanges();
//        testable.fillInRenderChanges(stopRendering);
//        assertRenderChangesSizes(stopRendering, 0, 1, 0, 0);
//
//        checkDisplayDocumentArrays(stopRendering.getDocumentsToStopRendering(),
//                                   new DisplayDocumentURI[]{testDisplayURI1_1});
//
//        //Check that we get two All Court Status documents for court room 3.
//        changes = testable.getRenderChanges(new DisplayDocumentType[]{DisplayDocumentType.ALL_COURT_STATUS},
//                                            new CourtRoomIdentifier(new Integer(1), new Integer(1)));
//        assertRenderChangesSizes(changes, 0, 0, 0, 0);
//    }
//
//    /**
//     * This method tests the more complex DisplayRotationSetData that occurs
//     * when several DisplayRotationSetData instances are added and removed.
//     */
//    public void testComplexBehaviour()
//    {
//        //First two new DisplayRotationSetData.
//        testable.addDisplayDocumentReferences(display1RotationSetData);
//        testable.addDisplayDocumentReferences(display2RotationSetData);
//
//        //Check for first rendering.
//        RenderChanges firstRendering = new RenderChanges();
//        testable.fillInRenderChanges(firstRendering);
//        //Should be 5 as we have 5 completely new documents.
//        assertRenderChangesSizes(firstRendering, 5, 0, 0, 0);
//        checkDisplayDocumentArrays(firstRendering.getDocumentsToStartRendering(),
//                                   new DisplayDocumentURI[]
//                                   {
//                                       testDisplayURI1_1, testDisplayURI2_1, testDisplayURI2_2,
//                                       testDisplayURI2_3, testDisplayURI2_4
//                                   });
//
//        //Next third new DisplayRotationSetData.
//        testable.addDisplayDocumentReferences(display3RotationSetData);
//        //Check for first rendering.
//        firstRendering = new RenderChanges();
//        testable.fillInRenderChanges(firstRendering);
//        //Should be 3 not 4 as one of the documents already has had it's
//        //first rendering.
//        assertRenderChangesSizes(firstRendering, 3, 0, 0, 0);
//        checkDisplayDocumentArrays(firstRendering.getDocumentsToStartRendering(),
//                                   new DisplayDocumentURI[]
//                                   {
//                                       testDisplayURI3_1, testDisplayURI3_3, testDisplayURI3_4
//                                   });
//
//        //Check that the appropriate All Court Status documents are now available
//        //for normal data changes.
//        RenderChanges changes = testable.getRenderChanges(new DisplayDocumentType[]{DisplayDocumentType.ALL_COURT_STATUS},
//                                                          new CourtRoomIdentifier(new Integer(1), new Integer(3)));
//        //Should be two returned for all court status court room 3.
//        assertRenderChangesSizes(changes, 2, 0, 0, 0);
//        checkDisplayDocumentArrays(changes.getDocumentsToStartRendering(),
//                                   new DisplayDocumentURI[]
//                                   {
//                                       testDisplayURI1_1, testDisplayURI2_1
//                                   });
//
//        //Check the null case for a document type that is not in use.
//        changes = testable.getRenderChanges(new DisplayDocumentType[]{DisplayDocumentType.JURY_CURRENT_STATUS},
//                                            new CourtRoomIdentifier(new Integer(1), new Integer(3)));
//        //Should be none returned for jury current status court room 3.
//        assertRenderChangesSizes(changes, 0, 0, 0, 0);
//
//        //Check that we work for multiple document types.
//        changes = testable.getRenderChanges(new DisplayDocumentType[]
//        {DisplayDocumentType.ALL_COURT_STATUS,
//         DisplayDocumentType.COURT_DETAIL,
//         DisplayDocumentType.COURT_LIST},
//                                            new CourtRoomIdentifier(new Integer(1), new Integer(5)));
//        //Should be three documents of these types relevant to court room 5.
//        assertRenderChangesSizes(changes, 3, 0, 0, 0);
//        checkDisplayDocumentArrays(changes.getDocumentsToStartRendering(),
//                                   new DisplayDocumentURI[]
//                                   {
//                                       testDisplayURI2_1, testDisplayURI2_4, testDisplayURI3_1
//                                   });
//
//        //Now remove the DisplayRotationSetData for display 3.
//        testable.removeDisplayDocumentReferences(display3RotationSetData);
//        //Check that we get the correct stop rendering.
//        RenderChanges stopRendering = new RenderChanges();
//        testable.fillInRenderChanges(stopRendering);
//        //One of the four display documents for display 3 is being rendered
//        //for display 2 and so should not stop rendering.
//        //That is the court detail for court 5.
//        assertRenderChangesSizes(stopRendering, 0, 3, 0, 0);
//        checkDisplayDocumentArrays(stopRendering.getDocumentsToStopRendering(),
//                                   new DisplayDocumentURI[]
//                                   {
//                                       testDisplayURI3_1, testDisplayURI3_3, testDisplayURI3_4
//                                   });
//
//        //Check that we work for multiple document types having removed a
//        //display.
//        changes = testable.getRenderChanges(new DisplayDocumentType[]
//        {DisplayDocumentType.ALL_COURT_STATUS,
//         DisplayDocumentType.COURT_DETAIL,
//         DisplayDocumentType.COURT_LIST},
//                                            new CourtRoomIdentifier(new Integer(1), new Integer(5)));
//        //Should now be two documents of these types relevant to court room 5
//        //As there should no longer be a court list relevant.
//        assertRenderChangesSizes(changes, 2, 0, 0, 0);
//        checkDisplayDocumentArrays(changes.getDocumentsToStartRendering(),
//                                   new DisplayDocumentURI[]
//                                   {
//                                       testDisplayURI2_1, testDisplayURI2_4
//                                   });
//
//        //Now remove the DisplayRotationSetData for displays 1 and 2.
//        testable.removeDisplayDocumentReferences(display1RotationSetData);
//        testable.removeDisplayDocumentReferences(display2RotationSetData);
//        //Check that we get the correct stop rendering.
//        stopRendering = new RenderChanges();
//        testable.fillInRenderChanges(stopRendering);
//        //Should be 5 documents to stop rendering.
//        assertRenderChangesSizes(stopRendering, 0, 5, 0, 0);
//        checkDisplayDocumentArrays(stopRendering.getDocumentsToStopRendering(),
//                                   new DisplayDocumentURI[]
//                                   {
//                                       testDisplayURI1_1, testDisplayURI2_1, testDisplayURI2_2,
//                                       testDisplayURI2_3, testDisplayURI2_4
//                                   });
//    }
//
//    /**
//     * TODO:
//     */
//    protected void setUp()
//    {
//        testable = new DisplayDocumentReferenceManager();
//    }
//
//    /**
//     * TODO:
//     */
//    protected void tearDown()
//    {
//        testable = null;
//    }
//
//    private void assertRenderChangesSizes(RenderChanges changes,
//                                          int numberOfStartDocuments, int numberOfStopDocuments,
//                                          int numberOfStartRotationSets, int numberOfStopRotationSets)
//    {
//        assertEquals("Wrong number of 'documents to start rendering'.",
//                     numberOfStartDocuments,
//                     changes.getDocumentsToStartRendering().length);
//        assertEquals("Wrong number of 'documents to stop rendering'.",
//                     numberOfStopDocuments,
//                     changes.getDocumentsToStopRendering().length);
//        assertEquals("Wrong number of 'rotation sets to start rendering'.",
//                     numberOfStartRotationSets,
//                     changes.getDisplayRotationSetsToStartRendering().length);
//        assertEquals("Wrong number of 'rotation sets to stop rendering'.",
//                     numberOfStopRotationSets,
//                     changes.getDisplayRotationSetsToStopRendering().length);
//    }
//
//    private void checkDisplayDocumentArrays(DisplayDocument[] displayDocuments,
//                                            DisplayDocumentURI[] displayDocumentURIs)
//    {
//        //First wrap the input array of DisplayDocumentURI in instances of
//        //DisplayDocument.
//        DisplayDocument[] checkDisplayDocuments =
//                new DisplayDocument[displayDocumentURIs.length];
//        for (int i = displayDocumentURIs.length - 1; i >= 0; i--)
//        {
//            checkDisplayDocuments[i] = new DisplayDocument(displayDocumentURIs[i]);
//        }
//
//        //Now sort both DisplayDocument arrays.
//        Arrays.sort(displayDocuments, DisplayDocumentComparator.getInstance());
//        Arrays.sort(checkDisplayDocuments, DisplayDocumentComparator.getInstance());
//
//        //Check that the arrays prove equal.
//        assertTrue("The Display Documents passed in do not contain the expected URIs",
//                   Arrays.equals(displayDocuments, checkDisplayDocuments));
//
//    }
//}