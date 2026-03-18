package uk.gov.courtservice.xhibit.web.framework.listener;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.web.framework.util.ObjectNotSerializableException;

/**
 * <p>
 * Title: Control
 * </p>
 * <p>
 * Description: Checks data as it is added to the session to ensure it is
 * serializable
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003)
 */

public class SerializationCheckerHttpSessionAttributeListener implements HttpSessionAttributeListener {

    /**
     * The log4j logger
     */
    private static final Logger log = CSServices.getLogger(SerializationCheckerHttpSessionAttributeListener.class);

    /**
     * System property key used to enable serialization checker
     */
    private static final String ENABLE_SERIALIZATION_CHECKER_KEY = "serialization.check.enabled";

    /**
     * System property key used to check if exception is to be thrown
     */
    private static final String SERIALIZATION_EXCEPTION_CHECKER_KEY = "serialization.exception.enabled";

    /**
     * Set to true if the system property is set to true
     */
    private static final boolean serializationCheckerEnabled = isSerializationCheckerEnabled();

    /**
     * Set to true if the system property is set to true
     */
    private static final boolean throwExceptionEnabled = isThrowExceptionEnabled();

    /**
     * HttpSessionAttributeListener Implementation: Notification that an
     * attribute has been added to a session. Called after the attribute is
     * added.
     * 
     * @param e
     *            describes the event
     */
    public void attributeAdded(HttpSessionBindingEvent e) {
        if (serializationCheckerEnabled) {
            Object value = e.getValue();
            if (isSerializable(value)) {
                log.debug("Added object of type " + value.getClass().getName() + " to session using key \""
                        + e.getName() + "\".");
            } else {
                log.fatal("Added unserializable object of type " + value.getClass().getName()
                        + " to session using key \"" + e.getName() + "\".");
                if (throwExceptionEnabled) {
                    throw new ObjectNotSerializableException(value.getClass().getName());
                }
            }
        }
    }

    /**
     * HttpSessionAttributeListener Implementation: Notification that an
     * attribute has been removed from a session. Called after the attribute is
     * removed.
     * 
     * @param e
     *            describes the event
     */
    public void attributeRemoved(HttpSessionBindingEvent e) {
        if (serializationCheckerEnabled) {
            log.debug("Removed from session object of type " + e.getValue().getClass().getName() + " keyed by \""
                    + e.getName() + "\".");
        }
    }

    /**
     * HttpSessionAttributeListener Implementation: Notification that an
     * attribute has been replaced in a session. Called after the attribute is
     * replaced.
     * 
     * @param e
     *            describes the event
     */
    public void attributeReplaced(HttpSessionBindingEvent e) {
        if (serializationCheckerEnabled) {
            Object newValue = e.getSession().getAttribute(e.getName());
            if (isSerializable(newValue)) {
                log.debug("Replaced object of type " + e.getValue().getClass().getName()
                        + " in session with object of type " + newValue.getClass().getName() + " keyed by \""
                        + e.getName() + "\".");
            } else {
                log.error("Replaced object of type " + e.getValue().getClass().getName()
                        + " in session with unserializable object of type  " + newValue.getClass().getName()
                        + " keyed by \"" + e.getName() + "\".");
            }
        }
    }

    /**
     * Utility method to check if system property has been set to true
     * 
     * @return true if property set to true
     */
    private static boolean isSerializationCheckerEnabled() {
        String property = System.getProperty(ENABLE_SERIALIZATION_CHECKER_KEY);
        boolean serializationChecker = property != null
                && (property.equalsIgnoreCase("TRUE") || property.equalsIgnoreCase("YES"));
        log.debug("Is Serialization checker enabled : " + serializationChecker);
        return serializationChecker;
    }

    /**
     * Utility method to check if system property has been set to true
     * 
     * @return true if property set to true
     */
    private static boolean isThrowExceptionEnabled() {
        String property = System.getProperty(SERIALIZATION_EXCEPTION_CHECKER_KEY);
        boolean throwException = property != null
                && (property.equalsIgnoreCase("TRUE") || property.equalsIgnoreCase("YES"));
        log.debug("Is Exception to be thrown :: " + throwException);
        return throwException;
    }

    /**
     * Utility method to check if an object is serializable
     * 
     * @param object
     *            the object to check
     * @return true if the object is serializable
     */
    private static boolean isSerializable(Object object) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new NullOutputStream());
            try {
                out.writeObject(object);
                return true;
            } finally {
                out.close();
            }
        } catch (IOException ioe) {
            return false;
        }
    }

    /**
     * Implementation of OutputStream for sinking output
     */
    private static final class NullOutputStream extends OutputStream {

        /**
         * Construct a new null output stream
         */
        public NullOutputStream() {

        }

        /**
         * Sink the call
         * 
         * @param b
         *            the <code>byte</code>.
         */
        public void write(int b) {
            // Do Nothing
        }

        /**
         * Sink the call (implemented for performance reasons)
         * 
         * @param b
         *            the date
         */

        public void write(byte b[]) {
            // Do Nothing
        }

        /**
         * Sink the call (implemented for performance reasons)
         * 
         * @param b
         *            the data.
         * @param off
         *            the start offset in the data.
         * @param len
         *            the number of bytes to write.
         */
        public void write(byte b[], int off, int len) {
            // Do Nothing
        }
    }

}
