package uk.gov.courtservice.xhibit.client.updatecase;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.HearingHeaderValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.LegalRepValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.HearingHeaderValueHelper;
import uk.gov.courtservice.xhibit.client.util.UnknownCaseTypeException;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.XhibitProperties;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Xhibit Update Case Panel
 * </p>
 * <p>
 * Description: This panel contains the tabs (and their contents pannels) to
 * update a Case This is NOT the panel to be instantiated by Case Properties
 * component consummers. Instead, consumers must instantiate UpdateCaseDialog
 * passing a Header Value object.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 * @version 1.1 Iteration 2.a rework - new Mid Tier and Value Objects.
 */
public class UpdateCasePanel extends XPanel {

	private static final long serialVersionUID = 1L;

	public static final String xADDDefenceRepresentation = "Add.DefenceRep";

	public static final String xREMDefenceRepresentation = "Rem.DefenceRep";

	private final Logger log = CSServices.getLogger(UpdateCasePanel.class);

	// only to be set in the constructor
	protected final UpdateCaseDialog ucd;

	private UpdateGeneralCaseData xGeneralCaseData;

	private UpdateProsecutionCaseData xProsecutionCaseData;

	private UpdateDefendantCaseData xDefenceCaseData;

	private UpdateCourtStaffCaseData xCourtStaffCaseData;

	protected HearingHeaderValue hhv = null;

	private Boolean isExported = null;

	protected Integer scheduledHearingId;

	protected Date scheduleHearingStartTime;

	protected HearingHeaderValueHelper hhvh;

	private JTabbedPane updateCaseTabbedPane = null;

	private boolean useDummyData = false;

	public boolean cancelledPrompt = false;

	public HearingRecordModel hearingRecordModel;

