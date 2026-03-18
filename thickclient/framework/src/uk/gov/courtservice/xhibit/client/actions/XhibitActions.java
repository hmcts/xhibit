package uk.gov.courtservice.xhibit.client.actions;

import java.util.MissingResourceException;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * This class is used to register all actions that need to be
 * XhibitApplicationController specific.<br>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Revision: 1.36 $
 * @history James Powell 13/03/2009 - Added string for Unauthorised Cases Status
 *          functionality
 */
public class XhibitActions {
    // private static XhibitActions xa = null;
    private static XhibitActionBundle xb = null; // new

    // XhibitActionBundle();

    // Repeat the static strings below for each action to be registered here
    // Then create an entry in the XhibitActionBundle

    // hearing record
    public static final String HearingDetailsRecalculate = "HearingDetailsRecalculateAction";
    public static final String SearchMagCourts = "SearchMagCourtsAction";
    public static final String PrintCrestFormA = "PrintCrestFormAAction";
    public static final String OpenLinkedHearingsSummary = "OpenLinkedHearingsSummary";
    public static final String LinkHearing = "LinkHearing";
    public static final String UnlinkHearing = "UnlinkHearing";
    public static final String LinkHearingSearch = "LinkHearingSearch";
    public static final String EditDefendant = "EditDefendant";
    public static final String EditDefendantSpecial = "EditDefendantSpecial";
    public static final String EditEstimateForTrial = "EditEstimateForTrial";

    // searches
    public static final String OpenSearchJudge = "OpenSearchJudge";
    public static final String OpenSearchJudgeUpdateParent = "OpenSearchJudgeUpdateParent";
	public static final String OpenSearchCase = "OpenSearchCase";
    public static final String OpenSearchCourt = "OpenSearchCourt";
    public static final String OpenSearchJustice = "OpenSearchJustice";
    public static final String OpenSearchSolicitorFirm = "OpenSearchSolicitorFirm";
    public static final String OpenSearchLegalRep = "OpenSearchLegalRep";
    public static final String OpenSearchListingJudge = "OpenSearchListingJudge";
    public static final String OpenSearchOffence = "OpenSearchOffence";
    public static final String OpenSearchBailActOffence = "OpenSearchBailActOffence";
    public static final String OpenSearchObsoleteOffence = "OpenSearchObsoleteOffence";
    public static final String OpenSearchColMagCourts = "OpenSearchColMagCourts";

    // Todays Schedule Component
    public static final String ViewCase = "ViewCase";

    public static final String UpdateCase = "UpdateCase"; // ?? = CaseProp

    // = maintain
    // hearing
    // header?

    // List
    public static final String MoveCase = "MoveCase";

    // public static final String AddBWH = "AddBWH";
    public static final String AddHearing = "AddHearing";
    public static final String DailyList = "DailyList";
    public static final String DailyListPrison = "DailyListPrsion";
    public static final String RunningList = "RunningList";
    public static final String FirmList = "FirmList";
    public static final String WarnedList = "WarnedList";
    public static final String WarnedListLetter = "WarnedListLetter";
    public static final String ViewDistributionStatus = "ViewDistributionStatus";
    public static final String CreateList = "CreateList";
    
    public static final String OpenExistingList = "OpenExistingList";
    
    public static final String ListResults = "ListResults";
    public static final String NonAvailableDays = "NonAvailableDays";
    public static final String CaseListingEntry = "CaseListingEntry";
    public static final String ListOfficersDiary = "ListOfficersDiary";
    
    public static final String PrintWLL = "PrintWLL";
    public static final String DistributeListLetters = "DistributeListLetters";
    public static final String MaintainListLetterRecipients = "MaintainListLetterRecipients";
    public static final String DeleteDocument = "DeleteDocument";
         
	// Daily Listings
    public static final String CaseListingFixture = "CaseListingFixture";
  
	// List Diary Menu
    public static final String CaseListingDetail = "CaseListingDetail";
    
