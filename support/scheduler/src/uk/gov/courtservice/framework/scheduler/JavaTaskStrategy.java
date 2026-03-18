package uk.gov.courtservice.framework.scheduler;

import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: TaskStrategy implementation allowing the execution of plain java
 * classes implementing the JavaTask interface.
 * </p>
 * <p>
 * Description: Handles the execution of a java class that implements the
 * JavaTask interface and has a no arguments constructor. In future, more
 * complex TaskStrategy implementation that support complex constructor may be
 * implemented.
 * </p>
 * <p>
 * To use this strategy it must be supplied with the required property defined
 * in JAVA_TASK_CLASS, otherwise it will not be valid after initialisation.
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
public class JavaTaskStrategy implements TaskStrategy {
    private static Logger log = CSServices.getLogger(JavaTaskStrategy.class);

    /**
     * Property to set in order to define the class of the task. It must be an
     * implementation of JavaTask. There can be no default for this.
     */
    public static final String JAVA_TASK_CLASS = "class";

    private JavaTask task;

    /**
     * Initializer that instantiates the class implementing JavaTask and makes
     * it available for scheduling.
     */
    public void init(Properties props, Schedulable schedulable) {
        // Assume invalid unless otherwise specified...
        boolean valid = false;

        String name = schedulable.getName();

        String classname = props.getProperty(JAVA_TASK_CLASS);
        if (classname != null) {
            try {
                Class taskClass = Class.forName(classname);
                Object obj = taskClass.newInstance();
                task = (JavaTask) obj;
                // Only here can we be sure that initialisation has been
                // successful.
                valid = true;
            } catch (ClassNotFoundException cnfe) {
                log.error("Class defined in " + name + "." + JAVA_TASK_CLASS + " property does not exist.", cnfe);
            } catch (InstantiationException ie) {
                log.error("Class defined in " + name + "." + JAVA_TASK_CLASS + " property could not be instantiated.",
                        ie);
            } catch (IllegalAccessException iae) {
                log.error("Class defined in " + name + "." + JAVA_TASK_CLASS + " property could not be accessed.", iae);
            } catch (ClassCastException cce) {
                log.error("Class defined in " + name + "." + JAVA_TASK_CLASS
                        + " property is not an instance of ScheduledTask.", cce);
            }
        } else {
            log.error("There is no class defined by " + name + "." + JAVA_TASK_CLASS + " property.");
        }
        schedulable.setValid(valid);
    }

    /**
     * Actually executes the ScheduledTask implementation.
     */
    public void executeTask() {
        task.doTask();
    }

    /**
     * Cleans up
     */
    public void cleanup() {
        if (task instanceof Stoppable) {
            ((Stoppable) task).stop();
        }
    }

}