//package uk.gov.courtservice.xhibit.courtlog;
//
//import java.util.Date;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
///**
// * @author pznwc5
// */
//public class TestOperationContext extends CourtLogTestCase
//{
//	private static final Integer CASE_ID = new Integer(2);
//	private static final Integer DEFENDANT_ON_CASE_ID = new Integer(2);
//	private static final Integer DEFENDANT_ON_OFFENCE_ID = new Integer(1);
//
//	private XhbCourtLogEntry entry = null;
//
//    /**
//     * Constructor for TestOperationContext.
//     * @param arg0
//     */
//    public TestOperationContext(String arg0) throws Exception
//    {
//        super(arg0);
//    }
//
//    public void setUp()
//    {
//		XhbCourtLogEntryBasicValue basicVal = new XhbCourtLogEntryBasicValue();
//		basicVal.setDateTime(new Date());
//		basicVal.setLogEntryXml("<test/>");
//        basicVal.setCaseId(CASE_ID);
//        // no scheduled hearing id is to be set...
//        basicVal.setEventDescId(new Integer(1));
//        basicVal.setDefendantOnCaseId(DEFENDANT_ON_CASE_ID);
//        basicVal.setDefendantOnOffenceId(DEFENDANT_ON_OFFENCE_ID);
//
//		entry = XhbCourtLogEntryBeanHelper2.createLocal(basicVal);
//    }
//
//    public void tearDown() throws Exception
//    {
//    	entry.remove();
//    }
//
//    public void testNewInstance() throws Exception
//    {
//		CourtLogCRUDValue val = new CourtLogCRUDValue();
//    	OperationContext ctx = OperationContext.newInstance(val);
//    	assertEquals(OperationContext.class, ctx.getClass());
//    	val.setLogEntryId(new Long(-1));
//		ctx = OperationContext.newInstance(val);
//		assertEquals(OperationContext.UpdateDeleteContext.class, ctx.getClass());
//		assertNotNull(ctx.getOriginalViewValue());
//		assertNotNull(ctx.getOriginalBasicValue());
//    }
//
//    public void testGetPutAttribute()
//    {
//		CourtLogCRUDValue val = new CourtLogCRUDValue();
//		OperationContext ctx = OperationContext.newInstance(val);
//		ctx.putAttribute("test.key", "test.value");
//		assertEquals("test.value", ctx.getAttribute("test.key"));
//    }
//
//    public void testGetCrudValue()
//    {
//		CourtLogCRUDValue val = new CourtLogCRUDValue();
//		OperationContext ctx = OperationContext.newInstance(val);
//		assertNotNull(ctx.getCrudValue());
//    }
//
//    public void testGetOriginalViewValue() throws Exception
//    {
//		CourtLogCRUDValue val = new CourtLogCRUDValue();
//		OperationContext ctx = OperationContext.newInstance(val);
//		assertEquals(OperationContext.class, ctx.getClass());
//		try
//		{
//			ctx.getOriginalViewValue();
//			fail();
//		}
//		catch(UnsupportedOperationException ex)
//		{
//            // expected exception...
//		}
//		val.setLogEntryId(new Long(-1));
//		ctx = OperationContext.newInstance(val);
//		assertEquals(OperationContext.UpdateDeleteContext.class, ctx.getClass());
//		assertNotNull(ctx.getOriginalViewValue());
//    }
//
//    public void testGetOriginalBasicValue() throws Exception
//    {
//		CourtLogCRUDValue val = new CourtLogCRUDValue();
//		OperationContext ctx = OperationContext.newInstance(val);
//		assertEquals(OperationContext.class, ctx.getClass());
//		try
//		{
//			ctx.getOriginalBasicValue();
//			fail();
//		}
//		catch(UnsupportedOperationException ex)
//		{
//            // expected exception...
//		}
//		val.setLogEntryId(new Long(-1));
//		ctx = OperationContext.newInstance(val);
//		assertEquals(OperationContext.UpdateDeleteContext.class, ctx.getClass());
//		assertNotNull(ctx.getOriginalBasicValue());
//    }
//}
//