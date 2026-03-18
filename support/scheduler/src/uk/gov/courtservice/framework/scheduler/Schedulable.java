package uk.gov.courtservice.framework.scheduler;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Class that handles the configuration and execution of tasks to be
 * executed in a scheduled manner.
 * </p>
 * <p>
 * Description: This class uses the strategy pattern to allow the scheduled
 * execution of tasks implemented in many manners.
 * </p>
 * <p>
 * Properties are passed in by the 'user' of this class defining the required
 * scheduling, execution strategy and it's configuration. The properties defined
 * within Schedulable all have defaults, further properties may be defined by
 * the individual strategies that may or may not have defaults.
 * </p>
 * <p>
 * Maintains an internal instance of java.util.Timer as we need to be able to
 * support longer running tasks that if run on a shared timer may hog the shared
 * Timer's execution thread.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @see java.util.Timer
 * @see TaskStrategy
 */
public class Schedulable {
    // Setup the contained timer as a Daemon.
    private Timer timer = new Timer(true);

    // The implementation of the task executor.
    private TaskStrategy taskStrategy;

    private static Logger log = CSServices.getLogger(Schedulable.class);

    private InternalTimerTask internalTimerTask = null;

    /**
     * Property that needs setting for determining whether the task is to run at
     * a fixed rate or not. Fixed rate is best defined by java.util.Timer
     * 
     * @see java.util.Timer
     * @see FIXED_RATE_DEFAULT
     */
    public static final String FIXED_RATE = "fixedrate";

    /**
     * Default value for the fixed rate property.
     * 
     * @see FIXED_RATE
     */
    public static final String FIXED_RATE_DEFAULT = "false";

    private boolean fixedRate = false;

    /**
     * Property that needs setting for determining the delay in ms before
     * initial execution of the task.
     * 
     * @see DELAY_DEFAULT
     */
    public static final String DELAY = "delay";

    /**
     * Default value for the delay property.
     * 
     * @see DELAY
     */
    public static final String DELAY_DEFAULT = "0";

    private long delay = 0L;

    /**
     * Property that needs setting for determining the period in ms between
     * executions of the task. If the period is 0, the task will run once and
     * terminate.
     * 
     * @see PERIOD_DEFAULT
     */
    public static final String PERIOD = "period";

    /**
     * Default value for the period.
     * 
     * @see PERIOD
     */
    public static final String PERIOD_DEFAULT = "0";

    private long period = 0L;

    /**
     * Property that specifies which strategy class to use in execution of the
     * task.
     * 
     * @see TaskStrategy
     * @see TASK_STRATEGY_DEFAULT
     */
    public static final String TASK_STRATEGY = "strategy";

    /**
     * Default value for the strategy class.
     * 
     * @see TASK_STRATEGY
     */
    public static final String TASK_STRATEGY_DEFAULT = JavaTaskStrategy.class.getName();

    private String name;

    private boolean valid = true;

    private boolean running = false;

    /**
     * This constructor that reads the basic scheduling properties before
     * passing the properties to the TaskStrategy implementation for
     * initialisation.
     * 
     * @see TaskStrategy.init()
     */
    protected Schedulable(String name, Properties props) {
        this.name = name;
        props.put("taskName", name);
        // No exception to be caught here as anything other than a
        // case insensitive 'true' is interpreted as false.
        fixedRate = Boolean.valueOf(props.getProperty(FIXED_RATE, FIXED_RATE_DEFAULT)).booleanValue();

        try {
            delay = Long.parseLong(props.getProperty(DELAY, DELAY_DEFAULT));
        } catch (NumberFormatException nfe) {
            valid = false;
            log.error("Failure parsing " + name + "." + DELAY + " Property.", nfe);
            delay = Long.parseLong(DELAY_DEFAULT);
        }

        try {
            period = Long.parseLong(props.getProperty(PERIOD, PERIOD_DEFAULT));
        } catch (NumberFormatException nfe) {
            valid = false;
            log.error("Failure parsing " + name + "." + PERIOD + " Property.", nfe);
            delay = Long.parseLong(PERIOD_DEFAULT);
        }

        String strategyClassname = props.getProperty(TASK_STRATEGY, TASK_STRATEGY_DEFAULT);
        try {
            Class strategyClass = Class.forName(strategyClassname);

            // Check that it is an instance of
            // TaskStrategy.
            if (TaskStrategy.class.isAssignableFrom(strategyClass)) {
                taskStrategy = (TaskStrategy) strategyClass.newInstance();
                taskStrategy.init(props, this);
            } else {
                valid = false;
                log.error("class defined in " + name + "." + TASK_STRATEGY + " is not an instance of TaskStrategy.");
            }
        } catch (ClassNotFoundException cnfe) {
            valid = false;
            log.error("Class defined in " + name + "." + TASK_STRATEGY + " property does not exist.", cnfe);
        } catch (InstantiationException ie) {
            log.error("Class defined in " + name + "." + TASK_STRATEGY + " property could not be instantiated.", ie);
        } catch (IllegalAccessException iae) {
            log.error("Class defined in " + name + "." + TASK_STRATEGY + " property could not be accessed.", iae);
        }
    }

    /**
     * This method returns whether the task schedule configuration is valid and
     * ready for scheduling.
     * 
     * @return Whether the task can be scheduled.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Method supplied for implementations of TaskStrategy to be able to set
     * their validity. Will not allow validity to be set to true if already
     * false.
     * 
     * @param validity
     *            boolean value indicating how to attempt to set validity
     * @return the validity of the whole Schedulable including the TaskStrategy.
     */
    public final boolean setValid(boolean validity) {
        // If it is not valid after the Schedulable constructor, then
        // it will never be valid.
        valid = valid & validity;
        return valid;
    }

    /**
     * @return the name of this particular Schedulable.
     */
    public String getName() {
        return name;
    }

    /**
     * @return for repeating tasks, whether they are currently running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Call to start this schedulable.
     * 
     * @return true if successful.
     */
    public synchronized boolean start() {
        if (running || !valid) {
            return false;
        }

        // if scheduled to run ongoing.
        if (period > 0L)
            running = true;

        internalTimerTask = new InternalTimerTask(taskStrategy);

        if (fixedRate)
            timer.scheduleAtFixedRate(internalTimerTask, delay, period);
        else if (period == 0L)
            timer.schedule(internalTimerTask, delay);
        else
            timer.schedule(internalTimerTask, delay, period);

        return true;
    }

    /**
     * Call to stop this schedulable.
     */
    public synchronized void stop() {
        if (running)
            internalTimerTask.cancel();

        internalTimerTask = null;
        taskStrategy.cleanup();
    }

    /**
     * Private internal TimerTask descendant designed to pick up and execute the
     * instance of schedulable. NEVER to be exposed to the outside world.
     */
    private class InternalTimerTask extends TimerTask {
        private TaskStrategy taskStrategy;

        /**
         * Constructor, taking which Task Strategy it is to execute.
         * 
         * @param taskStrategy
         *            the TaskStrategy to execute.
         */
        private InternalTimerTask(TaskStrategy taskStrategy) {
            this.taskStrategy = taskStrategy;
        }

        /**
         * This run method catches ALL exceptions thrown by the underlying
         * implementation. They will be logged.
         */
        public void run() {
            long startTime = System.currentTimeMillis();
            try {
                taskStrategy.executeTask();
            } catch (Throwable t) {
                log.error("An exception was thrown during the execution of the task", t);
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                if(log.isDebugEnabled()) {
                    log.debug("Executing scheduled task " + name + " took " + duration + "ms.");
                }
            }
        }
    }
}