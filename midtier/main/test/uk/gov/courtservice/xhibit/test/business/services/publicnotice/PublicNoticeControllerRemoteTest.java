package uk.gov.courtservice.xhibit.test.business.services.publicnotice;
//package uk.gov.courtservice.xhibit.business.services.publicnotice;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
//
///**
// * <p>Title: Test for the PublicNoticeControllerRemote class</p><p>
// * Description:see title</p><p>Copyright: Copyright (c) 2003</p><p>
// * Company: Electronic Data Systems</p>
// *
// * @author tzxbys
// * @created 05 March 2003
// */
//public class PublicNoticeControllerRemoteTest extends TransactionTestCase
//{
//    PublicNoticeController m_delegate = null;
//
//    Logger log = CSServices.getLogger(PublicNoticeControllerRemoteTest.class);
//
//    /**
//     * Constructor for the PublicNoticeControllerRemoteTest object
//     *
//     * @param s
//     *            Description of the Parameter
//     */
//    public PublicNoticeControllerRemoteTest(String s) throws Exception
//    {
//        super(s, true);
//
//        m_delegate = (PublicNoticeController) CSServices.getEJBServices()
//                        .createRemoteSession(PublicNoticeControllerHome.class);
//    }
//
//    /**
//     * The JUnit setup method
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
//     */
//    protected void tearDown() throws Exception
//    {
//        super.tearDown();
//    }
//
//    /**
//     * testGetAllPublicNoticesForCourtRoom
//     *
//     * Retrieves all the displayable public notices for the spiecfic court room
//     * id -- 999990 expects to receive ten notices back.
//     *
//     * @throws Exception -
//     */
//    public void testGetAllPublicNoticesForCourtRoom() throws Exception
//    {
//
//        log.debug("Test Method testGetAllPublicNoticesForCourtRoom() ");
//
//        log.debug("Calling Operation");
//
//        DisplayablePublicNoticeValue[] displayablePublicNoticeValues = m_delegate
//                        .getAllPublicNoticesForCourtRoom(999990);
//
//        // check the number returned
//        assertEquals(10, displayablePublicNoticeValues.length);
//
//        log.debug("The Notice is" + displayablePublicNoticeValues[0].getDesc());
//
//        // check the value of the first Value object
//        assertEquals(120001, displayablePublicNoticeValues[0].getId().intValue());
//        assertEquals("Reporting restrictions. For details please see Court Manager.",
//                     displayablePublicNoticeValues[0].getDesc());
//        assertEquals(false, displayablePublicNoticeValues[0].getIsActive());
//
//    }
//
//    /**
//     * testSetAllPublicNoticesForCourtRoom
//     *
//     * Passes back an Array of displayable public notices and persists them
//     * correctly.
//     *
//     * 1. retrieves the array of public notices
//     *
//     * 2. alters the isActive state of the first one.
//     *
//     * 3. calls SetAllPublicNoticesForCourtRoom()
//     *
//     * 4. check that it persists it correctly.
//     *
//     * @throws Exception -
//     */
//    public void testSetAllPublicNoticesForCourtRoom() throws Exception
//    {
//
//        log.debug("Test Method testSetAllPublicNoticesForCourtRoom ");
//
//        DisplayablePublicNoticeValue[] displayablePublicNoticeValues = null;
//        DisplayablePublicNoticeValue[] l_retrieveDisplayablePublicNoticeValues = null;
//
//        log.debug("Calling Operation");
//
//        displayablePublicNoticeValues = m_delegate.getAllPublicNoticesForCourtRoom(999990);
//
//        // check the number returned
//        assertEquals(10, displayablePublicNoticeValues.length);
//
//        // check the value of the first Value object
//        assertEquals(120001, displayablePublicNoticeValues[0].getId().intValue());
//        assertEquals("Reporting restrictions. For details please see Court Manager.",
//                     displayablePublicNoticeValues[0].getDesc());
//        assertEquals(false, displayablePublicNoticeValues[0].getIsActive());
//
//        displayablePublicNoticeValues[0].setIsActive(true);
//
//        //set the dirty flag
//        log.debug("Setting dirty flag ");
//        displayablePublicNoticeValues[0].setDirty(true);
//
//        log.debug("after change isActive is set to " + displayablePublicNoticeValues[0].getIsActive());
//
//        log.debug("Calling set method " + displayablePublicNoticeValues[0].getIsActive());
//        m_delegate.setAllPublicNoticesForCourtRoom(displayablePublicNoticeValues, 999990);
//
//        log.debug("=======>>>> RETRIEVING RESULTS AFTER OPERATION");
//
//        l_retrieveDisplayablePublicNoticeValues = m_delegate.getAllPublicNoticesForCourtRoom(999990);
//
//        // check the number returned
//        assertEquals(10, displayablePublicNoticeValues.length);
//
//        // check the value of the first Value object
//        assertEquals(120001, displayablePublicNoticeValues[0].getId().intValue());
//        assertEquals("Reporting restrictions. For details please see Court Manager.",
//                     displayablePublicNoticeValues[0].getDesc());
//        assertEquals(true, l_retrieveDisplayablePublicNoticeValues[0].getIsActive());
//
//        //check the other remain 9 displayable public notice activation level
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[1].getIsActive());
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[2].getIsActive());
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[3].getIsActive());
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[4].getIsActive());
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[5].getIsActive());
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[6].getIsActive());
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[7].getIsActive());
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[8].getIsActive());
//        assertEquals(false, l_retrieveDisplayablePublicNoticeValues[9].getIsActive());
//
//    }
//
//    /**
//     * testSetAllPublicNoticesForCourtRoomVailadtionException
//     *
//     * Passes back an Array of displayable public notices but should not persist
//     * them as I have set 8 of the public notices and the max number set to
//     * activate at any one time is currently 5
//     *
//     * 1. retrieves the array of public notices
//     *
//     * 2. alters the isActive state on eight of the displayable public notices.
//     *
//     * 3. calls SetAllPublicNoticesForCourtRoom().
//     *
//     * 4. throws a public notice Invalid Selection Exception.
//     *
//     * @throws Exception -
//     */
//    public void testSetAllPublicNoticesForCourtRoomVailadtionException() throws Exception
//    {
//
//        DisplayablePublicNoticeValue[] displayablePublicNoticeValues = null;
//        DisplayablePublicNoticeValue[] l_retrieveDisplayablePublicNoticeValues = null;
//
//        log.debug("Test Method testSetAllPublicNoticesForCourtRoomException ");
//
//        log.debug("Calling Operation");
//
//        displayablePublicNoticeValues = m_delegate.getAllPublicNoticesForCourtRoom(999990);
//
//        // check the number returned
//        assertEquals(10, displayablePublicNoticeValues.length);
//
//        // check the value of the first Value object
//        assertEquals(120001, displayablePublicNoticeValues[0].getId().intValue());
//        assertEquals("Reporting restrictions. For details please see Court Manager.",
//                     displayablePublicNoticeValues[0].getDesc());
//        assertEquals(false, displayablePublicNoticeValues[0].getIsActive());
//
//        // change more than MAX_Number
//        displayablePublicNoticeValues[0].setIsActive(true);
//        displayablePublicNoticeValues[1].setIsActive(true);
//        displayablePublicNoticeValues[2].setIsActive(true);
//        displayablePublicNoticeValues[3].setIsActive(true);
//        displayablePublicNoticeValues[4].setIsActive(true);
//        displayablePublicNoticeValues[5].setIsActive(true);
//        displayablePublicNoticeValues[6].setIsActive(true);
//        displayablePublicNoticeValues[7].setIsActive(true);
//
//        log.debug("Changed to isActive  is set on 8 displayable public Notice");
//
//        try {
//            m_delegate.setAllPublicNoticesForCourtRoom(displayablePublicNoticeValues, 999990);
//            fail("Should have thrown an Exception");
//        } catch (PublicNoticeInvalidSelectionException ex) {
//            log.debug("GOT EXPECTED EXCEPTION");
//        }
//
//    }
//
//}