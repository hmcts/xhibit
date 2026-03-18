package uk.gov.courtservice.xhibit.test.business.services.publicnotice;
//package uk.gov.courtservice.xhibit.business.services.publicnotice;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_configured_public_notice.XhbConfiguredPublicNotice;
//import uk.gov.courtservice.xhibit.business.entities.xhb_configured_public_notice.XhbConfiguredPublicNoticeBeanHelper2;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//
///**
// * <p>Title: Test for the PublicNoticeSelectionManipulator class</p><p>
// * Description:see title</p><p>Copyright: Copyright (c) 2003</p><p>
// * Company: Electronic Data Systems</p>
// *
// * @author Pat Fox
// * @created 17 February 2003
// */
//public class PublicNoticeSelectionManipulatorTest extends TransactionTestCase
//{
//    Logger log = CSServices.getLogger(PublicNoticeSelectionManipulatorTest.class);
//
//    /**
//     * Constructor for the PublicNoticeControllerWorkFlowTest object
//     *
//     * @param s
//     *            Description of the Parameter
//     */
//    public PublicNoticeSelectionManipulatorTest(String s) throws Exception
//    {
//        super(s, true);
//    }
//
//    /**
//     * The JUnit setup method
//     *
//     * @exception Exception
//     *                Description of the Exception
//     */
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//        PublicNoticeTestDataLoader.clean(connection);
//        PublicNoticeTestDataLoader.load(connection);
//    }
//
//    /**
//     * The teardown method for JUnit
//     *
//     * @exception Exception
//     *                Description of the Exception
//     */
//    protected void tearDown() throws Exception
//    {
//        super.tearDown();
//    }
//
//    /**
//     * Create a CourtlogSubscription Object that requires the public Manipulator
//     * to change several configurable public notice activation Level ( setting
//     * is the IsActive Flag)
//     *
//     * 1. Create a CourtlogSubscription.
//     *
//     * 2. invoke operation.
//     *
//     * 3. check results.
//     *
//     * @exception Exception
//     *                Description of the Exception
//     */
//    public void testManipulateSelection20903TvLink() throws Exception
//    {
//
//        log.debug("Test Method testManipulateSelection20903TvLink() ");
//
//        // Create CourtLogSubscriptionValue and add the events xml entry - TV
//        // link
//        CourtLogSubscriptionValue l_courtLogSubscriptionValue = PublicNoticeTestUtil
//                        .createSubscriptionValue(999990, 20903);
//
//        String logEntryXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
//                        .append(
//                                "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='20903.xsd'>")
//                        .append("<E20903_Prosecution_Case_Options>").append("<E20903_PCO_Type>")
//                        .append(PublicNoticeSelectionManipulator.XPATH_EVENT_ID_20903_OPTION_TV_LINK)
//                        .append("</E20903_PCO_Type>")
//                        .append("</E20903_Prosecution_Case_Options>").append("<free_text>Test 2</free_text>")
//                        .append("<type>20903</type>").append("</event>").toString();
//
//        l_courtLogSubscriptionValue.getCourtLogViewValue().setLogEntry(logEntryXml);
//
//        //invoke operation to update public notice
//
//        PublicNoticeSelectionManipulator.manipulateSelection(l_courtLogSubscriptionValue);
//
//        //Retrieve contents of the specified configured Public notice
//        // and ensure correct message states
//
//        XhbConfiguredPublicNotice configuredPublicNotice400 = (XhbConfiguredPublicNotice) XhbConfiguredPublicNoticeBeanHelper2
//                        .findByDefinitivePNCourtRoom(new Integer(999990), new Integer(400)).iterator().next();
//
//        assertEquals("TV link in progress message should have been active", "1", configuredPublicNotice400
//                        .getIsActive());
//        log.debug("configuredPublicNotice400 isActive " + configuredPublicNotice400.getIsActive());
//
//        XhbConfiguredPublicNotice configuredPublicNotice500 = (XhbConfiguredPublicNotice) XhbConfiguredPublicNoticeBeanHelper2
//                        .findByDefinitivePNCourtRoom(new Integer(999990), new Integer(500)).iterator().next();
//
//        assertEquals("Video link in progress message shouldn't have been active", "0",
//                     configuredPublicNotice500.getIsActive());
//        log.debug("configuredPublicNotice500 isActive " + configuredPublicNotice500.getIsActive());
//
//        log.debug(" Exiting Test Method testManipulateSelection() ");
//
//    }
//
//    /**
//     * Create a CourtlogSubscription Object that requires the public Manipulator
//     * to change several configurable public notice activation Level ( setting
//     * is the IsActive Flag)
//     *
//     * 1. Create a CourtlogSubscription.
//     *
//     * 2. invoke operation.
//     *
//     * 3. check results.
//     *
//     * @exception Exception
//     *                Description of the Exception
//     */
//    public void testManipulateSelection20903VideoLink() throws Exception
//    {
//
//        log.debug("Test Method testManipulateSelection20903VideoLink() ");
//
//        // Create CourtLogSubscriptionValue and add the events xml entry - TV
//        // link
//        CourtLogSubscriptionValue l_courtLogSubscriptionValue = PublicNoticeTestUtil
//                        .createSubscriptionValue(999990, 20903);
//
//        String logEntryXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
//                        .append(
//                                "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='20903.xsd'>")
//                        .append("<E20903_Prosecution_Case_Options>").append("<E20903_PCO_Type>")
//                        .append(PublicNoticeSelectionManipulator.XPATH_EVENT_ID_20903_OPTION_VIDEO_LINK)
//                        .append("</E20903_PCO_Type>").append("</E20903_Prosecution_Case_Options>")
//                        .append("<free_text>Test 2</free_text>").append("<type>20903</type>")
//                        .append("</event>").toString();
//
//        l_courtLogSubscriptionValue.getCourtLogViewValue().setLogEntry(logEntryXml);
//
//        //invoke operation to update public notice
//
//        PublicNoticeSelectionManipulator.manipulateSelection(l_courtLogSubscriptionValue);
//
//        //Retrieve contents of the specified configured Public notice
//        // and ensure correct message states
//
//        XhbConfiguredPublicNotice configuredPublicNotice400 = (XhbConfiguredPublicNotice) XhbConfiguredPublicNoticeBeanHelper2
//                        .findByDefinitivePNCourtRoom(new Integer(999990), new Integer(400)).iterator().next();
//
//        assertEquals("TV link in progress message shouldn't have been active", "0", configuredPublicNotice400
//                        .getIsActive());
//        log.debug("configuredPublicNotice400 isActive " + configuredPublicNotice400.getIsActive());
//
//        XhbConfiguredPublicNotice configuredPublicNotice500 = (XhbConfiguredPublicNotice) XhbConfiguredPublicNoticeBeanHelper2
//                        .findByDefinitivePNCourtRoom(new Integer(999990), new Integer(500)).iterator().next();
//
//        assertEquals("Video link in progress message should have been active", "1", configuredPublicNotice500
//                        .getIsActive());
//        log.debug("configuredPublicNotice500 isActive " + configuredPublicNotice500.getIsActive());
//
//        log.debug(" Exiting Test Method testManipulateSelection() ");
//
//    }
//
//    /**
//     * Create a CourtlogSubscription Object that requires the public Manipulator
//     * to change several configurable public notice activation Level ( setting
//     * is the IsActive Flag)
//     *
//     * 1. Create a CourtlogSubscription.
//     *
//     * 2. invoke operation.
//     *
//     * 3. check results.
//     *
//     * @exception Exception
//     *                Description of the Exception
//     */
//    public void testManipulateSelectionEventId() throws Exception
//    {
//        log.debug("Test Method testManipulateSelection() ");
//
//        CourtLogSubscriptionValue l_courtLogSubscriptionValue = PublicNoticeTestUtil
//                        .createSubscriptionValue(999990, 21201);
//
//        //invoke operation to test
//
//        PublicNoticeSelectionManipulator.manipulateSelection(l_courtLogSubscriptionValue);
//
//        //Retrieve contents of the specified configured Public notice
//        // to ensure correct action
//
//        XhbConfiguredPublicNotice configuredPublicNotice100 = (XhbConfiguredPublicNotice) XhbConfiguredPublicNoticeBeanHelper2
//                        .findByDefinitivePNCourtRoom(new Integer(999990), new Integer(100)).iterator().next();
//
//        XhbConfiguredPublicNotice configuredPublicNotice200 = (XhbConfiguredPublicNotice) XhbConfiguredPublicNoticeBeanHelper2
//                        .findByDefinitivePNCourtRoom(new Integer(999990), new Integer(200)).iterator().next();
//
//        XhbConfiguredPublicNotice configuredPublicNotice300 = (XhbConfiguredPublicNotice) XhbConfiguredPublicNoticeBeanHelper2
//                        .findByDefinitivePNCourtRoom(new Integer(999990), new Integer(300)).iterator().next();
//
//        XhbConfiguredPublicNotice configuredPublicNotice700 = (XhbConfiguredPublicNotice) XhbConfiguredPublicNoticeBeanHelper2
//                        .findByDefinitivePNCourtRoom(new Integer(999990), new Integer(700)).iterator().next();
//
//        assertEquals("0", configuredPublicNotice100.getIsActive());
//        log.debug("configuredPublicNotice100 isActive " + configuredPublicNotice100.getIsActive());
//        assertEquals("0", configuredPublicNotice200.getIsActive());
//        log.debug("configuredPublicNotice200 isActive " + configuredPublicNotice200.getIsActive());
//        assertEquals("0", configuredPublicNotice300.getIsActive());
//        log.debug("configuredPublicNotice300 isActive " + configuredPublicNotice300.getIsActive());
//        assertEquals("1", configuredPublicNotice700.getIsActive());
//        log.debug("configuredPublicNotice700 isActive " + configuredPublicNotice700.getIsActive());
//
//        log.debug(" Exiting Test Method testManipulateSelection() ");
//
//    }
//
//    /**
//     * Create a CourtlogSubscription Object that requires the public Manipulator
//     * but enter an invalid court Room
//     *
//     * 1. Create a CourtlogSubscription.
//     *
//     * 2. invoke operation.
//     *
//     * 3 should get and PublicNoticeException
//     *
//     * @exception Exception
//     *                Description of the Exception
//     */
//
//    public void testManipulateSelectionException() throws Exception
//    {
//        log.debug("Test Method testManipulateSelectionException() ");
//
//        // Create a CourtLogSubscriptionValue with
//        // Invalid courtroom but an event type that requires a
//        // status change.
//        CourtLogSubscriptionValue l_courtLogSubscriptionValue = PublicNoticeTestUtil
//                        .createSubscriptionValue(11111, 21201);
//
//        try {
//            PublicNoticeSelectionManipulator.manipulateSelection(l_courtLogSubscriptionValue);
//            //should have thrown an Exception
//            fail();
//        } catch (PublicNoticeException ex) {
//            log.debug(ex);
//        }
//
//        log.debug(" Exiting Test Method testManipulateSelectionException() ");
//
//    }
//
//}