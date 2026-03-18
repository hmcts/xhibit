//package uk.gov.courtservice.xhibit.web.publicdisplay.storage.test.impl.test;
//
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.DisplayRotationSetData;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.RotationSetDisplayDocument;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayURI;
//import uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.impl.FileStorer;
//import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.Storer;
//import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.StoreException;
//import uk.gov.courtservice.xhibit.web.publicdisplay.test.framework.TestCaseWithInitialization;
//import uk.gov.courtservice.xhibit.web.publicdisplay.types.rotationset.DisplayRotationSet;
//
//import java.io.File;
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
// * @version $Revision: 1.5 $
// */
//public class TestRotationSetStorer extends TestCaseWithInitialization
//{
//    private static final DisplayURI URI_1 = new DisplayURI("pd://display/Isleworth/Site1/EntranceVestibule/Screen1");
//    private static final String RENDERED_STRING = "                changed= true;\n" +
//            "                tempDocument= new DocumentReference('http://localhost:5001/PublicDisplay/FileServlet?uri=pd://document/1/CourtDetail:1',11);\n" +
//            "                rotationSet.add(tempDocument);\n" +
//            "                tempDocument= new DocumentReference('http://localhost:5001/PublicDisplay/FileServlet?uri=pd://document/1/CourtDetail:2',11);\n" +
//            "                rotationSet.add(tempDocument);\n" +
//            "                tempDocument= new DocumentReference('http://localhost:5001/PublicDisplay/FileServlet?uri=pd://document/1/CourtDetail:3',11);\n" +
//            "                rotationSet.add(tempDocument);\n" +
//            "                tempDocument= new DocumentReference('http://localhost:5001/PublicDisplay/FileServlet?uri=pd://document/1/CourtList:1',12);\n" +
//            "                rotationSet.add(tempDocument);\n" +
//            "                tempDocument= new DocumentReference('http://localhost:5001/PublicDisplay/FileServlet?uri=pd://document/1/CourtList:2',12);\n" +
//            "                rotationSet.add(tempDocument);\n" +
//            "                tempDocument= new DocumentReference('http://localhost:5001/PublicDisplay/FileServlet?uri=pd://document/1/CourtList:3',12);\n" +
//            "                rotationSet.add(tempDocument);\n" +
//            "                tempDocument= new DocumentReference('http://localhost:5001/PublicDisplay/FileServlet?uri=pd://document/1/DailyList:1,2,3',13);\n" +
//            "                rotationSet.add(tempDocument);";
//    private static final File FILE = new File(STORE_LOCATION + File.separator + "displays" + File.separator + "Isleworth" + File.separator + "Site1" + File.separator + "EntranceVestibule" + File.separator + "Screen1.html");
//
//    /**
//     * Creates a new TestRotationSetStorer object.
//     *
//     * @param s TODO:
//     * @throws Exception TODO:
//     */
//    public TestRotationSetStorer(String s)
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
//    public void testStore_negative()
//            throws Exception
//    {
//        DisplayRotationSet inRS = new DisplayRotationSet(new DisplayRotationSetData(URI_1, new RotationSetDisplayDocument[]
//        {
//            new RotationSetDisplayDocument(displayDocumentUri, 1)
//        }, 1, 1, "42in"));
//        Storer storer = FileStorer.getInstance();
//
//        try
//        {
//            storer.store(inRS);
//            fail("Should not be able to store an unrendered rotation set.");
//        } catch (StoreException e)
//        {
//            e.printStackTrace();
//        }
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
//        DisplayRotationSet inRS = new DisplayRotationSet(new DisplayRotationSetData(URI_1, new RotationSetDisplayDocument[]
//        {
//            new RotationSetDisplayDocument(displayDocumentUri, 1)
//        }, 1, 1, "42in"));
//        inRS.setRenderedString(RENDERED_STRING);
//
//        Storer storer = FileStorer.getInstance();
//
//        storer.store(inRS);
//
//        File file = FILE;
//        String renderedString = RENDERED_STRING;
//        TestFileStore.assertFileRenderedOkay(file, renderedString);
//    }
//
//    /**
//     * TODO:
//     *
//     * @throws Exception TODO:
//     */
//    protected void setUp()
//            throws Exception
//    {
//        super.setUp();
//
//        if (FILE.exists())
//        {
//            FILE.delete();
//        }
//    }
//
//}
//