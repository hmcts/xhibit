package uk.gov.courtservice.framework.client.delegate;

import java.lang.reflect.Method;

/**
 * @author pznwc5
 * 
 * The class provides decoration to log the invocations
 */
public class LoggingHandler extends DecoratingHandler {

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("Home class: " + getDelegateInfo().getHomeClass());
        logger.info("Delegate class: " + getDelegateInfo().getDelegateClass());
        logger.info("Method: " + method);
        logger.info("Arguments\\");
        for (int i = 0; args != null && i < args.length; i++)
            logger.info("\t" + args[i]);
        Object result = super.invoke(proxy, method, args);
        logger.info("Return value: " + result);
        return result;
    }
}
