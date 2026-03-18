//package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.test;
//
//
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicNoticeEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
//import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayJMSConstants;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event.EventStore;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event.EventStoreFactory;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.MessageReceiver;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.MessagingMode;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.Subscription;
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
//public class TestMessageReceiver extends TestCase
//{
//
//	private ConnectionFactory cf;
//	private Queue q;
//
//	public TestMessageReceiver(String s)
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
//
//		ctx.close();
//
//	}
//
//	public void testOnMessage()
//		throws Exception
//	{
//		QueueConnection senderConnection = ((QueueConnectionFactory)cf).createQueueConnection();
//		QueueSession senderSession = senderConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//
//		CourtRoomIdentifier id = new CourtRoomIdentifier(new Integer(-1), new Integer(-1));
//		PublicDisplayEvent senderEvent = new PublicNoticeEvent(id);
//
//		ObjectMessage senderMsg = senderSession.createObjectMessage(senderEvent);
//		senderMsg.setLongProperty(PublicDisplayJMSConstants.COURT_ID_PROPERTY_NAME, -1);
//		QueueSender sender = senderSession.createSender(q);
//		sender.send(senderMsg);
//
//		Subscription p2p = Subscription.getSubscription(q, cf, -1, MessagingMode.P2P);
//		EventStore eventStore = EventStoreFactory.getEventStore();
//		MessageReceiver messageReceiver = new MessageReceiver(eventStore);
//		p2p.setMessageListener(messageReceiver);
//		p2p.start();
//		PublicDisplayEvent receiverEvent = eventStore.popEvent();
//		System.out.println(receiverEvent.getClass());
//		assertNotNull(receiverEvent);
//		p2p.close();
//	}
//
//}
//