package uk.gov.courtservice.xhibit.client.casemanagement.caselinking;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseLinkingValue;

public class LinkedCaseDetailsPanel extends XPanel {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private LinkedCaseDetailsModel linkedCaseDetailsModel;
	private XDialog parent;
	
	private JPanel mainPanel;
	private JTable tableResults;
	private JScrollPane scrollPane;
	
	private JButton btnClose;
	
	private CaseControllerBeanBusinessDelegate caseDelegate = null;
	private BisRefControllerBeanBusinessDelegate bisRefDelegate = null;

	public LinkedCaseDetailsPanel(XDialog parent, LinkedCaseDetailsModel linkedCaseDetailsModel) throws CSRecoverableException {
		this.parent = parent;
		this.linkedCaseDetailsModel = linkedCaseDetailsModel;
		stepInitialise();
		jbInit();
		stepUpdateViewState();
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {		
		CustomButtonPanel buttonPanel = (CustomButtonPanel)parent.getButtonPanel();
		btnClose = buttonPanel.addButton("btnClose", false, false);
		btnClose.setText("Close");
	}


	@Override
	public void stepActivate() throws CSRecoverableException {
	}


	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		bisRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		
		CaseBasicValue cbv = caseDelegate.getCase(linkedCaseDetailsModel.getCaseId());
		
		parent.setTitle("Linked Case Details, Group Number: " + cbv.getCaseGroupNumber().toString());
		
		ArrayList<CaseLinkingValue> clvColl = (ArrayList<CaseLinkingValue>) 
					caseDelegate.findActiveCasesWithGroupNumber(XhibitSingleton.getInstance().getCourtId(), 
													cbv.getCaseGroupNumber());
		
		// populate table
		DefaultTableModel model = (DefaultTableModel) tableResults.getModel();
		SimpleDateFormat simpleDateFo = new SimpleDateFormat("dd-MMMM-yyyy");
		
		for (CaseLinkingValue clv : clvColl) {
			if (!clv.getCaseId().equals(cbv.getCaseId())) {	
				String caseNumber = clv.getCaseType() + clv.getCaseNumber().toString();
				Date sentCommittalDate = null;
				RefCourtBasicValue rcBV = new RefCourtBasicValue();
				if (clv.getRefCourtId() != null) {
					rcBV = bisRefDelegate.findCourtByRefId(clv.getRefCourtId());
				} else {
					rcBV.setCourtFullName(" ");
				}
	
				if (clv.getSentForTrialDate() != null) {
					sentCommittalDate = clv.getSentForTrialDate();
				} else if (clv.getCommittalDate() != null) {
					sentCommittalDate = clv.getCommittalDate();
				}
				
				String date = "";
				if (sentCommittalDate != null) {
					date = simpleDateFo.format(sentCommittalDate);
				}
				
				model.addRow(new Object[] { caseNumber, clv.getCaseTitle(), 
											date, rcBV.getCourtFullName() });
			}
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
		parent.dispose();
	}

	
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(800, 300));
		
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		this.add(getMainPanel(), gbc);
	}
	
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.setBorder(BorderFactory.createTitledBorder("Cases linked to the searched case"));
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			
			tableResults = new JTable();
			tableResults.setModel(new DefaultTableModel(new String[] {
				"Case Number", "Case Title", "Sent/Committal Date", "Magistrate's Court"	
			}, 0));
			
			// Set widths of table to properly pre-size it
			tableResults.getColumnModel().getColumn(0).setPreferredWidth(20);
			tableResults.getColumnModel().getColumn(1).setPreferredWidth(70);
			tableResults.getColumnModel().getColumn(2).setPreferredWidth(20);
			tableResults.getColumnModel().getColumn(3).setPreferredWidth(100);
			
			tableResults.setDefaultEditor(Object.class, null);
			tableResults.getTableHeader().setReorderingAllowed(false);
			
			tableResults.setPreferredScrollableViewportSize(tableResults.getPreferredSize());
			
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(tableResults);
			mainPanel.add(scrollPane, gbc);
		}
		
		return mainPanel;
	}
	
	// Standard grid bag constraints
	private GridBagConstraints getDefaultGridBagConstraints() {
		GridBagConstraints gbc = null;
		gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0 ,0);
		return gbc;
	}	
}
