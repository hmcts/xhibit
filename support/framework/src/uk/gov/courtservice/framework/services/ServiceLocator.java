package uk.gov.courtservice.framework.services;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

/**
 * <p>
 * Title: LocatorServices
 * </p>
 * <p>
 * Description: Provides a base implementation of services which locate EJBs
 * from the naming conventions for binding these objects into the JNDI tree. A
 * number of additonal services are alss provided by the EJBServices interface.
 * It is expected that this interface will primarily be used by the
 * BusinessDelegate to locate sessin beans
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
public interface ServiceLocator {

    /**
     * @returns the InitialContext
     */
    public InitialContext getInitialContext();

    /**
     * @returns the DataSource
     * @throws CSServicesException
     */
    public DataSource getDataSource();

    /**
     * @returns the DataSource
     * @throws CSServicesException
     */
    public DataSource getDataSource(String name);
    
    /**
     * Gets an EJBLocalHome home object for the home associated with the class
     * So for the Defendant Entity Bean the DefendantRemote interface is passed
     * in and the jhe JNDI name DefendantRemote will be used for the lookkup. If
     * DefendantController.class is passed in the JNDI name DefendantContoller
     * will be used for the lookup. The JNDI lookup will be performed only from
     * the configured subcontext for this framework application.
     * 
     * @param homeClass
     *            the Class to perform the lookup with.
     * @returns the EJBLocalHome for the found ejb
     * @throws a
     *             CSResourceUnavailableException if the ejb cannot be located
     */
    public EJBLocalHome getLocalHome(Class homeClass);

    /**
     * Gets an EJBHome home object for the home associated with the class So for
     * the Defendant Entity Bean the DefendantRemote interface is passed in and
     * the jhe JNDI name DefendantRemote will be used for the lookkup. If
     * DefendantController.class is passed in the JNDI name DefendantContoller
     * will be used for the lookup. The JNDI lookup will be performed only from
     * the configured subcontext for this framework application.
     * 
     * @param klass
     *            the Class to perform the lookup with.
     * @returns the EJBHome for the found ejb
     * @throws a
     *             CSResourceUnavailableException if the ejb cannot be located
     */
    public EJBHome getRemoteHome(Class homeClass);

    /**
     * Uses the initial context to look up for the QueueConnectionFactory
     * specified. QueueConnectionFactory is used to create a QueueConnection,
     * e.g. JMSQueueAppender, which is necessary in order to obtain a
     * QueueSession or to create a message, a QueueSender, or a QueueReciever.
     * 
     * @param qcfBindingName
     *            the String to perform the lookup with
     * @return queueConnectionFactory the QueueConnectionFactory
     * @throws CSResourceUnavailableException
     */
    public QueueConnectionFactory getQueueConnectionFactory(String qcfBindingName);

    /**
     * Uses the initial context to look up for the Queue specified. Queue is
     * used to create a QueueSender when a QueueConnection is already
     * established.
     * 
     * @param qBindingName
     *            the String to perform the lookup with
     * @return queue the Queue
     * @throws CSResourceUnavailableException
     */
    public Queue getQueue(String qBindingName);

    /**
     * Uses the initial context to look up for the TopicConnectionFactory
     * specified.
     * 
     * @param tcfBindingName
     *            the String to perform the lookup with
     * @return topicConnectionFactory the TopicConnectionFactory
     * @throws CSResourceUnavailableException
     */
    public TopicConnectionFactory getTopicConnectionFactory(String tcfBindingName);
    
    /**
     * Uses the initial context to look up for the Topoc specified.
     * 
     * @param tBindingName
     *            the String to perform the lookup with
     * @return topic the Topic
     * @throws CSResourceUnavailableException
     */
    public Topic getTopic(String tBindingName);

    /**
     * Uses the initial context to look up for the TopicConnectionFactory
     * specified.
     * 
     * @param cfBindingName
     *            the String to perform the lookup with
     * @return topicConnectionFactory the TopicConnectionFactory
     * @throws CSResourceUnavailableException
     */
    public ConnectionFactory getConnectionFactory(String cfBindingName);
    
    /**
     * Uses the initial context to look up for the Topoc specified.
     * 
     * @param dBindingName
     *            the String to perform the lookup with
     * @return topic the Topic
     * @throws CSResourceUnavailableException
     */
    public Destination getDestination(String dBindingName);
    
    
    
    
    
    /**
     * Gets a user transaction
     * 
     * @return User Transaction
     */
    public UserTransaction getUserTx();

}
