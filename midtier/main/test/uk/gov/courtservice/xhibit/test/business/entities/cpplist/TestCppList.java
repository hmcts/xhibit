package uk.gov.courtservice.xhibit.test.business.entities.cpplist;


import javax.naming.NamingException;

import org.junit.Test;

import uk.gov.courtservice.xhibit.business.vos.entities.CppListBasicValue;


/**
 * Test classes for all CPP List objects.
 * 
 * @author harrism
 *
 */

public class TestCppList extends AbstractTestBasicValue {	
	
	public TestCppList(String s) throws NamingException {
		super(s);
	}	  

	protected void setUp()throws Exception
	{
		super.setUp();
	}
	 
	@Test
	public void testCppListBasicValue() {
		testBasicValue(new CppListBasicValue());
	}
}