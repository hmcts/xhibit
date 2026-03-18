package uk.gov.courtservice.xhibit.integration.mercator;

//jdk
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ReturnMVO;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;
import uk.gov.courtservice.xhibit.integration.services.MercatorExecutionException;
import uk.gov.courtservice.xhibit.integration.services.MercatorValidationException;

/**
 * 
 * <p>
 * Title: MercatorWrapper
 * </p>
 * <p>
 * Description: Abstract class to execute the Mercator map
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Cag Onganer
 * @author Abdul Rahim Hussain
 * @author Marie Holmberg
 * @author GJS: rewritten with runMap as abstract so we can either send the MVO
 *         to the Mercator EJB or package the MVO as an XML message and send
 *         over HTTP to the Mercator Web Service
 * @version 1.1
 */

public abstract class MercatorWrapper {
    // the logger
    private static Logger log = CSServices.getLogger(MercatorWrapper.class);

    // Error keys
    protected static final String BUSINESS_VALIDATION = "integ.mercator.businessvalidation";

    protected static final String EXECUTION = "integ.mercator.execution";

    // return codes will be seperated with a colon.
    protected static final String VALIDATION_CODE_SEPERATOR = ":";

    /**
     * Default, empty constructor
     */
    public MercatorWrapper() {
    }

    /**
     * Abstract Method to set the required settings and run the map.
     * 
     * @param logicalMapName
     *            String the name of the logical name used.
     * @param obj
     *            Object - this is any MercatorValueObject that will be passed
     *            in to the map.
     * @return Object extraInfo will be returned if successful
     * @throws CSBusinessException
     */
    public abstract Object runMap(String logicalMapName, Object obj) throws MercatorException;

    /**
     * Generic method to execute a method on an executor object passing the
     * specified parameter
     * 
     * @param parameter
     * @param methodName
     * @param executor
     * @return Object
     */
    protected Object execute(Object parameter, String methodName, Object executor) {
        log.debug("***** execute parameter: " + parameter.getClass() + " methodName: " + methodName);

        Method m = null;
        Class[] methodParamTypes = { parameter.getClass() };
        Object[] parameters = { parameter };

        try {
            m = executor.getClass().getMethod(methodName, methodParamTypes);
        } catch (NoSuchMethodException nsme) {
            CSServices.getDefaultErrorHandler().handleError(nsme, MercatorWrapper.class);

            // this is an unexpected exception, not a business exception
            throw new CSUnrecoverableException("No Such Method Exception when creating: " + methodName
                    + " method for: " + parameter.getClass(), nsme);
        }

        try {
            return m.invoke(executor, parameters);
        } catch (IllegalAccessException iae) {
            CSServices.getDefaultErrorHandler().handleError(iae, MercatorWrapper.class);

            // this is an unexpected exception, not a business exception
            throw new CSUnrecoverableException("IllegalAccessException when invoking: " + methodName + " method for: "
                    + parameter.getClass(), iae);
        } catch (IllegalArgumentException iarge) {
            CSServices.getDefaultErrorHandler().handleError(iarge, MercatorWrapper.class);

            // this is an unexpected exception, not a business exception
            throw new CSUnrecoverableException("IllegalArgumentException when invoking: " + methodName
                    + " method for: " + parameter.getClass(), iarge);
        } catch (InvocationTargetException ite) {
            CSServices.getDefaultErrorHandler().handleError(ite, MercatorWrapper.class);

            log.debug("InvocationTargetException (RMI Exception) when invoking: " + methodName + " method for: "
                    + parameter.getClass() + " Exception was: " + ite);
            ite.printStackTrace();

            // this is an unexpected exception, not a business exception
            throw new CSUnrecoverableException("InvocationTargetException (RMI Exception) when invoking: " + methodName
                    + " method for: " + parameter.getClass(), ite);
        } catch (Exception e) {
            CSServices.getDefaultErrorHandler().handleError(e, MercatorWrapper.class);

            // this is an unexpected exception, not a business exception
            throw new CSUnrecoverableException("Exception when invoking: " + methodName + " method for: "
                    + parameter.getClass(), e);
        }
    }

    /**
     * Get the real map name and its storage from the property. The logical name
     * is being used in the code.
     * 
     * @param logicalMapName
     *            String the name of the logical name used.
     * @return String name of the real map name
     */
    protected String getMapName(String logicalMapName) {
        return getMercatorProperties().getProperty(logicalMapName);
    }

    /**
     * Private method to get the input card number from the property.
     * 
     * @param logicalMapName
     *            String
     * @return int - the number of the input card to use
     */
    public int getInputCard(String logicalMapName) {
        return new Integer(getMercatorProperties().getProperty(logicalMapName + ".InputCard")).intValue();
    }

    /**
     * Private method to get the output card number from the property.
     * 
     * @param logicalMapName
     *            String
     * @return int - the number of the output card to use
     */
    public int getOutputCard(String logicalMapName) {
        return new Integer(getMercatorProperties().getProperty(logicalMapName + ".OutputCard")).intValue();
    }

