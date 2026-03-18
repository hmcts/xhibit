package uk.gov.courtservice.xhibit.client.results.verdicts;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.authorise.AuthoriseResultsDialog;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SaveFunction;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

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
 */
public class VerdictsController extends XPanel implements SaveFunction {
	private static final long serialVersionUID = 1L;
	public static final int INDICTMENTS_TAB = 0;
	private static final String emptyStr = "";
    private static final Logger log = Logger.getLogger(VerdictsController.class);

    private ApplicationCaseModel acm = null;
    private XhibitApplicationController xac = null;
    private VerdictControllerModel model = null;
    
    private ResultsHelper resultsHelper = null;
    
    private VerdictIndictmentTableModel verdictIndictmentTableModel;
    private VerdictsIndictmentPanel verdictsIndictmentPanel = null;
    
    private VerdictFilterModel verdictFilterModel = null;
    private VerdictFilterSelectionModel verdictFilterSelectionModel = null; 
    
    private ChargeCompositeValue ccv;
    private ResultsSaveValue resultsSaveValue;
    
    private JTabbedPane verdictsTabbedPane = null;
    
    private Collection indictmentVerdictRefData = new ArrayList();
    private Collection indictmentResults = null;
    private int indictmentRowCount = 0;
    private int selectedTab = 0;

    /**
     * Construct taking in ApplicationCaseModel
     * 
     * @param acm
     * @throws CSRecoverableException
     */
    public VerdictsController(ApplicationCaseModel acm) throws CSRecoverableException {
        super();
        this.acm = acm;
        stepInitialise();
        jbInit();
    }

