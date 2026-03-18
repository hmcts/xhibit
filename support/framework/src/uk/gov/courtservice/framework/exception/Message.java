package uk.gov.courtservice.framework.exception;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.ConfigServices;

/**
 * 
 * <p>
 * Title: Message
 * </p>
 * <p>
 * Description: The Message class supports internationalisation in the creation
 * of message texts. Messages are created by providing a key which exists in the
 * external properties file (e.g. errortext.properties). These messages may
 * require additional parameters for the construction of the completed message.
 * see also java.lang.MessageFormat
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */
public class Message implements Serializable {

    public static final String UNEXPECTED_ERROR = "xhibit.error.unexpected";

    public static final String OPTIMISTIC_LOCK_VIOLATED = "xhibit.error.optimisticlock_violated";

    // private vos
    private final java.lang.String key;

    private final java.lang.Object[] parameters;

    // private ResourceBundle errorText;
    private Locale locale;

    // initially using errortext.properties
    // private static String ERROR_MESSAGES = ConfigServices.ERROR_MESSAGES;
    private String ERROR_MESSAGES = ConfigServices.ERROR_MESSAGES;

    /**
     * Constructor
     * 
     * @param key
     *            external key in properties file
     * @param parameters
     *            paramaters that will be passed into
     *            MessageFormat.format(String, Object[]) to format the message
     */
    public Message(String key, Object[] parameters) {
        this.key = key;
        this.parameters = parameters;
        // init();
    }

    /**
     * Constructor
     * 
     * @param key
     *            external key in properties file
     * @param parameters
     *            paramaters that will be passed into
     *            MessageFormat.format(String, Object[]) to format the message
     * @param properties
     *            file if not using default
     */
    public Message(String key, Object[] parameters, String properties) {
        this.key = key;
        this.parameters = parameters;
        this.ERROR_MESSAGES = properties;
    }

    /**
     * Convienience constructor that passes the parameter into Object[] and
     * calls the Message(String, Object[]) constructor.
     * 
     * @param key
     *            external key in properties file
     * @param parameter
     *            paramater that will be passed into
     *            MessageFormat.format(String, Object[])
     */
    public Message(String key, Object parameter) {
        this(key, new Object[] { parameter });
    }

    /**
     * Convienience constructor that passes the parameter into Object[] and
     * calls the Message(String, Object[]) constructor.
     * 
     * @param key
     *            external key in properties file
     * @param parameter
     *            paramater that will be passed into
     *            MessageFormat.format(String, Object[])
     * @param properties
     *            file if not using default
     */
    public Message(String key, Object parameter, String properties) {
        this(key, new Object[] { parameter }, properties);
    }

    /**
     * Constructor
     * 
     * @param key
     *            external key in properties file
     */
    public Message(String key) {
        this(key, new Object[] {});
    }

    /**
     * Constructor
     * 
     * @param key
     *            external key in properties file
     * @param properties
     *            file if not using default
     */
    public Message(String key, String properties) {
        this(key, new Object[] {});
    }

    /**
     * 
     * @return external key
     */
    public String getKey() {
        return key;
    }

    /**
     * Formats and returns text associated with the key provided
     * 
     * @return the formated message text
     */
    public String getMessage() {
        ResourceBundle errorText;

        if (locale == null) {
            locale = Locale.getDefault();
        }

        errorText = CSServices.getConfigServices().getBundle(ERROR_MESSAGES, locale);

        String msg = errorText.getString(key);
        return MessageFormat.format(msg, parameters);
    }

    /**
     * Returns any parameters associated with the current message
     * 
     * @return Array of object parameters or an empty array if nor parameters
     */
    public Object[] getParameters() {
        if (parameters == null) {
            return new Object[] {};
        }

        return parameters;
    }

    // private void init()
    // {
    // if( locale == null )
    // {
    // locale = Locale.getDefault();
    // }
    // if( errorText == null )
    // {
    // errorText = CSServices.getConfigServices().getBundle( ERROR_MESSAGES,
    // locale);
    // }
    // }

    /**
     * 
     * @param loc
     *            the Locale
     * @throws CSResourceUnavailableException
     */
    public void setLocale(Locale loc) throws CSConfigurationException {
        locale = loc;
        // errorText = CSServices.getConfigServices().getBundle( ERROR_MESSAGES,
        // locale);
    }

    /**
     * 
     * @return the propeties file used to match key value pairs
     */
    public String getPropertiesFileName() {
        return ERROR_MESSAGES + ".properties";
    }

}