    /**
     * Private method to get the result output card number from the property.
     * 
     * @param logicalMapName
     *            String
     * @return int - the number of the result output card to use
     */
    public int getResultOutputCard(String logicalMapName) {
        String rocNumString = getMercatorProperties().getProperty(logicalMapName + ".ResultOutputCard");
        // if ResultOutputCard property doesn't exist use the OutputCard
        if (rocNumString == null) {
            return getOutputCard(logicalMapName);
        } else {
            return new Integer(rocNumString).intValue();
        }
    }

    /**
     * Method to return the properties set in the property file that stores all
     * the default setting for executing the mercator map.
     * 
     * @return Properties mercator map specific
     */
    protected Properties getMercatorProperties() {
        try {
            if (MercatorWrapperFactory.MERCATOR_PROPERTIES != null) {
                return MercatorWrapperFactory.MERCATOR_PROPERTIES;
            } else {
                return MercatorWrapperFactory.getMercatorProperties();
            }
        } catch (RuntimeException e) {
            CSServices.getDefaultErrorHandler().handleError(e, MercatorWrapper.class);
            throw new CSConfigurationException("Could not read integration.mercatorwrap.properties", e);
        }
    }

    /**
     * Method to validate the responde code from Mercator. 0 = Success (default)
     * 8 = Wrong inputs 9 = Database not available 21 = Input valid but un-named
     * data found - NOTE: This is still treated as a success transaction! 30 =
     * business validation errors - the validationCode will be populated with a
     * key and error message.
     * 
     * @param rc
     *            the return code from mercator
     * @param returnValue
     *            ReturnMVO
     * @throws MercatorExecutionException
     * @throws MercatorValidationException
     */
    protected void respond(int rc, ReturnMVO returnValue) throws MercatorExecutionException,
            MercatorValidationException {
        if (returnValue == null) {
            log.debug("************ MERCATOR DIDN'T RETURN A ReturnMVO *********** ");

            throw new MercatorExecutionException(EXECUTION, "Mercator ReturnMVO was null", returnValue);
        }

        log.debug("respond() entered rc=" + rc + " returnValue = " + returnValue.toString());

        switch (rc) {
        case 0:
            log.debug("************ SUCCESSFUL MERCATOR RETURN VALUE OF 0 *********** ");
            break;
        case 21:
            log.debug("************ INPUT VALID BUT SOME UNNAMED - TREATED AS SUCCESSFUL *********** ");
            log.debug(returnValue.toString());
            break;
        case 30:
            log.debug("************ BUSINESS VALIDATION EXCEPTION IN MERCATOR *********** ");
            log.debug(returnValue.toString());
            // create a customised MercatorValidationException and throw it.
            throw this.createValidationException(returnValue);
        default:
            log.debug("************ UNKNOWN MERCATOR RETURN VALUE (POSSIBLE DB UNAVAILABLE (9)) *********** ");
            log.debug(returnValue.toString());
            throw new MercatorExecutionException(EXECUTION, "Mercator Execution Errors", returnValue);
        }
        log.debug("Mercator HTTP connection has been made and the XML message sent and received successfully.");
        log.debug("respond() exited ");
    }

    /**
     * This method should ONLY be used when a mercator call has failed becuase
     * of business validation.
     * 
     * It will get the validationCode from the returnvalue passed back from the
     * Mercator in an XML document via the HTTP connection and return a
     * MercatorValidationException.
     * 
     * The validation code contains the error key and error message. The key and
     * the message are seperated with a colon (:). The key will always be
     * followed by the message.
     * 
     * The validation code is popultated in the following format: "xhb123:The
     * data input for last conviction data is invalid"
     * 
     * If the validation code of some reason cannot be fetched or the format is
     * wrong then a default key and message will be set.
     * 
     * @param returnValue
     *            of type ReturnMVO. This object will be populated with
     *            transaction specific information.
     * @return MercatorValidationException populated with the specific key and
     *         error message.
     */
    protected MercatorValidationException createValidationException(ReturnMVO returnValue) {
        log.debug("createValidationException() entered ");
        // The exception that will be returned.
        MercatorValidationException mercatorValidationException = null;

        // Take out the validation code that contains the key and the message.
        String validationCode = returnValue.getValidationCode();

        // get the index of the colon (:)
        int splitIndex = validationCode.indexOf(VALIDATION_CODE_SEPERATOR);

        // if the colon doesn't exist the splitIndex will be set to -1 and there
        // is
        // no customised key and message and the exception will be populated
        // with a
        // generic Business Validation message.
        if (splitIndex != -1) {
            // get the key out e.g. "xhb123"
            String key = validationCode.substring(0, splitIndex).trim();

            // get the errormessage out e.g. "The data input for last
            // conviction data is invalid"
            String errorMessage = validationCode.substring(splitIndex + 1).trim();

            // create an Exception with the key and message populated.
            mercatorValidationException = new MercatorValidationException(key, errorMessage, returnValue);

            log.info("error key : " + key + ", error message : " + errorMessage);
        } else {
            // create a generic message
            mercatorValidationException = new MercatorValidationException(BUSINESS_VALIDATION,
                    "Business validation errors", returnValue);
            log.debug("Could not get the error key and message. Create a generic message.");
        }
        log.debug("createValidationException() finished ");
        return mercatorValidationException;
    }
}