	public UpdateCasePanel(UpdateCaseDialog ucd) throws Exception {
		try {
			this.ucd = ucd;
			this.initUseDummyData();
			String componentDialogName = XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "name");
			if (componentDialogName == null) {
				componentDialogName = "Update Case Properties";
				log.error(
						"Update Case Component Dialog Name not found in the resource bundle. Hardcoded 'Update Case Properties' used instead.");
			}
			this.setName(componentDialogName);
			// jbInit(); // do not call jbInit() here - the life cycle
			// methods will/must do this.
		} catch (Exception e) {
			log.error(e);
			throw (new CSRecoverableException("gui.updateCase.ConstructorFailed",
					"Exception thrown whilst constructing the UpdateCase instance.", e));
		}
	}

	/**
	 * Returns whether the current HearingHeaderValue (passed in at
	 * construction) is representing an 'Appeal hearing'
	 * 
	 * @return
	 */
	public boolean isCriminalAppealHearing() {
		try {
			return CaseTypeHelper.isCriminalAppeal_CaseType(this.hhv);
		} catch (Exception e) {
			log.debug("UpdateCase can not determine whether the current case is a Combined Case.");
			return false;
		}
	}

	/**
	 * Returns whether the current HearingHeaderValue (passed in at
	 * construction) is representing a 'Normal Hearing'
	 * 
	 * @return
	 */
	public boolean isNormalHearing() {
		try {
			return CaseTypeHelper.isNormal_CaseType(this.hhv);
		} catch (Exception e) {
			log.debug("UpdateCase can not determine whether the current case is a Combined Case.");
			return false;
		}
	}

	/**
	 * Checks whether the current HearingHeaderValue (passed in at construction)
	 * is representing a 'C Case'
	 * 
	 * @return
	 */
	public boolean isCombinedCase() {
		try {
			return CaseTypeHelper.isCombinedCourt_CaseType(this.hhv);
		} catch (Exception e) {
			log.debug("UpdateCase can not determine whether the current case is a Combined Case.");
			return false;
		}
	}

	public boolean isMiscAppealCase() {
		try {
			return CaseTypeHelper.isMiscelleanousAppeal_CaseType(this.hhv);
		} catch (Exception e) {
			log.debug("UpdateCase can not determine whether the current case is a Misc Appeal Case.");
			return false;
		}
	}

	/**
	 * Checks whether the current HearingHeaderValue (passed in at construction)
	 * is representing a 'U Case'
	 * 
	 * @return
	 */
	public boolean isUndefindedCase() {
		try {
			return (CaseTypeHelper.isUndefined_CaseType(this.hhv));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks whether the current HearingHeaderValue (passed in at construction)
	 * is representing a 'T Case'
	 * 
	 * @return
	 */
	public boolean isTrialCase() {
		try {
			return (CaseTypeHelper.isTrial_CaseType(this.hhv));
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * This init method creates the 'Sub Components' (the tabs 'General',
	 * 'Prosecution', 'Defence/Appellant' and 'Court Staff', puts them into a
	 * tabbed view and maintains references for later processing.
	 * 
	 * @throws Exception
	 */
	void jbInit() throws Exception {
		// validate the clhv
		// chech the case type first - if unknow then fail the whole component

		try {
			// now we are sure we can determine the case (incl. sub) type,
			// we shall proceed...
			updateCaseTabbedPane = new JTabbedPane();
			// the panels created below, that are to serve as tabs, are
			// JPanel, not XPanel, cause
			// there is no need for individual life cycle maintenance per
			// tab (only needed on 'main level' ~ this level)
			// as the data is being passed in in the constructor. (This
			// class here is basically controlling the tabs)

			// GENERAL CASE DATA TAB CREATION
			try {
				this.xGeneralCaseData = new UpdateGeneralCaseData(this);
				updateCaseTabbedPane.add(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
						"updateGeneralCasePropertiesTab"), this.xGeneralCaseData);
				updateCaseTabbedPane.setSelectedIndex(0);
				if (ucd.internalDebug)
					log.debug("jbInit() added the UpdateGeneralCaseData component.");
			} catch (Exception e) {
				log.error(e);
				throw new CSRecoverableException("gui.updateCase.xGeneralCaseData.failed",
						"The UpdateCase component experienced trouble constructing the UpdateGeneralCaseData tab!!", e);
			}

			/* the following tabs do not apply to U and C cases */
			if (hasProsecutionTab()) {
				// PROSECUTION CASE DATA TAB CREATION
				try {
					this.xProsecutionCaseData = new UpdateProsecutionCaseData(this);
					String tabName;
					if (isCriminalAppealHearing() || isMiscAppealCase()) {
						tabName = XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
								"updateProsecutionCasePropertiesTab.appeal");
					} else {
						tabName = XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
								"updateProsecutionCasePropertiesTab.normal");
					}
					updateCaseTabbedPane.add(tabName, this.xProsecutionCaseData);
					if (ucd.internalDebug)
						log.debug("jbInit() added the UpdateProsecutionCaseData component.");
				} catch (Exception e) {
					log.error(e);
					throw new CSRecoverableException("gui.updateCase.xProsecutionCaseData.failed",
							"The UpdateCase component experienced trouble constructing the UpdateProsecutionCaseData tab!!",
							e);
				}
				// DEFENCE CASE DATA TAB CREATION
				try {
					this.xDefenceCaseData = new UpdateDefendantCaseData(this);
					String tabName;
					if (isCriminalAppealHearing() || isMiscAppealCase()) {
						tabName = XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
								"updateDefendantCasePropertiesTab.appeal");
					} else {
						tabName = XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
								"updateDefendantCasePropertiesTab.normal");
					}
					updateCaseTabbedPane.add(tabName, this.xDefenceCaseData);
					if (ucd.internalDebug)
						log.debug("jbInit() added the UpdateDefendantCaseData component.");
				} catch (Exception e) {
					log.error(e);
					throw new CSRecoverableException("gui.updateCase.xDefenceCaseData.failed",
							"The UpdateCase component experienced trouble constructing the UpdateDefendantCaseData tab!!",
							e);
				}
			}

			/* the following tabs do not apply to C cases */
			if (!isCombinedCase()) {
				// COURT STAFF CASE DATA TAB CREATION
				try {
					this.xCourtStaffCaseData = new UpdateCourtStaffCaseData(this);
					updateCaseTabbedPane.add(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
							"updateCourtStaffCasePropertiesTab"), this.xCourtStaffCaseData);
					if (ucd.internalDebug)
						log.debug("jbInit() added the xCourtStaffCaseData component.");
				} catch (Exception e) {
					log.error(e);
					throw new CSRecoverableException("gui.updateCase.xCourtStaffCaseData.failed",
							"The UpdateCase component experienced trouble constructing the UpdateCourtStaffCaseData tab!!",
							e);
				}
			}

			setLayout(new BorderLayout());
			add(updateCaseTabbedPane, BorderLayout.CENTER);
			setSize(updateCaseTabbedPane.getPreferredSize());
		} catch (UnknownCaseTypeException e) {
			log.error(e);
			throw new CSRecoverableException("gui.updateCase.noCaseType",
					"The UpdateCase component was passed a CourtLogHeaderValue with invalid caseType/caseSubType!!", e);
		} catch (Exception e) {
			log.error(e);
			throw new CSRecoverableException("gui.updateCase.noCaseType",
					"The UpdateCase component was passed a CourtLogHeaderValue with invalid caseType/caseSubType!!", e);
		}
		if (ucd.internalDebug)
			log.debug("jbInit() completed.");
	}

	public void stepInitialise() {
		if (ucd.internalDebug)
			log.debug("stepInitialise()");
		this.xGeneralCaseData = null;
		this.xCourtStaffCaseData = null;
		this.xDefenceCaseData = null;
		this.xProsecutionCaseData = null;
	}

	/**
	 * XPanel life cycle method implementation which either creates dummy data
	 * or hits the XHIBIT mid tier to load the data
	 * 
	 * @throws CSRecoverableException
	 */
	public void stepActivate() throws CSRecoverableException {
		if (ucd.internalDebug)
			log.debug(".stepActivitate() - loading data");

		try {
			// GET THE DATA
			if (this.useDummyData) {
				// do not connect to mid tier, rather create value objects
				// ourselves.
				log.debug("this.useDummyData == true - start creating dummy data");
				try {
					this.hhv = new HearingHeaderValue();
					final CaseBasicValue theCase = new CaseBasicValue();
					hhv.setAttendeeHistoryValues(new Vector());
					theCase.setCaseNumber(new Integer(7070707));
					theCase.setCaseType("A");
					theCase.setCaseSubType("O"); // not relevant when
					// case type is of type
					// C or U.
					hhv.setHhCase(theCase);
					hhv.setHearingType("T");
					hhv.setTimeListed(new GregorianCalendar(2003, 03, 21, 10, 00));
					// defendants - denfece LEGALREPVALUES
					PersonValue defendant1 = new PersonValue(new Integer(0), new Integer(0), new Integer(0),
							new Integer(0), "mr defendant 1 - p4", PersonValue.DEFENDANT);
					PersonValue defendant2 = new PersonValue(new Integer(0), new Integer(0), new Integer(0),
							new Integer(0), "mr defendant 2 - p4", PersonValue.DEFENDANT);
					PersonValue defendant3 = new PersonValue(new Integer(0), new Integer(0), new Integer(0),
							new Integer(0), "mr defendant 3 - p4", PersonValue.DEFENDANT);

					PersonValue defence1 = new PersonValue(new Integer(0), new Integer(0), new Integer(0),
							new Integer(0), "mr defence 1- p4", PersonValue.DEFENCE);
					PersonValue defence2 = new PersonValue(new Integer(0), new Integer(0), new Integer(0),
							new Integer(0), "mr defence 2- p4", PersonValue.DEFENCE);
					SHLegRepBasicValue shlrp = new SHLegRepBasicValue(new Integer(0), new Integer(0));

					LegalRepValue lrv1 = new LegalRepValue(new Integer(0), new Integer(0));
					lrv1.setCcInfo("Mr Defence 1 says xxx about defendant 1");
					lrv1.setDefendant(defendant1);
					lrv1.setLegalRep(defence1);
					lrv1.setSHLegRep(shlrp);

					LegalRepValue lrv2 = new LegalRepValue(new Integer(0), new Integer(0));
					lrv2.setCcInfo("Mr Defence 2 says xxx about defendant 1");
					lrv2.setDefendant(defendant1);
					lrv2.setLegalRep(defence2);
					lrv2.setSHLegRep(shlrp);

					LegalRepValue lrv3 = new LegalRepValue(new Integer(0), new Integer(0));
					lrv3.setCcInfo("Mr Defence 2 says xxx about defendant 2");
					lrv3.setDefendant(defendant2);
					lrv3.setLegalRep(defence2);
					lrv3.setSHLegRep(shlrp);

					LegalRepValue lrv4 = new LegalRepValue(new Integer(0), new Integer(0));
					lrv4.setCcInfo("Mr Defence 2 says xxx about defendant 3");
					lrv4.setDefendant(defendant3);
					lrv4.setLegalRep(defence2);
					lrv4.setSHLegRep(shlrp);

					Vector<LegalRepValue> legalRepValues = new Vector<LegalRepValue>();
					legalRepValues.add(lrv1);
					legalRepValues.add(lrv2);
					legalRepValues.add(lrv3);
					legalRepValues.add(lrv4);

					hhv.setLegalRepValues(legalRepValues);

					// Staff
					PersonValue judge1 = new PersonValue(new Integer(0), new Integer(0), new Integer(0), new Integer(0),
							"mr judge 1 - p4", PersonValue.JUDGE);
					PersonValue courtreporter2 = new PersonValue(new Integer(0), new Integer(0), new Integer(0),
							new Integer(0), "mr court reporter 2 - p4", PersonValue.COURT_REPORTER);

					PersonValue usher1 = new PersonValue(new Integer(0), new Integer(0), new Integer(0), new Integer(0),
							"mr usher 1 - p4", PersonValue.USHER);
					PersonValue usher2 = new PersonValue(new Integer(0), new Integer(0), new Integer(0), new Integer(0),
							"mr usher 2 - p4", PersonValue.USHER);
					PersonValue courtclerk1 = new PersonValue(new Integer(0), new Integer(0), new Integer(0),
							new Integer(0), "mr courtClerk 1 - p4", PersonValue.COURT_CLERK);
					PersonValue courtclerk2 = new PersonValue(new Integer(0), new Integer(0), new Integer(0),
							new Integer(0), "mr courtClerk 2 - p4", PersonValue.COURT_CLERK);

					PersonValue justice1 = new PersonValue(new Integer(0), new Integer(0), new Integer(0),
							new Integer(0), "mr justice 1", PersonValue.JUSTICE);
					PersonValue justice2 = new PersonValue(new Integer(0), new Integer(0), new Integer(0),
							new Integer(0), "mr justice 2", PersonValue.JUSTICE);

					Vector<PersonValue> staffValues = new Vector<PersonValue>();
					staffValues.add(judge1);
					staffValues.add(courtreporter2);
					staffValues.add(usher1);
					staffValues.add(usher2);
					staffValues.add(justice1);
					staffValues.add(justice2);

					staffValues.add(courtclerk1);
					staffValues.add(courtclerk2);
					hhv.setStaffValues(staffValues);
					setExported(new Boolean(false));
					log.debug("this.useDummyData == true - finished creating dummy data");
				} catch (Exception eeee) {
					log.error("OpenUpdateCasePropertiestAction: trouble creating dummy data : " + eeee);
					log.error(eeee);
					throw (eeee);
				}
			} else {
				if (ucd.internalDebug)
					log.debug("this.useDummyData != true - start hitting midtier");

				hearingRecordModel = ucd.openingAction.getHearingRecordModel();				
				if (ucd.openingAction.getScheduledHearingID2BOpened() != null) {
					scheduledHearingId = ucd.openingAction.getScheduledHearingID2BOpened();
					if (ucd.internalDebug)
						log.debug("OpenUpdateCasePropertiesAction: openingAction.getScheduledHearingID2BOpened="
								+ ucd.openingAction.getScheduledHearingID2BOpened()
								+ ". now set it to null to avoid opneing this id again without explicitly being told/the id being reset. ");
					ucd.openingAction.setScheduledHearingID2BOpened(null);
				} else {
					ApplicationCaseModel acm = (ApplicationCaseModel) ucd.openingAction.getModel();
					if (ucd.internalDebug)
						log.debug("OpenUpdateCasePropertiesAction: ApplicationCaseModel=" + acm);
					scheduledHearingId = acm.getScheduledHearingId();
					scheduleHearingStartTime = acm.getScheduledHearingValue().getScheduledHearingBasicValue()
							.getStartTime();
					if (ucd.internalDebug)
						log.debug("OpenUpdateCasePropertiesAction: acm.getScheduledHearingId()="
								+ acm.getScheduledHearingId());
				}

				try {
					if (ucd.internalDebug)
						log.debug("the ID of the ScheduledHearing to load is " + scheduledHearingId);
					this.hhv = this.getHearingScheduleBD().getHearingHeader(scheduledHearingId, XhibitSingleton
							.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
					setExported(this.getHearingScheduleBD().isExported(this.hhv.getId()));
					if (ucd.internalDebug) {
						log.debug("################################");
						log.debug("hhv : " + hhv.toString());
						log.debug("hhv.getAttendeeHistoryValues() : " + hhv.getAttendeeHistoryValues().toString());
						log.debug("hhv.getHearingProgress() : " + hhv.getHearingProgress().toString());
						log.debug("hhv.getHhCase() : " + hhv.getHhCase().toString());
						log.debug("hhv.getLegalRepValues() : " + hhv.getLegalRepValues().toString());
						log.debug("hhv.getStaffValues() : " + hhv.getStaffValues().toString());
						log.debug("this.isExported? : " + isExported().booleanValue());
						log.debug("################################");
					}
				} catch (Exception ee) {
					log.error(ee);
					throw (ee);
				}
			}
			// GOT THE DATA, NOW PAINT THE SCREENS
			if (ucd.internalDebug)
				log.debug("The HearingHeaderValue for the component is : " + hhv.toString());
			this.hhvh = new HearingHeaderValueHelper(hhv);
			jbInit();

			// Call life-cycle method to enable/disable screen components
			stepUpdateViewState();
		} catch (Exception eee) {
			String msg = "UpdateCase.stepActivate throws Exception";
			log.error(msg);
			log.error(eee);
			throw new CSRecoverableException("gui.updateCase.stepActivate", msg, eee);
		}
	}

	/**
	 * Life-cycle method to enable/disable screen components
	 */
	public void stepUpdateViewState() {
		if (ucd.internalDebug)
			log.debug("UpdateCase.stepUpdateViewState");
		XHIBITConstant.debug("UpdateCasePanel.stepUpdateViewState( ).isEditable: " + ucd.openingAction.isEditable());
		((ApplyOkCancelPanel) ucd.getButtonPanel()).applyButton.setEnabled(ucd.openingAction.isEditable());
		((ApplyOkCancelPanel) ucd.getButtonPanel()).okButton.setEnabled(ucd.openingAction.isEditable());
	}

	public void stepValidate() throws CSValidationException {
		// The individual tabs validate themselves. Here
		// we validate the data as a whole. Ensure that no
		// legal representation is shared between prosecution
		// and defence.

		if (ucd.internalDebug)
			log.debug("UpdateCase.stepValidate");

		Vector<Integer> prosecutionReps;

		if (!hasProsecutionTab()) {
			// These cases do not have prosecutionReps and xProsecutionCaseData
			// will be null.
			prosecutionReps = new Vector<Integer>();
		} else {
			prosecutionReps = xProsecutionCaseData.getLegalReps();
		}

		Vector<Integer> defendants;

		if (!hasProsecutionTab()) {
			// These cases do not have defendants and xDefenceCaseData
			// will be null.
			defendants = new Vector<Integer>();
		} else {
			defendants = xDefenceCaseData.getDefendantLegalReps();
		}

		Iterator<Integer> iter = defendants.iterator();

		while (iter.hasNext()) {
			Integer legalRepId = iter.next();

			Iterator<Integer> prosecutionRepIter = prosecutionReps.iterator();

			while (prosecutionRepIter.hasNext()) {
				Integer prosecutionRep = prosecutionRepIter.next();

				if (prosecutionRep.equals(legalRepId)) {
					String errorKey = "updateCase.save_Representation_Conflict";
					String logMessage = "The same legal rep has been chosen for defence and procecution";
					throw new CSValidationException(errorKey, logMessage);
				}
			}
		}
	}

	/*
	 * Return true if the case has a prosecution tab.
	 */
	private boolean hasProsecutionTab() {
		return !(isUndefindedCase() || isCombinedCase());
	}

	/**
	 * @deprecated one should use public boolean stepDeactivate(boolean
	 *             triggerSaving) instead
	 */
	public void stepDeactivate() throws UserCancelException, CSRecoverableException {
		this.stepDeactivate(true);
	}

	/**
	 * XPanel Life cycle method implementation, saving the state of the 'sub
	 * components'.
	 * 
	 * @param triggerSaving
	 * @return
	 */
	public boolean stepDeactivate(boolean triggerSaving) throws UserCancelException, CSRecoverableException {
		if (ucd.internalDebug)
			log.debug("UpdateCase.stepDeactivate");

		CSRecoverableException save_General_Exception = null;
		CSRecoverableException save_Prosecution_Exception = null;
		CSRecoverableException save_Defence_Exception = null;
		CSRecoverableException save_Staff_Exception = null;

		try {
			if (!CaseTypeHelper.isCombinedCourt_CaseType(this.hhv))
			// if ( (!CaseTypeHelper.isUndefined_CaseType(this.hhv))
			// && (!CaseTypeHelper.isCombinedCourt_CaseType(this.hhv)) )
			{
				try {
					this.xGeneralCaseData.stepValidate();
					this.xGeneralCaseData.stepDeactivate();
					ucd.openingAction.setHearingRecordModel(hearingRecordModel);
				} catch (UserCancelException uce) {
					throw uce;
				} catch (HearingScheduleException hse) {
					throw hse;
				} catch (Exception hse) {
					save_General_Exception = new CSRecoverableException("updateCase.save_General_Exception",
							"HearingScheduleException caught during xGeneralCaseData.stepDeactivate", hse);
					XHIBITConstant.handleError(save_General_Exception, this.getClass(),
							"reporting on save general exception");
					// If optimistic lock, indicate that it saved so that
					// window
					// closes and user can re-open and reenter changes
					if (XHIBITConstant.isOptimisticLockError(hse))
						return true;
				}

				if (!CaseTypeHelper.isUndefined_CaseType(this.hhv)) {
					try {
						this.xProsecutionCaseData.stepValidate();
						this.xProsecutionCaseData.stepDeactivate();
					} catch (Exception hse) {
						save_Prosecution_Exception = new CSRecoverableException("updateCase.save_Prosecution_Exception",
								"HearingScheduleException caught during xProsectutionCaseData.stepDeactivate", hse);
						XHIBITConstant.handleError(save_Prosecution_Exception, this.getClass(),
								"reporting on save prosecution exception");
						// If optimistic lock, indicate that it saved so that
						// window
						// closes and user can re-open and reenter changes
						if (XHIBITConstant.isOptimisticLockError(hse))
							return true;
					}
					try {
						this.xDefenceCaseData.stepDeactivate();
					} catch (Exception hse) {
						save_Defence_Exception = new CSRecoverableException("updateCase.save_Defence_Exception",
								"HearingScheduleException caught during xDefenceCaseData.stepDeactivate", hse);
						XHIBITConstant.handleError(save_Defence_Exception, this.getClass(),
								"reporting on save defence exception");
						// If optimistic lock, indicate that it saved so that
						// window
						// closes and user can re-open and reenter changes
						if (XHIBITConstant.isOptimisticLockError(hse))
							return true;
					}
					try {
						this.stepValidate();
					} catch (Exception hse) {
						save_Defence_Exception = new CSRecoverableException("updateCase.save_Representation_Confilict",
								"Exception caught during xDefenceCaseData.stepDeactivate", hse);
						XHIBITConstant.handleError(save_Defence_Exception, this.getClass(),
								"reporting on save legal representation exception");
						// If optimistic lock, indicate that it saved so that
						// window
						// closes and user can re-open and reenter changes
						if (XHIBITConstant.isOptimisticLockError(hse))
							return true;
					}
				}
				try {
					this.xCourtStaffCaseData.stepDeactivate();
				} catch (UserCancelException uce) {
					throw uce;
				} catch (HearingScheduleException hse) {
					save_Staff_Exception = new CSRecoverableException("updateCase.save_Staff_Exception",
							"HearingScheduleException caught during xCourtStaffCaseData.stepDeactivate", hse);
					XHIBITConstant.handleError(save_Staff_Exception, this.getClass(),
							"reporting on save court staff exception");
					// If optimistic lock, indicate that it saved so that
					// window
					// closes and user can re-open and reenter changes
					if (XHIBITConstant.isOptimisticLockError(hse))
						return true;
				} catch (CSRecoverableException cre) {
					save_Staff_Exception = new CSRecoverableException("updateCase.save_Staff_Exception",
							"HearingScheduleException caught during xCourtStaffCaseData.stepDeactivate", cre);
					XHIBITConstant.handleError(save_Staff_Exception, this.getClass(),
							"reporting on save court staff exception");
					// If optimistic lock, indicate that it saved so that
					// window
					// closes and user can re-open and reenter changes
					if (XHIBITConstant.isOptimisticLockError(cre))
						return true;
				}
			}
		} catch (CSRecoverableException csex) {
			throw csex;
		} catch (Exception e) {
			if (e instanceof UserCancelException) {
				throw (UserCancelException) e;
			}

			e.printStackTrace();
			log.error(e);
			XHIBITConstant.handleError(e, this.getClass(),
					"Trouble experienced whilst saving your case/hearing changes.");
			return false;
		}

		if ((save_General_Exception == null) && (save_Prosecution_Exception == null) && (save_Defence_Exception == null)
				&& (save_Staff_Exception == null)) {
			this.setModified(false); // as everything is saved now!
			return true;
		} else {
			if (ucd.internalDebug)
				log.debug("StepDeactivate returns FALSE - the component must stay open!");
			this.setModified(true); // making sure we're not closing the
			// dialog this hard cancel or success.
			// save.
			return false;
		}
	}

	public void stepDeinitialise(boolean update) {
		if (ucd.internalDebug)
			log.debug("UpdateCase.stepDeinitialise");
	}

	/**
	 * Iteration 2 Access to the Mid Tier. Loads (if necessary) and returns the
	 * HearingScheduleControllerBusinessDelegate
	 * 
	 * @return the HearingScheduleControllerBusinessDelegate
	 */
	protected HearingScheduleControllerBeanBusinessDelegate getHearingScheduleBD() {
		return XhibitDelegateHelper.getHearingDelegate();
	}

	/**
	 * This method reads XhibitProperties.XhibitClientProject looking for key
	 * UpdateCase.useDummyData. If this key is found then no interaction with
	 * the MID Tier is to happen - for dev & test purposes only.
	 */
	private void initUseDummyData() {
		useDummyData = false;
		String useDummyDataIndicator = XHIBITConstant.getProperty(XhibitProperties.XhibitClientProject,
				"UpdateCase.useDummyData");
		if ((useDummyDataIndicator != null)
				&& (useDummyDataIndicator.indexOf(XHIBITConstant.propertyNotFoundStringStart) != -1)) {
			if (ucd.internalDebug)
				log.debug("UpdateCase.useDummyData property key not found in " + XhibitProperties.XhibitClientProject
						+ " property file - Update Case component will interact with XHIBIT 2 MID Tier.");
		} else {
			useDummyData = true;
			log.debug("UpdateCase.useDummyData property key found in " + XhibitProperties.XhibitClientProject
					+ " property file - Update Case component NOT interacting with XHIBIT 2 MID Tier, rather using dummy data value!");
		}
	}

	public UpdateCaseDialog getUcd() {
		return ucd;
	}

	public Integer getScheduledHearingId() {
		return scheduledHearingId;
	}

	public HearingRecordModel getHearingRecordModel() {
		return hearingRecordModel;
	}
	
	public void setHearingRecordModel(HearingRecordModel hearingRecordModel) {
		this.hearingRecordModel = hearingRecordModel;
	}

	public JTabbedPane getUpdateCaseTabbedPane() {
		return updateCaseTabbedPane;
	}

	public void setExported(Boolean exported) {
		this.isExported = exported;
	}

	public Boolean isExported() {
		return (this.isExported == null ? new Boolean(false) : this.isExported);
	}
}