package uk.gov.courtservice.xhibit.client.casemanagement.caselinking;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
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

import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseLinkingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;

public class CaseLinkingPanel extends XPanel {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private CaseLinkingModel caseLinkingModel;
	private XDialog parent;

	private JPanel caseNumberTitlePanel;
	private JLabel lblCaseNumberReadOnly;
	private JLabel lblCaseTitleReadOnly;
	private XTextField txtCaseNumberReadOnly;
	private XTextField txtCaseTitleReadOnly;
	
	private JPanel linkedCaseDetailsPanel;
	private JLabel lblNumCasesLinked;
	private JButton btnLinkedCaseDetails;

	private JPanel caseDetailsPanel;
	private JLabel lblICaseNumberToLink;
	private JLabel lblCaseNumberToLink;
	private JLabel lblCaseTitleToLink;
	private XTextField txtCaseNumberToLink;
	private XTextField txtCaseTitleToLink;
	private JButton btnSearch;
	private JButton btnViewLinks;
	
	private JButton btnCreateLink;
	
	private JButton btnCancel;
	
	private XhibitApplicationController xac;
	
	private CaseControllerBeanBusinessDelegate caseDelegate = null;
	private DefendantControllerBeanBusinessDelegate defendantDelegate = null;
			
	private CaseBasicValue caseToLink;
	private CaseBasicValue readOnlyCase;
	
