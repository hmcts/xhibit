//package uk.gov.courtservice.xhibit.courtlog.helpers.event;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDesc;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDescBeanNotFoundException;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
///**
// * @author pznwc5
// */
//public class TestEventHelper extends TestCase
//{
//	private static final Integer VALID_EVENT_TYPE = new Integer(10100);
//	private static final Integer INVALID_EVENT_TYPE = new Integer(-1);
//	private static final Integer VALID_EVENT_ID = new Integer(1);
//	private static final Integer INVALID_EVENT_ID = new Integer(-1);
//
//	public TestEventHelper(String name)
//	{
//		super(name);
//	}
//
//    public void testGetEventDescriptionByEventType()
//    {
//		CourtLogCRUDValue crudVal = new CourtLogCRUDValue();
//		crudVal.setEventType(VALID_EVENT_TYPE);
//
//		XhbCourtLogEventDesc val =
//			EventHelper.getXhbCourtLogEventDescByEventType(crudVal);
//		assertNotNull(val);
//		assertEquals("Case Called On", val.getEventDescription());
//
//		try
//		{
//			crudVal.setEventType(INVALID_EVENT_TYPE);
//			val = EventHelper.getXhbCourtLogEventDescByEventType(crudVal);
//			fail("Invalid event description found");
//		}
//		catch (XhbCourtLogEventDescBeanNotFoundException ex)
//		{
//            // expected exception...
//		}
//    }
//
//	public void testGetEventDescriptionByEventId()
//	{
//		XhbCourtLogEntryBasicValue basicVal = new XhbCourtLogEntryBasicValue();
//		basicVal.setEventDescId(VALID_EVENT_ID);
//
//		XhbCourtLogEventDesc val =
//			EventHelper.getXhbCourtLogEventDescByEventId(basicVal);
//		assertNotNull(val);
//		assertEquals("Case Called On", val.getEventDescription());
//
//		try
//		{
//			basicVal.setEventDescId(INVALID_EVENT_ID);
//			val = EventHelper.getXhbCourtLogEventDescByEventId(basicVal);
//			fail("Invalid event description found");
//		}
//		catch (XhbCourtLogEventDescBeanNotFoundException ex)
//		{
//            // expected exception...
//		}
//	}
//}
//