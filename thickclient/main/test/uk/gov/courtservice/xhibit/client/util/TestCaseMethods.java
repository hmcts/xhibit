package uk.gov.courtservice.xhibit.client.util;

import java.util.ArrayList;


import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.client.casemanagement.util.CaseMethods;

/**
 * Test the various case methods.
 * @author waltersn
 *
 */
public class TestCaseMethods extends TestCase {
	
	private static final String [] titles = new String[]{"num1","num2","num3","num4"};
	private static final String TEST = "testing";
	private ArrayList<Object> mandatoryFields = new ArrayList<Object>();


	/**
	 * Test for isAllMandatoryFieldsEntered when 1 text field isn't entered.
	 */
	public void testMandatoryXTextFieldNotEntered() {
		XTextField textField = new XTextField();
		XComboBox combo = new XComboBox(titles);
		combo.setSelectedIndex(2);
		XTextArea area = new XTextArea();
		area.setText(TEST);
		
		mandatoryFields.add(textField);
		mandatoryFields.add(combo);
		mandatoryFields.add(area);
		
		
		assertFalse(CaseMethods.isAllMandatoryFieldsEntered(mandatoryFields));
	}
	
	
	/**
	 * Test for isAllMandatoryFieldsEntered when 1 combo box isn't entered.
	 */
	public void testMandatoryXComboBoxNotEntered() {
		XTextField textField = new XTextField(TEST);
		XComboBox combo = new XComboBox(titles);
		combo.setSelectedIndex(0);
		XTextArea area = new XTextArea();
		area.setText(TEST);
		
		mandatoryFields.add(textField);
		mandatoryFields.add(combo);
		mandatoryFields.add(area);
		
		
		assertFalse(CaseMethods.isAllMandatoryFieldsEntered(mandatoryFields));
	}
	
	/**
	 * Test for isAllMandatoryFieldsEntered when 1 text area isn't entered.
	 */
	public void testMandatoryXTextAreaBoxNotEntered() {
		XTextField textField = new XTextField(TEST);
		XComboBox combo = new XComboBox(titles);
		combo.setSelectedIndex(2);
		XTextArea area = new XTextArea();
		
		mandatoryFields.add(textField);
		mandatoryFields.add(combo);
		mandatoryFields.add(area);
		
		
		assertFalse(CaseMethods.isAllMandatoryFieldsEntered(mandatoryFields));
	}
	
	/**
	 * Test for isAllMandatoryFieldsEntered when all fields are entered.
	 */
	public void testMandatoryAllEntered() {
		XTextField textField = new XTextField(TEST);
		XComboBox combo = new XComboBox(titles);
		combo.setSelectedIndex(2);
		XTextArea area = new XTextArea();
		area.setText(TEST);
		
		mandatoryFields.add(textField);
		mandatoryFields.add(combo);
		mandatoryFields.add(area);
		
		
		assertTrue(CaseMethods.isAllMandatoryFieldsEntered(mandatoryFields));
	}

	
	/**
	 * Test to ensure that calling the method disables/enables
	 * the combo box accordingly.
	 */
	public void testDisableEnableComboBox() {
		XComboBox combo = new XComboBox(titles);

		CaseMethods.disableEnableComboBox(combo, true);
		assertTrue(combo.isEnabled());
		combo.setSelectedIndex(3);

		CaseMethods.disableEnableComboBox(combo, false);
		assertFalse(combo.isEnabled());
		assertEquals(combo.getSelectedIndex(),0);
		
	}
	
	/**
	 * Test to ensure that calling the method disables the text field and sets the 
	 * text to "".
	 */
	public void testDisableTextField() {
		XTextField field = new XTextField(TEST);
		assertTrue(field.getText().equals(TEST));
		assertTrue(field.isEnabled());

		CaseMethods.disableTextField(field);
		assertTrue(field.getText().equals(""));
		assertFalse(field.isEnabled());
		
	}
}
