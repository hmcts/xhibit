package uk.gov.courtservice.xhibit.client.results.pleas;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellRenderer;

/**
 * <p>
 * Title: Pleas Section41 Panel
 * </p>
 * <p>
 * Description: Show the Pleas Section 41s (Summary Offences) table
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */
public class PleasSection41Panel extends JPanel {

    private static final long serialVersionUID = 1L;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JScrollPane scrollPane = null;

    private XTable xtable = null;

    private PleaControllerModel model = null;

    private PleaFilterSelectionModel pleaFilterSelectionModel = null;

    private PleaFilterModel pleaFilterModel = null;

    private JComboBox pleaCb = null;

    public PleasSection41Panel(PleaControllerModel pcm) {
        try {
            this.model = pcm;
            stepInitialise();
            jbInit();
        } catch (Exception e) {
            XHIBITConstant.handleError(e);
        }
    }

    public void stepInitialise() {
        pleaFilterSelectionModel = model.getS41FilterSelectionModel();
        pleaFilterModel = model.getS41FilterModel();
    }

    /**
     * Create and layout the widgets on this panel.
     */
    private void jbInit() {
        this.setLayout(gridBagLayout1);

        this.add(getScrollPane(), new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        PleaFilterPanel filterPanel = new PleaFilterPanel(pleaFilterSelectionModel, model, false, getSection41Table());

        this.add(filterPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane(getSection41Table());
        }
        return scrollPane;
    }

    /**
     * Lazy instantiates the Section 41s (Summary Offences) table.
     * 
     * @return the Section 41s table.
     */
    public XTable getSection41Table() {
        if (xtable == null) {
            xtable = XTableFactory.getInstance().createMultiLineTable(pleaFilterModel);
            xtable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            Object[] longValues = new Object[] { 
                    XTable.COLUMN_WIDTH_UNDEFINED, // no preference 
                    XTable.COLUMN_WIDTH_UNDEFINED, // no preference 
                    " Plea code ", 
                    " Plea Description ", 
                    " Altered " };
            XHIBITConstant.debug("Table column count: " + xtable.getColumnModel().getColumnCount());
            XHIBITConstant.debug("Long values size: " + longValues.length);
            xtable.initColumnSizes(longValues, 700);
            xtable.getColumnModel().getColumn(PleaSection41TableModel.COLUMN_PLEA_CODE).setMaxWidth(PleaTableColumnWidths.PLEA_CODE_COLUMN_WIDTH);
            xtable.getColumnModel().getColumn(PleaSection41TableModel.COLUMN_PLEA_DESC).setMaxWidth(PleaTableColumnWidths.PLEA_DESCRIPTION_COLUMN_WIDTH);
            xtable.getColumnModel().getColumn(PleaSection41TableModel.COLUMN_ALTERED).setMaxWidth(PleaTableColumnWidths.ALTERED_COLUMN_WIDTH);
            xtable.setPreferredScrollableViewportSize(new Dimension(700, 400));
            xtable.getTableHeader().setReorderingAllowed(false);

            TableColumnModel columnModel = xtable.getColumnModel();

            // Plea code editor
            TableColumn pleaCodeColumn = columnModel.getColumn(2);
            pleaCodeColumn.setCellEditor(new DefaultCellEditor(new JTextField()));

            // Plea description editor
            TableColumn pleaDescriptionColumn = columnModel.getColumn(3);
            PleaComboBoxEditor comboEditor = new PleaComboBoxEditor(getPleaCb(), model.getSection41PleaRefData());
            pleaDescriptionColumn.setCellEditor(comboEditor);
            pleaDescriptionColumn.setCellRenderer(new XDefaultComboBoxCellRenderer());

            // Altered Column
            TableColumn alteredColumn = columnModel.getColumn(4);
            alteredColumn.setCellRenderer(xtable.getDefaultRenderer(ImageIcon.class));
        }
        return xtable;
    }

    /**
     * Lazy instantiates the Plea description combo box.
     * 
     * @return the populated Plea description combo box.
     */
    private JComboBox getPleaCb() {
        if (pleaCb == null) {
            RefSystemCodeBasicValue ref;
            String pleaDesc;
            Collection col = model.getSection41PleaRefData();
            java.util.List<String> l = new ArrayList<String>();
            Iterator i = col.iterator();
            while (i.hasNext()) {
                ref = (RefSystemCodeBasicValue) i.next();
                pleaDesc = ref.getDecode();
                l.add(pleaDesc);
            }
            pleaCb = new JComboBox(l.toArray());
        }
        return pleaCb;
    }
}