   // Case Properties Component
    public static final String CaseProps = "CASEPROPS";
    public static final String OpenAddDefendantAdvocate = "OpenAddDefenceAdvocateAction";
    public static final String RemoveDefendantAdvocate = "RemoveDefendantAdvocateAction";
    public static final String OpenAddAppealAdvocate = "OpenAddAppealAdvocateAction";
    public static final String RemoveAppealAdvocate = "RemoveAppealAdvocateAction";
    public static final String RemoveRespondentAdvocate = "RemoveRespondentAdvocateAction";
    public static final String OpenAddCourtClerk = "OpenAddCourtClerkAction";
    public static final String RemoveCourtClerk = "RemoveCourtClerkAction";
    public static final String OpenAddUsher = "OpenAddUsherAction";
    public static final String RemoveUsher = "RemoveUsherAction";
    public static final String OpenChangeRespondentAdvocate = "OpenChangeRespondentAdvocateAction";

    // END of Case Properties Component

    // Linked Cases
    public static final String LinkCases = "LinkCasesAction";
    public static final String UnlinkCase = "UnlinkCaseAction";
	public static final String NewLinkCases = "NewLinkCasesAction";
	public static final String NewUnlinkCases = "NewUnlinkCasesAction";
    public static final String LinkUnlinkCases = "LinkUnlinkCasesAction";

    // END of Linked Cases

    // Update Defendant Component
    public static final String OpenAmendDefendant = "OpenAmendDefendantAction";
    public static final String UpdateDefendant = "UpdateDefendantAction";

    // END of Update Defendant Component

    // File Menu
    public static final String Open = "OPEN";
    public static final String OpenOtherLog = "OpenOtherLog";
    public static final String Close = "CLOSE";
    public static final String Save = "SAVE";
    public static final String Print = "PRINT";
    public static final String PrintToolbar = "PRINTTOOLBAR";
    public static final String PrintPreview = "PRINTPREVIEW";
    public static final String OpenOtherDaysLog = "OpenOtherDaysLog";
    public static final String CounselSignInWizard = "CounselSignInWizard";
    public static final String FindCounselDefendant = "FindCounselDefendant";
    public static final String PreviewDailyList = "PreviewDailyList";
    public static final String PreviewTomorrowsList = "PreviewTomorrowsList";

    // Edit Menu
    public static final String EditSelectAll = "SelectAll";
    public static final String EditClEvent = "EDITCLEVENT";
    public static final String DeleteClEvent = "DELETECLEVENT";

    // Note, court log action names will be registered as Simple, MediumT,
    // MediumD, MediumO
    // and each complex will be individually listed.
    // Court Log Actions
    // public static final String clFreeTextEvent = "clFreeTextEvent";
    // The freetext, simple and medium events can not be part of this bundle
    // as they are
    // reused multiple times. Therefore, they need to be created as
    // individual actions,
    // Then the CourtLogXMLReader will manage the individual instances per
    // XhibitApplicationController.

    // Now each complex will be individually listed.
    public static final String BailCustody = "BailCustody";
    public static final String clShortAdjournment = "clShortAdjournment";
    public static final String clLongAdjournment = "clLongAdjournment";
    public static final String clTimeEstimate = "clTimeEstimate";
    public static final String JurySwornIn = "JurySwornIn";
    public static final String clWitnessSworn = "clWitnessSworn";
    public static final String clWitnessRead = "clWitnessRead";
    public static final String JuryDischarged = "JuryDischarged";
    public static final String TakenIntoConsideration = "TakenIntoConsideration";
    public static final String clWitnessSwornAppeal = "clWitnessSwornAppeal";
    public static final String clWitnessReadAppeal = "clWitnessReadAppeal";
    public static final String CaseDirections = "CaseDirections";
    public static final String SpecialMeasuresApplication = "SpecialMeasuresApplication";
    public static final String SevenFourteenDayOrder = "SevenFourteenDayOrder";
    public static final String EndHearing = "EndHearing";
    public static final String PreliminaryHearings = "PreliminaryHearings";

