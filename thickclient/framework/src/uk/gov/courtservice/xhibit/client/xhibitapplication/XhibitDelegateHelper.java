package uk.gov.courtservice.xhibit.client.xhibitapplication;

import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.casehistory.CaseHistoryControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caseprosecutoragency.CaseProsecutorAgencyControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.contactdetail.ContactDetailControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.crestformsbf.CrestFormsBFControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.crestimport.CrestImportControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.darts.XhibitDartsControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendantreference.DefendantReferenceControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defoncaserefsolfirm.DefOnCaseRefSolFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.directions.DirectionsControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.legalaidorder.LegalAidOrderControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.listdistribution.ListDistribution2ControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.listdistribution.MaintainRecipientControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.messaging.MessagingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.monetaryordertracking.MonetaryOrderTrackingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.orders.CollectionCentreControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.orders.OrdersControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.orders.OrdersReferenceControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.prosecutorrefsolfirm.ProsecutorRefSolFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.PDConfigurationControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.refsolicitorfirm.RefSolicitorFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.results.Results2ControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.schedhearingdefendant.SchedHearingDefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.shjustice.SHJusticeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ImportExportStatusControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.version.VersionControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.viewschedule.ViewScheduleControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessSelectorBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLog2ControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.rolemapping.services.RoleMappingControllerBeanBusinessDelegate;

/**
 * <p>
 * Title: XHIBIT Client Framework
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: XhibitDelegateHelper.java,v 1.27 2015/12/10 17:25:37 atwells
 *          Exp $
 */
public class XhibitDelegateHelper {
	private XhibitDelegateHelper() {
		// prevent external instantiation...
	}

