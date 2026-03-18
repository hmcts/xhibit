package uk.gov.courtservice.xhibit.client.results.pleas;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import mseries.ui.MSimpleDateFormat;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.util.table.AdditionalInfoTableCellEditor;
import uk.gov.courtservice.xhibit.client.results.util.table.AdditionalInfoTableCellRenderer;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDateTableCellEditor;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDateTableCellRenderer;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellRenderer;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;

/**
 * <p>
 * Title: PleasIndictmentPanel
 * </p>
 * <p>
 * Description: Shows the Pleas Indictments Panel which contains the arraignment
 * date, the filtering options and the Indictment table.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Simon Gilmore
 */
public class PleasIndictmentPanel extends JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    public static final String SCREEN_CHANGED = "SCREEN_CHANGED";

    private static final Logger log = Logger.getLogger(PleasIndictmentPanel.class);

    private XhibitApplicationController xac = null;

    private ArraignmentPanel arraignmentPanel = null;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JScrollPane scrollPane = null;

    private JPanel buttonPanel = null;

    private JButton selectAllBtn = null;

    private JButton multiplePleaBtn = null;

    private XTable xtable = null;

    private PleaControllerModel model = null;

    private PleaFilterSelectionModel pleaFilterSelectionModel = null;

    private PleaFilterModel pleaFilterModel = null;

    private PleaFilterPanel filterPanel = null;

    private JComboBox pleaCb = null;

    private int[] oldRows;

    /**
     * Creates the PleasIndictmentPanel by getting the data required for this
     * panel and then creating and laying out the widgets on the panel.
     * 
     * @param pcm
     *            Data passed from the PleaController.
     */
    public PleasIndictmentPanel(PleaControllerModel pcm) {
        try {
            this.model = pcm;
            stepInitialise();
            jbInit();
        } catch (Exception e) {
            XHIBITErrorHandler.handleError(e);
        }
    }

    public void stepInitialise() {
        model.addPropertyChangeListener(this);
        pleaFilterSelectionModel = model.getIndictmentFilterSelectionModel();
        pleaFilterModel = model.getIndictmentFilterModel();
        xac = model.getACM().getXhibitApplicationController();
    }

    /**
     * Create and layout the widgets on this panel.
     */
    private void jbInit() {
        setLayout(gridBagLayout1);

        add(getArraignmentPanel(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        add(getFilterPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        add(getButtonPanel(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        add(getScrollPane(), new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    }

    private ArraignmentPanel getArraignmentPanel() {
        if (arraignmentPanel == null) {
            arraignmentPanel = new ArraignmentPanel(model);
        }
        return arraignmentPanel;
    }

    private PleaFilterPanel getFilterPanel() {
        if (filterPanel == null) {
            filterPanel = new PleaFilterPanel(pleaFilterSelectionModel, model, true, getIndictmentTable());
        }
        return filterPanel;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane(getIndictmentTable());
        }
        return scrollPane;
    }

    /**
     * Lazy instantiates the Indictment table.
     * 
     * @return the Indictment table.
     */
    protected XTable getIndictmentTable() {
        if (xtable == null) {
            xtable = XTableFactory.getInstance().createMultiLineTable(pleaFilterModel);
            xtable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            Object[] longValues = new Object[] { 
                    " No. ", 
                    " Count xxxxxxxxxx ",  
                    " Defendant xxxxxx ",  
                    " Plea Code ",
                    " Plea Description ",  
                    " Additional Info  ", 
                    " Arraignment Date ",
                    " Altered " };
            log.debug("Table column count: " + xtable.getColumnModel().getColumnCount());
            log.debug("Long values size: " + longValues.length);
            xtable.initColumnSizes(longValues, 750);
            xtable.getColumnModel().getColumn(PleaIndictmentTableModel.COLUMN_NO).setMaxWidth(PleaTableColumnWidths.OFFENCE_NO_COLUMN_WIDTH);
            xtable.getColumnModel().getColumn(PleaIndictmentTableModel.COLUMN_PLEA_CODE).setMaxWidth(PleaTableColumnWidths.PLEA_CODE_COLUMN_WIDTH);
            xtable.getColumnModel().getColumn(PleaIndictmentTableModel.COLUMN_DATE).setMaxWidth(PleaTableColumnWidths.DATE_COLUMN_WIDTH);
            xtable.getColumnModel().getColumn(PleaIndictmentTableModel.COLUMN_DATE).setMinWidth(PleaTableColumnWidths.DATE_COLUMN_WIDTH);
            xtable.getColumnModel().getColumn(PleaIndictmentTableModel.COLUMN_ALTERED).setMaxWidth(PleaTableColumnWidths.ALTERED_COLUMN_WIDTH);
            xtable.setPreferredScrollableViewportSize(new Dimension(750, 400));
            xtable.getTableHeader().setReorderingAllowed(false);

            TableColumnModel columnModel = xtable.getColumnModel();

            // Plea code editor
            TableColumn pleaCodeColumn = columnModel.getColumn(PleaIndictmentTableModel.COLUMN_PLEA_CODE);
            pleaCodeColumn.setCellEditor(new DefaultCellEditor(new JTextField()));

            // Plea description editor
            TableColumn pleaDescriptionColumn = columnModel.getColumn(PleaIndictmentTableModel.COLUMN_PLEA_DESC);
            PleaComboBoxEditor comboEditor = new PleaComboBoxEditor(getPleaCb(), model.getIndictmentPleaRefData());
            pleaDescriptionColumn.setCellEditor(comboEditor);
            pleaDescriptionColumn.setCellRenderer(new XDefaultComboBoxCellRenderer());

            // Additional Info column
            TableColumn additionalInfoColumn = columnModel.getColumn(PleaIndictmentTableModel.COLUMN_ADDITIONAL_INFO);
            additionalInfoColumn.setCellRenderer(new AdditionalInfoTableCellRenderer(xac, xtable.getMultiLineHelper()));
            additionalInfoColumn.setCellEditor(new AdditionalInfoTableCellEditor(xac, xtable.getMultiLineHelper()));
            // The following is to avoid the problem raised by PR 57373
            // The scrollpane withn the cell was being repainted with and
            // without the scrollbars
            // if the size of the text area matches the width of he scroll
            // bar.
            // Not ideal but prevents us from having to trap all the repaint
            // events
            additionalInfoColumn.setMinWidth(AdditionalInfoTableCellRenderer.COLUMN_ADDITIONAL_INFO_MIN_WIDTH);

            // Arraignment Date Column
            TableColumn dateColumn = columnModel.getColumn(PleaIndictmentTableModel.COLUMN_DATE);
            MSimpleDateFormat msdf = new MSimpleDateFormat(XDateFormat.simpleDateFormat);
            XDateTableCellEditor editor = new XDateTableCellEditor(msdf, this);
            editor.comp.setEditable(false);

            dateColumn.setCellEditor(editor);
            dateColumn.setCellRenderer(new XDateTableCellRenderer(xtable));

            // Altered Column
            TableColumn alteredColumn = columnModel.getColumn(PleaIndictmentTableModel.COLUMN_ALTERED);
            alteredColumn.setCellRenderer(xtable.getDefaultRenderer(ImageIcon.class));

            ListSelectionModel rowSM = xtable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    actionListRowSelectionChanged(e);
                }
            });
        }
        return xtable;
    }

    /**
     * Called when table row selection changes.
     * 
     * @param e
     *            Event that characterizes a change in the current table row
     *            selection.
     */
    private void actionListRowSelectionChanged(ListSelectionEvent e) {
        log.debug("in actionListRowSelectionChanged - Begin");
        // Ignore extra messages.
        if (e.getValueIsAdjusting())
            return;

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (lsm.isSelectionEmpty()) {
            // no rows are selected
        } else {
            int[] rows = xtable.getSelectedRows();
            model.setSelectedIndictments(rows);
            rowSelectionChanged(rows);
        }
    }

    private void rowSelectionChanged(final int[] newRows) {
        String oldRowsText = oldRows == null ? null : "" + oldRows.length;
        String newRowsText = newRows == null ? null : "" + newRows.length;
        log.debug("rowSelectionChanged " + "oldRows = " + oldRowsText + ", newRows = " + newRowsText);

        firePropertyChange(SCREEN_CHANGED, oldRows, newRows);
        oldRows = newRows;
    }

    /**
     * PropertyChangeListener implementation called when the arraignment date
     * changes.
     * 
     * @param pce
     *            the PropertyChangeEvent
     */
    public void propertyChange(PropertyChangeEvent pce) {
        log.debug("PCE old value = " + pce.getOldValue() + " new value = " + pce.getNewValue());

        // if the date was null and is now still null, no change so return.
        if (pce.getOldValue() == null && pce.getNewValue() == null) {
            return;
        }

        // if the new value is null, the arraignment date has not been set, so
        // disable the filters, otherwise restore them to their original states.
        if (pce.getNewValue() == null) {
            getFilterPanel().disableFilters();

            // Cannot use "enableSelectAll" of XPanel here as events are
            // being
            // trapped due to invalid/blank arraignment date.
            // enableSelectAll(false);
            XhibitActions.getAction(xac, XhibitActions.EditSelectAll).setEnabled(false);

            XhibitActions.getAction(xac, XhibitActions.MultiplePlea).setEnabled(false);
        } else {
            getFilterPanel().restoreFilters();

            // There may have been a change in the table selection while an
            // incomplete/invalid arraignment date was being entered. Then
            // the
            // stepValidate call in the tables focusGained method would
            // throw an
            // exception and the ListSelectionListener's valueChanged call
            // actionListRowSelectionChanged gets stopped by
            // e.getValueIsAdjusting()
            int[] rows = xtable.getSelectedRows();
            model.setSelectedIndictments(rows);
            rowSelectionChanged(rows);

            // enableSelectAll(true);
            XhibitActions.getAction(xac, XhibitActions.EditSelectAll).setEnabled(true);

            XhibitActions.getAction(xac, XhibitActions.MultiplePlea).setEnabled(rows != null && rows.length > 1);
        }
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
            Collection col = model.getIndictmentPleaRefData();
            java.util.List<String> l = new ArrayList<String>();
            Iterator i = col.iterator();
            while (i.hasNext()) {
                ref = (RefSystemCodeBasicValue) i.next();
                pleaDesc = ref.getDecode();
                l.add(pleaDesc);
            }
            pleaCb = new JComboBox(l.toArray());
            // pleaCb.setSelectedIndex(0);
        }
        return pleaCb;
    }

    /**
     * Lazy instantiate the button panel for selectin table rows and launching
     * the multiple pleas dialog.
     * 
     * @return button panel.
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(gridBagLayout1);
            buttonPanel.add(getSelectAllBtn(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
            buttonPanel.add(getMultiplePleaBtn(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        }
        return buttonPanel;
    }

    /**
     * Lazy instantiate the Select All button that selects all of the visible
     * rows in the table.
     * 
     * @return the SelectAll button.
     */
    private JButton getSelectAllBtn() {
        if (selectAllBtn == null) {
            selectAllBtn = new JButton(XhibitActions.getAction(xac, XhibitActions.EditSelectAll, this));
            selectAllBtn.setPreferredSize(new Dimension(150, 25));
        }
        return selectAllBtn;
    }

    /**
     * Lazy instantiate the Multiple Pleas button that open the Multiple Pleas
     * dialog for the selected rows in the table.
     * 
     * @return the MultiplePleas button.
     */
    private JButton getMultiplePleaBtn() {
        if (multiplePleaBtn == null) {
            multiplePleaBtn = new JButton(XhibitActions.getAction(xac, XhibitActions.MultiplePlea, this));
            multiplePleaBtn.setPreferredSize(new Dimension(150, 25));
        }
        return multiplePleaBtn;
    }

    public void stepValidate() throws CSRecoverableException {
        getArraignmentPanel().stepValidate();

        Collection<String> validationErrors = new ArrayList<String>();
        Collection<String> deleteConfirms = new ArrayList<String>();

        XHIBITTableModelInterface tableModel = (XHIBITTableModelInterface) getIndictmentTable().getModel();
        for (int i = 0; i < getIndictmentTable().getRowCount(); i++) {
            ResultsRowValue rrv = (ResultsRowValue) tableModel.getDataAt(i);
            PleaValue pv = rrv.getPleaValue();

            if (pv != null && pv.getRefPleaCode() != null) {
                // Validate alternate/lessor offence
                if (pv.isGuiltyOfLesser()) {
                    if (pv.getAltRefOffenceId() == null) {
                        validationErrors.add(buildNoAltOffenceError(rrv));
                    }
                }

                // Validate other text recorded
                if (containsCode(OTHER_TEXT_CODES, pv.getRefPleaCode())
                        && (pv.getOtherPleaText() == null || pv.getOtherPleaText().trim().length() == 0)) {
                    validationErrors.add(buildNoOtherTextError(rrv));
                }
            } else {
                if (rrv.getDeletePleaValue() != null)
                    deleteConfirms.add(buildConfirmDeleteMessage(rrv));
            }
        }

        if (!validationErrors.isEmpty()) {
            JOptionPane.showMessageDialog(XSwingUtilities.getWindowAncestor(this),
                    getConcatenatedString(validationErrors), ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                            "validate.title"), JOptionPane.ERROR_MESSAGE);
            throw new UserCancelException();
        }
    }

    private static final String[] ALTERNATE_LESSER_CODES = new String[] { "GAO", "GLO" };

    private static final String[] OTHER_TEXT_CODES = new String[] { "O" };

    private boolean containsCode(String[] codeList, String code) {
        return Arrays.binarySearch(codeList, code) >= 0;
    }

    private String buildNoAltOffenceError(ResultsRowValue rrv) {
        // Return string in the format:
        // Alternate/Lesser offence not recorded for indictment {0} count {1}
        return ResourceBundleHelper.getResource(XhibitBundles.Pleas, "validate.alternateOffenceNotRecorded",
                new String[] { rrv.getChargeSequenceNumber().toString(), rrv.getOffenceSequenceNumber().toString(),
                        rrv.getDefendantValue().getSurName() });
    }

    private String buildNoOtherTextError(ResultsRowValue rrv) {
        // Return string in the format:
        // Other text not entered for plea of Other on indictment {0} count {1}
        return ResourceBundleHelper.getResource(XhibitBundles.Pleas, "validate.plea.noOtherTextRecorded", new String[] {
                rrv.getChargeSequenceNumber().toString(), rrv.getOffenceSequenceNumber().toString(),
                rrv.getDefendantValue().getSurName() });
    }

    private String buildConfirmDeleteMessage(ResultsRowValue rrv) {
        // Return string in the format:
        // - on indictment {0} count {1}
        return ResourceBundleHelper.getResource(XhibitBundles.Pleas, "validate.confirmDeletePleaDetail", new String[] {
                rrv.getChargeSequenceNumber().toString(), rrv.getOffenceSequenceNumber().toString(),
                rrv.getDefendantValue().getSurName() });
    }

    private String getConcatenatedString(Collection list) {
        StringBuffer sb = new StringBuffer();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            String item = (String) iter.next();
            sb.append(item);
            sb.append("\n");
        }
        return sb.toString();
    }

}
