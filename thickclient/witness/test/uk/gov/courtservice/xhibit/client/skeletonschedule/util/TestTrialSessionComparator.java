package uk.gov.courtservice.xhibit.client.skeletonschedule.util;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDayValue;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSessionValue;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.TrialSessionImpl;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;

/**
 * @author  Jon Powell (Electronic Data Systems)
 * @date    01-Sep-03
 */
public class TestTrialSessionComparator extends TestCase
{
	// class under test
	private TrialSessionComparator comparator = null;
	// supporting classes
	private TrialSession[] sessions = null;
	private XhbSkeletonSessionValue[] skeletons = null;
	// constants
	private short DAY_1 = (short)1;
	private short DAY_2 = (short)2;
	private short DAY_3 = (short)3;
	private String MORNING_SESSION = "M";
	private String AFTERNOON_SESSION = "A";	
	private String[] illegalArray = {"2-A","3-M","1-M","3-A","1-A","2-M"};

	/**
	 * Create new test class
	 * @param testName  will be passed in by JUnit runner
	 */
	public TestTrialSessionComparator(String testName)
	{
		super(testName);
	}

	/**
	 * Have JUnit pick up all tests in this class
	 */
	public static Test suite()
	{
		return new TestSuite(TestTrialSessionComparator.class);
	}

	/**
	 * Initialise variables common to each test. Re-run before each test.
	 */
	protected void setUp()
	{		
		comparator = new TrialSessionComparator();	
		skeletons = new XhbSkeletonSessionValue[6];
		sessions = new TrialSession[6];
		
		// 2-A
		skeletons[0] = new XhbSkeletonSessionValue();
		skeletons[0].setMorningOrAfternoon(AFTERNOON_SESSION);
		skeletons[0].setXhbSkeletonDay(new XhbSkeletonDayValue());
		skeletons[0].getXhbSkeletonDay().setDayNumber(DAY_2);
		// 3-M
		skeletons[1] = new XhbSkeletonSessionValue();
		skeletons[1].setMorningOrAfternoon(MORNING_SESSION);
		skeletons[1].setXhbSkeletonDay(new XhbSkeletonDayValue());
		skeletons[1].getXhbSkeletonDay().setDayNumber(DAY_3);   
		// 1-M
		skeletons[2] = new XhbSkeletonSessionValue();
		skeletons[2].setMorningOrAfternoon(MORNING_SESSION);
		skeletons[2].setXhbSkeletonDay(new XhbSkeletonDayValue());
		skeletons[2].getXhbSkeletonDay().setDayNumber(DAY_1);  
		// 3-A
		skeletons[3] = new XhbSkeletonSessionValue();
		skeletons[3].setMorningOrAfternoon(AFTERNOON_SESSION);
		skeletons[3].setXhbSkeletonDay(new XhbSkeletonDayValue());
		skeletons[3].getXhbSkeletonDay().setDayNumber(DAY_3);  		
		// 1-A
		skeletons[4] = new XhbSkeletonSessionValue();
		skeletons[4].setMorningOrAfternoon(AFTERNOON_SESSION);
		skeletons[4].setXhbSkeletonDay(new XhbSkeletonDayValue());
		skeletons[4].getXhbSkeletonDay().setDayNumber(DAY_1); 
		// 2-M        
		skeletons[5] = new XhbSkeletonSessionValue();
		skeletons[5].setMorningOrAfternoon(MORNING_SESSION);
		skeletons[5].setXhbSkeletonDay(new XhbSkeletonDayValue());
		skeletons[5].getXhbSkeletonDay().setDayNumber(DAY_2); 
		
		for (int i=0 ; i<6 ; i++)
			sessions[i] = new TrialSessionImpl(skeletons[i]);	
	}

	/**
	 * Cleanup variables used by tests. Re-run after each test.
	 */
	protected void tearDown()
	{
		comparator = null;
		skeletons = null;
		sessions = null;
	}


	//
	// tests
	//

	public void testIllegalArguments()
	{
		try
		{
			Arrays.sort(illegalArray, comparator);
			fail("should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException intentionally_ignored) {}
	}
	
	public void testSort()
	{
		// test position 0 and 3 before and after sort
		assertEquals(DAY_2, sessions[0].getDayNumber());
		assertEquals(AFTERNOON_SESSION, sessions[0].getSessionType());
		assertEquals(DAY_3, sessions[3].getDayNumber());
		assertEquals(AFTERNOON_SESSION, sessions[3].getSessionType());
		// sort em
		Arrays.sort(sessions, comparator);
		// should now be 1-M,1-A,2-M,2-A,3-M,3-A
		assertEquals(DAY_1, sessions[0].getDayNumber());
		assertEquals(MORNING_SESSION, sessions[0].getSessionType());
		assertEquals(DAY_2, sessions[3].getDayNumber());
		assertEquals(AFTERNOON_SESSION, sessions[3].getSessionType());		
	}

}

