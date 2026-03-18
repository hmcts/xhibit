package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCaseStatusValue;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * Action for viewing the Case Status details
 */
public class ViewAllCaseStatusAction extends TerminalCookieAction {

    protected static Logger log = CSServices.getLogger(ViewAllCourtStatusAction.class);

    /**
     * Empty default constructor
     */
    public ViewAllCaseStatusAction() {
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
        Collection details = delegate.getAllCaseStatus(courtId.intValue(), new java.util.Date());

        AllCaseStatusValue[] acs = new AllCaseStatusValue[details.size()];
        int i = 0;
        for (Iterator it = details.iterator(); it.hasNext();) {
            AllCaseStatusValue als = (AllCaseStatusValue) it.next();
            acs[i] = als;
            i++;
        }
        if (acs.length > 0) {
            actionEnvironment.setRequestParameter("courts", acs);
        }
        actionEnvironment.setResponseName("viewallcasestatuscomplete");
    }

}
