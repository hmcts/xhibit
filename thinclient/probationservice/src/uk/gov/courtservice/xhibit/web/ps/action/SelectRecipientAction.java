package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
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
 * Title: SelectRecipientAction
 * </p>
 * <p>
 * Description: The the action for selecting a recipient for a psrrequest.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.17 $ $Log:
 *         SelectRecipientAction.java,v $ Revision 1.15 2006/04/26 09:01:53
 *         bzjrnl Change: TI901 Comment: Weblogic Upgrade
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
 * Revision 1.10 2003/09/18 10:40:29 tzj8k5 Refactoring for text validation
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
 * Revision 1.4 2003/03/26 16:54:51 fz0n8j Bug fixes.
 * 
 * Revision 1.3 2003/03/21 17:28:39 fz0n8j *** empty log message ***
 * 
 * Revision 1.2 2003/03/19 21:14:56 fz0n8j Modified for new mappings.
 * 
 * Revision 1.1 2003/03/19 19:34:11 fz0n8j Added to cvs.
 * 
 * 
 */
public class SelectRecipientAction extends TerminalCookieAction {

    /**
     * Empty default constructor
     */
    public SelectRecipientAction() {
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
            String requestObjectId = (String) actionEnvironment.getRequestParameter("objectid");
            String enteredHrgDate = (String) actionEnvironment.getRequestParameter("hearingDateString");
            String enteredNoLaterDate = (String) actionEnvironment.getRequestParameter("nolaterthanField");
            PSRRequestValueSet requestValueFromSession = (PSRRequestValueSet) actionEnvironment
                    .getSessionParameter(requestObjectId);
            PSRRequestValueSet requestValue = delegate.findRequestByPrimaryKey(requestValueFromSession.getRequestId(),
                    getTerminalName(actionEnvironment)); // get the value
            // again from
            // the database
            // . . .
            PSRRequestDetailBean bean = ActionUtil.getRequestDetails(requestValue); // make
                                                                                    // a
                                                                                    // bean

            bean.setDefendantLocation(new StringField((String) actionEnvironment
                    .getRequestParameter("defendantLocation"), 100, true));
            bean.setProbationContact(new StringField(
                    (String) actionEnvironment.getRequestParameter("probationContact"), 30, true));
            bean.setAntecedents(new StringField((String) actionEnvironment.getRequestParameter("antecedents"), 255,
                    true));
            bean.setCpsOffice(new StringField((String) actionEnvironment.getRequestParameter("cpsOffice"), 30, true));
            bean.setCircumstances(new StringField((String) actionEnvironment.getRequestParameter("circumstances"), 255,
                    true));
            bean.setComments(new StringField((String) actionEnvironment.getRequestParameter("comments"), 255, true));
            bean.setAvailable(new StringField((String) actionEnvironment.getRequestParameter("available"), 30, true));
            if (bean.isValid()) // then save and go to the recipient selection
            {
                ActionUtil.modifyRequestValue(requestValue.getRequest(), bean);
                delegate.updateRequestValues(requestValue);
                requestValue = delegate.findRequestByPrimaryKey(requestValueFromSession.getRequestId(),
                        getTerminalName(actionEnvironment)); // get the value
                // again from
                // the database
                // . . .
                List recipientvalues = delegate.getAllRecipients();
                String id = KeyFactory.getInstance().nextKey();
                HashMap recipientsAndRequest = new HashMap();
                recipientsAndRequest.put("recipients", recipientvalues);
                recipientsAndRequest.put("request", requestValue);
                actionEnvironment.setSessionParameter(id, recipientsAndRequest);
                actionEnvironment.setRequestParameter("objectid", id);
                List beans = ActionUtil.getRecipientSummaries(recipientvalues);
                Collections.sort(beans);
                if (!beans.isEmpty()) {
                    actionEnvironment.setRequestParameter("recipientsummaries", beans);
                }
                actionEnvironment.setRequestParameter("userhearingdate", enteredHrgDate);
                actionEnvironment.setRequestParameter("usernolaterdate", enteredNoLaterDate);
                actionEnvironment.setRequestParameter("requestObjectId", requestObjectId);
                actionEnvironment.setResponseName("selectrecipientcomplete");
            } else { // go back to edit for the errors . . . .
                String id = KeyFactory.getInstance().nextKey();
                actionEnvironment.setSessionParameter(id, requestValue); // put
                // value
                // objects
                // into
                // session
                // . .
                // .
                actionEnvironment.setRequestParameter("objectid", id);
                actionEnvironment.setRequestParameter("psrRequestDetail", bean);
                actionEnvironment.setResponseName("selectrecipientinvalid");
            }
        }
    }

}
