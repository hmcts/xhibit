package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.List;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRequestValueSet;
import uk.gov.courtservice.xhibit.business.vos.entities.PSRSummaryValue;
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
 * Title: EditPSRRequestDetailsAction
 * </p>
 * <p>
 * Description: The the action for displaying the psr details for editing.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.20 $ $Log:
 *         EditPSRRequestDetailsAction.java,v $ Revision 1.18 2006/04/26
 *         09:01:52 bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.17 2004/11/04 14:35:14 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.16 2004/10/14 13:07:19 tzj8k5 PRE 305 - Missing dates entered when
 * selecting a recipient
 * 
 * Revision 1.15 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
 * 
 * Revision 1.14 2004/03/11 16:42:23 qzd3k3 Code linting.
 * 
 * Revision 1.12.18.1 2004/03/03 13:26:02 tzj8k5 55563 PSR Performance Fix
 * 
 * Revision 1.13 2004/02/24 08:13:57 tzj8k5 PSR Request update to use FLR
 * 
 * Revision 1.12 2003/12/10 14:35:05 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.11 2003/12/09 16:40:11 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.10 2003/10/27 10:58:05 tzj8k5 Amended to pick up Full User name
 * from Active Directory
 * 
 * Revision 1.9 2003/09/18 10:40:28 tzj8k5 Refactoring for text validation
 * 
 * Revision 1.8 2003/08/11 08:22:11 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.7 2003/05/01 15:29:50 fz0n8j Now uses new terminal action.
 * 
 * Revision 1.6 2003/03/28 15:28:31 fz0n8j Changed terminal lookup back to
 * machine name . . .
 * 
 * Revision 1.5 2003/03/27 16:24:32 fz0n8j Changed to remote address again.
 * 
 * Revision 1.4 2003/03/27 16:21:43 fz0n8j Modified to use get remote address.
 * 
 * Revision 1.3 2003/03/26 16:54:51 fz0n8j Bug fixes.
 * 
 * Revision 1.2 2003/03/19 21:14:55 fz0n8j Modified for new mappings.
 * 
 * Revision 1.1 2003/03/19 19:34:11 fz0n8j Added to cvs.
 * 
 * 
 */
public class EditPSRRequestDetailsAction extends TerminalCookieAction {

    private static final String VIEW2EDIT = "View2Edit";

    /**
     * Empty default constructor
     */
    public EditPSRRequestDetailsAction() {
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
            Object fromSession = actionEnvironment.getSessionParameter((String) actionEnvironment
                    .getRequestParameter("objectid"));
            PSRRequestDetailBean bean;
            PSRRequestValueSet requestValue;
            String enteredHrgDate = VIEW2EDIT;
            String enteredNoLaterDate = VIEW2EDIT;
            // handle scenario when select edit psr from list of all psrs
            if (fromSession instanceof List) {
                PSRSummaryValue requestValueFromSession;
                List requestValues = (List) fromSession;
                requestValueFromSession = (PSRSummaryValue) requestValues.get(Integer
                        .parseInt((String) actionEnvironment.getRequestParameter("id")));
                requestValue = delegate.findRequestByPrimaryKey(requestValueFromSession.getId(),
                        getTerminalName(actionEnvironment));
            } else
            // handle scenario when select edit psr from individual psr
            // viewed
            {
                PSRRequestValueSet requestValueFromSession;
                requestValueFromSession = (PSRRequestValueSet) actionEnvironment
                        .getSessionParameter((String) actionEnvironment.getRequestParameter("objectid"));
                requestValue = delegate.findRequestByPrimaryKey(requestValueFromSession.getRequestId(),
                        getTerminalName(actionEnvironment));
                enteredHrgDate = (String) actionEnvironment.getRequestParameter("hrgdate");
                enteredNoLaterDate = (String) actionEnvironment.getRequestParameter("nolaterdate");
            }
            bean = ActionUtil.getRequestDetails(requestValue);
            bean.setUserName(new StringField(ActionUtil.getUserName(), 30, true));
            String id = KeyFactory.getInstance().nextKey();
            actionEnvironment.setSessionParameter(id, requestValue); // put
            // value
            // objects
            // into
            // session
            // . . .
            if (!enteredHrgDate.equals(VIEW2EDIT) && enteredHrgDate != null) {
                actionEnvironment.setRequestParameter("hrgDate", enteredHrgDate);
            }
            if (!enteredNoLaterDate.equals(VIEW2EDIT) && enteredNoLaterDate != null) {
                actionEnvironment.setRequestParameter("nolaterDate", enteredNoLaterDate);
            }

            actionEnvironment.setRequestParameter("objectid", id);
            actionEnvironment.setRequestParameter("psrRequestDetail", bean);
            actionEnvironment.setResponseName("editpsrrequestdetailscomplete");
        }
    }
}