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
import java.util.Iterator;

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

import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;

/**
 * <p>
 * Title: Shows the summary details of a Breach on the Charges screen
 * </p>
 * <p>
 * Description: Shows the offences and defendant for one Breach. There will be
 * one of these panels for each Breach on the Case.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Simon Gilmore EDS
 * @version 1.0
 */
public class BreachSummaryPanel extends ChargePanel implements UncodedOffenceInterface {
    
    private static final int OFFENCE_COLUMN = 0;

    private static final int DESC_COLUMN = 1;

    private static final int PANEL_HEADER_HEIGHT = 45;

    private static final int PANEL_WIDTH = 300;

    private int panelHeight = 0;

    private int tableHeight = 0;

    private ChargesController parent = null;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JPanel displayPanel = null;

    private JScrollPane chargeSummaryTableScrollPane = null;

    private JLabel defendantName = null;

    private XTable chargeSummaryTable = null;

    private ChargeValue breach = null;

    private BreachSummaryTableModel chargeSummaryTableModel;

    private Dimension panelDimension = null;

    private Dimension tableDimension = null;

    private JPopupMenu popup = null;

    private JPopupMenu.Separator breachSeperator;

    private JMenuItem menuItemRemoveBreach;

    private JMenuItem menuItemUncodedOffence;

