package uk.gov.courtservice.xhibit.business.entities.courtsite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.entities.address.AddressHome;
import uk.gov.courtservice.xhibit.business.entities.contactdetail.ContactDetail;
import uk.gov.courtservice.xhibit.business.entities.contactdetail.ContactDetailHome;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoom;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtsatellite.CourtSatellite;
import uk.gov.courtservice.xhibit.business.entities.courtsatellite.CourtSatelliteMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetail;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteComplexValue;

/**
 * Maintains 'read-only' Court Site instances.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.15 $
 */
public class CourtSiteMaintainer extends ReferenceDataMaintainer {
	private static CourtSiteHome home = null;

    public static interface ContactType {
	    public static final String FAX = "FAX";
		public static final String TEL = "TEL";
    }
    
	/**
	 * Given the Primary Key, find the Local instance of Court Site.
	 * 
	 * @param id
	 *            Integer
	 * @return Court
	 * @throws ObjectNotFoundException
	 */
	public CourtSite findByPrimaryKey(Integer id) throws ObjectNotFoundException {

		try {
			log.debug(ENTER_METHOD + "findByPrimaryKey(" + id + ")");
			return this.getHome().findByPrimaryKey(id);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}

	/**
	 * Given the courtId, find all Court Sites.
	 * 
	 * @param id
	 *            Integer
	 * @return Collection<CourtSiteBasicValue>
	 * @throws ObjectNotFoundException
	 */
	public Collection findAllCourtSites(Integer courtId) throws SysRefControllerException {

		try {
			log.debug(ENTER_METHOD + "findAllCourtSites(" + courtId + ")");
			Collection<CourtSite> courtSites = this.getHome().findAllCourtSites(courtId);
			List<CourtSiteBasicValue> basicCourtSites = new ArrayList<CourtSiteBasicValue>();
			if (courtSites != null && courtSites.size() > 0) {
				for (CourtSite local : courtSites) {
					basicCourtSites.add(getCourtSiteBasicValue(local));
				}
			}
			return basicCourtSites;
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw new EJBException(anException);
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	/**
	 * Given the courtId, find the home court and satellites.
	 * 
	 * @param id
	 *            Integer
	 * @return Collection<CourtSiteBasicValue>
	 * @throws ObjectNotFoundException
	 */
	public Collection findHomeCourtAndSatellite(Integer courtId) throws ObjectNotFoundException{

		try {
			log.debug(ENTER_METHOD + "findHomeCourtAndSatellite(" + courtId + ")");
			Collection<CourtSite> courtSites = this.getHome().findHomeCourtAndSatellite(courtId);
			List<CourtSiteBasicValue> basicCourtSites = new ArrayList<CourtSiteBasicValue>();
			if (courtSites != null && courtSites.size() > 0) {
				for (CourtSite local : courtSites) {
					basicCourtSites.add(getCourtSiteBasicValue(local));
				}
			}
			return basicCourtSites;
		} catch (ObjectNotFoundException anException) {
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	/**
	 * Given the Court Site Id, find the instance(s) of Court Site - similar to Highlander "There should be only one" :-)
	 * 
	 * @param id
	 *            Integer
	 * @return Court
	 * @throws ObjectNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public Collection findByCrestCourtId(Integer courtSiteId) throws ObjectNotFoundException {

		try {
			log.debug(ENTER_METHOD + "findByCrestCourtId(" + courtSiteId + ")");
			Collection<CourtSite> courtSites = this.getHome().findByCrestCourtId(courtSiteId);
			List<CourtSiteBasicValue> basicCourtSites = new ArrayList<CourtSiteBasicValue>();
			if (courtSites != null && courtSites.size() > 0) {
				for (CourtSite local : courtSites) {
					basicCourtSites.add(getCourtSiteBasicValue(local));
				}
			}
			return basicCourtSites;
		} catch (ObjectNotFoundException anException) {
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}

	/**
	 * Creates a [basic] value object from the local reference
	 * 
	 * @param Local
	 *            reference
	 * @return Value Object
	 */
	public CourtSiteBasicValue getCourtSiteBasicValue(CourtSite local) {

		log.debug(ENTER_METHOD + "getCourtSiteBasicValue");

		CourtSiteBasicValue value = new CourtSiteBasicValue(local.getCourtSiteId(), local.getVersion());
		loadValue(value, local);

		log.debug(EXIT_METHOD + "getCourtSiteBasicValue");

		return value;
	}

	/**
	 * Creates a [complex] value object from the local reference
	 * 
	 * @param Local
	 *            reference
	 * @return Value Object
	 */
	public CourtSiteComplexValue getCourtSiteComplexValue(CourtSite local) throws FinderException {

		log.debug(ENTER_METHOD + "getCourtSiteComplexValue");

		CourtSiteComplexValue complexCourtSite = new CourtSiteComplexValue(local.getCourtSiteId(), local.getVersion());
		loadValue(complexCourtSite, local);

		// Set Address
		loadAddressDetails(complexCourtSite, local);

		// Set Contacts
		loadContactDetails(complexCourtSite, local);

		// Set CourtRooms
		CourtRoomMaintainer courtRoomMaintainer = new CourtRoomMaintainer();
		List<CourtRoomBasicValue> basicCourtRooms = new ArrayList<CourtRoomBasicValue>();
		for (CourtRoom courtRoom : (Collection<CourtRoom>) local.getCourtRooms()) {
				basicCourtRooms.add(courtRoomMaintainer.getBasicValue(courtRoom));
		}

		if (basicCourtRooms != null && basicCourtRooms.size() > 0) {
			complexCourtSite.setCourtRooms((List<CourtRoomBasicValue>) basicCourtRooms);
		}

		// Set CourtSatellite
		CourtSatelliteMaintainer courtSatelliteMaintainer = new CourtSatelliteMaintainer();
		if (local.getCourtSatellite() != null) {
			complexCourtSite.setCourtSatellite(courtSatelliteMaintainer.getCourtSatelliteBasicValue(local.getCourtSatellite()));
		}

		log.debug(EXIT_METHOD + "getCourtSiteComplexValue");

		return complexCourtSite;
	}

	/**
	 * Cache the home in our local static for all instances of our class to use.
	 * 
	 * @return CourtSite
	 */
	public CourtSiteHome getHome() {
		if (home == null) {
			log.debug(": Lazy initialise this.home.");
			home = (CourtSiteHome) CSServices.getServiceLocator().getLocalHome(CourtSiteHome.class);
		}
		return home;
	}

	private void loadValue(CourtSiteBasicValue value, CourtSite local) {
		value.setAddressId(local.getAddressId());
		value.setCourtSiteCode(local.getCourtSiteCode());
		value.setCourtSiteName(local.getCourtSiteName());
		value.setCourtId(local.getCourtId());
		value.setDisplayName(local.getDisplayName());
		value.setObsInd(local.getObsInd());
		value.setShortName(local.getShortName());
		value.setCrestCourtId(local.getCrestCourtId());
		value.setFloaterText(local.getFloaterText());
		value.setListName(local.getListName());
		value.setSiteGroup(local.getSiteGroup());
		value.setTier(local.getTier());
	}

	private void loadAddressDetails(CourtSiteComplexValue complexCourtSite, CourtSite local) throws FinderException {
		AddressHome aHome = (AddressHome) CSServices.getServiceLocator().getLocalHome(AddressHome.class);

		if (!(local.getAddressId() == null)) {
			complexCourtSite.setAddressId(local.getAddressId());
			Address aV = aHome.findByPrimaryKey(local.getAddressId());
			if (!(aV == null)) {
				if (!(aV.getAddress1() == null)) {
					complexCourtSite.setAddress1(aV.getAddress1());
				}
				if (!(aV.getAddress2() == null)) {
					complexCourtSite.setAddress2(aV.getAddress2());
				}
				if (!(aV.getAddress3() == null)) {
					complexCourtSite.setAddress3(aV.getAddress3());
				}
				if (!(aV.getAddress4() == null)) {
					complexCourtSite.setAddress4(aV.getAddress4());
				}
				if (!(aV.getCounty() == null)) {
					complexCourtSite.setCounty(aV.getCounty());
				}
				if (!(aV.getPostcode() == null)) {
					complexCourtSite.setPostcode(aV.getPostcode());
				}
				if (!(aV.getTown() == null)) {
					complexCourtSite.setTown(aV.getTown());
				}
			}
		}
	}

	private void loadContactDetails(CourtSiteComplexValue complexCourtSite, CourtSite local) throws FinderException {
		ContactDetailHome cHome = (ContactDetailHome) CSServices.getServiceLocator()
				.getLocalHome(ContactDetailHome.class);

		Collection contactDetails = cHome.findByAddressId(local.getAddressId());
		for (ContactDetail contactDetail : (ArrayList<ContactDetail>) contactDetails) {
			String contactType = contactDetail.getContactType();
			if (contactType != null) {
				if (contactType.equals(ContactType.FAX)) {
					complexCourtSite.setFaxNumber(contactDetail.getContactValue());
				} else if (contactType.equals(ContactType.TEL)) {
					complexCourtSite.setTelephoneNumber(contactDetail.getContactValue());
				}
			}
		}
	}

	/**
	 * Updates a Court Site.
	 * 
	 * @param courtSiteId
	 * @param courtSiteComplexVal
	 * @param userDisplayName
	 * @throws SysRefControllerException
	 */
	@SuppressWarnings("unchecked")
	public void updateCourtSite(Integer courtSiteId, CourtSiteComplexValue complex, String userDisplayName) throws SysRefControllerException {
		try {
			// --- Update CourtSite fields ---
			CourtSite courtSite = this.getHome().findByPrimaryKey(courtSiteId);

			// check version
			if (courtSite.getVersion() == null || complex.getVersion() == null
					|| !courtSite.getVersion().equals(complex.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			// --- Update CourtSite fields ---
			courtSite.setCourtSiteName(complex.getCourtSiteName());
			if ( null == complex.getDisplayName() ) {
				// No Display Name has been specified so use Court Site Name instead
				courtSite.setDisplayName(complex.getCourtSiteName());
			}
			courtSite.setCrestCourtId(complex.getCrestCourtId());
			courtSite.setCourtSiteCode(complex.getCourtSiteCode());
			courtSite.setSiteGroup(complex.getSiteGroup());
			courtSite.setListName(complex.getListName());
			courtSite.setFloaterText(complex.getFloaterText());
			courtSite.setTier(complex.getTier());
			courtSite.setUpdated(userDisplayName);

			// --- Update address for CourtSite ---
			XhbAddress courtSiteAddress = XhbAddressBeanHelper2.findByPrimaryKey(complex.getAddressId());
			courtSiteAddress.setAddress1(complex.getAddress1());
			courtSiteAddress.setAddress2(complex.getAddress2());
			courtSiteAddress.setAddress3(complex.getAddress3());
			courtSiteAddress.setAddress4(complex.getAddress4());
			courtSiteAddress.setCounty(complex.getCounty());
			courtSiteAddress.setPostcode(complex.getPostcode());
			courtSiteAddress.setTown(complex.getTown());
			courtSiteAddress.setLastUpdatedBy(userDisplayName);

		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}

	/**
	 * Deletes a CourtSite.
	 * 
	 * @param local
	 * @param userDisplayName
	 */
	public void deleteCourtSite(CourtSiteComplexValue complex, String userDisplayName, String doDelete) throws SysRefControllerException {
		try {
			CourtSite local = findByPrimaryKey(complex.getId());

			// check version
			if (local.getVersion() == null || complex.getVersion() == null
					|| !local.getVersion().equals(complex.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}

			local.setObsInd(doDelete);
			local.setUpdated(userDisplayName);

			Collection<CourtRoom> courtRooms = local.getCourtRooms();
			if (courtRooms != null) {
				for (CourtRoom courtRoom : courtRooms) {
					courtRoom.setObsInd(doDelete);
					courtRoom.setUpdated(userDisplayName);
				}
			}

			CourtSatellite courtSatellite = local.getCourtSatellite();
			if (courtSatellite != null) {
				courtSatellite.setObsInd(doDelete);
				courtSatellite.setUpdated(userDisplayName);
			}
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}
}