    // Charge Actions
    public static final String AddIndictment = "addIndictment";
    public static final String AddCount = "addCount";
    public static final String AddCountToJoinder = "addCountToJoinder";
    public static final String RemoveIndictment = "RemoveIndictment";
    public static final String RenumberCounts = "RenumberCounts";
    public static final String StayIndictment = "StayIndictment";
    public static final String SignIndictment = "SignIndictment";
    public static final String SignIndictmentRefused = "SignIndictmentRefused";
    public static final String JoinIndictment = "JoinIndictment";
    public static final String AddS41Offence = "AddS41Offence";
    public static final String AddC4SOffence = "AddC4SOffence";
    public static final String AddAppealOffence = "AddAppealOffence";
    
	/* ####### BAIL ACT OFFENCES ###### */
    public static final String AddBailActOffence = "AddBailActOffence";
    public static final String AddWizardBailActOffence = "AddWizardBailActOffence";
    public static final String ChangeBailActOffence = "ChangeBailActOffence";
    public static final String RemoveBailActOffence = "RemoveBailActOffence";
    public static final String AddUncodedOffence = "AddUncodedOffence";
    public static final String UpdateUncodedOffence = "UpdateUncodedOffence";
    public static final String C4SBringBack = "C4SBringBack";
    public static final String C4SPutAndAdmitted = "C4SPutAndAdmitted";
    public static final String C4SNotAdmitted = "C4SNotAdmitted";
    public static final String SOProsecutionNoEvidence = "SOProsecutionNoEvidence";
    public static final String DefendantSummaryOffences = "DefendantSummaryOffences";
    public static final String ApplicationToSever = "ApplicationToSever";
    public static final String VoluntaryBillPreferred = "VoluntaryBillPreferred";
    public static final String LateBillOfIndictment = "LateBillOfIndictment";
    public static final String BillOfIndictment = "BillOfIndictment";
    public static final String AddBreach = "AddBreach";
    public static final String AddBreachOffence = "AddBreachOffence";
    public static final String EditBreachProps = "EditBreachProps";
    public static final String RemoveBreach = "RemoveBreach";
    public static final String AddWizardBreachOffence = "AddWizardBreachOffence";
    public static final String RemoveWizardBreachOffence = "RemoveWizardBreachOffence";
    public static final String AddDefendantsToCount = "AddDefendantsToCount";
    public static final String RemoveDefendantsOnCount = "RemoveDefendantsOnCount";
    public static final String RenumberCount = "RenumberCount";
    public static final String ChangeCount = "ChangeCount";
    public static final String RemoveCount = "RemoveCount";
    public static final String StayCount = "StayCount";
    public static final String CopyCharge = "CopyCharge";

    // public static final String LieOnFileCount = "LieOnFileCount";
    public static final String CountParticularsAmended = "CountParticularsAmended";
    public static final String ChangeDefendant = "ChangeDefendant";
    public static final String AddCountsToDefendant = "AddCountsToDefendant";
    public static final String StayDefendantOnCount = "StayDefendantOnCount";

    // public static final String LieOnFileDefendantOnCount =
    // "LieOnFileDefendantOnCount";
    public static final String StayDefendantOnIndictment = "StayDefendantOnIndictment";

    // public static final String LieOnFileDefendantOnIndictment =
    // "LieOnFileDefendantOnIndictment";
    public static final String AddDefendantsToOffence = "AddDefendantsToOffence";
    public static final String ChangeOffence = "ChangeOffence";
	public static final String RemoveOffence = "RemoveOffence";

    // public static final String QuashCount = "QuashCount";
    // public static final String QuashIndictment = "QuashIndictment";
    // public static final String QuashDefendantOnCount =
    // "QuashDefendantOnCount";
    // public static final String QuashDefendantOnIndictment =
    // "QuashDefendantOnIndictment";
    public static final String ExportCharges = "ExportCharges";
    public static final String OriginalCharges = "OriginalCharges";

