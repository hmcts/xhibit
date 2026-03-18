///*
// * Created on Apr 30, 2004
// *
// * To change the template for this generated file go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//package uk.gov.courtservice.xhibit.courtlog.reportingrestrictions;
//
//import java.util.Collection;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case_reference.XhbCaseReference;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case_reference.XhbCaseReferenceBasicValue;
//import uk.gov.courtservice.xhibit.courtlog.CourtLogTestCase;
//
//
///**
// * @author pznwc5
// *
// * To change the template for this generated type comment go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestReportingRestrictionsHelper extends CourtLogTestCase
//{
//
//    private XhbCase caze;
//    /*
//     * @see TestCase#setUp()
//     */
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//        caze = XhbCaseBeanHelper2.findByPrimaryKey(getCaseId());
//        caze.getXhbCaseReferences().clear();
//    }
//
//    /*
//     * @see TestCase#tearDown()
//     */
//    protected void tearDown() throws Exception
//    {
//
//        super.tearDown();
//    }
//
//    /**
//     * Constructor for TestReportingRestrictionsHelper.
//     * @param arg0
//     */
//    public TestReportingRestrictionsHelper(String arg0) throws Exception
//    {
//        super(arg0);
//    }
//
//    public void testSetRestrictions()
//    {
//        ReportingRestrictionsHelper.setRestrictions(caze.getCaseId());
//        Collection caseRefs = caze.getXhbCaseReferences();
//        assertEquals(caseRefs.size(), 1);
//        XhbCaseReference caseRef = (XhbCaseReference)caseRefs.iterator().next();
//        assertEquals(caseRef.getReportingRestrictions(),
//                XhbCaseReferenceBasicValue.REPORTING_RESTRICTIONS);
//
//    }
//
//    public void testLiftRestrictions()
//    {
//        ReportingRestrictionsHelper.liftRestrictions(caze.getCaseId());
//        Collection caseRefs = caze.getXhbCaseReferences();
//        assertEquals(caseRefs.size(), 1);
//        XhbCaseReference caseRef = (XhbCaseReference)caseRefs.iterator().next();
//        assertEquals(caseRef.getReportingRestrictions(),
//                XhbCaseReferenceBasicValue.LIFTING_RESTRICTIONS);
//    }
//}
//