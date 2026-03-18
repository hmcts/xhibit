package uk.gov.courtservice.xhibit.client.results.appealresults;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import mseries.Calendar.MFieldListener;
import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.ResultsUtil;
import uk.gov.courtservice.xhibit.client.results.util.table.AdditionalInfoTableCellEditor;
import uk.gov.courtservice.xhibit.client.results.util.table.AdditionalInfoTableCellRenderer;
import uk.gov.courtservice.xhibit.client.util.IconFactory;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellRenderer;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

/**
 * <p>
 * Title: CriminalAppealPanel for Appeal results
 * </p>
 * <p>
 * Description: The panel to show criminal appeals on the appeal results screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra / Rakesh Lakhani
 * @version $Revision: 1.49 $
 */
public class CriminalAppealPanel extends XPanel implements AppealSaver {
    private static final String CASE_RESULT_AB = "AB";

    private static final String CASE_RESULT_REM = "REM";

    private static final int GENERAL_MAGISTRATES_TAB = 1;

    private static final Logger log = CSServices.getLogger(CriminalAppealPanel.class);

    private final XhibitApplicationController xac;

    private final AppealResultsModel appealResultsModel;

    private JTabbedPane tabbedPane = null;

    private JPanel topPanel = null;

    private XDatePanel resultsDatePanel = null;

    private XComboBox resultsComboBox = null;

    private XTable resultsTable = null;

    private XTable generalMagsResultsTable = null;

    private AppealResultsTableModel appealResultsTableModel = null;

    private DisposalAppealResultsTableModel disposalAppealResultsTableModel = null;

    private BorderedTextAreaPanel judgesCommentsPanel = null;
    
    private AdditionalInfoTableCellEditor addInfoCellEditor;

    private AppealResultsComboBoxEditor resultsComboCellEditor;

    private AppealResultsComboBoxEditor magsResultsComboCellEditor;

    private DocumentListener documentListener = null;

    private ItemListener itemListener = null;

    private ResultsRowValue caseCriminalRow = null;

    private RefSystemCodeBasicValue originalToSelect = null;

    private TableModelListener tableModelListener = null;

    private boolean resetCaseCombo = false;

    /**
     * Constructor creates the Criminal Appeal Panel
     * 
     * @param xac
     *            the main application controller.
     * @param rb
     *            the resource bundle for screen text.
     */
    public CriminalAppealPanel(XhibitApplicationController xac, AppealResultsModel appealResultsModel)
            throws CSRecoverableException {
        this.xac = xac;
        this.appealResultsModel = appealResultsModel;
        stepInitialise();
        setupPanel();
    }

    public void stepInitialise() throws CSRecoverableException {
        itemListener = new CaseResultItemListener();
        appealResultsModel.setRefData(AppealResultsHelper.getCaseCrimRefData());
        appealResultsModel.setAppealOffenceRefData(AppealResultsHelper.getCrimOffenceRefData());
        appealResultsModel.setAppealGeneralMagsRefData(AppealResultsHelper.getAppealGeneralMagsRefData());
    }

    public void stepActivate() {
        // remove the listeners
        getJudgesCommentsPanel().getTextArea().getDocument().removeDocumentListener(getDocumentListener());

        // Case Level Combo listener
        getResultsComboBox().removeItemListener(itemListener);

        getResultsTable().getModel().removeTableModelListener(getTableModelListener());
        getGeneralMagsResultsTable().getModel().removeTableModelListener(getTableModelListener());

        this.caseCriminalRow = appealResultsModel.getResultsHelper().getCaseResultsRowValue();

        if (caseCriminalRow != null) {
            RefSystemCodeBasicValue toSelect = AppealResultsHelper.getRefSystemCodeBasicValue(caseCriminalRow,
                    appealResultsModel.getRefData());
            getResultsDatePanel().setRequired(toSelect != null);
            toSelect = toSelect == null ? AppealResultsHelper.NO_RESULT_TYPE_SELECTED : toSelect;
            setOriginalResultType(toSelect);
            getResultsComboBox().setSelectedItem(toSelect);
            if (caseCriminalRow.getVerdictValue() != null && caseCriminalRow.getVerdictValue().getVerdictDate() != null) {
                getResultsDatePanel().setDate(caseCriminalRow.getVerdictValue().getVerdictDate());
            } else {
                getResultsDatePanel().setDate((java.util.Date) null);
            }
        }

        // Offence Level Results Data
        Collection offenceAppealResults = appealResultsModel.getResultsHelper().getResultsForCharge(
                ChargeTypes.CRIMINAL_APPEAL.getChargeType());
        if (offenceAppealResults != null) {
            ((XHIBITTableModelInterface) getResultsTable().getModel()).setData(offenceAppealResults.toArray());
        }

        // General Magistrates Disposals
        Collection generalMagsAppealResults = appealResultsModel.getResultsHelper().getResultsForCharge(
                ResultsHelper.CHARGETYPE_UNRELATED_MAGISTRATE);
        if (generalMagsAppealResults != null) {
            log.debug("generalMagsAppealResults size = " + generalMagsAppealResults.size());

            ((XHIBITTableModelInterface) getGeneralMagsResultsTable().getModel()).setData(generalMagsAppealResults
                    .toArray());
            if (generalMagsAppealResults.size() <= 0) {
                getTabbedPane().setEnabledAt(GENERAL_MAGISTRATES_TAB, false);
            }

        }

        // Set judges comments
        getJudgesCommentsPanel().getTextArea().setText(buildJudgesComments());

        // add the listeners
        getJudgesCommentsPanel().getTextArea().getDocument().addDocumentListener(getDocumentListener());

        // Case Level Combo listener
        getResultsComboBox().addItemListener(itemListener);

        getResultsTable().getModel().addTableModelListener(getTableModelListener());
        getGeneralMagsResultsTable().getModel().addTableModelListener(getTableModelListener());

        getResultsTable().repaint();
        setModified(false);
    }

