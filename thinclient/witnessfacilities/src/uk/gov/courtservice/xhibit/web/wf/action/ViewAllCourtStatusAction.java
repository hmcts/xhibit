package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCourtStatusValue;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: View Summary By Name Action
 * </p>
 * <p>
 * Description: The action for viewing the all court status.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley $Revision: 1.5 $
 * 
 */

public class ViewAllCourtStatusAction extends TerminalCookieAction {

    protected static Logger log = CSServices.getLogger(ViewAllCourtStatusAction.class);

    /**
     * Empty default constructor
     */
    public ViewAllCourtStatusAction() {
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
        WitnessControllerBeanBusinessDelegate delegate = WitnessControllerBeanBusinessDelegate.DelegateFactory
                .getInstance();
        Integer courtId = delegate.getCourtIdByTerminalName(getTerminalName(actionEnvironment));
        Collection details = delegate.getAllCourtStatus(courtId.intValue(), new java.util.Date());

        AllCourtStatusValue[] acs = new AllCourtStatusValue[details.size()];
        int i = 0;
        for (Iterator it = details.iterator(); it.hasNext();) {
            AllCourtStatusValue als = (AllCourtStatusValue) it.next();
            acs[i] = als;
            i++;
        }
        actionEnvironment.setRequestParameter("courts", acs);

        actionEnvironment.setResponseName("viewallcourtstatuscomplete");
    }

}
