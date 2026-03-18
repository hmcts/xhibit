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
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRequestDetailBean;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: ConfirmIssueAction
 * </p>
 * <p>
 * Description: The the action for displaying the psr details for confirmation
 * before issueing.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley (2003) $Revision: 1.19 $ $Log:
 *         ConfirmIssueAction.java,v $ Revision 1.17 2006/04/26 09:01:51 bzjrnl
 *         Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.16 2005/10/12 14:52:34 tzj8k5 Int 3 Testing - Issue 37
 * 
 * Revision 1.15 2004/11/04 14:35:14 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.14 2004/08/12 15:56:21 zz7n1c Merged from 6_X_BRANCH on 12/08/2004
 * 
 * Revision 1.13.4.1 2004/08/05 07:47:41 tzj8k5 56339 - DateField validation
 * 
 * Revision 1.13 2004/06/15 12:41:56 tzj8k5 PSR email issue
 * 
 * Revision 1.12 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
 * 
 * Revision 1.11 2004/02/18 16:08:53 rzgbyh Merge with End_Hearing_Changes
 * 
 * Revision 1.10.2.1 2004/02/03 14:57:22 xzmw8n allow psr date of hearing and
 * arrive no later than fields to become editable.
 * 
 * Revision 1.10 2004/01/12 12:17:34 cz4lvy Amended to set the username (Officer
 * name) for the PSR Request Form.
 * 
 * Revision 1.9 2003/12/10 14:35:05 xzmw8n Implemented check token functionality
 * 
 * Revision 1.8 2003/09/18 10:40:28 tzj8k5 Refactoring for text validation
 * 
 * Revision 1.7 2003/08/11 08:22:11 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.6 2003/05/01 15:29:50 fz0n8j Now uses new terminal action.
 * 
 * Revision 1.5 2003/03/28 15:28:30 fz0n8j Changed terminal lookup back to
 * machine name . . .
 * 
 * Revision 1.4 2003/03/27 16:24:31 fz0n8j Changed to remote address again.
 * 
 * Revision 1.3 2003/03/27 16:21:43 fz0n8j Modified to use get remote address.
 * 
 * Revision 1.2 2003/03/26 16:54:51 fz0n8j Bug fixes.
 * 
 * Revision 1.1 2003/03/20 17:40:39 fz0n8j Added to cvs
 * 
 * 
 */
public class ConfirmIssueAction extends TerminalCookieAction {

    private static final String EMAIL = "Email";

    private static final String FAX = "Fax";

    private Logger log = CSServices.getLogger(ConfirmIssueAction.class);

    /**
     * Empty default constructor
     */
    public ConfirmIssueAction() {
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

            // hearing date only needs to be set if trying to issue.
            if (bean.getHearingDate().getValue() == null) {
                delegate.updateRequestValues(requestValue);
                actionEnvironment.setResponseName("updatepsrrequestdetailsnohearing");
                actionEnvironment.setRequestParameter("errorMessageKeys", "nohearingdate");
            }
            // check no later than when trying to issue
            else if (bean.getNoLaterThan().getValue() == null) {
                delegate.updateRequestValues(requestValue);
                actionEnvironment.setResponseName("updatepsrrequestdetailsnohearing");
                actionEnvironment.setRequestParameter("errorMessageKeys", "nolaterthandate");
            } else if (bean.isValid() && requestValue.getRequest().getPsrRecipientId() != null) // also
            // should
            // check
            // for
            // recipient
            // id
            {
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

                // set sender method of contact information information
                String methodOfContact = requestValue.getRecipient().getRecipient().getRecipientMethodOfContact();
                bean.getrecipientDetails().setMethod(methodOfContact);

                // check that some sender details are avialable
                String senderDetails = getSenderDetails(actionEnvironment, methodOfContact);
                // check to see if the sender details contains spaces and remove
                // otherwise will invalidate the InternAddres object
                if (senderDetails != null && senderDetails.indexOf(' ') > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < senderDetails.length(); i++) {
                        char c = senderDetails.charAt(i);
                        if (c != ' ') {
                            sb.append(c);
                        }
                    }
                    senderDetails = sb.toString();
                }

                if (senderDetails != null) {
                    log.debug("Sender Details Ok");
                    actionEnvironment.setSessionParameter("sender", senderDetails);
                    actionEnvironment.setResponseName("confirmissue"); // data
                    // was
                    // fine,
                    // confirm
                    // issue

                } else // no sender information was defined
                {
                    log.debug("Invalid Sender Details");
                    actionEnvironment.setResponseName("updatepsrrequestdetailsnohearing");
                    actionEnvironment.setRequestParameter("errorMessageKeys", "nosenderinformation");
                }
            } else {
                if (!bean.isValid()) {
                    actionEnvironment.setResponseName("updatepsrrequestdetailsinvalid"); // invalid
                    // data
                } else {
                    actionEnvironment.setResponseName("updatepsrrequestdetailsnorecipient"); // no
                    // recipient
                }
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

    private String getSenderDetails(ActionEnvironment actEnv, String moc) {
        String retVal = null;
        String email = null;
        String fax = null;
        String office = null;
        try {
            email = (String) actEnv.getRequestParameter("senderemail");
        } catch (ParameterNotFoundException e) {
            log.debug(e);
        }
        try {
            fax = (String) actEnv.getRequestParameter("senderfax");
        } catch (ParameterNotFoundException e) {
            log.debug(e);
        }
        try {
            office = (String) actEnv.getRequestParameter("senderoffice");
        } catch (ParameterNotFoundException e) {
            log.debug(e);
        }

        if (moc.equals(FAX)) {
            if (fax != null && !fax.trim().equals("")) {
                retVal = fax;
            } else {
                retVal = office;
            }
        } else if (moc.equals(EMAIL)) {
            if (email != null && !email.trim().equals("")) {
                retVal = email;
            } else {
                retVal = office;
            }
        }
        return retVal;
    }

}
