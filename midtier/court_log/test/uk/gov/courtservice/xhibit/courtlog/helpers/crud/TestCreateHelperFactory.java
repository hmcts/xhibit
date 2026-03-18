package uk.gov.courtservice.xhibit.courtlog.helpers.crud;

import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
import uk.gov.courtservice.xhibit.courtlog.vos.MultiCaseCourtLogCRUDValue;
import junit.framework.TestCase;

/**
 * @author pznwc5
 */
public class TestCreateHelperFactory extends TestCase
{
	public TestCreateHelperFactory(String name)
	{
		super(name);
	}

    public void testGetCreateHelper() throws Exception
    {
    	CourtLogCRUDValue crudVal1 = new CourtLogCRUDValue();
        crudVal1.setProperty(CourtLogCRUDValue.PROCESS_LINKED_CASES_PROPERTY, "false");
    	OperationContext ctx1 = OperationContext.newInstance(crudVal1);
		CreateHelper helper1 = CreateHelperFactory.getCreateHelper(ctx1);
		assertEquals(helper1.getClass(), CreateHelper.class);
		
		CourtLogCRUDValue crudVal2 = new MultiCaseCourtLogCRUDValue();
		OperationContext ctx2 = OperationContext.newInstance(crudVal2);
		CreateHelper helper2 = CreateHelperFactory.getCreateHelper(ctx2);
		assertEquals(helper2.getClass(), MultiCaseCreateHelper.class);
		
		CourtLogCRUDValue crudVal3 = new CourtLogCRUDValue();
        crudVal3.setProperty(CourtLogCRUDValue.PROCESS_LINKED_CASES_PROPERTY, "true");
		OperationContext ctx3 = OperationContext.newInstance(crudVal3);
		CreateHelper helper3 = CreateHelperFactory.getCreateHelper(ctx3);
		assertEquals(helper3.getClass(), LinkedCaseCreateHelper.class);
    }
}
