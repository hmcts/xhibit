package uk.gov.courtservice.xhibit.web.messaging.bean;

import uk.gov.courtservice.xhibit.client.im.util.AdHocMessageServices;
import uk.gov.courtservice.xhibit.web.framework.bean.AbstractField;

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

public class AdHocMessageField extends AbstractField {
    private String unEscapedMessage;

    public AdHocMessageField(String newMessage) {
        setValue(newMessage);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the value.
     */
    public String getValue() {
        return unEscapedMessage;
    }

    /**
     * Sets the value taking in whatever escape characters are passed..
     * 
     * @param newMessage
     *            the new message in escaped format.
     */
    public void setValue(String newMessage) {
        newMessage = new String(AdHocMessageServices.htmlUnEscapeCharacters(newMessage));

        if (AdHocMessageServices.validUnicodeForSMS(newMessage) > -1)// invalid.
        {
            setErrorValue(newMessage);
            setErrorMessageKey("adhocmessagefield.invalidCharacters");
        }

        unEscapedMessage = newMessage;
    }

}