package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLogDialog;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.UnknownCaseTypeException;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.PrintFunction;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.SeqNoHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

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
 * @author Simon Gilmore
 * @version $Revision: 1.85 $
 */
public class ChargesController extends XPanel implements PrintFunction {

	private static final long serialVersionUID = 1L;

	public static final int INDICTMENTS_TAB = 0;
	public static final int SECTION41S_TAB = 1;
	public static final int COMMITTALS_TAB = 2;
	public static final int BREACHES_TAB = 3;
	public static final int FAIL2APPEAR_TAB = 4;
	public static final int APPEALOFFENCES_TAB = 5;

	private static final Logger log = CSServices.getLogger(ChargesController.class);

	private int currentCourt = -1;
	private int currentCourtRoom = -1;

	private XhibitApplicationController xac = null;
	private ApplicationCaseModel applicationCaseModel = null;
	private List chargesActions = null;
	private ChargeCompositeValue ccv = null;

	private JSplitPane jSplitPane = null;
	private JSplitPane rightWindowSplitPane = null;
	private JPanel mainPanel = null;
	private JTabbedPane chargesTabbedPane = null;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel amendmentLogPanel = null;
	private JPanel indictmentsPanel = null;
	private IndictmentSummaryPanel selectedIndictmentPanel = null;
	private OffencePanel section41sPanel = null;
	private OffencePanel committalsPanel = null;
	private OffencePanel appealOffencesPanel = null;
	private JPanel breachesPanel = null;
	private BreachSummaryPanel selectedBreachPanel = null;
	private JPanel breachDetailsHolderPanel = null;
	private JPanel breachDetailsPanel = null;
	private BailActOffencePanel bailActPanel = null;

