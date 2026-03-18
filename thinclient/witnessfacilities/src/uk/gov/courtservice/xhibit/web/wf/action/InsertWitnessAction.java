package uk.gov.courtservice.xhibit.web.wf.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoScheduleForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonSessionNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessCreationException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummary;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ResourceUtil;

/**
 * <p>
 * Title: Insert Witness Action
 * </p>
 * <p>
 * Description: The action for inserting a witness. <p/> Copyright: Copyright
 * (c) 2003 Company: EDS <p/> Author: David Duncan (2003) $$ $Log:
 * InsertWitnessAction.java,v $ Revision 1.3 2005/04/27 08:26:58 bzjrnl Manual
 * Merge From BRANCH_7_X
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:44 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.12 2004/03/11 16:42:23 qzd3k3 Code linting.
 * 
 * Revision 1.11.18.1 2004/03/10 12:55:08 qzd3k3 Code linting, most of these
 * fixes are poor exception handling. This may result as a side effect in more
 * bugs appearing; but that is a necessary as they were hidden bugs before.
 * 
 * Revision 1.11 2003/12/08 14:50:07 cz4lvy Refactored code to directly access
 * isUserInRole value in the JSP <p/> Revision 1.10 2003/12/04 17:37:23 xzmw8n
 * Fix to distinguish between duplicate form submission and session timeout <p/>
 * Revision 1.9 2003/10/14 14:04:13 xzmw8n Added tokens and checks to identify
 * and reject duplicate form submissions <p/> Revision 1.8 2003/05/20 14:51:23
 * fz0n8j Now sorts witness sessions. <p/> Revision 1.7 2003/05/14 14:45:42
 * fz0n8j Bug fix, added printskeletonbydayaction <p/> Revision 1.6 2003/05/14
 * 13:09:57 fz0n8j Bug fixes and first version of print by day. <p/> Revision
 * 1.5 2003/05/13 10:02:22 fz0n8j Bug fix. <p/> Revision 1.4 2003/05/12 15:18:19
 * rz3jq5 Hack fix until Ed can sort out. <p/> Revision 1.3 2003/05/09 15:52:10
 * fz0n8j *** empty log message *** <p/> Revision 1.2 2003/04/30 11:44:01 qzd3k3
 * Merge from dev branch. <p/> Revision 1.1.2.4 2003/04/25 15:51:16 hzf3bb no
 * message <p/> Revision 1.1.2.3 2003/04/25 15:29:28 hzf3bb no message <p/>
 * Revision 1.1.2.2 2003/04/24 16:31:14 hzf3bb *** empty log message *** <p/>
 * Revision 1.1.2.1 2003/04/22 17:34:58 hzf3bb *** empty log message *** <p/>
 * Revision 1.1 2003/04/02 09:02:05 hzf3bb added new action
 */
public class InsertWitnessAction extends AbstractAction {
    private static final Logger log = CSServices.getLogger(InsertWitnessAction.class);

    private SkeletonSchedule schedule = null;

