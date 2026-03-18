package uk.gov.courtservice.xhibit.client.results.pleas;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
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
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SaveFunction;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SelectAllFunction;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title: This is the Plea screen
 * </p>
 * <p>
 * Description: The Plea screen that contains three tabs (Indictments, Summary
 * Offences, Breaches & Failure to Appear Offences)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 * 
 *  @history 09/06/2009 Luis Valenzuela - Added Bail Act (Failure to Appear Charge) functionality
 */
public class PleaController extends XPanel implements SaveFunction, SelectAllFunction {

    private static final long serialVersionUID = 101L;
    
    private static final int INDICTMENTS_TAB = 0;
    private static final int SECTION41S_TAB = 1;
    private static final int BREACHES_TAB = 2;
    private static final int FAIL2APPEAR_TAB = 3; // This panel holds Bail Act Offence Pleas

    private static final Logger log = Logger.getLogger(PleaController.class);

    /** Guilty Plea codes. Note - codes must be in alphabetical order! */
    private static final String[] GUILTY_PLEA_CODES = new String[] { "G", "GAO", "GLO" };

    private XhibitApplicationController xac = null;

    private PleaHelper pleaHelper = null;
    private ResultsHelper resultsHelper = null;

    private PleaControllerModel model = null;
    private ApplicationCaseModel acm = null;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JTabbedPane pleasTabbedPane = null;

    private PleasIndictmentPanel pleasIndictmentPanel = null;
    private PleasSection41Panel pleasSection41sPanel = null;
    private PleasBreachesPanel pleasBreachesPanel = null;
    private PleasBailActPanel pleasBailActPanel = null;

    private int selectedTab = 0;

    private Collection indictmentResults;
    private Collection section41Results;
    private Collection breachResults;
    private Collection bailActResults;

    private PleaIndictmentTableModel pleaIndictmentTableModel;
    private PleaSection41TableModel pleaSection41TableModel;
    private PleaBreachTableModel pleaBreachTableModel;
    private PleaBailActTableModel pleaBailActTableModel;
    
    private PleaFilterSelectionModel indictmentFilterSelectionModel;
    private PleaFilterSelectionModel s41FilterSelectionModel;
     
    private PleaFilterModel indictmentFilterModel;
    private PleaFilterModel s41FilterModel;
   
    private ChargeCompositeValue ccv;

    private Collection<RefSystemCodeBasicValue> indictmentPleaRefData = new ArrayList<RefSystemCodeBasicValue>();
    private Collection<RefSystemCodeBasicValue> section41PleaRefData = new ArrayList<RefSystemCodeBasicValue>();
    private Collection<String> breachPleaRefData = new ArrayList<String>();
    private Collection<String> bailActPleaRefData = new ArrayList<String>();
    
    private int indictmentRowCount = 0;
    private int section41RowCount = 0;
    private int breachRowCount = 0;
    private int bailActRowCount = 0;
  
    private ResultsSaveValue resultsSaveValue;

    /**
     * Creates the Plea screen.
     * 
     * @param acm
     *            ApplicationCaseModel
     * @throws CSRecoverableException
     */
    public PleaController(ApplicationCaseModel acm) throws CSRecoverableException {
        super();
        this.acm = acm;
        stepInitialise();
        jbInit();
    }

