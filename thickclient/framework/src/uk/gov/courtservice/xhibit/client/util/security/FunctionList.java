package uk.gov.courtservice.xhibit.client.util.security;

import uk.gov.courtservice.framework.client.CSUserSession;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

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
 * @author unascribed
 * @version 1.0
 * @history added VUnauthorisedCaseStatusAction for Unauthorised Cases Functionality
 */

public class FunctionList {
    private String function;

    private FunctionList(String function) {
        this.function = function;
    }

    public String toString() {
        return function;
    }

    public static final FunctionList NA = new FunctionList("Not Applicable");
    public static final FunctionList VSchedule = new FunctionList("XHBViewTodaysSchedule");
    public static final FunctionList VTomorrowSchedule = new FunctionList("XHBViewTomorrowsSchedule");
    public static final FunctionList ELinkCases = new FunctionList("XHBOpenLinkedCases");
    public static final FunctionList EUnlinkCases = new FunctionList("XHBUnlinkCases");
    public static final FunctionList EPublicDisplay = new FunctionList("XHBActivatePublicDisplay");
    public static final FunctionList VCourtLog = new FunctionList("XHBViewCourtLog");
    public static final FunctionList ECourtLog = new FunctionList("XHBEditCourtLog");
    public static final FunctionList ECourtLogOut = new FunctionList("XHBEditCourtLogOutOfCourt");
    public static final FunctionList VCharge = new FunctionList("XHBViewCharges");
    public static final FunctionList ECharge = new FunctionList("XHBEditCharges");
    public static final FunctionList EPlea = new FunctionList("XHBEditPleas");
    public static final FunctionList EVerdict = new FunctionList("XHBEditVerdicts");
    public static final FunctionList EDisposal = new FunctionList("XHBEditDisposals");
    public static final FunctionList VCaseProgress = new FunctionList("XHBViewCaseProgress");
    public static final FunctionList ECaseProgressDetail = new FunctionList("XHBViewDetailedCaseProgress");
    public static final FunctionList EAuthoriseResult = new FunctionList("XHBAuthoriseResults");
    // public static final FunctionList EFormA = new
    // FunctionList("XHBEditFormA");
    public static final FunctionList VPublicDisplay = new FunctionList("XHBViewPublicDisplays");
    public static final FunctionList ENewLinkCases = new FunctionList("XHBManualCaseLink");
    public static final FunctionList ENewUnlinkCases = new FunctionList("XHBManualCaseUnLink");
    public static final FunctionList EMoveCase = new FunctionList("XHBMoveCase");
    // public static final FunctionList EAddBenchWarrant = new
    // FunctionList("XHBAddBenchWarrantHearing");
    public static final FunctionList EAddHearing = new FunctionList("XHBAddHearing");
    public static final FunctionList VCounselSignin = new FunctionList("XHBViewCounselSignin");
    public static final FunctionList ECounselSignin = new FunctionList("XHBEditCounselSignin");
    public static final FunctionList EDefendant = new FunctionList("XHBEditDefendant");
    public static final FunctionList VUsers = new FunctionList("XHBViewUsers");
    public static final FunctionList EUsers = new FunctionList("XHBEditUsers");
    public static final FunctionList VRoles = new FunctionList("XHBViewRoles");
    public static final FunctionList ERoles = new FunctionList("XHBEditRoles");
    public static final FunctionList EManagePublicDisplay = new FunctionList("XHBManagePublicDisplays");
    public static final FunctionList VViewCase = new FunctionList("XHBViewCase");
    public static final FunctionList VSystemAdmin = new FunctionList("XHBSystemAdmin");

