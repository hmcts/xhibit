package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Frame;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.courtlog.directions.caze.TimeEstimatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva
 * @version 1.0
 */

public class TimeEstimateDialog extends XDialog {
    private HearingRecordModel model;

    private TimeEstimatePanel bodyPanel;

    private static final Logger log = CSServices.getLogger(TimeEstimateDialog.class);

    public TimeEstimateDialog(Frame frame, HearingRecordModel model) {
        super(frame, "", true);
        this.model = model;

        try {
            bodyPanel = new TimeEstimatePanel(model.getHearingRecordUpdateVal().getDirectionsForCaseValue());
            super.addBodyPanel(this.bodyPanel);
            super.pack();
        } catch (Exception e) {
            log.fatal(e, e);
            throw new CSUnrecoverableException(e);

        }

    }

    public TimeEstimatePanel getBodyPanel() {
        return this.bodyPanel;
    }

}