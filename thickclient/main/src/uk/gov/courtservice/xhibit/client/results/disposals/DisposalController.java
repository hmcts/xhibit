package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseProcess;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseStatus;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.authorise.AuthoriseResultsDialog;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaFilterModel;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaFilterSelectionModel;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaHelper;
import uk.gov.courtservice.xhibit.client.results.pleas.RowSelectionPropertyListener;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SaveFunction;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.ScrollTableRowToView;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title: Main screen for adding, changing and deleting disposals.
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
 * @version $Revision: 1.69 $
 * 
 *  @history 18/06/2009 Luis Valenzuela - Added Bail Act (Failure to Appear Charge) functionality
 */
public class DisposalController extends XPanel implements SaveFunction {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(DisposalController.class);
	private static final int INDICTMENTS_TAB = 0;
	
	private int selectedTab = -1;
    private int tabIndex = 0;

    private boolean isCriminalAppeal = false;
    private boolean isNormal = false;

    private boolean indictmentTabExists = false;
    private boolean section41TabExists = false;
    private boolean committalTabExists = false;
    private boolean breachTabExists = false;
    private boolean fail2AppearExists = false;
    private boolean criminalTabExists = false;
    private boolean unrelatedTabExists = false;

    private XhibitApplicationController xac = null;
    protected ApplicationCaseModel acm;

    private ResultsHelper resultsHelper = null;

    private JTabbedPane chargesTabbedPane = null;
    private OffencePanel indictmentsPanel = null;
    private OffencePanel selectedPanel = null;
    private OffencePanel section41sPanel = null;
    private OffencePanel committalsPanel = null;
    private OffencePanel breachesPanel = null;
    private OffencePanel bailActPanel = null;
    /**
     * @todo This will be not be using OffenceValue... it is at a charge level
     */
    private OffencePanel criminalPanel = null;
    private OffencePanel unrelatedPanel = null;

    private OffenceTableModel indictmentTableModel = null;
    private OffenceTableModel section41TableModel = null;
    private OffenceTableModel committalsTableModel = null;
    private OffenceTableModel breachesTableModel = null;
    private OffenceTableModel bailActTableModel = null;
    private OffenceTableModel criminalTableModel = null;
    private UnrelatedDisposalTableModel unrelatedTableModel = null;

    private OffenceTableModel selectedModel = null; // model to hold the currently selected model
    
    //private DisposalFilterSelectionModel filterSelectionModel = null;
    private DisposalFilterSelectionModel disposalFilterSelectionModel = null;
    
    private DisposalFilterModel indictmentlFilterModel = null;
    private DisposalFilterModel s41FilterModel = null;
    private DisposalFilterModel committalFilterModel = null;
    private DisposalFilterModel breachFilterModel = null;
    private DisposalFilterModel bailActFilterModel = null;
    private DisposalFilterModel criminalFilterModel = null;
    private DisposalFilterModel unrelatedFilterModel = null;
    
    private ChargeCompositeValue ccv;

    private List indictmentResults = null;
    private List section41Results = null;
    private List committalResults = null;
    private List breachResults = null;
    private List bailActResults = null;
    private List criminalResults = null;
    private List unrelatedResults = null;

    private Component selectedComponent = null;
    private List selectedList = null;
    private List chargeTypes;
    private ResultsSaveValue resultsSaveValue;

    private XAction addMagistrateAction = null;
    private XAction addVariationAction = null;

    private String addMagistrateActionText = null;
    private String addVariationActionText = null;
    private String addMagistrateGeneralActionText = null;
    private String addVariationGeneralActionText = null;
    private boolean resetFilters = false;
    private int indictmentRowCount = 0;
    
    private JPanel buttonPanel = null;
    private JButton btnExitCase = null;

    /**
     * Creates a disposal controller.
     * 
     * @param acm
     *            the ApplicationCaseModel
     */
    public DisposalController(ApplicationCaseModel acm) {
        super();
        this.acm = acm;

        try {
            stepInitialise();
            stepActivate();
            jbInit();
            stepUpdateViewState();
            // Default to first tab
            chargesTabbedPane.setSelectedIndex(0);
        } catch (CSRecoverableException e) {
            XHIBITErrorHandler.handleError(e);
        }
    }

    private void jbInit() {
        this.setLayout(new BorderLayout());
        this.add(getChargesTabbedPane(), BorderLayout.CENTER);
        // ctx-1968
        if (xac.isCaseChargesDisposalsOpened()) {
        	getButtonPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        	this.add(getButtonPanel(), BorderLayout.SOUTH);
        }
    }

