package uk.gov.courtservice.xhibit.client.util.security;

import java.util.ListResourceBundle;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;

/**
 * <p>
 * Title: EditMapping
 * </p>
 * <p>
 * Description: EditMapping
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 2.1
 * @history James Powell 13/03/2009 - Added action for Unauthorised Case Status
 * @history Kelvin Davies 21/04/2009 - Added action for Unauthorised Update
 *          Vulnerable Victim Indicator
 * 
 */

public class EditMapping extends ListResourceBundle {
	private Object[][] contents = { { "AddBreachAction", FunctionList.ECharge },
            { "AddWizardBreachOffenceAction", FunctionList.ECharge },
            { "RemoveWizardBreachOffenceAction", FunctionList.ECharge },
			{ "AddBreachOffenceAction", FunctionList.ECharge }, { "AddC4SOffenceAction", FunctionList.ECharge },

			{ "C4SBringBackAction", FunctionList.ECharge }, { "C4SPutAndAdmittedAction", FunctionList.ECharge },
			{ "C4SNotAdmittedAction", FunctionList.ECharge }, { "SOProsecutionNoEvidenceAction", FunctionList.ECharge },
            { "DefendantSummaryOffencesAction", FunctionList.ECharge },
            { "ApplicationToSeverAction", FunctionList.ECharge },
            { "VoluntaryBillPreferredAction", FunctionList.ECharge },
			{ "LateBillOfIndictmentAction", FunctionList.ECharge }, { "BillOfIndictmentAction", FunctionList.ECharge },

			{ "AddCountAction", FunctionList.ECharge }, { "AddCountToJoinderAction", FunctionList.ECharge },

            { "AddCountsToDefendantAction", FunctionList.ECharge },
            { "AddDefendantsToCountAction", FunctionList.ECharge },
            { "RemoveDefendantsFromCountAction", FunctionList.ECharge },
			{ "RenumberCountsAction", FunctionList.ECharge }, { "AddDefendantsToOffenceAction", FunctionList.ECharge },
			{ "AddIndictmentAction", FunctionList.ECharge }, { "AddS41OffenceAction", FunctionList.ECharge },
			{ "AddAppealOffenceAction", FunctionList.ECharge }, { "ChangeCountAction", FunctionList.ECharge },
			{ "ChangeCourtAction", FunctionList.ECharge }, { "ChangeDefendantAction", FunctionList.ECharge },
			{ "ChangeOffenceAction", FunctionList.ECharge }, { "CountParticularsAmendedAction", FunctionList.ECharge },
			{ "EditBreachAction", FunctionList.ECharge }, { "RemoveBreachAction", FunctionList.ECharge },
			// Bail Act Offences
			{ "AddBailActOffenceAction", FunctionList.ECharge },
			{ "AddWizardBailActOffenceAction", FunctionList.ECharge },
			{ "ChangeBailActOffenceAction", FunctionList.ECharge },
			{ "RemoveBailActOffenceAction", FunctionList.ECharge },
            
			{ "RemoveCountAction", FunctionList.ECharge }, { "RemoveIndictmentAction", FunctionList.ECharge },
			{ "RemoveOffenceAction", FunctionList.ECharge }, { "RenumberCountsAction", FunctionList.ECharge },
			{ "SignIndictmentAction", FunctionList.ECharge }, { "SignIndictmentRefusedAction", FunctionList.ECharge },
			{ "StayCountAction", FunctionList.ECharge }, { "StayDefendantOnCountAction", FunctionList.ECharge },
            { "StayDefendantOnIndictmentAction", FunctionList.ECharge },
			{ "StayIndictmentAction", FunctionList.ECharge }, { "ExportChargesAction", FunctionList.ECharge },
			{ "OriginalChargesAction", FunctionList.ECharge }, { "tbAddAction", FunctionList.ECharge },
			{ "tbAddChargeAction", FunctionList.ECharge }, { "tbAddDefendantAction", FunctionList.ECharge },
			{ "tbAddOffenceAction", FunctionList.ECharge }, { "tbChangeAction", FunctionList.ECharge },
			{ "tbLieOnFileAction", FunctionList.ECharge }, { "tbStayAction", FunctionList.ECharge },
            { "AdditionalOffenceInfoAction", FunctionList.ECharge },
            { "AdditionalCountInfoAction", FunctionList.ECharge },
            { "AdditionalDefendantOnOffenceInfoAction", FunctionList.ECharge },
            { "AdditionalDefendantOnCountInfoAction", FunctionList.ECharge },
            { "AdditionalBreachOffenceDefendantInfoAction", FunctionList.ECharge },
            
			{ "HelpAction", FunctionList.NA }, { "AboutAction", FunctionList.NA }, { "CloseAction", FunctionList.NA },
			{ "CopyAction", FunctionList.NA }, { "CopyTextComponentToClipboardAction", FunctionList.NA },
			{ "CutAction", FunctionList.NA }, { "DisposeWindowAction", FunctionList.NA },
			{ "ExitAction", FunctionList.NA }, { "NewAction", FunctionList.NA }, { "PasteAction", FunctionList.NA },
			{ "ViewToolbarAction", FunctionList.NA }, { "PrintAction", FunctionList.NA },
			{ "PrintToolbarAction", FunctionList.NA }, { "PrintPreviewAction", FunctionList.NA },
            { "SaveAction", FunctionList.NA },

            { "SpecialMeasuresApplicationAction", FunctionList.ECourtLog },
			{ "BailCustodyAction", FunctionList.ECourtLog }, { "EndHearingAction", FunctionList.ECourtLog },
            { "PreliminaryHearingsAction", FunctionList.ECourtLog },
            { "clLongAdjournmentAction", FunctionList.ECourtLog },
			{ "clShortAdjournmentAction", FunctionList.ECourtLog }, { "clTimeEstimateAction", FunctionList.ECourtLog },
			{ "clWitnessSwornAction", FunctionList.ECourtLog }, { "clWitnessReadAction", FunctionList.ECourtLog },
            { "clWitnessSwornAppealAction", FunctionList.ECourtLog },
			{ "clWitnessReadAppealAction", FunctionList.ECourtLog }, { "CourtLogAction", FunctionList.NA },
            { "DeleteCourtLogEventAction", FunctionList.ECourtLog },
			{ "EditCourtLogEventAction", FunctionList.ECourtLog }, { "EditDirectionsActions", FunctionList.ECourtLog },
			{ "FreeTextEventAction", FunctionList.ECourtLog }, { "JuryDischargedAction", FunctionList.ECourtLog },
			{ "JurySwornInAction", FunctionList.ECourtLog }, { "LegalArgumentOptionsAction", FunctionList.ECourtLog },
            { "LegalArgumentOptionsAppealAction", FunctionList.ECourtLog },
            { "LegalArgumentOptionsTrialAction", FunctionList.ECourtLog },
			{ "MediumMDEventAction", FunctionList.ECourtLog }, { "MediumMLEventAction", FunctionList.ECourtLog },
			{ "MediumMTEventAction", FunctionList.ECourtLog }, { "BWEventAction", FunctionList.ECourtLog },
			{ "EndBWEventAction", FunctionList.ECourtLog }, { "OpenOtherDaysLogAction", FunctionList.VCourtLog },
			{ "SimpleEventAction", FunctionList.ECourtLog }, { "TakenIntoConsiderationAction", FunctionList.ECourtLog },
			{ "LinkCasesAction", FunctionList.ELinkCases }, { "UnlinkCaseAction", FunctionList.EUnlinkCases },
			{ "TodaysScheduleAction", FunctionList.VSchedule }, { "ViewChargesAction", FunctionList.VCharge },
			{ "ViewCourtLogAction", FunctionList.VCourtLog }, { "AddHearingAction", FunctionList.EAddHearing },
            { "NewLinkCasesAction", FunctionList.ENewLinkCases },
			{ "NewUnlinkCasesAction", FunctionList.ENewUnlinkCases }, { "MoveCaseAction", FunctionList.EMoveCase },
			{ "OpenCaseAction", FunctionList.VCourtLog }, { "UpdateCaseAction", FunctionList.VCourtLog },
            { "ViewCaseAction", FunctionList.VCourtLog },

            { "OpenAddAppealAdvocateAction", FunctionList.ECounselSignin },
            { "OpenAddCourtClerkAction", FunctionList.ECourtLog },
            { "OpenAddDefenceAdvocateAction", FunctionList.ECounselSignin },
			{ "OpenAddUsherAction", FunctionList.ECourtLog }, { "OpenAmendDefendantAction", FunctionList.EDefendant },
            { "OpenChangeProsecutionAdvocateAction", FunctionList.ECounselSignin },
            { "OpenChangeRespondentAdvocateAction", FunctionList.ECounselSignin },
            { "OpenChangeShorthandWriterAction", FunctionList.ECourtLog },
            { "OpenUpdateCasePropertiesAction", FunctionList.VCaseProperty },
            { "RemoveAppealAdvocateAction", FunctionList.ECounselSignin },
            { "RemoveCourtClerkAction", FunctionList.ECourtLog },
            { "RemoveDefendantAdvocateAction", FunctionList.ECounselSignin },
			{ "RemoveUsherAction", FunctionList.ECourtLog }, { "UpdateDefendantAction", FunctionList.EDefendant },

            // New actions for itr 2
			{ "AbstractSearchAction", FunctionList.NA }, { "ActivatePublicDisplayAction", FunctionList.EPublicDisplay },
			{ "AddDisposalAction", FunctionList.EDisposal }, { "AddMagistrateDisposalAction", FunctionList.EDisposal },
			{ "AddVariationDisposalAction", FunctionList.EDisposal }, { "AppealResultAction", FunctionList.EVerdict },
            { "AuthoriseResultsAction", FunctionList.EAuthoriseResult },
            { "AuthoriseSyncAction", FunctionList.EAuthoriseResult },
            { "AuthorisePreviewAction", FunctionList.EAuthoriseResult },
            { "AuthoriseUpdateVictimIndicatorAction", FunctionList.EAuthoriseResult },
			{ "CaseDirectionsAction", FunctionList.ECourtLog }, { "CaseProgressAction", FunctionList.VCaseProgress },
			{ "CopyDisposalAction", FunctionList.EDisposal }, { "CopyUnrelatedDisposalAction", FunctionList.EDisposal },
            { "CounselSignInWizardAction", FunctionList.ECounselSignin },
			{ "CrestIndictmentLogAction", FunctionList.ECharge }, { "DailyListAction", FunctionList.VRecipient },
			{ "DailyListPrisonAction", FunctionList.VRecipient }, { "DeleteDisposalAction", FunctionList.EDisposal },
			{ "DeleteDocumentAction", FunctionList.EDisposal }, { "EditDefendantAction", FunctionList.EDefendant },
			{ "EditDefendantSpecialAction", FunctionList.EDefendant }, { "EditDisposalAction", FunctionList.EDisposal },
            { "EditEstimateForTrialAction", FunctionList.EHearingRecord },
            { "ExportHearingRecordAction", FunctionList.EExportHearingRecord },
            { "FindCounselDefendantAction", FunctionList.VCounselSignin },
            { "FirmListAction", FunctionList.VRecipient },
			{ "HearingDetailsRecalculateAction", FunctionList.EHearingRecord }, { "JoinAction", FunctionList.ECharge },
            { "LinkHearingAction", FunctionList.ELinkHearings },
			{ "LinkHearingSearchAction", FunctionList.ELinkHearings }, { "MultiplePleaAction", FunctionList.EPlea },
            { "OpenCrestFormAAction", FunctionList.EHearingRecord },
            { "OpenLinkedHearingsSummary", FunctionList.EHearingRecord },
            { "OpenOtherCaseAction", FunctionList.VCourtLog },
			{ "OpenSearchCollectingMagCourtsAction", FunctionList.NA }, { "OpenSearchCourtAction", FunctionList.NA },
			{ "OpenSearchJudgeAction", FunctionList.NA },
			{ "OpenSearchLegalRepAction", FunctionList.NA }, { "OpenSearchOffenceAction", FunctionList.NA },
			{ "OpenSearchListingJudgeAction", FunctionList.NA }, { "OpenSearchBailActOffenceAction", FunctionList.NA },
			{ "OpenSearchSolicitorFirmAction", FunctionList.NA }, { "PleaAction", FunctionList.EPlea },
			{ "PleasAndDirectionsAction", FunctionList.EPlea }, { "PreviewDailyList", FunctionList.VSchedule },
            { "PreviewTomorrowsList", FunctionList.VTomorrowSchedule },
			{ "PrintCrestFormAAction", FunctionList.EHearingRecord }, { "PrintWLLAction", FunctionList.VWLLRecipient },
            { "DistributeListLettersAction", FunctionList.EListCase },
            { "MaintainListLetterRecipientsAction", FunctionList.EWLLRecipient },
            { "PublicNoticesAction", FunctionList.EPublicDisplay },
            { "RemoveRespondentAdvocateAction", FunctionList.ECounselSignin },
			{ "RunningListAction", FunctionList.VRecipient }, { "SearchMagCourtsAction", FunctionList.NA },
			{ "SearchProcessHandler", FunctionList.NA }, { "SelectAllAction", FunctionList.NA },
			{ "SentenceAction", FunctionList.EDisposal }, { "SevenFourteenDayOrderAction", FunctionList.ECourtLog },
			{ "tbQuashAction", FunctionList.EDisposal }, { "UndeleteDisposalAction", FunctionList.EDisposal },
			{ "UnlinkHearingAction", FunctionList.ELinkHearings }, { "VerdictAction", FunctionList.EVerdict },
			{ "ViewChargeDetailsAction", FunctionList.ECaseProgressDetail },
            { "ViewDistributionStatusAction", FunctionList.VRecipient },
            { "ViewInformationPagesAction", FunctionList.VPublicDisplay },
			{ "InformationPagesAction", FunctionList.VPublicDisplay }, { "WarnedListAction", FunctionList.VRecipient },
            { "WarnedListLetterAction", FunctionList.VWLLRecipient },
            
			// Listings
            
			{ "CreateNewFirmListAction", FunctionList.ECreateList },
			{ "CaseListingEntryAction", FunctionList.EMaintainList },
			{ "ListOfficersDiaryAction", FunctionList.EMaintainList },
			{ "CreateListAction", FunctionList.EMaintainList },
			{ "OpenExistingListAction", FunctionList.EMaintainList },
			{ "ListResultsAction", FunctionList.EMaintainList },
			{ "NonAvailableDaysAction", FunctionList.EMaintainList },
			{ XhibitActions.CaseSummary, FunctionList.EMaintainList },
            
            // admin
            { "CrestImportAction", FunctionList.ECrestImport },
            { "PublicDisplayConfigurationAction", FunctionList.EPublicDisplayAdmin },
            { "PublicDisplayConfigAction", FunctionList.EPublicDisplayAdmin },
            { "RoleMappingAction", FunctionList.VSecurityAdmin },
            { "ImportExportNotificationAction", FunctionList.VImportExportNotification },
            { "UnauthorisedCaseStatusAction", FunctionList.VUnauthorisedCaseStatusAction },
            { "MonetaryOrderAcknowledgementAction", FunctionList.EMonetaryOrderAcknowledgement },
			{ XhibitActions.CourtOfAppeal, FunctionList.VCourtOfAppealAction },
            { XhibitActions.RecoredCourtroomStatistics, FunctionList.VRunRecordedCourtroomStatistics },
			{ XhibitActions.DOCARReport, FunctionList.VRunDOCARReport },
			{ XhibitActions.NFIXReport, FunctionList.VRunNFIXReport },
			{ XhibitActions.ADJSSReport, FunctionList.VRunADJSSReport },
            { XhibitActions.OUTCReport, FunctionList.VRunOUTCReport},
            { XhibitActions.UNLCReport, FunctionList.VRunUNLCReport},
			{ XhibitActions.LFIXReport, FunctionList.VRunLFIXReport },
			{ XhibitActions.PRLISReport, FunctionList.VRunPRLISReport },
			{ XhibitActions.DEFSSReport, FunctionList.VRunDEFSSReport },
			{ XhibitActions.NHAReport, FunctionList.VRunNHAReport },
			{ XhibitActions.LODReport, FunctionList.VRunLODReport },
			{ XhibitActions.OBWReport, FunctionList.VRunOBWReport },
			{ XhibitActions.CTLRPReport, FunctionList.VRunCTLRPReport },
			{ XhibitActions.CFIXReport, FunctionList.VRunCFIXReport },
			{ XhibitActions.DRSRReport, FunctionList.VRunDRSRReport },
			{ XhibitActions.RELCJReport, FunctionList.VRunRELCJReport },
			{ XhibitActions.NTRSFReport, FunctionList.VRunNTRSFReport },
			{ XhibitActions.INFTRPCReport, FunctionList.VRunINFTRPCReport},
			{ XhibitActions.RJSReport, FunctionList.VRunRJSReport },
			{ XhibitActions.RAGEReport, FunctionList.VRunRAGEReport },
			{ XhibitActions.RUMOReport, FunctionList.VRunRUMOReport },
			{ XhibitActions.RRCAReport, FunctionList.VRunRRCAReport },
			{ XhibitActions.RRECReport, FunctionList.VRunRRECReport },
			{ XhibitActions.RSITReport, FunctionList.VRunRSITReport },
			{ XhibitActions.DARTSReport, FunctionList.VRunDARTSReport },
			{ XhibitActions.QueryCompletedCase, FunctionList.EQueryCompletedCase },
        
			// reference data - chamber
            { "ChamberAndAdvocateDetailsAction", FunctionList.EChamberData },
 
			// reference data
			{ "ChamberAndAdvocateDetailsReadOnlyAction", FunctionList.EReferenceData },
            { "CourtCalendarAction", FunctionList.EReferenceData },
            { "HomeCourtCentreAndCourtroomDetailsAction", FunctionList.EReferenceData },
            { "JudgeDetailsAction", FunctionList.EReferenceData },
            { "ProsecutorRespondentDetailsAction", FunctionList.EReferenceData },
            { "SolicitorFirmDetailsAction", FunctionList.EReferenceData },

            // orders etc.
            { "OrderAction", FunctionList.VViewOrder },
            { "OrderCopyAction", FunctionList.ECopyOrder },
            { "OrderCreateAction", FunctionList.ECreateOrder },
            { XhibitActions.AppealResultOrder, FunctionList.EAppealResultOrder},
            { "MonetaryOrderViewAction", FunctionList.VMonetaryViewOrder },
            { "MonetaryOrderCopyAction", FunctionList.EMonetaryCopyOrder },
            { "MonetaryOrderCreateAction", FunctionList.EMonetaryCreateOrder },
            { "D20ViewAction", FunctionList.VD20ViewOrder },
            { "D20CopyAction", FunctionList.ED20CopyOrder },
            { "D20CreateAction", FunctionList.ED20CreateOrder },
            { "OrderExistsAction", FunctionList.VViewOrder },
            { "OrderSavedAction", FunctionList.ECreateOrder },
            { "OrderSavedDialogAction", FunctionList.ECreateOrder },
            { "OrderSummaryDefendantListener", FunctionList.VViewOrder },
            { "OrderSummaryOrderTypeListener", FunctionList.ECreateOrder },
            { "OrderSummaryTextListener", FunctionList.VViewOrder },
            { "OrderUpdateSummaryAction", FunctionList.VViewOrder },
            { "OrderViewAction", FunctionList.VViewOrder },

			{ "IMReceiverAction", FunctionList.EIMSendMessage }, { "IMSenderAction", FunctionList.EIMSendMessage },

			// create case
			{ "CreateTrialCaseAction", FunctionList.ECreateCase },
			{ "CreateSentenceCaseAction", FunctionList.ECreateCase },
			{ "CreateAppealCaseAction", FunctionList.ECreateCase },
			{ "CreateMiscCaseAction", FunctionList.ECreateCase },

			// maintain and delete case
			{ "ReplaceDeleteDeftAction", FunctionList.RDDeft },
			{ "AddIndictmentCaseAction", FunctionList.AddIndictmentCase },
			{ "MaintainCaseAction", FunctionList.EMaintainCase }, { "DeleteCaseAction", FunctionList.EDeleteCase },
			{ "TransferCaseAction", FunctionList.EMaintainCase },
            
			// Inner Classes
            { "OkAction", FunctionList.NA }, { "CancelAction", FunctionList.NA }, { "ApplyAction", FunctionList.NA },
            { "JoinMoreAction", FunctionList.NA }, { "MoveUpAction", FunctionList.NA },
            { "MoveDownAction", FunctionList.NA }, { "AppendAction", FunctionList.NA },
            { "MergeAction", FunctionList.NA }, { "DownAction", FunctionList.NA }, { "UpAction", FunctionList.NA },
            { "ViewFormAction", FunctionList.NA }, { "PrintFormAction", FunctionList.NA },
            { "SearchAction", FunctionList.NA }, { "BackAction", FunctionList.NA },
			{ "SearchDisposalAction", FunctionList.NA }, { "PleaSearchOffenceAction", FunctionList.NA },
			{ "VerdictSearchOffenceAction", FunctionList.NA }, { "OpenCasePanel_btnUpLevelAction", FunctionList.NA },
			{ "LeftAllAction", FunctionList.NA }, { "LeftAction", FunctionList.NA }, { "RightAction", FunctionList.NA },
			{ "RightAllAction", FunctionList.NA }, { "backAction", FunctionList.NA }, { "nextAction", FunctionList.NA },
			{ "finishAction", FunctionList.NA }, { "cancelAction", FunctionList.NA },
			{ "YesToAllAction", FunctionList.NA }, { "XSignInAction", FunctionList.NA },

            // William Fardell (Xdevelopment) Skeleton Schedule Start
            { "EditTrialTimeEstimateAction", FunctionList.ETrialTimeEstimate },
            { "EditSkeletonScheduleAction", FunctionList.ESkeletonSchedule },
            { "AddWitnessAction", FunctionList.AWitness }, { "EditWitnessAction", FunctionList.EWitness },
            { "ViewPreviousWeekAction", FunctionList.VPreviousWeek }, { "ViewNextWeekAction", FunctionList.VNextWeek },
            { "EditNotesAction", FunctionList.ESkeletonSchedule },
            { "IssueScheduleAction", FunctionList.ESkeletonSchedule },
            { "PrintScheduleDayAction", FunctionList.ESkeletonSchedule },
            { "PrintScheduleWeekAction", FunctionList.ESkeletonSchedule },
            { "DeleteScheduleAction", FunctionList.ESkeletonSchedule },
            // William Fardell (Xdevelopment) Skeleton Schedule End

            // William Fardell (Xdevelopment) - Crest Forms B - F Start
            { "ViewCrestFormsBFAction", FunctionList.VCrestFormsBF },
            // William Fardell (Xdevelopment) - Crest Forms B - F End

            // List distribution internal panle actions
            { "DeleteRecipientAction", FunctionList.ERecipient }

    };

    public EditMapping() {
        // empty
    }

    protected Object[][] getContents() {
        return contents;
    }
}
