package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Revision: 1.7 $
 */
public class TodaysScheduleAction extends SynchXAction {
    private static final Logger log = CSServices.getLogger(TodaysScheduleAction.class);

    private XhibitApplicationController xac;

    private TodaysScheduleController tsd;

    public TodaysScheduleAction() {
        populateFromBundle("OpenTodaysSchedule");
    }

    /**
     * @see uk.gov.courtservice.xhibit.client.util.SynchXAction
     *      #synchActionPerformed(java.awt.event.ActionEvent)
     */
    public void synchActionPerformed(ActionEvent e) {
        log.debug("Starting sync process");
        if (xac != null) {
            tsd = new TodaysScheduleController(xac);
        }
    }

    /**
     * @see uk.gov.courtservice.xhibit.client.util.SynchXAction
     *      #preSynchActionPerformed(java.awt.event.ActionEvent)
     */
    public void preSynchActionPerformed(ActionEvent parm1) throws Exception {
        xac = (XhibitApplicationController) getController();
    }

    /**
     * @see uk.gov.courtservice.xhibit.client.util.SynchXAction
     *      #postSynchActionPerformed(java.awt.event.ActionEvent)
     */
    public void postSynchActionPerformed(ActionEvent parm1) throws Exception {
        try {
            if ((xac != null) && (tsd != null)) {
                xac.open(tsd);
            }
        } finally {
            // Perf PR: 56516 - need to ensure that all reference all
            // removed,
            // to prevent memory "leaks"...
            xac = null;
            tsd = null;
        }
    }
}
