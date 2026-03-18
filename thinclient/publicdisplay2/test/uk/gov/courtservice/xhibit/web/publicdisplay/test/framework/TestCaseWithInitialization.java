//package uk.gov.courtservice.xhibit.web.publicdisplay.test.framework;
//
//import uk.gov.courtservice.xhibit.common.publicdisplay.data.DataSource;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
//
//import javax.naming.NamingException;
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
//public abstract class TestCaseWithInitialization extends PublicDisplayTestCase
//{
//    protected DataSource courtDetailDataSource;
//    protected DisplayDocumentType courtDetailDisplayDocumentType;
//    protected DisplayDocumentType courtListDisplayDocumentType;
//    protected DisplayDocumentType summaryByNameDisplayDocumentType;
//    protected DisplayDocumentURI displayDocumentUri;
//
//    /**
//     * Creates a new TestCaseWithInitialization object.
//     *
//     * @param s TODO:
//     * @throws NamingException TODO:
//     */
//    public TestCaseWithInitialization(String s)
//            throws NamingException
//    {
//        super(s);
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
//        //System.setProperty("publicdisplay.web.store_base", STORE_LOCATION);
//        //System.setProperty("publicdisplay.web.base_url", STORE_URL);
//        courtDetailDisplayDocumentType = DisplayDocumentType.getDisplayDocumentType("CourtDetail");
//        summaryByNameDisplayDocumentType = DisplayDocumentType.getDisplayDocumentType("SummaryByName");
//        courtListDisplayDocumentType = DisplayDocumentType.getDisplayDocumentType("CourtList");
//        displayDocumentUri = new DisplayDocumentURI("pd://document:en/1/CourtDetail:1,2,3,100");
//
//        System.setProperty("use.date", "" + START_DATE.getTime());
//    }
//
//}
//