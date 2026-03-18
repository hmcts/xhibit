package uk.gov.courtservice.xhibit.client.results.verdicts;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import mseries.ui.MSimpleDateFormat;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.results.AssentingDissentingComboBoxModel;
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
import uk.gov.courtservice.xhibit.client.util.table.ScrollTableRowToView;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDateTableCellEditor;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDateTableCellRenderer;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellEditor;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellRenderer;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XNumericLimitedLengthCellEditor;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Show Verdicts Indictments Table
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
public class VerdictsIndictmentPanel extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	public static final String SCREEN_CHANGED = "SCREEN_CHANGED";
    private static final Logger log = Logger.getLogger(VerdictsIndictmentPanel.class);
    private final static int ASSENTING_DISSENTING_JURORS_INPUT_FIELD_WIDTH = 2;
    
    private XhibitApplicationController xac = null;
    private ConvictionDatePanel convictionDatePanel = null;
    
    private VerdictControllerModel model = null;
    private VerdictFilterSelectionModel verdictFilterSelectionModel = null;
    private VerdictFilterModel verdictFilterModel = null;
    private VerdictFilterPanel verdictFilterPanel = null;
    
    private JScrollPane scrollPane = null;
    private XTable xtable = null;
    
    private JComboBox verdictCb = null;
    private JComboBox assentingDissentingCb = null;
    private int[] oldRows;

    public VerdictsIndictmentPanel(VerdictControllerModel vcm) {
        try {
            this.model = vcm;
            stepInitialise();
            jbInit();
        } catch (Exception e) {
            XHIBITErrorHandler.handleError(e);
        }
    }

    public void stepInitialise() {
    	model.addPropertyChangeListener(this);
    	verdictFilterSelectionModel = model.getVerdictFilterSelectionModel();
    	verdictFilterModel = model.getVerdictFilterModel();
        xac = model.getACM().getXhibitApplicationController();
    }

    private void jbInit() throws Exception {
        this.setLayout(new GridBagLayout());

        this.add(getConvictionDatePanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        
        this.add(getVerdictFilterPanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        
        this.add(getScrollPane(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    }

    private ConvictionDatePanel getConvictionDatePanel() {
        if (convictionDatePanel == null) {
            convictionDatePanel = new ConvictionDatePanel(model);
        }
        return convictionDatePanel;
    }
    
    private VerdictFilterPanel getVerdictFilterPanel() {
    	if (verdictFilterPanel == null) {
    		verdictFilterPanel = new VerdictFilterPanel(verdictFilterSelectionModel, 
    				model, getIndictmentTable());
    	}
    	return verdictFilterPanel;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane(getIndictmentTable());
        }
        return scrollPane;
    }

    public XTable getIndictmentTable() {
        if (xtable == null) {
            //xtable = XTableFactory.getInstance().createMultiLineTable(model.getVerdictIndictmentTableModel());
        	xtable = XTableFactory.getInstance().createMultiLineTable(verdictFilterModel);
            xtable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            xtable.setColumnSelectionAllowed(true);
            xtable.setRowSelectionAllowed(true);
            Object[] longValues = new Object[] { "000", "                                   ",
                    "                                   ", "Verdict code", "Change of Verdict: Not guilty to guilty",
                    "assent/dessent",
                    // "dissent",
                    "Additional Information      ", "12-Apr-2003", "   " };
            log.debug("Table column count: " + xtable.getColumnModel().getColumnCount());
            log.debug("Long values size: " + longValues.length);
            xtable.initColumnSizes(longValues, 700);
            xtable.setPreferredScrollableViewportSize(new Dimension(700, 400));
            xtable.getTableHeader().setReorderingAllowed(false);

            TableColumnModel columnModel = xtable.getColumnModel();

            // Verdict code editor
            // RL: Verdict code editor not required as validiation moved to
            // setValueAt
            TableColumn verdictCodeColumn = columnModel.getColumn(VerdictIndictmentTableModel.COLUMN_VERDICT_CODE);
            verdictCodeColumn.setCellEditor(new DefaultCellEditor(new JTextField()));

            // Verdict description editor
            TableColumn verdictDescriptionColumn = columnModel
                    .getColumn(VerdictIndictmentTableModel.COLUMN_VERDICT_DESC);
            VerdictComboBoxEditor comboEditor = new VerdictComboBoxEditor(getVerdictCb(xtable), model
                    .getIndictmentVerdictRefData());
            verdictDescriptionColumn.setCellEditor(comboEditor);
            XDefaultComboBoxCellRenderer comboRenderer = new XDefaultComboBoxCellRenderer();
            verdictDescriptionColumn.setCellRenderer(comboRenderer);

            // Assenting/Dissenting jurors
            TableColumn assentingDissentingColumn = columnModel
                    .getColumn(VerdictIndictmentTableModel.COLUMN_JUROR_ASSENT_DISSENT);
            assentingDissentingColumn.setCellEditor(new XNumericLimitedLengthCellEditor(
                    ASSENTING_DISSENTING_JURORS_INPUT_FIELD_WIDTH));
            assentingDissentingColumn.setCellEditor(new XDefaultComboBoxCellEditor(getAssentingDissentingCb()));
            assentingDissentingColumn.setCellRenderer(new OptionalComboBoxCellRenderer());

            // DefaultTableCellRenderer assRenderer = new
            // DefaultTableCellRenderer();
            // assRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            // assentingDissentingColumn.setCellRenderer(assRenderer);

            // dissenting jurors
            // TableColumn dissentingColumn = columnModel.getColumn(
            // VerdictIndictmentTableModel.COLUMN_JUROR_ASSENT_DESSENT);
            // dissentingColumn.setCellEditor(new
            // XNumericLimitedLengthCellEditor
            // (ASSENTING_DISSENTING_JURORS_INPUT_FIELD_WIDTH));
            // DefaultTableCellRenderer disRenderer = new
            // DefaultTableCellRenderer();
            // disRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            // dissentingColumn.setCellRenderer(disRenderer);

            // Additional Info column
            TableColumn additionalInfoColumn = columnModel
                    .getColumn(VerdictIndictmentTableModel.COLUMN_ADDITIONAL_INFO);
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

            // Verdict Date Column
            TableColumn dateColumn = columnModel.getColumn(VerdictIndictmentTableModel.COLUMN_DATE);
            MSimpleDateFormat msdf = new MSimpleDateFormat(XDateFormat.simpleDateFormat);
            XDateTableCellEditor editor = new XDateTableCellEditor(msdf, this);
            editor.comp.setEditable(false);

            dateColumn.setCellEditor(editor);
            dateColumn.setCellRenderer(new XDateTableCellRenderer(xtable));

            // Altered Column
            // Use get column class to return ImageIcon so JTable auto uses
            // default image renderer
            TableColumn alteredColumn = columnModel.getColumn(VerdictIndictmentTableModel.COLUMN_ALTERED);
            alteredColumn.setCellRenderer(xtable.getDefaultRenderer(ImageIcon.class));

            ListSelectionModel rowSM = xtable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    actionListRowSelectionChanged(e);
                }
            });

            // The following listener is used to overcome a VERY strange
            // bug that on the first change to the table caused it to only
            // repaint
            // the first row. It only happens if the table has a scroll bar.
            xtable.getModel().addTableModelListener(new TableModelListener() {
                private boolean firstTime = true;

                public void tableChanged(TableModelEvent e) {
                    if (firstTime) {
                        firstTime = false;
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                xtable.validate();
                                xtable.revalidate();
                                xtable.repaint();
                            }
                        });
                        SwingUtilities.invokeLater(new ScrollTableRowToView(xtable, getScrollPane()));
                        xtable.getModel().removeTableModelListener(this);
                    }
                }
            });

            xtable.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent fe) {
                    try {
                        getConvictionDatePanel().stepValidate();
                    } catch (CSRecoverableException csve) {
                        XHIBITErrorHandler.handleError(csve);
                    }
                }

                public void focusLost(FocusEvent fe) {
                }
            });
        }
        return xtable;
    }

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
        log.debug("[VerdictsIndictmentPanel] rowSelectionChanged \noldRows = " + oldRows + "\nnewRows = " + newRows);
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
        	getVerdictFilterPanel().disableFilters();

            // Cannot use "enableSelectAll" of XPanel here as events are
            // being
            // trapped due to invalid/blank arraignment date.
            // enableSelectAll(false);
            XhibitActions.getAction(xac, XhibitActions.EditSelectAll).setEnabled(false);

            XhibitActions.getAction(xac, XhibitActions.MultiplePlea).setEnabled(false);
        } else {
            getVerdictFilterPanel().restoreFilters();

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

    private JComboBox getAssentingDissentingCb() {
        if (assentingDissentingCb == null) {
            assentingDissentingCb = new JComboBox(new AssentingDissentingComboBoxModel());
            assentingDissentingCb.setMaximumRowCount(assentingDissentingCb.getItemCount());
        }
        return assentingDissentingCb;
    }

    private JComboBox getVerdictCb(XTable vedictTable) {
        if (verdictCb == null) {
            DefaultComboBoxModel vcbModel = new DefaultComboBoxModel(VerdictRestrictionHelper.getInstance()
                    .getRestrictedVerdictList(""));
            verdictCb = new JComboBox(vcbModel);
        }
        return verdictCb;
    }

    public void stepValidate() throws CSRecoverableException {
        getConvictionDatePanel().stepValidate();

        Collection validationErrors = new ArrayList();
        Collection deleteConfirms = new ArrayList();

        XHIBITTableModelInterface tableModel = (XHIBITTableModelInterface) getIndictmentTable().getModel();
        for (int i = 0; i < getIndictmentTable().getRowCount(); i++) {
            ResultsRowValue rrv = (ResultsRowValue) tableModel.getDataAt(i);
            VerdictValue vv = rrv.getVerdictValue();

            if (vv != null && vv.getRefVerdictCode() != null) {
                // Validate alternate/lessor offence
                if (containsCode(ALTERNATE_LESSER_CODES, vv.getRefVerdictCode())) {
                    if (vv.getAltRefOffenceId() == null) {
                        validationErrors.add(buildNoAltOffenceError(rrv));
                    }
                }

                // Validate other text recorded
                if (containsCode(OTHER_TEXT_CODES, vv.getRefVerdictCode())
                        && (vv.getOtherVerdictText() == null || vv.getOtherVerdictText().trim().length() == 0)) {
                    validationErrors.add(buildNoOtherTextError(rrv));
                }

                // Validate Split recorded
                if (vv.hasAssentingDissenting()
                        && (vv.getJurorsAssenting() == null || vv.getJurorsDissenting() == null)) {
                    validationErrors.add(buildNoJurySplitError(rrv));
                }
            } else {
                if (rrv.getDeleteVerdictValue() != null)
                    deleteConfirms.add(buildConfirmDeleteMessage(rrv));
            }
        }

        if (!validationErrors.isEmpty()) {
            JOptionPane.showMessageDialog(XSwingUtilities.getWindowAncestor(this),
                    getConcatenatedString(validationErrors), ResourceBundleHelper.getResource(XhibitBundles.Verdicts,
                            "validate.title"), JOptionPane.ERROR_MESSAGE);
            throw new UserCancelException();
        }
    }

    private static final String[] ALTERNATE_LESSER_CODES = new String[] { "GA", "GAJ", "GAOJ", "GL", "GLJ", "GLOJ" };

    private static final String[] OTHER_TEXT_CODES = new String[] { "O" };
 
    private boolean containsCode(String[] codeList, String code) {
        return Arrays.binarySearch(codeList, code) >= 0;
    }

    private String buildNoAltOffenceError(ResultsRowValue rrv) {
        // Return string in the format:
        // Alternate/Lesser offence not recorded for indictment {0} count {1}
        return ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "validate.alternateOffenceNotRecorded",
                new String[] { rrv.getChargeSequenceNumber().toString(), rrv.getOffenceSequenceNumber().toString(),
                        rrv.getDefendantValue().getSurName() });
    }

    private String buildNoOtherTextError(ResultsRowValue rrv) {
        // Return string in the format:
        // Other text not entered for plea of Other on indictment {0} count {1}
        return ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "validate.verdict.noOtherTextRecorded",
                new String[] { rrv.getChargeSequenceNumber().toString(), rrv.getOffenceSequenceNumber().toString(),
                        rrv.getDefendantValue().getSurName() });
    }

    private String buildNoJurySplitError(ResultsRowValue rrv) {
        // Return string in the format:
        // Other text not entered for plea of Other on indictment {0} count {1}
        return ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "validate.verdict.noJurySplitRecorded",
                new String[] { rrv.getChargeSequenceNumber().toString(), rrv.getOffenceSequenceNumber().toString(),
                        rrv.getDefendantValue().getSurName() });
    }

    private String buildConfirmDeleteMessage(ResultsRowValue rrv) {
        // Return string in the format:
        // - on indictment {0} count {1}
        return ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "validate.confirmDeleteVerdictDetail",
                new String[] { rrv.getChargeSequenceNumber().toString(), rrv.getOffenceSequenceNumber().toString(),
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