//package uk.gov.courtservice.xhibit.client.courtlog;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import org.apache.log4j.Logger;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
//import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
//
///**
// * @author  Jon Powell (Electronic Data Systems)
// * @date    21-Aug-2003
// *
// * Tests for TestTakenIntoConsiderationTableModel
// * This class should not include any tests which require display of GUI elements as it is
// * intended to be run automatically (i.e. no user intervention).
// */
//public class TestTakenIntoConsiderationTableModel extends TestCase
//{
//
//	// logging
//	private static final Logger log = Logger.getLogger(TestTakenIntoConsiderationTableModel.class.getName());
//
//	// class under test
//	private TakenIntoConsiderationTableModel ticTableModel = null;
//	// supporting classes/variables
//	private Collection tableModelCollection = null;
//	private DefendantOnCaseValue val1, val2, val3 = null;
//	private final static String DEF1_NAME = "Fred Bloggs";
//	private final static String DEF2_NAME = "John Doe";
//	private final static String DEF3_NAME = "A.N.Other";
//	private final static Integer DEF1_TICS = new Integer(111);
//	private final static Integer DEF2_TICS = new Integer(222);
//	private final static Integer DEF3_TICS = new Integer(333);
//	private final static int NUM_COLS = 2;
//	private final static int NUM_ROWS = 3;
//
//	/**
//	 * Create new test class
//	 * @param testName  will be passed in by JUnit runner
//	 */
//	public TestTakenIntoConsiderationTableModel(String testName)
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
//		return new TestSuite(TestTakenIntoConsiderationTableModel.class);
//	}
//
//
//	/**
//	 * Initialise variables common to each test. Re-run before each test.
//	 * Set the DefendantOnCaseBasicValue for each DefendantOnCaseValue otherwise
//	 * setting no of TICs on DefendantOnCaseValue has no effect (but doesn't warn you)
//	 * - only set if DefendantOnCaseBasicValue is non null
//	 */
//	protected void setUp()
//	{
//		// create some defendants
//		val1 = new DefendantOnCaseValue();
//		val1.setDefendantOnCaseBVO(new DefendantOnCaseBasicValue());
//		val2 = new DefendantOnCaseValue();
//		val2.setDefendantOnCaseBVO(new DefendantOnCaseBasicValue());
//		val3 = new DefendantOnCaseValue();
//		val3.setDefendantOnCaseBVO(new DefendantOnCaseBasicValue());
//		// create a collection for the table mdoel
//		tableModelCollection = new ArrayList(3);
//		tableModelCollection.add(new TakenIntoConsiderationTableRowModel(val1, DEF1_NAME, DEF1_TICS));
//		tableModelCollection.add(new TakenIntoConsiderationTableRowModel(val2, DEF2_NAME, DEF2_TICS));
//		tableModelCollection.add(new TakenIntoConsiderationTableRowModel(val3, DEF3_NAME, DEF3_TICS));
//		// create model
//		ticTableModel = new TakenIntoConsiderationTableModel(tableModelCollection, "T");
//	}
//
//
//	/**
//	 * Cleanup variables used by tests. Re-run after each test.
//	 */
//	protected void tearDown()
//	{
//		val1 = null;
//		val2 = null;
//		val3 = null;
//		tableModelCollection = null;
//	}
//
//
//	//
//	// constructor test(s)
//	//
//
//	public void testConstructor() throws Exception
//	{
//		log.info("[testConstructor]");
//		assertNotNull("table model constructor returned null", ticTableModel);
//	}
//
//
//	//
//	// method tests (EXPECTED, ACTUAL)
//	//
//
//	public void testGetDefendantArray() throws Exception
//	{
//		log.info("[testGetDefendantArray]");
//		Object[] rowModels = ticTableModel.getDefendantArray();
//		assertNotNull("row model array returned from table model was null", rowModels);
//		assertEquals("row model array returned from table model was wrong length", 3, rowModels.length);
//		TakenIntoConsiderationTableRowModel rowModel = (TakenIntoConsiderationTableRowModel)rowModels[0];
//		assertNotNull(rowModel);
//		assertEquals("didn't get the expected name.", DEF1_NAME, rowModel.getFullName());
//	}
//
//	public void testGetRowColumnCount() throws Exception
//	{
//		log.info("[testGetColumnCount]");
//		int cols = ticTableModel.getColumnCount();
//		assertEquals("unexpected number of columns in model.", NUM_COLS, cols);
//		int rows = ticTableModel.getRowCount();
//		assertEquals("unexpected number of rows in model.", NUM_ROWS, rows);
//	}
//
//	public void testIsCellEditable() throws Exception
//	{
//		log.info("[testIsCellEditable]");
//		boolean defEditFlag = ticTableModel.isCellEditable(0, TakenIntoConsiderationTableModel.DEFENDANT_NAME_COLUMN);
//		assertEquals("defendant name column should not be editable.", false, defEditFlag);
//		boolean ticEditFlag = ticTableModel.isCellEditable(0, TakenIntoConsiderationTableModel.TIC_COLUMN);
//		assertEquals("TIC column should be editable.", true, ticEditFlag);
//	}
//
//	public void testGetValueAt() throws Exception
//	{
//		log.info("[testGetValueAt]");
//		String defName = (String)ticTableModel.getValueAt(0, TakenIntoConsiderationTableModel.DEFENDANT_NAME_COLUMN);
//		assertNotNull("defendant name returned should not be null.", defName);
//		assertEquals("unexpected defendant name", DEF1_NAME, defName);
//		Integer tic = (Integer)ticTableModel.getValueAt(1, TakenIntoConsiderationTableModel.TIC_COLUMN);
//		assertEquals("unexpected TIC value.", DEF2_TICS, tic);
//	}
//
//	public void testSetValueAt() throws Exception
//	{
//		log.info("[testSetValueAt]");
//		ticTableModel.setValueAt("Foo Bar", 0, TakenIntoConsiderationTableModel.DEFENDANT_NAME_COLUMN);
//		String defendant = (String)ticTableModel.getValueAt(0, TakenIntoConsiderationTableModel.DEFENDANT_NAME_COLUMN);
//		assertNotNull("defendant name returned should not be null.", defendant);
//		assertEquals("defendant name should not be editable.", DEF1_NAME, defendant);
//		ticTableModel.setValueAt("21474", 0, TakenIntoConsiderationTableModel.TIC_COLUMN);
//		Integer tics = (Integer)ticTableModel.getValueAt(0, TakenIntoConsiderationTableModel.TIC_COLUMN);
//		assertEquals("TIC should not have been accepted.", DEF1_TICS, tics);
//	}
//
//}
//
//