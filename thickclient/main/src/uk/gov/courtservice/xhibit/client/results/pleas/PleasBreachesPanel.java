package uk.gov.courtservice.xhibit.client.results.pleas;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import mseries.ui.MSimpleDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDateTableCellEditor;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDateTableCellRenderer;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellEditor;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellRenderer;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */
public class PleasBreachesPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JScrollPane scrollPane = null;

    private XTable xtable = null;

    private PleaControllerModel model = null;

    private PleaBreachTableModel pleaBreachTableModel = null;

    private JComboBox pleaCb = null;

    public PleasBreachesPanel(PleaControllerModel pcm) {
        try {
            this.model = pcm;
            pleaBreachTableModel = model.getPleaBreachTableModel();
            jbInit();
        } catch (Exception e) {
            XHIBITConstant.handleError(e);
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);

        this.add(getScrollPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    }

    public JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane(getBreachTable());
        }
        return scrollPane;
    }

    public XTable getBreachTable() {
        if (xtable == null) {
            xtable = XTableFactory.getInstance().createMultiLineTable(pleaBreachTableModel);
            xtable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            Object[] longValues = new Object[] { 
                    XTable.COLUMN_WIDTH_UNDEFINED, // no preference
                    XTable.COLUMN_WIDTH_UNDEFINED, // no preference 
                    XTable.COLUMN_WIDTH_UNDEFINED, // no preference 
                    " Admitted? ",
                    " Date put xxxxxx ", 
                    " Altered " };
            xtable.initColumnSizes(longValues, 200);
            xtable.getColumnModel().getColumn(PleaBreachTableModel.COLUMN_PLEA).setMaxWidth(PleaTableColumnWidths.PLEA_COLUMN_WIDTH);
            xtable.getColumnModel().getColumn(PleaBreachTableModel.COLUMN_DATEPUT).setMaxWidth(PleaTableColumnWidths.DATE_COLUMN_WIDTH);
            xtable.getColumnModel().getColumn(PleaBreachTableModel.COLUMN_ALTERED).setMaxWidth(PleaTableColumnWidths.ALTERED_COLUMN_WIDTH);
            xtable.setPreferredScrollableViewportSize(new Dimension(700, 400));
            xtable.getTableHeader().setReorderingAllowed(false);

            TableColumnModel columnModel = xtable.getColumnModel();

            // Plea description editor
            TableColumn pleaDescriptionColumn = columnModel.getColumn(PleaBreachTableModel.COLUMN_PLEA);
            pleaDescriptionColumn.setCellEditor(new XDefaultComboBoxCellEditor(getPleaCb()));
            pleaDescriptionColumn.setCellRenderer(new XDefaultComboBoxCellRenderer());

            // DatePut Column
            TableColumn pleaDatePutColumn = columnModel.getColumn(PleaBreachTableModel.COLUMN_DATEPUT);

            MSimpleDateFormat msdf = new MSimpleDateFormat(XDateFormat.simpleDateFormat);
            XDateTableCellEditor editor = new XDateTableCellEditor(msdf, this);
            pleaDatePutColumn.setCellEditor(editor);
            pleaDatePutColumn.setCellRenderer(new XDateTableCellRenderer(xtable));

            // Altered Column
            TableColumn alteredColumn = columnModel.getColumn(PleaBreachTableModel.COLUMN_ALTERED);
            alteredColumn.setCellRenderer(xtable.getDefaultRenderer(ImageIcon.class));
        }
        return xtable;
    }

    private JComboBox getPleaCb() {
        if (pleaCb == null) {
            Collection col = model.getBreachPleaRefData();
            pleaCb = new JComboBox(col.toArray());
            pleaCb.setSelectedIndex(0);
        }
        return pleaCb;
    }
}