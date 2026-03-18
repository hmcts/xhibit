package uk.gov.courtservice.xhibit.common.publicdisplay.events.test;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.ActivateCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.AddCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.CaseStatusEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.HearingStatusEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.MoveCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicNoticeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.UpdateCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseCourtLogInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.EventType;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.8 $
 */
public class TestEventCreation extends TestCase {
    private static final CourtRoomIdentifier courtRoomId =
    		new CourtRoomIdentifier(new Integer(1), new Integer(5), "Test Court Name", new Integer(1), new DisplayablePublicNoticeValue[0]);

    private static final CourtRoomIdentifier courtRoomId2 =
    		new CourtRoomIdentifier(new Integer(2), new Integer(8), "Test Court Name", new Integer(1), new DisplayablePublicNoticeValue[0]);

    private static final CaseChangeInformation caseChangeInformation = new CaseChangeInformation(true);

    /**
     * Creates a new TestEventCreation object.
     * 
     * @param s
     *            TODO:
     */
    public TestEventCreation(String s) {
        super(s);
    }

    public void testActivateCaseEvent() {
        ActivateCaseEvent a = new ActivateCaseEvent(courtRoomId, caseChangeInformation);
        assertNotNull(a);
        assertEquals("Wrong event type", EventType.getEventType(EventType.ACTIVATE_CASE_EVENT), a.getEventType());
    }

    public void testAddCaseEvent() {
        AddCaseEvent a = new AddCaseEvent(courtRoomId, caseChangeInformation);
        assertNotNull(a);
        assertEquals("Wrong event type", EventType.getEventType(EventType.ADD_CASE_EVENT), a.getEventType());
    }

    public void testCaseStatusEvent() {
        CourtLogViewValue clvv = new CourtLogViewValue();
        clvv.setCaseId(new Integer(1));

        CourtLogSubscriptionValue clSub = new CourtLogSubscriptionValue(clvv);
        clSub.setCourtRoomId(courtRoomId.getCourtRoomId());

        CaseCourtLogInformation ccli = new CaseCourtLogInformation(clSub, true);

        CaseStatusEvent a = new CaseStatusEvent(courtRoomId, ccli);
        assertNotNull(a);
        assertEquals("Wrong event type", EventType.getEventType(EventType.CASE_STATUS_EVENT), a.getEventType());
    }

    public void testConfigurationChangeEvent() {
        ConfigurationChangeEvent a = new ConfigurationChangeEvent(new CourtConfigurationChange(1, "Test"));
        assertNotNull(a);
        assertEquals("Wrong event type", EventType.getEventType(EventType.CONFIGURATION_EVENT), a.getEventType());
    }

    public void testHearingStatusEvent() {
        HearingStatusEvent a = new HearingStatusEvent(courtRoomId, caseChangeInformation);
        assertNotNull(a);
        assertEquals("Wrong event type", EventType.getEventType(EventType.HEARING_STATUS_EVENT), a.getEventType());
    }

    public void testMoveCaseEvent() {

        try {
            // This also test the super classes of CourtRoomIdentifier and
            // CaseChangeInfo
            MoveCaseEvent a = new MoveCaseEvent(courtRoomId, courtRoomId2, caseChangeInformation);
            assertNotNull(a);
            assertEquals("Wrong event type", EventType.getEventType(EventType.MOVE_CASE_EVENT), a.getEventType());
            assertEquals("Wrong From Court", new Integer(1), a.getFromCourtRoomIdentifier().getCourtId());
            assertEquals("Wrong From Court room", new Integer(5), a.getFromCourtRoomIdentifier().getCourtRoomId());
            assertEquals("Wrong To Court", new Integer(2), a.getToCourtRoomIdentifier().getCourtId());
            assertEquals("Wrong To Court room", new Integer(8), a.getToCourtRoomIdentifier().getCourtRoomId());
            assertEquals("Case Active incorrect", true, a.getCaseChangeInformation().isCaseActive());
        } catch (Error rte) {
            rte.printStackTrace(System.out);
            throw rte;
        }

    }

    public void testPublicNoticeEvent() {
        PublicNoticeEvent a = new PublicNoticeEvent(courtRoomId);
        assertNotNull(a);
        assertEquals("Wrong event type", EventType.getEventType(EventType.PUBLIC_NOTICE_EVENT), a.getEventType());
    }

    public void testUpdateCaseEvent() {
        UpdateCaseEvent a = new UpdateCaseEvent(courtRoomId, caseChangeInformation);
        assertNotNull(a);
        assertEquals("Wrong event type", EventType.getEventType(EventType.UPDATE_CASE_EVENT), a.getEventType());
    }
}
