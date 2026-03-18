//package uk.gov.courtservice.xhibit.courtlog.helpers.crud;
//
//import java.util.Date;
//
//import uk.gov.courtservice.xhibit.courtlog.OperationContext;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.MultiCaseCourtLogCRUDValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.MultiCaseCourtLogViewValue;
//
///**
// * @author pznwc5
// */
//public class TestMultiCaseCreateHelper extends TestCreateHelper
//{
//    /**
//     * Constructor for TestMultiCaseCreateHelper.
//     * @param arg0
//     */
//    public TestMultiCaseCreateHelper(String arg0) throws Exception
//    {
//        super(arg0);
//    }
//
//    public void testNewEntry() throws Exception
//    {
//		CourtLogCRUDValue crudVal = getMultiCaseCrudValue();
//		OperationContext ctx = OperationContext.newInstance(crudVal);
//
//		CreateHelper helper = new CreateHelper(ctx);
//
//		CourtLogViewValue viewValue[] = helper.newEntry();
//		assertNotNull(viewValue);
//		assertEquals(viewValue.length, 1);
//		assertEquals(MultiCaseCourtLogViewValue.class, viewValue[0].getClass());
//		assertNotNull(viewValue[0].getLogEntryId());
//		assertNotNull(viewValue[0].getLogEntry());
//		assertEquals(new Integer(22), viewValue[0].getCaseId());
//		assertNull(viewValue[0].getDefendantOnCaseId());
//		assertNull(viewValue[0].getDefendantOnOffenceId());
//		assertEquals(EVENT_TYPE, viewValue[0].getEventType());
//		assertEquals(new Integer(1), viewValue[0].getVersion());
//    }
//
//    public void testMultiCaseCreateHelper()
//    {
//		CourtLogCRUDValue crudVal = getMultiCaseCrudValue();
//		OperationContext ctx = OperationContext.newInstance(crudVal);
//
//		MultiCaseCreateHelper helper = new MultiCaseCreateHelper(ctx);
//		assertNotNull(helper);
//    }
//
//	private CourtLogCRUDValue getMultiCaseCrudValue()
//	{
//		MultiCaseCourtLogCRUDValue crudVal = new MultiCaseCourtLogCRUDValue();
//		crudVal.setEntryDate(new Date());
//		crudVal.setEntryFreeText("test");
//		crudVal.setEventType(EVENT_TYPE);
//		crudVal.setCaseIds(new Integer[] {new Integer(2), new Integer(22)});
//
//		return crudVal;
//	}
//}
//