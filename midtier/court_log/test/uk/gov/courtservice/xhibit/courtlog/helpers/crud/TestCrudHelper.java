package uk.gov.courtservice.xhibit.courtlog.helpers.crud;

import java.util.Date;

import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

import junit.framework.TestCase;

/**
 * @author pznwc5
 */
public class TestCrudHelper extends TestCase
{
	private static final Integer ID = new Integer(-1);
	
    /**
     * Constructor for TestCrudHelper.
     * @param arg0
     */
    public TestCrudHelper(String arg0)
    {
        super(arg0);
    }

    public void testValidateEntry() throws Exception
    {
    	CourtLogCRUDValue crudVal = new CourtLogCRUDValue();
    	crudVal.setCaseId(ID);
		crudVal.setDefendantOnCaseId(ID);
		crudVal.setDefendantOnOffenceId(ID);
		crudVal.setEntryDate(new Date());
		crudVal.setEntryFreeText("test");
		crudVal.setEventType(new Integer(10100));
		crudVal.setProperty("id", "1");
		crudVal.setProperty("flagged", "1");
		crudVal.setProperty("defendant_on_case_id", "1");
		crudVal.setProperty("flagged", "1");
		crudVal.setProperty("defendant_name", "1");
		crudVal.setProperty("flagged", "1");
		crudVal.setProperty("defendant_masked_name", "1");
		
		String logEntry = CrudHelper.validateEntry(crudVal);
		System.out.println(logEntry);
    	assertNotNull(logEntry);
    }
}
