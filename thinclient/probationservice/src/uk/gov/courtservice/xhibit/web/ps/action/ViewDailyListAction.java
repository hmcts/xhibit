package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyListWithJudge;
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
 * @author David Duncan (2003) $Revision: 1.9 $ $Log: ViewDailyListAction.java,v $
 * @author David Duncan (2003) $Revision: 1.9 $ Revision 1.9  2006/06/05 12:32:26  bzjrnl
 * @author David Duncan (2003) $Revision: 1.9 $ Change: TI901
 * @author David Duncan (2003) $Revision: 1.9 $ Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * @author David Duncan (2003) $Revision: 1.9 $
 * @author David Duncan (2003) $Revision: 1.9 $ Revision 1.8 2006/05/31 14:26:55
 *         bzjrnl
 * @author David Duncan (2003) $Revision: 1.9 $ Change: TI901
 * @author David Duncan (2003) $Revision: 1.9 $ Comment: Weblogic Upgrade -
 *         Standadise code formatting
 * @author David Duncan (2003) $Revision: 1.9 $ Revision 1.7 2006/04/26 09:01:54
 *         bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.6 2004/11/04 14:35:14 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.5 2004/08/03 13:32:52 tz0d5m Daily list performance improvements,
 * seperate the monolithic daily list query out into seperate queries, and
 * provide access to each seperately.
 * 
 * Acquire the new daily lists.
 * 
 * Revision 1.4 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
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
            // @todo - do not want to have 2 delegates here...
            PSControllerBeanBusinessDelegate delegate = PSControllerBeanBusinessDelegate.DelegateFactory.getInstance();
            WitnessControllerBeanBusinessDelegate witnessDelegate = WitnessControllerBeanBusinessDelegate.DelegateFactory
                    .getInstance();

            DailyListWithJudge[] dl = delegate.getDailyListWithJudge(witnessDelegate
                    .getCourtIdByTerminalName(getTerminalName(actionEnvironment)), new Date());
            if (dl.length > 0) {
                actionEnvironment.setRequestParameter("hearings", dl);
            }

            actionEnvironment.setResponseName("viewdailylistcomplete");
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
    }
}
