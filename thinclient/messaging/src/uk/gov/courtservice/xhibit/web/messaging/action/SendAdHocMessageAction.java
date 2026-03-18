package uk.gov.courtservice.xhibit.web.messaging.action;

import uk.gov.courtservice.xhibit.business.services.messaging.MessagingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
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
public class SendAdHocMessageAction extends AbstractAction {

    protected void internalPerformAction(ActionEnvironment actionEnvironment)
            throws uk.gov.courtservice.xhibit.web.framework.util.FrameworkException {
        MessagingControllerBeanBusinessDelegate bean = MessagingControllerBeanBusinessDelegate.DelegateFactory
                .getInstance();
        String[] deviceTypes = bean.getDeviceTypes();
        actionEnvironment.setRequestParameter("adHocMessageBean", new AdHocMessageBean(deviceTypes[0], "+", ""));
        actionEnvironment.setRequestParameter("adHocdeviceTypes", deviceTypes);
        actionEnvironment.setResponseName("sendadhocmessagecomplete");
    }
}