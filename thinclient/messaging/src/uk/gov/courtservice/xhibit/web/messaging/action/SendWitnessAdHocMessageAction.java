package uk.gov.courtservice.xhibit.web.messaging.action;

import uk.gov.courtservice.xhibit.business.services.messaging.MessagingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.messaging.bean.AdHocMessageBean;

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
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class SendWitnessAdHocMessageAction extends AbstractAction {

    protected void internalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        try {
            String witnessid = (String) actionEnvironment.getRequestParameter("witnessid");
            WitnessDetail wd = WitnessFactory.getInstance().getWitnessDetail(new Integer(witnessid));
            if (wd.getMobileNumber() == null && wd.getPagerNumber() == null) {
                throw new FrameworkException("witness.nopager",
                        "The witness has no mobile number or pager number associated with them");
            }
            MessagingControllerBeanBusinessDelegate bean = MessagingControllerBeanBusinessDelegate.DelegateFactory
                    .getInstance();
            String[] deviceTypes = bean.getDeviceTypes();
            actionEnvironment.setRequestParameter("witnessname", wd.getName());
            actionEnvironment.setRequestParameter("witnessid", witnessid);
            actionEnvironment.setRequestParameter("adHocMessageBean", new AdHocMessageBean(deviceTypes[0], "+", ""));
            actionEnvironment.setRequestParameter("adHocdeviceTypes", deviceTypes);
            actionEnvironment.setResponseName("sendwitnessadhocmessagecomplete");
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
    }
}