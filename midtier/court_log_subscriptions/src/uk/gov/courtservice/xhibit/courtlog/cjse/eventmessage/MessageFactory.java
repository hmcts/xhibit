package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.darts.DartsEventTextHelper;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is tasked with localising and populating a CJSE message text. All
 * CJSE events have a message text associated with them, made up of various
 * combinations of data and plain text. This class provides a single point of
 * access for this work.
 * </p>
 * <p>
 * The only constructor is private to prevent unnecessary instantiation.
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
public class MessageFactory {
    public static final String EVENT_MESSAGE_BUNDLE_NAME = "CjseEventMessageText";

    private static final Logger log = CSServices.getLogger(MessageFactory.class);
    
    private static DartsEventTextHelper _dartsTextHelper = DartsEventTextHelper.getInstance();

    // Terrible cheat here to provide lazy instantiation...
    // Note also that while I normally avoid lazy instantiation in
    // server applications, there is no way to know the complete list of
    // localisations available.
    // While 'synchronization' is theoretically 'Verboten'
    // in EJB applications, I am aware that there is no
    // app server yet written that will fall over on the
    // use of core java synchronized classes.
    private static final Map messageBuilders = Collections.synchronizedMap(new HashMap());

    /**
     * Private to ensure that it can never be instantiated.
     */
    private MessageFactory() {
        // Never will be instantiated.
    }

    /**
     * This method returns a localised and dynamically populated CJSE message
     * text for a given message code/event/locale.
     * 
     * @param messageCode
     *            the message code to look up.
     * @param value
     *            the details of the event.
     * @param locale
     *            the Locale for which to generate the event.
     * @return the properly constructed message.
     */
    public static String getMessageForMessageCode(String messageCode, Locale locale, CourtLogSubscriptionValue value,
            XhbCase theCase) {
        MessageBuilder messageBuilder = getMessageBuilderForKey(new MessageBuilderKey(messageCode, locale));
        return messageBuilder.getBuiltMessage(value, theCase);
    }
    
    
    /***
     *  Returns the additional information to be added into DARTS event messages when the event is either relating
     *  to a defendant or a witness, otherwise will return an empty string.
     
     * @param value - The CourtLogSubscriptionValue of the current event
     * @param theCase - The case relating to new event
     * @param cjseMessageCode - the XHIBIT event Text code as defined in CjseEventMessageText.properties
     * @param xhibtEventId - the id of the XHIBIT event 
     * @return
     */
    public static String getDartsMessageForMessageCode( CourtLogSubscriptionValue value,
            XhbCase theCase, String cjseMessageCode, String xhibtEventId) {
        return _dartsTextHelper.getDartsEventText(cjseMessageCode, value, theCase, xhibtEventId );
    }
    
    
    /**
     * This method looks after the caching and lazy instantiation of
     * MessageBuilders.
     * 
     * @param messageBuilderKey
     *            The key to look for
     * @return The appropriate MessageBuilder for the key.
     */
    private static MessageBuilder getMessageBuilderForKey(MessageBuilderKey messageBuilderKey) {
        MessageBuilder messageBuilder = (MessageBuilder) messageBuilders.get(messageBuilderKey);
        if (messageBuilder == null) {
            messageBuilder = getNewMessageBuilder(messageBuilderKey);
            messageBuilders.put(messageBuilderKey, messageBuilder);
        }
        return messageBuilder;
    }

    /**
     * This method is concerned with the instantiation of new MessageBuilders
     * for a given message code and locale (encapsulated in a
     * MessageBuilderKey).
     * 
     * @param messageBuilderKey
     *            The code and locale for which to create a new MessageBuilder
     * @return The new instance of MessageBuilder for the key.
     */
    private static MessageBuilder getNewMessageBuilder(MessageBuilderKey messageBuilderKey) {
        String messageText;
        ResourceBundle localisedMessageBundle = CSServices.getConfigServices().getBundle(EVENT_MESSAGE_BUNDLE_NAME,
                messageBuilderKey.getLocale());
        messageText = localisedMessageBundle.getString(messageBuilderKey.getMessageCode());

        if (messageText == null) {
            messageText = messageBuilderKey.getMessageCode();
            log.error("Cannot retrieve a message text for message code: " + messageText);
        }
        MessageElement[] messageElements = MessageElementFactory.getElementsForMessageText(messageText);
        return new MessageBuilder(messageElements);
    }
}
