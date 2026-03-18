package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.reference.WitnessReferenceDataFactory;
import uk.gov.courtservice.xhibit.business.services.witness.reference.interfaces.WitnessReferenceData;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: WitnessSignInAction
 * </p>
 * <p>
 * Description: The action for signing in Witnesses. <p/> Copyright: Copyright
 * (c) 2003 Company: EDS <p/> Author: Edward Cawley, Xdevelopment LLP (2003)
 */
public class WitnessSignInAction extends AbstractAction {

    private static final String PROSECUTION = "Prosecution";

    private static final Logger log = CSServices.getLogger(WitnessSignInAction.class);

    /**
     * Empty default constructor
     */
    public WitnessSignInAction() {
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
        log.debug("WitnessSignInAction");
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("loggedout");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }

            CaseDetail caseDetail;
            WitnessDetail witnessDetail;
            TrialSession trialSession;

            String witnessid = (String) actionEnvironment.getRequestParameter("id");
            String caseid = (String) actionEnvironment.getRequestParameter("caseid");

            try {
                caseDetail = CaseDetailFactory.getInstance().getCaseDetail(new Integer(caseid));
                witnessDetail = WitnessFactory.getInstance().getWitnessDetail(new Integer(witnessid));
                trialSession = WitnessFactory.getInstance().getWitnessSession(new Integer(witnessid)).getTrialSession();
            } catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException e) {
                throw new FrameworkException(e);
            } catch (NumberFormatException e) {
                throw new FrameworkException(e);
            } catch (WitnessNotFoundException e) {
                throw new FrameworkException(e);
            }
            if ((caseDetail != null) && (caseDetail.getCaseType() != null)) {
                actionEnvironment.setRequestParameter("casedetail", caseDetail);
            }

            // perform same check as Save on edit page to ensure that values
            // are ok before signin in the witness.
            Calendar expectedCalendar = Calendar.getInstance();
            String age = new Integer(witnessDetail.getAge()).toString();
            TrialSession trialsession = trialSession;
            String witnessname = witnessDetail.getName();
            String witnessstatus = witnessDetail.getStatus();
            String expected = witnessDetail.getExpected().toString();
            String notes = witnessDetail.getNotes();
            String casetype = witnessDetail.getType();

            HashMap errors = new HashMap();
            HashMap values = new HashMap();

            checkSelect(trialsession.getSessionType(), errors, "trialsession", true);
            checkString(witnessname, errors, "witnessname", true, 30);
            // checkNumber(age, errors, "age", true, 2);
            if (witnessDetail.getType().equals(PROSECUTION)) {
                checkSelect(witnessstatus, errors, "witnessstatus", true);
            }
            checkTime(expected, errors, "expected", true, expectedCalendar);
            checkString(notes, errors, "notes", false, 255);

            // getSessions(caseid, actionEnvironment);

            boolean errorflag = false;

            if (errors.isEmpty()) {
                // no errors go to the witness sign in page
                try {
                    WitnessDetail wd = WitnessFactory.getInstance().getWitnessDetail(new Integer(witnessid));
                    WitnessReferenceData wrd = WitnessReferenceDataFactory.getWitnessReferenceData();
                    if (wd.getArrived() == null) {
                        wd.setArrived(new Date());
                    }
                    actionEnvironment.setRequestParameter("witness", wd);
                    actionEnvironment.setRequestParameter("witnessref", wrd);
                    actionEnvironment.setRequestParameter("id", witnessid); // push
                    // case
                    // id
                    // for
                    // cancel
                    // . .
                    // .

                } catch (Exception e) {
                    throw new FrameworkException("xhibit.error.unexpected", "Error occurred getting witness", e);
                }
                actionEnvironment.setRequestParameter("caseid", actionEnvironment.getRequestParameter("caseid")); // push
                // case
                // id
                // for
                // cancel
                // . .
                // .
                actionEnvironment.setRequestParameter("id", witnessid); // push
                // case
                // id
                // for
                // cancel
                // . . .
                actionEnvironment.setResponseName("witnesssignincomplete");
                actionEnvironment.setRequestParameter("pagesource", "witnesssignin");
            }
            // errors on the page
            else {
                errorflag = true; // set the error flag
            }

            // if there are error reload the Edit Witness Screen
            if (errorflag) {
                values.put("age", age);
                values.put("trialsession", trialsession);
                values.put("witnessname", witnessname);
                values.put("witnessstatus", witnessstatus);
                values.put("expected", expected);
                values.put("notes", notes);

                actionEnvironment.setRequestParameter("caseid", caseid);
                actionEnvironment.setRequestParameter("witnessid", witnessid);
                actionEnvironment.setRequestParameter("values", values);
                actionEnvironment.setRequestParameter("errors", errors);
                actionEnvironment.setRequestParameter("casetype", casetype);
                actionEnvironment.setRequestParameter("id", witnessid); // push
                // case
                // id
                // for
                // cancel
                // . . .
                actionEnvironment.setRequestParameter("pagesource", "editwitness");

                try {
                    WitnessDetail wd = WitnessFactory.getInstance().getWitnessDetail(new Integer(witnessid));
                    WitnessSession ws = WitnessFactory.getInstance().getWitnessSession(new Integer(witnessid));
                    actionEnvironment.setRequestParameter("witnessdetail", wd);
                    actionEnvironment.setRequestParameter("casedetail", CaseDetailFactory.getInstance().getCaseDetail(
                            new Integer(caseid)));
                    actionEnvironment.setRequestParameter("trialsession", ws.getTrialSession());
                    getSessions(caseid, actionEnvironment);
                    actionEnvironment.setRequestParameter("errorpagesource", "witnesssignin");

                } catch (Exception e) {
                    throw new FrameworkException(e);
                }
                actionEnvironment.setResponseName("editwitnesscomplete");
            }
        }
    }

    private void checkTime(String str, HashMap errors, String errorName, boolean required, Calendar toSet) {
        if (null != str && str.length() > 0) {
            try {
                int startIndex = str.indexOf(':');
                if (startIndex > 0) {
                    Integer hours = new Integer(str.substring(0, startIndex));
                    Integer minutes = new Integer(str.substring(startIndex + 1, str.lastIndexOf(':')));

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
        if (null == str || (str.equals("-1") && (required))) {
            errors.put(errorName, "requiredfield");
        }
    }

    private void checkString(String str, HashMap errors, String errorName, boolean required, int maxLength) {
        if (null != str && str.length() > 0) {
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
}