    public void stepUpdateViewState() {
    }

    public void stepDeactivate() {
    }

    public void stepDeinitialise(boolean save) {
    }

    public void populateResultsSaveValue(ResultsSaveValue resultsSaveValue) throws CSValidationException,
            UserCancelException, CSRecoverableException {
        if (getResultsTable().isEditing()) {
            getResultsTable().getCellEditor().stopCellEditing();
            getResultsTable().clearSelection();
        }
        if (getGeneralMagsResultsTable().isEditing()) {
            getGeneralMagsResultsTable().getCellEditor().stopCellEditing();
            getGeneralMagsResultsTable().clearSelection();
        }
        stepValidate();
        stepDeactivate();
        stepDeinitialise(true);

        processCriminalAppeals(getJudgesCommentsPanel().getTextArea().getText(), resultsSaveValue);
    }

    /**
     * sets up this panel
     */
    private void setupPanel() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        setLayout(gbl);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.insets = XHIBITConstant.nonContainerInsets;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.7;
        add(getTopPanel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.3;
        add(getJudgesCommentsPanel(), gbc);
    }

    /**
     * sets up the case level result panel
     * 
     * @return the panel
     */
    private JPanel getTopPanel() {
        if (topPanel == null) {
            topPanel = new JPanel();
            topPanel.setBorder(new TitledBorder(getResource("criminal.resultsBorder")));

            GridBagLayout gbl = new GridBagLayout();
            topPanel.setLayout(gbl);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = XHIBITConstant.nonContainerInsets;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            gbc.gridwidth = 4;
            gbc.gridx = 0;
            gbc.gridy = 0;
            topPanel.add(new JLabel(getResource("criminal.case.instruction")), gbc);
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 2;
            topPanel.add(new JLabel(getResource("miscellaneous.resultsLabel")), gbc);
            gbc.weightx = 0.9;
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            topPanel.add(getResultsComboBox(), gbc);
            gbc.gridwidth = 1;
            gbc.weightx = 0.0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            topPanel.add(new JLabel(getResource("miscellaneous.resultsDateLabel")), gbc);
            gbc.weightx = 0.1;
            gbc.gridx = 3;
            gbc.gridy = 2;
            topPanel.add(getResultsDatePanel(), gbc);

            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = XHIBITConstant.nonContainerInsets;
            gbc.weightx = 0.0f;
            gbc.weighty = 0.0f;
            gbc.gridwidth = 4;

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0f;
            gbc.weighty = 1.0f;

            topPanel.add(getTabbedPane(), gbc);
        }

        return topPanel;
    }

    private JTabbedPane getTabbedPane() {
        // set tab selection
        if (tabbedPane == null) {
            tabbedPane = new JTabbedPane();
            String offenceResultsTabText = getResource("criminal.tab.offenceResults");
            String magsDisposalsResultsTabText = getResource("criminal.tab.magsDisposalsResults");

            tabbedPane.addTab(offenceResultsTabText, getOffencePanel());
            tabbedPane.addTab(magsDisposalsResultsTabText, getGeneralResultsPanel());
        }
        return tabbedPane;
    }

    private JPanel getOffencePanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = XHIBITConstant.nonContainerInsets;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        p.add(new JLabel(getResource("criminal.offence.instruction")), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        p.add(new JScrollPane(getResultsTable()), gbc);

        return p;
    }

    private JPanel getGeneralResultsPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = XHIBITConstant.nonContainerInsets;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        p.add(new JLabel(getResource("criminal.general.instruction")), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        p.add(new JScrollPane(getGeneralMagsResultsTable()), gbc);

        return p;
    }

