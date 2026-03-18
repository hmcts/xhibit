package uk.gov.courtservice.xhibit.client.util.security;

import java.util.ListResourceBundle;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;

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
 * @author Rakesh Lakhani
 * @version 2.0
 * @history James Powell 13/02/1009 - added entry for Unauthorised Case
 *          Functionality
 */

public class ReadMapping extends ListResourceBundle {
	private Object[][] contents = { { "AddBreachAction", FunctionList.VCharge },
            { "AddWizardBreachOffenceAction", FunctionList.VCharge },
            { "RemoveWizardBreachOffenceAction", FunctionList.VCharge },
			{ "AddBreachOffenceAction", FunctionList.VCharge }, { "AddC4SOffenceAction", FunctionList.VCharge },

			{ "C4SBringBackAction", FunctionList.VCharge }, { "C4SPutAndAdmittedAction", FunctionList.VCharge },
			{ "C4SNotAdmittedAction", FunctionList.VCharge }, { "SOProsecutionNoEvidenceAction", FunctionList.VCharge },
            { "DefendantSummaryOffencesAction", FunctionList.VCharge },
            { "ApplicationToSeverAction", FunctionList.VCharge },
            { "VoluntaryBillPreferredAction", FunctionList.VCharge },
			{ "LateBillOfIndictmentAction", FunctionList.VCharge }, { "BillOfIndictmentAction", FunctionList.VCharge },

			{ "AddCountAction", FunctionList.VCharge }, { "AddCountToJoinderAction", FunctionList.VCharge },
            { "AddCountsToDefendantAction", FunctionList.VCharge },
            { "AddDefendantsToCountAction", FunctionList.VCharge },
            { "RemoveDefendantsFromCountAction", FunctionList.VCharge },
			{ "RenumberCountsAction", FunctionList.VCharge }, { "AddDefendantsToOffenceAction", FunctionList.VCharge },
			{ "AddIndictmentAction", FunctionList.VCharge }, { "AddS41OffenceAction", FunctionList.VCharge },
			{ "AddAppealOffenceAction", FunctionList.VCharge }, { "ChangeCountAction", FunctionList.VCharge },
			{ "ChangeCourtAction", FunctionList.VCharge }, { "ChangeDefendantAction", FunctionList.VCharge },
			{ "ChangeOffenceAction", FunctionList.VCharge }, { "CountParticularsAmendedAction", FunctionList.VCharge },
            { "EditBreachAction", FunctionList.VCharge },
            // {"LieOnFileCountAction", FunctionList.VCharge},
            // {"LieOnFileDefendantOnCountAction", FunctionList.VCharge},
            // {"LieOnFileDefendantOnIndictmentAction",
            // FunctionList.VCharge},
			{ "RemoveBreachAction", FunctionList.VCharge },
            // Bail Act Offences
			{ "AddBailActOffenceAction", FunctionList.VCharge },
			{ "AddWizardBailActOffenceAction", FunctionList.VCharge },
			{ "ChangeBailActOffenceAction", FunctionList.VCharge },
			{ "RemoveBailActOffenceAction", FunctionList.VCharge },
            
			{ "RemoveCountAction", FunctionList.VCharge }, { "RemoveIndictmentAction", FunctionList.VCharge },
			{ "RemoveOffenceAction", FunctionList.VCharge }, { "RenumberCountsAction", FunctionList.VCharge },
			{ "SignIndictmentAction", FunctionList.VCharge }, { "SignIndictmentRefusedAction", FunctionList.VCharge },
			{ "StayCountAction", FunctionList.VCharge }, { "StayDefendantOnCountAction", FunctionList.VCharge },
            { "StayDefendantOnIndictmentAction", FunctionList.VCharge },
			{ "StayIndictmentAction", FunctionList.VCharge }, { "ExportChargesAction", FunctionList.VCharge },
			{ "OriginalChargesAction", FunctionList.VCharge }, { "tbAddAction", FunctionList.VCharge },
			{ "tbAddChargeAction", FunctionList.VCharge }, { "tbAddDefendantAction", FunctionList.VCharge },
			{ "tbAddOffenceAction", FunctionList.VCharge }, { "tbChangeAction", FunctionList.VCharge },
			{ "tbLieOnFileAction", FunctionList.VCharge }, { "tbStayAction", FunctionList.VCharge },
            { "AdditionalOffenceInfoAction", FunctionList.VCharge },
            { "AdditionalCountInfoAction", FunctionList.VCharge },
            { "AdditionalDefendantOnOffenceInfoAction", FunctionList.VCharge },
            { "AdditionalDefendantOnCountInfoAction", FunctionList.VCharge },
			{ "AdditionalBreachOffenceDefendantInfoAction", FunctionList.VCharge }, { "HelpAction", FunctionList.NA },
			{ "AboutAction", FunctionList.NA }, { "CloseAction", FunctionList.NA }, { "CopyAction", FunctionList.NA },
			{ "CopyTextComponentToClipboardAction", FunctionList.NA }, { "CutAction", FunctionList.NA },
			{ "DisposeWindowAction", FunctionList.NA }, { "ExitAction", FunctionList.NA },
			{ "NewAction", FunctionList.NA }, { "PasteAction", FunctionList.NA },
			{ "ViewToolbarAction", FunctionList.NA }, { "PrintAction", FunctionList.NA },
			{ "PrintToolbarAction", FunctionList.NA }, { "PrintPreviewAction", FunctionList.NA },
			{ "SaveAction", FunctionList.NA }, { "SpecialMeasuresApplicationAction", FunctionList.VCourtLog },
			{ "BailCustodyAction", FunctionList.VCourtLog }, { "clLongAdjournmentAction", FunctionList.VCourtLog },
			{ "EndHearingAction", FunctionList.VCourtLog }, { "PreliminaryHearingsAction", FunctionList.VCourtLog },
			{ "clShortAdjournmentAction", FunctionList.VCourtLog }, { "clTimeEstimateAction", FunctionList.VCourtLog },
			{ "clWitnessSwornAction", FunctionList.VCourtLog }, { "clWitnessReadAction", FunctionList.ECourtLog },
            { "clWitnessSwornAppealAction", FunctionList.VCourtLog },
			{ "clWitnessReadAppealAction", FunctionList.VCourtLog }, { "CourtLogAction", FunctionList.NA },
            { "DeleteCourtLogEventAction", FunctionList.VCourtLog },
			{ "EditCourtLogEventAction", FunctionList.VCourtLog }, { "EditDirectionsActions", FunctionList.VCourtLog },
			{ "FreeTextEventAction", FunctionList.VCourtLog }, { "JuryDischargedAction", FunctionList.VCourtLog },
			{ "JurySwornInAction", FunctionList.VCourtLog }, { "LegalArgumentOptionsAction", FunctionList.VCourtLog },
            { "LegalArgumentOptionsAppealAction", FunctionList.VCourtLog },
            { "LegalArgumentOptionsTrialAction", FunctionList.VCourtLog },
			{ "MediumMDEventAction", FunctionList.VCourtLog }, { "MediumMLEventAction", FunctionList.VCourtLog },
			{ "MediumMTEventAction", FunctionList.VCourtLog }, { "BWEventAction", FunctionList.VCourtLog },
			{ "EndBWEventAction", FunctionList.VCourtLog }, { "OpenOtherDaysLogAction", FunctionList.VCourtLog },
			{ "SimpleEventAction", FunctionList.VCourtLog }, { "TakenIntoConsiderationAction", FunctionList.VCourtLog },
			{ "LinkCasesAction", FunctionList.ELinkCases }, { "UnlinkCaseAction", FunctionList.EUnlinkCases },
            { "NewLinkCasesAction", FunctionList.ENewLinkCases },
            { "NewUnlinkCasesAction", FunctionList.ENewUnlinkCases },
			{ "TodaysScheduleAction", FunctionList.VSchedule }, { "ViewChargesAction", FunctionList.VCharge },
            { "ViewCourtLogAction", FunctionList.VCourtLog },
            // {"AddBWHAction", FunctionList.EAddBenchWarrant},
			{ "AddHearingAction", FunctionList.EAddHearing }, { "LinkUnlinkCasesAction", FunctionList.ECreateCase },
			{ "MoveCaseAction", FunctionList.EMoveCase }, { "OpenCaseAction", FunctionList.VCourtLog },
			{ "UpdateCaseAction", FunctionList.VCourtLog }, { "ViewCaseAction", FunctionList.VCourtLog },

            { "OpenAddAppealAdvocateAction", FunctionList.VCounselSignin },
            { "OpenAddCourtClerkAction", FunctionList.VCourtLog },
            { "OpenAddDefenceAdvocateAction", FunctionList.VCounselSignin },
			{ "OpenAddUsherAction", FunctionList.VCourtLog }, { "OpenAmendDefendantAction", FunctionList.EDefendant },
            { "OpenChangeProsecutionAdvocateAction", FunctionList.VCounselSignin },
            { "OpenChangeRespondentAdvocateAction", FunctionList.VCounselSignin },
            { "OpenChangeShorthandWriterAction", FunctionList.VCourtLog },
            { "OpenUpdateCasePropertiesAction", FunctionList.VCaseProperty },
            { "RemoveAppealAdvocateAction", FunctionList.VCounselSignin },
            { "RemoveCourtClerkAction", FunctionList.VCourtLog },
            { "RemoveDefendantAdvocateAction", FunctionList.VCounselSignin },
			{ "RemoveUsherAction", FunctionList.VCourtLog }, { "UpdateDefendantAction", FunctionList.EDefendant },

            // New actions for itr 2
			{ "AbstractSearchAction", FunctionList.NA }, { "ActivatePublicDisplayAction", FunctionList.EPublicDisplay },
			{ "AddDisposalAction", FunctionList.EDisposal }, { "AddMagistrateDisposalAction", FunctionList.EDisposal },
			{ "AddVariationDisposalAction", FunctionList.EDisposal }, { "AppealResultAction", FunctionList.EVerdict },
            { "AuthoriseResultsAction", FunctionList.EAuthoriseResult },
            { "AuthoriseSyncAction", FunctionList.EAuthoriseResult },
            { "AuthorisePreviewAction", FunctionList.EAuthoriseResult },
			{ "CaseDirectionsAction", FunctionList.VCourtLog }, { "CaseProgressAction", FunctionList.VCaseProgress },
			{ "CopyDisposalAction", FunctionList.EDisposal }, { "CopyUnrelatedDisposalAction", FunctionList.EDisposal },
            { "CounselSignInWizardAction", FunctionList.VCounselSignin },
			{ "CrestIndictmentLogAction", FunctionList.VCharge }, { "DailyListAction", FunctionList.VRecipient },
			{ "DailyListPrisonAction", FunctionList.VRecipient }, { "DeleteDisposalAction", FunctionList.EDisposal },
			{ "DeleteDocumentAction", FunctionList.EDisposal }, { "EditDefendantAction", FunctionList.EDefendant },
			{ "EditDefendantSpecialAction", FunctionList.EDefendant }, { "EditDisposalAction", FunctionList.EDisposal },
            { "EditEstimateForTrialAction", FunctionList.VHearingRecord },
            { "ExportHearingRecordAction", FunctionList.VHearingRecord },
            { "FindCounselDefendantAction", FunctionList.VCounselSignin },
            { "FirmListAction", FunctionList.VRecipient },
			{ "HearingDetailsRecalculateAction", FunctionList.VHearingRecord }, { "JoinAction", FunctionList.VCharge },
            { "LinkHearingAction", FunctionList.VHearingRecord },
			{ "LinkHearingSearchAction", FunctionList.VHearingRecord }, { "MultiplePleaAction", FunctionList.EPlea },
            { "OpenCrestFormAAction", FunctionList.VHearingRecord },
            { "OpenLinkedHearingsSummary", FunctionList.VHearingRecord },
            { "OpenOtherCaseAction", FunctionList.VCourtLog },
			{ "OpenSearchCollectingMagCourtsAction", FunctionList.NA }, { "OpenSearchCourtAction", FunctionList.NA },
			{ "OpenSearchJudgeAction", FunctionList.NA },
			{ "OpenSearchLegalRepAction", FunctionList.NA }, { "OpenSearchOffenceAction", FunctionList.NA },
			{ "OpenSearchListingJudgeAction", FunctionList.NA }, { "OpenSearchBailActOffenceAction", FunctionList.NA },
			{ "OpenSearchSolicitorFirmAction", FunctionList.NA }, { "PleaAction", FunctionList.EPlea },
			{ "PleasAndDirectionsAction", FunctionList.EPlea }, { "PreviewDailyList", FunctionList.VSchedule },
            { "PreviewTomorrowsList", FunctionList.VTomorrowSchedule },
			{ "PrintCrestFormAAction", FunctionList.VHearingRecord }, { "PrintWLLAction", FunctionList.VWLLRecipient },
            { "DistributeListLettersAction", FunctionList.VListCase },
            { "MaintainListLetterRecipientsAction", FunctionList.VWLLRecipient },
            { "PublicNoticesAction", FunctionList.EPublicDisplay },
            // {"QuashCountAction",FunctionList.EDisposal},
            // {"QuashDefendantOnCountAction",FunctionList.EDisposal},
            // {"QuashDefendantOnIndictmentAction",FunctionList.EDisposal},
            // {"QuashIndictmentAction",FunctionList.EDisposal},
            { "RemoveRespondentAdvocateAction", FunctionList.VCounselSignin },
			{ "RunningListAction", FunctionList.VRecipient }, { "SearchMagCourtsAction", FunctionList.NA },
			{ "SearchProcessHandler", FunctionList.NA }, { "SelectAllAction", FunctionList.NA },
			{ "SentenceAction", FunctionList.EDisposal }, { "SevenFourteenDayOrderAction", FunctionList.VCourtLog },
			{ "tbQuashAction", FunctionList.EDisposal }, { "UndeleteDisposalAction", FunctionList.EDisposal },
			{ "UnlinkHearingAction", FunctionList.VHearingRecord }, { "VerdictAction", FunctionList.EVerdict },
            // {"VerifyResultsAction",FunctionList.VCaseProgress},
            { "ViewChargeDetailsAction", FunctionList.VCaseProgress },
            // {"ViewCrestFormsAction",FunctionList.VCrestForms},
            { "ViewDistributionStatusAction", FunctionList.VRecipient },
            { "ViewInformationPagesAction", FunctionList.VPublicDisplay },
			{ "InformationPagesAction", FunctionList.VPublicDisplay }, { "WarnedListAction", FunctionList.VRecipient },
            { "WarnedListLetterAction", FunctionList.VWLLRecipient },

            // admin
            { "CrestImportAction", FunctionList.ECrestImport },
            { "PublicDisplayConfigurationAction", FunctionList.EPublicDisplayAdmin },
            { "PublicDisplayConfigAction", FunctionList.EPublicDisplayAdmin },
            { "RoleMappingAction", FunctionList.VSecurityAdmin },
            { "ImportExportNotificationAction", FunctionList.VImportExportNotification },
            { "UnauthorisedCaseStatusAction", FunctionList.VUnauthorisedCaseStatusAction },
            { "MonetaryOrderAcknowledgementAction", FunctionList.EMonetaryOrderAcknowledgement },
            { "CourtOfAppealAction", FunctionList.VCourtOfAppealAction },
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
			{ "OrderAction", FunctionList.VViewOrder }, { "OrderCopyAction", FunctionList.ECopyOrder },
            { "OrderCreateAction", FunctionList.ECreateOrder },
            { XhibitActions.AppealResultOrder, FunctionList.EAppealResultOrder},
            { "MonetaryOrderViewAction", FunctionList.VMonetaryViewOrder },
            { "MonetaryOrderCopyAction", FunctionList.EMonetaryCopyOrder },
            { "MonetaryOrderCreateAction", FunctionList.EMonetaryCreateOrder },
			{ "D20ViewAction", FunctionList.VD20ViewOrder }, { "D20CopyAction", FunctionList.ED20CopyOrder },
			{ "D20CreateAction", FunctionList.ED20CreateOrder }, { "OrderExistsAction", FunctionList.VViewOrder },
			{ "OrderSavedAction", FunctionList.ECreateOrder }, { "OrderSavedDialogAction", FunctionList.ECreateOrder },
            { "OrderSummaryDefendantListener", FunctionList.VViewOrder },
            { "OrderSummaryOrderTypeListener", FunctionList.ECreateOrder },
            { "OrderSummaryTextListener", FunctionList.VViewOrder },
			{ "OrderUpdateSummaryAction", FunctionList.VViewOrder }, { "OrderViewAction", FunctionList.VViewOrder },

			{ "IMReceiverAction", FunctionList.EIMSendMessage }, { "IMSenderAction", FunctionList.EIMSendMessage },

			// create case
			{ "CreateTrialCaseAction", FunctionList.ECreateCase },
			{ "CreateSentenceCaseAction", FunctionList.ECreateCase },
			{ "CreateAppealCaseAction", FunctionList.ECreateCase },
			{ "CreateMiscCaseAction", FunctionList.ECreateCase },

			// maintain and delete case
			{ "AddIndictmentCaseAction", FunctionList.AddIndictmentCase },
			{ "ReplaceDeleteDeftAction", FunctionList.RDDeft }, { "MaintainCaseAction", FunctionList.EMaintainCase },
			{ "DeleteCaseAction", FunctionList.EDeleteCase }, { "TransferCaseAction", FunctionList.EMaintainCase },

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

            // Skeleton Schedule Start
            { "EditTrialTimeEstimateAction", FunctionList.ETrialTimeEstimate },
            { "EditSkeletonScheduleAction", FunctionList.ESkeletonSchedule },
            { "AddWitnessAction", FunctionList.AWitness }, { "EditWitnessAction", FunctionList.EWitness },
            { "ViewPreviousWeekAction", FunctionList.VPreviousWeek }, { "ViewNextWeekAction", FunctionList.VNextWeek },
            { "EditNotesAction", FunctionList.ESkeletonSchedule },
            { "IssueScheduleAction", FunctionList.ESkeletonSchedule },
            { "PrintScheduleDayAction", FunctionList.ESkeletonSchedule },
            { "PrintScheduleWeekAction", FunctionList.ESkeletonSchedule },
            { "DeleteScheduleAction", FunctionList.ESkeletonSchedule },
            // Skeleton Schedule End
            
			// Listings
            
			{ "CaseListingEntryAction", FunctionList.EMaintainList },
			{ "ListOfficersDiaryAction", FunctionList.EMaintainList },
			{ "CreateListAction", FunctionList.EMaintainList },
			{ "OpenExistingListAction", FunctionList.EMaintainList },
			{ "ListResultsAction", FunctionList.EMaintainList },
			{ "NonAvailableDaysAction", FunctionList.EMaintainList },
			{ XhibitActions.CaseSummary, FunctionList.EMaintainList },

            // Crest Forms B - F Start
            { "ViewCrestFormsBFAction", FunctionList.VCrestFormsBF },
            // Crest Forms B - F End

            // List distribution internal panle actions
            { "DeleteRecipientAction", FunctionList.ERecipient } };

    public ReadMapping() {
        // empty
    }

    protected Object[][] getContents() {
        return contents;
    }
}