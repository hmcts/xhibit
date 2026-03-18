//package uk.gov.courtservice.xhibit.common.publicdisplay.types.uri;
//
//import java.util.Locale;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.exceptions.InvalidURIFormatException;
//
///**
// * <p/> Title:
// * </p>
// * <p/> <p/> Description:
// * </p>
// * <p/> <p/> Copyright: Copyright (c) 2003
// * </p>
// * <p/> <p/> Company: Electronic Data Systems
// * </p>
// *
// * @author Neil Ellis
// * @version $Revision: 1.8 $
// */
//public class TestPublicDisplayURI extends TestCase {
//    /**
//     * Creates a new TestPublicDisplayURI object.
//     *
//     * @param s
//     *            TODO:
//     */
//    public TestPublicDisplayURI(String s) throws Exception {
//        super(s);
//    }
//
//    /**
//     * TODO:
//     */
//    public final void testGetCourtId() {
//        DisplayDocumentURI uri = new DisplayDocumentURI("pd://document:en/111111/CourtDetail:1,2,3");
//        assertEquals(uri.getCourtId(), 111111);
//    }
//
//    /**
//     * TODO:
//     */
//    public final void testGetCourtRoomIds() {
//        DisplayDocumentURI uri = new DisplayDocumentURI("pd://document:en/1/CourtDetail:1,2,3");
//        assertEquals(3, uri.getCourtRoomIds().length);
//        assertEquals(1, uri.getCourtRoomIds()[0]);
//        assertEquals(2, uri.getCourtRoomIds()[1]);
//        assertEquals(3, uri.getCourtRoomIds()[2]);
//    }
//
//    /**
//     * TODO:
//     */
//    public final void testGetDocumentType() {
//        DisplayDocumentURI uri = new DisplayDocumentURI("pd://document:en/1/CourtDetail:1,2,3");
//        assertEquals(uri.getDocumentType(), DisplayDocumentType.getDisplayDocumentType("CourtDetail"));
//    }
//
//    /*
//     * Class to test for void DisplayDocumentURI(String)
//     */
//    public final void testPublicDisplayURIString() {
//        DisplayDocumentURI uri = new DisplayDocumentURI("pd://document:en/1/CourtDetail:1,2,3");
//        assertEquals("pd://document:en/1/CourtDetail:1,2,3", uri.toString());
//        assertEquals(1, uri.getCourtId());
//        assertEquals(3, uri.getCourtRoomIds().length);
//        assertEquals(1, uri.getCourtRoomIds()[0]);
//        assertEquals(2, uri.getCourtRoomIds()[1]);
//        assertEquals(3, uri.getCourtRoomIds()[2]);
//        assertEquals(DisplayDocumentType.getDisplayDocumentType("CourtDetail"), uri.getDocumentType());
//    }
//
//    /*
//     * Class to test for void DisplayDocumentURI(int, String, int[])
//     */
//    public final void testPublicDisplayURIintStringintArray() {
//        DisplayDocumentURI uri = new DisplayDocumentURI(Locale.ENGLISH, 1, DisplayDocumentType
//                .getDisplayDocumentType("CourtDetail"), new int[] { 1, 2, 3 });
//        assertEquals(uri.toString(), "pd://document:en/1/CourtDetail:1,2,3");
//        assertEquals(uri.getCourtId(), 1);
//        assertEquals(uri.getCourtRoomIds().length, 3);
//        assertEquals(uri.getCourtRoomIds()[0], 1);
//        assertEquals(uri.getCourtRoomIds()[1], 2);
//        assertEquals(uri.getCourtRoomIds()[2], 3);
//        assertEquals(uri.getDocumentType(), DisplayDocumentType.getDisplayDocumentType("CourtDetail"));
//    }
//
//    /**
//     * TODO:
//     */
//    public final void testValidateURI() {
//        new DisplayDocumentURI("pd://document:en/1/CourtDetail:1,2,3");
//        new DisplayDocumentURI("pd://document:en/1/CourtDetail:1");
//        new DisplayDocumentURI("pd://document:en/10/CourtDetail:1,2,3");
//        new DisplayDocumentURI("pd://document:en/1/CourtDetail:1,2,3");
//
//        try {
//            new DisplayDocumentURI("pd://document:en/10/../CourtDetail:1,2,3");
//            TestCase.fail("The url was invalid yet no exception was thrown.");
//        } catch (InvalidURIFormatException e) {
//        }
//
//        try {
//            new DisplayDocumentURI("pd://document:en/1/CourtDetail_1_2_Fred/../:1,2,3");
//            TestCase.fail("The url was invalid yet no exception was thrown.");
//        } catch (InvalidURIFormatException e) {
//        }
//    }
//}
//