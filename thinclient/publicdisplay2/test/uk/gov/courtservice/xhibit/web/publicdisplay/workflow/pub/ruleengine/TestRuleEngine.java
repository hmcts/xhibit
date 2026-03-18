//package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine;
//
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.AddCaseEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.CaseStatusEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicNoticeEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.UpdateCaseEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseCourtLogInformation;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
//import uk.gov.courtservice.xhibit.web.publicdisplay.test.framework.TestCaseWithInitialization;
//
//
///**
// * <p>
// * Title:
// * </p>
// *
// * <p>
// * Description:
// * </p>
// *
// * <p>
// * Copyright: Copyright (c) 2002
// * </p>
// *
// * <p>
// * Company: EDS
// * </p>
// *
// * @author Rakesh Lakhani
// * @version 1.0
// */
//public class TestRuleEngine extends TestCaseWithInitialization
//{
//    private CaseChangeInformation ccInfo = new CaseChangeInformation(true);
//    private CourtRoomIdentifier crId = new CourtRoomIdentifier(new Integer(1), new Integer(2));
//
//    /**
// * Creates a new TestRuleEngine object.
// *
// * @param s TODO:
// *
// * @throws Exception TODO:
// */
//    public TestRuleEngine(String s)
//        throws Exception
//    {
//        super(s);
//    }
//
//    /**
// * TODO:
// */
//    public void testDisplayDocumentTypesForEvent()
//    {
//        // Event with just a court room
//        RulesEngine rules = RulesEngine.getInstance();
//        PublicNoticeEvent pnEvent = new PublicNoticeEvent(crId);
//        DocumentsForEvent docs = rules.getDisplayDocumentTypesForEvent(pnEvent);
//        assertEquals("Public Notice: Incorrect number of docs returned", 1, docs.getDisplayDocumentTypes().length);
//
//        // Public notice event with reporting restrictions changed
//        pnEvent = new PublicNoticeEvent(crId, true);
//        docs = rules.getDisplayDocumentTypesForEvent(pnEvent);
//        assertEquals("Public Notice: Incorrect number of docs returned", 7, docs.getDisplayDocumentTypes().length);
//
//        // Event with court and case - no rules
//        AddCaseEvent acEvent = new AddCaseEvent(crId, ccInfo);
//        acEvent.setCaseActive(true);
//        docs = rules.getDisplayDocumentTypesForEvent(acEvent);
//        assertEquals("Add Case: Incorrect number of docs returned", 5, docs.getDisplayDocumentTypes().length);
//
//        // an event that requires case active and it is active
//        UpdateCaseEvent ucEvent = new UpdateCaseEvent(crId, ccInfo);
//        ucEvent.setCaseActive(true);
//        docs = rules.getDisplayDocumentTypesForEvent(ucEvent);
//        assertEquals("Add Case: Incorrect number of docs returned", 7, docs.getDisplayDocumentTypes().length);
//
//        // an event that requires case active and it is NOT active
//        ucEvent.setCaseActive(false);
//        docs = rules.getDisplayDocumentTypesForEvent(ucEvent);
//        assertEquals("Add Case: Incorrect number of docs returned", 6, docs.getDisplayDocumentTypes().length);
//
//        // court log subs value
//        CourtLogSubscriptionValue subs = new CourtLogSubscriptionValue();
//        CourtLogViewValue clvv = new CourtLogViewValue();
//        clvv.setCaseId(new Integer(3));
//        clvv.setEventType(new Integer("10100"));
//        subs.setCourtLogViewValue(clvv);
//
//        CaseCourtLogInformation ccLogInfo = new CaseCourtLogInformation(subs, true);
//
//        CaseStatusEvent csEvent = new CaseStatusEvent(crId, ccLogInfo);
//
//        // an event for court log, regular event, active
//        csEvent.setCaseActive(true);
//        docs = rules.getDisplayDocumentTypesForEvent(csEvent);
//        assertEquals("Case Status - called on;Case active: Incorrect number of docs returned", 3, docs.getDisplayDocumentTypes().length);
//
//        // an event for court log, regular event, inactive
//        csEvent.setCaseActive(false);
//        docs = rules.getDisplayDocumentTypesForEvent(csEvent);
////        System.out.println("docreturned=" + docs.getDisplayDocumentTypes()[0].getLongName());
//        assertEquals("Case Status - called on;Case inactive: Incorrect number of docs returned", 1, docs.getDisplayDocumentTypes().length);
//
//        // an event for court log, defendant change event, active
//        csEvent.setCaseActive(true);
//        clvv.setEventType(new Integer("40300"));
//        docs = rules.getDisplayDocumentTypesForEvent(csEvent);
//        assertEquals("Case Status - def amend;Case active", 3, docs.getDisplayDocumentTypes().length);
//
//        // an event for court log, defendant change event, active
//        csEvent.setCaseActive(false);
//        docs = rules.getDisplayDocumentTypesForEvent(csEvent);
//        assertEquals("Case Status - def amend;Case inactive: Incorrect number of docs returned", 1, docs.getDisplayDocumentTypes().length);
//    }
//
//}
//