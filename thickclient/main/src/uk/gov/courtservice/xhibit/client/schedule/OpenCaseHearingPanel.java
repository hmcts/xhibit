package uk.gov.courtservice.xhibit.client.schedule;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;

/**
 * <p>
 * Title: XHIBIT 2
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
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class OpenCaseHearingPanel extends JPanel {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    protected XTable hearingTable = null;

    private JScrollPane courtTableScrollPane = null;

    private OpenCaseHearingTableModel courtTableModel = new OpenCaseHearingTableModel(new Object[] {});

    public OpenCaseHearingPanel() {
        jbInit();
    }

    void jbInit() {
        this.setLayout(gridBagLayout1);
        this.add(getTableScrollPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    private JScrollPane getTableScrollPane() {
        if (courtTableScrollPane == null) {
            courtTableScrollPane = new JScrollPane(getHearingTable());
        }
        return courtTableScrollPane;
    }

    protected XTable getHearingTable() {
        if (hearingTable == null) {
            hearingTable = XTableFactory.getInstance().createDefaultTable(courtTableModel); // new
                                                                                            // JTable(courtTableModel);
            hearingTable.initColumnSizes(new Object[] { "T20029999", "For Application XXX",
                    "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" }, 100);
            hearingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            TableCellRenderer tcr = new DisableTableCellRender();
            hearingTable.setDefaultRenderer(String.class, tcr);
        }
        return hearingTable;
    }
}