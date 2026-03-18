//package uk.gov.courtservice.xhibit.web.publicdisplay.storage.test.impl.test;
//
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
//import uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.impl.FileStorer;
//import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.Storer;
//import uk.gov.courtservice.xhibit.web.publicdisplay.test.framework.TestCaseWithInitialization;
//import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;
//
//import java.io.File;
//import java.util.Locale;
//
//
///**
// * <p/>
// * Title:
// * </p>
// * <p/>
// * <p/>
// * Description:
// * </p>
// * <p/>
// * <p/>
// * Copyright: Copyright (c) 2003
// * </p>
// * <p/>
// * <p/>
// * Company: Electronic Data Systems
// * </p>
// *
// * @author Neil Ellis
// * @version $Revision: 1.4 $
// */
//public class TestDisplayDocumentStorer extends TestCaseWithInitialization
//{
//    private static final File FILE = new File(STORE_LOCATION + File.separator + "documents" + File.separator + "court1" + File.separator + "31" + File.separator + "32" + File.separator + "33" + File.separator + "34" + File.separator + "35" + File.separator + "40" + File.separator + "44" + File.separator + "45" + File.separator + "CourtDetail.html");
//
//    /**
//     * Creates a new TestDisplayDocumentStorer object.
//     *
//     * @param s TODO:
//     */
//    public TestDisplayDocumentStorer(String s)
//            throws Exception
//    {
//        super(s);
//    }
//
//    /**
//     * TODO:
//     *
//     * @throws Exception TODO:
//     */
//    public void testStore_positive()
//            throws Exception
//    {
//        DisplayDocument inDoc = new DisplayDocument(new DisplayDocumentURI(Locale.ENGLISH, 1, courtDetailDisplayDocumentType, COURT_ROOM_IDS));
//        Storer storer = FileStorer.getInstance();
//        inDoc.setRenderedString(RENDERED_STRING);
//        storer.store(inDoc);
//        TestFileStore.assertFileRenderedOkay(FILE, RENDERED_STRING);
//    }
//
//}
//