package uk.gov.courtservice.xhibit.business.entities.refprosecutoragency;

import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetail;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_prosecutor_agency.XhbRefProsecutorAgency;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefProsecutorAgencyBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefProsecutorAgencyComplexValue;

public class RefProsecutorAgencyMaintainer extends ReferenceDataMaintainer {
    private RefProsecutorAgencyHome home = null;
	
    /**
     * Default constructor.
     */
    public RefProsecutorAgencyMaintainer() {
    }


    /**
     * Find the entity using the supplied primary key.
     * 
     * @param key
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefProsecutorAgency findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     * Find the entity using the supplied prosecutor agency id.
     * 
     * @param refProsecutorAgencyId
     *            Prosecutor agency id to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefProsecutorAgency findByRefProsecutorAgencyId(Integer refProsecutorAgencyId) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findByRefProsecutorAgencyId");
            return this.getHome().findByRefProsecutorAgencyId(refProsecutorAgencyId);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }

    /**
     * Find the entity using the supplied parameters.
     * 
     * @param courtId
     *            Integer
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findByCourtId(Integer courtId) throws ObjectNotFoundException {
        try {
			log.debug(ENTER_METHOD + "findByCourtId");
            return this.getHome().findByCourtId(courtId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new EJBException(e);
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }


    /**
     * Find the entity using the supplied parameters.
     * 
     * @param courtId
     *            Integer
     * @param prosecutorName3
     *            String
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findByCourtIdAndProsecutorName(Integer courtId, String prosecutorName3) throws ObjectNotFoundException {
        try {
			log.debug(ENTER_METHOD + "findByCourtIdAndProsecutorName");
            return this.getHome().findByCourtIdAndProsecutorName(courtId, prosecutorName3);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new EJBException(e);
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }


    /**
     * Find the entity using the supplied parameters.
     * 
     * @param courtId
     *            Integer
     * @param cpsCode
     *            String
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findByCourtIdAndCpsCode(Integer courtId, String cpsCode) throws ObjectNotFoundException {
        try {
			log.debug(ENTER_METHOD + "findByCourtIdAndCpsCode");
            return this.getHome().findByCourtIdAndCpsCode(courtId, cpsCode);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new EJBException(e);
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }


    /**
     * Find the entity using the supplied parameters.
     * 
     * @param courtId
     *            Integer
     * @param prosecutorName3
     *            String
     * @param cpsCode
     *            String
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findByCourtIdProsecutorNameandCpsCode(Integer courtId, String prosecutorName3, String cpsCode) throws ObjectNotFoundException {
        try {
			log.debug(ENTER_METHOD + "findByCourtIdProsecutorNameAndCpsCode");
            return this.getHome().findByCourtIdProsecutorNameAndCpsCode(courtId, prosecutorName3, cpsCode);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new EJBException(e);
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }


    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            RefProsecutorAgency
     * @return RefProsecutorAgencyBasicValue
     */
    public RefProsecutorAgencyBasicValue getBasicValue(RefProsecutorAgency local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefProsecutorAgencyBasicValue value = new RefProsecutorAgencyBasicValue();
        /** @todo use getId() */
        this.loadValue(value, local);
        return value;
    }


    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            RefProsecutorAgency
     * @return RefProsecutorAgencyComplexValue
     */
    public RefProsecutorAgencyComplexValue getComplexValue(RefProsecutorAgency local) {
        log.debug(ENTER_METHOD + "getComplexValue");
        RefProsecutorAgencyComplexValue value = new RefProsecutorAgencyComplexValue();
        /** @todo use getId() */
        this.loadValue(value, local);
        return value;
    }


    /**
     * The RefProsecutorAgency Home.
     * 
     * @return RefProsecutorAgencyHome
     */
    public RefProsecutorAgencyHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefProsecutorAgencyHome");
            this.home = (RefProsecutorAgencyHome)CSServices.getServiceLocator().getLocalHome(RefProsecutorAgencyHome.class);
        }
        return this.home;
    }


    protected void loadValue(RefProsecutorAgencyBasicValue value, RefProsecutorAgency local) {
    	value.setRefProsecutorAgencyId(local.getRefProsecutorAgencyId());
    	value.setTitle(local.getTitle());
    	value.setProsecutorName1(local.getProsecutorName1());
    	value.setProsecutorName2(local.getProsecutorName2());
    	value.setProsecutorName3(local.getProsecutorName3());
    	value.setInitials(local.getInitials());
    	value.setAddressId(local.getAddressId());
    	value.setCrestOpposerId(local.getCrestOpposerId());
    	value.setCourtId(local.getCourtId());
    	value.setCpsCode(local.getCpsCode());
    	value.setDxRef(local.getDxRef());
    	value.setObsInd(local.getObsInd());
    	value.setVersion(local.getVersion());
    }
    
    public RefProsecutorAgencyComplexValue returnComplexValue(XhbRefProsecutorAgency bv) {
    	RefProsecutorAgencyComplexValue complex = new RefProsecutorAgencyComplexValue();
    	complex.setId(bv.getRefProsecutorAgencyId());
    	complex.setAddressId(bv.getAddressId());
    	complex.setCourtId(bv.getCourtId());
    	complex.setCpsCode(bv.getCpsCode());
    	complex.setCreatedBy(bv.getCreatedBy());
    	complex.setCreationDate(bv.getCreationDate());
    	complex.setCrestOpposerId(bv.getCrestOpposerId());
    	complex.setDxRef(bv.getDxRef());
    	
    	complex.setInitials(bv.getInitials());
    	complex.setLastUpdateDate(bv.getLastUpdateDate());
    	complex.setLastUpdatedBy(bv.getLastUpdatedBy());
    	complex.setObsInd(bv.getObsInd());
    	complex.setProsecutorName1(bv.getProsecutorName1());
    	complex.setProsecutorName2(bv.getProsecutorName2());
    	complex.setProsecutorName3(bv.getProsecutorName3());
    	complex.setTitle(bv.getTitle());
    	complex.setVersion(bv.getVersion());
    	complex.setRefProsecutorAgencyId(bv.getRefProsecutorAgencyId());
    	
    	return complex;
    	
    }
    
    /**
     * Used to add the address to a complex value
     * @param value
     * @param refChamberAddress
     */
    public void loadAddress(RefProsecutorAgencyComplexValue value, XhbAddress refProsAddress) {
		value.setAddressId(refProsAddress.getAddressId());
		AddressBasicValue adBV = new AddressBasicValue();
		adBV.setId(refProsAddress.getAddressId());
		adBV.setAddress1(refProsAddress.getAddress1());
		adBV.setAddress2(refProsAddress.getAddress2());
		adBV.setAddress3(refProsAddress.getAddress3());
		adBV.setAddress4(refProsAddress.getAddress4());
		adBV.setCounty(refProsAddress.getCounty());
		adBV.setCountry(refProsAddress.getCountry());
		adBV.setTown(refProsAddress.getTown());
		adBV.setPostcode(refProsAddress.getPostcode());
		adBV.setAddressId(refProsAddress.getAddressId());
		adBV.setVersion(refProsAddress.getVersion());
		value.setAddress(adBV);
    }
    
    /**
     * Used to add contact details to a complex value
     * @param value
     * @param contactDetails
     */
	public  void loadContact(RefProsecutorAgencyComplexValue value, Collection contactDetails) {
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
						value.setNonsecureEmailAddress(contactDetail.getContactValue());
					} else if (contactType.equals("Secure Email")) {
						value.setSecureEmailAddress(contactDetail.getContactValue());
					}
				}
			}
		}
	}
}
