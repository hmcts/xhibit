package uk.gov.courtservice.xhibit.web.ps.action;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRequestValueSet;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.bean.DateField;
import uk.gov.courtservice.xhibit.web.framework.bean.StringField;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.KeyFactory;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRequestDetailBean;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: UpdatePSRRequestDetailsAction
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
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.21 $ $Log:
 *         UpdatePSRRequestDetailsAction.java,v $ Revision 1.19 2006/04/26
 *         09:01:53 bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.18 2005/04/27 08:26:56 bzjrnl Manual Merge From BRANCH_7_X
 * 
 * Revision 1.16.4.1 2005/04/20 09:06:18 bzjrnl Changes to error handling to
 * improve display of error messages.
 * 
 * Revision 1.16 2004/11/04 14:35:14 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.15 2004/08/12 15:56:21 zz7n1c Merged from 6_X_BRANCH on 12/08/2004
 * 
 * Revision 1.14.6.1 2004/08/05 07:47:41 tzj8k5 56339 - DateField validation
 * 
 * Revision 1.14 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
 * 
 * Revision 1.13 2004/02/18 16:08:53 rzgbyh Merge with End_Hearing_Changes
 * 
 * Revision 1.12.10.1 2004/02/03 14:57:22 xzmw8n allow psr date of hearing and
 * arrive no later than fields to become editable.
 * 
 * Revision 1.12 2003/12/10 14:35:06 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.11 2003/12/08 17:29:04 xzmw8n Added username to response
 * 
 * Revision 1.10 2003/09/18 10:40:29 tzj8k5 Refactoring for text validation
 * 
 * Revision 1.9 2003/08/11 08:22:13 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.8 2003/05/01 15:29:51 fz0n8j Now uses new terminal action.
 * 
 * Revision 1.7 2003/04/04 08:24:31 fz0n8j Blocks update on issued request.
 * 
 * Revision 1.6 2003/03/28 15:28:31 fz0n8j Changed terminal lookup back to
 * machine name . . .
 * 
 * Revision 1.5 2003/03/27 16:24:35 fz0n8j Changed to remote address again.
 * 
 * Revision 1.4 2003/03/27 16:21:43 fz0n8j Modified to use get remote address.
 * 
 * Revision 1.3 2003/03/20 17:41:14 fz0n8j *** empty log message ***
 * 
 * Revision 1.2 2003/03/19 21:14:56 fz0n8j Modified for new mappings.
 * 
 * Revision 1.1 2003/03/19 19:34:11 fz0n8j Added to cvs.
 * 
 * 
 */
public class UpdatePSRRequestDetailsAction extends TerminalCookieAction {

    /**
     * Empty default constructor
     */
    public UpdatePSRRequestDetailsAction() {
    }

    private static Logger log = CSServices.getLogger(UpdatePSRRequestDetailsAction.class);

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
            PSRRequestValueSet requestValueFromSession = (PSRRequestValueSet) actionEnvironment
                    .getSessionParameter((String) actionEnvironment.getRequestParameter("objectid"));
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
            bean.setHearingDate(new DateField((String) actionEnvironment.getRequestParameter("hearingDateString")));
            bean.setNoLaterThan(new DateField((String) actionEnvironment.getRequestParameter("nolaterthanField")));

            // Check issued just in case someone somehow got to the edit
            // page on an issued psr request . . .
            if (bean.isValid() && !isIssued(requestValue)) {
                ActionUtil.modifyRequestValue(requestValue.getRequest(), bean);
                delegate.updateRequestValues(requestValue);
                requestValue = delegate.findRequestByPrimaryKey(requestValueFromSession.getRequestId(),
                        getTerminalName(actionEnvironment)); // get the value
                // again from
                // the database
                // . . .
                bean = ActionUtil.getRequestDetails(requestValue); // make
                // a
                // bean
                // again
                bean.setUserName(new StringField(ActionUtil.getUserName(), 30, true));
                actionEnvironment.setResponseName("updatepsrrequestdetailscomplete");
            } else {
                actionEnvironment.setRequestParameter("errorMessageKeys", bean.getErrorMessageKeys());
                actionEnvironment.setResponseName("updatepsrrequestdetailsinvalid");
            }

            String id = KeyFactory.getInstance().nextKey();
            actionEnvironment.setSessionParameter(id, requestValue); // put
            // value
            // objects
            // into
            // session
            // . . .
            actionEnvironment.setRequestParameter("objectid", id);
            actionEnvironment.setRequestParameter("psrRequestDetail", bean);
        }
    }

    private static boolean isIssued(PSRRequestValueSet requestValue) {
        return requestValue.getRequest().getPsrStatus().equalsIgnoreCase("ISSUED");
    }

}