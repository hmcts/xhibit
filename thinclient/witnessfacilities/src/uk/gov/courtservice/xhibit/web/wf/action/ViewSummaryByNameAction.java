package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyList;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: View Summary By Name Action
 * </p>
 * <p>
 * Description: The action for viewing summaries by name.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.5 $
 */
public class ViewSummaryByNameAction extends TerminalCookieAction {
    /**
     * Empty default constructor
     */
    public ViewSummaryByNameAction() {
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
            WitnessControllerBeanBusinessDelegate delegate = WitnessControllerBeanBusinessDelegate.DelegateFactory
                    .getInstance();

            DailyList[] vos = delegate.getDailyListByDefendant(delegate
                    .getCourtIdByTerminalName(getTerminalName(actionEnvironment)), new Date());
            if (vos.length > 0) {
                actionEnvironment.setRequestParameter("defendants", vos);
            }
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
        actionEnvironment.setResponseName("viewsummarybynamecomplete");
    }
}