    /**
     * gets the judges comments panel
     * 
     * @return the jusdges comments panel
     */
    public BorderedTextAreaPanel getJudgesCommentsPanel() {
        if (judgesCommentsPanel == null) {
            judgesCommentsPanel = new BorderedTextAreaPanel(getResource("criminal.judgesCommentsBorder"));

            Object[] params = new Object[] { String.valueOf(AppealResultsController.JUDGES_COMMENTS_MAX_SIZE) };
            judgesCommentsPanel.getTextArea().setToolTipText(getResource("judgesComments.tooltiptext", params));
        }
        return judgesCommentsPanel;
    }

    /**
     * gets the resultsDatePanel
     * 
     * @return the resultsDatePanel
     */
    public XDatePanel getResultsDatePanel() {
        if (resultsDatePanel == null) {
            resultsDatePanel = new XDatePanel(this);
            resultsDatePanel.setDateEditable(true);
            resultsDatePanel.getDateComponent().addMChangeListener(new MChangeListener() {
                public void valueChanged(MChangeEvent e) {
                    if (e.getType() == MChangeEvent.PULLDOWN_OPENED || e.getType() == MChangeEvent.PULLDOWN_CLOSED) {
                        // If the chooser is being opened or closed the
                        // date will not have
                        // changed.
                        return;
                    }
                    if (e.getValue() != null) {
                        processResultsDateEvent();
                    }
                }
            });
            resultsDatePanel.getDateComponent().addMFieldListener(new MFieldListener() {
                public void fieldEntered(FocusEvent fe) {
                }

                public void fieldExited(FocusEvent fe) {
                    if (!fe.isTemporary()) {
                        processResultsDateEvent();
                    }
                }
            });
        }
        return resultsDatePanel;
    }

    /**
     * gets the results combo box
     * 
     * @return the results combo box
     */
    private XComboBox getResultsComboBox() {
        if (resultsComboBox == null) {
            resultsComboBox = new XComboBox();
            resultsComboBox.setModel(new DefaultComboBoxModel(appealResultsModel.getRefData().toArray()));
            resultsComboBox.setRenderer(new RefAppResultComboBoxRenderer());
            resultsComboBox.setPreferredSize(new Dimension(0, XHIBITConstant.getLineHeight()));
        }

        return resultsComboBox;
    }

    /**
     * gets the criminal appeal results table
     * 
     * @return the results table
     */
    public XTable getResultsTable() {
        if (resultsTable == null) {
            appealResultsTableModel = getAppealResultsTableModel();

            resultsTable = XTableFactory.getInstance().createMultiLineTable(appealResultsTableModel);
            resultsTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

            SwingUtilities.invokeLater(new ColSize(resultsTable));
            resultsTable.setPreferredScrollableViewportSize(new Dimension(700, 400));
            resultsTable.getTableHeader().setReorderingAllowed(false);

            TableColumnModel tcm = resultsTable.getColumnModel();

            // Appeal result column
            TableColumn appealResult = tcm.getColumn(AppealResultsTableModel.COLUMN_APPEAL_RESULT);
            appealResult.setCellEditor(getResultsComboCellEditor());
            appealResult.setCellRenderer(new XDefaultComboBoxCellRenderer());

            // Additional Info column (currently used for Lesser Offence)
            TableColumn additionalInfoColumn = tcm.getColumn(AppealResultsTableModel.COLUMN_ADDITIONAL_INFO);
            additionalInfoColumn.setCellRenderer(new AdditionalInfoTableCellRenderer(xac, resultsTable
                    .getMultiLineHelper(), true));
            additionalInfoColumn.setCellEditor(getAddInfoCellEditor());
            // The following is to avoid the problem raised by PR 57373
            // The scrollpane withn the cell was being repainted with and
            // without the scrollbars
            // if the size of the text area matches the width of he scroll
            // bar.
            // Not ideal but prevents us from having to trap all the repaint
            // events
            additionalInfoColumn.setMinWidth(AdditionalInfoTableCellRenderer.COLUMN_ADDITIONAL_INFO_MIN_WIDTH);

            // Altered column
            TableColumn iconColumn = tcm.getColumn(AppealResultsTableModel.COLUMN_ALTERED);
            iconColumn.setCellRenderer(resultsTable.getDefaultRenderer(ImageIcon.class));
        }

        return resultsTable;
    }

    /**
     * gets the criminal appeal results table
     * 
     * @return the results table
     */
    public XTable getGeneralMagsResultsTable() {
        if (generalMagsResultsTable == null) {
            disposalAppealResultsTableModel = getDisposalAppealResultsTableModel();

            generalMagsResultsTable = XTableFactory.getInstance().createMultiLineTable(disposalAppealResultsTableModel);
            generalMagsResultsTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

            SwingUtilities.invokeLater(new ColSize(generalMagsResultsTable));
            generalMagsResultsTable.setPreferredScrollableViewportSize(new Dimension(700, 400));
            generalMagsResultsTable.getTableHeader().setReorderingAllowed(false);

            TableColumnModel tcm = generalMagsResultsTable.getColumnModel();

            // Appeal result column
            TableColumn appealResult = tcm.getColumn(DisposalAppealResultsTableModel.COLUMN_APPEAL_RESULT);
            appealResult.setCellEditor(getMagsResultsComboCellEditor());
            appealResult.setCellRenderer(new XDefaultComboBoxCellRenderer());

            // Altered column
            TableColumn iconColumn = tcm.getColumn(DisposalAppealResultsTableModel.COLUMN_ALTERED);
            iconColumn.setCellRenderer(resultsTable.getDefaultRenderer(ImageIcon.class));
        }

        return generalMagsResultsTable;
    }

