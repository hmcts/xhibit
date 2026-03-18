package uk.gov.courtservice.framework.client.delegate;

import java.lang.reflect.Proxy;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: CSBusinessDelegateFactory
 * </p>
 * <p>
 * Description: Supplier of all business delegates for client use
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Framework Team, Pete Raymond
 * @version 1.0
 */
public class CSBusinessDelegateFactory {

    /**
     * Singleton instance
     */
    private static CSBusinessDelegateFactory self = new CSBusinessDelegateFactory();

    private static final boolean decorate = CSServices.getConfigServices().getProperty("invoker.decorate") != null;

    /**
     * Private constructor initalizes the bundle
     */
    private CSBusinessDelegateFactory() {
    }

    /**
     * Gets the business delegate factory instance
     * 
     * @return
     */
    public static CSBusinessDelegateFactory getInstance() {
        return self;
    }

    /**
     * Retrieve a business delegate from supplied delegate and home class names.
     * 
     * @param delegateClass
     *            the name of the delegate class eg "com.xyz.thing.MyDelegate"
     * @param homeClass
     *            the name of the home class eg "com.xyz.thing.MyDelegate"
     * @return a valid business delegate
     * @throws CSUnrecoverableException
     */
    public CSBusinessDelegate getBusinessDelegate(Class delegateClass, Class homeClass) throws CSUnrecoverableException {
        return retrieveDelegateFromInfo(new CSBusinessDelegateInfo(delegateClass, homeClass));
    }

    // Does the bulk of the delegate work.
    private CSBusinessDelegate retrieveDelegateFromInfo(CSBusinessDelegateInfo info) {

        XhibitHandler handler = null;
        if (decorate) {
            handler = getDecoratedHandler(new CSBusinessDelegateHandler(info));
        } else {
            handler = new CSBusinessDelegateHandler(info);
        }
        return (CSBusinessDelegate) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { info
                .getDelegateClass() }, handler);
    }

    private XhibitHandler getDecoratedHandler(CSBusinessDelegateHandler handler) {
        return DecoratorFactory.getDecoratedHandler(handler);
    }
}