//package uk.gov.courtservice.xhibit.courtlog.helpers.crud;
//
//import java.util.Date;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanNotFoundException;
//import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanNotFoundException;
//import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanNotFoundException;
//import uk.gov.courtservice.xhibit.courtlog.OperationContext;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
///**
// * @author pznwc5
// */
//public class TestCreateHelper extends TransactionTestCase
//{
//	protected static final Integer CASE_ID = new Integer(2);
//	protected static final Integer DEFENDANT_ON_CASE_ID = new Integer(2);
//	protected static final Integer DEFENDANT_ON_OFFENCE_ID = new Integer(1);
//	protected static final Integer EVENT_TYPE = new Integer(10100);
//	protected static final Integer INVALID_ID = new Integer(-1);
//
//    /**
//     * Constructor for TestCreateHelper.
//     * @param arg0
//     */
//    public TestCreateHelper(String arg0) throws Exception
//    {
//        super(arg0, false);
//    }
//
//	/**
//	 * Tests ceate helper construction
//	 *
//	 */
//    public void testCreateHelper()
//    {
//		CourtLogCRUDValue crudVal = getValidCrudValue();
//		OperationContext ctx = OperationContext.newInstance(crudVal);
//
//		CreateHelper helper = new CreateHelper(ctx);
//		assertNotNull(helper);
//    }
//
//	/**
//	 * Tests new entry creation for valid entry
//	 * @throws Exception
//	 */
//    public void testValidNewEntry() throws Exception
//    {
//		CourtLogCRUDValue crudVal = getValidCrudValue();
//		OperationContext ctx = OperationContext.newInstance(crudVal);
//
//		CreateHelper helper = new CreateHelper(ctx);
//
//    	CourtLogViewValue viewValue[] = helper.newEntry();
//    	assertNotNull(viewValue);
//    	assertEquals(1, viewValue.length);
//    	assertNotNull(viewValue[0].getLogEntryId());
//		assertNotNull(viewValue[0].getLogEntry());
//
//		assertEquals(CASE_ID, viewValue[0].getCaseId());
//		assertEquals(DEFENDANT_ON_CASE_ID, viewValue[0].getDefendantOnCaseId());
//		assertEquals(DEFENDANT_ON_OFFENCE_ID, viewValue[0].getDefendantOnOffenceId());
//		assertEquals(EVENT_TYPE, viewValue[0].getEventType());
//		assertEquals(new Integer(1), viewValue[0].getVersion());
//    }
//
//	/**
//	 * Tests new entry creation for invalid case entry
//	 * @throws Exception
//	 */
//	public void testInvalidCaseNewEntry() throws Exception
//	{
//		CourtLogCRUDValue crudVal = getInvalidCaseCrudValue();
//		OperationContext ctx = OperationContext.newInstance(crudVal);
//
//		try
//		{
//			new CreateHelper(ctx).newEntry();
//			fail();
//		}
//		catch (XhbCaseBeanNotFoundException ex)
//		{
//            // expected exception...
//		}
//	}
//
//	/**
//	 * Tests new entry creation for invalid defendant on case entry
//	 * @throws Exception
//	 */
//	public void testInvalidDefendantOnCaseNewEntry() throws Exception
//	{
//		CourtLogCRUDValue crudVal = getInvalidDefendantOnCaseCrudValue();
//		OperationContext ctx = OperationContext.newInstance(crudVal);
//
//		try
//		{
//			new CreateHelper(ctx).newEntry();
//			fail();
//		}
//		catch (XhbDefendantOnCaseBeanNotFoundException ex)
//		{
//            // expected exception...
//		}
//	}
//
//	/**
//	 * Tests new entry creation for invalid defendant on offence entry
//	 * @throws Exception
//	 */
//	public void testInvalidDefendantOnOffenceNewEntry() throws Exception
//	{
//		CourtLogCRUDValue crudVal = getInvalidDefendantOnOffenceCrudValue();
//		OperationContext ctx = OperationContext.newInstance(crudVal);
//
//		try
//		{
//			new CreateHelper(ctx).newEntry();
//			fail();
//		}
//		catch (XhbDefendantOnOffenceBeanNotFoundException ex)
//		{
//            // expected exception...
//		}
//	}
//
//	// Gets a valid enty
//	private CourtLogCRUDValue getValidCrudValue()
//	{
//		return getCrudValue(CASE_ID, DEFENDANT_ON_CASE_ID, new Integer(1), EVENT_TYPE);
//	}
//
//	// Gets an invalid case entry
//	private CourtLogCRUDValue getInvalidCaseCrudValue()
//	{
//		return getCrudValue(INVALID_ID, DEFENDANT_ON_CASE_ID, DEFENDANT_ON_OFFENCE_ID, EVENT_TYPE);
//	}
//
//	// Gets an invalid defendant on case entry
//	private CourtLogCRUDValue getInvalidDefendantOnCaseCrudValue()
//	{
//		return getCrudValue(CASE_ID, INVALID_ID, DEFENDANT_ON_OFFENCE_ID, EVENT_TYPE);
//	}
//
//	// Gets an invalid defendant on offence entry
//	private CourtLogCRUDValue getInvalidDefendantOnOffenceCrudValue()
//	{
//		return getCrudValue(CASE_ID, DEFENDANT_ON_CASE_ID, INVALID_ID, EVENT_TYPE);
//	}
//
//	private CourtLogCRUDValue getCrudValue(Integer caseId, Integer defendentOnCaseId, Integer defenfantOnOffenceId, Integer eventType)
//	{
//		CourtLogCRUDValue crudVal = new CourtLogCRUDValue();
//		crudVal.setCaseId(caseId);
//		crudVal.setDefendantOnCaseId(defendentOnCaseId);
//		crudVal.setDefendantOnOffenceId(defenfantOnOffenceId);
//		crudVal.setEntryDate(new Date());
//		crudVal.setEntryFreeText("test");
//		crudVal.setEventType(eventType);
//
//		return crudVal;
//	}
//}
//