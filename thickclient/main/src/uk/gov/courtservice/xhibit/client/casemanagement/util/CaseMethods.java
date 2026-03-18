package uk.gov.courtservice.xhibit.client.casemanagement.util;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseUtils;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseXPanel;
import uk.gov.courtservice.xhibit.client.casemanagement.DefendantAppellantTab;
import uk.gov.courtservice.xhibit.client.casemanagement.GeneralCriminalAppeal;
import uk.gov.courtservice.xhibit.client.casemanagement.GeneralMiscAppeal;
import uk.gov.courtservice.xhibit.client.casemanagement.GeneralSentence;
import uk.gov.courtservice.xhibit.client.casemanagement.GeneralTrial;
import uk.gov.courtservice.xhibit.client.util.XCheckBox;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTextArea;
import uk.gov.courtservice.xhibit.client.util.XTextField;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * Class with methods that were previously defined in every single case create
 * screen.
 */
public final class CaseMethods {

	private CaseMethods() {
		//private constructor so that it can't be
		//initialised.
	}
	/**
	 * Method used to check mandatory fields and enabled
	 */
	public static void checkMandatoryFields(CaseXPanel caseX, List<Object> mandatoryFields, boolean fieldsChangedGlobal) {
		// enable createcase button if all mandatory fields are entered
		boolean fieldsChanged = isAllMandatoryFieldsEntered(mandatoryFields);

		if (caseX.getCreateButton().getText().equals("Save")) {
			fieldsChanged = fieldsChangedGlobal && fieldsChanged; // for proper
															// control
															// of whether save
															// button
															// enabled/disabled
		}

		caseX.getCreateButton().setEnabled(fieldsChanged);
		if (fieldsChangedGlobal)
			caseX.getFinishButton().setEnabled(!fieldsChangedGlobal);

		enableTabbedPane(caseX);
	}
	
	/**
	 * If save button is enabled then iterates
	 * through tabs and disables, removes need to distinguish
	 * between case type
	 * @param caseX CaseXPanel
	 */
	public static void enableTabbedPane(CaseXPanel caseX) {
		if (caseX.getCreateButton().getText().equals("Save")) {
			for (int i = 0; i < caseX.getTabbedPane().getComponentCount(); i++) {
				if (caseX.getTabbedPane().getSelectedIndex() != i) {
					caseX.getTabbedPane().setEnabledAt(i, caseX.getFinishButton().isEnabled());
				}
			}
		}
	}
	
	/**
	 * Checks that all mandatory fields have been entered.
	 * @param mandatoryFields
	 * @return true/false
	 */
	public static boolean isAllMandatoryFieldsEntered(List<Object> mandatoryFields) {
		boolean isValid = true;
		for (int i = 0; i < mandatoryFields.size(); i++) {
			if ((mandatoryFields.get(i).getClass() == XTextField.class &&
				( "".equals(((XTextField) mandatoryFields.get(i)).getText()) || ((XTextField) mandatoryFields.get(i)).getText()== null ))
				
				|| (mandatoryFields.get(i).getClass() == XDatePanel.class &&
					( "".equals(((XDatePanel) mandatoryFields.get(i)).getText()) || ((XDatePanel) mandatoryFields.get(i)).getText()== null ))
				
				||(mandatoryFields.get(i).getClass() == XComboBox.class && ((XComboBox) mandatoryFields.get(i)).getSelectedIndex() == 0)
				
				|| (mandatoryFields.get(i).getClass() == XTextArea.class &&
				( "".equals(((XTextArea) mandatoryFields.get(i)).getText()) || ((XTextArea) mandatoryFields.get(i)).getText()== null ))){	
					isValid = false;
			}
			
			if(!isValid) {
				break;
			}
		}
		return isValid;
	}
	
	/**
	 * Change listeners to be added onto components 
	 * @param components
	 */
	public static void addChangeListeners(Component[] components, final CaseXPanel caseX) {
		for (int i = 0; i < components.length; i++) {
			if (components[i].getClass() == XTextField.class) {
				((XTextField) components[i]).getDocument().addDocumentListener(new GlobalTrueDocListener(caseX));
			} else if (components[i].getClass() == XDatePanel.class) {
				((XDatePanel) components[i]).getDateComponent().addMChangeListener(new GlobalTrueMChangeListener(caseX));
				((XDatePanel) components[i]).getEntryField().getDisplay().addKeyListener(new GlobalTrueKeyListener(caseX));
			} else if (components[i].getClass() == XComboBox.class) {
				((XComboBox) components[i]).addItemListener(new GlobalTrueItemListener(caseX));
			} else if (components[i].getClass() == XCheckBox.class) {
				((XCheckBox) components[i]).addActionListener(new GlobalTrueActionListener(caseX));
			} else if (components[i].getClass() == JRadioButton.class) {
				((JRadioButton) components[i]).addActionListener(new GlobalTrueActionListener(caseX));
			} else if(components[i].getClass() == JTextArea.class) {
				((JTextArea)components[i]).getDocument().addDocumentListener(new GlobalTrueDocListener(caseX));
			}
		}
	}
	
