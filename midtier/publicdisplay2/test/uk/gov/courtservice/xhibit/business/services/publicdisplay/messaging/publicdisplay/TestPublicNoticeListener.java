//package uk.gov.courtservice.xhibit.business.services.publicdisplay.messaging.publicdisplay;
//
//import javax.jms.DeliveryMode;
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.ObjectMessage;
//import javax.jms.Topic;
//import javax.jms.TopicConnection;
//import javax.jms.TopicConnectionFactory;
//import javax.jms.TopicPublisher;
//import javax.jms.TopicSession;
//import javax.jms.TopicSubscriber;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicNoticeEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayJMSConstants;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
///**
// * <p>Title: Test for the PublicNoticeListener component.</p>
// * <p>Description: </p>
// * <p>
// * This unit test checks that the <code>PublicNoticeListener</code> message driven
// * bean successfully posts a <code>PublicNoticeEvent</code> to the PublicDisplay topic
// * for a given input message on the old PublicNoticeNotification topic.
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author Bob Boothby
// * @version 1.0
// */
//public class TestPublicNoticeListener extends TestCase
//{
//    private static final String INPUT_TOPIC_NAME = "PublicNoticeNotification";
//
//    //The topic on which the 'incoming' message will be posted.
//    private Topic inputTopic;
//
//    //The topic on which the resulting message will be posted.
//    private Topic outputTopic;
//
//    //Used to set up the JMS connections...
//    private TopicConnection topicConnection;
//    private TopicSession topicSession;
//
//    /**
//     * Construct an instance of the test.
//     * @param s The name of the instance of the test.
//     */
//    public TestPublicNoticeListener(String s)
//    {
//        super(s);
//    }
//
//    /**
//     * Set up for the test.
//     * @throws JMSException When there is a problem with the JMS server...
//     */
//    protected void setUp() throws JMSException
//    {
//        //Get the topics.
//        inputTopic = CSServices.getServiceLocator().getTopic(
//                INPUT_TOPIC_NAME);
//        outputTopic = CSServices.getServiceLocator().getTopic(
//                PublicDisplayJMSConstants.DEFAULT_DESTINATION);
//
//        //Set up a basic topic connection.
//        TopicConnectionFactory topicConnectionFactory =
//                CSServices.getServiceLocator().getTopicConnectionFactory(
//                PublicDisplayJMSConstants.DEFAULT_TCF);
//        topicConnection = topicConnectionFactory.createTopicConnection();
//
//        //Create a non-transactional auto acknowledged topic session.
//        topicSession = topicConnection.createTopicSession(false,
//                TopicSession.AUTO_ACKNOWLEDGE);
//
//        //Start the connection.
//        topicConnection.start();
//    }
//
//    /**
//     * Tidy up after the test.
//     * @throws JMSException When there is a problem with the JMS server...
//     */
//    protected void tearDown() throws JMSException
//    {
//        //Close the connection.
//        topicConnection.close();
//    }
//
//    /**
//     * Check that a message incoming on the PublicNoticeNotification topic
//     * is successfully turned into a message containing a <code>PublicNoticeEvent</code>
//     * on the PublicDisplay topic.
//     * @throws JMSException When there is a problem with the JMS server...
//     */
//    public void testMessageTransfer() throws JMSException
//    {
//        TopicPublisher inputTopicPublisher = null;
//        TopicSubscriber outputTopicSubscriber = null;
//        try
//        {
//            //Make ready to publish the test message to the 'incoming' topic
//            //for collection by the PublicNoticeListener.
//            inputTopicPublisher = topicSession.createPublisher(inputTopic);
//
//            //Make ready to receive the resultant message posted by the
//            //PublicNoticeListener.
//            outputTopicSubscriber = topicSession.createSubscriber(
//                    outputTopic,
//                    PublicDisplayJMSConstants.COURT_ID_PROPERTY_NAME + " = 3",
//                    false);
//
//            //Create and publish the test message.
//            ObjectMessage inputMessage = topicSession.createObjectMessage(
//                    getMessageValue());
//            inputTopicPublisher.publish(
//                    inputMessage,
//                    DeliveryMode.NON_PERSISTENT,
//                    4,
//                    20000L);
//
//            //Get the resulting message.
//            Message outputMessage = outputTopicSubscriber.receive(20000L);
//
//            //Check validity of output.
//            assertNotNull(
//                    "Did not receive the expected message from the output topic.",
//                    outputMessage);
//            assertTrue("The message is not an instance of ObjectMessage.",
//                       outputMessage instanceof ObjectMessage);
//            ObjectMessage outputObjectMessage = (ObjectMessage) outputMessage;
//            assertTrue("The message does not contain an instance of PublicNoticeEvent.",
//                       outputObjectMessage.getObject() instanceof PublicNoticeEvent);
//        }
//        finally
//        {
//            if(inputTopicPublisher != null)
//                inputTopicPublisher.close();
//        }
//    }
//
//    /**
//     * Construct an instance of <code>CourtLogSubscriptionValue</code> for the
//     * message to be tested.
//     * @return an instance of <code>CourtLogSubscriptionValue</code>
//     */
//    private CourtLogSubscriptionValue getMessageValue()
//    {
//        CourtLogSubscriptionValue value = new CourtLogSubscriptionValue();
//        value.setCourtSiteId(new Integer(3));
//        value.setCourtRoomId(new Integer(31));
//        CourtLogViewValue courtLogViewValue = new CourtLogViewValue();
//        courtLogViewValue.setCaseId(new Integer(1));
//        value.setCourtLogViewValue(courtLogViewValue);
//        return value;
//    }
//}
//