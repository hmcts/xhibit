package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;

/**
 * <p>
 * Title: XHIBIT2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 * 
 * Notes: Fred Vandendriessche changed JTable offenceTable to XTable. IF
 * required, this courtTable may be made sortable using the .makeSortable()
 * method. (and make your courtTableModel a subclass of XSortableTableModel)
 * Whether or not sorting is required, an XTable must be used (always, from now
 * on)
 */
public class OffencePanel extends JPanel implements UncodedOffenceInterface {
    private static final int PANEL_WIDTH = 300;

    private static final int OFFENCE_COLUMN = OffenceTableModel.OFFENCE_COLUMN;

    private static final int OFFENCE_DESC_COLUMN = OffenceTableModel.DESCRIPTION_COLUMN;

    private static final int DEFENDANT_COLUMN = OffenceTableModel.DEFENDANT_COLUMN;

    private ChargesController parent = null;

    private XTable offenceTable = null;

    private ChargesSelectionModel csm = null;

    private OffenceTableModel offenceTableModel = null;

    private JPopupMenu popup = null;

    private JMenuItem menuItem = null;

    private JPopupMenu.Separator offenceSeperator;

    private JMenuItem menuItemUncodedOffence;

    private int selectedRow = -1;

    private int selectedColumn = -1;

    public OffencePanel(ChargesController cc, ChargesSelectionModel csm) {
        super();
        this.setLayout(new GridBagLayout());
        this.parent = cc;
        this.csm = csm;
        add(new JScrollPane(getOffenceTable()), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    }

    protected JTable getOffenceTable() {
        if (offenceTable == null) {
            offenceTableModel = new OffenceTableModel(csm.getChargeValue());
            offenceTable = XTableFactory.getInstance().createMultiLineTable(offenceTableModel);
            offenceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            offenceTable.initColumnSizes(new Object[] { XTableFactory.COLUMN_WIDTH_UNDEFINED, "00000",
                    XTableFactory.COLUMN_WIDTH_UNDEFINED }, 400);
            // XTableFactory.getInstance().initColumnSizes( offenceTable,
            // new Object[]
            // {
            // "00000", XTableFactory.COLUMN_WIDTH_UNDEFINED,
            // XTableFactory.COLUMN_WIDTH_UNDEFINED
            // }, 400);

            offenceTable.getTableHeader().setReorderingAllowed(false);

            int cfsTableHeight = 25 + (XHIBITConstant.TABLE_ROW_HEIGHT * offenceTableModel.getRowCount());
            Dimension cfsTableDimension = new Dimension(PANEL_WIDTH, cfsTableHeight);
            // offenceTable.setMinimumSize(cfsTableDimension);
            offenceTable.setPreferredScrollableViewportSize(cfsTableDimension);

            ListSelectionModel rowSM = offenceTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    actionListRowSelectionChanged(e);
                }
            });
            // offenceTable.setColumnSelectionAllowed(true);

