package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.TextRegexValidator;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class DeleteCasePanel extends XPanel implements ValidationListener {
	private static final long serialVersionUID = 1L;
	private DeleteCaseModel deleteCaseModel = null;
	private DeleteCaseDialog parentDialog = null;

	private XhibitApplicationController xac;
	private CaseBasicValue caseBV = null;

	private JPanel caseDetailsPanel = null;
	private JPanel mainPanel = null;

	private JLabel lblDateReceived = null;
	private JLabel lblReceivedFrom = null;
	private JLabel lblTransferredFrom = null;
	private JLabel lblReasonForDeletion = null;

	private XTextField txtCaseNumber = null;
	private XTextField txtCaseTitle = null;
	private XTextField txtDateReceived = null;
	private XTextField txtReceivedFrom = null;
	private XTextField txtTransferredFrom = null;
	private XTextField txtReasonForDeletion;

	private JLabel lblIReasonForDeletion = null;

	private JButton btnDelete = null;
	private JButton btnCancel = null;
	private CustomButtonPanel buttonPanel;

	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();

	public DeleteCasePanel(DeleteCaseDialog parentDialog, DeleteCaseModel deleteCaseModel)
			throws CSRecoverableException {
		this.parentDialog = parentDialog;
		this.deleteCaseModel = deleteCaseModel;
		stepInitialise();
		jbInit();
	}

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(500, 300));

		GridBagConstraints gbc = getGridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 2;
		this.add(getCaseDetailsPanel(), gbc);
		gbc.gridy++;

		this.add(getMainPanel(), gbc);

		buttonPanel = (CustomButtonPanel) parentDialog.getButtonPanel();
		btnDelete = buttonPanel.addButton("DeleteCase", false, false);
		btnDelete.setEnabled(false);
		btnCancel = buttonPanel.addButton("btnCancel", false, false);

	}

	private GridBagConstraints getGridBagConstraints() {
		GridBagConstraints gbc = null;
		gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				XHIBITConstant.nonContainerInsets, 0, 0);
		return gbc;
	}

	private JPanel getCaseDetailsPanel() {
		if (caseDetailsPanel == null) {
			caseDetailsPanel = new JPanel();
			GridBagConstraints gbc = getGridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			caseDetailsPanel.setLayout(new GridBagLayout());

			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			gbc.insets = XHIBITConstant.nonContainerInsets;

			caseDetailsPanel.setBorder(new TitledBorder(null,
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "caseDelete.CaseDetails"),
					TitledBorder.LEADING, TitledBorder.TOP, null, null));

			txtCaseNumber = new XTextField();
			txtCaseNumber.setEnabled(false);
			txtCaseNumber.setDisabledTextColor(Color.BLACK);
			caseDetailsPanel.add(txtCaseNumber, gbc);
			gbc.gridy++;

			lblDateReceived = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "caseDelete.DateReceived"));
			caseDetailsPanel.add(lblDateReceived, gbc);
			gbc.gridy++;

			lblReceivedFrom = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "caseDelete.ReceivedFrom"));
			caseDetailsPanel.add(lblReceivedFrom, gbc);
			gbc.gridy++;

			lblTransferredFrom = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "caseDelete.TransferredFrom"));
			caseDetailsPanel.add(lblTransferredFrom, gbc);

			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 0;

			txtCaseTitle = new XTextField();
			txtCaseTitle.setEnabled(false);
			txtCaseTitle.setDisabledTextColor(Color.BLACK);
			caseDetailsPanel.add(txtCaseTitle, gbc);
			gbc.gridy++;

			txtDateReceived = new XTextField();
			txtDateReceived.setEnabled(false);
			txtDateReceived.setDisabledTextColor(Color.BLACK);
			caseDetailsPanel.add(txtDateReceived, gbc);
			gbc.gridy++;

			txtReceivedFrom = new XTextField();
			txtReceivedFrom.setEnabled(false);
			txtReceivedFrom.setDisabledTextColor(Color.BLACK);
			caseDetailsPanel.add(txtReceivedFrom, gbc);
			gbc.gridy++;

			txtTransferredFrom = new XTextField();
			txtTransferredFrom.setEnabled(false);
			txtTransferredFrom.setDisabledTextColor(Color.BLACK);
			caseDetailsPanel.add(txtTransferredFrom, gbc);

		}
		return caseDetailsPanel;
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			GridBagConstraints gbc = getGridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			mainPanel.setLayout(new GridBagLayout());

			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			gbc.insets = XHIBITConstant.nonContainerInsets;

			lblReasonForDeletion = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "caseDelete.ReasonForDeletion"));
			mainPanel.add(lblReasonForDeletion, gbc);
			gbc.gridy++;

			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			lblIReasonForDeletion = new JLabel(" ");
			mainPanel.add(lblIReasonForDeletion, gbc);
			gbc.gridy++;

			gbc.weighty = 0.2;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			mainPanel.add(getReasonForDeletion(), gbc);

		}
		return mainPanel;
	}

	private XTextField getReasonForDeletion() {

		if (txtReasonForDeletion == null) {
			txtReasonForDeletion = new XTextField(35, "^.{1,35}$", lblIReasonForDeletion, true);
			txtReasonForDeletion.setMaxLength(255);
			txtReasonForDeletion.setColumns(10);
			txtReasonForDeletion.setPreferredSize(txtReasonForDeletion.getMinimumSize());
			txtReasonForDeletion.setEnabled(true);
			txtReasonForDeletion.setGridBagLayout(true);
			txtReasonForDeletion.setUpperCase(true);

			final TextValidationController txtRFDValidation = ValidationControllerFactory.createTextRequired(this,
					txtReasonForDeletion, lblIReasonForDeletion, new TextRegexValidator("^.{1,255}$"));
			validationControllers.add(txtRFDValidation);
			
			txtReasonForDeletion.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					validationUpdatedView(txtRFDValidation);
					
				}
			});
		}
		return txtReasonForDeletion;
	}

	private void moveModelToScreen() {
		if (deleteCaseModel != null) {
			if (deleteCaseModel.getCaseBasicValue() != null) {
				caseBV = deleteCaseModel.getCaseBasicValue();
				parentDialog.setTitle("Delete Case " + caseBV.getCaseType() + caseBV.getCaseNumber());

				if (caseBV.getCaseNumber() != null) {
					if (txtCaseNumber != null) {
						txtCaseNumber.setText(caseBV.getCaseType() + caseBV.getCaseNumber());
					}
				}

				if (caseBV.getCaseTitle() != null) {
					if (txtCaseTitle != null) {
						txtCaseTitle.setText(caseBV.getCaseTitle());
					}
				}
				if (caseBV.getReceivedDate() != null) {
					if (txtDateReceived != null) {
						Date receivedDate = new Date();
						receivedDate.setTime(caseBV.getReceivedDate().getTime());
						txtDateReceived.setText(new SimpleDateFormat("dd-MMM-yyyy").format(receivedDate));
					}
				}
				if (caseBV.getRefCourtID() != null) {
					try {
						BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
						RefCourtBasicValue refCBV = bizRefDelegate.findCourtByRefId(caseBV.getRefCourtID());
						if (refCBV != null) {
							if (txtReceivedFrom != null) {
								txtReceivedFrom.setText(refCBV.getCourtFullName());
							}
						}
					} catch (SysRefControllerException e) {
						e.printStackTrace();
					}
				}

				if (caseBV.getCccTransFromRefCourtId() != null) {
					try {
						BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
						RefCourtBasicValue refCBV = bizRefDelegate.findCourtByRefId(caseBV.getCccTransFromRefCourtId());
						if (refCBV != null) {
							if (txtTransferredFrom != null) {
								txtTransferredFrom.setText(refCBV.getCourtFullName());
							}
						}
					} catch (SysRefControllerException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void moveScreenToModel() {
		deleteCaseModel.setCaseBasicValue(caseBV);
		deleteCaseModel.setReasonForDeletion(txtReasonForDeletion.getText());
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
		moveModelToScreen();
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		if (!ValidationControllerFactory.validateComponents(validationControllers)) {
			throw new CSValidationException("validation.general", "Field validation Failed");
		}
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (btnDelete.equals(getDeinitialiseSource())) {
			if (!ValidationControllerFactory.validateComponents(validationControllers)) {
				throw new CSValidationException("validation.general", "Field validation failed");
			}

			int dialogButton = JOptionPane.YES_NO_OPTION;
			int result = JOptionPane.showConfirmDialog((Component) null, "Are you sure?", "Confirm", dialogButton);
			if (result == JOptionPane.YES_OPTION) {
				moveScreenToModel();
				try {
					XhibitDelegateHelper.getCaseHistoryDelegate().deleteCase(
						deleteCaseModel.getCaseBasicValue().getCaseId(), deleteCaseModel.getReasonForDeletion());
					JOptionPane.showOptionDialog((Component) null, "The case has been scheduled for deletion", "Success",
						JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, null, null);
					parentDialog.dispose();
				} catch(Exception e) {
					JOptionPane.showOptionDialog(parentDialog, "Non fatal error, please contact Xhibit "
							+ "support on: 0300 303 0688", "Non fatal error",
							JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, null, null);
				}
			}
		}

		if (btnCancel.equals(getDeinitialiseSource())) {
			if (!(txtReasonForDeletion.getText().equals(""))) {

				int dialogButton = JOptionPane.YES_NO_OPTION;
				int result = JOptionPane.showConfirmDialog((Component) null,
						"All the changes will be lost, are you sure?", "Confirm", dialogButton);
				if (result == JOptionPane.YES_OPTION) {
					parentDialog.clearStatusBarScreenCode();
					parentDialog.dispose();
				}
			} else {
				parentDialog.clearStatusBarScreenCode();
				parentDialog.dispose();
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.gov.courtservice.xhibit.client.util.validation.ValidationListener#
	 * validationUpdatedView(uk.gov.courtservice.xhibit.client.util.validation.
	 * ValidationController)
	 */
	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		if (!ValidationControllerFactory.validateComponents(validationControllers)) {
			btnDelete.setEnabled(false);
		} else {
			btnDelete.setEnabled(true);
		}

	}

}
