package uk.gov.courtservice.xhibit.client.casemanagement.caselinking;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;


import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseLinkingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;


public class CaseUnlinkingPanel extends XPanel {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private CaseUnlinkingModel caseUnlinkingModel;
	private XDialog parent;
	private XhibitApplicationController xac;
	
	private String defaultError = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"warningDialog.defaultError");
	
	private String commonDefendants = "<html><body style='text-align: center'>The case selected has defendants in common" + 
									  "<br/><br/>with other cases in the group." + 
									  "<b><br/><br/>Do you want to continue?</b></html>";
	
	private JLabel lblCaseNumber; 
	private JLabel lblCaseTitle;
	private JLabel lblNumCasesLinked;
	
	private XTextField txtCaseNumber;
	private XTextField txtCaseTitle;
	
	private JPanel linkedCasesPanel;
	private JTable linkedCasesTable;
	private JScrollPane scrollPane;
	
	private JButton btnUnlink;
	
	private JButton btnCancel;	
	
	private CaseControllerBeanBusinessDelegate caseDelegate = null;
	private DefendantControllerBeanBusinessDelegate defendantDelegate = null;
	
	public CaseUnlinkingPanel(XDialog parent, CaseUnlinkingModel caseUnlinkingModel, XhibitApplicationController xac) throws CSRecoverableException {
		this.parent = parent;
		this.caseUnlinkingModel = caseUnlinkingModel;
		this.xac = xac;
		stepInitialise();
		jbInit();
		stepUpdateViewState();
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
		lblCaseNumber = new JLabel("Case number");
		lblCaseTitle = new JLabel("Case Title");
		lblNumCasesLinked = new JLabel("<n> cases linked to this case.");
		
		txtCaseNumber = new XTextField();
		txtCaseNumber.setColumns(15);
		txtCaseNumber.setMinimumSize(txtCaseNumber.getPreferredSize());
		txtCaseNumber.setEnabled(false);
		
		txtCaseTitle = new XTextField();
		txtCaseTitle.setColumns(30);
		txtCaseTitle.setMinimumSize(txtCaseTitle.getPreferredSize());
		txtCaseTitle.setEnabled(false);
		
		btnUnlink = new JButton("Unlink");
		btnUnlink.setEnabled(false);
		btnUnlink.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unlinkCases();
			}
		});
		
		CustomButtonPanel buttonPanel = (CustomButtonPanel)parent.getButtonPanel();
		btnCancel = buttonPanel.addButton("btnCancel", false, false);
		btnCancel.setText("Cancel");
	}


	@Override
	public void stepActivate() throws CSRecoverableException {
	}


	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		
		CaseBasicValue cbv = caseDelegate.getCase(caseUnlinkingModel.getCaseId());		
		
		parent.setTitle("View/Un-Link Case Links - " + cbv.getCaseType() + cbv.getCaseNumber());
		txtCaseNumber.setText(cbv.getCaseType() + cbv.getCaseNumber());
		txtCaseTitle.setText(cbv.getCaseTitle());
		
		ArrayList<CaseLinkingValue> clvColl = (ArrayList<CaseLinkingValue>) 
					caseDelegate.findActiveCasesWithGroupNumber(XhibitSingleton.getInstance().getCourtId(), 
								cbv.getCaseGroupNumber());

		lblNumCasesLinked.setText(clvColl.size() - 1 + " cases linked to this case.");
		
		// populate table
		DefaultTableModel model = (DefaultTableModel) linkedCasesTable.getModel();
		
		model.setRowCount(0);	// clear table
		
		for (CaseLinkingValue clv : clvColl) {
			model.addRow(new Object[]{ clv.getCaseType() + clv.getCaseNumber(), 
									   clv.getCaseTitle(), clv.getCaseId() });
		}
	}


	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		
	}


	@Override
	public void stepDeactivate() throws CSRecoverableException {
		
	}


	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (btnCancel.equals(getDeinitialiseSource())) {
			int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel this process?", "Are you sure?", 
												  JOptionPane.YES_NO_OPTION);
			if (JOptionPane.YES_OPTION == n) {
				parent.clearStatusBarScreenCode();
				parent.dispose();
			}
		}
	}

	
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(600, 300));
		
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		
		gbc.weightx = 0.1;
		gbc.weighty = 0.05;
		add(lblCaseNumber, gbc);
		
		gbc.gridy++;
		add(lblCaseTitle, gbc);
		
		gbc.gridwidth = 2;
		gbc.gridy++;
		add(lblNumCasesLinked, gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx++;
		gbc.gridy = 0;
		gbc.weightx = 0.9;
		add(txtCaseNumber, gbc);
		
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(txtCaseTitle, gbc);
	
		gbc.gridwidth = 2;
		gbc.weighty = 0.95;
		gbc.gridy+=2;
		gbc.gridx=0;
		gbc.fill = GridBagConstraints.BOTH;
		add(getLinkedCasesPanel(), gbc);
		
		gbc.gridwidth = 1;
		gbc.weighty = 0.05;
		gbc.gridx++;
		gbc.gridy++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		add(btnUnlink, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridx--;
		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(new JSeparator(), gbc);
	}
	
	
	// Standard grid bag constraints
	private GridBagConstraints getDefaultGridBagConstraints() {
		GridBagConstraints gbc = null;
		gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0 ,0);
		return gbc;
	}	
	
	private JPanel getLinkedCasesPanel() {
		if (linkedCasesPanel == null) { 
			linkedCasesPanel = new JPanel();
			linkedCasesPanel.setLayout(new GridBagLayout());
			linkedCasesPanel.setBorder(BorderFactory.createTitledBorder("Linked Cases - Select one or more records to un-link"));
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			
			//linkedCasesTable
			linkedCasesTable = new JTable();
			linkedCasesTable.setModel(new DefaultTableModel(new String[] {
				"Case Number", "Case Title", "CaseId"	
			}, 0));
			
			// Set widths of table to properly pre-size it
			linkedCasesTable.getColumnModel().getColumn(0).setPreferredWidth(50);
			linkedCasesTable.getColumnModel().getColumn(1).setPreferredWidth(220);
			linkedCasesTable.getColumnModel().removeColumn(linkedCasesTable.getColumnModel().getColumn(2));
			
			linkedCasesTable.setDefaultEditor(Object.class, null);
			linkedCasesTable.getTableHeader().setReorderingAllowed(false);
			
			linkedCasesTable.setPreferredScrollableViewportSize(linkedCasesTable.getPreferredSize());
			linkedCasesTable.setFillsViewportHeight(true);
			linkedCasesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			linkedCasesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (linkedCasesTable.getSelectedRow() >= 0) {
						btnUnlink.setEnabled(true);
					} else {
						btnUnlink.setEnabled(false);
					}
				}
			});
			
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(linkedCasesTable);
			linkedCasesPanel.add(scrollPane, gbc);
		}
		
		return linkedCasesPanel;
	}
	
	private void unlinkCases() {
		int row = linkedCasesTable.getSelectedRow();
		
		caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		
		try {
			int selectedCaseId = (Integer) linkedCasesTable.getModel().getValueAt(row, 2);
			CaseBasicValue cbv = caseDelegate.getCase(selectedCaseId);
			
			ArrayList<CaseLinkingValue> clvColl = (ArrayList<CaseLinkingValue>)
					caseDelegate.findCommonDefendantsWithGroupNumber(cbv.getCaseId(), cbv.getCourtID(), cbv.getCaseGroupNumber());
			
			ArrayList<CaseLinkingValue> clvCollGroup = (ArrayList<CaseLinkingValue>) 
					caseDelegate.findActiveCasesWithGroupNumber(XhibitSingleton.getInstance().getCourtId(), 
								cbv.getCaseGroupNumber());
			
			boolean unlinked = false; 
			
			if (clvColl.size() > 0) {
				int result = JOptionPane.showConfirmDialog(null, new JLabel(commonDefendants, SwingConstants.CENTER), "Warning", JOptionPane.ERROR_MESSAGE);
				
				if (result == JOptionPane.YES_OPTION) {
					caseDelegate.removeGroupNumber(selectedCaseId, 
							XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME));
					
					JOptionPane.showMessageDialog(null, "Case unlinked successfully", "Un-link successful", JOptionPane.INFORMATION_MESSAGE);
					unlinked = true;
				}
			} else {
				caseDelegate.removeGroupNumber(selectedCaseId, 
						XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME));

				JOptionPane.showMessageDialog(null, "Case unlinked successfully", "Un-link successful", JOptionPane.INFORMATION_MESSAGE);
				unlinked = true;
			}
			
			boolean noLinks = false;
			// If it's just this case grouped with a single other case, then unlink the other case too
			if (clvCollGroup.size() == 2 && unlinked) {
				if(selectedCaseId == clvCollGroup.get(0).getCaseId()) {	// to find the other one, i.e. the non selected case id
					caseDelegate.removeGroupNumber(clvCollGroup.get(1).getCaseId(), 
							XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME));
			
					noLinks = true;
				} else {
					caseDelegate.removeGroupNumber(clvCollGroup.get(0).getCaseId(), 
							XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME));
					
					noLinks = true;
				}
			}
			
			// unlinked from itself
			if (selectedCaseId == caseUnlinkingModel.getCaseId() || noLinks) {
				String titleString = "Case Number: " + cbv.getCaseType() + cbv.getCaseNumber().toString();
				
				JOptionPane.showMessageDialog((Component) null, "The case is not linked to any others", 
						  titleString, JOptionPane.INFORMATION_MESSAGE);
				
				parent.dispose();
			} else {
				stepUpdateViewState();
			}
		} catch (CaseControllerException e) {
			log.debug("Couldn't find case at row: " + row);
			JOptionPane.showMessageDialog(null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
		} catch (CSRecoverableException e) {
			log.debug("Couldnt execute step update view state");
			JOptionPane.showMessageDialog(null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
		} 
	}
}
