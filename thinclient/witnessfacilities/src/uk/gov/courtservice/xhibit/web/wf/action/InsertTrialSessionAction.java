package uk.gov.courtservice.xhibit.web.wf.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CouldNotCreateSessionException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonDayHasDateException;
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
 * Title: Insert Trial Session Action
 * </p>
 * <p>
 * Description: The action for inserting a trial session
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * @author David Duncan (2003)
 * @version $Id: InsertTrialSessionAction.java,v 1.3 2005/04/27 08:26:58 bzjrnl
 *          Exp $
 */
public class InsertTrialSessionAction extends AbstractAction {
    /**
     * Empty default constructor
     */
    public InsertTrialSessionAction() {
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

            String trialday = (String) actionEnvironment.getRequestParameter("trialday");
            String trialdate = (String) actionEnvironment.getRequestParameter("trialdate");
            String trialsession = (String) actionEnvironment.getRequestParameter("trialsession");

            String pageRequestFrom = (String) actionEnvironment.getRequestParameter("pagesource");
            actionEnvironment.setRequestParameter("pagesource", pageRequestFrom);

            HashMap errors = new HashMap();
            HashMap values = new HashMap();
            String adderror = null;

            String pattern = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                    .getRemoteLocale(), "dateformat");
            String caseID = (String) actionEnvironment.getRequestParameter("caseid");
            actionEnvironment.setRequestParameter("caseid", caseID);

            checkNumber(trialday, errors, "trialday", true);
            Date trialDate = checkDate(trialdate, errors, "trialdate", false, pattern);
            checkSelect(trialsession, errors, "trialsession", true);

            boolean errorflag = false;

            if (errors.isEmpty()) {

                SkeletonSchedule schedule = null;

                try {
                    schedule = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(new Integer(caseID));
                } catch (NumberFormatException e) {
                    adderror = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "invalidcaseid");
                } catch (ScheduleNotFoundException e) {
                    adderror = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "noschedule");
                }

                TrialSession session = null;
                try {
                    session = schedule.createTrialSession(new Integer(trialday), trialsession, trialDate);
                } catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException e) {
                    adderror = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "couldnotcreate");
                } catch (SkeletonDayHasDateException e) {
                    adderror = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "hasdate");
                } catch (CouldNotCreateSessionException e) {
                    adderror = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "couldnotcreate");
                } catch (NumberFormatException e) {
                    adderror = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                            .getRemoteLocale(), "invalidtrialday");
                } catch (CSUnrecoverableException e) {
                    if (e.getCause() instanceof uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException) {
                        adderror = "Trial Session for day " + trialday + " at " + trialsession
                                + " already exists for this schedule.";
                    } else if (e.getCause() instanceof SkeletonDayHasDateException) {
                        adderror = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                                .getRemoteLocale(), "hasdate");
                    } else if (e.getCause() instanceof CouldNotCreateSessionException) {
                        adderror = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                                .getRemoteLocale(), "couldnotcreate");
                    } else if (e.getCause() instanceof NumberFormatException) {
                        adderror = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                                .getRemoteLocale(), "invalidtrialday");
                    }
                }

                if (adderror != null) {
                    actionEnvironment.setRequestParameter("adderror", adderror);
                    errorflag = true;
                } else {

                    TrialSession[] trialsessions = schedule.getTrialSessions();

                    if (trialsessions != null && trialsessions.length > 0) {
                        actionEnvironment.setRequestParameter("trialsessions", trialsessions);
                    }
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
                actionEnvironment.setResponseName("addtrialsessioncomplete");
            }
        }

    }

    private Date checkDate(String str, HashMap errors, String errorName, boolean required, String pattern) {
        Date date = null;
        if (str.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            // set lenient to false to stop the user entering dates in the
            // correct format but are invalid
            // i.e. 33/33/33
            sdf.setLenient(false);
            try {
                date = sdf.parse(str);
            } catch (ParseException e) {
                errors.put(errorName, "formaterror");
            }
        } else {
            if (required) {
                errors.put(errorName, "requiredfield");
            }
        }

        return date;
    }

    private void checkSelect(String str, HashMap errors, String errorName, boolean required) {
        if ((str.equals("-") && (required))) {
            errors.put(errorName, "requiredfield");
        }
    }

    private void checkNumber(String str, HashMap errors, String errorName, boolean required) {
        if (str.length() > 0) {
            try {
                Integer.parseInt(str);
            } catch (NumberFormatException nfe) {
                errors.put(errorName, "formaterror");
                return;
            }

            if (Integer.valueOf(str).intValue() <= 0) {
                errors.put(errorName, "invalidentry");
                return;
            }

        } else {
            if (required) {
                errors.put(errorName, "requiredfield");
            }
        }
    }
}
