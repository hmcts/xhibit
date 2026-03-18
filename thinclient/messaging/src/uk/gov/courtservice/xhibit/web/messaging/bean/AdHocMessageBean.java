package uk.gov.courtservice.xhibit.web.messaging.bean;

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

public class AdHocMessageBean {
    private StringField deviceType;

    private AdHocNumberField number;

    private AdHocMessageField message;

    public AdHocMessageBean(String deviceType, String number, String message) throws IllegalArgumentException {
        this.setDeviceType(new StringField(deviceType, 30, false));
        this.setMessage(new AdHocMessageField(message));
        this.setNumber(new AdHocNumberField(number));
    }

    /**
     * Used to find out if the data in the bean is valid
     * 
     * @return a boolean showing the status of the bean
     */
    public boolean isValid() {
        return deviceType.getErrorValue() == null && number.getErrorValue() == null && message.getErrorValue() == null;
    }

    public StringField getDeviceType() {
        return deviceType;
    }

    public AdHocMessageField getMessage() {
        return message;
    }

    public AdHocNumberField getNumber() {
        return number;
    }

    public void setDeviceType(StringField deviceType) throws IllegalArgumentException {
        if (deviceType == null) {
            throw new IllegalArgumentException("deviceType");
        }
        this.deviceType = deviceType;
    }

    public void setMessage(AdHocMessageField message) throws IllegalArgumentException {
        if (message == null) {
            throw new IllegalArgumentException("message");
        }
        this.message = message;
    }

    public void setNumber(AdHocNumberField number) throws IllegalArgumentException {
        if (number == null) {
            throw new IllegalArgumentException("number");
        }
        this.number = number;
    }

}