    /**
     * Paints the screen.
     */
    private void jbInit() {
        this.setLayout(gridBagLayout1);
        this.add(getPleasTabbedPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

    }

    /**
     * Lazy instantiates the Pleas tabbed pane.
     * 
     * @return the Pleas tabbed pane.
     */
    private JTabbedPane getPleasTabbedPane() {
        if (pleasTabbedPane == null) {
            pleasTabbedPane = new JTabbedPane();
            pleasTabbedPane.setMinimumSize(new Dimension(700, 400));

            String indictmentText = ResourceBundleHelper.getResource(XhibitBundles.Pleas, "tabIndictments");
            String section41sText = ResourceBundleHelper.getResource(XhibitBundles.Pleas, "tabSection41s");
            String breachesText = ResourceBundleHelper.getResource(XhibitBundles.Pleas, "tabBreaches");
            String fail2AppearText = ResourceBundleHelper.getResource(XhibitBundles.Pleas, "tabFail2Appear");

            
            pleasTabbedPane.addTab(indictmentText, indictmentRowCount == 0 ? new JPanel() : getPleasIndictmentPanel());
            pleasTabbedPane.addTab(section41sText, section41RowCount == 0 ? new JPanel() : getPleasSection41sPanel());
            pleasTabbedPane.addTab(breachesText, breachRowCount == 0 ? new JPanel() : getPleasBreachesPanel());
            pleasTabbedPane.addTab(fail2AppearText, bailActRowCount == 0 ? new JPanel() : getPleasBailActPanel());

            
            pleasTabbedPane.setToolTipTextAt(INDICTMENTS_TAB, indictmentText);
            pleasTabbedPane.setToolTipTextAt(SECTION41S_TAB, section41sText);
            pleasTabbedPane.setToolTipTextAt(BREACHES_TAB, breachesText);
            pleasTabbedPane.setToolTipTextAt(FAIL2APPEAR_TAB, fail2AppearText);
            

            pleasTabbedPane.setEnabledAt(INDICTMENTS_TAB, indictmentRowCount != 0);
            pleasTabbedPane.setEnabledAt(SECTION41S_TAB, section41RowCount != 0);
            pleasTabbedPane.setEnabledAt(BREACHES_TAB, breachRowCount != 0);
            pleasTabbedPane.setEnabledAt(FAIL2APPEAR_TAB, bailActRowCount != 0);
            

            pleasTabbedPane.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    actionTabSelectionChanged();
                }
            });

        }
        return pleasTabbedPane;
    }

    /**
     * This is called when the user selects a different tab.
     */
    private void actionTabSelectionChanged() {
        selectedTab = getPleasTabbedPane().getSelectedIndex();
        log.debug("tab changed! Tab = " + selectedTab);

        switch (selectedTab) {
        case INDICTMENTS_TAB:
            break;

        case SECTION41S_TAB:
            break;

        case BREACHES_TAB:
            break;
        
        case FAIL2APPEAR_TAB:
            break;
        }
        model.setSelectedChargeType(selectedTab);
        stepUpdateViewState();
    }

    /**
     * Lazy instantiates the Pleas Indictment panel.
     * 
     * @return the Pleas Indictment panel.
     */
    public PleasIndictmentPanel getPleasIndictmentPanel() {
        if (pleasIndictmentPanel == null) {
            pleasIndictmentPanel = new PleasIndictmentPanel(model);
            pleasIndictmentPanel.addPropertyChangeListener(PleasIndictmentPanel.SCREEN_CHANGED,
                    new RowSelectionPropertyListener(this));
        }
        return pleasIndictmentPanel;
    }

    /**
     * Lazy instantiates the Pleas Section41 (Summary Offences) panel.
     * 
     * @return the Pleas Section41 (Summary Offences) panel.
     */
    public PleasSection41Panel getPleasSection41sPanel() {
        if (pleasSection41sPanel == null) {
            pleasSection41sPanel = new PleasSection41Panel(model);
        }
        return pleasSection41sPanel;
    }

    /**
     * Lazy instantiates the Pleas Breaches panel.
     * 
     * @return the Pleas Breaches panel.
     */
    public PleasBreachesPanel getPleasBreachesPanel() {
        if (pleasBreachesPanel == null) {
            pleasBreachesPanel = new PleasBreachesPanel(model);
        }
        return pleasBreachesPanel;
    }
    
    /**
     * Lazy instantiates the Pleas Bail Act panel.
     * 
     * @return the Pleas Bail Act panel.
     */
    public PleasBailActPanel getPleasBailActPanel() {
        if (pleasBailActPanel == null) {
            pleasBailActPanel = new PleasBailActPanel(model);
        }
        return pleasBailActPanel;
    }
    
    

    /**
     * Sets up the data for the table and filter models.
     */
    private void setupData() {
        indictmentResults = resultsHelper.getResultsForCharge(ChargeTypes.INDICTMENT.getChargeType());
        section41Results = resultsHelper.getResultsForCharge(ChargeTypes.SECTION_41.getChargeType());
        breachResults = resultsHelper.getResultsForCharge(ChargeTypes.BREACH.getChargeType());
        bailActResults = resultsHelper.getResultsForCharge(ChargeTypes.FAIL2APPEAR.getChargeType());

        
        Object[] indictments = new ResultsRowValue[] {};
        if (indictmentResults != null) {
            indictments = indictmentResults.toArray();
            indictmentRowCount = indictments.length;
        }

        if (pleaIndictmentTableModel == null) {
            pleaIndictmentTableModel = new PleaIndictmentTableModel(indictments, model);
            pleaIndictmentTableModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save
                    // button.
                    modified();
                }
            });
        } else {
            pleaIndictmentTableModel.setData(indictments);
        }

        Object[] section41s = new ResultsRowValue[] {};
        if (section41Results != null) {
            section41s = section41Results.toArray();
            section41RowCount = section41s.length;
        }

        if (pleaSection41TableModel == null) {
            pleaSection41TableModel = new PleaSection41TableModel(section41s);
            pleaSection41TableModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save
                    // button.
                    modified();
                }
            });
        } else {
            pleaSection41TableModel.setData(section41s);
        }

        Object[] breaches = new ResultsRowValue[] {};
        if (breachResults != null) {
            breaches = breachResults.toArray();
            breachRowCount = breaches.length;
        }

        if (pleaBreachTableModel == null) {
            pleaBreachTableModel = new PleaBreachTableModel(breaches, model);
            pleaBreachTableModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save
                    // button.
                    modified();
                }
            });
        } else {
            pleaBreachTableModel.setData(breaches);
        }
        
        /* Bail Act Offences (FAIL2APPEAR TAB) */
        Object[] bailActOffences = new ResultsRowValue[] {};
        if (bailActResults != null) {
            bailActOffences = bailActResults.toArray();
            bailActRowCount = bailActOffences.length;
        }

        if (pleaBailActTableModel == null) {
            pleaBailActTableModel = new PleaBailActTableModel(bailActOffences, model);
            pleaBailActTableModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save
                    // button.
                    modified();
                }
            });
        } else {
            pleaBailActTableModel.setData(bailActOffences);
        }

        /* reset Filters for indictments and s41s */
        if (model.isResetFilters()) {
            // indictments
            indictmentFilterSelectionModel = new PleaFilterSelectionModel();
            indictmentFilterSelectionModel.setIndictments(pleaHelper.getAllIndictments(ccv));
            indictmentFilterSelectionModel.setCounts(pleaHelper.getAllCounts(ccv, PleaHelper.INDICTMENT_CHARGE_TYPE));
            indictmentFilterSelectionModel.setDefendants(pleaHelper.getAllDefendants(ccv,
                    PleaHelper.INDICTMENT_CHARGE_TYPE));

            indictmentFilterModel = new PleaFilterModel(pleaIndictmentTableModel, indictmentFilterSelectionModel);
            indictmentFilterModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    if (pleasIndictmentPanel != null) {
                        getPleasIndictmentPanel().getIndictmentTable().clearSelection();
                        stepUpdateViewState();
                    }
                }
            });

            // section 41s
            s41FilterSelectionModel = new PleaFilterSelectionModel();
            s41FilterSelectionModel.setIndictments(new ArrayList());
            s41FilterSelectionModel.setCounts(pleaHelper.getAllCounts(ccv, PleaHelper.SECTION41_CHARGE_TYPE));
            s41FilterSelectionModel.setDefendants(pleaHelper.getAllDefendants(ccv, PleaHelper.SECTION41_CHARGE_TYPE));

            s41FilterModel = new PleaFilterModel(pleaSection41TableModel, s41FilterSelectionModel);
            
        }// end of reset filters

        model.setPleaIndictmentTableModel(pleaIndictmentTableModel);
        model.setPleaSection41TableModel(pleaSection41TableModel);
        model.setPleaBreachTableModel(pleaBreachTableModel);
        model.setPleaBailActTableModel(pleaBailActTableModel);
        
        model.setIndictmentFilterModel(indictmentFilterModel);
        model.setS41FilterModel(s41FilterModel);
        
        model.setIndictmentFilterSelectionModel(indictmentFilterSelectionModel);
        model.setS41FilterSelectionModel(s41FilterSelectionModel);
        
    }

    /**
     * Gets the system reference data for this screen.
     * 
     * @throws CSRecoverableException
     */
    private void getSysRefData() {
        Integer zero = new Integer(0);
        RefSystemCodeBasicValue emptyRef = new RefSystemCodeBasicValue(zero, zero, null, "", "", "", "", "", null);

        Collection<RefSystemCodeBasicValue> tempIndictmentPleaRefData = PleaHelper.getPleaRefData();

        indictmentPleaRefData.add(emptyRef);
        indictmentPleaRefData.addAll(tempIndictmentPleaRefData);

        Collection<RefSystemCodeBasicValue> tempSection41PleaRefData = PleaHelper.getPleaS41RefData();
        section41PleaRefData.add(emptyRef);
        section41PleaRefData.addAll(tempSection41PleaRefData);

        breachPleaRefData.add("");
        breachPleaRefData.add(ResourceBundleHelper.getResource(XhibitBundles.Pleas, "breach.plea.true"));
        breachPleaRefData.add(ResourceBundleHelper.getResource(XhibitBundles.Pleas, "breach.plea.false"));

        bailActPleaRefData.add(""); 
        bailActPleaRefData.add(ResourceBundleHelper.getResource(XhibitBundles.Pleas, "bailAct.plea.admitted"));
        bailActPleaRefData.add(ResourceBundleHelper.getResource(XhibitBundles.Pleas, "bailAct.plea.notAdmitted"));
        
        model.setIndictmentPleaRefData(indictmentPleaRefData);
        model.setSection41PleaRefData(section41PleaRefData);
        model.setBreachPleaRefData(breachPleaRefData);
        model.setBailActPleaRefData(bailActPleaRefData);
    }

    /**
     * Gets the PleaControllerModel i.e that data model for this screen.
     * 
     * @return the PleaControllerModel
     */
    public PleaControllerModel getPleaControllerModel() {
        return model;
    }

    /**
     * Used from Multiple Plea screen.<br/> 1. Loops through the selected rows
     * of the Indictment table model (there is 1 row for each
     * Indictment/Count/Defendant combination) and sets the PleaValue for each.<br/>
     * 2. Updates the table display.<br/> 3. Enables the Save action.
     */
    public void updateIndictmentTable() {
        ResultsRowValue rrv;
        PleaValue pleaValue;

        int rows[] = model.getSelectedIndictments();
        for (int i = 0; i < rows.length; i++) {
            rrv = (ResultsRowValue) indictmentFilterModel.getDataAt(rows[i]);
            pleaValue = rrv.getPleaValue();
            System.err.println("RRV action: " + rrv.getAction());
            if (pleaValue == null) {
                pleaValue = new PleaValue();
                rrv.setPleaValue(pleaValue);
                rrv.setAction(ResultsRowValue.RESULT_ADD);
            } else if (rrv.getAction() != ResultsRowValue.RESULT_ADD) {
                rrv.setAction(ResultsRowValue.RESULT_UPDATE);
            }
            pleaValue.setArraignmentDate(model.getArraignmentDate());
            pleaValue.setRefPleaId(model.getPleaId());
            pleaValue.setRefPleaCode(model.getPleaCode());
            pleaValue.setRefPleaDesc(model.getPleaDescription());
            pleaValue.setAltRefOffenceId(model.getAltOffenceId());

            pleaValue.setAltRefOffenceCodeAndDesc(model.getAltOffenceCode(), model.getAltOffenceDesc());

            pleaValue.setOtherPleaText(model.getOtherPleaText());
            // set basic information (non plea specific)
            pleaValue.setDefendantOnOffenceId(rrv.getDefendantOnOffenceId());
        }

        // Notifies listeners that all cell values in the table may have changed
        // The number of rows may also have changed and the table should redraw
        indictmentFilterModel.fireTableDataChanged();

        // Data has been modified so enable the Save action.
        modified();

        // Any rows in the table that were selected have been deselected by the
        // fireTableDataChanged() method call above, so update the model.
        model.setSelectedIndictments(null);

        // call life cycle method to enable/disable actions as appropriate.
        stepUpdateViewState();
    }

    /**
     * Checks to see if a defendant with the given defendant id has a Plea for
     * any type of charge/offence. Called by Pleas and Directions.
     * 
     * @param defendantId
     *            defendant to check for Pleas.
     * @return true if the given defendant id has a Plea on any Indictment/
     *         Section 41/Breach/ Fail2Appear.
     */
    public boolean hasPlea(Integer defendantId) {
        if (pleaHelper.hasPlea(pleaIndictmentTableModel, defendantId))
            return true;
        if (pleaHelper.hasPlea(pleaSection41TableModel, defendantId))
            return true;
        if (pleaHelper.hasPlea(pleaBreachTableModel, defendantId))
            return true;
        if (pleaHelper.hasPlea(pleaBailActTableModel, defendantId))
            return true;
        
        return false;
    }

    /**
     * Checks if there is a guilty Plea on all defendant/count pairs on all
     * indictments. Called by Pleas and Directions
     * 
     * @returns true, if all Pleas on Indictments are Guilty or Guilty to a
     *          Lesser or Alternate Offence.
     */
    public boolean allGuiltyOnAllIndictments() {
        ResultsRowValue rrv;
        PleaValue pleaValue;
        String pleaCode;

        for (int i = 0; i < pleaIndictmentTableModel.getRowCount(); i++) {
            rrv = (ResultsRowValue) pleaIndictmentTableModel.getDataAt(i);
            if (rrv.getPleaValue() == null) {
                return false;
            } else {
                pleaValue = rrv.getPleaValue();
                pleaCode = pleaValue.getRefPleaCode();

                if (pleaCode == null || pleaValue.isGuilty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * overrides modified of XPanel.
     */
    public void modified() {
        if (selectedTab == INDICTMENTS_TAB) {
            getPleasIndictmentPanel().getIndictmentTable().repaint();
        } else if (selectedTab == SECTION41S_TAB) {
            getPleasSection41sPanel().getSection41Table().repaint();
        } else if (selectedTab == BREACHES_TAB) {
            getPleasBreachesPanel().getBreachTable().repaint();
        } else if (selectedTab == FAIL2APPEAR_TAB) {
            getPleasBailActPanel().getBailActTable().repaint();
        }
        
        super.setModified(true);
    }

    /**
     * XPanel implementation used to retrieve any data required by the screen.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        xac = acm.getXhibitApplicationController();
        model = new PleaControllerModel();
        model.setACM(acm);

        pleaHelper = new PleaHelper(acm);
        resultsHelper = pleaHelper.getResultsHelper();

        ResultsCompositeValue rcv = resultsHelper.getResultsCompositeValue();
        ccv = rcv.getChargeCompositeValue();
        model.setChargeCompositeValue(ccv);

        getSysRefData();

        model.setResetFilters(true);
        setupData();
    }

    /**
     * XPanel implementation called by the application when leaving the screen.
     * Used here to disable any action used by this screen.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        XhibitActions.getAction(xac, XhibitActions.MultiplePlea).setEnabled(false);
        enableSelectAll(false);
    }

    /**
     * XPanel implementation called by the application when leaving the screen
     * Used here to prompt the user if there is any data that has not yet been
     * saved.
     * 
     * @throws CSRecoverableException
     * @throws CSValidationException
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        if (getModified()) {
            int rc = JOptionPane.showConfirmDialog(xac, ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.QuerySave.Message"), ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.QuerySave.Title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (rc == JOptionPane.YES_OPTION) {
                save();
            } else if (rc == JOptionPane.NO_OPTION) {
                setModified(false);
            } else {
                throw new UserCancelException();
            }
        }
    }

    /**
     * XPanel implementation that updates the controls on the screen after some
     * action/event occurs.
     */
    public void stepUpdateViewState() {
        log.debug("[PleaController] stepUpdateViewState");

        int[] rows = model.getSelectedIndictments();

        boolean multiplePleasEnabled = (rows != null) && (selectedTab == INDICTMENTS_TAB) && (rows.length > 1);

        XhibitActions.getAction(xac, XhibitActions.MultiplePlea).setEnabled(multiplePleasEnabled);

        enableSelectAll((selectedTab == INDICTMENTS_TAB) && (indictmentRowCount > 0));
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // Empty
    }

    /**
     * XPanel implementation
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        log.debug("[PleaController] stepActivate");

        if (indictmentRowCount > 0) {
            getPleasTabbedPane().setSelectedIndex(INDICTMENTS_TAB);
        } else if (section41RowCount > 0) {
            getPleasTabbedPane().setSelectedIndex(SECTION41S_TAB);
        } else if (breachRowCount > 0) {
            getPleasTabbedPane().setSelectedIndex(BREACHES_TAB);
        } else if (bailActRowCount > 0) {
            getPleasTabbedPane().setSelectedIndex(FAIL2APPEAR_TAB);
        }

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
        	log.error("Error saving plea: "+e.getMessage());
            throw e;
        } catch (CSUnrecoverableException e) {
        	log.error("Error saving plea: "+e.getMessage());
            throw e;
        } catch (Exception ex) {
        	log.error("Error saving plea: "+ex.getMessage());
            throw new CSUnrecoverableException(ex);
        }
    }

    public void savePreSynchAction() throws Exception {
        log.debug("Method: save()");

        try {
            // User may have been editing a cell in table when they clicked
            // save
            // (or Save action fired), so this captures the latest state of
            // that cell.
            if (indictmentRowCount > 0) {
                if (getPleasIndictmentPanel().getIndictmentTable().isEditing()) {
                    getPleasIndictmentPanel().getIndictmentTable().getCellEditor().stopCellEditing();

                    // Abandon further save processing if this is being
                    // edited
                    if (getPleasIndictmentPanel().getIndictmentTable().getSelectedColumn() == PleaIndictmentTableModel.COLUMN_DATE
                            && model.isDateRangeError()) {
                        // An error message would appear before this validation
                        // forcing escape.
                        model.setDateRangeError(false); // reset
                        throw new UserCancelException();
                    }
                    getPleasIndictmentPanel().getIndictmentTable().clearSelection();
                }
                getPleasIndictmentPanel().stepValidate();
            }
            if (section41RowCount > 0) {
                if (getPleasSection41sPanel().getSection41Table().isEditing()) {
                    getPleasSection41sPanel().getSection41Table().getCellEditor().stopCellEditing();
                    getPleasSection41sPanel().getSection41Table().clearSelection();
                }
            }
            if (breachRowCount > 0) {
                if (getPleasBreachesPanel().getBreachTable().isEditing()) {
                    getPleasBreachesPanel().getBreachTable().getCellEditor().stopCellEditing();

                    // Abandon further save processing if this is being
                    // edited
                    if (getPleasBreachesPanel().getBreachTable().getSelectedColumn() == PleaBreachTableModel.COLUMN_DATEPUT
                            && model.isDateRangeError()) {
                        // An error message would appear before this validation
                        // forcing escape.
                        model.setDateRangeError(false); // reset
                        throw new UserCancelException();
                    }
                    getPleasBreachesPanel().getBreachTable().clearSelection();
                }
            }//  end of Breach Save
            
            if (bailActRowCount > 0) {
                if (getPleasBailActPanel().getBailActTable().isEditing()) {
                    getPleasBailActPanel().getBailActTable().getCellEditor().stopCellEditing();

                    // Abandon further save processing if this is being
                    // edited
                    if (getPleasBailActPanel().getBailActTable().getSelectedColumn() == PleaBailActTableModel.COLUMN_DATEPUT
                            && model.isDateRangeError()) {
                        // An error message would appear before this validation
                        // forcing escape.
                        model.setDateRangeError(false); // reset
                        throw new UserCancelException();
                    }
                    getPleasBailActPanel().getBailActTable().clearSelection();
                }
                
                getPleasBailActPanel().stepValidate();
            }
        } catch (Exception e) {
            // Need to ensure can save again
            setModified(true);
            throw e;
        }
    }

    public void saveSynchAction() throws Exception {
    	String username = XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
        // Disable Save Action to prevent re-clicking
        setModified(false);
        // Update resultsSaveValue with Pleas and Courtlog information and
        // operations
        resultsSaveValue = new ResultsSaveValue(XhibitSingleton.getInstance().getCourtId());
        pleaHelper.setAlteredFlags(pleaIndictmentTableModel, resultsSaveValue);
        pleaHelper.setAlteredFlags(pleaSection41TableModel, resultsSaveValue);
        pleaHelper.setAlteredFlags(pleaBreachTableModel, resultsSaveValue);
        pleaHelper.setAlteredFlags(pleaBailActTableModel, resultsSaveValue);
        
        try {
            if (resultsSaveValue.getResultSaveValueCount() > 0) {
                XhibitDelegateHelper.getResults2Delegate().setPleas(resultsSaveValue, username);
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
                reloadDataPostSynch();
            }
            // Disables the Save action.
            setModified(false);
            stepUpdateViewState();
        } catch (Exception e) {
            // Need to ensure can save again
            setModified(true);
            throw e;
        }
    }

    /*
     * private void debugPleas(PleaValue[] pv) { for (int i = 0; i < pv.length;
     * i++) { System.err.println("\n---- plea[" + i + "] ---");
     * System.err.println("Ref Plea code = " + pv[i].getRefPleaCode());
     * System.err.println("Ref Plea id = " + pv[i].getRefPleaId());
     * System.err.println("DefendantChargeId = " +
     * pv[i].getDefendantChargeId()); System.err.println("DefendantOnOffenceId = " +
     * pv[i].getDefendantOnOffenceId());
     * System.err.println("getDefOnChargeOrOffenceID = " +
     * pv[i].getDefOnChargeOrOffenceID()); } }
     */

    private void reloadDataSynch() throws CSRecoverableException {
        resultsHelper = new ResultsHelper(acm);

        ResultsCompositeValue rcv = resultsHelper.getResultsCompositeValue();
        ccv = rcv.getChargeCompositeValue();
        model.setChargeCompositeValue(ccv);
    }

    private void reloadDataPostSynch() {
        model.setResetFilters(false);
        setupData();

        if (indictmentRowCount > 0) {
            XTable table = getPleasIndictmentPanel().getIndictmentTable();
            table.tableChanged(new TableModelEvent(table.getModel()));
            table.revalidate();
            table.repaint();
        }
        if (section41RowCount > 0) {
            XTable table = getPleasSection41sPanel().getSection41Table();
            table.tableChanged(new TableModelEvent(table.getModel()));
            table.revalidate();
            table.repaint();
        }
        if (breachRowCount > 0) {
            XTable table = getPleasBreachesPanel().getBreachTable();
            table.tableChanged(new TableModelEvent(table.getModel()));
            table.revalidate();
            table.repaint();
        }
        if (bailActRowCount > 0) {
            XTable table = getPleasBailActPanel().getBailActTable();
            table.tableChanged(new TableModelEvent(table.getModel()));
            table.revalidate();
            table.repaint();
        }
    }

    /**
     * CommonFunctions implementation returns true if the SelectAll action is
     * enabled.
     * 
     * @return true - SelectAll action is enabled.
     */
    public boolean canSelectAll() {
        return true;
    }

    /**
     * CommonFunctions implementation is called when the SelectAll action is
     * fired. Selects all rows in the Indictment table.
     */
    public void selectAll() {
        if (selectedTab == INDICTMENTS_TAB) {
            // JDK 1.5.0_04 appears to have introduced an issue where
            // selectAll
            // on a JTable seems to cause a mismatch between getRowCount()
            // and
            // getSelectedRows().length - the values returned are different.
            // This
            // workaround sets the selection interval across all the rows if
            // Select All requested.
            getPleasIndictmentPanel().getIndictmentTable().getSelectionModel().clearSelection();
            getPleasIndictmentPanel().getIndictmentTable().setRowSelectionInterval(0,
                    getPleasIndictmentPanel().getIndictmentTable().getRowCount() - 1);
            // getPleasIndictmentPanel().getIndictmentTable().selectAll();
        }
    }

    /**
     * Takes a collection of ResultsRowValue and removes rows with committed
     * breaches.
     * 
     * @param results
     *            collection of ResultsRowValue that populate the plea screen
     *            indictment table model.
     */
    public static void removedCommitedBreachRow(Collection results) {
        if (results != null) {
            ResultsRowValue rrv = null;
            Iterator i = results.iterator();
            while (i.hasNext()) {
                rrv = (ResultsRowValue) i.next();
                if (rrv.getChargeValue().getBreachValue() != null) {
                    if (rrv.getChargeValue().getBreachValue().getBreachType().equalsIgnoreCase("C")) {
                        // Plea not required id breach is commited
                        i.remove();
                    }
                }
            }
        }
    }

}