	private String defaultError = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"warningDialog.defaultError");
	
	public CaseLinkingPanel(XDialog parent, CaseLinkingModel caseLinkingModel, XhibitApplicationController xac) throws CSRecoverableException {
		this.parent = parent;
		this.caseLinkingModel = caseLinkingModel;
		this.xac = xac;
		stepInitialise();
		jbInit();
		stepUpdateViewState();
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
		// top panel
		lblCaseNumberReadOnly = new JLabel("Case Number");
		lblCaseTitleReadOnly = new JLabel("Case Title");
		txtCaseNumberReadOnly = new XTextField();
		txtCaseNumberReadOnly.setEnabled(false);
		txtCaseTitleReadOnly = new XTextField();
		txtCaseTitleReadOnly.setEnabled(false);
		
		// middle panel
		lblNumCasesLinked = new JLabel("<n> cases linked to this case");
		btnLinkedCaseDetails = new JButton("Linked case details");
		btnLinkedCaseDetails.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getLinkedCaseDetails();				
			}
		});
		
		// bottom panel
		lblICaseNumberToLink = new JLabel(" ");
		lblICaseNumberToLink.setForeground(Color.RED);
		lblCaseNumberToLink = new JLabel("Case Number");
		lblCaseTitleToLink = new JLabel("Case Title");
		txtCaseNumberToLink = new XTextField();
		txtCaseNumberToLink.setMaxLength(9);
		txtCaseNumberToLink.setUpperCase(true);
		txtCaseNumberToLink.setAlphaNumeric(true);
		txtCaseNumberToLink.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				btnSearch.setEnabled(checkCaseNumberLength());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				btnSearch.setEnabled(checkCaseNumberLength());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				btnSearch.setEnabled(checkCaseNumberLength());
			}
		});
		
		txtCaseTitleToLink = new XTextField();
		txtCaseTitleToLink.setEnabled(false);
		btnSearch = new JButton("Search");
		btnSearch.setEnabled(false);
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchForLinkedCases();
			}
		});
		
		btnViewLinks = new JButton("View Links");
		btnViewLinks.setEnabled(false);
		btnViewLinks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewLinks();
			}
		});
		
		btnCreateLink = new JButton("Create Link");
		btnCreateLink.setEnabled(false);
		btnCreateLink.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createLink();
			}
		});
		
		CustomButtonPanel buttonPanel = (CustomButtonPanel)parent.getButtonPanel();
		btnCancel = buttonPanel.addButton("btnCancel", false, false);
		btnCancel.setText("Close");
	}


	@Override
	public void stepActivate() throws CSRecoverableException {
		caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		readOnlyCase = caseDelegate.getCase(caseLinkingModel.getCaseId());
		
		// fill in read-only fields
		txtCaseNumberReadOnly.setText(readOnlyCase.getCaseType() + 
									  readOnlyCase.getCaseNumber().toString());
		
		txtCaseTitleReadOnly.setText(readOnlyCase.getCaseTitle());
		boolean linkedCaseDetailsEnabled = false;
		
		Integer numLinkedCases = 0;
		if (readOnlyCase.getCaseGroupNumber() != null) {
			ArrayList<CaseLinkingValue> clvColl = (ArrayList<CaseLinkingValue>) caseDelegate.findActiveCasesWithGroupNumber(
														XhibitSingleton.getInstance().getCourtId(),
														readOnlyCase.getCaseGroupNumber());
			numLinkedCases = clvColl.size();
			if (numLinkedCases > 0) {
				for (CaseLinkingValue clv : clvColl) {
					if (clv.getCaseId().equals(readOnlyCase.getCaseId())) {
						clvColl.remove(clv);
						numLinkedCases = clvColl.size();
						break;
					}
				}
				if (numLinkedCases > 0) {
					linkedCaseDetailsEnabled = true;
				}
			} 
		}
		
		btnLinkedCaseDetails.setEnabled(linkedCaseDetailsEnabled);
		
		lblNumCasesLinked.setText(numLinkedCases.toString() + " cases linked to this case.");
	}


	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		
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
			((CaseLinkingDialog) parent).processCancel();
		} 
	}

	
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		// to mimic the spacing added by the border panel for the caseDetailsPanel, so they match
		gbc.insets = new Insets(5, 6, 5, 6);
		add(getCaseNumberTitlePanel(), gbc);
		
		gbc.gridy++;
		add(getLinkedCaseDetailsPanel(), gbc);
		
		gbc.gridy++;
		gbc.insets = new Insets(5, 0, 5, 0);
		add(getCaseDetailsPanel(), gbc);
		
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 0, 5, 10);
		add(btnCreateLink, gbc);
		
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridy++;
		add(new JSeparator(), gbc);
	}
	
	private JPanel getCaseNumberTitlePanel() {
		if (caseNumberTitlePanel == null) {
			caseNumberTitlePanel = new JPanel();
			caseNumberTitlePanel.setLayout(new GridBagLayout());
			
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			
			gbc.weightx = 0.05;
			caseNumberTitlePanel.add(lblCaseNumberReadOnly, gbc);
			
			gbc.gridy++;
			caseNumberTitlePanel.add(lblCaseTitleReadOnly, gbc);

			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 0.95;
			gbc.gridy = 0;
			gbc.gridx++;
			txtCaseNumberReadOnly.setColumns(15);
			txtCaseNumberReadOnly.setMinimumSize(txtCaseNumberReadOnly.getPreferredSize());
			caseNumberTitlePanel.add(txtCaseNumberReadOnly, gbc);
			
			gbc.gridy++;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			txtCaseTitleReadOnly.setColumns(30);
			txtCaseTitleReadOnly.setMinimumSize(txtCaseTitleReadOnly.getPreferredSize());
			caseNumberTitlePanel.add(txtCaseTitleReadOnly, gbc);
		}
		
		return caseNumberTitlePanel;
	}
	
	private JPanel getLinkedCaseDetailsPanel() {
		if (linkedCaseDetailsPanel == null) {
			linkedCaseDetailsPanel = new JPanel();
			linkedCaseDetailsPanel.setLayout(new GridBagLayout());
			
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 0.5;
			linkedCaseDetailsPanel.add(lblNumCasesLinked, gbc);
			
			gbc.gridx++;
			gbc.anchor = GridBagConstraints.EAST;
			linkedCaseDetailsPanel.add(btnLinkedCaseDetails, gbc);
		}
		
		return linkedCaseDetailsPanel;
	}
	
	private JPanel getCaseDetailsPanel() {
		if (caseDetailsPanel == null) {
			caseDetailsPanel = new JPanel();
			caseDetailsPanel.setLayout(new GridBagLayout());
			caseDetailsPanel.setBorder(BorderFactory.createTitledBorder("Enter case number to link and click Search"));
			
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.gridx++;
			gbc.weighty = 0.1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			caseDetailsPanel.add(lblICaseNumberToLink, gbc);
			
			gbc.gridy++;
			gbc.gridx = 0;
			gbc.weightx = 0.05;
			gbc.weighty = 0.2;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			caseDetailsPanel.add(lblCaseNumberToLink, gbc);
			
			gbc.gridy++;
			caseDetailsPanel.add(lblCaseTitleToLink, gbc);
			
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 0.95;
			gbc.gridy = 1;
			gbc.gridx++;
			txtCaseNumberToLink.setColumns(15);
			txtCaseNumberToLink.setMinimumSize(txtCaseNumberToLink.getPreferredSize());
			caseDetailsPanel.add(txtCaseNumberToLink, gbc);
			
			gbc.gridy++;
			gbc.gridwidth = 2;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			txtCaseTitleToLink.setColumns(30);
			txtCaseTitleToLink.setMinimumSize(txtCaseTitleToLink.getPreferredSize());
			caseDetailsPanel.add(txtCaseTitleToLink, gbc);
			
			gbc.gridwidth = 1;
			gbc.gridy = 1;
			gbc.gridx++;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.EAST;
			caseDetailsPanel.add(btnSearch, gbc);
			
			gbc.gridy+=2;
			caseDetailsPanel.add(btnViewLinks, gbc);
		}
		
		return caseDetailsPanel;
	}
	
	// Standard grid bag constraints
	private GridBagConstraints getDefaultGridBagConstraints() {
		GridBagConstraints gbc = null;
		gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0 ,0);
		return gbc;
	}	
	
	private void getLinkedCaseDetails() {
		try {
			LinkedCaseDetailsModel linkedCaseDetailsModel = new LinkedCaseDetailsModel();
			linkedCaseDetailsModel.setCaseId(caseLinkingModel.getCaseId());
			
			CaseBasicValue cbv = caseDelegate.getCase(caseLinkingModel.getCaseId());
			linkedCaseDetailsModel.setCaseGroupNumber(cbv.getCaseGroupNumber());
			LinkedCaseDetailsDialog linkedCaseDetailsDialog = new LinkedCaseDetailsDialog(xac, linkedCaseDetailsModel);
			linkedCaseDetailsDialog.setVisible(true);
		} catch (CSRecoverableException e) {
			log.debug("Couldn't find case: " + caseLinkingModel.getCaseId());
			JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void searchForLinkedCases() {
		// if link present
		if (txtCaseNumberToLink.getText().matches("^[AST]{1}[0-9]{8}$")) {
			lblICaseNumberToLink.setText(" ");
			caseLinkingModel.setCaseSearched(true);
			
			// search on case number
			String caseNumber = txtCaseNumberToLink.getText();
			if (caseNumber.equals(txtCaseNumberReadOnly.getText())) {
				// no cases found
				JOptionPane.showMessageDialog(null, "Can't link the entered case with itself.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {				
				Integer caseIdToLink = caseDelegate.findCaseId(caseNumber.substring(0, 1), 
										Integer.parseInt(caseNumber.substring(1, 9)), 
										XhibitSingleton.getInstance().getCourtId());
				
				if (caseIdToLink > 0) {
					defendantDelegate = XhibitDelegateHelper.getDefendantDelegate();
					
					Collection<DefendantOnCaseBasicValue> defendantsOnCase = (ArrayList<DefendantOnCaseBasicValue>) defendantDelegate
							.findByCaseId(caseIdToLink);
					
					if (defendantsOnCase.size() > 0) {		
						for (DefendantOnCaseBasicValue defendantOnCase : defendantsOnCase) {
							if ((null != defendantOnCase.getResultsVerified())
									&& (defendantOnCase.getResultsVerified().equals("E"))) {
								throw new CaseControllerException();
							}
						}			
					} else {
						throw new CaseControllerException();
					}
					caseToLink = caseDelegate.getCase(caseIdToLink);
					txtCaseTitleToLink.setText(caseToLink.getCaseTitle());
					
					if (caseToLink.getCaseGroupNumber() != null) {
						btnViewLinks.setEnabled(true);	
					} else {
						btnViewLinks.setEnabled(false);
					}

					btnCreateLink.setEnabled(true);
				} 
			} catch (CaseControllerException e) {
				log.debug("Couldn't find case: " + txtCaseNumberToLink.getText());
				// no cases found
				JOptionPane.showMessageDialog(null, "No results found for the entered criteria", 
						"Error", JOptionPane.ERROR_MESSAGE);
			} 
		} else {
			// error label?
			btnViewLinks.setEnabled(false);
			btnCreateLink.setEnabled(false);
			lblICaseNumberToLink.setText("Invalid Case Number format");
		}
	}
	
	private void viewLinks() {
		try {
			// in case user edits field after search but before press view links,
			// repopulate to avoid confusion
			txtCaseNumberToLink.setText(caseToLink.getCaseType() + caseToLink.getCaseNumber());
			LinkedCaseDetailsModel linkedCaseDetailsModel = new LinkedCaseDetailsModel();
			linkedCaseDetailsModel.setCaseId(caseToLink.getCaseId());
			
			linkedCaseDetailsModel.setCaseGroupNumber(caseToLink.getCaseGroupNumber());
			LinkedCaseDetailsDialog linkedCaseDetailsDialog = new LinkedCaseDetailsDialog(xac, linkedCaseDetailsModel);
			linkedCaseDetailsDialog.setVisible(true);
		} catch (CSRecoverableException e) {
			log.debug("Couldn't find case: " + caseLinkingModel.getCaseId());
			JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void createLink() {
		boolean linkCreated = false;
		
		// re-populate readOnlyCase and caseToLink
		try {
			readOnlyCase = caseDelegate.getCase(readOnlyCase.getCaseId());
			caseToLink = caseDelegate.getCase(caseToLink.getCaseId());
		} catch (CaseControllerException e) {
			log.debug("Couldn't find cases, caseId = " + readOnlyCase.getCaseId() + " and: " + caseToLink.getCaseId());
			JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		int groupNum;
		boolean updateReadOnly = false;
		boolean updateToLink = false;
		
		// Link Cases scenario 1: neither has group num
		if (readOnlyCase.getCaseGroupNumber() == null && caseToLink.getCaseGroupNumber() == null) {
			// create new case group number, assign to both
			groupNum = caseDelegate.generateCaseGroupNumber(XhibitSingleton.getInstance().getCourtId());
			
			readOnlyCase.setCaseGroupNumber(groupNum);
			caseToLink.setCaseGroupNumber(groupNum);
			updateReadOnly = true;
			updateToLink = true;
		} else if (readOnlyCase.getCaseGroupNumber() != null && caseToLink.getCaseGroupNumber() == null) {
			// scenario 2: case to be linked has group num, case to be linked to doesn't
			caseToLink.setCaseGroupNumber(readOnlyCase.getCaseGroupNumber());
			updateToLink = true;
		} else if (readOnlyCase.getCaseGroupNumber() == null && caseToLink.getCaseGroupNumber() != null) {
			// scenario 3: case to be linked has no group num, case to be linked does
			readOnlyCase.setCaseGroupNumber(caseToLink.getCaseGroupNumber());
			updateReadOnly = true;
		} else if (readOnlyCase.getCaseGroupNumber() != null && caseToLink.getCaseGroupNumber() != null) {
			// scenario 5: both have same group nums
			if (readOnlyCase.getCaseGroupNumber().equals(caseToLink.getCaseGroupNumber())) {
				JOptionPane.showMessageDialog((Component) null, 
						"The cases are already linked", "Error", JOptionPane.ERROR_MESSAGE);
				//reset this so you don't get the 'this hasn't been linked' message when clicking cancel
				caseLinkingModel.setCaseSearched(false);

				return;	// avoid amending cases etc.
			} else {
				// scenario 4: both have group num, different so update both sets of groups
				// create new case group number, assign to both sets of group numbers
				groupNum = caseDelegate.generateCaseGroupNumber(XhibitSingleton.getInstance().getCourtId());
				
				try { 
					ArrayList<CaseLinkingValue> clvCollReadOnly = (ArrayList<CaseLinkingValue>) caseDelegate.findActiveCasesWithGroupNumber(
							XhibitSingleton.getInstance().getCourtId(),
							readOnlyCase.getCaseGroupNumber());
					
					ArrayList<CaseLinkingValue> clvCollToLink = (ArrayList<CaseLinkingValue>) caseDelegate.findActiveCasesWithGroupNumber(
							XhibitSingleton.getInstance().getCourtId(),
							caseToLink.getCaseGroupNumber());
					
					for (CaseLinkingValue clvReadOnly : clvCollReadOnly) {
						CaseBasicValue cbvReadOnly = caseDelegate.getCase(clvReadOnly.getCaseId());
						cbvReadOnly.setCaseGroupNumber(groupNum);
						caseDelegate.amendCaseOnly(cbvReadOnly, 
								XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME));
					}
					
					for (CaseLinkingValue clvToLink : clvCollToLink) {
						CaseBasicValue cbvToLink = caseDelegate.getCase(clvToLink.getCaseId());
						cbvToLink.setCaseGroupNumber(groupNum);
						caseDelegate.amendCaseOnly(cbvToLink, 
								XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME));
					}
					
					// re-populate <n> cases linked to this case
					stepActivate();
				} catch (CaseControllerException e) {
					log.debug("Couldn't update cases.");
					JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
				} catch (CSRecoverableException e) {
					log.debug("Failed calling stepActivate()");
					JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		linkCreated = updateCasesAndRepopulate(readOnlyCase, caseToLink, updateReadOnly, updateToLink);
		
		if (linkCreated) {
			JOptionPane.showMessageDialog((Component) null, "Case linked successfully.", 
					"Case linked successfully", 1);
			caseLinkingModel.setCaseLinked(true);
		}
	}
	
	public boolean updateCasesAndRepopulate(CaseBasicValue readOnlyCase, CaseBasicValue caseToLink, 
			boolean updateReadOnly, boolean updateToLink) {
		caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		
		try {
			if (updateToLink) {
				caseDelegate.amendCaseOnly(caseToLink, 
						XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME));
			}
			
			if (updateReadOnly) { 
				caseDelegate.amendCaseOnly(readOnlyCase, 
						XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME));
			}
			
			// re-populate readOnlyCase and caseToLink
			readOnlyCase = caseDelegate.getCase(readOnlyCase.getCaseId());
			caseToLink = caseDelegate.getCase(caseToLink.getCaseId());
			
			// re-populate <n> cases linked to this case
			stepActivate();

			txtCaseNumberToLink.setText("");
			txtCaseTitleToLink.setText("");
			btnViewLinks.setEnabled(false);
			btnCreateLink.setEnabled(false);
			
			return true;
		} catch (CaseControllerException e) {
			log.debug("Couldn't update cases.");
			JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (CSRecoverableException e) {
			log.debug("Failed calling stepActivate()");
			JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	private boolean checkCaseNumberLength() {
		if (!txtCaseNumberToLink.getText().isEmpty()) {
			if (txtCaseNumberToLink.getText().length() == 9) {
				return true;
			}
		}

		return false;
	}
}