    // Charges Toolbar (not buttons not supported in above list)
    public static final String tbAdd = "tbAdd";
    public static final String tbAddCharge = "tbAddCharge";
    public static final String tbAddOffence = "tbAddOffence";
    public static final String tbAddDefendant = "tbAddDefendant";
    public static final String tbRemoveDefendant = "tbRemoveDefendant";
    public static final String tbChange = "tbChange";
    public static final String tbStay = "tbStay";

    // public static final String tbLieOnFile = "tbLieOnFile";
    // public static final String tbQuash = "tbQuash";
    public static final String CrestIndictmentLog = "CrestIndictmentLog";
    
	// Additional Offence and Defendant on Offence related actions
    public static final String AdditionalOffenceInfo = "AdditionalOffenceInfoAction";
    public static final String AdditionalDefendantOnOffenceInfo = "AdditionalDefendantOnOffenceInfoAction";
    public static final String AdditionalCountInfo = "AdditionalCountInfoAction";
    public static final String AdditionalDefendantOnCountInfo = "AdditionalDefendantOnCountInfoAction";
    public static final String AdditionalBreachOffenceDefendantInfo = "AdditionalBreachOffenceDefendantInfoAction";
    
    // View Menu
    public static final String ViewTodaysSchedule = "ViewTodaysSchedule";
    public static final String ViewCourtLog = "ViewCourtLog";
    public static final String ViewCaseProgress = "ViewCaseProgress";
    public static final String ViewCharges = "ViewCharges";
    public static final String ViewInformationPages = "ViewInformationPages";
    public static final String ViewInformationPagesV2 = "ViewInformationPagesV2";

    // Case Create Menu
    public static final String CreateCase = "CreateCase";
    public static final String CreateTrialCase = "CreateTrialCase";
    public static final String CreateSentenceCase = "CreateSentenceCase";
    public static final String CreateAppealCase = "CreateAppealCase";
    public static final String CreateMiscCase = "CreateMiscCase";
    public static final String MaintainCase = "MaintainCase";
    public static final String DeleteCase = "DeleteCase";
    public static final String TransferCase = "TransferCase";
    public static final String ReplaceDeleteDeft = "ReplaceDeleteDeft";
    public static final String AddIndictmentCase = "AddIndictmentCase";

    // Results Disposals menu
    public static final String PleasAndDirections = "PleasAndDirections";
    public static final String Plea = "Plea";
    public static final String MultiplePlea = "MultiplePlea";
    public static final String Verdict = "Verdict";
    public static final String Sentence = "Sentence";
    public static final String AddDisposal = "AddDisposal";
    public static final String AddMagistrateDisposal = "AddMagistrateDisposal";
    public static final String AddVariationDisposal = "AddVariationDisposal";
    public static final String EditDisposal = "EditDisposal";
    public static final String DeleteDisposal = "DeleteDisposal";
    public static final String UndeleteDisposal = "UndeleteDisposal";
    public static final String AppealResult = "AppealResult";
    public static final String CopyDisposal = "CopyDisposal";
    public static final String CopyUnrelatedDisposal = "CopyUnrelatedDisposal";

    // public static final String VerifyResults = "VerifyResults";
    public static final String AuthoriseResults = "AuthoriseResults";
    public static final String AuthoriseSync = "AuthoriseSync";
    public static final String AuthorisePreview = "AuthorisePreview";

    // Admin Menu
    public static final String PublicDisplayConfigV2 = "PublicDisplayConfigV2";
    public static final String QueryCompletedCase = "QueryCompletedCaseAction";
    
    // Reference Data
    public static final String ChamberAndAdvocateDetails = "ChamberAndAdvocateDetails";
	public static final String ChamberAndAdvocateDetailsReadOnly = "ChamberAndAdvocateDetailsReadOnly";
    public static final String CourtCalendar = "CourtCalendar";
    public static final String HomeCourtCentreAndCourtroomDetails = "HomeCourtCentreAndCourtroomDetails";
    public static final String JudgeDetails = "JudgeDetails";
    public static final String ProsecutorRespondentDetails = "ProsecutorRespondentDetails";
    public static final String SolicitorFirmDetails = "SolicitorFirmDetails";

