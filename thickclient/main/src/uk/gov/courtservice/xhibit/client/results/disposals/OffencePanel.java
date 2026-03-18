package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.actions.ActionNotFoundException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaControllerModel;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaFilterSelectionModel;
import uk.gov.courtservice.xhibit.client.results.verdicts.VerdictsController;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPopupMenu;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */
public class OffencePanel extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(OffencePanel.class);
    private static final int PANEL_WIDTH = 300;
    public static final String SCREEN_CHANGED = "SCREEN_CHANGED";

    private XhibitApplicationController xac = null;
    private DisposalController parent = null;

    private XTable offenceTable = null;
    private UnrelatedDisposalTableModel unrelatedTableModel = null;

    private JPopupMenu popup = null;
    private JMenuItem menuItem = null;

    private int selectedRow = -1;
    private int selectedColumn = -1;
    private ResultsRowValue selectedRRV = null;

    private List results;

    private XAction add = null;
    private XAction addMagistrate = null;
    private XAction addVariation = null;
    private XAction edit = null;
    private XAction delete = null;
    private XAction undelete = null;
    private XAction copyDisposal = null;
    private XAction copyUnrelatedDisposal = null;

    private String chargeType;

    private OffencePanelModel offencePanelModel = null;
    private DisposalFilterSelectionModel disposalFilterSelectionModel = null;
    
    private DisposalFilterModel indictmentFilterModel = null; //added
    private DisposalFilterModel s41FilterModel = null; //added
    private DisposalFilterModel committalFilterModel = null; //added
    private DisposalFilterModel breachFilterModel = null; //added
    private DisposalFilterModel bailActFilterModel = null; //added
    private DisposalFilterModel criminalFilterModel = null; //added
    private DisposalFilterModel unrelatedFilterModel = null; //added
    
    private DisposalFilterPanel disposalFilterPanel = null;
    private XHIBITTableModelInterface tableModel = null;
    
    private boolean criminalAppeal = false;
    private boolean magistrate = false;
    private boolean variation = false;
    private int[] oldRows;
    //added tableModel to constructor
    public OffencePanel(DisposalController dc, List results, XHIBITTableModelInterface tableModel, String chargeType) {
        super();
        this.setLayout(new GridBagLayout());
        this.parent = dc;
        this.results = results; //Results required to determine whether to create an offence table.
        this.tableModel = tableModel;
        this.chargeType = chargeType;
        
        stepInitialise();
        getOffencePanelModel().setDisposalController(dc);

        setUpActions();

        if (results != null) {
    		this.add(getDisposalFilterPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    		
        	this.add(new JScrollPane(getOffenceTable()), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        }
    }
    
    public void stepInitialise() {
    	log.debug("stepInitialise");
        parent.addPropertyChangeListener(this);
        
        disposalFilterSelectionModel = parent.getDisposalFilterSelectionModel();
        indictmentFilterModel = parent.getIndictmentFilterModel();
        
        s41FilterModel = parent.getS41FilterModel(); //added
        committalFilterModel = parent.getCommittalFilterModel(); //added
        breachFilterModel = parent.getBreachFilterModel(); //added
        bailActFilterModel = parent.getBailActFilterModel(); //added
        criminalFilterModel = parent.getCriminalFilterModel(); //added
        unrelatedFilterModel = parent.getUnrelatedFilterModel(); //added
        
        xac = parent.getACM().getXhibitApplicationController();
    }
    
    private Set<String> getDefendantName(List results) {
    	
    	Set<String> indictmentsDefendants = new TreeSet<String>();
    	ResultsRowValue rrv;
    	String defendantName;
    	for (int i = 0; i < results.size(); i++) {
    		rrv = (ResultsRowValue) results.get(i);
    		
    		if (rrv == null) {
                defendantName = "";
            } else {
                String firstName = (rrv.getDefendantValue().getFirstName());
                String middle = (rrv.getDefendantValue().getMiddleName());
                String lastName = (rrv.getDefendantValue().getSurName());
                if (middle.equals("")) {
                    defendantName = firstName + " " + lastName;
                } else {
                    defendantName = firstName + " " + middle + " " + lastName;
                }
            }
    		log.debug("getDefendantName " + defendantName);
    		indictmentsDefendants.add(defendantName);
    	}
    	return indictmentsDefendants;
    }
    
    private DisposalFilterPanel getDisposalFilterPanel() {
    	log.debug("Getting defendantDisposalPanel");
    	if (disposalFilterPanel == null) {
    		disposalFilterPanel = new DisposalFilterPanel(disposalFilterSelectionModel, parent, getOffenceTable());
    	}
    	return disposalFilterPanel;
    }
    
    private OffencePanelModel getOffencePanelModel() {
        if (offencePanelModel == null) {
            offencePanelModel = new OffencePanelModel();
        }

        return offencePanelModel;
    }

    private boolean isCriminalAppeal() {
        return parent.isCriminalAppeal();
    }

    public void refreshTable(List<Object> newData) {
        selectedRRV = null;
        offenceTable.clearSelection();
        if (unrelatedTableModel != null) {
            ((UnrelatedDisposalTableModel) offenceTable.getModel()).setData(newData);
        } else {
            ((OffenceTableModel) offenceTable.getModel()).setData(newData);
        }
    }

    /**
     * ensures Results list passed in during construction is in sync. This is
     * especially required for Criminal Appeals.
     * 
     * @param results
     */
    public void refreshResults(List results) {
    	log.debug("Results refreshed");
        this.results = results;
    }

    /**
     * Lazy instantiates the Offence table.
     * @return the Offence table.
     */
    protected XTable getOffenceTable() {
        if (results != null) {
            if (offenceTable == null) {
                Object[] longValues = null;

                // Retrieving table model from DisposalController
                /* INDICTMENTS */
                if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
                	//IndictmentTableModel doesn't update data
                    //offenceTable = XTableFactory.getInstance().createMultiLineTable(parent.getIndictmentTableModel());
                    offenceTable = XTableFactory.getInstance().createMultiLineTable(indictmentFilterModel); //added filter
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                            OffenceTableModel.COURTTYPE_COLUMN));
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                            OffenceTableModel.APPEAL_AGAINST_COLUMN));

                    longValues = new Object[] { "No.", XTable.COLUMN_WIDTH_UNDEFINED, XTable.COLUMN_WIDTH_UNDEFINED,
                            XTable.COLUMN_WIDTH_UNDEFINED, "Tick" };
                } 
                /* COMMITAL_FOR_SENTENCE */
                else if (chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())) 
                {
                    //offenceTable = XTableFactory.getInstance().createMultiLineTable(parent.getCommittalsTableModel()); 
                    offenceTable = XTableFactory.getInstance().createMultiLineTable(committalFilterModel); //added filter
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                            OffenceTableModel.COURTTYPE_COLUMN));
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                            OffenceTableModel.APPEAL_AGAINST_COLUMN));
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(OffenceTableModel.CHARGE_COLUMN));

                    longValues = new Object[] { XTable.COLUMN_WIDTH_UNDEFINED, XTable.COLUMN_WIDTH_UNDEFINED,
                            XTable.COLUMN_WIDTH_UNDEFINED, "Tick" };
                } 
                /* SECTION_41 */
                else if (chargeType.equals(ChargeTypes.SECTION_41.getChargeType())) 
                {
                    //offenceTable = XTableFactory.getInstance().createMultiLineTable(parent.getSection41TableModel());
                    offenceTable = XTableFactory.getInstance().createMultiLineTable(s41FilterModel);//added s41FilterModel
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                            OffenceTableModel.COURTTYPE_COLUMN));
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                            OffenceTableModel.APPEAL_AGAINST_COLUMN));
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(OffenceTableModel.CHARGE_COLUMN));
                    longValues = new Object[] { XTable.COLUMN_WIDTH_UNDEFINED, XTable.COLUMN_WIDTH_UNDEFINED,
                            XTable.COLUMN_WIDTH_UNDEFINED, "Tick" };
                } 
                /* BREACH_DISPOSAL */
                else if (chargeType.equals(ChargeTypes.BREACH_DISPOSAL.getChargeType())) 
                {
                    //offenceTable = XTableFactory.getInstance().createMultiLineTable(parent.getBreachesTableModel());
                	offenceTable = XTableFactory.getInstance().createMultiLineTable(breachFilterModel); //added filter
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                            OffenceTableModel.COURTTYPE_COLUMN));
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                            OffenceTableModel.APPEAL_AGAINST_COLUMN));
                    longValues = parent.getBreachesTableModel().getLongValues();
                    longValues = new Object[] { "BreachDescription", XTable.COLUMN_WIDTH_UNDEFINED,
                            XTable.COLUMN_WIDTH_UNDEFINED, XTable.COLUMN_WIDTH_UNDEFINED, "Tick" };
                } 
                /* FAIL2APPEAR_DISPOSAL */
                else if (chargeType.equals(ChargeTypes.FAIL2APPEAR_DISPOSAL.getChargeType())) 
                {
                    //offenceTable = XTableFactory.getInstance().createMultiLineTable(parent.getBailActTableModel());
                    offenceTable = XTableFactory.getInstance().createMultiLineTable(bailActFilterModel); //added filter
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                            OffenceTableModel.COURTTYPE_COLUMN));
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                            OffenceTableModel.APPEAL_AGAINST_COLUMN));
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                            OffenceTableModel.CHARGE_COLUMN));
                    longValues = new Object[] { 
                            "LongFirstName",// ExtraLongSecondName",
                            "xx. Offence Description  xxxxxxxxxxxxxxxxxxxxxxxxx", 
                            "Disposal Text xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", 
                            "Tick" };
                } 
                /* CRIMINAL_APPEAL */
                else if (chargeType.equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())) 
                {
                    criminalAppeal = true;
                    //offenceTable = XTableFactory.getInstance().createMultiLineTable(parent.getCriminalTableModel());
                    offenceTable = XTableFactory.getInstance().createMultiLineTable(criminalFilterModel); // added filter
                    offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(OffenceTableModel.CHARGE_COLUMN));
                    longValues = new Object[] { XTable.COLUMN_WIDTH_UNDEFINED, XTable.COLUMN_WIDTH_UNDEFINED,
                            XTable.COLUMN_WIDTH_UNDEFINED, XTable.COLUMN_WIDTH_UNDEFINED,
                            XTable.COLUMN_WIDTH_UNDEFINED, "Tick" };
                } 
                /* CHARGETYPE_UNRELATED */
                else if (chargeType.equals(ResultsHelper.CHARGETYPE_UNRELATED)) 
                {
                    unrelatedTableModel = parent.getUnrelatedTableModel();
                    //offenceTable = XTableFactory.getInstance().createMultiLineTable(parent.getUnrelatedTableModel());
                    offenceTable = XTableFactory.getInstance().createMultiLineTable(unrelatedFilterModel); //added filter
                    if (isCriminalAppeal()) {
                        longValues = new Object[] { XTable.COLUMN_WIDTH_UNDEFINED, "Type",
                                XTable.COLUMN_WIDTH_UNDEFINED, "Tick" };
                    } else {
                        offenceTable.removeColumn(offenceTable.getColumnModel().getColumn(
                                UnrelatedDisposalTableModel.DISPOSAL_TYPE_COLUMN));
                        longValues = new Object[] { XTable.COLUMN_WIDTH_UNDEFINED, XTable.COLUMN_WIDTH_UNDEFINED,
                                "Tick" };
                    }
                }

                offenceTable.initColumnSizes(longValues, 400);
                offenceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                offenceTable.getTableHeader().setReorderingAllowed(false);
                offenceTable.setPreferredScrollableViewportSize(new Dimension(PANEL_WIDTH, 150));

                ListSelectionModel rowSM = offenceTable.getSelectionModel();
                rowSM.addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        actionListRowSelectionChanged(e);
                    }
                });

                MouseListener popupListener = new PopupListener(getPopup());
                offenceTable.addMouseListener(popupListener);
            } //end if 
            return offenceTable;
        }

        // There is no data therefore a table is not created or retrieved
        return null;
    }

    private void actionMouseClickedOnTable(MouseEvent e) {
        XHIBITConstant.debug("* * * * in actionMouseClickedOnTable   * * * *");

        // get point where user right clicked.
        selectedRow = getOffenceTable().rowAtPoint(e.getPoint());
        if (selectedRow != -1) {
            if ((unrelatedTableModel != null) 
            		&& (offenceTable.getModel()) != null
            		&& (offenceTable.getModel()) instanceof UnrelatedDisposalTableModel) {
                selectedRRV = (ResultsRowValue) ((UnrelatedDisposalTableModel) 
                		offenceTable.getModel()).getDataAt(selectedRow);
            } else {
            	//TODO
            	//xhibit.client.results.disposals.DisposalFilterModel cannot be cast to uk.gov.courtservice.xhibit.client.results.disposals.OffenceTableModel
                //selectedRRV = (ResultsRowValue) ((OffenceTableModel) offenceTable.getModel()).getDataAt(selectedRow);
                selectedRRV = (ResultsRowValue) ((DisposalFilterModel) offenceTable.getModel()).getDataAt(selectedRow);
                setMagistrate(selectedRRV);
                setVariation(selectedRRV);
            }

            enableActions();
            // Select row in table where user clicked.
            getOffenceTable().setRowSelectionInterval(selectedRow, selectedRow);
        } else {
            selectedRRV = null;
            enableActions();
        }
        XHIBITConstant.debug("actionMouseClickedOnTable: row = " + selectedRow + " :  col = " + selectedColumn);
    }

    private void actionListRowSelectionChanged(ListSelectionEvent e) {
        XHIBITConstant.debug("* * * * in actionListRowSelectionChanged   * * * * Row");
        // Ignore extra messages.
        if (e.getValueIsAdjusting())
            return;
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (!lsm.isSelectionEmpty()) {
            selectedRow = lsm.getMinSelectionIndex();
            if ((unrelatedTableModel != null) 
            		&& (offenceTable.getModel()) != null
            		&& (offenceTable.getModel()) instanceof UnrelatedDisposalTableModel) {
                selectedRRV = (ResultsRowValue) ((UnrelatedDisposalTableModel) offenceTable.getModel())
                        .getDataAt(selectedRow);
            } else {
            	//TODO
                //selectedRRV = (ResultsRowValue) ((OffenceTableModel) offenceTable.getModel()).getDataAt(selectedRow);
            	int sr = selectedRow;
            	DisposalFilterModel localDFM = (DisposalFilterModel) offenceTable.getModel();
            	if (localDFM.getDataAt(selectedRow) != null) {
            		selectedRRV = (ResultsRowValue) localDFM.getDataAt(selectedRow);
            	}
            }
            setMagistrate(selectedRRV);
            setVariation(selectedRRV);
        } else {
            selectedRow = -1;
            selectedRRV = null;
            magistrate = false;
            variation = false;
        }
        enableActions();
    }

    class PopupListener extends MouseAdapter {
        private JPopupMenu thisPopup = null;

        public PopupListener(JPopupMenu pMenu) {
            thisPopup = pMenu;
        }

        public void mouseClicked(MouseEvent e) {
            actionMouseClickedOnTable(e);
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                // getRow(e);
                actionMouseClickedOnTable(e);
                if (thisPopup != null) {
                    thisPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }

    private void setUpActions() throws ActionNotFoundException {
        add = XhibitActions.getAction(parent.getXAC(), XhibitActions.AddDisposal);
        addMagistrate = XhibitActions.getAction(parent.getXAC(), XhibitActions.AddMagistrateDisposal);
        addVariation = XhibitActions.getAction(parent.getXAC(), XhibitActions.AddVariationDisposal);
        edit = XhibitActions.getAction(parent.getXAC(), XhibitActions.EditDisposal);
        delete = XhibitActions.getAction(parent.getXAC(), XhibitActions.DeleteDisposal);
        undelete = XhibitActions.getAction(parent.getXAC(), XhibitActions.UndeleteDisposal);
        copyDisposal = XhibitActions.getAction(parent.getXAC(), XhibitActions.CopyDisposal);
        copyUnrelatedDisposal = XhibitActions.getAction(parent.getXAC(), XhibitActions.CopyUnrelatedDisposal);

        // default all to disabled
        add.setEnabled(false);
        addMagistrate.setEnabled(false);
        addVariation.setEnabled(false);
        edit.setEnabled(false);
        delete.setEnabled(false);
        undelete.setEnabled(false);
        copyDisposal.setEnabled(false);
        copyUnrelatedDisposal.setEnabled(false);
    }

    private JPopupMenu getPopup() {
        if (popup == null) {
            // Create the popup menu.
            popup = new XPopupMenu();
            menuItem = new JMenuItem(add);
            popup.add(menuItem);
            menuItem = new JMenuItem(addMagistrate);
            popup.add(menuItem);
            menuItem = new JMenuItem(addVariation);
            popup.add(menuItem);
            popup.addSeparator();
            menuItem = new JMenuItem(edit);
            popup.add(menuItem);
            menuItem = new JMenuItem(delete);
            popup.add(menuItem);
            menuItem = new JMenuItem(undelete);
            popup.add(menuItem);
            popup.addSeparator();
            menuItem = new JMenuItem(copyDisposal);
            popup.add(menuItem);
            menuItem = new JMenuItem(copyUnrelatedDisposal);
            popup.add(menuItem);
        }
        return popup;
    }

    private void setMagistrate(ResultsRowValue rrv) {
        if (rrv.getDisposalValue() != null) {
            magistrate = rrv.getDisposalValue().getCourtType().equals("M");
        }
    }

    private void setVariation(ResultsRowValue rrv) {
        if (rrv.getDisposalValue() != null) {
            variation = rrv.getDisposalValue().getCourtType().equals("C")
                    && rrv.getDisposalValue().getPsdDisposal2Id() != null;
        }
    }

    protected void enableActions() {
        if (selectedRRV != null && parent.acm.isInEditMode(FunctionList.ECharge)) {
            if (selectedRRV.getDefendantOnOffenceValue() != null) {
                // Related disposals.
                copyUnrelatedDisposal.setEnabled(false);
                if (selectedRRV.getDefendantValue() != null) {
                    offencePanelModel.setResultRowValue(selectedRRV);
                    add.setModel(offencePanelModel);
                    add.setEnabled(true);

                    // On criminal appeals you can only add variation
                    // disposals
                    add.setEnabled(!criminalAppeal);

                    addMagistrate.setModel(offencePanelModel);
                    addMagistrate.setEnabled(criminalAppeal);

                    if (selectedRRV.getDisposalValue() != null) {
                        addVariation.setModel(offencePanelModel);

                        final boolean haveVariableAppealResult = selectedRRV.getVerdictValue() != null
                                && selectedRRV.getVerdictValue().getRefAppResultId() != null
                                && selectedRRV.getVerdictValue().isAppealResultVariable();

                        addVariation
                                .setEnabled(criminalAppeal
                                        && (magistrate && selectedRRV.getAction() == ResultsRowValue.RESULT_UNCHANGED)
                                        && selectedRRV.getPreDeleteAction() == ResultsRowValue.RESULT_UNCHANGED
                                        && selectedRRV.getAction() != ResultsRowValue.RESULT_DELETE
                                        && haveVariableAppealResult);
                        edit.setModel(offencePanelModel);
                        edit.setEnabled(selectedRRV.getPreDeleteAction() == ResultsRowValue.RESULT_UNCHANGED
                                && selectedRRV.getAction() != ResultsRowValue.RESULT_DELETE);
                        delete.setModel(offencePanelModel);
                        delete.setEnabled(selectedRRV.getPreDeleteAction() == ResultsRowValue.RESULT_UNCHANGED
                                && selectedRRV.getAction() != ResultsRowValue.RESULT_DELETE);
                        undelete.setModel(offencePanelModel);

                        /** @todo Logic for Variation Disposal */
                        undelete.setEnabled(selectedRRV.getAction() == ResultsRowValue.RESULT_DELETE
                                || (selectedRRV.getAction() == ResultsRowValue.RESULT_UNCHANGED && selectedRRV
                                        .getPreDeleteAction() == ResultsRowValue.RESULT_ADD));

                        if (variation) {
                            undelete
                                    .setEnabled((selectedRRV.getAction() == ResultsRowValue.RESULT_DELETE && !isMagistrateDeleted(selectedRRV))
                                            || (selectedRRV.getAction() == ResultsRowValue.RESULT_UNCHANGED && selectedRRV
                                                    .getPreDeleteAction() == ResultsRowValue.RESULT_ADD));
                            copyDisposal.setEnabled(false);
                        } else {
                            undelete.setEnabled(selectedRRV.getAction() == ResultsRowValue.RESULT_DELETE
                                    || (selectedRRV.getAction() == ResultsRowValue.RESULT_UNCHANGED && selectedRRV
                                            .getPreDeleteAction() == ResultsRowValue.RESULT_ADD));
                            copyDisposal.setEnabled(true);
                            copyDisposal.setModel(offencePanelModel);
                        }
                    } else // no disposal exists
                    {
                        addVariation.setModel(offencePanelModel);
                        addVariation.setEnabled(false);
                        edit.setModel(offencePanelModel);
                        edit.setEnabled(false);
                        delete.setModel(offencePanelModel);
                        delete.setEnabled(false);
                        undelete.setModel(offencePanelModel);
                        undelete.setEnabled(false);
                        copyDisposal.setEnabled(false);
                    }
                } else // no defendant selected
                {
                    add.setEnabled(false);
                    addMagistrate.setEnabled(false);
                    addVariation.setEnabled(false);
                    edit.setEnabled(false);
                    delete.setEnabled(false);
                    undelete.setEnabled(false);
                    copyDisposal.setEnabled(false);
                }
            } else // Unrelated disposals.
            {
                // These actions are criminal appeal specific. No need to enable
                // for Unrelated.
                // addMagistrate.setModel(offencePanelModel);
                // addMagistrate.setEnabled(false);
                // addVariation.setModel(offencePanelModel);
                // addVariation.setEnabled(false);

                copyDisposal.setEnabled(false);

                if (selectedRRV.getDefendantValue() != null) {
                    offencePanelModel.setResultRowValue(selectedRRV);
                    add.setModel(offencePanelModel);
                    add.setEnabled(true);

                    addMagistrate.setModel(offencePanelModel);
                    addMagistrate.setEnabled(isCriminalAppeal()
                            && chargeType.equals(ResultsHelper.CHARGETYPE_UNRELATED));

                    if (selectedRRV.getDisposalValue() != null) {
                        addVariation.setModel(offencePanelModel);
                        final boolean haveVariableAppealResult = selectedRRV.getVerdictValue() != null
                                && selectedRRV.getVerdictValue().getRefAppResultId() != null
                                && selectedRRV.getVerdictValue().isAppealResultVariable();

                        addVariation
                                .setEnabled(isCriminalAppeal() && magistrate
                                        && chargeType.equals(ResultsHelper.CHARGETYPE_UNRELATED)
                                        && selectedRRV.getAction() == ResultsRowValue.RESULT_UNCHANGED
                                        && selectedRRV.getPreDeleteAction() == ResultsRowValue.RESULT_UNCHANGED
                                        && selectedRRV.getAction() != ResultsRowValue.RESULT_DELETE
                                        && haveVariableAppealResult);

                        edit.setEnabled(selectedRRV.getPreDeleteAction() == ResultsRowValue.RESULT_UNCHANGED
                                && selectedRRV.getAction() != ResultsRowValue.RESULT_DELETE);
                        edit.setModel(offencePanelModel);
                        delete.setEnabled(selectedRRV.getPreDeleteAction() == ResultsRowValue.RESULT_UNCHANGED
                                && selectedRRV.getAction() != ResultsRowValue.RESULT_DELETE);
                        delete.setModel(offencePanelModel);

                        undelete.setEnabled(selectedRRV.getAction() == ResultsRowValue.RESULT_DELETE
                                || (selectedRRV.getAction() == ResultsRowValue.RESULT_UNCHANGED && selectedRRV
                                        .getPreDeleteAction() == ResultsRowValue.RESULT_ADD));
                        undelete.setModel(offencePanelModel);
                        copyUnrelatedDisposal.setModel(offencePanelModel);

                        if (variation) {
                            undelete
                                    .setEnabled((selectedRRV.getAction() == ResultsRowValue.RESULT_DELETE && !isMagistrateDeleted(selectedRRV))
                                            || (selectedRRV.getAction() == ResultsRowValue.RESULT_UNCHANGED && selectedRRV
                                                    .getPreDeleteAction() == ResultsRowValue.RESULT_ADD));
                            copyUnrelatedDisposal.setEnabled(false);
                        } else if (magistrate) {
                            undelete.setEnabled(selectedRRV.getAction() == ResultsRowValue.RESULT_DELETE
                                    || (selectedRRV.getAction() == ResultsRowValue.RESULT_UNCHANGED && selectedRRV
                                            .getPreDeleteAction() == ResultsRowValue.RESULT_ADD));
                            copyUnrelatedDisposal.setEnabled(false);
                        } else {
                            undelete.setEnabled(selectedRRV.getAction() == ResultsRowValue.RESULT_DELETE
                                    || (selectedRRV.getAction() == ResultsRowValue.RESULT_UNCHANGED && selectedRRV
                                            .getPreDeleteAction() == ResultsRowValue.RESULT_ADD));
                            copyUnrelatedDisposal.setEnabled(true);
                        }

                    } else // no disposal selected
                    {
                        addVariation.setEnabled(false);
                        edit.setEnabled(false);
                        delete.setEnabled(false);
                        undelete.setEnabled(false);
                        copyUnrelatedDisposal.setEnabled(false);
                    }
                } else // no defendant selected
                {
                    add.setEnabled(false);
                    edit.setEnabled(false);
                    delete.setEnabled(false);
                    undelete.setEnabled(false);
                    copyUnrelatedDisposal.setEnabled(false);
                    addMagistrate.setEnabled(false);
                    addVariation.setEnabled(false);
                }
            }
        } else {
            add.setEnabled(false);
            addMagistrate.setEnabled(false);
            addVariation.setEnabled(false);
            edit.setEnabled(false);
            delete.setEnabled(false);
            undelete.setEnabled(false);
            copyDisposal.setEnabled(false);
            copyUnrelatedDisposal.setEnabled(false);
        }
    }

    public String getChargeType() {
        return chargeType;
    }

    private boolean isMagistrateDeleted(final ResultsRowValue variationRow) {
        final Integer psdDisposal2Id = variationRow.getDisposalValue().getPsdDisposal2Id();
        ResultsRowValue rrv;
        for (int i = 0; i < results.size(); i++) {
            rrv = (ResultsRowValue) results.get(i);
            if (rrv.getDisposalValue() != null) {
                if (psdDisposal2Id.equals(rrv.getDisposalValue().getDisposal2Id())) {
                    /**
                     * @todo Logic for newly added variation that is deleted. Do
                     *       not want to enable undelete for this.
                     */
                    return rrv.getAction() == ResultsRowValue.RESULT_DELETE
                            || (rrv.getAction() == ResultsRowValue.RESULT_UNCHANGED && rrv.getPreDeleteAction() == ResultsRowValue.RESULT_ADD);
                }
            }
        }
        return false;
    }

    public void propertyChange(PropertyChangeEvent pce) {
    	log.debug("Property change");
        log.debug("PCE old value = " + pce.getOldValue() + " new value = " + pce.getNewValue());
        /*
        // if the date was null and is now still null, no change so return.
        if (pce.getOldValue() == null && pce.getNewValue() == null) {
            return;
        }

        // if the new value is null, the arraignment date has not been set, so
        // disable the filters, otherwise restore them to their original states.
        if (pce.getNewValue() == null) {
        	getDisposalFilterPanel().disableFilters();

            // Cannot use "enableSelectAll" of XPanel here as events are
            // being
            // trapped due to invalid/blank arraignment date.
            // enableSelectAll(false);
            XhibitActions.getAction(xac, XhibitActions.EditSelectAll).setEnabled(false);

            XhibitActions.getAction(xac, XhibitActions.MultiplePlea).setEnabled(false);
        } else {
            getDisposalFilterPanel().restoreFilters();

            // There may have been a change in the table selection while an
            // incomplete/invalid arraignment date was being entered. Then
            // the
            // stepValidate call in the tables focusGained method would
            // throw an
            // exception and the ListSelectionListener's valueChanged call
            // actionListRowSelectionChanged gets stopped by
            // e.getValueIsAdjusting()
            int[] rows = offenceTable.getSelectedRows();
            //parent.setSelectedIndictments(rows);
            rowSelectionChanged(rows);
        }
        */
    }
    
    private void rowSelectionChanged(final int[] newRows) {
        log.debug("[DisposalIndictmentPanel] rowSelectionChanged \noldRows = " + oldRows + "\nnewRows = " + newRows);
        firePropertyChange(SCREEN_CHANGED, oldRows, newRows);
        oldRows = newRows;
    }
}