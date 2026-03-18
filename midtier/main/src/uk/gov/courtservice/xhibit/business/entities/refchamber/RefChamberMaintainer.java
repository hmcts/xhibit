package uk.gov.courtservice.xhibit.business.entities.refchamber;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetail;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefChamberBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefChamberComplexValue;

/**
 * Maintainer for Chamber reference data entity.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.7 $
 */
public class RefChamberMaintainer extends ReferenceDataMaintainer {
    private RefChamberHome home = null;

    /**
     * Default constructor.
     */
    public RefChamberMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefChamber findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     * Find the non-deleted entity using the supplied Crest Chamber ID.
     * 
     * @param id
     *            Crest Chamber ID to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findChamberByCrestChamberIdCourtId(Integer crestChamberId, Integer courtId) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findChamberByCrestChamberIdCourtId");
            return this.getHome().findChamberByCrestChamberIdCourtId(crestChamberId, courtId);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }
    
    /**
     * Find the entity (Including deleted) using the supplied Crest Chamber ID.
     * 
     * @param id
     *            Crest Chamber ID to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findAllChambersByCrestChamberIdCourtId(Integer crestChamberId, Integer courtId) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findAllChambersByCrestChamberIdCourtId");
            return this.getHome().findAllChambersByCrestChamberIdCourtId(crestChamberId, courtId);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }
    
    /**
     * Find the entity using the supplied Firm Name.
     * 
     * @param firmName
     *            Firm name to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findChamberByFirmNameCourtId(String firmName, Integer courtId) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findChamberByFirmNameCourtId");
            return this.getHome().findChamberByFirmNameCourtId(firmName, courtId);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }
    
    /**
     * Find the entity using the supplied Firm Name.
     * 
     * @param firmName
     *            Firm name to use when performing the search
     * @param crestChamberId
     *			  Crest Chamber ID to use when performing the search
     * @param courtId
     *  		  ID of logged in court
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findChamberByFirmNameCrestChamberIdCourtId(String firmName, Integer crestChamberId, Integer courtId) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findChamberByFirmNameCrestChamberIdCourtId");
            return this.getHome().findChamberByFirmNameCrestChamberIdCourtId(firmName, crestChamberId, courtId);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }

    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            RefChamber
     * @return RefChamberBasicValue
     */
    public RefChamberBasicValue getBasicValue(RefChamber local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefChamberBasicValue value = new RefChamberBasicValue(local.getRefChamberId(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            RefChamber
     * @return RefChamberComplexValue
     */
    public RefChamberComplexValue getComplexValue(RefChamber local) {
        log.debug(ENTER_METHOD + "getComplexValue");
        RefChamberComplexValue value = new RefChamberComplexValue(local.getRefChamberId(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }
    
    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            RefChamber
     * @return RefChamberComplexValue
     */
    public RefChamberComplexValue getComplexValueAddressContact(RefChamber local, XhbAddress address, Collection contactDetails) {
        log.debug(ENTER_METHOD + "getComplexValue");
        RefChamberComplexValue value = new RefChamberComplexValue(local.getRefChamberId(), local.getVersion());
        this.loadValue(value, local);
        this.loadAddress(value, local, address);
        this.loadContact(value, contactDetails);
        
        return value;
    }

    /**
     * The RefChamber Home.
     * 
     * @return RefChamberHome
     */
    public RefChamberHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefChamberHome");
            this.home = (RefChamberHome) CSServices.getServiceLocator().getLocalHome(RefChamberHome.class);
        }
        return this.home;
    }

    private void loadValue(RefChamberBasicValue value, RefChamber local) {

        // Chamber address id is a nullable field
        if (local.getAddress() != null)
            value.setAddressId(local.getAddress().getAddressId());
        value.setCourtId(local.getCourt().getCourtId());
        value.setCrestChamberId(local.getCrestChamberId());
        value.setDxRef(local.getDxRef());
        value.setFirmName(local.getFirmName());
        value.setIsGlobal(local.getIsGlobal());
        value.setLocationCode(local.getLocationCode());
        value.setObsInd(local.getObsInd());
        value.setClerkName(local.getClerkName());
    }
    
    private void loadAddress(RefChamberComplexValue value, RefChamber local, XhbAddress refChamberAddress) {
		// Chamber address id is a nullable field
		if (local.getAddress() != null) {
			value.setAddressId(local.getAddress().getAddressId());
			AddressBasicValue adBV = new AddressBasicValue();
			adBV.setAddress1(refChamberAddress.getAddress1());
			adBV.setAddress2(refChamberAddress.getAddress2());
			adBV.setAddress3(refChamberAddress.getAddress3());
			adBV.setAddress4(refChamberAddress.getAddress4());
			adBV.setCounty(refChamberAddress.getCounty());
			adBV.setTown(refChamberAddress.getTown());
			adBV.setPostcode(refChamberAddress.getPostcode());
			adBV.setAddressId(refChamberAddress.getAddressId());
			value.setAddress(adBV);
		}
    }
    
	private void loadContact(RefChamberComplexValue value, Collection contactDetails) {
		if (contactDetails != null) {
			for (XhbContactDetail contactDetail : (ArrayList<XhbContactDetail>) contactDetails) {
				XhbContactDetailBasicValue contact = contactDetail.getData();
				String contactType = contact.getContactType();
				if (contactType != null) {
					if (contactType.equals("Fax")) {
						value.setFaxNumber(contact.getContactValue());
					} else if (contactType.equals("Phone")) {
						value.setTelephoneNumber(contactDetail.getContactValue());
					} else if (contactType.equals("Non Secure Email")) {
						value.setEmailAddress(contactDetail.getContactValue());
					} else if (contactType.equals("Secure Email")) {
						value.setSecureEmailAddress(contactDetail.getContactValue());
					}
				}
			}
		}
	}
}