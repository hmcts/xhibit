package uk.gov.courtservice.xhibit.web.wf.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TrialSessionNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ResourceUtil;

/**
 * <p>
 * Title: Save Edit Trial Session Action
 * </p>
 * <p>
 * Description: The action for saving an edited trial session <p/> Copyright:
 * Copyright (c) 2003 Company: EDS <p/> Author: David Duncan (2003) $$ $Log:
 * SaveEditTrialSessionAction.java,v $ Revision 1.4 2006/03/01 11:14:03 xztnfq
 * Change: PR58315 Comment: Corrected flows and added logging
 * 
 * Revision 1.3.6.1 2006/03/01 10:51:34 xztnfq Change: PR58315 Comment:
 * Corrected flows and added logging
 * 
 * Revision 1.3 2005/04/27 08:26:59 bzjrnl Manual Merge From BRANCH_7_X
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:45 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.7 2004/04/21 09:52:40 fz1f7w Added code to get and set pagesource
 * variable
 * 
 * Revision 1.6 2004/04/14 12:57:00 tzj8k5 SetLenient on DateFormatter to ensure
 * that date is validated correctly
 * 
 * Revision 1.5 2004/03/11 16:42:23 qzd3k3 Code linting.
 * 
 * Revision 1.4.18.1 2004/03/10 12:55:09 qzd3k3 Code linting, most of these
 * fixes are poor exception handling. This may result as a side effect in more
 * bugs appearing; but that is a necessary as they were hidden bugs before.
 * 
 * Revision 1.4 2003/12/04 17:37:23 xzmw8n Fix to distinguish between duplicate
 * form submission and session timeout <p/> Revision 1.3 2003/10/14 14:04:13
 * xzmw8n Added tokens and checks to identify and reject duplicate form
 * submissions <p/> Revision 1.2 2003/04/30 11:44:01 qzd3k3 Merge from dev
 * branch. <p/> Revision 1.1.2.2 2003/04/24 16:31:15 hzf3bb *** empty log
 * message *** <p/> Revision 1.1.2.1 2003/04/22 17:34:58 hzf3bb *** empty log
 * message ***
 */
public class SaveEditTrialSessionAction extends AbstractAction {
    private static final Logger log = CSServices.getLogger(SaveEditTrialSessionAction.class);

    /**
     * Empty default constructor
     */
    public SaveEditTrialSessionAction() {
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
        log.debug("SaveEditTrialSessionAction");
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("loggedout");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }

            String trialday = (String) actionEnvironment.getRequestParameter("trialday");
            String trialdate = (String) actionEnvironment.getRequestParameter("trialdate");
            String trialsession = (String) actionEnvironment.getRequestParameter("trialsession");
            String pageRequestFrom = (String) actionEnvironment.getRequestParameter("pagesource");
            actionEnvironment.setRequestParameter("pagesource", pageRequestFrom);

            HashMap errors = new HashMap();
            HashMap values = new HashMap();
            String errormessage = null;

            String pattern = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                    .getRemoteLocale(), "dateformat");
            String caseID = (String) actionEnvironment.getRequestParameter("caseid");

            checkDate(trialdate, errors, "trialdate", false, pattern);

            boolean errorflag = false;

            if (errors.isEmpty()) {
                SkeletonSchedule schedule = null;

                try {
                    schedule = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(new Integer(caseID));
                } catch (NumberFormatException e) {
                    errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "invalidcaseid");

                } catch (ScheduleNotFoundException e) {
                    errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "noschedule");
                }

                TrialSession session = null;
                try {
                    session = schedule.getTrialSession(new Integer(trialday), trialsession);
                } catch (TrialSessionNotFoundException e) {
                    errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "notrialsession");
                }

                // Set the trial date

                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                Date date = null;
                try {
                    date = sdf.parse(trialdate);
                } catch (ParseException e) {
                    // date remains null
                    log.debug(e);
                }

                session.setAppearanceDate(date);

                try {
                    session.update();
                } catch (ModificationException e) {
                    errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "updateerror");
                }

                TrialSession[] trialsessions = schedule.getTrialSessions();

                if (trialsessions != null && trialsessions.length > 0) {
                    actionEnvironment.setRequestParameter("trialsessions", trialsessions);
                }
                actionEnvironment.setRequestParameter("caseid", caseID);

                if (errormessage != null) {
                    actionEnvironment.setRequestParameter("errormessage", errormessage);
                    errorflag = true;
                } else {
                    actionEnvironment.setResponseName("viewtrialsessioncomplete");
                }

            } else {
                errorflag = true;
            }
            if (errorflag) {
                values.put("trialday", trialday);
                values.put("trialdate", trialdate);
                values.put("trialsession", trialsession);

                actionEnvironment.setRequestParameter("values", values);
                actionEnvironment.setRequestParameter("errors", errors);
                actionEnvironment.setRequestParameter("caseid", caseID);
                actionEnvironment.setResponseName("edittrialsessioncomplete");
            }
        }

    }

    private void checkDate(String str, HashMap errors, String errorName, boolean required, String pattern) {
        if (str.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            // set lenient to false to stop the user entering dates in the
            // correct format but are invalid
            // i.e. 33/33/33
            sdf.setLenient(false);
            try {
                sdf.parse(str);
            } catch (ParseException e) {
                errors.put(errorName, "formaterror");
            }
        } else {
            if (required) {
                errors.put(errorName, "requiredfield");
            }
        }
    }

}
