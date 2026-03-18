package uk.gov.courtservice.framework.services.ejb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.EJBServices;
import uk.gov.courtservice.framework.services.ServiceLocator;

/**
 * <p>
 * Title: EJBServicesImpl
 * </p>
 * <p>
 * Description: Implementation of the <code>EJBServices</code> interface.
 * Provides a range of utilites for finding and using EJBs. In certain
 * circumstances this can be used instead of the framework ServiceLocator and is
 * designed to provided additional functionality to the erviceLocator.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond much code taken from the work of Nick Lawson
 * @version 1.0
 */
public class EJBServicesImpl implements EJBServices {

    /** Logger */
    private static Logger log = CSServices.getLogger(EJBServicesImpl.class);

    /** Singleton instance */
    private static EJBServicesImpl instance = new EJBServicesImpl();

    /** Suffix for remote homes */
    // private static final String REMOTE_HOME = "RemoteHome";
    /** Suffix for local instance */
    // private static final String HOME = "Home";
    /** Service locator used by this EJB service */
    private ServiceLocator locator;

    /**
     * Private constructor
     */
    private EJBServicesImpl() throws CSUnrecoverableException {
        locator = CSServices.getServiceLocator();
        log.info("EJBServicesImpl created");
    }

    /**
     * Singleton accessor
     * 
     */
    public static EJBServicesImpl getInstance() {
        return instance;
    }

    /**
     * Creates a remote session
     */
    public EJBObject createRemoteSession(Class klass) {
        try {
            EJBHome home = locator.getRemoteHome(klass);

            Method createMethod = home.getClass().getMethod("create", new Class[] {});
            return (EJBObject) createMethod.invoke(home, new Object[] {});

        } catch (InvocationTargetException e) {
            throw handleException(e);
        } catch (IllegalAccessException e) {
            throw handleException(e);
        } catch (NoSuchMethodException e) {
            throw handleException(e);
        }
    }

    /**
     * see interface
     */
    public EJBLocalObject createLocalSession(Class klass) {
        try {
            EJBLocalHome home = locator.getLocalHome(klass);

            Method createMethod = home.getClass().getMethod("create", new Class[] {});
            return (EJBLocalObject) createMethod.invoke(home, new Object[] {});

        } catch (InvocationTargetException e) {
            throw handleException(e);
        } catch (IllegalAccessException e) {
            throw handleException(e);
        } catch (NoSuchMethodException e) {
            throw handleException(e);
        }
    }

    /**
     * see interface
     */
    public EJBLocalObject findLocalEntityByPrimaryKey(Class ejbLocalHome, Integer primaryKey)
            throws ObjectNotFoundException {
        try {
            EJBLocalHome home = locator.getLocalHome(ejbLocalHome);

            // find an instance:
            Class ejbHomeClass = home.getClass();
            Class[] methodArgs = new Class[] { Integer.class };
            Method findMethod = ejbHomeClass.getMethod("findByPrimaryKey", methodArgs);

            EJBLocalObject entity = (EJBLocalObject) findMethod.invoke(home, new Object[] { primaryKey });
            log.debug("Returning entity=" + entity);
            return entity;
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof ObjectNotFoundException) {
                throw (ObjectNotFoundException) e.getTargetException();
            } else {
                throw handleException(e);
            }
        } catch (NoSuchMethodException e) {
            throw handleException(e);
        } catch (IllegalAccessException e) {
            throw handleException(e);
        }
    }

    /**
     * see interface
     */
    public void deleteLocalEntity(Class ejbLocalHomeClass, Integer primaryKey) {
        try {
            // get a home and remove
            EJBLocalHome home = locator.getLocalHome(ejbLocalHomeClass);
            home.remove(primaryKey);
        } catch (EJBException e) {
            throw handleException(e);
        } catch (RemoveException e) {
            throw handleException(e);
        }
    }

    /**
     * Calculates the jndi name for a class base don the convention that each
     * class is bound to the jndi tree with the same name as its class.
     * 
     * @param klass
     *            the Class to find the name for
     * @returns the String for the JNDI name
     */
    public String getJndiName(Class klass) {
        String fullClassName = klass.getName();
        int lastDotIndex = fullClassName.lastIndexOf('.');
        String jndiName = fullClassName.substring(lastDotIndex + 1, fullClassName.length());
        log("Class=" + klass + " JNDIName=" + jndiName);
        return jndiName;
    }

    /**
     * see interface
     */
    public String getSerializedEJBObject(EJBObject session) {
        try {
            javax.ejb.Handle handle = session.getHandle();
            ByteArrayOutputStream fo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(fo);
            so.writeObject(handle);
            so.flush();
            so.close();
            return new String(fo.toByteArray());
        } catch (RemoteException ex) {
            throw handleException(ex);
        } catch (IOException ex) {
            throw handleException(ex);
        }
    }

    /**
     * see interface
     */
    public EJBObject getEJBObject(String id) throws CSUnrecoverableException {
        if (id == null) {
            throw new CSUnrecoverableException();
        }
        try {
            byte[] bytes = new String(id).getBytes();
            InputStream io = new ByteArrayInputStream(bytes);
            ObjectInputStream os = new ObjectInputStream(io);
            javax.ejb.Handle handle = (javax.ejb.Handle) os.readObject();
            return handle.getEJBObject();
        } catch (Exception ex) {
            CSUnrecoverableException x = new CSUnrecoverableException(ex);
            CSServices.getDefaultErrorHandler().handleError(x, EJBServicesImpl.class);
            throw x;
        }
    }

    /**
     * Utility method for logging
     * 
     * @param msg
     */
    private void log(String msg) {
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
    }

    /**
     * Utility method for handling exception
     * 
     * @param th
     * @return
     */
    private static CSUnrecoverableException handleException(Throwable th) {
        CSUnrecoverableException x = new CSUnrecoverableException(th);
        CSServices.getDefaultErrorHandler().handleError(x, EJBServicesImpl.class);
        return x;
    }

}