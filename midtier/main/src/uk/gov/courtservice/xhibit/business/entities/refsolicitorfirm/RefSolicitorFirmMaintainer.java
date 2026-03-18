package uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;

/**
 * Maintainer for SolicitorFirm reference data entity.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.8 $
 */
public class RefSolicitorFirmMaintainer extends ReferenceDataMaintainer {
	private RefSolicitorFirmHome home = null;

	/**
	 * Default constructor.
	 */
	public RefSolicitorFirmMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public RefSolicitorFirm findByPrimaryKey(Integer key) throws ObjectNotFoundException {
		try {
			log.debug(ENTER_METHOD + "findByPrimaryKey");
			return this.getHome().findByPrimaryKey(key);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public Collection findByCourtId(Integer courtId) throws ObjectNotFoundException {
		String methodName = "findByCourtId() - ";
		log.debug(methodName + "called :: courtId: " + courtId);

		try {
			Collection coll = home.findByCourtId(courtId);
			int size = (coll != null) ? coll.size() : -1;
			log.debug(methodName + "Entries found: " + size);
			return coll;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
			if (f instanceof ObjectNotFoundException)
				throw (ObjectNotFoundException) f;
			throw new EJBException(f);
		}
	}

	/**
	 * Updates a Solicitor Firm.
	 * 
	 * @param refSolicitorFirmId
	 * @param refSolicitorFirm
	 * @throws SysRefControllerException
	 * @throws FinderException 
	 */
	@SuppressWarnings("unchecked")
	public void updateRefSolicitorFirm(Integer refSolicitorFirmId, RefSolicitorFirmComplexValue refSolicitorFirmCV, String userDisplayName)
			throws SysRefControllerException, FinderException {
		
			// --- Update RefSolicitorFirm fields ---
			RefSolicitorFirm refSolicitorFirm = this.getHome().findByPrimaryKey(refSolicitorFirmId);
			refSolicitorFirm.setSolicitorFirmName(refSolicitorFirmCV.getSolicitorFirmName());
			refSolicitorFirm.setDxRef(refSolicitorFirmCV.getDxRef());
			refSolicitorFirm.setLaCode(refSolicitorFirmCV.getLaCode());
			refSolicitorFirm.setShortName(refSolicitorFirmCV.getShortName());
			refSolicitorFirm.setObsInd(refSolicitorFirmCV.getObsInd());
			
			refSolicitorFirm.setUpdated(userDisplayName);

			// --- Update address for RefSolicitorFirm ---
			XhbAddress refSolicitorFirmAddress = XhbAddressBeanHelper2.findByPrimaryKey(refSolicitorFirmCV.getAddressId());
			refSolicitorFirmAddress.setAddress1(refSolicitorFirmCV.getAddress1());
			refSolicitorFirmAddress.setAddress2(refSolicitorFirmCV.getAddress2());
			refSolicitorFirmAddress.setAddress3(refSolicitorFirmCV.getAddress3());
			refSolicitorFirmAddress.setAddress4(refSolicitorFirmCV.getAddress4());
			refSolicitorFirmAddress.setCounty(refSolicitorFirmCV.getCounty());
			refSolicitorFirmAddress.setCountry(refSolicitorFirmCV.getCountry());
			refSolicitorFirmAddress.setPostcode(refSolicitorFirmCV.getPostcode());
			refSolicitorFirmAddress.setTown(refSolicitorFirmCV.getTown());
			refSolicitorFirmAddress.setLastUpdatedBy(userDisplayName);
		
	}

	/**
	 * Create and return a Basic Value given a local entity.
	 * 
	 * @param local
	 *            RefSolicitorFirm
	 * @return RefSolicitorFirmBasicValue
	 */
	public RefSolicitorFirmBasicValue getBasicValue(RefSolicitorFirm local) {
		log.debug(ENTER_METHOD + "getBasicValue");
		RefSolicitorFirmBasicValue value = new RefSolicitorFirmBasicValue(local.getRefSolicitorFirmId(),
				local.getVersion());
		/** @todo use getId() */
		this.loadValue(value, local);
		return value;
	}

	/**
	 * Create and return a Complex Value given a local entity.
	 * 
	 * @param local
	 *            RefSolicitorFirm
	 * @return RefSolicitorFirmComplexValue
	 */
	public RefSolicitorFirmComplexValue getComplexValue(RefSolicitorFirm local) {
		log.debug(ENTER_METHOD + "getComplexValue");
		RefSolicitorFirmComplexValue value = new RefSolicitorFirmComplexValue(local.getRefSolicitorFirmId(),
				local.getVersion());
		/** @todo use getId() */
		this.loadValue(value, local);
		return value;
	}

	/**
	 * The RefSolicitorFirm Home.
	 * 
	 * @return RefSolicitorFirmHome
	 */
	public RefSolicitorFirmHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise RefSolicitorFirmHome");
			this.home = (RefSolicitorFirmHome) CSServices.getServiceLocator().getLocalHome(RefSolicitorFirmHome.class);
		}
		return this.home;
	}

	protected void loadValue(RefSolicitorFirmBasicValue value, RefSolicitorFirm local) {
		value.setCourtId(local.getCourtId());
		value.setAddressId(local.getAddress().getAddressId());
		value.setCrestSofId(local.getCrestSofId());
		value.setSolicitorFirmName(local.getSolicitorFirmName());
		value.setVatNo(local.getVatNo());
		value.setDxRef(local.getDxRef());
		value.setObsInd(local.getObsInd());
		value.setShortName(local.getShortName());
		value.setLaCode(local.getLaCode());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
	}
}