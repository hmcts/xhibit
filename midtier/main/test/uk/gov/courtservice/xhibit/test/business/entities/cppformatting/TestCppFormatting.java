package uk.gov.courtservice.xhibit.test.business.entities.cppformatting;


import java.util.Calendar;
import java.util.Date;

import javax.naming.NamingException;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.vos.entities.CppFormattingBasicValue;


/**
 * Test classes for all CPP Formatting objects.
 * 
 * @author waltersn
 *
 */

public class TestCppFormatting extends TestCase {

	public TestCppFormatting(String s) throws NamingException {
		  
	    super(s);
	  }	  
	 protected void setUp()throws Exception
	  {
	    super.setUp();
	  }
	  
	  public void testCppFormattingBasicValue() {
		  Date d = Calendar.getInstance().getTime();
			Integer id = 2;
			Integer stagingId = 1;
			String status = "S";
			String docType = "WP";
			Integer court=81;
			Long docblob=new Long(1001);
			String errorMessage="AN ERROR";
			CppFormattingBasicValue formatting = new CppFormattingBasicValue(id,stagingId,d,status,docType,court,docblob, errorMessage);
			assertEquals((Integer)formatting.getCppFormattingId(),id);
			assertEquals((Integer)formatting.getStagingTableId(),stagingId);
			assertEquals((String)formatting.getFormatStatus(),status);
			assertEquals((String)formatting.getDocumentType(),docType);
			assertEquals((Integer)formatting.getCourtId(),court);
			assertEquals((Long)formatting.getXmlDocumentClobId(),docblob);	
			assertEquals((String)formatting.getErrorMessage(), errorMessage);
	  }
	}