package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Factory class for Message Elements
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This factory generates either single MessageElements or an array of
 * MessageElements from a single String. A message element is a portion of a
 * message that needs individual handling, this allows us to build up messages
 * of arbitrary complexity in an extensible manner.
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
public class MessageElementFactory {
    /**
     * The separator used to signify different portions of an unparsed
     * messageText.
     */
    public static final String ELEMENT_SEPARATOR = "$$$";

    /**
     * The component properties file name.
     * <p>
     * Used as:
     * <code>CSServices.getConfigServices().getProperties(PROPERTIES_FILE_NAME)</code>
     * <p>
     * <code>cjse.event.message.elements</code>
     */
    public static final String PROPERTIES_FILE_NAME = "cjse.event.message.elements";

    // The different message element classes are cached for reuse and
    // efficiency.
    // In a server application unlike a client application, lazy
    // instantiation has
    // limited value as the application is likely to remain up for long
    // enough that
    // virtually all portions of the application will see use.
    private static Map messageElementsCache = new HashMap();

    // get the properties defining the individual message elements and
    // instantiate
    // them to the cache.
    static {
        Properties elementProperties = CSServices.getConfigServices().getProperties(PROPERTIES_FILE_NAME);
        Enumeration elementKeys = elementProperties.keys();
        while (elementKeys.hasMoreElements()) {
            String key = (String) elementKeys.nextElement();
            String elementClass = elementProperties.getProperty(key);
            messageElementsCache.put(key, getMessageElementInstance(elementClass));
        }
    }

    /**
     * This method breaks down a message text string using the ELEMENT_SEPARATOR
     * and gets the appropriate message elements.
     * 
     * @param messageText
     *            The message text to parse.
     * @return an array of MessageElements representing the messageText.
     */
    public static MessageElement[] getElementsForMessageText(String messageText) {
        String[] elementStrings = getElementStrings(messageText);
        MessageElement[] messageElements = new MessageElement[elementStrings.length];
        for (int i = elementStrings.length - 1; i >= 0; i--) {
            messageElements[i] = getElementForString(elementStrings[i]);
        }
        return messageElements;
    }

    /**
     * This simple utility method breaks down a string into an array of
     * substrings using the ELEMENT_SEPARATOR and the beginning and end of the
     * String. The ELEMENT_SEPARATOR will not be a part of the returned array.
     * 
     * @param messageText
     *            the string to break down.
     * @return an array of strings from the messageText not including the
     *         ELEMENT_SEPARATOR.
     */
    private static String[] getElementStrings(String messageText) {
        int currentPosition = 0;
        ArrayList elementStringList = new ArrayList(); // build up the return
        // array.

        // Mildly complex use of for loop to step through a String.
        for (int positionInString = // First position of ELEMENT_SEPARATOR
        messageText.indexOf(ELEMENT_SEPARATOR); positionInString >= 0; // When
        // there
        // are
        // no
        // more
        // (==-1)
        // stop.
        positionInString = // Next position of ELEMENT_SEPARATOR
        messageText.indexOf(ELEMENT_SEPARATOR, currentPosition)) {
            addNonZeroLengthToList(elementStringList, messageText.substring(currentPosition, positionInString));
            currentPosition = positionInString + ELEMENT_SEPARATOR.length();
        }
        // Get any remaining element information.
        addNonZeroLengthToList(elementStringList, messageText.substring(currentPosition));

        // Construct the return array.
        String[] returnArray = new String[elementStringList.size()];
        elementStringList.toArray(returnArray);
        return returnArray;
    }

    /**
     * Simple pass by reference method for adding only non-zero length strings
     * to the list. Will also cope gracefully with null values for the String
     * parameter.
     * 
     * @param list
     *            The list to potentially add the string to.
     * @param toBeAdded
     *            The String to be added.
     */
    private static void addNonZeroLengthToList(ArrayList list, String toBeAdded) {
        if (toBeAdded != null && toBeAdded.length() > 0) {
            list.add(toBeAdded);
        }
    }

    /**
     * This method attempts to return an appropriate message element for the
     * passed in element string. if no message element exists, it instantiates a
     * StringMessageElement passing in the element string.
     * 
     * @param elementString
     *            The string representing the element.
     * @return An instance of MessageElement, either one specific to the element
     *         string or, if no specific one can be found, an instance of
     *         StringMessageElement.
     */
    public static MessageElement getElementForString(String elementString) {
        MessageElement returnMessageElement = (MessageElement) messageElementsCache.get(elementString);
        if (returnMessageElement == null) // Can't find a specific one.
            returnMessageElement = new StringMessageElement(elementString);
        return returnMessageElement;
    }

    /**
     * Utility method that attempts to instantiate an instance of a class
     * implementing MessageElement.
     * 
     * @param messageElementClassName
     *            The name of the class to instantiate
     * @return an instance of the class cast to MessageElement.
     */
    private static MessageElement getMessageElementInstance(String messageElementClassName) {
        // Attempt to instantiate it.
        try {
            Class candidateClass = Class.forName(messageElementClassName);
            if (MessageElement.class.isAssignableFrom(candidateClass)) {
                return (MessageElement) candidateClass.newInstance();
            } else {
                // Not an instance of MessageElement so throw an
                // exception.
                throw new CSUnrecoverableException("Class: " + messageElementClassName
                        + " defined as an message element is not an instance" + "of MessageElement.");
            }
        } catch (ClassNotFoundException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not find class named: " + messageElementClassName, ex);
        } catch (IllegalAccessException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not access class named: " + messageElementClassName, ex);
        } catch (InstantiationException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not instantiate class named: " + messageElementClassName, ex);
        }
    }
}
