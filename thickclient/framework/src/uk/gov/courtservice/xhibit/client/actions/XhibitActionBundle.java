package uk.gov.courtservice.xhibit.client.actions;

import java.util.ListResourceBundle;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Register each action declaredin the XhibitActions in this class
 * as well
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 2.0
 * @history James Powell 13/03/2008 - Added an action for Unauthorised Case
 *          Status Functionality
 * @history luis Valenzuela 08/05/2009 - Added Bail Act offence Actions.
 * @history Andy Ruddock 26/04/2017 - Added Case Creation Actions.
 */

public class XhibitActionBundle extends ListResourceBundle {

	// The simple and medium events can not be part of this bundle as they
	// are
	// reused multiple times. Therefore, they need to be created as
	// individual actions,
	// Then the CourtLogXMLReader will manage the individual instances per
	// XhibitApplicationController.
	// {XhibitActions.clSimpleEvent, SimpleEventAction.class},
	// {XhibitActions.clMediumChoiceEvent, TempEventAction.class},
	// {XhibitActions.clMediumDateEvent, TempEventAction.class},
	// {XhibitActions.clMediumTextEvent, TempEventAction.class},

	private Object[][] contents = null;

	public XhibitActionBundle() {
		// empty
	}

	protected Object[][] getContents() {
		return contents;
	}