    // Case Progress Actions
    public static final String ViewChargeDetailsAction = "ViewChargeDetailsAction";

    // Public Display
    public static final String ActivatePublicDisplay = "ActivatePublicDisplay";
    public static final String PublicNotice = "PublicNotice";

    // Orders
    public static final String OrderCreate = "OrderCreate";
    public static final String OrderView = "OrderView";
    public static final String OrderCopy = "OrderCopy";
	public static final String AppealResultOrder = "AppealResultOrderAction";
    public static final String OrderAcknowledgement = "OrderAcknowledgement";
    public static final String MonetaryOrderCreate = "MonetaryOrderCreate";
    public static final String MonetaryOrderView = "MonetaryOrderView";
    public static final String MonetaryOrderCopy = "MonetaryOrderCopy";
    public static final String MonetaryOrderAcknowledgement = "MonetaryOrderAcknowledgementAction";
    
    // D20 (DVLA)
    public static final String D20Create = "D20Create";
    public static final String D20View = "D20View";
    
    // Role Mapping
    public static final String RoleMapping = "RoleMapping";

    // Crest Import
    public static final String CrestImport = "CrestImport";

    // Neil Entwistle - Adding MESSAGING to main menu BEGIN
    // Messaging
    public static final String Messaging = "IMSender";

    // Neil Entwistle - Adding MESSAGING to main menu END

    // William Fardell (Xdevelopment) - Skeleton Schedule Start
    public static final String EditTrialTimeEstimate = "EditTrialTimeEstimateAction";
    public static final String EditSkeletonSchedule = "EditSkeletonScheduleAction";
    public static final String AddWitness = "AddWitnessAction";
    public static final String EditWitness = "EditWitnessAction";
    public static final String ViewPreviousWeek = "ViewPreviousWeekAction";
    public static final String ViewNextWeek = "ViewNextWeekAction";
    public static final String IssueSchedule = "IssueScheduleAction";
    public static final String DeleteSchedule = "DeleteScheduleAction";
    public static final String PrintScheduleDay = "PrintScheduleDayAction";
    public static final String PrintScheduleWeek = "PrintScheduleWeekAction";
    public static final String EditNotes = "EditNotesAction";

    // William Fardell (Xdevelopment) - Skeleton Schedule End

    // William Fardell (Xdevelopment) - Crest Forms B - F Start
    public static final String ViewCrestFormsBF = "ViewCrestFormsBFAction";

    // William Fardell (Xdevelopment) - Crest Forms B - F End

    // Import Export Statuses/notification
    public static final String ImportExportNotification = "ImportExportNotificationAction";
    public static final String UnauthorisedCaseStatus = "UnauthorisedCaseStatus";

	 
	public static final String CaseSummary = "CaseSummaryMenuItemAction";
 
	// Reports Menu Actions
	public static final String DARTSReport = "DARTSMenuItemAction";
	public static final String DOCARReport = "RunDOCARReportAction";
	public static final String ADJSSReport = "DisplayADJSSReportAction";
	public static final String NFIXReport = "DisplayNFIXReportAction";
	public static final String OUTCReport = "OUTCMenuItemAction";
	public static final String CFIXReport = "CFIXMenuItemAction";
 	public static final String NHAReport = "DisplayNHAReportAction";
	public static final String OBWReport = "OBWMenuItemAction";
	public static final String LFIXReport = "LFIXMenuItemAction";
    public static final String LODReport = "LODMenuItemAction";
    public static final String UNLCReport = "UNLCMenuItemAction";
	public static final String PRLISReport = "PRLISMenuItemAction";
	public static final String DEFSSReport = "DisplayDEFSSReportAction";
	public static final String CTLRPReport = "CTLRPMenuItemAction";
	public static final String DRSRReport = "DRSRMenuItemAction";
	public static final String RAGEReport = "RAGEMenuItemAction";
	public static final String RELCJReport = "RELCJMenuItemAction";
    public static final String RJSReport = "RJSMenuItemAction";
	public static final String NTRSFReport = "NTRSFMenuItemAction";
	public static final String INFTRPCReport ="INFTRPCMenuItemAction";

