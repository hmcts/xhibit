package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine.rules;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.CaseCourtRoomEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;

/**
 * <p>
 * Title: Case Being Heard Rule
 * </p>
 * <p>
 * Description: Checks whether the case associated with event being processed
 * has its display turned on
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class CaseBeingHeardRule implements Rule {
    private static final Logger log = Logger.getLogger(CaseBeingHeardRule.class);

    public CaseBeingHeardRule() {
    }

    /**
     * Rule is true if the case associated with the event being processed has
     * its display turned on
     * 
     * @param event
     *            An event that sub classes
     * @return
     */
    public boolean isValid(PublicDisplayEvent event) {
        if (event instanceof CaseCourtRoomEvent) {
            return ((CaseCourtRoomEvent) event).isCaseActive();
        } else {
            log.error("Rule called with wrong error type:" + event.getClass().toString());
            return false;
        }
    }
}