	/**
	 * Biz Ref Controller
	 * 
	 * @return BisRefControllerBeanBusinessDelegate
	 */
	public static BisRefControllerBeanBusinessDelegate getBizRefDelegate() {
		return BisRefControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Case Controller
	 * 
	 * @return CaseControllerBeanBusinessDelegate
	 */
	public static CaseControllerBeanBusinessDelegate getCaseDelegate() {
		return CaseControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Darts Controller
	 * 
	 * @return DartsControllerBeanBusinessDelegate
	 */
	public static XhibitDartsControllerBeanBusinessDelegate getDartsDelegate() {
		return XhibitDartsControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}
	
	/**
	 * Listings Controller
	 * 
	 * @return ListingsControllerBeanBusinessDelegate
	 */
	public static ListingsControllerBeanBusinessDelegate getListingsDelegate() {
		return ListingsControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Charge Controller
	 * 
	 * @return ChargeControllerBeanBusinessDelegate
	 */
	public static ChargeControllerBeanBusinessDelegate getChargeDelegate() {
		return ChargeControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Counsel Facilities Controller
	 * 
	 * @return CounselFacilitiesControllerBeanBusinessDelegate
	 */
	public static CounselFacilitiesControllerBeanBusinessDelegate getCounselFacilitiesDelegate() {
		return CounselFacilitiesControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Court Log Controller 2
	 * 
	 * @return CourtLog2ControllerBeanBusinessDelegate
	 */
	public static CourtLog2ControllerBeanBusinessDelegate getCourtLogDelegate2() {
		return CourtLog2ControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Crest Import Controller
	 * 
	 * @return CrestImportControllerBeanBusinessDelegate
	 */
	public static CrestImportControllerBeanBusinessDelegate getCrestImportDelegate() {
		return CrestImportControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Defendant Controller
	 * 
	 * @return DefendantControllerBeanBusinessDelegate
	 */
	public static DefendantControllerBeanBusinessDelegate getDefendantDelegate() {
		return DefendantControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * New Public Display Controller
	 * 
	 * @return PDConfigurationControllerBeanBusinessDelegate
	 */
	public static final PDConfigurationControllerBeanBusinessDelegate getDisplayDelegate() {
		return PDConfigurationControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Hearing Schedule Controller
	 * 
	 * @return HearingScheduleControllerBeanBusinessDelegate
	 */
	public static HearingScheduleControllerBeanBusinessDelegate getHearingDelegate() {
		return HearingScheduleControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Import Export Status Controller
	 * 
	 * @return ImportExportStatusControllerBeanBusinessDelegate
	 */
	public static ImportExportStatusControllerBeanBusinessDelegate getImportExportDelegate() {
		return ImportExportStatusControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Maintain Recipient Controller
	 * 
	 * @return MaintainRecipientControllerBeanBusinessDelegate
	 */
	public static MaintainRecipientControllerBeanBusinessDelegate getMaintainRecipientDelegate() {
		return MaintainRecipientControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Public Notice Controller
	 * 
	 * @return PublicNoticeControllerBeanBusinessDelegate
	 */
	public static PublicNoticeControllerBeanBusinessDelegate getPublicNoticeDelegate() {
		return PublicNoticeControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Results Controller
	 * 
	 * @return DirectionsControllerBeanBusinessDelegate
	 */
	public static DirectionsControllerBeanBusinessDelegate getDirectionsDelegate() {
		return DirectionsControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Role Mapper Controller
	 * 
	 * @return RoleMappingControllerBeanBusinessDelegate
	 */
	public static RoleMappingControllerBeanBusinessDelegate getRoleMapperDelegate() {
		return RoleMappingControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * User Session Controller
	 * 
	 * @return UserTerminalControllerBeanBusinessDelegate
	 */
	public static UserTerminalControllerBeanBusinessDelegate getUserSessionDelegate() {
		return UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Get the view schedule delegate
	 * 
	 * @return ViewScheduleControllerBeanBusinessDelegate
	 */
	public static ViewScheduleControllerBeanBusinessDelegate getViewScheduleDelegate() {
		return ViewScheduleControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Get the view role mapping delegate
	 * 
	 * @return RoleMappingControllerBeanBusinessDelegate
	 */
	public static RoleMappingControllerBeanBusinessDelegate getRoleMappingDelegate() {
		return RoleMappingControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Get the view user terminal delegate
	 * 
	 * @return UserTerminalControllerBeanBusinessDelegate
	 */
	public static UserTerminalControllerBeanBusinessDelegate getUserTerminalDelegate() {
		return UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Get the results 2 delegate
	 * 
	 * @return Results2ControllerBeanBusinessDelegate
	 */
	public static Results2ControllerBeanBusinessDelegate getResults2Delegate() {
		return Results2ControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Get the results 2 delegate
	 * 
	 * @return Results2ControllerBeanBusinessDelegate
	 */
	public static MessagingControllerBeanBusinessDelegate getMessagingDelegate() {
		return MessagingControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Get the list distribution 2 business delegate
	 * 
	 * @return ListDistribution2ControllerBeanBusinessDelegate
	 */
	public static ListDistribution2ControllerBeanBusinessDelegate getListDistribution2Delegate() {
		return ListDistribution2ControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Return an instance of the pd configuration delegate.
	 * 
	 * @return PDConfigurationControllerBeanBusinessDelegate
	 */
	public static PDConfigurationControllerBeanBusinessDelegate getPDConfigurationDelegate() {
		return PDConfigurationControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Return an instance of the witness delegate.
	 * 
	 * @return WitnessSelectorBeanBusinessDelegate
	 */
	public static WitnessSelectorBeanBusinessDelegate getWitnessSelectorDelegate() {
		return WitnessSelectorBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Return an instance of the version delegate.
	 * 
	 * @return VersionControllerBeanBusinessDelegate
	 */
	public static VersionControllerBeanBusinessDelegate getVersionDelegate() {
		return VersionControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Return an instance of the orders reference delegate.
	 * 
	 * @return OrdersReferenceControllerBeanBusinessDelegate
	 */
	public static OrdersReferenceControllerBeanBusinessDelegate getOrdersReferenceDelegate() {
		return OrdersReferenceControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Return an instance of the orders delegate.
	 * 
	 * @return OrdersControllerBeanBusinessDelegate
	 */
	public static OrdersControllerBeanBusinessDelegate getOrdersDelegate() {
		return OrdersControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Return an instance of the monetary order tracking delegate.
	 * 
	 * @return MonetaryOrderTrackingControllerBeanBusinessDelegate
	 */
	public static MonetaryOrderTrackingControllerBeanBusinessDelegate getMonetaryOrderTrackingDelegate() {
		return MonetaryOrderTrackingControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Return an instance of the crest forms bf delegate
	 * 
	 * @return CrestFormsBFControllerBeanBusinessDelegate
	 */
	public static CrestFormsBFControllerBeanBusinessDelegate getCrestFormsBFDelegate() {
		return CrestFormsBFControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	/**
	 * Return an instance of the collection centre delegate
	 * 
	 * @return CrestFormsBFControllerBeanBusinessDelegate
	 */
	public static CollectionCentreControllerBeanBusinessDelegate getCollectionCentreDelegate() {
		return CollectionCentreControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	public static DefOnCaseRefSolFirmControllerBeanBusinessDelegate getDefOnCaseRefSolFirmDelegate() {
		return DefOnCaseRefSolFirmControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	public static BwHistoryControllerBeanBusinessDelegate getBwHistoryDelegate() {
		return BwHistoryControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	public static RefSolicitorFirmControllerBeanBusinessDelegate getRefSolicitorFirmController() {
		return RefSolicitorFirmControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	public static CaseProsecutorAgencyControllerBeanBusinessDelegate getCaseProsecutorAgencyDelegate() {
		return CaseProsecutorAgencyControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	public static ProsecutorRefSolFirmControllerBeanBusinessDelegate getProsRefSolFirmDelegate() {
		return ProsecutorRefSolFirmControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	public static ContactDetailControllerBeanBusinessDelegate getContactDetailDelegate() {
		return ContactDetailControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	public static DefendantReferenceControllerBeanBusinessDelegate getDefendantReferenceDelegate() {
		return DefendantReferenceControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	public static LegalAidOrderControllerBeanBusinessDelegate getLegalAidDelegate() {
		return LegalAidOrderControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	public static CaseHistoryControllerBeanBusinessDelegate getCaseHistoryDelegate() {
		return CaseHistoryControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	public static SHJusticeControllerBeanBusinessDelegate getSHJusticeDelegate() {
		return SHJusticeControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}

	public static SchedHearingDefendantControllerBeanBusinessDelegate getSchedHearingDefendantDelegate() {
		return SchedHearingDefendantControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}
}
