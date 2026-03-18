package uk.gov.courtservice.xhibit.business.services.systemadmin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.rcs.RCSDatabaseManager;
import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.entities.address.AddressHome;
import uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoom;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomHome;
import uk.gov.courtservice.xhibit.business.entities.courtroomusage.CourtRoomUsageMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtsatellite.CourtSatellite;
import uk.gov.courtservice.xhibit.business.entities.courtsatellite.CourtSatelliteHome;
import uk.gov.courtservice.xhibit.business.entities.courtsatellite.CourtSatelliteMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSite;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSiteHome;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSiteMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReferenceMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.judgeusage.JudgeUsageMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntryMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.SittingOnListMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocate;
import uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocateMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refappresd20map.RefAppResD20MapMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refappresult.RefAppResult;
import uk.gov.courtservice.xhibit.business.entities.refappresult.RefAppResultMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refcalendar.RefCalendar;
import uk.gov.courtservice.xhibit.business.entities.refcalendar.RefCalendarMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamber;
import uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamberMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudge;
import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudgeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refjudgeticket.RefJudgeTicketMaintainer;
import uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentative;
import uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentativeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingData;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingDataMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refprosecutoragency.RefProsecutorAgency;
import uk.gov.courtservice.xhibit.business.entities.refprosecutoragency.RefProsecutorAgencyHome;
import uk.gov.courtservice.xhibit.business.entities.refprosecutoragency.RefProsecutorAgencyMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirmHome;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirmMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetail;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_codes.XhbD20OffenceCodes;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_codes.XhbD20OffenceCodesBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_hate_sentencing_type.XhbRefHateSentencingType;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_mon_ord_disposals.XhbRefMonOrdDisposals;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_monitoring_category.XhbRefMonitoringCategory;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_nationality.XhbRefNationality;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_prosecutor_agency.XhbRefProsecutorAgency;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_prosecutor_agency.XhbRefProsecutorAgencyBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipient;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientBasicValue;
import uk.gov.courtservice.xhibit.business.services.listing.CaseDiaryFixtureHelper;
import uk.gov.courtservice.xhibit.business.services.listing.ListHelper;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsDatabaseManager;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.CourtHelper;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.CourtRoomUsageHelper;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.DisposalHelper;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.HearingHelper;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.JudgeUsageHelper;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.LegalRepresentativeHelper;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.RefCalendarHelper;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.RefCourtHelper;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.SystemCodeHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomUsageComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSatelliteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.JudgeUsageComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResD20MapBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCalendarBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefChamberComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeTicketBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefMonOrdDisposalsValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefMonitoringCategoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefProsecutorAgencyComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtRoomCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtSiteCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAdvocateCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAppResultCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCalendarCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtReporterCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefHearingTypeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJudgeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJusticeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefLegalRepresentativeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefOffenceCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSolicitorFirmCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.SolicitorCriteria;

/**
 * Controller for all Business Reference Data (aka CREST Reference Data or
 * MetaData).
 * 
 * <p>
 * Iteration 2 brings in a new Reference Data search design pattern. Rather than
 * creating a method for each and every search required, we create ONE method
 * for each Reference Data Type (e.g. Hearing Type).
 * </p>
 * <p>
 * The old methods have been marked deprecated were a new version is made
 * available.
 * </p>
 * <p>
 * Introduced the private helper-method 'createQuery'.
 * </p>
 * 
 * @ejb.bean name="BisRefController" description="Bis Ref Session Bean"
 *           type="Stateless" view-type="both" jndi-name="BisRefControllerHome"
 *           local-jndi-name="BisRefControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 *                  <p>
 *                  Copyright (c) 2002, 2003
 *                  </p>
 *                  <p>
 *                  EDS
 *                  </p>
 * @author Faisal Shoukat, Pete Raymond, Khanh Tran
 * @author Jem Marsh
 * @version 1.2
 * @todo The exception throwing needs updating when the framework classes are
 *       updated. Not sure if this is a relevant todo (Jem)
 */
public class BisRefControllerBean extends CSSessionBean implements SessionBean {
	private static final long serialVersionUID = 1L;
	private static uk.gov.courtservice.xhibit.business.entities.xhb_ref_nationality.XhbRefNationalityHome nationalityHome = null;
	private static uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_codes.XhbD20OffenceCodesHome d20OffenceCodesHome = null;
	private static uk.gov.courtservice.xhibit.business.entities.xhb_ref_mon_ord_disposals.XhbRefMonOrdDisposalsHome refMonOrdDisposalsHome = null;
	private static uk.gov.courtservice.xhibit.business.entities.xhb_ref_monitoring_category.XhbRefMonitoringCategoryHome refMonitoringCategoryHome = null;
	private static uk.gov.courtservice.xhibit.business.entities.xhb_ref_prosecutor_agency.XhbRefProsecutorAgencyHome refProsecutorAgencyHome = null;
	private static uk.gov.courtservice.xhibit.business.entities.xhb_ref_hate_sentencing_type.XhbRefHateSentencingTypeHome refHateSentencingType = null;
	private static uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressHome addressHome = null;
	private static uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailHome contactDetailHome = null;
	private static uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientHome wllHome = null;

	private uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReferenceMaintainer dfMaintainer = null;
	private uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer addressMaintainer = null;
	private uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamberMaintainer refChamberMaintainer = null;
	private uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocateMaintainer refAdvocateMaintainer = null;
	private uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentativeMaintainer refLegalRepresentativeMaintainer = null;
	private uk.gov.courtservice.xhibit.business.entities.courtsatellite.CourtSatelliteMaintainer courtSatelliteMaintainer = null;
	private ListingsDatabaseManager listDatabaseManager = new ListingsDatabaseManager();
	private ReferenceDataDatabaseManager referenceDataDatabaseManager = new ReferenceDataDatabaseManager();