	private String defaultError = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"warningDialog.defaultError");

	/*
	 * Charges on the Case, Section41 and Commital Offences all exist on a
	 * single Section41 or Commital Charge. Indictments, Breaches and
	 * Fail2Appear have mutliple Charges. Fail2Appear Charges are Breaches that
	 * only have one Bail Act Offence associated with them
	 */
	private List<ChargeValue> indictmentsList = new ArrayList<ChargeValue>();
	private List<ChargeValue> breachesList = new ArrayList<ChargeValue>();
	private List<ChargeValue> fail2AppearList = new ArrayList<ChargeValue>();
	private ChargeValue section41Charge = null;
	private ChargeValue appealOffenceCharge = null;
	private ChargeValue committalForSentenceCharge = null;

	private Map<Integer, JPanel> indictmentsPanelsMap = null;
	private Map<Integer, JPanel> breachesPanelsMap = null;

	/* The specific Charge, Offence and Defendant value selected */
	private ChargesSelectionModel currentSM = null;
	private ChargesSelectionModel indictmentSM = null;
	private ChargesSelectionModel section41SM = null;
	private ChargesSelectionModel appealOffencesSM = null;
	private ChargesSelectionModel committalSM = null;
	private ChargesSelectionModel breachSM = null;
	private ChargesSelectionModel fail2AppearSM = null;
	private ChargesSelectionModel emptySM = new ChargesSelectionModel();

	private ChargesControllerModel model = new ChargesControllerModel();

	private JPanel indPanelToActivate = null;

	private int selectedTab = -1;
	private int previouslySelectedTab = INDICTMENTS_TAB;

	private boolean editMode = false;
	private boolean isSelectedIndexInitialised = false;
	boolean isThisIndictmentAJoinder = false;

	private boolean accessedFromCaseCreateAmend = false;
	private boolean accessedFromAddIndictmentCase = false;

	public boolean isAccessedFromAddIndictmentCase() {
		return accessedFromAddIndictmentCase;
	}

	public void setAccessedFromAddIndictmentCase(boolean accessedFromAddIndictmentCase) {
		this.accessedFromAddIndictmentCase = accessedFromAddIndictmentCase;
	}

	public ChargesController(ApplicationCaseModel acm) throws CSRecoverableException {
		super();
		applicationCaseModel = acm;
		model.setACM(acm);
		stepInitialise();
		jbInit();
		// to properly allow switching between sentence and charges screen if
		// coming from case create (ctx-1968)
		if (xac.isCaseChargesDisposalsOpened()) {
			loadCharges(true);
		} else {
			loadCharges();
		}
		rightWindowSplitPane.setResizeWeight(1.0);
	}

	// Secondary charges controller constructor (overload not used) to access
	// charges menu from
	// case create.
	public ChargesController(ApplicationCaseModel acm, boolean overload) throws CSRecoverableException {
		super();
		accessedFromCaseCreateAmend = true;
		applicationCaseModel = acm;
		model.setACM(acm);
		this.editMode = true; // needed?
		stepInitialise();
		jbInit();
		loadCharges(true);
		rightWindowSplitPane.setResizeWeight(1.0);
	}

	private void jbInit() {
		this.setLayout(new BorderLayout());
		this.add(getMainPanel(), BorderLayout.CENTER);

		getMainPanel().add(getSplitPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(gridBagLayout1);
		}

		return mainPanel;
	}

	private JSplitPane getRightWindowSplitPane() {
		if (rightWindowSplitPane == null) {
			rightWindowSplitPane = new JSplitPane();
			rightWindowSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			rightWindowSplitPane.setDividerLocation(1.0);
			rightWindowSplitPane.setTopComponent(getAmendmentLogPanel());
			rightWindowSplitPane.setBottomComponent(getBreachDetailsHolderPanel());
		}

		return rightWindowSplitPane;
	}

	private JSplitPane getSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			jSplitPane.setResizeWeight(0.5);
			jSplitPane.setDividerLocation(0.5);
		}
		return jSplitPane;
	}

	private JTabbedPane getChargesTabbedPane() {
		if (chargesTabbedPane == null) {
			chargesTabbedPane = new JTabbedPane();
			chargesTabbedPane.setPreferredSize(new Dimension(300, 400));

			String indictmentText = getResource("tabIndictments");
			String section41sText = getResource("tabSection41s");
			String committalsText = getResource("tabCommittals");
			String breachesText = getResource("tabBreaches");
			String fail2AppearText = getResource("tabFail2Appear");
			String appealOffencesText = getResource("tabAppealOffences");

			chargesTabbedPane.addTab(indictmentText, new JScrollPane(getIndictmentsPanel()));
			chargesTabbedPane.addTab(section41sText, getSection41sPanel());
			chargesTabbedPane.addTab(committalsText, getCommittalsPanel());
			chargesTabbedPane.addTab(breachesText, new JScrollPane(getBreachesPanel()));
			// chargesTabbedPane.addTab(fail2AppearText, new
			// JScrollPane(getBailActPanel()));
			chargesTabbedPane.addTab(fail2AppearText, getBailActPanel());
			chargesTabbedPane.addTab(appealOffencesText, getAppealOffencesPanel());

			chargesTabbedPane.setToolTipTextAt(INDICTMENTS_TAB, indictmentText);
			chargesTabbedPane.setToolTipTextAt(SECTION41S_TAB, section41sText);
			chargesTabbedPane.setToolTipTextAt(COMMITTALS_TAB, committalsText);
			chargesTabbedPane.setToolTipTextAt(BREACHES_TAB, breachesText);
			chargesTabbedPane.setToolTipTextAt(FAIL2APPEAR_TAB, fail2AppearText);
			chargesTabbedPane.setToolTipTextAt(APPEALOFFENCES_TAB, appealOffencesText);

			chargesTabbedPane.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					actionTabSelectionChanged();
				}
			});
		}

		// We want to avoid explicitly setting the SelectedIndex except first
		// time thru
		// This is to fix an issue where the COMMITTALS_TAB was being set in
		// stepActivate
		// and returning the user to the Committals tab following a Breach
		// operation

		try {
			if (!isSelectedIndexInitialised && applicationCaseModel != null
					&& applicationCaseModel.getScheduledHearingValue() != null) {
				// Must set this first as setting the index triggers re-entrancy
				// due to a state change
				isSelectedIndexInitialised = true;

				ScheduledHearingValue shv = applicationCaseModel.getScheduledHearingValue();

				if (CaseTypeHelper.isSentence_CaseType(shv)) {
					chargesTabbedPane.setSelectedIndex(COMMITTALS_TAB);
					if (accessedFromCaseCreateAmend) { // ctx-1610
						if (applicationCaseModel.getScheduledHearingValue().getCaseBasicValue().getReceiptType()
								.equals("CB")
								|| applicationCaseModel.getScheduledHearingValue().getCaseBasicValue().getReceiptType()
										.equals("BB")) {
							chargesTabbedPane.setSelectedIndex(BREACHES_TAB);
						} else {
							chargesTabbedPane.setSelectedIndex(COMMITTALS_TAB);
						}
					} else {
						chargesTabbedPane.setSelectedIndex(COMMITTALS_TAB);
					}

				} else if (CaseTypeHelper.isCriminalAppeal_CaseType(shv)) {
					if (accessedFromCaseCreateAmend || xac.isCaseChargesDisposalsOpened()) {
						chargesTabbedPane.setSelectedIndex(APPEALOFFENCES_TAB);
					} else {
						chargesTabbedPane.setSelectedIndex(INDICTMENTS_TAB);
					}
				} else if (CaseTypeHelper.isTrial_CaseType(shv)) {
					chargesTabbedPane.setSelectedIndex(SECTION41S_TAB);
				} else {
					chargesTabbedPane.setSelectedIndex(INDICTMENTS_TAB);
				}
			}
		} catch (CSRecoverableException cre) {
			log.error("Error: Initialisation of chargesTabbedPane.setSelectedIndex failed");
			chargesTabbedPane.setSelectedIndex(INDICTMENTS_TAB);
			isSelectedIndexInitialised = true;
		}
		return chargesTabbedPane;
	}

	void actionTabSelectionChanged() {
		selectedTab = getChargesTabbedPane().getSelectedIndex();
		previouslySelectedTab = selectedTab;
		// XHIBITConstant.debug("tab changed! Tab = " + selectedTab);

		switch (selectedTab) {
		case INDICTMENTS_TAB:
			if (selectedIndictmentPanel != null) {
				currentSM = selectedIndictmentPanel.getChargeSelectionModel();
			} else {
				currentSM = emptySM;
			}
			break;

		case SECTION41S_TAB:
			currentSM = section41SM;
			break;

		case APPEALOFFENCES_TAB:
			currentSM = appealOffencesSM;
			break;

		case COMMITTALS_TAB:
			currentSM = committalSM;
			break;

		case BREACHES_TAB:
			if (selectedBreachPanel != null) {
				currentSM = selectedBreachPanel.getChargeSelectionModel();
			} else {
				currentSM = emptySM;
			}
			break;

		case FAIL2APPEAR_TAB:
			if (fail2AppearSM != null) {
				currentSM = fail2AppearSM;
			} else {
				currentSM = emptySM;
			}
			break;
		}

		model.setSelectedChargeType(selectedTab);
		stepUpdateViewState();
	}

	private JPanel getAmendmentLogPanel() {
		if (amendmentLogPanel == null) {
			// The ChargeCompositeValue ccv could be null if there are no
			// charges on the case.
			if (ccv == null) {
				amendmentLogPanel = new AmendmentLogPanel();
			} else {
				amendmentLogPanel = new AmendmentLogPanel(ccv.getChargeLogItems());
			}
			amendmentLogPanel.setPreferredSize(new Dimension(350, 150));
		}
		return amendmentLogPanel;
	}

	private JPanel getBreachDetailsHolderPanel() {
		if (breachDetailsHolderPanel == null) {
			breachDetailsHolderPanel = new JPanel();
			breachDetailsHolderPanel.setLayout(gridBagLayout1);
			// breachDetailsHolderPanel.setMinimumSize(new Dimension(300,
			// 300));
			// breachDetailsHolderPanel.setPreferredSize(new Dimension(300,
			// 300));

			breachDetailsPanel = new JPanel();

			breachDetailsHolderPanel.add(breachDetailsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return breachDetailsHolderPanel;
	}

	public void loadCharges() throws CSRecoverableException {
		// .loadCharges called from all actions so this will properly switch
		// loadcharges() methods
		// based on whether we're coming from case create/update
		if (xac.isCaseChargesDisposalsOpened()) {
			loadCharges(true);
			return;
		}

		Integer caseID = model.getACM().getCaseId();

		try {
			// Get the charges for the given case id and get the log events.
			ccv = XhibitDelegateHelper.getChargeDelegate().getCharges(caseID, true);
		} catch (Exception e) {
			throw new CSRecoverableException("gui.ChargesController.loadCharges",
					"Exception whilst getting the charge composite value object from the mid tier", e);
		}

		model.setChargeValue(null);
		model.setCCV(ccv);

		processCharges();
		// Create hashmap containing lists of used sequence numbers (only
		// containing non-obsolete charges/offences)
		HashMap<Integer, List> activeDefOnCaseSeqNosMap = SeqNoHelper.constructSequenceNumberMap(ccv.getCharges(),
				ccv.getAllDefendants());
		// Make active sequenceNumber map available via model
		xac.getApplicationCaseModel().setDefOnCaseSeqNosMap(activeDefOnCaseSeqNosMap);

		// Now we need to construct the Hashmap for all charges/offences,
		// including those which are obsolete
		Collection allCharges = null;
		try {
			allCharges = XhibitDelegateHelper.getChargeDelegate().getChargesList(caseID);
		} catch (Exception e) {
			throw new CSRecoverableException("gui.ChargesController.loadCharges",
					"Exception whilst getting the chargeList from the mid tier", e);
		}

		HashMap<Integer, List> allDefOnCaseSeqNosMap = SeqNoHelper.constructSequenceNumberMap(allCharges,
				ccv.getAllDefendants());
		// Make hashmap available via chargeController model
		model.setAllDefOnCaseSeqNosMap(allDefOnCaseSeqNosMap);

		// Organize Sequence of Breaches
		orderBreachesList();

		rightWindowSplitPane = null;

		indictmentsPanel = null;
		section41sPanel = null;
		appealOffencesPanel = null;
		committalsPanel = null;
		breachesPanel = null;
		bailActPanel = null;
		indictmentsPanelsMap = new TreeMap<Integer, JPanel>();
		breachesPanelsMap = new TreeMap<Integer, JPanel>();

		selectedBreachPanel = null;
		selectedIndictmentPanel = null;

		chargesTabbedPane = null;
		amendmentLogPanel = null;
		breachDetailsPanel = null;
		breachDetailsHolderPanel = null;

		currentSM = new ChargesSelectionModel();
		indictmentSM = new ChargesSelectionModel();
		section41SM = new ChargesSelectionModel();
		appealOffencesSM = new ChargesSelectionModel();
		committalSM = new ChargesSelectionModel();
		breachSM = new ChargesSelectionModel();
		fail2AppearSM = new ChargesSelectionModel();

		// Make panel to hold charges Tabbed pane and exit case button, ctx-1968
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		leftPanel.setPreferredSize(getChargesTabbedPane().getPreferredSize());
		leftPanel.add(getChargesTabbedPane(), new GridBagConstraints(0, 0, 1, 1, 1, 0.95, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if (accessedFromCaseCreateAmend || accessedFromAddIndictmentCase || xac.isCaseChargesDisposalsOpened()) {
			leftPanel.add(getExitCaseButton(), new GridBagConstraints(0, 1, 1, 1, 1, 0.05, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}

		getSplitPane().setLeftComponent(leftPanel);

		getSplitPane().setRightComponent(getRightWindowSplitPane());
		getRightWindowSplitPane().setResizeWeight(1.0);

		if (indictmentsList.size() > 1) {
			setChildrenEnabled(getIndictmentsPanel(), false);
		}
		if (breachesList.size() > 1) {
			setChildrenEnabled(getBreachesPanel(), false);
		}

		if (indPanelToActivate != null) {
			chargePanelSelectionChanged(indPanelToActivate, indictmentsPanelsMap, true);
		}

		selectedTab = previouslySelectedTab;
		model.setSelectedChargeType(selectedTab);
		getChargesTabbedPane().setSelectedIndex(selectedTab);

		// set initial tab enabling and update actions
		stepActivate();
		actionTabSelectionChanged();
	}

	// Overloaded loadCharges();
	public void loadCharges(boolean overload) throws CSRecoverableException {
		Integer caseID = model.getACM().getCaseId();

		try {
			// Get the charges for the given case id and get the log events.
			ccv = XhibitDelegateHelper.getChargeDelegate().getCharges(caseID, true);
		} catch (Exception e) {
			throw new CSRecoverableException("gui.ChargesController.loadCharges",
					"Exception whilst getting the charge composite value object from the mid tier", e);
		}

		model.setChargeValue(null);
		model.setCCV(ccv);

		processCharges();

		// Now we need to construct the Hashmap for all charges/offences,
		// including those which are obsolete
		Collection allCharges = null;
		try {
			allCharges = XhibitDelegateHelper.getChargeDelegate().getChargesList(caseID);
		} catch (Exception e) {
			throw new CSRecoverableException("gui.ChargesController.loadCharges",
					"Exception whilst getting the chargeList from the mid tier", e);
		}

		HashMap<Integer, List> allDefOnCaseSeqNosMap = SeqNoHelper.constructSequenceNumberMap(allCharges,
				ccv.getAllDefendants());
		// Make hashmap available via chargeController model
		model.setAllDefOnCaseSeqNosMap(allDefOnCaseSeqNosMap);

		// Organize Sequence of Breaches
		orderBreachesList();

		rightWindowSplitPane = null;

		indictmentsPanel = null;
		section41sPanel = null;
		appealOffencesPanel = null;
		committalsPanel = null;
		breachesPanel = null;
		bailActPanel = null;
		indictmentsPanelsMap = new TreeMap<Integer, JPanel>();
		breachesPanelsMap = new TreeMap<Integer, JPanel>();

		selectedBreachPanel = null;
		selectedIndictmentPanel = null;

		chargesTabbedPane = null;
		amendmentLogPanel = null;
		breachDetailsPanel = null;
		breachDetailsHolderPanel = null;

		currentSM = new ChargesSelectionModel();
		indictmentSM = new ChargesSelectionModel();
		section41SM = new ChargesSelectionModel();
		appealOffencesSM = new ChargesSelectionModel();
		committalSM = new ChargesSelectionModel();
		breachSM = new ChargesSelectionModel();
		fail2AppearSM = new ChargesSelectionModel();

		// Make panel to hold charges Tabbed pane and exit case button, ctx-1968
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		leftPanel.setPreferredSize(getChargesTabbedPane().getPreferredSize());
		leftPanel.add(getChargesTabbedPane(), new GridBagConstraints(0, 0, 1, 1, 1, 0.95, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if (accessedFromCaseCreateAmend || accessedFromAddIndictmentCase || xac.isCaseChargesDisposalsOpened()) {
			leftPanel.add(getExitCaseButton(), new GridBagConstraints(0, 1, 1, 1, 1, 0.05, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}

		getSplitPane().setLeftComponent(leftPanel);
		getSplitPane().setRightComponent(getRightWindowSplitPane());
		getRightWindowSplitPane().setResizeWeight(1.0);

		if (indictmentsList.size() > 1) {
			setChildrenEnabled(getIndictmentsPanel(), false);
		}
		if (breachesList.size() > 1) {
			setChildrenEnabled(getBreachesPanel(), false);
		}

		if (indPanelToActivate != null) {
			chargePanelSelectionChanged(indPanelToActivate, indictmentsPanelsMap, true);
		}

		selectedTab = previouslySelectedTab;
		model.setSelectedChargeType(selectedTab);
		getChargesTabbedPane().setSelectedIndex(selectedTab);

		// set initial tab enabling and update actions
		stepActivate();
	}

	private JPanel getIndictmentsPanel() {
		ChargeValue cv = null;
		JPanel p = null;
		Dimension panelDimension = null;
		double overallHeight = 0;

		if (indictmentsPanel == null) {
			indictmentsPanel = new JPanel();
			indictmentsPanel.setLayout(gridBagLayout1);
			// indictmentsPanel.setMinimumSize(new Dimension(350, 500));
			// indictmentsPanel.setPreferredSize(new Dimension(350, 500));

			Iterator i = indictmentsList.iterator();
			while (i.hasNext()) {
				cv = (ChargeValue) i.next();
				indictmentSM.setChargeValue(cv);
				try {
					p = new IndictmentSummaryPanel(this, indictmentSM.clone());
				} catch (CloneNotSupportedException cnse) {
					XHIBITErrorHandler.handleError(cnse);
					throw new CSUnrecoverableException("Error cloning ChargesSelectionModel", cnse);
				}

				indictmentsPanelsMap.put(cv.getCrestChargeSeqNo(), p);

				p.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						actionMousePressed(e, indictmentsPanelsMap);
					}
				});
				panelDimension = p.getMinimumSize();
				overallHeight += panelDimension.getHeight();
			}

			int row = 0;
			Collection c = indictmentsPanelsMap.values();
			Iterator it = c.iterator();
			boolean isFirst = true;
			while (it.hasNext()) {
				JPanel currentPanel = (JPanel) it.next();
				if (isFirst) {
					indPanelToActivate = currentPanel;
					isFirst = false;
				}
				indictmentsPanel.add(currentPanel, new GridBagConstraints(0, row, 1, 1, 1.0, 1.0,
						GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
				row++;
			}

			indictmentsPanel.setPreferredSize(new Dimension(300, new Double(overallHeight).intValue()));

		}
		return indictmentsPanel;
	}

	protected void resizeIndictmentPanel() {
		JPanel p;
		Dimension d;
		double height = 0;
		Collection c = indictmentsPanelsMap.values();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			p = (JPanel) it.next();
			d = p.getMinimumSize();
			height += d.getHeight();
		}
		indictmentsPanel.setPreferredSize(new Dimension(300, new Double(height).intValue()));
	}

	private OffencePanel getSection41sPanel() {
		if (section41sPanel == null) {
			section41SM.setChargeValue(section41Charge);
			section41sPanel = new OffencePanel(this, section41SM);
		}
		return section41sPanel;
	}

	private OffencePanel getAppealOffencesPanel() {
		if (appealOffencesPanel == null) {
			appealOffencesSM.setChargeValue(appealOffenceCharge);
			appealOffencesPanel = new OffencePanel(this, appealOffencesSM);
		}
		return appealOffencesPanel;
	}

	private OffencePanel getCommittalsPanel() {
		if (committalsPanel == null) {
			committalSM.setChargeValue(committalForSentenceCharge);
			committalsPanel = new OffencePanel(this, committalSM);
		}
		return committalsPanel;
	}

	private JPanel getBreachesPanel() {
		ChargeValue cv = null;
		JPanel p = null;
		Dimension panelDimension = null;
		double overallHeight = 0;

		if (breachesPanel == null) {
			breachesPanel = new JPanel();
			breachesPanel.setLayout(gridBagLayout1);
			// breachesPanel.setMinimumSize(new Dimension(300, 500));

			Iterator i = breachesList.iterator();
			int count = 0;
			while (i.hasNext()) {
				cv = (ChargeValue) i.next();
				// Integer seqNo = cv.getCrestChargeSeqNo();
				log.debug("getBreachesPanel print start");
				log.debug("cv.getDefendantID(): " + cv.getDefendantID());
				log.debug("cv.getDefendantValue: " + getDefendantValue(cv.getDefendantID()));
				log.debug("getBreachesPanel print end");
				breachSM.setChargeValue(cv);
				breachSM.setDefendantValue(getDefendantValue(cv.getDefendantID()));
				try {
					p = new BreachSummaryPanel(this, breachSM.clone());
				} catch (CloneNotSupportedException cnse) {
					XHIBITErrorHandler.handleError(cnse);
					throw new CSUnrecoverableException("Error cloning ChargesSelectionModel", cnse);
				}

				// breachesPanelsMap.put(seqNo, p);
				// Integer chargeId = cv.getChargeID();
				// 55951 ordering breaches
				breachesPanelsMap.put(new Integer(count), p);

				p.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						actionMousePressed(e, breachesPanelsMap);
					}
				});

				panelDimension = p.getMinimumSize();
				overallHeight += panelDimension.getHeight();
				count++;
			}

			int row = 0;
			Collection c = breachesPanelsMap.values();
			Iterator it = c.iterator();
			while (it.hasNext()) {
				breachesPanel.add((JPanel) it.next(), new GridBagConstraints(0, row, 1, 1, 1.0, 1.0,
						GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
				row++;
			}

			breachesPanel.setPreferredSize(new Dimension(300, new Double(overallHeight).intValue()));
		}
		return breachesPanel;
	}

	protected void resizeBreachPanel() {
		JPanel p;
		Dimension d;
		double height = 0;
		Collection c = breachesPanelsMap.values();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			p = (JPanel) it.next();
			d = p.getMinimumSize();
			height += d.getHeight();
		}
		breachesPanel.setPreferredSize(new Dimension(300, new Double(height).intValue()));
	}

	private BailActOffencePanel getBailActPanel() {
		if (bailActPanel == null) {
			// fail2AppearSM.setChargeValue(fail2AppearSM);
			if (fail2AppearList.iterator().hasNext()) { // fail2appear can only
														// contain one offence
				ChargeValue cv = fail2AppearList.iterator().next();
				fail2AppearSM.setChargeValue(cv);
				fail2AppearSM.setDefendantValue(getDefendantValue(fail2AppearSM.getChargeValue().getDefendantID()));
			}

			bailActPanel = new BailActOffencePanel(this, fail2AppearSM);
		}
		return bailActPanel;
	}

	private void actionMousePressed(MouseEvent e, Map map) {
		log.debug("actionMousePressed - Begin");
		Component c = e.getComponent();
		chargePanelSelectionChanged(c, map, true);
	}

	/**
	 * If Component "c" exists in "map", then enable it and disable the other
	 * panels in the map.
	 *
	 * @param c
	 *            JPanel to enable
	 * @param map
	 *            of JPanels
	 * @param enabledByPanelSelection
	 *            If user clicked the panel (i.e. not the table)
	 */
	protected void chargePanelSelectionChanged(Component c, Map map, boolean enabledByPanelSelection) {
		log.debug("chargePanelSelectionChanged - Begin - enabledByPanelSelection = " + enabledByPanelSelection);
		JPanel panel = null;
		Collection col = map.values();
		Iterator i = col.iterator();
		while (i.hasNext()) {
			panel = (JPanel) i.next();
			if (panel == c) {

				if (panel == selectedIndictmentPanel || panel == selectedBreachPanel) {
					// The panel just clicked on was already selected so
					// there
					// is nothing to do.
					return;
				}

				setChildrenEnabled(panel, true);

				if (panel instanceof IndictmentSummaryPanel) {

					selectedIndictmentPanel = (IndictmentSummaryPanel) panel;

					if (enabledByPanelSelection) {
						selectedIndictmentPanel.getChargeSelectionModel().setOffenceValue(null);
					}
					currentSM = selectedIndictmentPanel.getChargeSelectionModel();
				} else if (panel instanceof BreachSummaryPanel) {
					selectedBreachPanel = (BreachSummaryPanel) panel;
					if (enabledByPanelSelection) {
						selectedBreachPanel.getChargeSelectionModel().setOffenceValue(null);
					}
					currentSM = selectedBreachPanel.getChargeSelectionModel();
				} else {
					CSUnrecoverableException csue = new CSUnrecoverableException(
							"ChargesController: " + "method: actionMousePressed - Unknown JPanel type selected");
					XHIBITErrorHandler.handleError(csue);
				}
			} else {
				setChildrenEnabled(panel, false);
			}
		}
		stepUpdateViewState();
	}

	/**
	 * To enable/disable a container and all of its components.
	 *
	 * @param comp
	 *            the component you wish to enable/disable.
	 * @param enabled
	 *            state to set the component.
	 */
	public static void setChildrenEnabled(Component comp, boolean enabled) {
		comp.setEnabled(enabled);
		if (comp instanceof Container) {
			Container container = (Container) comp;
			for (int idx = 0; idx < container.getComponentCount(); idx++) {
				setChildrenEnabled(container.getComponent(idx), enabled);
			}
		}
	}

	private void processCharges() {
		indictmentsList.clear();
		breachesList.clear();
		fail2AppearList.clear();
		section41Charge = null;
		committalForSentenceCharge = null;
		appealOffenceCharge = null;

		// If the ChargeCompositeValue is null, then there are no charges to
		// process, so return.
		if (ccv == null)
			return;

		Collection charges = ccv.getCharges();
		Iterator iterator = charges.iterator();
		while (iterator.hasNext()) {
			ChargeValue chargeValue = (ChargeValue) iterator.next();
			if (chargeValue.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())) {
				indictmentsList.add(chargeValue);

			} else if (chargeValue.getChargeType().equals(ChargeTypes.BREACH.getChargeType())) {
				breachesList.add(chargeValue);

			} else if (chargeValue.getChargeType().equals(ChargeTypes.FAIL2APPEAR.getChargeType())) {
				fail2AppearList.add(chargeValue);

			} else if (chargeValue.getChargeType().equals(ChargeTypes.SECTION_41.getChargeType())) {
				if (section41Charge == null) {
					section41Charge = chargeValue;
					model.setSection41ChargeCreated(true);
				} else {
					// throw new CSRecoverableException("Error: Found a
					// second ChargeValue of type section 41. Can only be
					// one.");
					log.error("Error: Found a second ChargeValue of type section 41.  Can only be one.");
				}
				// } else if(chargeValue.getChargeType().equals(ChargeTypes.)){
			} else if (chargeValue.getChargeType().equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())) {
				if (committalForSentenceCharge == null) {
					committalForSentenceCharge = chargeValue;
					model.setCommittalChargeCreated(true);
				} else {
					log.error("Error: Found a second ChargeValue of type Committal for Sentence.  Can only be one.");
				}

			} else if (chargeValue.getChargeType().equals(ChargeTypes.ORIGINAL_CHARGE.getChargeType())) {
				log.debug("Original Charge found - not processed with normal charges so ignore");
			} else if (chargeValue.getChargeType().equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())) {
				if (appealOffenceCharge == null) {
					appealOffenceCharge = chargeValue;
					model.setAppealChargeCreated(true);
				} else {
					log.error("Error: Found a second ChargeValue of type Criminal Appeal. Can only be one.");
				}
			} else {
				log.error("Found an unexpected charge of type: " + chargeValue.getChargeType() + " = "
						+ chargeValue.getChargeTypeDescription());
			}
		} // end of While loop

		// printSeqNosListsInMap();
	}

	protected Map getIndictmentsMap() {
		return indictmentsPanelsMap;
	}

	protected Map getBreachesMap() {
		return breachesPanelsMap;
	}

	protected void setSelectedIndictmentPanel(IndictmentSummaryPanel csp) {
		selectedIndictmentPanel = csp;
	}

	protected void setSelectedBreachPanel(BreachSummaryPanel bsp) {
		selectedBreachPanel = bsp;
	}

	public ChargesControllerModel getModel() {
		return model;
	}

	protected XhibitApplicationController getXAC() {
		return xac;
	}

	private void getUserSessionProperties() {
		XhibitSingleton xs = XhibitSingleton.getInstance();

		// Get the current court if available
		Integer courtID = xs.getCourtId();
		if (courtID == null) {
			currentCourt = -1;
		} else {
			currentCourt = courtID.intValue();
		}
		model.setCourtId(currentCourt);

		// Get the current court room if available
		Integer courtroomID = xs.getCourtRoomId();
		if (courtroomID == null) {
			currentCourtRoom = -1;
		} else {
			currentCourtRoom = courtroomID.intValue();
		}
		model.setCourtRoomId(currentCourtRoom);
	}

	public void stepInitialise() throws CSRecoverableException {
		log.debug("stepInitialise - Begin");

		xac = model.getACM().getXhibitApplicationController();
		editMode = applicationCaseModel.isInEditMode(FunctionList.ECharge);

		// unfortunately we need to keep this call to initialise the actions
		// list...
		chargesActions = ChargesControllerHelper.getChargesActions(xac);
		getUserSessionProperties();
	}

	/**
	 * XPanel implementation called by the application when leaving the screen.
	 * Used here to disable the actions used by this screen.
	 *
	 * @throws CSRecoverableException
	 */
	public void stepDeactivate() throws CSRecoverableException {
		enableChargesActions(false);

		// Reset the menu items enabled in this screen back to their original
		// state
		selectedTab = -1;
		stepUpdateViewState();
		XhibitActions.getAction(xac, XhibitActions.tbAddOffence).setEnabled(false);
		XhibitActions.getAction(xac, XhibitActions.tbAddDefendant).setEnabled(false);
	}

	/**
	 * XPanel implementation called by the application just before leaving the
	 * screen. Used here to display the Crest Indictment Log dialog if there are
	 * Indictment changes for this case.
	 *
	 * @throws CSRecoverableException
	 * @throws CSValidationException
	 */
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		try {
			ScheduledHearingValue shv = applicationCaseModel.getScheduledHearingValue();
			if (shv != null && shv.getCaseId() != null) {
				CaseBasicValue cbv = XhibitDelegateHelper.getCaseDelegate().getCase(shv.getCaseId());

				// The Crest Indictment Log for this case only has to be updated
				// if there are Indictment changes for this case.
				if (CrestIndictmentLog.getInstance().isIndictmentActionOnCase(cbv.getId())) {
					// If the case can be updated, display the dialog,
					// otherwise
					// ask the user if they want to continue or wait.
					if (ChargesControllerHelper.canCaseBeUpdated(cbv)) {
						CrestIndictmentLogDialog dialog = new CrestIndictmentLogDialog(this.xac, cbv,
								CrestIndictmentLog.getInstance().getLatestLogStoredInMemory(cbv), 400, 400);
						dialog.setVisible(true);
						dialog.clearChangesForCase();
					} else {
						int rc = JOptionPane.showConfirmDialog(xac, getResource("crest.updating.message"),
								getResource("crest.updating.title"), JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
						if (rc == JOptionPane.YES_OPTION) {
							// continue
						} else if (rc == JOptionPane.NO_OPTION) {
							throw new UserCancelException();
						} else {
							throw new UserCancelException();
						}
					}
				}
			} else {
				log.debug("ChargesControler.stepValidate(): Failed to get Case ID");
			}
		} catch (UserCancelException uce) {
			throw uce;
		} catch (Exception e) {
			XHIBITErrorHandler.handleError(e);
		}
	}

	/**
	 * XPanel implementation that updates the controls on the screen after some
	 * action/event occurs.
	 */
	public void stepUpdateViewState() {
		ChargeValue chargeValue = null;
		try {
			model.setSelectionModel(currentSM);

			boolean indictmentTab = selectedTab == INDICTMENTS_TAB;
			boolean section41sTab = selectedTab == SECTION41S_TAB;
			boolean committalsTab = selectedTab == COMMITTALS_TAB;
			boolean breachesTab = selectedTab == BREACHES_TAB;
			boolean fail2AppearTab = selectedTab == FAIL2APPEAR_TAB;
			boolean appealOffenceTab = selectedTab == APPEALOFFENCES_TAB;

			boolean chargeSelected = model.getChargeValue() != null;
			boolean offenceSelected = currentSM.getSelectionType() == ChargesSelectionModel.OFFENCE_SELECTION_TYPE;

			if (editMode) {
				if (chargeSelected)
					chargeValue = model.getChargeValue();
				boolean hasIndSignedDate = false;
				if (indictmentTab && chargeSelected && chargeValue != null) {
					hasIndSignedDate = chargeValue.getIndSignedDate() == null;
				} else {
					hasIndSignedDate = false;
				}
				// boolean chargeSelected = currentSM.getSelectionType() ==
				// ChargesSelectionModel.CHARGE_SELECTION_TYPE;
				// boolean offenceSelected = currentSM.getSelectionType() ==
				// ChargesSelectionModel.OFFENCE_SELECTION_TYPE;
				boolean defendantColumnSelected = currentSM
						.getSelectionType() == ChargesSelectionModel.DEFENDANT_SELECTION_TYPE;

				/**
				 * The offence or defendant column is selected but the defendant
				 * column does not have to be populated e.g. a count with or
				 * without a defendant
				 */
				boolean offenceOrDefendantColumn = offenceSelected || defendantColumnSelected;

				/**
				 * The offence or defendant column is selected and the defendant
				 * column is populated i.e. a count with a defendant
				 */
				boolean offenceWithDefendant = (offenceSelected || defendantColumnSelected)
						&& (currentSM.getDefendantValue() != null);

				boolean isThisIndictmentAJoinder = false;
				if (selectedIndictmentPanel != null)
					isThisIndictmentAJoinder = selectedIndictmentPanel.isThisIndictmentAJoinder();

				// Charges actions
				// Indictment menu actions
				XhibitActions.getAction(xac, XhibitActions.AddIndictment).setEnabled(indictmentTab);
				XhibitActions.getAction(xac, XhibitActions.AddCount)
						.setEnabled(indictmentTab && chargeSelected && !isThisIndictmentAJoinder);
				XhibitActions.getAction(xac, XhibitActions.AddCountToJoinder)
						.setEnabled(indictmentTab && chargeSelected && isThisIndictmentAJoinder);
				XhibitActions.getAction(xac, XhibitActions.RemoveIndictment)
						.setEnabled(indictmentTab && chargeSelected);
				XhibitActions.getAction(xac, XhibitActions.RenumberCounts)
						.setEnabled(indictmentTab && offenceOrDefendantColumn && !isThisIndictmentAJoinder);
				XhibitActions.getAction(xac, XhibitActions.StayIndictment).setEnabled(indictmentTab && chargeSelected);
				// XhibitActions.getAction(xac,
				// XhibitActions.SignIndictment).setEnabled(
				// indictmentTab && chargeSelected && hasIndSignedDate);
				XhibitActions.getAction(xac, XhibitActions.SignIndictmentRefused)
						.setEnabled(indictmentTab && chargeSelected && hasIndSignedDate);
				XhibitActions.getAction(xac, XhibitActions.JoinIndictment)
						.setEnabled(indictmentTab && chargeSelected && !isThisIndictmentAJoinder);
				XhibitActions.getAction(xac, XhibitActions.SOProsecutionNoEvidence).setEnabled(indictmentTab);

				// Section 41( a.k.a Summary Offences ) menu actions
				XhibitActions.getAction(xac, XhibitActions.AddS41Offence).setEnabled(section41sTab);
				XhibitActions.getAction(xac, XhibitActions.DefendantSummaryOffences).setEnabled(section41sTab);

				// Appeal Offences
				XhibitActions.getAction(xac, XhibitActions.AddAppealOffence).setEnabled(appealOffenceTab);

				// Committals for Sentence menu actions
				XhibitActions.getAction(xac, XhibitActions.AddC4SOffence).setEnabled(committalsTab);
				XhibitActions.getAction(xac, XhibitActions.C4SBringBack).setEnabled(committalsTab);
				XhibitActions.getAction(xac, XhibitActions.C4SNotAdmitted).setEnabled(committalsTab);
				XhibitActions.getAction(xac, XhibitActions.C4SPutAndAdmitted).setEnabled(committalsTab);

				// Breach menu actions
				XhibitActions.getAction(xac, XhibitActions.AddBreach).setEnabled(breachesTab);
				XhibitActions.getAction(xac, XhibitActions.EditBreachProps).setEnabled(breachesTab && chargeSelected);
				XhibitActions.getAction(xac, XhibitActions.AddBreachOffence).setEnabled(breachesTab && chargeSelected);
				XhibitActions.getAction(xac, XhibitActions.AdditionalBreachOffenceDefendantInfo)
						.setEnabled(breachesTab && chargeSelected && offenceSelected);
				XhibitActions.getAction(xac, XhibitActions.RemoveBreach).setEnabled(breachesTab && chargeSelected);

				// Failure to Appear menu actions
				XhibitActions.getAction(xac, XhibitActions.AddBailActOffence).setEnabled(fail2AppearTab);
				XhibitActions.getAction(xac, XhibitActions.ChangeBailActOffence)
						.setEnabled(fail2AppearTab && offenceOrDefendantColumn);
				XhibitActions.getAction(xac, XhibitActions.RemoveBailActOffence)
						.setEnabled(fail2AppearTab && offenceOrDefendantColumn);

				// Count menu actions
				XhibitActions.getAction(xac, XhibitActions.AdditionalCountInfo)
						.setEnabled(indictmentTab && offenceOrDefendantColumn);
				XhibitActions.getAction(xac, XhibitActions.AdditionalDefendantOnCountInfo)
						.setEnabled(indictmentTab && offenceOrDefendantColumn && offenceWithDefendant);
				XhibitActions.getAction(xac, XhibitActions.AddDefendantsToCount)
						.setEnabled(indictmentTab && offenceOrDefendantColumn);
				XhibitActions.getAction(xac, XhibitActions.RemoveDefendantsOnCount)
						.setEnabled(indictmentTab && offenceOrDefendantColumn);
				XhibitActions.getAction(xac, XhibitActions.CountParticularsAmended)
						.setEnabled(indictmentTab && offenceOrDefendantColumn);
				XhibitActions.getAction(xac, XhibitActions.StayCount)
						.setEnabled(indictmentTab && offenceOrDefendantColumn);
				XhibitActions.getAction(xac, XhibitActions.ChangeCount)
						.setEnabled(indictmentTab && offenceOrDefendantColumn);
				XhibitActions.getAction(xac, XhibitActions.RemoveCount)
						.setEnabled(indictmentTab && offenceOrDefendantColumn);

				// Defendant menu actions
				XhibitActions.getAction(xac, XhibitActions.ChangeDefendant)
						.setEnabled(offenceWithDefendant && !breachesTab && !fail2AppearTab);
				XhibitActions.getAction(xac, XhibitActions.AddCountsToDefendant)
						.setEnabled(indictmentTab && offenceWithDefendant);
				XhibitActions.getAction(xac, XhibitActions.StayDefendantOnCount)
						.setEnabled(indictmentTab && offenceWithDefendant);
				XhibitActions.getAction(xac, XhibitActions.StayDefendantOnIndictment)
						.setEnabled(indictmentTab && offenceWithDefendant);
				XhibitActions.getAction(xac, XhibitActions.ApplicationToSever)
						.setEnabled(indictmentTab && offenceWithDefendant);
				XhibitActions.getAction(xac, XhibitActions.VoluntaryBillPreferred)
						.setEnabled(indictmentTab && offenceWithDefendant);
				XhibitActions.getAction(xac, XhibitActions.LateBillOfIndictment)
						.setEnabled(indictmentTab && offenceWithDefendant);
				XhibitActions.getAction(xac, XhibitActions.BillOfIndictment)
						.setEnabled(indictmentTab && offenceWithDefendant);

				// Offence menu actions
				XhibitActions.getAction(xac, XhibitActions.AdditionalOffenceInfo)
						.setEnabled(!indictmentTab && !breachesTab && !fail2AppearTab && offenceOrDefendantColumn);
				XhibitActions.getAction(xac, XhibitActions.AdditionalDefendantOnOffenceInfo)
						.setEnabled(!indictmentTab && !breachesTab && offenceOrDefendantColumn && offenceWithDefendant);
				XhibitActions.getAction(xac, XhibitActions.AddDefendantsToOffence)
						.setEnabled(!indictmentTab && !breachesTab && !fail2AppearTab && offenceOrDefendantColumn);
				XhibitActions.getAction(xac, XhibitActions.ChangeOffence)
						.setEnabled(!indictmentTab && !fail2AppearTab && offenceOrDefendantColumn);
				XhibitActions.getAction(xac, XhibitActions.RemoveOffence)
						.setEnabled(!indictmentTab && !fail2AppearTab && offenceOrDefendantColumn);

				// Crest Indictment Log action.
				XhibitActions.getAction(xac, XhibitActions.CrestIndictmentLog).setEnabled(indictmentTab);

				// what are these????
				// XhibitActions.getAction(xac,
				// XhibitActions.StayDefendant).setEnabled(indictmentTab &&
				// offenceOrDefendant);

				// toolbar actions.
				// XhibitActions.getAction(xac,
				// XhibitActions.tbAdd).setEnabled(true);
				XhibitActions.getAction(xac, XhibitActions.tbChange).setEnabled(
						(indictmentTab || section41sTab || committalsTab || breachesTab) && offenceOrDefendantColumn);
				XhibitActions.getAction(xac, XhibitActions.tbStay).setEnabled(indictmentTab && chargeSelected);

				XAction toolbarChargeAction = XhibitActions.getAction(xac, XhibitActions.tbAddCharge);
				if (indictmentTab) {
					XAction indictmentAction = XhibitActions.getAction(xac, XhibitActions.AddIndictment);

					toolbarChargeAction.setSmallIcon(indictmentAction.getIcon());
					toolbarChargeAction.setShortDescription(indictmentAction.getShortDescription());
					toolbarChargeAction.setLongDescription(indictmentAction.getLongDescription());
					toolbarChargeAction.setEnabled(true);
				} else if (breachesTab) {
					XAction breachAction = XhibitActions.getAction(xac, XhibitActions.AddBreach);

					toolbarChargeAction.setSmallIcon(breachAction.getIcon());
					toolbarChargeAction.setShortDescription(breachAction.getShortDescription());
					toolbarChargeAction.setLongDescription(breachAction.getLongDescription());
					toolbarChargeAction.setEnabled(true);
				} else {
					toolbarChargeAction.setEnabled(false);
				}

				XhibitActions.getAction(xac, XhibitActions.tbAddOffence).setEnabled(
						section41sTab || committalsTab || fail2AppearTab || chargeSelected || appealOffenceTab);
				XhibitActions.getAction(xac, XhibitActions.tbAddDefendant)
						.setEnabled(offenceOrDefendantColumn && !breachesTab && !fail2AppearTab);
			}

			if (breachesTab && chargeSelected && offenceSelected) {
				model.setDefendantValue(currentSM.getDefendantValue());
				if ((breachDetailsPanel != null) && (breachDetailsPanel instanceof BreachPanel)) {
					// A breach has previously been selected, if it is the
					// same
					// on then just activate it.
					BreachPanel tempBreach = (BreachPanel) breachDetailsPanel;
					if (tempBreach.getBreachValue().getBreachID().intValue() == currentSM.getChargeValue()
							.getBreachValue().getBreachID().intValue()) {
						tempBreach.stepActivate();
					} else {
						// A Breach has been selected that is not the same as
						// the previous breach that was selected.
						getBreachDetailsHolderPanel().remove(breachDetailsPanel);
						breachDetailsPanel = new BreachPanel(model, BreachPanel.VIEW_MODE);
						getBreachDetailsHolderPanel().add(breachDetailsPanel,
								new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
										GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
				} else {
					// The first breach ever selected on the breach tab.
					breachDetailsPanel = new BreachPanel(model, BreachPanel.VIEW_MODE);

					getBreachDetailsHolderPanel().add(breachDetailsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

					getBreachDetailsHolderPanel().setPreferredSize(new Dimension(400, 400));
				}
				getRightWindowSplitPane().setDividerLocation(0.5);
			} else {
				if (breachDetailsHolderPanel != null) {
					if (breachDetailsPanel != null) {
						getBreachDetailsHolderPanel().remove(breachDetailsPanel);
						breachDetailsPanel = null;
					}
					getBreachDetailsHolderPanel().setPreferredSize(new Dimension(0, 0));
				}
				getRightWindowSplitPane().setDividerLocation(1.0);
			}
		} catch (CSRecoverableException csre) {
			XHIBITErrorHandler.handleError(csre);
		}
	}

	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		// empty
	}

	public void stepActivate() throws CSRecoverableException {
		log.debug("stepActivate - Begin");

		ScheduledHearingValue shv = applicationCaseModel.getScheduledHearingValue();

		if (!accessedFromCaseCreateAmend && !xac.isCaseChargesDisposalsOpened()) { // standard
																					// method,
																					// this
																					// gets
																					// called
			// when xac.open(cc) is called for
			// example
			// Enable tabs based on case type
			if (CaseTypeHelper.isTrial_CaseType(shv)) {
				// Enable: indictment, s41, breach, fail2appear. Disable: c4s,
				// appeal.
				setTabsEnabled(true, true, true, true, false, false);
				chargesTabbedPane.setSelectedIndex(INDICTMENTS_TAB);
			} else if (CaseTypeHelper.isSentence_CaseType(shv)) {
				// Enable: c4s, breach, fail2appear. Disable: indictment, s41,
				// appeal
				setTabsEnabled(false, false, true, true, true, false);
			} else if (CaseTypeHelper.isBail_CaseType(shv)) {
				enableChargesActions(false);
				editMode = false;
			} else if (CaseTypeHelper.isCriminalAppeal_CaseType(shv)) {
				// Disable all
				setTabsEnabled(false, false, false, false, false, false);
				enableChargesActions(false);
				editMode = false;
			} else if (isUnsupportedCase(shv)) {
				// disable all
				setTabsEnabled(false, false, false, false, false, false);
				getChargesTabbedPane().setEnabled(false);
				enableChargesActions(false);
				editMode = false;
			}
		} else if (accessedFromAddIndictmentCase) {
			/*
			 * Called when accessed from AddIndictmentCase
			 */
			if (CaseTypeHelper.isTrial_CaseType(shv)) {
				// Enable: indictment, s41
				setTabsEnabled(true, true, false, false, false, false);
				chargesTabbedPane.setSelectedIndex(INDICTMENTS_TAB);
			} else if (CaseTypeHelper.isSentence_CaseType(shv)) {
				/*
				 * Check if Receipt type is commital for sentence, if so, enable
				 * commital for sentence tab
				 */
				CaseControllerBeanBusinessDelegate cDelegate = XhibitDelegateHelper.getCaseDelegate();
				CaseBasicValue cbv = cDelegate.getCase(shv.getCaseBasicValue().getCaseId());
				if (cbv != null) {
					if (cbv.getReceiptType().equals("CS")) {
						setTabsEnabled(false, false, true, false, true, false);
					} else {
						setTabsEnabled(false, false, true, false, false, false);
					}
				}
			} else if (CaseTypeHelper.isCriminalAppeal_CaseType(shv)) {
				// Enable: appeal .
				setTabsEnabled(false, false, false, false, false, true);
				
			} else if (isUnsupportedCase(shv)) {
				// disable all
				setTabsEnabled(true, false, false, false, false, false);
				getChargesTabbedPane().setEnabled(false);
				enableChargesActions(false);
				editMode = false;
			} else {
				// disable all
				setTabsEnabled(true, false, false, false, false, false);
				getChargesTabbedPane().setEnabled(false);
				enableChargesActions(false);
				editMode = false;
			}
			xac.enableCourtLogActions(false); // disable court log actions
		} else { // alternatively called from CaseXPanel after case created
			stepActivate(true);
			xac.enableCourtLogActions(false); // disable court log actions
			xac.getCaseStatus().setCaseCreateInProgressFlag(true);

			if (CaseTypeHelper.isCriminalAppeal_CaseType(shv)) {
				XhibitActions.getAction(xac, XhibitActions.Sentence).setEnabled(true);
			}
		}

		XhibitActions.getAction(xac, XhibitActions.OriginalCharges)
				.setEnabled(!isUnsupportedCase(shv) && CaseTypeHelper.isTrial_CaseType(shv));
		XhibitActions.getAction(xac, XhibitActions.ExportCharges).setEnabled(!isUnsupportedCase(shv));

		stepUpdateViewState();
	}

	public void stepActivate(boolean overload) throws CSRecoverableException {
		log.debug("stepActivate - Begin");

		// Enable tabs based on case type
		String caseType = applicationCaseModel.getCaseType();

		if (caseType.equals("T")) { // trial case
			// Enable: indictment, s41, breach, fail2appear. Disable: c4s,
			// appeal.
			setTabsEnabled(false, true, false, false, false, false);
		} else if (caseType.equals("S")) { // sentence case
			// ctx-1610
			if (applicationCaseModel.getScheduledHearingValue().getCaseBasicValue().getReceiptType() != null) {
				if (applicationCaseModel.getScheduledHearingValue().getCaseBasicValue().getReceiptType().equals("CB")
						|| applicationCaseModel.getScheduledHearingValue().getCaseBasicValue().getReceiptType()
								.equals("BB")) {
					// Only breaches tab for CB and BB receipt types
					setTabsEnabled(false, false, true, false, false, false);
				} else {
					// Enable: c4s, breach. Disable: indictment, s41, appeal,
					// fail2appear
					setTabsEnabled(false, false, true, false, true, false);
				}
			} else {
				// Enable: c4s, breach. Disable: indictment, s41, appeal,
				// fail2appear
				setTabsEnabled(false, false, true, false, true, false);
			}
		} else if (caseType.equals("B")) { // bail case
			enableChargesActions(false);
			editMode = false;
		} else if (caseType.equals("A")) { // appeal case
			// Enable: indictment, s41, breach, appeal, fail2appear. Disable:
			// c4s.
			setTabsEnabled(false, false, false, false, false, true);
		} else { // unsupported?
			// disable all
			setTabsEnabled(false, false, false, false, false, false);
			getChargesTabbedPane().setEnabled(false);
			enableChargesActions(false);
			editMode = false;
		}
	}

	private void setTabsEnabled(boolean indictment, boolean section41, boolean breach, boolean fail2appear,
			boolean committal, boolean appeal) {
		getChargesTabbedPane().setEnabledAt(INDICTMENTS_TAB, indictment);
		getChargesTabbedPane().setEnabledAt(SECTION41S_TAB, section41);
		getChargesTabbedPane().setEnabledAt(BREACHES_TAB, breach);
		getChargesTabbedPane().setEnabledAt(FAIL2APPEAR_TAB, fail2appear);
		getChargesTabbedPane().setEnabledAt(COMMITTALS_TAB, committal);
		getChargesTabbedPane().setEnabledAt(APPEALOFFENCES_TAB, appeal);
	}

	/**
	 * Check if this is an unsupported case i.e. a case has no indictments,
	 * summary offences, committals for sentences offences or breaches.
	 *
	 * @param shv
	 *            the ScheduledHearingValue
	 * @return true if the charge screen does not support this type of case.
	 * @throws UnknownCaseTypeException
	 */
	private boolean isUnsupportedCase(ScheduledHearingValue shv) throws UnknownCaseTypeException {
		return (CaseTypeHelper.isCriminalAppeal_CaseType(shv) || CaseTypeHelper.isMiscelleanousAppeal_CaseType(shv)
				|| CaseTypeHelper.isCombinedCourt_CaseType(shv) || CaseTypeHelper.isUndefined_CaseType(shv));
	}

	/**
	 * CommonFunctions implementation - called by the Xhibit Application
	 * Controller to check when saving if it is to write the output to a pdf
	 * file.
	 *
	 * @return false as there is no requirement for charges to automatically
	 *         create a PDF document.
	 */
	public boolean autoSaveToFile() {
		return false;
	}

	/**
	 * CommonFunctions implementation - called when the user selects print.
	 *
	 * @return a string containing XSL:FO formatted text
	 * @throws UserCancelException
	 * @throws CSRecoverableException
	 */
	public String[] print() throws UserCancelException, CSRecoverableException {
		String xslfo = ChargesControllerHelper.formatCharges(ccv, xac);
		// boolean preview = true;
		// cch.printCharges(xslfo, preview);

		return new String[] { xslfo };
	}

	/**
	 * Get resource string for the given key.
	 *
	 * @param key
	 *            the key to lookup in the resource bundle.
	 * @return the resource string
	 */
	protected String getResource(final String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.MaintainCharges, key);
	}

	/**
	 * enable or disable the Actions for the charges screen.
	 *
	 * @param enabled
	 *            true to enable the charges Actions
	 */
	private void enableChargesActions(final boolean enabled) {
		XAction xAction;
		final Iterator i = chargesActions.iterator();
		while (i.hasNext()) {
			xAction = (XAction) i.next();
			xAction.setEnabled(enabled);
		}
	}

	/**
	 * This function takes the list of breaches, groups them by type and then
	 * orders them according to crest sequence number.
	 *
	 */
	public void orderBreachesList() {

		final String BREACH_TYPE_BRING_BACK = "B";
		final String BREACH_TYPE_COMMITTAL = "C";

		ArrayList<ChargeValue> committalList = new ArrayList<ChargeValue>();
		ArrayList<ChargeValue> bringBackList = new ArrayList<ChargeValue>();
		ArrayList<ChargeValue> nullList = new ArrayList<ChargeValue>();

		// Group by breach type "Committal" or "Bring back"
		for (ChargeValue cv : breachesList) {
			if (cv.getBreachValue().getBreachType().equals(BREACH_TYPE_COMMITTAL)) {
				committalList.add(cv);
			} else if (cv.getBreachValue().getBreachType().equals(BREACH_TYPE_BRING_BACK)) {
				bringBackList.add(cv);
			} else {
				nullList.add(cv);
			}
		}

		// Order by Crest Sequence No
		ChargeSequenceComparator comparator = new ChargeSequenceComparator();

		ChargeValue[] bArrays = new ChargeValue[bringBackList.size()];
		bringBackList.toArray(bArrays);
		ChargeValue[] cArrays = new ChargeValue[committalList.size()];
		committalList.toArray(cArrays);
		ChargeValue[] nArrays = new ChargeValue[nullList.size()];
		nullList.toArray(nArrays);

		Arrays.sort(bArrays, comparator);
		Arrays.sort(cArrays, comparator);
		Arrays.sort(nArrays, comparator);

		// Organize breachesList
		breachesList.clear();
		breachesList.addAll(Arrays.asList(bArrays));
		breachesList.addAll(Arrays.asList(cArrays));
		breachesList.addAll(Arrays.asList(nArrays));
	}

	private DefendantValue getDefendantValue(Integer defId) {
		if (defId == null) {
			XHIBITConstant.error("Breach does not have a defendant. Defendant id = " + defId);
		} else {
			for (DefendantValue item : ccv.getAllDefendants()) {
				if (item.getDefendantID().intValue() == defId.intValue())
					return item;
			}
		}
		return null;
	}

	public class ChargeSequenceComparator implements Comparator<ChargeValue> {
		public int compare(ChargeValue o1, ChargeValue o2) {
			int rtn = 0;
			if (o1.getCrestChargeSeqNo().intValue() > o2.getCrestChargeSeqNo().intValue()) {
				rtn = 1;
			} else if (o1.getCrestChargeSeqNo().intValue() < o2.getCrestChargeSeqNo().intValue()) {
				rtn = -1;
			}
			return rtn;
		}
	}

	public java.util.List getIndictmentsList() {
		return indictmentsList;
	}

	public java.util.List getFailure2AppearList() {
		return fail2AppearList;
	}

	private JButton getExitCaseButton() {
		JButton cancelButton = new JButton("Exit Case");
		cancelButton.addActionListener(new ActionListener() {
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

		return cancelButton;
	}
}
