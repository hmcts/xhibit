/*
 * Created on May 6, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package uk.gov.courtservice.framework.client.delegate;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * @author pznwc5
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class DecoratorFactory {

    /** Decorator properties */
    private static Properties prop;

    /** Decorator prototype */
    private static DecoratingHandler decoratorPrototype;

    /**
     * Populates the decorators
     */
    static {
        createDecorators();
    }

    /**
     * Private constructor to stop instantiation
     * 
     */
    private DecoratorFactory() {

    }

    /**
     * Creates the decorator prototype
     */
    private static void createDecorators() {
        prop = CSServices.getConfigServices().getProperties("invoker");
        System.out.println(prop);

        Stack handlers = new Stack();

        StringTokenizer decoratorList = new StringTokenizer(prop.getProperty("decorators"), ",");

        while (decoratorList.hasMoreTokens()) {
            String decorator = decoratorList.nextToken().trim();
            String decoratorClass = prop.getProperty(decorator);
            System.out.println("Decorator==>>" + decorator + ":" + decoratorClass);

            DecoratingHandler handler = createDecorator(decorator, decoratorClass);
            if (handlers.size() > 0) {
                DecoratingHandler lastHandler = (DecoratingHandler) handlers.peek();
                lastHandler.setNextHandler(handler);
            }
            handlers.push(handler);
        }

        decoratorPrototype = (DecoratingHandler) handlers.firstElement();

    }

    /**
     * @param decoratorClass
     * @return
     */
    private static DecoratingHandler createDecorator(String decorator, String decoratorClass) {
        try {
            DecoratingHandler handler = (DecoratingHandler) Class.forName(decoratorClass).newInstance();

            PropertyDescriptor pds[] = Introspector.getBeanInfo(handler.getClass()).getPropertyDescriptors();

            for (int i = 0; i < pds.length; i++) {
                // All properties are treated as Strings for the time being
                String val = System.getProperty(decorator + "." + pds[i].getName());
                if (val == null)
                    val = prop.getProperty(decorator + "." + pds[i].getName());
                if (val == null)
                    continue;
                System.out.println(pds[i].getName() + ":" + val);
                pds[i].getWriteMethod().invoke(handler, new Object[] { val });
            }
            return handler;
        } catch (Exception ex) {
            throw new CSUnrecoverableException(ex);
        }
    }

    /**
     * Gets a decorated invocation handler
     * 
     * @param base
     *            Base invocation handler
     * @return Decorated invocation handler
     */
    public static XhibitHandler getDecoratedHandler(CSBusinessDelegateHandler base) {
        DecoratingHandler copy = decoratorPrototype.deepCopy();
        copy.setTarget(base);
        return copy;
    }

}
