package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoScheduleForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
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
 * Title: Save Edit Witness Action
 * </p>
 * <p>
 * Description: The action for saving an edited witness
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * @author David Duncan (2003)
 * @version $Revision: 1.7 $
 */
public class SaveEditWitnessAction extends AbstractAction {
    private static final Logger log = CSServices.getLogger(SaveEditWitnessAction.class);

    private static final String MAINTAIN_WITNESS_PAGE = "maintainwitnessdetails";

    private static final String MAINTAIN_WITNESS_ALL_PAGE = "maintainwitnessdetailsall";

    private static final String MAINTAIN_WITNESS_ALL_FUTURE = "maintainwitnessdetailsfuture";

    /**
     * Empty default constructor
     */
    public SaveEditWitnessAction() {
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
        log.debug("SaveEditWitnessAction");
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

            TrialSession trialSession;

            while (en.hasMoreElements()) {
                String name = (String) en.nextElement();
                if (name.equals("week")) {
                    week = new Integer((String) actionEnvironment.getRequestParameter(name));
                    actionEnvironment.setRequestParameter("week", week);
                }
            }

            Calendar expectedCalendar = Calendar.getInstance();
            String id = (String) actionEnvironment.getRequestParameter("id");
            String age = (String) actionEnvironment.getRequestParameter("witnessage");
            String trialsession = (String) actionEnvironment.getRequestParameter("trialsession");
            String witnessname = (String) actionEnvironment.getRequestParameter("witnessname");
            String witnessstatus = (String) actionEnvironment.getRequestParameter("witnessstatus");
            String expected = (String) actionEnvironment.getRequestParameter("expected");
            String notes = (String) actionEnvironment.getRequestParameter("notes");
            String casetype = (String) actionEnvironment.getRequestParameter("casetype");
            String caseid = (String) actionEnvironment.getRequestParameter("caseid");

            HashMap errors = new HashMap();
            HashMap values = new HashMap();
            String errormessage = null;

            checkSelect(trialsession, errors, "trialsession", true);
            checkString(witnessname, errors, "witnessname", true, 30);
            checkNumber(age, errors, "age", true, 2);
            checkSelect(witnessstatus, errors, "witnessstatus", true);
            checkTime(expected, errors, "expected", true, expectedCalendar);
            checkString(notes, errors, "notes", false, 255);

            try {
                trialSession = WitnessFactory.getInstance().getWitnessSession(new Integer(id)).getTrialSession();
            } catch (NumberFormatException e) {
                throw new FrameworkException(e);
            } catch (WitnessNotFoundException e) {
                throw new FrameworkException(e);
            }

            getSessions(caseid, actionEnvironment);

            boolean errorflag = false;

            if (errors.isEmpty()) {
                try {
                    log.debug("Errors empty");
                    TrialSession ts = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(new Integer(caseid))
                            .getTrialSession(new Integer(trialsession));
                    WitnessSession ws = WitnessFactory.getInstance().getWitnessSession(new Integer(id));
                    // set up the default age for the witness
                    ws.setAge(-1);
                    // update if value added
                    if (age.length() > 0) {
                        ws.setAge(Integer.parseInt(age));
                    }
                    ws.setExpected(new java.sql.Time(expectedCalendar.getTime().getTime()));
                    ws.setName(witnessname);
                    ws.setNotes(notes);
                    ws.setStatus(witnessstatus);
                    ws.setTrialSession(ts);
                    log.debug("Updating witness");
                    ws.update();
                } catch (WitnessModificationException e) {
                    errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "couldnotcreatewitness");
                } catch (NumberFormatException e) {
                    errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "invalidcaseid");
                } catch (Exception e) {
                    errormessage = "Error while updating witness details";
                }

