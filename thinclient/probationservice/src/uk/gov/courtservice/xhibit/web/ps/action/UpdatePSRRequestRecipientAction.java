package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.HashMap;
import java.util.List;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRecipientValueSet;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRequestValueSet;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.bean.StringField;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.KeyFactory;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRequestDetailBean;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: UpdatePSRRequestRecipientAction
 * </p>
 * <p>
 * Description: The the action to update recipient details.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.17 $ $Log:
 *         UpdatePSRRequestRecipientAction.java,v $ Revision 1.15 2006/04/26
 *         09:01:53 bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.14 2004/11/04 14:35:14 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.13 2004/10/14 13:07:19 tzj8k5 PRE 305 - Missing dates entered when
 * selecting a recipient
 * 
 * Revision 1.12 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
 * 
 * Revision 1.11 2003/12/10 14:35:06 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.10 2003/12/08 17:18:52 xzmw8n Added username to response
 * 
 * Revision 1.9 2003/08/11 08:22:13 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.8 2003/05/01 15:29:51 fz0n8j Now uses new terminal action.
 * 
 * Revision 1.7 2003/03/28 15:28:31 fz0n8j Changed terminal lookup back to
 * machine name . . .
 * 
 * Revision 1.6 2003/03/27 16:24:35 fz0n8j Changed to remote address again.
 * 
 * Revision 1.5 2003/03/27 16:21:43 fz0n8j Modified to use get remote address.
 * 
 * Revision 1.4 2003/03/21 17:28:39 fz0n8j *** empty log message ***
 * 
 * Revision 1.3 2003/03/20 17:41:14 fz0n8j *** empty log message ***
 * 
 * Revision 1.2 2003/03/19 21:14:56 fz0n8j Modified for new mappings.
 * 
 * Revision 1.1 2003/03/19 19:34:12 fz0n8j Added to cvs.
 * 
 * 
 */
public class UpdatePSRRequestRecipientAction extends TerminalCookieAction {

    /**
     * Empty default constructor
     */
    public UpdatePSRRequestRecipientAction() {
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
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("loggedout");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }
            PSControllerBeanBusinessDelegate delegate = PSControllerBeanBusinessDelegate.DelegateFactory.getInstance();
            HashMap recipientsAndRequest = (HashMap) actionEnvironment.getSessionParameter((String) actionEnvironment
                    .getRequestParameter("objectid"));
            PSRRequestValueSet requestValueFromSession = (PSRRequestValueSet) recipientsAndRequest.get("request");
            PSRRequestValueSet requestValue = delegate.findRequestByPrimaryKey(requestValueFromSession.getRequestId(),
                    getTerminalName(actionEnvironment)); // get the value
            // again from
            // the database
            // . . .

            String enteredHrgDate = (String) actionEnvironment.getRequestParameter("hrgdate");
            String enteredNoLaterDate = (String) actionEnvironment.getRequestParameter("nolaterdate");

            List recipientValues = (List) recipientsAndRequest.get("recipients");
            PSRRecipientValueSet recipientValueFromSession = (PSRRecipientValueSet) recipientValues.get(Integer
                    .parseInt((String) actionEnvironment.getRequestParameter("id")));

            // so now we habe the recipient value and the request value . .
            // .

            requestValue.getRequest().setPsrRecipientId(recipientValueFromSession.getRecipient().getPrimaryKey()); // set
            // the
            // recipient
            // id .
            // . .
            delegate.updateRequestValues(requestValue); // update the request .
            // . .
            requestValue = delegate.findRequestByPrimaryKey(requestValue.getRequestId(),
                    getTerminalName(actionEnvironment)); // get
            // the
            // value
            // again
            // from
            // the
            // database
            // . .
            // .
            PSRRequestDetailBean bean = ActionUtil.getRequestDetails(requestValue); // make
                                                                                    // a
                                                                                    // bean
                                                                                    // . .
                                                                                    // .
            bean.setUserName(new StringField(ActionUtil.getUserName(), 30, true));
            String id = KeyFactory.getInstance().nextKey();
            actionEnvironment.setSessionParameter(id, requestValue); // put
            // value
            // objects
            // into
            // session
            // . . .
            actionEnvironment.setRequestParameter("objectid", id);
            if (enteredHrgDate != null) {
                actionEnvironment.setRequestParameter("hrgDate", enteredHrgDate);
            }
            if (enteredNoLaterDate != null) {
                actionEnvironment.setRequestParameter("nolaterDate", enteredNoLaterDate);
            }
            actionEnvironment.setRequestParameter("psrRequestDetail", bean);
            actionEnvironment.setResponseName("updatepsrrequestrecipientcomplete");
        }
    }

}