    /**
     * Empty default constructor
     */
    public InsertWitnessAction() {
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
            Enumeration en = actionEnvironment.getRequestParameterNames();
            Integer week = null;
            while (en.hasMoreElements()) {
                String name = (String) en.nextElement();
                if (name.equals("week")) {
                    week = new Integer((String) actionEnvironment.getRequestParameter(name));
                    actionEnvironment.setRequestParameter("week", week);
                }
            }
            String age = (String) actionEnvironment.getRequestParameter("age");
            String trialsession = (String) actionEnvironment.getRequestParameter("trialsession");
            String witnessname = (String) actionEnvironment.getRequestParameter("witnessname");
            String witnessstatus = (String) actionEnvironment.getRequestParameter("witnessstatus");
            String expectedarrivaltime = (String) actionEnvironment.getRequestParameter("expectedarrivaltime");
            String notes = (String) actionEnvironment.getRequestParameter("notes");
            String casetype = (String) actionEnvironment.getRequestParameter("casetype");
            String caseid = (String) actionEnvironment.getRequestParameter("caseid");
            HashMap errors = new HashMap();
            HashMap values = new HashMap();
            String errormessage = null;

            getSessions(caseid, actionEnvironment);

            checkNumber(age, errors, "age", true, 2);
            checkString(witnessname, errors, "witnessname", true, 30);
            checkSelect(witnessstatus, errors, "witnessstatus", true);
            checkTime(expectedarrivaltime, errors, "expectedarrivaltime", true);
            checkString(notes, errors, "notes", false, 255);
            checkSelect(trialsession, errors, "trialsession", true);

            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            java.util.Date expected = null;

            try {
                expected = format.parse(expectedarrivaltime);
            } catch (ParseException e) {
                // expected arrival time remains null...
                log.debug(e);
            }

            boolean errorflag = false;
            WitnessDetail wd;

            if ((errors.isEmpty()) && (expected != null)) {
                try {
                    log.debug("*************************CREATING WITNESS********************");
                    log.debug("CASE ID = " + caseid);
                    log.debug("SESSION ID = " + trialsession);
                    log.debug("WITNESS NAME = " + witnessname);
                    log.debug("STATUS = " + witnessstatus);
                    log.debug("AGE = " + age);
                    log.debug("DATE = " + new java.sql.Time(expected.getTime()));
                    log.debug("NOTES = " + notes);
                    wd = WitnessFactory.getInstance().createWitnessDetail(new Integer(caseid),
                            new Integer(trialsession), witnessname, WitnessDetail.PROSECUTION_TYPE, witnessstatus,
                            Integer.parseInt(age), new java.sql.Time(expected.getTime()), notes);
                } catch (WitnessCreationException e) {
                    e.printStackTrace();
                    e.getCause().printStackTrace();
                    errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "couldnotcreatewitness");
                } catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException e) {
                    e.printStackTrace();
                    errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "casenotfound");
                } catch (SkeletonSessionNotFoundException e) {
                    e.printStackTrace();
                    errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "sessionnotfound");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "invalidcaseid");
                }

                if (errormessage != null) {
                    actionEnvironment.setRequestParameter("errormessage", errormessage);
                    errorflag = true;
                } else {
                    if (week == null) {
                        try {
                            CaseDetail caseDetail = CaseDetailFactory.getInstance().getCaseDetail(new Integer(caseid));
                            WitnessSummary[] witnesses = WitnessFactory.getInstance().getWitnessSummarySelector()
                                    .getTodaysWitnesses(new Integer(caseid));
                            if (caseDetail != null) {
                                /*
                                 * Refactored out, directly access isUserInRole
                                 * value in the JSP if
                                 * (actionEnvironment.isUserInRole("XHBCPS")) {
                                 * actionEnvironment.setRequestParameter("CPS",
                                 * "CPS"); }
                                 */
                                actionEnvironment.setRequestParameter("caseDetail", caseDetail);
                                actionEnvironment.setRequestParameter("caseid", actionEnvironment
                                        .getRequestParameter("caseid"));
                                if (witnesses != null && witnesses.length > 0) {
                                    actionEnvironment.setRequestParameter("witnesses", witnesses);
                                }
                            }
                            actionEnvironment.setResponseName("maintainwitnessdetailscomplete");

                        } catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException e) {
                            throw new FrameworkException(e);
                        } catch (NoScheduleForCaseException e) {
                            throw new FrameworkException(e);
                        }
                    } else { // go to the case skeleton view
                        try {
                            SkeletonSchedule ss = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(
                                    new Integer(caseid));
                            WitnessSession[] ws = WitnessFactory.getInstance().getWitnessSessionSelector()
                                    .getWitnessesForWeek(new Integer(caseid), week.intValue());
                            java.util.Arrays.sort(ws);
                            actionEnvironment.setRequestParameter("caseSkeleton", ss);
                            actionEnvironment.setRequestParameter("caseid", caseid);
                            actionEnvironment.setRequestParameter("week", week);
                            actionEnvironment.setRequestParameter("witnessSessions", ws);
                            actionEnvironment.setResponseName("caseskeletonschedulecomplete");
                        } catch (ScheduleNotFoundException se) {
                            throw new FrameworkException(se);
                        }
                    }
                }

            } else {
                errorflag = true;
            }

            if (errorflag) {
                values.put("age", age);
                values.put("trialsession", trialsession);
                values.put("witnessname", witnessname);
                values.put("witnessstatus", witnessstatus);
                values.put("expectedarrivaltime", expectedarrivaltime);
                values.put("notes", notes);

                actionEnvironment.setRequestParameter("caseid", caseid);
                actionEnvironment.setRequestParameter("values", values);
                actionEnvironment.setRequestParameter("errors", errors);
                actionEnvironment.setRequestParameter("casetype", casetype);
                actionEnvironment.setResponseName("addwitnesscomplete");
            }
        }
    }

    private void getSessions(String caseid, ActionEnvironment actionEnvironment) throws FrameworkException {

        try {
            schedule = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(new Integer(caseid));
        } catch (Exception e) {
            throw new FrameworkException(e);
        }

        TrialSession[] session = null;
        session = schedule.getTrialSessions();

        if ((session != null) && (session.length > 0)) {
            actionEnvironment.setRequestParameter("ts", session);
        }
    }

    private void checkTime(String str, HashMap errors, String errorName, boolean required) {
        if (str.length() > 0) {
            try {
                if (str.indexOf(':') > 0) {
                    Integer hours = new Integer(str.substring(0, str.indexOf(':')));
                    Integer minutes = new Integer(str.substring(str.indexOf(':') + 1));

                    if (hours.intValue() >= 0 && hours.intValue() < 24 && minutes.intValue() >= 0
                            && minutes.intValue() < 60) {

                    } else {
                        errors.put(errorName, "formaterror");
                    }
                } else {
                    errors.put(errorName, "formaterror");
                }
            } catch (Exception e) {
                errors.put(errorName, "formaterror");
            }
        } else {
            if (required) {
                errors.put(errorName, "requiredfield");
            }
        }
    }

    private void checkSelect(String str, HashMap errors, String errorName, boolean required) {
        if ((str.equals("-1") && (required))) {
            errors.put(errorName, "requiredfield");
        }
    }

    private void checkString(String str, HashMap errors, String errorName, boolean required, int maxLength) {
        if (str.length() > 0) {
            if (str.length() > maxLength) {
                errors.put(errorName, "toolong");
            }
        } else {
            if (required) {
                errors.put(errorName, "requiredfield");
            }
        }
    }

    private void checkNumber(String str, HashMap errors, String errorName, boolean required, int maxLength) {
        int i = 0;
        if (str.length() > 0) {
            if (str.length() <= maxLength) {
                try {
                    i = Integer.parseInt(str);
                    if (i < 0) {
                        errors.put(errorName, "formaterror");
                    }
                } catch (NumberFormatException nfe) {
                    errors.put(errorName, "formaterror");
                }
            } else {
                errors.put(errorName, "toolong");
            }

        } else {
            if (required) {
                errors.put(errorName, "requiredfield");
            }
        }
    }
}
