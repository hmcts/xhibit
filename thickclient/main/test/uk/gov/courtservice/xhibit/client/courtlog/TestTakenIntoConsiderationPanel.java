//package uk.gov.courtservice.xhibit.client.courtlog;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
//import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
//import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
//
///**
// * @author  Jon Powell (Electronic Data Systems)
// * @date    21-Aug-2003
// *
// * Tests for TakenIntoConsiderationPanel
// * This class should not include any tests which require display of GUI elements as it is
// * intended to be run automatically (i.e. no user intervention).
// * THIS DOES NOT WORK - can't just create a panel - end up having to initialise the whole
// * client before you can do anything which is a pain in the neck
// */
//public class TestTakenIntoConsiderationPanel extends TestCase
//{
//	private static final Logger log = Logger.getLogger(TestTakenIntoConsiderationPanel.class.getName());
//
//	// class under test
//	private TakenIntoConsiderationPanel ticPanel = null;
//	// supporting classes
//	private TakenIntoConsiderationDialog ticDialog = null;
//	private TakenIntoConsiderationModel ticModel = null;
//	private DefendantOnCaseValue val1, val2, val3 = null;
//
//
//	/**
//	 * Create new test class
//	 * @param testName  will be passed in by JUnit runner
//	 */
//	public TestTakenIntoConsiderationPanel(String testName)
//	{
//		super(testName);
//	}
//
//
//	/**
//	 * Have JUnit pick up all tests in this class
//	 */
//	public static Test suite()
//	{
//		return new TestSuite(TestTakenIntoConsiderationPanel.class);
//	}
//
//
//	/**
//	 * Initialise variables common to each test. Re-run before each test.
//	 */
//	protected void setUp()
//	{
//
//		// create some defendants
//		val1 = new DefendantOnCaseValue();
//		val2 = new DefendantOnCaseValue();
//		val3 = new DefendantOnCaseValue();
//
//		// create a model for the panel
//		List collection = new ArrayList(3);
//		collection.add(new TakenIntoConsiderationTableRowModel(val1, "Fred Bloggs"));
//		collection.add(new TakenIntoConsiderationTableRowModel(val2, "John Doe"));
//		collection.add(new TakenIntoConsiderationTableRowModel(val3, "A.N.Other"));
//		ticModel = new TakenIntoConsiderationModel();
//
//		ApplicationCaseModel acm = new ApplicationCaseModel();
//		acm.setCaseId(new Integer(999));
//		log.debug("set case id");
//		XhibitApplicationController xac = null;
//		/*
//		xac = new XhibitApplicationController();
//		xac.setApplicationCaseModel(acm);
//		log.debug("set case model");
//		ticModel.setXac(xac);
//
//		ticModel.setDefendantCollection(collection);
//		// create parent dialog
//		try
//        {
//            ticDialog = new TakenIntoConsiderationDialog(null, ticModel);
//        }
//        catch (CSRecoverableException e)
//        {
//        	log.fatal("setUp: exception creating TakenIntoConsiderationDialog - " + e.getMessage());
//            e.printStackTrace();
//        }
//        */
//	}
//
//
//	/**
//	 * Cleanup variables used by tests. Re-run after each test.
//	 */
//	protected void tearDown()
//	{
//	}
//
//
//	/**
//	 * test constructor
//	 */
//	public void testTakenIntoConsiderationPanel() throws Exception
//	{
//		log.info("[testTakenIntoConsiderationPanel]");
//		//ticPanel = new TakenIntoConsiderationPanel(ticDialog, ticModel);
//		//assertNotNull(ticPanel);
//		fail("does not test anything");
//	}
//
//}
//
//