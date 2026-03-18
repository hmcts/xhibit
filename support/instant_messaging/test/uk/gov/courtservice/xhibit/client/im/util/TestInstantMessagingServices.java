package uk.gov.courtservice.xhibit.client.im.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author  Jon Powell (Electronic Data Systems)
 * 
 * Empty test created for running the ant test framework 
 */
public class TestInstantMessagingServices extends TestCase
{
	
	/**
	 * Create new test class
	 * @param testName  will be passed in by JUnit runner
	 */
	public TestInstantMessagingServices(String testName)
	{
		super(testName);
	}
	
	
	/**
	 * Have JUnit pick up all tests in this class
	 */
	public static Test suite()
	{
		return new TestSuite(TestInstantMessagingServices.class);
	}
	
	
	/**
	 * Initialise variables common to each test. Re-run before each test.
	 */
	protected void setUp()
	{
	}


	/**
	 * Cleanup variables used by tests. Re-run after each test.
	 */
	protected void tearDown()
	{
	}


	/**
	 * Test Constructors
	 */
	public void testInstantMessagingServices()
	{
	
	}
}
