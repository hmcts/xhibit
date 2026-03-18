package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mseries.Calendar.MFieldListener;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.client.casemanagement.util.CaseMethods;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;


public abstract class GeneralAllCasesTab extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// Error Labels
	private JLabel lblICivilUnrest;
	
	// Labels
	private JLabel lblCivilUnrest;
	
	// Comboboxes
	protected XComboBox cmbCivilUnrest;
	
	// Dropdown values
	private ArrayList<DropdownCodeStringValue> yesNoBlank;
	
	//Panels
	private JPanel civilUnrestPanel;

	protected abstract void validateMandatoryFields();
	protected abstract List<Object> getMandatoryFields();
	
	protected void initDropdownTypes() {
		if (yesNoBlank == null) {
			yesNoBlank = GeneralDropdownPopulation.getYesNo(true);
		}
	}
	
	protected void initCivilUnrestPanel(List<JLabel> validationFields) {
		lblCivilUnrest = new JLabel(getTranslation("general.allCases.label.civilUnrest"));
		lblICivilUnrest = CaseMethods.createErrorLabel(validationFields);
		cmbCivilUnrest = new XComboBox(true, lblICivilUnrest);
		cmbCivilUnrest.setGridBagLayout(true);
		if (!yesNoBlank.isEmpty()) {
			cmbCivilUnrest.setModel(new DefaultComboBoxModel(yesNoBlank.toArray()));
			cmbCivilUnrest.setRenderer(new DropdownBoxCellRender());
		}
		cmbCivilUnrest.addFocusListener(new MandatoryFieldsFocusListener());
		cmbCivilUnrest.setMandatory(true);
		CaseMethods.addRemoveFromMandatoryFields(cmbCivilUnrest, lblCivilUnrest, getMandatoryFields(), true);
	}
	
	protected JPanel getCivilUnrestPanel(CaseXPanel caseX) {
		if (civilUnrestPanel == null) {
			civilUnrestPanel = new JPanel();
			civilUnrestPanel.setLayout(new GridBagLayout());
			civilUnrestPanel.setBorder(BorderFactory.createTitledBorder(getTranslation("general.allCases.panel.civilUnrest")));

			GridBagConstraints gbcInner = CaseMethods.getDefaultGridBagConstraints();

			gbcInner.fill = GridBagConstraints.HORIZONTAL;
			gbcInner.gridy+=2;
			civilUnrestPanel.add(lblCivilUnrest, gbcInner);
			
			gbcInner.gridx++;
			gbcInner.gridy = 1;
			gbcInner.insets = XHIBITConstant.errorLabelInsets;
			gbcInner.weighty = 0.1;
			gbcInner.gridwidth = 2;
			civilUnrestPanel.add(lblICivilUnrest, gbcInner);

			gbcInner.insets = XHIBITConstant.nonContainerInsets;
			gbcInner.weighty = 0.2;
			gbcInner.gridy = 2;
			civilUnrestPanel.add(cmbCivilUnrest, gbcInner);

			CaseMethods.addChangeListeners(civilUnrestPanel.getComponents(), caseX);
		}

		return civilUnrestPanel;
	}

	protected void populateCivilUnrest(CaseBasicValue caseBasicValue) {
		DefaultComboBoxModel comboModel = (DefaultComboBoxModel) cmbCivilUnrest.getModel();
		for (int i =0; i < comboModel.getSize(); i++) {
			DropdownCodeStringValue dropdownValue = (DropdownCodeStringValue) comboModel.getElementAt(i);
			if ((dropdownValue.getCode() == null && caseBasicValue.getCivilUnrest() == null) ||
				(dropdownValue.getCode() != null && dropdownValue.getCode().equals(caseBasicValue.getCivilUnrest()))) {
				cmbCivilUnrest.setSelectedIndex(i);
				break;
			}
		}
	}
	
	protected String getCivilUnrest() {
		DropdownCodeStringValue selectedItem = (DropdownCodeStringValue) cmbCivilUnrest.getSelectedItem();
		return selectedItem != null ? selectedItem.getCode() : null;
	}
	
	private String getTranslation(String key) {
		return XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, key);
	}
	
	public class MandatoryFieldsFocusListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			//Don't want to do anything on gained only on lost
		}

		@Override
		public void focusLost(FocusEvent e) {
			validateMandatoryFields();
		}
	}
	
	public class MandatoryFieldsMFieldListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//Don't want to do anything on field entered only on exit
		}

		@Override
		public void fieldExited(FocusEvent event) {
			validateMandatoryFields();
		}
	}

}