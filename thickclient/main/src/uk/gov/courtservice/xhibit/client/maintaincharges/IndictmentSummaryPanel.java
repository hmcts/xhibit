package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderChargeInfoValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.business.entities.migration.MigrateUtils;
import uk.gov.courtservice.xhibit.business.entities.migration.MigrateUtils.MigrationDetail;

/**
 * <p>
 * Title: Shows the summary details of an Indictment on the Charges screen
 * </p>
 * <p>
 * Description: Shows the counts and defendants and their status for one
 * Indictment. Also shows Sign Indictment details and Joinder details where
 * appropriate. There will be one of these panels for each Indictment on the
 * Case.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Simon Gilmore EDS
 * @version $Revision: 1.27 $
 */
public class IndictmentSummaryPanel extends ChargePanel implements UncodedOffenceInterface {

	private static final long serialVersionUID = 1L;

	private static final int COUNT_COLUMN = 0;

	private static final int COUNT_DESC_COLUMN = 1;

	private static final int DEFENDANT_COLUMN = 2;

	private static final int STATUS_COLUMN = 3;

	private static final int PANEL_MIN_WIDTH = 300;

	private int panelHeight = 0;

	private int tableHeight = 0;

	private ChargesController parent = null;

	private JPanel displayPanel = null;

	private JScrollPane chargeSummaryTableScrollPane = null;

	private XTable chargeSummaryTable = null;

	private JLabel isIndictmentSignedLabel = null;

	private JLabel indictmentSignedDateLabel = null;

	private ChargeValue indictment = null;

	private ChargeSummaryTableModel chargeSummaryTableModel;

	private Dimension panelMinDimension = null;

	private Dimension tableMinDimension = null;


	private JPopupMenu popup = null;

	private JPopupMenu.Separator offenceSeperator;

	private JMenuItem menuItemUncodedOffence;

	private boolean isThisIndictmentAJoinder = false;

