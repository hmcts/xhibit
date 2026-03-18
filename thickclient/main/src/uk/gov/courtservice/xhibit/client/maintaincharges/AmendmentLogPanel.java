package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import uk.gov.courtservice.xhibit.client.courtlog.CourtLogEventsTableModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.courtlog.helpers.xsl.CourtLogXslHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.xsl.TranslationType;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: Amendment Log Panel
 * </p>
 * <p>
 * Description: Displays the Court Log Events relating to amendments to Charges.
 * i.e. Court log events where the event type is in the charges category in the
 * xhb_court_log_category table.
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
 * Notes: Fred Vandendriessche changed JTable eventsTable and eventsTable to
 * XTable. IF required, this courtTable may be made sortable using the
 * .makeSortable() method. (and make your courtTableModel a subclass of
 * XSortableTableModel) Whether or not sorting is required, an XTable must be
 * used (always, from now on)
 * 
 */

public class AmendmentLogPanel extends JPanel {
    private static final int PANEL_WIDTH = 350;

    private XTable eventsTable;

    private CourtLogEventsTableModel eventTableModel = null;

    /**
     * Creates the panel to display the court log events relating to Charges.
     * 
     * @param chargeLogItems
     *            Array of CourtLogViewValue objects
     */
    public AmendmentLogPanel(CourtLogViewValue[] chargeLogItems) {
        translateLogItems(chargeLogItems);
        eventTableModel = new CourtLogEventsTableModel(chargeLogItems);

        jbInit();
    }

    /**
     * Creates an empty AmendmentLogPanel.
     */
    public AmendmentLogPanel() {
        this(new CourtLogViewValue[0]);
    }

    private void jbInit() {
        setLayout(new GridBagLayout());
        this.add(new JScrollPane(getEventsTable()), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));
    }

    private JTable getEventsTable() {
        if (eventsTable == null) {
            eventsTable = XTableFactory.getInstance().createMultiLineTable(eventTableModel);

            eventsTable.setPreferredScrollableViewportSize(new Dimension(PANEL_WIDTH, 100));

            eventsTable.getColumnModel().getColumn(CourtLogEventsTableModel.EVENT_COLUMN).setCellRenderer(
                    XTableFactory.getInstance().getStyleTableRenderer(eventsTable));

            eventsTable.initColumnSizes(new Object[] { "DD-MON-YYYY", "hh:mm", XTableFactory.COLUMN_WIDTH_UNDEFINED },
                    PANEL_WIDTH);

            eventsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            eventsTable.getTableHeader().setReorderingAllowed(false);
        }
        return eventsTable;
    }

    /**
     * Translates the events for the GUI CLIENT based on the default locale.
     * 
     * @param chargeLogItems
     *            Collection of CourtLogViewValue objects
     */
    private void translateLogItems(CourtLogViewValue[] chargeLogItems) {
        for (int i = 0; i < chargeLogItems.length; i++) {
            try {
                final String xml = CourtLogXslHelper.translateEvent(chargeLogItems[i], Locale.getDefault(),
                        TranslationType.GUI);
                chargeLogItems[i].setLogEntry(xml);
            } catch (Exception cle) {
                XHIBITConstant.handleError(cle);
            }
        }
    }
}