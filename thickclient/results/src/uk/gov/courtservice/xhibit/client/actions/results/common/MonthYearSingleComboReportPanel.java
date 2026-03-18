package uk.gov.courtservice.xhibit.client.actions.results.common;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
 * Base class to select a single Month / Year.
 * 
 * @author westalll
 *
 */

public abstract class MonthYearSingleComboReportPanel extends XPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JComboBox monthsComboBox;
	private JLabel selectMonthlyLabel;
	private JLabel previewLabel;
	private JPanel previewPanel;
	private JPanel dateSelectionPanel;
	protected XDialog parent;
	
	protected MonthYearDatePeriodReportModel model;
	
	
	public MonthYearSingleComboReportPanel(final XDialog parent, final MonthYearDatePeriodReportModel model)
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
		gbc.weighty = 0.01;
		dateSelectionPanel = initDateSelectionPanel();
		this.add(dateSelectionPanel, gbc);

		// Panel - Print Preview Panel
		gbc.weighty = 0.02;
		gbc.gridy++;
		previewPanel = initPreviewPanel();
		this.add(previewPanel, gbc);

	}
	
	private JPanel initDateSelectionPanel(){ 
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		dateSelectionPanel = new JPanel();
		dateSelectionPanel.setLayout(new GridBagLayout());
		
		// Add the panel elements
		gbc.weightx = 0.10;
		selectMonthlyLabel = new JLabel(getSelectMonthLabel());
		dateSelectionPanel.add(selectMonthlyLabel, gbc);
		gbc.gridx++;
		gbc.weightx = 0.90;
		monthsComboBox = new JComboBox(); 
		monthsComboBox.setModel(new DefaultComboBoxModel(getStringDates()));
		monthsComboBox.setSelectedIndex(0);
		dateSelectionPanel.add(monthsComboBox, gbc);	
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
	
	protected String[] getStringDates() {
		final DateFormat df = new SimpleDateFormat("MMM-yyyy");

		List<String> dateStrings = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();

		dateStrings.add(df.format(cal.getTime()));
		for (int i = 0; i < getNumberOfPreviousMonthsToShowInCalendar(); i++) {
			cal.add(Calendar.MONTH, -1);
			dateStrings.add(df.format(cal.getTime()));
		}
		return dateStrings.toArray(new String[0]);
	}
	
	/**
	 * Number of months to show in Calendar. Must be > 1
	 */
	public abstract int getNumberOfPreviousMonthsToShowInCalendar();
	
	public abstract void stepDeinitialise(boolean update) throws CSRecoverableException;
	
	
	private void moveScreenToModel() throws CSRecoverableException {
		// Update the model from the screen
		String monthYears[] = ((String) monthsComboBox.getSelectedItem()).split("-");
		model.setMonthPeriod(monthYears[0]);
		model.setYearPeriod(monthYears[1]);
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
