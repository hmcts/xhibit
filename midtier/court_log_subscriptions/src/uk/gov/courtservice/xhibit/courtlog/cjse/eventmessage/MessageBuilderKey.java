package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import java.util.Locale;

/**
 * <p>
 * Title: Key to a message builder
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is intended to act as a key in a map of message builder instances,
 * different instances of MessageBuilder are differentiated by the message code
 * that they are building messages for and the Locale for which the message is
 * being constructed.
 * </p>
 * <p>
 * This class <u>does</u> cache the hashcode.
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
public class MessageBuilderKey {
    private final String _messageCode;

    private final Locale _locale;

    // cached hashcode.
    private final int _hashCode;

    /**
     * Builds a key to facilitate the holding of MessageBuilder instances in a
     * Map
     * 
     * @param messageCode
     *            The message code for the message.
     * @param locale
     *            The Locale for the message.
     */
    public MessageBuilderKey(String messageCode, Locale locale) {
        _messageCode = messageCode;
        _locale = locale;

        // As instances of this object are intended to be used as keys
        // in a map (most likely an HashMap), it makes sense to cache
        // the hash as long as the fields are immutable.
        _hashCode = messageCode.hashCode() + locale.hashCode();
    }

    /**
     * Returns the cached hash code.
     * 
     * @return The hashcode.
     */
    public int hashCode() {
        return _hashCode;
    }

    /**
     * Confirms whether the passed in object is equivalent to this instance of
     * MessageBuilderKey. Based on whether the Locale and message code are the
     * same.
     * 
     * @param obj
     *            The object to be checked
     * @return True if the object is equivalent.
     */
    public boolean equals(Object obj) {
        if (obj instanceof MessageBuilderKey) {
            MessageBuilderKey candidate = (MessageBuilderKey) obj;
            return _hashCode == candidate._hashCode // Hashes are equal
                    && // AND
                    _messageCode.equals(candidate._messageCode) // MessageCodes
                    // equal
                    && // AND
                    _locale.equals(candidate._locale); // Locales equal
        }
        return false;
    }

    /**
     * Get the message code.
     * 
     * @return the message code.
     */
    public String getMessageCode() {
        return _messageCode;
    }

    /**
     * Get the locale.
     * 
     * @return the locale.
     */
    public Locale getLocale() {
        return _locale;
    }
}