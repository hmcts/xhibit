package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyListWithWitness;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: View Daily List Action
 * </p>
 * <p>
 * Description: The action for viewing daily lists.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan (2003) $Revision: 1.5 $
 */
public class ViewDailyListAction extends TerminalCookieAction {
    /**
     * Empty default constructor
     */
    public ViewDailyListAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void terminalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        try {
            if (actionEnvironment.isUserInRole("XHBCPS")) {
                actionEnvironment.setRequestParameter("CPS", "CPS");
            }

            WitnessControllerBeanBusinessDelegate delegate = WitnessControllerBeanBusinessDelegate.DelegateFactory
                    .getInstance();

            DailyListWithWitness[] list = delegate.getDailyList(delegate
                    .getCourtIdByTerminalName(getTerminalName(actionEnvironment)), new Date());
            if (list.length > 0) {
                actionEnvironment.setRequestParameter("hearings", list);
            }

        } catch (Exception e) {
            throw new FrameworkException(e);
        }
        actionEnvironment.setResponseName("viewdailylistcomplete");
    }
}
