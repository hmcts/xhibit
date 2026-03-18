package uk.gov.courtservice.xhibit.client.admin.courtofappeal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.PageController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XTextArea;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CourtOfAppealPanel extends XPanel implements TableModelListener, ValidationListener {

	private static final long serialVersionUID = 1L;

	private static final Integer SAVE = Integer.valueOf(0);
	private static final Integer SEND = Integer.valueOf(1);

	@SuppressWarnings("unused")
	private XhibitApplicationController xac;

	private CaseBasicValue caseBV;

	private int selectedRow;

	private Collection allDefendants;

	private JScrollPane defendantsScrollPane;

	private Vector<CourtOfAppealTableRowModel> defendantDetails = new Vector<CourtOfAppealTableRowModel>();

	private JTable defendantsTable;

	private JLabel lblReceiptDate = null;

	private JLabel lblPapersSentDate = null;

	private JLabel lblResultDate = null;

	private XTextField txtDefendantName = null;

	private JScrollPane appealScrollPane = null;

	private XTextArea appealText = null;

	private XDatePanelWithEvent resultDatePanel = null;

	private XDatePanelWithEvent datePapersSent = null;

	private XDatePanelWithEvent receiptDatePanel = null;

	private XDialog parentDialog = null;

	private boolean error = false;

	private Calendar receivedDate = null;

	private Calendar todaysDate = null;

	private JPanel datePanel = null;

	private JPanel appealResultPanel = null;

	private JLabel lblReceiptDateInvalidEntry = null;

	private JLabel lblDatePapersSentInvalidEntry = null;

	private JLabel lblResultDateInvalidEntry = null;

	private JLabel lblAppealTextInvalidEntry = null;

	private DefendantControllerBeanBusinessDelegate defCtrlBeanDelegate = null;
	
	private LocalPageController pageController = new LocalPageController();
	
	private boolean invalidEntry = true;
	
	/**
	 * Validators.
	 */
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();

	/**
	 * Fields on Button panel.
	 */
	private JButton btnSend = null;
	private JButton btnSave = null;
	private JButton btnClose = null;

	public CourtOfAppealPanel(XDialog parent, XhibitApplicationController xac, CaseBasicValue caseBasicVal)
			throws CSRecoverableException {
		this.xac = xac;
		this.caseBV = caseBasicVal;
		this.parentDialog = parent;
		stepInitialise();
		jbInit();
	}

	/**
	 * Initialise GUI components
	 */
	private void jbInit() {
		GridBagConstraints gbc = getGridBagLayout();

		this.setLayout(new GridBagLayout());
		this.parentDialog.setMinimumSize(new Dimension(640, 480));

		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weighty = 0.05;
		gbc.weightx = 0.95;
		gbc.fill = GridBagConstraints.BOTH;

		this.add(getDefendantsScrollPane(), gbc);
		gbc.gridy++;

		this.add(getDatePanel(), gbc);
		gbc.gridy++;

		this.add(getAppealResultPanel(), gbc);

		CustomButtonPanel buttonPanel = (CustomButtonPanel) this.parentDialog.getButtonPanel();
		btnSend = buttonPanel.addButton("CourtOfAppealSend", false, false);
		btnSave = buttonPanel.addButton("CourtOfAppealSave", false, true);
		btnClose = buttonPanel.addButton("CourtOfAppealClose", true, false);

		btnSend.setEnabled(false);
		btnSave.setEnabled(false);
		todaysDate = Calendar.getInstance();
		todaysDate.setTime(new Date());

	}

	private JScrollPane getDefendantsScrollPane() {
		if (defendantsScrollPane == null) {
			defendantsScrollPane = new JScrollPane();
			defendantsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			defendantsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			defendantsScrollPane.getViewport().add(getDefendantsTable(), null);
			defendantsScrollPane.setPreferredSize(new Dimension(1100, XHIBITConstant.getLineHeight() * 7));
			defendantsScrollPane.setMinimumSize(defendantsScrollPane.getPreferredSize());
		}

		return defendantsScrollPane;
	}

	private JPanel getDatePanel() {
		if (datePanel == null) {
			datePanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.NONE;
			datePanel.setLayout(new GridBagLayout());

			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			datePanel.add(getDefendantName(), gbc);
			gbc.gridy += 2;

			lblReceiptDate = new JLabel("Date of Receipt of Notice of Appeal");
			datePanel.add(lblReceiptDate, gbc);
			gbc.gridy += 2;

			lblPapersSentDate = new JLabel("Date Papers Sent to Court of Appeal");
			datePanel.add(lblPapersSentDate, gbc);

			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 2;
			gbc.fill = GridBagConstraints.NONE;

			datePanel.add(getReceiptDatePanel(), gbc);
			receiptDatePanel.setEnabled(false);
			gbc.gridy += 2;

			datePanel.add(getDatePapersSent(), gbc);
			datePapersSent.setEnabled(false);

			/* Top Of Column */
			gbc.gridx = 1;
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			datePanel.add(lblReceiptDateInvalidEntry, gbc);

			gbc.gridy += 3;
			datePanel.add(lblDatePapersSentInvalidEntry, gbc);
		}
		return datePanel;
	}

	public XTextField getDefendantName() {
		if (txtDefendantName == null) {
			txtDefendantName = new XTextField();
			txtDefendantName.setColumns(15);
			txtDefendantName.setUpperCase(true);
			txtDefendantName.setMinimumSize(txtDefendantName.getPreferredSize());
			txtDefendantName.setEnabled(false);
		}
		return txtDefendantName;
	}

	public XDatePanel getReceiptDatePanel() {
		if (lblReceiptDateInvalidEntry == null) {
			lblReceiptDateInvalidEntry = new JLabel(" ");
		}
		if (receiptDatePanel == null) {
			receiptDatePanel = new XDatePanelWithEvent(this, null, false) {
				private static final long serialVersionUID = 1L;
				@Override
				protected void fireEvent() {
					String receiptDate = "";
					if (receiptDatePanel.getText() != null) {
						receiptDate = receiptDatePanel.getText().toUpperCase();
					}
					if ((defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 1) != null)) {
						if (!(defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 1).equals(receiptDate))
								|| (defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 1) == null
										&& receiptDate != null)) {
							pageController.setPageChanged();
						}
					}
					
					if ("".equals(receiptDate)) {
						datePapersSent.setEnabled(false);
						resultDatePanel.setEnabled(false);
						appealText.setEnabled(false);
					}
				}
			};
			DateValidationController receiptDateValCon = ValidationControllerFactory.createDateValid(this,
					receiptDatePanel, lblReceiptDateInvalidEntry, new AbstractDateValidator() {
						@Override
						public void validate(XDatePanel target, List<String> errors) {
							if (getDate(target) != null) {
								if (hasDate(target) && hasDate(receiptDatePanel) && getDate(target).before(receivedDate)
										|| getDate(target).equals(receivedDate)) {
									errors.add(
											"Date of Receipt of Notice of Appeal must be after receipt date of case");
								}
								if (getDate(target).after(todaysDate)) {
									errors.add("Date of Receipt of Notice of Appeal must not be in future");
								}

							}
							else {
								try {
									if (datePapersSent.getDate() != null) {
										errors.add("Field is mandatory");
									}
								} catch (CSValidationException e) {
								}
							}

						}
					});
			validationControllers.add(receiptDateValCon);
		}
		return receiptDatePanel;
	}

	public XDatePanel getDatePapersSent() {
		if (lblDatePapersSentInvalidEntry == null) {
			lblDatePapersSentInvalidEntry = new JLabel(" ");
		}
		if (datePapersSent == null) {
			datePapersSent = new XDatePanelWithEvent(this, null, false) {
				private static final long serialVersionUID = 1L;
				@Override
				protected void fireEvent() {
					String datePapers = "";
					if (datePapersSent.getText() != null) {
						datePapers = datePapersSent.getText().toUpperCase();
					}
					if ((defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 2) != null
							&& !defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 2).equals(datePapers))
							|| (defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 2) == null
									&& datePapers != null)) {
						pageController.setPageChanged();
					}
					
					if ("".equals(datePapers)) {
						resultDatePanel.setEnabled(false);
						appealText.setEnabled(false);
					}
				}
			};
			DateValidationController datePapersSentValCon = ValidationControllerFactory.createDateValid(this,
					datePapersSent, lblDatePapersSentInvalidEntry, new AbstractDateValidator() {
						@Override
						public void validate(XDatePanel target, List<String> errors) {
							if (getDate(target) != null) {
								if (hasDate(target) && hasDate(datePapersSent)
										&& getDate(target).before(getDate(receiptDatePanel))) {
									errors.add("Date Papers Sent must be equal to or after the Date of Receipt");
								}
								if (getDate(target).after(todaysDate)) {
									errors.add("Date Papers Sent must not be in future");
								}

							}

						}
					});
			validationControllers.add(datePapersSentValCon);
		}
		return datePapersSent;
	}

	private void setTxtDefendantName(String defendantName) {
		txtDefendantName.setText(defendantName);
	}

	private JPanel getAppealResultPanel() {
		if (appealResultPanel == null) {
			appealResultPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			appealResultPanel.setLayout(new GridBagLayout());
			TitledBorder titleBorder = new TitledBorder("Appeal Result");
			appealResultPanel.setBorder(titleBorder);

			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblResultDate = new JLabel("Date of Result of Appeal Hearing");
			// Stops the date panels going out of alignment
			lblResultDate.setPreferredSize(lblPapersSentDate.getPreferredSize());
			appealResultPanel.add(lblResultDate, gbc);

			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.weightx = 0.52;
			gbc.fill = GridBagConstraints.NONE;

			appealResultPanel.add(getResultDatePanel(), gbc);
			resultDatePanel.setEnabled(false);

			/* Top Of Column */
			gbc.gridx = 1;
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;

			appealResultPanel.add(lblResultDateInvalidEntry, gbc);

			/* Next Column */
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.weighty = 0.8;

			appealResultPanel.add(getAppealText(), gbc);
			gbc.fill = GridBagConstraints.BOTH;
			// 20 = Vertical scroll bar as needed, 31 is horizontal never
			appealScrollPane = new JScrollPane(appealText, 20, 31);
			appealResultPanel.add(appealScrollPane, gbc);
			appealText.setEnabled(false);

			/* Next Column */
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			appealResultPanel.add(lblAppealTextInvalidEntry, gbc);
			pageController.addChangeListeners(appealResultPanel.getComponents());
		}
		return appealResultPanel;
	}

	private XTextArea getAppealText() {
		if (lblAppealTextInvalidEntry == null) {
			lblAppealTextInvalidEntry = new JLabel(" ");
			lblAppealTextInvalidEntry.setForeground(Color.RED);
		}
		if (appealText == null) {

			appealText = new XTextArea(" ", 3, 150, 240, lblAppealTextInvalidEntry);
			appealText.setRows(5);
			appealText.setColumns(20);
			appealText.setMaximumSize(appealText.getPreferredSize());
			appealText.setLineWrap(true);
			appealText.setGridBagLayout(true);
			appealText.setEnabled(false);
			appealText.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
			appealText.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
		}
		return appealText;
	}

	public XDatePanel getResultDatePanel() {
		if (lblResultDateInvalidEntry == null) {
			lblResultDateInvalidEntry = new JLabel(" ");
		}
		if (resultDatePanel == null) {
			resultDatePanel = new XDatePanelWithEvent(this, null, false) {
				private static final long serialVersionUID = 1L;
				@Override
				protected void fireEvent() {
					String resultDate = "";
					if (resultDatePanel.getText() != null) {
						resultDate = resultDatePanel.getText().toUpperCase();
					}
					if ((defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 3) != null
							&& !defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 3).equals(resultDate))
							|| (defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 3) == null
									&& resultDate != null)) {
						appealText.setEnabled(true);
						pageController.setPageChanged();
					}
					
					if ("".equals(resultDate)) {
						appealText.setEnabled(false);
					}
				}
			};
			DateValidationController resultDateValCon = ValidationControllerFactory.createDateValid(this,
					resultDatePanel, lblResultDateInvalidEntry, new AbstractDateValidator() {
						@Override
						public void validate(XDatePanel target, List<String> errors) {
							if (getDate(target) != null) {
								if (hasDate(target) && hasDate(resultDatePanel)
										&& getDate(target).before(getDate(datePapersSent))
										|| getDate(target).equals(getDate(datePapersSent))) {
									errors.add("Date of Result of Appeal must be later than Date Papers Sent");
								}
								if (getDate(target).after(todaysDate)) {
									errors.add("Date of Result of Appeal must not be in future");
								}

							}

						}
					});
			validationControllers.add(resultDateValCon);
		}
		return resultDatePanel;
	}

	private JTable getDefendantsTable() {
		if (defendantsTable == null) {

			CourtOfAppealTableModel artm = new CourtOfAppealTableModel();
			defendantsTable = XTableFactory.getInstance().createMultiLineTable(artm);

			defendantsTable.getTableHeader().setReorderingAllowed(false);
			defendantsTable.getTableHeader().setPreferredSize(new Dimension(100, 40));
			TableColumn column = null;
			column = defendantsTable.getColumnModel().getColumn(CourtOfAppealTableModel.DEFENDANT_NAME);
			column.setPreferredWidth(250);
			column = defendantsTable.getColumnModel().getColumn(CourtOfAppealTableModel.DATE_OF_RECEIPT);
			column.setPreferredWidth(150);
			column = defendantsTable.getColumnModel().getColumn(CourtOfAppealTableModel.DATE_PAPERS_SENT);
			column.setPreferredWidth(150);
			column = defendantsTable.getColumnModel().getColumn(CourtOfAppealTableModel.DATE_OF_RESULT);
			column.setPreferredWidth(150);
			column = defendantsTable.getColumnModel().getColumn(CourtOfAppealTableModel.APPEAL_RESULT);
			column.setPreferredWidth(150);
			column = defendantsTable.getColumnModel().getColumn(CourtOfAppealTableModel.STATUS);
			column.setPreferredWidth(150);

			defendantsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			defendantsTable.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
			defendantsTable.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
			defendantsTable.setPreferredScrollableViewportSize(defendantsTable.getPreferredSize());
			ListSelectionModel rowSM = defendantsTable.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting())
						return;
					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (!lsm.isSelectionEmpty()) {
						stepViewState();
					}
				}
			});
			artm.addTableModelListener(this);
		}

		return defendantsTable;
	}

	public void tableChanged(TableModelEvent tme) {
	}

	/**
	 * Life-cycle method to retrieve non-volatile data or data to be shown in
	 * its initial state. In this instance, invoke a call to get a list of
	 * defendants on the case.
	 * 
	 * @throws CSRecoverableException
	 */
	public void stepInitialise() throws CSRecoverableException {
		refreshAppealPanelData();
	}

	/**
	 * Retrieve the defendants on case and build a table model with the results.
	 * 
	 * @throws CSRecoverableException
	 */
	private void refreshAppealPanelData() throws CSRecoverableException {

		Integer caseId = caseBV.getCaseId();
		receivedDate = Calendar.getInstance();

		if (caseBV.getReceivedDate() != null) {
			receivedDate.setTime(caseBV.getReceivedDate());
		}

		allDefendants = XhibitDelegateHelper.getCaseDelegate().getDefendants(caseId);
		defendantDetails = new Vector<CourtOfAppealTableRowModel>();

		Iterator it = allDefendants.iterator();

		while (it.hasNext()) {

			CourtOfAppealTableRowModel item = new CourtOfAppealTableRowModel();
			DefendantValue val = (DefendantValue) it.next();

			if (DefendantValue.GENDER_COMPANY.equals(val.getGender())) {
				item.setDefendantName(val.getSurName());
			} else {
				item.setDefendantName(val.getFirstName() + " " + val.getMiddleName() + " " + val.getSurName());
			}
			item.setDateOfReceipt(val.getDefOnCaseBasicValue().getDateReceiptNoticeAppeal());
			item.setDatePapersSent(val.getDefOnCaseBasicValue().getFormNgSentDate());
			item.setDateOfResult(val.getDefOnCaseBasicValue().getCacdAppealResultDate());
			item.setAppealResult(val.getDefOnCaseBasicValue().getCacdAppealResult());
			item.setStatus(val.getDefOnCaseBasicValue().getCoaStatus());

			item.setDefOnCaseID(val.getDefOnCaseBasicValue().getDefendantOnCaseId());
			defendantDetails.add(item);
		}
	}

	/**
	 * Life-cycle method executed when the screen is made visible. In this
	 * instance, move details to the screen.
	 */
	public void stepActivate() {
		moveDetailsToScreen();
	}

	/**
	 * Pseudo life-cycle method to refresh the screen.
	 */
	private void moveDetailsToScreen() {
		XHIBITTableModelInterface model = (XHIBITTableModelInterface) getDefendantsTable().getModel();
		model.setData(defendantDetails);
		defendantsTable.tableChanged(new TableModelEvent(model));
	}

	/**
	 * Life-cycle method to manage the enabled state of screen widgets
	 */
	public void stepUpdateViewState() {

		if (receiptDatePanel.getText().length() > 0) {
			datePapersSent.setEnabled(true);
		}
		
		if (datePapersSent.getText().length() > 0) {
			resultDatePanel.setEnabled(true);
		}
		
		if (resultDatePanel.getText().length() > 0) {
			appealText.setEnabled(true);
		}
	}

	/**
	 * method to populate the fields with table values.
	 */
	private void stepViewState() {

		if (defendantsTable.getSelectedRow() != -1) {
			pageController.disableListeners();
			selectedRow = defendantsTable.getSelectedRow();

			String rowDefendantName = defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 0).toString();
			String rowReceiptDate = defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 1).toString();
			String rowPapersSentDate = defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 2).toString();
			String rowAppealResultDate = defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 3).toString();
			String rowAppealResult = defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 4).toString();

			Boolean hasReceiptDate = false;
			Boolean hasPapersSentDate = false;
			Boolean hasResultDate = false;

			if (!rowDefendantName.equals("")) {
				setTxtDefendantName(rowDefendantName);
				receiptDatePanel.setEnabled(true);
			} else {
				setTxtDefendantName("");
			}

			if (!rowReceiptDate.equals("")) {
				setDate(receiptDatePanel, rowReceiptDate);
				hasReceiptDate = true;
				hasPapersSentDate = true;
			} else {
				setDate(receiptDatePanel, null);
			}

			if (!rowPapersSentDate.equals("")) {
				setDate(datePapersSent, rowPapersSentDate);
				hasResultDate = true;

			} else {
				setDate(datePapersSent, null);
			}

			if (!rowAppealResultDate.equals("")) {
				setDate(resultDatePanel, rowAppealResultDate);
			} else {
				setDate(resultDatePanel, null);
			}

			if (!rowAppealResult.equals("")) {
				appealText.setText(rowAppealResult);
			} else {
				appealText.setText("");
			}

			try {
				datePapersSent.setEnabled(hasReceiptDate && receiptDatePanel.getDate() != null);
			} catch (CSValidationException e) {
				datePapersSent.setEnabled(false);
			}
			resultDatePanel.setEnabled(
					hasPapersSentDate && (!(rowAppealResultDate.equals("")) || (!(rowPapersSentDate.equals("")))));
			appealText.setEnabled(hasReceiptDate && hasPapersSentDate && hasResultDate);
			pageController.reset();
		}

	}

	private void setDate(XDatePanel datePanel, String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			if (dateStr != null) {
				datePanel.setDate(sdf.parse(dateStr));
			} else {
				datePanel.clear();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Life-cycle method to perform screen validation.
	 * 
	 * @throws CSValidationException
	 * @throws CSRecoverableException
	 */
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		// Throw exception if validation failures to prevent saving
		
		error = isValidationError();
	}

	private boolean isValidationError() throws CSValidationException {
		boolean error = false;
		Boolean hasDatePapersSent = false;
		Boolean hasResultDate = false;

		if (!defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 2).equals("")) {
			hasDatePapersSent = true;
		}

		if (!defendantsTable.getValueAt(defendantsTable.getSelectedRow(), 3).equals("")) {
			hasResultDate = true;
		}

		// Display warning message if the Date Papers Sent is changed from a date to a NULL value and any
		// of the Result fields are not blank
		if (datePapersSent.isEnabled() && hasDatePapersSent && datePapersSent.getDate() == null 
				&& (resultDatePanel.getDate() != null || 
					(appealText.getText() != null && !"".equals(appealText.getText())) ) ) {
			error = true;
			JOptionPane.showMessageDialog(new JFrame(), "Non null Date Papers Sent cannot be made null",
					"Invalid Date Papers Date", JOptionPane.ERROR_MESSAGE);
		}

		// Display warning message if the Result Date is changed from a date to a NULL value and the
		// Result is not blank
		if ( resultDatePanel.isEnabled() && hasResultDate && resultDatePanel.getDate() == null
				&& appealText.getText() != null && !"".equals(appealText.getText()) ) {
			error = true;
			JOptionPane.showMessageDialog(new JFrame(), "Non null result date cannot be made null",
					"Invalid Result Date", JOptionPane.ERROR_MESSAGE);
		}
		
		for(ValidationController<?> validationController :  validationControllers) {
			if (!validationController.validate()) {
				error = true;
			}
		}
		
		return error;
	}

	/**
	 * Life-cycle method executed when the screen is made invisible
	 * 
	 * @throws CSRecoverableException
	 */
	public void stepDeactivate() throws CSRecoverableException {
	}

	/**
	 * Life-cycle method executed when an either the OK or Cancel button is
	 * clicked. In this instance, if OK( Authorise ) is clicked, call authorise
	 * on the selected defendants. NOTE: Do not perform any GUI actions in this
	 * method
	 * 
	 * @param save
	 *            - true if the OK button was clicked
	 * @throws CSRecoverableException
	 */
	public void stepDeinitialise(boolean save) throws CSRecoverableException {

		if (save) {
			if (error) {
				throw new UserCancelException();
			} else {
				saveToDBUpdateModel(SAVE);
			}
		} else {
			closeWindow();
		}
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnSend.setEnabled(false);
				btnSave.setEnabled(false);
 				try {
				 	saveToDBUpdateModel(SEND);
				} catch (CSRecoverableException e1) {
					XHIBITConstant.handleError(e1);
				}
	 		}
		});

	}

	private void saveToDBUpdateModel(Integer statusChange) throws CSRecoverableException {

		int indexValue = 0;
		CourtOfAppealTableRowModel courtTableRow = defendantDetails.get(defendantsTable.getSelectedRow());
		defCtrlBeanDelegate = XhibitDelegateHelper.getDefendantDelegate();

		DefendantOnCaseBasicValue defBV = defCtrlBeanDelegate.getDefendantOnCaseDetails(courtTableRow.getDefOnCaseID());

		for (int i = 0; i < defendantDetails.size(); i++) {
			if (defendantDetails.get(i).getDefOnCaseID() == courtTableRow.getDefOnCaseID()) {
				indexValue = i;
				break;
			}
		}
 
		if (SEND.equals(statusChange)) {
			
 			defBV.setCoaStatus("E");
			defendantDetails.get(indexValue).setStatus("E");
			defCtrlBeanDelegate.updateDefOnCaseCOAStatus(defBV, 
					XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
	 	}

		else {

			defBV.setDateReceiptNoticeAppeal(receiptDatePanel.getTimestamp());
			defendantDetails.get(indexValue).setDateOfReceipt(receiptDatePanel.getTimestamp());
	
			defBV.setFormNgSentDate(datePapersSent.getTimestamp());
			defendantDetails.get(indexValue).setDatePapersSent(datePapersSent.getTimestamp());
	
			defBV.setCacdAppealResultDate(resultDatePanel.getTimestamp());
			defendantDetails.get(indexValue).setDateOfResult(resultDatePanel.getTimestamp());
	
			defBV.setCacdAppealResult(appealText.getText());
			defendantDetails.get(indexValue).setAppealResult(appealText.getText());
			
			defCtrlBeanDelegate.updateDefendantOnCaseBasicValue(defBV, 
					XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
 		}
		defendantsTable.clearSelection();
		moveDetailsToScreen();
		defendantsTable.setRowSelectionInterval(selectedRow, selectedRow);
		pageController.reset();

	}

	private boolean isSendActionValid() {
		try {
			return receiptDatePanel.getTimestamp() != null &&
					datePapersSent.getTimestamp() != null &&
					resultDatePanel.getTimestamp() != null &&
					appealText.getText() != null && !"".equals(appealText.getText());
		} catch (CSRecoverableException e) {
			return false;
		}
	}

	private void closeWindow() throws UserCancelException {
		if (pageController.isPageChanged()) {
			int dialogResult = JOptionPane.showConfirmDialog(this,
					"Warning – any unsaved data will be lost. Do you wish to continue?.", "Warning",
					JOptionPane.YES_NO_OPTION);
			if (dialogResult == JOptionPane.NO_OPTION) {
				throw new UserCancelException();
			}
			if (dialogResult == JOptionPane.YES_OPTION) {
				return;
			}
		}
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		if (!validationController.hasErrors()
				&& ValidationControllerFactory.validateComponents(validationControllers)) {
			btnSave.setEnabled(true);
			invalidEntry = false;
		} else {
			btnSave.setEnabled(false);
			invalidEntry = true;
		}
	}

	/**
	 * Default gridbag that's used throughout the panels.
	 * 
	 * @return gridbagconstraints
	 */
	private GridBagConstraints getGridBagLayout() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				XHIBITConstant.nonContainerInsets, 0, 0);
	}

	private class LocalPageController extends PageController {

		public void disableListeners() {
			this.enabled = false;
		}
		
		@Override
		public void setPageChanged() {
			super.setPageChanged();
			btnSave.setEnabled(!invalidEntry);
			btnSend.setEnabled(false);
		}
		
		@Override
		public void reset() {
			super.reset();
			btnSave.setEnabled(false);
			btnSend.setEnabled(isSendActionValid());
		}
	}
}