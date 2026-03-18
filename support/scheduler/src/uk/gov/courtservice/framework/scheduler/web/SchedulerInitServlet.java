package uk.gov.courtservice.framework.scheduler.web;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.scheduler.Scheduler;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Initialisation servlet for a scheduler web application.
 * </p>
 * <p>
 * Description: Servlet called at instantiation of a scheduler web application.
 * Responsible for retrieving properties and initialising the scheduler. In
 * future can be configured to provide much more control over the scheduler.
 * </p>
 * <p>
 * This class requires complex configuration as it is used both as a context
 * listener and as a servlet. This is required as it needs to create the
 * scheduler with a valid subject and not destroy it untill the context is
 * destroyed.
 * </p>
 * <p>
 * The first of these criteria is met by starting it as a servlet (use
 * load-on-start and run-as tags in config).
 * </p>
 * <p>
 * The second of these criteria is met by destroying it in the destoryContext
 * handler of the context listener. The servlet destroy method is unsafe as a
 * servlet can be destroyed before the context to free up resources.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @see Scheduler
 * @author Bob Boothby, Will Fardell
 */
public class SchedulerInitServlet extends HttpServlet implements ServletContextListener {
    private static String DISABLE_SCHEDULER_SERVLET_KEY = "disableSchedulerServlet";

    private static Logger log = CSServices.getLogger(SchedulerInitServlet.class);

    /**
     * Lock used to synchornize creation and destruction of scheduler
     */
    private static final Object schedularLock = new Object();

    /**
     * The position in the web application where this servlet expects to find
     * the configuration file.
     */
    public static final String SCHEDULER_PROPERTIES_PATH = "/WEB-INF/scheduler.properties";

    /**
     * The key the scheduler is stored under in the context
     */
    public static final String SCHEDULER_CONTEXT_KEY = "SchedulerInitServlet.scheduler";

    /**
     * Called by the servlet container to indicate to a servlet that the servlet
     * is being placed into service.
     */
    public void init() {
        if (schedulerServletDisabled()) {
            log.info("Scheduler has been disabled (system property " + DISABLE_SCHEDULER_SERVLET_KEY + " = true).");
        } else {
            ServletContext sc = getServletContext();

            synchronized (schedularLock) {
                Scheduler scheduler = getScheduler(sc);
                if (scheduler == null) {
                    scheduler = new Scheduler(loadProperties(SCHEDULER_PROPERTIES_PATH));
                    scheduler.start();
                    setScheduler(sc, scheduler);
                }
            }
        }
    }

    /**
     * Called by the servlet container to indicate to a servlet that the servlet
     * is being taken out of service
     */
    public void destroy() {
        // Do nothing
    }

    /**
     * ServletContextListener implementation: Notification that the web
     * application is ready to process requests.
     */
    public void contextInitialized(ServletContextEvent sce) {
        // Do nothing
    }

    /**
     * ServletContextListener implementation: Notification that the servlet
     * context is about to be shut down.
     */
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();

        synchronized (schedularLock) {
            Scheduler scheduler = getScheduler(sc);
            if (scheduler != null) {
                scheduler.cleanup();
                setScheduler(sc, null);
            }
        }
    }

    /**
     * Get the Scheduler from the serlet context
     */
    private Scheduler getScheduler(ServletContext sc) {
        return (Scheduler) sc.getAttribute(SCHEDULER_CONTEXT_KEY);
    }

    /**
     * Set the Scheduler in the serlet context
     */
    private void setScheduler(ServletContext sc, Scheduler scheduler) {
        sc.setAttribute(SCHEDULER_CONTEXT_KEY, scheduler);
    }

    /**
     * Load the properties from the specified resource
     */
    private Properties loadProperties(String resourceName) {
        Properties properties = new Properties();
        try {
            InputStream in = getServletContext().getResourceAsStream(SCHEDULER_PROPERTIES_PATH);
            if (in != null) {
                properties.load(in);
            }
        } catch (java.io.IOException ioe) {
            log.info("Failed to load scheduling properties file. No scheduled tasks will occur.", ioe);
        }
        return properties;
    }

    /**
     * Return true if the scheduler servlet has been disabled by system property
     */
    private boolean schedulerServletDisabled() {
        String property = System.getProperty(DISABLE_SCHEDULER_SERVLET_KEY);
        return property != null && property.equalsIgnoreCase("TRUE");
    }

}