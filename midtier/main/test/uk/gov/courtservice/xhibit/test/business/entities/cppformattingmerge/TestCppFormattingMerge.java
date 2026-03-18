package uk.gov.courtservice.xhibit.test.business.entities.cppformattingmerge;


import java.util.Calendar;
import java.util.Date;

import javax.naming.NamingException;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.vos.entities.CppFormattingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CppFormattingMergeBasicValue;


/**
 * Test classes for all CPP Formatting Merge objects.
 * 
 * @author waltersn
 *
 */

public class TestCppFormattingMerge extends TestCase {

	public TestCppFormattingMerge(String s) throws NamingException {
		  
	    super(s);
	  }	  
	 protected void setUp()throws Exception
	  {
	    super.setUp();
	  }
	  
	  public void testCppFormattingBasicValue() {
			Integer cppFormattingMergeId = 2;
			Integer cppFormattingId = 1;
			Integer formattingId=1001;
			String language = "cy";
			Long xhibitClobId = new Long(10);
			Integer courtId=81;
			
			CppFormattingMergeBasicValue fM = new CppFormattingMergeBasicValue(cppFormattingMergeId, cppFormattingId, formattingId, courtId, language, xhibitClobId);
			
			assertEquals((Integer)fM.getCppFormattingId(),cppFormattingId);
			assertEquals((Integer)fM.getCppFormattingMergeId(),cppFormattingMergeId);
			assertEquals((Integer)fM.getFormattingId(),formattingId);
			assertEquals((String)fM.getLanguage(),language);
			assertEquals((Integer)fM.getCourtId(),courtId);
			assertEquals((Long)fM.getXhibitClobId(),xhibitClobId);	

			

	  }
	}