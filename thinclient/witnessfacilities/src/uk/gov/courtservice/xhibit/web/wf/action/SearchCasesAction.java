package uk.gov.courtservice.xhibit.web.wf.action;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoScheduleForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Default Action
 * </p>
 * <p>
 * Description: The default action for the application. <p/>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley
 */
public class SearchCasesAction extends TerminalCookieAction {

    private static final Logger log = CSServices.getLogger(SearchCasesAction.class);

    // delivery status of unissued skeleton schedules
    private static final String NOTREADY = "NOTREADY";

    /**
     * Empty default constructor
     */
    public SearchCasesAction() {
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
        if (actionEnvironment.getRequestParameterNames().hasMoreElements()) {
            String search = (String) actionEnvironment.getRequestParameter("search");
            actionEnvironment.setRequestParameter("search", search);

            // get the users courtId
            Integer courtIdValue = getCourtId(actionEnvironment);
            try {
                Integer.parseInt(search.substring(1));
                String type = search.substring(0, 1);
                CaseDetail caseDetail = CaseDetailFactory.getInstance().getCaseDetailsByCaseRefAndCourtId(search,
                        courtIdValue);

                Integer caseId = caseDetail.getId();

                WitnessControllerBeanBusinessDelegate delegate = WitnessControllerBeanBusinessDelegate.DelegateFactory
                        .getInstance();
                boolean validcase = delegate.isCurrentCourtCase(caseId, courtIdValue);
                // test to see if the case is at the current court.
                if (validcase) {
                    try {
                        SkeletonSchedule ss = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(caseId);
                        if (ss == null) // there is no scedule for this case . .
                        // .
                        {
                            makeSchedule(actionEnvironment, caseId);
                        }
                    } catch (ScheduleNotFoundException e) { // there is no
                        // scedule for
                        // this case . .
                        // .

                        makeSchedule(actionEnvironment, caseId);
                    }

                    WitnessDetail[] witnesses = WitnessFactory.getInstance().getWitnessDetailSelector()
                            .getAllWitnessDetails(caseDetail.getId());
                    if (caseDetail != null) {
                        if (type.equalsIgnoreCase(caseDetail.getCaseType())) {
                            if (!hasIssuedSkeletonSchedule(caseId)) {
                                if (actionEnvironment.isUserInRole("XHBCPS")) {
                                    actionEnvironment.setRequestParameter("CPS", "CPS");
                                }
                                actionEnvironment.setRequestParameter("caseDetail", caseDetail);
                                actionEnvironment.setRequestParameter("caseid", caseDetail.getId());
                                if (witnesses != null && witnesses.length > 0) {
                                    actionEnvironment.setRequestParameter("witnesses", witnesses);
                                }
                                actionEnvironment.setResponseName("maintainwitnessdetailscomplete");
                            } else {
                                log.debug("SKELETON EXISTS THAT HAS NOT BEEN ISSUED YET");
                                actionEnvironment.setRequestParameter("error", "invalidcase");
                                actionEnvironment.setResponseName("searchcasescomplete");
                            }
                        } else {
                            actionEnvironment.setRequestParameter("error", "badtype");
                            actionEnvironment.setResponseName("searchcasescomplete");
                        }
                    } else {
                        actionEnvironment.setRequestParameter("error", "notfound");
                        actionEnvironment.setResponseName("searchcasescomplete");
                    }
                } else {
                    // case is not at the current court
                    actionEnvironment.setRequestParameter("error", "notcurrentcourtcase");
                    actionEnvironment.setResponseName("searchcasescomplete");
                }

            } catch (NoScheduleForCaseException e) {
                throw new FrameworkException(e);
            } catch (NumberFormatException e) {
                actionEnvironment.setRequestParameter("error", "formaterror");
                actionEnvironment.setResponseName("searchcasescomplete");
            } catch (Exception e) {
                actionEnvironment.setRequestParameter("error", "notfound");
                actionEnvironment.setResponseName("searchcasescomplete");
            }
        } else {
            actionEnvironment.setResponseName("searchcasescomplete");
        }
    }

    public void makeSchedule(ActionEnvironment actionEnvironment, Integer caseid) throws FrameworkException {
        if (actionEnvironment.isUserInRole("XHBCPS")) // if the user is cps
        // create one . . . .
        {
            try {
                SkeletonScheduleFactory.getInstance().createSkeletonSchedule(caseid);
            } catch (ScheduleModificationException sme) {
                throw new FrameworkException("witness.createschedule", "Cannot create scedule for case!", sme);
            }
        }
    }

    /**
     * Check to see if the case has an issued skeleton schedule This should not
     * be available when searching for cases
     * 
     * @param caseId
     * @return
     */
    private boolean hasIssuedSkeletonSchedule(Integer caseId) throws ScheduleNotFoundException {
        boolean hasIssued = false;
        SkeletonSchedule skeleton = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(caseId);
        String delStatus = skeleton.getDeliveryStatus();
        boolean deliverable = skeleton.isDeliverable();
        if (deliverable && !(delStatus == null) && delStatus.equals(NOTREADY)) {
            hasIssued = true;
        }
        log.debug("Return Value :: hasIssuedSkeletonSchedule :: " + hasIssued);
        return hasIssued;
    }
}