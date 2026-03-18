package uk.gov.courtservice.xhibit.client.originalcharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.charge.OriginalChargeInterface;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.ChargeVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantOnCaseVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.OriginalChargeVO;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefOffenceCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.hearingrecord.EditDefendantAction;
import uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAmendDefendantAction;
import uk.gov.courtservice.xhibit.client.actions.updatecase.UpdateDefendantModel;
import uk.gov.courtservice.xhibit.client.courtlog.CheckBoxTableCellRenderer;
import uk.gov.courtservice.xhibit.client.originalcharges.maintainoriginalcharge.MaintainOriginalChargeDialog;
import uk.gov.courtservice.xhibit.client.originalcharges.maintainoriginalcharge.MaintainOriginalChargeModel;
import uk.gov.courtservice.xhibit.client.originalcharges.tables.ChargesTableModel;
import uk.gov.courtservice.xhibit.client.originalcharges.tables.ChargesTableRowModel;
import uk.gov.courtservice.xhibit.client.originalcharges.tables.DefendantsTableModel;
import uk.gov.courtservice.xhibit.client.originalcharges.tables.DefendantsTableRowModel;
import uk.gov.courtservice.xhibit.client.originalcharges.tables.OriginalChargesTableRowModel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.Refreshable;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.helpers.SeqNoHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/** 
 * <p>Title: XHIBIT</p>
 * <p>Description: When an XHIBIT user elects to export results for a defendant,
 * the system verifies that there is an indictment for the defendant in question.
 * 
 * Where there is none, the user is prevented from completing the function until
 * such time as this is remedied.
 * 
 * The Original Charges task allows the user to enter Magistrate’s charges for a 
 * defendant and so satisfy this requirement.
 * 
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: EDS</p>
 * 
 * @author Stephen Tully
 */
public class OriginalChargesPanel extends XPanel implements Refreshable, OriginalChargeInterface {
    
    // Parameters passed to the constructor
    private XDialog parent;
    private OriginalChargesModel model;
    
    // Screen widgets
    private JScrollPane defendantsScrollPane = null;
    private XTable defendantsTable = null;
    private JPopupMenu defendantsTablePopup = null;
    private XAction editAction = null;
    private JButton showAllBtn = null;
    private JScrollPane chargesScrollPane = null;
    private XTable chargesTable = null;
    private JButton addBtn = null;
    private JButton editBtn = null;
    private JButton deleteBtn = null;
    private OkCancelPanel okCancelPanel;
    
    // Data obtained during step initialisation
    private DefendantChargesCompositeVO[] defendantChargesCompositeVOArray;
    private RefOffenceBasicValue          refOffenceBasicValue;
    private HashMap<Integer,List>defOnCaseSeqNosMap;

    // Data used for screen initialisation
    private ArrayList<DefendantsTableRowModel> defendantData   = new ArrayList<DefendantsTableRowModel>();
    private ArrayList<ChargesTableRowModel>    chargeData      = new ArrayList<ChargesTableRowModel>();

    /**
     * Constructor
     * @param parent - the XDialog in which this panel sits
     * @param model - the model used to store scratchpad data
     * @throws CSRecoverableException
     */
    public OriginalChargesPanel(XDialog parent, OriginalChargesModel model)
    throws CSRecoverableException
    {
        super();
        this.model = model;
        this.parent = parent;
        this.okCancelPanel = (OkCancelPanel)parent.getButtonPanel();

        stepInitialise();
        init();
    }

    /**
     * Life-cycle method called in the constructor.
     * Obtains DefendantOnCase data and related original charge data from the mid-tier.
     */
    public void stepInitialise() throws CSRecoverableException {
        RefOffenceCriteria criteria = new RefOffenceCriteria();
        criteria.setCourtId(Integer.toString(XhibitSingleton.getInstance().getCourtId().intValue()));
        criteria.setOffenceCode(ORIGINAL_CHARGE_OFFENCE_CODE);
        
        Collection refOffences = XhibitDelegateHelper.getBizRefDelegate().findOffences(criteria);
        Iterator iter = refOffences.iterator();
        refOffenceBasicValue = (RefOffenceBasicValue)iter.next();

        Integer caseId = model.getXac().getApplicationCaseModel().getCaseId();
        
        defendantChargesCompositeVOArray = XhibitDelegateHelper.getChargeDelegate().
            getDefendantChargesByCaseId(
                    caseId,
                ChargeTypes.ORIGINAL_CHARGE.getChargeType()
            );
        
        //Get charges, and construct sequenceNumber Hashmap
        ChargeCompositeValue ccv = null;
        ccv = XhibitDelegateHelper.getChargeDelegate().getCharges(caseId, true);
        Collection charges = null;
        
        try{
            charges = XhibitDelegateHelper.getChargeDelegate().getChargesList(caseId);
        }catch (Exception e){
            throw new CSRecoverableException("gui.OriginalChargesAction.reloadDefOnCaseSeqNosMap",
                    "Exception whilst getting the charge composite value object from the mid tier", e);
        }
        
        //Create a hashmap with an empty list for each deft
        defOnCaseSeqNosMap = SeqNoHelper.createSequenceNosMap(ccv.getAllDefendants());
        
        Iterator iterator = charges.iterator();
        while (iterator.hasNext()) {
            //For each charge, process it... 
            ChargeValue chargeValue = (ChargeValue) iterator.next();
            SeqNoHelper.processSequenceNos(chargeValue,defOnCaseSeqNosMap);
        }
        
        model.setDefsWithIndictmentsList(createDefsWithIndictmentsList(charges));
        
    }
    
