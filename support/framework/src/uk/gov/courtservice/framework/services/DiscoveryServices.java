package uk.gov.courtservice.framework.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: DiscoveryServices
 * </p>
 * <p>
 * Description: Used to find concrete impelementations of services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DiscoveryServices.java,v 1.3 2006/06/05 12:30:18 bzjrnl Exp $
 */
public class DiscoveryServices {
    private static final Logger log = Logger.getLogger(DiscoveryServices.class);

    private static final DiscoveryServices instance = new DiscoveryServices();

    /**
     * Get the singleton instance of the service
     * 
     * @return the DiscoveryServices
     */
    public static DiscoveryServices getInstance() {
        return instance;
    }

    /**
     * Discover the concrete instance of the class, returns null if cant be
     * found. This allows a default to be created in a type safe manor.
     * 
     * @param clazz
     *            the cla
     * @return a new instance of the clazz or null if cant be found
     */
    public Object createInstance(Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz: null");
        }

        String className = getClassName(clazz);
        if (className != null) {
            try {
                Class concreteClazz = clazz.getClassLoader().loadClass(className);
                if (clazz.isAssignableFrom(concreteClazz)) {
                    Object object = concreteClazz.newInstance();
                    if (log.isDebugEnabled()) {
                        log.debug("Discovered concrete implementation " + className + " of " + clazz.getName() + ".");
                    }
                    return object;
                } else {
                    log.warn("Concrete implemenation " + className + ", not an instance of " + clazz.getName() + ".");
                }
            } catch (InstantiationException e) {
                log.warn("Could not create " + className + ".", e);
            } catch (IllegalAccessException e) {
                log.warn("Could not create " + className + ".", e);
            } catch (ClassNotFoundException e) {
                log.warn("Could not create " + className + ".", e);
            }
        } else {
            log.warn("Could not discover concrete implementation of " + clazz.getName() + ".");
        }

        return null;
    }

    private static String getClassName(Class clazz) {
        String className = getClassNameProperty(clazz);
        if (className != null) {
            return className;
        }
        className = getServiceClassNameIdentifier(clazz);
        if (className != null) {
            return className;
        }
        return null;
    }

    private static String getClassNameProperty(Class clazz) {
        String property = System.getProperty(clazz.getName());
        if (property != null) {
            String trimed = property.trim();
            if (trimed.length() > 0) {
                return trimed;
            }
        }
        return null;
    }

    private static String getServiceClassNameIdentifier(Class clazz) {
        URL url = clazz.getClassLoader().getResource("META-INF/services/" + clazz.getName());
        if (url != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                try {
                    String line = reader.readLine();
                    if (line != null) {
                        String trimed = line.trim();
                        if (trimed.length() > 0) {
                            return trimed;
                        }
                    }
                } finally {
                    closeSilently(reader);
                }
            } catch (IOException ioe) {
                log.warn("An error occured reading resource " + url + ".", ioe);
            }
        }
        return null;
    }

    private static void closeSilently(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ioe) {
            log.warn("An error occured closing the character stream.", ioe);
        }
    }

}
