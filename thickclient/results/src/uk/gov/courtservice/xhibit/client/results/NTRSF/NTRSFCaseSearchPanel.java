package uk.gov.courtservice.xhibit.client.results.NTRSF;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class NTRSFCaseSearchPanel extends XPanel {
	private static final long serialVersionUID = 1L;
	private NTRSFCaseSearchModel caseSearchModel = null;
	private XDialog parent;
	// --- Search Panel ---
	private JPanel searchPanel = null;
	private JLabel lblCaseNumber = new JLabel("Case Number");
	private JLabel lblICaseNumber = new JLabel(" ");
	private XTextField txtCaseNumber = new XTextField(9, "^[A-Za-z]{1}[0-9]{8}$", lblICaseNumber, false);
	private DocumentListener searchPanelDocListener;
	// --- Case Panel ---
	
	private JButton btnBack;
	private JButton btnSummary;
	private JButton btnOk;
	private JButton btnCancel;
	private Integer caseId;
	private Integer courtId;
	private CaseControllerBeanBusinessDelegate caseDelegate = null;
	private String caseType;
	private Integer caseNumber;
	private String caseTitle;
	private boolean cancelled;

	// *******************************************************************************
	// * public CaseSearchPanel(XDialog parent, CaseSearchModel caseSearchModel)
	// *
	// * Purpose : Constructor
	// * To call : parent - dialog parent class
	// * caseSearchModel - class model data
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	public NTRSFCaseSearchPanel(XDialog parent, NTRSFCaseSearchModel caseSearchModel) throws CSRecoverableException {
		this.parent = parent;
		this.caseSearchModel = caseSearchModel;
		stepInitialise();
		jbInit();
	}

	// *******************************************************************************
	// * private void jbInit()
	// *
	// * Purpose : Add components to screen
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private void jbInit() {
		parent.setPreferredSize(new Dimension(400, 200));
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		// --- Add panels to dialog ---
		this.add(getSearchPanel(), gbc);
		searchPanel.setVisible(true);
		// --- Add custom buttons ---
		CustomButtonPanel buttonPanel = (CustomButtonPanel) parent.getButtonPanel();
		btnBack = buttonPanel.addButton("", false, true);
		btnBack.setText(
				ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "caseSearch.btnBack.btnText"));
		btnBack.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
				"caseSearch.btnBack.tooltipText"));
		btnBack.setVisible(false);
		btnSummary = buttonPanel.addButton("", false, false);
		btnSummary.setText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
				"caseSearch.btnSummary.btnText"));
		btnSummary.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
				"caseSearch.btnSummary.tooltipText"));
		btnSummary.setVisible(false);
		btnOk = buttonPanel.addButton("btnOk", false, true);
		btnOk.setEnabled(false);
		btnCancel = buttonPanel.addButton("btnCancel", true, false);
		parent.getRootPane().setDefaultButton(btnOk);
		// --- Add document listeners for each panel ---
		searchPanelDocListener = new SearchPanelDocListener();
		txtCaseNumber.getDocument().addDocumentListener(searchPanelDocListener);
		// --- Misc setup ---
		lblICaseNumber.setForeground(Color.RED);
	}

	// *******************************************************************************
	// * private GridBagConstraints getDefaultGridBagConstraints()
	// *
	// * Purpose : Get default set of gridbag constraints
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private GridBagConstraints getDefaultGridBagConstraints() {
		GridBagConstraints gbc = null;
		gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				XHIBITConstant.nonContainerInsets, 0, 0);
		return gbc;
	}

	// *******************************************************************************
	// * private JPanel getSearchPanel()
	// *
	// * Purpose : Get JPanel for case search
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private JPanel getSearchPanel() {
		if (null == searchPanel) {
			searchPanel = new JPanel();
			searchPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			// --- Set initial positions ---
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			searchPanel.setBorder(new TitledBorder(null, "Please enter a case number", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			// --- Add labels to layout ---
			searchPanel.add(lblCaseNumber, gbc);
			// --- Add error labels to layout ---
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weighty = 0.1;
			searchPanel.add(lblICaseNumber, gbc);
			// --- Set constraints for text fields ---
			gbc.gridx = 1;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.weightx = 0.9;
			gbc.weighty = 0.2;
			// --- Add text fields ---
			txtCaseNumber.setColumns(10);
			txtCaseNumber.setUpperCase(true);
			txtCaseNumber.setGridBagLayout(true);
			txtCaseNumber.setMinimumSize(txtCaseNumber.getPreferredSize());
			txtCaseNumber.setInvalidText("Please enter Case Type and Case Number");
			searchPanel.add(txtCaseNumber, gbc);
		}
		return searchPanel;
	}


	// *******************************************************************************
	// * private void Screen()
	// *
	// * Purpose : Update local data from model
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private void moveModelToScreen() {
		caseId = caseSearchModel.getCaseId();
		cancelled = caseSearchModel.getCancelled();
		caseType = caseSearchModel.getCaseType();
		caseNumber = caseSearchModel.getCaseNumber();
		courtId = caseSearchModel.getCourtId();
	}

	// *******************************************************************************
	// * private void moveScreenToModel
	// *
	// * Purpose : Update model from local data
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private void moveScreenToModel() {
		caseSearchModel.setCaseId(caseId);
		caseSearchModel.setCancelled(cancelled);
		caseSearchModel.setCaseType(caseType);
		caseSearchModel.setCaseNumber(caseNumber);
		caseSearchModel.setCourtId(courtId);
		caseSearchModel.setCaseTitle(caseTitle);
	}

	// *******************************************************************************
	// * public void stepInitialise()
	// *
	// * Purpose :
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepInitialise() throws CSRecoverableException {
		caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		courtId = XhibitSingleton.getInstance().getCourtId();
	}

	// *******************************************************************************
	// * public void stepActivate()
	// *
	// * Purpose :
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();
	}

	// *******************************************************************************
	// * public void stepUpdateViewState()
	// *
	// * Purpose :
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}

	// *******************************************************************************
	// * public void stepValidate()
	// *
	// * Purpose :
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
	}

	// *******************************************************************************
	// * public void stepDeactivate()
	// *
	// * Purpose :
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	// *******************************************************************************
	// * public void stepDeinitialise(boolean update)
	// *
	// * Purpose : update -
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		boolean gotResults = false;

		courtId = XhibitSingleton.getInstance().getCourtId();
		// === OK Button ===
		if (btnOk.equals(getDeinitialiseSource())) {
			caseSearchModel.setOk(true);
				// --- Check search parameters ---
				if (txtCaseNumber.getText().length() > 1) {
					if (!txtCaseNumber.hasError()) {
						gotResults = true;
					}
				}
				if (!txtCaseNumber.getText().isEmpty()) {
					gotResults = true;
				}
				if (gotResults && !txtCaseNumber.isNullOrEmpty()
						&& !isValidCaseType(txtCaseNumber.getText().substring(0, 1).toUpperCase())) {
					gotResults = false;
				}
				// --- Search for cases ---
				if (gotResults) {
					if (!txtCaseNumber.getText().isEmpty()) {
						// --- Search on case number ---
						if (txtCaseNumber.isRegexMatch()) {
							// --- Use full case number ---
							caseType = txtCaseNumber.getText().substring(0, 1).toUpperCase();
							String caseNumberString = txtCaseNumber.getText().substring(1,
									txtCaseNumber.getText().length());
							caseNumber = Integer.valueOf(caseNumberString);
							try {
								caseId = caseDelegate.findCaseId(caseType, caseNumber, courtId);
								 
								CaseBasicValue newCBV = caseDelegate.getCase(caseId);
								caseType = newCBV.getCaseType(); 
								caseNumber = newCBV.getCaseNumber();
								caseTitle = newCBV.getCaseTitle();
								cancelled = false;
								moveScreenToModel();
								parent.dispose();
							} catch (CaseControllerException ex) {
								gotResults = false;
							}
						}
					} 
			  }
				
				if (!gotResults) {
					JOptionPane.showMessageDialog(null,
							XHIBITConstant.getResource(XhibitBundles.Listings, "caseSearchOKConfirmationMessage"),
							XHIBITConstant.getResource(XhibitBundles.Listings, "caseSearchOKConfirmationTitle"),
							JOptionPane.INFORMATION_MESSAGE);
				}
			  	
		} else if (btnCancel.equals(getDeinitialiseSource())) {
			cancelled = true;
		}
	}

	private boolean isValidCaseType(String caseType) {
		boolean isValid = true;
		if (caseSearchModel.getValidCaseTypes() != null) {
			isValid = caseSearchModel.getValidCaseTypes().contains(caseType);
		}
		return isValid;
	}

	// *******************************************************************************
	// * private class SearchPanelDocListener implements DocumentListener
	// *
	// * Purpose : Custom DocumentListener class to only enable OK button when
	// either
	// * txtCaseNumber contain data, otherwise disable.
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private class SearchPanelDocListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			if (txtCaseNumber.getText() != null && txtCaseNumber.getText().length() > 0) {
				btnOk.setEnabled(true);
			} else {
				btnOk.setEnabled(false);
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			if (txtCaseNumber.getText() != null && txtCaseNumber.getText().length() > 0) {
				btnOk.setEnabled(true);
			} else {
				btnOk.setEnabled(false);
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			if (txtCaseNumber.getText() != null && txtCaseNumber.getText().length() > 0) {
				btnOk.setEnabled(true);
			} else {
				btnOk.setEnabled(false);
			}
		}
	}

}
