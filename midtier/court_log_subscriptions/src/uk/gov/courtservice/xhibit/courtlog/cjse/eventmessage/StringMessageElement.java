package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: String Message Element
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class represents the simplest case of MessageElement, a string. It
 * should be the only instance with a parameter in it's constructor.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class StringMessageElement implements MessageElement {
    // The string.
    private String _messageFragment;

    /**
     * Construct with the given string.
     * 
     * @param messageFragment
     *            the string that this message element encapsulates.
     */
    public StringMessageElement(String messageFragment) {
        _messageFragment = messageFragment;
    }

    /**
     * The string that this message element contains.
     * 
     * @param value
     *            not used.
     * @return The string that this instance contains.
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        return _messageFragment;
    }
}