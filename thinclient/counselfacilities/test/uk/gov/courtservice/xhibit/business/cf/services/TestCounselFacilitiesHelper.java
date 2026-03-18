//package uk.gov.courtservice.xhibit.business.cf.services;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
///**
// * @author  Jon Powell (Electronic Data Systems)
// *
// * Example test
// */
//public class TestCounselFacilitiesHelper extends TestCase
//{
//
//	// constants
//	private static final String EMPTY_STRING = "";
//	private static final String FIRST_NAME = "Fred";
//	private static final String LAST_NAME = "Flintstone";
//
//	// shared variables
//	private CounselFacilitiesHelper helper = null;
//
//
//	/**
//	 * Create new test class
//	 * @param testName  will be passed in by JUnit/JUnitEE runner
//	 */
//	public TestCounselFacilitiesHelper(String testName)
//	{
//		super(testName);
//	}
//
//
//	/**
//	 * Have JUnit/JUnitEE pick up all tests in this class
//	 */
//	public static Test suite()
//	{
//		return new TestSuite(TestCounselFacilitiesHelper.class);
//	}
//
//
//	/**
//	 * Initialise variables common to each test. Re-run before each test.
//	 */
//	protected void setUp()
//	{
//		helper = new CounselFacilitiesHelper();
//	}
//
//
//	/**
//	 * Cleanup variables used by tests. Re-run after each test.
//	 */
//	protected void tearDown()
//	{
//		helper = null;
//	}
//
//
//	/**
//	 * example test method
//	 */
//	public void testTidyUp()
//	{
//		System.out.println("[testTidyUp]");
//		String result = helper.tidyUp(null);
//		assertNotNull(result);
//		assertEquals("null not converted to empty string", EMPTY_STRING, result);
//		String result2 = helper.tidyUp(FIRST_NAME);
//		assertEquals("first name not tidied up correctly", FIRST_NAME, result2);
//	}
//}
//