	static Class clazz = BisRefControllerBean.class;
	static {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			nationalityHome = (uk.gov.courtservice.xhibit.business.entities.xhb_ref_nationality.XhbRefNationalityHome) ctx
					.lookup(uk.gov.courtservice.xhibit.business.entities.xhb_ref_nationality.XhbRefNationalityHome.JNDI_NAME);
			d20OffenceCodesHome = (uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_codes.XhbD20OffenceCodesHome) ctx
					.lookup(uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_codes.XhbD20OffenceCodesHome.JNDI_NAME);
			refMonOrdDisposalsHome = (uk.gov.courtservice.xhibit.business.entities.xhb_ref_mon_ord_disposals.XhbRefMonOrdDisposalsHome) ctx
					.lookup(uk.gov.courtservice.xhibit.business.entities.xhb_ref_mon_ord_disposals.XhbRefMonOrdDisposalsHome.JNDI_NAME);
			refMonitoringCategoryHome = (uk.gov.courtservice.xhibit.business.entities.xhb_ref_monitoring_category.XhbRefMonitoringCategoryHome) ctx
					.lookup(uk.gov.courtservice.xhibit.business.entities.xhb_ref_monitoring_category.XhbRefMonitoringCategoryHome.JNDI_NAME);
			refHateSentencingType = (uk.gov.courtservice.xhibit.business.entities.xhb_ref_hate_sentencing_type.XhbRefHateSentencingTypeHome) ctx
					.lookup(uk.gov.courtservice.xhibit.business.entities.xhb_ref_hate_sentencing_type.XhbRefHateSentencingTypeHome.JNDI_NAME);
			refProsecutorAgencyHome = (uk.gov.courtservice.xhibit.business.entities.xhb_ref_prosecutor_agency.XhbRefProsecutorAgencyHome) ctx
					.lookup(uk.gov.courtservice.xhibit.business.entities.xhb_ref_prosecutor_agency.XhbRefProsecutorAgencyHome.JNDI_NAME);
			addressHome = (uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressHome) ctx
					.lookup(uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressHome.JNDI_NAME);
			contactDetailHome = (uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailHome) ctx
					.lookup(uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailHome.JNDI_NAME);
			wllHome = (uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientHome) ctx
					.lookup(uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientHome.JNDI_NAME);
		} catch (NamingException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, clazz);
			throw new EJBException(ex);
		} finally {
			try {
				if (ctx != null)
					ctx.close();
			} catch (NamingException ignore) {
				CSServices.getDefaultErrorHandler().handleError(ignore, clazz);
				ignore.printStackTrace();
			}
		}
	}

	private DisposalHelper disposalHelper = null;
	private HearingHelper hearingHelper = null;
	private LegalRepresentativeHelper legalRepresentativeHelper = null;
	private RefCourtHelper refCourtHelper = null;
	private RefCalendarHelper refCalendarHelper = null;
	private SystemCodeHelper systemCodeHelper = null;

	// From Old Sys ref bean
	private static final String METHOD_ENTER = "Entered: SysRefController.";
	private static final String METHOD_EXIT = "Exited: SysRefController.";
	private CourtHelper courtHelper = null;

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @return Collection of Nationalities
	 * @throws SysRefControllerException
	 */
	public Collection findAllNationalities() throws SysRefControllerException {
		final String METHOD_NAME = "findAllNationalities";
		log.debug(METHOD_ENTER + METHOD_NAME);

		Collection<String> data = new LinkedList<String>();
		try {
			// Create reference nationality collection

			Iterator itr = nationalityHome.findAllNationalities().iterator();
			while (itr.hasNext()) {
				XhbRefNationality tempNationality = (XhbRefNationality) itr.next();
				String temp = tempNationality.getRefNationalityCode() + ": " + tempNationality.getRefCountry();
				data.add(temp);
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @return Collection of Nationalities
	 * @throws SysRefControllerException
	 */
	public Collection findAllCountries() throws SysRefControllerException {
		final String METHOD_NAME = "findAllCountries";
		log.debug(METHOD_ENTER + METHOD_NAME);

		Collection<String> data = new LinkedList<String>();
		try {
			// Create reference nationality collection

			Iterator itr = nationalityHome.findAllNationalities().iterator();
			while (itr.hasNext()) {
				XhbRefNationality tempNationality = (XhbRefNationality) itr.next();
				String temp = tempNationality.getRefCountry();
				data.add(temp);
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @return Collection of Monitoring category names
	 * @throws SysRefControllerException
	 */
	public Collection findAllMonitoringCategories() throws SysRefControllerException {
		final String METHOD_NAME = "findAllMonitoringCategories";
		log.debug(METHOD_ENTER + METHOD_NAME);

		Collection<RefMonitoringCategoryBasicValue> data = new ArrayList<RefMonitoringCategoryBasicValue>();
		try {
			Iterator itr = refMonitoringCategoryHome.findAll().iterator();
			while (itr.hasNext()) {
				XhbRefMonitoringCategory tempMon = (XhbRefMonitoringCategory) itr.next();
				RefMonitoringCategoryBasicValue val = new RefMonitoringCategoryBasicValue();
				val.setRefMonitoringCategoryId(tempMon.getRefMonitoringCategoryId());
				val.setMonitoringCategoryName(tempMon.getMonitoringCategoryName());
				val.setMonitoringCategoryCode(tempMon.getMonitoringCategoryCode());
				data.add(val);
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/**
	 * Returns a RefProsecutorAgencyComplexValue
	 * 
	 * @param RefProsecutorAgencyComplexValue
	 *            refProsecutor
	 * @param Integer
	 *            addressId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public RefProsecutorAgencyComplexValue createProsecutorAgency(RefProsecutorAgencyComplexValue refProsecutorAgency, AddressBasicValue addressValue, String userDisplayName)
			throws SysRefControllerException {
		
		final String METHOD_NAME = "createProsecutorAgency";
		log.debug(METHOD_ENTER + METHOD_NAME);
		
		try {
			Address address = createAddress(addressValue,userDisplayName);
			refProsecutorAgency.setAddressId(address.getAddressId());

			if (refProsecutorAgency.getTelephoneNumber()!=null && !refProsecutorAgency.getTelephoneNumber().equals("")) {
				createTelephoneContact(refProsecutorAgency.getTelephoneNumber(), address.getAddressId(), userDisplayName);
			}
			if (refProsecutorAgency.getFaxNumber()!=null && !(refProsecutorAgency.getFaxNumber().equals(""))) {
				createFaxContact(refProsecutorAgency.getFaxNumber(), address.getAddressId(), userDisplayName);
			}
			if (refProsecutorAgency.getSecureEmailAddress()!=null && !(refProsecutorAgency.getSecureEmailAddress().equals(""))) {
				createSecureEmailContact(refProsecutorAgency.getSecureEmailAddress(), address.getAddressId(), userDisplayName);
			}
			if (refProsecutorAgency.getNonsecureEmailAddress()!=null && !(refProsecutorAgency.getNonsecureEmailAddress().equals(""))) {
				createEmailContact(refProsecutorAgency.getNonsecureEmailAddress(), address.getAddressId(), userDisplayName);
			}
						
			XhbRefProsecutorAgencyBasicValue refProsecutorAgencyBasicValue = new XhbRefProsecutorAgencyBasicValue();
			if (refProsecutorAgency.getInitials() != null) {
				refProsecutorAgencyBasicValue.setInitials(refProsecutorAgency.getInitials());
			}
			if (refProsecutorAgency.getTitle() != null) {
				refProsecutorAgencyBasicValue.setTitle(refProsecutorAgency.getTitle());
			}
			if (refProsecutorAgency.getProsecutorName1() != null) {
				refProsecutorAgencyBasicValue.setProsecutorName1(refProsecutorAgency.getProsecutorName1());
			}
			if (refProsecutorAgency.getProsecutorName2() != null) {
				refProsecutorAgencyBasicValue.setProsecutorName2(refProsecutorAgency.getProsecutorName2());
			}
			if (refProsecutorAgency.getProsecutorName3() != null) {
				refProsecutorAgencyBasicValue.setProsecutorName3(refProsecutorAgency.getProsecutorName3());
			}
			if (refProsecutorAgency.getCpsCode() != null) {
				refProsecutorAgencyBasicValue.setCpsCode(refProsecutorAgency.getCpsCode());
			}
			if (address.getAddressId() != null) {
				refProsecutorAgencyBasicValue.setAddressId(address.getAddressId());
			}
			if (refProsecutorAgency.getDxRef() != null) {
				refProsecutorAgencyBasicValue.setDxRef(refProsecutorAgency.getDxRef());
			}
			if (refProsecutorAgency.getCourtId() != null) {
				refProsecutorAgencyBasicValue.setCourtId(refProsecutorAgency.getCourtId());
			}
			refProsecutorAgencyBasicValue.setObsInd("N");
			refProsecutorAgencyBasicValue.setLastUpdatedBy(userDisplayName);
			refProsecutorAgencyBasicValue.setCreatedBy(userDisplayName);
			// Placeholder
			refProsecutorAgencyBasicValue.setCrestOpposerId("000000");
			XhbRefProsecutorAgency newRefProsecutorAgency = refProsecutorAgencyHome
					.create(refProsecutorAgencyBasicValue);
			Integer newRefProsecutorAgencyId = newRefProsecutorAgency.getRefProsecutorAgencyId();
			// Set CREST_OPPOSER_ID the same value as the primary key
			newRefProsecutorAgency.setCrestOpposerId(Integer.toString(newRefProsecutorAgencyId));
			
			RefProsecutorAgencyMaintainer maintainer = new RefProsecutorAgencyMaintainer();
			
			RefProsecutorAgencyComplexValue val = maintainer.returnComplexValue(newRefProsecutorAgency);
			val.setFaxNumber(refProsecutorAgency.getFaxNumber());
			val.setTelephoneNumber(refProsecutorAgency.getTelephoneNumber());
			val.setSecureEmailAddress(refProsecutorAgency.getSecureEmailAddress());
			val.setNonsecureEmailAddress(refProsecutorAgency.getNonsecureEmailAddress());
			if (address.getAddressId() != null) {
				val.setAddress(getAddressMaintainer().getAddressBasicValue(address));
			}
			log.debug(METHOD_EXIT + METHOD_NAME);
			return val;
		} catch (CreateException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} catch(EJBException e) {
			ctx.setRollbackOnly();
			throw e;
		}
	}

	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @return RefProsecutorAgencyComplexValue
	 */
	public RefProsecutorAgencyComplexValue findByRefProsecutorAgencyId(Integer refProsecutorAgencyId) {
		final String METHOD_NAME = "findByRefProsecutorAgencyId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		
		RefProsecutorAgencyComplexValue refProsecutorAgencyComplexValue = null;
		RefProsecutorAgencyMaintainer maintainer= new RefProsecutorAgencyMaintainer();
		try {
			RefProsecutorAgencyHome refHome = (RefProsecutorAgencyHome) CSServices.getServiceLocator()
					.getLocalHome(RefProsecutorAgencyHome.class);
			if (refProsecutorAgencyId != null) {
				RefProsecutorAgency RefProsecutorAgency = refHome.findByPrimaryKey(refProsecutorAgencyId);
				refProsecutorAgencyComplexValue = maintainer.getComplexValue(RefProsecutorAgency);
				refProsecutorAgencyComplexValue.setFullName();
				
				// --- Get address for prosecutor agency ---
				XhbAddress refProsecutorAgencyAddress = addressHome
						.findByPrimaryKey(RefProsecutorAgency.getAddressId());
				maintainer.loadAddress(refProsecutorAgencyComplexValue, refProsecutorAgencyAddress);

				// --- Get contact detail for prosecutor agency ---
				Collection contactDetails= contactDetailHome.findByAddressId(RefProsecutorAgency.getAddressId());
				maintainer.loadContact(refProsecutorAgencyComplexValue, contactDetails);
				
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return refProsecutorAgencyComplexValue;
	}

	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @return Collection of Contact Details
	 * @throws SysRefControllerException
	 */
	public Collection findContactsByAddressId(Integer addressId) {
		final String METHOD_NAME = "findContactsByAddressId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		
		ArrayList<XhbContactDetailBasicValue> contactDetailBasicValues = new ArrayList<XhbContactDetailBasicValue>();
		try {
			// --- Get contact details
			Collection contactDetails;
			contactDetails = contactDetailHome.findByAddressId(addressId);

			Iterator iter = contactDetails.iterator();
			while (iter.hasNext()) {
				XhbContactDetail contactDetail = (XhbContactDetail) iter.next();
				XhbContactDetailBasicValue val = getXhbContactDetailBasicValue(contactDetail);
				contactDetailBasicValues.add(val);
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return contactDetailBasicValues;
	}

	/**
	 * This method populates XhbContactDetailBasicValue
	 * 
	 * @param contactDetail
	 *            the current XhbContactDetail contactDetail entry
	 * @return XhbContactDetailBasicValue a composite value object
	 */
	private XhbContactDetailBasicValue getXhbContactDetailBasicValue(XhbContactDetail contactDetail) {
		final String METHOD_NAME = "getXhbContactDetailBasicValue";
		log.debug(METHOD_ENTER + METHOD_NAME);
		XhbContactDetailBasicValue xhbContactDetailBasicValue = new XhbContactDetailBasicValue(
				contactDetail.getContactId(), contactDetail.getContactType(), contactDetail.getContactValue(),
				contactDetail.getEmailFormat(), contactDetail.getPagerNet(), contactDetail.getAddressId(),
				contactDetail.getLastUpdateDate(), contactDetail.getCreationDate(), contactDetail.getCreatedBy(),
				contactDetail.getLastUpdatedBy(), contactDetail.getVersion());
		log.debug(METHOD_EXIT + METHOD_NAME);
		return xhbContactDetailBasicValue;
	}

	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @return Nothing
	 */
	public void updateRefProsecutorAgency(RefProsecutorAgencyComplexValue refProsecutor, String userDisplayName) {
		final String METHOD_NAME = "updateRefProsecutorAgency";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			RefProsecutorAgencyHome refHome = (RefProsecutorAgencyHome) CSServices.getServiceLocator()
					.getLocalHome(RefProsecutorAgencyHome.class);
			AddressHome adHome = (AddressHome) CSServices.getServiceLocator().getLocalHome(AddressHome.class);
			
			updateContactDetails(refProsecutor.getAddressId(), userDisplayName, refProsecutor.getTelephoneNumber(), 
					refProsecutor.getFaxNumber(), refProsecutor.getSecureEmailAddress(), refProsecutor.getNonsecureEmailAddress());
			
			RefProsecutorAgency ref = refHome.findByRefProsecutorAgencyId(refProsecutor.getRefProsecutorAgencyId());			
			if (ref.getVersion() == null || refProsecutor.getVersion() == null || !ref.getVersion().equals(refProsecutor.getVersion())) {
				log.debug("Optimistic Lock Error: Entity: " + ref.getVersion() + "Requested version: "
						+ refProsecutor.getVersion());
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			ref.setTitle(refProsecutor.getTitle());
			ref.setInitials(refProsecutor.getInitials());
			ref.setProsecutorName1(refProsecutor.getProsecutorName1());
			ref.setProsecutorName2(refProsecutor.getProsecutorName2());
			ref.setProsecutorName3(refProsecutor.getProsecutorName3());
			ref.setCpsCode(refProsecutor.getCpsCode());
			ref.setDxRef(refProsecutor.getDxRef());
			ref.setUpdated(userDisplayName);

			// --- Update address for prosecutor agency ---
			getAddressMaintainer().update(refProsecutor.getAddress(), userDisplayName);
			
			//update wll if needs be
			XhbWllRecipientBasicValue bv = getWllRecipient(refProsecutor);
			ArrayList<XhbContactDetailBasicValue> contactDetails = (ArrayList<XhbContactDetailBasicValue>) 
																		findContactsByAddressId(refProsecutor.getAddressId());
			
			Iterator<XhbContactDetailBasicValue> it = contactDetails.iterator();
			boolean emailFound = false;
			String email = "";
			String fax = "";
			while(it.hasNext()) {
				XhbContactDetailBasicValue contact = (XhbContactDetailBasicValue) it.next();
				if (contact.getContactType().equals("Fax")) {
					fax = contact.getContactValue();
				} else if (contact.getContactType().equals("Secure Email") && contact.getContactValue() != null && !contact.getContactValue().equals("")) {
					emailFound = true;
					email = contact.getContactValue();
				} else if (contact.getContactType().equals("Non Secure Email") && !emailFound) {
					email = contact.getContactValue();
				}
			}
			if(bv != null) {
				try {
					XhbWllRecipient wllRecipient = wllHome.findByPrimaryKey(bv.getWllRecipientId());
		            WllRecipientHelper wllHelper = new WllRecipientHelper();
		            wllHelper.updateWLLRecipient(ref,bv, email, fax, userDisplayName, wllRecipient);
				} catch(ObjectNotFoundException e) {
					log.debug("No WLL Recipient for "+bv.getWllRecipientId());
				}
			}
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} catch (CreateException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} catch(OptimisticLockException e){
			ctx.setRollbackOnly();
			throw e;
		} catch (IllegalArgumentException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(EJBException e) {
			ctx.setRollbackOnly();
			throw e;
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	private void updateContactDetails(Integer addressId, String userDisplayName, String telephoneNumber, String faxNumber, String secureEmail, String nonSecureEmail) throws CreateException, FinderException {
		final String METHOD_NAME = "updateContactDetails";
		log.debug(METHOD_ENTER + METHOD_NAME);
		
		Boolean telephoneContactExists = false;
		Boolean faxContactExists = false;
		Boolean secureEmailContactExists = false;
		Boolean nonSecureEmailContactExists = false;

		Collection contactDetails = contactDetailHome.findByAddressId(addressId);
		for (XhbContactDetail contactDetail : (ArrayList<XhbContactDetail>) contactDetails) {
			if (contactDetail.getContactType().equals("Phone")) {
				telephoneContactExists = true;
				contactDetail.setContactValue(telephoneNumber);
				contactDetail.setLastUpdatedBy(userDisplayName);
			}
			if (contactDetail.getContactType().equals("Fax")) {
				faxContactExists = true;
				contactDetail.setContactValue(faxNumber);
				contactDetail.setLastUpdatedBy(userDisplayName);
			}
			if (contactDetail.getContactType().equals("Secure Email")) {
				secureEmailContactExists = true;
				contactDetail.setContactValue(secureEmail);
				contactDetail.setLastUpdatedBy(userDisplayName);
			}
			if (contactDetail.getContactType().equals("Non Secure Email")) {
				nonSecureEmailContactExists = true;
				contactDetail.setContactValue(nonSecureEmail);
				contactDetail.setLastUpdatedBy(userDisplayName);
			}
		}
		
		if (telephoneNumber!=null && !(telephoneNumber.equals("")) && telephoneContactExists == false) {
			createTelephoneContact(telephoneNumber, addressId, userDisplayName);
		}
		if (faxNumber!=null && !(faxNumber.equals("")) && faxContactExists == false) {
			createFaxContact(faxNumber, addressId, userDisplayName);
		}
		if (secureEmail!=null &&!(secureEmail.equals("")) && secureEmailContactExists == false) {
			createSecureEmailContact(secureEmail,
					addressId, userDisplayName);
		}
		if (nonSecureEmail!=null && !(nonSecureEmail.equals("")) && nonSecureEmailContactExists == false) {
			createEmailContact(nonSecureEmail,
					addressId, userDisplayName);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}
	
	/**
	 * Used to create/update court details
	 * @param addressId
	 * @param userDisplayName
	 * @param telephoneNumber
	 * @param faxNumber
	 * @throws CreateException
	 * @throws SysRefControllerException
	 * @throws FinderException
	 */
	private void updateCourtContactDetails(Integer addressId, String userDisplayName, String telephoneNumber, String faxNumber) throws CreateException, SysRefControllerException, FinderException {
		final String METHOD_NAME = "updateCourtContactDetails";
		log.debug(METHOD_ENTER + METHOD_NAME);
		
		Boolean telephoneContactExists = false;
		Boolean faxContactExists = false;

		Collection contactDetails = contactDetailHome.findByAddressId(addressId);
		for (XhbContactDetail contactDetail : (ArrayList<XhbContactDetail>) contactDetails) {
			if (contactDetail.getContactType().equals("TEL")) {
				telephoneContactExists = true;
				contactDetail.setContactValue(telephoneNumber);
				contactDetail.setLastUpdatedBy(userDisplayName);
			}
			if (contactDetail.getContactType().equals("FAX")) {
				faxContactExists = true;
				contactDetail.setContactValue(faxNumber);
				contactDetail.setLastUpdatedBy(userDisplayName);
			}
		}
		
		if (telephoneNumber!=null && !(telephoneNumber.equals("")) && telephoneContactExists == false) {
			createContactByType(telephoneNumber, addressId, "TEL",userDisplayName);
		}
		if (faxNumber!=null && !(faxNumber.equals("")) && faxContactExists == false) {
			createContactByType(faxNumber, addressId, "FAX",userDisplayName);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}


	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @return Nothing
	 * @throws SysRefControllerException
	 */
	public void deleteRefProsecutorAgency(RefProsecutorAgencyComplexValue refPros, String userDisplayName) throws SysRefControllerException {
		final String METHOD_NAME = "deleteRefProsecutorAgency";
		
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			XhbRefProsecutorAgency xhbRefProsecutorAgency = refProsecutorAgencyHome
					.findByRefProsecutorAgencyId(refPros.getRefProsecutorAgencyId());
			xhbRefProsecutorAgency.setObsInd("Y");
			xhbRefProsecutorAgency.setLastUpdatedBy(userDisplayName);
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @return Collection of ProsecutorAgencies
	 * @throws SysRefControllerException
	 */
	public Collection findByCourtIdProsecutorNameAndCpsCode(Integer courtId, String prosecutorName3, String cpsCode)
			throws SysRefControllerException {
		final String METHOD_NAME = "findByCourtIdProsecutorNameAndCpsCode";
		log.debug(METHOD_ENTER + METHOD_NAME);
		RefProsecutorAgencyMaintainer maintainer= new RefProsecutorAgencyMaintainer();


		Collection<RefProsecutorAgencyComplexValue> refProsecutorAgencyList = new LinkedList<RefProsecutorAgencyComplexValue>();
		try {
			Iterator itr;
			if (cpsCode.equals("%")) {
				itr = refProsecutorAgencyHome.findByCourtIdAndProsecutorName(courtId, prosecutorName3).iterator();
			} else {
				if (prosecutorName3.equals("%")) {
					itr = refProsecutorAgencyHome.findByCourtIdAndCpsCode(courtId, cpsCode).iterator();
				} else {
					itr = refProsecutorAgencyHome
							.findByCourtIdProsecutorNameAndCpsCode(courtId, prosecutorName3, cpsCode).iterator();
				}
			}
			while (itr.hasNext()) {
				XhbRefProsecutorAgency xhbRefProsecutorAgency = (XhbRefProsecutorAgency) itr.next();
				RefProsecutorAgencyComplexValue refProsecutorAgencyComplexValue = new RefProsecutorAgencyComplexValue();
				
				refProsecutorAgencyComplexValue = maintainer.returnComplexValue(xhbRefProsecutorAgency);
				refProsecutorAgencyComplexValue.setFullName();
				
				// --- Get address for prosecutor agency ---
				XhbAddress refProsecutorAgencyAddress = addressHome
						.findByPrimaryKey(xhbRefProsecutorAgency.getAddressId());
				maintainer.loadAddress(refProsecutorAgencyComplexValue, refProsecutorAgencyAddress);

				// --- Get contact detail for prosecutor agency ---
				Collection contactDetails= contactDetailHome.findByAddressId(xhbRefProsecutorAgency.getAddressId());
				maintainer.loadContact(refProsecutorAgencyComplexValue, contactDetails);
				
				// --- Add Prosecutor Agency to list ---
				refProsecutorAgencyList.add(refProsecutorAgencyComplexValue);
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return refProsecutorAgencyList;
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @return Collection of hate sentence types
	 * @throws SysRefControllerException
	 */
	public Collection findAllHateSentencingTypes(Integer courtId) throws SysRefControllerException {
		final String METHOD_NAME = "findAllHateSentencingTypes";
		log.debug(METHOD_ENTER + METHOD_NAME);

		Collection<String> data = new LinkedList<String>();
		try {
			Iterator itr = refHateSentencingType.findByCourtId(courtId).iterator();
			while (itr.hasNext()) {
				XhbRefHateSentencingType tempSent = (XhbRefHateSentencingType) itr.next();
				data.add(tempSent.getTitle());
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param criteria
	 * @return
	 * @throws SysRefControllerException
	 */
	public Collection findCourts(CourtCriteria criteria) throws SysRefControllerException {
		final String METHOD_NAME = "findCourts";
		log.debug(METHOD_ENTER + METHOD_NAME);
		Collection data = this.getCourtHelper().findCourts(criteria);
		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param Integer
	 * @return
	 * @throws SysRefControllerException
	 */
	public RefCourtBasicValue findCourtByRefId(Integer refCourtId) throws SysRefControllerException {
		final String METHOD_NAME = "findCourtByRefId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		RefCourtBasicValue data = null;
		try {
			data = this.getRefCourtHelper().findCourtByRefId(refCourtId);
		} catch (BisRefControllerException e) {
			throw new EJBException(e);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param criteria
	 * @return
	 * @throws SysRefControllerException
	 */
	public Collection findCalendarDays(RefCalendarCriteria criteria) throws SysRefControllerException {
		final String METHOD_NAME = "findCalendarDays";
		log.debug(METHOD_ENTER + METHOD_NAME);
		Collection data = this.getRefCalendarHelper().findCalendarDays(criteria);
		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/**
	 * 
	 * @ejb.interface-method view-type="remote"
	 * 
	 * @param criteria
	 * @return
	 * @throws SysRefControllerException
	 */
	public Collection findCourtSites(CourtSiteCriteria criteria) throws SysRefControllerException {
		final String METHOD_NAME = "findCourtSites";
		log.debug(METHOD_ENTER + METHOD_NAME);
		Collection data = this.getCourtHelper().findCourtSites(criteria);
		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}
	
	
	/**
	 * 
	 * @ejb.interface-method view-type="remote"
	 * 
	 * @param criteria
	 * @return
	 */
	public Collection findHomeCourtAndSatellites(Integer courtId) {
		final String METHOD_NAME = "findHomeCourtAndSatellites";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			CourtSiteMaintainer courtSiteMaintainer = new CourtSiteMaintainer();
			Collection data = courtSiteMaintainer.findHomeCourtAndSatellite(courtId);
			log.debug(METHOD_EXIT + METHOD_NAME);
			return data;
		} catch (Exception e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
	}
	
	/**
	 * Finds all non-obsolete Court Sites with associated Court Rooms and Court
	 * Satellite data.
	 * 
	 * @ejb.interface-method view-type="remote"
	 * 
	 * @param courtId 
	 * @return
	 * @throws SysRefControllerException
	 */
	public Collection findAllCourtSitesByComplex(Integer courtId) throws SysRefControllerException {
		final String METHOD_NAME = "findAllCourtSitesByComplex";
		log.debug(METHOD_ENTER + METHOD_NAME);
		CourtSiteMaintainer courtSiteMaintainer = new CourtSiteMaintainer();
		
		List<CourtSiteComplexValue> courtSitesComplex = new ArrayList<CourtSiteComplexValue>();
		
		try {
			Collection values = courtSiteMaintainer.findAllCourtSites(courtId);
			if (values != null && values.size() > 0) {
				for (CourtSiteBasicValue val :(List<CourtSiteBasicValue>) values) {
					CourtSite local = courtSiteMaintainer.findByPrimaryKey(val.getId());
					CourtSiteComplexValue complexVal = courtSiteMaintainer.getCourtSiteComplexValue(local);
					courtSitesComplex.add(complexVal);
				}
			}
			log.debug(METHOD_EXIT + METHOD_NAME);
			return courtSitesComplex;
		} catch (Exception e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param criteria
	 * @return
	 * @throws SysRefControllerException
	 */
	public Collection findCourtRooms(CourtRoomCriteria criteria) throws SysRefControllerException {
		final String METHOD_NAME = "findCourtRooms";
		log.debug(METHOD_ENTER + METHOD_NAME);
		Collection data = this.getCourtHelper().findCourtRooms(criteria);
		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	private CourtHelper getCourtHelper() {
		final String METHOD_NAME = "getCourtHelper";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (this.courtHelper == null) {
			log.debug("Lazy initialisation of CourtHelper.");
			this.courtHelper = new CourtHelper();
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return this.courtHelper;
	}

	// End from old sys ref bean

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param criteria
	 * @return
	 * @throws BisRefControllerException
	 */
	public Collection findAdvocates(RefAdvocateCriteria criteria) throws BisRefControllerException {
		final String METHOD_NAME = "findAdvocates";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getLegalRepresentativeHelper().findAdvocates(criteria);
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param criteria
	 * @return
	 * @throws BisRefControllerException
	 */
	public Collection findCourtReporters(RefCourtReporterCriteria criteria) throws BisRefControllerException {
		final String METHOD_NAME = "findCourtReporters";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getRefCourtHelper().findCourtReporters(criteria);
	}

	/**
	 * Find [BisRef] Courts.
	 * <p>
	 * NOT to be confused with [SysRef] Courts!
	 * </p>
	 * 
	 * @ejb.interface-method view-type="remote"
	 * 
	 * @param criteria
	 *            RefCourtCriteria
	 * @return Collection A collection of RefCourtBasicValue
	 * @throws BisRefControllerException
	 */
	public Collection findCourts(RefCourtCriteria criteria) throws BisRefControllerException {
		final String METHOD_NAME = "findCourts";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getRefCourtHelper().findCourts(criteria);
	}

	/**
	 * Find the Mag name from the PSD_CT_CODE and courtId.
	 * 
	 * @ejb.interface-method view-type="remote"
	 * 
	 * @param psdCTCode
	 *            from case history table
	 * @param courtId
	 *            the court Id of logged in user
	 * @return String of court short name
	 */
	public String findByPSDCTCodeAndCourtId(String psdCode, Integer courtId) {
		final String METHOD_NAME = "findByPSDCTCodeAndCourtId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getRefCourtHelper().findByPSDCTCodeAndCourtId(psdCode, courtId);
	}
	
	/**
	 * Find the courts from the is psd flag and courtId.
	 * 
	 * @ejb.interface-method view-type="remote"
	 * 
	 * @param isPsd
	 *            string is psd
	 * @param courtId
	 *            the court Id of logged in user
	 * @return String of court short name
	 */
	public ArrayList<RefCourtBasicValue>findByCourtIdAndIsPSD(String isPsd, Integer courtId) {
		final String METHOD_NAME = "findByPSDCTCodeAndCourtId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getRefCourtHelper().findByCourtIdAndIsPSD(isPsd, courtId);
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param criteria
	 * @return
	 * @throws BisRefControllerException
	 */
	public Collection findHearingTypes(RefHearingTypeCriteria criteria) throws BisRefControllerException {
		final String METHOD_NAME = "findHearingTypes";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getHearingHelper().findHearingTypes(criteria);
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param hearingTypeId
	 * @return
	 * @throws BisRefControllerException
	 */
	public RefHearingTypeBasicValue findHearingTypeById(Integer hearingTypeId) throws BisRefControllerException {
		final String METHOD_NAME = "RefHearingTypeBasicValue";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getHearingHelper().findHearingTypeById(hearingTypeId);
	}

	/**
	 * Find a distinct list of hearing types (category='x')
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param courtId
	 * @return
	 * @throws BisRefControllerException
	 */
	public Collection findHearingTypesByCourtId(Integer courtId) throws BisRefControllerException {
		final String METHOD_NAME = "findHearingTypesByCourtId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getHearingHelper().findHearingTypesByCourtIdAndCategory(courtId, "X");
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findJudges(RefJudgeCriteria criteria) throws BisRefControllerException {
		final String METHOD_NAME = "findJudges";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getLegalRepresentativeHelper().findJudges(criteria);
	}

	/**
	 * Finds all the judges matching the courtId and crestJudgeId.
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public RefJudgeComplexValue findJudgeByCourtIdAndCrestJudgeId(Integer courtId, Integer crestJudgeId) {
		final String METHOD_NAME = "findJudgeByCourtIdAndCrestJudgeId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		ListHelper listHelper = new ListHelper();
		return listHelper.findJudgeByCourtIdAndCrestJudgeId(courtId, crestJudgeId);
	}

	/**
	 * Finds all the judges matching the courtId and statsCode.
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findJudgesByCourtIdAndStatsCode(Integer courtId, String statsCode)
			throws BisRefControllerException {
		final String METHOD_NAME = "findJudgesByCourtIdAndStatsCode";
		log.debug(METHOD_ENTER + METHOD_NAME);
		RefJudgeMaintainer maintainer = new RefJudgeMaintainer();
		try {
			Collection<RefJudge> judgeList = maintainer.findByCourtIdAndStatsCode(courtId, statsCode);
			Collection<RefJudgeBasicValue> judgeBVList = new ArrayList<RefJudgeBasicValue>();
			for (RefJudge judge : judgeList) {
				RefJudgeBasicValue bv = new RefJudgeBasicValue(judge.getRefJudgeId(), judge.getVersion(),
						judge.getCrestJudgeId(), judge.getJudgeType(), judge.getFirstName(), judge.getMiddleName(),
						judge.getSurname(), judge.getFullListTitle1(), judge.getFullListTitle2(),
						judge.getFullListTitle3(), judge.getStatsCode(), judge.getInitials(), judge.getHonours(),
						judge.getJudVers(), judge.getObsInd(), judge.getSourceTable(), judge.getTitle(),
						judge.getCourtId());
				judgeBVList.add(bv);
			}
			log.debug(METHOD_EXIT + METHOD_NAME);
			return judgeBVList;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Find a RefJudge ComplexValue by primary key.
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public RefJudgeComplexValue findJudgeById(Integer refJudgeId) throws BisRefControllerException {
		final String METHOD_NAME = "findJudgeById";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			return this.getLegalRepresentativeHelper().findJudgeByPK(refJudgeId);
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findJustices(RefJusticeCriteria criteria) throws BisRefControllerException {
		final String METHOD_NAME = "findJustices";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getLegalRepresentativeHelper().findJustices(criteria);
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findLegalRepresentatives(RefLegalRepresentativeCriteria criteria)
			throws BisRefControllerException {
		final String METHOD_NAME = "findLegalRepresentatives";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getLegalRepresentativeHelper().findLegalRepresentatives(criteria);
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findOffences(RefOffenceCriteria criteria) throws BisRefControllerException {
		final String METHOD_NAME = "findOffences";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getDisposalHelper().findOffences(criteria);
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findSolicitorFirms(RefSolicitorFirmCriteria criteria) throws BisRefControllerException {
		final String METHOD_NAME = "findSolicitorFirms";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getLegalRepresentativeHelper().findSolicitorFirms(criteria);
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findSolicitors(SolicitorCriteria criteria) throws BisRefControllerException {
		final String METHOD_NAME = "findSolicitors";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getLegalRepresentativeHelper().findSolicitors(criteria);
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findSystemCodes(RefSystemCodeCriteria criteria) throws BisRefControllerException {
		final String METHOD_NAME = "findSystemCodes";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getSystemCodeHelper().findSystemCodes(criteria);
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findRefListingData(String refDataType) throws BisRefControllerException {
		final String METHOD_NAME = "findRefListingData";
		log.debug(METHOD_ENTER + METHOD_NAME);

		Collection<RefListingDataBasicValue> data = new ArrayList<RefListingDataBasicValue>();
		try {
			RefListingDataMaintainer maintainer = new RefListingDataMaintainer();
			Iterator itr = maintainer.findByRefDataType(refDataType).iterator();
			while (itr.hasNext()) {
				RefListingData tempSent = (RefListingData) itr.next();
				RefListingDataBasicValue ref = maintainer.getBasicValue(tempSent);
				data.add(ref);
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/**
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection findAppResults(RefAppResultCriteria criteria) throws BisRefControllerException {
		final String METHOD_NAME = "findAppResults";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getSystemCodeHelper().findAppResults(criteria);
	}

	/**
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public SolicitorBasicValue createSolicitor(SolicitorBasicValue vo, String userDisplayName)
			throws BisRefControllerException {
		final String METHOD_NAME = "createSolicitor";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return this.getLegalRepresentativeHelper().createSolicitor(vo, userDisplayName);
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection getOffenceCodes() {
		final String METHOD_NAME = "getOffenceCodes";
		log.debug(METHOD_ENTER + METHOD_NAME);

		Collection<String> data = new ArrayList<String>();
		try {
			Iterator itr = d20OffenceCodesHome.findAll().iterator();
			while (itr.hasNext()) {
				XhbD20OffenceCodes tempD20OffenceCode = (XhbD20OffenceCodes) itr.next();
				String temp = tempD20OffenceCode.getOffenceCode();
				data.add(temp);
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/**
	 * Get the D20 Offence code with the given offence code name
	 * 
	 * @param	offenceCode				The named offence code
	 * 
	 * @return	an XhbD20OffenceCodes instance if found, null if not
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public XhbD20OffenceCodesBasicValue getNamedOffenceCode( final String offenceCode ){
		XhbD20OffenceCodes offenceCodeInst = null;
		
		if ( getOffenceCodes().contains( offenceCode)){
			try{
				Collection allCodes = d20OffenceCodesHome.findAll();
				Iterator itr = allCodes.iterator();
				
				while (itr.hasNext()&& offenceCodeInst == null ){
					XhbD20OffenceCodes tempD20OffenceCode = (XhbD20OffenceCodes) itr.next();
					if ( tempD20OffenceCode.getOffenceCode().equals( offenceCode )){
						offenceCodeInst = tempD20OffenceCode;
					}
				}
			} catch (FinderException e){
				throw new EJBException(e);
			}
		}
		
		XhbD20OffenceCodesBasicValue foundValue = null;
		
		if ( offenceCodeInst != null ){
			foundValue = new XhbD20OffenceCodesBasicValue();
			
			foundValue.setOffenceCodeId( offenceCodeInst.getOffenceCodeId());
			foundValue.setOffenceCode( offenceCodeInst.getOffenceCode());
			foundValue.setReasonType( offenceCodeInst.getReasonType());
			foundValue.setReason( offenceCodeInst.getReason());
			foundValue.setPenaltyPoints( offenceCodeInst.getPenaltyPoints());
			foundValue.setCreatedBy( offenceCodeInst.getCreatedBy());
			foundValue.setLastUpdatedBy( offenceCodeInst.getLastUpdatedBy());
			foundValue.setCreationDate( offenceCodeInst.getCreationDate());
			foundValue.setLastUpdateDate( offenceCodeInst.getLastUpdateDate());
			foundValue.setObsInd( offenceCodeInst.getObsInd());
			foundValue.setVersion( offenceCodeInst.getVersion());
			foundValue.setMandDisq( offenceCodeInst.getMandDisq());
			foundValue.setMandAlcDrugLevel( offenceCodeInst.getMandAlcDrugLevel());
			foundValue.setMandDTETP( offenceCodeInst.getMandDTETP());
		}
		
		return foundValue;
	}
	
	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection getMonetaryOrderDisposalTypes() {
		final String METHOD_NAME = "getMonetaryOrderDisposalTypes";
		log.debug(METHOD_ENTER + METHOD_NAME);

		Collection<RefMonOrdDisposalsValue> data = new ArrayList<RefMonOrdDisposalsValue>();
		try {
			Iterator itr = refMonOrdDisposalsHome.findAll().iterator();
			while (itr.hasNext()) {
				XhbRefMonOrdDisposals tempRefMonOrdDisposal = (XhbRefMonOrdDisposals) itr.next();
				RefMonOrdDisposalsValue rmodv = new RefMonOrdDisposalsValue();
				String disposalCode = tempRefMonOrdDisposal.getDisposalCode();
				String moType = tempRefMonOrdDisposal.getMoType();
				rmodv.setDisposalCode(disposalCode);
				rmodv.setMOType(moType);
				data.add(rmodv);
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findRefMonOrderByDisposalCode(String disposalCode) {
		final String METHOD_NAME = "findRefMonOrderByDisposalCode";
		log.debug(METHOD_ENTER + METHOD_NAME);

		Collection<RefMonOrdDisposalsValue> data = new ArrayList<RefMonOrdDisposalsValue>();
		try {
			Iterator itr = refMonOrdDisposalsHome.findByDisposalCode(disposalCode).iterator();
			while (itr.hasNext()) {
				XhbRefMonOrdDisposals tempRefMonOrdDisposal = (XhbRefMonOrdDisposals) itr.next();
				RefMonOrdDisposalsValue rmodv = new RefMonOrdDisposalsValue();
				String disCode = tempRefMonOrdDisposal.getDisposalCode();
				String moType = tempRefMonOrdDisposal.getMoType();
				rmodv.setDisposalCode(disCode);
				rmodv.setMOType(moType);
				data.add(rmodv);
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/**
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findRefMonOrderByMOType(String moType) {
		final String METHOD_NAME = "findRefMonOrderByMOType";
		log.debug(METHOD_ENTER + METHOD_NAME);

		Collection<RefMonOrdDisposalsValue> data = new ArrayList<RefMonOrdDisposalsValue>();
		try {
			Iterator itr = refMonOrdDisposalsHome.findByMOType(moType).iterator();
			while (itr.hasNext()) {
				XhbRefMonOrdDisposals tempRefMonOrdDisposal = (XhbRefMonOrdDisposals) itr.next();
				RefMonOrdDisposalsValue rmodv = new RefMonOrdDisposalsValue();
				String disCode = tempRefMonOrdDisposal.getDisposalCode();
				String tempMOType = tempRefMonOrdDisposal.getMoType();
				rmodv.setDisposalCode(disCode);
				rmodv.setMOType(tempMOType);
				data.add(rmodv);
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
		return data;
	}

	/* private helper-methods */

	/**
	 * Provide a Disposal Helper instance.
	 * 
	 * @returns DisposalHelper
	 */
	private DisposalHelper getDisposalHelper() {
		final String METHOD_NAME = "getDisposalHelper";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (this.disposalHelper == null) {
			this.disposalHelper = new DisposalHelper();
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return this.disposalHelper;
	}

	/**
	 * Provide a Hearing Helper instance.
	 * 
	 * @returns DisposalHelper
	 */
	private HearingHelper getHearingHelper() {
		final String METHOD_NAME = "getHearingHelper";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (this.hearingHelper == null) {
			this.hearingHelper = new HearingHelper();
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return this.hearingHelper;
	}

	/**
	 * Provide a Legal Representative Helper instance.
	 * 
	 * @returns LegalRepresentativeHelper
	 */
	private LegalRepresentativeHelper getLegalRepresentativeHelper() {
		final String METHOD_NAME = "getLegalRepresentativeHelper";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (this.legalRepresentativeHelper == null) {
			this.legalRepresentativeHelper = new LegalRepresentativeHelper();
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return this.legalRepresentativeHelper;
	}

	/**
	 * Provide a [Ref] Court Helper instance.
	 * 
	 * @returns RefCourtHelper
	 */
	private RefCourtHelper getRefCourtHelper() {
		final String METHOD_NAME = "getRefCourtHelper";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (this.refCourtHelper == null) {
			this.refCourtHelper = new RefCourtHelper();
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return this.refCourtHelper;
	}

	/**
	 * Provide a [Ref] Calendar Helper instance.
	 * 
	 * @returns RefCalendarHelper
	 */
	private RefCalendarHelper getRefCalendarHelper() {
		final String METHOD_NAME = "getRefCalendarHelper";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (this.refCalendarHelper == null) {
			this.refCalendarHelper = new RefCalendarHelper();
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return this.refCalendarHelper;
	}

	/**
	 * Provide a System Code Helper instance.
	 * 
	 * @returns SystemCodeHelper
	 */
	private SystemCodeHelper getSystemCodeHelper() {
		final String METHOD_NAME = "getSystemCodeHelper";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (this.systemCodeHelper == null) {
			this.systemCodeHelper = new SystemCodeHelper();
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return this.systemCodeHelper;
	}

	/**
	 * Provide a Defendant Reference Maintainer instance.
	 * 
	 * @returns defendantReferenceMaintainer
	 */
	private DefendantReferenceMaintainer getDefendantReferenceMaintainer() {
		final String METHOD_NAME = "getDefendantReferenceMaintainer";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (this.dfMaintainer == null) {
			this.dfMaintainer = new DefendantReferenceMaintainer();
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return this.dfMaintainer;
	}

	/**
	 * Provide a Defendant Reference Maintainer instance.
	 * 
	 * @returns defendantReferenceMaintainer
	 */
	private AddressMaintainer getAddressMaintainer() {
		final String METHOD_NAME = "getAddressMaintainer";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (this.addressMaintainer == null) {
			this.addressMaintainer = new AddressMaintainer();
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return this.addressMaintainer;
	}

	/**
	 * Returns the defendant Reference information for a given defendant
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param Integer
	 *            DefendantId
	 * @param String
	 *            refName
	 * @returns String
	 * @throws ObjectNotFoundException
	 */
	public String findAllByDefendantIdAndReferenceName(Integer defendantId, String refName) {
		final String METHOD_NAME = "findAllByDefendantIdAndReferenceName";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			String s = getDefendantReferenceMaintainer().findAllByDefendantIdAndReferenceName(defendantId, refName);
			log.debug(METHOD_EXIT + METHOD_NAME);
			if (!(s == null)) {
				return s;
			} else {
				return "";
			}
		} catch (ObjectNotFoundException e) {
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return "";

	}

	/**
	 * Returns the defendant Reference information for a given defendant
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param Integer
	 *            DefendantId
	 * @param String
	 *            refName
	 * @returns String
	 * @throws ObjectNotFoundException
	 */
	public Integer findReferenceNameIdByDefendantId(Integer defendantId, String refName) {
		final String METHOD_NAME = "findReferenceNameIdByDefendantId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			Integer s = getDefendantReferenceMaintainer().findReferenceNameIdByDefendantId(defendantId, refName);
			log.debug(METHOD_EXIT + METHOD_NAME);
			if (!(s == null)) {
				return s;
			} else {
				return null;
			}
		} catch (ObjectNotFoundException e) {
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return null;

	}

	/**
	 * Returns the address information for a given Address Id
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param Integer
	 *            pK
	 * @returns AddressBasicValue
	 * @throws ObjectNotFoundException
	 */
	public AddressBasicValue findByPK(Integer pK) {
		final String METHOD_NAME = "findByPK";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			AddressBasicValue abv = new AddressBasicValue();
			abv = getAddressMaintainer().findByPkReturnBasicValue(pK);
			log.debug(METHOD_EXIT + METHOD_NAME);
			return abv;
		} catch (ObjectNotFoundException e) {
		}
		return null;

	}

	/**
	 * Returns a RefChamberComplexValue
	 * 
	 * @param Integer
	 *            refChamberId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public RefChamberComplexValue findChamberByRefChamberId(Integer refChamberId)
			throws SysRefControllerException, CreateException {
		final String METHOD_NAME = "findChamberByRefChamberId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		RefChamberComplexValue refChamberComplexVal = new RefChamberComplexValue();

		try {
			RefChamber refChamber;
			refChamberMaintainer = new RefChamberMaintainer();
			refChamber = refChamberMaintainer.findByPrimaryKey(refChamberId);
			if (refChamber.getAddress() != null) {
				XhbAddress refChamberAddress = addressHome.findByPrimaryKey(refChamber.getAddress().getAddressId());
				Collection contactDetails = contactDetailHome.findByAddressId(refChamber.getAddress().getAddressId());
				refChamberComplexVal = refChamberMaintainer.getComplexValueAddressContact(refChamber, refChamberAddress,
						contactDetails);
			} else {
				refChamberComplexVal = refChamberMaintainer.getComplexValueAddressContact(refChamber, null, null);
			}
			log.debug(METHOD_EXIT + METHOD_NAME);
			return refChamberComplexVal;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Returns a Collection of RefChamberComplexValues
	 * 
	 * @param Integer
	 *            crestChamberId
	 * @param Integer
	 *            courtId
	 * @param Integer
	 *            includeDeleted
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Collection findChamberByCrestChamberIdCourtId(Integer crestChamberId, Integer courtId,
			Boolean includeDeleted) throws SysRefControllerException, CreateException {
		final String METHOD_NAME = "findChamberByCrestChamberIdCourtId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		ArrayList<RefChamberComplexValue> refChamberComplexVals = new ArrayList<RefChamberComplexValue>();

		try {
			Collection refChambers;
			refChamberMaintainer = new RefChamberMaintainer();
			if (includeDeleted) {
				refChambers = refChamberMaintainer.findAllChambersByCrestChamberIdCourtId(crestChamberId, courtId);
			} else {
				refChambers = refChamberMaintainer.findChamberByCrestChamberIdCourtId(crestChamberId, courtId);
			}

			Iterator iter = refChambers.iterator();
			while (iter.hasNext()) {
				RefChamber refChamber = (RefChamber) iter.next();
				RefChamberComplexValue val = null;
				if (refChamber.getAddress() != null) {
					XhbAddress refChamberAddress = addressHome.findByPrimaryKey(refChamber.getAddress().getAddressId());
					Collection contactDetails = contactDetailHome
							.findByAddressId(refChamber.getAddress().getAddressId());
					val = refChamberMaintainer.getComplexValueAddressContact(refChamber, refChamberAddress,
							contactDetails);
				} else {
					val = refChamberMaintainer.getComplexValueAddressContact(refChamber, null, null);
				}
				refChamberComplexVals.add(val);
			}
			log.debug(METHOD_EXIT + METHOD_NAME);
			return refChamberComplexVals;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Returns a Collection of RefChamberComplexValues
	 * 
	 * @param String
	 *            firmName
	 * @param Integer
	 *            courtId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Collection findChamberByFirmNameCourtId(String firmName, Integer courtId)
			throws SysRefControllerException, CreateException {
		final String METHOD_NAME = "findChamberByFirmNameCourtId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		ArrayList<RefChamberComplexValue> refChamberComplexVals = new ArrayList<RefChamberComplexValue>();

		try {
			Collection refChambers;
			refChamberMaintainer = new RefChamberMaintainer();
			refChambers = refChamberMaintainer.findChamberByFirmNameCourtId(firmName, courtId);

			Iterator iter = refChambers.iterator();
			while (iter.hasNext()) {
				RefChamber refChamber = (RefChamber) iter.next();
				RefChamberComplexValue val = null;
				if (refChamber.getAddress() != null) {
					XhbAddress refChamberAddress = addressHome.findByPrimaryKey(refChamber.getAddress().getAddressId());
					Collection contactDetails = contactDetailHome
							.findByAddressId(refChamber.getAddress().getAddressId());
					val = refChamberMaintainer.getComplexValueAddressContact(refChamber, refChamberAddress,
							contactDetails);
				} else {
					val = refChamberMaintainer.getComplexValueAddressContact(refChamber, null, null);
				}
				refChamberComplexVals.add(val);
			}
			log.debug(METHOD_EXIT + METHOD_NAME);
			return refChamberComplexVals;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Returns a Collection of RefChamberComplexValues
	 * 
	 * @param String
	 *            firmName
	 * @param Integer
	 *            crestChamberId
	 * @param Integer
	 *            courtId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Collection findChamberByFirmNameCrestChamberIdCourtId(String firmName, Integer crestChamberId,
			Integer courtId) throws SysRefControllerException, CreateException {
		final String METHOD_NAME = "findChamberByFirmNameCrestChamberIdCourtId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		ArrayList<RefChamberComplexValue> refChamberComplexVals = new ArrayList<RefChamberComplexValue>();

		try {
			Collection refChambers;
			refChamberMaintainer = new RefChamberMaintainer();
			refChambers = refChamberMaintainer.findChamberByFirmNameCrestChamberIdCourtId(firmName, crestChamberId,
					courtId);

			Iterator iter = refChambers.iterator();
			while (iter.hasNext()) {
				RefChamber refChamber = (RefChamber) iter.next();
				RefChamberComplexValue val = null;
				if (refChamber.getAddress() != null) {
					XhbAddress refChamberAddress = addressHome.findByPrimaryKey(refChamber.getAddress().getAddressId());
					Collection contactDetails = contactDetailHome
							.findByAddressId(refChamber.getAddress().getAddressId());
					val = refChamberMaintainer.getComplexValueAddressContact(refChamber, refChamberAddress,
							contactDetails);
				} else {
					val = refChamberMaintainer.getComplexValueAddressContact(refChamber, null, null);
				}
				refChamberComplexVals.add(val);
			}
			log.debug(METHOD_EXIT + METHOD_NAME);
			return refChamberComplexVals;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}
	
	/**
	 * Updates Chamber Ref Number for all valid courts
	 * 
	 * @param RefAdvocateComplexValue
	 *            refAdvocateComplexVal
	 * @param String 
	 *          userName
	 * @throws SQLException SysRefControllerException
	 * @ejb.interface-method view-type="both"
	 */
	public void updateChamberRef(RefAdvocateComplexValue refAdvocateComplexVal, String userName)
			throws SysRefControllerException {
		final String METHOD_NAME = "updateChamberRef";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			referenceDataDatabaseManager.updateChamberRef(refAdvocateComplexVal.getCrestAdvocateId(),
					refAdvocateComplexVal.getCrestChamberId(), userName);
		} catch (SQLException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Returns a RefAdvocateComplexValue
	 * 
	 * @param Integer
	 *            refAdvocateId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public RefAdvocateComplexValue findCounselByRefAdvocateId(Integer refAdvocateId)
			throws SysRefControllerException, CreateException {
		final String METHOD_NAME = "findCounselByRefAdvocateId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		RefAdvocateComplexValue refAdvocateComplexVal = new RefAdvocateComplexValue();
		RefChamberComplexValue refChamberComplexVal = new RefChamberComplexValue();

		try {
			RefAdvocate refAdvocate;
			refAdvocateMaintainer = new RefAdvocateMaintainer();
			refAdvocate = refAdvocateMaintainer.findByPrimaryKey(refAdvocateId);

			refAdvocateComplexVal = refAdvocateMaintainer.getComplexValue(refAdvocate);

			RefChamber refChamber;
			refChamberMaintainer = new RefChamberMaintainer();
			refChamber = refChamberMaintainer.findByPrimaryKey(refAdvocateComplexVal.getRefChamberId());
			if (refChamber.getAddress() != null) {
				XhbAddress refChamberAddress = addressHome.findByPrimaryKey(refChamber.getAddress().getAddressId());
				Collection contactDetails = contactDetailHome.findByAddressId(refChamber.getAddress().getAddressId());
				refChamberComplexVal = refChamberMaintainer.getComplexValueAddressContact(refChamber, refChamberAddress,
						contactDetails);
			} else {
				refChamberComplexVal = refChamberMaintainer.getComplexValueAddressContact(refChamber, null, null);
			}
			refAdvocateComplexVal.populateFromRefChamber(refChamberComplexVal);
			log.debug(METHOD_EXIT + METHOD_NAME);
			return refAdvocateComplexVal;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Returns a RefLegalRepBasicValue
	 * 
	 * @param Integer
	 *            refLegalRepId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public RefLegalRepresentativeBasicValue findLegalRepresentativeFromAdvocateLegalRepId(Integer refLegalRepId)
			throws SysRefControllerException, CreateException {
		final String METHOD_NAME = "findLegalRepresentativeFromAdvocateLegalRepId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		RefLegalRepresentativeBasicValue legalRepBasicVal = new RefLegalRepresentativeBasicValue();

		try {
			RefLegalRepresentative refLegalRepresentative;
			refLegalRepresentativeMaintainer = new RefLegalRepresentativeMaintainer();
			refLegalRepresentative = refLegalRepresentativeMaintainer.findByPrimaryKey(refLegalRepId);

			legalRepBasicVal = refLegalRepresentativeMaintainer.getBasicValue(refLegalRepresentative);
			log.debug(METHOD_EXIT + METHOD_NAME);
			return legalRepBasicVal;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Returns a Collection of RefAdvocateComplexValues
	 * 
	 * @param String
	 *            surname
	 * @param String
	 *            initials
	 * @param Integer
	 *            courtId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Collection findCounselBySurnameInitialsCourtId(String surname, String initials, Integer courtId)
			throws SysRefControllerException, CreateException {
		final String METHOD_NAME = "findCounselBySurnameInitialsCourtId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		ArrayList<RefAdvocateComplexValue> refAdvocateComplexVals = new ArrayList<RefAdvocateComplexValue>();

		try {
			Collection refAdvocates;
			RefChamberComplexValue refChamberComplexVal = new RefChamberComplexValue();
			refAdvocateMaintainer = new RefAdvocateMaintainer();
			refAdvocates = refAdvocateMaintainer.findCounselBySurnameInitialsCourtId(surname, initials, courtId);

			Iterator iter = refAdvocates.iterator();
			while (iter.hasNext()) {
				RefAdvocate refAdvocate = (RefAdvocate) iter.next();
				RefAdvocateComplexValue val = refAdvocateMaintainer.getComplexValue(refAdvocate);

				RefChamber refChamber;
				refChamberMaintainer = new RefChamberMaintainer();
				refChamber = refChamberMaintainer.findByPrimaryKey(val.getRefChamberId());
				if (refChamber.getAddress() != null) {
					XhbAddress refChamberAddress = addressHome.findByPrimaryKey(refChamber.getAddress().getAddressId());
					Collection contactDetails = contactDetailHome
							.findByAddressId(refChamber.getAddress().getAddressId());
					refChamberComplexVal = refChamberMaintainer.getComplexValueAddressContact(refChamber,
							refChamberAddress, contactDetails);
				} else {
					refChamberComplexVal = refChamberMaintainer.getComplexValueAddressContact(refChamber, null, null);
				}
				val.populateFromRefChamber(refChamberComplexVal);
				refAdvocateComplexVals.add(val);
			}
			log.debug(METHOD_EXIT + METHOD_NAME);
			return refAdvocateComplexVals;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}
	
	/**
	 * Updates Chamber for all valid courts
	 * 
	 * @param RefChamberComplexValue
	 *            refChamberComplexVal
	 * @param String
	 * 			  userDisplayName
	 * @return
	 * @throws SysRefControllerException
	 * @ejb.interface-method view-type="both"
	 */
	public void updateChamberDetails(RefChamberComplexValue refChamberComplexVal, String userDisplayName)
			throws SysRefControllerException {
		final String METHOD_NAME = "updateChamberDetails";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			referenceDataDatabaseManager.updateChamberDetails(refChamberComplexVal.getCrestChamberId(),
					refChamberComplexVal.getDxRef(), refChamberComplexVal.getLocationCode(),
					refChamberComplexVal.getFirmName(), refChamberComplexVal.getAddressId(),
					refChamberComplexVal.getClerkName(), refChamberComplexVal.getAddress().getAddress1(),
					refChamberComplexVal.getAddress().getAddress2(), refChamberComplexVal.getAddress().getAddress3(),
					refChamberComplexVal.getAddress().getAddress4(), refChamberComplexVal.getAddress().getTown(),
					refChamberComplexVal.getAddress().getCounty(), refChamberComplexVal.getAddress().getPostcode(),
					refChamberComplexVal.getAddress().getCountry(), userDisplayName,
					refChamberComplexVal.getTelephoneNumber(), refChamberComplexVal.getFaxNumber(),
					refChamberComplexVal.getEmailAddress(), refChamberComplexVal.getSecureEmailAddress());
		} catch (SQLException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Returns a Integer
	 * 
	 * @param String
	 *            telephoneNumber
	 * @param Integer
	 *            addressId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Integer createTelephoneContact(String telephoneNumber, Integer addressId, String createdBy)
			throws  CreateException {
		final String METHOD_NAME = "createTelephoneContact";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return createContactByType(telephoneNumber, addressId, "Phone", createdBy);
	}

	/**
	 * Returns a Integer
	 * 
	 * @param String
	 *            faxNumber
	 * @param Integer
	 *            addressId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Integer createFaxContact(String faxNumber, Integer addressId, String createdBy)
			throws CreateException {
		final String METHOD_NAME = "createFaxContact";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return createContactByType(faxNumber, addressId, "Fax", createdBy);
	}

	/**
	 * Returns a Integer
	 * 
	 * @param String
	 *            emailAddress
	 * @param Integer
	 *            addressId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Integer createEmailContact(String emailAddress, Integer addressId, String createdBy)
			throws CreateException {
		final String METHOD_NAME = "createEmailContact";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return createContactByType(emailAddress, addressId, "Non Secure Email", createdBy);
	}

	/**
	 * Returns a Integer
	 * 
	 * @param String
	 *            secureEmailAddress
	 * @param Integer
	 *            addressId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Integer createSecureEmailContact(String secureEmailAddress, Integer addressId, String createdBy)
			throws CreateException {
		final String METHOD_NAME = "createSecureEmailContact";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return createContactByType(secureEmailAddress, addressId, "Secure Email", createdBy);
	}

	/**
	 * Returns a Integer
	 * 
	 * @param String
	 *            contactValue
	 * @param Integer
	 *            addressId
	 * @param String 
	 *            contactType
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Integer createContactByType(String contactValue, Integer addressId, String contactType, String createdBy)
			throws CreateException {
		final String METHOD_NAME = "createContactByType";
		log.debug(METHOD_ENTER + METHOD_NAME);
		XhbContactDetailBasicValue contact = new XhbContactDetailBasicValue();

		if (contactValue != null) {
			contact.setContactType(contactType);
			contact.setContactValue(contactValue);
			contact.setCreatedBy(createdBy);
			contact.setLastUpdatedBy(createdBy);
			contact.setAddressId(addressId);
		}

		XhbContactDetail newContact = contactDetailHome.create(contact);
		Integer contactId = newContact.getContactId();
		log.debug(METHOD_EXIT + METHOD_NAME);
		return contactId;
	}

	/**
	 * Returns a Collection of RefAdvocateComplexValues
	 * 
	 * @param Integer
	 *            barNumber
	 * @param Integer
	 *            courtId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Collection findCounselByBarNumberCourtId(Integer barNumber, Integer courtId)
			throws SysRefControllerException, CreateException {
		final String METHOD_NAME = "findCounselByBarNumberCourtId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		ArrayList<RefAdvocateBasicValue> refAdvocateBasicVals = new ArrayList<RefAdvocateBasicValue>();

		try {
			Collection refAdvocates;
			refAdvocateMaintainer = new RefAdvocateMaintainer();
			refAdvocates = refAdvocateMaintainer.findCounselByBarNumberCourtId(barNumber, courtId);

			Iterator iter = refAdvocates.iterator();
			while (iter.hasNext()) {
				RefAdvocate refAdvocate = (RefAdvocate) iter.next();
				RefAdvocateBasicValue val = refAdvocateMaintainer.getBasicValue(refAdvocate);
				refAdvocateBasicVals.add(val);
			}
			log.debug(METHOD_EXIT + METHOD_NAME);
			return refAdvocateBasicVals;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}
	
	/**
	 * Updates Counsels for all valid courts
	 * 
	 * @param RefAdvocateBasicValue
	 *            refAdvocateBasicVal
	 * @param RefLegalRepresentativeBasicValue
	 *            refLegalRepBasicVal
	 * @param String
	 * 			  userDisplayName
	 * @return
	 * @throws SQLException SysRefControllerException
	 * @ejb.interface-method view-type="both"
	 */
	public void updateCounselDetails(RefAdvocateBasicValue refAdvocateBasicVal,
			RefLegalRepresentativeBasicValue refLegalRepBasicVal, String userDisplayName)
			throws SysRefControllerException {
		final String METHOD_NAME = "updateCounselDetails";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			referenceDataDatabaseManager.updateCounselDetails(refAdvocateBasicVal.getId(),
					refLegalRepBasicVal.getFirstName(), refLegalRepBasicVal.getMiddleName(),
					refLegalRepBasicVal.getSurname(), refLegalRepBasicVal.getTitle(), refLegalRepBasicVal.getInitials(),
					refLegalRepBasicVal.getLegalRepType(), refAdvocateBasicVal.getyearOfCall(),
					refAdvocateBasicVal.getVatNo(), refAdvocateBasicVal.getbarNo(), refAdvocateBasicVal.getHonours(),
					refAdvocateBasicVal.getAdvTypeInd(), userDisplayName);
		} catch (SQLException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}
	
	/**
	 * Deletes Counsels for all valid courts
	 * 
	 * @param RefAdvocateBasicValue
	 *            refAdvocateBasicVal
	 * @param String
	 * 			  userDisplayName
	 * @return
	 * @throws SysRefControllerException
	 * @ejb.interface-method view-type="both"
	 */
	public void deleteCounselDetails(RefAdvocateBasicValue refAdvocateBasicVal, String userDisplayName)
			throws SysRefControllerException {
		final String METHOD_NAME = "deleteCounselDetails";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			referenceDataDatabaseManager.deleteCounselDetails(refAdvocateBasicVal.getCrestAdvocateId(),
					userDisplayName);
		} catch (SQLException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}
	
	/**
	 * Retrieves the wll recipient (should only be one but coded safely) for the given court id and crest sol firm id
	 * 
	 * @param Integer 
	 *            courtId
	 * @param Integer
	 * 			  crestSolFirmId
	 * @return
	 * @throws SysRefControllerException
	 * @ejb.interface-method view-type="both"
	 */
	public XhbWllRecipientBasicValue[] getWllRecipientValues(Integer courtId, Integer crestSolFirmId) {
		final String METHOD_NAME = "getWllRecipientValues";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return referenceDataDatabaseManager.getWllRecipientValues(courtId, crestSolFirmId);
	}
	
	/**
	 * Return the given xhb wll recipient value
	 * 
	 * @param RefSolicitorFirmComplexValue
	 * 				cv
	 * @return XhbWllRecipientBasicValue
	 * @throws SysRefControllerException
	 * @ejb.interface-method view-type="both"
	 */
	public XhbWllRecipientBasicValue getWllRecipient(RefSolicitorFirmComplexValue cv) {
		final String METHOD_NAME = "getWllRecipient";
		log.debug(METHOD_ENTER + METHOD_NAME);
		XhbWllRecipientBasicValue[] wllRecipBVs = getWllRecipientValues(cv.getCourtId(), cv.getCrestSofId());
		log.debug(METHOD_EXIT + METHOD_NAME);
		// if there are actually any attached, take first one only as should only be one
		if (wllRecipBVs.length > 0) {
			return wllRecipBVs[0];
		} else {
			return null;
		}
	}

	/**
	 * Return the given xhb wll recipient value
	 * 
	 * @param RefProsecutorAgency
	 * 				cv
	 * @return XhbWllRecipientBasicValue
	 * @throws SysRefControllerException
	 * @ejb.interface-method view-type="both"
	 */
	public XhbWllRecipientBasicValue getWllRecipient(RefProsecutorAgencyComplexValue cv) {
		final String METHOD_NAME = "getWllRecipient";
		log.debug(METHOD_ENTER + METHOD_NAME);
		XhbWllRecipientBasicValue[] wllRecipBVs = getWllRecipientValues(cv.getCourtId(), Integer.parseInt(cv.getCrestOpposerId()));
		log.debug(METHOD_EXIT + METHOD_NAME);
		// if there are actually any attached, take first one only as should only be one
		if (wllRecipBVs.length > 0) {
			return wllRecipBVs[0];
		} else {
			return null;
		}
	}
	/**
	 * Invokes a stored function to get the next available CREST_CHAMBER_ID
	 * 
	 * @param
	 * @return CREST_CHAMBER_ID
	 * @ejb.interface-method view-type="both"
	 */
	public Integer getNextCrestChamberId() {
		final String METHOD_NAME = "getNextCrestChamberId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		return referenceDataDatabaseManager.getNextCrestChamberId();
	}

	/**
	 * Creates a new address, contact values and invokes a stored procedure to
	 * insert a new chamber for all valid courts
	 * 
	 * @param RefChamberComplexValue
	 *            refChamberComplexVal
	 * @return Integer newCrestChamberId
	 * @throws SQLException
	 *             SysRefControllerException
	 * 			refChamberComplexVal
	 * @param String 
	 *          userDisplayName
	 * @return Integer
	 * 			newCrestChamberId
	 * @throws SQLException SysRefControllerException
	 * @ejb.interface-method view-type="both"
	 */
	public Integer createNewChamber(RefChamberComplexValue refChamberComplexVal, String userDisplayName)
			throws SQLException, SysRefControllerException {
		final String METHOD_NAME = "createNewChamber";
		log.debug(METHOD_ENTER + METHOD_NAME);
		Integer newCrestChamberId = null;
		Integer newAddressId = null;

		try {
			newCrestChamberId = getNextCrestChamberId();
			newAddressId = createNewAddress(refChamberComplexVal.getAddress(), userDisplayName);

			createNewContact(refChamberComplexVal.getTelephoneNumber(), refChamberComplexVal.getFaxNumber(),
					refChamberComplexVal.getEmailAddress(), refChamberComplexVal.getSecureEmailAddress(), newAddressId, userDisplayName);

			refChamberComplexVal.setObsInd("N");
			refChamberComplexVal.setIsGlobal("Y");
			refChamberComplexVal.setCrestChamberId(newCrestChamberId);
			refChamberComplexVal.setAddressId(newAddressId);

			referenceDataDatabaseManager.addChamber(refChamberComplexVal.getId(), refChamberComplexVal.getObsInd(),
					refChamberComplexVal.getIsGlobal(), refChamberComplexVal.getDxRef(),
					refChamberComplexVal.getLocationCode(), refChamberComplexVal.getCrestChamberId(),
					refChamberComplexVal.getFirmName(), refChamberComplexVal.getAddressId(),
					refChamberComplexVal.getClerkName(), userDisplayName);
		} catch (CreateException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} catch (SQLException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} catch (SysRefControllerException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return newCrestChamberId;
	}
	
	/**
	 * Inserts a new Counsel for all valid courts
	 * 
	 * @param RefAdvocateBasicValue
	 *            refAdvocateBasicVal
	 * @param RefLegalRepresentativeBasicValue
	 *            refLegalRepBasicVal
	 * @param String
	 * 			  userDisplayName
	 * @return
	 * @throws SQLException SysRefControllerException
	 * @ejb.interface-method view-type="both"
	 */
	public void createNewCounsel(RefAdvocateBasicValue refAdvocateBasicVal,
			RefLegalRepresentativeBasicValue refLegalRepBasicVal, String userDisplayName)
			throws SysRefControllerException {
		final String METHOD_NAME = "createNewCounsel";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			referenceDataDatabaseManager.addCounsel(refAdvocateBasicVal.getId(),
					refLegalRepBasicVal.getFirstName(), refLegalRepBasicVal.getMiddleName(),
					refLegalRepBasicVal.getSurname(), refLegalRepBasicVal.getTitle(), refLegalRepBasicVal.getInitials(),
					refLegalRepBasicVal.getLegalRepType(), refAdvocateBasicVal.getyearOfCall(),
					refAdvocateBasicVal.getVatNo(), refAdvocateBasicVal.getbarNo(), refAdvocateBasicVal.getHonours(),
					refAdvocateBasicVal.getAdvTypeInd(), refAdvocateBasicVal.getCrestChamberId(), userDisplayName);
		} catch (SQLException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Creates a new contact
	 * 
	 * @param String
	 *            telphone
	 * @param String
	 *            fax
	 * @param String
	 *            email
	 * @param String
	 *            secureEmail
	 * @param Integer
	 *            addressId
	 * @return
	 * @throws CreateException
	 *             SysRefControllerException
	 * @ejb.interface-method view-type="both"
	 */
	public void createNewContact(String telephone, String fax, String email, String secureEmail, Integer addressId, String userDisplayName)
			throws CreateException, SysRefControllerException {
		final String METHOD_NAME = "createNewContact";
		log.debug(METHOD_ENTER + METHOD_NAME);
		if (!telephone.equals("")) {
			createTelephoneContact(telephone, addressId, userDisplayName);
		}
		if (!fax.equals("")) {
			createFaxContact(fax, addressId, userDisplayName);
		}
		if (!email.equals("")) {
			createEmailContact(email, addressId, userDisplayName);
		}
		if (!secureEmail.equals("")) {
			createSecureEmailContact(secureEmail, addressId, userDisplayName);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/*
     * Creates a new address using the address maintainer
     * @param AddressBasicValue 
     *            address
     * @param String 
     *            userDisplayName
     * @return Integer
     * @ejb.interface-method view-type="both"
     */
    public Integer createNewAddress(AddressBasicValue address, String userDisplayName)  throws CreateException {
    	final String METHOD_NAME = "createNewAddress";
		log.debug(METHOD_ENTER + METHOD_NAME);
    	Address newAddress = null;
    	
    	AddressMaintainer addressMaintainer = new AddressMaintainer();
		newAddress = (Address) addressMaintainer.create(address, userDisplayName);
		log.debug(METHOD_EXIT + METHOD_NAME);
		return newAddress.getAddressId();
	}

	/**
	 * Gets a list of calendar dates matching the court id and in between the
	 * from and to dates.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param courtId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Collection getRefCalendarDatesByCourt(Integer courtId, Date fromDate, Date toDate)
			throws SysRefControllerException {
		final String METHOD_NAME = "getRefCalendarDatesByCourt";
		log.debug(METHOD_ENTER + METHOD_NAME);
		ArrayList<RefCalendarBasicValue> refCalendarListBasicValues = new ArrayList<RefCalendarBasicValue>();

		RefCalendarMaintainer calendarMaintainer = new RefCalendarMaintainer();
		try {
			Collection<RefCalendar> refCalendarList;
			log.debug("calendarMaintainer finding refCalendar dates with a courtID of " + courtId
					+ " and calDate between " + fromDate + " and " + toDate + ".");
			refCalendarList = calendarMaintainer.findByCourtIdAndCalDate(courtId, fromDate, toDate);
			for (RefCalendar refCalendar : refCalendarList) {
				RefCalendarBasicValue refCalBasicValue = new RefCalendarBasicValue(refCalendar.getRefCalendarId(),
						refCalendar.getVersion(), refCalendar.getAvail(), refCalendar.getCalDate(),
						refCalendar.getCourtId(), refCalendar.getDescription(), refCalendar.getSysAcAvail());
				refCalendarListBasicValues.add(refCalBasicValue);
			}
		} catch (ObjectNotFoundException onfe) {
			throw new EJBException(onfe);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return refCalendarListBasicValues;
	}

	/**
	 * Returns true if court is available for date
	 * 
	 * @param courtId
	 *            court id
	 * @param date
	 *            date to check if available
	 * @return true court date is available
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws SysRefControllerException
	 */
	public Boolean isCourtAvailableOnDate(Integer courtId, Date date) throws SysRefControllerException {
		String METHOD_NAME = "isCourtAvailableOnDate";
		log.debug(METHOD_ENTER + METHOD_NAME + "(" + courtId + "," + date + ")");
		boolean result;
		try {
			result = getRefCalendarHelper().isCourtAvailableOnDate(courtId, date);
		} catch (ObjectNotFoundException e) {
			// If no record then assume its a working date
			result = true;
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return result;
	}

	/**
	 * Finds a final daily list for the specified court id and Calendar date.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param courtId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Collection getFinalDailyListByCourtAndDate(Integer courtId, Date diaryDate) {
		String METHOD_NAME = "getFinalDailyListByCourtAndDate";
		log.debug(METHOD_ENTER + METHOD_NAME);
		Collection<ListBasicValue> finalDailyList = listDatabaseManager.findFinalDailyListByDate(courtId, diaryDate);

		log.debug(METHOD_EXIT + METHOD_NAME);
		return finalDailyList;
	}

	/**
	 * Updates a list of Calendar Dates.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param updateList
	 *            list of RefCalendarBasicValue to update
	 */
	public void updateRefCalendarDates(List<RefCalendarBasicValue> updateList, String userDisplayName) {
		String METHOD_NAME = "updateRefCalendarDates";
		log.debug(METHOD_ENTER + METHOD_NAME);
		RefCalendarMaintainer calendarMaintainer = new RefCalendarMaintainer();
		calendarMaintainer.updateRefCalendar(updateList, userDisplayName);

		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Updates a RefSolicitorFirm complex entry including address and contact
	 * details.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param refSolicitorFirmId
	 * @param refSolicitorFirm
	 */
	public void updateRefSolicitorFirm(Integer refSolicitorFirmId, RefSolicitorFirmComplexValue refSolicitorFirm, String userDisplayName)
			throws SysRefControllerException {
		String METHOD_NAME = "updateRefSolicitorFirm";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			updateContactDetails(refSolicitorFirm.getAddressId(), userDisplayName, refSolicitorFirm.getTelephoneNumber(), 
					refSolicitorFirm.getFaxNumber(), refSolicitorFirm.getSecureEmailAddress(), refSolicitorFirm.getNonsecureEmailAddress());
			RefSolicitorFirmMaintainer maintainer = new RefSolicitorFirmMaintainer();
			maintainer.updateRefSolicitorFirm(refSolicitorFirmId, refSolicitorFirm, userDisplayName);
			XhbWllRecipientBasicValue bv = getWllRecipient(refSolicitorFirm);
			
			ArrayList<XhbContactDetailBasicValue> contactDetails = (ArrayList<XhbContactDetailBasicValue>) 
																		findContactsByAddressId(refSolicitorFirm.getAddressId());
			
			Iterator it = contactDetails.iterator();
			boolean emailFound = false;
			String email = "";
			String fax = "";
			while(it.hasNext()) {
				XhbContactDetailBasicValue contact = (XhbContactDetailBasicValue) it.next();
				if (contact.getContactType().equals("Fax")) {
					fax = contact.getContactValue();
				} else if (contact.getContactType().equals("Secure Email") && contact.getContactValue() != null && !contact.getContactValue().equals("")) {
					emailFound = true;
					email = contact.getContactValue();
				} else if (contact.getContactType().equals("Non Secure Email") && !emailFound) {
					email = contact.getContactValue();
				}
			}
			
			if(bv != null) {
				try {
		            XhbWllRecipient wllRecipient = wllHome.findByPrimaryKey(bv.getWllRecipientId());
		            WllRecipientHelper wllHelper = new WllRecipientHelper();
		            wllHelper.updateWLLRecipient(refSolicitorFirm, bv, email, fax, userDisplayName, wllRecipient);
				} catch(ObjectNotFoundException e) {
					log.debug("No WLL Recipient for "+bv.getWllRecipientId());
				}
			}
		} catch (CreateException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} catch(OptimisticLockException e){
			ctx.setRollbackOnly();
			throw e;
		} catch (IllegalArgumentException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(EJBException e) {
			ctx.setRollbackOnly();
			throw e;
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Creates a new Solicitor Firm.
	 * 
	 * @param RefSolicitorFirmComplexValue
	 *            refSolicitorFirm
	 * @param AddressValue
	 *            addressValue
	 * @param String
	 *            userDisplayName
	 * 
	 * @return new id
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Integer createSolicitorFirm(RefSolicitorFirmComplexValue refSolicitorFirm, AddressBasicValue addressValue, String userDisplayName)
			throws SysRefControllerException, CreateException {
		String METHOD_NAME = "createSolicitorFirm";
		log.debug(METHOD_ENTER + METHOD_NAME);
		// Create address
		Address address = createAddress(addressValue, userDisplayName);

		// create contacts
		int contactId;
		if (refSolicitorFirm.getTelephoneNumber() != null && !refSolicitorFirm.getTelephoneNumber().equals("")) {
			contactId = createTelephoneContact(refSolicitorFirm.getTelephoneNumber(), address.getAddressId(), userDisplayName);
			log.debug("PHONE: Contact ID is " + contactId + " associated with Address ID " + address.getAddressId());
		}
		if (refSolicitorFirm.getFaxNumber() != null && !refSolicitorFirm.getFaxNumber().equals("")) {
			contactId = createFaxContact(refSolicitorFirm.getFaxNumber(), address.getAddressId(), userDisplayName);
			log.debug("FAX: Contact ID is " + contactId + " associated with Address ID " + address.getAddressId());
		}
		if (refSolicitorFirm.getSecureEmailAddress() != null && !refSolicitorFirm.getSecureEmailAddress().equals("")) {
			contactId = createSecureEmailContact(refSolicitorFirm.getSecureEmailAddress(), address.getAddressId(), userDisplayName);
			log.debug("SECURE EMAIL: Contact ID is " + contactId + " associated with Address ID " + address.getAddressId());
		}
		if (refSolicitorFirm.getNonsecureEmailAddress() != null
				&& !refSolicitorFirm.getNonsecureEmailAddress().equals("")) {
			contactId = createEmailContact(refSolicitorFirm.getNonsecureEmailAddress(), address.getAddressId(), userDisplayName);
			log.debug("NON SECURE EMAIL: Contact ID is " + contactId + " associated with Address ID " + address.getAddressId());
		}

		// Create Solicitor Firm
		RefSolicitorFirmBasicValue refSolicitorBasicValue = new RefSolicitorFirmBasicValue();

		refSolicitorBasicValue.setAddressId(address.getAddressId());
		refSolicitorBasicValue.setSolicitorFirmName(refSolicitorFirm.getSolicitorFirmName());
		refSolicitorBasicValue.setShortName(refSolicitorFirm.getShortName());
		refSolicitorBasicValue.setLaCode(refSolicitorFirm.getLaCode());
		refSolicitorBasicValue.setDxRef(refSolicitorFirm.getDxRef());
		refSolicitorBasicValue.setObsInd("N");
		refSolicitorBasicValue.setCourtId(refSolicitorFirm.getCourtId());
		// Placeholder
		refSolicitorBasicValue.setCrestSofId(0);

		try {
			RefSolicitorFirmHome refSolicitorFirmHome = (RefSolicitorFirmHome) CSServices.getServiceLocator()
					.getLocalHome(RefSolicitorFirmHome.class);
			
			RefSolicitorFirm newRefSolicitorFirm = refSolicitorFirmHome.create(address,
					refSolicitorBasicValue.getCourtId(), refSolicitorBasicValue.getCrestSofId(),
					refSolicitorBasicValue.getDxRef(), refSolicitorBasicValue.getObsInd(),
					refSolicitorBasicValue.getShortName(), refSolicitorBasicValue.getLaCode(),
					refSolicitorBasicValue.getSolicitorFirmName(), null, userDisplayName);

			Integer newRefSolicitorFirmId = newRefSolicitorFirm.getRefSolicitorFirmId();
			// Set CREST_SOF_ID the same value as the primary key
			newRefSolicitorFirm.setCrestSofId(newRefSolicitorFirmId);

			//create the entry in wll recipient
			XhbWllRecipientBasicValue recObject = new XhbWllRecipientBasicValue();
			recObject.setCreatedBy(userDisplayName);
			recObject.setLastUpdatedBy(userDisplayName);
			recObject.setCourtId(refSolicitorBasicValue.getCourtId());
			recObject.setCrestSolicitorFirmId(newRefSolicitorFirm.getRefSolicitorFirmId());
			recObject.setRecipientType("S");
			
			String[] addresses = { address.getAddress1(), address.getAddress2(), address.getAddress3(),
					address.getAddress4(), address.getTown(), address.getCounty(), 
					address.getPostcode() };
			
			StringBuffer solictiorFirmAddress = new StringBuffer();
			for (int i = 0; i < addresses.length; i++) { 
				if(i==0) {
					//don't have to check if its null as its a mandatory field
					solictiorFirmAddress.append(addresses[i]);
				} else {
					if(addresses[i] != null && !addresses[i].equals("")) {
						solictiorFirmAddress.append(", "+addresses[i]);
					}
				}
			}
            recObject.setSolictiorFirmAddress(solictiorFirmAddress.toString());
			recObject.setSolicitorFirmName(refSolicitorBasicValue.getSolicitorFirmName());
            recObject.setSolicitorFirmFax(refSolicitorFirm.getFaxNumber());
            if(refSolicitorFirm.getNonsecureEmailAddress()!=null && !refSolicitorFirm.getNonsecureEmailAddress().equals("")) {
            	recObject.setSolicitorFirmEmail(refSolicitorFirm.getNonsecureEmailAddress());
            } else {
            	recObject.setSolicitorFirmEmail(refSolicitorFirm.getSecureEmailAddress());
            }
			wllHome.create(recObject);

			log.debug(METHOD_EXIT + METHOD_NAME);

			return newRefSolicitorFirmId;
		} catch (CreateException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} 
	}

	/**
	 * Updates a RefJudge.
	 * 
	 * @ejb.interface-method view-type="both"
	 * @param refJudgeId
	 * @param refJudge
	 * @param judgeTicketsToAdd
	 * @param judgeTicketsToDelete
	 * @param userDisplayName
	 */
	public void updateRefJudge(Integer refJudgeId, RefJudgeComplexValue refJudge,
			List<RefJudgeTicketBasicValue> judgeTicketsToAdd, List<RefJudgeTicketBasicValue> judgeTicketsToDelete,
			String userDisplayName) throws SysRefControllerException {
		String METHOD_NAME = "updateRefJudge";
		log.debug(METHOD_ENTER + METHOD_NAME);
		RefJudgeMaintainer maintainer = new RefJudgeMaintainer();
		try {
		maintainer.updateRefJudge(refJudgeId, refJudge);
		insertRefJudgeTickets(judgeTicketsToAdd, userDisplayName);
		deleteRefJudgeTickets(judgeTicketsToDelete, userDisplayName);
		} catch (CreateException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} catch (EJBException e) {
			ctx.setRollbackOnly();
			throw e;
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Inserts RefJudgeTickets associated with RefJudge.
	 * 
	 * @ejb.interface-method view-type="both"
	 * @param judgeTickets
	 *            list of judgeTickets
	 * @param userDisplayName
	 *            the supplied userDisplayName
	 */
	public void insertRefJudgeTickets(List<RefJudgeTicketBasicValue> judgeTickets, String userDisplayName)
			throws CreateException {
		String METHOD_NAME = "insertRefJudgeTickets";
		log.debug(METHOD_ENTER + METHOD_NAME);
		RefJudgeTicketMaintainer maintainer = new RefJudgeTicketMaintainer();
		maintainer.createRefJudgeTickets(judgeTickets, userDisplayName);

		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Deletes the specified list of JudgeTickets from the database.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param judgeTickets
	 * @param userDisplayName
	 */
	public void deleteRefJudgeTickets(List<RefJudgeTicketBasicValue> judgeTickets, String userDisplayName) throws EJBException {
		String METHOD_NAME = "deleteRefJudgeTickets";
		log.debug(METHOD_ENTER + METHOD_NAME);
		RefJudgeTicketMaintainer maintainer = new RefJudgeTicketMaintainer();

		for (RefJudgeTicketBasicValue value : judgeTickets) {
			maintainer.deleteRefJudgeTicket(value, userDisplayName);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Inserts a new RefJudge.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param RefJudgeComplexValue
	 *            the RefJudgeCV
	 * @param String
	 *            the supplied userDisplayName
	 * @return void
	 * @throws SysRefControllerException
	 */
	public void insertRefJudge(RefJudgeComplexValue refJudgeCV, List<RefJudgeTicketBasicValue> judgeTicketsToAdd,
			String userDisplayName) throws SysRefControllerException {
		String METHOD_NAME = "insertRefJudge";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			RefJudgeMaintainer maintainer = new RefJudgeMaintainer();
			Integer refJudgeId = maintainer.createRefJudge(refJudgeCV, userDisplayName);

			for (RefJudgeTicketBasicValue ticket : judgeTicketsToAdd) {
				ticket.setJudgeId(refJudgeId);
			}

			insertRefJudgeTickets(judgeTicketsToAdd, userDisplayName);
			log.debug(METHOD_EXIT + METHOD_NAME);
		} catch (CreateException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
	}

	/**
	 * Deletes RefJudge and associated RefJudgeTickets by marking as obsolete.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param RefJudgeComplexValue
	 *            the RefJudge to delete
	 * @param String userDisplayName           
	 * @return Nothing
	 * @throws SysRefControllerException
	 */
	public void deleteRefJudge(RefJudgeComplexValue refJudge, String userDisplayName) throws SysRefControllerException {
		final String METHOD_NAME = "deleteRefJudge";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			RefJudgeMaintainer maintainer = new RefJudgeMaintainer();
			maintainer.deleteRefJudge(refJudge, userDisplayName);

			RefJudgeTicketMaintainer ticketMaintainer = new RefJudgeTicketMaintainer();
			Collection<RefJudgeTicketBasicValue> judgeTickets = refJudge.getRefJudgeTickets();
			for (RefJudgeTicketBasicValue judgeTicket : judgeTickets) {
				ticketMaintainer.deleteRefJudgeTicket(judgeTicket, userDisplayName);
			}
		} catch (SysRefControllerException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Returns a collection of CaseListingEntry by judge id.
	 * 
	 * @param Integer
	 *            judgeId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws SysRefControllerException
	 */
	public Collection findCaseListingEntriesByJudgeId(Integer judgeId) throws SysRefControllerException {
		final String METHOD_NAME = "findCaseListingEntriesByJudgeId";
		log.debug(METHOD_ENTER + METHOD_NAME);

		try {
			CaseListingEntryMaintainer maintainer = new CaseListingEntryMaintainer();
			Collection<CaseListingEntryBasicValue> results = maintainer.findByJudgeId(judgeId);
			log.debug(METHOD_EXIT + METHOD_NAME);
			return results;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Returns a collection of CaseDiaryFixture by caseListingEntry id.
	 * 
	 * @param Integer
	 *            caseListingEntryId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws SysRefControllerException
	 */
	public Collection findCaseDiaryFixtureByListingId(Integer caseListingEntryId) throws SysRefControllerException {
		final String METHOD_NAME = "findCaseDiaryFixtureByListingId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		Collection results = null;
		try {
			CaseDiaryFixtureHelper caseDiaryFixtureHelper = new CaseDiaryFixtureHelper();

			results = caseDiaryFixtureHelper.getCaseDiaryFixturesByCaseListingIdAndStatus(caseListingEntryId, "A");
		} catch (FinderException e) {
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
		return results;
	}

	/**
	 * Returns a collection of SittinOnListBasicValue matching the judge id and
	 * after the specified date.
	 * 
	 * @param Integer
	 *            judgeId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws SysRefControllerException
	 */
	public Collection findSittingByJudgeIdAndDate(Integer judgeId, Date currentDate) throws SysRefControllerException {
		final String METHOD_NAME = "findSittingByJudgeIdAndDate";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			SittingOnListMaintainer maintainer = new SittingOnListMaintainer();
			Collection<SittingOnListBasicValue> results = maintainer.findByJudgeIdAndDate(judgeId, currentDate);
			log.debug(METHOD_EXIT + METHOD_NAME);
			return results;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Returns a collection of RefCourtComplexValue matching the court id.
	 * 
	 * @param Integer
	 *            courtId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws SysRefControllerException
	 */
	public CourtComplexValue findHomeCourtById(Integer courtId) throws SysRefControllerException {
		final String METHOD_NAME = "findHomeCourtById";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			CourtHelper helper = new CourtHelper();
			CourtComplexValue retValue = helper.findCourtById(courtId);
			log.debug(METHOD_EXIT + METHOD_NAME);
			return retValue;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Updates a Court complex entry including address and contact details.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param courtId
	 * @param courtComplexValue
	 * @param userDisplayName
	 */
	public void updateCourt(Integer courtId, CourtComplexValue courtComplexValue, String userDisplayName) throws SysRefControllerException {
		String METHOD_NAME = "updateCourt";
		log.debug(METHOD_ENTER + METHOD_NAME);
	
		//update contact details
		try {
			CourtMaintainer maintainer = new CourtMaintainer();
			maintainer.updateCourt(courtId, courtComplexValue, userDisplayName);
			updateCourtContactDetails(courtComplexValue.getAddressId(), userDisplayName, courtComplexValue.getTelephoneNumber(), courtComplexValue.getFaxNumber());
		} catch (CreateException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Updates a Court complex entry including address and contact details.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param courtComplexValue
	 */
	public Integer createCourtSite(CourtSiteComplexValue newComplexValue, String userDisplayName) throws SysRefControllerException {
		try {
			String METHOD_NAME = "createCourtSite";
			log.debug(METHOD_ENTER + METHOD_NAME);
			// Create address
			Address address = createAddress(newComplexValue.getAddress(), userDisplayName);

			// create contacts
			if (newComplexValue.getTelephoneNumber() != null && !newComplexValue.getTelephoneNumber().equals("")) {
				createContactByType(newComplexValue.getTelephoneNumber(), address.getAddressId(), "TEL", userDisplayName);
			}
			if (newComplexValue.getFaxNumber() != null && !newComplexValue.getFaxNumber().equals("")) {
				createContactByType(newComplexValue.getFaxNumber(), address.getAddressId(), "FAX", userDisplayName);
			}

			CourtSiteHome courtSiteHome = (CourtSiteHome) CSServices.getServiceLocator()
					.getLocalHome(CourtSiteHome.class);

			CourtMaintainer courtMaintainer = new CourtMaintainer();
			Court court = courtMaintainer.findByPrimaryKey(newComplexValue.getCourtId());

			// If the Display Name supplied is NULL then use Court Site Name instead
			String displayName = ( null == newComplexValue.getDisplayName() ) ? newComplexValue.getCourtSiteName() : newComplexValue.getDisplayName();
			
			CourtSite newCourtSite = courtSiteHome.create(newComplexValue.getCourtSiteName(),
					newComplexValue.getCourtSiteCode(), displayName, address.getAddressId(), court, "N", newComplexValue.getShortName(),
					userDisplayName, newComplexValue.getCrestCourtId(),
					newComplexValue.getFloaterText(), newComplexValue.getListName(), newComplexValue.getSiteGroup(),
					newComplexValue.getTier());

			Integer newCourtSiteId = newCourtSite.getCourtSiteId();
			
			// Create CourtRooms, moved to in here so we can do a rollback on the other tables if something goes wrong here
			createCourtRooms(newCourtSiteId, newComplexValue.getCourtRooms(), userDisplayName);

			
			log.debug(METHOD_EXIT + METHOD_NAME);

			return newCourtSiteId;
		} catch (Exception e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * Returns a Integer
	 * 
	 * @param AddressBasicValue
	 *            address
	 * @param String
	 *            userDisplayName
	 * 
	 * 
	 * @throws CreateException
	 */
	public Address createAddress(AddressBasicValue address, String userDisplayName) throws SysRefControllerException {
		try {
			String METHOD_NAME = "createAddress";
			log.debug(METHOD_ENTER + METHOD_NAME);
			AddressHome adHome = (AddressHome) CSServices.getServiceLocator().getLocalHome(AddressHome.class);

			String address1 = null;
			String address2 = null;
			String address3 = null;
			String address4 = null;
			String town = null;
			String county = null;
			String country = null;
			String postcode = null;
			if (address.getAddress1() != null) {
				address1 = address.getAddress1();
			}
			if (address.getAddress2() != null) {
				address2 = address.getAddress2();
			}
			if (address.getAddress3() != null) {
				address3 = address.getAddress3();
			}
			if (address.getAddress4() != null) {
				address4 = address.getAddress4();
			}
			if (address.getTown() != null) {
				town = address.getTown();
			}
			if (address.getCounty() != null) {
				county = address.getCounty();
			}
			if (address.getCountry() != null) {
				country = address.getCountry();
			}
			if (address.getPostcode() != null) {
				postcode = address.getPostcode();
			}

			Address newAddress = adHome.create(address1, address2, address3, address4, town, county, postcode, country, userDisplayName);
			log.debug(METHOD_EXIT + METHOD_NAME);
			return newAddress;
		} catch (Exception e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * Find a CourtSite ComplexValue by primary key.
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public CourtSiteComplexValue findCourtSiteById(Integer courtSiteId) throws BisRefControllerException {
		String METHOD_NAME = "findCourtSiteById";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			CourtSiteMaintainer courtSiteMaintainer = new CourtSiteMaintainer();
			return courtSiteMaintainer.getCourtSiteComplexValue(courtSiteMaintainer.findByPrimaryKey(courtSiteId));
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Inserts Court Rooms associated with a given Court Site.
	 * 
	 * @ejb.interface-method view-type="both"
	 * @param courtRooms
	 *            list of courtRooms
	 * @param userDisplayName
	 *            the supplied userDisplayName
	 */
	public void createCourtRooms(Integer courtSiteId, List<CourtRoomBasicValue> courtRooms, String userDisplayName)
			throws SysRefControllerException {
		String METHOD_NAME = "createCourtRooms";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			CourtRoomHome courtRoomHome = (CourtRoomHome) CSServices.getServiceLocator()
					.getLocalHome(CourtRoomHome.class);

			CourtSiteMaintainer courtSiteMaintainer = new CourtSiteMaintainer();

			CourtSite courtSite = courtSiteMaintainer.findByPrimaryKey(courtSiteId);

			for (CourtRoomBasicValue value : courtRooms) {
				CourtRoom courtRoom = courtRoomHome.create(courtSite, value.getCourtRoomName(), value.getDescription(),
						value.getCrestCourtRoomNo(), value.getObsInd(), userDisplayName, value.getSecurityInd(),
						value.getVideoInd());
				Integer courtRoomId = courtRoom.getCourtRoomId();
				log.debug("Created Court Room with id - " + courtRoomId);
			}
		} catch (Exception onfe) {
			ctx.setRollbackOnly();
			throw new EJBException(onfe);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Inserts Court Satellite.
	 * 
	 * @ejb.interface-method view-type="both"
	 * @param courtSatellite
	 *            the Court Satellite
	 * @param String 
	 *            userDisplayName
	 */
	public void createCourtSatellite(CourtSatelliteBasicValue value, String userDisplayName) throws SysRefControllerException {
		String METHOD_NAME = "createCourtSatellite";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			CourtSatelliteHome courtSatelliteHome = (CourtSatelliteHome) CSServices.getServiceLocator()
					.getLocalHome(CourtSatelliteHome.class);

			CourtSiteMaintainer courtSiteMaintainer = new CourtSiteMaintainer();
			CourtSite courtSite = courtSiteMaintainer.findByPrimaryKey(value.getCourtSiteId());

			CourtSatellite courtSatellite = courtSatelliteHome.create(value.getInternetSatelliteName(), courtSite,
					value.getObsInd(), userDisplayName);
			Integer courtSatelliteId = courtSatellite.getCourtSatelliteId();
			log.debug("Created Court Satellite with id - " + courtSatelliteId);
		} catch (Exception onfe) {
			ctx.setRollbackOnly();
			throw new EJBException(onfe);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Updates a CourtSite complex entry including address, contact details,
	 * Court Satellite and Court Rooms.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param courtSiteId
	 * @param details
	 *            of CourtSite
	 * @param userDisplayName
	 */
	public void updateCourtSite(Integer courtSiteId, CourtSiteComplexValue details, String userDisplayName, String initialObs) throws SysRefControllerException {
		String METHOD_NAME = "updateCourtSite";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			// Update CourtSite including address and contact details
			CourtSiteMaintainer maintainer = new CourtSiteMaintainer();
			courtSatelliteMaintainer = new CourtSatelliteMaintainer();
			maintainer.updateCourtSite(courtSiteId, details, userDisplayName);
			
			//update contact details
			updateCourtContactDetails(details.getAddressId(), userDisplayName, details.getTelephoneNumber(), details.getFaxNumber());
			
			CourtSite courtSite = maintainer.findByPrimaryKey(details.getId());

			// Insert/Update CourtRooms. For delete, ObsInd will be set to 'Y'
			// for
			// courtRooms marked as obsolete
			Collection<CourtRoom> courtRoomsInDb = courtSite.getCourtRooms();
			Collection<CourtRoomBasicValue> courtRoomsToSave = details.getCourtRooms();
			List<CourtRoomBasicValue> newCourtRooms = new ArrayList<CourtRoomBasicValue>();
			for (CourtRoomBasicValue courtRoomToSave : courtRoomsToSave) {
				boolean found = false;
				for (CourtRoom courtRoomInDb : courtRoomsInDb) {
					if (courtRoomInDb.getCourtRoomId().equals(courtRoomToSave.getId())) {
						courtRoomInDb.setObsInd(courtRoomToSave.getObsInd());
						courtRoomInDb.setSecurityInd(courtRoomToSave.getSecurityInd());
						courtRoomInDb.setVideoInd(courtRoomToSave.getVideoInd());
						courtRoomInDb.setCrestCourtRoomNo(courtRoomToSave.getCrestCourtRoomNo());
						courtRoomInDb.setDescription(courtRoomToSave.getDescription());
						courtRoomInDb.setCourtRoomName(courtRoomToSave.getCourtRoomName());
						courtRoomInDb.setDisplayName(courtRoomToSave.getDisplayName());
						courtRoomInDb.setUpdated(userDisplayName);
						found = true;
						break;
					}
				}
				if (found == false) {
					newCourtRooms.add(courtRoomToSave);
				}
			}
			createCourtRooms(details.getId(), newCourtRooms, userDisplayName);

			// Insert or Update CourtSatellite
			if (courtSite.getCourtSatellite() == null) {
				if (details.getCourtSatellite() != null) {
					createCourtSatellite(details.getCourtSatellite(), userDisplayName);
				}
			} else {
				if (details.getCourtSatellite() != null) {
					courtSatelliteMaintainer.deleteCourtSatellite(courtSite.getCourtSatellite(), userDisplayName);
				}
			}
			if(!initialObs.equals(details.getObsInd())) {
				// Delete CourtSite (or reinstate)
				deleteCourtSite(courtSiteId, details, userDisplayName);
			}
		} catch (Exception onfe) {
			ctx.setRollbackOnly();
			throw new EJBException(onfe);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Deletes a CourtSite including Court Satellite and Court Rooms.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param courtSiteId
	 * @param details
	 *            of CourtSite
	 * @param userDisplayName
	 */
	public void deleteCourtSite(Integer courtSiteId, CourtSiteComplexValue details, String userDisplayName) throws SysRefControllerException {
		String METHOD_NAME = "deleteCourtSite";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			CourtSiteMaintainer maintainer = new CourtSiteMaintainer();
			maintainer.deleteCourtSite(details, userDisplayName, details.getObsInd());
		} catch (Exception onfe) {
			ctx.setRollbackOnly();
			throw new EJBException(onfe);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Returns all hearing basic values by caseId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId
	 * @return Collection
	 * @throws ObjectNotFoundException
	 */
	public Collection findHearingByCaseId(Integer caseId) throws ObjectNotFoundException {
		String METHOD_NAME = "findHearingByCaseId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		HearingMaintainer hrMaintainer = new HearingMaintainer();
		return hrMaintainer.findByCaseId(caseId);
	}

	/**
	 * finds Judge Usage by room and sitting date.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param sittingDate
	 * @param courtRoomId
	 * @throws SysRefControllerException
	 */
	public Collection<JudgeUsageComplexValue> findJudgeUsageByDateAndRoom(final Date sittingDate,
			final Integer courtRoomId) throws SysRefControllerException {
		String METHOD_NAME = "findJudgeUsageByDateAndRoom";
		log.debug(METHOD_ENTER + METHOD_NAME);
		final JudgeUsageHelper helper = new JudgeUsageHelper();

		Collection<JudgeUsageComplexValue> judgeUsage;
		judgeUsage = helper.findBySittingDateAndCourtRoom(courtRoomId, sittingDate);

		log.debug(METHOD_EXIT + METHOD_NAME);

		return judgeUsage;
	}

	/**
	 * finds Court Room Usage by room and sitting date.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param sittingDate
	 * @param courtRoomId
	 * @throws SysRefControllerException
	 */
	public Collection<CourtRoomUsageComplexValue> findCourtRoomUsageByDateAndRoom(final Date sittingDate,
			final Integer courtRoomId) throws SysRefControllerException {
		String METHOD_NAME = "findCourtRoomUsageByDateAndRoom";
		log.debug(METHOD_ENTER + METHOD_NAME);
		final CourtRoomUsageHelper helper = new CourtRoomUsageHelper();

		Collection<CourtRoomUsageComplexValue> courtRoomUsage = helper.findBySittingDateAndCourtRoom(courtRoomId,
				sittingDate);

		log.debug(METHOD_EXIT + METHOD_NAME);

		return courtRoomUsage;
	}
	
	/**
	 * Adds a new court room usage record
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param courtRoomUsage
	 * @param userName
	 */
	public void addCourtRoomUsageTime(final CourtRoomUsageComplexValue courtRoomUsage, final String userName) {
		String METHOD_NAME = "addCourtRoomUsageTime";
		log.debug(METHOD_ENTER + METHOD_NAME);
		final CourtRoomUsageMaintainer maintainer = new CourtRoomUsageMaintainer();
		maintainer.create(courtRoomUsage, userName);
		log.debug(METHOD_EXIT + METHOD_NAME);
	}
	
	/**
	 * Adds a new judge usage record
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param courtRoomUsage
	 * @param userName
	 */
	public void addJudgeUsage(final JudgeUsageComplexValue judgeUsage, final String userName) {
		String METHOD_NAME = "addJudgeUsage";
		log.debug(METHOD_ENTER + METHOD_NAME);
		final JudgeUsageMaintainer maintainer = new JudgeUsageMaintainer();
		maintainer.create(judgeUsage, userName);
		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Updates hours and minutes on court room usage.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param courtRoomUsage
	 * @param userName
	 */
	public void updateCourtRoomUsageTime(final CourtRoomUsageComplexValue courtRoomUsage, final String userName) {

		String METHOD_NAME = "updateCourtRoomUsageTime";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			final CourtRoomUsageMaintainer maintainer = new CourtRoomUsageMaintainer();
			maintainer.updateCourtRoomUsageTime(courtRoomUsage, userName);

		} catch (ObjectNotFoundException onfe) {
			ctx.setRollbackOnly();
			throw new EJBException(onfe);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Update the CourtChambersInd field on JudgeUsage
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param complex
	 * @param userName
	 */
	public void updateJudgeUsageCourtChambersInd(final JudgeUsageComplexValue complex, final String userName) {
		String METHOD_NAME = "updateJudgeUsageCourtChambersInd";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			final JudgeUsageMaintainer maintainer = new JudgeUsageMaintainer();
			maintainer.updateJudgeUsageCourtChambersInd(complex, userName);
		} catch (ObjectNotFoundException onfe) {
			ctx.setRollbackOnly();
			throw new EJBException(onfe);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Deletes a CourtRoomUsage Entry.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param primaryKey
	 */
	public void deleteCourtRoomUsage(final Integer primaryKey) {

		String METHOD_NAME = "deleteCourtRoomUsage";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			final CourtRoomUsageMaintainer maintainer = new CourtRoomUsageMaintainer();
			maintainer.delete(maintainer.findByPrimaryKey(primaryKey));

		} catch (ObjectNotFoundException onfe) {
			ctx.setRollbackOnly();
			throw new EJBException(onfe);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Deletes a JudgeUsage Entry.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param primaryKey
	 */
	public void deleteJudgeUsage(final Integer primaryKey) {

		String METHOD_NAME = "deleteJudgeUsage";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			final JudgeUsageMaintainer maintainer = new JudgeUsageMaintainer();
			maintainer.delete(maintainer.findByPrimaryKey(primaryKey));

		} catch (ObjectNotFoundException onfe) {
			ctx.setRollbackOnly();
			throw new EJBException(onfe);
		}

		log.debug(METHOD_EXIT + METHOD_NAME);
	}

	/**
	 * Method to auto populate the JudgeUsage tables based on
	 * sittings.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param site
	 * @param startDate
	 * @param endDate
	 */
	public void generateRCSRecord(final Integer site, final Date startDate, final Date endDate) {
		String METHOD_NAME = "generateRCSRecord";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			final RCSDatabaseManager manager = new RCSDatabaseManager();
			manager.generateCourtroomStatisticsForSiteAndDate(site, startDate, endDate);

		} catch (final DataAccessException dae) {
			ctx.setRollbackOnly();
			throw new EJBException(dae);
		}
		log.debug(METHOD_EXIT + METHOD_NAME);
	}
	
    /**
     * Perfoms validation checks that a court room usage entry exists for a given room and date
     * @param courtroomid Court Room Id to check
     * @param sittingdate Sitting Date to check
     * @return 'Y' if exists, else 'N'
     * @ejb.interface-method view-type="both"
     */
    public String getCourtRoomUsageExists(Integer courtroomid, Date sittingdate) {
    	String METHOD_NAME = "getCourtRoomUsageExists";
		log.debug(METHOD_ENTER + METHOD_NAME);
    	try {
			final RCSDatabaseManager manager = new RCSDatabaseManager();
			return manager.getCourtRoomUsageExists(courtroomid, sittingdate);

		} catch (final DataAccessException dae) {
			ctx.setRollbackOnly();
			throw new EJBException(dae);
		}
    }
    
    /**
     * Perfoms validation checks that a judge usage entry exists for a given judge and date
     * @param sittingdate Sitting Date to check
     * @param judgeid Judge Id to check
     * @return 'Y' if exists, else 'N'
     * @ejb.interface-method view-type="both"
     */
	public String getJudgeUsageExists(Date sittingdate, Integer judgeid) {
		String METHOD_NAME = "getJudgeUsageExists";
		log.debug(METHOD_ENTER + METHOD_NAME);
    	try {
			final RCSDatabaseManager manager = new RCSDatabaseManager();
			return manager.getJudgeUsageExists(sittingdate, judgeid);

		} catch (final DataAccessException dae) {
			ctx.setRollbackOnly();
			throw new EJBException(dae);
		}
    }
	
	/**
	 * 
	 * @param courtID Court Crest no
	 * @ejb.interface-method view-type="both"
	 * @return list of System codes 
	 */
	public Collection getAllVerdictCodes(String courtID)
	{
		try
		{
			SystemCodeHelper sysCode = new SystemCodeHelper();
			RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
			criteria.setCodeType("VERDICT");
			criteria.setCourtId(courtID);
			ArrayList<RefSystemCodeBasicValue>systemCodes = new ArrayList<RefSystemCodeBasicValue>(sysCode.findSystemCodes(criteria));
			
			return systemCodes;
		}
		catch(Exception e)
		{
			
		}
		
		return null;
	}
	
	/** 
	 * Returns the count of number of xhb_ref_advocate objects with given crest chamber id and OBS_IND != 'Y'
	 * @param crestChamberId crest chamber Id
	 * @return count
	 * @ejb.interface-method view-type="both"
	 */
	public Integer countRefAdvocateWithCrestChamberId(Integer crestChamberId) {
		String METHOD_NAME = "countRefAdvocateWithCrestChamberId";
		log.debug(METHOD_ENTER + METHOD_NAME);
		final ReferenceDataDatabaseManager manager = new ReferenceDataDatabaseManager();
		try {
      return manager.countRefAdvocateWithCrestChamberId(crestChamberId);
    } catch (SQLException e) {
 			CSServices.getDefaultErrorHandler().handleError(e, clazz);
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
	}
	
	/**
	 * Sets xhb_ref_chamber objects obs_ind = 'Y' with the given  crest chamber id
	 * @param crestChamberId
	 * @param userDisplayName
	 * @ejb.interface-method view-type="both"
	 */
	public void deleteRefChamber(Integer crestChamberId, String userDisplayName) {
		String METHOD_NAME = "deleteRefChamber";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			final ReferenceDataDatabaseManager manager = new ReferenceDataDatabaseManager();
			manager.deleteRefChamber(crestChamberId, userDisplayName);
		} catch (SQLException e) {
			CSServices.getDefaultErrorHandler().handleError(e, clazz);
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
	}
	
	/**
	 * Sets xhb_justice_id objects which match the given sh justice id
	 * @param shJusticeId
	 * @ejb.interface-method view-type="both"
	 */
	public void deleteShJustice(Integer shJusticeId) {
		String METHOD_NAME = "deleteShJustice";
		log.debug(METHOD_ENTER + METHOD_NAME);
		try {
			final ReferenceDataDatabaseManager manager = new ReferenceDataDatabaseManager();
			manager.deleteShJustice(shJusticeId);
		} catch (SQLException e) {
			CSServices.getDefaultErrorHandler().handleError(e, clazz);
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
	}
	
	/**
	 * Return a RefAppResul by its primary key
	 * @param refAppPrimaryKey
	 * @return RefAppResult instance (or null)
	 * @ejb.interface-method view-type="both"
	 */
	public RefAppResultBasicValue findRefAppByPrimaryKey( Integer refAppPrimaryKey){
		RefAppResultBasicValue outcome = null;
		
		RefAppResultMaintainer maintainer = new RefAppResultMaintainer();
		
		try {
			RefAppResult appResult = maintainer.findByPrimaryKey(refAppPrimaryKey);
			
			if ( appResult != null ){
				outcome = maintainer.getBasicValue(appResult);
			}
		} catch (ObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EJBException(e);
		}
		
		return outcome;
	}
	
	/**
	 * Get all Appeal Result D20 Mappings
	 * @throws ObjectNotFoundException 
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public Collection<RefAppResD20MapBasicValue> getAppealResultD20Mappings() throws ObjectNotFoundException {
		String METHOD_NAME = "getAppealResultD20Mappings";
		log.debug(METHOD_ENTER + METHOD_NAME);
		final RefAppResD20MapMaintainer maintainer = new RefAppResD20MapMaintainer();
		Collection<RefAppResD20MapBasicValue> result = maintainer.findAll();
		log.debug(METHOD_EXIT + METHOD_NAME);
		return result;
	}
}
