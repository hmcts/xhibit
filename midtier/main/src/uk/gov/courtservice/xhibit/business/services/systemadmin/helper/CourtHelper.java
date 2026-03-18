package uk.gov.courtservice.xhibit.business.services.systemadmin.helper;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.query.search.CourtQuery;
import uk.gov.courtservice.xhibit.business.database.query.search.CourtRoomQuery;
import uk.gov.courtservice.xhibit.business.database.query.search.CourtSiteQuery;
import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer;
import uk.gov.courtservice.xhibit.business.entities.contactdetail.ContactDetail;
import uk.gov.courtservice.xhibit.business.entities.contactdetail.ContactDetailHome;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoom;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSite;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSiteMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtRoomCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtSiteCriteria;

/**
 * This [SysRef] Helper channels all Court related queries.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.20 $
 */
public class CourtHelper extends AbstractHelper {
	private CourtMaintainer courtMaintainer = null;

	private CourtRoomMaintainer courtRoomMaintainer = null;

	private CourtSiteMaintainer courtSiteMaintainer = null;

	public CourtHelper() {
	}

	/**
	 * Given the criteria, build an SQL statement, execute and return Basic
	 * Value Objects.
	 * 
	 * @param CourtRoomCriteria
	 *            criteria
	 * @return Collection of CourtRoomBasicValue
	 * @throws SysRefControllerException
	 */
	public Collection findCourtRooms(CourtRoomCriteria criteria) throws SysRefControllerException {
		final String METHOD_NAME = "::findCourtRooms ";
		log.debug(METHOD_NAME + METHOD_ENTER);
		Collection courtRooms = null;
		if (criteria.getPrimaryKey() == null) {
			log.debug("find by criteria " + criteria.toString());
			courtRooms = this.findCourtRoomsByQuery(criteria);
		} else {
			log.debug("find by primary key [" + criteria.getPrimaryKey() + "]");
			try {
				CourtRoom localRef = this.getCourtRoomMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
				CourtRoomBasicValue value = this.getCourtRoomMaintainer().getBasicValue(localRef);
				log.debug("CourtHelper" + METHOD_NAME + "CourtRoomBasic Value " + value.toString());
				courtRooms = this.newCollection();
				courtRooms.add(value);
			} catch (ObjectNotFoundException anException) {
				CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
				throw this.buildSysObjectNotFoundException("ERR_NO", criteria, anException);
				/** @TODO Create an external error code for this situation. */
			}
		}

		log.debug("CourtHelper" + METHOD_NAME + " size of collection is " + courtRooms.size());
		log.debug(METHOD_NAME + METHOD_EXIT);
		return courtRooms;
	}

	/**
	 * Given the criteria, build an SQL statement, execute and return Basic
	 * Value Objects.
	 * 
	 * @param CourtCriteria
	 *            criteria
	 * @return Collection of CourtBasicValue
	 * @throws SysRefControllerException
	 */
	public Collection findCourts(CourtCriteria criteria) throws SysRefControllerException {
		final String METHOD_NAME = "::findCourts ";
		log.debug(METHOD_NAME + METHOD_ENTER);
		Collection courts = null;
		if (criteria.getPrimaryKey() == null) {
			log.debug("find by criteria " + criteria.toString());
			courts = this.findCourtsByQuery(criteria);
		} else {
			try {
				log.debug("find by primary key [" + criteria.getPrimaryKey() + "]");
				Court localRef = this.getCourtMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
				CourtBasicValue value = this.getCourtMaintainer().getCourtBasicValue(localRef);
				courts = this.newCollection();
				courts.add(value);
			} catch (ObjectNotFoundException anException) {
				CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
				throw this.buildSysObjectNotFoundException("ERR_NO", criteria, anException);
				/** @TODO Create an external error code for this situation. */
			}
		}
		log.debug(METHOD_NAME + METHOD_EXIT);
		return courts;
	}

	/**
	 * Given the criteria, build an SQL statement, execute and return Basic
	 * Value Objects.
	 * 
	 * @param CourtSiteCriteria
	 *            criteria
	 * @return Collection of CourtSiteBasicValue
	 * @throws SysRefControllerException
	 */
	public Collection findCourtSites(CourtSiteCriteria criteria) throws SysRefControllerException {
		final String METHOD_NAME = "::findCourtSites ";
		log.debug(METHOD_NAME + METHOD_ENTER);
		Collection courtSites = null;
		if (criteria.getPrimaryKey() == null) {
			log.debug("find by criteria " + criteria.toString());
			courtSites = this.findCourtSitesByQuery(criteria);
		} else {
			try {
				log.debug("find by primary key [" + criteria.getPrimaryKey() + "]");
				CourtSite localRef = this.getCourtSiteMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
				CourtSiteBasicValue value = this.getCourtSiteMaintainer().getCourtSiteBasicValue(localRef);
				courtSites = this.newCollection();
				courtSites.add(value);
			} catch (ObjectNotFoundException anException) {
				CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
				throw this.buildSysObjectNotFoundException("ERR_NO", criteria, anException);
				/** @TODO Create an external error code for this situation. */
			}
		}
		log.debug(METHOD_NAME + METHOD_EXIT);
		return courtSites;
	}