            ListSelectionModel colSM = offenceTable.getColumnModel().getSelectionModel();
            colSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    actionListColumnSelectionChanged(e);
                }
            });

            MouseListener popupListener = new PopupListener(getPopup());
            offenceTable.addMouseListener(popupListener);
        }
        return offenceTable;
    }

    private JPopupMenu getPopup() {
        if (popup == null) {
            // Create the popup menu.
            popup = new JPopupMenu();
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.AddDefendantsToOffence));
            popup.add(menuItem);
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.CopyCharge));
            popup.add(menuItem);
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.ChangeOffence));
            popup.add(menuItem);
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.RemoveOffence));
            popup.add(menuItem);
            offenceSeperator = new JPopupMenu.Separator();
            menuItemUncodedOffence = new JMenuItem(XhibitActions.getAction(parent.getXAC(),
                    XhibitActions.UpdateUncodedOffence));
            popup.add(menuItem);
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.AdditionalOffenceInfo));
            popup.add(menuItem);
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.AdditionalDefendantOnOffenceInfo));
            popup.add(menuItem);
        }
        return popup;
    }

    private void actionMouseClickedOnTable(MouseEvent e) {
        XHIBITConstant.debug("* * * * in actionMouseClickedOnTable   * * * *");

        // get point where user right clicked.
        selectedRow = getOffenceTable().rowAtPoint(e.getPoint());
        // Select row in table where user clicked.
        getOffenceTable().setRowSelectionInterval(selectedRow, selectedRow);

        selectedColumn = offenceTable.columnAtPoint(e.getPoint());
        getOffenceTable().setColumnSelectionInterval(selectedColumn, selectedColumn);

        // row = offenceTable.getSelectedRow();
        XHIBITConstant.debug("actionMouseClickedOnTable: row = " + selectedRow + " :  col = " + selectedColumn);

        populateChargesSelectionModel(selectedRow, selectedColumn);
    }

    private void actionListRowSelectionChanged(ListSelectionEvent e) {
        XHIBITConstant.debug("* * * * in actionListRowSelectionChanged   * * * * Row");
        // Ignore extra messages.
        if (e.getValueIsAdjusting())
            return;

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (lsm.isSelectionEmpty()) {
            // no rows are selected
        } else {
            selectedRow = lsm.getMinSelectionIndex();
            populateChargesSelectionModel(selectedRow, selectedColumn);
        }
    }

    private void actionListColumnSelectionChanged(ListSelectionEvent e) {
        XHIBITConstant.debug("* * * * in actionListColumnSelectionChanged   * * * * Column");
        // Ignore extra messages.
        if (e.getValueIsAdjusting())
            return;

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (lsm.isSelectionEmpty()) {
            // no columns are selected
        } else {
            selectedColumn = lsm.getMinSelectionIndex();
            populateChargesSelectionModel(selectedRow, selectedColumn);
        }
    }

    private void populateChargesSelectionModel(int row, int col) {
        ChargeSummaryTableRow cstr = (ChargeSummaryTableRow) offenceTableModel.getDataAt(row);
        switch (col) {
        case OFFENCE_COLUMN:
            XHIBITConstant.debug("OFFENCE_COLUMN");
            csm.setSelectionType(ChargesSelectionModel.OFFENCE_SELECTION_TYPE);
            break;
        case OFFENCE_DESC_COLUMN:
            XHIBITConstant.debug("OFFENCE_DESC_COLUMN");
            csm.setSelectionType(ChargesSelectionModel.OFFENCE_SELECTION_TYPE);
            break;
        case DEFENDANT_COLUMN:
            XHIBITConstant.debug("DEFENDANT_COLUMN");
            csm.setSelectionType(ChargesSelectionModel.DEFENDANT_SELECTION_TYPE);
            break;
        default:
            XHIBITConstant.debug("default");
            csm.setSelectionType(ChargesSelectionModel.OFFENCE_SELECTION_TYPE);
            break;
        }
        csm.setOffenceValue(cstr.getOffenceValue());
        csm.setDefendantValue(cstr.getDefendantValue());
        parent.stepUpdateViewState();
    }

    public ChargesSelectionModel getChargeSelectionModel() {
        return csm;
    }

    class PopupListener extends MouseAdapter {
        private JPopupMenu thisPopup = null;

        public PopupListener(JPopupMenu pMenu) {
            thisPopup = pMenu;
        }

        public void mouseClicked(MouseEvent e) {
            actionMouseClickedOnTable(e);
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                // getRow(e);
                actionMouseClickedOnTable(e);
                if (thisPopup != null) {
                    final Point p = e.getPoint();
                    final int row = offenceTable.rowAtPoint(p);

                    // CR58 Add Edit Uncoded Offence...
                    if (offenceTableModel.getRow(row).getOffenceValue().getOffenceCode().equalsIgnoreCase(
                            UNCODED_OFFENCE_REFERENCE_CODE)) {
                        popup.add(offenceSeperator);
                        popup.add(menuItemUncodedOffence);
                    } else {
                        popup.remove(menuItemUncodedOffence);
                        popup.remove(offenceSeperator);
                    }

                    thisPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }
}
