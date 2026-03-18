//package uk.gov.courtservice.xhibit.common.publicdisplay.jms;
//
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.Topic;
//import javax.jms.TopicConnection;
//import javax.jms.TopicConnectionFactory;
//import javax.jms.TopicSession;
//import javax.jms.TopicSubscriber;
//import javax.naming.NamingException;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
//
///**
// * <p>
// * Title: Unit Test for PublicDisplayNotifier
// * </p>
// * <p>
// * Description:
// * </p>
// * <p>
// * Tests the <code>PublicDisplayNotifier</code> using message receipt both
// * with and without message selectors.
// * </p>
// * <p>
// * Copyright: Copyright (c) 2003
// * </p>
// * <p>
// * Company:
// * </p>
// *
// * @author Bob Boothby
// * @version 1.0
// */
//public class TestPublicDisplayNotifier extends TestCase {
//    PublicDisplayNotifier publicdisplaynotifier;
//
//    Topic topic;
//
//    TopicConnection topicConnection;
//
//    TopicSession topicSession;
//
//    /**
//     * Create an instant of the unit test.
//     *
//     * @param s
//     *            The name of the test.
//     */
//    public TestPublicDisplayNotifier(String s) {
//        super(s);
//    }
//
//    /**
//     * Initialise the tests.
//     *
//     * @throws NamingException
//     *             When there is a problem in looking up a distributed object.
//     * @throws JMSException
//     *             When there is a problem with the JMS Server.
//     */
//    protected void setUp() throws NamingException, JMSException {
//        topic = CSServices.getServiceLocator().getTopic(PublicDisplayJMSConstants.DEFAULT_DESTINATION);
//
//        TopicConnectionFactory topicConnectionFactory = CSServices.getServiceLocator().getTopicConnectionFactory(
//                PublicDisplayJMSConstants.DEFAULT_TCF);
//
//        topicConnection = topicConnectionFactory.createTopicConnection();
//
//        topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
//
//        topicConnection.start();
//
//        publicdisplaynotifier = new PublicDisplayNotifier(CSServices.getServiceLocator().getInitialContext()
//                .getEnvironment());
//    }
//
//    /**
//     * Tidy up after the test.
//     *
//     * @throws JMSException
//     *             When there is a problem with the JMS server.
//     */
//    protected void tearDown() throws JMSException {
//        topicConnection.close();
//        publicdisplaynotifier.close();
//    }
//
//    /**
//     * Tests the basic fact that the <code>PublicDisplayNotifier</code> will
//     * send a message via JMS.
//     *
//     * @throws JMSException
//     *             When there is a problem with the JMS server.
//     */
//    public void testSendMessageNoSelector() throws JMSException {
//        CourtConfigurationChange courtConfigurationChange = new CourtConfigurationChange(1, false);
//        ConfigurationChangeEvent configurationChangeEvent = new ConfigurationChangeEvent(courtConfigurationChange);
//        TopicSubscriber topicSubscriber = null;
//        try {
//            topicSubscriber = topicSession.createSubscriber(topic, "", false);
//            publicdisplaynotifier.sendMessage(configurationChangeEvent);
//            Message message = topicSubscriber.receive(5000L);
//            assertNotNull("Did not receive the expected message", message);
//        } finally {
//            if (topicSubscriber != null)
//                topicSubscriber.close();
//        }
//    }
//
//    /**
//     * Tests the that the <code>PublicDisplayNotifier</code> will send a
//     * message via JMS that can be filtered using a message selector.
//     *
//     * @throws JMSException
//     *             When there is a problem with the JMS server.
//     */
//    public void testSendMessageWithSelector() throws JMSException {
//        CourtConfigurationChange courtConfigurationChange1 = new CourtConfigurationChange(1, false);
//        ConfigurationChangeEvent configurationChangeEvent1 = new ConfigurationChangeEvent(courtConfigurationChange1);
//
//        CourtConfigurationChange courtConfigurationChange2 = new CourtConfigurationChange(2, false);
//        ConfigurationChangeEvent configurationChangeEvent2 = new ConfigurationChangeEvent(courtConfigurationChange2);
//
//        TopicSubscriber topicSubscriber = null;
//        try {
//            topicSubscriber = topicSession.createSubscriber(topic, PublicDisplayJMSConstants.COURT_ID_PROPERTY_NAME
//                    + " = 1", false);
//
//            publicdisplaynotifier.sendMessage(configurationChangeEvent2);
//            Message message = topicSubscriber.receive(1000L);
//            assertNull("Received message when expecting none.", message);
//            publicdisplaynotifier.sendMessage(configurationChangeEvent1);
//            message = topicSubscriber.receive(1000L);
//            assertNotNull("Did not receive the expected message", message);
//
//        } finally {
//            if (topicSubscriber != null)
//                topicSubscriber.close();
//        }
//
//    }
//}