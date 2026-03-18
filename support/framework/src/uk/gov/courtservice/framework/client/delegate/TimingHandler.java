package uk.gov.courtservice.framework.client.delegate;

import java.lang.reflect.Method;

/**
 * @author pznwc5
 * 
 * The class provides decoration to time the invocations
 */
public class TimingHandler extends DecoratingHandler {

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        long now = System.currentTimeMillis();
        Object result = super.invoke(proxy, method, args);
        long duration = System.currentTimeMillis() - now;

        logger
                .info("Time taken to execute " + method + " on " + getDelegateInfo().getDelegateClass() + ": "
                        + duration);
        // TODO Auto-generated method stub
        return result;
    }
}