    private void setOriginalResultType(RefSystemCodeBasicValue originalToSelect) {
        this.originalToSelect = originalToSelect;
    }

    /**
     * gets the results combo cell editor
     * 
     * @return the results combo cell editor
     */
    public AppealResultsComboBoxEditor getResultsComboCellEditor() {
        if (resultsComboCellEditor == null) {
            JComboBox jcb = new JComboBox();
            jcb.setModel(new DefaultComboBoxModel(appealResultsModel.getAppealOffenceRefData().toArray()));
            jcb.setRenderer(new RefAppResultComboBoxRenderer());
            resultsComboCellEditor = new AppealResultsComboBoxEditor(jcb, appealResultsModel.getAppealOffenceRefData());
        }
        return resultsComboCellEditor;
    }
    
    public AdditionalInfoTableCellEditor getAddInfoCellEditor()
    {
    	if(this.addInfoCellEditor==null)
		{
			addInfoCellEditor = new AdditionalInfoTableCellEditor(xac,
            resultsTable.getMultiLineHelper(), true);
		}
		
		return addInfoCellEditor;
    }

    /**
     * gets the general magistrates results combo cell editor
     * 
     * @return the results combo cell editor
     */
    public AppealResultsComboBoxEditor getMagsResultsComboCellEditor() {
        if (magsResultsComboCellEditor == null) {
            JComboBox jcb = new JComboBox();
            jcb.setModel(new DefaultComboBoxModel(appealResultsModel.getAppealGeneralMagsRefData().toArray()));
            jcb.setRenderer(new RefAppResultComboBoxRenderer());
            magsResultsComboCellEditor = new AppealResultsComboBoxEditor(jcb, appealResultsModel
                    .getAppealGeneralMagsRefData());
        }
        return magsResultsComboCellEditor;
    }

    /**
     * gets the appeal results table model
     * 
     * @return the appeal results table model
     */
    private AppealResultsTableModel getAppealResultsTableModel() {
        if (appealResultsTableModel == null) {
            appealResultsTableModel = new AppealResultsTableModel();
        }
        return appealResultsTableModel;
    }

    /**
     * gets the appeal results table model
     * 
     * @return the appeal results table model
     */
    private DisposalAppealResultsTableModel getDisposalAppealResultsTableModel() {
        if (disposalAppealResultsTableModel == null) {
            disposalAppealResultsTableModel = new DisposalAppealResultsTableModel();
        }
        return disposalAppealResultsTableModel;
    }

    public void stepValidate() throws CSValidationException, UserCancelException {
        ArrayList validationErrors = new ArrayList();
        
        
        if (caseCriminalRow.getAction() != ResultsRowValue.RESULT_DELETE
                && getResultsComboBox().getSelectedIndex() == 0) {
            throw new CSValidationException("criminal.validate.caseLevelNotRecorded", new String[] {},
                    "No case level appeal result recorded");
        }
        // Only validate date if not deleting results
        if (caseCriminalRow.getAction() != ResultsRowValue.RESULT_DELETE) {
            getResultsDatePanel().stepValidate();
            if (getResultsDatePanel().getDate().after(Calendar.getInstance())) {
                throw new CSValidationException("validation.date.maxinclusive", new String[] { XDateFormat.format(
                        getResultsDatePanel().getDate(), XDateFormat.DATEFORMAT) },
                        "The result date entered is in the future - not allowed");
            }
        }
        
        XHIBITTableModelInterface tableModel = (XHIBITTableModelInterface) getResultsTable().getModel();
        for (int i = 0; i < getResultsTable().getRowCount(); i++) {
            ResultsRowValue rrv = (ResultsRowValue) tableModel.getDataAt(i);
            VerdictValue vv = rrv.getVerdictValue();

            if (vv != null && vv.getRefAppResultId() != null) {
                // Validate alternate/lessor offence
                if (AppealResultsHelper.hasAlternateOffence(vv.getRefAppealOffenceCode())) {
                    if (vv.getAppLesserOffence() == null || vv.getAppLesserOffence().trim().equals("")) {
                        validationErrors.add(buildNoAltOffenceError(rrv));
                    }
                }
            }
        }

        if (!validationErrors.isEmpty()) {
            JOptionPane.showMessageDialog(XSwingUtilities.getWindowAncestor(this), AppealResultsHelper
                    .getConcatenatedString(validationErrors), getResource("criminal.validate.title"),
                    JOptionPane.ERROR_MESSAGE);
            throw new UserCancelException();
        }
    }

