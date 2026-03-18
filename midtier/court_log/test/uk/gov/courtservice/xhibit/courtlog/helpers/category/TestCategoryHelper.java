///*
// * Created on 09-Mar-2004
// *
// * To change the template for this generated file go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//package uk.gov.courtservice.xhibit.courtlog.helpers.category;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_category_desc.XhbCourtLogCategoryDescBasicValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//import junit.framework.TestCase;
//
///**
// * @author pznwc5
// *
// * To change the template for this generated type comment go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestCategoryHelper extends TestCase
//{
//
//	private static final Integer VALID_EVENT_TYPE_1 = new Integer(10100);
//	private static final Integer VALID_EVENT_TYPE_2 = new Integer(20603);
//	private static final Integer INVALID_EVENT_TYPE = new Integer(-1);
//
//	public TestCategoryHelper(String name)
//	{
//		super(name);
//	}
//
//    public void testGetCategoryDescriptions()
//    {
//		XhbCourtLogCategoryDescBasicValue[] vals =
//			CategoryHelper.getCategoryDescriptionsByEventType(VALID_EVENT_TYPE_1);
//		assertEquals(2, vals.length);
//
//		vals = CategoryHelper.getCategoryDescriptionsByEventType(VALID_EVENT_TYPE_2);
//		assertEquals(1, vals.length);
//
//		vals = CategoryHelper.getCategoryDescriptionsByEventType(INVALID_EVENT_TYPE);
//		assertEquals(0, vals.length);
//    }
//
//    public void testGetDescriptions()
//    {
//		CourtLogCRUDValue crudVal = new CourtLogCRUDValue();
//
//		crudVal.setEventType(VALID_EVENT_TYPE_1);
//		String descriptions[] = CategoryHelper.getDescriptions(crudVal);
//		assertEquals(2, descriptions.length);
//		assertEquals("Total_Hearing_Time_Start", descriptions[0]);
//		assertEquals("Scheduled_Hearing_Time_Start", descriptions[1]);
//
//		crudVal.setEventType(VALID_EVENT_TYPE_2);
//		descriptions = CategoryHelper.getDescriptions(crudVal);
//		assertEquals(1, descriptions.length);
//		assertEquals("Appeal_Witness_Sworn", descriptions[0]);
//
//		crudVal.setEventType(INVALID_EVENT_TYPE);
//		descriptions = CategoryHelper.getDescriptions(crudVal);
//		assertEquals(0, descriptions.length);
//    }
//
//}
//