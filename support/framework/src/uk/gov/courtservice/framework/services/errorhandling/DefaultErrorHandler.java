package uk.gov.courtservice.framework.services.errorhandling;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: DefaultErrorHandler
 * </p>
 * <p>
 * Description: Default handler to handle the logging of errors and exceptions.
 * The output is configured in the log4j.properties file.
 * </p>
 * <p>
 * DefaultErrorHandler is a singleton; use getInstance() to obtain a reference.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @author Kevin Buckthorpe 20/09/2002
 * @version 1.0
 */

public class DefaultErrorHandler extends AbstractErrorHandler {
    private static DefaultErrorHandler instance;

    private DefaultErrorHandler() {
    }

    /**
     * 
     * @return DefaultErrorHandler reference
     */
    public static DefaultErrorHandler getInstance() {
        if (instance == null) {
            instance = new DefaultErrorHandler();
        }

        return instance;
    }

    /**
     * Handle the logging/notification of the exception raised
     * 
     * @param e
     *            The exception being logged
     * @param klass
     *            The calling class name
     * @return Error message for user if available or null
     */
    public String handleError(Throwable t, Class klass) {
        // boolean isLogged = isLogged( t );
        return handleError(t, klass, "");
    }

    /**
     * Handle the logging/notification of the exception raised
     * 
     * @param e
     *            The exception being logged
     * @param klass
     *            The calling class name
     * @param errMsg
     *            Addition information to be logged
     * @return Error message for user if available or null
     */
    public String handleError(Throwable t, Class klass, String errMsg) {
        boolean isLogged = isLogged(t);
        Logger log = CSServices.getLogger(klass);

        // only log the error message if not yet logged
        if (!isLogged) {
            /** @todo check if the circular reference here is a worry */
            log.error(errMsg, t);

            // Throwable t = e; // dont need this if we pas in a throwable
            // [rl] Do not need to log cause explicitly as Log4J does it
            // already.
            // do
            // { t = logCause( t, log );
            // }while( t != null );
        } else { // make a note that the error was logged and record any
            // errMsg intended to be recorded
            // t.getMessage() should include the error id for CSException
            // type exceptions to enable
            // tracing of previous logging
            log.error(errMsg + ": " + t.getMessage());
        }

        // set islogged flag on exception to true
        // return the message intended for the user of the application (or null
        // if no message)
        if (t instanceof CSException) {
            CSException ex = (CSException) t;
            ex.setIsLogged(true);
            return ex.getUserMessage();
        } else {
            return null;
        }
    }

    /*
     * Checks that this instance of Exception has not previously been logged.
     * Returns true only if the the exception is of type CSException AND the
     * exception logging flag has been set to true.
     */
    protected boolean isLogged(Throwable t) {
        boolean isLogged = false;

        // if the Exception is of type CSRecoverableException or CSUnrecoverable
        // exception,
        // check to see if exception has previously been logged.
        if (t instanceof CSException) {
            CSException ex = (CSException) t;
            isLogged = ex.isLogged();
        }

        return isLogged;
    }

    /*
     * If Throwable is of type CSException and contains a reference to the
     * cause, then this logs the stack trace of the cause exception. To enable
     * tracing this does not check that the cause has been logged.
     */
    protected Throwable logCause(Throwable e, Logger log) {
        Throwable cause = null;

        if (e instanceof CSException) {
            CSException ex = (CSException) e;
            cause = ex.getCause();

            if (cause != null) {
                log.error("Exception " + e.getClass().getName() + " caused by: ...", cause);
            }
        }

        return cause;
    }
}