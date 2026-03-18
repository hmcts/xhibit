package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.HashMap;
import java.util.List;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRequestValueSet;
import uk.gov.courtservice.xhibit.business.vos.entities.PSRSummaryValue;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.bean.StringField;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRequestDetailBean;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: PrintPSRRequestDetailsAction
 * </p>
 * <p>
 * Description: The the action for displaying the psr details for print.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.32 $ $Log:
 *         PrintPSRRequestDetailsAction.java,v $ Revision 1.30 2006/04/26
 *         09:01:53 bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.29 2004/11/04 14:35:14 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.28 2004/08/12 15:56:21 zz7n1c Merged from 6_X_BRANCH on 12/08/2004
 * 
 * Revision 1.27.6.1 2004/08/05 07:47:41 tzj8k5 56339 - DateField validation
 * 
 * Revision 1.27 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
 * 
 * Revision 1.26 2004/03/11 16:42:23 qzd3k3 Code linting.
 * 
 * Revision 1.22.18.1 2004/03/03 13:26:03 tzj8k5 55563 PSR Performance Fix
 * 
 * Revision 1.23 2004/02/24 08:14:20 tzj8k5 PSR Request update to use FLR
 * 
 * Revision 1.22 2003/12/10 14:35:06 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.21 2003/10/27 10:58:06 tzj8k5 Amended to pick up Full User name
 * from Active Directory
 * 
 * Revision 1.20 2003/10/06 11:47:13 xzmw8n Added to recipient, fax no and email
 * details.
 * 
 * Revision 1.19 2003/09/18 10:40:29 tzj8k5 Refactoring for text validation
 * 
 * Revision 1.18 2003/08/11 08:22:13 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.17 2003/05/01 15:29:51 fz0n8j Now uses new terminal action.
 * 
 * Revision 1.16 2003/04/29 17:15:29 fz0n8j Merged devBranch-2b-030404. (WDF)
 * 
 * Revision 1.15.2.1 2003/04/09 16:16:17 fz0n8j Print now uses new pdf framework
 * classes, added xsl file.
 * 
 * Revision 1.15 2003/03/28 15:28:31 fz0n8j Changed terminal lookup back to
 * machine name . . .
 * 
 * Revision 1.14 2003/03/27 16:24:35 fz0n8j Changed to remote address again.
 * 
 * Revision 1.13 2003/03/27 16:21:43 fz0n8j Modified to use get remote address.
 * 
 * Revision 1.12 2003/03/26 16:54:51 fz0n8j Bug fixes.
 * 
 * Revision 1.11 2003/03/24 16:45:22 fz0n8j Added/Modified for Email
 * functionality.
 * 
 * Revision 1.10 2003/03/19 21:14:55 fz0n8j Modified for new mappings.
 * 
 * Revision 1.9 2003/03/19 12:28:32 fz0n8j Sets username in bean.
 * 
 * Revision 1.8 2003/03/18 22:03:10 fz0n8j Now calls database!
 * 
 * Revision 1.7 2003/03/18 13:25:09 fz0n8j Moved action utils
 * 
 * Revision 1.6 2003/03/18 11:16:34 fz0n8j bug fix, now uses database probation
 * details
 * 
 * Revision 1.5 2003/03/17 11:32:19 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.4 2003/03/14 11:17:48 fz0n8j Changed setParameter and getParameter
 * to setRequestParameter and getRequestParameter.
 * 
 * Revision 1.3 2003/03/12 18:11:09 fz0n8j Now adds a bean with dummy data to
 * the session, ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:45 fz0n8j Added CVS log comments - ecawley
 * 
 * 
 */
public class PrintPSRRequestDetailsAction extends TerminalCookieAction {

    /**
     * Empty default constructor
     */
    public PrintPSRRequestDetailsAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void terminalPerformAction(ActionEnvironment actionEnvironment) {
        PSControllerBeanBusinessDelegate delegate = PSControllerBeanBusinessDelegate.DelegateFactory.getInstance();
        Object fromSession = actionEnvironment.getSessionParameter((String) actionEnvironment
                .getRequestParameter("objectid"));
        PSRRequestDetailBean bean;
        PSRRequestValueSet requestValue;
        // handle scenario when select print psr from list of all psrs
        if (fromSession instanceof List) {
            PSRSummaryValue requestValueFromSession;
            List requestValues = (List) fromSession;
            requestValueFromSession = (PSRSummaryValue) requestValues.get(Integer.parseInt((String) actionEnvironment
                    .getRequestParameter("id")));
            requestValue = delegate.findRequestByPrimaryKey(requestValueFromSession.getId(),
                    getTerminalName(actionEnvironment));
        } else
        // handle scenario when select print psr from individual psr viewed
        {
            PSRRequestValueSet requestValueFromSession;
            requestValueFromSession = (PSRRequestValueSet) actionEnvironment
                    .getSessionParameter((String) actionEnvironment.getRequestParameter("objectid"));
            requestValue = delegate.findRequestByPrimaryKey(requestValueFromSession.getRequestId(),
                    getTerminalName(actionEnvironment));
        }

        bean = ActionUtil.getRequestDetails(requestValue);
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

        actionEnvironment.setRequestParameter(uk.gov.courtservice.xhibit.web.framework.response.PDFResponse.dataKey,
                data);
        actionEnvironment.setResponseName("printpsrrequestdetailspdfcomplete");
    }
    // old html printing . . .
    // actionEnvironment.setRequestParameter("psrRequestDetail",bean);
    // actionEnvironment.setResponseName("printpsrrequestdetailscomplete");

}
