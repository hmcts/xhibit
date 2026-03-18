package uk.gov.courtservice.xhibit.client.actions.results.common;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * Base class taken from DRSRReportPanel to show a month and year input.
 * 
 * @author westalll
 *
 */

public abstract class MonthYearDatePeriodReportPanel extends XPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JComboBox monthsComboBox;
	private JComboBox yearsComboBox;
	private JLabel selectMonthlyLabel;
	private JLabel previewLabel;
	private JPanel previewPanel;
	private JPanel dateSelectionPanel;
	protected XDialog parent;
	
	protected MonthYearDatePeriodReportModel model;
	
	
	public MonthYearDatePeriodReportPanel(final XDialog parent, final MonthYearDatePeriodReportModel model)
			throws CSRecoverableException {

		this.parent = parent;
		this.model = model;
		stepInitialise();
		jbInit();
	}

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		this.setPreferredSize(new Dimension(400, 100));
		// Content Panel Element

		// Panel - Monthly,Yearly ComboBox
		gbc.weighty = 0.50;
		dateSelectionPanel = initDateSelectionPanel();
		this.add(dateSelectionPanel, gbc);

		// Panel - Print Preview Panel
		gbc.weighty = 0.50;
		gbc.gridy++;
		previewPanel = initPreviewPanel();
		this.add(previewPanel, gbc);

	}
	
	protected JPanel initDateSelectionPanel(){ 
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		dateSelectionPanel = new JPanel();
		dateSelectionPanel.setLayout(new GridBagLayout());
		
		// Add the panel elements
		gbc.weightx = 0.05;
		selectMonthlyLabel = new JLabel(getSelectMonthLabel());
		dateSelectionPanel.add(selectMonthlyLabel, gbc);
		gbc.gridx++;
		gbc.weightx = 0.50;
		monthsComboBox = new JComboBox(); 
		Calendar cal = Calendar.getInstance();
		int thisMonth = cal.get(Calendar.MONTH);
		int lastMonth = thisMonth==0?11:thisMonth -1;
		monthsComboBox.setModel(new DefaultComboBoxModel(getMonths()));
		monthsComboBox.setSelectedIndex(lastMonth);
		dateSelectionPanel.add(monthsComboBox, gbc);	
		gbc.gridx++;
		gbc.weightx = 0.45;		
		yearsComboBox = new JComboBox(); 
		String[]years = getYears();
		yearsComboBox.setModel(new DefaultComboBoxModel(years));
		dateSelectionPanel.add(yearsComboBox, gbc);	
		return dateSelectionPanel;
	}
	
	
	
	protected abstract String getSelectMonthLabel();
	
	protected abstract String getPreviewLabel();
	
	
	private JPanel initPreviewPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		previewPanel = new JPanel();
		previewPanel.setLayout(new GridBagLayout());
		previewLabel = new JLabel(getPreviewLabel());
		previewPanel.add(previewLabel, gbc);
		return previewPanel;
	}
	

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		 moveScreenToModel();

	}
	
	public abstract void stepDeinitialise(boolean update) throws CSRecoverableException;
	
	protected abstract String[]getYears();
	
	
	private String[]getMonths(){
		String[]strMonths = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		return strMonths;
	}
	
	protected void moveScreenToModel() throws CSRecoverableException {
		// Update the model from the screen
		String month = (String) monthsComboBox.getSelectedItem();		
		model.setMonthPeriod(month);
		String year = (String) yearsComboBox.getSelectedItem();
		model.setYearPeriod(year);
	}
	

	@Override
	public void stepInitialise() throws CSRecoverableException {

	}

	@Override
	public void stepActivate() throws CSRecoverableException {
	
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		
	}
}

