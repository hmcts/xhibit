package uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing;

// jdk
import java.util.Collection;

import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.LinkSuggestionValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class LinkHearingWorkflow {
    private LinkHearingHelper linkHearingHelper = new LinkHearingHelper();

    private LinkHearingsHelper linkHearingsHelper = new LinkHearingsHelper();

    public LinkHearingWorkflow() {
    }

    public boolean previouslyLinked(Integer CaseId) throws HearingScheduleException {
        // throw new UnsupportedOperationException();
        return false;
    }

    public LinkSuggestionValue suggestLinkCases(Integer scheduledHearingId) throws HearingScheduleException {
        return linkHearingHelper.suggestLinkCases(scheduledHearingId);
    }

    public void linkCases(CaseSchedHearingValue[] caseSchedHearingValues, Integer leadScheduledHearingId, String userDisplayName)
            throws HearingScheduleException {
        linkHearingHelper.linkCases(caseSchedHearingValues, leadScheduledHearingId, userDisplayName);
    }

    public void unLinkCase(ScheduledHearingBasicValue shBasicValue, String userDisplayName) throws HearingScheduleException {
        linkHearingHelper.unLinkCase(shBasicValue, userDisplayName);
    }

    public CaseSchedHearingValue[] getLinkedSchedHearingsByLinkId(Integer linkedShId) throws HearingScheduleException {
        return linkHearingHelper.getLinkedSchedHearingsByLinkId(linkedShId);
    }

    public CaseSchedHearingValue[] getLinkedSchedHearingsByShId(Integer schedHearingId) throws HearingScheduleException {
        return linkHearingHelper.getLinkedSchedHearingsByShId(schedHearingId);
    }

    public void linkHearings(Integer leadHearingID, Collection hearingIDs, String userDisplayName) throws HearingScheduleException {
        linkHearingsHelper.linkHearings(leadHearingID, hearingIDs, userDisplayName);
    }

    public CaseHearingValue listCaseWithHearings(String caseTypeAndNumber, Integer courtID, Integer leadHearingID)
            throws HearingScheduleException {
        return linkHearingsHelper.listCaseWithHearings(caseTypeAndNumber, courtID, leadHearingID);
    }

    public CaseHearingValue listCaseWithHearings(String caseTypeAndNumber, Integer courtID)
            throws HearingScheduleException

    {
        return linkHearingsHelper.listCaseWithHearings(caseTypeAndNumber, courtID);
    }

    public void unlinkHearing(Integer hearingID, String userDisplayName) throws HearingScheduleException {
        linkHearingsHelper.unlinkHearing(hearingID, userDisplayName);
    }
}