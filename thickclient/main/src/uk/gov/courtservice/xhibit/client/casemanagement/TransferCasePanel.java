package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import javax.ejb.FinderException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryControllerException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ChargesLogBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.bwhistory.BwHistoryValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayNTRSFReportAction;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class TransferCasePanel extends XPanel {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private TransferCaseModel transferCaseModel;
	private XDialog parent;
	//--- Panel ---
	private JPanel panel = null;
	private JLabel lblCaseNumber;
	private JLabel lblCaseTitle;
	private JLabel lblTransferredTo;
	private JLabel lblTransferDate;
	private JLabel lblICourtList = new JLabel(" ");
	private XComboBox cmbCourtList;
	private JLabel lblITransferDate = new JLabel(" ");
	private XDate dtTransferDate;
	//--- Buttons ---
	private JButton btnRevokeTransfer;
	private JButton btnTransferCase;
	private JButton btnCancel;
	//--- Other ---
	private Integer caseId;
	private String defaultError;
	private CaseControllerBeanBusinessDelegate caseDelegate = null;
	private BwHistoryControllerBeanBusinessDelegate bwHistoryDelegate;
	private DefendantControllerBeanBusinessDelegate defendantDelegate;


	//*******************************************************************************
	//* public TransferCasePanel(XDialog parent,
	//*                          TransferSearchModel transferCaseModel)
	//*
	//* Purpose : Constructor
	//* To call :            parent - dialog parent class
	//*           transferCaseModel - class model data
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	public TransferCasePanel(XDialog parent, TransferCaseModel transferCaseModel) throws CSRecoverableException {
		this.parent = parent;
		this.transferCaseModel = transferCaseModel;
		stepInitialise();
		jbInit();
		stepUpdateViewState();
	}


	//*******************************************************************************
	//* private void jbInit()
	//*
	//* Purpose : Add components to screen 
	//* To call : Nothing
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	private void jbInit() {
		parent.setPreferredSize(new Dimension(400,250));
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		//--- Add panels to dialog ---
		this.add(getPanel(), gbc);
		panel.setVisible(true);
		//---
	    lblCaseNumber.setBorder(new LineBorder(Color.BLACK));
	    lblCaseTitle.setBorder(new LineBorder(Color.BLACK));
	    lblITransferDate.setForeground(Color.RED);
		//--- Add custom buttons ---
		CustomButtonPanel buttonPanel = (CustomButtonPanel)parent.getButtonPanel();
		btnRevokeTransfer = buttonPanel.addButton("", false, true);
		btnRevokeTransfer.setText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.revokeTransfer.btnText"));
		btnRevokeTransfer.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.revokeTransfer.tooltipText"));
		btnRevokeTransfer.setVisible(true);
		btnTransferCase = buttonPanel.addButton("", false, false);
		btnTransferCase.setText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.transferCase.btnText"));
		btnTransferCase.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.transferCase.tooltipText"));
		btnTransferCase.setEnabled(false);
		btnTransferCase.setVisible(true);
		btnCancel = buttonPanel.addButton("btnCancel", false, false);
		btnCancel.setText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.cancel.btnText"));
		btnCancel.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.cancel.tooltipText"));
		btnCancel.setVisible(true);
		parent.getRootPane().setDefaultButton(btnTransferCase);
		//--- Add document listeners for each panel ---
		//--- Misc initialisation ---
		caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		defendantDelegate = XhibitDelegateHelper.getDefendantDelegate();
		bwHistoryDelegate = XhibitDelegateHelper.getBwHistoryDelegate();
	}


	//*******************************************************************************
	//* private GridBagConstraints getDefaultGridBagConstraints()
	//*
	//* Purpose : Get default set of gridbag constraints
	//* To call : Nothing
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	private GridBagConstraints getDefaultGridBagConstraints() {
		GridBagConstraints gbc = null;
		gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0 ,0);
		return gbc;
	}


	//*******************************************************************************
	//* private JPanel getPanel() 
	//*
	//* Purpose : Get JPanel for case transfer
	//* To call : Nothing
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	private JPanel getPanel() {
		if (null == panel) {
			int f;	//Temporary storage for gbc.fill
			panel = new JPanel();
	        panel.setLayout(new GridBagLayout());
	        GridBagConstraints gbc = XHIBITConstant.getDefaultGridBagConstraints();
	        //--- Set initial positions ---
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.weightx = 0.1;
	        gbc.weighty = 0.2;
			panel.setBorder(new TitledBorder(null, ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.caseDetails"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
	        //--- Add labels to layout ---
			gbc.gridy++;
        	f = gbc.fill;
			gbc.fill = GridBagConstraints.HORIZONTAL;
	        panel.add(lblCaseNumber, gbc);
	        gbc.fill = f;
        	gbc.gridy++;
        	gbc.gridy++;
        	panel.add(lblTransferredTo, gbc);
        	gbc.gridy++;
        	gbc.gridy++;
        	panel.add(lblTransferDate, gbc);
        	gbc.gridx++;
        	gbc.gridy = 1;
        	gbc.weightx = 0.9;	//weightx for right-hand column, so it resizes more than left-hand-side
        	f = gbc.fill;
			gbc.fill = GridBagConstraints.HORIZONTAL;
        	panel.add(lblCaseTitle, gbc);
        	gbc.fill = f;
        	gbc.gridy++;
        	panel.add(lblICourtList, gbc);
        	gbc.gridy++;
    		cmbCourtList = new XComboBox(true);	// mandatory
    		cmbCourtList.setBounds(190, 216, 280, 20);
    		cmbCourtList.setEnabled(true);
    		cmbCourtList.setModel(new DefaultComboBoxModel(GeneralDropdownPopulation.getCourts(false)));
    		cmbCourtList.addItemListener(new CourtIdListener());
    		DropdownBoxCellRender renderTransferTo = new DropdownBoxCellRender();
    		renderTransferTo.setFormat(true);
    		cmbCourtList.setRenderer(renderTransferTo);	
        	panel.add(cmbCourtList, gbc);
        	gbc.gridy++;
        	panel.add(lblITransferDate, gbc);
        	gbc.gridy++;
        	dtTransferDate = new XDate(this, null, true, lblITransferDate, "before");
        	dtTransferDate.setGridBagLayout(true);
        	panel.add(dtTransferDate, gbc);
        	//---
		}
		return panel;
	}


	//*******************************************************************************
	//* private void moveModelToScreen() 
	//*
	//* Purpose : Update components from model
	//* To call : Nothing
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	private void moveModelToScreen() {
		caseId = transferCaseModel.getCaseId();
	}


	//*******************************************************************************
	//* private void moveScreenToModel() 
	//*
	//* Purpose : Update model from screen components
	//* To call : Nothing
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	private void moveScreenToModel() {
		transferCaseModel.setCaseId(caseId);
	}


	//*******************************************************************************
	//* private void stepInitialise() 
	//*
	//* Purpose : Initialisation
	//* To call : Nothing
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	@Override
	public void stepInitialise() throws CSRecoverableException {
		caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		lblCaseNumber = new JLabel(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.caseNumber"));
		lblCaseTitle = new JLabel(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.caseTitle"));
		lblTransferredTo = new JLabel(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.transferredTo"));
		lblTransferDate = new JLabel(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.transferDate"));
		defaultError = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "warningDialog.defaultError");
		moveModelToScreen();
	}
	

	//*******************************************************************************
	//* private void stepActivate() 
	//*
	//* Purpose : 
	//* To call : Nothing
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	@Override
	public void stepActivate() throws CSRecoverableException {
		//log.debug("transferCase::stepActivate");
		try {
			CaseBasicValue caseDetails = caseDelegate.getCase(caseId);
			lblCaseNumber.setText(caseDetails.getCaseType() + caseDetails.getCaseNumber().toString());
			if ((null == caseDetails.getCaseTitle()) || (caseDetails.getCaseTitle().isEmpty())) {
				lblCaseTitle.setText(" ");
			} else {
				lblCaseTitle.setText(caseDetails.getCaseTitle());
			}
			if (null != caseDetails.getCccTransToRefCourtId()) {
				//--- Case has already been transferred out ---
				cmbCourtList.setSelectedItemByCode(caseDetails.getCccTransToRefCourtId());
				cmbCourtList.setEnabled(false);
				dtTransferDate.setDate(caseDetails.getDateTransTo());
				dtTransferDate.setEnabled(false);
				btnRevokeTransfer.setEnabled(true);
				btnTransferCase.setEnabled(false);
			} else {
				//--- Case has not already been transferred out ---
				cmbCourtList.setSelectedIndex(0);
				cmbCourtList.setEnabled(true);
				dtTransferDate.setDate(Calendar.getInstance().getTime());
				dtTransferDate.setEnabled(true);
				btnRevokeTransfer.setEnabled(false);
				btnTransferCase.setEnabled(false);		//Enabled when court selected
			}
			//--- Set font bold ---
		    Font font = lblCaseNumber.getFont();
		    lblCaseNumber.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
		    lblCaseTitle.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
		} catch (CaseControllerException e) {
			e.printStackTrace();
		}
	}


	//*******************************************************************************
	//* private void stepUpdateViewState() 
	//*
	//* Purpose : 
	//* To call : Nothing
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		//log.debug("transferCase::stepUpdateViewState");
	}


	//*******************************************************************************
	//* private void stepValidate() 
	//*
	//* Purpose : 
	//* To call : Nothing
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		//log.debug("transferCase::stepUpdateViewState");
	}


	//*******************************************************************************
	//* private void stepDeactivate() 
	//*
	//* Purpose : 
	//* To call : Nothing
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	@Override
	public void stepDeactivate() throws CSRecoverableException {
		//log.debug("transferCase::stepUpdateViewState");
	}


	//*******************************************************************************
	//* private void stepDeinitialise() 
	//*
	//* Purpose : 
	//* To call : Nothing
	//* Returns : Nothing
	//* Notes :
	//*******************************************************************************
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		//log.debug("transferCase::stepDeinitialise");
		//=== Transfer Case Button ===
		if (btnTransferCase.equals(getDeinitialiseSource())) {
			try {
				if (OutstandingBenchWarrantCheck()) {
					JOptionPane.showMessageDialog((Component) null, ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.benchWarrantError"), 
							"Error", JOptionPane.ERROR_MESSAGE);
					parent.dispose();
					return;
				}
				CaseBasicValue caseToUpdate = caseDelegate.getCase(caseId);
				caseToUpdate.setCccTransToRefCourtId(((RefCourtBasicValue)cmbCourtList.getSelectedItem()).getId());
				caseToUpdate.setDateTransTo(dtTransferDate.getTimestamp());
				Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
				caseToUpdate.setDateTransRecordedTo(timestamp);
				ArrayList<ChargesLogBasicValue>chargesLogList = new ArrayList<ChargesLogBasicValue>();
				caseDelegate.amendCase(caseToUpdate, chargesLogList,
						XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME), null);
				//--- Display pop-up & print ---
				Object[] message = {ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.printDlg.text1"),
									"",
									ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.printDlg.text2")};
				Object[] options = {ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.printDlg.printText"),
									ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.printDlg.closeText")};
				int o = JOptionPane.showOptionDialog(null, message, ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.printDlg.title"),
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (JOptionPane.YES_OPTION == o) {
					try {
						new DisplayNTRSFReportAction(parent.getParentFrame(),caseId);
					} catch (Exception e) {
						JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				parent.dispose();
			} catch (BwHistoryControllerException e1) {	// catches are necessary as can't change actionListener to throws
				JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
			} catch (DefendantControllerException e1) {
				JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
			} catch (FinderException e1) {
				JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
			} catch (CaseControllerException e1) {
				JOptionPane.showMessageDialog((Component) null, defaultError, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		//=== Revoke Transfer Button === 
		if (btnRevokeTransfer.equals(getDeinitialiseSource())) {
			CaseBasicValue caseToUpdate = caseDelegate.getCase(caseId);
			caseToUpdate.setCccTransToRefCourtId(null);
			caseToUpdate.setDateTransTo(null);
			caseToUpdate.setDateTransRecordedTo(null);

			ArrayList<ChargesLogBasicValue>chargesLogList = new ArrayList<ChargesLogBasicValue>();
			caseDelegate.amendCase(caseToUpdate, chargesLogList,
					XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME), null);	
			JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.revokedTransfer.text"),
					ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.revokedTransfer.title"), JOptionPane.INFORMATION_MESSAGE);
			stepActivate();
			//parent.dispose();
		}
		//=== Cancel button ===
		if (btnCancel.equals(getDeinitialiseSource())) {
			int n = JOptionPane.showConfirmDialog(null, ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.cancelDlg.text"),
						ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "transferCase.cancelDlg.title"), JOptionPane.YES_NO_OPTION);
			if (JOptionPane.YES_OPTION == n) {
				parent.dispose();
			}
		}
	}


	//*******************************************************************************
	//* private boolean OutstandingBenchWarrantCheck()
	//*
	//* Purpose : Validate that the case does not have an outstanding bench warrant
	//* To call : Nothing
	//* Returns : Nothing
	//*   Notes : CTX-211
	//*******************************************************************************
	private boolean OutstandingBenchWarrantCheck() throws BwHistoryControllerException, FinderException, DefendantControllerException {
		defendantDelegate = XhibitDelegateHelper.getDefendantDelegate();
		bwHistoryDelegate = XhibitDelegateHelper.getBwHistoryDelegate();
		
		// Search XHB_DEFENDANT_ON_CASE and return where case_id = this.caseId
		ArrayList<DefendantOnCaseBasicValue> defendantsOnCase = (ArrayList<DefendantOnCaseBasicValue>)defendantDelegate.findByCaseId(caseId);
		for (DefendantOnCaseBasicValue defendantOnCase : defendantsOnCase) {
			Integer defOnCaseId = ((DefendantOnCaseBasicValue)defendantOnCase).getDefendantOnCaseId();
			// Return any entries in XHB_BW_HISTORY where an outstanding bench warrant exists for the given defendant on case
			ArrayList<BwHistoryValue>bwHistoryColl = (ArrayList<BwHistoryValue>)bwHistoryDelegate.findOutstandingBenchWarrantsForDefOnCaseId(defOnCaseId);
			if(bwHistoryColl != null && !bwHistoryColl.isEmpty()) {	// if any entries found then show pop-up
				return true;
			}
		}
		return false;	// no defendants attached to case have outstanding bench warrants
	}
	
	
	//*******************************************************************************
	//* private class CourtIdLstener()
	//*
	//* Purpose : ItemListener to enable transfer button when court selected
	//* To call : Nothing
	//* Returns : Nothing
	//*   Notes :
	//*******************************************************************************
	private class CourtIdListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			//--- Only enable button if date is ok ---
			if (null != ((RefCourtBasicValue)cmbCourtList.getSelectedItem()).getId()) {
				btnTransferCase.setEnabled(lblITransferDate.getText().equals(" "));
			} else {
				btnTransferCase.setEnabled(false);
			}
		}
	}


	//*******************************************************************************
	//* private class XDate()
	//*
	//* Purpose : ItemListener to enable transfer button when court selected
	//* To call : Nothing
	//* Returns : Nothing
	//*   Notes :
	//*******************************************************************************
	private class XDate extends XDatePanelWithEvent {
		public XDate(JPanel containingPanel, Calendar defaultDate, boolean required, JLabel WarningLabel, String beforeOrAfter) {
			super(containingPanel, defaultDate, required, WarningLabel,	beforeOrAfter);
		}
		@Override
		protected void fireEvent() {
			log.debug("Event handled here");
			validateDate();
			if (null != ((RefCourtBasicValue)cmbCourtList.getSelectedItem()).getId()) {
				btnTransferCase.setEnabled(lblITransferDate.getText().equals(" "));
			} else {
				btnTransferCase.setEnabled(false);
			}
		}
	}

}