    private void jbInit() {
        this.setLayout(new GridBagLayout());
        this.add(getVerdictsTabbedPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    }

    private JTabbedPane getVerdictsTabbedPane() {
        if (verdictsTabbedPane == null) {
            verdictsTabbedPane = new JTabbedPane();
            verdictsTabbedPane.setMinimumSize(new Dimension(700, 400));

            String indictmentText = ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "tabIndictments");

            verdictsTabbedPane.addTab(indictmentText, indictmentRowCount == 0 ? new JPanel()
                    : getVerdictsIndictmentPanel());
            log.debug("indictmentRowCount is " + indictmentRowCount);
            verdictsTabbedPane.setToolTipTextAt(INDICTMENTS_TAB, indictmentText);
            verdictsTabbedPane.setEnabledAt(INDICTMENTS_TAB, indictmentRowCount != 0);

            verdictsTabbedPane.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    actionTabSelectionChanged();
                }
            });

            // verdictsTabbedPane.addFocusListener(new FocusAdapter()
            // {
            // public void focusGained(FocusEvent fe)
            // {
            // try
            // {
            // getVerdictsIndictmentPanel().stepValidate();
            // }
            // catch (CSRecoverableException csve)
            // {
            // XHIBITErrorHandler.handleError(csve);
            // }
            // }
            // });
        }
        return verdictsTabbedPane;
    }

    void actionTabSelectionChanged() {
        selectedTab = getVerdictsTabbedPane().getSelectedIndex();
        log.debug("tab changed! Tab = " + selectedTab);
        /*
         * switch (selectedTab) { case INDICTMENTS_TAB: break; default: break; }
         */
        model.setSelectedChargeType(selectedTab);
        stepUpdateViewState();
    }

    /**
     * Get VerdictIndictmentPanel
     * 
     * @return VerdictsIndictmentPanel
     */
    public VerdictsIndictmentPanel getVerdictsIndictmentPanel() {
        if (verdictsIndictmentPanel == null) {
            verdictsIndictmentPanel = new VerdictsIndictmentPanel(model);
        }
        return verdictsIndictmentPanel;
    }

    private void setupData() {
    	log.debug("setupData");
        indictmentResults = resultsHelper.getResultsForCharge(ChargeTypes.INDICTMENT.getChargeType());

        VerdictHelper.removeGuiltyPleaAndNoPleaIndictmentResults(indictmentResults);

        Object[] indictments = new ResultsRowValue[] {};
        if (indictmentResults != null) {
            indictments = indictmentResults.toArray();
            indictmentRowCount = indictments.length;
        } else {
        	// no indictment data so don't go any further
        	return;
        }
        
        for (int i = 0; i<indictments.length; i++) {
        	ResultsRowValue rrv = (ResultsRowValue) indictments[i];
        	log.debug("indictment reults " + rrv.getFirstName() + " " + rrv.getSurName());
        }

        if (verdictIndictmentTableModel != null
                && verdictsIndictmentPanel.getIndictmentTable().getModel() instanceof XHIBITTableModelInterface) {
        	verdictIndictmentTableModel.setData(indictments);
        } else {
            verdictIndictmentTableModel = new VerdictIndictmentTableModel(indictments, model);
            verdictIndictmentTableModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save
                    // button.
                    modified();
                }
            });
            model.setVerdictIndictmentTableModel(verdictIndictmentTableModel);
        }
        
        /* reset Filters for indictments */
        if (model.isResetFilters()) {
            // indictments
        	verdictFilterSelectionModel = new VerdictFilterSelectionModel();
        	
        	//resultsHelper returns ResultsRowValues type for indictmentResults
        	//need to get string value as ResultsRowValue can't be cast to DefendantValue for vfsm
        	Collection<ResultsRowValue> indictmentResults = resultsHelper.getResultsForCharge(ChargeTypes.INDICTMENT.getChargeType());
        	VerdictHelper.removeGuiltyPleaAndNoPleaIndictmentResults(indictmentResults);
        	List<ResultsRowValue> defList = new ArrayList<ResultsRowValue>(indictmentResults); //null pointer if no verdicts exist 
        	
        	Set<String> indictmentsDefendants = new TreeSet<String>();
        	ResultsRowValue rrv;
        	String defendantName;
        	for (int i = 0; i < defList.size(); i++) {
        		rrv = (ResultsRowValue) defList.get(i);
        		defendantName = getDefendantName(rrv);
        		indictmentsDefendants.add(defendantName);       		
        	}
        	
        	verdictFilterSelectionModel.setDefendants(indictmentsDefendants);

            verdictFilterModel = new VerdictFilterModel(verdictIndictmentTableModel, verdictFilterSelectionModel);
            verdictFilterModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    if (verdictsIndictmentPanel != null) {
                        getVerdictsIndictmentPanel().getIndictmentTable().clearSelection();
                        stepUpdateViewState();
                    }
                }
            });
        }// end of reset filters
        
        model.setVerdictFilterModel(verdictFilterModel);
        model.setVerdictFilterSelectionModel(verdictFilterSelectionModel);
    }
    
    private String checkNull(String toCheck) {
        if (toCheck == null) {
            return emptyStr;
        } else {
            return toCheck;
        }
    }
    
    private String getDefendantName(ResultsRowValue rrv) {
    	String defendantName;
        if (rrv == null) {
            defendantName = "";
        } else {
            String firstName = checkNull(rrv.getDefendantValue().getFirstName());
            String middle = checkNull(rrv.getDefendantValue().getMiddleName());
            String lastName = checkNull(rrv.getDefendantValue().getSurName());
            if (middle.equals(emptyStr)) {
                defendantName = firstName + " " + lastName;
            } else {
                defendantName = firstName + " " + middle + " " + lastName;
            }
        }
        return defendantName;
    }
    
    /**
     * Sets IndictmentReferenceData to VerdictControllerModel
     * 
     * @throws CSRecoverableException
     */
    public void getSysRefData() throws CSRecoverableException {
        Integer zero = new Integer(0);
        RefSystemCodeBasicValue emptyRef = new RefSystemCodeBasicValue(zero, zero, null, "", "", "", "", "", null);

        // Collection tempIndictmentVerdictRefData =
        // VerdictHelper.getVerdictRefData(
        // RefSystemCodeCriteria.CodeType.VERDICT);

        Collection tempIndictmentVerdictRefData = VerdictHelper.getVerdictRefData();

        indictmentVerdictRefData.add(emptyRef);
        indictmentVerdictRefData.addAll(tempIndictmentVerdictRefData);

        model.setIndictmentVerdictRefData(indictmentVerdictRefData);

        VerdictRestrictionHelper verdictRestrictionHelper = VerdictRestrictionHelper
                .getInstance(indictmentVerdictRefData);
        // Not add verdict lists for pleas which restrict allowed verdict.
        // Verdicts for plea CPGJ
        Vector verdictCPGJ = new Vector();
        verdictCPGJ.add("");
        verdictCPGJ.add(VerdictHelper.getRefSystemCodeBasicValue(indictmentVerdictRefData, "GJJ",
                VerdictHelper.SEARCH_TYPE_VERDICT_CODE).getDecode());
        verdictCPGJ.add(VerdictHelper.getRefSystemCodeBasicValue(indictmentVerdictRefData, "GJ",
                VerdictHelper.SEARCH_TYPE_VERDICT_CODE).getDecode());
        verdictCPGJ.add(VerdictHelper.getRefSystemCodeBasicValue(indictmentVerdictRefData, "GLJ",
                VerdictHelper.SEARCH_TYPE_VERDICT_CODE).getDecode());
        verdictCPGJ.add(VerdictHelper.getRefSystemCodeBasicValue(indictmentVerdictRefData, "GAJ",
                VerdictHelper.SEARCH_TYPE_VERDICT_CODE).getDecode());
        verdictRestrictionHelper.putRestrictedVerdictList("CPGJ", verdictCPGJ);
        verdictRestrictionHelper.putRestrictedVerdictCodes("CPGJ", new String[] { "GJ", "GJJ", "GLJ", "GAJ" });

        Vector verdictNPT = new Vector();
        verdictNPT.add("");
        verdictNPT.add(VerdictHelper.getRefSystemCodeBasicValue(indictmentVerdictRefData, "NV",
                VerdictHelper.SEARCH_TYPE_VERDICT_CODE).getDecode());
        verdictNPT.add(VerdictHelper.getRefSystemCodeBasicValue(indictmentVerdictRefData, "O",
                VerdictHelper.SEARCH_TYPE_VERDICT_CODE).getDecode());
        verdictNPT.add(VerdictHelper.getRefSystemCodeBasicValue(indictmentVerdictRefData, "DUD",
                VerdictHelper.SEARCH_TYPE_VERDICT_CODE).getDecode());
        verdictRestrictionHelper.putRestrictedVerdictList("NPT", verdictNPT);
        verdictRestrictionHelper.putRestrictedVerdictCodes("NPT", new String[] { "NV", "O", "DUD" });

    }

    /**
     * 
     * @return VerdictControllerModel
     */
    public VerdictControllerModel getVerdictControllerModel() {
        return model;
    }

    /*
     * used for Multiple Verdict updates
     */
    /*
     * public void updateIndictmentTable() { ResultsRowValue rrv; VerdictValue
     * verdictValue;
     * 
     * int rows[] = model.getSelectedIndictments(); Calendar cal =
     * model.getArraignmentDate(); Integer verdictId = model.getVerdictId();
     * String verdictCode = model.getVerdictCode(); String verdictDescription =
     * model.getVerdictDescription(); Integer altRefOffenceId =
     * model.getAltOffenceId(); String altRefOffenceCode =
     * model.getAltOffenceCode(); String altRefOffenceDesc =
     * model.getAltOffenceDesc();
     * 
     * for (int i = 0; i < rows.length; i++) { rrv = (ResultsRowValue)
     * indictmentFilterModel.getRow(i); verdictValue = rrv.getVerdictValue(); if
     * (verdictValue == null) { verdictValue = new VerdictValue();
     * rrv.setVerdictValue(verdictValue);
     * verdictValue.setCaseId(rrv.getChargeValue().getCaseID());
     * verdictValue.setDefendantID(rrv.getDefendantValue().getDefendantID());
     * verdictValue.setOffenceID(rrv.getOffenceValue().getOffenceID()); }
     * verdictValue.setArraignmentDate(cal);
     * verdictValue.setVerdictID(verdictId);
     * verdictValue.setRefVerdictCode(verdictCode);
     * verdictValue.setRefVerdictDesc(verdictDescription);
     * verdictValue.setAltRefOffenceID(altRefOffenceId);
     * verdictValue.setAltRefOffenceCode(altRefOffenceCode);
     * verdictValue.setAltRefOffenceDesc(altRefOffenceDesc);
     * rrv.setModified(true); } indictmentFilterModel.fireTableDataChanged(); }
     */

    private void reloadDataSynch() throws CSRecoverableException {
        resultsHelper = new ResultsHelper(acm);

        ResultsCompositeValue rcv = resultsHelper.getResultsCompositeValue();
        ccv = rcv.getChargeCompositeValue();
        model.setChargeCompositeValue(ccv);
    }

    private void reloadDataPostSync() {
    	model.setResetFilters(false);
        setupData();

        XTable table = getVerdictsIndictmentPanel().getIndictmentTable();
        table.tableChanged(new TableModelEvent(table.getModel()));
        table.revalidate();
        table.repaint();
    }

    /**
     * 
     * overrides modified of XPanel.
     */
    public void modified() {
        if (selectedTab == INDICTMENTS_TAB) {
            JTable table = getVerdictsIndictmentPanel().getIndictmentTable();
            // JA added
            table.revalidate();
            table.repaint();
        }
        super.modified();
    }

    // * * * * * Implemented abstract methods from XPanel * * * * *

    public void stepInitialise() throws CSRecoverableException {
        xac = acm.getXhibitApplicationController();
        model = new VerdictControllerModel();
        model.setACM(acm);

        resultsHelper = new ResultsHelper(acm);

        ResultsCompositeValue rcv = resultsHelper.getResultsCompositeValue();
        ccv = rcv.getChargeCompositeValue();
        model.setChargeCompositeValue(ccv);

        getSysRefData();
        model.setResetFilters(true);
        setupData();
    }

    public void stepDeactivate() throws CSRecoverableException {
    }

    public void stepValidate() throws CSRecoverableException, CSValidationException {
        if (getModified()) {
            int rc = JOptionPane.showConfirmDialog(xac, ResourceBundleHelper.getResource(XhibitBundles.Verdicts,
                    "Verdict.QuerySave.Message"), ResourceBundleHelper.getResource(XhibitBundles.Verdicts,
                    "Verdict.QuerySave.Title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (rc == JOptionPane.YES_OPTION) {
                save();
            } else if (rc == JOptionPane.NO_OPTION) {
                setModified(false);
            } else {
                throw new UserCancelException();
            }
        }
    }

    public void stepUpdateViewState() // throws CSRecoverableException
    {
        log.debug("[VerdictController] stepUpdateViewState");
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    public void stepActivate() throws CSRecoverableException {
        log.debug("[VerdictController] stepActivate");
        stepUpdateViewState();
    }

    /**
     * SaveFunctions implementation. When the user clicks the save button, the
     * action will call these methods.
     */
    public void save() throws CSRecoverableException {
        try {
            savePreSynchAction();
            saveSynchAction();
            savePostSynchAction();
        } catch (CSRecoverableException e) {
        	log.error("Error saving verdict: "+e.getMessage());
            throw e;
        } catch (CSUnrecoverableException e) {
        	log.error("Error saving verdict: "+e.getMessage());
            throw e;
        } catch (Exception ex) {
        	log.error("Error saving verdict: "+ex.getMessage());
            throw new CSUnrecoverableException(ex);
        }
    }

    public void savePreSynchAction() throws Exception {
        log.debug("Method: save()");
        try {
            setModified(false);
            if (getVerdictsIndictmentPanel().getIndictmentTable().isEditing()) {
                getVerdictsIndictmentPanel().getIndictmentTable().getCellEditor().stopCellEditing();

                // Abandon further save processing if this is being edited
                if (getVerdictsIndictmentPanel().getIndictmentTable().getSelectedColumn() == verdictIndictmentTableModel.COLUMN_DATE
                        && model.isDateRangeError()) {
                    // An error message would appear before this validation
                    // forcing escape.
                    model.setDateRangeError(false); // reset
                    throw new UserCancelException();
                }
            }
            getVerdictsIndictmentPanel().stepValidate();
        } catch (Exception e) {
            setModified(true);
            throw e;
        }
    }

    // CCN0229
    private boolean promptForExportReminder() {
        boolean myDisplay = false;
        for (int i = 0; i < verdictIndictmentTableModel.getRowCount(); i++) {
            ResultsRowValue rrv = (ResultsRowValue) verdictIndictmentTableModel.getDataAt(i);
            VerdictValue vv = rrv.getVerdictValue();

            if (vv != null){
                if (vv.promptForExportReminder()) { //VV requires the displaying of the 'export' dialog
                    myDisplay = true;
                }else {//VV does not require the displaying of the 'export' dialog
                    return false;
                }
            }else{//IF VV is blank then results cannot be exported to CJSE, as only the completed verdicts should be saved to CJSE.
                return false;
            }
        }

        return myDisplay;
    }

    public void saveSynchAction() throws Exception {
    	String username = XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
        setModified(false);
        // Update resultsSaveValue with Verdicts and Courtlog information and
        // operations
        resultsSaveValue = new ResultsSaveValue(XhibitSingleton.getInstance().getCourtId());
        VerdictHelper.setAlteredFlags(verdictIndictmentTableModel, resultsSaveValue);
        try {
            if (resultsSaveValue.getResultSaveValueCount() > 0) {
                XhibitDelegateHelper.getResults2Delegate().setVerdicts(resultsSaveValue, username);
                reloadDataSynch();
            }
        } catch (Exception e) {
            // Need to ensure can save again
            setModified(true);
            throw e;
        }
    }

    public void savePostSynchAction() throws Exception {
        try {
            if (resultsSaveValue.getResultSaveValueCount() > 0) {
                getVerdictsIndictmentPanel().getIndictmentTable().clearSelection();
                reloadDataPostSync();

                /*
                 * CCN0229
                 */
                if (promptForExportReminder()) {
                    int rtn = JOptionPane.showConfirmDialog(xac, ResourceBundleHelper.getResource(
                            XhibitBundles.CaseProgressResources, "results.export.message"), ResourceBundleHelper
                            .getResource(XhibitBundles.CaseProgressResources, "results.export.title"),
                            JOptionPane.YES_NO_OPTION);

                    if (rtn == JOptionPane.YES_OPTION) {
                        AuthoriseResultsDialog dialog = new AuthoriseResultsDialog(xac);
                        dialog.setVisible(true);
                    }
                }
            }
            setModified(false);
            stepUpdateViewState();
        } catch (Exception e) {
            // Need to ensure can save again
            setModified(true);
            throw e;
        }
    }
}