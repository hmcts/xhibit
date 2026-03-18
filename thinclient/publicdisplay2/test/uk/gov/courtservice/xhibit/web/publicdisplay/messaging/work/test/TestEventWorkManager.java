//package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work.test;
//
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicNoticeEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event.EventStore;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event.EventStoreFactory;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work.EventWorkManager;
//
///**
// * @author pznwc5
// *
// * To change the template for this generated type comment go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestEventWorkManager extends TestCase
//{
//
//	private PublicDisplayEvent event;
//	private EventStore eventStore = EventStoreFactory.getEventStore();
//
//	public TestEventWorkManager(String s)
//		throws Exception
//	{
//		super(s);
//	}
//
//	public void setUp()
//		throws Exception
//	{
//		CourtRoomIdentifier id = new CourtRoomIdentifier(new Integer(1), new Integer(31));
//		event = new PublicNoticeEvent(id);
//	}
//
//	public void testShutdown()
//		throws Exception
//	{
//		EventWorkManager manager = new EventWorkManager(eventStore, 1);
//		manager.start();
//		assertTrue(manager.isAlive());
//		manager.shutDown();
//		manager.join();
//		assertTrue(!manager.isAlive());
//	}
//
//	public void testDoWork()
//		throws Exception
//	{
//		EventWorkManager manager = new EventWorkManager(eventStore, 1);
//		manager.start();
//		eventStore.pushEvent(event);
//		manager.shutDown();
//	}
//
//}
//