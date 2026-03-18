//package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.test;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.MessagingMode;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.P2PSubscription;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.PubSubSubscription;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.Subscription;
//import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayJMSConstants;
//
//import javax.jms.*;
//import javax.naming.Context;
//
///**
// * @author pznwc5
// *
// * To change the template for this generated type comment go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestSubscription extends TestCase
//{
//
//	private ConnectionFactory cf;
//	private Queue q;
//	private Topic t;
//
//	public TestSubscription(String s)
//		throws Exception
//	{
//		super(s);
//	}
//
//	public void setUp()
//		throws Exception
//	{
//		Context ctx = CSServices.getServiceLocator().getInitialContext();
//
//		cf = (ConnectionFactory)ctx.lookup("CSJMSConnectionFactory");
//		q = (Queue)ctx.lookup("TestQueue");
//		t = (Topic)ctx.lookup("TestTopic");
//
//		ctx.close();
//
//	}
//
//	public void testGetSubscription()
//		throws Exception
//	{
//		Subscription p2p = Subscription.getSubscription(q, cf, -1, MessagingMode.P2P);
//		assertNotNull(p2p);
//		assertTrue(p2p instanceof P2PSubscription);
//
//		Subscription pubSub = Subscription.getSubscription(q, cf, -1, MessagingMode.PUB_SUB);
//		assertNotNull(pubSub);
//		assertTrue(pubSub instanceof PubSubSubscription);
//	}
//
//	public void testStartAndClose()
//		throws Exception
//	{
//		Subscription p2p = Subscription.getSubscription(q, cf, -1, MessagingMode.P2P);
//		p2p.start();
//		p2p.close();
//
//		Subscription pubSub = Subscription.getSubscription(q, cf, -1, MessagingMode.PUB_SUB);
//		pubSub.start();
//		pubSub.close();
//	}
//
//	public void testSetMessageListener()
//		throws Exception
//	{
//		QueueConnection senderConnection = ((QueueConnectionFactory)cf).createQueueConnection();
//		QueueSession senderSession = senderConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//		String senderText = new java.rmi.server.UID().toString();
//		TextMessage senderMsg = senderSession.createTextMessage(senderText);
//		senderMsg.setLongProperty(PublicDisplayJMSConstants.COURT_ID_PROPERTY_NAME, -1);
//		QueueSender sender = senderSession.createSender(q);
//		sender.send(senderMsg);
//
//		Subscription p2p = Subscription.getSubscription(q, cf, -1, MessagingMode.P2P);
//		TestMessageListener p2pMessageListener = new TestMessageListener();
//		p2p.setMessageListener(p2pMessageListener);
//		p2p.start();
//		TextMessage receiverMsg = null;
//		for(int i = 0;i < 6;i++)
//		{
//			receiverMsg = (TextMessage)p2pMessageListener.getMessage();
//			if(receiverMsg != null) break;
//			Thread.sleep(2000);
//		}
//		assertNotNull(receiverMsg);
//		assertEquals(receiverMsg.getText(), senderText);
//		p2p.close();
//
//		Subscription pubSub = Subscription.getSubscription(t, cf, -1, MessagingMode.PUB_SUB);
//		TestMessageListener pubSubMessageListener = new TestMessageListener();
//		pubSub.setMessageListener(pubSubMessageListener);
//		pubSub.start();
//
//		TopicConnection publisherConnection = ((TopicConnectionFactory)cf).createTopicConnection();
//		TopicSession publisherSession = publisherConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
//		String publisherText = new java.rmi.server.UID().toString();
//		TextMessage publisherMsg = publisherSession.createTextMessage(publisherText);
//		publisherMsg.setLongProperty(PublicDisplayJMSConstants.COURT_ID_PROPERTY_NAME, -1);
//		TopicPublisher publisher = publisherSession.createPublisher(t);
//		publisher.publish(publisherMsg);
//
//		TextMessage subscriberMsg = null;
//		for(int i = 0;i < 6;i++)
//		{
//			subscriberMsg = (TextMessage)pubSubMessageListener.getMessage();
//			if(subscriberMsg != null) break;
//			Thread.sleep(2000);
//		}
//		assertNotNull(subscriberMsg);
//		assertEquals(subscriberMsg.getText(), publisherText);
//		pubSub.close();
//	}
//
//	private class TestMessageListener implements MessageListener
//	{
//		private Message msg;
//		public void onMessage(Message msg)
//		{
//			this.msg = msg;
//		}
//		public Message getMessage()
//		{
//			return msg;
//		}
//	}
//
//}
//