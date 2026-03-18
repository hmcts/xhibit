package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caseprosecutoragency.CaseProsecutorAgencyControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ChargesLogBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefusedBroadcastCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caseprosecutoragency.CaseProsecutorAgencyValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.casemanagement.util.CaseMethods;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.CaseHelper;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CaseXPanel extends XPanel {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private JTabbedPane tabbedPane;
	private JPanel generalPanel;
	private DefendantAppellantTab defendantPanel;
	private JPanel prosecutorRespondentPanel;
	private XhibitApplicationController xac;
	private JPanel buttonPanel;
	private JButton createButton;
	private JButton finishButton;
	private JButton cancelButton;
	private CaseControllerBeanBusinessDelegate caseDelegate;
	private CaseBasicValue caseBasicValue;
	private List<RefusedBroadcastCaseBasicValue> refusedBroadcastCaseArray;
	private static final String PACKAGE_NAME = "uk.gov.courtservice.xhibit.client.casemanagement";
	private static final String CLASS_NAME = ".CaseXPanel";
	private static final String ERROR_IN = "Error in ";

	private Dimension resizeDimension;
	// Strings for popup messages from resource bundle
	private String incompleteDetails = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"warningDialog.incompletePartyDetails");

	private String incompleteDefendants = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"warningDialog.incompleteDefendantDetails");

	private String defaultError = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"warningDialog.defaultError");

	private String caseUpdated = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"confirmDialog.caseUpdated");

	private String caseUpdatedAddOffences = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"confirmDialog.caseUpdated.addOffences");

	private String caseUpdatedAddConvictions = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"confirmDialog.caseUpdated.addConvictions");

	private String caseCreate = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"confirmDialog.caseCreate");

	private String caseCreateAddOffences = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"confirmDialog.caseCreate.addOffences");

	private String caseCreateAddConvictions = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"confirmDialog.caseCreate.addConvictions");
	
	private String caseCreateAbort = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"confirmDialog.caseCreate.abortMessage");
	
	private String caseAmendAbort = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"confirmDialog.caseAmend.abortMessage");

	public CaseXPanel(final XhibitApplicationController xac){
		this.xac = xac;

		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		this.setFocusTraversalPolicyProvider(true);

		caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		try {
			if (xac.getCaseStatus().getCaseId() > 0) {
				caseBasicValue = caseDelegate.getCase(xac.getCaseStatus().getCaseId());
				refusedBroadcastCaseArray = caseDelegate.getRefusedBroadcastCaseArray(xac.getCaseStatus().getCaseId());
				xac.getCaseStatus().setCaseTitle(caseBasicValue.getCaseType() + "" + caseBasicValue.getCaseNumber());

				if (caseBasicValue.getCaseType().equalsIgnoreCase("A")
						&& (caseBasicValue.getCaseSubType() == null || !caseBasicValue.getCaseSubType().equals("O"))) {
					xac.getCaseStatus().setCaseType(CaseType.APPEAL);
				}
				if (caseBasicValue.getCaseType().equalsIgnoreCase("S")) {
					xac.getCaseStatus().setCaseType(CaseType.SENTENCE);
				}
				if (caseBasicValue.getCaseType().equalsIgnoreCase("T")) {
					xac.getCaseStatus().setCaseType(CaseType.TRIAL);
				}
				if (caseBasicValue.getCaseType().equalsIgnoreCase("A") && caseBasicValue.getCaseSubType() != null
						&& caseBasicValue.getCaseSubType().equals("O")) {
					xac.getCaseStatus().setCaseType(CaseType.MISC);
				}
			}

			// Button to create the case
			if (xac.getCaseStatus().getCaseProcess() == CaseProcess.NEW)
				createButton = new JButton("Create Case");
			else
				createButton = new JButton("Save");

			createButton.setEnabled(false);
			createButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (createButton.getText().equals("Create Case")) {
						// Perform create case here, only create case with
						// general tab information
						// Populate caseBasicValue, gets data from fields within
						// whichever general tab is used
						caseBasicValue = populateBasicValue(xac.getCaseStatus().getCaseType());
						
						caseBasicValue.setCaseListed("N");

						//if its a general tab we need to ensure that 
						//validation is correct on app granted
						if(!televisedTrialCheck(xac.getCaseStatus().getCaseType())) {
							return;
						}

						if (!validateGeneralTab())
							return;

						List<ChargesLogBasicValue> chargesLogList = new ArrayList<ChargesLogBasicValue>();
						if (xac.getCaseStatus().isCaseType(CaseType.TRIAL)) {
							chargesLogList = ((GeneralTrial) generalPanel).getCharges();
						}

						String createdCase = "";
						try {
							// Create case with only general tab populated
							// caseBasicValue
							createdCase = caseDelegate
									.createCase(
											caseBasicValue, XhibitSingleton.getInstance().getUserSession()
													.getSessionProperty(UserTerminalProperties.USER_NAME),
											chargesLogList, refusedBroadcastCaseArray);
							int caseId = caseDelegate.findCaseId(caseBasicValue.getCaseType(),
									Integer.parseInt(createdCase.substring(1)),
									XhibitSingleton.getInstance().getCourtId());

							caseBasicValue = caseDelegate.getCase(caseId);
							refusedBroadcastCaseArray = caseDelegate.getRefusedBroadcastCaseArray(xac.getCaseStatus().getCaseId());

							populateVersionPostSave(caseBasicValue.getVersion(), xac.getCaseStatus().getCaseType(), caseId);

							xac.getCaseStatus().setCaseProcess(CaseProcess.SAYG_AMEND);
							// Actually saved to db and returned successfully
							if (!createdCase.isEmpty()) {
								// needed so dropdown is disabled and text field
								// appears in its placey
								if (xac.getCaseStatus().getCaseType() == CaseType.TRIAL) {
									((GeneralTrial) generalPanel).populateReceiptType(caseBasicValue);
								} 
								// Change button text to "Save"
								createButton.setText("Save");
								createButton.setEnabled(false);
								finishButton.setEnabled(true);
								// Update case number on each tab
								populateCaseNumberField(xac.getCaseStatus().getCaseType(), createdCase);
								defendantPanel.populateCaseNumberAndTitle(createdCase, caseBasicValue.getCaseTitle());
								((ProsecutorRespondentTab) prosecutorRespondentPanel)
										.populateCaseNumberAndTitle(createdCase, caseBasicValue.getCaseTitle());

								clearGeneralTabChangedState(xac.getCaseStatus().getCaseType());
								tabbedPane.setEnabledAt(1, true);
								tabbedPane.setEnabledAt(2, true);
								// Update xac in defendant/appellant tab to
								// properly
								// notify that its no longer new case but rather
								// update
								xac.getCaseStatus().setCaseProcess(CaseProcess.SAYG_AMEND);
								defendantPanel.updateXAC(xac);

								// So prosecutor respondent tab has correct info
								// for adding pros/resp
								((ProsecutorRespondentTab) prosecutorRespondentPanel).populate(caseBasicValue);
								// Do the same for defendant
								defendantPanel.populate(caseBasicValue);
								// Set title of page
								((XhibitApplicationControllerImpl) xac).setNewTitle(createdCase + " - Amend Case");

								xac.getCaseStatus().setCaseId(caseId);
							}
						} catch (CaseControllerException e1) {
							log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e1);
							XHIBITErrorHandler.handleError(e1, null, defaultError);
							return;
						} catch (ChargeControllerException e1) {
							log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
							XHIBITConstant.handleError(e1, this.getClass());
							return;
						} 

					} else {
						// true if save was okay
						if (saveCaseDetails(xac.getCaseStatus().getCaseType())) {
							// Only necessary to do clear changed state for
							// general and defendant tab as cant enter
							// data into fields on prosecutor/respondent tab
							clearGeneralTabChangedState(xac.getCaseStatus().getCaseType());
							defendantPanel.clearChangedState();

							createButton.setEnabled(false);
							finishButton.setEnabled(true);
							enableTabbedPane();
						}
					}
				}
			});

			// Button to cancel the whole case create process
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new cancelButtonActionListener());

			finishButton = new JButton("Finish");
			finishButton.setEnabled(false);
			finishButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = -1;
					boolean chargesEnabled = false;

					// to avoid null pointer thrown, just simply return and do
					// nothing
					if (caseBasicValue == null || caseBasicValue.getCaseType() == null
							|| caseBasicValue.getCaseType().isEmpty()) {
						return;
					}

					String caseString = caseBasicValue.getCaseType() + caseBasicValue.getCaseNumber().toString();

					// Either defendants or prosecutors missing
					if (((DefendantAppellantTab) defendantPanel).getDefendantInTableCount() <= 0
							|| ((ProsecutorRespondentTab) prosecutorRespondentPanel)
									.getProsecutorRespondentInTableCount() <= 0) {

						result = JOptionPane.showConfirmDialog(xac, incompleteDetails,
								"Details Incomplete - Please Confirm", JOptionPane.OK_CANCEL_OPTION);

						if (result == 0) {
							if (((DefendantAppellantTab) defendantPanel).getDefendantInTableCount() > 0) {
								chargesEnabled = true;
							}
						} else {
							return;
						}
					} else {
						// both defendants and prosecutors added to case
						chargesEnabled = true;

						// CTX-2063
						// For misc appeal cases they are incomplete unless
						// Respondent is added,
						// i.e. respondent added with status = respondent
						if (xac.getCaseStatus().isCaseType(CaseType.MISC)) {
							if (((ProsecutorRespondentTab) prosecutorRespondentPanel).getRespondentCount() <= 0) {
								result = JOptionPane.showConfirmDialog(xac, incompleteDetails,
										"Details Incomplete - Please Confirm", JOptionPane.OK_CANCEL_OPTION);

								if (result != 0) {
									return;
								}
							}
						}
					}

					if (xac.getCaseStatus().isCaseType(CaseType.TRIAL)) {
						if (((GeneralTrial) generalPanel)
								.getNumDefendantsToAdd() != ((DefendantAppellantTab) defendantPanel)
										.getDefendantInTableCount()) {
							result = JOptionPane.showConfirmDialog(xac, incompleteDefendants,
									"Missing defendants", JOptionPane.OK_CANCEL_OPTION);
							// if user presses cancel, return, else continue as
							// normal
							if (result != 0) {
								return;
							}
						}
					} else if (xac.getCaseStatus().isCaseType(CaseType.SENTENCE)) {
						if (((GeneralSentence) generalPanel)
								.getNumDefendantsToAdd() != ((DefendantAppellantTab) defendantPanel)
										.getDefendantInTableCount()) {
							result = JOptionPane.showConfirmDialog(xac, incompleteDefendants,
									"Missing defendants", JOptionPane.OK_CANCEL_OPTION);
							// if user presses cancel, return, else continue as
							// normal
							if (result != 0) {
								return;
							}
						}
					}

					if (chargesEnabled) {
						// allow access to charges screen based on case type
						if (xac.getCaseStatus().isCaseType(CaseType.APPEAL)) {
							if (xac.getCaseStatus().isCaseProcess(CaseProcess.AMEND))
								result = JOptionPane.showConfirmDialog(xac, caseUpdatedAddConvictions,
										"Add Convictions", JOptionPane.YES_NO_OPTION);
							else
								result = JOptionPane.showConfirmDialog(xac,
										caseCreate + caseString + caseCreateAddConvictions, "Add Convictions",
										JOptionPane.YES_NO_OPTION);
						} else if (xac.getCaseStatus().isCaseType(CaseType.SENTENCE)
								|| xac.getCaseStatus().isCaseType(CaseType.TRIAL)) {
							if (xac.getCaseStatus().isCaseProcess(CaseProcess.AMEND))
								result = JOptionPane.showConfirmDialog(xac, caseUpdatedAddOffences,
										"Add Offences", JOptionPane.YES_NO_OPTION);
							else
								result = JOptionPane.showConfirmDialog(xac,
										caseCreate + caseString + caseCreateAddOffences, "Add Offences",
										JOptionPane.YES_NO_OPTION);
						} else { // misc appeal simply show standard create
							if (xac.getCaseStatus().isCaseProcess(CaseProcess.AMEND))
								JOptionPane.showMessageDialog(xac, caseUpdated);
							else
								JOptionPane.showMessageDialog(xac, caseCreate + caseString);
							result = 1;
						}

						if (0 == result) {
							try {
								ArrayList<DefendantOnCaseBasicValue> defToAddToCase = new ArrayList<DefendantOnCaseBasicValue>();
								defToAddToCase = (ArrayList<DefendantOnCaseBasicValue>) XhibitDelegateHelper
										.getDefendantDelegate().findByCaseId(caseBasicValue.getCaseId());

								ApplicationCaseModel acm = populateApplicationCaseModel(true, caseBasicValue,
										defToAddToCase);
								acm.setXhibitApplicationController(xac);
								ChargesController cc = new ChargesController(acm, true);
								xac.setApplicationCaseModel(acm);
								xac.open(cc);
								xac.getCaseStatus().setCaseCreateInProgressFlag(true);
								xac.setCaseChargesDisposalsOpened(true);
							} catch (DefendantControllerException e1) {
								log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e1);
								XHIBITErrorHandler.handleError(e1, null, defaultError);
							} catch (CSRecoverableException e1) {
								log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e1);
								XHIBITErrorHandler.handleError(e1, null, defaultError);
							} catch (EJBException e1) {
								log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
								XHIBITConstant.handleError(e1, this.getClass());
							}
						} else {
							xac.getCaseStatus().setCaseCreateInProgressFlag(false);
							xac.getCaseStatus().setCaseId(0);
							try {
								xac.close();
							} catch (CSRecoverableException e1) {
								log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e1);
								XHIBITErrorHandler.handleError(e1, null, defaultError);
							}
						}
					} else {
						// Case created successfully pop-up
						if (xac.getCaseStatus().isCaseProcess(CaseProcess.AMEND))
							JOptionPane.showMessageDialog(xac, caseUpdated);
						else
							JOptionPane.showMessageDialog(xac, caseCreate + caseString);

						xac.getCaseStatus().setCaseCreateInProgressFlag(false);
						xac.getCaseStatus().setCaseId(0);
						try {
							xac.close();
						} catch (CSRecoverableException e1) {
							log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e1);
							XHIBITErrorHandler.handleError(e1, null, defaultError);
						}
					}
				}
			});

			// Button Panel
			buttonPanel = new JPanel();
			buttonPanel.add(createButton);
			buttonPanel.add(finishButton);
			buttonPanel.add(cancelButton);

			// caseNumber
			tabbedPane = new CaseTabbedPane(JTabbedPane.TOP);
			tabbedPane.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					Component x = ((JScrollPane) tabbedPane.getSelectedComponent()).getViewport().getView();
					if (x.getClass() == GeneralSentence.class) {
						setFocusTraversalPolicy(((GeneralSentence) generalPanel).getTabbedPaneOrder());
					} else if (x.getClass() == GeneralTrial.class) {
						setFocusTraversalPolicy(((GeneralTrial) generalPanel).getTabbedPaneOrder());
					} else if (x.getClass() == GeneralMiscAppeal.class) {
						setFocusTraversalPolicy(((GeneralMiscAppeal) generalPanel).getTabbedPaneOrder());
					} else if (x.getClass() == GeneralCriminalAppeal.class) {
						setFocusTraversalPolicy(((GeneralCriminalAppeal) generalPanel).getTabbedPaneOrder());
					} else if (x.getClass() == DefendantAppellantTab.class) {
						setFocusTraversalPolicy(((DefendantAppellantTab) defendantPanel).getTabbedPaneOrder());
					}
				}
			});

			// Set the basic template for the case management pages dependent on
			// caseType
			setCaseManagement(tabbedPane);

			displayTabs(xac.getCaseStatus(), tabbedPane);
			gbc.gridx = 0; // already done in getDefaultGridBagConstraints, just
							// for
							// code clarity
			gbc.gridy = 0;
			this.add(tabbedPane, gbc);

			// Add Create/Cancel button (in panel w/ no border)
			gbc.gridy++;
			gbc.anchor = GridBagConstraints.EAST;
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 0.0;
			gbc.weighty = 0.0;
			this.add(buttonPanel, gbc);

			if (this.resizeDimension != null)
				xac.repaintScreen(new Dimension(this.resizeDimension.width + 200, this.resizeDimension.height + 200));

			if (xac.getCaseStatus().getCaseProcess() == CaseProcess.NEW) {
				// Disable tabs other than general tab, re-enable once create
				// case is pressed etc. as per SAYG instructions
				tabbedPane.setEnabledAt(1, false); // defendant
				tabbedPane.setEnabledAt(2, false); // prosecutor
			} else {
				tabbedPane.setEnabledAt(1, true); // defendant
				tabbedPane.setEnabledAt(2, true); // prosecutor

				finishButton.setEnabled(true);
				createButton.setEnabled(false);
			}

			if (this.resizeDimension != null)
				xac.repaintScreen(new Dimension(this.resizeDimension.width + 200, this.resizeDimension.height + 200));
		} catch (CaseControllerException ex) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + ex);
			XHIBITConstant.handleError(ex, this.getClass());
		} catch (ChargeControllerException e1) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e1);
			XHIBITConstant.handleError(e1, this.getClass());
		}
	}

	private void setCaseManagement(JTabbedPane tabbedPane) throws ChargeControllerException {
		switch (xac.getCaseStatus().getCaseType()) {
		case APPEAL:
			generalPanel = new GeneralCriminalAppeal(this);
			this.resizeDimension = new Dimension(1100, 550);
			tabbedPane.setPreferredSize(new Dimension(1100, 650));
			if (caseBasicValue != null) {
				((GeneralCriminalAppeal) generalPanel).populate(caseBasicValue);
				CaseMethods.checkMandatoryFields(this, ((GeneralCriminalAppeal) generalPanel).getMandatoryFields(),((GeneralCriminalAppeal) generalPanel).getFieldsChangedGlobal());
			}
			this.setFocusTraversalPolicy(((GeneralCriminalAppeal) generalPanel).getTabbedPaneOrder());
			break;
		case SENTENCE:
			generalPanel = new GeneralSentence(this);
			this.resizeDimension = new Dimension(1000, 400);
			tabbedPane.setPreferredSize(new Dimension(1000, 650));
			if (caseBasicValue != null) {
				((GeneralSentence) generalPanel).populate(caseBasicValue);
				CaseMethods.checkMandatoryFields(this, ((GeneralSentence) generalPanel).getMandatoryFields(),((GeneralSentence) generalPanel).getFieldsChangedGlobal());
			}
			this.setFocusTraversalPolicy(((GeneralSentence) generalPanel).getTabbedPaneOrder());
			break;
		case TRIAL:
			generalPanel = new GeneralTrial(this);
			this.resizeDimension = new Dimension(1500, 830);
			tabbedPane.setPreferredSize(new Dimension(1150, 850));
			if (caseBasicValue != null) {
				((GeneralTrial) generalPanel).populate(caseBasicValue, refusedBroadcastCaseArray);
				CaseMethods.checkMandatoryFields(this, ((GeneralTrial) generalPanel).getMandatoryFields(),((GeneralTrial) generalPanel).getFieldsChangedGlobal());
			}
			this.setFocusTraversalPolicy(((GeneralTrial) generalPanel).getTabbedPaneOrder());
			break;
		case MISC:
			generalPanel = new GeneralMiscAppeal(this);
			this.resizeDimension = new Dimension(1000, 550);
			tabbedPane.setPreferredSize(new Dimension(1000, 650));
			if (caseBasicValue != null) {
				((GeneralMiscAppeal) generalPanel).populate(caseBasicValue);
				CaseMethods.checkMandatoryFields(this, ((GeneralMiscAppeal) generalPanel).getMandatoryFields(),((GeneralMiscAppeal) generalPanel).getFieldsChangedGlobal());
			}
			setFocusTraversalPolicy(((GeneralMiscAppeal) generalPanel).getTabbedPaneOrder());
			break;
		}
		// Added scroll pane so that general panel will display a scroll bar if
		// it goes below dimensions that distort entry fields
		JScrollPane scrollPane = new JScrollPane(generalPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		generalPanel.setPreferredSize(new Dimension(this.resizeDimension.width - 50, this.resizeDimension.height - 50));
		tabbedPane.addTab("General", null, scrollPane, null);
	}

	private void displayTabs(final CaseStatus caseStatus, JTabbedPane tabbedPane) {
		// --- Only display prosecutor tab for trial & sentence cases ---
		if (xac.getCaseStatus().isCaseType(CaseType.TRIAL) || xac.getCaseStatus().isCaseType(CaseType.SENTENCE)) {
			// new ProsecutorRespondentTab

			prosecutorRespondentPanel = new ProsecutorRespondentTab(xac, this);
			if (caseBasicValue != null) { 
				((ProsecutorRespondentTab) prosecutorRespondentPanel).populate(caseBasicValue);
			}
			JScrollPane PRscrollPane = new JScrollPane(prosecutorRespondentPanel,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			prosecutorRespondentPanel.setPreferredSize(new Dimension(1000, 580));
			tabbedPane.addTab("Prosecutor", null, PRscrollPane, null);
		}

		defendantPanel = new DefendantAppellantTab(xac, this);
		if (caseBasicValue != null) {
			((DefendantAppellantTab) defendantPanel).populate(caseBasicValue);
		}
		String tabName = "Defendant";
		if (xac.getCaseStatus().isCaseType(CaseType.APPEAL) || xac.getCaseStatus().isCaseType(CaseType.MISC)) {
			tabName = "Appellant";
		}

		// Added scroll pane so that general panel will display a scroll bar if
		// it goes below dimensions that distort entry fields
		JScrollPane scrollPane = new JScrollPane(defendantPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		defendantPanel.setPreferredSize(new Dimension(1000, 580));
		tabbedPane.addTab(tabName, null, scrollPane, null);

		// --- Only display Respondent tab for appeal cases ---
		if (xac.getCaseStatus().isCaseType(CaseType.APPEAL) || xac.getCaseStatus().isCaseType(CaseType.MISC)) {
			prosecutorRespondentPanel = new ProsecutorRespondentTab(xac, this);
			if (caseBasicValue != null) {
				((ProsecutorRespondentTab) prosecutorRespondentPanel).populate(caseBasicValue);
			}
			JScrollPane PRscrollPane = new JScrollPane(prosecutorRespondentPanel,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			prosecutorRespondentPanel.setPreferredSize(new Dimension(1000, 580));
			tabbedPane.addTab("Respondent", null, PRscrollPane, null);
		}
	}

	public void stepInitialise() throws CSRecoverableException {
	}

	public void stepActivate() throws CSRecoverableException {
	}

	public void stepUpdateViewState() throws CSRecoverableException {
	}

	public void stepValidate() throws CSValidationException, CSRecoverableException {
	}

	public void stepDeactivate() throws CSRecoverableException {
	}

	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		xac.getCaseStatus().setCaseId(0);
	}

	public Boolean validateDefendant(final List<DefendantAppellant> def) {
		boolean complete = true;

		if (xac.getCaseStatus().isCaseType(CaseType.TRIAL)) {
			for (DefendantAppellant d : def) {
				if (d.getBCStatus().getCode() == null || d.getBCStatus().getCode().equals("")) {
					complete = false;
				}
				// --- CTX-1884 - Start ---
				if (((GeneralTrial) generalPanel).getReceiptType().equals("EW")
						|| ((GeneralTrial) generalPanel).getReceiptType().equals("IO")
						|| ((GeneralTrial) generalPanel).getReceiptType().equals("ST")) {
					if (d.getFirstDate() == null || d.getFinalDate() == null) {
						complete = false;
					}
				}
				// --- CTX-1884 - End ---
			}
		}
		if (xac.getCaseStatus().isCaseType(CaseType.SENTENCE)) {
			for (DefendantAppellant d : def) {
				if (d.getBCStatus().getCode() == null || d.getBCStatus().getCode().equals("")) {
					complete = false;
				}
			}
		}

		return complete;
	}

	public JPanel getButtonPanel() {
		return buttonPanel;
	}

	public JButton getCreateButton() {
		return createButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getFinishButton() {
		return finishButton;
	}

	private CaseBasicValue populateBasicValue(CaseType caseType) {
		CaseBasicValue val = new CaseBasicValue();
		switch (caseType) {
		case TRIAL:
			refusedBroadcastCaseArray = new ArrayList<RefusedBroadcastCaseBasicValue>();
			val = ((GeneralTrial) generalPanel).populateCaseBasicValue(refusedBroadcastCaseArray);
			break;
		case SENTENCE:
			val = ((GeneralSentence) generalPanel).populateCaseBasicValue();
			break;
		case MISC:
			val = ((GeneralMiscAppeal) generalPanel).populateCaseBasicValue();
			break;
		case APPEAL:
			val = ((GeneralCriminalAppeal) generalPanel).populateCaseBasicValue();
			break;
		}
		return val;
	}

	private void populateCaseNumberField(CaseType caseType, String caseNumber) {
		switch (caseType) {
		case TRIAL:
			((GeneralTrial) generalPanel).setCaseNumber(caseNumber);
			break;
		case SENTENCE:
			((GeneralSentence) generalPanel).setCaseNumber(caseNumber);
			break;
		case MISC:
			((GeneralMiscAppeal) generalPanel).setCaseNumber(caseNumber);
			break;
		case APPEAL:
			((GeneralCriminalAppeal) generalPanel).setCaseNumber(caseNumber);
			break;
		}
	}

	private void populateCaseBasicValue(CaseType caseType, CaseBasicValue basicValue) {
		switch (caseType) {
		case TRIAL:
			((GeneralTrial) generalPanel).populateCaseBasicValue(basicValue, refusedBroadcastCaseArray);
			break;
		case SENTENCE:
			((GeneralSentence) generalPanel).populateCaseBasicValue(basicValue);
			break;
		case MISC:
			((GeneralMiscAppeal) generalPanel).populateCaseBasicValue(basicValue);
			break;
		case APPEAL:
			((GeneralCriminalAppeal) generalPanel).populateCaseBasicValue(basicValue);
			break;
		}
	}

	public void defendantCountResponder(Integer count) {
		defendantPanel.setDefendantCount(count);
	}

	public int getCurrentNoOfDefendants() {
		return defendantPanel.getDefendantInTableCount();
	}

	/**
	 * Used in GeneralTrial for search charges purposes.
	 * 
	 * @return the current application controller
	 * 
	 **/
	public XhibitApplicationController getXac() {
		return xac;
	}

	/*
	 * Action Listeners
	 */
	private class cancelButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				String abortMessage = null;
				if (xac.getCaseStatus().isCaseProcess(CaseProcess.AMEND)) {
					abortMessage = caseAmendAbort;
				} else {
					abortMessage = caseCreateAbort;
				}
				
				int result = JOptionPane.showConfirmDialog(xac,
						abortMessage,
						"Confirm Discard", JOptionPane.OK_CANCEL_OPTION);
				if (0 == result) {
					log.debug("Case cancelled, unsetting case create in progress flag");
					xac.getCaseStatus().setCaseCreateInProgressFlag(false);
					xac.getCaseStatus().setCaseId(0);
					xac.close();
				}
			} catch (CSRecoverableException e1) {
				log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e1);
				XHIBITErrorHandler.handleError(e1, null, defaultError);
			}
		}
	}

	// Additions for CTX-498
	private ApplicationCaseModel populateApplicationCaseModel(boolean overload, CaseBasicValue oldCBV,
			ArrayList<DefendantOnCaseBasicValue> colDefToAddToCase) {
		ApplicationCaseModel populatedACM = new ApplicationCaseModel();

		ScheduledHearingValue val = getSchedHearingValue(oldCBV);
		if (val != null) {
			populatedACM.setScheduledHearingValue(val);
		} else {
			populatedACM.setScheduledHearingValue(populateScheduledHearingValue(true, oldCBV, colDefToAddToCase));
		}
		populatedACM.setForAllDaysLogs(false);
		populatedACM.setForRangeLogs(false);
		populatedACM.setInEditMode(true);
		populatedACM.setXhibitApplicationController(xac);

		return populatedACM;
	}

	private ScheduledHearingValue populateScheduledHearingValue(boolean overload, CaseBasicValue oldCBV,
			ArrayList<DefendantOnCaseBasicValue> colDefToAddToCase) {
		ScheduledHearingValue populatedSHV = new ScheduledHearingValue();
		populatedSHV.setCaseBasicValue(oldCBV);
		populatedSHV.setCourtRoomValue(populateCourtRoomBasicValue(true));
		populatedSHV.setCourtSiteShortName("");
		populatedSHV.setCrestCourtId("");
		populatedSHV.setCurrentStatus("");
		populatedSHV.setCurrentStatusTime(Calendar.getInstance());
		populatedSHV.setDefendantOnCaseBasicValues(colDefToAddToCase);
		populatedSHV.setHearingListStartDate(Calendar.getInstance());
		populatedSHV.setId(null);
		populatedSHV.setIsFloating(false);
		populatedSHV.setJudge("");
		populatedSHV.setScheduledHearingBasicValue(populateScheduledHearingBasicValue(true));
		populatedSHV.setSittingSequenceNo(0);
		populatedSHV.setVersion(0);

		return populatedSHV;
	}

	private ScheduledHearingBasicValue populateScheduledHearingBasicValue(boolean overload) {
		ScheduledHearingBasicValue populatedSHBV = new ScheduledHearingBasicValue();

		populatedSHBV.setAddHearingUsed("");
		populatedSHBV.setDateOfHearing(Calendar.getInstance().getTime());
		populatedSHBV.setEndTime(Calendar.getInstance().getTime());
		populatedSHBV.setHearingID(0);
		populatedSHBV.setHearingProgress(0);
		populatedSHBV.setId(null); // again, not written to db, no need for this
									// (primary key)
		populatedSHBV.setIsCaseActive(true);
		populatedSHBV.setLinkedSHID(0);
		populatedSHBV.setListingNote("");
		populatedSHBV.setMovedFrom("");
		populatedSHBV.setMovedFromCourtRoomId(0);
		populatedSHBV.setNotBeforeTime(Calendar.getInstance().getTime());
		populatedSHBV.setOriginalTime(Calendar.getInstance().getTime());
		populatedSHBV.setSequenceNo(0);
		populatedSHBV.setSittingID(0);
		populatedSHBV.setStartTime(Calendar.getInstance().getTime());
		populatedSHBV.setVersion(0);

		return populatedSHBV;
	}

	private CourtRoomBasicValue populateCourtRoomBasicValue(boolean overload) {
		CourtRoomBasicValue populatedCRBV = new CourtRoomBasicValue();

		populatedCRBV.setCourtRoomName("Court N/A");
		populatedCRBV.setCourtSiteId(0);
		populatedCRBV.setCrestCourtRoomNo(0);
		populatedCRBV.setDescription("");
		populatedCRBV.setDisplayName("");
		populatedCRBV.setId(0); // not needed for db
		populatedCRBV.setLocation("");
		populatedCRBV.setVersion(0);

		return populatedCRBV;
	}

	public JPanel getGeneralPanel() {
		return generalPanel;
	}

	private GridBagConstraints getDefaultGridBagConstraints() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0);
	}

	public DefendantAppellantTab getDefendantPanel() {
		return defendantPanel;
	}

	private void clearGeneralTabChangedState(CaseType caseType) {
		switch (caseType) {
		case TRIAL:
			((GeneralTrial) generalPanel).clearChangedState();
			break;
		case SENTENCE:
			((GeneralSentence) generalPanel).clearChangedState();
			break;
		case MISC:
			((GeneralMiscAppeal) generalPanel).clearChangedState();
			break;
		case APPEAL:
			((GeneralCriminalAppeal) generalPanel).clearChangedState();
			break;
		}
	}

	private boolean saveCaseDetails(CaseType caseType) {
		// Determine which tab is the accessor
		if (tabbedPane.getSelectedComponent() instanceof JScrollPane) {
			// Because of added scroll pane to make grid bag layout work
			// properly it's necessary
			// to get the scroll pane surrounded the panel as this was what was
			// added to the JTabbedPane.
			// Inside this scroll pane is then a JViewport with the original
			// defendant panel attached.
			JScrollPane scrollPane = (JScrollPane) (tabbedPane.getSelectedComponent());

			if (((JViewport) scrollPane.getComponent(0)).getComponent(0) instanceof DefendantAppellantTab) {
				return saveDefendantTabDetails();
			} else if (((JViewport) scrollPane.getComponent(0)).getComponent(0) instanceof ProsecutorRespondentTab) {
				return saveProsecutorTabDetails();
			} else {
				return saveGeneralTabDetails(caseType);
			}
		} else {
			return false;
		}
	}

	private boolean saveGeneralTabDetails(CaseType caseType) {
		// Check details are valid, if not then return false
		if (!validateGeneralTab())
			return false;
		
		// Get current Case object from DB to avoid optimistic lock exception
		try {
			caseBasicValue = caseDelegate.getCase(caseBasicValue.getCaseId());
			// save current value of committal date or sent for trial date
			Timestamp prevCommittalDate = caseBasicValue.getCommittalDate();
			Timestamp prevSentForTrialDate = caseBasicValue.getSentForTrialDate();

			// Amend value of case object from DB with new information in
			// general tab
			populateCaseBasicValue(caseType, caseBasicValue);

			if(!televisedTrialCheck(caseType)) {
				return false;
			}
			
			List<ChargesLogBasicValue> chargesLogList = new ArrayList<ChargesLogBasicValue>();
			if (xac.getCaseStatus().isCaseType(CaseType.TRIAL)) {
				chargesLogList = ((GeneralTrial) generalPanel).getCharges();
			}

			// CTX-1919: if COMMITTAL_DATE or SENT_FOR_TRIAL_DATE has been
			// changed, then update
			boolean amendDefendants = false;
			Calendar newDate = Calendar.getInstance();
			// all def on case rows with new value (DATE_OF_COMMITTAL)
			if (caseType == CaseType.TRIAL || caseType == CaseType.SENTENCE) {
				if (prevCommittalDate != null) {
					if (caseBasicValue.getCommittalDate() != null
							&& !caseBasicValue.getCommittalDate().equals(prevCommittalDate)) {
						// committal date was present before and has changed
						amendDefendants = true;
						newDate.setTimeInMillis(caseBasicValue.getCommittalDate().getTime());
					} else if (caseBasicValue.getSentForTrialDate() != null) {
						// committal date was present before, now it's a sent
						// for trial date
						amendDefendants = true;
						newDate.setTimeInMillis(caseBasicValue.getSentForTrialDate().getTime());
					}
				} else if (prevSentForTrialDate != null) {
					if (caseBasicValue.getSentForTrialDate() != null
							&& !caseBasicValue.getSentForTrialDate().equals(prevSentForTrialDate)) {
						// sent for trial date was present before and has
						// changed
						amendDefendants = true;
						newDate.setTimeInMillis(caseBasicValue.getSentForTrialDate().getTime());
					} else if (caseBasicValue.getCommittalDate() != null) {
						// sent for trial date was present before, now it's a
						// committal date
						amendDefendants = true;
						newDate.setTimeInMillis(caseBasicValue.getCommittalDate().getTime());
					}
				}
			}

			if (amendDefendants) {
				// get any defendants attached to case
				ArrayList<DefendantOnCaseBasicValue> defsOnCase = (ArrayList<DefendantOnCaseBasicValue>) XhibitDelegateHelper
						.getDefendantDelegate().findByCaseId(caseBasicValue.getCaseId());

				// if any are actually present
				if (defsOnCase.size() > 0) {
					for (int i = 0; i < defsOnCase.size(); i++) {
						defsOnCase.get(i).setDateOfCommittal(newDate);
					}
					// amend case and defendant as normal
					caseDelegate.amendCaseAndDefendant(caseBasicValue, defsOnCase, XhibitSingleton.getInstance()
							.getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME), true);
					// re-populate DefendantAppellantTab to make sure def on
					// case id is properly available
					((DefendantAppellantTab) defendantPanel).clearTable();
					((DefendantAppellantTab) defendantPanel).populate(caseBasicValue);

					caseBasicValue = caseDelegate.getCase(xac.getCaseStatus().getCaseId());
					populateVersionPostSave(caseBasicValue.getVersion(), xac.getCaseStatus().getCaseType(), caseBasicValue.getCaseId());
				} else {
					caseDelegate.amendCase(caseBasicValue, chargesLogList, XhibitSingleton.getInstance()
							.getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME), refusedBroadcastCaseArray);

					// Update just the case entry
					caseBasicValue = caseDelegate.getCase(xac.getCaseStatus().getCaseId());
					populateVersionPostSave(caseBasicValue.getVersion(), xac.getCaseStatus().getCaseType(), caseBasicValue.getCaseId());
				}
			} else {
				caseDelegate.amendCase(caseBasicValue, chargesLogList, XhibitSingleton.getInstance().getUserSession()
						.getSessionProperty(UserTerminalProperties.USER_NAME), refusedBroadcastCaseArray);

				// Update just the case entry
				caseBasicValue = caseDelegate.getCase(xac.getCaseStatus().getCaseId());
				populateVersionPostSave(caseBasicValue.getVersion(), xac.getCaseStatus().getCaseType(), caseBasicValue.getCaseId());
			}

			defendantPanel.populateCaseTitle(caseBasicValue.getCaseTitle());
			((ProsecutorRespondentTab) prosecutorRespondentPanel).populateCaseTitle(caseBasicValue.getCaseTitle());
			return true;
		} catch (CaseControllerException e) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
			return false;
		} catch (ChargeControllerException e) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
			return false;
		}  catch (EJBException e) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
			return false;
		} catch (CSUnrecoverableException e) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
			return false;
		}

	}

	private boolean saveDefendantTabDetails() {
		// Get current Case object from DB to avoid optimistic lock exception
		try {
			caseBasicValue = caseDelegate.getCase(caseBasicValue.getCaseId());

			// To be populated from defendant appellant tab, used for updating
			// case records
			List<DefendantOnCaseBasicValue> defToAddToCase = null;

			((DefendantAppellantTab) defendantPanel).saveCurrentDetailsOnCaseCreate();

			List<DefendantAppellant> def = ((DefendantAppellantTab) defendantPanel).getDefendants();

			if (def.size() > 0) {
				Boolean valid = true;
				// Only executes any checks if sentence or trial case types
				if (xac.getCaseStatus().isCaseType(CaseType.SENTENCE)
						|| xac.getCaseStatus().isCaseType(CaseType.TRIAL)) {
					valid = validateDefendant(def);
					if (!valid) {
						JOptionPane.showMessageDialog(xac, "Not all defendants are complete",
								"Please complete all details", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					if (((DefendantAppellantTab) defendantPanel).multipleAnyErrors()) {
						return false;
					}
				} else if (xac.getCaseStatus().isCaseType(CaseType.APPEAL)) {
					valid = !(def.get(0).getMagCourtConviction() == null)
							&& !(def.get(0).getOriginalDateOfSentence() == null)
							&& !(def.get(0).getBCStatus().getCode() == null)
							&& !(def.get(0).getBCStatus().getCode().equals(""))
							&& !((DefendantAppellantTab) defendantPanel).anyErrors();
				}

				if (valid) {
					// save currently selected row's fields to the object.
					((DefendantAppellantTab) defendantPanel).saveCurrentDetailsOnCaseCreate();
					if (def.get(0).getOriginalDateOfSentence() != null) {
						caseBasicValue.setLcSentDate(new Timestamp(def.get(0).getOriginalDateOfSentence().getTime()));
					}
					if (def.get(0).getMagCourtConviction() != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTime((def.get(0).getMagCourtConviction()));
						caseBasicValue.setMagConvictionDate(cal);
					}

					defToAddToCase = ((DefendantAppellantTab) defendantPanel)
							.populateDefendantOnCaseBasicValue(caseBasicValue.getCaseId());

					// save date of committal to def on case
					for (DefendantOnCaseBasicValue docBV : defToAddToCase) {
						if (caseBasicValue.getCommittalDate() != null || caseBasicValue.getSentForTrialDate() != null) {
							docBV.setDateOfCommittal(returnCommittalDate(caseBasicValue));
						}
					}

					setDefendantDifferences((List<DefendantOnCaseBasicValue>) XhibitDelegateHelper
							.getDefendantDelegate().findByCaseId(caseBasicValue.getCaseId()), defToAddToCase);

					// amend case and defendant as normal
					caseDelegate.amendCaseAndDefendant(caseBasicValue, defToAddToCase, XhibitSingleton.getInstance()
							.getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME), true);
					caseBasicValue = caseDelegate.getCase(xac.getCaseStatus().getCaseId());

					populateVersionPostSave(caseBasicValue.getVersion(), xac.getCaseStatus().getCaseType(), caseBasicValue.getCaseId());

					// re-populate DefendantAppellantTab to make sure def on
					// case id is properly available
					((DefendantAppellantTab) defendantPanel).clearTable();
					((DefendantAppellantTab) defendantPanel).populate(caseBasicValue);

					return true;
				} else {
					return false;
				}
			}
		} catch (CaseControllerException e) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
			return false;
		} catch (ChargeControllerException e) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
			return false;
		} catch (EJBException e) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
			return false;
		} catch (CSUnrecoverableException e) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
			return false;
		}

		return false;
	}

	private boolean saveProsecutorTabDetails() {
		// Get current Case object from DB to avoid optimistic lock exception
		try {
			caseBasicValue = caseDelegate.getCase(caseBasicValue.getCaseId());

			// Temporary variables needed to be stored for amend to function
			List<CaseProsecutorAgencyValue> prosRespList = new ArrayList<CaseProsecutorAgencyValue>();

			// Add list of case prosecutor agency basic values to our local list
			if (((ProsecutorRespondentTab) prosecutorRespondentPanel).getProsecutorRespondentInTableCount() > 0) {
				prosRespList = ((ProsecutorRespondentTab) prosecutorRespondentPanel).getAmendProsecutorBasicValue();
			}

			// Update entry
			CaseProsecutorAgencyControllerBeanBusinessDelegate prosDelegate = XhibitDelegateHelper
					.getCaseProsecutorAgencyDelegate();
			prosDelegate.amendProsecutorAgency(caseBasicValue.getCaseId(), prosRespList, XhibitSingleton.getInstance()
					.getUserSession().getSessionProperty(UserTerminalProperties.USER_NAME));

			// Clear and populate respondent table
			((ProsecutorRespondentTab) prosecutorRespondentPanel).clearTable();
			((ProsecutorRespondentTab) prosecutorRespondentPanel).populate(caseBasicValue);
			((ProsecutorRespondentTab) prosecutorRespondentPanel).enableRepresentationButtons();

			return true;
		} catch (CaseControllerException e) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
			return false;
		} catch (EJBException e) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
			return false;
		} catch (CSUnrecoverableException e) {
			log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
			return false;
		}
	}

	private boolean validateGeneralTab() {
		List<JLabel> labels = new ArrayList<JLabel>();
		if (xac.getCaseStatus().isCaseType(CaseType.SENTENCE)) {
			labels = ((GeneralSentence) generalPanel).getValidationFields();
		} else if (xac.getCaseStatus().isCaseType(CaseType.TRIAL)) {
			labels = ((GeneralTrial) generalPanel).getValidationFields();
		} else if (xac.getCaseStatus().isCaseType(CaseType.APPEAL)) {
			labels = ((GeneralCriminalAppeal) generalPanel).getValidationFields();
		} else if (xac.getCaseStatus().isCaseType(CaseType.MISC)) {
			labels = ((GeneralMiscAppeal) generalPanel).getValidationFields();
		}
		boolean isValid = true;
		for (int i = 0; i < labels.size(); i++) {
			if (!(labels.get(i).getText().isEmpty()) && !(labels.get(i).getText().equals(" "))) {
				isValid = false;
				break;
			}
		}

		return isValid;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	// Method used to disable tabs based on enabled state of create case / save
	// button
	private void enableTabbedPane() {
		// Iterates through tabs and disables, removes need to distinguish
		// between case types
		for (int i = 0; i < tabbedPane.getComponentCount(); i++) {
			if (tabbedPane.getSelectedIndex() != i) {
				tabbedPane.setEnabledAt(i, !createButton.isEnabled());
			}
		}
	}

	private void setDefendantDifferences(List<DefendantOnCaseBasicValue> oldDOC,
			List<DefendantOnCaseBasicValue> newDOC) {
		// check if def on case entries have changed
		boolean defendantDifference = false;
		for (int i = 0; i < oldDOC.size(); i++) {
			for (int j = 0; j < newDOC.size(); j++) {
				if (oldDOC.get(i).getDefendantID().equals(newDOC.get(j).getDefendantID())) {
					// Check masked details, nationality, PTIURN, ASN & last
					// conviction date
					defendantDifference |= !CaseUtils.compareObjectsWithNullCheck(oldDOC.get(i).getIsMasked(),
							newDOC.get(i).getIsMasked());
					defendantDifference |= !CaseUtils.compareObjectsWithNullCheck(oldDOC.get(i).getMaskedName(),
							newDOC.get(i).getMaskedName());
					defendantDifference |= !CaseUtils.compareObjectsWithNullCheck(oldDOC.get(i).getNationality(),
							newDOC.get(i).getNationality());
					defendantDifference |= !CaseUtils.compareObjectsWithNullCheck(oldDOC.get(i).getPtiurn(),
							newDOC.get(i).getPtiurn());
					defendantDifference |= !CaseUtils.compareObjectsWithNullCheck(oldDOC.get(i).getAsn(),
							newDOC.get(i).getAsn());

					try {
						CaseBasicValue cbv = caseDelegate.getCase(caseBasicValue.getCaseId());

						if (cbv.getCaseListed() != null && cbv.getCaseListed().equals("Y")) {
							if (defendantDifference) {
								newDOC.get(i).setDifferenceReport("Y");
							}
						}
					} catch (CaseControllerException e) {
						log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
						XHIBITConstant.handleError(e, this.getClass());
					} catch (EJBException e) {
						log.error(ERROR_IN + PACKAGE_NAME + CLASS_NAME + " : " + e);
						XHIBITConstant.handleError(e, this.getClass());
					}
					break;
				}
			}
		}
	}

	public Calendar returnCommittalDate(CaseBasicValue cbv) {
		Calendar newDate = Calendar.getInstance();

		if (caseBasicValue.getCommittalDate() != null) {
			newDate.setTimeInMillis(caseBasicValue.getCommittalDate().getTime());
		} else if (caseBasicValue.getSentForTrialDate() != null) {
			newDate.setTimeInMillis(caseBasicValue.getSentForTrialDate().getTime());
		}

		return newDate;
	}

	public void populateVersionPostSave(Integer version, CaseType type, Integer caseId) throws ChargeControllerException {
		switch (type) {
		case TRIAL:
			((GeneralTrial) generalPanel).setVersion(version);
			break;
		case SENTENCE:
			((GeneralSentence) generalPanel).setVersion(version);
			((GeneralSentence) generalPanel).setCharges(caseId);
			break;
		case MISC:
			((GeneralMiscAppeal) generalPanel).setVersion(version);
			break;
		case APPEAL:
			((GeneralCriminalAppeal) generalPanel).setVersion(version);
			break;
		}
	}

	public ScheduledHearingValue getSchedHearingValue(CaseBasicValue cv) {
		CaseHelper ch = new CaseHelper();
		try {
			Collection listShv = ch.getScheduledHearings(cv.getCaseType(), cv.getCaseNumber(),
					XhibitSingleton.getInstance().getCourtId());
			Iterator iter = listShv.iterator();
			uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue largestShv = null;
			while (iter.hasNext()) {
				uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue item = (uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue) iter
						.next();
				if (largestShv == null) {
					largestShv = item;
				} else {
					largestShv = getLargestSHV(largestShv, item);
				}
			}

			if (largestShv != null) {
				Integer shvId = largestShv.getScheduledHearingID();
				ScheduledHearingValue[] ts_Shv = XhibitDelegateHelper.getHearingDelegate()
						.getScheduledHearings(new Integer[] { shvId });
				return ts_Shv[0];
			} else {
				return null;
			}

		} catch (CaseControllerException e) {
			log.error("No Scheduled Hearing Values for " + cv.getCaseNumber());
			return null;
		} catch (HearingScheduleException e) {
			log.error("No Scheduled Hearing Values for " + cv.getCaseNumber());
			return null;
		}
	}

	private static uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue getLargestSHV(
			uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue shv1,
			uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue shv2) {
		return shv1.getScheduledHearingDate().getTime().compareTo(shv2.getScheduledHearingDate().getTime()) > 0 ? shv1
				: shv2;
	}
	
	/**
	 * Returns false if it's a trial case and app granted = N but no checkboxes
	 * have been selected 
	 */
	private boolean televisedTrialCheck(CaseType caseType) {
		boolean isValid = true;
		//if its a general tab we need to ensure that 
		//validation is correct on app granted
		if(caseType == CaseType.TRIAL 
			&& "N".equals(caseBasicValue.getTelevisedAppGranted()) 
			&& !((GeneralTrial) generalPanel).isAppGrantedFalsedAndReasonProvided()){
			JOptionPane.showMessageDialog(xac, "If televised case application has been refused at least one reason must be provided",
						"Error", JOptionPane.ERROR_MESSAGE);
			isValid= false;
		}
		return isValid;
	}
}