    public BreachSummaryPanel(ChargesController cc, Object object) {
        super((ChargesSelectionModel) object);

        parent = cc;
        breach = getChargeSelectionModel().getChargeValue();

        String chargeType = breach.getChargeTypeDescription();
        String breachType = new String();

        if (breach.getBreachValue().getBreachType().equals("B")) {
            breachType = ResourceBundleHelper.getResource(XhibitBundles.Breaches, "bringBackTitle");
        } else if (breach.getBreachValue().getBreachType().equals("C")) {
            breachType = ResourceBundleHelper.getResource(XhibitBundles.Breaches, "committalForBreachTitle");
        }

        String titleText = chargeType + " " + breach.getCrestChargeSeqNo() + " (" + breachType + ")";
        getTitleLabel().setText(titleText);
        chargeSummaryTableModel = new BreachSummaryTableModel(breach);
        tableHeight = 25 + (XHIBITConstant.TABLE_ROW_HEIGHT * chargeSummaryTableModel.getRowCount());
        panelHeight = PANEL_HEADER_HEIGHT + tableHeight;
        panelDimension = new Dimension(PANEL_WIDTH, panelHeight);
        tableDimension = new Dimension(PANEL_WIDTH, tableHeight);

        getMainPanel().add(
                createPanel(),
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                        new Insets(2, 2, 2, 2), 0, 0));
    }

    private JPanel createPanel() {
        if (displayPanel == null) {
            displayPanel = new JPanel();
            displayPanel.setLayout(gridBagLayout1);
            displayPanel.add(getDefendantName(breach.getDefendantID()), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0,
                    0));
            displayPanel.add(getChargeSummaryTableScrollPane(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

            displayPanel.setMinimumSize(panelDimension);
        }
        return displayPanel;
    }

    private JLabel getDefendantName(Integer defendantId) {
        if (defendantName == null) {
            defendantName = new JLabel();
            defendantName.setMinimumSize(new Dimension(150, XHIBITConstant.getLineHeight()));
            DefendantValue dv = getDefendantValue(defendantId);
            if (dv != null)
                defendantName.setText(dv.getSurName() + ", " + dv.getFirstName());
        }
        return defendantName;
    }

    private DefendantValue getDefendantValue(Integer defId) {
        if (defId == null) {
            XHIBITConstant.error("Breach does not have a defendant. Defendant id = " + defId);
        } else {
            Iterator iter = parent.getModel().getCCV().getAllDefendants().iterator();
            while (iter.hasNext()) {
                DefendantValue item = (DefendantValue) iter.next();
                if (item.getDefendantID().intValue() == defId.intValue())
                    return item;
            }
        }
        return null;
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
            chargeSummaryTable.setPreferredScrollableViewportSize(tableDimension);

            chargeSummaryTable.initColumnSizes(new Object[] { "0000", XTableFactory.COLUMN_WIDTH_UNDEFINED }, 300);

            MouseClickListener popupListener = new MouseClickListener(getPopup());
            chargeSummaryTable.addMouseListener(popupListener);

            final TableListener listener = new TableListener();
            chargeSummaryTable.getSelectionModel().addListSelectionListener(listener);
            chargeSummaryTable.addHierarchyBoundsListener(listener);
        }
        return chargeSummaryTable;
    }

    private int getTableHeight() {
        int height = 0;
        for (int i = 0; i < getChargeSummaryTable().getRowCount(); i++) {
            height += getChargeSummaryTable().getRowHeight(i);
        }
        return height;
    }

    private void actionTableResized() {
        tableHeight = 25 + getTableHeight();
        tableDimension = new Dimension(PANEL_WIDTH, tableHeight);
        getChargeSummaryTable().setPreferredScrollableViewportSize(tableDimension);

        panelHeight = PANEL_HEADER_HEIGHT + tableHeight;
        panelDimension = new Dimension(PANEL_WIDTH, panelHeight);
        displayPanel.setMinimumSize(panelDimension);

        parent.resizeBreachPanel();
    }

    private void actionMouseClickedOnTable() {
        if (!isEnabled()) {
            // This Breach Summary panel was disabled until this mouse
            // click, so
            // need to enable it and disable any other Breach Summary
            // panels.
            parent.chargePanelSelectionChanged(this, parent.getBreachesMap(), false);
        }
    }

    protected ChargesController getParentController() {
        return this.parent;
    }

    private JPopupMenu getPopup() {
        if (popup == null) {
            // Create the popup menu.
            popup = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.AddBreachOffence));
            popup.add(menuItem);
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.CopyCharge));
            popup.add(menuItem);
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.ChangeOffence));
            popup.add(menuItem);
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.RemoveOffence));
            popup.add(menuItem);

            menuItemUncodedOffence = new JMenuItem(XhibitActions.getAction(parent.getXAC(),
                    XhibitActions.UpdateUncodedOffence));

            breachSeperator = new JPopupMenu.Separator();
            popup.add(breachSeperator);
            menuItemRemoveBreach = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.RemoveBreach));
            popup.add(menuItem);
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.AdditionalBreachOffenceDefendantInfo));
            popup.add(menuItem);
        }
        return popup;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            parent.setSelectedBreachPanel(this);
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
            actionMouseClickedOnTable();
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if ((thisPopup != null) && e.isPopupTrigger()) {
                final JTable table = getChargeSummaryTable();
                final Point point = e.getPoint();

                if (table.contains(point)) {
                    if (table.isEnabled()) {
                        final int row = table.rowAtPoint(point);
                        final int column = table.columnAtPoint(point);
                        table.requestFocus();
                        table.setRowSelectionInterval(row, row);
                        table.setColumnSelectionInterval(column, column);

                        BreachSummaryTableModel bstm = (BreachSummaryTableModel) table.getModel();
                        // CR58 Add Edit Uncoded Offence...
                        if (bstm.getRow(row).getOffenceValue().getOffenceCode().equalsIgnoreCase(
                                UNCODED_OFFENCE_REFERENCE_CODE)) {
                            popup.remove(menuItemRemoveBreach);
                            popup.remove(breachSeperator);
                            popup.add(menuItemUncodedOffence);
                            popup.add(breachSeperator);
                            popup.add(menuItemRemoveBreach);
                        } else {
                            popup.remove(menuItemUncodedOffence);
                        }

                    }
                    thisPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }

    private class TableListener implements ListSelectionListener, HierarchyBoundsListener {
        public void valueChanged(ListSelectionEvent e) {
            final int rowIndex = getChargeSummaryTable().getSelectedRow();

            if (rowIndex != -1) {
                final ChargeSummaryTableRow cstr = ((BreachSummaryTableModel) getChargeSummaryTable().getModel())
                        .getRow(rowIndex);

                getChargeSelectionModel().setOffenceValue(cstr.getOffenceValue());
                getChargeSelectionModel().setDefendantValue(getDefendantValue(breach.getDefendantID()));
                parent.getModel().setDefendantValue(getDefendantValue(breach.getDefendantID()));
                
                getChargeSelectionModel().setSelectionType(ChargesSelectionModel.OFFENCE_SELECTION_TYPE);
                
                
            } else {
                getChargeSelectionModel().setOffenceValue(null);
                getChargeSelectionModel().setDefendantValue(null);
            }
            getParentController().stepUpdateViewState();
        }

        public void ancestorMoved(HierarchyEvent he) {
        }

        public void ancestorResized(HierarchyEvent he) {
            actionTableResized();
        }
    }
}
