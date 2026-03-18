package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
 * Title: BailActOffencePanel
 * </p>
 * <p>
 * Description: This is the panel that houses the bail act offences on the charges menu, it is
 * used in place of the normal OffencePanel because a different set of actions need to be associated
 * with each row but the functionality and layout are all based on the orginal class. 
 * </p>
 * 
 * <p>
 * Company: Logica 
 * </p>
 * 
 * @author luis Valenzuela
 * @version 1.0
 * 
 */
public class BailActOffencePanel extends JPanel implements UncodedOffenceInterface {

    private static final long serialVersionUID = 1L;
    private static final int PANEL_WIDTH = 300;
    private static final int OFFENCE_COLUMN = OffenceTableModel.OFFENCE_COLUMN;
    private static final int OFFENCE_DESC_COLUMN = OffenceTableModel.DESCRIPTION_COLUMN;
    private static final int DEFENDANT_COLUMN = OffenceTableModel.DEFENDANT_COLUMN;

    private ChargesController parent = null;
    private XTable bailActOffenceTable = null;
    private ChargesSelectionModel csm = null;
    private BailActOffenceTableModel bailActOffenceTableModel = null;

    private JPopupMenu popup = null;
    private JMenuItem menuItem = null;
    private int selectedRow = -1;
    private int selectedColumn = -1;

    public BailActOffencePanel(ChargesController cc, ChargesSelectionModel csm){
        super();
        this.setLayout(new GridBagLayout());
        this.parent = cc;
        this.csm = csm;
        
        add(new JScrollPane(getOffenceTable()), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    }

    protected JTable getOffenceTable() {
        if (bailActOffenceTable == null) {
            bailActOffenceTableModel = new BailActOffenceTableModel(parent.getFailure2AppearList() );
            bailActOffenceTable = XTableFactory.getInstance().createMultiLineTable(bailActOffenceTableModel);
            bailActOffenceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bailActOffenceTable.initColumnSizes(new Object[] { XTableFactory.COLUMN_WIDTH_UNDEFINED, "00000",
                    XTableFactory.COLUMN_WIDTH_UNDEFINED }, 400);
            bailActOffenceTable.getTableHeader().setReorderingAllowed(false);

            int cfsTableHeight = 25 + (XHIBITConstant.TABLE_ROW_HEIGHT * bailActOffenceTableModel.getRowCount());
            Dimension cfsTableDimension = new Dimension(PANEL_WIDTH, cfsTableHeight);
            // offenceTable.setMinimumSize(cfsTableDimension);
            bailActOffenceTable.setPreferredScrollableViewportSize(cfsTableDimension);

            ListSelectionModel rowSM = bailActOffenceTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    actionListRowSelectionChanged(e);
                }
            });
            // offenceTable.setColumnSelectionAllowed(true);

            ListSelectionModel colSM = bailActOffenceTable.getColumnModel().getSelectionModel();
            colSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    actionListColumnSelectionChanged(e);
                }
            });

            MouseListener popupListener = new PopupListener(getPopup());
            bailActOffenceTable.addMouseListener(popupListener);
        }
        return bailActOffenceTable;
    }

    private JPopupMenu getPopup() {
        if (popup == null) {
            // Create the popup menu.
            popup = new JPopupMenu();
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.ChangeBailActOffence));
            popup.add(menuItem);
            menuItem = new JMenuItem(XhibitActions.getAction(parent.getXAC(), XhibitActions.RemoveBailActOffence));
            popup.add(menuItem);
        }
        return popup;
    }

    private void actionMouseClickedOnTable(MouseEvent e){
        XHIBITConstant.debug("* * * * in actionMouseClickedOnTable   * * * *");

        // get point where user right clicked.
        selectedRow = getOffenceTable().rowAtPoint(e.getPoint());
        // Select row in table where user clicked.
        getOffenceTable().setRowSelectionInterval(selectedRow, selectedRow);

        selectedColumn = bailActOffenceTable.columnAtPoint(e.getPoint());
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
        ChargeSummaryTableRow cstr = (ChargeSummaryTableRow) bailActOffenceTableModel.getDataAt(row);
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
        csm.setChargeValue(cstr.getChargeValue());
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
                actionMouseClickedOnTable(e);
                if (thisPopup != null) {
                    thisPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }
}