	{
		try {
			Object[][] tmpContents = {
					{ XhibitActions.OpenSearchJudge,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.search.OpenSearchJudgeAction") },
					{ XhibitActions.OpenSearchListingJudge,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.search.OpenSearchListingJudgeAction") },
					{ XhibitActions.OpenSearchJudgeUpdateParent,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.search.OpenSearchJudgeUpdateParentAction") },
					{ XhibitActions.OpenSearchCase,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.search.OpenSearchCaseAction") },
					{ XhibitActions.OpenSearchCourt,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.search.OpenSearchCourtAction") },
					{ XhibitActions.OpenSearchOffence,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.search.OpenSearchOffenceAction") },
					{ XhibitActions.OpenSearchBailActOffence,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.search.OpenSearchBailActOffenceAction") },
					{ XhibitActions.OpenSearchObsoleteOffence,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.search.OpenSearchObsoleteOffenceAction") },
					{ XhibitActions.OpenSearchSolicitorFirm,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.search.OpenSearchSolicitorFirmAction") },
					{ XhibitActions.OpenSearchColMagCourts, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.search.OpenSearchCollectingMagCourtsAction") },

					{ XhibitActions.Open,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.todaysschedule.OpenCaseAction") },
					{ XhibitActions.OpenOtherLog,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.todaysschedule.OpenOtherCaseAction") },
					{ XhibitActions.Close,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.common.CloseAction") },

					{ XhibitActions.Print,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.menu.PrintAction") },
					{ XhibitActions.PrintToolbar,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.menu.PrintToolbarAction") },
					{ XhibitActions.PrintPreview,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.menu.PrintPreviewAction") },
					{ XhibitActions.Save, Class.forName("uk.gov.courtservice.xhibit.client.actions.menu.SaveAction") },
					{ XhibitActions.OpenOtherDaysLog,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.courtlog.OpenOtherDaysLogAction") },
					{ XhibitActions.CounselSignInWizard,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.counselfacilities.CounselSignInWizardAction") },
					{ XhibitActions.FindCounselDefendant, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.counselfacilities.FindCounselDefendantAction") },

					{ XhibitActions.CaseProps,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.updatecase.OpenUpdateCasePropertiesAction") },
					{ XhibitActions.OpenAddDefendantAdvocate,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAddDefenceAdvocateAction") },
					{ XhibitActions.RemoveDefendantAdvocate,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.updatecase.RemoveDefendantAdvocateAction") },
					{ XhibitActions.OpenAddAppealAdvocate,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAddAppealAdvocateAction") },
					{ XhibitActions.RemoveAppealAdvocate, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.updatecase.RemoveAppealAdvocateAction") },

					{ XhibitActions.OpenAddCourtClerk,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAddCourtClerkAction") },
					{ XhibitActions.RemoveCourtClerk,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.updatecase.RemoveCourtClerkAction") },
					{ XhibitActions.OpenAddUsher,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAddUsherAction") },
					{ XhibitActions.RemoveUsher,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.updatecase.RemoveUsherAction") },
					{ XhibitActions.OpenChangeRespondentAdvocate, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.updatecase.OpenChangeRespondentAdvocateAction") },

					{ XhibitActions.OpenAmendDefendant,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAmendDefendantAction") },
					{ XhibitActions.UpdateDefendant, Class
							.forName("uk.gov.courtservice.xhibit.client.actions.updatecase.UpdateDefendantAction") },

					{ XhibitActions.ViewCase,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.todaysschedule.ViewCaseAction") },

					{ XhibitActions.EditSelectAll,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.menu.SelectAllAction") },

					{ XhibitActions.HearingDetailsRecalculate,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.hearingrecord.HearingDetailsRecalculateAction") },
					{ XhibitActions.SearchMagCourts,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.hearingrecord.SearchMagCourtsAction") },
					{ XhibitActions.PrintCrestFormA,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.hearingrecord.PrintCrestFormAAction") },
					{ XhibitActions.OpenLinkedHearingsSummary,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.hearingrecord.OpenLinkedHearingsSummary") },
					{ XhibitActions.LinkHearing,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.hearingrecord.LinkHearingAction") },
					{ XhibitActions.UnlinkHearing,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.hearingrecord.UnlinkHearingAction") },
					{ XhibitActions.LinkHearingSearch,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.hearingrecord.LinkHearingSearchAction") },
					{ XhibitActions.EditDefendant,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.hearingrecord.EditDefendantAction") },
					{ XhibitActions.EditDefendantSpecial,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.hearingrecord.EditDefendantSpecialAction") },
					{ XhibitActions.EditEstimateForTrial, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.hearingrecord.EditEstimateForTrialAction") },

					{ XhibitActions.EditClEvent,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.courtlog.EditCourtLogEventAction") },
					{ XhibitActions.DeleteClEvent,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.courtlog.DeleteCourtLogEventAction") },
					{ XhibitActions.BailCustody,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.courtlog.BailCustodyAction") },
					{ XhibitActions.clShortAdjournment,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.courtlog.clShortAdjournmentAction") },
					{ XhibitActions.clLongAdjournment,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.courtlog.clLongAdjournmentAction") },
					{ XhibitActions.clTimeEstimate,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.courtlog.clTimeEstimateAction") },
					{ XhibitActions.JurySwornIn,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.courtlog.JurySwornInAction") },
					{ XhibitActions.clWitnessSworn,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.courtlog.clWitnessSwornAction") },
					{ XhibitActions.clWitnessRead,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.courtlog.clWitnessReadAction") },
					{ XhibitActions.JuryDischarged,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.courtlog.JuryDischargedAction") },
					{ XhibitActions.TakenIntoConsideration,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.courtlog.TakenIntoConsiderationAction") },
					{ XhibitActions.clWitnessSwornAppeal,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.courtlog.clWitnessSwornAppealAction") },
					{ XhibitActions.clWitnessReadAppeal,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.courtlog.clWitnessReadAppealAction") },
					{ XhibitActions.SevenFourteenDayOrder,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.courtlog.SevenFourteenDayOrderAction") },
					{ XhibitActions.EndHearing,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.courtlog.EndHearingAction") },
					{ XhibitActions.PreliminaryHearings,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.courtlog.PreliminaryHearingsAction") },
					{ XhibitActions.CaseDirections,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.courtlog.CaseDirectionsAction") },

					{ XhibitActions.SpecialMeasuresApplication, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.courtlog.SpecialMeasuresApplicationAction") },

					{ XhibitActions.AddBreach,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.AddBreachAction") },
					{ XhibitActions.AddBreachOffence,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.AddBreachOffenceAction") },
					{ XhibitActions.AddC4SOffence,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.AddC4SOffenceAction") },
					{ XhibitActions.AddAppealOffence,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.AddAppealOffenceAction") },
					// Added for CCN701
					{ XhibitActions.AddBailActOffence,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AddBailActOffenceAction") },
					{ XhibitActions.AddWizardBailActOffence,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AddWizardBailActOffenceAction") },
					{ XhibitActions.ChangeBailActOffence,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.ChangeBailActOffenceAction") },
					{ XhibitActions.RemoveBailActOffence, Class
							.forName("uk.gov.courtservice.xhibit.client.actions.charges.RemoveBailActOffenceAction") },

					{ XhibitActions.AddCount,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.AddCountAction") },
					{ XhibitActions.AddCountToJoinder,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AddCountToJoinderAction") },
					{ XhibitActions.AddCountsToDefendant,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AddCountsToDefendantAction") },
					{ XhibitActions.AddDefendantsToCount,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AddDefendantsToCountAction") },
					{ XhibitActions.RemoveDefendantsOnCount, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.charges.RemoveDefendantsFromCountAction") },
					{ XhibitActions.CopyCharge,
								Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.CopyChargeAction") },

					{ XhibitActions.RenumberCounts,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.RenumberCountsAction") },

					{ XhibitActions.AddDefendantsToOffence,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AddDefendantsToOffenceAction") },
					{ XhibitActions.AddIndictment,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.AddIndictmentAction") },
					{ XhibitActions.AddS41Offence,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.AddS41OffenceAction") },
					{ XhibitActions.AddUncodedOffence,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AddUncodedOffenceAction") },
					{ XhibitActions.AddWizardBreachOffence,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AddWizardBreachOffenceAction") },
					{ XhibitActions.ApplicationToSever,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.ApplicationToSeverAction") },
					{ XhibitActions.BillOfIndictment,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.BillOfIndictmentAction") },
					{ XhibitActions.C4SBringBack,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.C4SBringBackAction") },
					{ XhibitActions.C4SNotAdmitted,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.C4SNotAdmittedAction") },
					{ XhibitActions.C4SPutAndAdmitted,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.C4SPutAndAdmittedAction") },
					{ XhibitActions.ChangeCount,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.ChangeCountAction") },
					{ XhibitActions.ChangeDefendant,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.ChangeDefendantAction") },
					{ XhibitActions.ChangeOffence,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.ChangeOffenceAction") },
					{ XhibitActions.UpdateUncodedOffence,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.UpdateUncodedOffenceAction") },
					{ XhibitActions.CountParticularsAmended,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.CountParticularsAmendedAction") },
					{ XhibitActions.CrestIndictmentLog,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.CrestIndictmentLogAction") },
					{ XhibitActions.DefendantSummaryOffences,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.DefendantSummaryOffencesAction") },
					{ XhibitActions.EditBreachProps,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.EditBreachAction") },
					{ XhibitActions.ExportCharges,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.ExportChargesAction") },
					{ XhibitActions.OriginalCharges,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.OriginalChargesAction") },
					{ XhibitActions.JoinIndictment,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.JoinAction") },
					{ XhibitActions.LateBillOfIndictment,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.LateBillOfIndictmentAction") },
					// { XhibitActions.LieOnFileCount,
					// Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.LieOnFileCountAction")
					// },
					// { XhibitActions.LieOnFileDefendantOnCount,
					// Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.LieOnFileDefendantOnCountAction")
					// },
					// { XhibitActions.LieOnFileDefendantOnIndictment,
					// Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.LieOnFileDefendantOnIndictmentAction")
					// },
					// { XhibitActions.QuashCount,
					// Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.QuashCountAction")
					// },
					// { XhibitActions.QuashDefendantOnCount,
					// Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.QuashDefendantOnCountAction")
					// },
					// { XhibitActions.QuashDefendantOnIndictment,
					// Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.QuashDefendantOnIndictmentAction")
					// },
					// { XhibitActions.QuashIndictment,
					// Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.QuashIndictmentAction")
					// },
					{ XhibitActions.RemoveBreach,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.RemoveBreachAction") },
					{ XhibitActions.RemoveCount,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.RemoveCountAction") },
					{ XhibitActions.RemoveIndictment,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.RemoveIndictmentAction") },
					{ XhibitActions.RemoveOffence,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.RemoveOffenceAction") },
					{ XhibitActions.RemoveWizardBreachOffence,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.RemoveWizardBreachOffenceAction") },
					{ XhibitActions.SOProsecutionNoEvidence,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.SOProsecutionNoEvidenceAction") },
					{ XhibitActions.SignIndictment,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.SignIndictmentAction") },
					{ XhibitActions.SignIndictmentRefused,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.SignIndictmentRefusedAction") },
					{ XhibitActions.StayCount,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.StayCountAction") },
					{ XhibitActions.StayDefendantOnCount,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.StayDefendantOnCountAction") },
					{ XhibitActions.StayDefendantOnIndictment,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.StayDefendantOnIndictmentAction") },
					{ XhibitActions.StayIndictment,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.StayIndictmentAction") },
					{ XhibitActions.VoluntaryBillPreferred,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.VoluntaryBillPreferredAction") },
					{ XhibitActions.tbAdd,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.tbAddAction") },
					{ XhibitActions.tbAddCharge,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.tbAddChargeAction") },
					{ XhibitActions.tbAddDefendant,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.tbAddDefendantAction") },
					{ XhibitActions.tbAddOffence,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.tbAddOffenceAction") },
					{ XhibitActions.tbChange,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.tbChangeAction") },
					// { XhibitActions.tbLieOnFile,
					// Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.tbLieOnFileAction")
					// },
					// { XhibitActions.tbQuash,
					// Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.tbQuashAction")
					// },
					{ XhibitActions.AdditionalOffenceInfo,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AdditionalOffenceInfoAction") },
					{ XhibitActions.AdditionalCountInfo,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AdditionalCountInfoAction") },
					{ XhibitActions.AdditionalDefendantOnOffenceInfo,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AdditionalDefendantOnOffenceInfoAction") },
					{ XhibitActions.AdditionalDefendantOnCountInfo,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AdditionalDefendantOnCountInfoAction") },
					{ XhibitActions.AdditionalBreachOffenceDefendantInfo,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.charges.AdditionalBreachOffenceDefendantInfoAction") },
					{ XhibitActions.tbStay,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.charges.tbStayAction") },

					{ XhibitActions.PreviewDailyList,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.menu.PreviewDailyList") },
					{ XhibitActions.PreviewTomorrowsList,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.menu.PreviewTomorrowsList") },
					{ XhibitActions.ViewTodaysSchedule,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.menu.TodaysScheduleAction") },
					{ XhibitActions.ViewCourtLog,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.menu.ViewCourtLogAction") },
					{ XhibitActions.ViewCaseProgress,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.results.CaseProgressAction") },
					{ XhibitActions.ViewCharges,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.menu.ViewChargesAction") },

					{ XhibitActions.DailyList,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.DailyListAction") },
					{ XhibitActions.DailyListPrison,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.DailyListPrisonAction") },
					{ XhibitActions.RunningList,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.RunningListAction") },
					{ XhibitActions.FirmList,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.FirmListAction") },
					{ XhibitActions.WarnedList,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.WarnedListAction") },
					{ XhibitActions.WarnedListLetter,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.WarnedListLetterAction") },
					{ XhibitActions.PrintWLL,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.PrintWLLAction") },
					{ XhibitActions.DistributeListLetters,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.DistributeListLettersAction") },
					{ XhibitActions.MaintainListLetterRecipients,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.MaintainListLetterRecipientsAction") },
					{ XhibitActions.ViewDistributionStatus,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.ViewDistributionStatusAction") },
					{ XhibitActions.DeleteDocument,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.DeleteDocumentAction") },
					{ XhibitActions.CreateList,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.CreateListAction") },
					{ XhibitActions.OpenExistingList,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.OpenExistingListAction") },
					{ XhibitActions.CaseListingDetail,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.CaseListingDetailAction") },
					{ XhibitActions.CaseListingEntry,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.CaseListingEntryAction") },
					{ XhibitActions.ListOfficersDiary,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.ListOfficersDiaryAction") },
					{ XhibitActions.ListResults,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.ListResultsAction") },
					{ XhibitActions.CaseSummary,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.CaseSummaryMenuItemAction") },
					{ XhibitActions.NonAvailableDays,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.NonAvailableDaysAction") },
					{ XhibitActions.UpdateCase,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.todaysschedule.UpdateCaseAction") },
					{ XhibitActions.NewLinkCases,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.NewLinkCasesAction") },
					{ XhibitActions.LinkUnlinkCases,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.LinkUnlinkCasesAction") },
					{ XhibitActions.NewUnlinkCases,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.listdistribution.NewUnlinkCasesAction") },
					{ XhibitActions.MoveCase,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.todaysschedule.MoveCaseAction") },
					// { XhibitActions.AddBWH,
					// Class.forName("uk.gov.courtservice.xhibit.client.actions.todaysschedule.AddBWHAction")
					// },
					{ XhibitActions.AddHearing,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.todaysschedule.AddHearingAction") },
					{ XhibitActions.CreateCase,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.casemanagement.CreateCaseAction") },
					{ XhibitActions.CreateTrialCase,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.casemanagement.CreateTrialCaseAction") },
					{ XhibitActions.CreateSentenceCase,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.casemanagement.CreateSentenceCaseAction") },
					{ XhibitActions.CreateAppealCase,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.casemanagement.CreateAppealCaseAction") },
					{ XhibitActions.CreateMiscCase,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.casemanagement.CreateMiscCaseAction") },
					{ XhibitActions.MaintainCase,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.casemanagement.MaintainCaseAction") },
					{ XhibitActions.AddIndictmentCase,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.casemanagement.AddIndictmentCaseAction") },
					{ XhibitActions.ReplaceDeleteDeft,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.casemanagement.ReplaceDeleteDeftAction") },
					{ XhibitActions.TransferCase,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.casemanagement.TransferCaseAction") },
					{ XhibitActions.DeleteCase,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.casemanagement.DeleteCaseAction") },
					{ XhibitActions.LinkCases,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.linkedhearings.LinkCasesAction") },
					{ XhibitActions.UnlinkCase,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.linkedhearings.UnlinkCaseAction") },
					{ XhibitActions.AddDisposal,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.results.AddDisposalAction") },
					{ XhibitActions.AddMagistrateDisposal,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.AddMagistrateDisposalAction") },
					{ XhibitActions.AddVariationDisposal,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.AddVariationDisposalAction") },
					{ XhibitActions.EditDisposal,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.results.EditDisposalAction") },
					{ XhibitActions.DeleteDisposal,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.results.DeleteDisposalAction") },
					{ XhibitActions.UndeleteDisposal,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.results.UndeleteDisposalAction") },
					{ XhibitActions.CopyDisposal,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.results.CopyDisposalAction") },
					{ XhibitActions.CopyUnrelatedDisposal,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.CopyUnrelatedDisposalAction") },
					{ XhibitActions.ViewChargeDetailsAction,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.caseprogress.ViewChargeDetailsAction") },
					{ XhibitActions.ActivatePublicDisplay,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.menu.ActivatePublicDisplayAction") },
					{ XhibitActions.PublicNotice,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.menu.PublicNoticesAction") },
					{ XhibitActions.ViewInformationPagesV2,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.publicdisplayconfig.actions.InformationPagesAction") },
					{ XhibitActions.PleasAndDirections,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.PleasAndDirectionsAction") },
					{ XhibitActions.Plea,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.results.PleaAction") },
					{ XhibitActions.MultiplePlea,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.results.MultiplePleaAction") },
					{ XhibitActions.Verdict,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.results.VerdictAction") },
					{ XhibitActions.Sentence,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.results.SentenceAction") },
					{ XhibitActions.AppealResult,
							Class.forName("uk.gov.courtservice.xhibit.client.actions.results.AppealResultAction") },
					// {XhibitActions.VerifyResults,
					// Class.forName("uk.gov.courtservice.xhibit.client.actions.results.VerifyResultsAction")},

					{ XhibitActions.AuthoriseResults,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.authorise.AuthoriseResultsAction") },
					{ XhibitActions.AuthoriseSync,
							Class.forName("uk.gov.courtservice.xhibit.client.results.authorise.AuthoriseSyncAction") },
					{ XhibitActions.AuthorisePreview,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.results.authorise.AuthorisePreviewAction") },
					{ XhibitActions.OrderCreate,
							Class.forName("uk.gov.courtservice.xhibit.client.order.actions.OrderCreateAction") },
					{ XhibitActions.OrderView,
							Class.forName("uk.gov.courtservice.xhibit.client.order.actions.OrderViewAction") },
					{ XhibitActions.OrderCopy,
							Class.forName("uk.gov.courtservice.xhibit.client.order.actions.OrderCopyAction") },
					{ XhibitActions.AppealResultOrder,
								Class.forName("uk.gov.courtservice.xhibit.client.order.actions.AppealResultOrderAction") },

					{ XhibitActions.MonetaryOrderCreate,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.order.actions.MonetaryOrderCreateAction") },
					{ XhibitActions.MonetaryOrderView,
							Class.forName("uk.gov.courtservice.xhibit.client.order.actions.MonetaryOrderViewAction") },
					{ XhibitActions.MonetaryOrderCopy,
							Class.forName("uk.gov.courtservice.xhibit.client.order.actions.MonetaryOrderCopyAction") },
					{ XhibitActions.MonetaryOrderCopy,
							Class.forName("uk.gov.courtservice.xhibit.client.order.actions.MonetaryOrderCopyAction") },
					{ XhibitActions.MonetaryOrderAcknowledgement, Class.forName(
							"uk.gov.courtservice.xhibit.client.order.actions.MonetaryOrderAcknowledgementAction") },

					{ XhibitActions.D20Create,
							Class.forName("uk.gov.courtservice.xhibit.client.order.actions.D20CreateAction") },
					{ XhibitActions.D20View,
							Class.forName("uk.gov.courtservice.xhibit.client.order.actions.D20ViewAction") },

					// Neil Entwistle - Adding MESSAGING to main menu BEGIN
					{ XhibitActions.Messaging,
							Class.forName("uk.gov.courtservice.xhibit.client.im.actions.IMSenderAction") },
					// Neil Entwistle - Adding MESSAGING to main menu END

					{ XhibitActions.RoleMapping,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.admin.security.RoleMappingAction") },
					{ XhibitActions.PublicDisplayConfigV2,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.publicdisplayconfig.actions.PublicDisplayConfigAction") },
					{ XhibitActions.CrestImport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.admin.crestimport.CrestImportAction") },
					{ XhibitActions.ChamberAndAdvocateDetails,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.admin.referencedata.ChamberAndAdvocateDetailsAction") },
					{ XhibitActions.ChamberAndAdvocateDetailsReadOnly,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.admin.referencedata.ChamberAndAdvocateDetailsReadOnlyAction") },
					{ XhibitActions.CourtCalendar,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.admin.referencedata.CourtCalendarAction") },
					{ XhibitActions.HomeCourtCentreAndCourtroomDetails,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.admin.referencedata.HomeCourtCentreAndCourtroomDetailsAction") },
					{ XhibitActions.JudgeDetails,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.admin.referencedata.JudgeDetailsAction") },
					{ XhibitActions.ProsecutorRespondentDetails,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.admin.referencedata.ProsecutorRespondentDetailsAction") },
					{ XhibitActions.SolicitorFirmDetails, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.admin.referencedata.SolicitorFirmDetailsAction") },

					// William Fardell (Xdevelopment) - Skeleton Schedule
					// Start
					// Please note the use of reflection to remove tight
					// coupling between modules.
					// These actions can be found in thickclient/witness
					{ XhibitActions.EditSkeletonSchedule,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.skeletonschedule.EditSkeletonScheduleAction") },
					{ XhibitActions.EditTrialTimeEstimate,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.skeletonschedule.EditTrialTimeEstimateAction") },
					{ XhibitActions.AddWitness,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.skeletonschedule.AddWitnessAction") },
					{ XhibitActions.EditWitness,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.skeletonschedule.EditWitnessAction") },
					{ XhibitActions.ViewPreviousWeek,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.skeletonschedule.ViewPreviousWeekAction") },
					{ XhibitActions.ViewNextWeek,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.skeletonschedule.ViewNextWeekAction") },
					{ XhibitActions.DeleteSchedule,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.skeletonschedule.DeleteScheduleAction") },
					{ XhibitActions.IssueSchedule,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.skeletonschedule.IssueScheduleAction") },
					{ XhibitActions.EditNotes,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.skeletonschedule.EditNotesAction") },
					{ XhibitActions.PrintScheduleDay,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.skeletonschedule.PrintScheduleDayAction") },
					{ XhibitActions.PrintScheduleWeek, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.skeletonschedule.PrintScheduleWeekAction") },

					// William Fardell (Xdevelopment) - Skeleton Schedule
					// End

					// William Fardell (Xdevelopment) - Crest Forms B - F
					// Start
					// Please note the use of reflection to remove tight
					// coupling between modules.
					// These actions can be found in
					// thickclient/crestformsbf
					{ XhibitActions.ViewCrestFormsBF,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.crestformsbf.ViewCrestFormsBFAction") },
					// William Fardell (Xdevelopment) - Crest Forms B - F
					// End

					// Import Export Statuses/notification
					{ XhibitActions.ImportExportNotification,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.importexportnotification.ImportExportNotificationAction") },
					{ XhibitActions.UnauthorisedCaseStatus,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.UnauthorisedCase.UnauthorisedCaseStatusAction") },
 
					{ XhibitActions.DOCARReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.RunDOCARReportAction") },
					{ XhibitActions.ADJSSReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayADJSSReportAction") },
					{ XhibitActions.NFIXReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayNFIXReportAction") },
					{ XhibitActions.PRLISReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.PRLISMenuItemAction") },
					{ XhibitActions.OUTCReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.OUTCMenuItemAction") },
					{ XhibitActions.UNLCReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.UNLCMenuItemAction") },
					{ XhibitActions.LFIXReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.LFIXMenuItemAction") },
					{ XhibitActions.LODReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.LODMenuItemAction") },
					{ XhibitActions.DEFSSReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayDEFSSReportAction") },
					{ XhibitActions.NHAReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayNHAReportAction") },
					{ XhibitActions.OBWReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.OBWMenuItemAction") },
					{ XhibitActions.CFIXReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.CFIXMenuItemAction") },
					{ XhibitActions.CTLRPReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.CTLRPMenuItemAction") },
					{ XhibitActions.DRSRReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.DRSRMenuItemAction") },
					{ XhibitActions.RAGEReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.RAGEMenuItemAction") },
					{ XhibitActions.RELCJReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.RELCJMenuItemAction") },
					{ XhibitActions.NTRSFReport,
										Class.forName(
												"uk.gov.courtservice.xhibit.client.actions.results.Reports.NTRSFMenuItemAction") },
					{ XhibitActions.RUMOReport,
							Class.forName(
									"uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayRUMOReportAction") },
					{ XhibitActions.RRECReport,
										Class.forName(
												"uk.gov.courtservice.xhibit.client.actions.results.Reports.RRECMenuItemAction") },
					{ XhibitActions.CourtOfAppeal, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.admin.courtofappeal.CourtOfAppealAction") },
					{ XhibitActions.QueryCompletedCase, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.admin.querycompletedcase.QueryCompletedCaseAction") },
					{ XhibitActions.INFTRPCReport, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.results.Reports.INFTRPCMenuItemAction") },
					{ XhibitActions.RJSReport,
								Class.forName(
										"uk.gov.courtservice.xhibit.client.actions.results.Reports.RJSMenuItemAction") },

					{ XhibitActions.RecoredCourtroomStatistics, Class.forName(
							"uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats.RecordCourtroomStatisticsAction") },

					{ XhibitActions.RRCAReport,
											Class.forName(
													"uk.gov.courtservice.xhibit.client.actions.results.Reports.RRCAMenuItemAction") },
					{ XhibitActions.RSITReport,
											Class.forName(
													"uk.gov.courtservice.xhibit.client.actions.results.Reports.RSITMenuItemAction") },
					{ XhibitActions.DARTSReport,
														Class.forName(
																"uk.gov.courtservice.xhibit.client.actions.results.Reports.DARTSMenuItemAction") },
					};

			contents = tmpContents;
		} catch (ClassNotFoundException e) {
			throw new CSUnrecoverableException("Could not locate action classes for the thickclient.", e);
		}
	}
}