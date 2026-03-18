package uk.gov.courtservice.xhibit.client.skeletonschedule.combo;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test class written to support bug fix X53677
 * 
 * @author  Jon Powell (Electronic Data Systems)
 * @date    01-Sep-03
 * @see     TestTrialSessionComboBoxModel
 */
public class TestArrayComboBoxModel extends TestCase
{

	// class under test
	private ArrayComboBoxModel model = null;
	private ArrayComboBoxModel selectedModel = null;
	// supporting data
	private int numItems = 6;
	private String[] data = null;
	private String FIRST_ITEM = "ITEM_0";
	private String SELECTED_ITEM = "ITEM_4";

	/**
	 * Create new test class
	 * @param testName  will be passed in by JUnit runner
	 */
	public TestArrayComboBoxModel(String testName)
	{
		super(testName);
	}

	/**
	 * Have JUnit pick up all tests in this class
	 */
	public static Test suite()
	{
		return new TestSuite(TestArrayComboBoxModel.class);
	}

	/**
	 * Initialise variables common to each test. Re-run before each test.
	 */
	protected void setUp()
	{
		data = new String[numItems];
		for (int i=0; i<numItems ; i++)
			data[i] = "ITEM_" + i;
		model = new ArrayComboBoxModel(data);
		selectedModel = new ArrayComboBoxModel(data, SELECTED_ITEM);
	}

	/**
	 * Cleanup variables used by tests. Re-run after each test.
	 */
	protected void tearDown()
	{
	}

	//
	// tests
	//
	
	public void testGetSize()
	{
		assertNotNull(model);
		assertEquals(numItems, model.getSize());
	}
	
	public void testGetElementAt()
	{
		String item0 = (String)model.getElementAt(0);
		assertEquals("first element not correct", FIRST_ITEM, item0);
		String item3 = (String)model.getElementAt(3);
		assertEquals("second check returned invalid element", "ITEM_3", item3);
	}
	
	public void testGetSelectedItem()
	{
		// no explicit selection
		String item = (String)model.getSelectedItem();	
		assertEquals(FIRST_ITEM, item);
		// explicit selection
		item = (String)selectedModel.getSelectedItem();	
		assertEquals(SELECTED_ITEM, item);
	}
	
	public void testSetSelectedItem()
	{
		String item = (String)model.getSelectedItem();	
		assertEquals(FIRST_ITEM, item);
		model.setSelectedItem(SELECTED_ITEM);
		assertEquals(SELECTED_ITEM, (String)model.getSelectedItem());
	}
	
}
