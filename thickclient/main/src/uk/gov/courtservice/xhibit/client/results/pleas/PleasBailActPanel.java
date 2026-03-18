package uk.gov.courtservice.xhibit.client.results.pleas;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import mseries.ui.MSimpleDateFormat;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
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
 * Title: PleasBailActsPanel
 * </p>
 * <p>
 * Description: Panel for holding plea informattion associated with Bail Act Offences.
 * These offences are held on a Failure to Appear Charge, this panel was modelled on 
 * the existing Breach Panel
 * </p>
 * 
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.0
 */
public class PleasBailActPanel extends JPanel {
    
    private static final long serialVersionUID = 102L;
    
    /* components */
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JScrollPane scrollPane = null;
    private XTable xtable = null;
    private JComboBox pleaCb = null;
    
    private PleaControllerModel model = null;
    private PleaBailActTableModel pleaBailActTableModel = null;

   
    public PleasBailActPanel(PleaControllerModel pcm) {
        try {
            this.model = pcm;
            pleaBailActTableModel = model.getPleaBailActTableModel();
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
            scrollPane = new JScrollPane(getBailActTable());
        }
        return scrollPane;
    }

    
    public XTable getBailActTable() {
        if (xtable == null) {
            xtable = XTableFactory.getInstance().createMultiLineTable(pleaBailActTableModel);
            xtable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            Object[] longValues = new Object[] { 
                    XTable.COLUMN_WIDTH_UNDEFINED, // no preference
                    XTable.COLUMN_WIDTH_UNDEFINED, // no preference 
                    " Date failure to Attend put.  ", 
                    " Admitted?  ",
                    " Altered " };
            xtable.initColumnSizes(longValues, 700);
            xtable.getColumnModel().getColumn(PleaBailActTableModel.COLUMN_PLEA).setMaxWidth(PleaTableColumnWidths.PLEA_COLUMN_WIDTH);
            xtable.getColumnModel().getColumn(PleaBailActTableModel.COLUMN_DATEPUT).setMaxWidth(PleaTableColumnWidths.BAIL_ACT_DATE_COLUMN_WIDTH);
            xtable.getColumnModel().getColumn(PleaBailActTableModel.COLUMN_ALTERED).setMaxWidth(PleaTableColumnWidths.ALTERED_COLUMN_WIDTH); 
            
            xtable.setPreferredScrollableViewportSize(new Dimension(700, 400));
            xtable.getTableHeader().setReorderingAllowed(false);

            TableColumnModel columnModel = xtable.getColumnModel();

            // Plea description editor
            TableColumn pleaColumn = columnModel.getColumn(PleaBailActTableModel.COLUMN_PLEA);
            pleaColumn.setCellEditor(new XDefaultComboBoxCellEditor(getPleaCb()));
            pleaColumn.setCellRenderer(new XDefaultComboBoxCellRenderer());

            // DatePut Column
            TableColumn pleaDatePutColumn = columnModel.getColumn(PleaBailActTableModel.COLUMN_DATEPUT);

            MSimpleDateFormat msdf = new MSimpleDateFormat(XDateFormat.simpleDateFormat);
            XDateTableCellEditor editor = new XDateTableCellEditor(msdf, this);
            pleaDatePutColumn.setCellEditor(editor);
            pleaDatePutColumn.setCellRenderer(new XDateTableCellRenderer(xtable));

            // Altered Column
            TableColumn alteredColumn = columnModel.getColumn(PleaBailActTableModel.COLUMN_ALTERED);
            alteredColumn.setCellRenderer(xtable.getDefaultRenderer(ImageIcon.class));
        }
        return xtable;
    }

    private JComboBox getPleaCb() {
        if (pleaCb == null) {
            Collection col = model.getBailActPleaRefData();
            pleaCb = new JComboBox(col.toArray());
            pleaCb.setSelectedIndex(0);
        }
        return pleaCb;
    }
    
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        int rowCount = pleaBailActTableModel.getRowCount();
        for (int i = 0; i < rowCount; ++i) {
            Date date = (Date)pleaBailActTableModel.getValueAt(i, PleaBailActTableModel.COLUMN_DATEPUT);
            String plea = (String)pleaBailActTableModel.getValueAt(i, PleaBailActTableModel.COLUMN_PLEA);
            if (date == null && (plea != null && !plea.equals(""))
                    || date != null && (plea == null || plea.equals(""))) {
                throw new CSRecoverableException(
                        "results.saveError.bailactpleainvalid", 
                        "the plea and date_put must be both set or both unset");
            }
        }
    }
}