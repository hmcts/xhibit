package uk.gov.courtservice.framework.scheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: TaskStrategy implementation that allows the execution of a RemoteTask
 * implemented by a session bean with a no arguments create method.
 * </p>
 * <p>
 * Description: Handles the execution of a Session Bean that implements the
 * RemoteTask interface with a no arguments constructor. In future an
 * implementation can easily be written to allow more complex create methods to
 * be used.
 * </p>
 * <p>
 * To use this class, it must be supplied with the required properties. It will
 * not initialise correctly if the session bean's remote interface does not
 * implement the RemoteTask interface.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 */
public final class RemoteSessionTaskStrategy implements TaskStrategy {
    private static Logger log = CSServices.getLogger(RemoteSessionTaskStrategy.class);

    // Instance variables containing appropriate references to
    // the EJB Home interface and the create method.
    private Object ejbHome;

    private Method ejbCreateMethod;

    private String taskName;
    
    /**
     * Property to set in order to define the home interface class of the
     * RemoteTask implementing session bean. There can be no default for this.
     */
    public static final String REMOTE_HOME_CLASS = "remotehome";

    /**
     * Property to set in order to define the JNDI lookup name for the
     * RemoteTask implementing Session Bean. There can be no default for this.
     */
    public static final String LOOKUP = "lookup";

    /**
     * Initialiser providing addditional configuration capabilities, runtime
     * checking and preparation for execution.
     */
    public void init(Properties props, Schedulable schedulable) {
        // Assume invalid unless otherwise specified...
        boolean valid = false;

        // Get the appropriate properties
        String remoteHomeClassname = props.getProperty(REMOTE_HOME_CLASS);
        String lookup = props.getProperty(LOOKUP);

        taskName = props.getProperty("taskName");
        log.debug("TaskName = "+taskName);
        
        String name = schedulable.getName();

        // Check that the properties are set.
        if (lookup != null) {
            if (remoteHomeClassname != null) {
                try {
                    Class remoteHomeClass = Class.forName(remoteHomeClassname);

                    // Get the create() method.
                    Class[] emptyParamTypes = new Class[0];
                    ejbCreateMethod = remoteHomeClass.getMethod("create", emptyParamTypes);
                    Class returnType = ejbCreateMethod.getReturnType();

                    // Check that it returns an instance of
                    // RemoteScheduledTask.
                    if (RemoteTask.class.isAssignableFrom(returnType)) {
                        // Get an instance of the home interface.
                        InitialContext ic = CSServices.getServiceLocator().getInitialContext();
                        Object obj = ic.lookup(lookup);
                        ejbHome = PortableRemoteObject.narrow(obj, remoteHomeClass);

                        // Only here can we be sure that initialisation has been
                        // successful.
                        valid = true;
                    } else {
                        log.error("Create() method for class defined in " + name + "." + REMOTE_HOME_CLASS
                                + " property does return an instance of RemoteTask.");
                    }
                } catch (ClassNotFoundException cnfe) {
                    log.error("Class defined in " + name + "." + REMOTE_HOME_CLASS + " property does not exist.", cnfe);
                } catch (NamingException ne) {
                    log.error("JNDI name defined in " + name + "." + LOOKUP + " property could not be found.", ne);
                } catch (ClassCastException cce) {
                    log.error("Object retrieved for JNDI name defined in " + name + "." + LOOKUP
                            + " property is not an instance of the class degined by " + name + "." + REMOTE_HOME_CLASS
                            + " property.", cce);
                } catch (NoSuchMethodException nsme) {
                    log.error("Class defined in " + name + "." + REMOTE_HOME_CLASS
                            + " property does not have a create() method.", nsme);
                }
            } else {
                log.error("There is no class defined by " + name + "." + REMOTE_HOME_CLASS + " property.");
            }
        } else {
            log.error("There is no jndi lookup defined by " + name + "." + LOOKUP + " property.");
        }
        schedulable.setValid(valid);
    }

    /**
     * Actually execute the task.
     */
    public void executeTask() {
        try {
            Object[] args = new Object[0];
            RemoteTask rt = (RemoteTask) ejbCreateMethod.invoke(ejbHome, args);
            log.debug("RemoteSessionTaskName: About to execute task "+taskName);
            rt.doTask(taskName);
        } catch (IllegalAccessException iae) {
            log.error("Could not access the create() method on the Remote Interface.", iae);
        } catch (InvocationTargetException ite) {
            log.error("Create() method threw an exception.", ite);
        } catch (java.rmi.RemoteException re) {
            log.error("A problem occurred in executing the remote task.", re);
        }
    }

    /**
     * Cleans up
     */
    public void cleanup() {
    }
}