    private String buildNoAltOffenceError(ResultsRowValue rrv) {
        // Return string in the format:
        // Lesser offence not recorded for count {0}
        return getResource("criminal.validate.alternateOffenceNotRecorded", new String[] { rrv
                .getOffenceSequenceNumber().toString() });
    }

    /**
     * get the document listener for the judges text modifications
     * 
     * @return the document listener
     */
    public DocumentListener getDocumentListener() {
        if (documentListener == null) {

            documentListener = new DocumentAdapter() {
                public void insertUpdate(DocumentEvent de) {
                    modified();
                }

                public void removeUpdate(DocumentEvent de) {
                    modified();
                }
            };
        }
        return documentListener;
    }

    /**
     * Gets a Table Model Listener for criminal appeals.
     * 
     * @return the TableModelListener
     */
    private TableModelListener getTableModelListener() {
        if (tableModelListener == null) {
            tableModelListener = new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save button.
                	//TO DO: PUT THIS SOMEWHERE WERE IT CAN BE USED BY INDIVIDUAL TABLE ROWS
                	XHIBITTableModelInterface tableModel = (XHIBITTableModelInterface) getResultsTable().getModel();
                	
                	// Handle a change to the "Results" dropdown which is technically not part of the table
                	ResultsRowValue rrv = null;
                	if (resultsTable.getSelectedRow() >= 0) {
                		rrv = (ResultsRowValue) tableModel.getDataAt(resultsTable.getSelectedRow());
                	}
            		RefOffenceBasicValue refOffence =  (RefOffenceBasicValue)((AdditionalInfoTableCellEditor)getAddInfoCellEditor()).getRefOffence();
            		      
                    if((refOffence!= null) && (rrv != null)) {
                    	rrv.getVerdictValue().setAltRefOffenceId(refOffence.getId());  
                    }
                    modified();
                }
            };
        }
        return tableModelListener;
    }

    private void processResultsDateEvent() {
        try {
            java.util.Date resultDate = getResultsDatePanel().getDate() == null ? null : getResultsDatePanel()
                    .getDate().getTime();
            // if verdict value is null then resultDate will be set when
            // result is selected
            // and a new verdict value is created.
            VerdictValue verdictValue = caseCriminalRow.getVerdictValue();

            // Don't process if data has not changed.
            if (resultDate == null) {
                if (verdictValue == null)
                    return;
                if (verdictValue.getVerdictDate() == null)
                    return;
            } else if (verdictValue != null) {
                if (resultDate.equals(verdictValue.getVerdictDate()))
                    return;
            }

            if (verdictValue != null) {
                verdictValue.setVerdictDate(resultDate);
            }
            if (caseCriminalRow.getAction() == ResultsRowValue.RESULT_UNCHANGED) {
                caseCriminalRow.setAction(ResultsRowValue.RESULT_UPDATE);
            }
            enableControls();
        } catch (CSValidationException csve) {
            XHIBITErrorHandler.handleError(csve);
        }
    }

    /**
     * Set the selected item on the combo box to the item represented by the ref
     * system code.
     * 
     * @param refCodeId
     */
    private void resetComboBox(String refCode) {
        if (refCode == null) {
            getResultsComboBox().setSelectedIndex(0);
        } else {
            boolean found = false;
            ComboBoxModel model = getResultsComboBox().getModel();
            for (int i = 1; i < model.getSize() && !found; i++) {
                RefSystemCodeBasicValue bv = (RefSystemCodeBasicValue) model.getElementAt(i);
                if (bv.getCode().equals(refCode)) {
                    getResultsComboBox().setSelectedIndex(i);
                    found = true;
                }
            }
            if (!found)
                getResultsComboBox().setSelectedIndex(0);
        }
    }

    private void processItemSelection(ItemEvent ie) throws UserCancelException {
        final RefSystemCodeBasicValue rscbv = (RefSystemCodeBasicValue) ie.getItem();
        final String selectedCode = rscbv.getCode();
        if (selectedCode.equals("TO")) {
            JOptionPane.showMessageDialog(this, getResource("criminal.useCrestForResult"),
                    getResource("criminal.useCrestForResult.title"), JOptionPane.WARNING_MESSAGE);
            resetCaseCombo = true;
            resetComboBox(caseCriminalRow.getVerdictValue() == null ? null : caseCriminalRow.getVerdictValue()
                    .getRefVerdictCode());
            resetCaseCombo = false;
        } else {
            VerdictValue verdictValue = caseCriminalRow.getVerdictValue();
            if (verdictValue == null && !selectedCode.equals("")) {
                // Check if previous action was a Delete. If so we want to
                // restore previous verdictValue.
                if (caseCriminalRow.getAction() == ResultsRowValue.RESULT_DELETE) {
                    verdictValue = caseCriminalRow.getDeleteVerdictValue();
                    caseCriminalRow.setAction(ResultsRowValue.RESULT_UPDATE);
                    caseCriminalRow.setVerdictValue(verdictValue);
                } else {
                    // Adding new verdict
                    caseCriminalRow.setAction(ResultsRowValue.RESULT_ADD);
                    verdictValue = new VerdictValue();
                    verdictValue.setCaseId(xac.getApplicationCaseModel().getCaseId());
                    caseCriminalRow.setVerdictValue(verdictValue);

                    processResultsABandREM(selectedCode);
                }
            } else {
                // Check if row is being added but result is changed before
                // saving.
                if (caseCriminalRow.getAction() != ResultsRowValue.RESULT_ADD) {
                    // Updating existing result
                    caseCriminalRow.setAction(ResultsRowValue.RESULT_UPDATE);
                }
                // Check if case level selection affects the other appeal
                // results.
                processResultsABandREM(selectedCode);
            }

            getResultsDatePanel().setRequired(!selectedCode.equals(""));

            if (rscbv.getCode() != null && !rscbv.getCode().equals("")) {
                verdictValue.setRefVerdictId(rscbv.getId());
                verdictValue.setRefVerdictCode(rscbv.getCode());
                verdictValue.setRefVerdictDesc(rscbv.getDecode());

                if (verdictValue.getVerdictDate() == null && getResultsDatePanel().getDateComponent().getText() != null
                        && !getResultsDatePanel().getDateComponent().getText().equals("")) {
                    // Use the currently selected date
                    processResultsDateEvent();
                }
                enableControls();
            } else {
                if (originalToSelect.equals(AppealResultsHelper.NO_RESULT_TYPE_SELECTED)) {
                    caseCriminalRow.setAction(ResultsRowValue.RESULT_UNCHANGED);
                } else {
                    if (verdictValue != null) {
                        /**
                         * @todo This code needs review. This extra check id
                         *       required because this method is fired twice in
                         *       a row. Although verdictValue is set to null the
                         *       first time, this condition is bypassed and the
                         *       verdictValue is not null. The verdictId is
                         *       however null!!!
                         */
                        if (verdictValue.getXhbVerdictBasicValue().getVerdictId() != null) {
                            caseCriminalRow.setDeleteVerdictValue(verdictValue);
                            verdictValue = null;
                            getResultsDatePanel().setDate((java.util.Date) null);
                        }
                    }
                    caseCriminalRow.setAction(ResultsRowValue.RESULT_DELETE);
                }
                caseCriminalRow.setVerdictValue(null);
                enableControls();
            }
        }
    }

    /**
     * Set results to AB or REM if the case level is set to AB or REM
     * respectively.
     * 
     * @param selectedCode
     *            the new ly selected case level appeal result code.
     */
    private void processResultsABandREM(String selectedCode) throws UserCancelException {
        log.debug("processABandREMforOffences - BEGIN");
        log.debug("processABandREMforOffences - selectedCode = " + selectedCode);
        log.debug("processABandREMforOffences - originalToSelect.getCode() = " + originalToSelect.getCode());
        if (selectedCode.equals(CASE_RESULT_AB) || selectedCode.equals(CASE_RESULT_REM)) {
            log.debug("processABandREMforOffences Result is AB or REM");
            if (!resetCaseCombo) {
                RefAppResultBasicValue refAppResult = AppealResultsHelper.getRefAppResultBasicValueForCode(
                        selectedCode, AppealResultsHelper.getCrimOffenceRefData());

                // Offence Level Results Data
                Collection offenceAppealResults = appealResultsModel.getResultsHelper().getResultsForCharge(
                        ChargeTypes.CRIMINAL_APPEAL.getChargeType());
                if (offenceAppealResults != null) {
                    ResultsRowValue rrv;
                    Iterator iter = offenceAppealResults.iterator();
                    while (iter.hasNext()) {
                        rrv = ((ResultsRowValue) iter.next());
                        rrv.setOperationalAppealResult();
                        rrv.processAppealResult(refAppResult, false);
                    }
                    ((XHIBITTableModelInterface) getResultsTable().getModel()).setData(offenceAppealResults.toArray());
                }

                // Magistrates General Disposal level results.
                XHIBITTableModelInterface tableModel = (XHIBITTableModelInterface) getGeneralMagsResultsTable()
                        .getModel();
                ResultsRowValue rrv;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    rrv = (ResultsRowValue) tableModel.getDataAt(i);
                    rrv.setOperationalAppealResult();
                    rrv.processAppealResult(refAppResult, false);
                    rrv.linkResultToMagsGeneralDisposal(refAppResult);
                }
                tableModel.fireTableDataChanged();
            }
        } else if ((originalToSelect.getCode().equals(CASE_RESULT_AB) || originalToSelect.getCode().equals(
                CASE_RESULT_REM))
                && (caseCriminalRow.getVerdictValue() == null || (!selectedCode.equals(CASE_RESULT_AB) && !selectedCode
                        .equals(CASE_RESULT_REM)))) {
            log.debug("processABandREMforOffences in else if");
            if (!resetCaseCombo) {
                // Get hold of offence level data and set result code to same
                // Offence Level Results Data
                Collection offenceAppealResults = appealResultsModel.getResultsHelper().getResultsForCharge(
                        ChargeTypes.CRIMINAL_APPEAL.getChargeType());
                if (offenceAppealResults != null) {
                    ResultsRowValue rrv;
                    Iterator iter = offenceAppealResults.iterator();
                    while (iter.hasNext()) {
                        rrv = ((ResultsRowValue) iter.next());
                        rrv.processDeletedAppealResult(false);
                    }

                    ((XHIBITTableModelInterface) getResultsTable().getModel()).setData(offenceAppealResults.toArray());
                }

                XHIBITTableModelInterface tableModel = (XHIBITTableModelInterface) getGeneralMagsResultsTable()
                        .getModel();
                ResultsRowValue rrv;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    rrv = (ResultsRowValue) tableModel.getDataAt(i);
                    rrv.processDeletedAppealResult(false);
                }
                tableModel.fireTableDataChanged();
            }
        }
    }

    private void enableControls() {
        if (caseCriminalRow != null) {
            setModified(caseCriminalRow.getAction() != ResultsRowValue.RESULT_UNCHANGED);
        }
    }

    private void processCriminalAppeals(String judgesComments, ResultsSaveValue resultsSaveValue)
            throws UserCancelException, CSRecoverableException {
        // If the Case level appeal result is being deleted - all other appeal
        // results will be deleted as well.
        if (caseCriminalRow.getAction() == ResultsRowValue.RESULT_DELETE) {
            boolean rc = XMessageBox.alert(xac, getResource("criminal.delete.title"), true, XMessageBox.ICONQUESTION,
                    getResource("criminal.delete.message"), XDialog.YESNO, XDialog.DEFAULTNO);

            if (!rc)
                throw new UserCancelException();

            // Delete Judges comments first
            if (appealResultsModel.getResultsCompositeValue().getCaseAppReasonValues() != null) {
                resultsSaveValue.removeCaseAppReasonValues(appealResultsModel.getResultsCompositeValue()
                        .getCaseAppReasonValues(), caseCriminalRow.getCaseId(), caseCriminalRow.getCaseType(),
                        caseCriminalRow.getCaseNumber());
            }

            // Delete Offence level results 1st
            AppealResultsHelper.setDeleteFlags(appealResultsModel, (XHIBITTableModelInterface) getResultsTable()
                    .getModel(), resultsSaveValue, false);

            // Delete appeal results for Magistrates general disposals.
            AppealResultsHelper.setDeleteFlags(appealResultsModel,
                    (XHIBITTableModelInterface) getGeneralMagsResultsTable().getModel(), resultsSaveValue, true);

            // Process Case level row for deletion
            AppealResultsHelper.setAlteredFlag(caseCriminalRow, resultsSaveValue, appealResultsModel, false);
        } else {
            String[] judgesCommentsLines = ResultsUtil.getLines(judgesComments);
            // Check for empty lines
            if (ResultsUtil.checkForEmptyLines(judgesCommentsLines)) {
                int rc = JOptionPane.showConfirmDialog(null, getResource("criminal.emptylines.message"),
                        getResource("criminal.emptylines.title"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (rc == JOptionPane.NO_OPTION)
                    throw new UserCancelException();

                // Remove empty lines
                judgesCommentsLines = ResultsUtil.removeEmptyLines(judgesCommentsLines);
            }

            // Get delete confirmations for offence level appeal results.
            XTable crimTable = getResultsTable();
            XHIBITTableModelInterface crimModel = (XHIBITTableModelInterface) crimTable.getModel();
            ArrayList deleteConfirms = new ArrayList();
            for (int i = 0; i < crimTable.getRowCount(); i++) {
                ResultsRowValue rrv = (ResultsRowValue) crimModel.getDataAt(i);
                if (rrv.getDeleteVerdictValue() != null) {
                    deleteConfirms.add(buildConfirmDeleteMessage(rrv));
                }
            }

            // Get delete confirmations for magistrates general disposals
            // appeal results.
            final XTable magsTable = getGeneralMagsResultsTable();
            final XHIBITTableModelInterface magsTableModel = (XHIBITTableModelInterface) magsTable.getModel();
            for (int i = 0; i < magsTable.getRowCount(); i++) {
                ResultsRowValue rrv = (ResultsRowValue) magsTableModel.getDataAt(i);
                if (rrv.getDeleteVerdictValue() != null) {
                    deleteConfirms.add(buildConfirmGeneralMagsDeleteMessage(rrv));
                }
            }

            // Prompt to confirm deleted appeal results
            if (!deleteConfirms.isEmpty()) {
                int rc = JOptionPane.showConfirmDialog(XSwingUtilities.getWindowAncestor(this),
                        getResource("criminal.validate.confirmDeleteAppealMessage") + "\n"
                                + AppealResultsHelper.getConcatenatedString(deleteConfirms),
                        getResource("criminal.validate.confirmDeleteAppealTitle"), JOptionPane.YES_NO_OPTION);
                if (rc == JOptionPane.NO_OPTION)
                    throw new UserCancelException();

            }

            log.debug("resultsSaveValue = " + resultsSaveValue);

            // Process appeal results for Magistrates general disposals.
            AppealResultsHelper.setAlteredFlags((XHIBITTableModelInterface) getGeneralMagsResultsTable().getModel(),
                    resultsSaveValue, appealResultsModel, true);

            // Process Offence Level Rows
            AppealResultsHelper.setAlteredFlags((XHIBITTableModelInterface) getResultsTable().getModel(),
                    resultsSaveValue, appealResultsModel, false);

            // Process Case level row
            AppealResultsHelper.setAlteredFlag(caseCriminalRow, resultsSaveValue, judgesCommentsLines,
                    AppealResultsHelper.copyCaseAppReasonValues(appealResultsModel.getResultsCompositeValue()
                            .getCaseAppReasonValues()), appealResultsModel, false);
        }
    }

    private String buildDeleteMessage(Collection messages) {
        StringBuffer sb = new StringBuffer(200);
        sb.append(getResource("criminal.validate.confirmDeleteAppealMessage"));
        sb.append(System.getProperty("line.separator", "\n"));
        sb.append(AppealResultsHelper.getConcatenatedString(messages));
        return sb.toString();
    }

    /**
     * Builds StringBuffer of Judges comments retrieved from
     * ResultsCompositeValue
     */
    private String buildJudgesComments() {
        int caseAppReasonCount = appealResultsModel.getResultsCompositeValue().getCaseAppReasonValueCount();
        if (0 < caseAppReasonCount) {
            StringBuffer judgesComments = new StringBuffer();
            String appReason = appealResultsModel.getResultsCompositeValue().getCaseAppReasonValue(0).getAppReason();
            judgesComments.append(appReason != null ? appReason : "");
            for (int i = 1; i < caseAppReasonCount; i++) {
                appReason = appealResultsModel.getResultsCompositeValue().getCaseAppReasonValue(i).getAppReason();
                judgesComments.append('\n');
                judgesComments.append(appReason != null ? appReason : "");
            }
            return judgesComments.toString();
        }

        return "";
    }

    private String buildConfirmDeleteMessage(ResultsRowValue rrv) {
        // Return string in the format:
        // - count {0}
        return getResource("criminal.validate.confirmDeleteAppealDetail", new String[] { rrv.getOffenceSequenceNumber()
                .toString() });
    }

    private String buildConfirmGeneralMagsDeleteMessage(ResultsRowValue rrv) {
        final String msg = rrv.getDisposalReferenceValue().getDisposalText(rrv.getDisposalValue());
        return getResource("criminal.validate.confirmGeneralMagsDeleteAppealDetail", new String[] { msg });
    }

    /**
     * Gets a resource string from the Appeal Result bundle.
     * 
     * @param key
     *            the key to lookup in the resource bundle.
     * @return the resource string for the given key.
     */
    private String getResource(final String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AppealResults, key);
    }

    /**
     * Gets a resource string from the Appeal Result bundle.
     * 
     * @param key
     *            the key to lookup in the resource bundle.
     * @param params
     *            array of objects to insert into the resource string.
     * @return the resource string for the given key.
     */
    private String getResource(final String key, final Object[] params) {
        return ResourceBundleHelper.getResource(XhibitBundles.AppealResults, key, params);
    }

    private class CaseResultItemListener implements ItemListener {
        Object oldItem = null;

        public void itemStateChanged(ItemEvent ie) {
            if (ie.getStateChange() == ItemEvent.SELECTED && oldItem != ie.getItem()) {
                log.debug("\n\nitemStateChanged " + ie.getItem());
                log.debug("itemStateChanged " + ie.getStateChange());
                log.debug("itemStateChanged " + ie.paramString());
                log.debug("itemStateChanged " + getResultsComboBox().isPopupVisible());
                try {
                    processItemSelection(ie);
                    oldItem = ie.getItem();
                } catch (UserCancelException uce) {
                    // Do nothing as selection cancelled
                }
            }
        }
    }

    private class ColSize implements Runnable {
        private final XTable _table;

        public ColSize(final XTable table) {
            _table = table;
        }

        public void run() {
            Object[] longValues = new Object[] { "Defendant Name Surname", "Charge offence description",
                    XTableFactory.COLUMN_WIDTH_UNDEFINED, IconFactory.createTickIcon() };
            // int
            // width=XSwingUtilities.getUltimateFrameAncestor(_table).getSize().width
            // - 20;
            int width = _table.getSize().width;
            if (width <= 0)
                width = 700;
            _table.initColumnSizes(longValues, width);
        }
    }
}