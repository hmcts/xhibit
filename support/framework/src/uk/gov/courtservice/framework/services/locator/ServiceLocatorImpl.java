package uk.gov.courtservice.framework.services.locator;

import java.security.PrivilegedActionException;
import java.util.Hashtable;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.security.ServerRepository;
import uk.gov.courtservice.framework.security.SubjectManager;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.ConfigServices;
import uk.gov.courtservice.framework.services.ServiceLocator;

/**
 * <p>
 * Title: LocatorServicesImpl
 * </p>
 * <p>
 * Description: Implementation of LocatorServices provides basic lookup
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @version 1.0
 */
public abstract class ServiceLocatorImpl implements ServiceLocator {

    /** Property set service locator eager loading */
    private static final String LOCATOR_EAGER_LOAD = "locator.eager.load";

    /** Config services */
    private static ConfigServices config = CSServices.getConfigServices();

    /** Default server */
    private static final String DEFAULT_SERVER = config.getProperty("defaultServer");

    /** Default datasource JNDI name */
    private static final String DS_NAME = CSServices.getConfigServices().getProperty("xhibit.datasourcename");

    /** A reference to the logger */
    private static Logger log = CSServices.getLogger(ServiceLocatorImpl.class);

    /** Cache for service locators */
    private static ServiceLocator self;

    static {
        boolean eagerLoad = config.getProperty(LOCATOR_EAGER_LOAD) != null;
        if (eagerLoad) {
            self = new EagerLoadingServiceLocatorImpl();
            log.info("Eager loading service locator");
        } else {
            self = new LazyLoadingServiceLocatorImpl();
            log.info("Lazy loading service locator");
        }
    }

    /**
     * Returns the service locator for unauthenticated access to the default
     * server
     * 
     * @return the instance of ServiceLocator class
     */
    public static ServiceLocator getInstance() {
        return self;
    }

    /**
     * Gets the initail context
     * 
     * @return
     */
    public InitialContext getInitialContext() {
        try {
            return (InitialContext) SubjectManager.getInstance().runAs(new ContextCreator(getEnv()));
        } catch (PrivilegedActionException e) {
            throw handleException(e);
        }
    }

    /**
     * Gets the default datasource
     * 
     * @return
     */
    public DataSource getDataSource() {
        return getDataSource(null);
    }

    /**
     * Gets the default datasource
     * 
     * @return
     */
    public DataSource getDataSource(String databaseName) {
        String dsJndiName;
        if (databaseName != null) {
            dsJndiName = CSServices.getConfigServices().getProperty(databaseName + ".datasourcename");
            if (dsJndiName == null) {
                throw new CSUnrecoverableException("Could not find datasource name for database \"" + databaseName
                        + "\".");
            }
        } else {
            dsJndiName = DS_NAME;
        }

        return (javax.sql.DataSource) lookup(dsJndiName);
    }

    /**
     * Gets a remote EJB
     * 
     * @param homeClass
     * @return
     */
    public EJBHome getRemoteHome(Class homeClass) {
        Object homeObject = lookup(getHomeJndiName(homeClass));
        return (EJBHome) PortableRemoteObject.narrow(homeObject, homeClass);
    }

    /**
     * Gets a local EJB
     * 
     * @param homeClass
     * @return
     */
    public EJBLocalHome getLocalHome(Class homeClass) {
        return (EJBLocalHome) lookup(getHomeJndiName(homeClass));
    }

    /**
     * Gets the queue connection factory
     * 
     * @param qcfBindingName
     * @return
     */
    public QueueConnectionFactory getQueueConnectionFactory(String qcfBindingName) {
        return (QueueConnectionFactory) lookup(qcfBindingName);
    }

    /**
     * Gets the queue
     * 
     * @param queueBindingName
     * @return
     */
    public Queue getQueue(String queueBindingName) {
        return (Queue) lookup(queueBindingName);
    }

    /**
     * Gets the topic connection factory
     * 
     * @param tcfBindingName
     * @return
     */
    public TopicConnectionFactory getTopicConnectionFactory(String tcfBindingName) {
        return (TopicConnectionFactory) lookup(tcfBindingName);
    }

    /**
     * Gets the topic
     * 
     * @param topicBindingName
     * @return
     */
    public Topic getTopic(String topicBindingName) {
        return (Topic) lookup(topicBindingName);
    }
    
    /**
     * Gets the connection factory
     * 
     * @param cfBindingName
     * @return
     */
    public ConnectionFactory getConnectionFactory(String cfBindingName) {
        return (ConnectionFactory) lookup(cfBindingName);
    }

    /**
     * Gets the destination
     * 
     * @param dBindingName
     * @return
     */
    public Destination getDestination(String dBindingName) {
        return (Destination) lookup(dBindingName);
    } 
    
    /**
     * Gets a user transaction
     * 
     * @return User Transaction
     */
    public UserTransaction getUserTx() {
        Context ctx = this.getInitialContext();
        try {
            return (UserTransaction) ctx.lookup("java:comp/UserTransaction");
        } catch (NamingException ex) {
            throw new CSUnrecoverableException(ex);
        } finally {
            try {
                ctx.close();
            } catch (NamingException e1) {
                log.fatal(e1.getMessage(), e1);
            }
        }
    }

    /**
     * Utility method to populate the JNDI properties
     * 
     * @return
     */
    protected Hashtable getEnv() {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, ServerRepository.getInitialContextFactory(DEFAULT_SERVER));
        env.put(Context.PROVIDER_URL, ServerRepository.getProviderURL(DEFAULT_SERVER));

        return env;
    }

    /**
     * Utility method to lookup objects
     * 
     * @param serverName
     * @return
     */
    protected abstract Object lookup(String jndiName);

    /**
     * Handles exception
     * 
     * @param ex
     */
    protected static CSUnrecoverableException handleException(PrivilegedActionException ex) {
        CSServices.getDefaultErrorHandler().handleError(ex, ServiceLocatorImpl.class);
        return new CSUnrecoverableException(ex.getException());
    }

    /**
     * Gets the home JNDI name
     * 
     * @param clazz
     * @return
     */
    private static String getHomeJndiName(Class clazz) {
        String fullClassName = clazz.getName();
        int lastDotIndex = fullClassName.lastIndexOf('.');

        return fullClassName.substring(lastDotIndex + 1, fullClassName.length());
    }
}