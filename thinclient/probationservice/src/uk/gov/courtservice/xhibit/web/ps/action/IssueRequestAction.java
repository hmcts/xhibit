package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRequestValueSet;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.bean.StringField;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.KeyFactory;
import uk.gov.courtservice.xhibit.web.framework.util.PDFUtil;
import uk.gov.courtservice.xhibit.web.framework.util.ResourceUtil;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRequestDetailBean;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: IssueRequestAction
 * </p>
 * <p>
 * Description: The the action issue a psr request.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.21 $ $Revision:
 *         1.19 $ $Log: IssueRequestAction.java,v $
 *         1.19 $ Revision 1.21  2006/06/05 12:32:26  bzjrnl
 *         1.19 $ Change: TI901
 *         1.19 $ Comment: Weblogic Upgrade - Standadise code formatting tab fix
 *         1.19 $ 1.19 $ Revision 1.20
 *         2006/05/31 14:26:54 bzjrnl 1.19 $ Change: TI901 1.19 $ Comment:
 *         Weblogic Upgrade - Standadise code formatting 1.19 $ Revision 1.19
 *         2006/04/26 09:01:52 bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.18 2004/11/04 14:35:14 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.17 2004/10/26 14:33:41 xztnfq 56619 - population of hash map was
 * different for print and issue PSR. Issue now brought into line with Print
 * neil.entwistle-eds@eds.com
 * 
 * Revision 1.15.6.1 2004/10/26 14:25:18 xztnfq 56619 - population of hash map
 * was different for print and issue PSR. Issue now brought into line with Print
 * neil.entwistle-eds@eds.com
 * 
 * Revision 1.15 2004/06/15 12:41:56 tzj8k5 PSR email issue
 * 
 * Revision 1.14 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
 * 
 * Revision 1.13 2003/12/10 14:35:06 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.12 2003/10/27 10:58:06 tzj8k5 Amended to pick up Full User name
 * from Active Directory
 * 
 * Revision 1.11 2003/09/18 10:40:29 tzj8k5 Refactoring for text validation
 * 
 * Revision 1.10 2003/08/28 12:37:06 rz7jlh Now creates the pdf in this action
 * rather than doing a callback.
 * 
 * Revision 1.9 2003/08/11 08:22:12 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.8 2003/05/01 15:29:50 fz0n8j Now uses new terminal action.
 * 
 * Revision 1.7 2003/04/29 17:15:29 fz0n8j Merged devBranch-2b-030404. (WDF)
 * 
 * Revision 1.6.2.1 2003/04/29 17:12:36 fz0n8j new e-mail thing.
 * 
 * Revision 1.6 2003/03/28 15:28:31 fz0n8j Changed terminal lookup back to
 * machine name . . .
 * 
 * Revision 1.5 2003/03/27 16:24:34 fz0n8j Changed to remote address again.
 * 
 * Revision 1.4 2003/03/27 16:21:43 fz0n8j Modified to use get remote address.
 * 
 * Revision 1.3 2003/03/25 12:42:45 fz0n8j Improved error handleing + logging.
 * 
 * Revision 1.2 2003/03/24 16:45:22 fz0n8j Added/Modified for Email
 * functionality.
 * 
 * Revision 1.1 2003/03/20 17:40:39 fz0n8j Added to cvs
 * 
 */
public class IssueRequestAction extends TerminalCookieAction {
    /**
     * The log4j logger
     */
    private static final Logger log = CSServices.getLogger(IssueRequestAction.class);

