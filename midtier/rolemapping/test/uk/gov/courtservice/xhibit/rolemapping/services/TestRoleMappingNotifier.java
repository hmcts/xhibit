//package uk.gov.courtservice.xhibit.rolemapping.services;
//
//import javax.jms.JMSException;
//import javax.jms.ObjectMessage;
//import javax.jms.Session;
//import javax.jms.Topic;
//import javax.jms.TopicConnection;
//import javax.jms.TopicConnectionFactory;
//import javax.jms.TopicSession;
//import javax.jms.TopicSubscriber;
//import javax.naming.NamingException;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.services.CSServices;
//
///**
// * Test class for the role mapping notifier.
// *
// * @author tz0d5m
// * @version $Id: TestRoleMappingNotifier.java,v 1.2 2006/07/13 12:58:02 xzfdtb Exp $
// *
// * @see uk.gov.courtservice.xhibit.rolemapping.services.RoleMappingNotifier
// */
//public class TestRoleMappingNotifier extends TestCase
//{
//    RoleMappingNotifier roleMappingNotifier = null;
//    Topic topic;
//    TopicConnection topicConnection;
//    TopicSession topicSession;
//
//    /**
//     * Create an instant of the unit test.
//     * @param s The name of the test.
//     */
//    public TestRoleMappingNotifier(String name)
//    {
//        super(name);
//    }
//
//    /**
//     * Initialise the tests.
//     * @throws NamingException When there is a problem in looking up a
//     * distributed object.
//     * @throws JMSException When there is a problem with the JMS Server.
//     */
//    protected void setUp() throws NamingException, JMSException
//    {
//        topic = CSServices.getServiceLocator().getTopic(
//                        RoleMappingNotifier.DESTINATION);
//
//        TopicConnectionFactory topicConnectionFactory =
//                CSServices.getServiceLocator().getTopicConnectionFactory(
//                        RoleMappingNotifier.CONNECTION_FACTORY);
//
//        topicConnection = topicConnectionFactory.createTopicConnection();
//
//        topicSession = topicConnection.createTopicSession(false,
//                Session.AUTO_ACKNOWLEDGE);
//
//        topicConnection.start();
//
//        this.roleMappingNotifier = new RoleMappingNotifier(
//                CSServices.getServiceLocator().getInitialContext().getEnvironment());
//    }
//
//    /**
//     * Tidy up after the test.
//     * @throws JMSException When there is a problem with the JMS server.
//     */
//    protected void tearDown() throws JMSException
//    {
//        this.topicConnection.close();
//        this.roleMappingNotifier.close();
//    }
//
//    /**
//     * Tests the basic fact that the <code>RoleMappingNotifier</code> will
//     * send a message via JMS.
//     *
//     * @throws JMSException When there is a problem with the JMS server.
//     */
//    public void testSendMessage() throws JMSException
//    {
//        TopicSubscriber topicSubscriber = null;
//        try
//        {
//            final String testString = "hello world";
//
//            topicSubscriber = topicSession.createSubscriber(topic, null, false);
//            roleMappingNotifier.sendMessage(testString);
//
//            ObjectMessage message = (ObjectMessage) topicSubscriber.receive(1000L);
//            assertEquals("Unexpected results", message.getObject(), testString);
//        }
//        finally
//        {
//            if (topicSubscriber != null)
//            {
//                topicSubscriber.close();
//            }
//        }
//    }
//}
//