    // public static final FunctionList VCrestForms = new
    // FunctionList("XHBViewCrestForms");
    // public static final FunctionList ECrestForms = new
    // FunctionList("XHBViewCrestForms");
    public static final FunctionList VHearingRecord = new FunctionList("XHBUpdateHearingRecord");
    public static final FunctionList EHearingRecord = new FunctionList("XHBUpdateHearingRecord");
    public static final FunctionList EExportHearingRecord = new FunctionList("XHBExportHearingRecord");
    public static final FunctionList ELinkHearings = new FunctionList("XHBLinkHearings");
    public static final FunctionList VReferenceData = new FunctionList("XHBViewReferenceData");
    public static final FunctionList VCourtStaff = new FunctionList("XHBViewCourtStaff");
    public static final FunctionList ECourtStaff = new FunctionList("XHBEditCourtStaff");
    public static final FunctionList VCaseProperty = new FunctionList("XHBViewCaseProperty");
    public static final FunctionList ECaseProperty = new FunctionList("XHBEditCaseProperty");
    public static final FunctionList VCourtInformation = new FunctionList("XHBViewCourtInformation");
    // public static final FunctionList VSearchForCase = new
    // FunctionList("XHBSearchForCase");
    public static final FunctionList ESearchForCase = new FunctionList("XHBSearchForCase");
    public static final FunctionList VListCase = new FunctionList("XHBViewListStatus");
    public static final FunctionList EListCase = new FunctionList("XHBUpdateListStatus");
    public static final FunctionList VRecipient = new FunctionList("XHBViewRecipient");
    public static final FunctionList ERecipient = new FunctionList("XHBEditRecipient");
    public static final FunctionList VWLLRecipient = new FunctionList("XHBViewWllRecipient");
    public static final FunctionList EWLLRecipient = new FunctionList("XHBEditWllRecipient");
    // public static final FunctionList VRefSolicitorFirm = new
    // FunctionList("XHBViewRefSolicitorFirm");
    public static final FunctionList ERefSolicitorFirm = new FunctionList("XHBViewRefSolicitorFirm");
    public static final FunctionList EUpdateWLLStatus = new FunctionList("XHBUpdateWLLStatus");
    public static final FunctionList VViewList = new FunctionList("XHBViewList");
    public static final FunctionList VSecurityAdmin = new FunctionList("XHBSecurityAdmin");
    // public static final FunctionList VPublicDisplayAdmin = new
    // FunctionList("XHBPublicDisplayAdmin");
    public static final FunctionList EPublicDisplayAdmin = new FunctionList("XHBPublicDisplayAdmin");
    // public static final FunctionList VCrestImport = new
    // FunctionList("XHBCrestImport");
    public static final FunctionList ECrestImport = new FunctionList("XHBCrestImport");
    public static final FunctionList ECrestLogin = new FunctionList("XHBLogin");
    public static final FunctionList ESynchronizeTerminals = new FunctionList("XHBSynchronizeTerminals");

    // Orders etc
    public static final FunctionList ECreateOrder = new FunctionList("XHBOrdersCreateOrder");
    public static final FunctionList VViewOrder = new FunctionList("XHBOrdersViewOrder");
    public static final FunctionList ECopyOrder = new FunctionList("XHBOrdersCopyOrder");
	public static final FunctionList EAppealResultOrder = new FunctionList("XHBAppealResultOrder");;
    public static final FunctionList ESignOrder = new FunctionList("XHBOrdersSignOrder");
    public static final FunctionList EMonetaryCreateOrder = new FunctionList("XHBMonetaryOrdersCreateOrder");
    public static final FunctionList VMonetaryViewOrder = new FunctionList("XHBMonetaryOrdersViewOrder");
    public static final FunctionList EMonetaryCopyOrder = new FunctionList("XHBMonetaryOrdersCopyOrder");
    public static final FunctionList ESendOrder = new FunctionList("XHBOrdersSendOrder");
    public static final FunctionList ED20CreateOrder = new FunctionList("XHBD20CreateOrder");
    public static final FunctionList VD20ViewOrder = new FunctionList("XHBD20ViewOrder");
    public static final FunctionList ED20CopyOrder = new FunctionList("XHBD20CopyOrder");
    public static final FunctionList EIMSendMessage = new FunctionList("XHBIMSendMessage");
    public static final FunctionList EMonetaryOrderAcknowledgement = new FunctionList("XHBMonetaryOrderAcknowledgement");
    
    //Create case
    public static final FunctionList ECreateCase = new FunctionList("XHBCreateCase");
    public static final FunctionList EMaintainCase = new FunctionList("XHBMaintainCase");
    public static final FunctionList EDeleteCase = new FunctionList("XHBDeleteCase");
    public static final FunctionList RDDeft = new FunctionList("XHBRemoveDeft");
    public static final FunctionList AddIndictmentCase = new FunctionList("XHBAddIndictment");
    
    public static final FunctionList EQueryCompletedCase = new FunctionList("XHBQACAS");
    
    // Reference data
    public static final FunctionList EReferenceData = new FunctionList("XHBMaintainReferenceData");
    public static final FunctionList EChamberData = new FunctionList("XHBMaintainChamberData");

