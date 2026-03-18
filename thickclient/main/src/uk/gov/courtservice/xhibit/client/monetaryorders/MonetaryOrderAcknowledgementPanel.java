package uk.gov.courtservice.xhibit.client.monetaryorders;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.ejb.FinderException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.monetaryordertracking.MonetaryOrderTrackingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.monetaryordertracking.MonetaryOrderTrackingControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.MonetaryOrderTrackingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class MonetaryOrderAcknowledgementPanel extends XPanel implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private MonetaryOrderTrackingControllerBeanBusinessDelegate monetaryOrderTrackingDelegate = null;
	private MonetaryOrderAcknowledgementModel monetaryOrderAcknowledgementModel;
	private XDialog parent;
	private Integer courtId;
	private DefaultTableModel monetaryOrderAcknowledgementTableModel;
	private JTable monetaryOrderAcknowledgementTable;
	private JScrollPane scrollPane;
	private JPanel headerPanel = null;
	private JTextField txtCaseNumber;
	private JPanel tablePanel = null;
	private JTextField txtDefendant;
	private JTextField txtCollectingCourt;
	private JTextField txtFined;
	private JTextField txtCompensation;
	private JTextField txtCosts;
	private JTextField txtOrderDate;
	private XDatePanel dateAcknowledgementReceived;
	private JButton saveButton;
	private JButton deleteButton;
	private JButton closeButton;

	public JTextField getTxtDefendant() { return txtDefendant; };
	public JTextField getTxtCollectingCourt() { return txtCollectingCourt; };
	public JTextField getTxtFined() { return txtFined; };
	public JTextField getTxtCompensation() { return txtCompensation; };
	public JTextField getTxtCosts() { return txtCosts; };
	public JTextField getTxtOrderDate() { return txtOrderDate; };
	public XDatePanel getDateAcknowledgementReceived() { return dateAcknowledgementReceived; };

  private String userDisplayName = XhibitSingleton.getInstance().getUserSession()
			.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
			
	protected class Columns {
		public static final int DEFENDANT = 0;
		public static final int COLLECTING_COURT = 1;
		public static final int FINED = 2;
		public static final int COMPENSATION = 3;
		public static final int COSTS = 4;
		public static final int ORDER_DATE = 5;
		public static final int DATE_ACKNOWLEDGEMENT_RECEIVED = 6;
		public static final int MONETARY_ORDER = 7;
	}


	// *******************************************************************************
	// * public MonetaryOrderAcknowledgementPanel(XDialog parent,
	// *     MonetaryOrderAcknowledgementModel monetaryOrderAcknowledgementModel)
	// *
	// * Purpose : Constructor
	// * To call : parent - dialog parent class
	// *           monetaryOrderAcknowledgementModel - class model data
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	public MonetaryOrderAcknowledgementPanel(XDialog parent, MonetaryOrderAcknowledgementModel monetaryOrderAcknowledgementModel) throws CSRecoverableException {
		this.parent = parent;
		this.monetaryOrderAcknowledgementModel = monetaryOrderAcknowledgementModel;
		monetaryOrderTrackingDelegate = monetaryOrderAcknowledgementModel.getMonetaryOrderTrackingControllerBeanBusinessDelegate();
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
		parent.setPreferredSize(new Dimension(900, 400));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		//--- Add header panel ---
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		headerPanel = getHeaderPanel();
		add(headerPanel, gbc);
		//--- Add table panel ---
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		tablePanel = getTablePanel();
		add(tablePanel, gbc);
		add(Box.createRigidArea(tablePanel.getMinimumSize()), gbc);
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
	// * private JPanel getHeaderPanel()
	// *
	// * Purpose : Get header panel
	// * To call : Nothing
	// * Returns : Header panel for case number
	// * Notes :
	// *******************************************************************************
	private JPanel getHeaderPanel() {
		if (null == headerPanel) {
			headerPanel = new JPanel();
			headerPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.NONE;
			headerPanel.add(new JLabel("Case:"), gbc);
			gbc.gridx = 1;
			txtCaseNumber = new JTextField();
			txtCaseNumber.setEnabled(false);;
			txtCaseNumber.setColumns(10);
			txtCaseNumber.setMinimumSize(txtCaseNumber.getPreferredSize());
			headerPanel.add(txtCaseNumber, gbc);
		}
		return headerPanel;
	}
	

	// *******************************************************************************
	// * private JPanel getTablePanel()
	// *
	// * Purpose : Get table panel
	// * To call : Nothing
	// * Returns : Panel with table and associated text fields
	// * Notes : Dates are stored as Date types & it is the job of the cell renderer
	// *         to correctly format & display. null Date types are stored as "" 
	// *******************************************************************************
	private JPanel getTablePanel() {
		if (null == tablePanel) {
			tablePanel = new JPanel();
			tablePanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			//--- Table ---
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 7;
			gbc.fill = GridBagConstraints.BOTH;
			monetaryOrderAcknowledgementTable = new JTable();
			monetaryOrderAcknowledgementTableModel = new DefaultTableModel(new Object[][]{},
					new String[] { "Defendant", "Collecting Court", "Fined", "Compensation", "Costs", "Order Date", "Date Acknowledgement Received" }) {
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			monetaryOrderAcknowledgementTable.setModel(monetaryOrderAcknowledgementTableModel);
			// --- Add a column to hold the monetaryOrderTrackingBasicValue object, ---
			// --- this column will not be displayed ---
			monetaryOrderAcknowledgementTableModel.addColumn("monetaryOrderTrackingBasicValue");
			monetaryOrderAcknowledgementTable.getColumnModel().getColumn(monetaryOrderAcknowledgementTableModel.getColumnCount()-1).setMinWidth(0);
			monetaryOrderAcknowledgementTable.getColumnModel().getColumn(monetaryOrderAcknowledgementTableModel.getColumnCount()-1).setMaxWidth(0);
			//set column widths
			monetaryOrderAcknowledgementTable.getColumnModel().getColumn(Columns.DEFENDANT).setPreferredWidth(60);	//Defendant
			monetaryOrderAcknowledgementTable.getColumnModel().getColumn(Columns.COLLECTING_COURT).setPreferredWidth(80);	//Collecting court
			monetaryOrderAcknowledgementTable.getColumnModel().getColumn(Columns.FINED).setPreferredWidth(40);	//Fined
			monetaryOrderAcknowledgementTable.getColumnModel().getColumn(Columns.COMPENSATION).setPreferredWidth(40);	//Compensation
			monetaryOrderAcknowledgementTable.getColumnModel().getColumn(Columns.COSTS).setPreferredWidth(40);	//Costs
			monetaryOrderAcknowledgementTable.getColumnModel().getColumn(Columns.ORDER_DATE).setPreferredWidth(60);	//Order date
			monetaryOrderAcknowledgementTable.getColumnModel().getColumn(Columns.DATE_ACKNOWLEDGEMENT_RECEIVED).setPreferredWidth(60);	//Date ack received
			monetaryOrderAcknowledgementTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			monetaryOrderAcknowledgementTable.setPreferredScrollableViewportSize(monetaryOrderAcknowledgementTable.getPreferredSize());
			//Set cell renderer for tooltips
			for (int c = 0; c < monetaryOrderAcknowledgementTable.getColumnCount()-1; c++) {
				monetaryOrderAcknowledgementTable.getColumnModel().getColumn(c).setCellRenderer(new MonetaryOrderTableCellRenderer());
			}
			//---
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(monetaryOrderAcknowledgementTable);
			tablePanel.add(scrollPane, gbc);
			//--- Textfields ---
			txtDefendant = new JTextField();
			txtDefendant.setEnabled(false);
			txtDefendant.setColumns(10);
			txtDefendant.setMinimumSize(txtDefendant.getPreferredSize());
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weighty = 0.0;
			gbc.weightx = 0.0;
			gbc.gridwidth = 1;
			tablePanel.add(txtDefendant, gbc);
			//Court
			txtCollectingCourt = new JTextField();
			txtCollectingCourt.setEnabled(false);
			txtCollectingCourt.setColumns(15);
			txtCollectingCourt.setMinimumSize(txtCollectingCourt.getPreferredSize());
			txtCollectingCourt.setMaximumSize(txtCollectingCourt.getPreferredSize());
			gbc.gridx++;
			tablePanel.add(txtCollectingCourt, gbc);
			//Fine
			txtFined = new JTextField();
			txtFined.setEnabled(false);
			txtFined.setColumns(4);
			txtFined.setMinimumSize(txtFined.getPreferredSize());
			txtFined.setMaximumSize(txtFined.getPreferredSize());
			gbc.gridx++;
			tablePanel.add(txtFined, gbc);
			//Compensation
			txtCompensation = new JTextField();
			txtCompensation.setEnabled(false);
			txtCompensation.setColumns(4);
			txtCompensation.setMinimumSize(txtCompensation.getPreferredSize());
			txtCompensation.setMaximumSize(txtCompensation.getPreferredSize());
			gbc.gridx++;
			tablePanel.add(txtCompensation, gbc);
			//Costs
			txtCosts = new JTextField();
			txtCosts.setEnabled(false);
			txtCosts.setColumns(4);
			txtCosts.setMinimumSize(txtCosts.getPreferredSize());
			txtCosts.setMaximumSize(txtCosts.getPreferredSize());
			gbc.gridx++;
			tablePanel.add(txtCosts, gbc);
			//Order date
			txtOrderDate = new JTextField();
			txtOrderDate.setEnabled(false);
			txtOrderDate.setColumns(7);
			txtOrderDate.setMinimumSize(txtOrderDate.getPreferredSize());
			txtOrderDate.setMaximumSize(txtOrderDate.getPreferredSize());
			gbc.gridx++;
			tablePanel.add(txtOrderDate, gbc);
			//Acknowledgement Date
			dateAcknowledgementReceived = new XDatePanel(this, Calendar.getInstance()/*, false, null, ""*/);
			dateAcknowledgementReceived.clear();
			gbc.weightx = 1.0;
			gbc.gridx++;
			tablePanel.add(dateAcknowledgementReceived, gbc);
			//--- Save/Delete buttons ---
			saveButton = new JButton("Save");
			saveButton.addActionListener(this);
			saveButton.setEnabled(false);
			saveButton.setMinimumSize(saveButton.getPreferredSize());
			gbc.gridx++;
			tablePanel.add(saveButton, gbc);
			deleteButton = new JButton("Delete");
			deleteButton.addActionListener(this);
			deleteButton.setMinimumSize(deleteButton.getPreferredSize());
			deleteButton.setEnabled(false);
			gbc.gridx++;
			tablePanel.add(deleteButton, gbc);
			//--- Close button ---
			closeButton = new JButton("Close");
			closeButton.addActionListener(this);
			gbc.gridy++;
			tablePanel.add(closeButton, gbc);
			//--- Listeners ---
			monetaryOrderAcknowledgementTable.getSelectionModel().addListSelectionListener(this);
			
			dateAcknowledgementReceived.getDateComponent().addMChangeListener(new MChangeListener() {
				@Override
				public void valueChanged(MChangeEvent event) {
					dateAcknowledgementReceived.setEnabled(true);
					saveButton.setEnabled(true);
				}
			});
			
			dateAcknowledgementReceived.getEntryField().getDisplay().addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					saveButton.setEnabled(true);
				}
				@Override
				public void keyPressed(KeyEvent e) {
					saveButton.setEnabled(true);
				}
				@Override
				public void keyReleased(KeyEvent e) {
					saveButton.setEnabled(true);
				}
			});
			
		}
		return tablePanel;
	}


	// *******************************************************************************
	// * private class MonetaryOrderTableCellRenderer extends DefaultTableCellRenderer
	// *
	// * Purpose : Class to implement date-formatting & tooltips for table
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private class MonetaryOrderTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel c = new JLabel();
			c = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if ((column == 5) || (column == 6)) {
				if (!value.equals("")) {
					setValue(XDateFormat.format((Date)value, XDateFormat.DATEFORMAT));
				}
			}
			String toolTip = table.getValueAt(row, column).toString();
			c.setToolTipText(toolTip);
			return c;
		}
	}
	

	// *******************************************************************************
	// * private void moveModelToScreen()
	// *
	// * Purpose : Update local data from model
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@SuppressWarnings("unchecked")
	private void moveModelToScreen() {
		txtCaseNumber.setText(monetaryOrderAcknowledgementModel.getCaseType() + monetaryOrderAcknowledgementModel.getCaseNumber().toString());
		for (MonetaryOrderTrackingBasicValue monetaryOrderTrackingBasicValue : (Collection<MonetaryOrderTrackingBasicValue>)monetaryOrderAcknowledgementModel.getMonetaryOrderAcknowledgements()) {
			monetaryOrderAcknowledgementTableModel.addRow(new Object[]{ 
					monetaryOrderTrackingBasicValue.getDefendantOnCaseId() == null ? "" : defendantName(monetaryOrderTrackingBasicValue.getDefendantOnCaseId()),
					monetaryOrderTrackingBasicValue.getCollectMagistratesCourtId() == null ? "" : courtName(monetaryOrderTrackingBasicValue.getCollectMagistratesCourtId()),
					monetaryOrderTrackingBasicValue.getFined() == null ? "" : monetaryOrderTrackingBasicValue.getFined().setScale(2).toString(),
					monetaryOrderTrackingBasicValue.getCompensation() == null ? "" : monetaryOrderTrackingBasicValue.getCompensation().setScale(2).toString(),
					monetaryOrderTrackingBasicValue.getCosts() == null ? "" : monetaryOrderTrackingBasicValue.getCosts().setScale(2).toString(),
					monetaryOrderTrackingBasicValue.getOrderDate() == null ? "" : monetaryOrderTrackingBasicValue.getOrderDate(),
					monetaryOrderTrackingBasicValue.getAcknowledgementDate() == null ? "" : monetaryOrderTrackingBasicValue.getAcknowledgementDate(),
					monetaryOrderTrackingBasicValue
			});
		}
	}
	

	// *******************************************************************************
	// * private void moveScreenToModel
	// *
	// * Purpose : Update model from local data
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@SuppressWarnings("unused")
	private void moveScreenToModel() {
	}

	
	// *******************************************************************************
	// * private String defendantName(Integer defendantOnCaseId)
	// *
	// * Purpose : Get defendant name from defendantOnCaseId
	// * To call : defendantOnCaseId - ID of defendant to search for
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private String defendantName(Integer defendantOnCaseId) {
		DefendantControllerBeanBusinessDelegate defendantDelegate = null;
		DefendantOnCaseBasicValue defendantOnCase;
		DefendantValue defendant = null;
		String defendantName = "";
		defendantDelegate = XhibitDelegateHelper.getDefendantDelegate();
		try {
			defendantOnCase = defendantDelegate.getDefendantOnCaseDetails(defendantOnCaseId);
			defendant = defendantDelegate.findByDefId(defendantOnCase.getDefendantID());
			if (null != defendant.getFirstName()) {
				defendantName = defendant.getFirstName();
			}
			if (null != defendant.getSurName()) {
				if ((defendantName.length() > 0) && (defendant.getSurName().length() > 0)) {
					defendantName += " ";
				}
				defendantName += defendant.getSurName();
			}
		} catch (DefendantControllerException e) {
			log.debug("Unable to find defendant "+e);
		} catch (FinderException e) {
			log.debug("Unable to find defendant "+e);
		}
		return defendantName;
	}


	// *******************************************************************************
	// * private String courtName(Integer courtId)
	// *
	// * Purpose : Get court name from courtId
	// * To call : courtId - ID of defendant to search for
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private String courtName(Integer courtId) {
		String courtName = "";
    	RefCourtBasicValue courtBasicValue;
	    try {	    	
    		courtBasicValue = (RefCourtBasicValue)(XhibitDelegateHelper.getBizRefDelegate().findCourtByRefId(courtId));
    		courtName = courtBasicValue.getCourtFullName();
		} catch (SysRefControllerException e) {
			log.debug("Unable to find court "+e);
		}
		return courtName;
	}


	@Override
	public void stepInitialise() throws CSRecoverableException {
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
	}


	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}


	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		String msg;
		String title;
		MonetaryOrderTrackingControllerBeanBusinessDelegate monetaryOrderTrackingDelegate = XhibitDelegateHelper.getMonetaryOrderTrackingDelegate();
		int selectedRow = monetaryOrderAcknowledgementTable.getSelectedRow();
		//--- Delete button handler ---
		if (e.getSource().equals(deleteButton)) {
			if (selectedRow >= 0) {
				msg = ResourceBundleHelper.getResource(XhibitBundles.OrdersClient, "monetary.orders.client.delete.message");
				title = ResourceBundleHelper.getResource(XhibitBundles.OrdersClient, "monetary.orders.client.delete.title");
				int r = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION);
				if (JOptionPane.YES_OPTION == r) {
					try {
						//Database update
						MonetaryOrderTrackingBasicValue monetaryOrderTrackingBasicValue = (MonetaryOrderTrackingBasicValue)monetaryOrderAcknowledgementTable.getValueAt(monetaryOrderAcknowledgementTable.getSelectedRow(), Columns.MONETARY_ORDER);
						monetaryOrderTrackingBasicValue.setObsInd("Y");
						monetaryOrderTrackingBasicValue.setLastUpdatedBy(userDisplayName);
						monetaryOrderTrackingDelegate.update(monetaryOrderTrackingBasicValue, userDisplayName);
						//Table update
						monetaryOrderAcknowledgementTableModel.removeRow(selectedRow);
						//Clear table selection & textfields
						clearSelection();
					} catch (MonetaryOrderTrackingControllerException ex) {
						log.debug("Unable to update MonetaryOrder "+ex);
						XHIBITConstant.handleError(ex, this.getClass());
					}
				}
			}
		}
		//--- Save button handler ---
		if (e.getSource().equals(saveButton)) {
			try {
				Date enteredDate;
				enteredDate = getDateAcknowledgementReceived().getDateComponent().getValue();
				Date orderDate = (Date)monetaryOrderAcknowledgementTable.getValueAt(selectedRow, 5);
				Date todayDate = new Date();
				//Date validation
				if (enteredDate.after(orderDate) && !enteredDate.after(todayDate)) {
					//Database update
					MonetaryOrderTrackingBasicValue monetaryOrderTrackingBasicValue = (MonetaryOrderTrackingBasicValue)monetaryOrderAcknowledgementTable.getValueAt(monetaryOrderAcknowledgementTable.getSelectedRow(), Columns.MONETARY_ORDER);
					monetaryOrderTrackingBasicValue.setAcknowledgementDate(enteredDate);
					monetaryOrderTrackingBasicValue.setLastUpdatedBy(userDisplayName);
					boolean updateOk = monetaryOrderTrackingDelegate.update(monetaryOrderTrackingBasicValue, userDisplayName);
					//Table update
					if (updateOk) {
						//Re-read record from DB to update version in BasicValue
						monetaryOrderTrackingBasicValue = monetaryOrderTrackingDelegate.findByPrimaryKey(monetaryOrderTrackingBasicValue.getMonetaryOrderTrackingId());
						monetaryOrderAcknowledgementTable.setValueAt(monetaryOrderTrackingBasicValue.getAcknowledgementDate(), selectedRow, Columns.DATE_ACKNOWLEDGEMENT_RECEIVED);
						monetaryOrderAcknowledgementTable.setValueAt(monetaryOrderTrackingBasicValue, selectedRow, Columns.MONETARY_ORDER); 
						//Clear table selection & textfields
						clearSelection();
						dateAcknowledgementReceived.setEnabled(false);
						saveButton.setEnabled(false);
					} else {
						msg = ResourceBundleHelper.getResource(XhibitBundles.OrdersClient, "monetary.orders.client.error.db");
						title = ResourceBundleHelper.getResource(XhibitBundles.OrdersClient, "monetary.orders.client.error.title");
						JOptionPane.showMessageDialog(null, msg, title, JOptionPane.OK_OPTION);
					}
				} else {
					msg = ResourceBundleHelper.getResource(XhibitBundles.OrdersClient, "monetary.orders.client.error.date");
					title = ResourceBundleHelper.getResource(XhibitBundles.OrdersClient, "monetary.orders.client.error.title");
					JOptionPane.showMessageDialog(null, msg, title, JOptionPane.OK_OPTION);
				}
			} catch (ParseException ex) {
				log.debug("Unable to get acknowledgment date");
			} catch (MonetaryOrderTrackingControllerException e1) {
				log.debug("Unable to update "+e1);
				XHIBITConstant.handleError(e1, this.getClass());

			} catch (FinderException e1) {
				log.debug("Unable to find basic value object : "+e1);
				XHIBITConstant.handleError(e1, this.getClass());
			}
			
		}
		//--- Close button handler ---
		if (e.getSource().equals(closeButton)) {
			msg = ResourceBundleHelper.getResource(XhibitBundles.OrdersClient, "monetary.orders.client.exit.message");
			title = ResourceBundleHelper.getResource(XhibitBundles.OrdersClient, "monetary.orders.client.exit.title");
			int r = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION);
			if (JOptionPane.YES_OPTION == r) {
				parent.dispose();
			}
		}
	}

	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if ((monetaryOrderAcknowledgementTable.getRowCount() > 0) && (monetaryOrderAcknowledgementTable.getSelectedRow() >= 0)) {
			//--- Set textfield text ---
			int selectedRow = monetaryOrderAcknowledgementTable.getSelectedRow();
			getTxtDefendant().setText(monetaryOrderAcknowledgementTable.getValueAt(selectedRow, Columns.DEFENDANT).toString());
			getTxtCollectingCourt().setText(monetaryOrderAcknowledgementTable.getValueAt(selectedRow, Columns.COLLECTING_COURT).toString());
			getTxtFined().setText(monetaryOrderAcknowledgementTable.getValueAt(selectedRow, Columns.FINED).toString());
			getTxtCompensation().setText(monetaryOrderAcknowledgementTable.getValueAt(selectedRow, Columns.COMPENSATION).toString());
			getTxtCosts().setText(monetaryOrderAcknowledgementTable.getValueAt(selectedRow, Columns.COSTS).toString());
			if (!monetaryOrderAcknowledgementTable.getValueAt(selectedRow, Columns.ORDER_DATE).equals("")) {
				Date d = (Date)monetaryOrderAcknowledgementTable.getValueAt(selectedRow, Columns.ORDER_DATE);
				getTxtOrderDate().setText(XDateFormat.format(d, XDateFormat.DATEFORMAT));
			}
			if (monetaryOrderAcknowledgementTable.getValueAt(selectedRow, Columns.DATE_ACKNOWLEDGEMENT_RECEIVED).equals("")) {
				getDateAcknowledgementReceived().clear();
			} else {
				getDateAcknowledgementReceived().setDate((Date)monetaryOrderAcknowledgementTable.getValueAt(selectedRow, Columns.DATE_ACKNOWLEDGEMENT_RECEIVED));
			}
			//--- Set caret position in case strings are too long ---
			getTxtDefendant().setCaretPosition(0);
			getTxtCollectingCourt().setCaretPosition(0);
			//--- Set tooltips ---
			getTxtDefendant().setToolTipText(getTxtDefendant().getText());
			getTxtCollectingCourt().setToolTipText(getTxtCollectingCourt().getText());
			getTxtFined().setToolTipText(getTxtFined().getText());
			getTxtCompensation().setToolTipText(getTxtCompensation().getText());
			getTxtCosts().setToolTipText(getTxtCosts().getText());
			getTxtOrderDate().setToolTipText(getTxtOrderDate().getText());
			//--- Enable date picker ---
			dateAcknowledgementReceived.setEnabled(true);
			//--- Enable delete button ---
			deleteButton.setEnabled(true);
			//--- Disable save button ---
			saveButton.setEnabled(false);
		}
	}
	

	public void clearSelection() {
		monetaryOrderAcknowledgementTable.clearSelection();
		getTxtDefendant().setText("");
		getTxtCollectingCourt().setText("");
		getTxtFined().setText("");
		getTxtCompensation().setText("");
		getTxtCosts().setText("");
		getTxtOrderDate().setText("");
		getDateAcknowledgementReceived().clear();
		deleteButton.setEnabled(false);
	}

}
