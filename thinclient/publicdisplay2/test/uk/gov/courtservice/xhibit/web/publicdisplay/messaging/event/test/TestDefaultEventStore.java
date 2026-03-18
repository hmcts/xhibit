//package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event.test;
//
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.testutils.junit.threaded.FailureGatherer;
//import uk.gov.courtservice.framework.testutils.junit.threaded.FailureGatheringRunnable;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicNoticeEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event.DefaultEventStore;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event.EventStore;
///**
// * @author pznwc5
// *
// * To change the template for this generated type comment go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestDefaultEventStore extends TestCase
//{
//
//	private static final long JOIN_TIMEOUT = 300L;
//
//	private EventStore eventStore;
//	private PublicDisplayEvent event;
//
//	public TestDefaultEventStore(String s)
//		throws Exception
//	{
//		super(s);
//	}
//
//	public void setUp()
//	{
//		eventStore = new DefaultEventStore();
//		CourtRoomIdentifier id = new CourtRoomIdentifier(new Integer(-1), new Integer(-1));
//		event = new PublicNoticeEvent(id);
//	}
//
//	public void testPushEvent()
//		throws Exception
//	{
//		eventStore.pushEvent(event);
//		assertNotNull(eventStore.popEvent());
//	}
//
//	public void testPopEvent()
//		throws Exception
//	{
//		FailureGatherer failureGatherer = new FailureGatherer();
//		Thread popThread = new Thread(
//			new FailureGatheringRunnable("Pop thread", failureGatherer)
//			{
//				public void runTest()
//				{
//					assertNotNull(eventStore.popEvent());
//					System.out.println("Event received");
//				}
//			}
//		);
//		popThread.start();
//		popThread.join(JOIN_TIMEOUT);
//
//		Thread pushThread = new Thread()
//		{
//			public void run()
//			{
//				eventStore.pushEvent(event);
//			}
//		};
//		pushThread.start();
//		pushThread.join(JOIN_TIMEOUT);
//
//		failureGatherer.checkForFailures();
//	}
//
//}
//