	/*
	 * findByQuery methods
	 * -------------------------------------------------------------------------
	 * -----------------------
	 */

	/**
	 * Find Court Rooms matching the given the criteria.
	 * <p>
	 * This ignores the primary key criterion.
	 * </p>
	 * 
	 * @param criteria
	 *            CourtRoomCriteria
	 * @return java.util.Collection
	 */
	private Collection findCourtRoomsByQuery(CourtRoomCriteria criteria) {
		final Collection results = (new CourtRoomQuery()).search(criteria);
		log.debug("findCourtRoomsByQuery() - returning" + results.size() + " results");
		return results;
	}

	/**
	 * Find Courts matching the given the criteria.
	 * <p>
	 * This ignores the primary key criterion.
	 * </p>
	 * 
	 * @param criteria
	 *            CourtCriteria
	 * @return java.util.Collection
	 */
	private Collection findCourtsByQuery(CourtCriteria criteria) {
		final Collection results = (new CourtQuery()).search(criteria);
		log.debug("findCourtsByQuery() - returning" + results.size() + " results");
		return results;
	}

	/**
	 * Find Court Sites matching the given the criteria.
	 * <p>
	 * This ignores the primary key criterion.
	 * </p>
	 * 
	 * @param criteria
	 *            CourtSiteCriteria
	 * @return java.util.Collection
	 */
	private Collection findCourtSitesByQuery(CourtSiteCriteria criteria) {
		final Collection results = (new CourtSiteQuery()).search(criteria);
		log.debug("findCourtSitesByQuery() - returning" + results.size() + " results");
		return results;
	}

	/**
	 * Find Court matching the given court id. Also get Contact Details - Tel
	 * and Fax.
	 * 
	 * @param courtId
	 * @return
	 */
	public CourtComplexValue findCourtById(Integer courtId) throws ObjectNotFoundException {
		final String METHOD_NAME = "::findCourtById ";
		log.debug(METHOD_NAME + METHOD_ENTER);
		CourtComplexValue retValue = new CourtComplexValue();
		try {
			Court court = getCourtMaintainer().findByPrimaryKey(courtId);
			
			Address address = court.getAddress();
			AddressMaintainer addressMaintainer = new AddressMaintainer();
			retValue.populateFromAddress(addressMaintainer.createBasicVO(address));
			retValue.setAddressId(address.getAddressId());
			retValue.setCourtName(court.getCourtName());
			retValue.setDxRef(court.getDxRef());
			retValue.setPoliceForceCode(court.getPoliceForceCode());
			retValue.setCrestCourtId(court.getCrestCourtId());
			retValue.setFlRepSort(court.getFlRepSort());
			retValue.setCourtStartTime(court.getCourtStartTime());
			retValue.setWlRepSort(court.getWlRepSort());
			retValue.setWlRepPeriod(court.getWlRepPeriod());
			retValue.setWlRepTime(court.getWlRepTime());
			retValue.setWlFreeText(court.getWlFreeText());
			retValue.setTier(court.getTier());
			retValue.setCountyLocCode(court.getCountyLocCode());
			retValue.setShortName(court.getShortName());
			Collection contactDetails;
			ContactDetailHome cHome = (ContactDetailHome) CSServices.getServiceLocator()
					.getLocalHome(ContactDetailHome.class);
			
			contactDetails = cHome.findByAddressId(address.getAddressId());
			for (ContactDetail contactDetail : (ArrayList<ContactDetail>) contactDetails) {
				String contactType = contactDetail.getContactType();
				if (contactType != null) {
					if (contactType.equals(CourtMaintainer.ContactType.FAX)) {
						retValue.setFaxNumber(contactDetail.getContactValue());
					} else if (contactType.equals(CourtMaintainer.ContactType.TEL)) {
						retValue.setTelephoneNumber(contactDetail.getContactValue());
					} 
				}
			}
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new EJBException(e);
		}
		log.debug(METHOD_EXIT + METHOD_ENTER);
		return retValue;
	}

	/*
	 * Maintainer accessors
	 * -------------------------------------------------------------------------
	 * ----------------------
	 */
	private CourtMaintainer getCourtMaintainer() {
		if (this.courtMaintainer == null) {
			log.debug(": Lazy initialise this.courtMaintainer");
			this.courtMaintainer = new CourtMaintainer();
		}
		return this.courtMaintainer;
	}

	private CourtRoomMaintainer getCourtRoomMaintainer() {
		if (this.courtRoomMaintainer == null) {
			log.debug(": Lazy initialise this.courtRoomMaintainer");
			this.courtRoomMaintainer = new CourtRoomMaintainer();
		}
		return this.courtRoomMaintainer;
	}

	private CourtSiteMaintainer getCourtSiteMaintainer() {
		if (this.courtSiteMaintainer == null) {
			log.debug(": Lazy initialise this.courtSiteMaintainer");
			this.courtSiteMaintainer = new CourtSiteMaintainer();
		}
		return this.courtSiteMaintainer;
	}
}