                if (errormessage != null) {
                    actionEnvironment.setRequestParameter("errormessage", errormessage);
                    errorflag = true;
                } else {
                    // page been called from Maintain Witness Details
                    // [today/future/all]
                    if (week == null) {
                        try {
                            CaseDetail caseDetail = CaseDetailFactory.getInstance().getCaseDetail(new Integer(caseid));
                            String pageRequestFrom = (String) actionEnvironment.getRequestParameter("pagesource");
                            WitnessSummary[] witnesses = null;

                            // toggle on which witness list to return
                            // depending on calling page.
                            if (pageRequestFrom.equals(MAINTAIN_WITNESS_PAGE)) {
                                witnesses = WitnessFactory.getInstance().getWitnessSummarySelector()
                                        .getTodaysWitnesses(new Integer(caseid));
                            } else if (pageRequestFrom.equals(MAINTAIN_WITNESS_ALL_PAGE)) {
                                witnesses = WitnessFactory.getInstance().getWitnessSummarySelector().getAllWitnesses(
                                        new Integer(caseid));
                            } else if (pageRequestFrom.equals(MAINTAIN_WITNESS_ALL_FUTURE)) {
                                witnesses = WitnessFactory.getInstance().getWitnessSummarySelector()
                                        .getFutureWitnesses(new Integer(caseid));
                            }

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

                            // return to intial calling page
                            actionEnvironment.setResponseName(pageRequestFrom + "complete");

                        } catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException e) {
                            throw new FrameworkException(e);
                        } catch (NoScheduleForCaseException e) {
                            throw new FrameworkException(e);
                        }
                    } else // page been called from schedule for trial
                    {
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
                values.put("trialsession", trialSession);
                values.put("witnessname", witnessname);
                values.put("witnessstatus", witnessstatus);
                values.put("expected", expected);
                values.put("notes", notes);

                actionEnvironment.setRequestParameter("caseid", caseid);
                actionEnvironment.setRequestParameter("values", values);
                actionEnvironment.setRequestParameter("errors", errors);
                actionEnvironment.setRequestParameter("casetype", casetype);
                try {
                    WitnessDetail witnessDetial = WitnessFactory.getInstance().getWitnessDetail(new Integer(id));
                    WitnessSession ws = WitnessFactory.getInstance().getWitnessSession(new Integer(id));
                    // need to reset the page source to handle event of
                    // multiple presses of save button when errors on the
                    // page
                    if (week == null) {
                        // get the existing page source from the page and reset
                        // it for the same page
                        String pageRequestFrom = (String) actionEnvironment.getRequestParameter("pagesource");
                        actionEnvironment.setRequestParameter("pagesource", pageRequestFrom);
                    }
                    actionEnvironment.setRequestParameter("witnessdetail", witnessDetial);
                    actionEnvironment.setRequestParameter("casedetail", CaseDetailFactory.getInstance().getCaseDetail(
                            new Integer(caseid)));
                    actionEnvironment.setRequestParameter("trialsession", ws.getTrialSession());
                } catch (Exception e) {
                    throw new FrameworkException(e);
                }
                actionEnvironment.setResponseName("editwitnesscomplete");
            }
        }

    }

    private void getSessions(String caseid, ActionEnvironment actionEnvironment) throws FrameworkException {
        TrialSession[] ts;
        try {
            ts = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(new Integer(caseid)).getTrialSessions();
        } catch (ScheduleNotFoundException e) {
            throw new FrameworkException(e);
        } catch (NumberFormatException e) {
            throw new FrameworkException(e);
        }

        actionEnvironment.setRequestParameter("ts", ts);
    }

    private void checkTime(String str, HashMap errors, String errorName, boolean required, Calendar toSet) {
        if (str.length() > 0) {
            try {
                if (str.indexOf(':') > 0) {
                    Integer hours = new Integer(str.substring(0, str.indexOf(':')));
                    Integer minutes = new Integer(str.substring(str.indexOf(':') + 1));

                    if (hours.intValue() >= 0 && hours.intValue() < 24 && minutes.intValue() >= 0
                            && minutes.intValue() < 60) {
                        toSet.set(Calendar.HOUR_OF_DAY, hours.intValue());
                        toSet.set(Calendar.MINUTE, minutes.intValue());
                        toSet.set(Calendar.SECOND, 0);
                        toSet.set(Calendar.MILLISECOND, 0);
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