	public IndictmentSummaryPanel(ChargesController cc, Object object) {
		super((ChargesSelectionModel) object);

		parent = cc;
		indictment = getChargeSelectionModel().getChargeValue();
		setTitle();
		chargeSummaryTableModel = new ChargeSummaryTableModel(indictment);
		tableHeight = 25 + (XHIBITConstant.TABLE_ROW_HEIGHT * chargeSummaryTableModel.getRowCount());
		panelHeight = 75 + tableHeight;
		panelMinDimension = new Dimension(PANEL_MIN_WIDTH, panelHeight);
		tableMinDimension = new Dimension(PANEL_MIN_WIDTH, tableHeight);

		getMainPanel().add(createPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		tableHeight = 25 + getTableHeight();
		tableMinDimension = new Dimension(PANEL_MIN_WIDTH, tableHeight);
		getChargeSummaryTable().setPreferredScrollableViewportSize(tableMinDimension);
	}

	private void setTitle() {
		String chargeType = indictment.getChargeTypeDescription();
		String indictmentTitle = chargeType + " " + indictment.getCrestChargeSeqNo();

		JoinderChargeInfoValue[] joinderCharges = indictment.getJoinderChargeInfoValues();
		if (joinderCharges != null && joinderCharges.length > 0) {
			isThisIndictmentAJoinder = true;
			// We have a Joinder Indictment.
			StringBuffer joinderDetails = new StringBuffer(" - ");
			joinderDetails.append(parent.getResource("Joinder"));
			joinderDetails.append(" (");
			for (int i = 0; i < joinderCharges.length; i++) {
				joinderDetails.append(joinderCharges[i].getCaseType());
				joinderDetails.append(joinderCharges[i].getCaseNumber());
				joinderDetails.append('-');
				joinderDetails.append(joinderCharges[i].getCrestChargeSeqNo());
				if (i < joinderCharges.length - 1) {
					joinderDetails.append(", ");
				}
			}
			joinderDetails.append(')');
			indictmentTitle = indictmentTitle + joinderDetails.toString();
		}

		getTitleLabel().setText(indictmentTitle);
	}

	private JPanel createPanel() {
		if (displayPanel == null) {
			displayPanel = new JPanel();
			displayPanel.setLayout(new GridBagLayout());

			final GridBagConstraints gbc = new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0, 0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0);

			
			displayPanel.add(getChargeSummaryTableScrollPane(), new GridBagConstraints(0, 2, 3, 1, 1.0, 1.0,
					GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			displayPanel.setMinimumSize(panelMinDimension);
		}

		return displayPanel;
	}



	private JScrollPane getChargeSummaryTableScrollPane() {
		if (chargeSummaryTableScrollPane == null) {
			chargeSummaryTableScrollPane = new JScrollPane(getChargeSummaryTable());
		}
		return chargeSummaryTableScrollPane;
	}

	protected JTable getChargeSummaryTable() {
		if (chargeSummaryTable == null) {
			chargeSummaryTable = XTableFactory.getInstance().createMultiLineTable(chargeSummaryTableModel);

			chargeSummaryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			chargeSummaryTable.setPreferredScrollableViewportSize(tableMinDimension);
			chargeSummaryTable.initColumnSizes(new Object[] { "000000", XTableFactory.COLUMN_WIDTH_UNDEFINED,
					XTableFactory.COLUMN_WIDTH_UNDEFINED, "lie on file" }, 300);

			MouseClickListener popupListener = new MouseClickListener(getPopup());
			chargeSummaryTable.addMouseListener(popupListener);
			// chargeSummaryTable.addMouseListener(new
			// TablePopupListener(getPopup(), chargeSummaryTable));

			final TableListener listener = new TableListener();
			chargeSummaryTable.getColumnModel().addColumnModelListener(listener);
			chargeSummaryTable.getSelectionModel().addListSelectionListener(listener);
			chargeSummaryTable.addHierarchyBoundsListener(listener);
		}
		return chargeSummaryTable;
	}

	private int getTableHeight() {
		int h = 0;
		for (int i = 0; i < getChargeSummaryTable().getRowCount(); i++) {
			h += getChargeSummaryTable().getRowHeight(i);
		}
		return h;
	}

	protected void actionTableResized() {
		tableHeight = 25 + getTableHeight();
		tableMinDimension = new Dimension(PANEL_MIN_WIDTH, tableHeight);
		getChargeSummaryTable().setPreferredScrollableViewportSize(tableMinDimension);

		panelHeight = 75 + tableHeight;
		panelMinDimension = new Dimension(PANEL_MIN_WIDTH, panelHeight);
		displayPanel.setMinimumSize(panelMinDimension);

		parent.resizeIndictmentPanel();
	}

	protected ChargesController getParentController() {
		return this.parent;
	}

	//TODO: See below
	private JPopupMenu getPopup() {
		if (popup == null) {
			// Create the popup menu.
			popup = new JPopupMenu();
			JMenuItem menuItem = new JMenuItem(
					XhibitActions.getAction(parent.getXAC(), XhibitActions.AddDefendantsToCount));
			popup.add(menuItem);
			menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.CopyCharge));
			// XDMX-6 Line below disables the Copy Charge option if in read only. Need more logic to check
			// whether the case being viewed is migrated in the new table after blocker is resolved.
			MigrateUtils migrateUtils = new MigrateUtils();
			int caseId = parent.getXAC().getApplicationCaseModel().getCaseId();
			boolean isReadOnly = parent.getXAC().getApplicationCaseModel().isInEditMode();
			MigrationDetail migrationDetail = migrateUtils.getMigrationDetails(caseId);
			
			if(migrationDetail != null && migrationDetail.isMigrated() && isReadOnly) {
				menuItem.setEnabled(false);
			}
			popup.add(menuItem);
			menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.RemoveDefendantsOnCount));
			popup.add(menuItem);
			menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.ChangeCount));
			popup.add(menuItem);
			menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.RemoveCount));
			popup.add(menuItem);
			menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.RenumberCounts));
			popup.add(menuItem);
			offenceSeperator = new JPopupMenu.Separator();
			menuItemUncodedOffence = new JMenuItem(
					XhibitActions.getAction(parent.getXAC(), XhibitActions.UpdateUncodedOffence));
			menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.AdditionalCountInfo));
			popup.add(menuItem);
			menuItem = new JMenuItem(
					XhibitActions.getAction(parent.getXAC(), XhibitActions.AdditionalDefendantOnCountInfo));
			popup.add(menuItem);
		}
		return popup;
	}

	protected void actionMouseClickedOnTable() {
		if (!isEnabled()) {
			// This Indictment panel was disabled until this mouse click, so
			// need to enable it and disable any other Indictment panels.
			parent.chargePanelSelectionChanged(this, parent.getIndictmentsMap(), false);
		}
	}

	public void setEnabled(boolean enabled) {
		if (enabled) {
			parent.setSelectedIndictmentPanel(this);
		} else {
			if (chargeSummaryTable != null) {
				getChargeSelectionModel().setSelectionType(ChargesSelectionModel.CHARGE_SELECTION_TYPE);
				ListSelectionModel lsm = chargeSummaryTable.getSelectionModel();
				lsm.clearSelection();
			}
		}
		super.setEnabled(enabled);
	}

	private class MouseClickListener extends MouseAdapter {
		private JPopupMenu thisPopup = null;

		public MouseClickListener(JPopupMenu pMenu) {
			thisPopup = pMenu;
		}

		public void mouseClicked(MouseEvent e) {
			actionMouseClickedOnTable();
		}

		public void mousePressed(MouseEvent e) {
			actionMouseClickedOnTable();
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if ((thisPopup != null) && e.isPopupTrigger()) {
				final JTable table = getChargeSummaryTable();
				final Point p = e.getPoint();

				if (table.contains(p)) {
					if (table.isEnabled()) {
						// quick hack to allow to work as previously,
						// as effectively right-click select the current row
						final int row = table.rowAtPoint(p);
						final int column = table.columnAtPoint(p);
						table.requestFocus();
						table.setRowSelectionInterval(row, row);
						table.setColumnSelectionInterval(column, column);

						ChargeSummaryTableModel cstm = (ChargeSummaryTableModel) table.getModel();
						// CR58 Add Edit Uncoded Offence...
						if (cstm.getRow(row).getOffenceValue().getOffenceCode()
								.equalsIgnoreCase(UNCODED_OFFENCE_REFERENCE_CODE)) {
							popup.add(offenceSeperator);
							popup.add(menuItemUncodedOffence);
						} else {
							popup.remove(menuItemUncodedOffence);
							popup.remove(offenceSeperator);
						}

					}
					thisPopup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	}

	private class TableListener implements ListSelectionListener, TableColumnModelListener, HierarchyBoundsListener {
		public void valueChanged(ListSelectionEvent e) {
			final int rowIndex = getChargeSummaryTable().getSelectedRow();

			if (rowIndex != -1) {
				final ChargeSummaryTableRow cstr = ((ChargeSummaryTableModel) getChargeSummaryTable().getModel())
						.getRow(rowIndex);
				getChargeSelectionModel().setOffenceValue(cstr.getOffenceValue());
				getChargeSelectionModel().setDefendantValue(cstr.getDefendantValue());

				// This is required here because in certain instances of
				// mouse clicks the column selected is lost.
				setColumnSelection();
			} else {
				getChargeSelectionModel().setOffenceValue(null);
				getChargeSelectionModel().setDefendantValue(null);
			}

			getParentController().stepUpdateViewState();
		}

		public void columnAdded(TableColumnModelEvent e) {
			// emtpy
		}

		public void columnRemoved(TableColumnModelEvent e) {
			// empty
		}

		public void columnMoved(TableColumnModelEvent e) {
			// empty
		}

		public void columnMarginChanged(ChangeEvent e) {
			// empty
		}

		public void columnSelectionChanged(ListSelectionEvent e) {
			setColumnSelection();

			getParentController().stepUpdateViewState();
		}

		private void setColumnSelection() {
			final int colIndex = getChargeSummaryTable().getSelectedColumn();

			switch (colIndex) {
			case COUNT_COLUMN:
			case COUNT_DESC_COLUMN:
				getChargeSelectionModel().setSelectionType(ChargesSelectionModel.OFFENCE_SELECTION_TYPE);
				break;
			case DEFENDANT_COLUMN:
				getChargeSelectionModel().setSelectionType(ChargesSelectionModel.DEFENDANT_SELECTION_TYPE);
				break;
			case STATUS_COLUMN:
				getChargeSelectionModel().setSelectionType(ChargesSelectionModel.OFFENCE_SELECTION_TYPE);
				break;
			default:
				getChargeSelectionModel().setSelectionType(ChargesSelectionModel.CHARGE_SELECTION_TYPE);
				break;
			}
		}

		public void ancestorMoved(HierarchyEvent he) {
			// empty
		}

		public void ancestorResized(HierarchyEvent he) {
			actionTableResized();
		}
	}

	public boolean isThisIndictmentAJoinder() {
		return isThisIndictmentAJoinder;
	}

	public void setThisIndictmentAJoinder(boolean isThisIndictmentAJoinder) {
		this.isThisIndictmentAJoinder = isThisIndictmentAJoinder;
	}
}
