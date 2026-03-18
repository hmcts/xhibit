package uk.gov.courtservice.xhibit.client.results.authorise;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.services.results.Results2ControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XSLTransformHelper;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.fopViewer.FopPanel;
import uk.gov.courtservice.xhibit.client.util.fopViewer.FopViewerHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.DefendantHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.CheckBoxTableCellEditor;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationFailurePrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationRequestValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthoriseWarning;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.CaseAuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantAuthorisationFailureValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantAuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantFailureReason;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantOnCaseAuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.FailureMessage;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani, Steve Tully
 * @version $Id: AuthoriseResultsPanel.java,v 1.16 2005/01/28 10:19:25 rzvddy
 *          Exp $
 * @history James Powell 13/03/2009 - Added a constructor and made modifications to allow
 * this dialog to be called from the new Unauthorised Case Status screen as well as
 * being called from the relevant action when the case is open.
 * @history Kelvin Davies 03/04/2009 - Added new 'Vulnerable/intimidated victim' checkbox 
 * and updated the Authorise resultsTable to include new 'Date Record Sheet Sent' as per CCN1263
 */

public class AuthoriseResultsPanel extends XPanel implements TableModelListener {

    private static final long serialVersionUID = 1L;

    private static final String AUTHORISATION_FAILED_MESSAGE = "results.authorise.authorisationFailed.message";
	private static final String AUTHORISATION_FAILED_TITLE = "results.authorise.authorisationFailed.title";
	private static final String CASE_CLOSED_REQUIRED_MSGKEY = "results.authorise.authorisationFailed.caseClosed.message";
    private static final String CASE_CLOSED_REQUIRED_TITLEKEY = "results.authorise.authorisationFailed.caseClosed.title";
        
    private static final Logger log = CSServices.getLogger(AuthoriseResultsPanel.class);

    private XhibitApplicationController xac;
    private XhbCaseBasicValue caseValue;
    private Integer scheduledHearingId;

    private AuthorisationValue[] allAuthorisableDefendants;

    private JScrollPane defendantsScrollPane;
    
    private JCheckBox vulnerableCheckBox;

    private AuthoriseWarning[] warnings;
    
    private Vector<AuthoriseResultsTableRowModel> defendantDetails = 
        new Vector<AuthoriseResultsTableRowModel>();

    private JTable defendantsTable;

    private AuthorisationReturnValue authReturnValues = null;

    private AuthoriseSyncAction authoriseSyncAction = null;
    
    private AuthorisePreviewAction authorisePreviewAction = null;
    
    private AuthoriseUpdateVictimIndicatorAction authoriseUpdateVictimIndicatorAction = null;
    
    private String[] colAmendedReasonCodes;
    
    private boolean initialised = false;
    
    private static final char CRIMINAL_APPEAL = 'C';

    private static final char BREACH = 'B';

    private static final char INDICTMENT = 'I';

    private static final char MISCELLANEOUS_APPEAL = 'M';

    private static final char SUMMARY_OFFENCE = 'O';

    private static final char COMMITAL_FOR_SENTENCE = 'S';

    private static final char ORIGINAL_CHARGE = 'G';
  
    private FopViewerHelper fopHelper = new FopViewerHelper();
    
    private AuthoriseResultsHelper authoriseResultsHelper = new AuthoriseResultsHelper();
    
    public AuthoriseResultsPanel(
            XhibitApplicationController xac, 
            AuthoriseWarning[] warnings) 
    throws CSRecoverableException {
        this.warnings = warnings;
        this.xac = xac;
        stepInitialise();
        jbInit();
    }
    
    public AuthoriseResultsPanel(
            AuthoriseWarning[] warnings,
            XhbCaseBasicValue caseValue,
            Integer scheduledHearingId) 
    throws CSRecoverableException {
        this.warnings = warnings;
        this.xac = null;
        this.caseValue = caseValue;
        this.scheduledHearingId = scheduledHearingId;
        stepInitialise();
        jbInit();
    }

    public void setAuthoriseSyncAction(AuthoriseSyncAction action) {
        authoriseSyncAction = action;
        stepUpdateViewState();
    }
    
    public void setAuthorisePreviewAction(AuthorisePreviewAction action) {
    	authorisePreviewAction = action;
   }
    
    public void setAuthoriseUpdateVictimIndicatorAction(AuthoriseUpdateVictimIndicatorAction action) {
        authoriseUpdateVictimIndicatorAction = action;
    }
    