	/**
	 *  Method to add change listeners to each component within form
	 */
		protected static void setGlobalTrueAndCheckMandatory(CaseXPanel caseX) {
			Component x = null;
			if(((JScrollPane) caseX.getTabbedPane().getSelectedComponent())!=null ){
				x = ((JScrollPane) caseX.getTabbedPane().getSelectedComponent()).getViewport().getView();
			}
				
			if (x!=null && x.getClass()==GeneralSentence.class) {
				((GeneralSentence)x).setFieldsChangedGlobal(true);
			} else if (x!=null && x.getClass()==GeneralTrial.class) {
				((GeneralTrial)x).setFieldsChangedGlobal(true);
			} else if (x!=null && x.getClass()==GeneralCriminalAppeal.class) {
				((GeneralCriminalAppeal)x).setFieldsChangedGlobal(true);
			} else if (x!=null && x.getClass()==GeneralMiscAppeal.class) {
				((GeneralMiscAppeal)x).setFieldsChangedGlobal(true);
			} else if(x!=null && x.getClass()==DefendantAppellantTab.class) {
				((DefendantAppellantTab)x).setFieldsChangedGlobal(true);
			}
			
			
		}
		
		/**
		 * DocListener that sets global changed to true.
		 */
		private static class GlobalTrueDocListener implements DocumentListener {

			private CaseXPanel caseX;

			public GlobalTrueDocListener(CaseXPanel caseX) {
				this.caseX=caseX;
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				setGlobalTrueAndCheckMandatory(caseX);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				setGlobalTrueAndCheckMandatory(caseX);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setGlobalTrueAndCheckMandatory(caseX);
			}
		}
		
		/**
		 * Action listener that sets the global changed to true.
		 */
		private static class GlobalTrueActionListener implements ActionListener {
			
			private CaseXPanel caseX;

			public GlobalTrueActionListener(CaseXPanel caseX) {
				this.caseX = caseX;
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				setGlobalTrueAndCheckMandatory(caseX);				
			}
		}
		
		/**
		 * Action listener that sets the global changed to true.
		 */
		private static class GlobalTrueItemListener implements ItemListener {
			
			private CaseXPanel caseX;

			public GlobalTrueItemListener(CaseXPanel caseX) {
				this.caseX = caseX;
			}
			@Override
			public void itemStateChanged(ItemEvent e) {
				setGlobalTrueAndCheckMandatory(caseX);
			}
		}
		
		/**
		 * Key listener that sets global changed to true.
		 * @author waltersn
		 *
		 */
		private static class GlobalTrueKeyListener implements KeyListener {
			
			private CaseXPanel caseX;

			public GlobalTrueKeyListener(CaseXPanel caseX) {
				this.caseX = caseX;
			}
			@Override
			public void keyTyped(KeyEvent e) {
				setGlobalTrueAndCheckMandatory(caseX);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				//Don't want to do anything on only on typed
			}

			@Override
			public void keyReleased(KeyEvent e) {
				//Don't want to do anything on only on typed
			}
		}

		/**
		 * Key listener that sets global changed to true.
		 * @author waltersn
		 *
		 */
		private static class GlobalTrueMChangeListener implements MChangeListener {
			
			private CaseXPanel caseX;

			public GlobalTrueMChangeListener(CaseXPanel caseX) {
				this.caseX = caseX;
			}
			@Override
			public void valueChanged(MChangeEvent event) {
				if (event.getType() == MChangeEvent.CHANGE) { // value
																// changed
					setGlobalTrueAndCheckMandatory(caseX);
				}
			}
		}
		
		/**
		 * Create error labels.
		 * @return
		 */
		public static JLabel createErrorLabel(List<JLabel> validationFields) {
			JLabel label = new JLabel(" ");
			label.setForeground(Color.RED);
			validationFields.add( label);
			return label;
		}
		
		/**
		 * Add or remove the specified object from the mandatory fields list.
		 * @param 
		 */
		public static void addRemoveFromMandatoryFields(Object field, JLabel manLabel, List<Object> mandatoryFields, boolean add) {
			if (add) {
				if(!mandatoryFields.contains(field)) {
					mandatoryFields.add(field);
				}
				if(manLabel!=null){
					CaseUtils.addMandatoryLabel(manLabel);
				}
			} else {
				mandatoryFields.remove(field);	
				if(manLabel!=null) {
					CaseUtils.removeMandatoryLabel(manLabel);
				}

			}
		}
		
		/**
		 * Validates dates, same code used several times so 
		 * put it in one method
		 */
		public static void compareDates(XDatePanel mainDatePanel, XDatePanel datetoCompare, String beforeOrAfter, String warning) {
			mainDatePanel.setToCompare(datetoCompare);
			mainDatePanel.setBeforeOrAfter(beforeOrAfter);
			mainDatePanel.setCustomWarning(warning);
			mainDatePanel.crossValidateDate();
		}
		
		/**
		 * Default gridbag implementation.
		 */
		public static GridBagConstraints getDefaultGridBagConstraints() {
			return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
					XHIBITConstant.nonContainerInsets, 0, 0);
		}
	
		/**
		 * Sets a combo box to either be enabled and mandatory
		 * or disabled/not mandatory, sets it back to 0 and clears
		 * the error.
		 * @param comboBox to disable/enable
		 * @param enabled true or false
		 */
		public static void disableEnableComboBox(XComboBox comboBox, boolean enabled) {
			comboBox.setEnabled(enabled);
			comboBox.setMandatory(enabled);
			if(!enabled) {
				comboBox.setSelectedIndex(0);
				comboBox.clearError();
			}
		}
		
		/**
		 * Disable textfield, reset error set text to blank.
		 */
		public static void disableTextField(XTextField txtField) {
			txtField.setText("");
			txtField.setEnabled(false);
			txtField.clearError();
		}
}
