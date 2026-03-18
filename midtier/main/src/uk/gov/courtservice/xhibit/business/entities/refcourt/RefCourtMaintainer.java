package uk.gov.courtservice.xhibit.business.entities.refcourt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtComplexValue;

/**
 * Maintainer for Reference Court.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.10 $
 */
public class RefCourtMaintainer extends ReferenceDataMaintainer {
    private RefCourtHome home = null;
    private AddressMaintainer addressMaintainer = null;

    /**
     * Default constructor.
     */
    public RefCourtMaintainer() {
        getHome();
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefCourt findByPrimaryKey(Integer key) throws ObjectNotFoundException {

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
     * Find by courtId
     * 
     * @param courtId
     * @return Collection of Recipient local references.
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
	 * Find by refCourtId
	 * 
	 * @param refCourtId
	 * @return Collection of Recipient local references.
	 * @throws ObjectNotFoundException
	 */
	public RefCourt findByRefCourtId(Integer refCourtId) throws ObjectNotFoundException {
		String methodName = "findByRefCourtId() - ";
		log.debug(methodName + "called :: refCourtId: " + refCourtId);

		try {
			return home.findByRefCourtId(refCourtId);
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
			if (f instanceof ObjectNotFoundException)
				throw (ObjectNotFoundException) f;
			throw new EJBException(f);
		}
	}

    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            RefCourt
     * @return RefCourtBasicValue
     */
    public RefCourtBasicValue getBasicValue(RefCourt local) {

        log.debug(ENTER_METHOD + "getBasicValue");
        RefCourtBasicValue value = new RefCourtBasicValue(local.getRefCourtId(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            RefCourt
     * @return RefCourtComplexValue
     */
    public RefCourtComplexValue getComplexValue(RefCourt local) {

        log.debug(ENTER_METHOD + "getComplexValue");
        RefCourtComplexValue value = new RefCourtComplexValue(local.getRefCourtId(), local.getVersion());
        this.loadValue(value, local);
    	
    	AddressBasicValue addressBasicValue = getAddressMaintainer().getAddressBasicValue(
                local.getAddress());
        value.setAddress(addressBasicValue);
        
        return value;
    }

    /**
     * The RefCourt Home.
     * 
     * @return RefCourtHome
     */
    public RefCourtHome getHome() {

        if (this.home == null) {
            log.debug(": lazy initialise RefCourtHome");
            this.home = (RefCourtHome) CSServices.getServiceLocator().getLocalHome(RefCourtHome.class);
        }
        return this.home;
    }

    private void loadValue(RefCourtBasicValue value, RefCourt local) {
        value.setCourtFullName(local.getCourtFullName());
        value.setCourtShortName(local.getCourtShortName());
        value.setNamePrefix(local.getNamePrefix());
        value.setCourtType(local.getCourtType());
        value.setCrestCode(local.getCrestCode());
        value.setObsInd(local.getObsInd());
        value.setDxRef(local.getDxRef());
        value.setIsPsd(local.getIsPsd());
        value.setAddressId(local.getAddressId());
      
        
    }
    private AddressMaintainer getAddressMaintainer() {

        if (addressMaintainer == null) {
            log.debug(": Lazy initialise this.addressMaintainer");
            this.addressMaintainer = new AddressMaintainer();
        }
        return this.addressMaintainer;
    }
    
    public String findByPSDCTCodeAndCourtId(String psdCode, Integer courtId) throws ObjectNotFoundException{
    	String methodName = "findByPSDCTCodeAndCourtId() - ";
		log.debug(methodName + "called :: psdCode: " + psdCode);

		try {
			return ((RefCourt)(home.findByPSDCTCodeAndCourtId(psdCode, courtId))).getCourtShortName();
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
			if (f instanceof ObjectNotFoundException)
				throw (ObjectNotFoundException) f;
			throw new EJBException(f);
		}
    }
    
    public ArrayList<RefCourtBasicValue> findByCourtIdAndIsPSD(String isPSD, Integer courtId){
    	String methodName = "findByCourtIdAndIsPSD() - ";
		log.debug(methodName + "called :: psd: " + isPSD);

		try {
			ArrayList<RefCourtBasicValue> refCourts = new ArrayList<RefCourtBasicValue>();
			Collection c = home.findByCourtIdAndIsPSD(isPSD, courtId);
			Iterator<RefCourt> iter = c.iterator();
			while(iter.hasNext()) {
				refCourts.add(getBasicValue(iter.next()));
			}
			return refCourts;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, this.getClass());
            throw new EJBException(f);
		}
    }

}