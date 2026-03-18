package uk.gov.courtservice.xhibit.web.messaging.action;

import java.util.Arrays;

import uk.gov.courtservice.xhibit.business.exceptions.messaging.InvalidDeviceException;
import uk.gov.courtservice.xhibit.business.exceptions.messaging.MessagingException;
import uk.gov.courtservice.xhibit.business.services.messaging.MessagingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.business.vos.services.messaging.AdHocMessageValue;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.messaging.IMSessionInfo;
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
 * A modified version of the normal adhoc message sent
 * 
 * @author Edward Cawley
 * @version 1.0
 */

public class AdHocWitnessMessageSent extends AbstractAction {
    protected void internalPerformAction(ActionEnvironment actionEnvironment)
            throws uk.gov.courtservice.xhibit.web.framework.util.FrameworkException {
        // get the list of valid device types.
        MessagingControllerBeanBusinessDelegate mcDelegate = MessagingControllerBeanBusinessDelegate.DelegateFactory
                .getInstance();
        String[] deviceTypes = mcDelegate.getDeviceTypes();

        // Get sorted list of device types to support binary search.
        String[] sortedDeviceTypes = new String[deviceTypes.length];
        System.arraycopy(deviceTypes, 0, sortedDeviceTypes, 0, deviceTypes.length);
        Arrays.sort(sortedDeviceTypes);

        String witnessid = (String) actionEnvironment.getRequestParameter("witnessid");
        String device = null;
        String number = null;
        try {
            WitnessDetail wd = WitnessFactory.getInstance().getWitnessDetail(new Integer(witnessid));
            if (wd.getPagerNetwork() != null) {
                device = wd.getPagerNetwork();
                if (wd.getPagerNumber() != null) {
                    number = wd.getPagerNumber();
                }
            } else {
                device = "SMS";
                if (wd.getMobileNumber() != null) {
                    number = wd.getMobileNumber();
                }
            }

        } catch (WitnessNotFoundException e) {
            throw new FrameworkException(e);
        }
        if (device == null || number == null) {
            throw new FrameworkException("witness.devicenotfound",
                    "Could not find appropriate device and number details for witness!");
        }

        AdHocMessageBean inputValues = new AdHocMessageBean(device, number, (String) actionEnvironment
                .getRequestParameter("message"));

        if (Arrays.binarySearch( // Device type not in the valid device
                // types
                sortedDeviceTypes, (inputValues.getDeviceType().getValue())) < 0) {
            inputValues.getDeviceType().setErrorValue(deviceTypes[0]);// Set
            // to a
            // valid
            // one.
            inputValues.getDeviceType().setErrorMessageKey("adhocdevicefield.deviceNotValid");
        } else if (inputValues.isValid()) // Valid to send.
        {
            // Setup the value object to send.
            IMSessionInfo imsi = (IMSessionInfo) actionEnvironment
                    .getSessionParameter(IMSessionInfo.INSTANT_MESSAGE_SESSION);
            AdHocMessageValue av = new AdHocMessageValue(inputValues.getMessage().getValue(), imsi
                    .getFormattedLocation(), inputValues.getDeviceType().getValue(), inputValues.getNumber().getValue());

            try {
                // Send the message.
                mcDelegate.sendMessage(av);

                // Reset the value object to present a new empty number.
                inputValues = new AdHocMessageBean(deviceTypes[0], "+", "");// Reset
                // to
                // clean
                // bean
            } catch (InvalidDeviceException ex) {
                inputValues.getDeviceType().setErrorValue(deviceTypes[0]);// Set
                // to a
                // valid
                // one.
                inputValues.getDeviceType().setErrorMessageKey("adhocdevicefield.deviceNotValid");
            } catch (MessagingException ex) {
                throw new FrameworkException(ex);
            }
            actionEnvironment.setResponseName("adhocmessagesentcomplete");
        } else {
            actionEnvironment.setRequestParameter("adHocMessageBean", inputValues);
            actionEnvironment.setRequestParameter("adHocdeviceTypes", deviceTypes);
            actionEnvironment.setResponseName("sendadhocmessagecomplete");
        }
    }
}