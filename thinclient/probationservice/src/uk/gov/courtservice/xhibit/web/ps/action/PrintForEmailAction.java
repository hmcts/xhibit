package uk.gov.courtservice.xhibit.web.ps.action;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRequestValueSet;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.bean.StringField;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.KeyFactory;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRequestDetailBean;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: PrintForEmailAction
 * </p>
 * <p>
 * Description: The action to print for email, the same as the print action but
 * the session parameters are different.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.8 $ $Log:
 *         PrintForEmailAction.java,v $ Revision 1.6 2006/04/26 09:01:52 bzjrnl
 *         Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.5 2003/12/10 14:35:06 xzmw8n Implemented check token functionality
 * 
 * Revision 1.4 2003/10/27 10:58:06 tzj8k5 Amended to pick up Full User name
 * from Active Directory
 * 
 * Revision 1.3 2003/09/18 10:40:29 tzj8k5 Refactoring for text validation
 * 
 * Revision 1.2 2003/08/11 08:22:12 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.1 2003/03/24 16:45:22 fz0n8j Added/Modified for Email
 * functionality.
 * 
 * 
 */
public class PrintForEmailAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public PrintForEmailAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("loggedout");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }

            PSControllerBeanBusinessDelegate delegate = PSControllerBeanBusinessDelegate.DelegateFactory.getInstance();
            PSRRequestValueSet requestValueFromSession = (PSRRequestValueSet) actionEnvironment
                    .getSessionParameter((String) actionEnvironment.getRequestParameter("objectid"));
            PSRRequestDetailBean bean = ActionUtil.getRequestDetails(requestValueFromSession);
            bean.setUserName(new StringField(ActionUtil.getUserName(), 30, true));
            String id = KeyFactory.getInstance().nextKey();
            actionEnvironment.setRequestParameter("psrRequestDetail", bean);
            actionEnvironment.setResponseName("printforemailcomplete");
        }
    }
}
