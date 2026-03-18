//package uk.gov.courtservice.xhibit.web.publicdisplay.storage.test.impl.test;
//
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.DisplayRotationSetData;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.RotationSetDisplayDocument;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayURI;
//import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.StoredObject;
//import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.StorerFactory;
//import uk.gov.courtservice.xhibit.web.publicdisplay.test.framework.TestCaseWithInitialization;
//import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;
//import uk.gov.courtservice.xhibit.web.publicdisplay.types.rotationset.DisplayRotationSet;
//
//import java.io.DataInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
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
// * @version $Revision: 1.6 $
// */
//public class TestFileStore extends TestCaseWithInitialization
//{
//    /**
//     * Creates a new TestFileStore object.
//     *
//     * @param s TODO:
//     */
//    public TestFileStore(String s)
//            throws Exception
//    {
//        super(s);
//    }
//
//    /**
//     * TODO:
//     *
//     * @param file           TODO:
//     * @param renderedString TODO:
//     * @throws IOException TODO:
//     */
//    public static void assertFileRenderedOkay(File file, String renderedString)
//            throws IOException
//    {
//        assertTrue("File was not created '" + file.getAbsolutePath(), file.exists());
//
//        DataInputStream dis = new DataInputStream(new FileInputStream(file));
//        byte[] bytes = new byte[renderedString.length()];
//        dis.readFully(bytes);
//        dis.close();
//        System.out.println(new String(bytes));
//        assertEquals("The stored value is not the same as the rendered string.", new String(bytes), (renderedString));
//    }
//
//    /**
//     * TODO:
//     */
//    public void testStoreDocument()
//    {
//        int[] courts = {1, 2, 3, 100};
//        DisplayDocument inDoc = new DisplayDocument(new DisplayDocumentURI(Locale.ENGLISH, 1, courtDetailDisplayDocumentType, courts));
//        DisplayRotationSet inRS = new DisplayRotationSet(new DisplayRotationSetData(new DisplayURI("pd://display/Isleworth/Site1/Entrance/Display1"), new RotationSetDisplayDocument[]
//        {
//            new RotationSetDisplayDocument(displayDocumentUri, 1)
//        }, 1, 1, "42in"));
//        inDoc.fetchData();
//        inDoc.render();
//        inRS.render();
//        inDoc.store();
//        inRS.store();
//
//        StoredObject storedDoc = StorerFactory.getInstance().retrieve(inDoc.getUri());
//        assertEquals(inDoc.getRenderedString(), storedDoc.getText());
//        System.out.println(storedDoc.getText());
//
//        StoredObject storedRS = StorerFactory.getInstance().retrieve(inRS.getUri());
//        assertEquals(inRS.getRenderedString(), storedRS.getText());
//        System.out.println(storedRS.getText());
//    }
//}
//