    /**
     * Initialise GUI components
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());

        int row = 0;
        this.add(getDefendantsScrollPane(), getConstraints(row++));
        if (warnings != null && warnings.length > 0) {
            GridBagConstraints constraints = getConstraints(row++);
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.NONE;
            this.add(getLabel(), constraints);
        }
        this.add(getAuthFailurePane(), getConstraints(row++));
        this.add(getVulnerableCheckBox(), getConstraints(row++));
    }
    
    
    private JLabel getLabel() {
        return new JLabel(lookupResource("results.authorise.warningcolumn.description"));
    }
    
    
    private GridBagConstraints getConstraints(int gridy) {
        return new GridBagConstraints(0, gridy, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0);
    }
    

    private JCheckBox getVulnerableCheckBox(){
        if(vulnerableCheckBox == null){
            AuthoriseResultsHelper helperer = new AuthoriseResultsHelper();
            vulnerableCheckBox = new JCheckBox();
            
            vulnerableCheckBox.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent e) {
                    if (authoriseUpdateVictimIndicatorAction != null) {
                        authoriseUpdateVictimIndicatorAction.setEnabled(true);
                    }
                    if (authoriseSyncAction != null){
                        authoriseSyncAction.setEnabled(false);
                    }

                 }
            });
            
            vulnerableCheckBox.setText(lookupResource("results.authorise.VunerableCheckBoxLabel"));
            vulnerableCheckBox.setToolTipText(lookupResource("results.authorise.VunerableCheckBoxToolTip"));
            Integer caseId = (xac == null? caseValue.getCaseId() : xac.getApplicationCaseModel().getCaseId());
            vulnerableCheckBox.setSelected(helperer.isVulnerableVictim(caseId));

        }
        
        return vulnerableCheckBox;
    }
    
    
    /**
     * Get a list of items for the amended reason combo box
     * 
     * @return
     * @throws CSRecoverableException
     */
    private void getAmendedReasonCodes() throws CSRecoverableException {

        colAmendedReasonCodes = new String[9];
        colAmendedReasonCodes[0]=XHIBITConstant.getResource(XhibitBundles.AuthoriseResultsAmendedReasonResources, "Select");
        colAmendedReasonCodes[1]=XHIBITConstant.getResource(XhibitBundles.AuthoriseResultsAmendedReasonResources, "Incorrect_defendant_remand_time_provided_to_the_court_via_the_Prison");
        colAmendedReasonCodes[2]=XHIBITConstant.getResource(XhibitBundles.AuthoriseResultsAmendedReasonResources, "Judicial_amendments_via_slip_rule");
        colAmendedReasonCodes[3]=XHIBITConstant.getResource(XhibitBundles.AuthoriseResultsAmendedReasonResources, "POCAconfiscation_orders_made_after_sentence");
        colAmendedReasonCodes[4]=XHIBITConstant.getResource(XhibitBundles.AuthoriseResultsAmendedReasonResources, "Probation_application_to_amend_or_revoke_a_Community_Order_or_Suspended_Sentence_Order");
        colAmendedReasonCodes[5]=XHIBITConstant.getResource(XhibitBundles.AuthoriseResultsAmendedReasonResources, "Court_error");
        colAmendedReasonCodes[6]=XHIBITConstant.getResource(XhibitBundles.AuthoriseResultsAmendedReasonResources, "Bench_Warrant_Issued");
        colAmendedReasonCodes[7]=XHIBITConstant.getResource(XhibitBundles.AuthoriseResultsAmendedReasonResources, "Court_of_Appeal_Decision_added_to_Record_Sheet");
        colAmendedReasonCodes[8]=XHIBITConstant.getResource(XhibitBundles.AuthoriseResultsAmendedReasonResources, "Other");
    }
    
    /**
     * Description: Returns a boolean representation of the VulnerableVictimIndicator
     * @return Boolean
     */
    public boolean isVulnerableSelected(){
        boolean flag = false; 

        if (getVulnerableCheckBox().isSelected()){
            flag = true;
        }   
        
        return flag;
    }
    
    private JScrollPane getDefendantsScrollPane() {
        if (defendantsScrollPane == null) {
            defendantsScrollPane = new JScrollPane();
            defendantsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            defendantsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            defendantsScrollPane.getViewport().add(getDefendantsTable(), null);
            defendantsScrollPane.setPreferredSize(new Dimension(1100, XHIBITConstant.getLineHeight() * 7));
        }

        return defendantsScrollPane;
    }
    

