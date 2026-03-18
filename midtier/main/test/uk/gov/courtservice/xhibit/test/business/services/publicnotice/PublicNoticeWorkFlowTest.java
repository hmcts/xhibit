package uk.gov.courtservice.xhibit.test.business.services.publicnotice;
//package uk.gov.courtservice.xhibit.business.services.publicnotice;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_configured_public_notice.XhbConfiguredPublicNotice;
//import uk.gov.courtservice.xhibit.business.entities.xhb_configured_public_notice.XhbConfiguredPublicNoticeBeanHelper2;
//import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//
///**
// * <p>Title: Test Class for testing the PublicNoticeWorkFlow Component.</p>
// * <p>Description: see title</p><p>Copyright: Copyright (c) 2003</p><p>
// * Company: Electronic Data Systems</p>
// *
// * @author Pat Fox
// * @created 17 February 2003
// */
//public class PublicNoticeWorkFlowTest extends TransactionTestCase
//{
//
//    //PublicNoticeWorkFlow m_publicNoticeWorkFlow = null;
//    Logger log = CSServices.getLogger(PublicNoticeWorkFlowTest.class);
//
//    /**
//     * Constructor for the PublicNoticeControllerWorkFlowTest object
//     *
//     * @param s
//     *            Description of the Parameter
//     */
//    public PublicNoticeWorkFlowTest(String s) throws Exception
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
//     * Retrieves the value objects that contain the displayable public notices
//     * for the specified court room.
//     *
//     * @throws Exception
//     *             Description of the Exception
//     */
//    public void testGetAllPublicNoticesForCourtRoom() throws Exception
//    {
//        log.debug("Calling Operation -> getAllPublicNoticesForCourtRoom()");
//        DisplayablePublicNoticeValue[] l_displayablePublicNoticeValues = PublicNoticeWorkFlow
//                        .getAllPublicNoticesForCourtRoom(999990);
//
//        // check the number returned
//        assertEquals(10, l_displayablePublicNoticeValues.length);
//
//        // check the value of the first Value object
//        assertEquals(120001, l_displayablePublicNoticeValues[0].getId().intValue());
//        assertEquals("Reporting restrictions. For details please see Court Manager.",
//                     l_displayablePublicNoticeValues[0].getDesc());
//        assertEquals(false, l_displayablePublicNoticeValues[0].getIsActive());
//    }
//
//    /**
//     * This sets all public Notice Activation Level for the specified court
//     * room.
//     *
//     * 1. Gets an Array of diaplayable public Notices
//     *
//     * 2. changes Activation level to true for the first Displayable Pulbic
//     * Notice
//     *
//     * 3. Sets the Dirty Flag for this Displayable Pulbic Notice.
//     *
//     * 4. Retrieves Same set of Displayable Pulbic Notice and checks the
//     * activation level
//     *
//     * @throws Exception
//     *             Description of the Exception
//     */
//    public void testSetAllPublicNoticesForCourtRoom() throws Exception
//    {
//
//        DisplayablePublicNoticeValue[] displayablePublicNoticeValues = null;
//        DisplayablePublicNoticeValue[] l_retrieveDisplayablePublicNoticeValues = null;
//
//        log.debug("Test Method testSetAllPublicNoticesForCourtRoom ");
//
//        displayablePublicNoticeValues = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(999991);
//
//        // check the number returned
//        assertEquals(10, displayablePublicNoticeValues.length);
//
//        // check the value of the first Value object
//        assertEquals(130001, displayablePublicNoticeValues[0].getId().intValue());
//        assertEquals("Reporting restrictions. For details please see Court Manager.",
//                     displayablePublicNoticeValues[0].getDesc());
//        log.debug("IsActive is set to " + displayablePublicNoticeValues[0].getIsActive());
//        assertEquals(false, displayablePublicNoticeValues[0].getIsActive());
//
//        displayablePublicNoticeValues[0].setIsActive(true);
//        log.debug("Setting is Active to : " + displayablePublicNoticeValues[0].getIsActive());
//
//        //set the dirty flag
//        log.debug("Setting dirty flag ");
//        displayablePublicNoticeValues[0].setDirty(true);
//
//        PublicNoticeWorkFlow.setAllPublicNoticesForCourtRoom(displayablePublicNoticeValues, 999991);
//
//        log.debug("=======>>>> RETRIEVING RESULTS AFTER OPERATION");
//
//        l_retrieveDisplayablePublicNoticeValues = PublicNoticeWorkFlow
//                        .getAllPublicNoticesForCourtRoom(999991);
//
//        // check the number returned
//        assertEquals(10, l_retrieveDisplayablePublicNoticeValues.length);
//
//        // check the value of the first Value object
//        assertEquals(130001, l_retrieveDisplayablePublicNoticeValues[0].getId().intValue());
//        assertEquals("Reporting restrictions. For details please see Court Manager.",
//                     l_retrieveDisplayablePublicNoticeValues[0].getDesc());
//        log.debug("IsActive is set to :" + l_retrieveDisplayablePublicNoticeValues[0].getIsActive());
//        assertEquals(true, l_retrieveDisplayablePublicNoticeValues[0].getIsActive());
//
//        assertEquals(130002, l_retrieveDisplayablePublicNoticeValues[1].getId().intValue());
//        assertEquals("In chambers, no entry.", l_retrieveDisplayablePublicNoticeValues[1].getDesc());
//        log.debug("IsActive is set to :" + l_retrieveDisplayablePublicNoticeValues[1].getIsActive());
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[1].getIsActive());
//
//    }
//
//    /**
//     * Set the Activation level of only one Configurable Publice Notice (a
//     * particular public Notice associated with a particular court room).
//     * Deciding which Configurable Publice Notice to set based on the
//     * CourtLogSubscriptionValue's eventType.
//     *
//     * 1. creates a CourtLogSubscriptionValue
//     *
//     * 2. creates a workflow ( with a dummied out notification piece)
//     *
//     * 3. invokes the operation
//     *
//     * 4. check status of Configurable Publice Notice(s)
//     *
//     * @throws Exception
//     *             Description of the Exception
//     */
//    public void testSetPublicNoticeforCourtRoom() throws Exception
//    {
//
//        log.debug("Test Method testSetPublicNoticeforCourtRoom()");
//
//        CourtLogSubscriptionValue l_courtLogSubscriptionValue = PublicNoticeTestUtil
//                        .createSubscriptionValue(999990, 21201);
//
//        //invoke operation
//        PublicNoticeWorkFlow.setPublicNoticeforCourtRoom(l_courtLogSubscriptionValue);
//
//        // check Configured Displayable Public
//        // Notices are in a correct activation State after the end of method.
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
//
//        assertEquals("0", configuredPublicNotice200.getIsActive());
//        log.debug("configuredPublicNotice200 isActive " + configuredPublicNotice200.getIsActive());
//
//        assertEquals("0", configuredPublicNotice300.getIsActive());
//        log.debug("configuredPublicNotice300 isActive " + configuredPublicNotice300.getIsActive());
//
//        assertEquals("1", configuredPublicNotice700.getIsActive());
//        log.debug("configuredPublicNotice700 isActive " + configuredPublicNotice700.getIsActive());
//
//        log.debug(" Exiting Test Method testSetPublicNoticeforCourtRoom()");
//
//    }
//
//    /**
//     * Set the Activation level of only one Configurable Publice Notice (a
//     * particular public Notice associated with a particular court room).
//     * Deciding which Configurable Publice Notice to set based on the
//     * CourtLogSubscriptionValue's eventType. But the
//     * CourtLogSubscriptionValue's event type this time should not cause any
//     * update.
//     *
//     * 1. creates a CourtLogSubscriptionValue
//     *
//     * 2. creates a workflow ( with a dummied out notification piece)
//     *
//     * 3. invokes the operation
//     *
//     * 4. check status of Configurable Publice Notice(s)
//     *
//     * @throws Exception
//     *             Description of the Exception
//     */
//    public void testSetPublicNoticeforCourtRoomNoUpdate() throws Exception
//    {
//
//        log.debug("Test Method testSetPublicNoticeforCourtRoomNoUpdate()");
//
//        //setup the courtLog value
//        CourtLogSubscriptionValue l_courtLogSubscriptionValue = PublicNoticeTestUtil
//                        .createSubscriptionValue(999990, 11111);
//
//        //invoke operation
//        PublicNoticeWorkFlow.setPublicNoticeforCourtRoom(l_courtLogSubscriptionValue);
//
//        // check Beans to make sure they are correct after the end of method.
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
//        XhbConfiguredPublicNotice configuredPublicNotice400 = (XhbConfiguredPublicNotice) XhbConfiguredPublicNoticeBeanHelper2
//                        .findByDefinitivePNCourtRoom(new Integer(999990), new Integer(400)).iterator().next();
//
//        assertEquals("0", configuredPublicNotice100.getIsActive());
//        log.debug("configuredPublicNotice100 isActive " + configuredPublicNotice100.getIsActive());
//        assertEquals("0", configuredPublicNotice200.getIsActive());
//        log.debug("configuredPublicNotice200 isActive " + configuredPublicNotice200.getIsActive());
//        assertEquals("0", configuredPublicNotice300.getIsActive());
//        log.debug("configuredPublicNotice300 isActive " + configuredPublicNotice300.getIsActive());
//        assertEquals("0", configuredPublicNotice400.getIsActive());
//        log.debug("configuredPublicNotice400 isActive " + configuredPublicNotice400.getIsActive());
//
//    }
//
//    /**
//     * This sets all public Notice Activation Level for the specified court
//     * room. But in this test case the dirty flag is not set.
//     *
//     * 1. retrieve an array of displayable value object
//     *
//     * 2. change the contents of the one of the value object to updated but not
//     * setting the Dirty flag.
//     *
//     * 3. Call the setAll method
//     *
//     * 4. retrieve the array displayable value objects and the change should not
//     * be reflected
//     */
//    public void testSetAllPublicNoticesForCourtRoomWithOutDirty() throws Exception
//    {
//
//        log.debug("Test Method testSetAllPublicNoticesForCourtRoom ");
//
//        DisplayablePublicNoticeValue[] l_displayablePublicNoticeValues = null;
//        DisplayablePublicNoticeValue[] l_retrieveDisplayablePublicNoticeValues = null;
//
//        //retrieving an array of displayablePublicNoticeValues
//
//        l_displayablePublicNoticeValues = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(999991);
//
//        // check the number returned
//        assertEquals(10, l_displayablePublicNoticeValues.length);
//
//        // check the value of the first Value object
//        assertEquals(130001, l_displayablePublicNoticeValues[0].getId().intValue());
//        assertEquals("Reporting restrictions. For details please see Court Manager.",
//                     l_displayablePublicNoticeValues[0].getDesc());
//        log.debug("IsActive is set to : " + l_displayablePublicNoticeValues[0].getIsActive());
//        assertEquals(false, l_displayablePublicNoticeValues[0].getIsActive());
//
//        l_displayablePublicNoticeValues[0].setIsActive(true);
//        log.debug("Just after setting isActive to :" + l_displayablePublicNoticeValues[0].getIsActive());
//
//        log.debug("Calling set method " + l_displayablePublicNoticeValues[0].getIsActive());
//        log.debug("Dirty Flag Not Set ");
//
//        // calling the actual operation to be tested
//
//        PublicNoticeWorkFlow.setAllPublicNoticesForCourtRoom(l_displayablePublicNoticeValues, 999991);
//
//        log.debug("=======>>>> RETRIEVING RESULTS AFTER OPERATION");
//
//        l_retrieveDisplayablePublicNoticeValues = PublicNoticeWorkFlow
//                        .getAllPublicNoticesForCourtRoom(999991);
//
//        // check the number returned
//        assertEquals(10, l_retrieveDisplayablePublicNoticeValues.length);
//
//        // check the value of the first Value object
//        assertEquals(130001, l_retrieveDisplayablePublicNoticeValues[0].getId().intValue());
//        assertEquals("Reporting restrictions. For details please see Court Manager.",
//                     l_retrieveDisplayablePublicNoticeValues[0].getDesc());
//        log.debug("IsActive is set to " + l_retrieveDisplayablePublicNoticeValues[0].getIsActive());
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[0].getIsActive());
//
//        assertEquals(130002, l_retrieveDisplayablePublicNoticeValues[1].getId().intValue());
//        assertEquals("In chambers, no entry.", l_retrieveDisplayablePublicNoticeValues[1].getDesc());
//        log.debug("IsActive is set to " + l_retrieveDisplayablePublicNoticeValues[1].getIsActive());
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[1].getIsActive());
//    }
//
//}