    private List createDefsWithIndictmentsList(Collection charges){
        List defsWithIndictments = new ArrayList<Integer>();
        
        if (charges == null)
            return defsWithIndictments;
        
        Iterator chargeIt = charges.iterator();
        while(chargeIt.hasNext()){
            //For every indictment on the case
            ChargeValue charge = (ChargeValue) chargeIt.next();
            if(charge.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())){
                Collection offences = charge.getOffenceValues();
                Iterator offenceIt = offences.iterator();
                while(offenceIt.hasNext()){
                    //For every count within the indictment
                    OffenceValue offence = (OffenceValue)offenceIt.next();
                    Collection defendants = offence.getDefendantValues();
                    Iterator defIt = defendants.iterator();
                    while(defIt.hasNext()){
                        //For every defendant on the count
                        DefendantValue indDef = (DefendantValue)defIt.next();
                        //id the defendant ID is not already in the list, then add it.
                        if(!defsWithIndictments.contains(indDef.getDefOnCaseBasicValue().getDefendantID())){
                            defsWithIndictments.add(indDef.getDefOnCaseBasicValue().getDefendantID());
                        }
                    }
                }
            }
        }
        
        return defsWithIndictments;
    }
    
    /**
     * Build the screen adding all widgets to the main panel 
     */
    private void init() {
        this.setLayout(new GridBagLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        
        // Defendants widgets
        mainPanel.add(
            new JLabel(OriginalChargesHelper.getResource("defendantsLbl")),
            new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE, 
                XHIBITConstant.nonContainerInsets, 0, 0));
        
        mainPanel.add(
            getDefendantsScrollPane(), 
            new GridBagConstraints(
                1, 0, 2, 1, 1.0, 1.0, 
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, 
                XHIBITConstant.nonContainerInsets, 0, 0));

        // Separator        
        mainPanel.add(
            OriginalChargesHelper.getSeparator(),
            new GridBagConstraints(
                0, 1, 3, 1, 1.0, 0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, 
                XHIBITConstant.containerInsets, 0, 0));
        
        // Charges widgets
        mainPanel.add(
            getShowAllBtn(),
            new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, 
                XHIBITConstant.nonContainerInsets, 0, 0));
            
        mainPanel.add(
            getChargesScrollPane(), 
            new GridBagConstraints(
                1, 2, 1, 1, 1.0, 1.0, 
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, 
                XHIBITConstant.nonContainerInsets, 0, 0));

        // Add, Edit & Delete button widgets
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.add(
            getAddBtn(),
            new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, 
                XHIBITConstant.nonContainerInsets, 0, 0));
            
        buttonPanel.add(
            getEditBtn(),
            new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.HORIZONTAL, 
                XHIBITConstant.nonContainerInsets, 0, 0));
            
        buttonPanel.add(
            getDeleteBtn(),
            new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.HORIZONTAL, 
                XHIBITConstant.nonContainerInsets, 0, 0));
        
        mainPanel.add(
            buttonPanel, 
            new GridBagConstraints(
                2, 2, 1, 1, 0.0, 0.0, 
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.HORIZONTAL, 
                XHIBITConstant.containerInsets, 0, 0));

        this.add(
            mainPanel, 
            new GridBagConstraints(
                0, 0, 1, 1, 1.0, 1.0, 
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, 
                XHIBITConstant.nonContainerInsets, 0, 0));

        MouseListener defendantsTablePopupListener = new DefendantsTablePopupListener();
        getDefendantsTable().addMouseListener(defendantsTablePopupListener);
    }

    /**
     * The scroll pane for the Defendants table 
     * @return JScrollPane
     */
    private JScrollPane getDefendantsScrollPane() {
        if( defendantsScrollPane == null ) {
            defendantsScrollPane = new JScrollPane();
            defendantsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            defendantsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            defendantsScrollPane.getViewport().add(getDefendantsTable(), null);
            defendantsScrollPane.setPreferredSize(new Dimension(400, XHIBITConstant.getLineHeight() * 5));
        }

        return defendantsScrollPane;
    }

    /**
     * The table in which the DefendantsTableRowModel records are displayed 
     * @return XTable
     */
    private XTable getDefendantsTable() {
        if( defendantsTable == null ) {
            defendantsTable = XTableFactory.getInstance().createMultiLineTable(new DefendantsTableModel());
            defendantsTable.getColumnModel().getColumn(DefendantsTableModel.HAS_INDICTMENTS).
                setCellRenderer(new CheckBoxTableCellRenderer());
            defendantsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            defendantsTable.makeSortable();

            ListSelectionModel rowSM = defendantsTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // Ignore extra messages.
                    if (e.getValueIsAdjusting()) { return; }

                    OriginalChargesHelper.redisplayTable(
                        getChargesTable(), 
                        new ArrayList<OriginalChargesTableRowModel>(
                            OriginalChargesHelper.filterCharges(
                                chargeData, 
                                getSelectedDefendant()
                            )
                        )
                    );

                    stepUpdateViewState();
                }
            });
            
            defendantsTable.add(getDefendantsTablePopup());
        }

        return defendantsTable;
    }

    /**
     * A pop-up menu on the Defendants table that allows the user to edit a defendant record
     * @return JPopupMenu 
     */
    private JPopupMenu getDefendantsTablePopup() {
        if (defendantsTablePopup == null) {
            defendantsTablePopup = new JPopupMenu();

            JMenuItem menuItem = new JMenuItem();
            menuItem.setAction(getEditDefendantAction());
            defendantsTablePopup.add(menuItem);
        }

        return defendantsTablePopup;
    }

    /**
     * A command button that causes all charge records to be displayed in the Charges table
     * @return JButton
     */
    private JButton getShowAllBtn() {
        if( showAllBtn == null) {
            showAllBtn = new JButton();
            showAllBtn.setToolTipText(OriginalChargesHelper.getResource("showAllToolTip"));
            showAllBtn.setMnemonic(OriginalChargesHelper.getResource("showAllMnm").charAt(0));
            showAllBtn.setEnabled(false);
            showAllBtn.setActionCommand("SEARCH");
            showAllBtn.setText(OriginalChargesHelper.getResource("showAllBtn"));
            showAllBtn.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    processShowAllBtn();
                    
                    stepUpdateViewState();
                }
            });
        }

        return showAllBtn;
    }

    /**
     * Redisplay the Charges table and clears the Defendants table selection 
     */
    private void processShowAllBtn() {              
        OriginalChargesHelper.redisplayTable(
            getChargesTable(), 
            new ArrayList<OriginalChargesTableRowModel>(
                OriginalChargesHelper.filterCharges(
                    chargeData, 
                    null
                )
            )
        );
        
        OriginalChargesHelper.positionRowInView(
            getChargesTable(), 
            getChargesScrollPane(), 
            true
        );

        getDefendantsTable().clearSelection();
        getChargesTable().clearSelection();
    }

    /**
     * A command button that invokes the MaintainOriginalCharge dialog in "ADD" mode.
     * This screen allows the user to add a new original charge
     * @return JButton
     */
    private JButton getAddBtn() {
        if( addBtn == null) {
            addBtn = new JButton();
            addBtn.setToolTipText(OriginalChargesHelper.getResource("addToolTip"));
            addBtn.setMnemonic(OriginalChargesHelper.getResource("addMnm").charAt(0));
            addBtn.setEnabled(false);
            addBtn.setActionCommand(MaintainOriginalChargeModel.MODE.ADD.getMode());
            addBtn.setText(OriginalChargesHelper.getResource("addBtn"));
            addBtn.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    if( isAddPossible() ) {
                        processAddBtn();
                        stepUpdateViewState();                        
                    }
                    else {
                        JOptionPane.showMessageDialog(
                            parent,
                            OriginalChargesHelper.getResource("addNoDefaultPossibleMessageLbl"),
                            OriginalChargesHelper.getResource("addNoDefaultPossibleTitleBarLbl"),
                            JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
            });
        }

        return addBtn;
    }
    
    /**
     * Checks to see if a new original charge may be added for the defendant
     * @return true if the total number of original charges ever raised for the 
     * defendant is < 999
     */
    private boolean isAddPossible() {
        return getUsedSequenceNumbers(getSelectedDefendant()).size() < 999;
    }

    
    /**
     * Gets the used sequence numbers for a defendant on case using a DefendantsTableRowModel.
     * This is used when the user has elected to add an existing charge as they are
     * required to have selected a defendant first.
     * @param selectedDefendant - coresponding to the defendant for whom the original
     * charge is to be added
     * @return List - containing all of the sequence numbers used for the defendant
     */
    private List getUsedSequenceNumbers(DefendantsTableRowModel selectedDefendant) {
        List seqNoList = defOnCaseSeqNosMap.get(selectedDefendant.getDefendantOnCaseVO().getDefendantOnCaseId()); 
        Collections.sort(seqNoList);
        return seqNoList;
        
    }
    
    /**
     * Gets the used sequence numbers for a defendant on case using a ChargesTableRowModel.
     * This is used when the user has elected to edit an existing charge as they are not
     * required to have selected a defendant first.  The ChargesTableRowModel contains
     * a defendantOnCaseVO that coresponds to the defendant for whom the charge is for.
     * The defendantOnCaseVO is used to find a record in the DefendantsTableRowModel to
     * obtain the defendant's obsolete charges. 
     * @param selectedCharge - coresponding to the original charge record to be edited
     * @return List - containing all of the sequence numbers used for the defendant
     */
    private List getUsedSequenceNumbers(ChargesTableRowModel selectedCharge) {
        List usedSequenceNumbers = null;
        for(DefendantsTableRowModel selectedDefendant : defendantData) {
            if( selectedDefendant.getDefendantOnCaseVO() == selectedCharge.getDefendantOnCaseVO()) {
                usedSequenceNumbers = getUsedSequenceNumbers(selectedDefendant);
            }
        }
        
        return usedSequenceNumbers;
    }
    
    /**
     * Invoke the dialog.
     * If the user clicked the OK button, display the new record in the Charges table
     * @throws CSRecoverableException
     */
    private void processAddBtn() throws CSRecoverableException {
        // Instantiate a MaintainOriginalChargeModel & populate it with basic details
        MaintainOriginalChargeModel mocModel = new MaintainOriginalChargeModel();
        mocModel.setMode(MaintainOriginalChargeModel.MODE.ADD.getMode());
        ChargesTableRowModel ctrm = new ChargesTableRowModel(getSelectedDefendant().getDefendantOnCaseVO());
        ctrm.setHasAnyIndictments(defHasIndictments(getSelectedDefendant().getDefendantOnCaseVO().getDefendantId()));
        //We are adding this original charge now, so it is 'new'
        ctrm.setIsNew(true);
        mocModel.setChargesTableRowModel(ctrm);            
        mocModel.setUsedSequenceNumbers(getUsedSequenceNumbers(getSelectedDefendant()));
        
        // Invoke the MaintainOriginalChargeDialog
        MaintainOriginalChargeDialog mocDialog = new MaintainOriginalChargeDialog(
            parent.getParentFrame(), mocModel
        );
        mocDialog.setVisible(true);
        
        // Check if the OK button was clicked and...
        if(mocDialog.isOkClicked()) {
            // add the new record to the chargeData as a ChargesTableRowModel
            chargeData.add(mocModel.getChargesTableRowModel());
            //Add sequence number to used sequencenumbers list
            mocModel.getUsedSequenceNumbers().add(mocModel.getChargesTableRowModel().getChargeVO().getSeqNo());
            // redisplay the Charges table filtering on any selected defendant
            OriginalChargesHelper.redisplayTable(
                getChargesTable(), 
                new ArrayList<OriginalChargesTableRowModel>(
                    OriginalChargesHelper.filterCharges(
                        chargeData, 
                        getSelectedDefendant()
                    )
                )
            );
            
            // scroll the Charges table so that the new row has focus
            OriginalChargesHelper.positionRowInView(
                getChargesTable(), 
                getChargesScrollPane(),
                false
            );
            
            this.modified();
        }
    }

    /**
     * A command button that invokes the MaintainOriginalCharge dialog in "EDIT" mode.
     * This screen allows the user to edit an original charge
     * @return JButton
     */
    private JButton getEditBtn() {
        if( editBtn == null) {
            editBtn = new JButton();
            editBtn.setToolTipText(OriginalChargesHelper.getResource("editToolTip"));
            editBtn.setMnemonic(OriginalChargesHelper.getResource("editMnm").charAt(0));
            editBtn.setEnabled(false);
            editBtn.setActionCommand(MaintainOriginalChargeModel.MODE.EDIT.getMode());
            editBtn.setText(OriginalChargesHelper.getResource("editBtn"));
            editBtn.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    processEditBtn();
                    
                    stepUpdateViewState();
                }
            });
        }

        return editBtn;
    }
    
    /**
     * Invoke the dialog.
     * If the user clicked OK, display the changed details in the Charges table
     * @throws CSRecoverableException
     */
    private void processEditBtn() throws CSRecoverableException {
        // Instantiate a MaintainOriginalChargeModel & populate it with basic details
        MaintainOriginalChargeModel mocModel = new MaintainOriginalChargeModel();
        mocModel.setMode(MaintainOriginalChargeModel.MODE.EDIT.getMode());
        mocModel.setChargesTableRowModel(getSelectedCharge());
        mocModel.setUsedSequenceNumbers(getUsedSequenceNumbers(getSelectedCharge()));
        Integer currentSeqNo = getSelectedCharge().getChargeVO().getSeqNo();
        /*
         * If the selected row was READ from the D/B, change the TRX code to UPDATE
         * otherwise the user is editing a record that is still to be CREATE-d on the
         * D/B.  In this case, leave the TRX code unchanged 
         */
        if(getSelectedCharge().getTrxCode().equalsIgnoreCase(ChargesTableRowModel.READ)) {
            mocModel.getChargesTableRowModel().setTrxCode(ChargesTableRowModel.UPDATE);
        }
        
        // Invoke the MaintainOriginalChargeDialog
        MaintainOriginalChargeDialog mocDialog = new MaintainOriginalChargeDialog(
            parent.getParentFrame(), mocModel
        );
        mocDialog.setVisible(true);

        // Check if the OK button was clicked and...
        if(mocDialog.isOkClicked()) {
            if(currentSeqNo != mocModel.getChargesTableRowModel().getChargeVO().getSeqNo()){
                //User has changed sequence number
                List sequenceNo = getUsedSequenceNumbers(getSelectedCharge());
                //Remove old number from list
                sequenceNo.remove(sequenceNo.indexOf(currentSeqNo));
                //Add new number                
                sequenceNo.add(mocModel.getChargesTableRowModel().getChargeVO().getSeqNo());
                
            }
            
            // scroll the Charges table so that the edited row has focus
            OriginalChargesHelper.positionRowInView(
                getChargesTable(),
                getChargesScrollPane(),
                getChargesTable().getSelectedRow()
            );
            
            

            this.modified();
        }
    }

    /**
     * A command button that allows the user to delete an original charge
     * @return JButton
     */
    private JButton getDeleteBtn() {
        if( deleteBtn == null) {
            deleteBtn = new JButton();
            deleteBtn.setToolTipText(OriginalChargesHelper.getResource("deleteToolTip"));
            deleteBtn.setMnemonic(OriginalChargesHelper.getResource("deleteMnm").charAt(0));
            deleteBtn.setEnabled(false);
            deleteBtn.setActionCommand(MaintainOriginalChargeModel.MODE.DELETE.getMode());
            deleteBtn.setText(OriginalChargesHelper.getResource("deleteBtn"));
            deleteBtn.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    processDeleteBtn();
                    
                    stepUpdateViewState();
                }
            });
        }

        return deleteBtn;
    }
    
    /**
     * Display a delete confirmation dialog.
     * If the user clicks OK, remove the original charge from the Charges table 
     */
    private void processDeleteBtn() {
        // Request confirmation of the delete
        boolean yesSelected = XMessageBox.alert(
            parent,
            OriginalChargesHelper.getResource("confirmDialogTitleBarLbl"),
            true,
            XMessageBox.ICONWARNING,
            OriginalChargesHelper.getResource("confirmDialogMessageLbl"),
            XDialog.YESNO,
            XDialog.DEFAULTNO
        );
        
        // Check the response and, if the delete is confirmed,...
        if( yesSelected ) {
            // remove the record from the Charges table
            getSelectedCharge().setTrxCode(ChargesTableRowModel.DELETE);
            
            /*If the charge has been added but not yet committed, then we remove the sequence number from the list
             * so that the user can use it again. We also notify the user that this is the case to avoid any confusion
             */
            if(getSelectedCharge().getIsNew()){
                Integer seqNo = getSelectedCharge().getChargeVO().getSeqNo();
                String title = OriginalChargesHelper.getResource("nonCommittedChargeDeletedTitle");
                String message = OriginalChargesHelper.getResource("nonCommittedChargeDeletedText1");//"The charge being deleted has not yet been committed to the database";
                message+="\n\n"+OriginalChargesHelper.getResource("nonCommittedChargeDeletedText2")+" "
                    +getSelectedCharge().getChargeVO().getSeqNo().toString()+" "
                    +OriginalChargesHelper.getResource("nonCommittedChargeDeletedText3");
                JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
                getUsedSequenceNumbers(getSelectedCharge()).remove(getSelectedCharge().getChargeVO().getSeqNo());
            }            
            
            // redisplay the Charges table filtering on any selected defendant
            OriginalChargesHelper.redisplayTable(
                getChargesTable(), 
                new ArrayList<OriginalChargesTableRowModel>(
                    OriginalChargesHelper.filterCharges(
                        chargeData, 
                        getSelectedDefendant()
                    )
                )
            );
                
            // scroll the Charges table so that the first row has focus
            OriginalChargesHelper.positionRowInView(
                getChargesTable(),
                getChargesScrollPane(),
                true
            );

            this.modified();
        }
    }

    /**
     * The scroll pane for the Charges table
     * @return JScrollPane
     */
    private JScrollPane getChargesScrollPane() {
        if( chargesScrollPane == null ) {
            chargesScrollPane = new JScrollPane();
            chargesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            chargesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            chargesScrollPane.getViewport().add(getChargesTable(), null);
            chargesScrollPane.setPreferredSize(new Dimension(400, XHIBITConstant.getLineHeight() * 7));
        }

        return chargesScrollPane;
    }

    /**
     * The table in which the ChargesTableRowModel records are displayed
     * @return XTable
     */
    private XTable getChargesTable() {
        if( chargesTable == null ) {
            chargesTable = XTableFactory.getInstance().createMultiLineTable(new ChargesTableModel());
            chargesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            chargesTable.makeSortable();

            ListSelectionModel rowSM = chargesTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // Ignore extra messages.
                    if (e.getValueIsAdjusting()) { return; }

                    stepUpdateViewState();
                }
            });
        }

        return chargesTable;
    }

    /**
     * The action that allows the user to invoke the Defendant Details dialog
     * @return XAction
     */
    private XAction getEditDefendantAction() {
        if( editAction == null) {
            editAction = new OCEditDefendantAction();
        }
        return editAction;
    }

    /**
     * Life-cycle method.
     * Iterates through the DefendantChargesCompositeVO array to create 
     * a collection of DefendantsTableRowModel records and ChargesTableRowModel
     * records and saves these in the model as a DefendantsTableModel and a
     * ChargesTableModel.
     */
    public void stepActivate() throws CSRecoverableException {
        defendantData = new ArrayList<DefendantsTableRowModel>();
        chargeData    = new ArrayList<ChargesTableRowModel>();
        
        // populate the DefendantsTableModel using the defendantCharges array
        for(DefendantChargesCompositeVO element : defendantChargesCompositeVOArray) {
            DefendantOnCaseVO doc = element.getDefendantOnCase();
            boolean hasIndictments = defHasIndictments(doc.getDefendantId());
            DefendantsTableRowModel dtrm = new DefendantsTableRowModel(doc, element.getObsoleteCharges());
            dtrm.setHasAnyIndictments(hasIndictments);
            defendantData.add(dtrm);
            
            for(ChargeVO charge : element.getCharges()) {
                ChargesTableRowModel chargesTRM = new ChargesTableRowModel(
                    doc, charge, ChargesTableRowModel.READ
                );
                chargesTRM.setHasAnyIndictments(hasIndictments);
                //This charge is already present so it is not 'New'
                chargesTRM.setIsNew(false);
                chargeData.add(chargesTRM);
            }
        }
        
        // Save the DefendantsTableModel & ChargesTableModel in the OriginalChargesModel
        model.setDefendantsTableModel(new DefendantsTableModel(defendantData));
        model.setChargesTableModel(new ChargesTableModel(chargeData));
        
        moveModelToScreen();
        
        stepUpdateViewState();
    }
    
    private boolean defHasIndictments(Integer defOnCaseId){
        return model.getDefsWithIndictmentsList().contains(defOnCaseId);
    }
    
    /**
     * Moves data from the model to the screen
     */
    private void moveModelToScreen() {
        OriginalChargesHelper.redisplayTable(getDefendantsTable(), model.getDefendantsTableModel().getData());
        OriginalChargesHelper.redisplayTable(getChargesTable(), model.getChargesTableModel().getData());
    }
    

    /**
     * Life-cycle method.
     */
    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }

    /**
     * Moves data from the screen to the model
     */
    private void moveScreenToModel() {
        model.setDefendantsTableModel(new DefendantsTableModel(defendantData));
        model.setChargesTableModel(new ChargesTableModel(chargeData));
    }

    /**
     * Life-cycle method called when the OK or Cancel button is clicked on the dialog.
     * If OK is clicked - indicated by update being true - build an array of OriginalChargeVO 
     * objects from the created, edited and deleted original charges and send them to the mid-tier.
     * If Cancel is clicked - indicated by update being false - check to see if the user has any
     * unsaved records in the charges table.  If they do, allow them to abort "cancel" processing.
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException
    {
        if( update ) {
            // Build an array of OriginalChargeVO objects containing the data to be persisted
            ArrayList<OriginalChargeVO> originalChargeVOArray = new ArrayList<OriginalChargeVO>();
            for( int x = 0; x < model.getChargesTableModel().getData().length; x++ ) {
                ChargesTableRowModel item = (ChargesTableRowModel)model.getChargesTableModel().getData()[x];
                
                if( item.databaseUpdateRequired() ) {
                    OriginalChargeVO data = new OriginalChargeVO();
                    data.setCourtId(item.getDefendantOnCaseVO().getCourtId());
                    data.setCaseId(item.getDefendantOnCaseVO().getCaseId());
                    data.setChargeId(item.getChargeVO().getChargeId());
                    data.setChargeType(item.getChargeVO().getChargeType());
                    data.setDefendantId(item.getDefendantOnCaseVO().getDefendantId());
                    data.setDefendantOnCaseId(item.getDefendantOnCaseVO().getDefendantOnCaseId());
                    data.setDefendantOnOffenceId(item.getChargeVO().getDefendantOnOffenceId());
                    data.setOffenceId(item.getChargeVO().getOffenceId());
                    data.setOriginalCharge(item.getChargeVO().getCrestOffenceFreetext());
                    data.setRefOffenceId(refOffenceBasicValue.getId());
                    data.setSeqNo(item.getChargeVO().getSeqNo());
                    data.setTrxCode(item.getTrxCode());
                    
                    originalChargeVOArray.add(data);
                }
            }
            
            // If there are any original charges to process, call the appropriate delegate method
            if( originalChargeVOArray.size() > 0 ) {
                XhibitDelegateHelper.getChargeDelegate().maintainOriginalCharges(
                    originalChargeVOArray.toArray(new OriginalChargeVO[originalChargeVOArray.size()]),
                    		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)
                );
            }
        }
        else {
            // If there are unsaved changes, allow the user to abort the cancel
            if(this.getModified())
            {
                if(cancelAborted()) {
                    throw new UserCancelException();
                }
            }
        }
    }
    
    /**
     * Display a pop-up dialog that allows the user to abort the cancel 
     * @return true if the cancel is to be aborted
     */
    private boolean cancelAborted() {
        boolean yesSelected = XMessageBox.alert(
            parent,
            OriginalChargesHelper.getResource("cancelDialogTitleBarLbl"),
            true,
            XMessageBox.ICONWARNING,
            OriginalChargesHelper.getResource("cancelDialogMessageLbl"),
            XDialog.YESNO,
            XDialog.DEFAULTNO
        );
        
        return !yesSelected;
    }

    /**
     * Life-cycle method called whenever the user interacts with the screen widgets.
     * Enables and disables screen widgets depending on user interaction. 
     */
    public void stepUpdateViewState() {
        boolean inEditMode = (
            model.getXac() != null
         && model.getXac().getApplicationCaseModel() != null
         && model.getXac().getApplicationCaseModel().isInEditMode());
        
        getAddBtn().setEnabled(inEditMode && (getSelectedDefendant() != null && (!getSelectedDefendant().getHasAnyIndictments())));
        getEditBtn().setEnabled(inEditMode && getChargesTable().getSelectedRowCount() > 0 && (!getSelectedCharge().getHasAnyIndictments()));
        getDeleteBtn().setEnabled(inEditMode && getChargesTable().getSelectedRowCount() > 0 && (!getSelectedCharge().getHasAnyIndictments()));
        getShowAllBtn().setEnabled(getDefendantsTable().getSelectedRowCount() > 0);
        
        okCancelPanel.okButton.setEnabled(inEditMode && isMandatoryFieldsComplete());
        okCancelPanel.cancelButton.setEnabled(true);
    }
    
    /**
     * Determines if the mandatory data input requirements have been met.
     * @return true if the Charges table has been modified
     */    
    private boolean isMandatoryFieldsComplete() {
        return this.getModified();
    }
    
    /**
     * Delegates the work to find the DefendantsTableRowModel that the user has selected.
     * @return DefendantsTableRowModel coresponding to the selected record or null
     */
    private DefendantsTableRowModel getSelectedDefendant() {
        return (DefendantsTableRowModel)getSelectedRecord(getDefendantsTable());
    }
    
    /**
     * Delegates the work to find the ChargesTableRowModel that the user has selected.
     * @return ChargesTableRowModel coresponding to the selected record or null
     */
    private ChargesTableRowModel getSelectedCharge() {
        return (ChargesTableRowModel)getSelectedRecord(getChargesTable());
    }
    
    /**
     * Returns the selected OriginalChargesTableRowModel from the indicated table.
     * @param table - XTable coresponding to the table to inspect
     * @return OriginalChargesTableRowModel - coresponding to the selected row or null
     * if no row was selected
     */
    private OriginalChargesTableRowModel getSelectedRecord(XTable table) {
        OriginalChargesTableRowModel returnValue = null;

        XHIBITTableModelInterface xtmi = (XHIBITTableModelInterface)table.getModel();
        if( table.getSelectedRow() >= 0) {
            returnValue = (OriginalChargesTableRowModel)xtmi.getDataAt(table.getSelectedRow());
        }
        
        return returnValue;
    }   

    /**
     * Life-cycle method called when the user clicks the OK button.
     * This performs any cross-field validation
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // NoAction
    }
    
    /**
     * Implementation of Refreshable interface.
     * Refreshes the Defendants and Charges tables to cater for any changes following
     * invocation of the DefendantDetails dialog
     */
    public void refresh() {
        try {
            // Save the current position of selected rows in both tables
            int selectedDefendantRow = getDefendantsTable().getSelectedRow();
            int selectedChargeRow    = getChargesTable().getSelectedRow();
            
            // Get the new version of the DefendantValue
            DefendantValue defendant = XhibitDelegateHelper.getDefendantDelegate().getDefendantDetails(
                getSelectedDefendant().getDefendantOnCaseVO().getDefendantId(),
                getSelectedDefendant().getDefendantOnCaseVO().getCaseId()
            );

            // Update the DefendantOnCaseVO with any displayable data
            getSelectedDefendant().getDefendantOnCaseVO().setAsn(defendant.getDefOnCaseBasicValue().getAsn());
            getSelectedDefendant().getDefendantOnCaseVO().setFirstName(defendant.getFirstName());
            getSelectedDefendant().getDefendantOnCaseVO().setMiddleName(defendant.getMiddleName());
            getSelectedDefendant().getDefendantOnCaseVO().setSurname(defendant.getSurName());
            
            // Redisplay the Defendants table
            OriginalChargesHelper.redisplayTable(
                getDefendantsTable(),
                new ArrayList<OriginalChargesTableRowModel>(defendantData)
            );
            
            // Re-select the defendant that was updated and position the row in the scroll pane
            OriginalChargesHelper.positionRowInView(
                getDefendantsTable(), 
                getDefendantsScrollPane(), 
                selectedDefendantRow
            );
            
            // Redisplay the Charges table filtering on the selected defendant
            OriginalChargesHelper.redisplayTable(
                getChargesTable(), 
                new ArrayList<OriginalChargesTableRowModel>(
                    OriginalChargesHelper.filterCharges(
                        chargeData, 
                        getSelectedDefendant()
                    )
                )
            );
            
            // Re-select any charge that was selected and position the row in the scroll pane
            OriginalChargesHelper.positionRowInView(
                getChargesTable(), 
                getChargesScrollPane(), 
                selectedChargeRow
            );
        }
        catch( DefendantControllerException dce ) {
            // Consuming the error
            XHIBITErrorHandler.handleError(dce);
        }
    }

    /**
     * Inner class to handle the pop-up menu on the Defendants table
     */
    class DefendantsTablePopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            int row = getDefendantsTable().rowAtPoint(e.getPoint());

            getDefendantsTable().setRowSelectionInterval(row, row);

            if (model.getXac().getApplicationCaseModel().isInEditMode(FunctionList.EDefendant))
            {
                if (e.isPopupTrigger()) {
                    getDefendantsTablePopup().show(e.getComponent(), e.getX(), e.getY());
                } 
                else if (e.getClickCount() == 2) {
                    getEditDefendantAction().actionPerformed(
                        new ActionEvent(getDefendantsTable(), 0, "EditAction")
                    );
                    e.consume();
                }
            }
        }
    }

    /**
     * Inner class that executes the action to open the DefendantDetails dialog
     * @author szn20z
     */
    class OCEditDefendantAction extends EditDefendantAction {
        public OCEditDefendantAction() {
            super();
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            UpdateDefendantModel udm = new UpdateDefendantModel(
                getSelectedDefendant().getDefendantOnCaseVO().getDefendantId(),
                model.getXac().getApplicationCaseModel().getCaseId(),
                true
            );

            OpenAmendDefendantAction action = ((OpenAmendDefendantAction)XhibitActions.getAction(
                model.getXac(),
                XhibitActions.OpenAmendDefendant)
            );
            action.setModel(udm);
            action.setCaller(OriginalChargesPanel.this);
            action.actionPerformed(e);
        }
    }
}


