package uk.gov.courtservice.xhibit.client.actions.hearingrecord;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.hearingrecord.HearingDetails;
import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

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

public class HearingDetailsRecalculateAction extends XAction {
    private static final Logger log = CSServices.getLogger(HearingDetailsRecalculateAction.class);

    HearingRecordModel model;

    public HearingDetailsRecalculateAction() {
        this.setName(ResourceBundleHelper.getResource(XhibitBundles.HearingRecord, "recalculate"));
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        log.debug("HearingDetailsRecalculateAction");
        model = ((HearingDetails) this.getCaller()).getModel();

        getDelegate().reCalculateHearingDuration(model.getHearingRecordUpdateVal().getDefHearingRecordValue(),
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

        DefHearingRecordValue defHearingRecord = getDelegate().getDefHearingRecord(model.getHearingId(),
                model.getDefendantId());

        model.getHearingRecordUpdateVal().setDefHearingRecordValue(defHearingRecord);

        model.getHearingDetailsPanel().setDuration(defHearingRecord.getLastCalculatedDuration());
    }

    private HearingScheduleControllerBeanBusinessDelegate getDelegate() {
        return XhibitDelegateHelper.getHearingDelegate();
    }

}