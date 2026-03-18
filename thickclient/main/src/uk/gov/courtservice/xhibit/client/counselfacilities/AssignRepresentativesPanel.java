package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.courtlog.CheckBoxTableCellEditor;
import uk.gov.courtservice.xhibit.client.courtlog.CheckBoxTableCellRenderer;
import uk.gov.courtservice.xhibit.client.print.FOPInterface;
import uk.gov.courtservice.xhibit.client.print.factory.FOPFactory;
import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: AssignRepresentativesPanel
 * </p>
 * <p>
 * Description: The panel for the main counsel sign in screen where the entire
 * list will be displayed and new counsels can be signed in.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class AssignRepresentativesPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private String resources = XhibitBundles.CounselFacilities;

    private CounselFacilitiesHelper helper = new CounselFacilitiesHelper();

    private GridBagLayout gridBagLayout = new GridBagLayout();

    private JScrollPane resultsScrollPane = null;

    private XTable resultsTable = null;

    private Collection<AssignRepresentativesTableRowModel> assignedReps = null;

    private JLabel nameLbl = null;

    private JTextField nameText = null;

    private JButton printBtn = null;

    private JButton findBtn = null;

    private JLabel sortByLbl = null;

    private ButtonGroup sortByRadioGroup = null;

    private JRadioButton courtRoomRadio = null;

    private JRadioButton defendantRadio = null;

    private TitledBorder tb = new TitledBorder(BorderFactory.createEtchedBorder(SystemColor.controlHighlight,
            SystemColor.controlShadow), XHIBITConstant.getResource(resources, "lblAssignedRepresentatives"));

    private XDialog parent;

    private AssignRepresentativesModel model;

    private ApplyOkCancelPanel buttonPanel;

    /**
     * Public constructor
     * 
     * @param parent
     * @param model
     * @throws CSRecoverableException
     */
    public AssignRepresentativesPanel(XDialog parent, AssignRepresentativesModel model) throws CSRecoverableException {
        super();

        this.parent = parent;
        this.model = model;
        this.buttonPanel = (ApplyOkCancelPanel) parent.getButtonPanel();

        stepInitialise();
        jbInit();
    }

    /**
     * Obtain reference data and/or data that is required to be available before
     * the screen is built
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        // empty
    }

    /**
     * Add components to the screen
     */
    private void jbInit() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.add(getNameLbl(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getNameText(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getFindBtn(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getPrintBtn(), new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getSortByLbl(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getCourtRoomRadio(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getDefendantRadio(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        JPanel assignPanel = new JPanel();
        assignPanel.setLayout(new GridBagLayout());
        assignPanel.add(getResultsScrollPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        this.setLayout(gridBagLayout);
        this.add(searchPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(assignPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        getSortByRadioGroup().add(getCourtRoomRadio());
        getSortByRadioGroup().add(getDefendantRadio());
    }

    private JLabel getNameLbl() {
        if (nameLbl == null) {
            nameLbl = new JLabel();
            nameLbl.setText(XHIBITConstant.getResource(resources, "lblName"));
        }

        return nameLbl;
    }

    private JTextField getNameText() {
        if (nameText == null) {
            nameText = new JTextField();
            nameText.setToolTipText(XHIBITConstant.getResource(resources, "ttName"));
            nameText.setPreferredSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            nameText.setMinimumSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            nameText.setColumns(30);
        }

        return nameText;
    }

    private JLabel getSortByLbl() {
        if (sortByLbl == null) {
            sortByLbl = new JLabel();
            sortByLbl.setText(XHIBITConstant.getResource(resources, "lblSortBy"));
        }

        return sortByLbl;
    }

    private ButtonGroup getSortByRadioGroup() {
        if (sortByRadioGroup == null) {
            sortByRadioGroup = new ButtonGroup();
        }

        return sortByRadioGroup;
    }

    private JRadioButton getCourtRoomRadio() {
        if (courtRoomRadio == null) {
            courtRoomRadio = new JRadioButton();
            courtRoomRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblCourt"));
            courtRoomRadio.setActionCommand(CounselFacilitiesHelper.COURADIO);
            courtRoomRadio.setText(XHIBITConstant.getResource(resources, "lblCourt"));
            courtRoomRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmCourt").charAt(0));
            courtRoomRadio.setSelected(true);
            courtRoomRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sortByRadio(e);
                }
            });
        }

        return courtRoomRadio;
    }

    private JRadioButton getDefendantRadio() {
        if (defendantRadio == null) {
            defendantRadio = new JRadioButton();
            defendantRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblPartyName"));
            defendantRadio.setActionCommand(CounselFacilitiesHelper.DEFRADIO);
            defendantRadio.setText(XHIBITConstant.getResource(resources, "lblPartyName"));
            defendantRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmParty").charAt(0));
            defendantRadio.setSelected(false);
            defendantRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sortByRadio(e);
                }
            });
        }

        return defendantRadio;
    }

    private void sortByRadio(ActionEvent e) {
        sortByRadio(e.getActionCommand());
    }

    private void sortByRadio() {
        sortByRadio(getSortByRadioGroup().getSelection().getActionCommand());
    }

    private void sortByRadio(String actionCommand) {
        if (actionCommand.equalsIgnoreCase(CounselFacilitiesHelper.COURADIO)) {
            CounselFacilitiesHelper.sortByCourtRoom(model.getAssignRepresentativesTableModel().getData());
        } else {
            CounselFacilitiesHelper.sortByDefendant(model.getAssignRepresentativesTableModel().getData());
        }

        moveModelToScreen();

        stepUpdateViewState();
    }

    private JScrollPane getResultsScrollPane() {
        if (resultsScrollPane == null) {
            resultsScrollPane = new JScrollPane();
            resultsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            resultsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            resultsScrollPane.setBorder(tb);
            resultsScrollPane.getViewport().add(getResultsTable(), null);
            resultsScrollPane.setPreferredSize(new Dimension(800, 496));
        }

        return resultsScrollPane;
    }

    private XTable getResultsTable() {
        if (resultsTable == null) {
            resultsTable = XTableFactory.getInstance().createMultiLineTable(new AssignRepresentativesTableModel());

            resultsTable.getTableHeader().setReorderingAllowed(false);
            resultsTable.getColumnModel().getColumn(AssignRepresentativesTableModel.SELECT).setCellRenderer(
                    new CheckBoxTableCellRenderer());
            resultsTable.getColumnModel().getColumn(AssignRepresentativesTableModel.SELECT).setCellEditor(
                    new CheckBoxTableCellEditor(new JCheckBox()));

            TableColumn column = null;
            column = resultsTable.getColumnModel().getColumn(AssignRepresentativesTableModel.COURTROOM);
            column.setPreferredWidth(90);
            column = resultsTable.getColumnModel().getColumn(AssignRepresentativesTableModel.CASE_NUMBER);
            column.setPreferredWidth(90);
            column = resultsTable.getColumnModel().getColumn(AssignRepresentativesTableModel.TIME_LISTED);
            column.setPreferredWidth(50);
            column = resultsTable.getColumnModel().getColumn(AssignRepresentativesTableModel.ROLE_IN_CASE);
            column.setPreferredWidth(100);
            column = resultsTable.getColumnModel().getColumn(AssignRepresentativesTableModel.PARTY);
            column.setPreferredWidth(210);
            column = resultsTable.getColumnModel().getColumn(AssignRepresentativesTableModel.SELECT);
            column.setPreferredWidth(50);
            column = resultsTable.getColumnModel().getColumn(AssignRepresentativesTableModel.REPRESENTATIVES);
            column.setPreferredWidth(210);

            resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionModel rowSM = resultsTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // Ignore extra messages.
                    if (e.getValueIsAdjusting())
                        return;

                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if (!lsm.isSelectionEmpty()) {
                        //empty
                    } else {
                        stepUpdateViewState();
                    }
                }
            });
        }

        return resultsTable;
    }

    private JButton getFindBtn() {
        if (findBtn == null) {
            findBtn = new JButton();
            findBtn.setToolTipText(XHIBITConstant.getResource(resources, "ttFindLegalRep"));
            findBtn.setMnemonic(XHIBITConstant.getResource(resources, "mnmFind").charAt(0));
            findBtn.setText(XHIBITConstant.getResource(resources, "lblFindLegalRep"));
            findBtn.addActionListener(new XAction() {
                private static final long serialVersionUID = 1L;

                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    // Call the FindLegalRepresentativeDialog utility screen
                    FindLegalRepresentativeModel flrModel = new FindLegalRepresentativeModel();
                    flrModel.setXac(model.getXac());
                    FindLegalRepresentativeDialog flrDialog = new FindLegalRepresentativeDialog(
                            parent.getParentFrame(), flrModel);

                    flrDialog.setVisible(true);

                    // If the OK button was clicked, save the details in the
                    // model
                    if (flrDialog.isOkClicked()) {
                        FindLegalRepresentativeTableRowModel flrtrModel = flrModel
                                .getFindLegalRepresentativeTableRowModel();

                        model.setLegalRep(flrtrModel);

                        moveModelToScreen();
                    }

                    stepUpdateViewState();
                }
            });
        }

        return findBtn;
    }

    private JButton getPrintBtn() {
        if (printBtn == null) {
            printBtn = new JButton();
            printBtn.setToolTipText(XHIBITConstant.getResource(resources, "ttPrintAssignedReps"));
            printBtn.setMnemonic(XHIBITConstant.getResource(resources, "mnmPrintAssignedReps").charAt(0));
            printBtn.setActionCommand("PRINT");
            printBtn.setText(XHIBITConstant.getResource(resources, "lblPrintAssignedReps"));
            printBtn.addActionListener(new XAction() {

                private static final long serialVersionUID = 1L;

                public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
                    try {
                        String xslfo = helper.formatAssignedRepsForPrinting(model);

                        XHIBITConstant.debug("xslfo:" + xslfo);

                        // Call the FOP interface passing the XML document to be
                        // printed
                        try {
                            FOPInterface fop = FOPFactory.getFOPRenderer(false);
                            fop.printDocument(xslfo, false);
                        } catch (Exception fope) {
                            throw new CSRecoverableException("gui.printaction.print", null,
                                    "An error occurred whilst printing.", fope);
                        }
                    } catch (UserCancelException uce) {
                        // No action to take. User has merely cancelled out of
                        // the print options screen
                    } catch (Exception e) {
                        throw new CSRecoverableException("gui.printaction.format", null,
                                "An error occurred whilst formatting the data to print.", e);
                    }

                    stepUpdateViewState();
                }
            });
        }

        return printBtn;
    }

    /**
     * Obtain non-reference data from the mid-tier. For each record returned:
     * Populate an AssignRepresentativesTableRowModel object Add the details to
     * a collection Save the collection in the model. Update the view state.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        assignedReps = new Vector<AssignRepresentativesTableRowModel>();

        Iterator iter = getCFCDelegate().getAssignRepresentatives(XhibitSingleton.getInstance().getCourtId(),
                new Date()).iterator();

        while (iter.hasNext()) {
            PartyOnCaseValue item = (PartyOnCaseValue) iter.next();

            AssignRepresentativesTableRowModel row = new AssignRepresentativesTableRowModel();

            row.setCaseNumber(item.getCaseNumber());
            row.setCaseType(item.getCaseType());

            row.setCourtRoomId(item.getCourtRoomId());
            if (item.getIsFloating().equalsIgnoreCase(CounselFacilitiesHelper.UNASSIGNED)) {
                row.setCourtRoom(XHIBITConstant.getResource(resources, "dcdUnassigned"));
            } else {
                row.setCourtRoom(item.getCourtRoomDisplayName());
            }
            row.setCourtSiteShortName(item.getCourtSiteShortName());

            row.setCrestCourtRoomNumber(item.getCrestCourtRoomNumber());

            if (item.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_A)
                    || item.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_S)
                    || item.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_T)) {
                row.setParty(CounselFacilitiesHelper.getDefendantNames(item.getDefendants()));
            } else {
                row.setParty(item.getCaseTitle());
            }

            row.setRepresentatives(CounselFacilitiesHelper.getFullNamesAndChambers(item.getRepresentatives()));
            row.setRole(item.getPartyRole());
            if (item.getTimeListed() == null) {
                row.setTimeListed("");
            } else {
                row.setTimeListed(XDateFormat.format(item.getTimeListed(), XDateFormat.TIMEFORMAT));
            }
            row.setSelected(new Boolean(false));

            if (item.getDefendants().size() > 0) {
                row.setDefendantId(CounselFacilitiesHelper.getFirstDefendant(item.getDefendants()).getId());
            } else {
                row.setDefendantId(null);
            }

            row.setShvId(item.getScheduledHearingId());
            row.setScheduledHearingDefendantId(item.getScheduledHearingDefendantId());
            row.setIsFloating(item.getIsFloating());

            // PRE00180 - need to set the new values also
            row.setCourtSiteCode(item.getCourtSiteCode());
            row.setSittingSequenceNo(item.getSittingSequenceNo());
            row.setShSequenceNo(item.getShSequenceNo());

            assignedReps.add(row);
        }

        model.setAssignRepresentativesTableModel(new AssignRepresentativesTableModel(assignedReps));

        sortByRadio();

        moveModelToScreen();

        stepUpdateViewState();
    }

    /**
     * Move data from the model to the screen. Refresh the table of assigned
     * representatives
     */
    private void moveModelToScreen() {
        if (model.getLegalRep() != null) {
            getNameText().setText(model.getLegalRep().getFullName());
        }

        getResultsTable().setModel(model.getAssignRepresentativesTableModel());
        getResultsTable().tableChanged(new TableModelEvent(getResultsTable().getModel()));
    }

    /**
     * Change the state of the screen components depending upon available data
     */
    public void stepUpdateViewState() {
        int resultsTableSize = model.getAssignRepresentativesTableModel().getData().size();

        enableTextField(getNameText(), false);
        getFindBtn().setEnabled(true);
        getPrintBtn().setEnabled(resultsTableSize > 0);
        getCourtRoomRadio().setEnabled(resultsTableSize > 0);
        getDefendantRadio().setEnabled(resultsTableSize > 0);

        // Only enable the OK/Apply/Finish button when all mandatory fields
        // have been populated
        buttonPanel.applyButton.setEnabled(isMandatoryFieldsComplete());
        buttonPanel.okButton.setEnabled(isMandatoryFieldsComplete());
        buttonPanel.cancelButton.setEnabled(true);
    }

    private boolean isMandatoryFieldsComplete() {
        return (model.getLegalRep() != null);
    }

    /**
     * Perform logical validation for all the data on the screen
     * 
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSRecoverableException {
        // An 'In Person' sign in may only be performed for
        // Defendants/Appellants
        if (model.getLegalRep().getLegalRepType().equalsIgnoreCase(CounselFacilitiesHelper.INPRADIO)) {
            Iterator iter = assignedReps.iterator();
            while (iter.hasNext()) {
                AssignRepresentativesTableRowModel row = (AssignRepresentativesTableRowModel) iter.next();
                if (row.isSelected().booleanValue()) {
                    if (row.getRole().equalsIgnoreCase(CounselFacilitiesHelper.ROLE_APPELLANT)
                            || row.getRole().equalsIgnoreCase(CounselFacilitiesHelper.ROLE_DEFENDANT)) {
                        // NoAction. This is acceptable
                    } else {
                        throw new CSValidationException("gui.counselSignIn.inPerson",
                                "In Person sign-in may only be performed on a Defendant or Appellant role");
                    }
                }
            }
        }
        
        Vector<Integer> caseNumbersProcessed = new Vector<Integer>();
        Iterator iter = model.getAssignRepresentativesTableModel().getData().iterator();
        while (iter.hasNext()) {
            AssignRepresentativesTableRowModel item = (AssignRepresentativesTableRowModel) iter.next();
            if ((item.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_A)
                    || item.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_S)
                    || item.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_T))
                    && !caseNumbersProcessed.contains(item.getCaseNumber())) {
                validateCase(item.getCaseNumber());
                caseNumbersProcessed.add(item.getCaseNumber());
            }
        }
    }
    
    private void validateCase(Integer caseNumber) throws CSRecoverableException {
        boolean defending = false;
        boolean prosecuting = false;
        
        Iterator iter = model.getAssignRepresentativesTableModel().getData().iterator();
        while (iter.hasNext()) {
            AssignRepresentativesTableRowModel item = (AssignRepresentativesTableRowModel) iter.next();
            if (item.isSelected() != null 
                    && item.isSelected().booleanValue()
                    && item.getCaseNumber().equals(caseNumber)) {
                String role = item.getRole();
                if (role != null && "P".equals(role)) {
                        //uk.gov.courtservice.xhibit.business.database.query.counsel.CounselConstants.PARTY_ROLE_PROSECUTION.equals(role)) {
                    prosecuting = true;
                }
                if (role != null && "D".equals(role)) {
                        //uk.gov.courtservice.xhibit.business.database.query.counsel.CounselConstants.PARTY_ROLE_DEFENDANT.equals(role)) {
                    defending = true;
                }
            }
        }
        
        if (defending && prosecuting) {
            throw new CSRecoverableException(
                    "gui.counselSignIn.sameRepForDefenceAndProsecution",
                    "The same counsel has been chosen for defence and prosecution");
        }
    }

    /**
     * Populate the model with data from the screen ready to be written back to
     * the mid-tier
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }

    /**
     * Move data from the screen to the model
     */
    private void moveScreenToModel() {
        model.setAssignRepresentativesTableModel((AssignRepresentativesTableModel) getResultsTable().getModel());
        // model.printModel( true );
    }

    /**
     * If the Apply/Finish/OK button was clicked the value of the parameter will
     * be set to true. For each record with the Select checkbox ticked: Build a
     * collection of SHLegRepBasicValue objects Call the
     * setAssignRepresentativesUsingBasicVO( ) method on the Counsel Facilities
     * Controller passing the collection as a parameter
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            Collection<SHLegRepBasicValue> legalRepsCollection = new Vector<SHLegRepBasicValue>();

            Iterator iter = model.getAssignRepresentativesTableModel().getData().iterator();
            while (iter.hasNext()) {
                AssignRepresentativesTableRowModel item = (AssignRepresentativesTableRowModel) iter.next();

                // Only process "A", "S" or "T" cases
                if (item.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_A)
                        || item.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_S)
                        || item.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_T)) {
                    if (item.isSelected() != null && item.isSelected().booleanValue()) {
                        SHLegRepBasicValue shLegRepBV = new SHLegRepBasicValue();

                        shLegRepBV.setCcInfoID(null);
                        shLegRepBV.setCrestSequenceNo(null);
                        shLegRepBV.setIsSignIn("Y");
                        shLegRepBV.setLegalRole(item.getLegalRole());
                        shLegRepBV.setRefDefenceCategoryID(null);
                        shLegRepBV.setRefLegalRepID(model.getLegalRep().getLegalRepId());
                        shLegRepBV.setSchedHearDefID(item.getScheduledHearingDefendantId());
                        shLegRepBV.setScheduledHearingID(item.getShvId());

                        if (model.getLegalRep().getLegalRepType().equalsIgnoreCase(CounselFacilitiesHelper.BARRADIO)) {
                            shLegRepBV.setSolFirmOrRefLegalRep(CounselFacilitiesHelper.LEGAL_REP_TYPE_LAWYER);
                            shLegRepBV.setRefLegalRepID(model.getLegalRep().getLegalRepId());
                            shLegRepBV.setRefSolicitorFirmID(null);
                        } else if (model.getLegalRep().getLegalRepType().equalsIgnoreCase(
                                CounselFacilitiesHelper.SOLRADIO)) {
                            shLegRepBV.setRefLegalRepID(model.getLegalRep().getLegalRepId());
                            shLegRepBV.setSolFirmOrRefLegalRep(CounselFacilitiesHelper.LEGAL_REP_TYPE_SOLICITOR);
                            shLegRepBV.setRefSolicitorFirmID(model.getLegalRep().getChambersId());
                        } else if (model.getLegalRep().getLegalRepType().equalsIgnoreCase(CounselFacilitiesHelper.LEGAL_REP_TYPE_IN_PERSON)) {
                            shLegRepBV.setSolFirmOrRefLegalRep(CounselFacilitiesHelper.LEGAL_REP_TYPE_IN_PERSON);
                            shLegRepBV.setRefLegalRepID(null);
                            shLegRepBV.setRefSolicitorFirmID(null);
                        } else {
                        	shLegRepBV.setSolFirmOrRefLegalRep(CounselFacilitiesHelper.LEGAL_REP_TYPE_NON_ATTENDANCE);
                            shLegRepBV.setRefLegalRepID(null);
                            shLegRepBV.setRefSolicitorFirmID(null);
                        }

                        legalRepsCollection.add(shLegRepBV);
                    }
                }
            }

            // If any rows were selected, make the appropriate BusOp call to
            // save the data
            if (legalRepsCollection.size() > 0) {
                getCFCDelegate().setAssignRepresentativesUsingBasicVO(legalRepsCollection,
                        XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

                if (parent.isApplyClicked()) {
                    int result = JOptionPane.showConfirmDialog(parent, XHIBITConstant.getResource(resources,
                            "lblAssignRepresentativesApplyMessage"), XHIBITConstant.getResource(resources,
                            "lblAssignRepresentativesApplyTitle"), JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        stepActivate();
                    }
                }
            }
        }
    }

    private CounselFacilitiesControllerBeanBusinessDelegate getCFCDelegate() {
        return XhibitDelegateHelper.getCounselFacilitiesDelegate();
    }

    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setEditable(state);
        textField.setBackground((state ? Color.white : SystemColor.text));
    }

    public JComponent getFirstEnterableComponent() {
        return getFindBtn();
    }
}
