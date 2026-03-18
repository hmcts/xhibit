package uk.gov.courtservice.framework.client.delegate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivilegedExceptionAction;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.ServiceLocator;

/**
 * The class provides decoration to log the invocations
 * 
 * @author pznwc5
 * @version $Revision: 1.7 $
 */
public abstract class XhibitHandler implements InvocationHandler {
    // Logger
    protected static final Logger logger = CSServices.getLogger(XhibitHandler.class);

    /**
     * @param in
     *            method on the business delegate
     * @param remote
     *            Target of invocation
     * @return Target method
     * @throws NoSuchMethodException
     */
    protected static Method matchMethod(Method in, EJBObject remote) throws NoSuchMethodException {
        Method[] declaredMethods = remote.getClass().getDeclaredMethods();
        Class inTypes[] = in.getParameterTypes();

        // Iterate through the declared methods
        for (int i = 0; i < declaredMethods.length; i++) {
            // Match the method named
            if (!in.getName().equals(declaredMethods[i].getName())) {
                continue;
            }

            // Match the argument types
            Class declaredTypes[] = declaredMethods[i].getParameterTypes();

            // Check the number of arguments
            if (inTypes.length != declaredTypes.length) {
                continue;
            }

            boolean notFound = false;

            // Check each argument is assignable
            for (int j = 0; j < inTypes.length; j++) {
                if (!inTypes[j].isAssignableFrom(declaredTypes[j])) {
                    notFound = true;
                    break;
                }
            }

            if (!notFound) {
                // We have found a match
                return declaredMethods[i];
            }
        }

        // No match found
        throw new NoSuchMethodException("Method " + in.toString() + " not found on remote object "
                + remote.getClass().getName());
    }

    /**
     * Utility method to get the remote object
     * 
     * @return
     */
    protected static EJBObject getRemote(CSBusinessDelegateInfo delegateInfo) throws Exception {
        ServiceLocator sl = CSServices.getServiceLocator();
        EJBHome home = sl.getRemoteHome(delegateInfo.getHomeClass());
        Method createMethod = home.getClass().getMethod("create", null);
        return (EJBObject) createMethod.invoke(home, null);
    }

    /**
     * An inner class for performing remote invocation
     * 
     * @author Meeraj Kunnumpurath
     */
    protected static class RemoteInvoker implements PrivilegedExceptionAction {
        /** The business method that needs to be invoked */
        private Method businessMethod;

        /** Target remote object */
        private Object target;

        /** Invocation arguments */
        private Object[] args;

        /**
         * Constructor initializes the required information for invocation
         * 
         * @param businessMethod
         * @param target
         * @param args
         */
        public RemoteInvoker(Method businessMethod, Object target, Object args[]) {
            this.businessMethod = businessMethod;
            this.target = target;
            this.args = args;
        }

        /**
         * PrivilegedExceptionACtion implementation
         * 
         * @return
         * @throws InvocationTargetException
         * @throws IllegalAccessException
         */
        public Object run() throws InvocationTargetException, IllegalAccessException {
            return invoke();
        }

        /**
         * This invokes the method
         * 
         * @return
         * @throws InvocationTargetException
         * @throws IllegalAccessException
         */
        public Object invoke() throws InvocationTargetException, IllegalAccessException {
            return businessMethod.invoke(target, args);
        }
    }
}