    private void createChargeTab(String chargeType) {
        // We only create the tabbed pane if a tab exists creating tabbed pane
        // when creating first tab.
        if (chargesTabbedPane == null) {
            chargesTabbedPane = new JTabbedPane();
            
            chargesTabbedPane.setMinimumSize(new Dimension(500, 400));
            chargesTabbedPane.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    actionTabSelectionChanged();
                }
            });
        }

        if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
            // and tab created is false
            if (!indictmentTabExists) {
                indictmentTabExists = true;
                String indictmentText = getResource("tabIndictments");
                chargesTabbedPane.addTab(indictmentText, getIndictmentsPanel());
                chargesTabbedPane.setSelectedIndex(tabIndex);
                chargesTabbedPane.setToolTipTextAt(tabIndex, indictmentText);
                tabIndex++;
            } else {
                getIndictmentsPanel().refreshTable(indictmentResults);
            }
        } else if (chargeType.equals(ChargeTypes.SECTION_41.getChargeType())) {
            if (!section41TabExists) {
                section41TabExists = true;
                String section41sText = getResource("tabSection41s");
                chargesTabbedPane.addTab(section41sText, getSection41sPanel());
                chargesTabbedPane.setSelectedIndex(tabIndex);
                chargesTabbedPane.setToolTipTextAt(tabIndex, section41sText);
                tabIndex++;
            } else {
                getSection41sPanel().refreshTable(section41Results);
            }
        } else if (chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())) {
            if (!committalTabExists) {
                committalTabExists = true;
                String committalsText = getResource("tabCommittals");
                chargesTabbedPane.addTab(committalsText, getCommittalsPanel());
                chargesTabbedPane.setSelectedIndex(tabIndex);
                chargesTabbedPane.setToolTipTextAt(tabIndex, committalsText);
                tabIndex++;
            } else {
                getCommittalsPanel().refreshTable(committalResults);
            }
        } else if (chargeType.equals(ChargeTypes.BREACH_DISPOSAL.getChargeType())) {
            if (!breachTabExists) {
                breachTabExists = true;
                String breachesText = getResource("tabBreaches");
                chargesTabbedPane.addTab(breachesText, getBreachesPanel());
                chargesTabbedPane.setSelectedIndex(tabIndex);
                chargesTabbedPane.setToolTipTextAt(tabIndex, breachesText);
                tabIndex++;
            } else {
                getBreachesPanel().refreshTable(breachResults);
            }
        } else if (chargeType.equals(ChargeTypes.FAIL2APPEAR_DISPOSAL.getChargeType())) {
            if (!fail2AppearExists) {
                fail2AppearExists = true;
                String fail2AppearText = getResource("tabFail2Appear");
                chargesTabbedPane.addTab(fail2AppearText, getBailActPanel());
                chargesTabbedPane.setSelectedIndex(tabIndex);
                chargesTabbedPane.setToolTipTextAt(tabIndex, fail2AppearText);
                tabIndex++;
            } else {
                getBailActPanel().refreshTable(bailActResults);
            }    
        } else if (chargeType.equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())) {
            if (!criminalTabExists) {
                criminalTabExists = true;
                String criminalText = getResource("tabCriminal");
                chargesTabbedPane.addTab(criminalText, getCriminalPanel());
                chargesTabbedPane.setSelectedIndex(tabIndex);
                chargesTabbedPane.setToolTipTextAt(tabIndex, criminalText);
                tabIndex++;
            } else {
                getCriminalPanel().refreshTable(criminalResults);
            }
        } else if (chargeType.equals(ResultsHelper.CHARGETYPE_UNRELATED)) {
            if (!unrelatedTabExists) {
                unrelatedTabExists = true;
                String unrelatedText = getResource("tabUnrelated");
                chargesTabbedPane.addTab(unrelatedText, getUnrelatedPanel());
                chargesTabbedPane.setSelectedIndex(tabIndex);
                chargesTabbedPane.setToolTipTextAt(tabIndex, unrelatedText);
                tabIndex++;
            } else {
                getUnrelatedPanel().refreshTable(unrelatedResults);
            }
        }
    }

    private JTabbedPane getChargesTabbedPane() {
        // set tab selection
        if (chargesTabbedPane == null) {
            createTabs();
            chargesTabbedPane.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    actionTabSelectionChanged();
                }
            });
        } else {
            if (selectedTab == -1) {
                chargesTabbedPane.setSelectedIndex(0);
            }
        }
        return chargesTabbedPane;
    }

    private void actionTabSelectionChanged() {
        selectedTab = getChargesTabbedPane().getSelectedIndex();
        log.debug("selected Tab is " + selectedTab);
        selectedComponent = getChargesTabbedPane().getSelectedComponent();
        if (selectedComponent instanceof OffencePanel) {
            selectedPanel = (OffencePanel) selectedComponent;
            stepUpdateViewState();
            
            XTable xt = selectedPanel.getOffenceTable();
            if (xt != null) {
            	boolean found = false;
            	XHIBITTableModelInterface xtmi = ((XHIBITTableModelInterface) xt.getModel());
            	if (xtmi instanceof DisposalFilterModel) {
            		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
            		if (dfm.getModel() instanceof OffenceTableModel) {
            			found = true;
            			//OffenceTableModel otm = (OffenceTableModel) dfm.getModel();
            			
            			//if (selectedPanel.getOffenceTable().getModel() instanceof OffenceTableModel) {
	                    if (selectedPanel.equals(indictmentsPanel)) {
	                        this.selectedList = indictmentResults;
	                    } else if (selectedPanel.equals(section41sPanel)) {
	                        this.selectedList = section41Results;
	                    } else if (selectedPanel.equals(committalsPanel)) {
	                        this.selectedList = committalResults;
	                    } else if (selectedPanel.equals(breachesPanel)) {
	                        this.selectedList = breachResults;
	                    } else if (selectedPanel.equals(bailActPanel)) {
	                        this.selectedList = bailActResults;
	                    } else if (selectedPanel.equals(criminalPanel)) {
	                        this.selectedList = criminalResults;
	                    }

	                    addMagistrateAction.setName(addMagistrateActionText);
	                    addVariationAction.setName(addVariationActionText);
            		}
                } 
            	if (!found) {
                    if (selectedPanel.equals(unrelatedPanel)) {
                        this.selectedList = unrelatedResults;

                        addMagistrateAction.setName(addMagistrateGeneralActionText);
                        addVariationAction.setName(addVariationGeneralActionText);
                    }
                }
            } else {
                this.selectedList = null;
            }
        }
    }

    public void createTabs() {
        Object chargeType;
        // Get collections of charges of different types
        Iterator chargeTypeIterator = chargeTypes.iterator();
        while (chargeTypeIterator.hasNext()) {
            chargeType = chargeTypeIterator.next();

            if (chargeType != null) {
                if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
                    createChargeTab(ChargeTypes.INDICTMENT.getChargeType());
                } else if (chargeType.equals(ChargeTypes.BREACH_DISPOSAL.getChargeType())) {
                    createChargeTab(ChargeTypes.BREACH_DISPOSAL.getChargeType());
                } else if (chargeType.equals(ChargeTypes.FAIL2APPEAR_DISPOSAL.getChargeType())) {
                    createChargeTab(ChargeTypes.FAIL2APPEAR_DISPOSAL.getChargeType());
                } else if (chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())) {
                    createChargeTab(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType());
                } else if (chargeType.equals(ChargeTypes.SECTION_41.getChargeType())) {
                    createChargeTab(ChargeTypes.SECTION_41.getChargeType());
                } else if (chargeType.equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())) {
                    createChargeTab(ChargeTypes.CRIMINAL_APPEAL.getChargeType());
                } else if (chargeType.equals(ResultsHelper.CHARGETYPE_UNRELATED)) {
                    createChargeTab(ResultsHelper.CHARGETYPE_UNRELATED);
                }
            }
        }
    }
    // added table model to offence panel constructor
    private OffencePanel getIndictmentsPanel() {
        if (indictmentsPanel == null) {
            indictmentsPanel = new OffencePanel(this, indictmentResults, 
            		indictmentTableModel, ChargeTypes.INDICTMENT.getChargeType());
            indictmentsPanel.addPropertyChangeListener(OffencePanel.SCREEN_CHANGED,
            		new RowSelectionPropertyListener(this));
        }
        return indictmentsPanel;
    }

    private OffencePanel getSection41sPanel() {
        if (section41sPanel == null) {
            section41sPanel = new OffencePanel(this, section41Results, 
            		section41TableModel, ChargeTypes.SECTION_41.getChargeType());
            section41sPanel.addPropertyChangeListener(OffencePanel.SCREEN_CHANGED,
            		new RowSelectionPropertyListener(this));
        }
        return section41sPanel;
    }

    private OffencePanel getCommittalsPanel() {
        if (committalsPanel == null) {
            committalsPanel = new OffencePanel(this, committalResults, 
            		committalsTableModel, ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType());
            committalsPanel.addPropertyChangeListener(OffencePanel.SCREEN_CHANGED,
            		new RowSelectionPropertyListener(this));
        }
        return committalsPanel;
    }

    private OffencePanel getCriminalPanel() {
        if (criminalPanel == null) {
            criminalPanel = new OffencePanel(this, criminalResults, 
            		criminalTableModel, ChargeTypes.CRIMINAL_APPEAL.getChargeType());
            criminalPanel.addPropertyChangeListener(OffencePanel.SCREEN_CHANGED,
            		new RowSelectionPropertyListener(this));
        }
        return criminalPanel;
    }

    private OffencePanel getUnrelatedPanel() {
        if (unrelatedPanel == null) {
            unrelatedPanel = new OffencePanel(this, unrelatedResults, 
            		unrelatedTableModel, ResultsHelper.CHARGETYPE_UNRELATED);
            unrelatedPanel.addPropertyChangeListener(OffencePanel.SCREEN_CHANGED,
            		new RowSelectionPropertyListener(this));
        }
        return unrelatedPanel;
    }

    private OffencePanel getBreachesPanel() {
        if (breachesPanel == null) {
            breachesPanel = new OffencePanel(this, breachResults, 
            		breachesTableModel, ChargeTypes.BREACH_DISPOSAL.getChargeType());
            breachesPanel.addPropertyChangeListener(OffencePanel.SCREEN_CHANGED,
            		new RowSelectionPropertyListener(this));
        }
        return breachesPanel;
    }

    private OffencePanel getBailActPanel() {
        if (bailActPanel == null) {
            bailActPanel = new OffencePanel(this, bailActResults, 
            		bailActTableModel, ChargeTypes.FAIL2APPEAR_DISPOSAL.getChargeType());
            bailActPanel.addPropertyChangeListener(OffencePanel.SCREEN_CHANGED,
            		new RowSelectionPropertyListener(this));
        }
        return bailActPanel;
    }
    
    protected ApplicationCaseModel getACM() {
    	return acm;
    }

    protected XhibitApplicationController getXAC() {
        return xac;
    }
    
    protected DisposalFilterSelectionModel getDisposalFilterSelectionModel() {
    	return disposalFilterSelectionModel;
    }
    
    protected DisposalFilterModel getIndictmentFilterModel() {
    	return indictmentlFilterModel;
    }
    
    //added
    protected DisposalFilterModel getS41FilterModel() {
    	return s41FilterModel;
    }
    
    protected DisposalFilterModel getCommittalFilterModel() {
    	return committalFilterModel;
    }
    
    protected DisposalFilterModel getBreachFilterModel() {
    	return breachFilterModel;
    }
    
    protected DisposalFilterModel getBailActFilterModel() {
    	return bailActFilterModel;
    }
    
    protected DisposalFilterModel getCriminalFilterModel() {
    	return criminalFilterModel;
    }
    
    protected DisposalFilterModel getUnrelatedFilterModel() {
    	return unrelatedFilterModel;
    }
    //end added

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
        	log.error("Error saving disposal: "+e.getMessage());
            throw e;
        } catch (CSUnrecoverableException e) {
        	log.error("Error saving disposal: "+e.getMessage());
            throw e;
        } catch (Exception ex) {
        	log.error("Error saving disposal: "+ex.getMessage());
            throw new CSUnrecoverableException(ex);
        }
    }

    public void savePreSynchAction() throws Exception {
        // Update resultsSaveValue with Disposals and Courtlog information and
        // operations
        resultsSaveValue = new ResultsSaveValue(XhibitSingleton.getInstance().getCourtId());

        /**
         * @todo Nice to have. Is there a collection of these that can be looped
         *       thru???
         */
        if (indictmentTableModel != null) {
            DisposalHelper.setAlteredFlags(indictmentTableModel, resultsSaveValue);
        }
        if (section41TableModel != null) {
            DisposalHelper.setAlteredFlags(section41TableModel, resultsSaveValue);
        }
        if (committalsTableModel != null) {
            DisposalHelper.setAlteredFlags(committalsTableModel, resultsSaveValue);
        }
        if (breachesTableModel != null) {
            DisposalHelper.setAlteredFlags(breachesTableModel, resultsSaveValue);
        }
        if (bailActTableModel != null) {
            DisposalHelper.setAlteredFlags(bailActTableModel, resultsSaveValue);
        }
        if (criminalTableModel != null) {
            DisposalHelper.setOrderedAlteredFlags(criminalTableModel, resultsSaveValue);
        }
        if (unrelatedTableModel != null) {
            if (isCriminalAppeal) {
                DisposalHelper.setOrderedAlteredFlags(unrelatedTableModel, resultsSaveValue);
            } else {
                DisposalHelper.setAlteredFlags(unrelatedTableModel, resultsSaveValue);
            }
        }
    }

    public void saveSynchAction() throws Exception {
        // Disable Save Action to prevent re-clicking
        setModified(false);

        try {
            if (resultsSaveValue.getResultSaveValueCount() > 0) {
                XhibitDelegateHelper.getResults2Delegate().setDisposals(resultsSaveValue);
                reloadDataSynch();
            } else {
                // This needs to reload always to resolve Added
                // disposal-deleted-Save
                // as this doesn;t change the status quo but the information is
                // left on screen
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
            // This needs to reload always to resolve Added
            // disposal-deleted-Save
            // as this doesn't change the status quo but the information is
            // left on screen
            reloadDataPostSynch();
            stepUpdateViewState();
            setModified(false);
            
            XTable xt = selectedPanel.getOffenceTable();
        	XHIBITTableModelInterface xtmi = ((XHIBITTableModelInterface) xt.getModel());
        	if (xtmi instanceof DisposalFilterModel) {
        		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
        		// Ensure the DisposalFilterModel has the correct rows inside for the selected defendant
        		dfm.filterDisposals();
        	}

        	CaseProcess caseProcess = xac.getCaseStatus().getCaseProcess();
        	log.debug("caseProcess = " + caseProcess);
        	
        	if (resultsSaveValue.getResultSaveValueCount() > 0) {
        		if (xac.getCaseStatus().getCaseProcess() == CaseProcess.UPDATE) {
	                int rtn = JOptionPane.showConfirmDialog(xac, ResourceBundleHelper.getResource(
	                        XhibitBundles.CaseProgressResources, "results.export.message"), ResourceBundleHelper
	                        .getResource(XhibitBundles.CaseProgressResources, "results.export.title"),
	                        JOptionPane.YES_NO_OPTION);
	
	                if (rtn == JOptionPane.YES_OPTION) {
	                	xac.setApplicationCaseModel(acm);
	                    AuthoriseResultsDialog dialog = new AuthoriseResultsDialog(xac);
	                    dialog.setVisible(true);
	                }
        		}
            }
        } catch (Exception e) {
            // Need to ensure can save again
            setModified(true);
            throw e;
        }
    }

    public void stepInitialise() throws CSRecoverableException {
        xac = acm.getXhibitApplicationController();
        // Check case Type
        // caseID = acm.getCaseId();
        ScheduledHearingValue shv = acm.getScheduledHearingValue();

        if (shv == null) {
            CSRecoverableException csbe = new CSRecoverableException(
                    "gui.user.DisposalController.scheduleHearingValue",
                    "gui.log.DisposalController.scheduleHearingValue");
            throw csbe;
        }

        isCriminalAppeal = CaseTypeHelper.isCriminalAppeal_CaseType(shv);
        isNormal = CaseTypeHelper.isNormal_CaseType(shv);

        if (!isNormal && !isCriminalAppeal) {
            JOptionPane.showMessageDialog(xac, getResource("MessageSentenceNotAllowedText"),
                    getResource("MessageSentenceNotAllowedTitle"), JOptionPane.ERROR_MESSAGE);
            throw new UserCancelException();
        }

        try {
            resultsHelper = new ResultsHelper(acm);
        } catch (Exception e) {
            CSRecoverableException csre = new CSRecoverableException("gui.DisposalController.loadCharges",
                    "Exception whilst getting the charge composite value object from the mid tier", e);
            throw csre;
        }

        chargeTypes = resultsHelper.getChargeTypeList();
        if (chargeTypes == null || chargeTypes.size() == 0) {
            CSRecoverableException csbe = new CSRecoverableException("gui.user.DisposalController.chargeType",
                    "gui.log.DisposalController.chargeType");
            throw csbe;
        }
        
        ResultsCompositeValue rcv = resultsHelper.getResultsCompositeValue();
        ccv = rcv.getChargeCompositeValue();
        //model.setChargeCompositeValue(ccv);
        resetFilters = true;
        
        initialiseActions();

        setupData();
    }

    public boolean isCriminalAppeal() {
        return isCriminalAppeal;
    }

    public void initialiseActions() {
        addMagistrateAction = XhibitActions.getAction(xac, XhibitActions.AddMagistrateDisposal);
        addVariationAction = XhibitActions.getAction(xac, XhibitActions.AddVariationDisposal);
        addMagistrateActionText = getActionResource("AddMagistrateDisposalName");
        addVariationActionText = getActionResource("AddVariationDisposalName");
        addMagistrateGeneralActionText = getActionResource("AddMagistrateGeneralDisposalName");
        addVariationGeneralActionText = getActionResource("AddVariationGeneralDisposalName");
    }

    public void stepActivate() throws CSRecoverableException {
    	log.debug("[DisposalController] stepActivate");

        if (indictmentRowCount > 0) {
            getChargesTabbedPane().setSelectedIndex(INDICTMENTS_TAB);
        }

        //stepUpdateViewState();
    }

    public void refreshInsertedData(int dataInsertedAt) {
    	XTable xt = selectedPanel.getOffenceTable();
    	XHIBITTableModelInterface xtmi = ((XHIBITTableModelInterface) xt.getModel());
    	if (xtmi instanceof DisposalFilterModel) {
    		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
    		List<ResultsRowValue> filteredSelectedList = getFilteredSelectedList(dfm);
    		if (dfm.getModel() instanceof OffenceTableModel) {
    			OffenceTableModel otm = (OffenceTableModel) dfm.getModel();
    			for(int i = otm.getRowCount(); i < filteredSelectedList.size(); i++){
    				dfm.addRow();
    			}
    			otm.setData(selectedList);
    			otm.fireTableRowsInserted(dataInsertedAt, dataInsertedAt);
    	        otm.fireTableDataChanged();
    		} else if (dfm.getModel() instanceof UnrelatedDisposalTableModel) {
    			UnrelatedDisposalTableModel udtm = (UnrelatedDisposalTableModel) dfm.getModel();
    			for(int i = udtm.getRowCount(); i < filteredSelectedList.size(); i++){
    				dfm.addRow();
    			}
    			udtm.setData(selectedList);
    			udtm.fireTableRowsInserted(dataInsertedAt, dataInsertedAt);
    	        udtm.fireTableDataChanged();
    		}
    	}
        //xtmi.setData(selectedList);
        //xtmi.fireTableRowsInserted(dataInsertedAt, dataInsertedAt);
        xtmi.fireTableDataChanged();

        if (selectedPanel.getChargeType().equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())
                && criminalTableModel != null) {
            // refreshing results in panel here because action enabling
            // logic in OffencePanel
            // requires access to latest rows for Magistrate/Variation
            // disposals
            criminalPanel.refreshResults(selectedList);
        }

        // It could be filtered so check row length
        if (xtmi.getRowCount() != getSelectedList().size()) {
        	if (xtmi instanceof DisposalFilterModel) {
        		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
        		dfm.fixRowsAfterAdd(dataInsertedAt);
        		int newDataInsertedAt = dfm.getFilteredRowNoAt(dataInsertedAt);
        		xt.setRowSelectionInterval(newDataInsertedAt, newDataInsertedAt);
        	}
        } else {
        	xt.setRowSelectionInterval(dataInsertedAt, dataInsertedAt);
        }
        JScrollPane scrollPane = getScrollPaneForTable(selectedPanel.getOffenceTable());
        if (scrollPane != null) {
            ScrollTableRowToView scroll = new ScrollTableRowToView(selectedPanel.getOffenceTable(), scrollPane);
            SwingUtilities.invokeLater(scroll);
        }
        selectedPanel.getOffenceTable().revalidate();
        selectedPanel.getOffenceTable().repaint();
    }
    
   private List<ResultsRowValue> getFilteredSelectedList(DisposalFilterModel dfs){
    	List<ResultsRowValue> filteredSelectedList = new ArrayList<ResultsRowValue>();
        for (int i = 0; i < selectedList.size(); i++) {
            ResultsRowValue disposalTableRow = (ResultsRowValue) selectedList.get(i);
            if (dfs.matchesFilter(disposalTableRow)) {
            	filteredSelectedList.add(disposalTableRow);
            }
        }
        return filteredSelectedList;
    }

    private JScrollPane getScrollPaneForTable(XTable table) {
        Container container = table.getParent();
        while (container != null && !(container instanceof JScrollPane)) {
            container = container.getParent();
        }
        if (container instanceof JScrollPane) {
            return (JScrollPane) container;
        }

        return null;
    }

    public void refreshData() {
        XTable xt = selectedPanel.getOffenceTable();
        int selectedRow = xt.getSelectedRow();
    	XHIBITTableModelInterface xtmi = ((XHIBITTableModelInterface) xt.getModel());
    	if (xtmi instanceof DisposalFilterModel) {
    		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
    		if (dfm.getModel() instanceof OffenceTableModel) {
    			OffenceTableModel otm = (OffenceTableModel) dfm.getModel();
    			otm.fireTableRowsUpdated(selectedRow, selectedRow);
    		} else if (dfm.getModel() instanceof UnrelatedDisposalTableModel) {
    			UnrelatedDisposalTableModel udtm = (UnrelatedDisposalTableModel) dfm.getModel();
    			udtm.fireTableRowsUpdated(selectedRow, selectedRow);
    		}
    	}
    	xtmi.fireTableDataChanged();

        if (selectedPanel.getChargeType().equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())
                && criminalTableModel != null) {
            // refreshing results in panel here because action enabling
            // logic in OffencePanel
            // requires access to latest rows for Magistrate/Variation
            // disposals
            criminalPanel.refreshResults(selectedList);
        }
    }

    private void reloadDataSynch() throws CSRecoverableException {
        resultsHelper = new ResultsHelper(acm);
        selectedList = null;
    }

    private void reloadDataPostSynch() throws CSRecoverableException {
        setupData();
    }

    /**
     * Retrieves results information for each charge type and creates/updates
     * table models
     */
    private void setupData() {
        indictmentResults = resultsHelper.getResultsForCharge(ChargeTypes.INDICTMENT_DISPOSAL.getChargeType());
        section41Results = resultsHelper.getResultsForCharge(ChargeTypes.SECTION_41_DISPOSAL.getChargeType());
        committalResults = resultsHelper.getResultsForCharge(ChargeTypes.COMMITAL_FOR_SENTENCE_DISPOSAL.getChargeType());
        breachResults = resultsHelper.getResultsForCharge(ChargeTypes.BREACH_DISPOSAL.getChargeType());
        bailActResults = resultsHelper.getResultsForCharge(ChargeTypes.FAIL2APPEAR_DISPOSAL.getChargeType());
        criminalResults = resultsHelper.getResultsForCharge(ChargeTypes.CRIMINAL_APPEAL_DISPOSAL.getChargeType());
        unrelatedResults = resultsHelper.getResultsForCharge(ResultsHelper.CHARGETYPE_UNRELATED);
        if (isCriminalAppeal()) {
            unrelatedResults = DisposalHelper.sortCriminalAppealUnrelatedDisposals(unrelatedResults);
        }
        
        Object[] indictments = new ResultsRowValue[] {};
        if (indictmentResults != null) {
            indictments = indictmentResults.toArray();
            indictmentRowCount = indictments.length;
        }

        // Remove Results not containing pleas
        DisposalHelper.removedNoPleaResults(indictmentResults);
        DisposalHelper.removedNoPleaResults(section41Results);
        DisposalHelper.removedNoPleaResults(bailActResults);
        // removedNoPleaResults(breachResults);

        // Some Breaches do not have offences - we do not want to display these
        // as Disposals are entered against Breach Offences.
        DisposalHelper.removeBreachesWithNoOffences(breachResults);
        
        if (indictmentTableModel != null && indictmentsPanel != null
                && indictmentsPanel.getOffenceTable().getModel() instanceof XHIBITTableModelInterface) {
        	
        	XTable xt = indictmentsPanel.getOffenceTable();
	    	XHIBITTableModelInterface xtmi = ((XHIBITTableModelInterface) xt.getModel());
	    	// Assume that xtmi instanceof DisposalFilterModel
	   		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
        	((OffenceTableModel) dfm.getModel()).setData(indictmentResults);
            if (selectedPanel.equals(indictmentsPanel)) {
                this.selectedList = indictmentResults;
                this.selectedModel = indictmentTableModel; //added
            }
        } else {
            indictmentTableModel = new OffenceTableModel(indictmentResults);
            indictmentTableModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save
                    // button.
                    modified();
                }
            });
        }

        // Section 41 data
        if (section41TableModel != null && section41sPanel != null
                && section41sPanel.getOffenceTable().getModel() instanceof XHIBITTableModelInterface) {
        	
        	XTable xt = section41sPanel.getOffenceTable();
	    	XHIBITTableModelInterface xtmi = ((XHIBITTableModelInterface) xt.getModel());
	    	// Assume that xtmi instanceof DisposalFilterModel
	   		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
        	((OffenceTableModel) dfm.getModel()).setData(section41Results);
            if (selectedPanel.equals(section41sPanel)) {
                this.selectedList = section41Results;
                this.selectedModel = section41TableModel; //added
            }
        } else {
            section41TableModel = new OffenceTableModel(section41Results);
            section41TableModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save button.
                    modified();
                }
            });
        }

        // Committals data
        if (committalsTableModel != null && committalsPanel != null
                && committalsPanel.getOffenceTable().getModel() instanceof XHIBITTableModelInterface) {
        	
        	XTable xt = committalsPanel.getOffenceTable();
	    	XHIBITTableModelInterface xtmi = ((XHIBITTableModelInterface) xt.getModel());
	    	// Assume that xtmi instanceof DisposalFilterModel
	   		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
        	((OffenceTableModel) dfm.getModel()).setData(committalResults);
            if (selectedPanel.equals(committalsPanel)) {
                this.selectedList = committalResults;
                this.selectedModel = committalsTableModel; //added
            }
        } else {
            committalsTableModel = new OffenceTableModel(committalResults);
            committalsTableModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save
                    // button.
                    modified();
                }
            });
        }

        // Breaches data

        if (breachResults != null) {
            Sorter.sort(breachResults, new String[] { "chargeSequenceNumber", "defendantOnChargeId" });
        }

        if (breachesTableModel != null && breachesPanel != null
                && breachesPanel.getOffenceTable().getModel() instanceof XHIBITTableModelInterface) {
        	
        	XTable xt = breachesPanel.getOffenceTable();
	    	XHIBITTableModelInterface xtmi = ((XHIBITTableModelInterface) xt.getModel());
	    	// Assume that xtmi instanceof DisposalFilterModel
	   		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
        	((OffenceTableModel) dfm.getModel()).setData(breachResults);
            if (selectedPanel.equals(breachesPanel)) {
                this.selectedList = breachResults;
                this.selectedModel = breachesTableModel; //added
            }
        } else {
            breachesTableModel = new OffenceTableModel(breachResults);
            breachesTableModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save button.
                    modified();
                }
            });
        }

        /* Bail Act Offence data */
        if (bailActResults != null) {
            Sorter.sort(bailActResults, new String[] { "chargeSequenceNumber", "defendantOnChargeId" });
        }
        if (bailActTableModel != null && bailActPanel != null
                && bailActPanel.getOffenceTable().getModel() instanceof XHIBITTableModelInterface) {
        	
        	XTable xt = bailActPanel.getOffenceTable();
	    	XHIBITTableModelInterface xtmi = ((XHIBITTableModelInterface) xt.getModel());
	    	// Assume that xtmi instanceof DisposalFilterModel
	   		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
        	((OffenceTableModel) dfm.getModel()).setData(bailActResults);
            if (selectedPanel.equals(bailActPanel)) {
                this.selectedList = bailActResults;
                this.selectedModel = bailActTableModel; //added
            }
        } else {
            bailActTableModel = new OffenceTableModel(bailActResults);
            bailActTableModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save button.
                    modified();
                }
            });
        }

        // Criminal Appeal data
        if (criminalTableModel != null && criminalPanel != null
                && criminalPanel.getOffenceTable().getModel() instanceof XHIBITTableModelInterface) {
        	
        	XTable xt = criminalPanel.getOffenceTable();
	    	XHIBITTableModelInterface xtmi = ((XHIBITTableModelInterface) xt.getModel());
	    	// Assume that xtmi instanceof DisposalFilterModel
	   		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
        	((OffenceTableModel) dfm.getModel()).setData(criminalResults);
            if (selectedPanel.equals(criminalPanel)) {
                this.selectedList = criminalResults;
                this.selectedModel = criminalTableModel; //added
            }
        } else {
            criminalTableModel = new OffenceTableModel(criminalResults);
            criminalTableModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                    // Data has changed in table so enable the Save button.
                    modified();
                }
            });
        }

        // Unrelated data
        if (unrelatedResults != null && unrelatedPanel != null
                && unrelatedPanel.getOffenceTable().getModel() instanceof XHIBITTableModelInterface) {
        	
        	XTable xt = unrelatedPanel.getOffenceTable();
	    	XHIBITTableModelInterface xtmi = ((XHIBITTableModelInterface) xt.getModel());
	    	// Assume that xtmi instanceof DisposalFilterModel
	   		DisposalFilterModel dfm = (DisposalFilterModel) xtmi;
        	((UnrelatedDisposalTableModel) dfm.getModel()).setData(unrelatedResults);
            if (selectedPanel.equals(unrelatedPanel)) {
                this.selectedList = unrelatedResults;
                //this.selectedModel = unrelatedTableModel; //added
            }
        } else {
            if (unrelatedResults != null) {
                unrelatedTableModel = new UnrelatedDisposalTableModel(unrelatedResults);
                unrelatedTableModel.addTableModelListener(new TableModelListener() {
                    public void tableChanged(TableModelEvent tme) {
                        // Data has changed in table so enable the Save
                        // button.
                        modified();
                    }
                });
            }
        }
        
        // Disposals for drop down
        /* reset Filters for indictments and s41s */
        if (resetFilters) {
            // indictments
        	log.debug("resetFilters true");
        	disposalFilterSelectionModel = new DisposalFilterSelectionModel();
        	
        	Collection<DefendantValue> defendants = ccv.getAllDefendants();
        	List<DefendantValue> defList = new ArrayList<DefendantValue>(defendants);
        	Set<String> indictmentsDefendants = new TreeSet<String>();
        	
        	for (int i = 0; i < defList.size(); i++) {
        		DefendantValue dv = (DefendantValue) defList.get(i);
        		indictmentsDefendants.add(resultsHelper.getName(dv));
        	}
        	//below getAllDefendants returns collection of DefendantValues 
        	//and can't be converted to strings
        	//disposalFilterSelectionModel.setDefendants(ccv.getAllDefendants());
        	disposalFilterSelectionModel.setDefendants(indictmentsDefendants);
        	
        	//TODO only indictment data update because only indictmentModel passed in
        	OffenceTableModel selectedModel = this.selectedModel;
        	if (selectedModel != null) {
        		log.debug("selectedModel is " + selectedModel.toString());
        	} else {
        		log.error("selectedModel is null");
        		//return;
        	} //remove
        	
        	indictmentlFilterModel = new DisposalFilterModel(indictmentTableModel, disposalFilterSelectionModel);
        	indictmentlFilterModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                	log.debug("table changed event for panel " + selectedPanel);
                    if (indictmentsPanel != null) {
                        getIndictmentsPanel().getOffenceTable().clearSelection(); 
                        stepUpdateViewState();
                    } 
                }//indictmentTableModel
            });
            
            //added
            s41FilterModel = new DisposalFilterModel(section41TableModel, disposalFilterSelectionModel);
            s41FilterModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                	log.debug("table changed event for panel " + selectedPanel);
                    if (section41sPanel != null) {
                        getSection41sPanel().getOffenceTable().clearSelection();
                        stepUpdateViewState();
                    } 
                }//s41TableModel
            });
            
            committalFilterModel = new DisposalFilterModel(committalsTableModel, disposalFilterSelectionModel);
            committalFilterModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                	log.debug("table changed event for panel " + selectedPanel);
                    if (committalsPanel != null) {
                        getCommittalsPanel().getOffenceTable().clearSelection(); 
                        stepUpdateViewState();
                    } 
                }//committalsTableModel
            });
            
            breachFilterModel = new DisposalFilterModel(breachesTableModel, disposalFilterSelectionModel);
            breachFilterModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                	log.debug("table changed event for panel " + selectedPanel);
                    if (breachesPanel != null) {
                        getBreachesPanel().getOffenceTable().clearSelection(); 
                        stepUpdateViewState();
                    } 
                }//breachesTableModel
            });
            
            bailActFilterModel = new DisposalFilterModel(bailActTableModel, disposalFilterSelectionModel);
            bailActFilterModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                	log.debug("table changed event for panel " + selectedPanel);
                    if (bailActPanel != null) {
                        getBailActPanel().getOffenceTable().clearSelection(); 
                        stepUpdateViewState();
                    } 
                }//bailTableModel
            });
            
            criminalFilterModel = new DisposalFilterModel(criminalTableModel, disposalFilterSelectionModel);
            criminalFilterModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                	log.debug("table changed event for panel " + selectedPanel);
                    if (criminalPanel != null) {
                        getCriminalPanel().getOffenceTable().clearSelection(); 
                        stepUpdateViewState();
                    } 
                }//criminalTableModel
            });
        	
            unrelatedFilterModel = new DisposalFilterModel(unrelatedTableModel, disposalFilterSelectionModel);
            unrelatedFilterModel.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent tme) {
                	log.debug("table changed event for panel " + selectedPanel);
                    if (unrelatedPanel != null) {
                        getUnrelatedPanel().getOffenceTable().clearSelection(); 
                        stepUpdateViewState();
                    }
                }//unrealtedTableModel
            });
            //end added
        }// end of reset filters 
        else {
        	log.debug("reset filter false");
        }
        //Missing set filter models?
    }

    public List getSelectedList() {
        return selectedList;
    }

    // Get Table models
    public OffenceTableModel getIndictmentTableModel() {
        return indictmentTableModel;
    }

    public OffenceTableModel getSection41TableModel() {
        return section41TableModel;
    }

    public OffenceTableModel getCommittalsTableModel() {
        return committalsTableModel;
    }

    public OffenceTableModel getBreachesTableModel() {
        return breachesTableModel;
    }
    
    public OffenceTableModel getBailActTableModel() {
        return bailActTableModel;
    }
    public OffenceTableModel getCriminalTableModel() {
        return criminalTableModel;
    }

    public UnrelatedDisposalTableModel getUnrelatedTableModel() {
        return unrelatedTableModel;
    }

    public void stepUpdateViewState() { // update all tabs here?
    	log.debug("[DisposalController] stepUpdateViewState");
        selectedPanel.enableActions();
        
        //enableSelectAll((selectedTab == INDICTMENTS_TAB) && (indictmentRowCount > 0));
        enableSelectAll((indictmentRowCount > 0));
    }

    public void stepDeactivate() throws CSRecoverableException {
        DisposalHelper.disableDisposalActions(xac);
    }

    public void stepValidate() throws CSRecoverableException, CSValidationException {
        if (getModified()) {
            int rc = JOptionPane.showConfirmDialog(xac, getResource("Disposal.QuerySave.Message"),
                    getResource("Disposal.QuerySave.Title"), JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (rc == JOptionPane.YES_OPTION) {
                save();
            } else if (rc == JOptionPane.NO_OPTION) {
                setModified(false);
            } else {
                throw new UserCancelException();
            }
        }
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    private String getResource(final String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.Disposals, key);
    }

    private String getActionResource(final String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.XhibitActionResources, key);
    }
    
    private JPanel getButtonPanel() {
    	if (buttonPanel == null) {
    		buttonPanel = new JPanel();
    		buttonPanel.setLayout(new BorderLayout());
    		
    		JPanel innerPanel = new JPanel();
    		innerPanel.setLayout(new BorderLayout());
    		buttonPanel.add(innerPanel, BorderLayout.EAST);
    		
    		btnExitCase = new JButton("Exit Case");
    		btnExitCase.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						xac.close();
						xac.setCaseChargesDisposalsOpened(false);
					} catch (CSRecoverableException e1) {
						log.debug("Cancel pressed.");
					}
				}
    		});
    		
    		innerPanel.add(btnExitCase, BorderLayout.CENTER);	
    	}
    	
    	return buttonPanel;
    }
}
