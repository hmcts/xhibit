package uk.gov.courtservice.xhibit.web.wf.action;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;

/**
 * <p>
 * Title: View Case Skeletons Action
 * </p>
 * <p>
 * Description: The action for viewing case skeletons.
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * @author David Duncan
 * 
 * 
 */
public class ViewCaseSkeletonsAction extends TerminalCookieAction {

    /**
     * The log4j logger
     */

    private static final Logger log = CSServices.getLogger(ViewCaseSkeletonsAction.class);

    /**
     * Empty default constructor
     */
    public ViewCaseSkeletonsAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void terminalPerformAction(ActionEnvironment actionEnvironment) {
        Integer courtIdValue = getCourtId(actionEnvironment);
        log.debug("COURT ID FROM USER::::: " + courtIdValue);
        SkeletonSchedule[] ss = SkeletonScheduleFactory.getInstance().getIssuedSkeletonSchedules(courtIdValue);
        if (ss != null && ss.length > 0) {
            actionEnvironment.setRequestParameter("skeletons", ss);
        }
        actionEnvironment.setResponseName("viewcaseskeletonscomplete");
    }
}
