package uk.gov.courtservice.xhibit.client.schedule;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

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

public class OpenCaseCourtPanel extends JPanel {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JTable courtTable = null;

    private JScrollPane courtTableScrollPane = null;

    private OpenCaseCourtModel courtTableModel = new OpenCaseCourtModel(new Object[] {});

    public OpenCaseCourtPanel() {
        jbInit();
    }

    void jbInit() {
        this.setLayout(gridBagLayout1);
        this.add(getCourtTableScrollPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    private JScrollPane getCourtTableScrollPane() {
        if (courtTableScrollPane == null) {
            courtTableScrollPane = new JScrollPane(getCourtTable());
        }
        return courtTableScrollPane;
    }

    protected JTable getCourtTable() {
        if (courtTable == null) {
            courtTable = new JTable(courtTableModel);

            courtTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        return courtTable;
    }
}