    /**
     * Empty default constructor
     */
    public IssueRequestAction() {
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

            String sender = (String) actionEnvironment.getSessionParameter("sender");

            PSControllerBeanBusinessDelegate delegate = PSControllerBeanBusinessDelegate.DelegateFactory.getInstance();
            String objectid = (String) actionEnvironment.getRequestParameter("objectid");
            PSRRequestValueSet requestValueFromSession = (PSRRequestValueSet) actionEnvironment
                    .getSessionParameter(objectid);

            // check to see if there is a probation office email address
            // otherwise set one up using the sender information
            if (requestValueFromSession.getProbation().getContacts().get("PS_Email") == null) {
                Integer addressId = requestValueFromSession.getProbation().getAddress().getAddressId();
                XhbContactDetailBasicValue contactDetail = new XhbContactDetailBasicValue();
                contactDetail.setAddressId(addressId);
                contactDetail.setContactType("PS_Email");
                contactDetail.setContactValue(sender);
                requestValueFromSession.getProbation().getContacts().put("PS_Email", contactDetail);
            }

            /*
             * Used to be a callback from when HTML was attached, now uses PDF . . .
             * if (actionEnvironment.getRequestedSessionId() == null) { throw
             * new FrameworkException("Cannot get session id!"); } String
             * externalURL =
             * actionEnvironment.getExternalUrl("/printpsrrequestdetails;jsessionid=" +
             * actionEnvironment.getRequestedSessionId() + "?objectid=" +
             * objectid); try{ URL url = new URL(externalURL); URLConnection
             * conn = url.openConnection(); InputStream is =
             * conn.getInputStream(); byte[] pdf = new
             * byte[conn.getContentLength()];
             * is.read(pdf,0,conn.getContentLength()); log.debug("externalURL:" +
             * externalURL); //log.debug("email:" + email);
             * requestValueFromSession.setEmail(pdf); // set the email to be
             * sent for this request } catch (Exception e){ throw new
             * FrameworkException(e); }
             */

            PSRRequestDetailBean bean = ActionUtil.getRequestDetails(requestValueFromSession);
            bean.setUserName(new StringField(ActionUtil.getUserName(), 30, true));

            HashMap data = new HashMap();
            // move data into the hashmap for pdf generation

            data.put("antecedents", bean.getAntecedents().getValue());
            data.put("available", bean.getAvailable().getValue());
            data.put("circumstances", bean.getCircumstances().getValue());
            data.put("codefendants", bean.getCoDefendants().getValue());
            data.put("comments", bean.getComments().getValue());
            data.put("courtname", bean.getCourtName().getValue());
            data.put("officename", bean.getProbationDetails().getOfficeName().getValue());
            data.put("cpsoffice", bean.getCpsOffice().getValue());
            data.put("creationdate", bean.getCreationDate());
            data.put("currentdate", bean.getCurrentDate());
            data.put("defendantaddress", bean.getDefendantAddress().getLine1().getValue());
            data.put("defendantage", String.valueOf(bean.getDefendantAge()));
            data.put("defendantdob", bean.getDefendantDOB());
            data.put("defendantforenames", bean.getDefendantForenames().getValue());
            data.put("defendantlocation", bean.getDefendantLocation().getValue());
            data.put("defendantsurname", bean.getDefendantSurname().getValue());
            data.put("defendanttelephone", bean.getDefendantTelephone().getValue());
            data.put("hearingdate", bean.getHearingDate().getValue());
            data.put("judgetitle", bean.getJudgeTitle().getValue());
            data.put("nolaterthan", bean.getNoLaterThan().getValue());
            data.put("offences", bean.getOffences().getValue());
            data.put("probationaddress", bean.getProbationAddress().getValue());
            data.put("probationcontact", bean.getProbationContact().getValue());
            data.put("probationaddress1", bean.getProbationDetails().getAddress().getLine1().getValue());
            data.put("probationaddress2", bean.getProbationDetails().getAddress().getLine2().getValue());
            data.put("probationaddress3", bean.getProbationDetails().getAddress().getLine3().getValue());
            data.put("probationaddress4", bean.getProbationDetails().getAddress().getLine4().getValue());
            data.put("probationaddresstown", bean.getProbationDetails().getAddress().getTown().getValue());
            data.put("probationcounty", bean.getProbationDetails().getAddress().getCounty().getValue());
            data.put("probationpostcode", bean.getProbationDetails().getAddress().getPostcode().getValue());
            data.put("probationtelephone", bean.getProbationDetails().getTelephone().getValue());
            data.put("probationfax", bean.getProbationDetails().getFax().getValue());
            data.put("probationemail", bean.getProbationDetails().getEmail().getValue());
            data.put("recipientoffice", bean.getrecipientDetails().getOfficeName().getValue());
            data.put("recipientaddress", bean.getrecipientDetails().getAddress().getLine1().getValue());
            data.put("recipientfax", bean.getrecipientDetails().getFax().getValue());
            data.put("recipientemail", bean.getrecipientDetails().getEmail().getValue());
            data.put("solicitorname", bean.getSolicitorName().getValue());
            data.put("solicitortelephone", bean.getSolicitorTelephone().getValue());
            data.put("username", bean.getUserName().getValue());

            Properties messages = new Properties();
            try {
                messages.load(ResourceUtil.getResourceAsStream("Messages.properties"));
            } catch (java.io.IOException e) {
                throw new FrameworkException(e);
            }
            data.putAll(messages); // add the messages to the data hashmap
            // . . .
            byte[] content = PDFUtil.getPDF("psrrequest.xsl", data);

            log.debug("Adding PDF bytes to PSR about to be issued . . . .");

            requestValueFromSession.setEmail(content);

            // get the value objects from session . . .
            // issue the psr request . . .
            delegate.issuePSRRequest(requestValueFromSession);
            // set up for summary view.
            List requestValues = delegate.findAllUnissuedRequests(getTerminalName(actionEnvironment));
            String id = KeyFactory.getInstance().nextKey();
            actionEnvironment.setSessionParameter(id, requestValues);
            actionEnvironment.setRequestParameter("objectid", id);
            List beans = ActionUtil.getRequestSummaries(requestValues);
            if (!beans.isEmpty()) {
                actionEnvironment.setRequestParameter("psrRequestSummaries", beans);
            }
            actionEnvironment.setResponseName("viewunissuedpsrrequestsummaries");
        }
    }
}