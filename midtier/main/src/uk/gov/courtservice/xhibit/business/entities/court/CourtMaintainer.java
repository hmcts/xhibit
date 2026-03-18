package uk.gov.courtservice.xhibit.business.entities.court;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetail;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtComplexValue;

/**
 * Maintainer of the read-only (system)reference data type, Court.
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
public class CourtMaintainer extends ReferenceDataMaintainer {
    private static CourtHome home = null;
    
    public static interface ContactType {
	    public static final String FAX = "FAX";
		public static final String TEL = "TEL";
    }

    public CourtMaintainer() {
    }

    /**
     * Given the Primary Key, find the Local instance of Court.
     * 
     * @param id
     *            Integer
     * @return Court
     */
    public Court findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            Court
     * @return CourtBasicValue
     */
    public CourtBasicValue getCourtBasicValue(Court local) {
        log.debug(ENTER_METHOD + "getCourtBasicValue");

        CourtBasicValue value = new CourtBasicValue(local.getCourtId(), local.getVersion());
        loadValue(value, local);

        log.debug(EXIT_METHOD + "getCourtBasicValue");
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            Court
     * @return CourtComplexValue
     */
    public CourtComplexValue getCourtComplexValue(Court local) {
        log.debug(ENTER_METHOD + "getCourtComplexValue");

        CourtComplexValue value = new CourtComplexValue(local.getCourtId(), local.getVersion());
        loadValue(value, local);

        log.debug(EXIT_METHOD + "getCourtComplexValue");

        return value;
    }

    /**
     * Cache the home in our local static for all instances of our class to use.
     * 
     * @return CourtValue
     */
    public CourtHome getHome() {
        if (home == null) {
            log.debug(": getHome - Lazy initialise this.home");
            home = (CourtHome) CSServices.getServiceLocator().getLocalHome(CourtHome.class);
        }

        return home;
    }

    /**
     * Returns all
     * 
     * @return
     */
    public CourtComplexValue[] findAll() {
        try {
            Collection col = getHome().findAll();
            CourtComplexValue[] ret = new CourtComplexValue[col.size()];
            Iterator it = getHome().findAll().iterator();

            for (int i = 0; i < ret.length && it.hasNext(); i++) {
                ret[i] = getCourtComplexValue((Court) it.next());
            }

            return ret;
        } catch (FinderException ex) {
            throw new EJBException(ex);
        }
    }
    
    /**
     * Returns all
     * 
     * @return
     */
    public Collection findAllCourts() {
    	try {
			Collection col = getHome().findAll();
			return col;
		} catch (FinderException ex) {
			throw new EJBException(ex);
		}
    }

    private void loadValue(CourtBasicValue value, Court local) {
        value.setCircuit(local.getCircuit());
        value.setCourtName(local.getCourtName());
        value.setCourtType(local.getCourtType());
        value.setCrestCourtId(local.getCrestCourtId());
        value.setCourtPrefix(local.getCourtPrefix());
        value.setShortName(local.getShortName());
        value.setDisplayName(local.getDisplayName());
        value.setObsInd(local.getObsInd());
        value.setPoliceForceCode(local.getPoliceForceCode());
        value.setFlRepSort(local.getFlRepSort());
        value.setCourtStartTime(local.getCourtStartTime());
        value.setWlRepSort(local.getWlRepSort());
        value.setWlRepPeriod(local.getWlRepPeriod());
        value.setWlRepTime(local.getWlRepTime());
        value.setWlFreeText(local.getWlFreeText());
    }
    
	/**
	 * Updates a Court.
	 * 
	 * @param courtId
	 * @param courtComplexValue
	 * @param userDisplayName
	 * @throws SysRefControllerException
	 */
	@SuppressWarnings("unchecked")
	public void updateCourt(Integer courtId, CourtComplexValue courtComplexValue, String userDisplayName)
			throws SysRefControllerException, FinderException {
		// --- Update Court fields ---
		Court court = this.getHome().findByPrimaryKey(courtId);
		court.setCourtName(courtComplexValue.getCourtName());
		court.setDxRef(courtComplexValue.getDxRef());
		court.setPoliceForceCode(courtComplexValue.getPoliceForceCode());
		
		court.setFlRepSort(courtComplexValue.getFlRepSort());
		court.setCourtStartTime(courtComplexValue.getCourtStartTime());
		court.setWlRepSort(courtComplexValue.getWlRepSort());
		court.setWlRepPeriod(courtComplexValue.getWlRepPeriod());
		court.setWlRepTime(courtComplexValue.getWlRepTime());
		court.setWlFreeText(courtComplexValue.getWlFreeText());
			
		court.setTier(courtComplexValue.getTier());
		court.setCountyLocCode(courtComplexValue.getCountyLocCode());
		court.setUpdated(userDisplayName);

		// --- Update address for Court ---
		XhbAddress courtAddress = XhbAddressBeanHelper2.findByPrimaryKey(courtComplexValue.getAddressId());
		courtAddress.setAddress1(courtComplexValue.getAddress1());
		courtAddress.setAddress2(courtComplexValue.getAddress2());
		courtAddress.setAddress3(courtComplexValue.getAddress3());
		courtAddress.setAddress4(courtComplexValue.getAddress4());
		courtAddress.setCounty(courtComplexValue.getCounty());
		courtAddress.setCountry(courtComplexValue.getCountry());
		courtAddress.setPostcode(courtComplexValue.getPostcode());
		courtAddress.setTown(courtComplexValue.getTown());
		courtAddress.setLastUpdatedBy(userDisplayName);

	}
}