    // Skeleton Schedule Start
    public static FunctionList ETrialTimeEstimate = new FunctionList("XHBEditTrialTimeEstimate");
    public static FunctionList ESkeletonSchedule = new FunctionList("XHBEditSkeletonSchedule");
    public static FunctionList AWitness = new FunctionList("XHBAddWitness");
    public static FunctionList EWitness = new FunctionList("XHBEditWitness");
    public static FunctionList VPreviousWeek = new FunctionList("XHBViewPreviousWeek");
    public static FunctionList VNextWeek = new FunctionList("XHBViewNextWeek");
    
    // Listing
    public static final FunctionList ECreateList = new FunctionList("XHBCreateList");
    public static final FunctionList EMaintainList = new FunctionList("XHBMaintainList");

    // Skeleton Schedule End

    // Crest Forms B - F Start
    public static FunctionList VCrestFormsBF = new FunctionList("XHBViewCrestForms");
    // Crest Forms B - F End

    // ImportExportNotification
    public static FunctionList VImportExportNotification = new FunctionList("XHBViewImportExportStatuses");
    public static FunctionList VUnauthorisedCaseStatusAction = new FunctionList("XHBViewUnauthorisedCaseStatus");
    public static FunctionList VCourtOfAppealAction = new FunctionList("XHBCourtOfAppeal");
    public static final FunctionList VRunDOCARReport = new FunctionList("XHBReportsDOCAR");
    public static final FunctionList VRunNFIXReport = new FunctionList("XHBReportsNFIX");
    public static final FunctionList VRunADJSSReport = new FunctionList("XHBReportsADJSS");
    public static final FunctionList VRunOUTCReport = new FunctionList("XHBReportsOUTC");
    public static final FunctionList VRunUNLCReport = new FunctionList("XHBReportsUNLC");
    public static final FunctionList VRunLFIXReport = new FunctionList("XHBReportsLFIX");
    public static final FunctionList VRunPRLISReport = new FunctionList("XHBReportsPRLIS");
    public static final FunctionList VRunDEFSSReport = new FunctionList("XHBReportsDEFSS");
    public static final FunctionList VRunDARTSReport = new FunctionList("XHBReportsDARTS");
    public static final FunctionList VRunNHAReport = new FunctionList("XHBReportsNHA");
    public static final FunctionList VRunLODReport = new FunctionList("XHBReportsLOD");
    public static final FunctionList VRunOBWReport = new FunctionList("XHBReportsOBW");
    public static final FunctionList VRunCTLRPReport = new FunctionList("XHBReportsCTLRP");
    public static final FunctionList VRunCFIXReport = new FunctionList("XHBReportsCFIX");
    public static final FunctionList VRunDRSRReport = new FunctionList("XHBReportsDRSR");
    public static final FunctionList VRunRELCJReport = new FunctionList("XHBReportsRELCJ");
    public static final FunctionList VRunNTRSFReport = new FunctionList("XHBReportsNTRSF");
    public static final FunctionList VRunINFTRPCReport = new FunctionList("XHBReportsINFTRPC");
    public static final FunctionList VRunRJSReport = new FunctionList("XHBReportsRJS");
    public static final FunctionList VRunRAGEReport = new FunctionList("XHBReportsRAGE");
    public static final FunctionList VRunRUMOReport = new FunctionList("XHBReportsRUMO");
    public static final FunctionList VRunRRCAReport = new FunctionList("XHBReportsRRCA");
    public static final FunctionList VRunRRECReport = new FunctionList("XHBReportsRREC");
    public static final FunctionList VRunRSITReport = new FunctionList("XHBReportsRSIT");
    public static final FunctionList VRunRecordedCourtroomStatistics = new FunctionList("XHBRecordedCourtroomStatistics");




    
    // Client side permission only
    public static FunctionList UserCanRoamWithinCourt = new FunctionList("XHBUserCanRoamWithinCourt");
    public static FunctionList UserCanRoamAllCourts = new FunctionList("XHBUserCanRoamAllCourts");

    public static boolean hasAccess(FunctionList access) {
        return access == NA ? true : hasAccess(access.toString());
    }

    public static boolean hasAccess(String access) {
        CSUserSession csu = XhibitSingleton.getInstance().getUserSession();
        return access.equals(NA.toString()) ? true : csu.hasAccess(access);
    }
}
