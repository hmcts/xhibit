//package uk.gov.courtservice.xhibit.web.publicdisplay.test;
//
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
//import uk.gov.courtservice.xhibit.web.publicdisplay.test.framework.TestCaseWithInitialization;
//import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;
//
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
// * @version $Revision: 1.5 $
// */
//public class TestRenderer extends TestCaseWithInitialization
//{
//    /**
//     * Creates a new TestRenderer object.
//     *
//     * @param s TODO:
//     */
//    public TestRenderer(String s)
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
//    public void testRender()
//            throws Exception
//    {
//        int[] courts = {1, 2, 3};
//        DisplayDocument inDoc = new DisplayDocument(new DisplayDocumentURI(Locale.ENGLISH, 1, courtDetailDisplayDocumentType, courts));
//        inDoc.fetchData();
//        inDoc.render();
//        System.out.println(inDoc.getRenderedString());
//    }
//
//}
//