	public static final String RUMOReport = "DisplayRUMOReportAction";
	public static final String RRCAReport = "RRCAMenuItemAction";
	public static final String RRECReport = "RRECMenuItemAction";
	public static final String RSITReport = "RSITMenuItemAction";
	
	

	// court of appeal new menu
	public static final String CourtOfAppeal = "CourtOfAppealAction";
	
	// Record Courtroom Statistics new menu
		public static final String RecoredCourtroomStatistics = "RecordCourtroomStatisticsAction";

	 private XhibitActions() {
 	        // emtpy
	    }
	
    private static XhibitActionBundle getXhibitActionBundle() {
        if (xb == null) {
            xb = new XhibitActionBundle();
        }
        return xb;
    }

    // This will not throw any user messages. This may need to be addressed.
    public static XAction getAction(XhibitApplicationController xac, String actionName, Object caller)
            throws ActionNotFoundException {
        XAction xaction = getAction(xac, actionName);
        xaction.setCaller(caller);
        return xaction;
    }

    // This will not throw any user messages. This may need to be addressed.
    public static XAction getAction(XhibitApplicationController xac, String actionName) throws ActionNotFoundException {
        if (xac == null) {
            throw new ActionNotFoundException("XAC Null: The requested action '" + actionName + "' could not be found");
        }

        // Because there is a dependency between this jar and the
        // orders_client.jar,
        // we can not add Orders actions in the normal way, because the code
        // will not compile.
        // so, in the mean time we check if the requested action is an orders
        // action, and if so
        // create it using Class.forName(...)
        try {
            XAction xa = (XAction) xac.getActionMap().get(actionName);
            if (xa == null) {
                Class newAction = (Class) (getXhibitActionBundle().getObject(actionName));

                if (newAction == null) {
					throw new ActionNotFoundException(
							"NewAction Null: The requested action '" + actionName + "' could not be found");
                }

                xa = (XAction) newAction.newInstance();
                xa.setController(xac);
                xac.getActionMap().put(actionName, xa);
                
            }

            return xa;
        } catch (MissingResourceException mre) {
			throw new ActionNotFoundException(
					"MissingResourceException: The action is not registered in the bundle: '" + actionName + "'", mre);
        } catch (ClassCastException cce) {
			throw new ActionNotFoundException(
					"ClassCastException: The action is not of the appropriate type: '" + actionName + "'", cce);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            throw new ActionNotFoundException("IllegalAccessException attempting to create '" + actionName + "'", iae);
        } catch (InstantiationException ie) {
			throw new ActionNotFoundException(
					"InstantiationException: The action could not be instantiated: '" + actionName + "'", ie);
        }
    }

    public static XAction getCourtLogAction(XhibitApplicationController xac, String eventType)
            throws ActionNotFoundException {
        if (xac == null) {
            throw new ActionNotFoundException("XAC Null: The requested action '" + eventType + "' could not be found");
        }

        try {
            XAction xa = (XAction) xac.getCourtLogActionMap().get(eventType);
            if (xa == null) {
                // attempt to get the action from the parent action map
                xa = (XAction) xac.getCourtLogActionMap().getParent().get(eventType);
                if (xa == null) {
					throw new ActionNotFoundException(
							"No Action: The requested action for '" + eventType + "' could not be found");
                }
            }

            return xa;
        } catch (ClassCastException cce) {
			throw new ActionNotFoundException(
					"ClassCastException: The action is not of the appropriate type: '" + eventType + "'", cce);
        }
    }
}