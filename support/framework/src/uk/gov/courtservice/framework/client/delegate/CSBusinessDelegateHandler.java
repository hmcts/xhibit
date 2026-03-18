package uk.gov.courtservice.framework.client.delegate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.security.PrivilegedActionException;

import javax.ejb.EJBObject;
import javax.transaction.TransactionRolledbackException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.security.SubjectManager;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Invocation handler for business delegate
 * </p>
 * <p>
 * Warning: This class is not thread safe
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * <P>
 * Removed EJBObject caching
 * </p>
 * <P>
 * Added code for passing through business exception
 * </p>
 * <P>
 * Changed invoke method to throw Exception
 * </p>
 * 
 * @author Sarah Tong
 * @version 1.0
 */

public class CSBusinessDelegateHandler extends XhibitHandler {
    private static Logger log = CSServices.getLogger(CSBusinessDelegateHandler.class);

    /**
     * EJBObject associated with this business delegate
     */
    private EJBObject remote;

    /**
     * Number of retries
     */
    private static final int MAX_ATTEMPTS = 3;

    /**
     * Business delegate information
     */
    protected final CSBusinessDelegateInfo delegateInfo;

    /**
     * Construvtor initailizes delegate and access information
     * 
     * @param delegateInfo
     */
    public CSBusinessDelegateHandler(CSBusinessDelegateInfo delegateInfo) throws CSUnrecoverableException {
        if (delegateInfo == null) {
            throw new IllegalArgumentException("delegateInfo: null");
        }
        this.delegateInfo = delegateInfo;

        try {
            // Get the EJB remote object
            remote = getRemote(delegateInfo);
        } catch (Exception ex) {
            // This is an unexpected exception
            CSUnrecoverableException x = new CSUnrecoverableException(ex);
            CSServices.getDefaultErrorHandler().handleError(ex, CSBusinessDelegateHandler.class);
            throw x;

        }

    }

    protected CSBusinessDelegateInfo getDelegateInfo() {
        return delegateInfo;
    }

    /**
     * Invocation handler implementation that maps a method invocation on the
     * dynamic proxy to session facade invocation
     * 
     * @param obj
     * @param method
     * @param args
     * @return
     * @throws
     */
    public Object invoke(Object obj, Method method, Object[] args) throws CSBusinessException, CSUnrecoverableException {

        // Business method
        Method businessMethod = null;
        Throwable lastException = null;
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            try {

                attempts++;

                // Get the business method
                businessMethod = matchMethod(method, remote);

                // Create a remote invoker
                RemoteInvoker invoker = new RemoteInvoker(businessMethod, remote, args);

                try {
                    return invoker.invoke();
                    //return SubjectManager.getInstance().runAs(invoker);
                } catch (Exception ex) {
                    // Translate privileged action exception to
                    throw ex;
                }
            } catch (IllegalAccessException ex) {
                rethrow(ex);
            } catch (NoSuchMethodException ex) {
                rethrow(ex);
            } catch (InvocationTargetException ex) {
                // RemoteException is bubbled up
                lastException = processInvocationTargetExceptions(ex, attempts);
            } catch (CSUnrecoverableException ex) {
                if (ex.getCause() instanceof TransactionRolledbackException) {
                    lastException = ex;
                    logger.warn(ex);
                } else {
                    rethrow(ex);
                }
            } catch (Exception ex) {
                // This is unexpected and cased by the signature of
                // PrivilegedActionException.getException
                rethrow(ex);
            }
        }
        final String message = "Method " + method.getName() + " was invoked " + MAX_ATTEMPTS
                + " times and still failed.";
        logger.fatal(message, lastException);
        throw new CSUnrecoverableException(message, lastException);
    }

    private Throwable processInvocationTargetExceptions(InvocationTargetException ex, int attempts)
            throws CSBusinessException {
        if (ex.getTargetException() instanceof RemoteException) {
            rethrow(ex);
        }
        // TransactionRolledBackException triggers a retry
        InvocationTargetException lastException = null;
        if (ex.getTargetException() instanceof TransactionRolledbackException) {
            lastException = ex;
            logger.warn(ex);
        } else {
            // If we reach here it is a business exception
            if (ex.getTargetException() instanceof CSBusinessException) {
                throw (CSBusinessException) ex.getTargetException();
            } else {
                logger.fatal("Incorrect Exception thrown from session facade, please consult the developer guide.", ex
                        .getTargetException());
                throw new CSUnrecoverableException(ex.getTargetException());
            }
        }
        return lastException;
    }

    private static void rethrow(Exception ex) {
        CSUnrecoverableException x = new CSUnrecoverableException(ex);
        CSServices.getDefaultErrorHandler().handleError(x, CSBusinessDelegateHandler.class);
        throw x;
    }

    private static final String getMethodSummary(Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.getDeclaringClass());
        builder.append('.');
        builder.append(method.getName());
        builder.append('(');
        Class[] params = method.getParameterTypes();
        if (0 < params.length) {
            builder.append(params[0].getName());
            for (int i = 1; i < params.length; i++) {
                builder.append(',');
                builder.append(params[i].getName());
            }
        }
        builder.append(')');
        return builder.toString();
    }

}