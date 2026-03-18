package uk.gov.courtservice.xhibit.client.skeletonschedule.util;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.client.skeletonschedule.SkeletonSchedulePanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Controller Util
 * </p>
 * <p>
 * Description: Util methods for manipulating the xac controller
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment
 * @version 1.0
 */

public final class ControllerUtil {
    private static final String INVALID_SCHEDULE_KEY = "skeletonschedule.skeletonpanel.invalidschedule";

    private static final String INVALID_CASE_KEY = "skeletonschedule.skeletonpanel.invalidcase";

    private static final String INVALID_PANEL_KEY = "skeletonschedule.witnessdialog.invalidpanel";

    private ControllerUtil() {
        // Stop unnecesary creation of util class
    }

    public static Integer getCaseId(XhibitApplicationController xac) {
        return xac.getApplicationCaseModel().getCaseId();
    }

    public static CaseDetail getCaseDetail(XhibitApplicationController xac) throws CSRecoverableException {
        Integer caseId = getCaseId(xac);
        try {
            return CaseDetailFactory.getInstance().getCaseDetail(caseId);
        } catch (CaseNotFoundException cnfe) {
            // push back into CaseNotFoundException
            throw new CSRecoverableException(INVALID_CASE_KEY, new Object[] { caseId }, "Invalid case " + caseId + ".",
                    cnfe);
        }
    }

    public static SkeletonSchedule getSkeletonSchedule(XhibitApplicationController xac) throws CSRecoverableException {
        Integer caseId = getCaseId(xac);
        try {
            return SkeletonScheduleFactory.getInstance().getSkeletonSchedule(caseId);
        } catch (ScheduleNotFoundException snfe) {
            // push back into ScheduleNotFoundException
            throw new CSRecoverableException(INVALID_SCHEDULE_KEY, new Object[] { caseId },
                    "Could not find schedule for case " + caseId + ".", snfe);
        }

    }

    public static SkeletonSchedulePanel getSkeletonSchedulePanel(XhibitApplicationController xac)
            throws CSRecoverableException {
        XPanel panel = xac.getBodyPanel();
        try {
            return (SkeletonSchedulePanel) panel;
        } catch (ClassCastException cce) {
            String panelClassName = panel.getClass().getName();
            throw new CSRecoverableException(INVALID_PANEL_KEY, new Object[] { panelClassName }, "Invalid panel "
                    + panelClassName + ".");
        }
    }

}