    private JTable getDefendantsTable() {
        if (defendantsTable == null) {
            AuthoriseResultsTableModel artm = new AuthoriseResultsTableModel(this);
            defendantsTable = XTableFactory.getInstance().createMultiLineTable(artm);
   
            defendantsTable.getTableHeader().setReorderingAllowed(false);
            defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.SELECT).setCellRenderer(
                    new MyAuthCheckBoxTableCellRenderer());
            defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.SELECT).setCellEditor(
                    new CheckBoxTableCellEditor(new JCheckBox()));
            defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.RESELECT).setCellRenderer(
                    new MyReAuthCheckBoxTableCellRenderer());
            defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.RESELECT).setCellEditor(
                    new CheckBoxTableCellEditor(new JCheckBox()));
            
            defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.AMENDED_REASON).setCellEditor(
                    new MyComboBoxEditor(colAmendedReasonCodes));
            defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.AMENDED_REASON).setCellRenderer(
                    new MyComboBoxRenderer(colAmendedReasonCodes));
                    
            TableColumn column = null;
            column = defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.WARNING_FLAG);
            column.setPreferredWidth(10);
            column = defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.DEFENDANT_NAME);
            column.setPreferredWidth(300);
            column = defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.AUTHORISATION_STATE);
            column.setPreferredWidth(150);
            column = defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.DATE_RECORDSHEET_SENT);
            column.setPreferredWidth(250);
            column = defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.SELECT);
            column.setPreferredWidth(100);
            column = defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.RESELECT);
            column.setPreferredWidth(100);
            column = defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.AMENDED_REASON);
            column.setPreferredWidth(600);

            defendantsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionModel rowSM = defendantsTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                     // Ignore extra messages.
                    if (e.getValueIsAdjusting())
                        return;
                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if (!lsm.isSelectionEmpty()) {
                           stepUpdateViewState();
                     }
                   }
              });
             artm.addTableModelListener(this);
        }

        return defendantsTable;
    }
    
    class MyReAuthCheckBoxItemListener implements ItemListener {
        public void itemStateChanged(ItemEvent evt) {
            JCheckBox cb = (JCheckBox)evt.getSource();
            
            Object item = evt.getItem();
            
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                if (isAuthoriseCheckBoxSelected()) {
                    authoriseSyncAction.setEnabled(true);
                 }
            }
        }
    }
    
    class MyReAuthCheckBoxTableCellRenderer extends JCheckBox implements TableCellRenderer {
        public MyReAuthCheckBoxTableCellRenderer(){
            super();
            setHorizontalAlignment(JLabel.CENTER);
            MyReAuthCheckBoxItemListener itemListener = new MyReAuthCheckBoxItemListener();
            addItemListener(itemListener);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column){
            if(isSelected){
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            }else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setSelected(value != null && ((Boolean) value).booleanValue());
            
            if (!defendantsTable.isCellEditable(row, column)) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
            
            return this;
        }
    }
    
    class MyAuthCheckBoxItemListener implements ItemListener {
        public void itemStateChanged(ItemEvent evt) {
            JCheckBox cb = (JCheckBox)evt.getSource();
            
            Object item = evt.getItem();
            
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                if (isAuthoriseCheckBoxSelected()) {
                    authoriseSyncAction.setEnabled(true);
                }
            }
        }
    }
    
    
    class MyAuthCheckBoxTableCellRenderer extends JCheckBox implements TableCellRenderer {
        public MyAuthCheckBoxTableCellRenderer(){
            super();
            setHorizontalAlignment(JLabel.CENTER);
            MyAuthCheckBoxItemListener itemListener = new MyAuthCheckBoxItemListener();
            addItemListener(itemListener);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column){
            if(isSelected){
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            }else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setSelected(value != null && ((Boolean) value).booleanValue());
            
            if (!defendantsTable.isCellEditable(row, column)) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
            return this;
        }
    }
    
    
    class MyComboBoxListener implements ItemListener {
        public void itemStateChanged(ItemEvent evt) {
            JComboBox cb = (JComboBox)evt.getSource();
            
            Object item = evt.getItem();
        }
    }
    
    private boolean isAuthoriseCheckBoxSelected() {
        boolean authoriseCheckBoxSelected = false;
        if  (initialised) {
            authoriseCheckBoxSelected = true;
        }
        return authoriseCheckBoxSelected;
    }
    
    
    class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
        public MyComboBoxRenderer(String[] items){
            super(items);
            setSelectedIndex(0);
            addActionListener(this);
            MyComboBoxListener actionListener = new MyComboBoxListener();
            addItemListener(actionListener);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column){
            if(isSelected){
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            }else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setSelectedItem(value);
            
            if (!defendantsTable.isCellEditable(row, column)) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
            
            return this;
        }
    }
    
    class MyComboBoxEditor extends DefaultCellEditor {
        public MyComboBoxEditor(String[] items){
            super(new JComboBox(items));
        }
    }

    public void tableChanged(TableModelEvent tme) {
        stepUpdateViewState();
    }

    private FopPanel getAuthFailurePane() {
        FopPanel p = fopHelper.getDisplayPanel();
        p.showZoom(false);
        p.setVisible(false);
        return p;
    }

    /**
     * Life-cycle method to retrieve non-volatile data or data to be shown in
     * its initial state. In this instance, invoke a call to get a list of
     * defendants on the case and their authorisation state.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        getAmendedReasonCodes();
        refreshAuthorisationData();
    }

    /**
     * Retrieve the defendants on case and build a table model with the results.
     * 
     * @throws CSRecoverableException
     */
    private void refreshAuthorisationData() throws CSRecoverableException {
        Integer caseId = (xac == null? caseValue.getCaseId() : xac.getApplicationCaseModel().getCaseId());
        allAuthorisableDefendants = getResultsDelegate().getAuthorisable(caseId);

        defendantDetails = new Vector<AuthoriseResultsTableRowModel>();
        
        for (int i = 0; i < allAuthorisableDefendants.length; i++) {
            AuthoriseResultsTableRowModel item = new AuthoriseResultsTableRowModel();

            item.setWarningFlag("");
            for (AuthoriseWarning warning : warnings) {
                Integer id = allAuthorisableDefendants[i].getDefendant().getDefendantId();
                if (warning.getDefendantId().equals(id)) {
                    item.setWarningFlag("*");
                    break;
                }
            }
            
            item.setAuthorisationStatus(allAuthorisableDefendants[i].getResultsAuthorised());
            item.setAuthorisationValue(allAuthorisableDefendants[i]);
            item.setDateRecordSheetSent(authoriseResultsHelper.getExportDate(allAuthorisableDefendants[i].getResultsAuthorised(), 
                    allAuthorisableDefendants[i].getDefendant().getDefendantId(), caseId));
            item.setDefendantName(DefendantHelper.getDefendantFullName(allAuthorisableDefendants[i].getDefendant()));
            item.setSelected(new Boolean(false));
            item.setAmendedReason(colAmendedReasonCodes[0]);
            item.setPreviouslyAuthorised(authoriseResultsHelper.getPreviouslyExported(allAuthorisableDefendants[i].getResultsAuthorised(), 
                    allAuthorisableDefendants[i].getDefendant().getDefendantId(), caseId));
            item.setReSelected(new Boolean(false));
            if (allAuthorisableDefendants[i].getAmendedReason() == null) {
                initialised = false;
            } else {
                initialised = true;
            }
           
            if ((item.isSelected() != null) && (item.isSelected().booleanValue())) {
                initialised = true;
            }

            defendantDetails.add(item);
        }
    }
        
    /**
     * Life-cycle method executed when the screen is made visible. In this
     * instance, move details to the screen.
     */
    public void stepActivate() {
        moveDetailsToScreen();
        resetFopViewer();
    }

    private void resetFopViewer() {
        if (fopHelper.getDisplayPanel().isVisible()) {
            displayFopWindow(false);
        }
    }

    private void displayFopWindow(boolean visible) {
        fopHelper.getDisplayPanel().setVisible(visible);
        java.awt.Window parent = XSwingUtilities.getWindowAncestor(fopHelper.getDisplayPanel());
        parent.setSize(parent.getPreferredSize());
        parent.validate();
        parent.repaint();
    }

    /**
     * Pseudo life-cycle method to refresh the screen.
     */
    private void moveDetailsToScreen() {
        XHIBITTableModelInterface model = (XHIBITTableModelInterface) getDefendantsTable().getModel();
        int rowSelected = getDefendantsTable().getSelectedRow();
        model.setData(defendantDetails);
        defendantsTable.tableChanged(new TableModelEvent(model));
        
        if (rowSelected >= 0) {
        	defendantsTable.setRowSelectionInterval(rowSelected, rowSelected);
        }
    }

    /**
     * Life-cycle method to manage the enabled state of screen widgets
     */
    public void stepUpdateViewState() {    	
        boolean noSelectionMade = true;
         
        boolean isAnyCheckboxesChecked = false;
        boolean rowValidForEnablingAuthButton = true;
        ArrayList rowsValid = new ArrayList();
     
        Iterator it = ((AuthoriseResultsTableModel) getDefendantsTable().getModel()).getData().iterator();
        while (it.hasNext()) {
            rowValidForEnablingAuthButton = true;
            AuthoriseResultsTableRowModel row = (AuthoriseResultsTableRowModel) it.next();
            if ((row.isSelected() != null) && (row.isSelected().booleanValue())) {
                noSelectionMade = false;
                isAnyCheckboxesChecked = true;
             }
            
            if ((row.isReSelected()!= null) && (row.isReSelected().booleanValue())) {
                isAnyCheckboxesChecked = true;
                JComboBox jcb = (JComboBox) defendantsTable.getColumnModel().getColumn(AuthoriseResultsTableModel.AMENDED_REASON).getCellRenderer();
                if ((row.getAmendedReason() == null) || (row.getAmendedReason().equals(colAmendedReasonCodes[0]))) {
                    rowValidForEnablingAuthButton = false;
                }
                jcb.setEditable(true);
                if (jcb.getSelectedIndex() > 0) {
                    noSelectionMade = false;
                }
            }
            rowsValid.add(rowValidForEnablingAuthButton);
        }
        
        boolean finalCheck = true;
        for (int i=0; i<rowsValid.size(); i++) {
            if (((Boolean) rowsValid.get(i)).booleanValue() == false) {
                finalCheck = false;
            }
        }
        if ((isAnyCheckboxesChecked) && (finalCheck)) {
            initialised = true;
        } else {
            initialised = false;
        }

        if (authoriseSyncAction != null) {
            //This method check if Vulnerable Victim indicator changes have been applied.
            //If so, no results can be authorised
            if(authoriseUpdateVictimIndicatorAction != null){
                if(authoriseUpdateVictimIndicatorAction.isEnabled() == true){
                    noSelectionMade = true;
                }
                log.debug("Vulnerable Victim Indicator Flag: " + authoriseUpdateVictimIndicatorAction.isEnabled());
            }
            
            authoriseSyncAction.setEnabled(!noSelectionMade);
        }
        
        if (authorisePreviewAction != null) {
        	if (defendantsTable.getSelectedRow() != -1) {
        		authorisePreviewAction.setEnabled(true);
        		authorisePreviewAction.setDefendantsModel(defendantDetails);
        		authorisePreviewAction.setDefendantsTable(defendantsTable);
        		authorisePreviewAction.setCaseType(getCaseType());
        	}
        }
   
        if (isAuthoriseCheckBoxSelected()) {
            authoriseSyncAction.setEnabled(true);
            log.debug("Authorise button enabled");
        } else {
            authoriseSyncAction.setEnabled(false);
            log.debug("Authorise button disabled");
        }
    }

    

    /**
     * Life-cycle method to perform screen validation. In this instance, at
     * least one defendant must be selected for authorisation to take place.
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        boolean noSelectionMade = true;

        Iterator it = ((AuthoriseResultsTableModel) getDefendantsTable().getModel()).getData().iterator();
        while (it.hasNext()) {
            AuthoriseResultsTableRowModel row = (AuthoriseResultsTableRowModel) it.next();
            if (row.isSelected().booleanValue()) {
                noSelectionMade = false;
            }else if(row.isReSelected().booleanValue()) {
                noSelectionMade = false;
            }
        }
        if (noSelectionMade) {
            throw new CSValidationException("gui.authoriseResults.noSelectionMade",
                    "No defendants were selected for authorisation");
        }
    }

    /**
     * Life-cycle method executed when the screen is made invisible
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        resetFopViewer();
    }

    /**
     * Life-cycle method executed when an either the OK or Cancel button is
     * clicked. In this instance, if OK( Authorise ) is clicked, call authorise
     * on the selected defendants. NOTE: Do not perform any GUI actions in this
     * method
     * 
     * @param save -
     *            true if the OK button was clicked
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean save) throws CSRecoverableException {
    	if (save) {
            
            AuthorisationRequestValue request = new AuthorisationRequestValue();
            
            Integer caseId = (xac == null? caseValue.getCaseId() : xac.getApplicationCaseModel().getCaseId());
            request.setCaseType(xac == null ? caseValue.getCaseType() : xac.getApplicationCaseModel().getCaseType());
            request.setCaseId(caseId);
            request.setCourtLogDate(Calendar.getInstance(Locale.getDefault()));

            Vector<AuthorisationValue> temp = new Vector<AuthorisationValue>();
            for (int x = 0; x < defendantDetails.size(); x++) {
                AuthoriseResultsTableRowModel row = defendantDetails.get(x);

                if (row.isSelected().booleanValue()) {
                    temp.add(row.getAuthorisationValue());
                }else if(row.isReSelected().booleanValue()) {
                    row.getAuthorisationValue().setAmendedReason(row.getAmendedReason());
                    temp.add(row.getAuthorisationValue());
                }
            }

            AuthorisationValue[] authorisationValueArray = new AuthorisationValue[temp.size()];
            for (int i = 0; i < temp.size(); i++) {
                authorisationValueArray[i] = temp.get(i);
            }
            request.setDefendantsToAuthorise(authorisationValueArray);

            request.setCourtLogDate(Calendar.getInstance());
            
            Integer shid = (xac == null? scheduledHearingId : xac.getApplicationCaseModel().getScheduledHearingId());
            
            authReturnValues = getResultsDelegate().authoriseResults(request, shid);
        }
    }
    
    /**
     * Report back to the user the success or failure of their request. If there
     * has been either a case level or defendant level authorisation failure,
     * provide the users with an opportunity to view the reasons
     */
    public void postStepDeinitialise() throws CSRecoverableException, FOPException {
    	AuthorisationFailurePrintValue printValue = new AuthorisationFailurePrintValue();
        printValue.setCaseTypeAndNumber(getCaseTypeAndNumber());
        
        
        boolean caseFailure = debugCaseLevel(authReturnValues.getCaseAuthorisationReturnValue(), printValue);
        boolean defendantFailure = debugOffenceLevel(authReturnValues.getDefendantAuthorisationReturnValue(),
                printValue);
        boolean defendantOnCaseFailure = debugDefendantLevel(authReturnValues.getDefOnCaseAuthorisationReturnValue(),
                printValue);
        
        if (caseFailure || defendantFailure || defendantOnCaseFailure) {
            if (isCaseClosedRequired(authReturnValues.getCaseAuthorisationReturnValue())) {
            	// Report that a case closed event is required
            	JOptionPane.showMessageDialog(this, lookupResource(CASE_CLOSED_REQUIRED_MSGKEY),
            			lookupResource(CASE_CLOSED_REQUIRED_TITLEKEY), JOptionPane.ERROR_MESSAGE);
            	return;
            } 
	        JOptionPane.showMessageDialog(this, lookupResource(AUTHORISATION_FAILED_MESSAGE),
	        		lookupResource(AUTHORISATION_FAILED_TITLE), JOptionPane.ERROR_MESSAGE);
            if(!processD20OffenceLinkFailure()){
                return;
            }

            XSLTransformHelper xslt = new XSLTransformHelper(Locale.getDefault());

            fopHelper.showFop(xslt.transform(printValue, "results/authorise/printAuthorisationFailures"));
            displayFopWindow(true);
        } else {
            if(!processD20OffenceLinkFailure()){
                return;
            }
        }
        

        refreshAuthorisationData();
        moveDetailsToScreen();
    }

    private boolean processD20OffenceLinkFailure() {
    	if(authReturnValues.getOffenceLink().getCaseFailures()!=null 
    			&& authReturnValues.getOffenceLink().getCaseFailures().length!=0){
    		
    		String[] titleAndMessage = getFinalD20ErorMessage(authReturnValues.getOffenceLink());
    		
    		Object[] options = { "OK", "Cancel" };
    		int response = JOptionPane.showOptionDialog(null, titleAndMessage[1],
    				titleAndMessage[0], JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,null,options,options[0]);
    		 return response == JOptionPane.OK_OPTION;
    	}
		return true;
		
	}

	private String[] getFinalD20ErorMessage(D20OffenceLinkReturnValue link) {
		String errorMessage = "", title = "";
		for(String str : link.getCaseFailures()){
			if (str.equals("results.authorise.d20.offenceLinkError")) {
				if (link.getDefendantAuthoriseCount() > 1) {
					errorMessage = lookupResource("results.authorise.d20.offenceLinkErrorMulti") + ".\n" + errorMessage;
				} else {
					errorMessage = lookupResource(str) + ".\n" + errorMessage;
				}
			} else if (str.equals("results.authorise.d20.offenceLinkFinalD20DateError") && link.getFinalD20Date()!=null) {
				errorMessage += lookupResource(str)  +  " [" +link.getFinalD20Date()+ "]" + "\n\n";
				title = lookupResource("results.authorise.d20.confirmD20Title");
			} else{
				errorMessage += lookupResource(str) + "\n";
			}
		}
		
		if (title.length() == 0) {
			title = lookupResource("results.authorise.d20.offenceLinkErrorTitle");
		}
		
		if(!errorMessage.isEmpty()){
			errorMessage+=lookupResource("results.authorise.d20.offenceLinkError1") + "\n" ;
			errorMessage+=lookupResource("results.authorise.d20.offenceLinkError2");
		}
		
		// Build the 2 element array to send back
		String [] retArr = {title, errorMessage};
		return retArr;
	}


	/**
     * Return the "case number" from the XAC.
     * 
     * @return String representing a concatenation of the case type an case
     *         number
     */
    private String getCaseTypeAndNumber() {
        String caseType = getCaseType();
        Integer caseNumber = (xac == null? caseValue.getCaseNumber() : xac.getApplicationCaseModel().getCaseNumber());
        StringBuffer buf = new StringBuffer(caseType);
        buf.append(caseNumber.toString());

        return buf.toString();
    }

	private String getCaseType() {
		String caseType = (xac == null? caseValue.getCaseType() : xac.getApplicationCaseModel().getCaseType());
		return caseType;
	}

    /**
     * Check for case level failures and populate the print value the failure
     * reasons
     * 
     * @param ret -
     *            the returned case authorisation value
     * @param printValue -
     *            the value object to contain the data to be FOPped
     * @return true if the case failed authorisation
     */
    private boolean debugCaseLevel(CaseAuthorisationReturnValue ret, AuthorisationFailurePrintValue printValue) {
        Vector<String> reasons = new Vector<String>();

        printValue.setCaseFailure(ret.hasCaseLevelFailed());
        if (ret.hasCaseLevelFailed()) {
            String[] failures = ret.getCaseFailures();
            for (int i = 0; i < failures.length; i++) {
                reasons.add(lookupResource(failures[i]));
            }
        }

        printValue.setCaseFailureReasons(reasons);

        return ret.hasCaseLevelFailed();
    }
    
    private boolean isCaseClosedRequired(CaseAuthorisationReturnValue ret) {
    	return ret.hasCaseLevelFailed() &&
    		isMessageKeyInFailures(ret.getCaseFailures(), CASE_CLOSED_REQUIRED_MSGKEY);
    }
    
    private boolean isMessageKeyInFailures(String[] failuresArray, String messageKey) {
    	List<String> failures = failuresArray != null ? Arrays.asList(failuresArray) : null;
		return failures != null && failures.contains(messageKey);
    }

    /**
     * Check for defendant level authorisation failures and populate the print
     * value with those defendants that failed
     * 
     * @param allDefs
     * @param printValue
     * @return true if there was at least one defendant authorisation failure
     */
    private boolean debugOffenceLevel(DefendantAuthorisationReturnValue[] allDefs,
            AuthorisationFailurePrintValue printValue) {
        boolean resultCode = false;
        Vector<DefendantPrintValue> defendantPrintValues = new Vector<DefendantPrintValue>();

        Arrays.sort(allDefs, DefendantComparator.getInstance());
        for (int i = 0; i < allDefs.length; i++) {
            DefendantAuthorisationReturnValue ret = allDefs[i];
            DefendantPrintValue thisDefendantPrintValue = new DefendantPrintValue();
            thisDefendantPrintValue.setDefendantFullName(DefendantHelper.getDefendantFullName(ret.getDefendant()));

            thisDefendantPrintValue.setDefendantFailure(ret.hasDefendantFailed());
            if (ret.hasDefendantFailed()) {
                resultCode = true;

                Vector<DefendantFailureReason> thisDefendantReasons = new Vector<DefendantFailureReason>();
                DefendantAuthorisationFailureValue[] failures = ret.getFailures();
                Sorter.sort(failures, new String[] { "chargeTypeSort", "chargeSequence", "offenceSequence" },
                        Sorter.ASCENDING);

                for (int f = 0; f < failures.length; f++) {
                    DefendantAuthorisationFailureValue failureValue = failures[f];
                    DefendantFailureReason thisReason = new DefendantFailureReason();

                    if (failureValue.getCharge() == null) {
                        thisReason.setColumn01(decodeChargeType("UNRELATED"));
                    } else {
                        thisReason.setColumn01(decodeChargeType(failureValue.getCharge().getChargeType()));
                    }
                    thisReason.setColumn02(lookupResource(failureValue.getReasonCode()));
                    thisReason.setColumn03(buildMessageForChargeType(failureValue));

                    thisDefendantReasons.add(thisReason);
                }
                thisDefendantPrintValue.setDefendantFailureReasons(thisDefendantReasons);

                defendantPrintValues.add(thisDefendantPrintValue);
            }
        }

        printValue.setDefendantPrintValue(defendantPrintValues);

        return resultCode;
    }

    /**
     * Check for case level failures and populate the print value the failure
     * reasons
     * 
     * @param ret -
     *            the returned case authorisation value
     * @param printValue -
     *            the value object to contain the data to be FOPped
     * @return true if the case failed authorisation
     */
    private boolean debugDefendantLevel(DefendantOnCaseAuthorisationReturnValue[] allDefs,
            AuthorisationFailurePrintValue printValue) {
        boolean resultCode = false;
        final Vector<DefendantPrintValue> defendantPrintValues = new Vector<DefendantPrintValue>();

        for (int i = 0; i < allDefs.length; i++) {
            DefendantOnCaseAuthorisationReturnValue ret = allDefs[i];
            DefendantPrintValue thisDefendantPrintValue = new DefendantPrintValue();
            thisDefendantPrintValue.setDefendantFullName(DefendantHelper.getDefendantFullName(ret.getDefendant()));

            thisDefendantPrintValue.setDefendantFailure(ret.hasDefendantFailed());
            if (ret.hasDefendantFailed()) {
                
                resultCode = true;
                Vector<DefendantFailureReason> defendantReasons = new Vector<DefendantFailureReason>();
                FailureMessage[] failures = ret.getFailures();
                for (int f = 0; f < failures.length; f++) {
                    DefendantFailureReason reason = new DefendantFailureReason();
                    reason.setColumn01(lookupResource(failures[f].getFailureKey()));
                    if (failures[f].hasFailureDetailsParameters()) {
                        reason.setColumn02(lookupResource(failures[f].getFailureDetailsKey(), failures[f]
                                .getFailureDetailsParameters()));
                    } else {
                        reason.setColumn02(lookupResource(failures[f].getFailureDetailsKey()));
                    }
                    defendantReasons.add(reason);
                }
                thisDefendantPrintValue.setDefendantFailureReasons(defendantReasons);
                defendantPrintValues.add(thisDefendantPrintValue);
            }
        }
        printValue.setDefendantOnCasePrintValue(defendantPrintValues);
        printValue.setDefOnCaseFailure(resultCode);
        return resultCode;
    }

    private Results2ControllerBeanBusinessDelegate getResultsDelegate() {
        return XhibitDelegateHelper.getResults2Delegate();
    }

    private String lookupResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, key);
    }

    private String lookupResource(String key, Object[] parameters) {
        return ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, key, parameters);
    }

    private String decodeChargeType(String param) {
        StringBuffer buf = new StringBuffer("results.authorise.chargeType.");
        buf.append(param);
        return lookupResource(buf.toString());
    }

    /**
     * Build a description/details message depending on the charge type
     * 
     * @param failureValue
     * @return String containing the message to be shown
     */
    private String buildMessageForChargeType(DefendantAuthorisationFailureValue failureValue) {
        StringBuffer buf = new StringBuffer();

        if (failureValue.getCharge() == null) {
            buf.append(lookupResource("results.authorise.printValue.magistrate"));
        } else {
            switch (failureValue.getCharge().getChargeType().charAt(0)) {
            case BREACH:
                buf.append(lookupResource("results.authorise.printValue.breach"));
                buf.append(" ");
                buf.append(failureValue.getChargeSequence());
                buf.append("   ");
                buf.append(lookupResource("results.authorise.printValue.offence"));
                buf.append(" ");
                buf.append(failureValue.getOffenceSequence());
                buf.append("   - ");
                buf.append(failureValue.getCharge().getBreachValue().getHoDescription());
                break;
            case CRIMINAL_APPEAL:
                buf.append(lookupResource("results.authorise.printValue.offence"));
                buf.append(" ");
                buf.append(failureValue.getOffenceSequence());
                buf.append("   - ");
                buf.append(failureValue.getOffence().getOffenceDescription());
                break;
            case COMMITAL_FOR_SENTENCE:
                buf.append(lookupResource("results.authorise.printValue.commital"));
                buf.append(" ");
                buf.append(failureValue.getOffenceSequence());
                buf.append("   - ");
                buf.append(failureValue.getOffence().getOffenceDescription());
                break;
            case SUMMARY_OFFENCE:
                buf.append(lookupResource("results.authorise.printValue.offence"));
                buf.append(" ");
                buf.append(failureValue.getOffenceSequence());
                buf.append("   - ");
                buf.append(failureValue.getOffence().getOffenceDescription());
                break;
            case INDICTMENT:
                buf.append(lookupResource("results.authorise.printValue.indictment"));
                buf.append(" ");
                buf.append(failureValue.getChargeSequence());
                buf.append("   ");
                buf.append(lookupResource("results.authorise.printValue.count"));
                buf.append(" ");
                buf.append(failureValue.getOffenceSequence());
                buf.append("   - ");
                buf.append(failureValue.getOffence().getOffenceDescription());
                break;
            case MISCELLANEOUS_APPEAL:
            case ORIGINAL_CHARGE:
                buf.append(lookupResource("results.authorise.printValue.originalchargeforseqno"));
                buf.append("   - ");
                //Original Charge Desc is held in CrestOffenceFreeText
                buf.append(failureValue.getOffence().getCrestOffenceFreeText());
                break;
            default:
                break;
            }
        }

        return buf.toString();
    }
    
    /**
     * Description: This public method is called by the AuthoriseVulnerableVictimIndicatorAction
     *              and updates the case record to include the indicator value (true/false)
     *
     */
    public void updateCaseWithVulnerableVictimIndicator(){
        Integer caseId = (xac == null? caseValue.getCaseId() : xac.getApplicationCaseModel().getCaseId());
        authoriseResultsHelper.updateCase(caseId, isVulnerableSelected(),
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
        
        authoriseUpdateVictimIndicatorAction.setEnabled(false);            
            
    }
}