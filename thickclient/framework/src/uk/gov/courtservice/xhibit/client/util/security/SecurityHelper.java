package uk.gov.courtservice.xhibit.client.util.security;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

//import uk.gov.courtservice.xhibit.client.actions.FunctionList;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class SecurityHelper {
    private static final Logger log = CSServices.getLogger(SecurityHelper.class);

    private SecurityHelper() {
        // Change Permissions
    }

    public final static int SECURITY_READ = 0;

    public final static int SECURITY_EDIT = 1;

    public static ResourceBundle securityReadBundle = null;

    public static ResourceBundle securityEditBundle = null;

    /**
     * @todo For development this is true, but needs to be changed to false for
     *       integration testing
     */
	//public static final boolean defaultAccess = true;
    public static final boolean defaultAccess = false;

    public static boolean hasReadAccess(Class toCheck) {
        // return true;
        return hasAccess(toCheck, SECURITY_READ);
    }

    public static boolean hasEditAccess(Class toCheck) {
        //return true;
        return hasAccess(toCheck, SECURITY_EDIT);
    }

    private static boolean hasAccess(Class toCheck, int type) {
        String className = toCheck.getName().substring(toCheck.getName().lastIndexOf('.') + 1);

        // Check if the classname has a $ in it.
        // If so, it is an inner class and except from security.
        // It is assumed that the security will be handled by the owner class.
        if (className.indexOf("$") > 0)
            return true;

        ResourceBundle accessBundle = getSecurityBundle(type);
        if (accessBundle != null) {
            try {
                FunctionList access = (FunctionList) accessBundle.getObject(className);
                return FunctionList.hasAccess(access);
            } catch (MissingResourceException ex) {
                XHIBITConstant.error("SECURITY ERROR: The Class " + className
                        + " does not have a security assignment. Default security will be applied");
                // The action is not listed therefore drop out
                // and just return the default access
            } catch (Exception ex) {
                // Any other error, continue and return default action
                log.fatal(ex);
            }
        }
        return defaultAccess;
    }

    public static ResourceBundle getSecurityBundle(int securityType) {
        if (securityType == SECURITY_READ) {
            if (securityReadBundle == null) {
                securityReadBundle = new ReadMapping();
            }
            return securityReadBundle;
        }
        if (securityType == SECURITY_EDIT) {
            if (securityEditBundle == null) {
                securityEditBundle = new EditMapping();
            }
            return securityEditBundle;
        }
        return null;
    }

    public static FunctionList[] getFunctions() {
        List functionListList = new ArrayList();

        // Get the functions
        Field[] fields = FunctionList.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            int modifiers = fields[i].getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
                if (FunctionList.class.isAssignableFrom(fields[i].getType())) {
                    try {
                        functionListList.add(fields[i].get(null));
                    } catch (IllegalAccessException e) {
                        // Never thrown as already checked that its public
                    }
                }
            }
        }

        // Sort the functions
        Collections.sort(functionListList, new Comparator() {
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        // Return the functions
        return (FunctionList[]) functionListList.toArray(new FunctionList[functionListList.size()]);
    }

    public static String[] getEditActions(FunctionList function) {
        if (function == null) {
            throw new IllegalArgumentException("function: null");
        }
        return getActions(getSecurityBundle(SECURITY_EDIT), function);
    }

    public static String[] getReadActions(FunctionList function) {
        if (function == null) {
            throw new IllegalArgumentException("function: null");
        }
        return getActions(getSecurityBundle(SECURITY_READ), function);
    }

    private static String[] getActions(ResourceBundle bundle, FunctionList function) {
        List actionList = new ArrayList();

        Enumeration e = bundle.getKeys();
        while (e.hasMoreElements()) {
            String action = (String) e.nextElement();
            if (function.equals(bundle.getObject(action))) {
                actionList.add(action);
            }
        }

        Collections.sort(actionList);

        return (String[]) actionList.toArray(new String[actionList.size()]);
    }

}