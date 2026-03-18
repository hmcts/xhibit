package uk.gov.courtservice.xhibit.client.listings.casesummary;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DarRetentionPolicyComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseSummaryLinkingValue;
import uk.gov.courtservice.xhibit.business.vos.services.caseprosecutoragency.CaseProsecutorAgencyValue;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseStatus;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CaseSummaryPanel extends XPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private XDialog parent = null;
	private CaseSummaryModel model = null;
	private final Logger log = CSServices.getLogger(getClass());
	private CaseSummaryGeneralPanel generalPanel;
	private CaseSummaryDefendantPanel defendantPanel;
	private CaseSummaryAssociatedCasesPanel associatedCasesPanel;
	private CaseSummaryAppellantPanel appellantPanel;
	private CaseSummaryListingHistoryPanel listingHistoryPanel;
	private ReloadCaseSummaryAction reloadCaseSummaryAction = new ReloadCaseSummaryAction();
	private JTabbedPane tabbedPane;
	
	
	public CaseSummaryPanel(XDialog parent, CaseSummaryModel model) throws CSRecoverableException
	{
		this.parent = parent;
		this.model = model;
		stepInitialise();
		jbInit();

	}

	private void jbInit() {
			
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));	
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(800, 600));
		scrollPane.setViewportView(tabbedPane);
				
		displayTabs();
	}
		
	private void displayTabs() {
		// If this is a refresh then make sure we are displaying the General Tab 
		if (tabbedPane.getTabCount() > 0) {
			tabbedPane.setSelectedIndex(0);
		}
		
		// Show / Hide the required tabs
		initTab(getResourceBundle("generalTabName"), getGeneralPanel(), true);
		initTab(getResourceBundle("defendantsTabName"), getDefendantPanel(), model.isDefendantTabRequired());
		initTab(getResourceBundle("associatedCasesTabName"), getAssociatedCasesPanel(), true);
		initTab(getResourceBundle("listingHistoryTabName"), getListingHistoryPanel(), true);
		initTab(getResourceBundle("appellantTabName"), getAppellantPanel(), model.isAppellantTabRequired());
		
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				((CaseSummaryTab) tabbedPane.getSelectedComponent()).loadDataFirstTime();
			}
			
		} );
	}
	
	private CaseSummaryGeneralPanel getGeneralPanel() {
		if (generalPanel == null) {
			generalPanel = new CaseSummaryGeneralPanel(model);
		}
		return generalPanel;
	}
	
	private CaseSummaryDefendantPanel getDefendantPanel() {
		if (defendantPanel == null) {
			defendantPanel = new CaseSummaryDefendantPanel(model);
		}
		return defendantPanel;
	}
	
	private CaseSummaryAssociatedCasesPanel getAssociatedCasesPanel() {
		if (associatedCasesPanel == null) {
			associatedCasesPanel = new CaseSummaryAssociatedCasesPanel(parent, model, reloadCaseSummaryAction);
		}
		return associatedCasesPanel;
	}
	
	private CaseSummaryListingHistoryPanel getListingHistoryPanel() {
		if (listingHistoryPanel == null) {
			listingHistoryPanel = new CaseSummaryListingHistoryPanel(parent, model);
		}
		return listingHistoryPanel;
	}
	
	private CaseSummaryAppellantPanel getAppellantPanel() { 
		if (appellantPanel == null) {
			appellantPanel = new CaseSummaryAppellantPanel(model);
		}
		return appellantPanel;
	}
	
	private List<Component> getTabsDisplayed() {
		return Arrays.asList(tabbedPane.getComponents());
	}
	
	private void initTab(String tabName, JPanel tabPanel, boolean showTab) {
		List<Component> tabArray = getTabsDisplayed();
		// If the tab is no longer required, but is currently displayed then remove it
		if (!showTab && tabArray.contains(tabPanel)) {
			tabbedPane.remove(tabPanel);
		}
		// If the tab is required, but not displayed then add it
		if (showTab && !tabArray.contains(tabPanel)) {
			tabbedPane.addTab(tabName, null, tabPanel, null);
		}
	}
			
	@Override
	public void stepInitialise() throws CSUnrecoverableException  {
		populateModel();
	}

	private void populateModel() {
		try
		{
			if (model.getCaseId() > 0) {
				CaseListingEntryComplexValue caseListingEntry = XhibitDelegateHelper.getListingsDelegate().getCaseListingEntryByCaseIdAndCourtId(model.getCaseId(), 
						XhibitSingleton.getInstance().getCourtId());
				model.setCaseListingEntry(caseListingEntry);
				
				//Retreive prosecutors for the case here as they are used in multiple tabs
				@SuppressWarnings("unchecked")
				List<CaseProsecutorAgencyValue> caseProsecutors = XhibitDelegateHelper.getCaseProsecutorAgencyDelegate().findByCaseId(model.getCaseId());
				model.setCaseProsecutors(caseProsecutors);
				
				// Retrieve the live case status
				String liveStatus = XhibitDelegateHelper.getCaseDelegate().determineCaseStatus(model.getCaseId());
				model.setLiveStatus(liveStatus);
				
				// Set Case Status
				model.setCaseStatus(new CaseStatus());
				CaseBasicValue caseBasicValue = model.getCaseListingEntry().getCaseBasicValue();
				log.debug("caseType from VO is " + caseBasicValue.getCaseType());
				if (caseBasicValue.getCaseType().equalsIgnoreCase("A")
						&& (caseBasicValue.getCaseSubType() == null || !caseBasicValue.getCaseSubType().equals("O"))) {
					model.getCaseStatus().setCaseType(CaseType.APPEAL);
				}
				if (caseBasicValue.getCaseType().equalsIgnoreCase("S")) {
					model.getCaseStatus().setCaseType(CaseType.SENTENCE);
				}
				if (caseBasicValue.getCaseType().equalsIgnoreCase("T")) {
					model.getCaseStatus().setCaseType(CaseType.TRIAL);
				}
				if (caseBasicValue.getCaseType().equalsIgnoreCase("A") && caseBasicValue.getCaseSubType() != null
						&& caseBasicValue.getCaseSubType().equals("O")) {
					model.getCaseStatus().setCaseType(CaseType.MISC);
				}
				
				CourtSiteBasicValue courtSite = getCourtSite(caseBasicValue.getCourtIdReceivingSite());
				model.setCourtSite(courtSite);
				
				DarRetentionPolicyComplexValue dartsRetentionPolicy = null;
				if (caseBasicValue.getDarRetentionPolicyId() != null) {
					dartsRetentionPolicy = XhibitDelegateHelper.getCaseDelegate().findDartsRetentionPolicy(model.getCaseId(), caseBasicValue.getDarRetentionPolicyId());
				}
				model.setDartsRetentionPolicy(dartsRetentionPolicy);
				model.setDvrReleaseDate(XhibitDelegateHelper.getCaseDelegate().findDvrReleaseDate());
			}
			else {
				throw new CSUnrecoverableException("Case ID not provided");
			}
		} catch (ListingsControllerException ex) {
			throw new CSUnrecoverableException("Case could not be found", ex);
		}
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();
	}

	private void moveModelToScreen() {
		// Set the screen title
		CaseBasicValue caseBasicValue = model.getCaseListingEntry().getCaseBasicValue();
		String dialogTitle = getResourceBundle("dialogTitle");
		parent.setTitle(String.format(dialogTitle, caseBasicValue.getCaseType(), caseBasicValue.getCaseNumber()));
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
	}

	protected void reloadScreen() {
		// Reload this model
		populateModel();	
		// Hide / Show the required Tabs
		displayTabs();
		// Refresh this screen
		moveModelToScreen();		
		// Refresh all the displayed Tabs
		for ( Component tab : getTabsDisplayed() ) {
			((CaseSummaryTab) tab).moveModelToScreen();
		}
	}
	
	private String getResourceBundle(String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.CaseSummaryResources, key);
	}
	
	public class ReloadCaseSummaryAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public ReloadCaseSummaryAction()
		{
			super("ViewCaseSummary");
		} 
				
		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			Integer caseId = model.getCaseId();
			
			// Get parent table from where the request originated
			JMenuItem parentMenuItem = (JMenuItem) e.getSource();
			JPopupMenu parentPopupMenu = (JPopupMenu) parentMenuItem.getParent();
			JTable parentTable = (JTable) parentPopupMenu.getInvoker();
						
			// Get the selected object from the table model
			int selectedRow = parentTable.getSelectedRow();
			CaseSummaryTableModel tableModel = (CaseSummaryTableModel) parentTable.getModel();
			Object selectedValue = tableModel.getRow(selectedRow);
			
			// Get the new CaseId
			if (selectedValue instanceof CaseSummaryLinkingValue) {
				caseId = ((CaseSummaryLinkingValue)selectedValue).getCaseId();
			} else {
				throw new IllegalArgumentException("Invalid class: " + selectedValue.getClass().getSimpleName());
			}			
			
			// Only reload the display if we've selected a new case
			if (!model.getCaseId().equals(caseId)) {
				model.setCaseId(caseId);
			
				// Reload the screen using the current model.caseId
				reloadScreen();
			}
		}		
	}
	
	private CourtSiteBasicValue getCourtSite(Integer courtSiteId) {
		CourtSiteBasicValue result = null;
		if (courtSiteId != null) {
			ArrayList<CourtSiteBasicValue> courtSites = ListingDropdownPopulation.getCourtSites();
			if (!courtSites.isEmpty()) {
				for (CourtSiteBasicValue courtSite : courtSites) {
					if (courtSiteId.equals(courtSite.getId())) {
						result = courtSite;
						break;
					}
				}
			}
		}
		return result;
	}
}
